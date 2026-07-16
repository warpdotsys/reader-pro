/**
 * Business-oriented rewrite from reader-pro-3.2.14.jar reverse engineering.
 * Source: CFR/Vineflower + manual semantic cleanup. For audit/readability.
 */

package io.legado.app.model.webBook

import io.legado.app.data.entities.Book
import io.legado.app.data.entities.BookChapter
import io.legado.app.data.entities.BookSource
import io.legado.app.model.DebugLog
import io.legado.app.model.analyzeRule.AnalyzeRule
import io.legado.app.model.analyzeRule.AnalyzeUrl

/** Fetch and parse chapter HTML → plain/content HTML via ContentRule. */
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
        val contentRule = bookSource.ruleContent
            ?: return ""
        val analyzeUrl = AnalyzeUrl(
            mUrl = bookChapter.url.ifEmpty { baseUrl },
            source = bookSource,
            ruleData = book,
            chapter = bookChapter
        )
        val resp = analyzeUrl.getStrResponseAwait()
        val html = resp.body ?: ""
        val rule = AnalyzeRule(book, bookSource, debugLog)
        rule.setContent(html, redirectUrl.ifEmpty { baseUrl })
        var content = contentRule.content?.let { rule.getString(it) } ?: html

        // next-page content concatenation
        var nextUrl = contentRule.nextContentUrl?.let { rule.getString(it) }
        val seen = linkedSetOf(bookChapter.url)
        while (!nextUrl.isNullOrEmpty() && nextUrl !in seen) {
            if (nextUrl == nextChapterUrl) break
            seen += nextUrl
            val more = AnalyzeUrl(mUrl = nextUrl, source = bookSource, ruleData = book)
                .getStrResponseAwait().body ?: break
            rule.setContent(more, nextUrl)
            val part = contentRule.content?.let { rule.getString(it) } ?: ""
            content += "\n" + part
            nextUrl = contentRule.nextContentUrl?.let { rule.getString(it) }
        }

        // replaceRegex optional
        contentRule.replaceRegex?.takeIf { it.isNotEmpty() }?.let { reg ->
            // format: regex##replacement multi-line rules
            reg.split("\n").forEach { line ->
                val parts = line.split("##", limit = 2)
                if (parts.size == 2) {
                    content = content.replace(Regex(parts[0]), parts[1])
                }
            }
        }
        return content.trim()
    }
}
