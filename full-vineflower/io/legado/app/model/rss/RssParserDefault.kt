package io.legado.app.model.rss

import io.legado.app.data.entities.RssArticle
import io.legado.app.model.DebugLog
import java.io.StringReader
import java.util.ArrayList
import java.util.regex.Matcher
import java.util.regex.Pattern
import kotlin.jvm.internal.Intrinsics
import org.xmlpull.v1.XmlPullParser
import org.xmlpull.v1.XmlPullParserFactory

public object RssParserDefault {
   private const val RSS_ITEM: String = "item"
   private const val RSS_ITEM_CATEGORY: String = "category"
   private const val RSS_ITEM_CONTENT: String = "content:encoded"
   private const val RSS_ITEM_DESCRIPTION: String = "description"
   private const val RSS_ITEM_ENCLOSURE: String = "enclosure"
   private const val RSS_ITEM_LINK: String = "link"
   private const val RSS_ITEM_PUB_DATE: String = "pubDate"
   private const val RSS_ITEM_THUMBNAIL: String = "media:thumbnail"
   private const val RSS_ITEM_TIME: String = "time"
   private const val RSS_ITEM_TITLE: String = "title"
   private const val RSS_ITEM_TYPE: String = "type"
   private const val RSS_ITEM_URL: String = "url"

   @Throws(org/xmlpull/v1/XmlPullParserException::class, java/io/IOException::class)
   public fun parseXML(sortName: String, xml: String, sourceUrl: String, debugLog: DebugLog?): Pair<MutableList<RssArticle>, String?> {
      val articleList: java.util.List = new ArrayList();
      var var18: RssArticle = new RssArticle(null, null, null, 0L, null, null, null, null, null, false, null, 2047, null);
      val factory: XmlPullParserFactory = XmlPullParserFactory.newInstance(
         "\n        org.kxml2.io.KXmlParser\n        org.kxml2.io.KXmlSerializer\n               ", Thread.currentThread().getContextClassLoader().getClass()
      );
      factory.setNamespaceAware(false);
      val xmlPullParser: XmlPullParser = factory.newPullParser();
      xmlPullParser.setInput(new StringReader(xml));
      var insideItem: Boolean = false;
      var eventType: Int = xmlPullParser.getEventType();

      while (eventType != 1) {
         if (eventType == 2) {
            if (StringsKt.equals(xmlPullParser.getName(), "item", true)) {
               insideItem = true;
            } else if (StringsKt.equals(xmlPullParser.getName(), "title", true)) {
               if (insideItem) {
                  val nextTokenType: java.lang.String = xmlPullParser.nextText();
                  var18.setTitle(StringsKt.trim(nextTokenType).toString());
               }
            } else if (StringsKt.equals(xmlPullParser.getName(), "link", true)) {
               if (insideItem) {
                  val var19: java.lang.String = xmlPullParser.nextText();
                  var18.setLink(StringsKt.trim(var19).toString());
               }
            } else if (StringsKt.equals(xmlPullParser.getName(), "media:thumbnail", true)) {
               if (insideItem) {
                  var18.setImage(xmlPullParser.getAttributeValue(null, "url"));
               }
            } else if (StringsKt.equals(xmlPullParser.getName(), "enclosure", true)) {
               if (insideItem) {
                  val var20: java.lang.String = xmlPullParser.getAttributeValue(null, "type");
                  if (var20 != null && StringsKt.contains$default(var20, "image/", false, 2, null)) {
                     var18.setImage(xmlPullParser.getAttributeValue(null, "url"));
                  }
               }
            } else if (StringsKt.equals(xmlPullParser.getName(), "description", true)) {
               if (insideItem) {
                  val var21: java.lang.String = xmlPullParser.nextText();
                  var18.setDescription(StringsKt.trim(var21).toString());
                  if (var18.getImage() == null) {
                     var18.setImage(this.getImageUrl(var21));
                  }
               }
            } else if (StringsKt.equals(xmlPullParser.getName(), "content:encoded", true)) {
               if (insideItem) {
                  val var26: java.lang.String = xmlPullParser.nextText();
                  val var22: java.lang.String = StringsKt.trim(var26).toString();
                  var18.setContent(var22);
                  if (var18.getImage() == null) {
                     var18.setImage(this.getImageUrl(var22));
                  }
               }
            } else if (StringsKt.equals(xmlPullParser.getName(), "pubDate", true)) {
               if (insideItem) {
                  if (xmlPullParser.next() == 4) {
                     val var27: java.lang.String = xmlPullParser.getText();
                     var18.setPubDate(StringsKt.trim(var27).toString());
                  }
                  continue;
               }
            } else if (StringsKt.equals(xmlPullParser.getName(), "time", true) && insideItem) {
               var18.setPubDate(xmlPullParser.nextText());
            }
         } else if (eventType == 3 && StringsKt.equals(xmlPullParser.getName(), "item", true)) {
            insideItem = false;
            var18.setOrigin(sourceUrl);
            var18.setSort(sortName);
            articleList.add(var18);
            var18 = new RssArticle(null, null, null, 0L, null, null, null, null, null, false, null, 2047, null);
         }

         eventType = xmlPullParser.next();
      }

      val var24: RssArticle = CollectionsKt.firstOrNull(articleList);
      if (var24 != null) {
         if (debugLog != null) {
            DebugLog.DefaultImpls.log$default(debugLog, sourceUrl, "┌获取标题", false, 4, null);
         }

         if (debugLog != null) {
            DebugLog.DefaultImpls.log$default(debugLog, sourceUrl, Intrinsics.stringPlus("└", var24.getTitle()), false, 4, null);
         }

         if (debugLog != null) {
            DebugLog.DefaultImpls.log$default(debugLog, sourceUrl, "┌获取时间", false, 4, null);
         }

         if (debugLog != null) {
            DebugLog.DefaultImpls.log$default(debugLog, sourceUrl, Intrinsics.stringPlus("└", var24.getPubDate()), false, 4, null);
         }

         if (debugLog != null) {
            DebugLog.DefaultImpls.log$default(debugLog, sourceUrl, "┌获取描述", false, 4, null);
         }

         if (debugLog != null) {
            DebugLog.DefaultImpls.log$default(debugLog, sourceUrl, Intrinsics.stringPlus("└", var24.getDescription()), false, 4, null);
         }

         if (debugLog != null) {
            DebugLog.DefaultImpls.log$default(debugLog, sourceUrl, "┌获取图片url", false, 4, null);
         }

         if (debugLog != null) {
            DebugLog.DefaultImpls.log$default(debugLog, sourceUrl, Intrinsics.stringPlus("└", var24.getImage()), false, 4, null);
         }

         if (debugLog != null) {
            DebugLog.DefaultImpls.log$default(debugLog, sourceUrl, "┌获取文章链接", false, 4, null);
         }

         if (debugLog != null) {
            DebugLog.DefaultImpls.log$default(debugLog, sourceUrl, Intrinsics.stringPlus("└", var24.getLink()), false, 4, null);
         }
      }

      return new Pair<>(articleList, null);
   }

   private fun getImageUrl(input: String): String? {
      var url: java.lang.String = null;
      var var10000: Pattern = Pattern.compile("(<img [^>]*>)", 0);
      val var10: Matcher = var10000.matcher(input);
      if (var10.find()) {
         val var11: java.lang.String = var10.group(1);
         var10000 = Pattern.compile("src\\s*=\\s*\"([^\"]+)\"", 0);
         val var13: Matcher = var10000.matcher(var11);
         if (var13.find()) {
            val var17: java.lang.String = var13.group(1);
            if (var17 == null) {
               throw new NullPointerException("null cannot be cast to non-null type kotlin.CharSequence");
            }

            url = StringsKt.trim(var17).toString();
         }
      }

      return url;
   }
}
