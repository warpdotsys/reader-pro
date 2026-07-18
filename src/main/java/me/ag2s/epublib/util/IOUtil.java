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

public class IOUtil {
   private static final String TAG = IOUtil.class.getName();
   public static final int EOF = -1;
   public static final int DEFAULT_BUFFER_SIZE = 8192;
   private static final byte[] SKIP_BYTE_BUFFER = new byte[8192];
   private static char[] SKIP_CHAR_BUFFER;

   public static byte[] toByteArray(Reader in, String encoding) throws IOException {
      StringWriter out = new StringWriter();
      copy((Reader)in, (Writer)out);
      out.flush();
      return out.toString().getBytes(encoding);
   }

   public static byte[] toByteArray(InputStream in) throws IOException {
      ByteArrayOutputStream result = new ByteArrayOutputStream();
      copy((InputStream)in, (OutputStream)result);
      result.flush();
      return result.toByteArray();
   }

   public static byte[] toByteArray(InputStream in, int size) throws IOException {
      try {
         ByteArrayOutputStream result;
         if (size > 0) {
            result = new ByteArrayOutputStream(size);
         } else {
            result = new ByteArrayOutputStream();
         }

         copy((InputStream)in, (OutputStream)result);
         result.flush();
         return result.toByteArray();
      } catch (OutOfMemoryError var3) {
         return null;
      }
   }

   protected static int calcNewNrReadSize(int nrRead, int totalNrNread) {
      if (totalNrNread < 0) {
         return totalNrNread;
      } else {
         return totalNrNread > Integer.MAX_VALUE - nrRead ? -1 : totalNrNread + nrRead;
      }
   }

   public static void copy(InputStream in, OutputStream result) throws IOException {
      copy(in, result, 8192);
   }

   public static long copy(final InputStream input, final OutputStream output, final int bufferSize) throws IOException {
      return copyLarge(input, output, new byte[bufferSize]);
   }

   /** @deprecated */
   @Deprecated
   public static void copy(final InputStream input, final Writer output) throws IOException {
      copy(input, output, Charset.defaultCharset());
   }

   public static void copy(final InputStream input, final Writer output, final Charset inputCharset) throws IOException {
      InputStreamReader in = new InputStreamReader(input, inputCharset.name());
      copy((Reader)in, (Writer)output);
   }

   public static void copy(final InputStream input, final Writer output, final String inputCharsetName) throws IOException {
      copy(input, output, Charset.forName(inputCharsetName));
   }

   public static long copy(final Reader input, final Appendable output) throws IOException {
      return copy(input, output, CharBuffer.allocate(8192));
   }

   public static long copy(final Reader input, final Appendable output, final CharBuffer buffer) throws IOException {
      long count;
      int n;
      for(count = 0L; -1 != (n = input.read(buffer)); count += (long)n) {
         buffer.flip();
         output.append(buffer, 0, n);
      }

      return count;
   }

   /** @deprecated */
   @Deprecated
   public static void copy(final Reader input, final OutputStream output) throws IOException {
      copy(input, output, Charset.defaultCharset());
   }

   public static void copy(final Reader input, final OutputStream output, final Charset outputCharset) throws IOException {
      OutputStreamWriter out = new OutputStreamWriter(output, outputCharset.name());
      copy((Reader)input, (Writer)out);
      out.flush();
   }

   public static void copy(final Reader input, final OutputStream output, final String outputCharsetName) throws IOException {
      copy(input, output, Charset.forName(outputCharsetName));
   }

   public static int copy(final Reader input, final Writer output) throws IOException {
      long count = copyLarge(input, output);
      return count > 2147483647L ? -1 : (int)count;
   }

   public static long copyLarge(final InputStream input, final OutputStream output) throws IOException {
      return copy(input, output, 8192);
   }

   public static long copyLarge(final InputStream input, final OutputStream output, final byte[] buffer) throws IOException {
      long count = 0L;
      int n;
      if (input != null) {
         while(-1 != (n = input.read(buffer))) {
            output.write(buffer, 0, n);
            count += (long)n;
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
      } else {
         int bufferLength = buffer.length;
         int bytesToRead = bufferLength;
         if (length > 0L && length < (long)bufferLength) {
            bytesToRead = (int)length;
         }

         long totalRead = 0L;

         int read;
         while(bytesToRead > 0 && -1 != (read = input.read(buffer, 0, bytesToRead))) {
            output.write(buffer, 0, read);
            totalRead += (long)read;
            if (length > 0L) {
               bytesToRead = (int)Math.min(length - totalRead, (long)bufferLength);
            }
         }

         return totalRead;
      }
   }

   public static long copyLarge(final Reader input, final Writer output) throws IOException {
      return copyLarge(input, output, new char[8192]);
   }

   public static long copyLarge(final Reader input, final Writer output, final char[] buffer) throws IOException {
      long count;
      int n;
      for(count = 0L; -1 != (n = input.read(buffer)); count += (long)n) {
         output.write(buffer, 0, n);
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
      } else {
         int bytesToRead = buffer.length;
         if (length > 0L && length < (long)buffer.length) {
            bytesToRead = (int)length;
         }

         long totalRead = 0L;

         int read;
         while(bytesToRead > 0 && -1 != (read = input.read(buffer, 0, bytesToRead))) {
            output.write(buffer, 0, read);
            totalRead += (long)read;
            if (length > 0L) {
               bytesToRead = (int)Math.min(length - totalRead, (long)buffer.length);
            }
         }

         return totalRead;
      }
   }

   public static long skip(final InputStream input, final long toSkip) throws IOException {
      if (toSkip < 0L) {
         throw new IllegalArgumentException("Skip count must be non-negative, actual: " + toSkip);
      } else {
         long remain;
         long n;
         for(remain = toSkip; remain > 0L; remain -= n) {
            n = (long)input.read(SKIP_BYTE_BUFFER, 0, (int)Math.min(remain, (long)SKIP_BYTE_BUFFER.length));
            if (n < 0L) {
               break;
            }
         }

         return toSkip - remain;
      }
   }

   public static long skip(final ReadableByteChannel input, final long toSkip) throws IOException {
      if (toSkip < 0L) {
         throw new IllegalArgumentException("Skip count must be non-negative, actual: " + toSkip);
      } else {
         ByteBuffer skipByteBuffer = ByteBuffer.allocate((int)Math.min(toSkip, (long)SKIP_BYTE_BUFFER.length));

         long remain;
         int n;
         for(remain = toSkip; remain > 0L; remain -= (long)n) {
            skipByteBuffer.position(0);
            skipByteBuffer.limit((int)Math.min(remain, (long)SKIP_BYTE_BUFFER.length));
            n = input.read(skipByteBuffer);
            if (n == -1) {
               break;
            }
         }

         return toSkip - remain;
      }
   }

   public static long skip(final Reader input, final long toSkip) throws IOException {
      if (toSkip < 0L) {
         throw new IllegalArgumentException("Skip count must be non-negative, actual: " + toSkip);
      } else {
         if (SKIP_CHAR_BUFFER == null) {
            SKIP_CHAR_BUFFER = new char[SKIP_BYTE_BUFFER.length];
         }

         long remain;
         long n;
         for(remain = toSkip; remain > 0L; remain -= n) {
            n = (long)input.read(SKIP_CHAR_BUFFER, 0, (int)Math.min(remain, (long)SKIP_BYTE_BUFFER.length));
            if (n < 0L) {
               break;
            }
         }

         return toSkip - remain;
      }
   }

   public static void skipFully(final InputStream input, final long toSkip) throws IOException {
      if (toSkip < 0L) {
         throw new IllegalArgumentException("Bytes to skip must not be negative: " + toSkip);
      } else {
         long skipped = skip(input, toSkip);
         if (skipped != toSkip) {
            throw new EOFException("Bytes to skip: " + toSkip + " actual: " + skipped);
         }
      }
   }

   public static void skipFully(final ReadableByteChannel input, final long toSkip) throws IOException {
      if (toSkip < 0L) {
         throw new IllegalArgumentException("Bytes to skip must not be negative: " + toSkip);
      } else {
         long skipped = skip(input, toSkip);
         if (skipped != toSkip) {
            throw new EOFException("Bytes to skip: " + toSkip + " actual: " + skipped);
         }
      }
   }

   public static void skipFully(final Reader input, final long toSkip) throws IOException {
      long skipped = skip(input, toSkip);
      if (skipped != toSkip) {
         throw new EOFException("Chars to skip: " + toSkip + " actual: " + skipped);
      }
   }

   public static int length(final byte[] array) {
      return array == null ? 0 : array.length;
   }

   public static int length(final char[] array) {
      return array == null ? 0 : array.length;
   }

   public static int length(final CharSequence csq) {
      return csq == null ? 0 : csq.length();
   }

   public static int length(final Object[] array) {
      return array == null ? 0 : array.length;
   }

   public static void close(final Closeable closeable) throws IOException {
      if (closeable != null) {
         closeable.close();
      }

   }

   public static void close(final Closeable... closeables) throws IOException {
      if (closeables != null) {
         for(Closeable closeable : closeables) {
            close(closeable);
         }
      }

   }

   public static void close(final Closeable closeable, final IOConsumer consumer) throws IOException {
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
         ((HttpURLConnection)conn).disconnect();
      }

   }

   public static String Stream2String(InputStream inputStream) {
      ByteArrayOutputStream result = new ByteArrayOutputStream();

      try {
         byte[] buffer = new byte[8192];

         int length;
         while((length = inputStream.read(buffer)) != -1) {
            result.write(buffer, 0, length);
         }

         return result.toString();
      } catch (Exception e) {
         return e.getLocalizedMessage();
      }
   }
}
