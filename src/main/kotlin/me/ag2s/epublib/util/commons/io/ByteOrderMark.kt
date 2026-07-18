package me.ag2s.epublib.util.commons.io

import java.io.Serializable
import java.util.Locale

class ByteOrderMark(charsetName: String?, vararg bytes: Int) : Serializable {
    private val charsetName: String
    private val bytes: IntArray
    init {
        require(!charsetName.isNullOrEmpty()) { "No charsetName specified" }
        require(bytes.isNotEmpty()) { "No bytes specified" }
        this.charsetName = charsetName
        this.bytes = bytes.copyOf()
    }
    fun getCharsetName(): String = charsetName
    fun length(): Int = bytes.size
    operator fun get(position: Int): Int = bytes[position]
    fun getBytes(): ByteArray = bytes.map { it.toByte() }.toByteArray()
    override fun equals(other: Any?): Boolean = other is ByteOrderMark && bytes.contentEquals(other.bytes)
    override fun hashCode(): Int = javaClass.hashCode() + bytes.sum()
    override fun toString(): String = buildString { append(javaClass.simpleName).append('[').append(charsetName).append(": "); bytes.forEachIndexed { index, value -> if (index > 0) append(','); append("0x").append((value and 0xff).toString(16).uppercase(Locale.ROOT)) }; append(']') }
    companion object {
        private const val serialVersionUID = 1L
        @JvmField val UTF_8 = ByteOrderMark("UTF-8", 239, 187, 191)
        @JvmField val UTF_16BE = ByteOrderMark("UTF-16BE", 254, 255)
        @JvmField val UTF_16LE = ByteOrderMark("UTF-16LE", 255, 254)
        @JvmField val UTF_32BE = ByteOrderMark("UTF-32BE", 0, 0, 254, 255)
        @JvmField val UTF_32LE = ByteOrderMark("UTF-32LE", 255, 254, 0, 0)
        const val UTF_BOM = '\ufeff'
    }
}
