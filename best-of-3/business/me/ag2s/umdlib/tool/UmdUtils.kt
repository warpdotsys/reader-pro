package me.ag2s.umdlib.tool

import java.io.ByteArrayInputStream
import java.io.ByteArrayOutputStream
import java.io.File
import java.util.Random
import java.util.zip.DeflaterOutputStream
import java.util.zip.InflaterInputStream

object UmdUtils {
    private val random = Random()

    fun stringToUnicodeBytes(s: String): ByteArray {
        val ret = ByteArray(s.length * 2)
        for (i in s.indices) {
            val c = s[i].code
            ret[i * 2] = (c and 0xFF).toByte()
            ret[i * 2 + 1] = (c shr 8 and 0xFF).toByte()
        }
        return ret
    }

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

    fun compress(data: ByteArray, off: Int = 0, len: Int = data.size): ByteArray {
        val bos = ByteArrayOutputStream(len.coerceAtMost(32768) + 256)
        DeflaterOutputStream(bos).use { zos ->
            zos.write(data, off, len)
        }
        return bos.toByteArray()
    }

    fun genRandomBytes(len: Int): ByteArray {
        require(len > 0)
        return ByteArray(len) { random.nextInt(256).toByte() }
    }

    fun readFile(f: File): ByteArray = f.readBytes()

    fun saveFile(f: File, content: ByteArray) {
        f.parentFile?.mkdirs()
        f.writeBytes(content)
    }
}

