package io.legado.app.exception;

import kotlin.Metadata;
import kotlin.jvm.internal.Intrinsics;
import me.ag2s.epublib.epub.PackageDocumentBase;
import org.jetbrains.annotations.NotNull;

/* JADX INFO: compiled from: ConcurrentException.kt */
/* JADX INFO: loaded from: reader-pro-classes-3.2.14.jar:io/legado/app/exception/ConcurrentException.class */
@Metadata(mv = {1, 5, 1}, k = 1, xi = 48, d1 = {"\u0000\u0018\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u000e\n\u0000\n\u0002\u0010\b\n\u0002\b\u0004\u0018\u00002\u00020\u0001B\u0015\u0012\u0006\u0010\u0002\u001a\u00020\u0003\u0012\u0006\u0010\u0004\u001a\u00020\u0005¢\u0006\u0002\u0010\u0006R\u0011\u0010\u0004\u001a\u00020\u0005¢\u0006\b\n\u0000\u001a\u0004\b\u0007\u0010\b¨\u0006\t"}, d2 = {"Lio/legado/app/exception/ConcurrentException;", "Lio/legado/app/exception/NoStackTraceException;", "msg", PackageDocumentBase.PREFIX_OPF, "waitTime", PackageDocumentBase.PREFIX_OPF, "(Ljava/lang/String;I)V", "getWaitTime", "()I", "reader-pro"})
public final class ConcurrentException extends NoStackTraceException {
    private final int waitTime;

    /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
    public ConcurrentException(@NotNull String msg, int waitTime) {
        super(msg);
        Intrinsics.checkNotNullParameter(msg, "msg");
        this.waitTime = waitTime;
    }

    public final int getWaitTime() {
        return this.waitTime;
    }
}
