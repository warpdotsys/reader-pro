package me.ag2s.umdlib.domain

import me.ag2s.umdlib.tool.UmdUtils
import me.ag2s.umdlib.tool.WrapOutputStream
import java.io.ByteArrayOutputStream
import java.io.IOException
import java.io.OutputStream
import java.util.Arrays

class UmdHeader {
    var umdType: Byte = 0
    var title: String = ""
    var author: String = ""
    var year: String = ""
    var month: String = ""
    var day: String = ""
    var bookType: String = ""
    var bookMan: String = ""
    var shopKeeper: String = ""

    @Throws(IOException::class)
    fun buildHeader(wos: WrapOutputStream) {
        // magic 0xDE9A9B89 LE == -560292983
        wos.writeBytes(137, 155, 154, 222)
        wos.writeByte(35)
        wos.writeBytes(1, 0, 0, 8)
        wos.writeByte(1)
        wos.write(UmdUtils.genRandomBytes(2))
        buildType(wos, 2, title)
        buildType(wos, 3, author)
        buildType(wos, 4, year)
        buildType(wos, 5, month)
        buildType(wos, 6, day)
        buildType(wos, 7, bookType)
        buildType(wos, 8, bookMan)
        buildType(wos, 9, shopKeeper)
    }

    @Throws(IOException::class)
    private fun buildType(wos: WrapOutputStream, type: Int, content: String?) {
        if (content.isNullOrEmpty()) return
        wos.writeBytes(35, type, 0, 0)
        val temp = UmdUtils.stringToUnicodeBytes(content)
        wos.writeByte(temp.size + 5)
        wos.write(temp)
    }
}

class UmdChapters {
    private val titles = ArrayList<ByteArray>()
    /** When writing: chapter byte lengths. After reading: absolute offsets into contents. */
    val contentLengths = ArrayList<Int>()
    val contents = ByteArrayOutputStream()
    var totalContentLen: Int = 0

    fun addTitle(t: ByteArray) {
        titles.add(t)
    }

    fun addTitle(s: String) {
        titles.add(UmdUtils.stringToUnicodeBytes(s))
    }

    fun addContentLength(n: Int) {
        contentLengths.add(n)
    }

    fun getTitles(): List<ByteArray> = titles

    fun getTitle(index: Int): String =
        UmdUtils.unicodeBytesToString(titles[index])

    fun getContentString(index: Int): String {
        val all = contents.toByteArray()
        if (contentLengths.isEmpty() || index !in contentLengths.indices) return ""
        val st = contentLengths[index]
        val end = if (index + 1 < contentLengths.size) contentLengths[index + 1]
        else totalContentLen.coerceAtMost(all.size).let { if (it <= st) all.size else it }
        if (st < 0 || st >= all.size) return ""
        val e = end.coerceIn(st, all.size)
        return UmdUtils.unicodeBytesToString(all.copyOfRange(st, e))
            .replace('\u2029', '\n')
    }

    /** Append a chapter for writing (stores title + body unicode bytes). */
    fun addChapter(title: String, content: String) {
        titles.add(UmdUtils.stringToUnicodeBytes(title))
        val b = UmdUtils.stringToUnicodeBytes(content)
        contentLengths.add(b.size)
        contents.write(b)
    }

    fun clearChapters() {
        titles.clear()
        contentLengths.clear()
        contents.reset()
        totalContentLen = 0
    }

    @Throws(IOException::class)
    fun buildChapters(wos: WrapOutputStream) {
        writeChaptersHead(wos)
        writeChaptersContentOffset(wos)
        writeChaptersTitles(wos)
        writeChaptersChunks(wos)
    }

    @Throws(IOException::class)
    private fun writeChaptersHead(wos: WrapOutputStream) {
        wos.writeBytes(35, 11, 0, 0, 9)
        wos.writeInt(contents.size())
    }

    @Throws(IOException::class)
    private fun writeChaptersContentOffset(wos: WrapOutputStream) {
        wos.writeBytes(35, 131, 0, 0, 9)
        val rb = UmdUtils.genRandomBytes(4)
        wos.write(rb)
        wos.write(36)
        wos.write(rb)
        wos.writeInt(contentLengths.size * 4 + 9)
        var offset = 0
        for (n in contentLengths) {
            wos.writeInt(offset)
            offset += n
        }
    }

    @Throws(IOException::class)
    private fun writeChaptersTitles(wos: WrapOutputStream) {
        wos.writeBytes(35, 132, 0, 1, 9)
        val rb = UmdUtils.genRandomBytes(4)
        wos.write(rb)
        wos.write(36)
        wos.write(rb)
        var totalTitlesLen = 0
        for (t in titles) totalTitlesLen += t.size
        wos.writeInt(totalTitlesLen + titles.size + 9)
        for (t in titles) {
            wos.writeByte(t.size)
            wos.write(t)
        }
    }

    @Throws(IOException::class)
    private fun writeChaptersChunks(wos: WrapOutputStream) {
        val allContents = contents.toByteArray()
        val zero16 = ByteArray(16)
        Arrays.fill(zero16, 0.toByte())
        var startPos = 0
        val chunkRbList = ArrayList<ByteArray>()
        while (startPos < allContents.size) {
            val left = allContents.size - startPos
            val len = minOf(32768, left)
            val chunk = UmdUtils.compress(allContents, startPos, len)
            val rb = UmdUtils.genRandomBytes(4)
            wos.writeByte(36)
            wos.write(rb)
            chunkRbList.add(rb)
            wos.writeInt(chunk.size + 9)
            wos.write(chunk)
            wos.writeBytes(35, 241, 0, 0, 21)
            wos.write(zero16)
            startPos += len
        }
        val chunkCnt = chunkRbList.size
        wos.writeBytes(35, 129, 0, 1, 9)
        wos.writeBytes(0, 0, 0, 0)
        wos.write(36)
        wos.writeBytes(0, 0, 0, 0)
        wos.writeInt(chunkCnt * 4 + 9)
        for (i in chunkCnt - 1 downTo 0) {
            wos.write(chunkRbList[i])
        }
    }
}

class UmdCover(var coverData: ByteArray? = null) {
    @Throws(IOException::class)
    fun buildCover(wos: WrapOutputStream) {
        val data = coverData
        if (data == null || data.isEmpty()) return
        wos.writeBytes(35, 130, 0, 1, 10, 1)
        val rb = UmdUtils.genRandomBytes(4)
        wos.write(rb)
        wos.write(36)
        wos.write(rb)
        wos.writeInt(data.size + 9)
        wos.write(data)
    }
}

class UmdEnd {
    @Throws(IOException::class)
    fun buildEnd(wos: WrapOutputStream) {
        wos.writeBytes(35, 12, 0, 1, 9)
        wos.writeInt(wos.written + 4)
    }
}

class UmdBook {
    var num: Int = 0
    var header: UmdHeader = UmdHeader()
    var chapters: UmdChapters = UmdChapters()
    var cover: UmdCover = UmdCover()
    var end: UmdEnd = UmdEnd()

    @Throws(IOException::class)
    fun buildUmd(os: OutputStream) {
        val wos = WrapOutputStream(os)
        header.buildHeader(wos)
        chapters.buildChapters(wos)
        cover.buildCover(wos)
        end.buildEnd(wos)
    }
}
