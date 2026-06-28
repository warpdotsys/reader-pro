package io.legado.app.help.http;

import java.lang.annotation.Annotation;
import java.lang.reflect.Type;
import kotlin.Metadata;
import me.ag2s.epublib.epub.PackageDocumentBase;
import okhttp3.ResponseBody;
import org.jetbrains.annotations.Nullable;
import retrofit2.Converter;
import retrofit2.Retrofit;

/* JADX INFO: compiled from: ByteConverter.kt */
/* JADX INFO: loaded from: app-classes.jar:io/legado/app/help/http/ByteConverter.class */
@Metadata(mv = {1, 5, 1}, k = 1, xi = 48, d1 = {"\u00002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0010\u0012\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u0011\n\u0002\u0010\u001b\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\u0018\u00002\u00020\u0001B\u0005¢\u0006\u0002\u0010\u0002J?\u0010\u0003\u001a\u0010\u0012\u0004\u0012\u00020\u0005\u0012\u0004\u0012\u00020\u0006\u0018\u00010\u00042\b\u0010\u0007\u001a\u0004\u0018\u00010\b2\u000e\u0010\t\u001a\n\u0012\u0004\u0012\u00020\u000b\u0018\u00010\n2\b\u0010\f\u001a\u0004\u0018\u00010\rH\u0016¢\u0006\u0002\u0010\u000e¨\u0006\u000f"}, d2 = {"Lio/legado/app/help/http/ByteConverter;", "Lretrofit2/Converter$Factory;", "()V", "responseBodyConverter", "Lretrofit2/Converter;", "Lokhttp3/ResponseBody;", PackageDocumentBase.PREFIX_OPF, "type", "Ljava/lang/reflect/Type;", "annotations", PackageDocumentBase.PREFIX_OPF, PackageDocumentBase.PREFIX_OPF, "retrofit", "Lretrofit2/Retrofit;", "(Ljava/lang/reflect/Type;[Ljava/lang/annotation/Annotation;Lretrofit2/Retrofit;)Lretrofit2/Converter;", "reader-pro"})
public final class ByteConverter extends Converter.Factory {
    @Nullable
    public Converter<ResponseBody, byte[]> responseBodyConverter(@Nullable Type type, @Nullable Annotation[] annotations, @Nullable Retrofit retrofit) {
        return ByteConverter::m176responseBodyConverter$lambda0;
    }

    /* JADX INFO: renamed from: responseBodyConverter$lambda-0, reason: not valid java name */
    private static final byte[] m176responseBodyConverter$lambda0(ResponseBody value) {
        return value.bytes();
    }
}
