package io.legado.app.help.http;

import io.legado.app.constant.RSSKeywords;
import io.legado.app.utils.EncodingDetect;
import io.legado.app.utils.GsonExtensionsKt;
import io.legado.app.utils.Utf8BomUtils;
import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.util.Map;
import kotlin.Metadata;
import kotlin.ResultKt;
import kotlin.Unit;
import kotlin.coroutines.Continuation;
import kotlin.coroutines.intrinsics.IntrinsicsKt;
import kotlin.coroutines.jvm.internal.ContinuationImpl;
import kotlin.coroutines.jvm.internal.DebugMetadata;
import kotlin.coroutines.jvm.internal.DebugProbesKt;
import kotlin.coroutines.jvm.internal.SuspendLambda;
import kotlin.jvm.functions.Function1;
import kotlin.jvm.functions.Function2;
import kotlin.jvm.internal.DefaultConstructorMarker;
import kotlin.jvm.internal.Intrinsics;
import kotlinx.coroutines.BuildersKt;
import kotlinx.coroutines.CancellableContinuation;
import kotlinx.coroutines.CancellableContinuationImpl;
import kotlinx.coroutines.CoroutineScope;
import kotlinx.coroutines.Dispatchers;
import me.ag2s.epublib.epub.NCXDocumentV2;
import me.ag2s.epublib.epub.PackageDocumentBase;
import okhttp3.Call;
import okhttp3.FormBody;
import okhttp3.HttpUrl;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import okhttp3.ResponseBody;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/* JADX INFO: compiled from: OkHttpUtils.kt */
/* JADX INFO: loaded from: reader-pro-classes-3.2.14.jar:io/legado/app/help/http/OkHttpUtilsKt.class */
@Metadata(mv = {1, 5, 1}, k = 2, xi = 48, d1 = {"\u0000T\n\u0000\n\u0002\u0010\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010$\n\u0002\u0010\u000e\n\u0000\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0005\n\u0002\u0010\u000b\n\u0000\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\b\n\u0000\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0004\n\u0002\u0018\u0002\n\u0002\b\u0006\n\u0002\u0010\u0000\n\u0002\b\u0003\u001a\u001e\u0010\u0000\u001a\u00020\u0001*\u00020\u00022\u0012\u0010\u0003\u001a\u000e\u0012\u0004\u0012\u00020\u0005\u0012\u0004\u0012\u00020\u00050\u0004\u001a\u0015\u0010\u0006\u001a\u00020\u0007*\u00020\bH\u0086@ø\u0001\u0000¢\u0006\u0002\u0010\t\u001a0\u0010\n\u001a\u00020\u0001*\u00020\u00022\u0006\u0010\u000b\u001a\u00020\u00052\u0012\u0010\f\u001a\u000e\u0012\u0004\u0012\u00020\u0005\u0012\u0004\u0012\u00020\u00050\u00042\b\b\u0002\u0010\r\u001a\u00020\u000e\u001a8\u0010\u000f\u001a\u00020\u0010*\u00020\u00112\b\b\u0002\u0010\u0012\u001a\u00020\u00132\u0017\u0010\u0014\u001a\u0013\u0012\u0004\u0012\u00020\u0002\u0012\u0004\u0012\u00020\u00010\u0015¢\u0006\u0002\b\u0016H\u0086@ø\u0001\u0000¢\u0006\u0002\u0010\u0017\u001a8\u0010\u0018\u001a\u00020\u0007*\u00020\u00112\b\b\u0002\u0010\u0012\u001a\u00020\u00132\u0017\u0010\u0014\u001a\u0013\u0012\u0004\u0012\u00020\u0002\u0012\u0004\u0012\u00020\u00010\u0015¢\u0006\u0002\b\u0016H\u0086@ø\u0001\u0000¢\u0006\u0002\u0010\u0017\u001a8\u0010\u0019\u001a\u00020\u0010*\u00020\u00112\b\b\u0002\u0010\u0012\u001a\u00020\u00132\u0017\u0010\u0014\u001a\u0013\u0012\u0004\u0012\u00020\u0002\u0012\u0004\u0012\u00020\u00010\u0015¢\u0006\u0002\b\u0016H\u0086@ø\u0001\u0000¢\u0006\u0002\u0010\u0017\u001a8\u0010\u001a\u001a\u00020\u001b*\u00020\u00112\b\b\u0002\u0010\u0012\u001a\u00020\u00132\u0017\u0010\u0014\u001a\u0013\u0012\u0004\u0012\u00020\u0002\u0012\u0004\u0012\u00020\u00010\u0015¢\u0006\u0002\b\u0016H\u0086@ø\u0001\u0000¢\u0006\u0002\u0010\u0017\u001a(\u0010\u001c\u001a\u00020\u0001*\u00020\u00022\u0012\u0010\u001d\u001a\u000e\u0012\u0004\u0012\u00020\u0005\u0012\u0004\u0012\u00020\u00050\u00042\b\b\u0002\u0010\r\u001a\u00020\u000e\u001a\u0014\u0010\u001e\u001a\u00020\u0001*\u00020\u00022\b\u0010\u001f\u001a\u0004\u0018\u00010\u0005\u001a(\u0010 \u001a\u00020\u0001*\u00020\u00022\b\u0010!\u001a\u0004\u0018\u00010\u00052\u0012\u0010\u001d\u001a\u000e\u0012\u0004\u0012\u00020\u0005\u0012\u0004\u0012\u00020\"0\u0004\u001a\u0016\u0010#\u001a\u00020\u0005*\u00020\u00102\n\b\u0002\u0010$\u001a\u0004\u0018\u00010\u0005\u0082\u0002\u0004\n\u0002\b\u0019¨\u0006%"}, d2 = {"addHeaders", PackageDocumentBase.PREFIX_OPF, "Lokhttp3/Request$Builder;", "headers", PackageDocumentBase.PREFIX_OPF, PackageDocumentBase.PREFIX_OPF, "await", "Lokhttp3/Response;", "Lokhttp3/Call;", "(Lokhttp3/Call;Lkotlin/coroutines/Continuation;)Ljava/lang/Object;", "get", RSSKeywords.RSS_ITEM_URL, "queryMap", "encoded", PackageDocumentBase.PREFIX_OPF, "newCall", "Lokhttp3/ResponseBody;", "Lokhttp3/OkHttpClient;", "retry", PackageDocumentBase.PREFIX_OPF, "builder", "Lkotlin/Function1;", "Lkotlin/ExtensionFunctionType;", "(Lokhttp3/OkHttpClient;ILkotlin/jvm/functions/Function1;Lkotlin/coroutines/Continuation;)Ljava/lang/Object;", "newCallResponse", "newCallResponseBody", "newCallStrResponse", "Lio/legado/app/help/http/StrResponse;", "postForm", "form", "postJson", "json", "postMultipart", "type", PackageDocumentBase.PREFIX_OPF, NCXDocumentV2.NCXTags.text, "encode", "reader-pro"})
public final class OkHttpUtilsKt {

    /* JADX INFO: renamed from: io.legado.app.help.http.OkHttpUtilsKt$newCall$1, reason: invalid class name */
    /* JADX INFO: compiled from: OkHttpUtils.kt */
    /* JADX INFO: loaded from: reader-pro-classes-3.2.14.jar:io/legado/app/help/http/OkHttpUtilsKt$newCall$1.class */
    @Metadata(mv = {1, 5, 1}, k = 3, xi = 48)
    @DebugMetadata(f = "OkHttpUtils.kt", l = {59}, i = {0}, s = {"I$2"}, n = {"i"}, m = "newCall", c = "io.legado.app.help.http.OkHttpUtilsKt")
    static final class AnonymousClass1 extends ContinuationImpl {
        Object L$0;
        Object L$1;
        int I$0;
        int I$1;
        int I$2;
        /* synthetic */ Object result;
        int label;

        AnonymousClass1(Continuation<? super AnonymousClass1> $completion) {
            super($completion);
        }

        @Nullable
        public final Object invokeSuspend(@NotNull Object $result) {
            this.result = $result;
            this.label |= Integer.MIN_VALUE;
            return OkHttpUtilsKt.newCall(null, 0, null, (Continuation) this);
        }
    }

    /* JADX INFO: renamed from: io.legado.app.help.http.OkHttpUtilsKt$newCallResponseBody$1, reason: invalid class name and case insensitive filesystem */
    /* JADX INFO: compiled from: OkHttpUtils.kt */
    /* JADX INFO: loaded from: reader-pro-classes-3.2.14.jar:io/legado/app/help/http/OkHttpUtilsKt$newCallResponseBody$1.class */
    @Metadata(mv = {1, 5, 1}, k = 3, xi = 48)
    @DebugMetadata(f = "OkHttpUtils.kt", l = {46}, i = {}, s = {}, n = {}, m = "newCallResponseBody", c = "io.legado.app.help.http.OkHttpUtilsKt")
    static final class C01501 extends ContinuationImpl {
        /* synthetic */ Object result;
        int label;

        C01501(Continuation<? super C01501> $completion) {
            super($completion);
        }

        @Nullable
        public final Object invokeSuspend(@NotNull Object $result) {
            this.result = $result;
            this.label |= Integer.MIN_VALUE;
            return OkHttpUtilsKt.newCallResponseBody(null, 0, null, (Continuation) this);
        }
    }

    /* JADX INFO: renamed from: io.legado.app.help.http.OkHttpUtilsKt$newCallStrResponse$1, reason: invalid class name and case insensitive filesystem */
    /* JADX INFO: compiled from: OkHttpUtils.kt */
    /* JADX INFO: loaded from: reader-pro-classes-3.2.14.jar:io/legado/app/help/http/OkHttpUtilsKt$newCallStrResponse$1.class */
    @Metadata(mv = {1, 5, 1}, k = 3, xi = 48)
    @DebugMetadata(f = "OkHttpUtils.kt", l = {76}, i = {}, s = {}, n = {}, m = "newCallStrResponse", c = "io.legado.app.help.http.OkHttpUtilsKt")
    static final class C01511 extends ContinuationImpl {
        Object L$0;
        Object L$1;
        int I$0;
        int I$1;
        int I$2;
        /* synthetic */ Object result;
        int label;

        C01511(Continuation<? super C01511> $completion) {
            super($completion);
        }

        @Nullable
        public final Object invokeSuspend(@NotNull Object $result) {
            this.result = $result;
            this.label |= Integer.MIN_VALUE;
            return OkHttpUtilsKt.newCallStrResponse(null, 0, null, (Continuation) this);
        }
    }

    public static /* synthetic */ Object newCallResponse$default(OkHttpClient okHttpClient, int i, Function1 function1, Continuation continuation, int i2, Object obj) {
        if ((i2 & 1) != 0) {
            i = 0;
        }
        return newCallResponse(okHttpClient, i, function1, continuation);
    }

    /* JADX INFO: renamed from: io.legado.app.help.http.OkHttpUtilsKt$newCallResponse$2, reason: invalid class name */
    /* JADX INFO: compiled from: OkHttpUtils.kt */
    /* JADX INFO: loaded from: reader-pro-classes-3.2.14.jar:io/legado/app/help/http/OkHttpUtilsKt$newCallResponse$2.class */
    @Metadata(mv = {1, 5, 1}, k = 3, xi = 48, d1 = {"\u0000\n\n\u0000\n\u0002\u0018\u0002\n\u0002\u0018\u0002\u0010\u0000\u001a\u00020\u0001*\u00020\u0002H\u008a@"}, d2 = {"<anonymous>", "Lokhttp3/Response;", "Lkotlinx/coroutines/CoroutineScope;"})
    @DebugMetadata(f = "OkHttpUtils.kt", l = {33}, i = {0}, s = {"I$1"}, n = {"i"}, m = "invokeSuspend", c = "io.legado.app.help.http.OkHttpUtilsKt$newCallResponse$2")
    static final class AnonymousClass2 extends SuspendLambda implements Function2<CoroutineScope, Continuation<? super Response>, Object> {
        Object L$0;
        int I$0;
        int I$1;
        int label;
        final /* synthetic */ Function1<Request.Builder, Unit> $builder;
        final /* synthetic */ int $retry;
        final /* synthetic */ OkHttpClient $this_newCallResponse;

        /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
        AnonymousClass2(Function1<? super Request.Builder, Unit> $builder, int $retry, OkHttpClient $this_newCallResponse, Continuation<? super AnonymousClass2> $completion) {
            super(2, $completion);
            this.$builder = $builder;
            this.$retry = $retry;
            this.$this_newCallResponse = $this_newCallResponse;
        }

        @NotNull
        public final Continuation<Unit> create(@Nullable Object value, @NotNull Continuation<?> $completion) {
            return new AnonymousClass2(this.$builder, this.$retry, this.$this_newCallResponse, $completion);
        }

        @Nullable
        public final Object invoke(@NotNull CoroutineScope p1, @Nullable Continuation<? super Response> p2) {
            return create(p1, p2).invokeSuspend(Unit.INSTANCE);
        }

        /* JADX WARN: Code restructure failed: missing block: B:16:0x00b5, code lost:
        
            if (r10 == r5.$retry) goto L17;
         */
        /* JADX WARN: Code restructure failed: missing block: B:17:0x00b8, code lost:
        
            r0 = r8;
            kotlin.jvm.internal.Intrinsics.checkNotNull(r0);
         */
        /* JADX WARN: Code restructure failed: missing block: B:18:0x00bd, code lost:
        
            return r0;
         */
        /* JADX WARN: Code restructure failed: missing block: B:5:0x004e, code lost:
        
            if (0 <= r5.$retry) goto L6;
         */
        /* JADX WARN: Code restructure failed: missing block: B:6:0x0051, code lost:
        
            r10 = r9;
            r9 = r9 + 1;
            r5.L$0 = r7;
            r5.I$0 = r9;
            r5.I$1 = r10;
            r5.label = 1;
            r0 = io.legado.app.help.http.OkHttpUtilsKt.await(r5.$this_newCallResponse.newCall(r7.build()), (kotlin.coroutines.Continuation) r5);
         */
        /* JADX WARN: Code restructure failed: missing block: B:7:0x0083, code lost:
        
            if (r0 != r0) goto L11;
         */
        /* JADX WARN: Code restructure failed: missing block: B:9:0x0088, code lost:
        
            return r0;
         */
        /* JADX WARN: Unsupported multi-entry loop pattern (BACK_EDGE: B:16:0x00b5 -> B:6:0x0051). Please report as a decompilation issue!!! */
        @Nullable
        /*
            Code decompiled incorrectly, please refer to instructions dump.
        */
        public final Object invokeSuspend(@NotNull Object $result) {
            int i;
            Request.Builder requestBuilder;
            Response response;
            Object coroutine_suspended = IntrinsicsKt.getCOROUTINE_SUSPENDED();
            switch (this.label) {
                case 0:
                    ResultKt.throwOnFailure($result);
                    requestBuilder = new Request.Builder();
                    this.$builder.invoke(requestBuilder);
                    response = null;
                    i = 0;
                    break;
                case 1:
                    int i2 = this.I$1;
                    i = this.I$0;
                    requestBuilder = (Request.Builder) this.L$0;
                    ResultKt.throwOnFailure($result);
                    Object objAwait = $result;
                    response = (Response) objAwait;
                    if (response.isSuccessful()) {
                        return response;
                    }
                    break;
                default:
                    throw new IllegalStateException("call to 'resume' before 'invoke' with coroutine");
            }
        }
    }

    @Nullable
    public static final Object newCallResponse(@NotNull OkHttpClient $this$newCallResponse, int retry, @NotNull Function1<? super Request.Builder, Unit> builder, @NotNull Continuation<? super Response> $completion) {
        return BuildersKt.withContext(Dispatchers.getIO(), new AnonymousClass2(builder, retry, $this$newCallResponse, null), $completion);
    }

    /* JADX WARN: Removed duplicated region for block: B:7:0x0027  */
    @Nullable
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    public static final Object newCallResponseBody(@NotNull OkHttpClient $this$newCallResponseBody, int retry, @NotNull Function1<? super Request.Builder, Unit> builder, @NotNull Continuation<? super ResponseBody> $completion) throws IOException {
        C01501 c01501;
        Object objNewCallResponse;
        if ($completion instanceof C01501) {
            c01501 = (C01501) $completion;
            if ((c01501.label & Integer.MIN_VALUE) != 0) {
                c01501.label -= Integer.MIN_VALUE;
            } else {
                c01501 = new C01501($completion);
            }
        }
        Object $result = c01501.result;
        Object coroutine_suspended = IntrinsicsKt.getCOROUTINE_SUSPENDED();
        switch (c01501.label) {
            case 0:
                ResultKt.throwOnFailure($result);
                c01501.label = 1;
                objNewCallResponse = newCallResponse($this$newCallResponseBody, retry, builder, c01501);
                if (objNewCallResponse == coroutine_suspended) {
                    return coroutine_suspended;
                }
                break;
            case 1:
                ResultKt.throwOnFailure($result);
                objNewCallResponse = $result;
                break;
            default:
                throw new IllegalStateException("call to 'resume' before 'invoke' with coroutine");
        }
        Response it = (Response) objNewCallResponse;
        ResponseBody responseBodyBody = it.body();
        if (responseBodyBody == null) {
            throw new IOException(it.message());
        }
        return responseBodyBody;
    }

    public static /* synthetic */ Object newCallResponseBody$default(OkHttpClient okHttpClient, int i, Function1 function1, Continuation continuation, int i2, Object obj) {
        if ((i2 & 1) != 0) {
            i = 0;
        }
        return newCallResponseBody(okHttpClient, i, function1, continuation);
    }

    /* JADX WARN: Code restructure failed: missing block: B:11:0x0082, code lost:
    
        if (0 <= r6) goto L12;
     */
    /* JADX WARN: Code restructure failed: missing block: B:12:0x0085, code lost:
    
        r12 = r11;
        r11 = r11 + 1;
        r14.L$0 = r5;
        r14.L$1 = r9;
        r14.I$0 = r6;
        r14.I$1 = r11;
        r14.I$2 = r12;
        r14.label = 1;
        r0 = await(r5.newCall(r9.build()), r14);
     */
    /* JADX WARN: Code restructure failed: missing block: B:13:0x00c4, code lost:
    
        if (r0 != r0) goto L17;
     */
    /* JADX WARN: Code restructure failed: missing block: B:15:0x00c9, code lost:
    
        return r0;
     */
    /* JADX WARN: Code restructure failed: missing block: B:22:0x0112, code lost:
    
        if (r12 == r6) goto L23;
     */
    /* JADX WARN: Code restructure failed: missing block: B:23:0x0115, code lost:
    
        r0 = r10;
        kotlin.jvm.internal.Intrinsics.checkNotNull(r0);
        r0 = r0.body();
     */
    /* JADX WARN: Code restructure failed: missing block: B:24:0x0122, code lost:
    
        if (r0 != null) goto L27;
     */
    /* JADX WARN: Code restructure failed: missing block: B:26:0x0131, code lost:
    
        throw new java.io.IOException(r10.message());
     */
    /* JADX WARN: Code restructure failed: missing block: B:28:0x0134, code lost:
    
        return r0;
     */
    /* JADX WARN: Removed duplicated region for block: B:7:0x0027  */
    /* JADX WARN: Unsupported multi-entry loop pattern (BACK_EDGE: B:22:0x0112 -> B:12:0x0085). Please report as a decompilation issue!!! */
    @Nullable
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    public static final Object newCall(@NotNull OkHttpClient $this$newCall, int retry, @NotNull Function1<? super Request.Builder, Unit> builder, @NotNull Continuation<? super ResponseBody> $completion) throws IOException {
        AnonymousClass1 anonymousClass1;
        int i;
        Request.Builder requestBuilder;
        Response response;
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
                requestBuilder = new Request.Builder();
                builder.invoke(requestBuilder);
                response = null;
                i = 0;
                break;
            case 1:
                int i2 = anonymousClass1.I$2;
                i = anonymousClass1.I$1;
                retry = anonymousClass1.I$0;
                requestBuilder = (Request.Builder) anonymousClass1.L$1;
                $this$newCall = (OkHttpClient) anonymousClass1.L$0;
                ResultKt.throwOnFailure($result);
                Object objAwait = $result;
                response = (Response) objAwait;
                if (response.isSuccessful()) {
                    ResponseBody responseBodyBody = response.body();
                    Intrinsics.checkNotNull(responseBodyBody);
                    return responseBodyBody;
                }
                break;
            default:
                throw new IllegalStateException("call to 'resume' before 'invoke' with coroutine");
        }
    }

    public static /* synthetic */ Object newCall$default(OkHttpClient okHttpClient, int i, Function1 function1, Continuation continuation, int i2, Object obj) {
        if ((i2 & 1) != 0) {
            i = 0;
        }
        return newCall(okHttpClient, i, function1, continuation);
    }

    /* JADX WARN: Code restructure failed: missing block: B:11:0x0082, code lost:
    
        if (0 <= r9) goto L12;
     */
    /* JADX WARN: Code restructure failed: missing block: B:12:0x0085, code lost:
    
        r15 = r14;
        r14 = r14 + 1;
        kotlinx.coroutines.JobKt.ensureActive(r17.getContext());
        r17.L$0 = r8;
        r17.L$1 = r12;
        r17.I$0 = r9;
        r17.I$1 = r14;
        r17.I$2 = r15;
        r17.label = 1;
        r0 = await(r8.newCall(r12.build()), r17);
     */
    /* JADX WARN: Code restructure failed: missing block: B:13:0x00ce, code lost:
    
        if (r0 != r0) goto L17;
     */
    /* JADX WARN: Code restructure failed: missing block: B:15:0x00d3, code lost:
    
        return r0;
     */
    /* JADX WARN: Code restructure failed: missing block: B:22:0x012b, code lost:
    
        if (r15 == r9) goto L23;
     */
    /* JADX WARN: Code restructure failed: missing block: B:23:0x012e, code lost:
    
        r2 = r13;
        kotlin.jvm.internal.Intrinsics.checkNotNull(r2);
        r3 = r13.body();
     */
    /* JADX WARN: Code restructure failed: missing block: B:24:0x0141, code lost:
    
        if (r3 != null) goto L26;
     */
    /* JADX WARN: Code restructure failed: missing block: B:25:0x0144, code lost:
    
        r3 = r13.message();
     */
    /* JADX WARN: Code restructure failed: missing block: B:26:0x014c, code lost:
    
        r3 = text$default(r3, null, 1, null);
     */
    /* JADX WARN: Code restructure failed: missing block: B:28:0x0157, code lost:
    
        return new io.legado.app.help.http.StrResponse(r2, r3);
     */
    /* JADX WARN: Removed duplicated region for block: B:7:0x0027  */
    /* JADX WARN: Unsupported multi-entry loop pattern (BACK_EDGE: B:22:0x012b -> B:12:0x0085). Please report as a decompilation issue!!! */
    @Nullable
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    public static final Object newCallStrResponse(@NotNull OkHttpClient $this$newCallStrResponse, int retry, @NotNull Function1<? super Request.Builder, Unit> builder, @NotNull Continuation<? super StrResponse> $completion) {
        C01511 c01511;
        int i;
        Request.Builder requestBuilder;
        Response response;
        if ($completion instanceof C01511) {
            c01511 = (C01511) $completion;
            if ((c01511.label & Integer.MIN_VALUE) != 0) {
                c01511.label -= Integer.MIN_VALUE;
            } else {
                c01511 = new C01511($completion);
            }
        }
        Object $result = c01511.result;
        Object coroutine_suspended = IntrinsicsKt.getCOROUTINE_SUSPENDED();
        switch (c01511.label) {
            case 0:
                ResultKt.throwOnFailure($result);
                requestBuilder = new Request.Builder();
                builder.invoke(requestBuilder);
                response = null;
                i = 0;
                break;
            case 1:
                int i2 = c01511.I$2;
                i = c01511.I$1;
                retry = c01511.I$0;
                requestBuilder = (Request.Builder) c01511.L$1;
                $this$newCallStrResponse = (OkHttpClient) c01511.L$0;
                ResultKt.throwOnFailure($result);
                Object objAwait = $result;
                response = (Response) objAwait;
                if (response.isSuccessful()) {
                    ResponseBody responseBodyBody = response.body();
                    Intrinsics.checkNotNull(responseBodyBody);
                    return new StrResponse(response, text$default(responseBodyBody, null, 1, null));
                }
                break;
            default:
                throw new IllegalStateException("call to 'resume' before 'invoke' with coroutine");
        }
    }

    public static /* synthetic */ Object newCallStrResponse$default(OkHttpClient okHttpClient, int i, Function1 function1, Continuation continuation, int i2, Object obj) {
        if ((i2 & 1) != 0) {
            i = 0;
        }
        return newCallStrResponse(okHttpClient, i, function1, continuation);
    }

    @Nullable
    public static final Object await(@NotNull Call $this$await, @NotNull Continuation<? super Response> $completion) {
        CancellableContinuation cancellableContinuationImpl = new CancellableContinuationImpl(IntrinsicsKt.intercepted($completion), 1);
        cancellableContinuationImpl.initCancellability();
        CancellableContinuation block = cancellableContinuationImpl;
        block.invokeOnCancellation(new OkHttpUtilsKt$await$2$1($this$await));
        $this$await.enqueue(new OkHttpUtilsKt$await$2$2(block));
        Object result = cancellableContinuationImpl.getResult();
        if (result == IntrinsicsKt.getCOROUTINE_SUSPENDED()) {
            DebugProbesKt.probeCoroutineSuspended($completion);
        }
        return result;
    }

    public static /* synthetic */ String text$default(ResponseBody responseBody, String str, int i, Object obj) {
        if ((i & 1) != 0) {
            str = null;
        }
        return text(responseBody, str);
    }

    @NotNull
    public static final String text(@NotNull ResponseBody $this$text, @Nullable String encode) {
        Charset it;
        Intrinsics.checkNotNullParameter($this$text, "<this>");
        byte[] responseBytes = Utf8BomUtils.INSTANCE.removeUTF8BOM($this$text.bytes());
        if (encode != null) {
            Charset charsetForName = Charset.forName(encode);
            Intrinsics.checkNotNullExpressionValue(charsetForName, "forName(charsetName)");
            return new String(responseBytes, charsetForName);
        }
        MediaType mediaTypeContentType = $this$text.contentType();
        if (mediaTypeContentType != null && (it = MediaType.charset$default(mediaTypeContentType, (Charset) null, 1, (Object) null)) != null) {
            return new String(responseBytes, it);
        }
        Charset charsetForName2 = Charset.forName(EncodingDetect.INSTANCE.getHtmlEncode(responseBytes));
        Intrinsics.checkNotNullExpressionValue(charsetForName2, "forName(charsetName)");
        return new String(responseBytes, charsetForName2);
    }

    public static final void addHeaders(@NotNull Request.Builder $this$addHeaders, @NotNull Map<String, String> headers) {
        Intrinsics.checkNotNullParameter($this$addHeaders, "<this>");
        Intrinsics.checkNotNullParameter(headers, "headers");
        for (Map.Entry<String, String> entry : headers.entrySet()) {
            $this$addHeaders.addHeader(entry.getKey(), entry.getValue());
        }
    }

    public static /* synthetic */ void get$default(Request.Builder builder, String str, Map map, boolean z, int i, Object obj) {
        if ((i & 4) != 0) {
            z = false;
        }
        get(builder, str, map, z);
    }

    public static final void get(@NotNull Request.Builder $this$get, @NotNull String url, @NotNull Map<String, String> queryMap, boolean encoded) {
        Intrinsics.checkNotNullParameter($this$get, "<this>");
        Intrinsics.checkNotNullParameter(url, RSSKeywords.RSS_ITEM_URL);
        Intrinsics.checkNotNullParameter(queryMap, "queryMap");
        HttpUrl.Builder httpBuilder = HttpUrl.Companion.get(url).newBuilder();
        for (Map.Entry<String, String> entry : queryMap.entrySet()) {
            if (encoded) {
                httpBuilder.addEncodedQueryParameter(entry.getKey(), entry.getValue());
            } else {
                httpBuilder.addQueryParameter(entry.getKey(), entry.getValue());
            }
        }
        $this$get.url(httpBuilder.build());
    }

    public static /* synthetic */ void postForm$default(Request.Builder builder, Map map, boolean z, int i, Object obj) {
        if ((i & 2) != 0) {
            z = false;
        }
        postForm(builder, map, z);
    }

    public static final void postForm(@NotNull Request.Builder $this$postForm, @NotNull Map<String, String> form, boolean encoded) {
        Intrinsics.checkNotNullParameter($this$postForm, "<this>");
        Intrinsics.checkNotNullParameter(form, "form");
        FormBody.Builder formBody = new FormBody.Builder((Charset) null, 1, (DefaultConstructorMarker) null);
        for (Map.Entry<String, String> entry : form.entrySet()) {
            if (encoded) {
                formBody.addEncoded(entry.getKey(), entry.getValue());
            } else {
                formBody.add(entry.getKey(), entry.getValue());
            }
        }
        $this$postForm.post(formBody.build());
    }

    public static final void postMultipart(@NotNull Request.Builder $this$postMultipart, @Nullable String type, @NotNull Map<String, ? extends Object> form) {
        RequestBody requestBodyCreate;
        Intrinsics.checkNotNullParameter($this$postMultipart, "<this>");
        Intrinsics.checkNotNullParameter(form, "form");
        MultipartBody.Builder multipartBody = new MultipartBody.Builder((String) null, 1, (DefaultConstructorMarker) null);
        if (type != null) {
            multipartBody.setType(MediaType.Companion.get(type));
        }
        for (Map.Entry<String, ? extends Object> entry : form.entrySet()) {
            Object value = entry.getValue();
            if (value instanceof Map) {
                Object obj = ((Map) value).get("fileName");
                if (obj == null) {
                    throw new NullPointerException("null cannot be cast to non-null type kotlin.String");
                }
                String fileName = (String) obj;
                Object file = ((Map) value).get("file");
                Object obj2 = ((Map) value).get("contentType");
                String str = obj2 instanceof String ? (String) obj2 : null;
                MediaType mediaType = str == null ? null : MediaType.Companion.get(str);
                if (file instanceof File) {
                    requestBodyCreate = RequestBody.Companion.create((File) file, mediaType);
                } else if (file instanceof byte[]) {
                    requestBodyCreate = RequestBody.Companion.create$default(RequestBody.Companion, (byte[]) file, mediaType, 0, 0, 6, (Object) null);
                } else if (file instanceof String) {
                    requestBodyCreate = RequestBody.Companion.create((String) file, mediaType);
                } else {
                    RequestBody.Companion companion = RequestBody.Companion;
                    String json = GsonExtensionsKt.getGSON().toJson(file);
                    Intrinsics.checkNotNullExpressionValue(json, "GSON.toJson(file)");
                    requestBodyCreate = companion.create(json, mediaType);
                }
                RequestBody requestBody = requestBodyCreate;
                multipartBody.addFormDataPart(entry.getKey(), fileName, requestBody);
            } else {
                multipartBody.addFormDataPart(entry.getKey(), entry.getValue().toString());
            }
        }
        $this$postMultipart.post(multipartBody.build());
    }

    public static final void postJson(@NotNull Request.Builder $this$postJson, @Nullable String json) {
        Intrinsics.checkNotNullParameter($this$postJson, "<this>");
        if (json != null) {
            RequestBody requestBody = RequestBody.Companion.create(json, MediaType.Companion.get("application/json; charset=UTF-8"));
            $this$postJson.post(requestBody);
        }
    }
}
