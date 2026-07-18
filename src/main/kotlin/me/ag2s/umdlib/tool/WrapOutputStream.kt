package me.ag2s.umdlib.tool

import java.io.OutputStream

class WrapOutputStream(private val outputStream: OutputStream) : OutputStream() {
    var written: Int = 0
        private set

    private fun incCount(value: Int) {
        val next = written + value
        written = if (next < 0) Int.MAX_VALUE else next
    }

    fun writeInt(value: Int) {
        outputStream.write(value ushr 0 and 0xff)
        outputStream.write(value ushr 8 and 0xff)
        outputStream.write(value ushr 16 and 0xff)
        outputStream.write(value ushr 24 and 0xff)
        incCount(4)
    }

    fun writeByte(value: Byte) = write(value.toInt())
    fun writeByte(value: Int) = write(value)
    fun writeBytes(vararg bytes: Byte) = write(bytes)
    fun writeBytes(vararg values: Int) = values.forEach(::write)

    override fun write(bytes: ByteArray, off: Int, len: Int) {
        outputStream.write(bytes, off, len)
        incCount(len)
    }

    override fun write(bytes: ByteArray) {
        outputStream.write(bytes)
        incCount(bytes.size)
    }

    override fun write(value: Int) {
        outputStream.write(value)
        incCount(1)
    }

    override fun close() = outputStream.close()
    override fun flush() = outputStream.flush()
    override fun equals(other: Any?): Boolean = outputStream == other
    override fun hashCode(): Int = outputStream.hashCode()
    override fun toString(): String = outputStream.toString()
}
