package me.ag2s.umdlib.domain

import me.ag2s.umdlib.tool.UmdUtils
import me.ag2s.umdlib.tool.WrapOutputStream

class UmdHeader {
    var umdType: Byte = 0
    var title: String? = null
    var author: String? = null
    var year: String? = null
    var month: String? = null
    var day: String? = null
    var bookType: String? = null
    var bookMan: String? = null
    var shopKeeper: String? = null

    fun buildHeader(wos: WrapOutputStream) {
        wos.writeBytes(137,155,154,222,35,1,0,0,8,1)
        wos.writeBytes(UmdUtils.genRandomBytes(2))
        listOf(2 to title,3 to author,4 to year,5 to month,6 to day,7 to bookType,8 to bookMan,9 to shopKeeper).forEach { (type, content) -> buildType(wos,type.toByte(),content) }
    }
    fun buildType(wos: WrapOutputStream, type: Byte, content: String?) {
        if (content.isNullOrEmpty()) return
        wos.writeBytes(35,type.toInt(),0,0)
        val bytes=UmdUtils.stringToUnicodeBytes(content)
        wos.writeByte(bytes.size+5)
        wos.write(bytes)
    }
}
