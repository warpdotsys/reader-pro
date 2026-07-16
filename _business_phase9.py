# -*- coding: utf-8 -*-
"""Phase 9: TTS stream, remote bookSource import, SearchResult, WebDAV backup/sync edges."""
from pathlib import Path
import os
import re

BIZ = Path(r"C:\Users\chong\reader-pro-3.2.14-reverse\best-of-3\business")
H = "/** Business rewrite from reader-pro-3.2.14.jar — phase9. */\n\n"


def w(rel, c):
    p = BIZ / rel.replace("/", os.sep)
    p.parent.mkdir(parents=True, exist_ok=True)
    p.write_text(H + c.lstrip("\n"), encoding="utf-8", newline="\n")
    print("wrote", rel, p.stat().st_size)


# ---------------------------------------------------------------------------
# SearchResult entity
# ---------------------------------------------------------------------------
w(
    "io/legado/app/data/entities/SearchResult.kt",
    r'''
package io.legado.app.data.entities

/**
 * 正文检索命中（对齐 jar SearchResult 字段）
 */
data class SearchResult(
    var resultCount: Int = 0,
    var resultCountWithinChapter: Int = 0,
    var resultText: String = "",
    var chapterTitle: String = "",
    var query: String = "",
    var pageSize: Int = 0,
    var chapterIndex: Int = 0,
    var pageIndex: Int = 0,
    var queryIndexInResult: Int = 0,
    var queryIndexInChapter: Int = 0
)
''',
)

# ---------------------------------------------------------------------------
# HttpTTS enrich name
# ---------------------------------------------------------------------------
http_tts = BIZ / "io/legado/app/data/entities/HttpTTS.kt"
ht = http_tts.read_text(encoding="utf-8")
if "var name:" not in ht:
    ht = ht.replace(
        "data class HttpTTS(\n    var url: String = \"\",",
        "data class HttpTTS(\n    var name: String = \"\",\n    var url: String = \"\",",
    )
    ht = re.sub(
        r"/\*\* Business rewrite.*?phase\d+\. \*/",
        "/** Business rewrite from reader-pro-3.2.14.jar — phase9. */",
        ht,
        count=1,
        flags=re.S,
    )
    # header may not match
    if "phase9" not in ht[:200]:
        ht = "/** Business rewrite from reader-pro-3.2.14.jar — phase9. */\n\n" + ht.split("\n", 2)[-1] if ht.startswith("/**") else "/** Business rewrite from reader-pro-3.2.14.jar — phase9. */\n\n" + ht
    # parse header map from header JSON
    if "header ?: return emptyMap" not in ht and "getHeaderMap" in ht:
        ht = ht.replace(
            "override fun getHeaderMap(withLogin: Boolean): Map<String, String> = emptyMap()",
            '''override fun getHeaderMap(withLogin: Boolean): Map<String, String> {
        val h = header ?: return emptyMap()
        return runCatching {
            val o = com.google.gson.JsonParser.parseString(h).asJsonObject
            o.entrySet().associate { it.key to it.value.asString }
        }.getOrDefault(emptyMap())
    }''',
        )
    http_tts.write_text(ht, encoding="utf-8", newline="\n")
    print("patched HttpTTS")

# ---------------------------------------------------------------------------
# EdgeTts + BookTts controller extras
# ---------------------------------------------------------------------------
w(
    "com/htmake/reader/lib/tts/EdgeTts.kt",
    r'''
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
''',
)

w(
    "com/htmake/reader/api/controller/BookTts.kt",
    r'''
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
''',
)

# ---------------------------------------------------------------------------
# Remote book source import
# ---------------------------------------------------------------------------
bsc = BIZ / "com/htmake/reader/api/controller/BookSourceController.kt"
bs = bsc.read_text(encoding="utf-8")
old_remote = '''    suspend fun saveFromRemoteSource(context: RoutingContext): ReturnData {
        val rd = ReturnData()
        if (!checkAuth(context)) return rd.setData("NEED_LOGIN").setErrorMsg("请登录后使用")
        val url = param(context, "url") ?: return rd.setErrorMsg("远程书源链接错误")
        // original: WebClient GET url -> JsonArray -> saveBookSources
        return rd.setErrorMsg("请通过 HTTP 客户端拉取后调用 saveBookSources（业务重写占位）")
            .also { it.isSuccess = false }
    }'''

new_remote = r'''    /**
     * 远程订阅：HTTP GET url → JsonArray → saveBookSources。
     * jar 用 Vert.x WebClient timeout 3s；此处 OkHttp 等价。
     */
    suspend fun saveFromRemoteSource(context: RoutingContext): ReturnData {
        val rd = ReturnData()
        if (!checkAuth(context)) return rd.setData("NEED_LOGIN").setErrorMsg("请登录后使用")
        if (!canEditBookSource(context)) return rd.setErrorMsg("无权编辑书源")
        val url = param(context, "url") ?: return rd.setErrorMsg("请输入远程书源链接")
        if (url.isBlank()) return rd.setErrorMsg("请输入远程书源链接")
        return try {
            val client = okhttp3.OkHttpClient.Builder()
                .connectTimeout(3, java.util.concurrent.TimeUnit.SECONDS)
                .readTimeout(15, java.util.concurrent.TimeUnit.SECONDS)
                .build()
            val req = okhttp3.Request.Builder().url(url).get().build()
            val body = client.newCall(req).execute().use { resp ->
                if (!resp.isSuccessful) return rd.setErrorMsg("远程书源链接错误 HTTP ${resp.code()}")
                resp.body()?.string() ?: return rd.setErrorMsg("远程书源链接错误")
            }
            val arr = try {
                JsonArray(body)
            } catch (_: Exception) {
                // single object or wrapped
                try {
                    val o = JsonObject(body)
                    o.getJsonArray("data")
                        ?: o.getJsonArray("bookSources")
                        ?: JsonArray().add(o)
                } catch (_: Exception) {
                    return rd.setErrorMsg("远程书源链接错误")
                }
            }
            saveBookSources(context, arr)
        } catch (e: Exception) {
            rd.setErrorMsg(e.message ?: "远程书源链接错误")
        }
    }'''

if "请通过 HTTP 客户端拉取后调用" in bs:
    bs = bs.replace(old_remote, new_remote)
    if "请通过 HTTP 客户端拉取后调用" in bs:
        # looser replace
        bs = re.sub(
            r"suspend fun saveFromRemoteSource\(context: RoutingContext\): ReturnData \{.*?\n    \}",
            new_remote.strip(),
            bs,
            count=1,
            flags=re.S,
        )
    bsc.write_text(bs, encoding="utf-8", newline="\n")
    print("patched saveFromRemoteSource")
elif "OkHttpClient" not in bs or "saveFromRemoteSource" not in bs:
    # insert before generateBookSourceMap
    if "saveFromRemoteSource" not in bs:
        bs = bs.replace(
            "    fun generateBookSourceMap",
            new_remote + "\n\n    fun generateBookSourceMap",
        )
        bsc.write_text(bs, encoding="utf-8", newline="\n")
        print("inserted saveFromRemoteSource")
    else:
        print("saveFromRemoteSource already present, skip structure")

# ---------------------------------------------------------------------------
# searchBookContent → SearchResult + searchChapter helpers on BookControllerExtras
# ---------------------------------------------------------------------------
extras = BIZ / "com/htmake/reader/api/controller/BookControllerExtras.kt"
ex = extras.read_text(encoding="utf-8")

# Replace searchBookContent implementation block
search_old_start = "suspend fun BookController.searchBookContent(context: RoutingContext): ReturnData {"
if search_old_start in ex:
    # find and replace the whole function until next suspend fun BookController.saveBookContent
    pat = re.compile(
        r"suspend fun BookController\.searchBookContent\(context: RoutingContext\): ReturnData \{.*?\n\}\n\n"
        r"private suspend fun BookController\.readChapterText",
        re.S,
    )
    new_search = r'''suspend fun BookController.searchBookContent(context: RoutingContext): ReturnData {
    val rd = ReturnData()
    if (!checkAuth(context)) return rd.setData("NEED_LOGIN").setErrorMsg("请登录后使用")
    val bookUrl = pStr(context, "url") ?: pStr(context, "bookUrl") ?: ""
    val keyword = pStr(context, "keyword") ?: ""
    val lastIndex = pInt(context, "lastIndex") ?: 0
    val size = pInt(context, "size") ?: 20
    if (bookUrl.isEmpty()) return rd.setErrorMsg("请输入书籍链接")
    if (keyword.isEmpty()) return rd.setErrorMsg("请输入搜索关键词")
    val ns = getUserNameSpace(context)
    val book = getShelfBookByURL(bookUrl, ns) ?: return rd.setErrorMsg("请先加入书架")
    val source = if (book.isLocalBook) null else getBookSourceString(book, ns)
    if (!book.isLocalBook && source.isNullOrEmpty()) return rd.setErrorMsg("未配置书源")
    val chapters = getLocalChapterList(book, source, false, ns, false, null)
    if (lastIndex >= chapters.size) return rd.setErrorMsg("没有更多了")
    val hits = ArrayList<io.legado.app.data.entities.SearchResult>()
    var currentIndex = lastIndex
    var i = lastIndex + 1
    while (i < chapters.size && hits.size < size) {
        val ch = chapters[i]
        currentIndex = i
        try {
            hits.addAll(searchChapter(book, ch, keyword, source, ns, chapters.getOrNull(i + 1)?.url))
        } catch (_: Exception) {
        }
        i++
    }
    return rd.setData(mapOf("list" to hits, "lastIndex" to currentIndex))
}

/** 单章检索，对齐 jar SearchResult 字段 */
suspend fun BookController.searchChapter(
    book: Book,
    chapter: BookChapter,
    query: String,
    source: String? = null,
    ns: String = book.userNameSpace ?: "default",
    nextUrl: String? = null
): List<io.legado.app.data.entities.SearchResult> {
    val content = readChapterText(book, source, ns, chapter, nextUrl)
    if (content.isEmpty()) return emptyList()
    val positions = searchPosition(content, query)
    return positions.mapIndexed { idx, pos ->
        val (qInResult, snippet) = getResultAndQueryIndex(content, pos, query)
        io.legado.app.data.entities.SearchResult(
            resultCount = 0,
            resultCountWithinChapter = idx,
            resultText = snippet,
            chapterTitle = chapter.title,
            query = query,
            pageSize = 0,
            chapterIndex = chapter.index,
            pageIndex = 0,
            queryIndexInResult = qInResult,
            queryIndexInChapter = pos
        )
    }
}

private fun searchPosition(content: String, pattern: String): List<Int> {
    val out = ArrayList<Int>()
    var from = 0
    while (true) {
        val i = content.indexOf(pattern, from)
        if (i < 0) break
        out += i
        from = i + 1
    }
    return out
}

private fun getResultAndQueryIndex(content: String, queryIndexInContent: Int, query: String): Pair<Int, String> {
    var po1 = queryIndexInContent - 20
    var po2 = queryIndexInContent + query.length + 20
    if (po1 < 0) po1 = 0
    if (po2 > content.length) po2 = content.length
    return (queryIndexInContent - po1) to content.substring(po1, po2)
}

private suspend fun BookController.readChapterText'''
    if pat.search(ex):
        ex = pat.sub(new_search, ex, count=1)
        extras.write_text(ex, encoding="utf-8", newline="\n")
        print("rewrote searchBookContent → SearchResult")
    else:
        print("WARN: searchBookContent pattern not matched")

# remove placeholder tts in extras if present
ex = extras.read_text(encoding="utf-8")
ex2 = re.sub(
    r"\nfun BookController\.tts\(context: RoutingContext\): ReturnData =\n"
    r"    ReturnData\(\)\.setData\(mapOf\(\"note\" to \"stream audio via getSpeakStream[^\"]*\"\)\)\n",
    "\n// tts / textToSpeech → BookTts.kt\n",
    ex,
)
if ex2 != ex:
    extras.write_text(ex2, encoding="utf-8", newline="\n")
    print("removed tts placeholder from Extras")

# ---------------------------------------------------------------------------
# BookController syncFromWebdav edges + WebDAV save helpers
# ---------------------------------------------------------------------------
bc_path = BIZ / "com/htmake/reader/api/controller/BookController.kt"
bc = bc_path.read_text(encoding="utf-8")

old_sync = '''    suspend fun syncFromWebdav(zipFilePath: String, userNameSpace: String): Boolean {
        val descDir = ExtKt.getWorkDir("storage", "data", userNameSpace, "tmp")
        val descDirFile = File(descDir)
        try {
            val zipFile = File(zipFilePath)
            if (!zipFile.exists()) return false
            ExtKt.deleteRecursively(descDirFile)
            ZipUtils.unzipFile(zipFile, descDirFile)
            for (name in getBackupFileNames()) {
                val backupFile = File(descDir, name)
                if (!backupFile.exists()) continue
                val userData = File(ExtKt.getWorkDir("storage", "data", userNameSpace, name))
                ExtKt.deleteRecursively(userData)
                backupFile.copyRecursively(userData, overwrite = false)
            }
            return true
        } catch (e: Exception) {
            e.printStackTrace()
            return false
        } finally {
            ExtKt.deleteRecursively(descDirFile)
        }
    }'''

new_sync = r'''    /**
     * 从 WebDAV 备份 zip 恢复：json 配置 + books 目录 + bookProgress。
     */
    suspend fun syncFromWebdav(zipFilePath: String, userNameSpace: String): Boolean {
        val descDir = ExtKt.getWorkDir("storage", "data", userNameSpace, "tmp")
        val descDirFile = File(descDir)
        try {
            val zipFile = File(zipFilePath)
            if (!zipFile.exists()) return false
            ExtKt.deleteRecursively(descDirFile)
            ZipUtils.unzipFile(zipFile, descDirFile)
            for (name in getBackupFileNames()) {
                val backupFile = File(descDir, name)
                if (!backupFile.exists()) continue
                val userData = File(ExtKt.getWorkDir("storage", "data", userNameSpace, name))
                ExtKt.deleteRecursively(userData)
                backupFile.copyRecursively(userData, overwrite = false)
            }
            // books/ under zip → storage/data/{user}/webdav/books
            val backupBooks = File(descDir, "books")
            if (backupBooks.isDirectory) {
                val webdavBooks = File(ExtKt.getWorkDir("storage", "data", userNameSpace, "webdav", "books"))
                ExtKt.deleteRecursively(webdavBooks)
                backupBooks.copyRecursively(webdavBooks, overwrite = false)
            }
            // progress files from webdav home
            val userHome = File(getUserWebdavHome(userNameSpace))
            var progressDir = File(userHome, "bookProgress")
            if (!progressDir.isDirectory) progressDir = File(userHome, "legado/bookProgress")
            if (progressDir.isDirectory) {
                progressDir.listFiles()?.forEach { f ->
                    if (f.isFile) syncBookProgressFromWebdav(f, userNameSpace)
                }
            }
            return true
        } catch (e: Exception) {
            e.printStackTrace()
            return false
        } finally {
            ExtKt.deleteRecursively(descDirFile)
        }
    }

    /** 单书进度 JSON 合并进书架 */
    fun syncBookProgressFromWebdav(progressFile: File, userNameSpace: String) {
        try {
            val o = JsonObject(progressFile.readText())
            val bookUrl = o.getString("bookUrl") ?: o.getString("url") ?: return
            val book = getShelfBookByURL(bookUrl, userNameSpace) ?: return
            // fire-and-forget style edit — sync is suspend context often
            val arr = ExtKt.asJsonArray(getUserStorage(userNameSpace, "bookshelf")) ?: return
            for (i in 0 until arr.size()) {
                val b = arr.getJsonObject(i) ?: continue
                if (b.getString("bookUrl") == bookUrl) {
                    o.getInteger("durChapterIndex")?.let { b.put("durChapterIndex", it) }
                    o.getInteger("durChapterPos")?.let { b.put("durChapterPos", it) }
                    o.getString("durChapterTitle")?.let { b.put("durChapterTitle", it) }
                    o.getLong("durChapterTime")?.let { b.put("durChapterTime", it) }
                    break
                }
            }
            saveUserStorage(userNameSpace, "bookshelf", arr)
        } catch (_: Exception) {
        }
    }

    /**
     * 打包用户配置到 WebDAV 家目录（legado 风格）。
     * @return 生成的 zip File，失败 null
     */
    suspend fun saveToWebdav(userNameSpace: String, latestZipFilePath: String? = null): Boolean {
        val userHome = getUserWebdavHome(userNameSpace)
        val zipHint = latestZipFilePath ?: getLastBackFileFromWebdav(userNameSpace)
        val legadoHome = if (zipHint != null && zipHint.contains("legado")) {
            File(userHome, "legado").absolutePath
        } else if (zipHint == null) {
            File(userHome, "legado").absolutePath
        } else {
            userHome
        }
        val file = createUserBackup(userNameSpace, legadoHome, zipHint)
        return file != null
    }

    fun getLastBackFileFromWebdav(userNameSpace: String): String? {
        val home = File(getUserWebdavHome(userNameSpace))
        val candidates = mutableListOf<File>()
        fun scan(dir: File) {
            if (!dir.isDirectory) return
            dir.listFiles()?.forEach { f ->
                if (f.isFile && f.name.endsWith(".zip", true) &&
                    (f.name.contains("backup", true) || f.name.startsWith("backup"))
                ) {
                    candidates += f
                } else if (f.isDirectory && (f.name == "legado" || f.name == "backup")) {
                    scan(f)
                }
            }
        }
        scan(home)
        return candidates.maxByOrNull { it.lastModified() }?.absolutePath
    }

    fun createUserBackup(
        userNameSpace: String,
        backupDir: String,
        latestZipFilePath: String? = null
    ): File? {
        val today = java.text.SimpleDateFormat("yyyy-MM-dd").format(System.currentTimeMillis())
        val staging = File(ExtKt.getWorkDir("storage", "data", userNameSpace, "backup$today"))
        try {
            ExtKt.deleteRecursively(staging)
            staging.mkdirs()
            // seed from previous zip if present
            if (latestZipFilePath != null) {
                val prev = File(latestZipFilePath)
                if (prev.isFile) {
                    try {
                        ZipUtils.unzipFile(prev, staging)
                    } catch (_: Exception) {
                    }
                }
            }
            val dataDir = File(ExtKt.getWorkDir("storage", "data", userNameSpace))
            for (name in getBackupFileNames()) {
                val src = File(dataDir, name)
                if (src.isFile) src.copyTo(File(staging, name), overwrite = true)
            }
            // optional books mirror
            val books = File(dataDir, "webdav/books")
            if (books.isDirectory) {
                books.copyRecursively(File(staging, "books"), overwrite = true)
            }
            val outDir = File(backupDir).apply { mkdirs() }
            val zip = File(outDir, "backup$today.zip")
            if (zip.exists()) zip.delete()
            java.util.zip.ZipOutputStream(zip.outputStream()).use { zos ->
                staging.walkTopDown().filter { it.isFile }.forEach { f ->
                    val entry = f.relativeTo(staging).invariantSeparatorsPath
                    zos.putNextEntry(java.util.zip.ZipEntry(entry))
                    f.inputStream().use { it.copyTo(zos) }
                    zos.closeEntry()
                }
            }
            return zip
        } catch (e: Exception) {
            e.printStackTrace()
            return null
        } finally {
            ExtKt.deleteRecursively(staging)
        }
    }
'''

if "syncBookProgressFromWebdav" not in bc:
    if old_sync in bc:
        bc = bc.replace(old_sync, new_sync)
        print("expanded syncFromWebdav + saveToWebdav")
    else:
        # try insert after existing syncFromWebdav block end
        m = re.search(
            r"suspend fun syncFromWebdav\(zipFilePath: String, userNameSpace: String\): Boolean \{.*?\n    \}\n",
            bc,
            re.S,
        )
        if m:
            bc = bc[: m.start()] + new_sync + "\n" + bc[m.end() :]
            print("replaced syncFromWebdav via regex")
        else:
            print("WARN: could not patch syncFromWebdav")

# ensure getUserWebdavHome available - usually on BaseController
if "fun getUserWebdavHome" not in bc and "getUserWebdavHome" in bc:
    pass  # uses base

bc_path.write_text(bc, encoding="utf-8", newline="\n")

base_path = BIZ / "com/htmake/reader/api/controller/BaseController.kt"
base = base_path.read_text(encoding="utf-8")
if "getUserWebdavHome" not in base:
    # append helper before last brace
    helper = '''
    open fun getUserWebdavHome(userNameSpace: String): String =
        ExtKt.getWorkDir("storage", "data", userNameSpace, "webdav")
'''
    idx = base.rfind("}")
    base = base[:idx] + helper + base[idx:]
    base_path.write_text(base, encoding="utf-8", newline="\n")
    print("added getUserWebdavHome to BaseController")

# WebdavController backup includes books folder
wd = BIZ / "com/htmake/reader/api/controller/WebdavController.kt"
wdt = wd.read_text(encoding="utf-8")
if "webdav/books" not in wdt and "books" not in wdt[wdt.find("backupToWebdav"):]:
    wdt = wdt.replace(
        '''            ZipOutputStream(zip.outputStream()).use { zos ->
                for (name in names) {
                    val f = File(dataDir, name)
                    if (!f.isFile) continue
                    zos.putNextEntry(ZipEntry(name))
                    f.inputStream().use { it.copyTo(zos) }
                    zos.closeEntry()
                }
            }''',
        '''            ZipOutputStream(zip.outputStream()).use { zos ->
                for (name in names) {
                    val f = File(dataDir, name)
                    if (!f.isFile) continue
                    zos.putNextEntry(ZipEntry(name))
                    f.inputStream().use { it.copyTo(zos) }
                    zos.closeEntry()
                }
                // books mirror (local assets under webdav/books)
                val booksDir = File(dataDir, "webdav/books")
                if (booksDir.isDirectory) {
                    booksDir.walkTopDown().filter { it.isFile }.forEach { f ->
                        val rel = "books/" + f.relativeTo(booksDir).invariantSeparatorsPath
                        zos.putNextEntry(ZipEntry(rel))
                        f.inputStream().use { it.copyTo(zos) }
                        zos.closeEntry()
                    }
                }
            }''',
    )
    # also wire restore endpoint convenience
    if "restoreFromWebdav" not in wdt:
        wdt = wdt.replace(
            "    private fun xmlEsc(s: String) =",
            r'''    /**
     * 从最新 WebDAV backup zip 恢复（委托 BookController.syncFromWebdav）。
     * 需要路由侧持有 BookController；此处仅打包路径解析。
     */
    suspend fun restoreFromWebdavZip(zipPath: String, userNameSpace: String, book: BookController): Boolean =
        book.syncFromWebdav(zipPath, userNameSpace)

    private fun xmlEsc(s: String) =''',
        )
    wd.write_text(wdt, encoding="utf-8", newline="\n")
    print("enhanced WebdavController backup")

# ---------------------------------------------------------------------------
# YueduApi: tts returns null (stream), saveFromRemoteSource route if missing
# ---------------------------------------------------------------------------
api = BIZ / "com/htmake/reader/api/YueduApi.kt"
at = api.read_text(encoding="utf-8")
changed = False
if 'get(router, "/reader3/book/tts") { book.tts(it) }' in at:
    at = at.replace(
        'get(router, "/reader3/book/tts") { book.tts(it) }',
        'get(router, "/reader3/book/tts") { book.tts(it); null }',
    )
    at = at.replace(
        'post(router, "/reader3/book/tts") { book.tts(it) }',
        'post(router, "/reader3/book/tts") { book.tts(it); null }',
    )
    changed = True
if "saveFromRemoteSource" not in at and "saveBookSources" in at:
    at = at.replace(
        'post(router, "/reader3/saveBookSources")',
        'post(router, "/reader3/saveFromRemoteSource") { bookSource.saveFromRemoteSource(it) }\n'
        '        post(router, "/reader3/saveBookSources")',
    )
    # may already have different path names - check API_ROUTES
    changed = True
if changed:
    api.write_text(at, encoding="utf-8", newline="\n")
    print("patched YueduApi routes")

# Check API_ROUTES for remote path
routes = Path(r"C:\Users\chong\reader-pro-3.2.14-reverse\API_ROUTES.md")
if routes.exists():
    rt = routes.read_text(encoding="utf-8", errors="replace")
    remote_paths = re.findall(r"/reader3/\S*[Rr]emote\S*|/reader3/\S*[Ss]ource\S*", rt)
    print("route hints:", remote_paths[:15])

# ---------------------------------------------------------------------------
# HttpTTSController delete by name/id
# ---------------------------------------------------------------------------
htt = BIZ / "com/htmake/reader/api/controller/HttpTTSController.kt"
htc = htt.read_text(encoding="utf-8")
if 'return rd.setData(true)\n    }\n\n    suspend fun deleteMulti' in htc or (
    "delete" in htc and "getString(\"name\")" not in htc
):
    htc = re.sub(
        r"suspend fun delete\(context: RoutingContext\): ReturnData \{.*?\n    \}",
        r'''suspend fun delete(context: RoutingContext): ReturnData {
        val rd = ReturnData()
        if (!checkAuth(context)) return rd.setData("NEED_LOGIN").setErrorMsg("请登录后使用")
        val ns = getUserNameSpace(context)
        val name = context.bodyAsJson?.getString("name")
            ?: context.queryParam("name").firstOrNull()
        val id = context.bodyAsJson?.getLong("id")
            ?: context.queryParam("id").firstOrNull()?.toLongOrNull()
        val arr = load(ns)
        val list = arr.list.filterIndexed { i, _ ->
            val o = arr.getJsonObject(i)
            when {
                name != null && o.getString("name") == name -> false
                id != null && o.getLong("id") == id -> false
                else -> true
            }
        }
        save(ns, JsonArray(list))
        return rd.setData(true)
    }''',
        htc,
        count=1,
        flags=re.S,
    )
    htt.write_text(htc, encoding="utf-8", newline="\n")
    print("HttpTTS delete by name/id")

# ---------------------------------------------------------------------------
# README / INDEX
# ---------------------------------------------------------------------------
readme = BIZ / "README.md"
r = readme.read_text(encoding="utf-8", errors="replace")
if "Phase 9" not in r:
    r = r.rstrip() + """


## Phase 9 增量

- **TTS**：`textToSpeech` / `ttsByEdge`（TTSService 反射）/ `ttsByApi`（HttpTTS）/ `ttsByTextToSpeechCn`
- **EdgeTts**：SSML 构造 + jar `TTSService.sendText` 对接
- **saveFromRemoteSource**：OkHttp 拉取远程书源 JSON → `saveBookSources`
- **SearchResult** + `searchChapter` / `searchPosition` / `getResultAndQueryIndex`
- **syncFromWebdav**：恢复 books 目录 + bookProgress 合并
- **saveToWebdav / createUserBackup / getLastBackFileFromWebdav**
- **backupToWebdav** 打包 books 镜像
- **HttpTTS.name** + 按 name 删除
"""
    readme.write_text(r, encoding="utf-8", newline="\n")
    print("README phase9")

index = BIZ / "INDEX.md"
ix = index.read_text(encoding="utf-8", errors="replace")
if "SearchResult" not in ix:
    ix = ix.replace(
        "| SearchBook |",
        "| SearchResult | `io/legado/app/data/entities/SearchResult.kt` | `io/legado/app/data/entities/SearchResult.java` |\n| SearchBook |",
    )
if "EdgeTts" not in ix:
    ix = ix.replace(
        "| EncoderUtils |",
        "| EdgeTts | `com/htmake/reader/lib/tts/EdgeTts.kt` | `lib/tts/service/TTSService` + SSML |\n| EncoderUtils |",
    )
if "BookTts" not in ix:
    ix = ix.replace(
        "| BookExport |",
        "| BookExport | `com/htmake/reader/api/controller/BookExport.kt` | `BookController.exportBook` |\n| BookTts | `com/htmake/reader/api/controller/BookTts.kt` | `textToSpeech` / `ttsByEdge` / `ttsByApi` |\n| __dup_export |",
    )
    # clean accidental dup if we broke BookExport line
    ix = ix.replace(
        "| BookExport | `com/htmake/reader/api/controller/BookExport.kt` | `BookController.exportBook` |\n| BookTts | `com/htmake/reader/api/controller/BookTts.kt` | `textToSpeech` / `ttsByEdge` / `ttsByApi` |\n| __dup_export | `com/htmake/reader/api/controller/BookExport.kt` | `BookController.exportBook` |",
        "| BookExport | `com/htmake/reader/api/controller/BookExport.kt` | `BookController.exportBook` |\n| BookTts | `com/htmake/reader/api/controller/BookTts.kt` | `textToSpeech` / `ttsByEdge` / `ttsByApi` |",
    )
kt_count = sum(1 for _ in BIZ.rglob("*.kt"))
lines = sum(len(p.read_text(encoding="utf-8", errors="replace").splitlines()) for p in BIZ.rglob("*.kt"))
ix = ix.rstrip() + f"\n\n- phase9 后 business `.kt` 文件数: **{kt_count}** / 约 **{lines}** 行\n"
index.write_text(ix, encoding="utf-8", newline="\n")
print(f"DONE phase9: {kt_count} files, ~{lines} lines")
