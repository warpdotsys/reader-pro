// 
// Decompiled by Procyon v0.6.0
// 

package io.legado.app.utils;

import kotlin.ExceptionsKt;
import kotlin.jvm.internal.Intrinsics;
import org.jetbrains.annotations.NotNull;
import kotlin.Metadata;

@Metadata(mv = { 1, 5, 1 }, k = 2, xi = 48, d1 = { "\u0000\u000e\n\u0000\n\u0002\u0010\u000e\n\u0002\u0010\u0003\n\u0002\b\u0003\"\u0015\u0010\u0000\u001a\u00020\u0001*\u00020\u00028F?\u0006\u0006\u001a\u0004\b\u0003\u0010\u0004¡§\u0006\u0005" }, d2 = { "msg", "", "", "getMsg", "(Ljava/lang/Throwable;)Ljava/lang/String;", "reader-pro" })
public final class ThrowableExtensionsKt
{
    @NotNull
    public static final String getMsg(@NotNull final Throwable $this$msg) {
        Intrinsics.checkNotNullParameter((Object)$this$msg, "<this>");
        final String stackTrace = ExceptionsKt.stackTraceToString($this$msg);
        final String localizedMessage = $this$msg.getLocalizedMessage();
        final String lMsg = (localizedMessage == null) ? "noErrorMsg" : localizedMessage;
        return (stackTrace.length() > 0) ? stackTrace : lMsg;
    }
}
