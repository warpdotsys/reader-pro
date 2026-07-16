package com.htmake.reader.utils

import cn.hutool.crypto.asymmetric.KeyType
import cn.hutool.crypto.asymmetric.RSA
import java.security.KeyFactory
import java.security.KeyPairGenerator
import java.security.PrivateKey
import java.security.PublicKey
import java.security.spec.PKCS8EncodedKeySpec
import java.security.spec.X509EncodedKeySpec
import java.util.Base64
import javax.crypto.Cipher

/**
 * RSA helpers for License (jar EncoderUtils segment encrypt).
 * 2048-bit PKCS1: encrypt max ~245 bytes, decrypt blocks 256 bytes.
 */
object EncoderUtils {
    private const val ENCRYPT_BLOCK = 245
    private const val DECRYPT_BLOCK = 256

    fun genRsaPair(): Pair<String, String> {
        val kpg = KeyPairGenerator.getInstance("RSA").apply { initialize(2048) }
        val kp = kpg.generateKeyPair()
        val pub = Base64.getEncoder().encodeToString(kp.public.encoded)
        val pri = Base64.getEncoder().encodeToString(kp.private.encoded)
        return pub to pri
    }

    fun publicKeyFromBase64(b64: String): PublicKey =
        KeyFactory.getInstance("RSA").generatePublic(X509EncodedKeySpec(Base64.getDecoder().decode(b64)))

    fun privateKeyFromBase64(b64: String): PrivateKey =
        KeyFactory.getInstance("RSA").generatePrivate(PKCS8EncodedKeySpec(Base64.getDecoder().decode(b64)))

    fun encryptSegmentByPrivateKey(data: String, privateKey: PrivateKey): String {
        val cipher = Cipher.getInstance("RSA/ECB/PKCS1Padding")
        cipher.init(Cipher.ENCRYPT_MODE, privateKey)
        return segmentProcess(data.toByteArray(Charsets.UTF_8), ENCRYPT_BLOCK) { cipher.doFinal(it) }
    }

    fun decryptSegmentByPrivateKey(data: String, privateKey: PrivateKey): String {
        val cipher = Cipher.getInstance("RSA/ECB/PKCS1Padding")
        cipher.init(Cipher.DECRYPT_MODE, privateKey)
        val bytes = Base64.getDecoder().decode(data)
        val plain = segmentProcessBytes(bytes, DECRYPT_BLOCK) { cipher.doFinal(it) }
        return String(plain, Charsets.UTF_8)
    }

    fun encryptSegmentByPublicKey(data: String, publicKey: PublicKey): String {
        val cipher = Cipher.getInstance("RSA/ECB/PKCS1Padding")
        cipher.init(Cipher.ENCRYPT_MODE, publicKey)
        return segmentProcess(data.toByteArray(Charsets.UTF_8), ENCRYPT_BLOCK) { cipher.doFinal(it) }
    }

    fun decryptSegmentByPublicKey(data: String, publicKey: PublicKey): String {
        val cipher = Cipher.getInstance("RSA/ECB/PKCS1Padding")
        cipher.init(Cipher.DECRYPT_MODE, publicKey)
        val bytes = Base64.getDecoder().decode(data)
        val plain = segmentProcessBytes(bytes, DECRYPT_BLOCK) { cipher.doFinal(it) }
        return String(plain, Charsets.UTF_8)
    }

    /** Hutool convenience (single block / small data). */
    fun rsaEncrypt(publicKeyBase64: String, data: String): String {
        val rsa = RSA(null, publicKeyBase64)
        return rsa.encryptBase64(data, KeyType.PublicKey)
    }

    fun rsaDecrypt(privateKeyBase64: String, data: String): String {
        val rsa = RSA(privateKeyBase64, null)
        return rsa.decryptStr(data, KeyType.PrivateKey)
    }

    private fun segmentProcess(bytes: ByteArray, block: Int, op: (ByteArray) -> ByteArray): String {
        val out = ArrayList<Byte>(bytes.size + 64)
        var i = 0
        while (i < bytes.size) {
            val end = minOf(i + block, bytes.size)
            out += op(bytes.copyOfRange(i, end)).toList()
            i = end
        }
        return Base64.getEncoder().encodeToString(out.toByteArray())
    }

    private fun segmentProcessBytes(bytes: ByteArray, block: Int, op: (ByteArray) -> ByteArray): ByteArray {
        val out = ArrayList<Byte>()
        var i = 0
        while (i < bytes.size) {
            val end = minOf(i + block, bytes.size)
            out += op(bytes.copyOfRange(i, end)).toList()
            i = end
        }
        return out.toByteArray()
    }
}
