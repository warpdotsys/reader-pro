package me.ag2s.epublib.domain

import java.io.IOException
import java.io.InputStream

interface LazyResourceProvider {
    @Throws(IOException::class)
    fun getResourceStream(href: String?): InputStream
}
