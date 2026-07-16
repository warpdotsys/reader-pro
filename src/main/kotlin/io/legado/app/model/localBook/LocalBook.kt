package io.legado.app.model.localBook

import io.legado.app.data.entities.Book
import io.legado.app.data.entities.BookChapter

object LocalBook {
    fun getChapterList(book: Book): ArrayList<BookChapter> = when {
        book.isEpub -> EpubFile.getChapterList(book)
        book.isUmd -> UmdFile.getChapterList(book)
        book.isCbz -> CbzFile.getChapterList(book)
        book.isPdf -> PdfFile.getChapterList(book)
        else -> TextFile(book).getChapterList()
    }

    fun getContent(book: Book, chapter: BookChapter): String? = when {
        book.isEpub -> EpubFile.getContent(book, chapter)
        book.isUmd -> UmdFile.getContent(book, chapter)
        book.isCbz -> CbzFile.getContent(book, chapter)
        book.isPdf -> PdfFile.getContent(book, chapter)
        else -> TextFile(book).getContent(chapter)
    }
}
