package com.htmake.reader.entity

data class User(
    var username: String = "",
    var password: String = "",
    var salt: String = "",
    var token: String? = null,
    var isManager: Boolean = false,
    var enableWebdav: Boolean = true,
    var enableLocalStore: Boolean = true,
    var enableBookSource: Boolean = true,
    var enableRssSource: Boolean = true
)
