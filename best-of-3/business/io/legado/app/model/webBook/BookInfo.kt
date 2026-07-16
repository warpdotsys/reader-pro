/**
 * Business-oriented rewrite from reader-pro-3.2.14.jar reverse engineering.
 * Source: CFR/Vineflower + manual semantic cleanup. For audit/readability.
 */

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
        val infoRule = bookSource.ruleBookInfo
        val analyzeUrl = AnalyzeUrl(mUrl = bookUrl, source = bookSource)
        val html = analyzeUrl.getStrResponseAwait().body ?: ""
        val rule = AnalyzeRule(null, bookSource, debugLog)
        rule.setContent(html, bookUrl)
        val book = Book(
            bookUrl = bookUrl,
            origin = bookSource.bookSourceUrl,
            originName = bookSource.bookSourceName,
            name = infoRule?.name?.let { rule.getString(it) } ?: "",
            author = infoRule?.author?.let { rule.getString(it) } ?: "",
            kind = infoRule?.kind?.let { rule.getString(it) },
            coverUrl = infoRule?.coverUrl?.let { rule.getString(it) },
            intro = infoRule?.intro?.let { rule.getString(it) },
            tocUrl = infoRule?.tocUrl?.let { rule.getString(it) } ?: bookUrl,
        )
        return book
    }
}
