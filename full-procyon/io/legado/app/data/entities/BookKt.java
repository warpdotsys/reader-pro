// 
// Decompiled by Procyon v0.6.0
// 

package io.legado.app.data.entities;

import kotlin.jvm.functions.Function0;
import mu.KotlinLogging;
import org.jetbrains.annotations.NotNull;
import mu.KLogger;
import kotlin.Metadata;

@Metadata(mv = { 1, 5, 1 }, k = 2, xi = 48, d1 = { "\u0000\n\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0003\"\u0011\u0010\u0000\u001a\u00020\u0001?\u0006\b\n\u0000\u001a\u0004\b\u0002\u0010\u0003¡§\u0006\u0004" }, d2 = { "logger", "Lmu/KLogger;", "getLogger", "()Lmu/KLogger;", "reader-pro" })
public final class BookKt
{
    @NotNull
    private static final KLogger logger;
    
    @NotNull
    public static final KLogger getLogger() {
        return BookKt.logger;
    }
    
    static {
        logger = KotlinLogging.INSTANCE.logger((Function0)BookKt$logger.BookKt$logger$1.INSTANCE);
    }
}
