package me.ag2s.epublib.util.commons.io

import java.io.FilterInputStream
import java.io.IOException
import java.io.InputStream
import me.ag2s.epublib.util.IOUtil

abstract class ProxyInputStream(proxy: InputStream) : FilterInputStream(proxy) {
    @Throws(IOException::class)
    override fun read(): Int = try { beforeRead(1); val value = `in`.read(); afterRead(if (value != -1) 1 else -1); value } catch (e: IOException) { handleIOException(e); -1 }
    @Throws(IOException::class)
    override fun read(bytes: ByteArray): Int = try { beforeRead(IOUtil.length(bytes)); val value = `in`.read(bytes); afterRead(value); value } catch (e: IOException) { handleIOException(e); -1 }
    @Throws(IOException::class)
    override fun read(bytes: ByteArray, offset: Int, length: Int): Int = try { beforeRead(length); val value = `in`.read(bytes, offset, length); afterRead(value); value } catch (e: IOException) { handleIOException(e); -1 }
    @Throws(IOException::class)
    override fun skip(length: Long): Long = try { `in`.skip(length) } catch (e: IOException) { handleIOException(e); 0L }
    @Throws(IOException::class)
    override fun available(): Int = try { super.available() } catch (e: IOException) { handleIOException(e); 0 }
    @Throws(IOException::class)
    override fun close() { IOUtil.close(`in`, IOConsumer { exception -> handleIOException(exception) }) }
    override fun mark(readlimit: Int) { `in`.mark(readlimit) }
    @Throws(IOException::class)
    override fun reset() { try { `in`.reset() } catch (e: IOException) { handleIOException(e) } }
    override fun markSupported(): Boolean = `in`.markSupported()
    protected open fun beforeRead(length: Int) {}
    protected open fun afterRead(length: Int) {}
    @Throws(IOException::class)
    protected open fun handleIOException(exception: IOException) { throw exception }
}
