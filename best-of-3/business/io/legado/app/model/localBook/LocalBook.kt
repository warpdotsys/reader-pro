/** Business rewrite from reader-pro-3.2.14.jar — phase4. */

package io.legado.app.model.localBook

import io.legado.app.data.entities.Book
import io.legado.app.data.entities.BookChapter
import io.legado.app.exception.TocEmptyException
import java.io.File
import java.io.FileInputStream
import java.io.FileNotFoundException
import java.io.InputStream
import java.util.regex.Pattern

object LocalBook {
    private val nameAuthorPatterns = arrayOf(
        Pattern.compile("(?i)(.*?)[\\[【](.+?)[\\]】]"),
        Pattern.compile("(?i)(.*?)-{1,2}(.+)"),
        Pattern.compile("(?i)(.*?)_{1,2}(.+)"),
        Pattern.compile("(?i)(.*?)\\s+作者[:：]?\\s*(.+)")
    )

    fun getBookInputStream(book: Book): InputStream {
        val file = book.localFile()
        if (!file.exists()) throw FileNotFoundException("${book.name} 文件不存在")
        return FileInputStream(file)
    }

    fun getChapterList(book: Book): ArrayList<BookChapter> {
        val chapters: ArrayList<BookChapter> = when {
            book.isEpub -> EpubFile.getChapterList(book)
            book.isUmd -> UmdFile.getChapterList(book)
            book.isCbz -> CbzFile.getChapterList(book)
            book.isPdf -> PdfFile.getChapterList(book)
            else -> TextFile(book).getChapterList()
        }
        if (chapters.isEmpty()) throw TocEmptyException("Chapterlist is empty  ${book.localFile()}")
        return chapters
    }

    fun getContent(book: Book, chapter: BookChapter): String? = when {
        book.isEpub -> EpubFile.getContent(book, chapter)
        book.isUmd -> UmdFile.getContent(book, chapter)
        book.isCbz -> CbzFile.getContent(book, chapter)
        book.isPdf -> PdfFile.getContent(book, chapter)
        else -> TextFile(book).getContent(chapter)
    }

    fun analyzeNameAuthor(fileName: String): Pair<String, String> {
        val temp = fileName.substringBeforeLast('.')
        for (p in nameAuthorPatterns) {
            val m = p.matcher(temp)
            if (m.find()) {
                val g1 = m.group(1)?.trim().orEmpty()
                val g2 = m.group(2)?.trim().orEmpty()
                return if (g2.isNotEmpty()) g1 to g2 else temp to ""
            }
        }
        return temp to ""
    }

    fun deleteBook(book: Book) {
        val f = book.localFile()
        if ((book.isLocalTxt || book.isUmd) && f.exists()) f.delete()
        if (book.isEpub) f.parentFile?.deleteRecursively()
    }
}

fun Book.localFile(): File {
    val path = when {
        bookUrl.startsWith("file:") -> bookUrl.removePrefix("file://").removePrefix("file:")
        else -> bookUrl
    }
    val base = rootDir?.let { File(it) } ?: File(".")
    val f = File(path)
    return if (f.isAbsolute) f else File(base, path)
}
