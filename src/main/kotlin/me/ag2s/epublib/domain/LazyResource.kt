package me.ag2s.epublib.domain

import java.io.ByteArrayInputStream
import java.io.IOException
import java.io.InputStream
import me.ag2s.epublib.util.IOUtil

class LazyResource : Resource {
    private val tag = javaClass.name
    private val resourceProvider: LazyResourceProvider
    private val cachedSize: Long
    constructor(resourceProvider: LazyResourceProvider, href: String?) : this(resourceProvider, -1L, href)
    constructor(resourceProvider: LazyResourceProvider, href: String?, originalHref: String?) : this(resourceProvider, -1L, href, originalHref)
    constructor(resourceProvider: LazyResourceProvider, size: Long, href: String?) : super(null, null, href, MediaTypes.determineMediaType(href)) { this.resourceProvider = resourceProvider; cachedSize = size }
    constructor(resourceProvider: LazyResourceProvider, size: Long, href: String?, originalHref: String?) : super(null, null, href, originalHref, MediaTypes.determineMediaType(href)) { this.resourceProvider = resourceProvider; cachedSize = size }
    @Throws(IOException::class)
    override fun getInputStream(): InputStream = if (isInitialized()) ByteArrayInputStream(getData()) else resourceProvider.getResourceStream(originalHref)
    @Throws(IOException::class)
    fun initialize() { getData() }
    @Throws(IOException::class)
    override fun getData(): ByteArray {
        if (data == null) {
            val input = resourceProvider.getResourceStream(originalHref)
            val readData = IOUtil.toByteArray(input, cachedSize.toInt())
            if (readData == null) throw IOException("Could not load the contents of resource: ${getHref()}")
            data = readData
            input.close()
        }
        return data!!
    }
    override fun close() { data = null }
    fun isInitialized(): Boolean = data != null
    override fun getSize(): Long = data?.size?.toLong() ?: cachedSize
    companion object { private const val serialVersionUID = 5089400472352002866L }
}
