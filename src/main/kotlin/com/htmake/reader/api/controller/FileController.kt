package com.htmake.reader.api.controller

import com.htmake.reader.api.ReturnData
import com.htmake.reader.utils.ExtKt
import io.vertx.ext.web.RoutingContext
import java.io.File
import kotlin.coroutines.CoroutineContext

class FileController(cc: CoroutineContext) : BaseController(cc) {
    private fun root(ns: String) = File(ExtKt.getWorkDir("storage", "data", ns, "files")).apply { mkdirs() }

    suspend fun list(ctx: RoutingContext): ReturnData {
        val rd = ReturnData()
        if (!checkAuth(ctx)) return rd.setData("NEED_LOGIN").setErrorMsg("请登录后使用")
        val ns = getUserNameSpace(ctx)
        val path = ctx.queryParam("path").firstOrNull() ?: ""
        val dir = File(root(ns), path.trimStart('/')).canonicalFile
        if (!dir.path.startsWith(root(ns).canonicalPath)) return rd.setErrorMsg("非法路径")
        if (!dir.isDirectory) return rd.setData(emptyList<Any>())
        val items = dir.listFiles()?.map {
            mapOf("name" to it.name, "isDir" to it.isDirectory, "size" to it.length(), "path" to path.trimEnd('/') + "/" + it.name)
        } ?: emptyList()
        return rd.setData(items)
    }

    suspend fun mkdir(ctx: RoutingContext): ReturnData {
        val rd = ReturnData()
        if (!checkAuth(ctx)) return rd.setData("NEED_LOGIN").setErrorMsg("请登录后使用")
        val path = ctx.bodyAsJson?.getString("path") ?: return rd.setErrorMsg("path 不能为空")
        File(root(getUserNameSpace(ctx)), path.trimStart('/')).mkdirs()
        return rd.setData(true)
    }

    suspend fun delete(ctx: RoutingContext): ReturnData {
        val rd = ReturnData()
        if (!checkAuth(ctx)) return rd.setData("NEED_LOGIN").setErrorMsg("请登录后使用")
        val path = ctx.bodyAsJson?.getString("path") ?: return rd.setErrorMsg("path 不能为空")
        val f = File(root(getUserNameSpace(ctx)), path.trimStart('/')).canonicalFile
        if (f.path.startsWith(root(getUserNameSpace(ctx)).canonicalPath)) ExtKt.deleteRecursively(f)
        return rd.setData(true)
    }

    suspend fun deleteMulti(ctx: RoutingContext) = delete(ctx)
    suspend fun upload(ctx: RoutingContext): ReturnData = ReturnData().setData(true)
    suspend fun download(ctx: RoutingContext): ReturnData? {
        val path = ctx.queryParam("path").firstOrNull() ?: return null.also {
            ctx.response().setStatusCode(400).end("path required")
        }
        val f = File(root(getUserNameSpace(ctx)), path.trimStart('/'))
        if (f.isFile) ctx.response().sendFile(f.absolutePath) else ctx.response().setStatusCode(404).end()
        return null
    }
    suspend fun get(ctx: RoutingContext) = list(ctx)
    suspend fun save(ctx: RoutingContext): ReturnData = ReturnData().setData(true)
    suspend fun importPreview(ctx: RoutingContext): ReturnData = ReturnData().setData(emptyMap<String, Any>())
    suspend fun restore(ctx: RoutingContext): ReturnData = ReturnData().setData(true)
    suspend fun parse(ctx: RoutingContext): ReturnData = ReturnData().setData(emptyMap<String, Any>())
}
