/** Business rewrite from reader-pro-3.2.14.jar — phase8. */

package io.legado.app.model.rss

import io.legado.app.data.entities.RssArticle
import io.legado.app.data.entities.RssSource
import io.legado.app.model.DebugLog
import io.legado.app.model.analyzeRule.AnalyzeRule
import io.legado.app.model.analyzeRule.AnalyzeUrl
import org.jsoup.Jsoup
import org.jsoup.parser.Parser

/**
 * RSS 拉文：有 ruleArticles 走 AnalyzeRule；否则按标准 RSS/Atom XML 解析。
 */
object Rss {

    suspend fun getArticles(
        sortName: String,
        sortUrl: String,
        rssSource: RssSource,
        page: Int,
        debugLog: DebugLog? = null
    ): Pair<MutableList<RssArticle>, String?> {
        val url = sortUrl.ifBlank { rssSource.sourceUrl }
        val analyzeUrl = AnalyzeUrl(
            mUrl = url,
            page = page,
            source = rssSource,
            headerMapF = rssSource.getHeaderMap(true),
            debugLog = debugLog
        )
        val body = analyzeUrl.getStrResponseAwait().body ?: ""
        return if (!rssSource.ruleArticles.isNullOrBlank()) {
            parseByRule(sortName, url, body, rssSource, debugLog)
        } else {
            parseDefaultXml(sortName, url, body, rssSource) to null
        }
    }

    suspend fun getContent(
        rssArticle: RssArticle,
        ruleContent: String,
        rssSource: RssSource,
        debugLog: DebugLog? = null
    ): String {
        if (ruleContent.isBlank()) return rssArticle.description ?: rssArticle.content ?: ""
        val analyzeUrl = AnalyzeUrl(
            mUrl = rssArticle.link,
            source = rssSource,
            headerMapF = rssSource.getHeaderMap(true),
            debugLog = debugLog
        )
        val body = analyzeUrl.getStrResponseAwait().body ?: ""
        val rule = AnalyzeRule(null, rssSource, debugLog)
        rule.setContent(body, rssArticle.link)
        return rule.getString(ruleContent)
    }

    private fun parseByRule(
        sortName: String,
        sortUrl: String,
        body: String,
        source: RssSource,
        debugLog: DebugLog?
    ): Pair<MutableList<RssArticle>, String?> {
        val rule = AnalyzeRule(null, source, debugLog)
        rule.setContent(body, sortUrl)
        val elements = rule.getStringList(source.ruleArticles!!)
        val list = ArrayList<RssArticle>()
        var order = System.currentTimeMillis()
        for (el in elements) {
            // when rule returns HTML snippets, re-parse fields from each
            val sub = AnalyzeRule(null, source, debugLog).setContent(el, sortUrl)
            val title = source.ruleTitle?.let { sub.getString(it) }.orEmpty()
            val link = source.ruleLink?.let { sub.getString(it, isUrl = true) }.orEmpty()
            if (title.isEmpty() && link.isEmpty()) continue
            list.add(
                RssArticle(
                    origin = source.sourceUrl,
                    sort = sortName,
                    title = title.ifEmpty { link },
                    order = order--,
                    link = link.ifEmpty { sortUrl },
                    pubDate = source.rulePubDate?.let { sub.getString(it) },
                    description = source.ruleDescription?.let { sub.getString(it) },
                    image = source.ruleImage?.let { sub.getString(it, isUrl = true) }
                )
            )
        }
        val next = source.ruleNextPage?.let { rule.getString(it, isUrl = true) }?.takeIf { it.isNotBlank() }
        return list to next
    }

    private fun parseDefaultXml(
        sortName: String,
        sortUrl: String,
        body: String,
        source: RssSource
    ): MutableList<RssArticle> {
        val list = ArrayList<RssArticle>()
        try {
            val doc = Jsoup.parse(body, sortUrl, Parser.xmlParser())
            // RSS 2.0 item
            val items = doc.select("item")
            if (items.isNotEmpty()) {
                var order = System.currentTimeMillis()
                for (item in items) {
                    list.add(
                        RssArticle(
                            origin = source.sourceUrl,
                            sort = sortName,
                            title = item.selectFirst("title")?.text().orEmpty(),
                            order = order--,
                            link = item.selectFirst("link")?.text()
                                ?: item.selectFirst("link")?.attr("href").orEmpty(),
                            pubDate = item.selectFirst("pubDate")?.text(),
                            description = item.selectFirst("description")?.html()
                                ?: item.selectFirst("content|encoded")?.html(),
                            image = item.selectFirst("enclosure[url]")?.attr("url")
                                ?: item.selectFirst("media|content")?.attr("url")
                        )
                    )
                }
                return list
            }
            // Atom entry
            for (entry in doc.select("entry")) {
                list.add(
                    RssArticle(
                        origin = source.sourceUrl,
                        sort = sortName,
                        title = entry.selectFirst("title")?.text().orEmpty(),
                        order = System.currentTimeMillis(),
                        link = entry.selectFirst("link[href]")?.attr("href")
                            ?: entry.selectFirst("link")?.text().orEmpty(),
                        pubDate = entry.selectFirst("updated")?.text()
                            ?: entry.selectFirst("published")?.text(),
                        description = entry.selectFirst("summary")?.html()
                            ?: entry.selectFirst("content")?.html()
                    )
                )
            }
        } catch (_: Exception) {
        }
        return list
    }
}
