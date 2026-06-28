package io.legado.app.help.http;

import io.legado.app.constant.AppConst;
import io.legado.app.model.DebugLog;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.Proxy;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;
import kotlin.Lazy;
import kotlin.LazyKt;
import kotlin.Metadata;
import kotlin.collections.CollectionsKt;
import kotlin.jvm.functions.Function0;
import kotlin.jvm.internal.Intrinsics;
import kotlin.jvm.internal.Ref;
import kotlin.sequences.Sequence;
import kotlin.sequences.SequencesKt;
import kotlin.text.MatchResult;
import kotlin.text.Regex;
import kotlin.text.StringsKt;
import me.ag2s.epublib.epub.PackageDocumentBase;
import okhttp3.Authenticator;
import okhttp3.ConnectionSpec;
import okhttp3.Credentials;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.Route;
import okhttp3.logging.HttpLoggingInterceptor;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/* JADX INFO: compiled from: HttpHelper.kt */
/* JADX INFO: loaded from: app-classes.jar:io/legado/app/help/http/HttpHelperKt.class */
@Metadata(mv = {1, 5, 1}, k = 2, xi = 48, d1 = {"\u0000\u001c\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0005\n\u0002\u0018\u0002\n\u0002\u0010\u000e\n\u0002\b\u0006\n\u0002\u0018\u0002\n\u0000\u001a\u001e\u0010\f\u001a\u00020\u00012\n\b\u0002\u0010\r\u001a\u0004\u0018\u00010\b2\n\b\u0002\u0010\u000e\u001a\u0004\u0018\u00010\u000f\"\u001b\u0010\u0000\u001a\u00020\u00018FX\u0086\u0084\u0002¢\u0006\f\n\u0004\b\u0004\u0010\u0005\u001a\u0004\b\u0002\u0010\u0003\"'\u0010\u0006\u001a\u000e\u0012\u0004\u0012\u00020\b\u0012\u0004\u0012\u00020\u00010\u00078BX\u0082\u0084\u0002¢\u0006\f\n\u0004\b\u000b\u0010\u0005\u001a\u0004\b\t\u0010\n¨\u0006\u0010"}, d2 = {"okHttpClient", "Lokhttp3/OkHttpClient;", "getOkHttpClient", "()Lokhttp3/OkHttpClient;", "okHttpClient$delegate", "Lkotlin/Lazy;", "proxyClientCache", "Ljava/util/concurrent/ConcurrentHashMap;", PackageDocumentBase.PREFIX_OPF, "getProxyClientCache", "()Ljava/util/concurrent/ConcurrentHashMap;", "proxyClientCache$delegate", "getProxyClient", "proxy", "debugLog", "Lio/legado/app/model/DebugLog;", "reader-pro"})
public final class HttpHelperKt {

    @NotNull
    private static final Lazy proxyClientCache$delegate = LazyKt.lazy(new Function0<ConcurrentHashMap<String, OkHttpClient>>() { // from class: io.legado.app.help.http.HttpHelperKt$proxyClientCache$2
        @NotNull
        /* JADX INFO: renamed from: invoke, reason: merged with bridge method [inline-methods] */
        public final ConcurrentHashMap<String, OkHttpClient> m186invoke() {
            return new ConcurrentHashMap<>();
        }
    });

    @NotNull
    private static final Lazy okHttpClient$delegate = LazyKt.lazy(new Function0<OkHttpClient>() { // from class: io.legado.app.help.http.HttpHelperKt$okHttpClient$2
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
    });

    private static final ConcurrentHashMap<String, OkHttpClient> getProxyClientCache() {
        return (ConcurrentHashMap) proxyClientCache$delegate.getValue();
    }

    @NotNull
    public static final OkHttpClient getOkHttpClient() {
        return (OkHttpClient) okHttpClient$delegate.getValue();
    }

    public static /* synthetic */ OkHttpClient getProxyClient$default(String str, DebugLog debugLog, int i, Object obj) {
        if ((i & 1) != 0) {
            str = null;
        }
        if ((i & 2) != 0) {
            debugLog = null;
        }
        return getProxyClient(str, debugLog);
    }

    @NotNull
    public static final OkHttpClient getProxyClient(@Nullable String proxy, @Nullable DebugLog debugLog) {
        OkHttpClient it;
        String str = proxy;
        if (str == null || StringsKt.isBlank(str)) {
            if (debugLog == null) {
                return getOkHttpClient();
            }
            OkHttpClient.Builder builder = getOkHttpClient().newBuilder();
            Interceptor httpLoggingInterceptor = new HttpLoggingInterceptor(debugLog);
            httpLoggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
            builder.addNetworkInterceptor(httpLoggingInterceptor);
            return builder.build();
        }
        if (debugLog == null && (it = getProxyClientCache().get(proxy)) != null) {
            return it;
        }
        Regex r = new Regex("(http|socks4|socks5)://(.*):(\\d{2,5})(@.*@.*)?");
        Sequence ms = Regex.findAll$default(r, proxy, 0, 2, (Object) null);
        MatchResult group = (MatchResult) SequencesKt.first(ms);
        final Ref.ObjectRef username = new Ref.ObjectRef();
        username.element = PackageDocumentBase.PREFIX_OPF;
        final Ref.ObjectRef password = new Ref.ObjectRef();
        password.element = PackageDocumentBase.PREFIX_OPF;
        String type = Intrinsics.areEqual(group.getGroupValues().get(1), "http") ? "http" : "socks";
        String host = (String) group.getGroupValues().get(2);
        int port = Integer.parseInt((String) group.getGroupValues().get(3));
        if (!Intrinsics.areEqual(group.getGroupValues().get(4), PackageDocumentBase.PREFIX_OPF)) {
            username.element = StringsKt.split$default((CharSequence) group.getGroupValues().get(4), new String[]{"@"}, false, 0, 6, (Object) null).get(1);
            password.element = StringsKt.split$default((CharSequence) group.getGroupValues().get(4), new String[]{"@"}, false, 0, 6, (Object) null).get(2);
        }
        if (!Intrinsics.areEqual(type, "direct") && !Intrinsics.areEqual(host, PackageDocumentBase.PREFIX_OPF)) {
            OkHttpClient.Builder builder2 = getOkHttpClient().newBuilder();
            if (Intrinsics.areEqual(type, "http")) {
                builder2.proxy(new Proxy(Proxy.Type.HTTP, new InetSocketAddress(host, port)));
            } else {
                builder2.proxy(new Proxy(Proxy.Type.SOCKS, new InetSocketAddress(host, port)));
            }
            if (!Intrinsics.areEqual(username.element, PackageDocumentBase.PREFIX_OPF) && !Intrinsics.areEqual(password.element, PackageDocumentBase.PREFIX_OPF)) {
                builder2.proxyAuthenticator(new Authenticator() { // from class: io.legado.app.help.http.HttpHelperKt$getProxyClient$proxyAuthenticator$1
                    @NotNull
                    public Request authenticate(@Nullable Route route, @NotNull Response response) throws IOException {
                        Intrinsics.checkNotNullParameter(response, "response");
                        String credential = Credentials.basic$default((String) username.element, (String) password.element, (Charset) null, 4, (Object) null);
                        return response.request().newBuilder().header("Proxy-Authorization", credential).build();
                    }
                });
            }
            if (debugLog != null) {
                Interceptor httpLoggingInterceptor2 = new HttpLoggingInterceptor(debugLog);
                httpLoggingInterceptor2.setLevel(HttpLoggingInterceptor.Level.BODY);
                builder2.addNetworkInterceptor(httpLoggingInterceptor2);
                return builder2.build();
            }
            OkHttpClient proxyClient = builder2.build();
            getProxyClientCache().put(proxy, proxyClient);
            return proxyClient;
        }
        return getOkHttpClient();
    }
}
