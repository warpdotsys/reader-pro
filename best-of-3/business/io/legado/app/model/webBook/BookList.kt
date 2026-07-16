/** Business rewrite from reader-pro-3.2.14.jar — phase11. */

package io.legado.app.model.webBook

import io.legado.app.data.entities.BookSource
import io.legado.app.data.entities.SearchBook
import io.legado.app.data.entities.rule.ExploreRule
import io.legado.app.data.entities.rule.SearchRule
import io.legado.app.model.DebugLog
import io.legado.app.model.analyzeRule.AnalyzeRule
import io.legado.app.model.analyzeRule.AnalyzeUrl

/**
 * 搜索 / 发现列表解析。
 * explore: url 可含 {{page}}；列表规则优先 ruleExplore，字段缺省回落 ruleSearch。
 */
object BookList {

    suspend fun searchBook(
        bookSource: BookSource,
        key: String,
        page: Int = 1,
        debugLog: DebugLog? = null
    ): List<SearchBook> {
        val rule = bookSource.ruleSearch ?: return emptyList()
        val urlRule = rule.url ?: return emptyList()
        val analyzeUrl = AnalyzeUrl(
            mUrl = urlRule,
            key = key,
            page = page,
            source = bookSource
        )
        // loginCheckJs optional
        bookSource.loginCheckJs?.takeIf { it.isNotBlank() }?.let {
            // checked after response in AnalyzeUrl path; pre-login if loginUrl is JS
        }
        val html = analyzeUrl.getStrResponseAwait().body ?: return emptyList()
        return parseList(bookSource, html, analyzeUrl.finalUrl, rule, isSearch = true, debugLog)
    }

    suspend fun exploreBook(
        bookSource: BookSource,
        url: String,
        page: Int = 1,
        debugLog: DebugLog? = null
    ): List<SearchBook> {
        // url may be relative explore path; expand page
        val analyzeUrl = AnalyzeUrl(mUrl = url, page = page, source = bookSource)
        val html = analyzeUrl.getStrResponseAwait().body ?: return emptyList()
        val explore = bookSource.ruleExplore
        val search = bookSource.ruleSearch
        // build synthetic SearchRule from explore + search fallbacks
        val listRule = explore?.bookList ?: search?.bookList
        val synthetic = SearchRule(
            bookList = listRule,
            name = explore?.name ?: search?.name,
            author = explore?.author ?: search?.author,
            bookUrl = explore?.bookUrl ?: search?.bookUrl,
            coverUrl = explore?.coverUrl ?: search?.coverUrl,
            intro = explore?.intro ?: search?.intro,
            kind = explore?.kind ?: search?.kind,
            lastChapter = explore?.lastChapter ?: search?.lastChapter,
            wordCount = explore?.wordCount ?: search?.wordCount,
            updateTime = explore?.updateTime ?: search?.updateTime
        )
        return parseList(bookSource, html, analyzeUrl.finalUrl, synthetic, isSearch = false, debugLog)
    }

    /**
     * 解析 exploreUrl 文本为 (title,url) 列表。
     * 格式：标题::url  多行；或 JSON 数组。
     */
    fun parseExploreUrl(exploreUrl: String?, page: Int = 1): List<Pair<String, String>> {
        if (exploreUrl.isNullOrBlank()) return emptyList()
        val text = exploreUrl.replace("{{page}}", page.toString())
        // try JSON
        try {
            if (text.trimStart().startsWith("[")) {
                val arr = io.vertx.core.json.JsonArray(text)
                return (0 until arr.size()).mapNotNull { i ->
                    val v = arr.getValue(i)
                    when (v) {
                        is io.vertx.core.json.JsonObject -> {
                            val title = v.getString("title") ?: v.getString("name") ?: return@mapNotNull null
                            val u = v.getString("url") ?: return@mapNotNull null
                            title to u
                        }
                        is String -> {
                            val parts = v.split("::", limit = 2)
                            if (parts.size == 2) parts[0] to parts[1] else null
                        }
                        else -> null
                    }
                }
            }
        } catch (_: Exception) {
        }
        return text.lines().mapNotNull { line ->
            val t = line.trim()
            if (t.isEmpty() || t.startsWith("//")) return@mapNotNull null
            val parts = t.split("::", limit = 2)
            if (parts.size == 2) parts[0].trim() to parts[1].trim() else null
        }
    }

    private fun parseList(
        bookSource: BookSource,
        html: String,
        baseUrl: String,
        rule: SearchRule,
        isSearch: Boolean,
        debugLog: DebugLog?
    ): List<SearchBook> {
        val bookListRule = rule.bookList ?: return emptyList()
        val analyze = AnalyzeRule(null, bookSource, debugLog)
        analyze.setContent(html, baseUrl)
        // allInOne when rule starts with :
        val allInOne = bookListRule.trimStart().startsWith(":")
        val els = if (allInOne) {
            // regex list returns strings as "elements"
            analyze.getStringList(bookListRule, allInOne = true).map { it as Any }
        } else {
            analyze.getElements(bookListRule)
        }
        debugLog?.log(bookSource.bookSourceUrl, "${if (isSearch) "搜索" else "发现"}列表 ${els.size} 条")
        return els.mapNotNull { el ->
            analyze.setContent(el, baseUrl)
            val name = rule.name?.let { analyze.getString(it) }?.takeIf { it.isNotBlank() }
                ?: return@mapNotNull null
            val bookUrl = rule.bookUrl?.let { analyze.getString(it, isUrl = true) }?.ifBlank { baseUrl } ?: baseUrl
            SearchBook(
                name = name,
                author = rule.author?.let { analyze.getString(it) } ?: "",
                bookUrl = bookUrl,
                origin = bookSource.bookSourceUrl,
                originName = bookSource.bookSourceName,
                coverUrl = rule.coverUrl?.let { analyze.getString(it, isUrl = true) },
                intro = rule.intro?.let { analyze.getString(it) },
                kind = rule.kind?.let { analyze.getString(it) },
                latestChapterTitle = rule.lastChapter?.let { analyze.getString(it) }
            )
        }
    }
}
