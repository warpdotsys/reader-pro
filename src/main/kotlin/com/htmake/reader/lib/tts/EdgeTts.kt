package com.htmake.reader.lib.tts

import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody.Companion.toRequestBody
import java.net.URLEncoder
import java.util.concurrent.TimeUnit

/**
 * TTS helpers used by /reader3/book/tts.
 *
 * Priority:
 * 1. Optional classpath jar service: com.htmake.reader.lib.tts.service.TTSService (original fat-jar)
 * 2. text-to-speech.cn form API (fallback, same as jar ttsByTextToSpeechCn)
 */
object EdgeTts {
    const val DEFAULT_VOICE = "zh-CN-XiaoxiaoNeural"

    private val client = OkHttpClient.Builder()
        .connectTimeout(15, TimeUnit.SECONDS)
        .readTimeout(60, TimeUnit.SECONDS)
        .build()

    fun buildSsml(text: String, voice: String, rate: String = "0", pitch: String = "0%"): String {
        val esc = text.replace("&", "&amp;").replace("<", "&lt;").replace(">", "&gt;")
        val rateAttr = if (rate.endsWith("%") || rate.startsWith("+") || rate.startsWith("-")) rate else "+0%"
        val pitchAttr = if (pitch.endsWith("%")) pitch else "$pitch%"
        return """
            <speak version="1.0" xmlns="http://www.w3.org/2001/10/synthesis"
                   xmlns:mstts="https://www.w3.org/2001/mstts" xml:lang="zh-CN">
              <voice name="$voice">
                <mstts:express-as style="chat">
                  <prosody rate="$rateAttr" pitch="$pitchAttr">$esc</prosody>
                </mstts:express-as>
              </voice>
            </speak>
        """.trimIndent()
    }

    fun synthesize(text: String, voice: String = DEFAULT_VOICE, rate: String = "0", pitch: String = "0%"): ByteArray {
        // try original TTSService via reflection
        runCatching {
            return synthesizeViaJarService(text, voice, rate, pitch)
        }
        return synthesizeTextToSpeechCn(text, voice, rate, pitch)
    }

    fun synthesizeViaJarService(text: String, voice: String, rate: String, pitch: String): ByteArray {
        val voiceEnumCls = Class.forName("com.htmake.reader.lib.tts.constant.VoiceEnum")
        val fromSort = voiceEnumCls.getMethod("fromSortName", String::class.java)
        val voiceEnum = fromSort.invoke(null, voice)
            ?: voiceEnumCls.getField("zh_CN_XiaoxiaoNeural").get(null)
        val ssmlCls = Class.forName("com.htmake.reader.lib.tts.model.SSML")
        var builder = ssmlCls.getMethod("builder").invoke(null)
        builder = builder.javaClass.getMethod("synthesisText", String::class.java).invoke(builder, text)
        builder = builder.javaClass.getMethod("voice", voiceEnumCls).invoke(builder, voiceEnum)
        builder = builder.javaClass.getMethod("rate", String::class.java).invoke(builder, rate)
        builder = builder.javaClass.getMethod("pitch", String::class.java).invoke(builder, pitch)
        val styleEnum = Class.forName("com.htmake.reader.lib.tts.constant.TtsStyleEnum").getField("chat").get(null)
        builder = builder.javaClass.getMethod(
            "style",
            Class.forName("com.htmake.reader.lib.tts.constant.TtsStyleEnum")
        ).invoke(builder, styleEnum)
        val ssml = builder.javaClass.getMethod("build").invoke(builder)
        val svcCls = Class.forName("com.htmake.reader.lib.tts.service.TTSService")
        val svc = svcCls.getMethod("builder").invoke(null).javaClass.getMethod("build")
            .invoke(svcCls.getMethod("builder").invoke(null))
        return svc.javaClass.getMethod("sendText", ssmlCls).invoke(svc, ssml) as ByteArray
    }

    fun synthesizeTextToSpeechCn(
        text: String,
        voice: String = DEFAULT_VOICE,
        rate: String = "0",
        pitch: String = "0"
    ): ByteArray {
        val form = linkedMapOf(
            "language" to "中文（普通话，简体）",
            "voice" to voice.ifBlank { DEFAULT_VOICE },
            "text" to text,
            "role" to "0",
            "style" to "0",
            "rate" to rate.removeSuffix("%"),
            "pitch" to pitch.removeSuffix("%"),
            "kbitrate" to "audio-16khz-32kbitrate-mono-mp3",
            "silence" to "",
            "styledegree" to "1",
            "user_id" to "",
            "yzm" to ""
        )
        val body = form.entries.joinToString("&") { (k, v) ->
            URLEncoder.encode(k, "UTF-8") + "=" + URLEncoder.encode(v, "UTF-8")
        }
        val req = Request.Builder()
            .url("https://www.text-to-speech.cn/getSpeek.php")
            .post(body.toRequestBody("application/x-www-form-urlencoded".toMediaTypeOrNull()))
            .header("Origin", "https://www.text-to-speech.cn")
            .header("Referer", "https://www.text-to-speech.cn/")
            .header(
                "User-Agent",
                "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/120.0.0.0 Safari/537.36"
            )
            .build()
        client.newCall(req).execute().use { resp ->
            if (!resp.isSuccessful) error("TTS HTTP ${resp.code}")
            return resp.body?.bytes() ?: ByteArray(0)
        }
    }
}
