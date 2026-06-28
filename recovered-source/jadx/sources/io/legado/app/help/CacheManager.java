package io.legado.app.help;

import io.legado.app.adapters.ReaderAdapterHelper;
import io.legado.app.model.analyzeRule.QueryTTF;
import io.legado.app.utils.ACache;
import java.io.File;
import java.util.HashMap;
import kotlin.Metadata;
import kotlin.Pair;
import kotlin.jvm.JvmOverloads;
import kotlin.jvm.internal.Intrinsics;
import kotlin.text.StringsKt;
import me.ag2s.epublib.browsersupport.NavigationHistory;
import me.ag2s.epublib.epub.PackageDocumentBase;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/* JADX INFO: compiled from: CacheManager.kt */
/* JADX INFO: loaded from: app-classes.jar:io/legado/app/help/CacheManager.class */
@Metadata(mv = {1, 5, 1}, k = 1, xi = 48, d1 = {"\u0000X\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0000\n\u0002\u0010\u000e\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0010\t\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0010\u0002\n\u0002\b\u0003\n\u0002\u0010\u0012\n\u0000\n\u0002\u0010\u0006\n\u0002\b\u0003\n\u0002\u0010\u0007\n\u0002\b\u0002\n\u0002\u0010\b\n\u0002\b\t\u0018\u00002\u00020\u0001B\r\u0012\u0006\u0010\u0002\u001a\u00020\u0003¢\u0006\u0002\u0010\u0004J\u000e\u0010\u0011\u001a\u00020\u00122\u0006\u0010\u0013\u001a\u00020\u0003J\u0010\u0010\u0014\u001a\u0004\u0018\u00010\u00032\u0006\u0010\u0013\u001a\u00020\u0003J\u0010\u0010\u0015\u001a\u0004\u0018\u00010\u00162\u0006\u0010\u0013\u001a\u00020\u0003J\u0015\u0010\u0017\u001a\u0004\u0018\u00010\u00182\u0006\u0010\u0013\u001a\u00020\u0003¢\u0006\u0002\u0010\u0019J\u0010\u0010\u001a\u001a\u0004\u0018\u00010\u00032\u0006\u0010\u0013\u001a\u00020\u0003J\u0015\u0010\u001b\u001a\u0004\u0018\u00010\u001c2\u0006\u0010\u0013\u001a\u00020\u0003¢\u0006\u0002\u0010\u001dJ\u0015\u0010\u001e\u001a\u0004\u0018\u00010\u001f2\u0006\u0010\u0013\u001a\u00020\u0003¢\u0006\u0002\u0010 J\u0015\u0010!\u001a\u0004\u0018\u00010\f2\u0006\u0010\u0013\u001a\u00020\u0003¢\u0006\u0002\u0010\"J\u0010\u0010#\u001a\u0004\u0018\u00010\r2\u0006\u0010\u0013\u001a\u00020\u0003J\"\u0010$\u001a\u00020\u00122\u0006\u0010\u0013\u001a\u00020\u00032\u0006\u0010%\u001a\u00020\u00012\b\b\u0002\u0010&\u001a\u00020\u001fH\u0007J \u0010'\u001a\u00020\u00122\u0006\u0010\u0013\u001a\u00020\u00032\u0006\u0010%\u001a\u00020\u00032\b\b\u0002\u0010&\u001a\u00020\u001fR\u0011\u0010\u0005\u001a\u00020\u0006¢\u0006\b\n\u0000\u001a\u0004\b\u0007\u0010\bRB\u0010\t\u001a6\u0012\u0004\u0012\u00020\u0003\u0012\u0010\u0012\u000e\u0012\u0004\u0012\u00020\f\u0012\u0004\u0012\u00020\r0\u000b0\nj\u001a\u0012\u0004\u0012\u00020\u0003\u0012\u0010\u0012\u000e\u0012\u0004\u0012\u00020\f\u0012\u0004\u0012\u00020\r0\u000b`\u000eX\u0082\u0004¢\u0006\u0002\n\u0000R\u0011\u0010\u0002\u001a\u00020\u0003¢\u0006\b\n\u0000\u001a\u0004\b\u000f\u0010\u0010¨\u0006("}, d2 = {"Lio/legado/app/help/CacheManager;", PackageDocumentBase.PREFIX_OPF, "userNameSpace", PackageDocumentBase.PREFIX_OPF, "(Ljava/lang/String;)V", "cacheInstance", "Lio/legado/app/utils/ACache;", "getCacheInstance", "()Lio/legado/app/utils/ACache;", "queryTTFMap", "Ljava/util/HashMap;", "Lkotlin/Pair;", PackageDocumentBase.PREFIX_OPF, "Lio/legado/app/model/analyzeRule/QueryTTF;", "Lkotlin/collections/HashMap;", "getUserNameSpace", "()Ljava/lang/String;", "delete", PackageDocumentBase.PREFIX_OPF, "key", "get", "getByteArray", PackageDocumentBase.PREFIX_OPF, "getDouble", PackageDocumentBase.PREFIX_OPF, "(Ljava/lang/String;)Ljava/lang/Double;", "getFile", "getFloat", PackageDocumentBase.PREFIX_OPF, "(Ljava/lang/String;)Ljava/lang/Float;", "getInt", PackageDocumentBase.PREFIX_OPF, "(Ljava/lang/String;)Ljava/lang/Integer;", "getLong", "(Ljava/lang/String;)Ljava/lang/Long;", "getQueryTTF", "put", "value", "saveTime", "putFile", "reader-pro"})
public final class CacheManager {

    @NotNull
    private final String userNameSpace;

    @NotNull
    private final HashMap<String, Pair<Long, QueryTTF>> queryTTFMap;

    @NotNull
    private final ACache cacheInstance;

    @JvmOverloads
    public final void put(@NotNull String key, @NotNull Object value) {
        Intrinsics.checkNotNullParameter(key, "key");
        Intrinsics.checkNotNullParameter(value, "value");
        put$default(this, key, value, 0, 4, null);
    }

    public CacheManager(@NotNull String userNameSpace) {
        Intrinsics.checkNotNullParameter(userNameSpace, "userNameSpace");
        this.userNameSpace = userNameSpace;
        this.queryTTFMap = new HashMap<>();
        File cacheDir = new File(ReaderAdapterHelper.INSTANCE.getAdapter().getWorkDir("storage", "cache", "runtimeCache", this.userNameSpace));
        this.cacheInstance = ACache.INSTANCE.get(cacheDir, 50000000L, 1000000);
    }

    @NotNull
    public final String getUserNameSpace() {
        return this.userNameSpace;
    }

    @NotNull
    public final ACache getCacheInstance() {
        return this.cacheInstance;
    }

    public static /* synthetic */ void put$default(CacheManager cacheManager, String str, Object obj, int i, int i2, Object obj2) {
        if ((i2 & 4) != 0) {
            i = 0;
        }
        cacheManager.put(str, obj, i);
    }

    @JvmOverloads
    public final void put(@NotNull String key, @NotNull Object value, int saveTime) {
        Intrinsics.checkNotNullParameter(key, "key");
        Intrinsics.checkNotNullParameter(value, "value");
        if (key.length() == 0) {
            return;
        }
        long deadline = saveTime == 0 ? 0L : System.currentTimeMillis() + ((long) (saveTime * NavigationHistory.DEFAULT_MAX_HISTORY_SIZE));
        if (!(value instanceof QueryTTF)) {
            if (value instanceof byte[]) {
                this.cacheInstance.put(key, (byte[]) value, saveTime);
                return;
            } else {
                this.cacheInstance.put(key, value.toString(), saveTime);
                return;
            }
        }
        this.queryTTFMap.put(key, new Pair<>(Long.valueOf(deadline), value));
    }

    @Nullable
    public final String get(@NotNull String key) {
        Intrinsics.checkNotNullParameter(key, "key");
        if (key.length() == 0) {
            return null;
        }
        return this.cacheInstance.getAsString(key);
    }

    @Nullable
    public final Integer getInt(@NotNull String key) {
        Intrinsics.checkNotNullParameter(key, "key");
        String str = get(key);
        if (str == null) {
            return null;
        }
        return StringsKt.toIntOrNull(str);
    }

    @Nullable
    public final Long getLong(@NotNull String key) {
        Intrinsics.checkNotNullParameter(key, "key");
        String str = get(key);
        if (str == null) {
            return null;
        }
        return StringsKt.toLongOrNull(str);
    }

    @Nullable
    public final Double getDouble(@NotNull String key) {
        Intrinsics.checkNotNullParameter(key, "key");
        String str = get(key);
        if (str == null) {
            return null;
        }
        return StringsKt.toDoubleOrNull(str);
    }

    @Nullable
    public final Float getFloat(@NotNull String key) {
        Intrinsics.checkNotNullParameter(key, "key");
        String str = get(key);
        if (str == null) {
            return null;
        }
        return StringsKt.toFloatOrNull(str);
    }

    @Nullable
    public final byte[] getByteArray(@NotNull String key) {
        Intrinsics.checkNotNullParameter(key, "key");
        if (key.length() == 0) {
            return null;
        }
        return this.cacheInstance.getAsBinary(key);
    }

    @Nullable
    public final QueryTTF getQueryTTF(@NotNull String key) {
        Intrinsics.checkNotNullParameter(key, "key");
        Pair<Long, QueryTTF> pair = this.queryTTFMap.get(key);
        if (pair == null) {
            return null;
        }
        if (((Number) pair.getFirst()).longValue() == 0 || ((Number) pair.getFirst()).longValue() > System.currentTimeMillis()) {
            return (QueryTTF) pair.getSecond();
        }
        return null;
    }

    public static /* synthetic */ void putFile$default(CacheManager cacheManager, String str, String str2, int i, int i2, Object obj) {
        if ((i2 & 4) != 0) {
            i = 0;
        }
        cacheManager.putFile(str, str2, i);
    }

    public final void putFile(@NotNull String key, @NotNull String value, int saveTime) {
        Intrinsics.checkNotNullParameter(key, "key");
        Intrinsics.checkNotNullParameter(value, "value");
        if (key.length() == 0) {
            return;
        }
        this.cacheInstance.put(key, value, saveTime);
    }

    @Nullable
    public final String getFile(@NotNull String key) {
        Intrinsics.checkNotNullParameter(key, "key");
        if (key.length() == 0) {
            return null;
        }
        return this.cacheInstance.getAsString(key);
    }

    public final void delete(@NotNull String key) {
        Intrinsics.checkNotNullParameter(key, "key");
        if (key.length() == 0) {
            return;
        }
        this.cacheInstance.remove(key);
    }
}
