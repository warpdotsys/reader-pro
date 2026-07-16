// 
// Decompiled by Procyon v0.6.0
// 

package io.legado.app.utils;

import java.util.Iterator;
import java.util.Set;
import java.util.Collections;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;
import kotlin.text.StringsKt;
import kotlin.text.Charsets;
import java.util.Map;
import io.legado.app.adapters.ReaderAdapterHelper;
import kotlin.jvm.internal.DefaultConstructorMarker;
import java.io.ObjectInputStream;
import java.io.InputStream;
import java.io.ByteArrayInputStream;
import kotlin.jvm.JvmOverloads;
import kotlin.io.CloseableKt;
import kotlin.Unit;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.io.Closeable;
import java.io.ByteArrayOutputStream;
import java.io.Serializable;
import java.io.IOException;
import java.nio.charset.Charset;
import kotlin.io.FilesKt;
import kotlin.jvm.internal.Intrinsics;
import java.io.File;
import java.util.HashMap;
import org.jetbrains.annotations.Nullable;
import org.jetbrains.annotations.NotNull;
import kotlin.Metadata;

@Metadata(mv = { 1, 5, 1 }, k = 1, xi = 48, d1 = { "\u0000J\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\t\n\u0000\n\u0002\u0010\b\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u0002\n\u0002\b\u0002\n\u0002\u0010\u000e\n\u0000\n\u0002\u0010\u0012\n\u0002\b\u0006\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\u000b\n\u0002\b\u0004\u0018\u0000 \u001d2\u00020\u0001:\u0003\u001c\u001d\u001eB\u001f\b\u0002\u0012\u0006\u0010\u0002\u001a\u00020\u0003\u0012\u0006\u0010\u0004\u001a\u00020\u0005\u0012\u0006\u0010\u0006\u001a\u00020\u0007?\u0006\u0002\u0010\bJ\u0006\u0010\u000b\u001a\u00020\fJ\u0010\u0010\r\u001a\u0004\u0018\u00010\u00032\u0006\u0010\u000e\u001a\u00020\u000fJ\u0010\u0010\u0010\u001a\u0004\u0018\u00010\u00112\u0006\u0010\u000e\u001a\u00020\u000fJ\u0010\u0010\u0012\u001a\u0004\u0018\u00010\u00012\u0006\u0010\u000e\u001a\u00020\u000fJ\u0010\u0010\u0013\u001a\u0004\u0018\u00010\u000f2\u0006\u0010\u000e\u001a\u00020\u000fJ\u0010\u0010\u0014\u001a\u0004\u0018\u00010\u000f2\u0006\u0010\u0015\u001a\u00020\u000fJ\"\u0010\u0016\u001a\u00020\f2\u0006\u0010\u000e\u001a\u00020\u000f2\u0006\u0010\u0017\u001a\u00020\u00182\b\b\u0002\u0010\u0019\u001a\u00020\u0007H\u0007J\u0016\u0010\u0016\u001a\u00020\f2\u0006\u0010\u000e\u001a\u00020\u000f2\u0006\u0010\u0017\u001a\u00020\u0011J\u001e\u0010\u0016\u001a\u00020\f2\u0006\u0010\u000e\u001a\u00020\u000f2\u0006\u0010\u0017\u001a\u00020\u00112\u0006\u0010\u0019\u001a\u00020\u0007J\u0016\u0010\u0016\u001a\u00020\f2\u0006\u0010\u000e\u001a\u00020\u000f2\u0006\u0010\u0017\u001a\u00020\u000fJ\u001e\u0010\u0016\u001a\u00020\f2\u0006\u0010\u000e\u001a\u00020\u000f2\u0006\u0010\u0017\u001a\u00020\u000f2\u0006\u0010\u0019\u001a\u00020\u0007J\u000e\u0010\u001a\u001a\u00020\u001b2\u0006\u0010\u000e\u001a\u00020\u000fR\u0014\u0010\t\u001a\b\u0018\u00010\nR\u00020\u0000X\u0082\u000e?\u0006\u0002\n\u0000：\u0006\u001f" }, d2 = { "Lio/legado/app/utils/ACache;", "", "cacheDir", "Ljava/io/File;", "max_size", "", "max_count", "", "(Ljava/io/File;JI)V", "mCache", "Lio/legado/app/utils/ACache$ACacheManager;", "clear", "", "file", "key", "", "getAsBinary", "", "getAsObject", "getAsString", "getByHashCode", "hashCode", "put", "value", "Ljava/io/Serializable;", "saveTime", "remove", "", "ACacheManager", "Companion", "Utils", "reader-pro" })
public final class ACache
{
    @NotNull
    public static final Companion Companion;
    @Nullable
    private ACacheManager mCache;
    public static final int TIME_HOUR = 3600;
    public static final int TIME_DAY = 86400;
    private static final int MAX_SIZE = 50000000;
    private static final int MAX_COUNT = Integer.MAX_VALUE;
    @NotNull
    private static final HashMap<String, ACache> mInstanceMap;
    
    private ACache(final File cacheDir, final long max_size, final int max_count) {
        try {
            if (!cacheDir.exists() && !cacheDir.mkdirs()) {
                ACacheKt.access$getLogger$p().info(Intrinsics.stringPlus("ACache can't make dirs in %s", (Object)cacheDir.getAbsolutePath()));
            }
            this.mCache = new ACacheManager(cacheDir, max_size, max_count);
        }
        catch (final Exception e) {
            e.printStackTrace();
        }
    }
    
    public final void put(@NotNull final String key, @NotNull final String value) {
        Intrinsics.checkNotNullParameter((Object)key, "key");
        Intrinsics.checkNotNullParameter((Object)value, "value");
        final ACacheManager mCache2 = this.mCache;
        if (mCache2 != null) {
            final ACacheManager mCache = mCache2;
            final int n = 0;
            try {
                final File file = mCache.newFile(key);
                FilesKt.writeText$default(file, value, (Charset)null, 2, (Object)null);
                mCache.put(file);
            }
            catch (final Exception e) {
                e.printStackTrace();
            }
        }
    }
    
    public final void put(@NotNull final String key, @NotNull final String value, final int saveTime) {
        Intrinsics.checkNotNullParameter((Object)key, "key");
        Intrinsics.checkNotNullParameter((Object)value, "value");
        if (saveTime <= 0) {
            this.put(key, value);
            return;
        }
        this.put(key, Utils.INSTANCE.newStringWithDateInfo(saveTime, value));
    }
    
    @Nullable
    public final String getAsString(@NotNull final String key) {
        Intrinsics.checkNotNullParameter((Object)key, "key");
        final ACacheManager mCache2 = this.mCache;
        if (mCache2 != null) {
            final ACacheManager mCache = mCache2;
            final int n = 0;
            final File file = mCache.get(key);
            if (!file.exists()) {
                return null;
            }
            boolean removeFile = false;
            try {
                final String text = FilesKt.readText$default(file, (Charset)null, 1, (Object)null);
                if (!Utils.INSTANCE.isDue(text)) {
                    return Utils.INSTANCE.clearDateInfo(text);
                }
                removeFile = true;
                this.remove(key);
            }
            catch (final IOException e) {
                e.printStackTrace();
            }
        }
        return null;
    }
    
    @Nullable
    public final String getByHashCode(@NotNull final String hashCode) {
        Intrinsics.checkNotNullParameter((Object)hashCode, "hashCode");
        final ACacheManager mCache2 = this.mCache;
        if (mCache2 != null) {
            final ACacheManager mCache = mCache2;
            final int n = 0;
            final File file = mCache.newFileFromHashCode(hashCode);
            if (!file.exists()) {
                return null;
            }
            boolean removeFile = false;
            try {
                final String text = FilesKt.readText$default(file, (Charset)null, 1, (Object)null);
                if (!Utils.INSTANCE.isDue(text)) {
                    return Utils.INSTANCE.clearDateInfo(text);
                }
                removeFile = true;
                file.delete();
            }
            catch (final IOException e) {
                e.printStackTrace();
            }
        }
        return null;
    }
    
    public final void put(@NotNull final String key, @NotNull final byte[] value) {
        Intrinsics.checkNotNullParameter((Object)key, "key");
        Intrinsics.checkNotNullParameter((Object)value, "value");
        final ACacheManager mCache2 = this.mCache;
        if (mCache2 != null) {
            final ACacheManager mCache = mCache2;
            final int n = 0;
            final File file = mCache.newFile(key);
            FilesKt.writeBytes(file, value);
            mCache.put(file);
        }
    }
    
    public final void put(@NotNull final String key, @NotNull final byte[] value, final int saveTime) {
        Intrinsics.checkNotNullParameter((Object)key, "key");
        Intrinsics.checkNotNullParameter((Object)value, "value");
        if (saveTime <= 0) {
            this.put(key, value);
            return;
        }
        this.put(key, Utils.INSTANCE.newByteArrayWithDateInfo(saveTime, value));
    }
    
    @Nullable
    public final byte[] getAsBinary(@NotNull final String key) {
        Intrinsics.checkNotNullParameter((Object)key, "key");
        final ACacheManager mCache2 = this.mCache;
        if (mCache2 != null) {
            final ACacheManager mCache = mCache2;
            final int n = 0;
            boolean removeFile = false;
            try {
                final File file = mCache.get(key);
                if (!file.exists()) {
                    return null;
                }
                final byte[] byteArray = FilesKt.readBytes(file);
                byte[] clearDateInfo;
                if (!Utils.INSTANCE.isDue(byteArray)) {
                    clearDateInfo = Utils.INSTANCE.clearDateInfo(byteArray);
                }
                else {
                    removeFile = true;
                    clearDateInfo = null;
                }
                return clearDateInfo;
            }
            catch (final Exception e) {
                e.printStackTrace();
            }
            finally {
                if (removeFile) {
                    this.remove(key);
                }
            }
        }
        return null;
    }
    
    @JvmOverloads
    public final void put(@NotNull final String key, @NotNull final Serializable value, final int saveTime) {
        Intrinsics.checkNotNullParameter((Object)key, "key");
        Intrinsics.checkNotNullParameter((Object)value, "value");
        try {
            final ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
            final Closeable closeable = new ObjectOutputStream(byteArrayOutputStream);
            Throwable t = null;
            try {
                final ObjectOutputStream oos = (ObjectOutputStream)closeable;
                final int n = 0;
                oos.writeObject(value);
                final byte[] data = byteArrayOutputStream.toByteArray();
                if (saveTime != -1) {
                    Intrinsics.checkNotNullExpressionValue((Object)data, "data");
                    this.put(key, data, saveTime);
                }
                else {
                    Intrinsics.checkNotNullExpressionValue((Object)data, "data");
                    this.put(key, data);
                }
                final Unit instance = Unit.INSTANCE;
            }
            catch (final Throwable t2) {
                t = t2;
                throw t2;
            }
            finally {
                CloseableKt.closeFinally(closeable, t);
            }
        }
        catch (final Exception e) {
            e.printStackTrace();
        }
    }
    
    public static /* synthetic */ void put$default(final ACache aCache, final String key, final Serializable value, int saveTime, final int n, final Object o) {
        if ((n & 0x4) != 0x0) {
            saveTime = -1;
        }
        aCache.put(key, value, saveTime);
    }
    
    @Nullable
    public final Object getAsObject(@NotNull final String key) {
        Intrinsics.checkNotNullParameter((Object)key, "key");
        final byte[] data = this.getAsBinary(key);
        if (data != null) {
            ByteArrayInputStream bis = null;
            ObjectInputStream ois = null;
            try {
                bis = new ByteArrayInputStream(data);
                ois = new ObjectInputStream(bis);
                final Object object = ois.readObject();
                try {
                    bis.close();
                }
                catch (final IOException e) {
                    e.printStackTrace();
                }
                try {
                    ois.close();
                }
                catch (final IOException e) {
                    e.printStackTrace();
                }
                return object;
            }
            catch (final Exception e2) {
                e2.printStackTrace();
            }
            finally {
                try (final ByteArrayInputStream byteArrayInputStream = bis) {}
                catch (final IOException e) {
                    e.printStackTrace();
                }
                try (final ObjectInputStream objectInputStream = ois) {}
                catch (final IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return null;
    }
    
    @Nullable
    public final File file(@NotNull final String key) {
        Intrinsics.checkNotNullParameter((Object)key, "key");
        final ACacheManager mCache2 = this.mCache;
        if (mCache2 != null) {
            final ACacheManager mCache = mCache2;
            final int n = 0;
            try {
                final File f = mCache.newFile(key);
                if (f.exists()) {
                    return f;
                }
            }
            catch (final Exception e) {
                e.printStackTrace();
            }
        }
        return null;
    }
    
    public final boolean remove(@NotNull final String key) {
        Intrinsics.checkNotNullParameter((Object)key, "key");
        final ACacheManager mCache = this.mCache;
        return mCache != null && mCache.remove(key);
    }
    
    public final void clear() {
        final ACacheManager mCache = this.mCache;
        if (mCache != null) {
            mCache.clear();
        }
    }
    
    @JvmOverloads
    public final void put(@NotNull final String key, @NotNull final Serializable value) {
        Intrinsics.checkNotNullParameter((Object)key, "key");
        Intrinsics.checkNotNullParameter((Object)value, "value");
        put$default(this, key, value, 0, 4, null);
    }
    
    public static final /* synthetic */ HashMap access$getMInstanceMap$cp() {
        return ACache.mInstanceMap;
    }
    
    static {
        Companion = new Companion(null);
        mInstanceMap = new HashMap<String, ACache>();
    }
    
    @Metadata(mv = { 1, 5, 1 }, k = 1, xi = 48, d1 = { "\u00002\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\b\u0002\n\u0002\u0010\b\n\u0002\b\u0004\n\u0002\u0018\u0002\n\u0002\u0010\u000e\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\t\n\u0002\b\u0003\b\u0086\u0003\u0018\u00002\u00020\u0001B\u0007\b\u0002?\u0006\u0002\u0010\u0002J$\u0010\f\u001a\u00020\u000b2\u0006\u0010\r\u001a\u00020\u000e2\b\b\u0002\u0010\u000f\u001a\u00020\u00102\b\b\u0002\u0010\u0011\u001a\u00020\u0004H\u0007J&\u0010\f\u001a\u00020\u000b2\b\b\u0002\u0010\u0012\u001a\u00020\n2\b\b\u0002\u0010\u000f\u001a\u00020\u00102\b\b\u0002\u0010\u0011\u001a\u00020\u0004H\u0007R\u000e\u0010\u0003\u001a\u00020\u0004X\u0082T?\u0006\u0002\n\u0000R\u000e\u0010\u0005\u001a\u00020\u0004X\u0082T?\u0006\u0002\n\u0000R\u000e\u0010\u0006\u001a\u00020\u0004X\u0086T?\u0006\u0002\n\u0000R\u000e\u0010\u0007\u001a\u00020\u0004X\u0086T?\u0006\u0002\n\u0000R\u001a\u0010\b\u001a\u000e\u0012\u0004\u0012\u00020\n\u0012\u0004\u0012\u00020\u000b0\tX\u0082\u0004?\u0006\u0002\n\u0000：\u0006\u0013" }, d2 = { "Lio/legado/app/utils/ACache$Companion;", "", "()V", "MAX_COUNT", "", "MAX_SIZE", "TIME_DAY", "TIME_HOUR", "mInstanceMap", "Ljava/util/HashMap;", "", "Lio/legado/app/utils/ACache;", "get", "cacheDir", "Ljava/io/File;", "maxSize", "", "maxCount", "cacheName", "reader-pro" })
    public static final class Companion
    {
        private Companion() {
        }
        
        @JvmOverloads
        @NotNull
        public final ACache get(@NotNull final String cacheName, final long maxSize, final int maxCount) {
            Intrinsics.checkNotNullParameter((Object)cacheName, "cacheName");
            final File f = new File(ReaderAdapterHelper.INSTANCE.getAdapter().getCacheDir(), cacheName);
            return this.get(f, maxSize, maxCount);
        }
        
        public static /* synthetic */ ACache get$default(final Companion companion, String cacheName, long maxSize, int maxCount, final int n, final Object o) {
            if ((n & 0x1) != 0x0) {
                cacheName = "ACache";
            }
            if ((n & 0x2) != 0x0) {
                maxSize = 50000000L;
            }
            if ((n & 0x4) != 0x0) {
                maxCount = Integer.MAX_VALUE;
            }
            return companion.get(cacheName, maxSize, maxCount);
        }
        
        @JvmOverloads
        @NotNull
        public final ACache get(@NotNull final File cacheDir, final long maxSize, final int maxCount) {
            Intrinsics.checkNotNullParameter((Object)cacheDir, "cacheDir");
            synchronized (this) {
                final int n = 0;
                ACache manager = ACache.access$getMInstanceMap$cp().get(cacheDir.getAbsoluteFile().toString());
                if (manager == null) {
                    manager = new ACache(cacheDir, maxSize, maxCount, null);
                    final Map map = ACache.access$getMInstanceMap$cp();
                    final String absolutePath = cacheDir.getAbsolutePath();
                    Intrinsics.checkNotNullExpressionValue((Object)absolutePath, "cacheDir.absolutePath");
                    map.put(absolutePath, manager);
                }
                return manager;
            }
        }
        
        public static /* synthetic */ ACache get$default(final Companion companion, final File cacheDir, long maxSize, int maxCount, final int n, final Object o) {
            if ((n & 0x2) != 0x0) {
                maxSize = 50000000L;
            }
            if ((n & 0x4) != 0x0) {
                maxCount = Integer.MAX_VALUE;
            }
            return companion.get(cacheDir, maxSize, maxCount);
        }
        
        @JvmOverloads
        @NotNull
        public final ACache get(@NotNull final String cacheName, final long maxSize) {
            Intrinsics.checkNotNullParameter((Object)cacheName, "cacheName");
            return get$default(this, cacheName, maxSize, 0, 4, null);
        }
        
        @JvmOverloads
        @NotNull
        public final ACache get(@NotNull final String cacheName) {
            Intrinsics.checkNotNullParameter((Object)cacheName, "cacheName");
            return get$default(this, cacheName, 0L, 0, 6, null);
        }
        
        @JvmOverloads
        @NotNull
        public final ACache get() {
            return get$default(this, (String)null, 0L, 0, 7, null);
        }
        
        @JvmOverloads
        @NotNull
        public final ACache get(@NotNull final File cacheDir, final long maxSize) {
            Intrinsics.checkNotNullParameter((Object)cacheDir, "cacheDir");
            return get$default(this, cacheDir, maxSize, 0, 4, null);
        }
        
        @JvmOverloads
        @NotNull
        public final ACache get(@NotNull final File cacheDir) {
            Intrinsics.checkNotNullParameter((Object)cacheDir, "cacheDir");
            return get$default(this, cacheDir, 0L, 0, 6, null);
        }
    }
    
    @Metadata(mv = { 1, 5, 1 }, k = 1, xi = 48, d1 = { "\u00008\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\b\u0002\n\u0002\u0010\f\n\u0000\n\u0002\u0010\u0012\n\u0000\n\u0002\u0010\u000e\n\u0002\b\u0004\n\u0002\u0010\b\n\u0002\b\u0004\n\u0002\u0010\u0011\n\u0002\b\u0002\n\u0002\u0010\u000b\n\u0002\b\b\b\u00c2\u0002\u0018\u00002\u00020\u0001B\u0007\b\u0002?\u0006\u0002\u0010\u0002J\u000e\u0010\u0005\u001a\u00020\u00062\u0006\u0010\u0007\u001a\u00020\u0006J\u0012\u0010\u0005\u001a\u0004\u0018\u00010\b2\b\u0010\t\u001a\u0004\u0018\u00010\bJ \u0010\n\u001a\u00020\u00062\u0006\u0010\u000b\u001a\u00020\u00062\u0006\u0010\f\u001a\u00020\r2\u0006\u0010\u000e\u001a\u00020\rH\u0002J\u0010\u0010\u000f\u001a\u00020\b2\u0006\u0010\u0010\u001a\u00020\rH\u0002J\u001b\u0010\u0011\u001a\n\u0012\u0004\u0012\u00020\b\u0018\u00010\u00122\u0006\u0010\u0007\u001a\u00020\u0006?\u0006\u0002\u0010\u0013J\u0010\u0010\u0014\u001a\u00020\u00152\b\u0010\u0007\u001a\u0004\u0018\u00010\u0006J\u0018\u0010\u0016\u001a\u00020\r2\u0006\u0010\u0007\u001a\u00020\u00062\u0006\u0010\u0017\u001a\u00020\u0004H\u0002J\u000e\u0010\u0018\u001a\u00020\u00152\u0006\u0010\u0007\u001a\u00020\u0006J\u000e\u0010\u0018\u001a\u00020\u00152\u0006\u0010\u0019\u001a\u00020\bJ\u0016\u0010\u001a\u001a\u00020\u00062\u0006\u0010\u0010\u001a\u00020\r2\u0006\u0010\u001b\u001a\u00020\u0006J\u0016\u0010\u001c\u001a\u00020\b2\u0006\u0010\u0010\u001a\u00020\r2\u0006\u0010\t\u001a\u00020\bR\u000e\u0010\u0003\u001a\u00020\u0004X\u0082T?\u0006\u0002\n\u0000：\u0006\u001d" }, d2 = { "Lio/legado/app/utils/ACache$Utils;", "", "()V", "mSeparator", "", "clearDateInfo", "", "data", "", "strInfo", "copyOfRange", "original", "from", "", "to", "createDateInfo", "second", "getDateInfoFromDate", "", "([B)[Ljava/lang/String;", "hasDateInfo", "", "indexOf", "c", "isDue", "str", "newByteArrayWithDateInfo", "data2", "newStringWithDateInfo", "reader-pro" })
    private static final class Utils
    {
        @NotNull
        public static final Utils INSTANCE;
        private static final char mSeparator = ' ';
        
        public final boolean isDue(@NotNull final String str) {
            Intrinsics.checkNotNullParameter((Object)str, "str");
            final byte[] bytes = str.getBytes(Charsets.UTF_8);
            Intrinsics.checkNotNullExpressionValue((Object)bytes, "(this as java.lang.String).getBytes(charset)");
            return this.isDue(bytes);
        }
        
        public final boolean isDue(@NotNull final byte[] data) {
            Intrinsics.checkNotNullParameter((Object)data, "data");
            try {
                final String[] text = this.getDateInfoFromDate(data);
                if (text != null && text.length == 2) {
                    String saveTimeStr;
                    String substring;
                    for (saveTimeStr = text[0]; StringsKt.startsWith$default(saveTimeStr, "0", false, 2, (Object)null); saveTimeStr = substring) {
                        final String s = saveTimeStr;
                        final int beginIndex = 1;
                        final String s2 = s;
                        if (s2 == null) {
                            throw new NullPointerException("null cannot be cast to non-null type java.lang.String");
                        }
                        substring = s2.substring(beginIndex);
                        Intrinsics.checkNotNullExpressionValue((Object)substring, "(this as java.lang.String).substring(startIndex)");
                    }
                    final Long saveTime = Long.valueOf(saveTimeStr);
                    final Long deleteAfter = Long.valueOf(text[1]);
                    if (System.currentTimeMillis() > saveTime + deleteAfter * 1000) {
                        return true;
                    }
                }
            }
            catch (final Exception e) {
                e.printStackTrace();
            }
            return false;
        }
        
        @NotNull
        public final String newStringWithDateInfo(final int second, @NotNull final String strInfo) {
            Intrinsics.checkNotNullParameter((Object)strInfo, "strInfo");
            return Intrinsics.stringPlus(this.createDateInfo(second), (Object)strInfo);
        }
        
        @NotNull
        public final byte[] newByteArrayWithDateInfo(final int second, @NotNull final byte[] data2) {
            Intrinsics.checkNotNullParameter((Object)data2, "data2");
            final String dateInfo = this.createDateInfo(second);
            final Charset utf_8 = Charsets.UTF_8;
            final String s = dateInfo;
            if (s == null) {
                throw new NullPointerException("null cannot be cast to non-null type java.lang.String");
            }
            final byte[] bytes = s.getBytes(utf_8);
            Intrinsics.checkNotNullExpressionValue((Object)bytes, "(this as java.lang.String).getBytes(charset)");
            final byte[] data3 = bytes;
            final byte[] retData = new byte[data3.length + data2.length];
            System.arraycopy(data3, 0, retData, 0, data3.length);
            System.arraycopy(data2, 0, retData, data3.length, data2.length);
            return retData;
        }
        
        @Nullable
        public final String clearDateInfo(@Nullable final String strInfo) {
            if (strInfo != null) {
                final String it = strInfo;
                final int n = 0;
                final Utils instance = Utils.INSTANCE;
                final Charset utf_8 = Charsets.UTF_8;
                if (strInfo == null) {
                    throw new NullPointerException("null cannot be cast to non-null type java.lang.String");
                }
                final byte[] bytes = strInfo.getBytes(utf_8);
                Intrinsics.checkNotNullExpressionValue((Object)bytes, "(this as java.lang.String).getBytes(charset)");
                if (instance.hasDateInfo(bytes)) {
                    final int beginIndex = StringsKt.indexOf$default((CharSequence)strInfo, ' ', 0, false, 6, (Object)null) + 1;
                    if (strInfo == null) {
                        throw new NullPointerException("null cannot be cast to non-null type java.lang.String");
                    }
                    final String substring = strInfo.substring(beginIndex);
                    Intrinsics.checkNotNullExpressionValue((Object)substring, "(this as java.lang.String).substring(startIndex)");
                    return substring;
                }
            }
            return strInfo;
        }
        
        @NotNull
        public final byte[] clearDateInfo(@NotNull final byte[] data) {
            Intrinsics.checkNotNullParameter((Object)data, "data");
            return this.hasDateInfo(data) ? this.copyOfRange(data, this.indexOf(data, ' ') + 1, data.length) : data;
        }
        
        public final boolean hasDateInfo(@Nullable final byte[] data) {
            return data != null && data.length > 15 && data[13] == (byte)45 && this.indexOf(data, ' ') > 14;
        }
        
        @Nullable
        public final String[] getDateInfoFromDate(@NotNull final byte[] data) {
            Intrinsics.checkNotNullParameter((Object)data, "data");
            if (this.hasDateInfo(data)) {
                final String saveDate = new String(this.copyOfRange(data, 0, 13), Charsets.UTF_8);
                final String deleteAfter = new String(this.copyOfRange(data, 14, this.indexOf(data, ' ')), Charsets.UTF_8);
                return new String[] { saveDate, deleteAfter };
            }
            return null;
        }
        
        private final int indexOf(final byte[] data, final char c) {
            int j = 0;
            final int n = data.length - 1;
            if (j <= n) {
                do {
                    final int i = j;
                    ++j;
                    if (data[i] == (byte)c) {
                        return i;
                    }
                } while (j <= n);
            }
            return -1;
        }
        
        private final byte[] copyOfRange(final byte[] original, final int from, final int to) {
            final int newLength = to - from;
            if (newLength < 0) {
                final int n = 0;
                throw new IllegalArgumentException((from + " > " + to).toString());
            }
            final byte[] copy = new byte[newLength];
            System.arraycopy(original, from, copy, 0, Math.min(original.length - from, newLength));
            return copy;
        }
        
        private final String createDateInfo(final int second) {
            final StringBuilder currentTime = new StringBuilder(System.currentTimeMillis() + "");
            while (currentTime.length() < 13) {
                currentTime.insert(0, "0");
            }
            return new StringBuilder().append((Object)currentTime).append('-').append(second).append(' ').toString();
        }
        
        static {
            INSTANCE = new Utils();
        }
    }
    
    @Metadata(mv = { 1, 5, 1 }, k = 1, xi = 48, d1 = { "\u0000N\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\t\n\u0000\n\u0002\u0010\b\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010%\n\u0000\n\u0002\u0010$\n\u0000\n\u0002\u0010\u0002\n\u0002\b\u0005\n\u0002\u0010\u000e\n\u0002\b\u0005\n\u0002\u0010\u000b\n\u0002\b\u0002\b\u0096\u0004\u0018\u00002\u00020\u0001B\u001d\u0012\u0006\u0010\u0002\u001a\u00020\u0003\u0012\u0006\u0010\u0004\u001a\u00020\u0005\u0012\u0006\u0010\u0006\u001a\u00020\u0007?\u0006\u0002\u0010\bJ\b\u0010\u0011\u001a\u00020\u0012H\u0002J\u0010\u0010\u0013\u001a\u00020\u00052\u0006\u0010\u0014\u001a\u00020\u0003H\u0002J\u0006\u0010\u0015\u001a\u00020\u0012J\u0011\u0010\u0016\u001a\u00020\u00032\u0006\u0010\u0017\u001a\u00020\u0018H\u0086\u0002J\u000e\u0010\u0019\u001a\u00020\u00032\u0006\u0010\u0017\u001a\u00020\u0018J\u000e\u0010\u001a\u001a\u00020\u00032\u0006\u0010\u001b\u001a\u00020\u0018J\u000e\u0010\u001c\u001a\u00020\u00122\u0006\u0010\u0014\u001a\u00020\u0003J\u000e\u0010\u001d\u001a\u00020\u001e2\u0006\u0010\u0017\u001a\u00020\u0018J\b\u0010\u001f\u001a\u00020\u0005H\u0002R\u000e\u0010\t\u001a\u00020\nX\u0082\u0004?\u0006\u0002\n\u0000R\u000e\u0010\u0002\u001a\u00020\u0003X\u0082\u000e?\u0006\u0002\n\u0000R\u000e\u0010\u000b\u001a\u00020\fX\u0082\u0004?\u0006\u0002\n\u0000R\u000e\u0010\u0006\u001a\u00020\u0007X\u0082\u0004?\u0006\u0002\n\u0000RN\u0010\r\u001aB\u0012\f\u0012\n \u000f*\u0004\u0018\u00010\u00030\u0003\u0012\f\u0012\n \u000f*\u0004\u0018\u00010\u00050\u0005 \u000f* \u0012\f\u0012\n \u000f*\u0004\u0018\u00010\u00030\u0003\u0012\f\u0012\n \u000f*\u0004\u0018\u00010\u00050\u0005\u0018\u00010\u00100\u000eX\u0082\u0004?\u0006\u0002\n\u0000R\u000e\u0010\u0004\u001a\u00020\u0005X\u0082\u0004?\u0006\u0002\n\u0000：\u0006 " }, d2 = { "Lio/legado/app/utils/ACache$ACacheManager;", "", "cacheDir", "Ljava/io/File;", "sizeLimit", "", "countLimit", "", "(Lio/legado/app/utils/ACache;Ljava/io/File;JI)V", "cacheCount", "Ljava/util/concurrent/atomic/AtomicInteger;", "cacheSize", "Ljava/util/concurrent/atomic/AtomicLong;", "lastUsageDates", "", "kotlin.jvm.PlatformType", "", "calculateCacheSizeAndCacheCount", "", "calculateSize", "file", "clear", "get", "key", "", "newFile", "newFileFromHashCode", "hashCode", "put", "remove", "", "removeNext", "reader-pro" })
    public class ACacheManager
    {
        @NotNull
        private File cacheDir;
        private final long sizeLimit;
        private final int countLimit;
        @NotNull
        private final AtomicLong cacheSize;
        @NotNull
        private final AtomicInteger cacheCount;
        private final Map<File, Long> lastUsageDates;
        
        public ACacheManager(final File cacheDir, final long sizeLimit, final int countLimit) {
            Intrinsics.checkNotNullParameter((Object)ACache.this, "this$0");
            Intrinsics.checkNotNullParameter((Object)cacheDir, "cacheDir");
            this.cacheDir = cacheDir;
            this.sizeLimit = sizeLimit;
            this.countLimit = countLimit;
            this.cacheSize = new AtomicLong();
            this.cacheCount = new AtomicInteger();
            this.lastUsageDates = Collections.synchronizedMap(new HashMap<File, Long>());
            this.calculateCacheSizeAndCacheCount();
        }
        
        private final void calculateCacheSizeAndCacheCount() {
            new Thread(ACacheManager::calculateCacheSizeAndCacheCount$lambda-0).start();
        }
        
        public final void put(@NotNull final File file) {
            Intrinsics.checkNotNullParameter((Object)file, "file");
            try {
                for (int curCacheCount = this.cacheCount.get(); curCacheCount + 1 > this.countLimit; curCacheCount = this.cacheCount.addAndGet(-1)) {
                    final long freedSize = this.removeNext();
                    this.cacheSize.addAndGet(-freedSize);
                }
                this.cacheCount.addAndGet(1);
                final long valueSize = this.calculateSize(file);
                long freedSize2;
                for (long curCacheSize = this.cacheSize.get(); curCacheSize + valueSize > this.sizeLimit; curCacheSize = this.cacheSize.addAndGet(-freedSize2)) {
                    freedSize2 = this.removeNext();
                }
                this.cacheSize.addAndGet(valueSize);
                final long currentTime = System.currentTimeMillis();
                file.setLastModified(currentTime);
                final Map<File, Long> lastUsageDates = this.lastUsageDates;
                Intrinsics.checkNotNullExpressionValue((Object)lastUsageDates, "lastUsageDates");
                lastUsageDates.put(file, currentTime);
            }
            catch (final Exception e) {
                e.printStackTrace();
            }
        }
        
        @NotNull
        public final File get(@NotNull final String key) {
            Intrinsics.checkNotNullParameter((Object)key, "key");
            final File file = this.newFile(key);
            final long currentTime = System.currentTimeMillis();
            file.setLastModified(currentTime);
            final Map<File, Long> lastUsageDates = this.lastUsageDates;
            Intrinsics.checkNotNullExpressionValue((Object)lastUsageDates, "lastUsageDates");
            lastUsageDates.put(file, currentTime);
            return file;
        }
        
        @NotNull
        public final File newFile(@NotNull final String key) {
            Intrinsics.checkNotNullParameter((Object)key, "key");
            return new File(this.cacheDir, key.hashCode() + "");
        }
        
        @NotNull
        public final File newFileFromHashCode(@NotNull final String hashCode) {
            Intrinsics.checkNotNullParameter((Object)hashCode, "hashCode");
            return new File(this.cacheDir, hashCode);
        }
        
        public final boolean remove(@NotNull final String key) {
            Intrinsics.checkNotNullParameter((Object)key, "key");
            final File image = this.get(key);
            return image.delete();
        }
        
        public final void clear() {
            try {
                this.lastUsageDates.clear();
                this.cacheSize.set(0L);
                final File[] files = this.cacheDir.listFiles();
                if (files != null) {
                    final File[] array = files;
                    int i = 0;
                    while (i < array.length) {
                        final File f = array[i];
                        ++i;
                        f.delete();
                    }
                }
            }
            catch (final Exception e) {
                e.printStackTrace();
            }
        }
        
        private final long removeNext() {
            try {
                if (this.lastUsageDates.isEmpty()) {
                    return 0L;
                }
                Object oldestUsage = null;
                Object mostLongUsedFile = null;
                final Set entries = this.lastUsageDates.entrySet();
                final Map<File, Long> lastUsageDates = this.lastUsageDates;
                Intrinsics.checkNotNullExpressionValue((Object)lastUsageDates, "lastUsageDates");
                final Map<File, Long> map = lastUsageDates;
                synchronized (map) {
                    final int n = 0;
                    for (final Map.Entry<File, Long> entry : entries) {
                        final File key = entry.getKey();
                        final Long lastValueUsage = entry.getValue();
                        if (mostLongUsedFile == null) {
                            mostLongUsedFile = key;
                            oldestUsage = lastValueUsage;
                        }
                        else {
                            Intrinsics.checkNotNullExpressionValue((Object)lastValueUsage, "lastValueUsage");
                            final long longValue = lastValueUsage;
                            final Object o = oldestUsage;
                            Intrinsics.checkNotNull(o);
                            if (longValue >= ((Number)o).longValue()) {
                                continue;
                            }
                            oldestUsage = lastValueUsage;
                            mostLongUsedFile = key;
                        }
                    }
                    final Unit instance = Unit.INSTANCE;
                }
                long fileSize = 0L;
                if (mostLongUsedFile != null) {
                    fileSize = this.calculateSize((File)mostLongUsedFile);
                    if (((File)mostLongUsedFile).delete()) {
                        this.lastUsageDates.remove(mostLongUsedFile);
                    }
                }
                return fileSize;
            }
            catch (final Exception e) {
                e.printStackTrace();
                return 0L;
            }
        }
        
        private final long calculateSize(final File file) {
            return file.length();
        }
        
        private static final void calculateCacheSizeAndCacheCount$lambda-0(final ACacheManager this$0) {
            Intrinsics.checkNotNullParameter((Object)this$0, "this$0");
            try {
                int size = 0;
                int count = 0;
                final File[] cachedFiles = this$0.cacheDir.listFiles();
                if (cachedFiles != null) {
                    final File[] array = cachedFiles;
                    int i = 0;
                    while (i < array.length) {
                        final File cachedFile = array[i];
                        ++i;
                        final int n = size;
                        Intrinsics.checkNotNullExpressionValue((Object)cachedFile, "cachedFile");
                        size = n + (int)this$0.calculateSize(cachedFile);
                        ++count;
                        final Map<File, Long> lastUsageDates = this$0.lastUsageDates;
                        Intrinsics.checkNotNullExpressionValue((Object)lastUsageDates, "lastUsageDates");
                        lastUsageDates.put(cachedFile, cachedFile.lastModified());
                    }
                    this$0.cacheSize.set(size);
                    this$0.cacheCount.set(count);
                }
            }
            catch (final Exception e) {
                e.printStackTrace();
            }
        }
    }
}
