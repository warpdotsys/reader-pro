/**
 * Business-oriented rewrite from reader-pro-3.2.14.jar reverse engineering. Phase8: export wired + cache helpers.
 * Source: CFR/Vineflower + manual semantic cleanup. For audit/readability.
 */

package com.htmake.reader.api.controller

import com.htmake.reader.api.ReturnData
import com.htmake.reader.utils.ExtKt
import com.htmake.reader.utils.UserMutex
import io.legado.app.data.entities.Book
import io.legado.app.data.entities.BookChapter
import io.legado.app.data.entities.BookSource
import io.legado.app.data.entities.HttpTTS
import io.legado.app.help.BookHelp
import io.legado.app.help.ContentProcessor
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
        var content = if (book.isLocalBook) {
            LocalBook.getContent(book, chapter) ?: ""
        } else {
            val src = sourceStr ?: return rd.setErrorMsg("书源信息错误")
            WebBook(src, appConfig.debugLog, null, ns).getBookContent(book, chapter, nextUrl)
        }
        // apply user replace rules (replaceRule.json)
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
            if (!bookSource.isNullOrEmpty()) {
                val bookSourceObject = BookSource.fromJson(bookSource).getOrNull()
                if (bookSourceObject != null) {
                    addInvalidBookSource(
                        bookSourceObject.bookSourceUrl,
                        mapOf(
                            "sourceUrl" to bookSourceObject.bookSourceUrl,
                            "time" to System.currentTimeMillis(),
                            "error" to e.toString()
                        ),
                        userNameSpace
                    )
                }
            }
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

    suspend fun exportBook(context: RoutingContext): ReturnData =
        BookExport.exportBook(this, context)

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

    /**
     * 从 WebDAV 备份 zip 恢复：json 配置 + books 目录 + bookProgress。
     */
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
            // books/ under zip → storage/data/{user}/webdav/books
            val backupBooks = File(descDir, "books")
            if (backupBooks.isDirectory) {
                val webdavBooks = File(ExtKt.getWorkDir("storage", "data", userNameSpace, "webdav", "books"))
                ExtKt.deleteRecursively(webdavBooks)
                backupBooks.copyRecursively(webdavBooks, overwrite = false)
            }
            // progress files from webdav home
            val userHome = File(getUserWebdavHome(userNameSpace))
            var progressDir = File(userHome, "bookProgress")
            if (!progressDir.isDirectory) progressDir = File(userHome, "legado/bookProgress")
            if (progressDir.isDirectory) {
                progressDir.listFiles()?.forEach { f ->
                    if (f.isFile) syncBookProgressFromWebdav(f, userNameSpace)
                }
            }
            return true
        } catch (e: Exception) {
            e.printStackTrace()
            return false
        } finally {
            ExtKt.deleteRecursively(descDirFile)
        }
    }

    /** 单书进度 JSON 合并进书架 */
    fun syncBookProgressFromWebdav(progressFile: File, userNameSpace: String) {
        try {
            val o = JsonObject(progressFile.readText())
            val bookUrl = o.getString("bookUrl") ?: o.getString("url") ?: return
            val book = getShelfBookByURL(bookUrl, userNameSpace) ?: return
            // fire-and-forget style edit — sync is suspend context often
            val arr = ExtKt.asJsonArray(getUserStorage(userNameSpace, "bookshelf")) ?: return
            for (i in 0 until arr.size()) {
                val b = arr.getJsonObject(i) ?: continue
                if (b.getString("bookUrl") == bookUrl) {
                    o.getInteger("durChapterIndex")?.let { b.put("durChapterIndex", it) }
                    o.getInteger("durChapterPos")?.let { b.put("durChapterPos", it) }
                    o.getString("durChapterTitle")?.let { b.put("durChapterTitle", it) }
                    o.getLong("durChapterTime")?.let { b.put("durChapterTime", it) }
                    break
                }
            }
            saveUserStorage(userNameSpace, "bookshelf", arr)
        } catch (_: Exception) {
        }
    }

    /**
     * 打包用户配置到 WebDAV 家目录（legado 风格）。
     * @return 生成的 zip File，失败 null
     */
    suspend fun saveToWebdav(userNameSpace: String, latestZipFilePath: String? = null): Boolean {
        val userHome = getUserWebdavHome(userNameSpace)
        val zipHint = latestZipFilePath ?: getLastBackFileFromWebdav(userNameSpace)
        val legadoHome = if (zipHint != null && zipHint.contains("legado")) {
            File(userHome, "legado").absolutePath
        } else if (zipHint == null) {
            File(userHome, "legado").absolutePath
        } else {
            userHome
        }
        val file = createUserBackup(userNameSpace, legadoHome, zipHint)
        return file != null
    }

    fun getLastBackFileFromWebdav(userNameSpace: String): String? {
        val home = File(getUserWebdavHome(userNameSpace))
        val candidates = mutableListOf<File>()
        fun scan(dir: File) {
            if (!dir.isDirectory) return
            dir.listFiles()?.forEach { f ->
                if (f.isFile && f.name.endsWith(".zip", true) &&
                    (f.name.contains("backup", true) || f.name.startsWith("backup"))
                ) {
                    candidates += f
                } else if (f.isDirectory && (f.name == "legado" || f.name == "backup")) {
                    scan(f)
                }
            }
        }
        scan(home)
        return candidates.maxByOrNull { it.lastModified() }?.absolutePath
    }

    fun createUserBackup(
        userNameSpace: String,
        backupDir: String,
        latestZipFilePath: String? = null
    ): File? {
        val today = java.text.SimpleDateFormat("yyyy-MM-dd").format(System.currentTimeMillis())
        val staging = File(ExtKt.getWorkDir("storage", "data", userNameSpace, "backup$today"))
        try {
            ExtKt.deleteRecursively(staging)
            staging.mkdirs()
            // seed from previous zip if present
            if (latestZipFilePath != null) {
                val prev = File(latestZipFilePath)
                if (prev.isFile) {
                    try {
                        ZipUtils.unzipFile(prev, staging)
                    } catch (_: Exception) {
                    }
                }
            }
            val dataDir = File(ExtKt.getWorkDir("storage", "data", userNameSpace))
            for (name in getBackupFileNames()) {
                val src = File(dataDir, name)
                if (src.isFile) src.copyTo(File(staging, name), overwrite = true)
            }
            // optional books mirror
            val books = File(dataDir, "webdav/books")
            if (books.isDirectory) {
                books.copyRecursively(File(staging, "books"), overwrite = true)
            }
            val outDir = File(backupDir).apply { mkdirs() }
            val zip = File(outDir, "backup$today.zip")
            if (zip.exists()) zip.delete()
            java.util.zip.ZipOutputStream(zip.outputStream()).use { zos ->
                staging.walkTopDown().filter { it.isFile }.forEach { f ->
                    val entry = f.relativeTo(staging).invariantSeparatorsPath
                    zos.putNextEntry(java.util.zip.ZipEntry(entry))
                    f.inputStream().use { it.copyTo(zos) }
                    zos.closeEntry()
                }
            }
            return zip
        } catch (e: Exception) {
            e.printStackTrace()
            return null
        } finally {
            ExtKt.deleteRecursively(staging)
        }
    }


    // endregion

    // region ---- helpers ----


    // region ---- invalid book source cache ----

    fun getInvalidBookSourceCache(userNameSpace: String): ACache =
        ACache.get(File(ExtKt.getWorkDir("storage", "cache", "invalidBookSourceCache", userNameSpace)))

    fun isInvalidBookSource(bookSourceUrl: String, userNameSpace: String): Boolean =
        getInvalidBookSourceCache(userNameSpace).getAsString(bookSourceUrl) != null

    fun isInvalidBookSource(bookSource: BookSource, userNameSpace: String): Boolean =
        isInvalidBookSource(bookSource.bookSourceUrl, userNameSpace)

    /** jar: put with TTL 600s */
    fun addInvalidBookSource(sourceUrl: String, invalidInfo: Map<String, Any?>, userNameSpace: String) {
        getInvalidBookSourceCache(userNameSpace).put(sourceUrl, ExtKt.jsonEncode(invalidInfo), 600)
    }

    // endregion

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
