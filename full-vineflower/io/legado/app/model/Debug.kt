package io.legado.app.model

public object Debug : DebugLog {
   override fun log(message: java.lang.String) {
      DebugLog.DefaultImpls.log(this, message);
   }

   override fun log(sourceUrl: java.lang.String?, msg: java.lang.String?, isHtml: Boolean) {
      DebugLog.DefaultImpls.log(this, sourceUrl, msg, isHtml);
   }
}
