package io.legado.app.model.localBook

import io.legado.app.data.entities.Book
import io.legado.app.data.entities.BookChapter
import io.legado.app.exception.TocEmptyException
import io.legado.app.help.BookHelp
import io.legado.app.utils.FileUtils
import java.io.FileInputStream
import java.io.FileNotFoundException
import java.io.InputStream
import java.util.regex.Pattern

object LocalBook {

    private val nameAuthorPatterns = arrayOf(
        Pattern.compile("(.*?)\u300a([^\u300a\u300b]+)\u300b.*?\u4f5c\u8005\uff1a(.*)"),
        Pattern.compile("(.*?)\u300a([^\u300a\u300b]+)\u300b(.*)"),
        Pattern.compile("(^)(.+) \u4f5c\u8005\uff1a(.+)$"),
        Pattern.compile("(^)(.+) by (.+)$")
    )

    @Throws(FileNotFoundException::class, SecurityException::class)
    fun getBookInputStream(book: Book): InputStream {
        val file = book.getLocalFile()
        if (file.exists()) {
            return FileInputStream(file)
        }
        throw FileNotFoundException(book.name + " \u6587\u4ef6\u4e0d\u5b58\u5728")
    }

    @Throws(Exception::class)
    fun getChapterList(book: Book): ArrayList<BookChapter> {
        val chapters = when {
            book.isEpub() -> EpubFile.getChapterList(book)
            book.isUmd() -> UmdFile.getChapterList(book)
            book.isCbz() -> CbzFile.getChapterList(book)
            book.isPdf() -> PdfFile.getChapterList(book)
            else -> TextFile.getChapterList(book)
        }
        if (chapters.isEmpty()) {
            throw TocEmptyException("Chapterlist is empty  " + book.getLocalFile())
        }
        return chapters
    }

    fun getContent(book: Book, chapter: BookChapter): String? {
        return when {
            book.isEpub() -> EpubFile.getContent(book, chapter)
            book.isUmd() -> UmdFile.getContent(book, chapter)
            book.isCbz() -> CbzFile.getContent(book, chapter)
            book.isPdf() -> PdfFile.getContent(book, chapter)
            else -> TextFile.getContent(book, chapter)
        }
    }

    fun analyzeNameAuthor(fileName: String): Pair<String, String> {
        val tempFileName = fileName.substringBeforeLast(".")
        for (pattern in nameAuthorPatterns) {
            pattern.matcher(tempFileName).takeIf { it.find() }?.run {
                val name = group(2)!!
                val group1 = group(1) ?: ""
                val group3 = group(3) ?: ""
                val author = BookHelp.formatBookAuthor(group1 + group3)
                return Pair(name, author)
            }
        }
        val name = BookHelp.formatBookName(tempFileName)
        val author = BookHelp.formatBookAuthor(tempFileName.replace(name, ""))
            .takeIf { it.length != tempFileName.length } ?: ""
        return Pair(name, author)
    }

    fun deleteBook(book: Book) {
        kotlin.runCatching {
            var bookFile = book.getLocalFile()
            if ((book.isLocalTxt() || book.isUmd()) && bookFile.exists()) {
                bookFile.delete()
            }
            if (book.isEpub()) {
                bookFile = bookFile.parentFile
                if (bookFile.exists()) {
                    FileUtils.delete(bookFile, true)
                }
            }
        }
    }
}
