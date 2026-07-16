package io.legado.app.adapters

import io.legado.app.help.http.StrResponse
import io.legado.app.model.DebugLog

public interface ReaderAdapterInterface {
   public abstract fun getWorkDir(subPath: String = ...): String {
   }

   public abstract fun getWorkDir(vararg subDirFiles: String): String {
   }

   public abstract fun getCacheDir(): String {
   }

   public abstract suspend fun getStrResponseByRemoteWebview(
      url: String? = ...,
      html: String? = ...,
      encode: String? = ...,
      tag: String? = ...,
      headerMap: Map<String, String>? = ...,
      sourceRegex: String? = ...,
      javaScript: String? = ...,
      proxy: String? = ...,
      post: Boolean = ...,
      body: String? = ...,
      userNameSpace: String = ...,
      debugLog: DebugLog? = ...
   ): StrResponse {
   }

   internal class DefaultImpls
}
