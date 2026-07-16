package io.legado.app.model.localBook

import io.legado.app.data.entities.Book
import io.legado.app.data.entities.BookChapter

/**
 * Unified local media (image page) access for PDF / CBZ / EPUB cover.
 */
object LocalMedia {
    fun getChapterImage(book: Book, chapter: BookChapter): ByteArray? = when {
        book.isPdf -> PdfFile.getPageImage(book, chapter.index, book.pdfImageWidth)
        book.isCbz -> CbzFile.getImage(book, chapter.resourceUrl ?: chapter.url)
        else -> null
    }

    fun getCoverBytes(book: Book): ByteArray? {
        return when {
            book.isPdf -> PdfFile.getPageImage(book, 0, book.pdfImageWidth.takeIf { it > 0 } ?: 600f)
            book.isCbz -> {
                val chapters = CbzFile.getChapterList(book)
                val first = chapters.firstOrNull() ?: return null
                CbzFile.getImage(book, first.resourceUrl ?: first.url)
            }
            else -> null
        }
    }
}
