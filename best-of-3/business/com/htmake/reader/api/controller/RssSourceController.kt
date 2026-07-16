/** Business rewrite from reader-pro-3.2.14.jar — phase8. */

package com.htmake.reader.api.controller

import com.htmake.reader.api.ReturnData
import com.htmake.reader.utils.ExtKt
import io.legado.app.data.entities.RssArticle
import io.legado.app.data.entities.RssSource
import io.legado.app.model.rss.Rss
import io.vertx.core.http.HttpMethod
import io.vertx.core.json.JsonArray
import io.vertx.core.json.JsonObject
import io.vertx.ext.web.RoutingContext
import kotlin.coroutines.CoroutineContext

class RssSourceController(coroutineContext: CoroutineContext) : BaseController(coroutineContext) {
    private fun load(ns: String) = ExtKt.asJsonArray(getUserStorage(ns, "rssSource")) ?: JsonArray()
    private fun save(ns: String, a: JsonArray) = saveUserStorage(ns, "rssSource", a)

    fun getRssSourceByURL(url: String, userNameSpace: String): RssSource? {
        val arr = load(userNameSpace)
        for (i in 0 until arr.size()) {
            val o = arr.getJsonObject(i) ?: continue
            val u = o.getString("sourceUrl") ?: o.getString("rssUrl")
            if (u == url) {
                return try {
                    o.mapTo(RssSource::class.java).also { it.setUserNameSpace(userNameSpace) }
                } catch (_: Exception) {
                    RssSource(
                        sourceUrl = u ?: "",
                        sourceName = o.getString("sourceName") ?: "",
                        ruleArticles = o.getString("ruleArticles"),
                        ruleTitle = o.getString("ruleTitle"),
                        ruleLink = o.getString("ruleLink"),
                        rulePubDate = o.getString("rulePubDate"),
                        ruleDescription = o.getString("ruleDescription"),
                        ruleImage = o.getString("ruleImage"),
                        ruleContent = o.getString("ruleContent"),
                        ruleNextPage = o.getString("ruleNextPage"),
                        header = o.getString("header"),
                        sortUrl = o.getString("sortUrl")
                    ).also { it.setUserNameSpace(userNameSpace) }
                }
            }
        }
        return null
    }

    suspend fun getRssSources(context: RoutingContext): ReturnData {
        val rd = ReturnData()
        if (!checkAuth(context)) return rd.setData("NEED_LOGIN").setErrorMsg("请登录后使用")
        return rd.setData(load(getUserNameSpace(context)))
    }

    suspend fun saveRssSource(context: RoutingContext): ReturnData {
        val rd = ReturnData()
        if (!checkAuth(context)) return rd.setData("NEED_LOGIN").setErrorMsg("请登录后使用")
        val ns = getUserNameSpace(context)
        val bodyArr = context.bodyAsJsonArray
        if (bodyArr != null) {
            save(ns, bodyArr)
            return rd.setData(bodyArr.size())
        }
        val src = context.bodyAsJson ?: return rd.setErrorMsg("参数错误")
        val arr = load(ns)
        val key = src.getString("sourceUrl") ?: src.getString("rssUrl") ?: return rd.setErrorMsg("链接不能为空")
        val list = arr.list
        var found = false
        for (i in list.indices) {
            val o = arr.getJsonObject(i)
            val k = o.getString("sourceUrl") ?: o.getString("rssUrl")
            if (k == key) {
                list[i] = src
                found = true
                break
            }
        }
        if (!found) list.add(src)
        save(ns, JsonArray(list))
        return rd.setData(src)
    }

    suspend fun deleteRssSource(context: RoutingContext): ReturnData {
        val rd = ReturnData()
        if (!checkAuth(context)) return rd.setData("NEED_LOGIN").setErrorMsg("请登录后使用")
        val ns = getUserNameSpace(context)
        val key = context.bodyAsJson?.getString("sourceUrl")
            ?: context.queryParam("sourceUrl").firstOrNull()
            ?: return rd.setErrorMsg("参数错误")
        val arr = load(ns)
        val list = arr.list.filterIndexed { i, _ ->
            val o = arr.getJsonObject(i)
            (o.getString("sourceUrl") ?: o.getString("rssUrl")) != key
        }
        save(ns, JsonArray(list))
        return rd.setData(true)
    }

    suspend fun getRssArticles(context: RoutingContext): ReturnData {
        val rd = ReturnData()
        if (!checkAuth(context)) return rd.setData("NEED_LOGIN").setErrorMsg("请登录后使用")
        val sourceUrl = p(context, "sourceUrl") ?: p(context, "url") ?: return rd.setErrorMsg("RSS源链接不能为空")
        val sortName = p(context, "sortName") ?: ""
        var sortUrl = p(context, "sortUrl") ?: ""
        val page = pInt(context, "page") ?: 1
        if (sortUrl.isEmpty()) sortUrl = sourceUrl
        val ns = getUserNameSpace(context)
        val source = getRssSourceByURL(sourceUrl, ns) ?: return rd.setErrorMsg("RSS源不存在")
        val (articles, next) = Rss.getArticles(sortName, sortUrl, source, page, null)
        return rd.setData(mapOf("articles" to articles, "nextPage" to next))
    }

    suspend fun getRssContent(context: RoutingContext): ReturnData {
        val rd = ReturnData()
        if (!checkAuth(context)) return rd.setData("NEED_LOGIN").setErrorMsg("请登录后使用")
        val sourceUrl = p(context, "sourceUrl") ?: ""
        val link = p(context, "link") ?: p(context, "url") ?: return rd.setErrorMsg("link 不能为空")
        val title = p(context, "title") ?: ""
        val ns = getUserNameSpace(context)
        val source = if (sourceUrl.isNotEmpty()) getRssSourceByURL(sourceUrl, ns) else null
        val article = RssArticle(origin = sourceUrl, title = title, link = link)
        val content = if (source != null && !source.ruleContent.isNullOrBlank()) {
            Rss.getContent(article, source.ruleContent!!, source, null)
        } else if (source != null) {
            Rss.getContent(article, "", source, null)
        } else {
            // bare fetch
            try {
                val au = io.legado.app.model.analyzeRule.AnalyzeUrl(mUrl = link)
                au.getStrResponseAwait().body ?: ""
            } catch (_: Exception) {
                ""
            }
        }
        return rd.setData(mapOf("link" to link, "content" to content))
    }

    private fun p(context: RoutingContext, name: String): String? {
        if (context.request().method() == HttpMethod.POST) {
            context.bodyAsJson?.getString(name)?.let { return it }
        }
        return context.queryParam(name).firstOrNull()
    }

    private fun pInt(context: RoutingContext, name: String): Int? {
        if (context.request().method() == HttpMethod.POST) {
            context.bodyAsJson?.getInteger(name)?.let { return it }
        }
        return context.queryParam(name).firstOrNull()?.toIntOrNull()
    }
}
