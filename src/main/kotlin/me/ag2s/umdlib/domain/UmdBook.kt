package me.ag2s.umdlib.domain

import java.io.OutputStream
import me.ag2s.umdlib.tool.WrapOutputStream

class UmdBook {
    var num: Int = 0
    var header = UmdHeader()
    var chapters = UmdChapters()
    var cover = UmdCover()
    var end = UmdEnd()
    fun buildUmd(output: OutputStream) { WrapOutputStream(output).also { wos -> header.buildHeader(wos); chapters.buildChapters(wos); cover.buildCover(wos); end.buildEnd(wos) } }
}
