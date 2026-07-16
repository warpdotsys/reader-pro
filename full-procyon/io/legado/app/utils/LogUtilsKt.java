// 
// Decompiled by Procyon v0.6.0
// 

package io.legado.app.utils;

import kotlin.jvm.internal.Intrinsics;
import org.jetbrains.annotations.NotNull;
import kotlin.Metadata;

@Metadata(mv = { 1, 5, 1 }, k = 2, xi = 48, d1 = { "\u0000\f\n\u0000\n\u0002\u0010\u0002\n\u0002\u0010\u0003\n\u0000\u001a\n\u0010\u0000\u001a\u00020\u0001*\u00020\u0002¡§\u0006\u0003" }, d2 = { "printOnDebug", "", "", "reader-pro" })
public final class LogUtilsKt
{
    public static final void printOnDebug(@NotNull final Throwable $this$printOnDebug) {
        Intrinsics.checkNotNullParameter((Object)$this$printOnDebug, "<this>");
        $this$printOnDebug.printStackTrace();
    }
}
