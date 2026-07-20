package com.htmake.reader.api.controller

import com.htmake.reader.api.ReturnData
import com.htmake.reader.entity.User
import com.htmake.reader.utils.SpringContextUtils
import com.htmake.reader.utils.asJsonArray
import com.htmake.reader.utils.asJsonObject
import com.htmake.reader.utils.deleteRecursively
import com.htmake.reader.utils.getStorageFile
import com.htmake.reader.utils.getWorkDir
import com.htmake.reader.utils.parseJsonStringList
import com.htmake.reader.utils.success
import io.legado.app.data.entities.BookSource
import io.vertx.core.http.HttpMethod
import io.vertx.core.json.JsonArray
import io.vertx.core.json.JsonObject
import io.vertx.ext.web.RoutingContext
import io.vertx.ext.web.client.WebClient
import java.io.File
import mu.KotlinLogging
import kotlin.coroutines.CoroutineContext

private val logger = KotlinLogging.logger {}

class BookSourceController(
    coroutineContext: CoroutineContext,
) : BaseController(coroutineContext) {
    private var webClient: WebClient = requireNotNull(
        SpringContextUtils.getBean("webClient", WebClient::class.java),
    )

    fun getUserBookSourceJsonOpt(
        userNameSpace: String,
        fields: Set<String>? = null,
        checkNotEmpty: Set<String>? = null,
    ): JsonArray? {
        var file = getStorageFile("data", userNameSpace, "bookSource")
        if (!file.exists()) {
            file = getStorageFile("data", "default", "bookSource")
        }
        return parseJsonStringList(file, fields = fields, checkNotEmpty = checkNotEmpty)
    }

    fun getUserBookSourceJson(userNameSpace: String): JsonArray? {
        var sources = getUserStorage(userNameSpace, "bookSource").asJsonArray()
        if (sources == null && userNameSpace != "default") {
            sources = getUserStorage("default", "bookSource").asJsonArray()
        }
        return sources
    }

    suspend fun canEditBookSource(context: RoutingContext): Boolean =
        !appConfig.secure || context.get<User>("userInfo")?.enable_book_source == true

    suspend fun saveBookSource(context: RoutingContext): ReturnData {
        val result = ReturnData()
        if (!checkAuth(context)) {
            return result.setData("NEED_LOGIN").setErrorMsg("请登录后使用")
        }
        if (!canEditBookSource(context)) {
            return result.setErrorMsg("权限不足")
        }

        val source = BookSource.fromJson(context.bodyAsString).getOrNull()
            ?: return result.setErrorMsg("参数错误")
        if (source.bookSourceUrl.isEmpty()) {
            return result.setErrorMsg("书源链接不能为空")
        }

        val userNameSpace = getUserNameSpace(context)
        val sources = getUserBookSourceJson(userNameSpace) ?: JsonArray()
        val index = sourceIndex(sources, source.bookSourceUrl)
        if (index >= 0) {
            sources.list[index] = JsonObject.mapFrom(source)
        } else {
            sources.add(JsonObject.mapFrom(source))
        }
        saveUserStorage(userNameSpace, "bookSource", sources.list)
        generateBookSourceMap(userNameSpace, sources)
        return result.setData("")
    }

    suspend fun saveBookSources(context: RoutingContext): ReturnData {
        val result = ReturnData()
        if (!checkAuth(context)) {
            return result.setData("NEED_LOGIN").setErrorMsg("请登录后使用")
        }
        if (!canEditBookSource(context)) {
            return result.setErrorMsg("权限不足")
        }
        val sources = context.bodyAsJsonArray ?: return result.setErrorMsg("参数错误")
        return saveBookSources(context, sources)
    }

    fun saveBookSources(context: RoutingContext, bookSourceJsonArray: JsonArray): ReturnData {
        val userNameSpace = getUserNameSpace(context)
        val user = context.get<User>("userInfo") ?: User(username = userNameSpace)
        return saveUserBookSources(userNameSpace, user, bookSourceJsonArray)
    }

    fun saveUserBookSources(
        userNameSpace: String,
        userInfo: User,
        bookSourceJsonArray: JsonArray,
    ): ReturnData {
        val result = ReturnData()
        val sources = JsonArray()
        val seen = linkedSetOf<String>()
        for (index in 0 until bookSourceJsonArray.size()) {
            val source = BookSource.fromJson(bookSourceJsonArray.getJsonObject(index).toString()).getOrNull()
                ?: continue
            if (source.bookSourceUrl.isEmpty() || !seen.add(source.bookSourceUrl)) {
                continue
            }
            if (userInfo.book_source_limit > 0 && sources.size() >= userInfo.book_source_limit) {
                break
            }
            sources.add(JsonObject.mapFrom(source))
        }
        saveUserStorage(userNameSpace, "bookSource", sources.list)
        generateBookSourceMap(userNameSpace, sources)
        return result.setData("")
    }

    suspend fun getBookSource(context: RoutingContext): ReturnData {
        val result = ReturnData()
        if (!checkAuth(context)) {
            return result.setData("NEED_LOGIN").setErrorMsg("请登录后使用")
        }
        val url = when (context.request().method()) {
            HttpMethod.POST -> context.bodyAsJson?.getString("bookSourceUrl") ?: ""
            else -> context.queryParam("bookSourceUrl").firstOrNull() ?: ""
        }
        if (url.isEmpty()) {
            return result.setErrorMsg("书源链接不能为空")
        }
        val source = getUserBookSourceJson(getUserNameSpace(context))
            ?.let { sources -> sourceIndex(sources, url).takeIf { it >= 0 }?.let(sources::getJsonObject) }
            ?: return result.setErrorMsg("书源不存在")
        return result.setData(source)
    }

    suspend fun getBookSources(context: RoutingContext): ReturnData {
        val result = ReturnData()
        if (!checkAuth(context)) {
            return result.setData("NEED_LOGIN").setErrorMsg("请登录后使用")
        }
        return result.setData(getUserBookSourceJson(getUserNameSpace(context))?.list ?: emptyList<Any>())
    }

    suspend fun deleteBookSource(context: RoutingContext): ReturnData {
        val result = ReturnData()
        if (!checkAuth(context)) {
            return result.setData("NEED_LOGIN").setErrorMsg("请登录后使用")
        }
        if (!canEditBookSource(context)) {
            return result.setErrorMsg("权限不足")
        }
        val url = context.bodyAsJson?.getString("bookSourceUrl") ?: return result.setErrorMsg("参数错误")
        val userNameSpace = getUserNameSpace(context)
        val sources = getUserStorage(userNameSpace, "bookSource").asJsonArray() ?: JsonArray()
        sourceIndex(sources, url).takeIf { it >= 0 }?.let(sources::remove)
        saveUserStorage(userNameSpace, "bookSource", sources.list)
        generateBookSourceMap(userNameSpace, sources)
        return result.setData("")
    }

    suspend fun deleteBookSources(context: RoutingContext): ReturnData {
        val result = ReturnData()
        if (!checkAuth(context)) {
            return result.setData("NEED_LOGIN").setErrorMsg("请登录后使用")
        }
        if (!canEditBookSource(context)) {
            return result.setErrorMsg("权限不足")
        }
        val values = context.bodyAsJsonArray ?: return result.setErrorMsg("参数错误")
        val urls = values.mapNotNull { item -> item.asJsonObject()?.getString("bookSourceUrl") }.toSet()
        val userNameSpace = getUserNameSpace(context)
        val sources = getUserStorage(userNameSpace, "bookSource").asJsonArray() ?: JsonArray()
        val kept = sources.filter { item -> item.asJsonObject()?.getString("bookSourceUrl") !in urls }
        val updated = JsonArray(kept)
        saveUserStorage(userNameSpace, "bookSource", updated.list)
        generateBookSourceMap(userNameSpace, updated)
        return result.setData("")
    }

    suspend fun deleteAllBookSources(context: RoutingContext): ReturnData {
        val result = ReturnData()
        if (!checkAuth(context)) {
            return result.setData("NEED_LOGIN").setErrorMsg("请登录后使用")
        }
        if (!canEditBookSource(context)) {
            return result.setErrorMsg("权限不足")
        }
        val userNameSpace = getUserNameSpace(context)
        saveUserStorage(userNameSpace, "bookSource", emptyList<Any>())
        generateBookSourceMap(userNameSpace, JsonArray())
        return result.setData("")
    }

    suspend fun setAsDefaultBookSources(context: RoutingContext): ReturnData {
        val result = ReturnData()
        if (!checkAuth(context)) {
            return result.setData("NEED_LOGIN").setErrorMsg("请登录后使用")
        }
        if (!checkManagerAuth(context)) {
            return result.setData("NEED_SECURE_KEY").setErrorMsg("需要管理密码")
        }
        val username = context.bodyAsJson?.getString("username") ?: return result.setErrorMsg("参数错误")
        val sources = getUserStorage(username, "bookSource").asJsonArray()
            ?: return result.setErrorMsg("用户书源不存在")
        saveUserStorage("default", "bookSource", sources.list)
        generateBookSourceMap("default", sources)
        return result.setData("设置默认书源成功")
    }

    suspend fun readSourceFile(context: RoutingContext): ReturnData {
        val result = ReturnData()
        val uploads = context.fileUploads()
        if (uploads.isNullOrEmpty()) {
            return result.setErrorMsg("请上传文件")
        }
        val sources = JsonArray()
        uploads.forEach { upload ->
            val file = File(upload.uploadedFileName())
            if (file.exists()) {
                sources.add(file.readText())
                file.delete()
            }
        }
        return result.setData(sources.list)
    }

    suspend fun saveFromRemoteSource(context: RoutingContext) {
        val result = ReturnData()
        if (!checkAuth(context)) {
            context.success(result.setData("NEED_LOGIN").setErrorMsg("请登录后使用"))
            return
        }
        val url = if (context.request().method() == HttpMethod.POST) {
            context.bodyAsJson?.getString("url").orEmpty()
        } else {
            context.queryParam("url").firstOrNull().orEmpty()
        }
        if (url.isEmpty()) {
            context.success(result.setErrorMsg("请输入远程书源链接"))
            return
        }
        webClient.getAbs(url).send { response ->
            logger.debug { "remote book source subscription completed with $webClient" }
            if (response.failed()) {
                context.success(result.setErrorMsg(response.cause()?.message ?: "远程书源链接错误"))
                return@send
            }
            val content = response.result().bodyAsString()
            val sources = content.asJsonArray()
                ?: JsonObject(content).getJsonArray("data")
                ?: JsonArray().add(content)
            context.success(saveBookSources(context, sources))
        }
    }

    suspend fun updateRemoteSourceSub(userNameSpace: String, user: User) {
        if (user.book_source_limit == 0) {
            return
        }
        generateBookSourceMap(userNameSpace)
    }

    suspend fun deleteUserBookSource(context: RoutingContext): ReturnData {
        val result = ReturnData()
        if (!checkAuth(context)) {
            return result.setData("NEED_LOGIN").setErrorMsg("请登录后使用")
        }
        if (!checkManagerAuth(context)) {
            return result.setData("NEED_SECURE_KEY").setErrorMsg("需要管理密码")
        }
        val username = context.bodyAsJson?.getString("username") ?: return result.setErrorMsg("参数错误")
        getStorageFile("data", username, "bookSource").delete()
        File(getWorkDir("storage", "data", username, "bookSource.json")).deleteRecursively()
        return result.setData("删除书源成功")
    }

    suspend fun deleteBookSourcesFile(context: RoutingContext): ReturnData {
        val result = ReturnData()
        if (!checkAuth(context)) {
            return result.setData("NEED_LOGIN").setErrorMsg("请登录后使用")
        }
        File(getWorkDir("storage", "data", getUserNameSpace(context), "bookSource.json")).deleteRecursively()
        return result.setData("")
    }

    fun generateBookSourceMap(
        userNameSpace: String,
        bookSourceJsonArray: JsonArray? = null,
    ): Map<String, Int> {
        val sources = bookSourceJsonArray ?: getUserBookSourceJson(userNameSpace) ?: JsonArray()
        val index = linkedMapOf<String, Int>()
        val explore = arrayListOf<Map<String, String?>>()
        for (position in 0 until sources.size()) {
            val source = sources.getJsonObject(position)
            source.getString("bookSourceUrl")?.let { index[it] = position }
            if (!source.getString("exploreUrl").isNullOrEmpty()) {
                explore += mapOf(
                    "bookSourceUrl" to source.getString("bookSourceUrl"),
                    "bookSourceGroup" to source.getString("bookSourceGroup"),
                    "bookSourceName" to source.getString("bookSourceName"),
                )
            }
        }
        saveUserStorage(userNameSpace, "bookSourceMap", index)
        saveUserStorage(userNameSpace, "bookSourceExploreList", explore)
        return index
    }

    @Suppress("UNCHECKED_CAST")
    fun getBookSourceMap(userNameSpace: String): Map<String, Int> {
        val sourceFile = getStorageFile("data", userNameSpace, "bookSource")
        val value = if (sourceFile.exists()) {
            getUserStorage(userNameSpace, "bookSourceMap")
        } else {
            getUserStorage("default", "bookSourceMap")
        }
        return value.asJsonObject()?.map?.mapValues { (_, index) -> (index as Number).toInt() }
            ?: emptyMap()
    }

    private fun sourceIndex(sources: JsonArray, url: String): Int {
        for (index in 0 until sources.size()) {
            if (sources.getJsonObject(index).getString("bookSourceUrl") == url) {
                return index
            }
        }
        return -1
    }
}
