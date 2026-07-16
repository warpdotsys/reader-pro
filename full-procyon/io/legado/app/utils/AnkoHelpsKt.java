// 
// Decompiled by Procyon v0.6.0
// 

package io.legado.app.utils;

import kotlin.jvm.internal.Intrinsics;
import org.jetbrains.annotations.NotNull;
import kotlin.jvm.functions.Function0;
import kotlin.Metadata;

@Metadata(mv = { 1, 5, 1 }, k = 2, xi = 48, d1 = { "\u0000\u0010\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\u001a&\u0010\u0000\u001a\b\u0012\u0004\u0012\u0002H\u00020\u0001\"\u0004\b\u0000\u0010\u00022\f\u0010\u0003\u001a\b\u0012\u0004\u0012\u0002H\u00020\u0004H\u0086\b\u00f8\u0001\u0000\u0082\u0002\u0007\n\u0005\b\u009920\u0001¡§\u0006\u0005" }, d2 = { "attempt", "Lio/legado/app/utils/AttemptResult;", "T", "f", "Lkotlin/Function0;", "reader-pro" })
public final class AnkoHelpsKt
{
    @NotNull
    public static final <T> AttemptResult<T> attempt(@NotNull final Function0<? extends T> f) {
        Intrinsics.checkNotNullParameter((Object)f, "f");
        final int $i$f$attempt = 0;
        Object value = null;
        Throwable error = null;
        try {
            value = f.invoke();
        }
        catch (final Throwable t) {
            error = t;
        }
        return new AttemptResult<T>((T)value, error);
    }
}
