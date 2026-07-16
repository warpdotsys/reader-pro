package me.ag2s.umdlib.tool

import java.io.IOException
import java.io.OutputStream

/** Little-endian counting output stream used by UMD builders. */
class WrapOutputStream(private val os: OutputStream) : OutputStream() {
    var written: Int = 0
        private set

    private fun inc(n: Int) {
        val t = written + n
        written = if (t < 0) Int.MAX_VALUE else t
    }

    fun writeInt(v: Int) {
        os.write(v ushr 0 and 0xFF)
        os.write(v ushr 8 and 0xFF)
        os.write(v ushr 16 and 0xFF)
        os.write(v ushr 24 and 0xFF)
        inc(4)
    }

    fun writeByte(b: Byte) = write(b.toInt() and 0xFF)

    fun writeByte(n: Int) = write(n and 0xFF)

    fun writeBytes(vararg bytes: Byte) {
        write(bytes)
    }

    fun writeBytes(vararg vals: Int) {
        for (v in vals) write(v and 0xFF)
    }

    @Throws(IOException::class)
    override fun write(b: ByteArray, off: Int, len: Int) {
        os.write(b, off, len)
        inc(len)
    }

    @Throws(IOException::class)
    override fun write(b: ByteArray) {
        os.write(b)
        inc(b.size)
    }

    @Throws(IOException::class)
    override fun write(b: Int) {
        os.write(b)
        inc(1)
    }

    @Throws(IOException::class)
    override fun close() = os.close()

    @Throws(IOException::class)
    override fun flush() = os.flush()
}
