// 
// Decompiled by Procyon v0.6.0
// 

package io.legado.app.data.entities;

import org.jetbrains.annotations.Nullable;
import kotlin.jvm.internal.DefaultConstructorMarker;
import kotlin.jvm.internal.Intrinsics;
import org.jetbrains.annotations.NotNull;
import kotlin.Metadata;

@Metadata(mv = { 1, 5, 1 }, k = 1, xi = 48, d1 = { "\u0000\"\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0000\n\u0002\u0010\u000e\n\u0002\b\f\n\u0002\u0010\u000b\n\u0002\b\u0002\n\u0002\u0010\b\n\u0002\b\u0002\b\u0086\b\u0018\u00002\u00020\u0001B\u0019\u0012\b\b\u0002\u0010\u0002\u001a\u00020\u0003\u0012\b\b\u0002\u0010\u0004\u001a\u00020\u0003?\u0006\u0002\u0010\u0005J\t\u0010\f\u001a\u00020\u0003H\u00c6\u0003J\t\u0010\r\u001a\u00020\u0003H\u00c6\u0003J\u001d\u0010\u000e\u001a\u00020\u00002\b\b\u0002\u0010\u0002\u001a\u00020\u00032\b\b\u0002\u0010\u0004\u001a\u00020\u0003H\u00c6\u0001J\u0013\u0010\u000f\u001a\u00020\u00102\b\u0010\u0011\u001a\u0004\u0018\u00010\u0001H\u00d6\u0003J\t\u0010\u0012\u001a\u00020\u0013H\u00d6\u0001J\t\u0010\u0014\u001a\u00020\u0003H\u00d6\u0001R\u001a\u0010\u0004\u001a\u00020\u0003X\u0086\u000e?\u0006\u000e\n\u0000\u001a\u0004\b\u0006\u0010\u0007\"\u0004\b\b\u0010\tR\u001a\u0010\u0002\u001a\u00020\u0003X\u0086\u000e?\u0006\u000e\n\u0000\u001a\u0004\b\n\u0010\u0007\"\u0004\b\u000b\u0010\t¡§\u0006\u0015" }, d2 = { "Lio/legado/app/data/entities/Cookie;", "", "url", "", "cookie", "(Ljava/lang/String;Ljava/lang/String;)V", "getCookie", "()Ljava/lang/String;", "setCookie", "(Ljava/lang/String;)V", "getUrl", "setUrl", "component1", "component2", "copy", "equals", "", "other", "hashCode", "", "toString", "reader-pro" })
public final class Cookie
{
    @NotNull
    private String url;
    @NotNull
    private String cookie;
    
    public Cookie(@NotNull final String url, @NotNull final String cookie) {
        Intrinsics.checkNotNullParameter((Object)url, "url");
        Intrinsics.checkNotNullParameter((Object)cookie, "cookie");
        this.url = url;
        this.cookie = cookie;
    }
    
    @NotNull
    public final String getUrl() {
        return this.url;
    }
    
    public final void setUrl(@NotNull final String <set-?>) {
        Intrinsics.checkNotNullParameter((Object)<set-?>, "<set-?>");
        this.url = <set-?>;
    }
    
    @NotNull
    public final String getCookie() {
        return this.cookie;
    }
    
    public final void setCookie(@NotNull final String <set-?>) {
        Intrinsics.checkNotNullParameter((Object)<set-?>, "<set-?>");
        this.cookie = <set-?>;
    }
    
    @NotNull
    public final String component1() {
        return this.url;
    }
    
    @NotNull
    public final String component2() {
        return this.cookie;
    }
    
    @NotNull
    public final Cookie copy(@NotNull final String url, @NotNull final String cookie) {
        Intrinsics.checkNotNullParameter((Object)url, "url");
        Intrinsics.checkNotNullParameter((Object)cookie, "cookie");
        return new Cookie(url, cookie);
    }
    
    @NotNull
    @Override
    public String toString() {
        return "Cookie(url=" + this.url + ", cookie=" + this.cookie + ')';
    }
    
    @Override
    public int hashCode() {
        int result = this.url.hashCode();
        result = result * 31 + this.cookie.hashCode();
        return result;
    }
    
    @Override
    public boolean equals(@Nullable final Object other) {
        if (this == other) {
            return true;
        }
        if (!(other instanceof Cookie)) {
            return false;
        }
        final Cookie cookie = (Cookie)other;
        return Intrinsics.areEqual((Object)this.url, (Object)cookie.url) && Intrinsics.areEqual((Object)this.cookie, (Object)cookie.cookie);
    }
    
    public Cookie() {
        this(null, null, 3, null);
    }
}
