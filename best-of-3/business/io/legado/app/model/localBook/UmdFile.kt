/** Business rewrite from reader-pro-3.2.14.jar — phase4. */

package io.legado.app.model.localBook

import io.legado.app.data.entities.Book
import io.legado.app.data.entities.BookChapter
import me.ag2s.umdlib.umd.UmdReader
import java.io.FileInputStream

/** UMD via umdlib (same as jar). */
object UmdFile {
    fun getChapterList(book: Book): ArrayList<BookChapter> {
        val list = ArrayList<BookChapter>()
        try {
            val umd = UmdReader().read(FileInputStream(book.localFile()))
            val titles = umd?.chapters?.titles ?: return list
            titles.forEachIndexed { i, t ->
                list += BookChapter(
                    url = i.toString(),
                    title = t?.toString() ?: "章节${i + 1}",
                    index = i,
                    bookUrl = book.bookUrl
                )
            }
            book.totalChapterNum = list.size
            book.latestChapterTitle = list.lastOrNull()?.title
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return list
    }

    fun getContent(book: Book, chapter: BookChapter): String? {
        return try {
            val umd = UmdReader().read(FileInputStream(book.localFile()))
            val idx = chapter.index
            umd?.chapters?.getContent(idx)?.toString()
        } catch (_: Exception) {
            null
        }
    }
}
