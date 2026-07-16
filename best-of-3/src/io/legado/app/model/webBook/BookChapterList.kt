package io.legado.app.model.webBook

import com.htmake.reader.synth.SyntheticContinuation
import com.htmake.reader.synth.SyntheticFunction0
import com.htmake.reader.synth.SyntheticType

import io.legado.app.data.entities.BaseSource
import io.legado.app.data.entities.Book
import io.legado.app.data.entities.BookChapter
import io.legado.app.data.entities.BookSource
import io.legado.app.data.entities.rule.TocRule
import io.legado.app.exception.TocEmptyException
import io.legado.app.help.http.StrResponse
import io.legado.app.model.DebugLog
import io.legado.app.model.analyzeRule.AnalyzeRule
import io.legado.app.model.analyzeRule.AnalyzeUrl
import io.legado.app.utils.StringExtensionsKt
import io.legado.app.utils.TextUtils
import java.util.ArrayList
import java.util.LinkedHashSet
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

public object BookChapterList {
   public suspend fun analyzeChapterList(book: Book, body: String?, bookSource: BookSource, baseUrl: String, redirectUrl: String, debugLog: DebugLog? = ...): List<
         BookChapter
      > {
      var `$continuation`: Continuation;
      label178: {
         if (`$completion` is SyntheticContinuation) {
            `$continuation` = `$completion` as SyntheticContinuation;
            if (((`$completion` as SyntheticContinuation).label and Integer.MIN_VALUE) != 0) {
               `$continuation`.label -= Integer.MIN_VALUE;
               break label178;
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
            int I$0;
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
               return this.this$0.analyzeChapterList(null, null, null, null, null, null, this);
            }
         };
      }

      var chapterList: ArrayList;
      var reverse: Boolean;
      label170: {
         var tocRule: TocRule;
         var nextUrlList: ArrayList;
         var listRule: ObjectRef;
         var chapterData: ObjectRef;
         var list: ObjectRef;
         var var30: Any;
         label169: {
            val `$result`: Any = `$continuation`.result;
            var30 = IntrinsicsKt.getCOROUTINE_SUSPENDED();
            var var26: ObjectRef;
            var var61: Any;
            switch ($continuation.label) {
               case 0:
                  ResultKt.throwOnFailure(`$result`);
                  if (body == null) {
                     throw new Exception("error_get_web_content");
                  }

                  chapterList = new ArrayList();
                  if (debugLog != null) {
                     DebugLog.DefaultImpls.log$default(debugLog, bookSource.getBookSourceUrl(), Intrinsics.stringPlus("≡获取成功:", baseUrl), false, 4, null);
                  }

                  tocRule = bookSource.getTocRule();
                  nextUrlList = CollectionsKt.arrayListOf(new java.lang.String[]{redirectUrl});
                  reverse = false;
                  listRule = new ObjectRef();
                  val var33: java.lang.String = tocRule.getChapterList();
                  listRule.element = (T)(if (var33 == null) "" else var33);
                  if (StringsKt.startsWith$default(listRule.element as java.lang.String, "-", false, 2, null)) {
                     reverse = (boolean)1;
                     val var34: java.lang.String = listRule.element as java.lang.String;
                     if (listRule.element as java.lang.String == null) {
                        throw new NullPointerException("null cannot be cast to non-null type java.lang.String");
                     }

                     val var10001: java.lang.String = var34.substring(1);
                     listRule.element = (T)var10001;
                  }

                  if (StringsKt.startsWith$default(listRule.element as java.lang.String, "+", false, 2, null)) {
                     val var35: java.lang.String = listRule.element as java.lang.String;
                     if (listRule.element as java.lang.String == null) {
                        throw new NullPointerException("null cannot be cast to non-null type java.lang.String");
                     }

                     val var74: java.lang.String = var35.substring(1);
                     listRule.element = (T)var74;
                  }

                  chapterData = new ObjectRef();
                  var26 = chapterData;
                  val var78: java.lang.String = listRule.element as java.lang.String;
                  `$continuation`.L$0 = book;
                  `$continuation`.L$1 = bookSource;
                  `$continuation`.L$2 = debugLog;
                  `$continuation`.L$3 = chapterList;
                  `$continuation`.L$4 = tocRule;
                  `$continuation`.L$5 = nextUrlList;
                  `$continuation`.L$6 = listRule;
                  `$continuation`.L$7 = chapterData;
                  `$continuation`.L$8 = chapterData;
                  `$continuation`.I$0 = reverse;
                  `$continuation`.label = 1;
                  var61 = (BookChapterList)this.analyzeChapterList(
                     book, baseUrl, redirectUrl, body, tocRule, var78, bookSource, true, true, debugLog, `$continuation`
                  );
                  if (var61 === var30) {
                     return var30;
                  }
                  break;
               case 1:
                  reverse = (boolean)`$continuation`.I$0;
                  var26 = `$continuation`.L$8 as ObjectRef;
                  chapterData = `$continuation`.L$7 as ObjectRef;
                  listRule = `$continuation`.L$6 as ObjectRef;
                  nextUrlList = `$continuation`.L$5 as ArrayList;
                  tocRule = `$continuation`.L$4 as TocRule;
                  chapterList = `$continuation`.L$3 as ArrayList;
                  debugLog = `$continuation`.L$2 as DebugLog;
                  bookSource = `$continuation`.L$1 as BookSource;
                  book = `$continuation`.L$0 as Book;
                  ResultKt.throwOnFailure(`$result`);
                  var61 = (BookChapterList)`$result`;
                  break;
               case 2:
                  reverse = (boolean)`$continuation`.I$0;
                  list = `$continuation`.L$8 as ObjectRef;
                  chapterData = `$continuation`.L$7 as ObjectRef;
                  listRule = `$continuation`.L$6 as ObjectRef;
                  nextUrlList = `$continuation`.L$5 as ArrayList;
                  tocRule = `$continuation`.L$4 as TocRule;
                  chapterList = `$continuation`.L$3 as ArrayList;
                  debugLog = `$continuation`.L$2 as DebugLog;
                  bookSource = `$continuation`.L$1 as BookSource;
                  book = `$continuation`.L$0 as Book;
                  ResultKt.throwOnFailure(`$result`);
                  val `$this$forEachIndexed$iv`: java.lang.String = (`$result` as StrResponse).getBody();
                  if (`$this$forEachIndexed$iv` != null) {
                     var61 = INSTANCE;
                     val var10002: java.lang.String = list.element as java.lang.String;
                     val var10003: java.lang.String = list.element as java.lang.String;
                     val var10006: java.lang.String = listRule.element as java.lang.String;
                     `$continuation`.L$0 = book;
                     `$continuation`.L$1 = bookSource;
                     `$continuation`.L$2 = debugLog;
                     `$continuation`.L$3 = chapterList;
                     `$continuation`.L$4 = tocRule;
                     `$continuation`.L$5 = nextUrlList;
                     `$continuation`.L$6 = listRule;
                     `$continuation`.L$7 = chapterData;
                     `$continuation`.L$8 = list;
                     `$continuation`.L$9 = chapterData;
                     `$continuation`.I$0 = reverse;
                     `$continuation`.label = 3;
                     var61 = (BookChapterList)var61.analyzeChapterList(
                        book, var10002, var10003, `$this$forEachIndexed$iv`, tocRule, var10006, bookSource, true, false, debugLog, `$continuation`
                     );
                     if (var61 === var30) {
                        return var30;
                     }

                     chapterData.element = (T)var61;
                     val var56: java.lang.String = CollectionsKt.firstOrNull((chapterData.element as Pair).getSecond() as MutableList<java.lang.String>);
                     list.element = (T)(if (var56 == null) "" else var56);
                     Boxing.boxBoolean(chapterList.addAll((chapterData.element as Pair).getFirst() as java.util.Collection));
                  }
                  break label169;
               case 3:
                  reverse = (boolean)`$continuation`.I$0;
                  val var22: ObjectRef = `$continuation`.L$9 as ObjectRef;
                  list = `$continuation`.L$8 as ObjectRef;
                  chapterData = `$continuation`.L$7 as ObjectRef;
                  listRule = `$continuation`.L$6 as ObjectRef;
                  nextUrlList = `$continuation`.L$5 as ArrayList;
                  tocRule = `$continuation`.L$4 as TocRule;
                  chapterList = `$continuation`.L$3 as ArrayList;
                  debugLog = `$continuation`.L$2 as DebugLog;
                  bookSource = `$continuation`.L$1 as BookSource;
                  book = `$continuation`.L$0 as Book;
                  ResultKt.throwOnFailure(`$result`);
                  var22.element = (T)`$result`;
                  val index: java.lang.String = CollectionsKt.firstOrNull((chapterData.element as Pair).getSecond() as MutableList<java.lang.String>);
                  list.element = (T)(if (index == null) "" else index);
                  Boxing.boxBoolean(chapterList.addAll((chapterData.element as Pair).getFirst() as java.util.Collection));
                  break label169;
               case 4:
                  reverse = (boolean)`$continuation`.I$0;
                  chapterList = `$continuation`.L$2 as ArrayList;
                  debugLog = `$continuation`.L$1 as DebugLog;
                  book = `$continuation`.L$0 as Book;
                  ResultKt.throwOnFailure(`$result`);
                  break label170;
               default:
                  throw new IllegalStateException("call to 'resume' before 'invoke' with coroutine");
            }

            var26.element = (T)var61;
            chapterList.addAll((chapterData.element as Pair).getFirst() as java.util.Collection);
            switch (((java.util.List)((Pair)chapterData.element).getSecond()).size()) {
               case 0:
                  break label170;
               case 1:
                  list = new ObjectRef();
                  list.element = (T)((chapterData.element as Pair).getSecond() as java.util.List).get(0);
                  break;
               default:
                  if (debugLog != null) {
                     DebugLog.DefaultImpls.log$default(
                        debugLog,
                        bookSource.getBookSourceUrl(),
                        Intrinsics.stringPlus("◇并发解析目录,总页数:", Boxing.boxInt(((chapterData.element as Pair).getSecond() as java.util.List).size())),
                        false,
                        4,
                        null
                     );
                  }

                  val var64: CoroutineContext = Dispatchers.getIO();
                  val var75: Function2 = (
                     new Function2<CoroutineScope, Continuation<? super Unit>, Object>(
                        chapterData, bookSource, book, debugLog, tocRule, listRule, chapterList, null
                     ) {
                        Object L$1;
                        Object L$2;
                        int I$0;
                        int I$1;
                        int label;

                        {
                           super(2, `$completionx`);
                           this.$chapterData = `$chapterData`;
                           this.$bookSource = `$bookSource`;
                           this.$book = `$book`;
                           this.$debugLog = `$debugLog`;
                           this.$tocRule = `$tocRule`;
                           this.$listRule = `$listRule`;
                           this.$chapterList = `$chapterList`;
                        }

                        // NOTE: decompiler split irreducible bytecode (logic preserved)
                        @Nullable
                        @Override
                        public final Object invokeSuspend(@NotNull Object $result) {
                           val var15: Any = IntrinsicsKt.getCOROUTINE_SUSPENDED();
                           var var5: ArrayList;
                           var var7: Array<Deferred>;
                           var var8: Int;
                           var var9: Int;
                           switch (this.label) {
                              case 0:
                                 ResultKt.throwOnFailure(`$result`);
                                 val `$this$withContext`: CoroutineScope = this.L$0 as CoroutineScope;
                                 var `$this$forEach$iv`: Int = 0;
                                 val var17: Int = this.$chapterData.element.getSecond().size();

                                 val var18: Array<Deferred>;
                                 for (var18 = new Deferred[var17]; $this$forEach$iv < var17; $this$forEach$iv++) {
                                    var18[`$this$forEach$iv`] = BuildersKt.async$default(
                                       `$this$withContext`,
                                       Dispatchers.getIO(),
                                       null,
                                       (
                                          new Function2<CoroutineScope, Continuation<? super java.util.List<? extends BookChapter>>, Object>(
                                             this.$chapterData,
                                             `$this$forEach$iv`,
                                             this.$bookSource,
                                             this.$book,
                                             this.$debugLog,
                                             this.$tocRule,
                                             this.$listRule,
                                             null
                                          ) {
                                             Object L$0;
                                             int label;

                                             {
                                                super(2, `$completionx`);
                                                this.$chapterData = `$chapterData`;
                                                this.$tmp = `$tmp`;
                                                this.$bookSource = `$bookSource`;
                                                this.$book = `$book`;
                                                this.$debugLog = `$debugLog`;
                                                this.$tocRule = `$tocRule`;
                                                this.$listRule = `$listRule`;
                                             }

                                             @Nullable
                                             @Override
                                             public final Object invokeSuspend(@NotNull Object $result) {
                                                val var5: Any = IntrinsicsKt.getCOROUTINE_SUSPENDED();
                                                var urlStr: java.lang.String;
                                                var var6: Any;
                                                switch (this.label) {
                                                   case 0:
                                                      ResultKt.throwOnFailure(`$result`);
                                                      urlStr = this.$chapterData.element.getSecond().get(this.$tmp);
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
                                                      var6 = AnalyzeUrl.getStrResponseAwait$default(analyzeUrl, null, null, false, var10004, 7, null);
                                                      if (var6 === var5) {
                                                         return var5;
                                                      }
                                                      break;
                                                   case 1:
                                                      urlStr = this.L$0 as java.lang.String;
                                                      ResultKt.throwOnFailure(`$result`);
                                                      var6 = `$result`;
                                                      break;
                                                   case 2:
                                                      ResultKt.throwOnFailure(`$result`);
                                                      return (`$result` as Pair).getFirst();
                                                   default:
                                                      throw new IllegalStateException("call to 'resume' before 'invoke' with coroutine");
                                                }

                                                val res: StrResponse = var6 as StrResponse;
                                                var6 = BookChapterList.INSTANCE;
                                                val var10001: Book = this.$book;
                                                val var10003: java.lang.String = res.getUrl();
                                                val var8: java.lang.String = res.getBody();
                                                val var10005: TocRule = this.$tocRule;
                                                val var10006: java.lang.String = this.$listRule.element;
                                                val var10007: BookSource = this.$bookSource;
                                                val var10010: DebugLog = this.$debugLog;
                                                val var10011: Continuation = this;
                                                this.L$0 = null;
                                                this.label = 2;
                                                var6 = BookChapterList.access$analyzeChapterList(
                                                   (BookChapterList)var6,
                                                   var10001,
                                                   urlStr,
                                                   var10003,
                                                   var8,
                                                   var10005,
                                                   var10006,
                                                   var10007,
                                                   false,
                                                   false,
                                                   var10010,
                                                   var10011
                                                );
                                                return if (var6 === var5) var5 else (var6 as Pair).getFirst();
                                             }

                                             @NotNull
                                             @Override
                                             public final Continuation<Unit> create(@Nullable Object value, @NotNull Continuation<?> $completion) {
                                                return new <anonymous constructor>(
                                                   this.$chapterData,
                                                   this.$tmp,
                                                   this.$bookSource,
                                                   this.$book,
                                                   this.$debugLog,
                                                   this.$tocRule,
                                                   this.$listRule,
                                                   `$completion`
                                                );
                                             }

                                             @Nullable
                                             public final Object invoke(
                                                @NotNull CoroutineScope p1, @Nullable Continuation<? super java.util.List<BookChapter>> p2
                                             ) {
                                                return (this.create(p1, p2) as SyntheticContinuation).invokeSuspend(Unit.INSTANCE);
                                             }
                                          }
                                       ) as Function2,
                                       2,
                                       null
                                    );
                                 }

                                 var5 = this.$chapterList;
                                 var7 = var18;
                                 var8 = var18.length;
                                 var9 = 0;
                                 break;
                              case 1:
                                 var9 = this.I$1;
                                 var8 = this.I$0;
                                 val var13: ArrayList = this.L$2 as ArrayList;
                                 var7 = this.L$1 as Array<Deferred>;
                                 var5 = this.L$0 as ArrayList;
                                 ResultKt.throwOnFailure(`$result`);
                                 var13.addAll(`$result` as java.util.Collection);
                                 var9++;
                                 break;
                              default:
                                 throw new IllegalStateException("call to 'resume' before 'invoke' with coroutine");
                           }

                           while (var9 < var8) {
                              val `element$iv`: Any = var7[var9];
                              this.L$0 = var5;
                              this.L$1 = var7;
                              this.L$2 = var5;
                              this.I$0 = var8;
                              this.I$1 = var9;
                              this.label = 1;
                              val var10000: Any = `element$iv`.await(this);
                              if (var10000 === var15) {
                                 return var15;
                              }

                              var5.addAll(var10000 as java.util.Collection);
                              var9++;
                           }

                           return Unit.INSTANCE;
                        }

                        @NotNull
                        @Override
                        public final Continuation<Unit> create(@Nullable Object value, @NotNull Continuation<?> $completion) {
                           val var3: Function2 = new <anonymous constructor>(
                              this.$chapterData, this.$bookSource, this.$book, this.$debugLog, this.$tocRule, this.$listRule, this.$chapterList, `$completion`
                           );
                           var3.L$0 = value;
                           return var3 as Continuation<Unit>;
                        }

                        @Nullable
                        public final Object invoke(@NotNull CoroutineScope p1, @Nullable Continuation<? super Unit> p2) {
                           return (this.create(p1, p2) as SyntheticContinuation).invokeSuspend(Unit.INSTANCE);
                        }
                     }
                  ) as Function2;
                  `$continuation`.L$0 = book;
                  `$continuation`.L$1 = debugLog;
                  `$continuation`.L$2 = chapterList;
                  `$continuation`.L$3 = null;
                  `$continuation`.L$4 = null;
                  `$continuation`.L$5 = null;
                  `$continuation`.L$6 = null;
                  `$continuation`.L$7 = null;
                  `$continuation`.L$8 = null;
                  `$continuation`.I$0 = reverse;
                  `$continuation`.label = 4;
                  if (BuildersKt.withContext(var64, var75, `$continuation`) === var30) {
                     return var30;
                  }
                  break label170;
            }
         }

         while (true) {
            if ((list.element as java.lang.CharSequence).length() <= 0 || nextUrlList.contains(list.element)) {
               if (debugLog != null) {
                  DebugLog.DefaultImpls.log$default(
                     debugLog, bookSource.getBookSourceUrl(), Intrinsics.stringPlus("◇目录总页数:", Boxing.boxInt(nextUrlList.size())), false, 4, null
                  );
               }
               break;
            }

            nextUrlList.add(list.element);
            var var65: AnalyzeUrl = new AnalyzeUrl(
               list.element as java.lang.String,
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
            `$continuation`.L$1 = bookSource;
            `$continuation`.L$2 = debugLog;
            `$continuation`.L$3 = chapterList;
            `$continuation`.L$4 = tocRule;
            `$continuation`.L$5 = nextUrlList;
            `$continuation`.L$6 = listRule;
            `$continuation`.L$7 = chapterData;
            `$continuation`.L$8 = list;
            `$continuation`.L$9 = null;
            `$continuation`.I$0 = reverse;
            `$continuation`.label = 2;
            var65 = (AnalyzeUrl)AnalyzeUrl.getStrResponseAwait$default(var65, null, null, false, `$continuation`, 7, null);
            if (var65 === var30) {
               return var30;
            }

            val var43: java.lang.String = (var65 as StrResponse).getBody();
            if (var43 != null) {
               val var68: BookChapterList = INSTANCE;
               val var76: java.lang.String = list.element as java.lang.String;
               val var77: java.lang.String = list.element as java.lang.String;
               val var79: java.lang.String = listRule.element as java.lang.String;
               `$continuation`.L$0 = book;
               `$continuation`.L$1 = bookSource;
               `$continuation`.L$2 = debugLog;
               `$continuation`.L$3 = chapterList;
               `$continuation`.L$4 = tocRule;
               `$continuation`.L$5 = nextUrlList;
               `$continuation`.L$6 = listRule;
               `$continuation`.L$7 = chapterData;
               `$continuation`.L$8 = list;
               `$continuation`.L$9 = chapterData;
               `$continuation`.I$0 = reverse;
               `$continuation`.label = 3;
               var65 = (AnalyzeUrl)var68.analyzeChapterList(book, var76, var77, var43, tocRule, var79, bookSource, true, false, debugLog, `$continuation`);
               if (var65 === var30) {
                  return var30;
               }

               chapterData.element = (T)var65;
               val var57: java.lang.String = CollectionsKt.firstOrNull((chapterData.element as Pair).getSecond() as MutableList<java.lang.String>);
               list.element = (T)(if (var57 == null) "" else var57);
               Boxing.boxBoolean(chapterList.addAll((chapterData.element as Pair).getFirst() as java.util.Collection));
            }
         }
      }

      if (chapterList.isEmpty()) {
         throw new TocEmptyException("目录为空");
      } else {
         if (reverse == 0) {
            CollectionsKt.reverse(chapterList);
         }

         JobKt.ensureActive(`$continuation`.getContext());
         val var41: ArrayList = new ArrayList(new LinkedHashSet(chapterList));
         CollectionsKt.reverse(var41);
         if (debugLog != null) {
            DebugLog.DefaultImpls.log$default(debugLog, book.getOrigin(), Intrinsics.stringPlus("◇目录总数:", Boxing.boxInt(var41.size())), false, 4, null);
         }

         JobKt.ensureActive(`$continuation`.getContext());
         val var44: java.lang.Iterable = var41;
         var var47: Int = 0;

         for (Object item$iv : $this$forEachIndexed$iv) {
            val var52: Int = var47++;
            if (var52 < 0) {
               CollectionsKt.throwIndexOverflow();
            }

            (`item$iv` as BookChapter).setIndex(Boxing.boxInt(var52).intValue());
         }

         if (var41.size() > 0) {
            book.setLatestChapterTitle(CollectionsKt.last(var41).getTitle());
         }

         if (book.getTotalChapterNum() < var41.size()) {
            book.setLastCheckCount(var41.size() - book.getTotalChapterNum());
         }

         book.setTotalChapterNum(var41.size());
         JobKt.ensureActive(`$continuation`.getContext());
         return var41;
      }
   }

   private suspend fun analyzeChapterList(
      book: Book,
      baseUrl: String,
      redirectUrl: String,
      body: String,
      tocRule: TocRule,
      listRule: String,
      bookSource: BookSource,
      getNextUrl: Boolean = ...,
      log: Boolean = ...,
      debugLog: DebugLog? = ...
   ): Pair<List<BookChapter>, List<String>> {
      val analyzeRule: AnalyzeRule = new AnalyzeRule(book, bookSource, debugLog);
      AnalyzeRule.setContent$default(analyzeRule, body, null, 2, null).setBaseUrl(baseUrl);
      analyzeRule.setRedirectUrl(redirectUrl);
      val chapterList: ArrayList = new ArrayList();
      if (log && debugLog != null) {
         DebugLog.DefaultImpls.log$default(debugLog, bookSource.getBookSourceUrl(), "┌获取目录列表", false, 4, null);
      }

      val var36: java.util.List = analyzeRule.getElements(listRule);
      if (log && debugLog != null) {
         DebugLog.DefaultImpls.log$default(
            debugLog, bookSource.getBookSourceUrl(), Intrinsics.stringPlus("└列表大小:", Boxing.boxInt(var36.size())), false, 4, null
         );
      }

      val nextUrlList: ArrayList = new ArrayList();
      val var37: java.lang.String = tocRule.getNextTocUrl();
      if (getNextUrl && var37 != null && var37.length() != 0) {
         if (log && debugLog != null) {
            DebugLog.DefaultImpls.log$default(debugLog, bookSource.getBookSourceUrl(), "┌获取目录下一页列表", false, 4, null);
         }

         val var38: java.util.List = AnalyzeRule.getStringList$default(analyzeRule, var37, null, true, 2, null);
         if (var38 != null) {
            for (java.lang.String item : var38) {
               if (!(`index$iv` == redirectUrl)) {
                  nextUrlList.add(`index$iv`);
               }
            }
         }

         if (log && debugLog != null) {
            DebugLog.DefaultImpls.log$default(
               debugLog, bookSource.getBookSourceUrl(), Intrinsics.stringPlus("└", TextUtils.join("，\n", nextUrlList)), false, 4, null
            );
         }
      }

      JobKt.ensureActive(`$completion`.getContext());
      if (!var36.isEmpty()) {
         if (log && debugLog != null) {
            DebugLog.DefaultImpls.log$default(debugLog, bookSource.getBookSourceUrl(), "┌解析目录列表", false, 4, null);
         }

         val var40: java.util.List = AnalyzeRule.splitSourceRule$default(analyzeRule, tocRule.getChapterName(), false, 2, null);
         val var42: java.util.List = AnalyzeRule.splitSourceRule$default(analyzeRule, tocRule.getChapterUrl(), false, 2, null);
         val var44: java.util.List = AnalyzeRule.splitSourceRule$default(analyzeRule, tocRule.isVip(), false, 2, null);
         val var45: java.util.List = AnalyzeRule.splitSourceRule$default(analyzeRule, tocRule.getUpdateTime(), false, 2, null);
         val isVolumeRule: java.util.List = AnalyzeRule.splitSourceRule$default(analyzeRule, tocRule.isVolume(), false, 2, null);
         val var46: java.lang.Iterable = var36;
         var var48: Int = 0;

         for (Object item$iv : $this$forEachIndexed$iv) {
            val var27: Int = var48++;
            if (var27 < 0) {
               CollectionsKt.throwIndexOverflow();
            }

            val index: Int = Boxing.boxInt(var27).intValue();
            JobKt.ensureActive(`$completion`.getContext());
            AnalyzeRule.setContent$default(analyzeRule, `item$iv`, null, 2, null);
            val bookChapter: BookChapter = new BookChapter(
               null, null, false, redirectUrl, book.getBookUrl(), 0, null, null, null, null, null, null, null, 8167, null
            );
            analyzeRule.setChapter(bookChapter);
            bookChapter.setTitle(AnalyzeRule.getString$default(analyzeRule, var40, null, false, 6, null));
            bookChapter.setUrl(AnalyzeRule.getString$default(analyzeRule, var42, null, false, 6, null));
            bookChapter.setTag(AnalyzeRule.getString$default(analyzeRule, var45, null, false, 6, null));
            bookChapter.setUserNameSpace(book.getUserNameSpace());
            val var49: java.lang.String = AnalyzeRule.getString$default(analyzeRule, isVolumeRule, null, false, 6, null);
            bookChapter.setVolume(false);
            if (StringExtensionsKt.isTrue$default(var49, false, 1, null)) {
               bookChapter.setVolume(true);
            }

            if (bookChapter.getUrl().length() == 0) {
               if (bookChapter.isVolume()) {
                  bookChapter.setUrl(Intrinsics.stringPlus(bookChapter.getTitle(), Boxing.boxInt(index)));
                  if (log && debugLog != null) {
                     DebugLog.DefaultImpls.log$default(debugLog, bookSource.getBookSourceUrl(), "⇒一级目录$index未获取到url,使用标题替代", false, 4, null);
                  }
               } else {
                  bookChapter.setUrl(baseUrl);
                  if (log && debugLog != null) {
                     DebugLog.DefaultImpls.log$default(debugLog, bookSource.getBookSourceUrl(), "⇒目录$index未获取到url,使用baseUrl替代", false, 4, null);
                  }
               }
            }

            if (bookChapter.getTitle().length() > 0) {
               if (StringExtensionsKt.isTrue$default(AnalyzeRule.getString$default(analyzeRule, var44, null, false, 6, null), false, 1, null)) {
                  bookChapter.setTitle(Intrinsics.stringPlus("\ud83d\udd12", bookChapter.getTitle()));
               }

               chapterList.add(bookChapter);
            } else if (log && debugLog != null) {
               DebugLog.DefaultImpls.log$default(debugLog, bookSource.getBookSourceUrl(), "章节名为空", false, 4, null);
            }
         }

         if (log && debugLog != null) {
            DebugLog.DefaultImpls.log$default(debugLog, bookSource.getBookSourceUrl(), "└目录列表解析完成", false, 4, null);
         }

         if (chapterList.size() > 0) {
            if (log && debugLog != null) {
               DebugLog.DefaultImpls.log$default(debugLog, bookSource.getBookSourceUrl(), "≡首章信息", false, 4, null);
            }

            if (log && debugLog != null) {
               DebugLog.DefaultImpls.log$default(
                  debugLog, bookSource.getBookSourceUrl(), Intrinsics.stringPlus("◇章节名称:", (chapterList.get(0) as BookChapter).getTitle()), false, 4, null
               );
            }

            if (log && debugLog != null) {
               DebugLog.DefaultImpls.log$default(
                  debugLog, bookSource.getBookSourceUrl(), Intrinsics.stringPlus("◇章节链接:", (chapterList.get(0) as BookChapter).getUrl()), false, 4, null
               );
            }

            if (log && debugLog != null) {
               DebugLog.DefaultImpls.log$default(
                  debugLog, bookSource.getBookSourceUrl(), Intrinsics.stringPlus("◇章节信息:", (chapterList.get(0) as BookChapter).getTag()), false, 4, null
               );
            }

            if (log && debugLog != null) {
               DebugLog.DefaultImpls.log$default(
                  debugLog,
                  bookSource.getBookSourceUrl(),
                  Intrinsics.stringPlus("◇是否卷名:", Boxing.boxBoolean((chapterList.get(0) as BookChapter).isVolume())),
                  false,
                  4,
                  null
               );
            }
         } else if (log && debugLog != null) {
            DebugLog.DefaultImpls.log$default(debugLog, bookSource.getBookSourceUrl(), "章节列表为空", false, 4, null);
         }
      }

      return new Pair<>(chapterList, nextUrlList);
   }
}
