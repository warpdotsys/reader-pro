package io.legado.app.adapters

import io.legado.app.help.http.StrResponse
import io.legado.app.model.DebugLog
import java.io.File
import java.nio.file.Paths
import java.util.Arrays

public class DefaultAdpater : ReaderAdapterInterface {
   public override fun getWorkDir(subPath: String): String {
      var var7: java.lang.String;
      label12: {
         val osName: java.lang.String = System.getProperty("os.name");
         val currentDir: java.lang.String = System.getProperty("user.dir");
         if (StringsKt.startsWith(osName, "Mac OS", true)) {
            if (!StringsKt.startsWith$default(currentDir, "/Users/", false, 2, null)) {
               var7 = Paths.get(System.getProperty("user.home"), ".reader").toString();
               break label12;
            }
         }

         var7 = currentDir;
      }

      return Paths.get(var7, subPath).toString();
   }

   public override fun getWorkDir(vararg subDirFiles: String): String {
      return this.getWorkDir(this.getRelativePath(Arrays.copyOf(subDirFiles, subDirFiles.length)));
   }

   public fun getRelativePath(vararg subDirFiles: String): String {
      val path: StringBuilder = new StringBuilder("");
      val var5: Array<java.lang.String> = subDirFiles;
      val it: Int = subDirFiles.length;

      for (int var7 = 0; var7 < it; var7++) {
         val `element$iv`: Any = var5[var7];
         if (var5[var7].length() > 0) {
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

   public override fun getCacheDir(): String {
      return this.getWorkDir("storage", "cache");
   }

   public override suspend fun getStrResponseByRemoteWebview(
      url: String?,
      html: String?,
      encode: String?,
      tag: String?,
      headerMap: Map<String, String>?,
      sourceRegex: String?,
      javaScript: String?,
      proxy: String?,
      post: Boolean,
      body: String?,
      userNameSpace: String,
      debugLog: DebugLog?
   ): StrResponse {
      throw new Exception("不支持webview");
   }
}
