/** Business rewrite from reader-pro-3.2.14.jar — phase6. */

package io.legado.app.model.webBook

import io.legado.app.data.entities.Book
import io.legado.app.data.entities.BookChapter
import io.legado.app.data.entities.BookSource
import io.legado.app.data.entities.SearchBook
import io.legado.app.model.DebugLog

class WebBook(
    private val bookSourceStr: String,
    private val debugLog: Boolean = false,
    debugLogger: DebugLog? = null,
    private val userNameSpace: String = "default"
) {
    var debugLogger: DebugLog? = debugLogger

    private val source: BookSource by lazy {
        BookSource.fromJson(bookSourceStr).getOrThrow().also {
            it.setUserNameSpace(userNameSpace)
        }
    }

    fun getBookSource(): BookSource = source

    suspend fun searchBook(key: String, page: Int = 1): List<SearchBook> {
        debugLogger?.log(source.bookSourceUrl, "搜索: $key page=$page")
        return BookList.searchBook(source, key, page, debugLogger)
    }

    suspend fun exploreBook(url: String, page: Int = 1): List<SearchBook> {
        debugLogger?.log(source.bookSourceUrl, "发现: $url page=$page")
        return BookList.exploreBook(source, url, page, debugLogger)
    }

    suspend fun getBookInfo(bookUrl: String): Book {
        debugLogger?.log(source.bookSourceUrl, "详情: $bookUrl")
        return BookInfo.getBookInfo(source, bookUrl, debugLogger)
    }

    suspend fun getChapterList(book: Book): List<BookChapter> {
        debugLogger?.log(source.bookSourceUrl, "目录: ${book.bookUrl}")
        return BookChapterList.analyzeChapterList(
            book = book,
            body = null,
            bookSource = source,
            baseUrl = book.tocUrl.ifEmpty { book.bookUrl },
            redirectUrl = book.tocUrl.ifEmpty { book.bookUrl },
            debugLog = debugLogger
        )
    }

    suspend fun getBookContent(
        book: Book,
        chapter: BookChapter,
        nextChapterUrl: String? = null
    ): String {
        debugLogger?.log(source.bookSourceUrl, "正文: ${chapter.title}")
        return BookContent.analyzeContent(
            book = book,
            bookChapter = chapter,
            bookSource = source,
            baseUrl = chapter.url,
            redirectUrl = chapter.url,
            nextChapterUrl = nextChapterUrl,
            debugLog = debugLogger
        )
    }
}
