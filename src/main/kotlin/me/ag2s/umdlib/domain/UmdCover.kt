package me.ag2s.umdlib.domain

import java.io.File
import me.ag2s.umdlib.tool.UmdUtils
import me.ag2s.umdlib.tool.WrapOutputStream

class UmdCover(var coverData: ByteArray? = null) {
    fun load(file: File) { coverData = UmdUtils.readFile(file) }
    fun load(fileName: String) = load(File(fileName))
    fun initDefaultCover(title: String) = Unit
    fun buildCover(wos: WrapOutputStream) {
        val data = coverData ?: return
        if (data.isEmpty()) return
        wos.writeBytes(35, 130, 0, 1, 10, 1)
        val randomBytes = UmdUtils.genRandomBytes(4)
        wos.writeBytes(randomBytes)
        wos.write(36)
        wos.writeBytes(randomBytes)
        wos.writeInt(data.size + 9)
        wos.write(data)
    }
}
