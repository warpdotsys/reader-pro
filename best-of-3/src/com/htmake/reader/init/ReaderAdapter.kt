package com.htmake.reader.init

import com.htmake.reader.utils.ExtKt
import com.htmake.reader.utils.RemoteWebview
import io.legado.app.adapters.ReaderAdapterInterface
import io.legado.app.help.http.StrResponse
import io.legado.app.model.DebugLog
import java.io.File
import java.util.Arrays

public object ReaderAdapter : ReaderAdapterInterface {
   public override fun getWorkDir(subPath: String): String {
      return ExtKt.getWorkDir(subPath);
   }

   public override fun getWorkDir(vararg subDirFiles: String): String {
      return ExtKt.getWorkDir(this.getRelativePath(Arrays.copyOf(subDirFiles, subDirFiles.length)));
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
      var encodeStr: java.lang.String = encode;
      if (encode == null || encode.length() == 0) {
         encodeStr = if (headerMap == null) null else headerMap.get("charset") as java.lang.String;
      }

      return RemoteWebview.INSTANCE
         .getStrResponse(url, html, encodeStr, tag, headerMap, sourceRegex, javaScript, proxy, post, body, userNameSpace, debugLog, `$completion`);
   }
}
