package io.legado.app.help.coroutine;

import kotlin.Metadata;
import kotlin.ResultKt;
import kotlin.Unit;
import kotlin.coroutines.Continuation;
import kotlin.coroutines.intrinsics.IntrinsicsKt;
import kotlin.coroutines.jvm.internal.DebugMetadata;
import kotlin.coroutines.jvm.internal.SuspendLambda;
import kotlin.jvm.functions.Function2;
import kotlinx.coroutines.BuildersKt;
import kotlinx.coroutines.CoroutineScope;
import me.ag2s.epublib.epub.PackageDocumentBase;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/* JADX INFO: compiled from: Coroutine.kt */
/* JADX INFO: loaded from: app-classes.jar:io/legado/app/help/coroutine/Coroutine$cancel$1$1.class */
@Metadata(mv = {1, 5, 1}, k = 3, xi = 48, d1 = {"\u0000\f\n\u0000\n\u0002\u0010\u0002\n\u0000\n\u0002\u0018\u0002\u0010\u0000\u001a\u00020\u0001\"\u0004\b\u0000\u0010\u0002*\u00020\u0003H\u008a@"}, d2 = {"<anonymous>", PackageDocumentBase.PREFIX_OPF, "T", "Lkotlinx/coroutines/CoroutineScope;"})
@DebugMetadata(f = "Coroutine.kt", l = {117, 119}, i = {}, s = {}, n = {}, m = "invokeSuspend", c = "io.legado.app.help.coroutine.Coroutine$cancel$1$1")
final class Coroutine$cancel$1$1 extends SuspendLambda implements Function2<CoroutineScope, Continuation<? super Unit>, Object> {
    int label;
    final /* synthetic */ Coroutine<T>.VoidCallback $it;
    final /* synthetic */ Coroutine<T> this$0;

    /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
    Coroutine$cancel$1$1(Coroutine<T>.VoidCallback $it, Coroutine<T> this$0, Continuation<? super Coroutine$cancel$1$1> $completion) {
        super(2, $completion);
        this.$it = $it;
        this.this$0 = this$0;
    }

    @NotNull
    public final Continuation<Unit> create(@Nullable Object value, @NotNull Continuation<?> $completion) {
        return new Coroutine$cancel$1$1(this.$it, this.this$0, $completion);
    }

    @Nullable
    public final Object invoke(@NotNull CoroutineScope p1, @Nullable Continuation<? super Unit> p2) {
        return create(p1, p2).invokeSuspend(Unit.INSTANCE);
    }

    @Nullable
    public final Object invokeSuspend(@NotNull Object $result) {
        Object coroutine_suspended = IntrinsicsKt.getCOROUTINE_SUSPENDED();
        switch (this.label) {
            case 0:
                ResultKt.throwOnFailure($result);
                if (this.$it.getContext() != null) {
                    this.label = 2;
                    if (BuildersKt.withContext(this.this$0.getScope().getCoroutineContext().plus(this.$it.getContext()), new AnonymousClass1(this.$it, null), (Continuation) this) == coroutine_suspended) {
                        return coroutine_suspended;
                    }
                } else {
                    Function2<CoroutineScope, Continuation<? super Unit>, Object> block = this.$it.getBlock();
                    CoroutineScope scope = this.this$0.getScope();
                    this.label = 1;
                    if (block.invoke(scope, this) == coroutine_suspended) {
                        return coroutine_suspended;
                    }
                }
            case 1:
                ResultKt.throwOnFailure($result);
                break;
            case 2:
                ResultKt.throwOnFailure($result);
                break;
            default:
                throw new IllegalStateException("call to 'resume' before 'invoke' with coroutine");
        }
        return Unit.INSTANCE;
    }

    /* JADX INFO: renamed from: io.legado.app.help.coroutine.Coroutine$cancel$1$1$1, reason: invalid class name */
    /* JADX INFO: compiled from: Coroutine.kt */
    /* JADX INFO: loaded from: app-classes.jar:io/legado/app/help/coroutine/Coroutine$cancel$1$1$1.class */
    @Metadata(mv = {1, 5, 1}, k = 3, xi = 48, d1 = {"\u0000\f\n\u0000\n\u0002\u0010\u0002\n\u0000\n\u0002\u0018\u0002\u0010\u0000\u001a\u00020\u0001\"\u0004\b\u0000\u0010\u0002*\u00020\u0003H\u008a@"}, d2 = {"<anonymous>", PackageDocumentBase.PREFIX_OPF, "T", "Lkotlinx/coroutines/CoroutineScope;"})
    @DebugMetadata(f = "Coroutine.kt", l = {120}, i = {}, s = {}, n = {}, m = "invokeSuspend", c = "io.legado.app.help.coroutine.Coroutine$cancel$1$1$1")
    static final class AnonymousClass1 extends SuspendLambda implements Function2<CoroutineScope, Continuation<? super Unit>, Object> {
        int label;
        private /* synthetic */ Object L$0;
        final /* synthetic */ Coroutine<T>.VoidCallback $it;

        /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
        AnonymousClass1(Coroutine<T>.VoidCallback $it, Continuation<? super AnonymousClass1> $completion) {
            super(2, $completion);
            this.$it = $it;
        }

        @NotNull
        public final Continuation<Unit> create(@Nullable Object value, @NotNull Continuation<?> $completion) {
            Continuation<Unit> anonymousClass1 = new AnonymousClass1(this.$it, $completion);
            anonymousClass1.L$0 = value;
            return anonymousClass1;
        }

        @Nullable
        public final Object invoke(@NotNull CoroutineScope p1, @Nullable Continuation<? super Unit> p2) {
            return create(p1, p2).invokeSuspend(Unit.INSTANCE);
        }

        @Nullable
        public final Object invokeSuspend(@NotNull Object $result) {
            Object coroutine_suspended = IntrinsicsKt.getCOROUTINE_SUSPENDED();
            switch (this.label) {
                case 0:
                    ResultKt.throwOnFailure($result);
                    CoroutineScope $this$withContext = (CoroutineScope) this.L$0;
                    Function2<CoroutineScope, Continuation<? super Unit>, Object> block = this.$it.getBlock();
                    this.label = 1;
                    if (block.invoke($this$withContext, this) == coroutine_suspended) {
                        return coroutine_suspended;
                    }
                    break;
                case 1:
                    ResultKt.throwOnFailure($result);
                    break;
                default:
                    throw new IllegalStateException("call to 'resume' before 'invoke' with coroutine");
            }
            return Unit.INSTANCE;
        }
    }
}
