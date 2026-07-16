package io.legado.app.data.entities

data class BookChapter(
    var url: String = "",
    var title: String = "",
    var bookUrl: String = "",
    var index: Int = 0,
    var resourceUrl: String? = null,
    var tag: String? = null,
    var start: Long? = null,
    var end: Long? = null,
    var byteStart: Long = 0,
    var variable: String? = null
)
