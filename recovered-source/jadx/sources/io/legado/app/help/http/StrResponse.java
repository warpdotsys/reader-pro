package io.legado.app.help.http;

import io.legado.app.constant.RSSKeywords;
import kotlin.Metadata;
import kotlin.jvm.internal.Intrinsics;
import me.ag2s.epublib.epub.NCXDocumentV3;
import me.ag2s.epublib.epub.PackageDocumentBase;
import okhttp3.Headers;
import okhttp3.Protocol;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.ResponseBody;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/* JADX INFO: compiled from: StrResponse.kt */
/* JADX INFO: loaded from: reader-pro-classes-3.2.14.jar:io/legado/app/help/http/StrResponse.class */
@Metadata(mv = {1, 5, 1}, k = 1, xi = 48, d1 = {"\u00004\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u000e\n\u0002\b\u0004\n\u0002\u0018\u0002\n\u0002\b\u000b\n\u0002\u0010\b\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u000b\n\u0002\b\u0003\u0018\u00002\u00020\u0001B\u0019\b\u0016\u0012\u0006\u0010\u0002\u001a\u00020\u0003\u0012\b\u0010\u0004\u001a\u0004\u0018\u00010\u0005¢\u0006\u0002\u0010\u0006B\u0019\b\u0016\u0012\u0006\u0010\u0007\u001a\u00020\u0005\u0012\b\u0010\u0004\u001a\u0004\u0018\u00010\u0005¢\u0006\u0002\u0010\bB\u0019\b\u0016\u0012\u0006\u0010\u0002\u001a\u00020\u0003\u0012\b\u0010\t\u001a\u0004\u0018\u00010\n¢\u0006\u0002\u0010\u000bJ\b\u0010\u0004\u001a\u0004\u0018\u00010\u0005J\u0006\u0010\u0015\u001a\u00020\u0016J\b\u0010\t\u001a\u0004\u0018\u00010\nJ\u0006\u0010\u0017\u001a\u00020\u0018J\u0006\u0010\u0019\u001a\u00020\u001aJ\u0006\u0010\u001b\u001a\u00020\u0005J\u0006\u0010\u0011\u001a\u00020\u0003J\b\u0010\u001c\u001a\u00020\u0005H\u0016J\u0006\u0010\u0007\u001a\u00020\u0005R\"\u0010\u0004\u001a\u0004\u0018\u00010\u00052\b\u0010\f\u001a\u0004\u0018\u00010\u0005@BX\u0086\u000e¢\u0006\b\n\u0000\u001a\u0004\b\r\u0010\u000eR\"\u0010\t\u001a\u0004\u0018\u00010\n2\b\u0010\f\u001a\u0004\u0018\u00010\n@BX\u0086\u000e¢\u0006\b\n\u0000\u001a\u0004\b\u000f\u0010\u0010R\u001e\u0010\u0011\u001a\u00020\u00032\u0006\u0010\f\u001a\u00020\u0003@BX\u0086\u000e¢\u0006\b\n\u0000\u001a\u0004\b\u0012\u0010\u0013R\u0011\u0010\u0007\u001a\u00020\u00058F¢\u0006\u0006\u001a\u0004\b\u0014\u0010\u000e¨\u0006\u001d"}, d2 = {"Lio/legado/app/help/http/StrResponse;", PackageDocumentBase.PREFIX_OPF, "rawResponse", "Lokhttp3/Response;", NCXDocumentV3.XHTMLTgs.body, PackageDocumentBase.PREFIX_OPF, "(Lokhttp3/Response;Ljava/lang/String;)V", RSSKeywords.RSS_ITEM_URL, "(Ljava/lang/String;Ljava/lang/String;)V", "errorBody", "Lokhttp3/ResponseBody;", "(Lokhttp3/Response;Lokhttp3/ResponseBody;)V", "<set-?>", "getBody", "()Ljava/lang/String;", "getErrorBody", "()Lokhttp3/ResponseBody;", "raw", "getRaw", "()Lokhttp3/Response;", "getUrl", "code", PackageDocumentBase.PREFIX_OPF, "headers", "Lokhttp3/Headers;", "isSuccessful", PackageDocumentBase.PREFIX_OPF, "message", "toString", "reader-pro"})
public final class StrResponse {

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

    public StrResponse(@NotNull Response rawResponse, @Nullable String body) {
        Intrinsics.checkNotNullParameter(rawResponse, "rawResponse");
        this.raw = rawResponse;
        this.body = body;
    }

    public StrResponse(@NotNull String url, @Nullable String body) {
        Intrinsics.checkNotNullParameter(url, RSSKeywords.RSS_ITEM_URL);
        this.raw = new Response.Builder().code(200).message("OK").protocol(Protocol.HTTP_1_1).request(new Request.Builder().url(url).build()).build();
        this.body = body;
    }

    public StrResponse(@NotNull Response rawResponse, @Nullable ResponseBody errorBody) {
        Intrinsics.checkNotNullParameter(rawResponse, "rawResponse");
        this.raw = rawResponse;
        this.errorBody = errorBody;
    }

    @NotNull
    public final Response raw() {
        return this.raw;
    }

    @NotNull
    public final String url() {
        Response it = this.raw.networkResponse();
        if (it != null) {
            return it.request().url().toString();
        }
        return this.raw.request().url().toString();
    }

    @NotNull
    public final String getUrl() {
        return url();
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
    public String toString() {
        return this.raw.toString();
    }
}
