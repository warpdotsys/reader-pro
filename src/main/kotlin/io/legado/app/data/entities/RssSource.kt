package io.legado.app.data.entities

data class RssSource(
    var sourceUrl: String = "",
    var sourceName: String = "",
    var sourceIcon: String = "",
    var sourceGroup: String? = null,
    var enabled: Boolean = true,
    var headerJson: String? = null,
    var sortUrl: String? = null,
    var ruleArticles: String? = null,
    var ruleNextPage: String? = null,
    var ruleTitle: String? = null,
    var rulePubDate: String? = null,
    var ruleDescription: String? = null,
    var ruleImage: String? = null,
    var ruleLink: String? = null,
    var ruleContent: String? = null,
    private var _userNameSpace: String = "default"
) : BaseSource {
    override fun getKey() = sourceUrl
    override fun getTag() = sourceName
    override fun getHeader() = headerJson
    override fun getUserNameSpace() = _userNameSpace
    fun setUserNameSpace(ns: String) { _userNameSpace = ns }
}
