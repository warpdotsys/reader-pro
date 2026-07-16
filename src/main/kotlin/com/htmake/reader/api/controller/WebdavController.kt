package com.htmake.reader.api.controller

import com.htmake.reader.api.ReturnData
import com.htmake.reader.utils.ExtKt
import io.vertx.core.http.HttpMethod
import io.vertx.ext.web.Router
import io.vertx.ext.web.RoutingContext
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.io.File
import java.util.zip.ZipEntry
import java.util.zip.ZipOutputStream
import kotlin.coroutines.CoroutineContext

class WebdavController(cc: CoroutineContext) : BaseController(cc) {
    fun mount(router: Router, scope: CoroutineScope = this) {
        router.route("/reader3/webdav*").handler { ctx ->
            scope.launch(Dispatchers.IO) {
                try { dispatch(ctx) } catch (e: Exception) {
                    if (!ctx.response().ended()) ctx.response().setStatusCode(500).end(e.message ?: "error")
                }
            }
        }
    }

    private fun home(ns: String) = File(getUserWebdavHome(ns)).canonicalFile.apply { mkdirs() }

    private suspend fun dispatch(ctx: RoutingContext) {
        if (!checkAuthorization(ctx)) {
            ctx.response().setStatusCode(401).putHeader("WWW-Authenticate", "Basic realm=\"webdav\"").end()
            return
        }
        when (ctx.request().method()) {
            HttpMethod.OPTIONS -> ctx.response()
                .putHeader("Allow", "OPTIONS,GET,PUT,DELETE,MKCOL,MOVE,COPY,PROPFIND")
                .putHeader("DAV", "1,2").end()
            HttpMethod.GET -> {
                val ns = getUserNameSpace(ctx)
                val f = WebdavPaths.resolveUnderHome(home(ns), WebdavPaths.pathFromRequest(ctx.request().path() ?: "/"))
                if (f.isFile) ctx.response().sendFile(f.absolutePath) else ctx.response().setStatusCode(404).end()
            }
            HttpMethod.PUT -> {
                val ns = getUserNameSpace(ctx)
                val f = WebdavPaths.resolveUnderHome(home(ns), WebdavPaths.pathFromRequest(ctx.request().path() ?: "/"))
                f.parentFile?.mkdirs()
                val buf = ctx.body
                f.writeBytes(buf?.bytes ?: ByteArray(0))
                ctx.response().setStatusCode(201).end()
            }
            HttpMethod.DELETE -> {
                val ns = getUserNameSpace(ctx)
                val f = WebdavPaths.resolveUnderHome(home(ns), WebdavPaths.pathFromRequest(ctx.request().path() ?: "/"))
                ExtKt.deleteRecursively(f)
                ctx.response().setStatusCode(204).end()
            }
            else -> when ((ctx.request().rawMethod() ?: "").uppercase()) {
                "PROPFIND" -> propfind(ctx)
                "MKCOL" -> {
                    val ns = getUserNameSpace(ctx)
                    val f = WebdavPaths.resolveUnderHome(home(ns), WebdavPaths.pathFromRequest(ctx.request().path() ?: "/"))
                    f.mkdirs(); ctx.response().setStatusCode(201).end()
                }
                "MOVE", "COPY" -> moveCopy(ctx, (ctx.request().rawMethod() ?: "").uppercase() == "MOVE")
                else -> ctx.response().setStatusCode(405).end()
            }
        }
    }

    private fun checkAuthorization(ctx: RoutingContext): Boolean {
        val auth = ctx.request().getHeader("Authorization")
        return !auth.isNullOrBlank() || !ctx.session()?.get<String>("username").isNullOrBlank() || !appConfig.secure
    }

    private fun propfind(ctx: RoutingContext) {
        val ns = getUserNameSpace(ctx)
        val f = WebdavPaths.resolveUnderHome(home(ns), WebdavPaths.pathFromRequest(ctx.request().path() ?: "/"))
        val sb = StringBuilder("""<?xml version="1.0"?><D:multistatus xmlns:D="DAV:">""")
        fun entry(file: File, href: String) {
            sb.append("<D:response><D:href>").append(href).append("</D:href><D:propstat><D:prop>")
            if (file.isDirectory) sb.append("<D:resourcetype><D:collection/></D:resourcetype>")
            else sb.append("<D:resourcetype/><D:getcontentlength>").append(file.length()).append("</D:getcontentlength>")
            sb.append("</D:prop><D:status>HTTP/1.1 200 OK</D:status></D:propstat></D:response>")
        }
        entry(f, ctx.request().path() ?: "/")
        if (f.isDirectory) f.listFiles()?.forEach {
            entry(it, (ctx.request().path() ?: "/").trimEnd('/') + "/" + it.name)
        }
        sb.append("</D:multistatus>")
        ctx.response().setStatusCode(207).putHeader("Content-Type", "application/xml; charset=utf-8").end(sb.toString())
    }

    private fun moveCopy(ctx: RoutingContext, move: Boolean) {
        val ns = getUserNameSpace(ctx)
        val src = WebdavPaths.resolveUnderHome(home(ns), WebdavPaths.pathFromRequest(ctx.request().path() ?: "/"))
        val destHeader = ctx.request().getHeader("Destination") ?: run {
            ctx.response().setStatusCode(400).end(); return
        }
        val rel = WebdavPaths.destinationToRelativePath(destHeader) ?: run {
            ctx.response().setStatusCode(400).end(); return
        }
        val dest = WebdavPaths.resolveUnderHome(home(ns), rel)
        val overwrite = ctx.request().getHeader("Overwrite")?.uppercase() != "F"
        if (dest.exists() && !overwrite) {
            ctx.response().setStatusCode(412).end(); return
        }
        dest.parentFile?.mkdirs()
        if (move) src.renameTo(dest) else src.copyRecursively(dest, overwrite = true)
        ctx.response().setStatusCode(201).end()
    }

    suspend fun backupToWebdav(ctx: RoutingContext): ReturnData {
        val rd = ReturnData()
        if (!checkAuth(ctx)) return rd.setData("NEED_LOGIN").setErrorMsg("请登录后使用")
        val ns = getUserNameSpace(ctx)
        val zip = File(home(ns), "backup-${System.currentTimeMillis()}.zip")
        val dataDir = File(ExtKt.getWorkDir("storage", "data", ns))
        val names = arrayOf("bookshelf.json", "bookSource.json", "rssSource.json", "replaceRule.json", "bookmark.json", "bookGroup.json", "userConfig.json")
        ZipOutputStream(zip.outputStream()).use { zos ->
            for (name in names) {
                val f = File(dataDir, name)
                if (!f.isFile) continue
                zos.putNextEntry(ZipEntry(name))
                f.inputStream().use { it.copyTo(zos) }
                zos.closeEntry()
            }
        }
        return rd.setData(mapOf("path" to zip.absolutePath, "size" to zip.length()))
    }
}
