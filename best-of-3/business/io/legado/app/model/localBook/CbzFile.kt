/** Business rewrite from reader-pro-3.2.14.jar — phase4. */

package io.legado.app.model.localBook

import io.legado.app.data.entities.Book
import io.legado.app.data.entities.BookChapter
import java.util.zip.ZipFile

/** CBZ = zip of images; each image is a "chapter" page. */
object CbzFile {
    fun getChapterList(book: Book): ArrayList<BookChapter> {
        val list = ArrayList<BookChapter>()
        val f = book.localFile()
        if (!f.isFile) return list
        ZipFile(f).use { zf ->
            val names = zf.entries().asSequence()
                .filter { !it.isDirectory && it.name.matches(Regex(".*\\.(jpe?g|png|webp|gif)$", RegexOption.IGNORE_CASE)) }
                .map { it.name }
                .sorted()
                .toList()
            names.forEachIndexed { i, name ->
                list += BookChapter(
                    url = name,
                    title = "第${i + 1}页",
                    index = i,
                    bookUrl = book.bookUrl
                )
            }
        }
        book.totalChapterNum = list.size
        book.latestChapterTitle = list.lastOrNull()?.title
        return list
    }

    fun getContent(book: Book, chapter: BookChapter): String? {
        // return img tag pointing at zip entry path for web layer to resolve
        return """<img src="${chapter.url}" />"""
    }
}
