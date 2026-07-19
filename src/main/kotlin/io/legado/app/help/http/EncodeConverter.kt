package io.legado.app.help.http

import io.legado.app.utils.UTF8BOMFighter
import okhttp3.ResponseBody
import io.legado.app.utils.EncodingDetect
import retrofit2.Converter
import retrofit2.Retrofit
import java.lang.reflect.Type
import java.nio.charset.Charset

class EncodeConverter(private val encode: String? = null) : Converter.Factory() {

    // Kotlin 1.9 rejects the original nullable Java override, but the JVM descriptor still overrides it.
    @Suppress("NOTHING_TO_OVERRIDE", "ACCIDENTAL_OVERRIDE")
    override fun responseBodyConverter(
        type: Type?,
        annotations: Array<out Annotation>?,
        retrofit: Retrofit?
    ): Converter<ResponseBody, String>? {
        return Converter { value ->
            val responseBytes = UTF8BOMFighter.removeUTF8BOM(value.bytes())
            encode?.let { return@Converter String(responseBytes, Charset.forName(encode)) }

            var charsetName: String? = null
            val mediaType = value.contentType()
            if (mediaType != null) {
                val charset = mediaType.charset()
                charsetName = charset?.displayName()
            }

            if (charsetName == null) {
                charsetName = EncodingDetect.getHtmlEncode(responseBytes)
            }

            String(responseBytes, Charset.forName(charsetName))
        }
    }

}
