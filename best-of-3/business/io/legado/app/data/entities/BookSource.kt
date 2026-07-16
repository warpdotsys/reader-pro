/** Business rewrite from reader-pro-3.2.14.jar — phase11. */

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
