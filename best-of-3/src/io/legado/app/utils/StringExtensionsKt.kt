package io.legado.app.utils

import io.legado.app.constant.AppPattern
import java.util.ArrayList
import java.util.Arrays

public fun String?.safeTrim(): String? {
   val var10000: java.lang.String;
   if (`$this$safeTrim` == null || StringsKt.isBlank(`$this$safeTrim`)) {
      var10000 = null;
   } else {
      if (`$this$safeTrim` == null) {
         throw new NullPointerException("null cannot be cast to non-null type kotlin.CharSequence");
      }

      var10000 = StringsKt.trim(`$this$safeTrim`).toString();
   }

   return var10000;
}

public fun String?.isAbsUrl(): Boolean {
   return `$this$isAbsUrl` != null
      && !StringsKt.isBlank(`$this$isAbsUrl`)
      && (StringsKt.startsWith(`$this$isAbsUrl`, "http://", true) || StringsKt.startsWith(`$this$isAbsUrl`, "https://", true));
}

public fun String?.isDataUrl(): Boolean {
   return `$this$isDataUrl` != null && AppPattern.INSTANCE.getDataUriRegex().matches(`$this$isDataUrl`);
}

public fun String?.isJson(): Boolean {
   val var10000: Boolean;
   if (`$this$isJson` == null) {
      var10000 = false;
   } else {
      val str: java.lang.String = StringsKt.trim(`$this$isJson`).toString();
      var10000 = StringsKt.startsWith$default(str, "{", false, 2, null) && StringsKt.endsWith$default(str, "}", false, 2, null)
         || StringsKt.startsWith$default(str, "[", false, 2, null) && StringsKt.endsWith$default(str, "]", false, 2, null);
   }

   return var10000;
}

public fun String?.isJsonObject(): Boolean {
   val var10000: Boolean;
   if (`$this$isJsonObject` == null) {
      var10000 = false;
   } else {
      val str: java.lang.String = StringsKt.trim(`$this$isJsonObject`).toString();
      var10000 = StringsKt.startsWith$default(str, "{", false, 2, null) && StringsKt.endsWith$default(str, "}", false, 2, null);
   }

   return var10000;
}

public fun String?.isJsonArray(): Boolean {
   val var10000: Boolean;
   if (`$this$isJsonArray` == null) {
      var10000 = false;
   } else {
      val str: java.lang.String = StringsKt.trim(`$this$isJsonArray`).toString();
      var10000 = StringsKt.startsWith$default(str, "[", false, 2, null) && StringsKt.endsWith$default(str, "]", false, 2, null);
   }

   return var10000;
}

public fun String?.isXml(): Boolean {
   val var10000: Boolean;
   if (`$this$isXml` == null) {
      var10000 = false;
   } else {
      val str: java.lang.String = StringsKt.trim(`$this$isXml`).toString();
      var10000 = StringsKt.startsWith$default(str, "<", false, 2, null) && StringsKt.endsWith$default(str, ">", false, 2, null);
   }

   return var10000;
}

public fun String?.isTrue(nullIsTrue: Boolean = false): Boolean {
   if (`$this$isTrue` != null && !StringsKt.isBlank(`$this$isTrue`) && !(`$this$isTrue` == "null")) {
      return !new Regex("\\s*(?i)(false|no|not|0)\\s*").matches(`$this$isTrue`);
   } else {
      return nullIsTrue;
   }
}

@JvmSynthetic
fun `isTrue$default`(var0: java.lang.String, var1: Boolean, var2: Int, var3: Any): Boolean {
   if ((var2 and 1) != 0) {
      var1 = false;
   }

   return isTrue(var0, var1);
}

public fun String?.htmlFormat(): String {
   return if (`$this$htmlFormat` as java.lang.CharSequence == null || StringsKt.isBlank(`$this$htmlFormat`))
      ""
      else
      new Regex("[\\n\\s]+$")
         .replace(
            new Regex("^[\\n\\s]+")
               .replace(
                  new Regex("\\s*\\n+\\s*")
                     .replace(
                        new Regex("<[script>]*.*?>|&nbsp;").replace(new Regex("(?i)<(br[\\s/]*|/*p\\b.*?|/*div\\b.*?)>").replace(`$this$htmlFormat`, "\n"), ""),
                        "\n　　"
                     ),
                  "　　"
               ),
            ""
         );
}

public fun String.splitNotBlank(vararg delimiter: String): Array<String> {
   var `$this$toTypedArray$iv`: java.lang.Iterable = StringsKt.split$default(
      `$this$splitNotBlank`, Arrays.copyOf(delimiter, delimiter.length), false, 0, 6, null
   );
   var `destination$iv$iv`: java.util.Collection = new ArrayList(CollectionsKt.collectionSizeOrDefault(`$this$toTypedArray$iv`, 10));

   for (Object item$iv$iv : $this$map$iv) {
      val it: java.lang.String = `element$iv$iv` as java.lang.String;
      if (`element$iv$iv` as java.lang.String == null) {
         throw new NullPointerException("null cannot be cast to non-null type kotlin.CharSequence");
      }

      `destination$iv$iv`.add(StringsKt.trim(it).toString());
   }

   `$this$toTypedArray$iv` = `destination$iv$iv` as java.util.List;
   `destination$iv$iv` = new ArrayList();

   for (Object element$iv$iv : $this$map$iv) {
      if (!StringsKt.isBlank(var27 as java.lang.String)) {
         `destination$iv$iv`.add(var27);
      }
   }

   val var10000: Array<Any> = (`destination$iv$iv` as java.util.List).toArray(new java.lang.String[0]);
   if (var10000 == null) {
      throw new NullPointerException("null cannot be cast to non-null type kotlin.Array<T>");
   } else {
      return var10000 as Array<java.lang.String>;
   }
}

public fun String.splitNotBlank(regex: Regex, limit: Int = 0): Array<String> {
   var var21: java.lang.Iterable = regex.split(`$this$splitNotBlank`, limit);
   var `destination$iv$iv`: java.util.Collection = new ArrayList(CollectionsKt.collectionSizeOrDefault(var21, 10));

   for (Object item$iv$iv : var21) {
      val it: java.lang.String = `element$iv$iv` as java.lang.String;
      if (`element$iv$iv` as java.lang.String == null) {
         throw new NullPointerException("null cannot be cast to non-null type kotlin.CharSequence");
      }

      `destination$iv$iv`.add(StringsKt.trim(it).toString());
   }

   var21 = `destination$iv$iv` as java.util.List;
   `destination$iv$iv` = new ArrayList();

   for (Object element$iv$iv : var21) {
      if (!StringsKt.isBlank(var30 as java.lang.String)) {
         `destination$iv$iv`.add(var30);
      }
   }

   val var10000: Array<Any> = (`destination$iv$iv` as java.util.List).toArray(new java.lang.String[0]);
   if (var10000 == null) {
      throw new NullPointerException("null cannot be cast to non-null type kotlin.Array<T>");
   } else {
      return var10000 as Array<java.lang.String>;
   }
}

@JvmSynthetic
fun `splitNotBlank$default`(var0: java.lang.String, var1: Regex, var2: Int, var3: Int, var4: Any): Array<java.lang.String> {
   if ((var3 and 2) != 0) {
      var2 = 0;
   }

   return splitNotBlank(var0, var1, var2);
}

public fun String.startWithIgnoreCase(start: String): Boolean {
   return !StringsKt.isBlank(`$this$startWithIgnoreCase`) && StringsKt.startsWith(`$this$startWithIgnoreCase`, start, true);
}

public fun String.cnCompare(other: String): Int {
   return `$this$cnCompare`.compareTo(other);
}

public fun String.toStringArray(): Array<String> {
   var codePointIndex: Int = 0;

   var var2: Array<java.lang.String>;
   try {
      var var11: Int = 0;
      val e: Int = `$this$toStringArray`.codePointCount(0, `$this$toStringArray`.length());

      val var13: Array<java.lang.String>;
      for (var13 = new java.lang.String[e]; var11 < e; var11++) {
         val var15: Int = codePointIndex;
         codePointIndex = `$this$toStringArray`.offsetByCodePoints(codePointIndex, 1);
         val var10002: java.lang.String = `$this$toStringArray`.substring(var15, codePointIndex);
         var13[var11] = var10002;
      }

      var2 = var13;
   } catch (var10: Exception) {
      val var17: Array<Any> = StringsKt.split$default(`$this$toStringArray`, new java.lang.String[]{""}, false, 0, 6, null).toArray(new java.lang.String[0]);
      if (var17 == null) {
         throw new NullPointerException("null cannot be cast to non-null type kotlin.Array<T>");
      }

      var2 = var17 as Array<java.lang.String>;
   }

   return var2;
}
