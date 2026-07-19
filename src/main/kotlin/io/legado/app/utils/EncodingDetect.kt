package io.legado.app.utils

import io.legado.app.lib.icu4j.CharsetDetector
import org.jsoup.Jsoup
import java.io.File
import java.io.FileInputStream
import java.nio.charset.StandardCharsets
import java.util.Locale

object EncodingDetect {
    fun getHtmlEncode(bytes: ByteArray): String? {
        try {
            val doc = Jsoup.parse(String(bytes, StandardCharsets.UTF_8))
            val metaTags = doc.getElementsByTag("meta")
            for (metaTag in metaTags) {
                var charsetStr = metaTag.attr("charset")
                if (charsetStr.isNotEmpty()) return charsetStr
                val content = metaTag.attr("content")
                if (metaTag.attr("http-equiv").lowercase(Locale.getDefault()) == "content-type") {
                    charsetStr = if (content.lowercase(Locale.getDefault()).contains("charset")) {
                        content.substring(
                            content.lowercase(Locale.getDefault()).indexOf("charset") + "charset=".length
                        )
                    } else {
                        content.substring(content.lowercase(Locale.getDefault()).indexOf(";") + 1)
                    }
                    if (charsetStr.isNotEmpty()) return charsetStr
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
