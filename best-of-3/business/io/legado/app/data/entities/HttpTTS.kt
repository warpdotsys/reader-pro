/** Business rewrite from reader-pro-3.2.14.jar — phase11. */

package io.legado.app.data.entities

data class HttpTTS(
    var name: String = "",
    var url: String = "",
    var contentType: String? = null,
    var loginCheckJs: String? = null,
    var header: String? = null,
    private var _userNameSpace: String = "default"
) : BaseSource {
    override fun getKey(): String = name.ifEmpty { url }
    override fun getTag(): String = name
    override fun getHeader(): String? = header
    override fun getLoginCheckJs(): String? = loginCheckJs
    override fun getUserNameSpace(): String = _userNameSpace

    fun setUserNameSpace(ns: String) {
        _userNameSpace = ns
    }
}
