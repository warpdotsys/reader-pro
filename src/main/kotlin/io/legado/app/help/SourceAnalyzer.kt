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
            headerJson = str("header"),
            loginUrlValue = str("loginUrl"),
            loginUiValue = str("loginUi"),
            loginCheckJsValue = str("loginCheckJs"),
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
