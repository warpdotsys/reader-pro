package io.legado.app.utils

import com.jayway.jsonpath.ParseContext
import com.jayway.jsonpath.ReadContext

public final val jsonPath: ParseContext by LazyKt.lazy(<unrepresentable>.INSTANCE)
   public final get() {
      val var0: Any = jsonPath$delegate.getValue();
      return var0 as ParseContext;
   }


public fun ReadContext.readString(path: String): String? {
   return `$this$readString`.read(path, java.lang.String.class);
}

public fun ReadContext.readBool(path: String): Boolean? {
   return `$this$readBool`.read(path, boolean.class);
}

public fun ReadContext.readInt(path: String): Int? {
   return `$this$readInt`.read(path, int.class);
}

public fun ReadContext.readLong(path: String): Long? {
   return `$this$readLong`.read(path, long.class);
}
