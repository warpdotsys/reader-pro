package com.htmake.reader.entity

/**
 * Installed product license (stored as JSON under storage/data/license.json).
 */
data class License(
    var host: String? = null,
    var email: String? = null,
    var code: String? = null,
    var userMax: Int = 0,
    var expireAt: Long = 0,
    var simpleWebExpiredAt: Long = 0,
    var payload: String? = null,
    var signature: String? = null,
    var activated: Boolean = false,
    var activatedAt: Long = 0
) {
    fun validHost(requestHost: String?): Boolean {
        val h = host?.trim().orEmpty()
        if (h.isEmpty() || h == "*") return true
        if (requestHost.isNullOrBlank()) return true
        return h.equals(requestHost, true) ||
            requestHost.contains(h, true) ||
            h.contains(requestHost, true)
    }

    fun isExpired(now: Long = System.currentTimeMillis()): Boolean =
        expireAt > 0 && now > expireAt
}

data class ActiveLicense(
    var licenseId: String? = null,
    var activatedAt: Long = 0,
    var host: String? = null,
    var result: String? = null
)
