package io.legado.app.help.http;

import io.legado.app.adapters.ReaderAdapterHelper;
import io.legado.app.constant.RSSKeywords;
import io.legado.app.help.http.api.CookieManager;
import io.legado.app.utils.ACache;
import io.legado.app.utils.NetworkUtils;
import io.legado.app.utils.TextUtils;
import java.io.File;
import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.ListIterator;
import java.util.Map;
import kotlin.Metadata;
import kotlin.collections.CollectionsKt;
import kotlin.jvm.internal.Intrinsics;
import kotlin.text.Regex;
import kotlin.text.StringsKt;
import me.ag2s.epublib.epub.PackageDocumentBase;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/* JADX INFO: compiled from: CookieStore.kt */
/* JADX INFO: loaded from: reader-pro-classes-3.2.14.jar:io/legado/app/help/http/CookieStore.class */
@Metadata(mv = {1, 5, 1}, k = 1, xi = 48, d1 = {"\u00000\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u000e\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0005\n\u0002\u0010\u0002\n\u0000\n\u0002\u0010%\n\u0002\b\u0007\n\u0002\u0010$\n\u0002\b\u0004\u0018\u00002\u00020\u0001B\r\u0012\u0006\u0010\u0002\u001a\u00020\u0003¢\u0006\u0002\u0010\u0004J\u0006\u0010\u000b\u001a\u00020\fJ\u001c\u0010\r\u001a\u000e\u0012\u0004\u0012\u00020\u0003\u0012\u0004\u0012\u00020\u00030\u000e2\u0006\u0010\u000f\u001a\u00020\u0003H\u0016J\u0010\u0010\u0010\u001a\u00020\u00032\u0006\u0010\u0011\u001a\u00020\u0003H\u0016J\u0016\u0010\u0012\u001a\u00020\u00032\u0006\u0010\u0011\u001a\u00020\u00032\u0006\u0010\u0013\u001a\u00020\u0003J \u0010\u0014\u001a\u0004\u0018\u00010\u00032\u0014\u0010\u0015\u001a\u0010\u0012\u0004\u0012\u00020\u0003\u0012\u0004\u0012\u00020\u0003\u0018\u00010\u0016H\u0016J\u0010\u0010\u0017\u001a\u00020\f2\u0006\u0010\u0011\u001a\u00020\u0003H\u0016J\u0018\u0010\u0018\u001a\u00020\f2\u0006\u0010\u0011\u001a\u00020\u00032\u0006\u0010\u000f\u001a\u00020\u0003H\u0016J\u001a\u0010\u0019\u001a\u00020\f2\u0006\u0010\u0011\u001a\u00020\u00032\b\u0010\u000f\u001a\u0004\u0018\u00010\u0003H\u0016R\u0011\u0010\u0005\u001a\u00020\u0006¢\u0006\b\n\u0000\u001a\u0004\b\u0007\u0010\bR\u0011\u0010\u0002\u001a\u00020\u0003¢\u0006\b\n\u0000\u001a\u0004\b\t\u0010\n¨\u0006\u001a"}, d2 = {"Lio/legado/app/help/http/CookieStore;", "Lio/legado/app/help/http/api/CookieManager;", "userNameSpace", PackageDocumentBase.PREFIX_OPF, "(Ljava/lang/String;)V", "cacheInstance", "Lio/legado/app/utils/ACache;", "getCacheInstance", "()Lio/legado/app/utils/ACache;", "getUserNameSpace", "()Ljava/lang/String;", "clear", PackageDocumentBase.PREFIX_OPF, "cookieToMap", PackageDocumentBase.PREFIX_OPF, "cookie", "getCookie", RSSKeywords.RSS_ITEM_URL, "getKey", "key", "mapToCookie", "cookieMap", PackageDocumentBase.PREFIX_OPF, "removeCookie", "replaceCookie", "setCookie", "reader-pro"})
public final class CookieStore implements CookieManager {

    @NotNull
    private final String userNameSpace;

    @NotNull
    private final ACache cacheInstance;

    public CookieStore(@NotNull String userNameSpace) {
        Intrinsics.checkNotNullParameter(userNameSpace, "userNameSpace");
        this.userNameSpace = userNameSpace;
        File cacheDir = new File(ReaderAdapterHelper.INSTANCE.getAdapter().getWorkDir("storage", "cache", "cookie", this.userNameSpace));
        this.cacheInstance = ACache.INSTANCE.get(cacheDir, 50000000L, 1000000);
    }

    @NotNull
    public final String getUserNameSpace() {
        return this.userNameSpace;
    }

    @NotNull
    public final ACache getCacheInstance() {
        return this.cacheInstance;
    }

    @Override // io.legado.app.help.http.api.CookieManager
    public void setCookie(@NotNull String url, @Nullable String cookie) {
        Intrinsics.checkNotNullParameter(url, RSSKeywords.RSS_ITEM_URL);
        String domain = NetworkUtils.INSTANCE.getSubDomain(url);
        if (domain.length() > 0) {
            this.cacheInstance.put(domain, cookie == null ? PackageDocumentBase.PREFIX_OPF : cookie);
        }
    }

    @Override // io.legado.app.help.http.api.CookieManager
    public void replaceCookie(@NotNull String url, @NotNull String cookie) {
        Intrinsics.checkNotNullParameter(url, RSSKeywords.RSS_ITEM_URL);
        Intrinsics.checkNotNullParameter(cookie, "cookie");
        if (TextUtils.isEmpty(url) || TextUtils.isEmpty(cookie)) {
            return;
        }
        String oldCookie = getCookie(url);
        if (TextUtils.isEmpty(oldCookie)) {
            setCookie(url, cookie);
            return;
        }
        Map<String, String> mapCookieToMap = cookieToMap(oldCookie);
        mapCookieToMap.putAll(cookieToMap(cookie));
        String newCookie = mapToCookie(mapCookieToMap);
        setCookie(url, newCookie);
    }

    @Override // io.legado.app.help.http.api.CookieManager
    @NotNull
    public String getCookie(@NotNull String url) {
        String asString;
        Intrinsics.checkNotNullParameter(url, RSSKeywords.RSS_ITEM_URL);
        String domain = NetworkUtils.INSTANCE.getSubDomain(url);
        return ((domain.length() == 0) || (asString = this.cacheInstance.getAsString(domain)) == null) ? PackageDocumentBase.PREFIX_OPF : asString;
    }

    @NotNull
    public final String getKey(@NotNull String url, @NotNull String key) {
        Intrinsics.checkNotNullParameter(url, RSSKeywords.RSS_ITEM_URL);
        Intrinsics.checkNotNullParameter(key, "key");
        String cookie = getCookie(url);
        String str = cookieToMap(cookie).get(key);
        return str == null ? PackageDocumentBase.PREFIX_OPF : str;
    }

    @Override // io.legado.app.help.http.api.CookieManager
    public void removeCookie(@NotNull String url) {
        Intrinsics.checkNotNullParameter(url, RSSKeywords.RSS_ITEM_URL);
        String domain = NetworkUtils.INSTANCE.getSubDomain(url);
        if (domain.length() == 0) {
            return;
        }
        this.cacheInstance.remove(domain);
    }

    @Override // io.legado.app.help.http.api.CookieManager
    @NotNull
    public Map<String, String> cookieToMap(@NotNull String cookie) {
        Collection collectionEmptyList;
        Collection collectionEmptyList2;
        Intrinsics.checkNotNullParameter(cookie, "cookie");
        Map cookieMap = new LinkedHashMap();
        if (StringsKt.isBlank(cookie)) {
            return cookieMap;
        }
        List $this$dropLastWhile$iv = new Regex(";").split(cookie, 0);
        if ($this$dropLastWhile$iv.isEmpty()) {
            collectionEmptyList = CollectionsKt.emptyList();
        } else {
            ListIterator iterator$iv = $this$dropLastWhile$iv.listIterator($this$dropLastWhile$iv.size());
            while (iterator$iv.hasPrevious()) {
                String it = (String) iterator$iv.previous();
                if (!(it.length() == 0)) {
                    collectionEmptyList = CollectionsKt.take($this$dropLastWhile$iv, iterator$iv.nextIndex() + 1);
                    break;
                }
            }
            collectionEmptyList = CollectionsKt.emptyList();
        }
        Collection $this$toTypedArray$iv = collectionEmptyList;
        Object[] array = $this$toTypedArray$iv.toArray(new String[0]);
        if (array == null) {
            throw new NullPointerException("null cannot be cast to non-null type kotlin.Array<T>");
        }
        String[] pairArray = (String[]) array;
        int i = 0;
        int length = pairArray.length;
        while (i < length) {
            String pair = pairArray[i];
            i++;
            List $this$dropLastWhile$iv2 = new Regex("=").split(pair, 0);
            if ($this$dropLastWhile$iv2.isEmpty()) {
                collectionEmptyList2 = CollectionsKt.emptyList();
            } else {
                ListIterator iterator$iv2 = $this$dropLastWhile$iv2.listIterator($this$dropLastWhile$iv2.size());
                while (iterator$iv2.hasPrevious()) {
                    String it2 = (String) iterator$iv2.previous();
                    if (!(it2.length() == 0)) {
                        collectionEmptyList2 = CollectionsKt.take($this$dropLastWhile$iv2, iterator$iv2.nextIndex() + 1);
                        break;
                    }
                }
                collectionEmptyList2 = CollectionsKt.emptyList();
            }
            Collection $this$toTypedArray$iv2 = collectionEmptyList2;
            Object[] array2 = $this$toTypedArray$iv2.toArray(new String[0]);
            if (array2 == null) {
                throw new NullPointerException("null cannot be cast to non-null type kotlin.Array<T>");
            }
            CharSequence[] pairs = (String[]) array2;
            if (pairs.length != 1) {
                CharSequence $this$trim$iv = pairs[0];
                CharSequence $this$trim$iv$iv = $this$trim$iv;
                int startIndex$iv$iv = 0;
                int endIndex$iv$iv = $this$trim$iv$iv.length() - 1;
                boolean startFound$iv$iv = false;
                while (startIndex$iv$iv <= endIndex$iv$iv) {
                    int index$iv$iv = !startFound$iv$iv ? startIndex$iv$iv : endIndex$iv$iv;
                    char it3 = $this$trim$iv$iv.charAt(index$iv$iv);
                    boolean match$iv$iv = Intrinsics.compare(it3, 32) <= 0;
                    if (startFound$iv$iv) {
                        if (!match$iv$iv) {
                            break;
                        }
                        endIndex$iv$iv--;
                    } else if (match$iv$iv) {
                        startIndex$iv$iv++;
                    } else {
                        startFound$iv$iv = true;
                    }
                }
                String key = $this$trim$iv$iv.subSequence(startIndex$iv$iv, endIndex$iv$iv + 1).toString();
                CharSequence value = pairs[1];
                if (!(!StringsKt.isBlank(value))) {
                    CharSequence $this$trim$iv$iv2 = value;
                    int startIndex$iv$iv2 = 0;
                    int endIndex$iv$iv2 = $this$trim$iv$iv2.length() - 1;
                    boolean startFound$iv$iv2 = false;
                    while (startIndex$iv$iv2 <= endIndex$iv$iv2) {
                        int index$iv$iv2 = !startFound$iv$iv2 ? startIndex$iv$iv2 : endIndex$iv$iv2;
                        char it4 = $this$trim$iv$iv2.charAt(index$iv$iv2);
                        boolean match$iv$iv2 = Intrinsics.compare(it4, 32) <= 0;
                        if (startFound$iv$iv2) {
                            if (!match$iv$iv2) {
                                break;
                            }
                            endIndex$iv$iv2--;
                        } else if (match$iv$iv2) {
                            startIndex$iv$iv2++;
                        } else {
                            startFound$iv$iv2 = true;
                        }
                    }
                    if (Intrinsics.areEqual($this$trim$iv$iv2.subSequence(startIndex$iv$iv2, endIndex$iv$iv2 + 1).toString(), "null")) {
                    }
                }
                CharSequence $this$trim$iv$iv3 = value;
                int startIndex$iv$iv3 = 0;
                int endIndex$iv$iv3 = $this$trim$iv$iv3.length() - 1;
                boolean startFound$iv$iv3 = false;
                while (startIndex$iv$iv3 <= endIndex$iv$iv3) {
                    int index$iv$iv3 = !startFound$iv$iv3 ? startIndex$iv$iv3 : endIndex$iv$iv3;
                    char it5 = $this$trim$iv$iv3.charAt(index$iv$iv3);
                    boolean match$iv$iv3 = Intrinsics.compare(it5, 32) <= 0;
                    if (startFound$iv$iv3) {
                        if (!match$iv$iv3) {
                            break;
                        }
                        endIndex$iv$iv3--;
                    } else if (match$iv$iv3) {
                        startIndex$iv$iv3++;
                    } else {
                        startFound$iv$iv3 = true;
                    }
                }
                cookieMap.put(key, $this$trim$iv$iv3.subSequence(startIndex$iv$iv3, endIndex$iv$iv3 + 1).toString());
            }
        }
        return cookieMap;
    }

    @Override // io.legado.app.help.http.api.CookieManager
    @Nullable
    public String mapToCookie(@Nullable Map<String, String> cookieMap) {
        boolean z;
        if (cookieMap == null || cookieMap.isEmpty()) {
            return null;
        }
        StringBuilder builder = new StringBuilder();
        for (String key : cookieMap.keySet()) {
            String value = cookieMap.get(key);
            if (value == null) {
                z = false;
            } else {
                z = !StringsKt.isBlank(value);
            }
            if (z) {
                builder.append(key).append("=").append(value).append(";");
            }
        }
        return builder.deleteCharAt(builder.lastIndexOf(";")).toString();
    }

    public final void clear() {
        this.cacheInstance.clear();
    }
}
