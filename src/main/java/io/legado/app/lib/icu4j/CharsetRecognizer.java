package io.legado.app.lib.icu4j;

abstract class CharsetRecognizer {
   abstract String getName();

   public String getLanguage() {
      return null;
   }

   abstract CharsetMatch match(CharsetDetector det);
}
