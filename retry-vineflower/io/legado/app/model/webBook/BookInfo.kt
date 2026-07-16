package io.legado.app.model.webBook

import io.legado.app.data.entities.Book
import io.legado.app.data.entities.BookSource
import io.legado.app.data.entities.rule.BookInfoRule
import io.legado.app.help.BookHelp
import io.legado.app.model.DebugLog
import io.legado.app.model.DebugLog.DefaultImpls
import io.legado.app.model.analyzeRule.AnalyzeRule
import io.legado.app.utils.NetworkUtils
import io.legado.app.utils.StringExtensionsKt
import io.legado.app.utils.StringUtils
import kotlin.coroutines.intrinsics.IntrinsicsKt
import kotlin.jvm.internal.Intrinsics
import kotlinx.coroutines.JobKt

public object BookInfo {
   @Throws(java/lang/Exception::class)
   public suspend fun analyzeBookInfo(
      book: Book,
      body: String?,
      bookSource: BookSource,
      baseUrl: String,
      redirectUrl: String,
      canReName: Boolean,
      debugLog: DebugLog? = ...
   ) {
      if (body == null) {
         throw new Exception(Intrinsics.stringPlus("error_get_web_content: ", baseUrl));
      } else {
         if (debugLog != null) {
            DefaultImpls.log$default(debugLog, bookSource.getBookSourceUrl(), Intrinsics.stringPlus("≡获取成功:", baseUrl), false, 4, null);
         }

         val analyzeRule: AnalyzeRule = new AnalyzeRule(book, bookSource, debugLog);
         AnalyzeRule.setContent$default(analyzeRule, body, null, 2, null).setBaseUrl(baseUrl);
         analyzeRule.setRedirectUrl(redirectUrl);
         val var10000: Any = this.analyzeBookInfo(book, body, analyzeRule, bookSource, baseUrl, redirectUrl, canReName, debugLog, `$completion`);
         return if (var10000 === IntrinsicsKt.getCOROUTINE_SUSPENDED()) var10000 else Unit.INSTANCE;
      }
   }

   @Throws(java/lang/Exception::class)
   public suspend fun analyzeBookInfo(
      book: Book,
      body: String?,
      analyzeRule: AnalyzeRule,
      bookSource: BookSource,
      baseUrl: String,
      redirectUrl: String,
      canReName: Boolean,
      debugLog: DebugLog? = ...
   ) {
      if (body == null) {
         throw new Exception(Intrinsics.stringPlus("error_get_web_content: ", baseUrl));
      } else {
         val infoRule: BookInfoRule = bookSource.getBookInfoRule();
         val mCanReName: java.lang.String = infoRule.getInit();
         if (mCanReName != null) {
            if (mCanReName.length() > 0) {
               JobKt.ensureActive(`$completion`.getContext());
               if (debugLog != null) {
                  DefaultImpls.log$default(debugLog, bookSource.getBookSourceUrl(), "≡执行详情页初始化规则", false, 4, null);
               }

               AnalyzeRule.setContent$default(analyzeRule, analyzeRule.getElement(mCanReName), null, 2, null);
            }
         }

         var var76: Boolean;
         label307: {
            if (canReName) {
               val e: java.lang.CharSequence = infoRule.getCanReName();
               if (e != null && !StringsKt.isBlank(e)) {
                  var76 = true;
                  break label307;
               }
            }

            var76 = false;
         }

         JobKt.ensureActive(`$completion`.getContext());
         if (debugLog != null) {
            DefaultImpls.log$default(debugLog, bookSource.getBookSourceUrl(), "┌获取书名", false, 4, null);
         }

         var var27: java.lang.String = BookHelp.INSTANCE.formatBookName(AnalyzeRule.getString$default(analyzeRule, infoRule.getName(), null, false, 6, null));
         if (var27.length() > 0 && (var76 || book.getName().length() == 0)) {
            book.setName(var27);
         }

         if (debugLog != null) {
            DefaultImpls.log$default(debugLog, bookSource.getBookSourceUrl(), Intrinsics.stringPlus("└", var27), false, 4, null);
         }

         JobKt.ensureActive(`$completion`.getContext());
         if (debugLog != null) {
            DefaultImpls.log$default(debugLog, bookSource.getBookSourceUrl(), "┌获取作者", false, 4, null);
         }

         var27 = BookHelp.INSTANCE.formatBookAuthor(AnalyzeRule.getString$default(analyzeRule, infoRule.getAuthor(), null, false, 6, null));
         if (var27.length() > 0 && (var76 || book.getAuthor().length() == 0)) {
            book.setAuthor(var27);
         }

         if (debugLog != null) {
            DefaultImpls.log$default(debugLog, bookSource.getBookSourceUrl(), Intrinsics.stringPlus("└", var27), false, 4, null);
         }

         JobKt.ensureActive(`$completion`.getContext());
         if (debugLog != null) {
            DefaultImpls.log$default(debugLog, bookSource.getBookSourceUrl(), "┌获取分类", false, 4, null);
         }

         try {
            val var29: java.util.List = AnalyzeRule.getStringList$default(analyzeRule, infoRule.getKind(), null, false, 6, null);
            if (var29 != null) {
               val var38: java.lang.String = CollectionsKt.joinToString$default(var29, ",", null, null, 0, null, null, 62, null);
               if (var38 != null) {
                  if (var38.length() > 0) {
                     book.setKind(var38);
                  }
               }
            }

            if (debugLog != null) {
               DefaultImpls.log$default(debugLog, bookSource.getBookSourceUrl(), Intrinsics.stringPlus("└", book.getKind()), false, 4, null);
            }
         } catch (var25: Exception) {
            if (debugLog != null) {
               DefaultImpls.log$default(debugLog, bookSource.getBookSourceUrl(), Intrinsics.stringPlus("└", var25.getLocalizedMessage()), false, 4, null);
            }
         }

         JobKt.ensureActive(`$completion`.getContext());
         if (debugLog != null) {
            DefaultImpls.log$default(debugLog, bookSource.getBookSourceUrl(), "┌获取字数", false, 4, null);
         }

         try {
            var27 = StringUtils.INSTANCE.wordCountFormat(AnalyzeRule.getString$default(analyzeRule, infoRule.getWordCount(), null, false, 6, null));
            if (var27.length() > 0) {
               book.setWordCount(var27);
            }

            if (debugLog != null) {
               DefaultImpls.log$default(debugLog, bookSource.getBookSourceUrl(), Intrinsics.stringPlus("└", book.getWordCount()), false, 4, null);
            }
         } catch (var24: Exception) {
            if (debugLog != null) {
               DefaultImpls.log$default(debugLog, bookSource.getBookSourceUrl(), Intrinsics.stringPlus("└", var24.getLocalizedMessage()), false, 4, null);
            }
         }

         JobKt.ensureActive(`$completion`.getContext());
         if (debugLog != null) {
            DefaultImpls.log$default(debugLog, bookSource.getBookSourceUrl(), "┌获取最新章节", false, 4, null);
         }

         try {
            var27 = AnalyzeRule.getString$default(analyzeRule, infoRule.getLastChapter(), null, false, 6, null);
            if (var27.length() > 0) {
               book.setLatestChapterTitle(var27);
            }

            if (debugLog != null) {
               DefaultImpls.log$default(debugLog, bookSource.getBookSourceUrl(), Intrinsics.stringPlus("└", book.getLatestChapterTitle()), false, 4, null);
            }
         } catch (var23: Exception) {
            if (debugLog != null) {
               DefaultImpls.log$default(debugLog, bookSource.getBookSourceUrl(), Intrinsics.stringPlus("└", var23.getLocalizedMessage()), false, 4, null);
            }
         }

         JobKt.ensureActive(`$completion`.getContext());
         if (debugLog != null) {
            DefaultImpls.log$default(debugLog, bookSource.getBookSourceUrl(), "┌获取简介", false, 4, null);
         }

         try {
            var27 = AnalyzeRule.getString$default(analyzeRule, infoRule.getIntro(), null, false, 6, null);
            if (var27.length() > 0) {
               book.setIntro(StringExtensionsKt.htmlFormat(var27));
            }

            if (debugLog != null) {
               DefaultImpls.log$default(debugLog, bookSource.getBookSourceUrl(), Intrinsics.stringPlus("└", book.getIntro()), false, 4, null);
            }
         } catch (var22: Exception) {
            if (debugLog != null) {
               DefaultImpls.log$default(debugLog, bookSource.getBookSourceUrl(), Intrinsics.stringPlus("└", var22.getLocalizedMessage()), false, 4, null);
            }
         }

         JobKt.ensureActive(`$completion`.getContext());
         if (debugLog != null) {
            DefaultImpls.log$default(debugLog, bookSource.getBookSourceUrl(), "┌获取封面链接", false, 4, null);
         }

         try {
            var27 = AnalyzeRule.getString$default(analyzeRule, infoRule.getCoverUrl(), null, false, 6, null);
            if (var27.length() > 0) {
               book.setCoverUrl(NetworkUtils.INSTANCE.getAbsoluteURL(redirectUrl, var27));
            }

            if (debugLog != null) {
               DefaultImpls.log$default(debugLog, bookSource.getBookSourceUrl(), Intrinsics.stringPlus("└", book.getCoverUrl()), false, 4, null);
            }
         } catch (var21: Exception) {
            if (debugLog != null) {
               DefaultImpls.log$default(debugLog, bookSource.getBookSourceUrl(), Intrinsics.stringPlus("└", var21.getLocalizedMessage()), false, 4, null);
            }
         }

         JobKt.ensureActive(`$completion`.getContext());
         if (debugLog != null) {
            DefaultImpls.log$default(debugLog, bookSource.getBookSourceUrl(), "┌获取目录链接", false, 4, null);
         }

         book.setTocUrl(AnalyzeRule.getString$default(analyzeRule, infoRule.getTocUrl(), null, true, 2, null));
         if (book.getTocUrl().length() == 0) {
            book.setTocUrl(baseUrl);
         }

         if (book.getTocUrl() == baseUrl) {
            book.setTocHtml(body);
         }

         val var99: Unit;
         if (debugLog == null) {
            var99 = null;
         } else {
            DefaultImpls.log$default(debugLog, bookSource.getBookSourceUrl(), Intrinsics.stringPlus("└", book.getTocUrl()), false, 4, null);
            var99 = Unit.INSTANCE;
         }

         return if (var99 === IntrinsicsKt.getCOROUTINE_SUSPENDED()) var99 else Unit.INSTANCE;
      }
   }
}
