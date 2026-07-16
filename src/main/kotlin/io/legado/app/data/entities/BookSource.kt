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
    var headerJson: String? = null,
    var loginUrlValue: String? = null,
    var loginUiValue: String? = null,
    var loginCheckJsValue: String? = null,
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
    override fun getHeader() = headerJson
    override fun getLoginUrl() = loginUrlValue
    override fun getLoginUi() = loginUiValue
    override fun getLoginCheckJs() = loginCheckJsValue
    override fun getUserNameSpace() = _userNameSpace
    fun setUserNameSpace(ns: String) { _userNameSpace = ns }

    companion object {
        fun fromJson(json: String): Result<BookSource> = SourceAnalyzer.jsonToBookSource(json)
        fun fromJsonArray(json: String): Result<List<BookSource>> =
            SourceAnalyzer.jsonToBookSources(json)
    }
}
