package me.ag2s.epublib.domain

import java.io.FilterInputStream
import java.io.IOException
import java.io.InputStream
import java.util.zip.ZipFile

class ResourceInputStream(input: InputStream, private val zipFile: ZipFile) : FilterInputStream(input) {
    @Throws(IOException::class)
    override fun close() { super.close(); zipFile.close() }
}
