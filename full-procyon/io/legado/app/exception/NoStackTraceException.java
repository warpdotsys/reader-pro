// 
// Decompiled by Procyon v0.6.0
// 

package io.legado.app.exception;

import kotlin.jvm.internal.Intrinsics;
import org.jetbrains.annotations.NotNull;
import kotlin.Metadata;

@Metadata(mv = { 1, 5, 1 }, k = 1, xi = 48, d1 = { "\u0000\u001c\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u000e\n\u0002\b\u0002\n\u0002\u0010\u0003\n\u0000\b\u0016\u0018\u00002\u00060\u0001j\u0002`\u0002B\r\u0012\u0006\u0010\u0003\u001a\u00020\u0004?\u0006\u0002\u0010\u0005J\b\u0010\u0006\u001a\u00020\u0007H\u0016¡§\u0006\b" }, d2 = { "Lio/legado/app/exception/NoStackTraceException;", "Ljava/lang/Exception;", "Lkotlin/Exception;", "msg", "", "(Ljava/lang/String;)V", "fillInStackTrace", "", "reader-pro" })
public class NoStackTraceException extends Exception
{
    public NoStackTraceException(@NotNull final String msg) {
        Intrinsics.checkNotNullParameter((Object)msg, "msg");
        super(msg);
    }
    
    @NotNull
    @Override
    public Throwable fillInStackTrace() {
        return this;
    }
}
