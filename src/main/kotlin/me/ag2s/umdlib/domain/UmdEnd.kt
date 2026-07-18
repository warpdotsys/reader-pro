package me.ag2s.umdlib.domain

import me.ag2s.umdlib.tool.WrapOutputStream

class UmdEnd {
    fun buildEnd(wos: WrapOutputStream) {
        wos.writeBytes(35, 12, 0, 1, 9)
        wos.writeInt(wos.written + 4)
    }
}
