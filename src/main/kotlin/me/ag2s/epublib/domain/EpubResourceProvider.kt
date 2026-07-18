package me.ag2s.epublib.domain

import java.io.IOException
import java.io.InputStream
import java.util.zip.ZipFile

class EpubResourceProvider(private val epubFilename: String) : LazyResourceProvider {
    @Throws(IOException::class)
    override fun getResourceStream(href: String?): InputStream {
        val zipFile = ZipFile(epubFilename)
        val zipEntry = zipFile.getEntry(href)
        if (zipEntry == null) {
            zipFile.close()
            throw IllegalStateException("Cannot find entry $href in epub file $epubFilename")
        }
        return ResourceInputStream(zipFile.getInputStream(zipEntry), zipFile)
    }
}
