// 
// Decompiled by Procyon v0.6.0
// 

package com.htmake.reader.utils;

import java.util.Iterator;
import java.util.List;
import okhttp3.OkHttpClient;
import io.legado.app.help.http.CookieStore;
import io.legado.app.utils.NetworkUtils;
import kotlin.Unit;
import okhttp3.Request$Builder;
import io.legado.app.help.http.OkHttpUtilsKt;
import kotlin.jvm.functions.Function1;
import io.legado.app.help.http.HttpHelperKt;
import kotlin.collections.MapsKt;
import kotlin.TuplesKt;
import kotlin.Pair;
import kotlin.jvm.internal.Ref$ObjectRef;
import kotlin.ResultKt;
import kotlin.coroutines.intrinsics.IntrinsicsKt;
import io.legado.app.help.http.StrResponse;
import kotlin.coroutines.Continuation;
import io.legado.app.model.DebugLog;
import java.util.Map;
import org.jetbrains.annotations.Nullable;
import kotlin.jvm.internal.Intrinsics;
import org.jetbrains.annotations.NotNull;
import kotlin.Metadata;

@Metadata(mv = { 1, 5, 1 }, k = 1, xi = 48, d1 = { "\u0000<\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\b\u0002\n\u0002\u0010\u000e\n\u0002\b\u0005\n\u0002\u0018\u0002\n\u0002\b\u0005\n\u0002\u0010$\n\u0002\b\u0004\n\u0002\u0010\u000b\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\u0002\n\u0002\b\u0002\b\u00c6\u0002\u0018\u00002\u00020\u0001B\u0007\b\u0002?\u0006\u0002\u0010\u0002J?\u0001\u0010\t\u001a\u00020\n2\n\b\u0002\u0010\u000b\u001a\u0004\u0018\u00010\u00042\n\b\u0002\u0010\f\u001a\u0004\u0018\u00010\u00042\n\b\u0002\u0010\r\u001a\u0004\u0018\u00010\u00042\n\b\u0002\u0010\u000e\u001a\u0004\u0018\u00010\u00042\u0016\b\u0002\u0010\u000f\u001a\u0010\u0012\u0004\u0012\u00020\u0004\u0012\u0004\u0012\u00020\u0004\u0018\u00010\u00102\n\b\u0002\u0010\u0011\u001a\u0004\u0018\u00010\u00042\n\b\u0002\u0010\u0012\u001a\u0004\u0018\u00010\u00042\n\b\u0002\u0010\u0013\u001a\u0004\u0018\u00010\u00042\b\b\u0002\u0010\u0014\u001a\u00020\u00152\n\b\u0002\u0010\u0016\u001a\u0004\u0018\u00010\u00042\b\b\u0002\u0010\u0017\u001a\u00020\u00042\n\b\u0002\u0010\u0018\u001a\u0004\u0018\u00010\u0019H\u0086@\u00f8\u0001\u0000?\u0006\u0002\u0010\u001aJ\u000e\u0010\u001b\u001a\u00020\u001c2\u0006\u0010\u001d\u001a\u00020\u0004R\u001a\u0010\u0003\u001a\u00020\u0004X\u0086\u000e?\u0006\u000e\n\u0000\u001a\u0004\b\u0005\u0010\u0006\"\u0004\b\u0007\u0010\b\u0082\u0002\u0004\n\u0002\b\u0019¡§\u0006\u001e" }, d2 = { "Lcom/htmake/reader/utils/RemoteWebview;", "", "()V", "remoteWebviewApi", "", "getRemoteWebviewApi", "()Ljava/lang/String;", "setRemoteWebviewApi", "(Ljava/lang/String;)V", "getStrResponse", "Lio/legado/app/help/http/StrResponse;", "url", "html", "encode", "tag", "headerMap", "", "sourceRegex", "javaScript", "proxy", "post", "", "body", "userNameSpace", "debugLog", "Lio/legado/app/model/DebugLog;", "(Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;Ljava/util/Map;Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;ZLjava/lang/String;Ljava/lang/String;Lio/legado/app/model/DebugLog;Lkotlin/coroutines/Continuation;)Ljava/lang/Object;", "setRemoteApi", "", "remoteApi", "reader-pro" })
public final class RemoteWebview
{
    @NotNull
    public static final RemoteWebview INSTANCE;
    @NotNull
    private static String remoteWebviewApi;
    
    private RemoteWebview() {
    }
    
    @NotNull
    public final String getRemoteWebviewApi() {
        return RemoteWebview.remoteWebviewApi;
    }
    
    public final void setRemoteWebviewApi(@NotNull final String <set-?>) {
        Intrinsics.checkNotNullParameter((Object)<set-?>, "<set-?>");
        RemoteWebview.remoteWebviewApi = <set-?>;
    }
    
    public final void setRemoteApi(@NotNull final String remoteApi) {
        Intrinsics.checkNotNullParameter((Object)remoteApi, "remoteApi");
        RemoteWebview.remoteWebviewApi = remoteApi;
    }
    
    @Nullable
    public final Object getStrResponse(@Nullable String url, @Nullable final String html, @Nullable final String encode, @Nullable final String tag, @Nullable final Map<String, String> headerMap, @Nullable final String sourceRegex, @Nullable final String javaScript, @Nullable final String proxy, final boolean post, @Nullable final String body, @NotNull String userNameSpace, @Nullable final DebugLog debugLog, @NotNull final Continuation<? super StrResponse> $completion) {
        final Continuation $continuation;
        Label_0053: {
            if ($completion instanceof RemoteWebview$getStrResponse.RemoteWebview$getStrResponse$1) {
                final RemoteWebview$getStrResponse.RemoteWebview$getStrResponse$1 remoteWebview$getStrResponse$1 = (RemoteWebview$getStrResponse.RemoteWebview$getStrResponse$1)$completion;
                if ((remoteWebview$getStrResponse$1.label & Integer.MIN_VALUE) != 0x0) {
                    final RemoteWebview$getStrResponse.RemoteWebview$getStrResponse$1 remoteWebview$getStrResponse$2 = remoteWebview$getStrResponse$1;
                    remoteWebview$getStrResponse$2.label -= Integer.MIN_VALUE;
                    break Label_0053;
                }
            }
            $continuation = (Continuation)new RemoteWebview$getStrResponse.RemoteWebview$getStrResponse$1(this, (Continuation)$completion);
        }
        final Object $result = ((RemoteWebview$getStrResponse.RemoteWebview$getStrResponse$1)$continuation).result;
        final Object coroutine_SUSPENDED = IntrinsicsKt.getCOROUTINE_SUSPENDED();
        Object callStrResponse = null;
        switch (((RemoteWebview$getStrResponse.RemoteWebview$getStrResponse$1)$continuation).label) {
            case 0: {
                ResultKt.throwOnFailure($result);
                final CharSequence charSequence = this.getRemoteWebviewApi();
                if (charSequence == null || charSequence.length() == 0) {
                    throw new Exception("\u4e0d\u652f\u6301webview");
                }
                final Ref$ObjectRef requestBody = new Ref$ObjectRef();
                requestBody.element = ExtKt.jsonEncode$default(MapsKt.mapOf(new Pair[] { TuplesKt.to((Object)"url", (Object)url), TuplesKt.to((Object)"html", (Object)html), TuplesKt.to((Object)"headers", (Object)headerMap), TuplesKt.to((Object)"js_source", (Object)javaScript), TuplesKt.to((Object)"proxy", (Object)proxy), TuplesKt.to((Object)"http_method", (Object)(post ? "POST" : "GET")), TuplesKt.to((Object)"body", (Object)body), TuplesKt.to((Object)"encode", (Object)encode), TuplesKt.to((Object)"tag", (Object)tag), TuplesKt.to((Object)"sourceRegex", (Object)sourceRegex) }), false, 2, null);
                final Ref$ObjectRef remoteApi = new Ref$ObjectRef();
                remoteApi.element = Intrinsics.stringPlus(this.getRemoteWebviewApi(), (Object)"/render.html");
                final OkHttpClient proxyClient$default = HttpHelperKt.getProxyClient$default(null, debugLog, 1, null);
                final int retry = 0;
                final Function1 builder = (Function1)new RemoteWebview$getStrResponse$strResponse.RemoteWebview$getStrResponse$strResponse$1(remoteApi, requestBody);
                final Continuation $completion2 = $continuation;
                ((RemoteWebview$getStrResponse.RemoteWebview$getStrResponse$1)$continuation).L$0 = url;
                ((RemoteWebview$getStrResponse.RemoteWebview$getStrResponse$1)$continuation).L$1 = userNameSpace;
                ((RemoteWebview$getStrResponse.RemoteWebview$getStrResponse$1)$continuation).label = 1;
                if ((callStrResponse = OkHttpUtilsKt.newCallStrResponse(proxyClient$default, retry, (Function1<? super Request$Builder, Unit>)builder, (Continuation<? super StrResponse>)$completion2)) == coroutine_SUSPENDED) {
                    return coroutine_SUSPENDED;
                }
                break;
            }
            case 1: {
                userNameSpace = (String)((RemoteWebview$getStrResponse.RemoteWebview$getStrResponse$1)$continuation).L$1;
                url = (String)((RemoteWebview$getStrResponse.RemoteWebview$getStrResponse$1)$continuation).L$0;
                ResultKt.throwOnFailure($result);
                callStrResponse = $result;
                break;
            }
            default: {
                throw new IllegalStateException("call to 'resume' before 'invoke' with coroutine");
            }
        }
        final StrResponse strResponse = (StrResponse)callStrResponse;
        if (url != null) {
            final String domain = NetworkUtils.INSTANCE.getSubDomain(url);
            if (domain.length() > 0) {
                final List cookieList = strResponse.getRaw().headers("Set-Cookie");
                if (cookieList.size() > 0) {
                    final CookieStore cookieStore = new CookieStore(userNameSpace);
                    final Iterable $this$forEach$iv = cookieList;
                    final int $i$f$forEach = 0;
                    for (final Object element$iv : $this$forEach$iv) {
                        final String it = (String)element$iv;
                        final int n = 0;
                        cookieStore.replaceCookie(Intrinsics.stringPlus(domain, (Object)"_cookieJar"), it);
                    }
                }
            }
        }
        final String s = url;
        return new StrResponse((s == null) ? "" : s, strResponse.getBody());
    }
    
    static {
        INSTANCE = new RemoteWebview();
        RemoteWebview.remoteWebviewApi = "";
    }
}
