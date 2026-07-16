// 
// Decompiled by Procyon v0.6.0
// 

package com.htmake.reader.utils;

import java.util.Arrays;
import kotlin.jvm.internal.StringCompanionObject;
import kotlin.jvm.internal.Intrinsics;
import kotlin.jvm.internal.TypeIntrinsics;
import java.util.Map;
import org.jetbrains.annotations.Nullable;
import org.jetbrains.annotations.NotNull;
import java.util.concurrent.ConcurrentHashMap;
import kotlin.Metadata;

@Metadata(mv = { 1, 5, 1 }, k = 1, xi = 48, d1 = { "\u00002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\u0000\n\u0000\n\u0002\u0010\b\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0010\u0002\n\u0002\b\f\n\u0002\u0010\u000e\n\u0002\b\u0002\u0018\u0000*\u0004\b\u0000\u0010\u0001*\u0004\b\u0001\u0010\u00022\u00020\u0003:\u0001\u001cB\u000f\b\u0016\u0012\u0006\u0010\u0004\u001a\u00020\u0005?\u0006\u0002\u0010\u0006J\u0006\u0010\r\u001a\u00020\u000eJ\u0015\u0010\u000f\u001a\u0004\u0018\u00018\u00012\u0006\u0010\u0010\u001a\u00028\u0000?\u0006\u0002\u0010\u0011J \u0010\u0012\u001a\u00020\u000e2\u0016\u0010\u0013\u001a\u00120\nR\u000e\u0012\u0004\u0012\u00028\u0000\u0012\u0004\u0012\u00028\u00010\u0000H\u0002J\u001b\u0010\u0014\u001a\u00020\u000e2\u0006\u0010\u0010\u001a\u00028\u00002\u0006\u0010\u0015\u001a\u00028\u0001?\u0006\u0002\u0010\u0016J%\u0010\u0017\u001a\u0014\u0018\u00010\nR\u000e\u0012\u0004\u0012\u00028\u0000\u0012\u0004\u0012\u00028\u00010\u00002\u0006\u0010\u0010\u001a\u00028\u0000?\u0006\u0002\u0010\u0018J\b\u0010\u0019\u001a\u00020\u000eH\u0002J\b\u0010\u001a\u001a\u00020\u001bH\u0016R\u000e\u0010\u0007\u001a\u00020\u0005X\u0082\u000e?\u0006\u0002\n\u0000R*\u0010\b\u001a\u001e\u0012\u0004\u0012\u00028\u0000\u0012\u0014\u0012\u00120\nR\u000e\u0012\u0004\u0012\u00028\u0000\u0012\u0004\u0012\u00028\u00010\u00000\tX\u0082\u000e?\u0006\u0002\n\u0000R \u0010\u000b\u001a\u0014\u0018\u00010\nR\u000e\u0012\u0004\u0012\u00028\u0000\u0012\u0004\u0012\u00028\u00010\u0000X\u0082\u000e?\u0006\u0002\n\u0000R \u0010\f\u001a\u0014\u0018\u00010\nR\u000e\u0012\u0004\u0012\u00028\u0000\u0012\u0004\u0012\u00028\u00010\u0000X\u0082\u000e?\u0006\u0002\n\u0000¡§\u0006\u001d" }, d2 = { "Lcom/htmake/reader/utils/LRUCache;", "K", "V", "", "size", "", "(I)V", "cacheCapacity", "caches", "Ljava/util/concurrent/ConcurrentHashMap;", "Lcom/htmake/reader/utils/LRUCache$CacheNode;", "first", "last", "clear", "", "get", "k", "(Ljava/lang/Object;)Ljava/lang/Object;", "moveToFirst", "node", "put", "v", "(Ljava/lang/Object;Ljava/lang/Object;)V", "remove", "(Ljava/lang/Object;)Lcom/htmake/reader/utils/LRUCache$CacheNode;", "removeLast", "toString", "", "CacheNode", "reader-pro" })
public final class LRUCache<K, V>
{
    private int cacheCapacity;
    @NotNull
    private ConcurrentHashMap<K, CacheNode> caches;
    @Nullable
    private CacheNode first;
    @Nullable
    private CacheNode last;
    
    public LRUCache(final int size) {
        this.cacheCapacity = size;
        this.caches = new ConcurrentHashMap<K, CacheNode>(size);
    }
    
    public final void put(final K k, final V v) {
        CacheNode node = this.caches.get(k);
        if (node == null) {
            if (this.caches.size() >= this.cacheCapacity) {
                final Map map = this.caches;
                final CacheNode last = this.last;
                final K i = (last == null) ? null : last.getKey();
                final Map map2 = map;
                if (map2 == null) {
                    throw new NullPointerException("null cannot be cast to non-null type kotlin.collections.MutableMap<K, V>");
                }
                TypeIntrinsics.asMutableMap((Object)map2).remove(i);
                this.removeLast();
            }
            node = new CacheNode();
            node.setKey(k);
        }
        node.setValue(v);
        this.moveToFirst(node);
        this.caches.put(k, node);
    }
    
    @Nullable
    public final V get(final K k) {
        final CacheNode node = this.caches.get(k);
        V value;
        if (node == null) {
            value = null;
        }
        else {
            this.moveToFirst(node);
            value = node.getValue();
        }
        return value;
    }
    
    @Nullable
    public final CacheNode remove(final K k) {
        final CacheNode node = this.caches.get(k);
        if (node != null) {
            if (node.getPre() != null) {
                final CacheNode pre = node.getPre();
                if (pre != null) {
                    pre.setNext(node.getNext());
                }
            }
            if (node.getNext() != null) {
                final CacheNode next = node.getNext();
                if (next != null) {
                    next.setPre(node.getPre());
                }
            }
            if (Intrinsics.areEqual((Object)node, (Object)this.first)) {
                this.first = node.getNext();
            }
            if (Intrinsics.areEqual((Object)node, (Object)this.last)) {
                this.last = node.getPre();
            }
        }
        return this.caches.remove(k);
    }
    
    private final void moveToFirst(final CacheNode node) {
        if (Intrinsics.areEqual((Object)this.first, (Object)node)) {
            return;
        }
        if (node.getNext() != null) {
            final CacheNode next = node.getNext();
            if (next != null) {
                next.setPre(node.getPre());
            }
        }
        if (node.getPre() != null) {
            final CacheNode pre = node.getPre();
            if (pre != null) {
                pre.setNext(node.getNext());
            }
        }
        if (Intrinsics.areEqual((Object)node, (Object)this.last)) {
            final CacheNode last = this.last;
            this.last = ((last == null) ? null : last.getPre());
        }
        if (this.first == null || this.last == null) {
            this.first = node;
            this.last = node;
            return;
        }
        node.setNext(this.first);
        final CacheNode first = this.first;
        if (first != null) {
            first.setPre(node);
        }
        this.first = node;
        final CacheNode first2 = this.first;
        if (first2 != null) {
            first2.setPre(null);
        }
    }
    
    private final void removeLast() {
        if (this.last != null) {
            final CacheNode last = this.last;
            this.last = ((last == null) ? null : last.getPre());
            if (this.last == null) {
                this.first = null;
            }
            else {
                final CacheNode last2 = this.last;
                if (last2 != null) {
                    last2.setNext(null);
                }
            }
        }
    }
    
    public final void clear() {
        this.first = null;
        this.last = null;
        this.caches.clear();
    }
    
    @NotNull
    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder();
        for (CacheNode node = this.first; node != null; node = node.getNext()) {
            final StringBuilder sb2 = sb;
            final StringCompanionObject instance = StringCompanionObject.INSTANCE;
            final String s = "%s:%s ";
            final Object[] array = { node.getKey(), node.getValue() };
            final String format = s;
            final Object[] original = array;
            final String format2 = String.format(format, Arrays.copyOf(original, original.length));
            Intrinsics.checkNotNullExpressionValue((Object)format2, "java.lang.String.format(format, *args)");
            sb2.append(format2);
        }
        final String string = sb.toString();
        Intrinsics.checkNotNullExpressionValue((Object)string, "sb.toString()");
        return string;
    }
    
    @Metadata(mv = { 1, 5, 1 }, k = 1, xi = 48, d1 = { "\u0000\u0014\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\b\b\n\u0002\u0018\u0002\n\u0002\b\u000b\b\u0086\u0004\u0018\u00002\u00020\u0001B\u0005?\u0006\u0002\u0010\u0002R\u001e\u0010\u0003\u001a\u0004\u0018\u00018\u0000X\u0086\u000e?\u0006\u0010\n\u0002\u0010\b\u001a\u0004\b\u0004\u0010\u0005\"\u0004\b\u0006\u0010\u0007R,\u0010\t\u001a\u0014\u0018\u00010\u0000R\u000e\u0012\u0004\u0012\u00028\u0000\u0012\u0004\u0012\u00028\u00010\nX\u0086\u000e?\u0006\u000e\n\u0000\u001a\u0004\b\u000b\u0010\f\"\u0004\b\r\u0010\u000eR,\u0010\u000f\u001a\u0014\u0018\u00010\u0000R\u000e\u0012\u0004\u0012\u00028\u0000\u0012\u0004\u0012\u00028\u00010\nX\u0086\u000e?\u0006\u000e\n\u0000\u001a\u0004\b\u0010\u0010\f\"\u0004\b\u0011\u0010\u000eR\u001e\u0010\u0012\u001a\u0004\u0018\u00018\u0001X\u0086\u000e?\u0006\u0010\n\u0002\u0010\b\u001a\u0004\b\u0013\u0010\u0005\"\u0004\b\u0014\u0010\u0007¡§\u0006\u0015" }, d2 = { "Lcom/htmake/reader/utils/LRUCache$CacheNode;", "", "(Lcom/htmake/reader/utils/LRUCache;)V", "key", "getKey", "()Ljava/lang/Object;", "setKey", "(Ljava/lang/Object;)V", "Ljava/lang/Object;", "next", "Lcom/htmake/reader/utils/LRUCache;", "getNext", "()Lcom/htmake/reader/utils/LRUCache$CacheNode;", "setNext", "(Lcom/htmake/reader/utils/LRUCache$CacheNode;)V", "pre", "getPre", "setPre", "value", "getValue", "setValue", "reader-pro" })
    public final class CacheNode
    {
        @Nullable
        private CacheNode pre;
        @Nullable
        private CacheNode next;
        @Nullable
        private K key;
        @Nullable
        private V value;
        
        public CacheNode() {
            Intrinsics.checkNotNullParameter((Object)LRUCache.this, "this$0");
        }
        
        @Nullable
        public final CacheNode getPre() {
            return this.pre;
        }
        
        public final void setPre(@Nullable final CacheNode <set-?>) {
            this.pre = <set-?>;
        }
        
        @Nullable
        public final CacheNode getNext() {
            return this.next;
        }
        
        public final void setNext(@Nullable final CacheNode <set-?>) {
            this.next = <set-?>;
        }
        
        @Nullable
        public final K getKey() {
            return this.key;
        }
        
        public final void setKey(@Nullable final K <set-?>) {
            this.key = <set-?>;
        }
        
        @Nullable
        public final V getValue() {
            return this.value;
        }
        
        public final void setValue(@Nullable final V <set-?>) {
            this.value = <set-?>;
        }
    }
}
