package io.legado.app.utils;

import io.legado.app.constant.RSSKeywords;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.net.URL;
import java.util.BitSet;
import java.util.Enumeration;
import java.util.regex.Pattern;
import kotlin.Lazy;
import kotlin.LazyKt;
import kotlin.Metadata;
import kotlin.jvm.internal.Intrinsics;
import kotlin.text.StringsKt;
import me.ag2s.epublib.domain.TableOfContents;
import me.ag2s.epublib.epub.PackageDocumentBase;
import okhttp3.HttpUrl;
import okhttp3.Request;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import retrofit2.Response;

/* JADX INFO: compiled from: NetworkUtils.kt */
/* JADX INFO: loaded from: reader-pro-classes-3.2.14.jar:io/legado/app/utils/NetworkUtils.class */
@Metadata(mv = {1, 5, 1}, k = 1, xi = 48, d1 = {"\u0000H\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0005\n\u0002\u0010\u000e\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0004\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u000b\n\u0002\b\u0003\n\u0002\u0010\f\n\u0002\b\u0003\bÆ\u0002\u0018\u00002\u00020\u0001B\u0007\b\u0002¢\u0006\u0002\u0010\u0002J\u0018\u0010\f\u001a\u00020\r2\b\u0010\u000e\u001a\u0004\u0018\u00010\u000f2\u0006\u0010\u0010\u001a\u00020\rJ\u0018\u0010\f\u001a\u00020\r2\b\u0010\u000e\u001a\u0004\u0018\u00010\r2\u0006\u0010\u0010\u001a\u00020\rJ\u0012\u0010\u0011\u001a\u0004\u0018\u00010\r2\b\u0010\u0012\u001a\u0004\u0018\u00010\rJ\b\u0010\u0013\u001a\u0004\u0018\u00010\u0014J\u0010\u0010\u0015\u001a\u00020\r2\b\u0010\u0012\u001a\u0004\u0018\u00010\rJ\u0012\u0010\u0016\u001a\u00020\r2\n\u0010\u0017\u001a\u0006\u0012\u0002\b\u00030\u0018J\u000e\u0010\u0019\u001a\u00020\u001a2\u0006\u0010\u001b\u001a\u00020\rJ\u0010\u0010\u001c\u001a\u00020\u001a2\u0006\u0010\u001d\u001a\u00020\u001eH\u0002J\u000e\u0010\u001f\u001a\u00020\u001a2\u0006\u0010 \u001a\u00020\rR\u0016\u0010\u0003\u001a\n \u0005*\u0004\u0018\u00010\u00040\u0004X\u0082\u0004¢\u0006\u0002\n\u0000R\u001b\u0010\u0006\u001a\u00020\u00078BX\u0082\u0084\u0002¢\u0006\f\n\u0004\b\n\u0010\u000b\u001a\u0004\b\b\u0010\t¨\u0006!"}, d2 = {"Lio/legado/app/utils/NetworkUtils;", PackageDocumentBase.PREFIX_OPF, "()V", "IPV4_PATTERN", "Ljava/util/regex/Pattern;", "kotlin.jvm.PlatformType", "notNeedEncoding", "Ljava/util/BitSet;", "getNotNeedEncoding", "()Ljava/util/BitSet;", "notNeedEncoding$delegate", "Lkotlin/Lazy;", "getAbsoluteURL", PackageDocumentBase.PREFIX_OPF, "baseURL", "Ljava/net/URL;", "relativePath", "getBaseUrl", RSSKeywords.RSS_ITEM_URL, "getLocalIPAddress", "Ljava/net/InetAddress;", "getSubDomain", "getUrl", "response", "Lretrofit2/Response;", "hasUrlEncoded", PackageDocumentBase.PREFIX_OPF, "str", "isDigit16Char", "c", PackageDocumentBase.PREFIX_OPF, "isIPv4Address", "input", "reader-pro"})
public final class NetworkUtils {

    @NotNull
    public static final NetworkUtils INSTANCE = new NetworkUtils();

    /* JADX INFO: renamed from: notNeedEncoding$delegate, reason: from kotlin metadata */
    @NotNull
    private static final Lazy notNeedEncoding = LazyKt.lazy(NetworkUtils$notNeedEncoding$2.INSTANCE);
    private static final Pattern IPV4_PATTERN = Pattern.compile("^(([0-9]|[1-9][0-9]|1[0-9]{2}|2[0-4][0-9]|25[0-5])\\.){3}([0-9]|[1-9][0-9]|1[0-9]{2}|2[0-4][0-9]|25[0-5])$");

    private NetworkUtils() {
    }

    @NotNull
    public final String getUrl(@NotNull Response<?> response) {
        Request request;
        HttpUrl httpUrlUrl;
        Intrinsics.checkNotNullParameter(response, "response");
        okhttp3.Response networkResponse = response.raw().networkResponse();
        String string = (networkResponse == null || (request = networkResponse.request()) == null || (httpUrlUrl = request.url()) == null) ? null : httpUrlUrl.toString();
        String str = string;
        if (str != null) {
            return str;
        }
        return response.raw().request().url().toString();
    }

    private final BitSet getNotNeedEncoding() {
        return (BitSet) notNeedEncoding.getValue();
    }

    public final boolean hasUrlEncoded(@NotNull String str) {
        Intrinsics.checkNotNullParameter(str, "str");
        boolean needEncode = false;
        int i = 0;
        while (true) {
            int i2 = i;
            if (i2 >= str.length()) {
                break;
            }
            char c = str.charAt(i2);
            if (getNotNeedEncoding().get(c)) {
                i = i2 + 1;
            } else {
                if (c != '%' || i2 + 2 >= str.length()) {
                    break;
                }
                int i3 = i2 + 1;
                char c1 = str.charAt(i3);
                int i4 = i3 + 1;
                char c2 = str.charAt(i4);
                if (!isDigit16Char(c1) || !isDigit16Char(c2)) {
                    break;
                }
                i = i4 + 1;
            }
        }
        needEncode = true;
        return !needEncode;
    }

    private final boolean isDigit16Char(char c) {
        boolean z = '0' <= c && c <= '9';
        if (!z) {
            boolean z2 = 'A' <= c && c <= 'F';
            if (!z2) {
                boolean z3 = 'a' <= c && c <= 'f';
                if (!z3) {
                    return false;
                }
            }
        }
        return true;
    }

    @NotNull
    public final String getAbsoluteURL(@Nullable String baseURL, @NotNull String relativePath) {
        Intrinsics.checkNotNullParameter(relativePath, "relativePath");
        String str = baseURL;
        if (str == null || str.length() == 0) {
            return relativePath;
        }
        if (relativePath.length() == 0) {
            return baseURL;
        }
        String relativeUrl = relativePath;
        try {
            URL absoluteUrl = new URL(StringsKt.substringBefore$default(baseURL, ",", (String) null, 2, (Object) null));
            URL parseUrl = new URL(absoluteUrl, relativePath);
            String string = parseUrl.toString();
            Intrinsics.checkNotNullExpressionValue(string, "parseUrl.toString()");
            relativeUrl = string;
            return relativeUrl;
        } catch (Exception e) {
            e.printStackTrace();
            return relativeUrl;
        }
    }

    @NotNull
    public final String getAbsoluteURL(@Nullable URL baseURL, @NotNull String relativePath) {
        Intrinsics.checkNotNullParameter(relativePath, "relativePath");
        if (baseURL == null) {
            return relativePath;
        }
        String relativeUrl = relativePath;
        try {
            URL parseUrl = new URL(baseURL, relativePath);
            String string = parseUrl.toString();
            Intrinsics.checkNotNullExpressionValue(string, "parseUrl.toString()");
            relativeUrl = string;
            return relativeUrl;
        } catch (Exception e) {
            e.printStackTrace();
            return relativeUrl;
        }
    }

    @Nullable
    public final String getBaseUrl(@Nullable String url) {
        if (url == null || !StringsKt.startsWith$default(url, "http", false, 2, (Object) null)) {
            return null;
        }
        int index = StringsKt.indexOf$default(url, TableOfContents.DEFAULT_PATH_SEPARATOR, 9, false, 4, (Object) null);
        if (index == -1) {
            return url;
        }
        String strSubstring = url.substring(0, index);
        Intrinsics.checkNotNullExpressionValue(strSubstring, "(this as java.lang.Strin…ing(startIndex, endIndex)");
        return strSubstring;
    }

    @NotNull
    public final String getSubDomain(@Nullable String url) {
        String baseUrl = getBaseUrl(url);
        if (baseUrl == null) {
            return PackageDocumentBase.PREFIX_OPF;
        }
        if (StringsKt.indexOf$default(baseUrl, ".", 0, false, 6, (Object) null) == StringsKt.lastIndexOf$default(baseUrl, ".", 0, false, 6, (Object) null)) {
            String strSubstring = baseUrl.substring(StringsKt.lastIndexOf$default(baseUrl, TableOfContents.DEFAULT_PATH_SEPARATOR, 0, false, 6, (Object) null) + 1);
            Intrinsics.checkNotNullExpressionValue(strSubstring, "(this as java.lang.String).substring(startIndex)");
            return strSubstring;
        }
        String strSubstring2 = baseUrl.substring(StringsKt.indexOf$default(baseUrl, ".", 0, false, 6, (Object) null) + 1);
        Intrinsics.checkNotNullExpressionValue(strSubstring2, "(this as java.lang.String).substring(startIndex)");
        return strSubstring2;
    }

    @Nullable
    public final InetAddress getLocalIPAddress() {
        Enumeration<NetworkInterface> networkInterfaces = null;
        try {
            networkInterfaces = NetworkInterface.getNetworkInterfaces();
        } catch (SocketException e) {
            e.printStackTrace();
        }
        if (networkInterfaces != null) {
            while (networkInterfaces.hasMoreElements()) {
                NetworkInterface nif = networkInterfaces.nextElement();
                Enumeration<InetAddress> inetAddresses = nif.getInetAddresses();
                if (inetAddresses != null) {
                    while (inetAddresses.hasMoreElements()) {
                        InetAddress address = inetAddresses.nextElement();
                        if (!address.isLoopbackAddress()) {
                            String hostAddress = address.getHostAddress();
                            Intrinsics.checkNotNullExpressionValue(hostAddress, "address.hostAddress");
                            if (isIPv4Address(hostAddress)) {
                                return address;
                            }
                        }
                    }
                }
            }
            return null;
        }
        return null;
    }

    public final boolean isIPv4Address(@NotNull String input) {
        Intrinsics.checkNotNullParameter(input, "input");
        return IPV4_PATTERN.matcher(input).matches();
    }
}
