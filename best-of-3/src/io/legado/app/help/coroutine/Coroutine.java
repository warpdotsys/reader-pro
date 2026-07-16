//
// Decompiled by Procyon v0.6.0
//

package io.legado.app.help.coroutine;

import kotlin.jvm.internal.InlineMarker;
import kotlinx.coroutines.DisposableHandle;
import kotlin.jvm.functions.Function1;
import kotlinx.coroutines.CoroutineStart;
import kotlinx.coroutines.BuildersKt;
import kotlinx.coroutines.CoroutineScopeKt;
import java.util.concurrent.CancellationException;
import kotlin.jvm.functions.Function3;
import kotlin.Unit;
import kotlin.jvm.functions.Function0;
import kotlinx.coroutines.Dispatchers;
import kotlin.jvm.internal.DefaultConstructorMarker;
import kotlin.jvm.internal.Intrinsics;
import kotlin.coroutines.Continuation;
import kotlin.jvm.functions.Function2;
import kotlin.coroutines.CoroutineContext;
import org.jetbrains.annotations.Nullable;
import kotlinx.coroutines.Job;
import kotlinx.coroutines.CoroutineScope;
import org.jetbrains.annotations.NotNull;
import kotlin.Metadata;

@Metadata(mv = { 1, 5, 1 }, k = 1, xi = 48, d1 = { "\u0000\u0090\u0001\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u0000\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\u0010\u0003\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\u000b\n\u0002\b\u0004\n\u0002\u0018\u0002\n\u0002\b\u0005\n\u0002\u0010\t\n\u0000\n\u0002\u0010\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\f\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\n\u0018\u0000 E*\u0004\b\u0000\u0010\u00012\u00020\u0002:\u0004DEFGBC\u0012\u0006\u0010\u0003\u001a\u00020\u0004\u0012\b\b\u0002\u0010\u0005\u001a\u00020\u0006\u0012'\u0010\u0007\u001a#\b\u0001\u0012\u0004\u0012\u00020\u0004\u0012\n\u0012\b\u0012\u0004\u0012\u00028\u00000\t\u0012\u0006\u0012\u0004\u0018\u00010\u00020\b?\u0006\u0002\b\n\u00f8\u0001\u0000?\u0006\u0002\u0010\u000bJ\u0018\u0010\f\u001a\u00020\"2\u0010\b\u0002\u0010#\u001a\n\u0018\u00010$j\u0004\u0018\u0001`%J?\u0010&\u001a\u00020\"\"\u0004\b\u0001\u0010'2\u0006\u0010\u0003\u001a\u00020\u00042\u0006\u0010(\u001a\u0002H'2\u0016\u0010)\u001a\u0012\u0012\u0004\u0012\u0002H'0\u000fR\b\u0012\u0004\u0012\u00028\u00000\u0000H\u0082H\u00f8\u0001\u0000?\u0006\u0002\u0010*J+\u0010+\u001a\u00020\"2\u0006\u0010\u0003\u001a\u00020\u00042\u0010\u0010)\u001a\f0\rR\b\u0012\u0004\u0012\u00028\u00000\u0000H\u0082H\u00f8\u0001\u0000?\u0006\u0002\u0010,JT\u0010-\u001a\u00028\u00002\u0006\u0010\u0003\u001a\u00020\u00042\u0006\u0010\u0005\u001a\u00020\u00062\u0006\u0010\u001f\u001a\u00020 2)\b\b\u0010\u0007\u001a#\b\u0001\u0012\u0004\u0012\u00020\u0004\u0012\n\u0012\b\u0012\u0004\u0012\u00028\u00000\t\u0012\u0006\u0012\u0004\u0018\u00010\u00020\b?\u0006\u0002\b\nH\u0082H\u00f8\u0001\u0000?\u0006\u0002\u0010.JA\u0010/\u001a\u00020\u001a2\u0006\u0010\u0005\u001a\u00020\u00062'\u0010\u0007\u001a#\b\u0001\u0012\u0004\u0012\u00020\u0004\u0012\n\u0012\b\u0012\u0004\u0012\u00028\u00000\t\u0012\u0006\u0012\u0004\u0018\u00010\u00020\b?\u0006\u0002\b\nH\u0002\u00f8\u0001\u0000?\u0006\u0002\u00100J/\u00101\u001a\u0002022'\u00103\u001a#\u0012\u0015\u0012\u0013\u0018\u00010\u0010?\u0006\f\b5\u0012\b\b6\u0012\u0004\b\b(#\u0012\u0004\u0012\u00020\"04j\u0002`7JI\u00108\u001a\b\u0012\u0004\u0012\u00028\u00000\u00002\n\b\u0002\u0010\u0005\u001a\u0004\u0018\u00010\u00062'\u0010\u0007\u001a#\b\u0001\u0012\u0004\u0012\u00020\u0004\u0012\n\u0012\b\u0012\u0004\u0012\u00020\"0\t\u0012\u0006\u0012\u0004\u0018\u00010\u00020\b?\u0006\u0002\b\n\u00f8\u0001\u0000?\u0006\u0002\u00109JO\u0010:\u001a\b\u0012\u0004\u0012\u00028\u00000\u00002\n\b\u0002\u0010\u0005\u001a\u0004\u0018\u00010\u00062-\u0010\u0007\u001a)\b\u0001\u0012\u0004\u0012\u00020\u0004\u0012\u0004\u0012\u00020\u0010\u0012\n\u0012\b\u0012\u0004\u0012\u00020\"0\t\u0012\u0006\u0012\u0004\u0018\u00010\u00020;?\u0006\u0002\b\n\u00f8\u0001\u0000?\u0006\u0002\u0010<J\u001c\u0010=\u001a\b\u0012\u0004\u0012\u00028\u00000\u00002\u000e\u0010(\u001a\n\u0012\u0006\u0012\u0004\u0018\u00018\u00000>J\u001b\u0010=\u001a\b\u0012\u0004\u0012\u00028\u00000\u00002\b\u0010(\u001a\u0004\u0018\u00018\u0000?\u0006\u0002\u0010?JI\u0010@\u001a\b\u0012\u0004\u0012\u00028\u00000\u00002\n\b\u0002\u0010\u0005\u001a\u0004\u0018\u00010\u00062'\u0010\u0007\u001a#\b\u0001\u0012\u0004\u0012\u00020\u0004\u0012\n\u0012\b\u0012\u0004\u0012\u00020\"0\t\u0012\u0006\u0012\u0004\u0018\u00010\u00020\b?\u0006\u0002\b\n\u00f8\u0001\u0000?\u0006\u0002\u00109JI\u0010A\u001a\b\u0012\u0004\u0012\u00028\u00000\u00002\n\b\u0002\u0010\u0005\u001a\u0004\u0018\u00010\u00062'\u0010\u0007\u001a#\b\u0001\u0012\u0004\u0012\u00020\u0004\u0012\n\u0012\b\u0012\u0004\u0012\u00020\"0\t\u0012\u0006\u0012\u0004\u0018\u00010\u00020\b?\u0006\u0002\b\n\u00f8\u0001\u0000?\u0006\u0002\u00109JO\u0010B\u001a\b\u0012\u0004\u0012\u00028\u00000\u00002\n\b\u0002\u0010\u0005\u001a\u0004\u0018\u00010\u00062-\u0010\u0007\u001a)\b\u0001\u0012\u0004\u0012\u00020\u0004\u0012\u0004\u0012\u00028\u0000\u0012\n\u0012\b\u0012\u0004\u0012\u00020\"0\t\u0012\u0006\u0012\u0004\u0018\u00010\u00020;?\u0006\u0002\b\n\u00f8\u0001\u0000?\u0006\u0002\u0010<J\u001a\u0010C\u001a\b\u0012\u0004\u0012\u00028\u00000\u00002\f\u0010\u001f\u001a\b\u0012\u0004\u0012\u00020 0>J\u0014\u0010C\u001a\b\u0012\u0004\u0012\u00028\u00000\u00002\u0006\u0010\u001f\u001a\u00020 R\u001a\u0010\f\u001a\u000e\u0018\u00010\rR\b\u0012\u0004\u0012\u00028\u00000\u0000X\u0082\u000e?\u0006\u0002\n\u0000R \u0010\u000e\u001a\u0014\u0012\u0004\u0012\u00020\u0010\u0018\u00010\u000fR\b\u0012\u0004\u0012\u00028\u00000\u0000X\u0082\u000e?\u0006\u0002\n\u0000R\u0016\u0010\u0011\u001a\n\u0012\u0004\u0012\u00028\u0000\u0018\u00010\u0012X\u0082\u000e?\u0006\u0002\n\u0000R\u001a\u0010\u0013\u001a\u000e\u0018\u00010\rR\b\u0012\u0004\u0012\u00028\u00000\u0000X\u0082\u000e?\u0006\u0002\n\u0000R\u0011\u0010\u0014\u001a\u00020\u00158F?\u0006\u0006\u001a\u0004\b\u0014\u0010\u0016R\u0011\u0010\u0017\u001a\u00020\u00158F?\u0006\u0006\u001a\u0004\b\u0017\u0010\u0016R\u0011\u0010\u0018\u001a\u00020\u00158F?\u0006\u0006\u001a\u0004\b\u0018\u0010\u0016R\u000e\u0010\u0019\u001a\u00020\u001aX\u0082\u0004?\u0006\u0002\n\u0000R\u0011\u0010\u0003\u001a\u00020\u0004?\u0006\b\n\u0000\u001a\u0004\b\u001b\u0010\u001cR\u001a\u0010\u001d\u001a\u000e\u0018\u00010\rR\b\u0012\u0004\u0012\u00028\u00000\u0000X\u0082\u000e?\u0006\u0002\n\u0000R \u0010\u001e\u001a\u0014\u0012\u0004\u0012\u00028\u0000\u0018\u00010\u000fR\b\u0012\u0004\u0012\u00028\u00000\u0000X\u0082\u000e?\u0006\u0002\n\u0000R\u0012\u0010\u001f\u001a\u0004\u0018\u00010 X\u0082\u000e?\u0006\u0004\n\u0002\u0010!\u0082\u0002\u0004\n\u0002\b\u0019��\u0006H" }, d2 = { "Lio/legado/app/help/coroutine/Coroutine;", "T", "", "scope", "Lkotlinx/coroutines/CoroutineScope;", "context", "Lkotlin/coroutines/CoroutineContext;", "block", "Lkotlin/Function2;", "Lkotlin/coroutines/Continuation;", "Lkotlin/ExtensionFunctionType;", "(Lkotlinx/coroutines/CoroutineScope;Lkotlin/coroutines/CoroutineContext;Lkotlin/jvm/functions/Function2;)V", "cancel", "Lio/legado/app/help/coroutine/Coroutine$VoidCallback;", "error", "Lio/legado/app/help/coroutine/Coroutine$Callback;", "", "errorReturn", "Lio/legado/app/help/coroutine/Coroutine$Result;", "finally", "isActive", "", "()Z", "isCancelled", "isCompleted", "job", "Lkotlinx/coroutines/Job;", "getScope", "()Lkotlinx/coroutines/CoroutineScope;", "start", "success", "timeMillis", "", "Ljava/lang/Long;", "", "cause", "Ljava/util/concurrent/CancellationException;", "Lkotlinx/coroutines/CancellationException;", "dispatchCallback", "R", "value", "callback", "(Lkotlinx/coroutines/CoroutineScope;Ljava/lang/Object;Lio/legado/app/help/coroutine/Coroutine$Callback;Lkotlin/coroutines/Continuation;)Ljava/lang/Object;", "dispatchVoidCallback", "(Lkotlinx/coroutines/CoroutineScope;Lio/legado/app/help/coroutine/Coroutine$VoidCallback;Lkotlin/coroutines/Continuation;)Ljava/lang/Object;", "executeBlock", "(Lkotlinx/coroutines/CoroutineScope;Lkotlin/coroutines/CoroutineContext;JLkotlin/jvm/functions/Function2;Lkotlin/coroutines/Continuation;)Ljava/lang/Object;", "executeInternal", "(Lkotlin/coroutines/CoroutineContext;Lkotlin/jvm/functions/Function2;)Lkotlinx/coroutines/Job;", "invokeOnCompletion", "Lkotlinx/coroutines/DisposableHandle;", "handler", "Lkotlin/Function1;", "Lkotlin/ParameterName;", "name", "Lkotlinx/coroutines/CompletionHandler;", "onCancel", "(Lkotlin/coroutines/CoroutineContext;Lkotlin/jvm/functions/Function2;)Lio/legado/app/help/coroutine/Coroutine;", "onError", "Lkotlin/Function3;", "(Lkotlin/coroutines/CoroutineContext;Lkotlin/jvm/functions/Function3;)Lio/legado/app/help/coroutine/Coroutine;", "onErrorReturn", "Lkotlin/Function0;", "(Ljava/lang/Object;)Lio/legado/app/help/coroutine/Coroutine;", "onFinally", "onStart", "onSuccess", "timeout", "Callback", "Companion", "Result", "VoidCallback", "reader-pro" })
public final class Coroutine<T>
{
    @NotNull
    public static final Companion Companion;
    @NotNull
    private final CoroutineScope scope;
    @NotNull
    private final Job job;
    @Nullable
    private VoidCallback start;
    @Nullable
    private Callback<T> success;
    @Nullable
    private Callback<Throwable> error;
    @Nullable
    private VoidCallback finally;
    @Nullable
    private VoidCallback cancel;
    @Nullable
    private Long timeMillis;
    @Nullable
    private Result<? extends T> errorReturn;
    @NotNull
    private static final CoroutineScope DEFAULT;

    public Coroutine(@NotNull final CoroutineScope scope, @NotNull final CoroutineContext context, @NotNull final Function2<? super CoroutineScope, ? super Continuation<? super T>, ?> block) {
        Intrinsics.checkNotNullParameter((Object)scope, "scope");
        Intrinsics.checkNotNullParameter((Object)context, "context");
        Intrinsics.checkNotNullParameter((Object)block, "block");
        this.scope = scope;
        this.job = this.executeInternal(context, block);
    }

    @NotNull
    public final CoroutineScope getScope() {
        return this.scope;
    }

    public final boolean isCancelled() {
        return this.job.isCancelled();
    }

    public final boolean isActive() {
        return this.job.isActive();
    }

    public final boolean isCompleted() {
        return this.job.isCompleted();
    }

    @NotNull
    public final Coroutine<T> timeout(@NotNull final Function0<Long> timeMillis) {
        Intrinsics.checkNotNullParameter((Object)timeMillis, "timeMillis");
        this.timeMillis = (Long)timeMillis.invoke();
        return this;
    }

    @NotNull
    public final Coroutine<T> timeout(final long timeMillis) {
        this.timeMillis = timeMillis;
        return this;
    }

    @NotNull
    public final Coroutine<T> onErrorReturn(@NotNull final Function0<? extends T> value) {
        Intrinsics.checkNotNullParameter((Object)value, "value");
        this.errorReturn = (Result<? extends T>)new Result<T>((T)value.invoke());
        return this;
    }

    @NotNull
    public final Coroutine<T> onErrorReturn(@Nullable final T value) {
        this.errorReturn = (Result<? extends T>)new Result<T>((T)value);
        return this;
    }

    @NotNull
    public final Coroutine<T> onStart(@Nullable final CoroutineContext context, @NotNull final Function2<? super CoroutineScope, ? super Continuation<? super Unit>, ?> block) {
        Intrinsics.checkNotNullParameter((Object)block, "block");
        this.start = new VoidCallback(context, block);
        return this;
    }

    @NotNull
    public final Coroutine<T> onSuccess(@Nullable final CoroutineContext context, @NotNull final Function3<? super CoroutineScope, ? super T, ? super Continuation<? super Unit>, ?> block) {
        Intrinsics.checkNotNullParameter((Object)block, "block");
        this.success = new Callback<T>(context, block);
        return this;
    }

    @NotNull
    public final Coroutine<T> onError(@Nullable final CoroutineContext context, @NotNull final Function3<? super CoroutineScope, ? super Throwable, ? super Continuation<? super Unit>, ?> block) {
        Intrinsics.checkNotNullParameter((Object)block, "block");
        this.error = new Callback<Throwable>(context, block);
        return this;
    }

    @NotNull
    public final Coroutine<T> onFinally(@Nullable final CoroutineContext context, @NotNull final Function2<? super CoroutineScope, ? super Continuation<? super Unit>, ?> block) {
        Intrinsics.checkNotNullParameter((Object)block, "block");
        this.finally = new VoidCallback(context, block);
        return this;
    }

    @NotNull
    public final Coroutine<T> onCancel(@Nullable final CoroutineContext context, @NotNull final Function2<? super CoroutineScope, ? super Continuation<? super Unit>, ?> block) {
        Intrinsics.checkNotNullParameter((Object)block, "block");
        this.cancel = new VoidCallback(context, block);
        return this;
    }

    public final void cancel(@Nullable final CancellationException cause) {
        this.job.cancel(cause);
        final VoidCallback cancel = this.cancel;
        if (cancel != null) {
            final VoidCallback it = cancel;
            final int n = 0;
            BuildersKt.launch$default(CoroutineScopeKt.MainScope(), (CoroutineContext)null, (CoroutineStart)null, (Function2)new Coroutine$cancel$1.Coroutine$cancel$1$1(it, this, (Continuation)null), 3, (Object)null);
        }
    }

    @NotNull
    public final DisposableHandle invokeOnCompletion(@NotNull final Function1<? super Throwable, Unit> handler) {
        Intrinsics.checkNotNullParameter((Object)handler, "handler");
        return this.job.invokeOnCompletion((Function1)handler);
    }

    private final Job executeInternal(final CoroutineContext context, final Function2<? super CoroutineScope, ? super Continuation<? super T>, ?> block) {
        return BuildersKt.launch$default(CoroutineScopeKt.plus(this.scope, (CoroutineContext)Dispatchers.getIO()), (CoroutineContext)null, (CoroutineStart)null, (Function2)new Coroutine$executeInternal.Coroutine$executeInternal$1(this, context, (Function2)block, (Continuation)null), 3, (Object)null);
    }

    private final Object dispatchVoidCallback(final CoroutineScope scope, final VoidCallback callback, final Continuation<? super Unit> $completion) {
        final int $i$f$dispatchVoidCallback = 0;
        if (callback.getContext() == null) {
            final Function2<CoroutineScope, Continuation<? super Unit>, Object> block = callback.getBlock();
            InlineMarker.mark(0);
            block.invoke((Object)scope, (Object)$completion);
            InlineMarker.mark(1);
            return Unit.INSTANCE;
        }
        final CoroutineContext plus = scope.getCoroutineContext().plus(callback.getContext());
        final Function2 function2 = (Function2)new Coroutine$dispatchVoidCallback.Coroutine$dispatchVoidCallback$2(callback, (Continuation)null);
        InlineMarker.mark(0);
        BuildersKt.withContext(plus, function2, (Continuation)$completion);
        InlineMarker.mark(1);
        return Unit.INSTANCE;
    }

    private final <R> Object dispatchCallback(final CoroutineScope scope, final R value, final Callback<R> callback, final Continuation<? super Unit> $completion) {
        final int $i$f$dispatchCallback = 0;
        if (!CoroutineScopeKt.isActive(scope)) {
            return Unit.INSTANCE;
        }
        if (callback.getContext() == null) {
            final kotlin.jvm.functions.Function3<CoroutineScope, R, Continuation<? super Unit>, Object> block = callback.getBlock();
            InlineMarker.mark(0);
            block.invoke((Object)scope, (Object)value, (Object)$completion);
            InlineMarker.mark(1);
            return Unit.INSTANCE;
        }
        final CoroutineContext plus = scope.getCoroutineContext().plus(callback.getContext());
        final Function2 function2 = (Function2)new Coroutine$dispatchCallback.Coroutine$dispatchCallback$2((Callback)callback, (Object)value, (Continuation)null);
        InlineMarker.mark(0);
        BuildersKt.withContext(plus, function2, (Continuation)$completion);
        InlineMarker.mark(1);
        return Unit.INSTANCE;
    }

    private final Object executeBlock(final CoroutineScope scope, final CoroutineContext context, final long timeMillis, final Function2<? super CoroutineScope, ? super Continuation<? super T>, ?> block, final Continuation<? super T> $completion) {
        final int $i$f$executeBlock = 0;
        final CoroutineContext plus = scope.getCoroutineContext().plus(context);
        final Function2 function2 = (Function2)new Coroutine$executeBlock.Coroutine$executeBlock$2(timeMillis, (Function2)block, (Continuation)null);
        InlineMarker.mark(0);
        final Object withContext = BuildersKt.withContext(plus, function2, (Continuation)$completion);
        InlineMarker.mark(1);
        return withContext;
    }

    static {
        Companion = new Companion(null);
        DEFAULT = CoroutineScopeKt.MainScope();
    }

    @Metadata(mv = { 1, 5, 1 }, k = 1, xi = 48, d1 = { "\u00000\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\b\u0086\u0003\u0018\u00002\u00020\u0001B\u0007\b\u0002?\u0006\u0002\u0010\u0002JW\u0010\u0005\u001a\b\u0012\u0004\u0012\u0002H\u00070\u0006\"\u0004\b\u0001\u0010\u00072\b\b\u0002\u0010\b\u001a\u00020\u00042\b\b\u0002\u0010\t\u001a\u00020\n2'\u0010\u000b\u001a#\b\u0001\u0012\u0004\u0012\u00020\u0004\u0012\n\u0012\b\u0012\u0004\u0012\u0002H\u00070\r\u0012\u0006\u0012\u0004\u0018\u00010\u00010\f?\u0006\u0002\b\u000e\u00f8\u0001\u0000?\u0006\u0002\u0010\u000fR\u000e\u0010\u0003\u001a\u00020\u0004X\u0082\u0004?\u0006\u0002\n\u0000\u0082\u0002\u0004\n\u0002\b\u0019��\u0006\u0010" }, d2 = { "Lio/legado/app/help/coroutine/Coroutine$Companion;", "", "()V", "DEFAULT", "Lkotlinx/coroutines/CoroutineScope;", "async", "Lio/legado/app/help/coroutine/Coroutine;", "T", "scope", "context", "Lkotlin/coroutines/CoroutineContext;", "block", "Lkotlin/Function2;", "Lkotlin/coroutines/Continuation;", "Lkotlin/ExtensionFunctionType;", "(Lkotlinx/coroutines/CoroutineScope;Lkotlin/coroutines/CoroutineContext;Lkotlin/jvm/functions/Function2;)Lio/legado/app/help/coroutine/Coroutine;", "reader-pro" })
    public static final class Companion
    {
        private Companion() {
        }

        @NotNull
        public final <T> Coroutine<T> async(@NotNull final CoroutineScope scope, @NotNull final CoroutineContext context, @NotNull final Function2<? super CoroutineScope, ? super Continuation<? super T>, ?> block) {
            Intrinsics.checkNotNullParameter((Object)scope, "scope");
            Intrinsics.checkNotNullParameter((Object)context, "context");
            Intrinsics.checkNotNullParameter((Object)block, "block");
            return new Coroutine<T>(scope, context, block);
        }
    }

    @Metadata(mv = { 1, 5, 1 }, k = 1, xi = 48, d1 = { "\u0000\"\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u0000\n\u0002\b\t\n\u0002\u0010\u000b\n\u0002\b\u0002\n\u0002\u0010\b\n\u0000\n\u0002\u0010\u000e\n\u0000\b\u0082\b\u0018\u0000*\u0006\b\u0001\u0010\u0001 \u00012\u00020\u0002B\u000f\u0012\b\u0010\u0003\u001a\u0004\u0018\u00018\u0001?\u0006\u0002\u0010\u0004J\u0010\u0010\b\u001a\u0004\u0018\u00018\u0001H\u00c6\u0003?\u0006\u0002\u0010\u0006J \u0010\t\u001a\b\u0012\u0004\u0012\u00028\u00010\u00002\n\b\u0002\u0010\u0003\u001a\u0004\u0018\u00018\u0001H\u00c6\u0001?\u0006\u0002\u0010\nJ\u0013\u0010\u000b\u001a\u00020\f2\b\u0010\r\u001a\u0004\u0018\u00010\u0002H\u00d6\u0003J\t\u0010\u000e\u001a\u00020\u000fH\u00d6\u0001J\t\u0010\u0010\u001a\u00020\u0011H\u00d6\u0001R\u0015\u0010\u0003\u001a\u0004\u0018\u00018\u0001?\u0006\n\n\u0002\u0010\u0007\u001a\u0004\b\u0005\u0010\u0006��\u0006\u0012" }, d2 = { "Lio/legado/app/help/coroutine/Coroutine$Result;", "T", "", "value", "(Ljava/lang/Object;)V", "getValue", "()Ljava/lang/Object;", "Ljava/lang/Object;", "component1", "copy", "(Ljava/lang/Object;)Lio/legado/app/help/coroutine/Coroutine$Result;", "equals", "", "other", "hashCode", "", "toString", "", "reader-pro" })
    private static final class Result<T>
    {
        @Nullable
        private final T value;

        public Result(@Nullable final T value) {
            this.value = value;
        }

        @Nullable
        public final T getValue() {
            return this.value;
        }

        @Nullable
        public final T component1() {
            return this.value;
        }

        @NotNull
        public final Result<T> copy(@Nullable final T value) {
            return new Result<T>(value);
        }

        @NotNull
        @Override
        public String toString() {
            return "Result(value=" + this.value + ')';
        }

        @Override
        public int hashCode() {
            return (this.value == null) ? 0 : this.value.hashCode();
        }

        @Override
        public boolean equals(@Nullable final Object other) {
            return this == other || (other instanceof Result && Intrinsics.areEqual((Object)this.value, (Object)((Result)other).value));
        }
    }

    @Metadata(mv = { 1, 5, 1 }, k = 1, xi = 48, d1 = { "\u0000(\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0010\u0002\n\u0002\u0018\u0002\n\u0002\b\u0007\b\u0082\u0004\u0018\u00002\u00020\u0001B;\u0012\b\u0010\u0002\u001a\u0004\u0018\u00010\u0003\u0012'\u0010\u0004\u001a#\b\u0001\u0012\u0004\u0012\u00020\u0006\u0012\n\u0012\b\u0012\u0004\u0012\u00020\b0\u0007\u0012\u0006\u0012\u0004\u0018\u00010\u00010\u0005?\u0006\u0002\b\t\u00f8\u0001\u0000?\u0006\u0002\u0010\nR7\u0010\u0004\u001a#\b\u0001\u0012\u0004\u0012\u00020\u0006\u0012\n\u0012\b\u0012\u0004\u0012\u00020\b0\u0007\u0012\u0006\u0012\u0004\u0018\u00010\u00010\u0005?\u0006\u0002\b\t\u00f8\u0001\u0000?\u0006\n\n\u0002\u0010\r\u001a\u0004\b\u000b\u0010\fR\u0013\u0010\u0002\u001a\u0004\u0018\u00010\u0003?\u0006\b\n\u0000\u001a\u0004\b\u000e\u0010\u000f\u0082\u0002\u0004\n\u0002\b\u0019��\u0006\u0010" }, d2 = { "Lio/legado/app/help/coroutine/Coroutine$VoidCallback;", "", "context", "Lkotlin/coroutines/CoroutineContext;", "block", "Lkotlin/Function2;", "Lkotlinx/coroutines/CoroutineScope;", "Lkotlin/coroutines/Continuation;", "", "Lkotlin/ExtensionFunctionType;", "(Lio/legado/app/help/coroutine/Coroutine;Lkotlin/coroutines/CoroutineContext;Lkotlin/jvm/functions/Function2;)V", "getBlock", "()Lkotlin/jvm/functions/Function2;", "Lkotlin/jvm/functions/Function2;", "getContext", "()Lkotlin/coroutines/CoroutineContext;", "reader-pro" })
    private final class VoidCallback
    {
        @Nullable
        private final CoroutineContext context;
        @NotNull
        private final Function2<CoroutineScope, Continuation<? super Unit>, Object> block;

        public VoidCallback(@NotNull final CoroutineContext context, final Function2<? super CoroutineScope, ? super Continuation<? super Unit>, ?> block) {
            Intrinsics.checkNotNullParameter((Object)Coroutine.this, "this$0");
            Intrinsics.checkNotNullParameter((Object)block, "block");
            this.context = context;
            this.block = (Function2<CoroutineScope, Continuation<? super Unit>, Object>)block;
        }

        @Nullable
        public final CoroutineContext getContext() {
            return this.context;
        }

        @NotNull
        public final Function2<CoroutineScope, Continuation<? super Unit>, Object> getBlock() {
            return this.block;
        }
    }

    @Metadata(mv = { 1, 5, 1 }, k = 1, xi = 48, d1 = { "\u0000*\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u0000\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0010\u0002\n\u0002\u0018\u0002\n\u0002\b\u0007\b\u0082\u0004\u0018\u0000*\u0004\b\u0001\u0010\u00012\u00020\u0002BA\u0012\b\u0010\u0003\u001a\u0004\u0018\u00010\u0004\u0012-\u0010\u0005\u001a)\b\u0001\u0012\u0004\u0012\u00020\u0007\u0012\u0004\u0012\u00028\u0001\u0012\n\u0012\b\u0012\u0004\u0012\u00020\t0\b\u0012\u0006\u0012\u0004\u0018\u00010\u00020\u0006?\u0006\u0002\b\n\u00f8\u0001\u0000?\u0006\u0002\u0010\u000bR=\u0010\u0005\u001a)\b\u0001\u0012\u0004\u0012\u00020\u0007\u0012\u0004\u0012\u00028\u0001\u0012\n\u0012\b\u0012\u0004\u0012\u00020\t0\b\u0012\u0006\u0012\u0004\u0018\u00010\u00020\u0006?\u0006\u0002\b\n\u00f8\u0001\u0000?\u0006\n\n\u0002\u0010\u000e\u001a\u0004\b\f\u0010\rR\u0013\u0010\u0003\u001a\u0004\u0018\u00010\u0004?\u0006\b\n\u0000\u001a\u0004\b\u000f\u0010\u0010\u0082\u0002\u0004\n\u0002\b\u0019��\u0006\u0011" }, d2 = { "Lio/legado/app/help/coroutine/Coroutine$Callback;", "VALUE", "", "context", "Lkotlin/coroutines/CoroutineContext;", "block", "Lkotlin/Function3;", "Lkotlinx/coroutines/CoroutineScope;", "Lkotlin/coroutines/Continuation;", "", "Lkotlin/ExtensionFunctionType;", "(Lio/legado/app/help/coroutine/Coroutine;Lkotlin/coroutines/CoroutineContext;Lkotlin/jvm/functions/Function3;)V", "getBlock", "()Lkotlin/jvm/functions/Function3;", "Lkotlin/jvm/functions/Function3;", "getContext", "()Lkotlin/coroutines/CoroutineContext;", "reader-pro" })
    private final class Callback<VALUE>
    {
        @Nullable
        private final CoroutineContext context;
        @NotNull
        private final Function3<CoroutineScope, VALUE, Continuation<? super Unit>, Object> block;

        public Callback(@NotNull final CoroutineContext context, final Function3<? super CoroutineScope, ? super VALUE, ? super Continuation<? super Unit>, ?> block) {
            Intrinsics.checkNotNullParameter((Object)Coroutine.this, "this$0");
            Intrinsics.checkNotNullParameter((Object)block, "block");
            this.context = context;
            this.block = (Function3<CoroutineScope, VALUE, Continuation<? super Unit>, Object>)block;
        }

        @Nullable
        public final CoroutineContext getContext() {
            return this.context;
        }

        @NotNull
        public final Function3<CoroutineScope, VALUE, Continuation<? super Unit>, Object> getBlock() {
            return this.block;
        }
    }
}
