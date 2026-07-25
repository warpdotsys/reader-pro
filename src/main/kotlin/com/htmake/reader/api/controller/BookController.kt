package com.htmake.reader.api.controller

import com.htmake.reader.api.ReturnData
import com.htmake.reader.entity.User
import com.htmake.reader.utils.SpringContextUtils
import com.htmake.reader.utils.UserMutex
import com.htmake.reader.utils.asJsonArray
import com.htmake.reader.utils.getWorkDir
import com.htmake.reader.utils.gson
import io.legado.app.data.entities.Book
import io.legado.app.data.entities.BookChapter
import io.legado.app.data.entities.BookSource
import io.legado.app.data.entities.HttpTTS
import io.legado.app.data.entities.SearchBook
import io.legado.app.data.entities.SearchResult
import io.legado.app.data.entities.TxtTocRule
import io.legado.app.model.localBook.LocalBook
import io.legado.app.model.webBook.WebBook
import io.legado.app.utils.ACache
import io.legado.app.utils.MD5Utils
import io.vertx.core.http.HttpMethod
import io.vertx.core.http.HttpServerResponse
import io.vertx.core.json.JsonArray
import io.vertx.core.json.JsonObject
import io.vertx.ext.web.RoutingContext
import io.vertx.ext.web.client.WebClient
import java.io.BufferedInputStream
import java.io.File
import java.io.FileInputStream
import java.io.FileOutputStream
import java.io.InputStream
import java.net.URL
import java.nio.charset.Charset
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import java.util.zip.ZipEntry
import java.util.zip.ZipInputStream
import java.util.zip.ZipOutputStream
import javax.imageio.ImageIO
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import org.apache.pdfbox.pdmodel.PDDocument
import org.apache.pdfbox.rendering.PDFRenderer
import kotlin.coroutines.CoroutineContext
import mu.KotlinLogging

private val logger = KotlinLogging.logger {}

@Suppress("UNCHECKED_CAST", "UNUSED_PARAMETER")
class BookController(
    coroutineContext: CoroutineContext,
) : BaseController(coroutineContext) {
    private var bookInfoCache: ACache = ACache.get("bookInfoCache", 2000000L, 10000)
    private val concurrentLoopCount: Int = 8
    private val backupFileNames: Array<String> by lazy {
        arrayOf(
            "bookSource.json",
            "bookshelf.json",
            "bookGroup.json",
            "rssSources.json",
            "replaceRule.json",
            "bookmark.json",
            "userConfig.json",
            "httpTTS.json",
            "remoteBookSourceSub.json",
            "txtTocRule.json"
        )
    }
    private var webClient: WebClient = SpringContextUtils.getBean("webClient", WebClient::class.java)!!

    suspend fun getInvalidBookSources(context: RoutingContext): ReturnData {
        val result = authenticated(context) ?: return needLogin()
        return result.setData(getInvalidBookSourceCache(getUserNameSpace(context)).getAsString("sources") ?: "[]")
    }

    suspend fun getBookInfo(context: RoutingContext): ReturnData {
        val result = authenticated(context) ?: return needLogin()
        val userNameSpace = getUserNameSpace(context)
        val bookUrl = stringParam(context, "url", "bookUrl")
            ?: return result.setErrorMsg("book url is required")
        val source = getBookSourceString(context, bookUrl, true)
        if (source.isEmpty()) return result.setErrorMsg("book source is unavailable")
        val cacheKey = "$userNameSpace@$bookUrl"
        bookInfoCache.getAsString(cacheKey)?.let { cached ->
            Book.fromJson(cached).getOrNull()?.let { return result.setData(it) }
        }
        return runCatching {
            val book = WebBook(source, appConfig.debugLog, null, userNameSpace).getBookInfo(bookUrl)
            bookInfoCache.put(cacheKey, gson.toJson(book), 600)
            result.setData(book)
        }.getOrElse { result.setErrorMsg(it.message ?: "book information request failed") }
    }

    suspend fun getBookCover(context: RoutingContext) {
        val bookUrl = stringParam(context, "url", "bookUrl").orEmpty()
        val userNameSpace = getUserNameSpace(context)
        val book = getShelfBookByURL(bookUrl, userNameSpace)
        val cover = book.customCoverUrl ?: book.coverUrl
        if (cover.isNullOrEmpty()) {
            context.response().setStatusCode(404).end()
            return
        }
        val local = File(cover.removePrefix("file://"))
        if (local.isFile) {
            context.response().sendFile(local.absolutePath)
        } else {
            context.response().setStatusCode(302).putHeader("Location", cover).end()
        }
    }

    suspend fun importBookPreview(context: RoutingContext): ReturnData {
        val result = authenticated(context) ?: return needLogin()
        val uploaded = context.fileUploads().firstOrNull()
            ?: return result.setErrorMsg("a book file is required")
        val file = File(uploaded.uploadedFileName())
        if (!file.isFile) return result.setErrorMsg("uploaded book is unavailable")
        val book = Book.initLocalBook(file.absolutePath, file.absolutePath, getWorkDir())
        return result.setData(book)
    }

    suspend fun getTxtTocRules(context: RoutingContext): ReturnData {
        val result = authenticated(context) ?: return needLogin()
        val rules = getUserStorage(getUserNameSpace(context), "txtTocRule").asJsonArray()
            ?: JsonArray()
        return result.setData(rules.list)
    }

    suspend fun getChapterListByRule(context: RoutingContext): ReturnData {
        val result = authenticated(context) ?: return needLogin()
        val book = bodyBook(context, getUserNameSpace(context)) ?: return result.setErrorMsg("book is required")
        stringParam(context, "rule")?.takeIf { it.isNotBlank() }?.let { book.tocUrl = it }
        return runCatching {
            prepareLocalBook(book, getUserNameSpace(context))
            result.setData(LocalBook.getChapterList(book))
        }.getOrElse { result.setErrorMsg(it.message ?: "chapter extraction failed") }
    }

    suspend fun refreshLocalBook(context: RoutingContext): ReturnData {
        val result = authenticated(context) ?: return needLogin()
        val userNameSpace = getUserNameSpace(context)
        val book = resolveBook(context, userNameSpace) ?: return result.setErrorMsg("book is required")
        if (!book.isLocalBook()) return result.setErrorMsg("book is not local")
        return runCatching {
            prepareLocalBook(book, userNameSpace)
            book.updateFromLocal()
            saveBookToShelf(book, userNameSpace, context)
            result.setData(book)
        }.getOrElse { result.setErrorMsg(it.message ?: "local refresh failed") }
    }

    suspend fun getChapterList(context: RoutingContext): ReturnData {
        val result = authenticated(context) ?: return needLogin()
        val userNameSpace = getUserNameSpace(context)
        val book = resolveBook(context, userNameSpace) ?: return result.setErrorMsg("book is required")
        val source = getBookSourceString(context, book.bookUrl, true)
        return runCatching {
            val chapters = getLocalChapterList(
                book,
                source,
                booleanParam(context, "refresh"),
                userNameSpace,
            )
            saveShelfBookLatestChapter(book, chapters, userNameSpace)
            result.setData(chapters)
        }.getOrElse { result.setErrorMsg(it.message ?: "chapter request failed") }
    }

    suspend fun saveBookProgress(context: RoutingContext): ReturnData {
        val result = authenticated(context) ?: return needLogin()
        val userNameSpace = getUserNameSpace(context)
        val body = context.bodyAsJson ?: JsonObject()
        val book = getShelfBookByURL(body.getString("bookUrl") ?: stringParam(context, "url", "bookUrl").orEmpty(), userNameSpace)
        if (book.bookUrl.isEmpty()) return result.setErrorMsg("book is unavailable")
        val chapter = BookChapter(
            index = body.getInteger("durChapterIndex", book.durChapterIndex),
            title = body.getString("durChapterTitle", book.durChapterTitle.orEmpty()),
        )
        book.durChapterPos = body.getInteger("durChapterPos", body.getInteger("position", book.durChapterPos))
        saveShelfBookProgress(book, chapter, userNameSpace)
        return result.setData("")
    }

    suspend fun getBookContent(context: RoutingContext): ReturnData {
        val result = authenticated(context) ?: return needLogin()
        val userNameSpace = getUserNameSpace(context)
        val book = resolveBook(context, userNameSpace) ?: return result.setErrorMsg("book is required")
        val index = intParam(context, "index") ?: return result.setErrorMsg("chapter index is required")
        val source = getBookSourceString(context, book.bookUrl, true)
        return runCatching {
            val chapters = getLocalChapterList(book, source, false, userNameSpace)
            val chapter = chapters.getOrNull(index) ?: error("chapter is unavailable")
            val cacheFile = File(getChapterCacheDir(book, userNameSpace), "$index.txt")
            val refresh = booleanParam(context, "refresh") || booleanParam(context, "cache")
            val content = if (!refresh && cacheFile.isFile) {
                cacheFile.readText(book.fileCharset())
            } else if (book.isLocalBook()) {
                LocalBook.getContent(book, chapter).orEmpty()
            } else {
                WebBook(source, appConfig.debugLog, null, userNameSpace).getBookContent(
                    book,
                    chapter,
                    chapters.getOrNull(index + 1)?.url,
                )
            }
            cacheFile.parentFile.mkdirs()
            cacheFile.writeText(content, book.fileCharset())
            result.setData(content)
        }.getOrElse { result.setErrorMsg(it.message ?: "content request failed") }
    }

    suspend fun saveBookContent(context: RoutingContext): ReturnData {
        val result = authenticated(context) ?: return needLogin()
        val userNameSpace = getUserNameSpace(context)
        val body = context.bodyAsJson ?: return result.setErrorMsg("content is required")
        val book = getShelfBookByURL(body.getString("bookUrl").orEmpty(), userNameSpace)
        if (book.bookUrl.isEmpty()) return result.setErrorMsg("book is unavailable")
        val index = body.getInteger("index", -1)
        if (index < 0) return result.setErrorMsg("chapter index is required")
        File(getChapterCacheDir(book, userNameSpace), "$index.txt").apply {
            parentFile.mkdirs()
            writeText(body.getString("content").orEmpty(), book.fileCharset())
        }
        return result.setData("")
    }

    suspend fun exploreBook(context: RoutingContext): ReturnData {
        val result = authenticated(context) ?: return needLogin()
        val sourceUrl = stringParam(context, "bookSourceUrl", "sourceUrl")
            ?: return result.setErrorMsg("book source is required")
        val source = getBookSourceStringBySourceURLOpt(sourceUrl, getUserNameSpace(context))
        if (source.isEmpty()) return result.setErrorMsg("book source is unavailable")
        val url = stringParam(context, "url", "exploreUrl") ?: return result.setErrorMsg("url is required")
        return runCatching {
            result.setData(WebBook(source, appConfig.debugLog, null, getUserNameSpace(context)).exploreBook(url, intParam(context, "page") ?: 1))
        }.getOrElse { result.setErrorMsg(it.message ?: "explore request failed") }
    }

    suspend fun searchBook(context: RoutingContext): ReturnData {
        val result = authenticated(context) ?: return needLogin()
        val key = stringParam(context, "key") ?: return result.setErrorMsg("search key is required")
        val userNameSpace = getUserNameSpace(context)
        val sourceUrl = stringParam(context, "bookSourceUrl", "sourceUrl")
            ?: return result.setErrorMsg("book source is required")
        val source = getBookSourceStringBySourceURLOpt(sourceUrl, userNameSpace)
        if (source.isEmpty()) return result.setErrorMsg("book source is unavailable")
        return runCatching {
            result.setData(WebBook(source, appConfig.debugLog, null, userNameSpace).searchBook(key, intParam(context, "page") ?: 1))
        }.getOrElse { result.setErrorMsg(it.message ?: "search request failed") }
    }

    suspend fun searchBookMulti(context: RoutingContext): ReturnData {
        val result = authenticated(context) ?: return needLogin()
        val key = stringParam(context, "key") ?: return result.setErrorMsg("search key is required")
        val userNameSpace = getUserNameSpace(context)
        val group = stringParam(context, "bookSourceGroup").orEmpty()
        val sources = userBookSources(userNameSpace).filter { source ->
            group.isEmpty() || source.bookSourceGroup?.split(',')?.any { it.trim() == group } == true
        }.take(concurrentLoopCount * 5)
        val books = arrayListOf<SearchBook>()
        sources.forEach { source ->
            if (!isInvalidBookSource(source, userNameSpace)) {
                runCatching {
                    WebBook(gson.toJson(source), appConfig.debugLog, null, userNameSpace)
                        .searchBook(key, intParam(context, "page") ?: 1)
                }.onSuccess { books.addAll(it) }.onFailure {
                    addInvalidBookSource(source.bookSourceUrl, mapOf("error" to (it.message ?: "search failed")), userNameSpace)
                }
            }
        }
        return result.setData(mapOf("list" to books, "lastIndex" to sources.size))
    }

    suspend fun searchBookMultiSSE(context: RoutingContext) {
        val result = searchBookMulti(context)
        sse(context, result.data)
    }

    suspend fun searchBookSource(context: RoutingContext): ReturnData = searchBook(context)

    suspend fun searchBookSourceSSE(context: RoutingContext) {
        sse(context, searchBookSource(context).data)
    }

    suspend fun searchBookWithSource(
        key: String,
        book: Book,
        precise: Boolean = false,
        userNameSpace: String = "",
    ): ArrayList<SearchBook> {
        val namespace = userNameSpace.ifEmpty { book.getUserNameSpace() }
        val source = getBookSourceStringBySourceURLOpt(book.origin, namespace)
        if (source.isEmpty()) return arrayListOf()
        val found = WebBook(source, appConfig.debugLog, null, namespace).searchBook(key)
        return ArrayList(if (precise) found.filter { it.name == book.name && it.author == book.author } else found)
    }

    suspend fun getAvailableBookSource(context: RoutingContext): ReturnData {
        val result = authenticated(context) ?: return needLogin()
        val userNameSpace = getUserNameSpace(context)
        val book = resolveBook(context, userNameSpace) ?: return result.setErrorMsg("book is required")
        val matches = arrayListOf<SearchBook>()
        userBookSources(userNameSpace).take(concurrentLoopCount * 5).forEach { source ->
            if (source.bookSourceUrl != book.origin && !isInvalidBookSource(source, userNameSpace)) {
                matches.addAll(runCatching {
                    searchBookWithSource(book.name, book.copy(origin = source.bookSourceUrl), true, userNameSpace)
                }.getOrElse { emptyList() })
            }
        }
        return result.setData(matches)
    }

    suspend fun getBookshelf(context: RoutingContext): ReturnData {
        val result = authenticated(context) ?: return needLogin()
        return result.setData(getBookShelfBooks(false, getUserNameSpace(context)))
    }

    suspend fun getShelfBook(context: RoutingContext): ReturnData {
        val result = authenticated(context) ?: return needLogin()
        val book = getShelfBookByURL(stringParam(context, "url", "bookUrl").orEmpty(), getUserNameSpace(context))
        return if (book.bookUrl.isEmpty()) result.setErrorMsg("book is unavailable") else result.setData(book)
    }

    suspend fun saveBook(context: RoutingContext): ReturnData {
        val result = authenticated(context) ?: return needLogin()
        val userNameSpace = getUserNameSpace(context)
        val book = bodyBook(context, userNameSpace) ?: return result.setErrorMsg("book is required")
        if (book.bookUrl.isEmpty()) return result.setErrorMsg("book url is required")
        return result.setData(saveBookToShelf(book, userNameSpace, context).first)
    }

    fun saveBookToShelf(book: Book, userNameSpace: String, context: RoutingContext): Pair<Book, String> {
        return persistBook(book, userNameSpace)
    }

    private fun persistBook(book: Book, userNameSpace: String): Pair<Book, String> {
        val existing = getShelfBookByURL(book.bookUrl, userNameSpace)
        val saved = book.copy(isInShelf = true).also {
            it.setUserNameSpace(userNameSpace)
            if (existing.bookUrl.isNotEmpty()) {
                it.durChapterIndex = existing.durChapterIndex
                it.durChapterPos = existing.durChapterPos
                it.durChapterTitle = existing.durChapterTitle
                it.durChapterTime = existing.durChapterTime
            }
        }
        val bookshelf = shelfJson(userNameSpace)
        val index = shelfIndex(bookshelf, saved.bookUrl)
        if (index >= 0) bookshelf.list[index] = JsonObject.mapFrom(saved) else bookshelf.add(JsonObject.mapFrom(saved))
        saveUserStorage(userNameSpace, "bookshelf", bookshelf)
        return Pair(saved, if (index >= 0) "updated" else "saved")
    }

    suspend fun saveBookCover(book: Book, coverUrl: String = "", userNameSpace: String = "") {
        val cover = coverUrl.ifEmpty { book.customCoverUrl ?: book.coverUrl.orEmpty() }
        val file = File(cover.removePrefix("file://"))
        if (file.isFile) {
            val target = File(getChapterCacheDir(book, userNameSpace), "cover${file.extension.let { if (it.isEmpty()) "" else ".$it" }}")
            target.parentFile.mkdirs()
            file.copyTo(target, overwrite = true)
            book.customCoverUrl = target.absolutePath
        }
    }

    suspend fun setBookSource(context: RoutingContext): ReturnData {
        val result = authenticated(context) ?: return needLogin()
        val userNameSpace = getUserNameSpace(context)
        val body = context.bodyAsJson ?: return result.setErrorMsg("book source is required")
        val book = getShelfBookByURL(body.getString("bookUrl").orEmpty(), userNameSpace)
        if (book.bookUrl.isEmpty()) return result.setErrorMsg("book is unavailable")
        book.origin = body.getString("bookSourceUrl", body.getString("origin", book.origin))
        book.originName = body.getString("bookSourceName", book.originName)
        saveBookToShelf(book, userNameSpace, context)
        return result.setData("")
    }

    suspend fun saveBookConfig(context: RoutingContext): ReturnData {
        val result = authenticated(context) ?: return needLogin()
        val userNameSpace = getUserNameSpace(context)
        val body = context.bodyAsJson ?: return result.setErrorMsg("book config is required")
        val book = getShelfBookByURL(body.getString("bookUrl").orEmpty(), userNameSpace)
        if (book.bookUrl.isEmpty()) return result.setErrorMsg("book is unavailable")
        body.getJsonObject("readConfig")?.let { book.readConfig = it.mapTo(Book.ReadConfig::class.java) }
        body.getString("customCoverUrl")?.let { book.customCoverUrl = it }
        body.getString("customIntro")?.let { book.customIntro = it }
        saveBookToShelf(book, userNameSpace, context)
        return result.setData("")
    }

    suspend fun saveBookGroupId(context: RoutingContext): ReturnData = updateBookGroups(context, add = null)

    suspend fun addBookGroupMulti(context: RoutingContext): ReturnData = updateBookGroups(context, add = true)

    suspend fun removeBookGroupMulti(context: RoutingContext): ReturnData = updateBookGroups(context, add = false)

    suspend fun deleteBook(context: RoutingContext): ReturnData {
        val result = authenticated(context) ?: return needLogin()
        val userNameSpace = getUserNameSpace(context)
        val url = stringParam(context, "url", "bookUrl") ?: return result.setErrorMsg("book url is required")
        deleteShelfBooks(userNameSpace, setOf(url))
        return result.setData("")
    }

    suspend fun deleteBooks(context: RoutingContext): ReturnData {
        val result = authenticated(context) ?: return needLogin()
        val urls = linkedSetOf<String>()
        context.bodyAsJsonArray?.forEach { item ->
            when (item) {
                is String -> urls += item
                is JsonObject -> item.getString("bookUrl")?.let(urls::add)
            }
        }
        context.bodyAsJson?.getJsonArray("bookUrls")?.forEach { it.toString().let(urls::add) }
        if (urls.isEmpty()) return result.setErrorMsg("book urls are required")
        deleteShelfBooks(getUserNameSpace(context), urls)
        return result.setData("")
    }

    suspend fun saveBookInfoCache(books: List<Book>): List<Book> {
        books.forEach { bookInfoCache.put(it.bookUrl, gson.toJson(it), 600) }
        return books
    }

    suspend fun mergeBookCacheInfo(book: Book): Book = bookInfoCache.getAsString(book.bookUrl)
        ?.let { Book.fromJson(it).getOrNull() }
        ?: book

    suspend fun getBookShelfBooks(
        refresh: Boolean = false,
        userNameSpace: String = "",
    ): List<Book> {
        val books = shelfJson(userNameSpace).map { item ->
            item as JsonObject
            item.mapTo(Book::class.java).apply {
                isInShelf = true
                setUserNameSpace(userNameSpace)
            }
        }
        return if (refresh) saveBookInfoCache(books) else books
    }

    suspend fun getLocalChapterList(
        book: Book,
        bookSource: String = "",
        refresh: Boolean = false,
        userNameSpace: String = "",
        saveCache: Boolean = true,
        mutex: Mutex = Mutex(),
    ): List<BookChapter> = mutex.withLock {
        prepareLocalBook(book, userNameSpace)
        val cacheFile = File(getChapterCacheDir(book, userNameSpace), "chapters.json")
        if (!refresh && cacheFile.isFile) {
            runCatching {
                gson.fromJson(cacheFile.readText(), Array<BookChapter>::class.java).toList()
            }.getOrNull()?.let { return@withLock it }
        }
        val chapters = if (book.isLocalBook()) {
            LocalBook.getChapterList(book)
        } else {
            if (bookSource.isEmpty()) error("book source is unavailable")
            val webBook = WebBook(bookSource, appConfig.debugLog, null, userNameSpace)
            if (book.tocUrl.isEmpty()) webBook.getBookInfo(book)
            webBook.getChapterList(book)
        }.onEachIndexed { index, chapter ->
            chapter.index = index
            chapter.bookUrl = book.bookUrl
            chapter.setUserNameSpace(userNameSpace)
        }
        if (saveCache) {
            cacheFile.parentFile.mkdirs()
            cacheFile.writeText(gson.toJson(chapters))
            getBookChaptersCache(userNameSpace).put(book.bookUrl, gson.toJson(chapters), 86_400)
        }
        chapters
    }

    suspend fun getBookSourceString(
        context: RoutingContext,
        bookUrl: String,
        withSourceOpt: Boolean = false,
    ): String {
        val userNameSpace = getUserNameSpace(context)
        val requested = stringParam(context, "bookSourceUrl", "sourceUrl")
        if (!requested.isNullOrEmpty()) return getBookSourceStringBySourceURLOpt(requested, userNameSpace)
        val book = getShelfBookByURL(bookUrl, userNameSpace)
        return getBookSourceStringBySourceURLOpt(book.origin, userNameSpace)
    }

    fun getBookSourceStringBySourceURLOpt(sourceUrl: String, userNameSpace: String): String {
        if (sourceUrl.isEmpty()) return ""
        return userBookSources(userNameSpace).firstOrNull { it.bookSourceUrl == sourceUrl }
            ?.let(gson::toJson)
            .orEmpty()
    }

    fun getShelfBookByURL(bookUrl: String, userNameSpace: String): Book {
        if (bookUrl.isEmpty()) return Book()
        val value = shelfJson(userNameSpace).firstOrNull { item ->
            (item as? JsonObject)?.getString("bookUrl") == bookUrl
        } as? JsonObject ?: return Book()
        return value.mapTo(Book::class.java).apply {
            isInShelf = true
            setUserNameSpace(userNameSpace)
        }
    }

    suspend fun saveShelfBookProgress(book: Book, chapter: BookChapter, userNameSpace: String) {
        editShelfBook(book, userNameSpace) { saved ->
            saved.durChapterIndex = chapter.index
            saved.durChapterTitle = chapter.title
            saved.durChapterPos = book.durChapterPos
            saved.durChapterTime = System.currentTimeMillis()
            saved
        }
        saveBookProgressToWebdav(book, chapter, userNameSpace)
    }

    suspend fun saveShelfBookLatestChapter(
        book: Book,
        chapters: List<BookChapter>,
        userNameSpace: String,
        mutex: Mutex = Mutex(),
    ) {
        mutex.withLock {
            editShelfBook(book, userNameSpace) { saved ->
                saved.totalChapterNum = chapters.size
                saved.latestChapterTitle = chapters.lastOrNull()?.title
                saved.latestChapterTime = System.currentTimeMillis()
                saved
            }
        }
        Unit
    }

    suspend fun editShelfBook(
        book: Book,
        userNameSpace: String,
        handler: (Book) -> Book,
    ): Book {
        val mutex = UserMutex.getLocker("$userNameSpace@bookshelf")
        return mutex.withLock {
            val shelf = shelfJson(userNameSpace)
            val index = shelfIndex(shelf, book.bookUrl)
            if (index < 0) return@withLock Book()
            val updated = handler(shelf.getJsonObject(index).mapTo(Book::class.java)).apply {
                isInShelf = true
                setUserNameSpace(userNameSpace)
            }
            shelf.list[index] = JsonObject.mapFrom(updated)
            saveUserStorage(userNameSpace, "bookshelf", shelf)
            updated
        }
    }

    fun saveBookSources(
        book: Book,
        searchBooks: List<SearchBook>,
        userNameSpace: String,
        append: Boolean = false,
    ) {
        val selected = searchBooks.firstOrNull { it.bookUrl == book.bookUrl } ?: return
        if (append) selected.addOrigin(book.origin)
        book.origin = selected.origin
        book.originName = selected.originName
        persistBook(book, userNameSpace)
    }

    fun extractEpub(book: Book, force: Boolean = false): Boolean = extractArchive(book, force)

    fun extractCbz(book: Book, force: Boolean = false): Boolean = extractArchive(book, force)

    fun convertPdfToImage(book: Book, force: Boolean = false): Boolean = runCatching {
        val source = book.getLocalFile()
        if (!source.isFile) return false
        PDDocument.load(source).use { document ->
            val renderer = PDFRenderer(document)
            for (page in 0 until document.numberOfPages) convertPdfPageToImage(book, page, force, document, renderer)
        }
        true
    }.getOrDefault(false)

    fun convertPdfPageToImage(book: Book, pageIndex: Int, force: Boolean = false) {
        PDDocument.load(book.getLocalFile()).use { document ->
            convertPdfPageToImage(book, pageIndex, force, document, PDFRenderer(document))
        }
    }

    fun savePdfPageToImage(
        document: PDDocument,
        renderer: PDFRenderer,
        pageIndex: Int,
        width: Float,
        imagePath: String,
        imageFile: File,
    ) {
        if (pageIndex !in 0 until document.numberOfPages) return
        imageFile.parentFile.mkdirs()
        val dpi = if (width > 0) width.coerceIn(72F, 600F) else 144F
        ImageIO.write(renderer.renderImageWithDPI(pageIndex, dpi), "png", imageFile)
    }

    suspend fun syncBookProgressFromWebdav(progress: Any, userNameSpace: String) {
        val json = when (progress) {
            is JsonObject -> progress
            is String -> runCatching { JsonObject(progress) }.getOrDefault(JsonObject())
            is Map<*, *> -> JsonObject(progress as Map<String, Any>)
            else -> JsonObject()
        }
        val book = getShelfBookByURL(json.getString("bookUrl").orEmpty(), userNameSpace)
        if (book.bookUrl.isNotEmpty()) {
            book.durChapterIndex = json.getInteger("durChapterIndex", book.durChapterIndex)
            book.durChapterPos = json.getInteger("durChapterPos", book.durChapterPos)
            book.durChapterTitle = json.getString("durChapterTitle", book.durChapterTitle)
            book.durChapterTime = json.getLong("durChapterTime", book.durChapterTime)
            persistBook(book, userNameSpace)
        }
    }

    suspend fun saveBookProgressToWebdav(book: Book, chapter: BookChapter, userNameSpace: String) {
        val progressDir = File(getUserWebdavHome(userNameSpace), "progress")
        progressDir.mkdirs()
        File(progressDir, "${MD5Utils.md5Encode16(book.bookUrl)}.json").writeText(
            JsonObject()
                .put("bookUrl", book.bookUrl)
                .put("durChapterIndex", chapter.index)
                .put("durChapterPos", book.durChapterPos)
                .put("durChapterTitle", chapter.title)
                .put("durChapterTime", System.currentTimeMillis())
                .encode(),
        )
    }

    suspend fun syncFromWebdav(sourcePath: String, userNameSpace: String): Boolean = runCatching {
        val source = File(sourcePath)
        if (!source.isFile) return false
        syncBookProgressFromWebdav(source.readText(), userNameSpace)
        true
    }.getOrDefault(false)

    suspend fun saveToWebdav(userNameSpace: String, previousBackup: String = ""): Boolean = runCatching {
        createUserBackup(userNameSpace, getUserWebdavHome(userNameSpace), previousBackup)
        true
    }.getOrDefault(false)

    suspend fun createUserBackup(
        userNameSpace: String,
        destination: String = "",
        previousBackup: String = "",
    ): File {
        val fileName = "backup${SimpleDateFormat("yyyy-MM-dd", Locale.ROOT).format(Date())}.zip"
        val output = File(destination.ifEmpty { getUserWebdavHome(userNameSpace) }, fileName)
        output.parentFile.mkdirs()
        val root = File(getWorkDir("storage", "data", userNameSpace))
        ZipOutputStream(FileOutputStream(output)).use { zip ->
            if (root.isDirectory) addFilesToZip(root, root, zip)
        }
        return output
    }

    suspend fun getLastBackFileFromWebdav(userNameSpace: String): String {
        val home = File(getUserWebdavHome(userNameSpace))
        val backupHome = File(home, "legado").takeIf(File::isDirectory) ?: home
        return backupHome
        .listFiles { file -> file.isFile && Regex("^backup[0-9-]+\\.zip$", RegexOption.IGNORE_CASE).matches(file.name) }
        ?.maxByOrNull(File::lastModified)
        ?.absolutePath
        .orEmpty()
    }

    suspend fun bookSourceDebugSSE(context: RoutingContext) {
        sse(context, mapOf("status" to "unsupported"))
    }

    suspend fun cacheBookSSE(context: RoutingContext) {
        sse(context, cacheBookOnServer(context).data)
    }

    suspend fun cacheBookOnServer(context: RoutingContext): ReturnData {
        val result = authenticated(context) ?: return needLogin()
        val userNameSpace = getUserNameSpace(context)
        val books = context.bodyAsJsonArray ?: JsonArray(getBookShelfBooks(false, userNameSpace))
        cacheBookOnServer(books, userNameSpace)
        return result.setData("")
    }

    suspend fun cacheBookOnServer(books: JsonArray, userNameSpace: String) {
        books.forEach { value ->
            val book = when (value) {
                is JsonObject -> value.mapTo(Book::class.java)
                is String -> getShelfBookByURL(value, userNameSpace)
                else -> Book()
            }
            if (book.bookUrl.isNotEmpty()) runCatching {
                val source = getBookSourceStringBySourceURLOpt(book.origin, userNameSpace)
                getLocalChapterList(book, source, false, userNameSpace).forEach { chapter ->
                    if (!File(getChapterCacheDir(book, userNameSpace), "${chapter.index}.txt").isFile) {
                        val content = if (book.isLocalBook()) LocalBook.getContent(book, chapter).orEmpty()
                        else WebBook(source, appConfig.debugLog, null, userNameSpace).getBookContent(book, chapter)
                        File(getChapterCacheDir(book, userNameSpace), "${chapter.index}.txt").apply {
                            parentFile.mkdirs()
                            writeText(content)
                        }
                    }
                }
            }
        }
    }

    suspend fun deleteBookCache(context: RoutingContext): ReturnData {
        val result = authenticated(context) ?: return needLogin()
        val book = resolveBook(context, getUserNameSpace(context)) ?: return result.setErrorMsg("book is required")
        getChapterCacheDir(book, getUserNameSpace(context)).deleteRecursively()
        return result.setData("")
    }

    suspend fun textToSpeech(context: RoutingContext) {
        context.response().setStatusCode(501).end()
    }

    suspend fun ttsByEdge(response: HttpServerResponse, text: String, headers: Map<String, String> = emptyMap()) {
        response.setStatusCode(501).end()
    }

    fun getHttpTTSByName(name: String, userNameSpace: String): HttpTTS = getUserStorage(userNameSpace, "httpTTS")
        .asJsonArray()
        ?.mapNotNull { value -> runCatching { (value as JsonObject).mapTo(HttpTTS::class.java) }.getOrNull() }
        ?.firstOrNull { it.name == name }
        ?: HttpTTS()

    suspend fun ttsByApi(
        response: HttpServerResponse,
        ttsName: String,
        text: String,
        headers: Map<String, String> = emptyMap(),
    ) {
        val tts = getHttpTTSByName(ttsName, "default")
        val stream = getSpeakStream(tts, text, 0)
        if (stream == null) {
            response.setStatusCode(501).end()
            return
        }
        stream.use { response.end(io.vertx.core.buffer.Buffer.buffer(it.readBytes())) }
    }

    suspend fun getSpeakStream(tts: HttpTTS, text: String, index: Int): InputStream? = runCatching {
        if (tts.url.isBlank()) return null
        URL(tts.url.replace("{text}", java.net.URLEncoder.encode(text, "UTF-8"))).openStream()
    }.getOrNull()

    suspend fun ttsByTextToSpeechCn(
        response: HttpServerResponse,
        text: String,
        headers: Map<String, String> = emptyMap(),
    ) {
        ttsByEdge(response, text, headers)
    }

    fun getChapterCacheDir(book: Book, userNameSpace: String): File = File(
        getWorkDir("storage", "data", userNameSpace, "cache", MD5Utils.md5Encode16(book.bookUrl)),
    )

    fun getCachedChapterContentSet(book: Book, userNameSpace: String): Set<Int> = getChapterCacheDir(book, userNameSpace)
        .listFiles { file -> file.isFile && file.name.endsWith(".txt") }
        ?.mapNotNull { it.nameWithoutExtension.toIntOrNull() }
        ?.toSet()
        ?: emptySet()

    suspend fun getShelfBookWithCacheInfo(context: RoutingContext): ReturnData {
        val result = authenticated(context) ?: return needLogin()
        val userNameSpace = getUserNameSpace(context)
        return result.setData(getBookShelfBooks(false, userNameSpace).map { book ->
            JsonObject.mapFrom(book).put("cachedChapterIndexes", getCachedChapterContentSet(book, userNameSpace).sorted())
        })
    }

    suspend fun exportBook(context: RoutingContext) {
        if (!checkAuth(context)) {
            context.response().setStatusCode(401).end()
            return
        }
        val userNameSpace = getUserNameSpace(context)
        val book = resolveBook(context, userNameSpace)
        if (book == null) {
            context.response().setStatusCode(404).end()
            return
        }
        val output = File(getWorkDir("storage", "data", userNameSpace, "export", "${safeFileName(book.name)}.txt"))
        exportToTxt(output, book, getBookSourceStringBySourceURLOpt(book.origin, userNameSpace), userNameSpace)
        context.response()
            .putHeader("Content-Type", "text/plain; charset=utf-8")
            .putHeader("Content-Disposition", "attachment; filename=${safeFileName(book.name)}.txt")
            .sendFile(output.absolutePath)
    }

    suspend fun exportToTxt(file: File, book: Book, bookSource: String, userNameSpace: String): File {
        val chapters = getLocalChapterList(book, bookSource, false, userNameSpace)
        file.parentFile.mkdirs()
        file.outputStream().bufferedWriter(Charset.forName(appConfig.exportCharset)).use { writer ->
            chapters.forEach { chapter ->
                if (!appConfig.exportNoChapterName) writer.appendLine(chapter.title)
                val cache = File(getChapterCacheDir(book, userNameSpace), "${chapter.index}.txt")
                val content = when {
                    cache.isFile -> cache.readText(book.fileCharset())
                    book.isLocalBook() -> LocalBook.getContent(book, chapter).orEmpty()
                    else -> WebBook(bookSource, appConfig.debugLog, null, userNameSpace).getBookContent(book, chapter)
                }
                writer.appendLine(content).appendLine()
            }
        }
        return file
    }

    suspend fun searchBookContent(context: RoutingContext): ReturnData {
        val result = authenticated(context) ?: return needLogin()
        val userNameSpace = getUserNameSpace(context)
        val book = resolveBook(context, userNameSpace) ?: return result.setErrorMsg("book is required")
        val key = stringParam(context, "key", "query") ?: return result.setErrorMsg("search key is required")
        val chapters = getLocalChapterList(book, getBookSourceStringBySourceURLOpt(book.origin, userNameSpace), false, userNameSpace)
        val matches = chapters.flatMap { searchChapter(book, it, key) }
        return result.setData(matches)
    }

    suspend fun searchChapter(book: Book, chapter: BookChapter, key: String): List<SearchResult> {
        if (key.isEmpty()) return emptyList()
        val content = File(getChapterCacheDir(book, book.getUserNameSpace()), "${chapter.index}.txt")
            .takeIf(File::isFile)
            ?.readText(book.fileCharset())
            ?: if (book.isLocalBook()) LocalBook.getContent(book, chapter).orEmpty() else ""
        val positions = searchPosition(content, key)
        return positions.mapIndexed { count, position ->
            val pair = getResultAndQueryIndex(content, position, key)
            SearchResult(
                resultCount = positions.size,
                resultCountWithinChapter = count + 1,
                resultText = pair.second,
                chapterTitle = chapter.title,
                query = key,
                chapterIndex = chapter.index,
                queryIndexInResult = pair.first,
                queryIndexInChapter = position,
            )
        }
    }

    suspend fun backupToMongodb(context: RoutingContext): ReturnData {
        val result = authenticated(context) ?: return needLogin()
        return result.setErrorMsg("MongoDB backup is not configured")
    }

    suspend fun restoreFromMongodb(context: RoutingContext): ReturnData {
        val result = authenticated(context) ?: return needLogin()
        return result.setErrorMsg("MongoDB restore is not configured")
    }

    private fun getInvalidBookSourceCache(userNameSpace: String): ACache =
        ACache.get(File(getWorkDir("storage", "cache", "invalidBookSource", userNameSpace)))

    private fun isInvalidBookSource(bookSource: BookSource, userNameSpace: String): Boolean =
        getInvalidBookSourceCache(userNameSpace).getAsString(bookSource.bookSourceUrl) != null

    private fun addInvalidBookSource(sourceUrl: String, detail: Map<String, Any>, userNameSpace: String) {
        getInvalidBookSourceCache(userNameSpace).put(sourceUrl, gson.toJson(detail), 600)
        getInvalidBookSourceCache(userNameSpace).put("sources", gson.toJson(listOf(detail)), 600)
    }

    private fun getBookChaptersCache(userNameSpace: String): ACache =
        ACache.get(File(getWorkDir("storage", "cache", "bookChapters", userNameSpace)))

    private fun updateImageLinkInContent(book: Book, chapter: BookChapter, content: String): String = content

    private fun prepareLocalBook(book: Book, userNameSpace: String) {
        book.setRootDir(getWorkDir())
        book.setUserNameSpace(userNameSpace)
    }

    private suspend fun authenticated(context: RoutingContext): ReturnData? =
        if (checkAuth(context)) ReturnData() else null

    private fun needLogin(): ReturnData = ReturnData().setData("NEED_LOGIN").setErrorMsg("login required")

    private fun resolveBook(context: RoutingContext, userNameSpace: String): Book? {
        val bookUrl = stringParam(context, "url", "bookUrl")
        return bookUrl?.let { getShelfBookByURL(it, userNameSpace).takeIf { book -> book.bookUrl.isNotEmpty() } }
            ?: bodyBook(context, userNameSpace)
    }

    private fun bodyBook(context: RoutingContext, userNameSpace: String): Book? = runCatching {
        context.bodyAsJson?.mapTo(Book::class.java)?.apply { setUserNameSpace(userNameSpace) }
    }.getOrNull()

    private fun stringParam(context: RoutingContext, vararg names: String): String? {
        for (name in names) {
            context.bodyAsJson?.getValue(name)?.toString()?.takeIf(String::isNotEmpty)?.let { return it }
            context.queryParam(name).firstOrNull()?.takeIf(String::isNotEmpty)?.let { return it }
        }
        return null
    }

    private fun intParam(context: RoutingContext, name: String): Int? = context.bodyAsJson?.getValue(name)?.let { value ->
        when (value) {
            is Number -> value.toInt()
            is Boolean -> if (value) 1 else 0
            else -> value.toString().toIntOrNull()
        }
    } ?: context.queryParam(name).firstOrNull()?.toIntOrNull()

    private fun booleanParam(context: RoutingContext, name: String): Boolean = intParam(context, name)?.let { it > 0 }
        ?: context.queryParam(name).firstOrNull()?.toBooleanStrictOrNull()
        ?: false

    private fun shelfJson(userNameSpace: String): JsonArray =
        getUserStorage(userNameSpace, "bookshelf").asJsonArray() ?: JsonArray()

    private fun shelfIndex(shelf: JsonArray, bookUrl: String): Int = (0 until shelf.size()).firstOrNull { index ->
        shelf.getJsonObject(index).getString("bookUrl") == bookUrl
    } ?: -1

    private fun userBookSources(userNameSpace: String): List<BookSource> {
        val sources = getUserStorage(userNameSpace, "bookSource").asJsonArray()
            ?: getUserStorage("default", "bookSource").asJsonArray()
            ?: JsonArray()
        return sources.mapNotNull { value ->
            runCatching { BookSource.fromJson((value as JsonObject).encode()).getOrNull() }.getOrNull()
        }
    }

    private suspend fun updateBookGroups(context: RoutingContext, add: Boolean?): ReturnData {
        val result = authenticated(context) ?: return needLogin()
        val userNameSpace = getUserNameSpace(context)
        val body = context.bodyAsJson ?: return result.setErrorMsg("group data is required")
        val urls = body.getJsonArray("bookUrls")?.map { it.toString() }
            ?: listOf(body.getString("bookUrl").orEmpty()).filter(String::isNotEmpty)
        val group = body.getLong("groupId", body.getLong("group", 0L))
        urls.forEach { url ->
            val book = getShelfBookByURL(url, userNameSpace)
            if (book.bookUrl.isNotEmpty()) {
                book.group = when (add) {
                    true -> book.group or group
                    false -> book.group and group.inv()
                    null -> group
                }
                saveBookToShelf(book, userNameSpace, context)
            }
        }
        return result.setData("")
    }

    private fun deleteShelfBooks(userNameSpace: String, urls: Set<String>) {
        val shelf = shelfJson(userNameSpace)
        saveUserStorage(userNameSpace, "bookshelf", JsonArray(shelf.filter { value ->
            (value as JsonObject).getString("bookUrl") !in urls
        }))
    }

    private fun extractArchive(book: Book, force: Boolean): Boolean = runCatching {
        val archive = book.getLocalFile()
        if (!archive.isFile) return false
        val destination = File(archive.parentFile, archive.nameWithoutExtension)
        if (destination.exists() && !force) return true
        destination.mkdirs()
        ZipInputStream(BufferedInputStream(FileInputStream(archive))).use { zip ->
            var entry: ZipEntry? = zip.nextEntry
            val root = destination.canonicalFile
            while (entry != null) {
                val target = File(destination, entry.name).canonicalFile
                if (!target.path.startsWith(root.path + File.separator) && target != root) {
                    error("archive contains an unsafe path")
                }
                if (entry.isDirectory) target.mkdirs() else {
                    target.parentFile.mkdirs()
                    target.outputStream().use { zip.copyTo(it) }
                }
                zip.closeEntry()
                entry = zip.nextEntry
            }
        }
        true
    }.getOrDefault(false)

    private fun convertPdfPageToImage(
        book: Book,
        pageIndex: Int,
        force: Boolean,
        document: PDDocument,
        renderer: PDFRenderer,
    ) {
        val image = File(getChapterCacheDir(book, book.getUserNameSpace()), "pdf-$pageIndex.png")
        if (!force && image.isFile) return
        savePdfPageToImage(document, renderer, pageIndex, book.pdfImageWidth, image.absolutePath, image)
    }

    private fun addFilesToZip(root: File, current: File, zip: ZipOutputStream) {
        current.listFiles()?.forEach { file ->
            if (file.isDirectory) {
                addFilesToZip(root, file, zip)
            } else {
                val path = root.toPath().relativize(file.toPath()).toString().replace(File.separatorChar, '/')
                zip.putNextEntry(ZipEntry(path))
                file.inputStream().use { it.copyTo(zip) }
                zip.closeEntry()
            }
        }
    }

    private fun sse(context: RoutingContext, data: Any?) {
        context.response()
            .putHeader("Content-Type", "text/event-stream; charset=utf-8")
            .putHeader("Cache-Control", "no-cache")
            .end("data: ${gson.toJson(data)}\\n\\n")
    }

    private suspend fun searchPosition(content: String, query: String): List<Int> {
        if (query.isEmpty()) return emptyList()
        val positions = arrayListOf<Int>()
        var offset = 0
        while (offset <= content.length - query.length) {
            val position = content.indexOf(query, offset, ignoreCase = true)
            if (position < 0) break
            positions += position
            offset = position + query.length
        }
        return positions
    }

    private fun getResultAndQueryIndex(content: String, queryIndex: Int, query: String): Pair<Int, String> {
        val start = (queryIndex - 40).coerceAtLeast(0)
        val end = (queryIndex + query.length + 80).coerceAtMost(content.length)
        return Pair(queryIndex - start, content.substring(start, end))
    }

    private fun safeFileName(name: String): String = name.replace(Regex("[\\\\/:*?\"<>|]"), "_").ifEmpty { "book" }
}
