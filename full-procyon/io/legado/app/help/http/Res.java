// 
// Decompiled by Procyon v0.6.0
// 

package io.legado.app.help.http;

import kotlin.jvm.internal.Intrinsics;
import org.jetbrains.annotations.Nullable;
import org.jetbrains.annotations.NotNull;
import kotlin.Metadata;

@Metadata(mv = { 1, 5, 1 }, k = 1, xi = 48, d1 = { "\u0000\"\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0000\n\u0002\u0010\u000e\n\u0002\b\t\n\u0002\u0010\u000b\n\u0002\b\u0002\n\u0002\u0010\b\n\u0002\b\u0002\b\u0086\b\u0018\u00002\u00020\u0001B\u0017\u0012\u0006\u0010\u0002\u001a\u00020\u0003\u0012\b\u0010\u0004\u001a\u0004\u0018\u00010\u0003?\u0006\u0002\u0010\u0005J\t\u0010\t\u001a\u00020\u0003H\u00c6\u0003J\u000b\u0010\n\u001a\u0004\u0018\u00010\u0003H\u00c6\u0003J\u001f\u0010\u000b\u001a\u00020\u00002\b\b\u0002\u0010\u0002\u001a\u00020\u00032\n\b\u0002\u0010\u0004\u001a\u0004\u0018\u00010\u0003H\u00c6\u0001J\u0013\u0010\f\u001a\u00020\r2\b\u0010\u000e\u001a\u0004\u0018\u00010\u0001H\u00d6\u0003J\t\u0010\u000f\u001a\u00020\u0010H\u00d6\u0001J\t\u0010\u0011\u001a\u00020\u0003H\u00d6\u0001R\u0013\u0010\u0004\u001a\u0004\u0018\u00010\u0003?\u0006\b\n\u0000\u001a\u0004\b\u0006\u0010\u0007R\u0011\u0010\u0002\u001a\u00020\u0003?\u0006\b\n\u0000\u001a\u0004\b\b\u0010\u0007¡§\u0006\u0012" }, d2 = { "Lio/legado/app/help/http/Res;", "", "url", "", "body", "(Ljava/lang/String;Ljava/lang/String;)V", "getBody", "()Ljava/lang/String;", "getUrl", "component1", "component2", "copy", "equals", "", "other", "hashCode", "", "toString", "reader-pro" })
public final class Res
{
    @NotNull
    private final String url;
    @Nullable
    private final String body;
    
    public Res(@NotNull final String url, @Nullable final String body) {
        Intrinsics.checkNotNullParameter((Object)url, "url");
        this.url = url;
        this.body = body;
    }
    
    @NotNull
    public final String getUrl() {
        return this.url;
    }
    
    @Nullable
    public final String getBody() {
        return this.body;
    }
    
    @NotNull
    public final String component1() {
        return this.url;
    }
    
    @Nullable
    public final String component2() {
        return this.body;
    }
    
    @NotNull
    public final Res copy(@NotNull final String url, @Nullable final String body) {
        Intrinsics.checkNotNullParameter((Object)url, "url");
        return new Res(url, body);
    }
    
    @NotNull
    @Override
    public String toString() {
        return "Res(url=" + this.url + ", body=" + (Object)this.body + ')';
    }
    
    @Override
    public int hashCode() {
        int result = this.url.hashCode();
        result = result * 31 + ((this.body == null) ? 0 : this.body.hashCode());
        return result;
    }
    
    @Override
    public boolean equals(@Nullable final Object other) {
        if (this == other) {
            return true;
        }
        if (!(other instanceof Res)) {
            return false;
        }
        final Res res = (Res)other;
        return Intrinsics.areEqual((Object)this.url, (Object)res.url) && Intrinsics.areEqual((Object)this.body, (Object)res.body);
    }
}
