package io.legado.app.exception;

import kotlin.Metadata;
import kotlin.jvm.internal.Intrinsics;
import me.ag2s.epublib.epub.PackageDocumentBase;
import org.jetbrains.annotations.NotNull;

/* JADX INFO: compiled from: TocEmptyException.kt */
/* JADX INFO: loaded from: reader-pro-classes-3.2.14.jar:io/legado/app/exception/TocEmptyException.class */
@Metadata(mv = {1, 5, 1}, k = 1, xi = 48, d1 = {"\u0000\u0012\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u000e\n\u0002\b\u0002\u0018\u00002\u00020\u0001B\r\u0012\u0006\u0010\u0002\u001a\u00020\u0003¢\u0006\u0002\u0010\u0004¨\u0006\u0005"}, d2 = {"Lio/legado/app/exception/TocEmptyException;", "Lio/legado/app/exception/NoStackTraceException;", "msg", PackageDocumentBase.PREFIX_OPF, "(Ljava/lang/String;)V", "reader-pro"})
public final class TocEmptyException extends NoStackTraceException {
    /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
    public TocEmptyException(@NotNull String msg) {
        super(msg);
        Intrinsics.checkNotNullParameter(msg, "msg");
    }
}
