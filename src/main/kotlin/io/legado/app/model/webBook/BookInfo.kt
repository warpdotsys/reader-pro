package io.legado.app.model.webBook

import io.legado.app.data.entities.Book
import io.legado.app.data.entities.BookSource
import io.legado.app.model.DebugLog
import io.legado.app.model.analyzeRule.AnalyzeRule
import io.legado.app.model.analyzeRule.AnalyzeUrl

object BookInfo {
    suspend fun getBookInfo(
        bookSource: BookSource,
        bookUrl: String,
        debugLog: DebugLog? = null
    ): Book {
        val analyzeUrl = AnalyzeUrl(mUrl = bookUrl, source = bookSource, debugLog = debugLog)
        val html = analyzeUrl.getStrResponseAwait().body ?: ""
        val book = Book(bookUrl = bookUrl, origin = bookSource.bookSourceUrl, originName = bookSource.bookSourceName)
        val rule = bookSource.ruleBookInfo ?: return book
        val analyze = AnalyzeRule(book, bookSource, debugLog)
        analyze.setContent(html, analyzeUrl.finalUrl)
        rule.name?.let { book.name = analyze.getString(it) }
        rule.author?.let { book.author = analyze.getString(it) }
        rule.kind?.let { book.kind = analyze.getString(it) }
        rule.coverUrl?.let { book.coverUrl = analyze.getString(it, isUrl = true) }
        rule.intro?.let { book.intro = analyze.getString(it) }
        rule.tocUrl?.let { book.tocUrl = analyze.getString(it, isUrl = true) }
        if (book.tocUrl.isBlank()) book.tocUrl = bookUrl
        return book
    }
}
