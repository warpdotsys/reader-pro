package io.legado.app.help

import io.legado.app.adapters.ReaderAdapterHelper
import io.legado.app.model.analyzeRule.QueryTTF
import io.legado.app.utils.ACache
import java.io.File
import java.util.HashMap

public class CacheManager(userNameSpace: String) {
   public final val cacheInstance: ACache
   private final val queryTTFMap: HashMap<String, Pair<Long, QueryTTF>>
   public final val userNameSpace: String

   init {
      this.userNameSpace = userNameSpace;
      this.queryTTFMap = new HashMap<>();
      this.cacheInstance = ACache.Companion
         .get(new File(ReaderAdapterHelper.INSTANCE.getAdapter().getWorkDir("storage", "cache", "runtimeCache", this.userNameSpace)), 50000000L, 1000000);
   }

   @JvmOverloads
   public fun put(key: String, value: Any, saveTime: Int = 0) {
      if (key.length() != 0) {
         val var10: Long = if (saveTime == 0) 0L else System.currentTimeMillis() + saveTime * 1000;
         if (value is QueryTTF) {
            this.queryTTFMap.put(key, new Pair<>(var10, (QueryTTF)value));
         } else if (value is ByteArray) {
            this.cacheInstance.put(key, value as ByteArray, saveTime);
         } else {
            this.cacheInstance.put(key, value.toString(), saveTime);
         }
      }
   }

   public fun get(key: String): String? {
      return if (key.length() == 0) null else this.cacheInstance.getAsString(key);
   }

   public fun getInt(key: String): Int? {
      val var2: java.lang.String = this.get(key);
      return if (var2 == null) null else StringsKt.toIntOrNull(var2);
   }

   public fun getLong(key: String): Long? {
      val var2: java.lang.String = this.get(key);
      return if (var2 == null) null else StringsKt.toLongOrNull(var2);
   }

   public fun getDouble(key: String): Double? {
      val var2: java.lang.String = this.get(key);
      return if (var2 == null) null else StringsKt.toDoubleOrNull(var2);
   }

   public fun getFloat(key: String): Float? {
      val var2: java.lang.String = this.get(key);
      return if (var2 == null) null else StringsKt.toFloatOrNull(var2);
   }

   public fun getByteArray(key: String): ByteArray? {
      return if (key.length() == 0) null else this.cacheInstance.getAsBinary(key);
   }

   public fun getQueryTTF(key: String): QueryTTF? {
      val var4: Pair = this.queryTTFMap.get(key);
      if (var4 == null) {
         return null;
      } else {
         return if ((var4.getFirst() as java.lang.Number).longValue() != 0L && (var4.getFirst() as java.lang.Number).longValue() <= System.currentTimeMillis())
            null
            else
            var4.getSecond() as QueryTTF;
      }
   }

   public fun putFile(key: String, value: String, saveTime: Int = 0) {
      if (key.length() != 0) {
         this.cacheInstance.put(key, value, saveTime);
      }
   }

   public fun getFile(key: String): String? {
      return if (key.length() == 0) null else this.cacheInstance.getAsString(key);
   }

   public fun delete(key: String) {
      if (key.length() != 0) {
         this.cacheInstance.remove(key);
      }
   }

   @JvmOverloads
   fun put(key: java.lang.String, value: Any) {
      put$default(this, key, value, 0, 4, null);
   }
}
