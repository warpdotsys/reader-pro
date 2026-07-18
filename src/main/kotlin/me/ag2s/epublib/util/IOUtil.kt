package me.ag2s.epublib.util

import java.io.Closeable
import java.io.ByteArrayOutputStream
import java.io.EOFException
import java.io.InputStream
import java.io.InputStreamReader
import java.io.OutputStream
import java.io.OutputStreamWriter
import java.io.Reader
import java.io.StringWriter
import java.io.Writer
import java.net.HttpURLConnection
import java.net.URLConnection
import java.nio.ByteBuffer
import java.nio.CharBuffer
import java.nio.channels.ReadableByteChannel
import java.nio.charset.Charset
import me.ag2s.epublib.util.commons.io.IOConsumer

object IOUtil {
    const val EOF = -1
    const val DEFAULT_BUFFER_SIZE = 8192
    private val skipByteBuffer = ByteArray(DEFAULT_BUFFER_SIZE)
    private var skipCharBuffer: CharArray? = null

    @JvmStatic fun toByteArray(input: Reader, encoding: String): ByteArray = StringWriter().use { output -> copy(input, output); output.flush(); output.toString().toByteArray(Charset.forName(encoding)) }
    @JvmStatic fun toByteArray(input: InputStream): ByteArray = ByteArrayOutputStream().use { output -> copy(input, output); output.flush(); output.toByteArray() }
    @JvmStatic fun toByteArray(input: InputStream, size: Int): ByteArray? = try { ByteArrayOutputStream(if (size > 0) size else 32).use { output -> copy(input, output); output.flush(); output.toByteArray() } } catch (_: OutOfMemoryError) { null }

    @JvmStatic fun calcNewNrReadSize(nrRead: Int, totalNrNread: Int): Int = if (totalNrNread < 0) totalNrNread else if (totalNrNread > Int.MAX_VALUE - nrRead) -1 else totalNrNread + nrRead
    @JvmStatic fun copy(input: InputStream, output: OutputStream) { copy(input, output, DEFAULT_BUFFER_SIZE) }
    @JvmStatic fun copy(input: InputStream, output: OutputStream, bufferSize: Int): Long = copyLarge(input, output, ByteArray(bufferSize))
    @JvmStatic fun copy(input: InputStream, output: Writer) { copy(input, output, Charset.defaultCharset()) }
    @JvmStatic fun copy(input: InputStream, output: Writer, inputCharset: Charset) { copy(InputStreamReader(input, inputCharset.name()), output) }
    @JvmStatic fun copy(input: InputStream, output: Writer, inputCharsetName: String) { copy(input, output, Charset.forName(inputCharsetName)) }
    @JvmStatic fun copy(input: Reader, output: Appendable): Long = copy(input, output, CharBuffer.allocate(DEFAULT_BUFFER_SIZE))
    @JvmStatic fun copy(input: Reader, output: Appendable, buffer: CharBuffer): Long { var count = 0L; var read: Int; while (input.read(buffer).also { read = it } != -1) { buffer.flip(); output.append(buffer, 0, read); count += read }; return count }
    @JvmStatic fun copy(input: Reader, output: OutputStream) { copy(input, output, Charset.defaultCharset()) }
    @JvmStatic fun copy(input: Reader, output: OutputStream, outputCharset: Charset) { val writer = OutputStreamWriter(output, outputCharset.name()); copy(input, writer); writer.flush() }
    @JvmStatic fun copy(input: Reader, output: OutputStream, outputCharsetName: String) { copy(input, output, Charset.forName(outputCharsetName)) }
    @JvmStatic fun copy(input: Reader, output: Writer): Int { val count = copyLarge(input, output); return if (count > Int.MAX_VALUE) -1 else count.toInt() }
    @JvmStatic fun copyLarge(input: InputStream, output: OutputStream): Long = copy(input, output, DEFAULT_BUFFER_SIZE)
    @JvmStatic fun copyLarge(input: InputStream?, output: OutputStream, buffer: ByteArray): Long { var count = 0L; if (input != null) { var read: Int; while (input.read(buffer).also { read = it } != -1) { output.write(buffer, 0, read); count += read } }; return count }
    @JvmStatic fun copyLarge(input: InputStream, output: OutputStream, offset: Long, length: Long): Long = copyLarge(input, output, offset, length, ByteArray(DEFAULT_BUFFER_SIZE))
    @JvmStatic fun copyLarge(input: InputStream, output: OutputStream, offset: Long, length: Long, buffer: ByteArray): Long { if (offset > 0) skipFully(input, offset); if (length == 0L) return 0; var bytesToRead = if (length in 1 until buffer.size.toLong()) length.toInt() else buffer.size; var total = 0L; while (bytesToRead > 0) { val read = input.read(buffer, 0, bytesToRead); if (read == -1) break; output.write(buffer, 0, read); total += read; if (length > 0) bytesToRead = minOf(length - total, buffer.size.toLong()).toInt() }; return total }
    @JvmStatic fun copyLarge(input: Reader, output: Writer): Long = copyLarge(input, output, CharArray(DEFAULT_BUFFER_SIZE))
    @JvmStatic fun copyLarge(input: Reader, output: Writer, buffer: CharArray): Long { var count = 0L; var read: Int; while (input.read(buffer).also { read = it } != -1) { output.write(buffer, 0, read); count += read }; return count }
    @JvmStatic fun copyLarge(input: Reader, output: Writer, offset: Long, length: Long): Long = copyLarge(input, output, offset, length, CharArray(DEFAULT_BUFFER_SIZE))
    @JvmStatic fun copyLarge(input: Reader, output: Writer, offset: Long, length: Long, buffer: CharArray): Long { if (offset > 0) skipFully(input, offset); if (length == 0L) return 0; var bytesToRead = if (length in 1 until buffer.size.toLong()) length.toInt() else buffer.size; var total = 0L; while (bytesToRead > 0) { val read = input.read(buffer, 0, bytesToRead); if (read == -1) break; output.write(buffer, 0, read); total += read; if (length > 0) bytesToRead = minOf(length - total, buffer.size.toLong()).toInt() }; return total }
    @JvmStatic fun skip(input: InputStream, toSkip: Long): Long { require(toSkip >= 0) { "Skip count must be non-negative, actual: $toSkip" }; var remaining = toSkip; while (remaining > 0) { val read = input.read(skipByteBuffer, 0, minOf(remaining, skipByteBuffer.size.toLong()).toInt()); if (read < 0) break; remaining -= read }; return toSkip - remaining }
    @JvmStatic fun skip(input: ReadableByteChannel, toSkip: Long): Long { require(toSkip >= 0) { "Skip count must be non-negative, actual: $toSkip" }; val buffer = ByteBuffer.allocate(minOf(toSkip, skipByteBuffer.size.toLong()).toInt()); var remaining = toSkip; while (remaining > 0) { buffer.position(0); buffer.limit(minOf(remaining, skipByteBuffer.size.toLong()).toInt()); val read = input.read(buffer); if (read == -1) break; remaining -= read }; return toSkip - remaining }
    @JvmStatic fun skip(input: Reader, toSkip: Long): Long { require(toSkip >= 0) { "Skip count must be non-negative, actual: $toSkip" }; val buffer = skipCharBuffer ?: CharArray(skipByteBuffer.size).also { skipCharBuffer = it }; var remaining = toSkip; while (remaining > 0) { val read = input.read(buffer, 0, minOf(remaining, buffer.size.toLong()).toInt()); if (read < 0) break; remaining -= read }; return toSkip - remaining }
    @JvmStatic fun skipFully(input: InputStream, toSkip: Long) { require(toSkip >= 0) { "Bytes to skip must not be negative: $toSkip" }; val skipped = skip(input, toSkip); if (skipped != toSkip) throw EOFException("Bytes to skip: $toSkip actual: $skipped") }
    @JvmStatic fun skipFully(input: ReadableByteChannel, toSkip: Long) { require(toSkip >= 0) { "Bytes to skip must not be negative: $toSkip" }; val skipped = skip(input, toSkip); if (skipped != toSkip) throw EOFException("Bytes to skip: $toSkip actual: $skipped") }
    @JvmStatic fun skipFully(input: Reader, toSkip: Long) { val skipped = skip(input, toSkip); if (skipped != toSkip) throw EOFException("Chars to skip: $toSkip actual: $skipped") }
    @JvmStatic fun length(array: ByteArray?): Int = array?.size ?: 0
    @JvmStatic fun length(array: CharArray?): Int = array?.size ?: 0
    @JvmStatic fun length(csq: CharSequence?): Int = csq?.length ?: 0
    @JvmStatic fun length(array: Array<out Any?>?): Int = array?.size ?: 0
    @JvmStatic fun close(closeable: Closeable?) { closeable?.close() }
    @JvmStatic fun close(vararg closeables: Closeable?) { closeables.forEach { close(it) } }
    @JvmStatic fun close(closeable: Closeable?, consumer: IOConsumer<java.io.IOException>?) { if (closeable != null) try { closeable.close() } catch (e: java.io.IOException) { consumer?.accept(e) } }
    @JvmStatic fun close(conn: URLConnection) { if (conn is HttpURLConnection) conn.disconnect() }
    @JvmStatic fun Stream2String(inputStream: InputStream): String = try { ByteArrayOutputStream().use { output -> val buffer = ByteArray(DEFAULT_BUFFER_SIZE); var length: Int; while (inputStream.read(buffer).also { length = it } != -1) output.write(buffer, 0, length); output.toString() } } catch (e: Exception) { e.localizedMessage }
}
