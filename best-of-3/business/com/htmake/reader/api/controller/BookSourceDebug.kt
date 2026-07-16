/** Business rewrite from reader-pro-3.2.14.jar — phase6. */

package com.htmake.reader.api.controller

import com.htmake.reader.api.ReturnData
import com.htmake.reader.utils.ExtKt
import io.legado.app.model.Debugger
import io.legado.app.model.webBook.WebBook
import io.vertx.core.json.Json
import io.vertx.ext.web.RoutingContext
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

/**
 * bookSourceDebugSSE: stream debug lines while Debugger runs search/info/toc/content.
 */
suspend fun BookController.bookSourceDebugSSE(context: RoutingContext) {
    val response = context.response()
        .putHeader("Content-Type", "text/event-stream; charset=utf-8")
        .putHeader("Cache-Control", "no-cache")
        .setChunked(true)

    fun emit(map: Map<String, Any?>) {
        if (!response.ended()) {
            response.write("data: ${ExtKt.jsonEncode(map)}\n\n")
        }
    }

    val ns = getUserNameSpace(context)
    val keyword = context.queryParam("keyword").firstOrNull()
        ?: context.queryParam("key").firstOrNull()
        ?: ""
    val sourceUrl = context.queryParam("bookSourceUrl").firstOrNull()
        ?: context.queryParam("url").firstOrNull()
    val sourceStr = when {
        !sourceUrl.isNullOrEmpty() -> getBookSourceStringBySourceURLOpt(sourceUrl, ns)
        else -> context.queryParam("bookSource").firstOrNull()
    }

    if (sourceStr.isNullOrEmpty()) {
        emit(mapOf("msg" to "未配置书源"))
        response.write("event: end\n")
        response.end("data: ${ExtKt.jsonEncode(mapOf("end" to true))}\n\n")
        return
    }
    if (keyword.isEmpty()) {
        emit(mapOf("msg" to "请输入关键字或书籍URL"))
        response.write("event: end\n")
        response.end("data: ${ExtKt.jsonEncode(mapOf("end" to true))}\n\n")
        return
    }

    context.request().connection().closeHandler {
        // client disconnected
    }

    val debugger = Debugger { msg -> emit(mapOf("msg" to msg)) }
    val webBook = WebBook(sourceStr, false, debugger, ns)
    try {
        withContext(Dispatchers.IO) {
            debugger.startDebug(webBook, keyword)
        }
    } catch (e: Exception) {
        emit(mapOf("msg" to "※调试异常: ${e.message}"))
    }
    response.write("event: end\n")
    response.end("data: ${ExtKt.jsonEncode(mapOf("end" to true))}\n\n")
}
