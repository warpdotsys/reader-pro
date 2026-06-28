package io.legado.app.help.http;

import io.legado.app.constant.AppConst;
import java.util.ArrayList;
import java.util.concurrent.TimeUnit;
import kotlin.Metadata;
import kotlin.collections.CollectionsKt;
import kotlin.jvm.functions.Function0;
import kotlin.jvm.internal.Intrinsics;
import kotlin.jvm.internal.Lambda;
import okhttp3.ConnectionSpec;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import org.jetbrains.annotations.NotNull;

/* JADX INFO: compiled from: HttpHelper.kt */
/* JADX INFO: loaded from: reader-pro-classes-3.2.14.jar:io/legado/app/help/http/HttpHelperKt$okHttpClient$2.class */
@Metadata(mv = {1, 5, 1}, k = 3, xi = 48, d1 = {"\u0000\u0006\n\u0000\n\u0002\u0018\u0002\u0010\u0000\u001a\u00020\u0001H\n"}, d2 = {"<anonymous>", "Lokhttp3/OkHttpClient;"})
final class HttpHelperKt$okHttpClient$2 extends Lambda implements Function0<OkHttpClient> {
    public static final HttpHelperKt$okHttpClient$2 INSTANCE = new HttpHelperKt$okHttpClient$2();

    HttpHelperKt$okHttpClient$2() {
        super(0);
    }

    @NotNull
    /* JADX INFO: renamed from: invoke, reason: merged with bridge method [inline-methods] */
    public final OkHttpClient m184invoke() {
        ArrayList specs = CollectionsKt.arrayListOf(new ConnectionSpec[]{ConnectionSpec.MODERN_TLS, ConnectionSpec.COMPATIBLE_TLS, ConnectionSpec.CLEARTEXT});
        OkHttpClient.Builder builder = new OkHttpClient.Builder().connectTimeout(15L, TimeUnit.SECONDS).writeTimeout(15L, TimeUnit.SECONDS).readTimeout(15L, TimeUnit.SECONDS).sslSocketFactory(SSLHelper.INSTANCE.getUnsafeSSLSocketFactory(), SSLHelper.INSTANCE.getUnsafeTrustManager()).retryOnConnectionFailure(true).hostnameVerifier(SSLHelper.INSTANCE.getUnsafeHostnameVerifier()).connectionSpecs(specs).followRedirects(true).followSslRedirects(true).addInterceptor(HttpHelperKt$okHttpClient$2::m182invoke$lambda0);
        return builder.build();
    }

    /* JADX INFO: renamed from: invoke$lambda-0, reason: not valid java name */
    private static final Response m182invoke$lambda0(Interceptor.Chain chain) {
        Intrinsics.checkNotNullParameter(chain, "chain");
        Request request = chain.request();
        Request.Builder builder = request.newBuilder();
        if (request.header(AppConst.UA_NAME) == null) {
            builder.addHeader(AppConst.UA_NAME, AppConst.INSTANCE.getUserAgent());
        } else if (Intrinsics.areEqual(request.header(AppConst.UA_NAME), "null")) {
            builder.removeHeader(AppConst.UA_NAME);
        }
        builder.addHeader("Keep-Alive", "10").addHeader("Connection", "Keep-Alive").addHeader("Cache-Control", "no-cache");
        return chain.proceed(builder.build());
    }
}
