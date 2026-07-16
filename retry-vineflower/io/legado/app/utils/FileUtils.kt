package io.legado.app.utils

import java.io.BufferedInputStream
import java.io.BufferedOutputStream
import java.io.ByteArrayOutputStream
import java.io.Closeable
import java.io.File
import java.io.FileInputStream
import java.io.FileOutputStream
import java.io.FileWriter
import java.io.IOException
import java.io.InputStream
import java.io.UnsupportedEncodingException
import java.lang.annotation.RetentionPolicy
import java.nio.charset.Charset
import java.text.DecimalFormat
import java.text.SimpleDateFormat
import java.util.ArrayList
import java.util.Arrays
import java.util.Calendar
import java.util.Collections
import java.util.Comparator
import java.util.Locale
import java.util.regex.Matcher
import java.util.regex.Pattern
import kotlin.jvm.internal.Intrinsics

public object FileUtils {
   public const val BY_EXTENSION_ASC: Int = 6
   public const val BY_EXTENSION_DESC: Int = 7
   public const val BY_NAME_ASC: Int = 0
   public const val BY_NAME_DESC: Int = 1
   public const val BY_SIZE_ASC: Int = 4
   public const val BY_SIZE_DESC: Int = 5
   public const val BY_TIME_ASC: Int = 2
   public const val BY_TIME_DESC: Int = 3
   public const val GB: Long = 1073741824L
   public const val KB: Long = 1024L
   public const val MB: Long = 1048576L

   public fun exists(root: File, vararg subDirFiles: String): Boolean {
      return this.getFile(root, Arrays.copyOf(subDirFiles, subDirFiles.length)).exists();
   }

   public fun createFileIfNotExist(root: File, vararg subDirFiles: String): File {
      return this.createFileIfNotExist(this.getPath(root, Arrays.copyOf(subDirFiles, subDirFiles.length)));
   }

   public fun createFolderIfNotExist(root: File, vararg subDirs: String): File {
      return this.createFolderIfNotExist(this.getPath(root, Arrays.copyOf(subDirs, subDirs.length)));
   }

   public fun createFolderIfNotExist(filePath: String): File {
      val file: File = new File(filePath);
      if (!file.exists()) {
         file.mkdirs();
      }

      return file;
   }

   @Synchronized
   public fun createFileIfNotExist(filePath: String): File {
      val file: File = new File(filePath);

      try {
         if (!file.exists()) {
            val e: java.lang.String = file.getParent();
            if (e != null) {
               INSTANCE.createFolderIfNotExist(e);
            }

            file.createNewFile();
         }
      } catch (var9: IOException) {
         var9.printStackTrace();
      }

      return file;
   }

   public fun createFileWithReplace(filePath: String): File {
      val file: File = new File(filePath);
      if (!file.exists()) {
         val var3: java.lang.String = file.getParent();
         if (var3 != null) {
            INSTANCE.createFolderIfNotExist(var3);
         }

         file.createNewFile();
      } else {
         file.delete();
         file.createNewFile();
      }

      return file;
   }

   public fun getFile(root: File, vararg subDirFiles: String): File {
      return new File(this.getPath(root, Arrays.copyOf(subDirFiles, subDirFiles.length)));
   }

   public fun getPath(root: File, vararg subDirFiles: String): String {
      val path: StringBuilder = new StringBuilder(root.getAbsolutePath());
      val var6: Array<java.lang.String> = subDirFiles;
      val var7: Int = subDirFiles.length;

      for (int var8 = 0; var8 < var7; var8++) {
         val `element$iv`: Any = var6[var8];
         if (var6[var8].length() > 0) {
            path.append(File.separator).append((java.lang.String)`element$iv`);
         }
      }

      val `$this$forEach$iv`: java.lang.String = path.toString();
      return `$this$forEach$iv`;
   }

   @Synchronized
   public fun deleteFile(filePath: String) {
      val file: File = new File(filePath);
      if (file.exists()) {
         if (file.isDirectory()) {
            val files: Array<File> = file.listFiles();
            if (files != null) {
               val var7: Array<File> = files;
               val var8: Int = files.length;

               for (int var9 = 0; var9 < var8; var9++) {
                  val path: java.lang.String = var7[var9].getPath();
                  val var10000: FileUtils = INSTANCE;
                  var10000.deleteFile(path);
               }
            }
         }

         file.delete();
      }
   }

   public fun getCachePath(): String {
      throw new Exception("Not implemented");
   }

   public fun separator(path: String): String {
      val separator: java.lang.String = File.separator;
      var path1: java.lang.String = StringsKt.replace$default(path, "\\", separator, false, 4, null);
      if (!StringsKt.endsWith$default(path1, separator, false, 2, null)) {
         path1 = Intrinsics.stringPlus(path1, separator);
      }

      return path1;
   }

   public fun closeSilently(c: Closeable?) {
      if (c != null) {
         try {
            c.close();
         } catch (var3: IOException) {
         }
      }
   }

   @JvmOverloads
   public fun listDirs(startDirPath: String, excludeDirs: Array<String>? = null, sortType: Int = 0): Array<File> {
      var excludeDirs1: Array<java.lang.String> = excludeDirs;
      val dirList: ArrayList = new ArrayList();
      val startDir: File = new File(startDirPath);
      if (!startDir.isDirectory()) {
         return new File[0];
      } else {
         val `$this$toTypedArray$iv`: Array<File> = startDir.listFiles(FileUtils::listDirs$lambda-4);
         if (`$this$toTypedArray$iv` == null) {
            return new File[0];
         } else {
            if (excludeDirs == null) {
               excludeDirs1 = new java.lang.String[0];
            }

            val var15: Array<File> = `$this$toTypedArray$iv`;
            var `$i$f$toTypedArray`: Int = 0;
            val `thisCollection$iv`: Int = `$this$toTypedArray$iv`.length;

            while ($i$f$toTypedArray < thisCollection$iv) {
               val dir: File = var15[`$i$f$toTypedArray`];
               `$i$f$toTypedArray`++;
               val file: File = dir.getAbsoluteFile();
               val var10000: java.lang.CharSequence = ArraysKt.contentDeepToString(excludeDirs1);
               val var13: java.lang.String = file.getName();
               if (!StringsKt.contains$default(var10000, var13, false, 2, null)) {
                  dirList.add(file);
               }
            }

            switch (sortType) {
               case 0:
                  Collections.sort(dirList, new FileUtils.SortByName());
                  break;
               case 1:
                  Collections.sort(dirList, new FileUtils.SortByName());
                  CollectionsKt.reverse(dirList);
                  break;
               case 2:
                  Collections.sort(dirList, new FileUtils.SortByTime());
                  break;
               case 3:
                  Collections.sort(dirList, new FileUtils.SortByTime());
                  CollectionsKt.reverse(dirList);
                  break;
               case 4:
                  Collections.sort(dirList, new FileUtils.SortBySize());
                  break;
               case 5:
                  Collections.sort(dirList, new FileUtils.SortBySize());
                  CollectionsKt.reverse(dirList);
                  break;
               case 6:
                  Collections.sort(dirList, new FileUtils.SortByExtension());
                  break;
               case 7:
                  Collections.sort(dirList, new FileUtils.SortByExtension());
                  CollectionsKt.reverse(dirList);
               default:
            }

            val var18: Array<Any> = dirList.toArray(new File[0]);
            if (var18 == null) {
               throw new NullPointerException("null cannot be cast to non-null type kotlin.Array<T>");
            } else {
               return var18 as Array<File>;
            }
         }
      }
   }

   @JvmOverloads
   public fun listDirsAndFiles(startDirPath: String, allowExtensions: Array<String>? = null): Array<File>? {
      val files: Array<File> = if (allowExtensions == null)
         listFiles$default(this, startDirPath, null, 0, 6, null)
         else
         this.listFiles(startDirPath, allowExtensions);
      return if (files == null) null else ArraysKt.plus(listDirs$default(this, startDirPath, null, 0, 6, null), files);
   }

   @JvmOverloads
   public fun listFiles(startDirPath: String, filterPattern: Pattern? = null, sortType: Int = 0): Array<File> {
      val fileList: ArrayList = new ArrayList();
      val f: File = new File(startDirPath);
      if (!f.isDirectory()) {
         return new File[0];
      } else {
         val `$this$toTypedArray$iv`: Array<File> = f.listFiles(FileUtils::listFiles$lambda-5);
         if (`$this$toTypedArray$iv` == null) {
            return new File[0];
         } else {
            val var11: Array<File> = `$this$toTypedArray$iv`;
            var `$i$f$toTypedArray`: Int = 0;
            val `thisCollection$iv`: Int = `$this$toTypedArray$iv`.length;

            while ($i$f$toTypedArray < thisCollection$iv) {
               val file: File = var11[`$i$f$toTypedArray`];
               `$i$f$toTypedArray`++;
               fileList.add(file.getAbsoluteFile());
            }

            switch (sortType) {
               case 0:
                  Collections.sort(fileList, new FileUtils.SortByName());
                  break;
               case 1:
                  Collections.sort(fileList, new FileUtils.SortByName());
                  CollectionsKt.reverse(fileList);
                  break;
               case 2:
                  Collections.sort(fileList, new FileUtils.SortByTime());
                  break;
               case 3:
                  Collections.sort(fileList, new FileUtils.SortByTime());
                  CollectionsKt.reverse(fileList);
                  break;
               case 4:
                  Collections.sort(fileList, new FileUtils.SortBySize());
                  break;
               case 5:
                  Collections.sort(fileList, new FileUtils.SortBySize());
                  CollectionsKt.reverse(fileList);
                  break;
               case 6:
                  Collections.sort(fileList, new FileUtils.SortByExtension());
                  break;
               case 7:
                  Collections.sort(fileList, new FileUtils.SortByExtension());
                  CollectionsKt.reverse(fileList);
               default:
            }

            val var10000: Array<Any> = fileList.toArray(new File[0]);
            if (var10000 == null) {
               throw new NullPointerException("null cannot be cast to non-null type kotlin.Array<T>");
            } else {
               return var10000 as Array<File>;
            }
         }
      }
   }

   public fun listFiles(startDirPath: String, allowExtensions: Array<String>?): Array<File>? {
      return new File(startDirPath).listFiles(FileUtils::listFiles$lambda-6);
   }

   public fun listFiles(startDirPath: String, allowExtension: String?): Array<File>? {
      return if (allowExtension == null) this.listFiles(startDirPath, null) else this.listFiles(startDirPath, new java.lang.String[]{allowExtension});
   }

   public fun exist(path: String): Boolean {
      return new File(path).exists();
   }

   @JvmOverloads
   public fun delete(file: File, deleteRootDir: Boolean = false): Boolean {
      var result: Boolean = false;
      if (file.isFile()) {
         result = this.deleteResolveEBUSY(file);
      } else {
         val var5: Array<File> = file.listFiles();
         if (var5 == null) {
            return false;
         }

         if (var5.length != 0) {
            val var9: Array<File> = var5;
            var var10: Int = 0;
            val var7: Int = var5.length;

            while (var10 < var7) {
               val f: File = var9[var10];
               var10++;
               this.delete(f, deleteRootDir);
               result = this.deleteResolveEBUSY(f);
            }
         } else {
            result = deleteRootDir && this.deleteResolveEBUSY(file);
         }

         if (deleteRootDir) {
            result = this.deleteResolveEBUSY(file);
         }
      }

      return result;
   }

   private fun deleteResolveEBUSY(file: File): Boolean {
      val to: File = new File(Intrinsics.stringPlus(file.getAbsolutePath(), System.currentTimeMillis()));
      file.renameTo(to);
      return to.delete();
   }

   @JvmOverloads
   public fun delete(path: String, deleteRootDir: Boolean = false): Boolean {
      val file: File = new File(path);
      return file.exists() && this.delete(file, deleteRootDir);
   }

   public fun copy(src: String, tar: String): Boolean {
      val srcFile: File = new File(src);
      return srcFile.exists() && this.copy(srcFile, new File(tar));
   }

   public fun copy(src: File, tar: File): Boolean {
      try {
         if (src.isFile()) {
            val e: FileInputStream = new FileInputStream(src);
            val `$this$forEach$iv`: FileOutputStream = new FileOutputStream(tar);
            val `$i$f$forEach`: BufferedInputStream = new BufferedInputStream(e);
            val bos: BufferedOutputStream = new BufferedOutputStream(`$this$forEach$iv`);
            val bt: ByteArray = new byte[8192];

            while (true) {
               val len: Int = `$i$f$forEach`.read(bt);
               if (len == -1) {
                  `$i$f$forEach`.close();
                  bos.close();
                  break;
               }

               bos.write(bt, 0, len);
            }
         } else if (src.isDirectory()) {
            tar.mkdirs();
            val var14: Array<File> = src.listFiles();
            if (var14 != null) {
               val var16: Array<File> = var14;
               val var17: Int = var14.length;

               for (int var18 = 0; var18 < var17; var18++) {
                  val `element$iv`: Any = var16[var18];
                  val var10000: FileUtils = INSTANCE;
                  val var12: File = `element$iv`.getAbsoluteFile();
                  var10000.copy(var12, new File(tar.getAbsoluteFile(), `element$iv`.getName()));
               }
            }
         }

         return true;
      } catch (var13: Exception) {
         return false;
      }
   }

   public fun move(src: String, tar: String): Boolean {
      return this.move(new File(src), new File(tar));
   }

   public fun move(src: File, tar: File): Boolean {
      return this.rename(src, tar);
   }

   public fun rename(oldPath: String, newPath: String): Boolean {
      return this.rename(new File(oldPath), new File(newPath));
   }

   public fun rename(src: File, tar: File): Boolean {
      return src.renameTo(tar);
   }

   @JvmOverloads
   public fun readText(filepath: String, charset: String = "utf-8"): String {
      try {
         val data: ByteArray = this.readBytes(filepath);
         if (data != null) {
            val `$this$trim$iv`: Charset = Charset.forName(charset);
            val `$this$trim$iv$iv`: java.lang.CharSequence = new java.lang.String(data, `$this$trim$iv`);
            var `startIndex$iv$iv`: Int = 0;
            var `endIndex$iv$iv`: Int = `$this$trim$iv$iv`.length() - 1;
            var `startFound$iv$iv`: Boolean = false;

            while (startIndex$iv$iv <= endIndex$iv$iv) {
               val var17: Boolean = Intrinsics.compare(`$this$trim$iv$iv`.charAt(if (!`startFound$iv$iv`) `startIndex$iv$iv` else `endIndex$iv$iv`), 32) <= 0;
               if (!`startFound$iv$iv`) {
                  if (!var17) {
                     `startFound$iv$iv` = true;
                  } else {
                     `startIndex$iv$iv`++;
                  }
               } else {
                  if (!var17) {
                     break;
                  }

                  `endIndex$iv$iv`--;
               }
            }

            return `$this$trim$iv$iv`.subSequence(`startIndex$iv$iv`, `endIndex$iv$iv` + 1).toString();
         }
      } catch (var14: UnsupportedEncodingException) {
      }

      return "";
   }

   public fun readBytes(filepath: String): ByteArray? {
      label31: {
         var fis: FileInputStream = null;

         label28: {
            try {
               try {
                  fis = new FileInputStream(filepath);
                  val e: ByteArrayOutputStream = new ByteArrayOutputStream();
                  val var11: ByteArray = new byte[1024];

                  while (true) {
                     val data: Int = fis.read(var11, 0, var11.length);
                     if (data == -1) {
                        val var12: ByteArray = e.toByteArray();
                        e.close();
                        break label28;
                     }

                     e.write(var11, 0, data);
                  }
               } catch (var7: IOException) {
               }
            } catch (var8: java.lang.Throwable) {
               this.closeSilently(fis);
            }

            this.closeSilently(fis);
         }

         this.closeSilently(fis);
      }
   }

   @JvmOverloads
   public fun writeText(filepath: String, content: String, charset: String = "utf-8"): Boolean {
      var var4: Boolean;
      try {
         val var10002: Charset = Charset.forName(charset);
         val var9: ByteArray = content.getBytes(var10002);
         var4 = this.writeBytes(filepath, var9);
      } catch (var7: UnsupportedEncodingException) {
         var4 = false;
      }

      return var4;
   }

   public fun writeBytes(filepath: String, data: ByteArray): Boolean {
      label54: {
         val file: File = new File(filepath);
         var fos: FileOutputStream = null;

         var var12: Boolean;
         label55: {
            try {
               try {
                  if (!file.exists()) {
                     val var11: File = file.getParentFile();
                     if (var11 != null) {
                        var11.mkdirs();
                     }

                     file.createNewFile();
                  }

                  fos = new FileOutputStream(filepath);
                  fos.write(data);
                  var12 = true;
                  break label55;
               } catch (var7: IOException) {
                  var12 = false;
               }
            } catch (var8: java.lang.Throwable) {
               this.closeSilently(fos);
            }

            this.closeSilently(fos);
            return var12;
         }

         this.closeSilently(fos);
         return var12;
      }
   }

   public fun writeInputStream(filepath: String, data: InputStream): Boolean {
      return this.writeInputStream(new File(filepath), data);
   }

   public fun writeInputStream(file: File, data: InputStream): Boolean {
      label66: {
         var fos: FileOutputStream = null;

         var buffer: Boolean;
         label67: {
            try {
               try {
                  if (!file.exists()) {
                     val var10: File = file.getParentFile();
                     if (var10 != null) {
                        var10.mkdirs();
                     }

                     file.createNewFile();
                  }

                  val var11: ByteArray = new byte[4096];
                  fos = new FileOutputStream(file);

                  while (true) {
                     val e: Int = data.read(var11, 0, var11.length);
                     if (e == -1) {
                        data.close();
                        fos.flush();
                        buffer = true;
                        break label67;
                     }

                     fos.write(var11, 0, e);
                  }
               } catch (var6: IOException) {
                  buffer = false;
               }
            } catch (var7: java.lang.Throwable) {
               this.closeSilently(fos);
            }

            this.closeSilently(fos);
            return buffer;
         }

         this.closeSilently(fos);
         return buffer;
      }
   }

   public fun appendText(path: String, content: String): Boolean {
      label45: {
         val file: File = new File(path);
         var writer: FileWriter = null;

         var var11: Boolean;
         label46: {
            try {
               try {
                  if (!file.exists()) {
                     file.createNewFile();
                  }

                  writer = new FileWriter(file, true);
                  writer.write(content);
                  var11 = true;
                  break label46;
               } catch (var7: IOException) {
                  var11 = false;
               }
            } catch (var8: java.lang.Throwable) {
               this.closeSilently(writer);
            }

            this.closeSilently(writer);
            return var11;
         }

         this.closeSilently(writer);
         return var11;
      }
   }

   public fun getLength(path: String): Long {
      val file: File = new File(path);
      return if (file.isFile() && file.exists()) file.length() else 0L;
   }

   public fun getName(pathOrUrl: String?): String {
      if (pathOrUrl == null) {
         return "";
      } else {
         val pos: Int = StringsKt.lastIndexOf$default(pathOrUrl, '/', 0, false, 6, null);
         val var10000: java.lang.String;
         if (0 <= pos) {
            var10000 = pathOrUrl.substring(pos + 1);
         } else {
            var10000 = "${System.currentTimeMillis()}.${this.getExtension(pathOrUrl)}";
         }

         return var10000;
      }
   }

   public fun getNameExcludeExtension(path: String): String {
      var var2: java.lang.String;
      try {
         var e: java.lang.String = new File(path).getName();
         val lastIndexOf: Int = StringsKt.lastIndexOf$default(e, ".", 0, false, 6, null);
         if (lastIndexOf != -1) {
            val var10000: java.lang.String = e.substring(0, lastIndexOf);
            e = var10000;
         }

         var2 = e;
      } catch (var8: Exception) {
         var2 = "";
      }

      return var2;
   }

   public fun getSize(path: String): String {
      return this.toFileSizeString(this.getLength(path));
   }

   public fun toFileSizeString(fileSize: Long): String {
      val df: DecimalFormat = new DecimalFormat("0.00");
      return if (fileSize < 1024L)
         "$fileSizeB"
         else
         (
            if (fileSize < 1048576L)
               Intrinsics.stringPlus(df.format((double)fileSize / (double)1024L), "K")
               else
               (
                  if (fileSize < 1073741824L)
                     Intrinsics.stringPlus(df.format((double)fileSize / (double)1048576L), "M")
                     else
                     Intrinsics.stringPlus(df.format((double)fileSize / (double)1073741824L), "G")
               )
         );
   }

   public fun getExtension(pathOrUrl: String): String {
      val dotPos: Int = StringsKt.lastIndexOf$default(pathOrUrl, '.', 0, false, 6, null);
      val var10000: java.lang.String;
      if (0 <= dotPos) {
         var10000 = pathOrUrl.substring(dotPos + 1);
      } else {
         var10000 = "ext";
      }

      return var10000;
   }

   public fun getFileExtetion(url: String, defaultExt: String = ""): String {
      try {
         val var10: java.lang.String = CollectionsKt.last(
            StringsKt.split$default(StringsKt.split(url, new java.lang.String[]{"?"}, true, 2).get(0), new java.lang.String[]{"/"}, false, 0, 6, null)
         );
         val var11: Int = StringsKt.lastIndexOf$default(var10, '.', 0, false, 6, null);
         val var13: java.lang.String;
         if (0 <= var11) {
            val var7: Int = var11 + 1;
            if (var10 == null) {
               throw new NullPointerException("null cannot be cast to non-null type java.lang.String");
            }

            var13 = var10.substring(var7);
         } else {
            var13 = defaultExt;
         }

         return var13;
      } catch (var9: Exception) {
         return defaultExt;
      }
   }

   public fun getMimeType(pathOrUrl: String): String {
      throw new Exception("Not implemented");
   }

   @JvmOverloads
   public fun getDateTime(path: String, format: String = "yyyy年MM月dd日HH:mm"): String {
      return this.getDateTime(new File(path), format);
   }

   public fun getDateTime(file: File, format: String): String {
      val cal: Calendar = Calendar.getInstance();
      cal.setTimeInMillis(file.lastModified());
      val var4: java.lang.String = new SimpleDateFormat(format, Locale.PRC).format(cal.getTime());
      return var4;
   }

   public fun compareLastModified(path1: String, path2: String): Int {
      val stamp1: Long = new File(path1).lastModified();
      val stamp2: Long = new File(path2).lastModified();
      return if (stamp1 > stamp2) 1 else (if (stamp1 < stamp2) -1 else 0);
   }

   public fun makeDirs(path: String): Boolean {
      return this.makeDirs(new File(path));
   }

   public fun makeDirs(file: File): Boolean {
      return file.mkdirs();
   }

   @JvmOverloads
   fun listDirs(startDirPath: java.lang.String, excludeDirs: Array<java.lang.String>?): Array<File> {
      return listDirs$default(this, startDirPath, excludeDirs, 0, 4, null);
   }

   @JvmOverloads
   fun listDirs(startDirPath: java.lang.String): Array<File> {
      return listDirs$default(this, startDirPath, null, 0, 6, null);
   }

   @JvmOverloads
   fun listDirsAndFiles(startDirPath: java.lang.String): Array<File>? {
      return listDirsAndFiles$default(this, startDirPath, null, 2, null);
   }

   @JvmOverloads
   fun listFiles(startDirPath: java.lang.String, filterPattern: Pattern?): Array<File> {
      return listFiles$default(this, startDirPath, filterPattern, 0, 4, null);
   }

   @JvmOverloads
   fun listFiles(startDirPath: java.lang.String): Array<File> {
      return listFiles$default(this, startDirPath, null, 0, 6, null);
   }

   @JvmOverloads
   fun delete(file: File): Boolean {
      return delete$default(this, file, false, 2, null);
   }

   @JvmOverloads
   fun delete(path: java.lang.String): Boolean {
      return delete$default(this, path, false, 2, null);
   }

   @JvmOverloads
   fun readText(filepath: java.lang.String): java.lang.String {
      return readText$default(this, filepath, null, 2, null);
   }

   @JvmOverloads
   fun writeText(filepath: java.lang.String, content: java.lang.String): Boolean {
      return writeText$default(this, filepath, content, null, 4, null);
   }

   @JvmOverloads
   fun getDateTime(path: java.lang.String): java.lang.String {
      return getDateTime$default(this, path, null, 2, null);
   }

   @JvmStatic
   fun `listDirs$lambda-4`(f: File): Boolean {
      return f != null && f.isDirectory();
   }

   @JvmStatic
   fun `listFiles$lambda-5`(`$filterPattern`: Pattern, file: File): Boolean {
      if (file == null) {
         return false;
      } else if (file.isDirectory()) {
         return false;
      } else {
         val var10000: Boolean;
         if (`$filterPattern` == null) {
            var10000 = true;
         } else {
            val var3: Matcher = `$filterPattern`.matcher(file.getName());
            var10000 = var3 == null || var3.find();
         }

         return var10000;
      }
   }

   @JvmStatic
   fun `listFiles$lambda-6`(`$allowExtensions`: Array<java.lang.String>, `$noName_0`: File, name: java.lang.String): Boolean {
      val var10000: FileUtils = INSTANCE;
      val extension: java.lang.String = var10000.getExtension(name);
      val var8: Boolean;
      if (`$allowExtensions` == null) {
         var8 = false;
      } else {
         val var5: java.lang.String = ArraysKt.contentDeepToString(`$allowExtensions`);
         var8 = var5 != null && StringsKt.contains$default(var5, extension, false, 2, null);
      }

      return var8 || `$allowExtensions` == null;
   }

   public class SortByExtension : Comparator<File> {
      public open fun compare(f1: File?, f2: File?): Int {
         val var5: Int;
         if (f1 == null || f2 == null) {
            var5 = if (f1 == null) -1 else 1;
         } else if (f1.isDirectory() && f2.isFile()) {
            var5 = -1;
         } else if (f1.isFile() && f2.isDirectory()) {
            var5 = 1;
         } else {
            val var3: java.lang.String = f1.getName();
            val var4: java.lang.String = f2.getName();
            var5 = StringsKt.compareTo(var3, var4, true);
         }

         return var5;
      }
   }

   public class SortByName : Comparator<File> {
      private final var caseSensitive: Boolean

      public constructor(caseSensitive: Boolean)  {
         this.caseSensitive = caseSensitive;
      }

      public constructor()  {
         this.caseSensitive = false;
      }

      public open fun compare(f1: File?, f2: File?): Int {
         if (f1 != null && f2 != null) {
            val var10000: Int;
            if (f1.isDirectory() && f2.isFile()) {
               var10000 = -1;
            } else if (f1.isFile() && f2.isDirectory()) {
               var10000 = 1;
            } else {
               val s1: java.lang.String = f1.getName();
               val s2: java.lang.String = f2.getName();
               if (this.caseSensitive) {
                  var10000 = StringsKt.compareTo(s1, s2, false);
               } else {
                  var10000 = StringsKt.compareTo(s1, s2, true);
               }
            }

            return var10000;
         } else {
            return if (f1 == null) -1 else 1;
         }
      }
   }

   public class SortBySize : Comparator<File> {
      public open fun compare(f1: File?, f2: File?): Int {
         return if (f1 == null || f2 == null)
            (if (f1 == null) -1 else 1)
            else
            (if (f1.isDirectory() && f2.isFile()) -1 else (if (f1.isFile() && f2.isDirectory()) 1 else (if (f1.length() < f2.length()) -1 else 1)));
      }
   }

   public class SortByTime : Comparator<File> {
      public open fun compare(f1: File?, f2: File?): Int {
         return if (f1 == null || f2 == null)
            (if (f1 == null) -1 else 1)
            else
            (if (f1.isDirectory() && f2.isFile()) -1 else (if (f1.isFile() && f2.isDirectory()) 1 else (if (f1.lastModified() > f2.lastModified()) -1 else 1)));
      }
   }

   @Retention(AnnotationRetention.SOURCE)
   @java.lang.annotation.Retention(RetentionPolicy.SOURCE)
   annotation class SortType(

   )
}
