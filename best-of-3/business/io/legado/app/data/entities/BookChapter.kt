/** Business rewrite from reader-pro-3.2.14.jar — readability / audit. */

package io.legado.app.data.entities

data class BookChapter(
    var url: String = "",
    var title: String = "",
    var index: Int = 0,
    var bookUrl: String = "",
    var resourceUrl: String? = null,
    var tag: String? = null
)
