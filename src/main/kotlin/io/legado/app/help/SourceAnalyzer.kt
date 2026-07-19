package io.legado.app.help

import com.jayway.jsonpath.JsonPath
import io.legado.app.data.entities.BookSource
import io.legado.app.data.entities.rule.BookInfoRule
import io.legado.app.data.entities.rule.ContentRule
import io.legado.app.data.entities.rule.ExploreRule
import io.legado.app.data.entities.rule.SearchRule
import io.legado.app.data.entities.rule.TocRule
import io.legado.app.exception.NoStackTraceException
import io.legado.app.model.Debug
import io.legado.app.utils.GSON
import io.legado.app.utils.fromJsonObject
import io.legado.app.utils.isJsonArray
import io.legado.app.utils.isJsonObject
import io.legado.app.utils.jsonPath
import io.legado.app.utils.readBool
import io.legado.app.utils.readInt
import io.legado.app.utils.readString
import java.io.InputStream
import java.util.regex.Pattern

object SourceAnalyzer {
    private val headerPattern = Pattern.compile("@Header:\\{.+?\\}", Pattern.CASE_INSENSITIVE)
    private val jsPattern = Pattern.compile("\\{\\{.+?\\}\\}", Pattern.CASE_INSENSITIVE)

    fun jsonToBookSources(json: String): Result<MutableList<BookSource>> = kotlin.runCatching {
        val bookSources = mutableListOf<BookSource>()
        if (json.isJsonArray()) {
            val items: List<Map<*, *>> = jsonPath.parse(json).read("$")
            for (item in items) {
                val jsonItem = jsonPath.parse(item)
                bookSources.add(jsonToBookSource(jsonItem.jsonString()).getOrThrow())
            }
        } else {
            if (!json.isJsonObject()) {
                throw NoStackTraceException("\u683c\u5f0f\u4e0d\u5bf9")
            }
            bookSources.add(jsonToBookSource(json).getOrThrow())
        }
        bookSources
    }

    fun jsonToBookSources(inputStream: InputStream): Result<MutableList<BookSource>> = kotlin.runCatching {
        val bookSources = mutableListOf<BookSource>()
        kotlin.runCatching {
            val items: List<Map<*, *>> = jsonPath.parse(inputStream).read("$")
            for (item in items) {
                val jsonItem = jsonPath.parse(item)
                bookSources.add(jsonToBookSource(jsonItem.jsonString()).getOrThrow())
            }
        }.onFailure {
            val item: Map<*, *> = jsonPath.parse(inputStream).read("$")
            val jsonItem = jsonPath.parse(item)
            bookSources.add(jsonToBookSource(jsonItem.jsonString()).getOrThrow())
        }
        bookSources
    }

    fun jsonToBookSource(json: String): Result<BookSource> {
        val source = BookSource()
        val sourceAny = GSON.fromJsonObject<BookSourceAny>(json.trim()).onFailure {
            Debug.log("\u8f6c\u5316\u4e66\u6e90\u51fa\u9519", it.localizedMessage)
        }.getOrNull()
        return kotlin.runCatching {
            if (sourceAny?.ruleToc != null) {
                source.bookSourceUrl = sourceAny.bookSourceUrl
                source.bookSourceName = sourceAny.bookSourceName
                source.bookSourceGroup = sourceAny.bookSourceGroup
                source.bookSourceType = sourceAny.bookSourceType
                source.bookUrlPattern = sourceAny.bookUrlPattern
                source.customOrder = sourceAny.customOrder
                source.enabled = sourceAny.enabled
                source.enabledExplore = sourceAny.enabledExplore
                source.enabledCookieJar = sourceAny.enabledCookieJar
                source.concurrentRate = sourceAny.concurrentRate
                source.header = sourceAny.header
                source.loginUrl = sourceAny.loginUrl?.let {
                    if (it is String) it.toString() else JsonPath.parse(it).readString("url")
                }
                source.loginCheckJs = sourceAny.loginCheckJs
                source.bookSourceComment = sourceAny.bookSourceComment
                source.lastUpdateTime = sourceAny.lastUpdateTime
                source.respondTime = sourceAny.respondTime
                source.weight = sourceAny.weight
                source.exploreUrl = sourceAny.exploreUrl
                source.ruleExplore = if (sourceAny.ruleExplore is String) {
                    GSON.fromJsonObject<ExploreRule>(sourceAny.ruleExplore.toString()).getOrNull()
                } else {
                    GSON.fromJsonObject<ExploreRule>(GSON.toJson(sourceAny.ruleExplore)).getOrNull()
                }
                source.searchUrl = sourceAny.searchUrl
                source.ruleSearch = if (sourceAny.ruleSearch is String) {
                    GSON.fromJsonObject<SearchRule>(sourceAny.ruleSearch.toString()).getOrNull()
                } else {
                    GSON.fromJsonObject<SearchRule>(GSON.toJson(sourceAny.ruleSearch)).getOrNull()
                }
                source.ruleBookInfo = if (sourceAny.ruleBookInfo is String) {
                    GSON.fromJsonObject<BookInfoRule>(sourceAny.ruleBookInfo.toString()).getOrNull()
                } else {
                    GSON.fromJsonObject<BookInfoRule>(GSON.toJson(sourceAny.ruleBookInfo)).getOrNull()
                }
                source.ruleToc = if (sourceAny.ruleToc is String) {
                    GSON.fromJsonObject<TocRule>(sourceAny.ruleToc.toString()).getOrNull()
                } else {
                    GSON.fromJsonObject<TocRule>(GSON.toJson(sourceAny.ruleToc)).getOrNull()
                }
                source.ruleContent = if (sourceAny.ruleContent is String) {
                    GSON.fromJsonObject<ContentRule>(sourceAny.ruleContent.toString()).getOrNull()
                } else {
                    GSON.fromJsonObject<ContentRule>(GSON.toJson(sourceAny.ruleContent)).getOrNull()
                }
            } else {
                val jsonItem = jsonPath.parse(json.trim())
                val sourceType = jsonItem.readString("bookSourceUrl")
                    ?: throw NoStackTraceException("\u683c\u5f0f\u4e0d\u5bf9")
                source.bookSourceUrl = sourceType
                source.bookSourceName = jsonItem.readString("bookSourceName") ?: ""
                source.bookSourceGroup = jsonItem.readString("bookSourceGroup")
                source.bookSourceComment = jsonItem.readString("bookSourceComment") ?: ""
                source.bookUrlPattern = jsonItem.readString("ruleBookUrlPattern")
                source.customOrder = jsonItem.readInt("serialNumber") ?: 0
                source.header = uaToHeader(jsonItem.readString("httpUserAgent"))
                source.searchUrl = toNewUrl(jsonItem.readString("ruleSearchUrl"))
                source.exploreUrl = toNewUrls(jsonItem.readString("ruleFindUrl"))
                source.bookSourceType = when (jsonItem.readString("bookSourceType")) {
                    "1" -> 1
                    "2" -> 2
                    "3" -> 3
                    "FILE", "file" -> 3
                    "AUDIO", "audio" -> 1
                    "IMAGE", "image" -> 2
                    else -> 0
                }
                source.enabled = jsonItem.readBool("enable") ?: true
                if (source.exploreUrl.isNullOrBlank()) {
                    source.enabledExplore = false
                }
                var content = toNewRule(jsonItem.readString("ruleSearchList"))
                source.ruleSearch = SearchRule(
                    bookList = content,
                    name = toNewRule(jsonItem.readString("ruleSearchName")),
                    author = toNewRule(jsonItem.readString("ruleSearchAuthor")),
                    intro = toNewRule(jsonItem.readString("ruleSearchIntroduce")),
                    kind = toNewRule(jsonItem.readString("ruleSearchKind")),
                    lastChapter = toNewRule(jsonItem.readString("ruleSearchLastChapter")),
                    bookUrl = toNewRule(jsonItem.readString("ruleSearchNoteUrl")),
                    coverUrl = toNewRule(jsonItem.readString("ruleSearchCoverUrl"))
                )
                source.ruleExplore = ExploreRule(
                    bookList = toNewRule(jsonItem.readString("ruleFindList")),
                    name = toNewRule(jsonItem.readString("ruleFindName")),
                    author = toNewRule(jsonItem.readString("ruleFindAuthor")),
                    intro = toNewRule(jsonItem.readString("ruleFindIntroduce")),
                    kind = toNewRule(jsonItem.readString("ruleFindKind")),
                    lastChapter = toNewRule(jsonItem.readString("ruleFindLastChapter")),
                    bookUrl = toNewRule(jsonItem.readString("ruleFindNoteUrl")),
                    coverUrl = toNewRule(jsonItem.readString("ruleFindCoverUrl"))
                )
                source.ruleBookInfo = BookInfoRule(
                    init = toNewRule(jsonItem.readString("ruleBookInfoInit")),
                    name = toNewRule(jsonItem.readString("ruleBookName")),
                    author = toNewRule(jsonItem.readString("ruleBookAuthor")),
                    intro = toNewRule(jsonItem.readString("ruleIntroduce")),
                    kind = toNewRule(jsonItem.readString("ruleBookKind")),
                    lastChapter = toNewRule(jsonItem.readString("ruleBookLastChapter")),
                    coverUrl = toNewRule(jsonItem.readString("ruleCoverUrl")),
                    tocUrl = toNewRule(jsonItem.readString("ruleChapterUrl"))
                )
                source.ruleToc = TocRule(
                    chapterList = toNewRule(jsonItem.readString("ruleChapterList")),
                    chapterName = toNewRule(jsonItem.readString("ruleChapterName")),
                    chapterUrl = toNewRule(jsonItem.readString("ruleContentUrl")),
                    nextTocUrl = toNewRule(jsonItem.readString("ruleChapterUrlNext"))
                )
                content = toNewRule(jsonItem.readString("ruleBookContent")) ?: ""
                if (content.startsWith("$") && !content.startsWith("$.")) {
                    content = content.substring(1)
                }
                source.ruleContent = ContentRule(
                    content = content,
                    nextContentUrl = toNewRule(jsonItem.readString("ruleContentUrlNext")),
                    replaceRegex = toNewRule(jsonItem.readString("ruleBookContentReplace"))
                )
            }
            source
        }
    }

    /**
     * 旧规则转新规则
     */
    private fun toNewRule(oldRule: String?): String? {
        val sourceRule = oldRule ?: return null
        if (sourceRule.isBlank()) {
            return null
        }
        var newRule = sourceRule
        var reverse = false
        var allinone = false
        if (oldRule.startsWith("-")) {
            reverse = true
            newRule = oldRule.substring(1)
        }
        if (newRule.startsWith("+")) {
            allinone = true
            newRule = newRule.substring(1)
        }
        if (!newRule.startsWith("@CSS:", ignoreCase = true) &&
            !newRule.startsWith("@XPath:", ignoreCase = true) &&
            !newRule.startsWith("//") &&
            !newRule.startsWith("##") &&
            !newRule.startsWith(":") &&
            !newRule.contains("@js:", ignoreCase = true) &&
            !newRule.contains("<js>", ignoreCase = true)
        ) {
            if (newRule.contains("#") && !newRule.contains("##")) {
                newRule = sourceRule.replace("#", "##")
            }
            if (newRule.contains("|") && !newRule.contains("||")) {
                if (newRule.contains("##")) {
                    val list = newRule.split("##")
                    if (list[0].contains("|")) {
                        newRule = list[0].replace("|", "||")
                        for (i in 1 until list.size) {
                            newRule = newRule + "##" + list[i]
                        }
                    }
                } else {
                    newRule = newRule.replace("|", "||")
                }
            }
            if (newRule.contains("&") && !newRule.contains("&&") && !newRule.contains("http") && !newRule.startsWith("/")) {
                newRule = newRule.replace("&", "&&")
            }
        }
        if (allinone) {
            newRule = "+$newRule"
        }
        if (reverse) {
            newRule = "-$newRule"
        }
        return newRule
    }

    /**
     * 旧发现地址转新发现地址
     */
    private fun toNewUrls(oldUrls: String?): String? {
        if (oldUrls.isNullOrBlank()) {
            return null
        }
        if (oldUrls.startsWith("@js:") || oldUrls.startsWith("<js>")) {
            return oldUrls
        }
        if (!oldUrls.contains("\n") && !oldUrls.contains("&&")) {
            return toNewUrl(oldUrls)
        }
        val urls = Regex("(&&|\r?\n)+").split(oldUrls)
        return urls.map {
            toNewUrl(it)?.replace(Regex("\n\\s*"), "")
        }.joinToString("\n")
    }

    /**
     * 旧搜索地址转新搜索地址
     */
    private fun toNewUrl(oldUrl: String?): String? {
        val sourceUrl = oldUrl ?: return null
        if (sourceUrl.isBlank()) {
            return null
        }
        var url = sourceUrl
        if (sourceUrl.startsWith("<js>", ignoreCase = true)) {
            url = sourceUrl.replace("=searchKey", "={{key}}").replace("=searchPage", "={{page}}")
            return url
        }
        val map = HashMap<String, Any>()
        var mather = headerPattern.matcher(sourceUrl)
        if (mather.find()) {
            val header = mather.group()
            url = sourceUrl.replace(header, "")
            map["headers"] = header.substring(8)
        }
        var urlList = url.split("|")
        url = urlList[0]
        if (urlList.size > 1) {
            map["charset"] = urlList[1].split("=")[1]
        }
        mather = jsPattern.matcher(url)
        val jsList = arrayListOf<String>()
        while (mather.find()) {
            jsList.add(mather.group())
            url = url.replace(jsList.last(), "$" + (jsList.size - 1))
        }
        url = url.replace("{", "<").replace("}", ">")
        url = url.replace("searchKey", "{{key}}")
        url = Regex("<searchPage([-+]1)>").replace(url, "{{page$1}}")
        url = Regex("searchPage([-+]1)").replace(url, "{{page$1}}")
        url = url.replace("searchPage", "{{page}}")
        jsList.forEachIndexed { index, item ->
            url = url.replace("$" + index, item.replace("searchKey", "key").replace("searchPage", "page"))
        }
        urlList = url.split("@")
        url = urlList[0]
        if (urlList.size > 1) {
            map["method"] = "POST"
            map["body"] = urlList[1]
        }
        if (map.isNotEmpty()) {
            url = "$url,${GSON.toJson(map)}"
        }
        return url
    }

    /**
     * ua转header
     */
    private fun uaToHeader(ua: String?): String? {
        if (ua.isNullOrEmpty()) {
            return null
        }
        val map = mapOf("User-Agent" to ua)
        return GSON.toJson(map)
    }

    data class BookSourceAny(
        var bookSourceName: String = "",
        var bookSourceGroup: String? = null,
        var bookSourceUrl: String = "",
        var bookSourceType: Int = 0,
        var bookUrlPattern: String? = null,
        var customOrder: Int = 0,
        var enabled: Boolean = true,
        var enabledExplore: Boolean = true,
        var enabledCookieJar: Boolean = false,
        var concurrentRate: String? = null,
        var header: String? = null,
        var loginUrl: Any? = null,
        var loginUi: Any? = null,
        var loginCheckJs: String? = null,
        var bookSourceComment: String = "",
        var lastUpdateTime: Long = 0L,
        var respondTime: Long = 180000L,
        var weight: Int = 0,
        var exploreUrl: String? = null,
        var ruleExplore: Any? = null,
        var searchUrl: String? = null,
        var ruleSearch: Any? = null,
        var ruleBookInfo: Any? = null,
        var ruleToc: Any? = null,
        var ruleContent: Any? = null
    )
}
