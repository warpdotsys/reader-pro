package me.ag2s.umdlib.tool

import java.io.InputStream

class StreamReader(private val inputStream: InputStream) {
    var offset: Long = 0
    var size: Long = 0

    private fun incCount(value: Int) { offset = (offset + value).let { if (it < 0) Int.MAX_VALUE.toLong() else it } }
    fun readUint8(): Short = ByteArray(1).also { inputStream.read(it); incCount(1) }[0].let { (it.toInt() and 0xff).toShort() }
    fun readByte(): Byte = ByteArray(1).also { inputStream.read(it); incCount(1) }[0]
    fun readBytes(length: Int): ByteArray { require(length >= 1) { "Length must > 0: $length" }; return ByteArray(length).also { inputStream.read(it); incCount(length) } }
    fun readHex(length: Int): String = UmdUtils.toHex(readBytes(length))
    fun readShort(): Short { val b=readBytes(2); return (((b[0].toInt() and 0xff) shl 8) or (b[1].toInt() and 0xff)).toShort() }
    fun readShortLe(): Short { val b=readBytes(2); return (((b[1].toInt() and 0xff) shl 8) or (b[0].toInt() and 0xff)).toShort() }
    fun readInt(): Int { val b=readBytes(4); return (b[0].toInt() and 0xff shl 24) or (b[1].toInt() and 0xff shl 16) or (b[2].toInt() and 0xff shl 8) or (b[3].toInt() and 0xff) }
    fun readIntLe(): Int { val b=readBytes(4); return (b[3].toInt() and 0xff shl 24) or (b[2].toInt() and 0xff shl 16) or (b[1].toInt() and 0xff shl 8) or (b[0].toInt() and 0xff) }
    fun skip(length: Int) { readBytes(length) }
    fun read(bytes: ByteArray): ByteArray { inputStream.read(bytes); incCount(bytes.size); return bytes }
    fun read(bytes: ByteArray, off: Int, len: Int): ByteArray { inputStream.read(bytes,off,len); incCount(len); return bytes }
}
