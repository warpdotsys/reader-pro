package me.ag2s.epublib.util.commons.io

import java.io.IOException
import java.io.InputStream

class BOMInputStream(delegate: InputStream, private val include: Boolean, vararg configuredBoms: ByteOrderMark) : ProxyInputStream(delegate) {
    private val boms: List<ByteOrderMark>
    private var byteOrderMark: ByteOrderMark? = null
    private var firstBytes: IntArray? = null
    private var fbLength = 0
    private var fbIndex = 0
    private var markFbIndex = 0
    private var markedAtStart = false
    init { require(configuredBoms.isNotEmpty()) { "No BOMs specified" }; boms = configuredBoms.asList() }
    constructor(delegate: InputStream) : this(delegate, false, ByteOrderMark.UTF_8)
    constructor(delegate: InputStream, include: Boolean) : this(delegate, include, ByteOrderMark.UTF_8)
    constructor(delegate: InputStream, vararg boms: ByteOrderMark) : this(delegate, false, *boms)
    @Throws(IOException::class) fun hasBOM(): Boolean = getBOM() != null
    @Throws(IOException::class) fun hasBOM(bom: ByteOrderMark): Boolean { require(boms.contains(bom)) { "Stream not configure to detect $bom" }; getBOM(); return byteOrderMark?.equals(bom) ?: false }
    @Throws(IOException::class) fun getBOM(): ByteOrderMark? {
        if (firstBytes == null) {
            fbLength = 0
            firstBytes = IntArray(boms.first().length())
            var index = 0
            while (index < firstBytes!!.size) {
                firstBytes!![index] = `in`.read()
                fbLength++
                if (firstBytes!![index] < 0) break
                index++
            }
            byteOrderMark = find()
            if (byteOrderMark != null && !include) { if (byteOrderMark!!.length() < firstBytes!!.size) fbIndex = byteOrderMark!!.length() else fbLength = 0 }
        }
        return byteOrderMark
    }
    @Throws(IOException::class) fun getBOMCharsetName(): String? = getBOM()?.getCharsetName()
    @Throws(IOException::class) private fun readFirstBytes(): Int { getBOM(); return if (fbIndex < fbLength) firstBytes!![fbIndex++] else -1 }
    private fun find(): ByteOrderMark? = boms.firstOrNull { matches(it) }
    private fun matches(bom: ByteOrderMark): Boolean = (0 until bom.length()).all { bom[it] == firstBytes!![it] }
    @Throws(IOException::class) override fun read(): Int { val value = readFirstBytes(); return if (value >= 0) value else `in`.read() }
    @Throws(IOException::class) override fun read(buffer: ByteArray, offset: Int, length: Int): Int { var off = offset; var remaining = length; var count = 0; var value = 0; while (remaining > 0 && value >= 0) { value = readFirstBytes(); if (value >= 0) { buffer[off++] = (value and 0xff).toByte(); remaining--; count++ } }; val secondCount = `in`.read(buffer, off, remaining); return if (secondCount < 0) if (count > 0) count else -1 else count + secondCount }
    @Throws(IOException::class) override fun read(buffer: ByteArray): Int = read(buffer, 0, buffer.size)
    override fun mark(readlimit: Int) { markFbIndex = fbIndex; markedAtStart = firstBytes == null; `in`.mark(readlimit) }
    @Throws(IOException::class) override fun reset() { fbIndex = markFbIndex; if (markedAtStart) firstBytes = null; `in`.reset() }
    @Throws(IOException::class) override fun skip(amount: Long): Long { var skipped = 0; while (amount > skipped && readFirstBytes() >= 0) skipped++; return `in`.skip(amount - skipped) + skipped }
}
