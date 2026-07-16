# -*- coding: utf-8 -*-
"""Phase 7: WebDAV Destination, Debugger explore, replace scope/timeout, export txt/epub."""
from pathlib import Path
import os
import re

BIZ = Path(r"C:\Users\chong\reader-pro-3.2.14-reverse\best-of-3\business")
H = "/** Business rewrite from reader-pro-3.2.14.jar — phase7. */\n\n"

def w(rel, c):
    p = BIZ / rel.replace("/", os.sep)
    p.parent.mkdir(parents=True, exist_ok=True)
    p.write_text(H + c.lstrip("\n"), encoding="utf-8", newline="\n")
    print("wrote", rel, p.stat().st_size)

# ---------------------------------------------------------------------------
# WebDAV path utils + fixed MOVE/COPY
# ---------------------------------------------------------------------------
w("com/htmake/reader/api/controller/WebdavPaths.kt", r'''
package com.htmake.reader.api.controller

import java.io.File
import java.net.URI
import java.net.URLDecoder

/**
 * Resolve WebDAV Destination header the same way as jar:
 * Destination is absolute URL → take path → strip /reader3/webdav → join under home.
 */
object WebdavPaths {

    fun pathFromRequest(requestPath: String): String {
        var path = requestPath
        path = path.replace(Regex("/reader3/webdav", RegexOption.IGNORE_CASE), "")
        path = URLDecoder.decode(path, "UTF-8")
        if (!path.startsWith("/")) path = "/$path"
        return path
    }

    fun resolveUnderHome(home: File, relativeWebPath: String): File {
        val rel = relativeWebPath.trimStart('/')
        val target = File(home, rel).canonicalFile
        require(target.path.startsWith(home.canonicalPath)) { "非法路径" }
        return target
    }

    /**
     * @param destinationHeader full URL or path from Destination header
     * @return path relative to webdav root starting with /
     */
    fun destinationToRelativePath(destinationHeader: String): String? {
        return try {
            val uri = URI(destinationHeader)
            var path = uri.path ?: return null
            path = path.replace(Regex("/reader3/webdav", RegexOption.IGNORE_CASE), "")
            if (!path.startsWith("/")) path = "/$path"
            URLDecoder.decode(path, "UTF-8")
        } catch (_: Exception) {
            // not a valid URI — treat as path
            var path = destinationHeader
            path = path.replace(Regex("https?://[^/]+", RegexOption.IGNORE_CASE), "")
            path = path.replace(Regex("/reader3/webdav", RegexOption.IGNORE_CASE), "")
            if (!path.startsWith("/")) path = "/$path"
            URLDecoder.decode(path, "UTF-8")
        }
    }
}
''')

# Patch WebdavController move/copy methods by rewriting key functions via full file update of those methods
# Easier: rewrite entire WebdavController with fixed Destination
w("com/htmake/reader/api/controller/WebdavController.kt", r'''
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
''')

# ---------------------------------------------------------------------------
# Debugger full branches
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
 * Debug entry key forms (from jar):
 * - absolute URL → infoDebug
 * - contains `::` → exploreDebug (after :: is explore URL)
 * - starts with `++` → tocDebug (toc url)
 * - starts with `--` → contentDebug (content url)
 * - else → searchDebug
 */
class Debugger(val logMsg: (String) -> Unit) : DebugLog {
    private val debugTimeFormat = SimpleDateFormat("[mm:ss.SSS]", Locale.getDefault())
    private var startTime = System.currentTimeMillis()

    override fun log(sourceUrl: String?, msg: String?) = log(sourceUrl, msg, false)

    override fun log(message: String) {
        val t = debugTimeFormat.format(Date(System.currentTimeMillis() - startTime))
        logMsg("$t $message")
    }

    override fun log(sourceUrl: String?, msg: String?, isHtml: Boolean) {
        if (msg == null) return
        var printMsg = msg
        if (isHtml) printMsg = HtmlFormatter.formatKeepImg(msg)
        val t = debugTimeFormat.format(Date(System.currentTimeMillis() - startTime))
        logMsg("$t $printMsg")
    }

    suspend fun startDebug(webBook: WebBook, key: String) {
        val bookSource = webBook.getBookSource()
        val origin = bookSource.bookSourceUrl
        startTime = System.currentTimeMillis()

        when {
            key.startsWith("http://") || key.startsWith("https://") -> {
                log(origin, "⇒开始访问详情页:$key")
                val book = Book(bookUrl = key, origin = origin)
                infoDebug(webBook, book)
            }
            key.contains("::") -> {
                val url = key.substringAfter("::")
                log(origin, "⇒开始访问发现页:$url")
                exploreDebug(webBook, url)
            }
            key.startsWith("++") -> {
                val tocUrl = key.substring(2)
                log(origin, "⇒开始访问目录页:$tocUrl")
                val book = Book(bookUrl = tocUrl, tocUrl = tocUrl, origin = origin)
                tocDebug(webBook, book)
            }
            key.startsWith("--") -> {
                val contentUrl = key.substring(2)
                log(origin, "⇒开始访问正文页:$contentUrl")
                val book = Book(bookUrl = contentUrl, origin = origin)
                val chapter = io.legado.app.data.entities.BookChapter(url = contentUrl, title = "debug", bookUrl = contentUrl)
                contentDebug(webBook, book, chapter)
            }
            else -> {
                log(origin, "⇒开始搜索关键字:$key")
                searchDebug(webBook, key)
            }
        }
    }

    private suspend fun searchDebug(webBook: WebBook, key: String) {
        val origin = webBook.getBookSource().bookSourceUrl
        val list = webBook.searchBook(key, 1)
        log(origin, "≡搜索到 ${list.size} 条")
        list.take(5).forEachIndexed { i, s ->
            log(origin, "  [$i] ${s.name} - ${s.author} | ${s.bookUrl}")
        }
        val first = list.firstOrNull() ?: run {
            log(origin, "※未搜索到结果")
            return
        }
        log(origin, "⇒取第一条进入详情")
        infoDebug(webBook, Book(bookUrl = first.bookUrl, name = first.name, author = first.author, origin = origin, coverUrl = first.coverUrl))
    }

    private suspend fun exploreDebug(webBook: WebBook, url: String) {
        val origin = webBook.getBookSource().bookSourceUrl
        val list = webBook.exploreBook(url, 1)
        log(origin, "≡发现 ${list.size} 条")
        list.take(5).forEachIndexed { i, s ->
            log(origin, "  [$i] ${s.name} - ${s.author}")
        }
        val first = list.firstOrNull() ?: return
        infoDebug(webBook, Book(bookUrl = first.bookUrl, name = first.name, author = first.author, origin = origin))
    }

    private suspend fun infoDebug(webBook: WebBook, book: Book) {
        val origin = webBook.getBookSource().bookSourceUrl
        val info = webBook.getBookInfo(book.bookUrl)
        log(origin, "≡详情《${info.name}》 author=${info.author} toc=${info.tocUrl}")
        tocDebug(webBook, info)
    }

    private suspend fun tocDebug(webBook: WebBook, book: Book) {
        val origin = webBook.getBookSource().bookSourceUrl
        val chapters = webBook.getChapterList(book)
        log(origin, "≡目录 ${chapters.size} 章")
        chapters.take(3).forEach { log(origin, "  - ${it.title}") }
        if (chapters.size > 3) log(origin, "  …")
        val first = chapters.firstOrNull() ?: return
        contentDebug(webBook, book, first, chapters.getOrNull(1)?.url)
    }

    private suspend fun contentDebug(
        webBook: WebBook,
        book: Book,
        chapter: io.legado.app.data.entities.BookChapter,
        nextUrl: String? = null
    ) {
        val origin = webBook.getBookSource().bookSourceUrl
        val content = webBook.getBookContent(book, chapter, nextUrl)
        val preview = content.take(200).replace("\n", " ")
        log(origin, "≡正文 length=${content.length} 预览: $preview")
    }
}
''')

# ---------------------------------------------------------------------------
# ContentProcessor with timeout + bookName
# ---------------------------------------------------------------------------
w("io/legado/app/help/ContentProcessor.kt", r'''
package io.legado.app.help

import com.google.gson.JsonParser
import com.htmake.reader.utils.ExtKt
import io.legado.app.data.entities.Book
import java.util.concurrent.Callable
import java.util.concurrent.Executors
import java.util.concurrent.TimeUnit
import java.util.concurrent.TimeoutException

/**
 * Apply replaceRule.json.
 *
 * Fields (legado-compatible):
 * - pattern / regex
 * - replacement / replace
 * - isRegex (default true)
 * - isEnabled / enable
 * - scope: content | title (default content)
 * - timeout: ms for single rule (default 3000)
 * - name
 * - bookName: if set, only apply when book.name contains / matches
 * - useReplace: optional
 */
object ContentProcessor {

    data class ReplaceRule(
        val name: String = "",
        val pattern: String = "",
        val replacement: String = "",
        val isRegex: Boolean = true,
        val isEnabled: Boolean = true,
        val scope: String = "content",
        val timeoutMs: Long = 3000,
        val bookName: String = "" // empty = all books
    )

    private val pool = Executors.newCachedThreadPool()

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
                    isEnabled = o.get("isEnabled")?.asBoolean
                        ?: o.get("enable")?.asBoolean
                        ?: o.get("isEnabled")?.asBoolean
                        ?: true,
                    scope = o.get("scope")?.asString ?: "content",
                    timeoutMs = o.get("timeout")?.asLong
                        ?: o.get("timeoutMillisecond")?.asLong
                        ?: 3000L,
                    bookName = o.get("bookName")?.asString
                        ?: o.get("nameFilter")?.asString
                        ?: ""
                )
            }
        } catch (_: Exception) {
            emptyList()
        }
    }

    fun applyContent(userNameSpace: String, book: Book?, content: String): String {
        var text = content
        val rules = loadRules(userNameSpace).filter {
            it.isEnabled && it.scope != "title" && matchesBook(it, book)
        }
        for (r in rules) {
            text = applyOne(text, r)
        }
        return text
    }

    fun applyTitle(userNameSpace: String, book: Book?, title: String): String {
        var text = title
        val rules = loadRules(userNameSpace).filter {
            it.isEnabled && it.scope == "title" && matchesBook(it, book)
        }
        for (r in rules) {
            text = applyOne(text, r)
        }
        return text
    }

    private fun matchesBook(rule: ReplaceRule, book: Book?): Boolean {
        if (rule.bookName.isBlank()) return true
        val name = book?.name ?: return true
        return name.contains(rule.bookName) || name.matches(Regex(rule.bookName))
    }

    private fun applyOne(text: String, r: ReplaceRule): String {
        val task = Callable {
            if (r.isRegex) text.replace(Regex(r.pattern), r.replacement)
            else text.replace(r.pattern, r.replacement)
        }
        val future = pool.submit(task)
        return try {
            future.get(r.timeoutMs.coerceAtLeast(100), TimeUnit.MILLISECONDS)
        } catch (_: TimeoutException) {
            future.cancel(true)
            text // skip on timeout
        } catch (_: Exception) {
            text
        }
    }
}
''')

# ---------------------------------------------------------------------------
# Export TXT / EPUB
# ---------------------------------------------------------------------------
w("com/htmake/reader/api/controller/BookExport.kt", r'''
package com.htmake.reader.api.controller

import com.htmake.reader.api.ReturnData
import com.htmake.reader.utils.ExtKt
import io.legado.app.data.entities.Book
import io.legado.app.data.entities.BookChapter
import io.legado.app.help.ContentProcessor
import io.legado.app.model.localBook.LocalBook
import io.legado.app.model.webBook.WebBook
import io.legado.app.utils.FileUtils
import io.vertx.ext.web.RoutingContext
import me.ag2s.epublib.domain.Author
import me.ag2s.epublib.domain.EpubBook
import me.ag2s.epublib.domain.Metadata
import me.ag2s.epublib.domain.Resource
import me.ag2s.epublib.epub.EpubWriter
import java.io.File
import java.io.FileOutputStream
import java.nio.charset.Charset

/**
 * exportBook: type=txt|epub → file under storage/data/{user}/export/
 */
object BookExport {

    suspend fun exportBook(ctrl: BookController, context: RoutingContext): ReturnData {
        val rd = ReturnData()
        if (!ctrl.checkAuth(context)) return rd.setData("NEED_LOGIN").setErrorMsg("请登录后使用")
        val ns = ctrl.getUserNameSpace(context)
        val type = context.queryParam("type").firstOrNull()
            ?: context.bodyAsJson?.getString("type")
            ?: "txt"
        val bookUrl = context.queryParam("bookUrl").firstOrNull()
            ?: context.bodyAsJson?.getString("bookUrl")
            ?: return rd.setErrorMsg("bookUrl 不能为空")
        val book = ctrl.getShelfBookByURL(bookUrl, ns) ?: return rd.setErrorMsg("书籍不存在")
        val source = ctrl.getBookSourceString(book, ns) ?: ""
        val exportDir = File(ExtKt.getWorkDir("storage", "data", ns, "export")).apply { mkdirs() }
        val file = when (type.lowercase()) {
            "epub" -> exportToEpub(exportDir, book, source, ns, ctrl)
            else -> exportToTxt(exportDir, book, source, ns, ctrl)
        }
        return rd.setData(
            mapOf(
                "path" to file.absolutePath,
                "name" to file.name,
                "size" to file.length(),
                "type" to type
            )
        )
    }

    suspend fun exportToTxt(
        exportDir: File,
        book: Book,
        bookSource: String,
        userNameSpace: String,
        ctrl: BookController
    ): File {
        val safeName = book.name.ifEmpty { "book" }.replace(Regex("""[\\/:*?"<>|]"""), "_")
        val author = book.author.ifEmpty { "未知" }
        val out = File(exportDir, "《${safeName}》作者：${author}.txt")
        if (out.exists()) out.delete()
        out.parentFile?.mkdirs()
        val charset = Charset.forName("UTF-8")
        getAllContents(book, bookSource, userNameSpace, ctrl) { text, _ ->
            out.appendText(text, charset)
        }
        return out
    }

    suspend fun exportToEpub(
        exportDir: File,
        book: Book,
        bookSource: String?,
        userNameSpace: String,
        ctrl: BookController
    ): File {
        val safeName = book.name.ifEmpty { "book" }.replace(Regex("""[\\/:*?"<>|]"""), "_")
        val out = File(exportDir, "${safeName}.epub")
        val epub = EpubBook()
        val md: Metadata = epub.metadata
        md.addTitle(book.name)
        if (book.author.isNotEmpty()) md.addAuthor(Author(book.author))
        val chapters = ctrl.getLocalChapterList(book, bookSource, false, userNameSpace, false, null)
        chapters.forEachIndexed { i, ch ->
            var content = fetchChapter(book, bookSource, userNameSpace, ch, chapters.getOrNull(i + 1)?.url, ctrl)
            content = ContentProcessor.applyContent(userNameSpace, book, content)
            val html = """<!DOCTYPE html><html><head><meta charset="utf-8"/><title>${esc(ch.title)}</title></head>
<body><h1>${esc(ch.title)}</h1>${toHtmlParagraphs(content)}</body></html>"""
            val href = "chapter_$i.xhtml"
            epub.addSection(ch.title, Resource(html.toByteArray(Charsets.UTF_8), href))
        }
        FileOutputStream(out).use { EpubWriter().write(epub, it) }
        return out
    }

    private suspend fun getAllContents(
        book: Book,
        bookSource: String,
        userNameSpace: String,
        ctrl: BookController,
        append: (String, Any?) -> Unit
    ) {
        val chapters = ctrl.getLocalChapterList(book, bookSource, false, userNameSpace, false, null)
        append("《${book.name}》\n作者：${book.author}\n\n", null)
        chapters.forEachIndexed { i, ch ->
            val title = ContentProcessor.applyTitle(userNameSpace, book, ch.title)
            append("\n\n$title\n\n", null)
            var content = fetchChapter(book, bookSource, userNameSpace, ch, chapters.getOrNull(i + 1)?.url, ctrl)
            content = ContentProcessor.applyContent(userNameSpace, book, content)
            // strip tags lightly for txt
            content = content.replace(Regex("<br\\s*/?>", RegexOption.IGNORE_CASE), "\n")
                .replace(Regex("<[^>]+>"), "")
            append(content, null)
        }
    }

    private suspend fun fetchChapter(
        book: Book,
        bookSource: String?,
        userNameSpace: String,
        ch: BookChapter,
        nextUrl: String?,
        ctrl: BookController
    ): String {
        return if (book.isLocalBook) {
            LocalBook.getContent(book, ch) ?: ""
        } else {
            val src = bookSource ?: return ""
            WebBook(src, false, null, userNameSpace).getBookContent(book, ch, nextUrl)
        }
    }

    private fun toHtmlParagraphs(text: String): String {
        if (text.contains("<p", ignoreCase = true) || text.contains("<div", ignoreCase = true)) return text
        return text.split(Regex("\n+")).joinToString("") { "<p>${esc(it)}</p>" }
    }

    private fun esc(s: String) =
        s.replace("&", "&amp;").replace("<", "&lt;").replace(">", "&gt;")
}

suspend fun BookController.exportBook(context: RoutingContext): ReturnData =
    BookExport.exportBook(this, context)
''')

# Wire export in BookControllerExtras - remove stub exportBook
extras = BIZ / "com/htmake/reader/api/controller/BookControllerExtras.kt"
if extras.exists():
    t = extras.read_text(encoding="utf-8")
    t2 = re.sub(
        r"\nfun BookController\.exportBook[\s\S]*?(?=\nfun BookController\.|\Z)",
        "\n// exportBook → BookExport.kt\n",
        t,
        count=1,
    )
    # also fix getTxtTocRules already ok
    if t2 != t:
        extras.write_text(t2, encoding="utf-8")
        print("removed exportBook stub")

# Fix applyTitle signature used with book - already has book param in ContentProcessor

# README
rp = BIZ / "README.md"
rd = rp.read_text(encoding="utf-8") if rp.exists() else ""
if "Phase 7" not in rd:
    rd += """

## Phase 7 增量

- **WebdavPaths + MOVE/COPY**：Destination 按 URL.path 解析并去掉 `/reader3/webdav`，Overwrite 语义对齐 jar（缺省 412）
- **Debugger**：`http(s)` 详情 / `::` 发现 / `++` 目录 / `--` 正文 / 默认搜索
- **ContentProcessor**：timeout（默认 3s）、bookName 过滤、title/content 作用域
- **BookExport**：`exportToTxt` / `exportToEpub`（全文拉取 + 替换规则）
"""
    rp.write_text(rd, encoding="utf-8")

# Update INDEX briefly
print("phase7 complete", len(list(BIZ.rglob('*.kt'))))
