package io.legado.app.data.entities

import io.legado.app.help.CacheManager
import io.legado.app.model.analyzeRule.AnalyzeRule

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
            "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/120.0.0.0 Safari/537.36"
        val h = getHeader()
        if (!h.isNullOrBlank()) {
            val json = when {
                h.startsWith("@js:", true) ->
                    runCatching { AnalyzeRule().evalJS(h.substring(4), null)?.toString() }.getOrNull()
                else -> h
            }
            if (!json.isNullOrBlank()) {
                runCatching {
                    com.google.gson.JsonParser.parseString(json).asJsonObject
                        .entrySet().forEach { map[it.key] = it.value.asString }
                }
            }
        }
        if (withLogin) getLoginHeaderMap()?.let { map.putAll(it) }
        return map
    }

    fun getLoginHeader(): String? =
        CacheManager(getUserNameSpace()).get("loginHeader_${getKey()}")

    fun putLoginHeader(headerJson: String) {
        CacheManager(getUserNameSpace()).put("loginHeader_${getKey()}", headerJson)
    }

    fun getLoginHeaderMap(): Map<String, String>? {
        val raw = getLoginHeader() ?: return null
        return runCatching {
            com.google.gson.JsonParser.parseString(raw).asJsonObject
                .entrySet().associate { it.key to it.value.asString }
        }.getOrNull()
    }
}
