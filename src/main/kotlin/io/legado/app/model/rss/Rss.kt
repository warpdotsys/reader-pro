package io.legado.app.model.rss

import io.legado.app.data.entities.RssArticle
import io.legado.app.data.entities.RssSource
import io.legado.app.model.DebugLog
import io.legado.app.model.analyzeRule.AnalyzeRule
import io.legado.app.model.analyzeRule.AnalyzeUrl
import org.jsoup.Jsoup
import org.jsoup.parser.Parser

object Rss {
    suspend fun getArticles(
        sortName: String,
        sortUrl: String,
        rssSource: RssSource,
        page: Int,
        debugLog: DebugLog? = null
    ): Pair<MutableList<RssArticle>, String?> {
        val url = sortUrl.ifBlank { rssSource.sourceUrl }
        val body = AnalyzeUrl(mUrl = url, page = page, source = rssSource, debugLog = debugLog)
            .getStrResponseAwait().body ?: ""
        return if (!rssSource.ruleArticles.isNullOrBlank()) {
            parseByRule(sortName, url, body, rssSource, debugLog)
        } else {
            parseDefaultXml(sortName, url, body, rssSource) to null
        }
    }

    suspend fun getContent(
        article: RssArticle,
        ruleContent: String,
        rssSource: RssSource,
        debugLog: DebugLog? = null
    ): String {
        if (ruleContent.isBlank()) return article.description ?: article.content ?: ""
        val body = AnalyzeUrl(mUrl = article.link, source = rssSource, debugLog = debugLog)
            .getStrResponseAwait().body ?: ""
        return AnalyzeRule(null, rssSource, debugLog).setContent(body, article.link).getString(ruleContent)
    }

    private fun parseByRule(
        sortName: String, sortUrl: String, body: String, source: RssSource, debugLog: DebugLog?
    ): Pair<MutableList<RssArticle>, String?> {
        val rule = AnalyzeRule(null, source, debugLog).setContent(body, sortUrl)
        val elements = rule.getStringList(source.ruleArticles!!)
        val list = ArrayList<RssArticle>()
        var order = System.currentTimeMillis()
        for (el in elements) {
            val sub = AnalyzeRule(null, source, debugLog).setContent(el, sortUrl)
            val title = source.ruleTitle?.let { sub.getString(it) }.orEmpty()
            val link = source.ruleLink?.let { sub.getString(it, isUrl = true) }.orEmpty()
            if (title.isEmpty() && link.isEmpty()) continue
            list += RssArticle(
                origin = source.sourceUrl, sort = sortName,
                title = title.ifEmpty { link }, order = order--,
                link = link.ifEmpty { sortUrl },
                pubDate = source.rulePubDate?.let { sub.getString(it) },
                description = source.ruleDescription?.let { sub.getString(it) },
                image = source.ruleImage?.let { sub.getString(it, isUrl = true) }
            )
        }
        val next = source.ruleNextPage?.let { rule.getString(it, isUrl = true) }?.takeIf { it.isNotBlank() }
        return list to next
    }

    private fun parseDefaultXml(sortName: String, sortUrl: String, body: String, source: RssSource): MutableList<RssArticle> {
        val list = ArrayList<RssArticle>()
        try {
            val doc = Jsoup.parse(body, sortUrl, Parser.xmlParser())
            var order = System.currentTimeMillis()
            for (item in doc.select("item")) {
                list += RssArticle(
                    origin = source.sourceUrl, sort = sortName,
                    title = item.selectFirst("title")?.text().orEmpty(),
                    order = order--,
                    link = item.selectFirst("link")?.text() ?: item.selectFirst("link")?.attr("href").orEmpty(),
                    pubDate = item.selectFirst("pubDate")?.text(),
                    description = item.selectFirst("description")?.html()
                )
            }
            if (list.isEmpty()) {
                for (entry in doc.select("entry")) {
                    list += RssArticle(
                        origin = source.sourceUrl, sort = sortName,
                        title = entry.selectFirst("title")?.text().orEmpty(),
                        order = System.currentTimeMillis(),
                        link = entry.selectFirst("link[href]")?.attr("href") ?: "",
                        pubDate = entry.selectFirst("updated")?.text(),
                        description = entry.selectFirst("summary")?.html()
                    )
                }
            }
        } catch (_: Exception) {
        }
        return list
    }
}
