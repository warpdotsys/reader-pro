package io.legado.app.utils

public inline fun <T> attempt(f: () -> T): AttemptResult<T> {
   var value: Any = null;
   var error: java.lang.Throwable = null;

   try {
      value = f.invoke();
   } catch (var5: java.lang.Throwable) {
      error = var5;
   }

   return (AttemptResult<T>)(new AttemptResult<>(value, error));
}
