package io.legado.app.utils;

import io.legado.app.adapters.ReaderAdapterHelper;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.nio.charset.Charset;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;
import kotlin.Metadata;
import kotlin.Unit;
import kotlin.io.CloseableKt;
import kotlin.io.FilesKt;
import kotlin.jvm.JvmOverloads;
import kotlin.jvm.internal.DefaultConstructorMarker;
import kotlin.jvm.internal.Intrinsics;
import kotlin.text.Charsets;
import kotlin.text.StringsKt;
import me.ag2s.epublib.browsersupport.NavigationHistory;
import me.ag2s.epublib.epub.PackageDocumentBase;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/* JADX INFO: compiled from: ACache.kt */
/* JADX INFO: loaded from: app-classes.jar:io/legado/app/utils/ACache.class */
@Metadata(mv = {1, 5, 1}, k = 1, xi = 48, d1 = {"\u0000J\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\t\n\u0000\n\u0002\u0010\b\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u0002\n\u0002\b\u0002\n\u0002\u0010\u000e\n\u0000\n\u0002\u0010\u0012\n\u0002\b\u0006\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\u000b\n\u0002\b\u0004\u0018\u0000 \u001d2\u00020\u0001:\u0003\u001c\u001d\u001eB\u001f\b\u0002\u0012\u0006\u0010\u0002\u001a\u00020\u0003\u0012\u0006\u0010\u0004\u001a\u00020\u0005\u0012\u0006\u0010\u0006\u001a\u00020\u0007¢\u0006\u0002\u0010\bJ\u0006\u0010\u000b\u001a\u00020\fJ\u0010\u0010\r\u001a\u0004\u0018\u00010\u00032\u0006\u0010\u000e\u001a\u00020\u000fJ\u0010\u0010\u0010\u001a\u0004\u0018\u00010\u00112\u0006\u0010\u000e\u001a\u00020\u000fJ\u0010\u0010\u0012\u001a\u0004\u0018\u00010\u00012\u0006\u0010\u000e\u001a\u00020\u000fJ\u0010\u0010\u0013\u001a\u0004\u0018\u00010\u000f2\u0006\u0010\u000e\u001a\u00020\u000fJ\u0010\u0010\u0014\u001a\u0004\u0018\u00010\u000f2\u0006\u0010\u0015\u001a\u00020\u000fJ\"\u0010\u0016\u001a\u00020\f2\u0006\u0010\u000e\u001a\u00020\u000f2\u0006\u0010\u0017\u001a\u00020\u00182\b\b\u0002\u0010\u0019\u001a\u00020\u0007H\u0007J\u0016\u0010\u0016\u001a\u00020\f2\u0006\u0010\u000e\u001a\u00020\u000f2\u0006\u0010\u0017\u001a\u00020\u0011J\u001e\u0010\u0016\u001a\u00020\f2\u0006\u0010\u000e\u001a\u00020\u000f2\u0006\u0010\u0017\u001a\u00020\u00112\u0006\u0010\u0019\u001a\u00020\u0007J\u0016\u0010\u0016\u001a\u00020\f2\u0006\u0010\u000e\u001a\u00020\u000f2\u0006\u0010\u0017\u001a\u00020\u000fJ\u001e\u0010\u0016\u001a\u00020\f2\u0006\u0010\u000e\u001a\u00020\u000f2\u0006\u0010\u0017\u001a\u00020\u000f2\u0006\u0010\u0019\u001a\u00020\u0007J\u000e\u0010\u001a\u001a\u00020\u001b2\u0006\u0010\u000e\u001a\u00020\u000fR\u0014\u0010\t\u001a\b\u0018\u00010\nR\u00020\u0000X\u0082\u000e¢\u0006\u0002\n\u0000¨\u0006\u001f"}, d2 = {"Lio/legado/app/utils/ACache;", PackageDocumentBase.PREFIX_OPF, "cacheDir", "Ljava/io/File;", "max_size", PackageDocumentBase.PREFIX_OPF, "max_count", PackageDocumentBase.PREFIX_OPF, "(Ljava/io/File;JI)V", "mCache", "Lio/legado/app/utils/ACache$ACacheManager;", "clear", PackageDocumentBase.PREFIX_OPF, "file", "key", PackageDocumentBase.PREFIX_OPF, "getAsBinary", PackageDocumentBase.PREFIX_OPF, "getAsObject", "getAsString", "getByHashCode", "hashCode", "put", "value", "Ljava/io/Serializable;", "saveTime", "remove", PackageDocumentBase.PREFIX_OPF, "ACacheManager", "Companion", "Utils", "reader-pro"})
public final class ACache {

    @Nullable
    private ACacheManager mCache;
    public static final int TIME_HOUR = 3600;
    public static final int TIME_DAY = 86400;
    private static final int MAX_SIZE = 50000000;
    private static final int MAX_COUNT = Integer.MAX_VALUE;

    /* JADX INFO: renamed from: Companion, reason: from kotlin metadata */
    @NotNull
    public static final Companion INSTANCE = new Companion(null);

    @NotNull
    private static final HashMap<String, ACache> mInstanceMap = new HashMap<>();

    @JvmOverloads
    public final void put(@NotNull String key, @NotNull Serializable value) {
        Intrinsics.checkNotNullParameter(key, "key");
        Intrinsics.checkNotNullParameter(value, "value");
        put$default(this, key, value, 0, 4, null);
    }

    public /* synthetic */ ACache(File cacheDir, long max_size, int max_count, DefaultConstructorMarker $constructor_marker) {
        this(cacheDir, max_size, max_count);
    }

    private ACache(File cacheDir, long max_size, int max_count) {
        try {
            if (!cacheDir.exists() && !cacheDir.mkdirs()) {
                ACacheKt.logger.info(Intrinsics.stringPlus("ACache can't make dirs in %s", cacheDir.getAbsolutePath()));
            }
            this.mCache = new ACacheManager(this, cacheDir, max_size, max_count);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /* JADX INFO: compiled from: ACache.kt */
    /* JADX INFO: loaded from: app-classes.jar:io/legado/app/utils/ACache$Companion.class */
    @Metadata(mv = {1, 5, 1}, k = 1, xi = 48, d1 = {"\u00002\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\b\u0002\n\u0002\u0010\b\n\u0002\b\u0004\n\u0002\u0018\u0002\n\u0002\u0010\u000e\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\t\n\u0002\b\u0003\b\u0086\u0003\u0018\u00002\u00020\u0001B\u0007\b\u0002¢\u0006\u0002\u0010\u0002J$\u0010\f\u001a\u00020\u000b2\u0006\u0010\r\u001a\u00020\u000e2\b\b\u0002\u0010\u000f\u001a\u00020\u00102\b\b\u0002\u0010\u0011\u001a\u00020\u0004H\u0007J&\u0010\f\u001a\u00020\u000b2\b\b\u0002\u0010\u0012\u001a\u00020\n2\b\b\u0002\u0010\u000f\u001a\u00020\u00102\b\b\u0002\u0010\u0011\u001a\u00020\u0004H\u0007R\u000e\u0010\u0003\u001a\u00020\u0004X\u0082T¢\u0006\u0002\n\u0000R\u000e\u0010\u0005\u001a\u00020\u0004X\u0082T¢\u0006\u0002\n\u0000R\u000e\u0010\u0006\u001a\u00020\u0004X\u0086T¢\u0006\u0002\n\u0000R\u000e\u0010\u0007\u001a\u00020\u0004X\u0086T¢\u0006\u0002\n\u0000R\u001a\u0010\b\u001a\u000e\u0012\u0004\u0012\u00020\n\u0012\u0004\u0012\u00020\u000b0\tX\u0082\u0004¢\u0006\u0002\n\u0000¨\u0006\u0013"}, d2 = {"Lio/legado/app/utils/ACache$Companion;", PackageDocumentBase.PREFIX_OPF, "()V", "MAX_COUNT", PackageDocumentBase.PREFIX_OPF, "MAX_SIZE", "TIME_DAY", "TIME_HOUR", "mInstanceMap", "Ljava/util/HashMap;", PackageDocumentBase.PREFIX_OPF, "Lio/legado/app/utils/ACache;", "get", "cacheDir", "Ljava/io/File;", "maxSize", PackageDocumentBase.PREFIX_OPF, "maxCount", "cacheName", "reader-pro"})
    public static final class Companion {
        @JvmOverloads
        @NotNull
        public final ACache get(@NotNull String cacheName, long maxSize) {
            Intrinsics.checkNotNullParameter(cacheName, "cacheName");
            return get$default(this, cacheName, maxSize, 0, 4, (Object) null);
        }

        @JvmOverloads
        @NotNull
        public final ACache get(@NotNull String cacheName) {
            Intrinsics.checkNotNullParameter(cacheName, "cacheName");
            return get$default(this, cacheName, 0L, 0, 6, (Object) null);
        }

        @JvmOverloads
        @NotNull
        public final ACache get() {
            return get$default(this, (String) null, 0L, 0, 7, (Object) null);
        }

        @JvmOverloads
        @NotNull
        public final ACache get(@NotNull File cacheDir, long maxSize) {
            Intrinsics.checkNotNullParameter(cacheDir, "cacheDir");
            return get$default(this, cacheDir, maxSize, 0, 4, (Object) null);
        }

        @JvmOverloads
        @NotNull
        public final ACache get(@NotNull File cacheDir) {
            Intrinsics.checkNotNullParameter(cacheDir, "cacheDir");
            return get$default(this, cacheDir, 0L, 0, 6, (Object) null);
        }

        public /* synthetic */ Companion(DefaultConstructorMarker $constructor_marker) {
            this();
        }

        private Companion() {
        }

        public static /* synthetic */ ACache get$default(Companion companion, String str, long j, int i, int i2, Object obj) {
            if ((i2 & 1) != 0) {
                str = "ACache";
            }
            if ((i2 & 2) != 0) {
                j = 50000000;
            }
            if ((i2 & 4) != 0) {
                i = ACache.MAX_COUNT;
            }
            return companion.get(str, j, i);
        }

        @JvmOverloads
        @NotNull
        public final ACache get(@NotNull String cacheName, long maxSize, int maxCount) {
            Intrinsics.checkNotNullParameter(cacheName, "cacheName");
            File f = new File(ReaderAdapterHelper.INSTANCE.getAdapter().getCacheDir(), cacheName);
            return get(f, maxSize, maxCount);
        }

        public static /* synthetic */ ACache get$default(Companion companion, File file, long j, int i, int i2, Object obj) {
            if ((i2 & 2) != 0) {
                j = 50000000;
            }
            if ((i2 & 4) != 0) {
                i = ACache.MAX_COUNT;
            }
            return companion.get(file, j, i);
        }

        @JvmOverloads
        @NotNull
        public final ACache get(@NotNull File cacheDir, long maxSize, int maxCount) {
            ACache aCache;
            Intrinsics.checkNotNullParameter(cacheDir, "cacheDir");
            synchronized (this) {
                ACache manager = (ACache) ACache.mInstanceMap.get(cacheDir.getAbsoluteFile().toString());
                if (manager == null) {
                    manager = new ACache(cacheDir, maxSize, maxCount, null);
                    HashMap map = ACache.mInstanceMap;
                    String absolutePath = cacheDir.getAbsolutePath();
                    Intrinsics.checkNotNullExpressionValue(absolutePath, "cacheDir.absolutePath");
                    map.put(absolutePath, manager);
                }
                aCache = manager;
            }
            return aCache;
        }
    }

    public final void put(@NotNull String key, @NotNull String value) {
        Intrinsics.checkNotNullParameter(key, "key");
        Intrinsics.checkNotNullParameter(value, "value");
        ACacheManager mCache = this.mCache;
        if (mCache != null) {
            try {
                File file = mCache.newFile(key);
                FilesKt.writeText$default(file, value, (Charset) null, 2, (Object) null);
                mCache.put(file);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public final void put(@NotNull String key, @NotNull String value, int saveTime) {
        Intrinsics.checkNotNullParameter(key, "key");
        Intrinsics.checkNotNullParameter(value, "value");
        if (saveTime <= 0) {
            put(key, value);
        } else {
            put(key, Utils.INSTANCE.newStringWithDateInfo(saveTime, value));
        }
    }

    @Nullable
    public final String getAsString(@NotNull String key) {
        Intrinsics.checkNotNullParameter(key, "key");
        ACacheManager mCache = this.mCache;
        if (mCache == null) {
            return null;
        }
        File file = mCache.get(key);
        if (!file.exists()) {
            return null;
        }
        try {
            try {
                String text = FilesKt.readText$default(file, (Charset) null, 1, (Object) null);
                if (!Utils.INSTANCE.isDue(text)) {
                    return Utils.INSTANCE.clearDateInfo(text);
                }
                remove(key);
                return null;
            } catch (IOException e) {
                e.printStackTrace();
                return null;
            }
        } catch (Throwable th) {
            throw th;
        }
        throw th;
    }

    @Nullable
    public final String getByHashCode(@NotNull String hashCode) {
        Intrinsics.checkNotNullParameter(hashCode, "hashCode");
        ACacheManager mCache = this.mCache;
        if (mCache == null) {
            return null;
        }
        File file = mCache.newFileFromHashCode(hashCode);
        if (!file.exists()) {
            return null;
        }
        try {
            try {
                String text = FilesKt.readText$default(file, (Charset) null, 1, (Object) null);
                if (!Utils.INSTANCE.isDue(text)) {
                    return Utils.INSTANCE.clearDateInfo(text);
                }
                file.delete();
                return null;
            } catch (IOException e) {
                e.printStackTrace();
                return null;
            }
        } catch (Throwable th) {
            throw th;
        }
        throw th;
    }

    public final void put(@NotNull String key, @NotNull byte[] value) {
        Intrinsics.checkNotNullParameter(key, "key");
        Intrinsics.checkNotNullParameter(value, "value");
        ACacheManager mCache = this.mCache;
        if (mCache != null) {
            File file = mCache.newFile(key);
            FilesKt.writeBytes(file, value);
            mCache.put(file);
        }
    }

    public final void put(@NotNull String key, @NotNull byte[] value, int saveTime) {
        Intrinsics.checkNotNullParameter(key, "key");
        Intrinsics.checkNotNullParameter(value, "value");
        if (saveTime <= 0) {
            put(key, value);
        } else {
            put(key, Utils.INSTANCE.newByteArrayWithDateInfo(saveTime, value));
        }
    }

    @Nullable
    public final byte[] getAsBinary(@NotNull String key) {
        byte[] bArrClearDateInfo;
        Intrinsics.checkNotNullParameter(key, "key");
        ACacheManager mCache = this.mCache;
        if (mCache == null) {
            return null;
        }
        boolean removeFile = false;
        try {
            try {
                File file = mCache.get(key);
                if (!file.exists()) {
                    return null;
                }
                byte[] byteArray = FilesKt.readBytes(file);
                if (Utils.INSTANCE.isDue(byteArray)) {
                    removeFile = true;
                    bArrClearDateInfo = (byte[]) null;
                } else {
                    bArrClearDateInfo = Utils.INSTANCE.clearDateInfo(byteArray);
                }
                byte[] bArr = bArrClearDateInfo;
                if (removeFile) {
                    remove(key);
                }
                return bArr;
            } catch (Exception e) {
                e.printStackTrace();
                if (0 == 0) {
                    return null;
                }
                remove(key);
                return null;
            }
        } catch (Throwable th) {
            if (0 != 0) {
                remove(key);
            }
            throw th;
        }
    }

    public static /* synthetic */ void put$default(ACache aCache, String str, Serializable serializable, int i, int i2, Object obj) {
        if ((i2 & 4) != 0) {
            i = -1;
        }
        aCache.put(str, serializable, i);
    }

    @JvmOverloads
    public final void put(@NotNull String key, @NotNull Serializable value, int saveTime) {
        Intrinsics.checkNotNullParameter(key, "key");
        Intrinsics.checkNotNullParameter(value, "value");
        try {
            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
            ObjectOutputStream objectOutputStream = new ObjectOutputStream(byteArrayOutputStream);
            Throwable th = (Throwable) null;
            try {
                try {
                    ObjectOutputStream oos = objectOutputStream;
                    oos.writeObject(value);
                    byte[] data = byteArrayOutputStream.toByteArray();
                    if (saveTime != -1) {
                        Intrinsics.checkNotNullExpressionValue(data, "data");
                        put(key, data, saveTime);
                    } else {
                        Intrinsics.checkNotNullExpressionValue(data, "data");
                        put(key, data);
                    }
                    Unit unit = Unit.INSTANCE;
                    CloseableKt.closeFinally(objectOutputStream, th);
                } catch (Throwable th2) {
                    th = th2;
                    throw th2;
                }
            } catch (Throwable th3) {
                CloseableKt.closeFinally(objectOutputStream, th);
                throw th3;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Nullable
    public final Object getAsObject(@NotNull String key) {
        Intrinsics.checkNotNullParameter(key, "key");
        byte[] data = getAsBinary(key);
        if (data != null) {
            ByteArrayInputStream bis = null;
            ObjectInputStream ois = null;
            try {
                try {
                    bis = new ByteArrayInputStream(data);
                    ois = new ObjectInputStream(bis);
                    Object object = ois.readObject();
                    try {
                        bis.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    try {
                        ois.close();
                    } catch (IOException e2) {
                        e2.printStackTrace();
                    }
                    return object;
                } catch (Exception e3) {
                    e3.printStackTrace();
                    ByteArrayInputStream byteArrayInputStream = bis;
                    if (byteArrayInputStream != null) {
                        try {
                            byteArrayInputStream.close();
                        } catch (IOException e4) {
                            e4.printStackTrace();
                        }
                    }
                    ObjectInputStream objectInputStream = ois;
                    if (objectInputStream == null) {
                        return null;
                    }
                    try {
                        objectInputStream.close();
                        return null;
                    } catch (IOException e5) {
                        e5.printStackTrace();
                        return null;
                    }
                }
            } catch (Throwable th) {
                ByteArrayInputStream byteArrayInputStream2 = bis;
                if (byteArrayInputStream2 != null) {
                    try {
                        byteArrayInputStream2.close();
                    } catch (IOException e6) {
                        e6.printStackTrace();
                    }
                }
                ObjectInputStream objectInputStream2 = ois;
                if (objectInputStream2 != null) {
                    try {
                        objectInputStream2.close();
                    } catch (IOException e7) {
                        e7.printStackTrace();
                    }
                }
                throw th;
            }
        }
        return null;
    }

    @Nullable
    public final File file(@NotNull String key) {
        Intrinsics.checkNotNullParameter(key, "key");
        ACacheManager mCache = this.mCache;
        if (mCache != null) {
            try {
                File f = mCache.newFile(key);
                if (f.exists()) {
                    return f;
                }
                return null;
            } catch (Exception e) {
                e.printStackTrace();
                return null;
            }
        }
        return null;
    }

    public final boolean remove(@NotNull String key) {
        Intrinsics.checkNotNullParameter(key, "key");
        ACacheManager aCacheManager = this.mCache;
        return aCacheManager != null && aCacheManager.remove(key);
    }

    public final void clear() {
        ACacheManager aCacheManager = this.mCache;
        if (aCacheManager == null) {
            return;
        }
        aCacheManager.clear();
    }

    /* JADX INFO: compiled from: ACache.kt */
    /* JADX INFO: loaded from: app-classes.jar:io/legado/app/utils/ACache$Utils.class */
    @Metadata(mv = {1, 5, 1}, k = 1, xi = 48, d1 = {"\u00008\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\b\u0002\n\u0002\u0010\f\n\u0000\n\u0002\u0010\u0012\n\u0000\n\u0002\u0010\u000e\n\u0002\b\u0004\n\u0002\u0010\b\n\u0002\b\u0004\n\u0002\u0010\u0011\n\u0002\b\u0002\n\u0002\u0010\u000b\n\u0002\b\b\bÂ\u0002\u0018\u00002\u00020\u0001B\u0007\b\u0002¢\u0006\u0002\u0010\u0002J\u000e\u0010\u0005\u001a\u00020\u00062\u0006\u0010\u0007\u001a\u00020\u0006J\u0012\u0010\u0005\u001a\u0004\u0018\u00010\b2\b\u0010\t\u001a\u0004\u0018\u00010\bJ \u0010\n\u001a\u00020\u00062\u0006\u0010\u000b\u001a\u00020\u00062\u0006\u0010\f\u001a\u00020\r2\u0006\u0010\u000e\u001a\u00020\rH\u0002J\u0010\u0010\u000f\u001a\u00020\b2\u0006\u0010\u0010\u001a\u00020\rH\u0002J\u001b\u0010\u0011\u001a\n\u0012\u0004\u0012\u00020\b\u0018\u00010\u00122\u0006\u0010\u0007\u001a\u00020\u0006¢\u0006\u0002\u0010\u0013J\u0010\u0010\u0014\u001a\u00020\u00152\b\u0010\u0007\u001a\u0004\u0018\u00010\u0006J\u0018\u0010\u0016\u001a\u00020\r2\u0006\u0010\u0007\u001a\u00020\u00062\u0006\u0010\u0017\u001a\u00020\u0004H\u0002J\u000e\u0010\u0018\u001a\u00020\u00152\u0006\u0010\u0007\u001a\u00020\u0006J\u000e\u0010\u0018\u001a\u00020\u00152\u0006\u0010\u0019\u001a\u00020\bJ\u0016\u0010\u001a\u001a\u00020\u00062\u0006\u0010\u0010\u001a\u00020\r2\u0006\u0010\u001b\u001a\u00020\u0006J\u0016\u0010\u001c\u001a\u00020\b2\u0006\u0010\u0010\u001a\u00020\r2\u0006\u0010\t\u001a\u00020\bR\u000e\u0010\u0003\u001a\u00020\u0004X\u0082T¢\u0006\u0002\n\u0000¨\u0006\u001d"}, d2 = {"Lio/legado/app/utils/ACache$Utils;", PackageDocumentBase.PREFIX_OPF, "()V", "mSeparator", PackageDocumentBase.PREFIX_OPF, "clearDateInfo", PackageDocumentBase.PREFIX_OPF, "data", PackageDocumentBase.PREFIX_OPF, "strInfo", "copyOfRange", "original", "from", PackageDocumentBase.PREFIX_OPF, "to", "createDateInfo", "second", "getDateInfoFromDate", PackageDocumentBase.PREFIX_OPF, "([B)[Ljava/lang/String;", "hasDateInfo", PackageDocumentBase.PREFIX_OPF, "indexOf", "c", "isDue", "str", "newByteArrayWithDateInfo", "data2", "newStringWithDateInfo", "reader-pro"})
    private static final class Utils {

        @NotNull
        public static final Utils INSTANCE = new Utils();
        private static final char mSeparator = ' ';

        private Utils() {
        }

        public final boolean isDue(@NotNull String str) {
            Intrinsics.checkNotNullParameter(str, "str");
            byte[] bytes = str.getBytes(Charsets.UTF_8);
            Intrinsics.checkNotNullExpressionValue(bytes, "(this as java.lang.String).getBytes(charset)");
            return isDue(bytes);
        }

        public final boolean isDue(@NotNull byte[] data) {
            Intrinsics.checkNotNullParameter(data, "data");
            try {
                String[] text = getDateInfoFromDate(data);
                if (text != null && text.length == 2) {
                    String saveTimeStr = text[0];
                    while (StringsKt.startsWith$default(saveTimeStr, "0", false, 2, (Object) null)) {
                        String str = saveTimeStr;
                        if (str == null) {
                            throw new NullPointerException("null cannot be cast to non-null type java.lang.String");
                        }
                        String strSubstring = str.substring(1);
                        Intrinsics.checkNotNullExpressionValue(strSubstring, "(this as java.lang.String).substring(startIndex)");
                        saveTimeStr = strSubstring;
                    }
                    Long saveTime = Long.valueOf(saveTimeStr);
                    Long deleteAfter = Long.valueOf(text[1]);
                    if (System.currentTimeMillis() > saveTime.longValue() + (deleteAfter.longValue() * ((long) NavigationHistory.DEFAULT_MAX_HISTORY_SIZE))) {
                        return true;
                    }
                    return false;
                }
                return false;
            } catch (Exception e) {
                e.printStackTrace();
                return false;
            }
        }

        @NotNull
        public final String newStringWithDateInfo(int second, @NotNull String strInfo) {
            Intrinsics.checkNotNullParameter(strInfo, "strInfo");
            return Intrinsics.stringPlus(createDateInfo(second), strInfo);
        }

        @NotNull
        public final byte[] newByteArrayWithDateInfo(int second, @NotNull byte[] data2) {
            Intrinsics.checkNotNullParameter(data2, "data2");
            String strCreateDateInfo = createDateInfo(second);
            Charset charset = Charsets.UTF_8;
            if (strCreateDateInfo == null) {
                throw new NullPointerException("null cannot be cast to non-null type java.lang.String");
            }
            byte[] data1 = strCreateDateInfo.getBytes(charset);
            Intrinsics.checkNotNullExpressionValue(data1, "(this as java.lang.String).getBytes(charset)");
            byte[] retData = new byte[data1.length + data2.length];
            System.arraycopy(data1, 0, retData, 0, data1.length);
            System.arraycopy(data2, 0, retData, data1.length, data2.length);
            return retData;
        }

        @Nullable
        public final String clearDateInfo(@Nullable String strInfo) {
            if (strInfo != null) {
                Utils utils = INSTANCE;
                Charset charset = Charsets.UTF_8;
                if (strInfo == null) {
                    throw new NullPointerException("null cannot be cast to non-null type java.lang.String");
                }
                byte[] bytes = strInfo.getBytes(charset);
                Intrinsics.checkNotNullExpressionValue(bytes, "(this as java.lang.String).getBytes(charset)");
                if (utils.hasDateInfo(bytes)) {
                    int iIndexOf$default = StringsKt.indexOf$default(strInfo, ' ', 0, false, 6, (Object) null) + 1;
                    if (strInfo == null) {
                        throw new NullPointerException("null cannot be cast to non-null type java.lang.String");
                    }
                    String strSubstring = strInfo.substring(iIndexOf$default);
                    Intrinsics.checkNotNullExpressionValue(strSubstring, "(this as java.lang.String).substring(startIndex)");
                    return strSubstring;
                }
            }
            return strInfo;
        }

        @NotNull
        public final byte[] clearDateInfo(@NotNull byte[] data) {
            Intrinsics.checkNotNullParameter(data, "data");
            if (hasDateInfo(data)) {
                return copyOfRange(data, indexOf(data, ' ') + 1, data.length);
            }
            return data;
        }

        public final boolean hasDateInfo(@Nullable byte[] data) {
            return data != null && data.length > 15 && data[13] == ((byte) 45) && indexOf(data, ' ') > 14;
        }

        @Nullable
        public final String[] getDateInfoFromDate(@NotNull byte[] data) {
            Intrinsics.checkNotNullParameter(data, "data");
            if (hasDateInfo(data)) {
                String saveDate = new String(copyOfRange(data, 0, 13), Charsets.UTF_8);
                String deleteAfter = new String(copyOfRange(data, 14, indexOf(data, ' ')), Charsets.UTF_8);
                return new String[]{saveDate, deleteAfter};
            }
            return null;
        }

        private final int indexOf(byte[] data, char c) {
            int i = 0;
            int length = data.length - 1;
            if (0 <= length) {
                do {
                    int i2 = i;
                    i++;
                    if (data[i2] == ((byte) c)) {
                        return i2;
                    }
                } while (i <= length);
                return -1;
            }
            return -1;
        }

        private final byte[] copyOfRange(byte[] original, int from, int to) {
            int newLength = to - from;
            if (!(newLength >= 0)) {
                throw new IllegalArgumentException((from + " > " + to).toString());
            }
            byte[] copy = new byte[newLength];
            System.arraycopy(original, from, copy, 0, Math.min(original.length - from, newLength));
            return copy;
        }

        private final String createDateInfo(int second) {
            StringBuilder currentTime = new StringBuilder(System.currentTimeMillis() + PackageDocumentBase.PREFIX_OPF);
            while (currentTime.length() < 13) {
                currentTime.insert(0, "0");
            }
            return new StringBuilder().append((Object) currentTime).append('-').append(second).append(' ').toString();
        }
    }

    /* JADX INFO: compiled from: ACache.kt */
    /* JADX INFO: loaded from: app-classes.jar:io/legado/app/utils/ACache$ACacheManager.class */
    @Metadata(mv = {1, 5, 1}, k = 1, xi = 48, d1 = {"\u0000N\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\t\n\u0000\n\u0002\u0010\b\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010%\n\u0000\n\u0002\u0010$\n\u0000\n\u0002\u0010\u0002\n\u0002\b\u0005\n\u0002\u0010\u000e\n\u0002\b\u0005\n\u0002\u0010\u000b\n\u0002\b\u0002\b\u0096\u0004\u0018\u00002\u00020\u0001B\u001d\u0012\u0006\u0010\u0002\u001a\u00020\u0003\u0012\u0006\u0010\u0004\u001a\u00020\u0005\u0012\u0006\u0010\u0006\u001a\u00020\u0007¢\u0006\u0002\u0010\bJ\b\u0010\u0011\u001a\u00020\u0012H\u0002J\u0010\u0010\u0013\u001a\u00020\u00052\u0006\u0010\u0014\u001a\u00020\u0003H\u0002J\u0006\u0010\u0015\u001a\u00020\u0012J\u0011\u0010\u0016\u001a\u00020\u00032\u0006\u0010\u0017\u001a\u00020\u0018H\u0086\u0002J\u000e\u0010\u0019\u001a\u00020\u00032\u0006\u0010\u0017\u001a\u00020\u0018J\u000e\u0010\u001a\u001a\u00020\u00032\u0006\u0010\u001b\u001a\u00020\u0018J\u000e\u0010\u001c\u001a\u00020\u00122\u0006\u0010\u0014\u001a\u00020\u0003J\u000e\u0010\u001d\u001a\u00020\u001e2\u0006\u0010\u0017\u001a\u00020\u0018J\b\u0010\u001f\u001a\u00020\u0005H\u0002R\u000e\u0010\t\u001a\u00020\nX\u0082\u0004¢\u0006\u0002\n\u0000R\u000e\u0010\u0002\u001a\u00020\u0003X\u0082\u000e¢\u0006\u0002\n\u0000R\u000e\u0010\u000b\u001a\u00020\fX\u0082\u0004¢\u0006\u0002\n\u0000R\u000e\u0010\u0006\u001a\u00020\u0007X\u0082\u0004¢\u0006\u0002\n\u0000RN\u0010\r\u001aB\u0012\f\u0012\n \u000f*\u0004\u0018\u00010\u00030\u0003\u0012\f\u0012\n \u000f*\u0004\u0018\u00010\u00050\u0005 \u000f* \u0012\f\u0012\n \u000f*\u0004\u0018\u00010\u00030\u0003\u0012\f\u0012\n \u000f*\u0004\u0018\u00010\u00050\u0005\u0018\u00010\u00100\u000eX\u0082\u0004¢\u0006\u0002\n\u0000R\u000e\u0010\u0004\u001a\u00020\u0005X\u0082\u0004¢\u0006\u0002\n\u0000¨\u0006 "}, d2 = {"Lio/legado/app/utils/ACache$ACacheManager;", PackageDocumentBase.PREFIX_OPF, "cacheDir", "Ljava/io/File;", "sizeLimit", PackageDocumentBase.PREFIX_OPF, "countLimit", PackageDocumentBase.PREFIX_OPF, "(Lio/legado/app/utils/ACache;Ljava/io/File;JI)V", "cacheCount", "Ljava/util/concurrent/atomic/AtomicInteger;", "cacheSize", "Ljava/util/concurrent/atomic/AtomicLong;", "lastUsageDates", PackageDocumentBase.PREFIX_OPF, "kotlin.jvm.PlatformType", PackageDocumentBase.PREFIX_OPF, "calculateCacheSizeAndCacheCount", PackageDocumentBase.PREFIX_OPF, "calculateSize", "file", "clear", "get", "key", PackageDocumentBase.PREFIX_OPF, "newFile", "newFileFromHashCode", "hashCode", "put", "remove", PackageDocumentBase.PREFIX_OPF, "removeNext", "reader-pro"})
    public class ACacheManager {

        @NotNull
        private File cacheDir;
        private final long sizeLimit;
        private final int countLimit;

        @NotNull
        private final AtomicLong cacheSize;

        @NotNull
        private final AtomicInteger cacheCount;
        private final Map<File, Long> lastUsageDates;
        final /* synthetic */ ACache this$0;

        public ACacheManager(@NotNull ACache this$0, File cacheDir, long sizeLimit, int countLimit) {
            Intrinsics.checkNotNullParameter(this$0, "this$0");
            Intrinsics.checkNotNullParameter(cacheDir, "cacheDir");
            this.this$0 = this$0;
            this.cacheDir = cacheDir;
            this.sizeLimit = sizeLimit;
            this.countLimit = countLimit;
            this.cacheSize = new AtomicLong();
            this.cacheCount = new AtomicInteger();
            this.lastUsageDates = Collections.synchronizedMap(new HashMap());
            calculateCacheSizeAndCacheCount();
        }

        private final void calculateCacheSizeAndCacheCount() {
            new Thread(() -> {
                m274calculateCacheSizeAndCacheCount$lambda0(r2);
            }).start();
        }

        /* JADX INFO: renamed from: calculateCacheSizeAndCacheCount$lambda-0, reason: not valid java name */
        private static final void m274calculateCacheSizeAndCacheCount$lambda0(ACacheManager this$0) {
            Intrinsics.checkNotNullParameter(this$0, "this$0");
            try {
                int size = 0;
                int count = 0;
                File[] cachedFiles = this$0.cacheDir.listFiles();
                if (cachedFiles != null) {
                    int i = 0;
                    int length = cachedFiles.length;
                    while (i < length) {
                        File cachedFile = cachedFiles[i];
                        i++;
                        Intrinsics.checkNotNullExpressionValue(cachedFile, "cachedFile");
                        size += (int) this$0.calculateSize(cachedFile);
                        count++;
                        Map<File, Long> map = this$0.lastUsageDates;
                        Intrinsics.checkNotNullExpressionValue(map, "lastUsageDates");
                        map.put(cachedFile, Long.valueOf(cachedFile.lastModified()));
                    }
                    this$0.cacheSize.set(size);
                    this$0.cacheCount.set(count);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        public final void put(@NotNull File file) {
            Intrinsics.checkNotNullParameter(file, "file");
            try {
                int curCacheCount = this.cacheCount.get();
                while (curCacheCount + 1 > this.countLimit) {
                    long freedSize = removeNext();
                    this.cacheSize.addAndGet(-freedSize);
                    curCacheCount = this.cacheCount.addAndGet(-1);
                }
                this.cacheCount.addAndGet(1);
                long valueSize = calculateSize(file);
                long curCacheSize = this.cacheSize.get();
                while (curCacheSize + valueSize > this.sizeLimit) {
                    long freedSize2 = removeNext();
                    curCacheSize = this.cacheSize.addAndGet(-freedSize2);
                }
                this.cacheSize.addAndGet(valueSize);
                long currentTime = System.currentTimeMillis();
                file.setLastModified(currentTime);
                Map<File, Long> map = this.lastUsageDates;
                Intrinsics.checkNotNullExpressionValue(map, "lastUsageDates");
                map.put(file, Long.valueOf(currentTime));
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        @NotNull
        public final File get(@NotNull String key) {
            Intrinsics.checkNotNullParameter(key, "key");
            File file = newFile(key);
            long currentTime = System.currentTimeMillis();
            file.setLastModified(currentTime);
            Map<File, Long> map = this.lastUsageDates;
            Intrinsics.checkNotNullExpressionValue(map, "lastUsageDates");
            map.put(file, Long.valueOf(currentTime));
            return file;
        }

        @NotNull
        public final File newFile(@NotNull String key) {
            Intrinsics.checkNotNullParameter(key, "key");
            return new File(this.cacheDir, key.hashCode() + PackageDocumentBase.PREFIX_OPF);
        }

        @NotNull
        public final File newFileFromHashCode(@NotNull String hashCode) {
            Intrinsics.checkNotNullParameter(hashCode, "hashCode");
            return new File(this.cacheDir, hashCode);
        }

        public final boolean remove(@NotNull String key) {
            Intrinsics.checkNotNullParameter(key, "key");
            File image = get(key);
            return image.delete();
        }

        public final void clear() {
            try {
                this.lastUsageDates.clear();
                this.cacheSize.set(0L);
                File[] files = this.cacheDir.listFiles();
                if (files != null) {
                    int i = 0;
                    int length = files.length;
                    while (i < length) {
                        File f = files[i];
                        i++;
                        f.delete();
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        private final long removeNext() {
            try {
                if (this.lastUsageDates.isEmpty()) {
                    return 0L;
                }
                Object oldestUsage = null;
                File file = null;
                Set<Map.Entry<File, Long>> setEntrySet = this.lastUsageDates.entrySet();
                Map<File, Long> map = this.lastUsageDates;
                Intrinsics.checkNotNullExpressionValue(map, "lastUsageDates");
                synchronized (map) {
                    for (Map.Entry<File, Long> entry : setEntrySet) {
                        File key = entry.getKey();
                        Long lastValueUsage = entry.getValue();
                        if (file == null) {
                            file = key;
                            oldestUsage = lastValueUsage;
                        } else {
                            Intrinsics.checkNotNullExpressionValue(lastValueUsage, "lastValueUsage");
                            long jLongValue = lastValueUsage.longValue();
                            Object obj = oldestUsage;
                            Intrinsics.checkNotNull(obj);
                            if (jLongValue < ((Number) obj).longValue()) {
                                oldestUsage = lastValueUsage;
                                file = key;
                            }
                        }
                    }
                    Unit unit = Unit.INSTANCE;
                }
                long fileSize = 0;
                if (file != null) {
                    fileSize = calculateSize(file);
                    if (file.delete()) {
                        this.lastUsageDates.remove(file);
                    }
                }
                return fileSize;
            } catch (Exception e) {
                e.printStackTrace();
                return 0L;
            }
        }

        private final long calculateSize(File file) {
            return file.length();
        }
    }
}
