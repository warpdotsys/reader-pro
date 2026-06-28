package io.legado.app.help;

import io.legado.app.help.http.StrResponse;
import io.legado.app.model.analyzeRule.AnalyzeUrl;
import kotlin.Metadata;
import kotlin.ResultKt;
import kotlin.Unit;
import kotlin.coroutines.Continuation;
import kotlin.coroutines.intrinsics.IntrinsicsKt;
import kotlin.coroutines.jvm.internal.DebugMetadata;
import kotlin.coroutines.jvm.internal.SuspendLambda;
import kotlin.jvm.functions.Function2;
import kotlinx.coroutines.CoroutineScope;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/* JADX INFO: compiled from: JsExtensions.kt */
/* JADX INFO: loaded from: app-classes.jar:io/legado/app/help/JsExtensions$ajaxAll$1$asyncArray$1$1.class */
@Metadata(mv = {1, 5, 1}, k = 3, xi = 48, d1 = {"\u0000\n\n\u0000\n\u0002\u0018\u0002\n\u0002\u0018\u0002\u0010\u0000\u001a\u00020\u0001*\u00020\u0002H\u008a@"}, d2 = {"<anonymous>", "Lio/legado/app/help/http/StrResponse;", "Lkotlinx/coroutines/CoroutineScope;"})
@DebugMetadata(f = "JsExtensions.kt", l = {}, i = {}, s = {}, n = {}, m = "invokeSuspend", c = "io.legado.app.help.JsExtensions$ajaxAll$1$asyncArray$1$1")
final class JsExtensions$ajaxAll$1$asyncArray$1$1 extends SuspendLambda implements Function2<CoroutineScope, Continuation<? super StrResponse>, Object> {
    int label;
    final /* synthetic */ String[] $urlList;
    final /* synthetic */ int $tmp;
    final /* synthetic */ JsExtensions this$0;

    /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
    JsExtensions$ajaxAll$1$asyncArray$1$1(String[] $urlList, int $tmp, JsExtensions this$0, Continuation<? super JsExtensions$ajaxAll$1$asyncArray$1$1> $completion) {
        super(2, $completion);
        this.$urlList = $urlList;
        this.$tmp = $tmp;
        this.this$0 = this$0;
    }

    @NotNull
    public final Continuation<Unit> create(@Nullable Object value, @NotNull Continuation<?> $completion) {
        return new JsExtensions$ajaxAll$1$asyncArray$1$1(this.$urlList, this.$tmp, this.this$0, $completion);
    }

    @Nullable
    public final Object invoke(@NotNull CoroutineScope p1, @Nullable Continuation<? super StrResponse> p2) {
        return create(p1, p2).invokeSuspend(Unit.INSTANCE);
    }

    @Nullable
    public final Object invokeSuspend(@NotNull Object $result) {
        IntrinsicsKt.getCOROUTINE_SUSPENDED();
        switch (this.label) {
            case 0:
                ResultKt.throwOnFailure($result);
                String url = this.$urlList[this.$tmp];
                AnalyzeUrl analyzeUrl = new AnalyzeUrl(url, null, null, null, null, null, this.this$0.getSource(), null, null, null, this.this$0.getLogger(), 958, null);
                return AnalyzeUrl.getStrResponse$default(analyzeUrl, url, null, false, 6, null);
            default:
                throw new IllegalStateException("call to 'resume' before 'invoke' with coroutine");
        }
    }
}
