package me.ag2s.epublib.util.commons.io

import java.io.IOException

class XmlStreamReaderException : IOException {
    private val bomEncoding: String?
    private val xmlGuessEncoding: String?
    private val xmlEncoding: String?
    private val contentTypeMime: String?
    private val contentTypeEncoding: String?
    constructor(message: String, bomEncoding: String?, xmlGuessEncoding: String?, xmlEncoding: String?) : this(message, null, null, bomEncoding, xmlGuessEncoding, xmlEncoding)
    constructor(message: String, contentTypeMime: String?, contentTypeEncoding: String?, bomEncoding: String?, xmlGuessEncoding: String?, xmlEncoding: String?) : super(message) { this.contentTypeMime = contentTypeMime; this.contentTypeEncoding = contentTypeEncoding; this.bomEncoding = bomEncoding; this.xmlGuessEncoding = xmlGuessEncoding; this.xmlEncoding = xmlEncoding }
    fun getBomEncoding(): String? = bomEncoding
    fun getXmlGuessEncoding(): String? = xmlGuessEncoding
    fun getXmlEncoding(): String? = xmlEncoding
    fun getContentTypeMime(): String? = contentTypeMime
    fun getContentTypeEncoding(): String? = contentTypeEncoding
    companion object { private const val serialVersionUID = 1L }
}
