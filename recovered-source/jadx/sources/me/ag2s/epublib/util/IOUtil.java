package me.ag2s.epublib.util;

import java.io.ByteArrayOutputStream;
import java.io.Closeable;
import java.io.EOFException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.Reader;
import java.io.StringWriter;
import java.io.Writer;
import java.net.HttpURLConnection;
import java.net.URLConnection;
import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.nio.channels.ReadableByteChannel;
import java.nio.charset.Charset;
import me.ag2s.epublib.util.commons.io.IOConsumer;

/* JADX INFO: loaded from: reader-pro-classes-3.2.14.jar:me/ag2s/epublib/util/IOUtil.class */
public class IOUtil {
    public static final int EOF = -1;
    private static char[] SKIP_CHAR_BUFFER;
    private static final String TAG = IOUtil.class.getName();
    public static final int DEFAULT_BUFFER_SIZE = 8192;
    private static final byte[] SKIP_BYTE_BUFFER = new byte[DEFAULT_BUFFER_SIZE];

    public static byte[] toByteArray(Reader in, String encoding) throws IOException {
        StringWriter out = new StringWriter();
        copy(in, (Writer) out);
        out.flush();
        return out.toString().getBytes(encoding);
    }

    public static byte[] toByteArray(InputStream in) throws IOException {
        ByteArrayOutputStream result = new ByteArrayOutputStream();
        copy(in, result);
        result.flush();
        return result.toByteArray();
    }

    public static byte[] toByteArray(InputStream in, int size) throws IOException {
        ByteArrayOutputStream result;
        try {
            if (size > 0) {
                result = new ByteArrayOutputStream(size);
            } else {
                result = new ByteArrayOutputStream();
            }
            copy(in, result);
            result.flush();
            return result.toByteArray();
        } catch (OutOfMemoryError e) {
            return null;
        }
    }

    protected static int calcNewNrReadSize(int nrRead, int totalNrNread) {
        if (totalNrNread < 0) {
            return totalNrNread;
        }
        if (totalNrNread > Integer.MAX_VALUE - nrRead) {
            return -1;
        }
        return totalNrNread + nrRead;
    }

    public static void copy(InputStream in, OutputStream result) throws IOException {
        copy(in, result, DEFAULT_BUFFER_SIZE);
    }

    public static long copy(final InputStream input, final OutputStream output, final int bufferSize) throws IOException {
        return copyLarge(input, output, new byte[bufferSize]);
    }

    @Deprecated
    public static void copy(final InputStream input, final Writer output) throws IOException {
        copy(input, output, Charset.defaultCharset());
    }

    public static void copy(final InputStream input, final Writer output, final Charset inputCharset) throws IOException {
        InputStreamReader in = new InputStreamReader(input, inputCharset.name());
        copy((Reader) in, output);
    }

    public static void copy(final InputStream input, final Writer output, final String inputCharsetName) throws IOException {
        copy(input, output, Charset.forName(inputCharsetName));
    }

    public static long copy(final Reader input, final Appendable output) throws IOException {
        return copy(input, output, CharBuffer.allocate(DEFAULT_BUFFER_SIZE));
    }

    public static long copy(final Reader input, final Appendable output, final CharBuffer buffer) throws IOException {
        long j = 0;
        while (true) {
            long count = j;
            int n = input.read(buffer);
            if (-1 != n) {
                buffer.flip();
                output.append(buffer, 0, n);
                j = count + ((long) n);
            } else {
                return count;
            }
        }
    }

    @Deprecated
    public static void copy(final Reader input, final OutputStream output) throws IOException {
        copy(input, output, Charset.defaultCharset());
    }

    public static void copy(final Reader input, final OutputStream output, final Charset outputCharset) throws IOException {
        OutputStreamWriter out = new OutputStreamWriter(output, outputCharset.name());
        copy(input, (Writer) out);
        out.flush();
    }

    public static void copy(final Reader input, final OutputStream output, final String outputCharsetName) throws IOException {
        copy(input, output, Charset.forName(outputCharsetName));
    }

    public static int copy(final Reader input, final Writer output) throws IOException {
        long count = copyLarge(input, output);
        if (count > 2147483647L) {
            return -1;
        }
        return (int) count;
    }

    public static long copyLarge(final InputStream input, final OutputStream output) throws IOException {
        return copy(input, output, DEFAULT_BUFFER_SIZE);
    }

    public static long copyLarge(final InputStream input, final OutputStream output, final byte[] buffer) throws IOException {
        long count = 0;
        if (input != null) {
            while (true) {
                int n = input.read(buffer);
                if (-1 == n) {
                    break;
                }
                output.write(buffer, 0, n);
                count += (long) n;
            }
        }
        return count;
    }

    public static long copyLarge(final InputStream input, final OutputStream output, final long inputOffset, final long length) throws IOException {
        return copyLarge(input, output, inputOffset, length, new byte[DEFAULT_BUFFER_SIZE]);
    }

    public static long copyLarge(final InputStream input, final OutputStream output, final long inputOffset, final long length, final byte[] buffer) throws IOException {
        int read;
        if (inputOffset > 0) {
            skipFully(input, inputOffset);
        }
        if (length == 0) {
            return 0L;
        }
        int bufferLength = buffer.length;
        int bytesToRead = bufferLength;
        if (length > 0 && length < bufferLength) {
            bytesToRead = (int) length;
        }
        long totalRead = 0;
        while (bytesToRead > 0 && -1 != (read = input.read(buffer, 0, bytesToRead))) {
            output.write(buffer, 0, read);
            totalRead += (long) read;
            if (length > 0) {
                bytesToRead = (int) Math.min(length - totalRead, bufferLength);
            }
        }
        return totalRead;
    }

    public static long copyLarge(final Reader input, final Writer output) throws IOException {
        return copyLarge(input, output, new char[DEFAULT_BUFFER_SIZE]);
    }

    public static long copyLarge(final Reader input, final Writer output, final char[] buffer) throws IOException {
        long j = 0;
        while (true) {
            long count = j;
            int n = input.read(buffer);
            if (-1 != n) {
                output.write(buffer, 0, n);
                j = count + ((long) n);
            } else {
                return count;
            }
        }
    }

    public static long copyLarge(final Reader input, final Writer output, final long inputOffset, final long length) throws IOException {
        return copyLarge(input, output, inputOffset, length, new char[DEFAULT_BUFFER_SIZE]);
    }

    public static long copyLarge(final Reader input, final Writer output, final long inputOffset, final long length, final char[] buffer) throws IOException {
        int read;
        if (inputOffset > 0) {
            skipFully(input, inputOffset);
        }
        if (length == 0) {
            return 0L;
        }
        int bytesToRead = buffer.length;
        if (length > 0 && length < buffer.length) {
            bytesToRead = (int) length;
        }
        long totalRead = 0;
        while (bytesToRead > 0 && -1 != (read = input.read(buffer, 0, bytesToRead))) {
            output.write(buffer, 0, read);
            totalRead += (long) read;
            if (length > 0) {
                bytesToRead = (int) Math.min(length - totalRead, buffer.length);
            }
        }
        return totalRead;
    }

    public static long skip(final InputStream input, final long toSkip) throws IOException {
        long remain;
        if (toSkip < 0) {
            throw new IllegalArgumentException("Skip count must be non-negative, actual: " + toSkip);
        }
        long j = toSkip;
        while (true) {
            remain = j;
            if (remain <= 0) {
                break;
            }
            long n = input.read(SKIP_BYTE_BUFFER, 0, (int) Math.min(remain, SKIP_BYTE_BUFFER.length));
            if (n < 0) {
                break;
            }
            j = remain - n;
        }
        return toSkip - remain;
    }

    public static long skip(final ReadableByteChannel input, final long toSkip) throws IOException {
        long remain;
        if (toSkip < 0) {
            throw new IllegalArgumentException("Skip count must be non-negative, actual: " + toSkip);
        }
        ByteBuffer skipByteBuffer = ByteBuffer.allocate((int) Math.min(toSkip, SKIP_BYTE_BUFFER.length));
        long j = toSkip;
        while (true) {
            remain = j;
            if (remain <= 0) {
                break;
            }
            skipByteBuffer.position(0);
            skipByteBuffer.limit((int) Math.min(remain, SKIP_BYTE_BUFFER.length));
            int n = input.read(skipByteBuffer);
            if (n == -1) {
                break;
            }
            j = remain - ((long) n);
        }
        return toSkip - remain;
    }

    public static long skip(final Reader input, final long toSkip) throws IOException {
        long remain;
        if (toSkip < 0) {
            throw new IllegalArgumentException("Skip count must be non-negative, actual: " + toSkip);
        }
        if (SKIP_CHAR_BUFFER == null) {
            SKIP_CHAR_BUFFER = new char[SKIP_BYTE_BUFFER.length];
        }
        long j = toSkip;
        while (true) {
            remain = j;
            if (remain <= 0) {
                break;
            }
            long n = input.read(SKIP_CHAR_BUFFER, 0, (int) Math.min(remain, SKIP_BYTE_BUFFER.length));
            if (n < 0) {
                break;
            }
            j = remain - n;
        }
        return toSkip - remain;
    }

    public static void skipFully(final InputStream input, final long toSkip) throws IOException {
        if (toSkip < 0) {
            throw new IllegalArgumentException("Bytes to skip must not be negative: " + toSkip);
        }
        long skipped = skip(input, toSkip);
        if (skipped != toSkip) {
            throw new EOFException("Bytes to skip: " + toSkip + " actual: " + skipped);
        }
    }

    public static void skipFully(final ReadableByteChannel input, final long toSkip) throws IOException {
        if (toSkip < 0) {
            throw new IllegalArgumentException("Bytes to skip must not be negative: " + toSkip);
        }
        long skipped = skip(input, toSkip);
        if (skipped != toSkip) {
            throw new EOFException("Bytes to skip: " + toSkip + " actual: " + skipped);
        }
    }

    public static void skipFully(final Reader input, final long toSkip) throws IOException {
        long skipped = skip(input, toSkip);
        if (skipped != toSkip) {
            throw new EOFException("Chars to skip: " + toSkip + " actual: " + skipped);
        }
    }

    public static int length(final byte[] array) {
        if (array == null) {
            return 0;
        }
        return array.length;
    }

    public static int length(final char[] array) {
        if (array == null) {
            return 0;
        }
        return array.length;
    }

    public static int length(final CharSequence csq) {
        if (csq == null) {
            return 0;
        }
        return csq.length();
    }

    public static int length(final Object[] array) {
        if (array == null) {
            return 0;
        }
        return array.length;
    }

    public static void close(final Closeable closeable) throws IOException {
        if (closeable != null) {
            closeable.close();
        }
    }

    public static void close(final Closeable... closeables) throws IOException {
        if (closeables != null) {
            for (Closeable closeable : closeables) {
                close(closeable);
            }
        }
    }

    public static void close(final Closeable closeable, final IOConsumer<IOException> consumer) throws IOException {
        if (closeable != null) {
            try {
                closeable.close();
            } catch (IOException e) {
                if (consumer != null) {
                    consumer.accept(e);
                }
            }
        }
    }

    public static void close(final URLConnection conn) {
        if (conn instanceof HttpURLConnection) {
            ((HttpURLConnection) conn).disconnect();
        }
    }

    public static String Stream2String(InputStream inputStream) {
        ByteArrayOutputStream result = new ByteArrayOutputStream();
        try {
            byte[] buffer = new byte[DEFAULT_BUFFER_SIZE];
            while (true) {
                int length = inputStream.read(buffer);
                if (length != -1) {
                    result.write(buffer, 0, length);
                } else {
                    return result.toString();
                }
            }
        } catch (Exception e) {
            return e.getLocalizedMessage();
        }
    }
}
