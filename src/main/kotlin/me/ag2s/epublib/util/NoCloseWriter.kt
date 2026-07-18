package me.ag2s.epublib.util

import java.io.Writer

class NoCloseWriter(private val writer: Writer) : Writer() {
    override fun close() = Unit
    override fun flush() = writer.flush()
    override fun write(cbuf: CharArray, off: Int, len: Int) = writer.write(cbuf, off, len)
}
