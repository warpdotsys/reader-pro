package me.ag2s.epublib.util;

import java.io.IOException;
import java.io.Writer;

public class NoCloseWriter extends Writer {
   private final Writer writer;

   public NoCloseWriter(Writer writer) {
      this.writer = writer;
   }

   public void close() {
   }

   public void flush() throws IOException {
      this.writer.flush();
   }

   public void write(char[] cbuf, int off, int len) throws IOException {
      this.writer.write(cbuf, off, len);
   }
}
