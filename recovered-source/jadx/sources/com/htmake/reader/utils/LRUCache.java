package com.htmake.reader.utils;

import io.legado.app.constant.Action;
import java.util.Arrays;
import java.util.concurrent.ConcurrentHashMap;
import kotlin.Metadata;
import kotlin.jvm.internal.Intrinsics;
import kotlin.jvm.internal.StringCompanionObject;
import kotlin.jvm.internal.TypeIntrinsics;
import me.ag2s.epublib.epub.PackageDocumentBase;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/* JADX INFO: compiled from: LRUCache.kt */
/* JADX INFO: loaded from: app-classes.jar:com/htmake/reader/utils/LRUCache.class */
@Metadata(mv = {1, 5, 1}, k = 1, xi = 48, d1 = {"\u00002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\u0000\n\u0000\n\u0002\u0010\b\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0010\u0002\n\u0002\b\f\n\u0002\u0010\u000e\n\u0002\b\u0002\u0018\u0000*\u0004\b\u0000\u0010\u0001*\u0004\b\u0001\u0010\u00022\u00020\u0003:\u0001\u001cB\u000f\b\u0016\u0012\u0006\u0010\u0004\u001a\u00020\u0005¢\u0006\u0002\u0010\u0006J\u0006\u0010\r\u001a\u00020\u000eJ\u0015\u0010\u000f\u001a\u0004\u0018\u00018\u00012\u0006\u0010\u0010\u001a\u00028\u0000¢\u0006\u0002\u0010\u0011J \u0010\u0012\u001a\u00020\u000e2\u0016\u0010\u0013\u001a\u00120\nR\u000e\u0012\u0004\u0012\u00028\u0000\u0012\u0004\u0012\u00028\u00010\u0000H\u0002J\u001b\u0010\u0014\u001a\u00020\u000e2\u0006\u0010\u0010\u001a\u00028\u00002\u0006\u0010\u0015\u001a\u00028\u0001¢\u0006\u0002\u0010\u0016J%\u0010\u0017\u001a\u0014\u0018\u00010\nR\u000e\u0012\u0004\u0012\u00028\u0000\u0012\u0004\u0012\u00028\u00010\u00002\u0006\u0010\u0010\u001a\u00028\u0000¢\u0006\u0002\u0010\u0018J\b\u0010\u0019\u001a\u00020\u000eH\u0002J\b\u0010\u001a\u001a\u00020\u001bH\u0016R\u000e\u0010\u0007\u001a\u00020\u0005X\u0082\u000e¢\u0006\u0002\n\u0000R*\u0010\b\u001a\u001e\u0012\u0004\u0012\u00028\u0000\u0012\u0014\u0012\u00120\nR\u000e\u0012\u0004\u0012\u00028\u0000\u0012\u0004\u0012\u00028\u00010\u00000\tX\u0082\u000e¢\u0006\u0002\n\u0000R \u0010\u000b\u001a\u0014\u0018\u00010\nR\u000e\u0012\u0004\u0012\u00028\u0000\u0012\u0004\u0012\u00028\u00010\u0000X\u0082\u000e¢\u0006\u0002\n\u0000R \u0010\f\u001a\u0014\u0018\u00010\nR\u000e\u0012\u0004\u0012\u00028\u0000\u0012\u0004\u0012\u00028\u00010\u0000X\u0082\u000e¢\u0006\u0002\n\u0000¨\u0006\u001d"}, d2 = {"Lcom/htmake/reader/utils/LRUCache;", "K", "V", PackageDocumentBase.PREFIX_OPF, "size", PackageDocumentBase.PREFIX_OPF, "(I)V", "cacheCapacity", "caches", "Ljava/util/concurrent/ConcurrentHashMap;", "Lcom/htmake/reader/utils/LRUCache$CacheNode;", "first", "last", "clear", PackageDocumentBase.PREFIX_OPF, "get", "k", "(Ljava/lang/Object;)Ljava/lang/Object;", "moveToFirst", "node", "put", "v", "(Ljava/lang/Object;Ljava/lang/Object;)V", "remove", "(Ljava/lang/Object;)Lcom/htmake/reader/utils/LRUCache$CacheNode;", "removeLast", "toString", PackageDocumentBase.PREFIX_OPF, "CacheNode", "reader-pro"})
public final class LRUCache<K, V> {
    private int cacheCapacity;

    @NotNull
    private ConcurrentHashMap<K, LRUCache<K, V>.CacheNode> caches;

    @Nullable
    private LRUCache<K, V>.CacheNode first;

    @Nullable
    private LRUCache<K, V>.CacheNode last;

    public LRUCache(int size) {
        this.cacheCapacity = size;
        this.caches = new ConcurrentHashMap<>(size);
    }

    public final void put(K k, V v) {
        LRUCache<K, V>.CacheNode cacheNode = this.caches.get(k);
        if (cacheNode == null) {
            if (this.caches.size() >= this.cacheCapacity) {
                ConcurrentHashMap<K, LRUCache<K, V>.CacheNode> concurrentHashMap = this.caches;
                LRUCache<K, V>.CacheNode cacheNode2 = this.last;
                K key = cacheNode2 == null ? null : cacheNode2.getKey();
                if (concurrentHashMap == null) {
                    throw new NullPointerException("null cannot be cast to non-null type kotlin.collections.MutableMap<K, V>");
                }
                TypeIntrinsics.asMutableMap(concurrentHashMap).remove(key);
                removeLast();
            }
            cacheNode = new CacheNode(this);
            cacheNode.setKey(k);
        }
        cacheNode.setValue(v);
        moveToFirst(cacheNode);
        this.caches.put(k, cacheNode);
    }

    @Nullable
    public final V get(K k) {
        LRUCache<K, V>.CacheNode cacheNode = this.caches.get(k);
        if (cacheNode == null) {
            return null;
        }
        moveToFirst(cacheNode);
        return cacheNode.getValue();
    }

    @Nullable
    public final LRUCache<K, V>.CacheNode remove(K k) {
        LRUCache<K, V>.CacheNode next;
        LRUCache<K, V>.CacheNode pre;
        LRUCache<K, V>.CacheNode cacheNode = this.caches.get(k);
        if (cacheNode != null) {
            if (cacheNode.getPre() != null && (pre = cacheNode.getPre()) != null) {
                pre.setNext(cacheNode.getNext());
            }
            if (cacheNode.getNext() != null && (next = cacheNode.getNext()) != null) {
                next.setPre(cacheNode.getPre());
            }
            if (Intrinsics.areEqual(cacheNode, this.first)) {
                this.first = cacheNode.getNext();
            }
            if (Intrinsics.areEqual(cacheNode, this.last)) {
                this.last = cacheNode.getPre();
            }
        }
        return this.caches.remove(k);
    }

    private final void moveToFirst(LRUCache<K, V>.CacheNode node) {
        LRUCache<K, V>.CacheNode pre;
        LRUCache<K, V>.CacheNode next;
        if (Intrinsics.areEqual(this.first, node)) {
            return;
        }
        if (node.getNext() != null && (next = node.getNext()) != null) {
            next.setPre(node.getPre());
        }
        if (node.getPre() != null && (pre = node.getPre()) != null) {
            pre.setNext(node.getNext());
        }
        if (Intrinsics.areEqual(node, this.last)) {
            LRUCache<K, V>.CacheNode cacheNode = this.last;
            this.last = cacheNode == null ? null : cacheNode.getPre();
        }
        if (this.first == null || this.last == null) {
            this.first = node;
            this.last = node;
            return;
        }
        node.setNext(this.first);
        LRUCache<K, V>.CacheNode cacheNode2 = this.first;
        if (cacheNode2 != null) {
            cacheNode2.setPre(node);
        }
        this.first = node;
        LRUCache<K, V>.CacheNode cacheNode3 = this.first;
        if (cacheNode3 == null) {
            return;
        }
        cacheNode3.setPre(null);
    }

    private final void removeLast() {
        if (this.last != null) {
            LRUCache<K, V>.CacheNode cacheNode = this.last;
            this.last = cacheNode == null ? null : cacheNode.getPre();
            if (this.last == null) {
                this.first = null;
                return;
            }
            LRUCache<K, V>.CacheNode cacheNode2 = this.last;
            if (cacheNode2 == null) {
                return;
            }
            cacheNode2.setNext(null);
        }
    }

    public final void clear() {
        this.first = null;
        this.last = null;
        this.caches.clear();
    }

    /* JADX INFO: compiled from: LRUCache.kt */
    /* JADX INFO: loaded from: app-classes.jar:com/htmake/reader/utils/LRUCache$CacheNode.class */
    @Metadata(mv = {1, 5, 1}, k = 1, xi = 48, d1 = {"\u0000\u0014\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\b\b\n\u0002\u0018\u0002\n\u0002\b\u000b\b\u0086\u0004\u0018\u00002\u00020\u0001B\u0005¢\u0006\u0002\u0010\u0002R\u001e\u0010\u0003\u001a\u0004\u0018\u00018\u0000X\u0086\u000e¢\u0006\u0010\n\u0002\u0010\b\u001a\u0004\b\u0004\u0010\u0005\"\u0004\b\u0006\u0010\u0007R,\u0010\t\u001a\u0014\u0018\u00010\u0000R\u000e\u0012\u0004\u0012\u00028\u0000\u0012\u0004\u0012\u00028\u00010\nX\u0086\u000e¢\u0006\u000e\n\u0000\u001a\u0004\b\u000b\u0010\f\"\u0004\b\r\u0010\u000eR,\u0010\u000f\u001a\u0014\u0018\u00010\u0000R\u000e\u0012\u0004\u0012\u00028\u0000\u0012\u0004\u0012\u00028\u00010\nX\u0086\u000e¢\u0006\u000e\n\u0000\u001a\u0004\b\u0010\u0010\f\"\u0004\b\u0011\u0010\u000eR\u001e\u0010\u0012\u001a\u0004\u0018\u00018\u0001X\u0086\u000e¢\u0006\u0010\n\u0002\u0010\b\u001a\u0004\b\u0013\u0010\u0005\"\u0004\b\u0014\u0010\u0007¨\u0006\u0015"}, d2 = {"Lcom/htmake/reader/utils/LRUCache$CacheNode;", PackageDocumentBase.PREFIX_OPF, "(Lcom/htmake/reader/utils/LRUCache;)V", "key", "getKey", "()Ljava/lang/Object;", "setKey", "(Ljava/lang/Object;)V", "Ljava/lang/Object;", Action.next, "Lcom/htmake/reader/utils/LRUCache;", "getNext", "()Lcom/htmake/reader/utils/LRUCache$CacheNode;", "setNext", "(Lcom/htmake/reader/utils/LRUCache$CacheNode;)V", "pre", "getPre", "setPre", "value", "getValue", "setValue", "reader-pro"})
    public final class CacheNode {

        @Nullable
        private LRUCache<K, V>.CacheNode pre;

        @Nullable
        private LRUCache<K, V>.CacheNode next;

        @Nullable
        private K key;

        @Nullable
        private V value;
        final /* synthetic */ LRUCache<K, V> this$0;

        public CacheNode(LRUCache this$0) {
            Intrinsics.checkNotNullParameter(this$0, "this$0");
            this.this$0 = this$0;
        }

        @Nullable
        public final LRUCache<K, V>.CacheNode getPre() {
            return this.pre;
        }

        public final void setPre(@Nullable LRUCache<K, V>.CacheNode cacheNode) {
            this.pre = cacheNode;
        }

        @Nullable
        public final LRUCache<K, V>.CacheNode getNext() {
            return this.next;
        }

        public final void setNext(@Nullable LRUCache<K, V>.CacheNode cacheNode) {
            this.next = cacheNode;
        }

        @Nullable
        public final K getKey() {
            return this.key;
        }

        public final void setKey(@Nullable K k) {
            this.key = k;
        }

        @Nullable
        public final V getValue() {
            return this.value;
        }

        public final void setValue(@Nullable V v) {
            this.value = v;
        }
    }

    @NotNull
    public String toString() {
        StringBuilder sb = new StringBuilder();
        LRUCache<K, V>.CacheNode next = this.first;
        while (true) {
            LRUCache<K, V>.CacheNode cacheNode = next;
            if (cacheNode != null) {
                StringCompanionObject stringCompanionObject = StringCompanionObject.INSTANCE;
                Object[] objArr = {cacheNode.getKey(), cacheNode.getValue()};
                String str = String.format("%s:%s ", Arrays.copyOf(objArr, objArr.length));
                Intrinsics.checkNotNullExpressionValue(str, "java.lang.String.format(format, *args)");
                sb.append(str);
                next = cacheNode.getNext();
            } else {
                String string = sb.toString();
                Intrinsics.checkNotNullExpressionValue(string, "sb.toString()");
                return string;
            }
        }
    }
}
