package io.legado.app.utils

object Utf8BomUtils {
    private val UTF8_BOM_BYTES = byteArrayOf(0xEF.toByte(), 0xBB.toByte(), 0xBF.toByte())

    fun removeUTF8BOM(xmlText: String): String = removeUTF8BOM(xmlText.toByteArray(Charsets.UTF_8)).toString(Charsets.UTF_8)

    fun removeUTF8BOM(bytes: ByteArray): ByteArray =
        if (hasBom(bytes)) bytes.copyOfRange(3, bytes.size) else bytes

    fun hasBom(bytes: ByteArray): Boolean =
        bytes.size > 3 && bytes[0] == UTF8_BOM_BYTES[0] && bytes[1] == UTF8_BOM_BYTES[1] && bytes[2] == UTF8_BOM_BYTES[2]
}
