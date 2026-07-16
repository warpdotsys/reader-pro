/** Business rewrite from reader-pro-3.2.14.jar — phase7. */

package com.htmake.reader.api.controller

import com.htmake.reader.api.ReturnData
import com.htmake.reader.utils.ExtKt
import io.vertx.core.http.HttpMethod
import io.vertx.ext.web.Router
import io.vertx.ext.web.RoutingContext
import java.io.File
import java.util.UUID
import java.util.zip.ZipEntry
import java.util.zip.ZipOutputStream
import kotlin.coroutines.CoroutineContext
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

/**
 * WebDAV /reader3/webdav/* — Destination URL parsed like jar (URL.path + strip prefix).
 */
class WebdavController(coroutineContext: CoroutineContext) : BaseController(coroutineContext) {

    fun mount(router: Router, scope: CoroutineScope = this) {
        router.route("/reader3/webdav*").handler { ctx ->
            scope.launch(Dispatchers.IO) {
                try {
                    dispatch(ctx)
                } catch (e: Exception) {
                    if (!ctx.response().ended()) {
                        ctx.response().setStatusCode(500).end(e.message ?: "error")
                    }
                }
            }
        }
    }

    private suspend fun dispatch(ctx: RoutingContext) {
        when (ctx.request().method()) {
            HttpMethod.OPTIONS -> ctx.response()
                .putHeader("Allow", "OPTIONS,GET,PUT,DELETE,MKCOL,MOVE,COPY,PROPFIND,LOCK,UNLOCK")
                .putHeader("DAV", "1,2")
                .end()
            HttpMethod.GET -> webdavDownload(ctx)
            HttpMethod.PUT -> webdavUpload(ctx)
            HttpMethod.DELETE -> webdavDelete(ctx)
            HttpMethod.MOVE -> webdavMove(ctx)
            HttpMethod.COPY -> webdavCopy(ctx)
            else -> when ((ctx.request().rawMethod() ?: ctx.request().method().name()).uppercase()) {
                "PROPFIND" -> webdavList(ctx)
                "MKCOL" -> webdavMkdir(ctx)
                "LOCK" -> webdavLock(ctx)
                "UNLOCK" -> webdavUnLock(ctx)
                else -> ctx.response().setStatusCode(405).end()
            }
        }
    }

    fun checkAuthorization(context: RoutingContext): Boolean {
        val auth = context.request().getHeader("Authorization")
        return !auth.isNullOrBlank() ||
            !context.session().get<String>("username").isNullOrBlank() ||
            !appConfig.secure
    }

    private fun home(ns: String) = File(getUserWebdavHome(ns)).canonicalFile.apply { mkdirs() }

    private fun resolvePath(context: RoutingContext, ns: String): File {
        val rel = WebdavPaths.pathFromRequest(context.request().path() ?: "/")
        return WebdavPaths.resolveUnderHome(home(ns), rel)
    }

    private fun resolveDestination(context: RoutingContext, ns: String): File? {
        val destHeader = context.request().getHeader("Destination") ?: return null
        val rel = WebdavPaths.destinationToRelativePath(destHeader) ?: return null
        return WebdavPaths.resolveUnderHome(home(ns), rel)
    }

    suspend fun webdavList(context: RoutingContext) {
        if (!checkAuthorization(context)) {
            context.response().setStatusCode(401)
                .putHeader("WWW-Authenticate", "Basic realm=\"webdav\"").end()
            return
        }
        val ns = getUserNameSpace(context)
        val h = home(ns)
        val file = resolvePath(context, ns)
        if (!file.exists()) {
            context.response().setStatusCode(404).end(); return
        }
        val sb = StringBuilder("""<?xml version="1.0" encoding="utf-8"?><D:multistatus xmlns:D="DAV:">""")
        fun emit(f: File) {
            val href = "/reader3/webdav/" + f.relativeTo(h).invariantSeparatorsPath
            sb.append("<D:response><D:href>").append(xmlEsc(href)).append("</D:href><D:propstat><D:prop>")
            sb.append("<D:displayname>").append(xmlEsc(f.name.ifEmpty { "/" })).append("</D:displayname>")
            if (f.isFile) sb.append("<D:getcontentlength>").append(f.length()).append("</D:getcontentlength>")
            sb.append("<D:resourcetype>")
            if (f.isDirectory) sb.append("<D:collection/>")
            sb.append("</D:resourcetype></D:prop><D:status>HTTP/1.1 200 OK</D:status></D:propstat></D:response>")
        }
        emit(file)
        if (file.isDirectory) file.listFiles()?.forEach { emit(it) }
        sb.append("</D:multistatus>")
        context.response().setStatusCode(207)
            .putHeader("Content-Type", "application/xml; charset=utf-8")
            .end(sb.toString())
    }

    suspend fun webdavMkdir(context: RoutingContext) {
        val file = resolvePath(context, getUserNameSpace(context))
        if (file.exists()) {
            context.response().setStatusCode(405).end(); return
        }
        file.mkdirs()
        context.response().setStatusCode(201).end()
    }

    suspend fun webdavUpload(context: RoutingContext) {
        val file = resolvePath(context, getUserNameSpace(context))
        file.parentFile?.mkdirs()
        file.writeBytes(context.body().bytes)
        context.response().setStatusCode(201).end()
    }

    suspend fun webdavDownload(context: RoutingContext) {
        val file = resolvePath(context, getUserNameSpace(context))
        if (!file.isFile) {
            context.response().setStatusCode(404).end(); return
        }
        context.response().sendFile(file.absolutePath)
    }

    suspend fun webdavDelete(context: RoutingContext) {
        val file = resolvePath(context, getUserNameSpace(context))
        if (!file.exists()) {
            context.response().setStatusCode(404).end(); return
        }
        ExtKt.deleteRecursively(file)
        context.response().setStatusCode(204).end()
    }

    /**
     * Destination: full URL → path → strip /reader3/webdav → under home.
     * Overwrite: header "T" required if dest exists; else 412.
     */
    suspend fun webdavMove(context: RoutingContext) {
        val ns = getUserNameSpace(context)
        val src = resolvePath(context, ns)
        if (!src.exists()) {
            context.response().setStatusCode(412).end(); return
        }
        val dest = resolveDestination(context, ns)
        if (dest == null) {
            context.response().setStatusCode(400).end(); return
        }
        val overwrite = context.request().getHeader("Overwrite")
        if (dest.exists()) {
            if (overwrite.isNullOrEmpty() || overwrite == "F") {
                context.response().setStatusCode(412).end(); return
            }
            ExtKt.deleteRecursively(dest)
        }
        dest.parentFile?.mkdirs()
        if (!src.renameTo(dest)) {
            // cross-device fallback
            if (src.isDirectory) src.copyRecursively(dest, overwrite = true).also { ExtKt.deleteRecursively(src) }
            else {
                src.copyTo(dest, overwrite = true)
                src.delete()
            }
        }
        context.response().setStatusCode(201).end()
    }

    suspend fun webdavCopy(context: RoutingContext) {
        val ns = getUserNameSpace(context)
        val src = resolvePath(context, ns)
        if (!src.exists()) {
            context.response().setStatusCode(412).end(); return
        }
        val dest = resolveDestination(context, ns)
        if (dest == null) {
            context.response().setStatusCode(400).end(); return
        }
        val overwrite = context.request().getHeader("Overwrite")
        if (dest.exists()) {
            if (overwrite.isNullOrEmpty() || overwrite == "F") {
                context.response().setStatusCode(412).end(); return
            }
            ExtKt.deleteRecursively(dest)
        }
        dest.parentFile?.mkdirs()
        if (src.isDirectory) src.copyRecursively(dest, overwrite = true)
        else src.copyTo(dest, overwrite = true)
        context.response().setStatusCode(201).end()
    }

    suspend fun webdavLock(context: RoutingContext) {
        val token = "urn:uuid:${UUID.randomUUID()}"
        val href = context.request().absoluteURI()
        val xml = """<?xml version="1.0" encoding="utf-8"?>
        <D:prop xmlns:D="DAV:"><D:lockdiscovery><D:activelock>
        <D:locktype><write/></D:locktype><D:lockscope><exclusive/></D:lockscope>
        <D:locktoken><D:href>$token</D:href></D:locktoken>
        <D:lockroot><D:href>$href</D:href></D:lockroot>
        <D:depth>infinity</D:depth><D:timeout>Second-3600</D:timeout>
        </D:activelock></D:lockdiscovery></D:prop>"""
        context.response().setStatusCode(200)
            .putHeader("Lock-Token", "<$token>")
            .putHeader("Content-Type", "application/xml; charset=utf-8")
            .end(xml)
    }

    suspend fun webdavUnLock(context: RoutingContext) {
        context.response().setStatusCode(204).end()
    }

    suspend fun backupToWebdav(context: RoutingContext): ReturnData {
        val rd = ReturnData()
        if (!checkAuth(context)) return rd.setData("NEED_LOGIN").setErrorMsg("请登录后使用")
        val ns = getUserNameSpace(context)
        val u = loadUserMap()[ns]
        if (u != null && u["enableWebdav"] == false) return rd.setErrorMsg("未开启webdav功能")
        val homeDir = home(ns)
        val zip = File(homeDir, "backup-${System.currentTimeMillis()}.zip")
        val dataDir = File(ExtKt.getWorkDir("storage", "data", ns))
        val names = arrayOf(
            "bookshelf.json", "bookSource.json", "rssSource.json",
            "replaceRule.json", "bookmark.json", "bookGroup.json", "userConfig.json"
        )
        return try {
            ZipOutputStream(zip.outputStream()).use { zos ->
                for (name in names) {
                    val f = File(dataDir, name)
                    if (!f.isFile) continue
                    zos.putNextEntry(ZipEntry(name))
                    f.inputStream().use { it.copyTo(zos) }
                    zos.closeEntry()
                }
            }
            rd.setData(mapOf("path" to zip.absolutePath, "size" to zip.length()))
        } catch (e: Exception) {
            rd.setErrorMsg(e.message ?: "backup failed")
        }
    }

    private fun xmlEsc(s: String) =
        s.replace("&", "&amp;").replace("<", "&lt;").replace(">", "&gt;").replace("\"", "&quot;")
}
