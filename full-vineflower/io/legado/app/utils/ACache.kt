package io.legado.app.utils

import io.legado.app.adapters.ReaderAdapterHelper
import java.io.ByteArrayInputStream
import java.io.ByteArrayOutputStream
import java.io.Closeable
import java.io.File
import java.io.IOException
import java.io.ObjectInputStream
import java.io.ObjectOutputStream
import java.io.Serializable
import java.util.Collections
import java.util.HashMap
import java.util.Map.Entry
import java.util.concurrent.atomic.AtomicInteger
import java.util.concurrent.atomic.AtomicLong
import kotlin.jvm.internal.Intrinsics

public class ACache private constructor(cacheDir: File, max_size: Long, max_count: Int) {
   private final var mCache: io.legado.app.utils.ACache.ACacheManager?

   public fun put(key: String, value: String) {
      if (this.mCache != null) {
         val mCache: ACache.ACacheManager = this.mCache;

         try {
            val e: File = mCache.newFile(key);
            FilesKt.writeText$default(e, value, null, 2, null);
            mCache.put(e);
         } catch (var10: Exception) {
            var10.printStackTrace();
         }
      }
   }

   public fun put(key: String, value: String, saveTime: Int) {
      if (saveTime <= 0) {
         this.put(key, value);
      } else {
         this.put(key, ACache.Utils.INSTANCE.newStringWithDateInfo(saveTime, value));
      }
   }

   public fun getAsString(key: String): String? {
      label83: {
         if (this.mCache != null) {
            val file: File = this.mCache.get(key);
            if (!file.exists()) {
               return null;
            }

            try {
               try {
                  val e: java.lang.String = FilesKt.readText$default(file, null, 1, null);
                  if (!ACache.Utils.INSTANCE.isDue(e)) {
                     return ACache.Utils.INSTANCE.clearDateInfo(e);
                  }
               } catch (var12: IOException) {
                  var12.printStackTrace();
                  return null;
               }
            } catch (var13: java.lang.Throwable) {
               ;
            }

            this.remove(key);
         }

         return null;
      }
   }

   public fun getByHashCode(hashCode: String): String? {
      label83: {
         if (this.mCache != null) {
            val file: File = this.mCache.newFileFromHashCode(hashCode);
            if (!file.exists()) {
               return null;
            }

            try {
               try {
                  val e: java.lang.String = FilesKt.readText$default(file, null, 1, null);
                  if (!ACache.Utils.INSTANCE.isDue(e)) {
                     return ACache.Utils.INSTANCE.clearDateInfo(e);
                  }
               } catch (var12: IOException) {
                  var12.printStackTrace();
                  return null;
               }
            } catch (var13: java.lang.Throwable) {
               ;
            }

            file.delete();
         }

         return null;
      }
   }

   public fun put(key: String, value: ByteArray) {
      val var3: ACache.ACacheManager = this.mCache;
      if (this.mCache != null) {
         val file: File = this.mCache.newFile(key);
         FilesKt.writeBytes(file, value);
         var3.put(file);
      }
   }

   public fun put(key: String, value: ByteArray, saveTime: Int) {
      if (saveTime <= 0) {
         this.put(key, value);
      } else {
         this.put(key, ACache.Utils.INSTANCE.newByteArrayWithDateInfo(saveTime, value));
      }
   }

   public fun getAsBinary(key: String): ByteArray? {
      label55: {
         if (this.mCache != null) {
            val mCache: ACache.ACacheManager = this.mCache;
            var removeFile: Boolean = false;

            label57: {
               try {
                  try {
                     val e: File = mCache.get(key);
                     if (!e.exists()) {
                        return null;
                     }

                     val byteArray: ByteArray = FilesKt.readBytes(e);
                     if (!ACache.Utils.INSTANCE.isDue(byteArray)) {
                        ACache.Utils.INSTANCE.clearDateInfo(byteArray);
                     } else {
                        removeFile = true;
                     }
                  } catch (var12: Exception) {
                     var12.printStackTrace();
                     break label57;
                  }
               } catch (var13: java.lang.Throwable) {
                  if (removeFile) {
                     this.remove(key);
                  }
               }

               if (removeFile) {
                  this.remove(key);
               }
            }

            if (removeFile) {
               this.remove(key);
            }
         }

         return null;
      }
   }

   @JvmOverloads
   public fun put(key: String, value: Serializable, saveTime: Int = -1) {
      label30: {
         try {
            val e: ByteArrayOutputStream = new ByteArrayOutputStream();
            val var5: Closeable = new ObjectOutputStream(e);
            var var17: java.lang.Throwable = null as java.lang.Throwable;

            try {
               try {
                  (var5 as ObjectOutputStream).writeObject(value);
                  val data: ByteArray = e.toByteArray();
                  if (saveTime != -1) {
                     this.put(key, data, saveTime);
                  } else {
                     this.put(key, data);
                  }
               } catch (var11: java.lang.Throwable) {
                  var17 = var11;
                  throw var11;
               }
            } catch (var12: java.lang.Throwable) {
               CloseableKt.closeFinally(var5, var17);
            }

            CloseableKt.closeFinally(var5, null as java.lang.Throwable);
         } catch (var13: Exception) {
            var13.printStackTrace();
         }
      }
   }

   public fun getAsObject(key: String): Any? {
      label73:
      if (this.getAsBinary(key) != null) {
         val bis: ByteArrayInputStream = null;
         val ois: ObjectInputStream = null;

         label112: {
            try {
               try {
                  break label112;
               } catch (var13: Exception) {
                  var13.printStackTrace();
               }
            } catch (var14: java.lang.Throwable) {
               try {
                  if (bis != null) {
                     bis.close();
                  }
               } catch (var8: IOException) {
                  var8.printStackTrace();
               }

               try {
                  if (ois != null) {
                     ois.close();
                  }
               } catch (var7: IOException) {
                  var7.printStackTrace();
               }
            }

            try {
               if (bis != null) {
                  bis.close();
               }
            } catch (var12: IOException) {
               var12.printStackTrace();
            }

            try {
               if (ois != null) {
                  ois.close();
                  return null;
               }
            } catch (var11: IOException) {
               var11.printStackTrace();
            }

            return null;
         }

         try {
            bis.close();
         } catch (var10: IOException) {
            var10.printStackTrace();
         }

         try {
            ois.close();
         } catch (var9: IOException) {
            var9.printStackTrace();
         }

         val e: Any;
         return e;
      } else {
         return null;
      }
   }

   public fun file(key: String): File? {
      if (this.mCache != null) {
         val mCache: ACache.ACacheManager = this.mCache;

         try {
            val e: File = mCache.newFile(key);
            if (e.exists()) {
               return e;
            }
         } catch (var9: Exception) {
            var9.printStackTrace();
         }
      }

      return null;
   }

   public fun remove(key: String): Boolean {
      return this.mCache != null && this.mCache.remove(key);
   }

   public fun clear() {
      if (this.mCache != null) {
         this.mCache.clear();
      }
   }

   @JvmOverloads
   fun put(key: java.lang.String, value: Serializable) {
      put$default(this, key, value, 0, 4, null);
   }

   public open inner class ACacheManager(cacheDir: File, sizeLimit: Long, countLimit: Int) {
      private final val cacheCount: AtomicInteger
      private final var cacheDir: File
      private final val cacheSize: AtomicLong
      private final val countLimit: Int
      private final val lastUsageDates: MutableMap<File, Long>
      private final val sizeLimit: Long

      init {
         this.this$0 = `this$0`;
         this.cacheDir = cacheDir;
         this.sizeLimit = sizeLimit;
         this.countLimit = countLimit;
         this.cacheSize = new AtomicLong();
         this.cacheCount = new AtomicInteger();
         this.lastUsageDates = Collections.synchronizedMap(new HashMap<>());
         this.calculateCacheSizeAndCacheCount();
      }

      private fun calculateCacheSizeAndCacheCount() {
         new Thread(ACache.ACacheManager::calculateCacheSizeAndCacheCount$lambda-0).start();
      }

      public fun put(file: File) {
         try {
            for (int curCacheCount = this.cacheCount.get(); curCacheCount + 1 > this.countLimit; curCacheCount = this.cacheCount.addAndGet(-1)) {
               this.cacheSize.addAndGet(-this.removeNext());
            }

            this.cacheCount.addAndGet(1);
            val var13: Long = this.calculateSize(file);
            var curCacheSize: Long = this.cacheSize.get();

            while (curCacheSize + valueSize > this.sizeLimit) {
               curCacheSize = this.cacheSize.addAndGet(-this.removeNext());
            }

            this.cacheSize.addAndGet(var13);
            val var14: Long = System.currentTimeMillis();
            file.setLastModified(var14);
            val var9: java.util.Map = this.lastUsageDates;
            var9.put(file, var14);
         } catch (var12: Exception) {
            var12.printStackTrace();
         }
      }

      public operator fun get(key: String): File {
         val file: File = this.newFile(key);
         val currentTime: Long = System.currentTimeMillis();
         file.setLastModified(currentTime);
         val var5: java.util.Map = this.lastUsageDates;
         var5.put(file, currentTime);
         return file;
      }

      public fun newFile(key: String): File {
         return new File(this.cacheDir, "${key.hashCode()}");
      }

      public fun newFileFromHashCode(hashCode: String): File {
         return new File(this.cacheDir, hashCode);
      }

      public fun remove(key: String): Boolean {
         return this.get(key).delete();
      }

      public fun clear() {
         try {
            this.lastUsageDates.clear();
            this.cacheSize.set(0L);
            val e: Array<File> = this.cacheDir.listFiles();
            if (e != null) {
               val var2: Array<File> = e;
               var var3: Int = 0;
               val var4: Int = e.length;

               while (var3 < var4) {
                  val f: File = var2[var3];
                  var3++;
                  f.delete();
               }
            }
         } catch (var6: Exception) {
            var6.printStackTrace();
         }
      }

      private fun removeNext(): Long {
         try {
            if (this.lastUsageDates.isEmpty()) {
               return 0L;
            } else {
               var e: Any = null;
               var mostLongUsedFile: Any = null;
               val entries: java.util.Set = this.lastUsageDates.entrySet();
               val fileSize: java.util.Map = this.lastUsageDates;
               synchronized (fileSize) {
                  for (Entry var9 : entries) {
                     val key: File = var9.getKey() as File;
                     val lastValueUsage: java.lang.Long = var9.getValue() as java.lang.Long;
                     if (mostLongUsedFile == null) {
                        mostLongUsedFile = key;
                        e = lastValueUsage;
                     } else {
                        val var10000: Long = lastValueUsage;
                        if (var10000 < (e as java.lang.Number).longValue()) {
                           e = lastValueUsage;
                           mostLongUsedFile = key;
                        }
                     }
                  }
               }

               var var16: Long = 0L;
               if (mostLongUsedFile != null) {
                  var16 = this.calculateSize((File)mostLongUsedFile);
                  if (mostLongUsedFile.delete()) {
                     this.lastUsageDates.remove(mostLongUsedFile);
                  }
               }

               return var16;
            }
         } catch (var15: Exception) {
            var15.printStackTrace();
            return 0L;
         }
      }

      private fun calculateSize(file: File): Long {
         return file.length();
      }

      @JvmStatic
      fun `calculateCacheSizeAndCacheCount$lambda-0`(`this$0`: ACache.ACacheManager) {
         try {
            var e: Int = 0;
            var count: Int = 0;
            val cachedFiles: Array<File> = `this$0`.cacheDir.listFiles();
            if (cachedFiles != null) {
               val var4: Array<File> = cachedFiles;
               var var5: Int = 0;
               val var6: Int = cachedFiles.length;

               while (var5 < var6) {
                  val cachedFile: File = var4[var5];
                  var5++;
                  e += (int)`this$0`.calculateSize(cachedFile);
                  count++;
                  val var8: java.util.Map = `this$0`.lastUsageDates;
                  var8.put(cachedFile, cachedFile.lastModified());
               }

               `this$0`.cacheSize.set((long)e);
               `this$0`.cacheCount.set(count);
            }
         } catch (var11: Exception) {
            var11.printStackTrace();
         }
      }
   }

   public companion object {
      private const val MAX_COUNT: Int
      private const val MAX_SIZE: Int
      public const val TIME_DAY: Int
      public const val TIME_HOUR: Int
      private final val mInstanceMap: HashMap<String, ACache>

      @JvmOverloads
      public fun get(cacheName: String = "ACache", maxSize: Long = 50000000L, maxCount: Int = Integer.MAX_VALUE): ACache {
         return this.get(new File(ReaderAdapterHelper.INSTANCE.getAdapter().getCacheDir(), cacheName), maxSize, maxCount);
      }

      @JvmOverloads
      public fun get(cacheDir: File, maxSize: Long = 50000000L, maxCount: Int = Integer.MAX_VALUE): ACache {
         synchronized (this) {
            var manager: ACache = ACache.access$getMInstanceMap$cp().get(cacheDir.getAbsoluteFile().toString()) as ACache;
            if (manager == null) {
               manager = new ACache(cacheDir, maxSize, maxCount, null);
               val var9: java.util.Map = ACache.access$getMInstanceMap$cp();
               val var10: java.lang.String = cacheDir.getAbsolutePath();
               var9.put(var10, manager);
            }

            return manager;
         }
      }

      @JvmOverloads
      fun get(cacheName: java.lang.String, maxSize: Long): ACache {
         return get$default(this, cacheName, maxSize, 0, 4, null);
      }

      @JvmOverloads
      fun get(cacheName: java.lang.String): ACache {
         return get$default(this, cacheName, 0L, 0, 6, null);
      }

      @JvmOverloads
      fun get(): ACache {
         return get$default(this, null, 0L, 0, 7, null);
      }

      @JvmOverloads
      fun get(cacheDir: File, maxSize: Long): ACache {
         return get$default(this, cacheDir, maxSize, 0, 4, null);
      }

      @JvmOverloads
      fun get(cacheDir: File): ACache {
         return get$default(this, cacheDir, 0L, 0, 6, null);
      }
   }

   private object Utils {
      private const val mSeparator: Char = ' '

      public fun isDue(str: String): Boolean {
         val var10001: ByteArray = str.getBytes(Charsets.UTF_8);
         return this.isDue(var10001);
      }

      public fun isDue(data: ByteArray): Boolean {
         try {
            val e: Array<java.lang.String> = this.getDateInfoFromDate(data);
            if (e != null && e.length == 2) {
               var saveTimeStr: java.lang.String = e[0];

               while (StringsKt.startsWith$default(saveTimeStr, "0", false, 2, null)) {
                  if (saveTimeStr == null) {
                     throw new NullPointerException("null cannot be cast to non-null type java.lang.String");
                  }

                  val var10000: java.lang.String = saveTimeStr.substring(1);
                  saveTimeStr = var10000;
               }

               if (System.currentTimeMillis() > java.lang.Long.valueOf(saveTimeStr) + java.lang.Long.valueOf(e[1]) * 1000) {
                  return true;
               }
            }
         } catch (var7: Exception) {
            var7.printStackTrace();
         }

         return false;
      }

      public fun newStringWithDateInfo(second: Int, strInfo: String): String {
         return Intrinsics.stringPlus(this.createDateInfo(second), strInfo);
      }

      public fun newByteArrayWithDateInfo(second: Int, data2: ByteArray): ByteArray {
         val retData: java.lang.String = this.createDateInfo(second);
         if (retData == null) {
            throw new NullPointerException("null cannot be cast to non-null type java.lang.String");
         } else {
            val var10000: ByteArray = retData.getBytes(Charsets.UTF_8);
            val var7: ByteArray = new byte[var10000.length + data2.length];
            System.arraycopy(var10000, 0, var7, 0, var10000.length);
            System.arraycopy(data2, 0, var7, var10000.length, data2.length);
            return var7;
         }
      }

      public fun clearDateInfo(strInfo: String?): String? {
         if (strInfo != null) {
            val var10000: ACache.Utils = INSTANCE;
            if (strInfo == null) {
               throw new NullPointerException("null cannot be cast to non-null type java.lang.String");
            }

            val var10001: ByteArray = strInfo.getBytes(Charsets.UTF_8);
            if (var10000.hasDateInfo(var10001)) {
               val var11: Int = StringsKt.indexOf$default(strInfo, ' ', 0, false, 6, null) + 1;
               if (strInfo == null) {
                  throw new NullPointerException("null cannot be cast to non-null type java.lang.String");
               }

               val var13: java.lang.String = strInfo.substring(var11);
               return var13;
            }
         }

         return strInfo;
      }

      public fun clearDateInfo(data: ByteArray): ByteArray {
         return if (this.hasDateInfo(data)) this.copyOfRange(data, this.indexOf(data, ' ') + 1, data.length) else data;
      }

      public fun hasDateInfo(data: ByteArray?): Boolean {
         return data != null && data.length > 15 && data[13] == 45 && this.indexOf(data, ' ') > 14;
      }

      public fun getDateInfoFromDate(data: ByteArray): Array<String>? {
         return if (this.hasDateInfo(data))
            new java.lang.String[]{
               new java.lang.String(this.copyOfRange(data, 0, 13), Charsets.UTF_8),
               new java.lang.String(this.copyOfRange(data, 14, this.indexOf(data, ' ')), Charsets.UTF_8)
            }
            else
            null;
      }

      private fun indexOf(data: ByteArray, c: Char): Int {
         var var3: Int = 0;
         val var4: Int = data.length + -1;
         if (0 <= data.length + -1) {
            do {
               val i: Int = var3++;
               if (data[i] == (byte)c) {
                  return i;
               }
            } while (var3 <= var4);
         }

         return -1;
      }

      private fun copyOfRange(original: ByteArray, from: Int, to: Int): ByteArray {
         val newLength: Int = to - from;
         if (to - from < 0) {
            throw (new IllegalArgumentException(("$from > $to").toString())) as java.lang.Throwable;
         } else {
            val var9: ByteArray = new byte[newLength];
            System.arraycopy(original, from, var9, 0, Math.min(original.length - from, newLength));
            return var9;
         }
      }

      private fun createDateInfo(second: Int): String {
         val currentTime: StringBuilder = new StringBuilder("${System.currentTimeMillis()}");

         while (currentTime.length() < 13) {
            currentTime.insert(0, "0");
         }

         return "$currentTime-$second ";
      }
   }
}
