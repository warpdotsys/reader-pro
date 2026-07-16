/**
 * Business-oriented rewrite from reader-pro-3.2.14.jar reverse engineering.
 * Source: CFR/Vineflower decompilation + manual semantic cleanup.
 * Purpose: readability / audit — not drop-in recompilation of the original APK/JAR.
 */
package com.htmake.reader.api.controller

import com.htmake.reader.config.AppConfig
import com.htmake.reader.entity.User
import com.htmake.reader.utils.ExtKt
import com.htmake.reader.utils.SpringContextUtils
import com.htmake.reader.utils.UserMutex
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

/**
 * Base for all REST controllers: auth, multi-user storage paths, concurrency helpers.
 */
open class BaseController(
    override val coroutineContext: CoroutineContext
) : CoroutineScope {

    protected val appConfig: AppConfig =
        SpringContextUtils.getBean("appConfig", AppConfig::class.java)
    protected val env: Environment =
        SpringContextUtils.getBean(Environment::class.java)
    protected val userMutex: Mutex = Mutex()
    protected var loginExpireDays: Int = 7

    fun getAppConfig(): AppConfig = appConfig
    fun getEnv(): Environment = env
    fun getUserMutex(): Mutex = userMutex

    /** Session / token auth. */
    open suspend fun checkAuth(context: RoutingContext): Boolean {
        if (!appConfig.secure) return true
        val username = context.session().get<String>("username")
        if (!username.isNullOrEmpty()) return true
        // accessToken query: "user:token"
        val accessToken = context.queryParam("accessToken").firstOrNull().orEmpty()
        if (accessToken.isEmpty()) return false
        val parts = accessToken.split(":", limit = 2)
        if (parts.size < 2) return false
        val user = parts[0]
        val token = parts[1]
        val users = loadUserMap()
        val info = users[user] ?: return false
        val tokenMap = info["token_map"] as? Map<*, *>
        if (tokenMap != null && tokenMap.containsKey(token)) {
            context.session().put("username", user)
            return true
        }
        if (info["token"] == token) {
            context.session().put("username", user)
            return true
        }
        return false
    }

    /** Manager / secureKey auth. */
    open fun checkManagerAuth(context: RoutingContext): Boolean {
        if (!appConfig.secure) return true
        val key = context.queryParam("secureKey").firstOrNull()
            ?: context.request().getHeader("secureKey")
            ?: ""
        return key.isNotEmpty() && key == appConfig.secureKey
    }

    open fun getUserNameSpace(context: RoutingContext): String {
        if (!appConfig.secure) return "default"
        return context.session().get<String>("username") ?: "default"
    }

    open fun getUserWebdavHome(userNameSpace: String): String {
        return ExtKt.getWorkDir("storage", "data", userNameSpace, "webdav")
    }

    open fun getUserStorage(userNameSpace: String, vararg path: String): String? {
        return ExtKt.getStorage(*arrayOf("data", userNameSpace) + path)
    }

    open fun saveUserStorage(userNameSpace: String, name: String, value: Any?) {
        val encoded = when (value) {
            is String -> value
            else -> Json.encode(value)
        }
        ExtKt.saveStorage(arrayOf("data", userNameSpace, name), encoded)
    }

    open fun getFileExt(url: String, defaultExt: String = "jpg"): String {
        val pure = url.substringBefore('?').substringBefore('#')
        val name = pure.substringAfterLast('/')
        val ext = name.substringAfterLast('.', "")
        return if (ext.isNotEmpty() && ext.length <= 5) ext else defaultExt
    }

    @Suppress("UNCHECKED_CAST")
    protected fun loadUserMap(): MutableMap<String, MutableMap<String, Any>> {
        val raw = ExtKt.getStorage("data", "users")
        val obj = ExtKt.asJsonObject(raw) ?: return linkedMapOf()
        return (obj.map as? MutableMap<String, MutableMap<String, Any>>) ?: linkedMapOf()
    }

    protected fun saveUserMap(map: Map<String, *> ) {
        ExtKt.saveStorage(arrayOf("data", "users"), Json.encode(map))
    }

    open suspend fun saveUserSession(
        context: RoutingContext,
        user: User,
        regenerateToken: Boolean = false
    ): Map<String, Any?> {
        context.session().put("username", user.username)
        // token bookkeeping simplified; original mutates users storage under mutex
        return mapOf(
            "username" to user.username,
            "token" to (user.token ?: ""),
            "isManager" to user.isManager
        )
    }

    /**
     * Run [handler] for indices [startIndex, endIndex) with at most [concurrentCount] in flight.
     * Business rewrite of limitConcurrent used by bookshelf refresh etc.
     */
    open suspend fun limitConcurrent(
        concurrentCount: Int,
        startIndex: Int,
        endIndex: Int,
        handler: suspend CoroutineScope.(index: Int) -> Any?
    ) = coroutineScope {
        val jobs = ArrayList<kotlinx.coroutines.Deferred<Any?>>()
        var i = startIndex
        while (i < endIndex) {
            while (jobs.size >= concurrentCount) {
                jobs.removeAt(0).await()
            }
            val idx = i++
            jobs += async { handler(idx) }
        }
        jobs.awaitAll()
    }
}
