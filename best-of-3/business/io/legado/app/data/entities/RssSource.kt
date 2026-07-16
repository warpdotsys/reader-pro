/** Business rewrite from reader-pro-3.2.14.jar — phase11. */

package io.legado.app.data.entities

/**
 * RSS 源（legado 字段子集，业务可读版）
 */
data class RssSource(
    var sourceUrl: String = "",
    var sourceName: String = "",
    var sourceIcon: String = "",
    var sourceGroup: String? = null,
    var sourceComment: String? = null,
    var enabled: Boolean = true,
    var header: String? = null,
    var sortUrl: String? = null,
    var singleUrl: Boolean = false,
    var ruleArticles: String? = null,
    var ruleNextPage: String? = null,
    var ruleTitle: String? = null,
    var rulePubDate: String? = null,
    var ruleDescription: String? = null,
    var ruleImage: String? = null,
    var ruleLink: String? = null,
    var ruleContent: String? = null,
    var enableJs: Boolean = true,
    var loadWithBaseUrl: Boolean = true,
    var customOrder: Int = 0,
    private var _userNameSpace: String = "default"
) : BaseSource {

    override fun getKey(): String = sourceUrl
    override fun getTag(): String = sourceName
    override fun getHeader(): String? = header
    override fun getUserNameSpace(): String = _userNameSpace

    fun setUserNameSpace(ns: String) {
        _userNameSpace = ns
    }
}
