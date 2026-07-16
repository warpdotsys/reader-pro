# -*- coding: utf-8 -*-
"""Generate elegant, self-contained, compilable reader-pro Kotlin sources."""
from pathlib import Path
import os

ROOT = Path(r"C:\Users\chong\reader-pro-3.2.14-reverse")
SRC = ROOT / "src" / "main" / "kotlin"
RES = ROOT / "src" / "main" / "resources"


def w(rel: str, content: str):
    p = SRC / rel.replace("/", os.sep)
    p.parent.mkdir(parents=True, exist_ok=True)
    p.write_text(content.lstrip("\n") if content.startswith("\n") else content, encoding="utf-8", newline="\n")
    print("wrote", rel)


# ---------------------------------------------------------------------------
# Bootstrap
# ---------------------------------------------------------------------------
w("com/htmake/reader/ReaderApplication.kt", r'''
package com.htmake.reader

import com.fasterxml.jackson.databind.DeserializationFeature
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.fasterxml.jackson.module.kotlin.registerKotlinModule
import com.htmake.reader.api.YueduApi
import io.vertx.core.Vertx
import io.vertx.core.http.HttpClientOptions
import io.vertx.core.json.Json
import io.vertx.ext.web.client.WebClient
import io.vertx.ext.web.client.WebClientOptions
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.autoconfigure.data.mongo.MongoDataAutoConfiguration
import org.springframework.boot.autoconfigure.mongo.MongoAutoConfiguration
import org.springframework.boot.builder.SpringApplicationBuilder
import org.springframework.context.annotation.Bean
import org.springframework.scheduling.annotation.EnableScheduling
import javax.annotation.PostConstruct

@SpringBootApplication(exclude = [MongoAutoConfiguration::class, MongoDataAutoConfiguration::class])
@EnableScheduling
open class ReaderApplication(
    private val yueduApi: YueduApi
) {
    @PostConstruct
    open fun deployVerticle() {
        runCatching {
            Json.mapper.registerKotlinModule()
            Json.prettyMapper.registerKotlinModule()
        }
        Json.mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false)
        vertx.deployVerticle(yueduApi)
    }

    @Bean
    open fun webClient(): WebClient {
        val opts = WebClientOptions()
            .setTryUseCompression(true)
            .setFollowRedirects(true)
            .setTrustAll(true)
        val http = vertx.createHttpClient(HttpClientOptions().setTrustAll(true))
        return WebClient.wrap(http, opts)
    }

    @Bean
    open fun objectMapper() = jacksonObjectMapper().findAndRegisterModules()
        .configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false)

    companion object {
        val vertx: Vertx by lazy { Vertx.vertx() }

        @JvmStatic
        fun main(args: Array<String>) {
            SpringApplicationBuilder(ReaderApplication::class.java).run(*args)
        }
    }
}
''')

w("com/htmake/reader/config/AppConfig.kt", r'''
package com.htmake.reader.config

import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.stereotype.Component

@Component("appConfig")
@ConfigurationProperties(prefix = "reader.app")
class AppConfig {
    var workDir: String = "."
    var showUI: Boolean = false
    var debug: Boolean = false
    var packaged: Boolean = false
    var secure: Boolean = false
    var inviteCode: String = ""
    var secureKey: String = ""
    var debugLog: Boolean = false
    var userLimit: Int = 50
    var userBookLimit: Int = 200
    var mongoUri: String = ""
    var mongoDbName: String = "reader"
    var cacheChapterContent: Boolean = true
    var remoteWebviewApi: String = ""
    var minUserPasswordLength: Int = 8
    var shelfUpdateInteval: Int = 30
    var autoClearInactiveUser: Int = 0
    var autoBackupUserData: Boolean = false
}
''')

w("com/htmake/reader/config/UserConfig.kt", r'''
package com.htmake.reader.config

import io.vertx.core.json.JsonObject

object UserConfigKeys {
    const val THEME = "theme"
    const val FONT_FAMILY = "fontFamily"
    const val FONT_SIZE = "fontSize"
    const val PAGE_MODE = "pageMode"
    const val TTS_TYPE = "ttsType"
    const val TTS_VOICE = "ttsVoice"
    const val SEARCH_CONCURRENT = "searchConcurrent"
    const val CACHE_CONCURRENT = "cacheConcurrent"
    const val UPDATE_TIME = "@updateTime"
}

object UserConfigDefaults {
    fun base(): JsonObject = JsonObject()
        .put(UserConfigKeys.THEME, "light")
        .put(UserConfigKeys.FONT_FAMILY, "system-ui")
        .put(UserConfigKeys.FONT_SIZE, 18)
        .put(UserConfigKeys.PAGE_MODE, "slide")
        .put(UserConfigKeys.TTS_TYPE, "edge")
        .put(UserConfigKeys.TTS_VOICE, "zh-CN-XiaoxiaoNeural")
        .put(UserConfigKeys.SEARCH_CONCURRENT, 36)
        .put(UserConfigKeys.CACHE_CONCURRENT, 24)

    fun merge(stored: JsonObject?, patch: JsonObject? = null): JsonObject {
        val out = base()
        stored?.forEach { out.put(it.key, it.value) }
        patch?.forEach { out.put(it.key, it.value) }
        return out
    }
}
''')

w("com/htmake/reader/entity/User.kt", r'''
package com.htmake.reader.entity

data class User(
    var username: String = "",
    var password: String = "",
    var salt: String = "",
    var token: String? = null,
    var isManager: Boolean = false,
    var enableWebdav: Boolean = true,
    var enableLocalStore: Boolean = true,
    var enableBookSource: Boolean = true,
    var enableRssSource: Boolean = true
)
''')

w("com/htmake/reader/entity/License.kt", r'''
package com.htmake.reader.entity

data class License(
    var host: String = "",
    var email: String = "",
    var code: String = "",
    var expireAt: Long = 0,
    var activated: Boolean = false
)
''')

w("com/htmake/reader/utils/SpringContextUtils.kt", r'''
package com.htmake.reader.utils

import org.springframework.context.ApplicationContext
import org.springframework.context.ApplicationContextAware
import org.springframework.stereotype.Component

@Component
class SpringContextUtils : ApplicationContextAware {
    override fun setApplicationContext(applicationContext: ApplicationContext) {
        ctx = applicationContext
    }

    companion object {
        @Volatile private var ctx: ApplicationContext? = null

        fun <T> getBean(name: String, type: Class<T>): T =
            ctx?.getBean(name, type)
                ?: error("Spring context not ready for bean $name")

        fun <T> getBean(type: Class<T>): T =
            ctx?.getBean(type)
                ?: error("Spring context not ready for type ${type.name}")
    }
}
''')

w("com/htmake/reader/utils/ExtKt.kt", r'''
package com.htmake.reader.utils

import com.htmake.reader.config.AppConfig
import io.vertx.core.json.Json
import io.vertx.core.json.JsonArray
import io.vertx.core.json.JsonObject
import java.io.File

object ExtKt {
    private fun appConfig(): AppConfig = try {
        SpringContextUtils.getBean("appConfig", AppConfig::class.java)
    } catch (_: Exception) {
        AppConfig()
    }

    fun getWorkDir(vararg parts: String): String {
        val base = File(appConfig().workDir).absoluteFile
        return if (parts.isEmpty()) base.absolutePath
        else File(base, parts.joinToString(File.separator)).absolutePath
    }

    fun getStorage(vararg path: String): String? {
        if (path.isEmpty()) return null
        val file = File(getWorkDir(*path))
        val withJson = if (file.extension.isEmpty()) File(file.parent, file.name + ".json") else file
        val target = when {
            file.isFile -> file
            withJson.isFile -> withJson
            else -> File(getWorkDir(*path.dropLast(1).toTypedArray() + (path.last() + ".json")))
        }
        return if (target.isFile) target.readText(Charsets.UTF_8) else null
    }

    fun saveStorage(path: Array<String>, value: String) {
        if (path.isEmpty()) return
        val name = path.last().let { if (it.endsWith(".json")) it else "$it.json" }
        val dir = File(getWorkDir(*path.dropLast(1).toTypedArray()))
        dir.mkdirs()
        File(dir, name).writeText(value, Charsets.UTF_8)
    }

    fun asJsonArray(raw: String?): JsonArray? = try {
        if (raw.isNullOrBlank()) null else JsonArray(raw)
    } catch (_: Exception) {
        null
    }

    fun asJsonObject(raw: String?): JsonObject? = try {
        if (raw.isNullOrBlank()) null else JsonObject(raw)
    } catch (_: Exception) {
        null
    }

    fun jsonEncode(obj: Any?, pretty: Boolean = false): String = when (obj) {
        null -> "null"
        is String -> obj
        is JsonObject, is JsonArray -> if (pretty) obj.encodePrettily() else obj.encode()
        else -> try {
            if (pretty) Json.encodePrettily(obj) else Json.encode(obj)
        } catch (_: Exception) {
            obj.toString()
        }
    }

    fun deleteRecursively(f: File?) {
        if (f == null) return
        if (f.isDirectory) f.listFiles()?.forEach { deleteRecursively(it) }
        f.delete()
    }

    fun getRelativePath(vararg parts: String): String = parts.joinToString(File.separator)
}
''')

w("com/htmake/reader/utils/VertExtKt.kt", r'''
package com.htmake.reader.utils

import com.htmake.reader.api.ReturnData
import io.vertx.core.http.HttpHeaders
import io.vertx.ext.web.RoutingContext

object VertExtKt {
    fun success(ctx: RoutingContext, data: Any?) {
        val body = when (data) {
            is ReturnData -> ExtKt.jsonEncode(data.toMap())
            is String -> data
            else -> ExtKt.jsonEncode(data)
        }
        if (!ctx.response().ended()) {
            ctx.response()
                .putHeader(HttpHeaders.CONTENT_TYPE, "application/json; charset=utf-8")
                .end(body)
        }
    }
}
''')

w("com/htmake/reader/utils/UserMutex.kt", r'''
package com.htmake.reader.utils

import kotlinx.coroutines.sync.Mutex
import java.util.concurrent.ConcurrentHashMap

object UserMutex {
    private val locks = ConcurrentHashMap<String, Mutex>()
    fun getLocker(key: String): Mutex = locks.computeIfAbsent(key) { Mutex() }
}
''')

w("com/htmake/reader/utils/EncoderUtils.kt", r'''
package com.htmake.reader.utils

import cn.hutool.crypto.asymmetric.KeyType
import cn.hutool.crypto.asymmetric.RSA
import java.util.Base64

object EncoderUtils {
    fun genRsaPair(): Pair<String, String> {
        val rsa = RSA()
        return Base64.getEncoder().encodeToString(rsa.publicKey.encoded) to
            Base64.getEncoder().encodeToString(rsa.privateKey.encoded)
    }

    fun rsaEncrypt(publicKeyBase64: String, data: String): String {
        val rsa = RSA(null, publicKeyBase64)
        return rsa.encryptBase64(data, KeyType.PublicKey)
    }

    fun rsaDecrypt(privateKeyBase64: String, data: String): String {
        val rsa = RSA(privateKeyBase64, null)
        return rsa.decryptStr(data, KeyType.PrivateKey)
    }
}
''')

w("com/htmake/reader/api/ReturnData.kt", r'''
package com.htmake.reader.api

data class ReturnData(
    var isSuccess: Boolean = true,
    var errorMsg: String = "",
    var data: Any? = null
) {
    fun setData(v: Any?): ReturnData {
        data = v
        if (v != null && errorMsg.isEmpty()) isSuccess = true
        return this
    }

    fun setErrorMsg(msg: String): ReturnData {
        errorMsg = msg
        isSuccess = false
        return this
    }

    fun toMap(): Map<String, Any?> = mapOf(
        "isSuccess" to isSuccess,
        "errorMsg" to errorMsg,
        "data" to data
    )
}
''')

w("com/htmake/reader/verticle/RestVerticle.kt", r'''
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

abstract class RestVerticle : CoroutineVerticle() {
    protected lateinit var router: Router
    var port: Int = 8080

    abstract fun getContextPath(): String
    abstract suspend fun initRouter(router: Router)

    override suspend fun start() {
        router = Router.router(vertx)
        router.route().handler(LoggerHandler.create())
        router.route().handler(
            BodyHandler.create().setUploadsDirectory(ExtKt.getWorkDir("storage", "cache", "uploads"))
        )
        router.route().handler(
            SessionHandler.create(LocalSessionStore.create(vertx)).setNagHttps(false)
        )
        router.route().handler { ctx ->
            val origin = ctx.request().getHeader("Origin") ?: "*"
            ctx.response()
                .putHeader("Access-Control-Allow-Origin", origin)
                .putHeader("Access-Control-Allow-Credentials", "true")
            if (ctx.request().method() == HttpMethod.OPTIONS) {
                ctx.response()
                    .putHeader("Access-Control-Allow-Methods", "GET,POST,PUT,DELETE,OPTIONS,MOVE,COPY,PROPFIND")
                    .putHeader("Access-Control-Allow-Headers", "Content-Type,Authorization,secureKey,Destination,Overwrite")
                    .end()
            } else ctx.next()
        }
        initRouter(router)
        vertx.createHttpServer().requestHandler(router).listen(port) { ar ->
            if (ar.succeeded()) {
                println("ReaderApplication Started on :$port")
            } else {
                ar.cause().printStackTrace()
            }
        }
    }

    open fun onHandlerError(ctx: RoutingContext, error: Exception) {
        error.printStackTrace()
        if (!ctx.response().ended()) {
            ctx.response().setStatusCode(500).end(error.message ?: "error")
        }
    }
}
''')

# Base controller + key controllers (streamlined but complete)
w("com/htmake/reader/api/controller/BaseController.kt", r'''
package com.htmake.reader.api.controller

import com.htmake.reader.config.AppConfig
import com.htmake.reader.utils.ExtKt
import com.htmake.reader.utils.SpringContextUtils
import io.vertx.core.json.Json
import io.vertx.core.json.JsonArray
import io.vertx.core.json.JsonObject
import io.vertx.ext.web.RoutingContext
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import org.springframework.core.env.Environment
import kotlin.coroutines.CoroutineContext

open class BaseController(
    override val coroutineContext: CoroutineContext
) : CoroutineScope {

    protected val appConfig: AppConfig by lazy {
        try {
            SpringContextUtils.getBean("appConfig", AppConfig::class.java)
        } catch (_: Exception) {
            AppConfig()
        }
    }

    protected val env: Environment by lazy {
        try {
            SpringContextUtils.getBean(Environment::class.java)
        } catch (_: Exception) {
            error("Environment not ready")
        }
    }

    fun getAppConfig(): AppConfig = appConfig

    open suspend fun checkAuth(context: RoutingContext): Boolean {
        if (!appConfig.secure) return true
        val username = context.session()?.get<String>("username")
        if (!username.isNullOrEmpty()) return true
        val accessToken = context.queryParam("accessToken").firstOrNull().orEmpty()
        if (accessToken.isEmpty()) return false
        val parts = accessToken.split(":", limit = 2)
        if (parts.size < 2) return false
        val users = loadUserMap()
        val info = users[parts[0]] ?: return false
        val tokenMap = info["token_map"] as? Map<*, *>
        if (tokenMap != null && tokenMap.containsKey(parts[1])) {
            context.session()?.put("username", parts[0])
            return true
        }
        if (info["token"] == parts[1]) {
            context.session()?.put("username", parts[0])
            return true
        }
        return false
    }

    open fun checkManagerAuth(context: RoutingContext): Boolean {
        if (!appConfig.secure) return true
        val key = context.queryParam("secureKey").firstOrNull()
            ?: context.request().getHeader("secureKey")
            ?: ""
        return key.isNotEmpty() && key == appConfig.secureKey
    }

    open fun getUserNameSpace(context: RoutingContext): String {
        if (!appConfig.secure) return "default"
        return context.session()?.get<String>("username") ?: "default"
    }

    open fun getUserWebdavHome(userNameSpace: String): String =
        ExtKt.getWorkDir("storage", "data", userNameSpace, "webdav")

    open fun getUserStorage(userNameSpace: String, vararg path: String): String? =
        ExtKt.getStorage(*arrayOf("data", userNameSpace) + path)

    open fun saveUserStorage(userNameSpace: String, name: String, value: Any?) {
        val encoded = when (value) {
            is String -> value
            else -> Json.encode(value)
        }
        ExtKt.saveStorage(arrayOf("data", userNameSpace, name), encoded)
    }

    @Suppress("UNCHECKED_CAST")
    protected fun loadUserMap(): MutableMap<String, MutableMap<String, Any?>> {
        val raw = ExtKt.getStorage("data", "users")
        val obj = ExtKt.asJsonObject(raw) ?: return linkedMapOf()
        val out = linkedMapOf<String, MutableMap<String, Any?>>()
        obj.forEach { (k, v) ->
            out[k] = when (v) {
                is JsonObject -> v.map.toMutableMap()
                is Map<*, *> -> (v as Map<String, Any?>).toMutableMap()
                else -> mutableMapOf()
            }
        }
        return out
    }

    protected fun saveUserMap(map: Map<String, *>) {
        ExtKt.saveStorage(arrayOf("data", "users"), JsonObject(map as Map<String, Any?>).encode())
    }

    open suspend fun limitConcurrent(
        concurrent: Int,
        start: Int,
        end: Int,
        block: suspend (Int) -> Boolean
    ) = coroutineScope {
        val n = concurrent.coerceAtLeast(1)
        (start until end).chunked(n).forEach { batch ->
            batch.map { i -> async { block(i) } }.awaitAll()
        }
    }
}
''')

print("core files done, generating entities & engines...")

# Entities
w("io/legado/app/data/entities/BaseSource.kt", r'''
package io.legado.app.data.entities

import io.legado.app.help.CacheManager
import io.legado.app.model.analyzeRule.AnalyzeRule

interface BaseSource {
    fun getKey(): String = ""
    fun getTag(): String = ""
    fun getHeader(): String? = null
    fun getLoginUrl(): String? = null
    fun getLoginUi(): String? = null
    fun getLoginCheckJs(): String? = null
    fun getUserNameSpace(): String = "default"

    fun getHeaderMap(withLogin: Boolean = false): Map<String, String> {
        val map = linkedMapOf<String, String>()
        map["User-Agent"] =
            "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/120.0.0.0 Safari/537.36"
        val h = getHeader()
        if (!h.isNullOrBlank()) {
            val json = when {
                h.startsWith("@js:", true) ->
                    runCatching { AnalyzeRule().evalJS(h.substring(4), null)?.toString() }.getOrNull()
                else -> h
            }
            if (!json.isNullOrBlank()) {
                runCatching {
                    com.google.gson.JsonParser.parseString(json).asJsonObject
                        .entrySet().forEach { map[it.key] = it.value.asString }
                }
            }
        }
        if (withLogin) getLoginHeaderMap()?.let { map.putAll(it) }
        return map
    }

    fun getLoginHeader(): String? =
        CacheManager(getUserNameSpace()).get("loginHeader_${getKey()}")

    fun putLoginHeader(headerJson: String) {
        CacheManager(getUserNameSpace()).put("loginHeader_${getKey()}", headerJson)
    }

    fun getLoginHeaderMap(): Map<String, String>? {
        val raw = getLoginHeader() ?: return null
        return runCatching {
            com.google.gson.JsonParser.parseString(raw).asJsonObject
                .entrySet().associate { it.key to it.value.asString }
        }.getOrNull()
    }
}
''')

w("io/legado/app/data/entities/BaseBook.kt", r'''
package io.legado.app.data.entities

import io.legado.app.model.analyzeRule.RuleDataInterface

interface BaseBook : RuleDataInterface {
    var name: String
    var author: String
}
''')

w("io/legado/app/data/entities/Book.kt", r'''
package io.legado.app.data.entities

data class Book(
    var bookUrl: String = "",
    var tocUrl: String = "",
    var origin: String = "",
    var originName: String = "",
    override var name: String = "",
    override var author: String = "",
    var kind: String? = null,
    var coverUrl: String? = null,
    var intro: String? = null,
    var latestChapterTitle: String? = null,
    var totalChapterNum: Int = 0,
    var durChapterIndex: Int = 0,
    var durChapterPos: Int = 0,
    var durChapterTitle: String? = null,
    var durChapterTime: Long = 0,
    var canUpdate: Boolean = true,
    var isInShelf: Boolean = false,
    var lastCheckCount: Int = 0,
    var lastCheckTime: Long = 0,
    var lastCheckError: String? = null,
    var rootDir: String? = null,
    var userNameSpace: String? = null,
    var variable: String? = null,
    var charset: String? = null,
    var group: Long = 0,
    var pdfImageWidth: Float = 0f
) : BaseBook {
    private val variableMap = linkedMapOf<String, String>()
    val isLocalBook: Boolean
        get() = origin == "loc_book" || bookUrl.startsWith("file:") ||
            listOf(".txt", ".epub", ".umd", ".cbz", ".pdf").any { bookUrl.endsWith(it, true) }
    val isEpub get() = bookUrl.endsWith(".epub", true)
    val isCbz get() = bookUrl.endsWith(".cbz", true)
    val isPdf get() = bookUrl.endsWith(".pdf", true)
    val isUmd get() = bookUrl.endsWith(".umd", true)
    val isLocalTxt get() = bookUrl.endsWith(".txt", true) || (isLocalBook && !isEpub && !isCbz && !isPdf && !isUmd)

    override fun getUserNameSpace(): String = userNameSpace ?: "default"
    override fun putVariable(key: String, value: String?) {
        if (value == null) variableMap.remove(key) else variableMap[key] = value
    }
    override fun getVariable(key: String): String? = variableMap[key]

    fun localFile(): java.io.File {
        val path = bookUrl.removePrefix("file://").removePrefix("file:")
        return java.io.File(path)
    }
}
''')

w("io/legado/app/data/entities/BookChapter.kt", r'''
package io.legado.app.data.entities

data class BookChapter(
    var url: String = "",
    var title: String = "",
    var bookUrl: String = "",
    var index: Int = 0,
    var resourceUrl: String? = null,
    var tag: String? = null,
    var start: Long? = null,
    var end: Long? = null,
    var byteStart: Long = 0,
    var variable: String? = null
)
''')

w("io/legado/app/data/entities/BookSource.kt", r'''
package io.legado.app.data.entities

import io.legado.app.data.entities.rule.*
import io.legado.app.help.SourceAnalyzer

data class BookSource(
    var bookSourceUrl: String = "",
    var bookSourceName: String = "",
    var bookSourceGroup: String? = null,
    var bookSourceType: Int = 0,
    var enabled: Boolean = true,
    var enabledExplore: Boolean = true,
    var header: String? = null,
    var loginUrl: String? = null,
    var loginUi: String? = null,
    var loginCheckJs: String? = null,
    var exploreUrl: String? = null,
    var bookUrlPattern: String? = null,
    var ruleSearch: SearchRule? = null,
    var ruleExplore: ExploreRule? = null,
    var ruleBookInfo: BookInfoRule? = null,
    var ruleToc: TocRule? = null,
    var ruleContent: ContentRule? = null,
    private var _userNameSpace: String = "default"
) : BaseSource {
    override fun getKey() = bookSourceUrl
    override fun getTag() = bookSourceName
    override fun getHeader() = header
    override fun getLoginUrl() = loginUrl
    override fun getLoginUi() = loginUi
    override fun getLoginCheckJs() = loginCheckJs
    override fun getUserNameSpace() = _userNameSpace
    fun setUserNameSpace(ns: String) { _userNameSpace = ns }

    companion object {
        fun fromJson(json: String): Result<BookSource> = SourceAnalyzer.jsonToBookSource(json)
        fun fromJsonArray(json: String): Result<List<BookSource>> =
            SourceAnalyzer.jsonToBookSources(json)
    }
}
''')

w("io/legado/app/data/entities/SearchBook.kt", r'''
package io.legado.app.data.entities

data class SearchBook(
    var name: String = "",
    var author: String = "",
    var bookUrl: String = "",
    var origin: String = "",
    var originName: String = "",
    var coverUrl: String? = null,
    var intro: String? = null,
    var kind: String? = null,
    var latestChapterTitle: String? = null
) {
    fun toBook() = Book(
        bookUrl = bookUrl, name = name, author = author,
        origin = origin, originName = originName,
        coverUrl = coverUrl, intro = intro, kind = kind,
        latestChapterTitle = latestChapterTitle
    )
}
''')

w("io/legado/app/data/entities/SearchResult.kt", r'''
package io.legado.app.data.entities

data class SearchResult(
    var resultCount: Int = 0,
    var resultCountWithinChapter: Int = 0,
    var resultText: String = "",
    var chapterTitle: String = "",
    var query: String = "",
    var pageSize: Int = 0,
    var chapterIndex: Int = 0,
    var pageIndex: Int = 0,
    var queryIndexInResult: Int = 0,
    var queryIndexInChapter: Int = 0
)
''')

w("io/legado/app/data/entities/HttpTTS.kt", r'''
package io.legado.app.data.entities

data class HttpTTS(
    var name: String = "",
    var url: String = "",
    var contentType: String? = null,
    var loginCheckJs: String? = null,
    var header: String? = null,
    private var _userNameSpace: String = "default"
) : BaseSource {
    override fun getKey() = name.ifEmpty { url }
    override fun getTag() = name
    override fun getHeader() = header
    override fun getLoginCheckJs() = loginCheckJs
    override fun getUserNameSpace() = _userNameSpace
}
''')

w("io/legado/app/data/entities/RssSource.kt", r'''
package io.legado.app.data.entities

data class RssSource(
    var sourceUrl: String = "",
    var sourceName: String = "",
    var sourceIcon: String = "",
    var sourceGroup: String? = null,
    var enabled: Boolean = true,
    var header: String? = null,
    var sortUrl: String? = null,
    var ruleArticles: String? = null,
    var ruleNextPage: String? = null,
    var ruleTitle: String? = null,
    var rulePubDate: String? = null,
    var ruleDescription: String? = null,
    var ruleImage: String? = null,
    var ruleLink: String? = null,
    var ruleContent: String? = null,
    private var _userNameSpace: String = "default"
) : BaseSource {
    override fun getKey() = sourceUrl
    override fun getTag() = sourceName
    override fun getHeader() = header
    override fun getUserNameSpace() = _userNameSpace
    fun setUserNameSpace(ns: String) { _userNameSpace = ns }
}
''')

w("io/legado/app/data/entities/RssArticle.kt", r'''
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
    var image: String? = null
)
''')

w("io/legado/app/data/entities/TxtTocRule.kt", r'''
package io.legado.app.data.entities

data class TxtTocRule(
    var name: String = "",
    var rule: String = "",
    var example: String? = null,
    var enable: Boolean = true
)
''')

w("io/legado/app/data/entities/rule/Rules.kt", r'''
package io.legado.app.data.entities.rule

data class SearchRule(
    var checkKeyWord: String? = null,
    var url: String? = null,
    var bookList: String? = null,
    var name: String? = null,
    var author: String? = null,
    var bookUrl: String? = null,
    var coverUrl: String? = null,
    var intro: String? = null,
    var kind: String? = null,
    var lastChapter: String? = null
)

data class ExploreRule(
    var bookList: String? = null,
    var name: String? = null,
    var author: String? = null,
    var bookUrl: String? = null,
    var coverUrl: String? = null,
    var intro: String? = null,
    var kind: String? = null,
    var lastChapter: String? = null
)

data class BookInfoRule(
    var name: String? = null,
    var author: String? = null,
    var kind: String? = null,
    var coverUrl: String? = null,
    var intro: String? = null,
    var tocUrl: String? = null
)

data class TocRule(
    var chapterList: String? = null,
    var chapterName: String? = null,
    var chapterUrl: String? = null,
    var nextTocUrl: String? = null,
    var preUpdateJs: String? = null
)

data class ContentRule(
    var content: String? = null,
    var nextContentUrl: String? = null,
    var replaceRegex: String? = null
)
''')

# Utils & help
w("io/legado/app/utils/NetworkUtils.kt", r'''
package io.legado.app.utils

object NetworkUtils {
    fun getAbsoluteURL(base: String?, relative: String): String {
        if (relative.startsWith("http://") || relative.startsWith("https://")) return relative
        if (base.isNullOrEmpty()) return relative
        return try {
            java.net.URL(java.net.URL(base), relative).toString()
        } catch (_: Exception) {
            relative
        }
    }

    fun getSubDomain(url: String): String {
        val host = try {
            java.net.URL(if ("://" in url) url else "http://$url").host
        } catch (_: Exception) {
            return url
        }
        if (host.isBlank()) return ""
        val parts = host.split('.')
        return if (parts.size >= 2) parts.takeLast(2).joinToString(".") else host
    }
}
''')

w("io/legado/app/utils/MD5Utils.kt", r'''
package io.legado.app.utils

import java.security.MessageDigest

object MD5Utils {
    fun md5Encode(text: String): String {
        val md = MessageDigest.getInstance("MD5")
        val bytes = md.digest(text.toByteArray())
        return bytes.joinToString("") { "%02x".format(it) }
    }
    fun md5Encode16(text: String): String = md5Encode(text).substring(8, 24)
}
''')

w("io/legado/app/utils/ACache.kt", r'''
package io.legado.app.utils

import java.io.File

class ACache private constructor(private val dir: File) {
    init { dir.mkdirs() }

    fun getAsString(key: String): String? {
        val f = fileOf(key)
        if (!f.isFile) return null
        val text = f.readText()
        if (text.startsWith("expireAt=")) {
            val nl = text.indexOf('\n')
            if (nl > 0) {
                val exp = text.substring(9, nl).toLongOrNull() ?: 0L
                if (exp > 0 && System.currentTimeMillis() > exp) {
                    f.delete(); return null
                }
                return text.substring(nl + 1)
            }
        }
        return text
    }

    fun getByHashCode(name: String): String? {
        val f = File(dir, name)
        return if (f.isFile) f.readText() else null
    }

    fun put(key: String, value: String, saveTimeSec: Int = 0) {
        dir.mkdirs()
        val body = if (saveTimeSec > 0) {
            "expireAt=${System.currentTimeMillis() + saveTimeSec * 1000L}\n$value"
        } else value
        fileOf(key).writeText(body)
    }

    fun remove(key: String) { fileOf(key).delete() }
    private fun fileOf(key: String) = File(dir, key.hashCode().toString())

    companion object {
        fun get(dir: File) = ACache(dir)
        fun get(dir: File, maxSize: Long, maxCount: Int) = ACache(dir)
    }
}
''')

w("io/legado/app/utils/FileUtils.kt", r'''
package io.legado.app.utils

import java.io.File

object FileUtils {
    fun createFileIfNotExist(parent: File, vararg names: String): File {
        var f = parent
        names.forEach { f = File(f, it) }
        f.parentFile?.mkdirs()
        if (!f.exists()) f.createNewFile()
        return f
    }
}
''')

w("io/legado/app/utils/HtmlFormatter.kt", r'''
package io.legado.app.utils

object HtmlFormatter {
    fun format(html: String): String =
        html.replace(Regex("<br\\s*/?>", RegexOption.IGNORE_CASE), "\n")
            .replace(Regex("</p>", RegexOption.IGNORE_CASE), "\n")
            .replace(Regex("<[^>]+>"), "")
            .trim()

    fun formatKeepImg(html: String): String =
        html.replace(Regex("<(?!img\\b)[^>]+>", RegexOption.IGNORE_CASE), "")
}
''')

w("io/legado/app/utils/ZipUtils.kt", r'''
package io.legado.app.utils

import java.io.File
import java.util.zip.ZipFile

object ZipUtils {
    fun unzipFile(zip: File, dest: File): Boolean {
        return try {
            dest.mkdirs()
            ZipFile(zip).use { zf ->
                zf.entries().asSequence().forEach { e ->
                    val out = File(dest, e.name)
                    if (e.isDirectory) out.mkdirs()
                    else {
                        out.parentFile?.mkdirs()
                        zf.getInputStream(e).use { ins -> out.outputStream().use { ins.copyTo(it) } }
                    }
                }
            }
            true
        } catch (_: Exception) {
            false
        }
    }
}
''')

w("io/legado/app/help/http/StrResponse.kt", r'''
package io.legado.app.help.http

data class StrResponse(val url: String, val body: String?)
''')

w("io/legado/app/help/http/CookieStore.kt", r'''
package io.legado.app.help.http

import com.htmake.reader.utils.ExtKt
import io.legado.app.utils.ACache
import io.legado.app.utils.NetworkUtils
import java.io.File

class CookieStore(val userNameSpace: String) {
    private val cache = ACache.get(File(ExtKt.getWorkDir("storage", "cache", "cookie", userNameSpace)))

    fun setCookie(url: String, cookie: String?) {
        val d = NetworkUtils.getSubDomain(url)
        if (d.isNotEmpty()) cache.put(d, cookie ?: "")
    }

    fun replaceCookie(url: String, cookie: String) {
        if (url.isBlank() || cookie.isBlank()) return
        val old = getCookie(url)
        if (old.isBlank()) { setCookie(url, cookie); return }
        val map = cookieToMap(old)
        map.putAll(cookieToMap(cookie))
        setCookie(url, mapToCookie(map))
    }

    fun getCookie(url: String): String {
        val d = NetworkUtils.getSubDomain(url)
        return if (d.isEmpty()) "" else cache.getAsString(d) ?: ""
    }

    fun removeCookie(url: String) {
        val d = NetworkUtils.getSubDomain(url)
        if (d.isNotEmpty()) cache.remove(d)
    }

    fun applySetCookie(url: String, headers: List<String>) {
        val pairs = headers.mapNotNull { it.substringBefore(';').trim().takeIf { p -> p.contains('=') } }
        if (pairs.isNotEmpty()) replaceCookie(url, pairs.joinToString("; "))
    }

    fun cookieToMap(cookie: String): MutableMap<String, String> {
        val map = linkedMapOf<String, String>()
        cookie.split(';').map { it.trim() }.filter { it.isNotEmpty() }.forEach { part ->
            val i = part.indexOf('=')
            if (i > 0) map[part.substring(0, i).trim()] = part.substring(i + 1).trim()
        }
        return map
    }

    fun mapToCookie(map: Map<String, String>) =
        map.entries.joinToString("; ") { "${it.key}=${it.value}" }
}
''')

w("io/legado/app/help/CacheManager.kt", r'''
package io.legado.app.help

import java.util.concurrent.ConcurrentHashMap

class CacheManager(private val userNameSpace: String) {
    private val map = store.computeIfAbsent(userNameSpace) { ConcurrentHashMap() }

    fun put(key: String, value: String, saveTime: Int = 0) {
        val exp = if (saveTime > 0) System.currentTimeMillis() + saveTime * 1000L else 0L
        map[key] = exp to value
    }

    fun get(key: String): String? {
        val p = map[key] ?: return null
        if (p.first > 0 && System.currentTimeMillis() > p.first) {
            map.remove(key); return null
        }
        return p.second
    }

    fun delete(key: String) { map.remove(key) }

    companion object {
        private val store = ConcurrentHashMap<String, ConcurrentHashMap<String, Pair<Long, String>>>()
    }
}
''')

w("io/legado/app/help/JsExtensions.kt", r'''
package io.legado.app.help

import io.legado.app.data.entities.BaseSource
import io.legado.app.help.http.StrResponse
import io.legado.app.model.DebugLog
import io.legado.app.utils.MD5Utils
import okhttp3.OkHttpClient
import okhttp3.Request
import java.util.Base64
import java.util.UUID
import java.util.concurrent.TimeUnit

interface JsExtensions {
    fun getSource(): BaseSource?
    fun getUserNameSpace(): String
    fun getLogger(): DebugLog? = null

    fun ajax(urlStr: String): String? = runCatching { connect(urlStr).body }.getOrNull()

    fun connect(urlStr: String): StrResponse {
        val client = OkHttpClient.Builder()
            .connectTimeout(15, TimeUnit.SECONDS)
            .readTimeout(30, TimeUnit.SECONDS)
            .build()
        client.newCall(Request.Builder().url(urlStr).get().build()).execute().use { resp ->
            return StrResponse(urlStr, resp.body?.string())
        }
    }

    fun base64Decode(str: String): String =
        String(Base64.getDecoder().decode(str.substringAfter(',')))

    fun base64Encode(str: String): String =
        Base64.getEncoder().encodeToString(str.toByteArray())

    fun md5Encode(str: String) = MD5Utils.md5Encode(str)
    fun md5Encode16(str: String) = MD5Utils.md5Encode16(str)
    fun randomUUID(): String = UUID.randomUUID().toString()
    fun log(msg: String): String {
        getLogger()?.log(getSource()?.toString(), msg); return msg
    }
}
''')

w("io/legado/app/model/DebugLog.kt", r'''
package io.legado.app.model

fun interface DebugLog {
    fun log(source: String?, msg: String?)
}

object ConsoleDebugLog : DebugLog {
    override fun log(source: String?, msg: String?) {
        println("[${source ?: "-"}] $msg")
    }
}
''')

w("io/legado/app/exception/TocEmptyException.kt", r'''
package io.legado.app.exception

class TocEmptyException(message: String = "目录为空") : RuntimeException(message)
''')

print("continuing engines...")
# Will continue in part 2 due to length - write remaining via second call in same script

# AnalyzeRule simplified but solid
w("io/legado/app/model/analyzeRule/RuleDataInterface.kt", r'''
package io.legado.app.model.analyzeRule

interface RuleDataInterface {
    fun getUserNameSpace(): String = "default"
    fun putVariable(key: String, value: String?) {}
    fun getVariable(key: String): String? = null
}
''')

w("io/legado/app/model/analyzeRule/AnalyzeByJSoup.kt", r'''
package io.legado.app.model.analyzeRule

import org.jsoup.Jsoup
import org.jsoup.nodes.Element
import org.jsoup.select.Elements

class AnalyzeByJSoup(content: Any?) {
    private val root: Element = when (content) {
        is Element -> content
        is String -> Jsoup.parse(content)
        else -> Jsoup.parse(content?.toString() ?: "")
    }

    fun getString(content: Any?, rule: String): String {
        val el = elementOf(content) ?: return ""
        val (css, attr) = splitAttr(rule)
        val selected = if (css.isEmpty()) el else el.selectFirst(css) ?: return ""
        return readAttr(selected, attr)
    }

    fun getStringList(content: Any?, rule: String): List<String> {
        val el = elementOf(content) ?: return emptyList()
        val (css, attr) = splitAttr(rule)
        val els: Elements = if (css.isEmpty()) Elements(el) else el.select(css)
        return els.map { readAttr(it, attr) }.filter { it.isNotEmpty() }
    }

    fun getElements(content: Any?, rule: String): List<Any> {
        val el = elementOf(content) ?: return emptyList()
        val (css, _) = splitAttr(rule)
        return if (css.isEmpty()) listOf(el) else el.select(css).toList()
    }

    private fun elementOf(content: Any?): Element? = when (content) {
        null -> root
        is Element -> content
        is String -> Jsoup.parse(content)
        else -> root
    }

    private fun splitAttr(rule: String): Pair<String, String> {
        val idx = rule.lastIndexOf('@')
        return if (idx > 0) rule.substring(0, idx) to rule.substring(idx + 1)
        else rule to "text"
    }

    private fun readAttr(el: Element, attr: String): String = when (attr.lowercase()) {
        "text", "textNodes" -> el.text()
        "html", "innerHtml" -> el.html()
        "outerHtml" -> el.outerHtml()
        "href", "src" -> el.attr(attr)
        else -> el.attr(attr).ifEmpty { el.text() }
    }
}
''')

w("io/legado/app/model/analyzeRule/AnalyzeByXPath.kt", r'''
package io.legado.app.model.analyzeRule

import org.jsoup.Jsoup
import org.jsoup.nodes.Element
import org.jsoup.select.Elements

/**
 * XPath subset via CSS fallback (full seimicrawler not required for compile/runtime smoke).
 * Supports //tag, //tag[@attr], text() lightly by mapping to CSS.
 */
class AnalyzeByXPath(content: Any?) {
    private val doc: Element = when (content) {
        is Element -> content
        is String -> Jsoup.parse(content)
        else -> Jsoup.parse(content?.toString() ?: "")
    }

    fun getString(content: Any?, rule: String): String =
        getStringList(content, rule).firstOrNull() ?: ""

    fun getStringList(content: Any?, rule: String): List<String> {
        val els = getElements(content, rule)
        return els.map {
            when (it) {
                is Element -> it.text()
                else -> it.toString()
            }
        }
    }

    fun getElements(content: Any?, rule: String): List<Any> {
        val root = when (content) {
            is Element -> content
            is String -> Jsoup.parse(content)
            null -> doc
            else -> doc
        }
        val css = xpathToCss(rule)
        return try {
            root.select(css).toList()
        } catch (_: Exception) {
            emptyList()
        }
    }

    fun getStringList(xPath: String): List<String> = getStringList(null, xPath)

    private fun xpathToCss(xp: String): String {
        var r = xp.trim()
        if (r.startsWith("//")) r = r.removePrefix("//")
        r = r.replace("//", " ")
        r = r.replace(Regex("""\[@(\w+)='([^']*)']"""), "[$1=$2]")
        r = r.replace(Regex("""/text\(\)"""), "")
        r = r.replace("/", " > ")
        return r.ifBlank { "*" }
    }
}
''')

w("io/legado/app/model/analyzeRule/AnalyzeByJSonPath.kt", r'''
package io.legado.app.model.analyzeRule

import com.jayway.jsonpath.JsonPath
import com.jayway.jsonpath.PathNotFoundException

class AnalyzeByJSonPath(content: Any?) {
    private val ctx = try {
        when (content) {
            is String -> JsonPath.parse(content)
            else -> JsonPath.parse(content?.toString() ?: "{}")
        }
    } catch (_: Exception) {
        JsonPath.parse("{}")
    }

    fun getString(content: Any?, rule: String): String {
        return try {
            val c = content?.let {
                if (it is String) JsonPath.parse(it) else ctx
            } ?: ctx
            val v = c.read<Any>(rule)
            when (v) {
                is List<*> -> v.firstOrNull()?.toString() ?: ""
                else -> v?.toString() ?: ""
            }
        } catch (_: PathNotFoundException) {
            ""
        } catch (_: Exception) {
            ""
        }
    }

    fun getStringList(content: Any?, rule: String): List<String> {
        return try {
            val c = content?.let {
                if (it is String) JsonPath.parse(it) else ctx
            } ?: ctx
            val v = c.read<Any>(rule)
            when (v) {
                is List<*> -> v.mapNotNull { it?.toString() }
                null -> emptyList()
                else -> listOf(v.toString())
            }
        } catch (_: Exception) {
            emptyList()
        }
    }

    fun getElements(content: Any?, rule: String): List<Any> =
        getStringList(content, rule)
}
''')

w("io/legado/app/model/analyzeRule/RuleAnalyzer.kt", r'''
package io.legado.app.model.analyzeRule

class RuleAnalyzer(private val rule: String) {
    var elementsType: String = "&&"

    fun splitRule(vararg separators: String): List<String> {
        for (sep in separators) {
            if (rule.contains(sep)) {
                elementsType = sep
                return rule.split(sep).map { it.trim() }.filter { it.isNotEmpty() }
            }
        }
        return listOf(rule)
    }
}
''')

w("io/legado/app/model/analyzeRule/AnalyzeRule.kt", r'''
package io.legado.app.model.analyzeRule

import io.legado.app.data.entities.BaseBook
import io.legado.app.data.entities.BaseSource
import io.legado.app.data.entities.Book
import io.legado.app.data.entities.BookChapter
import io.legado.app.help.CacheManager
import io.legado.app.help.JsExtensions
import io.legado.app.help.http.CookieStore
import io.legado.app.model.DebugLog
import io.legado.app.utils.NetworkUtils
import org.mozilla.javascript.Context
import org.mozilla.javascript.Scriptable
import java.util.regex.Pattern

class AnalyzeRule(
    var ruleData: RuleDataInterface? = null,
    private var source: BaseSource? = null,
    var debugLog: DebugLog? = null
) : JsExtensions {

    var content: Any? = null
        private set
    var baseUrl: String? = null
        private set
    var chapter: BookChapter? = null
    var nextChapterUrl: String? = null

    private var isJSON = false
    private var isRegex = false
    private var jsoup: AnalyzeByJSoup? = null
    private var xpath: AnalyzeByXPath? = null
    private var jsonp: AnalyzeByJSonPath? = null

    val book: BaseBook? get() = ruleData as? BaseBook

    constructor(book: Book?, source: BaseSource?, debugLog: DebugLog?) :
        this(book as? RuleDataInterface, source, debugLog)

    fun setContent(content: Any?, baseUrl: String? = null): AnalyzeRule {
        this.content = content
        if (baseUrl != null) this.baseUrl = baseUrl
        jsoup = null; xpath = null; jsonp = null
        isJSON = content is String && content.trimStart().let { it.startsWith("{") || it.startsWith("[") }
        return this
    }

    override fun getSource() = source
    override fun getUserNameSpace() = ruleData?.getUserNameSpace() ?: source?.getUserNameSpace() ?: "default"
    override fun getLogger() = debugLog

    fun getString(ruleStr: String?, mContent: Any? = null, isUrl: Boolean = false): String {
        if (ruleStr.isNullOrEmpty()) return ""
        var result: Any? = mContent ?: content
        for (rule in splitSourceRule(ruleStr)) {
            result = evalRule(rule, result)
            if (rule.replaceRegex.isNotEmpty() && result != null) {
                result = replaceRegex(result.toString(), rule)
            }
        }
        val s = result?.toString() ?: ""
        return if (isUrl) NetworkUtils.getAbsoluteURL(baseUrl, s) else s
    }

    fun getStringList(
        rule: String?,
        mContent: Any? = null,
        isUrl: Boolean = false,
        allInOne: Boolean = false
    ): List<String> {
        if (rule.isNullOrEmpty()) return emptyList()
        val rules = splitSourceRule(rule, allInOne)
        var result: Any? = mContent ?: content
        var list: List<String> = emptyList()
        for ((i, r) in rules.withIndex()) {
            if (i == rules.lastIndex) {
                list = when (r.mode) {
                    Mode.Js -> listOfNotNull(evalJS(r.rule, result)?.toString())
                    Mode.Regex -> listByRegex(result?.toString() ?: "", r)
                    Mode.Json -> jpath().getStringList(result, r.rule)
                    Mode.XPath -> xpath().getStringList(result, r.rule)
                    Mode.Default -> jSoup().getStringList(result, r.rule)
                }
            } else result = evalRule(r, result)
        }
        return if (isUrl) list.map { NetworkUtils.getAbsoluteURL(baseUrl, it) } else list
    }

    fun getElements(ruleStr: String, allInOne: Boolean = false): List<Any> {
        if (ruleStr.isEmpty()) return emptyList()
        val rules = splitSourceRule(ruleStr, allInOne)
        var result: Any? = content
        var elements: List<Any> = emptyList()
        for ((i, r) in rules.withIndex()) {
            if (i == rules.lastIndex) {
                elements = when (r.mode) {
                    Mode.Json -> jpath().getElements(result, r.rule)
                    Mode.XPath -> xpath().getElements(result, r.rule)
                    Mode.Default -> jSoup().getElements(result, r.rule)
                    Mode.Js -> listOfNotNull(evalJS(r.rule, result))
                    Mode.Regex -> listByRegex(result?.toString() ?: "", r)
                }
            } else result = evalRule(r, result)
        }
        return elements
    }

    private fun evalRule(r: SourceRule, result: Any?): Any? = when (r.mode) {
        Mode.Js -> evalJS(r.rule, result)
        Mode.Regex -> stringByRegex(result?.toString() ?: "", r)
        Mode.Json -> jpath().getString(result, r.rule)
        Mode.XPath -> xpath().getString(result, r.rule)
        Mode.Default -> jSoup().getString(result, r.rule)
    }

    fun splitSourceRule(ruleStr: String?, allInOne: Boolean = false): List<SourceRule> {
        if (ruleStr.isNullOrEmpty()) return emptyList()
        var mode = Mode.Default
        var start = 0
        if (allInOne && ruleStr.startsWith(":")) {
            mode = Mode.Regex; isRegex = true; start = 1
        } else if (isRegex) mode = Mode.Regex

        val jsPat = Pattern.compile("""<js>([\s\S]*?)</js>|@js[:：]([\s\S]*)""", Pattern.CASE_INSENSITIVE)
        val m = jsPat.matcher(ruleStr)
        val out = ArrayList<SourceRule>()
        var cursor = start
        var found = false
        while (m.find()) {
            found = true
            if (m.start() > cursor) {
                val chunk = ruleStr.substring(cursor, m.start()).trim()
                if (chunk.isNotEmpty()) out += SourceRule(chunk, mode)
            }
            out += SourceRule(m.group(1) ?: m.group(2) ?: "", Mode.Js)
            cursor = m.end()
        }
        if (found) {
            if (cursor < ruleStr.length) {
                val tail = ruleStr.substring(cursor).trim()
                if (tail.isNotEmpty()) out += SourceRule(tail, mode)
            }
            return out
        }
        val rest = ruleStr.substring(start).trim()
        if (rest.isNotEmpty()) out += SourceRule(rest, mode)
        return out
    }

    override fun evalJS(jsStr: String, result: Any?): Any? {
        val cx = Context.enter()
        try {
            cx.optimizationLevel = -1
            val scope: Scriptable = cx.initStandardObjects()
            scope.put("java", scope, this)
            scope.put("cookie", scope, CookieStore(getUserNameSpace()))
            scope.put("cache", scope, CacheManager(getUserNameSpace()))
            scope.put("source", scope, source)
            scope.put("book", scope, book)
            scope.put("result", scope, result)
            scope.put("baseUrl", scope, baseUrl)
            scope.put("chapter", scope, chapter)
            scope.put("title", scope, chapter?.title)
            scope.put("src", scope, content)
            scope.put("nextChapterUrl", scope, nextChapterUrl)
            return cx.evaluateString(scope, jsStr, "js", 1, null)
        } catch (e: Exception) {
            debugLog?.log(source?.toString(), "js error: ${e.message}")
            return null
        } finally {
            Context.exit()
        }
    }

    private fun stringByRegex(text: String, rule: SourceRule): String {
        var r = rule.rule
        if (r.startsWith(":")) r = r.substring(1)
        return try {
            val m = Pattern.compile(r, Pattern.MULTILINE).matcher(text)
            if (m.find()) {
                if (m.groupCount() >= 1) m.group(1) ?: m.group(0) ?: "" else m.group(0) ?: ""
            } else ""
        } catch (_: Exception) { "" }
    }

    private fun listByRegex(text: String, rule: SourceRule): List<String> {
        var r = rule.rule
        if (r.startsWith(":")) r = r.substring(1)
        val list = ArrayList<String>()
        try {
            val m = Pattern.compile(r, Pattern.MULTILINE).matcher(text)
            while (m.find()) {
                list += if (m.groupCount() >= 1) (m.group(1) ?: m.group(0) ?: "") else (m.group(0) ?: "")
            }
        } catch (_: Exception) {}
        return list
    }

    private fun replaceRegex(result: String, rule: SourceRule): String = try {
        val re = Regex(rule.replaceRegex)
        if (rule.replaceFirst) re.replaceFirst(result, rule.replacement) else re.replace(result, rule.replacement)
    } catch (_: Exception) {
        result.replace(rule.replaceRegex, rule.replacement)
    }

    private fun jSoup() = jsoup ?: AnalyzeByJSoup(content).also { jsoup = it }
    private fun xpath() = xpath ?: AnalyzeByXPath(content).also { xpath = it }
    private fun jpath() = jsonp ?: AnalyzeByJSonPath(content).also { jsonp = it }

    enum class Mode { XPath, Json, Default, Js, Regex }

    data class SourceRule(
        var rule: String,
        var mode: Mode,
        var replaceRegex: String = "",
        var replacement: String = "",
        var replaceFirst: Boolean = false
    ) {
        init {
            var r = rule
            if (mode != Mode.Js && mode != Mode.Regex) {
                when {
                    r.startsWith("@@") -> { mode = Mode.Default; r = r.removePrefix("@@") }
                    r.startsWith("@XPath:", true) -> { mode = Mode.XPath; r = r.substringAfter(':') }
                    r.startsWith("@Json:", true) -> { mode = Mode.Json; r = r.substringAfter(':') }
                    r.startsWith("$.") || r.startsWith("$[") -> mode = Mode.Json
                    r.startsWith("/") || r.startsWith("./") || r.startsWith("//") -> mode = Mode.XPath
                    r.startsWith(":") -> { mode = Mode.Regex; r = r.removePrefix(":") }
                }
            }
            if (r.contains("##")) {
                val idx = r.indexOf("##")
                val left = r.substring(0, idx)
                var right = r.substring(idx + 2)
                if (right.startsWith("#")) { replaceFirst = true; right = right.substring(1) }
                val idx2 = right.indexOf("##")
                if (idx2 >= 0) {
                    replaceRegex = right.substring(0, idx2)
                    replacement = right.substring(idx2 + 2)
                } else replaceRegex = right
                r = left
            }
            rule = r.trim()
        }
    }
}
''')

w("io/legado/app/model/analyzeRule/AnalyzeUrl.kt", r'''
package io.legado.app.model.analyzeRule

import io.legado.app.data.entities.BaseSource
import io.legado.app.data.entities.Book
import io.legado.app.data.entities.BookChapter
import io.legado.app.help.http.CookieStore
import io.legado.app.help.http.StrResponse
import io.legado.app.model.DebugLog
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody.Companion.toRequestBody
import okhttp3.Response
import java.util.concurrent.TimeUnit

class AnalyzeUrl(
    mUrl: String,
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
    var finalUrl: String = expand(mUrl)
    var body: String? = null
    var method: String = "GET"

    private fun expand(url: String): String {
        var u = url
        if (key != null) u = u.replace("{{key}}", key).replace("{{keyword}}", key)
        if (page != null) u = u.replace("{{page}}", page.toString())
        if (speakText != null) u = u.replace("{{speakText}}", speakText)
        return u
    }

    private fun client() = OkHttpClient.Builder()
        .connectTimeout(20, TimeUnit.SECONDS)
        .readTimeout(60, TimeUnit.SECONDS)
        .followRedirects(true)
        .build()

    private fun cookieStore(): CookieStore? {
        val ns = source?.getUserNameSpace() ?: ruleData?.userNameSpace ?: return null
        return CookieStore(ns)
    }

    private fun headers(): Map<String, String> {
        val map = linkedMapOf<String, String>()
        source?.getHeaderMap(true)?.let { map.putAll(it) }
        headerMapF?.let { map.putAll(it) }
        cookieStore()?.let { cs ->
            val c = cs.getCookie(finalUrl)
            if (c.isNotBlank() && map.keys.none { it.equals("Cookie", true) }) map["Cookie"] = c
        }
        return map
    }

    suspend fun getStrResponseAwait(): StrResponse {
        client().newCall(buildRequest()).execute().use { resp ->
            finalUrl = resp.request.url.toString()
            saveCookies(resp)
            return StrResponse(finalUrl, resp.body?.string())
        }
    }

    suspend fun getByteArrayAwait(): ByteArray {
        client().newCall(buildRequest()).execute().use { resp ->
            finalUrl = resp.request.url.toString()
            saveCookies(resp)
            return resp.body?.bytes() ?: ByteArray(0)
        }
    }

    suspend fun getResponseAwait(): Response {
        val resp = client().newCall(buildRequest()).execute()
        finalUrl = resp.request.url.toString()
        saveCookies(resp)
        return resp
    }

    private fun saveCookies(resp: Response) {
        val cs = cookieStore() ?: return
        val set = resp.headers("Set-Cookie")
        if (set.isNotEmpty()) cs.applySetCookie(finalUrl, set)
    }

    private fun buildRequest(): Request {
        val b = Request.Builder().url(finalUrl)
        headers().forEach { (k, v) -> b.header(k, v) }
        return if (method.equals("POST", true)) {
            val media = "application/json; charset=utf-8".toMediaTypeOrNull()
            b.post((body ?: "").toRequestBody(media)).build()
        } else b.get().build()
    }

    fun evalJS(js: String, result: Any?): Any? =
        AnalyzeRule(ruleData as? RuleDataInterface, source, debugLog).evalJS(js, result)
}
''')

print("part1 complete")
print("files so far", sum(1 for _ in SRC.rglob("*.kt")))
