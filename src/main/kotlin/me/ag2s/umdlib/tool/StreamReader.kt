package me.ag2s.umdlib.tool

import java.io.InputStream

class StreamReader(private val input: InputStream) {
    var offset: Long = 0
        private set

    private fun inc(n: Int) {
        offset += n
    }

    /**
     * Matches original umdlib: on EOF returns 0 (unsigned fill), does not throw.
     * UmdReader relies on this to exit the 0x23 section loop at end-of-file.
     */
    fun readByte(): Byte {
        val b = ByteArray(1)
        val n = input.read(b)
        if (n > 0) inc(1)
        // n < 0 → EOF, leave b[0]=0
        return b[0]
    }

    fun readUint8(): Short = (readByte().toInt() and 0xFF).toShort()

    fun readBytes(len: Int): ByteArray {
        require(len > 0) { "Length must > 0: $len" }
        val b = ByteArray(len)
        var off = 0
        while (off < len) {
            val n = input.read(b, off, len - off)
            if (n < 0) break // original leaves zeros for unread tail
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
