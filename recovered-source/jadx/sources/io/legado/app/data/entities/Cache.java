package io.legado.app.data.entities;

import kotlin.Metadata;
import kotlin.jvm.internal.DefaultConstructorMarker;
import kotlin.jvm.internal.Intrinsics;
import me.ag2s.epublib.epub.PackageDocumentBase;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/* JADX INFO: compiled from: Cache.kt */
/* JADX INFO: loaded from: app-classes.jar:io/legado/app/data/entities/Cache.class */
@Metadata(mv = {1, 5, 1}, k = 1, xi = 48, d1 = {"\u0000*\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0000\n\u0002\u0010\u000e\n\u0002\b\u0002\n\u0002\u0010\t\n\u0002\b\u000f\n\u0002\u0010\u000b\n\u0002\b\u0002\n\u0002\u0010\b\n\u0002\b\u0002\b\u0086\b\u0018\u00002\u00020\u0001B%\u0012\b\b\u0002\u0010\u0002\u001a\u00020\u0003\u0012\n\b\u0002\u0010\u0004\u001a\u0004\u0018\u00010\u0003\u0012\b\b\u0002\u0010\u0005\u001a\u00020\u0006¢\u0006\u0002\u0010\u0007J\t\u0010\u0011\u001a\u00020\u0003HÆ\u0003J\u000b\u0010\u0012\u001a\u0004\u0018\u00010\u0003HÆ\u0003J\t\u0010\u0013\u001a\u00020\u0006HÆ\u0003J)\u0010\u0014\u001a\u00020\u00002\b\b\u0002\u0010\u0002\u001a\u00020\u00032\n\b\u0002\u0010\u0004\u001a\u0004\u0018\u00010\u00032\b\b\u0002\u0010\u0005\u001a\u00020\u0006HÆ\u0001J\u0013\u0010\u0015\u001a\u00020\u00162\b\u0010\u0017\u001a\u0004\u0018\u00010\u0001HÖ\u0003J\t\u0010\u0018\u001a\u00020\u0019HÖ\u0001J\t\u0010\u001a\u001a\u00020\u0003HÖ\u0001R\u001a\u0010\u0005\u001a\u00020\u0006X\u0086\u000e¢\u0006\u000e\n\u0000\u001a\u0004\b\b\u0010\t\"\u0004\b\n\u0010\u000bR\u0011\u0010\u0002\u001a\u00020\u0003¢\u0006\b\n\u0000\u001a\u0004\b\f\u0010\rR\u001c\u0010\u0004\u001a\u0004\u0018\u00010\u0003X\u0086\u000e¢\u0006\u000e\n\u0000\u001a\u0004\b\u000e\u0010\r\"\u0004\b\u000f\u0010\u0010¨\u0006\u001b"}, d2 = {"Lio/legado/app/data/entities/Cache;", PackageDocumentBase.PREFIX_OPF, "key", PackageDocumentBase.PREFIX_OPF, "value", "deadline", PackageDocumentBase.PREFIX_OPF, "(Ljava/lang/String;Ljava/lang/String;J)V", "getDeadline", "()J", "setDeadline", "(J)V", "getKey", "()Ljava/lang/String;", "getValue", "setValue", "(Ljava/lang/String;)V", "component1", "component2", "component3", "copy", "equals", PackageDocumentBase.PREFIX_OPF, "other", "hashCode", PackageDocumentBase.PREFIX_OPF, "toString", "reader-pro"})
public final /* data */ class Cache {

    @NotNull
    private final String key;

    @Nullable
    private String value;
    private long deadline;

    @NotNull
    public final String component1() {
        return this.key;
    }

    @Nullable
    public final String component2() {
        return this.value;
    }

    public final long component3() {
        return this.deadline;
    }

    @NotNull
    public final Cache copy(@NotNull String key, @Nullable String value, long deadline) {
        Intrinsics.checkNotNullParameter(key, "key");
        return new Cache(key, value, deadline);
    }

    public static /* synthetic */ Cache copy$default(Cache cache, String str, String str2, long j, int i, Object obj) {
        if ((i & 1) != 0) {
            str = cache.key;
        }
        if ((i & 2) != 0) {
            str2 = cache.value;
        }
        if ((i & 4) != 0) {
            j = cache.deadline;
        }
        return cache.copy(str, str2, j);
    }

    @NotNull
    public String toString() {
        return "Cache(key=" + this.key + ", value=" + ((Object) this.value) + ", deadline=" + this.deadline + ')';
    }

    public int hashCode() {
        int result = this.key.hashCode();
        return (((result * 31) + (this.value == null ? 0 : this.value.hashCode())) * 31) + Long.hashCode(this.deadline);
    }

    public boolean equals(@Nullable Object other) {
        if (this == other) {
            return true;
        }
        if (!(other instanceof Cache)) {
            return false;
        }
        Cache cache = (Cache) other;
        return Intrinsics.areEqual(this.key, cache.key) && Intrinsics.areEqual(this.value, cache.value) && this.deadline == cache.deadline;
    }

    public Cache() {
        this(null, null, 0L, 7, null);
    }

    public Cache(@NotNull String key, @Nullable String value, long deadline) {
        Intrinsics.checkNotNullParameter(key, "key");
        this.key = key;
        this.value = value;
        this.deadline = deadline;
    }

    public /* synthetic */ Cache(String str, String str2, long j, int i, DefaultConstructorMarker defaultConstructorMarker) {
        this((i & 1) != 0 ? PackageDocumentBase.PREFIX_OPF : str, (i & 2) != 0 ? null : str2, (i & 4) != 0 ? 0L : j);
    }

    @NotNull
    public final String getKey() {
        return this.key;
    }

    @Nullable
    public final String getValue() {
        return this.value;
    }

    public final void setValue(@Nullable String str) {
        this.value = str;
    }

    public final long getDeadline() {
        return this.deadline;
    }

    public final void setDeadline(long j) {
        this.deadline = j;
    }
}
