/** Business rewrite from reader-pro-3.2.14.jar — phase8. */

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
    var url = context.queryParam("url").firstOrNull()
        ?: context.bodyAsJson?.getString("url")
        ?: context.queryParam("sortUrl").firstOrNull()
        ?: context.bodyAsJson?.getString("sortUrl")
        ?: ""
    val page = context.queryParam("page").firstOrNull()?.toIntOrNull()
        ?: context.bodyAsJson?.getInteger("page") ?: 1
    val ns = getUserNameSpace(context)
    val sourceUrl = context.queryParam("bookSourceUrl").firstOrNull()
        ?: context.bodyAsJson?.getString("bookSourceUrl")
    val sourceStr = sourceUrl?.let { getBookSourceStringBySourceURLOpt(it, ns) }
        ?: return rd.setErrorMsg("书源信息错误")
    // 若只给了书源、未给 url：返回 exploreUrl 分类列表
    if (url.isBlank()) {
        val src = io.legado.app.data.entities.BookSource.fromJson(sourceStr).getOrNull()
        val cats = io.legado.app.model.webBook.BookList.parseExploreUrl(src?.exploreUrl, page)
        return rd.setData(cats.map { (title, u) -> mapOf("title" to title, "url" to u) })
    }
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
    val enabled = filterEnabledSources(ns, sources)
    enabled.chunked(concurrent).forEach { batch ->
        batch.map { o ->
            async {
                try {
                    withTimeoutOrNull(perSourceTimeoutMs) {
                        WebBook(o.encode(), false, null, ns).searchBook(key, page)
                    } ?: emptyList()
                } catch (e: Exception) {
                    val url = o.getString("bookSourceUrl")
                    if (url != null) {
                        addInvalidBookSource(
                            url,
                            mapOf("sourceUrl" to url, "time" to System.currentTimeMillis(), "error" to e.toString()),
                            ns
                        )
                    }
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
    val cache = getInvalidBookSourceCache(ns)
    val dir = File(ExtKt.getWorkDir("storage", "cache", "invalidBookSourceCache", ns))
    if (!dir.isDirectory) return rd.setData(emptyList<Any>())
    val list = ArrayList<Map<String, Any?>>()
    dir.listFiles()?.forEach { f ->
        val raw = cache.getByHashCode(f.name) ?: return@forEach
        try {
            // strip expire header already handled by getByHashCode
            val text = raw.removePrefix("expireAt=").let {
                if (it.contains('
') && raw.startsWith("expireAt=")) it.substringAfter('
') else raw
            }
            val o = JsonObject(text)
            list += o.map
        } catch (_: Exception) {
            try {
                list += ExtKt.asJsonObject(raw)?.map ?: mapOf("raw" to raw)
            } catch (_: Exception) {
            }
        }
    }
    return rd.setData(list)
}

/** 跳过已标记失效的书源（多源搜索用） */
fun BookController.filterEnabledSources(ns: String, sources: JsonArray): List<JsonObject> {
    return (0 until sources.size()).mapNotNull { i ->
        val o = sources.getJsonObject(i) ?: return@mapNotNull null
        if (o.getBoolean("enabled", true) == false) return@mapNotNull null
        val url = o.getString("bookSourceUrl") ?: return@mapNotNull o
        if (isInvalidBookSource(url, ns)) null else o
    }
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
    val hits = ArrayList<io.legado.app.data.entities.SearchResult>()
    var currentIndex = lastIndex
    var i = lastIndex + 1
    while (i < chapters.size && hits.size < size) {
        val ch = chapters[i]
        currentIndex = i
        try {
            hits.addAll(searchChapter(book, ch, keyword, source, ns, chapters.getOrNull(i + 1)?.url))
        } catch (_: Exception) {
        }
        i++
    }
    return rd.setData(mapOf("list" to hits, "lastIndex" to currentIndex))
}

/** 单章检索，对齐 jar SearchResult 字段 */
suspend fun BookController.searchChapter(
    book: Book,
    chapter: BookChapter,
    query: String,
    source: String? = null,
    ns: String = book.userNameSpace ?: "default",
    nextUrl: String? = null
): List<io.legado.app.data.entities.SearchResult> {
    val content = readChapterText(book, source, ns, chapter, nextUrl)
    if (content.isEmpty()) return emptyList()
    val positions = searchPosition(content, query)
    return positions.mapIndexed { idx, pos ->
        val (qInResult, snippet) = getResultAndQueryIndex(content, pos, query)
        io.legado.app.data.entities.SearchResult(
            resultCount = 0,
            resultCountWithinChapter = idx,
            resultText = snippet,
            chapterTitle = chapter.title,
            query = query,
            pageSize = 0,
            chapterIndex = chapter.index,
            pageIndex = 0,
            queryIndexInResult = qInResult,
            queryIndexInChapter = pos
        )
    }
}

private fun searchPosition(content: String, pattern: String): List<Int> {
    val out = ArrayList<Int>()
    var from = 0
    while (true) {
        val i = content.indexOf(pattern, from)
        if (i < 0) break
        out += i
        from = i + 1
    }
    return out
}

private fun getResultAndQueryIndex(content: String, queryIndexInContent: Int, query: String): Pair<Int, String> {
    var po1 = queryIndexInContent - 20
    var po2 = queryIndexInContent + query.length + 20
    if (po1 < 0) po1 = 0
    if (po2 > content.length) po2 = content.length
    return (queryIndexInContent - po1) to content.substring(po1, po2)
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

// tts / textToSpeech → BookTts.kt

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
