package io.legado.app.model;

import kotlin.Metadata;
import kotlin.jvm.internal.Intrinsics;
import me.ag2s.epublib.epub.PackageDocumentBase;
import okhttp3.logging.HttpLoggingInterceptor;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/* JADX INFO: compiled from: DebugLog.kt */
/* JADX INFO: loaded from: app-classes.jar:io/legado/app/model/DebugLog.class */
@Metadata(mv = {1, 5, 1}, k = 1, xi = 48, d1 = {"\u0000\u001e\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u0002\n\u0000\n\u0002\u0010\u000e\n\u0002\b\u0003\n\u0002\u0010\u000b\n\u0000\bf\u0018\u00002\u00020\u0001J\u0010\u0010\u0002\u001a\u00020\u00032\u0006\u0010\u0004\u001a\u00020\u0005H\u0016J*\u0010\u0002\u001a\u00020\u00032\n\b\u0002\u0010\u0006\u001a\u0004\u0018\u00010\u00052\n\b\u0002\u0010\u0007\u001a\u0004\u0018\u00010\u00052\b\b\u0002\u0010\b\u001a\u00020\tH\u0016¨\u0006\n"}, d2 = {"Lio/legado/app/model/DebugLog;", "Lokhttp3/logging/HttpLoggingInterceptor$Logger;", "log", PackageDocumentBase.PREFIX_OPF, "message", PackageDocumentBase.PREFIX_OPF, "sourceUrl", "msg", "isHtml", PackageDocumentBase.PREFIX_OPF, "reader-pro"})
public interface DebugLog extends HttpLoggingInterceptor.Logger {
    void log(@Nullable String sourceUrl, @Nullable String msg, boolean isHtml);

    void log(@NotNull String message);

    /* JADX INFO: compiled from: DebugLog.kt */
    /* JADX INFO: loaded from: app-classes.jar:io/legado/app/model/DebugLog$DefaultImpls.class */
    @Metadata(mv = {1, 5, 1}, k = 3, xi = 48)
    public static final class DefaultImpls {
        public static /* synthetic */ void log$default(DebugLog debugLog, String str, String str2, boolean z, int i, Object obj) {
            if (obj != null) {
                throw new UnsupportedOperationException("Super calls with default arguments not supported in this target, function: log");
            }
            if ((i & 1) != 0) {
                str = PackageDocumentBase.PREFIX_OPF;
            }
            if ((i & 2) != 0) {
                str2 = PackageDocumentBase.PREFIX_OPF;
            }
            if ((i & 4) != 0) {
                z = false;
            }
            debugLog.log(str, str2, z);
        }

        public static void log(@NotNull DebugLog debugLog, @Nullable String sourceUrl, @Nullable String msg, boolean isHtml) {
            Intrinsics.checkNotNullParameter(debugLog, "this");
            DebugLogKt.logger.info("sourceUrl: {}, msg: {}", sourceUrl, msg);
        }

        public static void log(@NotNull DebugLog debugLog, @NotNull String message) {
            Intrinsics.checkNotNullParameter(debugLog, "this");
            Intrinsics.checkNotNullParameter(message, "message");
            DebugLogKt.logger.debug(message);
        }
    }
}
