// 
// Decompiled by Procyon v0.6.0
// 

package io.legado.app.help.http;

import kotlin.LazyKt;
import kotlin.jvm.functions.Function0;
import kotlin.sequences.Sequence;
import okhttp3.OkHttpClient$Builder;
import java.util.Map;
import okhttp3.Authenticator;
import java.net.InetSocketAddress;
import java.net.SocketAddress;
import java.net.Proxy;
import kotlin.jvm.internal.Intrinsics;
import kotlin.jvm.internal.Ref$ObjectRef;
import kotlin.sequences.SequencesKt;
import kotlin.text.MatchResult;
import kotlin.text.Regex;
import okhttp3.Interceptor;
import okhttp3.logging.HttpLoggingInterceptor$Level;
import okhttp3.logging.HttpLoggingInterceptor;
import okhttp3.logging.HttpLoggingInterceptor$Logger;
import kotlin.text.StringsKt;
import io.legado.app.model.DebugLog;
import org.jetbrains.annotations.Nullable;
import okhttp3.OkHttpClient;
import java.util.concurrent.ConcurrentHashMap;
import org.jetbrains.annotations.NotNull;
import kotlin.Lazy;
import kotlin.Metadata;

@Metadata(mv = { 1, 5, 1 }, k = 2, xi = 48, d1 = { "\u0000\u001c\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0005\n\u0002\u0018\u0002\n\u0002\u0010\u000e\n\u0002\b\u0006\n\u0002\u0018\u0002\n\u0000\u001a\u001e\u0010\f\u001a\u00020\u00012\n\b\u0002\u0010\r\u001a\u0004\u0018\u00010\b2\n\b\u0002\u0010\u000e\u001a\u0004\u0018\u00010\u000f\"\u001b\u0010\u0000\u001a\u00020\u00018FX\u0086\u0084\u0002?\u0006\f\n\u0004\b\u0004\u0010\u0005\u001a\u0004\b\u0002\u0010\u0003\"'\u0010\u0006\u001a\u000e\u0012\u0004\u0012\u00020\b\u0012\u0004\u0012\u00020\u00010\u00078BX\u0082\u0084\u0002?\u0006\f\n\u0004\b\u000b\u0010\u0005\u001a\u0004\b\t\u0010\n¡§\u0006\u0010" }, d2 = { "okHttpClient", "Lokhttp3/OkHttpClient;", "getOkHttpClient", "()Lokhttp3/OkHttpClient;", "okHttpClient$delegate", "Lkotlin/Lazy;", "proxyClientCache", "Ljava/util/concurrent/ConcurrentHashMap;", "", "getProxyClientCache", "()Ljava/util/concurrent/ConcurrentHashMap;", "proxyClientCache$delegate", "getProxyClient", "proxy", "debugLog", "Lio/legado/app/model/DebugLog;", "reader-pro" })
public final class HttpHelperKt
{
    @NotNull
    private static final Lazy proxyClientCache$delegate;
    @NotNull
    private static final Lazy okHttpClient$delegate;
    
    private static final ConcurrentHashMap<String, OkHttpClient> getProxyClientCache() {
        return (ConcurrentHashMap)HttpHelperKt.proxyClientCache$delegate.getValue();
    }
    
    @NotNull
    public static final OkHttpClient getOkHttpClient() {
        return (OkHttpClient)HttpHelperKt.okHttpClient$delegate.getValue();
    }
    
    @NotNull
    public static final OkHttpClient getProxyClient(@Nullable final String proxy, @Nullable final DebugLog debugLog) {
        final CharSequence charSequence = proxy;
        if (charSequence == null || StringsKt.isBlank(charSequence)) {
            if (debugLog == null) {
                return getOkHttpClient();
            }
            final OkHttpClient$Builder builder = getOkHttpClient().newBuilder();
            final HttpLoggingInterceptor logInterceptor = new HttpLoggingInterceptor((HttpLoggingInterceptor$Logger)debugLog);
            logInterceptor.setLevel(HttpLoggingInterceptor$Level.BODY);
            builder.addNetworkInterceptor((Interceptor)logInterceptor);
            return builder.build();
        }
        else {
            if (debugLog == null) {
                final OkHttpClient okHttpClient = getProxyClientCache().get(proxy);
                if (okHttpClient != null) {
                    final OkHttpClient it = okHttpClient;
                    final int n = 0;
                    return it;
                }
            }
            final Regex r = new Regex("(http|socks4|socks5)://(.*):(\\d{2,5})(@.*@.*)?");
            final Sequence ms = Regex.findAll$default(r, (CharSequence)proxy, 0, 2, (Object)null);
            final MatchResult group = (MatchResult)SequencesKt.first(ms);
            final Ref$ObjectRef username = new Ref$ObjectRef();
            username.element = "";
            final Ref$ObjectRef password = new Ref$ObjectRef();
            password.element = "";
            final String type = Intrinsics.areEqual(group.getGroupValues().get(1), (Object)"http") ? "http" : "socks";
            final String host = group.getGroupValues().get(2);
            final int port = Integer.parseInt(group.getGroupValues().get(3));
            if (!Intrinsics.areEqual(group.getGroupValues().get(4), (Object)"")) {
                username.element = StringsKt.split$default((CharSequence)group.getGroupValues().get(4), new String[] { "@" }, false, 0, 6, (Object)null).get(1);
                password.element = StringsKt.split$default((CharSequence)group.getGroupValues().get(4), new String[] { "@" }, false, 0, 6, (Object)null).get(2);
            }
            if (Intrinsics.areEqual((Object)type, (Object)"direct") || Intrinsics.areEqual((Object)host, (Object)"")) {
                return getOkHttpClient();
            }
            final OkHttpClient$Builder builder2 = getOkHttpClient().newBuilder();
            if (Intrinsics.areEqual((Object)type, (Object)"http")) {
                builder2.proxy(new Proxy(Proxy.Type.HTTP, new InetSocketAddress(host, port)));
            }
            else {
                builder2.proxy(new Proxy(Proxy.Type.SOCKS, new InetSocketAddress(host, port)));
            }
            if (!Intrinsics.areEqual(username.element, (Object)"") && !Intrinsics.areEqual(password.element, (Object)"")) {
                final HttpHelperKt$getProxyClient$proxyAuthenticator.HttpHelperKt$getProxyClient$proxyAuthenticator$1 proxyAuthenticator = new HttpHelperKt$getProxyClient$proxyAuthenticator.HttpHelperKt$getProxyClient$proxyAuthenticator$1(username, password);
                builder2.proxyAuthenticator((Authenticator)proxyAuthenticator);
            }
            if (debugLog != null) {
                final HttpLoggingInterceptor logInterceptor2 = new HttpLoggingInterceptor((HttpLoggingInterceptor$Logger)debugLog);
                logInterceptor2.setLevel(HttpLoggingInterceptor$Level.BODY);
                builder2.addNetworkInterceptor((Interceptor)logInterceptor2);
                return builder2.build();
            }
            final OkHttpClient proxyClient = builder2.build();
            getProxyClientCache().put(proxy, proxyClient);
            return proxyClient;
        }
    }
    
    static {
        proxyClientCache$delegate = LazyKt.lazy((Function0)HttpHelperKt$proxyClientCache.HttpHelperKt$proxyClientCache$2.INSTANCE);
        okHttpClient$delegate = LazyKt.lazy((Function0)HttpHelperKt$okHttpClient.HttpHelperKt$okHttpClient$2.INSTANCE);
    }
}
