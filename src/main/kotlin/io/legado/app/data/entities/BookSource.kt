package io.legado.app.data.entities

import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import io.legado.app.data.entities.rule.BookInfoRule
import io.legado.app.data.entities.rule.ContentRule
import io.legado.app.data.entities.rule.ExploreRule
import io.legado.app.data.entities.rule.SearchRule
import io.legado.app.data.entities.rule.TocRule
import io.legado.app.help.SourceAnalyzer
import io.legado.app.model.DebugLog
import io.legado.app.utils.GSON
import io.legado.app.utils.fromJsonObject
import java.io.InputStream

@JsonIgnoreProperties("headerMap", "source", "_userNameSpace", "userNameSpace", "loginHeader", "loginHeaderMap", "loginInfo", "loginInfoMap")
data class BookSource(
    var bookSourceUrl: String = "",
    var bookSourceName: String = "",
    var bookSourceGroup: String? = null,
    var bookSourceType: Int = 0,
    var bookUrlPattern: String? = null,
    var customOrder: Int = 0,
    var enabled: Boolean = true,
    var enabledExplore: Boolean = true,
    override var enabledCookieJar: Boolean? = false,
    override var concurrentRate: String? = null,
    override var header: String? = null,
    override var loginUrl: String? = null,
    override var loginUi: String? = null,
    var loginCheckJs: String? = null,
    var bookSourceComment: String? = null,
    var variableComment: String? = null,
    var lastUpdateTime: Long = 0L,
    var respondTime: Long = 180000L,
    var weight: Int = 0,
    var exploreUrl: String? = null,
    var ruleExplore: ExploreRule? = null,
    var searchUrl: String? = null,
    var ruleSearch: SearchRule? = null,
    var ruleBookInfo: BookInfoRule? = null,
    var ruleToc: TocRule? = null,
    var ruleContent: ContentRule? = null
) : BaseSource {

    override fun getTag(): String {
        return bookSourceName
    }

    override fun getKey(): String {
        return bookSourceUrl
    }

    @Transient
    private var _userNameSpace: String = ""

    fun setUserNameSpace(nameSpace: String) {
        _userNameSpace = nameSpace
    }

    override fun getUserNameSpace(): String {
        return _userNameSpace
    }

    @Transient
    private var debugLog: DebugLog? = null

    fun setLogger(logger: DebugLog?) {
        debugLog = logger
    }

    override fun getLogger(): DebugLog? {
        return debugLog
    }

    @Suppress("unused")
    private var searchRuleV: SearchRule? = null

    @Suppress("unused")
    private var exploreRuleV: ExploreRule? = null

    @Suppress("unused")
    private var bookInfoRuleV: BookInfoRule? = null

    @Suppress("unused")
    private var tocRuleV: TocRule? = null

    @Suppress("unused")
    private var contentRuleV: ContentRule? = null

    fun getSearchRule(): SearchRule {
        return ruleSearch ?: SearchRule()
    }

    fun getExploreRule(): ExploreRule {
        return ruleExplore ?: ExploreRule()
    }

    fun getBookInfoRule(): BookInfoRule {
        return ruleBookInfo ?: BookInfoRule()
    }

    fun getTocRule(): TocRule {
        return ruleToc ?: TocRule()
    }

    fun getContentRule(): ContentRule {
        return ruleContent ?: ContentRule()
    }

    fun equal(source: BookSource): Boolean {
        return equal(bookSourceName, source.bookSourceName) &&
            equal(bookSourceUrl, source.bookSourceUrl) &&
            equal(bookSourceGroup, source.bookSourceGroup) &&
            bookSourceType == source.bookSourceType &&
            equal(bookUrlPattern, source.bookUrlPattern) &&
            enabled == source.enabled &&
            enabledExplore == source.enabledExplore &&
            enabledCookieJar == source.enabledCookieJar &&
            equal(header, source.header) &&
            equal(loginUrl, source.loginUrl) &&
            equal(exploreUrl, source.exploreUrl) &&
            equal(searchUrl, source.searchUrl) &&
            getSearchRule() == source.getSearchRule() &&
            getExploreRule() == source.getExploreRule() &&
            getBookInfoRule() == source.getBookInfoRule() &&
            getTocRule() == source.getTocRule() &&
            getContentRule() == source.getContentRule()
    }

    private fun equal(a: String?, b: String?): Boolean {
        if (a != b) {
            if (!(a.isNullOrEmpty() && b.isNullOrEmpty())) {
                return false
            }
        }
        return true
    }

    companion object {
        fun fromJson(json: String) = SourceAnalyzer.jsonToBookSource(json)

        fun fromJsonArray(json: String) = SourceAnalyzer.jsonToBookSources(json)

        fun fromJsonArray(inputStream: InputStream) = SourceAnalyzer.jsonToBookSources(inputStream)
    }

    data class ExploreKind(
        var title: String,
        var url: String? = null
    )

    class Converters {
        fun exploreRuleToString(exploreRule: ExploreRule?): String = GSON.toJson(exploreRule)

        fun stringToExploreRule(json: String?): ExploreRule? = GSON.fromJsonObject<ExploreRule>(json).getOrNull()

        fun searchRuleToString(searchRule: SearchRule?): String = GSON.toJson(searchRule)

        fun stringToSearchRule(json: String?): SearchRule? = GSON.fromJsonObject<SearchRule>(json).getOrNull()

        fun bookInfoRuleToString(bookInfoRule: BookInfoRule?): String = GSON.toJson(bookInfoRule)

        fun stringToBookInfoRule(json: String?): BookInfoRule? = GSON.fromJsonObject<BookInfoRule>(json).getOrNull()

        fun tocRuleToString(tocRule: TocRule?): String = GSON.toJson(tocRule)

        fun stringToTocRule(json: String?): TocRule? = GSON.fromJsonObject<TocRule>(json).getOrNull()

        fun contentRuleToString(contentRule: ContentRule?): String = GSON.toJson(contentRule)

        fun stringToContentRule(json: String?): ContentRule? = GSON.fromJsonObject<ContentRule>(json).getOrNull()
    }
}
