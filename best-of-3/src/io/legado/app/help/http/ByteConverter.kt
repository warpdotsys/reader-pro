package io.legado.app.help.http

import java.lang.reflect.Type
import okhttp3.ResponseBody
import retrofit2.Converter
import retrofit2.Retrofit
import retrofit2.Converter.Factory

public class ByteConverter : Factory {
   public override fun responseBodyConverter(type: Type?, annotations: Array<Annotation>?, retrofit: Retrofit?): Converter<ResponseBody, ByteArray>? {
      return ByteConverter::responseBodyConverter$lambda-0;
   }

   @JvmStatic
   fun `responseBodyConverter$lambda-0`(value: ResponseBody): ByteArray {
      return value.bytes();
   }
}
