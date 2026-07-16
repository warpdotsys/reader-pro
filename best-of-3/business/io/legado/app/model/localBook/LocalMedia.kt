/** Business rewrite from reader-pro-3.2.14.jar — phase5. */

package io.legado.app.model.localBook

import io.legado.app.data.entities.Book
import io.legado.app.data.entities.BookChapter
import org.apache.pdfbox.pdmodel.PDDocument
import org.apache.pdfbox.rendering.ImageType
import org.apache.pdfbox.rendering.PDFRenderer
import java.awt.image.BufferedImage
import java.io.ByteArrayOutputStream
import java.util.zip.ZipFile
import javax.imageio.ImageIO

/**
 * Stream page/image bytes for CBZ/PDF/EPUB cover to HTTP layer.
 */
object LocalMedia {

    fun getChapterImage(book: Book, chapter: BookChapter): ByteArray? {
        return when {
            book.isCbz -> cbzImage(book, chapter.url)
            book.isPdf -> pdfPageImage(book, chapter)
            book.isEpub -> {
                // content may be img only — try resolve as resource
                EpubFile.getImage(book, chapter.url)
            }
            else -> null
        }
    }

    fun getCover(book: Book): ByteArray? {
        if (book.isEpub) return EpubFile.getCoverBytes(book)
        if (book.isCbz) {
            val chapters = CbzFile.getChapterList(book)
            val first = chapters.firstOrNull()?.url ?: return null
            return cbzImage(book, first)
        }
        if (book.isPdf) {
            return pdfPageImage(book, BookChapter(url = "page:1", index = 0))
        }
        return null
    }

    private fun cbzImage(book: Book, entryName: String): ByteArray? {
        return try {
            ZipFile(book.localFile()).use { zf ->
                val e = zf.getEntry(entryName) ?: return null
                zf.getInputStream(e).readBytes()
            }
        } catch (_: Exception) {
            null
        }
    }

    private fun pdfPageImage(book: Book, chapter: BookChapter): ByteArray? {
        val page = chapter.url.removePrefix("page:").toIntOrNull() ?: (chapter.index + 1)
        return try {
            PDDocument.load(book.localFile()).use { doc ->
                val renderer = PDFRenderer(doc)
                val img: BufferedImage = renderer.renderImageWithDPI(page - 1, 120f, ImageType.RGB)
                val baos = ByteArrayOutputStream()
                ImageIO.write(img, "jpg", baos)
                baos.toByteArray()
            }
        } catch (_: Exception) {
            null
        }
    }

    fun guessContentType(pathOrUrl: String): String = when {
        pathOrUrl.endsWith(".png", true) -> "image/png"
        pathOrUrl.endsWith(".gif", true) -> "image/gif"
        pathOrUrl.endsWith(".webp", true) -> "image/webp"
        pathOrUrl.endsWith(".jpg", true) || pathOrUrl.endsWith(".jpeg", true) -> "image/jpeg"
        pathOrUrl.startsWith("page:") -> "image/jpeg"
        else -> "application/octet-stream"
    }
}
