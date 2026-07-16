/**
 * Business-oriented rewrite from reader-pro-3.2.14.jar reverse engineering.
 * Source: CFR/Vineflower + manual semantic cleanup. For audit/readability.
 */

package com.htmake.reader.api.controller

import com.htmake.reader.api.ReturnData
import com.htmake.reader.utils.ExtKt
import io.legado.app.help.SourceAnalyzer
import io.vertx.core.http.HttpMethod
import io.vertx.core.json.Json
import io.vertx.core.json.JsonArray
import io.vertx.core.json.JsonObject
import io.vertx.ext.web.RoutingContext
import kotlin.coroutines.CoroutineContext

/**
 * Book source JSON CRUD per user namespace + remote subscription import.
 * Storage: storage/data/{user}/bookSource.json (and defaults).
 */
class BookSourceController(coroutineContext: CoroutineContext) : BaseController(coroutineContext) {

    private fun sourceFile(userNameSpace: String) =
        ExtKt.getWorkDir("storage", "data", userNameSpace, "bookSource.json")

    fun getUserBookSourceJson(userNameSpace: String): JsonArray? {
        val raw = ExtKt.getStorage("data", userNameSpace, "bookSource")
            ?: java.io.File(sourceFile(userNameSpace)).takeIf { it.exists() }?.readText()
        return ExtKt.asJsonArray(raw)
    }

    fun canEditBookSource(context: RoutingContext): Boolean {
        if (!appConfig.secure) return true
        val ns = getUserNameSpace(context)
        val users = loadUserMap()
        val u = users[ns] ?: return true
        return u["enableBookSource"] as? Boolean ?: true
    }

    suspend fun saveBookSource(context: RoutingContext): ReturnData {
        val rd = ReturnData()
        if (!checkAuth(context)) return rd.setData("NEED_LOGIN").setErrorMsg("请登录后使用")
        if (!canEditBookSource(context)) return rd.setErrorMsg("无权编辑书源")
        val ns = getUserNameSpace(context)
        val src = context.bodyAsJson ?: return rd.setErrorMsg("参数错误")
        val arr = getUserBookSourceJson(ns) ?: JsonArray()
        val url = src.getString("bookSourceUrl") ?: return rd.setErrorMsg("书源链接不能为空")
        // replace or append by bookSourceUrl
        var found = false
        val list = arr.list
        for (i in list.indices) {
            val o = arr.getJsonObject(i)
            if (o.getString("bookSourceUrl") == url) {
                list[i] = src
                found = true
                break
            }
        }
        if (!found) list.add(src)
        val out = JsonArray(list)
        saveUserStorage(ns, "bookSource", out)
        generateBookSourceMap(ns, out)
        return rd.setData(src)
    }

    suspend fun saveBookSources(context: RoutingContext): ReturnData {
        val body = context.body()
        val arr = when {
            context.bodyAsJsonArray != null -> context.bodyAsJsonArray
            context.bodyAsJson != null && context.bodyAsJson.getJsonArray("bookSources") != null ->
                context.bodyAsJson.getJsonArray("bookSources")
            else -> JsonArray()
        }
        return saveBookSources(context, arr)
    }

    suspend fun saveBookSources(context: RoutingContext, bookSourceJsonArray: JsonArray): ReturnData {
        val rd = ReturnData()
        if (!checkAuth(context)) return rd.setData("NEED_LOGIN").setErrorMsg("请登录后使用")
        if (!canEditBookSource(context)) return rd.setErrorMsg("无权编辑书源")
        val ns = getUserNameSpace(context)
        return saveUserBookSources(ns, null, bookSourceJsonArray)
    }

    fun saveUserBookSources(userNameSpace: String, userInfo: Any?, bookSourceJsonArray: JsonArray): ReturnData {
        val rd = ReturnData()
        // Normalize each source through SourceAnalyzer when possible
        val normalized = JsonArray()
        for (i in 0 until bookSourceJsonArray.size()) {
            val raw = bookSourceJsonArray.getValue(i)?.toString() ?: continue
            val src = SourceAnalyzer.jsonToBookSource(raw).getOrNull()
            if (src != null) {
                normalized.add(JsonObject.mapFrom(src))
            } else if (bookSourceJsonArray.getValue(i) is JsonObject) {
                normalized.add(bookSourceJsonArray.getJsonObject(i))
            }
        }
        val out = if (normalized.isEmpty) bookSourceJsonArray else normalized
        saveUserStorage(userNameSpace, "bookSource", out)
        generateBookSourceMap(userNameSpace, out)
        return rd.setData(out.size())
    }

    suspend fun getBookSource(context: RoutingContext): ReturnData {
        val rd = ReturnData()
        if (!checkAuth(context)) return rd.setData("NEED_LOGIN").setErrorMsg("请登录后使用")
        val url = param(context, "url") ?: param(context, "bookSourceUrl") ?: ""
        if (url.isEmpty()) return rd.setErrorMsg("书源链接不能为空")
        val ns = getUserNameSpace(context)
        val arr = getUserBookSourceJson(ns) ?: JsonArray()
        for (i in 0 until arr.size()) {
            val o = arr.getJsonObject(i)
            if (o.getString("bookSourceUrl") == url) return rd.setData(o)
        }
        return rd.setErrorMsg("书源不存在")
    }

    suspend fun getBookSources(context: RoutingContext): ReturnData {
        val rd = ReturnData()
        if (!checkAuth(context)) return rd.setData("NEED_LOGIN").setErrorMsg("请登录后使用")
        val ns = getUserNameSpace(context)
        val arr = getUserBookSourceJson(ns) ?: JsonArray()
        // optional simple field filter
        return rd.setData(arr)
    }

    suspend fun deleteBookSource(context: RoutingContext): ReturnData {
        val rd = ReturnData()
        if (!checkAuth(context)) return rd.setData("NEED_LOGIN").setErrorMsg("请登录后使用")
        val url = param(context, "url") ?: param(context, "bookSourceUrl") ?: ""
        val ns = getUserNameSpace(context)
        val arr = getUserBookSourceJson(ns) ?: JsonArray()
        val list = arr.list.filterIndexed { i, _ ->
            arr.getJsonObject(i).getString("bookSourceUrl") != url
        }
        val out = JsonArray(list)
        saveUserStorage(ns, "bookSource", out)
        generateBookSourceMap(ns, out)
        return rd.setData(true)
    }

    suspend fun deleteBookSources(context: RoutingContext): ReturnData {
        val rd = ReturnData()
        if (!checkAuth(context)) return rd.setData("NEED_LOGIN").setErrorMsg("请登录后使用")
        val urls = context.bodyAsJsonArray ?: JsonArray()
        val set = (0 until urls.size()).mapNotNull { urls.getString(it) }.toSet()
        val ns = getUserNameSpace(context)
        val arr = getUserBookSourceJson(ns) ?: JsonArray()
        val list = arr.list.filterIndexed { i, _ ->
            arr.getJsonObject(i).getString("bookSourceUrl") !in set
        }
        val out = JsonArray(list)
        saveUserStorage(ns, "bookSource", out)
        generateBookSourceMap(ns, out)
        return rd.setData(true)
    }

    suspend fun deleteAllBookSources(context: RoutingContext): ReturnData {
        val rd = ReturnData()
        if (!checkAuth(context)) return rd.setData("NEED_LOGIN").setErrorMsg("请登录后使用")
        val ns = getUserNameSpace(context)
        saveUserStorage(ns, "bookSource", JsonArray())
        generateBookSourceMap(ns, JsonArray())
        return rd.setData(true)
    }

    suspend fun setAsDefaultBookSources(context: RoutingContext): ReturnData {
        val rd = ReturnData()
        if (!checkAuth(context)) return rd.setData("NEED_LOGIN").setErrorMsg("请登录后使用")
        if (!checkManagerAuth(context)) return rd.setData("NEED_SECURE_KEY").setErrorMsg("请输入管理密码")
        val ns = getUserNameSpace(context)
        val arr = getUserBookSourceJson(ns) ?: JsonArray()
        // copy to default user template
        ExtKt.saveStorage(arrayOf("data", "default", "bookSource"), Json.encode(arr))
        return rd.setData(true)
    }

    suspend fun readSourceFile(context: RoutingContext): ReturnData {
        val rd = ReturnData()
        if (!checkAuth(context)) return rd.setData("NEED_LOGIN").setErrorMsg("请登录后使用")
        val path = param(context, "path") ?: return rd.setErrorMsg("path 不能为空")
        val f = java.io.File(path)
        if (!f.exists()) return rd.setErrorMsg("文件不存在")
        return rd.setData(f.readText())
    }

    /**
     * 远程订阅：HTTP GET url → JsonArray → saveBookSources。
     * jar 用 Vert.x WebClient timeout 3s；此处 OkHttp 等价。
     */
    suspend fun saveFromRemoteSource(context: RoutingContext): ReturnData {
        val rd = ReturnData()
        if (!checkAuth(context)) return rd.setData("NEED_LOGIN").setErrorMsg("请登录后使用")
        if (!canEditBookSource(context)) return rd.setErrorMsg("无权编辑书源")
        val url = param(context, "url") ?: return rd.setErrorMsg("请输入远程书源链接")
        if (url.isBlank()) return rd.setErrorMsg("请输入远程书源链接")
        return try {
            val client = okhttp3.OkHttpClient.Builder()
                .connectTimeout(3, java.util.concurrent.TimeUnit.SECONDS)
                .readTimeout(15, java.util.concurrent.TimeUnit.SECONDS)
                .build()
            val req = okhttp3.Request.Builder().url(url).get().build()
            val body = client.newCall(req).execute().use { resp ->
                if (!resp.isSuccessful) return rd.setErrorMsg("远程书源链接错误 HTTP ${resp.code()}")
                resp.body()?.string() ?: return rd.setErrorMsg("远程书源链接错误")
            }
            val arr = try {
                JsonArray(body)
            } catch (_: Exception) {
                // single object or wrapped
                try {
                    val o = JsonObject(body)
                    o.getJsonArray("data")
                        ?: o.getJsonArray("bookSources")
                        ?: JsonArray().add(o)
                } catch (_: Exception) {
                    return rd.setErrorMsg("远程书源链接错误")
                }
            }
            saveBookSources(context, arr)
        } catch (e: Exception) {
            rd.setErrorMsg(e.message ?: "远程书源链接错误")
        }
    }

    fun generateBookSourceMap(userNameSpace: String, bookSourceList: JsonArray? = null): MutableMap<String, Int> {
        val arr = bookSourceList ?: getUserBookSourceJson(userNameSpace) ?: JsonArray()
        val map = linkedMapOf<String, Int>()
        for (i in 0 until arr.size()) {
            val url = arr.getJsonObject(i).getString("bookSourceUrl") ?: continue
            map[url] = i
        }
        saveUserStorage(userNameSpace, "bookSourceMap", map)
        return map
    }

    fun getBookSourceMap(userNameSpace: String): MutableMap<String, Int> {
        val raw = getUserStorage(userNameSpace, "bookSourceMap")
        val obj = ExtKt.asJsonObject(raw)
        @Suppress("UNCHECKED_CAST")
        return (obj?.map as? MutableMap<String, Int>) ?: generateBookSourceMap(userNameSpace)
    }

    private fun param(ctx: RoutingContext, key: String): String? {
        if (ctx.request().method() == HttpMethod.POST) {
            ctx.bodyAsJson?.getString(key)?.let { return it }
        }
        return ctx.queryParam(key).firstOrNull()
    }
}
