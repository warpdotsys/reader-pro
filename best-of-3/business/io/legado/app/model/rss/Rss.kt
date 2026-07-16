package io.legado.app.model.rss

import io.legado.app.data.entities.RssArticle
import io.legado.app.data.entities.RssSource
import io.legado.app.model.DebugLog
import io.legado.app.model.analyzeRule.AnalyzeRule
import io.legado.app.model.analyzeRule.AnalyzeUrl
import io.legado.app.utils.NetworkUtils
import org.jsoup.Jsoup
import org.jsoup.nodes.Element
import org.jsoup.parser.Parser

/**
 * RSS/Atom pull: ruleArticles → AnalyzeRule; else standard RSS 2.0 / Atom XML.
 */
object Rss {

    /**
     * Parse sortUrl field: lines of `name::url` or plain urls.
     */
    fun parseSortUrls(source: RssSource): List<Pair<String, String>> {
        val raw = source.sortUrl?.trim().orEmpty()
        if (raw.isEmpty()) return listOf("" to source.sourceUrl)
        return raw.lines().mapNotNull { line ->
            val t = line.trim()
            if (t.isEmpty()) return@mapNotNull null
            val sep = when {
                "::" in t -> "::"
                "\\n" in t -> null
                else -> null
            }
            if (sep != null && t.contains(sep)) {
                val name = t.substringBefore(sep).trim()
                val url = t.substringAfter(sep).trim()
                if (url.isEmpty()) null else name to url
            } else {
                "" to t
            }
        }.ifEmpty { listOf("" to source.sourceUrl) }
    }

    suspend fun getArticles(
        sortName: String,
        sortUrl: String,
        rssSource: RssSource,
        page: Int,
        debugLog: DebugLog? = null
    ): Pair<MutableList<RssArticle>, String?> {
        val url = sortUrl.ifBlank { rssSource.sourceUrl }
        val body = AnalyzeUrl(
            mUrl = url,
            page = page,
            source = rssSource,
            headerMapF = rssSource.getHeaderMap(true),
            debugLog = debugLog
        ).getStrResponseAwait().body ?: ""
        return if (!rssSource.ruleArticles.isNullOrBlank()) {
            parseByRule(sortName, url, body, rssSource, debugLog)
        } else {
            parseDefaultXml(sortName, url, body, rssSource) to null
        }
    }

    /** Offline parse (tests / cached body). */
    fun parseArticlesFromBody(
        sortName: String,
        sortUrl: String,
        body: String,
        rssSource: RssSource,
        debugLog: DebugLog? = null
    ): Pair<MutableList<RssArticle>, String?> {
        return if (!rssSource.ruleArticles.isNullOrBlank()) {
            parseByRule(sortName, sortUrl, body, rssSource, debugLog)
        } else {
            parseDefaultXml(sortName, sortUrl, body, rssSource) to null
        }
    }

    suspend fun getContent(
        article: RssArticle,
        ruleContent: String?,
        rssSource: RssSource,
        debugLog: DebugLog? = null
    ): String {
        val rule = ruleContent?.takeIf { it.isNotBlank() } ?: rssSource.ruleContent
        if (rule.isNullOrBlank()) return article.description ?: article.content ?: ""
        if (article.link.isBlank()) return article.description ?: ""
        val body = AnalyzeUrl(
            mUrl = article.link,
            source = rssSource,
            headerMapF = rssSource.getHeaderMap(true),
            debugLog = debugLog
        ).getStrResponseAwait().body ?: ""
        return AnalyzeRule(null, rssSource, debugLog)
            .setContent(body, article.link)
            .getString(rule)
    }

    private fun parseByRule(
        sortName: String,
        sortUrl: String,
        body: String,
        source: RssSource,
        debugLog: DebugLog?
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
                origin = source.sourceUrl,
                sort = sortName,
                title = title.ifEmpty { link },
                order = order--,
                link = link.ifEmpty { sortUrl },
                pubDate = source.rulePubDate?.let { sub.getString(it) },
                description = source.ruleDescription?.let { sub.getString(it) },
                image = source.ruleImage?.let { sub.getString(it, isUrl = true) }
            )
        }
        val next = source.ruleNextPage?.let { rule.getString(it, isUrl = true) }?.takeIf { it.isNotBlank() }
        return list to next
    }

    fun parseDefaultXml(
        sortName: String,
        sortUrl: String,
        body: String,
        source: RssSource
    ): MutableList<RssArticle> {
        val list = ArrayList<RssArticle>()
        try {
            val doc = Jsoup.parse(body, sortUrl, Parser.xmlParser())
            var order = System.currentTimeMillis()
            val items = doc.select("item")
            if (items.isNotEmpty()) {
                for (item in items) {
                    list += itemToArticle(item, source, sortName, sortUrl, order--)
                }
                return list
            }
            for (entry in doc.select("entry")) {
                list += atomToArticle(entry, source, sortName, sortUrl, order--)
            }
        } catch (_: Exception) {
        }
        return list
    }

    private fun itemToArticle(
        item: Element,
        source: RssSource,
        sortName: String,
        sortUrl: String,
        order: Long
    ): RssArticle {
        val link = item.selectFirst("link")?.ownText()?.takeIf { it.isNotBlank() }
            ?: item.selectFirst("link")?.attr("href")?.takeIf { it.isNotBlank() }
            ?: item.selectFirst("guid")?.text().orEmpty()
        val absLink = NetworkUtils.getAbsoluteURL(sortUrl, link)
        val desc = item.selectFirst("description")?.html()
            ?: item.getElementsByTag("content:encoded").firstOrNull()?.html()
            ?: item.selectFirst("content")?.html()
        val image = item.selectFirst("enclosure[type^=image]")?.attr("url")
            ?: item.selectFirst("enclosure[url]")?.attr("url")?.takeIf {
                it.contains(Regex("""\.(jpe?g|png|gif|webp)""", RegexOption.IGNORE_CASE)) ||
                    item.selectFirst("enclosure")?.attr("type")?.startsWith("image") == true
            }
            ?: item.getElementsByTag("media:content").firstOrNull()?.attr("url")
            ?: item.getElementsByTag("media:thumbnail").firstOrNull()?.attr("url")
            ?: extractFirstImg(desc)
        return RssArticle(
            origin = source.sourceUrl,
            sort = sortName,
            title = item.selectFirst("title")?.text().orEmpty(),
            order = order,
            link = absLink,
            pubDate = item.selectFirst("pubDate")?.text() ?: item.selectFirst("dc\\:date")?.text(),
            description = desc,
            image = image?.let { NetworkUtils.getAbsoluteURL(sortUrl, it) }
        )
    }

    private fun atomToArticle(
        entry: Element,
        source: RssSource,
        sortName: String,
        sortUrl: String,
        order: Long
    ): RssArticle {
        val link = entry.selectFirst("link[rel=alternate]")?.attr("href")
            ?: entry.selectFirst("link[href]")?.attr("href")
            ?: entry.selectFirst("link")?.text().orEmpty()
        val desc = entry.selectFirst("summary")?.html()
            ?: entry.selectFirst("content")?.html()
        val image = entry.selectFirst("link[rel=enclosure]")?.attr("href")
            ?: entry.getElementsByTag("media:thumbnail").firstOrNull()?.attr("url")
            ?: extractFirstImg(desc)
        return RssArticle(
            origin = source.sourceUrl,
            sort = sortName,
            title = entry.selectFirst("title")?.text().orEmpty(),
            order = order,
            link = NetworkUtils.getAbsoluteURL(sortUrl, link),
            pubDate = entry.selectFirst("updated")?.text() ?: entry.selectFirst("published")?.text(),
            description = desc,
            image = image?.let { NetworkUtils.getAbsoluteURL(sortUrl, it) }
        )
    }

    private fun extractFirstImg(html: String?): String? {
        if (html.isNullOrBlank()) return null
        return try {
            Jsoup.parseBodyFragment(html).selectFirst("img[src]")?.attr("src")
        } catch (_: Exception) {
            null
        }
    }
}
