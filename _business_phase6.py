# -*- coding: utf-8 -*-
"""Phase 6: WebDAV ops, Debugger SSE, replace rules on content, INDEX.md."""
from pathlib import Path
import os

BIZ = Path(r"C:\Users\chong\reader-pro-3.2.14-reverse\best-of-3\business")
SRC = Path(r"C:\Users\chong\reader-pro-3.2.14-reverse\best-of-3\src")
H = "/** Business rewrite from reader-pro-3.2.14.jar — phase6. */\n\n"

def w(rel, c):
    p = BIZ / rel.replace("/", os.sep)
    p.parent.mkdir(parents=True, exist_ok=True)
    p.write_text(H + c.lstrip("\n"), encoding="utf-8", newline="\n")
    print("wrote", rel, p.stat().st_size)

# ---------------------------------------------------------------------------
# WebDAV full
# ---------------------------------------------------------------------------
w("com/htmake/reader/api/controller/WebdavController.kt", r'''
package com.htmake.reader.api.controller

import com.htmake.reader.api.ReturnData
import com.htmake.reader.utils.ExtKt
import io.legado.app.utils.ZipUtils
import io.vertx.core.http.HttpMethod
import io.vertx.ext.web.RoutingContext
import java.io.File
import java.net.URLDecoder
import java.util.UUID
import kotlin.coroutines.CoroutineContext

/**
 * WebDAV under /reader3/webdav/* (Basic auth via accessToken/user session).
 * Methods: LIST(PROPFIND), MKCOL, PUT, GET, DELETE, MOVE, COPY, LOCK, UNLOCK + backupToWebdav.
 */
class WebdavController(coroutineContext: CoroutineContext) : BaseController(coroutineContext) {

    fun mount(router: io.vertx.ext.web.Router) {
        router.route("/reader3/webdav*").handler { ctx ->
            // dispatch by method in coroutine from RestVerticle layer ideally
            launchHandle(ctx)
        }
    }

    private fun launchHandle(ctx: RoutingContext) {
        // synchronous dispatch entry; real server uses coroutineHandler
        kotlinx.coroutines.GlobalScope.launch(kotlinx.coroutines.Dispatchers.IO) {
            try {
                when (ctx.request().method()) {
                    HttpMethod.OPTIONS -> {
                        ctx.response()
                            .putHeader("Allow", "OPTIONS,GET,PUT,DELETE,MKCOL,MOVE,COPY,PROPFIND,LOCK,UNLOCK")
                            .putHeader("DAV", "1,2")
                            .end()
                    }
                    HttpMethod.GET -> webdavDownload(ctx)
                    HttpMethod.PUT -> webdavUpload(ctx)
                    HttpMethod.DELETE -> webdavDelete(ctx)
                    HttpMethod.MOVE -> webdavMove(ctx)
                    HttpMethod.COPY -> webdavCopy(ctx)
                    else -> {
                        val m = ctx.request().method().name()
                        when (m) {
                            "PROPFIND", "propfind" -> webdavList(ctx)
                            "MKCOL", "mkcol" -> webdavMkdir(ctx)
                            "LOCK", "lock" -> webdavLock(ctx)
                            "UNLOCK", "unlock" -> webdavUnLock(ctx)
                            else -> ctx.response().setStatusCode(405).end()
                        }
                    }
                }
            } catch (e: Exception) {
                if (!ctx.response().ended()) ctx.response().setStatusCode(500).end(e.message ?: "error")
            }
        }
    }

    fun checkAuthorization(context: RoutingContext): Boolean {
        // Basic auth or session; reuses checkAuth semantics
        val auth = context.request().getHeader("Authorization") ?: ""
        if (auth.startsWith("Basic ", ignoreCase = true)) {
            // decode user:pass → login check against user map (simplified)
            return true
        }
        return true // session path handled by outer middleware
    }

    private fun resolvePath(context: RoutingContext, ns: String): File {
        var path = context.request().path() ?: "/"
        path = path.replace("/reader3/webdav", "", ignoreCase = true)
        path = URLDecoder.decode(path, "UTF-8")
        if (!path.startsWith("/")) path = "/$path"
        val home = File(getUserWebdavHome(ns)).canonicalFile
        val target = File(home, path.trimStart('/')).canonicalFile
        if (!target.path.startsWith(home.path)) error("非法路径")
        return target
    }

    suspend fun webdavList(context: RoutingContext) {
        if (!checkAuthorization(context)) {
            context.response().setStatusCode(401).putHeader("WWW-Authenticate", "Basic realm=\"webdav\"").end()
            return
        }
        val ns = getUserNameSpace(context)
        val file = resolvePath(context, ns)
        val home = File(getUserWebdavHome(ns)).canonicalFile
        val sb = StringBuilder()
        sb.append("""<?xml version="1.0" encoding="utf-8"?><D:multistatus xmlns:D="DAV:">""")
        fun prop(f: File) {
            val href = "/reader3/webdav/" + f.relativeTo(home).invariantSeparatorsPath
            val isDir = f.isDirectory
            sb.append("<D:response><D:href>").append(href).append("</D:href><D:propstat><D:prop>")
            sb.append("<D:displayname>").append(f.name).append("</D:displayname>")
            sb.append("<D:getlastmodified>").append(java.util.Date(f.lastModified())).append("</D:getlastmodified>")
            if (!isDir) sb.append("<D:getcontentlength>").append(f.length()).append("</D:getcontentlength>")
            sb.append("<D:resourcetype>")
            if (isDir) sb.append("<D:collection/>")
            sb.append("</D:resourcetype></D:prop><D:status>HTTP/1.1 200 OK</D:status></D:propstat></D:response>")
        }
        if (!file.exists()) {
            context.response().setStatusCode(404).end()
            return
        }
        prop(file)
        if (file.isDirectory) file.listFiles()?.forEach { prop(it) }
        sb.append("</D:multistatus>")
        context.response()
            .setStatusCode(207)
            .putHeader("Content-Type", "application/xml; charset=utf-8")
            .end(sb.toString())
    }

    suspend fun webdavMkdir(context: RoutingContext) {
        val ns = getUserNameSpace(context)
        val file = resolvePath(context, ns)
        if (file.exists()) {
            context.response().setStatusCode(405).end()
            return
        }
        file.mkdirs()
        context.response().setStatusCode(201).end()
    }

    suspend fun webdavUpload(context: RoutingContext) {
        val ns = getUserNameSpace(context)
        val file = resolvePath(context, ns)
        file.parentFile?.mkdirs()
        // body as buffer
        val buf = context.body().bytes
        file.writeBytes(buf)
        context.response().setStatusCode(201).end()
    }

    suspend fun webdavDownload(context: RoutingContext) {
        val ns = getUserNameSpace(context)
        val file = resolvePath(context, ns)
        if (!file.isFile) {
            context.response().setStatusCode(404).end()
            return
        }
        context.response().sendFile(file.absolutePath)
    }

    suspend fun webdavDelete(context: RoutingContext) {
        val ns = getUserNameSpace(context)
        val file = resolvePath(context, ns)
        if (!file.exists()) {
            context.response().setStatusCode(404).end()
            return
        }
        ExtKt.deleteRecursively(file)
        context.response().setStatusCode(204).end()
    }

    suspend fun webdavMove(context: RoutingContext) {
        val ns = getUserNameSpace(context)
        val src = resolvePath(context, ns)
        var destHeader = context.request().getHeader("Destination") ?: run {
            context.response().setStatusCode(400).end(); return
        }
        destHeader = URLDecoder.decode(destHeader, "UTF-8")
            .replace("/reader3/webdav/", "/", ignoreCase = true)
        val home = File(getUserWebdavHome(ns)).canonicalFile
        val dest = File(home, destHeader.trimStart('/').removePrefix(context.request().host() ?: "")
            .substringAfter("/reader3/webdav/", destHeader.trimStart('/'))).canonicalFile
        // simplified dest resolution
        val destFile = File(home, File(destHeader).name)
        if (context.request().getHeader("Overwrite") == "T" && destFile.exists()) {
            ExtKt.deleteRecursively(destFile)
        }
        src.renameTo(destFile)
        context.response().setStatusCode(201).end()
    }

    suspend fun webdavCopy(context: RoutingContext) {
        val ns = getUserNameSpace(context)
        val src = resolvePath(context, ns)
        val destName = context.request().getHeader("Destination")?.substringAfterLast('/') ?: "copy"
        val dest = File(File(getUserWebdavHome(ns)), destName)
        if (src.isDirectory) src.copyRecursively(dest, overwrite = true)
        else src.copyTo(dest, overwrite = true)
        context.response().setStatusCode(201).end()
    }

    suspend fun webdavLock(context: RoutingContext) {
        val token = "urn:uuid:" + UUID.randomUUID()
        val href = context.request().absoluteURI()
        val xml = """<?xml version="1.0" encoding="utf-8"?>
        <D:prop xmlns:D="DAV:">
            <D:lockdiscovery><D:activelock>
                <D:locktype><write/></D:locktype>
                <D:lockscope><exclusive/></D:lockscope>
                <D:locktoken><D:href>$token</D:href></D:locktoken>
                <D:lockroot><D:href>$href</D:href></D:lockroot>
                <D:depth>infinity</D:depth>
                <D:timeout>Second-3600</D:timeout>
            </D:activelock></D:lockdiscovery>
        </D:prop>"""
        context.response()
            .setStatusCode(200)
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
        val users = loadUserMap()
        val u = users[ns]
        if (u != null && u["enableWebdav"] == false) {
            return rd.setErrorMsg("未开启webdav功能")
        }
        val home = File(getUserWebdavHome(ns)).apply { mkdirs() }
        val zip = File(home, "backup-${System.currentTimeMillis()}.zip")
        // zip key user json files
        val dataDir = File(ExtKt.getWorkDir("storage", "data", ns))
        val names = arrayOf(
            "bookshelf.json", "bookSource.json", "rssSource.json",
            "replaceRule.json", "bookmark.json", "bookGroup.json", "userConfig.json"
        )
        try {
            java.util.zip.ZipOutputStream(zip.outputStream()).use { zos ->
                for (name in names) {
                    val f = File(dataDir, name)
                    if (!f.isFile) continue
                    zos.putNextEntry(java.util.zip.ZipEntry(name))
                    f.inputStream().use { it.copyTo(zos) }
                    zos.closeEntry()
                }
            }
            return rd.setData(mapOf("path" to zip.absolutePath, "size" to zip.length()))
        } catch (e: Exception) {
            return rd.setErrorMsg(e.message ?: "backup failed")
        }
    }
}

// need GlobalScope import fix - use proper coroutine
private fun kotlinx.coroutines.GlobalScope.launch(
    context: kotlin.coroutines.CoroutineContext,
    block: suspend kotlinx.coroutines.CoroutineScope.() -> Unit
) = kotlinx.coroutines.GlobalScope.launch(context, block = block)
''')

# Fix Webdav - GlobalScope hack is ugly. Cleaner rewrite without GlobalScope hack at bottom.
w("com/htmake/reader/api/controller/WebdavController.kt", r'''
package com.htmake.reader.api.controller

import com.htmake.reader.api.ReturnData
import com.htmake.reader.utils.ExtKt
import io.vertx.core.http.HttpMethod
import io.vertx.ext.web.Router
import io.vertx.ext.web.RoutingContext
import java.io.File
import java.net.URLDecoder
import java.util.UUID
import java.util.zip.ZipEntry
import java.util.zip.ZipOutputStream
import kotlin.coroutines.CoroutineContext
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

/**
 * WebDAV under /reader3/webdav/* .
 * PROPFIND/MKCOL/PUT/GET/DELETE/MOVE/COPY/LOCK/UNLOCK + backupToWebdav.
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
            else -> when (ctx.request().rawMethod()?.uppercase() ?: ctx.request().method().name()) {
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
        return !auth.isNullOrBlank() || !context.session().get<String>("username").isNullOrBlank() || !appConfig.secure
    }

    private fun resolvePath(context: RoutingContext, ns: String): File {
        var path = context.request().path() ?: "/"
        path = path.replace(Regex("/reader3/webdav", RegexOption.IGNORE_CASE), "")
        path = URLDecoder.decode(path, "UTF-8")
        val home = File(getUserWebdavHome(ns)).canonicalFile.apply { mkdirs() }
        val target = File(home, path.trimStart('/')).canonicalFile
        require(target.path.startsWith(home.path)) { "非法路径" }
        return target
    }

    suspend fun webdavList(context: RoutingContext) {
        if (!checkAuthorization(context)) {
            context.response().setStatusCode(401)
                .putHeader("WWW-Authenticate", "Basic realm=\"webdav\"").end()
            return
        }
        val ns = getUserNameSpace(context)
        val file = resolvePath(context, ns)
        val home = File(getUserWebdavHome(ns)).canonicalFile
        if (!file.exists()) {
            context.response().setStatusCode(404).end(); return
        }
        val sb = StringBuilder("""<?xml version="1.0" encoding="utf-8"?><D:multistatus xmlns:D="DAV:">""")
        fun emit(f: File) {
            val rel = f.relativeTo(home).invariantSeparatorsPath
            val href = "/reader3/webdav/" + rel
            sb.append("<D:response><D:href>").append(xmlEsc(href)).append("</D:href><D:propstat><D:prop>")
            sb.append("<D:displayname>").append(xmlEsc(f.name)).append("</D:displayname>")
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

    suspend fun webdavMove(context: RoutingContext) {
        val ns = getUserNameSpace(context)
        val src = resolvePath(context, ns)
        val destName = context.request().getHeader("Destination")?.substringAfterLast('/') ?: "moved"
        val dest = File(getUserWebdavHome(ns), destName)
        if (context.request().getHeader("Overwrite") == "T" && dest.exists()) {
            ExtKt.deleteRecursively(dest)
        }
        src.renameTo(dest)
        context.response().setStatusCode(201).end()
    }

    suspend fun webdavCopy(context: RoutingContext) {
        val ns = getUserNameSpace(context)
        val src = resolvePath(context, ns)
        val destName = context.request().getHeader("Destination")?.substringAfterLast('/') ?: "copy"
        val dest = File(getUserWebdavHome(ns), destName)
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
        val home = File(getUserWebdavHome(ns)).apply { mkdirs() }
        val zip = File(home, "backup-${System.currentTimeMillis()}.zip")
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

    private fun xmlEsc(s: String): String =
        s.replace("&", "&amp;").replace("<", "&lt;").replace(">", "&gt;").replace("\"", "&quot;")
}
''')

# ---------------------------------------------------------------------------
# Debugger + bookSourceDebugSSE
# ---------------------------------------------------------------------------
w("io/legado/app/model/Debugger.kt", r'''
package io.legado.app.model

import io.legado.app.data.entities.Book
import io.legado.app.model.webBook.WebBook
import io.legado.app.utils.HtmlFormatter
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

/**
 * Book source debug runner: search → info → toc → content, streaming log lines.
 */
class Debugger(val logMsg: (String) -> Unit) : DebugLog {
    private val debugTimeFormat = SimpleDateFormat("[mm:ss.SSS]", Locale.getDefault())
    private var startTime = System.currentTimeMillis()

    override fun log(sourceUrl: String?, msg: String?) {
        log(sourceUrl, msg, false)
    }

    fun log(message: String) {
        val t = debugTimeFormat.format(Date(System.currentTimeMillis() - startTime))
        logMsg("$t $message")
    }

    override fun log(sourceUrl: String?, msg: String?, isHtml: Boolean) {
        if (sourceUrl == null || msg == null) return
        var printMsg = msg
        if (isHtml) printMsg = HtmlFormatter.formatKeepImg(msg)
        val t = debugTimeFormat.format(Date(System.currentTimeMillis() - startTime))
        logMsg("$t $printMsg")
    }

    /**
     * @param key search keyword, or absolute book URL to jump to info
     */
    suspend fun startDebug(webBook: WebBook, key: String) {
        startTime = System.currentTimeMillis()
        webBook.debugLogger = this
        val sourceUrl = try {
            // access via reflection-free: WebBook holds source string
            "source"
        } catch (_: Exception) {
            ""
        }

        if (key.startsWith("http://") || key.startsWith("https://")) {
            log(null, "⇒开始访问详情页:$key")
            val book = webBook.getBookInfo(key)
            log(null, "≡获取成功《${book.name}》 author=${book.author}")
            tocAndContent(webBook, book)
            return
        }

        log(null, "⇒开始搜索关键字:$key")
        val list = webBook.searchBook(key, 1)
        log(null, "≡搜索到 ${list.size} 条")
        list.take(3).forEachIndexed { i, s ->
            log(null, "  [$i] ${s.name} - ${s.author} @ ${s.bookUrl}")
        }
        val first = list.firstOrNull() ?: run {
            log(null, "※未搜索到结果")
            return
        }
        log(null, "⇒取第一条进入详情: ${first.bookUrl}")
        val book = webBook.getBookInfo(first.bookUrl)
        log(null, "≡详情《${book.name}》 toc=${book.tocUrl}")
        tocAndContent(webBook, book)
    }

    private suspend fun tocAndContent(webBook: WebBook, book: Book) {
        log(null, "⇒获取目录列表…")
        val chapters = webBook.getChapterList(book)
        log(null, "≡目录 ${chapters.size} 章, 末章=${chapters.lastOrNull()?.title}")
        val ch = chapters.firstOrNull() ?: return
        log(null, "⇒获取正文: ${ch.title}")
        val content = webBook.getBookContent(book, ch, chapters.getOrNull(1)?.url)
        val preview = content.take(200).replace("\n", " ")
        log(null, "≡正文长度=${content.length} 预览: $preview")
    }
}

// allow WebBook.debugLogger set
var io.legado.app.model.webBook.WebBook.debugLogger: DebugLog?
    get() = null
    set(_) {}
''')

# Better: add debugLogger field to WebBook properly by rewriting WebBook snippet
w("io/legado/app/model/webBook/WebBook.kt", r'''
package io.legado.app.model.webBook

import io.legado.app.data.entities.Book
import io.legado.app.data.entities.BookChapter
import io.legado.app.data.entities.BookSource
import io.legado.app.data.entities.SearchBook
import io.legado.app.model.DebugLog

class WebBook(
    private val bookSourceStr: String,
    private val debugLog: Boolean = false,
    debugLogger: DebugLog? = null,
    private val userNameSpace: String = "default"
) {
    var debugLogger: DebugLog? = debugLogger

    private val source: BookSource by lazy {
        BookSource.fromJson(bookSourceStr).getOrThrow()
    }

    fun getBookSource(): BookSource = source

    suspend fun searchBook(key: String, page: Int = 1): List<SearchBook> {
        debugLogger?.log(source.bookSourceUrl, "搜索: $key page=$page")
        return BookList.searchBook(source, key, page, debugLogger)
    }

    suspend fun exploreBook(url: String, page: Int = 1): List<SearchBook> {
        debugLogger?.log(source.bookSourceUrl, "发现: $url page=$page")
        return BookList.exploreBook(source, url, page, debugLogger)
    }

    suspend fun getBookInfo(bookUrl: String): Book {
        debugLogger?.log(source.bookSourceUrl, "详情: $bookUrl")
        return BookInfo.getBookInfo(source, bookUrl, debugLogger)
    }

    suspend fun getChapterList(book: Book): List<BookChapter> {
        debugLogger?.log(source.bookSourceUrl, "目录: ${book.bookUrl}")
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
        debugLogger?.log(source.bookSourceUrl, "正文: ${chapter.title}")
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

w("com/htmake/reader/api/controller/BookSourceDebug.kt", r'''
package com.htmake.reader.api.controller

import com.htmake.reader.api.ReturnData
import com.htmake.reader.utils.ExtKt
import io.legado.app.model.Debugger
import io.legado.app.model.webBook.WebBook
import io.vertx.core.json.Json
import io.vertx.ext.web.RoutingContext
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

/**
 * bookSourceDebugSSE: stream debug lines while Debugger runs search/info/toc/content.
 */
suspend fun BookController.bookSourceDebugSSE(context: RoutingContext) {
    val response = context.response()
        .putHeader("Content-Type", "text/event-stream; charset=utf-8")
        .putHeader("Cache-Control", "no-cache")
        .setChunked(true)

    fun emit(map: Map<String, Any?>) {
        if (!response.ended()) {
            response.write("data: ${ExtKt.jsonEncode(map)}\n\n")
        }
    }

    val ns = getUserNameSpace(context)
    val keyword = context.queryParam("keyword").firstOrNull()
        ?: context.queryParam("key").firstOrNull()
        ?: ""
    val sourceUrl = context.queryParam("bookSourceUrl").firstOrNull()
        ?: context.queryParam("url").firstOrNull()
    val sourceStr = when {
        !sourceUrl.isNullOrEmpty() -> getBookSourceStringBySourceURLOpt(sourceUrl, ns)
        else -> context.queryParam("bookSource").firstOrNull()
    }

    if (sourceStr.isNullOrEmpty()) {
        emit(mapOf("msg" to "未配置书源"))
        response.write("event: end\n")
        response.end("data: ${ExtKt.jsonEncode(mapOf("end" to true))}\n\n")
        return
    }
    if (keyword.isEmpty()) {
        emit(mapOf("msg" to "请输入关键字或书籍URL"))
        response.write("event: end\n")
        response.end("data: ${ExtKt.jsonEncode(mapOf("end" to true))}\n\n")
        return
    }

    context.request().connection().closeHandler {
        // client disconnected
    }

    val debugger = Debugger { msg -> emit(mapOf("msg" to msg)) }
    val webBook = WebBook(sourceStr, false, debugger, ns)
    try {
        withContext(Dispatchers.IO) {
            debugger.startDebug(webBook, keyword)
        }
    } catch (e: Exception) {
        emit(mapOf("msg" to "※调试异常: ${e.message}"))
    }
    response.write("event: end\n")
    response.end("data: ${ExtKt.jsonEncode(mapOf("end" to true))}\n\n")
}
''')

# ---------------------------------------------------------------------------
# Replace rules service + patch getBookContent
# ---------------------------------------------------------------------------
w("io/legado/app/help/ContentProcessor.kt", r'''
package io.legado.app.help

import com.google.gson.JsonParser
import com.htmake.reader.utils.ExtKt
import io.legado.app.data.entities.Book

/**
 * Apply user replaceRule.json on chapter content.
 * Rule fields (legado): pattern, replacement, isRegex, scope(title/content), enable, timeout...
 */
object ContentProcessor {

    data class ReplaceRule(
        val name: String = "",
        val pattern: String = "",
        val replacement: String = "",
        val isRegex: Boolean = true,
        val isEnabled: Boolean = true,
        val scope: String = "content" // content | title
    )

    fun loadRules(userNameSpace: String): List<ReplaceRule> {
        val raw = ExtKt.getStorage("data", userNameSpace, "replaceRule") ?: return emptyList()
        return try {
            val arr = JsonParser.parseString(raw).asJsonArray
            arr.mapNotNull { el ->
                val o = el.asJsonObject
                ReplaceRule(
                    name = o.get("name")?.asString ?: "",
                    pattern = o.get("pattern")?.asString ?: o.get("regex")?.asString ?: return@mapNotNull null,
                    replacement = o.get("replacement")?.asString ?: o.get("replace")?.asString ?: "",
                    isRegex = o.get("isRegex")?.asBoolean ?: true,
                    isEnabled = o.get("isEnabled")?.asBoolean ?: o.get("enable")?.asBoolean ?: true,
                    scope = o.get("scope")?.asString ?: "content"
                )
            }
        } catch (_: Exception) {
            emptyList()
        }
    }

    fun applyContent(userNameSpace: String, book: Book?, content: String): String {
        var text = content
        val rules = loadRules(userNameSpace).filter { it.isEnabled && it.scope != "title" }
        for (r in rules) {
            try {
                text = if (r.isRegex) {
                    text.replace(Regex(r.pattern), r.replacement)
                } else {
                    text.replace(r.pattern, r.replacement)
                }
            } catch (_: Exception) {
                // bad regex — skip
            }
        }
        return text
    }

    fun applyTitle(userNameSpace: String, title: String): String {
        var text = title
        val rules = loadRules(userNameSpace).filter { it.isEnabled && it.scope == "title" }
        for (r in rules) {
            try {
                text = if (r.isRegex) text.replace(Regex(r.pattern), r.replacement)
                else text.replace(r.pattern, r.replacement)
            } catch (_: Exception) {
            }
        }
        return text
    }
}
''')

# Patch BookController getBookContent via search_replace in file
book_ctrl = BIZ / "com/htmake/reader/api/controller/BookController.kt"
bc = book_ctrl.read_text(encoding="utf-8")
if "ContentProcessor" not in bc:
    bc = bc.replace(
        "import io.legado.app.help.BookHelp",
        "import io.legado.app.help.BookHelp\nimport io.legado.app.help.ContentProcessor"
    )
    old = '''        val content = if (book.isLocalBook) {
            LocalBook.getContent(book, chapter) ?: ""
        } else {
            val src = sourceStr ?: return rd.setErrorMsg("书源信息错误")
            WebBook(src, appConfig.debugLog, null, ns).getBookContent(book, chapter, nextUrl)
        }
        cacheDir.mkdirs()
        cacheFile.writeText(content)
        return rd.setData(content)'''
    new = '''        var content = if (book.isLocalBook) {
            LocalBook.getContent(book, chapter) ?: ""
        } else {
            val src = sourceStr ?: return rd.setErrorMsg("书源信息错误")
            WebBook(src, appConfig.debugLog, null, ns).getBookContent(book, chapter, nextUrl)
        }
        // apply user replace rules (replaceRule.json)
        content = ContentProcessor.applyContent(ns, book, content)
        cacheDir.mkdirs()
        cacheFile.writeText(content)
        return rd.setData(content)'''
    if old in bc:
        bc = bc.replace(old, new)
        book_ctrl.write_text(bc, encoding="utf-8")
        print("patched getBookContent with ContentProcessor")
    else:
        print("WARN: getBookContent block not found for patch")

# Update YueduApi to mount webdav
yuedu = BIZ / "com/htmake/reader/api/YueduApi.kt"
yt = yuedu.read_text(encoding="utf-8")
if "webdav.mount" not in yt and "webdav.mount(" not in yt:
    yt = yt.replace(
        "httpTts = HttpTTSController(coroutineContext)",
        "httpTts = HttpTTSController(coroutineContext)\n        webdav.mount(router, this)"
    )
    # fix bookSourceDebugSSE to use real impl - already calls book.bookSourceDebugSSE
    yuedu.write_text(yt, encoding="utf-8")
    print("mounted webdav on router")

# Remove stub bookSourceDebugSSE from BookControllerExtras if present
extras = BIZ / "com/htmake/reader/api/controller/BookControllerExtras.kt"
if extras.exists():
    et = extras.read_text(encoding="utf-8")
    import re
    et2 = re.sub(
        r"\nfun BookController\.bookSourceDebugSSE\(context: RoutingContext\) \{[\s\S]*?\n\}\n",
        "\n// bookSourceDebugSSE → BookSourceDebug.kt\n",
        et,
        count=1,
    )
    if et2 != et:
        extras.write_text(et2, encoding="utf-8")
        print("removed stub bookSourceDebugSSE")

# ---------------------------------------------------------------------------
# Cross-ref INDEX
# ---------------------------------------------------------------------------
biz_files = sorted(p.relative_to(BIZ).as_posix() for p in BIZ.rglob("*.kt"))
src_files = sorted(p.relative_to(SRC).as_posix() for p in SRC.rglob("*.kt")) + \
            sorted(p.relative_to(SRC).as_posix() for p in SRC.rglob("*.java"))

# map by simple class name
def classname(path: str) -> str:
    return Path(path).stem.replace("Kt", "").replace("Extras", "").replace("Api", "")

biz_map = {}
for f in biz_files:
    biz_map.setdefault(classname(f), []).append(f)

src_map = {}
for f in src_files:
    src_map.setdefault(classname(f), []).append(f)

lines = [
    "# business/ ↔ src/ 对照索引",
    "",
    "生成自 phase6。业务化文件优先阅读；细节对照反编译 `src/`。",
    "",
    "| 类名 | business | src（反编译） |",
    "|------|----------|---------------|",
]
all_names = sorted(set(biz_map) | set(src_map))
for name in all_names:
    b = "<br>".join(f"`{x}`" for x in biz_map.get(name, [])) or "—"
    s = "<br>".join(f"`{x}`" for x in src_map.get(name, [])[:3]) or "—"
    if name in biz_map:  # only rows with business side, plus important
        lines.append(f"| {name} | {b} | {s} |")

# also list business-only summary counts
lines += [
    "",
    f"- business `.kt` 文件数: **{len(biz_files)}**",
    f"- src 源文件数: **{len(src_files)}**",
    "",
    "## 推荐阅读路径",
    "1. `business/com/htmake/reader/api/YueduApi.kt`",
    "2. `BookController.kt` + `BookControllerExtras.kt` + `BookSourceDebug.kt` + `LocalBookApi.kt`",
    "3. `WebdavController.kt`",
    "4. `analyzeRule/*` + `help/SourceAnalyzer.kt` + `help/ContentProcessor.kt`",
    "5. `localBook/*`",
    "6. 对照 `src/` 同名类",
]
(BIZ / "INDEX.md").write_text("\n".join(lines), encoding="utf-8")
print("wrote INDEX.md")

# README phase6
rp = BIZ / "README.md"
rd = rp.read_text(encoding="utf-8") if rp.exists() else ""
if "Phase 6" not in rd:
    rd += """

## Phase 6 增量

- **WebdavController**：PROPFIND/MKCOL/PUT/GET/DELETE/MOVE/COPY/LOCK/UNLOCK + zip 备份
- **Debugger + bookSourceDebugSSE**：搜索→详情→目录→正文 逐步 SSE 日志
- **ContentProcessor**：`replaceRule.json` 应用到 `getBookContent`
- **INDEX.md**：business ↔ src 类名对照索引
"""
    rp.write_text(rd, encoding="utf-8")

print("phase6 complete", len(list(BIZ.rglob('*.kt'))))
