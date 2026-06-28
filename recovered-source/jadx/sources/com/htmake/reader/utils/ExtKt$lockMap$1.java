package com.htmake.reader.utils;

import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.locks.ReadWriteLock;
import kotlin.Metadata;
import me.ag2s.epublib.epub.PackageDocumentBase;
import org.jetbrains.annotations.Nullable;

/* JADX INFO: compiled from: Ext.kt */
/* JADX INFO: loaded from: reader-pro-classes-3.2.14.jar:com/htmake/reader/utils/ExtKt$lockMap$1.class */
@Metadata(mv = {1, 5, 1}, k = 1, xi = 48, d1 = {"\u0000\u001f\n\u0000\n\u0002\u0018\u0002\n\u0002\u0010\u000e\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u000b\n\u0000\n\u0002\u0010'\n\u0000*\u0001\u0000\b\n\u0018\u00002\u000e\u0012\u0004\u0012\u00020\u0002\u0012\u0004\u0012\u00020\u00030\u0001J\u001e\u0010\u0004\u001a\u00020\u00052\u0014\u0010\u0006\u001a\u0010\u0012\u0004\u0012\u00020\u0002\u0012\u0004\u0012\u00020\u0003\u0018\u00010\u0007H\u0014¨\u0006\b"}, d2 = {"com/htmake/reader/utils/ExtKt$lockMap$1", "Ljava/util/LinkedHashMap;", PackageDocumentBase.PREFIX_OPF, "Ljava/util/concurrent/locks/ReadWriteLock;", "removeEldestEntry", PackageDocumentBase.PREFIX_OPF, "eldest", PackageDocumentBase.PREFIX_OPF, "reader-pro"})
public final class ExtKt$lockMap$1 extends LinkedHashMap<String, ReadWriteLock> {
    ExtKt$lockMap$1() {
        super(16, 0.75f, true);
    }

    public /* bridge */ boolean containsKey(String key) {
        return super.containsKey((Object) key);
    }

    @Override // java.util.HashMap, java.util.AbstractMap, java.util.Map
    public final /* bridge */ boolean containsKey(Object key) {
        if (key instanceof String) {
            return containsKey((String) key);
        }
        return false;
    }

    public /* bridge */ boolean containsValue(ReadWriteLock value) {
        return super.containsValue((Object) value);
    }

    @Override // java.util.LinkedHashMap, java.util.HashMap, java.util.AbstractMap, java.util.Map
    public final /* bridge */ boolean containsValue(Object value) {
        if (value instanceof ReadWriteLock) {
            return containsValue((ReadWriteLock) value);
        }
        return false;
    }

    public /* bridge */ ReadWriteLock get(String key) {
        return (ReadWriteLock) super.get((Object) key);
    }

    @Override // java.util.LinkedHashMap, java.util.HashMap, java.util.AbstractMap, java.util.Map
    public final /* bridge */ ReadWriteLock get(Object key) {
        if (key instanceof String) {
            return get((String) key);
        }
        return null;
    }

    public /* bridge */ ReadWriteLock getOrDefault(String key, ReadWriteLock defaultValue) {
        return (ReadWriteLock) super.getOrDefault((Object) key, defaultValue);
    }

    public final /* bridge */ ReadWriteLock getOrDefault(Object key, ReadWriteLock defaultValue) {
        return !(key instanceof String) ? defaultValue : getOrDefault((String) key, defaultValue);
    }

    public /* bridge */ ReadWriteLock remove(String key) {
        return (ReadWriteLock) super.remove((Object) key);
    }

    @Override // java.util.HashMap, java.util.AbstractMap, java.util.Map
    public final /* bridge */ ReadWriteLock remove(Object key) {
        if (key instanceof String) {
            return remove((String) key);
        }
        return null;
    }

    public /* bridge */ boolean remove(String key, ReadWriteLock value) {
        return super.remove((Object) key, (Object) value);
    }

    @Override // java.util.HashMap, java.util.Map
    public final /* bridge */ boolean remove(Object key, Object value) {
        if ((key instanceof String) && (value instanceof ReadWriteLock)) {
            return remove((String) key, (ReadWriteLock) value);
        }
        return false;
    }

    public /* bridge */ Set<Map.Entry<String, ReadWriteLock>> getEntries() {
        return super.entrySet();
    }

    @Override // java.util.LinkedHashMap, java.util.HashMap, java.util.AbstractMap, java.util.Map
    public final /* bridge */ Set<Map.Entry<String, ReadWriteLock>> entrySet() {
        return getEntries();
    }

    public /* bridge */ Set<String> getKeys() {
        return super.keySet();
    }

    @Override // java.util.LinkedHashMap, java.util.HashMap, java.util.AbstractMap, java.util.Map
    public final /* bridge */ Set<String> keySet() {
        return getKeys();
    }

    public /* bridge */ int getSize() {
        return super.size();
    }

    @Override // java.util.HashMap, java.util.AbstractMap, java.util.Map
    public final /* bridge */ int size() {
        return getSize();
    }

    public /* bridge */ Collection<ReadWriteLock> getValues() {
        return super.values();
    }

    @Override // java.util.LinkedHashMap, java.util.HashMap, java.util.AbstractMap, java.util.Map
    public final /* bridge */ Collection<ReadWriteLock> values() {
        return getValues();
    }

    @Override // java.util.LinkedHashMap
    protected boolean removeEldestEntry(@Nullable Map.Entry<String, ReadWriteLock> eldest) {
        return size() > 1000;
    }
}
