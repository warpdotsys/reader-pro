package io.legado.app.utils

public object Utf8BomUtils {
   private final val UTF8_BOM_BYTES: ByteArray = new byte[]{-17, -69, -65}

   public fun removeUTF8BOM(xmlText: String): String {
      val var10000: ByteArray = xmlText.getBytes(Charsets.UTF_8);
      return if (var10000.length > 3 && var10000[0] == UTF8_BOM_BYTES[0] && var10000[1] == UTF8_BOM_BYTES[1] && var10000[2] == UTF8_BOM_BYTES[2])
         new java.lang.String(var10000, 3, var10000.length - 3, Charsets.UTF_8)
         else
         xmlText;
   }

   public fun removeUTF8BOM(bytes: ByteArray): ByteArray {
      if (bytes.length > 3 && bytes[0] == UTF8_BOM_BYTES[0] && bytes[1] == UTF8_BOM_BYTES[1] && bytes[2] == UTF8_BOM_BYTES[2]) {
         val copy: ByteArray = new byte[bytes.length - 3];
         System.arraycopy(bytes, 3, copy, 0, bytes.length - 3);
         return copy;
      } else {
         return bytes;
      }
   }

   public fun hasBom(bytes: ByteArray): Boolean {
      return bytes.length > 3 && bytes[0] == UTF8_BOM_BYTES[0] && bytes[1] == UTF8_BOM_BYTES[1] && bytes[2] == UTF8_BOM_BYTES[2];
   }
}
