package io.legado.app.exception

public class ConcurrentException(msg: String, waitTime: Int) : NoStackTraceException(msg) {
   public final val waitTime: Int

   init {
      this.waitTime = waitTime;
   }
}
