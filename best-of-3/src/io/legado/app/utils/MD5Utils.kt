package io.legado.app.utils

import java.security.MessageDigest
import java.security.NoSuchAlgorithmException

public object MD5Utils {
   public fun md5Encode(str: String?): String {
      if (str == null) {
         return "";
      } else {
         var reStr: java.lang.String = "";

         try {
            val bytes: MessageDigest = MessageDigest.getInstance("MD5");
            val var10001: ByteArray = str.getBytes(Charsets.UTF_8);
            val stringBuffer: ByteArray = bytes.digest(var10001);
            val var13: StringBuilder = new StringBuilder();
            val var6: ByteArray = stringBuffer;
            var var15: Int = 0;
            val var16: Int = stringBuffer.length;

            while (var15 < var16) {
               val b: Byte = var6[var15];
               var15++;
               val bt: Int = b and 255;
               if ((b and 255) < 16) {
                  var13.append(0);
               }

               var13.append(Integer.toHexString(bt));
            }

            val var14: java.lang.String = var13.toString();
            reStr = var14;
         } catch (var11: NoSuchAlgorithmException) {
            var11.printStackTrace();
         }

         return reStr;
      }
   }

   public fun md5Encode16(str: String): String {
      val reStr: java.lang.String = this.md5Encode(str);
      if (reStr == null) {
         throw new NullPointerException("null cannot be cast to non-null type java.lang.String");
      } else {
         val var10000: java.lang.String = reStr.substring(8, 24);
         return var10000;
      }
   }
}
