package me.ag2s.epublib.util;

import java.io.IOException;
import java.io.OutputStream;

public class NoCloseOutputStream extends OutputStream {
   private final OutputStream outputStream;

   public NoCloseOutputStream(OutputStream outputStream) {
      this.outputStream = outputStream;
   }

   @Override
   public void write(int b) throws IOException {
      this.outputStream.write(b);
   }

   @Override
   public void close() {
   }
}
