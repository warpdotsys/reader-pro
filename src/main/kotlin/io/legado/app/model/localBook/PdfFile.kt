package io.legado.app.model.localBook

import io.legado.app.data.entities.Book
import io.legado.app.data.entities.BookChapter
import org.apache.pdfbox.pdmodel.PDDocument

object PdfFile {
    fun getChapterList(book: Book): ArrayList<BookChapter> {
        val file = book.localFile()
        if (!file.isFile) return arrayListOf()
        val list = ArrayList<BookChapter>()
        try {
            PDDocument.load(file).use { doc ->
                repeat(doc.numberOfPages) { i ->
                    list += BookChapter(url = "page:$i", title = "第${i + 1}页", bookUrl = book.bookUrl, index = i)
                }
            }
        } catch (_: Exception) {
        }
        return list
    }

    fun getContent(book: Book, chapter: BookChapter): String? =
        "【PDF 第${chapter.index + 1}页】"
}
