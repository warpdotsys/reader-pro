// 
// Decompiled by Procyon v0.6.0
// 

package io.legado.app.model;

import org.jetbrains.annotations.Nullable;
import org.jetbrains.annotations.NotNull;
import kotlin.Metadata;

@Metadata(mv = { 1, 5, 1 }, k = 1, xi = 48, d1 = { "\u0000\f\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\b\u00c6\u0002\u0018\u00002\u00020\u0001B\u0007\b\u0002?\u0006\u0002\u0010\u0002¡§\u0006\u0003" }, d2 = { "Lio/legado/app/model/Debug;", "Lio/legado/app/model/DebugLog;", "()V", "reader-pro" })
public final class Debug implements DebugLog
{
    @NotNull
    public static final Debug INSTANCE;
    
    private Debug() {
    }
    
    @Override
    public void log(@NotNull final String message) {
        DefaultImpls.log(message);
    }
    
    @Override
    public void log(@Nullable final String sourceUrl, @Nullable final String msg, final boolean isHtml) {
        DefaultImpls.log(sourceUrl, msg, isHtml);
    }
    
    static {
        INSTANCE = new Debug();
    }
}
