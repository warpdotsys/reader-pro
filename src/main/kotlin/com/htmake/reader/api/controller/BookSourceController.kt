package com.htmake.reader.api.controller

import com.htmake.reader.api.ReturnData
import com.htmake.reader.utils.ExtKt
import io.legado.app.help.SourceAnalyzer
import io.vertx.core.http.HttpMethod
import io.vertx.core.json.JsonArray
import io.vertx.core.json.JsonObject
import io.vertx.ext.web.RoutingContext
import okhttp3.OkHttpClient
import okhttp3.Request
import java.util.concurrent.TimeUnit
import kotlin.coroutines.CoroutineContext

class BookSourceController(cc: CoroutineContext) : BaseController(cc) {
    fun getUserBookSourceJson(ns: String): JsonArray? =
        ExtKt.asJsonArray(getUserStorage(ns, "bookSource"))

    suspend fun getBookSources(ctx: RoutingContext): ReturnData {
        val rd = ReturnData()
        if (!checkAuth(ctx)) return rd.setData("NEED_LOGIN").setErrorMsg("请登录后使用")
        return rd.setData(getUserBookSourceJson(getUserNameSpace(ctx)) ?: JsonArray())
    }

    suspend fun saveBookSource(ctx: RoutingContext): ReturnData {
        val rd = ReturnData()
        if (!checkAuth(ctx)) return rd.setData("NEED_LOGIN").setErrorMsg("请登录后使用")
        val ns = getUserNameSpace(ctx)
        val src = ctx.bodyAsJson ?: return rd.setErrorMsg("参数错误")
        val url = src.getString("bookSourceUrl") ?: return rd.setErrorMsg("书源链接不能为空")
        val arr = getUserBookSourceJson(ns) ?: JsonArray()
        val list = arr.list
        var found = false
        for (i in list.indices) {
            if (arr.getJsonObject(i).getString("bookSourceUrl") == url) {
                list[i] = src; found = true; break
            }
        }
        if (!found) list.add(src)
        saveUserStorage(ns, "bookSource", JsonArray(list))
        return rd.setData(src)
    }

    suspend fun saveBookSources(ctx: RoutingContext): ReturnData {
        val arr = ctx.bodyAsJsonArray ?: ctx.bodyAsJson?.getJsonArray("bookSources") ?: JsonArray()
        return saveBookSources(ctx, arr)
    }

    suspend fun saveBookSources(ctx: RoutingContext, arr: JsonArray): ReturnData {
        val rd = ReturnData()
        if (!checkAuth(ctx)) return rd.setData("NEED_LOGIN").setErrorMsg("请登录后使用")
        val ns = getUserNameSpace(ctx)
        val normalized = JsonArray()
        for (i in 0 until arr.size()) {
            val raw = arr.getValue(i)?.toString() ?: continue
            val src = SourceAnalyzer.jsonToBookSource(raw).getOrNull()
            if (src != null) normalized.add(JsonObject.mapFrom(src))
            else if (arr.getValue(i) is JsonObject) normalized.add(arr.getJsonObject(i))
        }
        val out = if (normalized.isEmpty) arr else normalized
        saveUserStorage(ns, "bookSource", out)
        return rd.setData(out.size())
    }

    suspend fun deleteBookSource(ctx: RoutingContext): ReturnData {
        val rd = ReturnData()
        if (!checkAuth(ctx)) return rd.setData("NEED_LOGIN").setErrorMsg("请登录后使用")
        val ns = getUserNameSpace(ctx)
        val url = ctx.bodyAsJson?.getString("bookSourceUrl") ?: return rd.setErrorMsg("参数错误")
        val arr = getUserBookSourceJson(ns) ?: JsonArray()
        val list = arr.list.filterIndexed { i, _ -> arr.getJsonObject(i).getString("bookSourceUrl") != url }
        saveUserStorage(ns, "bookSource", JsonArray(list))
        return rd.setData(true)
    }

    suspend fun deleteAllBookSources(ctx: RoutingContext): ReturnData {
        val rd = ReturnData()
        if (!checkAuth(ctx)) return rd.setData("NEED_LOGIN").setErrorMsg("请登录后使用")
        saveUserStorage(getUserNameSpace(ctx), "bookSource", JsonArray())
        return rd.setData(true)
    }

    suspend fun saveFromRemoteSource(ctx: RoutingContext): ReturnData {
        val rd = ReturnData()
        if (!checkAuth(ctx)) return rd.setData("NEED_LOGIN").setErrorMsg("请登录后使用")
        val url = if (ctx.request().method() == HttpMethod.POST)
            ctx.bodyAsJson?.getString("url") else ctx.queryParam("url").firstOrNull()
        if (url.isNullOrBlank()) return rd.setErrorMsg("请输入远程书源链接")
        return try {
            val client = OkHttpClient.Builder().connectTimeout(3, TimeUnit.SECONDS).readTimeout(15, TimeUnit.SECONDS).build()
            val body = client.newCall(Request.Builder().url(url).get().build()).execute().use { it.body?.string() }
                ?: return rd.setErrorMsg("远程书源链接错误")
            val arr = try { JsonArray(body) } catch (_: Exception) {
                try {
                    val o = JsonObject(body)
                    o.getJsonArray("data") ?: o.getJsonArray("bookSources") ?: JsonArray().add(o)
                } catch (_: Exception) { return rd.setErrorMsg("远程书源链接错误") }
            }
            saveBookSources(ctx, arr)
        } catch (e: Exception) {
            rd.setErrorMsg(e.message ?: "远程书源链接错误")
        }
    }

    suspend fun getBookSource(ctx: RoutingContext): ReturnData {
        val rd = ReturnData()
        if (!checkAuth(ctx)) return rd.setData("NEED_LOGIN").setErrorMsg("请登录后使用")
        val url = ctx.queryParam("url").firstOrNull() ?: ctx.bodyAsJson?.getString("bookSourceUrl") ?: ""
        val arr = getUserBookSourceJson(getUserNameSpace(ctx)) ?: JsonArray()
        for (i in 0 until arr.size()) {
            val o = arr.getJsonObject(i)
            if (o.getString("bookSourceUrl") == url) return rd.setData(o)
        }
        return rd.setErrorMsg("书源不存在")
    }
}
