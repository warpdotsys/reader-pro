package io.legado.app.utils

import io.legado.app.lib.icu4j.CharsetDetector
import io.legado.app.lib.icu4j.CharsetMatch
import java.io.Closeable
import java.io.File
import java.io.FileInputStream
import java.nio.charset.Charset
import java.nio.charset.StandardCharsets
import java.util.Locale
import kotlin.jvm.internal.Intrinsics
import org.jsoup.nodes.Element

public object EncodingDetect {
   public fun getHtmlEncode(bytes: ByteArray): String? {
      try {
         val metaTags: Charset = StandardCharsets.UTF_8;

         for (Element metaTag : var14) {
            var content: java.lang.String = metaTag.attr("charset");
            if (content.length() != 0) {
               return content;
            }

            content = metaTag.attr("content");
            val var20: java.lang.String = metaTag.attr("http-equiv");
            var var10: Locale = Locale.getDefault();
            var var10000: java.lang.String = var20.toLowerCase(var10);
            if (var10000 == "content-type") {
               var10 = Locale.getDefault();
               var10000 = content.toLowerCase(var10);
               if (StringsKt.contains$default(var10000, "charset", false, 2, null)) {
                  val var26: Locale = Locale.getDefault();
                  val var32: java.lang.String = content.toLowerCase(var26);
                  var10000 = content.substring(StringsKt.indexOf$default(var32, "charset", 0, false, 6, null) + "charset=".length());
               } else {
                  val var28: Locale = Locale.getDefault();
                  val var34: java.lang.String = content.toLowerCase(var28);
                  var10000 = content.substring(StringsKt.indexOf$default(var34, ";", 0, false, 6, null) + 1);
               }

               if (var10000.length() != 0) {
                  return var10000;
               }
            }
         }
      } catch (var13: Exception) {
      }

      return this.getEncode(bytes);
   }

   public fun getEncode(bytes: ByteArray): String {
      val match: CharsetMatch = new CharsetDetector().setText(bytes).detect();
      val var10000: java.lang.String;
      if (match == null) {
         var10000 = "UTF-8";
      } else {
         val var4: java.lang.String = match.getName();
         var10000 = if (var4 == null) "UTF-8" else var4;
      }

      return var10000;
   }

   public fun getEncode(filePath: String): String {
      return this.getEncode(new File(filePath));
   }

   public fun getEncode(file: File): String {
      return this.getEncode(this.getFileBytes(file));
   }

   private fun getFileBytes(file: File?): ByteArray {
      label25: {
         val byteArray: ByteArray = new byte[8000];

         try {
            val e: Closeable = new FileInputStream(file);
            var var14: java.lang.Throwable = null as java.lang.Throwable;

            try {
               try {
                  val var15: Int = (e as FileInputStream).read(byteArray);
               } catch (var8: java.lang.Throwable) {
                  var14 = var8;
                  throw var8;
               }
            } catch (var9: java.lang.Throwable) {
               CloseableKt.closeFinally(e, var14);
            }

            CloseableKt.closeFinally(e, null as java.lang.Throwable);
         } catch (var10: Exception) {
            System.err.println(Intrinsics.stringPlus("Error: ", var10));
         }

         return byteArray;
      }
   }
}
