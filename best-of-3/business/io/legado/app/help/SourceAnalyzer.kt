/** Business rewrite from reader-pro-3.2.14.jar — phase5. */

package io.legado.app.help

import com.google.gson.Gson
import com.google.gson.JsonObject
import com.google.gson.JsonParser
import com.google.gson.reflect.TypeToken
import io.legado.app.data.entities.BookSource
import io.legado.app.data.entities.rule.BookInfoRule
import io.legado.app.data.entities.rule.ContentRule
import io.legado.app.data.entities.rule.ExploreRule
import io.legado.app.data.entities.rule.SearchRule
import io.legado.app.data.entities.rule.TocRule

/**
 * Normalize legado / legacy book source JSON.
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

    /**
     * Legacy rule → new rule (from jar SourceAnalyzer.toNewRule):
     * - leading `-` / `+` preserved
     * - skip if already @CSS/@XPath/js/regex
     * - single `#` → `##` (replace delimiter)
     * - single `|` → `||` (or)
     * - single `&` → `&&` (and) when not URL
     */
    fun toNewRule(oldRule: String?): String? {
        if (oldRule.isNullOrBlank()) return null
        var rule = oldRule
        var reverse = false
        var allInOne = false
        if (rule.startsWith("-")) {
            reverse = true
            rule = rule.substring(1)
        }
        if (rule.startsWith("+")) {
            allInOne = true
            rule = rule.substring(1)
        }
        val skip =
            rule.startsWith("@CSS:", ignoreCase = true) ||
                rule.startsWith("@XPath:", ignoreCase = true) ||
                rule.startsWith("//") ||
                rule.startsWith("##") ||
                rule.startsWith(":") ||
                rule.contains("@js:", ignoreCase = true) ||
                rule.contains("<js>", ignoreCase = true)
        if (!skip) {
            if (rule.contains("#") && !rule.contains("##")) {
                // careful: id selectors like #id — jar converts all # to ## for replace chains
                rule = rule.replace("#", "##")
            }
            if (rule.contains("|") && !rule.contains("||")) {
                if (rule.contains("##")) {
                    val list = rule.split("##")
                    if (list[0].contains("|")) {
                        var rebuilt = list[0].replace("|", "||")
                        for (i in 1 until list.size) rebuilt += "##" + list[i]
                        rule = rebuilt
                    }
                } else {
                    rule = rule.replace("|", "||")
                }
            }
            if (rule.contains("&") && !rule.contains("&&") &&
                !rule.contains("http") && !rule.startsWith("/")
            ) {
                rule = rule.replace("&", "&&")
            }
        }
        if (allInOne) rule = "+$rule"
        if (reverse) rule = "-$rule"
        return rule
    }

    fun toNewUrl(url: String?): String? {
        if (url.isNullOrBlank()) return url
        return url
            .replace("searchKey", "{{key}}")
            .replace("{searchKey}", "{{key}}")
            .replace("searchPage", "{{page}}")
            .replace("{page}", "{{page}}")
            .replace("pageIndex", "{{page}}")
    }

    private fun str(o: JsonObject?, key: String): String? {
        if (o == null || !o.has(key) || o.get(key).isJsonNull) return null
        val e = o.get(key)
        return if (e.isJsonPrimitive) e.asString else e.toString()
    }

    private fun int(o: JsonObject, key: String): Int? =
        if (o.has(key) && o.get(key).isJsonPrimitive) runCatching { o.get(key).asInt }.getOrNull() else null

    private fun bool(o: JsonObject, key: String): Boolean? =
        if (o.has(key) && o.get(key).isJsonPrimitive) runCatching { o.get(key).asBoolean }.getOrNull() else null
}
