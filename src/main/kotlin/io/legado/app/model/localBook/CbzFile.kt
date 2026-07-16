package io.legado.app.model.localBook

import com.htmake.reader.utils.ExtKt
import io.legado.app.data.entities.Book
import io.legado.app.data.entities.BookChapter
import io.legado.app.utils.MD5Utils
import java.io.File
import java.util.zip.ZipFile
import javax.xml.parsers.DocumentBuilderFactory

/**
 * CBZ (zip of images) comic reader.
 * Natural-sorts image entries; optional ComicInfo.xml for metadata.
 */
object CbzFile {
    private val imageExt = Regex("""\.(jpe?g|png|webp|gif|bmp)$""", RegexOption.IGNORE_CASE)

    fun getChapterList(book: Book): ArrayList<BookChapter> {
        val file = book.localFile()
        if (!file.isFile) return arrayListOf()
        val list = ArrayList<BookChapter>()
        try {
            ZipFile(file).use { zf ->
                upBookInfo(book, zf)
                val names = zf.entries().asSequence()
                    .filter { !it.isDirectory && imageExt.containsMatchIn(it.name) }
                    .map { it.name }
                    .sortedWith(naturalOrder())
                    .toList()
                names.forEachIndexed { i, name ->
                    val base = File(name).nameWithoutExtension
                    list += BookChapter(
                        url = name,
                        title = base.ifBlank { "P${i + 1}" },
                        bookUrl = book.bookUrl,
                        index = i,
                        resourceUrl = name
                    )
                }
                book.totalChapterNum = list.size
                book.latestChapterTitle = list.lastOrNull()?.title
                if (list.isNotEmpty()) saveCover(book, zf, names.first())
            }
        } catch (_: Exception) {
        }
        return list
    }

    fun getContent(book: Book, chapter: BookChapter): String? {
        val src = chapter.resourceUrl ?: chapter.url
        // HTML img for web reader; actual bytes via getImage
        return """<div class="cbz-page"><img src="$src" alt="${chapter.title}"/></div>"""
    }

    fun getImage(book: Book, entryName: String): ByteArray? {
        val file = book.localFile()
        if (!file.isFile) return null
        return try {
            ZipFile(file).use { zf ->
                val entry = zf.getEntry(entryName)
                    ?: zf.entries().asSequence().firstOrNull {
                        it.name == entryName || it.name.endsWith(entryName) || File(it.name).name == File(entryName).name
                    }
                    ?: return null
                zf.getInputStream(entry).use { it.readBytes() }
            }
        } catch (_: Exception) {
            null
        }
    }

    /** Natural sort: page2 < page10 */
    fun naturalOrder(): Comparator<String> = Comparator { a, b ->
        val na = File(a).name
        val nb = File(b).name
        val re = Regex("""(\d+)|(\D+)""")
        val pa = re.findAll(na).map { it.value }.toList()
        val pb = re.findAll(nb).map { it.value }.toList()
        val n = minOf(pa.size, pb.size)
        for (i in 0 until n) {
            val x = pa[i]; val y = pb[i]
            val cmp = if (x[0].isDigit() && y[0].isDigit()) {
                x.toBigInteger().compareTo(y.toBigInteger())
            } else x.compareTo(y, ignoreCase = true)
            if (cmp != 0) return@Comparator cmp
        }
        pa.size.compareTo(pb.size)
    }

    private fun upBookInfo(book: Book, zf: ZipFile) {
        val comicInfo = zf.entries().asSequence()
            .firstOrNull { it.name.endsWith("ComicInfo.xml", true) }
        if (comicInfo != null) {
            try {
                zf.getInputStream(comicInfo).use { ins ->
                    val doc = DocumentBuilderFactory.newInstance().newDocumentBuilder().parse(ins)
                    doc.documentElement.normalize()
                    fun tag(name: String): String? {
                        val nodes = doc.getElementsByTagName(name)
                        if (nodes.length == 0) return null
                        return nodes.item(0)?.textContent?.trim()?.takeIf { it.isNotEmpty() }
                    }
                    if (book.name.isBlank()) book.name = tag("Title") ?: book.localFile().nameWithoutExtension
                    if (book.author.isBlank()) book.author = tag("Writer") ?: tag("Penciller") ?: ""
                    if (book.kind.isNullOrBlank()) book.kind = tag("Genre")
                    if (book.intro.isNullOrBlank()) book.intro = tag("Summary")
                }
            } catch (_: Exception) {
                if (book.name.isBlank()) book.name = book.localFile().nameWithoutExtension
            }
        } else if (book.name.isBlank()) {
            book.name = book.localFile().nameWithoutExtension
        }
    }

    private fun saveCover(book: Book, zf: ZipFile, firstImage: String) {
        try {
            val entry = zf.getEntry(firstImage) ?: return
            val bytes = zf.getInputStream(entry).use { it.readBytes() }
            val ns = book.namespace ?: book.getUserNameSpace()
            val ext = File(firstImage).extension.ifBlank { "jpg" }
            val rel = "assets/$ns/covers/${MD5Utils.md5Encode16(book.bookUrl)}.$ext"
            val abs = File(ExtKt.getWorkDir("storage", rel))
            abs.parentFile?.mkdirs()
            if (!abs.exists()) abs.writeBytes(bytes)
            book.coverUrl = "/$rel".replace('\\', '/')
        } catch (_: Exception) {
        }
    }
}
