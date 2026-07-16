package me.ag2s.umdlib.tool

import java.io.ByteArrayInputStream
import java.io.ByteArrayOutputStream
import java.util.zip.InflaterInputStream

object UmdUtils {
    fun unicodeBytesToString(bytes: ByteArray): String {
        val sb = StringBuilder(bytes.size / 2)
        var i = 0
        while (i + 1 < bytes.size) {
            val c = ((bytes[i + 1].toInt() and 0xFF) shl 8) or (bytes[i].toInt() and 0xFF)
            sb.append(c.toChar())
            i += 2
        }
        return sb.toString()
    }

    fun toHex(bArr: ByteArray): String =
        bArr.joinToString("") { "%02X".format(it) }

    fun decompress(compress: ByteArray): ByteArray {
        ByteArrayInputStream(compress).use { bais ->
            InflaterInputStream(bais).use { iis ->
                val baos = ByteArrayOutputStream()
                val buf = ByteArray(8192)
                while (true) {
                    val n = iis.read(buf)
                    if (n < 0) break
                    baos.write(buf, 0, n)
                }
                return baos.toByteArray()
            }
        }
    }
}
