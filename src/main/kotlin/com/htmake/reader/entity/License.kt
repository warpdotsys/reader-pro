package com.htmake.reader.entity

data class License(
    var host: String = "",
    var email: String = "",
    var code: String = "",
    var expireAt: Long = 0,
    var activated: Boolean = false
)
