/** Business rewrite from reader-pro-3.2.14.jar — phase4. */

package io.legado.app.data.entities

data class TxtTocRule(
    var id: Long = 0,
    var enable: Boolean = true,
    var name: String = "",
    var rule: String = "",
    var serialNumber: Int = 0
)
