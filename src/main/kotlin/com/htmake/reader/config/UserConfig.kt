package com.htmake.reader.config

import io.vertx.core.json.JsonObject

object UserConfigKeys {
    const val THEME = "theme"
    const val FONT_FAMILY = "fontFamily"
    const val FONT_SIZE = "fontSize"
    const val PAGE_MODE = "pageMode"
    const val TTS_TYPE = "ttsType"
    const val TTS_VOICE = "ttsVoice"
    const val SEARCH_CONCURRENT = "searchConcurrent"
    const val CACHE_CONCURRENT = "cacheConcurrent"
    const val UPDATE_TIME = "@updateTime"
}

object UserConfigDefaults {
    fun base(): JsonObject = JsonObject()
        .put(UserConfigKeys.THEME, "light")
        .put(UserConfigKeys.FONT_FAMILY, "system-ui")
        .put(UserConfigKeys.FONT_SIZE, 18)
        .put(UserConfigKeys.PAGE_MODE, "slide")
        .put(UserConfigKeys.TTS_TYPE, "edge")
        .put(UserConfigKeys.TTS_VOICE, "zh-CN-XiaoxiaoNeural")
        .put(UserConfigKeys.SEARCH_CONCURRENT, 36)
        .put(UserConfigKeys.CACHE_CONCURRENT, 24)

    fun merge(stored: JsonObject?, patch: JsonObject? = null): JsonObject {
        val out = base()
        stored?.forEach { out.put(it.key, it.value) }
        patch?.forEach { out.put(it.key, it.value) }
        return out
    }
}
