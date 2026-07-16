package io.legado.app.utils

public final val msg: String
   public final get() {
      val stackTrace: java.lang.String = ExceptionsKt.stackTraceToString(`$this$msg`);
      val var3: java.lang.String = `$this$msg`.getLocalizedMessage();
      return if (stackTrace.length() > 0) stackTrace else (if (var3 == null) "noErrorMsg" else var3);
   }

