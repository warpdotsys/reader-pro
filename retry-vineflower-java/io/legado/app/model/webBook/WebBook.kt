package io.legado.app.model.webBook

import io.legado.app.data.entities.Book
import io.legado.app.data.entities.BookChapter
import io.legado.app.data.entities.BookSource
import io.legado.app.data.entities.SearchBook
import io.legado.app.exception.NoStackTraceException
import io.legado.app.help.http.StrResponse
import io.legado.app.model.Debug
import io.legado.app.model.DebugLog
import io.legado.app.model.DebugLog.DefaultImpls
import io.legado.app.model.analyzeRule.AnalyzeUrl
import java.util.ArrayList
import kotlin.Result.Companion
import kotlin.coroutines.Continuation
import kotlin.coroutines.intrinsics.IntrinsicsKt
import kotlin.coroutines.jvm.internal.Boxing
import kotlin.coroutines.jvm.internal.ContinuationImpl
import kotlin.jvm.internal.Intrinsics
import kotlin.jvm.internal.Ref.ObjectRef
import org.jetbrains.annotations.NotNull
import org.jetbrains.annotations.Nullable

public class WebBook(bookSource: BookSource, debugLog: Boolean = true, debugLogger: DebugLog? = null, userNameSpace: String? = null) {
   public final val bookSource: BookSource
   public final val debugLog: Boolean

   public final var debugLogger: DebugLog?
      internal set

   public final val debugger: DebugLog?
      public final get() {
         if (this.debugLogger != null) {
            return this.debugLogger;
         } else {
            return if (this.debugLog) Debug.INSTANCE else null;
         }
      }


   public final val sourceUrl: String
      public final get() {
         return this.bookSource.getBookSourceUrl();
      }


   public final val userNS: String
      public final get() {
         return if (this.userNameSpace == null) "unknow" else this.userNameSpace;
      }


   public final var userNameSpace: String?
      internal set

   init {
      this.bookSource = bookSource;
      this.debugLog = debugLog;
      this.debugLogger = debugLogger;
      this.userNameSpace = userNameSpace;
   }

   public constructor(bookSourceString: String, debugLog: Boolean = true, debugLogger: DebugLog? = null, userNameSpace: String? = null)  {
      val var6: Any = BookSource.Companion.fromJson-IoAF18A(bookSourceString);
      val var5: BookSource = (if (Result.isFailure-impl(var6)) null else var6) as BookSource;
      this(
         if (var5 == null)
            new BookSource(
               null,
               null,
               null,
               0,
               null,
               0,
               false,
               false,
               null,
               null,
               null,
               null,
               null,
               null,
               null,
               null,
               0L,
               0L,
               0,
               null,
               null,
               null,
               null,
               null,
               null,
               null,
               67108863,
               null
            )
            else
            var5,
         debugLog,
         debugLogger,
         userNameSpace
      );
   }

   public suspend fun searchBook(key: String, page: Int? = ...): List<SearchBook> {
      var `$continuation`: Continuation;
      label69: {
         if (`$completion` is <unrepresentable>) {
            `$continuation` = `$completion` as <unrepresentable>;
            if (((`$completion` as <unrepresentable>).label and Integer.MIN_VALUE) != 0) {
               `$continuation`.label -= Integer.MIN_VALUE;
               break label69;
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
               return this.this$0.searchBook(null, null, this);
            }
         };
      }

      var var39: java.util.List;
      label73: {
         label74: {
            val `$result`: Any = `$continuation`.result;
            val var27: Any = IntrinsicsKt.getCOROUTINE_SUSPENDED();
            var variableBook: SearchBook;
            var analyzeUrl: AnalyzeUrl;
            var res: ObjectRef;
            var var14: ObjectRef;
            switch ($continuation.label) {
               case 0:
                  ResultKt.throwOnFailure(`$result`);
                  variableBook = new SearchBook(null, null, null, 0, null, null, null, null, null, null, null, null, 0L, null, 0, 32767, null);
                  variableBook.setUserNameSpace(this.getUserNS());
                  this.getBookSource().setUserNameSpace(this.getUserNS());
                  this.getBookSource().setLogger(this.getDebugger());
                  val var6: java.lang.String = this.getBookSource().getSearchUrl();
                  if (var6 == null) {
                     var39 = null;
                     break label73;
                  }

                  analyzeUrl = new AnalyzeUrl(
                     var6,
                     key,
                     page,
                     null,
                     null,
                     this.getBookSource().getBookSourceUrl(),
                     this.getBookSource(),
                     variableBook,
                     null,
                     this.getBookSource().getHeaderMap(true),
                     this.getDebugger(),
                     280,
                     null
                  );
                  res = new ObjectRef();
                  var14 = res;
                  `$continuation`.L$0 = this;
                  `$continuation`.L$1 = variableBook;
                  `$continuation`.L$2 = analyzeUrl;
                  `$continuation`.L$3 = res;
                  `$continuation`.L$4 = res;
                  `$continuation`.label = 1;
                  var39 = (java.util.List)AnalyzeUrl.getStrResponseAwait$default(analyzeUrl, null, null, false, `$continuation`, 7, null);
                  if (var39 === var27) {
                     return var27;
                  }
                  break;
               case 1:
                  var14 = `$continuation`.L$4 as ObjectRef;
                  res = `$continuation`.L$3 as ObjectRef;
                  analyzeUrl = `$continuation`.L$2 as AnalyzeUrl;
                  variableBook = `$continuation`.L$1 as SearchBook;
                  this = `$continuation`.L$0 as WebBook;
                  ResultKt.throwOnFailure(`$result`);
                  var39 = (java.util.List)`$result`;
                  break;
               case 2:
                  ResultKt.throwOnFailure(`$result`);
                  var39 = (java.util.List)`$result`;
                  break label74;
               default:
                  throw new IllegalStateException("call to 'resume' before 'invoke' with coroutine");
            }

            var14.element = (T)var39;
            val `$this$map$iv`: java.lang.String = this.getBookSource().getLoginCheckJs();
            if (`$this$map$iv` != null && !StringsKt.isBlank(`$this$map$iv`)) {
               val var34: Any = analyzeUrl.evalJS(`$this$map$iv`, res.element);
               if (var34 == null) {
                  throw new NullPointerException("null cannot be cast to non-null type io.legado.app.help.http.StrResponse");
               }

               res.element = (T)(var34 as StrResponse);
            }

            val var38: BookList = BookList.INSTANCE;
            val var10001: java.lang.String = (res.element as StrResponse).getBody();
            val var10002: BookSource = this.getBookSource();
            val var10004: java.lang.String = (res.element as StrResponse).getUrl();
            val var10007: DebugLog = this.getDebugger();
            `$continuation`.L$0 = null;
            `$continuation`.L$1 = null;
            `$continuation`.L$2 = null;
            `$continuation`.L$3 = null;
            `$continuation`.L$4 = null;
            `$continuation`.label = 2;
            var39 = (java.util.List)var38.analyzeBookList(var10001, var10002, analyzeUrl, var10004, variableBook, true, var10007, `$continuation`);
            if (var39 === var27) {
               return var27;
            }
         }

         val var31: java.lang.Iterable = var39;
         val var32: java.util.Collection = new ArrayList(CollectionsKt.collectionSizeOrDefault(var39, 10));

         for (Object item$iv$iv : $this$map$iv) {
            val var36: SearchBook = var35 as SearchBook;
            (var35 as SearchBook).setTocHtml("");
            var36.setInfoHtml("");
            var32.add(var36);
         }

         var39 = var32 as java.util.List;
      }

      return if (var39 == null) new ArrayList() else var39;
   }

   public suspend fun exploreBook(url: String, page: Int? = ...): List<SearchBook> {
      var `$continuation`: Continuation;
      label45: {
         if (`$completion` is <unrepresentable>) {
            `$continuation` = `$completion` as <unrepresentable>;
            if (((`$completion` as <unrepresentable>).label and Integer.MIN_VALUE) != 0) {
               `$continuation`.label -= Integer.MIN_VALUE;
               break label45;
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
               return this.this$0.exploreBook(null, null, this);
            }
         };
      }

      val `$result`: Any = `$continuation`.result;
      val var19: Any = IntrinsicsKt.getCOROUTINE_SUSPENDED();
      var variableBook: SearchBook;
      var analyzeUrl: AnalyzeUrl;
      var res: ObjectRef;
      var var15: ObjectRef;
      var var21: Any;
      switch ($continuation.label) {
         case 0:
            ResultKt.throwOnFailure(`$result`);
            variableBook = new SearchBook(null, null, null, 0, null, null, null, null, null, null, null, null, 0L, null, 0, 32767, null);
            variableBook.setUserNameSpace(this.getUserNS());
            this.getBookSource().setUserNameSpace(this.getUserNS());
            this.getBookSource().setLogger(this.getDebugger());
            analyzeUrl = new AnalyzeUrl(
               url,
               null,
               page,
               null,
               null,
               this.getBookSource().getBookSourceUrl(),
               this.getBookSource(),
               variableBook,
               null,
               this.getBookSource().getHeaderMap(true),
               this.getDebugger(),
               282,
               null
            );
            res = new ObjectRef();
            var15 = res;
            `$continuation`.L$0 = this;
            `$continuation`.L$1 = variableBook;
            `$continuation`.L$2 = analyzeUrl;
            `$continuation`.L$3 = res;
            `$continuation`.L$4 = res;
            `$continuation`.label = 1;
            var21 = (BookList)AnalyzeUrl.getStrResponseAwait$default(analyzeUrl, null, null, false, `$continuation`, 7, null);
            if (var21 === var19) {
               return var19;
            }
            break;
         case 1:
            var15 = `$continuation`.L$4 as ObjectRef;
            res = `$continuation`.L$3 as ObjectRef;
            analyzeUrl = `$continuation`.L$2 as AnalyzeUrl;
            variableBook = `$continuation`.L$1 as SearchBook;
            this = `$continuation`.L$0 as WebBook;
            ResultKt.throwOnFailure(`$result`);
            var21 = (BookList)`$result`;
            break;
         case 2:
            ResultKt.throwOnFailure(`$result`);
            return `$result`;
         default:
            throw new IllegalStateException("call to 'resume' before 'invoke' with coroutine");
      }

      var15.element = (T)var21;
      val var7: java.lang.String = this.getBookSource().getLoginCheckJs();
      if (var7 != null && !StringsKt.isBlank(var7)) {
         val var20: Any = analyzeUrl.evalJS(var7, res.element);
         if (var20 == null) {
            throw new NullPointerException("null cannot be cast to non-null type io.legado.app.help.http.StrResponse");
         }

         res.element = (T)(var20 as StrResponse);
      }

      var21 = BookList.INSTANCE;
      val var10001: java.lang.String = (res.element as StrResponse).getBody();
      val var10002: BookSource = this.getBookSource();
      val var10004: java.lang.String = (res.element as StrResponse).getUrl();
      val var10007: DebugLog = this.getDebugger();
      `$continuation`.L$0 = null;
      `$continuation`.L$1 = null;
      `$continuation`.L$2 = null;
      `$continuation`.L$3 = null;
      `$continuation`.L$4 = null;
      `$continuation`.label = 2;
      var21 = (BookList)var21.analyzeBookList(var10001, var10002, analyzeUrl, var10004, variableBook, false, var10007, `$continuation`);
      return if (var21 === var19) var19 else var21;
   }

   public suspend fun getBookInfo(book: Book, canReName: Boolean = ...): Book {
      var `$continuation`: Continuation;
      label77: {
         if (`$completion` is <unrepresentable>) {
            `$continuation` = `$completion` as <unrepresentable>;
            if (((`$completion` as <unrepresentable>).label and Integer.MIN_VALUE) != 0) {
               `$continuation`.label -= Integer.MIN_VALUE;
               break label77;
            }
         }

         `$continuation` = new ContinuationImpl(this, `$completion`) {
            Object L$0;
            Object L$1;
            Object L$2;
            Object L$3;
            Object L$4;
            boolean Z$0;
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
               return this.this$0.getBookInfo(null, false, this);
            }
         };
      }

      label80: {
         val `$result`: Any = `$continuation`.result;
         val var18: Any = IntrinsicsKt.getCOROUTINE_SUSPENDED();
         var analyzeUrl: AnalyzeUrl;
         var res: ObjectRef;
         var var14: ObjectRef;
         var var10000: Any;
         switch ($continuation.label) {
            case 0:
               ResultKt.throwOnFailure(`$result`);
               book.setType(this.getBookSource().getBookSourceType());
               book.setUserNameSpace(this.getUserNS());
               this.getBookSource().setUserNameSpace(this.getUserNS());
               this.getBookSource().setLogger(this.getDebugger());
               val var19: java.lang.CharSequence = book.getInfoHtml();
               if (var19 != null && var19.length() != 0) {
                  var10000 = BookInfo.INSTANCE;
                  val var25: java.lang.String = book.getInfoHtml();
                  val var26: BookSource = this.getBookSource();
                  val var27: java.lang.String = book.getBookUrl();
                  val var28: java.lang.String = book.getBookUrl();
                  val var29: Boolean = canReName;
                  `$continuation`.L$0 = book;
                  `$continuation`.label = 1;
                  if (BookInfo.analyzeBookInfo$default((BookInfo)var10000, book, var25, var26, var27, var28, var29, null, `$continuation`, 64, null) === var18) {
                     return var18;
                  }

                  return book;
               }

               analyzeUrl = new AnalyzeUrl(
                  book.getBookUrl(),
                  null,
                  null,
                  null,
                  null,
                  this.getBookSource().getBookSourceUrl(),
                  this.getBookSource(),
                  book,
                  null,
                  this.getBookSource().getHeaderMap(true),
                  this.getDebugger(),
                  286,
                  null
               );
               res = new ObjectRef();
               var14 = res;
               `$continuation`.L$0 = this;
               `$continuation`.L$1 = book;
               `$continuation`.L$2 = analyzeUrl;
               `$continuation`.L$3 = res;
               `$continuation`.L$4 = res;
               `$continuation`.Z$0 = canReName;
               `$continuation`.label = 2;
               var10000 = AnalyzeUrl.getStrResponseAwait$default(analyzeUrl, null, null, false, `$continuation`, 7, null);
               if (var10000 === var18) {
                  return var18;
               }
               break;
            case 1:
               book = `$continuation`.L$0 as Book;
               ResultKt.throwOnFailure(`$result`);
               return book;
            case 2:
               canReName = `$continuation`.Z$0;
               var14 = `$continuation`.L$4 as ObjectRef;
               res = `$continuation`.L$3 as ObjectRef;
               analyzeUrl = `$continuation`.L$2 as AnalyzeUrl;
               book = `$continuation`.L$1 as Book;
               this = `$continuation`.L$0 as WebBook;
               ResultKt.throwOnFailure(`$result`);
               var10000 = `$result`;
               break;
            case 3:
               book = `$continuation`.L$0 as Book;
               ResultKt.throwOnFailure(`$result`);
               break label80;
            default:
               throw new IllegalStateException("call to 'resume' before 'invoke' with coroutine");
         }

         var14.element = (T)var10000;
         val var21: java.lang.String = this.getBookSource().getLoginCheckJs();
         if (var21 != null && !StringsKt.isBlank(var21)) {
            val var22: Any = analyzeUrl.evalJS(var21, res.element);
            if (var22 == null) {
               throw new NullPointerException("null cannot be cast to non-null type io.legado.app.help.http.StrResponse");
            }

            res.element = (T)(var22 as StrResponse);
         }

         var10000 = BookInfo.INSTANCE;
         val var10002: java.lang.String = (res.element as StrResponse).getBody();
         val var10003: BookSource = this.getBookSource();
         val var10004: java.lang.String = book.getBookUrl();
         val var10005: java.lang.String = (res.element as StrResponse).getUrl();
         val var10006: Boolean = canReName;
         val var10007: DebugLog = this.getDebugger();
         `$continuation`.L$0 = book;
         `$continuation`.L$1 = null;
         `$continuation`.L$2 = null;
         `$continuation`.L$3 = null;
         `$continuation`.L$4 = null;
         `$continuation`.label = 3;
         if (((BookInfo)var10000).analyzeBookInfo(book, var10002, var10003, var10004, var10005, var10006, var10007, `$continuation`) === var18) {
            return var18;
         }
      }

      book.setTocHtml(null);
      return book;
   }

   public suspend fun getBookInfo(bookUrl: String, canReName: Boolean = ...): Book {
      val book: Book = new Book(
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
         null,
         null,
         false,
         null,
         -1,
         1,
         null
      );
      book.setBookUrl(bookUrl);
      book.setOrigin(this.getBookSource().getBookSourceUrl());
      book.setOriginName(this.getBookSource().getBookSourceName());
      book.setOriginOrder(this.getBookSource().getCustomOrder());
      book.setType(this.getBookSource().getBookSourceType());
      book.setUserNameSpace(this.getUserNS());
      return this.getBookInfo(book, canReName, `$completion`);
   }

   public suspend fun getChapterList(book: Book): List<BookChapter> {
      var `$continuation`: Continuation;
      label71: {
         if (`$completion` is <unrepresentable>) {
            `$continuation` = `$completion` as <unrepresentable>;
            if (((`$completion` as <unrepresentable>).label and Integer.MIN_VALUE) != 0) {
               `$continuation`.label -= Integer.MIN_VALUE;
               break label71;
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
               return this.this$0.getChapterList(null, this);
            }
         };
      }

      val `$result`: Any = `$continuation`.result;
      val var17: Any = IntrinsicsKt.getCOROUTINE_SUSPENDED();
      var analyzeUrl: AnalyzeUrl;
      var res: ObjectRef;
      var var13: ObjectRef;
      var var22: Any;
      switch ($continuation.label) {
         case 0:
            ResultKt.throwOnFailure(`$result`);
            book.setType(this.getBookSource().getBookSourceType());
            book.setUserNameSpace(this.getUserNS());
            this.getBookSource().setUserNameSpace(this.getUserNS());
            this.getBookSource().setLogger(this.getDebugger());
            if (book.getBookUrl() == book.getTocUrl()) {
               val var18: java.lang.CharSequence = book.getTocHtml();
               if (var18 != null && var18.length() != 0) {
                  var22 = BookChapterList.INSTANCE;
                  val var26: java.lang.String = book.getTocHtml();
                  val var27: BookSource = this.getBookSource();
                  val var28: java.lang.String = book.getTocUrl();
                  val var29: java.lang.String = book.getTocUrl();
                  `$continuation`.label = 1;
                  var22 = BookChapterList.analyzeChapterList$default((BookChapterList)var22, book, var26, var27, var28, var29, null, `$continuation`, 32, null);
                  if (var22 === var17) {
                     return var17;
                  }

                  return var22;
               }
            }

            analyzeUrl = new AnalyzeUrl(
               book.getTocUrl(),
               null,
               null,
               null,
               null,
               book.getBookUrl(),
               this.getBookSource(),
               book,
               null,
               this.getBookSource().getHeaderMap(true),
               this.getDebugger(),
               286,
               null
            );
            res = new ObjectRef();
            var13 = res;
            `$continuation`.L$0 = this;
            `$continuation`.L$1 = book;
            `$continuation`.L$2 = analyzeUrl;
            `$continuation`.L$3 = res;
            `$continuation`.L$4 = res;
            `$continuation`.label = 2;
            var22 = (BookChapterList)AnalyzeUrl.getStrResponseAwait$default(analyzeUrl, null, null, false, `$continuation`, 7, null);
            if (var22 === var17) {
               return var17;
            }
            break;
         case 1:
            ResultKt.throwOnFailure(`$result`);
            return `$result`;
         case 2:
            var13 = `$continuation`.L$4 as ObjectRef;
            res = `$continuation`.L$3 as ObjectRef;
            analyzeUrl = `$continuation`.L$2 as AnalyzeUrl;
            book = `$continuation`.L$1 as Book;
            this = `$continuation`.L$0 as WebBook;
            ResultKt.throwOnFailure(`$result`);
            var22 = (BookChapterList)`$result`;
            break;
         case 3:
            ResultKt.throwOnFailure(`$result`);
            return `$result`;
         default:
            throw new IllegalStateException("call to 'resume' before 'invoke' with coroutine");
      }

      var13.element = (T)var22;
      val var20: java.lang.String = this.getBookSource().getLoginCheckJs();
      if (var20 != null && !StringsKt.isBlank(var20)) {
         val var21: Any = analyzeUrl.evalJS(var20, res.element);
         if (var21 == null) {
            throw new NullPointerException("null cannot be cast to non-null type io.legado.app.help.http.StrResponse");
         }

         res.element = (T)(var21 as StrResponse);
      }

      var22 = BookChapterList.INSTANCE;
      val var10002: java.lang.String = (res.element as StrResponse).getBody();
      val var10003: BookSource = this.getBookSource();
      val var10004: java.lang.String = book.getTocUrl();
      val var10005: java.lang.String = (res.element as StrResponse).getUrl();
      val var10006: DebugLog = this.getDebugger();
      `$continuation`.L$0 = null;
      `$continuation`.L$1 = null;
      `$continuation`.L$2 = null;
      `$continuation`.L$3 = null;
      `$continuation`.L$4 = null;
      `$continuation`.label = 3;
      var22 = (BookChapterList)var22.analyzeChapterList(book, var10002, var10003, var10004, var10005, var10006, `$continuation`);
      return if (var22 === var17) var17 else var22;
   }

   public suspend fun getBookContent(book: Book, bookChapter: BookChapter, nextChapterUrl: String? = ...): String {
      var `$continuation`: Continuation;
      label64: {
         if (`$completion` is <unrepresentable>) {
            `$continuation` = `$completion` as <unrepresentable>;
            if (((`$completion` as <unrepresentable>).label and Integer.MIN_VALUE) != 0) {
               `$continuation`.label -= Integer.MIN_VALUE;
               break label64;
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
               return this.this$0.getBookContent(null, null, null, this);
            }
         };
      }

      val `$result`: Any = `$continuation`.result;
      val var10: Any = IntrinsicsKt.getCOROUTINE_SUSPENDED();
      var var16: Any;
      switch ($continuation.label) {
         case 0:
            ResultKt.throwOnFailure(`$result`);
            book.setUserNameSpace(this.getUserNS());
            this.getBookSource().setUserNameSpace(this.getUserNS());
            this.getBookSource().setLogger(this.getDebugger());
            val analyzeUrl: java.lang.CharSequence = this.getBookSource().getContentRule().getContent();
            if (analyzeUrl == null || analyzeUrl.length() == 0) {
               val var14: DebugLog = this.getDebugger();
               if (var14 != null) {
                  DefaultImpls.log$default(
                     var14, this.getBookSource().getBookSourceUrl(), Intrinsics.stringPlus("⇒正文规则为空,使用章节链接: ", bookChapter.getUrl()), false, 4, null
                  );
               }

               return bookChapter.getUrl();
            }

            if (bookChapter.isVolume() && StringsKt.startsWith$default(bookChapter.getUrl(), bookChapter.getTitle(), false, 2, null)) {
               val var12: DebugLog = this.getDebugger();
               if (var12 != null) {
                  DefaultImpls.log$default(var12, this.getBookSource().getBookSourceUrl(), "⇒一级目录正文不解析规则", false, 4, null);
               }

               val var13: java.lang.String = bookChapter.getTag();
               return if (var13 == null) "" else var13;
            }

            WebBookKt.access$getLogger$p().info("bookChapterUrl: {}", bookChapter.getUrl(), bookChapter.getAbsoluteURL());
            val var11: AnalyzeUrl = new AnalyzeUrl(
               bookChapter.getAbsoluteURL(),
               null,
               null,
               null,
               null,
               book.getTocUrl(),
               this.getBookSource(),
               book,
               bookChapter,
               this.getBookSource().getHeaderMap(true),
               this.getDebugger(),
               30,
               null
            );
            val var10001: java.lang.String = this.getBookSource().getContentRule().getWebJs();
            val var10002: java.lang.String = this.getBookSource().getContentRule().getSourceRegex();
            `$continuation`.L$0 = this;
            `$continuation`.L$1 = book;
            `$continuation`.L$2 = bookChapter;
            `$continuation`.L$3 = nextChapterUrl;
            `$continuation`.label = 1;
            var16 = (BookContent)AnalyzeUrl.getStrResponseAwait$default(var11, var10001, var10002, false, `$continuation`, 4, null);
            if (var16 === var10) {
               return var10;
            }
            break;
         case 1:
            nextChapterUrl = `$continuation`.L$3 as java.lang.String;
            bookChapter = `$continuation`.L$2 as BookChapter;
            book = `$continuation`.L$1 as Book;
            this = `$continuation`.L$0 as WebBook;
            ResultKt.throwOnFailure(`$result`);
            var16 = (BookContent)`$result`;
            break;
         case 2:
            ResultKt.throwOnFailure(`$result`);
            return `$result`;
         default:
            throw new IllegalStateException("call to 'resume' before 'invoke' with coroutine");
      }

      val var15: StrResponse = var16 as StrResponse;
      var16 = BookContent.INSTANCE;
      val var18: java.lang.String = var15.getBody();
      val var10004: BookSource = this.getBookSource();
      val var10005: java.lang.String = bookChapter.getUrl();
      val var10006: java.lang.String = var15.getUrl();
      val var10008: DebugLog = this.getDebugger();
      `$continuation`.L$0 = null;
      `$continuation`.L$1 = null;
      `$continuation`.L$2 = null;
      `$continuation`.L$3 = null;
      `$continuation`.label = 2;
      var16 = (BookContent)var16.analyzeContent(var18, book, bookChapter, var10004, var10005, var10006, nextChapterUrl, var10008, `$continuation`);
      return if (var16 === var10) var10 else var16;
   }

   public suspend fun preciseSearch(name: String, author: String): Result<Book> {
      var `$continuation`: Continuation;
      label99: {
         if (`$completion` is <unrepresentable>) {
            `$continuation` = `$completion` as <unrepresentable>;
            if (((`$completion` as <unrepresentable>).label and Integer.MIN_VALUE) != 0) {
               `$continuation`.label -= Integer.MIN_VALUE;
               break label99;
            }
         }

         `$continuation` = new ContinuationImpl(this, `$completion`) {
            Object L$0;
            Object L$1;
            Object L$2;
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
               val var10000: Any = this.this$0.preciseSearch-0E7RQCE(null, null, this);
               return if (var10000 === IntrinsicsKt.getCOROUTINE_SUSPENDED()) var10000 else Result.box-impl(var10000);
            }
         };
      }

      var var46: Book;
      label92: {
         label91: {
            var var10000: Any;
            label102: {
               val `$result`: Any = `$continuation`.result;
               val var16: Any = IntrinsicsKt.getCOROUTINE_SUSPENDED();
               switch ($continuation.label) {
                  case 0:
                     ResultKt.throwOnFailure(`$result`);

                     try {
                        val var24: Companion = Result.Companion;
                        `$continuation`.L$0 = this;
                        `$continuation`.L$1 = name;
                        `$continuation`.L$2 = author;
                        `$continuation`.label = 1;
                        var10000 = searchBook$default(this, name, null, `$continuation`, 2, null);
                     } catch (var21: java.lang.Throwable) {
                        val var28: Companion = Result.Companion;
                        return Result.constructor-impl(ResultKt.createFailure(var21));
                     }

                     if (var10000 === var16) {
                        return var16;
                     }
                     break;
                  case 1:
                     author = `$continuation`.L$2 as java.lang.String;
                     name = `$continuation`.L$1 as java.lang.String;
                     this = `$continuation`.L$0 as WebBook;

                     try {
                        ResultKt.throwOnFailure(`$result`);
                        var10000 = `$result`;
                        break;
                     } catch (var22: java.lang.Throwable) {
                        val var27: Companion = Result.Companion;
                        return Result.constructor-impl(ResultKt.createFailure(var22));
                     }
                  case 2:
                     try {
                        ResultKt.throwOnFailure(`$result`);
                        var10000 = `$result`;
                        break label102;
                     } catch (var20: java.lang.Throwable) {
                        val `$this$firstOrNull$iv`: Companion = Result.Companion;
                        return Result.constructor-impl(ResultKt.createFailure(var20));
                     }
                  default:
                     throw new IllegalStateException("call to 'resume' before 'invoke' with coroutine");
               }

               try {
                  val var9: java.util.Iterator = (var10000 as java.lang.Iterable).iterator();

                  while (true) {
                     if (!var9.hasNext()) {
                        var10000 = null;
                        break;
                     }

                     val searchBook: Any = var9.next();
                     if (Boxing.boxBoolean((searchBook as SearchBook).getName() == name && (searchBook as SearchBook).getAuthor() == author)) {
                        var10000 = searchBook;
                        break;
                     }
                  }

                  val var13: SearchBook = var10000 as SearchBook;
                  if (var10000 as SearchBook == null) {
                     break label91;
                  }

                  var46 = var13.toBook();
                  if (!StringsKt.isBlank(var46.getTocUrl())) {
                     break label92;
                  }

                  `$continuation`.L$0 = null;
                  `$continuation`.L$1 = null;
                  `$continuation`.L$2 = null;
                  `$continuation`.label = 2;
                  var10000 = getBookInfo$default(this, var46, false, `$continuation`, 2, null);
               } catch (var23: java.lang.Throwable) {
                  val var29: Companion = Result.Companion;
                  return Result.constructor-impl(ResultKt.createFailure(var23));
               }

               if (var10000 === var16) {
                  return var16;
               }
            }

            try {
               var46 = var10000 as Book;
               break label92;
            } catch (var19: java.lang.Throwable) {
               val var31: Companion = Result.Companion;
               return Result.constructor-impl(ResultKt.createFailure(var19));
            }
         }

         try {
            throw new NoStackTraceException("未搜索到 $name($author) 书籍");
         } catch (var18: java.lang.Throwable) {
            val var34: Companion = Result.Companion;
            return Result.constructor-impl(ResultKt.createFailure(var18));
         }
      }

      var var5: Any;
      try {
         var5 = Result.constructor-impl(var46);
      } catch (var17: java.lang.Throwable) {
         val var32: Companion = Result.Companion;
         var5 = Result.constructor-impl(ResultKt.createFailure(var17));
      }

      return var5;
   }
}
