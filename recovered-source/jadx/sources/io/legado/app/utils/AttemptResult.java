package io.legado.app.utils;

import kotlin.Metadata;
import kotlin.PublishedApi;
import kotlin.jvm.functions.Function1;
import kotlin.jvm.internal.Intrinsics;
import me.ag2s.epublib.epub.PackageDocumentBase;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/* JADX INFO: compiled from: AnkoHelps.kt */
/* JADX INFO: loaded from: app-classes.jar:io/legado/app/utils/AttemptResult.class */
@Metadata(mv = {1, 5, 1}, k = 1, xi = 48, d1 = {"\u00002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u0000\n\u0002\b\u0002\n\u0002\u0010\u0003\n\u0002\b\u0004\n\u0002\u0010\u000b\n\u0002\b\r\n\u0002\u0010\b\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u000e\n\u0000\b\u0086\b\u0018\u0000*\u0006\b\u0000\u0010\u0001 \u00012\u00020\u0002B\u001b\b\u0001\u0012\b\u0010\u0003\u001a\u0004\u0018\u00018\u0000\u0012\b\u0010\u0004\u001a\u0004\u0018\u00010\u0005¢\u0006\u0002\u0010\u0006J\u0010\u0010\u0011\u001a\u0004\u0018\u00018\u0000HÆ\u0003¢\u0006\u0002\u0010\u000fJ\u000b\u0010\u0012\u001a\u0004\u0018\u00010\u0005HÆ\u0003J,\u0010\u0013\u001a\b\u0012\u0004\u0012\u00028\u00000\u00002\n\b\u0002\u0010\u0003\u001a\u0004\u0018\u00018\u00002\n\b\u0002\u0010\u0004\u001a\u0004\u0018\u00010\u0005HÆ\u0001¢\u0006\u0002\u0010\u0014J\u0013\u0010\u0015\u001a\u00020\n2\b\u0010\u0016\u001a\u0004\u0018\u00010\u0002HÖ\u0003J\t\u0010\u0017\u001a\u00020\u0018HÖ\u0001J,\u0010\u0019\u001a\b\u0012\u0004\u0012\u0002H\u001a0\u0000\"\u0004\b\u0001\u0010\u001a2\u0012\u0010\u001b\u001a\u000e\u0012\u0004\u0012\u00028\u0000\u0012\u0004\u0012\u0002H\u001a0\u001cH\u0086\bø\u0001\u0000J\t\u0010\u001d\u001a\u00020\u001eHÖ\u0001R\u0013\u0010\u0004\u001a\u0004\u0018\u00010\u0005¢\u0006\b\n\u0000\u001a\u0004\b\u0007\u0010\bR\u0012\u0010\t\u001a\u00020\n8Æ\u0002¢\u0006\u0006\u001a\u0004\b\u000b\u0010\fR\u0012\u0010\r\u001a\u00020\n8Æ\u0002¢\u0006\u0006\u001a\u0004\b\r\u0010\fR\u0015\u0010\u0003\u001a\u0004\u0018\u00018\u0000¢\u0006\n\n\u0002\u0010\u0010\u001a\u0004\b\u000e\u0010\u000f\u0082\u0002\u0007\n\u0005\b\u009920\u0001¨\u0006\u001f"}, d2 = {"Lio/legado/app/utils/AttemptResult;", "T", PackageDocumentBase.PREFIX_OPF, "value", "error", PackageDocumentBase.PREFIX_OPF, "(Ljava/lang/Object;Ljava/lang/Throwable;)V", "getError", "()Ljava/lang/Throwable;", "hasValue", PackageDocumentBase.PREFIX_OPF, "getHasValue", "()Z", "isError", "getValue", "()Ljava/lang/Object;", "Ljava/lang/Object;", "component1", "component2", "copy", "(Ljava/lang/Object;Ljava/lang/Throwable;)Lio/legado/app/utils/AttemptResult;", "equals", "other", "hashCode", PackageDocumentBase.PREFIX_OPF, "then", "R", "f", "Lkotlin/Function1;", "toString", PackageDocumentBase.PREFIX_OPF, "reader-pro"})
public final /* data */ class AttemptResult<T> {

    @Nullable
    private final T value;

    @Nullable
    private final Throwable error;

    @Nullable
    public final T component1() {
        return this.value;
    }

    @Nullable
    public final Throwable component2() {
        return this.error;
    }

    @NotNull
    public final AttemptResult<T> copy(@Nullable T value, @Nullable Throwable error) {
        return new AttemptResult<>(value, error);
    }

    /* JADX WARN: Multi-variable type inference failed */
    public static /* synthetic */ AttemptResult copy$default(AttemptResult attemptResult, Object obj, Throwable th, int i, Object obj2) {
        if ((i & 1) != 0) {
            obj = attemptResult.value;
        }
        if ((i & 2) != 0) {
            th = attemptResult.error;
        }
        return attemptResult.copy(obj, th);
    }

    @NotNull
    public String toString() {
        return "AttemptResult(value=" + this.value + ", error=" + this.error + ')';
    }

    public int hashCode() {
        int result = this.value == null ? 0 : this.value.hashCode();
        return (result * 31) + (this.error == null ? 0 : this.error.hashCode());
    }

    public boolean equals(@Nullable Object other) {
        if (this == other) {
            return true;
        }
        if (!(other instanceof AttemptResult)) {
            return false;
        }
        AttemptResult attemptResult = (AttemptResult) other;
        return Intrinsics.areEqual(this.value, attemptResult.value) && Intrinsics.areEqual(this.error, attemptResult.error);
    }

    @PublishedApi
    public AttemptResult(@Nullable T value, @Nullable Throwable error) {
        this.value = value;
        this.error = error;
    }

    @Nullable
    public final T getValue() {
        return this.value;
    }

    @Nullable
    public final Throwable getError() {
        return this.error;
    }

    /* JADX WARN: Multi-variable type inference failed */
    @NotNull
    public final <R> AttemptResult<R> then(@NotNull Function1<? super T, ? extends R> f) {
        Intrinsics.checkNotNullParameter(f, "f");
        if (getError() != null) {
            return this;
        }
        Object value$iv = null;
        Throwable error$iv = null;
        try {
            value$iv = f.invoke(getValue());
        } catch (Throwable t$iv) {
            error$iv = t$iv;
        }
        return new AttemptResult<>(value$iv, error$iv);
    }

    public final boolean isError() {
        return getError() != null;
    }

    public final boolean getHasValue() {
        return getError() == null;
    }
}
