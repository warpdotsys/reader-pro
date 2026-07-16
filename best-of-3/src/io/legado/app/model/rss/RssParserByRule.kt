package io.legado.app.model.rss

import io.legado.app.data.entities.RssArticle
import io.legado.app.data.entities.RssSource
import io.legado.app.exception.NoStackTraceException
import io.legado.app.model.DebugLog
import io.legado.app.model.analyzeRule.AnalyzeRule
import io.legado.app.model.analyzeRule.RuleData
import io.legado.app.model.analyzeRule.AnalyzeRule.SourceRule
import io.legado.app.utils.NetworkUtils
import java.util.ArrayList
import java.util.Locale
import kotlin.jvm.internal.Intrinsics

public object RssParserByRule {
   @Throws(java/lang/Exception::class)
   public fun parseXML(sortName: String, sortUrl: String, body: String?, rssSource: RssSource, ruleData: RuleData, debugLog: DebugLog?): Pair<
         MutableList<RssArticle>,
         String?
      > {
      val sourceUrl: java.lang.String = rssSource.getSourceUrl();
      var nextUrl: java.lang.String = null;
      if (body == null || StringsKt.isBlank(body)) {
         throw new NoStackTraceException(Intrinsics.stringPlus("error_get_web_content: ", rssSource.getSourceUrl()));
      } else {
         var var30: java.lang.String = rssSource.getRuleArticles();
         if (var30 == null || StringsKt.isBlank(var30)) {
            if (debugLog != null) {
               DebugLog.DefaultImpls.log$default(debugLog, sourceUrl, "⇒列表规则为空, 使用默认规则解析", false, 4, null);
            }

            return RssParserDefault.INSTANCE.parseXML(sortName, body, sourceUrl, debugLog);
         } else {
            val var32: java.util.List = new ArrayList();
            val var35: AnalyzeRule = new AnalyzeRule(ruleData, rssSource, debugLog);
            AnalyzeRule.setContent$default(var35, body, null, 2, null).setBaseUrl(sortUrl);
            var35.setRedirectUrl(sortUrl);
            var var36: Boolean = false;
            if (StringsKt.startsWith$default(var30, "-", false, 2, null)) {
               var36 = true;
               if (var30 == null) {
                  throw new NullPointerException("null cannot be cast to non-null type java.lang.String");
               }

               val var10000: java.lang.String = var30.substring(1);
               var30 = var10000;
            }

            if (debugLog != null) {
               DebugLog.DefaultImpls.log$default(debugLog, sourceUrl, "┌获取列表", false, 4, null);
            }

            val collections: java.util.List = var35.getElements(var30);
            if (debugLog != null) {
               DebugLog.DefaultImpls.log$default(debugLog, sourceUrl, Intrinsics.stringPlus("└列表大小:", collections.size()), false, 4, null);
            }

            val var37: java.lang.CharSequence = rssSource.getRuleNextPage();
            if (var37 != null && var37.length() != 0) {
               if (debugLog != null) {
                  DebugLog.DefaultImpls.log$default(debugLog, sourceUrl, "┌获取下一页链接", false, 4, null);
               }

               var var47: java.lang.String = rssSource.getRuleNextPage();
               val var42: Locale = Locale.getDefault();
               if (var47 == null) {
                  throw new NullPointerException("null cannot be cast to non-null type java.lang.String");
               }

               var47 = var47.toUpperCase(var42);
               if (var47 == "PAGE") {
                  nextUrl = sortUrl;
               } else {
                  nextUrl = AnalyzeRule.getString$default(var35, rssSource.getRuleNextPage(), null, false, 6, null);
                  if (nextUrl.length() > 0) {
                     nextUrl = NetworkUtils.INSTANCE.getAbsoluteURL(sortUrl, nextUrl);
                  }
               }

               if (debugLog != null) {
                  DebugLog.DefaultImpls.log$default(debugLog, sourceUrl, Intrinsics.stringPlus("└", nextUrl), false, 4, null);
               }
            }

            val var40: java.util.List = AnalyzeRule.splitSourceRule$default(var35, rssSource.getRuleTitle(), false, 2, null);
            val var44: java.util.List = AnalyzeRule.splitSourceRule$default(var35, rssSource.getRulePubDate(), false, 2, null);
            val var46: java.util.List = AnalyzeRule.splitSourceRule$default(var35, rssSource.getRuleDescription(), false, 2, null);
            val ruleImage: java.util.List = AnalyzeRule.splitSourceRule$default(var35, rssSource.getRuleImage(), false, 2, null);
            val ruleLink: java.util.List = AnalyzeRule.splitSourceRule$default(var35, rssSource.getRuleLink(), false, 2, null);
            val variable: java.lang.String = ruleData.getVariable();
            val var20: java.util.Iterator = collections.iterator();
            var var21: Int = 0;

            while (var20.hasNext()) {
               val var24: RssArticle = this.getItem(sourceUrl, var20.next(), var35, variable, var21++ == 0, var40, var44, var46, ruleImage, ruleLink, debugLog);
               if (var24 != null) {
                  var24.setSort(sortName);
                  var24.setOrigin(sourceUrl);
                  var32.add(var24);
               }
            }

            if (var36) {
               CollectionsKt.reverse(var32);
            }

            return new Pair<>(var32, nextUrl);
         }
      }
   }

   private fun getItem(
      sourceUrl: String,
      item: Any,
      analyzeRule: AnalyzeRule,
      variable: String?,
      log: Boolean,
      ruleTitle: List<SourceRule>,
      rulePubDate: List<SourceRule>,
      ruleDescription: List<SourceRule>,
      ruleImage: List<SourceRule>,
      ruleLink: List<SourceRule>,
      debugLog: DebugLog?
   ): RssArticle? {
      val rssArticle: RssArticle = new RssArticle(null, null, null, 0L, null, null, null, null, null, false, variable, 1023, null);
      analyzeRule.setRuleData(rssArticle);
      AnalyzeRule.setContent$default(analyzeRule, item, null, 2, null);
      if (debugLog != null) {
         debugLog.log(sourceUrl, "┌获取标题", log);
      }

      rssArticle.setTitle(AnalyzeRule.getString$default(analyzeRule, ruleTitle, null, false, 6, null));
      if (debugLog != null) {
         debugLog.log(sourceUrl, Intrinsics.stringPlus("└", rssArticle.getTitle()), log);
      }

      if (debugLog != null) {
         debugLog.log(sourceUrl, "┌获取时间", log);
      }

      rssArticle.setPubDate(AnalyzeRule.getString$default(analyzeRule, rulePubDate, null, false, 6, null));
      if (debugLog != null) {
         debugLog.log(sourceUrl, Intrinsics.stringPlus("└", rssArticle.getPubDate()), log);
      }

      if (debugLog != null) {
         debugLog.log(sourceUrl, "┌获取描述", log);
      }

      if (ruleDescription == null || ruleDescription.isEmpty()) {
         rssArticle.setDescription(null);
         if (debugLog != null) {
            debugLog.log(sourceUrl, "└描述规则为空，将会解析内容页", log);
         }
      } else {
         rssArticle.setDescription(AnalyzeRule.getString$default(analyzeRule, ruleDescription, null, false, 6, null));
         if (debugLog != null) {
            debugLog.log(sourceUrl, Intrinsics.stringPlus("└", rssArticle.getDescription()), log);
         }
      }

      if (debugLog != null) {
         debugLog.log(sourceUrl, "┌获取图片url", log);
      }

      rssArticle.setImage(AnalyzeRule.getString$default(analyzeRule, ruleImage, null, true, 2, null));
      if (debugLog != null) {
         debugLog.log(sourceUrl, Intrinsics.stringPlus("└", rssArticle.getImage()), log);
      }

      if (debugLog != null) {
         debugLog.log(sourceUrl, "┌获取文章链接", log);
      }

      rssArticle.setLink(NetworkUtils.INSTANCE.getAbsoluteURL(sourceUrl, AnalyzeRule.getString$default(analyzeRule, ruleLink, null, false, 6, null)));
      if (debugLog != null) {
         debugLog.log(sourceUrl, Intrinsics.stringPlus("└", rssArticle.getLink()), log);
      }

      return if (StringsKt.isBlank(rssArticle.getTitle())) null else rssArticle;
   }
}
