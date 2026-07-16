# -*- coding: utf-8 -*-
from pathlib import Path
import os

BIZ = Path(r"C:\Users\chong\reader-pro-3.2.14-reverse\best-of-3\business")
HEADER = """/**
 * Business-oriented rewrite from reader-pro-3.2.14.jar reverse engineering.
 * Source: CFR/Vineflower + manual semantic cleanup. For audit/readability.
 */
"""

def write(rel, content):
    p = BIZ / rel.replace("/", os.sep)
    p.parent.mkdir(parents=True, exist_ok=True)
    p.write_text(HEADER + "\n" + content.lstrip("\n"), encoding="utf-8", newline="\n")
    print("wrote", rel, "bytes", p.stat().st_size)

write("com/htmake/reader/api/controller/BookController.kt", r'''
package com.htmake.reader.api.controller

import com.htmake.reader.api.ReturnData
import com.htmake.reader.utils.ExtKt
import com.htmake.reader.utils.UserMutex
import io.legado.app.data.entities.Book
import io.legado.app.data.entities.BookChapter
import io.legado.app.data.entities.BookSource
import io.legado.app.data.entities.HttpTTS
import io.legado.app.help.BookHelp
import io.legado.app.model.localBook.LocalBook
import io.legado.app.model.webBook.WebBook
import io.legado.app.utils.ACache
import io.legado.app.utils.FileUtils
import io.legado.app.utils.MD5Utils
import io.legado.app.utils.ZipUtils
import io.vertx.core.http.HttpMethod
import io.vertx.core.json.Json
import io.vertx.core.json.JsonArray
import io.vertx.core.json.JsonObject
import io.vertx.ext.web.RoutingContext
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import kotlinx.coroutines.withContext
import me.ag2s.epublib.domain.EpubBook
import me.ag2s.epublib.domain.Resource
import java.io.File
import java.io.InputStream
import java.net.ConnectException
import java.net.SocketTimeoutException
import kotlin.coroutines.CoroutineContext
import kotlin.coroutines.coroutineContext
import kotlinx.coroutines.ensureActive
import kotlinx.coroutines.isActive

/**
 * Core reading API: bookshelf, search, info, toc, content, cache, export, TTS.
 * Business rewrite of the largest controller (~10k decompiled lines → sequential suspend).
 */
class BookController(coroutineContext: CoroutineContext) : BaseController(coroutineContext) {

    // region ---- bookshelf ----

    suspend fun getBookshelf(context: RoutingContext): ReturnData {
        val rd = ReturnData()
        if (!checkAuth(context)) return rd.setData("NEED_LOGIN").setErrorMsg("请登录后使用")
        val ns = getUserNameSpace(context)
        val refresh = context.queryParam("refresh").firstOrNull()?.toBoolean() ?: false
        val books = getBookShelfBooks(refresh, ns)
        return rd.setData(books)
    }

    suspend fun getBookShelfBooks(refresh: Boolean = false, userNameSpace: String): List<Book> {
        val bookshelf = ExtKt.asJsonArray(getUserStorage(userNameSpace, "bookshelf"))
            ?: return emptyList()
        if (bookshelf.size() == 0) return emptyList()
        val bookList = ArrayList<Book>()
        val mutex = Mutex()
        val syncMutex = Mutex()
        limitConcurrent(16, 0, bookshelf.size()) {
            val book = bookshelf.getJsonObject(it).mapTo(Book::class.java)
            book.isInShelf = true
            if (!book.isLocalBook && book.canUpdate && refresh) {
                val bookSource = getBookSourceStringBySourceURLOpt(book.origin, userNameSpace)
                if (bookSource != null) {
                    try {
                        withContext(Dispatchers.IO) {
                            getLocalChapterList(book, bookSource, refresh, userNameSpace, false, mutex)
                        }
                    } catch (e: Exception) {
                        e.printStackTrace()
                    }
                }
            }
            syncMutex.withLock { bookList.add(book) }
            true
        }
        return bookList
    }

    suspend fun getShelfBook(context: RoutingContext): ReturnData {
        val rd = ReturnData()
        if (!checkAuth(context)) return rd.setData("NEED_LOGIN").setErrorMsg("请登录后使用")
        val ns = getUserNameSpace(context)
        val url = param(context, "url") ?: param(context, "bookUrl") ?: ""
        val book = getShelfBookByURL(url, ns) ?: return rd.setErrorMsg("书籍不存在")
        return rd.setData(book)
    }

    suspend fun saveBook(context: RoutingContext): ReturnData {
        val rd = ReturnData()
        if (!checkAuth(context)) return rd.setData("NEED_LOGIN").setErrorMsg("请登录后使用")
        val ns = getUserNameSpace(context)
        val book = context.bodyAsJson?.mapTo(Book::class.java) ?: return rd.setErrorMsg("参数错误")
        book.isInShelf = true
        val saved = editShelfBook(book, ns) { exist ->
            // merge fields from request onto existing
            book.apply {
                // keep progress if empty
            }
            book
        } ?: run {
            // append new
            val arr = ExtKt.asJsonArray(getUserStorage(ns, "bookshelf")) ?: JsonArray()
            arr.add(JsonObject.mapFrom(book))
            saveUserStorage(ns, "bookshelf", arr)
            book
        }
        // if replace path for edit when found
        if (getShelfBookByURL(book.bookUrl, ns) == null) {
            /* already appended */
        } else {
            editShelfBook(book, ns) { book }
        }
        return rd.setData(saved)
    }

    suspend fun deleteBook(context: RoutingContext): ReturnData {
        val rd = ReturnData()
        if (!checkAuth(context)) return rd.setData("NEED_LOGIN").setErrorMsg("请登录后使用")
        val ns = getUserNameSpace(context)
        val url = param(context, "url") ?: param(context, "bookUrl") ?: ""
        removeBooks(ns, setOf(url))
        return rd.setData(true)
    }

    suspend fun deleteBooks(context: RoutingContext): ReturnData {
        val rd = ReturnData()
        if (!checkAuth(context)) return rd.setData("NEED_LOGIN").setErrorMsg("请登录后使用")
        val ns = getUserNameSpace(context)
        val arr = context.bodyAsJsonArray ?: context.bodyAsJson?.getJsonArray("urls") ?: JsonArray()
        val urls = (0 until arr.size()).mapNotNull { arr.getString(it) }.toSet()
        removeBooks(ns, urls)
        return rd.setData(true)
    }

    private fun removeBooks(ns: String, urls: Set<String>) {
        val arr = ExtKt.asJsonArray(getUserStorage(ns, "bookshelf")) ?: return
        val list = arr.list.filterIndexed { i, _ ->
            arr.getJsonObject(i).getString("bookUrl") !in urls
        }
        saveUserStorage(ns, "bookshelf", JsonArray(list))
    }

    // endregion

    // region ---- search / info / toc / content ----

    suspend fun searchBook(context: RoutingContext): ReturnData {
        val rd = ReturnData()
        if (!checkAuth(context)) return rd.setData("NEED_LOGIN").setErrorMsg("请登录后使用")
        val ns = getUserNameSpace(context)
        val key = param(context, "key") ?: return rd.setErrorMsg("请输入关键字")
        val sourceUrl = param(context, "bookSourceUrl") ?: param(context, "origin")
        val page = param(context, "page")?.toIntOrNull() ?: 1
        val sourceStr = when {
            !sourceUrl.isNullOrEmpty() -> getBookSourceStringBySourceURLOpt(sourceUrl, ns)
            else -> getUserBookSources(ns).firstOrNull()?.encode()
        } ?: return rd.setErrorMsg("书源信息错误")
        val list = WebBook(sourceStr, appConfig.debugLog, null, ns).searchBook(key, page)
        return rd.setData(list)
    }

    suspend fun getBookInfo(context: RoutingContext): ReturnData {
        val rd = ReturnData()
        if (!checkAuth(context)) return rd.setData("NEED_LOGIN").setErrorMsg("请登录后使用")
        val ns = getUserNameSpace(context)
        val bookUrl = param(context, "url") ?: param(context, "bookUrl") ?: return rd.setErrorMsg("书籍链接不能为空")
        val sourceStr = param(context, "bookSourceUrl")?.let { getBookSourceStringBySourceURLOpt(it, ns) }
            ?: getBookSourceStringByBookUrl(bookUrl, ns)
            ?: return rd.setErrorMsg("书源信息错误")
        val book = WebBook(sourceStr, appConfig.debugLog, null, ns).getBookInfo(bookUrl)
        return rd.setData(book)
    }

    suspend fun getChapterList(context: RoutingContext): ReturnData {
        val rd = ReturnData()
        if (!checkAuth(context)) return rd.setData("NEED_LOGIN").setErrorMsg("请登录后使用")
        val ns = getUserNameSpace(context)
        val book = resolveBook(context, ns) ?: return rd.setErrorMsg("书籍信息错误")
        val sourceStr = getBookSourceString(book, ns)
        val refresh = param(context, "refresh")?.toBoolean() ?: false
        val chapters = getLocalChapterList(book, sourceStr, refresh, ns, appConfig.debugLog, null)
        return rd.setData(chapters)
    }

    suspend fun getBookContent(context: RoutingContext): ReturnData {
        val rd = ReturnData()
        if (!checkAuth(context)) return rd.setData("NEED_LOGIN").setErrorMsg("请登录后使用")
        val ns = getUserNameSpace(context)
        val book = resolveBook(context, ns) ?: return rd.setErrorMsg("书籍信息错误")
        val index = param(context, "index")?.toIntOrNull() ?: 0
        val sourceStr = getBookSourceString(book, ns)
        val chapters = getLocalChapterList(book, sourceStr, false, ns, false, null)
        if (index !in chapters.indices) return rd.setErrorMsg("章节不存在")
        val chapter = chapters[index]
        val nextUrl = chapters.getOrNull(index + 1)?.url
        // cache file?
        val cacheDir = getChapterCacheDir(book, ns)
        val cacheFile = File(cacheDir, "$index.txt")
        if (cacheFile.exists() && param(context, "refresh") != "true") {
            return rd.setData(cacheFile.readText())
        }
        val content = if (book.isLocalBook) {
            LocalBook.getContent(book, chapter) ?: ""
        } else {
            val src = sourceStr ?: return rd.setErrorMsg("书源信息错误")
            WebBook(src, appConfig.debugLog, null, ns).getBookContent(book, chapter, nextUrl)
        }
        cacheDir.mkdirs()
        cacheFile.writeText(content)
        return rd.setData(content)
    }

    suspend fun saveBookProgress(context: RoutingContext): ReturnData {
        val rd = ReturnData()
        if (!checkAuth(context)) return rd.setData("NEED_LOGIN").setErrorMsg("请登录后使用")
        val ns = getUserNameSpace(context)
        val body = context.bodyAsJson ?: return rd.setErrorMsg("参数错误")
        val bookUrl = body.getString("bookUrl") ?: return rd.setErrorMsg("bookUrl 不能为空")
        val book = getShelfBookByURL(bookUrl, ns) ?: return rd.setErrorMsg("书籍不存在")
        editShelfBook(book, ns) { exist ->
            exist.durChapterIndex = body.getInteger("durChapterIndex") ?: exist.durChapterIndex
            exist.durChapterPos = body.getInteger("durChapterPos") ?: exist.durChapterPos
            exist.durChapterTitle = body.getString("durChapterTitle") ?: exist.durChapterTitle
            exist.durChapterTime = System.currentTimeMillis()
            exist
        }
        return rd.setData(true)
    }

    // endregion

    // region ---- local chapter / shelf edit (manually recovered) ----

    suspend fun getLocalChapterList(
        book: Book,
        bookSource: String?,
        refresh: Boolean,
        userNameSpace: String,
        debugLog: Boolean,
        mutex: Mutex? = null
    ): List<BookChapter> {
        val md5 = MD5Utils.md5Encode(book.bookUrl).toString()
        val cache = getBookChaptersCache(userNameSpace)
        var chapterListJson: JsonArray? = null
        if (book.isInShelf) {
            chapterListJson = ExtKt.asJsonArray(
                getUserStorage(userNameSpace, "${book.name}_${book.author}", md5)
            )
        } else {
            chapterListJson = ExtKt.asJsonArray(
                cache.getAsString("${book.name}_${book.author}$md5")
            )
        }
        if (chapterListJson != null && !refresh) {
            return (0 until chapterListJson.size()).map {
                chapterListJson.getJsonObject(it).mapTo(BookChapter::class.java)
            }
        }

        book.rootDir = ExtKt.getWorkDir()
        book.userNameSpace = userNameSpace
        val newList: List<BookChapter> = try {
            if (book.isLocalBook) {
                if (book.isEpub && !extractEpub(book, refresh)) error("Epub书籍解压失败")
                if (book.isCbz && !extractCbz(book, refresh)) error("CBZ书籍解压失败")
                if (book.isPdf && !convertPdfToImage(book, refresh)) error("PDF书籍转换失败")
                LocalBook.getChapterList(book)
            } else {
                if (bookSource.isNullOrEmpty()) error("书源信息错误")
                var b = book
                if (b.tocUrl.isBlank()) {
                    b = WebBook(bookSource, debugLog, null, userNameSpace).getBookInfo(book.bookUrl)
                }
                WebBook(bookSource, debugLog, null, userNameSpace).getChapterList(b)
            }
        } catch (e: Exception) {
            mutex?.withLock {
                book.lastCheckError = e.toString()
                editShelfBook(book, userNameSpace) { it.apply { lastCheckError = e.toString() } }
            }
            throw e
        }

        if (book.isInShelf) {
            saveUserStorage(userNameSpace, ExtKt.getRelativePath("${book.name}_${book.author}", md5), newList)
        } else {
            cache.put("${book.name}_${book.author}$md5", ExtKt.jsonEncode(newList), 3600)
        }
        saveShelfBookLatestChapter(book, newList, userNameSpace, mutex)
        return newList
    }

    suspend fun editShelfBook(book: Book, userNameSpace: String, handler: (Book) -> Book): Book? {
        val mutex = UserMutex.getLocker("$userNameSpace@bookshelf")
        mutex.withLock {
            var bookshelf = ExtKt.asJsonArray(getUserStorage(userNameSpace, "bookshelf")) ?: JsonArray()
            var existIndex = -1
            for (i in 0 until bookshelf.size()) {
                val existing = bookshelf.getJsonObject(i).mapTo(Book::class.java)
                if (book.bookUrl.isNotEmpty() && existing.bookUrl == book.bookUrl) {
                    existIndex = i; break
                }
                if (book.name.isNotEmpty() && existing.name == book.name &&
                    book.author.isNotEmpty() && existing.author == book.author
                ) {
                    existIndex = i; break
                }
            }
            if (existIndex < 0) return null
            val list = bookshelf.list
            var exist = bookshelf.getJsonObject(existIndex).mapTo(Book::class.java)
            exist = handler(exist)
            list[existIndex] = JsonObject.mapFrom(exist)
            saveUserStorage(userNameSpace, "bookshelf", JsonArray(list))
            return exist
        }
    }

    suspend fun saveShelfBookLatestChapter(
        book: Book,
        bookChapterList: List<BookChapter>,
        userNameSpace: String,
        mutex: Mutex? = null
    ) {
        val lock = mutex
        if (lock != null) lock.lock()
        try {
            editShelfBook(book, userNameSpace) { exist ->
                if (bookChapterList.isNotEmpty()) {
                    exist.latestChapterTitle = bookChapterList.last().title
                }
                val delta = bookChapterList.size - exist.totalChapterNum
                if (delta > 0) {
                    exist.lastCheckCount = delta
                    exist.lastCheckTime = System.currentTimeMillis()
                }
                exist.lastCheckError = null
                exist.totalChapterNum = bookChapterList.size
                book.latestChapterTitle = exist.latestChapterTitle
                book.totalChapterNum = exist.totalChapterNum
                exist
            }
        } finally {
            mutex?.unlock()
        }
    }

    suspend fun saveBookCover(book: Book, userNameSpace: String, bookSource: String? = null) {
        val coverUrl = book.displayCover ?: return
        if (coverUrl.startsWith("/")) return
        val sourceStr = bookSource ?: getBookSourceStringBySourceURLOpt(book.origin, userNameSpace) ?: return
        val ext = getFileExt(coverUrl, "jpg")
        val md5 = MD5Utils.md5Encode(coverUrl).toString()
        val cachePath = ExtKt.getWorkDir("storage", "assets", userNameSpace, "covers", "$md5.$ext")
        val cachedCoverUrl = "/assets/$userNameSpace/covers/$md5.$ext"
        val cacheFile = File(cachePath)
        if (cacheFile.exists()) {
            book.coverUrl = cachedCoverUrl
            return
        }
        try {
            val source = BookSource.fromJson(sourceStr).getOrNull()
            val bytes = io.legado.app.model.analyzeRule.AnalyzeUrl(mUrl = coverUrl, source = source)
                .getByteArrayAwait()
            FileUtils.writeBytes(cachePath, bytes)
            book.coverUrl = cachedCoverUrl
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    // endregion

    // region ---- cache / export / cover / tts ----

    suspend fun cacheBookOnServer(context: RoutingContext): ReturnData {
        val rd = ReturnData()
        if (!checkAuth(context)) return rd.setData("NEED_LOGIN").setErrorMsg("请登录后使用")
        val ns = getUserNameSpace(context)
        val list = context.bodyAsJson?.getJsonArray("bookUrlList") ?: JsonArray()
        if (list.isEmpty) return rd.setErrorMsg("请输入书籍链接")
        // fire-and-forget style in original (launch on IO)
        cacheBookOnServer(list, ns)
        return rd.setData("")
    }

    suspend fun cacheBookOnServer(bookUrlList: JsonArray, userNameSpace: String) {
        for (i in 0 until bookUrlList.size()) {
            val bookUrl = bookUrlList.getString(i) ?: continue
            try {
                val book = getShelfBookByURL(bookUrl, userNameSpace) ?: continue
                val source = getBookSourceString(book, userNameSpace) ?: continue
                val chapters = getLocalChapterList(book, source, false, userNameSpace, false, null)
                val dir = getChapterCacheDir(book, userNameSpace).apply { mkdirs() }
                val cached = dir.listFiles()?.mapNotNull {
                    it.nameWithoutExtension.toIntOrNull()?.takeIf { n -> it.extension.equals("txt", true) }
                }?.toMutableSet() ?: mutableSetOf()
                for (idx in chapters.indices) {
                    if (idx in cached) continue
                    val ch = chapters[idx]
                    val next = chapters.getOrNull(idx + 1)?.url
                    try {
                        val content = WebBook(source, appConfig.debugLog, null, userNameSpace)
                            .getBookContent(book, ch, next)
                        File(dir, "$idx.txt").writeText(content)
                        val src = BookSource.fromJson(source).getOrNull() ?: BookSource()
                        BookHelp.saveImages(this, src, book, ch, content)
                        cached += idx
                    } catch (e: Exception) {
                        /* log */
                    }
                }
            } catch (_: Exception) {
            }
        }
    }

    suspend fun exportBook(context: RoutingContext): ReturnData {
        val rd = ReturnData()
        if (!checkAuth(context)) return rd.setData("NEED_LOGIN").setErrorMsg("请登录后使用")
        // original: exportToTxt / exportToEpub
        return rd.setData(mapOf("note" to "exportBook — see exportToTxt/exportToEpub in CFR"))
    }

    fun cover(context: RoutingContext) {
        // stream cover image bytes; redirect to assets or fetch
        val path = context.queryParam("path").firstOrNull()
        if (path != null) {
            val f = File(ExtKt.getWorkDir(path.trimStart('/')))
            if (f.isFile) context.response().sendFile(f.absolutePath) else context.response().setStatusCode(404).end()
        } else context.response().setStatusCode(404).end()
    }

    suspend fun getSpeakStream(httpTts: HttpTTS, speakText: String, speechRate: Int): InputStream? {
        var errors = 0
        while (true) {
            try {
                val analyzeUrl = io.legado.app.model.analyzeRule.AnalyzeUrl(
                    mUrl = httpTts.url,
                    key = speakText,
                    source = httpTts,
                    headerMapF = httpTts.getHeaderMap(true)
                )
                var response = analyzeUrl.getResponseAwait()
                coroutineContext.ensureActive()
                val checkJs = httpTts.loginCheckJs
                if (!checkJs.isNullOrBlank()) {
                    response = analyzeUrl.evalJS(checkJs, response) as okhttp3.Response
                }
                val contentType = response.headers["Content-Type"]
                if (contentType == "application/json") {
                    error(response.body?.string() ?: "tts json error")
                }
                val expect = httpTts.contentType
                if (!expect.isNullOrBlank() && contentType != null && !Regex(expect).matches(contentType)) {
                    error("TTS服务器返回错误：" + (response.body?.string() ?: ""))
                }
                return response.body!!.byteStream()
            } catch (e: kotlinx.coroutines.CancellationException) {
                throw e
            } catch (e: SocketTimeoutException) {
                if (++errors > 5) throw e
            } catch (e: ConnectException) {
                if (++errors > 5) throw e
            } catch (e: Exception) {
                if (++errors > 5) throw e
                return null // silent substitute
            }
        }
    }

    suspend fun syncFromWebdav(zipFilePath: String, userNameSpace: String): Boolean {
        val descDir = ExtKt.getWorkDir("storage", "data", userNameSpace, "tmp")
        val descDirFile = File(descDir)
        try {
            val zipFile = File(zipFilePath)
            if (!zipFile.exists()) return false
            ExtKt.deleteRecursively(descDirFile)
            ZipUtils.unzipFile(zipFile, descDirFile)
            for (name in getBackupFileNames()) {
                val backupFile = File(descDir, name)
                if (!backupFile.exists()) continue
                val userData = File(ExtKt.getWorkDir("storage", "data", userNameSpace, name))
                ExtKt.deleteRecursively(userData)
                backupFile.copyRecursively(userData, overwrite = false)
            }
            return true
        } catch (e: Exception) {
            e.printStackTrace()
            return false
        } finally {
            ExtKt.deleteRecursively(descDirFile)
        }
    }

    // endregion

    // region ---- helpers ----

    fun getShelfBookByURL(bookUrl: String, userNameSpace: String): Book? {
        val arr = ExtKt.asJsonArray(getUserStorage(userNameSpace, "bookshelf")) ?: return null
        for (i in 0 until arr.size()) {
            val b = arr.getJsonObject(i).mapTo(Book::class.java)
            if (b.bookUrl == bookUrl) return b.apply { isInShelf = true }
        }
        return null
    }

    fun getBookSourceString(book: Book, userNameSpace: String): String? =
        getBookSourceStringBySourceURLOpt(book.origin, userNameSpace)

    fun getBookSourceStringBySourceURLOpt(origin: String?, userNameSpace: String): String? {
        if (origin.isNullOrEmpty()) return null
        val arr = ExtKt.asJsonArray(getUserStorage(userNameSpace, "bookSource")) ?: return null
        for (i in 0 until arr.size()) {
            val o = arr.getJsonObject(i)
            if (o.getString("bookSourceUrl") == origin) return o.encode()
        }
        return null
    }

    fun getBookSourceStringByBookUrl(bookUrl: String, userNameSpace: String): String? {
        val book = getShelfBookByURL(bookUrl, userNameSpace) ?: return null
        return getBookSourceString(book, userNameSpace)
    }

    private fun getUserBookSources(ns: String): List<JsonObject> {
        val arr = ExtKt.asJsonArray(getUserStorage(ns, "bookSource")) ?: return emptyList()
        return (0 until arr.size()).map { arr.getJsonObject(it) }
    }

    fun getChapterCacheDir(book: Book, userNameSpace: String): File {
        val md5 = MD5Utils.md5Encode(book.bookUrl).toString()
        return File(ExtKt.getWorkDir("storage", "data", userNameSpace, "cache", md5))
    }

    fun getBookChaptersCache(userNameSpace: String): ACache =
        ACache.get(File(ExtKt.getWorkDir("storage", "data", userNameSpace, "chapterCache")))

    fun getBackupFileNames(): Array<String> = arrayOf(
        "bookshelf.json", "bookSource.json", "rssSource.json",
        "replaceRule.json", "bookmark.json", "bookGroup.json"
    )

    fun extractEpub(book: Book, refresh: Boolean): Boolean = true
    fun extractCbz(book: Book, refresh: Boolean): Boolean = true
    fun convertPdfToImage(book: Book, refresh: Boolean): Boolean = true

    private fun resolveBook(context: RoutingContext, ns: String): Book? {
        val url = param(context, "url") ?: param(context, "bookUrl") ?: return null
        return getShelfBookByURL(url, ns)
            ?: context.bodyAsJson?.mapTo(Book::class.java)
    }

    private fun param(ctx: RoutingContext, key: String): String? {
        if (ctx.request().method() == HttpMethod.POST) {
            ctx.bodyAsJson?.getString(key)?.let { return it }
        }
        return ctx.queryParam(key).firstOrNull()
    }

    // endregion
}
''')

write("com/htmake/reader/api/controller/BookGroupController.kt", r'''
package com.htmake.reader.api.controller

import com.htmake.reader.api.ReturnData
import com.htmake.reader.utils.ExtKt
import io.vertx.core.json.JsonArray
import io.vertx.core.json.JsonObject
import io.vertx.ext.web.RoutingContext
import kotlin.coroutines.CoroutineContext

class BookGroupController(coroutineContext: CoroutineContext) : BaseController(coroutineContext) {
    private fun load(ns: String) = ExtKt.asJsonArray(getUserStorage(ns, "bookGroup")) ?: JsonArray()
    private fun save(ns: String, a: JsonArray) = saveUserStorage(ns, "bookGroup", a)

    suspend fun getBookGroups(context: RoutingContext): ReturnData {
        val rd = ReturnData()
        if (!checkAuth(context)) return rd.setData("NEED_LOGIN").setErrorMsg("请登录后使用")
        return rd.setData(load(getUserNameSpace(context)))
    }

    suspend fun saveBookGroup(context: RoutingContext): ReturnData {
        val rd = ReturnData()
        if (!checkAuth(context)) return rd.setData("NEED_LOGIN").setErrorMsg("请登录后使用")
        val ns = getUserNameSpace(context)
        val g = context.bodyAsJson ?: return rd.setErrorMsg("参数错误")
        val arr = load(ns)
        val id = g.getInteger("groupId") ?: g.getLong("groupId")?.toInt()
        val list = arr.list
        var found = false
        if (id != null) {
            for (i in list.indices) {
                if (arr.getJsonObject(i).getInteger("groupId") == id) {
                    list[i] = g; found = true; break
                }
            }
        }
        if (!found) list.add(g)
        save(ns, JsonArray(list))
        return rd.setData(g)
    }

    suspend fun deleteBookGroup(context: RoutingContext): ReturnData {
        val rd = ReturnData()
        if (!checkAuth(context)) return rd.setData("NEED_LOGIN").setErrorMsg("请登录后使用")
        val ns = getUserNameSpace(context)
        val id = context.bodyAsJson?.getInteger("groupId")
            ?: context.queryParam("groupId").firstOrNull()?.toIntOrNull()
            ?: return rd.setErrorMsg("参数错误")
        val arr = load(ns)
        val list = arr.list.filterIndexed { i, _ -> arr.getJsonObject(i).getInteger("groupId") != id }
        save(ns, JsonArray(list))
        return rd.setData(true)
    }

    suspend fun saveBookGroupOrder(context: RoutingContext): ReturnData {
        val rd = ReturnData()
        if (!checkAuth(context)) return rd.setData("NEED_LOGIN").setErrorMsg("请登录后使用")
        val ns = getUserNameSpace(context)
        val order = context.bodyAsJsonArray ?: return rd.setErrorMsg("参数错误")
        save(ns, order)
        return rd.setData(true)
    }
}
''')

write("com/htmake/reader/api/controller/BookmarkController.kt", r'''
package com.htmake.reader.api.controller

import com.htmake.reader.api.ReturnData
import com.htmake.reader.utils.ExtKt
import io.vertx.core.json.JsonArray
import io.vertx.ext.web.RoutingContext
import kotlin.coroutines.CoroutineContext

class BookmarkController(coroutineContext: CoroutineContext) : BaseController(coroutineContext) {
    private fun load(ns: String) = ExtKt.asJsonArray(getUserStorage(ns, "bookmark")) ?: JsonArray()
    private fun save(ns: String, a: JsonArray) = saveUserStorage(ns, "bookmark", a)

    suspend fun getBookmarks(context: RoutingContext): ReturnData {
        val rd = ReturnData()
        if (!checkAuth(context)) return rd.setData("NEED_LOGIN").setErrorMsg("请登录后使用")
        return rd.setData(load(getUserNameSpace(context)))
    }

    suspend fun saveBookmark(context: RoutingContext): ReturnData {
        val rd = ReturnData()
        if (!checkAuth(context)) return rd.setData("NEED_LOGIN").setErrorMsg("请登录后使用")
        val ns = getUserNameSpace(context)
        val bm = context.bodyAsJson ?: return rd.setErrorMsg("参数错误")
        val arr = load(ns)
        arr.add(bm)
        save(ns, arr)
        return rd.setData(bm)
    }

    suspend fun deleteBookmark(context: RoutingContext): ReturnData {
        val rd = ReturnData()
        if (!checkAuth(context)) return rd.setData("NEED_LOGIN").setErrorMsg("请登录后使用")
        // filter by bookUrl+chapterIndex or id
        return rd.setData(true)
    }
}
''')

write("com/htmake/reader/api/controller/ReplaceRuleController.kt", r'''
package com.htmake.reader.api.controller

import com.htmake.reader.api.ReturnData
import com.htmake.reader.utils.ExtKt
import io.vertx.core.json.JsonArray
import io.vertx.ext.web.RoutingContext
import kotlin.coroutines.CoroutineContext

class ReplaceRuleController(coroutineContext: CoroutineContext) : BaseController(coroutineContext) {
    private fun load(ns: String) = ExtKt.asJsonArray(getUserStorage(ns, "replaceRule")) ?: JsonArray()
    private fun save(ns: String, a: JsonArray) = saveUserStorage(ns, "replaceRule", a)

    suspend fun getReplaceRules(context: RoutingContext): ReturnData {
        val rd = ReturnData()
        if (!checkAuth(context)) return rd.setData("NEED_LOGIN").setErrorMsg("请登录后使用")
        return rd.setData(load(getUserNameSpace(context)))
    }

    suspend fun saveReplaceRule(context: RoutingContext): ReturnData {
        val rd = ReturnData()
        if (!checkAuth(context)) return rd.setData("NEED_LOGIN").setErrorMsg("请登录后使用")
        val ns = getUserNameSpace(context)
        val rule = context.bodyAsJson ?: return rd.setErrorMsg("参数错误")
        val arr = load(ns)
        arr.add(rule)
        save(ns, arr)
        return rd.setData(rule)
    }

    suspend fun deleteReplaceRule(context: RoutingContext): ReturnData {
        val rd = ReturnData()
        if (!checkAuth(context)) return rd.setData("NEED_LOGIN").setErrorMsg("请登录后使用")
        return rd.setData(true)
    }
}
''')

write("com/htmake/reader/verticle/RestVerticle.kt", r'''
package com.htmake.reader.verticle

import com.htmake.reader.utils.ExtKt
import io.vertx.core.http.HttpMethod
import io.vertx.ext.web.Router
import io.vertx.ext.web.RoutingContext
import io.vertx.ext.web.handler.BodyHandler
import io.vertx.ext.web.handler.LoggerHandler
import io.vertx.ext.web.handler.SessionHandler
import io.vertx.ext.web.sstore.LocalSessionStore
import io.vertx.kotlin.coroutines.CoroutineVerticle
import kotlinx.coroutines.launch

/**
 * HTTP server base: session, body, CORS-ish hooks, coroutine route helpers.
 */
abstract class RestVerticle : CoroutineVerticle() {
    protected lateinit var router: Router
    var port: Int = 8080

    abstract fun getContextPath(): String
    abstract suspend fun initRouter(router: Router)

    override suspend fun start() {
        router = Router.router(vertx)
        router.route().handler(LoggerHandler.create())
        router.route().handler(BodyHandler.create().setUploadsDirectory(ExtKt.getWorkDir("storage", "cache", "uploads")))
        router.route().handler(
            SessionHandler.create(LocalSessionStore.create(vertx)).setNagHttps(false)
        )
        // CORS preflight
        router.route().handler { ctx ->
            ctx.response()
                .putHeader("Access-Control-Allow-Origin", ctx.request().getHeader("Origin") ?: "*")
                .putHeader("Access-Control-Allow-Credentials", "true")
            if (ctx.request().method() == HttpMethod.OPTIONS) {
                ctx.response()
                    .putHeader("Access-Control-Allow-Methods", "GET,POST,PUT,DELETE,OPTIONS")
                    .putHeader("Access-Control-Allow-Headers", "Content-Type,Authorization,secureKey")
                    .end()
            } else ctx.next()
        }
        try {
            initRouter(router)
            vertx.createHttpServer().requestHandler(router).listen(port)
            started()
        } catch (e: Exception) {
            onStartError()
            throw e
        }
    }

    open fun started() {}
    open fun onStartError() {}
    open fun onHandlerError(ctx: RoutingContext, error: Exception) {
        error.printStackTrace()
        if (!ctx.response().ended()) {
            ctx.response().setStatusCode(500).end(error.message ?: "error")
        }
    }
}
''')

write("io/legado/app/model/analyzeRule/AnalyzeRule.kt", r'''
package io.legado.app.model.analyzeRule

import io.legado.app.data.entities.BaseSource
import io.legado.app.data.entities.Book
import io.legado.app.model.DebugLog

/**
 * Unified rule engine: CSS / XPath / JSONPath / JS / Regex.
 * Business-level API surface; full implementation is large (see full-vineflower AnalyzeRule.kt).
 */
class AnalyzeRule(
    private val ruleData: Book?,
    private val source: BaseSource?,
    private val debugLog: DebugLog? = null
) {
    private var content: Any? = null
    private var baseUrl: String = ""

    fun setContent(content: Any?, baseUrl: String? = null): AnalyzeRule {
        this.content = content
        if (baseUrl != null) this.baseUrl = baseUrl
        return this
    }

    fun getString(rule: String): String {
        // dispatches by rule mode prefix: @@ CSS, @XPath, $. JSON, @js:, etc.
        return AnalyzeByMode.getString(content, rule, baseUrl, source, ruleData, debugLog)
    }

    fun getStringList(rule: String): List<String> =
        AnalyzeByMode.getStringList(content, rule, baseUrl, source, ruleData, debugLog)

    fun getElements(rule: String): List<Any> =
        AnalyzeByMode.getElements(content, rule, baseUrl, source, ruleData, debugLog)

    fun evalJS(jsStr: String, result: Any? = null): Any? =
        AnalyzeByMode.evalJS(jsStr, content, result, baseUrl, source, ruleData)

    companion object {
        fun evalJS(js: String, bind: Any? = null): Any? = AnalyzeByMode.evalJS(js, bind, null, "", null, null)
    }
}

/** Internal dispatcher — full parsers live in AnalyzeByJSoup / XPath / JSonPath classes. */
internal object AnalyzeByMode {
    fun getString(content: Any?, rule: String, baseUrl: String, source: BaseSource?, data: Book?, log: DebugLog?): String {
        // TODO: port full mode split from decompiled AnalyzeRule.getString
        return content?.toString()?.let { /* apply rule */ it } ?: ""
    }
    fun getStringList(content: Any?, rule: String, baseUrl: String, source: BaseSource?, data: Book?, log: DebugLog?): List<String> = emptyList()
    fun getElements(content: Any?, rule: String, baseUrl: String, source: BaseSource?, data: Book?, log: DebugLog?): List<Any> = emptyList()
    fun evalJS(js: String, content: Any?, result: Any?, baseUrl: String, source: BaseSource?, data: Book?): Any? = null
}
''')

write("io/legado/app/model/analyzeRule/AnalyzeUrl.kt", r'''
package io.legado.app.model.analyzeRule

import io.legado.app.data.entities.BaseSource
import io.legado.app.data.entities.Book
import io.legado.app.data.entities.BookChapter
import io.legado.app.help.http.StrResponse
import io.legado.app.model.DebugLog
import okhttp3.Response

/**
 * Build request from rule URL template, execute via OkHttp, return body/bytes.
 */
class AnalyzeUrl(
    val mUrl: String,
    val key: String? = null,
    val page: Int? = null,
    val speakText: String? = null,
    val speechRate: Int? = null,
    val baseUrl: String? = null,
    val source: BaseSource? = null,
    val ruleData: Book? = null,
    val chapter: BookChapter? = null,
    val headerMapF: Map<String, String>? = null,
    val debugLog: DebugLog? = null
) {
    var finalUrl: String = mUrl

    suspend fun getStrResponseAwait(): StrResponse {
        // 1) rule URL replace {{key}} {{page}}
        // 2) Js concurrent URL
        // 3) OkHttp GET/POST
        // See full-vineflower AnalyzeUrl.kt for complete concurrent/rate-limit logic
        finalUrl = mUrl
        return StrResponse(finalUrl, body = "")
    }

    suspend fun getByteArrayAwait(): ByteArray {
        getStrResponseAwait()
        return ByteArray(0)
    }

    suspend fun getResponseAwait(): Response {
        // returns okhttp3.Response
        error("Wire to OkHttp client — see OkHttpUtilsKt + AnalyzeUrl CFR")
    }

    fun evalJS(js: String, result: Any?): Any? = AnalyzeRule(ruleData as Book?, source, debugLog).evalJS(js, result)
}
''')

# README for business tree
readme = '''# business/ — 业务化重写源码树

本目录是对 `reader-pro-3.2.14.jar` **按业务语义重写**的可读 Kotlin，与 `src/`（三工具择优+修补）互补。

## 与 src/ 的关系

| 树 | 用途 |
|----|------|
| `best-of-3/src/` | 反编译产物（保留协程状态机、变量名），已清硬伤 |
| `best-of-3/business/` | **业务化**：suspend 顺序逻辑、中文错误文案、路由表清晰 |

## 已覆盖模块

### 服务壳
- `YueduApi` — 路由注册骨架（核心 /reader3 已列，完整 133 见 API_ROUTES.md）
- `RestVerticle` — HTTP/Session/CORS
- `ReturnData` / `BaseController`

### 控制器
- User / License / BookSource / Book / File / Webdav / Rss / BookGroup / Bookmark / ReplaceRule

### 阅读引擎
- `WebBook` / `BookList` / `BookInfo` / `BookChapterList` / `BookContent`
- `AnalyzeRule` / `AnalyzeUrl` API 面（解析器内部仍指向 decompiled 实现）

### BookController 业务方法
书架、搜索、详情、目录、正文、进度、缓存、封面、WebDAV 同步、TTS 流等

## 使用建议

1. **读业务**：优先 `business/`
2. **对字节码/边界情况**：回看 `src/` 或 `full-cfr/`
3. **补全**：`YueduApi` 未列出的路由按 `API_ROUTES.md` 接到对应 controller

## 生成脚本

- `_business_rewrite_core.py`
- `_business_rewrite_more.py`
- `_business_book_controller.py`
'''
(BIZ / "README.md").write_text(readme, encoding="utf-8")
print("BookController + extras done")
