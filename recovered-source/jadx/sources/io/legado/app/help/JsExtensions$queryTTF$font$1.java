package io.legado.app.help;

import io.legado.app.help.http.HttpHelperKt;
import io.legado.app.help.http.OkHttpUtilsKt;
import kotlin.Metadata;
import kotlin.ResultKt;
import kotlin.Unit;
import kotlin.coroutines.Continuation;
import kotlin.coroutines.intrinsics.IntrinsicsKt;
import kotlin.coroutines.jvm.internal.DebugMetadata;
import kotlin.coroutines.jvm.internal.SuspendLambda;
import kotlin.jvm.functions.Function1;
import kotlin.jvm.functions.Function2;
import kotlin.jvm.internal.Intrinsics;
import kotlin.jvm.internal.Lambda;
import kotlin.jvm.internal.Ref;
import kotlinx.coroutines.CoroutineScope;
import me.ag2s.epublib.epub.PackageDocumentBase;
import okhttp3.Request;
import okhttp3.ResponseBody;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/* JADX INFO: compiled from: JsExtensions.kt */
/* JADX INFO: loaded from: reader-pro-classes-3.2.14.jar:io/legado/app/help/JsExtensions$queryTTF$font$1.class */
@Metadata(mv = {1, 5, 1}, k = 3, xi = 48, d1 = {"\u0000\n\n\u0000\n\u0002\u0010\u0012\n\u0002\u0018\u0002\u0010\u0000\u001a\u00020\u0001*\u00020\u0002H\u008a@"}, d2 = {"<anonymous>", PackageDocumentBase.PREFIX_OPF, "Lkotlinx/coroutines/CoroutineScope;"})
@DebugMetadata(f = "JsExtensions.kt", l = {508}, i = {}, s = {}, n = {}, m = "invokeSuspend", c = "io.legado.app.help.JsExtensions$queryTTF$font$1")
final class JsExtensions$queryTTF$font$1 extends SuspendLambda implements Function2<CoroutineScope, Continuation<? super byte[]>, Object> {
    int label;
    final /* synthetic */ Ref.ObjectRef<CacheManager> $cacheInstance;
    final /* synthetic */ String $key;
    final /* synthetic */ String $str;

    /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
    JsExtensions$queryTTF$font$1(Ref.ObjectRef<CacheManager> $cacheInstance, String $key, String $str, Continuation<? super JsExtensions$queryTTF$font$1> $completion) {
        super(2, $completion);
        this.$cacheInstance = $cacheInstance;
        this.$key = $key;
        this.$str = $str;
    }

    @NotNull
    public final Continuation<Unit> create(@Nullable Object value, @NotNull Continuation<?> $completion) {
        return new JsExtensions$queryTTF$font$1(this.$cacheInstance, this.$key, this.$str, $completion);
    }

    @Nullable
    public final Object invoke(@NotNull CoroutineScope p1, @Nullable Continuation<? super byte[]> p2) {
        return create(p1, p2).invokeSuspend(Unit.INSTANCE);
    }

    @Nullable
    public final Object invokeSuspend(@NotNull Object $result) {
        Object objNewCall$default;
        byte[] x;
        Object coroutine_suspended = IntrinsicsKt.getCOROUTINE_SUSPENDED();
        switch (this.label) {
            case 0:
                ResultKt.throwOnFailure($result);
                x = ((CacheManager) this.$cacheInstance.element).getByteArray(this.$key);
                if (x == null) {
                    this.label = 1;
                    objNewCall$default = OkHttpUtilsKt.newCall$default(HttpHelperKt.getOkHttpClient(), 0, new AnonymousClass1(this.$str), (Continuation) this, 1, null);
                    if (objNewCall$default == coroutine_suspended) {
                        return coroutine_suspended;
                    }
                    x = ((ResponseBody) objNewCall$default).bytes();
                    Ref.ObjectRef<CacheManager> objectRef = this.$cacheInstance;
                    CacheManager.put$default((CacheManager) objectRef.element, this.$key, x, 0, 4, null);
                }
                return x;
            case 1:
                ResultKt.throwOnFailure($result);
                objNewCall$default = $result;
                x = ((ResponseBody) objNewCall$default).bytes();
                Ref.ObjectRef<CacheManager> objectRef2 = this.$cacheInstance;
                CacheManager.put$default((CacheManager) objectRef2.element, this.$key, x, 0, 4, null);
                return x;
            default:
                throw new IllegalStateException("call to 'resume' before 'invoke' with coroutine");
        }
    }

    /* JADX INFO: renamed from: io.legado.app.help.JsExtensions$queryTTF$font$1$1, reason: invalid class name */
    /* JADX INFO: compiled from: JsExtensions.kt */
    /* JADX INFO: loaded from: reader-pro-classes-3.2.14.jar:io/legado/app/help/JsExtensions$queryTTF$font$1$1.class */
    @Metadata(mv = {1, 5, 1}, k = 3, xi = 48, d1 = {"\u0000\n\n\u0000\n\u0002\u0010\u0002\n\u0002\u0018\u0002\u0010\u0000\u001a\u00020\u0001*\u00020\u0002H\n"}, d2 = {"<anonymous>", PackageDocumentBase.PREFIX_OPF, "Lokhttp3/Request$Builder;"})
    static final class AnonymousClass1 extends Lambda implements Function1<Request.Builder, Unit> {
        final /* synthetic */ String $str;

        /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
        AnonymousClass1(String $str) {
            super(1);
            this.$str = $str;
        }

        public final void invoke(@NotNull Request.Builder $this$newCall) {
            Intrinsics.checkNotNullParameter($this$newCall, "$this$newCall");
            $this$newCall.url(this.$str);
        }

        public /* bridge */ /* synthetic */ Object invoke(Object p1) {
            invoke((Request.Builder) p1);
            return Unit.INSTANCE;
        }
    }
}
