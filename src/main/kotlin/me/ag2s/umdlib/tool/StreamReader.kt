package me.ag2s.umdlib.tool

import java.io.InputStream

class StreamReader(private val input: InputStream) {
    var offset: Long = 0
        private set

    private fun inc(n: Int) {
        offset += n
    }

    fun readByte(): Byte {
        val b = input.read()
        if (b < 0) error("EOF")
        inc(1)
        return b.toByte()
    }

    fun readUint8(): Short = (readByte().toInt() and 0xFF).toShort()

    fun readBytes(len: Int): ByteArray {
        require(len > 0)
        val b = ByteArray(len)
        var off = 0
        while (off < len) {
            val n = input.read(b, off, len - off)
            if (n < 0) error("EOF")
            off += n
        }
        inc(len)
        return b
    }

    fun readHex(len: Int): String = UmdUtils.toHex(readBytes(len))

    fun readShortLe(): Short {
        val b = readBytes(2)
        return (((b[1].toInt() and 0xFF) shl 8) or (b[0].toInt() and 0xFF)).toShort()
    }

    fun readIntLe(): Int {
        val b = readBytes(4)
        return ((b[3].toInt() and 0xFF) shl 24) or
            ((b[2].toInt() and 0xFF) shl 16) or
            ((b[1].toInt() and 0xFF) shl 8) or
            (b[0].toInt() and 0xFF)
    }
}
