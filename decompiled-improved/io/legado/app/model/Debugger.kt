package io.legado.app.model

import io.legado.app.data.entities.Book
import io.legado.app.data.entities.BookChapter
import io.legado.app.data.entities.BookSource
import io.legado.app.data.entities.SearchBook
import io.legado.app.model.DebugLog.DefaultImpls
import io.legado.app.model.webBook.WebBook
import io.legado.app.utils.GsonExtensionsKt
import io.legado.app.utils.HtmlFormatter
import io.legado.app.utils.StringExtensionsKt
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import kotlin.coroutines.Continuation
import kotlin.coroutines.intrinsics.IntrinsicsKt
import kotlin.coroutines.jvm.internal.Boxing
import kotlin.coroutines.jvm.internal.ContinuationImpl
import kotlin.jvm.internal.Intrinsics
import org.jetbrains.annotations.NotNull
import org.jetbrains.annotations.Nullable

public class Debugger(logMsg: (String) -> Unit) : DebugLog {
   private final val debugTimeFormat: SimpleDateFormat
   public final val logMsg: (String) -> Unit
   private final var startTime: Long

   init {
      this.logMsg = logMsg;
      this.debugTimeFormat = new SimpleDateFormat("[mm:ss.SSS]", Locale.getDefault());
      this.startTime = System.currentTimeMillis();
   }

   public fun log(sourceUrl: String?, msg: String?) {
      this.log(sourceUrl, msg, false);
   }

   public override fun log(message: String) {
      this.logMsg.invoke("${this.debugTimeFormat.format(new Date(System.currentTimeMillis() - this.startTime))} $message");
   }

   public override fun log(sourceUrl: String?, msg: String?, isHtml: Boolean) {
      if (sourceUrl != null && msg != null) {
         DebuggerKt.access$getLogger$p().info("sourceUrl: {}, msg: {}", sourceUrl, msg);
         var printMsg: java.lang.String = msg;
         if (isHtml) {
            printMsg = HtmlFormatter.format$default(HtmlFormatter.INSTANCE, msg, null, 2, null);
         }

         this.logMsg.invoke("${this.debugTimeFormat.format(new Date(System.currentTimeMillis() - this.startTime))} $printMsg");
      }
   }

   public suspend fun startDebug(webBook: WebBook, key: String) {
      val bookSource: BookSource = webBook.getBookSource();
      webBook.setDebugLogger(this);
      this.startTime = System.currentTimeMillis();
      if (StringExtensionsKt.isAbsUrl(key)) {
         val var11: Book = new Book(
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
         var11.setOrigin(bookSource.getBookSourceUrl());
         var11.setBookUrl(key);
         this.log(bookSource.getBookSourceUrl(), Intrinsics.stringPlus("⇒开始访问详情页:", key));
         val var21: Any = this.infoDebug(webBook, var11, `$completion`);
         return if (var21 === IntrinsicsKt.getCOROUTINE_SUSPENDED()) var21 else Unit.INSTANCE;
      } else if (StringsKt.contains$default(key, "::", false, 2, null)) {
         val var10: java.lang.String = StringsKt.substringAfter$default(key, "::", null, 2, null);
         this.log(bookSource.getBookSourceUrl(), Intrinsics.stringPlus("⇒开始访问发现页:", var10));
         val var20: Any = this.exploreDebug(webBook, var10, `$completion`);
         return if (var20 === IntrinsicsKt.getCOROUTINE_SUSPENDED()) var20 else Unit.INSTANCE;
      } else if (StringsKt.startsWith$default(key, "++", false, 2, null)) {
         if (key == null) {
            throw new NullPointerException("null cannot be cast to non-null type java.lang.String");
         } else {
            var var18: java.lang.String = key.substring(2);
            val var12: Book = new Book(
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
            var12.setOrigin(bookSource.getBookSourceUrl());
            var12.setTocUrl(var18);
            this.log(bookSource.getBookSourceUrl(), Intrinsics.stringPlus("⇒开始访目录页:", var18));
            var18 = (java.lang.String)this.tocDebug(webBook, var12, `$completion`);
            return if (var18 === IntrinsicsKt.getCOROUTINE_SUSPENDED()) var18 else Unit.INSTANCE;
         }
      } else if (StringsKt.startsWith$default(key, "--", false, 2, null)) {
         if (key == null) {
            throw new NullPointerException("null cannot be cast to non-null type java.lang.String");
         } else {
            var var16: java.lang.String = key.substring(2);
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
            book.setOrigin(bookSource.getBookSourceUrl());
            this.log(bookSource.getBookSourceUrl(), Intrinsics.stringPlus("⇒开始访正文页:", var16));
            val var13: BookChapter = new BookChapter(null, null, false, null, null, 0, null, null, null, null, null, null, null, 8191, null);
            var13.setTitle("调试");
            var13.setUrl(var16);
            var16 = (java.lang.String)this.contentDebug(webBook, book, var13, null, `$completion`);
            return if (var16 === IntrinsicsKt.getCOROUTINE_SUSPENDED()) var16 else Unit.INSTANCE;
         }
      } else {
         this.log(bookSource.getBookSourceUrl(), Intrinsics.stringPlus("⇒开始搜索关键字:", key));
         val var10000: Any = this.searchDebug(webBook, key, `$completion`);
         return if (var10000 === IntrinsicsKt.getCOROUTINE_SUSPENDED()) var10000 else Unit.INSTANCE;
      }
   }

   private suspend fun exploreDebug(webBook: WebBook, url: String) {
      var `$continuation`: Continuation;
      label74: {
         if (`$completion` is <unrepresentable>) {
            `$continuation` = `$completion` as <unrepresentable>;
            if (((`$completion` as <unrepresentable>).label and Integer.MIN_VALUE) != 0) {
               `$continuation`.label -= Integer.MIN_VALUE;
               break label74;
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
               return Debugger.access$exploreDebug(this.this$0, null, null, this);
            }
         };
      }

      var var4: Any;
      label78: {
         var var6: Any;
         var var18: Any;
         label64: {
            val `$result`: Any = `$continuation`.result;
            var18 = IntrinsicsKt.getCOROUTINE_SUSPENDED();
            var var10000: Any;
            switch ($continuation.label) {
               case 0:
                  ResultKt.throwOnFailure(`$result`);
                  webBook.setDebugLogger(this);
                  this.log("︾开始解析发现页");
                  var4 = this;

                  try {
                     var6 = Result.Companion;
                     val exploreBooks: Debugger = var4 as Debugger;
                     val var10002: Int = Boxing.boxInt(1);
                     `$continuation`.L$0 = this;
                     `$continuation`.L$1 = webBook;
                     `$continuation`.label = 1;
                     var10000 = webBook.exploreBook(url, var10002, `$continuation`);
                  } catch (var20: java.lang.Throwable) {
                     val var34: Result.Companion = Result.Companion;
                     var6 = Result.constructor-impl(ResultKt.createFailure(var20));
                     break label64;
                  }

                  if (var10000 === var18) {
                     return var18;
                  }
                  break;
               case 1:
                  webBook = `$continuation`.L$1 as WebBook;
                  this = `$continuation`.L$0 as Debugger;

                  try {
                     ResultKt.throwOnFailure(`$result`);
                     var10000 = `$result`;
                     break;
                  } catch (var21: java.lang.Throwable) {
                     val var33: Result.Companion = Result.Companion;
                     var6 = Result.constructor-impl(ResultKt.createFailure(var21));
                     break label64;
                  }
               case 2:
                  var4 = `$continuation`.L$2;
                  webBook = `$continuation`.L$1 as WebBook;
                  this = `$continuation`.L$0 as Debugger;
                  ResultKt.throwOnFailure(`$result`);
                  break label78;
               default:
                  throw new IllegalStateException("call to 'resume' before 'invoke' with coroutine");
            }

            try {
               var6 = Result.constructor-impl(var10000 as java.util.List);
            } catch (var19: java.lang.Throwable) {
               val var36: Result.Companion = Result.Companion;
               var6 = Result.constructor-impl(ResultKt.createFailure(var19));
            }
         }

         var4 = var6;
         if (Result.isSuccess-impl(var6)) {
            val var30: java.util.List = var6 as java.util.List;
            if ((var6 as java.util.List).isEmpty()) {
               this.log(webBook.getSourceUrl(), "︽未获取到书籍");
            } else {
               this.log("┌发现结果列表");
               this.log(Intrinsics.stringPlus("└", GsonExtensionsKt.getGSON().toJson(var30)));
               this.log(webBook.getSourceUrl(), "︽发现页解析完成\n\n");
               val var45: Book = (var30.get(0) as SearchBook).toBook();
               `$continuation`.L$0 = this;
               `$continuation`.L$1 = webBook;
               `$continuation`.L$2 = var6;
               `$continuation`.label = 2;
               if (this.infoDebug(webBook, var45, `$continuation`) === var18) {
                  return var18;
               }
            }
         }
      }

      val var44: java.lang.Throwable = Result.exceptionOrNull-impl(var4);
      if (var44 != null) {
         this.log(webBook.getSourceUrl(), Intrinsics.stringPlus("Error: ", var44.getLocalizedMessage()));
         throw var44;
      } else {
         return Unit.INSTANCE;
      }
   }

   private suspend fun searchDebug(webBook: WebBook, key: String) {
      var `$continuation`: Continuation;
      label74: {
         if (`$completion` is <unrepresentable>) {
            `$continuation` = `$completion` as <unrepresentable>;
            if (((`$completion` as <unrepresentable>).label and Integer.MIN_VALUE) != 0) {
               `$continuation`.label -= Integer.MIN_VALUE;
               break label74;
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
               return Debugger.access$searchDebug(this.this$0, null, null, this);
            }
         };
      }

      var var4: Any;
      label78: {
         var var6: Any;
         var var18: Any;
         label64: {
            val `$result`: Any = `$continuation`.result;
            var18 = IntrinsicsKt.getCOROUTINE_SUSPENDED();
            var var10000: Any;
            switch ($continuation.label) {
               case 0:
                  ResultKt.throwOnFailure(`$result`);
                  webBook.setDebugLogger(this);
                  DefaultImpls.log$default(this, null, "︾开始解析搜索页", false, 5, null);
                  var4 = this;

                  try {
                     var6 = Result.Companion;
                     val searchBooks: Debugger = var4 as Debugger;
                     val var10002: Int = Boxing.boxInt(1);
                     `$continuation`.L$0 = this;
                     `$continuation`.L$1 = webBook;
                     `$continuation`.label = 1;
                     var10000 = webBook.searchBook(key, var10002, `$continuation`);
                  } catch (var20: java.lang.Throwable) {
                     val var34: Result.Companion = Result.Companion;
                     var6 = Result.constructor-impl(ResultKt.createFailure(var20));
                     break label64;
                  }

                  if (var10000 === var18) {
                     return var18;
                  }
                  break;
               case 1:
                  webBook = `$continuation`.L$1 as WebBook;
                  this = `$continuation`.L$0 as Debugger;

                  try {
                     ResultKt.throwOnFailure(`$result`);
                     var10000 = `$result`;
                     break;
                  } catch (var21: java.lang.Throwable) {
                     val var33: Result.Companion = Result.Companion;
                     var6 = Result.constructor-impl(ResultKt.createFailure(var21));
                     break label64;
                  }
               case 2:
                  var4 = `$continuation`.L$2;
                  webBook = `$continuation`.L$1 as WebBook;
                  this = `$continuation`.L$0 as Debugger;
                  ResultKt.throwOnFailure(`$result`);
                  break label78;
               default:
                  throw new IllegalStateException("call to 'resume' before 'invoke' with coroutine");
            }

            try {
               var6 = Result.constructor-impl(var10000 as java.util.List);
            } catch (var19: java.lang.Throwable) {
               val var36: Result.Companion = Result.Companion;
               var6 = Result.constructor-impl(ResultKt.createFailure(var19));
            }
         }

         var4 = var6;
         if (Result.isSuccess-impl(var6)) {
            val var30: java.util.List = var6 as java.util.List;
            if ((var6 as java.util.List).isEmpty()) {
               this.log(webBook.getSourceUrl(), "︽未获取到书籍");
            } else {
               this.log("┌搜索结果列表");
               this.log(Intrinsics.stringPlus("└", GsonExtensionsKt.getGSON().toJson(var30)));
               this.log(webBook.getSourceUrl(), "︽搜索页解析完成\n\n");
               val var45: Book = (var30.get(0) as SearchBook).toBook();
               `$continuation`.L$0 = this;
               `$continuation`.L$1 = webBook;
               `$continuation`.L$2 = var6;
               `$continuation`.label = 2;
               if (this.infoDebug(webBook, var45, `$continuation`) === var18) {
                  return var18;
               }
            }
         }
      }

      val var44: java.lang.Throwable = Result.exceptionOrNull-impl(var4);
      if (var44 != null) {
         this.log(webBook.getSourceUrl(), Intrinsics.stringPlus("Error: ", var44.getLocalizedMessage()));
         throw var44;
      } else {
         return Unit.INSTANCE;
      }
   }

   private suspend fun infoDebug(webBook: WebBook, book: Book) {
      var `$continuation`: Continuation;
      label62: {
         if (`$completion` is <unrepresentable>) {
            `$continuation` = `$completion` as <unrepresentable>;
            if (((`$completion` as <unrepresentable>).label and Integer.MIN_VALUE) != 0) {
               `$continuation`.label -= Integer.MIN_VALUE;
               break label62;
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
               return Debugger.access$infoDebug(this.this$0, null, null, this);
            }
         };
      }

      var var4: Any;
      label66: {
         var var6: Any;
         var var15: Any;
         label53: {
            val `$result`: Any = `$continuation`.result;
            var15 = IntrinsicsKt.getCOROUTINE_SUSPENDED();
            var var10000: Any;
            switch ($continuation.label) {
               case 0:
                  ResultKt.throwOnFailure(`$result`);
                  webBook.setDebugLogger(this);
                  DefaultImpls.log$default(this, null, "︾开始解析详情页", false, 5, null);
                  var4 = this;

                  try {
                     var6 = Result.Companion;
                     val it: Debugger = var4 as Debugger;
                     `$continuation`.L$0 = this;
                     `$continuation`.L$1 = webBook;
                     `$continuation`.label = 1;
                     var10000 = WebBook.getBookInfo$default(webBook, book, false, `$continuation`, 2, null);
                  } catch (var17: java.lang.Throwable) {
                     val var31: Result.Companion = Result.Companion;
                     var6 = Result.constructor-impl(ResultKt.createFailure(var17));
                     break label53;
                  }

                  if (var10000 === var15) {
                     return var15;
                  }
                  break;
               case 1:
                  webBook = `$continuation`.L$1 as WebBook;
                  this = `$continuation`.L$0 as Debugger;

                  try {
                     ResultKt.throwOnFailure(`$result`);
                     var10000 = `$result`;
                     break;
                  } catch (var18: java.lang.Throwable) {
                     val var30: Result.Companion = Result.Companion;
                     var6 = Result.constructor-impl(ResultKt.createFailure(var18));
                     break label53;
                  }
               case 2:
                  var4 = `$continuation`.L$2;
                  webBook = `$continuation`.L$1 as WebBook;
                  this = `$continuation`.L$0 as Debugger;
                  ResultKt.throwOnFailure(`$result`);
                  break label66;
               default:
                  throw new IllegalStateException("call to 'resume' before 'invoke' with coroutine");
            }

            try {
               var6 = Result.constructor-impl(var10000 as Book);
            } catch (var16: java.lang.Throwable) {
               val var33: Result.Companion = Result.Companion;
               var6 = Result.constructor-impl(ResultKt.createFailure(var16));
            }
         }

         var4 = var6;
         if (Result.isSuccess-impl(var6)) {
            val var27: Book = var6 as Book;
            this.log("┌书籍详情");
            this.log(Intrinsics.stringPlus("└", GsonExtensionsKt.getGSON().toJson(var27)));
            this.log(webBook.getSourceUrl(), "︽详情页解析完成\n\n");
            `$continuation`.L$0 = this;
            `$continuation`.L$1 = webBook;
            `$continuation`.L$2 = var6;
            `$continuation`.label = 2;
            if (this.tocDebug(webBook, var27, `$continuation`) === var15) {
               return var15;
            }
         }
      }

      val var39: java.lang.Throwable = Result.exceptionOrNull-impl(var4);
      if (var39 != null) {
         this.log(webBook.getSourceUrl(), Intrinsics.stringPlus("Error: ", var39.getLocalizedMessage()));
         throw var39;
      } else {
         return Unit.INSTANCE;
      }
   }

   private suspend fun tocDebug(webBook: WebBook, book: Book) {
      var `$continuation`: Continuation;
      label86: {
         if (`$completion` is <unrepresentable>) {
            `$continuation` = `$completion` as <unrepresentable>;
            if (((`$completion` as <unrepresentable>).label and Integer.MIN_VALUE) != 0) {
               `$continuation`.label -= Integer.MIN_VALUE;
               break label86;
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
               return Debugger.access$tocDebug(this.this$0, null, null, this);
            }
         };
      }

      var var4: Any;
      label90: {
         var var6: Any;
         var var19: Any;
         label75: {
            val `$result`: Any = `$continuation`.result;
            var19 = IntrinsicsKt.getCOROUTINE_SUSPENDED();
            var var10000: Any;
            switch ($continuation.label) {
               case 0:
                  ResultKt.throwOnFailure(`$result`);
                  webBook.setDebugLogger(this);
                  DefaultImpls.log$default(this, null, "︾开始解析目录页", false, 5, null);
                  var4 = this;

                  try {
                     var6 = Result.Companion;
                     val chapterList: Debugger = var4 as Debugger;
                     `$continuation`.L$0 = this;
                     `$continuation`.L$1 = webBook;
                     `$continuation`.L$2 = book;
                     `$continuation`.label = 1;
                     var10000 = webBook.getChapterList(book, `$continuation`);
                  } catch (var21: java.lang.Throwable) {
                     val var35: Result.Companion = Result.Companion;
                     var6 = Result.constructor-impl(ResultKt.createFailure(var21));
                     break label75;
                  }

                  if (var10000 === var19) {
                     return var19;
                  }
                  break;
               case 1:
                  book = `$continuation`.L$2 as Book;
                  webBook = `$continuation`.L$1 as WebBook;
                  this = `$continuation`.L$0 as Debugger;

                  try {
                     ResultKt.throwOnFailure(`$result`);
                     var10000 = `$result`;
                     break;
                  } catch (var22: java.lang.Throwable) {
                     val var34: Result.Companion = Result.Companion;
                     var6 = Result.constructor-impl(ResultKt.createFailure(var22));
                     break label75;
                  }
               case 2:
                  var4 = `$continuation`.L$2;
                  webBook = `$continuation`.L$1 as WebBook;
                  this = `$continuation`.L$0 as Debugger;
                  ResultKt.throwOnFailure(`$result`);
                  break label90;
               default:
                  throw new IllegalStateException("call to 'resume' before 'invoke' with coroutine");
            }

            try {
               var6 = Result.constructor-impl(var10000 as java.util.List);
            } catch (var20: java.lang.Throwable) {
               val var37: Result.Companion = Result.Companion;
               var6 = Result.constructor-impl(ResultKt.createFailure(var20));
            }
         }

         var4 = var6;
         if (Result.isSuccess-impl(var6)) {
            val var31: java.util.List = var6 as java.util.List;
            if (var6 as java.util.List != null) {
               if (var31.isEmpty()) {
                  this.log(webBook.getSourceUrl(), "︽目录列表为空");
               } else {
                  this.log("┌目录列表");
                  this.log(Intrinsics.stringPlus("└", GsonExtensionsKt.getGSON().toJson(var31)));
                  this.log(webBook.getSourceUrl(), "︽目录页解析完成\n\n");
                  val var45: java.lang.String = if (var31.size() > 1) (var31.get(1) as BookChapter).getUrl() else null;
                  val var10003: BookChapter = var31.get(0) as BookChapter;
                  `$continuation`.L$0 = this;
                  `$continuation`.L$1 = webBook;
                  `$continuation`.L$2 = var6;
                  `$continuation`.label = 2;
                  if (this.contentDebug(webBook, book, var10003, var45, `$continuation`) === var19) {
                     return var19;
                  }
               }
            }
         }
      }

      val var46: java.lang.Throwable = Result.exceptionOrNull-impl(var4);
      if (var46 != null) {
         this.log(webBook.getSourceUrl(), Intrinsics.stringPlus("Error: ", var46.getLocalizedMessage()));
         throw var46;
      } else {
         return Unit.INSTANCE;
      }
   }

   private suspend fun contentDebug(webBook: WebBook, book: Book, bookChapter: BookChapter, nextChapterUrl: String?) {
      var `$continuation`: Continuation;
      label52: {
         if (`$completion` is <unrepresentable>) {
            `$continuation` = `$completion` as <unrepresentable>;
            if (((`$completion` as <unrepresentable>).label and Integer.MIN_VALUE) != 0) {
               `$continuation`.label -= Integer.MIN_VALUE;
               break label52;
            }
         }

         `$continuation` = new ContinuationImpl(this, `$completion`) {
            Object L$0;
            Object L$1;
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
               return Debugger.access$contentDebug(this.this$0, null, null, null, null, this);
            }
         };
      }

      var var8: Any;
      label46: {
         val `$result`: Any = `$continuation`.result;
         val var17: Any = IntrinsicsKt.getCOROUTINE_SUSPENDED();
         var var10000: Any;
         switch ($continuation.label) {
            case 0:
               ResultKt.throwOnFailure(`$result`);
               webBook.setDebugLogger(this);
               this.log(webBook.getSourceUrl(), "︾开始解析正文页");
               val var6: Debugger = this;

               try {
                  var8 = Result.Companion;
                  val it: Debugger = var6;
                  `$continuation`.L$0 = this;
                  `$continuation`.L$1 = webBook;
                  `$continuation`.label = 1;
                  var10000 = webBook.getBookContent(book, bookChapter, nextChapterUrl, `$continuation`);
               } catch (var19: java.lang.Throwable) {
                  val var32: Result.Companion = Result.Companion;
                  var8 = Result.constructor-impl(ResultKt.createFailure(var19));
                  break label46;
               }

               if (var10000 === var17) {
                  return var17;
               }
               break;
            case 1:
               webBook = `$continuation`.L$1 as WebBook;
               this = `$continuation`.L$0 as Debugger;

               try {
                  ResultKt.throwOnFailure(`$result`);
                  var10000 = `$result`;
                  break;
               } catch (var20: java.lang.Throwable) {
                  val var31: Result.Companion = Result.Companion;
                  var8 = Result.constructor-impl(ResultKt.createFailure(var20));
                  break label46;
               }
            default:
               throw new IllegalStateException("call to 'resume' before 'invoke' with coroutine");
         }

         try {
            var8 = Result.constructor-impl(var10000 as java.lang.String);
         } catch (var18: java.lang.Throwable) {
            val var34: Result.Companion = Result.Companion;
            var8 = Result.constructor-impl(ResultKt.createFailure(var18));
         }
      }

      if (Result.isSuccess-impl(var8)) {
         val var29: java.lang.String = var8 as java.lang.String;
         this.log("┌正文内容");
         this.log(Intrinsics.stringPlus("└", var29));
         this.log(webBook.getSourceUrl(), "︽正文页解析完成");
      }

      val var40: java.lang.Throwable = Result.exceptionOrNull-impl(var8);
      if (var40 != null) {
         this.log(webBook.getSourceUrl(), Intrinsics.stringPlus("Error: ", var40.getLocalizedMessage()));
      }

      return Unit.INSTANCE;
   }
}
