/** Business rewrite from reader-pro-3.2.14.jar — phase10. */

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
        return if (isUrl) io.legado.app.utils.NetworkUtils.getAbsoluteURL(baseUrl, s) else s
    }

    fun getStringList(rule: String?, mContent: Any? = null, isUrl: Boolean = false, allInOne: Boolean = false): List<String> {
        if (rule.isNullOrEmpty()) return emptyList()
        val rules = splitSourceRule(rule, allInOne)
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
        return if (isUrl) list.map { io.legado.app.utils.NetworkUtils.getAbsoluteURL(baseUrl, it) } else list
    }

    fun getElements(ruleStr: String, allInOne: Boolean = false): List<Any> {
        if (ruleStr.isEmpty()) return emptyList()
        val rules = splitSourceRule(ruleStr, allInOne)
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
            // loginInfo + form fields as top-level bindings (legado login scripts)
            scope.put("loginInfo", scope, source?.getLoginInfo())
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

