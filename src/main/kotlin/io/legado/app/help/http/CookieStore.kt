package io.legado.app.help.http

import io.legado.app.adapters.ReaderAdapterHelper
import io.legado.app.help.http.api.CookieManager
import io.legado.app.utils.ACache
import io.legado.app.utils.NetworkUtils
import io.legado.app.utils.TextUtils
import java.io.File

class CookieStore(val userNameSpace: String) : CookieManager {
    val cacheInstance = ACache.get(
        File(
            ReaderAdapterHelper.getAdapter().getWorkDir(
                "storage",
                "cache",
                "cookie",
                userNameSpace
            )
        ),
        50_000_000,
        1_000_000
    )

    override fun setCookie(url: String, cookie: String?) {
        val domain = NetworkUtils.getSubDomain(url)
        if (domain.isNotEmpty()) cacheInstance.put(domain, cookie ?: "")
    }

    override fun replaceCookie(url: String, cookie: String) {
        if (TextUtils.isEmpty(url) || TextUtils.isEmpty(cookie)) return

        val oldCookie = getCookie(url)
        if (TextUtils.isEmpty(oldCookie)) {
            setCookie(url, cookie)
        } else {
            val cookieMap = cookieToMap(oldCookie)
            cookieMap.putAll(cookieToMap(cookie))
            setCookie(url, mapToCookie(cookieMap))
        }
    }

    override fun getCookie(url: String): String {
        val domain = NetworkUtils.getSubDomain(url)
        return if (domain.isEmpty()) "" else cacheInstance.getAsString(domain) ?: ""
    }

    fun getKey(url: String, key: String): String = cookieToMap(getCookie(url))[key] ?: ""

    override fun removeCookie(url: String) {
        val domain = NetworkUtils.getSubDomain(url)
        if (domain.isNotEmpty()) cacheInstance.remove(domain)
    }

    override fun cookieToMap(cookie: String): MutableMap<String, String> {
        val cookieMap = linkedMapOf<String, String>()
        if (cookie.isBlank()) return cookieMap

        for (pair in cookie.split(";".toRegex()).dropLastWhile { it.isEmpty() }) {
            val pairs = pair.split("=".toRegex()).dropLastWhile { it.isEmpty() }
            if (pairs.size == 1) continue
            val key = pairs[0].trim()
            val value = pairs[1]
            if (value.isNotBlank() || value.trim() == "null") {
                cookieMap[key] = value.trim()
            }
        }
        return cookieMap
    }

    override fun mapToCookie(cookieMap: Map<String, String>?): String? {
        if (cookieMap.isNullOrEmpty()) return null
        val builder = StringBuilder()
        for (key in cookieMap.keys) {
            val value = cookieMap[key]
            if (!value.isNullOrBlank()) builder.append(key).append('=').append(value).append(';')
        }
        return builder.deleteCharAt(builder.lastIndexOf(";")).toString()
    }

    fun clear() {
        cacheInstance.clear()
    }
}
