package io.legado.app.help.http;

import io.legado.app.utils.EncodingDetect;
import io.legado.app.utils.UTF8BOMFighter;
import java.lang.annotation.Annotation;
import java.lang.reflect.Type;
import java.nio.charset.Charset;
import kotlin.Metadata;
import kotlin.jvm.internal.DefaultConstructorMarker;
import kotlin.jvm.internal.Intrinsics;
import me.ag2s.epublib.epub.PackageDocumentBase;
import okhttp3.MediaType;
import okhttp3.ResponseBody;
import org.jetbrains.annotations.Nullable;
import retrofit2.Converter;
import retrofit2.Retrofit;

/* JADX INFO: compiled from: EncodeConverter.kt */
/* JADX INFO: loaded from: app-classes.jar:io/legado/app/help/http/EncodeConverter.class */
@Metadata(mv = {1, 5, 1}, k = 1, xi = 48, d1 = {"\u00004\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u000e\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u0011\n\u0002\u0010\u001b\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\u0018\u00002\u00020\u0001B\u0011\u0012\n\b\u0002\u0010\u0002\u001a\u0004\u0018\u00010\u0003¢\u0006\u0002\u0010\u0004J?\u0010\u0005\u001a\u0010\u0012\u0004\u0012\u00020\u0007\u0012\u0004\u0012\u00020\u0003\u0018\u00010\u00062\b\u0010\b\u001a\u0004\u0018\u00010\t2\u000e\u0010\n\u001a\n\u0012\u0004\u0012\u00020\f\u0018\u00010\u000b2\b\u0010\r\u001a\u0004\u0018\u00010\u000eH\u0016¢\u0006\u0002\u0010\u000fR\u0010\u0010\u0002\u001a\u0004\u0018\u00010\u0003X\u0082\u0004¢\u0006\u0002\n\u0000¨\u0006\u0010"}, d2 = {"Lio/legado/app/help/http/EncodeConverter;", "Lretrofit2/Converter$Factory;", "encode", PackageDocumentBase.PREFIX_OPF, "(Ljava/lang/String;)V", "responseBodyConverter", "Lretrofit2/Converter;", "Lokhttp3/ResponseBody;", "type", "Ljava/lang/reflect/Type;", "annotations", PackageDocumentBase.PREFIX_OPF, PackageDocumentBase.PREFIX_OPF, "retrofit", "Lretrofit2/Retrofit;", "(Ljava/lang/reflect/Type;[Ljava/lang/annotation/Annotation;Lretrofit2/Retrofit;)Lretrofit2/Converter;", "reader-pro"})
public final class EncodeConverter extends Converter.Factory {

    @Nullable
    private final String encode;

    public EncodeConverter() {
        this(null, 1, null);
    }

    public EncodeConverter(@Nullable String encode) {
        this.encode = encode;
    }

    public /* synthetic */ EncodeConverter(String str, int i, DefaultConstructorMarker defaultConstructorMarker) {
        this((i & 1) != 0 ? null : str);
    }

    @Nullable
    public Converter<ResponseBody, String> responseBodyConverter(@Nullable Type type, @Nullable Annotation[] annotations, @Nullable Retrofit retrofit) {
        return (v1) -> {
            return m180responseBodyConverter$lambda1(r0, v1);
        };
    }

    /* JADX INFO: renamed from: responseBodyConverter$lambda-1, reason: not valid java name */
    private static final String m180responseBodyConverter$lambda1(EncodeConverter this$0, ResponseBody value) {
        Intrinsics.checkNotNullParameter(this$0, "this$0");
        byte[] responseBytes = UTF8BOMFighter.INSTANCE.removeUTF8BOM(value.bytes());
        if (this$0.encode != null) {
            Charset charsetForName = Charset.forName(this$0.encode);
            Intrinsics.checkNotNullExpressionValue(charsetForName, "forName(encode)");
            return new String(responseBytes, charsetForName);
        }
        String charsetName = null;
        MediaType mediaType = value.contentType();
        if (mediaType != null) {
            Charset charset = MediaType.charset$default(mediaType, (Charset) null, 1, (Object) null);
            charsetName = charset == null ? null : charset.displayName();
        }
        if (charsetName == null) {
            charsetName = EncodingDetect.INSTANCE.getHtmlEncode(responseBytes);
        }
        Charset charsetForName2 = Charset.forName(charsetName);
        Intrinsics.checkNotNullExpressionValue(charsetForName2, "forName(charsetName)");
        return new String(responseBytes, charsetForName2);
    }
}
