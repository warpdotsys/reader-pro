package io.legado.app.model.localBook

import com.htmake.reader.utils.ExtKt
import io.legado.app.data.entities.Book
import io.legado.app.data.entities.BookChapter
import io.legado.app.utils.MD5Utils
import org.apache.pdfbox.pdmodel.PDDocument
import org.apache.pdfbox.rendering.ImageType
import org.apache.pdfbox.rendering.PDFRenderer
import org.apache.pdfbox.text.PDFTextStripper
import java.awt.image.BufferedImage
import java.io.ByteArrayOutputStream
import java.io.File
import javax.imageio.ImageIO

/**
 * PDF local book via Apache PDFBox.
 * Chapters = pages; content prefers extracted text, falls back to page placeholder.
 */
object PdfFile {
    private val docCache = java.util.concurrent.ConcurrentHashMap<String, Int>() // bookUrl -> pageCount

    fun getChapterList(book: Book): ArrayList<BookChapter> {
        val file = book.localFile()
        if (!file.isFile) return arrayListOf()
        val list = ArrayList<BookChapter>()
        try {
            PDDocument.load(file).use { doc ->
                upBookInfo(book, doc)
                val n = doc.numberOfPages
                docCache[book.bookUrl] = n
                repeat(n) { i ->
                    list += BookChapter(
                        url = "page:$i",
                        title = "第${i + 1}页",
                        bookUrl = book.bookUrl,
                        index = i
                    )
                }
                book.totalChapterNum = n
                book.latestChapterTitle = list.lastOrNull()?.title
            }
        } catch (_: Exception) {
        }
        return list
    }

    fun getContent(book: Book, chapter: BookChapter): String? {
        val file = book.localFile()
        if (!file.isFile) return null
        val pageIndex = chapter.index.coerceAtLeast(0)
        return try {
            PDDocument.load(file).use { doc ->
                if (pageIndex >= doc.numberOfPages) return "【PDF 页码越界】"
                val stripper = PDFTextStripper().apply {
                    startPage = pageIndex + 1
                    endPage = pageIndex + 1
                    sortByPosition = true
                }
                val text = stripper.getText(doc)?.trim().orEmpty()
                if (text.isNotBlank()) text
                else "【PDF 第${pageIndex + 1}页 · 无可提取文本（可能为扫描件）】"
            }
        } catch (e: Exception) {
            "【PDF 读取失败: ${e.message}】"
        }
    }

    /** Render page as JPEG bytes. width<=0 uses default DPI 144. */
    fun getPageImage(book: Book, pageIndex: Int, targetWidth: Float = 0f): ByteArray? {
        val file = book.localFile()
        if (!file.isFile) return null
        return try {
            PDDocument.load(file).use { doc ->
                if (pageIndex !in 0 until doc.numberOfPages) return null
                val renderer = PDFRenderer(doc)
                val dpi = if (targetWidth > 0) {
                    val page = doc.getPage(pageIndex)
                    val wPt = page.mediaBox.width
                    (targetWidth / wPt * 72f).coerceIn(72f, 300f)
                } else 144f
                val img: BufferedImage = renderer.renderImageWithDPI(pageIndex, dpi, ImageType.RGB)
                ByteArrayOutputStream().use { baos ->
                    ImageIO.write(img, "jpg", baos)
                    baos.toByteArray()
                }
            }
        } catch (_: Exception) {
            null
        }
    }

    fun saveCover(book: Book) {
        val bytes = getPageImage(book, 0, book.pdfImageWidth.takeIf { it > 0 } ?: 600f) ?: return
        try {
            val ns = book.namespace ?: book.getUserNameSpace()
            val rel = "assets/$ns/covers/${MD5Utils.md5Encode16(book.bookUrl)}.jpg"
            val abs = File(ExtKt.getWorkDir("storage", rel))
            abs.parentFile?.mkdirs()
            if (!abs.exists()) abs.writeBytes(bytes)
            book.coverUrl = "/$rel".replace('\\', '/')
        } catch (_: Exception) {
        }
    }

    private fun upBookInfo(book: Book, doc: PDDocument) {
        try {
            val info = doc.documentInformation
            if (book.name.isBlank()) {
                book.name = info?.title?.takeIf { it.isNotBlank() }
                    ?: book.localFile().nameWithoutExtension
            }
            if (book.author.isBlank()) {
                book.author = info?.author?.orEmpty() ?: ""
            }
        } catch (_: Exception) {
            if (book.name.isBlank()) book.name = book.localFile().nameWithoutExtension
        }
    }
}
