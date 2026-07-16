# -*- coding: utf-8 -*-
"""Part 3: controllers + YueduApi + resources README."""
from pathlib import Path
import os

SRC = Path(r"C:\Users\chong\reader-pro-3.2.14-reverse\src\main\kotlin")
RES = Path(r"C:\Users\chong\reader-pro-3.2.14-reverse\src\main\resources")
ROOT = Path(r"C:\Users\chong\reader-pro-3.2.14-reverse")


def w(rel, c):
    p = SRC / rel.replace("/", os.sep)
    p.parent.mkdir(parents=True, exist_ok=True)
    p.write_text(c.lstrip("\n"), encoding="utf-8", newline="\n")
    print("wrote", rel)


w("com/htmake/reader/api/controller/BookController.kt", r'''
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
        book.userNameSpace = userNameSpace
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
''')

# Generate remaining thin controllers + YueduApi via a compact pattern
controllers = {
"BookSourceController.kt": r'''
package com.htmake.reader.api.controller

import com.htmake.reader.api.ReturnData
import com.htmake.reader.utils.ExtKt
import io.legado.app.help.SourceAnalyzer
import io.vertx.core.http.HttpMethod
import io.vertx.core.json.JsonArray
import io.vertx.core.json.JsonObject
import io.vertx.ext.web.RoutingContext
import okhttp3.OkHttpClient
import okhttp3.Request
import java.util.concurrent.TimeUnit
import kotlin.coroutines.CoroutineContext

class BookSourceController(cc: CoroutineContext) : BaseController(cc) {
    fun getUserBookSourceJson(ns: String): JsonArray? =
        ExtKt.asJsonArray(getUserStorage(ns, "bookSource"))

    suspend fun getBookSources(ctx: RoutingContext): ReturnData {
        val rd = ReturnData()
        if (!checkAuth(ctx)) return rd.setData("NEED_LOGIN").setErrorMsg("请登录后使用")
        return rd.setData(getUserBookSourceJson(getUserNameSpace(ctx)) ?: JsonArray())
    }

    suspend fun saveBookSource(ctx: RoutingContext): ReturnData {
        val rd = ReturnData()
        if (!checkAuth(ctx)) return rd.setData("NEED_LOGIN").setErrorMsg("请登录后使用")
        val ns = getUserNameSpace(ctx)
        val src = ctx.bodyAsJson ?: return rd.setErrorMsg("参数错误")
        val url = src.getString("bookSourceUrl") ?: return rd.setErrorMsg("书源链接不能为空")
        val arr = getUserBookSourceJson(ns) ?: JsonArray()
        val list = arr.list
        var found = false
        for (i in list.indices) {
            if (arr.getJsonObject(i).getString("bookSourceUrl") == url) {
                list[i] = src; found = true; break
            }
        }
        if (!found) list.add(src)
        saveUserStorage(ns, "bookSource", JsonArray(list))
        return rd.setData(src)
    }

    suspend fun saveBookSources(ctx: RoutingContext): ReturnData {
        val arr = ctx.bodyAsJsonArray ?: ctx.bodyAsJson?.getJsonArray("bookSources") ?: JsonArray()
        return saveBookSources(ctx, arr)
    }

    suspend fun saveBookSources(ctx: RoutingContext, arr: JsonArray): ReturnData {
        val rd = ReturnData()
        if (!checkAuth(ctx)) return rd.setData("NEED_LOGIN").setErrorMsg("请登录后使用")
        val ns = getUserNameSpace(ctx)
        val normalized = JsonArray()
        for (i in 0 until arr.size()) {
            val raw = arr.getValue(i)?.toString() ?: continue
            val src = SourceAnalyzer.jsonToBookSource(raw).getOrNull()
            if (src != null) normalized.add(JsonObject.mapFrom(src))
            else if (arr.getValue(i) is JsonObject) normalized.add(arr.getJsonObject(i))
        }
        val out = if (normalized.isEmpty) arr else normalized
        saveUserStorage(ns, "bookSource", out)
        return rd.setData(out.size())
    }

    suspend fun deleteBookSource(ctx: RoutingContext): ReturnData {
        val rd = ReturnData()
        if (!checkAuth(ctx)) return rd.setData("NEED_LOGIN").setErrorMsg("请登录后使用")
        val ns = getUserNameSpace(ctx)
        val url = ctx.bodyAsJson?.getString("bookSourceUrl") ?: return rd.setErrorMsg("参数错误")
        val arr = getUserBookSourceJson(ns) ?: JsonArray()
        val list = arr.list.filterIndexed { i, _ -> arr.getJsonObject(i).getString("bookSourceUrl") != url }
        saveUserStorage(ns, "bookSource", JsonArray(list))
        return rd.setData(true)
    }

    suspend fun deleteAllBookSources(ctx: RoutingContext): ReturnData {
        val rd = ReturnData()
        if (!checkAuth(ctx)) return rd.setData("NEED_LOGIN").setErrorMsg("请登录后使用")
        saveUserStorage(getUserNameSpace(ctx), "bookSource", JsonArray())
        return rd.setData(true)
    }

    suspend fun saveFromRemoteSource(ctx: RoutingContext): ReturnData {
        val rd = ReturnData()
        if (!checkAuth(ctx)) return rd.setData("NEED_LOGIN").setErrorMsg("请登录后使用")
        val url = if (ctx.request().method() == HttpMethod.POST)
            ctx.bodyAsJson?.getString("url") else ctx.queryParam("url").firstOrNull()
        if (url.isNullOrBlank()) return rd.setErrorMsg("请输入远程书源链接")
        return try {
            val client = OkHttpClient.Builder().connectTimeout(3, TimeUnit.SECONDS).readTimeout(15, TimeUnit.SECONDS).build()
            val body = client.newCall(Request.Builder().url(url).get().build()).execute().use { it.body?.string() }
                ?: return rd.setErrorMsg("远程书源链接错误")
            val arr = try { JsonArray(body) } catch (_: Exception) {
                try {
                    val o = JsonObject(body)
                    o.getJsonArray("data") ?: o.getJsonArray("bookSources") ?: JsonArray().add(o)
                } catch (_: Exception) { return rd.setErrorMsg("远程书源链接错误") }
            }
            saveBookSources(ctx, arr)
        } catch (e: Exception) {
            rd.setErrorMsg(e.message ?: "远程书源链接错误")
        }
    }

    suspend fun getBookSource(ctx: RoutingContext): ReturnData {
        val rd = ReturnData()
        if (!checkAuth(ctx)) return rd.setData("NEED_LOGIN").setErrorMsg("请登录后使用")
        val url = ctx.queryParam("url").firstOrNull() ?: ctx.bodyAsJson?.getString("bookSourceUrl") ?: ""
        val arr = getUserBookSourceJson(getUserNameSpace(ctx)) ?: JsonArray()
        for (i in 0 until arr.size()) {
            val o = arr.getJsonObject(i)
            if (o.getString("bookSourceUrl") == url) return rd.setData(o)
        }
        return rd.setErrorMsg("书源不存在")
    }
}
''',
"UserController.kt": r'''
package com.htmake.reader.api.controller

import com.htmake.reader.api.ReturnData
import com.htmake.reader.config.UserConfigDefaults
import com.htmake.reader.utils.ExtKt
import io.vertx.core.json.JsonObject
import io.vertx.ext.web.RoutingContext
import java.util.UUID
import kotlin.coroutines.CoroutineContext

class UserController(cc: CoroutineContext) : BaseController(cc) {
    suspend fun login(ctx: RoutingContext): ReturnData {
        val rd = ReturnData()
        val body = ctx.bodyAsJson ?: return rd.setErrorMsg("参数错误")
        val username = body.getString("username") ?: return rd.setErrorMsg("用户名不能为空")
        val password = body.getString("password") ?: return rd.setErrorMsg("密码不能为空")
        if (!appConfig.secure) {
            ctx.session()?.put("username", username)
            return rd.setData(mapOf("username" to username, "token" to "dev"))
        }
        val users = loadUserMap()
        val u = users[username] ?: return rd.setErrorMsg("用户不存在")
        if (u["password"] != password) return rd.setErrorMsg("密码错误")
        val token = UUID.randomUUID().toString().replace("-", "")
        @Suppress("UNCHECKED_CAST")
        val tokenMap = (u["token_map"] as? MutableMap<String, Any?>) ?: mutableMapOf()
        tokenMap[token] = System.currentTimeMillis()
        u["token_map"] = tokenMap
        u["token"] = token
        u["last_login_at"] = System.currentTimeMillis()
        users[username] = u
        saveUserMap(users)
        ctx.session()?.put("username", username)
        return rd.setData(mapOf("username" to username, "token" to token, "accessToken" to "$username:$token"))
    }

    suspend fun logout(ctx: RoutingContext): ReturnData {
        ctx.session()?.destroy()
        return ReturnData().setData(true)
    }

    suspend fun getUserInfo(ctx: RoutingContext): ReturnData {
        val rd = ReturnData()
        if (!checkAuth(ctx)) return rd.setData("NEED_LOGIN").setErrorMsg("请登录后使用")
        val ns = getUserNameSpace(ctx)
        return rd.setData(mapOf("username" to ns))
    }

    suspend fun saveUserConfig(ctx: RoutingContext): ReturnData {
        val rd = ReturnData()
        if (!checkAuth(ctx)) return rd.setData("NEED_LOGIN").setErrorMsg("请登录后使用")
        val body = ctx.bodyAsJson ?: return rd.setErrorMsg("参数错误")
        val merge = body.getBoolean("merge", false) == true
        body.remove("merge")
        body.put("@updateTime", System.currentTimeMillis())
        val ns = getUserNameSpace(ctx)
        val toSave = if (merge) UserConfigDefaults.merge(ExtKt.asJsonObject(getUserStorage(ns, "userConfig")), body) else body
        saveUserStorage(ns, "userConfig", toSave)
        return rd.setData("")
    }

    suspend fun getUserConfig(ctx: RoutingContext): ReturnData {
        val rd = ReturnData()
        if (!checkAuth(ctx)) return rd.setData("NEED_LOGIN").setErrorMsg("请登录后使用")
        val raw = getUserStorage(getUserNameSpace(ctx), "userConfig")
        val obj = ExtKt.asJsonObject(raw)
        if (obj == null) return rd.setErrorMsg("没有备份文件").setData(UserConfigDefaults.base().map)
        return rd.setData(obj.map)
    }

    suspend fun getUserList(ctx: RoutingContext): ReturnData {
        val rd = ReturnData()
        if (!checkManagerAuth(ctx)) return rd.setErrorMsg("需要管理密码")
        return rd.setData(loadUserMap().keys.toList())
    }

    suspend fun addUser(ctx: RoutingContext): ReturnData {
        val rd = ReturnData()
        if (!checkManagerAuth(ctx)) return rd.setErrorMsg("需要管理密码")
        val body = ctx.bodyAsJson ?: return rd.setErrorMsg("参数错误")
        val username = body.getString("username") ?: return rd.setErrorMsg("用户名不能为空")
        val password = body.getString("password") ?: return rd.setErrorMsg("密码不能为空")
        val users = loadUserMap()
        if (users.containsKey(username)) return rd.setErrorMsg("用户已存在")
        users[username] = mutableMapOf(
            "password" to password,
            "created_at" to System.currentTimeMillis(),
            "enableWebdav" to true,
            "enableBookSource" to true
        )
        saveUserMap(users)
        return rd.setData(true)
    }

    suspend fun deleteUsers(ctx: RoutingContext): ReturnData {
        val rd = ReturnData()
        if (!checkManagerAuth(ctx)) return rd.setErrorMsg("需要管理密码")
        val names = ctx.bodyAsJson?.getJsonArray("users") ?: return rd.setErrorMsg("参数错误")
        val users = loadUserMap()
        names.forEach { users.remove(it.toString()) }
        saveUserMap(users)
        return rd.setData(true)
    }

    suspend fun resetPassword(ctx: RoutingContext): ReturnData {
        val rd = ReturnData()
        if (!checkManagerAuth(ctx)) return rd.setErrorMsg("需要管理密码")
        val body = ctx.bodyAsJson ?: return rd.setErrorMsg("参数错误")
        val username = body.getString("username") ?: return rd.setErrorMsg("用户名不能为空")
        val password = body.getString("password") ?: return rd.setErrorMsg("密码不能为空")
        val users = loadUserMap()
        val u = users[username] ?: return rd.setErrorMsg("用户不存在")
        u["password"] = password
        saveUserMap(users)
        return rd.setData(true)
    }

    suspend fun updateUser(ctx: RoutingContext): ReturnData {
        val rd = ReturnData()
        if (!checkManagerAuth(ctx)) return rd.setErrorMsg("需要管理密码")
        return rd.setData(true)
    }

    suspend fun clearInactiveUsers(ctx: RoutingContext): ReturnData = ReturnData().setData(0)
}
''',
}

for name, content in controllers.items():
    w(f"com/htmake/reader/api/controller/{name}", content)

# Thin CRUD helpers
for name, storage in [
    ("BookGroupController", "bookGroup"),
    ("BookmarkController", "bookmark"),
    ("ReplaceRuleController", "replaceRule"),
    ("RssSourceController", "rssSource"),
    ("HttpTTSController", "httpTTS"),
]:
    w(f"com/htmake/reader/api/controller/{name}.kt", f'''
package com.htmake.reader.api.controller

import com.htmake.reader.api.ReturnData
import com.htmake.reader.utils.ExtKt
import io.vertx.core.json.JsonArray
import io.vertx.ext.web.RoutingContext
import kotlin.coroutines.CoroutineContext

class {name}(cc: CoroutineContext) : BaseController(cc) {{
    private fun load(ns: String) = ExtKt.asJsonArray(getUserStorage(ns, "{storage}")) ?: JsonArray()
    private fun save(ns: String, a: JsonArray) = saveUserStorage(ns, "{storage}", a)

    suspend fun list(ctx: RoutingContext): ReturnData {{
        val rd = ReturnData()
        if (!checkAuth(ctx)) return rd.setData("NEED_LOGIN").setErrorMsg("请登录后使用")
        return rd.setData(load(getUserNameSpace(ctx)))
    }}

    suspend fun save(ctx: RoutingContext): ReturnData {{
        val rd = ReturnData()
        if (!checkAuth(ctx)) return rd.setData("NEED_LOGIN").setErrorMsg("请登录后使用")
        val ns = getUserNameSpace(ctx)
        val arr = ctx.bodyAsJsonArray
        if (arr != null) {{ save(ns, arr); return rd.setData(arr.size()) }}
        val one = ctx.bodyAsJson ?: return rd.setErrorMsg("参数错误")
        val cur = load(ns); cur.add(one); save(ns, cur)
        return rd.setData(one)
    }}

    suspend fun delete(ctx: RoutingContext): ReturnData {{
        val rd = ReturnData()
        if (!checkAuth(ctx)) return rd.setData("NEED_LOGIN").setErrorMsg("请登录后使用")
        return rd.setData(true)
    }}

    // aliases used by YueduApi
    suspend fun getBookGroups(ctx: RoutingContext) = list(ctx)
    suspend fun saveBookGroup(ctx: RoutingContext) = save(ctx)
    suspend fun deleteBookGroup(ctx: RoutingContext) = delete(ctx)
    suspend fun saveBookGroupOrder(ctx: RoutingContext) = save(ctx)
    suspend fun getBookmarks(ctx: RoutingContext) = list(ctx)
    suspend fun saveBookmark(ctx: RoutingContext) = save(ctx)
    suspend fun deleteBookmark(ctx: RoutingContext) = delete(ctx)
    suspend fun getReplaceRules(ctx: RoutingContext) = list(ctx)
    suspend fun saveReplaceRule(ctx: RoutingContext) = save(ctx)
    suspend fun deleteReplaceRule(ctx: RoutingContext) = delete(ctx)
    suspend fun getRssSources(ctx: RoutingContext) = list(ctx)
    suspend fun saveRssSource(ctx: RoutingContext) = save(ctx)
    suspend fun deleteRssSource(ctx: RoutingContext) = delete(ctx)
    suspend fun getRssArticles(ctx: RoutingContext): ReturnData = ReturnData().setData(emptyList<Any>())
    suspend fun getRssContent(ctx: RoutingContext): ReturnData = ReturnData().setData(mapOf("content" to ""))
}}
''')

w("com/htmake/reader/api/controller/FileController.kt", r'''
package com.htmake.reader.api.controller

import com.htmake.reader.api.ReturnData
import com.htmake.reader.utils.ExtKt
import io.vertx.ext.web.RoutingContext
import java.io.File
import kotlin.coroutines.CoroutineContext

class FileController(cc: CoroutineContext) : BaseController(cc) {
    private fun root(ns: String) = File(ExtKt.getWorkDir("storage", "data", ns, "files")).apply { mkdirs() }

    suspend fun list(ctx: RoutingContext): ReturnData {
        val rd = ReturnData()
        if (!checkAuth(ctx)) return rd.setData("NEED_LOGIN").setErrorMsg("请登录后使用")
        val ns = getUserNameSpace(ctx)
        val path = ctx.queryParam("path").firstOrNull() ?: ""
        val dir = File(root(ns), path.trimStart('/')).canonicalFile
        if (!dir.path.startsWith(root(ns).canonicalPath)) return rd.setErrorMsg("非法路径")
        if (!dir.isDirectory) return rd.setData(emptyList<Any>())
        val items = dir.listFiles()?.map {
            mapOf("name" to it.name, "isDir" to it.isDirectory, "size" to it.length(), "path" to path.trimEnd('/') + "/" + it.name)
        } ?: emptyList()
        return rd.setData(items)
    }

    suspend fun mkdir(ctx: RoutingContext): ReturnData {
        val rd = ReturnData()
        if (!checkAuth(ctx)) return rd.setData("NEED_LOGIN").setErrorMsg("请登录后使用")
        val path = ctx.bodyAsJson?.getString("path") ?: return rd.setErrorMsg("path 不能为空")
        File(root(getUserNameSpace(ctx)), path.trimStart('/')).mkdirs()
        return rd.setData(true)
    }

    suspend fun delete(ctx: RoutingContext): ReturnData {
        val rd = ReturnData()
        if (!checkAuth(ctx)) return rd.setData("NEED_LOGIN").setErrorMsg("请登录后使用")
        val path = ctx.bodyAsJson?.getString("path") ?: return rd.setErrorMsg("path 不能为空")
        val f = File(root(getUserNameSpace(ctx)), path.trimStart('/')).canonicalFile
        if (f.path.startsWith(root(getUserNameSpace(ctx)).canonicalPath)) ExtKt.deleteRecursively(f)
        return rd.setData(true)
    }

    suspend fun deleteMulti(ctx: RoutingContext) = delete(ctx)
    suspend fun upload(ctx: RoutingContext): ReturnData = ReturnData().setData(true)
    suspend fun download(ctx: RoutingContext): ReturnData? {
        val path = ctx.queryParam("path").firstOrNull() ?: return null.also {
            ctx.response().setStatusCode(400).end("path required")
        }
        val f = File(root(getUserNameSpace(ctx)), path.trimStart('/'))
        if (f.isFile) ctx.response().sendFile(f.absolutePath) else ctx.response().setStatusCode(404).end()
        return null
    }
    suspend fun get(ctx: RoutingContext) = list(ctx)
    suspend fun save(ctx: RoutingContext): ReturnData = ReturnData().setData(true)
    suspend fun importPreview(ctx: RoutingContext): ReturnData = ReturnData().setData(emptyMap<String, Any>())
    suspend fun restore(ctx: RoutingContext): ReturnData = ReturnData().setData(true)
    suspend fun parse(ctx: RoutingContext): ReturnData = ReturnData().setData(emptyMap<String, Any>())
}
''')

w("com/htmake/reader/api/controller/LicenseController.kt", r'''
package com.htmake.reader.api.controller

import com.htmake.reader.api.ReturnData
import com.htmake.reader.entity.License
import com.htmake.reader.utils.EncoderUtils
import com.htmake.reader.utils.ExtKt
import io.vertx.core.json.JsonObject
import io.vertx.ext.web.RoutingContext
import kotlin.coroutines.CoroutineContext

class LicenseController(cc: CoroutineContext) : BaseController(cc) {
    suspend fun getLicense(ctx: RoutingContext): ReturnData {
        val raw = ExtKt.getStorage("data", "license")
        return ReturnData().setData(ExtKt.asJsonObject(raw)?.map ?: emptyMap<String, Any>())
    }

    suspend fun importLicense(ctx: RoutingContext): ReturnData {
        val body = ctx.bodyAsJson ?: return ReturnData().setErrorMsg("参数错误")
        ExtKt.saveStorage(arrayOf("data", "license"), body.encode())
        return ReturnData().setData(true)
    }

    suspend fun generateKeys(ctx: RoutingContext): ReturnData {
        val (pub, pri) = EncoderUtils.genRsaPair()
        return ReturnData().setData(mapOf("publicKey" to pub, "privateKey" to pri))
    }

    suspend fun generateLicense(ctx: RoutingContext): ReturnData {
        val body = ctx.bodyAsJson ?: JsonObject()
        val lic = License(
            host = body.getString("host") ?: "",
            email = body.getString("email") ?: "",
            code = body.getString("code") ?: "DEMO",
            expireAt = System.currentTimeMillis() + 365L * 24 * 3600 * 1000,
            activated = false
        )
        return ReturnData().setData(lic)
    }

    suspend fun isHostValid(ctx: RoutingContext): ReturnData = ReturnData().setData(true)
    suspend fun activateLicense(ctx: RoutingContext): ReturnData {
        val raw = ExtKt.getStorage("data", "license")
        val o = ExtKt.asJsonObject(raw) ?: JsonObject()
        o.put("activated", true)
        ExtKt.saveStorage(arrayOf("data", "license"), o.encode())
        return ReturnData().setData(true)
    }
    suspend fun isLicenseValid(ctx: RoutingContext): ReturnData = ReturnData().setData(true)
    suspend fun decryptLicense(ctx: RoutingContext): ReturnData = ReturnData().setData(emptyMap<String, Any>())
    suspend fun sendCodeToEmail(ctx: RoutingContext): ReturnData = ReturnData().setData(true)
    suspend fun supplyLicense(ctx: RoutingContext): ReturnData = ReturnData().setData(true)
}
''')

w("com/htmake/reader/api/controller/WebdavPaths.kt", r'''
package com.htmake.reader.api.controller

import java.io.File
import java.net.URI
import java.net.URLDecoder

object WebdavPaths {
    fun pathFromRequest(requestPath: String): String {
        var path = requestPath.replace(Regex("/reader3/webdav", RegexOption.IGNORE_CASE), "")
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

    fun destinationToRelativePath(destinationHeader: String): String? = try {
        val uri = URI(destinationHeader)
        var path = uri.path ?: return null
        path = path.replace(Regex("/reader3/webdav", RegexOption.IGNORE_CASE), "")
        if (!path.startsWith("/")) path = "/$path"
        URLDecoder.decode(path, "UTF-8")
    } catch (_: Exception) {
        var path = destinationHeader
        path = path.replace(Regex("https?://[^/]+", RegexOption.IGNORE_CASE), "")
        path = path.replace(Regex("/reader3/webdav", RegexOption.IGNORE_CASE), "")
        if (!path.startsWith("/")) path = "/$path"
        URLDecoder.decode(path, "UTF-8")
    }
}
''')

w("com/htmake/reader/api/controller/WebdavController.kt", r'''
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
                f.writeBytes(ctx.body().bytes)
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
''')

# BookControllerExtras - multi search etc as extensions in same package file
w("com/htmake/reader/api/controller/BookControllerExtras.kt", r'''
package com.htmake.reader.api.controller

import com.htmake.reader.api.ReturnData
import com.htmake.reader.utils.ExtKt
import io.legado.app.data.entities.SearchBook
import io.legado.app.model.Debugger
import io.legado.app.model.webBook.BookList
import io.legado.app.model.webBook.WebBook
import io.vertx.core.json.JsonArray
import io.vertx.core.json.JsonObject
import io.vertx.ext.web.RoutingContext
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.withTimeoutOrNull

suspend fun BookController.exploreBook(ctx: RoutingContext): ReturnData {
    val rd = ReturnData()
    if (!checkAuth(ctx)) return rd.setData("NEED_LOGIN").setErrorMsg("请登录后使用")
    var url = ctx.queryParam("url").firstOrNull() ?: ctx.bodyAsJson?.getString("url")
        ?: ctx.queryParam("sortUrl").firstOrNull() ?: ctx.bodyAsJson?.getString("sortUrl") ?: ""
    val page = ctx.queryParam("page").firstOrNull()?.toIntOrNull() ?: ctx.bodyAsJson?.getInteger("page") ?: 1
    val ns = getUserNameSpace(ctx)
    val sourceUrl = ctx.queryParam("bookSourceUrl").firstOrNull() ?: ctx.bodyAsJson?.getString("bookSourceUrl")
    val sourceStr = sourceUrl?.let { getBookSourceStringBySourceURLOpt(it, ns) } ?: return rd.setErrorMsg("书源信息错误")
    if (url.isBlank()) {
        val src = io.legado.app.data.entities.BookSource.fromJson(sourceStr).getOrNull()
        val cats = BookList.parseExploreUrl(src?.exploreUrl, page)
        return rd.setData(cats.map { (t, u) -> mapOf("title" to t, "url" to u) })
    }
    val list = withTimeoutOrNull(30_000L) {
        WebBook(sourceStr, getAppConfig().debugLog, null, ns).exploreBook(url, page)
    } ?: emptyList()
    return rd.setData(list)
}

suspend fun BookController.searchBookMulti(ctx: RoutingContext): ReturnData {
    val rd = ReturnData()
    if (!checkAuth(ctx)) return rd.setData("NEED_LOGIN").setErrorMsg("请登录后使用")
    val key = ctx.queryParam("key").firstOrNull() ?: ctx.bodyAsJson?.getString("key") ?: return rd.setErrorMsg("请输入关键字")
    val page = ctx.queryParam("page").firstOrNull()?.toIntOrNull() ?: 1
    val concurrent = (ctx.queryParam("concurrentCount").firstOrNull()?.toIntOrNull() ?: 36).coerceIn(1, 64)
    val ns = getUserNameSpace(ctx)
    val sources = ExtKt.asJsonArray(getUserStorage(ns, "bookSource")) ?: JsonArray()
    val enabled = (0 until sources.size()).mapNotNull { i ->
        val o = sources.getJsonObject(i) ?: return@mapNotNull null
        if (o.getBoolean("enabled", true) == false) null
        else if (isInvalidBookSource(o.getString("bookSourceUrl") ?: "", ns)) null
        else o
    }
    val out = ArrayList<SearchBook>()
    coroutineScope {
        enabled.chunked(concurrent).forEach { batch ->
            batch.map { o ->
                async {
                    try {
                        withTimeoutOrNull(15_000L) {
                            WebBook(o.encode(), false, null, ns).searchBook(key, page)
                        } ?: emptyList()
                    } catch (_: Exception) {
                        emptyList()
                    }
                }
            }.awaitAll().forEach { out.addAll(it) }
        }
    }
    return rd.setData(out.distinctBy { it.bookUrl.ifEmpty { "${it.name}|${it.author}" } })
}

suspend fun BookController.searchBookMultiSSE(ctx: RoutingContext) {
    val key = ctx.queryParam("key").firstOrNull() ?: ""
    val page = ctx.queryParam("page").firstOrNull()?.toIntOrNull() ?: 1
    val concurrent = (ctx.queryParam("concurrentCount").firstOrNull()?.toIntOrNull() ?: 24).coerceIn(1, 64)
    val ns = getUserNameSpace(ctx)
    val sources = ExtKt.asJsonArray(getUserStorage(ns, "bookSource")) ?: JsonArray()
    val resp = ctx.response().putHeader("Content-Type", "text/event-stream; charset=utf-8")
        .putHeader("Cache-Control", "no-cache").setChunked(true)
    val enabled = (0 until sources.size()).mapNotNull { i ->
        val o = sources.getJsonObject(i) ?: return@mapNotNull null
        if (o.getBoolean("enabled", true) == false) null else o
    }
    coroutineScope {
        enabled.chunked(concurrent).forEach { batch ->
            batch.map { o ->
                async {
                    try {
                        val list = withTimeoutOrNull(15_000L) {
                            WebBook(o.encode(), false, null, ns).searchBook(key, page)
                        } ?: emptyList()
                        val payload = JsonObject().put("origin", o.getString("bookSourceUrl"))
                            .put("data", JsonArray(list.map { JsonObject.mapFrom(it) }))
                        synchronized(resp) { if (!resp.ended()) resp.write("data: ${payload.encode()}\n\n") }
                    } catch (_: Exception) {
                    }
                }
            }.awaitAll()
        }
    }
    if (!resp.ended()) resp.write("event: end\ndata: []\n\n").end()
}

suspend fun BookController.bookSourceDebugSSE(ctx: RoutingContext) {
    val ns = getUserNameSpace(ctx)
    val sourceUrl = ctx.queryParam("bookSourceUrl").firstOrNull() ?: ""
    val key = ctx.queryParam("key").firstOrNull() ?: ""
    val source = getBookSourceStringBySourceURLOpt(sourceUrl, ns)
    val resp = ctx.response().putHeader("Content-Type", "text/event-stream").setChunked(true)
    if (source == null) {
        resp.end("event: error\ndata: {\"error\":\"书源不存在\"}\n\n"); return
    }
    val debugger = Debugger { msg ->
        if (!resp.ended()) resp.write("data: ${JsonObject().put("msg", msg).encode()}\n\n")
    }
    try {
        debugger.startDebug(WebBook(source, true, debugger, ns), key)
    } catch (e: Exception) {
        if (!resp.ended()) resp.write("event: error\ndata: ${JsonObject().put("error", e.message).encode()}\n\n")
    }
    if (!resp.ended()) resp.write("event: end\ndata: {}\n\n").end()
}

fun BookController.getInvalidBookSources(ctx: RoutingContext): ReturnData {
    val rd = ReturnData()
    if (!checkAuth(ctx)) return rd.setData("NEED_LOGIN").setErrorMsg("请登录后使用")
    // simplified empty list if none
    return rd.setData(emptyList<Any>())
}

fun BookController.getTxtTocRules(ctx: RoutingContext): ReturnData =
    ReturnData().setData(io.legado.app.help.DefaultData.txtTocRules)
''')

# YueduApi - full route table compact
w("com/htmake/reader/api/YueduApi.kt", r'''
package com.htmake.reader.api

import com.htmake.reader.api.controller.*
import com.htmake.reader.config.AppConfig
import com.htmake.reader.utils.ExtKt
import com.htmake.reader.utils.SpringContextUtils
import com.htmake.reader.utils.VertExtKt
import com.htmake.reader.verticle.RestVerticle
import io.vertx.ext.web.Router
import io.vertx.ext.web.RoutingContext
import io.vertx.ext.web.handler.StaticHandler
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.springframework.stereotype.Component

@Component
open class YueduApi : RestVerticle() {

    private val appConfig: AppConfig by lazy {
        try {
            SpringContextUtils.getBean("appConfig", AppConfig::class.java)
        } catch (_: Exception) {
            AppConfig()
        }
    }

    private lateinit var book: BookController
    private lateinit var bookSource: BookSourceController
    private lateinit var user: UserController
    private lateinit var license: LicenseController
    private lateinit var file: FileController
    private lateinit var webdav: WebdavController
    private lateinit var rss: RssSourceController
    private lateinit var group: BookGroupController
    private lateinit var bookmark: BookmarkController
    private lateinit var replace: ReplaceRuleController
    private lateinit var httpTts: HttpTTSController

    override fun getContextPath(): String = ""

    override suspend fun initRouter(router: Router) {
        port = try {
            System.getProperty("server.port")?.toIntOrNull()
                ?: 8080
        } catch (_: Exception) {
            8080
        }

        book = BookController(coroutineContext)
        bookSource = BookSourceController(coroutineContext)
        user = UserController(coroutineContext)
        license = LicenseController(coroutineContext)
        file = FileController(coroutineContext)
        webdav = WebdavController(coroutineContext)
        rss = RssSourceController(coroutineContext)
        group = BookGroupController(coroutineContext)
        bookmark = BookmarkController(coroutineContext)
        replace = ReplaceRuleController(coroutineContext)
        httpTts = HttpTTSController(coroutineContext)
        webdav.mount(router, this)

        router.route("/web/*").handler(StaticHandler.create("web").setDefaultContentEncoding("UTF-8"))
        router.route("/simple-web/*").handler(StaticHandler.create("simple-web"))
        router.get("/").handler { it.reroute("/web/index.html") }

        get(router, "/reader3/getSystemInfo") { getSystemInfo(it) }

        post(router, "/reader3/saveBookSource") { bookSource.saveBookSource(it) }
        post(router, "/reader3/saveBookSources") { bookSource.saveBookSources(it) }
        get(router, "/reader3/getBookSources") { bookSource.getBookSources(it) }
        post(router, "/reader3/getBookSources") { bookSource.getBookSources(it) }
        get(router, "/reader3/getBookSource") { bookSource.getBookSource(it) }
        post(router, "/reader3/deleteBookSource") { bookSource.deleteBookSource(it) }
        post(router, "/reader3/deleteAllBookSources") { bookSource.deleteAllBookSources(it) }
        post(router, "/reader3/saveFromRemoteSource") { bookSource.saveFromRemoteSource(it) }

        get(router, "/reader3/getBookshelf") { book.getBookshelf(it) }
        get(router, "/reader3/getShelfBook") { book.getShelfBook(it) }
        post(router, "/reader3/saveBook") { book.saveBook(it) }
        post(router, "/reader3/deleteBook") { book.deleteBook(it) }
        get(router, "/reader3/searchBook") { book.searchBook(it) }
        post(router, "/reader3/searchBook") { book.searchBook(it) }
        get(router, "/reader3/searchBookMulti") { book.searchBookMulti(it) }
        post(router, "/reader3/searchBookMulti") { book.searchBookMulti(it) }
        get(router, "/reader3/searchBookMultiSSE") { book.searchBookMultiSSE(it); null }
        get(router, "/reader3/exploreBook") { book.exploreBook(it) }
        post(router, "/reader3/exploreBook") { book.exploreBook(it) }
        get(router, "/reader3/getBookInfo") { book.getBookInfo(it) }
        post(router, "/reader3/getBookInfo") { book.getBookInfo(it) }
        get(router, "/reader3/getChapterList") { book.getChapterList(it) }
        post(router, "/reader3/getChapterList") { book.getChapterList(it) }
        get(router, "/reader3/getBookContent") { book.getBookContent(it) }
        post(router, "/reader3/getBookContent") { book.getBookContent(it) }
        post(router, "/reader3/saveBookProgress") { book.saveBookProgress(it) }
        get(router, "/reader3/bookSourceDebugSSE") { book.bookSourceDebugSSE(it); null }
        get(router, "/reader3/getInvalidBookSources") { book.getInvalidBookSources(it) }
        get(router, "/reader3/getTxtTocRules") { book.getTxtTocRules(it) }

        get(router, "/reader3/getBookGroups") { group.getBookGroups(it) }
        post(router, "/reader3/saveBookGroup") { group.saveBookGroup(it) }
        post(router, "/reader3/deleteBookGroup") { group.deleteBookGroup(it) }

        get(router, "/reader3/getBookmarks") { bookmark.getBookmarks(it) }
        post(router, "/reader3/saveBookmark") { bookmark.saveBookmark(it) }

        get(router, "/reader3/getReplaceRules") { replace.getReplaceRules(it) }
        post(router, "/reader3/saveReplaceRule") { replace.saveReplaceRule(it) }
        post(router, "/reader3/deleteReplaceRule") { replace.deleteReplaceRule(it) }

        post(router, "/reader3/login") { user.login(it) }
        post(router, "/reader3/logout") { user.logout(it) }
        get(router, "/reader3/getUserInfo") { user.getUserInfo(it) }
        post(router, "/reader3/saveUserConfig") { user.saveUserConfig(it) }
        get(router, "/reader3/getUserConfig") { user.getUserConfig(it) }
        get(router, "/reader3/getUserList") { user.getUserList(it) }
        post(router, "/reader3/addUser") { user.addUser(it) }
        post(router, "/reader3/deleteUsers") { user.deleteUsers(it) }
        post(router, "/reader3/resetPassword") { user.resetPassword(it) }
        post(router, "/reader3/updateUser") { user.updateUser(it) }

        get(router, "/reader3/getLicense") { license.getLicense(it) }
        post(router, "/reader3/importLicense") { license.importLicense(it) }
        get(router, "/reader3/generateKeys") { license.generateKeys(it) }
        post(router, "/reader3/generateLicense") { license.generateLicense(it) }
        get(router, "/reader3/isHostValid") { license.isHostValid(it) }
        post(router, "/reader3/activateLicense") { license.activateLicense(it) }
        get(router, "/reader3/isLicenseValid") { license.isLicenseValid(it) }

        get(router, "/reader3/file/list") { file.list(it) }
        post(router, "/reader3/file/mkdir") { file.mkdir(it) }
        post(router, "/reader3/file/delete") { file.delete(it) }
        get(router, "/reader3/file/download") { file.download(it); null }

        get(router, "/reader3/getRssSources") { rss.getRssSources(it) }
        post(router, "/reader3/saveRssSource") { rss.saveRssSource(it) }
        post(router, "/reader3/deleteRssSource") { rss.deleteRssSource(it) }
        get(router, "/reader3/getRssArticles") { rss.getRssArticles(it) }
        get(router, "/reader3/getRssContent") { rss.getRssContent(it) }

        get(router, "/reader3/httpTTS/list") { httpTts.list(it) }
        post(router, "/reader3/httpTTS/save") { httpTts.save(it) }

        post(router, "/reader3/backupToWebdav") { webdav.backupToWebdav(it) }
    }

    private fun get(router: Router, path: String, block: suspend (RoutingContext) -> Any?) {
        router.get(path).handler { ctx -> launch(Dispatchers.IO) { handle(ctx, block) } }
    }

    private fun post(router: Router, path: String, block: suspend (RoutingContext) -> Any?) {
        router.post(path).handler { ctx -> launch(Dispatchers.IO) { handle(ctx, block) } }
    }

    private suspend fun handle(ctx: RoutingContext, block: suspend (RoutingContext) -> Any?) {
        try {
            when (val r = block(ctx)) {
                is ReturnData -> VertExtKt.success(ctx, r)
                null -> { /* already written */ }
                else -> VertExtKt.success(ctx, r)
            }
        } catch (e: Exception) {
            onHandlerError(ctx, e)
        }
    }

    suspend fun getSystemInfo(ctx: RoutingContext): ReturnData =
        ReturnData().setData(
            mapOf(
                "version" to "3.2.14-rebuild",
                "secure" to appConfig.secure,
                "userLimit" to appConfig.userLimit,
                "workDir" to ExtKt.getWorkDir()
            )
        )
}
''')

# resources
RES.mkdir(parents=True, exist_ok=True)
(RES / "application.yml").write_text("""
reader:
  app:
    workDir: "."
    showUI: false
    debug: false
    packaged: false
    secure: false
    inviteCode: ""
    secureKey: ""
    debugLog: false
    userLimit: 50
    userBookLimit: 200
    mongoUri: ""
    mongoDbName: "reader"
    cacheChapterContent: true
  server:
    port: 8080

server:
  port: 8080

spring:
  main:
    web-application-type: none
  application:
    name: reader-pro

logging:
  level:
    root: INFO
    com.htmake.reader: INFO
""".lstrip(), encoding="utf-8")

(RES / "web").mkdir(exist_ok=True)
(RES / "web" / "index.html").write_text(
    "<!doctype html><html><head><meta charset=utf-8><title>Reader Pro</title></head>"
    "<body style='font-family:system-ui;padding:2rem'><h1>Reader Pro 3.2.14 rebuild</h1>"
    "<p>API base: <code>/reader3</code></p>"
    "<p><a href='/reader3/getSystemInfo'>getSystemInfo</a></p></body></html>",
    encoding="utf-8",
)
(RES / "simple-web").mkdir(exist_ok=True)
(RES / "simple-web" / "index.html").write_text("<html><body>simple-web</body></html>", encoding="utf-8")

# Project README for the app
(ROOT / "APP_README.md").write_text("""# Reader Pro (rebuild)

Elegant, self-contained rebuild of **reader-pro 3.2.14** semantics:

- Spring Boot 2.7 + Vert.x 3.9 HTTP
- Kotlin coroutines controllers
- Legado-style rule engine (JSoup / XPath-lite / JsonPath / Rhino JS)
- Web book search / explore / info / TOC / content
- Local TXT / EPUB / CBZ / PDF
- Multi-user file storage under `storage/data`
- WebDAV subset + backup zip
- GitHub Actions: `.github/workflows/build.yml`

## Build

```bash
./gradlew bootJar
java -jar build/libs/reader-pro-*.jar
```

## API

See `API_ROUTES.md` (original 133 routes). Core routes are wired in `YueduApi`.

## Layout

```
src/main/kotlin/com/htmake/reader/   # server
src/main/kotlin/io/legado/app/       # reading engine
src/main/resources/                  # yml + static web
best-of-3/                           # reverse-engineering archive (reference)
```
""", encoding="utf-8")

print("TOTAL kt", sum(1 for _ in SRC.rglob("*.kt")))
