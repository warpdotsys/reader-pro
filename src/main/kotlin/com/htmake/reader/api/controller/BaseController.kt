package com.htmake.reader.api.controller

import com.htmake.reader.config.AppConfig
import com.htmake.reader.entity.User
import com.htmake.reader.utils.SpringContextUtils
import com.htmake.reader.utils.asJsonObject
import com.htmake.reader.utils.genEncryptedPassword
import com.htmake.reader.utils.getStorage
import com.htmake.reader.utils.getWorkDir
import com.htmake.reader.utils.gson
import com.htmake.reader.utils.saveStorage
import com.htmake.reader.utils.toMap
import io.legado.app.utils.FileUtils
import io.vertx.core.json.Json
import io.vertx.ext.web.RoutingContext
import java.io.File
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Deferred
import kotlinx.coroutines.async
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import mu.KLogger
import mu.KotlinLogging
import org.springframework.core.env.Environment
import kotlin.coroutines.CoroutineContext

private val logger = KotlinLogging.logger {}

@JvmSynthetic
fun `access$getLogger$p`(): KLogger = logger

open class BaseController(
    override val coroutineContext: CoroutineContext,
) : CoroutineScope {
    private var loginExpireDays = 7

    val appConfig: AppConfig = requireNotNull(
        SpringContextUtils.getBean("appConfig", AppConfig::class.java)
    )
    val env: Environment = requireNotNull(SpringContextUtils.getBean(Environment::class.java))
    val userMutex: Mutex = Mutex()

    suspend fun saveUserSession(
        context: RoutingContext,
        user: User,
        regenerateToken: Boolean = false,
    ): Map<String, Any> = userMutex.withLock {
        user.last_login_at = System.currentTimeMillis()
        if (regenerateToken) {
            user.token = genEncryptedPassword(user.username, System.currentTimeMillis().toString())
            val expiresAt = System.currentTimeMillis() + loginExpireDays * 86_400_000L
            val tokenMap = (user.token_map as? MutableMap<String, Long>)
                ?: mutableMapOf(user.token to expiresAt)
            tokenMap[user.token] = expiresAt
            tokenMap.entries.removeAll { it.value < System.currentTimeMillis() }
            user.token_map = tokenMap
        }

        val users = getStorage("data", "users").asJsonObject()?.map?.toMutableMap()
            ?: linkedMapOf()
        users[user.username] = user.toMap()
        saveStorage("data", "users", value = Json.encode(users))

        formatUser(user).also {
            context.session().put("username", user.username)
            context.put("username", user.username)
        }
    }

    suspend fun checkAuth(context: RoutingContext): Boolean {
        if (!appConfig.secure) {
            return true
        }

        getUserInfoClass(context.session().get<String>("username") ?: "")?.let { user ->
            context.put("username", user.username)
            context.put("userInfo", user)
            return true
        }

        val accessToken = context.queryParam("accessToken").firstOrNull().orEmpty()
        if (accessToken.isEmpty()) {
            return false
        }
        val tokenParts = accessToken.split(":", limit = 2)
        if (tokenParts.size < 2) {
            return false
        }

        val user = getUserInfoMap(tokenParts[0])?.let {
            gson.fromJson(gson.toJson(it), User::class.java)
        } ?: return false
        val token = tokenParts[1]
        var authenticated = user.token.isNotEmpty() && user.token == token
        val tokenMap = user.token_map as? MutableMap<String, Long>
        if (!authenticated && tokenMap?.containsKey(token) == true) {
            if ((tokenMap[token] ?: 0L) > System.currentTimeMillis()) {
                authenticated = true
                tokenMap[token] = System.currentTimeMillis() + loginExpireDays * 86_400_000L
            } else {
                tokenMap.remove(token)
            }
            user.token_map = tokenMap
        }
        if (!authenticated) {
            return false
        }

        saveUserSession(context, user)
        context.put("username", user.username)
        context.put("userInfo", user)
        return true
    }

    fun checkManagerAuth(context: RoutingContext): Boolean {
        if (!appConfig.secure || appConfig.secureKey.isEmpty()) {
            return true
        }

        if (context.queryParam("secureKey").firstOrNull().orEmpty() != appConfig.secureKey) {
            return false
        }

        val userNameSpace = context.queryParam("userNS").firstOrNull()
        if (userNameSpace.isNullOrEmpty()) {
            context.remove("userNameSpace")
        } else {
            context.put("userNameSpace", userNameSpace)
        }
        return true
    }

    fun getUserNameSpace(context: RoutingContext): String {
        if (!appConfig.secure) {
            return "default"
        }
        checkManagerAuth(context)
        return context.get<String>("userNameSpace")?.takeIf { it.isNotEmpty() }
            ?: context.get<String>("username")
            ?: "default"
    }

    fun getUserStorage(context: Any, vararg path: String): String? {
        val userNameSpace = userNameSpace(context)
        return if (userNameSpace.isEmpty()) {
            getStorage("data", *path)
        } else {
            getStorage("data", userNameSpace, *path)
        }
    }

    fun saveUserStorage(context: Any, path: String, value: Any) {
        val userNameSpace = userNameSpace(context)
        if (userNameSpace.isEmpty()) {
            saveStorage("data", path, value = value)
        } else {
            saveStorage("data", userNameSpace, path, value = value)
        }
    }

    fun getUserInfoClass(username: String): User? = getUserInfoMap(username)?.let {
        gson.fromJson(gson.toJson(it), User::class.java)
    }

    @Suppress("UNCHECKED_CAST")
    fun getUserInfoMap(username: String): Map<String, Any>? {
        if (username.isEmpty()) {
            return null
        }
        val users = getStorage("data", "users").asJsonObject()?.map as? Map<String, Map<String, Any>>
            ?: linkedMapOf()
        return users[username]
    }

    fun formatUser(userInfo: Any): Map<String, Any> {
        val user = when (userInfo) {
            is User -> userInfo
            is Map<*, *> -> gson.fromJson(gson.toJson(userInfo), User::class.java)
            else -> null
        } ?: return linkedMapOf()

        return linkedMapOf(
            "username" to user.username,
            "lastLoginAt" to user.last_login_at,
            "accessToken" to "${user.username}:${user.token}",
            "enableWebdav" to user.enable_webdav,
            "enableLocalStore" to user.enable_local_store,
            "enableBookSource" to user.enable_book_source,
            "enableRssSource" to user.enable_rss_source,
            "bookSourceLimit" to user.book_source_limit,
            "bookLimit" to user.book_limit,
            "createdAt" to user.created_at,
        )
    }

    fun getUserWebdavHome(context: Any): String {
        var basePath = getWorkDir("storage", "data")
        val userNameSpace = userNameSpace(context)
        if (userNameSpace.isNotEmpty()) {
            basePath += File.separator + userNameSpace
        }
        val home = basePath + File.separator + "webdav"
        File(home).takeIf { !it.exists() }?.mkdirs()
        return home
    }

    fun getFileExt(url: String, defaultExt: String = ""): String =
        FileUtils.getFileExtetion(url, defaultExt)

    suspend fun limitConcurrent(
        concurrentCount: Int,
        startIndex: Int,
        endIndex: Int,
        handler: suspend CoroutineScope.(Int) -> Any?,
    ) {
        limitConcurrent(concurrentCount, startIndex, endIndex, handler) { _, _ -> true }
    }

    suspend fun limitConcurrent(
        concurrentCount: Int,
        startIndex: Int,
        endIndex: Int,
        handler: suspend CoroutineScope.(Int) -> Any?,
        shouldContinue: (ArrayList<Any?>, Int) -> Boolean,
    ) = coroutineScope {
        var current = startIndex
        val parallelism = concurrentCount.coerceAtLeast(1)
        while (current < endIndex) {
            val tasks = ArrayList<Deferred<Any?>>()
            while (tasks.size < parallelism && current < endIndex) {
                val index = current++
                tasks += async { handler(index) }
            }
            val results = ArrayList<Any?>(tasks.size)
            tasks.forEach { results += it.await() }
            if (!shouldContinue(results, current)) {
                break
            }
        }
    }

    private fun userNameSpace(context: Any): String = when (context) {
        is RoutingContext -> getUserNameSpace(context)
        is String -> context
        else -> ""
    }
}
