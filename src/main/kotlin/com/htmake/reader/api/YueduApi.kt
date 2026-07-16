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
