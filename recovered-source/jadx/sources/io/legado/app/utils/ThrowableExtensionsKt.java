package io.legado.app.utils;

import kotlin.ExceptionsKt;
import kotlin.Metadata;
import kotlin.jvm.internal.Intrinsics;
import me.ag2s.epublib.epub.PackageDocumentBase;
import org.jetbrains.annotations.NotNull;

/* JADX INFO: compiled from: ThrowableExtensions.kt */
/* JADX INFO: loaded from: app-classes.jar:io/legado/app/utils/ThrowableExtensionsKt.class */
@Metadata(mv = {1, 5, 1}, k = 2, xi = 48, d1 = {"\u0000\u000e\n\u0000\n\u0002\u0010\u000e\n\u0002\u0010\u0003\n\u0002\b\u0003\"\u0015\u0010\u0000\u001a\u00020\u0001*\u00020\u00028F¢\u0006\u0006\u001a\u0004\b\u0003\u0010\u0004¨\u0006\u0005"}, d2 = {"msg", PackageDocumentBase.PREFIX_OPF, PackageDocumentBase.PREFIX_OPF, "getMsg", "(Ljava/lang/Throwable;)Ljava/lang/String;", "reader-pro"})
public final class ThrowableExtensionsKt {
    @NotNull
    public static final String getMsg(@NotNull Throwable $this$msg) {
        Intrinsics.checkNotNullParameter($this$msg, "<this>");
        String stackTrace = ExceptionsKt.stackTraceToString($this$msg);
        String localizedMessage = $this$msg.getLocalizedMessage();
        String lMsg = localizedMessage == null ? "noErrorMsg" : localizedMessage;
        return stackTrace.length() > 0 ? stackTrace : lMsg;
    }
}
