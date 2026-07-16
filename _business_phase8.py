# -*- coding: utf-8 -*-
"""Phase 8: fill BookController stubs, wire export, RSS engine, shelf cache/group/search."""
from pathlib import Path
import os
import re

BIZ = Path(r"C:\Users\chong\reader-pro-3.2.14-reverse\best-of-3\business")
H = "/** Business rewrite from reader-pro-3.2.14.jar — phase8. */\n\n"


def w(rel, c):
    p = BIZ / rel.replace("/", os.sep)
    p.parent.mkdir(parents=True, exist_ok=True)
    p.write_text(H + c.lstrip("\n"), encoding="utf-8", newline="\n")
    print("wrote", rel, p.stat().st_size)


# ---------------------------------------------------------------------------
# Book entity: group + pdfImageWidth
# ---------------------------------------------------------------------------
book_path = BIZ / "io/legado/app/data/entities/Book.kt"
book_txt = book_path.read_text(encoding="utf-8")
if "pdfImageWidth" not in book_txt:
    book_txt = book_txt.replace(
        "    var charset: String? = null\n) {",
        "    var charset: String? = null,\n"
        "    var group: Long = 0,\n"
        "    var pdfImageWidth: Float = 0f\n) {",
    )
    # bump header
    book_txt = re.sub(
        r"/\*\* Business rewrite.*?phase\d+\. \*/",
        "/** Business rewrite from reader-pro-3.2.14.jar — phase8. */",
        book_txt,
        count=1,
        flags=re.S,
    )
    book_path.write_text(book_txt, encoding="utf-8", newline="\n")
    print("patched Book.kt group/pdfImageWidth")

# ---------------------------------------------------------------------------
# BookController: exportBook + getCachedChapterContentSet
# ---------------------------------------------------------------------------
bc_path = BIZ / "com/htmake/reader/api/controller/BookController.kt"
bc = bc_path.read_text(encoding="utf-8")

old_export = """    suspend fun exportBook(context: RoutingContext): ReturnData {
        val rd = ReturnData()
        if (!checkAuth(context)) return rd.setData("NEED_LOGIN").setErrorMsg("请登录后使用")
        // original: exportToTxt / exportToEpub
        return rd.setData(mapOf("note" to "exportBook — see exportToTxt/exportToEpub in CFR"))
    }"""

new_export = """    suspend fun exportBook(context: RoutingContext): ReturnData =
        BookExport.exportBook(this, context)"""

if "BookExport.exportBook" not in bc:
    if old_export in bc:
        bc = bc.replace(old_export, new_export)
    else:
        bc = re.sub(
            r"suspend fun exportBook\(context: RoutingContext\): ReturnData \{[^}]+\}",
            "suspend fun exportBook(context: RoutingContext): ReturnData =\n"
            "        BookExport.exportBook(this, context)",
            bc,
            count=1,
        )
    print("wired exportBook → BookExport")

if "getCachedChapterContentSet" not in bc:
    helper = """
    fun getCachedChapterContentSet(book: Book, userNameSpace: String): MutableSet<Int> {
        val dir = getChapterCacheDir(book, userNameSpace)
        if (!dir.isDirectory) return linkedSetOf()
        return dir.listFiles()
            ?.mapNotNull { f ->
                if (f.extension.equals("txt", true)) f.nameWithoutExtension.toIntOrNull() else null
            }
            ?.toMutableSet()
            ?: linkedSetOf()
    }

"""
    bc = bc.replace(
        "    fun getChapterCacheDir(book: Book, userNameSpace: String): File {",
        helper + "    fun getChapterCacheDir(book: Book, userNameSpace: String): File {",
    )
    print("added getCachedChapterContentSet")

bc = re.sub(
    r"/\*\* Business rewrite.*?phase\d+\. \*/",
    "/** Business rewrite from reader-pro-3.2.14.jar — phase8. */",
    bc,
    count=1,
    flags=re.S,
) if "Business rewrite" in bc[:200] else bc
# BookController has different header
if "phase8" not in bc[:300]:
    bc = re.sub(
        r"(Business-oriented rewrite from reader-pro-3\.2\.14\.jar reverse engineering\.)",
        r"\1 Phase8: export wired + cache helpers.",
        bc,
        count=1,
    )

bc_path.write_text(bc, encoding="utf-8", newline="\n")
print("patched BookController.kt")

# ---------------------------------------------------------------------------
# BookControllerExtras — full stub fill
# ---------------------------------------------------------------------------
w(
    "com/htmake/reader/api/controller/BookControllerExtras.kt",
    r'''
package com.htmake.reader.api.controller

import com.htmake.reader.api.ReturnData
import com.htmake.reader.utils.ExtKt
import io.legado.app.data.entities.Book
import io.legado.app.data.entities.BookChapter
import io.legado.app.data.entities.BookSource
import io.legado.app.data.entities.SearchBook
import io.legado.app.help.BookHelp
import io.legado.app.model.localBook.LocalBook
import io.legado.app.model.localBook.TextFile
import io.legado.app.model.webBook.WebBook
import io.vertx.core.http.HttpMethod
import io.vertx.core.json.JsonArray
import io.vertx.core.json.JsonObject
import io.vertx.ext.web.RoutingContext
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import kotlinx.coroutines.withTimeoutOrNull
import java.io.File

/**
 * Multi-source search + cache SSE + shelf extras.
 * Defaults from jar:
 * - searchBookMulti concurrentCount default **36**
 * - searchBookMultiSSE / cacheBookSSE concurrentCount default **24**
 * Per-source timeout 15s.
 */

// region ---- multi search ----

suspend fun BookController.exploreBook(context: RoutingContext): ReturnData {
    val rd = ReturnData()
    if (!checkAuth(context)) return rd.setData("NEED_LOGIN").setErrorMsg("请登录后使用")
    val url = context.queryParam("url").firstOrNull()
        ?: context.bodyAsJson?.getString("url")
        ?: return rd.setErrorMsg("url 不能为空")
    val page = context.queryParam("page").firstOrNull()?.toIntOrNull()
        ?: context.bodyAsJson?.getInteger("page") ?: 1
    val ns = getUserNameSpace(context)
    val sourceUrl = context.queryParam("bookSourceUrl").firstOrNull()
        ?: context.bodyAsJson?.getString("bookSourceUrl")
    val sourceStr = sourceUrl?.let { getBookSourceStringBySourceURLOpt(it, ns) }
        ?: return rd.setErrorMsg("书源信息错误")
    val list = withTimeoutOrNull(30_000L) {
        WebBook(sourceStr, getAppConfig().debugLog, null, ns).exploreBook(url, page)
    } ?: emptyList()
    return rd.setData(list)
}

suspend fun BookController.searchBookMulti(context: RoutingContext): ReturnData {
    val rd = ReturnData()
    if (!checkAuth(context)) return rd.setData("NEED_LOGIN").setErrorMsg("请登录后使用")
    val key = paramKey(context) ?: return rd.setErrorMsg("请输入关键字")
    val page = paramInt(context, "page") ?: 1
    val concurrent = (paramInt(context, "concurrentCount") ?: 36).coerceIn(1, 64)
    val ns = getUserNameSpace(context)
    val sources = ExtKt.asJsonArray(getUserStorage(ns, "bookSource")) ?: JsonArray()
    val results = searchMultiInternal(ns, sources, key, page, concurrent, perSourceTimeoutMs = 15_000L)
    return rd.setData(results)
}

suspend fun BookController.searchBookMultiSSE(context: RoutingContext) {
    val key = context.queryParam("key").firstOrNull()
        ?: context.bodyAsJson?.getString("key")
        ?: ""
    val page = context.queryParam("page").firstOrNull()?.toIntOrNull() ?: 1
    val concurrent = (context.queryParam("concurrentCount").firstOrNull()?.toIntOrNull()
        ?: context.bodyAsJson?.getInteger("concurrentCount")
        ?: 24).coerceIn(1, 64)
    val ns = getUserNameSpace(context)
    val sources = ExtKt.asJsonArray(getUserStorage(ns, "bookSource")) ?: JsonArray()
    val enabled = (0 until sources.size()).mapNotNull { i ->
        val o = sources.getJsonObject(i) ?: return@mapNotNull null
        if (o.getBoolean("enabled", true) == false) null else o
    }
    val resp = context.response()
        .putHeader("Content-Type", "text/event-stream; charset=utf-8")
        .putHeader("Cache-Control", "no-cache")
        .setChunked(true)
    coroutineScope {
        enabled.chunked(concurrent).forEach { batch ->
            batch.map { o ->
                async {
                    val src = o.encode()
                    val origin = o.getString("bookSourceUrl") ?: ""
                    try {
                        val list = withTimeoutOrNull(15_000L) {
                            WebBook(src, false, null, ns).searchBook(key, page)
                        } ?: emptyList()
                        val payload = JsonObject()
                            .put("origin", origin)
                            .put("name", o.getString("bookSourceName"))
                            .put("data", JsonArray(list.map { JsonObject.mapFrom(it) }))
                        synchronized(resp) {
                            if (!resp.ended()) resp.write("data: ${payload.encode()}\n\n")
                        }
                    } catch (e: Exception) {
                        val err = JsonObject().put("origin", origin).put("error", e.message)
                        synchronized(resp) {
                            if (!resp.ended()) resp.write("event: error\ndata: ${err.encode()}\n\n")
                        }
                    }
                }
            }.awaitAll()
        }
    }
    if (!resp.ended()) resp.write("event: end\ndata: []\n\n").end()
}

private suspend fun BookController.searchMultiInternal(
    ns: String,
    sources: JsonArray,
    key: String,
    page: Int,
    concurrent: Int,
    perSourceTimeoutMs: Long
): List<SearchBook> = coroutineScope {
    val out = ArrayList<SearchBook>()
    val lock = Mutex()
    val enabled = (0 until sources.size()).mapNotNull { i ->
        val o = sources.getJsonObject(i) ?: return@mapNotNull null
        if (o.getBoolean("enabled", true) == false) null else o
    }
    enabled.chunked(concurrent).forEach { batch ->
        batch.map { o ->
            async {
                try {
                    withTimeoutOrNull(perSourceTimeoutMs) {
                        WebBook(o.encode(), false, null, ns).searchBook(key, page)
                    } ?: emptyList()
                } catch (_: Exception) {
                    emptyList()
                }
            }
        }.awaitAll().forEach { list ->
            lock.withLock { out.addAll(list) }
        }
    }
    out.distinctBy { it.bookUrl.ifEmpty { "${it.name}|${it.author}|${it.origin}" } }
}

// endregion

// region ---- book source helpers ----

fun BookController.searchBookSource(context: RoutingContext): ReturnData {
    val rd = ReturnData()
    val name = context.queryParam("name").firstOrNull()
        ?: context.bodyAsJson?.getString("name") ?: ""
    val ns = getUserNameSpace(context)
    val sources = ExtKt.asJsonArray(getUserStorage(ns, "bookSource")) ?: JsonArray()
    val hits = (0 until sources.size()).mapNotNull { i ->
        val o = sources.getJsonObject(i)
        val n = o.getString("bookSourceName") ?: ""
        if (name.isEmpty() || n.contains(name, true)) o else null
    }
    return rd.setData(hits)
}

fun BookController.searchBookSourceSSE(context: RoutingContext) {
    val name = context.queryParam("name").firstOrNull()
        ?: context.bodyAsJson?.getString("name") ?: ""
    val ns = getUserNameSpace(context)
    val sources = ExtKt.asJsonArray(getUserStorage(ns, "bookSource")) ?: JsonArray()
    val resp = context.response()
        .putHeader("Content-Type", "text/event-stream; charset=utf-8")
        .putHeader("Cache-Control", "no-cache")
        .setChunked(true)
    for (i in 0 until sources.size()) {
        val o = sources.getJsonObject(i) ?: continue
        val n = o.getString("bookSourceName") ?: ""
        if (name.isEmpty() || n.contains(name, true)) {
            resp.write("data: ${o.encode()}\n\n")
        }
    }
    resp.write("event: end\ndata: []\n\n").end()
}

fun BookController.getAvailableBookSource(context: RoutingContext): ReturnData =
    searchBookSource(context)

suspend fun BookController.setBookSource(context: RoutingContext): ReturnData {
    val rd = ReturnData()
    if (!checkAuth(context)) return rd.setData("NEED_LOGIN").setErrorMsg("请登录后使用")
    val bookUrl = pStr(context, "bookUrl") ?: return rd.setErrorMsg("书籍链接不能为空")
    val newUrl = pStr(context, "newUrl") ?: return rd.setErrorMsg("新源书籍链接不能为空")
    val bookSourceUrl = pStr(context, "bookSourceUrl") ?: return rd.setErrorMsg("书源链接不能为空")
    val ns = getUserNameSpace(context)
    val book = getShelfBookByURL(bookUrl, ns) ?: return rd.setErrorMsg("书籍信息错误")
    val sourceStr = getBookSourceStringBySourceURLOpt(bookSourceUrl, ns)
        ?: return rd.setErrorMsg("书源不存在")
    val updated = editShelfBook(book, ns) { exist ->
        exist.bookUrl = newUrl
        exist.origin = bookSourceUrl
        exist.originName = JsonObject(sourceStr).getString("bookSourceName") ?: exist.originName
        exist.tocUrl = ""
        exist
    } ?: book
    // refresh chapter list under new source
    try {
        getLocalChapterList(updated, sourceStr, true, ns, false, null)
    } catch (_: Exception) {
    }
    return rd.setData(updated)
}

fun BookController.getInvalidBookSources(context: RoutingContext): ReturnData {
    val rd = ReturnData()
    if (!checkAuth(context)) return rd.setData("NEED_LOGIN").setErrorMsg("请登录后使用")
    val ns = getUserNameSpace(context)
    val dir = File(ExtKt.getWorkDir("storage", "cache", "invalidBookSourceCache", ns))
    if (!dir.isDirectory) return rd.setData(emptyList<Any>())
    val list = dir.listFiles()?.mapNotNull { f ->
        try {
            JsonObject(f.readText())
        } catch (_: Exception) {
            null
        }
    } ?: emptyList()
    return rd.setData(list)
}

// endregion

// region ---- local toc / cache / content ----

fun BookController.getTxtTocRules(context: RoutingContext): ReturnData {
    return ReturnData().setData(io.legado.app.help.DefaultData.txtTocRules)
}

suspend fun BookController.getChapterListByRule(context: RoutingContext): ReturnData {
    val rd = ReturnData()
    if (!checkAuth(context)) return rd.setData("NEED_LOGIN").setErrorMsg("请登录后使用")
    val body = context.bodyAsJson ?: return rd.setErrorMsg("参数错误")
    val book = try {
        body.mapTo(Book::class.java)
    } catch (_: Exception) {
        return rd.setErrorMsg("书籍信息错误")
    }
    if (book.origin.isEmpty() && !book.isLocalBook) {
        return rd.setErrorMsg("未找到书源信息")
    }
    if (!book.isLocalTxt && !book.isEpub && !book.isPdf && !book.isLocalBook) {
        return rd.setErrorMsg("非本地txt/epub/pdf书籍")
    }
    val ns = getUserNameSpace(context)
    book.rootDir = ExtKt.getWorkDir()
    book.userNameSpace = ns
    // optional custom rule for txt
    body.getString("rule")?.takeIf { it.isNotBlank() }?.let { book.tocUrl = it }
    val chapters = LocalBook.getChapterList(book)
    return rd.setData(mapOf("book" to book, "chapters" to chapters))
}

/**
 * cacheBookSSE — concurrent chapter cache with SSE progress.
 * Params: url/bookUrl, refresh=0|1, concurrentCount default 24.
 * Events: data progress, event:error, event:end
 */
suspend fun BookController.cacheBookSSE(context: RoutingContext) {
    val resp = context.response()
        .putHeader("Content-Type", "text/event-stream; charset=utf-8")
        .putHeader("Cache-Control", "no-cache")
        .setChunked(true)
    val rd = ReturnData()
    if (!checkAuth(context)) {
        resp.write("event: error\n")
        resp.end("data: ${rd.setData("NEED_LOGIN").setErrorMsg("请登录后使用").toJson()}\n\n")
        return
    }
    val bookUrl = pStr(context, "url") ?: pStr(context, "bookUrl") ?: ""
    val refresh = pInt(context, "refresh") ?: 0
    val concurrent = (pInt(context, "concurrentCount") ?: 24).coerceIn(1, 64)
    if (bookUrl.isEmpty()) {
        resp.write("event: error\n")
        resp.end("data: ${rd.setErrorMsg("请输入书籍链接").toJson()}\n\n")
        return
    }
    val ns = getUserNameSpace(context)
    val book = getShelfBookByURL(bookUrl, ns)
    if (book == null) {
        resp.write("event: error\n")
        resp.end("data: ${rd.setErrorMsg("请先加入书架").toJson()}\n\n")
        return
    }
    if (book.isLocalBook) {
        resp.write("event: error\n")
        resp.end("data: ${rd.setErrorMsg("本地书籍无需缓存").toJson()}\n\n")
        return
    }
    val source = getBookSourceString(book, ns)
    if (source.isNullOrEmpty()) {
        resp.write("event: error\n")
        resp.end("data: ${rd.setErrorMsg("未配置书源").toJson()}\n\n")
        return
    }
    val chapters = getLocalChapterList(book, source, false, ns, false, null)
    val cached: MutableSet<Int> =
        if (refresh <= 0) getCachedChapterContentSet(book, ns) else linkedSetOf()
    val cacheDir = getChapterCacheDir(book, ns).apply { mkdirs() }
    var success = 0
    var failed = 0
    val total = chapters.size
    val pending = chapters.indices.filter { it !in cached }
    coroutineScope {
        pending.chunked(concurrent).forEach { batch ->
            batch.map { idx ->
                async {
                    val ch = chapters[idx]
                    val next = chapters.getOrNull(idx + 1)?.url
                    try {
                        val content = WebBook(source, getAppConfig().debugLog, null, ns)
                            .getBookContent(book, ch, next)
                        File(cacheDir, "$idx.txt").writeText(content)
                        val src = BookSource.fromJson(source).getOrNull() ?: BookSource()
                        BookHelp.saveImages(this@async, src, book, ch, content)
                        synchronized(cached) {
                            cached += idx
                            success++
                        }
                        val progress = JsonObject()
                            .put("index", idx)
                            .put("title", ch.title)
                            .put("success", success)
                            .put("failed", failed)
                            .put("cached", cached.size)
                            .put("total", total)
                        synchronized(resp) {
                            if (!resp.ended()) resp.write("data: ${progress.encode()}\n\n")
                        }
                    } catch (e: Exception) {
                        synchronized(cached) { failed++ }
                        val err = JsonObject()
                            .put("index", idx)
                            .put("title", ch.title)
                            .put("error", e.message)
                            .put("success", success)
                            .put("failed", failed)
                        synchronized(resp) {
                            if (!resp.ended()) resp.write("event: error\ndata: ${err.encode()}\n\n")
                        }
                    }
                }
            }.awaitAll()
        }
    }
    val end = JsonObject()
        .put("success", success)
        .put("failed", failed)
        .put("cached", cached.size)
        .put("total", total)
    if (!resp.ended()) resp.write("event: end\ndata: ${end.encode()}\n\n").end()
}

suspend fun BookController.getShelfBookWithCacheInfo(context: RoutingContext): ReturnData {
    val rd = ReturnData()
    if (!checkAuth(context)) return rd.setData("NEED_LOGIN").setErrorMsg("请登录后使用")
    val ns = getUserNameSpace(context)
    val books = getBookShelfBooks(false, ns)
    val result = books.map { book ->
        if (book.isLocalBook) {
            book
        } else {
            val cached = getCachedChapterContentSet(book, ns)
            // expose as map with extra field (json-friendly)
            mapOf(
                "bookUrl" to book.bookUrl,
                "name" to book.name,
                "author" to book.author,
                "origin" to book.origin,
                "originName" to book.originName,
                "coverUrl" to book.coverUrl,
                "durChapterIndex" to book.durChapterIndex,
                "durChapterTitle" to book.durChapterTitle,
                "totalChapterNum" to book.totalChapterNum,
                "latestChapterTitle" to book.latestChapterTitle,
                "group" to book.group,
                "canUpdate" to book.canUpdate,
                "isInShelf" to true,
                "cachedChapterCount" to cached.size
            )
        }
    }
    return rd.setData(result)
}

suspend fun BookController.deleteBookCache(context: RoutingContext): ReturnData {
    val rd = ReturnData()
    if (!checkAuth(context)) return rd.setData("NEED_LOGIN").setErrorMsg("请登录后使用")
    val bookUrl = pStr(context, "url") ?: pStr(context, "bookUrl") ?: ""
    if (bookUrl.isEmpty()) return rd.setErrorMsg("请输入书籍链接")
    val ns = getUserNameSpace(context)
    val book = getShelfBookByURL(bookUrl, ns) ?: return rd.setErrorMsg("请先加入书架")
    if (book.isLocalBook) return rd.setErrorMsg("本地书籍无需删除缓存")
    ExtKt.deleteRecursively(getChapterCacheDir(book, ns))
    return rd.setData("")
}

suspend fun BookController.searchBookContent(context: RoutingContext): ReturnData {
    val rd = ReturnData()
    if (!checkAuth(context)) return rd.setData("NEED_LOGIN").setErrorMsg("请登录后使用")
    val bookUrl = pStr(context, "url") ?: pStr(context, "bookUrl") ?: ""
    val keyword = pStr(context, "keyword") ?: ""
    val lastIndex = pInt(context, "lastIndex") ?: 0
    val size = pInt(context, "size") ?: 20
    if (bookUrl.isEmpty()) return rd.setErrorMsg("请输入书籍链接")
    if (keyword.isEmpty()) return rd.setErrorMsg("请输入搜索关键词")
    val ns = getUserNameSpace(context)
    val book = getShelfBookByURL(bookUrl, ns) ?: return rd.setErrorMsg("请先加入书架")
    val source = if (book.isLocalBook) null else getBookSourceString(book, ns)
    if (!book.isLocalBook && source.isNullOrEmpty()) return rd.setErrorMsg("未配置书源")
    val chapters = getLocalChapterList(book, source, false, ns, false, null)
    if (lastIndex >= chapters.size) return rd.setErrorMsg("没有更多了")
    val hits = ArrayList<Map<String, Any?>>()
    var currentIndex = lastIndex
    var i = lastIndex + 1
    while (i < chapters.size && hits.size < size) {
        val ch = chapters[i]
        currentIndex = i
        val text = try {
            readChapterText(book, source, ns, ch, chapters.getOrNull(i + 1)?.url)
        } catch (_: Exception) {
            ""
        }
        if (text.contains(keyword, ignoreCase = true)) {
            // collect snippet windows
            var from = 0
            val lower = text.lowercase()
            val keyLower = keyword.lowercase()
            while (true) {
                val pos = lower.indexOf(keyLower, from)
                if (pos < 0) break
                val start = (pos - 20).coerceAtLeast(0)
                val end = (pos + keyword.length + 40).coerceAtMost(text.length)
                hits.add(
                    mapOf(
                        "chapterIndex" to i,
                        "chapterTitle" to ch.title,
                        "resultCount" to 1,
                        "resultText" to text.substring(start, end),
                        "chapterUrl" to ch.url
                    )
                )
                if (hits.size >= size) break
                from = pos + keyword.length
            }
        }
        i++
    }
    return rd.setData(mapOf("list" to hits, "lastIndex" to currentIndex))
}

private suspend fun BookController.readChapterText(
    book: Book,
    source: String?,
    ns: String,
    ch: BookChapter,
    nextUrl: String?
): String {
    val idx = ch.index
    val cache = File(getChapterCacheDir(book, ns), "$idx.txt")
    if (cache.isFile) return cache.readText()
    return if (book.isLocalBook) {
        LocalBook.getContent(book, ch) ?: ""
    } else {
        WebBook(source!!, false, null, ns).getBookContent(book, ch, nextUrl)
    }
}

suspend fun BookController.saveBookContent(context: RoutingContext): ReturnData {
    val rd = ReturnData()
    if (!checkAuth(context)) return rd.setData("NEED_LOGIN").setErrorMsg("请登录后使用")
    val body = context.bodyAsJson ?: return rd.setErrorMsg("参数错误")
    val bookUrl = body.getString("url") ?: body.getString("bookUrl") ?: ""
    val index = body.getInteger("index") ?: -1
    val content = body.getString("content") ?: ""
    if (bookUrl.isEmpty()) return rd.setErrorMsg("请输入书籍链接")
    val ns = getUserNameSpace(context)
    val book = getShelfBookByURL(bookUrl, ns) ?: return rd.setErrorMsg("获取书籍信息失败")
    val cacheDir = getChapterCacheDir(book, ns).apply { mkdirs() }
    File(cacheDir, "$index.txt").writeText(content)
    val custom = File(
        ExtKt.getWorkDir("storage", "data", ns, "${book.name}_${book.author}", "custom")
    ).apply { mkdirs() }
    File(custom, "$index.txt").writeText(content)
    return rd.setData("")
}

suspend fun BookController.saveBookConfig(context: RoutingContext): ReturnData {
    val rd = ReturnData()
    if (!checkAuth(context)) return rd.setData("NEED_LOGIN").setErrorMsg("请登录后使用")
    val bookUrl = pStr(context, "bookUrl") ?: return rd.setErrorMsg("书籍链接不能为空")
    val pdfImageWidth = pFloat(context, "pdfImageWidth") ?: 0f
    if (pdfImageWidth <= 0f) return rd.setErrorMsg("pdf图片宽度错误")
    val ns = getUserNameSpace(context)
    val book = getShelfBookByURL(bookUrl, ns) ?: return rd.setErrorMsg("书籍信息错误")
    val updated = editShelfBook(book, ns) { exist ->
        exist.pdfImageWidth = pdfImageWidth
        exist
    }
    return rd.setData(updated ?: book)
}

suspend fun BookController.saveBookGroupId(context: RoutingContext): ReturnData {
    val rd = ReturnData()
    if (!checkAuth(context)) return rd.setData("NEED_LOGIN").setErrorMsg("请登录后使用")
    val bookUrl = pStr(context, "bookUrl") ?: return rd.setErrorMsg("书籍链接不能为空")
    val groupId = pLong(context, "groupId") ?: 0L
    if (groupId <= 0L) return rd.setErrorMsg("分组信息错误")
    val ns = getUserNameSpace(context)
    val book = getShelfBookByURL(bookUrl, ns) ?: return rd.setErrorMsg("书籍信息错误")
    editShelfBook(book, ns) { exist ->
        exist.group = groupId
        exist
    }
    book.group = groupId
    return rd.setData(book)
}

suspend fun BookController.addBookGroupMulti(context: RoutingContext): ReturnData {
    val rd = ReturnData()
    if (!checkAuth(context)) return rd.setData("NEED_LOGIN").setErrorMsg("请登录后使用")
    val body = context.bodyAsJson ?: return rd.setErrorMsg("参数错误")
    val groupId = body.getLong("groupId") ?: 0L
    if (groupId <= 0L) return rd.setErrorMsg("分组信息错误")
    val bookList = body.getJsonArray("bookList") ?: JsonArray()
    val ns = getUserNameSpace(context)
    for (i in 0 until bookList.size()) {
        val item = bookList.getJsonObject(i) ?: continue
        val url = item.getString("bookUrl") ?: item.getString("url") ?: continue
        val book = getShelfBookByURL(url, ns) ?: continue
        editShelfBook(book, ns) { exist ->
            exist.group = groupId
            exist
        }
    }
    return rd.setData("")
}

suspend fun BookController.removeBookGroupMulti(context: RoutingContext): ReturnData {
    val rd = ReturnData()
    if (!checkAuth(context)) return rd.setData("NEED_LOGIN").setErrorMsg("请登录后使用")
    val body = context.bodyAsJson ?: return rd.setErrorMsg("参数错误")
    val bookList = body.getJsonArray("bookList") ?: JsonArray()
    val ns = getUserNameSpace(context)
    for (i in 0 until bookList.size()) {
        val item = bookList.getJsonObject(i) ?: continue
        val url = item.getString("bookUrl") ?: item.getString("url") ?: continue
        val book = getShelfBookByURL(url, ns) ?: continue
        editShelfBook(book, ns) { exist ->
            exist.group = 0
            exist
        }
    }
    return rd.setData("")
}

fun BookController.tts(context: RoutingContext): ReturnData =
    ReturnData().setData(mapOf("note" to "stream audio via getSpeakStream / textToSpeech"))

fun BookController.backupToMongodb(context: RoutingContext): ReturnData {
    val rd = ReturnData()
    val uri = getAppConfig().mongoUri
    if (uri.isBlank()) return rd.setErrorMsg("未配置 mongoUri")
    return rd.setData(MongoBackup.backupUser(getUserNameSpace(context), uri, getAppConfig().mongoDbName))
}

fun BookController.restoreFromMongodb(context: RoutingContext): ReturnData {
    val rd = ReturnData()
    val uri = getAppConfig().mongoUri
    if (uri.isBlank()) return rd.setErrorMsg("未配置 mongoUri")
    return rd.setData(MongoBackup.restoreUser(getUserNameSpace(context), uri, getAppConfig().mongoDbName))
}

// endregion

// region ---- param helpers ----

private fun paramKey(context: RoutingContext): String? =
    context.queryParam("key").firstOrNull() ?: context.bodyAsJson?.getString("key")

private fun paramInt(context: RoutingContext, name: String): Int? =
    context.queryParam(name).firstOrNull()?.toIntOrNull()
        ?: context.bodyAsJson?.getInteger(name)

private fun pStr(context: RoutingContext, name: String): String? {
    if (context.request().method() == HttpMethod.POST) {
        context.bodyAsJson?.getString(name)?.let { return it }
    }
    return context.queryParam(name).firstOrNull()
}

private fun pInt(context: RoutingContext, name: String): Int? {
    if (context.request().method() == HttpMethod.POST) {
        context.bodyAsJson?.getInteger(name)?.let { return it }
    }
    return context.queryParam(name).firstOrNull()?.toIntOrNull()
}

private fun pLong(context: RoutingContext, name: String): Long? {
    if (context.request().method() == HttpMethod.POST) {
        context.bodyAsJson?.getLong(name)?.let { return it }
    }
    return context.queryParam(name).firstOrNull()?.toLongOrNull()
}

private fun pFloat(context: RoutingContext, name: String): Float? {
    if (context.request().method() == HttpMethod.POST) {
        context.bodyAsJson?.getFloat(name)?.let { return it }
    }
    return context.queryParam(name).firstOrNull()?.toFloatOrNull()
}

/** Serialize ReturnData for SSE — prefer encode if present. */
private fun ReturnData.toJson(): String = try {
    JsonObject.mapFrom(this).encode()
} catch (_: Exception) {
    """{"isSuccess":false,"errorMsg":"${this}"}"""
}

// endregion
''',
)

# ---------------------------------------------------------------------------
# RSS entities + parser
# ---------------------------------------------------------------------------
w(
    "io/legado/app/data/entities/RssSource.kt",
    r'''
package io.legado.app.data.entities

/**
 * RSS 源（legado 字段子集，业务可读版）
 */
data class RssSource(
    var sourceUrl: String = "",
    var sourceName: String = "",
    var sourceIcon: String = "",
    var sourceGroup: String? = null,
    var sourceComment: String? = null,
    var enabled: Boolean = true,
    var header: String? = null,
    var sortUrl: String? = null,
    var singleUrl: Boolean = false,
    var ruleArticles: String? = null,
    var ruleNextPage: String? = null,
    var ruleTitle: String? = null,
    var rulePubDate: String? = null,
    var ruleDescription: String? = null,
    var ruleImage: String? = null,
    var ruleLink: String? = null,
    var ruleContent: String? = null,
    var enableJs: Boolean = true,
    var loadWithBaseUrl: Boolean = true,
    var customOrder: Int = 0
) : BaseSource {
    private var _userNameSpace: String = "default"

    override fun getKey(): String = sourceUrl
    override fun getTag(): String = sourceName
    override fun getHeaderMap(hasLoginHeader: Boolean): Map<String, String> {
        // header may be JSON object string
        val h = header ?: return emptyMap()
        return try {
            val o = io.vertx.core.json.JsonObject(h)
            o.map.mapValues { it.value?.toString() ?: "" }
        } catch (_: Exception) {
            emptyMap()
        }
    }
    override fun getUserNameSpace(): String = _userNameSpace
    fun setUserNameSpace(ns: String) { _userNameSpace = ns }
}
''',
)

w(
    "io/legado/app/data/entities/RssArticle.kt",
    r'''
package io.legado.app.data.entities

data class RssArticle(
    var origin: String = "",
    var sort: String = "",
    var title: String = "",
    var order: Long = 0,
    var link: String = "",
    var pubDate: String? = null,
    var description: String? = null,
    var content: String? = null,
    var image: String? = null,
    var read: Boolean = false,
    var variable: String? = null
)
''',
)

# Check BaseSource interface
base_src = BIZ / "io/legado/app/data/entities"
# may not exist as separate file — check BookSource
bs = (BIZ / "io/legado/app/data/entities/BookSource.kt").read_text(encoding="utf-8")
if "interface BaseSource" not in bs and not (BIZ / "io/legado/app/data/entities/BaseSource.kt").exists():
    w(
        "io/legado/app/data/entities/BaseSource.kt",
        r'''
package io.legado.app.data.entities

interface BaseSource {
    fun getKey(): String
    fun getTag(): String
    fun getHeaderMap(hasLoginHeader: Boolean = false): Map<String, String> = emptyMap()
    fun getUserNameSpace(): String = "default"
}
''',
    )

w(
    "io/legado/app/model/rss/Rss.kt",
    r'''
package io.legado.app.model.rss

import io.legado.app.data.entities.RssArticle
import io.legado.app.data.entities.RssSource
import io.legado.app.model.DebugLog
import io.legado.app.model.analyzeRule.AnalyzeRule
import io.legado.app.model.analyzeRule.AnalyzeUrl
import org.jsoup.Jsoup
import org.jsoup.parser.Parser

/**
 * RSS 拉文：有 ruleArticles 走 AnalyzeRule；否则按标准 RSS/Atom XML 解析。
 */
object Rss {

    suspend fun getArticles(
        sortName: String,
        sortUrl: String,
        rssSource: RssSource,
        page: Int,
        debugLog: DebugLog? = null
    ): Pair<MutableList<RssArticle>, String?> {
        val url = sortUrl.ifBlank { rssSource.sourceUrl }
        val analyzeUrl = AnalyzeUrl(
            mUrl = url,
            page = page,
            source = rssSource,
            headerMapF = rssSource.getHeaderMap(true),
            debugLog = debugLog
        )
        val body = analyzeUrl.getStrResponseAwait().body ?: ""
        return if (!rssSource.ruleArticles.isNullOrBlank()) {
            parseByRule(sortName, url, body, rssSource, debugLog)
        } else {
            parseDefaultXml(sortName, url, body, rssSource) to null
        }
    }

    suspend fun getContent(
        rssArticle: RssArticle,
        ruleContent: String,
        rssSource: RssSource,
        debugLog: DebugLog? = null
    ): String {
        if (ruleContent.isBlank()) return rssArticle.description ?: rssArticle.content ?: ""
        val analyzeUrl = AnalyzeUrl(
            mUrl = rssArticle.link,
            source = rssSource,
            headerMapF = rssSource.getHeaderMap(true),
            debugLog = debugLog
        )
        val body = analyzeUrl.getStrResponseAwait().body ?: ""
        val rule = AnalyzeRule(null, rssSource, debugLog)
        rule.setContent(body, rssArticle.link)
        return rule.getString(ruleContent)
    }

    private fun parseByRule(
        sortName: String,
        sortUrl: String,
        body: String,
        source: RssSource,
        debugLog: DebugLog?
    ): Pair<MutableList<RssArticle>, String?> {
        val rule = AnalyzeRule(null, source, debugLog)
        rule.setContent(body, sortUrl)
        val elements = rule.getStringList(source.ruleArticles!!)
        val list = ArrayList<RssArticle>()
        var order = System.currentTimeMillis()
        for (el in elements) {
            // when rule returns HTML snippets, re-parse fields from each
            val sub = AnalyzeRule(null, source, debugLog).setContent(el, sortUrl)
            val title = source.ruleTitle?.let { sub.getString(it) }.orEmpty()
            val link = source.ruleLink?.let { sub.getString(it, isUrl = true) }.orEmpty()
            if (title.isEmpty() && link.isEmpty()) continue
            list.add(
                RssArticle(
                    origin = source.sourceUrl,
                    sort = sortName,
                    title = title.ifEmpty { link },
                    order = order--,
                    link = link.ifEmpty { sortUrl },
                    pubDate = source.rulePubDate?.let { sub.getString(it) },
                    description = source.ruleDescription?.let { sub.getString(it) },
                    image = source.ruleImage?.let { sub.getString(it, isUrl = true) }
                )
            )
        }
        val next = source.ruleNextPage?.let { rule.getString(it, isUrl = true) }?.takeIf { it.isNotBlank() }
        return list to next
    }

    private fun parseDefaultXml(
        sortName: String,
        sortUrl: String,
        body: String,
        source: RssSource
    ): MutableList<RssArticle> {
        val list = ArrayList<RssArticle>()
        try {
            val doc = Jsoup.parse(body, sortUrl, Parser.xmlParser())
            // RSS 2.0 item
            val items = doc.select("item")
            if (items.isNotEmpty()) {
                var order = System.currentTimeMillis()
                for (item in items) {
                    list.add(
                        RssArticle(
                            origin = source.sourceUrl,
                            sort = sortName,
                            title = item.selectFirst("title")?.text().orEmpty(),
                            order = order--,
                            link = item.selectFirst("link")?.text()
                                ?: item.selectFirst("link")?.attr("href").orEmpty(),
                            pubDate = item.selectFirst("pubDate")?.text(),
                            description = item.selectFirst("description")?.html()
                                ?: item.selectFirst("content|encoded")?.html(),
                            image = item.selectFirst("enclosure[url]")?.attr("url")
                                ?: item.selectFirst("media|content")?.attr("url")
                        )
                    )
                }
                return list
            }
            // Atom entry
            for (entry in doc.select("entry")) {
                list.add(
                    RssArticle(
                        origin = source.sourceUrl,
                        sort = sortName,
                        title = entry.selectFirst("title")?.text().orEmpty(),
                        order = System.currentTimeMillis(),
                        link = entry.selectFirst("link[href]")?.attr("href")
                            ?: entry.selectFirst("link")?.text().orEmpty(),
                        pubDate = entry.selectFirst("updated")?.text()
                            ?: entry.selectFirst("published")?.text(),
                        description = entry.selectFirst("summary")?.html()
                            ?: entry.selectFirst("content")?.html()
                    )
                )
            }
        } catch (_: Exception) {
        }
        return list
    }
}
''',
)

# Check if AnalyzeRule has getStringList
ar = (BIZ / "io/legado/app/model/analyzeRule/AnalyzeRule.kt").read_text(encoding="utf-8")
if "fun getStringList" not in ar:
    # inject simple getStringList after getString
    insert = '''
    fun getStringList(ruleStr: String?, mContent: Any? = null, isUrl: Boolean = false): List<String> {
        if (ruleStr.isNullOrEmpty()) return emptyList()
        // Prefer element list for CSS; fallback split lines
        return try {
            val rules = splitSourceRule(ruleStr)
            var result: Any? = mContent ?: content
            for (rule in rules) {
                result = when (rule.mode) {
                    Mode.Js -> evalJS(rule.rule, result)
                    Mode.Json -> jsonPath().getStringList(result, rule.rule)
                    Mode.XPath -> xPath().getStringList(result, rule.rule)
                    Mode.Default -> jsoup().getStringList(result, rule.rule)
                    else -> listOf(getString(ruleStr, mContent, isUrl))
                }
            }
            when (result) {
                is List<*> -> result.mapNotNull { it?.toString() }.filter { it.isNotEmpty() }
                is String -> if (result.isNotEmpty()) listOf(result) else emptyList()
                else -> emptyList()
            }
        } catch (_: Exception) {
            emptyList()
        }
    }

'''
    ar = ar.replace(
        "    fun getString(ruleStr: String?, mContent: Any? = null, isUrl: Boolean = false): String {",
        insert + "    fun getString(ruleStr: String?, mContent: Any? = null, isUrl: Boolean = false): String {",
    )
    (BIZ / "io/legado/app/model/analyzeRule/AnalyzeRule.kt").write_text(ar, encoding="utf-8", newline="\n")
    print("added AnalyzeRule.getStringList")

# Patch jsoup/xpath/json for getStringList if missing — soft stubs via extensions in same file is hard;
# check AnalyzeByJSoup
for rel, method_body in [
    (
        "io/legado/app/model/analyzeRule/AnalyzeByJSoup.kt",
        "fun getStringList",
    ),
    (
        "io/legado/app/model/analyzeRule/AnalyzeByXPath.kt",
        "fun getStringList",
    ),
    (
        "io/legado/app/model/analyzeRule/AnalyzeByJSonPath.kt",
        "fun getStringList",
    ),
]:
    p = BIZ / rel
    t = p.read_text(encoding="utf-8")
    if "fun getStringList" not in t:
        # append method before last closing brace of class
        stub = '''
    fun getStringList(content: Any?, rule: String): List<String> {
        val s = getString(content, rule)
        if (s.isBlank()) return emptyList()
        // CSS multi-match: try elements text
        return try {
            val doc = when (content) {
                is org.jsoup.nodes.Element -> content
                is String -> org.jsoup.Jsoup.parse(content)
                else -> return listOf(s)
            }
            val els = doc.select(rule.substringBefore("@"))
            if (els.isEmpty()) listOf(s) else els.map { it.outerHtml() }
        } catch (_: Exception) {
            listOf(s)
        }
    }
'''
        # insert before last }
        idx = t.rfind("}")
        if idx > 0:
            t = t[:idx] + stub + t[idx:]
            p.write_text(t, encoding="utf-8", newline="\n")
            print("added getStringList to", rel)

# ---------------------------------------------------------------------------
# RssSourceController rewrite
# ---------------------------------------------------------------------------
w(
    "com/htmake/reader/api/controller/RssSourceController.kt",
    r'''
package com.htmake.reader.api.controller

import com.htmake.reader.api.ReturnData
import com.htmake.reader.utils.ExtKt
import io.legado.app.data.entities.RssArticle
import io.legado.app.data.entities.RssSource
import io.legado.app.model.rss.Rss
import io.vertx.core.http.HttpMethod
import io.vertx.core.json.JsonArray
import io.vertx.core.json.JsonObject
import io.vertx.ext.web.RoutingContext
import kotlin.coroutines.CoroutineContext

class RssSourceController(coroutineContext: CoroutineContext) : BaseController(coroutineContext) {
    private fun load(ns: String) = ExtKt.asJsonArray(getUserStorage(ns, "rssSource")) ?: JsonArray()
    private fun save(ns: String, a: JsonArray) = saveUserStorage(ns, "rssSource", a)

    fun getRssSourceByURL(url: String, userNameSpace: String): RssSource? {
        val arr = load(userNameSpace)
        for (i in 0 until arr.size()) {
            val o = arr.getJsonObject(i) ?: continue
            val u = o.getString("sourceUrl") ?: o.getString("rssUrl")
            if (u == url) {
                return try {
                    o.mapTo(RssSource::class.java).also { it.setUserNameSpace(userNameSpace) }
                } catch (_: Exception) {
                    RssSource(
                        sourceUrl = u ?: "",
                        sourceName = o.getString("sourceName") ?: "",
                        ruleArticles = o.getString("ruleArticles"),
                        ruleTitle = o.getString("ruleTitle"),
                        ruleLink = o.getString("ruleLink"),
                        rulePubDate = o.getString("rulePubDate"),
                        ruleDescription = o.getString("ruleDescription"),
                        ruleImage = o.getString("ruleImage"),
                        ruleContent = o.getString("ruleContent"),
                        ruleNextPage = o.getString("ruleNextPage"),
                        header = o.getString("header"),
                        sortUrl = o.getString("sortUrl")
                    ).also { it.setUserNameSpace(userNameSpace) }
                }
            }
        }
        return null
    }

    suspend fun getRssSources(context: RoutingContext): ReturnData {
        val rd = ReturnData()
        if (!checkAuth(context)) return rd.setData("NEED_LOGIN").setErrorMsg("请登录后使用")
        return rd.setData(load(getUserNameSpace(context)))
    }

    suspend fun saveRssSource(context: RoutingContext): ReturnData {
        val rd = ReturnData()
        if (!checkAuth(context)) return rd.setData("NEED_LOGIN").setErrorMsg("请登录后使用")
        val ns = getUserNameSpace(context)
        val bodyArr = context.bodyAsJsonArray
        if (bodyArr != null) {
            save(ns, bodyArr)
            return rd.setData(bodyArr.size())
        }
        val src = context.bodyAsJson ?: return rd.setErrorMsg("参数错误")
        val arr = load(ns)
        val key = src.getString("sourceUrl") ?: src.getString("rssUrl") ?: return rd.setErrorMsg("链接不能为空")
        val list = arr.list
        var found = false
        for (i in list.indices) {
            val o = arr.getJsonObject(i)
            val k = o.getString("sourceUrl") ?: o.getString("rssUrl")
            if (k == key) {
                list[i] = src
                found = true
                break
            }
        }
        if (!found) list.add(src)
        save(ns, JsonArray(list))
        return rd.setData(src)
    }

    suspend fun deleteRssSource(context: RoutingContext): ReturnData {
        val rd = ReturnData()
        if (!checkAuth(context)) return rd.setData("NEED_LOGIN").setErrorMsg("请登录后使用")
        val ns = getUserNameSpace(context)
        val key = context.bodyAsJson?.getString("sourceUrl")
            ?: context.queryParam("sourceUrl").firstOrNull()
            ?: return rd.setErrorMsg("参数错误")
        val arr = load(ns)
        val list = arr.list.filterIndexed { i, _ ->
            val o = arr.getJsonObject(i)
            (o.getString("sourceUrl") ?: o.getString("rssUrl")) != key
        }
        save(ns, JsonArray(list))
        return rd.setData(true)
    }

    suspend fun getRssArticles(context: RoutingContext): ReturnData {
        val rd = ReturnData()
        if (!checkAuth(context)) return rd.setData("NEED_LOGIN").setErrorMsg("请登录后使用")
        val sourceUrl = p(context, "sourceUrl") ?: p(context, "url") ?: return rd.setErrorMsg("RSS源链接不能为空")
        val sortName = p(context, "sortName") ?: ""
        var sortUrl = p(context, "sortUrl") ?: ""
        val page = pInt(context, "page") ?: 1
        if (sortUrl.isEmpty()) sortUrl = sourceUrl
        val ns = getUserNameSpace(context)
        val source = getRssSourceByURL(sourceUrl, ns) ?: return rd.setErrorMsg("RSS源不存在")
        val (articles, next) = Rss.getArticles(sortName, sortUrl, source, page, null)
        return rd.setData(mapOf("articles" to articles, "nextPage" to next))
    }

    suspend fun getRssContent(context: RoutingContext): ReturnData {
        val rd = ReturnData()
        if (!checkAuth(context)) return rd.setData("NEED_LOGIN").setErrorMsg("请登录后使用")
        val sourceUrl = p(context, "sourceUrl") ?: ""
        val link = p(context, "link") ?: p(context, "url") ?: return rd.setErrorMsg("link 不能为空")
        val title = p(context, "title") ?: ""
        val ns = getUserNameSpace(context)
        val source = if (sourceUrl.isNotEmpty()) getRssSourceByURL(sourceUrl, ns) else null
        val article = RssArticle(origin = sourceUrl, title = title, link = link)
        val content = if (source != null && !source.ruleContent.isNullOrBlank()) {
            Rss.getContent(article, source.ruleContent!!, source, null)
        } else if (source != null) {
            Rss.getContent(article, "", source, null)
        } else {
            // bare fetch
            try {
                val au = io.legado.app.model.analyzeRule.AnalyzeUrl(mUrl = link)
                au.getStrResponseAwait().body ?: ""
            } catch (_: Exception) {
                ""
            }
        }
        return rd.setData(mapOf("link" to link, "content" to content))
    }

    private fun p(context: RoutingContext, name: String): String? {
        if (context.request().method() == HttpMethod.POST) {
            context.bodyAsJson?.getString(name)?.let { return it }
        }
        return context.queryParam(name).firstOrNull()
    }

    private fun pInt(context: RoutingContext, name: String): Int? {
        if (context.request().method() == HttpMethod.POST) {
            context.bodyAsJson?.getInteger(name)?.let { return it }
        }
        return context.queryParam(name).firstOrNull()?.toIntOrNull()
    }
}
''',
)

# ---------------------------------------------------------------------------
# Fix BookExport extension conflict (class method now delegates)
# ---------------------------------------------------------------------------
be = (BIZ / "com/htmake/reader/api/controller/BookExport.kt").read_text(encoding="utf-8")
if "suspend fun BookController.exportBook" in be:
    be = re.sub(
        r"\nsuspend fun BookController\.exportBook\(context: RoutingContext\): ReturnData =\n    BookExport\.exportBook\(this, context\)\n?",
        "\n",
        be,
    )
    (BIZ / "com/htmake/reader/api/controller/BookExport.kt").write_text(be, encoding="utf-8", newline="\n")
    print("removed duplicate exportBook extension")

# ---------------------------------------------------------------------------
# README + INDEX update
# ---------------------------------------------------------------------------
readme = BIZ / "README.md"
r = readme.read_text(encoding="utf-8", errors="replace")
if "Phase 8" not in r:
    r = r.rstrip() + """


## Phase 8 增量

- **exportBook** 接线 `BookExport`（txt/epub）
- **cacheBookSSE**：并发默认 24，进度 SSE（success/failed/cached/total）
- **saveBookContent / deleteBookCache / getShelfBookWithCacheInfo**
- **getChapterListByRule**：本地 txt/epub/pdf 按规则重切目录
- **searchBookContent**：章节内关键字检索（分页 lastIndex/size）
- **saveBookGroupId / addBookGroupMulti / removeBookGroupMulti / saveBookConfig**
- **setBookSource / searchBookSourceSSE / getInvalidBookSources**
- **Rss** 引擎 + **RssSourceController**：规则解析 / 默认 RSS·Atom XML
- **Book**：`group`、`pdfImageWidth` 字段
"""
    readme.write_text(r, encoding="utf-8", newline="\n")
    print("README phase8")

index = BIZ / "INDEX.md"
ix = index.read_text(encoding="utf-8", errors="replace")
if "BookExport" not in ix:
    # insert rows before trailing stats
    rows = """| BookExport | `com/htmake/reader/api/controller/BookExport.kt` | `BookController.exportBook` |
| Rss | `io/legado/app/model/rss/Rss.kt` | `io/legado/app/model/rss/Rss.kt`<br>`RssParserByRule` / `RssParserDefault` |
| RssSource | `io/legado/app/data/entities/RssSource.kt` | `io/legado/app/data/entities/RssSource.java` |
| RssArticle | `io/legado/app/data/entities/RssArticle.kt` | `io/legado/app/data/entities/RssArticle.java` |
| WebdavPaths | `com/htmake/reader/api/controller/WebdavPaths.kt` | （业务新增） |
"""
    ix = ix.replace(
        "| Yuedu | `com/htmake/reader/api/YueduApi.kt` |",
        rows + "| Yuedu | `com/htmake/reader/api/YueduApi.kt` |",
    )
if "phase8" not in ix:
    ix = ix.replace("生成于 phase6", "生成于 phase6，增量 phase7–8")
    # recount files
    kt_count = sum(1 for _ in BIZ.rglob("*.kt"))
    ix = re.sub(r"business `\.kt` 文件数: \*\*\d+\*\*", f"business `.kt` 文件数: **{kt_count}**", ix)
    if "business `.kt`" not in ix:
        ix = re.sub(r"- business.*?文件.*", f"- business `.kt` 文件数: **{kt_count}**", ix)
index.write_text(ix, encoding="utf-8", newline="\n")
print("INDEX updated")

# stats
kt = list(BIZ.rglob("*.kt"))
lines = sum(len(p.read_text(encoding="utf-8", errors="replace").splitlines()) for p in kt)
print(f"DONE phase8: {len(kt)} kt files, ~{lines} lines")
