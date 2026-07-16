/** Business rewrite from reader-pro-3.2.14.jar — readability / audit. */

package com.htmake.reader.entity

data class User(
    var username: String = "",
    var password: String = "",
    var salt: String = "",
    var token: String? = null,
    var lastLoginAt: Long = 0,
    var createdAt: Long = 0,
    var isManager: Boolean = false,
    var enableWebdav: Boolean = true,
    var enableLocalStore: Boolean = true,
    var enableBookSource: Boolean = true,
    var enableRssSource: Boolean = true,
    var bookSourceLimit: Int = 100,
    var bookLimit: Int = 200
)
