package io.legado.app.utils

import io.legado.app.model.analyzeRule.AnalyzeUrl
import java.net.URL
import java.util.regex.Matcher
import java.util.regex.Pattern

public object HtmlFormatter {
   private final val commentRegex: Regex = new Regex("<!--[^>]*-->")
   private final val formatImagePattern: Pattern =
      Pattern.compile(
         "<img[^>]*src *= *\"([^\"{]*\\{(?:[^{}]|\\{[^}]+\\})+\\})\"[^>]*>|<img[^>]*data-[^=]*= *\"([^\"]*)\"[^>]*>|<img[^>]*src *= *\"([^\"]*)\"[^>]*>", 2
      )
      private final val notImgHtmlRegex: Regex = new Regex("</?(?!img)[a-zA-Z]+(?=[ >])[^<>]*>")
   private final val otherHtmlRegex: Regex = new Regex("</?[a-zA-Z]+(?=[ >])[^<>]*>")
   private final val wrapHtmlRegex: Regex = new Regex("</?(?:div|p|br|hr|h\\d|article|dd|dl)[^>]*>")

   public fun format(html: String?, otherRegex: Regex = otherHtmlRegex): String {
      return if (html == null)
         ""
         else
         new Regex("[\\n\\s]+$")
            .replace(
               new Regex("^[\\n\\s]+")
                  .replace(new Regex("\\s*\\n+\\s*").replace(otherRegex.replace(commentRegex.replace(wrapHtmlRegex.replace(html, "\n"), ""), ""), "\n　　"), "　　"),
               ""
            );
   }

   public fun formatKeepImg(html: String?, redirectUrl: URL? = null): String {
      if (html == null) {
         return "";
      } else {
         val keepImgHtml: java.lang.String = this.format(html, notImgHtmlRegex);
         val matcher: Matcher = formatImagePattern.matcher(keepImgHtml);
         var appendPos: Int = 0;

         val sb: StringBuffer;
         for (sb = new StringBuffer(); matcher.find(); appendPos = matcher.end()) {
            var var29: java.lang.String = "";
            val var10000: Appendable = sb;
            val var8: Array<java.lang.CharSequence> = new java.lang.CharSequence[2];
            val var10: Int = matcher.start();
            if (keepImgHtml == null) {
               throw new NullPointerException("null cannot be cast to non-null type java.lang.String");
            }

            val var10003: java.lang.String = keepImgHtml.substring(appendPos, var10);
            var8[0] = var10003;
            var var10001: Array<java.lang.CharSequence> = var8;
            var var10002: Byte = 1;
            val var42: StringBuilder = new StringBuilder().append("<img src=\"");
            val var10004: NetworkUtils = NetworkUtils.INSTANCE;
            var var10005: URL = redirectUrl;
            val var34: java.lang.String = matcher.group(1);
            var var10006: java.lang.String;
            if (var34 == null) {
               var10006 = null;
            } else {
               val urlMatcher: Matcher = AnalyzeUrl.Companion.getParamPattern().matcher(var34);
               val var40: java.lang.String;
               if (urlMatcher.find()) {
                  val var39: java.lang.String = var34.substring(urlMatcher.end());
                  var29 = "${44}$var39";
                  var40 = var34.substring(0, urlMatcher.start());
               } else {
                  var40 = var34;
               }

               var10001 = var8;
               var10002 = 1;
               var10005 = redirectUrl;
               var10006 = var40;
            }

            val var9: java.lang.String = if (var10006 == null) matcher.group(2) else var10006;
            if (var9 == null) {
               var10006 = matcher.group(3);
            } else {
               var10006 = var9;
            }

            var10001[var10002] = var42.append(var10004.getAbsoluteURL(var10005, var10006)).append(var29).append("\">").toString();
            StringsKt.append(var10000, var8);
         }

         if (appendPos < keepImgHtml.length()) {
            val var31: Int = keepImgHtml.length();
            if (keepImgHtml == null) {
               throw new NullPointerException("null cannot be cast to non-null type java.lang.String");
            }

            val var41: java.lang.String = keepImgHtml.substring(appendPos, var31);
            sb.append(var41);
         }

         val var30: java.lang.String = sb.toString();
         return var30;
      }
   }
}
