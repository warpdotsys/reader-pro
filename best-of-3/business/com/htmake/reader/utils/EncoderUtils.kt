/** Business rewrite from reader-pro-3.2.14.jar — phase2. Readability/audit. */

package com.htmake.reader.utils

import java.security.PrivateKey
import java.security.PublicKey
import java.util.Base64
import javax.crypto.Cipher

/**
 * RSA segment encrypt/decrypt used by LicenseController (jar: EncoderUtils).
 * Large payloads are split into blocks fitting RSA key size.
 */
object EncoderUtils {
    private const val RSA_BLOCK = 245 // approx for 2048-bit PKCS1

    fun encryptSegmentByPrivateKey(data: String, privateKey: PrivateKey, mode: Int = 0): String {
        val cipher = Cipher.getInstance("RSA/ECB/PKCS1Padding")
        cipher.init(Cipher.ENCRYPT_MODE, privateKey)
        val bytes = data.toByteArray(Charsets.UTF_8)
        val out = ArrayList<Byte>()
        var i = 0
        while (i < bytes.size) {
            val end = minOf(i + RSA_BLOCK, bytes.size)
            out += cipher.doFinal(bytes.copyOfRange(i, end)).toList()
            i = end
        }
        return Base64.getEncoder().encodeToString(out.toByteArray())
    }

    fun decryptSegmentByPrivateKey(data: String, privateKey: PrivateKey): String {
        val cipher = Cipher.getInstance("RSA/ECB/PKCS1Padding")
        cipher.init(Cipher.DECRYPT_MODE, privateKey)
        val bytes = Base64.getDecoder().decode(data)
        // block size for decrypt = key size in bytes
        val block = 256
        val out = ArrayList<Byte>()
        var i = 0
        while (i < bytes.size) {
            val end = minOf(i + block, bytes.size)
            out += cipher.doFinal(bytes.copyOfRange(i, end)).toList()
            i = end
        }
        return String(out.toByteArray(), Charsets.UTF_8)
    }

    fun encryptSegmentByPublicKey(data: String, publicKey: PublicKey): String {
        val cipher = Cipher.getInstance("RSA/ECB/PKCS1Padding")
        cipher.init(Cipher.ENCRYPT_MODE, publicKey)
        return Base64.getEncoder().encodeToString(cipher.doFinal(data.toByteArray()))
    }
}
