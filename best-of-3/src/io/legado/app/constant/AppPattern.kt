package io.legado.app.constant

import java.util.regex.Pattern

public object AppPattern {
   public final val EXP_PATTERN: Pattern
   public final val JS_PATTERN: Pattern
   public final val authorRegex: Regex = new Regex("^\\s*作\\s*者[:：\\s]+|\\s+著")
   public final val bdRegex: Regex = new Regex("(\\p{P})+")
   public final val bookFileRegex: Regex = new Regex(".*\\.(txt|epub|umd)", RegexOption.IGNORE_CASE)
   public final val dataUriRegex: Regex = new Regex("data:.*?;base64,(.*)")
   public final val debugMessageSymbolRegex: Regex = new Regex("[⇒◇┌└≡]")
   public final val fileNameRegex: Regex = new Regex("[\\\\/:*?\"<>|.]")
   public final val imgPattern: Pattern
   public final val nameRegex: Regex = new Regex("\\s+作\\s*者.*|\\s+\\S+\\s+著")
   public final val notReadAloudRegex: Regex = new Regex("^(\\s|\\p{C}|\\p{P}|\\p{Z}|\\p{S})+$")
   public final val rnRegex: Regex = new Regex("[\\r\\n]")
   public final val splitGroupRegex: Regex = new Regex("[,;，；]")

   @JvmStatic
   fun {
      var var0: Pattern = Pattern.compile("<js>([\\w\\W]*?)</js>|@js:([\\w\\W]*)", 2);
      JS_PATTERN = var0;
      var0 = Pattern.compile("\\{\\{([\\w\\W]*?)\\}\\}");
      EXP_PATTERN = var0;
      var0 = Pattern.compile("<img[^>]*src=\"([^\"]*(?:\"[^>]+\\})?)\"[^>]*>");
      imgPattern = var0;
   }
}
