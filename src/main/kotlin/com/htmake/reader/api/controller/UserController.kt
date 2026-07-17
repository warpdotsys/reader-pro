package com.htmake.reader.api.controller

import com.htmake.reader.api.ReturnData
import com.htmake.reader.config.UserConfigDefaults
import com.htmake.reader.utils.ExtKt
import io.vertx.core.json.JsonObject
import io.vertx.ext.web.RoutingContext
import java.util.UUID
import kotlin.coroutines.CoroutineContext

class UserController(cc: CoroutineContext) : BaseController(cc) {
    suspend fun login(ctx: RoutingContext): ReturnData {
        val rd = ReturnData()
        val body = ctx.bodyAsJson ?: return rd.setErrorMsg("参数错误")
        val username = body.getString("username") ?: return rd.setErrorMsg("用户名不能为空")
        val password = body.getString("password") ?: return rd.setErrorMsg("密码不能为空")
        val isLogin = body.getBoolean("isLogin", false)
        if (!appConfig.secure) {
            ctx.session()?.put("username", username)
            return rd.setData(
                mapOf(
                    "username" to username,
                    "token" to "dev",
                    "accessToken" to "$username:dev"
                )
            )
        }
        val users = loadUserMap()
        var u = users[username]
        if (u == null) {
            if (isLogin) return rd.setErrorMsg("用户不存在")
            // isLogin=false: 注册分支（原版 login 接口行为，邀请码字段为 code）
            if (username.length < 5) return rd.setErrorMsg("用户名不能低于5位")
            val minLen = appConfig.minUserPasswordLength.takeIf { it > 0 } ?: 6
            if (password.length < minLen) return rd.setErrorMsg("密码不能低于${minLen}位")
            if (username == "default") return rd.setErrorMsg("用户名不能为非法字符")
            if (!Regex("[a-z0-9]+", RegexOption.IGNORE_CASE).matches(username)) {
                return rd.setErrorMsg("用户名只能由字母和数字组成")
            }
            if (appConfig.inviteCode.isNotBlank()) {
                val code = body.getString("code") ?: ""
                if (code.isEmpty()) return rd.setErrorMsg("请输入邀请码")
                if (code != appConfig.inviteCode) return rd.setErrorMsg("邀请码错误")
            }
            if (users.size >= appConfig.userLimit) return rd.setErrorMsg("超过用户数上限")
            val salt = ExtKt.getRandomString(8)
            u = mutableMapOf(
                "password" to ExtKt.genEncryptedPassword(password, salt),
                "salt" to salt,
                "created_at" to System.currentTimeMillis(),
                "enableWebdav" to appConfig.defaultUserEnableWebdav,
                "enableLocalStore" to appConfig.defaultUserEnableLocalStore,
                "enableBookSource" to appConfig.defaultUserEnableBookSource,
                "enableRssSource" to appConfig.defaultUserEnableRssSource,
                "bookSourceLimit" to appConfig.defaultUserBookSourceLimit,
                "bookLimit" to (appConfig.defaultUserBookLimit.takeIf { it > 0 } ?: appConfig.userBookLimit)
            )
            users[username] = u
        } else if (!isLogin) {
            return rd.setErrorMsg("用户名已被占用")
        }
        val stored = u["password"]?.toString() ?: ""
        val salt = u["salt"]?.toString()
        if (!ExtKt.verifyPassword(password, stored, salt)) return rd.setErrorMsg("密码错误")
        val token = UUID.randomUUID().toString().replace("-", "")
        @Suppress("UNCHECKED_CAST")
        val tokenMap = (u["token_map"] as? MutableMap<String, Any?>)
            ?: (u["token_map"] as? Map<*, *>)?.mapKeys { it.key.toString() }?.mapValues { it.value }?.toMutableMap()
            ?: mutableMapOf()
        tokenMap[token] = System.currentTimeMillis()
        u["token_map"] = tokenMap
        u["token"] = token
        u["last_login_at"] = System.currentTimeMillis()
        users[username] = u
        saveUserMap(users)
        ctx.session()?.put("username", username)
        return rd.setData(mapOf("username" to username, "token" to token, "accessToken" to "$username:$token"))
    }

    suspend fun logout(ctx: RoutingContext): ReturnData {
        ctx.session()?.destroy()
        return ReturnData().setData(true)
    }

    suspend fun getUserInfo(ctx: RoutingContext): ReturnData {
        val rd = ReturnData()
        if (!checkAuth(ctx)) return rd.setData("NEED_LOGIN").setErrorMsg("请登录后使用")
        val ns = getUserNameSpace(ctx)
        return rd.setData(mapOf("username" to ns))
    }

    suspend fun saveUserConfig(ctx: RoutingContext): ReturnData {
        val rd = ReturnData()
        if (!checkAuth(ctx)) return rd.setData("NEED_LOGIN").setErrorMsg("请登录后使用")
        val body = ctx.bodyAsJson ?: return rd.setErrorMsg("参数错误")
        val merge = body.getBoolean("merge", false) == true
        body.remove("merge")
        body.put("@updateTime", System.currentTimeMillis())
        val ns = getUserNameSpace(ctx)
        val toSave = if (merge) UserConfigDefaults.merge(ExtKt.asJsonObject(getUserStorage(ns, "userConfig")), body) else body
        saveUserStorage(ns, "userConfig", toSave)
        return rd.setData("")
    }

    suspend fun getUserConfig(ctx: RoutingContext): ReturnData {
        val rd = ReturnData()
        if (!checkAuth(ctx)) return rd.setData("NEED_LOGIN").setErrorMsg("请登录后使用")
        val raw = getUserStorage(getUserNameSpace(ctx), "userConfig")
        val obj = ExtKt.asJsonObject(raw)
        if (obj == null) return rd.setErrorMsg("没有备份文件").setData(UserConfigDefaults.base().map)
        return rd.setData(obj.map)
    }

    suspend fun getUserList(ctx: RoutingContext): ReturnData {
        val rd = ReturnData()
        if (!checkManagerAuth(ctx)) return rd.setErrorMsg("需要管理密码")
        return rd.setData(loadUserMap().keys.toList())
    }

    suspend fun addUser(ctx: RoutingContext): ReturnData {
        val rd = ReturnData()
        val body = ctx.bodyAsJson ?: return rd.setErrorMsg("参数错误")
        val username = body.getString("username") ?: return rd.setErrorMsg("用户名不能为空")
        val password = body.getString("password") ?: return rd.setErrorMsg("密码不能为空")
        // Public self-register when secure=true: need inviteCode if configured;
        // admin can always create with secureKey (checkManagerAuth).
        val asAdmin = checkManagerAuth(ctx)
        if (!asAdmin) {
            if (!appConfig.secure) {
                // open mode: allow create without manager key
            } else {
                val invite = body.getString("inviteCode")
                    ?: ctx.queryParam("inviteCode").firstOrNull()
                    ?: ""
                if (appConfig.inviteCode.isNotBlank() && invite != appConfig.inviteCode) {
                    return rd.setErrorMsg("邀请码错误")
                }
            }
        }
        val minLen = appConfig.minUserPasswordLength.takeIf { it > 0 } ?: 6
        if (password.length < minLen) return rd.setErrorMsg("密码不能低于${minLen}位")
        val users = loadUserMap()
        if (users.size >= appConfig.userLimit && !users.containsKey(username)) {
            return rd.setErrorMsg("用户数已达上限(${appConfig.userLimit})")
        }
        if (users.containsKey(username)) return rd.setErrorMsg("用户已存在")
        val salt = ExtKt.getRandomString(8)
        val enc = ExtKt.genEncryptedPassword(password, salt)
        users[username] = mutableMapOf(
            "password" to enc,
            "salt" to salt,
            "created_at" to System.currentTimeMillis(),
            "enableWebdav" to appConfig.defaultUserEnableWebdav,
            "enableLocalStore" to appConfig.defaultUserEnableLocalStore,
            "enableBookSource" to appConfig.defaultUserEnableBookSource,
            "enableRssSource" to appConfig.defaultUserEnableRssSource,
            "bookSourceLimit" to appConfig.defaultUserBookSourceLimit,
            "bookLimit" to (appConfig.defaultUserBookLimit.takeIf { it > 0 } ?: appConfig.userBookLimit)
        )
        saveUserMap(users)
        return rd.setData(true)
    }

    suspend fun deleteUsers(ctx: RoutingContext): ReturnData {
        val rd = ReturnData()
        if (!checkManagerAuth(ctx)) return rd.setErrorMsg("需要管理密码")
        val names = ctx.bodyAsJson?.getJsonArray("users") ?: return rd.setErrorMsg("参数错误")
        val users = loadUserMap()
        names.forEach { users.remove(it.toString()) }
        saveUserMap(users)
        return rd.setData(true)
    }

    suspend fun resetPassword(ctx: RoutingContext): ReturnData {
        val rd = ReturnData()
        if (!checkManagerAuth(ctx)) return rd.setErrorMsg("需要管理密码")
        val body = ctx.bodyAsJson ?: return rd.setErrorMsg("参数错误")
        val username = body.getString("username") ?: return rd.setErrorMsg("用户名不能为空")
        val password = body.getString("password") ?: return rd.setErrorMsg("密码不能为空")
        val minLen = appConfig.minUserPasswordLength.takeIf { it > 0 } ?: 6
        if (password.length < minLen) return rd.setErrorMsg("密码不能低于${minLen}位")
        val users = loadUserMap()
        val u = users[username] ?: return rd.setErrorMsg("用户不存在")
        val salt = ExtKt.getRandomString(8)
        u["salt"] = salt
        u["password"] = ExtKt.genEncryptedPassword(password, salt)
        u["token_map"] = mutableMapOf<String, Any?>()
        u["token"] = ""
        saveUserMap(users)
        return rd.setData(true)
    }

    suspend fun updateUser(ctx: RoutingContext): ReturnData {
        val rd = ReturnData()
        if (!checkManagerAuth(ctx)) return rd.setErrorMsg("需要管理密码")
        val body = ctx.bodyAsJson ?: return rd.setErrorMsg("参数错误")
        val username = body.getString("username") ?: return rd.setErrorMsg("用户名不能为空")
        val users = loadUserMap()
        val u = users[username] ?: return rd.setErrorMsg("用户不存在")
        listOf("enableWebdav", "enableBookSource", "enableRssSource").forEach { k ->
            if (body.containsKey(k)) u[k] = body.getBoolean(k)
        }
        body.getString("email")?.let { u["email"] = it }
        users[username] = u
        saveUserMap(users)
        return rd.setData(true)
    }

    /**
     * Remove users whose last_login_at is older than `day` days (default from body/query or 90).
     * Does not delete user data dirs unless `purgeData=true`.
     */
    suspend fun clearInactiveUsers(ctx: RoutingContext): ReturnData {
        val rd = ReturnData()
        if (!checkManagerAuth(ctx)) return rd.setErrorMsg("需要管理密码")
        val day = ctx.bodyAsJson?.getInteger("day")
            ?: ctx.queryParam("day").firstOrNull()?.toIntOrNull()
            ?: appConfig.autoClearInactiveUser.takeIf { it > 0 }
            ?: 90
        val purge = ctx.bodyAsJson?.getBoolean("purgeData") == true
        val cutoff = System.currentTimeMillis() - day.toLong() * 24 * 3600 * 1000
        val users = loadUserMap()
        val removed = mutableListOf<String>()
        users.entries.removeAll { (name, info) ->
            val last = (info["last_login_at"] as? Number)?.toLong()
                ?: (info["created_at"] as? Number)?.toLong()
                ?: 0L
            // only drop when last_login/created is known and older than cutoff
            val drop = last > 0 && last < cutoff
            if (drop) {
                removed += name
                if (purge) {
                    val dir = java.io.File(ExtKt.getWorkDir("storage", "data", name))
                    ExtKt.deleteRecursively(dir)
                }
            }
            drop
        }
        if (removed.isNotEmpty()) saveUserMap(users)
        return rd.setData(mapOf("removed" to removed.size, "users" to removed, "day" to day))
    }
}
