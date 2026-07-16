# -*- coding: utf-8 -*-
"""Part 2: webBook, localBook, help, controllers, YueduApi."""
from pathlib import Path
import os

SRC = Path(r"C:\Users\chong\reader-pro-3.2.14-reverse\src\main\kotlin")
RES = Path(r"C:\Users\chong\reader-pro-3.2.14-reverse\src\main\resources")


def w(rel, c):
    p = SRC / rel.replace("/", os.sep)
    p.parent.mkdir(parents=True, exist_ok=True)
    p.write_text(c.lstrip("\n"), encoding="utf-8", newline="\n")
    print("wrote", rel)


w("io/legado/app/help/SourceAnalyzer.kt", r'''
package io.legado.app.help

import com.google.gson.Gson
import com.google.gson.JsonObject
import com.google.gson.JsonParser
import io.legado.app.data.entities.BookSource
import io.legado.app.data.entities.rule.*

object SourceAnalyzer {
    private val gson = Gson()

    fun jsonToBookSource(json: String): Result<BookSource> = runCatching {
        val o = JsonParser.parseString(json).asJsonObject
        normalize(o)
    }

    fun jsonToBookSources(json: String): Result<List<BookSource>> = runCatching {
        val el = JsonParser.parseString(json)
        when {
            el.isJsonArray -> el.asJsonArray.map { normalize(it.asJsonObject) }
            el.isJsonObject -> {
                val o = el.asJsonObject
                when {
                    o.has("data") && o.get("data").isJsonArray ->
                        o.getAsJsonArray("data").map { normalize(it.asJsonObject) }
                    else -> listOf(normalize(o))
                }
            }
            else -> emptyList()
        }
    }

    private fun normalize(o: JsonObject): BookSource {
        fun str(vararg keys: String): String? {
            for (k in keys) if (o.has(k) && !o.get(k).isJsonNull) return o.get(k).asString
            return null
        }
        val src = BookSource(
            bookSourceUrl = str("bookSourceUrl", "url") ?: "",
            bookSourceName = str("bookSourceName", "name") ?: "",
            bookSourceGroup = str("bookSourceGroup", "group"),
            bookSourceType = o.get("bookSourceType")?.takeIf { it.isJsonPrimitive }?.asInt ?: 0,
            enabled = o.get("enabled")?.takeIf { it.isJsonPrimitive }?.asBoolean ?: true,
            enabledExplore = o.get("enabledExplore")?.takeIf { it.isJsonPrimitive }?.asBoolean ?: true,
            header = str("header"),
            loginUrl = str("loginUrl"),
            loginUi = str("loginUi"),
            loginCheckJs = str("loginCheckJs"),
            exploreUrl = str("exploreUrl"),
            bookUrlPattern = str("bookUrlPattern")
        )
        o.get("ruleSearch")?.takeIf { it.isJsonObject }?.asJsonObject?.let {
            src.ruleSearch = gson.fromJson(it, SearchRule::class.java)
        }
        o.get("searchUrl")?.let { u ->
            if (src.ruleSearch == null) src.ruleSearch = SearchRule()
            src.ruleSearch?.url = u.asString
        }
        o.get("ruleExplore")?.takeIf { it.isJsonObject }?.asJsonObject?.let {
            src.ruleExplore = gson.fromJson(it, ExploreRule::class.java)
        }
        o.get("ruleBookInfo")?.takeIf { it.isJsonObject }?.asJsonObject?.let {
            src.ruleBookInfo = gson.fromJson(it, BookInfoRule::class.java)
        }
        o.get("ruleToc")?.takeIf { it.isJsonObject }?.asJsonObject?.let {
            src.ruleToc = gson.fromJson(it, TocRule::class.java)
        }
        o.get("ruleContent")?.takeIf { it.isJsonObject }?.asJsonObject?.let {
            src.ruleContent = gson.fromJson(it, ContentRule::class.java)
        }
        // legacy flat fields
        migrateLegacy(o, src)
        return src
    }

    private fun migrateLegacy(o: JsonObject, src: BookSource) {
        fun s(k: String) = o.get(k)?.takeIf { it.isJsonPrimitive }?.asString
        if (src.ruleSearch == null && (s("ruleSearchUrl") != null || s("ruleSearchList") != null)) {
            src.ruleSearch = SearchRule(
                url = s("ruleSearchUrl") ?: s("searchUrl"),
                bookList = s("ruleSearchList"),
                name = s("ruleSearchName"),
                author = s("ruleSearchAuthor"),
                bookUrl = s("ruleSearchNoteUrl") ?: s("ruleSearchBookUrl"),
                coverUrl = s("ruleSearchCoverUrl"),
                intro = s("ruleSearchIntroduce"),
                kind = s("ruleSearchKind")
            )
        }
        if (src.ruleToc == null && s("ruleChapterList") != null) {
            src.ruleToc = TocRule(
                chapterList = s("ruleChapterList"),
                chapterName = s("ruleChapterName"),
                chapterUrl = s("ruleContentUrl") ?: s("ruleChapterUrl"),
                nextTocUrl = s("ruleChapterListNext")
            )
        }
        if (src.ruleContent == null && s("ruleBookContent") != null) {
            src.ruleContent = ContentRule(content = s("ruleBookContent"), nextContentUrl = s("ruleContentUrlNext"))
        }
        if (src.ruleBookInfo == null && (s("ruleBookName") != null || s("ruleBookAuthor") != null)) {
            src.ruleBookInfo = BookInfoRule(
                name = s("ruleBookName"),
                author = s("ruleBookAuthor"),
                intro = s("ruleIntroduce"),
                coverUrl = s("ruleCoverUrl"),
                tocUrl = s("ruleChapterUrl")
            )
        }
    }

    /** Old rule migration helpers */
    fun toNewRule(old: String): String {
        var r = old
        if (r.startsWith("-")) r = r.removePrefix("-")
        if (r.startsWith("+")) r = r.removePrefix("+")
        r = r.replace("#", "##").replace("|", "||").replace("&", "&&")
        return r
    }
}
''')

w("io/legado/app/help/ContentProcessor.kt", r'''
package io.legado.app.help

import com.google.gson.JsonParser
import com.htmake.reader.utils.ExtKt
import io.legado.app.data.entities.Book
import java.util.concurrent.Callable
import java.util.concurrent.Executors
import java.util.concurrent.TimeUnit
import java.util.concurrent.TimeoutException

object ContentProcessor {
    data class ReplaceRule(
        val name: String = "",
        val pattern: String = "",
        val replacement: String = "",
        val isRegex: Boolean = true,
        val isEnabled: Boolean = true,
        val scope: String = "content",
        val timeoutMs: Long = 3000,
        val bookName: String = ""
    )

    private val pool = Executors.newCachedThreadPool()

    fun loadRules(userNameSpace: String): List<ReplaceRule> {
        val raw = ExtKt.getStorage("data", userNameSpace, "replaceRule") ?: return emptyList()
        return try {
            JsonParser.parseString(raw).asJsonArray.mapNotNull { el ->
                val o = el.asJsonObject
                ReplaceRule(
                    name = o.get("name")?.asString ?: "",
                    pattern = o.get("pattern")?.asString ?: o.get("regex")?.asString ?: return@mapNotNull null,
                    replacement = o.get("replacement")?.asString ?: o.get("replace")?.asString ?: "",
                    isRegex = o.get("isRegex")?.asBoolean ?: true,
                    isEnabled = o.get("isEnabled")?.asBoolean ?: o.get("enable")?.asBoolean ?: true,
                    scope = o.get("scope")?.asString ?: "content",
                    timeoutMs = o.get("timeout")?.asLong ?: o.get("timeoutMillisecond")?.asLong ?: 3000L,
                    bookName = o.get("bookName")?.asString ?: ""
                )
            }
        } catch (_: Exception) {
            emptyList()
        }
    }

    fun applyContent(ns: String, book: Book?, content: String): String {
        var text = content
        loadRules(ns).filter {
            it.isEnabled && (it.scope == "content" || it.scope == "all" || it.scope.isBlank()) && matches(it, book)
        }.forEach { text = applyOne(text, it) }
        return text
    }

    fun applyTitle(ns: String, book: Book?, title: String): String {
        var text = title
        loadRules(ns).filter {
            it.isEnabled && (it.scope == "title" || it.scope == "all") && matches(it, book)
        }.forEach { text = applyOne(text, it) }
        return text
    }

    private fun matches(rule: ReplaceRule, book: Book?): Boolean {
        if (rule.bookName.isBlank()) return true
        val name = book?.name ?: return true
        return name.contains(rule.bookName)
    }

    private fun applyOne(text: String, r: ReplaceRule): String {
        val task = Callable {
            if (r.isRegex) text.replace(Regex(r.pattern), r.replacement)
            else text.replace(r.pattern, r.replacement)
        }
        val f = pool.submit(task)
        return try {
            f.get(r.timeoutMs.coerceAtLeast(100), TimeUnit.MILLISECONDS)
        } catch (_: TimeoutException) {
            f.cancel(true); text
        } catch (_: Exception) {
            text
        }
    }
}
''')

w("io/legado/app/help/DefaultData.kt", r'''
package io.legado.app.help

import io.legado.app.data.entities.TxtTocRule

object DefaultData {
    val txtTocRules: List<TxtTocRule> = listOf(
        TxtTocRule("中文章节", """^(第[0-9零一二三四五六七八九十百千]+[章节回部集卷].*)$"""),
        TxtTocRule("Chapter", """^(Chapter\s+\d+.*)$""", enable = true),
        TxtTocRule("数字点", """^(\d+\.\s*.{2,40})$""")
    )
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
import java.io.File

object BookHelp {
    fun getBookCacheDir(book: Book): File =
        File(book.rootDir ?: ".", "cache", "${book.name}_${book.author}")

    fun getImage(book: Book, src: String): File {
        val name = MD5Utils.md5Encode16(src) + ".jpg"
        return File(getBookCacheDir(book), "images/$name")
    }

    suspend fun saveImage(bookSource: BookSource?, book: Book, src: String) {
        if (getImage(book, src).exists()) return
        try {
            val bytes = AnalyzeUrl(mUrl = src, source = bookSource).getByteArrayAwait()
            val f = FileUtils.createFileIfNotExist(getBookCacheDir(book), "images", MD5Utils.md5Encode16(src) + ".jpg")
            f.writeBytes(bytes)
        } catch (_: Exception) {
        }
    }

    suspend fun saveImages(
        scope: CoroutineScope,
        bookSource: BookSource,
        book: Book,
        chapter: BookChapter,
        content: String
    ) {
        val regex = Regex("""<img[^>]+src=["']([^"']+)["']""", RegexOption.IGNORE_CASE)
        regex.findAll(content).forEach { m -> saveImage(bookSource, book, m.groupValues[1]) }
    }
}
''')

w("io/legado/app/model/webBook/BookList.kt", r'''
package io.legado.app.model.webBook

import io.legado.app.data.entities.BookSource
import io.legado.app.data.entities.SearchBook
import io.legado.app.data.entities.rule.SearchRule
import io.legado.app.model.DebugLog
import io.legado.app.model.analyzeRule.AnalyzeRule
import io.legado.app.model.analyzeRule.AnalyzeUrl

object BookList {
    suspend fun searchBook(
        bookSource: BookSource,
        key: String,
        page: Int = 1,
        debugLog: DebugLog? = null
    ): List<SearchBook> {
        val rule = bookSource.ruleSearch ?: return emptyList()
        val urlRule = rule.url ?: return emptyList()
        val analyzeUrl = AnalyzeUrl(mUrl = urlRule, key = key, page = page, source = bookSource, debugLog = debugLog)
        val html = analyzeUrl.getStrResponseAwait().body ?: return emptyList()
        return parseList(bookSource, html, analyzeUrl.finalUrl, rule, debugLog)
    }

    suspend fun exploreBook(
        bookSource: BookSource,
        url: String,
        page: Int = 1,
        debugLog: DebugLog? = null
    ): List<SearchBook> {
        val analyzeUrl = AnalyzeUrl(mUrl = url, page = page, source = bookSource, debugLog = debugLog)
        val html = analyzeUrl.getStrResponseAwait().body ?: return emptyList()
        val explore = bookSource.ruleExplore
        val search = bookSource.ruleSearch
        val synthetic = SearchRule(
            bookList = explore?.bookList ?: search?.bookList,
            name = explore?.name ?: search?.name,
            author = explore?.author ?: search?.author,
            bookUrl = explore?.bookUrl ?: search?.bookUrl,
            coverUrl = explore?.coverUrl ?: search?.coverUrl,
            intro = explore?.intro ?: search?.intro,
            kind = explore?.kind ?: search?.kind,
            lastChapter = explore?.lastChapter ?: search?.lastChapter
        )
        return parseList(bookSource, html, analyzeUrl.finalUrl, synthetic, debugLog)
    }

    fun parseExploreUrl(exploreUrl: String?, page: Int = 1): List<Pair<String, String>> {
        if (exploreUrl.isNullOrBlank()) return emptyList()
        val text = exploreUrl.replace("{{page}}", page.toString())
        return text.lines().mapNotNull { line ->
            val t = line.trim()
            if (t.isEmpty() || t.startsWith("//")) return@mapNotNull null
            val parts = t.split("::", limit = 2)
            if (parts.size == 2) parts[0].trim() to parts[1].trim() else null
        }
    }

    private fun parseList(
        bookSource: BookSource,
        html: String,
        baseUrl: String,
        rule: SearchRule,
        debugLog: DebugLog?
    ): List<SearchBook> {
        val bookListRule = rule.bookList ?: return emptyList()
        val analyze = AnalyzeRule(null, bookSource, debugLog)
        analyze.setContent(html, baseUrl)
        val els = analyze.getElements(bookListRule)
        return els.mapNotNull { el ->
            analyze.setContent(el, baseUrl)
            val name = rule.name?.let { analyze.getString(it) }?.takeIf { it.isNotBlank() } ?: return@mapNotNull null
            SearchBook(
                name = name,
                author = rule.author?.let { analyze.getString(it) } ?: "",
                bookUrl = rule.bookUrl?.let { analyze.getString(it, isUrl = true) }?.ifBlank { baseUrl } ?: baseUrl,
                origin = bookSource.bookSourceUrl,
                originName = bookSource.bookSourceName,
                coverUrl = rule.coverUrl?.let { analyze.getString(it, isUrl = true) },
                intro = rule.intro?.let { analyze.getString(it) },
                kind = rule.kind?.let { analyze.getString(it) },
                latestChapterTitle = rule.lastChapter?.let { analyze.getString(it) }
            )
        }
    }
}
''')

w("io/legado/app/model/webBook/BookInfo.kt", r'''
package io.legado.app.model.webBook

import io.legado.app.data.entities.Book
import io.legado.app.data.entities.BookSource
import io.legado.app.model.DebugLog
import io.legado.app.model.analyzeRule.AnalyzeRule
import io.legado.app.model.analyzeRule.AnalyzeUrl

object BookInfo {
    suspend fun getBookInfo(
        bookSource: BookSource,
        bookUrl: String,
        debugLog: DebugLog? = null
    ): Book {
        val analyzeUrl = AnalyzeUrl(mUrl = bookUrl, source = bookSource, debugLog = debugLog)
        val html = analyzeUrl.getStrResponseAwait().body ?: ""
        val book = Book(bookUrl = bookUrl, origin = bookSource.bookSourceUrl, originName = bookSource.bookSourceName)
        val rule = bookSource.ruleBookInfo ?: return book
        val analyze = AnalyzeRule(book, bookSource, debugLog)
        analyze.setContent(html, analyzeUrl.finalUrl)
        rule.name?.let { book.name = analyze.getString(it) }
        rule.author?.let { book.author = analyze.getString(it) }
        rule.kind?.let { book.kind = analyze.getString(it) }
        rule.coverUrl?.let { book.coverUrl = analyze.getString(it, isUrl = true) }
        rule.intro?.let { book.intro = analyze.getString(it) }
        rule.tocUrl?.let { book.tocUrl = analyze.getString(it, isUrl = true) }
        if (book.tocUrl.isBlank()) book.tocUrl = bookUrl
        return book
    }
}
''')

w("io/legado/app/model/webBook/BookChapterList.kt", r'''
package io.legado.app.model.webBook

import io.legado.app.data.entities.Book
import io.legado.app.data.entities.BookChapter
import io.legado.app.data.entities.BookSource
import io.legado.app.exception.TocEmptyException
import io.legado.app.model.DebugLog
import io.legado.app.model.analyzeRule.AnalyzeRule
import io.legado.app.model.analyzeRule.AnalyzeUrl

object BookChapterList {
    suspend fun analyzeChapterList(
        book: Book,
        body: String?,
        bookSource: BookSource,
        baseUrl: String,
        redirectUrl: String,
        debugLog: DebugLog? = null
    ): List<BookChapter> {
        val tocRule = bookSource.ruleToc ?: throw TocEmptyException("无目录规则")
        var html = body
        var url = baseUrl.ifBlank { book.tocUrl.ifBlank { book.bookUrl } }
        if (html.isNullOrBlank()) {
            val analyzeUrl = AnalyzeUrl(mUrl = url, source = bookSource, ruleData = book, debugLog = debugLog)
            html = analyzeUrl.getStrResponseAwait().body
            url = analyzeUrl.finalUrl
        }
        val analyze = AnalyzeRule(book, bookSource, debugLog)
        analyze.setContent(html ?: "", url)
        val listRule = tocRule.chapterList ?: throw TocEmptyException("目录列表规则为空")
        val elements = analyze.getElements(listRule)
        val chapters = ArrayList<BookChapter>()
        elements.forEachIndexed { i, el ->
            analyze.setContent(el, url)
            val title = tocRule.chapterName?.let { analyze.getString(it) } ?: "第${i + 1}章"
            val chapterUrl = tocRule.chapterUrl?.let { analyze.getString(it, isUrl = true) } ?: url
            chapters += BookChapter(url = chapterUrl, title = title, bookUrl = book.bookUrl, index = i)
        }
        // next page
        var next = tocRule.nextTocUrl?.let { AnalyzeRule(book, bookSource, debugLog).setContent(html ?: "", url).getString(it, isUrl = true) }
        var guard = 0
        while (!next.isNullOrBlank() && guard++ < 50) {
            val au = AnalyzeUrl(mUrl = next, source = bookSource, ruleData = book, debugLog = debugLog)
            val page = au.getStrResponseAwait().body ?: break
            val ar = AnalyzeRule(book, bookSource, debugLog).setContent(page, au.finalUrl)
            ar.getElements(listRule).forEach { el ->
                ar.setContent(el, au.finalUrl)
                val title = tocRule.chapterName?.let { ar.getString(it) } ?: "章节"
                val chapterUrl = tocRule.chapterUrl?.let { ar.getString(it, isUrl = true) } ?: au.finalUrl
                chapters += BookChapter(url = chapterUrl, title = title, bookUrl = book.bookUrl, index = chapters.size)
            }
            next = tocRule.nextTocUrl?.let { ar.getString(it, isUrl = true) }
            if (next == au.finalUrl) break
        }
        if (chapters.isEmpty()) throw TocEmptyException()
        book.totalChapterNum = chapters.size
        book.latestChapterTitle = chapters.last().title
        return chapters
    }
}
''')

w("io/legado/app/model/webBook/BookContent.kt", r'''
package io.legado.app.model.webBook

import io.legado.app.data.entities.Book
import io.legado.app.data.entities.BookChapter
import io.legado.app.data.entities.BookSource
import io.legado.app.model.DebugLog
import io.legado.app.model.analyzeRule.AnalyzeRule
import io.legado.app.model.analyzeRule.AnalyzeUrl
import io.legado.app.utils.HtmlFormatter

object BookContent {
    suspend fun analyzeContent(
        book: Book,
        bookChapter: BookChapter,
        bookSource: BookSource,
        baseUrl: String,
        redirectUrl: String,
        nextChapterUrl: String? = null,
        debugLog: DebugLog? = null
    ): String {
        val rule = bookSource.ruleContent?.content ?: return ""
        val analyzeUrl = AnalyzeUrl(
            mUrl = baseUrl.ifBlank { bookChapter.url },
            source = bookSource,
            ruleData = book,
            chapter = bookChapter,
            debugLog = debugLog
        )
        var html = analyzeUrl.getStrResponseAwait().body ?: ""
        val analyze = AnalyzeRule(book, bookSource, debugLog)
        analyze.chapter = bookChapter
        analyze.nextChapterUrl = nextChapterUrl
        analyze.setContent(html, analyzeUrl.finalUrl)
        var content = analyze.getString(rule)
        // next content pages
        var next = bookSource.ruleContent?.nextContentUrl?.let { analyze.getString(it, isUrl = true) }
        var guard = 0
        while (!next.isNullOrBlank() && guard++ < 30) {
            val au = AnalyzeUrl(mUrl = next, source = bookSource, ruleData = book, chapter = bookChapter, debugLog = debugLog)
            html = au.getStrResponseAwait().body ?: break
            analyze.setContent(html, au.finalUrl)
            content += "\n" + analyze.getString(rule)
            val n = bookSource.ruleContent?.nextContentUrl?.let { analyze.getString(it, isUrl = true) }
            if (n == next) break
            next = n
        }
        bookSource.ruleContent?.replaceRegex?.takeIf { it.isNotBlank() }?.let {
            content = content.replace(Regex(it), "")
        }
        return HtmlFormatter.format(content)
    }
}
''')

w("io/legado/app/model/webBook/WebBook.kt", r'''
package io.legado.app.model.webBook

import io.legado.app.data.entities.Book
import io.legado.app.data.entities.BookChapter
import io.legado.app.data.entities.BookSource
import io.legado.app.data.entities.SearchBook
import io.legado.app.model.ConsoleDebugLog
import io.legado.app.model.DebugLog

class WebBook(
    private val bookSourceStr: String,
    private val debugLog: Boolean = false,
    debugLogger: DebugLog? = null,
    private val userNameSpace: String = "default"
) {
    var debugLogger: DebugLog? = debugLogger ?: if (debugLog) ConsoleDebugLog else null

    private val source: BookSource by lazy {
        BookSource.fromJson(bookSourceStr).getOrThrow().also { it.setUserNameSpace(userNameSpace) }
    }

    fun getBookSource(): BookSource = source

    suspend fun searchBook(key: String, page: Int = 1): List<SearchBook> =
        BookList.searchBook(source, key, page, debugLogger)

    suspend fun exploreBook(url: String, page: Int = 1): List<SearchBook> =
        BookList.exploreBook(source, url, page, debugLogger)

    suspend fun getBookInfo(bookUrl: String): Book =
        BookInfo.getBookInfo(source, bookUrl, debugLogger)

    suspend fun getChapterList(book: Book): List<BookChapter> =
        BookChapterList.analyzeChapterList(
            book = book, body = null, bookSource = source,
            baseUrl = book.tocUrl.ifEmpty { book.bookUrl },
            redirectUrl = book.tocUrl.ifEmpty { book.bookUrl },
            debugLog = debugLogger
        )

    suspend fun getBookContent(book: Book, chapter: BookChapter, nextChapterUrl: String? = null): String =
        BookContent.analyzeContent(
            book = book, bookChapter = chapter, bookSource = source,
            baseUrl = chapter.url, redirectUrl = chapter.url,
            nextChapterUrl = nextChapterUrl, debugLog = debugLogger
        )
}
''')

w("io/legado/app/model/localBook/LocalBook.kt", r'''
package io.legado.app.model.localBook

import io.legado.app.data.entities.Book
import io.legado.app.data.entities.BookChapter

object LocalBook {
    fun getChapterList(book: Book): ArrayList<BookChapter> = when {
        book.isEpub -> EpubFile.getChapterList(book)
        book.isUmd -> UmdFile.getChapterList(book)
        book.isCbz -> CbzFile.getChapterList(book)
        book.isPdf -> PdfFile.getChapterList(book)
        else -> TextFile(book).getChapterList()
    }

    fun getContent(book: Book, chapter: BookChapter): String? = when {
        book.isEpub -> EpubFile.getContent(book, chapter)
        book.isUmd -> UmdFile.getContent(book, chapter)
        book.isCbz -> CbzFile.getContent(book, chapter)
        book.isPdf -> PdfFile.getContent(book, chapter)
        else -> TextFile(book).getContent(chapter)
    }
}
''')

w("io/legado/app/model/localBook/TextFile.kt", r'''
package io.legado.app.model.localBook

import io.legado.app.data.entities.Book
import io.legado.app.data.entities.BookChapter
import io.legado.app.help.DefaultData
import io.legado.app.utils.MD5Utils
import java.nio.charset.Charset
import java.util.regex.Pattern

class TextFile(private val book: Book) {
    private var charset: Charset = Charset.forName(book.charset ?: "UTF-8")

    fun getChapterList(): ArrayList<BookChapter> {
        val file = book.localFile()
        if (!file.isFile) return arrayListOf()
        if (book.charset.isNullOrBlank()) book.charset = "UTF-8"
        charset = Charset.forName(book.charset)
        if (book.tocUrl.isBlank()) {
            val head = file.inputStream().use { ins ->
                val buf = ByteArray(minOf(512_000, file.length().toInt().coerceAtLeast(1)))
                ins.read(buf); String(buf, charset)
            }
            book.tocUrl = selectTocRule(head)
        }
        val pattern = Pattern.compile(book.tocUrl, Pattern.MULTILINE)
        val text = file.readText(charset)
        val m = pattern.matcher(text)
        val list = ArrayList<BookChapter>()
        var last = 0
        var idx = 0
        while (m.find()) {
            if (list.isNotEmpty()) {
                list.last().end = m.start().toLong()
            } else if (m.start() > 0) {
                list += BookChapter(title = "前言", index = idx++, bookUrl = book.bookUrl, byteStart = 0, start = 0)
                list.last().end = m.start().toLong()
                idx = list.size
            }
            list += BookChapter(
                title = m.group()?.trim() ?: "章节",
                index = idx++,
                bookUrl = book.bookUrl,
                byteStart = m.start().toLong(),
                start = m.start().toLong(),
                url = MD5Utils.md5Encode16("${book.name}$idx")
            )
            last = m.end()
        }
        if (list.isEmpty()) {
            list += BookChapter(title = book.name.ifEmpty { "全文" }, index = 0, bookUrl = book.bookUrl, url = "0")
        } else {
            list.last().end = text.length.toLong()
        }
        book.totalChapterNum = list.size
        book.latestChapterTitle = list.last().title
        return list
    }

    fun getContent(chapter: BookChapter): String? {
        val file = book.localFile()
        if (!file.isFile) return null
        val text = file.readText(charset)
        val start = (chapter.start ?: chapter.byteStart).toInt().coerceIn(0, text.length)
        val end = (chapter.end ?: text.length.toLong()).toInt().coerceIn(start, text.length)
        return text.substring(start, end).trim()
    }

    private fun selectTocRule(head: String): String {
        var best = DefaultData.txtTocRules.first().rule
        var bestCount = 0
        for (r in DefaultData.txtTocRules) {
            val c = try {
                Pattern.compile(r.rule, Pattern.MULTILINE).matcher(head).let { m ->
                    var n = 0; while (m.find()) n++; n
                }
            } catch (_: Exception) { 0 }
            if (c > bestCount) { bestCount = c; best = r.rule }
        }
        return best
    }
}
''')

w("io/legado/app/model/localBook/EpubFile.kt", r'''
package io.legado.app.model.localBook

import io.legado.app.data.entities.Book
import io.legado.app.data.entities.BookChapter
import java.util.zip.ZipFile

object EpubFile {
    fun getChapterList(book: Book): ArrayList<BookChapter> {
        val file = book.localFile()
        if (!file.isFile) return arrayListOf()
        val list = ArrayList<BookChapter>()
        try {
            ZipFile(file).use { zf ->
                val entries = zf.entries().asSequence()
                    .map { it.name }
                    .filter { it.endsWith(".xhtml", true) || it.endsWith(".html", true) || it.endsWith(".htm", true) }
                    .sorted()
                    .toList()
                entries.forEachIndexed { i, name ->
                    list += BookChapter(
                        url = name, title = name.substringAfterLast('/').substringBeforeLast('.'),
                        bookUrl = book.bookUrl, index = i, resourceUrl = name
                    )
                }
            }
        } catch (_: Exception) {
        }
        book.totalChapterNum = list.size
        return list
    }

    fun getContent(book: Book, chapter: BookChapter): String? {
        val file = book.localFile()
        if (!file.isFile) return null
        val name = chapter.resourceUrl ?: chapter.url
        return try {
            ZipFile(file).use { zf ->
                val e = zf.getEntry(name) ?: return null
                zf.getInputStream(e).bufferedReader().readText()
            }
        } catch (_: Exception) {
            null
        }
    }
}
''')

w("io/legado/app/model/localBook/CbzFile.kt", r'''
package io.legado.app.model.localBook

import io.legado.app.data.entities.Book
import io.legado.app.data.entities.BookChapter
import java.util.zip.ZipFile

object CbzFile {
    fun getChapterList(book: Book): ArrayList<BookChapter> {
        val file = book.localFile()
        if (!file.isFile) return arrayListOf()
        val list = ArrayList<BookChapter>()
        try {
            ZipFile(file).use { zf ->
                zf.entries().asSequence()
                    .map { it.name }
                    .filter { it.matches(Regex(""".*\.(jpg|jpeg|png|webp)$""", RegexOption.IGNORE_CASE)) }
                    .sorted()
                    .forEachIndexed { i, name ->
                        list += BookChapter(url = name, title = "P${i + 1}", bookUrl = book.bookUrl, index = i, resourceUrl = name)
                    }
            }
        } catch (_: Exception) {
        }
        return list
    }

    fun getContent(book: Book, chapter: BookChapter): String? =
        """<img src="${chapter.resourceUrl ?: chapter.url}"/>"""
}
''')

w("io/legado/app/model/localBook/PdfFile.kt", r'''
package io.legado.app.model.localBook

import io.legado.app.data.entities.Book
import io.legado.app.data.entities.BookChapter
import org.apache.pdfbox.pdmodel.PDDocument

object PdfFile {
    fun getChapterList(book: Book): ArrayList<BookChapter> {
        val file = book.localFile()
        if (!file.isFile) return arrayListOf()
        val list = ArrayList<BookChapter>()
        try {
            PDDocument.load(file).use { doc ->
                repeat(doc.numberOfPages) { i ->
                    list += BookChapter(url = "page:$i", title = "第${i + 1}页", bookUrl = book.bookUrl, index = i)
                }
            }
        } catch (_: Exception) {
        }
        return list
    }

    fun getContent(book: Book, chapter: BookChapter): String? =
        "【PDF 第${chapter.index + 1}页】"
}
''')

w("io/legado/app/model/localBook/UmdFile.kt", r'''
package io.legado.app.model.localBook

import io.legado.app.data.entities.Book
import io.legado.app.data.entities.BookChapter

object UmdFile {
    fun getChapterList(book: Book): ArrayList<BookChapter> =
        arrayListOf(BookChapter(url = "0", title = book.name.ifEmpty { "UMD" }, bookUrl = book.bookUrl, index = 0))

    fun getContent(book: Book, chapter: BookChapter): String? =
        "【UMD 内容请用完整 umdlib 解析；此处为可编译占位】"
}
''')

w("io/legado/app/model/rss/Rss.kt", r'''
package io.legado.app.model.rss

import io.legado.app.data.entities.RssArticle
import io.legado.app.data.entities.RssSource
import io.legado.app.model.DebugLog
import io.legado.app.model.analyzeRule.AnalyzeRule
import io.legado.app.model.analyzeRule.AnalyzeUrl
import org.jsoup.Jsoup
import org.jsoup.parser.Parser

object Rss {
    suspend fun getArticles(
        sortName: String,
        sortUrl: String,
        rssSource: RssSource,
        page: Int,
        debugLog: DebugLog? = null
    ): Pair<MutableList<RssArticle>, String?> {
        val url = sortUrl.ifBlank { rssSource.sourceUrl }
        val body = AnalyzeUrl(mUrl = url, page = page, source = rssSource, debugLog = debugLog)
            .getStrResponseAwait().body ?: ""
        return if (!rssSource.ruleArticles.isNullOrBlank()) {
            parseByRule(sortName, url, body, rssSource, debugLog)
        } else {
            parseDefaultXml(sortName, url, body, rssSource) to null
        }
    }

    suspend fun getContent(
        article: RssArticle,
        ruleContent: String,
        rssSource: RssSource,
        debugLog: DebugLog? = null
    ): String {
        if (ruleContent.isBlank()) return article.description ?: article.content ?: ""
        val body = AnalyzeUrl(mUrl = article.link, source = rssSource, debugLog = debugLog)
            .getStrResponseAwait().body ?: ""
        return AnalyzeRule(null, rssSource, debugLog).setContent(body, article.link).getString(ruleContent)
    }

    private fun parseByRule(
        sortName: String, sortUrl: String, body: String, source: RssSource, debugLog: DebugLog?
    ): Pair<MutableList<RssArticle>, String?> {
        val rule = AnalyzeRule(null, source, debugLog).setContent(body, sortUrl)
        val elements = rule.getStringList(source.ruleArticles!!)
        val list = ArrayList<RssArticle>()
        var order = System.currentTimeMillis()
        for (el in elements) {
            val sub = AnalyzeRule(null, source, debugLog).setContent(el, sortUrl)
            val title = source.ruleTitle?.let { sub.getString(it) }.orEmpty()
            val link = source.ruleLink?.let { sub.getString(it, isUrl = true) }.orEmpty()
            if (title.isEmpty() && link.isEmpty()) continue
            list += RssArticle(
                origin = source.sourceUrl, sort = sortName,
                title = title.ifEmpty { link }, order = order--,
                link = link.ifEmpty { sortUrl },
                pubDate = source.rulePubDate?.let { sub.getString(it) },
                description = source.ruleDescription?.let { sub.getString(it) },
                image = source.ruleImage?.let { sub.getString(it, isUrl = true) }
            )
        }
        val next = source.ruleNextPage?.let { rule.getString(it, isUrl = true) }?.takeIf { it.isNotBlank() }
        return list to next
    }

    private fun parseDefaultXml(sortName: String, sortUrl: String, body: String, source: RssSource): MutableList<RssArticle> {
        val list = ArrayList<RssArticle>()
        try {
            val doc = Jsoup.parse(body, sortUrl, Parser.xmlParser())
            var order = System.currentTimeMillis()
            for (item in doc.select("item")) {
                list += RssArticle(
                    origin = source.sourceUrl, sort = sortName,
                    title = item.selectFirst("title")?.text().orEmpty(),
                    order = order--,
                    link = item.selectFirst("link")?.text() ?: item.selectFirst("link")?.attr("href").orEmpty(),
                    pubDate = item.selectFirst("pubDate")?.text(),
                    description = item.selectFirst("description")?.html()
                )
            }
            if (list.isEmpty()) {
                for (entry in doc.select("entry")) {
                    list += RssArticle(
                        origin = source.sourceUrl, sort = sortName,
                        title = entry.selectFirst("title")?.text().orEmpty(),
                        order = System.currentTimeMillis(),
                        link = entry.selectFirst("link[href]")?.attr("href") ?: "",
                        pubDate = entry.selectFirst("updated")?.text(),
                        description = entry.selectFirst("summary")?.html()
                    )
                }
            }
        } catch (_: Exception) {
        }
        return list
    }
}
''')

w("io/legado/app/model/Debugger.kt", r'''
package io.legado.app.model

import io.legado.app.data.entities.BookSource
import io.legado.app.model.webBook.WebBook

class Debugger(private val onMsg: (String) -> Unit) : DebugLog {
    override fun log(source: String?, msg: String?) {
        onMsg(msg ?: "")
    }

    suspend fun startDebug(webBook: WebBook, key: String) {
        onMsg("◇开始调试: $key")
        when {
            key.startsWith("http://") || key.startsWith("https://") -> {
                onMsg("◇详情"); val book = webBook.getBookInfo(key); onMsg(book.name)
                onMsg("◇目录"); val toc = webBook.getChapterList(book); onMsg("共${toc.size}章")
                if (toc.isNotEmpty()) {
                    onMsg("◇正文"); val c = webBook.getBookContent(book, toc[0], toc.getOrNull(1)?.url)
                    onMsg(c.take(200))
                }
            }
            key.startsWith("::") -> {
                onMsg("◇发现"); val list = webBook.exploreBook(key.removePrefix("::"), 1); onMsg("结果${list.size}")
            }
            key.startsWith("++") -> onMsg("◇目录调试占位 ${key.removePrefix("++")}")
            key.startsWith("--") -> onMsg("◇正文调试占位 ${key.removePrefix("--")}")
            else -> {
                onMsg("◇搜索"); val list = webBook.searchBook(key, 1); onMsg("结果${list.size}")
                list.take(3).forEach { onMsg("- ${it.name} / ${it.author}") }
            }
        }
        onMsg("◇结束")
    }
}
''')

w("me/ag2s/epublib/domain/EpubDomain.kt", r'''
package me.ag2s.epublib.domain

class EpubBook {
    val metadata = Metadata()
    private val sections = mutableListOf<Pair<String, Resource>>()
    fun addSection(title: String, resource: Resource) { sections += title to resource }
    fun getSections(): List<Pair<String, Resource>> = sections
}

class Metadata {
    fun addTitle(t: String) {}
    fun addAuthor(a: Author) {}
}

data class Author(val name: String)
class Resource(val data: ByteArray, val href: String)
''')

w("me/ag2s/epublib/epub/EpubWriter.kt", r'''
package me.ag2s.epublib.epub

import me.ag2s.epublib.domain.EpubBook
import java.io.OutputStream
import java.util.zip.ZipEntry
import java.util.zip.ZipOutputStream

class EpubWriter {
    fun write(book: EpubBook, out: OutputStream) {
        ZipOutputStream(out).use { zos ->
            zos.putNextEntry(ZipEntry("mimetype"))
            zos.write("application/epub+zip".toByteArray())
            zos.closeEntry()
            book.getSections().forEach { (_, res) ->
                zos.putNextEntry(ZipEntry(res.href))
                zos.write(res.data)
                zos.closeEntry()
            }
        }
    }
}
''')

print("part2 engines done", sum(1 for _ in SRC.rglob("*.kt")))
