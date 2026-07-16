// 
// Decompiled by Procyon v0.6.0
// 

package io.legado.app.help.http;

import io.legado.app.utils.EncodingDetect;
import java.nio.charset.Charset;
import okhttp3.MediaType;
import io.legado.app.utils.UTF8BOMFighter;
import kotlin.jvm.internal.Intrinsics;
import okhttp3.ResponseBody;
import retrofit2.Converter;
import retrofit2.Retrofit;
import java.lang.annotation.Annotation;
import java.lang.reflect.Type;
import kotlin.jvm.internal.DefaultConstructorMarker;
import org.jetbrains.annotations.Nullable;
import kotlin.Metadata;
import retrofit2.Converter$Factory;

@Metadata(mv = { 1, 5, 1 }, k = 1, xi = 48, d1 = { "\u00004\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u000e\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u0011\n\u0002\u0010\u001b\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\u0018\u00002\u00020\u0001B\u0011\u0012\n\b\u0002\u0010\u0002\u001a\u0004\u0018\u00010\u0003?\u0006\u0002\u0010\u0004J?\u0010\u0005\u001a\u0010\u0012\u0004\u0012\u00020\u0007\u0012\u0004\u0012\u00020\u0003\u0018\u00010\u00062\b\u0010\b\u001a\u0004\u0018\u00010\t2\u000e\u0010\n\u001a\n\u0012\u0004\u0012\u00020\f\u0018\u00010\u000b2\b\u0010\r\u001a\u0004\u0018\u00010\u000eH\u0016?\u0006\u0002\u0010\u000fR\u0010\u0010\u0002\u001a\u0004\u0018\u00010\u0003X\u0082\u0004?\u0006\u0002\n\u0000¡§\u0006\u0010" }, d2 = { "Lio/legado/app/help/http/EncodeConverter;", "Lretrofit2/Converter$Factory;", "encode", "", "(Ljava/lang/String;)V", "responseBodyConverter", "Lretrofit2/Converter;", "Lokhttp3/ResponseBody;", "type", "Ljava/lang/reflect/Type;", "annotations", "", "", "retrofit", "Lretrofit2/Retrofit;", "(Ljava/lang/reflect/Type;[Ljava/lang/annotation/Annotation;Lretrofit2/Retrofit;)Lretrofit2/Converter;", "reader-pro" })
public final class EncodeConverter extends Converter$Factory
{
    @Nullable
    private final String encode;
    
    public EncodeConverter(@Nullable final String encode) {
        this.encode = encode;
    }
    
    @Nullable
    public Converter<ResponseBody, String> responseBodyConverter(@Nullable final Type type, @Nullable final Annotation[] annotations, @Nullable final Retrofit retrofit) {
        return (Converter<ResponseBody, String>)EncodeConverter::responseBodyConverter$lambda-1;
    }
    
    private static final String responseBodyConverter$lambda-1(final EncodeConverter this$0, final ResponseBody value) {
        Intrinsics.checkNotNullParameter((Object)this$0, "this$0");
        final byte[] responseBytes = UTF8BOMFighter.INSTANCE.removeUTF8BOM(value.bytes());
        final String encode = this$0.encode;
        if (encode == null) {
            String charsetName = null;
            final MediaType mediaType = value.contentType();
            if (mediaType != null) {
                final Charset charset$default;
                final Charset charset = charset$default = MediaType.charset$default(mediaType, (Charset)null, 1, (Object)null);
                charsetName = ((charset$default == null) ? null : charset$default.displayName());
            }
            if (charsetName == null) {
                charsetName = EncodingDetect.INSTANCE.getHtmlEncode(responseBytes);
            }
            final Charset forName = Charset.forName(charsetName);
            Intrinsics.checkNotNullExpressionValue((Object)forName, "forName(charsetName)");
            return new String(responseBytes, forName);
        }
        final String it = encode;
        final int n = 0;
        final Charset forName2 = Charset.forName(this$0.encode);
        Intrinsics.checkNotNullExpressionValue((Object)forName2, "forName(encode)");
        return new String(responseBytes, forName2);
    }
    
    public EncodeConverter() {
        this(null, 1, null);
    }
}
