package me.ag2s.epublib.epub

import me.ag2s.epublib.domain.EpubBook
import java.io.OutputStream
import java.util.zip.ZipEntry
import java.util.zip.ZipOutputStream

class EpubWriter {
    fun write(book: EpubBook, out: OutputStream) {
        ZipOutputStream(out).use { zos ->
            zos.putNextEntry(ZipEntry("mimetype"))
            zos.write("application/epub+zip".toByteArray())
            zos.closeEntry()
            book.getSections().forEach { (_, res) ->
                zos.putNextEntry(ZipEntry(res.href))
                zos.write(res.data)
                zos.closeEntry()
            }
        }
    }
}
