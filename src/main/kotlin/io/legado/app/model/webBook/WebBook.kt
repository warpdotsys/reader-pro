package io.legado.app.model.webBook

import io.legado.app.data.entities.Book
import io.legado.app.data.entities.BookChapter
import io.legado.app.data.entities.BookSource
import io.legado.app.data.entities.SearchBook
import io.legado.app.model.ConsoleDebugLog
import io.legado.app.model.DebugLog

class WebBook(
    private val bookSourceStr: String,
    private val debugLog: Boolean = false,
    debugLogger: DebugLog? = null,
    private val userNameSpace: String = "default"
) {
    var debugLogger: DebugLog? = debugLogger ?: if (debugLog) ConsoleDebugLog else null

    private val source: BookSource by lazy {
        BookSource.fromJson(bookSourceStr).getOrThrow().also { it.setUserNameSpace(userNameSpace) }
    }

    fun getBookSource(): BookSource = source

    suspend fun searchBook(key: String, page: Int = 1): List<SearchBook> {
        io.legado.app.help.SourceLogin.ensureLoginIfNeeded(source, debugLogger)
        return BookList.searchBook(source, key, page, debugLogger)
    }

    suspend fun exploreBook(url: String, page: Int = 1): List<SearchBook> {
        io.legado.app.help.SourceLogin.ensureLoginIfNeeded(source, debugLogger)
        return BookList.exploreBook(source, url, page, debugLogger)
    }

    suspend fun getBookInfo(bookUrl: String): Book {
        io.legado.app.help.SourceLogin.ensureLoginIfNeeded(source, debugLogger)
        return BookInfo.getBookInfo(source, bookUrl, debugLogger)
    }

    suspend fun getChapterList(book: Book): List<BookChapter> =
        BookChapterList.analyzeChapterList(
            book = book, body = null, bookSource = source,
            baseUrl = book.tocUrl.ifEmpty { book.bookUrl },
            redirectUrl = book.tocUrl.ifEmpty { book.bookUrl },
            debugLog = debugLogger
        )

    suspend fun getBookContent(book: Book, chapter: BookChapter, nextChapterUrl: String? = null): String {
        // 原版使用 chapter.getAbsoluteURL()：相对章节链接按 tocUrl/bookUrl 绝对化
        val absChapterUrl = io.legado.app.utils.NetworkUtils.getAbsoluteURL(
            book.tocUrl.ifBlank { book.bookUrl }, chapter.url
        )
        val absNextUrl = nextChapterUrl?.let {
            io.legado.app.utils.NetworkUtils.getAbsoluteURL(book.tocUrl.ifBlank { book.bookUrl }, it)
        }
        return BookContent.analyzeContent(
            book = book, bookChapter = chapter, bookSource = source,
            baseUrl = absChapterUrl, redirectUrl = absChapterUrl,
            nextChapterUrl = absNextUrl, debugLog = debugLogger
        )
    }
}
