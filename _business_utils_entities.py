# -*- coding: utf-8 -*-
from pathlib import Path
import os
BIZ = Path(r"C:\Users\chong\reader-pro-3.2.14-reverse\best-of-3\business")
H = "/** Business rewrite from reader-pro-3.2.14.jar — readability / audit. */\n\n"

def w(rel, c):
    p = BIZ / rel.replace("/", os.sep)
    p.parent.mkdir(parents=True, exist_ok=True)
    p.write_text(H + c.lstrip("\n"), encoding="utf-8", newline="\n")
    print(rel)

w("com/htmake/reader/entity/User.kt", r'''
package com.htmake.reader.entity

data class User(
    var username: String = "",
    var password: String = "",
    var salt: String = "",
    var token: String? = null,
    var lastLoginAt: Long = 0,
    var createdAt: Long = 0,
    var isManager: Boolean = false,
    var enableWebdav: Boolean = true,
    var enableLocalStore: Boolean = true,
    var enableBookSource: Boolean = true,
    var enableRssSource: Boolean = true,
    var bookSourceLimit: Int = 100,
    var bookLimit: Int = 200
)
''')

w("com/htmake/reader/entity/License.kt", r'''
package com.htmake.reader.entity

data class License(
    var host: String? = null,
    var userMax: Int = 0,
    var expireAt: Long = 0,
    var payload: String? = null,
    var signature: String? = null
)

data class ActiveLicense(
    var licenseId: String? = null,
    var activatedAt: Long = 0,
    var host: String? = null
)
''')

w("com/htmake/reader/config/AppConfig.kt", r'''
package com.htmake.reader.config

import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.stereotype.Component

@Component
@ConfigurationProperties(prefix = "reader.app")
class AppConfig {
    var workDir: String = "."
    var showUI: Boolean = false
    var debug: Boolean = false
    var packaged: Boolean = false
    var secure: Boolean = false
    var inviteCode: String = ""
    var secureKey: String = ""
    var proxy: Boolean = false
    var proxyType: String = "HTTP"
    var proxyHost: String = ""
    var proxyPort: String = ""
    var cacheChapterContent: Boolean = true
    var userLimit: Int = 15
    var userBookLimit: Int = 200
    var debugLog: Boolean = false
    var autoClearInactiveUser: Int = 0
    var mongoUri: String = ""
    var mongoDbName: String = "reader"
    var shelfUpdateInteval: Int = 30
    var remoteWebviewApi: String = ""
    var defaultUserEnableWebdav: Boolean = true
    var defaultUserEnableLocalStore: Boolean = true
    var defaultUserEnableBookSource: Boolean = true
    var defaultUserEnableRssSource: Boolean = true
    var defaultUserBookSourceLimit: Int = 100
    var defaultUserBookLimit: Int = 200
    var autoBackupUserData: Boolean = false
    var minUserPasswordLength: Int = 8
    var remoteBookSourceUpdateInterval: Int = 720
}
''')

w("com/htmake/reader/utils/ExtKt.kt", r'''
package com.htmake.reader.utils

import io.vertx.core.json.Json
import io.vertx.core.json.JsonArray
import io.vertx.core.json.JsonObject
import java.io.File
import java.security.MessageDigest
import javax.crypto.SecretKeyFactory
import javax.crypto.spec.PBEKeySpec

/**
 * Storage / crypto / path helpers used across controllers.
 */
object ExtKt {
    @JvmStatic
    var workDir: String = System.getProperty("reader.workDir", ".")

    @JvmStatic
    fun getWorkDir(vararg parts: String): String {
        return parts.fold(File(workDir)) { acc, p -> File(acc, p) }.absolutePath
    }

    @JvmStatic
    fun getWorkDir(): String = File(workDir).absolutePath

    @JvmStatic
    fun getStorage(vararg path: String): String? {
        val f = File(getWorkDir("storage", *path).let {
            // allow "data/x" style via multiple args
            File(getWorkDir(), listOf("storage", *path).joinToString(File.separator))
        })
        // simpler:
        val file = path.fold(File(getWorkDir(), "storage")) { a, p -> File(a, p) }
        val json = File(file.path + ".json")
        val plain = file
        return when {
            json.isFile -> json.readText()
            plain.isFile -> plain.readText()
            else -> null
        }
    }

    @JvmStatic
    fun saveStorage(path: Array<String>, content: String, pretty: Boolean = false, unused: Any? = null) {
        val base = path.fold(File(getWorkDir(), "storage")) { a, p -> File(a, p) }
        val f = if (base.extension.isEmpty()) File(base.path + ".json") else base
        f.parentFile?.mkdirs()
        f.writeText(content)
    }

    @JvmStatic fun asJsonObject(raw: String?): JsonObject? =
        raw?.let { try { JsonObject(it) } catch (_: Exception) { null } }

    @JvmStatic fun asJsonArray(raw: String?): JsonArray? =
        raw?.let { try { JsonArray(it) } catch (_: Exception) { null } }

    @JvmStatic fun jsonEncode(obj: Any?, pretty: Boolean = false): String = Json.encode(obj)

    @JvmStatic fun getRelativePath(vararg parts: String): String = parts.joinToString("/")

    @JvmStatic
    fun genEncryptedPassword(password: String, salt: String): String {
        // approximate: jar uses salted hash (see hutool / custom in ExtKt CFR)
        val md = MessageDigest.getInstance("SHA-256")
        return md.digest((password + salt).toByteArray()).joinToString("") { "%02x".format(it) }
    }

    @JvmStatic
    fun deleteRecursively(f: File?) {
        if (f == null || !f.exists()) return
        if (f.isDirectory) f.listFiles()?.forEach { deleteRecursively(it) }
        f.delete()
    }
}
''')

w("com/htmake/reader/utils/VertExtKt.kt", r'''
package com.htmake.reader.utils

import com.htmake.reader.api.ReturnData
import io.vertx.core.json.Json
import io.vertx.ext.web.RoutingContext

object VertExtKt {
    @JvmStatic
    fun success(ctx: RoutingContext, data: ReturnData) {
        if (!ctx.response().ended()) {
            ctx.response()
                .putHeader("Content-Type", "application/json; charset=utf-8")
                .end(Json.encode(data))
        }
    }

    @JvmStatic
    fun success(ctx: RoutingContext, data: Any?) {
        if (data is ReturnData) success(ctx, data)
        else if (!ctx.response().ended()) {
            ctx.response().putHeader("Content-Type", "application/json; charset=utf-8")
                .end(Json.encode(data))
        }
    }
}
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
        private lateinit var ctx: ApplicationContext
        @JvmStatic fun <T> getBean(clazz: Class<T>): T = ctx.getBean(clazz)
        @JvmStatic fun <T> getBean(name: String, clazz: Class<T>): T = ctx.getBean(name, clazz)
    }
}
''')

w("com/htmake/reader/utils/UserMutex.kt", r'''
package com.htmake.reader.utils

import kotlinx.coroutines.sync.Mutex
import java.util.concurrent.ConcurrentHashMap

object UserMutex {
    private val map = ConcurrentHashMap<String, Mutex>()
    fun getLocker(key: String): Mutex = map.getOrPut(key) { Mutex() }
}
''')

w("io/legado/app/data/entities/Book.kt", r'''
package io.legado.app.data.entities

/**
 * Simplified Book entity for business reading — full fields in decompiled Book.java.
 */
data class Book(
    var bookUrl: String = "",
    var tocUrl: String = "",
    var origin: String = "",
    var originName: String = "",
    var name: String = "",
    var author: String = "",
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
    var variable: String? = null
) {
    val displayCover: String? get() = coverUrl
    val isLocalBook: Boolean get() = origin == "loc_book" || bookUrl.startsWith("file:")
    val isEpub: Boolean get() = bookUrl.endsWith(".epub", true) || name.endsWith(".epub", true)
    val isCbz: Boolean get() = bookUrl.endsWith(".cbz", true)
    val isPdf: Boolean get() = bookUrl.endsWith(".pdf", true)
}
''')

w("io/legado/app/data/entities/BookChapter.kt", r'''
package io.legado.app.data.entities

data class BookChapter(
    var url: String = "",
    var title: String = "",
    var index: Int = 0,
    var bookUrl: String = "",
    var resourceUrl: String? = null,
    var tag: String? = null
)
''')

w("io/legado/app/data/entities/SearchBook.kt", r'''
package io.legado.app.data.entities

data class SearchBook(
    var name: String = "",
    var author: String = "",
    var bookUrl: String = "",
    var origin: String = "",
    var coverUrl: String? = null,
    var intro: String? = null,
    var kind: String? = null
) {
    fun toBook(): Book = Book(
        bookUrl = bookUrl, name = name, author = author,
        origin = origin, coverUrl = coverUrl, intro = intro, kind = kind
    )
}
''')

w("io/legado/app/data/entities/BookSource.kt", r'''
package io.legado.app.data.entities

import io.legado.app.data.entities.rule.*

/**
 * Book source — rules for search/explore/info/toc/content.
 * Full JSON schema matches阅读/legado; see decompiled BookSource.java for all fields.
 */
data class BookSource(
    var bookSourceUrl: String = "",
    var bookSourceName: String = "",
    var bookSourceGroup: String? = null,
    var bookSourceType: Int = 0,
    var enabled: Boolean = true,
    var header: String? = null,
    var loginUrl: String? = null,
    var ruleSearch: SearchRule? = null,
    var ruleExplore: ExploreRule? = null,
    var ruleBookInfo: BookInfoRule? = null,
    var ruleToc: TocRule? = null,
    var ruleContent: ContentRule? = null
) : BaseSource {
    fun getHeaderMap(withLogin: Boolean = false): Map<String, String> = emptyMap()

    companion object {
        fun fromJson(json: String): Result<BookSource> = runCatching {
            // Use jackson/gson in real wiring — placeholder decode
            BookSource(bookSourceUrl = json.take(32))
        }
    }
}

interface BaseSource {
    fun getHeaderMap(withLogin: Boolean = false): Map<String, String>
}
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
    var kind: String? = null
)

data class ExploreRule(
    var bookList: String? = null,
    var name: String? = null,
    var author: String? = null,
    var bookUrl: String? = null,
    var coverUrl: String? = null
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

w("io/legado/app/data/entities/HttpTTS.kt", r'''
package io.legado.app.data.entities

data class HttpTTS(
    var url: String = "",
    var contentType: String? = null,
    var loginCheckJs: String? = null,
    var header: String? = null
) : BaseSource {
    override fun getHeaderMap(withLogin: Boolean): Map<String, String> = emptyMap()
}
''')

w("io/legado/app/exception/TocEmptyException.kt", r'''
package io.legado.app.exception
class TocEmptyException(msg: String) : Exception(msg)
class ContentEmptyException(msg: String) : Exception(msg)
class NoStackTraceException(msg: String) : Exception(msg)
''')

w("io/legado/app/model/DebugLog.kt", r'''
package io.legado.app.model
interface DebugLog {
    fun log(sourceUrl: String?, msg: String?)
    object DefaultImpls {
        @JvmStatic fun log(log: DebugLog, sourceUrl: String?, msg: String?) {}
    }
}
object Debug : DebugLog {
    override fun log(sourceUrl: String?, msg: String?) {}
}
''')

w("io/legado/app/help/BookHelp.kt", r'''
package io.legado.app.help

import io.legado.app.data.entities.Book
import io.legado.app.data.entities.BookChapter
import io.legado.app.data.entities.BookSource
import io.legado.app.model.analyzeRule.AnalyzeUrl
import io.legado.app.utils.FileUtils
import io.legado.app.utils.MD5Utils
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.delay
import java.io.File
import java.util.concurrent.ConcurrentHashMap

object BookHelp {
    private val downloadImages = ConcurrentHashMap.newKeySet<String>()

    fun getBookCacheDir(book: Book): File =
        File(book.rootDir ?: ".", "cache", book.name + "_" + book.author)

    fun getImage(book: Book, src: String): File {
        val name = MD5Utils.md5Encode16(src) + "." + getImageSuffix(src)
        return File(getBookCacheDir(book), "images/$name")
    }

    fun getImageSuffix(src: String): String =
        src.substringAfterLast('.').substringBefore('?').takeIf { it.length in 1..5 } ?: "jpg"

    suspend fun saveImage(bookSource: BookSource?, book: Book, src: String) {
        while (src in downloadImages) delay(100)
        if (getImage(book, src).exists()) return
        downloadImages.add(src)
        try {
            val bytes = AnalyzeUrl(mUrl = src, source = bookSource).getByteArrayAwait()
            val f = FileUtils.createFileIfNotExist(getBookCacheDir(book), "images", MD5Utils.md5Encode16(src) + "." + getImageSuffix(src))
            f.writeBytes(bytes)
        } catch (e: Exception) {
            e.printStackTrace()
        } finally {
            downloadImages.remove(src)
        }
    }

    suspend fun saveImages(
        scope: CoroutineScope,
        bookSource: BookSource,
        book: Book,
        chapter: BookChapter,
        content: String
    ) {
        // extract <img src> and saveImage each — see decompiled BookHelp
        val regex = Regex("""<img[^>]+src=["']([^"']+)["']""", RegexOption.IGNORE_CASE)
        regex.findAll(content).forEach { m ->
            saveImage(bookSource, book, m.groupValues[1])
        }
    }
}
''')

w("io/legado/app/model/localBook/LocalBook.kt", r'''
package io.legado.app.model.localBook

import io.legado.app.data.entities.Book
import io.legado.app.data.entities.BookChapter

object LocalBook {
    fun getChapterList(book: Book): List<BookChapter> {
        // epub/txt/cbz/pdf — see decompiled localBook/*
        return emptyList()
    }
    fun getContent(book: Book, chapter: BookChapter): String? = null
    fun analyzeNameAuthor(fileName: String): Pair<String, String> {
        val base = fileName.substringBeforeLast('.')
        val parts = base.split(Regex("[-_]"), limit = 2)
        return if (parts.size == 2) parts[0] to parts[1] else base to ""
    }
}
''')

w("io/legado/app/utils/MD5Utils.kt", r'''
package io.legado.app.utils
import java.security.MessageDigest
object MD5Utils {
    fun md5Encode(str: String): String {
        val d = MessageDigest.getInstance("MD5").digest(str.toByteArray())
        return d.joinToString("") { "%02x".format(it) }
    }
    fun md5Encode16(str: String): String = md5Encode(str).substring(8, 24)
}
''')

w("io/legado/app/utils/FileUtils.kt", r'''
package io.legado.app.utils
import java.io.File
object FileUtils {
    fun createFileIfNotExist(dir: File, vararg names: String): File {
        val f = names.fold(dir) { a, n -> File(a, n) }
        f.parentFile?.mkdirs()
        if (!f.exists()) f.createNewFile()
        return f
    }
    fun writeBytes(path: String, bytes: ByteArray) {
        File(path).apply { parentFile?.mkdirs() }.writeBytes(bytes)
    }
}
''')

w("io/legado/app/utils/ZipUtils.kt", r'''
package io.legado.app.utils
import java.io.File
import java.util.zip.ZipFile
object ZipUtils {
    fun unzipFile(zip: File, dest: File) {
        dest.mkdirs()
        ZipFile(zip).use { zf ->
            zf.stream().forEach { e ->
                val out = File(dest, e.name)
                if (e.isDirectory) out.mkdirs()
                else {
                    out.parentFile?.mkdirs()
                    zf.getInputStream(e).use { input -> out.outputStream().use { input.copyTo(it) } }
                }
            }
        }
    }
}
''')

w("io/legado/app/utils/ACache.kt", r'''
package io.legado.app.utils
import java.io.File
class ACache private constructor(private dir: File) {
    fun getAsString(key: String): String? = File(dir, key.hashCode().toString()).takeIf { it.isFile }?.readText()
    fun put(key: String, value: String, saveTimeSec: Int = 0) {
        dir.mkdirs()
        File(dir, key.hashCode().toString()).writeText(value)
    }
    companion object {
        fun get(dir: File): ACache = ACache(dir)
    }
}
''')

w("io/legado/app/help/http/StrResponse.kt", r'''
package io.legado.app.help.http
data class StrResponse(val url: String, val body: String?)
''')

print("utils+entities done")
# recount
n=sum(1 for _ in BIZ.rglob('*.kt'))
print("total kt", n)
