package io.legado.app.help

import io.legado.app.data.entities.BaseSource
import io.legado.app.help.http.StrResponse
import io.legado.app.model.DebugLog
import io.legado.app.utils.MD5Utils
import okhttp3.OkHttpClient
import okhttp3.Request
import java.util.Base64
import java.util.UUID
import java.util.concurrent.TimeUnit

interface JsExtensions {
    fun getSource(): BaseSource?
    fun getUserNameSpace(): String
    fun getLogger(): DebugLog? = null

    fun ajax(urlStr: String): String? = runCatching { connect(urlStr).body }.getOrNull()

    fun connect(urlStr: String): StrResponse {
        val client = OkHttpClient.Builder()
            .connectTimeout(15, TimeUnit.SECONDS)
            .readTimeout(30, TimeUnit.SECONDS)
            .build()
        client.newCall(Request.Builder().url(urlStr).get().build()).execute().use { resp ->
            return StrResponse(urlStr, resp.body?.string())
        }
    }

    fun base64Decode(str: String): String =
        String(Base64.getDecoder().decode(str.substringAfter(',')))

    fun base64Encode(str: String): String =
        Base64.getEncoder().encodeToString(str.toByteArray())

    fun md5Encode(str: String) = MD5Utils.md5Encode(str)
    fun md5Encode16(str: String) = MD5Utils.md5Encode16(str)
    fun randomUUID(): String = UUID.randomUUID().toString()
    fun log(msg: String): String {
        getLogger()?.log(getSource()?.toString(), msg); return msg
    }

    fun evalJS(jsStr: String, result: Any?): Any? = null
}
