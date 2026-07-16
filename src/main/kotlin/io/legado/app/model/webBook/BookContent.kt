package io.legado.app.model.webBook

import io.legado.app.data.entities.Book
import io.legado.app.data.entities.BookChapter
import io.legado.app.data.entities.BookSource
import io.legado.app.model.DebugLog
import io.legado.app.model.analyzeRule.AnalyzeRule
import io.legado.app.model.analyzeRule.AnalyzeUrl
import io.legado.app.utils.HtmlFormatter

object BookContent {
    suspend fun analyzeContent(
        book: Book,
        bookChapter: BookChapter,
        bookSource: BookSource,
        baseUrl: String,
        redirectUrl: String,
        nextChapterUrl: String? = null,
        debugLog: DebugLog? = null
    ): String {
        val rule = bookSource.ruleContent?.content ?: return ""
        val analyzeUrl = AnalyzeUrl(
            mUrl = baseUrl.ifBlank { bookChapter.url },
            source = bookSource,
            ruleData = book,
            chapter = bookChapter,
            debugLog = debugLog
        )
        var html = analyzeUrl.getStrResponseAwait().body ?: ""
        val analyze = AnalyzeRule(book, bookSource, debugLog)
        analyze.chapter = bookChapter
        analyze.nextChapterUrl = nextChapterUrl
        analyze.setContent(html, analyzeUrl.finalUrl)
        var content = analyze.getString(rule)
        // next content pages
        var next = bookSource.ruleContent?.nextContentUrl?.let { analyze.getString(it, isUrl = true) }
        var guard = 0
        while (!next.isNullOrBlank() && guard++ < 30) {
            val au = AnalyzeUrl(mUrl = next, source = bookSource, ruleData = book, chapter = bookChapter, debugLog = debugLog)
            html = au.getStrResponseAwait().body ?: break
            analyze.setContent(html, au.finalUrl)
            content += "\n" + analyze.getString(rule)
            val n = bookSource.ruleContent?.nextContentUrl?.let { analyze.getString(it, isUrl = true) }
            if (n == next) break
            next = n
        }
        bookSource.ruleContent?.replaceRegex?.takeIf { it.isNotBlank() }?.let {
            content = content.replace(Regex(it), "")
        }
        return HtmlFormatter.format(content)
    }
}
