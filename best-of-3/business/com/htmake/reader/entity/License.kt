/** Business rewrite from reader-pro-3.2.14.jar — readability / audit. */

package com.htmake.reader.entity

data class License(
    var host: String? = null,
    var userMax: Int = 0,
    var expireAt: Long = 0,
    var payload: String? = null,
    var signature: String? = null
)

data class ActiveLicense(
    var licenseId: String? = null,
    var activatedAt: Long = 0,
    var host: String? = null
)
