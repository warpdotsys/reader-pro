package com.htmake.reader.api.controller

import com.htmake.reader.api.ReturnData
import com.htmake.reader.utils.ExtKt
import io.legado.app.data.entities.SearchBook
import io.legado.app.model.Debugger
import io.legado.app.model.webBook.BookList
import io.legado.app.model.webBook.WebBook
import io.vertx.core.json.JsonArray
import io.vertx.core.json.JsonObject
import io.vertx.ext.web.RoutingContext
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.withTimeoutOrNull

suspend fun BookController.exploreBook(ctx: RoutingContext): ReturnData {
    val rd = ReturnData()
    if (!checkAuth(ctx)) return rd.setData("NEED_LOGIN").setErrorMsg("请登录后使用")
    var url = ctx.queryParam("url").firstOrNull() ?: ctx.bodyAsJson?.getString("url")
        ?: ctx.queryParam("sortUrl").firstOrNull() ?: ctx.bodyAsJson?.getString("sortUrl") ?: ""
    val page = ctx.queryParam("page").firstOrNull()?.toIntOrNull() ?: ctx.bodyAsJson?.getInteger("page") ?: 1
    val ns = getUserNameSpace(ctx)
    val sourceUrl = ctx.queryParam("bookSourceUrl").firstOrNull() ?: ctx.bodyAsJson?.getString("bookSourceUrl")
    val sourceStr = sourceUrl?.let { getBookSourceStringBySourceURLOpt(it, ns) } ?: return rd.setErrorMsg("书源信息错误")
    if (url.isBlank()) {
        val src = io.legado.app.data.entities.BookSource.fromJson(sourceStr).getOrNull()
        val cats = BookList.parseExploreUrl(src?.exploreUrl, page)
        return rd.setData(cats.map { (t, u) -> mapOf("title" to t, "url" to u) })
    }
    val list = withTimeoutOrNull(30_000L) {
        WebBook(sourceStr, appConfig.debugLog, null, ns).exploreBook(url, page)
    } ?: emptyList()
    return rd.setData(list)
}

suspend fun BookController.searchBookMulti(ctx: RoutingContext): ReturnData {
    val rd = ReturnData()
    if (!checkAuth(ctx)) return rd.setData("NEED_LOGIN").setErrorMsg("请登录后使用")
    val key = ctx.queryParam("key").firstOrNull() ?: ctx.bodyAsJson?.getString("key") ?: return rd.setErrorMsg("请输入关键字")
    val page = ctx.queryParam("page").firstOrNull()?.toIntOrNull() ?: 1
    val concurrent = (ctx.queryParam("concurrentCount").firstOrNull()?.toIntOrNull() ?: 36).coerceIn(1, 64)
    val ns = getUserNameSpace(ctx)
    val sources = ExtKt.asJsonArray(getUserStorage(ns, "bookSource")) ?: JsonArray()
    val enabled = (0 until sources.size()).mapNotNull { i ->
        val o = sources.getJsonObject(i) ?: return@mapNotNull null
        if (o.getBoolean("enabled", true) == false) null
        else if (isInvalidBookSource(o.getString("bookSourceUrl") ?: "", ns)) null
        else o
    }
    val out = ArrayList<SearchBook>()
    coroutineScope {
        enabled.chunked(concurrent).forEach { batch ->
            batch.map { o ->
                async {
                    try {
                        withTimeoutOrNull(15_000L) {
                            WebBook(o.encode(), false, null, ns).searchBook(key, page)
                        } ?: emptyList()
                    } catch (_: Exception) {
                        emptyList()
                    }
                }
            }.awaitAll().forEach { out.addAll(it) }
        }
    }
    return rd.setData(out.distinctBy { it.bookUrl.ifEmpty { "${it.name}|${it.author}" } })
}

suspend fun BookController.searchBookMultiSSE(ctx: RoutingContext) {
    val key = ctx.queryParam("key").firstOrNull() ?: ""
    val page = ctx.queryParam("page").firstOrNull()?.toIntOrNull() ?: 1
    val concurrent = (ctx.queryParam("concurrentCount").firstOrNull()?.toIntOrNull() ?: 24).coerceIn(1, 64)
    val ns = getUserNameSpace(ctx)
    val sources = ExtKt.asJsonArray(getUserStorage(ns, "bookSource")) ?: JsonArray()
    val resp = ctx.response().putHeader("Content-Type", "text/event-stream; charset=utf-8")
        .putHeader("Cache-Control", "no-cache").setChunked(true)
    val enabled = (0 until sources.size()).mapNotNull { i ->
        val o = sources.getJsonObject(i) ?: return@mapNotNull null
        if (o.getBoolean("enabled", true) == false) null else o
    }
    coroutineScope {
        enabled.chunked(concurrent).forEach { batch ->
            batch.map { o ->
                async {
                    try {
                        val list = withTimeoutOrNull(15_000L) {
                            WebBook(o.encode(), false, null, ns).searchBook(key, page)
                        } ?: emptyList()
                        val payload = JsonObject().put("origin", o.getString("bookSourceUrl"))
                            .put("data", JsonArray(list.map { JsonObject.mapFrom(it) }))
                        synchronized(resp) { if (!resp.ended()) resp.write("data: ${payload.encode()}\n\n") }
                    } catch (_: Exception) {
                    }
                }
            }.awaitAll()
        }
    }
    if (!resp.ended()) resp.write("event: end\ndata: []\n\n").end()
}

suspend fun BookController.bookSourceDebugSSE(ctx: RoutingContext) {
    val ns = getUserNameSpace(ctx)
    val sourceUrl = ctx.queryParam("bookSourceUrl").firstOrNull() ?: ""
    val key = ctx.queryParam("key").firstOrNull() ?: ""
    val source = getBookSourceStringBySourceURLOpt(sourceUrl, ns)
    val resp = ctx.response().putHeader("Content-Type", "text/event-stream").setChunked(true)
    if (source == null) {
        resp.end("event: error\ndata: {\"error\":\"书源不存在\"}\n\n"); return
    }
    val debugger = Debugger { msg ->
        if (!resp.ended()) resp.write("data: ${JsonObject().put("msg", msg).encode()}\n\n")
    }
    try {
        debugger.startDebug(WebBook(source, true, debugger, ns), key)
    } catch (e: Exception) {
        if (!resp.ended()) resp.write("event: error\ndata: ${JsonObject().put("error", e.message).encode()}\n\n")
    }
    if (!resp.ended()) resp.write("event: end\ndata: {}\n\n").end()
}

suspend fun BookController.getInvalidBookSources(ctx: RoutingContext): ReturnData {
    val rd = ReturnData()
    if (!checkAuth(ctx)) return rd.setData("NEED_LOGIN").setErrorMsg("请登录后使用")
    // simplified empty list if none
    return rd.setData(emptyList<Any>())
}

fun BookController.getTxtTocRules(ctx: RoutingContext): ReturnData =
    ReturnData().setData(io.legado.app.help.DefaultData.txtTocRules)
