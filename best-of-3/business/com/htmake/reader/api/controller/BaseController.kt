package com.htmake.reader.api.controller

import com.htmake.reader.config.AppConfig
import com.htmake.reader.utils.ExtKt
import com.htmake.reader.utils.SpringContextUtils
import io.vertx.core.json.Json
import io.vertx.core.json.JsonArray
import io.vertx.core.json.JsonObject
import io.vertx.ext.web.RoutingContext
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import org.springframework.core.env.Environment
import kotlin.coroutines.CoroutineContext

open class BaseController(
    override val coroutineContext: CoroutineContext
) : CoroutineScope {

    val appConfig: AppConfig by lazy {
        try {
            SpringContextUtils.getBean("appConfig", AppConfig::class.java)
        } catch (_: Exception) {
            AppConfig()
        }
    }

    protected val env: Environment by lazy {
        try {
            SpringContextUtils.getBean(Environment::class.java)
        } catch (_: Exception) {
            error("Environment not ready")
        }
    }

    open suspend fun checkAuth(context: RoutingContext): Boolean {
        if (!appConfig.secure) return true
        val username = context.session()?.get<String>("username")
        if (!username.isNullOrEmpty()) return true
        // accessToken from query / header / Authorization Bearer
        val accessToken = context.queryParam("accessToken").firstOrNull().orEmpty()
            .ifBlank { context.request().getHeader("accessToken").orEmpty() }
            .ifBlank {
                val auth = context.request().getHeader("Authorization").orEmpty()
                when {
                    auth.startsWith("Bearer ", true) -> auth.substring(7).trim()
                    auth.startsWith("Token ", true) -> auth.substring(6).trim()
                    else -> ""
                }
            }
        if (accessToken.isEmpty()) return false
        val parts = accessToken.split(":", limit = 2)
        if (parts.size < 2) return false
        val users = loadUserMap()
        val info = users[parts[0]] ?: return false
        @Suppress("UNCHECKED_CAST")
        val tokenMap = info["token_map"] as? Map<*, *>
        if (tokenMap != null && tokenMap.containsKey(parts[1])) {
            context.session()?.put("username", parts[0])
            return true
        }
        if (info["token"] == parts[1]) {
            context.session()?.put("username", parts[0])
            return true
        }
        return false
    }

    open fun checkManagerAuth(context: RoutingContext): Boolean {
        if (!appConfig.secure) return true
        val key = context.queryParam("secureKey").firstOrNull()
            ?: context.request().getHeader("secureKey")
            ?: ""
        return key.isNotEmpty() && key == appConfig.secureKey
    }

    open fun getUserNameSpace(context: RoutingContext): String {
        if (!appConfig.secure) return "default"
        return context.session()?.get<String>("username") ?: "default"
    }

    open fun getUserWebdavHome(userNameSpace: String): String =
        ExtKt.getWorkDir("storage", "data", userNameSpace, "webdav")

    open fun getUserStorage(userNameSpace: String, vararg path: String): String? =
        ExtKt.getStorage(*arrayOf("data", userNameSpace) + path)

    open fun saveUserStorage(userNameSpace: String, name: String, value: Any?) {
        val encoded = when (value) {
            is String -> value
            else -> Json.encode(value)
        }
        ExtKt.saveStorage(arrayOf("data", userNameSpace, name), encoded)
    }

    @Suppress("UNCHECKED_CAST")
    protected fun loadUserMap(): MutableMap<String, MutableMap<String, Any?>> {
        val raw = ExtKt.getStorage("data", "users")
        val obj = ExtKt.asJsonObject(raw) ?: return linkedMapOf()
        val out = linkedMapOf<String, MutableMap<String, Any?>>()
        obj.forEach { (k, v) ->
            out[k] = when (v) {
                is JsonObject -> v.map.toMutableMap()
                is Map<*, *> -> (v as Map<String, Any?>).toMutableMap()
                else -> mutableMapOf()
            }
        }
        return out
    }

    protected fun saveUserMap(map: Map<String, *>) {
        ExtKt.saveStorage(arrayOf("data", "users"), JsonObject(map as Map<String, Any?>).encode())
    }

    open suspend fun limitConcurrent(
        concurrent: Int,
        start: Int,
        end: Int,
        block: suspend (Int) -> Boolean
    ) = coroutineScope {
        val n = concurrent.coerceAtLeast(1)
        (start until end).chunked(n).forEach { batch ->
            batch.map { i -> async { block(i) } }.awaitAll()
        }
    }
}
