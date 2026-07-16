package com.htmake.reader.utils

import com.fasterxml.jackson.core.JsonFactory
import com.fasterxml.jackson.core.JsonParser
import com.fasterxml.jackson.core.JsonToken
import com.fasterxml.jackson.core.TreeNode
import com.fasterxml.jackson.databind.JsonNode
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.databind.node.ObjectNode
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.google.gson.reflect.TypeToken
import com.htmake.reader.config.AppConfig
import com.htmake.reader.entity.License
import com.htmake.reader.entity.MongoFile
import com.mongodb.client.FindIterable
import com.mongodb.client.MongoCollection
import com.mongodb.client.model.Filters
import com.mongodb.client.model.ReplaceOptions
import com.mongodb.client.result.UpdateResult
import io.legado.app.data.entities.Book
import io.legado.app.utils.Base64
import io.legado.app.utils.EncoderUtils
import io.legado.app.utils.FileUtils
import io.legado.app.utils.MD5Utils
import io.legado.app.utils.MapDeserializerDoubleAsIntFix
import io.vertx.core.json.JsonArray
import io.vertx.core.json.JsonObject
import java.io.BufferedReader
import java.io.Closeable
import java.io.File
import java.io.FileInputStream
import java.io.FileOutputStream
import java.io.FileReader
import java.io.InputStream
import java.io.InputStreamReader
import java.io.OutputStream
import java.io.OutputStreamWriter
import java.io.Reader
import java.lang.reflect.Array
import java.net.Socket
import java.nio.file.Files
import java.nio.file.Path
import java.nio.file.Paths
import java.nio.file.StandardCopyOption
import java.security.KeyFactory
import java.security.PublicKey
import java.security.spec.X509EncodedKeySpec
import java.util.ArrayList
import java.util.Arrays
import java.util.Enumeration
import java.util.LinkedHashMap
import java.util.NoSuchElementException
import java.util.UUID
import java.util.Base64.Encoder
import java.util.Map.Entry
import java.util.concurrent.TimeUnit
import java.util.concurrent.locks.ReadWriteLock
import java.util.concurrent.locks.ReentrantReadWriteLock
import java.util.zip.ZipEntry
import java.util.zip.ZipFile
import java.util.zip.ZipOutputStream
import javax.net.ssl.SSLSocketFactory
import kotlin.jvm.functions.Function1
import kotlin.jvm.functions.Function3
import kotlin.jvm.internal.Intrinsics
import kotlin.jvm.internal.SpreadBuilder
import kotlin.random.Random
import kotlin.reflect.KMutableProperty
import kotlin.reflect.KProperty1
import mu.KLogger
import mu.KotlinLogging
import okhttp3.HttpUrl
import org.jetbrains.annotations.Nullable

private const val MAX_CACHE_SIZE: Int = 1000

public final var _licenseValid: Boolean = true
   internal set

public final val gson: Gson =
   new GsonBuilder()
      .registerTypeAdapter((new TypeToken<java.util.Map<java.lang.String, ? extends Object>>() {}).getType(), new MapDeserializerDoubleAsIntFix())
      .registerTypeAdapter(Int::class.javaPrimitiveType, new IntTypeAdapter())
      .registerTypeAdapter(java.lang.Long::class.javaPrimitiveType, new LongTypeAdapter())
      .disableHtmlEscaping()
      .create()

private final val lockMap: <unrepresentable> = new LinkedHashMap<java.lang.String, ReadWriteLock>() {
   {
      super(16, 0.75F, true);
   }

   @Override
   protected boolean removeEldestEntry(@Nullable Entry<java.lang.String, ReadWriteLock> eldest) {
      return this.size() > 1000;
   }
}

public final val logger: KLogger = KotlinLogging.INSTANCE.logger(<unrepresentable>.INSTANCE)
public final val prettyGson: Gson =
   new GsonBuilder()
      .registerTypeAdapter((new TypeToken<java.util.Map<java.lang.String, ? extends Object>>() {}).getType(), new MapDeserializerDoubleAsIntFix())
      .registerTypeAdapter(Int::class.javaPrimitiveType, new IntTypeAdapter())
      .registerTypeAdapter(java.lang.Long::class.javaPrimitiveType, new LongTypeAdapter())
      .disableHtmlEscaping()
      .setPrettyPrinting()
      .create()

public final var storageFinalPath: String = ""
   internal set

public final var workDirInit: Boolean
   internal set

public final var workDirPath: String = ""
   internal set

public fun String.url(): String {
   if (StringsKt.startsWith$default(`$this$url`, "//", false, 2, null)) {
      return HttpUrl.Companion.get(Intrinsics.stringPlus("http:", `$this$url`)).toString();
   } else {
      return if (StringsKt.startsWith$default(`$this$url`, "http", false, 2, null)) HttpUrl.Companion.get(`$this$url`).toString() else `$this$url`;
   }
}

public fun String.toDir(absolute: Boolean = false): String {
   var path: java.lang.String = `$this$toDir`;
   if (StringsKt.endsWith$default(`$this$toDir`, "/", false, 2, null)) {
      val var10000: java.lang.String = `$this$toDir`.substring(0, `$this$toDir`.length() - 1);
      path = var10000;
   }

   if (absolute && !StringsKt.startsWith$default(path, "/", false, 2, null)) {
      path = Intrinsics.stringPlus("/", path);
   }

   return path;
}

@JvmSynthetic
fun `toDir$default`(var0: java.lang.String, var1: Boolean, var2: Int, var3: Any): java.lang.String {
   if ((var2 and 1) != 0) {
      var1 = false;
   }

   return toDir(var0, var1);
}

public fun File.deleteRecursively() {
   if (`$this$deleteRecursively`.exists()) {
      if (`$this$deleteRecursively`.isFile()) {
         `$this$deleteRecursively`.delete();
      } else {
         val `$this$forEach$iv`: Array<File> = `$this$deleteRecursively`.listFiles();
         val var3: Array<Any> = `$this$forEach$iv`;
         val var4: Int = `$this$forEach$iv`.length;

         for (int var5 = 0; var5 < var4; var5++) {
            val it: File = var3[var5] as File;
            deleteRecursively(it);
         }

         `$this$deleteRecursively`.delete();
      }
   }
}

public fun File.listFilesRecursively(): List<File> {
   val var10: ArrayList = new ArrayList();
   if (`$this$listFilesRecursively`.exists()) {
      if (`$this$listFilesRecursively`.isFile()) {
         var10.add(`$this$listFilesRecursively`);
      } else {
         val var11: Array<File> = `$this$listFilesRecursively`.listFiles();
         val var4: Array<Any> = var11;
         val var5: Int = var11.length;

         for (int var6 = 0; var6 < var5; var6++) {
            val it: File = var4[var6] as File;
            var10.add(var4[var6] as File);
            if (it.isDirectory()) {
               var10.addAll(listFilesRecursively(it));
            }
         }
      }
   }

   return var10;
}

public fun File.unzip(descDir: String): Boolean {
   if (!`$this$unzip`.exists()) {
      return false;
   } else {
      label132: {
         val buffer: ByteArray = new byte[1024];
         var outputStream: OutputStream = null;
         var inputStream: InputStream = null;

         label88: {
            try {
               try {
                  val e: ZipFile = new ZipFile(`$this$unzip`.toString());
                  val entries: Enumeration = e.entries();

                  while (entries.hasMoreElements()) {
                     val zipEntryName: Any = entries.nextElement();
                     if (zipEntryName == null) {
                        throw new NullPointerException("null cannot be cast to non-null type java.util.zip.ZipEntry");
                     }

                     val zipEntry: ZipEntry = zipEntryName as ZipEntry;
                     var descFilePath: java.lang.String = (zipEntryName as ZipEntry).getName();
                     descFilePath = "$descDir${File.separator}$descFilePath";
                     if (zipEntry.isDirectory()) {
                        createDir(descFilePath);
                     } else {
                        inputStream = e.getInputStream(zipEntry);
                        outputStream = new FileOutputStream(createFile(descFilePath));

                        while (true) {
                           val var12: Int = inputStream.read(buffer);
                           if (var12 <= 0) {
                              inputStream.close();
                              (outputStream as FileOutputStream).close();
                              break;
                           }

                           (outputStream as FileOutputStream).write(buffer, 0, var12);
                        }
                     }
                  }
                  break label88;
               } catch (var17: Exception) {
                  var17.printStackTrace();
               }
            } catch (var18: java.lang.Throwable) {
               if (inputStream != null) {
                  inputStream.close();
               }

               if (outputStream != null) {
                  outputStream.close();
               }
            }

            if (inputStream != null) {
               inputStream.close();
            }

            if (outputStream != null) {
               outputStream.close();
            }
         }

         if (inputStream != null) {
            inputStream.close();
         }

         if (outputStream != null) {
            outputStream.close();
         }
      }
   }
}

public fun File.zip(zipFilePath: String): Boolean {
   if (!`$this$zip`.exists()) {
      return false;
   } else if (`$this$zip`.isDirectory()) {
      val var4: Array<File> = `$this$zip`.listFiles();
      return zip(ArraysKt.toList(var4), zipFilePath);
   } else {
      return zip(CollectionsKt.arrayListOf(new File[]{`$this$zip`}), zipFilePath);
   }
}

public fun zip(files: List<File>, zipFilePath: String): Boolean {
   if (files.isEmpty()) {
      return false;
   } else {
      label128: {
         val zipFile: File = createFile(zipFilePath);
         val buffer: ByteArray = new byte[1024];
         val zipOutputStream: ZipOutputStream = null;
         var inputStream: FileInputStream = null;

         var var19: Boolean;
         label79: {
            try {
               try {
                  while (e.hasNext()) {
                     val file: File = e.next() as File;
                     if (file.exists()) {
                        zipOutputStream.putNextEntry(new ZipEntry(file.getName()));
                        inputStream = new FileInputStream(file);

                        while (true) {
                           val var9: Int = inputStream.read(buffer);
                           if (var9 <= 0) {
                              zipOutputStream.closeEntry();
                              break;
                           }

                           zipOutputStream.write(buffer, 0, var9);
                        }
                     }
                  }

                  var19 = true;
                  break label79;
               } catch (var14: Exception) {
                  var14.printStackTrace();
               }
            } catch (var15: java.lang.Throwable) {
               if (inputStream != null) {
                  inputStream.close();
               }

               if (zipOutputStream != null) {
                  zipOutputStream.close();
               }
            }

            if (inputStream != null) {
               inputStream.close();
            }

            if (zipOutputStream != null) {
               zipOutputStream.close();
            }
         }

         if (inputStream != null) {
            inputStream.close();
         }

         zipOutputStream.close();
         return var19;
      }
   }
}

public fun createDir(filePath: String): File {
   logger.debug("createDir filePath {}", filePath);
   val file: File = new File(filePath);
   if (!file.exists()) {
      file.mkdirs();
   }

   return file;
}

public fun createFile(filePath: String): File {
   logger.debug("createFile filePath {}", filePath);
   val file: File = new File(filePath);
   val var10000: File = file.getParentFile();
   if (!var10000.exists()) {
      var10000.mkdirs();
   }

   if (!file.exists()) {
      file.createNewFile();
   }

   return file;
}

public fun getWorkDir(subPath: String = ""): String {
   if (!workDirInit && workDirPath.length() == 0) {
      val var5: AppConfig = SpringContextUtils.getBean("appConfig", AppConfig.class);
      if (var5 != null && var5.getWorkDir().length() > 0 && !var5.getWorkDir().equals(".")) {
         val var8: File = new File(var5.getWorkDir());
         if (var8.exists() && !var8.isDirectory()) {
            logger.error("reader.app.workDir={} is not a directory", var5.getWorkDir());
         } else {
            if (!var8.exists()) {
               logger.info("reader.app.workDir={} not exists, creating", var5.getWorkDir());
               var8.mkdirs();
            }

            val var12: java.lang.String = var8.getAbsolutePath();
            workDirPath = var12;
         }
      }

      label40:
      if (workDirPath.length() == 0) {
         val var10: java.lang.String = System.getProperty("os.name");
         val var14: java.lang.String = System.getProperty("user.dir");
         logger.info("osName: {} currentDir: {}", var10, var14);
         if (StringsKt.startsWith(var10, "Mac OS", true)) {
            if (!StringsKt.startsWith$default(var14, "/Users/", false, 2, null)) {
               workDirPath = Paths.get(System.getProperty("user.home"), ".reader").toString();
               break label40;
            }
         }

         workDirPath = var14;
      }

      logger.info("Using workdir: {}", workDirPath);
      workDirInit = true;
   }

   return Paths.get(workDirPath, subPath).toString();
}

@JvmSynthetic
fun `getWorkDir$default`(var0: java.lang.String, var1: Int, var2: Any): java.lang.String {
   if ((var1 and 1) != 0) {
      var0 = "";
   }

   return getWorkDir(var0);
}

public fun getWorkDir(vararg subDirFiles: String): String {
   return getWorkDir(getRelativePath(Arrays.copyOf(subDirFiles, subDirFiles.length)));
}

public fun getRelativePath(vararg subDirFiles: String): String {
   val path: StringBuilder = new StringBuilder("");
   val var4: Array<java.lang.String> = subDirFiles;
   val it: Int = subDirFiles.length;

   for (int var6 = 0; var6 < it; var6++) {
      val `element$iv`: Any = var4[var6];
      if (var4[var6].length() > 0) {
         path.append(File.separator).append((java.lang.String)`element$iv`);
      }
   }

   val `$this$forEach$iv`: java.lang.String = path.toString();
   val var10000: java.lang.String;
   if (StringsKt.startsWith$default(`$this$forEach$iv`, "/", false, 2, null)) {
      var10000 = `$this$forEach$iv`.substring(1);
   } else {
      var10000 = `$this$forEach$iv`;
   }

   return var10000;
}

public fun getStoragePath(): String {
   if (storageFinalPath.length() > 0) {
      return storageFinalPath;
   } else {
      val var4: java.lang.String;
      if (SpringContextUtils.getBean("appConfig", AppConfig.class) != null) {
         var4 = getWorkDir("storage");
         storageFinalPath = var4;
      } else {
         val var2: java.lang.String = new File("storage").getPath();
         var4 = var2;
      }

      logger.info("Using storagePath: {}", var4);
      return var4;
   }
}

public fun saveStorage(vararg name: String, value: Any, pretty: Boolean = false, ext: String = ".json") {
   label99: {
      var var10000: java.lang.String;
      if (value is java.lang.String) {
         var10000 = value as java.lang.String;
      } else if (value is JsonObject || value is JsonArray) {
         var10000 = value.toString();
      } else if (pretty) {
         val storagePath: java.lang.String = prettyGson.toJson(value);
         var10000 = storagePath;
      } else {
         val var28: java.lang.String = gson.toJson(value);
         var10000 = var28;
      }

      val toJson: java.lang.String = var10000;
      val var29: java.lang.String = getStoragePath();
      val storageDir: File = new File(var29);
      if (!storageDir.exists()) {
         storageDir.mkdirs();
      }

      var filename: java.lang.String = ArraysKt.last(name);
      val file: SpreadBuilder = new SpreadBuilder(2);
      file.addSpread(ArraysKt.copyOfRange(name, 0, name.length - 1));
      file.add(Intrinsics.stringPlus(filename, ext));
      val path: java.lang.String = getRelativePath(file.toArray(new java.lang.String[file.size()]) as Array<java.lang.String>);
      val var31: File = new File("$var29${File.separator}$path");
      logger.info("Save file to storage name: {} path: {}", name, var31.getAbsoluteFile());
      if (!var31.getParentFile().exists()) {
         var31.getParentFile().mkdirs();
      }

      val lock: File = var31.getAbsoluteFile();
      filename = FilesKt.getNameWithoutExtension(lock);
      val var56: ReadWriteLock;
      synchronized (lockMap) {
         val userCount: java.util.Map = lockMap;
         val verifyKeyPath: java.lang.String = var31.getAbsolutePath();
         val md5Encode: Any = userCount.get(verifyKeyPath);
         if (md5Encode == null) {
            val var51: Any = new ReentrantReadWriteLock();
            userCount.put(verifyKeyPath, var51);
            var10000 = (java.lang.String)var51;
         } else {
            var10000 = (java.lang.String)md5Encode;
         }

         var56 = var10000 as ReadWriteLock;
      }

      val var32: ReadWriteLock = var56;
      var var34: Boolean = false;

      try {
         try {
            var34 = var32.writeLock().tryLock(10L, TimeUnit.SECONDS);
            if (!var34) {
               throw new Exception(Intrinsics.stringPlus("保存文件超时: ", var31.getAbsolutePath()));
            }

            val var36: Path = Files.createTempFile(Paths.get(var31.getParentFile().getPath()).toAbsolutePath(), filename, ".temp");
            if (toJson == null) {
               throw new NullPointerException("null cannot be cast to non-null type java.lang.String");
            }

            val var10001: ByteArray = toJson.getBytes(Charsets.UTF_8);
            Files.write(var36, var10001);
            val var39: Path = Paths.get(var31.getPath());
            val var41: Path = Paths.get(var31.getParentFile().getPath(), Intrinsics.stringPlus(filename, ".backup.json")).toAbsolutePath();
            if (Files.exists(var39)) {
               Files.move(var39, var41, StandardCopyOption.ATOMIC_MOVE);
            }

            Files.move(var36, var39, StandardCopyOption.ATOMIC_MOVE);
            Files.deleteIfExists(var36);
            if (filename.length() >= 32) {
               Files.deleteIfExists(var41);
            }

            if ("users".equals(filename)) {
               val var46: Int = countOccurrences(toJson, "username");
               val var48: SpreadBuilder = new SpreadBuilder(2);
               var48.addSpread(ArraysKt.copyOfRange(name, 0, name.length - 1));
               var48.add(".$filename.key");
               val var49: File = new File(
                  "$var29${File.separator}${getRelativePath(var48.toArray(new java.lang.String[var48.size()]) as Array<java.lang.String>)}"
               );
               if (!var49.exists()) {
                  var49.createNewFile();
               }

               val var50: java.lang.String = MD5Utils.INSTANCE.md5Encode(Intrinsics.stringPlus("userCount=", var46)).toString();
               val var53: Int = var50.length() - 16;
               if (var50 == null) {
                  throw new NullPointerException("null cannot be cast to non-null type java.lang.String");
               }

               val var58: java.lang.String = var50.substring(var53);
               FilesKt.writeText$default(var49, var58, null, 2, null);
            }

            saveMongoFile(path, toJson);
         } catch (var23: Exception) {
            logger.error("保存文件失败: ", var23);
            throw new Exception(Intrinsics.stringPlus("保存文件失败: ", var31.getAbsolutePath()));
         }
      } catch (var24: java.lang.Throwable) {
         if (var34) {
            var56.writeLock().unlock();
         }
      }

      if (var34) {
         var56.writeLock().unlock();
      }
   }
}

@JvmSynthetic
fun `saveStorage$default`(var0: Array<java.lang.String>, var1: Any, var2: Boolean, var3: java.lang.String, var4: Int, var5: Any) {
   if ((var4 and 4) != 0) {
      var2 = false;
   }

   if ((var4 and 8) != 0) {
      var3 = ".json";
   }

   saveStorage(var0, var1, var2, var3);
}

public fun getStorageFile(vararg name: String, ext: String = ".json"): File {
   val storagePath: java.lang.String = getStoragePath();
   val storageDir: File = new File(storagePath);
   if (!storageDir.exists()) {
      storageDir.mkdirs();
   }

   val filename: java.lang.String = ArraysKt.last(name);
   val var6: SpreadBuilder = new SpreadBuilder(2);
   var6.addSpread(ArraysKt.copyOfRange(name, 0, name.length - 1));
   var6.add(Intrinsics.stringPlus(filename, ext));
   return new File("$storagePath${File.separator}${getRelativePath(var6.toArray(new java.lang.String[var6.size()]) as Array<java.lang.String>)}");
}

@JvmSynthetic
fun `getStorageFile$default`(var0: Array<java.lang.String>, var1: java.lang.String, var2: Int, var3: Any): File {
   if ((var2 and 2) != 0) {
      var1 = ".json";
   }

   return getStorageFile(var0, var1);
}

public fun getStorage(vararg name: String, ext: String = ".json"): String? {
   val storagePath: java.lang.String = getStoragePath();
   val filename: java.lang.String = ArraysKt.last(name);
   val file: SpreadBuilder = new SpreadBuilder(2);
   file.addSpread(ArraysKt.copyOfRange(name, 0, name.length - 1));
   file.add(Intrinsics.stringPlus(filename, ext));
   val path: java.lang.String = getRelativePath(file.toArray(new java.lang.String[file.size()]) as Array<java.lang.String>);
   val var31: File = getStorageFile(Arrays.copyOf(name, name.length), ext);
   logger.info("Read file from storage name: {} path: {}", name, var31.getAbsoluteFile());
   if (!var31.exists()) {
      val var32: java.lang.String = readMongoFile(path);
      val var64: java.lang.String;
      if (var32 == null) {
         var64 = null;
      } else {
         if (var32.length() > 0) {
            if (!var31.getParentFile().exists()) {
               var31.getParentFile().mkdirs();
            }

            var31.createNewFile();
            FilesKt.writeText$default(var31, var32, null, 2, null);
         }

         var64 = var32;
      }

      return var64;
   } else {
      label264: {
         var var63: ReadWriteLock;
         synchronized (lockMap) {
            val verifyKeyFile: java.util.Map = lockMap;
            val verifyKeyContent: java.lang.String = var31.getAbsolutePath();
            val md5Encode: Any = verifyKeyFile.get(verifyKeyContent);
            if (md5Encode == null) {
               val var59: Any = new ReentrantReadWriteLock();
               verifyKeyFile.put(verifyKeyContent, var59);
               var63 = (ReadWriteLock)var59;
            } else {
               var63 = (ReadWriteLock)md5Encode;
            }

            var63 = var63;
         }

         val lock: ReadWriteLock = var63;
         var var37: Boolean = false;

         label134: {
            try {
               try {
                  var var41: FileReader;
                  label268: {
                     var37 = lock.readLock().tryLock(10L, TimeUnit.SECONDS);
                     if (!var37) {
                        throw new Exception(Intrinsics.stringPlus("读取文件超时: ", var31.getAbsolutePath()));
                     }

                     var41 = new FileReader(var31);

                     try {
                        val var35: java.lang.String = TextStreamsKt.readText(var41);
                        if (var35.length() != 0) {
                           if (!"users".equals(filename)) {
                              break label268;
                           }

                           val var47: SpreadBuilder = new SpreadBuilder(2);
                           var47.addSpread(ArraysKt.copyOfRange(name, 0, name.length - 1));
                           var47.add(".$filename.key");
                           val var48: File = new File(
                              "$storagePath${File.separator}${getRelativePath(var47.toArray(new java.lang.String[var47.size()]) as Array<java.lang.String>)}"
                           );
                           if (var48.exists()) {
                              val var50: java.lang.String = FilesKt.readText$default(var48, null, 1, null);
                              val var58: java.lang.String = MD5Utils.INSTANCE
                                 .md5Encode(Intrinsics.stringPlus("userCount=", countOccurrences(var35, "username")))
                                 .toString();
                              val var61: Int = var58.length() - 16;
                              if (var58 == null) {
                                 throw new NullPointerException("null cannot be cast to non-null type java.lang.String");
                              }

                              val var10001: java.lang.String = var58.substring(var61);
                              if (!var50.equals(var10001)) {
                                 throw new Exception("用户数据被篡改，请联系开发者修复");
                              }
                           }
                           break label268;
                        }

                        val var44: java.lang.String = readMongoFile(path);
                        if (var44 != null) {
                           if (var44.length() > 0) {
                              if (!var31.getParentFile().exists()) {
                                 var31.getParentFile().mkdirs();
                              }

                              var31.createNewFile();
                              FilesKt.writeText$default(var31, var44, null, 2, null);
                           }
                        }
                     } catch (var20: java.lang.Throwable) {
                        var41.close();
                     }

                     var41.close();
                     break label134;
                  }

                  var41.close();
               } catch (var21: Exception) {
                  logger.error("读取文件失败: ", var21);
                  throw new Exception(Intrinsics.stringPlus("读取文件失败: ", var31.getAbsolutePath()));
               }
            } catch (var22: java.lang.Throwable) {
               if (var37) {
                  var63.readLock().unlock();
               }
            }

            if (var37) {
               var63.readLock().unlock();
            }
         }

         if (var37) {
            var63.readLock().unlock();
         }
      }
   }
}

@JvmSynthetic
fun `getStorage$default`(var0: Array<java.lang.String>, var1: java.lang.String, var2: Int, var3: Any): java.lang.String {
   if ((var2 and 2) != 0) {
      var1 = ".json";
   }

   return getStorage(var0, var1);
}

public fun getMongoFileStorage(): MongoCollection<MongoFile>? {
   return MongoManager.INSTANCE.fileStorage(SpringContextUtils.getBean("appConfig", AppConfig.class).getMongoDbName(), "storage");
}

public fun readMongoFile(path: String): String? {
   if (MongoManager.INSTANCE.isInit()) {
      logger.info("Get mongoFile {}", path);
      val var2: MongoCollection = getMongoFileStorage();
      val var10000: MongoFile;
      if (var2 == null) {
         var10000 = null;
      } else {
         val var3: FindIterable = var2.find(Filters.eq("path", path));
         var10000 = if (var3 == null) null else var3.first() as MongoFile;
      }

      if (var10000 != null) {
         return var10000.getContent();
      }
   }

   return null;
}

public fun saveMongoFile(path: String, content: String): Boolean {
   if (MongoManager.INSTANCE.isInit()) {
      logger.info("Save mongoFile {}", path);
      var e: MongoCollection = getMongoFileStorage();
      val var10000: MongoFile;
      if (e == null) {
         var10000 = null;
      } else {
         val var4: FindIterable = e.find(Filters.eq("path", path));
         var10000 = if (var4 == null) null else var4.first() as MongoFile;
      }

      if (var10000 != null) {
         var10000.setContent(content);
         var10000.setUpdated_at(System.currentTimeMillis());
         val var9: MongoCollection = getMongoFileStorage();
         val var8: UpdateResult = if (var9 == null) null else var9.replaceOne(Filters.eq("path", path), var10000, new ReplaceOptions().upsert(true));
         return var8 != null && var8.getModifiedCount() > 0L;
      }

      val var6: MongoFile = new MongoFile(path, content, 0L, 0L, 12, null);

      try {
         e = getMongoFileStorage();
         if (e != null) {
            e.insertOne(var6);
         }

         return true;
      } catch (var5: Exception) {
         logger.info("Save mongoFile {} failed", path);
         var5.printStackTrace();
      }
   }

   return false;
}

public fun countOccurrences(str: String, subStr: String): Int {
   var count: Int = 0;
   var startIndex: Int = 0;

   while (startIndex < str.length()) {
      val index: Int = StringsKt.indexOf$default(str, subStr, startIndex, false, 4, null);
      if (index == -1) {
         break;
      }

      count++;
      startIndex = index + subStr.length();
   }

   return count;
}

public fun asJsonArray(value: Any?): JsonArray? {
   if (value is JsonArray) {
      return value as JsonArray;
   } else if (value is java.lang.String) {
      try {
         return new JsonArray(value as java.lang.String);
      } catch (var2: Exception) {
         logger.error("解析内容出错: {}  内容: \n{}", var2, value);
         throw var2;
      }
   } else {
      return null;
   }
}

public fun parseJsonStringList(
   file: File,
   fields: Set<String>? = null,
   exclude: Set<String>? = null,
   startIndex: Int = 0,
   endIndex: Int = Integer.MAX_VALUE,
   checkNotEmpty: Set<String>? = null,
   filter: ((ObjectNode) -> Boolean)? = null
): JsonArray? {
   if (!file.exists()) {
      return null;
   } else {
      try {
         label148: {
            val factory: JsonFactory = new ObjectMapper().getFactory();
            val resultList: JsonArray = new JsonArray();
            var var30: Int = -1;
            val var11: Closeable = factory.createParser(file);
            var var31: java.lang.Throwable = null as java.lang.Throwable;

            try {
               try {
                  val parser: JsonParser = var11 as JsonParser;
                  if ((var11 as JsonParser).nextToken() === JsonToken.START_ARRAY) {
                     while (parser.nextToken() != JsonToken.END_ARRAY) {
                        if (parser.currentToken() === JsonToken.START_OBJECT) {
                           if (fields == null || fields.isEmpty()) {
                              if (filter != null) {
                                 val var37: TreeNode = parser.readValueAsTree();
                                 val var38: ObjectNode = (var37 as JsonNode) as ObjectNode;
                                 if (filter.invoke((var37 as JsonNode) as ObjectNode) as java.lang.Boolean) {
                                    var30++;
                                 }

                                 if (var30 >= startIndex) {
                                    if (var30 > endIndex) {
                                       break;
                                    }

                                    val var44: java.lang.String = var38.toString();
                                    resultList.add(var44);
                                 }
                              } else if (++var30 < startIndex) {
                                 parser.skipChildren();
                              } else {
                                 if (var30 > endIndex) {
                                    break;
                                 }

                                 val var39: TreeNode = parser.readValueAsTree();
                                 val objectNodex: ObjectNode = (var39 as JsonNode) as ObjectNode;
                                 if (exclude != null && !exclude.isEmpty()) {
                                    val var43: java.lang.Iterable;
                                    for (Object element$iv : var43) {
                                       objectNodex.remove(`element$iv` as java.lang.String);
                                    }
                                 }

                                 val var47: java.lang.String = objectNodex.toString();
                                 resultList.add(var47);
                              }
                           } else if (++var30 < startIndex) {
                              parser.skipChildren();
                           } else {
                              if (var30 > endIndex) {
                                 break;
                              }

                              val var33: JsonObject = new JsonObject();

                              while (parser.nextToken() != JsonToken.END_OBJECT) {
                                 val var36: java.lang.String = parser.getCurrentName();
                                 parser.nextToken();
                                 if (fields.contains(var36)) {
                                    var33.put(var36, parser.getValueAsString());
                                 } else if (checkNotEmpty != null && checkNotEmpty.contains(var36)) {
                                    val var41: java.lang.CharSequence = parser.getValueAsString();
                                    var33.put(var36, var41 != null && var41.length() != 0);
                                 } else {
                                    parser.skipChildren();
                                 }
                              }

                              resultList.add(var33.toString());
                           }
                        }
                     }
                  }

                  parser.close();
               } catch (var24: java.lang.Throwable) {
                  var31 = var24;
                  throw var24;
               }
            } catch (var25: java.lang.Throwable) {
               CloseableKt.closeFinally(var11, var31);
            }

            CloseableKt.closeFinally(var11, null as java.lang.Throwable);
         }
      } catch (var26: Exception) {
         logger.error("解析文件内容出错: {}  文件: \n{}", var26, file);
         throw var26;
      }
   }
}

@JvmSynthetic
fun `parseJsonStringList$default`(
   var0: File, var1: java.util.Set, var2: java.util.Set, var3: Int, var4: Int, var5: java.util.Set, var6: Function1, var7: Int, var8: Any
): JsonArray {
   if ((var7 and 2) != 0) {
      var1 = null;
   }

   if ((var7 and 4) != 0) {
      var2 = null;
   }

   if ((var7 and 8) != 0) {
      var3 = 0;
   }

   if ((var7 and 16) != 0) {
      var4 = Integer.MAX_VALUE;
   }

   if ((var7 and 32) != 0) {
      var5 = null;
   }

   if ((var7 and 64) != 0) {
      var6 = null;
   }

   return parseJsonStringList(var0, var1, var2, var3, var4, var5, var6);
}

public fun asJsonObject(value: Any?): JsonObject? {
   if (value is JsonObject) {
      return value as JsonObject;
   } else if (value is java.lang.String) {
      try {
         return new JsonObject(value as java.lang.String);
      } catch (var2: Exception) {
         logger.error("解析内容出错: {}  内容: \n{}", var2, value);
         throw var2;
      }
   } else {
      return null;
   }
}

public fun <T> T.serializeToMap(): Map<String, Any> {
   return getGson()
      .fromJson(
         if (`$this$serializeToMap` is java.lang.String) `$this$serializeToMap` as java.lang.String else getGson().toJson(`$this$serializeToMap`),
         new ExtKt$serializeToMap$$inlined$convert$1().getType()
      );
}

public fun <T> T.toMap(): Map<String, Any> {
   return getGson()
      .fromJson(
         if (`$this$toMap` is java.lang.String) `$this$toMap` as java.lang.String else getGson().toJson(`$this$toMap`),
         new ExtKt$toMap$$inlined$convert$1().getType()
      );
}

@JvmSynthetic
public inline fun <reified T> Map<String, Any>.toDataClass(): T {
   val `json$iv`: java.lang.String = if (`$this$toDataClass` is java.lang.String)
      `$this$toDataClass` as java.lang.String
      else
      getGson().toJson(`$this$toDataClass`);
   val var10000: Gson = getGson();
   Intrinsics.needClassReification();
   return (T)var10000.fromJson(`json$iv`, new ExtKt$toDataClass$$inlined$convert$1().getType());
}

@JvmSynthetic
public inline fun <I, reified O> I.convert(): O {
   val json: java.lang.String = if (`$this$convert` is java.lang.String) `$this$convert` as java.lang.String else getGson().toJson(`$this$convert`);
   val var10000: Gson = getGson();
   Intrinsics.needClassReification();
   return (O)var10000.fromJson(json, (new TypeToken<O>() {}).getType());
}

@JvmSynthetic
public inline fun <reified T> Class<T>.arrayType(): Class<Any> {
   return (Class<Object>)Array.newInstance(`$this$arrayType`, 0).getClass();
}

public fun <R> readInstanceProperty(instance: Any, propertyName: String): R {
   val `$this$first$iv`: java.lang.Iterable;
   for (Object element$iv : $this$first$iv) {
      if ((`element$iv` as KProperty1).getName() == propertyName) {
         return (R)(`element$iv` as KProperty1).get(instance);
      }
   }

   throw (new NoSuchElementException("Collection contains no element matching the predicate.")) as java.lang.Throwable;
}

public fun setInstanceProperty(instance: Any, propertyName: String, propertyValue: Any) {
   val `$this$first$iv`: java.lang.Iterable;
   for (Object element$iv : $this$first$iv) {
      if ((`element$iv` as KProperty1).getName() == propertyName) {
         val property: KProperty1 = `element$iv` as KProperty1;
         if (`element$iv` as KProperty1 is KMutableProperty) {
            (property as KMutableProperty).getSetter().call(new Object[]{instance, propertyValue});
         }

         return;
      }
   }

   throw (new NoSuchElementException("Collection contains no element matching the predicate.")) as java.lang.Throwable;
}

public fun Book.fillData(newBook: Book, keys: List<String>): Book {
   for (java.lang.String key : keys) {
      val cacheValue: java.lang.CharSequence = readInstanceProperty(`$this$fillData`, key) as java.lang.String;
      if (cacheValue == null || cacheValue.length() == 0) {
         val var15: java.lang.String = readInstanceProperty(newBook, key);
         if (var15 != null && var15.length() != 0) {
            setInstanceProperty(`$this$fillData`, key, var15);
         }
      }
   }

   return `$this$fillData`;
}

public fun getRandomString(length: Int): String {
   val allowedChars: java.lang.String = "ABCDEFGHIJKLMNOPQRSTUVWXTZabcdefghiklmnopqrstuvwxyz0123456789";
   val `$this$map$iv`: java.lang.Iterable = new IntRange(1, length);
   val `destination$iv$iv`: java.util.Collection = new ArrayList(CollectionsKt.collectionSizeOrDefault(`$this$map$iv`, 10));
   val var7: java.util.Iterator = `$this$map$iv`.iterator();

   while (var7.hasNext()) {
      val `item$iv$iv`: Int = (var7 as IntIterator).nextInt();
      `destination$iv$iv`.add(StringsKt.random(allowedChars, Random.Default));
   }

   return CollectionsKt.joinToString$default(`destination$iv$iv` as java.util.List, "", null, null, 0, null, null, 62, null);
}

public fun genEncryptedPassword(password: String, salt: String): String {
   return MD5Utils.INSTANCE.md5Encode(Intrinsics.stringPlus(MD5Utils.INSTANCE.md5Encode(Intrinsics.stringPlus(password, salt)), salt)).toString();
}

public fun jsonEncode(value: Any, pretty: Boolean = false): String {
   if (pretty) {
      val var3: java.lang.String = prettyGson.toJson(value);
      return var3;
   } else {
      val var2: java.lang.String = gson.toJson(value);
      return var2;
   }
}

@JvmSynthetic
fun `jsonEncode$default`(var0: Any, var1: Boolean, var2: Int, var3: Any): java.lang.String {
   if ((var2 and 2) != 0) {
      var1 = false;
   }

   return jsonEncode(var0, var1);
}

public fun File.deepListFiles(allowExtensions: kotlin.Array<String>?): List<File> {
   val fileList: ArrayList = new ArrayList();
   val var16: Array<File> = `$this$deepListFiles`.listFiles();
   val var5: Array<Any> = var16;
   val var6: Int = var16.length;

   for (int var7 = 0; var7 < var6; var7++) {
      val it: File = var5[var7] as File;
      if ((var5[var7] as File).isDirectory()) {
         fileList.addAll(deepListFiles(it, allowExtensions));
      } else {
         val var10000: FileUtils = FileUtils.INSTANCE;
         val var11: java.lang.String = it.getName();
         val extension: java.lang.String = var10000.getExtension(var11);
         val var18: Boolean;
         if (allowExtensions == null) {
            var18 = false;
         } else {
            val var15: java.lang.String = ArraysKt.contentDeepToString(allowExtensions);
            var18 = var15 != null && StringsKt.contains$default(var15, extension, false, 2, null);
         }

         if (var18 || allowExtensions == null) {
            fileList.add(it);
         }
      }
   }

   return fileList;
}

public fun getTraceId(): String {
   return UUID.randomUUID().toString().subSequence(0, 8).toString();
}

public fun setLicenseValid(isValid: Boolean) {
   _licenseValid = isValid;
}

public fun getInstalledLicense(ignoreInvalid: Boolean = false): License {
   val licenseKeyString: java.lang.String = getStorage(new java.lang.String[]{"data", "license"}, ".key");
   if (licenseKeyString == null || licenseKeyString.length() == 0) {
      return new License(null, 0, 0L, false, 0L, 0, null, null, null, false, null, 2047, null);
   } else if (!ignoreInvalid && !_licenseValid) {
      return new License(null, 0, 0L, false, 0L, 0, null, null, null, false, null, 2047, null);
   } else {
      val var6: License = decryptToLicense(licenseKeyString);
      logger.info("license: {}", var6);
      return if (var6 != null && var6.getVerified()) var6 else new License(null, 0, 0L, false, 0L, 0, null, null, null, false, null, 2047, null);
   }
}

@JvmSynthetic
fun `getInstalledLicense$default`(var0: Boolean, var1: Int, var2: Any): License {
   if ((var1 and 1) != 0) {
      var0 = false;
   }

   return getInstalledLicense(var0);
}

public fun decryptToLicense(content: String): License? {
   if (content.length() == 0) {
      return null;
   } else {
      val var13: java.lang.String = decryptData(content);
      val var10000: License;
      if (var13 == null) {
         var10000 = null;
      } else {
         val `$this$toDataClass$iv`: java.util.Map = toMap(var13);
         val var14: License = getGson()
            .fromJson(
               if (`$this$toDataClass$iv` is java.lang.String) `$this$toDataClass$iv` as java.lang.String else getGson().toJson(`$this$toDataClass$iv`),
               new ExtKt$decryptToLicense$lambda-19$$inlined$toDataClass$1().getType()
            );
         var10000 = if (var14 == null) null else var14;
      }

      return var10000;
   }
}

public fun decryptData(content: String): String? {
   val publicKey: PublicKey = KeyFactory.getInstance("RSA")
      .generatePublic(
         new X509EncodedKeySpec(
            Base64.decode(
               "MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEAj0G3qEPjVTvVd7pXFUVYZFHT8KaoG4onc5rLUKqFQ2DCh/5hFK9t2nKh2XB+C2Jp/GSK2ONwD7ceXenmA6uvr90uCK/gp6j62XFVRvc8sIm0d/bGbzZFJRk3HKtxEckBmASduPObY691DVVixxNtUrSJktx/TZaB42pUQk4j+7FuOVNNPra44hDdnyGhmYBBf2B4kjXVMjL+0NCblFIN1+qjmcol44k6NFKFF54q05bjR3CRyYdAnNTCOyt9va0oB6lDlKHplSZmAOH9JGMUki/HDJbABESXMnyIpux27w9SQ8aJStYttnJWHALO1hiFJsxbz5KUkldH6Ny1p/2W5QIDAQAB",
               2
            )
         )
      );
   val var10000: EncoderUtils = EncoderUtils.INSTANCE;
   return EncoderUtils.decryptSegmentByPublicKey$default(var10000, content, publicKey, 0, 4, null);
}

public fun validateEmail(email: String): Boolean {
   return new Regex("^[A-Za-z0-9._%+-]+@(163|126|qq|yahoo|sina|sohu|yeah|139|189|21cn|outlook|gmail|icloud).com$").matches(email);
}

public fun sendEmail(toEmail: String, subject: String, body: String): Boolean {
   val host: java.lang.String = "smtp.qiye.aliyun.com";
   val port: Int = 465;
   val sendCommand: Function3 = <unrepresentable>.INSTANCE;

   try {
      val socket: Socket = SSLSocketFactory.getDefault().createSocket(host, port);
      val writer: OutputStream = socket.getOutputStream();
      val var18: OutputStreamWriter = new OutputStreamWriter(writer);
      val response: InputStream = socket.getInputStream();
      val var14: Reader = new InputStreamReader(response, Charsets.UTF_8);
      val reader: BufferedReader = if (var14 is BufferedReader) var14 as BufferedReader else new BufferedReader(var14, 8192);
      val var19: java.lang.String = reader.readLine();
      if (!StringsKt.startsWith$default(var19, "220", false, 2, null)) {
         logger.error("Error connecting to the SMTP server.");
         return false;
      } else {
         val var20: java.util.List = getCommand(CollectionsKt.arrayListOf(new java.lang.String[]{toEmail}), subject, body);
         var var22: Boolean = false;
         var var23: Int = 0;
         val var25: Int = var20.size();
         if (0 < var25) {
            do {
               var22 = sendCommand.invoke(var18, reader, var20.get(var23++)) as java.lang.Boolean;
            } while (res && var23 < var25);
         }

         var18.close();
         reader.close();
         socket.close();
         return var22;
      }
   } catch (var17: Exception) {
      var17.printStackTrace();
      return false;
   }
}

public fun getCommand(to: List<String>, subject: String, body: String): List<Pair<String, Int>> {
   val separator: java.lang.String = "----=_Part_${System.currentTimeMillis()}${UUID.randomUUID()}";
   val command: java.util.List = CollectionsKt.mutableListOf(new Pair[]{new Pair<>("HELO sendmail\r\n", 250)});
   if ("no-reply@onmy.top".length() != 0) {
      command.add(new Pair<>("AUTH LOGIN\r\n", 334));
      command.add(new Pair<>(Intrinsics.stringPlus(encodeBase64("no-reply@onmy.top"), "\r\n"), 334));
      command.add(new Pair<>(Intrinsics.stringPlus(encodeBase64("no-reply@1."), "\r\n"), 235));
   }

   command.add(new Pair<>("MAIL FROM: <no-reply@onmy.top>\r\n", 250));
   var var14: java.lang.String = "FROM: Reader<no-reply@onmy.top>\r\n";
   if (!to.isEmpty()) {
      val var26: Int = to.size();
      if (var26 == 1) {
         command.add(new Pair<>("RCPT TO: <${to.get(0) as java.lang.String}>\r\n", 250));
         var14 = "$var14TO: <${to.get(0) as java.lang.String}>\r\n";
      } else {
         var var28: Int = 0;
         if (0 < var26) {
            do {
               val var29: Int = var28++;
               command.add(new Pair<>("RCPT TO: <${to.get(var29) as java.lang.String}>\r\n", 250));
               if (var29 == 0) {
                  var14 = "$var14TO: <${to.get(var29) as java.lang.String}>";
               } else if (var29 + 1 == var26) {
                  var14 = "$var14,<${to.get(var29) as java.lang.String}>\r\n";
               } else {
                  var14 = "$var14,<${to.get(var29) as java.lang.String}>";
               }
            } while (var28 < count);
         }
      }
   }

   var14 = Intrinsics.stringPlus(
      "${Intrinsics.stringPlus(
         Intrinsics.stringPlus(
            "${Intrinsics.stringPlus(
               "${Intrinsics.stringPlus("$var14Subject: =?UTF-8?B?${encodeBase64(subject)}?=\r\n", "Content-Type: multipart/alternative;\r\n")}\tboundary=\"$separator"",
               "\r\nMIME-Version: 1.0\r\n"
            )}\r\n--$separator\r\n",
            "Content-Type:text/html; charset=utf-8\r\n"
         ),
         "Content-Transfer-Encoding: base64\r\n\r\n"
      )}${encodeBase64(body)}\r\n--$separator\r\n",
      "\r\n.\r\n"
   );
   command.add(new Pair<>("DATA\r\n", 354));
   command.add(new Pair<>(var14, 250));
   command.add(new Pair<>("QUIT\r\n", 221));
   return command;
}

public fun encodeBase64(text: String): String {
   val var10000: Encoder = java.util.Base64.getEncoder();
   val var10001: ByteArray = text.getBytes(Charsets.UTF_8);
   val var1: java.lang.String = var10000.encodeToString(var10001);
   return var1;
}
