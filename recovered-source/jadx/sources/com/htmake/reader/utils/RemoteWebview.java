package com.htmake.reader.utils;

import io.legado.app.constant.RSSKeywords;
import io.legado.app.help.http.CookieStore;
import io.legado.app.help.http.HttpHelperKt;
import io.legado.app.help.http.OkHttpUtilsKt;
import io.legado.app.help.http.StrResponse;
import io.legado.app.model.DebugLog;
import io.legado.app.utils.NetworkUtils;
import java.util.List;
import java.util.Map;
import kotlin.Metadata;
import kotlin.Pair;
import kotlin.ResultKt;
import kotlin.TuplesKt;
import kotlin.Unit;
import kotlin.collections.MapsKt;
import kotlin.coroutines.Continuation;
import kotlin.coroutines.intrinsics.IntrinsicsKt;
import kotlin.coroutines.jvm.internal.ContinuationImpl;
import kotlin.coroutines.jvm.internal.DebugMetadata;
import kotlin.jvm.functions.Function1;
import kotlin.jvm.internal.Intrinsics;
import kotlin.jvm.internal.Ref;
import me.ag2s.epublib.epub.NCXDocumentV3;
import me.ag2s.epublib.epub.PackageDocumentBase;
import okhttp3.Request;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.kxml2.wap.Wbxml;

/* JADX INFO: compiled from: RemoteWebview.kt */
/* JADX INFO: loaded from: app-classes.jar:com/htmake/reader/utils/RemoteWebview.class */
@Metadata(mv = {1, 5, 1}, k = 1, xi = 48, d1 = {"\u0000<\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\b\u0002\n\u0002\u0010\u000e\n\u0002\b\u0005\n\u0002\u0018\u0002\n\u0002\b\u0005\n\u0002\u0010$\n\u0002\b\u0004\n\u0002\u0010\u000b\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\u0002\n\u0002\b\u0002\bÆ\u0002\u0018\u00002\u00020\u0001B\u0007\b\u0002¢\u0006\u0002\u0010\u0002J©\u0001\u0010\t\u001a\u00020\n2\n\b\u0002\u0010\u000b\u001a\u0004\u0018\u00010\u00042\n\b\u0002\u0010\f\u001a\u0004\u0018\u00010\u00042\n\b\u0002\u0010\r\u001a\u0004\u0018\u00010\u00042\n\b\u0002\u0010\u000e\u001a\u0004\u0018\u00010\u00042\u0016\b\u0002\u0010\u000f\u001a\u0010\u0012\u0004\u0012\u00020\u0004\u0012\u0004\u0012\u00020\u0004\u0018\u00010\u00102\n\b\u0002\u0010\u0011\u001a\u0004\u0018\u00010\u00042\n\b\u0002\u0010\u0012\u001a\u0004\u0018\u00010\u00042\n\b\u0002\u0010\u0013\u001a\u0004\u0018\u00010\u00042\b\b\u0002\u0010\u0014\u001a\u00020\u00152\n\b\u0002\u0010\u0016\u001a\u0004\u0018\u00010\u00042\b\b\u0002\u0010\u0017\u001a\u00020\u00042\n\b\u0002\u0010\u0018\u001a\u0004\u0018\u00010\u0019H\u0086@ø\u0001\u0000¢\u0006\u0002\u0010\u001aJ\u000e\u0010\u001b\u001a\u00020\u001c2\u0006\u0010\u001d\u001a\u00020\u0004R\u001a\u0010\u0003\u001a\u00020\u0004X\u0086\u000e¢\u0006\u000e\n\u0000\u001a\u0004\b\u0005\u0010\u0006\"\u0004\b\u0007\u0010\b\u0082\u0002\u0004\n\u0002\b\u0019¨\u0006\u001e"}, d2 = {"Lcom/htmake/reader/utils/RemoteWebview;", PackageDocumentBase.PREFIX_OPF, "()V", "remoteWebviewApi", PackageDocumentBase.PREFIX_OPF, "getRemoteWebviewApi", "()Ljava/lang/String;", "setRemoteWebviewApi", "(Ljava/lang/String;)V", "getStrResponse", "Lio/legado/app/help/http/StrResponse;", RSSKeywords.RSS_ITEM_URL, "html", "encode", "tag", "headerMap", PackageDocumentBase.PREFIX_OPF, "sourceRegex", "javaScript", "proxy", "post", PackageDocumentBase.PREFIX_OPF, NCXDocumentV3.XHTMLTgs.body, "userNameSpace", "debugLog", "Lio/legado/app/model/DebugLog;", "(Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;Ljava/util/Map;Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;ZLjava/lang/String;Ljava/lang/String;Lio/legado/app/model/DebugLog;Lkotlin/coroutines/Continuation;)Ljava/lang/Object;", "setRemoteApi", PackageDocumentBase.PREFIX_OPF, "remoteApi", "reader-pro"})
public final class RemoteWebview {

    @NotNull
    public static final RemoteWebview INSTANCE = new RemoteWebview();

    @NotNull
    private static String remoteWebviewApi = PackageDocumentBase.PREFIX_OPF;

    /* JADX INFO: renamed from: com.htmake.reader.utils.RemoteWebview$getStrResponse$1, reason: invalid class name */
    /* JADX INFO: compiled from: RemoteWebview.kt */
    /* JADX INFO: loaded from: app-classes.jar:com/htmake/reader/utils/RemoteWebview$getStrResponse$1.class */
    @Metadata(mv = {1, 5, 1}, k = 3, xi = 48)
    @DebugMetadata(f = "RemoteWebview.kt", l = {47}, i = {}, s = {}, n = {}, m = "getStrResponse", c = "com.htmake.reader.utils.RemoteWebview")
    static final class AnonymousClass1 extends ContinuationImpl {
        Object L$0;
        Object L$1;
        /* synthetic */ Object result;
        int label;

        AnonymousClass1(Continuation<? super AnonymousClass1> $completion) {
            super($completion);
        }

        @Nullable
        public final Object invokeSuspend(@NotNull Object $result) {
            this.result = $result;
            this.label |= Integer.MIN_VALUE;
            return RemoteWebview.this.getStrResponse(null, null, null, null, null, null, null, null, false, null, null, null, (Continuation) this);
        }
    }

    private RemoteWebview() {
    }

    @NotNull
    public final String getRemoteWebviewApi() {
        return remoteWebviewApi;
    }

    public final void setRemoteWebviewApi(@NotNull String str) {
        Intrinsics.checkNotNullParameter(str, "<set-?>");
        remoteWebviewApi = str;
    }

    public final void setRemoteApi(@NotNull String remoteApi) {
        Intrinsics.checkNotNullParameter(remoteApi, "remoteApi");
        remoteWebviewApi = remoteApi;
    }

    /* JADX WARN: Removed duplicated region for block: B:7:0x0029  */
    @Nullable
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    public final Object getStrResponse(@Nullable String url, @Nullable String html, @Nullable String encode, @Nullable String tag, @Nullable Map<String, String> headerMap, @Nullable String sourceRegex, @Nullable String javaScript, @Nullable String proxy, boolean post, @Nullable String body, @NotNull String userNameSpace, @Nullable DebugLog debugLog, @NotNull Continuation<? super StrResponse> $completion) throws Exception {
        AnonymousClass1 anonymousClass1;
        Object objNewCallStrResponse;
        if ($completion instanceof AnonymousClass1) {
            anonymousClass1 = (AnonymousClass1) $completion;
            if ((anonymousClass1.label & Integer.MIN_VALUE) != 0) {
                anonymousClass1.label -= Integer.MIN_VALUE;
            } else {
                anonymousClass1 = new AnonymousClass1($completion);
            }
        }
        Object $result = anonymousClass1.result;
        Object coroutine_suspended = IntrinsicsKt.getCOROUTINE_SUSPENDED();
        switch (anonymousClass1.label) {
            case 0:
                ResultKt.throwOnFailure($result);
                String remoteWebviewApi2 = getRemoteWebviewApi();
                if (remoteWebviewApi2 == null || remoteWebviewApi2.length() == 0) {
                    throw new Exception("不支持webview");
                }
                final Ref.ObjectRef requestBody = new Ref.ObjectRef();
                Pair[] pairArr = new Pair[10];
                pairArr[0] = TuplesKt.to(RSSKeywords.RSS_ITEM_URL, url);
                pairArr[1] = TuplesKt.to("html", html);
                pairArr[2] = TuplesKt.to("headers", headerMap);
                pairArr[3] = TuplesKt.to("js_source", javaScript);
                pairArr[4] = TuplesKt.to("proxy", proxy);
                pairArr[5] = TuplesKt.to("http_method", post ? "POST" : "GET");
                pairArr[6] = TuplesKt.to(NCXDocumentV3.XHTMLTgs.body, body);
                pairArr[7] = TuplesKt.to("encode", encode);
                pairArr[8] = TuplesKt.to("tag", tag);
                pairArr[9] = TuplesKt.to("sourceRegex", sourceRegex);
                requestBody.element = ExtKt.jsonEncode$default(MapsKt.mapOf(pairArr), false, 2, null);
                final Ref.ObjectRef remoteApi = new Ref.ObjectRef();
                remoteApi.element = Intrinsics.stringPlus(getRemoteWebviewApi(), "/render.html");
                anonymousClass1.L$0 = url;
                anonymousClass1.L$1 = userNameSpace;
                anonymousClass1.label = 1;
                objNewCallStrResponse = OkHttpUtilsKt.newCallStrResponse(HttpHelperKt.getProxyClient$default(null, debugLog, 1, null), 0, new Function1<Request.Builder, Unit>() { // from class: com.htmake.reader.utils.RemoteWebview$getStrResponse$strResponse$1
                    /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
                    {
                        super(1);
                    }

                    public /* bridge */ /* synthetic */ Object invoke(Object p1) {
                        invoke((Request.Builder) p1);
                        return Unit.INSTANCE;
                    }

                    public final void invoke(@NotNull Request.Builder $this$newCallStrResponse) {
                        Intrinsics.checkNotNullParameter($this$newCallStrResponse, "$this$newCallStrResponse");
                        $this$newCallStrResponse.url((String) remoteApi.element);
                        OkHttpUtilsKt.postJson($this$newCallStrResponse, (String) requestBody.element);
                    }
                }, anonymousClass1);
                if (objNewCallStrResponse == coroutine_suspended) {
                    return coroutine_suspended;
                }
                break;
            case 1:
                userNameSpace = (String) anonymousClass1.L$1;
                url = (String) anonymousClass1.L$0;
                ResultKt.throwOnFailure($result);
                objNewCallStrResponse = $result;
                break;
            default:
                throw new IllegalStateException("call to 'resume' before 'invoke' with coroutine");
        }
        StrResponse strResponse = (StrResponse) objNewCallStrResponse;
        if (url != null) {
            String domain = NetworkUtils.INSTANCE.getSubDomain(url);
            if (domain.length() > 0) {
                List cookieList = strResponse.getRaw().headers("Set-Cookie");
                if (cookieList.size() > 0) {
                    CookieStore cookieStore = new CookieStore(userNameSpace);
                    List $this$forEach$iv = cookieList;
                    for (Object element$iv : $this$forEach$iv) {
                        String it = (String) element$iv;
                        cookieStore.replaceCookie(Intrinsics.stringPlus(domain, "_cookieJar"), it);
                    }
                }
            }
        }
        String str = url;
        return new StrResponse(str == null ? PackageDocumentBase.PREFIX_OPF : str, strResponse.getBody());
    }

    /* JADX WARN: Multi-variable type inference failed */
    public static /* synthetic */ Object getStrResponse$default(RemoteWebview remoteWebview, String str, String str2, String str3, String str4, Map map, String str5, String str6, String str7, boolean z, String str8, String str9, DebugLog debugLog, Continuation continuation, int i, Object obj) {
        if ((i & 1) != 0) {
            str = null;
        }
        if ((i & 2) != 0) {
            str2 = null;
        }
        if ((i & 4) != 0) {
            str3 = null;
        }
        if ((i & 8) != 0) {
            str4 = null;
        }
        if ((i & 16) != 0) {
            map = null;
        }
        if ((i & 32) != 0) {
            str5 = null;
        }
        if ((i & 64) != 0) {
            str6 = null;
        }
        if ((i & Wbxml.EXT_T_0) != 0) {
            str7 = null;
        }
        if ((i & 256) != 0) {
            z = false;
        }
        if ((i & 512) != 0) {
            str8 = null;
        }
        if ((i & 1024) != 0) {
            str9 = PackageDocumentBase.PREFIX_OPF;
        }
        if ((i & 2048) != 0) {
            debugLog = null;
        }
        return remoteWebview.getStrResponse(str, str2, str3, str4, map, str5, str6, str7, z, str8, str9, debugLog, continuation);
    }
}
