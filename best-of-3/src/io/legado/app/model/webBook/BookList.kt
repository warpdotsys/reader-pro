package io.legado.app.model.webBook

import com.htmake.reader.synth.SyntheticContinuation
import com.htmake.reader.synth.SyntheticFunction0
import com.htmake.reader.synth.SyntheticType

import io.legado.app.data.entities.Book
import io.legado.app.data.entities.BookSource
import io.legado.app.data.entities.SearchBook
import io.legado.app.data.entities.rule.BookListRule
import io.legado.app.help.BookHelp
import io.legado.app.model.DebugLog
import io.legado.app.model.analyzeRule.AnalyzeRule
import io.legado.app.model.analyzeRule.AnalyzeUrl
import io.legado.app.model.analyzeRule.AnalyzeRule.SourceRule
import io.legado.app.utils.NetworkUtils
import io.legado.app.utils.StringExtensionsKt
import io.legado.app.utils.StringUtils
import java.util.ArrayList
import kotlin.coroutines.Continuation
import kotlin.coroutines.intrinsics.IntrinsicsKt
import kotlin.coroutines.jvm.internal.Boxing
import kotlin.coroutines.jvm.internal.ContinuationImpl
import kotlin.jvm.internal.Intrinsics
import kotlinx.coroutines.JobKt
import org.jetbrains.annotations.NotNull
import org.jetbrains.annotations.Nullable

public object BookList {
   @Throws(java/lang/Exception::class)
   public suspend fun analyzeBookList(
      body: String?,
      bookSource: BookSource,
      analyzeUrl: AnalyzeUrl,
      baseUrl: String,
      variableBook: SearchBook,
      isSearch: Boolean = ...,
      debugLog: DebugLog? = ...
   ): ArrayList<SearchBook> {
      var `$continuation`: Continuation;
      label184: {
         if (`$completion` is SyntheticContinuation) {
            `$continuation` = `$completion` as SyntheticContinuation;
            if (((`$completion` as SyntheticContinuation).label and Integer.MIN_VALUE) != 0) {
               `$continuation`.label -= Integer.MIN_VALUE;
               break label184;
            }
         }

         `$continuation` = new ContinuationImpl(this, `$completion`) {
            Object L$0;
            Object L$1;
            Object L$2;
            Object L$3;
            Object L$4;
            Object L$5;
            Object L$6;
            Object L$7;
            Object L$8;
            Object L$9;
            Object L$10;
            Object L$11;
            Object L$12;
            Object L$13;
            Object L$14;
            Object L$15;
            Object L$16;
            int I$0;
            int I$1;
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
               return this.this$0.analyzeBookList(null, null, null, null, null, false, null, this);
            }
         };
      }

      var bookList: ArrayList;
      var var71: BookList;
      label217: {
         label218: {
            val `$result`: Any = `$continuation`.result;
            val var35: Any = IntrinsicsKt.getCOROUTINE_SUSPENDED();
            var analyzeRule: AnalyzeRule;
            var reverse: Boolean;
            var ruleName: java.util.List;
            var ruleBookUrl: java.util.List;
            var ruleAuthor: java.util.List;
            var ruleCoverUrl: java.util.List;
            var ruleIntro: java.util.List;
            var ruleKind: java.util.List;
            var ruleLastChapter: java.util.List;
            var ruleWordCount: java.util.List;
            var var23: java.util.Iterator;
            var var24: Int;
            switch ($continuation.label) {
               case 0:
                  ResultKt.throwOnFailure(`$result`);
                  bookList = new ArrayList();
                  if (body == null) {
                     throw new Exception("error_get_web_content");
                  }

                  if (debugLog != null) {
                     DebugLog.DefaultImpls.log$default(
                        debugLog, bookSource.getBookSourceUrl(), Intrinsics.stringPlus("≡获取成功:", analyzeUrl.getRuleUrl()), false, 4, null
                     );
                  }

                  analyzeRule = new AnalyzeRule(variableBook, bookSource, debugLog);
                  AnalyzeRule.setContent$default(analyzeRule, body, null, 2, null).setBaseUrl(baseUrl);
                  analyzeRule.setRedirectUrl(baseUrl);
                  val collections: java.lang.String = bookSource.getBookUrlPattern();
                  if (collections != null) {
                     JobKt.ensureActive(`$continuation`.getContext());
                     if (new Regex(collections).matches(baseUrl)) {
                        if (debugLog != null) {
                           DebugLog.DefaultImpls.log$default(debugLog, bookSource.getBookSourceUrl(), "≡链接为详情页", false, 4, null);
                        }

                        var71 = INSTANCE;
                        val var87: java.lang.String = variableBook.getVariable();
                        `$continuation`.L$0 = body;
                        `$continuation`.L$1 = bookList;
                        `$continuation`.label = 1;
                        var71 = (BookList)var71.getInfoItem(body, analyzeRule, bookSource, analyzeUrl, baseUrl, var87, debugLog, `$continuation`);
                        if (var71 === var35) {
                           return var35;
                        }
                        break label217;
                     }
                  }

                  reverse = false;
                  val var74: BookListRule;
                  if (isSearch) {
                     var74 = bookSource.getSearchRule();
                  } else {
                     val var39: java.lang.CharSequence = bookSource.getExploreRule().getBookList();
                     var74 = if (var39 == null || StringsKt.isBlank(var39)) bookSource.getSearchRule() else bookSource.getExploreRule();
                  }

                  val var42: java.lang.String = var74.getBookList();
                  var var40: java.lang.String = if (var42 == null) "" else var42;
                  if (StringsKt.startsWith$default(if (var42 == null) "" else var42, "-", false, 2, null)) {
                     reverse = (boolean)1;
                     val var75: java.lang.String = var40.substring(1);
                     var40 = var75;
                  }

                  if (StringsKt.startsWith$default(var40, "+", false, 2, null)) {
                     if (var40 == null) {
                        throw new NullPointerException("null cannot be cast to non-null type java.lang.String");
                     }

                     val var76: java.lang.String = var40.substring(1);
                     var40 = var76;
                  }

                  if (debugLog != null) {
                     DebugLog.DefaultImpls.log$default(debugLog, bookSource.getBookSourceUrl(), "┌获取书籍列表", false, 4, null);
                  }

                  val var37: java.util.List = analyzeRule.getElements(var40);
                  JobKt.ensureActive(`$continuation`.getContext());
                  if (var37.isEmpty()) {
                     val var43: java.lang.CharSequence = bookSource.getBookUrlPattern();
                     if (var43 == null || var43.length() == 0) {
                        if (debugLog != null) {
                           DebugLog.DefaultImpls.log$default(debugLog, bookSource.getBookSourceUrl(), "└列表为空,按详情页解析", false, 4, null);
                        }

                        val var86: java.lang.String = variableBook.getVariable();
                        `$continuation`.L$0 = body;
                        `$continuation`.L$1 = bookList;
                        `$continuation`.label = 2;
                        var71 = this.getInfoItem(body, analyzeRule, bookSource, analyzeUrl, baseUrl, var86, debugLog, `$continuation`);
                        if (var71 === var35) {
                           return var35;
                        }
                        break label218;
                     }
                  }

                  ruleName = AnalyzeRule.splitSourceRule$default(analyzeRule, var74.getName(), false, 2, null);
                  ruleBookUrl = AnalyzeRule.splitSourceRule$default(analyzeRule, var74.getBookUrl(), false, 2, null);
                  ruleAuthor = AnalyzeRule.splitSourceRule$default(analyzeRule, var74.getAuthor(), false, 2, null);
                  ruleCoverUrl = AnalyzeRule.splitSourceRule$default(analyzeRule, var74.getCoverUrl(), false, 2, null);
                  ruleIntro = AnalyzeRule.splitSourceRule$default(analyzeRule, var74.getIntro(), false, 2, null);
                  ruleKind = AnalyzeRule.splitSourceRule$default(analyzeRule, var74.getKind(), false, 2, null);
                  ruleLastChapter = AnalyzeRule.splitSourceRule$default(analyzeRule, var74.getLastChapter(), false, 2, null);
                  ruleWordCount = AnalyzeRule.splitSourceRule$default(analyzeRule, var74.getWordCount(), false, 2, null);
                  if (debugLog != null) {
                     DebugLog.DefaultImpls.log$default(
                        debugLog, bookSource.getBookSourceUrl(), Intrinsics.stringPlus("└列表大小:", Boxing.boxInt(var37.size())), false, 4, null
                     );
                  }

                  var23 = var37.iterator();
                  var24 = 0;
                  break;
               case 1:
                  bookList = `$continuation`.L$1 as ArrayList;
                  body = `$continuation`.L$0 as java.lang.String;
                  ResultKt.throwOnFailure(`$result`);
                  var71 = (BookList)`$result`;
                  break label217;
               case 2:
                  bookList = `$continuation`.L$1 as ArrayList;
                  body = `$continuation`.L$0 as java.lang.String;
                  ResultKt.throwOnFailure(`$result`);
                  var71 = `$result`;
                  break label218;
               case 3:
                  var24 = `$continuation`.I$1;
                  reverse = (boolean)`$continuation`.I$0;
                  var23 = `$continuation`.L$16 as java.util.Iterator;
                  ruleWordCount = `$continuation`.L$15 as java.util.List;
                  ruleLastChapter = `$continuation`.L$14 as java.util.List;
                  ruleKind = `$continuation`.L$13 as java.util.List;
                  ruleIntro = `$continuation`.L$12 as java.util.List;
                  ruleCoverUrl = `$continuation`.L$11 as java.util.List;
                  ruleAuthor = `$continuation`.L$10 as java.util.List;
                  ruleBookUrl = `$continuation`.L$9 as java.util.List;
                  ruleName = `$continuation`.L$8 as java.util.List;
                  analyzeRule = `$continuation`.L$7 as AnalyzeRule;
                  bookList = `$continuation`.L$6 as ArrayList;
                  debugLog = `$continuation`.L$5 as DebugLog;
                  variableBook = `$continuation`.L$4 as SearchBook;
                  baseUrl = `$continuation`.L$3 as java.lang.String;
                  bookSource = `$continuation`.L$2 as BookSource;
                  body = `$continuation`.L$1 as java.lang.String;
                  this = `$continuation`.L$0 as BookList;
                  ResultKt.throwOnFailure(`$result`);
                  val var27: SearchBook = `$result` as SearchBook;
                  if (`$result` as SearchBook != null) {
                     if (baseUrl == var27.getBookUrl()) {
                        var27.setInfoHtml(body);
                     }

                     Boxing.boxBoolean(bookList.add(var27));
                  }
                  break;
               default:
                  throw new IllegalStateException("call to 'resume' before 'invoke' with coroutine");
            }

            while (var23.hasNext()) {
               val index: Int = var24++;
               val item: Any = var23.next();
               val var28: java.lang.String = variableBook.getVariable();
               val var10006: Boolean = index == 0;
               `$continuation`.L$0 = this;
               `$continuation`.L$1 = body;
               `$continuation`.L$2 = bookSource;
               `$continuation`.L$3 = baseUrl;
               `$continuation`.L$4 = variableBook;
               `$continuation`.L$5 = debugLog;
               `$continuation`.L$6 = bookList;
               `$continuation`.L$7 = analyzeRule;
               `$continuation`.L$8 = ruleName;
               `$continuation`.L$9 = ruleBookUrl;
               `$continuation`.L$10 = ruleAuthor;
               `$continuation`.L$11 = ruleCoverUrl;
               `$continuation`.L$12 = ruleIntro;
               `$continuation`.L$13 = ruleKind;
               `$continuation`.L$14 = ruleLastChapter;
               `$continuation`.L$15 = ruleWordCount;
               `$continuation`.L$16 = var23;
               `$continuation`.I$0 = reverse;
               `$continuation`.I$1 = var24;
               `$continuation`.label = 3;
               var71 = (BookList)this.getSearchItem(
                  item,
                  analyzeRule,
                  bookSource,
                  baseUrl,
                  var28,
                  var10006,
                  ruleName,
                  ruleBookUrl,
                  ruleAuthor,
                  ruleKind,
                  ruleCoverUrl,
                  ruleWordCount,
                  ruleIntro,
                  ruleLastChapter,
                  debugLog,
                  `$continuation`
               );
               if (var71 === var35) {
                  return var35;
               }

               val var65: SearchBook = var71 as SearchBook;
               if (var71 as SearchBook != null) {
                  if (baseUrl == var65.getBookUrl()) {
                     var65.setInfoHtml(body);
                  }

                  Boxing.boxBoolean(bookList.add(var65));
               }
            }

            if (reverse != 0) {
               CollectionsKt.reverse(bookList);
            }

            return bookList;
         }

         val var44: SearchBook = var71 as SearchBook;
         if (var71 as SearchBook != null) {
            var44.setInfoHtml(body);
            Boxing.boxBoolean(bookList.add(var44));
         }

         return bookList;
      }

      val var56: SearchBook = var71 as SearchBook;
      if (var71 as SearchBook != null) {
         var56.setInfoHtml(body);
         Boxing.boxBoolean(bookList.add(var56));
      }

      return bookList;
   }

   private suspend fun getInfoItem(
      body: String,
      analyzeRule: AnalyzeRule,
      bookSource: BookSource,
      analyzeUrl: AnalyzeUrl,
      baseUrl: String,
      variable: String?,
      debugLog: DebugLog? = ...
   ): SearchBook? {
      var `$continuation`: Continuation;
      label29: {
         if (`$completion` is SyntheticContinuation) {
            `$continuation` = `$completion` as SyntheticContinuation;
            if (((`$completion` as SyntheticContinuation).label and Integer.MIN_VALUE) != 0) {
               `$continuation`.label -= Integer.MIN_VALUE;
               break label29;
            }
         }

         `$continuation` = new ContinuationImpl(this, `$completion`) {
            Object L$0;
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
               return BookList.access$getInfoItem(this.this$0, null, null, null, null, null, null, null, this);
            }
         };
      }

      val `$result`: Any = `$continuation`.result;
      val var14: Any = IntrinsicsKt.getCOROUTINE_SUSPENDED();
      var book: Book;
      switch ($continuation.label) {
         case 0:
            ResultKt.throwOnFailure(`$result`);
            book = new Book(
               null,
               null,
               null,
               null,
               null,
               null,
               null,
               null,
               null,
               null,
               null,
               null,
               null,
               0,
               0L,
               null,
               0L,
               0L,
               0,
               0,
               null,
               0,
               0,
               0L,
               null,
               false,
               0,
               0,
               false,
               variable,
               null,
               false,
               null,
               -536870913,
               1,
               null
            );
            book.setBookUrl(analyzeUrl.getRuleUrl());
            book.setOrigin(bookSource.getBookSourceUrl());
            book.setOriginName(bookSource.getBookSourceName());
            book.setOriginOrder(bookSource.getCustomOrder());
            book.setType(bookSource.getBookSourceType());
            book.setUserNameSpace(analyzeRule.getUserNameSpace());
            analyzeRule.setRuleData(book);
            val var10000: BookInfo = BookInfo.INSTANCE;
            `$continuation`.L$0 = book;
            `$continuation`.label = 1;
            if (var10000.analyzeBookInfo(book, body, analyzeRule, bookSource, baseUrl, baseUrl, false, debugLog, `$continuation`) === var14) {
               return var14;
            }
            break;
         case 1:
            book = `$continuation`.L$0 as Book;
            ResultKt.throwOnFailure(`$result`);
            break;
         default:
            throw new IllegalStateException("call to 'resume' before 'invoke' with coroutine");
      }

      return if (!StringsKt.isBlank(book.getName())) book.toSearchBook() else null;
   }

   private suspend fun getSearchItem(
      item: Any,
      analyzeRule: AnalyzeRule,
      bookSource: BookSource,
      baseUrl: String,
      variable: String?,
      log: Boolean,
      ruleName: List<SourceRule>,
      ruleBookUrl: List<SourceRule>,
      ruleAuthor: List<SourceRule>,
      ruleKind: List<SourceRule>,
      ruleCoverUrl: List<SourceRule>,
      ruleWordCount: List<SourceRule>,
      ruleIntro: List<SourceRule>,
      ruleLastChapter: List<SourceRule>,
      debugLog: DebugLog? = ...
   ): SearchBook? {
      val searchBook: SearchBook = new SearchBook(null, null, null, 0, null, null, null, null, null, null, null, null, 0L, variable, 0, 24575, null);
      searchBook.setOrigin(bookSource.getBookSourceUrl());
      searchBook.setOriginName(bookSource.getBookSourceName());
      searchBook.setType(bookSource.getBookSourceType());
      searchBook.setOriginOrder(bookSource.getCustomOrder());
      searchBook.setUserNameSpace(analyzeRule.getUserNameSpace());
      analyzeRule.setRuleData(searchBook);
      AnalyzeRule.setContent$default(analyzeRule, item, null, 2, null);
      JobKt.ensureActive(`$completion`.getContext());
      if (log && debugLog != null) {
         DebugLog.DefaultImpls.log$default(debugLog, bookSource.getBookSourceUrl(), "┌获取书名", false, 4, null);
      }

      searchBook.setName(BookHelp.INSTANCE.formatBookName(AnalyzeRule.getString$default(analyzeRule, ruleName, null, false, 6, null)));
      if (log && debugLog != null) {
         DebugLog.DefaultImpls.log$default(debugLog, bookSource.getBookSourceUrl(), Intrinsics.stringPlus("└", searchBook.getName()), false, 4, null);
      }

      if (searchBook.getName().length() <= 0) {
         return null;
      } else {
         JobKt.ensureActive(`$completion`.getContext());
         if (log && debugLog != null) {
            DebugLog.DefaultImpls.log$default(debugLog, bookSource.getBookSourceUrl(), "┌获取作者", false, 4, null);
         }

         searchBook.setAuthor(BookHelp.INSTANCE.formatBookAuthor(AnalyzeRule.getString$default(analyzeRule, ruleAuthor, null, false, 6, null)));
         if (log && debugLog != null) {
            DebugLog.DefaultImpls.log$default(debugLog, bookSource.getBookSourceUrl(), Intrinsics.stringPlus("└", searchBook.getAuthor()), false, 4, null);
         }

         JobKt.ensureActive(`$completion`.getContext());
         if (log && debugLog != null) {
            DebugLog.DefaultImpls.log$default(debugLog, bookSource.getBookSourceUrl(), "┌获取分类", false, 4, null);
         }

         try {
            val var30: java.util.List = AnalyzeRule.getStringList$default(analyzeRule, ruleKind, null, false, 6, null);
            searchBook.setKind(if (var30 == null) null else CollectionsKt.joinToString$default(var30, ",", null, null, 0, null, null, 62, null));
            if (log && debugLog != null) {
               DebugLog.DefaultImpls.log$default(debugLog, bookSource.getBookSourceUrl(), Intrinsics.stringPlus("└", searchBook.getKind()), false, 4, null);
            }
         } catch (var29: Exception) {
            if (log && debugLog != null) {
               DebugLog.DefaultImpls.log$default(
                  debugLog, bookSource.getBookSourceUrl(), Intrinsics.stringPlus("└", var29.getLocalizedMessage()), false, 4, null
               );
            }
         }

         JobKt.ensureActive(`$completion`.getContext());
         if (log && debugLog != null) {
            DebugLog.DefaultImpls.log$default(debugLog, bookSource.getBookSourceUrl(), "┌获取字数", false, 4, null);
         }

         try {
            searchBook.setWordCount(StringUtils.INSTANCE.wordCountFormat(AnalyzeRule.getString$default(analyzeRule, ruleWordCount, null, false, 6, null)));
            if (log && debugLog != null) {
               DebugLog.DefaultImpls.log$default(debugLog, bookSource.getBookSourceUrl(), Intrinsics.stringPlus("└", searchBook.getWordCount()), false, 4, null);
            }
         } catch (var28: Exception) {
            if (log && debugLog != null) {
               DebugLog.DefaultImpls.log$default(
                  debugLog, bookSource.getBookSourceUrl(), Intrinsics.stringPlus("└", var28.getLocalizedMessage()), false, 4, null
               );
            }
         }

         JobKt.ensureActive(`$completion`.getContext());
         if (log && debugLog != null) {
            DebugLog.DefaultImpls.log$default(debugLog, bookSource.getBookSourceUrl(), "┌获取最新章节", false, 4, null);
         }

         try {
            searchBook.setLatestChapterTitle(AnalyzeRule.getString$default(analyzeRule, ruleLastChapter, null, false, 6, null));
            if (log && debugLog != null) {
               DebugLog.DefaultImpls.log$default(
                  debugLog, bookSource.getBookSourceUrl(), Intrinsics.stringPlus("└", searchBook.getLatestChapterTitle()), false, 4, null
               );
            }
         } catch (var27: Exception) {
            if (log && debugLog != null) {
               DebugLog.DefaultImpls.log$default(
                  debugLog, bookSource.getBookSourceUrl(), Intrinsics.stringPlus("└", var27.getLocalizedMessage()), false, 4, null
               );
            }
         }

         JobKt.ensureActive(`$completion`.getContext());
         if (log && debugLog != null) {
            DebugLog.DefaultImpls.log$default(debugLog, bookSource.getBookSourceUrl(), "┌获取简介", false, 4, null);
         }

         try {
            searchBook.setIntro(StringExtensionsKt.htmlFormat(AnalyzeRule.getString$default(analyzeRule, ruleIntro, null, false, 6, null)));
            if (log && debugLog != null) {
               DebugLog.DefaultImpls.log$default(debugLog, bookSource.getBookSourceUrl(), Intrinsics.stringPlus("└", searchBook.getIntro()), false, 4, null);
            }
         } catch (var26: Exception) {
            if (log && debugLog != null) {
               DebugLog.DefaultImpls.log$default(
                  debugLog, bookSource.getBookSourceUrl(), Intrinsics.stringPlus("└", var26.getLocalizedMessage()), false, 4, null
               );
            }
         }

         JobKt.ensureActive(`$completion`.getContext());
         if (log && debugLog != null) {
            DebugLog.DefaultImpls.log$default(debugLog, bookSource.getBookSourceUrl(), "┌获取封面链接", false, 4, null);
         }

         try {
            val var31: java.lang.String = AnalyzeRule.getString$default(analyzeRule, ruleCoverUrl, null, false, 6, null);
            if (var31.length() > 0) {
               searchBook.setCoverUrl(NetworkUtils.INSTANCE.getAbsoluteURL(baseUrl, var31));
            }

            if (log && debugLog != null) {
               DebugLog.DefaultImpls.log$default(debugLog, bookSource.getBookSourceUrl(), Intrinsics.stringPlus("└", searchBook.getCoverUrl()), false, 4, null);
            }
         } catch (var25: Exception) {
            if (log && debugLog != null) {
               DebugLog.DefaultImpls.log$default(
                  debugLog, bookSource.getBookSourceUrl(), Intrinsics.stringPlus("└", var25.getLocalizedMessage()), false, 4, null
               );
            }
         }

         JobKt.ensureActive(`$completion`.getContext());
         if (log && debugLog != null) {
            DebugLog.DefaultImpls.log$default(debugLog, bookSource.getBookSourceUrl(), "┌获取详情页链接", false, 4, null);
         }

         searchBook.setBookUrl(AnalyzeRule.getString$default(analyzeRule, ruleBookUrl, null, true, 2, null));
         if (searchBook.getBookUrl().length() == 0) {
            searchBook.setBookUrl(baseUrl);
         }

         if (log && debugLog != null) {
            DebugLog.DefaultImpls.log$default(debugLog, bookSource.getBookSourceUrl(), Intrinsics.stringPlus("└", searchBook.getBookUrl()), false, 4, null);
         }

         return searchBook;
      }
   }
}
