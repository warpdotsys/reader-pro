package me.ag2s.epublib.util.commons.io

import java.io.BufferedInputStream
import java.io.BufferedReader
import java.io.File
import java.io.FileInputStream
import java.io.IOException
import java.io.InputStream
import java.io.InputStreamReader
import java.io.Reader
import java.io.StringReader
import java.net.HttpURLConnection
import java.net.URL
import java.net.URLConnection
import java.text.MessageFormat
import java.util.Locale
import java.util.Objects
import java.util.regex.Pattern

class XmlStreamReader : Reader {
    private val reader: Reader
    private val encoding: String
    private val defaultEncoding: String?
    fun getDefaultEncoding(): String? = defaultEncoding
    @Throws(IOException::class) constructor(file: File) : this(FileInputStream(Objects.requireNonNull(file)))
    @Throws(IOException::class) constructor(inputStream: InputStream) : this(inputStream, true)
    @Throws(IOException::class) constructor(inputStream: InputStream, lenient: Boolean) : this(inputStream, lenient, null)
    @Throws(IOException::class) constructor(inputStream: InputStream, lenient: Boolean, defaultEncoding: String?) {
        Objects.requireNonNull(inputStream, "inputStream")
        this.defaultEncoding = defaultEncoding
        val bom = BOMInputStream(BufferedInputStream(inputStream, BUFFER_SIZE), false, *BOMS)
        val pis = BOMInputStream(bom, true, *XML_GUESS_BYTES)
        encoding = doRawStream(bom, pis, lenient)
        reader = InputStreamReader(pis, encoding)
    }
    @Throws(IOException::class) constructor(url: URL) : this(Objects.requireNonNull(url, "url").openConnection(), null)
    @Throws(IOException::class) constructor(connection: URLConnection, defaultEncoding: String?) {
        Objects.requireNonNull(connection, "conm")
        this.defaultEncoding = defaultEncoding
        val contentType = connection.contentType
        val bom = BOMInputStream(BufferedInputStream(connection.inputStream, BUFFER_SIZE), false, *BOMS)
        val pis = BOMInputStream(bom, true, *XML_GUESS_BYTES)
        encoding = if (connection !is HttpURLConnection && contentType == null) doRawStream(bom, pis, true) else processHttpStream(bom, pis, contentType, true)
        reader = InputStreamReader(pis, encoding)
    }
    @Throws(IOException::class) constructor(inputStream: InputStream, httpContentType: String?) : this(inputStream, httpContentType, true)
    @Throws(IOException::class) constructor(inputStream: InputStream, httpContentType: String?, lenient: Boolean) : this(inputStream, httpContentType, lenient, null)
    @Throws(IOException::class) constructor(inputStream: InputStream, httpContentType: String?, lenient: Boolean, defaultEncoding: String?) {
        Objects.requireNonNull(inputStream, "inputStream")
        this.defaultEncoding = defaultEncoding
        val bom = BOMInputStream(BufferedInputStream(inputStream, BUFFER_SIZE), false, *BOMS)
        val pis = BOMInputStream(bom, true, *XML_GUESS_BYTES)
        encoding = processHttpStream(bom, pis, httpContentType, lenient)
        reader = InputStreamReader(pis, encoding)
    }
    fun getEncoding(): String = encoding
    @Throws(IOException::class) override fun read(buffer: CharArray, offset: Int, length: Int): Int = reader.read(buffer, offset, length)
    @Throws(IOException::class) override fun close() { reader.close() }

    @Throws(IOException::class) private fun doRawStream(bom: BOMInputStream, pis: BOMInputStream, lenient: Boolean): String {
        val bomEncoding = bom.getBOMCharsetName(); val guessEncoding = pis.getBOMCharsetName(); val xmlEncoding = getXmlProlog(pis, guessEncoding)
        return try { calculateRawEncoding(bomEncoding, guessEncoding, xmlEncoding) } catch (exception: XmlStreamReaderException) { if (lenient) doLenientDetection(null, exception) else throw exception }
    }
    @Throws(IOException::class) private fun processHttpStream(bom: BOMInputStream, pis: BOMInputStream, httpContentType: String?, lenient: Boolean): String {
        val bomEncoding = bom.getBOMCharsetName(); val guessEncoding = pis.getBOMCharsetName(); val xmlEncoding = getXmlProlog(pis, guessEncoding)
        return try { calculateHttpEncoding(httpContentType, bomEncoding, guessEncoding, xmlEncoding, lenient) } catch (exception: XmlStreamReaderException) { if (lenient) doLenientDetection(httpContentType, exception) else throw exception }
    }
    @Throws(IOException::class) private fun doLenientDetection(httpContentType: String?, initial: XmlStreamReaderException): String {
        var contentType = httpContentType; var exception = initial
        if (contentType != null && contentType.startsWith("text/html")) {
            contentType = "text/xml" + contentType.substring("text/html".length)
            try { return calculateHttpEncoding(contentType, exception.getBomEncoding(), exception.getXmlGuessEncoding(), exception.getXmlEncoding(), true) } catch (other: XmlStreamReaderException) { exception = other }
        }
        return exception.getXmlEncoding() ?: exception.getContentTypeEncoding() ?: defaultEncoding ?: UTF_8
    }
    @Throws(IOException::class) internal fun calculateRawEncoding(bomEncoding: String?, xmlGuessEncoding: String?, xmlEncoding: String?): String {
        if (bomEncoding != null) {
            if (bomEncoding == UTF_8) {
                if (xmlGuessEncoding != null && xmlGuessEncoding != UTF_8 || xmlEncoding != null && xmlEncoding != UTF_8) throw rawException(RAW_EX_1, bomEncoding, xmlGuessEncoding, xmlEncoding)
                return bomEncoding
            }
            if (bomEncoding == UTF_16BE || bomEncoding == UTF_16LE) {
                if (xmlGuessEncoding != null && xmlGuessEncoding != bomEncoding || xmlEncoding != null && xmlEncoding != UTF_16 && xmlEncoding != bomEncoding) throw rawException(RAW_EX_1, bomEncoding, xmlGuessEncoding, xmlEncoding)
                return bomEncoding
            }
            if (bomEncoding == UTF_32BE || bomEncoding == UTF_32LE) {
                if (xmlGuessEncoding != null && xmlGuessEncoding != bomEncoding || xmlEncoding != null && xmlEncoding != UTF_32 && xmlEncoding != bomEncoding) throw rawException(RAW_EX_1, bomEncoding, xmlGuessEncoding, xmlEncoding)
                return bomEncoding
            }
            throw rawException(RAW_EX_2, bomEncoding, xmlGuessEncoding, xmlEncoding)
        }
        return if (xmlGuessEncoding != null && xmlEncoding != null) if (xmlEncoding == UTF_16 && (xmlGuessEncoding == UTF_16BE || xmlGuessEncoding == UTF_16LE)) xmlGuessEncoding else xmlEncoding else defaultEncoding ?: UTF_8
    }
    @Throws(IOException::class) internal fun calculateHttpEncoding(httpContentType: String?, bomEncoding: String?, xmlGuessEncoding: String?, xmlEncoding: String?, lenient: Boolean): String {
        if (lenient && xmlEncoding != null) return xmlEncoding
        val mime = getContentTypeMime(httpContentType); val contentEncoding = getContentTypeEncoding(httpContentType); val appXml = isAppXml(mime); val textXml = isTextXml(mime)
        if (!appXml && !textXml) throw httpException(HTTP_EX_3, mime, contentEncoding, bomEncoding, xmlGuessEncoding, xmlEncoding)
        if (contentEncoding == null) return if (appXml) calculateRawEncoding(bomEncoding, xmlGuessEncoding, xmlEncoding) else defaultEncoding ?: US_ASCII
        if (contentEncoding == UTF_16BE || contentEncoding == UTF_16LE || contentEncoding == UTF_32BE || contentEncoding == UTF_32LE) { if (bomEncoding != null) throw httpException(HTTP_EX_1, mime, contentEncoding, bomEncoding, xmlGuessEncoding, xmlEncoding); return contentEncoding }
        if (contentEncoding == UTF_16) { if (bomEncoding != null && bomEncoding.startsWith(UTF_16)) return bomEncoding; throw httpException(HTTP_EX_2, mime, contentEncoding, bomEncoding, xmlGuessEncoding, xmlEncoding) }
        if (contentEncoding == UTF_32) { if (bomEncoding != null && bomEncoding.startsWith(UTF_32)) return bomEncoding; throw httpException(HTTP_EX_2, mime, contentEncoding, bomEncoding, xmlGuessEncoding, xmlEncoding) }
        return contentEncoding
    }
    private fun rawException(template: String, bom: String?, guess: String?, xml: String?): XmlStreamReaderException = XmlStreamReaderException(MessageFormat.format(template, bom, guess, xml), bom, guess, xml)
    private fun httpException(template: String, mime: String?, content: String?, bom: String?, guess: String?, xml: String?): XmlStreamReaderException = XmlStreamReaderException(MessageFormat.format(template, mime, content, bom, guess, xml), mime, content, bom, guess, xml)

    companion object {
        private const val BUFFER_SIZE = 8192
        private const val UTF_8 = "UTF-8"; private const val US_ASCII = "US-ASCII"; private const val UTF_16BE = "UTF-16BE"; private const val UTF_16LE = "UTF-16LE"; private const val UTF_32BE = "UTF-32BE"; private const val UTF_32LE = "UTF-32LE"; private const val UTF_16 = "UTF-16"; private const val UTF_32 = "UTF-32"
        private val BOMS = arrayOf(ByteOrderMark.UTF_8, ByteOrderMark.UTF_16BE, ByteOrderMark.UTF_16LE, ByteOrderMark.UTF_32BE, ByteOrderMark.UTF_32LE)
        private val XML_GUESS_BYTES = arrayOf(ByteOrderMark("UTF-8", 60, 63, 120, 109), ByteOrderMark(UTF_16BE, 0, 60, 0, 63), ByteOrderMark(UTF_16LE, 60, 0, 63, 0), ByteOrderMark(UTF_32BE, 0, 0, 0, 60, 0, 0, 0, 63, 0, 0, 0, 120, 0, 0, 0, 109), ByteOrderMark(UTF_32LE, 60, 0, 0, 0, 63, 0, 0, 0, 120, 0, 0, 0, 109, 0, 0, 0), ByteOrderMark("CP1047", 76, 111, 167, 148))
        private val charsetPattern = Pattern.compile("charset=[\"']?([.[^; \"']]*)[\"']?")
        @JvmField val ENCODING_PATTERN: Pattern = Pattern.compile("<\\?xml.*encoding[\\s]*=[\\s]*((?:\".[^\"]*\")|(?:'.[^']*'))", Pattern.CASE_INSENSITIVE)
        private const val RAW_EX_1 = "Invalid encoding, BOM [{0}] XML guess [{1}] XML prolog [{2}] encoding mismatch"
        private const val RAW_EX_2 = "Invalid encoding, BOM [{0}] XML guess [{1}] XML prolog [{2}] unknown BOM"
        private const val HTTP_EX_1 = "Invalid encoding, CT-MIME [{0}] CT-Enc [{1}] BOM [{2}] XML guess [{3}] XML prolog [{4}], BOM must be NULL"
        private const val HTTP_EX_2 = "Invalid encoding, CT-MIME [{0}] CT-Enc [{1}] BOM [{2}] XML guess [{3}] XML prolog [{4}], encoding mismatch"
        private const val HTTP_EX_3 = "Invalid encoding, CT-MIME [{0}] CT-Enc [{1}] BOM [{2}] XML guess [{3}] XML prolog [{4}], Invalid MIME"
        @JvmStatic internal fun getContentTypeMime(contentType: String?): String? = contentType?.let { it.substringBefore(';').trim() }
        @JvmStatic internal fun getContentTypeEncoding(contentType: String?): String? { val index = contentType?.indexOf(';') ?: -1; return if (index > -1) charsetPattern.matcher(contentType!!.substring(index + 1)).let { if (it.find()) it.group(1)?.uppercase(Locale.ROOT) else null } else null }
        @Throws(IOException::class) private fun getXmlProlog(input: InputStream, guessedEncoding: String?): String? {
            if (guessedEncoding == null) return null
            val bytes = ByteArray(BUFFER_SIZE); input.mark(BUFFER_SIZE); var offset = 0; var maximum = BUFFER_SIZE; var count = input.read(bytes, offset, maximum); var firstGt = -1; var prolog = ""
            while (count != -1 && firstGt == -1 && offset < BUFFER_SIZE) { offset += count; maximum -= count; count = input.read(bytes, offset, maximum); prolog = String(bytes, 0, offset, charset(guessedEncoding)); firstGt = prolog.indexOf('>') }
            if (firstGt == -1) { if (count == -1) throw IOException("Unexpected end of XML stream") else throw IOException("XML prolog or ROOT element not found on first $offset bytes") }
            if (offset == 0) return null
            input.reset(); val joined = BufferedReader(StringReader(prolog.substring(0, firstGt + 1))).readLines().joinToString(""); val matcher = ENCODING_PATTERN.matcher(joined); return if (matcher.find()) matcher.group(1).uppercase(Locale.ROOT).drop(1).dropLast(1) else null
        }
        private fun charset(name: String) = java.nio.charset.Charset.forName(name)
        @JvmStatic internal fun isAppXml(mime: String?): Boolean = mime != null && (mime == "application/xml" || mime == "application/xml-dtd" || mime == "application/xml-external-parsed-entity" || mime.startsWith("application/") && mime.endsWith("+xml"))
        @JvmStatic internal fun isTextXml(mime: String?): Boolean = mime != null && (mime == "text/xml" || mime == "text/xml-external-parsed-entity" || mime.startsWith("text/") && mime.endsWith("+xml"))
    }
}
