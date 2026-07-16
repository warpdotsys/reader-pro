package io.legado.app.utils

import java.io.BufferedInputStream
import java.io.BufferedOutputStream
import java.io.Closeable
import java.io.File
import java.io.FileInputStream
import java.io.FileOutputStream
import java.io.IOException
import java.util.ArrayList
import java.util.Enumeration
import java.util.zip.ZipEntry
import java.util.zip.ZipFile
import java.util.zip.ZipOutputStream
import kotlin.coroutines.Continuation
import kotlin.coroutines.intrinsics.IntrinsicsKt
import kotlin.coroutines.jvm.internal.Boxing
import kotlin.jvm.functions.Function2
import kotlin.jvm.internal.Intrinsics
import kotlinx.coroutines.BuildersKt
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import org.jetbrains.annotations.NotNull
import org.jetbrains.annotations.Nullable

public object ZipUtils {
   public suspend fun zipFiles(srcFiles: Collection<String>, zipFilePath: String): Boolean {
      return this.zipFiles(srcFiles, zipFilePath, null, `$completion`);
   }

   public suspend fun zipFiles(srcFilePaths: Collection<String>?, zipFilePath: String?, comment: String?): Boolean {
      return BuildersKt.withContext(
         Dispatchers.getIO(), (new Function2<CoroutineScope, Continuation<? super java.lang.Boolean>, Object>(srcFilePaths, zipFilePath, comment, null) {
            int label;

            {
               super(2, `$completionx`);
               this.$srcFilePaths = `$srcFilePaths`;
               this.$zipFilePath = `$zipFilePath`;
               this.$comment = `$comment`;
            }

            @Nullable
            @Override
            public final Object invokeSuspend(@NotNull Object $result) {
               val var13: Any = IntrinsicsKt.getCOROUTINE_SUSPENDED();
               switch (this.label) {
                  case 0:
                     ResultKt.throwOnFailure(`$result`);
                     label43:
                     if (this.$srcFilePaths != null && this.$zipFilePath != null) {
                        val var2: Closeable = new ZipOutputStream(new FileOutputStream(this.$zipFilePath));
                        val var3: java.util.Collection = this.$srcFilePaths;
                        val var4: java.lang.String = this.$comment;
                        var var18: java.lang.Throwable = null as java.lang.Throwable;

                        label39: {
                           try {
                              try {
                                 val it: ZipOutputStream = var2 as ZipOutputStream;

                                 for (java.lang.String srcFile : var3) {
                                    val var10000: ZipUtils = ZipUtils.INSTANCE;
                                    val var10001: File = ZipUtils.access$getFileByPath(ZipUtils.INSTANCE, srcFile);
                                    if (!ZipUtils.access$zipFile(var10000, var10001, "", it, var4)) {
                                       val var12: java.lang.Boolean = Boxing.boxBoolean(false);
                                       break label39;
                                    }
                                 }

                                 val var11: java.lang.Boolean = Boxing.boxBoolean(true);
                              } catch (var14: java.lang.Throwable) {
                                 var18 = var14;
                                 throw var14;
                              }
                           } catch (var15: java.lang.Throwable) {
                              CloseableKt.closeFinally(var2, var18);
                           }

                           CloseableKt.closeFinally(var2, null as java.lang.Throwable);
                        }

                        CloseableKt.closeFinally(var2, null as java.lang.Throwable);
                     } else {
                        return Boxing.boxBoolean(false);
                     }
                  default:
                     throw new IllegalStateException("call to 'resume' before 'invoke' with coroutine");
               }
            }

            @NotNull
            @Override
            public final Continuation<Unit> create(@Nullable Object value, @NotNull Continuation<?> $completion) {
               return new <anonymous constructor>(this.$srcFilePaths, this.$zipFilePath, this.$comment, `$completion`);
            }

            @Nullable
            public final Object invoke(@NotNull CoroutineScope p1, @Nullable Continuation<? super java.lang.Boolean> p2) {
               return (this.create(p1, p2) as <unrepresentable>).invokeSuspend(Unit.INSTANCE);
            }
         }) as Function2, `$completion`
      );
   }

   @JvmOverloads
   @Throws(java/io/IOException::class)
   public fun zipFiles(srcFiles: Collection<File>?, zipFile: File?, comment: String? = null): Boolean {
      label39:
      if (srcFiles != null && zipFile != null) {
         val var4: Closeable = new ZipOutputStream(new FileOutputStream(zipFile));
         var var17: java.lang.Throwable = null as java.lang.Throwable;

         label35: {
            try {
               try {
                  val it: ZipOutputStream = var4 as ZipOutputStream;

                  for (File srcFile : srcFiles) {
                     if (!INSTANCE.zipFile(srcFile, "", it, comment)) {
                        break label35;
                     }
                  }
               } catch (var13: java.lang.Throwable) {
                  var17 = var13;
                  throw var13;
               }
            } catch (var14: java.lang.Throwable) {
               CloseableKt.closeFinally(var4, var17);
            }

            CloseableKt.closeFinally(var4, null as java.lang.Throwable);
         }

         CloseableKt.closeFinally(var4, null as java.lang.Throwable);
      } else {
         return false;
      }
   }

   @Throws(java/io/IOException::class)
   public fun zipFile(srcFilePath: String, zipFilePath: String): Boolean {
      return this.zipFile(this.getFileByPath(srcFilePath), this.getFileByPath(zipFilePath), null);
   }

   @Throws(java/io/IOException::class)
   public fun zipFile(srcFilePath: String, zipFilePath: String, comment: String): Boolean {
      return this.zipFile(this.getFileByPath(srcFilePath), this.getFileByPath(zipFilePath), comment);
   }

   @JvmOverloads
   @Throws(java/io/IOException::class)
   public fun zipFile(srcFile: File?, zipFile: File?, comment: String? = null): Boolean {
      label22:
      if (srcFile != null && zipFile != null) {
         val var4: Closeable = new ZipOutputStream(new FileOutputStream(zipFile));
         var var14: java.lang.Throwable = null as java.lang.Throwable;

         try {
            try {
               val var9: Boolean = INSTANCE.zipFile(srcFile, "", var4 as ZipOutputStream, comment);
            } catch (var10: java.lang.Throwable) {
               var14 = var10;
               throw var10;
            }
         } catch (var11: java.lang.Throwable) {
            CloseableKt.closeFinally(var4, var14);
         }

         CloseableKt.closeFinally(var4, null as java.lang.Throwable);
      } else {
         return false;
      }
   }

   @Throws(java/io/IOException::class)
   private fun zipFile(srcFile: File, rootPath: String, zos: ZipOutputStream, comment: String?): Boolean {
      if (!srcFile.exists()) {
         return true;
      } else {
         label80: {
            val var16: java.lang.String = "$rootPath${if (this.isSpace(rootPath)) "" else File.separator}${srcFile.getName()}";
            if (srcFile.isDirectory()) {
               val fileList: Array<File> = srcFile.listFiles();
               if (fileList != null && fileList.length != 0) {
                  val var18: Array<File> = fileList;
                  var var20: Int = 0;
                  val `is`: Int = fileList.length;

                  val var10: File;
                  do {
                     if (var20 >= `is`) {
                        return true;
                     }

                     var10 = var18[var20];
                     var20++;
                  } while (this.zipFile(file, var16, zos, comment));

                  return false;
               } else {
                  val entry: ZipEntry = new ZipEntry(Intrinsics.stringPlus(var16, "/"));
                  entry.setComment(comment);
                  zos.putNextEntry(entry);
                  zos.closeEntry();
               }
            } else {
               val var17: Closeable = new BufferedInputStream(new FileInputStream(srcFile));
               var var22: java.lang.Throwable = null as java.lang.Throwable;

               try {
                  try {
                     val var23: BufferedInputStream = var17 as BufferedInputStream;
                     val entry: ZipEntry = new ZipEntry(var16);
                     entry.setComment(comment);
                     zos.putNextEntry(entry);
                     zos.write(ByteStreamsKt.readBytes(var23));
                     zos.closeEntry();
                  } catch (var12: java.lang.Throwable) {
                     var22 = var12;
                     throw var12;
                  }
               } catch (var13: java.lang.Throwable) {
                  CloseableKt.closeFinally(var17, var22);
               }

               CloseableKt.closeFinally(var17, null as java.lang.Throwable);
            }

            return true;
         }
      }
   }

   @Throws(java/io/IOException::class)
   public fun unzipFile(zipFilePath: String, destDirPath: String): List<File>? {
      return this.unzipFileByKeyword(zipFilePath, destDirPath, null);
   }

   @Throws(java/io/IOException::class)
   public fun unzipFile(zipFile: File, destDir: File): List<File>? {
      return this.unzipFileByKeyword(zipFile, destDir, null);
   }

   @Throws(java/io/IOException::class)
   public fun unzipFileByKeyword(zipFilePath: String, destDirPath: String, keyword: String?): List<File>? {
      return this.unzipFileByKeyword(this.getFileByPath(zipFilePath), this.getFileByPath(destDirPath), keyword);
   }

   @Throws(java/io/IOException::class)
   public fun unzipFileByKeyword(zipFile: File?, destDir: File?, keyword: String?): List<File>? {
      label69:
      if (zipFile != null && destDir != null) {
         val files: ArrayList = new ArrayList();
         val zip: ZipFile = new ZipFile(zipFile);
         val entries: Enumeration = zip.entries();
         val var7: Closeable = zip;
         var var20: java.lang.Throwable = null as java.lang.Throwable;

         label65: {
            label64: {
               try {
                  try {
                     val it: ZipFile = var7 as ZipFile;
                     if (INSTANCE.isSpace(keyword)) {
                        while (entries.hasMoreElements()) {
                           var var23: java.lang.String = (java.lang.String)entries.nextElement();
                           if (var23 == null) {
                              throw new NullPointerException("null cannot be cast to non-null type java.util.zip.ZipEntry");
                           }

                           val var25: ZipEntry = var23 as ZipEntry;
                           var23 = (var23 as ZipEntry).getName();
                           if (StringsKt.contains$default(var23, "../", false, 2, null)) {
                              ZipUtilsKt.access$getLogger$p().error("ZipUtils entryName: $var23 is dangerous!");
                           } else if (!INSTANCE.unzipChildFile(destDir, files, zip, var25, var23)) {
                              val var15: java.util.List = files;
                              break label65;
                           }
                        }
                     } else {
                        while (entries.hasMoreElements()) {
                           var entryNamex: java.lang.String = (java.lang.String)entries.nextElement();
                           if (entryNamex == null) {
                              throw new NullPointerException("null cannot be cast to non-null type java.util.zip.ZipEntry");
                           }

                           val entry: ZipEntry = entryNamex as ZipEntry;
                           entryNamex = (entryNamex as ZipEntry).getName();
                           if (StringsKt.contains$default(entryNamex, "../", false, 2, null)) {
                              ZipUtilsKt.access$getLogger$p().error("ZipUtils entryName: $entryNamex is dangerous!");
                           } else {
                              val var10000: java.lang.CharSequence = entryNamex;
                              if (StringsKt.contains$default(var10000, keyword, false, 2, null)
                                 && !INSTANCE.unzipChildFile(destDir, files, zip, entry, entryNamex)) {
                                 val var14: java.util.List = files;
                                 break label64;
                              }
                           }
                        }
                     }
                  } catch (var16: java.lang.Throwable) {
                     var20 = var16;
                     throw var16;
                  }
               } catch (var17: java.lang.Throwable) {
                  CloseableKt.closeFinally(var7, var20);
               }

               CloseableKt.closeFinally(var7, null as java.lang.Throwable);
            }

            CloseableKt.closeFinally(var7, null as java.lang.Throwable);
         }

         CloseableKt.closeFinally(var7, null as java.lang.Throwable);
      } else {
         return null;
      }
   }

   @Throws(java/io/IOException::class)
   private fun unzipChildFile(destDir: File, files: MutableList<File>, zip: ZipFile, entry: ZipEntry, name: String): Boolean {
      val file: File = new File(destDir, name);
      files.add(file);
      if (entry.isDirectory()) {
         return this.createOrExistsDir(file);
      } else if (!this.createOrExistsFile(file)) {
         return false;
      } else {
         label77: {
            val var7: Closeable = new BufferedInputStream(zip.getInputStream(entry));
            var var29: java.lang.Throwable = null as java.lang.Throwable;

            try {
               try {
                  val `in`: BufferedInputStream = var7 as BufferedInputStream;
                  val var12: Closeable = new BufferedOutputStream(new FileOutputStream(file));
                  var var30: java.lang.Throwable = null as java.lang.Throwable;

                  try {
                     try {
                        (var12 as BufferedOutputStream).write(ByteStreamsKt.readBytes(`in`));
                     } catch (var17: java.lang.Throwable) {
                        var30 = var17;
                        throw var17;
                     }
                  } catch (var18: java.lang.Throwable) {
                     CloseableKt.closeFinally(var12, var30);
                  }

                  CloseableKt.closeFinally(var12, null as java.lang.Throwable);
               } catch (var19: java.lang.Throwable) {
                  var29 = var19;
                  throw var19;
               }
            } catch (var20: java.lang.Throwable) {
               CloseableKt.closeFinally(var7, var29);
            }

            CloseableKt.closeFinally(var7, null as java.lang.Throwable);
         }
      }
   }

   @Throws(java/io/IOException::class)
   public fun getFilesPath(zipFilePath: String): List<String>? {
      return this.getFilesPath(this.getFileByPath(zipFilePath));
   }

   @Throws(java/io/IOException::class)
   public fun getFilesPath(zipFile: File?): List<String>? {
      if (zipFile == null) {
         return null;
      } else {
         val paths: ArrayList = new ArrayList();
         val zip: ZipFile = new ZipFile(zipFile);
         val entries: Enumeration = zip.entries();

         while (entries.hasMoreElements()) {
            val var6: Any = entries.nextElement();
            if (var6 == null) {
               throw new NullPointerException("null cannot be cast to non-null type java.util.zip.ZipEntry");
            }

            val entryName: java.lang.String = (var6 as ZipEntry).getName();
            if (StringsKt.contains$default(entryName, "../", false, 2, null)) {
               ZipUtilsKt.access$getLogger$p().error("ZipUtils entryName: $entryName is dangerous!");
               paths.add(entryName);
            } else {
               paths.add(entryName);
            }
         }

         zip.close();
         return paths;
      }
   }

   @Throws(java/io/IOException::class)
   public fun getComments(zipFilePath: String): List<String>? {
      return this.getComments(this.getFileByPath(zipFilePath));
   }

   @Throws(java/io/IOException::class)
   public fun getComments(zipFile: File?): List<String>? {
      if (zipFile == null) {
         return null;
      } else {
         val comments: ArrayList = new ArrayList();
         val zip: ZipFile = new ZipFile(zipFile);
         val entries: Enumeration = zip.entries();

         while (entries.hasMoreElements()) {
            val var6: Any = entries.nextElement();
            if (var6 == null) {
               throw new NullPointerException("null cannot be cast to non-null type java.util.zip.ZipEntry");
            }

            comments.add((var6 as ZipEntry).getComment());
         }

         zip.close();
         return comments;
      }
   }

   private fun createOrExistsDir(file: File?): Boolean {
      return file != null && (if (file.exists()) file.isDirectory() else file.mkdirs());
   }

   private fun createOrExistsFile(file: File?): Boolean {
      if (file == null) {
         return false;
      } else if (file.exists()) {
         return file.isFile();
      } else if (!this.createOrExistsDir(file.getParentFile())) {
         return false;
      } else {
         var var2: Boolean;
         try {
            var2 = file.createNewFile();
         } catch (var4: IOException) {
            var4.printStackTrace();
            var2 = false;
         }

         return var2;
      }
   }

   private fun getFileByPath(filePath: String): File? {
      return if (this.isSpace(filePath)) null else new File(filePath);
   }

   private fun isSpace(s: String?): Boolean {
      if (s == null) {
         return true;
      } else {
         var i: Int = 0;

         for (int len = s.length(); i < len; i++) {
            if (!Character.isWhitespace(s.charAt(i))) {
               return false;
            }
         }

         return true;
      }
   }

   @JvmOverloads
   @Throws(java/io/IOException::class)
   fun zipFiles(srcFiles: MutableCollection<File>?, zipFile: File?): Boolean {
      return zipFiles$default(this, srcFiles, zipFile, null, 4, null);
   }

   @JvmOverloads
   @Throws(java/io/IOException::class)
   fun zipFile(srcFile: File?, zipFile: File?): Boolean {
      return zipFile$default(this, srcFile, zipFile, null, 4, null);
   }
}
