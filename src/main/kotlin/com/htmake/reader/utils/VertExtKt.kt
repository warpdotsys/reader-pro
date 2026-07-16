package com.htmake.reader.utils

import com.htmake.reader.api.ReturnData
import io.vertx.core.http.HttpHeaders
import io.vertx.ext.web.RoutingContext

object VertExtKt {
    fun success(ctx: RoutingContext, data: Any?) {
        val body = when (data) {
            is ReturnData -> ExtKt.jsonEncode(data.toMap())
            is String -> data
            else -> ExtKt.jsonEncode(data)
        }
        if (!ctx.response().ended()) {
            ctx.response()
                .putHeader(HttpHeaders.CONTENT_TYPE, "application/json; charset=utf-8")
                .end(body)
        }
    }
}
