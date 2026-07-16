/** Business rewrite from reader-pro-3.2.14.jar — phase11. */

package io.legado.app.model.analyzeRule

import io.legado.app.data.entities.BaseSource
import io.legado.app.data.entities.Book
import io.legado.app.data.entities.BookChapter
import io.legado.app.help.http.CookieStore
import io.legado.app.help.http.StrResponse
import io.legado.app.model.DebugLog
import okhttp3.MediaType
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody
import okhttp3.Response
import java.util.concurrent.TimeUnit

/**
 * URL 模板 + HTTP。注入 CookieStore；保存 Set-Cookie；可选 loginCheckJs。
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
    var finalUrl: String = expand(mUrl)
    var body: String? = null
    var method: String = "GET"

    private fun expand(url: String): String {
        var u = url
        if (key != null) {
            u = u.replace("{{key}}", key).replace("{{keyword}}", key)
        }
        if (page != null) u = u.replace("{{page}}", page.toString())
        if (speakText != null) u = u.replace("{{speakText}}", speakText)
        return u
    }

    private fun client(): OkHttpClient = OkHttpClient.Builder()
        .connectTimeout(20, TimeUnit.SECONDS)
        .readTimeout(60, TimeUnit.SECONDS)
        .followRedirects(true)
        .build()

    private fun cookieStore(): CookieStore? {
        val ns = source?.getUserNameSpace() ?: ruleData?.userNameSpace ?: return null
        return CookieStore(ns)
    }

    private fun headers(): Map<String, String> {
        val map = linkedMapOf<String, String>()
        source?.getHeaderMap(withLogin = true)?.let { map.putAll(it) }
        headerMapF?.let { map.putAll(it) }
        // Cookie
        cookieStore()?.let { cs ->
            val c = cs.getCookie(finalUrl)
            if (c.isNotBlank() && !map.keys.any { it.equals("Cookie", true) }) {
                map["Cookie"] = c
            }
        }
        return map
    }

    suspend fun getStrResponseAwait(): StrResponse {
        client().newCall(buildRequest()).execute().use { resp ->
            finalUrl = resp.request().url().toString()
            saveCookies(resp)
            var bodyStr = resp.body()?.string()
            // loginCheckJs on source
            val checkJs = source?.getLoginCheckJs()
            if (!checkJs.isNullOrBlank()) {
                val evaluated = evalJS(checkJs, resp)
                when (evaluated) {
                    is Response -> {
                        return getStrResponseAwait() // rare re-fetch path simplified
                    }
                    is StrResponse -> return evaluated
                    is String -> bodyStr = evaluated
                }
            }
            return StrResponse(finalUrl, bodyStr)
        }
    }

    suspend fun getByteArrayAwait(): ByteArray {
        client().newCall(buildRequest()).execute().use { resp ->
            finalUrl = resp.request().url().toString()
            saveCookies(resp)
            return resp.body()?.bytes() ?: ByteArray(0)
        }
    }

    suspend fun getResponseAwait(): Response {
        val resp = client().newCall(buildRequest()).execute()
        finalUrl = resp.request().url().toString()
        saveCookies(resp)
        return resp
    }

    private fun saveCookies(resp: Response) {
        val cs = cookieStore() ?: return
        val setCookies = resp.headers("Set-Cookie")
        if (setCookies.isNotEmpty()) {
            cs.applySetCookie(finalUrl, setCookies)
        }
    }

    private fun buildRequest(): Request {
        val b = Request.Builder().url(finalUrl)
        headers().forEach { (k, v) -> b.header(k, v) }
        return when (method.uppercase()) {
            "POST" -> b.post(
                RequestBody.create(MediaType.parse("application/json; charset=utf-8"), body ?: "")
            ).build()
            else -> b.get().build()
        }
    }

    fun evalJS(js: String, result: Any?): Any? =
        AnalyzeRule(ruleData as? io.legado.app.model.analyzeRule.RuleDataInterface, source, debugLog)
            .evalJS(js, result)
}
