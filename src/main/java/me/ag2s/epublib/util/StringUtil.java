package me.ag2s.epublib.util;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class StringUtil {
   public static String collapsePathDots(String path) {
      String[] stringParts = path.split("/");
      List<String> parts = new ArrayList(Arrays.asList(stringParts));

      for(int i = 0; i < parts.size() - 1; ++i) {
         String currentDir = (String)parts.get(i);
         if (currentDir.length() != 0 && !currentDir.equals(".")) {
            if (currentDir.equals("..")) {
               parts.remove(i - 1);
               parts.remove(i - 1);
               i -= 2;
            }
         } else {
            parts.remove(i);
            --i;
         }
      }

      StringBuilder result = new StringBuilder();
      if (path.startsWith("/")) {
         result.append('/');
      }

      for(int i = 0; i < parts.size(); ++i) {
         result.append((String)parts.get(i));
         if (i < parts.size() - 1) {
            result.append('/');
         }
      }

      return result.toString();
   }

   public static boolean isNotBlank(String text) {
      return !isBlank(text);
   }

   public static boolean isBlank(String text) {
      if (isEmpty(text)) {
         return true;
      } else {
         for(int i = 0; i < text.length(); ++i) {
            if (!Character.isWhitespace(text.charAt(i))) {
               return false;
            }
         }

         return true;
      }
   }

   public static boolean isEmpty(String text) {
      return text == null || text.length() == 0;
   }

   public static boolean endsWithIgnoreCase(String source, String suffix) {
      if (isEmpty(suffix)) {
         return true;
      } else if (isEmpty(source)) {
         return false;
      } else {
         return suffix.length() > source.length() ? false : source.substring(source.length() - suffix.length()).toLowerCase().endsWith(suffix.toLowerCase());
      }
   }

   public static String defaultIfNull(String text) {
      return defaultIfNull(text, "");
   }

   public static String defaultIfNull(String text, String defaultValue) {
      return text == null ? defaultValue : text;
   }

   public static boolean equals(String text1, String text2) {
      if (text1 == null) {
         return text2 == null;
      } else {
         return text1.equals(text2);
      }
   }

   public static String toString(Object... keyValues) {
      StringBuilder result = new StringBuilder();
      result.append('[');

      for(int i = 0; i < keyValues.length; i += 2) {
         if (i > 0) {
            result.append(", ");
         }

         result.append(keyValues[i]);
         result.append(": ");
         Object value = null;
         if (i + 1 < keyValues.length) {
            value = keyValues[i + 1];
         }

         if (value == null) {
            result.append("<null>");
         } else {
            result.append('\'');
            result.append(value);
            result.append('\'');
         }
      }

      result.append(']');
      return result.toString();
   }

   public static int hashCode(String... values) {
      int result = 31;

      for(String value : values) {
         result ^= String.valueOf(value).hashCode();
      }

      return result;
   }

   public static String substringBefore(String text, char separator) {
      if (isEmpty(text)) {
         return text;
      } else {
         int sepPos = text.indexOf(separator);
         return sepPos < 0 ? text : text.substring(0, sepPos);
      }
   }

   public static String substringBeforeLast(String text, char separator) {
      if (isEmpty(text)) {
         return text;
      } else {
         int cPos = text.lastIndexOf(separator);
         return cPos < 0 ? text : text.substring(0, cPos);
      }
   }

   public static String substringAfterLast(String text, char separator) {
      if (isEmpty(text)) {
         return text;
      } else {
         int cPos = text.lastIndexOf(separator);
         return cPos < 0 ? "" : text.substring(cPos + 1);
      }
   }

   public static String substringAfter(String text, char c) {
      if (isEmpty(text)) {
         return text;
      } else {
         int cPos = text.indexOf(c);
         return cPos < 0 ? "" : text.substring(cPos + 1);
      }
   }

   public static String formatHtml(String text) {
      StringBuilder body = new StringBuilder();

      for(String s : text.split("\\r?\\n")) {
         s = s.replaceAll("^\\s+|\\s+$", "");
         if (s.length() > 0) {
            if (s.matches("(?i)^<img\\s([^>]+)/?>$")) {
               body.append(s.replaceAll("(?i)^<img\\s([^>]+)/?>$", "<div class=\"duokan-image-single\"><img class=\"picture-80\" $1/></div>"));
            } else {
               body.append("<p>").append(s).append("</p>");
            }
         }
      }

      return body.toString();
   }
}
