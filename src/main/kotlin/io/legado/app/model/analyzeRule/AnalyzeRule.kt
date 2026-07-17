package io.legado.app.model.analyzeRule

import io.legado.app.data.entities.BaseBook
import io.legado.app.data.entities.BaseSource
import io.legado.app.data.entities.Book
import io.legado.app.data.entities.BookChapter
import io.legado.app.help.CacheManager
import io.legado.app.help.JsExtensions
import io.legado.app.help.http.CookieStore
import io.legado.app.model.DebugLog
import io.legado.app.utils.NetworkUtils
import org.mozilla.javascript.Context
import org.mozilla.javascript.Scriptable
import java.util.regex.Pattern

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

    private var isJSON = false
    private var isRegex = false
    private var jsoup: AnalyzeByJSoup? = null
    private var xpath: AnalyzeByXPath? = null
    private var jsonp: AnalyzeByJSonPath? = null

    val book: BaseBook? get() = ruleData as? BaseBook

    constructor(book: Book?, source: BaseSource?, debugLog: DebugLog?) :
        this(book as? RuleDataInterface, source, debugLog)

    fun setContent(content: Any?, baseUrl: String? = null): AnalyzeRule {
        this.content = content
        if (baseUrl != null) this.baseUrl = baseUrl
        jsoup = null; xpath = null; jsonp = null
        isJSON = content is String && content.trimStart().let { it.startsWith("{") || it.startsWith("[") }
        return this
    }

    override fun getSource() = source
    override fun getUserNameSpace() = ruleData?.getUserNameSpace() ?: source?.getUserNameSpace() ?: "default"
    override fun getLogger() = debugLog

    fun getString(ruleStr: String?, mContent: Any? = null, isUrl: Boolean = false): String {
        if (ruleStr.isNullOrEmpty()) return ""
        var result: Any? = mContent ?: content
        for (rule in splitSourceRule(ruleStr)) {
            result = evalRule(rule, result)
            if (rule.replaceRegex.isNotEmpty() && result != null) {
                result = replaceRegex(result.toString(), rule)
            }
        }
        val s = result?.toString() ?: ""
        return if (isUrl) NetworkUtils.getAbsoluteURL(baseUrl, s) else s
    }

    fun getStringList(
        rule: String?,
        mContent: Any? = null,
        isUrl: Boolean = false,
        allInOne: Boolean = false
    ): List<String> {
        if (rule.isNullOrEmpty()) return emptyList()
        val rules = splitSourceRule(rule, allInOne)
        var result: Any? = mContent ?: content
        var list: List<String> = emptyList()
        for ((i, r) in rules.withIndex()) {
            if (i == rules.lastIndex) {
                list = when (r.mode) {
                    Mode.Js -> listOfNotNull(evalJS(r.rule, result)?.toString())
                    Mode.Regex -> listByRegex(result?.toString() ?: "", r)
                    Mode.Json -> jpath().getStringList(result, r.rule)
                    Mode.XPath -> xpath().getStringList(result, r.rule)
                    Mode.Default -> jSoup().getStringList(result, r.rule)
                }
            } else result = evalRule(r, result)
        }
        return if (isUrl) list.map { NetworkUtils.getAbsoluteURL(baseUrl, it) } else list
    }

    fun getElements(ruleStr: String, allInOne: Boolean = false): List<Any> {
        if (ruleStr.isEmpty()) return emptyList()
        val rules = splitSourceRule(ruleStr, allInOne)
        var result: Any? = content
        var elements: List<Any> = emptyList()
        for ((i, r) in rules.withIndex()) {
            if (i == rules.lastIndex) {
                elements = when (r.mode) {
                    Mode.Json -> jpath().getElements(result, r.rule)
                    Mode.XPath -> xpath().getElements(result, r.rule)
                    Mode.Default -> jSoup().getElements(result, r.rule)
                    Mode.Js -> listOfNotNull(evalJS(r.rule, result))
                    Mode.Regex -> listByRegex(result?.toString() ?: "", r)
                }
            } else result = evalRule(r, result)
        }
        return elements
    }

    private fun evalRule(r: SourceRule, result: Any?): Any? = when (r.mode) {
        Mode.Js -> evalJS(r.rule, result)
        Mode.Regex -> stringByRegex(result?.toString() ?: "", r)
        Mode.Json -> jpath().getString(result, r.rule)
        Mode.XPath -> xpath().getString(result, r.rule)
        Mode.Default -> jSoup().getString(result, r.rule)
    }

    fun splitSourceRule(ruleStr: String?, allInOne: Boolean = false): List<SourceRule> {
        if (ruleStr.isNullOrEmpty()) return emptyList()
        var mode = Mode.Default
        var start = 0
        if (allInOne && ruleStr.startsWith(":")) {
            mode = Mode.Regex; isRegex = true; start = 1
        } else if (isRegex) mode = Mode.Regex

        val jsPat = Pattern.compile("""<js>([\s\S]*?)</js>|@js[:：]([\s\S]*)""", Pattern.CASE_INSENSITIVE)
        val m = jsPat.matcher(ruleStr)
        val out = ArrayList<SourceRule>()
        var cursor = start
        var found = false
        while (m.find()) {
            found = true
            if (m.start() > cursor) {
                val chunk = ruleStr.substring(cursor, m.start()).trim()
                if (chunk.isNotEmpty()) out += SourceRule(chunk, mode)
            }
            out += SourceRule(m.group(1) ?: m.group(2) ?: "", Mode.Js)
            cursor = m.end()
        }
        if (found) {
            if (cursor < ruleStr.length) {
                val tail = ruleStr.substring(cursor).trim()
                if (tail.isNotEmpty()) out += SourceRule(tail, mode)
            }
            return out
        }
        val rest = ruleStr.substring(start).trim()
        if (rest.isNotEmpty()) out += SourceRule(rest, mode)
        return out
    }

    override fun evalJS(jsStr: String, result: Any?): Any? {
        // Rhino evaluation
        val cx = Context.enter()
        try {
            cx.optimizationLevel = -1
            cx.languageVersion = Context.VERSION_ES6
            // 原版(RhinoScriptEngine)关闭 javaPrimitiveWrap：
            // Java 返回的 String 转成 JS 字符串而非 NativeJavaObject 包装
            cx.wrapFactory.isJavaPrimitiveWrap = false
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
            // loginInfo: AES-backed form JSON (legado scripts read this binding)
            val loginInfoRaw = source?.getLoginInfo()
            scope.put("loginInfo", scope, loginInfoRaw)
            // When result is a Map (e.g. login form), expose keys as top-level names
            // so scripts can use `username`, `password` directly.
            val formMap: Map<*, *>? = when (result) {
                is Map<*, *> -> result
                else -> source?.getLoginInfoMap()?.takeIf { it.isNotEmpty() }
            }
            formMap?.forEach { (k, v) ->
                if (k != null) {
                    val key = k.toString()
                    if (key.isNotBlank() && !scope.has(key, scope)) {
                        scope.put(key, scope, v?.toString() ?: "")
                    }
                }
            }
            return cx.evaluateString(scope, jsStr, "js", 1, null)
        } catch (e: Exception) {
            debugLog?.log(source?.toString(), "js error: ${e.message}")
            return null
        } finally {
            Context.exit()
        }
    }

    private fun stringByRegex(text: String, rule: SourceRule): String {
        var r = rule.rule
        if (r.startsWith(":")) r = r.substring(1)
        return try {
            val m = Pattern.compile(r, Pattern.MULTILINE).matcher(text)
            if (m.find()) {
                if (m.groupCount() >= 1) m.group(1) ?: m.group(0) ?: "" else m.group(0) ?: ""
            } else ""
        } catch (_: Exception) { "" }
    }

    private fun listByRegex(text: String, rule: SourceRule): List<String> {
        var r = rule.rule
        if (r.startsWith(":")) r = r.substring(1)
        val list = ArrayList<String>()
        try {
            val m = Pattern.compile(r, Pattern.MULTILINE).matcher(text)
            while (m.find()) {
                list += if (m.groupCount() >= 1) (m.group(1) ?: m.group(0) ?: "") else (m.group(0) ?: "")
            }
        } catch (_: Exception) {}
        return list
    }

    private fun replaceRegex(result: String, rule: SourceRule): String = try {
        val re = Regex(rule.replaceRegex)
        if (rule.replaceFirst) re.replaceFirst(result, rule.replacement) else re.replace(result, rule.replacement)
    } catch (_: Exception) {
        result.replace(rule.replaceRegex, rule.replacement)
    }

    private fun jSoup() = jsoup ?: AnalyzeByJSoup(content).also { jsoup = it }
    private fun xpath() = xpath ?: AnalyzeByXPath(content).also { xpath = it }
    private fun jpath() = jsonp ?: AnalyzeByJSonPath(content).also { jsonp = it }

    enum class Mode { XPath, Json, Default, Js, Regex }

    data class SourceRule(
        var rule: String,
        var mode: Mode,
        var replaceRegex: String = "",
        var replacement: String = "",
        var replaceFirst: Boolean = false
    ) {
        init {
            var r = rule
            if (mode != Mode.Js && mode != Mode.Regex) {
                when {
                    r.startsWith("@@") -> { mode = Mode.Default; r = r.removePrefix("@@") }
                    r.startsWith("@XPath:", true) -> { mode = Mode.XPath; r = r.substringAfter(':') }
                    r.startsWith("@Json:", true) -> { mode = Mode.Json; r = r.substringAfter(':') }
                    r.startsWith("$.") || r.startsWith("$[") -> mode = Mode.Json
                    r.startsWith("/") || r.startsWith("./") || r.startsWith("//") -> mode = Mode.XPath
                    r.startsWith(":") -> { mode = Mode.Regex; r = r.removePrefix(":") }
                }
            }
            if (r.contains("##")) {
                val idx = r.indexOf("##")
                val left = r.substring(0, idx)
                var right = r.substring(idx + 2)
                if (right.startsWith("#")) { replaceFirst = true; right = right.substring(1) }
                val idx2 = right.indexOf("##")
                if (idx2 >= 0) {
                    replaceRegex = right.substring(0, idx2)
                    replacement = right.substring(idx2 + 2)
                } else replaceRegex = right
                r = left
            }
            rule = r.trim()
        }
    }
}
