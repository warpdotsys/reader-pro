package io.legado.app.model.webBook

import io.legado.app.data.entities.Book
import io.legado.app.data.entities.BookChapter
import io.legado.app.data.entities.BookSource
import io.legado.app.exception.TocEmptyException
import io.legado.app.model.DebugLog
import io.legado.app.model.analyzeRule.AnalyzeRule
import io.legado.app.model.analyzeRule.AnalyzeUrl

object BookChapterList {
    suspend fun analyzeChapterList(
        book: Book,
        body: String?,
        bookSource: BookSource,
        baseUrl: String,
        redirectUrl: String,
        debugLog: DebugLog? = null
    ): List<BookChapter> {
        val tocRule = bookSource.ruleToc ?: throw TocEmptyException("无目录规则")
        var html = body
        var url = baseUrl.ifBlank { book.tocUrl.ifBlank { book.bookUrl } }
        if (html.isNullOrBlank()) {
            val analyzeUrl = AnalyzeUrl(mUrl = url, source = bookSource, ruleData = book, debugLog = debugLog)
            html = analyzeUrl.getStrResponseAwait().body
            url = analyzeUrl.finalUrl
        }
        val analyze = AnalyzeRule(book, bookSource, debugLog)
        analyze.setContent(html ?: "", url)
        val listRule = tocRule.chapterList ?: throw TocEmptyException("目录列表规则为空")
        val elements = analyze.getElements(listRule)
        val chapters = ArrayList<BookChapter>()
        elements.forEachIndexed { i, el ->
            analyze.setContent(el, url)
            val title = tocRule.chapterName?.let { analyze.getString(it) } ?: "第${i + 1}章"
            val chapterUrl = tocRule.chapterUrl?.let { analyze.getString(it, isUrl = true) } ?: url
            chapters += BookChapter(url = chapterUrl, title = title, bookUrl = book.bookUrl, index = i)
        }
        // next page
        var next = tocRule.nextTocUrl?.let { AnalyzeRule(book, bookSource, debugLog).setContent(html ?: "", url).getString(it, isUrl = true) }
        var guard = 0
        while (!next.isNullOrBlank() && guard++ < 50) {
            val au = AnalyzeUrl(mUrl = next, source = bookSource, ruleData = book, debugLog = debugLog)
            val page = au.getStrResponseAwait().body ?: break
            val ar = AnalyzeRule(book, bookSource, debugLog).setContent(page, au.finalUrl)
            ar.getElements(listRule).forEach { el ->
                ar.setContent(el, au.finalUrl)
                val title = tocRule.chapterName?.let { ar.getString(it) } ?: "章节"
                val chapterUrl = tocRule.chapterUrl?.let { ar.getString(it, isUrl = true) } ?: au.finalUrl
                chapters += BookChapter(url = chapterUrl, title = title, bookUrl = book.bookUrl, index = chapters.size)
            }
            next = tocRule.nextTocUrl?.let { ar.getString(it, isUrl = true) }
            if (next == au.finalUrl) break
        }
        if (chapters.isEmpty()) throw TocEmptyException()
        book.totalChapterNum = chapters.size
        book.latestChapterTitle = chapters.last().title
        return chapters
    }
}
