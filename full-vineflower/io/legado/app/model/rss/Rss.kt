package io.legado.app.model.rss

import io.legado.app.data.entities.BaseSource
import io.legado.app.data.entities.RssArticle
import io.legado.app.data.entities.RssSource
import io.legado.app.help.http.StrResponse
import io.legado.app.model.DebugLog
import io.legado.app.model.analyzeRule.AnalyzeRule
import io.legado.app.model.analyzeRule.AnalyzeUrl
import io.legado.app.model.analyzeRule.RuleData
import io.legado.app.utils.NetworkUtils
import kotlin.coroutines.Continuation
import kotlin.coroutines.intrinsics.IntrinsicsKt
import kotlin.coroutines.jvm.internal.Boxing
import kotlin.coroutines.jvm.internal.ContinuationImpl
import org.jetbrains.annotations.NotNull
import org.jetbrains.annotations.Nullable

public object Rss {
   public suspend fun getArticles(sortName: String, sortUrl: String, rssSource: RssSource, page: Int, debugLog: DebugLog?): Pair<
         MutableList<RssArticle>,
         String?
      > {
      var `$continuation`: Continuation;
      label20: {
         if (`$completion` is <unrepresentable>) {
            `$continuation` = `$completion` as <unrepresentable>;
            if (((`$completion` as <unrepresentable>).label and Integer.MIN_VALUE) != 0) {
               `$continuation`.label -= Integer.MIN_VALUE;
               break label20;
            }
         }

         `$continuation` = new ContinuationImpl(this, `$completion`) {
            Object L$0;
            Object L$1;
            Object L$2;
            Object L$3;
            Object L$4;
            int label;

            {
               super(`$completion`);
               this.this$0 = `this$0`;
            }

            @Nullable
            @Override
            public final Object invokeSuspend(@NotNull Object $result) {
               this.result = `$result`;
               this.label |= Integer.MIN_VALUE;
               return this.this$0.getArticles(null, null, null, 0, null, this);
            }
         };
      }

      val `$result`: Any = `$continuation`.result;
      val var12: Any = IntrinsicsKt.getCOROUTINE_SUSPENDED();
      var ruleData: RuleData;
      var var10000: Any;
      switch ($continuation.label) {
         case 0:
            ResultKt.throwOnFailure(`$result`);
            ruleData = new RuleData();
            val analyzeUrl: AnalyzeUrl = new AnalyzeUrl(
               sortUrl,
               null,
               Boxing.boxInt(page),
               null,
               null,
               null,
               rssSource,
               ruleData,
               null,
               BaseSource.DefaultImpls.getHeaderMap$default(rssSource, false, 1, null),
               debugLog,
               314,
               null
            );
            `$continuation`.L$0 = sortName;
            `$continuation`.L$1 = sortUrl;
            `$continuation`.L$2 = rssSource;
            `$continuation`.L$3 = debugLog;
            `$continuation`.L$4 = ruleData;
            `$continuation`.label = 1;
            var10000 = AnalyzeUrl.getStrResponseAwait$default(analyzeUrl, null, null, false, `$continuation`, 7, null);
            if (var10000 === var12) {
               return var12;
            }
            break;
         case 1:
            ruleData = `$continuation`.L$4 as RuleData;
            debugLog = `$continuation`.L$3 as DebugLog;
            rssSource = `$continuation`.L$2 as RssSource;
            sortUrl = `$continuation`.L$1 as java.lang.String;
            sortName = `$continuation`.L$0 as java.lang.String;
            ResultKt.throwOnFailure(`$result`);
            var10000 = `$result`;
            break;
         default:
            throw new IllegalStateException("call to 'resume' before 'invoke' with coroutine");
      }

      return RssParserByRule.INSTANCE.parseXML(sortName, sortUrl, (var10000 as StrResponse).getBody(), rssSource, ruleData, debugLog);
   }

   public suspend fun getContent(rssArticle: RssArticle, ruleContent: String, rssSource: RssSource, debugLog: DebugLog?): String {
      var `$continuation`: Continuation;
      label20: {
         if (`$completion` is <unrepresentable>) {
            `$continuation` = `$completion` as <unrepresentable>;
            if (((`$completion` as <unrepresentable>).label and Integer.MIN_VALUE) != 0) {
               `$continuation`.label -= Integer.MIN_VALUE;
               break label20;
            }
         }

         `$continuation` = new ContinuationImpl(this, `$completion`) {
            Object L$0;
            Object L$1;
            Object L$2;
            Object L$3;
            int label;

            {
               super(`$completion`);
               this.this$0 = `this$0`;
            }

            @Nullable
            @Override
            public final Object invokeSuspend(@NotNull Object $result) {
               this.result = `$result`;
               this.label |= Integer.MIN_VALUE;
               return this.this$0.getContent(null, null, null, null, this);
            }
         };
      }

      val `$result`: Any = `$continuation`.result;
      val var11: Any = IntrinsicsKt.getCOROUTINE_SUSPENDED();
      var var10000: Any;
      switch ($continuation.label) {
         case 0:
            ResultKt.throwOnFailure(`$result`);
            val analyzeUrl: AnalyzeUrl = new AnalyzeUrl(
               rssArticle.getLink(),
               null,
               null,
               null,
               null,
               rssArticle.getOrigin(),
               rssSource,
               rssArticle,
               null,
               BaseSource.DefaultImpls.getHeaderMap$default(rssSource, false, 1, null),
               debugLog,
               286,
               null
            );
            `$continuation`.L$0 = rssArticle;
            `$continuation`.L$1 = ruleContent;
            `$continuation`.L$2 = rssSource;
            `$continuation`.L$3 = debugLog;
            `$continuation`.label = 1;
            var10000 = AnalyzeUrl.getStrResponseAwait$default(analyzeUrl, null, null, false, `$continuation`, 7, null);
            if (var10000 === var11) {
               return var11;
            }
            break;
         case 1:
            debugLog = `$continuation`.L$3 as DebugLog;
            rssSource = `$continuation`.L$2 as RssSource;
            ruleContent = `$continuation`.L$1 as java.lang.String;
            rssArticle = `$continuation`.L$0 as RssArticle;
            ResultKt.throwOnFailure(`$result`);
            var10000 = `$result`;
            break;
         default:
            throw new IllegalStateException("call to 'resume' before 'invoke' with coroutine");
      }

      val body: java.lang.String = (var10000 as StrResponse).getBody();
      val analyzeRule: AnalyzeRule = new AnalyzeRule(rssArticle, rssSource, debugLog);
      AnalyzeRule.setContent$default(analyzeRule, body, null, 2, null)
         .setBaseUrl(NetworkUtils.INSTANCE.getAbsoluteURL(rssArticle.getOrigin(), rssArticle.getLink()));
      return AnalyzeRule.getString$default(analyzeRule, ruleContent, null, false, 6, null);
   }
}
