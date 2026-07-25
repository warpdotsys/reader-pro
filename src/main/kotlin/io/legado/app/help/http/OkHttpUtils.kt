package io.legado.app.help.http

import io.legado.app.utils.EncodingDetect
import io.legado.app.utils.GSON
import io.legado.app.utils.Utf8BomUtils
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.suspendCancellableCoroutine
import kotlinx.coroutines.withContext
import okhttp3.Call
import okhttp3.Callback
import okhttp3.FormBody
import okhttp3.HttpUrl.Companion.toHttpUrl
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.MultipartBody
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import okhttp3.Response
import okhttp3.ResponseBody
import java.io.File
import java.io.IOException
import java.nio.charset.Charset
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException

suspend fun OkHttpClient.newCallResponse(retry: Int = 0, builder: Request.Builder.() -> Unit): Response =
    withContext(Dispatchers.IO) {
        val requestBuilder = Request.Builder().apply(builder)
        var response: Response? = null
        repeat(retry + 1) {
            val currentResponse = newCall(requestBuilder.build()).await()
            response = currentResponse
            if (currentResponse.isSuccessful) return@withContext currentResponse
        }
        response!!
    }

suspend fun OkHttpClient.newCallResponseBody(
    retry: Int = 0,
    builder: Request.Builder.() -> Unit
): ResponseBody = newCallResponse(retry, builder).let { it.body ?: throw IOException(it.message) }

suspend fun OkHttpClient.newCall(retry: Int = 0, builder: Request.Builder.() -> Unit): ResponseBody {
    val requestBuilder = Request.Builder().apply(builder)
    var response: Response? = null
    repeat(retry + 1) {
        val currentResponse = newCall(requestBuilder.build()).await()
        response = currentResponse
        if (currentResponse.isSuccessful) return currentResponse.body!!
    }
    val finalResponse = response!!
    return finalResponse.body ?: throw IOException(finalResponse.message)
}

suspend fun OkHttpClient.newCallStrResponse(
    retry: Int = 0,
    builder: Request.Builder.() -> Unit
): StrResponse {
    val requestBuilder = Request.Builder().apply(builder)
    var response: Response? = null
    repeat(retry + 1) {
        val currentResponse = newCall(requestBuilder.build()).await()
        response = currentResponse
        if (currentResponse.isSuccessful) return StrResponse(currentResponse, currentResponse.body!!.text())
    }
    val finalResponse = response!!
    return StrResponse(finalResponse, finalResponse.body?.text() ?: finalResponse.message)
}

suspend fun Call.await(): Response = suspendCancellableCoroutine { continuation ->
    continuation.invokeOnCancellation { cancel() }
    enqueue(object : Callback {
        override fun onFailure(call: Call, e: IOException) = continuation.resumeWithException(e)

        override fun onResponse(call: Call, response: Response) = continuation.resume(response)
    })
}

fun ResponseBody.text(encode: String? = null): String {
    val responseBytes = Utf8BomUtils.removeUTF8BOM(bytes())
    if (encode != null) return String(responseBytes, Charset.forName(encode))
    contentType()?.charset()?.let { return String(responseBytes, it) }
    return String(responseBytes, Charset.forName(EncodingDetect.getHtmlEncode(responseBytes)))
}

fun Request.Builder.addHeaders(headers: Map<String, String>) {
    headers.forEach { addHeader(it.key, it.value) }
}

fun Request.Builder.get(url: String, queryMap: Map<String, String>, encoded: Boolean = false) {
    val httpBuilder = url.toHttpUrl().newBuilder()
    queryMap.forEach {
        if (encoded) httpBuilder.addEncodedQueryParameter(it.key, it.value)
        else httpBuilder.addQueryParameter(it.key, it.value)
    }
    url(httpBuilder.build())
}

fun Request.Builder.postForm(form: Map<String, String>, encoded: Boolean = false) {
    val formBody = FormBody.Builder()
    form.forEach {
        if (encoded) formBody.addEncoded(it.key, it.value) else formBody.add(it.key, it.value)
    }
    post(formBody.build())
}

fun Request.Builder.postMultipart(type: String?, form: Map<String, Any>) {
    val multipartBody = MultipartBody.Builder()
    type?.let { multipartBody.setType(it.toMediaType()) }
    form.forEach {
        when (val value = it.value) {
            is Map<*, *> -> {
                val fileName = value["fileName"] as String
                val file = value["file"]
                val mediaType = (value["contentType"] as? String)?.toMediaType()
                val requestBody = when (file) {
                    is File -> file.asRequestBody(mediaType)
                    is ByteArray -> file.toRequestBody(mediaType)
                    is String -> file.toRequestBody(mediaType)
                    else -> GSON.toJson(file).toRequestBody(mediaType)
                }
                multipartBody.addFormDataPart(it.key, fileName, requestBody)
            }
            else -> multipartBody.addFormDataPart(it.key, value.toString())
        }
    }
    post(multipartBody.build())
}

fun Request.Builder.postJson(json: String?) {
    json?.let { post(it.toRequestBody("application/json; charset=UTF-8".toMediaType())) }
}
