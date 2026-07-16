/**
 * Business-oriented rewrite from reader-pro-3.2.14.jar reverse engineering.
 * Source: CFR/Vineflower decompilation + manual semantic cleanup.
 * Purpose: readability / audit — not drop-in recompilation of the original APK/JAR.
 */
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

    /**
     * 保存阅读配置。jar：body JSON + `@updateTime` 时间戳。
     * 支持 merge=true 与已有配置合并（默认覆盖整份）。
     */
    suspend fun saveUserConfig(context: RoutingContext): ReturnData {
        val rd = ReturnData()
        if (!checkAuth(context)) return rd.setData("NEED_LOGIN").setErrorMsg("请登录后使用")
        val ns = getUserNameSpace(context)
        val body = context.bodyAsJson ?: return rd.setErrorMsg("参数错误")
        val merge = body.getBoolean("merge", false) == true
        body.remove("merge")
        body.put("@updateTime", System.currentTimeMillis())
        val toSave = if (merge) {
            val prev = ExtKt.asJsonObject(getUserStorage(ns, "userConfig"))
            com.htmake.reader.config.UserConfigDefaults.merge(prev, body)
        } else {
            body
        }
        saveUserStorage(ns, "userConfig", toSave)
        return rd.setData("")
    }

    /** 无备份时 jar 返回「没有备份文件」；同时 data 带 defaults 方便前端。 */
    suspend fun getUserConfig(context: RoutingContext): ReturnData {
        val rd = ReturnData()
        if (!checkAuth(context)) return rd.setData("NEED_LOGIN").setErrorMsg("请登录后使用")
        val ns = getUserNameSpace(context)
        val raw = getUserStorage(ns, "userConfig")
        val obj = ExtKt.asJsonObject(raw)
        if (obj == null) {
            return rd.setErrorMsg("没有备份文件").setData(
                com.htmake.reader.config.UserConfigDefaults.base().map
            )
        }
        return rd.setData(obj.map)
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
