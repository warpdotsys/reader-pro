package com.htmake.reader.verticle;

import com.htmake.reader.utils.VertExtKt;
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
/* JADX INFO: loaded from: app-classes.jar:com/htmake/reader/verticle/RestVerticle$coroutineHandler$1$2.class */
@Metadata(mv = {1, 5, 1}, k = 3, xi = 48, d1 = {"\u0000\n\n\u0000\n\u0002\u0010\u0002\n\u0002\u0018\u0002\u0010\u0000\u001a\u00020\u0001*\u00020\u0002H\u008a@"}, d2 = {"<anonymous>", PackageDocumentBase.PREFIX_OPF, "Lkotlinx/coroutines/CoroutineScope;"})
@DebugMetadata(f = "RestVerticle.kt", l = {163}, i = {}, s = {}, n = {}, m = "invokeSuspend", c = "com.htmake.reader.verticle.RestVerticle$coroutineHandler$1$2")
final class RestVerticle$coroutineHandler$1$2 extends SuspendLambda implements Function2<CoroutineScope, Continuation<? super Unit>, Object> {
    Object L$0;
    int label;
    final /* synthetic */ RoutingContext $ctx;
    final /* synthetic */ Function2<RoutingContext, Continuation<Object>, Object> $fn;
    final /* synthetic */ RestVerticle this$0;

    /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
    RestVerticle$coroutineHandler$1$2(RoutingContext $ctx, Function2<? super RoutingContext, ? super Continuation<Object>, ? extends Object> $fn, RestVerticle this$0, Continuation<? super RestVerticle$coroutineHandler$1$2> $completion) {
        super(2, $completion);
        this.$ctx = $ctx;
        this.$fn = $fn;
        this.this$0 = this$0;
    }

    @NotNull
    public final Continuation<Unit> create(@Nullable Object value, @NotNull Continuation<?> $completion) {
        return new RestVerticle$coroutineHandler$1$2(this.$ctx, this.$fn, this.this$0, $completion);
    }

    @Nullable
    public final Object invoke(@NotNull CoroutineScope p1, @Nullable Continuation<? super Unit> p2) {
        return create(p1, p2).invokeSuspend(Unit.INSTANCE);
    }

    @Nullable
    public final Object invokeSuspend(@NotNull Object $result) {
        RoutingContext routingContext;
        Object objInvoke;
        Object coroutine_suspended = IntrinsicsKt.getCOROUTINE_SUSPENDED();
        try {
            switch (this.label) {
                case 0:
                    ResultKt.throwOnFailure($result);
                    RoutingContext routingContext2 = this.$ctx;
                    Intrinsics.checkNotNullExpressionValue(routingContext2, "ctx");
                    routingContext = routingContext2;
                    Function2<RoutingContext, Continuation<Object>, Object> function2 = this.$fn;
                    RoutingContext routingContext3 = this.$ctx;
                    Intrinsics.checkNotNullExpressionValue(routingContext3, "ctx");
                    this.L$0 = routingContext;
                    this.label = 1;
                    objInvoke = function2.invoke(routingContext3, this);
                    if (objInvoke == coroutine_suspended) {
                        return coroutine_suspended;
                    }
                    break;
                case 1:
                    routingContext = (RoutingContext) this.L$0;
                    ResultKt.throwOnFailure($result);
                    objInvoke = $result;
                    break;
                default:
                    throw new IllegalStateException("call to 'resume' before 'invoke' with coroutine");
            }
            VertExtKt.success(routingContext, objInvoke);
        } catch (Exception e) {
            RestVerticle restVerticle = this.this$0;
            RoutingContext routingContext4 = this.$ctx;
            Intrinsics.checkNotNullExpressionValue(routingContext4, "ctx");
            restVerticle.onHandlerError(routingContext4, e);
        }
        return Unit.INSTANCE;
    }
}
