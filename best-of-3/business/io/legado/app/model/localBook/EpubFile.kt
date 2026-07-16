package io.legado.app.model.localBook

import io.legado.app.data.entities.Book
import io.legado.app.data.entities.BookChapter
import org.jsoup.Jsoup
import java.io.File
import java.util.zip.ZipFile

/**
 * EPUB chapter list:
 * 1. OPF spine order (primary)
 * 2. NCX / nav.xhtml titles merged when available
 * 3. Fallback: enumerate html/xhtml entries
 */
object EpubFile {

    fun getChapterList(book: Book): ArrayList<BookChapter> {
        val file = book.localFile()
        if (!file.isFile) return arrayListOf()
        return try {
            ZipFile(file).use { zf ->
                val opfPath = findOpfPath(zf)
                val spine = if (opfPath != null) parseSpine(zf, opfPath) else emptyList()
                val tocTitles = if (opfPath != null) parseTocTitles(zf, opfPath) else emptyMap()
                val list = ArrayList<BookChapter>()
                if (spine.isNotEmpty()) {
                    spine.forEachIndexed { i, href ->
                        val title = tocTitles[normalizeHref(href)]
                            ?: tocTitles.entries.firstOrNull { normalizeHref(href).endsWith(it.key) }?.value
                            ?: href.substringAfterLast('/').substringBeforeLast('.')
                        list += BookChapter(
                            url = href,
                            title = title,
                            bookUrl = book.bookUrl,
                            index = i,
                            resourceUrl = resolveInZip(opfPath!!, href)
                        )
                    }
                } else {
                    zf.entries().asSequence()
                        .map { it.name }
                        .filter { it.endsWith(".xhtml", true) || it.endsWith(".html", true) || it.endsWith(".htm", true) }
                        .filter { !it.contains("nav", true) && !it.contains("toc", true) }
                        .sorted()
                        .forEachIndexed { i, name ->
                            list += BookChapter(
                                url = name,
                                title = name.substringAfterLast('/').substringBeforeLast('.'),
                                bookUrl = book.bookUrl,
                                index = i,
                                resourceUrl = name
                            )
                        }
                }
                book.totalChapterNum = list.size
                book.latestChapterTitle = list.lastOrNull()?.title
                // cover from metadata
                if (book.coverUrl.isNullOrBlank() && opfPath != null) {
                    book.coverUrl = parseCover(zf, opfPath)
                }
                if (book.name.isBlank() && opfPath != null) {
                    parseMeta(zf, opfPath)?.let { (n, a) ->
                        if (n.isNotBlank()) book.name = n
                        if (a.isNotBlank() && book.author.isBlank()) book.author = a
                    }
                }
                list
            }
        } catch (_: Exception) {
            arrayListOf()
        }
    }

    fun getContent(book: Book, chapter: BookChapter): String? {
        val file = book.localFile()
        if (!file.isFile) return null
        val name = chapter.resourceUrl ?: chapter.url
        return try {
            ZipFile(file).use { zf ->
                val e = zf.getEntry(name)
                    ?: zf.entries().asSequence().firstOrNull { it.name.endsWith(name.substringAfterLast('/')) }
                    ?: return null
                val html = zf.getInputStream(e).bufferedReader().readText()
                // strip scripts/styles for cleaner body
                val doc = Jsoup.parse(html)
                doc.select("script, style").remove()
                doc.body()?.html() ?: html
            }
        } catch (_: Exception) {
            null
        }
    }

    private fun findOpfPath(zf: ZipFile): String? {
        val container = zf.getEntry("META-INF/container.xml") ?: return null
        val xml = zf.getInputStream(container).bufferedReader().readText()
        val doc = Jsoup.parse(xml, "", org.jsoup.parser.Parser.xmlParser())
        return doc.selectFirst("rootfile")?.attr("full-path")?.takeIf { it.isNotBlank() }
    }

    private fun parseSpine(zf: ZipFile, opfPath: String): List<String> {
        val opf = readZip(zf, opfPath) ?: return emptyList()
        val doc = Jsoup.parse(opf, "", org.jsoup.parser.Parser.xmlParser())
        val idToHref = doc.select("manifest item").associate {
            it.attr("id") to it.attr("href")
        }
        return doc.select("spine itemref").mapNotNull { ref ->
            idToHref[ref.attr("idref")]
        }
    }

    private fun parseTocTitles(zf: ZipFile, opfPath: String): Map<String, String> {
        val map = linkedMapOf<String, String>()
        val opf = readZip(zf, opfPath) ?: return map
        val doc = Jsoup.parse(opf, "", org.jsoup.parser.Parser.xmlParser())
        // NCX
        val ncxHref = doc.select("manifest item").firstOrNull {
            it.attr("media-type").contains("ncx", true) || it.attr("href").endsWith(".ncx", true)
        }?.attr("href")
        if (!ncxHref.isNullOrBlank()) {
            val ncxPath = resolveInZip(opfPath, ncxHref)
            readZip(zf, ncxPath)?.let { ncxXml ->
                val ncx = Jsoup.parse(ncxXml, "", org.jsoup.parser.Parser.xmlParser())
                ncx.select("navPoint").forEach { np ->
                    val title = np.selectFirst("navLabel text")?.text()?.trim().orEmpty()
                    val src = np.selectFirst("content")?.attr("src")?.substringBefore('#')?.trim().orEmpty()
                    if (title.isNotEmpty() && src.isNotEmpty()) {
                        map[normalizeHref(src)] = title
                        map[normalizeHref(resolveInZip(ncxPath, src))] = title
                    }
                }
            }
        }
        // EPUB3 nav
        val navHref = doc.select("manifest item").firstOrNull {
            it.attr("properties").contains("nav") || it.attr("href").contains("nav", true)
        }?.attr("href")
        if (!navHref.isNullOrBlank()) {
            val navPath = resolveInZip(opfPath, navHref)
            readZip(zf, navPath)?.let { navHtml ->
                val nav = Jsoup.parse(navHtml)
                nav.select("nav[epub:type=toc] a, nav a").forEach { a ->
                    val title = a.text().trim()
                    val href = a.attr("href").substringBefore('#').trim()
                    if (title.isNotEmpty() && href.isNotEmpty()) {
                        map[normalizeHref(href)] = title
                        map[normalizeHref(resolveInZip(navPath, href))] = title
                    }
                }
            }
        }
        return map
    }

    private fun parseCover(zf: ZipFile, opfPath: String): String? {
        val opf = readZip(zf, opfPath) ?: return null
        val doc = Jsoup.parse(opf, "", org.jsoup.parser.Parser.xmlParser())
        val coverId = doc.selectFirst("meta[name=cover]")?.attr("content")
        val href = if (!coverId.isNullOrBlank()) {
            doc.select("manifest item").firstOrNull { it.attr("id") == coverId }?.attr("href")
        } else {
            doc.select("manifest item").firstOrNull {
                it.attr("properties").contains("cover-image") ||
                    it.attr("media-type").startsWith("image/")
            }?.attr("href")
        }
        return href?.let { resolveInZip(opfPath, it) }
    }

    private fun parseMeta(zf: ZipFile, opfPath: String): Pair<String, String>? {
        val opf = readZip(zf, opfPath) ?: return null
        val doc = Jsoup.parse(opf, "", org.jsoup.parser.Parser.xmlParser())
        val title = doc.selectFirst("metadata title, dc|title, title")?.text().orEmpty()
        val author = doc.selectFirst("metadata creator, dc|creator, creator")?.text().orEmpty()
        return title to author
    }

    private fun readZip(zf: ZipFile, path: String): String? {
        val e = zf.getEntry(path) ?: zf.getEntry(path.replace('\\', '/')) ?: return null
        return zf.getInputStream(e).bufferedReader().readText()
    }

    private fun resolveInZip(basePath: String, href: String): String {
        if (href.startsWith("/")) return href.trimStart('/')
        val baseDir = File(basePath).parent?.replace('\\', '/') ?: ""
        if (baseDir.isEmpty()) return href
        // simple normalize
        val joined = "$baseDir/$href"
        val parts = mutableListOf<String>()
        for (p in joined.split('/')) {
            when (p) {
                "", "." -> {}
                ".." -> if (parts.isNotEmpty()) parts.removeAt(parts.lastIndex)
                else -> parts += p
            }
        }
        return parts.joinToString("/")
    }

    private fun normalizeHref(h: String): String =
        h.substringAfterLast('/').substringBefore('#').lowercase()
}
