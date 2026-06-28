package com.htmake.reader.verticle;

import io.vertx.ext.web.RoutingContext;
import kotlin.Metadata;
import kotlin.ResultKt;
import kotlin.Unit;
import kotlin.coroutines.Continuation;
import kotlin.coroutines.intrinsics.IntrinsicsKt;
import kotlin.coroutines.jvm.internal.DebugMetadata;
import kotlin.coroutines.jvm.internal.SuspendLambda;
import kotlin.jvm.functions.Function2;
import kotlin.jvm.internal.Intrinsics;
import kotlinx.coroutines.CoroutineScope;
import me.ag2s.epublib.epub.PackageDocumentBase;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/* JADX INFO: compiled from: RestVerticle.kt */
/* JADX INFO: loaded from: app-classes.jar:com/htmake/reader/verticle/RestVerticle$coroutineHandlerWithoutRes$1$2.class */
@Metadata(mv = {1, 5, 1}, k = 3, xi = 48, d1 = {"\u0000\n\n\u0000\n\u0002\u0010\u0002\n\u0002\u0018\u0002\u0010\u0000\u001a\u00020\u0001*\u00020\u0002H\u008a@"}, d2 = {"<anonymous>", PackageDocumentBase.PREFIX_OPF, "Lkotlinx/coroutines/CoroutineScope;"})
@DebugMetadata(f = "RestVerticle.kt", l = {181}, i = {}, s = {}, n = {}, m = "invokeSuspend", c = "com.htmake.reader.verticle.RestVerticle$coroutineHandlerWithoutRes$1$2")
final class RestVerticle$coroutineHandlerWithoutRes$1$2 extends SuspendLambda implements Function2<CoroutineScope, Continuation<? super Unit>, Object> {
    int label;
    final /* synthetic */ Function2<RoutingContext, Continuation<Object>, Object> $fn;
    final /* synthetic */ RoutingContext $ctx;
    final /* synthetic */ RestVerticle this$0;

    /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
    RestVerticle$coroutineHandlerWithoutRes$1$2(Function2<? super RoutingContext, ? super Continuation<Object>, ? extends Object> $fn, RoutingContext $ctx, RestVerticle this$0, Continuation<? super RestVerticle$coroutineHandlerWithoutRes$1$2> $completion) {
        super(2, $completion);
        this.$fn = $fn;
        this.$ctx = $ctx;
        this.this$0 = this$0;
    }

    @NotNull
    public final Continuation<Unit> create(@Nullable Object value, @NotNull Continuation<?> $completion) {
        return new RestVerticle$coroutineHandlerWithoutRes$1$2(this.$fn, this.$ctx, this.this$0, $completion);
    }

    @Nullable
    public final Object invoke(@NotNull CoroutineScope p1, @Nullable Continuation<? super Unit> p2) {
        return create(p1, p2).invokeSuspend(Unit.INSTANCE);
    }

    @Nullable
    public final Object invokeSuspend(@NotNull Object $result) {
        Object coroutine_suspended = IntrinsicsKt.getCOROUTINE_SUSPENDED();
        try {
            switch (this.label) {
                case 0:
                    ResultKt.throwOnFailure($result);
                    Function2<RoutingContext, Continuation<Object>, Object> function2 = this.$fn;
                    RoutingContext routingContext = this.$ctx;
                    Intrinsics.checkNotNullExpressionValue(routingContext, "ctx");
                    this.label = 1;
                    if (function2.invoke(routingContext, this) == coroutine_suspended) {
                        return coroutine_suspended;
                    }
                    break;
                case 1:
                    ResultKt.throwOnFailure($result);
                    break;
                default:
                    throw new IllegalStateException("call to 'resume' before 'invoke' with coroutine");
            }
        } catch (Exception e) {
            RestVerticle restVerticle = this.this$0;
            RoutingContext routingContext2 = this.$ctx;
            Intrinsics.checkNotNullExpressionValue(routingContext2, "ctx");
            restVerticle.onHandlerError(routingContext2, e);
        }
        return Unit.INSTANCE;
    }
}
