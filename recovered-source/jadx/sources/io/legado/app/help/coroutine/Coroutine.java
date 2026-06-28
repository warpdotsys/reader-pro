package io.legado.app.help.coroutine;

import io.legado.app.data.entities.Book;
import java.util.concurrent.CancellationException;
import kotlin.Metadata;
import kotlin.ResultKt;
import kotlin.Unit;
import kotlin.coroutines.Continuation;
import kotlin.coroutines.CoroutineContext;
import kotlin.coroutines.intrinsics.IntrinsicsKt;
import kotlin.coroutines.jvm.internal.Boxing;
import kotlin.coroutines.jvm.internal.DebugMetadata;
import kotlin.coroutines.jvm.internal.SuspendLambda;
import kotlin.jvm.functions.Function0;
import kotlin.jvm.functions.Function1;
import kotlin.jvm.functions.Function2;
import kotlin.jvm.functions.Function3;
import kotlin.jvm.internal.DefaultConstructorMarker;
import kotlin.jvm.internal.InlineMarker;
import kotlin.jvm.internal.Intrinsics;
import kotlinx.coroutines.BuildersKt;
import kotlinx.coroutines.CoroutineScope;
import kotlinx.coroutines.CoroutineScopeKt;
import kotlinx.coroutines.CoroutineStart;
import kotlinx.coroutines.Dispatchers;
import kotlinx.coroutines.DisposableHandle;
import kotlinx.coroutines.Job;
import kotlinx.coroutines.TimeoutKt;
import me.ag2s.epublib.epub.PackageDocumentBase;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.kxml2.kdom.Node;
import org.kxml2.wap.Wbxml;

/* JADX INFO: compiled from: Coroutine.kt */
/* JADX INFO: loaded from: reader-pro-classes-3.2.14.jar:io/legado/app/help/coroutine/Coroutine.class */
@Metadata(mv = {1, 5, 1}, k = 1, xi = 48, d1 = {"\u0000\u0090\u0001\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u0000\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\u0010\u0003\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\u000b\n\u0002\b\u0004\n\u0002\u0018\u0002\n\u0002\b\u0005\n\u0002\u0010\t\n\u0000\n\u0002\u0010\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\f\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\n\u0018\u0000 E*\u0004\b\u0000\u0010\u00012\u00020\u0002:\u0004DEFGBC\u0012\u0006\u0010\u0003\u001a\u00020\u0004\u0012\b\b\u0002\u0010\u0005\u001a\u00020\u0006\u0012'\u0010\u0007\u001a#\b\u0001\u0012\u0004\u0012\u00020\u0004\u0012\n\u0012\b\u0012\u0004\u0012\u00028\u00000\t\u0012\u0006\u0012\u0004\u0018\u00010\u00020\b¢\u0006\u0002\b\nø\u0001\u0000¢\u0006\u0002\u0010\u000bJ\u0018\u0010\f\u001a\u00020\"2\u0010\b\u0002\u0010#\u001a\n\u0018\u00010$j\u0004\u0018\u0001`%J?\u0010&\u001a\u00020\"\"\u0004\b\u0001\u0010'2\u0006\u0010\u0003\u001a\u00020\u00042\u0006\u0010(\u001a\u0002H'2\u0016\u0010)\u001a\u0012\u0012\u0004\u0012\u0002H'0\u000fR\b\u0012\u0004\u0012\u00028\u00000\u0000H\u0082Hø\u0001\u0000¢\u0006\u0002\u0010*J+\u0010+\u001a\u00020\"2\u0006\u0010\u0003\u001a\u00020\u00042\u0010\u0010)\u001a\f0\rR\b\u0012\u0004\u0012\u00028\u00000\u0000H\u0082Hø\u0001\u0000¢\u0006\u0002\u0010,JT\u0010-\u001a\u00028\u00002\u0006\u0010\u0003\u001a\u00020\u00042\u0006\u0010\u0005\u001a\u00020\u00062\u0006\u0010\u001f\u001a\u00020 2)\b\b\u0010\u0007\u001a#\b\u0001\u0012\u0004\u0012\u00020\u0004\u0012\n\u0012\b\u0012\u0004\u0012\u00028\u00000\t\u0012\u0006\u0012\u0004\u0018\u00010\u00020\b¢\u0006\u0002\b\nH\u0082Hø\u0001\u0000¢\u0006\u0002\u0010.JA\u0010/\u001a\u00020\u001a2\u0006\u0010\u0005\u001a\u00020\u00062'\u0010\u0007\u001a#\b\u0001\u0012\u0004\u0012\u00020\u0004\u0012\n\u0012\b\u0012\u0004\u0012\u00028\u00000\t\u0012\u0006\u0012\u0004\u0018\u00010\u00020\b¢\u0006\u0002\b\nH\u0002ø\u0001\u0000¢\u0006\u0002\u00100J/\u00101\u001a\u0002022'\u00103\u001a#\u0012\u0015\u0012\u0013\u0018\u00010\u0010¢\u0006\f\b5\u0012\b\b6\u0012\u0004\b\b(#\u0012\u0004\u0012\u00020\"04j\u0002`7JI\u00108\u001a\b\u0012\u0004\u0012\u00028\u00000\u00002\n\b\u0002\u0010\u0005\u001a\u0004\u0018\u00010\u00062'\u0010\u0007\u001a#\b\u0001\u0012\u0004\u0012\u00020\u0004\u0012\n\u0012\b\u0012\u0004\u0012\u00020\"0\t\u0012\u0006\u0012\u0004\u0018\u00010\u00020\b¢\u0006\u0002\b\nø\u0001\u0000¢\u0006\u0002\u00109JO\u0010:\u001a\b\u0012\u0004\u0012\u00028\u00000\u00002\n\b\u0002\u0010\u0005\u001a\u0004\u0018\u00010\u00062-\u0010\u0007\u001a)\b\u0001\u0012\u0004\u0012\u00020\u0004\u0012\u0004\u0012\u00020\u0010\u0012\n\u0012\b\u0012\u0004\u0012\u00020\"0\t\u0012\u0006\u0012\u0004\u0018\u00010\u00020;¢\u0006\u0002\b\nø\u0001\u0000¢\u0006\u0002\u0010<J\u001c\u0010=\u001a\b\u0012\u0004\u0012\u00028\u00000\u00002\u000e\u0010(\u001a\n\u0012\u0006\u0012\u0004\u0018\u00018\u00000>J\u001b\u0010=\u001a\b\u0012\u0004\u0012\u00028\u00000\u00002\b\u0010(\u001a\u0004\u0018\u00018\u0000¢\u0006\u0002\u0010?JI\u0010@\u001a\b\u0012\u0004\u0012\u00028\u00000\u00002\n\b\u0002\u0010\u0005\u001a\u0004\u0018\u00010\u00062'\u0010\u0007\u001a#\b\u0001\u0012\u0004\u0012\u00020\u0004\u0012\n\u0012\b\u0012\u0004\u0012\u00020\"0\t\u0012\u0006\u0012\u0004\u0018\u00010\u00020\b¢\u0006\u0002\b\nø\u0001\u0000¢\u0006\u0002\u00109JI\u0010A\u001a\b\u0012\u0004\u0012\u00028\u00000\u00002\n\b\u0002\u0010\u0005\u001a\u0004\u0018\u00010\u00062'\u0010\u0007\u001a#\b\u0001\u0012\u0004\u0012\u00020\u0004\u0012\n\u0012\b\u0012\u0004\u0012\u00020\"0\t\u0012\u0006\u0012\u0004\u0018\u00010\u00020\b¢\u0006\u0002\b\nø\u0001\u0000¢\u0006\u0002\u00109JO\u0010B\u001a\b\u0012\u0004\u0012\u00028\u00000\u00002\n\b\u0002\u0010\u0005\u001a\u0004\u0018\u00010\u00062-\u0010\u0007\u001a)\b\u0001\u0012\u0004\u0012\u00020\u0004\u0012\u0004\u0012\u00028\u0000\u0012\n\u0012\b\u0012\u0004\u0012\u00020\"0\t\u0012\u0006\u0012\u0004\u0018\u00010\u00020;¢\u0006\u0002\b\nø\u0001\u0000¢\u0006\u0002\u0010<J\u001a\u0010C\u001a\b\u0012\u0004\u0012\u00028\u00000\u00002\f\u0010\u001f\u001a\b\u0012\u0004\u0012\u00020 0>J\u0014\u0010C\u001a\b\u0012\u0004\u0012\u00028\u00000\u00002\u0006\u0010\u001f\u001a\u00020 R\u001a\u0010\f\u001a\u000e\u0018\u00010\rR\b\u0012\u0004\u0012\u00028\u00000\u0000X\u0082\u000e¢\u0006\u0002\n\u0000R \u0010\u000e\u001a\u0014\u0012\u0004\u0012\u00020\u0010\u0018\u00010\u000fR\b\u0012\u0004\u0012\u00028\u00000\u0000X\u0082\u000e¢\u0006\u0002\n\u0000R\u0016\u0010\u0011\u001a\n\u0012\u0004\u0012\u00028\u0000\u0018\u00010\u0012X\u0082\u000e¢\u0006\u0002\n\u0000R\u001a\u0010\u0013\u001a\u000e\u0018\u00010\rR\b\u0012\u0004\u0012\u00028\u00000\u0000X\u0082\u000e¢\u0006\u0002\n\u0000R\u0011\u0010\u0014\u001a\u00020\u00158F¢\u0006\u0006\u001a\u0004\b\u0014\u0010\u0016R\u0011\u0010\u0017\u001a\u00020\u00158F¢\u0006\u0006\u001a\u0004\b\u0017\u0010\u0016R\u0011\u0010\u0018\u001a\u00020\u00158F¢\u0006\u0006\u001a\u0004\b\u0018\u0010\u0016R\u000e\u0010\u0019\u001a\u00020\u001aX\u0082\u0004¢\u0006\u0002\n\u0000R\u0011\u0010\u0003\u001a\u00020\u0004¢\u0006\b\n\u0000\u001a\u0004\b\u001b\u0010\u001cR\u001a\u0010\u001d\u001a\u000e\u0018\u00010\rR\b\u0012\u0004\u0012\u00028\u00000\u0000X\u0082\u000e¢\u0006\u0002\n\u0000R \u0010\u001e\u001a\u0014\u0012\u0004\u0012\u00028\u0000\u0018\u00010\u000fR\b\u0012\u0004\u0012\u00028\u00000\u0000X\u0082\u000e¢\u0006\u0002\n\u0000R\u0012\u0010\u001f\u001a\u0004\u0018\u00010 X\u0082\u000e¢\u0006\u0004\n\u0002\u0010!\u0082\u0002\u0004\n\u0002\b\u0019¨\u0006H"}, d2 = {"Lio/legado/app/help/coroutine/Coroutine;", "T", PackageDocumentBase.PREFIX_OPF, "scope", "Lkotlinx/coroutines/CoroutineScope;", "context", "Lkotlin/coroutines/CoroutineContext;", "block", "Lkotlin/Function2;", "Lkotlin/coroutines/Continuation;", "Lkotlin/ExtensionFunctionType;", "(Lkotlinx/coroutines/CoroutineScope;Lkotlin/coroutines/CoroutineContext;Lkotlin/jvm/functions/Function2;)V", "cancel", "Lio/legado/app/help/coroutine/Coroutine$VoidCallback;", "error", "Lio/legado/app/help/coroutine/Coroutine$Callback;", PackageDocumentBase.PREFIX_OPF, "errorReturn", "Lio/legado/app/help/coroutine/Coroutine$Result;", "finally", "isActive", PackageDocumentBase.PREFIX_OPF, "()Z", "isCancelled", "isCompleted", "job", "Lkotlinx/coroutines/Job;", "getScope", "()Lkotlinx/coroutines/CoroutineScope;", "start", "success", "timeMillis", PackageDocumentBase.PREFIX_OPF, "Ljava/lang/Long;", PackageDocumentBase.PREFIX_OPF, "cause", "Ljava/util/concurrent/CancellationException;", "Lkotlinx/coroutines/CancellationException;", "dispatchCallback", "R", "value", "callback", "(Lkotlinx/coroutines/CoroutineScope;Ljava/lang/Object;Lio/legado/app/help/coroutine/Coroutine$Callback;Lkotlin/coroutines/Continuation;)Ljava/lang/Object;", "dispatchVoidCallback", "(Lkotlinx/coroutines/CoroutineScope;Lio/legado/app/help/coroutine/Coroutine$VoidCallback;Lkotlin/coroutines/Continuation;)Ljava/lang/Object;", "executeBlock", "(Lkotlinx/coroutines/CoroutineScope;Lkotlin/coroutines/CoroutineContext;JLkotlin/jvm/functions/Function2;Lkotlin/coroutines/Continuation;)Ljava/lang/Object;", "executeInternal", "(Lkotlin/coroutines/CoroutineContext;Lkotlin/jvm/functions/Function2;)Lkotlinx/coroutines/Job;", "invokeOnCompletion", "Lkotlinx/coroutines/DisposableHandle;", "handler", "Lkotlin/Function1;", "Lkotlin/ParameterName;", "name", "Lkotlinx/coroutines/CompletionHandler;", "onCancel", "(Lkotlin/coroutines/CoroutineContext;Lkotlin/jvm/functions/Function2;)Lio/legado/app/help/coroutine/Coroutine;", "onError", "Lkotlin/Function3;", "(Lkotlin/coroutines/CoroutineContext;Lkotlin/jvm/functions/Function3;)Lio/legado/app/help/coroutine/Coroutine;", "onErrorReturn", "Lkotlin/Function0;", "(Ljava/lang/Object;)Lio/legado/app/help/coroutine/Coroutine;", "onFinally", "onStart", "onSuccess", "timeout", "Callback", "Companion", "Result", "VoidCallback", "reader-pro"})
public final class Coroutine<T> {

    @NotNull
    private final CoroutineScope scope;

    @NotNull
    private final Job job;

    @Nullable
    private Coroutine<T>.VoidCallback start;

    @Nullable
    private Coroutine<T>.Callback<T> success;

    @Nullable
    private Coroutine<T>.Callback<Throwable> error;

    @Nullable
    private Coroutine<T>.VoidCallback finally;

    @Nullable
    private Coroutine<T>.VoidCallback cancel;

    @Nullable
    private Long timeMillis;

    @Nullable
    private Result<? extends T> errorReturn;

    /* JADX INFO: renamed from: Companion, reason: from kotlin metadata */
    @NotNull
    public static final Companion INSTANCE = new Companion(null);

    @NotNull
    private static final CoroutineScope DEFAULT = CoroutineScopeKt.MainScope();

    public Coroutine(@NotNull CoroutineScope scope, @NotNull CoroutineContext context, @NotNull Function2<? super CoroutineScope, ? super Continuation<? super T>, ? extends Object> block) {
        Intrinsics.checkNotNullParameter(scope, "scope");
        Intrinsics.checkNotNullParameter(context, "context");
        Intrinsics.checkNotNullParameter(block, "block");
        this.scope = scope;
        this.job = executeInternal(context, block);
    }

    public /* synthetic */ Coroutine(CoroutineScope coroutineScope, CoroutineContext coroutineContext, Function2 function2, int i, DefaultConstructorMarker defaultConstructorMarker) {
        this(coroutineScope, (i & 2) != 0 ? (CoroutineContext) Dispatchers.getIO() : coroutineContext, function2);
    }

    @NotNull
    public final CoroutineScope getScope() {
        return this.scope;
    }

    /* JADX INFO: compiled from: Coroutine.kt */
    /* JADX INFO: loaded from: reader-pro-classes-3.2.14.jar:io/legado/app/help/coroutine/Coroutine$Companion.class */
    @Metadata(mv = {1, 5, 1}, k = 1, xi = 48, d1 = {"\u00000\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\b\u0086\u0003\u0018\u00002\u00020\u0001B\u0007\b\u0002¢\u0006\u0002\u0010\u0002JW\u0010\u0005\u001a\b\u0012\u0004\u0012\u0002H\u00070\u0006\"\u0004\b\u0001\u0010\u00072\b\b\u0002\u0010\b\u001a\u00020\u00042\b\b\u0002\u0010\t\u001a\u00020\n2'\u0010\u000b\u001a#\b\u0001\u0012\u0004\u0012\u00020\u0004\u0012\n\u0012\b\u0012\u0004\u0012\u0002H\u00070\r\u0012\u0006\u0012\u0004\u0018\u00010\u00010\f¢\u0006\u0002\b\u000eø\u0001\u0000¢\u0006\u0002\u0010\u000fR\u000e\u0010\u0003\u001a\u00020\u0004X\u0082\u0004¢\u0006\u0002\n\u0000\u0082\u0002\u0004\n\u0002\b\u0019¨\u0006\u0010"}, d2 = {"Lio/legado/app/help/coroutine/Coroutine$Companion;", PackageDocumentBase.PREFIX_OPF, "()V", Book.imgStyleDefault, "Lkotlinx/coroutines/CoroutineScope;", "async", "Lio/legado/app/help/coroutine/Coroutine;", "T", "scope", "context", "Lkotlin/coroutines/CoroutineContext;", "block", "Lkotlin/Function2;", "Lkotlin/coroutines/Continuation;", "Lkotlin/ExtensionFunctionType;", "(Lkotlinx/coroutines/CoroutineScope;Lkotlin/coroutines/CoroutineContext;Lkotlin/jvm/functions/Function2;)Lio/legado/app/help/coroutine/Coroutine;", "reader-pro"})
    public static final class Companion {
        public /* synthetic */ Companion(DefaultConstructorMarker $constructor_marker) {
            this();
        }

        private Companion() {
        }

        public static /* synthetic */ Coroutine async$default(Companion companion, CoroutineScope coroutineScope, CoroutineContext coroutineContext, Function2 function2, int i, Object obj) {
            if ((i & 1) != 0) {
                coroutineScope = Coroutine.DEFAULT;
            }
            if ((i & 2) != 0) {
                coroutineContext = (CoroutineContext) Dispatchers.getIO();
            }
            return companion.async(coroutineScope, coroutineContext, function2);
        }

        @NotNull
        public final <T> Coroutine<T> async(@NotNull CoroutineScope scope, @NotNull CoroutineContext context, @NotNull Function2<? super CoroutineScope, ? super Continuation<? super T>, ? extends Object> block) {
            Intrinsics.checkNotNullParameter(scope, "scope");
            Intrinsics.checkNotNullParameter(context, "context");
            Intrinsics.checkNotNullParameter(block, "block");
            return new Coroutine<>(scope, context, block);
        }
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
    public final Coroutine<T> timeout(@NotNull Function0<Long> timeMillis) {
        Intrinsics.checkNotNullParameter(timeMillis, "timeMillis");
        this.timeMillis = (Long) timeMillis.invoke();
        return this;
    }

    @NotNull
    public final Coroutine<T> timeout(long timeMillis) {
        this.timeMillis = Long.valueOf(timeMillis);
        return this;
    }

    @NotNull
    public final Coroutine<T> onErrorReturn(@NotNull Function0<? extends T> value) {
        Intrinsics.checkNotNullParameter(value, "value");
        this.errorReturn = new Result<>(value.invoke());
        return this;
    }

    @NotNull
    public final Coroutine<T> onErrorReturn(@Nullable T value) {
        this.errorReturn = new Result<>(value);
        return this;
    }

    public static /* synthetic */ Coroutine onStart$default(Coroutine coroutine, CoroutineContext coroutineContext, Function2 function2, int i, Object obj) {
        if ((i & 1) != 0) {
            coroutineContext = null;
        }
        return coroutine.onStart(coroutineContext, function2);
    }

    @NotNull
    public final Coroutine<T> onStart(@Nullable CoroutineContext context, @NotNull Function2<? super CoroutineScope, ? super Continuation<? super Unit>, ? extends Object> block) {
        Intrinsics.checkNotNullParameter(block, "block");
        this.start = new VoidCallback(this, context, block);
        return this;
    }

    public static /* synthetic */ Coroutine onSuccess$default(Coroutine coroutine, CoroutineContext coroutineContext, Function3 function3, int i, Object obj) {
        if ((i & 1) != 0) {
            coroutineContext = null;
        }
        return coroutine.onSuccess(coroutineContext, function3);
    }

    @NotNull
    public final Coroutine<T> onSuccess(@Nullable CoroutineContext context, @NotNull Function3<? super CoroutineScope, ? super T, ? super Continuation<? super Unit>, ? extends Object> block) {
        Intrinsics.checkNotNullParameter(block, "block");
        this.success = new Callback<>(this, context, block);
        return this;
    }

    public static /* synthetic */ Coroutine onError$default(Coroutine coroutine, CoroutineContext coroutineContext, Function3 function3, int i, Object obj) {
        if ((i & 1) != 0) {
            coroutineContext = null;
        }
        return coroutine.onError(coroutineContext, function3);
    }

    @NotNull
    public final Coroutine<T> onError(@Nullable CoroutineContext context, @NotNull Function3<? super CoroutineScope, ? super Throwable, ? super Continuation<? super Unit>, ? extends Object> block) {
        Intrinsics.checkNotNullParameter(block, "block");
        this.error = new Callback<>(this, context, block);
        return this;
    }

    public static /* synthetic */ Coroutine onFinally$default(Coroutine coroutine, CoroutineContext coroutineContext, Function2 function2, int i, Object obj) {
        if ((i & 1) != 0) {
            coroutineContext = null;
        }
        return coroutine.onFinally(coroutineContext, function2);
    }

    @NotNull
    public final Coroutine<T> onFinally(@Nullable CoroutineContext context, @NotNull Function2<? super CoroutineScope, ? super Continuation<? super Unit>, ? extends Object> block) {
        Intrinsics.checkNotNullParameter(block, "block");
        this.finally = new VoidCallback(this, context, block);
        return this;
    }

    public static /* synthetic */ Coroutine onCancel$default(Coroutine coroutine, CoroutineContext coroutineContext, Function2 function2, int i, Object obj) {
        if ((i & 1) != 0) {
            coroutineContext = null;
        }
        return coroutine.onCancel(coroutineContext, function2);
    }

    @NotNull
    public final Coroutine<T> onCancel(@Nullable CoroutineContext context, @NotNull Function2<? super CoroutineScope, ? super Continuation<? super Unit>, ? extends Object> block) {
        Intrinsics.checkNotNullParameter(block, "block");
        this.cancel = new VoidCallback(this, context, block);
        return this;
    }

    public static /* synthetic */ void cancel$default(Coroutine coroutine, CancellationException cancellationException, int i, Object obj) {
        if ((i & 1) != 0) {
            cancellationException = null;
        }
        coroutine.cancel(cancellationException);
    }

    public final void cancel(@Nullable CancellationException cause) {
        this.job.cancel(cause);
        Coroutine<T>.VoidCallback voidCallback = this.cancel;
        if (voidCallback != null) {
            BuildersKt.launch$default(CoroutineScopeKt.MainScope(), (CoroutineContext) null, (CoroutineStart) null, new Coroutine$cancel$1$1(voidCallback, this, null), 3, (Object) null);
        }
    }

    @NotNull
    public final DisposableHandle invokeOnCompletion(@NotNull Function1<? super Throwable, Unit> handler) {
        Intrinsics.checkNotNullParameter(handler, "handler");
        return this.job.invokeOnCompletion(handler);
    }

    /* JADX INFO: renamed from: io.legado.app.help.coroutine.Coroutine$executeInternal$1, reason: invalid class name */
    /* JADX INFO: compiled from: Coroutine.kt */
    /* JADX INFO: loaded from: reader-pro-classes-3.2.14.jar:io/legado/app/help/coroutine/Coroutine$executeInternal$1.class */
    @Metadata(mv = {1, 5, 1}, k = 3, xi = 48, d1 = {"\u0000\f\n\u0000\n\u0002\u0010\u0002\n\u0000\n\u0002\u0018\u0002\u0010\u0000\u001a\u00020\u0001\"\u0004\b\u0000\u0010\u0002*\u00020\u0003H\u008a@"}, d2 = {"<anonymous>", PackageDocumentBase.PREFIX_OPF, "T", "Lkotlinx/coroutines/CoroutineScope;"})
    @DebugMetadata(f = "Coroutine.kt", l = {217, 219, 224, 227, 229, 235, 237, 244, 246, 253, 255, 261, 263, 261, 263}, i = {0, 1, 7, 8}, s = {"L$0", "L$0", "L$1", "L$1"}, n = {"$this$launch", "$this$launch", "e", "e"}, m = "invokeSuspend", c = "io.legado.app.help.coroutine.Coroutine$executeInternal$1")
    static final class AnonymousClass1 extends SuspendLambda implements Function2<CoroutineScope, Continuation<? super Unit>, Object> {
        Object L$1;
        int label;
        private /* synthetic */ Object L$0;
        final /* synthetic */ Coroutine<T> this$0;
        final /* synthetic */ CoroutineContext $context;
        final /* synthetic */ Function2<CoroutineScope, Continuation<? super T>, Object> $block;

        /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
        AnonymousClass1(Coroutine<T> this$0, CoroutineContext $context, Function2<? super CoroutineScope, ? super Continuation<? super T>, ? extends Object> $block, Continuation<? super AnonymousClass1> $completion) {
            super(2, $completion);
            this.this$0 = this$0;
            this.$context = $context;
            this.$block = $block;
        }

        @NotNull
        public final Continuation<Unit> create(@Nullable Object value, @NotNull Continuation<?> $completion) {
            Continuation<Unit> anonymousClass1 = new AnonymousClass1(this.this$0, this.$context, this.$block, $completion);
            anonymousClass1.L$0 = value;
            return anonymousClass1;
        }

        @Nullable
        public final Object invoke(@NotNull CoroutineScope p1, @Nullable Continuation<? super Unit> p2) {
            return create(p1, p2).invokeSuspend(Unit.INSTANCE);
        }

        /* JADX WARN: Removed duplicated region for block: B:126:0x046e  */
        /* JADX WARN: Removed duplicated region for block: B:127:0x0472 A[Catch: all -> 0x0622, TryCatch #0 {all -> 0x0622, blocks: (B:5:0x0064, B:27:0x0123, B:31:0x0150, B:38:0x01a3, B:40:0x01ac, B:43:0x01be, B:64:0x0275, B:46:0x01e9, B:48:0x01f1, B:56:0x022a, B:30:0x014b, B:8:0x0075, B:10:0x009d, B:26:0x011f, B:18:0x00d5, B:90:0x0334, B:130:0x047e, B:132:0x0485, B:135:0x0497, B:156:0x055a, B:138:0x04c2, B:140:0x04ca, B:148:0x0509, B:93:0x034a, B:96:0x035a, B:98:0x0378, B:101:0x0388, B:122:0x045d, B:104:0x03ad, B:106:0x03b5, B:114:0x0400, B:124:0x0462, B:127:0x0472, B:16:0x00cb, B:24:0x0118, B:37:0x019d, B:54:0x0220, B:62:0x026e, B:112:0x03f6, B:120:0x0456, B:146:0x04ff, B:154:0x0553), top: B:210:0x0009, inners: #1 }] */
        /* JADX WARN: Removed duplicated region for block: B:130:0x047e A[Catch: all -> 0x0622, TryCatch #0 {all -> 0x0622, blocks: (B:5:0x0064, B:27:0x0123, B:31:0x0150, B:38:0x01a3, B:40:0x01ac, B:43:0x01be, B:64:0x0275, B:46:0x01e9, B:48:0x01f1, B:56:0x022a, B:30:0x014b, B:8:0x0075, B:10:0x009d, B:26:0x011f, B:18:0x00d5, B:90:0x0334, B:130:0x047e, B:132:0x0485, B:135:0x0497, B:156:0x055a, B:138:0x04c2, B:140:0x04ca, B:148:0x0509, B:93:0x034a, B:96:0x035a, B:98:0x0378, B:101:0x0388, B:122:0x045d, B:104:0x03ad, B:106:0x03b5, B:114:0x0400, B:124:0x0462, B:127:0x0472, B:16:0x00cb, B:24:0x0118, B:37:0x019d, B:54:0x0220, B:62:0x026e, B:112:0x03f6, B:120:0x0456, B:146:0x04ff, B:154:0x0553), top: B:210:0x0009, inners: #1 }] */
        /* JADX WARN: Removed duplicated region for block: B:137:0x04bf  */
        /* JADX WARN: Removed duplicated region for block: B:138:0x04c2 A[Catch: all -> 0x0622, TryCatch #0 {all -> 0x0622, blocks: (B:5:0x0064, B:27:0x0123, B:31:0x0150, B:38:0x01a3, B:40:0x01ac, B:43:0x01be, B:64:0x0275, B:46:0x01e9, B:48:0x01f1, B:56:0x022a, B:30:0x014b, B:8:0x0075, B:10:0x009d, B:26:0x011f, B:18:0x00d5, B:90:0x0334, B:130:0x047e, B:132:0x0485, B:135:0x0497, B:156:0x055a, B:138:0x04c2, B:140:0x04ca, B:148:0x0509, B:93:0x034a, B:96:0x035a, B:98:0x0378, B:101:0x0388, B:122:0x045d, B:104:0x03ad, B:106:0x03b5, B:114:0x0400, B:124:0x0462, B:127:0x0472, B:16:0x00cb, B:24:0x0118, B:37:0x019d, B:54:0x0220, B:62:0x026e, B:112:0x03f6, B:120:0x0456, B:146:0x04ff, B:154:0x0553), top: B:210:0x0009, inners: #1 }] */
        /* JADX WARN: Removed duplicated region for block: B:161:0x0566  */
        /* JADX WARN: Removed duplicated region for block: B:166:0x059e  */
        /* JADX WARN: Removed duplicated region for block: B:172:0x05d3  */
        /* JADX WARN: Removed duplicated region for block: B:29:0x0147  */
        /* JADX WARN: Removed duplicated region for block: B:30:0x014b A[Catch: Throwable -> 0x0333, all -> 0x0622, TryCatch #1 {Throwable -> 0x0333, blocks: (B:5:0x0064, B:27:0x0123, B:31:0x0150, B:38:0x01a3, B:40:0x01ac, B:43:0x01be, B:64:0x0275, B:46:0x01e9, B:48:0x01f1, B:56:0x022a, B:30:0x014b, B:8:0x0075, B:10:0x009d, B:26:0x011f, B:18:0x00d5, B:16:0x00cb, B:24:0x0118, B:37:0x019d, B:54:0x0220, B:62:0x026e), top: B:210:0x0009, outer: #0 }] */
        /* JADX WARN: Removed duplicated region for block: B:34:0x018f  */
        /* JADX WARN: Removed duplicated region for block: B:40:0x01ac A[Catch: Throwable -> 0x0333, all -> 0x0622, TryCatch #1 {Throwable -> 0x0333, blocks: (B:5:0x0064, B:27:0x0123, B:31:0x0150, B:38:0x01a3, B:40:0x01ac, B:43:0x01be, B:64:0x0275, B:46:0x01e9, B:48:0x01f1, B:56:0x022a, B:30:0x014b, B:8:0x0075, B:10:0x009d, B:26:0x011f, B:18:0x00d5, B:16:0x00cb, B:24:0x0118, B:37:0x019d, B:54:0x0220, B:62:0x026e), top: B:210:0x0009, outer: #0 }] */
        /* JADX WARN: Removed duplicated region for block: B:45:0x01e6  */
        /* JADX WARN: Removed duplicated region for block: B:46:0x01e9 A[Catch: Throwable -> 0x0333, all -> 0x0622, TryCatch #1 {Throwable -> 0x0333, blocks: (B:5:0x0064, B:27:0x0123, B:31:0x0150, B:38:0x01a3, B:40:0x01ac, B:43:0x01be, B:64:0x0275, B:46:0x01e9, B:48:0x01f1, B:56:0x022a, B:30:0x014b, B:8:0x0075, B:10:0x009d, B:26:0x011f, B:18:0x00d5, B:16:0x00cb, B:24:0x0118, B:37:0x019d, B:54:0x0220, B:62:0x026e), top: B:210:0x0009, outer: #0 }] */
        /* JADX WARN: Removed duplicated region for block: B:69:0x0281  */
        /* JADX WARN: Removed duplicated region for block: B:74:0x02b9  */
        /* JADX WARN: Removed duplicated region for block: B:80:0x02e9  */
        @Nullable
        /*
            Code decompiled incorrectly, please refer to instructions dump.
        */
        public final Object invokeSuspend(@NotNull Object $result) throws Throwable {
            Object value;
            Callback it;
            boolean zBooleanValue;
            VoidCallback it2;
            CoroutineScope $this$launch;
            VoidCallback it3;
            boolean consume;
            Callback it4;
            Object objWithContext;
            VoidCallback it5;
            Callback it6;
            Object coroutine_suspended = IntrinsicsKt.getCOROUTINE_SUSPENDED();
            try {
                try {
                } catch (Throwable th) {
                    e = th;
                    e.printStackTrace();
                    Result result = ((Coroutine) this.this$0).errorReturn;
                    if (result == null || (value = result.getValue()) == null) {
                        zBooleanValue = false;
                        consume = zBooleanValue;
                        if (!consume) {
                        }
                        if (CoroutineScopeKt.isActive($this$launch)) {
                        }
                    } else {
                        Coroutine<T> coroutine = this.this$0;
                        if (CoroutineScopeKt.isActive($this$launch) && (it = ((Coroutine) coroutine).success) != null) {
                            if (CoroutineScopeKt.isActive($this$launch)) {
                                if (it.getContext() == null) {
                                    this.L$0 = $this$launch;
                                    this.L$1 = e;
                                    this.label = 8;
                                    if (it.getBlock().invoke($this$launch, value, this) == coroutine_suspended) {
                                        return coroutine_suspended;
                                    }
                                } else {
                                    CoroutineContext coroutineContextPlus = $this$launch.getCoroutineContext().plus(it.getContext());
                                    AnonymousClass2 anonymousClass2 = new AnonymousClass2(it, value, null);
                                    this.L$0 = $this$launch;
                                    this.L$1 = e;
                                    this.label = 9;
                                    if (BuildersKt.withContext(coroutineContextPlus, anonymousClass2, this) == coroutine_suspended) {
                                        return coroutine_suspended;
                                    }
                                }
                            }
                            Unit unit = Unit.INSTANCE;
                        }
                        Boolean boolBoxBoolean = Boxing.boxBoolean(true);
                        if (boolBoxBoolean == null) {
                        }
                        consume = zBooleanValue;
                        if (!consume) {
                        }
                        if (CoroutineScopeKt.isActive($this$launch)) {
                        }
                    }
                }
            } catch (Throwable th2) {
                th = th2;
                if (CoroutineScopeKt.isActive($this$launch) && (it2 = ((Coroutine) this.this$0).finally) != null) {
                    Coroutine<T> coroutine2 = this.this$0;
                    if (it2.getContext() == null) {
                        this.L$0 = th;
                        this.L$1 = null;
                        this.label = 14;
                        if (it2.getBlock().invoke($this$launch, this) == coroutine_suspended) {
                            return coroutine_suspended;
                        }
                    } else {
                        CoroutineContext coroutineContextPlus2 = $this$launch.getCoroutineContext().plus(it2.getContext());
                        C01482 c01482 = new C01482(it2, null);
                        this.L$0 = th;
                        this.L$1 = null;
                        this.label = 15;
                        if (BuildersKt.withContext(coroutineContextPlus2, c01482, this) == coroutine_suspended) {
                            return coroutine_suspended;
                        }
                    }
                }
                throw th;
            }
            switch (this.label) {
                case 0:
                    ResultKt.throwOnFailure($result);
                    $this$launch = (CoroutineScope) this.L$0;
                    VoidCallback it7 = ((Coroutine) this.this$0).start;
                    if (it7 == null) {
                        Coroutine<T> coroutine3 = this.this$0;
                        CoroutineScope scope$iv = this.this$0.getScope();
                        CoroutineContext context$iv = this.$context;
                        Long l = ((Coroutine) this.this$0).timeMillis;
                        long timeMillis$iv = l == null ? 0L : l.longValue();
                        Function2<CoroutineScope, Continuation<? super T>, Object> function2 = this.$block;
                        this.L$0 = $this$launch;
                        this.label = 3;
                        objWithContext = BuildersKt.withContext(scope$iv.getCoroutineContext().plus(context$iv), new C01492(timeMillis$iv, function2, null), (Continuation) this);
                        if (objWithContext == coroutine_suspended) {
                            return coroutine_suspended;
                        }
                        Object value2 = objWithContext;
                        if (CoroutineScopeKt.isActive($this$launch) && (it6 = ((Coroutine) this.this$0).success) != null) {
                            Coroutine<T> coroutine4 = this.this$0;
                            if (CoroutineScopeKt.isActive($this$launch)) {
                                if (it6.getContext() == null) {
                                    this.L$0 = $this$launch;
                                    this.label = 4;
                                    if (it6.getBlock().invoke($this$launch, value2, this) == coroutine_suspended) {
                                        return coroutine_suspended;
                                    }
                                } else {
                                    CoroutineContext coroutineContextPlus3 = $this$launch.getCoroutineContext().plus(it6.getContext());
                                    AnonymousClass2 anonymousClass22 = new AnonymousClass2(it6, value2, null);
                                    this.L$0 = $this$launch;
                                    this.label = 5;
                                    if (BuildersKt.withContext(coroutineContextPlus3, anonymousClass22, this) == coroutine_suspended) {
                                        return coroutine_suspended;
                                    }
                                }
                            }
                            Unit unit2 = Unit.INSTANCE;
                            break;
                        }
                        if (CoroutineScopeKt.isActive($this$launch) && (it5 = ((Coroutine) this.this$0).finally) != null) {
                            Coroutine<T> coroutine5 = this.this$0;
                            if (it5.getContext() != null) {
                                this.L$0 = null;
                                this.label = 6;
                                if (it5.getBlock().invoke($this$launch, this) == coroutine_suspended) {
                                    return coroutine_suspended;
                                }
                            } else {
                                CoroutineContext coroutineContextPlus4 = $this$launch.getCoroutineContext().plus(it5.getContext());
                                C01482 c014822 = new C01482(it5, null);
                                this.L$0 = null;
                                this.label = 7;
                                if (BuildersKt.withContext(coroutineContextPlus4, c014822, this) == coroutine_suspended) {
                                    return coroutine_suspended;
                                }
                            }
                            Unit unit3 = Unit.INSTANCE;
                        }
                        return Unit.INSTANCE;
                    }
                    Coroutine<T> coroutine6 = this.this$0;
                    if (it7.getContext() != null) {
                        CoroutineContext coroutineContextPlus5 = $this$launch.getCoroutineContext().plus(it7.getContext());
                        C01482 c014823 = new C01482(it7, null);
                        this.L$0 = $this$launch;
                        this.label = 2;
                        if (BuildersKt.withContext(coroutineContextPlus5, c014823, this) == coroutine_suspended) {
                            return coroutine_suspended;
                        }
                        Unit unit4 = Unit.INSTANCE;
                        Coroutine<T> coroutine32 = this.this$0;
                        CoroutineScope scope$iv2 = this.this$0.getScope();
                        CoroutineContext context$iv2 = this.$context;
                        Long l2 = ((Coroutine) this.this$0).timeMillis;
                        long timeMillis$iv2 = l2 == null ? 0L : l2.longValue();
                        Function2<CoroutineScope, Continuation<? super T>, Object> function22 = this.$block;
                        this.L$0 = $this$launch;
                        this.label = 3;
                        objWithContext = BuildersKt.withContext(scope$iv2.getCoroutineContext().plus(context$iv2), new C01492(timeMillis$iv2, function22, null), (Continuation) this);
                        if (objWithContext == coroutine_suspended) {
                        }
                        Object value22 = objWithContext;
                        if (CoroutineScopeKt.isActive($this$launch)) {
                        }
                        if (CoroutineScopeKt.isActive($this$launch)) {
                        }
                        return Unit.INSTANCE;
                    }
                    Function2<CoroutineScope, Continuation<? super Unit>, Object> block = it7.getBlock();
                    this.L$0 = $this$launch;
                    this.label = 1;
                    if (block.invoke($this$launch, this) == coroutine_suspended) {
                        return coroutine_suspended;
                    }
                    Unit unit42 = Unit.INSTANCE;
                    Coroutine<T> coroutine322 = this.this$0;
                    CoroutineScope scope$iv22 = this.this$0.getScope();
                    CoroutineContext context$iv22 = this.$context;
                    Long l22 = ((Coroutine) this.this$0).timeMillis;
                    long timeMillis$iv22 = l22 == null ? 0L : l22.longValue();
                    Function2<CoroutineScope, Continuation<? super T>, Object> function222 = this.$block;
                    this.L$0 = $this$launch;
                    this.label = 3;
                    objWithContext = BuildersKt.withContext(scope$iv22.getCoroutineContext().plus(context$iv22), new C01492(timeMillis$iv22, function222, null), (Continuation) this);
                    if (objWithContext == coroutine_suspended) {
                    }
                    Object value222 = objWithContext;
                    if (CoroutineScopeKt.isActive($this$launch)) {
                        Coroutine<T> coroutine42 = this.this$0;
                        if (CoroutineScopeKt.isActive($this$launch)) {
                        }
                        Unit unit22 = Unit.INSTANCE;
                        break;
                    }
                    if (CoroutineScopeKt.isActive($this$launch)) {
                        Coroutine<T> coroutine52 = this.this$0;
                        if (it5.getContext() != null) {
                        }
                        Unit unit32 = Unit.INSTANCE;
                    }
                    return Unit.INSTANCE;
                case 1:
                    $this$launch = (CoroutineScope) this.L$0;
                    ResultKt.throwOnFailure($result);
                    Unit unit422 = Unit.INSTANCE;
                    Coroutine<T> coroutine3222 = this.this$0;
                    CoroutineScope scope$iv222 = this.this$0.getScope();
                    CoroutineContext context$iv222 = this.$context;
                    Long l222 = ((Coroutine) this.this$0).timeMillis;
                    long timeMillis$iv222 = l222 == null ? 0L : l222.longValue();
                    Function2<CoroutineScope, Continuation<? super T>, Object> function2222 = this.$block;
                    this.L$0 = $this$launch;
                    this.label = 3;
                    objWithContext = BuildersKt.withContext(scope$iv222.getCoroutineContext().plus(context$iv222), new C01492(timeMillis$iv222, function2222, null), (Continuation) this);
                    if (objWithContext == coroutine_suspended) {
                    }
                    Object value2222 = objWithContext;
                    if (CoroutineScopeKt.isActive($this$launch)) {
                    }
                    if (CoroutineScopeKt.isActive($this$launch)) {
                    }
                    return Unit.INSTANCE;
                case 2:
                    $this$launch = (CoroutineScope) this.L$0;
                    ResultKt.throwOnFailure($result);
                    Unit unit4222 = Unit.INSTANCE;
                    Coroutine<T> coroutine32222 = this.this$0;
                    CoroutineScope scope$iv2222 = this.this$0.getScope();
                    CoroutineContext context$iv2222 = this.$context;
                    Long l2222 = ((Coroutine) this.this$0).timeMillis;
                    long timeMillis$iv2222 = l2222 == null ? 0L : l2222.longValue();
                    Function2<CoroutineScope, Continuation<? super T>, Object> function22222 = this.$block;
                    this.L$0 = $this$launch;
                    this.label = 3;
                    objWithContext = BuildersKt.withContext(scope$iv2222.getCoroutineContext().plus(context$iv2222), new C01492(timeMillis$iv2222, function22222, null), (Continuation) this);
                    if (objWithContext == coroutine_suspended) {
                    }
                    Object value22222 = objWithContext;
                    if (CoroutineScopeKt.isActive($this$launch)) {
                    }
                    if (CoroutineScopeKt.isActive($this$launch)) {
                    }
                    return Unit.INSTANCE;
                case 3:
                    $this$launch = (CoroutineScope) this.L$0;
                    ResultKt.throwOnFailure($result);
                    objWithContext = $result;
                    Object value222222 = objWithContext;
                    if (CoroutineScopeKt.isActive($this$launch)) {
                    }
                    if (CoroutineScopeKt.isActive($this$launch)) {
                    }
                    return Unit.INSTANCE;
                case 4:
                    $this$launch = (CoroutineScope) this.L$0;
                    ResultKt.throwOnFailure($result);
                    Unit unit222 = Unit.INSTANCE;
                    if (CoroutineScopeKt.isActive($this$launch)) {
                    }
                    return Unit.INSTANCE;
                case 5:
                    $this$launch = (CoroutineScope) this.L$0;
                    ResultKt.throwOnFailure($result);
                    Unit unit2222 = Unit.INSTANCE;
                    if (CoroutineScopeKt.isActive($this$launch)) {
                    }
                    return Unit.INSTANCE;
                case 6:
                    ResultKt.throwOnFailure($result);
                    Unit unit322 = Unit.INSTANCE;
                    return Unit.INSTANCE;
                case 7:
                    ResultKt.throwOnFailure($result);
                    Unit unit3222 = Unit.INSTANCE;
                    return Unit.INSTANCE;
                case 8:
                    e = (Throwable) this.L$1;
                    $this$launch = (CoroutineScope) this.L$0;
                    ResultKt.throwOnFailure($result);
                    Unit unit5 = Unit.INSTANCE;
                    Boolean boolBoxBoolean2 = Boxing.boxBoolean(true);
                    zBooleanValue = boolBoxBoolean2 == null ? false : boolBoxBoolean2.booleanValue();
                    consume = zBooleanValue;
                    if (!consume && CoroutineScopeKt.isActive($this$launch) && (it4 = ((Coroutine) this.this$0).error) != null) {
                        Coroutine<T> coroutine7 = this.this$0;
                        if (CoroutineScopeKt.isActive($this$launch)) {
                            if (it4.getContext() == null) {
                                this.L$0 = $this$launch;
                                this.L$1 = null;
                                this.label = 10;
                                if (it4.getBlock().invoke($this$launch, e, this) == coroutine_suspended) {
                                    return coroutine_suspended;
                                }
                            } else {
                                CoroutineContext coroutineContextPlus6 = $this$launch.getCoroutineContext().plus(it4.getContext());
                                AnonymousClass2 anonymousClass23 = new AnonymousClass2(it4, e, null);
                                this.L$0 = $this$launch;
                                this.L$1 = null;
                                this.label = 11;
                                if (BuildersKt.withContext(coroutineContextPlus6, anonymousClass23, this) == coroutine_suspended) {
                                    return coroutine_suspended;
                                }
                            }
                        }
                        Unit unit6 = Unit.INSTANCE;
                    }
                    if (CoroutineScopeKt.isActive($this$launch) && (it3 = ((Coroutine) this.this$0).finally) != null) {
                        Coroutine<T> coroutine8 = this.this$0;
                        if (it3.getContext() != null) {
                            this.L$0 = null;
                            this.L$1 = null;
                            this.label = 12;
                            if (it3.getBlock().invoke($this$launch, this) == coroutine_suspended) {
                                return coroutine_suspended;
                            }
                        } else {
                            CoroutineContext coroutineContextPlus7 = $this$launch.getCoroutineContext().plus(it3.getContext());
                            C01482 c014824 = new C01482(it3, null);
                            this.L$0 = null;
                            this.L$1 = null;
                            this.label = 13;
                            if (BuildersKt.withContext(coroutineContextPlus7, c014824, this) == coroutine_suspended) {
                                return coroutine_suspended;
                            }
                        }
                        Unit unit7 = Unit.INSTANCE;
                    }
                    return Unit.INSTANCE;
                case Node.COMMENT /* 9 */:
                    e = (Throwable) this.L$1;
                    $this$launch = (CoroutineScope) this.L$0;
                    ResultKt.throwOnFailure($result);
                    Unit unit52 = Unit.INSTANCE;
                    Boolean boolBoxBoolean22 = Boxing.boxBoolean(true);
                    if (boolBoxBoolean22 == null) {
                    }
                    consume = zBooleanValue;
                    if (!consume) {
                        Coroutine<T> coroutine72 = this.this$0;
                        if (CoroutineScopeKt.isActive($this$launch)) {
                        }
                        Unit unit62 = Unit.INSTANCE;
                    }
                    if (CoroutineScopeKt.isActive($this$launch)) {
                        Coroutine<T> coroutine82 = this.this$0;
                        if (it3.getContext() != null) {
                        }
                        Unit unit72 = Unit.INSTANCE;
                    }
                    return Unit.INSTANCE;
                case Node.DOCDECL /* 10 */:
                    $this$launch = (CoroutineScope) this.L$0;
                    ResultKt.throwOnFailure($result);
                    Unit unit622 = Unit.INSTANCE;
                    if (CoroutineScopeKt.isActive($this$launch)) {
                    }
                    return Unit.INSTANCE;
                case 11:
                    $this$launch = (CoroutineScope) this.L$0;
                    ResultKt.throwOnFailure($result);
                    Unit unit6222 = Unit.INSTANCE;
                    if (CoroutineScopeKt.isActive($this$launch)) {
                    }
                    return Unit.INSTANCE;
                case 12:
                    ResultKt.throwOnFailure($result);
                    Unit unit722 = Unit.INSTANCE;
                    return Unit.INSTANCE;
                case 13:
                    ResultKt.throwOnFailure($result);
                    Unit unit7222 = Unit.INSTANCE;
                    return Unit.INSTANCE;
                case 14:
                    th = (Throwable) this.L$0;
                    ResultKt.throwOnFailure($result);
                    Unit unit8 = Unit.INSTANCE;
                    throw th;
                case 15:
                    th = (Throwable) this.L$0;
                    ResultKt.throwOnFailure($result);
                    Unit unit82 = Unit.INSTANCE;
                    throw th;
                default:
                    throw new IllegalStateException("call to 'resume' before 'invoke' with coroutine");
            }
        }
    }

    private final Job executeInternal(CoroutineContext context, Function2<? super CoroutineScope, ? super Continuation<? super T>, ? extends Object> block) {
        return BuildersKt.launch$default(CoroutineScopeKt.plus(this.scope, Dispatchers.getIO()), (CoroutineContext) null, (CoroutineStart) null, new AnonymousClass1(this, context, block, null), 3, (Object) null);
    }

    private final Object dispatchVoidCallback(CoroutineScope scope, Coroutine<T>.VoidCallback callback, Continuation<? super Unit> $completion) {
        if (callback.getContext() == null) {
            Function2<CoroutineScope, Continuation<? super Unit>, Object> block = callback.getBlock();
            InlineMarker.mark(0);
            block.invoke(scope, $completion);
            InlineMarker.mark(1);
            return Unit.INSTANCE;
        }
        CoroutineContext coroutineContextPlus = scope.getCoroutineContext().plus(callback.getContext());
        C01482 c01482 = new C01482(callback, null);
        InlineMarker.mark(0);
        BuildersKt.withContext(coroutineContextPlus, c01482, $completion);
        InlineMarker.mark(1);
        return Unit.INSTANCE;
    }

    /* JADX INFO: renamed from: io.legado.app.help.coroutine.Coroutine$dispatchVoidCallback$2, reason: invalid class name and case insensitive filesystem */
    /* JADX INFO: compiled from: Coroutine.kt */
    /* JADX INFO: loaded from: reader-pro-classes-3.2.14.jar:io/legado/app/help/coroutine/Coroutine$dispatchVoidCallback$2.class */
    @Metadata(mv = {1, 5, 1}, k = 3, xi = 48, d1 = {"\u0000\f\n\u0000\n\u0002\u0010\u0002\n\u0000\n\u0002\u0018\u0002\u0010\u0000\u001a\u00020\u0001\"\u0004\b\u0000\u0010\u0002*\u00020\u0003H\u008a@"}, d2 = {"<anonymous>", PackageDocumentBase.PREFIX_OPF, "T", "Lkotlinx/coroutines/CoroutineScope;"})
    @DebugMetadata(f = "Coroutine.kt", l = {167}, i = {}, s = {}, n = {}, m = "invokeSuspend", c = "io.legado.app.help.coroutine.Coroutine$dispatchVoidCallback$2")
    public static final class C01482 extends SuspendLambda implements Function2<CoroutineScope, Continuation<? super Unit>, Object> {
        int label;
        private /* synthetic */ Object L$0;
        final /* synthetic */ Coroutine<T>.VoidCallback $callback;

        /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
        public C01482(Coroutine<T>.VoidCallback $callback, Continuation<? super C01482> $completion) {
            super(2, $completion);
            this.$callback = $callback;
        }

        @NotNull
        public final Continuation<Unit> create(@Nullable Object value, @NotNull Continuation<?> $completion) {
            Continuation<Unit> c01482 = new C01482(this.$callback, $completion);
            c01482.L$0 = value;
            return c01482;
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
                    Function2<CoroutineScope, Continuation<? super Unit>, Object> block = this.$callback.getBlock();
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

    private final <R> Object dispatchCallback(CoroutineScope scope, R value, Coroutine<T>.Callback<R> callback, Continuation<? super Unit> $completion) {
        if (!CoroutineScopeKt.isActive(scope)) {
            return Unit.INSTANCE;
        }
        if (callback.getContext() == null) {
            Function3<CoroutineScope, R, Continuation<? super Unit>, Object> block = callback.getBlock();
            InlineMarker.mark(0);
            block.invoke(scope, value, $completion);
            InlineMarker.mark(1);
            return Unit.INSTANCE;
        }
        CoroutineContext coroutineContextPlus = scope.getCoroutineContext().plus(callback.getContext());
        AnonymousClass2 anonymousClass2 = new AnonymousClass2(callback, value, null);
        InlineMarker.mark(0);
        BuildersKt.withContext(coroutineContextPlus, anonymousClass2, $completion);
        InlineMarker.mark(1);
        return Unit.INSTANCE;
    }

    /* JADX INFO: renamed from: io.legado.app.help.coroutine.Coroutine$dispatchCallback$2, reason: invalid class name */
    /* JADX INFO: compiled from: Coroutine.kt */
    /* JADX INFO: loaded from: reader-pro-classes-3.2.14.jar:io/legado/app/help/coroutine/Coroutine$dispatchCallback$2.class */
    @Metadata(mv = {1, 5, 1}, k = 3, xi = 48, d1 = {"\u0000\u000e\n\u0000\n\u0002\u0010\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\u0010\u0000\u001a\u00020\u0001\"\u0004\b\u0000\u0010\u0002\"\u0004\b\u0001\u0010\u0003*\u00020\u0004H\u008a@"}, d2 = {"<anonymous>", PackageDocumentBase.PREFIX_OPF, "R", "T", "Lkotlinx/coroutines/CoroutineScope;"})
    @DebugMetadata(f = "Coroutine.kt", l = {182}, i = {}, s = {}, n = {}, m = "invokeSuspend", c = "io.legado.app.help.coroutine.Coroutine$dispatchCallback$2")
    public static final class AnonymousClass2 extends SuspendLambda implements Function2<CoroutineScope, Continuation<? super Unit>, Object> {
        int label;
        private /* synthetic */ Object L$0;
        final /* synthetic */ Coroutine<T>.Callback<R> $callback;
        final /* synthetic */ R $value;

        /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
        public AnonymousClass2(Coroutine<T>.Callback<R> $callback, R $value, Continuation<? super AnonymousClass2> $completion) {
            super(2, $completion);
            this.$callback = $callback;
            this.$value = $value;
        }

        @NotNull
        public final Continuation<Unit> create(@Nullable Object value, @NotNull Continuation<?> $completion) {
            Continuation<Unit> anonymousClass2 = new AnonymousClass2(this.$callback, this.$value, $completion);
            anonymousClass2.L$0 = value;
            return anonymousClass2;
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
                    Function3 block = this.$callback.getBlock();
                    R r = this.$value;
                    this.label = 1;
                    if (block.invoke($this$withContext, r, this) == coroutine_suspended) {
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

    /* JADX INFO: renamed from: io.legado.app.help.coroutine.Coroutine$executeBlock$2, reason: invalid class name and case insensitive filesystem */
    /* JADX INFO: compiled from: Coroutine.kt */
    /* JADX INFO: loaded from: reader-pro-classes-3.2.14.jar:io/legado/app/help/coroutine/Coroutine$executeBlock$2.class */
    @Metadata(mv = {1, 5, 1}, k = 3, xi = 48, d1 = {"\u0000\b\n\u0002\b\u0002\n\u0002\u0018\u0002\u0010\u0000\u001a\u0002H\u0001\"\u0004\b\u0000\u0010\u0001*\u00020\u0002H\u008a@"}, d2 = {"<anonymous>", "T", "Lkotlinx/coroutines/CoroutineScope;"})
    @DebugMetadata(f = "Coroutine.kt", l = {Wbxml.EXT_2, 197}, i = {}, s = {}, n = {}, m = "invokeSuspend", c = "io.legado.app.help.coroutine.Coroutine$executeBlock$2")
    public static final class C01492 extends SuspendLambda implements Function2<CoroutineScope, Continuation<? super T>, Object> {
        int label;
        private /* synthetic */ Object L$0;
        final /* synthetic */ long $timeMillis;
        final /* synthetic */ Function2<CoroutineScope, Continuation<? super T>, Object> $block;

        /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
        public C01492(long $timeMillis, Function2<? super CoroutineScope, ? super Continuation<? super T>, ? extends Object> $block, Continuation<? super C01492> $completion) {
            super(2, $completion);
            this.$timeMillis = $timeMillis;
            this.$block = $block;
        }

        @NotNull
        public final Continuation<Unit> create(@Nullable Object value, @NotNull Continuation<?> $completion) {
            Continuation<Unit> c01492 = new C01492(this.$timeMillis, this.$block, $completion);
            c01492.L$0 = value;
            return c01492;
        }

        @Nullable
        public final Object invoke(@NotNull CoroutineScope p1, @Nullable Continuation<? super T> p2) {
            return create(p1, p2).invokeSuspend(Unit.INSTANCE);
        }

        /* JADX INFO: renamed from: io.legado.app.help.coroutine.Coroutine$executeBlock$2$1, reason: invalid class name */
        /* JADX INFO: compiled from: Coroutine.kt */
        /* JADX INFO: loaded from: reader-pro-classes-3.2.14.jar:io/legado/app/help/coroutine/Coroutine$executeBlock$2$1.class */
        @Metadata(mv = {1, 5, 1}, k = 3, xi = 48, d1 = {"\u0000\b\n\u0002\b\u0002\n\u0002\u0018\u0002\u0010\u0000\u001a\u0002H\u0001\"\u0004\b\u0000\u0010\u0001*\u00020\u0002H\u008a@"}, d2 = {"<anonymous>", "T", "Lkotlinx/coroutines/CoroutineScope;"})
        @DebugMetadata(f = "Coroutine.kt", l = {Wbxml.OPAQUE}, i = {}, s = {}, n = {}, m = "invokeSuspend", c = "io.legado.app.help.coroutine.Coroutine$executeBlock$2$1")
        public static final class AnonymousClass1 extends SuspendLambda implements Function2<CoroutineScope, Continuation<? super T>, Object> {
            int label;
            private /* synthetic */ Object L$0;
            final /* synthetic */ Function2<CoroutineScope, Continuation<? super T>, Object> $block;

            /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
            public AnonymousClass1(Function2<? super CoroutineScope, ? super Continuation<? super T>, ? extends Object> $block, Continuation<? super AnonymousClass1> $completion) {
                super(2, $completion);
                this.$block = $block;
            }

            @NotNull
            public final Continuation<Unit> create(@Nullable Object value, @NotNull Continuation<?> $completion) {
                Continuation<Unit> anonymousClass1 = new AnonymousClass1(this.$block, $completion);
                anonymousClass1.L$0 = value;
                return anonymousClass1;
            }

            @Nullable
            public final Object invoke(@NotNull CoroutineScope p1, @Nullable Continuation<? super T> p2) {
                return create(p1, p2).invokeSuspend(Unit.INSTANCE);
            }

            @Nullable
            public final Object invokeSuspend(@NotNull Object $result) {
                Object coroutine_suspended = IntrinsicsKt.getCOROUTINE_SUSPENDED();
                switch (this.label) {
                    case 0:
                        ResultKt.throwOnFailure($result);
                        CoroutineScope $this$withTimeout = (CoroutineScope) this.L$0;
                        Function2<CoroutineScope, Continuation<? super T>, Object> function2 = this.$block;
                        this.label = 1;
                        Object objInvoke = function2.invoke($this$withTimeout, this);
                        if (objInvoke == coroutine_suspended) {
                            return coroutine_suspended;
                        }
                        return objInvoke;
                    case 1:
                        ResultKt.throwOnFailure($result);
                        return $result;
                    default:
                        throw new IllegalStateException("call to 'resume' before 'invoke' with coroutine");
                }
            }
        }

        @Nullable
        public final Object invokeSuspend(@NotNull Object $result) {
            Object coroutine_suspended = IntrinsicsKt.getCOROUTINE_SUSPENDED();
            switch (this.label) {
                case 0:
                    ResultKt.throwOnFailure($result);
                    CoroutineScope $this$withContext = (CoroutineScope) this.L$0;
                    if (this.$timeMillis > 0) {
                        this.label = 1;
                        Object objWithTimeout = TimeoutKt.withTimeout(this.$timeMillis, new AnonymousClass1(this.$block, null), (Continuation) this);
                        if (objWithTimeout == coroutine_suspended) {
                            return coroutine_suspended;
                        }
                        return objWithTimeout;
                    }
                    Function2<CoroutineScope, Continuation<? super T>, Object> function2 = this.$block;
                    this.label = 2;
                    Object objInvoke = function2.invoke($this$withContext, this);
                    if (objInvoke == coroutine_suspended) {
                        return coroutine_suspended;
                    }
                    return objInvoke;
                case 1:
                    ResultKt.throwOnFailure($result);
                    return $result;
                case 2:
                    ResultKt.throwOnFailure($result);
                    return $result;
                default:
                    throw new IllegalStateException("call to 'resume' before 'invoke' with coroutine");
            }
        }
    }

    private final Object executeBlock(CoroutineScope scope, CoroutineContext context, long timeMillis, Function2<? super CoroutineScope, ? super Continuation<? super T>, ? extends Object> block, Continuation<? super T> $completion) {
        CoroutineContext coroutineContextPlus = scope.getCoroutineContext().plus(context);
        C01492 c01492 = new C01492(timeMillis, block, null);
        InlineMarker.mark(0);
        Object objWithContext = BuildersKt.withContext(coroutineContextPlus, c01492, $completion);
        InlineMarker.mark(1);
        return objWithContext;
    }

    /* JADX INFO: compiled from: Coroutine.kt */
    /* JADX INFO: loaded from: reader-pro-classes-3.2.14.jar:io/legado/app/help/coroutine/Coroutine$Result.class */
    @Metadata(mv = {1, 5, 1}, k = 1, xi = 48, d1 = {"\u0000\"\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u0000\n\u0002\b\t\n\u0002\u0010\u000b\n\u0002\b\u0002\n\u0002\u0010\b\n\u0000\n\u0002\u0010\u000e\n\u0000\b\u0082\b\u0018\u0000*\u0006\b\u0001\u0010\u0001 \u00012\u00020\u0002B\u000f\u0012\b\u0010\u0003\u001a\u0004\u0018\u00018\u0001¢\u0006\u0002\u0010\u0004J\u0010\u0010\b\u001a\u0004\u0018\u00018\u0001HÆ\u0003¢\u0006\u0002\u0010\u0006J \u0010\t\u001a\b\u0012\u0004\u0012\u00028\u00010\u00002\n\b\u0002\u0010\u0003\u001a\u0004\u0018\u00018\u0001HÆ\u0001¢\u0006\u0002\u0010\nJ\u0013\u0010\u000b\u001a\u00020\f2\b\u0010\r\u001a\u0004\u0018\u00010\u0002HÖ\u0003J\t\u0010\u000e\u001a\u00020\u000fHÖ\u0001J\t\u0010\u0010\u001a\u00020\u0011HÖ\u0001R\u0015\u0010\u0003\u001a\u0004\u0018\u00018\u0001¢\u0006\n\n\u0002\u0010\u0007\u001a\u0004\b\u0005\u0010\u0006¨\u0006\u0012"}, d2 = {"Lio/legado/app/help/coroutine/Coroutine$Result;", "T", PackageDocumentBase.PREFIX_OPF, "value", "(Ljava/lang/Object;)V", "getValue", "()Ljava/lang/Object;", "Ljava/lang/Object;", "component1", "copy", "(Ljava/lang/Object;)Lio/legado/app/help/coroutine/Coroutine$Result;", "equals", PackageDocumentBase.PREFIX_OPF, "other", "hashCode", PackageDocumentBase.PREFIX_OPF, "toString", PackageDocumentBase.PREFIX_OPF, "reader-pro"})
    private static final /* data */ class Result<T> {

        @Nullable
        private final T value;

        @Nullable
        public final T component1() {
            return this.value;
        }

        @NotNull
        public final Result<T> copy(@Nullable T value) {
            return new Result<>(value);
        }

        /* JADX WARN: Multi-variable type inference failed */
        public static /* synthetic */ Result copy$default(Result result, Object obj, int i, Object obj2) {
            if ((i & 1) != 0) {
                obj = result.value;
            }
            return result.copy(obj);
        }

        @NotNull
        public String toString() {
            return "Result(value=" + this.value + ')';
        }

        public int hashCode() {
            if (this.value == null) {
                return 0;
            }
            return this.value.hashCode();
        }

        public boolean equals(@Nullable Object other) {
            if (this == other) {
                return true;
            }
            return (other instanceof Result) && Intrinsics.areEqual(this.value, ((Result) other).value);
        }

        public Result(@Nullable T value) {
            this.value = value;
        }

        @Nullable
        public final T getValue() {
            return this.value;
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* JADX INFO: compiled from: Coroutine.kt */
    /* JADX INFO: loaded from: reader-pro-classes-3.2.14.jar:io/legado/app/help/coroutine/Coroutine$VoidCallback.class */
    @Metadata(mv = {1, 5, 1}, k = 1, xi = 48, d1 = {"\u0000(\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0010\u0002\n\u0002\u0018\u0002\n\u0002\b\u0007\b\u0082\u0004\u0018\u00002\u00020\u0001B;\u0012\b\u0010\u0002\u001a\u0004\u0018\u00010\u0003\u0012'\u0010\u0004\u001a#\b\u0001\u0012\u0004\u0012\u00020\u0006\u0012\n\u0012\b\u0012\u0004\u0012\u00020\b0\u0007\u0012\u0006\u0012\u0004\u0018\u00010\u00010\u0005¢\u0006\u0002\b\tø\u0001\u0000¢\u0006\u0002\u0010\nR7\u0010\u0004\u001a#\b\u0001\u0012\u0004\u0012\u00020\u0006\u0012\n\u0012\b\u0012\u0004\u0012\u00020\b0\u0007\u0012\u0006\u0012\u0004\u0018\u00010\u00010\u0005¢\u0006\u0002\b\tø\u0001\u0000¢\u0006\n\n\u0002\u0010\r\u001a\u0004\b\u000b\u0010\fR\u0013\u0010\u0002\u001a\u0004\u0018\u00010\u0003¢\u0006\b\n\u0000\u001a\u0004\b\u000e\u0010\u000f\u0082\u0002\u0004\n\u0002\b\u0019¨\u0006\u0010"}, d2 = {"Lio/legado/app/help/coroutine/Coroutine$VoidCallback;", PackageDocumentBase.PREFIX_OPF, "context", "Lkotlin/coroutines/CoroutineContext;", "block", "Lkotlin/Function2;", "Lkotlinx/coroutines/CoroutineScope;", "Lkotlin/coroutines/Continuation;", PackageDocumentBase.PREFIX_OPF, "Lkotlin/ExtensionFunctionType;", "(Lio/legado/app/help/coroutine/Coroutine;Lkotlin/coroutines/CoroutineContext;Lkotlin/jvm/functions/Function2;)V", "getBlock", "()Lkotlin/jvm/functions/Function2;", "Lkotlin/jvm/functions/Function2;", "getContext", "()Lkotlin/coroutines/CoroutineContext;", "reader-pro"})
    final class VoidCallback {

        @Nullable
        private final CoroutineContext context;

        @NotNull
        private final Function2<CoroutineScope, Continuation<? super Unit>, Object> block;
        final /* synthetic */ Coroutine<T> this$0;

        public VoidCallback(@Nullable Coroutine this$0, @NotNull CoroutineContext context, Function2<? super CoroutineScope, ? super Continuation<? super Unit>, ? extends Object> block) {
            Intrinsics.checkNotNullParameter(this$0, "this$0");
            Intrinsics.checkNotNullParameter(block, "block");
            this.this$0 = this$0;
            this.context = context;
            this.block = block;
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

    /* JADX INFO: compiled from: Coroutine.kt */
    /* JADX INFO: loaded from: reader-pro-classes-3.2.14.jar:io/legado/app/help/coroutine/Coroutine$Callback.class */
    @Metadata(mv = {1, 5, 1}, k = 1, xi = 48, d1 = {"\u0000*\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u0000\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0010\u0002\n\u0002\u0018\u0002\n\u0002\b\u0007\b\u0082\u0004\u0018\u0000*\u0004\b\u0001\u0010\u00012\u00020\u0002BA\u0012\b\u0010\u0003\u001a\u0004\u0018\u00010\u0004\u0012-\u0010\u0005\u001a)\b\u0001\u0012\u0004\u0012\u00020\u0007\u0012\u0004\u0012\u00028\u0001\u0012\n\u0012\b\u0012\u0004\u0012\u00020\t0\b\u0012\u0006\u0012\u0004\u0018\u00010\u00020\u0006¢\u0006\u0002\b\nø\u0001\u0000¢\u0006\u0002\u0010\u000bR=\u0010\u0005\u001a)\b\u0001\u0012\u0004\u0012\u00020\u0007\u0012\u0004\u0012\u00028\u0001\u0012\n\u0012\b\u0012\u0004\u0012\u00020\t0\b\u0012\u0006\u0012\u0004\u0018\u00010\u00020\u0006¢\u0006\u0002\b\nø\u0001\u0000¢\u0006\n\n\u0002\u0010\u000e\u001a\u0004\b\f\u0010\rR\u0013\u0010\u0003\u001a\u0004\u0018\u00010\u0004¢\u0006\b\n\u0000\u001a\u0004\b\u000f\u0010\u0010\u0082\u0002\u0004\n\u0002\b\u0019¨\u0006\u0011"}, d2 = {"Lio/legado/app/help/coroutine/Coroutine$Callback;", "VALUE", PackageDocumentBase.PREFIX_OPF, "context", "Lkotlin/coroutines/CoroutineContext;", "block", "Lkotlin/Function3;", "Lkotlinx/coroutines/CoroutineScope;", "Lkotlin/coroutines/Continuation;", PackageDocumentBase.PREFIX_OPF, "Lkotlin/ExtensionFunctionType;", "(Lio/legado/app/help/coroutine/Coroutine;Lkotlin/coroutines/CoroutineContext;Lkotlin/jvm/functions/Function3;)V", "getBlock", "()Lkotlin/jvm/functions/Function3;", "Lkotlin/jvm/functions/Function3;", "getContext", "()Lkotlin/coroutines/CoroutineContext;", "reader-pro"})
    private final class Callback<VALUE> {

        @Nullable
        private final CoroutineContext context;

        @NotNull
        private final Function3<CoroutineScope, VALUE, Continuation<? super Unit>, Object> block;
        final /* synthetic */ Coroutine<T> this$0;

        public Callback(@Nullable Coroutine this$0, @NotNull CoroutineContext context, Function3<? super CoroutineScope, ? super VALUE, ? super Continuation<? super Unit>, ? extends Object> block) {
            Intrinsics.checkNotNullParameter(this$0, "this$0");
            Intrinsics.checkNotNullParameter(block, "block");
            this.this$0 = this$0;
            this.context = context;
            this.block = block;
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
