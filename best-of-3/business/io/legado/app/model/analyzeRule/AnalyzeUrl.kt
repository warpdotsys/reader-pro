package io.legado.app.model.analyzeRule

import io.legado.app.data.entities.BaseSource
import io.legado.app.data.entities.Book
import io.legado.app.data.entities.BookChapter
import io.legado.app.help.SourceLogin
import io.legado.app.help.http.CookieStore
import io.legado.app.help.http.StrResponse
import io.legado.app.model.DebugLog
import io.legado.app.utils.NetworkUtils
import io.vertx.core.json.JsonObject
import okhttp3.FormBody
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody.Companion.toRequestBody
import okhttp3.Response
import java.net.URLEncoder
import java.util.concurrent.TimeUnit
import java.util.regex.Pattern

/**
 * Legado-style URL template + HTTP client.
 *
 * Supports:
 * - `{{key}}` / `{{page}}` / `{{speakText}}` expansion
 * - option suffix: `url,{ "method":"POST", "body":"...", "headers":{...}, "charset":"utf-8", "type":"..." }`
 * - query form fields via option `body` map
 * - Cookie jar + Set-Cookie
 * - loginCheckJs after response
 */
class AnalyzeUrl(
    mUrl: String,
    val key: String? = null,
    val page: Int? = null,
    val speakText: String? = null,
    val speechRate: Int? = null,
    val baseUrl: String? = null,
    val source: BaseSource? = null,
    val ruleData: Book? = null,
    val chapter: BookChapter? = null,
    val headerMapF: Map<String, String>? = null,
    val debugLog: DebugLog? = null
) {
    var ruleUrl: String = mUrl
    var finalUrl: String = ""
    var body: String? = null
    var method: String = "GET"
    var type: String? = null
    var charset: String = "UTF-8"
    var retry: Int = 0
    private val headerMap = linkedMapOf<String, String>()
    private val fieldMap = linkedMapOf<String, String>()

    init {
        parse(mUrl)
    }

    private fun expand(url: String): String {
        var u = url
        if (key != null) {
            u = u.replace("{{key}}", enc(key)).replace("{{keyword}}", enc(key))
            u = u.replace("\${key}", enc(key))
        }
        if (page != null) {
            u = u.replace("{{page}}", page.toString()).replace("\${page}", page.toString())
        }
        if (speakText != null) {
            u = u.replace("{{speakText}}", enc(speakText))
        }
        return u
    }

    private fun enc(s: String): String = try {
        URLEncoder.encode(s, charset).replace("+", "%20")
    } catch (_: Exception) {
        s
    }

    /**
     * Split rule URL and optional JSON option after first unquoted comma of form:
     * `https://x.com/s?q={{key}},{"method":"POST","body":"a=1"}`
     */
    fun parse(raw: String) {
        ruleUrl = raw
        var urlPart = raw
        var optionJson: String? = null
        // find ",{" or ", {" that starts option object
        val idx = findOptionIndex(raw)
        if (idx > 0) {
            urlPart = raw.substring(0, idx).trim()
            optionJson = raw.substring(idx + 1).trim()
        }
        // also support "url@body" legacy? skip
        urlPart = expand(urlPart)
        if (optionJson != null) applyOption(optionJson)
        // relative URL
        finalUrl = if (baseUrl != null && !urlPart.startsWith("http", true)) {
            NetworkUtils.getAbsoluteURL(baseUrl, urlPart)
        } else urlPart
        // page pattern page-1 or {{page}} already done
        headerMapF?.let { headerMap.putAll(it) }
    }

    private fun findOptionIndex(raw: String): Int {
        // last occurrence of ",{" or ", {"
        val p = Pattern.compile(",\\s*\\{")
        val m = p.matcher(raw)
        var last = -1
        while (m.find()) last = m.start()
        // only treat as option if looks like JSON after comma
        if (last > 0 && raw.indexOf('{', last) >= 0) return last
        return -1
    }

    private fun applyOption(json: String) {
        try {
            val o = JsonObject(json)
            o.getString("method")?.let { method = it.uppercase() }
            o.getString("charset")?.let { charset = it }
            o.getString("type")?.let { type = it }
            o.getInteger("retry")?.let { retry = it }
            when (val b = o.getValue("body")) {
                is String -> body = expand(b)
                is JsonObject -> {
                    b.forEach { (k, v) -> fieldMap[k] = expand(v?.toString() ?: "") }
                    body = fieldMap.entries.joinToString("&") { (k, v) ->
                        URLEncoder.encode(k, charset) + "=" + URLEncoder.encode(v, charset)
                    }
                    if (method == "GET") method = "POST"
                }
            }
            o.getJsonObject("headers")?.forEach { (k, v) ->
                headerMap[k] = v?.toString() ?: ""
            }
            o.getString("webJs") // reserved
            if (o.containsKey("webView") || o.getBoolean("useWebView", false) == true) {
                // webview not supported server-side; leave flag
            }
        } catch (e: Exception) {
            debugLog?.log(source?.toString(), "AnalyzeUrl option parse: ${e.message}")
        }
    }

    private fun client() = OkHttpClient.Builder()
        .connectTimeout(20, TimeUnit.SECONDS)
        .readTimeout(60, TimeUnit.SECONDS)
        .followRedirects(true)
        .build()

    private fun cookieStore(): CookieStore? {
        val ns = source?.getUserNameSpace() ?: ruleData?.namespace ?: return null
        return CookieStore(ns)
    }

    private fun headers(): Map<String, String> {
        val map = linkedMapOf<String, String>()
        source?.getHeaderMap(true)?.let { map.putAll(it) }
        map.putAll(headerMap)
        cookieStore()?.let { cs ->
            val c = cs.getCookie(finalUrl)
            if (c.isNotBlank() && map.keys.none { it.equals("Cookie", true) }) map["Cookie"] = c
        }
        if (type != null && map.keys.none { it.equals("Content-Type", true) } && method.equals("POST", true)) {
            map["Content-Type"] = type!!
        }
        return map
    }

    suspend fun getStrResponseAwait(): StrResponse {
        var lastErr: Exception? = null
        val attempts = (retry + 1).coerceAtLeast(1)
        repeat(attempts) {
            try {
                client().newCall(buildRequest()).execute().use { resp ->
                    finalUrl = resp.request.url.toString()
                    saveCookies(resp)
                    var bodyStr = resp.body?.string()
                    bodyStr = runLoginCheck(bodyStr, resp)
                    return StrResponse(finalUrl, bodyStr)
                }
            } catch (e: Exception) {
                lastErr = e
            }
        }
        throw lastErr ?: IllegalStateException("request failed")
    }

    suspend fun getByteArrayAwait(): ByteArray {
        client().newCall(buildRequest()).execute().use { resp ->
            finalUrl = resp.request.url.toString()
            saveCookies(resp)
            return resp.body?.bytes() ?: ByteArray(0)
        }
    }

    suspend fun getResponseAwait(): Response {
        val resp = client().newCall(buildRequest()).execute()
        finalUrl = resp.request.url.toString()
        saveCookies(resp)
        return resp
    }

    private fun runLoginCheck(bodyStr: String?, resp: Response): String? {
        val checkJs = source?.getLoginCheckJs()
        if (checkJs.isNullOrBlank()) return bodyStr
        return try {
            val evaluated = SourceLogin.checkLogin(source!!, bodyStr, finalUrl, debugLog)
            when (evaluated) {
                is String -> evaluated
                is StrResponse -> evaluated.body
                else -> bodyStr
            }
        } catch (_: Exception) {
            bodyStr
        }
    }

    private fun saveCookies(resp: Response) {
        val cs = cookieStore() ?: return
        val set = resp.headers("Set-Cookie")
        if (set.isNotEmpty()) cs.applySetCookie(finalUrl, set)
    }

    private fun buildRequest(): Request {
        val b = Request.Builder().url(finalUrl)
        headers().forEach { (k, v) -> b.header(k, v) }
        return if (method.equals("POST", true)) {
            val contentType = headers().entries.firstOrNull { it.key.equals("Content-Type", true) }?.value
                ?: type
                ?: "application/x-www-form-urlencoded;charset=$charset"
            when {
                fieldMap.isNotEmpty() && (contentType.contains("form", true) || type == null) -> {
                    val fb = FormBody.Builder()
                    fieldMap.forEach { (k, v) -> fb.add(k, v) }
                    b.post(fb.build()).build()
                }
                else -> {
                    val media = contentType.toMediaTypeOrNull()
                    b.post((body ?: "").toRequestBody(media)).build()
                }
            }
        } else b.get().build()
    }

    fun evalJS(js: String, result: Any?): Any? =
        AnalyzeRule(ruleData as? RuleDataInterface, source, debugLog).evalJS(js, result)

    /** Exposed for tests */
    fun getHeaderMap(): Map<String, String> = headers()
    fun getFieldMap(): Map<String, String> = fieldMap.toMap()
}
