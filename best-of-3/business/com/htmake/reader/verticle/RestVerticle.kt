/**
 * Business-oriented rewrite from reader-pro-3.2.14.jar reverse engineering.
 * Source: CFR/Vineflower + manual semantic cleanup. For audit/readability.
 */

package com.htmake.reader.verticle

import com.htmake.reader.utils.ExtKt
import io.vertx.core.http.HttpMethod
import io.vertx.ext.web.Router
import io.vertx.ext.web.RoutingContext
import io.vertx.ext.web.handler.BodyHandler
import io.vertx.ext.web.handler.LoggerHandler
import io.vertx.ext.web.handler.SessionHandler
import io.vertx.ext.web.sstore.LocalSessionStore
import io.vertx.kotlin.coroutines.CoroutineVerticle
import kotlinx.coroutines.launch

/**
 * HTTP server base: session, body, CORS-ish hooks, coroutine route helpers.
 */
abstract class RestVerticle : CoroutineVerticle() {
    protected lateinit var router: Router
    var port: Int = 8080

    abstract fun getContextPath(): String
    abstract suspend fun initRouter(router: Router)

    override suspend fun start() {
        router = Router.router(vertx)
        router.route().handler(LoggerHandler.create())
        router.route().handler(BodyHandler.create().setUploadsDirectory(ExtKt.getWorkDir("storage", "cache", "uploads")))
        router.route().handler(
            SessionHandler.create(LocalSessionStore.create(vertx)).setNagHttps(false)
        )
        // CORS preflight
        router.route().handler { ctx ->
            ctx.response()
                .putHeader("Access-Control-Allow-Origin", ctx.request().getHeader("Origin") ?: "*")
                .putHeader("Access-Control-Allow-Credentials", "true")
            if (ctx.request().method() == HttpMethod.OPTIONS) {
                ctx.response()
                    .putHeader("Access-Control-Allow-Methods", "GET,POST,PUT,DELETE,OPTIONS")
                    .putHeader("Access-Control-Allow-Headers", "Content-Type,Authorization,secureKey")
                    .end()
            } else ctx.next()
        }
        try {
            initRouter(router)
            vertx.createHttpServer().requestHandler(router).listen(port)
            started()
        } catch (e: Exception) {
            onStartError()
            throw e
        }
    }

    open fun started() {}
    open fun onStartError() {}
    open fun onHandlerError(ctx: RoutingContext, error: Exception) {
        error.printStackTrace()
        if (!ctx.response().ended()) {
            ctx.response().setStatusCode(500).end(error.message ?: "error")
        }
    }
}
