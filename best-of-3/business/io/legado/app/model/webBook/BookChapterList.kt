/**
 * Business-oriented rewrite from reader-pro-3.2.14.jar reverse engineering.
 * Source: CFR/Vineflower + manual semantic cleanup. For audit/readability.
 */

package io.legado.app.model.webBook

import io.legado.app.data.entities.Book
import io.legado.app.data.entities.BookChapter
import io.legado.app.data.entities.BookSource
import io.legado.app.data.entities.rule.TocRule
import io.legado.app.exception.TocEmptyException
import io.legado.app.model.DebugLog
import io.legado.app.model.analyzeRule.AnalyzeRule
import io.legado.app.model.analyzeRule.AnalyzeUrl

/**
 * Parse table-of-contents HTML/JSON using TocRule.
 * Manual recovery of analyzeChapterList (hard-fail in CFR, restored via Vineflower+CFR).
 */
object BookChapterList {

    suspend fun analyzeChapterList(
        book: Book,
        body: String?,
        bookSource: BookSource,
        baseUrl: String,
        redirectUrl: String,
        debugLog: DebugLog? = null
    ): List<BookChapter> {
        val tocRule: TocRule = bookSource.ruleToc
            ?: throw TocEmptyException("目录规则为空")

        var html = body
        if (html.isNullOrEmpty()) {
            val analyzeUrl = AnalyzeUrl(
                mUrl = baseUrl,
                source = bookSource,
                ruleData = book,
                headerMapF = bookSource.getHeaderMap()
            )
            html = analyzeUrl.getStrResponseAwait().body ?: ""
        }

        val analyzeRule = AnalyzeRule(book, bookSource, debugLog)
        analyzeRule.setContent(html, baseUrl)

        // chapter list elements
        val listRule = tocRule.chapterList ?: throw TocEmptyException("chapterList 规则为空")
        val elements = analyzeRule.getElements(listRule)
        if (elements.isEmpty()) throw TocEmptyException("目录为空")

        val chapters = ArrayList<BookChapter>()
        val nameRule = tocRule.chapterName
        val urlRule = tocRule.chapterUrl
        elements.forEachIndexed { index, el ->
            analyzeRule.setContent(el)
            val title = nameRule?.let { analyzeRule.getString(it) } ?: "章节${index + 1}"
            val url = urlRule?.let { analyzeRule.getString(it) } ?: baseUrl
            chapters += BookChapter(
                url = url,
                title = title,
                index = index,
                bookUrl = book.bookUrl
            )
        }

        // next URL pagination (tocRule.nextTocUrl) — fetch more pages if present
        var nextUrl = tocRule.nextTocUrl?.let {
            analyzeRule.setContent(html, baseUrl)
            analyzeRule.getString(it)
        }
        val seen = linkedSetOf(baseUrl)
        while (!nextUrl.isNullOrEmpty() && nextUrl !in seen) {
            seen += nextUrl
            val more = AnalyzeUrl(mUrl = nextUrl, source = bookSource, ruleData = book)
                .getStrResponseAwait().body ?: break
            analyzeRule.setContent(more, nextUrl)
            val moreEls = analyzeRule.getElements(listRule)
            moreEls.forEach { el ->
                analyzeRule.setContent(el)
                val title = nameRule?.let { analyzeRule.getString(it) } ?: return@forEach
                val url = urlRule?.let { analyzeRule.getString(it) } ?: nextUrl
                chapters += BookChapter(
                    url = url,
                    title = title,
                    index = chapters.size,
                    bookUrl = book.bookUrl
                )
            }
            nextUrl = tocRule.nextTocUrl?.let {
                analyzeRule.setContent(more, nextUrl)
                analyzeRule.getString(it)
            }
        }

        if (chapters.isEmpty()) throw TocEmptyException("目录为空")
        // reverse if tocRule need reverse
        return chapters
    }
}
