package com.htmake.reader.api.controller

import com.htmake.reader.api.ReturnData
import com.htmake.reader.utils.ExtKt
import com.htmake.reader.utils.UserMutex
import io.legado.app.data.entities.Book
import io.legado.app.data.entities.BookChapter
import io.legado.app.data.entities.BookSource
import io.legado.app.help.ContentProcessor
import io.legado.app.model.localBook.LocalBook
import io.legado.app.model.webBook.WebBook
import io.legado.app.utils.ACache
import io.legado.app.utils.MD5Utils
import io.vertx.core.http.HttpMethod
import io.vertx.core.json.JsonArray
import io.vertx.core.json.JsonObject
import io.vertx.ext.web.RoutingContext
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import java.io.File
import kotlin.coroutines.CoroutineContext

class BookController(coroutineContext: CoroutineContext) : BaseController(coroutineContext) {

    suspend fun getBookshelf(context: RoutingContext): ReturnData {
        val rd = ReturnData()
        if (!checkAuth(context)) return rd.setData("NEED_LOGIN").setErrorMsg("请登录后使用")
        val ns = getUserNameSpace(context)
        val arr = ExtKt.asJsonArray(getUserStorage(ns, "bookshelf")) ?: JsonArray()
        val books = (0 until arr.size()).map { arr.getJsonObject(it).mapTo(Book::class.java).apply { isInShelf = true } }
        return rd.setData(books)
    }

    suspend fun getShelfBook(context: RoutingContext): ReturnData {
        val rd = ReturnData()
        if (!checkAuth(context)) return rd.setData("NEED_LOGIN").setErrorMsg("请登录后使用")
        val url = param(context, "url") ?: param(context, "bookUrl") ?: ""
        val book = getShelfBookByURL(url, getUserNameSpace(context)) ?: return rd.setErrorMsg("书籍不存在")
        return rd.setData(book)
    }

    suspend fun saveBook(context: RoutingContext): ReturnData {
        val rd = ReturnData()
        if (!checkAuth(context)) return rd.setData("NEED_LOGIN").setErrorMsg("请登录后使用")
        val ns = getUserNameSpace(context)
        val book = context.bodyAsJson?.mapTo(Book::class.java) ?: return rd.setErrorMsg("参数错误")
        val mutex = UserMutex.getLocker("$ns@bookshelf")
        mutex.withLock {
            val arr = ExtKt.asJsonArray(getUserStorage(ns, "bookshelf")) ?: JsonArray()
            val list = arr.list
            var found = false
            for (i in list.indices) {
                val o = arr.getJsonObject(i)
                if (o.getString("bookUrl") == book.bookUrl) {
                    list[i] = JsonObject.mapFrom(book); found = true; break
                }
            }
            if (!found) list.add(JsonObject.mapFrom(book))
            saveUserStorage(ns, "bookshelf", JsonArray(list))
        }
        return rd.setData(book)
    }

    suspend fun deleteBook(context: RoutingContext): ReturnData {
        val rd = ReturnData()
        if (!checkAuth(context)) return rd.setData("NEED_LOGIN").setErrorMsg("请登录后使用")
        val ns = getUserNameSpace(context)
        val url = context.bodyAsJson?.getString("bookUrl") ?: param(context, "bookUrl") ?: return rd.setErrorMsg("参数错误")
        val arr = ExtKt.asJsonArray(getUserStorage(ns, "bookshelf")) ?: JsonArray()
        val list = arr.list.filterIndexed { i, _ -> arr.getJsonObject(i).getString("bookUrl") != url }
        saveUserStorage(ns, "bookshelf", JsonArray(list))
        return rd.setData(true)
    }

    suspend fun searchBook(context: RoutingContext): ReturnData {
        val rd = ReturnData()
        if (!checkAuth(context)) return rd.setData("NEED_LOGIN").setErrorMsg("请登录后使用")
        val key = param(context, "key") ?: return rd.setErrorMsg("请输入关键字")
        val page = param(context, "page")?.toIntOrNull() ?: 1
        val ns = getUserNameSpace(context)
        val sourceUrl = param(context, "bookSourceUrl")
        val source = sourceUrl?.let { getBookSourceStringBySourceURLOpt(it, ns) }
            ?: return rd.setErrorMsg("书源信息错误")
        val list = try {
            WebBook(source, appConfig.debugLog, null, ns).searchBook(key, page)
        } catch (e: Exception) {
            return rd.setErrorMsg(e.message ?: "搜索失败")
        }
        return rd.setData(list)
    }

    suspend fun getBookInfo(context: RoutingContext): ReturnData {
        val rd = ReturnData()
        if (!checkAuth(context)) return rd.setData("NEED_LOGIN").setErrorMsg("请登录后使用")
        val ns = getUserNameSpace(context)
        val url = param(context, "url") ?: param(context, "bookUrl") ?: return rd.setErrorMsg("url 不能为空")
        val sourceUrl = param(context, "bookSourceUrl")
        val source = sourceUrl?.let { getBookSourceStringBySourceURLOpt(it, ns) }
            ?: getBookSourceStringByBookUrl(url, ns)
            ?: return rd.setErrorMsg("书源信息错误")
        val book = WebBook(source, appConfig.debugLog, null, ns).getBookInfo(url)
        return rd.setData(book)
    }

    suspend fun getChapterList(context: RoutingContext): ReturnData {
        val rd = ReturnData()
        if (!checkAuth(context)) return rd.setData("NEED_LOGIN").setErrorMsg("请登录后使用")
        val ns = getUserNameSpace(context)
        val book = resolveBook(context, ns) ?: return rd.setErrorMsg("书籍信息错误")
        val sourceStr = getBookSourceString(book, ns)
        val refresh = param(context, "refresh")?.toBoolean() ?: false
        val chapters = getLocalChapterList(book, sourceStr, refresh, ns)
        return rd.setData(chapters)
    }

    suspend fun getBookContent(context: RoutingContext): ReturnData {
        val rd = ReturnData()
        if (!checkAuth(context)) return rd.setData("NEED_LOGIN").setErrorMsg("请登录后使用")
        val ns = getUserNameSpace(context)
        val book = resolveBook(context, ns) ?: return rd.setErrorMsg("书籍信息错误")
        val index = param(context, "index")?.toIntOrNull() ?: 0
        val sourceStr = getBookSourceString(book, ns)
        val chapters = getLocalChapterList(book, sourceStr, false, ns)
        if (index !in chapters.indices) return rd.setErrorMsg("章节不存在")
        val chapter = chapters[index]
        val nextUrl = chapters.getOrNull(index + 1)?.url
        val cacheDir = getChapterCacheDir(book, ns)
        val cacheFile = File(cacheDir, "$index.txt")
        if (cacheFile.exists() && param(context, "refresh") != "true") {
            return rd.setData(cacheFile.readText())
        }
        var content = if (book.isLocalBook) {
            LocalBook.getContent(book, chapter) ?: ""
        } else {
            val src = sourceStr ?: return rd.setErrorMsg("书源信息错误")
            WebBook(src, appConfig.debugLog, null, ns).getBookContent(book, chapter, nextUrl)
        }
        content = ContentProcessor.applyContent(ns, book, content)
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

    suspend fun getLocalChapterList(
        book: Book,
        bookSource: String?,
        refresh: Boolean,
        userNameSpace: String
    ): List<BookChapter> {
        val md5 = MD5Utils.md5Encode(book.bookUrl)
        if (book.isInShelf && !refresh) {
            val cached = ExtKt.asJsonArray(getUserStorage(userNameSpace, "${book.name}_${book.author}", md5))
            if (cached != null) {
                return (0 until cached.size()).map { cached.getJsonObject(it).mapTo(BookChapter::class.java) }
            }
        }
        book.rootDir = ExtKt.getWorkDir()
        book.namespace = userNameSpace
        val newList: List<BookChapter> = try {
            if (book.isLocalBook) LocalBook.getChapterList(book)
            else {
                if (bookSource.isNullOrEmpty()) error("书源信息错误")
                var b = book
                if (b.tocUrl.isBlank()) b = WebBook(bookSource, false, null, userNameSpace).getBookInfo(book.bookUrl)
                WebBook(bookSource, false, null, userNameSpace).getChapterList(b)
            }
        } catch (e: Exception) {
            if (!bookSource.isNullOrEmpty()) {
                BookSource.fromJson(bookSource).getOrNull()?.let {
                    addInvalidBookSource(it.bookSourceUrl, mapOf(
                        "sourceUrl" to it.bookSourceUrl,
                        "time" to System.currentTimeMillis(),
                        "error" to e.toString()
                    ), userNameSpace)
                }
            }
            throw e
        }
        if (book.isInShelf) {
            saveUserStorage(userNameSpace, ExtKt.getRelativePath("${book.name}_${book.author}", md5), newList)
        }
        return newList
    }

    suspend fun editShelfBook(book: Book, userNameSpace: String, handler: (Book) -> Book): Book? {
        val mutex = UserMutex.getLocker("$userNameSpace@bookshelf")
        return mutex.withLock {
            val bookshelf = ExtKt.asJsonArray(getUserStorage(userNameSpace, "bookshelf")) ?: return@withLock null
            var existIndex = -1
            for (i in 0 until bookshelf.size()) {
                val existing = bookshelf.getJsonObject(i).mapTo(Book::class.java)
                if (book.bookUrl.isNotEmpty() && existing.bookUrl == book.bookUrl) {
                    existIndex = i; break
                }
            }
            if (existIndex < 0) return@withLock null
            val list = bookshelf.list
            var exist = bookshelf.getJsonObject(existIndex).mapTo(Book::class.java)
            exist = handler(exist)
            list[existIndex] = JsonObject.mapFrom(exist)
            saveUserStorage(userNameSpace, "bookshelf", JsonArray(list))
            exist
        }
    }

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

    fun getChapterCacheDir(book: Book, userNameSpace: String): File {
        val md5 = MD5Utils.md5Encode(book.bookUrl)
        return File(ExtKt.getWorkDir("storage", "data", userNameSpace, "cache", md5))
    }

    fun getInvalidBookSourceCache(userNameSpace: String): ACache =
        ACache.get(File(ExtKt.getWorkDir("storage", "cache", "invalidBookSourceCache", userNameSpace)))

    fun isInvalidBookSource(bookSourceUrl: String, userNameSpace: String): Boolean =
        getInvalidBookSourceCache(userNameSpace).getAsString(bookSourceUrl) != null

    fun addInvalidBookSource(sourceUrl: String, invalidInfo: Map<String, Any?>, userNameSpace: String) {
        getInvalidBookSourceCache(userNameSpace).put(sourceUrl, ExtKt.jsonEncode(invalidInfo), 600)
    }

    private fun resolveBook(context: RoutingContext, ns: String): Book? {
        val url = param(context, "url") ?: param(context, "bookUrl") ?: return null
        return getShelfBookByURL(url, ns) ?: context.bodyAsJson?.mapTo(Book::class.java)
    }

    private fun param(ctx: RoutingContext, key: String): String? {
        if (ctx.request().method() == HttpMethod.POST) {
            ctx.bodyAsJson?.getString(key)?.let { return it }
        }
        return ctx.queryParam(key).firstOrNull()
    }
}
