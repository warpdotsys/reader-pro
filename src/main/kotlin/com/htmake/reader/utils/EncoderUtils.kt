package com.htmake.reader.utils

import cn.hutool.crypto.asymmetric.KeyType
import cn.hutool.crypto.asymmetric.RSA
import java.util.Base64

object EncoderUtils {
    fun genRsaPair(): Pair<String, String> {
        val rsa = RSA()
        return Base64.getEncoder().encodeToString(rsa.publicKey.encoded) to
            Base64.getEncoder().encodeToString(rsa.privateKey.encoded)
    }

    fun rsaEncrypt(publicKeyBase64: String, data: String): String {
        val rsa = RSA(null, publicKeyBase64)
        return rsa.encryptBase64(data, KeyType.PublicKey)
    }

    fun rsaDecrypt(privateKeyBase64: String, data: String): String {
        val rsa = RSA(privateKeyBase64, null)
        return rsa.decryptStr(data, KeyType.PrivateKey)
    }
}
