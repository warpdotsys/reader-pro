package io.legado.app.model.localBook

import io.legado.app.data.entities.Book
import io.legado.app.data.entities.BookChapter
import java.util.zip.ZipFile

object CbzFile {
    fun getChapterList(book: Book): ArrayList<BookChapter> {
        val file = book.localFile()
        if (!file.isFile) return arrayListOf()
        val list = ArrayList<BookChapter>()
        try {
            ZipFile(file).use { zf ->
                zf.entries().asSequence()
                    .map { it.name }
                    .filter { it.matches(Regex(""".*\.(jpg|jpeg|png|webp)$""", RegexOption.IGNORE_CASE)) }
                    .sorted()
                    .forEachIndexed { i, name ->
                        list += BookChapter(url = name, title = "P${i + 1}", bookUrl = book.bookUrl, index = i, resourceUrl = name)
                    }
            }
        } catch (_: Exception) {
        }
        return list
    }

    fun getContent(book: Book, chapter: BookChapter): String? =
        """<img src="${chapter.resourceUrl ?: chapter.url}"/>"""
}
