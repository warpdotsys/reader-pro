package com.htmake.reader.api.controller;

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

/* JADX INFO: compiled from: WebdavController.kt */
/* JADX INFO: loaded from: app-classes.jar:com/htmake/reader/api/controller/WebdavController$1$2.class */
@Metadata(mv = {1, 5, 1}, k = 3, xi = 48, d1 = {"\u0000\n\n\u0000\n\u0002\u0010\u0002\n\u0002\u0018\u0002\u0010\u0000\u001a\u00020\u0001*\u00020\u0002H\u008a@"}, d2 = {"<anonymous>", PackageDocumentBase.PREFIX_OPF, "Lkotlinx/coroutines/CoroutineScope;"})
@DebugMetadata(f = "WebdavController.kt", l = {119}, i = {}, s = {}, n = {}, m = "invokeSuspend", c = "com.htmake.reader.api.controller.WebdavController$1$2")
final class WebdavController$1$2 extends SuspendLambda implements Function2<CoroutineScope, Continuation<? super Unit>, Object> {
    int label;
    final /* synthetic */ WebdavController this$0;
    final /* synthetic */ RoutingContext $it;
    final /* synthetic */ Function2<RoutingContext, Exception, Unit> $onHandlerError;

    /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
    WebdavController$1$2(WebdavController this$0, RoutingContext $it, Function2<? super RoutingContext, ? super Exception, Unit> $onHandlerError, Continuation<? super WebdavController$1$2> $completion) {
        super(2, $completion);
        this.this$0 = this$0;
        this.$it = $it;
        this.$onHandlerError = $onHandlerError;
    }

    @NotNull
    public final Continuation<Unit> create(@Nullable Object value, @NotNull Continuation<?> $completion) {
        return new WebdavController$1$2(this.this$0, this.$it, this.$onHandlerError, $completion);
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
                    WebdavController webdavController = this.this$0;
                    RoutingContext routingContext = this.$it;
                    Intrinsics.checkNotNullExpressionValue(routingContext, "it");
                    this.label = 1;
                    if (webdavController.webdavList(routingContext, (Continuation) this) == coroutine_suspended) {
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
            Function2<RoutingContext, Exception, Unit> function2 = this.$onHandlerError;
            RoutingContext routingContext2 = this.$it;
            Intrinsics.checkNotNullExpressionValue(routingContext2, "it");
            function2.invoke(routingContext2, e);
        }
        return Unit.INSTANCE;
    }
}
