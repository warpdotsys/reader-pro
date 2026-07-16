//
// Decompiled by Procyon v0.6.0
//

package me.ag2s.epublib.util;

import java.net.HttpURLConnection;
import java.net.URLConnection;
import me.ag2s.epublib.util.commons.io.IOConsumer;
import java.io.Closeable;
import java.io.EOFException;
import java.nio.ByteBuffer;
import java.nio.channels.ReadableByteChannel;
import java.io.OutputStreamWriter;
import java.nio.CharBuffer;
import java.io.InputStreamReader;
import java.nio.charset.Charset;
import java.io.OutputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.io.IOException;
import java.io.Writer;
import java.io.StringWriter;
import java.io.Reader;

public class IOUtil
{
    private static final String TAG;
    public static final int EOF = -1;
    public static final int DEFAULT_BUFFER_SIZE = 8192;
    private static final byte[] SKIP_BYTE_BUFFER;
    private static char[] SKIP_CHAR_BUFFER;

    public static byte[] toByteArray(final Reader in, final String encoding) throws IOException {
        final StringWriter out = new StringWriter();
        copy(in, out);
        out.flush();
        return out.toString().getBytes(encoding);
    }

    public static byte[] toByteArray(final InputStream in) throws IOException {
        final ByteArrayOutputStream result = new ByteArrayOutputStream();
        copy(in, result);
        result.flush();
        return result.toByteArray();
    }

    public static byte[] toByteArray(final InputStream in, final int size) throws IOException {
        try {
            ByteArrayOutputStream result;
            if (size > 0) {
                result = new ByteArrayOutputStream(size);
            }
            else {
                result = new ByteArrayOutputStream();
            }
            copy(in, result);
            result.flush();
            return result.toByteArray();
        }
        catch (final OutOfMemoryError error) {
            return null;
        }
    }

    protected static int calcNewNrReadSize(final int nrRead, final int totalNrNread) {
        if (totalNrNread < 0) {
            return totalNrNread;
        }
        if (totalNrNread > Integer.MAX_VALUE - nrRead) {
            return -1;
        }
        return totalNrNread + nrRead;
    }

    public static void copy(final InputStream in, final OutputStream result) throws IOException {
        copy(in, result, 8192);
    }

    public static long copy(final InputStream input, final OutputStream output, final int bufferSize) throws IOException {
        return copyLarge(input, output, new byte[bufferSize]);
    }

    @Deprecated
    public static void copy(final InputStream input, final Writer output) throws IOException {
        copy(input, output, Charset.defaultCharset());
    }

    public static void copy(final InputStream input, final Writer output, final Charset inputCharset) throws IOException {
        final InputStreamReader in = new InputStreamReader(input, inputCharset.name());
        copy(in, output);
    }

    public static void copy(final InputStream input, final Writer output, final String inputCharsetName) throws IOException {
        copy(input, output, Charset.forName(inputCharsetName));
    }

    public static long copy(final Reader input, final Appendable output) throws IOException {
        return copy(input, output, CharBuffer.allocate(8192));
    }

    public static long copy(final Reader input, final Appendable output, final CharBuffer buffer) throws IOException {
        long count = 0L;
        int n;
        while (-1 != (n = input.read(buffer))) {
            buffer.flip();
            output.append(buffer, 0, n);
            count += n;
        }
        return count;
    }

    @Deprecated
    public static void copy(final Reader input, final OutputStream output) throws IOException {
        copy(input, output, Charset.defaultCharset());
    }

    public static void copy(final Reader input, final OutputStream output, final Charset outputCharset) throws IOException {
        final OutputStreamWriter out = new OutputStreamWriter(output, outputCharset.name());
        copy(input, out);
        out.flush();
    }

    public static void copy(final Reader input, final OutputStream output, final String outputCharsetName) throws IOException {
        copy(input, output, Charset.forName(outputCharsetName));
    }

    public static int copy(final Reader input, final Writer output) throws IOException {
        final long count = copyLarge(input, output);
        if (count > 2147483647L) {
            return -1;
        }
        return (int)count;
    }

    public static long copyLarge(final InputStream input, final OutputStream output) throws IOException {
        return copy(input, output, 8192);
    }

    public static long copyLarge(final InputStream input, final OutputStream output, final byte[] buffer) throws IOException {
        long count = 0L;
        if (input != null) {
            int n;
            while (-1 != (n = input.read(buffer))) {
                output.write(buffer, 0, n);
                count += n;
            }
        }
        return count;
    }

    public static long copyLarge(final InputStream input, final OutputStream output, final long inputOffset, final long length) throws IOException {
        return copyLarge(input, output, inputOffset, length, new byte[8192]);
    }

    public static long copyLarge(final InputStream input, final OutputStream output, final long inputOffset, final long length, final byte[] buffer) throws IOException {
        if (inputOffset > 0L) {
            skipFully(input, inputOffset);
        }
        if (length == 0L) {
            return 0L;
        }
        int bytesToRead;
        final int bufferLength = bytesToRead = buffer.length;
        if (length > 0L && length < bufferLength) {
            bytesToRead = (int)length;
        }
        long totalRead;
        int read;
        for (totalRead = 0L; bytesToRead > 0 && -1 != (read = input.read(buffer, 0, bytesToRead)); bytesToRead = (int)Math.min(length - totalRead, bufferLength)) {
            output.write(buffer, 0, read);
            totalRead += read;
            if (length > 0L) {}
        }
        return totalRead;
    }

    public static long copyLarge(final Reader input, final Writer output) throws IOException {
        return copyLarge(input, output, new char[8192]);
    }

    public static long copyLarge(final Reader input, final Writer output, final char[] buffer) throws IOException {
        long count = 0L;
        int n;
        while (-1 != (n = input.read(buffer))) {
            output.write(buffer, 0, n);
            count += n;
        }
        return count;
    }

    public static long copyLarge(final Reader input, final Writer output, final long inputOffset, final long length) throws IOException {
        return copyLarge(input, output, inputOffset, length, new char[8192]);
    }

    public static long copyLarge(final Reader input, final Writer output, final long inputOffset, final long length, final char[] buffer) throws IOException {
        if (inputOffset > 0L) {
            skipFully(input, inputOffset);
        }
        if (length == 0L) {
            return 0L;
        }
        int bytesToRead = buffer.length;
        if (length > 0L && length < buffer.length) {
            bytesToRead = (int)length;
        }
        long totalRead;
        int read;
        for (totalRead = 0L; bytesToRead > 0 && -1 != (read = input.read(buffer, 0, bytesToRead)); bytesToRead = (int)Math.min(length - totalRead, buffer.length)) {
            output.write(buffer, 0, read);
            totalRead += read;
            if (length > 0L) {}
        }
        return totalRead;
    }

    public static long skip(final InputStream input, final long toSkip) throws IOException {
        if (toSkip < 0L) {
            throw new IllegalArgumentException("Skip count must be non-negative, actual: " + toSkip);
        }
        long remain;
        long n;
        for (remain = toSkip; remain > 0L; remain -= n) {
            n = input.read(IOUtil.SKIP_BYTE_BUFFER, 0, (int)Math.min(remain, IOUtil.SKIP_BYTE_BUFFER.length));
            if (n < 0L) {
                break;
            }
        }
        return toSkip - remain;
    }

    public static long skip(final ReadableByteChannel input, final long toSkip) throws IOException {
        if (toSkip < 0L) {
            throw new IllegalArgumentException("Skip count must be non-negative, actual: " + toSkip);
        }
        final ByteBuffer skipByteBuffer = ByteBuffer.allocate((int)Math.min(toSkip, IOUtil.SKIP_BYTE_BUFFER.length));
        long remain;
        int n;
        for (remain = toSkip; remain > 0L; remain -= n) {
            skipByteBuffer.position(0);
            skipByteBuffer.limit((int)Math.min(remain, IOUtil.SKIP_BYTE_BUFFER.length));
            n = input.read(skipByteBuffer);
            if (n == -1) {
                break;
            }
        }
        return toSkip - remain;
    }

    public static long skip(final Reader input, final long toSkip) throws IOException {
        if (toSkip < 0L) {
            throw new IllegalArgumentException("Skip count must be non-negative, actual: " + toSkip);
        }
        if (IOUtil.SKIP_CHAR_BUFFER == null) {
            IOUtil.SKIP_CHAR_BUFFER = new char[IOUtil.SKIP_BYTE_BUFFER.length];
        }
        long remain;
        long n;
        for (remain = toSkip; remain > 0L; remain -= n) {
            n = input.read(IOUtil.SKIP_CHAR_BUFFER, 0, (int)Math.min(remain, IOUtil.SKIP_BYTE_BUFFER.length));
            if (n < 0L) {
                break;
            }
        }
        return toSkip - remain;
    }

    public static void skipFully(final InputStream input, final long toSkip) throws IOException {
        if (toSkip < 0L) {
            throw new IllegalArgumentException("Bytes to skip must not be negative: " + toSkip);
        }
        final long skipped = skip(input, toSkip);
        if (skipped != toSkip) {
            throw new EOFException("Bytes to skip: " + toSkip + " actual: " + skipped);
        }
    }

    public static void skipFully(final ReadableByteChannel input, final long toSkip) throws IOException {
        if (toSkip < 0L) {
            throw new IllegalArgumentException("Bytes to skip must not be negative: " + toSkip);
        }
        final long skipped = skip(input, toSkip);
        if (skipped != toSkip) {
            throw new EOFException("Bytes to skip: " + toSkip + " actual: " + skipped);
        }
    }

    public static void skipFully(final Reader input, final long toSkip) throws IOException {
        final long skipped = skip(input, toSkip);
        if (skipped != toSkip) {
            throw new EOFException("Chars to skip: " + toSkip + " actual: " + skipped);
        }
    }

    public static int length(final byte[] array) {
        return (array == null) ? 0 : array.length;
    }

    public static int length(final char[] array) {
        return (array == null) ? 0 : array.length;
    }

    public static int length(final CharSequence csq) {
        return (csq == null) ? 0 : csq.length();
    }

    public static int length(final Object[] array) {
        return (array == null) ? 0 : array.length;
    }

    public static void close(final Closeable closeable) throws IOException {
        if (closeable != null) {
            closeable.close();
        }
    }

    public static void close(final Closeable... closeables) throws IOException {
        if (closeables != null) {
            for (final Closeable closeable : closeables) {
                close(closeable);
            }
        }
    }

    public static void close(final Closeable closeable, final IOConsumer<IOException> consumer) throws IOException {
        if (closeable != null) {
            try {
                closeable.close();
            }
            catch (final IOException e) {
                if (consumer != null) {
                    consumer.accept(e);
                }
            }
        }
    }

    public static void close(final URLConnection conn) {
        if (conn instanceof HttpURLConnection) {
            ((HttpURLConnection)conn).disconnect();
        }
    }

    public static String Stream2String(final InputStream inputStream) {
        final ByteArrayOutputStream result = new ByteArrayOutputStream();
        try {
            final byte[] buffer = new byte[8192];
            int length;
            while ((length = inputStream.read(buffer)) != -1) {
                result.write(buffer, 0, length);
            }
            return result.toString();
        }
        catch (final Exception e) {
            return e.getLocalizedMessage();
        }
    }

    static {
        TAG = IOUtil.class.getName();
        SKIP_BYTE_BUFFER = new byte[8192];
    }
}
