package io.legado.app.model.webBook

import io.legado.app.data.entities.BaseSource
import io.legado.app.data.entities.Book
import io.legado.app.data.entities.BookChapter
import io.legado.app.data.entities.BookSource
import io.legado.app.data.entities.rule.ContentRule
import io.legado.app.help.http.StrResponse
import io.legado.app.model.DebugLog
import io.legado.app.model.analyzeRule.AnalyzeRule
import io.legado.app.model.analyzeRule.AnalyzeUrl
import io.legado.app.utils.HtmlFormatter
import io.legado.app.utils.NetworkUtils
import java.net.URL
import java.util.ArrayList
import kotlin.coroutines.Continuation
import kotlin.coroutines.CoroutineContext
import kotlin.coroutines.intrinsics.IntrinsicsKt
import kotlin.coroutines.jvm.internal.Boxing
import kotlin.coroutines.jvm.internal.ContinuationImpl
import kotlin.jvm.functions.Function2
import kotlin.jvm.internal.Intrinsics
import kotlin.jvm.internal.Ref.ObjectRef
import kotlinx.coroutines.BuildersKt
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Deferred
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.JobKt
import org.jetbrains.annotations.NotNull
import org.jetbrains.annotations.Nullable

public object BookContent {
   public suspend fun analyzeContent(
      body: String?,
      book: Book,
      bookChapter: BookChapter,
      bookSource: BookSource,
      baseUrl: String,
      redirectUrl: String,
      nextChapterUrl: String? = ...,
      debugLog: DebugLog? = ...
   ): String {
      var `$continuation`: Continuation;
      label183: {
         if (`$completion` is <unrepresentable>) {
            `$continuation` = `$completion` as <unrepresentable>;
            if (((`$completion` as <unrepresentable>).label and Integer.MIN_VALUE) != 0) {
               `$continuation`.label -= Integer.MIN_VALUE;
               break label183;
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
               return this.this$0.analyzeContent(null, null, null, null, null, null, null, null, this);
            }
         };
      }

      var content: StringBuilder;
      var contentRule: ContentRule;
      var analyzeRule: AnalyzeRule;
      label210: {
         val `$result`: Any = `$continuation`.result;
         val var28: Any = IntrinsicsKt.getCOROUTINE_SUSPENDED();
         var mNextChapterUrl: java.lang.String;
         var nextUrlList: ArrayList;
         var contentData: ObjectRef;
         var contentStr: ObjectRef;
         switch ($continuation.label) {
            case 0:
               ResultKt.throwOnFailure(`$result`);
               if (body == null) {
                  throw new Exception("error_get_web_content");
               }

               if (debugLog != null) {
                  DebugLog.DefaultImpls.log$default(debugLog, bookSource.getBookSourceUrl(), Intrinsics.stringPlus("≡获取成功:", baseUrl), false, 4, null);
               }

               mNextChapterUrl = if (nextChapterUrl as java.lang.CharSequence != null && nextChapterUrl.length() != 0)
                  nextChapterUrl
                  else
                  null as java.lang.String;
               content = new StringBuilder();
               nextUrlList = CollectionsKt.arrayListOf(new java.lang.String[]{redirectUrl});
               contentRule = bookSource.getContentRule();
               analyzeRule = new AnalyzeRule(book, bookSource, debugLog).setContent(body, baseUrl);
               analyzeRule.setRedirectUrl(redirectUrl);
               analyzeRule.setChapter(bookChapter);
               analyzeRule.setNextChapterUrl(mNextChapterUrl);
               JobKt.ensureActive(`$continuation`.getContext());
               contentData = new ObjectRef();
               contentData.element = (T)analyzeContent$default(
                  this, book, baseUrl, redirectUrl, body, contentRule, bookChapter, bookSource, mNextChapterUrl, false, debugLog, 256, null
               );
               content.append((contentData.element as Pair).getFirst() as java.lang.String);
               if (((contentData.element as Pair).getSecond() as java.util.List).size() != 1) {
                  if (((contentData.element as Pair).getSecond() as java.util.List).size() <= 1) {
                     break label210;
                  }

                  JobKt.ensureActive(`$continuation`.getContext());
                  if (debugLog != null) {
                     DebugLog.DefaultImpls.log$default(
                        debugLog,
                        bookSource.getBookSourceUrl(),
                        Intrinsics.stringPlus("◇并发解析正文,总页数:", Boxing.boxInt(((contentData.element as Pair).getSecond() as java.util.List).size())),
                        false,
                        4,
                        null
                     );
                  }

                  val var56: CoroutineContext = Dispatchers.getIO();
                  val var10001: Function2 = (
                     new Function2<CoroutineScope, Continuation<? super Unit>, Object>(
                        contentData, bookSource, book, debugLog, contentRule, bookChapter, mNextChapterUrl, content, null
                     ) {
                        Object L$1;
                        Object L$2;
                        Object L$3;
                        int I$0;
                        int I$1;
                        int label;

                        {
                           super(2, `$completionx`);
                           this.$contentData = `$contentData`;
                           this.$bookSource = `$bookSource`;
                           this.$book = `$book`;
                           this.$debugLog = `$debugLog`;
                           this.$contentRule = `$contentRule`;
                           this.$bookChapter = `$bookChapter`;
                           this.$mNextChapterUrl = `$mNextChapterUrl`;
                           this.$content = `$content`;
                        }

                        // $VF: Irreducible bytecode was duplicated to produce valid code
                        @Nullable
                        @Override
                        public final Object invokeSuspend(@NotNull Object $result) {
                           val var15: Any = IntrinsicsKt.getCOROUTINE_SUSPENDED();
                           var `$this$withContext`: CoroutineScope;
                           var var5: StringBuilder;
                           var var7: Array<Deferred>;
                           var var8: Int;
                           var var9: Int;
                           switch (this.label) {
                              case 0:
                                 ResultKt.throwOnFailure(`$result`);
                                 `$this$withContext` = this.L$0 as CoroutineScope;
                                 var `$this$forEach$iv`: Int = 0;
                                 val var17: Int = this.$contentData.element.getSecond().size();

                                 val var18: Array<Deferred>;
                                 for (var18 = new Deferred[var17]; $this$forEach$iv < var17; $this$forEach$iv++) {
                                    var18[`$this$forEach$iv`] = BuildersKt.async$default(
                                       `$this$withContext`,
                                       Dispatchers.getIO(),
                                       null,
                                       (
                                          new Function2<CoroutineScope, Continuation<? super java.lang.String>, Object>(
                                             this.$contentData,
                                             `$this$forEach$iv`,
                                             this.$bookSource,
                                             this.$book,
                                             this.$debugLog,
                                             this.$contentRule,
                                             this.$bookChapter,
                                             this.$mNextChapterUrl,
                                             null
                                          ) {
                                             Object L$0;
                                             int label;

                                             {
                                                super(2, `$completionx`);
                                                this.$contentData = `$contentData`;
                                                this.$tmp = `$tmp`;
                                                this.$bookSource = `$bookSource`;
                                                this.$book = `$book`;
                                                this.$debugLog = `$debugLog`;
                                                this.$contentRule = `$contentRule`;
                                                this.$bookChapter = `$bookChapter`;
                                                this.$mNextChapterUrl = `$mNextChapterUrl`;
                                             }

                                             @Nullable
                                             @Override
                                             public final Object invokeSuspend(@NotNull Object $result) {
                                                val var5: Any = IntrinsicsKt.getCOROUTINE_SUSPENDED();
                                                var urlStr: java.lang.String;
                                                var var10000: Any;
                                                switch (this.label) {
                                                   case 0:
                                                      ResultKt.throwOnFailure(`$result`);
                                                      urlStr = this.$contentData.element.getSecond().get(this.$tmp);
                                                      val analyzeUrl: AnalyzeUrl = new AnalyzeUrl(
                                                         urlStr,
                                                         null,
                                                         null,
                                                         null,
                                                         null,
                                                         null,
                                                         this.$bookSource,
                                                         this.$book,
                                                         null,
                                                         BaseSource.DefaultImpls.getHeaderMap$default(this.$bookSource, false, 1, null),
                                                         this.$debugLog,
                                                         318,
                                                         null
                                                      );
                                                      val var10004: Continuation = this;
                                                      this.L$0 = urlStr;
                                                      this.label = 1;
                                                      var10000 = AnalyzeUrl.getStrResponseAwait$default(analyzeUrl, null, null, false, var10004, 7, null);
                                                      if (var10000 === var5) {
                                                         return var5;
                                                      }
                                                      break;
                                                   case 1:
                                                      urlStr = this.L$0 as java.lang.String;
                                                      ResultKt.throwOnFailure(`$result`);
                                                      var10000 = `$result`;
                                                      break;
                                                   default:
                                                      throw new IllegalStateException("call to 'resume' before 'invoke' with coroutine");
                                                }

                                                val res: StrResponse = var10000 as StrResponse;
                                                var10000 = BookContent.INSTANCE;
                                                val var10001: Book = this.$book;
                                                val var10003: java.lang.String = res.getUrl();
                                                val var7: java.lang.String = res.getBody();
                                                return BookContent.access$analyzeContent(
                                                      (BookContent)var10000,
                                                      var10001,
                                                      urlStr,
                                                      var10003,
                                                      var7,
                                                      this.$contentRule,
                                                      this.$bookChapter,
                                                      this.$bookSource,
                                                      this.$mNextChapterUrl,
                                                      false,
                                                      this.$debugLog
                                                   )
                                                   .getFirst();
                                             }

                                             @NotNull
                                             @Override
                                             public final Continuation<Unit> create(@Nullable Object value, @NotNull Continuation<?> $completion) {
                                                return new <anonymous constructor>(
                                                   this.$contentData,
                                                   this.$tmp,
                                                   this.$bookSource,
                                                   this.$book,
                                                   this.$debugLog,
                                                   this.$contentRule,
                                                   this.$bookChapter,
                                                   this.$mNextChapterUrl,
                                                   `$completion`
                                                );
                                             }

                                             @Nullable
                                             public final Object invoke(@NotNull CoroutineScope p1, @Nullable Continuation<? super java.lang.String> p2) {
                                                return (this.create(p1, p2) as <unrepresentable>).invokeSuspend(Unit.INSTANCE);
                                             }
                                          }
                                       ) as Function2,
                                       2,
                                       null
                                    );
                                 }

                                 var5 = this.$content;
                                 var7 = var18;
                                 var8 = var18.length;
                                 var9 = 0;
                                 break;
                              case 1:
                                 var9 = this.I$1;
                                 var8 = this.I$0;
                                 val var13: StringBuilder = this.L$3 as StringBuilder;
                                 var7 = this.L$2 as Array<Deferred>;
                                 var5 = this.L$1 as StringBuilder;
                                 `$this$withContext` = this.L$0 as CoroutineScope;
                                 ResultKt.throwOnFailure(`$result`);
                                 var13.append(`$result` as java.lang.String);
                                 var9++;
                                 break;
                              default:
                                 throw new IllegalStateException("call to 'resume' before 'invoke' with coroutine");
                           }

                           while (var9 < var8) {
                              val `element$iv`: Any = var7[var9];
                              JobKt.ensureActive(`$this$withContext`.getCoroutineContext());
                              val var22: StringBuilder = var5.append("\n");
                              this.L$0 = `$this$withContext`;
                              this.L$1 = var5;
                              this.L$2 = var7;
                              this.L$3 = var22;
                              this.I$0 = var8;
                              this.I$1 = var9;
                              this.label = 1;
                              val var10000: Any = `element$iv`.await(this);
                              if (var10000 === var15) {
                                 return var15;
                              }

                              var22.append(var10000 as java.lang.String);
                              var9++;
                           }

                           return Unit.INSTANCE;
                        }

                        @NotNull
                        @Override
                        public final Continuation<Unit> create(@Nullable Object value, @NotNull Continuation<?> $completion) {
                           val var3: Function2 = new <anonymous constructor>(
                              this.$contentData,
                              this.$bookSource,
                              this.$book,
                              this.$debugLog,
                              this.$contentRule,
                              this.$bookChapter,
                              this.$mNextChapterUrl,
                              this.$content,
                              `$completion`
                           );
                           var3.L$0 = value;
                           return var3 as Continuation<Unit>;
                        }

                        @Nullable
                        public final Object invoke(@NotNull CoroutineScope p1, @Nullable Continuation<? super Unit> p2) {
                           return (this.create(p1, p2) as <unrepresentable>).invokeSuspend(Unit.INSTANCE);
                        }
                     }
                  ) as Function2;
                  `$continuation`.L$0 = bookChapter;
                  `$continuation`.L$1 = bookSource;
                  `$continuation`.L$2 = debugLog;
                  `$continuation`.L$3 = content;
                  `$continuation`.L$4 = contentRule;
                  `$continuation`.L$5 = analyzeRule;
                  `$continuation`.label = 2;
                  if (BuildersKt.withContext(var56, var10001, `$continuation`) === var28) {
                     return var28;
                  }
                  break label210;
               }

               contentStr = new ObjectRef();
               contentStr.element = (T)((contentData.element as Pair).getSecond() as java.util.List).get(0);
               break;
            case 1:
               contentStr = `$continuation`.L$11 as ObjectRef;
               contentData = `$continuation`.L$10 as ObjectRef;
               analyzeRule = `$continuation`.L$9 as AnalyzeRule;
               contentRule = `$continuation`.L$8 as ContentRule;
               nextUrlList = `$continuation`.L$7 as ArrayList;
               content = `$continuation`.L$6 as StringBuilder;
               mNextChapterUrl = `$continuation`.L$5 as java.lang.String;
               debugLog = `$continuation`.L$4 as DebugLog;
               redirectUrl = `$continuation`.L$3 as java.lang.String;
               bookSource = `$continuation`.L$2 as BookSource;
               bookChapter = `$continuation`.L$1 as BookChapter;
               book = `$continuation`.L$0 as Book;
               ResultKt.throwOnFailure(`$result`);
               val replaceRegex: StrResponse = `$result` as StrResponse;
               val var18: java.lang.String = (`$result` as StrResponse).getBody();
               if (var18 != null) {
                  contentData.element = (T)INSTANCE.analyzeContent(
                     book,
                     contentStr.element as java.lang.String,
                     replaceRegex.getUrl(),
                     var18,
                     contentRule,
                     bookChapter,
                     bookSource,
                     mNextChapterUrl,
                     false,
                     debugLog
                  );
                  contentStr.element = (T)(if (!((contentData.element as Pair).getSecond() as java.util.Collection).isEmpty())
                     ((contentData.element as Pair).getSecond() as java.util.List).get(0) as java.lang.String
                     else
                     "");
                  content.append("\n").append((contentData.element as Pair).getFirst() as java.lang.String);
               }
               break;
            case 2:
               analyzeRule = `$continuation`.L$5 as AnalyzeRule;
               contentRule = `$continuation`.L$4 as ContentRule;
               content = `$continuation`.L$3 as StringBuilder;
               debugLog = `$continuation`.L$2 as DebugLog;
               bookSource = `$continuation`.L$1 as BookSource;
               bookChapter = `$continuation`.L$0 as BookChapter;
               ResultKt.throwOnFailure(`$result`);
               break label210;
            default:
               throw new IllegalStateException("call to 'resume' before 'invoke' with coroutine");
         }

         while (true) {
            if ((contentStr.element as java.lang.CharSequence).length() <= 0
               || nextUrlList.contains(contentStr.element)
               || mNextChapterUrl != null
                  && mNextChapterUrl.length() != 0
                  && NetworkUtils.INSTANCE.getAbsoluteURL(redirectUrl, contentStr.element as java.lang.String)
                     == NetworkUtils.INSTANCE.getAbsoluteURL(redirectUrl, mNextChapterUrl)) {
               break;
            }

            nextUrlList.add(contentStr.element);
            JobKt.ensureActive(`$continuation`.getContext());
            var var10000: AnalyzeUrl = new AnalyzeUrl(
               contentStr.element as java.lang.String,
               null,
               null,
               null,
               null,
               null,
               bookSource,
               book,
               null,
               BaseSource.DefaultImpls.getHeaderMap$default(bookSource, false, 1, null),
               debugLog,
               318,
               null
            );
            `$continuation`.L$0 = book;
            `$continuation`.L$1 = bookChapter;
            `$continuation`.L$2 = bookSource;
            `$continuation`.L$3 = redirectUrl;
            `$continuation`.L$4 = debugLog;
            `$continuation`.L$5 = mNextChapterUrl;
            `$continuation`.L$6 = content;
            `$continuation`.L$7 = nextUrlList;
            `$continuation`.L$8 = contentRule;
            `$continuation`.L$9 = analyzeRule;
            `$continuation`.L$10 = contentData;
            `$continuation`.L$11 = contentStr;
            `$continuation`.label = 1;
            var10000 = (AnalyzeUrl)AnalyzeUrl.getStrResponseAwait$default(var10000, null, null, false, `$continuation`, 7, null);
            if (var10000 === var28) {
               return var28;
            }

            val var36: StrResponse = var10000 as StrResponse;
            val var41: java.lang.String = (var10000 as StrResponse).getBody();
            if (var41 != null) {
               contentData.element = (T)INSTANCE.analyzeContent(
                  book, contentStr.element as java.lang.String, var36.getUrl(), var41, contentRule, bookChapter, bookSource, mNextChapterUrl, false, debugLog
               );
               contentStr.element = (T)(if (!((contentData.element as Pair).getSecond() as java.util.Collection).isEmpty())
                  ((contentData.element as Pair).getSecond() as java.util.List).get(0) as java.lang.String
                  else
                  "");
               content.append("\n").append((contentData.element as Pair).getFirst() as java.lang.String);
            }
         }

         if (debugLog != null) {
            DebugLog.DefaultImpls.log$default(
               debugLog, bookSource.getBookSourceUrl(), Intrinsics.stringPlus("◇本章总页数:", Boxing.boxInt(nextUrlList.size())), false, 4, null
            );
         }
      }

      var var37: java.lang.String = content.toString();
      var var33: java.lang.String = var37;
      var37 = contentRule.getReplaceRegex();
      if (var37 != null && var37.length() != 0) {
         var33 = AnalyzeRule.getString$default(analyzeRule, var37, var37, false, 4, null);
      }

      if (debugLog != null) {
         DebugLog.DefaultImpls.log$default(debugLog, bookSource.getBookSourceUrl(), "┌获取章节名称", false, 4, null);
      }

      if (debugLog != null) {
         DebugLog.DefaultImpls.log$default(debugLog, bookSource.getBookSourceUrl(), Intrinsics.stringPlus("└", bookChapter.getTitle()), false, 4, null);
      }

      if (debugLog != null) {
         DebugLog.DefaultImpls.log$default(debugLog, bookSource.getBookSourceUrl(), "┌获取正文内容 (长度：${var33.length()})", false, 4, null);
      }

      if (var33.length() > 300) {
         if (debugLog != null) {
            val var57: java.lang.String = bookSource.getBookSourceUrl();
            var var10002: StringBuilder = new StringBuilder().append("└\n");
            if (var33 == null) {
               throw new NullPointerException("null cannot be cast to non-null type java.lang.String");
            }

            var var10003: java.lang.String = var33.substring(0, 150);
            var10002 = var10002.append(var10003).append(" ... ");
            val var47: Int = var33.length() - 150;
            val var50: Int = var33.length();
            if (var33 == null) {
               throw new NullPointerException("null cannot be cast to non-null type java.lang.String");
            }

            var10003 = var33.substring(var47, var50);
            DebugLog.DefaultImpls.log$default(debugLog, var57, var10002.append(var10003).toString(), false, 4, null);
         }
      } else if (debugLog != null) {
         DebugLog.DefaultImpls.log$default(debugLog, bookSource.getBookSourceUrl(), Intrinsics.stringPlus("└\n", var33), false, 4, null);
      }

      return var33;
   }

   @Throws(java/lang/Exception::class)
   private fun analyzeContent(
      book: Book,
      baseUrl: String,
      redirectUrl: String,
      body: String,
      contentRule: ContentRule,
      chapter: BookChapter,
      bookSource: BookSource,
      nextChapterUrl: String?,
      printLog: Boolean = true,
      debugLog: DebugLog? = null
   ): Pair<String, List<String>> {
      val analyzeRule: AnalyzeRule = new AnalyzeRule(book, bookSource, debugLog);
      analyzeRule.setContent(body, baseUrl);
      analyzeRule.setChapter(chapter);
      val rUrl: URL = analyzeRule.setRedirectUrl(redirectUrl);
      analyzeRule.setNextChapterUrl(nextChapterUrl);
      val nextUrlList: ArrayList = new ArrayList();
      analyzeRule.setChapter(chapter);
      val var23: java.lang.String = HtmlFormatter.INSTANCE
         .formatKeepImg(AnalyzeRule.getString$default(analyzeRule, contentRule.getContent(), null, false, 6, null), rUrl);
      val nextUrlRule: java.lang.String = contentRule.getNextContentUrl();
      if (nextUrlRule != null && nextUrlRule.length() != 0) {
         if (printLog && debugLog != null) {
            DebugLog.DefaultImpls.log$default(debugLog, bookSource.getBookSourceUrl(), "┌获取正文下一页链接", false, 4, null);
         }

         val var24: java.util.List = AnalyzeRule.getStringList$default(analyzeRule, nextUrlRule, null, true, 2, null);
         if (var24 != null) {
            nextUrlList.addAll(var24);
         }

         if (printLog && debugLog != null) {
            DebugLog.DefaultImpls.log$default(
               debugLog,
               bookSource.getBookSourceUrl(),
               Intrinsics.stringPlus("└", CollectionsKt.joinToString$default(nextUrlList, "，", null, null, 0, null, null, 62, null)),
               false,
               4,
               null
            );
         }
      }

      return new Pair<>(var23, nextUrlList);
   }
}
