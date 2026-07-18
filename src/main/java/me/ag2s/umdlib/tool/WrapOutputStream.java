package me.ag2s.umdlib.tool;

import java.io.IOException;
import java.io.OutputStream;

public class WrapOutputStream extends OutputStream {
   private OutputStream os;
   private int written;

   public WrapOutputStream(OutputStream os) {
      this.os = os;
   }

   private void incCount(int value) {
      int temp = this.written + value;
      if (temp < 0) {
         temp = Integer.MAX_VALUE;
      }

      this.written = temp;
   }

   public void writeInt(int v) throws IOException {
      this.os.write(v >>> 0 & 255);
      this.os.write(v >>> 8 & 255);
      this.os.write(v >>> 16 & 255);
      this.os.write(v >>> 24 & 255);
      this.incCount(4);
   }

   public void writeByte(byte b) throws IOException {
      this.write(b);
   }

   public void writeByte(int n) throws IOException {
      this.write(n);
   }

   public void writeBytes(byte... bytes) throws IOException {
      this.write(bytes);
   }

   public void writeBytes(int... vals) throws IOException {
      for(int v : vals) {
         this.write(v);
      }

   }

   public void write(byte[] b, int off, int len) throws IOException {
      this.os.write(b, off, len);
      this.incCount(len);
   }

   public void write(byte[] b) throws IOException {
      this.os.write(b);
      this.incCount(b.length);
   }

   public void write(int b) throws IOException {
      this.os.write(b);
      this.incCount(1);
   }

   public void close() throws IOException {
      this.os.close();
   }

   public void flush() throws IOException {
      this.os.flush();
   }

   public boolean equals(Object obj) {
      return this.os.equals(obj);
   }

   public int hashCode() {
      return this.os.hashCode();
   }

   public String toString() {
      return this.os.toString();
   }

   public int getWritten() {
      return this.written;
   }
}
