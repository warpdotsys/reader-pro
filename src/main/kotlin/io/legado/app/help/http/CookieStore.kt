package io.legado.app.help.http

import com.htmake.reader.utils.ExtKt
import io.legado.app.utils.ACache
import io.legado.app.utils.NetworkUtils
import java.io.File

class CookieStore(val userNameSpace: String) {
    private val cache = ACache.get(File(ExtKt.getWorkDir("storage", "cache", "cookie", userNameSpace)))

    fun setCookie(url: String, cookie: String?) {
        val d = NetworkUtils.getSubDomain(url)
        if (d.isNotEmpty()) cache.put(d, cookie ?: "")
    }

    fun replaceCookie(url: String, cookie: String) {
        if (url.isBlank() || cookie.isBlank()) return
        val old = getCookie(url)
        if (old.isBlank()) { setCookie(url, cookie); return }
        val map = cookieToMap(old)
        map.putAll(cookieToMap(cookie))
        setCookie(url, mapToCookie(map))
    }

    fun getCookie(url: String): String {
        val d = NetworkUtils.getSubDomain(url)
        return if (d.isEmpty()) "" else cache.getAsString(d) ?: ""
    }

    fun removeCookie(url: String) {
        val d = NetworkUtils.getSubDomain(url)
        if (d.isNotEmpty()) cache.remove(d)
    }

    fun applySetCookie(url: String, headers: List<String>) {
        val pairs = headers.mapNotNull { it.substringBefore(';').trim().takeIf { p -> p.contains('=') } }
        if (pairs.isNotEmpty()) replaceCookie(url, pairs.joinToString("; "))
    }

    fun cookieToMap(cookie: String): MutableMap<String, String> {
        val map = linkedMapOf<String, String>()
        cookie.split(';').map { it.trim() }.filter { it.isNotEmpty() }.forEach { part ->
            val i = part.indexOf('=')
            if (i > 0) map[part.substring(0, i).trim()] = part.substring(i + 1).trim()
        }
        return map
    }

    fun mapToCookie(map: Map<String, String>) =
        map.entries.joinToString("; ") { "${it.key}=${it.value}" }
}
