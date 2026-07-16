package io.legado.app.utils

import java.security.MessageDigest

object MD5Utils {
    fun md5Encode(text: String): String {
        val md = MessageDigest.getInstance("MD5")
        val bytes = md.digest(text.toByteArray())
        return bytes.joinToString("") { "%02x".format(it) }
    }
    fun md5Encode16(text: String): String = md5Encode(text).substring(8, 24)
}
