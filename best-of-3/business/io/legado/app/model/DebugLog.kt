/** Business rewrite from reader-pro-3.2.14.jar — readability / audit. */

package io.legado.app.model

interface DebugLog {
    fun log(sourceUrl: String?, msg: String?)
    fun log(sourceUrl: String?, msg: String?, isHtml: Boolean) {
        log(sourceUrl, msg)
    }
    fun log(message: String) {
        log(null, message)
    }
}

object Debug : DebugLog {
    override fun log(sourceUrl: String?, msg: String?) {}
}
