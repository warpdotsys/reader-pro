# -*- coding: utf-8 -*-
"""Phase 11: Cookie persist, loginUrl, explore polish, replace rules, API_INDEX."""
from pathlib import Path
import os
import re

BIZ = Path(r"C:\Users\chong\reader-pro-3.2.14-reverse\best-of-3\business")
H = "/** Business rewrite from reader-pro-3.2.14.jar — phase11. */\n\n"


def w(rel, c):
    p = BIZ / rel.replace("/", os.sep)
    p.parent.mkdir(parents=True, exist_ok=True)
    p.write_text(H + c.lstrip("\n"), encoding="utf-8", newline="\n")
    print("wrote", rel, p.stat().st_size)


# ---------------------------------------------------------------------------
# NetworkUtils getSubDomain (used by CookieStore)
# ---------------------------------------------------------------------------
# NetworkUtils is in AnalyzeRule.kt currently — move/extend helpers
w(
    "io/legado/app/utils/NetworkUtils.kt",
    r'''
package io.legado.app.utils

/**
 * URL helpers (legado NetworkUtils subset).
 */
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

    /** cookie 域名键：host 或末两段 subdomain */
    fun getSubDomain(url: String): String {
        val host = try {
            java.net.URL(if (url.contains("://")) url else "http://$url").host
        } catch (_: Exception) {
            return url
        }
        if (host.isBlank()) return ""
        val parts = host.split('.')
        return if (parts.size >= 2) parts.takeLast(2).joinToString(".") else host
    }
}
''',
)

# ---------------------------------------------------------------------------
# CookieStore — disk via ACache
# ---------------------------------------------------------------------------
w(
    "io/legado/app/help/http/CookieStore.kt",
    r'''
package io.legado.app.help.http

import com.htmake.reader.utils.ExtKt
import io.legado.app.utils.ACache
import io.legado.app.utils.NetworkUtils
import java.io.File

/**
 * Cookie 持久化：storage/cache/cookie/{user}/ 按 subdomain 存。
 * jar: CookieStore + ACache；JS 绑定 cookie.getCookie/setCookie/replaceCookie。
 */
class CookieStore(val userNameSpace: String) {

    private val cache: ACache = ACache.get(
        File(ExtKt.getWorkDir("storage", "cache", "cookie", userNameSpace))
    )

    fun setCookie(url: String, cookie: String?) {
        val domain = NetworkUtils.getSubDomain(url)
        if (domain.isEmpty()) return
        cache.put(domain, cookie ?: "")
    }

    fun replaceCookie(url: String, cookie: String) {
        if (url.isBlank() || cookie.isBlank()) return
        val old = getCookie(url)
        if (old.isBlank()) {
            setCookie(url, cookie)
            return
        }
        val map = cookieToMap(old)
        map.putAll(cookieToMap(cookie))
        setCookie(url, mapToCookie(map))
    }

    fun getCookie(url: String): String {
        val domain = NetworkUtils.getSubDomain(url)
        if (domain.isEmpty()) return ""
        return cache.getAsString(domain) ?: ""
    }

    fun getKey(url: String, key: String): String =
        cookieToMap(getCookie(url))[key] ?: ""

    fun removeCookie(url: String) {
        val domain = NetworkUtils.getSubDomain(url)
        if (domain.isNotEmpty()) cache.remove(domain)
    }

    fun cookieToMap(cookie: String): MutableMap<String, String> {
        val map = linkedMapOf<String, String>()
        if (cookie.isBlank()) return map
        cookie.split(';').map { it.trim() }.filter { it.isNotEmpty() }.forEach { part ->
            val i = part.indexOf('=')
            if (i > 0) {
                val k = part.substring(0, i).trim()
                val v = part.substring(i + 1).trim()
                if (k.isNotEmpty()) map[k] = v
            }
        }
        return map
    }

    fun mapToCookie(map: Map<String, String>): String =
        map.entries.joinToString("; ") { "${it.key}=${it.value}" }

    /** 合并响应 Set-Cookie 到存储 */
    fun applySetCookie(url: String, setCookieHeaders: List<String>) {
        if (setCookieHeaders.isEmpty()) return
        val pairs = setCookieHeaders.mapNotNull { line ->
            line.substringBefore(';').trim().takeIf { it.contains('=') }
        }
        if (pairs.isEmpty()) return
        replaceCookie(url, pairs.joinToString("; "))
    }
}
''',
)

# ---------------------------------------------------------------------------
# BaseSource + BookSource login
# ---------------------------------------------------------------------------
w(
    "io/legado/app/data/entities/BaseSource.kt",
    r'''
package io.legado.app.data.entities

import io.legado.app.help.CacheManager
import io.legado.app.model.analyzeRule.AnalyzeRule

/**
 * 书源/TTS/RSS 公共源接口。
 * loginHeader 存 CacheManager key = loginHeader_{getKey()}
 */
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
            "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/113.0.0.0 Safari/537.36"
        val h = getHeader()
        if (!h.isNullOrBlank()) {
            val json = when {
                h.startsWith("@js:", ignoreCase = true) ->
                    runCatching { AnalyzeRule().evalJS(h.substring(4), null)?.toString() }.getOrNull()
                h.startsWith("<js>", ignoreCase = true) -> {
                    val end = h.lastIndexOf('<')
                    val js = if (end > 4) h.substring(4, end) else h.removePrefix("<js>").removeSuffix("</js>")
                    runCatching { AnalyzeRule().evalJS(js, null)?.toString() }.getOrNull()
                }
                else -> h
            }
            if (!json.isNullOrBlank()) {
                runCatching {
                    val o = com.google.gson.JsonParser.parseString(json).asJsonObject
                    o.entrySet().forEach { map[it.key] = it.value.asString }
                }
            }
        }
        if (withLogin) {
            getLoginHeaderMap()?.let { map.putAll(it) }
        }
        return map
    }

    fun getLoginJs(): String? {
        val loginJs = getLoginUrl() ?: return null
        return when {
            loginJs.startsWith("@js:") -> loginJs.substring(4)
            loginJs.startsWith("<js>") -> {
                val end = loginJs.lastIndexOf('<')
                if (end > 4) loginJs.substring(4, end) else loginJs.removePrefix("<js>").removeSuffix("</js>")
            }
            else -> loginJs
        }
    }

    /** 执行 loginUrl 中的 JS（登录脚本） */
    fun login() {
        val js = getLoginJs() ?: return
        AnalyzeRule(null, this, null).evalJS(js, null)
    }

    fun getLoginHeader(): String? =
        CacheManager(getUserNameSpace()).get("loginHeader_${getKey()}")

    fun putLoginHeader(headerJson: String) {
        CacheManager(getUserNameSpace()).put("loginHeader_${getKey()}", headerJson, 0)
    }

    fun removeLoginHeader() {
        CacheManager(getUserNameSpace()).delete("loginHeader_${getKey()}")
    }

    fun getLoginHeaderMap(): Map<String, String>? {
        val raw = getLoginHeader() ?: return null
        return runCatching {
            val o = com.google.gson.JsonParser.parseString(raw).asJsonObject
            o.entrySet().associate { it.key to it.value.asString }
        }.getOrNull()
    }
}
''',
)

# Rewrite BookSource to implement full BaseSource
w(
    "io/legado/app/data/entities/BookSource.kt",
    r'''
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
    var variableComment: String? = null,
    private var _userNameSpace: String = "default"
) : BaseSource {

    override fun getKey(): String = bookSourceUrl
    override fun getTag(): String = bookSourceName
    override fun getHeader(): String? = header
    override fun getLoginUrl(): String? = loginUrl
    override fun getLoginUi(): String? = loginUi
    override fun getLoginCheckJs(): String? = loginCheckJs
    override fun getUserNameSpace(): String = _userNameSpace

    fun setUserNameSpace(ns: String) {
        _userNameSpace = ns
    }

    override fun getHeaderMap(withLogin: Boolean): Map<String, String> =
        super.getHeaderMap(withLogin)

    companion object {
        fun fromJson(json: String): Result<BookSource> = SourceAnalyzer.jsonToBookSource(json)
        fun fromJsonArray(json: String): Result<List<BookSource>> =
            SourceAnalyzer.jsonToBookSources(json).map { it.toList() }
    }
}
''',
)

# HttpTTS / RssSource should still implement BaseSource — check they don't break
# BookSource no longer defines interface BaseSource at bottom — old file had interface at end.
# HttpTTS implements BaseSource with only getHeaderMap — need getKey defaults on interface (OK)

# ---------------------------------------------------------------------------
# ExploreRule expand + BookList polish
# ---------------------------------------------------------------------------
w(
    "io/legado/app/data/entities/rule/Rules.kt",
    r'''
package io.legado.app.data.entities.rule

/** 搜索规则 */
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
    var lastChapter: String? = null,
    var wordCount: String? = null,
    var updateTime: String? = null
)

/** 发现规则（字段与 Search 列表项对齐） */
data class ExploreRule(
    var bookList: String? = null,
    var name: String? = null,
    var author: String? = null,
    var bookUrl: String? = null,
    var coverUrl: String? = null,
    var intro: String? = null,
    var kind: String? = null,
    var lastChapter: String? = null,
    var wordCount: String? = null,
    var updateTime: String? = null
)

data class BookInfoRule(
    var name: String? = null,
    var author: String? = null,
    var kind: String? = null,
    var coverUrl: String? = null,
    var intro: String? = null,
    var tocUrl: String? = null,
    var lastChapter: String? = null,
    var wordCount: String? = null
)

data class TocRule(
    var chapterList: String? = null,
    var chapterName: String? = null,
    var chapterUrl: String? = null,
    var nextTocUrl: String? = null,
    var preUpdateJs: String? = null,
    var isVolume: String? = null
)

data class ContentRule(
    var content: String? = null,
    var nextContentUrl: String? = null,
    var replaceRegex: String? = null,
    var imageStyle: String? = null
)
''',
)

w(
    "io/legado/app/model/webBook/BookList.kt",
    r'''
package io.legado.app.model.webBook

import io.legado.app.data.entities.BookSource
import io.legado.app.data.entities.SearchBook
import io.legado.app.data.entities.rule.ExploreRule
import io.legado.app.data.entities.rule.SearchRule
import io.legado.app.model.DebugLog
import io.legado.app.model.analyzeRule.AnalyzeRule
import io.legado.app.model.analyzeRule.AnalyzeUrl

/**
 * 搜索 / 发现列表解析。
 * explore: url 可含 {{page}}；列表规则优先 ruleExplore，字段缺省回落 ruleSearch。
 */
object BookList {

    suspend fun searchBook(
        bookSource: BookSource,
        key: String,
        page: Int = 1,
        debugLog: DebugLog? = null
    ): List<SearchBook> {
        val rule = bookSource.ruleSearch ?: return emptyList()
        val urlRule = rule.url ?: return emptyList()
        val analyzeUrl = AnalyzeUrl(
            mUrl = urlRule,
            key = key,
            page = page,
            source = bookSource
        )
        // loginCheckJs optional
        bookSource.loginCheckJs?.takeIf { it.isNotBlank() }?.let {
            // checked after response in AnalyzeUrl path; pre-login if loginUrl is JS
        }
        val html = analyzeUrl.getStrResponseAwait().body ?: return emptyList()
        return parseList(bookSource, html, analyzeUrl.finalUrl, rule, isSearch = true, debugLog)
    }

    suspend fun exploreBook(
        bookSource: BookSource,
        url: String,
        page: Int = 1,
        debugLog: DebugLog? = null
    ): List<SearchBook> {
        // url may be relative explore path; expand page
        val analyzeUrl = AnalyzeUrl(mUrl = url, page = page, source = bookSource)
        val html = analyzeUrl.getStrResponseAwait().body ?: return emptyList()
        val explore = bookSource.ruleExplore
        val search = bookSource.ruleSearch
        // build synthetic SearchRule from explore + search fallbacks
        val listRule = explore?.bookList ?: search?.bookList
        val synthetic = SearchRule(
            bookList = listRule,
            name = explore?.name ?: search?.name,
            author = explore?.author ?: search?.author,
            bookUrl = explore?.bookUrl ?: search?.bookUrl,
            coverUrl = explore?.coverUrl ?: search?.coverUrl,
            intro = explore?.intro ?: search?.intro,
            kind = explore?.kind ?: search?.kind,
            lastChapter = explore?.lastChapter ?: search?.lastChapter,
            wordCount = explore?.wordCount ?: search?.wordCount,
            updateTime = explore?.updateTime ?: search?.updateTime
        )
        return parseList(bookSource, html, analyzeUrl.finalUrl, synthetic, isSearch = false, debugLog)
    }

    /**
     * 解析 exploreUrl 文本为 (title,url) 列表。
     * 格式：标题::url  多行；或 JSON 数组。
     */
    fun parseExploreUrl(exploreUrl: String?, page: Int = 1): List<Pair<String, String>> {
        if (exploreUrl.isNullOrBlank()) return emptyList()
        val text = exploreUrl.replace("{{page}}", page.toString())
        // try JSON
        try {
            if (text.trimStart().startsWith("[")) {
                val arr = io.vertx.core.json.JsonArray(text)
                return (0 until arr.size()).mapNotNull { i ->
                    val v = arr.getValue(i)
                    when (v) {
                        is io.vertx.core.json.JsonObject -> {
                            val title = v.getString("title") ?: v.getString("name") ?: return@mapNotNull null
                            val u = v.getString("url") ?: return@mapNotNull null
                            title to u
                        }
                        is String -> {
                            val parts = v.split("::", limit = 2)
                            if (parts.size == 2) parts[0] to parts[1] else null
                        }
                        else -> null
                    }
                }
            }
        } catch (_: Exception) {
        }
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
        isSearch: Boolean,
        debugLog: DebugLog?
    ): List<SearchBook> {
        val bookListRule = rule.bookList ?: return emptyList()
        val analyze = AnalyzeRule(null, bookSource, debugLog)
        analyze.setContent(html, baseUrl)
        // allInOne when rule starts with :
        val allInOne = bookListRule.trimStart().startsWith(":")
        val els = if (allInOne) {
            // regex list returns strings as "elements"
            analyze.getStringList(bookListRule, allInOne = true).map { it as Any }
        } else {
            analyze.getElements(bookListRule)
        }
        debugLog?.log(bookSource.bookSourceUrl, "${if (isSearch) "搜索" else "发现"}列表 ${els.size} 条")
        return els.mapNotNull { el ->
            analyze.setContent(el, baseUrl)
            val name = rule.name?.let { analyze.getString(it) }?.takeIf { it.isNotBlank() }
                ?: return@mapNotNull null
            val bookUrl = rule.bookUrl?.let { analyze.getString(it, isUrl = true) }?.ifBlank { baseUrl } ?: baseUrl
            SearchBook(
                name = name,
                author = rule.author?.let { analyze.getString(it) } ?: "",
                bookUrl = bookUrl,
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
''',
)

# Fix AnalyzeRule getStringList / getElements to accept allInOne via splitSourceRule
# Patch AnalyzeRule - add overload or param
ar_path = BIZ / "io/legado/app/model/analyzeRule/AnalyzeRule.kt"
ar = ar_path.read_text(encoding="utf-8")
if "fun getStringList(rule: String?, mContent: Any? = null, isUrl: Boolean = false, allInOne: Boolean = false)" not in ar:
    ar = ar.replace(
        "fun getStringList(rule: String?, mContent: Any? = null, isUrl: Boolean = false): List<String> {\n"
        "        if (rule.isNullOrEmpty()) return emptyList()\n"
        "        val rules = splitSourceRule(rule)",
        "fun getStringList(rule: String?, mContent: Any? = null, isUrl: Boolean = false, allInOne: Boolean = false): List<String> {\n"
        "        if (rule.isNullOrEmpty()) return emptyList()\n"
        "        val rules = splitSourceRule(rule, allInOne)",
    )
    ar = ar.replace(
        "fun getElements(ruleStr: String): List<Any> {\n"
        "        if (ruleStr.isEmpty()) return emptyList()\n"
        "        val rules = splitSourceRule(ruleStr)",
        "fun getElements(ruleStr: String, allInOne: Boolean = false): List<Any> {\n"
        "        if (ruleStr.isEmpty()) return emptyList()\n"
        "        val rules = splitSourceRule(ruleStr, allInOne)",
    )
    # Prefer utils NetworkUtils if we now have two — keep AnalyzeRule NetworkUtils or replace
    if "object NetworkUtils" in ar and (BIZ / "io/legado/app/utils/NetworkUtils.kt").exists():
        ar = ar.replace(
            "return if (isUrl) NetworkUtils.getAbsoluteURL(baseUrl, s) else s",
            "return if (isUrl) io.legado.app.utils.NetworkUtils.getAbsoluteURL(baseUrl, s) else s",
        )
        ar = ar.replace(
            "return if (isUrl) list.map { NetworkUtils.getAbsoluteURL(baseUrl, it) } else list",
            "return if (isUrl) list.map { io.legado.app.utils.NetworkUtils.getAbsoluteURL(baseUrl, it) } else list",
        )
        # remove trailing object NetworkUtils from AnalyzeRule to avoid conflict
        ar = re.sub(
            r"\nobject NetworkUtils \{.*?\n\}\n?\Z",
            "\n",
            ar,
            flags=re.S,
        )
    ar_path.write_text(ar, encoding="utf-8", newline="\n")
    print("patched AnalyzeRule allInOne + NetworkUtils")

# SearchBook fields
sb = BIZ / "io/legado/app/data/entities/SearchBook.kt"
st = sb.read_text(encoding="utf-8")
if "originName" not in st:
    st = st.replace(
        "var origin: String = \"\",",
        "var origin: String = \"\",\n    var originName: String = \"\",",
    )
if "latestChapterTitle" not in st:
    st = st.replace(
        "var intro: String? = null",
        "var intro: String? = null,\n    var kind: String? = null,\n    var latestChapterTitle: String? = null",
    )
    if "var kind" in st and st.count("var kind") > 1:
        pass  # careful
    sb.write_text(st, encoding="utf-8", newline="\n")
    print("patched SearchBook")
else:
    if "var kind" not in st:
        st = st.replace("var intro: String? = null", "var intro: String? = null,\n    var kind: String? = null")
        sb.write_text(st, encoding="utf-8", newline="\n")

# ---------------------------------------------------------------------------
# AnalyzeUrl: Cookie header + Set-Cookie + loginCheckJs
# ---------------------------------------------------------------------------
w(
    "io/legado/app/model/analyzeRule/AnalyzeUrl.kt",
    r'''
package io.legado.app.model.analyzeRule

import io.legado.app.data.entities.BaseSource
import io.legado.app.data.entities.Book
import io.legado.app.data.entities.BookChapter
import io.legado.app.help.http.CookieStore
import io.legado.app.help.http.StrResponse
import io.legado.app.model.DebugLog
import okhttp3.MediaType
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody
import okhttp3.Response
import java.util.concurrent.TimeUnit

/**
 * URL 模板 + HTTP。注入 CookieStore；保存 Set-Cookie；可选 loginCheckJs。
 */
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
        if (key != null) {
            u = u.replace("{{key}}", key).replace("{{keyword}}", key)
        }
        if (page != null) u = u.replace("{{page}}", page.toString())
        if (speakText != null) u = u.replace("{{speakText}}", speakText)
        return u
    }

    private fun client(): OkHttpClient = OkHttpClient.Builder()
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
        source?.getHeaderMap(withLogin = true)?.let { map.putAll(it) }
        headerMapF?.let { map.putAll(it) }
        // Cookie
        cookieStore()?.let { cs ->
            val c = cs.getCookie(finalUrl)
            if (c.isNotBlank() && !map.keys.any { it.equals("Cookie", true) }) {
                map["Cookie"] = c
            }
        }
        return map
    }

    suspend fun getStrResponseAwait(): StrResponse {
        client().newCall(buildRequest()).execute().use { resp ->
            finalUrl = resp.request().url().toString()
            saveCookies(resp)
            var bodyStr = resp.body()?.string()
            // loginCheckJs on source
            val checkJs = source?.getLoginCheckJs()
            if (!checkJs.isNullOrBlank()) {
                val evaluated = evalJS(checkJs, resp)
                when (evaluated) {
                    is Response -> {
                        return getStrResponseAwait() // rare re-fetch path simplified
                    }
                    is StrResponse -> return evaluated
                    is String -> bodyStr = evaluated
                }
            }
            return StrResponse(finalUrl, bodyStr)
        }
    }

    suspend fun getByteArrayAwait(): ByteArray {
        client().newCall(buildRequest()).execute().use { resp ->
            finalUrl = resp.request().url().toString()
            saveCookies(resp)
            return resp.body()?.bytes() ?: ByteArray(0)
        }
    }

    suspend fun getResponseAwait(): Response {
        val resp = client().newCall(buildRequest()).execute()
        finalUrl = resp.request().url().toString()
        saveCookies(resp)
        return resp
    }

    private fun saveCookies(resp: Response) {
        val cs = cookieStore() ?: return
        val setCookies = resp.headers("Set-Cookie")
        if (setCookies.isNotEmpty()) {
            cs.applySetCookie(finalUrl, setCookies)
        }
    }

    private fun buildRequest(): Request {
        val b = Request.Builder().url(finalUrl)
        headers().forEach { (k, v) -> b.header(k, v) }
        return when (method.uppercase()) {
            "POST" -> b.post(
                RequestBody.create(MediaType.parse("application/json; charset=utf-8"), body ?: "")
            ).build()
            else -> b.get().build()
        }
    }

    fun evalJS(js: String, result: Any?): Any? =
        AnalyzeRule(ruleData as? io.legado.app.model.analyzeRule.RuleDataInterface, source, debugLog)
            .evalJS(js, result)
}
''',
)

# ---------------------------------------------------------------------------
# exploreBook API: also accept sortUrl from exploreUrl list
# ---------------------------------------------------------------------------
# patch explore in BookControllerExtras to pass userNameSpace to source
ex_path = BIZ / "com/htmake/reader/api/controller/BookControllerExtras.kt"
ex = ex_path.read_text(encoding="utf-8")
old_explore = '''suspend fun BookController.exploreBook(context: RoutingContext): ReturnData {
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
    val list = withTimeoutOrNull(30_000L) {
        WebBook(sourceStr, getAppConfig().debugLog, null, ns).exploreBook(url, page)
    } ?: emptyList()
    return rd.setData(list)
}'''

new_explore = '''suspend fun BookController.exploreBook(context: RoutingContext): ReturnData {
    val rd = ReturnData()
    if (!checkAuth(context)) return rd.setData("NEED_LOGIN").setErrorMsg("请登录后使用")
    var url = context.queryParam("url").firstOrNull()
        ?: context.bodyAsJson?.getString("url")
        ?: context.queryParam("sortUrl").firstOrNull()
        ?: context.bodyAsJson?.getString("sortUrl")
        ?: ""
    val page = context.queryParam("page").firstOrNull()?.toIntOrNull()
        ?: context.bodyAsJson?.getInteger("page") ?: 1
    val ns = getUserNameSpace(context)
    val sourceUrl = context.queryParam("bookSourceUrl").firstOrNull()
        ?: context.bodyAsJson?.getString("bookSourceUrl")
    val sourceStr = sourceUrl?.let { getBookSourceStringBySourceURLOpt(it, ns) }
        ?: return rd.setErrorMsg("书源信息错误")
    // 若只给了书源、未给 url：返回 exploreUrl 分类列表
    if (url.isBlank()) {
        val src = io.legado.app.data.entities.BookSource.fromJson(sourceStr).getOrNull()
        val cats = io.legado.app.model.webBook.BookList.parseExploreUrl(src?.exploreUrl, page)
        return rd.setData(cats.map { (title, u) -> mapOf("title" to title, "url" to u) })
    }
    val list = withTimeoutOrNull(30_000L) {
        WebBook(sourceStr, getAppConfig().debugLog, null, ns).exploreBook(url, page)
    } ?: emptyList()
    return rd.setData(list)
}'''

if old_explore in ex:
    ex = ex.replace(old_explore, new_explore)
    ex_path.write_text(ex, encoding="utf-8", newline="\n")
    print("exploreBook sortUrl + explore categories")
else:
    print("WARN: exploreBook block not matched")

# ---------------------------------------------------------------------------
# ReplaceRuleController full CRUD + ContentProcessor scope all
# ---------------------------------------------------------------------------
w(
    "com/htmake/reader/api/controller/ReplaceRuleController.kt",
    r'''
package com.htmake.reader.api.controller

import com.htmake.reader.api.ReturnData
import com.htmake.reader.utils.ExtKt
import io.vertx.core.json.JsonArray
import io.vertx.core.json.JsonObject
import io.vertx.ext.web.RoutingContext
import kotlin.coroutines.CoroutineContext

/**
 * 替换规则 CRUD。
 * 字段：name, pattern/regex, replacement, isRegex, isEnabled, scope(content|title|all),
 *       timeout/timeoutMillisecond, bookName
 */
class ReplaceRuleController(coroutineContext: CoroutineContext) : BaseController(coroutineContext) {
    private fun load(ns: String) = ExtKt.asJsonArray(getUserStorage(ns, "replaceRule")) ?: JsonArray()
    private fun save(ns: String, a: JsonArray) = saveUserStorage(ns, "replaceRule", a)

    suspend fun getReplaceRules(context: RoutingContext): ReturnData {
        val rd = ReturnData()
        if (!checkAuth(context)) return rd.setData("NEED_LOGIN").setErrorMsg("请登录后使用")
        return rd.setData(load(getUserNameSpace(context)))
    }

    suspend fun saveReplaceRule(context: RoutingContext): ReturnData {
        val rd = ReturnData()
        if (!checkAuth(context)) return rd.setData("NEED_LOGIN").setErrorMsg("请登录后使用")
        val ns = getUserNameSpace(context)
        // multi
        val arrBody = context.bodyAsJsonArray
        if (arrBody != null) {
            save(ns, normalizeArray(arrBody))
            return rd.setData(arrBody.size())
        }
        val rule = context.bodyAsJson ?: return rd.setErrorMsg("参数错误")
        normalizeRule(rule)
        val name = rule.getString("name") ?: ""
        val arr = load(ns)
        val list = arr.list
        var found = false
        if (name.isNotEmpty()) {
            for (i in list.indices) {
                val o = arr.getJsonObject(i)
                if (o.getString("name") == name) {
                    list[i] = rule
                    found = true
                    break
                }
            }
        }
        if (!found) list.add(rule)
        save(ns, JsonArray(list))
        return rd.setData(rule)
    }

    suspend fun deleteReplaceRule(context: RoutingContext): ReturnData {
        val rd = ReturnData()
        if (!checkAuth(context)) return rd.setData("NEED_LOGIN").setErrorMsg("请登录后使用")
        val ns = getUserNameSpace(context)
        val body = context.bodyAsJson
        val names = mutableSetOf<String>()
        body?.getString("name")?.let { names += it }
        body?.getJsonArray("names")?.forEach { names += it.toString() }
        context.queryParam("name").forEach { names += it }
        if (names.isEmpty() && context.bodyAsJsonArray != null) {
            context.bodyAsJsonArray.forEach { v ->
                when (v) {
                    is JsonObject -> v.getString("name")?.let { names += it }
                    is String -> names += v
                }
            }
        }
        if (names.isEmpty()) return rd.setErrorMsg("name 不能为空")
        val arr = load(ns)
        val list = arr.list.filterIndexed { i, _ ->
            arr.getJsonObject(i).getString("name") !in names
        }
        save(ns, JsonArray(list))
        return rd.setData(true)
    }

    private fun normalizeArray(arr: JsonArray): JsonArray {
        for (i in 0 until arr.size()) {
            arr.getJsonObject(i)?.let { normalizeRule(it) }
        }
        return arr
    }

    private fun normalizeRule(o: JsonObject) {
        if (!o.containsKey("pattern") && o.containsKey("regex")) {
            o.put("pattern", o.getString("regex"))
        }
        if (!o.containsKey("isEnabled") && o.containsKey("enable")) {
            o.put("isEnabled", o.getBoolean("enable", true))
        }
        if (!o.containsKey("timeout") && o.containsKey("timeoutMillisecond")) {
            o.put("timeout", o.getLong("timeoutMillisecond"))
        }
        if (!o.containsKey("scope")) o.put("scope", "content")
        if (!o.containsKey("timeout")) o.put("timeout", 3000)
    }
}
''',
)

# ContentProcessor: scope=all applies to both; bookName regex prefix
cp = BIZ / "io/legado/app/help/ContentProcessor.kt"
cpt = cp.read_text(encoding="utf-8")
if 'scope == "all"' not in cpt:
    cpt = cpt.replace(
        '''    fun applyContent(userNameSpace: String, book: Book?, content: String): String {
        var text = content
        val rules = loadRules(userNameSpace).filter {
            it.isEnabled && it.scope != "title" && matchesBook(it, book)
        }''',
        '''    fun applyContent(userNameSpace: String, book: Book?, content: String): String {
        var text = content
        val rules = loadRules(userNameSpace).filter {
            it.isEnabled && (it.scope == "content" || it.scope == "all" || it.scope.isBlank()) && matchesBook(it, book)
        }''',
    )
    cpt = cpt.replace(
        '''        val rules = loadRules(userNameSpace).filter {
            it.isEnabled && it.scope == "title" && matchesBook(it, book)
        }''',
        '''        val rules = loadRules(userNameSpace).filter {
            it.isEnabled && (it.scope == "title" || it.scope == "all") && matchesBook(it, book)
        }''',
    )
    cpt = cpt.replace(
        '''    private fun matchesBook(rule: ReplaceRule, book: Book?): Boolean {
        if (rule.bookName.isBlank()) return true
        val name = book?.name ?: return true
        return name.contains(rule.bookName) || name.matches(Regex(rule.bookName))
    }''',
        '''    private fun matchesBook(rule: ReplaceRule, book: Book?): Boolean {
        if (rule.bookName.isBlank()) return true
        val name = book?.name ?: return true
        val filter = rule.bookName
        // regex: prefix  or /pattern/
        return when {
            filter.startsWith("regex:") -> runCatching {
                name.contains(Regex(filter.removePrefix("regex:")))
            }.getOrDefault(false)
            filter.startsWith("/") && filter.endsWith("/") && filter.length > 2 -> runCatching {
                Regex(filter.substring(1, filter.length - 1)).containsMatchIn(name)
            }.getOrDefault(false)
            else -> name.contains(filter) || runCatching { name.matches(Regex(filter)) }.getOrDefault(false)
        }
    }''',
    )
    # also load scope "all" from json
    if 'o.get("scope")?.asString ?: "content"' in cpt:
        pass
    cp.write_text(cpt, encoding="utf-8", newline="\n")
    print("ContentProcessor scope=all + bookName regex")

# ---------------------------------------------------------------------------
# WebBook: set userNameSpace on source when loading
# ---------------------------------------------------------------------------
wb = BIZ / "io/legado/app/model/webBook/WebBook.kt"
wbt = wb.read_text(encoding="utf-8")
if "setUserNameSpace" not in wbt:
    wbt = wbt.replace(
        '''    private val source: BookSource by lazy {
        BookSource.fromJson(bookSourceStr).getOrThrow()
    }''',
        '''    private val source: BookSource by lazy {
        BookSource.fromJson(bookSourceStr).getOrThrow().also {
            it.setUserNameSpace(userNameSpace)
        }
    }''',
    )
    wb.write_text(wbt, encoding="utf-8", newline="\n")
    print("WebBook sets source userNameSpace")

# ---------------------------------------------------------------------------
# API_INDEX one-pager
# ---------------------------------------------------------------------------
routes = Path(r"C:\Users\chong\reader-pro-3.2.14-reverse\API_ROUTES.md")
route_lines = []
if routes.exists():
    for line in routes.read_text(encoding="utf-8", errors="replace").splitlines():
        line = line.strip()
        if line.startswith("- `") and "/reader3/" in line:
            route_lines.append(line.strip("- `").strip("`"))

api_index = BIZ / "API_INDEX.md"
route_preview = "\n".join(route_lines[:40])
n_routes = len(route_lines)
api_index.write_text(
    """# business API 对照一页纸

> 生成自 phase11。完整 133 路由见仓库根 `API_ROUTES.md`。  
> 实现优先读 `business/`；边界对照 `best-of-3/src/`。

## 核心模块

| 领域 | business 入口 | 关键能力 |
|------|---------------|----------|
| 路由 | `YueduApi.kt` | `/reader3/*` 挂载 |
| 书架/正文 | `BookController` + `BookControllerExtras` | 搜索/目录/正文/缓存 SSE/导出 |
| 书源 | `BookSourceController` | CRUD、远程导入、失效缓存 |
| 用户 | `UserController` + Extras | 登录、userConfig 主题、上传 |
| TTS | `BookTts` + `EdgeTts` | edge / api / textToSpeechCn |
| WebDAV | `WebdavController` + `WebdavPaths` | PROPFIND… + 备份 zip |
| 规则引擎 | `AnalyzeRule` / `AnalyzeUrl` | CSS/XPath/JSON/JS/Regex + Cookie |
| 列表 | `BookList` | 搜索 + 发现 + exploreUrl 分类 |
| 替换 | `ContentProcessor` + `ReplaceRuleController` | scope/timeout/bookName |
| RSS | `Rss` + `RssSourceController` | 规则 / 默认 XML |
| 本地书 | `LocalBook` / Epub/Txt/Pdf/Cbz | 目录与正文 |

## 用户配置键（userConfig）

见 `com/htmake/reader/config/UserConfig.kt`：`theme` `fontFamily` `fontSize` `pageMode` `ttsType` `searchConcurrent` …

## 书源登录

1. `loginUrl` 为 `@js:` / `<js>` → `BaseSource.login()`  
2. `loginHeader_` + bookSourceUrl 存 `CacheManager`  
3. `getHeaderMap(withLogin=true)` 合并 loginHeader  
4. `AnalyzeUrl` 自动带 Cookie，并写回 `Set-Cookie`

## Cookie 路径

`storage/cache/cookie/{user}/` — 按 subdomain 文件（ACache）

## 失效书源

`storage/cache/invalidBookSourceCache/{user}/` — TTL 600s  
目录拉取失败、多源搜索异常时写入；搜索时跳过。

## 替换规则 scope

| scope | 作用 |
|-------|------|
| content / 空 | 正文 |
| title | 章节标题 |
| all | 正文 + 标题 |
| timeout | 单规则毫秒，默认 3000 |
| bookName | 书名包含；`regex:…` 或 `/pat/` |

## 发现 explore

- `GET/POST /reader3/exploreBook?bookSourceUrl=&url=&page=`  
- `url` 空：返回 `exploreUrl` 解析的分类 title/url  
- `url` / `sortUrl`：抓取列表，`ruleExplore` 优先  

## 路由摘录（"""
    + str(n_routes)
    + """ 条来自 API_ROUTES）

```
"""
    + route_preview
    + """
…
```

完整列表：`../../API_ROUTES.md`
""",
    encoding="utf-8",
    newline="\n",
)
print("wrote API_INDEX.md")

# README phase11
readme = BIZ / "README.md"
r = readme.read_text(encoding="utf-8", errors="replace")
if "Phase 11" not in r:
    r = r.rstrip() + """


## Phase 11 增量

- **CookieStore**：磁盘 ACache 按 subdomain 持久化；`replaceCookie` / Set-Cookie 合并
- **BaseSource**：loginUrl / loginCheckJs / loginHeader / getHeaderMap(withLogin)
- **BookSource**：exploreUrl、enabledExplore、login* 字段；`setUserNameSpace`
- **AnalyzeUrl**：请求带 Cookie；响应写 Cookie；loginCheckJs 钩子
- **BookList.explore**：ruleExplore 字段回落 ruleSearch；allInOne 列表；`parseExploreUrl`
- **exploreBook API**：无 url 时返回发现分类；支持 sortUrl
- **ReplaceRuleController**：按 name 增改删；normalize scope/timeout
- **ContentProcessor**：scope=`all`；bookName 支持 `regex:` 与 `/pat/`
- **API_INDEX.md**：业务一页纸对照
"""
    readme.write_text(r, encoding="utf-8", newline="\n")
    print("README phase11")

# INDEX
index = BIZ / "INDEX.md"
ix = index.read_text(encoding="utf-8", errors="replace")
if "API_INDEX" not in ix:
    ix = ix.rstrip() + "\n\n详见 **API_INDEX.md**（phase11 一页纸）。\n"
kt = sum(1 for _ in BIZ.rglob("*.kt"))
lines = sum(len(p.read_text(encoding="utf-8", errors="replace").splitlines()) for p in BIZ.rglob("*.kt"))
ix = ix.rstrip() + f"\n- phase11 后 business `.kt` 文件数: **{kt}** / 约 **{lines}** 行\n"
index.write_text(ix, encoding="utf-8", newline="\n")
print(f"DONE phase11: {kt} files, ~{lines} lines")
