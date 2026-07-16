package me.ag2s.umdlib.umd

import me.ag2s.umdlib.domain.UmdBook
import me.ag2s.umdlib.domain.UmdCover
import me.ag2s.umdlib.domain.UmdHeader
import me.ag2s.umdlib.tool.StreamReader
import me.ag2s.umdlib.tool.UmdUtils
import java.io.InputStream

/** Read-only UMD parser (me.ag2s.umdlib port). */
class UmdReader {
    private var book = UmdBook()
    private var additionalCheckNumber = 0
    private var totalContentLen = 0

    @Synchronized
    fun read(inputStream: InputStream): UmdBook {
        book = UmdBook()
        val reader = StreamReader(inputStream)
        val header = UmdHeader()
        book.header = header
        if (reader.readIntLe() != -560292983) error("Wrong UMD header")
        var prevType: Short = -1
        var ch = reader.readByte()
        while (ch.toInt() == 0x23) {
            var segType = reader.readShortLe()
            reader.readByte()
            val len = (reader.readUint8() - 5).toShort()
            readSection(segType, len, reader, header)
            if (segType.toInt() == 241 || segType.toInt() == 10) segType = prevType
            ch = reader.readByte()
            while (ch.toInt() == 0x24) {
                val additional = reader.readIntLe()
                val length2 = reader.readIntLe() - 9
                readAdditional(segType, additional, length2, reader)
                ch = reader.readByte()
            }
            prevType = segType
        }
        return book
    }

    private fun readAdditional(segType: Short, additional: Int, length: Int, reader: StreamReader) {
        val len = length.coerceAtLeast(0)
        when (segType.toInt()) {
            129 -> if (len > 0) reader.readBytes(len)
            130 -> if (len > 0) book.cover = UmdCover(reader.readBytes(len))
            131 -> {
                book.num = len / 4
                repeat(book.num) { book.chapters.addContentLength(reader.readIntLe()) }
            }
            132 -> {
                if (additionalCheckNumber != additional) {
                    if (len > 0) {
                        val decompressed = UmdUtils.decompress(reader.readBytes(len))
                        book.chapters.contents.write(decompressed)
                    }
                } else {
                    repeat(book.num) {
                        val tlen = reader.readUint8().toInt()
                        book.chapters.addTitle(if (tlen > 0) reader.readBytes(tlen) else ByteArray(0))
                    }
                }
            }
            else -> if (len > 0) reader.readBytes(len)
        }
    }

    private fun readSection(segType: Short, length: Short, reader: StreamReader, header: UmdHeader) {
        val len = length.toInt().coerceAtLeast(0)
        when (segType.toInt()) {
            1 -> {
                header.umdType = reader.readByte()
                if (len > 1) reader.readBytes(len - 1)
            }
            2 -> if (len > 0) header.title = UmdUtils.unicodeBytesToString(reader.readBytes(len))
            3 -> if (len > 0) header.author = UmdUtils.unicodeBytesToString(reader.readBytes(len))
            4 -> if (len > 0) header.year = UmdUtils.unicodeBytesToString(reader.readBytes(len))
            5 -> if (len > 0) header.month = UmdUtils.unicodeBytesToString(reader.readBytes(len))
            6 -> if (len > 0) header.day = UmdUtils.unicodeBytesToString(reader.readBytes(len))
            7 -> if (len > 0) header.bookType = UmdUtils.unicodeBytesToString(reader.readBytes(len))
            8 -> if (len > 0) header.bookMan = UmdUtils.unicodeBytesToString(reader.readBytes(len))
            9 -> if (len > 0) header.shopKeeper = UmdUtils.unicodeBytesToString(reader.readBytes(len))
            10 -> if (len > 0) reader.readHex(len)
            11 -> {
                totalContentLen = reader.readIntLe()
                book.chapters.totalContentLen = totalContentLen
            }
            12 -> reader.readIntLe()
            14 -> reader.readByte()
            15, 129, 131 -> additionalCheckNumber = reader.readIntLe()
            132 -> additionalCheckNumber = reader.readIntLe()
            130 -> {
                reader.readByte()
                additionalCheckNumber = reader.readIntLe()
            }
            135 -> {
                reader.readUint8(); reader.readUint8(); reader.readBytes(4)
            }
            241 -> reader.readHex(16)
            else -> if (len > 0) reader.readBytes(len)
        }
    }
}
