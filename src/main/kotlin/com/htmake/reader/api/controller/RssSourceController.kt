package com.htmake.reader.api.controller

import com.htmake.reader.api.ReturnData
import com.htmake.reader.utils.asJsonArray
import io.legado.app.data.entities.RssArticle
import io.legado.app.data.entities.RssSource
import io.legado.app.model.Debug
import io.legado.app.model.rss.Rss
import io.vertx.core.http.HttpMethod
import io.vertx.core.json.JsonArray
import io.vertx.core.json.JsonObject
import io.vertx.ext.web.RoutingContext
import mu.KotlinLogging
import kotlin.coroutines.CoroutineContext

private val logger = KotlinLogging.logger {}

class RssSourceController(coroutineContext: CoroutineContext) : BaseController(coroutineContext) {
    suspend fun getRssSources(context: RoutingContext): ReturnData {
        val result = ReturnData()
        if (!checkAuth(context)) {
            return result.setData("NEED_LOGIN").setErrorMsg("请登录后使用")
        }

        val userNameSpace = getUserNameSpace(context)
        val list = getUserStorage(userNameSpace, "rssSources").asJsonArray()
        return result.setData(list?.list ?: ArrayList<Any>())
    }

    suspend fun canEditRssSource(context: RoutingContext): Boolean {
        if (!appConfig.secure) {
            return true
        }
        return context.get<com.htmake.reader.entity.User>("userInfo")?.enable_book_source ?: false
    }

    suspend fun saveRssSource(context: RoutingContext): ReturnData {
        val result = ReturnData()
        if (!checkAuth(context)) {
            return result.setData("NEED_LOGIN").setErrorMsg("请登录后使用")
        }
        if (!canEditRssSource(context)) {
            return result.setErrorMsg("权限不足")
        }

        val rssSource = RssSource.fromJson(context.bodyAsString).getOrNull()
            ?: return result.setErrorMsg("参数错误")
        if (rssSource.sourceUrl.isEmpty()) {
            return result.setErrorMsg("RSS链接不能为空")
        }
        if (rssSource.sourceName.isEmpty()) {
            return result.setErrorMsg("RSS名称不能为空")
        }

        val userNameSpace = getUserNameSpace(context)
        var rssSourceList = getUserStorage(userNameSpace, "rssSources").asJsonArray() ?: JsonArray()
        var index = -1
        for (itemIndex in 0 until rssSourceList.size()) {
            val existing = RssSource.fromJson(rssSourceList.getJsonObject(itemIndex).toString()).getOrNull()
            if (existing?.sourceUrl == rssSource.sourceUrl) {
                index = itemIndex
                break
            }
        }
        if (index >= 0) {
            val values = rssSourceList.list
            values[index] = JsonObject.mapFrom(rssSource)
            saveUserStorage(userNameSpace, "rssSources", JsonArray(values))
        } else {
            rssSourceList.add(JsonObject.mapFrom(rssSource))
            saveUserStorage(userNameSpace, "rssSources", rssSourceList)
        }
        return result.setData("")
    }

    suspend fun saveRssSources(context: RoutingContext): ReturnData {
        val result = ReturnData()
        if (!checkAuth(context)) {
            return result.setData("NEED_LOGIN").setErrorMsg("请登录后使用")
        }
        if (!canEditRssSource(context)) {
            return result.setErrorMsg("权限不足")
        }

        val sourceJsonArray = context.bodyAsJsonArray ?: return result.setErrorMsg("参数错误")
        val userNameSpace = getUserNameSpace(context)
        var rssSourceList = getUserStorage(userNameSpace, "rssSources").asJsonArray() ?: JsonArray()
        for (index in 0 until sourceJsonArray.size()) {
            val rssSource = RssSource.fromJson(sourceJsonArray.getJsonObject(index).toString()).getOrNull()
                ?: continue
            if (rssSource.sourceUrl.isEmpty() || rssSource.sourceName.isEmpty()) {
                continue
            }

            var existingIndex = -1
            for (itemIndex in 0 until rssSourceList.size()) {
                val existing = RssSource.fromJson(rssSourceList.getJsonObject(itemIndex).toString()).getOrNull()
                if (existing?.sourceUrl == rssSource.sourceUrl) {
                    existingIndex = itemIndex
                    break
                }
            }
            if (existingIndex >= 0) {
                val values = rssSourceList.list
                values[existingIndex] = JsonObject.mapFrom(rssSource)
                rssSourceList = JsonArray(values)
            } else {
                rssSourceList.add(JsonObject.mapFrom(rssSource))
            }
        }
        saveUserStorage(userNameSpace, "rssSources", rssSourceList)
        return result.setData("")
    }

    suspend fun deleteRssSource(context: RoutingContext): ReturnData {
        val result = ReturnData()
        if (!checkAuth(context)) {
            return result.setData("NEED_LOGIN").setErrorMsg("请登录后使用")
        }
        if (!canEditRssSource(context)) {
            return result.setErrorMsg("权限不足")
        }

        val rssSource = RssSource.fromJson(context.bodyAsString).getOrNull()
            ?: return result.setErrorMsg("参数错误")
        val userNameSpace = getUserNameSpace(context)
        val rssSourceList = getUserStorage(userNameSpace, "rssSources").asJsonArray() ?: JsonArray()
        var index = -1
        for (itemIndex in 0 until rssSourceList.size()) {
            val existing = RssSource.fromJson(rssSourceList.getJsonObject(itemIndex).toString()).getOrNull()
            if (existing?.sourceUrl == rssSource.sourceUrl) {
                index = itemIndex
                break
            }
        }
        if (index >= 0) {
            rssSourceList.remove(index)
        }
        saveUserStorage(userNameSpace, "rssSources", rssSourceList)
        return result.setData("")
    }

    fun getRssSourceByURL(url: String, userNameSpace: String): RssSource? {
        if (url.isEmpty()) {
            return null
        }
        val list = getUserStorage(userNameSpace, "rssSources").asJsonArray() ?: return null
        for (index in 0 until list.size()) {
            val source = RssSource.fromJson(list.getJsonObject(index).toString()).getOrNull()
            if (source?.sourceUrl == url) {
                return source
            }
        }
        return null
    }

    suspend fun getRssArticles(context: RoutingContext): ReturnData {
        val result = ReturnData()
        if (!checkAuth(context)) {
            return result.setData("NEED_LOGIN").setErrorMsg("请登录后使用")
        }

        val sourceUrl: String
        val sortName: String
        var sortUrl: String
        val page: Int
        if (context.request().method() == HttpMethod.POST) {
            val body = context.bodyAsJson
            sourceUrl = body.getString("sourceUrl")
            sortName = body.getString("sortName", "")
            sortUrl = body.getString("sortUrl", "")
            page = body.getInteger("page", 1)
        } else {
            sourceUrl = context.queryParam("sourceUrl").firstOrNull() ?: ""
            sortName = context.queryParam("sortName").firstOrNull() ?: ""
            sortUrl = context.queryParam("sortUrl").firstOrNull() ?: ""
            page = context.queryParam("page").firstOrNull()?.toInt() ?: 1
        }

        if (sourceUrl.isEmpty()) {
            return result.setErrorMsg("RSS源链接不能为空")
        }
        if (sortUrl.isEmpty()) {
            sortUrl = sourceUrl
        }
        val rssSource = getRssSourceByURL(sourceUrl, getUserNameSpace(context))
            ?: return result.setErrorMsg("RSS源不存在")
        return result.setData(Rss.getArticles(sortName, sortUrl, rssSource, page, Debug))
    }

    suspend fun getRssContent(context: RoutingContext): ReturnData {
        val result = ReturnData()
        if (!checkAuth(context)) {
            return result.setData("NEED_LOGIN").setErrorMsg("请登录后使用")
        }

        val sourceUrl: String
        val link: String
        val origin: String
        if (context.request().method() == HttpMethod.POST) {
            val body = context.bodyAsJson
            sourceUrl = body.getString("sourceUrl")
            link = body.getString("link")
            origin = body.getString("origin")
        } else {
            sourceUrl = context.queryParam("sourceUrl").firstOrNull() ?: ""
            link = context.queryParam("link").firstOrNull() ?: ""
            origin = context.queryParam("origin").firstOrNull() ?: ""
        }

        if (sourceUrl.isEmpty()) {
            return result.setErrorMsg("RSS链接不能为空")
        }
        if (link.isEmpty()) {
            return result.setErrorMsg("RSS文章链接不能为空")
        }
        if (origin.isEmpty()) {
            return result.setErrorMsg("RSS文章来源不能为空")
        }
        val rssSource = getRssSourceByURL(sourceUrl, getUserNameSpace(context))
            ?: return result.setErrorMsg("RSS源不存在")
        val rssArticle = RssArticle(origin = origin, link = link)
        val content = rssSource.ruleContent?.let {
            Rss.getContent(rssArticle, it, rssSource, Debug)
        } ?: ""
        return result.setData(content)
    }

}
