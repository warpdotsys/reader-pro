package io.legado.app.help.http

import okhttp3.Headers
import okhttp3.Protocol
import okhttp3.Request
import okhttp3.Response
import okhttp3.ResponseBody

class StrResponse {
    var raw: Response
        private set
    var body: String? = null
        private set
    var errorBody: ResponseBody? = null
        private set

    constructor(rawResponse: Response, body: String?) {
        raw = rawResponse
        this.body = body
    }

    constructor(url: String, body: String?) {
        raw = Response.Builder()
            .code(200)
            .message("OK")
            .protocol(Protocol.HTTP_1_1)
            .request(Request.Builder().url(url).build())
            .build()
        this.body = body
    }

    constructor(rawResponse: Response, errorBody: ResponseBody?) {
        raw = rawResponse
        this.errorBody = errorBody
    }

    fun raw(): Response = raw

    fun url(): String = raw.networkResponse?.request?.url?.toString() ?: raw.request.url.toString()

    val url: String
        get() = url()

    fun body(): String? = body

    fun code(): Int = raw.code

    fun message(): String = raw.message

    fun headers(): Headers = raw.headers

    fun isSuccessful(): Boolean = raw.isSuccessful

    fun errorBody(): ResponseBody? = errorBody

    override fun toString(): String = raw.toString()
}
