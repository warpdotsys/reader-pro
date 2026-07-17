package io.legado.app.utils

object UTF8BOMFighter {
    private val utf8BomBytes = byteArrayOf(0xEF.toByte(), 0xBB.toByte(), 0xBF.toByte())

    fun removeUTF8BOM(xmlText: String): String =
        removeUTF8BOM(xmlText.toByteArray(Charsets.UTF_8)).toString(Charsets.UTF_8)

    fun removeUTF8BOM(bytes: ByteArray): ByteArray =
        if (bytes.size > 3 && bytes[0] == utf8BomBytes[0] && bytes[1] == utf8BomBytes[1] && bytes[2] == utf8BomBytes[2]) {
            bytes.copyOfRange(3, bytes.size)
        } else {
            bytes
        }
}
