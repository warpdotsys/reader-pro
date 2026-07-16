/** Business rewrite from reader-pro-3.2.14.jar — phase5. */

package io.legado.app.model.localBook

import io.legado.app.data.entities.Book
import io.legado.app.data.entities.BookChapter
import io.legado.app.utils.HtmlFormatter
import me.ag2s.epublib.domain.EpubBook
import me.ag2s.epublib.domain.Resource
import me.ag2s.epublib.domain.SpineReference
import me.ag2s.epublib.epub.EpubReader
import org.jsoup.Jsoup
import org.jsoup.nodes.Element
import java.nio.charset.Charset
import java.util.zip.ZipFile

/**
 * EPUB reader (epublib).
 * - getChapterList: NCX/TOC unique resources
 * - getChapterListBySpine: spine order
 * - getChapterListBySpinAndToc: spine order + TOC titles
 * - getChapterListByTocAndSpin: TOC order + spine titles
 */
class EpubFile(var book: Book) {
    private var cached: EpubBook? = null
    private val charset: Charset = Charset.forName("UTF-8")

    private fun epub(): EpubBook? {
        if (cached != null) return cached
        return try {
            EpubReader().readEpubLazy(ZipFile(book.localFile()), "utf-8").also { cached = it }
        } catch (e: Exception) {
            e.printStackTrace()
            null
        }
    }

    /** Default: prefer TOC; if empty use spine. */
    fun getChapterList(): ArrayList<BookChapter> {
        val toc = getChapterListFromToc()
        if (toc.isNotEmpty()) {
            applyMeta(toc)
            return toc
        }
        return getChapterListBySpine()
    }

    fun getChapterListFromToc(): ArrayList<BookChapter> {
        val list = ArrayList<BookChapter>()
        val e = epub() ?: return list
        val resources = e.tableOfContents?.allUniqueResources ?: return list
        resources.forEachIndexed { index, res -> list += toChapter(index, res) }
        return list
    }

    fun getChapterListBySpine(): ArrayList<BookChapter> {
        val list = ArrayList<BookChapter>()
        val e = epub() ?: return list
        val refs = e.spine?.spineReferences ?: return list
        refs.forEachIndexed { index, ref ->
            val res = ref.resource ?: return@forEachIndexed
            val ch = toChapter(index, res)
            if (index == 0 && ch.title.isEmpty()) ch.title = "封面"
            list += ch
        }
        applyMeta(list)
        return list
    }

    /**
     * Spine order is canonical reading order; fill titles from TOC map by href.
     * @param useTocTitle always prefer TOC title when present
     */
    fun getChapterListBySpinAndToc(useTocTitle: Boolean = false): ArrayList<BookChapter> {
        val toc = getChapterListFromToc()
        val spin = getChapterListBySpine()
        if (spin.isEmpty()) return toc
        if (toc.isEmpty()) return spin
        val titleMap = toc.associateBy { normalizeHref(it.url) }
        for (ch in spin) {
            val tocCh = titleMap[normalizeHref(ch.url)]
            if (tocCh != null && tocCh.title.isNotEmpty() && (useTocTitle || ch.title.isEmpty())) {
                ch.title = tocCh.title
            }
        }
        applyMeta(spin)
        return spin
    }

    /** TOC order preferred; fill empty titles from spine map. */
    fun getChapterListByTocAndSpin(useSpinTitle: Boolean = false): ArrayList<BookChapter> {
        val toc = getChapterListFromToc()
        val spin = getChapterListBySpine()
        if (toc.isEmpty()) return spin
        if (spin.isEmpty()) return toc
        val titleMap = spin.associateBy { normalizeHref(it.url) }
        for (ch in toc) {
            val spinCh = titleMap[normalizeHref(ch.url)]
            if (spinCh != null && spinCh.title.isNotEmpty() && (useSpinTitle || ch.title.isEmpty())) {
                ch.title = spinCh.title
            }
        }
        applyMeta(toc)
        return toc
    }

    fun getContent(chapter: BookChapter): String? {
        if (chapter.url.contains("titlepage.xhtml")) {
            return """<img src="cover.jpeg" />"""
        }
        val e = epub() ?: return null
        val href = chapter.url.substringBefore('#')
        val startId = chapter.url.substringAfter('#', "").ifEmpty { null }
        val endId = chapter.variable // optional end fragment stored in variable in full impl
        val elements = org.jsoup.select.Elements()
        var collecting = false
        val nextUrl = null as String? // multi-resource chapter span simplified

        for (res in e.contents) {
            val rh = res.href ?: continue
            if (normalizeHref(rh) == normalizeHref(href)) {
                elements.add(getBody(res, startId, endId))
                collecting = true
                if (nextUrl == null || normalizeHref(rh) == normalizeHref(nextUrl)) break
            } else if (collecting) {
                if (nextUrl != null && normalizeHref(rh) == normalizeHref(nextUrl)) break
                elements.add(getBody(res, null, null))
            }
        }
        if (elements.isEmpty()) {
            val res = e.resources?.getByHref(href) ?: return null
            elements.add(getBody(res, startId, endId))
        }
        var html = elements.outerHtml()
        html = Regex("""<ruby>\s?([\u4e00-\u9fa5])\s?.*?</ruby>""").replace(html, "$1")
        return HtmlFormatter.formatKeepImg(html)
    }

    /** Cover image bytes for streaming. */
    fun getCoverBytes(): ByteArray? {
        val e = epub() ?: return null
        return try {
            e.coverImage?.data
        } catch (_: Exception) {
            null
        }
    }

    fun getImageByHref(href: String): ByteArray? {
        val e = epub() ?: return null
        val ab = href.replace("../", "")
        return try {
            e.resources?.getByHref(ab)?.data
        } catch (_: Exception) {
            null
        }
    }

    private fun toChapter(index: Int, res: Resource): BookChapter {
        var title = res.title
        if (title.isNullOrEmpty()) {
            try {
                val titles = Jsoup.parse(String(res.data, charset)).getElementsByTag("title")
                if (titles.isNotEmpty()) title = titles[0].text()
            } catch (_: Exception) {
            }
        }
        return BookChapter(
            url = res.href ?: "",
            title = title ?: "",
            index = index,
            bookUrl = book.bookUrl
        )
    }

    private fun getBody(res: Resource, startFragmentId: String?, endFragmentId: String?): Element {
        val body = Jsoup.parse(String(res.data, charset)).body()
        if (!startFragmentId.isNullOrBlank()) {
            body.getElementById(startFragmentId)?.previousElementSiblings()?.remove()
        }
        if (!endFragmentId.isNullOrBlank() && endFragmentId != startFragmentId) {
            body.getElementById(endFragmentId)?.let {
                it.nextElementSiblings().remove()
                it.remove()
            }
        }
        body.select("script,style").remove()
        return body
    }

    private fun applyMeta(chapters: List<BookChapter>) {
        if (chapters.isNotEmpty()) {
            book.latestChapterTitle = chapters.last().title
            book.totalChapterNum = chapters.size
        }
        epub()?.metadata?.let { md ->
            if (book.name.isEmpty()) book.name = md.firstTitle ?: book.name
            if (book.author.isEmpty()) {
                book.author = md.authors?.firstOrNull()?.toString() ?: ""
            }
        }
    }

    private fun normalizeHref(href: String): String =
        href.substringBefore('#').replace("\\", "/").trimStart('/')

    companion object {
        @Volatile private var eFile: EpubFile? = null

        @Synchronized
        private fun getEFile(book: Book): EpubFile {
            val cur = eFile
            if (cur != null && cur.book.bookUrl == book.bookUrl) {
                cur.book = book
                return cur
            }
            return EpubFile(book).also { eFile = it }
        }

        fun getChapterList(book: Book): ArrayList<BookChapter> =
            getEFile(book).getChapterListBySpinAndToc(useTocTitle = true)

        fun getContent(book: Book, chapter: BookChapter): String? =
            getEFile(book).getContent(chapter)

        fun getCoverBytes(book: Book): ByteArray? = getEFile(book).getCoverBytes()
        fun getImage(book: Book, href: String): ByteArray? = getEFile(book).getImageByHref(href)
    }
}
