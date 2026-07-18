package com.htmake.reader.utils

import com.htmake.reader.entity.BasicError
import io.legado.app.utils.GSON
import io.vertx.core.Handler
import io.vertx.core.json.JsonObject
import io.vertx.ext.web.Route
import io.vertx.ext.web.RoutingContext
import mu.KotlinLogging
import org.slf4j.MDC
import java.net.URLDecoder
import java.util.UUID

private val logger = KotlinLogging.logger {}

fun RoutingContext.success(any: Any?) {
    val json = if (any is JsonObject) any.toString() else GSON.toJson(any)
    response().putHeader("content-type", "application/json; charset=utf-8").end(json)
}

fun RoutingContext.error(throwable: Throwable) {
    val path = URLDecoder.decode(request().absoluteURI(), "UTF-8")
    val error = BasicError(
        "Internal Server Error",
        throwable.toString(),
        throwable.message.toString(),
        path,
        500,
        System.currentTimeMillis()
    )
    val errorJson = GSON.toJson(error)
    logger.error("Internal Server Error", throwable)
    logger.error { errorJson }
    response().putHeader("content-type", "application/json; charset=utf-8")
        .setStatusCode(500)
        .end(errorJson)
}

fun Route.globalHandler(handler: Handler<RoutingContext>) {
    this.handler { context ->
        val traceId = context.get<String>("traceId").takeUnless { it.isNullOrEmpty() }
            ?: UUID.randomUUID().toString().substring(0, 8)
        MDC.put("traceId", traceId)
        context.put("traceId", traceId)
        handler.handle(context)
    }
}
