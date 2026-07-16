package com.htmake.reader.api.controller

import com.htmake.reader.api.ReturnData
import com.htmake.reader.utils.ExtKt
import io.vertx.core.json.JsonArray
import io.vertx.core.json.JsonObject
import io.vertx.ext.web.RoutingContext
import java.io.File

// ---- BookSource extras ----
suspend fun BookSourceController.deleteBookSources(ctx: RoutingContext): ReturnData {
    val rd = ReturnData()
    if (!checkAuth(ctx)) return rd.setData("NEED_LOGIN").setErrorMsg("请登录后使用")
    val urls = ctx.bodyAsJson?.getJsonArray("urls")
        ?: ctx.bodyAsJsonArray
        ?: JsonArray()
    val set = (0 until urls.size()).mapNotNull {
        when (val v = urls.getValue(it)) {
            is String -> v
            is JsonObject -> v.getString("bookSourceUrl")
            else -> null
        }
    }.toSet()
    val ns = getUserNameSpace(ctx)
    val arr = getUserBookSourceJson(ns) ?: JsonArray()
    val list = arr.list.filterIndexed { i, _ -> arr.getJsonObject(i).getString("bookSourceUrl") !in set }
    saveUserStorage(ns, "bookSource", JsonArray(list))
    return rd.setData(true)
}

suspend fun BookSourceController.readSourceFile(ctx: RoutingContext): ReturnData {
    val rd = ReturnData()
    if (!checkAuth(ctx)) return rd.setData("NEED_LOGIN").setErrorMsg("请登录后使用")
    val path = ctx.bodyAsJson?.getString("path") ?: ctx.queryParam("path").firstOrNull()
        ?: return rd.setErrorMsg("path 不能为空")
    val f = File(path)
    if (!f.isFile) return rd.setErrorMsg("文件不存在")
    return rd.setData(f.readText())
}

suspend fun BookSourceController.setAsDefaultBookSources(ctx: RoutingContext): ReturnData {
    val rd = ReturnData()
    if (!checkAuth(ctx)) return rd.setData("NEED_LOGIN").setErrorMsg("请登录后使用")
    if (!checkManagerAuth(ctx)) return rd.setData("NEED_SECURE_KEY").setErrorMsg("请输入管理密码")
    val ns = getUserNameSpace(ctx)
    val arr = getUserBookSourceJson(ns) ?: JsonArray()
    ExtKt.saveStorage(arrayOf("data", "default", "bookSource"), arr.encode())
    return rd.setData(true)
}

suspend fun BookSourceController.deleteUserBookSource(ctx: RoutingContext): ReturnData {
    val rd = ReturnData()
    if (!checkManagerAuth(ctx)) return rd.setErrorMsg("需要管理密码")
    val user = ctx.bodyAsJson?.getString("username") ?: return rd.setErrorMsg("username 不能为空")
    ExtKt.deleteRecursively(File(ExtKt.getWorkDir("storage", "data", user, "bookSource.json")))
    File(ExtKt.getWorkDir("storage", "data", user, "bookSource.json")).delete()
    return rd.setData(true)
}

suspend fun BookSourceController.deleteBookSourcesFile(ctx: RoutingContext): ReturnData {
    val rd = ReturnData()
    if (!checkAuth(ctx)) return rd.setData("NEED_LOGIN").setErrorMsg("请登录后使用")
    val ns = getUserNameSpace(ctx)
    File(ExtKt.getWorkDir("storage", "data", ns, "bookSource.json")).delete()
    saveUserStorage(ns, "bookSource", JsonArray())
    return rd.setData(true)
}

/**
 * GET/POST ?bookSourceUrl= — return loginUi fields (+ saved values).
 */
suspend fun BookSourceController.getLoginUi(ctx: RoutingContext): ReturnData {
    val rd = ReturnData()
    if (!checkAuth(ctx)) return rd.setData("NEED_LOGIN").setErrorMsg("请登录后使用")
    val url = ctx.queryParam("bookSourceUrl").firstOrNull()
        ?: ctx.bodyAsJson?.getString("bookSourceUrl")
        ?: return rd.setErrorMsg("bookSourceUrl 不能为空")
    val ns = getUserNameSpace(ctx)
    val src = loadBookSource(ns, url) ?: return rd.setErrorMsg("书源不存在")
    return rd.setData(io.legado.app.help.SourceLogin.getLoginUiPayload(src))
}

/**
 * POST body: bookSourceUrl + loginInfo{...}  or flat username/password fields.
 * Saves form then runs loginUrl JS.
 */
suspend fun BookSourceController.loginBookSource(ctx: RoutingContext): ReturnData {
    val rd = ReturnData()
    if (!checkAuth(ctx)) return rd.setData("NEED_LOGIN").setErrorMsg("请登录后使用")
    val body = ctx.bodyAsJson ?: return rd.setErrorMsg("参数错误")
    val url = body.getString("bookSourceUrl")
        ?: ctx.queryParam("bookSourceUrl").firstOrNull()
        ?: return rd.setErrorMsg("bookSourceUrl 不能为空")
    val ns = getUserNameSpace(ctx)
    val src = loadBookSource(ns, url) ?: return rd.setErrorMsg("书源不存在")
    val form = linkedMapOf<String, String>()
    body.getJsonObject("loginInfo")?.forEach { (k, v) -> form[k] = v?.toString() ?: "" }
    // also accept top-level field values except meta keys
    body.forEach { (k, v) ->
        if (k !in setOf("bookSourceUrl", "loginInfo") && v != null) form[k] = v.toString()
    }
    return try {
        val result = if (form.isNotEmpty()) {
            io.legado.app.help.SourceLogin.loginWithForm(src, form)
        } else {
            val ok = io.legado.app.help.SourceLogin.login(src)
            if (!ok) io.legado.app.help.SourceLogin.ensureLoginIfNeeded(src)
            mapOf("ok" to (src.getLoginHeader() != null), "loginHeader" to src.getLoginHeader())
        }
        rd.setData(result)
    } catch (e: Exception) {
        rd.setErrorMsg(e.message ?: "登录失败")
    }
}

suspend fun BookSourceController.logoutBookSource(ctx: RoutingContext): ReturnData {
    val rd = ReturnData()
    if (!checkAuth(ctx)) return rd.setData("NEED_LOGIN").setErrorMsg("请登录后使用")
    val url = ctx.bodyAsJson?.getString("bookSourceUrl")
        ?: ctx.queryParam("bookSourceUrl").firstOrNull()
        ?: return rd.setErrorMsg("bookSourceUrl 不能为空")
    val ns = getUserNameSpace(ctx)
    val src = loadBookSource(ns, url)
    if (src != null) io.legado.app.help.SourceLogin.clearLogin(src)
    else {
        io.legado.app.help.CacheManager(ns).delete("loginHeader_$url")
        io.legado.app.help.CacheManager(ns).delete("userInfo_$url")
        io.legado.app.help.CacheManager(ns).delete("userInfo_plain_$url")
    }
    return rd.setData(true)
}

private fun BookSourceController.loadBookSource(ns: String, url: String): io.legado.app.data.entities.BookSource? {
    val raw = getUserBookSourceJson(ns)?.let { arr ->
        (0 until arr.size()).mapNotNull { i -> arr.getJsonObject(i) }
            .firstOrNull { it.getString("bookSourceUrl") == url }?.encode()
    } ?: return null
    return io.legado.app.data.entities.BookSource.fromJson(raw).getOrNull()?.also {
        it.setUserNameSpace(ns)
    }
}

// ---- Bookmark extras ----
suspend fun BookmarkController.saveBookmarks(ctx: RoutingContext) = save(ctx)
suspend fun BookmarkController.deleteBookmarks(ctx: RoutingContext): ReturnData {
    val rd = ReturnData()
    if (!checkAuth(ctx)) return rd.setData("NEED_LOGIN").setErrorMsg("请登录后使用")
    // clear all or by ids
    val ids = ctx.bodyAsJson?.getJsonArray("ids")
    val ns = getUserNameSpace(ctx)
    if (ids == null) {
        saveUserStorage(ns, "bookmark", JsonArray())
    } else {
        val arr = ExtKt.asJsonArray(getUserStorage(ns, "bookmark")) ?: JsonArray()
        val drop = (0 until ids.size()).map { ids.getValue(it).toString() }.toSet()
        val list = arr.list.filterIndexed { i, _ ->
            val o = arr.getJsonObject(i)
            (o.getString("id") ?: o.getInteger("index")?.toString() ?: "") !in drop
        }
        saveUserStorage(ns, "bookmark", JsonArray(list))
    }
    return rd.setData(true)
}

// ---- Replace extras ----
suspend fun ReplaceRuleController.saveReplaceRules(ctx: RoutingContext) = save(ctx)
suspend fun ReplaceRuleController.deleteReplaceRules(ctx: RoutingContext): ReturnData {
    val rd = ReturnData()
    if (!checkAuth(ctx)) return rd.setData("NEED_LOGIN").setErrorMsg("请登录后使用")
    saveUserStorage(getUserNameSpace(ctx), "replaceRule", JsonArray())
    return rd.setData(true)
}

// ---- RSS extras ----
suspend fun RssSourceController.saveRssSources(ctx: RoutingContext) = save(ctx)

// ---- HttpTTS extras ----
suspend fun HttpTTSController.saveMulti(ctx: RoutingContext): ReturnData {
    val rd = ReturnData()
    if (!checkAuth(ctx)) return rd.setData("NEED_LOGIN").setErrorMsg("请登录后使用")
    val arr = ctx.bodyAsJsonArray ?: return rd.setErrorMsg("参数错误")
    saveUserStorage(getUserNameSpace(ctx), "httpTTS", arr)
    return rd.setData(arr.size())
}

suspend fun HttpTTSController.deleteMulti(ctx: RoutingContext): ReturnData {
    val rd = ReturnData()
    if (!checkAuth(ctx)) return rd.setData("NEED_LOGIN").setErrorMsg("请登录后使用")
    val names = ctx.bodyAsJson?.getJsonArray("names") ?: JsonArray()
    val set = (0 until names.size()).map { names.getValue(it).toString() }.toSet()
    val ns = getUserNameSpace(ctx)
    val arr = ExtKt.asJsonArray(getUserStorage(ns, "httpTTS")) ?: JsonArray()
    val list = arr.list.filterIndexed { i, _ -> arr.getJsonObject(i).getString("name") !in set }
    saveUserStorage(ns, "httpTTS", JsonArray(list))
    return rd.setData(true)
}

// ---- File extras ----
suspend fun FileController.get(ctx: RoutingContext) = list(ctx)
suspend fun FileController.save(ctx: RoutingContext): ReturnData {
    val rd = ReturnData()
    if (!checkAuth(ctx)) return rd.setData("NEED_LOGIN").setErrorMsg("请登录后使用")
    val path = ctx.bodyAsJson?.getString("path") ?: return rd.setErrorMsg("path 不能为空")
    val content = ctx.bodyAsJson?.getString("content") ?: ""
    val f = File(ExtKt.getWorkDir("storage", "data", getUserNameSpace(ctx), "files", path.trimStart('/')))
    f.parentFile?.mkdirs()
    f.writeText(content)
    return rd.setData(true)
}

suspend fun FileController.upload(ctx: RoutingContext): ReturnData {
    val rd = ReturnData()
    if (!checkAuth(ctx)) return rd.setData("NEED_LOGIN").setErrorMsg("请登录后使用")
    val ns = getUserNameSpace(ctx)
    val dir = File(ExtKt.getWorkDir("storage", "data", ns, "files")).apply { mkdirs() }
    val uploads = ctx.fileUploads()
    if (uploads.isEmpty()) return rd.setErrorMsg("未上传文件")
    val saved = uploads.map { u ->
        val name = u.fileName() ?: "file"
        val dest = File(dir, name.replace(Regex("""[\\/:*?"<>|]"""), "_"))
        File(u.uploadedFileName()).copyTo(dest, overwrite = true)
        mapOf("name" to dest.name, "path" to dest.absolutePath, "size" to dest.length())
    }
    return rd.setData(if (saved.size == 1) saved[0] else saved)
}

suspend fun FileController.importPreview(ctx: RoutingContext): ReturnData =
    ReturnData().setData(mapOf("note" to "use importBookPreview"))

suspend fun FileController.restore(ctx: RoutingContext): ReturnData {
    val rd = ReturnData()
    if (!checkAuth(ctx)) return rd.setData("NEED_LOGIN").setErrorMsg("请登录后使用")
    return rd.setData(true)
}

suspend fun FileController.parse(ctx: RoutingContext): ReturnData =
    ReturnData().setData(emptyMap<String, Any>())

// ---- User extras ----
suspend fun UserController.uploadFile(ctx: RoutingContext): ReturnData {
    val rd = ReturnData()
    if (!checkAuth(ctx)) return rd.setData("NEED_LOGIN").setErrorMsg("请登录后使用")
    val ns = getUserNameSpace(ctx)
    val dir = File(ExtKt.getWorkDir("storage", "data", ns, "assets")).apply { mkdirs() }
    val uploads = ctx.fileUploads()
    if (uploads.isEmpty()) return rd.setErrorMsg("未上传文件")
    val u = uploads.first()
    val dest = File(dir, (u.fileName() ?: "file").replace(Regex("""[\\/:*?"<>|]"""), "_"))
    File(u.uploadedFileName()).copyTo(dest, overwrite = true)
    return rd.setData(mapOf("name" to dest.name, "path" to dest.absolutePath, "size" to dest.length()))
}

suspend fun UserController.deleteFile(ctx: RoutingContext): ReturnData {
    val rd = ReturnData()
    if (!checkAuth(ctx)) return rd.setData("NEED_LOGIN").setErrorMsg("请登录后使用")
    val name = ctx.bodyAsJson?.getString("name") ?: return rd.setErrorMsg("参数错误")
    val base = File(ExtKt.getWorkDir("storage", "data", getUserNameSpace(ctx), "assets")).canonicalFile
    val f = File(base, name).canonicalFile
    if (f.path.startsWith(base.path) && f.isFile) f.delete()
    return rd.setData(true)
}

fun UserController.downloadBackupFile(ctx: RoutingContext) {
    val ns = getUserNameSpace(ctx)
    val dataDir = File(ExtKt.getWorkDir("storage", "data", ns))
    val f = dataDir.listFiles()?.filter { it.name.endsWith(".zip") }?.maxByOrNull { it.lastModified() }
        ?: File(dataDir, "backup.zip")
    if (f.isFile) ctx.response().sendFile(f.absolutePath)
    else ctx.response().setStatusCode(404).end()
}

// ---- License extras ----
suspend fun LicenseController.decryptLicense(ctx: RoutingContext): ReturnData =
    ReturnData().setData(emptyMap<String, Any>())

suspend fun LicenseController.sendCodeToEmail(ctx: RoutingContext): ReturnData =
    ReturnData().setData(true)

suspend fun LicenseController.supplyLicense(ctx: RoutingContext): ReturnData =
    ReturnData().setData(true)
