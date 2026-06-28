package io.legado.app.data.entities;

import io.legado.app.constant.RSSKeywords;
import kotlin.Metadata;
import kotlin.jvm.internal.DefaultConstructorMarker;
import kotlin.jvm.internal.Intrinsics;
import me.ag2s.epublib.epub.PackageDocumentBase;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/* JADX INFO: compiled from: Cookie.kt */
/* JADX INFO: loaded from: reader-pro-classes-3.2.14.jar:io/legado/app/data/entities/Cookie.class */
@Metadata(mv = {1, 5, 1}, k = 1, xi = 48, d1 = {"\u0000\"\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0000\n\u0002\u0010\u000e\n\u0002\b\f\n\u0002\u0010\u000b\n\u0002\b\u0002\n\u0002\u0010\b\n\u0002\b\u0002\b\u0086\b\u0018\u00002\u00020\u0001B\u0019\u0012\b\b\u0002\u0010\u0002\u001a\u00020\u0003\u0012\b\b\u0002\u0010\u0004\u001a\u00020\u0003¢\u0006\u0002\u0010\u0005J\t\u0010\f\u001a\u00020\u0003HÆ\u0003J\t\u0010\r\u001a\u00020\u0003HÆ\u0003J\u001d\u0010\u000e\u001a\u00020\u00002\b\b\u0002\u0010\u0002\u001a\u00020\u00032\b\b\u0002\u0010\u0004\u001a\u00020\u0003HÆ\u0001J\u0013\u0010\u000f\u001a\u00020\u00102\b\u0010\u0011\u001a\u0004\u0018\u00010\u0001HÖ\u0003J\t\u0010\u0012\u001a\u00020\u0013HÖ\u0001J\t\u0010\u0014\u001a\u00020\u0003HÖ\u0001R\u001a\u0010\u0004\u001a\u00020\u0003X\u0086\u000e¢\u0006\u000e\n\u0000\u001a\u0004\b\u0006\u0010\u0007\"\u0004\b\b\u0010\tR\u001a\u0010\u0002\u001a\u00020\u0003X\u0086\u000e¢\u0006\u000e\n\u0000\u001a\u0004\b\n\u0010\u0007\"\u0004\b\u000b\u0010\t¨\u0006\u0015"}, d2 = {"Lio/legado/app/data/entities/Cookie;", PackageDocumentBase.PREFIX_OPF, RSSKeywords.RSS_ITEM_URL, PackageDocumentBase.PREFIX_OPF, "cookie", "(Ljava/lang/String;Ljava/lang/String;)V", "getCookie", "()Ljava/lang/String;", "setCookie", "(Ljava/lang/String;)V", "getUrl", "setUrl", "component1", "component2", "copy", "equals", PackageDocumentBase.PREFIX_OPF, "other", "hashCode", PackageDocumentBase.PREFIX_OPF, "toString", "reader-pro"})
public final /* data */ class Cookie {

    @NotNull
    private String url;

    @NotNull
    private String cookie;

    @NotNull
    public final String component1() {
        return this.url;
    }

    @NotNull
    public final String component2() {
        return this.cookie;
    }

    @NotNull
    public final Cookie copy(@NotNull String url, @NotNull String cookie) {
        Intrinsics.checkNotNullParameter(url, RSSKeywords.RSS_ITEM_URL);
        Intrinsics.checkNotNullParameter(cookie, "cookie");
        return new Cookie(url, cookie);
    }

    public static /* synthetic */ Cookie copy$default(Cookie cookie, String str, String str2, int i, Object obj) {
        if ((i & 1) != 0) {
            str = cookie.url;
        }
        if ((i & 2) != 0) {
            str2 = cookie.cookie;
        }
        return cookie.copy(str, str2);
    }

    @NotNull
    public String toString() {
        return "Cookie(url=" + this.url + ", cookie=" + this.cookie + ')';
    }

    public int hashCode() {
        int result = this.url.hashCode();
        return (result * 31) + this.cookie.hashCode();
    }

    public boolean equals(@Nullable Object other) {
        if (this == other) {
            return true;
        }
        if (!(other instanceof Cookie)) {
            return false;
        }
        Cookie cookie = (Cookie) other;
        return Intrinsics.areEqual(this.url, cookie.url) && Intrinsics.areEqual(this.cookie, cookie.cookie);
    }

    public Cookie() {
        this(null, null, 3, null);
    }

    public Cookie(@NotNull String url, @NotNull String cookie) {
        Intrinsics.checkNotNullParameter(url, RSSKeywords.RSS_ITEM_URL);
        Intrinsics.checkNotNullParameter(cookie, "cookie");
        this.url = url;
        this.cookie = cookie;
    }

    public /* synthetic */ Cookie(String str, String str2, int i, DefaultConstructorMarker defaultConstructorMarker) {
        this((i & 1) != 0 ? PackageDocumentBase.PREFIX_OPF : str, (i & 2) != 0 ? PackageDocumentBase.PREFIX_OPF : str2);
    }

    @NotNull
    public final String getUrl() {
        return this.url;
    }

    public final void setUrl(@NotNull String str) {
        Intrinsics.checkNotNullParameter(str, "<set-?>");
        this.url = str;
    }

    @NotNull
    public final String getCookie() {
        return this.cookie;
    }

    public final void setCookie(@NotNull String str) {
        Intrinsics.checkNotNullParameter(str, "<set-?>");
        this.cookie = str;
    }
}
