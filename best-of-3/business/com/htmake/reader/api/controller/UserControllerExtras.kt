/** Business rewrite from reader-pro-3.2.14.jar — phase10. */

package com.htmake.reader.api.controller

import com.htmake.reader.api.ReturnData
import com.htmake.reader.config.UserConfigDefaults
import com.htmake.reader.config.UserConfigKeys
import com.htmake.reader.utils.ExtKt
import io.vertx.core.json.JsonObject
import io.vertx.ext.web.RoutingContext
import java.io.File
import java.util.UUID

/**
 * 用户文件上传 / 备份下载 / 配置辅助。
 */
suspend fun UserController.uploadFile(context: RoutingContext): ReturnData {
    val rd = ReturnData()
    if (!checkAuth(context)) return rd.setData("NEED_LOGIN").setErrorMsg("请登录后使用")
    val ns = getUserNameSpace(context)
    val uploads = context.fileUploads()
    if (uploads.isEmpty()) return rd.setErrorMsg("未上传文件")
    val dir = File(ExtKt.getWorkDir("storage", "data", ns, "assets")).apply { mkdirs() }
    val saved = ArrayList<Map<String, Any?>>()
    for (u in uploads) {
        val name = u.fileName()?.takeIf { it.isNotBlank() } ?: "${UUID.randomUUID()}"
        val safe = name.replace(Regex("""[\\/:*?"<>|]"""), "_")
        val dest = File(dir, safe)
        try {
            // Vert.x uploaded file is already on disk at uploadedFileName
            val tmp = File(u.uploadedFileName())
            if (tmp.isFile) tmp.copyTo(dest, overwrite = true)
            saved += mapOf(
                "name" to safe,
                "path" to dest.absolutePath,
                "size" to dest.length(),
                "url" to "/reader3/assets/$ns/$safe"
            )
        } catch (e: Exception) {
            return rd.setErrorMsg(e.message ?: "上传失败")
        }
    }
    return rd.setData(if (saved.size == 1) saved[0] else saved)
}

suspend fun UserController.deleteFile(context: RoutingContext): ReturnData {
    val rd = ReturnData()
    if (!checkAuth(context)) return rd.setData("NEED_LOGIN").setErrorMsg("请登录后使用")
    val ns = getUserNameSpace(context)
    val name = context.bodyAsJson?.getString("name")
        ?: context.bodyAsJson?.getString("path")
        ?: context.queryParam("name").firstOrNull()
        ?: return rd.setErrorMsg("参数错误")
    val base = File(ExtKt.getWorkDir("storage", "data", ns, "assets")).canonicalFile
    val f = if (name.contains(File.separator) || name.contains('/')) {
        File(name)
    } else {
        File(base, name)
    }.canonicalFile
    if (!f.path.startsWith(base.path)) return rd.setErrorMsg("非法路径")
    if (f.isFile) f.delete()
    return rd.setData(true)
}

fun UserController.downloadBackupFile(context: RoutingContext) {
    val ns = getUserNameSpace(context)
    // prefer latest backup zip under user data
    val dataDir = File(ExtKt.getWorkDir("storage", "data", ns))
    val candidates = dataDir.listFiles()?.filter {
        it.isFile && it.name.endsWith(".zip", true) && it.name.contains("backup", true)
    }.orEmpty()
    val f = candidates.maxByOrNull { it.lastModified() }
        ?: File(dataDir, "backup.zip")
    if (f.isFile) context.response().sendFile(f.absolutePath)
    else context.response().setStatusCode(404).end()
}

/** 读取合并后的用户配置（含 defaults），不走 error 分支。 */
fun UserController.readMergedUserConfig(ns: String): JsonObject {
    val stored = ExtKt.asJsonObject(getUserStorage(ns, "userConfig"))
    return UserConfigDefaults.merge(stored)
}

fun UserController.configInt(ns: String, key: String, default: Int): Int =
    readMergedUserConfig(ns).getInteger(key, default) ?: default

fun UserController.configString(ns: String, key: String, default: String): String =
    readMergedUserConfig(ns).getString(key, default) ?: default
