package io.legado.app.model.localBook

import io.legado.app.data.entities.Book
import io.legado.app.data.entities.BookChapter
import java.util.zip.ZipFile

object EpubFile {
    fun getChapterList(book: Book): ArrayList<BookChapter> {
        val file = book.localFile()
        if (!file.isFile) return arrayListOf()
        val list = ArrayList<BookChapter>()
        try {
            ZipFile(file).use { zf ->
                val entries = zf.entries().asSequence()
                    .map { it.name }
                    .filter { it.endsWith(".xhtml", true) || it.endsWith(".html", true) || it.endsWith(".htm", true) }
                    .sorted()
                    .toList()
                entries.forEachIndexed { i, name ->
                    list += BookChapter(
                        url = name, title = name.substringAfterLast('/').substringBeforeLast('.'),
                        bookUrl = book.bookUrl, index = i, resourceUrl = name
                    )
                }
            }
        } catch (_: Exception) {
        }
        book.totalChapterNum = list.size
        return list
    }

    fun getContent(book: Book, chapter: BookChapter): String? {
        val file = book.localFile()
        if (!file.isFile) return null
        val name = chapter.resourceUrl ?: chapter.url
        return try {
            ZipFile(file).use { zf ->
                val e = zf.getEntry(name) ?: return null
                zf.getInputStream(e).bufferedReader().readText()
            }
        } catch (_: Exception) {
            null
        }
    }
}
