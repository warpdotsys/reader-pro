package io.legado.app.utils

object NetworkUtils {
    fun getAbsoluteURL(base: String?, relative: String): String {
        if (relative.startsWith("http://") || relative.startsWith("https://")) return relative
        if (base.isNullOrEmpty()) return relative
        return try {
            java.net.URL(java.net.URL(base), relative).toString()
        } catch (_: Exception) {
            relative
        }
    }

    fun getSubDomain(url: String): String {
        val host = try {
            java.net.URL(if ("://" in url) url else "http://$url").host
        } catch (_: Exception) {
            return url
        }
        if (host.isBlank()) return ""
        val parts = host.split('.')
        return if (parts.size >= 2) parts.takeLast(2).joinToString(".") else host
    }
}
