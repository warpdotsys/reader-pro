package io.legado.app.help.coroutine

internal interface CoroutineContainer {
   public abstract fun add(coroutine: Coroutine<*>): Boolean {
   }

   public abstract fun addAll(vararg coroutines: Coroutine<*>): Boolean {
   }

   public abstract fun remove(coroutine: Coroutine<*>): Boolean {
   }

   public abstract fun delete(coroutine: Coroutine<*>): Boolean {
   }

   public abstract fun clear() {
   }
}
