/** Business rewrite from reader-pro-3.2.14.jar — phase2. Readability/audit. */

package io.legado.app.help

import io.legado.app.data.entities.BaseSource
import io.legado.app.help.http.StrResponse
import io.legado.app.model.DebugLog
import io.legado.app.model.analyzeRule.AnalyzeUrl
import io.legado.app.utils.MD5Utils
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.Response
import java.io.File
import java.net.URLEncoder
import java.nio.charset.Charset
import java.util.Base64
import java.util.UUID
import java.util.concurrent.TimeUnit
import javax.crypto.Cipher
import javax.crypto.spec.IvParameterSpec
import javax.crypto.spec.SecretKeySpec

/**
 * JS runtime bindings for book source scripts (Rhino).
 * Implemented by AnalyzeRule; methods called from JS as `java.ajax(url)` etc.
 */
interface JsExtensions {
    fun getSource(): BaseSource?
    fun getUserNameSpace(): String
    fun getLogger(): DebugLog? = null

    // ---- network ----
    fun ajax(urlStr: String): String? =
        runCatching { connect(urlStr).body }.getOrNull()

    fun connect(urlStr: String): StrResponse = connect(urlStr, null)

    fun connect(urlStr: String, header: String?): StrResponse {
        // AnalyzeUrl does full rule URL; here plain HTTP
        val client = OkHttpClient.Builder()
            .connectTimeout(15, TimeUnit.SECONDS)
            .readTimeout(30, TimeUnit.SECONDS)
            .build()
        val req = Request.Builder().url(urlStr).get().build()
        client.newCall(req).execute().use { resp ->
            return StrResponse(urlStr, resp.body?.string())
        }
    }

    fun get(urlStr: String, headers: Map<String, String>): Response {
        val b = Request.Builder().url(urlStr)
        headers.forEach { (k, v) -> b.header(k, v) }
        return OkHttpClient().newCall(b.get().build()).execute()
    }

    fun post(urlStr: String, body: String, headers: Map<String, String>): Response {
        val media = okhttp3.MediaType.parse("application/json; charset=utf-8")
        val b = Request.Builder().url(urlStr).post(okhttp3.RequestBody.create(media, body))
        headers.forEach { (k, v) -> b.header(k, v) }
        return OkHttpClient().newCall(b.build()).execute()
    }

    fun head(urlStr: String, headers: Map<String, String>): Response {
        val b = Request.Builder().url(urlStr).head()
        headers.forEach { (k, v) -> b.header(k, v) }
        return OkHttpClient().newCall(b.build()).execute()
    }

    fun webView(html: String?, url: String?, js: String?): String? {
        // remote webview API if configured — else null
        return null
    }

    // ---- codec ----
    fun base64Decode(str: String): String =
        String(Base64.getDecoder().decode(str.substringAfter(',')))

    fun base64Encode(str: String): String? =
        Base64.getEncoder().encodeToString(str.toByteArray())

    fun md5Encode(str: String): String = MD5Utils.md5Encode(str)
    fun md5Encode16(str: String): String = MD5Utils.md5Encode16(str)

    fun encodeURI(str: String): String = URLEncoder.encode(str, "UTF-8")
    fun encodeURI(str: String, enc: String): String = URLEncoder.encode(str, enc)

    fun utf8ToGbk(str: String): String =
        String(str.toByteArray(Charsets.UTF_8), Charset.forName("GBK"))

    fun htmlFormat(str: String): String = str // HtmlFormatter.format keep img

    // ---- file ----
    fun getFile(path: String): File = File(path)
    fun readFile(path: String): ByteArray? = runCatching { File(path).readBytes() }.getOrNull()
    fun readTxtFile(path: String): String = File(path).readText()
    fun readTxtFile(path: String, charsetName: String): String =
        File(path).readText(Charset.forName(charsetName))
    fun deleteFile(path: String) { File(path).deleteRecursively() }

    fun log(msg: String): String {
        getLogger()?.log(getSource()?.toString(), msg)
        return msg
    }

    fun toast(msg: Any?) {}
    fun longToast(msg: Any?) {}
    fun randomUUID(): String = UUID.randomUUID().toString()
    fun androidId(): String = ""

    // ---- AES (hutool/javax.crypto in jar) ----
    fun aesDecodeToString(str: String, key: String, transformation: String, iv: String): String? =
        runCatching {
            val cipher = Cipher.getInstance(transformation)
            val keySpec = SecretKeySpec(key.toByteArray(), "AES")
            if (iv.isNotEmpty()) cipher.init(Cipher.DECRYPT_MODE, keySpec, IvParameterSpec(iv.toByteArray()))
            else cipher.init(Cipher.DECRYPT_MODE, keySpec)
            String(cipher.doFinal(Base64.getDecoder().decode(str)))
        }.getOrNull()

    fun aesEncodeToString(data: String, key: String, transformation: String, iv: String): String? =
        runCatching {
            val cipher = Cipher.getInstance(transformation)
            val keySpec = SecretKeySpec(key.toByteArray(), "AES")
            if (iv.isNotEmpty()) cipher.init(Cipher.ENCRYPT_MODE, keySpec, IvParameterSpec(iv.toByteArray()))
            else cipher.init(Cipher.ENCRYPT_MODE, keySpec)
            Base64.getEncoder().encodeToString(cipher.doFinal(data.toByteArray()))
        }.getOrNull()

    fun importScript(path: String): String = readTxtFile(path)
    fun cacheFile(urlStr: String): String? = cacheFile(urlStr, 0)
    fun cacheFile(urlStr: String, saveTime: Int): String? = ajax(urlStr)
    fun getCookie(tag: String, key: String? = null): String = ""
}
