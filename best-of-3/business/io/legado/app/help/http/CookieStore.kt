/** Business rewrite from reader-pro-3.2.14.jar — phase11. */

package io.legado.app.help.http

import com.htmake.reader.utils.ExtKt
import io.legado.app.utils.ACache
import io.legado.app.utils.NetworkUtils
import java.io.File

/**
 * Cookie 持久化：storage/cache/cookie/{user}/ 按 subdomain 存。
 * jar: CookieStore + ACache；JS 绑定 cookie.getCookie/setCookie/replaceCookie。
 */
class CookieStore(val userNameSpace: String) {

    private val cache: ACache = ACache.get(
        File(ExtKt.getWorkDir("storage", "cache", "cookie", userNameSpace))
    )

    fun setCookie(url: String, cookie: String?) {
        val domain = NetworkUtils.getSubDomain(url)
        if (domain.isEmpty()) return
        cache.put(domain, cookie ?: "")
    }

    fun replaceCookie(url: String, cookie: String) {
        if (url.isBlank() || cookie.isBlank()) return
        val old = getCookie(url)
        if (old.isBlank()) {
            setCookie(url, cookie)
            return
        }
        val map = cookieToMap(old)
        map.putAll(cookieToMap(cookie))
        setCookie(url, mapToCookie(map))
    }

    fun getCookie(url: String): String {
        val domain = NetworkUtils.getSubDomain(url)
        if (domain.isEmpty()) return ""
        return cache.getAsString(domain) ?: ""
    }

    fun getKey(url: String, key: String): String =
        cookieToMap(getCookie(url))[key] ?: ""

    fun removeCookie(url: String) {
        val domain = NetworkUtils.getSubDomain(url)
        if (domain.isNotEmpty()) cache.remove(domain)
    }

    fun cookieToMap(cookie: String): MutableMap<String, String> {
        val map = linkedMapOf<String, String>()
        if (cookie.isBlank()) return map
        cookie.split(';').map { it.trim() }.filter { it.isNotEmpty() }.forEach { part ->
            val i = part.indexOf('=')
            if (i > 0) {
                val k = part.substring(0, i).trim()
                val v = part.substring(i + 1).trim()
                if (k.isNotEmpty()) map[k] = v
            }
        }
        return map
    }

    fun mapToCookie(map: Map<String, String>): String =
        map.entries.joinToString("; ") { "${it.key}=${it.value}" }

    /** 合并响应 Set-Cookie 到存储 */
    fun applySetCookie(url: String, setCookieHeaders: List<String>) {
        if (setCookieHeaders.isEmpty()) return
        val pairs = setCookieHeaders.mapNotNull { line ->
            line.substringBefore(';').trim().takeIf { it.contains('=') }
        }
        if (pairs.isEmpty()) return
        replaceCookie(url, pairs.joinToString("; "))
    }
}
