package io.legado.app.help

import io.legado.app.data.entities.BaseSource
import io.legado.app.data.entities.BookSource
import io.legado.app.model.DebugLog
import io.legado.app.model.analyzeRule.AnalyzeRule
import io.legado.app.model.analyzeRule.AnalyzeUrl
import io.vertx.core.json.JsonObject

/**
 * Book source login helpers (legado BaseSource loginUrl / loginCheckJs / loginHeader).
 */
object SourceLogin {

    /**
     * Execute loginUrl when it is JS (`@js:` / `<js>`).
     * Script may call `java.putLoginHeader(json)` via bindings if exposed;
     * we also accept return value as header JSON string.
     */
    fun login(source: BaseSource, debugLog: DebugLog? = null): Boolean {
        val loginJs = source.getLoginJs() ?: return false
        val rule = AnalyzeRule(null, source, debugLog)
        // expose helper on scope through java = AnalyzeRule which is JsExtensions
        val result = rule.evalJS(loginJs, null)
        when (result) {
            is String -> if (result.isNotBlank() && (result.trimStart().startsWith("{") || result.contains(":"))) {
                source.putLoginHeader(result)
            }
            is Map<*, *> -> {
                val o = JsonObject()
                result.forEach { (k, v) -> if (k != null) o.put(k.toString(), v?.toString() ?: "") }
                source.putLoginHeader(o.encode())
            }
        }
        return source.getLoginHeader() != null
    }

    fun BaseSource.getLoginJs(): String? {
        val loginJs = getLoginUrl() ?: return null
        return when {
            loginJs.startsWith("@js:", ignoreCase = true) -> loginJs.substring(4)
            loginJs.startsWith("<js>", ignoreCase = true) -> {
                val end = loginJs.lastIndexOf('<')
                if (end > 4) loginJs.substring(4, end) else loginJs.removePrefix("<js>").removeSuffix("</js>")
            }
            loginJs.startsWith("http://") || loginJs.startsWith("https://") -> null // URL form: open in client
            else -> loginJs // treat as JS body
        }
    }

    /** Run loginCheckJs against last response body/url if provided. */
    fun checkLogin(
        source: BaseSource,
        responseBody: String?,
        baseUrl: String?,
        debugLog: DebugLog? = null
    ): Any? {
        val js = source.getLoginCheckJs() ?: return responseBody
        if (js.isBlank()) return responseBody
        val rule = AnalyzeRule(null, source, debugLog)
        rule.setContent(responseBody ?: "", baseUrl)
        return rule.evalJS(js, responseBody)
    }

    suspend fun ensureLoginIfNeeded(source: BookSource, debugLog: DebugLog? = null) {
        if (source.getLoginHeader() != null) return
        val loginUrl = source.getLoginUrl() ?: return
        if (loginUrl.startsWith("http://") || loginUrl.startsWith("https://")) {
            // fetch login page then run check js if any
            runCatching {
                val body = AnalyzeUrl(mUrl = loginUrl, source = source, debugLog = debugLog)
                    .getStrResponseAwait().body
                checkLogin(source, body, loginUrl, debugLog)
            }
        } else {
            login(source, debugLog)
        }
    }

    fun clearLogin(source: BaseSource) {
        CacheManager(source.getUserNameSpace()).delete("loginHeader_${source.getKey()}")
    }
}
