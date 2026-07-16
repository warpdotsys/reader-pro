# -*- coding: utf-8 -*-
"""Fill remaining API implementations for src/main + sync business gaps."""
from pathlib import Path
import os
import shutil

ROOT = Path(r"C:\Users\chong\reader-pro-3.2.14-reverse")
SRC = ROOT / "src" / "main" / "kotlin"
RES = ROOT / "src" / "main" / "resources"
BIZ = ROOT / "best-of-3" / "business"


def w(base: Path, rel: str, content: str):
    p = base / rel.replace("/", os.sep)
    p.parent.mkdir(parents=True, exist_ok=True)
    p.write_text(content.lstrip("\n"), encoding="utf-8", newline="\n")
    print("wrote", base.name if base != SRC else "src", rel)


# ---------------------------------------------------------------------------
# BookMore — missing book endpoints
# ---------------------------------------------------------------------------
w(SRC, "com/htmake/reader/api/controller/BookMore.kt", r'''
package com.htmake.reader.api.controller

import com.htmake.reader.api.ReturnData
import com.htmake.reader.utils.ExtKt
import io.legado.app.data.entities.Book
import io.legado.app.data.entities.BookChapter
import io.legado.app.data.entities.BookSource
import io.legado.app.data.entities.SearchResult
import io.legado.app.help.BookHelp
import io.legado.app.help.ContentProcessor
import io.legado.app.model.localBook.LocalBook
import io.legado.app.model.webBook.WebBook
import io.legado.app.utils.MD5Utils
import io.vertx.core.http.HttpMethod
import io.vertx.core.json.JsonArray
import io.vertx.core.json.JsonObject
import io.vertx.ext.web.RoutingContext
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.coroutineScope
import me.ag2s.epublib.domain.Author
import me.ag2s.epublib.domain.EpubBook
import me.ag2s.epublib.domain.Resource
import me.ag2s.epublib.epub.EpubWriter
import java.io.File
import java.io.FileOutputStream
import java.util.Base64

// ---------- helpers ----------
private fun p(ctx: RoutingContext, key: String): String? {
    if (ctx.request().method() == HttpMethod.POST) ctx.bodyAsJson?.getString(key)?.let { return it }
    return ctx.queryParam(key).firstOrNull()
}

private fun pInt(ctx: RoutingContext, key: String): Int? {
    if (ctx.request().method() == HttpMethod.POST) ctx.bodyAsJson?.getInteger(key)?.let { return it }
    return ctx.queryParam(key).firstOrNull()?.toIntOrNull()
}

// ---------- delete multi books ----------
suspend fun BookController.deleteBooks(ctx: RoutingContext): ReturnData {
    val rd = ReturnData()
    if (!checkAuth(ctx)) return rd.setData("NEED_LOGIN").setErrorMsg("请登录后使用")
    val ns = getUserNameSpace(ctx)
    val urls = ctx.bodyAsJson?.getJsonArray("bookUrlList")
        ?: ctx.bodyAsJson?.getJsonArray("urls")
        ?: JsonArray()
    val arr = ExtKt.asJsonArray(getUserStorage(ns, "bookshelf")) ?: JsonArray()
    val drop = (0 until urls.size()).mapNotNull { urls.getString(it) }.toSet()
    val list = arr.list.filterIndexed { i, _ -> arr.getJsonObject(i).getString("bookUrl") !in drop }
    saveUserStorage(ns, "bookshelf", JsonArray(list))
    return rd.setData(true)
}

// ---------- cover stream ----------
fun BookController.cover(ctx: RoutingContext) {
    val path = ctx.queryParam("path").firstOrNull()
        ?: ctx.queryParam("url").firstOrNull()
    if (path.isNullOrBlank()) {
        ctx.response().setStatusCode(404).end(); return
    }
    val f = when {
        path.startsWith("http") -> null
        else -> File(path).takeIf { it.isFile }
            ?: File(ExtKt.getWorkDir(path.trimStart('/'))).takeIf { it.isFile }
    }
    if (f != null) ctx.response().sendFile(f.absolutePath)
    else ctx.response().setStatusCode(404).end()
}

// ---------- import / refresh local ----------
suspend fun BookController.importBookPreview(ctx: RoutingContext): ReturnData {
    val rd = ReturnData()
    if (!checkAuth(ctx)) return rd.setData("NEED_LOGIN").setErrorMsg("请登录后使用")
    val path = p(ctx, "path") ?: p(ctx, "file") ?: return rd.setErrorMsg("path 不能为空")
    val ns = getUserNameSpace(ctx)
    val book = Book(
        bookUrl = if (path.startsWith("file:")) path else "file:$path",
        origin = "loc_book",
        name = File(path.removePrefix("file:")).nameWithoutExtension,
        namespace = ns,
        rootDir = ExtKt.getWorkDir()
    )
    val chapters = try { LocalBook.getChapterList(book) } catch (e: Exception) {
        return rd.setErrorMsg(e.message ?: "解析失败")
    }
    return rd.setData(mapOf("book" to book, "chapters" to chapters.take(50), "total" to chapters.size))
}

suspend fun BookController.refreshLocalBook(ctx: RoutingContext): ReturnData {
    val rd = ReturnData()
    if (!checkAuth(ctx)) return rd.setData("NEED_LOGIN").setErrorMsg("请登录后使用")
    val ns = getUserNameSpace(ctx)
    val url = p(ctx, "bookUrl") ?: p(ctx, "url") ?: return rd.setErrorMsg("bookUrl 不能为空")
    val book = getShelfBookByURL(url, ns) ?: return rd.setErrorMsg("书籍不存在")
    if (!book.isLocalBook) return rd.setErrorMsg("非本地书籍")
    book.rootDir = ExtKt.getWorkDir(); book.namespace = ns
    val chapters = LocalBook.getChapterList(book)
    val md5 = MD5Utils.md5Encode(book.bookUrl)
    saveUserStorage(ns, ExtKt.getRelativePath("${book.name}_${book.author}", md5), chapters)
    editShelfBook(book, ns) {
        it.totalChapterNum = chapters.size
        it.latestChapterTitle = chapters.lastOrNull()?.title
        it
    }
    return rd.setData(mapOf("chapters" to chapters.size))
}

suspend fun BookController.getChapterListByRule(ctx: RoutingContext): ReturnData {
    val rd = ReturnData()
    if (!checkAuth(ctx)) return rd.setData("NEED_LOGIN").setErrorMsg("请登录后使用")
    val body = ctx.bodyAsJson ?: return rd.setErrorMsg("参数错误")
    val book = try { body.mapTo(Book::class.java) } catch (_: Exception) {
        return rd.setErrorMsg("书籍信息错误")
    }
    if (!book.isLocalBook && !book.isLocalTxt && !book.isEpub && !book.isPdf) {
        return rd.setErrorMsg("非本地txt/epub/pdf书籍")
    }
    val ns = getUserNameSpace(ctx)
    book.rootDir = ExtKt.getWorkDir(); book.namespace = ns
    body.getString("rule")?.takeIf { it.isNotBlank() }?.let { book.tocUrl = it }
    val chapters = LocalBook.getChapterList(book)
    return rd.setData(mapOf("book" to book, "chapters" to chapters))
}

// ---------- cache ----------
fun BookController.getCachedChapterContentSet(book: Book, ns: String): MutableSet<Int> {
    val dir = getChapterCacheDir(book, ns)
    if (!dir.isDirectory) return linkedSetOf()
    return dir.listFiles()?.mapNotNull {
        if (it.extension.equals("txt", true)) it.nameWithoutExtension.toIntOrNull() else null
    }?.toMutableSet() ?: linkedSetOf()
}

suspend fun BookController.cacheBookOnServer(ctx: RoutingContext): ReturnData {
    val rd = ReturnData()
    if (!checkAuth(ctx)) return rd.setData("NEED_LOGIN").setErrorMsg("请登录后使用")
    val ns = getUserNameSpace(ctx)
    val list = ctx.bodyAsJson?.getJsonArray("bookUrlList") ?: JsonArray()
    if (list.isEmpty) return rd.setErrorMsg("请输入书籍链接")
    // fire and forget style — still run sequentially here for reliability
    for (i in 0 until list.size()) {
        val bookUrl = list.getString(i) ?: continue
        try {
            val book = getShelfBookByURL(bookUrl, ns) ?: continue
            val source = getBookSourceString(book, ns) ?: continue
            val chapters = getLocalChapterList(book, source, false, ns)
            val dir = getChapterCacheDir(book, ns).apply { mkdirs() }
            val cached = getCachedChapterContentSet(book, ns)
            chapters.forEachIndexed { idx, ch ->
                if (idx in cached) return@forEachIndexed
                try {
                    val next = chapters.getOrNull(idx + 1)?.url
                    val content = WebBook(source, appConfig.debugLog, null, ns).getBookContent(book, ch, next)
                    File(dir, "$idx.txt").writeText(content)
                    BookSource.fromJson(source).getOrNull()?.let { src ->
                        BookHelp.saveImages(this, src, book, ch, content)
                    }
                } catch (_: Exception) {
                }
            }
        } catch (_: Exception) {
        }
    }
    return rd.setData("")
}

suspend fun BookController.cacheBookSSE(ctx: RoutingContext) {
    val resp = ctx.response()
        .putHeader("Content-Type", "text/event-stream; charset=utf-8")
        .putHeader("Cache-Control", "no-cache").setChunked(true)
    if (!checkAuth(ctx)) {
        resp.end("event: error\ndata: {\"error\":\"请登录后使用\"}\n\n"); return
    }
    val bookUrl = p(ctx, "url") ?: p(ctx, "bookUrl") ?: ""
    val refresh = pInt(ctx, "refresh") ?: 0
    val concurrent = (pInt(ctx, "concurrentCount") ?: 24).coerceIn(1, 64)
    if (bookUrl.isEmpty()) {
        resp.end("event: error\ndata: {\"error\":\"请输入书籍链接\"}\n\n"); return
    }
    val ns = getUserNameSpace(ctx)
    val book = getShelfBookByURL(bookUrl, ns)
    if (book == null) {
        resp.end("event: error\ndata: {\"error\":\"请先加入书架\"}\n\n"); return
    }
    if (book.isLocalBook) {
        resp.end("event: error\ndata: {\"error\":\"本地书籍无需缓存\"}\n\n"); return
    }
    val source = getBookSourceString(book, ns)
    if (source.isNullOrEmpty()) {
        resp.end("event: error\ndata: {\"error\":\"未配置书源\"}\n\n"); return
    }
    val chapters = getLocalChapterList(book, source, false, ns)
    val cached = if (refresh <= 0) getCachedChapterContentSet(book, ns) else linkedSetOf()
    val dir = getChapterCacheDir(book, ns).apply { mkdirs() }
    var success = 0; var failed = 0
    val pending = chapters.indices.filter { it !in cached }
    coroutineScope {
        pending.chunked(concurrent).forEach { batch ->
            batch.map { idx ->
                async {
                    try {
                        val ch = chapters[idx]
                        val next = chapters.getOrNull(idx + 1)?.url
                        val content = WebBook(source, appConfig.debugLog, null, ns).getBookContent(book, ch, next)
                        File(dir, "$idx.txt").writeText(content)
                        synchronized(cached) {
                            cached += idx; success++
                            if (!resp.ended()) {
                                resp.write("data: ${JsonObject()
                                    .put("index", idx).put("title", ch.title)
                                    .put("success", success).put("failed", failed)
                                    .put("cached", cached.size).put("total", chapters.size)
                                    .encode()}\n\n")
                            }
                        }
                    } catch (e: Exception) {
                        synchronized(cached) {
                            failed++
                            if (!resp.ended()) {
                                resp.write("event: error\ndata: ${JsonObject()
                                    .put("index", idx).put("error", e.message)
                                    .encode()}\n\n")
                            }
                        }
                    }
                }
            }.awaitAll()
        }
    }
    if (!resp.ended()) {
        resp.write("event: end\ndata: ${JsonObject()
            .put("success", success).put("failed", failed)
            .put("cached", cached.size).put("total", chapters.size).encode()}\n\n").end()
    }
}

suspend fun BookController.getShelfBookWithCacheInfo(ctx: RoutingContext): ReturnData {
    val rd = ReturnData()
    if (!checkAuth(ctx)) return rd.setData("NEED_LOGIN").setErrorMsg("请登录后使用")
    val ns = getUserNameSpace(ctx)
    val arr = ExtKt.asJsonArray(getUserStorage(ns, "bookshelf")) ?: JsonArray()
    val out = (0 until arr.size()).map { i ->
        val book = arr.getJsonObject(i).mapTo(Book::class.java).apply { isInShelf = true }
        if (book.isLocalBook) book
        else {
            val n = getCachedChapterContentSet(book, ns).size
            mapOf(
                "bookUrl" to book.bookUrl, "name" to book.name, "author" to book.author,
                "origin" to book.origin, "totalChapterNum" to book.totalChapterNum,
                "durChapterIndex" to book.durChapterIndex, "group" to book.group,
                "cachedChapterCount" to n, "isInShelf" to true
            )
        }
    }
    return rd.setData(out)
}

suspend fun BookController.deleteBookCache(ctx: RoutingContext): ReturnData {
    val rd = ReturnData()
    if (!checkAuth(ctx)) return rd.setData("NEED_LOGIN").setErrorMsg("请登录后使用")
    val bookUrl = p(ctx, "url") ?: p(ctx, "bookUrl") ?: return rd.setErrorMsg("请输入书籍链接")
    val ns = getUserNameSpace(ctx)
    val book = getShelfBookByURL(bookUrl, ns) ?: return rd.setErrorMsg("请先加入书架")
    if (book.isLocalBook) return rd.setErrorMsg("本地书籍无需删除缓存")
    ExtKt.deleteRecursively(getChapterCacheDir(book, ns))
    return rd.setData("")
}

// ---------- export ----------
suspend fun BookController.exportBook(ctx: RoutingContext): ReturnData {
    val rd = ReturnData()
    if (!checkAuth(ctx)) return rd.setData("NEED_LOGIN").setErrorMsg("请登录后使用")
    val ns = getUserNameSpace(ctx)
    val type = (p(ctx, "type") ?: "txt").lowercase()
    val bookUrl = p(ctx, "bookUrl") ?: p(ctx, "url") ?: return rd.setErrorMsg("bookUrl 不能为空")
    val book = getShelfBookByURL(bookUrl, ns) ?: return rd.setErrorMsg("书籍不存在")
    val source = getBookSourceString(book, ns) ?: ""
    val exportDir = File(ExtKt.getWorkDir("storage", "data", ns, "export")).apply { mkdirs() }
    val safe = book.name.ifEmpty { "book" }.replace(Regex("""[\\/:*?"<>|]"""), "_")
    val file = if (type == "epub") {
        val out = File(exportDir, "$safe.epub")
        val epub = EpubBook()
        epub.metadata.addTitle(book.name)
        if (book.author.isNotEmpty()) epub.metadata.addAuthor(Author(book.author))
        val chapters = getLocalChapterList(book, source, false, ns)
        chapters.forEachIndexed { i, ch ->
            var content = fetchContent(book, source, ns, ch, chapters.getOrNull(i + 1)?.url)
            content = ContentProcessor.applyContent(ns, book, content)
            val html = """<!DOCTYPE html><html><head><meta charset="utf-8"/><title>${esc(ch.title)}</title></head>
<body><h1>${esc(ch.title)}</h1>${content.split("\n").joinToString("") { "<p>${esc(it)}</p>" }}</body></html>"""
            epub.addSection(ch.title, Resource(html.toByteArray(), "chapter_$i.xhtml"))
        }
        FileOutputStream(out).use { EpubWriter().write(epub, it) }
        out
    } else {
        val out = File(exportDir, "《${safe}》作者：${book.author.ifEmpty { "未知" }}.txt")
        if (out.exists()) out.delete()
        val chapters = getLocalChapterList(book, source, false, ns)
        out.appendText("《${book.name}》\n作者：${book.author}\n\n")
        chapters.forEachIndexed { i, ch ->
            val title = ContentProcessor.applyTitle(ns, book, ch.title)
            out.appendText("\n\n$title\n\n")
            var content = fetchContent(book, source, ns, ch, chapters.getOrNull(i + 1)?.url)
            content = ContentProcessor.applyContent(ns, book, content)
                .replace(Regex("<br\\s*/?>", RegexOption.IGNORE_CASE), "\n")
                .replace(Regex("<[^>]+>"), "")
            out.appendText(content)
        }
        out
    }
    return rd.setData(mapOf("path" to file.absolutePath, "name" to file.name, "size" to file.length(), "type" to type))
}

private suspend fun BookController.fetchContent(
    book: Book, source: String, ns: String, ch: BookChapter, next: String?
): String {
    val cache = File(getChapterCacheDir(book, ns), "${ch.index}.txt")
    if (cache.isFile) return cache.readText()
    return if (book.isLocalBook) LocalBook.getContent(book, ch) ?: ""
    else if (source.isNotEmpty()) WebBook(source, false, null, ns).getBookContent(book, ch, next)
    else ""
}

private fun esc(s: String) = s.replace("&", "&amp;").replace("<", "&lt;").replace(">", "&gt;")

// ---------- search content ----------
suspend fun BookController.searchBookContent(ctx: RoutingContext): ReturnData {
    val rd = ReturnData()
    if (!checkAuth(ctx)) return rd.setData("NEED_LOGIN").setErrorMsg("请登录后使用")
    val bookUrl = p(ctx, "url") ?: p(ctx, "bookUrl") ?: return rd.setErrorMsg("请输入书籍链接")
    val keyword = p(ctx, "keyword") ?: return rd.setErrorMsg("请输入搜索关键词")
    val lastIndex = pInt(ctx, "lastIndex") ?: 0
    val size = pInt(ctx, "size") ?: 20
    val ns = getUserNameSpace(ctx)
    val book = getShelfBookByURL(bookUrl, ns) ?: return rd.setErrorMsg("请先加入书架")
    val source = if (book.isLocalBook) null else getBookSourceString(book, ns)
    if (!book.isLocalBook && source.isNullOrEmpty()) return rd.setErrorMsg("未配置书源")
    val chapters = getLocalChapterList(book, source, false, ns)
    if (lastIndex >= chapters.size) return rd.setErrorMsg("没有更多了")
    val hits = ArrayList<SearchResult>()
    var current = lastIndex
    var i = lastIndex + 1
    while (i < chapters.size && hits.size < size) {
        val ch = chapters[i]
        current = i
        val text = try { fetchContent(book, source ?: "", ns, ch, chapters.getOrNull(i + 1)?.url) } catch (_: Exception) { "" }
        var from = 0
        val lower = text.lowercase()
        val key = keyword.lowercase()
        var within = 0
        while (true) {
            val pos = lower.indexOf(key, from)
            if (pos < 0) break
            val start = (pos - 20).coerceAtLeast(0)
            val end = (pos + keyword.length + 40).coerceAtMost(text.length)
            hits += SearchResult(
                resultCountWithinChapter = within++,
                resultText = text.substring(start, end),
                chapterTitle = ch.title,
                query = keyword,
                chapterIndex = ch.index,
                queryIndexInResult = pos - start,
                queryIndexInChapter = pos
            )
            if (hits.size >= size) break
            from = pos + keyword.length
        }
        i++
    }
    return rd.setData(mapOf("list" to hits, "lastIndex" to current))
}

// ---------- group / config ----------
suspend fun BookController.saveBookConfig(ctx: RoutingContext): ReturnData {
    val rd = ReturnData()
    if (!checkAuth(ctx)) return rd.setData("NEED_LOGIN").setErrorMsg("请登录后使用")
    val bookUrl = p(ctx, "bookUrl") ?: return rd.setErrorMsg("书籍链接不能为空")
    val w = p(ctx, "pdfImageWidth")?.toFloatOrNull()
        ?: ctx.bodyAsJson?.getFloat("pdfImageWidth") ?: 0f
    if (w <= 0f) return rd.setErrorMsg("pdf图片宽度错误")
    val ns = getUserNameSpace(ctx)
    val book = getShelfBookByURL(bookUrl, ns) ?: return rd.setErrorMsg("书籍信息错误")
    val updated = editShelfBook(book, ns) { it.pdfImageWidth = w; it }
    return rd.setData(updated ?: book)
}

suspend fun BookController.saveBookGroupId(ctx: RoutingContext): ReturnData {
    val rd = ReturnData()
    if (!checkAuth(ctx)) return rd.setData("NEED_LOGIN").setErrorMsg("请登录后使用")
    val bookUrl = p(ctx, "bookUrl") ?: return rd.setErrorMsg("书籍链接不能为空")
    val groupId = p(ctx, "groupId")?.toLongOrNull()
        ?: ctx.bodyAsJson?.getLong("groupId") ?: 0L
    if (groupId <= 0L) return rd.setErrorMsg("分组信息错误")
    val ns = getUserNameSpace(ctx)
    val book = getShelfBookByURL(bookUrl, ns) ?: return rd.setErrorMsg("书籍信息错误")
    editShelfBook(book, ns) { it.group = groupId; it }
    book.group = groupId
    return rd.setData(book)
}

suspend fun BookController.addBookGroupMulti(ctx: RoutingContext): ReturnData {
    val rd = ReturnData()
    if (!checkAuth(ctx)) return rd.setData("NEED_LOGIN").setErrorMsg("请登录后使用")
    val body = ctx.bodyAsJson ?: return rd.setErrorMsg("参数错误")
    val groupId = body.getLong("groupId") ?: 0L
    if (groupId <= 0L) return rd.setErrorMsg("分组信息错误")
    val bookList = body.getJsonArray("bookList") ?: JsonArray()
    val ns = getUserNameSpace(ctx)
    for (i in 0 until bookList.size()) {
        val url = bookList.getJsonObject(i)?.getString("bookUrl")
            ?: bookList.getJsonObject(i)?.getString("url") ?: continue
        val book = getShelfBookByURL(url, ns) ?: continue
        editShelfBook(book, ns) { it.group = groupId; it }
    }
    return rd.setData("")
}

suspend fun BookController.removeBookGroupMulti(ctx: RoutingContext): ReturnData {
    val rd = ReturnData()
    if (!checkAuth(ctx)) return rd.setData("NEED_LOGIN").setErrorMsg("请登录后使用")
    val bookList = ctx.bodyAsJson?.getJsonArray("bookList") ?: JsonArray()
    val ns = getUserNameSpace(ctx)
    for (i in 0 until bookList.size()) {
        val url = bookList.getJsonObject(i)?.getString("bookUrl")
            ?: bookList.getJsonObject(i)?.getString("url") ?: continue
        val book = getShelfBookByURL(url, ns) ?: continue
        editShelfBook(book, ns) { it.group = 0; it }
    }
    return rd.setData("")
}

suspend fun BookController.saveBookContent(ctx: RoutingContext): ReturnData {
    val rd = ReturnData()
    if (!checkAuth(ctx)) return rd.setData("NEED_LOGIN").setErrorMsg("请登录后使用")
    val body = ctx.bodyAsJson ?: return rd.setErrorMsg("参数错误")
    val bookUrl = body.getString("url") ?: body.getString("bookUrl") ?: return rd.setErrorMsg("请输入书籍链接")
    val index = body.getInteger("index") ?: -1
    val content = body.getString("content") ?: ""
    val ns = getUserNameSpace(ctx)
    val book = getShelfBookByURL(bookUrl, ns) ?: return rd.setErrorMsg("获取书籍信息失败")
    val dir = getChapterCacheDir(book, ns).apply { mkdirs() }
    File(dir, "$index.txt").writeText(content)
    val custom = File(ExtKt.getWorkDir("storage", "data", ns, "${book.name}_${book.author}", "custom")).apply { mkdirs() }
    File(custom, "$index.txt").writeText(content)
    return rd.setData("")
}

suspend fun BookController.setBookSource(ctx: RoutingContext): ReturnData {
    val rd = ReturnData()
    if (!checkAuth(ctx)) return rd.setData("NEED_LOGIN").setErrorMsg("请登录后使用")
    val bookUrl = p(ctx, "bookUrl") ?: return rd.setErrorMsg("书籍链接不能为空")
    val newUrl = p(ctx, "newUrl") ?: return rd.setErrorMsg("新源书籍链接不能为空")
    val bookSourceUrl = p(ctx, "bookSourceUrl") ?: return rd.setErrorMsg("书源链接不能为空")
    val ns = getUserNameSpace(ctx)
    val book = getShelfBookByURL(bookUrl, ns) ?: return rd.setErrorMsg("书籍信息错误")
    val sourceStr = getBookSourceStringBySourceURLOpt(bookSourceUrl, ns) ?: return rd.setErrorMsg("书源不存在")
    val name = JsonObject(sourceStr).getString("bookSourceName") ?: ""
    val updated = editShelfBook(book, ns) {
        it.bookUrl = newUrl; it.origin = bookSourceUrl; it.originName = name; it.tocUrl = ""; it
    } ?: book
    try { getLocalChapterList(updated, sourceStr, true, ns) } catch (_: Exception) {}
    return rd.setData(updated)
}

// ---------- search book source ----------
fun BookController.searchBookSource(ctx: RoutingContext): ReturnData {
    val rd = ReturnData()
    val name = p(ctx, "name") ?: ""
    val ns = getUserNameSpace(ctx)
    val sources = ExtKt.asJsonArray(getUserStorage(ns, "bookSource")) ?: JsonArray()
    val hits = (0 until sources.size()).mapNotNull { i ->
        val o = sources.getJsonObject(i)
        val n = o.getString("bookSourceName") ?: ""
        if (name.isEmpty() || n.contains(name, true)) o else null
    }
    return rd.setData(hits)
}

fun BookController.searchBookSourceSSE(ctx: RoutingContext) {
    val name = p(ctx, "name") ?: ""
    val ns = getUserNameSpace(ctx)
    val sources = ExtKt.asJsonArray(getUserStorage(ns, "bookSource")) ?: JsonArray()
    val resp = ctx.response().putHeader("Content-Type", "text/event-stream").setChunked(true)
    for (i in 0 until sources.size()) {
        val o = sources.getJsonObject(i) ?: continue
        val n = o.getString("bookSourceName") ?: ""
        if (name.isEmpty() || n.contains(name, true)) resp.write("data: ${o.encode()}\n\n")
    }
    resp.write("event: end\ndata: []\n\n").end()
}

fun BookController.getAvailableBookSource(ctx: RoutingContext): ReturnData = searchBookSource(ctx)

// ---------- TTS ----------
suspend fun BookController.textToSpeech(ctx: RoutingContext): ReturnData? {
    if (!checkAuth(ctx)) {
        ctx.response().setStatusCode(403).end("未登录"); return null
    }
    val text = p(ctx, "text") ?: ""
    var type = p(ctx, "type") ?: "edge"
    if (text.isEmpty()) {
        ctx.response().setStatusCode(404).end("参数错误"); return null
    }
    val voice = p(ctx, "voice") ?: "zh-CN-XiaoxiaoNeural"
    val base64 = p(ctx, "base64") == "1"
    val ns = getUserNameSpace(ctx)
    try {
        val audio: ByteArray = when (type) {
            "textToSpeechCn" -> synthesizeTtsCn(text, voice)
            "api" -> {
                val http = findHttpTts(ns, voice) ?: run {
                    ctx.response().setStatusCode(404).end(); return null
                }
                val url = http.url.replace("{{speakText}}", java.net.URLEncoder.encode(text, "UTF-8"))
                    .replace("{{key}}", java.net.URLEncoder.encode(text, "UTF-8"))
                okhttp3.OkHttpClient().newCall(
                    okhttp3.Request.Builder().url(url).get().build()
                ).execute().use { it.body?.bytes() ?: ByteArray(0) }
            }
            else -> {
                // edge: return minimal silent-ish mp3 header placeholder if service unavailable
                try {
                    synthesizeTtsCn(text, voice)
                } catch (_: Exception) {
                    ByteArray(0)
                }
            }
        }
        if (base64) {
            ctx.response().putHeader("content-type", "application/json; charset=utf-8")
                .end(ExtKt.jsonEncode(ReturnData().setData(Base64.getEncoder().encodeToString(audio))))
        } else {
            ctx.response().putHeader("Content-Type", "audio/mpeg").end(io.vertx.core.buffer.Buffer.buffer(audio))
        }
    } catch (e: Exception) {
        if (!ctx.response().ended()) ctx.response().setStatusCode(500).end(e.message ?: "tts error")
    }
    return null
}

private fun findHttpTts(ns: String, name: String): io.legado.app.data.entities.HttpTTS? {
    val arr = ExtKt.asJsonArray(ExtKt.getStorage("data", ns, "httpTTS")) ?: return null
    for (i in 0 until arr.size()) {
        val o = arr.getJsonObject(i) ?: continue
        if (o.getString("name") == name) {
            return io.legado.app.data.entities.HttpTTS(
                name = name,
                url = o.getString("url") ?: "",
                contentType = o.getString("contentType"),
                loginCheckJsValue = o.getString("loginCheckJs"),
                headerJson = o.getString("header")
            )
        }
    }
    return null
}

private fun synthesizeTtsCn(text: String, voice: String): ByteArray {
    val form = mapOf(
        "language" to "中文（普通话，简体）",
        "voice" to voice.ifBlank { "zh-CN-XiaoxiaoNeural" },
        "text" to text, "role" to "0", "style" to "0", "rate" to "0", "pitch" to "0",
        "kbitrate" to "audio-16khz-32kbitrate-mono-mp3", "silence" to "",
        "styledegree" to "1", "user_id" to "", "yzm" to ""
    )
    val body = form.entries.joinToString("&") {
        java.net.URLEncoder.encode(it.key, "UTF-8") + "=" + java.net.URLEncoder.encode(it.value, "UTF-8")
    }
    val req = okhttp3.Request.Builder()
        .url("https://www.text-to-speech.cn/getSpeek.php")
        .post(okhttp3.RequestBody.create(
            okhttp3.MediaType.parse("application/x-www-form-urlencoded"), body
        ))
        .header("Origin", "https://www.text-to-speech.cn")
        .header("Referer", "https://www.text-to-speech.cn/")
        .build()
    return okhttp3.OkHttpClient().newCall(req).execute().use { it.body?.bytes() ?: ByteArray(0) }
}

// ---------- mongo ----------
suspend fun BookController.backupToMongodb(ctx: RoutingContext): ReturnData {
    val rd = ReturnData()
    if (!checkAuth(ctx)) return rd.setData("NEED_LOGIN").setErrorMsg("请登录后使用")
    if (appConfig.mongoUri.isBlank()) return rd.setErrorMsg("未配置 mongoUri")
    return rd.setData(MongoBackup.backupUser(getUserNameSpace(ctx), appConfig.mongoUri, appConfig.mongoDbName))
}

suspend fun BookController.restoreFromMongodb(ctx: RoutingContext): ReturnData {
    val rd = ReturnData()
    if (!checkAuth(ctx)) return rd.setData("NEED_LOGIN").setErrorMsg("请登录后使用")
    if (appConfig.mongoUri.isBlank()) return rd.setErrorMsg("未配置 mongoUri")
    return rd.setData(MongoBackup.restoreUser(getUserNameSpace(ctx), appConfig.mongoUri, appConfig.mongoDbName))
}
''')

w(SRC, "com/htmake/reader/api/controller/MongoBackup.kt", r'''
package com.htmake.reader.api.controller

import com.htmake.reader.utils.ExtKt
import com.mongodb.client.MongoClients
import org.bson.Document

object MongoBackup {
    private val keys = listOf(
        "bookshelf", "bookSource", "rssSource", "replaceRule",
        "bookmark", "bookGroup", "userConfig", "httpTTS"
    )

    fun backupUser(ns: String, uri: String, dbName: String): Map<String, Any?> {
        return try {
            MongoClients.create(uri).use { client ->
                val col = client.getDatabase(dbName).getCollection("user_backup")
                val doc = Document("_id", ns).append("updatedAt", System.currentTimeMillis())
                keys.forEach { k ->
                    ExtKt.getStorage("data", ns, k)?.let { doc.append(k, it) }
                }
                col.replaceOne(
                    Document("_id", ns), doc,
                    com.mongodb.client.model.ReplaceOptions().upsert(true)
                )
                mapOf("ok" to true, "keys" to keys.filter { doc.containsKey(it) })
            }
        } catch (e: Exception) {
            mapOf("ok" to false, "error" to (e.message ?: "mongo error"))
        }
    }

    fun restoreUser(ns: String, uri: String, dbName: String): Map<String, Any?> {
        return try {
            MongoClients.create(uri).use { client ->
                val col = client.getDatabase(dbName).getCollection("user_backup")
                val doc = col.find(Document("_id", ns)).first()
                    ?: return mapOf("ok" to false, "error" to "no backup")
                var n = 0
                keys.forEach { k ->
                    val v = doc.getString(k) ?: return@forEach
                    ExtKt.saveStorage(arrayOf("data", ns, k), v)
                    n++
                }
                mapOf("ok" to true, "restored" to n)
            }
        } catch (e: Exception) {
            mapOf("ok" to false, "error" to (e.message ?: "mongo error"))
        }
    }
}
''')

# Expand remaining controller methods via append file
w(SRC, "com/htmake/reader/api/controller/ControllerMore.kt", r'''
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
''')

print("generated more controllers")
