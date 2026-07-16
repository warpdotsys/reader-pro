// 
// Decompiled by Procyon v0.6.0
// 

package io.legado.app.utils;

import kotlin.LazyKt;
import kotlin.jvm.functions.Function0;
import java.util.Enumeration;
import java.net.SocketException;
import java.net.NetworkInterface;
import java.net.InetAddress;
import java.net.URL;
import kotlin.text.StringsKt;
import org.jetbrains.annotations.Nullable;
import java.util.BitSet;
import okhttp3.HttpUrl;
import okhttp3.Request;
import kotlin.jvm.internal.Intrinsics;
import retrofit2.Response;
import java.util.regex.Pattern;
import kotlin.Lazy;
import org.jetbrains.annotations.NotNull;
import kotlin.Metadata;

@Metadata(mv = { 1, 5, 1 }, k = 1, xi = 48, d1 = { "\u0000H\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0005\n\u0002\u0010\u000e\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0004\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u000b\n\u0002\b\u0003\n\u0002\u0010\f\n\u0002\b\u0003\b\u00c6\u0002\u0018\u00002\u00020\u0001B\u0007\b\u0002?\u0006\u0002\u0010\u0002J\u0018\u0010\f\u001a\u00020\r2\b\u0010\u000e\u001a\u0004\u0018\u00010\u000f2\u0006\u0010\u0010\u001a\u00020\rJ\u0018\u0010\f\u001a\u00020\r2\b\u0010\u000e\u001a\u0004\u0018\u00010\r2\u0006\u0010\u0010\u001a\u00020\rJ\u0012\u0010\u0011\u001a\u0004\u0018\u00010\r2\b\u0010\u0012\u001a\u0004\u0018\u00010\rJ\b\u0010\u0013\u001a\u0004\u0018\u00010\u0014J\u0010\u0010\u0015\u001a\u00020\r2\b\u0010\u0012\u001a\u0004\u0018\u00010\rJ\u0012\u0010\u0016\u001a\u00020\r2\n\u0010\u0017\u001a\u0006\u0012\u0002\b\u00030\u0018J\u000e\u0010\u0019\u001a\u00020\u001a2\u0006\u0010\u001b\u001a\u00020\rJ\u0010\u0010\u001c\u001a\u00020\u001a2\u0006\u0010\u001d\u001a\u00020\u001eH\u0002J\u000e\u0010\u001f\u001a\u00020\u001a2\u0006\u0010 \u001a\u00020\rR\u0016\u0010\u0003\u001a\n \u0005*\u0004\u0018\u00010\u00040\u0004X\u0082\u0004?\u0006\u0002\n\u0000R\u001b\u0010\u0006\u001a\u00020\u00078BX\u0082\u0084\u0002?\u0006\f\n\u0004\b\n\u0010\u000b\u001a\u0004\b\b\u0010\t¡§\u0006!" }, d2 = { "Lio/legado/app/utils/NetworkUtils;", "", "()V", "IPV4_PATTERN", "Ljava/util/regex/Pattern;", "kotlin.jvm.PlatformType", "notNeedEncoding", "Ljava/util/BitSet;", "getNotNeedEncoding", "()Ljava/util/BitSet;", "notNeedEncoding$delegate", "Lkotlin/Lazy;", "getAbsoluteURL", "", "baseURL", "Ljava/net/URL;", "relativePath", "getBaseUrl", "url", "getLocalIPAddress", "Ljava/net/InetAddress;", "getSubDomain", "getUrl", "response", "Lretrofit2/Response;", "hasUrlEncoded", "", "str", "isDigit16Char", "c", "", "isIPv4Address", "input", "reader-pro" })
public final class NetworkUtils
{
    @NotNull
    public static final NetworkUtils INSTANCE;
    @NotNull
    private static final Lazy notNeedEncoding$delegate;
    private static final Pattern IPV4_PATTERN;
    
    private NetworkUtils() {
    }
    
    @NotNull
    public final String getUrl(@NotNull final Response<?> response) {
        Intrinsics.checkNotNullParameter((Object)response, "response");
        final okhttp3.Response networkResponse2;
        final okhttp3.Response networkResponse = networkResponse2 = response.raw().networkResponse();
        String s;
        if (networkResponse2 == null) {
            s = null;
        }
        else {
            final Request request = networkResponse2.request();
            if (request == null) {
                s = null;
            }
            else {
                final HttpUrl url = request.url();
                s = ((url == null) ? null : url.toString());
            }
        }
        final String s2 = s;
        return (s2 == null) ? response.raw().request().url().toString() : s2;
    }
    
    private final BitSet getNotNeedEncoding() {
        return (BitSet)NetworkUtils.notNeedEncoding$delegate.getValue();
    }
    
    public final boolean hasUrlEncoded(@NotNull final String str) {
        Intrinsics.checkNotNullParameter((Object)str, "str");
        boolean needEncode = false;
        int i = 0;
        while (i < str.length()) {
            final char c = str.charAt(i);
            if (!this.getNotNeedEncoding().get(c)) {
                if (c == '%' && i + 2 < str.length()) {
                    final char c2 = str.charAt(++i);
                    final char c3 = str.charAt(++i);
                    if (this.isDigit16Char(c2) && this.isDigit16Char(c3)) {
                        ++i;
                        continue;
                    }
                }
                needEncode = true;
                break;
            }
            ++i;
        }
        return !needEncode;
    }
    
    private final boolean isDigit16Char(final char c) {
        return ('0' <= c && c <= '9') || ('A' <= c && c <= 'F') || ('a' <= c && c <= 'f');
    }
    
    @NotNull
    public final String getAbsoluteURL(@Nullable final String baseURL, @NotNull final String relativePath) {
        Intrinsics.checkNotNullParameter((Object)relativePath, "relativePath");
        final CharSequence charSequence = baseURL;
        if (charSequence == null || charSequence.length() == 0) {
            return relativePath;
        }
        if (relativePath.length() == 0) {
            return baseURL;
        }
        String relativeUrl = relativePath;
        try {
            final URL absoluteUrl = new URL(StringsKt.substringBefore$default(baseURL, ",", (String)null, 2, (Object)null));
            final URL parseUrl = new URL(absoluteUrl, relativePath);
            final String string = parseUrl.toString();
            Intrinsics.checkNotNullExpressionValue((Object)string, "parseUrl.toString()");
            relativeUrl = string;
            return relativeUrl;
        }
        catch (final Exception e) {
            e.printStackTrace();
            return relativeUrl;
        }
    }
    
    @NotNull
    public final String getAbsoluteURL(@Nullable final URL baseURL, @NotNull final String relativePath) {
        Intrinsics.checkNotNullParameter((Object)relativePath, "relativePath");
        if (baseURL == null) {
            return relativePath;
        }
        String relativeUrl = relativePath;
        try {
            final URL parseUrl = new URL(baseURL, relativePath);
            final String string = parseUrl.toString();
            Intrinsics.checkNotNullExpressionValue((Object)string, "parseUrl.toString()");
            relativeUrl = string;
            return relativeUrl;
        }
        catch (final Exception e) {
            e.printStackTrace();
            return relativeUrl;
        }
    }
    
    @Nullable
    public final String getBaseUrl(@Nullable final String url) {
        if (url == null || !StringsKt.startsWith$default(url, "http", false, 2, (Object)null)) {
            return null;
        }
        final int index = StringsKt.indexOf$default((CharSequence)url, "/", 9, false, 4, (Object)null);
        String substring;
        if (index == -1) {
            substring = url;
        }
        else {
            Intrinsics.checkNotNullExpressionValue((Object)(substring = url.substring(0, index)), "(this as java.lang.Strin\u2026ing(startIndex, endIndex)");
        }
        return substring;
    }
    
    @NotNull
    public final String getSubDomain(@Nullable final String url) {
        final String baseUrl2 = this.getBaseUrl(url);
        if (baseUrl2 == null) {
            return "";
        }
        final String baseUrl = baseUrl2;
        String s;
        if (StringsKt.indexOf$default((CharSequence)baseUrl, ".", 0, false, 6, (Object)null) == StringsKt.lastIndexOf$default((CharSequence)baseUrl, ".", 0, false, 6, (Object)null)) {
            Intrinsics.checkNotNullExpressionValue((Object)(s = baseUrl.substring(StringsKt.lastIndexOf$default((CharSequence)baseUrl, "/", 0, false, 6, (Object)null) + 1)), "(this as java.lang.String).substring(startIndex)");
        }
        else {
            Intrinsics.checkNotNullExpressionValue((Object)(s = baseUrl.substring(StringsKt.indexOf$default((CharSequence)baseUrl, ".", 0, false, 6, (Object)null) + 1)), "(this as java.lang.String).substring(startIndex)");
        }
        return s;
    }
    
    @Nullable
    public final InetAddress getLocalIPAddress() {
        Enumeration enumeration = null;
        try {
            enumeration = NetworkInterface.getNetworkInterfaces();
        }
        catch (final SocketException e) {
            e.printStackTrace();
        }
        if (enumeration != null) {
            while (enumeration.hasMoreElements()) {
                final NetworkInterface nif = enumeration.nextElement();
                final Enumeration addresses = nif.getInetAddresses();
                if (addresses != null) {
                    while (addresses.hasMoreElements()) {
                        final InetAddress address = addresses.nextElement();
                        if (!address.isLoopbackAddress()) {
                            final String hostAddress = address.getHostAddress();
                            Intrinsics.checkNotNullExpressionValue((Object)hostAddress, "address.hostAddress");
                            if (this.isIPv4Address(hostAddress)) {
                                return address;
                            }
                            continue;
                        }
                    }
                }
            }
        }
        return null;
    }
    
    public final boolean isIPv4Address(@NotNull final String input) {
        Intrinsics.checkNotNullParameter((Object)input, "input");
        return NetworkUtils.IPV4_PATTERN.matcher(input).matches();
    }
    
    static {
        INSTANCE = new NetworkUtils();
        notNeedEncoding$delegate = LazyKt.lazy((Function0)NetworkUtils$notNeedEncoding.NetworkUtils$notNeedEncoding$2.INSTANCE);
        IPV4_PATTERN = Pattern.compile("^(([0-9]|[1-9][0-9]|1[0-9]{2}|2[0-4][0-9]|25[0-5])\\.){3}([0-9]|[1-9][0-9]|1[0-9]{2}|2[0-4][0-9]|25[0-5])$");
    }
}
