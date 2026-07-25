package com.htmake.reader.entity

import java.util.UUID

data class License(
    var host: String = "*",
    var userMaxLimit: Int = 15,
    var expiredAt: Long = 0,
    var openApi: Boolean = false,
    var simpleWebExpiredAt: Long = 1688140799000L,
    var instances: Int = 1,
    var type: String = "default",
    var id: String = UUID.randomUUID().toString(),
    var code: String = UUID.randomUUID().toString(),
    var verified: Boolean = false,
    var verifyTime: Long? = null
) {

    fun isValid(): Boolean {
        return expiredAt == 0L || expiredAt >= System.currentTimeMillis()
    }

    fun validHost(queryHost: String): Boolean {
        if (!isValid()) {
            return false
        }
        if (queryHost.isEmpty()) {
            return false
        }
        if ("*".equals(host)) {
            return true
        }
        val hostParts = queryHost.split(":")
        val queryParts = hostParts[0].split(".")
        val hostnames = host.split(",")
        for (hostname in hostnames) {
            val parts = hostname.split(".")
            if (parts.size == queryParts.size) {
                var isValid = true
                for (i in parts.indices) {
                    if (!"*".equals(parts[i]) && !parts[i].equals(queryParts[i])) {
                        isValid = false
                        break
                    }
                }
                if (isValid) {
                    return true
                }
            }
        }
        return false
    }

    fun toActiveLicense(): ActiveLicense {
        return ActiveLicense(
            host = host,
            userMaxLimit = userMaxLimit,
            expiredAt = expiredAt,
            openApi = openApi,
            simpleWebExpiredAt = simpleWebExpiredAt,
            id = id,
            code = code,
            verified = verified,
            verifyTime = verifyTime,
            instances = instances,
            type = type
        )
    }
}
