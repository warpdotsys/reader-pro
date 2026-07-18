package me.ag2s.epublib.util.commons.io;

import java.io.FilterInputStream;
import java.io.IOException;
import java.io.InputStream;
import me.ag2s.epublib.util.IOUtil;

public abstract class ProxyInputStream extends FilterInputStream {
   public ProxyInputStream(final InputStream proxy) {
      super(proxy);
   }

   public int read() throws IOException {
      try {
         this.beforeRead(1);
         int b = this.in.read();
         this.afterRead(b != -1 ? 1 : -1);
         return b;
      } catch (IOException e) {
         this.handleIOException(e);
         return -1;
      }
   }

   public int read(final byte[] bts) throws IOException {
      try {
         this.beforeRead(IOUtil.length(bts));
         int n = this.in.read(bts);
         this.afterRead(n);
         return n;
      } catch (IOException e) {
         this.handleIOException(e);
         return -1;
      }
   }

   public int read(final byte[] bts, final int off, final int len) throws IOException {
      try {
         this.beforeRead(len);
         int n = this.in.read(bts, off, len);
         this.afterRead(n);
         return n;
      } catch (IOException e) {
         this.handleIOException(e);
         return -1;
      }
   }

   public long skip(final long ln) throws IOException {
      try {
         return this.in.skip(ln);
      } catch (IOException e) {
         this.handleIOException(e);
         return 0L;
      }
   }

   public int available() throws IOException {
      try {
         return super.available();
      } catch (IOException e) {
         this.handleIOException(e);
         return 0;
      }
   }

   public void close() throws IOException {
      IOUtil.close(this.in, this::handleIOException);
   }

   public synchronized void mark(final int readlimit) {
      this.in.mark(readlimit);
   }

   public synchronized void reset() throws IOException {
      try {
         this.in.reset();
      } catch (IOException e) {
         this.handleIOException(e);
      }

   }

   public boolean markSupported() {
      return this.in.markSupported();
   }

   protected void beforeRead(final int n) {
   }

   protected void afterRead(final int n) {
   }

   protected void handleIOException(final IOException e) throws IOException {
      throw e;
   }
}
