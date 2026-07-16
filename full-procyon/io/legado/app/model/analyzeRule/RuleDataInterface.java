// 
// Decompiled by Procyon v0.6.0
// 

package io.legado.app.model.analyzeRule;

import kotlin.jvm.internal.Intrinsics;
import org.jetbrains.annotations.Nullable;
import org.jetbrains.annotations.NotNull;
import java.util.HashMap;
import kotlin.Metadata;

@Metadata(mv = { 1, 5, 1 }, k = 1, xi = 48, d1 = { "\u0000\"\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0000\n\u0002\u0018\u0002\n\u0002\u0010\u000e\n\u0002\u0018\u0002\n\u0002\b\u0006\n\u0002\u0010\u0002\n\u0002\b\u0002\bf\u0018\u00002\u00020\u0001J\b\u0010\b\u001a\u00020\u0004H&J\u0012\u0010\t\u001a\u0004\u0018\u00010\u00042\u0006\u0010\n\u001a\u00020\u0004H\u0016J\u001a\u0010\u000b\u001a\u00020\f2\u0006\u0010\n\u001a\u00020\u00042\b\u0010\r\u001a\u0004\u0018\u00010\u0004H&R.\u0010\u0002\u001a\u001e\u0012\u0004\u0012\u00020\u0004\u0012\u0004\u0012\u00020\u00040\u0003j\u000e\u0012\u0004\u0012\u00020\u0004\u0012\u0004\u0012\u00020\u0004`\u0005X?\u0004?\u0006\u0006\u001a\u0004\b\u0006\u0010\u0007¡§\u0006\u000e" }, d2 = { "Lio/legado/app/model/analyzeRule/RuleDataInterface;", "", "variableMap", "Ljava/util/HashMap;", "", "Lkotlin/collections/HashMap;", "getVariableMap", "()Ljava/util/HashMap;", "getUserNameSpace", "getVariable", "key", "putVariable", "", "value", "reader-pro" })
public interface RuleDataInterface
{
    @NotNull
    HashMap<String, String> getVariableMap();
    
    void putVariable(@NotNull final String key, @Nullable final String value);
    
    @Nullable
    String getVariable(@NotNull final String key);
    
    @NotNull
    String getUserNameSpace();
    
    @Metadata(mv = { 1, 5, 1 }, k = 3, xi = 48)
    public static final class DefaultImpls
    {
        @Nullable
        public static String getVariable(@NotNull final RuleDataInterface this, @NotNull final String key) {
            Intrinsics.checkNotNullParameter((Object)this, "this");
            Intrinsics.checkNotNullParameter((Object)key, "key");
            return this.getVariableMap().get(key);
        }
    }
}
