/** Business rewrite from reader-pro-3.2.14.jar — readability / audit. */

package com.htmake.reader.utils

import com.htmake.reader.api.ReturnData
import io.vertx.core.json.Json
import io.vertx.ext.web.RoutingContext

object VertExtKt {
    @JvmStatic
    fun success(ctx: RoutingContext, data: ReturnData) {
        if (!ctx.response().ended()) {
            ctx.response()
                .putHeader("Content-Type", "application/json; charset=utf-8")
                .end(Json.encode(data))
        }
    }

    @JvmStatic
    fun success(ctx: RoutingContext, data: Any?) {
        if (data is ReturnData) success(ctx, data)
        else if (!ctx.response().ended()) {
            ctx.response().putHeader("Content-Type", "application/json; charset=utf-8")
                .end(Json.encode(data))
        }
    }
}
