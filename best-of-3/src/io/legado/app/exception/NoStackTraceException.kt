package io.legado.app.exception

public open class NoStackTraceException(msg: String) : Exception(msg) {
   public override fun fillInStackTrace(): Throwable {
      return this;
   }
}
