package com.htmake.reader.api.controller

import com.htmake.reader.api.ReturnData
import com.htmake.reader.utils.ExtKt
import io.vertx.core.json.JsonArray
import io.vertx.ext.web.RoutingContext
import kotlin.coroutines.CoroutineContext

class HttpTTSController(cc: CoroutineContext) : BaseController(cc) {
    private fun load(ns: String) = ExtKt.asJsonArray(getUserStorage(ns, "httpTTS")) ?: JsonArray()
    private fun save(ns: String, a: JsonArray) = saveUserStorage(ns, "httpTTS", a)

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
        if (arr != null) { save(ns, arr); return rd.setData(arr.size()) }
        val one = ctx.bodyAsJson ?: return rd.setErrorMsg("参数错误")
        val cur = load(ns); cur.add(one); save(ns, cur)
        return rd.setData(one)
    }

    suspend fun delete(ctx: RoutingContext): ReturnData {
        val rd = ReturnData()
        if (!checkAuth(ctx)) return rd.setData("NEED_LOGIN").setErrorMsg("请登录后使用")
        return rd.setData(true)
    }

    // aliases used by YueduApi
    suspend fun getBookGroups(ctx: RoutingContext) = list(ctx)
    suspend fun saveBookGroup(ctx: RoutingContext) = save(ctx)
    suspend fun deleteBookGroup(ctx: RoutingContext) = delete(ctx)
    suspend fun saveBookGroupOrder(ctx: RoutingContext) = save(ctx)
    suspend fun getBookmarks(ctx: RoutingContext) = list(ctx)
    suspend fun saveBookmark(ctx: RoutingContext) = save(ctx)
    suspend fun deleteBookmark(ctx: RoutingContext) = delete(ctx)
    suspend fun getReplaceRules(ctx: RoutingContext) = list(ctx)
    suspend fun saveReplaceRule(ctx: RoutingContext) = save(ctx)
    suspend fun deleteReplaceRule(ctx: RoutingContext) = delete(ctx)
    suspend fun getRssSources(ctx: RoutingContext) = list(ctx)
    suspend fun saveRssSource(ctx: RoutingContext) = save(ctx)
    suspend fun deleteRssSource(ctx: RoutingContext) = delete(ctx)
    suspend fun getRssArticles(ctx: RoutingContext): ReturnData = ReturnData().setData(emptyList<Any>())
    suspend fun getRssContent(ctx: RoutingContext): ReturnData = ReturnData().setData(mapOf("content" to ""))
}
