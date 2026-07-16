package com.htmake.reader.utils

import java.util.Arrays
import java.util.concurrent.ConcurrentHashMap
import kotlin.jvm.internal.StringCompanionObject
import kotlin.jvm.internal.TypeIntrinsics

public class LRUCache<K, V> {
   private final var cacheCapacity: Int
   private final var caches: ConcurrentHashMap<Any, com.htmake.reader.utils.LRUCache.CacheNode>
   private final var first: com.htmake.reader.utils.LRUCache.CacheNode?
   private final var last: com.htmake.reader.utils.LRUCache.CacheNode?

   public constructor(size: Int)  {
      this.cacheCapacity = size;
      this.caches = new ConcurrentHashMap<>(size);
   }

   public fun put(k: Any, v: Any) {
      var node: LRUCache.CacheNode = this.caches.get(k);
      if (node == null) {
         if (this.caches.size() >= this.cacheCapacity) {
            val var4: java.util.Map = this.caches;
            val var8: Any = if (this.last == null) null else this.last.getKey();
            if (var4 == null) {
               throw new NullPointerException("null cannot be cast to non-null type kotlin.collections.MutableMap<K, V>");
            }

            TypeIntrinsics.asMutableMap(var4).remove(var8);
            this.removeLast();
         }

         node = new LRUCache.CacheNode(this);
         node.setKey((K)k);
      }

      node.setValue((V)v);
      this.moveToFirst(node);
      this.caches.put((K)k, node);
   }

   public fun get(k: Any): Any? {
      val node: LRUCache.CacheNode = this.caches.get(k);
      val var10000: Any;
      if (node == null) {
         var10000 = null;
      } else {
         this.moveToFirst(node);
         var10000 = node.getValue();
      }

      return (V)var10000;
   }

   public fun remove(k: Any): com.htmake.reader.utils.LRUCache.CacheNode? {
      val node: LRUCache.CacheNode = this.caches.get(k);
      if (node != null) {
         if (node.getPre() != null) {
            val var3: LRUCache.CacheNode = node.getPre();
            if (var3 != null) {
               var3.setNext(node.getNext());
            }
         }

         if (node.getNext() != null) {
            val var4: LRUCache.CacheNode = node.getNext();
            if (var4 != null) {
               var4.setPre(node.getPre());
            }
         }

         if (node == this.first) {
            this.first = node.getNext();
         }

         if (node == this.last) {
            this.last = node.getPre();
         }
      }

      return this.caches.remove(k);
   }

   private fun moveToFirst(node: com.htmake.reader.utils.LRUCache.CacheNode) {
      if (!(this.first == node)) {
         if (node.getNext() != null) {
            val var2: LRUCache.CacheNode = node.getNext();
            if (var2 != null) {
               var2.setPre(node.getPre());
            }
         }

         if (node.getPre() != null) {
            val var3: LRUCache.CacheNode = node.getPre();
            if (var3 != null) {
               var3.setNext(node.getNext());
            }
         }

         if (node == this.last) {
            this.last = if (this.last == null) null else this.last.getPre();
         }

         if (this.first != null && this.last != null) {
            node.setNext(this.first);
            if (this.first != null) {
               this.first.setPre(node);
            }

            this.first = node;
            if (this.first != null) {
               this.first.setPre(null);
            }
         } else {
            this.first = node;
            this.last = node;
         }
      }
   }

   private fun removeLast() {
      if (this.last != null) {
         this.last = if (this.last == null) null else this.last.getPre();
         if (this.last == null) {
            this.first = null;
         } else if (this.last != null) {
            this.last.setNext(null);
         }
      }
   }

   public fun clear() {
      this.first = null;
      this.last = null;
      this.caches.clear();
   }

   public override fun toString(): String {
      val sb: StringBuilder = new StringBuilder();

      for (LRUCache.CacheNode node = this.first; node != null; node = node.getNext()) {
         val var3: StringCompanionObject = StringCompanionObject.INSTANCE;
         val var5: Array<Any> = new Object[]{node.getKey(), node.getValue()};
         val var10001: java.lang.String = java.lang.String.format("%s:%s ", Arrays.copyOf(var5, var5.length));
         sb.append(var10001);
      }

      val var7: java.lang.String = sb.toString();
      return var7;
   }

   public inner class CacheNode {
      public final var key: Any?
         internal set

      public final var next: com.htmake.reader.utils.LRUCache.CacheNode?
         internal set

      public final var pre: com.htmake.reader.utils.LRUCache.CacheNode?
         internal set

      public final var value: Any?
         internal set
   }
}
