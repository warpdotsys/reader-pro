package io.legado.app.utils

import io.legado.app.lib.icu4j.CharsetDetector
import org.jsoup.Jsoup
import java.io.File
import java.io.FileInputStream

object EncodingDetect {
    private val headTagRegex = "(?i)<head>[\\s\\S]*?</head>".toRegex()

    fun getHtmlEncode(bytes: ByteArray): String {
        try {
            val html = String(bytes)
            val startIndex = html.indexOf("<head>", ignoreCase = true)
            val head = if (startIndex > -1) {
                val endIndex = html.indexOf("</head>", startIndex, ignoreCase = true)
                if (endIndex > -1) html.substring(startIndex, endIndex + "</head>".length) else null
            } else {
                null
            }
            val metaTags = Jsoup.parseBodyFragment(head ?: headTagRegex.find(html)!!.value).getElementsByTag("meta")
            for (metaTag in metaTags) {
                val charset = metaTag.attr("charset")
                if (charset.isNotEmpty()) return charset
                if (metaTag.attr("http-equiv").equals("content-type", true)) {
                    val content = metaTag.attr("content")
                    val index = content.indexOf("charset=", ignoreCase = true)
                    val detected = if (index > -1) content.substring(index + "charset=".length) else content.substringAfter(";")
                    if (detected.isNotEmpty()) return detected
                }
            }
        } catch (_: Exception) {
        }
        return getEncode(bytes)
    }

    fun getEncode(bytes: ByteArray): String = CharsetDetector().setText(bytes).detect()?.name ?: "UTF-8"

    fun getEncode(filePath: String): String = getEncode(File(filePath))

    fun getEncode(file: File): String = getEncode(getFileBytes(file))

    private fun getFileBytes(file: File?): ByteArray {
        val bytes = ByteArray(8000)
        try {
            FileInputStream(file).use { it.read(bytes) }
        } catch (error: Exception) {
            System.err.println("Error: $error")
        }
        return bytes
    }
}
