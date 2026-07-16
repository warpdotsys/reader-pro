package me.ag2s.umdlib.domain

import java.io.ByteArrayOutputStream

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
}

class UmdChapters {
    private val titles = ArrayList<ByteArray>()
    val contentLengths = ArrayList<Int>()
    val contents = ByteArrayOutputStream()
    var totalContentLen: Int = 0

    fun addTitle(t: ByteArray) {
        titles.add(t)
    }

    fun addContentLength(n: Int) {
        contentLengths.add(n)
    }

    fun getTitles(): List<ByteArray> = titles

    fun getTitle(index: Int): String =
        me.ag2s.umdlib.tool.UmdUtils.unicodeBytesToString(titles[index])

    fun getContentString(index: Int): String {
        val all = contents.toByteArray()
        if (contentLengths.isEmpty() || index !in contentLengths.indices) return ""
        val st = contentLengths[index]
        val end = if (index + 1 < contentLengths.size) contentLengths[index + 1]
        else totalContentLen.coerceAtMost(all.size).let { if (it <= st) all.size else it }
        if (st < 0 || st >= all.size) return ""
        val e = end.coerceIn(st, all.size)
        return me.ag2s.umdlib.tool.UmdUtils.unicodeBytesToString(all.copyOfRange(st, e))
            .replace('\u2029', '\n')
    }
}

class UmdCover(val coverData: ByteArray? = null)

class UmdBook {
    var num: Int = 0
    var header: UmdHeader = UmdHeader()
    var chapters: UmdChapters = UmdChapters()
    var cover: UmdCover = UmdCover()
}
