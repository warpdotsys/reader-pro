// 
// Decompiled by Procyon v0.6.0
// 

package io.legado.app.help.http;

import io.legado.app.utils.GsonExtensionsKt;
import okhttp3.RequestBody$Companion;
import java.io.File;
import okhttp3.MultipartBody$Builder;
import okhttp3.RequestBody;
import kotlin.jvm.internal.DefaultConstructorMarker;
import okhttp3.FormBody$Builder;
import okhttp3.HttpUrl$Builder;
import okhttp3.HttpUrl;
import java.util.Iterator;
import java.util.Map;
import io.legado.app.utils.EncodingDetect;
import java.nio.charset.Charset;
import okhttp3.MediaType;
import io.legado.app.utils.Utf8BomUtils;
import kotlin.coroutines.jvm.internal.DebugProbesKt;
import okhttp3.Callback;
import kotlinx.coroutines.CancellableContinuation;
import kotlinx.coroutines.CancellableContinuationImpl;
import kotlinx.coroutines.JobKt;
import okhttp3.Call;
import kotlin.jvm.internal.Intrinsics;
import java.io.IOException;
import kotlin.ResultKt;
import kotlin.coroutines.intrinsics.IntrinsicsKt;
import okhttp3.ResponseBody;
import org.jetbrains.annotations.Nullable;
import kotlinx.coroutines.BuildersKt;
import kotlin.jvm.functions.Function2;
import kotlinx.coroutines.Dispatchers;
import kotlin.coroutines.CoroutineContext;
import okhttp3.Response;
import kotlin.coroutines.Continuation;
import kotlin.Unit;
import okhttp3.Request$Builder;
import kotlin.jvm.functions.Function1;
import org.jetbrains.annotations.NotNull;
import okhttp3.OkHttpClient;
import kotlin.Metadata;

@Metadata(mv = { 1, 5, 1 }, k = 2, xi = 48, d1 = { "\u0000T\n\u0000\n\u0002\u0010\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010$\n\u0002\u0010\u000e\n\u0000\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0005\n\u0002\u0010\u000b\n\u0000\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\b\n\u0000\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0004\n\u0002\u0018\u0002\n\u0002\b\u0006\n\u0002\u0010\u0000\n\u0002\b\u0003\u001a\u001e\u0010\u0000\u001a\u00020\u0001*\u00020\u00022\u0012\u0010\u0003\u001a\u000e\u0012\u0004\u0012\u00020\u0005\u0012\u0004\u0012\u00020\u00050\u0004\u001a\u0015\u0010\u0006\u001a\u00020\u0007*\u00020\bH\u0086@\u00f8\u0001\u0000?\u0006\u0002\u0010\t\u001a0\u0010\n\u001a\u00020\u0001*\u00020\u00022\u0006\u0010\u000b\u001a\u00020\u00052\u0012\u0010\f\u001a\u000e\u0012\u0004\u0012\u00020\u0005\u0012\u0004\u0012\u00020\u00050\u00042\b\b\u0002\u0010\r\u001a\u00020\u000e\u001a8\u0010\u000f\u001a\u00020\u0010*\u00020\u00112\b\b\u0002\u0010\u0012\u001a\u00020\u00132\u0017\u0010\u0014\u001a\u0013\u0012\u0004\u0012\u00020\u0002\u0012\u0004\u0012\u00020\u00010\u0015?\u0006\u0002\b\u0016H\u0086@\u00f8\u0001\u0000?\u0006\u0002\u0010\u0017\u001a8\u0010\u0018\u001a\u00020\u0007*\u00020\u00112\b\b\u0002\u0010\u0012\u001a\u00020\u00132\u0017\u0010\u0014\u001a\u0013\u0012\u0004\u0012\u00020\u0002\u0012\u0004\u0012\u00020\u00010\u0015?\u0006\u0002\b\u0016H\u0086@\u00f8\u0001\u0000?\u0006\u0002\u0010\u0017\u001a8\u0010\u0019\u001a\u00020\u0010*\u00020\u00112\b\b\u0002\u0010\u0012\u001a\u00020\u00132\u0017\u0010\u0014\u001a\u0013\u0012\u0004\u0012\u00020\u0002\u0012\u0004\u0012\u00020\u00010\u0015?\u0006\u0002\b\u0016H\u0086@\u00f8\u0001\u0000?\u0006\u0002\u0010\u0017\u001a8\u0010\u001a\u001a\u00020\u001b*\u00020\u00112\b\b\u0002\u0010\u0012\u001a\u00020\u00132\u0017\u0010\u0014\u001a\u0013\u0012\u0004\u0012\u00020\u0002\u0012\u0004\u0012\u00020\u00010\u0015?\u0006\u0002\b\u0016H\u0086@\u00f8\u0001\u0000?\u0006\u0002\u0010\u0017\u001a(\u0010\u001c\u001a\u00020\u0001*\u00020\u00022\u0012\u0010\u001d\u001a\u000e\u0012\u0004\u0012\u00020\u0005\u0012\u0004\u0012\u00020\u00050\u00042\b\b\u0002\u0010\r\u001a\u00020\u000e\u001a\u0014\u0010\u001e\u001a\u00020\u0001*\u00020\u00022\b\u0010\u001f\u001a\u0004\u0018\u00010\u0005\u001a(\u0010 \u001a\u00020\u0001*\u00020\u00022\b\u0010!\u001a\u0004\u0018\u00010\u00052\u0012\u0010\u001d\u001a\u000e\u0012\u0004\u0012\u00020\u0005\u0012\u0004\u0012\u00020\"0\u0004\u001a\u0016\u0010#\u001a\u00020\u0005*\u00020\u00102\n\b\u0002\u0010$\u001a\u0004\u0018\u00010\u0005\u0082\u0002\u0004\n\u0002\b\u0019¡§\u0006%" }, d2 = { "addHeaders", "", "Lokhttp3/Request$Builder;", "headers", "", "", "await", "Lokhttp3/Response;", "Lokhttp3/Call;", "(Lokhttp3/Call;Lkotlin/coroutines/Continuation;)Ljava/lang/Object;", "get", "url", "queryMap", "encoded", "", "newCall", "Lokhttp3/ResponseBody;", "Lokhttp3/OkHttpClient;", "retry", "", "builder", "Lkotlin/Function1;", "Lkotlin/ExtensionFunctionType;", "(Lokhttp3/OkHttpClient;ILkotlin/jvm/functions/Function1;Lkotlin/coroutines/Continuation;)Ljava/lang/Object;", "newCallResponse", "newCallResponseBody", "newCallStrResponse", "Lio/legado/app/help/http/StrResponse;", "postForm", "form", "postJson", "json", "postMultipart", "type", "", "text", "encode", "reader-pro" })
public final class OkHttpUtilsKt
{
    @Nullable
    public static final Object newCallResponse(@NotNull final OkHttpClient $this$newCallResponse, final int retry, @NotNull final Function1<? super Request$Builder, Unit> builder, @NotNull final Continuation<? super Response> $completion) {
        return BuildersKt.withContext((CoroutineContext)Dispatchers.getIO(), (Function2)new OkHttpUtilsKt$newCallResponse.OkHttpUtilsKt$newCallResponse$2((Function1)builder, retry, $this$newCallResponse, (Continuation)null), (Continuation)$completion);
    }
    
    @Nullable
    public static final Object newCallResponseBody(@NotNull final OkHttpClient $this$newCallResponseBody, final int retry, @NotNull final Function1<? super Request$Builder, Unit> builder, @NotNull final Continuation<? super ResponseBody> $completion) {
        final Continuation $continuation;
        Label_0049: {
            if ($completion instanceof OkHttpUtilsKt$newCallResponseBody.OkHttpUtilsKt$newCallResponseBody$1) {
                final OkHttpUtilsKt$newCallResponseBody.OkHttpUtilsKt$newCallResponseBody$1 okHttpUtilsKt$newCallResponseBody$1 = (OkHttpUtilsKt$newCallResponseBody.OkHttpUtilsKt$newCallResponseBody$1)$completion;
                if ((okHttpUtilsKt$newCallResponseBody$1.label & Integer.MIN_VALUE) != 0x0) {
                    final OkHttpUtilsKt$newCallResponseBody.OkHttpUtilsKt$newCallResponseBody$1 okHttpUtilsKt$newCallResponseBody$2 = okHttpUtilsKt$newCallResponseBody$1;
                    okHttpUtilsKt$newCallResponseBody$2.label -= Integer.MIN_VALUE;
                    break Label_0049;
                }
            }
            $continuation = (Continuation)new OkHttpUtilsKt$newCallResponseBody.OkHttpUtilsKt$newCallResponseBody$1((Continuation)$completion);
        }
        final Object $result = ((OkHttpUtilsKt$newCallResponseBody.OkHttpUtilsKt$newCallResponseBody$1)$continuation).result;
        final Object coroutine_SUSPENDED = IntrinsicsKt.getCOROUTINE_SUSPENDED();
        Object callResponse = null;
        switch (((OkHttpUtilsKt$newCallResponseBody.OkHttpUtilsKt$newCallResponseBody$1)$continuation).label) {
            case 0: {
                ResultKt.throwOnFailure($result);
                final Continuation $completion2 = $continuation;
                ((OkHttpUtilsKt$newCallResponseBody.OkHttpUtilsKt$newCallResponseBody$1)$continuation).label = 1;
                if ((callResponse = newCallResponse($this$newCallResponseBody, retry, builder, (Continuation<? super Response>)$completion2)) == coroutine_SUSPENDED) {
                    return coroutine_SUSPENDED;
                }
                break;
            }
            case 1: {
                ResultKt.throwOnFailure($result);
                callResponse = $result;
                break;
            }
            default: {
                throw new IllegalStateException("call to 'resume' before 'invoke' with coroutine");
            }
        }
        final Response it = (Response)callResponse;
        final int n = 0;
        final ResponseBody body = it.body();
        if (body == null) {
            throw new IOException(it.message());
        }
        return body;
    }
    
    @Nullable
    public static final Object newCall(@NotNull OkHttpClient $this$newCall, int retry, @NotNull final Function1<? super Request$Builder, Unit> builder, @NotNull final Continuation<? super ResponseBody> $completion) {
        final Continuation $continuation;
        Label_0049: {
            if ($completion instanceof OkHttpUtilsKt$newCall.OkHttpUtilsKt$newCall$1) {
                final OkHttpUtilsKt$newCall.OkHttpUtilsKt$newCall$1 okHttpUtilsKt$newCall$1 = (OkHttpUtilsKt$newCall.OkHttpUtilsKt$newCall$1)$completion;
                if ((okHttpUtilsKt$newCall$1.label & Integer.MIN_VALUE) != 0x0) {
                    final OkHttpUtilsKt$newCall.OkHttpUtilsKt$newCall$1 okHttpUtilsKt$newCall$2 = okHttpUtilsKt$newCall$1;
                    okHttpUtilsKt$newCall$2.label -= Integer.MIN_VALUE;
                    break Label_0049;
                }
            }
            $continuation = (Continuation)new OkHttpUtilsKt$newCall.OkHttpUtilsKt$newCall$1((Continuation)$completion);
        }
        final Object $result = ((OkHttpUtilsKt$newCall.OkHttpUtilsKt$newCall$1)$continuation).result;
        final Object coroutine_SUSPENDED = IntrinsicsKt.getCOROUTINE_SUSPENDED();
        Response response = null;
        Label_0277: {
            while (true) {
                int i = 0;
                Object await = null;
                Label_0248: {
                    final Request$Builder requestBuilder;
                    int i$1 = 0;
                    switch (((OkHttpUtilsKt$newCall.OkHttpUtilsKt$newCall$1)$continuation).label) {
                        case 0: {
                            ResultKt.throwOnFailure($result);
                            requestBuilder = new Request$Builder();
                            builder.invoke((Object)requestBuilder);
                            response = null;
                            i$1 = 0;
                            if (i$1 <= retry) {
                                break;
                            }
                            break Label_0277;
                        }
                        case 1: {
                            i = ((OkHttpUtilsKt$newCall.OkHttpUtilsKt$newCall$1)$continuation).I$2;
                            i$1 = ((OkHttpUtilsKt$newCall.OkHttpUtilsKt$newCall$1)$continuation).I$1;
                            retry = ((OkHttpUtilsKt$newCall.OkHttpUtilsKt$newCall$1)$continuation).I$0;
                            final Request$Builder request$Builder = (Request$Builder)((OkHttpUtilsKt$newCall.OkHttpUtilsKt$newCall$1)$continuation).L$1;
                            $this$newCall = (OkHttpClient)((OkHttpUtilsKt$newCall.OkHttpUtilsKt$newCall$1)$continuation).L$0;
                            ResultKt.throwOnFailure($result);
                            await = $result;
                            break Label_0248;
                        }
                        default: {
                            throw new IllegalStateException("call to 'resume' before 'invoke' with coroutine");
                        }
                    }
                    i = i$1;
                    ++i$1;
                    final Call call = $this$newCall.newCall(requestBuilder.build());
                    final Continuation $completion2 = $continuation;
                    ((OkHttpUtilsKt$newCall.OkHttpUtilsKt$newCall$1)$continuation).L$0 = $this$newCall;
                    ((OkHttpUtilsKt$newCall.OkHttpUtilsKt$newCall$1)$continuation).L$1 = requestBuilder;
                    ((OkHttpUtilsKt$newCall.OkHttpUtilsKt$newCall$1)$continuation).I$0 = retry;
                    ((OkHttpUtilsKt$newCall.OkHttpUtilsKt$newCall$1)$continuation).I$1 = i$1;
                    ((OkHttpUtilsKt$newCall.OkHttpUtilsKt$newCall$1)$continuation).I$2 = i;
                    ((OkHttpUtilsKt$newCall.OkHttpUtilsKt$newCall$1)$continuation).label = 1;
                    if ((await = await(call, (Continuation<? super Response>)$completion2)) == coroutine_SUSPENDED) {
                        return coroutine_SUSPENDED;
                    }
                }
                response = (Response)await;
                if (response.isSuccessful()) {
                    final ResponseBody body = response.body();
                    Intrinsics.checkNotNull((Object)body);
                    return body;
                }
                if (i != retry) {
                    continue;
                }
                break;
            }
        }
        final Response response2 = response;
        Intrinsics.checkNotNull((Object)response2);
        final ResponseBody body2 = response2.body();
        if (body2 == null) {
            throw new IOException(response.message());
        }
        return body2;
    }
    
    @Nullable
    public static final Object newCallStrResponse(@NotNull OkHttpClient $this$newCallStrResponse, int retry, @NotNull final Function1<? super Request$Builder, Unit> builder, @NotNull final Continuation<? super StrResponse> $completion) {
        final Continuation $continuation;
        Label_0049: {
            if ($completion instanceof OkHttpUtilsKt$newCallStrResponse.OkHttpUtilsKt$newCallStrResponse$1) {
                final OkHttpUtilsKt$newCallStrResponse.OkHttpUtilsKt$newCallStrResponse$1 okHttpUtilsKt$newCallStrResponse$1 = (OkHttpUtilsKt$newCallStrResponse.OkHttpUtilsKt$newCallStrResponse$1)$completion;
                if ((okHttpUtilsKt$newCallStrResponse$1.label & Integer.MIN_VALUE) != 0x0) {
                    final OkHttpUtilsKt$newCallStrResponse.OkHttpUtilsKt$newCallStrResponse$1 okHttpUtilsKt$newCallStrResponse$2 = okHttpUtilsKt$newCallStrResponse$1;
                    okHttpUtilsKt$newCallStrResponse$2.label -= Integer.MIN_VALUE;
                    break Label_0049;
                }
            }
            $continuation = (Continuation)new OkHttpUtilsKt$newCallStrResponse.OkHttpUtilsKt$newCallStrResponse$1((Continuation)$completion);
        }
        final Object $result = ((OkHttpUtilsKt$newCallStrResponse.OkHttpUtilsKt$newCallStrResponse$1)$continuation).result;
        final Object coroutine_SUSPENDED = IntrinsicsKt.getCOROUTINE_SUSPENDED();
        Response response = null;
        Label_0302: {
            while (true) {
                final int i$2;
                Object await = null;
                Label_0258: {
                    final Request$Builder requestBuilder;
                    int i$1 = 0;
                    switch (((OkHttpUtilsKt$newCallStrResponse.OkHttpUtilsKt$newCallStrResponse$1)$continuation).label) {
                        case 0: {
                            ResultKt.throwOnFailure($result);
                            requestBuilder = new Request$Builder();
                            builder.invoke((Object)requestBuilder);
                            response = null;
                            i$1 = 0;
                            if (i$1 <= retry) {
                                break;
                            }
                            break Label_0302;
                        }
                        case 1: {
                            i$2 = ((OkHttpUtilsKt$newCallStrResponse.OkHttpUtilsKt$newCallStrResponse$1)$continuation).I$2;
                            i$1 = ((OkHttpUtilsKt$newCallStrResponse.OkHttpUtilsKt$newCallStrResponse$1)$continuation).I$1;
                            retry = ((OkHttpUtilsKt$newCallStrResponse.OkHttpUtilsKt$newCallStrResponse$1)$continuation).I$0;
                            final Request$Builder request$Builder = (Request$Builder)((OkHttpUtilsKt$newCallStrResponse.OkHttpUtilsKt$newCallStrResponse$1)$continuation).L$1;
                            $this$newCallStrResponse = (OkHttpClient)((OkHttpUtilsKt$newCallStrResponse.OkHttpUtilsKt$newCallStrResponse$1)$continuation).L$0;
                            ResultKt.throwOnFailure($result);
                            await = $result;
                            break Label_0258;
                        }
                        default: {
                            throw new IllegalStateException("call to 'resume' before 'invoke' with coroutine");
                        }
                    }
                    final int i = i$1;
                    ++i$1;
                    JobKt.ensureActive($continuation.getContext());
                    final Call call = $this$newCallStrResponse.newCall(requestBuilder.build());
                    final Continuation $completion2 = $continuation;
                    ((OkHttpUtilsKt$newCallStrResponse.OkHttpUtilsKt$newCallStrResponse$1)$continuation).L$0 = $this$newCallStrResponse;
                    ((OkHttpUtilsKt$newCallStrResponse.OkHttpUtilsKt$newCallStrResponse$1)$continuation).L$1 = requestBuilder;
                    ((OkHttpUtilsKt$newCallStrResponse.OkHttpUtilsKt$newCallStrResponse$1)$continuation).I$0 = retry;
                    ((OkHttpUtilsKt$newCallStrResponse.OkHttpUtilsKt$newCallStrResponse$1)$continuation).I$1 = i$1;
                    ((OkHttpUtilsKt$newCallStrResponse.OkHttpUtilsKt$newCallStrResponse$1)$continuation).I$2 = i;
                    ((OkHttpUtilsKt$newCallStrResponse.OkHttpUtilsKt$newCallStrResponse$1)$continuation).label = 1;
                    if ((await = await(call, (Continuation<? super Response>)$completion2)) == coroutine_SUSPENDED) {
                        return coroutine_SUSPENDED;
                    }
                }
                response = (Response)await;
                if (response.isSuccessful()) {
                    final Response rawResponse = response;
                    final ResponseBody body = response.body();
                    Intrinsics.checkNotNull((Object)body);
                    return new StrResponse(rawResponse, text$default(body, null, 1, null));
                }
                if (i$2 != retry) {
                    continue;
                }
                break;
            }
        }
        final Response rawResponse2 = response;
        Intrinsics.checkNotNull((Object)rawResponse2);
        final ResponseBody body2 = response.body();
        return new StrResponse(rawResponse2, (body2 == null) ? response.message() : text$default(body2, null, 1, null));
    }
    
    @Nullable
    public static final Object await(@NotNull final Call $this$await, @NotNull final Continuation<? super Response> $completion) {
        final int $i$f$suspendCancellableCoroutine = 0;
        final Continuation uCont$iv = $completion;
        final int n = 0;
        final CancellableContinuationImpl cancellable$iv = new CancellableContinuationImpl(IntrinsicsKt.intercepted(uCont$iv), 1);
        cancellable$iv.initCancellability();
        final CancellableContinuation block = (CancellableContinuation)cancellable$iv;
        final int n2 = 0;
        block.invokeOnCancellation((Function1)new OkHttpUtilsKt$await$2.OkHttpUtilsKt$await$2$1($this$await));
        $this$await.enqueue((Callback)new OkHttpUtilsKt$await$2.OkHttpUtilsKt$await$2$2(block));
        final Object result = cancellable$iv.getResult();
        if (result == IntrinsicsKt.getCOROUTINE_SUSPENDED()) {
            DebugProbesKt.probeCoroutineSuspended((Continuation)$completion);
        }
        return result;
    }
    
    @NotNull
    public static final String text(@NotNull final ResponseBody $this$text, @Nullable final String encode) {
        Intrinsics.checkNotNullParameter((Object)$this$text, "<this>");
        final byte[] responseBytes = Utf8BomUtils.INSTANCE.removeUTF8BOM($this$text.bytes());
        Object charsetName = null;
        final String s;
        charsetName = (s = encode);
        if (s == null) {
            final MediaType contentType = $this$text.contentType();
            if (contentType != null) {
                final Charset charset$default = MediaType.charset$default(contentType, (Charset)null, 1, (Object)null);
                if (charset$default != null) {
                    final Charset it = charset$default;
                    final int n = 0;
                    return new String(responseBytes, it);
                }
            }
            charsetName = EncodingDetect.INSTANCE.getHtmlEncode(responseBytes);
            final Charset forName = Charset.forName((String)charsetName);
            Intrinsics.checkNotNullExpressionValue((Object)forName, "forName(charsetName)");
            return new String(responseBytes, forName);
        }
        final String it2 = s;
        final int n2 = 0;
        final Charset forName2 = Charset.forName((String)charsetName);
        Intrinsics.checkNotNullExpressionValue((Object)forName2, "forName(charsetName)");
        return new String(responseBytes, forName2);
    }
    
    public static /* synthetic */ String text$default(final ResponseBody $this$text, String encode, final int n, final Object o) {
        if ((n & 0x1) != 0x0) {
            encode = null;
        }
        return text($this$text, encode);
    }
    
    public static final void addHeaders(@NotNull final Request$Builder $this$addHeaders, @NotNull final Map<String, String> headers) {
        Intrinsics.checkNotNullParameter((Object)$this$addHeaders, "<this>");
        Intrinsics.checkNotNullParameter((Object)headers, "headers");
        final Map $this$forEach$iv = headers;
        final int $i$f$forEach = 0;
        for (final Map.Entry it : $this$forEach$iv.entrySet()) {
            final Map.Entry element$iv = it;
            final int n = 0;
            $this$addHeaders.addHeader((String)it.getKey(), (String)it.getValue());
        }
    }
    
    public static final void get(@NotNull final Request$Builder $this$get, @NotNull final String url, @NotNull final Map<String, String> queryMap, final boolean encoded) {
        Intrinsics.checkNotNullParameter((Object)$this$get, "<this>");
        Intrinsics.checkNotNullParameter((Object)url, "url");
        Intrinsics.checkNotNullParameter((Object)queryMap, "queryMap");
        final HttpUrl$Builder httpBuilder = HttpUrl.Companion.get(url).newBuilder();
        final Map $this$forEach$iv = queryMap;
        final int $i$f$forEach = 0;
        for (final Map.Entry it : $this$forEach$iv.entrySet()) {
            final Map.Entry element$iv = it;
            final int n = 0;
            if (encoded) {
                httpBuilder.addEncodedQueryParameter((String)it.getKey(), (String)it.getValue());
            }
            else {
                httpBuilder.addQueryParameter((String)it.getKey(), (String)it.getValue());
            }
        }
        $this$get.url(httpBuilder.build());
    }
    
    public static final void postForm(@NotNull final Request$Builder $this$postForm, @NotNull final Map<String, String> form, final boolean encoded) {
        Intrinsics.checkNotNullParameter((Object)$this$postForm, "<this>");
        Intrinsics.checkNotNullParameter((Object)form, "form");
        final FormBody$Builder formBody = new FormBody$Builder((Charset)null, 1, (DefaultConstructorMarker)null);
        final Map $this$forEach$iv = form;
        final int $i$f$forEach = 0;
        for (final Map.Entry it : $this$forEach$iv.entrySet()) {
            final Map.Entry element$iv = it;
            final int n = 0;
            if (encoded) {
                formBody.addEncoded((String)it.getKey(), (String)it.getValue());
            }
            else {
                formBody.add((String)it.getKey(), (String)it.getValue());
            }
        }
        $this$postForm.post((RequestBody)formBody.build());
    }
    
    public static final void postMultipart(@NotNull final Request$Builder $this$postMultipart, @Nullable final String type, @NotNull final Map<String, ?> form) {
        Intrinsics.checkNotNullParameter((Object)$this$postMultipart, "<this>");
        Intrinsics.checkNotNullParameter((Object)form, "form");
        final MultipartBody$Builder multipartBody = new MultipartBody$Builder((String)null, 1, (DefaultConstructorMarker)null);
        if (type != null) {
            final String it = type;
            final int n = 0;
            multipartBody.setType(MediaType.Companion.get(type));
        }
        final Map $this$forEach$iv = form;
        final int $i$f$forEach = 0;
        for (final Map.Entry it2 : $this$forEach$iv.entrySet()) {
            final Map.Entry element$iv = it2;
            final int n2 = 0;
            final Object value = it2.getValue();
            if (value instanceof Map) {
                final String value2 = ((Map)value).get("fileName");
                if (value2 == null) {
                    throw new NullPointerException("null cannot be cast to non-null type kotlin.String");
                }
                final String fileName = value2;
                final Object file = ((Map)value).get("file");
                final String value3 = ((Map)value).get("contentType");
                final String s = (value3 instanceof String) ? value3 : null;
                final MediaType mediaType = (s == null) ? null : MediaType.Companion.get(s);
                final Object o = file;
                RequestBody requestBody2;
                if (o instanceof File) {
                    requestBody2 = RequestBody.Companion.create((File)file, mediaType);
                }
                else if (o instanceof byte[]) {
                    requestBody2 = RequestBody$Companion.create$default(RequestBody.Companion, (byte[])file, mediaType, 0, 0, 6, (Object)null);
                }
                else if (o instanceof String) {
                    requestBody2 = RequestBody.Companion.create((String)file, mediaType);
                }
                else {
                    final RequestBody$Companion companion = RequestBody.Companion;
                    final String json = GsonExtensionsKt.getGSON().toJson(file);
                    Intrinsics.checkNotNullExpressionValue((Object)json, "GSON.toJson(file)");
                    requestBody2 = companion.create(json, mediaType);
                }
                final RequestBody requestBody = requestBody2;
                multipartBody.addFormDataPart((String)it2.getKey(), fileName, requestBody);
            }
            else {
                multipartBody.addFormDataPart((String)it2.getKey(), it2.getValue().toString());
            }
        }
        $this$postMultipart.post((RequestBody)multipartBody.build());
    }
    
    public static final void postJson(@NotNull final Request$Builder $this$postJson, @Nullable final String json) {
        Intrinsics.checkNotNullParameter((Object)$this$postJson, "<this>");
        if (json != null) {
            final String it = json;
            final int n = 0;
            final RequestBody requestBody = RequestBody.Companion.create(json, MediaType.Companion.get("application/json; charset=UTF-8"));
            $this$postJson.post(requestBody);
        }
    }
}
