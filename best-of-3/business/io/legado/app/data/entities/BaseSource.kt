/** Business rewrite from reader-pro-3.2.14.jar — phase11. */

package io.legado.app.data.entities

import io.legado.app.help.CacheManager
import io.legado.app.model.analyzeRule.AnalyzeRule

/**
 * 书源/TTS/RSS 公共源接口。
 * loginHeader 存 CacheManager key = loginHeader_{getKey()}
 */
interface BaseSource {
    fun getKey(): String = ""
    fun getTag(): String = ""
    fun getHeader(): String? = null
    fun getLoginUrl(): String? = null
    fun getLoginUi(): String? = null
    fun getLoginCheckJs(): String? = null
    fun getUserNameSpace(): String = "default"

    fun getHeaderMap(withLogin: Boolean = false): Map<String, String> {
        val map = linkedMapOf<String, String>()
        map["User-Agent"] =
            "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/113.0.0.0 Safari/537.36"
        val h = getHeader()
        if (!h.isNullOrBlank()) {
            val json = when {
                h.startsWith("@js:", ignoreCase = true) ->
                    runCatching { AnalyzeRule().evalJS(h.substring(4), null)?.toString() }.getOrNull()
                h.startsWith("<js>", ignoreCase = true) -> {
                    val end = h.lastIndexOf('<')
                    val js = if (end > 4) h.substring(4, end) else h.removePrefix("<js>").removeSuffix("</js>")
                    runCatching { AnalyzeRule().evalJS(js, null)?.toString() }.getOrNull()
                }
                else -> h
            }
            if (!json.isNullOrBlank()) {
                runCatching {
                    val o = com.google.gson.JsonParser.parseString(json).asJsonObject
                    o.entrySet().forEach { map[it.key] = it.value.asString }
                }
            }
        }
        if (withLogin) {
            getLoginHeaderMap()?.let { map.putAll(it) }
        }
        return map
    }

    fun getLoginJs(): String? {
        val loginJs = getLoginUrl() ?: return null
        return when {
            loginJs.startsWith("@js:") -> loginJs.substring(4)
            loginJs.startsWith("<js>") -> {
                val end = loginJs.lastIndexOf('<')
                if (end > 4) loginJs.substring(4, end) else loginJs.removePrefix("<js>").removeSuffix("</js>")
            }
            else -> loginJs
        }
    }

    /** 执行 loginUrl 中的 JS（登录脚本） */
    fun login() {
        val js = getLoginJs() ?: return
        AnalyzeRule(null, this, null).evalJS(js, null)
    }

    fun getLoginHeader(): String? =
        CacheManager(getUserNameSpace()).get("loginHeader_${getKey()}")

    fun putLoginHeader(headerJson: String) {
        CacheManager(getUserNameSpace()).put("loginHeader_${getKey()}", headerJson, 0)
    }

    fun removeLoginHeader() {
        CacheManager(getUserNameSpace()).delete("loginHeader_${getKey()}")
    }

    fun getLoginHeaderMap(): Map<String, String>? {
        val raw = getLoginHeader() ?: return null
        return runCatching {
            val o = com.google.gson.JsonParser.parseString(raw).asJsonObject
            o.entrySet().associate { it.key to it.value.asString }
        }.getOrNull()
    }
}
