package io.legado.app.model.localBook

import io.legado.app.data.entities.Book
import io.legado.app.data.entities.BookChapter

object UmdFile {
    fun getChapterList(book: Book): ArrayList<BookChapter> =
        arrayListOf(BookChapter(url = "0", title = book.name.ifEmpty { "UMD" }, bookUrl = book.bookUrl, index = 0))

    fun getContent(book: Book, chapter: BookChapter): String? =
        "【UMD 内容请用完整 umdlib 解析；此处为可编译占位】"
}
