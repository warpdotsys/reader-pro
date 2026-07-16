package io.legado.app.help.http

import io.legado.app.utils.EncodingDetect
import io.legado.app.utils.UTF8BOMFighter
import java.lang.reflect.Type
import java.nio.charset.Charset
import okhttp3.MediaType
import okhttp3.ResponseBody
import retrofit2.Converter
import retrofit2.Retrofit
import retrofit2.Converter.Factory

public class EncodeConverter(encode: String? = null) : Factory {
   private final val encode: String?

   init {
      this.encode = encode;
   }

   public override fun responseBodyConverter(type: Type?, annotations: Array<Annotation>?, retrofit: Retrofit?): Converter<ResponseBody, String>? {
      return EncodeConverter::responseBodyConverter$lambda-1;
   }

   @JvmStatic
   fun `responseBodyConverter$lambda-1`(`this$0`: EncodeConverter, value: ResponseBody): java.lang.String {
      val responseBytes: ByteArray = UTF8BOMFighter.INSTANCE.removeUTF8BOM(value.bytes());
      if (`this$0`.encode == null) {
         var var11: java.lang.String = null;
         val mediaType: MediaType = value.contentType();
         if (mediaType != null) {
            val var12: Charset = MediaType.charset$default(mediaType, null, 1, null);
            var11 = if (var12 == null) null else var12.displayName();
         }

         if (var11 == null) {
            var11 = EncodingDetect.INSTANCE.getHtmlEncode(responseBytes);
         }

         val var13: Charset = Charset.forName(var11);
         return new java.lang.String(responseBytes, var13);
      } else {
         val var9: Charset = Charset.forName(`this$0`.encode);
         return new java.lang.String(responseBytes, var9);
      }
   }

   fun EncodeConverter() {
      this(null, 1, null);
   }
}
