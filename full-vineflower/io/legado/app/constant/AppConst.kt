package io.legado.app.constant

import com.script.javascript.RhinoScriptEngine
import java.text.SimpleDateFormat

public object AppConst {
   public final val SCRIPT_ENGINE: RhinoScriptEngine by LazyKt.lazy(<unrepresentable>.INSTANCE)
      public final get() {
         return SCRIPT_ENGINE$delegate.getValue() as RhinoScriptEngine;
      }


   public final val TIME_FORMAT: SimpleDateFormat by LazyKt.lazy(<unrepresentable>.INSTANCE)
      public final get() {
         return TIME_FORMAT$delegate.getValue() as SimpleDateFormat;
      }


   public const val UA_NAME: String = "User-Agent"

   public final val dateFormat: SimpleDateFormat by LazyKt.lazy(<unrepresentable>.INSTANCE)
      public final get() {
         return dateFormat$delegate.getValue() as SimpleDateFormat;
      }


   public final val fileNameFormat: SimpleDateFormat by LazyKt.lazy(<unrepresentable>.INSTANCE)
      public final get() {
         return fileNameFormat$delegate.getValue() as SimpleDateFormat;
      }


   public final val keyboardToolChars: List<String> by LazyKt.lazy(<unrepresentable>.INSTANCE)
      public final get() {
         return keyboardToolChars$delegate.getValue() as MutableList<java.lang.String>;
      }


   public final val timeFormat: SimpleDateFormat by LazyKt.lazy(<unrepresentable>.INSTANCE)
      public final get() {
         return timeFormat$delegate.getValue() as SimpleDateFormat;
      }


   public final val userAgent: String by LazyKt.lazy(<unrepresentable>.INSTANCE)
      public final get() {
         return userAgent$delegate.getValue() as java.lang.String;
      }

}
