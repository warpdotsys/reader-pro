package io.legado.app.data.entities

data class SearchKeyword(
    var word: String = "",
    var usage: Int = 1,
    var lastUseTime: Long = System.currentTimeMillis()
)
