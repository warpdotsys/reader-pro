package com.htmake.reader.init

public object appCtx {
   public final val cacheDir: String by LazyKt.lazy(<unrepresentable>.INSTANCE)
      public final get() {
         return cacheDir$delegate.getValue() as java.lang.String;
      }

}
