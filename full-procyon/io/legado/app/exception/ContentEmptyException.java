// 
// Decompiled by Procyon v0.6.0
// 

package io.legado.app.exception;

import kotlin.jvm.internal.Intrinsics;
import org.jetbrains.annotations.NotNull;
import kotlin.Metadata;

@Metadata(mv = { 1, 5, 1 }, k = 1, xi = 48, d1 = { "\u0000\u0012\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u000e\n\u0002\b\u0002\u0018\u00002\u00020\u0001B\r\u0012\u0006\u0010\u0002\u001a\u00020\u0003?\u0006\u0002\u0010\u0004¡§\u0006\u0005" }, d2 = { "Lio/legado/app/exception/ContentEmptyException;", "Lio/legado/app/exception/NoStackTraceException;", "msg", "", "(Ljava/lang/String;)V", "reader-pro" })
public final class ContentEmptyException extends NoStackTraceException
{
    public ContentEmptyException(@NotNull final String msg) {
        Intrinsics.checkNotNullParameter((Object)msg, "msg");
        super(msg);
    }
}
