package com.htmake.reader.utils

import io.legado.app.help.http.CookieStore
import io.legado.app.help.http.StrResponse
import io.legado.app.help.http.getProxyClient
import io.legado.app.help.http.newCallStrResponse
import io.legado.app.help.http.postJson
import io.legado.app.model.DebugLog
object RemoteWebview {
    var remoteWebviewApi: String = ""

    fun setRemoteApi(remoteApi: String) {
        remoteWebviewApi = remoteApi
    }

    suspend fun getStrResponse(
        url: String? = null,
        html: String? = null,
        encode: String? = null,
        tag: String? = null,
        headerMap: Map<String, String>? = null,
        sourceRegex: String? = null,
        javaScript: String? = null,
        proxy: String? = null,
        post: Boolean = false,
        body: String? = null,
        userNameSpace: String = "",
        debugLog: DebugLog? = null
    ): StrResponse {
        if (remoteWebviewApi.isNullOrEmpty()) {
            throw Exception("不支持webview")
        }
        var requestBody = jsonEncode(mapOf(
            "url" to url,
            "html" to html,
            "headers" to headerMap,
            "js_source" to javaScript,
            "proxy" to proxy,
            "http_method" to if (post) "POST" else "GET",
            "body" to body,

            "encode" to encode,
            "tag" to tag,
            "sourceRegex" to sourceRegex
        ))
        var remoteApi = remoteWebviewApi + "/render.html"
        val strResponse = getProxyClient(debugLog = debugLog).newCallStrResponse(0) {
            url(remoteApi)
            postJson(requestBody)
        }
        if (url != null) {
            val domain = io.legado.app.utils.NetworkUtils.getSubDomain(url)
            if (domain.isNotEmpty()) {
                val cookieList = strResponse.raw.headers("Set-Cookie")
                if (cookieList.size > 0) {
                    val cookieStore = CookieStore(userNameSpace)
                    cookieList.forEach {
                        cookieStore.replaceCookie(domain + "_cookieJar", it)
                    }
                }
            }
        }

        return StrResponse(url ?: "", strResponse.body)
    }
}