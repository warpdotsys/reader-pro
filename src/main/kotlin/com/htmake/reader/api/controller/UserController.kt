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
        val u = users[username] ?: return rd.setErrorMsg("用户不存在")
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
        if (!checkManagerAuth(ctx)) return rd.setErrorMsg("需要管理密码")
        val body = ctx.bodyAsJson ?: return rd.setErrorMsg("参数错误")
        val username = body.getString("username") ?: return rd.setErrorMsg("用户名不能为空")
        val password = body.getString("password") ?: return rd.setErrorMsg("密码不能为空")
        val minLen = appConfig.minUserPasswordLength.takeIf { it > 0 } ?: 6
        if (password.length < minLen) return rd.setErrorMsg("密码不能低于${minLen}位")
        val users = loadUserMap()
        if (users.containsKey(username)) return rd.setErrorMsg("用户已存在")
        val salt = ExtKt.getRandomString(8)
        val enc = ExtKt.genEncryptedPassword(password, salt)
        users[username] = mutableMapOf(
            "password" to enc,
            "salt" to salt,
            "created_at" to System.currentTimeMillis(),
            "enableWebdav" to true,
            "enableBookSource" to true,
            "enableRssSource" to true
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
        return rd.setData(true)
    }

    suspend fun clearInactiveUsers(ctx: RoutingContext): ReturnData = ReturnData().setData(0)
}
