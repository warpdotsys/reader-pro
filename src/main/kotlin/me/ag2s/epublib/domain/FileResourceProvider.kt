package me.ag2s.epublib.domain

import java.io.File
import java.io.FileInputStream
import java.io.IOException
import java.io.InputStream

class FileResourceProvider : LazyResourceProvider {
    internal var dir: String
    constructor(parentDir: String) { dir = parentDir }
    constructor(parentFile: File) { dir = parentFile.path }
    @Throws(IOException::class)
    override fun getResourceStream(href: String?): InputStream = FileInputStream(File(dir, href))
}
