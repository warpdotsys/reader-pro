package com.htmake.reader.entity;

import kotlin.Metadata;
import kotlin.jvm.internal.Intrinsics;
import me.ag2s.epublib.epub.PackageDocumentBase;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/* JADX INFO: compiled from: BasicError.kt */
/* JADX INFO: loaded from: app-classes.jar:com/htmake/reader/entity/BasicError.class */
@Metadata(mv = {1, 5, 1}, k = 1, xi = 48, d1 = {"\u0000(\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0000\n\u0002\u0010\u000e\n\u0002\b\u0004\n\u0002\u0010\b\n\u0000\n\u0002\u0010\t\n\u0002\b\u0012\n\u0002\u0010\u000b\n\u0002\b\u0004\b\u0086\b\u0018\u00002\u00020\u0001B5\u0012\u0006\u0010\u0002\u001a\u00020\u0003\u0012\u0006\u0010\u0004\u001a\u00020\u0003\u0012\u0006\u0010\u0005\u001a\u00020\u0003\u0012\u0006\u0010\u0006\u001a\u00020\u0003\u0012\u0006\u0010\u0007\u001a\u00020\b\u0012\u0006\u0010\t\u001a\u00020\n¢\u0006\u0002\u0010\u000bJ\t\u0010\u0015\u001a\u00020\u0003HÆ\u0003J\t\u0010\u0016\u001a\u00020\u0003HÆ\u0003J\t\u0010\u0017\u001a\u00020\u0003HÆ\u0003J\t\u0010\u0018\u001a\u00020\u0003HÆ\u0003J\t\u0010\u0019\u001a\u00020\bHÆ\u0003J\t\u0010\u001a\u001a\u00020\nHÆ\u0003JE\u0010\u001b\u001a\u00020\u00002\b\b\u0002\u0010\u0002\u001a\u00020\u00032\b\b\u0002\u0010\u0004\u001a\u00020\u00032\b\b\u0002\u0010\u0005\u001a\u00020\u00032\b\b\u0002\u0010\u0006\u001a\u00020\u00032\b\b\u0002\u0010\u0007\u001a\u00020\b2\b\b\u0002\u0010\t\u001a\u00020\nHÆ\u0001J\u0013\u0010\u001c\u001a\u00020\u001d2\b\u0010\u001e\u001a\u0004\u0018\u00010\u0001HÖ\u0003J\t\u0010\u001f\u001a\u00020\bHÖ\u0001J\t\u0010 \u001a\u00020\u0003HÖ\u0001R\u0011\u0010\u0002\u001a\u00020\u0003¢\u0006\b\n\u0000\u001a\u0004\b\f\u0010\rR\u0011\u0010\u0004\u001a\u00020\u0003¢\u0006\b\n\u0000\u001a\u0004\b\u000e\u0010\rR\u0011\u0010\u0005\u001a\u00020\u0003¢\u0006\b\n\u0000\u001a\u0004\b\u000f\u0010\rR\u0011\u0010\u0006\u001a\u00020\u0003¢\u0006\b\n\u0000\u001a\u0004\b\u0010\u0010\rR\u0011\u0010\u0007\u001a\u00020\b¢\u0006\b\n\u0000\u001a\u0004\b\u0011\u0010\u0012R\u0011\u0010\t\u001a\u00020\n¢\u0006\b\n\u0000\u001a\u0004\b\u0013\u0010\u0014¨\u0006!"}, d2 = {"Lcom/htmake/reader/entity/BasicError;", PackageDocumentBase.PREFIX_OPF, "error", PackageDocumentBase.PREFIX_OPF, "exception", "message", "path", "status", PackageDocumentBase.PREFIX_OPF, "timestamp", PackageDocumentBase.PREFIX_OPF, "(Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;IJ)V", "getError", "()Ljava/lang/String;", "getException", "getMessage", "getPath", "getStatus", "()I", "getTimestamp", "()J", "component1", "component2", "component3", "component4", "component5", "component6", "copy", "equals", PackageDocumentBase.PREFIX_OPF, "other", "hashCode", "toString", "reader-pro"})
public final /* data */ class BasicError {

    @NotNull
    private final String error;

    @NotNull
    private final String exception;

    @NotNull
    private final String message;

    @NotNull
    private final String path;
    private final int status;
    private final long timestamp;

    @NotNull
    public final String component1() {
        return this.error;
    }

    @NotNull
    public final String component2() {
        return this.exception;
    }

    @NotNull
    public final String component3() {
        return this.message;
    }

    @NotNull
    public final String component4() {
        return this.path;
    }

    public final int component5() {
        return this.status;
    }

    public final long component6() {
        return this.timestamp;
    }

    @NotNull
    public final BasicError copy(@NotNull String error, @NotNull String exception, @NotNull String message, @NotNull String path, int status, long timestamp) {
        Intrinsics.checkNotNullParameter(error, "error");
        Intrinsics.checkNotNullParameter(exception, "exception");
        Intrinsics.checkNotNullParameter(message, "message");
        Intrinsics.checkNotNullParameter(path, "path");
        return new BasicError(error, exception, message, path, status, timestamp);
    }

    public static /* synthetic */ BasicError copy$default(BasicError basicError, String str, String str2, String str3, String str4, int i, long j, int i2, Object obj) {
        if ((i2 & 1) != 0) {
            str = basicError.error;
        }
        if ((i2 & 2) != 0) {
            str2 = basicError.exception;
        }
        if ((i2 & 4) != 0) {
            str3 = basicError.message;
        }
        if ((i2 & 8) != 0) {
            str4 = basicError.path;
        }
        if ((i2 & 16) != 0) {
            i = basicError.status;
        }
        if ((i2 & 32) != 0) {
            j = basicError.timestamp;
        }
        return basicError.copy(str, str2, str3, str4, i, j);
    }

    @NotNull
    public String toString() {
        return "BasicError(error=" + this.error + ", exception=" + this.exception + ", message=" + this.message + ", path=" + this.path + ", status=" + this.status + ", timestamp=" + this.timestamp + ')';
    }

    public int hashCode() {
        int result = this.error.hashCode();
        return (((((((((result * 31) + this.exception.hashCode()) * 31) + this.message.hashCode()) * 31) + this.path.hashCode()) * 31) + Integer.hashCode(this.status)) * 31) + Long.hashCode(this.timestamp);
    }

    public boolean equals(@Nullable Object other) {
        if (this == other) {
            return true;
        }
        if (!(other instanceof BasicError)) {
            return false;
        }
        BasicError basicError = (BasicError) other;
        return Intrinsics.areEqual(this.error, basicError.error) && Intrinsics.areEqual(this.exception, basicError.exception) && Intrinsics.areEqual(this.message, basicError.message) && Intrinsics.areEqual(this.path, basicError.path) && this.status == basicError.status && this.timestamp == basicError.timestamp;
    }

    public BasicError(@NotNull String error, @NotNull String exception, @NotNull String message, @NotNull String path, int status, long timestamp) {
        Intrinsics.checkNotNullParameter(error, "error");
        Intrinsics.checkNotNullParameter(exception, "exception");
        Intrinsics.checkNotNullParameter(message, "message");
        Intrinsics.checkNotNullParameter(path, "path");
        this.error = error;
        this.exception = exception;
        this.message = message;
        this.path = path;
        this.status = status;
        this.timestamp = timestamp;
    }

    @NotNull
    public final String getError() {
        return this.error;
    }

    @NotNull
    public final String getException() {
        return this.exception;
    }

    @NotNull
    public final String getMessage() {
        return this.message;
    }

    @NotNull
    public final String getPath() {
        return this.path;
    }

    public final int getStatus() {
        return this.status;
    }

    public final long getTimestamp() {
        return this.timestamp;
    }
}
