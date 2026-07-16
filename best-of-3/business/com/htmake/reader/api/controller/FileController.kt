/**
 * Business-oriented rewrite from reader-pro-3.2.14.jar reverse engineering.
 * Source: CFR/Vineflower + manual semantic cleanup. For audit/readability.
 */

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
