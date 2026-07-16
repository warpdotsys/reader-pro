/** Business rewrite from reader-pro-3.2.14.jar — phase10. */

package com.htmake.reader.config

import io.vertx.core.json.JsonObject

/**
 * 用户阅读/界面配置（存 storage/data/{user}/userConfig.json）。
 * 字段来自前端 simple-web 与 jar saveUserConfig 透传 JSON；非强类型校验。
 */
object UserConfigKeys {
    /** 主题：light / dark / auto */
    const val THEME = "theme"
    /** 正文字体 */
    const val FONT_FAMILY = "fontFamily"
    const val FONT_SIZE = "fontSize"
    /** 行距 / 段距 */
    const val LINE_HEIGHT = "lineHeight"
    const val PARAGRAPH_SPACING = "paragraphSpacing"
    /** 翻页：slide / scroll / simulation */
    const val PAGE_MODE = "pageMode"
    /** 背景色 / 文字色 */
    const val BG_COLOR = "bgColor"
    const val TEXT_COLOR = "textColor"
    /** 阅读进度同步 */
    const val SYNC_PROGRESS = "syncProgress"
    /** TTS 默认 type / voice */
    const val TTS_TYPE = "ttsType"
    const val TTS_VOICE = "ttsVoice"
    /** 多源搜索并发 */
    const val SEARCH_CONCURRENT = "searchConcurrent"
    const val CACHE_CONCURRENT = "cacheConcurrent"
    /** 客户端写入时间戳（jar 写入 @updateTime） */
    const val UPDATE_TIME = "@updateTime"
}

object UserConfigDefaults {
    fun base(): JsonObject = JsonObject()
        .put(UserConfigKeys.THEME, "light")
        .put(UserConfigKeys.FONT_FAMILY, "system-ui")
        .put(UserConfigKeys.FONT_SIZE, 18)
        .put(UserConfigKeys.LINE_HEIGHT, 1.6)
        .put(UserConfigKeys.PAGE_MODE, "slide")
        .put(UserConfigKeys.BG_COLOR, "#ffffff")
        .put(UserConfigKeys.TEXT_COLOR, "#222222")
        .put(UserConfigKeys.SYNC_PROGRESS, true)
        .put(UserConfigKeys.TTS_TYPE, "edge")
        .put(UserConfigKeys.TTS_VOICE, "zh-CN-XiaoxiaoNeural")
        .put(UserConfigKeys.SEARCH_CONCURRENT, 36)
        .put(UserConfigKeys.CACHE_CONCURRENT, 24)

    /** 合并：defaults ← stored ← patch */
    fun merge(stored: JsonObject?, patch: JsonObject? = null): JsonObject {
        val out = base()
        stored?.forEach { e -> out.put(e.key, e.value) }
        patch?.forEach { e -> out.put(e.key, e.value) }
        return out
    }
}
