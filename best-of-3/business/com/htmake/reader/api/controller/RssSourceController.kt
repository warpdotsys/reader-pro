package com.htmake.reader.api.controller

import com.htmake.reader.api.ReturnData
import com.htmake.reader.utils.ExtKt
import io.legado.app.data.entities.RssArticle
import io.legado.app.data.entities.RssSource
import io.legado.app.model.rss.Rss
import io.vertx.core.json.JsonArray
import io.vertx.core.json.JsonObject
import io.vertx.ext.web.RoutingContext
import kotlin.coroutines.CoroutineContext

class RssSourceController(cc: CoroutineContext) : BaseController(cc) {
    private fun load(ns: String) = ExtKt.asJsonArray(getUserStorage(ns, "rssSource")) ?: JsonArray()
    private fun save(ns: String, a: JsonArray) = saveUserStorage(ns, "rssSource", a)

    suspend fun list(ctx: RoutingContext): ReturnData {
        val rd = ReturnData()
        if (!checkAuth(ctx)) return rd.setData("NEED_LOGIN").setErrorMsg("请登录后使用")
        return rd.setData(load(getUserNameSpace(ctx)))
    }

    suspend fun save(ctx: RoutingContext): ReturnData {
        val rd = ReturnData()
        if (!checkAuth(ctx)) return rd.setData("NEED_LOGIN").setErrorMsg("请登录后使用")
        val ns = getUserNameSpace(ctx)
        val arr = ctx.bodyAsJsonArray
        if (arr != null) {
            // merge by sourceUrl
            val cur = load(ns)
            val map = linkedMapOf<String, JsonObject>()
            for (i in 0 until cur.size()) {
                val o = cur.getJsonObject(i) ?: continue
                map[o.getString("sourceUrl") ?: ""] = o
            }
            for (i in 0 until arr.size()) {
                val o = arr.getJsonObject(i) ?: continue
                map[o.getString("sourceUrl") ?: ""] = o
            }
            val out = JsonArray()
            map.values.forEach { out.add(it) }
            save(ns, out)
            return rd.setData(out.size())
        }
        val one = ctx.bodyAsJson ?: return rd.setErrorMsg("参数错误")
        val url = one.getString("sourceUrl") ?: return rd.setErrorMsg("sourceUrl 不能为空")
        val cur = load(ns)
        var replaced = false
        val out = JsonArray()
        for (i in 0 until cur.size()) {
            val o = cur.getJsonObject(i) ?: continue
            if (o.getString("sourceUrl") == url) {
                out.add(one); replaced = true
            } else out.add(o)
        }
        if (!replaced) out.add(one)
        save(ns, out)
        return rd.setData(one)
    }

    suspend fun delete(ctx: RoutingContext): ReturnData {
        val rd = ReturnData()
        if (!checkAuth(ctx)) return rd.setData("NEED_LOGIN").setErrorMsg("请登录后使用")
        val ns = getUserNameSpace(ctx)
        val body = ctx.bodyAsJson
        val urls = mutableSetOf<String>()
        body?.getString("sourceUrl")?.let { urls += it }
        body?.getJsonArray("sourceUrls")?.forEach { if (it is String) urls += it }
        ctx.queryParam("sourceUrl").firstOrNull()?.let { urls += it }
        if (urls.isEmpty() && ctx.bodyAsJsonArray != null) {
            val arr = ctx.bodyAsJsonArray!!
            for (i in 0 until arr.size()) {
                arr.getJsonObject(i)?.getString("sourceUrl")?.let { urls += it }
                    ?: (arr.getValue(i) as? String)?.let { urls += it }
            }
        }
        if (urls.isEmpty()) return rd.setErrorMsg("sourceUrl 不能为空")
        val cur = load(ns)
        val out = JsonArray()
        for (i in 0 until cur.size()) {
            val o = cur.getJsonObject(i) ?: continue
            if (o.getString("sourceUrl") !in urls) out.add(o)
        }
        save(ns, out)
        return rd.setData(true)
    }

    suspend fun getRssSources(ctx: RoutingContext) = list(ctx)
    suspend fun saveRssSource(ctx: RoutingContext) = save(ctx)
    suspend fun saveRssSources(ctx: RoutingContext) = save(ctx)
    suspend fun deleteRssSource(ctx: RoutingContext) = delete(ctx)

    suspend fun getRssArticles(ctx: RoutingContext): ReturnData {
        val rd = ReturnData()
        if (!checkAuth(ctx)) return rd.setData("NEED_LOGIN").setErrorMsg("请登录后使用")
        val ns = getUserNameSpace(ctx)
        val sourceUrl = p(ctx, "sourceUrl") ?: p(ctx, "url") ?: return rd.setErrorMsg("sourceUrl 不能为空")
        val sortName = p(ctx, "sortName") ?: p(ctx, "sort") ?: ""
        val sortUrl = p(ctx, "sortUrl") ?: ""
        val page = p(ctx, "page")?.toIntOrNull() ?: 1
        val src = loadSource(ns, sourceUrl) ?: return rd.setErrorMsg("RSS 源不存在")
        src.setUserNameSpace(ns)
        return try {
            val url = sortUrl.ifBlank {
                Rss.parseSortUrls(src).firstOrNull { it.first == sortName }?.second
                    ?: src.sourceUrl
            }
            val (articles, next) = Rss.getArticles(sortName, url, src, page)
            rd.setData(
                mapOf(
                    "articles" to articles,
                    "nextPage" to next,
                    "sorts" to Rss.parseSortUrls(src).map { mapOf("name" to it.first, "url" to it.second) }
                )
            )
        } catch (e: Exception) {
            rd.setErrorMsg(e.message ?: "拉取失败")
        }
    }

    suspend fun getRssContent(ctx: RoutingContext): ReturnData {
        val rd = ReturnData()
        if (!checkAuth(ctx)) return rd.setData("NEED_LOGIN").setErrorMsg("请登录后使用")
        val ns = getUserNameSpace(ctx)
        val sourceUrl = p(ctx, "sourceUrl") ?: p(ctx, "origin") ?: return rd.setErrorMsg("sourceUrl 不能为空")
        val link = p(ctx, "link") ?: p(ctx, "url") ?: return rd.setErrorMsg("link 不能为空")
        val title = p(ctx, "title") ?: ""
        val src = loadSource(ns, sourceUrl) ?: return rd.setErrorMsg("RSS 源不存在")
        src.setUserNameSpace(ns)
        val article = RssArticle(
            origin = sourceUrl,
            title = title,
            link = link,
            description = p(ctx, "description")
        )
        return try {
            val content = Rss.getContent(article, src.ruleContent, src)
            rd.setData(mapOf("content" to content, "link" to link, "title" to title))
        } catch (e: Exception) {
            rd.setErrorMsg(e.message ?: "正文失败")
        }
    }

    /** List sort categories without fetching. */
    suspend fun getRssSorts(ctx: RoutingContext): ReturnData {
        val rd = ReturnData()
        if (!checkAuth(ctx)) return rd.setData("NEED_LOGIN").setErrorMsg("请登录后使用")
        val ns = getUserNameSpace(ctx)
        val sourceUrl = p(ctx, "sourceUrl") ?: return rd.setErrorMsg("sourceUrl 不能为空")
        val src = loadSource(ns, sourceUrl) ?: return rd.setErrorMsg("RSS 源不存在")
        return rd.setData(Rss.parseSortUrls(src).map { mapOf("name" to it.first, "url" to it.second) })
    }

    private fun p(ctx: RoutingContext, key: String): String? =
        ctx.queryParam(key).firstOrNull()?.takeIf { it.isNotBlank() }
            ?: ctx.bodyAsJson?.getString(key)?.takeIf { it.isNotBlank() }

    private fun loadSource(ns: String, url: String): RssSource? {
        val arr = load(ns)
        for (i in 0 until arr.size()) {
            val o = arr.getJsonObject(i) ?: continue
            if (o.getString("sourceUrl") == url) return o.toRssSource()
        }
        return null
    }

    private fun JsonObject.toRssSource(): RssSource =
        RssSource(
            sourceUrl = getString("sourceUrl") ?: "",
            sourceName = getString("sourceName") ?: "",
            sourceIcon = getString("sourceIcon") ?: "",
            sourceGroup = getString("sourceGroup"),
            enabled = getBoolean("enabled", true),
            headerJson = getString("header") ?: getString("headerJson"),
            sortUrl = getString("sortUrl"),
            ruleArticles = getString("ruleArticles"),
            ruleNextPage = getString("ruleNextPage"),
            ruleTitle = getString("ruleTitle"),
            rulePubDate = getString("rulePubDate"),
            ruleDescription = getString("ruleDescription"),
            ruleImage = getString("ruleImage"),
            ruleLink = getString("ruleLink"),
            ruleContent = getString("ruleContent")
        )
}
