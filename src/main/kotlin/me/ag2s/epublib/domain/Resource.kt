package me.ag2s.epublib.domain

import java.io.ByteArrayInputStream
import java.io.IOException
import java.io.InputStream
import java.io.Reader
import java.io.Serializable
import me.ag2s.epublib.util.IOUtil
import me.ag2s.epublib.util.StringUtil
import me.ag2s.epublib.util.commons.io.XmlStreamReader

open class Resource : Serializable {
    private var id: String? = null
    private var title: String? = null
    private var href: String? = null
    private var properties: String? = null
    protected val originalHref: String?
    private var mediaType: MediaType? = null
    private var inputEncoding: String? = null
    @JvmField
    protected var data: ByteArray? = null

    constructor(href: String?) : this(null, ByteArray(0), href, MediaTypes.determineMediaType(href))
    constructor(data: ByteArray?, mediaType: MediaType?) : this(null, data, null, mediaType)
    constructor(data: ByteArray?, href: String?) : this(null, data, href, MediaTypes.determineMediaType(href), "UTF-8")
    @Throws(IOException::class) constructor(input: Reader, href: String?) : this(null, IOUtil.toByteArray(input, "UTF-8"), href, MediaTypes.determineMediaType(href), "UTF-8")
    @Throws(IOException::class) constructor(input: InputStream, href: String?) : this(null, IOUtil.toByteArray(input), href, MediaTypes.determineMediaType(href))
    constructor(id: String?, data: ByteArray?, href: String?, mediaType: MediaType?) : this(id, data, href, mediaType, "UTF-8")
    constructor(id: String?, data: ByteArray?, href: String?, originalHref: String?, mediaType: MediaType?) : this(id, data, href, originalHref, mediaType, "UTF-8")
    constructor(id: String?, data: ByteArray?, href: String?, mediaType: MediaType?, inputEncoding: String?) {
        this.id = id; this.href = href; this.originalHref = href; this.mediaType = mediaType; this.inputEncoding = inputEncoding; this.data = data
    }
    constructor(id: String?, data: ByteArray?, href: String?, originalHref: String?, mediaType: MediaType?, inputEncoding: String?) {
        this.id = id; this.href = href; this.originalHref = originalHref; this.mediaType = mediaType; this.inputEncoding = inputEncoding; this.data = data
    }

    @Throws(IOException::class) open fun getInputStream(): InputStream = ByteArrayInputStream(getData())
    @Throws(IOException::class) open fun getData(): ByteArray = data!!
    open fun close() {}
    fun setData(data: ByteArray?) { this.data = data }
    open fun getSize(): Long = data!!.size.toLong()
    fun getTitle(): String? = title
    fun setId(id: String?) { this.id = id }
    fun getId(): String? = id
    fun getHref(): String? = href
    fun setHref(href: String?) { this.href = href }
    fun getInputEncoding(): String? = inputEncoding
    fun setInputEncoding(encoding: String?) { inputEncoding = encoding }
    @Throws(IOException::class) fun getReader(): Reader = XmlStreamReader(ByteArrayInputStream(getData()), inputEncoding)
    override fun hashCode(): Int = href!!.hashCode()
    override fun equals(other: Any?): Boolean = other is Resource && href!!.equals(other.getHref())
    fun getMediaType(): MediaType? = mediaType
    fun setMediaType(mediaType: MediaType?) { this.mediaType = mediaType }
    fun setTitle(title: String?) { this.title = title }
    fun getProperties(): String? = properties
    fun setProperties(properties: String?) { this.properties = properties }
    override fun toString(): String = StringUtil.toString("id", id, "title", title, "encoding", inputEncoding, "mediaType", mediaType, "href", href, "size", data?.size ?: 0)
    companion object { private const val serialVersionUID = 1043946707835004037L }
}
