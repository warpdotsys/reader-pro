// 
// Decompiled by Procyon v0.6.0
// 

package io.legado.app.help;

import kotlin.text.StringsKt;
import org.jetbrains.annotations.Nullable;
import kotlin.jvm.JvmOverloads;
import java.util.Map;
import java.io.File;
import io.legado.app.adapters.ReaderAdapterHelper;
import kotlin.jvm.internal.Intrinsics;
import io.legado.app.utils.ACache;
import io.legado.app.model.analyzeRule.QueryTTF;
import kotlin.Pair;
import java.util.HashMap;
import org.jetbrains.annotations.NotNull;
import kotlin.Metadata;

@Metadata(mv = { 1, 5, 1 }, k = 1, xi = 48, d1 = { "\u0000X\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0000\n\u0002\u0010\u000e\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0010\t\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0010\u0002\n\u0002\b\u0003\n\u0002\u0010\u0012\n\u0000\n\u0002\u0010\u0006\n\u0002\b\u0003\n\u0002\u0010\u0007\n\u0002\b\u0002\n\u0002\u0010\b\n\u0002\b\t\u0018\u00002\u00020\u0001B\r\u0012\u0006\u0010\u0002\u001a\u00020\u0003?\u0006\u0002\u0010\u0004J\u000e\u0010\u0011\u001a\u00020\u00122\u0006\u0010\u0013\u001a\u00020\u0003J\u0010\u0010\u0014\u001a\u0004\u0018\u00010\u00032\u0006\u0010\u0013\u001a\u00020\u0003J\u0010\u0010\u0015\u001a\u0004\u0018\u00010\u00162\u0006\u0010\u0013\u001a\u00020\u0003J\u0015\u0010\u0017\u001a\u0004\u0018\u00010\u00182\u0006\u0010\u0013\u001a\u00020\u0003?\u0006\u0002\u0010\u0019J\u0010\u0010\u001a\u001a\u0004\u0018\u00010\u00032\u0006\u0010\u0013\u001a\u00020\u0003J\u0015\u0010\u001b\u001a\u0004\u0018\u00010\u001c2\u0006\u0010\u0013\u001a\u00020\u0003?\u0006\u0002\u0010\u001dJ\u0015\u0010\u001e\u001a\u0004\u0018\u00010\u001f2\u0006\u0010\u0013\u001a\u00020\u0003?\u0006\u0002\u0010 J\u0015\u0010!\u001a\u0004\u0018\u00010\f2\u0006\u0010\u0013\u001a\u00020\u0003?\u0006\u0002\u0010\"J\u0010\u0010#\u001a\u0004\u0018\u00010\r2\u0006\u0010\u0013\u001a\u00020\u0003J\"\u0010$\u001a\u00020\u00122\u0006\u0010\u0013\u001a\u00020\u00032\u0006\u0010%\u001a\u00020\u00012\b\b\u0002\u0010&\u001a\u00020\u001fH\u0007J \u0010'\u001a\u00020\u00122\u0006\u0010\u0013\u001a\u00020\u00032\u0006\u0010%\u001a\u00020\u00032\b\b\u0002\u0010&\u001a\u00020\u001fR\u0011\u0010\u0005\u001a\u00020\u0006?\u0006\b\n\u0000\u001a\u0004\b\u0007\u0010\bRB\u0010\t\u001a6\u0012\u0004\u0012\u00020\u0003\u0012\u0010\u0012\u000e\u0012\u0004\u0012\u00020\f\u0012\u0004\u0012\u00020\r0\u000b0\nj\u001a\u0012\u0004\u0012\u00020\u0003\u0012\u0010\u0012\u000e\u0012\u0004\u0012\u00020\f\u0012\u0004\u0012\u00020\r0\u000b`\u000eX\u0082\u0004?\u0006\u0002\n\u0000R\u0011\u0010\u0002\u001a\u00020\u0003?\u0006\b\n\u0000\u001a\u0004\b\u000f\u0010\u0010¡§\u0006(" }, d2 = { "Lio/legado/app/help/CacheManager;", "", "userNameSpace", "", "(Ljava/lang/String;)V", "cacheInstance", "Lio/legado/app/utils/ACache;", "getCacheInstance", "()Lio/legado/app/utils/ACache;", "queryTTFMap", "Ljava/util/HashMap;", "Lkotlin/Pair;", "", "Lio/legado/app/model/analyzeRule/QueryTTF;", "Lkotlin/collections/HashMap;", "getUserNameSpace", "()Ljava/lang/String;", "delete", "", "key", "get", "getByteArray", "", "getDouble", "", "(Ljava/lang/String;)Ljava/lang/Double;", "getFile", "getFloat", "", "(Ljava/lang/String;)Ljava/lang/Float;", "getInt", "", "(Ljava/lang/String;)Ljava/lang/Integer;", "getLong", "(Ljava/lang/String;)Ljava/lang/Long;", "getQueryTTF", "put", "value", "saveTime", "putFile", "reader-pro" })
public final class CacheManager
{
    @NotNull
    private final String userNameSpace;
    @NotNull
    private final HashMap<String, Pair<Long, QueryTTF>> queryTTFMap;
    @NotNull
    private final ACache cacheInstance;
    
    public CacheManager(@NotNull final String userNameSpace) {
        Intrinsics.checkNotNullParameter((Object)userNameSpace, "userNameSpace");
        this.userNameSpace = userNameSpace;
        this.queryTTFMap = new HashMap<String, Pair<Long, QueryTTF>>();
        final File cacheDir = new File(ReaderAdapterHelper.INSTANCE.getAdapter().getWorkDir("storage", "cache", "runtimeCache", this.userNameSpace));
        this.cacheInstance = ACache.Companion.get(cacheDir, 50000000L, 1000000);
    }
    
    @NotNull
    public final String getUserNameSpace() {
        return this.userNameSpace;
    }
    
    @NotNull
    public final ACache getCacheInstance() {
        return this.cacheInstance;
    }
    
    @JvmOverloads
    public final void put(@NotNull final String key, @NotNull final Object value, final int saveTime) {
        Intrinsics.checkNotNullParameter((Object)key, "key");
        Intrinsics.checkNotNullParameter(value, "value");
        if (key.length() == 0) {
            return;
        }
        final long deadline = (saveTime == 0) ? 0L : (System.currentTimeMillis() + saveTime * 1000);
        if (value instanceof QueryTTF) {
            this.queryTTFMap.put(key, new Pair((Object)deadline, value));
        }
        else if (value instanceof byte[]) {
            this.cacheInstance.put(key, (byte[])value, saveTime);
        }
        else {
            this.cacheInstance.put(key, value.toString(), saveTime);
        }
    }
    
    public static /* synthetic */ void put$default(final CacheManager cacheManager, final String key, final Object value, int saveTime, final int n, final Object o) {
        if ((n & 0x4) != 0x0) {
            saveTime = 0;
        }
        cacheManager.put(key, value, saveTime);
    }
    
    @Nullable
    public final String get(@NotNull final String key) {
        Intrinsics.checkNotNullParameter((Object)key, "key");
        if (key.length() == 0) {
            return null;
        }
        return this.cacheInstance.getAsString(key);
    }
    
    @Nullable
    public final Integer getInt(@NotNull final String key) {
        Intrinsics.checkNotNullParameter((Object)key, "key");
        final String value = this.get(key);
        return (value == null) ? null : StringsKt.toIntOrNull(value);
    }
    
    @Nullable
    public final Long getLong(@NotNull final String key) {
        Intrinsics.checkNotNullParameter((Object)key, "key");
        final String value = this.get(key);
        return (value == null) ? null : StringsKt.toLongOrNull(value);
    }
    
    @Nullable
    public final Double getDouble(@NotNull final String key) {
        Intrinsics.checkNotNullParameter((Object)key, "key");
        final String value = this.get(key);
        return (value == null) ? null : StringsKt.toDoubleOrNull(value);
    }
    
    @Nullable
    public final Float getFloat(@NotNull final String key) {
        Intrinsics.checkNotNullParameter((Object)key, "key");
        final String value = this.get(key);
        return (value == null) ? null : StringsKt.toFloatOrNull(value);
    }
    
    @Nullable
    public final byte[] getByteArray(@NotNull final String key) {
        Intrinsics.checkNotNullParameter((Object)key, "key");
        if (key.length() == 0) {
            return null;
        }
        return this.cacheInstance.getAsBinary(key);
    }
    
    @Nullable
    public final QueryTTF getQueryTTF(@NotNull final String key) {
        Intrinsics.checkNotNullParameter((Object)key, "key");
        final Pair pair = this.queryTTFMap.get(key);
        if (pair == null) {
            return null;
        }
        final Pair cache = pair;
        if (((Number)cache.getFirst()).longValue() == 0L || ((Number)cache.getFirst()).longValue() > System.currentTimeMillis()) {
            return (QueryTTF)cache.getSecond();
        }
        return null;
    }
    
    public final void putFile(@NotNull final String key, @NotNull final String value, final int saveTime) {
        Intrinsics.checkNotNullParameter((Object)key, "key");
        Intrinsics.checkNotNullParameter((Object)value, "value");
        if (key.length() == 0) {
            return;
        }
        this.cacheInstance.put(key, value, saveTime);
    }
    
    @Nullable
    public final String getFile(@NotNull final String key) {
        Intrinsics.checkNotNullParameter((Object)key, "key");
        if (key.length() == 0) {
            return null;
        }
        return this.cacheInstance.getAsString(key);
    }
    
    public final void delete(@NotNull final String key) {
        Intrinsics.checkNotNullParameter((Object)key, "key");
        if (key.length() == 0) {
            return;
        }
        this.cacheInstance.remove(key);
    }
    
    @JvmOverloads
    public final void put(@NotNull final String key, @NotNull final Object value) {
        Intrinsics.checkNotNullParameter((Object)key, "key");
        Intrinsics.checkNotNullParameter(value, "value");
        put$default(this, key, value, 0, 4, null);
    }
}
