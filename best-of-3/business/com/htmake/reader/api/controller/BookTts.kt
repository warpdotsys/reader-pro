/** Business rewrite from reader-pro-3.2.14.jar — phase9. */

package com.htmake.reader.api.controller

import com.htmake.reader.api.ReturnData
import com.htmake.reader.lib.tts.EdgeTts
import com.htmake.reader.utils.ExtKt
import io.legado.app.data.entities.HttpTTS
import io.vertx.core.buffer.Buffer
import io.vertx.core.http.HttpMethod
import io.vertx.core.http.HttpServerResponse
import io.vertx.core.json.JsonArray
import io.vertx.core.json.JsonObject
import io.vertx.ext.web.RoutingContext
import java.util.Base64

/**
 * /reader3/book/tts — type=edge | textToSpeechCn | api(voice=HttpTTS.name)
 * 返回 null 表示响应已写出（音频流 / base64 JSON）。
 */
suspend fun BookController.textToSpeech(context: RoutingContext): ReturnData? {
    if (!checkAuth(context)) {
        context.response().setStatusCode(403).end("未登录")
        return null
    }
    val text = pStr(context, "text") ?: ""
    var type = pStr(context, "type") ?: ""
    if (type.isEmpty()) type = "edge"
    if (text.isEmpty()) {
        context.response().setStatusCode(404).end("参数错误")
        return null
    }
    val options = mapOf(
        "voice" to (pStr(context, "voice") ?: ""),
        "pitch" to (pStr(context, "pitch") ?: ""),
        "rate" to (pStr(context, "rate") ?: ""),
        "base64" to (pStr(context, "base64") ?: "")
    )
    val resp = context.response()
    try {
        when (type) {
            "edge" -> ttsByEdge(resp, text, options)
            "textToSpeechCn" -> ttsByTextToSpeechCn(resp, text, options)
            else -> ttsByApi(resp, text, getUserNameSpace(context), options)
        }
    } catch (e: Exception) {
        if (!resp.ended()) {
            resp.setStatusCode(500).end(e.message ?: "tts error")
        }
    }
    return null
}

/** 兼容旧路由扩展名 tts → textToSpeech */
suspend fun BookController.tts(context: RoutingContext): ReturnData? = textToSpeech(context)

suspend fun BookController.ttsByEdge(
    response: HttpServerResponse,
    text: String,
    options: Map<String, String>? = null
) {
    var voice = EdgeTts.DEFAULT_VOICE
    var rate = "0"
    var pitch = "0%"
    options?.let {
        it["voice"]?.takeIf { v -> v.isNotBlank() }?.let { voice = it }
        it["rate"]?.takeIf { v -> v.isNotBlank() }?.let { rate = it }
        it["pitch"]?.takeIf { v -> v.isNotBlank() }?.let { p ->
            pitch = if (p.endsWith("%")) p else "$p%"
        }
    }
    val audio = EdgeTts.synthesizeViaJarService(text, voice, rate, pitch)
    endAudio(response, audio, "audio/mpeg", options)
}

suspend fun BookController.ttsByTextToSpeechCn(
    response: HttpServerResponse,
    text: String,
    options: Map<String, String>? = null
) {
    val audio = EdgeTts.synthesizeTextToSpeechCn(text, options ?: emptyMap())
    endAudio(response, audio, "audio/mpeg", options)
}

suspend fun BookController.ttsByApi(
    response: HttpServerResponse,
    text: String,
    userNameSpace: String,
    options: Map<String, String>? = null
) {
    val voice = options?.get("voice").orEmpty()
    if (voice.isEmpty()) {
        response.setStatusCode(404).end()
        return
    }
    val httpTTS = getHttpTTSByName(voice, userNameSpace)
    if (httpTTS == null) {
        response.setStatusCode(404).end()
        return
    }
    val rate = options?.get("rate")?.toDoubleOrNull() ?: 1.0
    val speechRate = (5 + (rate - 0.5) * 30).toInt()
    val stream = getSpeakStream(httpTTS, text, speechRate)
    if (stream == null) {
        response.setStatusCode(404).end()
        return
    }
    val bytes = stream.use { it.readBytes() }
    val ct = httpTTS.contentType ?: "audio/mpeg"
    endAudio(response, bytes, ct, options)
}

fun BookController.getHttpTTSByName(name: String, userNameSpace: String): HttpTTS? {
    if (name.isEmpty()) return null
    val arr = ExtKt.asJsonArray(getUserStorage(userNameSpace, "httpTTS")) ?: return null
    for (i in 0 until arr.size()) {
        val o = arr.getJsonObject(i) ?: continue
        val n = o.getString("name") ?: ""
        if (n == name) {
            return try {
                o.mapTo(HttpTTS::class.java)
            } catch (_: Exception) {
                HttpTTS(
                    name = n,
                    url = o.getString("url") ?: "",
                    contentType = o.getString("contentType"),
                    loginCheckJs = o.getString("loginCheckJs"),
                    header = o.getString("header")
                )
            }
        }
    }
    return null
}

private fun endAudio(
    response: HttpServerResponse,
    audio: ByteArray,
    contentType: String,
    options: Map<String, String>?
) {
    if (options?.get("base64") == "1") {
        val b64 = Base64.getEncoder().encodeToString(audio)
        response
            .putHeader("content-type", "application/json; charset=utf-8")
            .end(ReturnData().setData(b64).let { ExtKt.jsonEncode(it, false) })
    } else {
        response
            .putHeader("Content-Type", contentType)
            .end(Buffer.buffer(audio))
    }
}

private fun pStr(context: RoutingContext, name: String): String? {
    if (context.request().method() == HttpMethod.POST) {
        context.bodyAsJson?.getString(name)?.let { return it }
    }
    return context.queryParam(name).firstOrNull()
}
