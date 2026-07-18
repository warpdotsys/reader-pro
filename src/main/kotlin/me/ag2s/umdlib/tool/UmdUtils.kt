package me.ag2s.umdlib.tool

import java.io.BufferedInputStream
import java.io.BufferedOutputStream
import java.io.ByteArrayInputStream
import java.io.ByteArrayOutputStream
import java.io.File
import java.io.FileInputStream
import java.io.FileOutputStream
import java.util.Random
import java.util.zip.InflaterInputStream

object UmdUtils {
    private val random = Random()

    fun stringToUnicodeBytes(value: String): ByteArray = ByteArray(value.length * 2).also { result ->
        value.forEachIndexed { index, char ->
            result[index * 2] = (char.code and 0xff).toByte()
            result[index * 2 + 1] = (char.code ushr 8).toByte()
        }
    }

    fun unicodeBytesToString(bytes: ByteArray): String = buildString(bytes.size / 2) {
        for (index in 0 until bytes.size / 2) append(((bytes[index * 2 + 1].toInt() and 0xff shl 8) or (bytes[index * 2].toInt() and 0xff)).toChar())
    }

    fun toHex(bytes: ByteArray): String = bytes.joinToString("") { "%02X".format(it.toInt() and 0xff) }

    fun decompress(compress: ByteArray): ByteArray = InflaterInputStream(ByteArrayInputStream(compress)).use { input ->
        ByteArrayOutputStream().use { output -> input.copyTo(output, 8192); output.toByteArray() }
    }

    fun saveFile(file: File, content: ByteArray) = FileOutputStream(file).use { stream -> BufferedOutputStream(stream).use { it.write(content); it.flush() } }
    fun readFile(file: File): ByteArray = FileInputStream(file).use { stream -> BufferedInputStream(stream).use { it.readBytes() } }
    fun genRandomBytes(length: Int): ByteArray {
        require(length > 0) { "Length must > 0: $length" }
        return ByteArray(length).also { random.nextBytes(it) }
    }
}
