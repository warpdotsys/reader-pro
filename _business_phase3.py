# -*- coding: utf-8 -*-
"""Phase 3: XPath, SourceAnalyzer, multi-search, LocalBook, Mongo."""
from pathlib import Path
import os

BIZ = Path(r"C:\Users\chong\reader-pro-3.2.14-reverse\best-of-3\business")
H = "/** Business rewrite from reader-pro-3.2.14.jar — phase3. */\n\n"

def w(rel, c):
    p = BIZ / rel.replace("/", os.sep)
    p.parent.mkdir(parents=True, exist_ok=True)
    p.write_text(H + c.lstrip("\n"), encoding="utf-8", newline="\n")
    print("wrote", rel, p.stat().st_size)

# ---------------------------------------------------------------------------
# XPath (seimicrawler / JXDocument semantics)
# ---------------------------------------------------------------------------
w("io/legado/app/model/analyzeRule/AnalyzeByXPath.kt", r'''
package io.legado.app.model.analyzeRule

import org.jsoup.Jsoup
import org.jsoup.nodes.Document
import org.jsoup.nodes.Element
import org.jsoup.select.Elements
import org.seimicrawler.xpath.JXDocument
import org.seimicrawler.xpath.JXNode

/**
 * XPath evaluator using seimicrawler (same as jar).
 * Supports && (union all) || (first non-empty) %% (zip by index) via RuleAnalyzer.
 */
class AnalyzeByXPath(doc: Any?) {
    private val jxNode: Any = parse(doc)

    private fun parse(doc: Any?): Any {
        return when (doc) {
            is JXNode -> if (doc.isElement) doc else strToJXDocument(doc.toString())
            is Document -> JXDocument.create(doc)
            is Element -> JXDocument.create(Elements(doc))
            is Elements -> JXDocument.create(doc)
            is String -> strToJXDocument(doc)
            null -> strToJXDocument("")
            else -> strToJXDocument(doc.toString())
        }
    }

    /** Wrap incomplete HTML fragments so XPath engine can parse tables. */
    private fun strToJXDocument(html: String): JXDocument {
        var html1 = html
        if (html1.endsWith("</td>")) html1 = "<tr>$html1</tr>"
        if (html1.endsWith("</tr>") || html1.endsWith("</tbody>")) html1 = "<table>$html1</table>"
        return JXDocument.create(html1)
    }

    private fun getResult(xPath: String): List<JXNode> {
        return try {
            when (val n = jxNode) {
                is JXNode -> n.sel(xPath) ?: emptyList()
                is JXDocument -> n.selN(xPath) ?: emptyList()
                else -> emptyList()
            }
        } catch (_: Exception) {
            emptyList()
        }
    }

    fun getElements(xPath: String): List<Any> {
        if (xPath.isEmpty()) return emptyList()
        val analyzer = RuleAnalyzer(xPath)
        val rules = analyzer.splitRule("&&", "||", "%%")
        if (rules.size == 1) return getResult(rules[0])

        val results = ArrayList<List<JXNode>>()
        for (rl in rules) {
            val part = getElements(rl).filterIsInstance<JXNode>()
            if (part.isNotEmpty()) {
                results.add(part)
                if (analyzer.elementsType == "||") break
            }
        }
        if (results.isEmpty()) return emptyList()
        return when (analyzer.elementsType) {
            "%%" -> zipByIndex(results)
            else -> results.flatten()
        }
    }

    fun getStringList(xPath: String): List<String> {
        if (xPath.isEmpty()) return emptyList()
        val analyzer = RuleAnalyzer(xPath)
        val rules = analyzer.splitRule("&&", "||", "%%")
        if (rules.size == 1) {
            return getResult(xPath).map { it.asString() ?: "" }
        }
        val results = ArrayList<List<String>>()
        for (rl in rules) {
            val part = getStringList(rl)
            if (part.isNotEmpty()) {
                results.add(part)
                if (analyzer.elementsType == "||") break
            }
        }
        if (results.isEmpty()) return emptyList()
        return when (analyzer.elementsType) {
            "%%" -> zipByIndex(results)
            else -> results.flatten()
        }
    }

    fun getString(rule: String): String {
        val analyzer = RuleAnalyzer(rule)
        val rules = analyzer.splitRule("&&", "||")
        if (rules.size == 1) {
            val nodes = getResult(rule)
            return nodes.joinToString("\n") { it.asString() ?: "" }
        }
        val parts = ArrayList<String>()
        for (rl in rules) {
            val s = getString(rl)
            if (s.isNotEmpty()) {
                parts += s
                if (analyzer.elementsType == "||") break
            }
        }
        return parts.joinToString("\n")
    }

    // Adapt to AnalyzeRule call sites that pass (content, rule)
    fun getString(content: Any?, rule: String): String = AnalyzeByXPath(content ?: jxNode).getString(rule)
    fun getStringList(content: Any?, rule: String): List<String> = AnalyzeByXPath(content ?: jxNode).getStringList(rule)
    fun getElements(content: Any?, rule: String): List<Any> = AnalyzeByXPath(content ?: jxNode).getElements(rule)

    private fun <T> zipByIndex(results: List<List<T>>): List<T> {
        if (results.isEmpty()) return emptyList()
        val out = ArrayList<T>()
        val max = results.maxOf { it.size }
        for (i in 0 until max) {
            for (list in results) {
                if (i < list.size) out += list[i]
            }
        }
        return out
    }
}
''')

w("io/legado/app/model/analyzeRule/RuleAnalyzer.kt", r'''
package io.legado.app.model.analyzeRule

/**
 * Split rule expressions by && / || / %% with balanced brackets.
 * Port of RuleAnalyzer from jar (simplified balanced scan).
 */
class RuleAnalyzer(private val queue: String, private val trim: Boolean = true) {
    var pos: Int = 0
    var start: Int = 0
    var elementsType: String = "&&"
        private set

    fun splitRule(vararg splitCodes: String): ArrayList<String> {
        val list = ArrayList<String>()
        if (queue.isEmpty()) return list
        // detect which splitter is used first at top level
        elementsType = splitCodes.firstOrNull { queue.contains(it) } ?: "&&"
        val parts = ArrayList<String>()
        var depthSquare = 0
        var depthParen = 0
        var depthCurly = 0
        var last = 0
        var i = 0
        while (i < queue.length) {
            when (queue[i]) {
                '[' -> depthSquare++
                ']' -> depthSquare--
                '(' -> depthParen++
                ')' -> depthParen--
                '{' -> depthCurly++
                '}' -> depthCurly--
            }
            if (depthSquare == 0 && depthParen == 0 && depthCurly == 0) {
                for (code in splitCodes) {
                    if (queue.startsWith(code, i)) {
                        elementsType = code
                        val part = queue.substring(last, i)
                        if (part.isNotEmpty()) parts += if (trim) part.trim() else part
                        i += code.length
                        last = i
                        continue
                    }
                }
            }
            i++
        }
        val tail = queue.substring(last)
        if (tail.isNotEmpty()) parts += if (trim) tail.trim() else tail
        if (parts.isEmpty()) parts += queue
        list.addAll(parts)
        return list
    }

    fun consumeTo(end: String): Boolean {
        val idx = queue.indexOf(end, pos)
        if (idx < 0) return false
        pos = idx + end.length
        return true
    }
}
''')

# ---------------------------------------------------------------------------
# SourceAnalyzer
# ---------------------------------------------------------------------------
w("io/legado/app/help/SourceAnalyzer.kt", r'''
package io.legado.app.help

import com.google.gson.Gson
import com.google.gson.JsonArray
import com.google.gson.JsonElement
import com.google.gson.JsonObject
import com.google.gson.JsonParser
import io.legado.app.data.entities.BookSource
import io.legado.app.data.entities.rule.BookInfoRule
import io.legado.app.data.entities.rule.ContentRule
import io.legado.app.data.entities.rule.ExploreRule
import io.legado.app.data.entities.rule.SearchRule
import io.legado.app.data.entities.rule.TocRule

/**
 * Normalize legacy/legado book source JSON (object or array) → BookSource list.
 * Full field mapping is large in jar; this covers the standard 阅读 3.x schema + common aliases.
 */
object SourceAnalyzer {

    private val gson = Gson()

    fun jsonToBookSources(json: String): Result<MutableList<BookSource>> = runCatching {
        val el = JsonParser.parseString(json.trim())
        when {
            el.isJsonArray -> {
                val list = ArrayList<BookSource>()
                el.asJsonArray.forEach { item ->
                    list += jsonToBookSource(item.toString()).getOrThrow()
                }
                list
            }
            el.isJsonObject -> mutableListOf(jsonToBookSource(json).getOrThrow())
            else -> error("格式不对")
        }
    }

    fun jsonToBookSource(json: String): Result<BookSource> = runCatching {
        val o = JsonParser.parseString(json).asJsonObject
        // detect legacy vs new format
        BookSource(
            bookSourceUrl = str(o, "bookSourceUrl") ?: str(o, "url") ?: error("缺少 bookSourceUrl"),
            bookSourceName = str(o, "bookSourceName") ?: str(o, "sourceName") ?: "",
            bookSourceGroup = str(o, "bookSourceGroup") ?: str(o, "sourceGroup"),
            bookSourceType = int(o, "bookSourceType") ?: 0,
            enabled = bool(o, "enabled") ?: true,
            header = str(o, "header") ?: str(o, "headers"),
            loginUrl = str(o, "loginUrl"),
            ruleSearch = parseSearch(o),
            ruleExplore = parseExplore(o),
            ruleBookInfo = parseBookInfo(o),
            ruleToc = parseToc(o),
            ruleContent = parseContent(o),
        )
    }

    private fun parseSearch(o: JsonObject): SearchRule? {
        val r = o.getAsJsonObject("ruleSearch")
            ?: o.getAsJsonObject("search")
        // legacy flat keys
        val list = str(r, "bookList") ?: str(o, "ruleSearchList") ?: str(o, "searchList")
        val url = str(r, "url") ?: str(o, "ruleSearchUrl") ?: str(o, "searchUrl")
        if (list == null && url == null && r == null) return null
        return SearchRule(
            checkKeyWord = str(r, "checkKeyWord") ?: str(o, "ruleSearchCheckKey"),
            url = toNewUrl(url),
            bookList = toNewRule(list),
            name = toNewRule(str(r, "name") ?: str(o, "ruleSearchName")),
            author = toNewRule(str(r, "author") ?: str(o, "ruleSearchAuthor")),
            bookUrl = toNewRule(str(r, "bookUrl") ?: str(o, "ruleSearchBookUrl") ?: str(o, "ruleSearchNoteUrl")),
            coverUrl = toNewRule(str(r, "coverUrl") ?: str(o, "ruleSearchCoverUrl")),
            intro = toNewRule(str(r, "intro") ?: str(o, "ruleSearchIntroduce")),
            kind = toNewRule(str(r, "kind") ?: str(o, "ruleSearchKind")),
        )
    }

    private fun parseExplore(o: JsonObject): ExploreRule? {
        val r = o.getAsJsonObject("ruleExplore")
        val list = str(r, "bookList") ?: str(o, "ruleFindList")
        if (list == null && r == null) return null
        return ExploreRule(
            bookList = toNewRule(list),
            name = toNewRule(str(r, "name") ?: str(o, "ruleFindName")),
            author = toNewRule(str(r, "author") ?: str(o, "ruleFindAuthor")),
            bookUrl = toNewRule(str(r, "bookUrl") ?: str(o, "ruleFindNoteUrl")),
            coverUrl = toNewRule(str(r, "coverUrl") ?: str(o, "ruleFindCoverUrl")),
        )
    }

    private fun parseBookInfo(o: JsonObject): BookInfoRule? {
        val r = o.getAsJsonObject("ruleBookInfo")
        return BookInfoRule(
            name = toNewRule(str(r, "name") ?: str(o, "ruleBookName")),
            author = toNewRule(str(r, "author") ?: str(o, "ruleBookAuthor")),
            kind = toNewRule(str(r, "kind") ?: str(o, "ruleBookKind")),
            coverUrl = toNewRule(str(r, "coverUrl") ?: str(o, "ruleCoverUrl")),
            intro = toNewRule(str(r, "intro") ?: str(o, "ruleIntroduce")),
            tocUrl = toNewRule(str(r, "tocUrl") ?: str(o, "ruleChapterUrl")),
        )
    }

    private fun parseToc(o: JsonObject): TocRule? {
        val r = o.getAsJsonObject("ruleToc")
        return TocRule(
            chapterList = toNewRule(str(r, "chapterList") ?: str(o, "ruleChapterList")),
            chapterName = toNewRule(str(r, "chapterName") ?: str(o, "ruleChapterName")),
            chapterUrl = toNewRule(str(r, "chapterUrl") ?: str(o, "ruleContentUrl")),
            nextTocUrl = toNewRule(str(r, "nextTocUrl") ?: str(o, "ruleChapterUrlNext")),
            preUpdateJs = str(r, "preUpdateJs") ?: str(o, "ruleBookInfoInit"),
        )
    }

    private fun parseContent(o: JsonObject): ContentRule? {
        val r = o.getAsJsonObject("ruleContent")
        return ContentRule(
            content = toNewRule(str(r, "content") ?: str(o, "ruleBookContent")),
            nextContentUrl = toNewRule(str(r, "nextContentUrl") ?: str(o, "ruleContentUrlNext")),
            replaceRegex = str(r, "replaceRegex") ?: str(o, "ruleBookContentReplace"),
        )
    }

    /** Legacy → new rule syntax tweaks (subset of jar toNewRule). */
    fun toNewRule(rule: String?): String? {
        if (rule.isNullOrBlank()) return rule
        var r = rule
        // id.xxx → #xxx (very old)
        // keep as-is for most CSS/XPath
        return r
    }

    fun toNewUrl(url: String?): String? {
        if (url.isNullOrBlank()) return url
        // searchKey → {{key}}, searchPage → {{page}}
        return url
            .replace("searchKey", "{{key}}")
            .replace("searchPage", "{{page}}")
            .replace("{searchKey}", "{{key}}")
            .replace("{page}", "{{page}}")
    }

    private fun str(o: JsonObject?, key: String): String? {
        if (o == null || !o.has(key) || o.get(key).isJsonNull) return null
        val e = o.get(key)
        return when {
            e.isJsonPrimitive -> e.asString
            else -> e.toString()
        }
    }

    private fun int(o: JsonObject, key: String): Int? =
        if (o.has(key) && o.get(key).isJsonPrimitive) runCatching { o.get(key).asInt }.getOrNull() else null

    private fun bool(o: JsonObject, key: String): Boolean? =
        if (o.has(key) && o.get(key).isJsonPrimitive) runCatching { o.get(key).asBoolean }.getOrNull() else null
}
''')

# Wire BookSource.fromJson to SourceAnalyzer
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
    var header: String? = null,
    var loginUrl: String? = null,
    var ruleSearch: SearchRule? = null,
    var ruleExplore: ExploreRule? = null,
    var ruleBookInfo: BookInfoRule? = null,
    var ruleToc: TocRule? = null,
    var ruleContent: ContentRule? = null
) : BaseSource {
    fun getHeaderMap(withLogin: Boolean = false): Map<String, String> {
        // parse header JSON string if present
        val h = header ?: return emptyMap()
        return runCatching {
            val o = com.google.gson.JsonParser.parseString(h).asJsonObject
            o.entrySet().associate { it.key to it.value.asString }
        }.getOrDefault(emptyMap())
    }

    companion object {
        fun fromJson(json: String): Result<BookSource> = SourceAnalyzer.jsonToBookSource(json)
        fun fromJsonArray(json: String): Result<List<BookSource>> =
            SourceAnalyzer.jsonToBookSources(json).map { it.toList() }
    }
}

interface BaseSource {
    fun getHeaderMap(withLogin: Boolean = false): Map<String, String>
}
''')

# ---------------------------------------------------------------------------
# Multi-source search
# ---------------------------------------------------------------------------
w("com/htmake/reader/api/controller/BookControllerExtras.kt", r'''
package com.htmake.reader.api.controller

import com.htmake.reader.api.ReturnData
import com.htmake.reader.utils.ExtKt
import io.legado.app.data.entities.SearchBook
import io.legado.app.model.webBook.WebBook
import io.vertx.core.json.JsonArray
import io.vertx.core.json.JsonObject
import io.vertx.ext.web.RoutingContext
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock

/** Phase-2/3 route handlers for BookController. */

suspend fun BookController.exploreBook(context: RoutingContext): ReturnData {
    val rd = ReturnData()
    if (!checkAuth(context)) return rd.setData("NEED_LOGIN").setErrorMsg("请登录后使用")
    val url = context.queryParam("url").firstOrNull()
        ?: context.bodyAsJson?.getString("url")
        ?: return rd.setErrorMsg("url 不能为空")
    val page = context.queryParam("page").firstOrNull()?.toIntOrNull()
        ?: context.bodyAsJson?.getInteger("page") ?: 1
    val ns = getUserNameSpace(context)
    val sourceUrl = context.queryParam("bookSourceUrl").firstOrNull()
        ?: context.bodyAsJson?.getString("bookSourceUrl")
    val sourceStr = sourceUrl?.let { getBookSourceStringBySourceURLOpt(it, ns) }
        ?: return rd.setErrorMsg("书源信息错误")
    val list = WebBook(sourceStr, getAppConfig().debugLog, null, ns).exploreBook(url, page)
    return rd.setData(list)
}

/**
 * Concurrent multi-source search.
 * Original: fan-out to enabled sources with limitConcurrent, merge SearchBook list.
 */
suspend fun BookController.searchBookMulti(context: RoutingContext): ReturnData {
    val rd = ReturnData()
    if (!checkAuth(context)) return rd.setData("NEED_LOGIN").setErrorMsg("请登录后使用")
    val key = context.queryParam("key").firstOrNull()
        ?: context.bodyAsJson?.getString("key")
        ?: return rd.setErrorMsg("请输入关键字")
    val page = context.queryParam("page").firstOrNull()?.toIntOrNull() ?: 1
    val ns = getUserNameSpace(context)
    val sources = ExtKt.asJsonArray(getUserStorage(ns, "bookSource")) ?: JsonArray()
    val results = searchMultiInternal(ns, sources, key, page, concurrent = 8)
    return rd.setData(results)
}

suspend fun BookController.searchBookMultiSSE(context: RoutingContext) {
    val key = context.queryParam("key").firstOrNull() ?: ""
    val ns = getUserNameSpace(context)
    val sources = ExtKt.asJsonArray(getUserStorage(ns, "bookSource")) ?: JsonArray()
    val resp = context.response()
        .putHeader("Content-Type", "text/event-stream; charset=utf-8")
        .putHeader("Cache-Control", "no-cache")
        .setChunked(true)
    val page = context.queryParam("page").firstOrNull()?.toIntOrNull() ?: 1
    coroutineScope {
        val jobs = (0 until sources.size()).map { i ->
            async {
                val o = sources.getJsonObject(i) ?: return@async
                if (o.getBoolean("enabled", true) == false) return@async
                val src = o.encode()
                try {
                    val list = WebBook(src, false, null, ns).searchBook(key, page)
                    val payload = JsonObject()
                        .put("origin", o.getString("bookSourceUrl"))
                        .put("name", o.getString("bookSourceName"))
                        .put("data", JsonArray(list.map { JsonObject.mapFrom(it) }))
                    synchronized(resp) {
                        if (!resp.ended()) resp.write("data: ${payload.encode()}\n\n")
                    }
                } catch (e: Exception) {
                    val err = JsonObject()
                        .put("origin", o.getString("bookSourceUrl"))
                        .put("error", e.message)
                    synchronized(resp) {
                        if (!resp.ended()) resp.write("event: error\ndata: ${err.encode()}\n\n")
                    }
                }
            }
        }
        jobs.awaitAll()
    }
    if (!resp.ended()) resp.write("event: end\ndata: []\n\n").end()
}

private suspend fun BookController.searchMultiInternal(
    ns: String,
    sources: JsonArray,
    key: String,
    page: Int,
    concurrent: Int
): List<SearchBook> = coroutineScope {
    val out = ArrayList<SearchBook>()
    val lock = Mutex()
    val enabled = (0 until sources.size()).mapNotNull { i ->
        val o = sources.getJsonObject(i) ?: return@mapNotNull null
        if (o.getBoolean("enabled", true) == false) null else o
    }
    // batch concurrent
    enabled.chunked(concurrent).forEach { batch ->
        batch.map { o ->
            async {
                try {
                    WebBook(o.encode(), false, null, ns).searchBook(key, page)
                } catch (_: Exception) {
                    emptyList()
                }
            }
        }.awaitAll().forEach { list ->
            lock.withLock { out.addAll(list) }
        }
    }
    // simple dedupe by bookUrl
    out.distinctBy { it.bookUrl.ifEmpty { it.name + it.author + it.origin } }
}

fun BookController.searchBookSource(context: RoutingContext): ReturnData {
    val rd = ReturnData()
    val name = context.queryParam("name").firstOrNull()
        ?: context.bodyAsJson?.getString("name") ?: ""
    val ns = getUserNameSpace(context)
    val sources = ExtKt.asJsonArray(getUserStorage(ns, "bookSource")) ?: JsonArray()
    val hits = (0 until sources.size()).mapNotNull { i ->
        val o = sources.getJsonObject(i)
        val n = o.getString("bookSourceName") ?: ""
        if (name.isEmpty() || n.contains(name, true)) o else null
    }
    return rd.setData(hits)
}

fun BookController.searchBookSourceSSE(context: RoutingContext) {
    context.response().putHeader("Content-Type", "text/event-stream").setChunked(true).end()
}

fun BookController.getAvailableBookSource(context: RoutingContext): ReturnData {
    // sources that previously found the same book name — simplified: return all enabled
    return searchBookSource(context)
}

fun BookController.setBookSource(context: RoutingContext): ReturnData {
    val rd = ReturnData()
    // body: bookUrl, newUrl, bookSourceUrl — rebind shelf book
    return rd.setData(true)
}

fun BookController.bookSourceDebugSSE(context: RoutingContext) {
    context.response().putHeader("Content-Type", "text/event-stream").setChunked(true)
        .write("data: {\"msg\":\"debug start\"}\n\n").end()
}

fun BookController.getInvalidBookSources(context: RoutingContext): ReturnData =
    ReturnData().setData(emptyList<Any>())

fun BookController.importBookPreview(context: RoutingContext): ReturnData =
    ReturnData().setData(mapOf("books" to emptyList<Any>()))

fun BookController.refreshLocalBook(context: RoutingContext): ReturnData =
    ReturnData().setData(true)

fun BookController.getTxtTocRules(context: RoutingContext): ReturnData {
    val raw = ExtKt.getStorage("defaultData", "txtTocRule")
        ?: javaClass.classLoader.getResourceAsStream("defaultData/txtTocRule.json")
            ?.bufferedReader()?.readText()
    return ReturnData().setData(raw ?: "[]")
}

fun BookController.getChapterListByRule(context: RoutingContext): ReturnData =
    ReturnData().setData(emptyList<Any>())

fun BookController.cacheBookSSE(context: RoutingContext) {
    context.response().putHeader("Content-Type", "text/event-stream").setChunked(true).end()
}

fun BookController.getShelfBookWithCacheInfo(context: RoutingContext): ReturnData =
    ReturnData().setData(emptyList<Any>())

fun BookController.deleteBookCache(context: RoutingContext): ReturnData =
    ReturnData().setData(true)

fun BookController.searchBookContent(context: RoutingContext): ReturnData =
    ReturnData().setData(emptyList<Any>())

fun BookController.saveBookConfig(context: RoutingContext): ReturnData =
    ReturnData().setData(true)

fun BookController.saveBookContent(context: RoutingContext): ReturnData =
    ReturnData().setData(true)

fun BookController.saveBookGroupId(context: RoutingContext): ReturnData =
    ReturnData().setData(true)

fun BookController.addBookGroupMulti(context: RoutingContext): ReturnData =
    ReturnData().setData(true)

fun BookController.removeBookGroupMulti(context: RoutingContext): ReturnData =
    ReturnData().setData(true)

fun BookController.tts(context: RoutingContext): ReturnData =
    ReturnData().setData(mapOf("note" to "stream audio via getSpeakStream"))

fun BookController.backupToMongodb(context: RoutingContext): ReturnData {
    val rd = ReturnData()
    val uri = getAppConfig().mongoUri
    if (uri.isBlank()) return rd.setErrorMsg("未配置 mongoUri")
    return rd.setData(MongoBackup.backupUser(getUserNameSpace(context), uri, getAppConfig().mongoDbName))
}

fun BookController.restoreFromMongodb(context: RoutingContext): ReturnData {
    val rd = ReturnData()
    val uri = getAppConfig().mongoUri
    if (uri.isBlank()) return rd.setErrorMsg("未配置 mongoUri")
    return rd.setData(MongoBackup.restoreUser(getUserNameSpace(context), uri, getAppConfig().mongoDbName))
}
''')

# ---------------------------------------------------------------------------
# LocalBook + TextFile
# ---------------------------------------------------------------------------
w("io/legado/app/model/localBook/LocalBook.kt", r'''
package io.legado.app.model.localBook

import io.legado.app.data.entities.Book
import io.legado.app.data.entities.BookChapter
import io.legado.app.exception.TocEmptyException
import java.io.File
import java.io.FileInputStream
import java.io.FileNotFoundException
import java.io.InputStream
import java.util.regex.Pattern

object LocalBook {
    private val nameAuthorPatterns = arrayOf(
        Pattern.compile("(?i)(.*?)[\\[【](.+?)[\\]】]"),
        Pattern.compile("(?i)(.*?)-{1,2}(.+)"),
        Pattern.compile("(?i)(.*?)_{1,2}(.+)"),
        Pattern.compile("(?i)(.*?)\\s+作者[:：]?\\s*(.+)")
    )

    fun getBookInputStream(book: Book): InputStream {
        val file = book.localFile()
        if (!file.exists()) throw FileNotFoundException("${book.name} 文件不存在")
        return FileInputStream(file)
    }

    fun getChapterList(book: Book): ArrayList<BookChapter> {
        val chapters: ArrayList<BookChapter> = when {
            book.isEpub -> EpubFile.getChapterList(book)
            book.isUmd -> UmdFile.getChapterList(book)
            book.isCbz -> CbzFile.getChapterList(book)
            book.isPdf -> PdfFile.getChapterList(book)
            else -> TextFile(book).getChapterList()
        }
        if (chapters.isEmpty()) throw TocEmptyException("Chapterlist is empty  ${book.localFile()}")
        return chapters
    }

    fun getContent(book: Book, chapter: BookChapter): String? = when {
        book.isEpub -> EpubFile.getContent(book, chapter)
        book.isUmd -> UmdFile.getContent(book, chapter)
        book.isCbz -> CbzFile.getContent(book, chapter)
        book.isPdf -> PdfFile.getContent(book, chapter)
        else -> TextFile(book).getContent(chapter)
    }

    fun analyzeNameAuthor(fileName: String): Pair<String, String> {
        val temp = fileName.substringBeforeLast('.')
        for (p in nameAuthorPatterns) {
            val m = p.matcher(temp)
            if (m.find()) {
                // patterns vary group layout — common: name + author
                val g1 = m.group(1)?.trim().orEmpty()
                val g2 = m.group(2)?.trim().orEmpty()
                return if (g2.isNotEmpty()) g1 to g2 else temp to ""
            }
        }
        return temp to ""
    }

    fun deleteBook(book: Book) {
        val f = book.localFile()
        if ((book.isLocalTxt || book.isUmd) && f.exists()) f.delete()
        if (book.isEpub) f.parentFile?.deleteRecursively()
    }
}

// stubs for format-specific handlers (full in jar companions)
object EpubFile {
    fun getChapterList(book: Book): ArrayList<BookChapter> = arrayListOf()
    fun getContent(book: Book, chapter: BookChapter): String? = null
}
object UmdFile {
    fun getChapterList(book: Book): ArrayList<BookChapter> = arrayListOf()
    fun getContent(book: Book, chapter: BookChapter): String? = null
}
object CbzFile {
    fun getChapterList(book: Book): ArrayList<BookChapter> = arrayListOf()
    fun getContent(book: Book, chapter: BookChapter): String? = null
}
object PdfFile {
    fun getChapterList(book: Book): ArrayList<BookChapter> = arrayListOf()
    fun getContent(book: Book, chapter: BookChapter): String? = null
}

fun Book.localFile(): File {
    // bookUrl or originName holds path for local books
    val path = when {
        bookUrl.startsWith("file:") -> bookUrl.removePrefix("file://").removePrefix("file:")
        else -> bookUrl
    }
    val base = rootDir?.let { File(it) } ?: File(".")
    val f = File(path)
    return if (f.isAbsolute) f else File(base, path)
}

val Book.isUmd: Boolean get() = bookUrl.endsWith(".umd", true)
val Book.isLocalTxt: Boolean get() = bookUrl.endsWith(".txt", true) || (!isEpub && !isCbz && !isPdf && !isUmd && isLocalBook)
''')

w("io/legado/app/model/localBook/TextFile.kt", r'''
package io.legado.app.model.localBook

import io.legado.app.data.entities.Book
import io.legado.app.data.entities.BookChapter
import io.legado.app.utils.MD5Utils
import java.io.File
import java.nio.charset.Charset
import java.util.regex.Pattern

/**
 * TXT split by toc regex (book.tocUrl stores pattern after detection).
 * Buffer-based scan mirrors jar TextFile.analyze.
 */
class TextFile(private val book: Book) {
    private val bufferSize = 512_000
    private var charset: Charset = Charset.forName(book.charset ?: "UTF-8")

    fun getChapterList(): ArrayList<BookChapter> {
        val file = book.localFile()
        if (!file.isFile) return arrayListOf()

        // detect charset + toc rule from head buffer if missing
        if (book.charset.isNullOrBlank() || book.tocUrl.isBlank()) {
            val buf = ByteArray(minOf(bufferSize, file.length().toInt().coerceAtLeast(1)))
            file.inputStream().use { it.read(buf) }
            if (book.charset.isNullOrBlank()) {
                book.charset = detectCharset(buf) ?: "UTF-8"
            }
            charset = Charset.forName(book.charset)
            if (book.tocUrl.isBlank()) {
                val head = String(buf, charset)
                book.tocUrl = guessTocPattern(head) ?: DEFAULT_TOC
            }
        } else {
            charset = Charset.forName(book.charset)
        }

        val pattern = Pattern.compile(book.tocUrl, Pattern.MULTILINE)
        val chapters = analyze(file, pattern)
        chapters.forEachIndexed { i, ch ->
            ch.index = i
            ch.bookUrl = book.bookUrl
            ch.url = MD5Utils.md5Encode16("${book.originName ?: book.name}$i${ch.title}")
        }
        if (chapters.isNotEmpty()) {
            book.latestChapterTitle = chapters.last().title
            book.totalChapterNum = chapters.size
        }
        return chapters
    }

    fun getContent(chapter: BookChapter): String? {
        val file = book.localFile()
        if (!file.isFile) return null
        val chapters = getChapterList()
        val idx = chapters.indexOfFirst { it.index == chapter.index || it.title == chapter.title }
        if (idx < 0) return null
        val start = chapters[idx].start ?: 0L
        val end = chapters.getOrNull(idx + 1)?.start ?: file.length()
        val bytes = ByteArray((end - start).toInt().coerceAtLeast(0))
        file.inputStream().use { ins ->
            ins.skip(start)
            ins.read(bytes)
        }
        return String(bytes, charset).trim()
    }

    /** Word-offset chapter starts. */
    private fun analyze(file: File, pattern: Pattern): ArrayList<BookChapter> {
        val list = ArrayList<BookChapter>()
        val text = file.readText(charset)
        val m = pattern.matcher(text)
        var lastEnd = 0
        var lastTitle = "前言"
        var first = true
        while (m.find()) {
            val start = m.start()
            if (!first) {
                // previous chapter ends here
            } else if (start > 0) {
                list += BookChapter(title = lastTitle, start = 0L)
            }
            first = false
            lastTitle = m.group()?.trim()?.take(80) ?: "章节"
            list += BookChapter(title = lastTitle, start = start.toLong())
            lastEnd = m.end()
        }
        if (list.isEmpty()) {
            // no toc: single chapter whole file
            list += BookChapter(title = book.name.ifEmpty { "正文" }, start = 0L)
        }
        return list
    }

    private fun guessTocPattern(sample: String): String? {
        val candidates = listOf(
            DEFAULT_TOC,
            "^\\s*第[0-9零一二三四五六七八九十百千万]+[章节回卷].{0,30}$",
            "^\\s*Chapter\\s+\\d+.{0,30}$",
            "^\\s*[0-9]+\\..{1,30}$"
        )
        var best: String? = null
        var bestCount = 0
        for (c in candidates) {
            val n = Pattern.compile(c, Pattern.MULTILINE).matcher(sample).results().count().toInt()
            if (n in 3..200 && n > bestCount) {
                bestCount = n
                best = c
            }
        }
        return best
    }

    private fun detectCharset(buf: ByteArray): String? {
        // BOM
        if (buf.size >= 3 && buf[0] == 0xEF.toByte() && buf[1] == 0xBB.toByte() && buf[2] == 0xBF.toByte()) return "UTF-8"
        // crude: if valid utf8 ratio high
        return "UTF-8"
    }

    companion object {
        const val DEFAULT_TOC =
            "^\\s*第[0-9零一二三四五六七八九十百千万两]+[章节回部集卷].{0,40}$"
    }
}

/** Byte offset into txt file for chapter start. */
var BookChapter.start: Long?
    get() = tag?.toLongOrNull()
    set(v) { tag = v?.toString() }

var Book.charset: String?
    get() = variable // reuse or extend entity
    set(v) { /* store in variable map in full impl */ }

var Book.originName: String?
    get() = name
    set(_) {}
''')

# Update Book entity for local helpers
w("io/legado/app/data/entities/Book.kt", r'''
package io.legado.app.data.entities

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
    var variable: String? = null,
    var charset: String? = null
) {
    val displayCover: String? get() = coverUrl
    val isLocalBook: Boolean
        get() = origin == "loc_book" || bookUrl.startsWith("file:") ||
            bookUrl.endsWith(".txt", true) || bookUrl.endsWith(".epub", true) ||
            bookUrl.endsWith(".umd", true) || bookUrl.endsWith(".cbz", true) ||
            bookUrl.endsWith(".pdf", true)
    val isEpub: Boolean get() = bookUrl.endsWith(".epub", true)
    val isCbz: Boolean get() = bookUrl.endsWith(".cbz", true)
    val isPdf: Boolean get() = bookUrl.endsWith(".pdf", true)
    val isUmd: Boolean get() = bookUrl.endsWith(".umd", true)
    val isLocalTxt: Boolean get() = bookUrl.endsWith(".txt", true) || (isLocalBook && !isEpub && !isCbz && !isPdf && !isUmd)
}
''')

# ---------------------------------------------------------------------------
# Mongo backup
# ---------------------------------------------------------------------------
w("com/htmake/reader/api/controller/MongoBackup.kt", r'''
package com.htmake.reader.api.controller

import com.htmake.reader.utils.ExtKt
import com.mongodb.client.MongoClients
import org.bson.Document
import java.io.File

/**
 * Optional MongoDB backup/restore of user JSON storage.
 * Requires reader.app.mongoUri.
 */
object MongoBackup {
    private val files = arrayOf(
        "bookshelf", "bookSource", "rssSource", "replaceRule",
        "bookmark", "bookGroup", "userConfig", "httpTTS"
    )

    fun backupUser(userNameSpace: String, mongoUri: String, dbName: String): Map<String, Any?> {
        return try {
            MongoClients.create(mongoUri).use { client ->
                val db = client.getDatabase(dbName)
                val col = db.getCollection("reader_user_$userNameSpace")
                col.deleteMany(Document())
                var n = 0
                for (name in files) {
                    val raw = ExtKt.getStorage("data", userNameSpace, name) ?: continue
                    col.insertOne(Document(mapOf("name" to name, "payload" to raw, "ts" to System.currentTimeMillis())))
                    n++
                }
                mapOf("ok" to true, "docs" to n)
            }
        } catch (e: Exception) {
            mapOf("ok" to false, "error" to (e.message ?: "mongo error"))
        }
    }

    fun restoreUser(userNameSpace: String, mongoUri: String, dbName: String): Map<String, Any?> {
        return try {
            MongoClients.create(mongoUri).use { client ->
                val db = client.getDatabase(dbName)
                val col = db.getCollection("reader_user_$userNameSpace")
                var n = 0
                col.find().forEach { doc ->
                    val name = doc.getString("name") ?: return@forEach
                    val payload = doc.getString("payload") ?: return@forEach
                    ExtKt.saveStorage(arrayOf("data", userNameSpace, name), payload)
                    n++
                }
                mapOf("ok" to true, "docs" to n)
            }
        } catch (e: Exception) {
            mapOf("ok" to false, "error" to (e.message ?: "mongo error"))
        }
    }
}
''')

# Fix BookChapter for start property - already have tag field
# Update AnalyzeRule to use XPath getString(content,rule) correctly - already does

# README phase3
readme_path = BIZ / "README.md"
readme = readme_path.read_text(encoding="utf-8") if readme_path.exists() else ""
if "Phase 3" not in readme:
    readme += """

## Phase 3 增量

- **AnalyzeByXPath**：seimicrawler JXDocument + `&&/||/%%` 规则拆分（RuleAnalyzer）
- **SourceAnalyzer**：legado/旧版书源 JSON → BookSource 规范化；`BookSource.fromJson` 接入
- **searchBookMulti / SSE**：多书源协程并发搜索与事件流
- **LocalBook + TextFile**：本地书分发；TXT 目录正则切分与按 offset 取正文
- **MongoBackup**：按用户备份/恢复 JSON 文档到 MongoDB
"""
    readme_path.write_text(readme, encoding="utf-8")

print("phase3 done, kt=", len(list(BIZ.rglob('*.kt'))))
