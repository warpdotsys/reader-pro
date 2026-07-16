package io.legado.app.model

import okhttp3.logging.HttpLoggingInterceptor.Logger

public interface DebugLog : Logger {
   public open fun log(sourceUrl: String? = ..., msg: String? = ..., isHtml: Boolean = ...) {
   }

   public override fun log(message: String) {
   }

   // $VF: Class flags could not be determined
   internal class DefaultImpls {
      @JvmStatic
      fun log(`this`: DebugLog, sourceUrl: java.lang.String?, msg: java.lang.String?, isHtml: Boolean) {
         DebugLogKt.access$getLogger$p().info("sourceUrl: {}, msg: {}", sourceUrl, msg);
      }

      @JvmStatic
      fun log(`this`: DebugLog, message: java.lang.String) {
         DebugLogKt.access$getLogger$p().debug(message);
      }
   }
}
