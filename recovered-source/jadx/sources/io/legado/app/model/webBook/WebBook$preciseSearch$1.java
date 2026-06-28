package io.legado.app.model.webBook;

import kotlin.Metadata;
import kotlin.Result;
import kotlin.coroutines.Continuation;
import kotlin.coroutines.intrinsics.IntrinsicsKt;
import kotlin.coroutines.jvm.internal.ContinuationImpl;
import kotlin.coroutines.jvm.internal.DebugMetadata;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/* JADX INFO: compiled from: WebBook.kt */
/* JADX INFO: loaded from: reader-pro-classes-3.2.14.jar:io/legado/app/model/webBook/WebBook$preciseSearch$1.class */
@Metadata(mv = {1, 5, 1}, k = 3, xi = 48)
@DebugMetadata(f = "WebBook.kt", l = {276, 281}, i = {0, 0, 0}, s = {"L$0", "L$1", "L$2"}, n = {"this", "name", "author"}, m = "preciseSearch-0E7RQCE", c = "io.legado.app.model.webBook.WebBook")
final class WebBook$preciseSearch$1 extends ContinuationImpl {
    Object L$0;
    Object L$1;
    Object L$2;
    /* synthetic */ Object result;
    final /* synthetic */ WebBook this$0;
    int label;

    /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
    WebBook$preciseSearch$1(WebBook this$0, Continuation<? super WebBook$preciseSearch$1> $completion) {
        super($completion);
        this.this$0 = this$0;
    }

    @Nullable
    public final Object invokeSuspend(@NotNull Object $result) {
        this.result = $result;
        this.label |= Integer.MIN_VALUE;
        Object objM269preciseSearch0E7RQCE = this.this$0.m269preciseSearch0E7RQCE(null, null, (Continuation) this);
        return objM269preciseSearch0E7RQCE == IntrinsicsKt.getCOROUTINE_SUSPENDED() ? objM269preciseSearch0E7RQCE : Result.box-impl(objM269preciseSearch0E7RQCE);
    }
}
