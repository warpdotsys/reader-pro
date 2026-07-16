/** Business rewrite from reader-pro-3.2.14.jar — phase2. Readability/audit. */

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
import org.springframework.scheduling.annotation.Scheduled
import org.springframework.stereotype.Component
import kotlin.coroutines.CoroutineContext

/**
 * Full /reader3 API surface (133 endpoints from jar MANIFEST routes).
 * Handlers map to business controllers; SSE endpoints noted explicitly.
 */
@Component
open class YueduApi : RestVerticle() {

    private val appConfig: AppConfig by lazy {
        SpringContextUtils.getBean("appConfig", AppConfig::class.java)
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
        port = 8080
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

        // static
        router.route("/*").handler(StaticHandler.create("web").setDefaultContentEncoding("UTF-8"))
        router.route("/assets/*").handler(
            StaticHandler.create().setAllowRootFileSystemAccess(true)
                .setWebRoot(ExtKt.getWorkDir("storage", "assets"))
        )
        router.route("/book-assets/*").handler(
            StaticHandler.create().setAllowRootFileSystemAccess(true)
                .setWebRoot(ExtKt.getWorkDir("storage", "data"))
        )
        router.route("/epub/*").handler(
            StaticHandler.create().setAllowRootFileSystemAccess(true)
                .setWebRoot(ExtKt.getWorkDir("storage", "data"))
        )
        router.route("/simple-web/*").handler(StaticHandler.create("simple-web"))

        // ---- system ----
        get(router, "/reader3/getSystemInfo") { getSystemInfo(it) }

        // ---- book source ----
        post(router, "/reader3/saveBookSource") { bookSource.saveBookSource(it) }
        post(router, "/reader3/saveBookSources") { bookSource.saveBookSources(it) }
        get(router, "/reader3/getBookSource") { bookSource.getBookSource(it) }
        post(router, "/reader3/getBookSource") { bookSource.getBookSource(it) }
        get(router, "/reader3/getBookSources") { bookSource.getBookSources(it) }
        post(router, "/reader3/getBookSources") { bookSource.getBookSources(it) }
        post(router, "/reader3/deleteAllBookSources") { bookSource.deleteAllBookSources(it) }
        post(router, "/reader3/deleteBookSource") { bookSource.deleteBookSource(it) }
        post(router, "/reader3/deleteBookSources") { bookSource.deleteBookSources(it) }
        post(router, "/reader3/readSourceFile") { bookSource.readSourceFile(it) }
        post(router, "/reader3/saveFromRemoteSource") { bookSource.saveFromRemoteSource(it) }
        post(router, "/reader3/setAsDefaultBookSources") { bookSource.setAsDefaultBookSources(it) }
        post(router, "/reader3/deleteUserBookSource") { bookSource.deleteAllBookSources(it) }
        post(router, "/reader3/deleteBookSourcesFile") { bookSource.deleteAllBookSources(it) }
        post(router, "/reader3/getInvalidBookSources") { book.getInvalidBookSources(it) }
        get(router, "/reader3/searchBookSource") { book.searchBookSource(it) }
        post(router, "/reader3/searchBookSource") { book.searchBookSource(it) }
        get(router, "/reader3/getAvailableBookSource") { book.getAvailableBookSource(it) }
        post(router, "/reader3/getAvailableBookSource") { book.getAvailableBookSource(it) }
        get(router, "/reader3/searchBookSourceSSE") { book.searchBookSourceSSE(it); null }
        get(router, "/reader3/setBookSource") { book.setBookSource(it) }
        post(router, "/reader3/setBookSource") { book.setBookSource(it) }
        get(router, "/reader3/bookSourceDebugSSE") { book.bookSourceDebugSSE(it); null }

        // ---- bookshelf / read ----
        get(router, "/reader3/getBookshelf") { book.getBookshelf(it) }
        get(router, "/reader3/getShelfBook") { book.getShelfBook(it) }
        post(router, "/reader3/saveBook") { book.saveBook(it) }
        post(router, "/reader3/deleteBook") { book.deleteBook(it) }
        post(router, "/reader3/deleteBooks") { book.deleteBooks(it) }
        get(router, "/reader3/exploreBook") { book.exploreBook(it) }
        post(router, "/reader3/exploreBook") { book.exploreBook(it) }
        get(router, "/reader3/searchBook") { book.searchBook(it) }
        post(router, "/reader3/searchBook") { book.searchBook(it) }
        get(router, "/reader3/searchBookMulti") { book.searchBookMulti(it) }
        post(router, "/reader3/searchBookMulti") { book.searchBookMulti(it) }
        get(router, "/reader3/searchBookMultiSSE") { book.searchBookMultiSSE(it); null }
        get(router, "/reader3/getBookInfo") { book.getBookInfo(it) }
        post(router, "/reader3/getBookInfo") { book.getBookInfo(it) }
        get(router, "/reader3/getChapterList") { book.getChapterList(it) }
        post(router, "/reader3/getChapterList") { book.getChapterList(it) }
        get(router, "/reader3/getBookContent") { book.getBookContent(it) }
        post(router, "/reader3/getBookContent") { book.getBookContent(it) }
        post(router, "/reader3/saveBookContent") { book.saveBookContent(it) }
        post(router, "/reader3/saveBookProgress") { book.saveBookProgress(it) }
        get(router, "/reader3/cover") { book.cover(it); null }
        post(router, "/reader3/importBookPreview") { book.importBookPreview(it) }
        post(router, "/reader3/refreshLocalBook") { book.refreshLocalBook(it) }
        get(router, "/reader3/getTxtTocRules") { book.getTxtTocRules(it) }
        post(router, "/reader3/getChapterListByRule") { book.getChapterListByRule(it) }
        get(router, "/reader3/cacheBookSSE") { book.cacheBookSSE(it); null }
        post(router, "/reader3/cacheBookOnServer") { book.cacheBookOnServer(it) }
        get(router, "/reader3/getShelfBookWithCacheInfo") { book.getShelfBookWithCacheInfo(it) }
        post(router, "/reader3/deleteBookCache") { book.deleteBookCache(it) }
        post(router, "/reader3/exportBook") { book.exportBook(it) }
        get(router, "/reader3/exportBook") { book.exportBook(it) }
        get(router, "/reader3/searchBookContent") { book.searchBookContent(it) }
        post(router, "/reader3/searchBookContent") { book.searchBookContent(it) }
        post(router, "/reader3/book/saveBookConfig") { book.saveBookConfig(it) }

        // ---- groups ----
        post(router, "/reader3/saveBookGroupId") { book.saveBookGroupId(it) }
        post(router, "/reader3/addBookGroupMulti") { book.addBookGroupMulti(it) }
        post(router, "/reader3/removeBookGroupMulti") { book.removeBookGroupMulti(it) }
        get(router, "/reader3/getBookGroups") { group.getBookGroups(it) }
        post(router, "/reader3/saveBookGroup") { group.saveBookGroup(it) }
        post(router, "/reader3/deleteBookGroup") { group.deleteBookGroup(it) }
        post(router, "/reader3/saveBookGroupOrder") { group.saveBookGroupOrder(it) }

        // ---- bookmarks ----
        get(router, "/reader3/getBookmarks") { bookmark.getBookmarks(it) }
        post(router, "/reader3/saveBookmark") { bookmark.saveBookmark(it) }
        post(router, "/reader3/saveBookmarks") { bookmark.saveBookmark(it) }
        post(router, "/reader3/deleteBookmark") { bookmark.deleteBookmark(it) }
        post(router, "/reader3/deleteBookmarks") { bookmark.deleteBookmark(it) }

        // ---- replace rules ----
        get(router, "/reader3/getReplaceRules") { replace.getReplaceRules(it) }
        post(router, "/reader3/saveReplaceRule") { replace.saveReplaceRule(it) }
        post(router, "/reader3/saveReplaceRules") { replace.saveReplaceRule(it) }
        post(router, "/reader3/deleteReplaceRule") { replace.deleteReplaceRule(it) }
        post(router, "/reader3/deleteReplaceRules") { replace.deleteReplaceRule(it) }

        // ---- user ----
        post(router, "/reader3/login") { user.login(it) }
        post(router, "/reader3/logout") { user.logout(it) }
        get(router, "/reader3/getUserInfo") { user.getUserInfo(it) }
        post(router, "/reader3/saveUserConfig") { user.saveUserConfig(it) }
        get(router, "/reader3/getUserConfig") { user.getUserConfig(it) }
        get(router, "/reader3/getUserList") { user.getUserList(it) }
        post(router, "/reader3/deleteUsers") { user.deleteUsers(it) }
        post(router, "/reader3/clearInactiveUsers") { user.clearInactiveUsers(it) }
        post(router, "/reader3/addUser") { user.addUser(it) }
        post(router, "/reader3/resetPassword") { user.resetPassword(it) }
        post(router, "/reader3/updateUser") { user.updateUser(it) }
        post(router, "/reader3/uploadFile") { user.uploadFile(it) }
        post(router, "/reader3/deleteFile") { user.deleteFile(it) }
        get(router, "/reader3/user/downloadBackupFile") { user.downloadBackupFile(it); null }

        // ---- license ----
        get(router, "/reader3/getLicense") { license.getLicense(it) }
        post(router, "/reader3/importLicense") { license.importLicense(it) }
        get(router, "/reader3/generateKeys") { license.generateKeys(it) }
        post(router, "/reader3/generateKeys") { license.generateKeys(it) }
        get(router, "/reader3/generateLicense") { license.generateLicense(it) }
        post(router, "/reader3/generateLicense") { license.generateLicense(it) }
        get(router, "/reader3/isHostValid") { license.isHostValid(it) }
        post(router, "/reader3/isHostValid") { license.isHostValid(it) }
        post(router, "/reader3/activateLicense") { license.activateLicense(it) }
        get(router, "/reader3/isLicenseValid") { license.isLicenseValid(it) }
        post(router, "/reader3/isLicenseValid") { license.isLicenseValid(it) }
        post(router, "/reader3/decryptLicense") { license.decryptLicense(it) }
        post(router, "/reader3/sendCodeToEmail") { license.sendCodeToEmail(it) }
        post(router, "/reader3/supplyLicense") { license.supplyLicense(it) }

        // ---- rss ----
        get(router, "/reader3/getRssSources") { rss.getRssSources(it) }
        post(router, "/reader3/saveRssSource") { rss.saveRssSource(it) }
        post(router, "/reader3/saveRssSources") { rss.saveRssSource(it) }
        post(router, "/reader3/deleteRssSource") { rss.deleteRssSource(it) }
        get(router, "/reader3/getRssArticles") { rss.getRssArticles(it) }
        post(router, "/reader3/getRssArticles") { rss.getRssArticles(it) }
        get(router, "/reader3/getRssContent") { rss.getRssContent(it) }
        post(router, "/reader3/getRssContent") { rss.getRssContent(it) }

        // ---- files ----
        get(router, "/reader3/file/list") { file.list(it) }
        get(router, "/reader3/file/get") { file.get(it) }
        post(router, "/reader3/file/save") { file.save(it) }
        post(router, "/reader3/file/mkdir") { file.mkdir(it) }
        get(router, "/reader3/file/download") { file.download(it); null }
        post(router, "/reader3/file/delete") { file.delete(it) }
        post(router, "/reader3/file/deleteMulti") { file.deleteMulti(it) }
        post(router, "/reader3/file/importPreview") { file.importPreview(it) }
        post(router, "/reader3/file/restore") { file.restore(it) }
        get(router, "/reader3/file/parse") { file.parse(it) }
        post(router, "/reader3/file/parse") { file.parse(it) }
        post(router, "/reader3/file/upload") { file.upload(it) }

        // ---- tts ----
        get(router, "/reader3/book/tts") { book.tts(it); null }
        post(router, "/reader3/book/tts") { book.tts(it); null }
        get(router, "/reader3/httpTTS/list") { httpTts.list(it) }
        post(router, "/reader3/httpTTS/save") { httpTts.save(it) }
        post(router, "/reader3/httpTTS/saveMulti") { httpTts.saveMulti(it) }
        post(router, "/reader3/httpTTS/delete") { httpTts.delete(it) }
        post(router, "/reader3/httpTTS/deleteMulti") { httpTts.deleteMulti(it) }

        // ---- webdav / mongo ----
        post(router, "/reader3/backupToWebdav") { webdav.backupToWebdav(it) }
        post(router, "/reader3/backupToMongodb") { book.backupToMongodb(it) }
        post(router, "/reader3/restoreFromMongodb") { book.restoreFromMongodb(it) }
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
                null -> { /* response already written (SSE/file) */ }
                else -> VertExtKt.success(ctx, r)
            }
        } catch (e: Exception) {
            onHandlerError(ctx, e)
        }
    }

    suspend fun getSystemInfo(context: RoutingContext): ReturnData =
        ReturnData().setData(
            mapOf(
                "version" to "3.2.14",
                "secure" to appConfig.secure,
                "userLimit" to appConfig.userLimit,
                "java" to System.getProperty("java.version"),
            )
        )

    @Scheduled(fixedDelayString = "\${reader.app.shelfUpdateInteval:30}000")
    open fun shelfUpdateJob() {}

    @Scheduled(fixedDelayString = "\${reader.app.remoteBookSourceUpdateInterval:720}0000")
    open fun remoteBookSourceSubUpdateJob() {}

    @Scheduled(cron = "0 0 3 * * ?")
    open fun clearUser() {}

    @Scheduled(cron = "0 30 3 * * ?")
    open fun autoBackup() {}

    @Scheduled(fixedDelay = 600_000)
    open fun autoGC() { System.gc() }

    @Scheduled(fixedDelay = 3_600_000)
    open fun checkLicense() {}
}
