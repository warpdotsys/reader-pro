package io.legado.app.data.entities

data class HttpTTS(
    var name: String = "",
    var url: String = "",
    var contentType: String? = null,
    var loginCheckJsValue: String? = null,
    var headerJson: String? = null,
    private var _userNameSpace: String = "default"
) : BaseSource {
    override fun getKey() = name.ifEmpty { url }
    override fun getTag() = name
    override fun getHeader() = headerJson
    override fun getLoginCheckJs() = loginCheckJsValue
    override fun getUserNameSpace() = _userNameSpace
}
