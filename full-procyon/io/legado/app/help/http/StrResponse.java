// 
// Decompiled by Procyon v0.6.0
// 

package io.legado.app.help.http;

import okhttp3.Headers;
import okhttp3.Request$Builder;
import okhttp3.Protocol;
import okhttp3.Response$Builder;
import kotlin.jvm.internal.Intrinsics;
import okhttp3.ResponseBody;
import org.jetbrains.annotations.Nullable;
import org.jetbrains.annotations.NotNull;
import okhttp3.Response;
import kotlin.Metadata;

@Metadata(mv = { 1, 5, 1 }, k = 1, xi = 48, d1 = { "\u00004\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u000e\n\u0002\b\u0004\n\u0002\u0018\u0002\n\u0002\b\u000b\n\u0002\u0010\b\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u000b\n\u0002\b\u0003\u0018\u00002\u00020\u0001B\u0019\b\u0016\u0012\u0006\u0010\u0002\u001a\u00020\u0003\u0012\b\u0010\u0004\u001a\u0004\u0018\u00010\u0005?\u0006\u0002\u0010\u0006B\u0019\b\u0016\u0012\u0006\u0010\u0007\u001a\u00020\u0005\u0012\b\u0010\u0004\u001a\u0004\u0018\u00010\u0005?\u0006\u0002\u0010\bB\u0019\b\u0016\u0012\u0006\u0010\u0002\u001a\u00020\u0003\u0012\b\u0010\t\u001a\u0004\u0018\u00010\n?\u0006\u0002\u0010\u000bJ\b\u0010\u0004\u001a\u0004\u0018\u00010\u0005J\u0006\u0010\u0015\u001a\u00020\u0016J\b\u0010\t\u001a\u0004\u0018\u00010\nJ\u0006\u0010\u0017\u001a\u00020\u0018J\u0006\u0010\u0019\u001a\u00020\u001aJ\u0006\u0010\u001b\u001a\u00020\u0005J\u0006\u0010\u0011\u001a\u00020\u0003J\b\u0010\u001c\u001a\u00020\u0005H\u0016J\u0006\u0010\u0007\u001a\u00020\u0005R\"\u0010\u0004\u001a\u0004\u0018\u00010\u00052\b\u0010\f\u001a\u0004\u0018\u00010\u0005@BX\u0086\u000e?\u0006\b\n\u0000\u001a\u0004\b\r\u0010\u000eR\"\u0010\t\u001a\u0004\u0018\u00010\n2\b\u0010\f\u001a\u0004\u0018\u00010\n@BX\u0086\u000e?\u0006\b\n\u0000\u001a\u0004\b\u000f\u0010\u0010R\u001e\u0010\u0011\u001a\u00020\u00032\u0006\u0010\f\u001a\u00020\u0003@BX\u0086\u000e?\u0006\b\n\u0000\u001a\u0004\b\u0012\u0010\u0013R\u0011\u0010\u0007\u001a\u00020\u00058F?\u0006\u0006\u001a\u0004\b\u0014\u0010\u000e¡§\u0006\u001d" }, d2 = { "Lio/legado/app/help/http/StrResponse;", "", "rawResponse", "Lokhttp3/Response;", "body", "", "(Lokhttp3/Response;Ljava/lang/String;)V", "url", "(Ljava/lang/String;Ljava/lang/String;)V", "errorBody", "Lokhttp3/ResponseBody;", "(Lokhttp3/Response;Lokhttp3/ResponseBody;)V", "<set-?>", "getBody", "()Ljava/lang/String;", "getErrorBody", "()Lokhttp3/ResponseBody;", "raw", "getRaw", "()Lokhttp3/Response;", "getUrl", "code", "", "headers", "Lokhttp3/Headers;", "isSuccessful", "", "message", "toString", "reader-pro" })
public final class StrResponse
{
    @NotNull
    private Response raw;
    @Nullable
    private String body;
    @Nullable
    private ResponseBody errorBody;
    
    @NotNull
    public final Response getRaw() {
        return this.raw;
    }
    
    @Nullable
    public final String getBody() {
        return this.body;
    }
    
    @Nullable
    public final ResponseBody getErrorBody() {
        return this.errorBody;
    }
    
    public StrResponse(@NotNull final Response rawResponse, @Nullable final String body) {
        Intrinsics.checkNotNullParameter((Object)rawResponse, "rawResponse");
        this.raw = rawResponse;
        this.body = body;
    }
    
    public StrResponse(@NotNull final String url, @Nullable final String body) {
        Intrinsics.checkNotNullParameter((Object)url, "url");
        this.raw = new Response$Builder().code(200).message("OK").protocol(Protocol.HTTP_1_1).request(new Request$Builder().url(url).build()).build();
        this.body = body;
    }
    
    public StrResponse(@NotNull final Response rawResponse, @Nullable final ResponseBody errorBody) {
        Intrinsics.checkNotNullParameter((Object)rawResponse, "rawResponse");
        this.raw = rawResponse;
        this.errorBody = errorBody;
    }
    
    @NotNull
    public final Response raw() {
        return this.raw;
    }
    
    @NotNull
    public final String url() {
        final Response networkResponse = this.raw.networkResponse();
        if (networkResponse == null) {
            return this.raw.request().url().toString();
        }
        final Response it = networkResponse;
        final int n = 0;
        return it.request().url().toString();
    }
    
    @NotNull
    public final String getUrl() {
        return this.url();
    }
    
    @Nullable
    public final String body() {
        return this.body;
    }
    
    public final int code() {
        return this.raw.code();
    }
    
    @NotNull
    public final String message() {
        return this.raw.message();
    }
    
    @NotNull
    public final Headers headers() {
        return this.raw.headers();
    }
    
    public final boolean isSuccessful() {
        return this.raw.isSuccessful();
    }
    
    @Nullable
    public final ResponseBody errorBody() {
        return this.errorBody;
    }
    
    @NotNull
    @Override
    public String toString() {
        return this.raw.toString();
    }
}
