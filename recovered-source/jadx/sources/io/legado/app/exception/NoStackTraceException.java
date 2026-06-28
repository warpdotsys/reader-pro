package io.legado.app.exception;

import kotlin.Metadata;
import kotlin.jvm.internal.Intrinsics;
import me.ag2s.epublib.epub.PackageDocumentBase;
import org.jetbrains.annotations.NotNull;

/* JADX INFO: compiled from: NoStackTraceException.kt */
/* JADX INFO: loaded from: app-classes.jar:io/legado/app/exception/NoStackTraceException.class */
@Metadata(mv = {1, 5, 1}, k = 1, xi = 48, d1 = {"\u0000\u001c\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u000e\n\u0002\b\u0002\n\u0002\u0010\u0003\n\u0000\b\u0016\u0018\u00002\u00060\u0001j\u0002`\u0002B\r\u0012\u0006\u0010\u0003\u001a\u00020\u0004¢\u0006\u0002\u0010\u0005J\b\u0010\u0006\u001a\u00020\u0007H\u0016¨\u0006\b"}, d2 = {"Lio/legado/app/exception/NoStackTraceException;", "Ljava/lang/Exception;", "Lkotlin/Exception;", "msg", PackageDocumentBase.PREFIX_OPF, "(Ljava/lang/String;)V", "fillInStackTrace", PackageDocumentBase.PREFIX_OPF, "reader-pro"})
public class NoStackTraceException extends Exception {
    /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
    public NoStackTraceException(@NotNull String msg) {
        super(msg);
        Intrinsics.checkNotNullParameter(msg, "msg");
    }

    @Override // java.lang.Throwable
    @NotNull
    public Throwable fillInStackTrace() {
        return this;
    }
}
