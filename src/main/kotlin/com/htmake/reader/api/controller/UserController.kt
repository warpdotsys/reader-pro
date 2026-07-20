package com.htmake.reader.api.controller

import com.htmake.reader.api.ReturnData
import com.htmake.reader.entity.User
import com.htmake.reader.utils.asJsonObject
import com.htmake.reader.utils.genEncryptedPassword
import com.htmake.reader.utils.getInstalledLicense
import com.htmake.reader.utils.getRandomString
import com.htmake.reader.utils.getStorage
import com.htmake.reader.utils.gson
import com.htmake.reader.utils.saveStorage
import com.htmake.reader.utils.success
import com.htmake.reader.utils.toMap
import io.vertx.core.json.Json
import io.vertx.core.json.JsonObject
import io.vertx.ext.web.RoutingContext
import java.io.File
import kotlinx.coroutines.coroutineScope
import mu.KotlinLogging
import kotlin.coroutines.CoroutineContext

private val logger = KotlinLogging.logger {}

class UserController(
    coroutineContext: CoroutineContext,
) : BaseController(coroutineContext) {
    val userMaxCount = 15

    private fun getUserLimit(context: RoutingContext): Int {
        val license = getInstalledLicense(ignoreInvalid = true)
        return if (license.validHost(context.request().host())) {
            appConfig.userLimit.coerceAtLeast(1).coerceAtMost(license.userMaxLimit)
        } else {
            appConfig.userLimit.coerceAtLeast(1).coerceAtMost(userMaxCount)
        }
    }

    suspend fun login(context: RoutingContext): ReturnData {
        val result = ReturnData()
        val body = context.bodyAsJson ?: return result.setErrorMsg("参数错误")
        val username = body.getString("username", "").orEmpty()
        val password = body.getString("password", "").orEmpty()
        val isLogin = body.getBoolean("isLogin", false) ?: false
        if (username.isEmpty()) return result.setErrorMsg("请输入用户名")
        if (password.isEmpty()) return result.setErrorMsg("请输入密码")

        val users = loadUsers()
        var user = users[username]
        if (user == null) {
            if (isLogin) return result.setErrorMsg("用户不存在")
            if (users.size >= getUserLimit(context)) return result.setErrorMsg("超过用户数上限")
            if (username == "default" || username.any { !it.isLetterOrDigit() }) return result.setErrorMsg("用户名不合法")
            if (password.length < appConfig.minUserPasswordLength) return result.setErrorMsg("密码长度不足")
            if (appConfig.inviteCode.isNotEmpty() && body.getString("code", "") != appConfig.inviteCode) {
                return result.setErrorMsg("邀请码错误")
            }
            user = User(
                username = username,
                salt = getRandomString(8),
                enable_webdav = appConfig.defaultUserEnableWebdav,
                enable_local_store = appConfig.defaultUserEnableLocalStore,
                enable_book_source = appConfig.defaultUserEnableBookSource,
                enable_rss_source = appConfig.defaultUserEnableRssSource,
                book_source_limit = appConfig.defaultUserBookSourceLimit,
                book_limit = appConfig.defaultUserBookLimit,
            )
            user.password = genEncryptedPassword(password, user.salt)
            users[username] = user
        } else if (!isLogin) {
            return result.setErrorMsg("用户名已被占用")
        } else if (user.password != genEncryptedPassword(password, user.salt)) {
            return result.setErrorMsg("密码错误")
        }

        saveUsers(users)
        return result.setData(saveUserSession(context, user, regenerateToken = true))
    }

    suspend fun logout(context: RoutingContext): ReturnData {
        context.session().destroy()
        return ReturnData().setData("")
    }

    suspend fun getUserList(context: RoutingContext): ReturnData {
        if (!checkManagerAuth(context)) return ReturnData().setErrorMsg("需要管理密码")
        return ReturnData().setData(loadUsers().values.map(::formatUser))
    }

    suspend fun addUser(context: RoutingContext): ReturnData {
        val result = ReturnData()
        if (!checkManagerAuth(context)) return result.setErrorMsg("需要管理密码")
        val body = context.bodyAsJson ?: return result.setErrorMsg("参数错误")
        val username = body.getString("username", "").orEmpty()
        val password = body.getString("password", "").orEmpty()
        if (username.isEmpty() || password.isEmpty()) return result.setErrorMsg("参数错误")
        val users = loadUsers()
        if (users.containsKey(username)) return result.setErrorMsg("用户已存在")
        if (users.size >= getUserLimit(context)) return result.setErrorMsg("超过用户数上限")
        val user = User(username = username, salt = getRandomString(8))
        user.password = genEncryptedPassword(password, user.salt)
        users[username] = user
        saveUsers(users)
        return result.setData("")
    }

    suspend fun resetPassword(context: RoutingContext): ReturnData {
        val result = ReturnData()
        if (!checkManagerAuth(context)) return result.setErrorMsg("需要管理密码")
        val body = context.bodyAsJson ?: return result.setErrorMsg("参数错误")
        val user = loadUsers()[body.getString("username", "")] ?: return result.setErrorMsg("用户不存在")
        val password = body.getString("password", "").orEmpty()
        if (password.length < appConfig.minUserPasswordLength) return result.setErrorMsg("密码长度不足")
        user.salt = getRandomString(8)
        user.password = genEncryptedPassword(password, user.salt)
        user.token = ""
        user.token_map = emptyMap()
        saveUsers(loadUsers().also { it[user.username] = user })
        return result.setData("")
    }

    suspend fun deleteUsers(context: RoutingContext): ReturnData {
        val result = ReturnData()
        if (!checkManagerAuth(context)) return result.setErrorMsg("需要管理密码")
        val names = context.bodyAsJson?.getJsonArray("users") ?: return result.setErrorMsg("参数错误")
        val users = loadUsers()
        names.forEach { users.remove(it.toString()) }
        saveUsers(users)
        return result.setData("")
    }

    suspend fun clearInactiveUsers(context: RoutingContext): ReturnData {
        if (!checkManagerAuth(context)) return ReturnData().setErrorMsg("需要管理密码")
        val days = context.bodyAsJson?.getInteger("days") ?: appConfig.autoClearInactiveUser
        clearInactiveUsers(days)
        return ReturnData().setData("")
    }

    suspend fun clearInactiveUsers(days: Int) {
        if (days <= 0) return
        val threshold = System.currentTimeMillis() - days * 86_400_000L
        val users = loadUsers()
        users.entries.removeIf { (_, user) -> user.last_login_at < threshold }
        saveUsers(users)
    }

    suspend fun updateUser(context: RoutingContext): ReturnData {
        val result = ReturnData()
        if (!checkManagerAuth(context)) return result.setErrorMsg("需要管理密码")
        val body = context.bodyAsJson ?: return result.setErrorMsg("参数错误")
        val users = loadUsers()
        val user = users[body.getString("username", "")] ?: return result.setErrorMsg("用户不存在")
        body.getBoolean("enableWebdav")?.let { user.enable_webdav = it }
        body.getBoolean("enableLocalStore")?.let { user.enable_local_store = it }
        body.getBoolean("enableBookSource")?.let { user.enable_book_source = it }
        body.getBoolean("enableRssSource")?.let { user.enable_rss_source = it }
        body.getInteger("bookSourceLimit")?.let { user.book_source_limit = it }
        body.getInteger("bookLimit")?.let { user.book_limit = it }
        saveUsers(users)
        return result.setData("")
    }

    suspend fun getUserInfo(context: RoutingContext): ReturnData {
        val result = ReturnData()
        if (!checkAuth(context)) return result.setData("NEED_LOGIN").setErrorMsg("请登录后使用")
        return result.setData(context.get<User>("userInfo")?.let(::formatUser) ?: emptyMap<String, Any>())
    }

    suspend fun saveUserConfig(context: RoutingContext): ReturnData {
        val result = ReturnData()
        if (!checkAuth(context)) return result.setData("NEED_LOGIN").setErrorMsg("请登录后使用")
        saveUserStorage(context, "userConfig", context.bodyAsJson ?: JsonObject())
        return result.setData("")
    }

    suspend fun getUserConfig(context: RoutingContext): ReturnData {
        val result = ReturnData()
        if (!checkAuth(context)) return result.setData("NEED_LOGIN").setErrorMsg("请登录后使用")
        return result.setData(getUserStorage(context, "userConfig").asJsonObject() ?: JsonObject())
    }

    suspend fun uploadFile(context: RoutingContext): ReturnData = ReturnData().setData(context.fileUploads().map { it.fileName() })

    suspend fun deleteFile(context: RoutingContext): ReturnData {
        val file = context.bodyAsJson?.getString("path")?.let(::File)
        if (file?.exists() == true) file.delete()
        return ReturnData().setData("")
    }

    suspend fun downloadBackupFile(context: RoutingContext) {
        context.success(ReturnData().setErrorMsg("备份文件不存在"))
    }

    suspend fun forEachUser(
        handler: suspend kotlinx.coroutines.CoroutineScope.(User) -> Boolean,
    ) = coroutineScope {
        val users = getStorage("data", "users").asJsonObject()?.map.orEmpty().mapNotNull { (name, value) ->
            gson.fromJson(gson.toJson(value), User::class.java)?.let { name to it }
        }
        for ((_, user) in users) {
            if (!handler(user)) break
        }
        logger.debug { "iterated user records" }
    }

    private fun loadUsers(): MutableMap<String, User> =
        getStorage("data", "users").asJsonObject()?.map.orEmpty().mapNotNull { (name, value) ->
            gson.fromJson(gson.toJson(value), User::class.java)?.let { name to it }
        }.toMap(linkedMapOf()).toMutableMap()

    private fun saveUsers(users: Map<String, User>) {
        saveStorage("data", "users", value = Json.encode(users.mapValues { it.value.toMap() }))
    }
}
