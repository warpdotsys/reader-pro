// 
// Decompiled by Procyon v0.6.0
// 

package com.htmake.reader.entity;

import org.jetbrains.annotations.Nullable;
import kotlin.jvm.internal.Intrinsics;
import org.jetbrains.annotations.NotNull;
import kotlin.Metadata;

@Metadata(mv = { 1, 5, 1 }, k = 1, xi = 48, d1 = { "\u0000(\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0000\n\u0002\u0010\u000e\n\u0002\b\u0004\n\u0002\u0010\b\n\u0000\n\u0002\u0010\t\n\u0002\b\u0012\n\u0002\u0010\u000b\n\u0002\b\u0004\b\u0086\b\u0018\u00002\u00020\u0001B5\u0012\u0006\u0010\u0002\u001a\u00020\u0003\u0012\u0006\u0010\u0004\u001a\u00020\u0003\u0012\u0006\u0010\u0005\u001a\u00020\u0003\u0012\u0006\u0010\u0006\u001a\u00020\u0003\u0012\u0006\u0010\u0007\u001a\u00020\b\u0012\u0006\u0010\t\u001a\u00020\n?\u0006\u0002\u0010\u000bJ\t\u0010\u0015\u001a\u00020\u0003H\u00c6\u0003J\t\u0010\u0016\u001a\u00020\u0003H\u00c6\u0003J\t\u0010\u0017\u001a\u00020\u0003H\u00c6\u0003J\t\u0010\u0018\u001a\u00020\u0003H\u00c6\u0003J\t\u0010\u0019\u001a\u00020\bH\u00c6\u0003J\t\u0010\u001a\u001a\u00020\nH\u00c6\u0003JE\u0010\u001b\u001a\u00020\u00002\b\b\u0002\u0010\u0002\u001a\u00020\u00032\b\b\u0002\u0010\u0004\u001a\u00020\u00032\b\b\u0002\u0010\u0005\u001a\u00020\u00032\b\b\u0002\u0010\u0006\u001a\u00020\u00032\b\b\u0002\u0010\u0007\u001a\u00020\b2\b\b\u0002\u0010\t\u001a\u00020\nH\u00c6\u0001J\u0013\u0010\u001c\u001a\u00020\u001d2\b\u0010\u001e\u001a\u0004\u0018\u00010\u0001H\u00d6\u0003J\t\u0010\u001f\u001a\u00020\bH\u00d6\u0001J\t\u0010 \u001a\u00020\u0003H\u00d6\u0001R\u0011\u0010\u0002\u001a\u00020\u0003?\u0006\b\n\u0000\u001a\u0004\b\f\u0010\rR\u0011\u0010\u0004\u001a\u00020\u0003?\u0006\b\n\u0000\u001a\u0004\b\u000e\u0010\rR\u0011\u0010\u0005\u001a\u00020\u0003?\u0006\b\n\u0000\u001a\u0004\b\u000f\u0010\rR\u0011\u0010\u0006\u001a\u00020\u0003?\u0006\b\n\u0000\u001a\u0004\b\u0010\u0010\rR\u0011\u0010\u0007\u001a\u00020\b?\u0006\b\n\u0000\u001a\u0004\b\u0011\u0010\u0012R\u0011\u0010\t\u001a\u00020\n?\u0006\b\n\u0000\u001a\u0004\b\u0013\u0010\u0014¡§\u0006!" }, d2 = { "Lcom/htmake/reader/entity/BasicError;", "", "error", "", "exception", "message", "path", "status", "", "timestamp", "", "(Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;IJ)V", "getError", "()Ljava/lang/String;", "getException", "getMessage", "getPath", "getStatus", "()I", "getTimestamp", "()J", "component1", "component2", "component3", "component4", "component5", "component6", "copy", "equals", "", "other", "hashCode", "toString", "reader-pro" })
public final class BasicError
{
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
    
    public BasicError(@NotNull final String error, @NotNull final String exception, @NotNull final String message, @NotNull final String path, final int status, final long timestamp) {
        Intrinsics.checkNotNullParameter((Object)error, "error");
        Intrinsics.checkNotNullParameter((Object)exception, "exception");
        Intrinsics.checkNotNullParameter((Object)message, "message");
        Intrinsics.checkNotNullParameter((Object)path, "path");
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
    public final BasicError copy(@NotNull final String error, @NotNull final String exception, @NotNull final String message, @NotNull final String path, final int status, final long timestamp) {
        Intrinsics.checkNotNullParameter((Object)error, "error");
        Intrinsics.checkNotNullParameter((Object)exception, "exception");
        Intrinsics.checkNotNullParameter((Object)message, "message");
        Intrinsics.checkNotNullParameter((Object)path, "path");
        return new BasicError(error, exception, message, path, status, timestamp);
    }
    
    @NotNull
    @Override
    public String toString() {
        return "BasicError(error=" + this.error + ", exception=" + this.exception + ", message=" + this.message + ", path=" + this.path + ", status=" + this.status + ", timestamp=" + this.timestamp + ')';
    }
    
    @Override
    public int hashCode() {
        int result = this.error.hashCode();
        result = result * 31 + this.exception.hashCode();
        result = result * 31 + this.message.hashCode();
        result = result * 31 + this.path.hashCode();
        result = result * 31 + Integer.hashCode(this.status);
        result = result * 31 + Long.hashCode(this.timestamp);
        return result;
    }
    
    @Override
    public boolean equals(@Nullable final Object other) {
        if (this == other) {
            return true;
        }
        if (!(other instanceof BasicError)) {
            return false;
        }
        final BasicError basicError = (BasicError)other;
        return Intrinsics.areEqual((Object)this.error, (Object)basicError.error) && Intrinsics.areEqual((Object)this.exception, (Object)basicError.exception) && Intrinsics.areEqual((Object)this.message, (Object)basicError.message) && Intrinsics.areEqual((Object)this.path, (Object)basicError.path) && this.status == basicError.status && this.timestamp == basicError.timestamp;
    }
}
