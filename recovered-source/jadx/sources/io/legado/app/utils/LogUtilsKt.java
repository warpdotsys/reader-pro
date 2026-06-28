package io.legado.app.utils;

import kotlin.Metadata;
import kotlin.jvm.internal.Intrinsics;
import me.ag2s.epublib.epub.PackageDocumentBase;
import org.jetbrains.annotations.NotNull;

/* JADX INFO: compiled from: LogUtils.kt */
/* JADX INFO: loaded from: reader-pro-classes-3.2.14.jar:io/legado/app/utils/LogUtilsKt.class */
@Metadata(mv = {1, 5, 1}, k = 2, xi = 48, d1 = {"\u0000\f\n\u0000\n\u0002\u0010\u0002\n\u0002\u0010\u0003\n\u0000\u001a\n\u0010\u0000\u001a\u00020\u0001*\u00020\u0002¨\u0006\u0003"}, d2 = {"printOnDebug", PackageDocumentBase.PREFIX_OPF, PackageDocumentBase.PREFIX_OPF, "reader-pro"})
public final class LogUtilsKt {
    public static final void printOnDebug(@NotNull Throwable $this$printOnDebug) {
        Intrinsics.checkNotNullParameter($this$printOnDebug, "<this>");
        $this$printOnDebug.printStackTrace();
    }
}
