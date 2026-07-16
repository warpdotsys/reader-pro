package io.legado.app.utils

import com.htmake.reader.synth.SyntheticContinuation
import com.htmake.reader.synth.SyntheticFunction0
import com.htmake.reader.synth.SyntheticType

import java.net.InetAddress
import java.net.NetworkInterface
import java.net.SocketException
import java.net.URL
import java.util.BitSet
import java.util.Enumeration
import java.util.regex.Pattern
import okhttp3.HttpUrl
import okhttp3.Request
import retrofit2.Response

public object NetworkUtils {
   private final val IPV4_PATTERN: Pattern =
      Pattern.compile("^(([0-9]|[1-9][0-9]|1[0-9]{2}|2[0-4][0-9]|25[0-5])\\.){3}([0-9]|[1-9][0-9]|1[0-9]{2}|2[0-4][0-9]|25[0-5])$")

   private final val notNeedEncoding: BitSet by LazyKt.lazy(SyntheticFunction0.INSTANCE)
      private final get() {
         return notNeedEncoding$delegate.getValue() as BitSet;
      }

   public fun getUrl(response: Response<*>): String {
      val networkResponse: okhttp3.Response = response.raw().networkResponse();
      val var10000: java.lang.String;
      if (networkResponse == null) {
         var10000 = null;
      } else {
         val var5: Request = networkResponse.request();
         if (var5 == null) {
            var10000 = null;
         } else {
            val var6: HttpUrl = var5.url();
            var10000 = if (var6 == null) null else var6.toString();
         }
      }

      return if (var10000 == null) response.raw().request().url().toString() else var10000;
   }

   public fun hasUrlEncoded(str: String): Boolean {
      var needEncode: Boolean = false;
      var i: Int = 0;

      while (i < str.length()) {
         val c: Char = str.charAt(i);
         if (this.getNotNeedEncoding().get(c)) {
            i++;
         } else {
            if (c != '%' || i + 2 >= str.length() || !this.isDigit16Char(str.charAt(++i)) || !this.isDigit16Char(str.charAt(++i))) {
               needEncode = true;
               break;
            }

            i++;
            continue;
         }
      }

      return !needEncode;
   }

   private fun isDigit16Char(c: Char): Boolean {
      return '0' <= c && c <= '9' || 'A' <= c && c <= 'F' || 'a' <= c && c <= 'f';
   }

   public fun getAbsoluteURL(baseURL: String?, relativePath: String): String {
      if (baseURL == null || baseURL.length() == 0) {
         return relativePath;
      } else if (relativePath.length() == 0) {
         return baseURL;
      } else {
         var var9: java.lang.String = relativePath;

         try {
            val var6: java.lang.String = new URL(new URL(StringsKt.substringBefore$default(baseURL, ",", null, 2, null)), relativePath).toString();
            var9 = var6;
            return var6;
         } catch (var7: Exception) {
            var7.printStackTrace();
            return var9;
         }
      }
   }

   public fun getAbsoluteURL(baseURL: URL?, relativePath: String): String {
      if (baseURL == null) {
         return relativePath;
      } else {
         var relativeUrl: java.lang.String = relativePath;

         try {
            val var5: java.lang.String = new URL(baseURL, relativePath).toString();
            relativeUrl = var5;
            return var5;
         } catch (var6: Exception) {
            var6.printStackTrace();
            return relativeUrl;
         }
      }
   }

   public fun getBaseUrl(url: String?): String? {
      if (url != null && StringsKt.startsWith$default(url, "http", false, 2, null)) {
         val index: Int = StringsKt.indexOf$default(url, "/", 9, false, 4, null);
         val var10000: java.lang.String;
         if (index == -1) {
            var10000 = url;
         } else {
            var10000 = url.substring(0, index);
         }

         return var10000;
      } else {
         return null;
      }
   }

   public fun getSubDomain(url: String?): String {
      val var3: java.lang.String = this.getBaseUrl(url);
      if (var3 == null) {
         return "";
      } else {
         val var10000: java.lang.String;
         if (StringsKt.indexOf$default(var3, ".", 0, false, 6, null) == StringsKt.lastIndexOf$default(var3, ".", 0, false, 6, null)) {
            var10000 = var3.substring(StringsKt.lastIndexOf$default(var3, "/", 0, false, 6, null) + 1);
         } else {
            var10000 = var3.substring(StringsKt.indexOf$default(var3, ".", 0, false, 6, null) + 1);
         }

         return var10000;
      }
   }

   public fun getLocalIPAddress(): InetAddress? {
      var enumeration: Enumeration = null;

      try {
         enumeration = NetworkInterface.getNetworkInterfaces();
      } catch (var6: SocketException) {
         var6.printStackTrace();
      }

      if (enumeration != null) {
         while (enumeration.hasMoreElements()) {
            val addresses: Enumeration = (enumeration.nextElement() as NetworkInterface).getInetAddresses();
            if (addresses != null) {
               while (addresses.hasMoreElements()) {
                  val address: InetAddress = addresses.nextElement() as InetAddress;
                  if (!address.isLoopbackAddress()) {
                     val var5: java.lang.String = address.getHostAddress();
                     if (this.isIPv4Address(var5)) {
                        return address;
                     }
                  }
               }
            }
         }
      }

      return null;
   }

   public fun isIPv4Address(input: String): Boolean {
      return IPV4_PATTERN.matcher(input).matches();
   }
}
