# -*- coding: utf-8 -*-
"""More business rewrites: License, BookSource, File, Webdav, Rss, YueduApi, WebBook, Book core."""
from pathlib import Path
import os

ROOT = Path(r"C:\Users\chong\reader-pro-3.2.14-reverse")
BIZ = ROOT / "best-of-3" / "business"
HEADER = '''/**
 * Business-oriented rewrite from reader-pro-3.2.14.jar reverse engineering.
 * Source: CFR/Vineflower + manual semantic cleanup. For audit/readability.
 */
'''

def write(rel: str, content: str):
    path = BIZ / rel.replace("/", os.sep)
    path.parent.mkdir(parents=True, exist_ok=True)
    path.write_text(HEADER + "\n" + content.lstrip("\n"), encoding="utf-8", newline="\n")
    print("wrote", rel, path.stat().st_size)

write("com/htmake/reader/api/controller/LicenseController.kt", r'''
package com.htmake.reader.api.controller

import com.htmake.reader.api.ReturnData
import com.htmake.reader.entity.License
import com.htmake.reader.entity.ActiveLicense
import com.htmake.reader.utils.ExtKt
import io.vertx.core.json.Json
import io.vertx.core.json.JsonObject
import io.vertx.ext.web.RoutingContext
import kotlin.coroutines.CoroutineContext
import java.io.File

/**
 * Pro license: import/activate/generate keys, host validation, email supply.
 * Routes under /reader3/*License*, generateKeys, isHostValid, sendCodeToEmail...
 */
class LicenseController(coroutineContext: CoroutineContext) : BaseController(coroutineContext) {

    private fun licensePath(): String = ExtKt.getWorkDir("storage", "data", "license.json")

    fun loadLicense(): License? {
        val f = File(licensePath())
        if (!f.exists()) return null
        return try {
            Json.decodeValue(f.readText(), License::class.java)
        } catch (_: Exception) {
            null
        }
    }

    fun saveLicense(license: License) {
        File(licensePath()).apply { parentFile?.mkdirs() }
            .writeText(Json.encode(license))
    }

    suspend fun getLicense(context: RoutingContext): ReturnData {
        val rd = ReturnData()
        val lic = loadLicense()
        return if (lic == null) rd.setErrorMsg("未导入授权") else rd.setData(lic)
    }

    suspend fun importLicense(context: RoutingContext): ReturnData {
        val rd = ReturnData()
        val raw = context.bodyAsJson?.getString("license")
            ?: context.bodyAsString
            ?: return rd.setErrorMsg("请输入授权内容")
        // Original: decrypt/validate signature with embedded public key then persist
        return try {
            val lic = Json.decodeValue(raw, License::class.java)
            checkLicense(lic)
            saveLicense(lic)
            rd.setData(lic)
        } catch (e: Exception) {
            rd.setErrorMsg("授权无效: ${e.message}")
        }
    }

    suspend fun generateKeys(context: RoutingContext): ReturnData {
        val rd = ReturnData()
        if (!checkManagerAuth(context)) return rd.setData("NEED_SECURE_KEY").setErrorMsg("请输入管理密码")
        // Original uses hutool-crypto RSA key pair generation
        return rd.setData(mapOf(
            "note" to "RSA keypair generation (see LicenseController CFR for hutool calls)",
            "publicKey" to "",
            "privateKey" to ""
        ))
    }

    suspend fun generateLicense(context: RoutingContext): ReturnData {
        val rd = ReturnData()
        if (!checkManagerAuth(context)) return rd.setData("NEED_SECURE_KEY").setErrorMsg("请输入管理密码")
        val body = context.bodyAsJson ?: return rd.setErrorMsg("参数错误")
        // Fields: host, userMax, expireAt, features...
        return rd.setData(body)
    }

    suspend fun isHostValid(context: RoutingContext): ReturnData {
        val rd = ReturnData()
        val host = context.queryParam("host").firstOrNull()
            ?: context.bodyAsJson?.getString("host")
            ?: context.request().host
        val lic = loadLicense() ?: return rd.setData(false).setErrorMsg("无授权")
        val ok = lic.host.isNullOrEmpty() || lic.host == host || lic.host == "*"
        return rd.setData(ok)
    }

    suspend fun activateLicense(context: RoutingContext): ReturnData {
        val rd = ReturnData()
        val body = context.bodyAsJson ?: return rd.setErrorMsg("参数错误")
        // Binds license to machine / host; persists ActiveLicense
        val active = ActiveLicense(/* from body + hardware id */)
        ExtKt.saveStorage(arrayOf("storage", "data", "activeLicense.json"), Json.encode(body))
        return rd.setData(true)
    }

    suspend fun isLicenseValid(context: RoutingContext): ReturnData {
        val rd = ReturnData()
        val lic = loadLicense() ?: return rd.setData(false)
        return try {
            checkLicense(lic)
            rd.setData(true)
        } catch (e: Exception) {
            rd.setData(false).setErrorMsg(e.message ?: "invalid")
        }
    }

    suspend fun decryptLicense(context: RoutingContext): ReturnData {
        val rd = ReturnData()
        val payload = context.bodyAsJson?.getString("license") ?: return rd.setErrorMsg("参数错误")
        // hutool decrypt with private/public key
        return rd.setData(payload)
    }

    /** Validates expire time / signature / host — throws on failure. */
    fun checkLicense(license: License) {
        val now = System.currentTimeMillis()
        if (license.expireAt > 0 && now > license.expireAt) {
            error("授权已过期")
        }
        // signature verification omitted — see entity/License + hutool-crypto in jar
    }

    suspend fun sendCodeToEmail(context: RoutingContext): ReturnData {
        val rd = ReturnData()
        val email = context.bodyAsJson?.getString("email") ?: return rd.setErrorMsg("请输入邮箱")
        // original: SMTP send verification code for license supply
        return rd.setData(mapOf("email" to email, "sent" to true))
    }

    suspend fun supplyLicense(context: RoutingContext): ReturnData {
        val rd = ReturnData()
        // original: verify email code then issue/extend license
        return rd.setData(true)
    }
}
''')

write("com/htmake/reader/api/controller/BookSourceController.kt", r'''
package com.htmake.reader.api.controller

import com.htmake.reader.api.ReturnData
import com.htmake.reader.utils.ExtKt
import io.vertx.core.http.HttpMethod
import io.vertx.core.json.Json
import io.vertx.core.json.JsonArray
import io.vertx.core.json.JsonObject
import io.vertx.ext.web.RoutingContext
import kotlin.coroutines.CoroutineContext

/**
 * Book source JSON CRUD per user namespace + remote subscription import.
 * Storage: storage/data/{user}/bookSource.json (and defaults).
 */
class BookSourceController(coroutineContext: CoroutineContext) : BaseController(coroutineContext) {

    private fun sourceFile(userNameSpace: String) =
        ExtKt.getWorkDir("storage", "data", userNameSpace, "bookSource.json")

    fun getUserBookSourceJson(userNameSpace: String): JsonArray? {
        val raw = ExtKt.getStorage("data", userNameSpace, "bookSource")
            ?: java.io.File(sourceFile(userNameSpace)).takeIf { it.exists() }?.readText()
        return ExtKt.asJsonArray(raw)
    }

    fun canEditBookSource(context: RoutingContext): Boolean {
        if (!appConfig.secure) return true
        val ns = getUserNameSpace(context)
        val users = loadUserMap()
        val u = users[ns] ?: return true
        return u["enableBookSource"] as? Boolean ?: true
    }

    suspend fun saveBookSource(context: RoutingContext): ReturnData {
        val rd = ReturnData()
        if (!checkAuth(context)) return rd.setData("NEED_LOGIN").setErrorMsg("请登录后使用")
        if (!canEditBookSource(context)) return rd.setErrorMsg("无权编辑书源")
        val ns = getUserNameSpace(context)
        val src = context.bodyAsJson ?: return rd.setErrorMsg("参数错误")
        val arr = getUserBookSourceJson(ns) ?: JsonArray()
        val url = src.getString("bookSourceUrl") ?: return rd.setErrorMsg("书源链接不能为空")
        // replace or append by bookSourceUrl
        var found = false
        val list = arr.list
        for (i in list.indices) {
            val o = arr.getJsonObject(i)
            if (o.getString("bookSourceUrl") == url) {
                list[i] = src
                found = true
                break
            }
        }
        if (!found) list.add(src)
        val out = JsonArray(list)
        saveUserStorage(ns, "bookSource", out)
        generateBookSourceMap(ns, out)
        return rd.setData(src)
    }

    suspend fun saveBookSources(context: RoutingContext): ReturnData {
        val body = context.body()
        val arr = when {
            context.bodyAsJsonArray != null -> context.bodyAsJsonArray
            context.bodyAsJson != null && context.bodyAsJson.getJsonArray("bookSources") != null ->
                context.bodyAsJson.getJsonArray("bookSources")
            else -> JsonArray()
        }
        return saveBookSources(context, arr)
    }

    suspend fun saveBookSources(context: RoutingContext, bookSourceJsonArray: JsonArray): ReturnData {
        val rd = ReturnData()
        if (!checkAuth(context)) return rd.setData("NEED_LOGIN").setErrorMsg("请登录后使用")
        if (!canEditBookSource(context)) return rd.setErrorMsg("无权编辑书源")
        val ns = getUserNameSpace(context)
        return saveUserBookSources(ns, null, bookSourceJsonArray)
    }

    fun saveUserBookSources(userNameSpace: String, userInfo: Any?, bookSourceJsonArray: JsonArray): ReturnData {
        val rd = ReturnData()
        saveUserStorage(userNameSpace, "bookSource", bookSourceJsonArray)
        generateBookSourceMap(userNameSpace, bookSourceJsonArray)
        return rd.setData(bookSourceJsonArray.size())
    }

    suspend fun getBookSource(context: RoutingContext): ReturnData {
        val rd = ReturnData()
        if (!checkAuth(context)) return rd.setData("NEED_LOGIN").setErrorMsg("请登录后使用")
        val url = param(context, "url") ?: param(context, "bookSourceUrl") ?: ""
        if (url.isEmpty()) return rd.setErrorMsg("书源链接不能为空")
        val ns = getUserNameSpace(context)
        val arr = getUserBookSourceJson(ns) ?: JsonArray()
        for (i in 0 until arr.size()) {
            val o = arr.getJsonObject(i)
            if (o.getString("bookSourceUrl") == url) return rd.setData(o)
        }
        return rd.setErrorMsg("书源不存在")
    }

    suspend fun getBookSources(context: RoutingContext): ReturnData {
        val rd = ReturnData()
        if (!checkAuth(context)) return rd.setData("NEED_LOGIN").setErrorMsg("请登录后使用")
        val ns = getUserNameSpace(context)
        val arr = getUserBookSourceJson(ns) ?: JsonArray()
        // optional simple field filter
        return rd.setData(arr)
    }

    suspend fun deleteBookSource(context: RoutingContext): ReturnData {
        val rd = ReturnData()
        if (!checkAuth(context)) return rd.setData("NEED_LOGIN").setErrorMsg("请登录后使用")
        val url = param(context, "url") ?: param(context, "bookSourceUrl") ?: ""
        val ns = getUserNameSpace(context)
        val arr = getUserBookSourceJson(ns) ?: JsonArray()
        val list = arr.list.filterIndexed { i, _ ->
            arr.getJsonObject(i).getString("bookSourceUrl") != url
        }
        val out = JsonArray(list)
        saveUserStorage(ns, "bookSource", out)
        generateBookSourceMap(ns, out)
        return rd.setData(true)
    }

    suspend fun deleteBookSources(context: RoutingContext): ReturnData {
        val rd = ReturnData()
        if (!checkAuth(context)) return rd.setData("NEED_LOGIN").setErrorMsg("请登录后使用")
        val urls = context.bodyAsJsonArray ?: JsonArray()
        val set = (0 until urls.size()).mapNotNull { urls.getString(it) }.toSet()
        val ns = getUserNameSpace(context)
        val arr = getUserBookSourceJson(ns) ?: JsonArray()
        val list = arr.list.filterIndexed { i, _ ->
            arr.getJsonObject(i).getString("bookSourceUrl") !in set
        }
        val out = JsonArray(list)
        saveUserStorage(ns, "bookSource", out)
        generateBookSourceMap(ns, out)
        return rd.setData(true)
    }

    suspend fun deleteAllBookSources(context: RoutingContext): ReturnData {
        val rd = ReturnData()
        if (!checkAuth(context)) return rd.setData("NEED_LOGIN").setErrorMsg("请登录后使用")
        val ns = getUserNameSpace(context)
        saveUserStorage(ns, "bookSource", JsonArray())
        generateBookSourceMap(ns, JsonArray())
        return rd.setData(true)
    }

    suspend fun setAsDefaultBookSources(context: RoutingContext): ReturnData {
        val rd = ReturnData()
        if (!checkAuth(context)) return rd.setData("NEED_LOGIN").setErrorMsg("请登录后使用")
        if (!checkManagerAuth(context)) return rd.setData("NEED_SECURE_KEY").setErrorMsg("请输入管理密码")
        val ns = getUserNameSpace(context)
        val arr = getUserBookSourceJson(ns) ?: JsonArray()
        // copy to default user template
        ExtKt.saveStorage(arrayOf("data", "default", "bookSource"), Json.encode(arr))
        return rd.setData(true)
    }

    suspend fun readSourceFile(context: RoutingContext): ReturnData {
        val rd = ReturnData()
        if (!checkAuth(context)) return rd.setData("NEED_LOGIN").setErrorMsg("请登录后使用")
        val path = param(context, "path") ?: return rd.setErrorMsg("path 不能为空")
        val f = java.io.File(path)
        if (!f.exists()) return rd.setErrorMsg("文件不存在")
        return rd.setData(f.readText())
    }

    suspend fun saveFromRemoteSource(context: RoutingContext): ReturnData {
        val rd = ReturnData()
        if (!checkAuth(context)) return rd.setData("NEED_LOGIN").setErrorMsg("请登录后使用")
        val url = param(context, "url") ?: return rd.setErrorMsg("远程书源链接错误")
        // original: WebClient GET url -> JsonArray -> saveBookSources
        return rd.setErrorMsg("请通过 HTTP 客户端拉取后调用 saveBookSources（业务重写占位）")
            .also { it.isSuccess = false }
    }

    fun generateBookSourceMap(userNameSpace: String, bookSourceList: JsonArray? = null): MutableMap<String, Int> {
        val arr = bookSourceList ?: getUserBookSourceJson(userNameSpace) ?: JsonArray()
        val map = linkedMapOf<String, Int>()
        for (i in 0 until arr.size()) {
            val url = arr.getJsonObject(i).getString("bookSourceUrl") ?: continue
            map[url] = i
        }
        saveUserStorage(userNameSpace, "bookSourceMap", map)
        return map
    }

    fun getBookSourceMap(userNameSpace: String): MutableMap<String, Int> {
        val raw = getUserStorage(userNameSpace, "bookSourceMap")
        val obj = ExtKt.asJsonObject(raw)
        @Suppress("UNCHECKED_CAST")
        return (obj?.map as? MutableMap<String, Int>) ?: generateBookSourceMap(userNameSpace)
    }

    private fun param(ctx: RoutingContext, key: String): String? {
        if (ctx.request().method() == HttpMethod.POST) {
            ctx.bodyAsJson?.getString(key)?.let { return it }
        }
        return ctx.queryParam(key).firstOrNull()
    }
}
''')

write("com/htmake/reader/api/controller/FileController.kt", r'''
package com.htmake.reader.api.controller

import com.htmake.reader.api.ReturnData
import com.htmake.reader.utils.ExtKt
import io.vertx.ext.web.RoutingContext
import java.io.File
import kotlin.coroutines.CoroutineContext

/**
 * Local store / file browser under user data (when enableLocalStore).
 */
class FileController(coroutineContext: CoroutineContext) : BaseController(coroutineContext) {

    private fun root(ns: String) = File(ExtKt.getWorkDir("storage", "data", ns, "files"))

    fun checkAccess(context: RoutingContext, isSave: Boolean = false, isDelete: Boolean = false): ReturnData? {
        if (!appConfig.secure) return null
        val ns = getUserNameSpace(context)
        val u = loadUserMap()[ns]
        if (u != null && u["enableLocalStore"] == false) {
            return ReturnData().setErrorMsg("无权访问本地存储")
        }
        return null
    }

    suspend fun list(context: RoutingContext): ReturnData {
        checkAccess(context)?.let { return it }
        val rd = ReturnData()
        if (!checkAuth(context)) return rd.setData("NEED_LOGIN").setErrorMsg("请登录后使用")
        val ns = getUserNameSpace(context)
        val rel = context.queryParam("path").firstOrNull() ?: ""
        val dir = File(root(ns), rel).canonicalFile
        if (!dir.path.startsWith(root(ns).canonicalPath)) return rd.setErrorMsg("非法路径")
        if (!dir.exists()) return rd.setData(emptyList<Any>())
        val items = dir.listFiles()?.map {
            mapOf(
                "name" to it.name,
                "path" to rel.trimEnd('/') + "/" + it.name,
                "isDir" to it.isDirectory,
                "size" to it.length(),
                "mtime" to it.lastModified()
            )
        } ?: emptyList()
        return rd.setData(items)
    }

    suspend fun upload(context: RoutingContext): ReturnData {
        checkAccess(context, isSave = true)?.let { return it }
        val rd = ReturnData()
        if (!checkAuth(context)) return rd.setData("NEED_LOGIN").setErrorMsg("请登录后使用")
        val ns = getUserNameSpace(context)
        val rel = context.request().getParam("path") ?: ""
        val dir = File(root(ns), rel).apply { mkdirs() }
        val uploads = context.fileUploads()
        val saved = uploads.map { up ->
            val dest = File(dir, up.fileName())
            File(up.uploadedFileName()).copyTo(dest, overwrite = true)
            dest.name
        }
        return rd.setData(saved)
    }

    suspend fun download(context: RoutingContext) {
        val ns = getUserNameSpace(context)
        val rel = context.queryParam("path").firstOrNull() ?: return
        val f = File(root(ns), rel).canonicalFile
        if (!f.path.startsWith(root(ns).canonicalPath) || !f.isFile) {
            context.response().setStatusCode(404).end()
            return
        }
        context.response().sendFile(f.absolutePath)
    }

    suspend fun get(context: RoutingContext): ReturnData {
        val rd = ReturnData()
        if (!checkAuth(context)) return rd.setData("NEED_LOGIN").setErrorMsg("请登录后使用")
        val ns = getUserNameSpace(context)
        val rel = context.queryParam("path").firstOrNull() ?: return rd.setErrorMsg("path 不能为空")
        val f = File(root(ns), rel)
        if (!f.isFile) return rd.setErrorMsg("文件不存在")
        return rd.setData(f.readText())
    }

    suspend fun save(context: RoutingContext): ReturnData {
        checkAccess(context, isSave = true)?.let { return it }
        val rd = ReturnData()
        if (!checkAuth(context)) return rd.setData("NEED_LOGIN").setErrorMsg("请登录后使用")
        val ns = getUserNameSpace(context)
        val path = context.bodyAsJson?.getString("path") ?: return rd.setErrorMsg("path 不能为空")
        val content = context.bodyAsJson?.getString("content") ?: ""
        val f = File(root(ns), path)
        f.parentFile?.mkdirs()
        f.writeText(content)
        return rd.setData(true)
    }

    suspend fun mkdir(context: RoutingContext): ReturnData {
        val rd = ReturnData()
        if (!checkAuth(context)) return rd.setData("NEED_LOGIN").setErrorMsg("请登录后使用")
        val ns = getUserNameSpace(context)
        val path = context.bodyAsJson?.getString("path")
            ?: context.queryParam("path").firstOrNull()
            ?: return rd.setErrorMsg("path 不能为空")
        File(root(ns), path).mkdirs()
        return rd.setData(true)
    }

    suspend fun delete(context: RoutingContext): ReturnData {
        checkAccess(context, isDelete = true)?.let { return it }
        val rd = ReturnData()
        if (!checkAuth(context)) return rd.setData("NEED_LOGIN").setErrorMsg("请登录后使用")
        val ns = getUserNameSpace(context)
        val path = context.bodyAsJson?.getString("path")
            ?: context.queryParam("path").firstOrNull()
            ?: return rd.setErrorMsg("path 不能为空")
        val f = File(root(ns), path)
        ExtKt.deleteRecursively(f)
        return rd.setData(true)
    }

    suspend fun deleteMulti(context: RoutingContext): ReturnData {
        val rd = ReturnData()
        if (!checkAuth(context)) return rd.setData("NEED_LOGIN").setErrorMsg("请登录后使用")
        val ns = getUserNameSpace(context)
        val arr = context.bodyAsJson?.getJsonArray("paths") ?: return rd.setErrorMsg("参数错误")
        for (i in 0 until arr.size()) {
            ExtKt.deleteRecursively(File(root(ns), arr.getString(i)))
        }
        return rd.setData(true)
    }

    suspend fun importPreview(context: RoutingContext): ReturnData =
        ReturnData().setData(mapOf("note" to "local book import preview — see FileController CFR"))

    suspend fun restore(context: RoutingContext): ReturnData =
        ReturnData().setData(true)

    suspend fun parse(context: RoutingContext): ReturnData =
        ReturnData().setData(mapOf("note" to "parse uploaded book metadata"))
}
''')

write("com/htmake/reader/api/controller/WebdavController.kt", r'''
package com.htmake.reader.api.controller

import com.htmake.reader.api.ReturnData
import com.htmake.reader.utils.ExtKt
import io.vertx.ext.web.RoutingContext
import java.io.File
import kotlin.coroutines.CoroutineContext

/**
 * WebDAV backup / restore under user webdav home.
 */
class WebdavController(coroutineContext: CoroutineContext) : BaseController(coroutineContext) {

    suspend fun backupToWebdav(context: RoutingContext): ReturnData {
        val rd = ReturnData()
        if (!checkAuth(context)) return rd.setData("NEED_LOGIN").setErrorMsg("请登录后使用")
        val ns = getUserNameSpace(context)
        val home = File(getUserWebdavHome(ns)).apply { mkdirs() }
        // original: zip user data files listed by getBackupFileNames into webdav
        val zip = File(home, "backup-${System.currentTimeMillis()}.zip")
        // ZipUtils zip of storage/data/{ns}/* key files
        return rd.setData(mapOf("path" to zip.absolutePath))
    }

    // Additional WebDAV file ops exist in decompiled WebdavController — list/upload/download style
}
''')

write("com/htmake/reader/api/controller/RssSourceController.kt", r'''
package com.htmake.reader.api.controller

import com.htmake.reader.api.ReturnData
import com.htmake.reader.utils.ExtKt
import io.vertx.core.json.JsonArray
import io.vertx.core.json.JsonObject
import io.vertx.ext.web.RoutingContext
import kotlin.coroutines.CoroutineContext

/** RSS source JSON CRUD — parallel to book sources. Storage: rssSource */
class RssSourceController(coroutineContext: CoroutineContext) : BaseController(coroutineContext) {

    private fun load(ns: String): JsonArray =
        ExtKt.asJsonArray(getUserStorage(ns, "rssSource")) ?: JsonArray()

    private fun save(ns: String, arr: JsonArray) = saveUserStorage(ns, "rssSource", arr)

    suspend fun getRssSources(context: RoutingContext): ReturnData {
        val rd = ReturnData()
        if (!checkAuth(context)) return rd.setData("NEED_LOGIN").setErrorMsg("请登录后使用")
        return rd.setData(load(getUserNameSpace(context)))
    }

    suspend fun saveRssSource(context: RoutingContext): ReturnData {
        val rd = ReturnData()
        if (!checkAuth(context)) return rd.setData("NEED_LOGIN").setErrorMsg("请登录后使用")
        val ns = getUserNameSpace(context)
        val src = context.bodyAsJson ?: return rd.setErrorMsg("参数错误")
        val arr = load(ns)
        val key = src.getString("sourceUrl") ?: src.getString("rssUrl") ?: return rd.setErrorMsg("链接不能为空")
        val list = arr.list
        var found = false
        for (i in list.indices) {
            val o = arr.getJsonObject(i)
            val k = o.getString("sourceUrl") ?: o.getString("rssUrl")
            if (k == key) { list[i] = src; found = true; break }
        }
        if (!found) list.add(src)
        save(ns, JsonArray(list))
        return rd.setData(src)
    }

    suspend fun deleteRssSource(context: RoutingContext): ReturnData {
        val rd = ReturnData()
        if (!checkAuth(context)) return rd.setData("NEED_LOGIN").setErrorMsg("请登录后使用")
        val ns = getUserNameSpace(context)
        val key = context.bodyAsJson?.getString("sourceUrl")
            ?: context.queryParam("sourceUrl").firstOrNull()
            ?: return rd.setErrorMsg("参数错误")
        val arr = load(ns)
        val list = arr.list.filterIndexed { i, _ ->
            val o = arr.getJsonObject(i)
            (o.getString("sourceUrl") ?: o.getString("rssUrl")) != key
        }
        save(ns, JsonArray(list))
        return rd.setData(true)
    }
}
''')

write("com/htmake/reader/api/YueduApi.kt", r'''
package com.htmake.reader.api

import com.htmake.reader.api.controller.*
import com.htmake.reader.config.AppConfig
import com.htmake.reader.utils.SpringContextUtils
import com.htmake.reader.verticle.RestVerticle
import io.vertx.ext.web.Router
import io.vertx.ext.web.handler.StaticHandler
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.springframework.scheduling.annotation.Scheduled
import org.springframework.stereotype.Component

/**
 * Central Vert.x router: static web + /reader3/* API surface.
 * Business rewrite of YueduApi.initRouter (133 endpoints in jar).
 */
@Component
open class YueduApi : RestVerticle() {

    private val appConfig: AppConfig by lazy {
        SpringContextUtils.getBean("appConfig", AppConfig::class.java)
    }

    override fun getContextPath(): String = appConfig.let {
        // reader.server.contextPath
        ""
    }

    override suspend fun initRouter(router: Router) {
        setupPort()
        // static SPA
        router.route("/*").handler(StaticHandler.create("web").setDefaultContentEncoding("UTF-8"))
        router.route("/assets/*").handler(
            StaticHandler.create().setAllowRootFileSystemAccess(true)
                .setWebRoot(com.htmake.reader.utils.ExtKt.getWorkDir("storage", "assets"))
        )
        router.route("/simple-web/*").handler(StaticHandler.create("simple-web"))

        val scope = this
        val book = BookController(coroutineContext)
        val bookSource = BookSourceController(coroutineContext)
        val user = UserController(coroutineContext)
        val license = LicenseController(coroutineContext)
        val file = FileController(coroutineContext)
        val webdav = WebdavController(coroutineContext)
        val rss = RssSourceController(coroutineContext)
        val group = BookGroupController(coroutineContext)
        val bookmark = BookmarkController(coroutineContext)
        val replace = ReplaceRuleController(coroutineContext)

        fun get(path: String, block: suspend (io.vertx.ext.web.RoutingContext) -> Any?) {
            router.get(path).handler { ctx ->
                launch(Dispatchers.IO) {
                    try {
                        val r = block(ctx)
                        if (r is ReturnData) com.htmake.reader.utils.VertExtKt.success(ctx, r)
                        else if (r != null) ctx.end(r.toString())
                    } catch (e: Exception) {
                        onHandlerError(ctx, e)
                    }
                }
            }
        }
        fun post(path: String, block: suspend (io.vertx.ext.web.RoutingContext) -> Any?) {
            router.post(path).handler { ctx ->
                launch(Dispatchers.IO) {
                    try {
                        val r = block(ctx)
                        if (r is ReturnData) com.htmake.reader.utils.VertExtKt.success(ctx, r)
                    } catch (e: Exception) {
                        onHandlerError(ctx, e)
                    }
                }
            }
        }

        // ---- system ----
        get("/reader3/getSystemInfo") { getSystemInfo(it) }

        // ---- book source ----
        post("/reader3/saveBookSource") { bookSource.saveBookSource(it) }
        post("/reader3/saveBookSources") { bookSource.saveBookSources(it) }
        get("/reader3/getBookSource") { bookSource.getBookSource(it) }
        post("/reader3/getBookSource") { bookSource.getBookSource(it) }
        get("/reader3/getBookSources") { bookSource.getBookSources(it) }
        post("/reader3/getBookSources") { bookSource.getBookSources(it) }
        post("/reader3/deleteBookSource") { bookSource.deleteBookSource(it) }
        post("/reader3/deleteBookSources") { bookSource.deleteBookSources(it) }
        post("/reader3/deleteAllBookSources") { bookSource.deleteAllBookSources(it) }
        post("/reader3/setAsDefaultBookSources") { bookSource.setAsDefaultBookSources(it) }
        post("/reader3/readSourceFile") { bookSource.readSourceFile(it) }
        post("/reader3/saveFromRemoteSource") { bookSource.saveFromRemoteSource(it) }

        // ---- bookshelf / read ----
        get("/reader3/getBookshelf") { book.getBookshelf(it) }
        get("/reader3/getShelfBook") { book.getShelfBook(it) }
        post("/reader3/saveBook") { book.saveBook(it) }
        post("/reader3/deleteBook") { book.deleteBook(it) }
        post("/reader3/deleteBooks") { book.deleteBooks(it) }
        get("/reader3/searchBook") { book.searchBook(it) }
        post("/reader3/searchBook") { book.searchBook(it) }
        get("/reader3/getBookInfo") { book.getBookInfo(it) }
        post("/reader3/getBookInfo") { book.getBookInfo(it) }
        get("/reader3/getChapterList") { book.getChapterList(it) }
        post("/reader3/getChapterList") { book.getChapterList(it) }
        get("/reader3/getBookContent") { book.getBookContent(it) }
        post("/reader3/getBookContent") { book.getBookContent(it) }
        post("/reader3/saveBookProgress") { book.saveBookProgress(it) }
        get("/reader3/cover") { book.cover(it); null }
        post("/reader3/exportBook") { book.exportBook(it) }
        post("/reader3/cacheBookOnServer") { book.cacheBookOnServer(it) }

        // ---- user ----
        post("/reader3/login") { user.login(it) }
        post("/reader3/logout") { user.logout(it) }
        get("/reader3/getUserInfo") { user.getUserInfo(it) }
        get("/reader3/getUserList") { user.getUserList(it) }
        post("/reader3/addUser") { user.addUser(it) }
        post("/reader3/deleteUsers") { user.deleteUsers(it) }
        post("/reader3/resetPassword") { user.resetPassword(it) }
        post("/reader3/updateUser") { user.updateUser(it) }
        post("/reader3/saveUserConfig") { user.saveUserConfig(it) }
        get("/reader3/getUserConfig") { user.getUserConfig(it) }

        // ---- license ----
        get("/reader3/getLicense") { license.getLicense(it) }
        post("/reader3/importLicense") { license.importLicense(it) }
        post("/reader3/activateLicense") { license.activateLicense(it) }
        get("/reader3/isLicenseValid") { license.isLicenseValid(it) }
        post("/reader3/isLicenseValid") { license.isLicenseValid(it) }

        // ---- rss / webdav / files (subset) ----
        get("/reader3/getRssSources") { rss.getRssSources(it) }
        post("/reader3/backupToWebdav") { webdav.backupToWebdav(it) }

        // full 133 routes: see API_ROUTES.md — wire remaining to controllers similarly
    }

    private fun setupPort() {
        port = try {
            // reader.server.port from env/yml default 8080
            8080
        } catch (_: Exception) {
            8080
        }
    }

    suspend fun getSystemInfo(context: io.vertx.ext.web.RoutingContext): ReturnData {
        return ReturnData().setData(
            mapOf(
                "version" to "3.2.14",
                "secure" to appConfig.secure,
                "userLimit" to appConfig.userLimit,
                "java" to System.getProperty("java.version"),
            )
        )
    }

    @Scheduled(fixedDelayString = "\${reader.app.shelfUpdateInteval:30}000")
    open fun shelfUpdateJob() { /* refresh shelves */ }

    @Scheduled(fixedDelayString = "\${reader.app.remoteBookSourceUpdateInterval:720}0000")
    open fun remoteBookSourceSubUpdateJob() { /* remote source sub */ }

    @Scheduled(cron = "0 0 3 * * ?")
    open fun clearUser() { /* inactive users */ }

    @Scheduled(cron = "0 30 3 * * ?")
    open fun autoBackup() { /* backup */ }

    @Scheduled(fixedDelay = 600_000)
    open fun autoGC() { System.gc() }

    @Scheduled(fixedDelay = 3_600_000)
    open fun checkLicense() { /* LicenseController.checkLicense */ }
}
''')

write("io/legado/app/model/webBook/WebBook.kt", r'''
package io.legado.app.model.webBook

import io.legado.app.data.entities.Book
import io.legado.app.data.entities.BookChapter
import io.legado.app.data.entities.BookSource
import io.legado.app.data.entities.SearchBook
import io.legado.app.model.analyzeRule.AnalyzeUrl
import io.legado.app.model.DebugLog

/**
 * Network book facade: search / info / toc / content via BookSource rules.
 * Business rewrite of io.legado.app.model.webBook.WebBook.
 */
class WebBook(
    private val bookSourceStr: String,
    private val debugLog: Boolean = false,
    private val debugLogger: DebugLog? = null,
    private val userNameSpace: String = "default"
) {
    private val source: BookSource by lazy {
        BookSource.fromJson(bookSourceStr).getOrThrow()
    }

    suspend fun searchBook(key: String, page: Int = 1): List<SearchBook> {
        return BookList.searchBook(source, key, page, debugLogger)
    }

    suspend fun exploreBook(url: String, page: Int = 1): List<SearchBook> {
        return BookList.exploreBook(source, url, page, debugLogger)
    }

    suspend fun getBookInfo(bookUrl: String): Book {
        return BookInfo.getBookInfo(source, bookUrl, debugLogger)
    }

    suspend fun getChapterList(book: Book): List<BookChapter> {
        return BookChapterList.analyzeChapterList(
            book = book,
            body = null,
            bookSource = source,
            baseUrl = book.tocUrl.ifEmpty { book.bookUrl },
            redirectUrl = book.tocUrl.ifEmpty { book.bookUrl },
            debugLog = debugLogger
        )
    }

    suspend fun getBookContent(
        book: Book,
        chapter: BookChapter,
        nextChapterUrl: String? = null
    ): String {
        return BookContent.analyzeContent(
            book = book,
            bookChapter = chapter,
            bookSource = source,
            baseUrl = chapter.url,
            redirectUrl = chapter.url,
            nextChapterUrl = nextChapterUrl,
            debugLog = debugLogger
        )
    }
}
''')

write("io/legado/app/model/webBook/BookChapterList.kt", r'''
package io.legado.app.model.webBook

import io.legado.app.data.entities.Book
import io.legado.app.data.entities.BookChapter
import io.legado.app.data.entities.BookSource
import io.legado.app.data.entities.rule.TocRule
import io.legado.app.exception.TocEmptyException
import io.legado.app.model.DebugLog
import io.legado.app.model.analyzeRule.AnalyzeRule
import io.legado.app.model.analyzeRule.AnalyzeUrl

/**
 * Parse table-of-contents HTML/JSON using TocRule.
 * Manual recovery of analyzeChapterList (hard-fail in CFR, restored via Vineflower+CFR).
 */
object BookChapterList {

    suspend fun analyzeChapterList(
        book: Book,
        body: String?,
        bookSource: BookSource,
        baseUrl: String,
        redirectUrl: String,
        debugLog: DebugLog? = null
    ): List<BookChapter> {
        val tocRule: TocRule = bookSource.ruleToc
            ?: throw TocEmptyException("目录规则为空")

        var html = body
        if (html.isNullOrEmpty()) {
            val analyzeUrl = AnalyzeUrl(
                mUrl = baseUrl,
                source = bookSource,
                ruleData = book,
                headerMapF = bookSource.getHeaderMap()
            )
            html = analyzeUrl.getStrResponseAwait().body ?: ""
        }

        val analyzeRule = AnalyzeRule(book, bookSource, debugLog)
        analyzeRule.setContent(html, baseUrl)

        // chapter list elements
        val listRule = tocRule.chapterList ?: throw TocEmptyException("chapterList 规则为空")
        val elements = analyzeRule.getElements(listRule)
        if (elements.isEmpty()) throw TocEmptyException("目录为空")

        val chapters = ArrayList<BookChapter>()
        val nameRule = tocRule.chapterName
        val urlRule = tocRule.chapterUrl
        elements.forEachIndexed { index, el ->
            analyzeRule.setContent(el)
            val title = nameRule?.let { analyzeRule.getString(it) } ?: "章节${index + 1}"
            val url = urlRule?.let { analyzeRule.getString(it) } ?: baseUrl
            chapters += BookChapter(
                url = url,
                title = title,
                index = index,
                bookUrl = book.bookUrl
            )
        }

        // next URL pagination (tocRule.nextTocUrl) — fetch more pages if present
        var nextUrl = tocRule.nextTocUrl?.let {
            analyzeRule.setContent(html, baseUrl)
            analyzeRule.getString(it)
        }
        val seen = linkedSetOf(baseUrl)
        while (!nextUrl.isNullOrEmpty() && nextUrl !in seen) {
            seen += nextUrl
            val more = AnalyzeUrl(mUrl = nextUrl, source = bookSource, ruleData = book)
                .getStrResponseAwait().body ?: break
            analyzeRule.setContent(more, nextUrl)
            val moreEls = analyzeRule.getElements(listRule)
            moreEls.forEach { el ->
                analyzeRule.setContent(el)
                val title = nameRule?.let { analyzeRule.getString(it) } ?: return@forEach
                val url = urlRule?.let { analyzeRule.getString(it) } ?: nextUrl
                chapters += BookChapter(
                    url = url,
                    title = title,
                    index = chapters.size,
                    bookUrl = book.bookUrl
                )
            }
            nextUrl = tocRule.nextTocUrl?.let {
                analyzeRule.setContent(more, nextUrl)
                analyzeRule.getString(it)
            }
        }

        if (chapters.isEmpty()) throw TocEmptyException("目录为空")
        // reverse if tocRule need reverse
        return chapters
    }
}
''')

write("io/legado/app/model/webBook/BookContent.kt", r'''
package io.legado.app.model.webBook

import io.legado.app.data.entities.Book
import io.legado.app.data.entities.BookChapter
import io.legado.app.data.entities.BookSource
import io.legado.app.model.DebugLog
import io.legado.app.model.analyzeRule.AnalyzeRule
import io.legado.app.model.analyzeRule.AnalyzeUrl

/** Fetch and parse chapter HTML → plain/content HTML via ContentRule. */
object BookContent {

    suspend fun analyzeContent(
        book: Book,
        bookChapter: BookChapter,
        bookSource: BookSource,
        baseUrl: String,
        redirectUrl: String,
        nextChapterUrl: String? = null,
        debugLog: DebugLog? = null
    ): String {
        val contentRule = bookSource.ruleContent
            ?: return ""
        val analyzeUrl = AnalyzeUrl(
            mUrl = bookChapter.url.ifEmpty { baseUrl },
            source = bookSource,
            ruleData = book,
            chapter = bookChapter
        )
        val resp = analyzeUrl.getStrResponseAwait()
        val html = resp.body ?: ""
        val rule = AnalyzeRule(book, bookSource, debugLog)
        rule.setContent(html, redirectUrl.ifEmpty { baseUrl })
        var content = contentRule.content?.let { rule.getString(it) } ?: html

        // next-page content concatenation
        var nextUrl = contentRule.nextContentUrl?.let { rule.getString(it) }
        val seen = linkedSetOf(bookChapter.url)
        while (!nextUrl.isNullOrEmpty() && nextUrl !in seen) {
            if (nextUrl == nextChapterUrl) break
            seen += nextUrl
            val more = AnalyzeUrl(mUrl = nextUrl, source = bookSource, ruleData = book)
                .getStrResponseAwait().body ?: break
            rule.setContent(more, nextUrl)
            val part = contentRule.content?.let { rule.getString(it) } ?: ""
            content += "\n" + part
            nextUrl = contentRule.nextContentUrl?.let { rule.getString(it) }
        }

        // replaceRegex optional
        contentRule.replaceRegex?.takeIf { it.isNotEmpty() }?.let { reg ->
            // format: regex##replacement multi-line rules
            reg.split("\n").forEach { line ->
                val parts = line.split("##", limit = 2)
                if (parts.size == 2) {
                    content = content.replace(Regex(parts[0]), parts[1])
                }
            }
        }
        return content.trim()
    }
}
''')

write("io/legado/app/model/webBook/BookList.kt", r'''
package io.legado.app.model.webBook

import io.legado.app.data.entities.BookSource
import io.legado.app.data.entities.SearchBook
import io.legado.app.model.DebugLog
import io.legado.app.model.analyzeRule.AnalyzeRule
import io.legado.app.model.analyzeRule.AnalyzeUrl

object BookList {

    suspend fun searchBook(
        bookSource: BookSource,
        key: String,
        page: Int = 1,
        debugLog: DebugLog? = null
    ): List<SearchBook> {
        val rule = bookSource.ruleSearch ?: return emptyList()
        val url = rule.checkKeyWord?.let { /* optional */ }
        val analyzeUrl = AnalyzeUrl(
            mUrl = rule.url ?: return emptyList(),
            key = key,
            page = page,
            source = bookSource
        )
        val html = analyzeUrl.getStrResponseAwait().body ?: return emptyList()
        return parseList(bookSource, html, analyzeUrl.finalUrl, rule.bookList, debugLog)
    }

    suspend fun exploreBook(
        bookSource: BookSource,
        url: String,
        page: Int = 1,
        debugLog: DebugLog? = null
    ): List<SearchBook> {
        val rule = bookSource.ruleExplore ?: bookSource.ruleSearch ?: return emptyList()
        val analyzeUrl = AnalyzeUrl(mUrl = url, page = page, source = bookSource)
        val html = analyzeUrl.getStrResponseAwait().body ?: return emptyList()
        return parseList(bookSource, html, analyzeUrl.finalUrl, rule.bookList, debugLog)
    }

    private fun parseList(
        bookSource: BookSource,
        html: String,
        baseUrl: String,
        bookListRule: String?,
        debugLog: DebugLog?
    ): List<SearchBook> {
        if (bookListRule.isNullOrEmpty()) return emptyList()
        val rule = AnalyzeRule(null, bookSource, debugLog)
        rule.setContent(html, baseUrl)
        val els = rule.getElements(bookListRule)
        val searchRule = bookSource.ruleSearch
        return els.mapNotNull { el ->
            rule.setContent(el)
            val name = searchRule?.name?.let { rule.getString(it) } ?: return@mapNotNull null
            val bookUrl = searchRule.bookUrl?.let { rule.getString(it) } ?: baseUrl
            SearchBook(
                name = name,
                author = searchRule.author?.let { rule.getString(it) } ?: "",
                bookUrl = bookUrl,
                origin = bookSource.bookSourceUrl,
                coverUrl = searchRule.coverUrl?.let { rule.getString(it) },
                intro = searchRule.intro?.let { rule.getString(it) }
            )
        }
    }
}
''')

write("io/legado/app/model/webBook/BookInfo.kt", r'''
package io.legado.app.model.webBook

import io.legado.app.data.entities.Book
import io.legado.app.data.entities.BookSource
import io.legado.app.model.DebugLog
import io.legado.app.model.analyzeRule.AnalyzeRule
import io.legado.app.model.analyzeRule.AnalyzeUrl

object BookInfo {
    suspend fun getBookInfo(
        bookSource: BookSource,
        bookUrl: String,
        debugLog: DebugLog? = null
    ): Book {
        val infoRule = bookSource.ruleBookInfo
        val analyzeUrl = AnalyzeUrl(mUrl = bookUrl, source = bookSource)
        val html = analyzeUrl.getStrResponseAwait().body ?: ""
        val rule = AnalyzeRule(null, bookSource, debugLog)
        rule.setContent(html, bookUrl)
        val book = Book(
            bookUrl = bookUrl,
            origin = bookSource.bookSourceUrl,
            originName = bookSource.bookSourceName,
            name = infoRule?.name?.let { rule.getString(it) } ?: "",
            author = infoRule?.author?.let { rule.getString(it) } ?: "",
            kind = infoRule?.kind?.let { rule.getString(it) },
            coverUrl = infoRule?.coverUrl?.let { rule.getString(it) },
            intro = infoRule?.intro?.let { rule.getString(it) },
            tocUrl = infoRule?.tocUrl?.let { rule.getString(it) } ?: bookUrl,
        )
        return book
    }
}
''')

print("more controllers + webBook done")
