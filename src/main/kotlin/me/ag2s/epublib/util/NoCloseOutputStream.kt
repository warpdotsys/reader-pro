package me.ag2s.epublib.util

import java.io.OutputStream

class NoCloseOutputStream(private val outputStream: OutputStream) : OutputStream() {
    override fun write(b: Int) = outputStream.write(b)
    override fun close() = Unit
}
