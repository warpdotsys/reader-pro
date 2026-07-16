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
                .putHeader("Allow", "OPTIONS,GET,HEAD,PUT,DELETE,MKCOL,MOVE,COPY,PROPFIND,LOCK,UNLOCK")
                .putHeader("DAV", "1,2")
                .putHeader("MS-Author-Via", "DAV")
                .end()
            HttpMethod.HEAD -> {
                val ns = getUserNameSpace(ctx)
                val f = WebdavPaths.resolveUnderHome(home(ns), WebdavPaths.pathFromRequest(ctx.request().path() ?: "/"))
                if (f.isFile) {
                    ctx.response()
                        .putHeader("Content-Length", f.length().toString())
                        .putHeader("Last-Modified", java.text.SimpleDateFormat("EEE, dd MMM yyyy HH:mm:ss z", java.util.Locale.US).apply {
                            timeZone = java.util.TimeZone.getTimeZone("GMT")
                        }.format(java.util.Date(f.lastModified())))
                        .setStatusCode(200).end()
                } else ctx.response().setStatusCode(404).end()
            }
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
                "LOCK" -> lock(ctx)
                "UNLOCK" -> ctx.response().setStatusCode(204).end()
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
        val df = java.text.SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'", java.util.Locale.US).apply {
            timeZone = java.util.TimeZone.getTimeZone("UTC")
        }
        val sb = StringBuilder("""<?xml version="1.0" encoding="utf-8"?><D:multistatus xmlns:D="DAV:">""")
        fun entry(file: File, href: String) {
            val safeHref = href.replace("&", "&amp;")
            sb.append("<D:response><D:href>").append(safeHref).append("</D:href><D:propstat><D:prop>")
            sb.append("<D:displayname>").append(file.name.replace("&", "&amp;")).append("</D:displayname>")
            sb.append("<D:getlastmodified>").append(df.format(java.util.Date(file.lastModified()))).append("</D:getlastmodified>")
            if (file.isDirectory) sb.append("<D:resourcetype><D:collection/></D:resourcetype>")
            else {
                sb.append("<D:resourcetype/>")
                sb.append("<D:getcontentlength>").append(file.length()).append("</D:getcontentlength>")
            }
            sb.append("</D:prop><D:status>HTTP/1.1 200 OK</D:status></D:propstat></D:response>")
        }
        entry(f, ctx.request().path() ?: "/")
        if (f.isDirectory) f.listFiles()?.forEach {
            entry(it, (ctx.request().path() ?: "/").trimEnd('/') + "/" + it.name)
        }
        sb.append("</D:multistatus>")
        ctx.response().setStatusCode(207).putHeader("Content-Type", "application/xml; charset=utf-8").end(sb.toString())
    }

    /** Minimal LOCK for clients that expect DAV:2 (no real exclusive locking). */
    private fun lock(ctx: RoutingContext) {
        val token = "opaquelocktoken:" + java.util.UUID.randomUUID()
        val body = """
            <?xml version="1.0" encoding="utf-8"?>
            <D:prop xmlns:D="DAV:"><D:lockdiscovery><D:activelock>
              <D:locktype><D:write/></D:locktype>
              <D:lockscope><D:exclusive/></D:lockscope>
              <D:depth>Infinity</D:depth>
              <D:timeout>Second-3600</D:timeout>
              <D:locktoken><D:href>$token</D:href></D:locktoken>
            </D:activelock></D:lockdiscovery></D:prop>
        """.trimIndent()
        ctx.response()
            .setStatusCode(200)
            .putHeader("Lock-Token", "<$token>")
            .putHeader("Content-Type", "application/xml; charset=utf-8")
            .end(body)
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
        return rd.setData(mapOf("path" to zip.absolutePath, "size" to zip.length(), "name" to zip.name))
    }

    /**
     * Restore latest (or named) backup zip from user WebDAV home into data/{ns}/.
     * Body/query: fileName optional.
     */
    suspend fun restoreFromWebdav(ctx: RoutingContext): ReturnData {
        val rd = ReturnData()
        if (!checkAuth(ctx)) return rd.setData("NEED_LOGIN").setErrorMsg("请登录后使用")
        val ns = getUserNameSpace(ctx)
        val homeDir = home(ns)
        val name = ctx.bodyAsJson?.getString("fileName")
            ?: ctx.queryParam("fileName").firstOrNull()
        val zip = if (!name.isNullOrBlank()) {
            File(homeDir, name)
        } else {
            homeDir.listFiles { f -> f.isFile && f.name.startsWith("backup-") && f.name.endsWith(".zip") }
                ?.maxByOrNull { it.lastModified() }
        } ?: return rd.setErrorMsg("没有备份文件")
        if (!zip.isFile) return rd.setErrorMsg("备份不存在: ${zip.name}")
        val dataDir = File(ExtKt.getWorkDir("storage", "data", ns)).apply { mkdirs() }
        var count = 0
        java.util.zip.ZipInputStream(zip.inputStream()).use { zis ->
            var entry = zis.nextEntry
            while (entry != null) {
                if (!entry.isDirectory) {
                    val safe = File(entry.name).name // flatten path
                    if (safe.endsWith(".json")) {
                        val out = File(dataDir, safe)
                        out.outputStream().use { zis.copyTo(it) }
                        count++
                    }
                }
                zis.closeEntry()
                entry = zis.nextEntry
            }
        }
        return rd.setData(mapOf("restored" to count, "from" to zip.name))
    }

    /** List backup-*.zip under webdav home. */
    suspend fun listWebdavBackups(ctx: RoutingContext): ReturnData {
        val rd = ReturnData()
        if (!checkAuth(ctx)) return rd.setData("NEED_LOGIN").setErrorMsg("请登录后使用")
        val ns = getUserNameSpace(ctx)
        val list = home(ns).listFiles { f -> f.isFile && f.name.endsWith(".zip") }
            ?.sortedByDescending { it.lastModified() }
            ?.map { mapOf("name" to it.name, "size" to it.length(), "mtime" to it.lastModified()) }
            ?: emptyList()
        return rd.setData(list)
    }
}

