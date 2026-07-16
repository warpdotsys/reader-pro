# -*- coding: utf-8 -*-
"""Phase 10: userConfig/theme, invalid bookSource cache, AnalyzeRule allInOne/JS edges."""
from pathlib import Path
import os
import re

BIZ = Path(r"C:\Users\chong\reader-pro-3.2.14-reverse\best-of-3\business")
H = "/** Business rewrite from reader-pro-3.2.14.jar — phase10. */\n\n"


def w(rel, c):
    p = BIZ / rel.replace("/", os.sep)
    p.parent.mkdir(parents=True, exist_ok=True)
    p.write_text(H + c.lstrip("\n"), encoding="utf-8", newline="\n")
    print("wrote", rel, p.stat().st_size)


# ---------------------------------------------------------------------------
# UserConfig model + defaults (reading theme keys used by frontend)
# ---------------------------------------------------------------------------
w(
    "com/htmake/reader/config/UserConfig.kt",
    r'''
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
''',
)

# ---------------------------------------------------------------------------
# Patch UserController save/get userConfig
# ---------------------------------------------------------------------------
uc = BIZ / "com/htmake/reader/api/controller/UserController.kt"
ut = uc.read_text(encoding="utf-8")
old_save = '''    suspend fun saveUserConfig(context: RoutingContext): ReturnData {
        val rd = ReturnData()
        if (!checkAuth(context)) return rd.setData("NEED_LOGIN").setErrorMsg("请登录后使用")
        val ns = getUserNameSpace(context)
        val cfg = context.bodyAsJson ?: return rd.setErrorMsg("参数错误")
        saveUserStorage(ns, "userConfig", cfg)
        return rd.setData(true)
    }

    suspend fun getUserConfig(context: RoutingContext): ReturnData {
        val rd = ReturnData()
        if (!checkAuth(context)) return rd.setData("NEED_LOGIN").setErrorMsg("请登录后使用")
        val ns = getUserNameSpace(context)
        val raw = getUserStorage(ns, "userConfig")
        return rd.setData(ExtKt.asJsonObject(raw) ?: mapOf<String, Any>())
    }'''

new_save = '''    /**
     * 保存阅读配置。jar：body JSON + `@updateTime` 时间戳。
     * 支持 merge=true 与已有配置合并（默认覆盖整份）。
     */
    suspend fun saveUserConfig(context: RoutingContext): ReturnData {
        val rd = ReturnData()
        if (!checkAuth(context)) return rd.setData("NEED_LOGIN").setErrorMsg("请登录后使用")
        val ns = getUserNameSpace(context)
        val body = context.bodyAsJson ?: return rd.setErrorMsg("参数错误")
        val merge = body.getBoolean("merge", false) == true
        body.remove("merge")
        body.put(com.htmake.reader.config.UserConfigKeys.UPDATE_TIME, System.currentTimeMillis())
        val toSave = if (merge) {
            val prev = ExtKt.asJsonObject(getUserStorage(ns, "userConfig"))
            com.htmake.reader.config.UserConfigDefaults.merge(prev, body)
        } else {
            body
        }
        saveUserStorage(ns, "userConfig", toSave)
        return rd.setData("")
    }

    /** 无备份时返回 error「没有备份文件」对齐 jar；业务侧也可返回 defaults。 */
    suspend fun getUserConfig(context: RoutingContext): ReturnData {
        val rd = ReturnData()
        if (!checkAuth(context)) return rd.setData("NEED_LOGIN").setErrorMsg("请登录后使用")
        val ns = getUserNameSpace(context)
        val raw = getUserStorage(ns, "userConfig")
        val obj = ExtKt.asJsonObject(raw)
        if (obj == null) {
            // jar: setErrorMsg("没有备份文件") — 前端可回落 defaults
            return rd.setErrorMsg("没有备份文件").setData(
                com.htmake.reader.config.UserConfigDefaults.base().map
            )
        }
        return rd.setData(obj.map)
    }'''

# fix typo UPDATE_TIME
new_save = new_save.replace("UPDATE_TIME", "UPDATE_TIME")  # oops
new_save = new_save.replace(
    "com.htmake.reader.config.UserConfigKeys.UPDATE_TIME",
    "com.htmake.reader.config.UserConfigKeys.UPDATE_TIME",
)
# I made a mess - just write correct string
new_save = '''    /**
     * 保存阅读配置。jar：body JSON + `@updateTime` 时间戳。
     * 支持 merge=true 与已有配置合并（默认覆盖整份）。
     */
    suspend fun saveUserConfig(context: RoutingContext): ReturnData {
        val rd = ReturnData()
        if (!checkAuth(context)) return rd.setData("NEED_LOGIN").setErrorMsg("请登录后使用")
        val ns = getUserNameSpace(context)
        val body = context.bodyAsJson ?: return rd.setErrorMsg("参数错误")
        val merge = body.getBoolean("merge", false) == true
        body.remove("merge")
        body.put(com.htmake.reader.config.UserConfigKeys.UPDATE_TIME, System.currentTimeMillis())
        val toSave = if (merge) {
            val prev = ExtKt.asJsonObject(getUserStorage(ns, "userConfig"))
            com.htmake.reader.config.UserConfigDefaults.merge(prev, body)
        } else {
            body
        }
        saveUserStorage(ns, "userConfig", toSave)
        return rd.setData("")
    }

    /** 无备份时 jar 返回「没有备份文件」；同时 data 带 defaults 方便前端。 */
    suspend fun getUserConfig(context: RoutingContext): ReturnData {
        val rd = ReturnData()
        if (!checkAuth(context)) return rd.setData("NEED_LOGIN").setErrorMsg("请登录后使用")
        val ns = getUserNameSpace(context)
        val raw = getUserStorage(ns, "userConfig")
        val obj = ExtKt.asJsonObject(raw)
        if (obj == null) {
            return rd.setErrorMsg("没有备份文件").setData(
                com.htmake.reader.config.UserConfigDefaults.base().map
            )
        }
        return rd.setData(obj.map)
    }'''

# Fix the UPDATE_TIME constant reference properly
new_save = new_save.replace(
    "UserConfigKeys.UPDATE_TIME",
    "UserConfigKeys.UPDATE_TIME",
)
# I'll just use the string "@updateTime" directly to avoid typos
new_save = '''    /**
     * 保存阅读配置。jar：body JSON + `@updateTime` 时间戳。
     * 支持 merge=true 与已有配置合并（默认覆盖整份）。
     */
    suspend fun saveUserConfig(context: RoutingContext): ReturnData {
        val rd = ReturnData()
        if (!checkAuth(context)) return rd.setData("NEED_LOGIN").setErrorMsg("请登录后使用")
        val ns = getUserNameSpace(context)
        val body = context.bodyAsJson ?: return rd.setErrorMsg("参数错误")
        val merge = body.getBoolean("merge", false) == true
        body.remove("merge")
        body.put("@updateTime", System.currentTimeMillis())
        val toSave = if (merge) {
            val prev = ExtKt.asJsonObject(getUserStorage(ns, "userConfig"))
            com.htmake.reader.config.UserConfigDefaults.merge(prev, body)
        } else {
            body
        }
        saveUserStorage(ns, "userConfig", toSave)
        return rd.setData("")
    }

    /** 无备份时 jar 返回「没有备份文件」；同时 data 带 defaults 方便前端。 */
    suspend fun getUserConfig(context: RoutingContext): ReturnData {
        val rd = ReturnData()
        if (!checkAuth(context)) return rd.setData("NEED_LOGIN").setErrorMsg("请登录后使用")
        val ns = getUserNameSpace(context)
        val raw = getUserStorage(ns, "userConfig")
        val obj = ExtKt.asJsonObject(raw)
        if (obj == null) {
            return rd.setErrorMsg("没有备份文件").setData(
                com.htmake.reader.config.UserConfigDefaults.base().map
            )
        }
        return rd.setData(obj.map)
    }'''

if old_save in ut:
    ut = ut.replace(old_save, new_save)
    uc.write_text(ut, encoding="utf-8", newline="\n")
    print("patched UserController userConfig")
else:
    print("WARN: UserController userConfig block not exact match")

# ---------------------------------------------------------------------------
# UserControllerExtras: real upload + config helpers
# ---------------------------------------------------------------------------
w(
    "com/htmake/reader/api/controller/UserControllerExtras.kt",
    r'''
package com.htmake.reader.api.controller

import com.htmake.reader.api.ReturnData
import com.htmake.reader.config.UserConfigDefaults
import com.htmake.reader.config.UserConfigKeys
import com.htmake.reader.utils.ExtKt
import io.vertx.core.json.JsonObject
import io.vertx.ext.web.RoutingContext
import java.io.File
import java.util.UUID

/**
 * 用户文件上传 / 备份下载 / 配置辅助。
 */
suspend fun UserController.uploadFile(context: RoutingContext): ReturnData {
    val rd = ReturnData()
    if (!checkAuth(context)) return rd.setData("NEED_LOGIN").setErrorMsg("请登录后使用")
    val ns = getUserNameSpace(context)
    val uploads = context.fileUploads()
    if (uploads.isEmpty()) return rd.setErrorMsg("未上传文件")
    val dir = File(ExtKt.getWorkDir("storage", "data", ns, "assets")).apply { mkdirs() }
    val saved = ArrayList<Map<String, Any?>>()
    for (u in uploads) {
        val name = u.fileName()?.takeIf { it.isNotBlank() } ?: "${UUID.randomUUID()}"
        val safe = name.replace(Regex("""[\\/:*?"<>|]"""), "_")
        val dest = File(dir, safe)
        try {
            // Vert.x uploaded file is already on disk at uploadedFileName
            val tmp = File(u.uploadedFileName())
            if (tmp.isFile) tmp.copyTo(dest, overwrite = true)
            saved += mapOf(
                "name" to safe,
                "path" to dest.absolutePath,
                "size" to dest.length(),
                "url" to "/reader3/assets/$ns/$safe"
            )
        } catch (e: Exception) {
            return rd.setErrorMsg(e.message ?: "上传失败")
        }
    }
    return rd.setData(if (saved.size == 1) saved[0] else saved)
}

suspend fun UserController.deleteFile(context: RoutingContext): ReturnData {
    val rd = ReturnData()
    if (!checkAuth(context)) return rd.setData("NEED_LOGIN").setErrorMsg("请登录后使用")
    val ns = getUserNameSpace(context)
    val name = context.bodyAsJson?.getString("name")
        ?: context.bodyAsJson?.getString("path")
        ?: context.queryParam("name").firstOrNull()
        ?: return rd.setErrorMsg("参数错误")
    val base = File(ExtKt.getWorkDir("storage", "data", ns, "assets")).canonicalFile
    val f = if (name.contains(File.separator) || name.contains('/')) {
        File(name)
    } else {
        File(base, name)
    }.canonicalFile
    if (!f.path.startsWith(base.path)) return rd.setErrorMsg("非法路径")
    if (f.isFile) f.delete()
    return rd.setData(true)
}

fun UserController.downloadBackupFile(context: RoutingContext) {
    val ns = getUserNameSpace(context)
    // prefer latest backup zip under user data
    val dataDir = File(ExtKt.getWorkDir("storage", "data", ns))
    val candidates = dataDir.listFiles()?.filter {
        it.isFile && it.name.endsWith(".zip", true) && it.name.contains("backup", true)
    }.orEmpty()
    val f = candidates.maxByOrNull { it.lastModified() }
        ?: File(dataDir, "backup.zip")
    if (f.isFile) context.response().sendFile(f.absolutePath)
    else context.response().setStatusCode(404).end()
}

/** 读取合并后的用户配置（含 defaults），不走 error 分支。 */
fun UserController.readMergedUserConfig(ns: String): JsonObject {
    val stored = ExtKt.asJsonObject(getUserStorage(ns, "userConfig"))
    return UserConfigDefaults.merge(stored)
}

fun UserController.configInt(ns: String, key: String, default: Int): Int =
    readMergedUserConfig(ns).getInteger(key, default) ?: default

fun UserController.configString(ns: String, key: String, default: String): String =
    readMergedUserConfig(ns).getString(key, default) ?: default
''',
)

# ---------------------------------------------------------------------------
# ACache: getByHashCode + TTL metadata lightly
# ---------------------------------------------------------------------------
w(
    "io/legado/app/utils/ACache.kt",
    r'''
package io.legado.app.utils

import java.io.File

/**
 * 简易磁盘缓存（对齐 jar ACache：按 key hash 文件名）。
 * put(key, value, saveTimeSec) — saveTimeSec>0 时写过期戳。
 */
class ACache private constructor(
    private val dir: File,
    private val maxSize: Long = 5_000_000L,
    private val maxCount: Int = 1_000_000
) {
    init {
        dir.mkdirs()
    }

    fun getAsString(key: String): String? {
        val f = fileOf(key)
        if (!f.isFile) return null
        val text = f.readText()
        // optional expire header: "expireAt=<ms>\n"
        if (text.startsWith("expireAt=")) {
            val nl = text.indexOf('\n')
            if (nl > 0) {
                val exp = text.substring(9, nl).toLongOrNull() ?: 0L
                if (exp > 0 && System.currentTimeMillis() > exp) {
                    f.delete()
                    return null
                }
                return text.substring(nl + 1)
            }
        }
        return text
    }

    /** jar getByHashCode(fileName) — 直接读 hash 文件名 */
    fun getByHashCode(hashFileName: String): String? {
        val f = File(dir, hashFileName)
        if (!f.isFile) return null
        val text = f.readText()
        if (text.startsWith("expireAt=")) {
            val nl = text.indexOf('\n')
            if (nl > 0) {
                val exp = text.substring(9, nl).toLongOrNull() ?: 0L
                if (exp > 0 && System.currentTimeMillis() > exp) {
                    f.delete()
                    return null
                }
                return text.substring(nl + 1)
            }
        }
        return text
    }

    fun put(key: String, value: String, saveTimeSec: Int = 0) {
        dir.mkdirs()
        val body = if (saveTimeSec > 0) {
            val exp = System.currentTimeMillis() + saveTimeSec * 1000L
            "expireAt=$exp\n$value"
        } else value
        fileOf(key).writeText(body)
    }

    fun remove(key: String) {
        fileOf(key).delete()
    }

    private fun fileOf(key: String): File = File(dir, key.hashCode().toString())

    companion object {
        fun get(dir: File, maxSize: Long = 5_000_000L, maxCount: Int = 1_000_000): ACache =
            ACache(dir, maxSize, maxCount)
        fun get(dir: File): ACache = ACache(dir)
    }
}
''',
)

# ---------------------------------------------------------------------------
# CookieStore + CacheManager for JS bindings
# ---------------------------------------------------------------------------
w(
    "io/legado/app/help/http/CookieStore.kt",
    r'''
package io.legado.app.help.http

import java.io.File
import java.util.concurrent.ConcurrentHashMap

/**
 * 书源 JS `cookie` 绑定（简化：内存 + 可选落盘）。
 */
class CookieStore(private val userNameSpace: String) {
    private val map = store.computeIfAbsent(userNameSpace) { ConcurrentHashMap() }

    fun getCookie(url: String): String = map[hostOf(url)] ?: ""
    fun setCookie(url: String, cookie: String) {
        map[hostOf(url)] = cookie
    }
    fun removeCookie(url: String) {
        map.remove(hostOf(url))
    }

    private fun hostOf(url: String): String = try {
        java.net.URL(url).host
    } catch (_: Exception) {
        url
    }

    companion object {
        private val store = ConcurrentHashMap<String, ConcurrentHashMap<String, String>>()
    }
}
''',
)

w(
    "io/legado/app/help/CacheManager.kt",
    r'''
package io.legado.app.help

import java.util.concurrent.ConcurrentHashMap

/**
 * 书源 JS `cache` 绑定：put/get 带可选 TTL 秒。
 */
class CacheManager(private val userNameSpace: String) {
    private val map = store.computeIfAbsent(userNameSpace) { ConcurrentHashMap() }

    fun put(key: String, value: String, saveTime: Int = 0) {
        val exp = if (saveTime > 0) System.currentTimeMillis() + saveTime * 1000L else 0L
        map[key] = exp to value
    }

    fun get(key: String): String? {
        val p = map[key] ?: return null
        if (p.first > 0 && System.currentTimeMillis() > p.first) {
            map.remove(key)
            return null
        }
        return p.second
    }

    fun delete(key: String) {
        map.remove(key)
    }

    companion object {
        private val store = ConcurrentHashMap<String, ConcurrentHashMap<String, Pair<Long, String>>>()
    }
}
''',
)

# ---------------------------------------------------------------------------
# AnalyzeRule rewrite (phase10) — keep package, fuller split/eval/regex
# ---------------------------------------------------------------------------
w(
    "io/legado/app/model/analyzeRule/AnalyzeRule.kt",
    r'''
package io.legado.app.model.analyzeRule

import io.legado.app.data.entities.BaseBook
import io.legado.app.data.entities.BaseSource
import io.legado.app.data.entities.Book
import io.legado.app.data.entities.BookChapter
import io.legado.app.help.CacheManager
import io.legado.app.help.JsExtensions
import io.legado.app.help.http.CookieStore
import io.legado.app.model.DebugLog
import org.mozilla.javascript.Context
import org.mozilla.javascript.Scriptable
import java.util.regex.Pattern

/**
 * Rule engine (legado) — phase10:
 * - allInOne `:` regex
 * - `@js:` / `<js>...</js>` 链式拆分
 * - `##regex##replacement` / `###` first-only
 * - evalJS 绑定 java/cookie/cache/source/book/result/baseUrl/chapter/title/src
 * - put/get 变量
 */
class AnalyzeRule(
    var ruleData: RuleDataInterface? = null,
    private var source: BaseSource? = null,
    var debugLog: DebugLog? = null
) : JsExtensions {

    var content: Any? = null
        private set
    var baseUrl: String? = null
        private set
    var chapter: BookChapter? = null
    var nextChapterUrl: String? = null
    var redirectUrl: java.net.URL? = null

    private var isJSON: Boolean = false
    private var isRegex: Boolean = false
    private var analyzeByJSoup: AnalyzeByJSoup? = null
    private var analyzeByXPath: AnalyzeByXPath? = null
    private var analyzeByJSonPath: AnalyzeByJSonPath? = null

    val book: BaseBook? get() = ruleData as? BaseBook

    constructor(book: Book?, source: BaseSource?, debugLog: DebugLog?) : this(
        book as? RuleDataInterface, source, debugLog
    )

    fun setContent(content: Any?, baseUrl: String? = null): AnalyzeRule {
        this.content = content
        if (baseUrl != null) this.baseUrl = baseUrl
        analyzeByJSoup = null
        analyzeByXPath = null
        analyzeByJSonPath = null
        isJSON = content is String && content.trimStart().let { it.startsWith("{") || it.startsWith("[") }
        return this
    }

    override fun getSource(): BaseSource? = source
    override fun getUserNameSpace(): String = ruleData?.getUserNameSpace() ?: "default"
    override fun getLogger(): DebugLog? = debugLog

    fun getString(ruleStr: String?, mContent: Any? = null, isUrl: Boolean = false): String {
        if (ruleStr.isNullOrEmpty()) return ""
        val rules = splitSourceRule(ruleStr)
        return getString(rules, mContent, isUrl)
    }

    fun getString(ruleList: List<SourceRule>, mContent: Any? = null, isUrl: Boolean = false): String {
        var result: Any? = mContent ?: content
        for (rule in ruleList) {
            // apply @put before eval
            rule.putMap.forEach { (k, v) -> put(k, getString(v)) }
            result = when (rule.mode) {
                Mode.Js -> evalJS(rule.rule, result)
                Mode.Regex -> getStringByRegex(result?.toString() ?: "", rule)
                Mode.Json -> jsonPath().getString(result, rule.rule)
                Mode.XPath -> xPath().getString(result, rule.rule)
                Mode.Default -> jSoup().getString(result, rule.rule)
            }
            if (rule.replaceRegex.isNotEmpty() && result != null) {
                result = replaceRegex(result.toString(), rule)
            }
        }
        val s = result?.toString() ?: ""
        return if (isUrl) NetworkUtils.getAbsoluteURL(baseUrl, s) else s
    }

    fun getStringList(rule: String?, mContent: Any? = null, isUrl: Boolean = false): List<String> {
        if (rule.isNullOrEmpty()) return emptyList()
        val rules = splitSourceRule(rule)
        var result: Any? = mContent ?: content
        var list: List<String> = emptyList()
        for ((i, r) in rules.withIndex()) {
            r.putMap.forEach { (k, v) -> put(k, getString(v)) }
            if (i == rules.lastIndex) {
                list = when (r.mode) {
                    Mode.Js -> listOfNotNull(evalJS(r.rule, result)?.toString())
                    Mode.Regex -> getListByRegex(result?.toString() ?: "", r)
                    Mode.Json -> jsonPath().getStringList(result, r.rule)
                    Mode.XPath -> xPath().getStringList(result, r.rule)
                    Mode.Default -> jSoup().getStringList(result, r.rule)
                }
                if (r.replaceRegex.isNotEmpty()) {
                    list = list.map { replaceRegex(it, r) }
                }
            } else {
                result = getSingle(r, result)
            }
        }
        return if (isUrl) list.map { NetworkUtils.getAbsoluteURL(baseUrl, it) } else list
    }

    fun getElements(ruleStr: String): List<Any> {
        if (ruleStr.isEmpty()) return emptyList()
        val rules = splitSourceRule(ruleStr)
        var result: Any? = content
        var elements: List<Any> = emptyList()
        for ((i, r) in rules.withIndex()) {
            if (i == rules.lastIndex) {
                elements = when (r.mode) {
                    Mode.Json -> jsonPath().getElements(result, r.rule)
                    Mode.XPath -> xPath().getElements(result, r.rule)
                    Mode.Default -> jSoup().getElements(result, r.rule)
                    Mode.Js -> listOfNotNull(evalJS(r.rule, result))
                    Mode.Regex -> getListByRegex(result?.toString() ?: "", r)
                }
            } else {
                result = getSingle(r, result)
            }
        }
        return elements
    }

    fun getElement(ruleStr: String): Any? = getElements(ruleStr).firstOrNull()

    private fun getSingle(r: SourceRule, result: Any?): Any? {
        val v = when (r.mode) {
            Mode.Js -> evalJS(r.rule, result)
            Mode.Regex -> getStringByRegex(result?.toString() ?: "", r)
            Mode.Json -> jsonPath().getString(result, r.rule)
            Mode.XPath -> xPath().getString(result, r.rule)
            Mode.Default -> jSoup().getString(result, r.rule)
        }
        return if (r.replaceRegex.isNotEmpty() && v != null) replaceRegex(v.toString(), r) else v
    }

    /**
     * @param allInOne 列表规则 allInOne 时，以 `:` 开头视为整段 regex
     */
    fun splitSourceRule(ruleStr: String?, allInOne: Boolean = false): List<SourceRule> {
        if (ruleStr.isNullOrEmpty()) return emptyList()
        val out = ArrayList<SourceRule>()
        var mode = Mode.Default
        var start = 0
        if (allInOne && ruleStr.startsWith(":")) {
            mode = Mode.Regex
            isRegex = true
            start = 1
        } else if (isRegex) {
            mode = Mode.Regex
        }

        // JS_PATTERN: <js>...</js> or @js: ...
        val jsPattern = Pattern.compile(
            """<js>([\s\S]*?)</js>|@js[:：]([\s\S]*)""",
            Pattern.CASE_INSENSITIVE
        )
        val m = jsPattern.matcher(ruleStr)
        var cursor = start
        var foundJs = false
        while (m.find()) {
            foundJs = true
            if (m.start() > cursor) {
                val chunk = ruleStr.substring(cursor, m.start()).trim()
                if (chunk.isNotEmpty()) out += SourceRule(chunk, mode)
            }
            val js = m.group(1) ?: m.group(2) ?: ""
            out += SourceRule(js, Mode.Js)
            cursor = m.end()
        }
        if (foundJs) {
            if (cursor < ruleStr.length) {
                val tail = ruleStr.substring(cursor).trim()
                if (tail.isNotEmpty()) out += SourceRule(tail, mode)
            }
            return out
        }

        // single rule (no js blocks)
        val rest = ruleStr.substring(start).trim()
        if (rest.isEmpty()) return out
        out += SourceRule(rest, mode)
        return out
    }

    fun put(key: String, value: String): String {
        chapter?.let {
            // BookChapter may not implement putVariable in business entity
            ruleData?.putVariable(key, value)
            return value
        }
        book?.let {
            ruleData?.putVariable(key, value)
            return value
        }
        ruleData?.putVariable(key, value)
        return value
    }

    fun get(key: String): String {
        if (key == "bookName") return book?.let {
            (it as? Book)?.name ?: ""
        } ?: ""
        if (key == "title") return chapter?.title ?: ""
        return chapter?.let { null }
            ?: ruleData?.getVariable(key)
            ?: ""
    }

    override fun evalJS(jsStr: String, result: Any?): Any? {
        val cx = Context.enter()
        try {
            cx.optimizationLevel = -1
            val scope: Scriptable = cx.initStandardObjects()
            scope.put("java", scope, this)
            scope.put("cookie", scope, CookieStore(getUserNameSpace()))
            scope.put("cache", scope, CacheManager(getUserNameSpace()))
            scope.put("source", scope, source)
            scope.put("book", scope, book)
            scope.put("result", scope, result)
            scope.put("baseUrl", scope, baseUrl)
            scope.put("chapter", scope, chapter)
            scope.put("title", scope, chapter?.title)
            scope.put("src", scope, content)
            scope.put("nextChapterUrl", scope, nextChapterUrl)
            return cx.evaluateString(scope, jsStr, "js", 1, null)
        } catch (e: Exception) {
            debugLog?.log(source?.toString(), "js error: ${e.message}")
            return null
        } finally {
            Context.exit()
        }
    }

    /**
     * allInOne / 普通 regex：
     * - rule 形如 `regex` 或 `regex&&group` — 取 group
     * - 以 `:` 开头时去掉
     */
    private fun getStringByRegex(text: String, rule: SourceRule): String {
        var r = rule.rule
        if (r.startsWith(":")) r = r.substring(1)
        // ## already stripped in SourceRule
        val group = 1
        return try {
            val p = Pattern.compile(r, Pattern.MULTILINE)
            val m = p.matcher(text)
            if (m.find()) {
                if (m.groupCount() >= group) m.group(group) ?: m.group(0) ?: ""
                else m.group(0) ?: ""
            } else ""
        } catch (_: Exception) {
            ""
        }
    }

    private fun getListByRegex(text: String, rule: SourceRule): List<String> {
        var r = rule.rule
        if (r.startsWith(":")) r = r.substring(1)
        val list = ArrayList<String>()
        try {
            val p = Pattern.compile(r, Pattern.MULTILINE)
            val m = p.matcher(text)
            while (m.find()) {
                list += if (m.groupCount() >= 1) (m.group(1) ?: m.group(0) ?: "")
                else (m.group(0) ?: "")
            }
        } catch (_: Exception) {
        }
        return list
    }

    private fun replaceRegex(result: String, rule: SourceRule): String {
        if (rule.replaceRegex.isEmpty()) return result
        return try {
            val regex = Regex(rule.replaceRegex)
            if (rule.replaceFirst) regex.replaceFirst(result, rule.replacement)
            else regex.replace(result, rule.replacement)
        } catch (_: Exception) {
            if (rule.replaceFirst) result.replaceFirst(rule.replaceRegex, rule.replacement)
            else result.replace(rule.replaceRegex, rule.replacement)
        }
    }

    private fun jSoup(): AnalyzeByJSoup {
        if (analyzeByJSoup == null) analyzeByJSoup = AnalyzeByJSoup(content)
        return analyzeByJSoup!!
    }

    private fun xPath(): AnalyzeByXPath {
        if (analyzeByXPath == null) analyzeByXPath = AnalyzeByXPath(content)
        return analyzeByXPath!!
    }

    private fun jsonPath(): AnalyzeByJSonPath {
        if (analyzeByJSonPath == null) analyzeByJSonPath = AnalyzeByJSonPath(content)
        return analyzeByJSonPath!!
    }

    enum class Mode { XPath, Json, Default, Js, Regex }

    /**
     * 单条规则：自动识别模式 + ## 替换 + @put:{}
     */
    data class SourceRule(
        var rule: String,
        var mode: Mode,
        var replaceRegex: String = "",
        var replacement: String = "",
        var replaceFirst: Boolean = false,
        val putMap: MutableMap<String, String> = linkedMapOf()
    ) {
        init {
            var r = rule
            // mode detection when Default placeholder
            if (mode != Mode.Js && mode != Mode.Regex) {
                when {
                    r.startsWith("@CSS:", ignoreCase = true) -> {
                        mode = Mode.Default; r = r
                    }
                    r.startsWith("@@") -> {
                        mode = Mode.Default; r = r.removePrefix("@@")
                    }
                    r.startsWith("@XPath:", ignoreCase = true) -> {
                        mode = Mode.XPath; r = r.substringAfter(':')
                    }
                    r.startsWith("@Json:", ignoreCase = true) -> {
                        mode = Mode.Json; r = r.substringAfter(':')
                    }
                    r.startsWith("$.") || r.startsWith("$[") -> mode = Mode.Json
                    r.startsWith("/") || r.startsWith("./") || r.startsWith("//") -> mode = Mode.XPath
                    r.startsWith(":") -> {
                        mode = Mode.Regex; r = r.removePrefix(":")
                    }
                    else -> { /* keep */ }
                }
            } else if (mode == Mode.Regex && r.startsWith(":")) {
                r = r.removePrefix(":")
            }
            // @put:{key:"value"}
            val putRe = Regex("""@put:(\{[^}]+})""", RegexOption.IGNORE_CASE)
            putRe.findAll(r).forEach { m ->
                val json = m.groupValues[1]
                try {
                    val o = io.vertx.core.json.JsonObject(json)
                    o.forEach { e -> putMap[e.key] = e.value?.toString() ?: "" }
                } catch (_: Exception) {
                }
                r = r.replace(m.value, "")
            }
            // ##regex##replacement  or  ##regex###replacement (first)
            if (r.contains("##")) {
                val idx = r.indexOf("##")
                val left = r.substring(0, idx)
                var right = r.substring(idx + 2)
                if (right.startsWith("#")) {
                    replaceFirst = true
                    right = right.substring(1)
                }
                val idx2 = right.indexOf("##")
                if (idx2 >= 0) {
                    replaceRegex = right.substring(0, idx2)
                    replacement = right.substring(idx2 + 2)
                } else {
                    replaceRegex = right
                    replacement = ""
                }
                r = left
            }
            rule = r.trim()
        }
    }

    companion object {
        fun evalJS(js: String, bind: Any? = null): Any? =
            AnalyzeRule().evalJS(js, bind)
    }
}

interface RuleDataInterface {
    fun getUserNameSpace(): String = "default"
    fun putVariable(key: String, value: String?) {}
    fun getVariable(key: String): String? = null
}

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
}
''',
)

# ---------------------------------------------------------------------------
# BookController: invalid book source helpers + wire getLocalChapterList
# ---------------------------------------------------------------------------
bc_path = BIZ / "com/htmake/reader/api/controller/BookController.kt"
bc = bc_path.read_text(encoding="utf-8")

if "fun addInvalidBookSource" not in bc:
    helpers = r'''
    // region ---- invalid book source cache ----

    fun getInvalidBookSourceCache(userNameSpace: String): ACache =
        ACache.get(File(ExtKt.getWorkDir("storage", "cache", "invalidBookSourceCache", userNameSpace)))

    fun isInvalidBookSource(bookSourceUrl: String, userNameSpace: String): Boolean =
        getInvalidBookSourceCache(userNameSpace).getAsString(bookSourceUrl) != null

    fun isInvalidBookSource(bookSource: BookSource, userNameSpace: String): Boolean =
        isInvalidBookSource(bookSource.bookSourceUrl, userNameSpace)

    /** jar: put with TTL 600s */
    fun addInvalidBookSource(sourceUrl: String, invalidInfo: Map<String, Any?>, userNameSpace: String) {
        getInvalidBookSourceCache(userNameSpace).put(sourceUrl, ExtKt.jsonEncode(invalidInfo), 600)
    }

    // endregion

'''
    # insert before helpers region or getShelfBookByURL
    if "fun getShelfBookByURL" in bc:
        bc = bc.replace(
            "    fun getShelfBookByURL(bookUrl: String, userNameSpace: String): Book? {",
            helpers + "    fun getShelfBookByURL(bookUrl: String, userNameSpace: String): Book? {",
        )
        print("added invalid book source helpers")
    else:
        print("WARN: no getShelfBookByURL")

# patch getLocalChapterList catch block
old_catch = '''        } catch (e: Exception) {
            mutex?.withLock {
                book.lastCheckError = e.toString()
                editShelfBook(book, userNameSpace) { it.apply { lastCheckError = e.toString() } }
            }
            throw e
        }'''

new_catch = '''        } catch (e: Exception) {
            if (!bookSource.isNullOrEmpty()) {
                val bookSourceObject = BookSource.fromJson(bookSource).getOrNull()
                if (bookSourceObject != null) {
                    addInvalidBookSource(
                        bookSourceObject.bookSourceUrl,
                        mapOf(
                            "sourceUrl" to bookSourceObject.bookSourceUrl,
                            "time" to System.currentTimeMillis(),
                            "error" to e.toString()
                        ),
                        userNameSpace
                    )
                }
            }
            mutex?.withLock {
                book.lastCheckError = e.toString()
                editShelfBook(book, userNameSpace) { it.apply { lastCheckError = e.toString() } }
            }
            throw e
        }'''

if old_catch in bc:
    bc = bc.replace(old_catch, new_catch)
    print("wired addInvalidBookSource into getLocalChapterList")
else:
    print("WARN: catch block not matched for invalid source")

# ensure BookSource import
if "import io.legado.app.data.entities.BookSource" not in bc:
    bc = bc.replace(
        "import io.legado.app.data.entities.Book\n",
        "import io.legado.app.data.entities.Book\nimport io.legado.app.data.entities.BookSource\n",
    )

bc_path.write_text(bc, encoding="utf-8", newline="\n")

# Improve getInvalidBookSources in BookControllerExtras
ex_path = BIZ / "com/htmake/reader/api/controller/BookControllerExtras.kt"
ex = ex_path.read_text(encoding="utf-8")
old_inv = '''fun BookController.getInvalidBookSources(context: RoutingContext): ReturnData {
    val rd = ReturnData()
    if (!checkAuth(context)) return rd.setData("NEED_LOGIN").setErrorMsg("请登录后使用")
    val ns = getUserNameSpace(context)
    val dir = File(ExtKt.getWorkDir("storage", "cache", "invalidBookSourceCache", ns))
    if (!dir.isDirectory) return rd.setData(emptyList<Any>())
    val list = dir.listFiles()?.mapNotNull { f ->
        try {
            JsonObject(f.readText())
        } catch (_: Exception) {
            null
        }
    } ?: emptyList()
    return rd.setData(list)
}'''

new_inv = '''fun BookController.getInvalidBookSources(context: RoutingContext): ReturnData {
    val rd = ReturnData()
    if (!checkAuth(context)) return rd.setData("NEED_LOGIN").setErrorMsg("请登录后使用")
    val ns = getUserNameSpace(context)
    val cache = getInvalidBookSourceCache(ns)
    val dir = File(ExtKt.getWorkDir("storage", "cache", "invalidBookSourceCache", ns))
    if (!dir.isDirectory) return rd.setData(emptyList<Any>())
    val list = ArrayList<Map<String, Any?>>()
    dir.listFiles()?.forEach { f ->
        val raw = cache.getByHashCode(f.name) ?: return@forEach
        try {
            // strip expire header already handled by getByHashCode
            val text = raw.removePrefix("expireAt=").let {
                if (it.contains('\n') && raw.startsWith("expireAt=")) it.substringAfter('\n') else raw
            }
            val o = JsonObject(text)
            list += o.map
        } catch (_: Exception) {
            try {
                list += ExtKt.asJsonObject(raw)?.map ?: mapOf("raw" to raw)
            } catch (_: Exception) {
            }
        }
    }
    return rd.setData(list)
}

/** 跳过已标记失效的书源（多源搜索用） */
fun BookController.filterEnabledSources(ns: String, sources: JsonArray): List<JsonObject> {
    return (0 until sources.size()).mapNotNull { i ->
        val o = sources.getJsonObject(i) ?: return@mapNotNull null
        if (o.getBoolean("enabled", true) == false) return@mapNotNull null
        val url = o.getString("bookSourceUrl") ?: return@mapNotNull o
        if (isInvalidBookSource(url, ns)) null else o
    }
}'''

if old_inv in ex:
    ex = ex.replace(old_inv, new_inv)
    print("improved getInvalidBookSources")
else:
    print("WARN: getInvalidBookSources not exact")

# wire filter into searchMultiInternal if possible
if "filterEnabledSources" not in ex or "val enabled = (0 until sources.size())" in ex:
    # replace enabled selection in searchMultiInternal
    ex2 = ex.replace(
        '''    val enabled = (0 until sources.size()).mapNotNull { i ->
        val o = sources.getJsonObject(i) ?: return@mapNotNull null
        if (o.getBoolean("enabled", true) == false) null else o
    }
    enabled.chunked(concurrent).forEach { batch ->
        batch.map { o ->
            async {
                try {
                    withTimeoutOrNull(perSourceTimeoutMs) {
                        WebBook(o.encode(), false, null, ns).searchBook(key, page)
                    } ?: emptyList()
                } catch (_: Exception) {
                    emptyList()
                }
            }
        }.awaitAll().forEach { list ->
            lock.withLock { out.addAll(list) }
        }
    }''',
        '''    val enabled = filterEnabledSources(ns, sources)
    enabled.chunked(concurrent).forEach { batch ->
        batch.map { o ->
            async {
                try {
                    withTimeoutOrNull(perSourceTimeoutMs) {
                        WebBook(o.encode(), false, null, ns).searchBook(key, page)
                    } ?: emptyList()
                } catch (e: Exception) {
                    val url = o.getString("bookSourceUrl")
                    if (url != null) {
                        addInvalidBookSource(
                            url,
                            mapOf("sourceUrl" to url, "time" to System.currentTimeMillis(), "error" to e.toString()),
                            ns
                        )
                    }
                    emptyList()
                }
            }
        }.awaitAll().forEach { list ->
            lock.withLock { out.addAll(list) }
        }
    }''',
    )
    if ex2 != ex:
        ex = ex2
        print("search multi skips invalid + marks failures")

ex_path.write_text(ex, encoding="utf-8", newline="\n")

# ---------------------------------------------------------------------------
# Book entity as RuleDataInterface optional variables
# ---------------------------------------------------------------------------
book_p = BIZ / "io/legado/app/data/entities/Book.kt"
bt = book_p.read_text(encoding="utf-8")
if "RuleDataInterface" not in bt:
    bt = bt.replace(
        "package io.legado.app.data.entities\n",
        "package io.legado.app.data.entities\n\nimport io.legado.app.model.analyzeRule.RuleDataInterface\n",
    )
    bt = bt.replace(
        ") {",
        ") : RuleDataInterface {",
        1,
    )
    if "override fun getUserNameSpace" not in bt:
        # add before closing of class
        idx = bt.rfind("}")
        bt = bt[:idx] + '''
    private val variableMap = linkedMapOf<String, String>()
    override fun getUserNameSpace(): String = userNameSpace ?: "default"
    override fun putVariable(key: String, value: String?) {
        if (value == null) variableMap.remove(key) else variableMap[key] = value
    }
    override fun getVariable(key: String): String? = variableMap[key]
''' + bt[idx:]
    book_p.write_text(bt, encoding="utf-8", newline="\n")
    print("Book implements RuleDataInterface")

# ---------------------------------------------------------------------------
# README / INDEX
# ---------------------------------------------------------------------------
readme = BIZ / "README.md"
r = readme.read_text(encoding="utf-8", errors="replace")
if "Phase 10" not in r:
    r = r.rstrip() + """


## Phase 10 增量

- **UserConfig**：主题/字体/翻页/TTS/并发等键；`saveUserConfig` 写 `@updateTime`，支持 `merge=true`
- **getUserConfig**：无文件时「没有备份文件」+ defaults
- **uploadFile / deleteFile**：用户 assets 上传与安全删除
- **invalidBookSource 缓存**：`addInvalidBookSource` TTL 600s；目录失败写入；多源搜索跳过/标记
- **getInvalidBookSources**：按 ACache hash 读取
- **AnalyzeRule**：allInOne `:`、`<js>`/`@js` 拆分、`##`/`###` 替换、`@put`、`put/get`、evalJS 全绑定
- **CookieStore / CacheManager**：JS `cookie` / `cache` 绑定
- **ACache**：过期头 + `getByHashCode`
"""
    readme.write_text(r, encoding="utf-8", newline="\n")
    print("README phase10")

index = BIZ / "INDEX.md"
ix = index.read_text(encoding="utf-8", errors="replace")
if "UserConfig" not in ix:
    ix = ix.replace(
        "| User |",
        "| UserConfig | `com/htmake/reader/config/UserConfig.kt` | userConfig.json 键与 defaults |\n| User |",
    )
if "CookieStore" not in ix:
    ix = ix.replace(
        "| ContentProcessor |",
        "| CookieStore | `io/legado/app/help/http/CookieStore.kt` | JS cookie 绑定 |\n| CacheManager | `io/legado/app/help/CacheManager.kt` | JS cache 绑定 |\n| ContentProcessor |",
    )
kt = sum(1 for _ in BIZ.rglob("*.kt"))
lines = sum(len(p.read_text(encoding="utf-8", errors="replace").splitlines()) for p in BIZ.rglob("*.kt"))
ix = ix.rstrip() + f"\n\n- phase10 后 business `.kt` 文件数: **{kt}** / 约 **{lines}** 行\n"
index.write_text(ix, encoding="utf-8", newline="\n")
print(f"DONE phase10: {kt} files, ~{lines} lines")
