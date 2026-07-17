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
import javax.crypto.Cipher
import javax.crypto.spec.IvParameterSpec
import javax.crypto.spec.SecretKeySpec

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

    /** Called from book-source login scripts: java.putLoginHeader('{"Cookie":"..."}') */
    fun putLoginHeader(header: String) {
        getSource()?.putLoginHeader(header)
    }

    fun getLoginHeader(): String? = getSource()?.getLoginHeader()

    fun getLoginInfo(): String? = getSource()?.getLoginInfo()

    fun putLoginInfo(info: String): Boolean = getSource()?.putLoginInfo(info) ?: false

    // ---- legado AES 函数族（与原版 JsExtensions 行为一致） ----

    private fun aesCipher(
        data: ByteArray, key: String, transformation: String, iv: String, mode: Int, base64Input: Boolean
    ): ByteArray? = try {
        val secretKey = SecretKeySpec(key.toByteArray(Charsets.UTF_8), transformation.substringBefore("/"))
        val cipher = Cipher.getInstance(transformation)
        val ivBytes = iv.toByteArray(Charsets.UTF_8)
        if (ivBytes.isEmpty()) cipher.init(mode, secretKey)
        else cipher.init(mode, secretKey, IvParameterSpec(ivBytes))
        cipher.doFinal(if (base64Input) Base64.getDecoder().decode(data) else data)
    } catch (e: Exception) {
        log(e.localizedMessage ?: "aesCipherERROR")
        null
    }

    fun aesDecodeToByteArray(str: String, key: String, transformation: String, iv: String): ByteArray? =
        aesCipher(str.toByteArray(Charsets.UTF_8), key, transformation, iv, Cipher.DECRYPT_MODE, false)

    fun aesDecodeToString(str: String, key: String, transformation: String, iv: String): String? =
        aesDecodeToByteArray(str, key, transformation, iv)?.let { String(it, Charsets.UTF_8) }

    fun aesBase64DecodeToByteArray(str: String, key: String, transformation: String, iv: String): ByteArray? =
        aesCipher(str.toByteArray(Charsets.UTF_8), key, transformation, iv, Cipher.DECRYPT_MODE, true)

    fun aesBase64DecodeToString(str: String, key: String, transformation: String, iv: String): String? =
        aesBase64DecodeToByteArray(str, key, transformation, iv)?.let { String(it, Charsets.UTF_8) }

    fun aesEncodeToByteArray(data: String, key: String, transformation: String, iv: String): ByteArray? =
        aesCipher(data.toByteArray(Charsets.UTF_8), key, transformation, iv, Cipher.ENCRYPT_MODE, false)

    fun aesEncodeToString(data: String, key: String, transformation: String, iv: String): String? =
        aesEncodeToByteArray(data, key, transformation, iv)?.let { String(it, Charsets.UTF_8) }

    fun aesEncodeToBase64String(data: String, key: String, transformation: String, iv: String): String? =
        aesEncodeToByteArray(data, key, transformation, iv)?.let { Base64.getEncoder().encodeToString(it) }
}
