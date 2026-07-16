// 
// Decompiled by Procyon v0.6.0
// 

package io.legado.app.model;

import kotlin.jvm.internal.Intrinsics;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import kotlin.Metadata;
import okhttp3.logging.HttpLoggingInterceptor$Logger;

@Metadata(mv = { 1, 5, 1 }, k = 1, xi = 48, d1 = { "\u0000\u001e\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u0002\n\u0000\n\u0002\u0010\u000e\n\u0002\b\u0003\n\u0002\u0010\u000b\n\u0000\bf\u0018\u00002\u00020\u0001J\u0010\u0010\u0002\u001a\u00020\u00032\u0006\u0010\u0004\u001a\u00020\u0005H\u0016J*\u0010\u0002\u001a\u00020\u00032\n\b\u0002\u0010\u0006\u001a\u0004\u0018\u00010\u00052\n\b\u0002\u0010\u0007\u001a\u0004\u0018\u00010\u00052\b\b\u0002\u0010\b\u001a\u00020\tH\u0016¡§\u0006\n" }, d2 = { "Lio/legado/app/model/DebugLog;", "Lokhttp3/logging/HttpLoggingInterceptor$Logger;", "log", "", "message", "", "sourceUrl", "msg", "isHtml", "", "reader-pro" })
public interface DebugLog extends HttpLoggingInterceptor$Logger
{
    void log(@Nullable final String sourceUrl, @Nullable final String msg, final boolean isHtml);
    
    void log(@NotNull final String message);
    
    @Metadata(mv = { 1, 5, 1 }, k = 3, xi = 48)
    public static final class DefaultImpls
    {
        public static void log(@NotNull final DebugLog this, @Nullable final String sourceUrl, @Nullable final String msg, final boolean isHtml) {
            Intrinsics.checkNotNullParameter((Object)this, "this");
            DebugLogKt.access$getLogger$p().info("sourceUrl: {}, msg: {}", (Object)sourceUrl, (Object)msg);
        }
        
        public static void log(@NotNull final DebugLog this, @NotNull final String message) {
            Intrinsics.checkNotNullParameter((Object)this, "this");
            Intrinsics.checkNotNullParameter((Object)message, "message");
            DebugLogKt.access$getLogger$p().debug(message);
        }
    }
}
