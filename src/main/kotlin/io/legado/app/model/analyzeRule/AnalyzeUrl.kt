package io.legado.app.model.analyzeRule

import io.legado.app.data.entities.BaseSource
import io.legado.app.data.entities.Book
import io.legado.app.data.entities.BookChapter
import io.legado.app.help.http.CookieStore
import io.legado.app.help.http.StrResponse
import io.legado.app.model.DebugLog
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody.Companion.toRequestBody
import okhttp3.Response
import java.util.concurrent.TimeUnit

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
        if (key != null) u = u.replace("{{key}}", key).replace("{{keyword}}", key)
        if (page != null) u = u.replace("{{page}}", page.toString())
        if (speakText != null) u = u.replace("{{speakText}}", speakText)
        return u
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
        headerMapF?.let { map.putAll(it) }
        cookieStore()?.let { cs ->
            val c = cs.getCookie(finalUrl)
            if (c.isNotBlank() && map.keys.none { it.equals("Cookie", true) }) map["Cookie"] = c
        }
        return map
    }

    suspend fun getStrResponseAwait(): StrResponse {
        client().newCall(buildRequest()).execute().use { resp ->
            finalUrl = resp.request.url.toString()
            saveCookies(resp)
            return StrResponse(finalUrl, resp.body?.string())
        }
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

    private fun saveCookies(resp: Response) {
        val cs = cookieStore() ?: return
        val set = resp.headers("Set-Cookie")
        if (set.isNotEmpty()) cs.applySetCookie(finalUrl, set)
    }

    private fun buildRequest(): Request {
        val b = Request.Builder().url(finalUrl)
        headers().forEach { (k, v) -> b.header(k, v) }
        return if (method.equals("POST", true)) {
            val media = "application/json; charset=utf-8".toMediaTypeOrNull()
            b.post((body ?: "").toRequestBody(media)).build()
        } else b.get().build()
    }

    fun evalJS(js: String, result: Any?): Any? =
        AnalyzeRule(ruleData as? RuleDataInterface, source, debugLog).evalJS(js, result)
}
