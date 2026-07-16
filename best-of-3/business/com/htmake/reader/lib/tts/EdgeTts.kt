/** Business rewrite from reader-pro-3.2.14.jar — phase9. */

package com.htmake.reader.lib.tts

import okhttp3.MediaType
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody
import java.net.URLEncoder
import java.util.Base64
import java.util.UUID
import java.util.concurrent.TimeUnit

/**
 * Edge 神经 TTS 业务封装。
 *
 * jar 实际调用：
 *   TTSService.builder().build()
 *     .sendText(SSML.builder().synthesisText(text).voice(...).rate(...).pitch(...).style(chat).build())
 *
 * 此处给出 SSML 构造 + 可选直连路径；生产环境优先用 jar 内 `TTSService`。
 */
object EdgeTts {

    /** 与 VoiceEnum.fromSortName 对齐的常用 shortName */
    val DEFAULT_VOICE = "zh-CN-XiaoxiaoNeural"

    fun buildSsml(
        text: String,
        voice: String = DEFAULT_VOICE,
        rate: String = "0",
        pitch: String = "0%",
        style: String = "chat"
    ): String {
        val rateAttr = if (rate.endsWith("%") || rate.startsWith("+") || rate.startsWith("-")) rate
        else if (rate == "0") "+0%" else "${if (rate.toIntOrNull() ?: 0 >= 0) "+" else ""}$rate%"
        val pitchAttr = if (pitch.endsWith("%")) pitch else "$pitch%"
        val esc = text
            .replace("&", "&amp;")
            .replace("<", "&lt;")
            .replace(">", "&gt;")
        return """
            <speak version="1.0" xmlns="http://www.w3.org/2001/10/synthesis"
                   xmlns:mstts="https://www.w3.org/2001/mstts" xml:lang="zh-CN">
              <voice name="$voice">
                <mstts:express-as style="$style">
                  <prosody rate="$rateAttr" pitch="$pitchAttr">$esc</prosody>
                </mstts:express-as>
              </voice>
            </speak>
        """.trimIndent()
    }

    /**
     * 尝试调用 classpath 上的 TTSService（与 jar 一致）；失败则抛出明确错误。
     * 业务树不捆绑 native WebSocket 实现细节。
     */
    fun synthesizeViaJarService(text: String, voice: String, rate: String, pitch: String): ByteArray {
        return try {
            val voiceEnumCls = Class.forName("com.htmake.reader.lib.tts.constant.VoiceEnum")
            val fromSort = voiceEnumCls.getMethod("fromSortName", String::class.java)
            val voiceEnum = fromSort.invoke(null, voice)
                ?: voiceEnumCls.getField("zh_CN_XiaoxiaoNeural").get(null)

            val ssmlBuilderCls = Class.forName("com.htmake.reader.lib.tts.model.SSML\$SSMLBuilder")
            val ssmlCls = Class.forName("com.htmake.reader.lib.tts.model.SSML")
            val builderMethod = ssmlCls.getMethod("builder")
            var builder = builderMethod.invoke(null)
            builder = builder.javaClass.getMethod("synthesisText", String::class.java).invoke(builder, text)
            builder = builder.javaClass.getMethod(
                "voice",
                Class.forName("com.htmake.reader.lib.tts.constant.VoiceEnum")
            ).invoke(builder, voiceEnum)
            builder = builder.javaClass.getMethod("rate", String::class.java).invoke(builder, rate)
            builder = builder.javaClass.getMethod("pitch", String::class.java).invoke(builder, pitch)
            val styleEnum = Class.forName("com.htmake.reader.lib.tts.constant.TtsStyleEnum")
                .getField("chat").get(null)
            builder = builder.javaClass.getMethod(
                "style",
                Class.forName("com.htmake.reader.lib.tts.constant.TtsStyleEnum")
            ).invoke(builder, styleEnum)
            val ssml = builder.javaClass.getMethod("build").invoke(builder)

            val svcCls = Class.forName("com.htmake.reader.lib.tts.service.TTSService")
            val svcBuilder = svcCls.getMethod("builder").invoke(null)
            val svc = svcBuilder.javaClass.getMethod("build").invoke(svcBuilder)
            svc.javaClass.getMethod("sendText", ssmlCls).invoke(svc, ssml) as ByteArray
        } catch (e: ClassNotFoundException) {
            // fallback: empty with diagnostic — caller may use HTTP TTS
            throw IllegalStateException(
                "TTSService 不在 classpath；请使用 type=api 的 HttpTTS，或附带 jar 内 tts 库。ssml=${buildSsml(text, voice, rate, pitch).take(80)}…",
                e
            )
        }
    }

    /**
     * text-to-speech.cn 兼容接口（jar ttsByTextToSpeechCn）
     */
    fun synthesizeTextToSpeechCn(text: String, options: Map<String, String> = emptyMap()): ByteArray {
        val form = linkedMapOf(
            "language" to (options["language"] ?: "中文（普通话，简体）"),
            "voice" to (options["voice"] ?: "zh-CN-XiaoxiaoNeural"),
            "text" to text,
            "role" to (options["role"] ?: "0"),
            "style" to (options["style"] ?: "0"),
            "rate" to (options["rate"] ?: "0"),
            "pitch" to (options["pitch"] ?: "0"),
            "kbitrate" to (options["kbitrate"] ?: "audio-16khz-32kbitrate-mono-mp3"),
            "silence" to (options["silence"] ?: ""),
            "styledegree" to (options["styledegree"] ?: "1"),
            "user_id" to (options["user_id"] ?: ""),
            "yzm" to (options["yzm"] ?: "")
        )
        val body = form.entries.joinToString("&") { (k, v) ->
            URLEncoder.encode(k, "UTF-8") + "=" + URLEncoder.encode(v, "UTF-8")
        }
        val client = OkHttpClient.Builder()
            .connectTimeout(10, TimeUnit.SECONDS)
            .readTimeout(30, TimeUnit.SECONDS)
            .build()
        val req = Request.Builder()
            .url("https://www.text-to-speech.cn/getSpeek.php")
            .post(RequestBody.create(MediaType.parse("application/x-www-form-urlencoded"), body))
            .header("Origin", "https://www.text-to-speech.cn")
            .header("Referer", "https://www.text-to-speech.cn/")
            .header(
                "User-Agent",
                "Mozilla/5.0 (Macintosh; Intel Mac OS X 10_15_7) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/113.0.0.0 Safari/537.36"
            )
            .build()
        client.newCall(req).execute().use { resp ->
            if (!resp.isSuccessful) error("text-to-speech.cn HTTP ${resp.code()}")
            return resp.body()?.bytes() ?: ByteArray(0)
        }
    }
}
