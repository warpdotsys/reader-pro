# -*- coding: utf-8 -*-
"""
Generate business-oriented Kotlin rewrites under best-of-3/business/
from decompiled sources + CFR semantics. These are reverse-engineered
readable suspend APIs, not bit-identical recompiles.
"""
from __future__ import annotations

import os
import re
import textwrap
from pathlib import Path

ROOT = Path(r"C:\Users\chong\reader-pro-3.2.14-reverse")
SRC = ROOT / "best-of-3" / "src"
BIZ = ROOT / "best-of-3" / "business"
CFR = ROOT / "full-cfr"

BIZ.mkdir(parents=True, exist_ok=True)

HEADER = '''\
/**
 * Business-oriented rewrite from reader-pro-3.2.14.jar reverse engineering.
 * Source: CFR/Vineflower decompilation + manual semantic cleanup.
 * Purpose: readability / audit — not drop-in recompilation of the original APK/JAR.
 */
'''


def write(rel: str, content: str):
    path = BIZ / rel.replace("/", os.sep)
    path.parent.mkdir(parents=True, exist_ok=True)
    path.write_text(HEADER + content.lstrip("\n"), encoding="utf-8", newline="\n")
    print("wrote", rel, "lines", content.count("\n") + 1)


# ---------------------------------------------------------------------------
# Shared / foundation
# ---------------------------------------------------------------------------

write(
    "com/htmake/reader/api/ReturnData.kt",
    r'''
package com.htmake.reader.api

/**
 * Uniform API response envelope used by /reader3/* handlers.
 */
class ReturnData {
    var isSuccess: Boolean = true
    var errorMsg: String = ""
    var data: Any? = null

    fun setErrorMsg(msg: String): ReturnData {
        isSuccess = false
        errorMsg = msg
        return this
    }

    fun setData(value: Any?, ignored: Any? = null): ReturnData {
        data = value
        isSuccess = true
        return this
    }

    companion object {
        /** CFR: ReturnData.setData$default */
        @JvmStatic
        fun setDataDefault(rd: ReturnData, value: Any?, ignored: Any? = null, mask: Int = 2, conf: Any? = null): ReturnData {
            return rd.setData(value)
        }
    }
}
''',
)

write(
    "com/htmake/reader/api/controller/BaseController.kt",
    r'''
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
''',
)

write(
    "com/htmake/reader/api/controller/UserController.kt",
    r'''
package com.htmake.reader.api.controller

import com.htmake.reader.api.ReturnData
import com.htmake.reader.entity.User
import com.htmake.reader.utils.ExtKt
import io.vertx.core.http.HttpMethod
import io.vertx.core.json.Json
import io.vertx.core.json.JsonArray
import io.vertx.ext.web.RoutingContext
import kotlinx.coroutines.sync.withLock
import java.util.UUID
import kotlin.coroutines.CoroutineContext
import kotlin.text.Regex
import kotlin.text.RegexOption

/**
 * Multi-user: login/logout, admin user CRUD, user config, backup file download.
 * Routes: /reader3/login|logout|getUserInfo|getUserList|addUser|...
 */
class UserController(coroutineContext: CoroutineContext) : BaseController(coroutineContext) {

    fun getUserLimit(context: RoutingContext): Int {
        // original also considers license max users
        return appConfig.userLimit.coerceAtLeast(1)
    }

    suspend fun login(context: RoutingContext): ReturnData {
        val rd = ReturnData()
        val body = context.bodyAsJson
        val username = body.getString("username", "") ?: ""
        val password = body.getString("password", "") ?: ""
        val isLogin = body.getBoolean("isLogin", false) ?: false

        if (username.isEmpty()) return rd.setErrorMsg("请输入用户名")
        if (password.isEmpty()) return rd.setErrorMsg("请输入密码")

        userMutex.withLock {
            val users = loadUserMap()
            val existed = users[username]

            if (isLogin) {
                // ---- login path ----
                if (existed == null) return rd.setErrorMsg("用户不存在")
                val enc = ExtKt.genEncryptedPassword(password, existed["salt"] as? String ?: "")
                if (enc != existed["password"]) return rd.setErrorMsg("密码错误")
                val user = mapToUser(username, existed)
                val session = saveUserSession(context, user, regenerateToken = true)
                return rd.setData(session)
            }

            // ---- register path ----
            if (username.length < 5) return rd.setErrorMsg("用户名不能低于5位")
            val minPwd = appConfig.minUserPasswordLength
            if (password.length < minPwd) return rd.setErrorMsg("密码不能低于${minPwd}位")
            if (username == "default") return rd.setErrorMsg("用户名不能为非法字符")
            if (!Regex("[a-z0-9]+", RegexOption.IGNORE_CASE).matches(username)) {
                return rd.setErrorMsg("用户名只能由字母和数字组成")
            }
            if (appConfig.inviteCode.isNotEmpty()) {
                val code = body.getString("code") ?: ""
                if (code.isEmpty()) return rd.setErrorMsg("请输入邀请码")
                if (code != appConfig.inviteCode) return rd.setErrorMsg("邀请码错误")
            }
            if (users.size >= getUserLimit(context)) return rd.setErrorMsg("超过用户数上限")
            if (existed != null) return rd.setErrorMsg("用户名已被占用")

            val salt = UUID.randomUUID().toString().replace("-", "").take(8)
            val passwordEncrypted = ExtKt.genEncryptedPassword(password, salt)
            val newUser = linkedMapOf<String, Any>(
                "username" to username,
                "password" to passwordEncrypted,
                "salt" to salt,
                "token" to "",
                "token_map" to linkedMapOf<String, Any>(),
                "enableWebdav" to appConfig.defaultUserEnableWebdav,
                "enableLocalStore" to appConfig.defaultUserEnableLocalStore,
                "enableBookSource" to appConfig.defaultUserEnableBookSource,
                "enableRssSource" to appConfig.defaultUserEnableRssSource,
                "bookSourceLimit" to appConfig.defaultUserBookSourceLimit,
                "bookLimit" to appConfig.defaultUserBookLimit,
                "last_login_at" to System.currentTimeMillis(),
                "created_at" to System.currentTimeMillis(),
            )
            users[username] = newUser
            saveUserMap(users)
            val user = mapToUser(username, newUser)
            val session = saveUserSession(context, user, regenerateToken = true)
            return rd.setData(session)
        }
    }

    suspend fun logout(context: RoutingContext): ReturnData {
        val rd = ReturnData()
        if (!checkAuth(context)) return rd.setData("NEED_LOGIN").setErrorMsg("请登录后使用")
        if (!appConfig.secure) return rd.setErrorMsg("不支持的操作")
        val username = context.session().get<String>("username") ?: ""
        context.session().destroy()
        var accessToken = context.queryParam("accessToken").firstOrNull().orEmpty()
        if (accessToken.isNotEmpty()) {
            val parts = accessToken.split(":", limit = 2)
            if (parts.size >= 2) {
                accessToken = parts[1]
                userMutex.withLock {
                    val users = loadUserMap()
                    val current = users[username] ?: return@withLock
                    val tokenMap = current["token_map"] as? MutableMap<Any?, Any?>
                    tokenMap?.remove(accessToken)
                    if (current["token"] == accessToken) current["token"] = ""
                    users[username] = current
                    saveUserMap(users)
                }
            }
        }
        return rd.setErrorMsg("请重新登录").setData("NEED_LOGIN")
    }

    suspend fun getUserList(context: RoutingContext): ReturnData {
        val rd = ReturnData()
        if (!checkAuth(context)) return rd.setData("NEED_LOGIN").setErrorMsg("请登录后使用")
        if (!checkManagerAuth(context) && appConfig.secure) {
            // original: requires manager or secureKey for list
            if (!checkManagerAuth(context)) {
                return rd.setData("NEED_SECURE_KEY").setErrorMsg("请输入管理密码")
            }
        }
        val list = loadUserMap().entries.map { (name, info) ->
            formatUserPublic(name, info)
        }
        return rd.setData(list)
    }

    suspend fun addUser(context: RoutingContext): ReturnData {
        val rd = ReturnData()
        if (!checkAuth(context)) return rd.setData("NEED_LOGIN").setErrorMsg("请登录后使用")
        if (!checkManagerAuth(context)) return rd.setData("NEED_SECURE_KEY").setErrorMsg("请输入管理密码")
        if (!appConfig.secure) return rd.setErrorMsg("不支持的操作")

        val body = context.bodyAsJson
        val username = body.getString("username") ?: ""
        val password = body.getString("password") ?: ""
        if (username.isEmpty()) return rd.setErrorMsg("请输入用户名")
        if (password.isEmpty()) return rd.setErrorMsg("请输入密码")
        if (username.length < 5) return rd.setErrorMsg("用户名不能低于5位")
        if (password.length < 8) return rd.setErrorMsg("密码不能低于8位")
        if (username == "default") return rd.setErrorMsg("用户名不能为非法字符")
        if (!Regex("[a-z0-9]+", RegexOption.IGNORE_CASE).matches(username)) {
            return rd.setErrorMsg("用户名只能由字母和数字组成")
        }

        userMutex.withLock {
            val users = loadUserMap()
            if (users.containsKey(username)) return rd.setErrorMsg("用户已存在")
            if (users.size >= getUserLimit(context)) return rd.setErrorMsg("超过用户数上限")
            val salt = UUID.randomUUID().toString().replace("-", "").take(8)
            val enc = ExtKt.genEncryptedPassword(password, salt)
            val u = linkedMapOf<String, Any>(
                "username" to username,
                "password" to enc,
                "salt" to salt,
                "enableWebdav" to (body.getBoolean("enableWebdav") ?: appConfig.defaultUserEnableWebdav),
                "enableLocalStore" to (body.getBoolean("enableLocalStore") ?: appConfig.defaultUserEnableLocalStore),
                "enableBookSource" to (body.getBoolean("enableBookSource") ?: appConfig.defaultUserEnableBookSource),
                "enableRssSource" to (body.getBoolean("enableRssSource") ?: appConfig.defaultUserEnableRssSource),
                "bookSourceLimit" to (body.getInteger("bookSourceLimit") ?: appConfig.defaultUserBookSourceLimit),
                "bookLimit" to (body.getInteger("bookLimit") ?: appConfig.defaultUserBookLimit),
                "created_at" to System.currentTimeMillis(),
            )
            users[username] = u
            saveUserMap(users)
            return rd.setData(formatUserPublic(username, u))
        }
    }

    suspend fun resetPassword(context: RoutingContext): ReturnData {
        val rd = ReturnData()
        if (!checkAuth(context)) return rd.setData("NEED_LOGIN").setErrorMsg("请登录后使用")
        if (!checkManagerAuth(context)) return rd.setData("NEED_SECURE_KEY").setErrorMsg("请输入管理密码")
        val body = context.bodyAsJson
        val username = body.getString("username") ?: ""
        val password = body.getString("password") ?: ""
        if (username.isEmpty() || password.isEmpty()) return rd.setErrorMsg("参数错误")
        userMutex.withLock {
            val users = loadUserMap()
            val u = users[username] ?: return rd.setErrorMsg("用户不存在")
            val salt = (u["salt"] as? String) ?: UUID.randomUUID().toString().take(8)
            u["salt"] = salt
            u["password"] = ExtKt.genEncryptedPassword(password, salt)
            users[username] = u
            saveUserMap(users)
            return rd.setData(true)
        }
    }

    suspend fun deleteUsers(context: RoutingContext): ReturnData {
        val rd = ReturnData()
        if (!checkAuth(context)) return rd.setData("NEED_LOGIN").setErrorMsg("请登录后使用")
        if (!checkManagerAuth(context)) return rd.setData("NEED_SECURE_KEY").setErrorMsg("请输入管理密码")
        val arr = context.bodyAsJson.getJsonArray("users") ?: JsonArray()
        userMutex.withLock {
            val users = loadUserMap()
            for (i in 0 until arr.size()) {
                val name = arr.getString(i) ?: continue
                if (name == "default") continue
                users.remove(name)
                // original also deletes storage/data/{name}
                ExtKt.deleteRecursively(java.io.File(ExtKt.getWorkDir("storage", "data", name)))
            }
            saveUserMap(users)
            return rd.setData(true)
        }
    }

    suspend fun clearInactiveUsers(context: RoutingContext): ReturnData {
        val rd = ReturnData()
        if (!checkAuth(context)) return rd.setData("NEED_LOGIN").setErrorMsg("请登录后使用")
        if (!checkManagerAuth(context)) return rd.setData("NEED_SECURE_KEY").setErrorMsg("请输入管理密码")
        val day = context.bodyAsJson?.getInteger("day")
            ?: context.queryParam("day").firstOrNull()?.toIntOrNull()
            ?: appConfig.autoClearInactiveUser
        clearInactiveUsers(day)
        return rd.setData(true)
    }

    fun clearInactiveUsers(day: Int) {
        if (day <= 0) return
        val deadline = System.currentTimeMillis() - day * 24L * 3600_000
        val users = loadUserMap()
        val victims = users.filter { (name, info) ->
            name != "default" && ((info["last_login_at"] as? Number)?.toLong() ?: 0L) < deadline
        }.keys.toList()
        victims.forEach { users.remove(it) }
        saveUserMap(users)
    }

    suspend fun updateUser(context: RoutingContext): ReturnData {
        val rd = ReturnData()
        if (!checkAuth(context)) return rd.setData("NEED_LOGIN").setErrorMsg("请登录后使用")
        if (!checkManagerAuth(context)) return rd.setData("NEED_SECURE_KEY").setErrorMsg("请输入管理密码")
        val body = context.bodyAsJson
        val username = body.getString("username") ?: ""
        if (username.isEmpty()) return rd.setErrorMsg("请输入用户名")
        userMutex.withLock {
            val users = loadUserMap()
            val u = users[username] ?: return rd.setErrorMsg("用户不存在")
            listOf(
                "enableWebdav", "enableLocalStore", "enableBookSource", "enableRssSource"
            ).forEach { k ->
                if (body.containsKey(k)) u[k] = body.getBoolean(k)
            }
            listOf("bookSourceLimit", "bookLimit").forEach { k ->
                if (body.containsKey(k)) u[k] = body.getInteger(k)
            }
            users[username] = u
            saveUserMap(users)
            return rd.setData(formatUserPublic(username, u))
        }
    }

    suspend fun getUserInfo(context: RoutingContext): ReturnData {
        val rd = ReturnData()
        if (!checkAuth(context)) return rd.setData("NEED_LOGIN").setErrorMsg("请登录后使用")
        val ns = getUserNameSpace(context)
        val users = loadUserMap()
        val info = users[ns]
        return if (info == null) rd.setErrorMsg("用户不存在")
        else rd.setData(formatUserPublic(ns, info))
    }

    suspend fun saveUserConfig(context: RoutingContext): ReturnData {
        val rd = ReturnData()
        if (!checkAuth(context)) return rd.setData("NEED_LOGIN").setErrorMsg("请登录后使用")
        val ns = getUserNameSpace(context)
        val cfg = context.bodyAsJson ?: return rd.setErrorMsg("参数错误")
        saveUserStorage(ns, "userConfig", cfg)
        return rd.setData(true)
    }

    suspend fun getUserConfig(context: RoutingContext): ReturnData {
        val rd = ReturnData()
        if (!checkAuth(context)) return rd.setData("NEED_LOGIN").setErrorMsg("请登录后使用")
        val ns = getUserNameSpace(context)
        val raw = getUserStorage(ns, "userConfig")
        return rd.setData(ExtKt.asJsonObject(raw) ?: mapOf<String, Any>())
    }

    private fun mapToUser(username: String, m: Map<String, Any?>): User {
        // Minimal mapping; entity fields depend on User class
        return User(
            username = username,
            password = m["password"] as? String ?: "",
            salt = m["salt"] as? String ?: "",
            token = m["token"] as? String,
            isManager = m["isManager"] as? Boolean ?: false
        )
    }

    private fun formatUserPublic(username: String, m: Map<String, Any?>): Map<String, Any?> {
        return mapOf(
            "username" to username,
            "enableWebdav" to m["enableWebdav"],
            "enableLocalStore" to m["enableLocalStore"],
            "enableBookSource" to m["enableBookSource"],
            "enableRssSource" to m["enableRssSource"],
            "bookSourceLimit" to m["bookSourceLimit"],
            "bookLimit" to m["bookLimit"],
            "last_login_at" to m["last_login_at"],
            "created_at" to m["created_at"],
        )
    }
}
''',
)

print("core foundation done")
print("biz root", BIZ)
