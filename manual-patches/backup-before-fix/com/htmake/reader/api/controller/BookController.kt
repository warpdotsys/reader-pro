package com.htmake.reader.api.controller

import com.fasterxml.jackson.core.JsonFactory
import com.fasterxml.jackson.core.JsonParser
import com.fasterxml.jackson.core.JsonToken
import com.fasterxml.jackson.core.TreeNode
import com.fasterxml.jackson.databind.JsonNode
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.databind.node.ObjectNode
import com.google.gson.Gson
import com.htmake.reader.api.ReturnData
import com.htmake.reader.entity.User
import com.htmake.reader.lib.tts.constant.TtsStyleEnum
import com.htmake.reader.lib.tts.constant.VoiceEnum
import com.htmake.reader.lib.tts.model.SSML
import com.htmake.reader.lib.tts.service.TTSService
import com.htmake.reader.utils.ExtKt
import com.htmake.reader.utils.MongoManager
import com.htmake.reader.utils.SpringContextUtils
import com.htmake.reader.utils.VertExtKt
import io.legado.app.constant.AppPattern
import io.legado.app.data.entities.Book
import io.legado.app.data.entities.BookChapter
import io.legado.app.data.entities.BookSource
import io.legado.app.data.entities.HttpTTS
import io.legado.app.data.entities.SearchBook
import io.legado.app.data.entities.SearchResult
import io.legado.app.data.entities.TxtTocRule
import io.legado.app.exception.TocEmptyException
import io.legado.app.help.BookHelp
import io.legado.app.help.DefaultData
import io.legado.app.model.Debugger
import io.legado.app.model.localBook.LocalBook
import io.legado.app.model.webBook.WebBook
import io.legado.app.utils.ACache
import io.legado.app.utils.FileUtils
import io.legado.app.utils.GsonExtensionsKt
import io.legado.app.utils.HtmlFormatter
import io.legado.app.utils.MD5Utils
import io.legado.app.utils.NetworkUtils
import io.legado.app.utils.ParameterizedTypeImpl
import io.legado.app.utils.ZipUtils
import io.vertx.core.AsyncResult
import io.vertx.core.Handler
import io.vertx.core.buffer.Buffer
import io.vertx.core.http.CaseInsensitiveHeaders
import io.vertx.core.http.HttpMethod
import io.vertx.core.http.HttpServerResponse
import io.vertx.core.json.JsonArray
import io.vertx.core.json.JsonObject
import io.vertx.ext.web.FileUpload
import io.vertx.ext.web.RoutingContext
import io.vertx.ext.web.client.HttpResponse
import io.vertx.ext.web.client.WebClient
import io.vertx.kotlin.coroutines.VertxCoroutineKt
import java.awt.Dimension
import java.awt.Graphics2D
import java.awt.Image
import java.awt.image.BufferedImage
import java.io.Closeable
import java.io.File
import java.io.FileOutputStream
import java.io.InputStream
import java.net.URL
import java.net.URLEncoder
import java.nio.charset.Charset
import java.nio.file.Paths
import java.text.SimpleDateFormat
import java.util.ArrayList
import java.util.Arrays
import java.util.Base64
import java.util.LinkedHashMap
import java.util.LinkedHashSet
import java.util.Locale
import java.util.Map.Entry
import java.util.function.BiConsumer
import java.util.regex.Matcher
import javax.imageio.ImageIO
import kotlin.Result.Companion
import kotlin.coroutines.Continuation
import kotlin.coroutines.CoroutineContext
import kotlin.coroutines.intrinsics.IntrinsicsKt
import kotlin.coroutines.jvm.internal.Boxing
import kotlin.coroutines.jvm.internal.ContinuationImpl
import kotlin.jvm.functions.Function1
import kotlin.jvm.functions.Function2
import kotlin.jvm.functions.Function3
import kotlin.jvm.internal.Intrinsics
import kotlin.jvm.internal.TypeIntrinsics
import kotlin.jvm.internal.Ref.BooleanRef
import kotlin.jvm.internal.Ref.FloatRef
import kotlin.jvm.internal.Ref.IntRef
import kotlin.jvm.internal.Ref.LongRef
import kotlin.jvm.internal.Ref.ObjectRef
import kotlinx.coroutines.BuildersKt
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.JobKt
import kotlinx.coroutines.slf4j.MDCContext
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.MutexKt
import me.ag2s.epublib.domain.Author
import me.ag2s.epublib.domain.Date
import me.ag2s.epublib.domain.EpubBook
import me.ag2s.epublib.domain.FileResourceProvider
import me.ag2s.epublib.domain.LazyResource
import me.ag2s.epublib.domain.Metadata
import me.ag2s.epublib.domain.Resource
import me.ag2s.epublib.domain.Resources
import me.ag2s.epublib.epub.EpubWriter
import me.ag2s.epublib.util.ResourceUtil
import org.apache.pdfbox.pdmodel.PDDocument
import org.apache.pdfbox.pdmodel.common.PDRectangle
import org.apache.pdfbox.rendering.ImageType
import org.apache.pdfbox.rendering.PDFRenderer
import org.jetbrains.annotations.NotNull
import org.jetbrains.annotations.Nullable

public class BookController(coroutineContext: CoroutineContext) : BaseController(coroutineContext) {
   private final val backupFileNames: Array<String> by LazyKt.lazy(<unrepresentable>.INSTANCE)
      private final get() {
         return this.backupFileNames$delegate.getValue() as Array<java.lang.String>;
      }


   private final var bookInfoCache: ACache = ACache.Companion.get("bookInfoCache", 2000000L, 10000)
   private final val concurrentLoopCount: Int = 8
   private final var webClient: WebClient

   init {
      val var2: Any = SpringContextUtils.getBean("webClient", WebClient.class);
      this.webClient = var2 as WebClient;
   }

   private fun getInvalidBookSourceCache(userNameSpace: String): ACache {
      return ACache.Companion.get(new File(ExtKt.getWorkDir("storage", "cache", "invalidBookSourceCache", userNameSpace)), 5000000L, 1000000);
   }

   private fun isInvalidBookSource(bookSource: BookSource, userNameSpace: String): Boolean {
      return this.getInvalidBookSourceCache(userNameSpace).getAsString(bookSource.getBookSourceUrl()) != null;
   }

   private fun addInvalidBookSource(sourceUrl: String, invalidInfo: Map<String, Any>, userNameSpace: String) {
      this.getInvalidBookSourceCache(userNameSpace).put(sourceUrl, ExtKt.jsonEncode$default(invalidInfo, false, 2, null), 600);
   }

   private fun getBookChaptersCache(userNameSpace: String): ACache {
      return ACache.Companion.get(new File(ExtKt.getWorkDir("storage", "cache", "bookChaptersCache", userNameSpace)), 5000000L, 1000000);
   }

   public suspend fun getInvalidBookSources(context: RoutingContext): ReturnData {
      var `$continuation`: Continuation;
      label38: {
         if (`$completion` is <unrepresentable>) {
            `$continuation` = `$completion` as <unrepresentable>;
            if (((`$completion` as <unrepresentable>).label and Integer.MIN_VALUE) != 0) {
               `$continuation`.label -= Integer.MIN_VALUE;
               break label38;
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
               return this.this$0.getInvalidBookSources(null, this);
            }
         };
      }

      val `$result`: Any = `$continuation`.result;
      val var21: Any = IntrinsicsKt.getCOROUTINE_SUSPENDED();
      var returnData: ReturnData;
      var var10000: Any;
      switch ($continuation.label) {
         case 0:
            ResultKt.throwOnFailure(`$result`);
            returnData = new ReturnData();
            `$continuation`.L$0 = this;
            `$continuation`.L$1 = context;
            `$continuation`.L$2 = returnData;
            `$continuation`.label = 1;
            var10000 = this.checkAuth(context, `$continuation`);
            if (var10000 === var21) {
               return var21;
            }
            break;
         case 1:
            returnData = `$continuation`.L$2 as ReturnData;
            context = `$continuation`.L$1 as RoutingContext;
            this = `$continuation`.L$0 as BookController;
            ResultKt.throwOnFailure(`$result`);
            var10000 = `$result`;
            break;
         default:
            throw new IllegalStateException("call to 'resume' before 'invoke' with coroutine");
      }

      if (!var10000 as java.lang.Boolean) {
         return ReturnData.setData$default(returnData, "NEED_LOGIN", null, 2, null).setErrorMsg("请登录后使用");
      } else {
         val userNameSpace: java.lang.String = this.getUserNameSpace(context);
         val invalidBookSourceCache: ACache = this.getInvalidBookSourceCache(userNameSpace);
         val var22: Array<File> = new File(ExtKt.getWorkDir("storage", "cache", "invalidBookSourceCache", userNameSpace)).listFiles();
         val invalidBookSourceList: ArrayList = new ArrayList();
         if (var22 != null) {
            val var23: Array<File> = var22;
            var var10: Int = 0;
            val var11: Int = var22.length;

            while (var10 < var11) {
               val f: File = var23[var10];
               var10++;
               val var14: java.lang.String = f.getName();
               val var13: java.lang.String = invalidBookSourceCache.getByHashCode(var14);
               if (var13 != null) {
                  Boxing.boxBoolean(invalidBookSourceList.add(ExtKt.toMap(var13)));
               }
            }
         }

         return ReturnData.setData$default(returnData, invalidBookSourceList, null, 2, null);
      }
   }

   public suspend fun getBookInfo(context: RoutingContext): ReturnData {
      var `$continuation`: Continuation;
      label135: {
         if (`$completion` is <unrepresentable>) {
            `$continuation` = `$completion` as <unrepresentable>;
            if (((`$completion` as <unrepresentable>).label and Integer.MIN_VALUE) != 0) {
               `$continuation`.label -= Integer.MIN_VALUE;
               break label135;
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
               return this.this$0.getBookInfo(null, this);
            }
         };
      }

      var var20: Any;
      var returnData: ReturnData;
      var bookInfo: Book;
      label154: {
         var var10000: Book;
         label155: {
            var var16: BookController;
            label146: {
               var bookUrl: java.lang.String;
               var userNameSpace: java.lang.String;
               var var28: java.lang.String;
               label122: {
                  label121: {
                     label139: {
                        val `$result`: Any = `$continuation`.result;
                        var20 = IntrinsicsKt.getCOROUTINE_SUSPENDED();
                        switch ($continuation.label) {
                           case 0:
                              ResultKt.throwOnFailure(`$result`);
                              returnData = new ReturnData();
                              if (context.request().method() === HttpMethod.POST) {
                                 val var25: java.lang.String = context.getBodyAsJson().getString("url");
                                 userNameSpace = if (var25 == null) context.getBodyAsJson().getJsonObject("searchBook").getString("bookUrl") else var25;
                                 bookUrl = if (userNameSpace == null) "" else userNameSpace;
                              } else {
                                 val var26: java.util.List = context.queryParam("url");
                                 userNameSpace = CollectionsKt.firstOrNull(var26);
                                 bookUrl = if (userNameSpace == null) "" else userNameSpace;
                              }

                              if (bookUrl.length() == 0) {
                                 return returnData.setErrorMsg("请输入书籍链接");
                              }

                              userNameSpace = this.getUserNameSpace(context);
                              BookControllerKt.access$getLogger$p().info("getBookInfo with bookUrl: {}", bookUrl);
                              bookInfo = null;
                              `$continuation`.L$0 = this;
                              `$continuation`.L$1 = context;
                              `$continuation`.L$2 = returnData;
                              `$continuation`.L$3 = bookUrl;
                              `$continuation`.L$4 = userNameSpace;
                              `$continuation`.label = 1;
                              var10000 = this.checkAuth(context, `$continuation`);
                              if (var10000 === var20) {
                                 return var20;
                              }
                              break;
                           case 1:
                              bookInfo = null;
                              userNameSpace = `$continuation`.L$4 as java.lang.String;
                              bookUrl = `$continuation`.L$3 as java.lang.String;
                              returnData = `$continuation`.L$2 as ReturnData;
                              context = `$continuation`.L$1 as RoutingContext;
                              this = `$continuation`.L$0 as BookController;
                              ResultKt.throwOnFailure(`$result`);
                              var10000 = `$result`;
                              break;
                           case 2:
                              userNameSpace = `$continuation`.L$3 as java.lang.String;
                              bookUrl = `$continuation`.L$2 as java.lang.String;
                              returnData = `$continuation`.L$1 as ReturnData;
                              this = `$continuation`.L$0 as BookController;
                              ResultKt.throwOnFailure(`$result`);
                              var10000 = `$result`;
                              break label121;
                           case 3:
                              userNameSpace = `$continuation`.L$3 as java.lang.String;
                              bookUrl = `$continuation`.L$2 as java.lang.String;
                              returnData = `$continuation`.L$1 as ReturnData;
                              this = `$continuation`.L$0 as BookController;
                              ResultKt.throwOnFailure(`$result`);
                              var10000 = `$result`;
                              break label139;
                           case 4:
                              var16 = `$continuation`.L$2 as BookController;
                              returnData = `$continuation`.L$1 as ReturnData;
                              this = `$continuation`.L$0 as BookController;
                              ResultKt.throwOnFailure(`$result`);
                              var10000 = `$result`;
                              break label146;
                           case 5:
                              returnData = `$continuation`.L$1 as ReturnData;
                              this = `$continuation`.L$0 as BookController;
                              ResultKt.throwOnFailure(`$result`);
                              var10000 = (Book)`$result`;
                              break label155;
                           case 6:
                              bookInfo = `$continuation`.L$1 as Book;
                              returnData = `$continuation`.L$0 as ReturnData;
                              ResultKt.throwOnFailure(`$result`);
                              return ReturnData.setData$default(returnData, bookInfo, null, 2, null);
                           default:
                              throw new IllegalStateException("call to 'resume' before 'invoke' with coroutine");
                        }

                        if (var10000 as java.lang.Boolean) {
                           bookInfo = this.getShelfBookByURL(bookUrl, userNameSpace);
                        }

                        if (bookInfo != null) {
                           break label154;
                        }

                        val var9: java.lang.String = this.bookInfoCache.getAsString(bookUrl);
                        if (var9 == null) {
                           var10000 = null;
                        } else {
                           val var10: java.util.Map = ExtKt.toMap(var9);
                           var10000 = if (var10 == null)
                              null
                              else
                              ExtKt.getGson()
                                 .fromJson(
                                    if (var10 is java.lang.String) var10 as java.lang.String else ExtKt.getGson().toJson(var10),
                                    new BookController$getBookInfo$$inlined$toDataClass$1().getType()
                                 );
                        }

                        if (var10000 != null) {
                           val var10002: java.lang.String = var10000.getOrigin();
                           `$continuation`.L$0 = this;
                           `$continuation`.L$1 = returnData;
                           `$continuation`.L$2 = bookUrl;
                           `$continuation`.L$3 = userNameSpace;
                           `$continuation`.L$4 = null;
                           `$continuation`.label = 2;
                           var10000 = getBookSourceString$default(this, context, var10002, false, `$continuation`, 4, null);
                           if (var10000 === var20) {
                              return var20;
                           }
                           break label121;
                        }

                        `$continuation`.L$0 = this;
                        `$continuation`.L$1 = returnData;
                        `$continuation`.L$2 = bookUrl;
                        `$continuation`.L$3 = userNameSpace;
                        `$continuation`.L$4 = null;
                        `$continuation`.label = 3;
                        var10000 = getBookSourceString$default(this, context, null, false, `$continuation`, 6, null);
                        if (var10000 === var20) {
                           return var20;
                        }
                     }

                     var28 = var10000 as java.lang.String;
                     break label122;
                  }

                  var28 = var10000 as java.lang.String;
               }

               if (var28 == null || var28.length() == 0) {
                  return returnData.setErrorMsg("未配置书源");
               }

               var16 = this;
               val var37: WebBook = new WebBook(var28, this.getAppConfig().getDebugLog(), null, userNameSpace, 4, null);
               `$continuation`.L$0 = this;
               `$continuation`.L$1 = returnData;
               `$continuation`.L$2 = this;
               `$continuation`.L$3 = null;
               `$continuation`.label = 4;
               var10000 = WebBook.getBookInfo$default(var37, bookUrl, false, `$continuation`, 2, null);
               if (var10000 === var20) {
                  return var20;
               }
            }

            val var10001: Book = var10000 as Book;
            `$continuation`.L$0 = this;
            `$continuation`.L$1 = returnData;
            `$continuation`.L$2 = null;
            `$continuation`.label = 5;
            var10000 = (Book)var16.mergeBookCacheInfo(var10001, `$continuation`);
            if (var10000 === var20) {
               return var20;
            }
         }

         bookInfo = var10000;
      }

      val var38: java.util.List = CollectionsKt.arrayListOf(new Book[]{bookInfo});
      `$continuation`.L$0 = returnData;
      `$continuation`.L$1 = bookInfo;
      `$continuation`.L$2 = null;
      `$continuation`.L$3 = null;
      `$continuation`.L$4 = null;
      `$continuation`.label = 6;
      return if (this.saveBookInfoCache(var38, `$continuation`) === var20) var20 else ReturnData.setData$default(returnData, bookInfo, null, 2, null);
   }

   public suspend fun getBookCover(context: RoutingContext) {
      val md5Encode: java.util.List = context.queryParam("path");
      val ext: java.lang.String = CollectionsKt.firstOrNull(md5Encode);
      val coverUrl: java.lang.String = if (ext == null) "" else ext;
      if ((if (ext == null) "" else ext).length() == 0) {
         context.response().setStatusCode(404).end();
         return Unit.INSTANCE;
      } else {
         val var15: File = new File(
            ExtKt.getWorkDir("storage", "cache", "bookCoverCache", "${MD5Utils.INSTANCE.md5Encode(coverUrl)}.${this.getFileExt(coverUrl, "png")}")
         );
         if (var15.exists()) {
            BookControllerKt.access$getLogger$p().info("send cache: {}", var15);
            val var16: HttpServerResponse = context.response().putHeader("Cache-Control", "86400").sendFile(var15.toString());
            return if (var16 === IntrinsicsKt.getCOROUTINE_SUSPENDED()) var16 else Unit.INSTANCE;
         } else {
            if (!var15.getParentFile().exists()) {
               var15.getParentFile().mkdirs();
            }

            val var10000: Job = BuildersKt.launch$default(
               this,
               new MDCContext(null, 1, null)
                  .plus(Dispatchers.getIO())
                  .plus(new BookController$getBookCover$$inlined$CoroutineExceptionHandler$1(CoroutineExceptionHandler.Key, context)),
               null,
               (new Function2<CoroutineScope, Continuation<? super Unit>, Object>(context, var15, this, coverUrl, null) {
                  int label;

                  {
                     super(2, `$completionx`);
                     this.$context = `$context`;
                     this.$cacheFile = `$cacheFile`;
                     this.this$0 = `$receiver`;
                     this.$coverUrl = `$coverUrl`;
                  }

                  @Nullable
                  @Override
                  public final Object invokeSuspend(@NotNull Object $result) {
                     val var5: Any = IntrinsicsKt.getCOROUTINE_SUSPENDED();
                     var var10000: Any;
                     switch (this.label) {
                        case 0:
                           ResultKt.throwOnFailure(`$result`);
                           var10000 = (new Function1<Handler<AsyncResult<HttpResponse<Buffer>>>, Unit>(this.this$0, this.$coverUrl) {
                              {
                                 super(1);
                                 this.this$0 = `$receiver`;
                                 this.$coverUrl = `$coverUrl`;
                              }

                              public final void invoke(@NotNull Handler<AsyncResult<HttpResponse<Buffer>>> handler) {
                                 BookController.access$getWebClient$p(this.this$0).getAbs(this.$coverUrl).timeout(3000L).send(handler);
                              }
                           }) as Function1;
                           val var10001: Continuation = this;
                           this.label = 1;
                           var10000 = VertxCoroutineKt.awaitResult((Function1)var10000, var10001);
                           if (var10000 === var5) {
                              return var5;
                           }
                           break;
                        case 1:
                           ResultKt.throwOnFailure(`$result`);
                           var10000 = `$result`;
                           break;
                        default:
                           throw new IllegalStateException("call to 'resume' before 'invoke' with coroutine");
                     }

                     val res: Buffer = (var10000 as HttpResponse).bodyAsBuffer();
                     val bodyBytes: ByteArray = if (res == null) null else res.getBytes();
                     if (bodyBytes != null) {
                        val var6: HttpServerResponse = this.$context.response().putHeader("Cache-Control", "86400");
                        FilesKt.writeBytes(this.$cacheFile, bodyBytes);
                        var6.sendFile(this.$cacheFile.toString());
                     } else {
                        this.$context.response().setStatusCode(404).end();
                     }

                     return Unit.INSTANCE;
                  }

                  @NotNull
                  @Override
                  public final Continuation<Unit> create(@Nullable Object value, @NotNull Continuation<?> $completion) {
                     return new <anonymous constructor>(this.$context, this.$cacheFile, this.this$0, this.$coverUrl, `$completion`);
                  }

                  @Nullable
                  public final Object invoke(@NotNull CoroutineScope p1, @Nullable Continuation<? super Unit> p2) {
                     return (this.create(p1, p2) as <unrepresentable>).invokeSuspend(Unit.INSTANCE);
                  }
               }) as Function2,
               2,
               null
            );
            return if (var10000 === IntrinsicsKt.getCOROUTINE_SUSPENDED()) var10000 else Unit.INSTANCE;
         }
      }
   }

   public suspend fun importBookPreview(context: RoutingContext): ReturnData {
      var `$continuation`: Continuation;
      label84: {
         if (`$completion` is <unrepresentable>) {
            `$continuation` = `$completion` as <unrepresentable>;
            if (((`$completion` as <unrepresentable>).label and Integer.MIN_VALUE) != 0) {
               `$continuation`.label -= Integer.MIN_VALUE;
               break label84;
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
               return this.this$0.importBookPreview(null, this);
            }
         };
      }

      val `$result`: Any = `$continuation`.result;
      val var25: Any = IntrinsicsKt.getCOROUTINE_SUSPENDED();
      var returnData: ReturnData;
      var var10000: Any;
      switch ($continuation.label) {
         case 0:
            ResultKt.throwOnFailure(`$result`);
            returnData = new ReturnData();
            `$continuation`.L$0 = this;
            `$continuation`.L$1 = context;
            `$continuation`.L$2 = returnData;
            `$continuation`.label = 1;
            var10000 = (FileUtils)this.checkAuth(context, `$continuation`);
            if (var10000 === var25) {
               return var25;
            }
            break;
         case 1:
            returnData = `$continuation`.L$2 as ReturnData;
            context = `$continuation`.L$1 as RoutingContext;
            this = `$continuation`.L$0 as BookController;
            ResultKt.throwOnFailure(`$result`);
            var10000 = (FileUtils)`$result`;
            break;
         default:
            throw new IllegalStateException("call to 'resume' before 'invoke' with coroutine");
      }

      if (!var10000 as java.lang.Boolean) {
         return ReturnData.setData$default(returnData, "NEED_LOGIN", null, 2, null).setErrorMsg("请登录后使用");
      } else if (context.fileUploads() != null && !context.fileUploads().isEmpty()) {
         val userNameSpace: java.lang.String = this.getUserNameSpace(context);
         val fileList: ArrayList = new ArrayList();

         val var28: java.lang.Iterable;
         for (Object element$iv : var28) {
            val it: FileUpload = `element$iv` as FileUpload;
            val file: File = new File((`element$iv` as FileUpload).uploadedFileName());
            BookControllerKt.access$getLogger$p()
               .info("uploadFile: {} {} {}", new Object[]{(`element$iv` as FileUpload).uploadedFileName(), (`element$iv` as FileUpload).fileName(), file});
            if (file.exists()) {
               var var29: java.lang.String = it.fileName();
               var10000 = this;
               val ext: java.lang.String = BaseController.getFileExt$default((BaseController)var10000, var29, null, 2, null);
               if (!(ext == "txt") && !(ext == "epub") && !(ext == "umd") && !(ext == "cbz") && !(ext == "pdf")) {
                  ExtKt.deleteRecursively(file);
                  return returnData.setErrorMsg("不支持导入$ext格式的书籍文件");
               }

               var10000 = FileUtils.INSTANCE;
               var29 = var10000.getNameExcludeExtension(var29);
               var29 = AppPattern.INSTANCE.getFileNameRegex().replace(var29, "");
               val var45: StringBuilder = new StringBuilder();
               val var10001: java.lang.String = var29.substring(0, Math.min(50, var29.length()));
               var29 = var45.append(var10001).append('.').append(ext).toString();
               val var33: java.lang.String = Paths.get("storage", "assets", userNameSpace, "book", var29).toString();
               val var36: java.lang.String = "/assets/$userNameSpace/book/$var29";
               var var38: java.lang.String = var33;
               if (StringsKt.endsWith(var29, ".epub", true)) {
                  var38 = "$var33${File.separator}index.epub";
               }

               if (StringsKt.endsWith(var29, ".cbz", true)) {
                  var38 = "$var38${File.separator}index.cbz";
               }

               if (StringsKt.endsWith(var29, ".pdf", true)) {
                  var38 = "$var38${File.separator}index.pdf";
               }

               val var40: File = new File(ExtKt.getWorkDir(var38));
               if (!var40.getParentFile().exists()) {
                  var40.getParentFile().mkdirs();
               }

               if (var40.exists()) {
                  var40.delete();
               }

               BookControllerKt.access$getLogger$p().info("moveTo: {}", var40);
               if (FilesKt.copyRecursively$default(file, var40, false, null, 6, null)) {
                  val book: Book = Book.Companion.initLocalBook(var36, var33, ExtKt.getWorkDir$default(null, 1, null));
                  book.setUserNameSpace(userNameSpace);

                  try {
                     fileList.add(MapsKt.mapOf(new Pair[]{TuplesKt.to("book", book), TuplesKt.to("chapters", LocalBook.INSTANCE.getChapterList(book))}));
                  } catch (var26: TocEmptyException) {
                     fileList.add(MapsKt.mapOf(new Pair[]{TuplesKt.to("book", book), TuplesKt.to("chapters", new ArrayList())}));
                  }
               }

               ExtKt.deleteRecursively(file);
            }
         }

         return ReturnData.setData$default(returnData, fileList, null, 2, null);
      } else {
         return returnData.setErrorMsg("请上传书籍文件");
      }
   }

   public suspend fun getTxtTocRules(context: RoutingContext): ReturnData {
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
               return this.this$0.getTxtTocRules(null, this);
            }
         };
      }

      val `$result`: Any = `$continuation`.result;
      val var18: Any = IntrinsicsKt.getCOROUTINE_SUSPENDED();
      var returnData: ReturnData;
      var var10000: Any;
      switch ($continuation.label) {
         case 0:
            ResultKt.throwOnFailure(`$result`);
            returnData = new ReturnData();
            `$continuation`.L$0 = this;
            `$continuation`.L$1 = context;
            `$continuation`.L$2 = returnData;
            `$continuation`.label = 1;
            var10000 = this.checkAuth(context, `$continuation`);
            if (var10000 === var18) {
               return var18;
            }
            break;
         case 1:
            returnData = `$continuation`.L$2 as ReturnData;
            context = `$continuation`.L$1 as RoutingContext;
            this = `$continuation`.L$0 as BookController;
            ResultKt.throwOnFailure(`$result`);
            var10000 = `$result`;
            break;
         default:
            throw new IllegalStateException("call to 'resume' before 'invoke' with coroutine");
      }

      if (!var10000 as java.lang.Boolean) {
         return ReturnData.setData$default(returnData, "NEED_LOGIN", null, 2, null).setErrorMsg("请登录后使用");
      } else {
         val txtTocRules: java.lang.String = this.getUserStorage(this.getUserNameSpace(context), new java.lang.String[]{"txtTocRule"});
         val var20: java.util.List = new ArrayList();
         var20.addAll(DefaultData.INSTANCE.getTxtTocRules());
         if (txtTocRules != null) {
            val `$this$fromJsonArray$iv`: Gson = GsonExtensionsKt.getGSON();

            var var12: Any;
            try {
               var12 = Result.Companion;
               val var25: Any = `$this$fromJsonArray$iv`.fromJson(txtTocRules, new ParameterizedTypeImpl(TxtTocRule.class));
               var12 = Result.constructor-impl(var25 as? java.util.List);
            } catch (var19: java.lang.Throwable) {
               val var14: Companion = Result.Companion;
               var12 = Result.constructor-impl(ResultKt.createFailure(var19));
            }

            val var8: java.util.List = (if (Result.isFailure-impl(var12)) null else var12) as java.util.List;
            var20.addAll(if (var8 == null) CollectionsKt.emptyList() else var8);
         }

         return ReturnData.setData$default(returnData, var20, null, 2, null);
      }
   }

   public suspend fun getChapterListByRule(context: RoutingContext): ReturnData {
      var `$continuation`: Continuation;
      label41: {
         if (`$completion` is <unrepresentable>) {
            `$continuation` = `$completion` as <unrepresentable>;
            if (((`$completion` as <unrepresentable>).label and Integer.MIN_VALUE) != 0) {
               `$continuation`.label -= Integer.MIN_VALUE;
               break label41;
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
               return this.this$0.getChapterListByRule(null, this);
            }
         };
      }

      val `$result`: Any = `$continuation`.result;
      val var9: Any = IntrinsicsKt.getCOROUTINE_SUSPENDED();
      var returnData: ReturnData;
      var var10000: Any;
      switch ($continuation.label) {
         case 0:
            ResultKt.throwOnFailure(`$result`);
            returnData = new ReturnData();
            `$continuation`.L$0 = this;
            `$continuation`.L$1 = context;
            `$continuation`.L$2 = returnData;
            `$continuation`.label = 1;
            var10000 = this.checkAuth(context, `$continuation`);
            if (var10000 === var9) {
               return var9;
            }
            break;
         case 1:
            returnData = `$continuation`.L$2 as ReturnData;
            context = `$continuation`.L$1 as RoutingContext;
            this = `$continuation`.L$0 as BookController;
            ResultKt.throwOnFailure(`$result`);
            var10000 = `$result`;
            break;
         default:
            throw new IllegalStateException("call to 'resume' before 'invoke' with coroutine");
      }

      if (!var10000 as java.lang.Boolean) {
         return ReturnData.setData$default(returnData, "NEED_LOGIN", null, 2, null).setErrorMsg("请登录后使用");
      } else {
         val book: Book = context.getBodyAsJson().mapTo(Book.class);
         if (book.getOrigin().length() == 0) {
            return returnData.setErrorMsg("未找到书源信息");
         } else if (!book.isLocalTxt() && !book.isLocalEpub() && !book.isLocalPdf()) {
            return returnData.setErrorMsg("非本地txt/epub/pdf书籍");
         } else {
            book.setRootDir(ExtKt.getWorkDir$default(null, 1, null));
            book.setUserNameSpace(this.getUserNameSpace(context));
            var10000 = LocalBook.INSTANCE;
            return ReturnData.setData$default(
               returnData,
               MapsKt.mapOf(new Pair[]{TuplesKt.to("book", book), TuplesKt.to("chapters", ((LocalBook)var10000).getChapterList(book))}),
               null,
               2,
               null
            );
         }
      }
   }

   public suspend fun refreshLocalBook(context: RoutingContext): ReturnData {
      var `$continuation`: Continuation;
      label54: {
         if (`$completion` is <unrepresentable>) {
            `$continuation` = `$completion` as <unrepresentable>;
            if (((`$completion` as <unrepresentable>).label and Integer.MIN_VALUE) != 0) {
               `$continuation`.label -= Integer.MIN_VALUE;
               break label54;
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
               return this.this$0.refreshLocalBook(null, this);
            }
         };
      }

      val `$result`: Any = `$continuation`.result;
      val var9: Any = IntrinsicsKt.getCOROUTINE_SUSPENDED();
      var returnData: ReturnData;
      var var10000: Any;
      switch ($continuation.label) {
         case 0:
            ResultKt.throwOnFailure(`$result`);
            returnData = new ReturnData();
            `$continuation`.L$0 = this;
            `$continuation`.L$1 = context;
            `$continuation`.L$2 = returnData;
            `$continuation`.label = 1;
            var10000 = this.checkAuth(context, `$continuation`);
            if (var10000 === var9) {
               return var9;
            }
            break;
         case 1:
            returnData = `$continuation`.L$2 as ReturnData;
            context = `$continuation`.L$1 as RoutingContext;
            this = `$continuation`.L$0 as BookController;
            ResultKt.throwOnFailure(`$result`);
            var10000 = `$result`;
            break;
         case 2:
            val bookInfo: Book = `$continuation`.L$1 as Book;
            returnData = `$continuation`.L$0 as ReturnData;
            ResultKt.throwOnFailure(`$result`);
            return ReturnData.setData$default(returnData, bookInfo, null, 2, null);
         default:
            throw new IllegalStateException("call to 'resume' before 'invoke' with coroutine");
      }

      if (!var10000 as java.lang.Boolean) {
         return ReturnData.setData$default(returnData, "NEED_LOGIN", null, 2, null).setErrorMsg("请登录后使用");
      } else {
         val var10: java.lang.String;
         if (context.request().method() === HttpMethod.POST) {
            val userNameSpace: java.lang.String = context.getBodyAsJson().getString("bookUrl");
            var10 = userNameSpace;
         } else {
            val var14: java.util.List = context.queryParam("bookUrl");
            val var11: java.lang.String = CollectionsKt.firstOrNull(var14);
            var10 = if (var11 == null) "" else var11;
         }

         if (var10.length() == 0) {
            return returnData.setErrorMsg("请输入书籍链接");
         } else {
            val var13: java.lang.String = this.getUserNameSpace(context);
            val bookInfo: Book = this.getShelfBookByURL(var10, var13);
            if (bookInfo == null) {
               return returnData.setErrorMsg("书籍信息错误");
            } else {
               bookInfo.updateFromLocal(true);
               val var10003: Function1 = (new Function1<Book, Book>(bookInfo) {
                  {
                     super(1);
                     this.$bookInfo = `$bookInfo`;
                  }

                  @NotNull
                  public final Book invoke(@NotNull Book existBook) {
                     existBook.setCoverUrl(this.$bookInfo.getCoverUrl());
                     BookControllerKt.access$getLogger$p().info("refreshLocalBook: {}", existBook);
                     return existBook;
                  }
               }) as Function1;
               `$continuation`.L$0 = returnData;
               `$continuation`.L$1 = bookInfo;
               `$continuation`.L$2 = null;
               `$continuation`.label = 2;
               return if (this.editShelfBook(bookInfo, var13, var10003, `$continuation`) === var9)
                  var9
                  else
                  ReturnData.setData$default(returnData, bookInfo, null, 2, null);
            }
         }
      }
   }

   public suspend fun getChapterList(context: RoutingContext): ReturnData {
      var `$continuation`: Continuation;
      label199: {
         if (`$completion` is <unrepresentable>) {
            `$continuation` = `$completion` as <unrepresentable>;
            if (((`$completion` as <unrepresentable>).label and Integer.MIN_VALUE) != 0) {
               `$continuation`.label -= Integer.MIN_VALUE;
               break label199;
            }
         }

         `$continuation` = new ContinuationImpl(this, `$completion`) {
            Object L$0;
            Object L$1;
            Object L$2;
            Object L$3;
            Object L$4;
            Object L$5;
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
               return this.this$0.getChapterList(null, this);
            }
         };
      }

      var returnData: ReturnData;
      var var10000: Book;
      label215: {
         var refresh: Int;
         var userNameSpace: java.lang.String;
         var bookInfo: Book;
         var bookSource: java.lang.String;
         var var21: Any;
         label232: {
            label233: {
               label234: {
                  var var17: BookController;
                  label219: {
                     var bookUrl: java.lang.String;
                     label180: {
                        label179: {
                           label204: {
                              val `$result`: Any = `$continuation`.result;
                              var21 = IntrinsicsKt.getCOROUTINE_SUSPENDED();
                              switch ($continuation.label) {
                                 case 0:
                                    ResultKt.throwOnFailure(`$result`);
                                    returnData = new ReturnData();
                                    `$continuation`.L$0 = this;
                                    `$continuation`.L$1 = context;
                                    `$continuation`.L$2 = returnData;
                                    `$continuation`.label = 1;
                                    var10000 = this.checkAuth(context, `$continuation`);
                                    if (var10000 === var21) {
                                       return var21;
                                    }
                                    break;
                                 case 1:
                                    returnData = `$continuation`.L$2 as ReturnData;
                                    context = `$continuation`.L$1 as RoutingContext;
                                    this = `$continuation`.L$0 as BookController;
                                    ResultKt.throwOnFailure(`$result`);
                                    var10000 = `$result`;
                                    break;
                                 case 2:
                                    refresh = `$continuation`.I$0;
                                    userNameSpace = `$continuation`.L$4 as java.lang.String;
                                    bookUrl = `$continuation`.L$3 as java.lang.String;
                                    returnData = `$continuation`.L$2 as ReturnData;
                                    context = `$continuation`.L$1 as RoutingContext;
                                    this = `$continuation`.L$0 as BookController;
                                    ResultKt.throwOnFailure(`$result`);
                                    var10000 = `$result`;
                                    break label179;
                                 case 3:
                                    refresh = `$continuation`.I$0;
                                    userNameSpace = `$continuation`.L$4 as java.lang.String;
                                    bookUrl = `$continuation`.L$3 as java.lang.String;
                                    returnData = `$continuation`.L$2 as ReturnData;
                                    context = `$continuation`.L$1 as RoutingContext;
                                    this = `$continuation`.L$0 as BookController;
                                    ResultKt.throwOnFailure(`$result`);
                                    var10000 = `$result`;
                                    break label204;
                                 case 4:
                                    refresh = `$continuation`.I$0;
                                    var17 = `$continuation`.L$5 as BookController;
                                    bookSource = `$continuation`.L$4 as java.lang.String;
                                    userNameSpace = `$continuation`.L$3 as java.lang.String;
                                    returnData = `$continuation`.L$2 as ReturnData;
                                    context = `$continuation`.L$1 as RoutingContext;
                                    this = `$continuation`.L$0 as BookController;
                                    ResultKt.throwOnFailure(`$result`);
                                    var10000 = `$result`;
                                    break label219;
                                 case 5:
                                    refresh = `$continuation`.I$0;
                                    bookSource = `$continuation`.L$4 as java.lang.String;
                                    userNameSpace = `$continuation`.L$3 as java.lang.String;
                                    returnData = `$continuation`.L$2 as ReturnData;
                                    context = `$continuation`.L$1 as RoutingContext;
                                    this = `$continuation`.L$0 as BookController;
                                    ResultKt.throwOnFailure(`$result`);
                                    var10000 = `$result`;
                                    break label234;
                                 case 6:
                                    refresh = `$continuation`.I$0;
                                    bookSource = `$continuation`.L$5 as java.lang.String;
                                    bookInfo = `$continuation`.L$4 as Book;
                                    userNameSpace = `$continuation`.L$3 as java.lang.String;
                                    returnData = `$continuation`.L$2 as ReturnData;
                                    context = `$continuation`.L$1 as RoutingContext;
                                    this = `$continuation`.L$0 as BookController;
                                    ResultKt.throwOnFailure(`$result`);
                                    break label232;
                                 case 7:
                                    refresh = `$continuation`.I$0;
                                    bookInfo = `$continuation`.L$4 as Book;
                                    userNameSpace = `$continuation`.L$3 as java.lang.String;
                                    returnData = `$continuation`.L$2 as ReturnData;
                                    context = `$continuation`.L$1 as RoutingContext;
                                    this = `$continuation`.L$0 as BookController;
                                    ResultKt.throwOnFailure(`$result`);
                                    var10000 = `$result`;
                                    break label233;
                                 case 8:
                                    returnData = `$continuation`.L$0 as ReturnData;
                                    ResultKt.throwOnFailure(`$result`);
                                    var10000 = (Book)`$result`;
                                    break label215;
                                 default:
                                    throw new IllegalStateException("call to 'resume' before 'invoke' with coroutine");
                              }

                              if (!var10000 as java.lang.Boolean) {
                                 return ReturnData.setData$default(returnData, "NEED_LOGIN", null, 2, null).setErrorMsg("请登录后使用");
                              }

                              if (context.request().method() === HttpMethod.POST) {
                                 val var29: java.lang.String = context.getBodyAsJson().getString("url");
                                 userNameSpace = if (var29 == null) context.getBodyAsJson().getJsonObject("book").getString("bookUrl") else var29;
                                 bookUrl = if (userNameSpace == null) "" else userNameSpace;
                                 val var25: Int = context.getBodyAsJson().getInteger("refresh", Boxing.boxInt(0));
                                 refresh = var25.intValue();
                              } else {
                                 val var30: java.util.List = context.queryParam("url");
                                 userNameSpace = CollectionsKt.firstOrNull(var30);
                                 bookUrl = if (userNameSpace == null) "" else userNameSpace;
                                 val var31: java.util.List = context.queryParam("refresh");
                                 userNameSpace = CollectionsKt.firstOrNull(var31);
                                 val var50: Int;
                                 if (userNameSpace == null) {
                                    var50 = 0;
                                 } else {
                                    val var32: Int = Boxing.boxInt(Integer.parseInt(userNameSpace));
                                    var50 = if (var32 == null) 0 else var32;
                                 }

                                 refresh = var50;
                              }

                              if (bookUrl.length() == 0) {
                                 return returnData.setErrorMsg("请输入书籍链接");
                              }

                              userNameSpace = this.getUserNameSpace(context);
                              bookInfo = this.getShelfBookByURL(bookUrl, userNameSpace);
                              if (bookInfo != null) {
                                 val var54: java.lang.String = bookInfo.getOrigin();
                                 `$continuation`.L$0 = this;
                                 `$continuation`.L$1 = context;
                                 `$continuation`.L$2 = returnData;
                                 `$continuation`.L$3 = userNameSpace;
                                 `$continuation`.L$4 = bookInfo;
                                 `$continuation`.I$0 = refresh;
                                 `$continuation`.label = 7;
                                 var10000 = getBookSourceString$default(this, context, var54, false, `$continuation`, 4, null);
                                 if (var10000 === var21) {
                                    return var21;
                                 }
                                 break label233;
                              }

                              val var10: java.lang.String = this.bookInfoCache.getAsString(bookUrl);
                              if (var10 == null) {
                                 var10000 = null;
                              } else {
                                 val var11: java.util.Map = ExtKt.toMap(var10);
                                 var10000 = if (var11 == null)
                                    null
                                    else
                                    ExtKt.getGson()
                                       .fromJson(
                                          if (var11 is java.lang.String) var11 as java.lang.String else ExtKt.getGson().toJson(var11),
                                          new BookController$getChapterList$$inlined$toDataClass$1().getType()
                                       );
                              }

                              if (var10000 != null) {
                                 val var10002: java.lang.String = var10000.getOrigin();
                                 `$continuation`.L$0 = this;
                                 `$continuation`.L$1 = context;
                                 `$continuation`.L$2 = returnData;
                                 `$continuation`.L$3 = bookUrl;
                                 `$continuation`.L$4 = userNameSpace;
                                 `$continuation`.I$0 = refresh;
                                 `$continuation`.label = 2;
                                 var10000 = getBookSourceString$default(this, context, var10002, false, `$continuation`, 4, null);
                                 if (var10000 === var21) {
                                    return var21;
                                 }
                                 break label179;
                              }

                              `$continuation`.L$0 = this;
                              `$continuation`.L$1 = context;
                              `$continuation`.L$2 = returnData;
                              `$continuation`.L$3 = bookUrl;
                              `$continuation`.L$4 = userNameSpace;
                              `$continuation`.I$0 = refresh;
                              `$continuation`.label = 3;
                              var10000 = getBookSourceString$default(this, context, null, false, `$continuation`, 6, null);
                              if (var10000 === var21) {
                                 return var21;
                              }
                           }

                           bookSource = var10000 as java.lang.String;
                           break label180;
                        }

                        bookSource = var10000 as java.lang.String;
                     }

                     if (bookSource == null || bookSource.length() == 0) {
                        return returnData.setErrorMsg("未配置书源");
                     }

                     var17 = this;
                     val var52: WebBook = new WebBook(bookSource, this.getAppConfig().getDebugLog(), null, userNameSpace, 4, null);
                     `$continuation`.L$0 = this;
                     `$continuation`.L$1 = context;
                     `$continuation`.L$2 = returnData;
                     `$continuation`.L$3 = userNameSpace;
                     `$continuation`.L$4 = bookSource;
                     `$continuation`.L$5 = this;
                     `$continuation`.I$0 = refresh;
                     `$continuation`.label = 4;
                     var10000 = WebBook.getBookInfo$default(var52, bookUrl, false, `$continuation`, 2, null);
                     if (var10000 === var21) {
                        return var21;
                     }
                  }

                  val var10001: Book = var10000 as Book;
                  `$continuation`.L$0 = this;
                  `$continuation`.L$1 = context;
                  `$continuation`.L$2 = returnData;
                  `$continuation`.L$3 = userNameSpace;
                  `$continuation`.L$4 = bookSource;
                  `$continuation`.L$5 = null;
                  `$continuation`.I$0 = refresh;
                  `$continuation`.label = 5;
                  var10000 = var17.mergeBookCacheInfo(var10001, `$continuation`);
                  if (var10000 === var21) {
                     return var21;
                  }
               }

               bookInfo = var10000 as Book;
               val var53: java.util.List = CollectionsKt.arrayListOf(new Book[]{var10000 as Book});
               `$continuation`.L$0 = this;
               `$continuation`.L$1 = context;
               `$continuation`.L$2 = returnData;
               `$continuation`.L$3 = userNameSpace;
               `$continuation`.L$4 = bookInfo;
               `$continuation`.L$5 = bookSource;
               `$continuation`.I$0 = refresh;
               `$continuation`.label = 6;
               if (this.saveBookInfoCache(var53, `$continuation`) === var21) {
                  return var21;
               }
               break label232;
            }

            bookSource = var10000 as java.lang.String;
         }

         if (!bookInfo.isLocalBook() && (bookSource == null || bookSource.length() == 0)) {
            return returnData.setErrorMsg("未配置书源");
         }

         bookInfo.setRootDir(ExtKt.getWorkDir$default(null, 1, null));
         bookInfo.setUserNameSpace(userNameSpace);
         if (bookInfo.isLocalBook()) {
            val var37: File = bookInfo.getLocalFile();
            if (!var37.exists()) {
               BookControllerKt.access$getLogger$p().info("localFile: {} not exists", var37);
               return returnData.setErrorMsg("本地书籍源文件不存在");
            }
         }

         BookControllerKt.access$getLogger$p().info("bookInfo: {}", bookInfo);
         val var55: java.lang.String = if (bookSource == null) "" else bookSource;
         val var10003: Boolean = refresh > 0;
         val var10004: java.lang.String = this.getUserNameSpace(context);
         `$continuation`.L$0 = returnData;
         `$continuation`.L$1 = null;
         `$continuation`.L$2 = null;
         `$continuation`.L$3 = null;
         `$continuation`.L$4 = null;
         `$continuation`.L$5 = null;
         `$continuation`.label = 8;
         var10000 = (Book)getLocalChapterList$default(this, bookInfo, var55, var10003, var10004, false, null, `$continuation`, 48, null);
         if (var10000 === var21) {
            return var21;
         }
      }

      return ReturnData.setData$default(returnData, var10000 as java.util.List, null, 2, null);
   }

   public suspend fun saveBookProgress(context: RoutingContext): ReturnData {
      var `$continuation`: Continuation;
      label131: {
         if (`$completion` is <unrepresentable>) {
            `$continuation` = `$completion` as <unrepresentable>;
            if (((`$completion` as <unrepresentable>).label and Integer.MIN_VALUE) != 0) {
               `$continuation`.label -= Integer.MIN_VALUE;
               break label131;
            }
         }

         `$continuation` = new ContinuationImpl(this, `$completion`) {
            Object L$0;
            Object L$1;
            Object L$2;
            Object L$3;
            Object L$4;
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
               return this.this$0.saveBookProgress(null, this);
            }
         };
      }

      var userNameSpace: java.lang.String;
      var bookInfo: Book;
      var chapterInfo: BookChapter;
      var var14: Any;
      var returnData: ReturnData;
      label134: {
         var chapterIndex: Int;
         var var10000: Any;
         label145: {
            val `$result`: Any = `$continuation`.result;
            var14 = IntrinsicsKt.getCOROUTINE_SUSPENDED();
            switch ($continuation.label) {
               case 0:
                  ResultKt.throwOnFailure(`$result`);
                  returnData = new ReturnData();
                  `$continuation`.L$0 = this;
                  `$continuation`.L$1 = context;
                  `$continuation`.L$2 = returnData;
                  `$continuation`.label = 1;
                  var10000 = this.checkAuth(context, `$continuation`);
                  if (var10000 === var14) {
                     return var14;
                  }
                  break;
               case 1:
                  returnData = `$continuation`.L$2 as ReturnData;
                  context = `$continuation`.L$1 as RoutingContext;
                  this = `$continuation`.L$0 as BookController;
                  ResultKt.throwOnFailure(`$result`);
                  var10000 = `$result`;
                  break;
               case 2:
                  chapterIndex = `$continuation`.I$0;
                  bookInfo = `$continuation`.L$3 as Book;
                  userNameSpace = `$continuation`.L$2 as java.lang.String;
                  returnData = `$continuation`.L$1 as ReturnData;
                  this = `$continuation`.L$0 as BookController;
                  ResultKt.throwOnFailure(`$result`);
                  var10000 = `$result`;
                  break label145;
               case 3:
                  chapterInfo = `$continuation`.L$4 as BookChapter;
                  bookInfo = `$continuation`.L$3 as Book;
                  userNameSpace = `$continuation`.L$2 as java.lang.String;
                  returnData = `$continuation`.L$1 as ReturnData;
                  this = `$continuation`.L$0 as BookController;
                  ResultKt.throwOnFailure(`$result`);
                  break label134;
               case 4:
                  returnData = `$continuation`.L$0 as ReturnData;
                  ResultKt.throwOnFailure(`$result`);
                  return ReturnData.setData$default(returnData, "", null, 2, null);
               default:
                  throw new IllegalStateException("call to 'resume' before 'invoke' with coroutine");
            }

            if (!var10000 as java.lang.Boolean) {
               return ReturnData.setData$default(returnData, "NEED_LOGIN", null, 2, null).setErrorMsg("请登录后使用");
            }

            val var15: java.lang.String;
            if (context.request().method() === HttpMethod.POST) {
               val var22: java.lang.String = context.getBodyAsJson().getString("url");
               userNameSpace = if (var22 == null) context.getBodyAsJson().getJsonObject("searchBook").getString("bookUrl") else var22;
               var15 = if (userNameSpace == null) "" else userNameSpace;
               val var18: Int = context.getBodyAsJson().getInteger("index", Boxing.boxInt(-1));
               chapterIndex = var18.intValue();
            } else {
               val var23: java.util.List = context.queryParam("url");
               userNameSpace = CollectionsKt.firstOrNull(var23);
               var15 = if (userNameSpace == null) "" else userNameSpace;
               val var24: java.util.List = context.queryParam("index");
               userNameSpace = CollectionsKt.firstOrNull(var24);
               val var33: Int;
               if (userNameSpace == null) {
                  var33 = -1;
               } else {
                  val var25: Int = Boxing.boxInt(Integer.parseInt(userNameSpace));
                  var33 = if (var25 == null) -1 else var25;
               }

               chapterIndex = var33;
            }

            if (var15.length() == 0) {
               return returnData.setErrorMsg("请输入书籍链接");
            }

            userNameSpace = this.getUserNameSpace(context);
            bookInfo = this.getShelfBookByURL(var15, userNameSpace);
            if (bookInfo == null) {
               return returnData.setErrorMsg("书籍未加入书架");
            }

            if (bookInfo.getOrigin().length() == 0) {
               return returnData.setErrorMsg("书籍未加入书架");
            }

            val var27: java.lang.String = this.getBookSourceStringBySourceURLOpt(bookInfo.getOrigin(), userNameSpace);
            if (!bookInfo.isLocalBook() && (var27 == null || var27.length() == 0)) {
               return returnData.setErrorMsg("未配置书源");
            }

            val var10002: java.lang.String = if (var27 == null) "" else var27;
            `$continuation`.L$0 = this;
            `$continuation`.L$1 = returnData;
            `$continuation`.L$2 = userNameSpace;
            `$continuation`.L$3 = bookInfo;
            `$continuation`.I$0 = chapterIndex;
            `$continuation`.label = 2;
            var10000 = getLocalChapterList$default(this, bookInfo, var10002, false, userNameSpace, false, null, `$continuation`, 48, null);
            if (var10000 === var14) {
               return var14;
            }
         }

         val var30: java.util.List = var10000 as java.util.List;
         if (chapterIndex >= (var10000 as java.util.List).size()) {
            return returnData.setErrorMsg("章节不存在");
         }

         chapterInfo = var30.get(chapterIndex) as BookChapter;
         `$continuation`.L$0 = this;
         `$continuation`.L$1 = returnData;
         `$continuation`.L$2 = userNameSpace;
         `$continuation`.L$3 = bookInfo;
         `$continuation`.L$4 = chapterInfo;
         `$continuation`.label = 3;
         if (this.saveShelfBookProgress(bookInfo, chapterInfo, userNameSpace, `$continuation`) === var14) {
            return var14;
         }
      }

      `$continuation`.L$0 = returnData;
      `$continuation`.L$1 = null;
      `$continuation`.L$2 = null;
      `$continuation`.L$3 = null;
      `$continuation`.L$4 = null;
      `$continuation`.label = 4;
      return if (this.saveBookProgressToWebdav(bookInfo, chapterInfo, userNameSpace, `$continuation`) === var14)
         var14
         else
         ReturnData.setData$default(returnData, "", null, 2, null);
   }

   public suspend fun getBookContent(context: RoutingContext): ReturnData {
      var `$continuation`: Continuation;
      label512: {
         if (`$completion` is <unrepresentable>) {
            `$continuation` = `$completion` as <unrepresentable>;
            if (((`$completion` as <unrepresentable>).label and Integer.MIN_VALUE) != 0) {
               `$continuation`.label -= Integer.MIN_VALUE;
               break label512;
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
            int I$0;
            int I$1;
            int I$2;
            int I$3;
            int I$4;
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
               return this.this$0.getBookContent(null, this);
            }
         };
      }

      var bookSource: java.lang.String;
      var userNameSpace: java.lang.String;
      var e: Exception;
      var var159: Int;
      label504: {
         label503: {
            var bookSourceObject: java.lang.CharSequence;
            label502: {
               var returnData: ReturnData;
               var bookInfo: Book;
               var chapterInfo: BookChapter;
               var content: java.lang.String;
               label576: {
                  var chapterCacheFile: File;
                  var var31: Any;
                  label577: {
                     var chapterUrl: java.lang.String;
                     var chapterIndex: Int;
                     var refresh: Int;
                     var epubContent: Int;
                     var nextChapterUrl: java.lang.String;
                     label578: {
                        label579: {
                           label580: {
                              var cache: Int;
                              var isInBookShelf: Boolean;
                              label521: {
                                 label581: {
                                    label582: {
                                       var var27: BookController;
                                       label555: {
                                          var bookUrl: java.lang.String;
                                          label583: {
                                             label523: {
                                                label524: {
                                                   val `$result`: Any = `$continuation`.result;
                                                   var31 = IntrinsicsKt.getCOROUTINE_SUSPENDED();
                                                   var var146: Any;
                                                   switch ($continuation.label) {
                                                      case 0:
                                                         ResultKt.throwOnFailure(`$result`);
                                                         returnData = new ReturnData();
                                                         `$continuation`.L$0 = this;
                                                         `$continuation`.L$1 = context;
                                                         `$continuation`.L$2 = returnData;
                                                         `$continuation`.label = 1;
                                                         var146 = this.checkAuth(context, `$continuation`);
                                                         if (var146 === var31) {
                                                            return var31;
                                                         }
                                                         break;
                                                      case 1:
                                                         returnData = `$continuation`.L$2 as ReturnData;
                                                         context = `$continuation`.L$1 as RoutingContext;
                                                         this = `$continuation`.L$0 as BookController;
                                                         ResultKt.throwOnFailure(`$result`);
                                                         var146 = `$result`;
                                                         break;
                                                      case 2:
                                                         epubContent = `$continuation`.I$3;
                                                         refresh = `$continuation`.I$2;
                                                         cache = `$continuation`.I$1;
                                                         chapterIndex = `$continuation`.I$0;
                                                         bookUrl = `$continuation`.L$4 as java.lang.String;
                                                         chapterUrl = `$continuation`.L$3 as java.lang.String;
                                                         returnData = `$continuation`.L$2 as ReturnData;
                                                         context = `$continuation`.L$1 as RoutingContext;
                                                         this = `$continuation`.L$0 as BookController;
                                                         ResultKt.throwOnFailure(`$result`);
                                                         var145 = `$result`;
                                                         break label524;
                                                      case 3:
                                                         isInBookShelf = (boolean)`$continuation`.I$4;
                                                         epubContent = `$continuation`.I$3;
                                                         refresh = `$continuation`.I$2;
                                                         cache = `$continuation`.I$1;
                                                         chapterIndex = `$continuation`.I$0;
                                                         nextChapterUrl = null;
                                                         chapterInfo = null;
                                                         bookInfo = `$continuation`.L$5 as Book;
                                                         userNameSpace = `$continuation`.L$4 as java.lang.String;
                                                         bookUrl = `$continuation`.L$3 as java.lang.String;
                                                         chapterUrl = `$continuation`.L$2 as java.lang.String;
                                                         returnData = `$continuation`.L$1 as ReturnData;
                                                         this = `$continuation`.L$0 as BookController;
                                                         ResultKt.throwOnFailure(`$result`);
                                                         var144 = `$result`;
                                                         break label523;
                                                      case 4:
                                                         isInBookShelf = (boolean)`$continuation`.I$4;
                                                         epubContent = `$continuation`.I$3;
                                                         refresh = `$continuation`.I$2;
                                                         cache = `$continuation`.I$1;
                                                         chapterIndex = `$continuation`.I$0;
                                                         var27 = `$continuation`.L$5 as BookController;
                                                         nextChapterUrl = null;
                                                         chapterInfo = null;
                                                         userNameSpace = `$continuation`.L$4 as java.lang.String;
                                                         bookSource = `$continuation`.L$3 as java.lang.String;
                                                         chapterUrl = `$continuation`.L$2 as java.lang.String;
                                                         returnData = `$continuation`.L$1 as ReturnData;
                                                         this = `$continuation`.L$0 as BookController;
                                                         ResultKt.throwOnFailure(`$result`);
                                                         var143 = `$result`;
                                                         break label555;
                                                      case 5:
                                                         isInBookShelf = (boolean)`$continuation`.I$4;
                                                         epubContent = `$continuation`.I$3;
                                                         refresh = `$continuation`.I$2;
                                                         cache = `$continuation`.I$1;
                                                         chapterIndex = `$continuation`.I$0;
                                                         nextChapterUrl = null;
                                                         chapterInfo = null;
                                                         userNameSpace = `$continuation`.L$4 as java.lang.String;
                                                         bookSource = `$continuation`.L$3 as java.lang.String;
                                                         chapterUrl = `$continuation`.L$2 as java.lang.String;
                                                         returnData = `$continuation`.L$1 as ReturnData;
                                                         this = `$continuation`.L$0 as BookController;
                                                         ResultKt.throwOnFailure(`$result`);
                                                         var142 = `$result`;
                                                         break label582;
                                                      case 6:
                                                         isInBookShelf = (boolean)`$continuation`.I$4;
                                                         epubContent = `$continuation`.I$3;
                                                         refresh = `$continuation`.I$2;
                                                         cache = `$continuation`.I$1;
                                                         chapterIndex = `$continuation`.I$0;
                                                         nextChapterUrl = null;
                                                         chapterInfo = null;
                                                         bookInfo = `$continuation`.L$5 as Book;
                                                         userNameSpace = `$continuation`.L$4 as java.lang.String;
                                                         bookSource = `$continuation`.L$3 as java.lang.String;
                                                         chapterUrl = `$continuation`.L$2 as java.lang.String;
                                                         returnData = `$continuation`.L$1 as ReturnData;
                                                         this = `$continuation`.L$0 as BookController;
                                                         ResultKt.throwOnFailure(`$result`);
                                                         var141 = `$result`;
                                                         break label521;
                                                      case 7:
                                                         epubContent = `$continuation`.I$2;
                                                         refresh = `$continuation`.I$1;
                                                         chapterIndex = `$continuation`.I$0;
                                                         var85 = `$continuation`.L$6 as java.util.List;
                                                         nextChapterUrl = null;
                                                         chapterInfo = `$continuation`.L$5 as BookChapter;
                                                         bookInfo = `$continuation`.L$4 as Book;
                                                         userNameSpace = `$continuation`.L$3 as java.lang.String;
                                                         bookSource = `$continuation`.L$2 as java.lang.String;
                                                         returnData = `$continuation`.L$1 as ReturnData;
                                                         this = `$continuation`.L$0 as BookController;
                                                         ResultKt.throwOnFailure(`$result`);
                                                         break label580;
                                                      case 8:
                                                         epubContent = `$continuation`.I$2;
                                                         refresh = `$continuation`.I$1;
                                                         chapterIndex = `$continuation`.I$0;
                                                         var85 = `$continuation`.L$6 as java.util.List;
                                                         nextChapterUrl = null;
                                                         chapterInfo = `$continuation`.L$5 as BookChapter;
                                                         bookInfo = `$continuation`.L$4 as Book;
                                                         userNameSpace = `$continuation`.L$3 as java.lang.String;
                                                         bookSource = `$continuation`.L$2 as java.lang.String;
                                                         returnData = `$continuation`.L$1 as ReturnData;
                                                         this = `$continuation`.L$0 as BookController;
                                                         ResultKt.throwOnFailure(`$result`);
                                                         break label579;
                                                      case 9:
                                                         chapterCacheFile = `$continuation`.L$6 as File;
                                                         chapterInfo = `$continuation`.L$5 as BookChapter;
                                                         bookInfo = `$continuation`.L$4 as Book;
                                                         userNameSpace = `$continuation`.L$3 as java.lang.String;
                                                         bookSource = `$continuation`.L$2 as java.lang.String;
                                                         returnData = `$continuation`.L$1 as ReturnData;
                                                         this = `$continuation`.L$0 as BookController;

                                                         try {
                                                            ResultKt.throwOnFailure(`$result`);
                                                            var10000 = `$result`;
                                                            break label577;
                                                         } catch (var35: Exception) {
                                                            e = var35;
                                                            bookSourceObject = bookSource;
                                                            if (bookSource == null) {
                                                               break label503;
                                                            }
                                                            break label502;
                                                         }
                                                      case 10:
                                                         content = `$continuation`.L$6 as java.lang.String;
                                                         chapterInfo = `$continuation`.L$5 as BookChapter;
                                                         bookInfo = `$continuation`.L$4 as Book;
                                                         userNameSpace = `$continuation`.L$3 as java.lang.String;
                                                         bookSource = `$continuation`.L$2 as java.lang.String;
                                                         returnData = `$continuation`.L$1 as ReturnData;
                                                         this = `$continuation`.L$0 as BookController;

                                                         try {
                                                            ResultKt.throwOnFailure(`$result`);
                                                            break label576;
                                                         } catch (var36: Exception) {
                                                            e = var36;
                                                            bookSourceObject = bookSource;
                                                            if (bookSource == null) {
                                                               break label503;
                                                            }
                                                            break label502;
                                                         }
                                                      default:
                                                         throw new IllegalStateException("call to 'resume' before 'invoke' with coroutine");
                                                   }

                                                   if (!var146 as java.lang.Boolean) {
                                                      return ReturnData.setData$default(returnData, "NEED_LOGIN", null, 2, null).setErrorMsg("请登录后使用");
                                                   }

                                                   if (context.request().method() === HttpMethod.POST) {
                                                      bookSource = context.getBodyAsJson().getString("chapterUrl");
                                                      val var147: java.lang.String;
                                                      if (bookSource == null) {
                                                         val var56: JsonObject = context.getBodyAsJson().getJsonObject("bookChapter");
                                                         if (var56 == null) {
                                                            var147 = "";
                                                         } else {
                                                            val var69: java.lang.String = var56.getString("url");
                                                            var147 = if (var69 == null) "" else var69;
                                                         }
                                                      } else {
                                                         var147 = bookSource;
                                                      }

                                                      chapterUrl = var147;
                                                      bookSource = context.getBodyAsJson().getString("url");
                                                      val var148: java.lang.String;
                                                      if (bookSource == null) {
                                                         val var57: JsonObject = context.getBodyAsJson().getJsonObject("searchBook");
                                                         if (var57 == null) {
                                                            var148 = "";
                                                         } else {
                                                            val var70: java.lang.String = var57.getString("bookUrl");
                                                            var148 = if (var70 == null) "" else var70;
                                                         }
                                                      } else {
                                                         var148 = bookSource;
                                                      }

                                                      bookUrl = var148;
                                                      val var45: Int = context.getBodyAsJson().getInteger("index", Boxing.boxInt(-1));
                                                      chapterIndex = var45.intValue();
                                                      val var46: Int = context.getBodyAsJson().getInteger("cache", Boxing.boxInt(0));
                                                      cache = var46.intValue();
                                                      val var47: Int = context.getBodyAsJson().getInteger("refresh", Boxing.boxInt(0));
                                                      refresh = var47.intValue();
                                                      val var48: Int = context.getBodyAsJson().getInteger("epubContent", Boxing.boxInt(0));
                                                      epubContent = var48.intValue();
                                                   } else {
                                                      val var58: java.util.List = context.queryParam("chapterUrl");
                                                      bookSource = CollectionsKt.firstOrNull(var58);
                                                      chapterUrl = if (bookSource == null) "" else bookSource;
                                                      val var59: java.util.List = context.queryParam("url");
                                                      bookSource = CollectionsKt.firstOrNull(var59);
                                                      bookUrl = if (bookSource == null) "" else bookSource;
                                                      val var60: java.util.List = context.queryParam("index");
                                                      bookSource = CollectionsKt.firstOrNull(var60);
                                                      if (bookSource == null) {
                                                         var159 = -1;
                                                      } else {
                                                         val var61: Int = Boxing.boxInt(Integer.parseInt(bookSource));
                                                         var159 = if (var61 == null) -1 else var61;
                                                      }

                                                      chapterIndex = var159;
                                                      val var62: java.util.List = context.queryParam("cache");
                                                      bookSource = CollectionsKt.firstOrNull(var62);
                                                      if (bookSource == null) {
                                                         var159 = 0;
                                                      } else {
                                                         val var63: Int = Boxing.boxInt(Integer.parseInt(bookSource));
                                                         var159 = if (var63 == null) 0 else var63;
                                                      }

                                                      cache = var159;
                                                      val var64: java.util.List = context.queryParam("refresh");
                                                      bookSource = CollectionsKt.firstOrNull(var64);
                                                      if (bookSource == null) {
                                                         var159 = 0;
                                                      } else {
                                                         val var65: Int = Boxing.boxInt(Integer.parseInt(bookSource));
                                                         var159 = if (var65 == null) 0 else var65;
                                                      }

                                                      refresh = var159;
                                                      val var66: java.util.List = context.queryParam("epubContent");
                                                      bookSource = CollectionsKt.firstOrNull(var66);
                                                      if (bookSource == null) {
                                                         var159 = 0;
                                                      } else {
                                                         val var67: Int = Boxing.boxInt(Integer.parseInt(bookSource));
                                                         var159 = if (var67 == null) 0 else var67;
                                                      }

                                                      epubContent = var159;
                                                   }

                                                   if (bookUrl.length() == 0) {
                                                      return returnData.setErrorMsg("请输入书籍链接");
                                                   }

                                                   `$continuation`.L$0 = this;
                                                   `$continuation`.L$1 = context;
                                                   `$continuation`.L$2 = returnData;
                                                   `$continuation`.L$3 = chapterUrl;
                                                   `$continuation`.L$4 = bookUrl;
                                                   `$continuation`.I$0 = chapterIndex;
                                                   `$continuation`.I$1 = cache;
                                                   `$continuation`.I$2 = refresh;
                                                   `$continuation`.I$3 = epubContent;
                                                   `$continuation`.label = 2;
                                                   var145 = getBookSourceString$default(this, context, null, false, `$continuation`, 6, null);
                                                   if (var145 === var31) {
                                                      return var31;
                                                   }
                                                }

                                                bookSource = var145 as java.lang.String;
                                                userNameSpace = this.getUserNameSpace(context);
                                                isInBookShelf = false;
                                                bookInfo = null;
                                                chapterInfo = null;
                                                nextChapterUrl = null;
                                                if (bookUrl.length() <= 0) {
                                                   break label578;
                                                }

                                                bookInfo = this.getShelfBookByURL(bookUrl, userNameSpace);
                                                if (bookInfo != null && bookInfo.getOrigin().length() > 0) {
                                                   isInBookShelf = (boolean)1;
                                                   bookSource = this.getBookSourceStringBySourceURLOpt(bookInfo.getOrigin(), userNameSpace);
                                                }

                                                val var88: java.lang.String = this.bookInfoCache.getAsString(bookUrl);
                                                val var153: Book;
                                                if (var88 == null) {
                                                   var153 = null;
                                                } else {
                                                   val var95: java.util.Map = ExtKt.toMap(var88);
                                                   var153 = if (var95 == null)
                                                      null
                                                      else
                                                      ExtKt.getGson()
                                                         .fromJson(
                                                            if (var95 is java.lang.String) var95 as java.lang.String else ExtKt.getGson().toJson(var95),
                                                            new BookController$getBookContent$$inlined$toDataClass$1().getType()
                                                         );
                                                }

                                                if (var153 == null) {
                                                   break label583;
                                                }

                                                val var10002: java.lang.String = var153.getOrigin();
                                                `$continuation`.L$0 = this;
                                                `$continuation`.L$1 = returnData;
                                                `$continuation`.L$2 = chapterUrl;
                                                `$continuation`.L$3 = bookUrl;
                                                `$continuation`.L$4 = userNameSpace;
                                                `$continuation`.L$5 = bookInfo;
                                                `$continuation`.I$0 = chapterIndex;
                                                `$continuation`.I$1 = cache;
                                                `$continuation`.I$2 = refresh;
                                                `$continuation`.I$3 = epubContent;
                                                `$continuation`.I$4 = isInBookShelf;
                                                `$continuation`.label = 3;
                                                var144 = getBookSourceString$default(this, context, var10002, false, `$continuation`, 4, null);
                                                if (var144 === var31) {
                                                   return var31;
                                                }
                                             }

                                             bookSource = var144 as java.lang.String;
                                          }

                                          if (chapterUrl.length() != 0 || chapterIndex < 0) {
                                             break label578;
                                          }

                                          if (bookUrl.length() == 0) {
                                             return returnData.setErrorMsg("请输入书籍链接");
                                          }

                                          if (bookInfo != null && !bookInfo.isLocalBook() && (bookSource == null || bookSource.length() == 0)) {
                                             return returnData.setErrorMsg("未配置书源");
                                          }

                                          if (bookInfo != null) {
                                             var155 = bookInfo;
                                             break label581;
                                          }

                                          var27 = this;
                                          val var154: WebBook = new WebBook(
                                             if (bookSource == null) "" else bookSource, this.getAppConfig().getDebugLog(), null, userNameSpace, 4, null
                                          );
                                          `$continuation`.L$0 = this;
                                          `$continuation`.L$1 = returnData;
                                          `$continuation`.L$2 = chapterUrl;
                                          `$continuation`.L$3 = bookSource;
                                          `$continuation`.L$4 = userNameSpace;
                                          `$continuation`.L$5 = this;
                                          `$continuation`.I$0 = chapterIndex;
                                          `$continuation`.I$1 = cache;
                                          `$continuation`.I$2 = refresh;
                                          `$continuation`.I$3 = epubContent;
                                          `$continuation`.I$4 = isInBookShelf;
                                          `$continuation`.label = 4;
                                          var143 = WebBook.getBookInfo$default(var154, bookUrl, false, `$continuation`, 2, null);
                                          if (var143 === var31) {
                                             return var31;
                                          }
                                       }

                                       val var10001: Book = var143 as Book;
                                       `$continuation`.L$0 = this;
                                       `$continuation`.L$1 = returnData;
                                       `$continuation`.L$2 = chapterUrl;
                                       `$continuation`.L$3 = bookSource;
                                       `$continuation`.L$4 = userNameSpace;
                                       `$continuation`.L$5 = null;
                                       `$continuation`.I$0 = chapterIndex;
                                       `$continuation`.I$1 = cache;
                                       `$continuation`.I$2 = refresh;
                                       `$continuation`.I$3 = epubContent;
                                       `$continuation`.I$4 = isInBookShelf;
                                       `$continuation`.label = 5;
                                       var142 = var27.mergeBookCacheInfo(var10001, `$continuation`);
                                       if (var142 === var31) {
                                          return var31;
                                       }
                                    }

                                    var155 = var142 as Book;
                                 }

                                 bookInfo = var155;
                                 val var168: java.lang.String = if (bookSource == null) "" else bookSource;
                                 `$continuation`.L$0 = this;
                                 `$continuation`.L$1 = returnData;
                                 `$continuation`.L$2 = chapterUrl;
                                 `$continuation`.L$3 = bookSource;
                                 `$continuation`.L$4 = userNameSpace;
                                 `$continuation`.L$5 = var155;
                                 `$continuation`.I$0 = chapterIndex;
                                 `$continuation`.I$1 = cache;
                                 `$continuation`.I$2 = refresh;
                                 `$continuation`.I$3 = epubContent;
                                 `$continuation`.I$4 = isInBookShelf;
                                 `$continuation`.label = 6;
                                 var141 = getLocalChapterList$default(this, var155, var168, false, userNameSpace, false, null, `$continuation`, 48, null);
                                 if (var141 === var31) {
                                    return var31;
                                 }
                              }

                              var85 = var141 as java.util.List;
                              if (chapterIndex >= (var141 as java.util.List).size()) {
                                 break label578;
                              }

                              chapterInfo = var85.get(chapterIndex) as BookChapter;
                              if (isInBookShelf == 0 || cache == 1) {
                                 break label579;
                              }

                              `$continuation`.L$0 = this;
                              `$continuation`.L$1 = returnData;
                              `$continuation`.L$2 = bookSource;
                              `$continuation`.L$3 = userNameSpace;
                              `$continuation`.L$4 = bookInfo;
                              `$continuation`.L$5 = chapterInfo;
                              `$continuation`.L$6 = var85;
                              `$continuation`.I$0 = chapterIndex;
                              `$continuation`.I$1 = refresh;
                              `$continuation`.I$2 = epubContent;
                              `$continuation`.label = 7;
                              if (this.saveShelfBookProgress(bookInfo, chapterInfo, userNameSpace, `$continuation`) === var31) {
                                 return var31;
                              }
                           }

                           `$continuation`.L$0 = this;
                           `$continuation`.L$1 = returnData;
                           `$continuation`.L$2 = bookSource;
                           `$continuation`.L$3 = userNameSpace;
                           `$continuation`.L$4 = bookInfo;
                           `$continuation`.L$5 = chapterInfo;
                           `$continuation`.L$6 = var85;
                           `$continuation`.I$0 = chapterIndex;
                           `$continuation`.I$1 = refresh;
                           `$continuation`.I$2 = epubContent;
                           `$continuation`.label = 8;
                           if (this.saveBookProgressToWebdav(bookInfo, chapterInfo, userNameSpace, `$continuation`) === var31) {
                              return var31;
                           }
                        }

                        chapterUrl = chapterInfo.getUrl();
                        if (chapterIndex + 1 < var85.size()) {
                           nextChapterUrl = (var85.get(chapterIndex + 1) as BookChapter).getUrl();
                        }
                     }

                     if (bookInfo == null) {
                        return returnData.setErrorMsg("获取书籍信息失败");
                     }

                     if (!bookInfo.isLocalBook() && (bookSource == null || bookSource.length() == 0)) {
                        return returnData.setErrorMsg("未配置书源");
                     }

                     if (chapterInfo == null) {
                        return returnData.setErrorMsg("获取章节链接失败");
                     }

                     if (chapterUrl.length() == 0) {
                        return returnData.setErrorMsg("获取章节链接失败");
                     }

                     bookInfo.setRootDir(ExtKt.getWorkDir$default(null, 1, null));
                     bookInfo.setUserNameSpace(userNameSpace);
                     if (bookInfo.isLocalBook()) {
                        if (!bookInfo.getLocalFile().exists()) {
                           return returnData.setErrorMsg("本地源书籍文件不存在");
                        }

                        if (bookInfo.isEpub()) {
                           if (!extractEpub$default(this, bookInfo, false, 2, null)) {
                              return returnData.setErrorMsg("Epub书籍解压失败");
                           }

                           val var105: java.lang.String = bookInfo.getEpubRootDir();
                           val var112: java.lang.String = ExtKt.getWorkDir(bookInfo.getBookUrl(), "index", var105, chapterInfo.getUrl());
                           BookControllerKt.access$getLogger$p().info("chapterFilePath: {} {}", var112, var105);
                           if (!new File(var112).exists()) {
                              return returnData.setErrorMsg("章节文件不存在");
                           }

                           if (var105.length() == 0) {
                              content = "${StringsKt.replace$default(
                                 StringsKt.replace$default(bookInfo.getBookUrl(), "\\", "/", false, 4, null), "storage/data/", "/book-assets/", false, 4, null
                              )}/index/${chapterInfo.getUrl()}";
                           } else {
                              content = "${StringsKt.replace$default(
                                 StringsKt.replace$default(bookInfo.getBookUrl(), "\\", "/", false, 4, null), "storage/data/", "/book-assets/", false, 4, null
                              )}/index/$var105/${chapterInfo.getUrl()}";
                           }

                           if (epubContent > 0) {
                              return ReturnData.setData$default(
                                 returnData,
                                 MapsKt.mapOf(
                                    new Pair[]{
                                       TuplesKt.to("url", Intrinsics.stringPlus("__API_ROOT__", content)),
                                       TuplesKt.to("content", FilesKt.readText$default(new File(var112), null, 1, null))
                                    }
                                 ),
                                 null,
                                 2,
                                 null
                              );
                           }

                           return ReturnData.setData$default(returnData, content, null, 2, null);
                        }

                        if (bookInfo.isCbz()) {
                           if (!extractCbz$default(this, bookInfo, false, 2, null)) {
                              return returnData.setErrorMsg("CBZ书籍解压失败");
                           }

                           val chapterFilePathx: java.lang.String = ExtKt.getWorkDir(bookInfo.getBookUrl(), "index", chapterInfo.getUrl());
                           BookControllerKt.access$getLogger$p().info("chapterFilePath: {}", chapterFilePathx);
                           val var111: File = new File(chapterFilePathx);
                           if (!var111.exists()) {
                              return returnData.setErrorMsg("章节文件不存在");
                           }

                           val var164: BaseController = this;
                           var var132: java.lang.String = var111.getName();
                           var132 = BaseController.getFileExt$default(var164, var132, null, 2, null);
                           if (var132 == null) {
                              throw new NullPointerException("null cannot be cast to non-null type java.lang.String");
                           }

                           val var165: java.lang.String = var132.toLowerCase(Locale.ROOT);
                           val var134: java.util.List = CollectionsKt.listOf(new java.lang.String[]{"jpg", "jpeg", "gif", "png", "bmp", "webp", "svg"});
                           val var139: java.lang.String = "__API_ROOT__${StringsKt.replace$default(
                              StringsKt.replace$default(bookInfo.getBookUrl(), "\\", "/", false, 4, null), "storage/data/", "/book-assets/", false, 4, null
                           )}/index/${chapterInfo.getUrl()}";
                           if (!var134.contains(var165)) {
                              return ReturnData.setData$default(returnData, var139, null, 2, null);
                           }

                           return ReturnData.setData$default(returnData, "<img src='$var139' />", null, 2, null);
                        }

                        if (bookInfo.isPdf()) {
                           if (!convertPdfToImage$default(this, bookInfo, false, 2, null)) {
                              return returnData.setErrorMsg("PDF生成图片失败");
                           }

                           content = "";
                           if (chapterInfo.getStart() != null && chapterInfo.getEnd() != null) {
                              val var160: java.lang.Long = chapterInfo.getStart();
                              val var161: Long = var160;
                              val var167: java.lang.Long = chapterInfo.getEnd();
                              if (var161 <= var167) {
                                 val var162: java.lang.Long = chapterInfo.getStart();
                                 var var103: Long = var162;
                                 val var163: java.lang.Long = chapterInfo.getEnd();
                                 val var121: Long = var163;
                                 val var136: Long;
                                 if (var103 <= var121) {
                                    do {
                                       var136 = var103++;
                                       this.convertPdfPageToImage(bookInfo, (int)var136, refresh > 0);
                                       val chapterFilePathxx: java.lang.String = ExtKt.getWorkDir(bookInfo.getBookUrl(), "index", "output-$var136.png");
                                       BookControllerKt.access$getLogger$p().info("chapterFilePath: {}", chapterFilePathxx);
                                       if (!new File(chapterFilePathxx).exists()) {
                                          return returnData.setErrorMsg("章节文件不存在");
                                       }

                                       content = "$content<img src='__API_ROOT__${StringsKt.replace$default(
                                          StringsKt.replace$default(bookInfo.getBookUrl(), "\\", "/", false, 4, null),
                                          "storage/data/",
                                          "/book-assets/",
                                          false,
                                          4,
                                          null
                                       )}/index/output-$var136.png' />";
                                    } while (i != var121);
                                 }
                              }
                           }

                           return ReturnData.setData$default(returnData, content, null, 2, null);
                        }

                        val var109: java.lang.String = LocalBook.INSTANCE.getContent(bookInfo, chapterInfo);
                        if (var109 == null) {
                           return returnData.setErrorMsg("获取章节内容失败");
                        }

                        return ReturnData.setData$default(returnData, var109, null, 2, null);
                     }

                     chapterCacheFile = null;
                     if (bookInfo.isInShelf() && this.getAppConfig().getCacheChapterContent()) {
                        chapterCacheFile = new File("${this.getChapterCacheDir(bookInfo, userNameSpace).getAbsolutePath()}${File.separator}$chapterIndex.txt");
                        if (refresh <= 0 && chapterCacheFile.exists()) {
                           content = FilesKt.readText$default(chapterCacheFile, null, 1, null);
                           if (StringsKt.indexOf$default(content, "<img", 0, false, 6, null) >= 0) {
                              content = this.updateImageLinkInContent(bookInfo, chapterInfo, content);
                           }

                           BookControllerKt.access$getLogger$p().info("使用缓存的章节内容: {}", chapterCacheFile.toString());
                           return ReturnData.setData$default(returnData, content, null, 2, null);
                        }
                     }

                     try {
                        val var156: WebBook = new WebBook(
                           if (bookSource == null) "" else bookSource, this.getAppConfig().getDebugLog(), null, userNameSpace, 4, null
                        );
                        `$continuation`.L$0 = this;
                        `$continuation`.L$1 = returnData;
                        `$continuation`.L$2 = bookSource;
                        `$continuation`.L$3 = userNameSpace;
                        `$continuation`.L$4 = bookInfo;
                        `$continuation`.L$5 = chapterInfo;
                        `$continuation`.L$6 = chapterCacheFile;
                        `$continuation`.label = 9;
                        var10000 = var156.getBookContent(bookInfo, chapterInfo, nextChapterUrl, `$continuation`);
                     } catch (var34: Exception) {
                        e = var34;
                        bookSourceObject = bookSource;
                        if (bookSource == null) {
                           break label503;
                        }
                        break label502;
                     }

                     if (var10000 === var31) {
                        return var31;
                     }
                  }

                  try {
                     content = var10000 as java.lang.String;
                     if (!this.getAppConfig().getCacheChapterContent() || chapterCacheFile == null) {
                        return ReturnData.setData$default(returnData, content, null, 2, null);
                     }

                     FilesKt.writeText$default(chapterCacheFile, content, null, 2, null);
                     val var157: BookHelp = BookHelp.INSTANCE;
                     val var166: CoroutineScope = this;
                     bookSourceObject = (java.lang.CharSequence)BookSource.Companion.fromJson-IoAF18A(if (bookSource == null) "" else bookSource);
                     val var102: BookSource = (if (Result.isFailure-impl(bookSourceObject)) null else bookSourceObject) as BookSource;
                     val var169: BookSource = if (var102 == null)
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
                        var102;
                     `$continuation`.L$0 = this;
                     `$continuation`.L$1 = returnData;
                     `$continuation`.L$2 = bookSource;
                     `$continuation`.L$3 = userNameSpace;
                     `$continuation`.L$4 = bookInfo;
                     `$continuation`.L$5 = chapterInfo;
                     `$continuation`.L$6 = content;
                     `$continuation`.label = 10;
                     var158 = var157.saveImages(var166, var169, bookInfo, chapterInfo, content, `$continuation`);
                  } catch (var33: Exception) {
                     e = var33;
                     bookSourceObject = bookSource;
                     if (bookSource == null) {
                        break label503;
                     }
                     break label502;
                  }

                  if (var158 === var31) {
                     return var31;
                  }
               }

               try {
                  return ReturnData.setData$default(returnData, this.updateImageLinkInContent(bookInfo, chapterInfo, content), null, 2, null);
               } catch (var32: Exception) {
                  e = var32;
                  bookSourceObject = bookSource;
                  if (bookSource == null) {
                     break label503;
                  }
               }
            }

            if (bookSourceObject.length() != 0) {
               var159 = 0;
               break label504;
            }
         }

         var159 = 1;
      }

      if (!var159) {
         val var119: Any = BookSource.Companion.fromJson-IoAF18A(bookSource);
         val var108: BookSource = (if (Result.isFailure-impl(var119)) null else var119) as BookSource;
         if (var108 != null) {
            this.addInvalidBookSource(
               var108.getBookSourceUrl(),
               MapsKt.mutableMapOf(
                  new Pair[]{
                     TuplesKt.to("sourceUrl", var108.getBookSourceUrl()),
                     TuplesKt.to("time", Boxing.boxLong(System.currentTimeMillis())),
                     TuplesKt.to("error", e.toString())
                  }
               ),
               userNameSpace
            );
         }
      }

      throw e;
   }

   public suspend fun saveBookContent(context: RoutingContext): ReturnData {
      var `$continuation`: Continuation;
      label51: {
         if (`$completion` is <unrepresentable>) {
            `$continuation` = `$completion` as <unrepresentable>;
            if (((`$completion` as <unrepresentable>).label and Integer.MIN_VALUE) != 0) {
               `$continuation`.label -= Integer.MIN_VALUE;
               break label51;
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
               return this.this$0.saveBookContent(null, this);
            }
         };
      }

      val `$result`: Any = `$continuation`.result;
      val var16: Any = IntrinsicsKt.getCOROUTINE_SUSPENDED();
      var returnData: ReturnData;
      var var10000: Any;
      switch ($continuation.label) {
         case 0:
            ResultKt.throwOnFailure(`$result`);
            returnData = new ReturnData();
            `$continuation`.L$0 = this;
            `$continuation`.L$1 = context;
            `$continuation`.L$2 = returnData;
            `$continuation`.label = 1;
            var10000 = this.checkAuth(context, `$continuation`);
            if (var10000 === var16) {
               return var16;
            }
            break;
         case 1:
            returnData = `$continuation`.L$2 as ReturnData;
            context = `$continuation`.L$1 as RoutingContext;
            this = `$continuation`.L$0 as BookController;
            ResultKt.throwOnFailure(`$result`);
            var10000 = `$result`;
            break;
         default:
            throw new IllegalStateException("call to 'resume' before 'invoke' with coroutine");
      }

      if (!var10000 as java.lang.Boolean) {
         return ReturnData.setData$default(returnData, "NEED_LOGIN", null, 2, null).setErrorMsg("请登录后使用");
      } else {
         val chapterIndex: java.lang.String = context.getBodyAsJson().getString("url");
         val bookUrl: java.lang.String = if (chapterIndex == null) "" else chapterIndex;
         val var17: Int = context.getBodyAsJson().getInteger("index", Boxing.boxInt(-1));
         var userNameSpace: java.lang.String = context.getBodyAsJson().getString("content");
         val content: java.lang.String = if (userNameSpace == null) "" else userNameSpace;
         if (bookUrl.length() == 0) {
            return returnData.setErrorMsg("请输入书籍链接");
         } else {
            userNameSpace = this.getUserNameSpace(context);
            val var20: Book = this.getShelfBookByURL(bookUrl, userNameSpace);
            if (var20 == null) {
               return returnData.setErrorMsg("获取书籍信息失败");
            } else {
               FilesKt.writeText$default(
                  new File("${this.getChapterCacheDir(var20, userNameSpace).getAbsolutePath()}${File.separator}$var17.txt"), content, null, 2, null
               );
               val var21: File = new File(ExtKt.getWorkDir("storage", "data", userNameSpace, "${var20.getName()}_${var20.getAuthor()}", "custom"));
               if (!var21.exists()) {
                  var21.mkdirs();
               }

               FilesKt.writeText$default(new File("${var21.getAbsolutePath()}${File.separator}$var17.txt"), content, null, 2, null);
               return ReturnData.setData$default(returnData, "", null, 2, null);
            }
         }
      }
   }

   private fun updateImageLinkInContent(book: Book, chapter: BookChapter, content: String): String {
      val data: StringBuilder = new StringBuilder("");
      val dataDir: java.lang.String = ExtKt.getWorkDir("storage", "data");

      val var25: java.lang.Iterable;
      for (Object element$iv : var25) {
         var var27: java.lang.String = `element$iv` as java.lang.String;
         val matcher: Matcher = AppPattern.INSTANCE.getImgPattern().matcher(`element$iv` as java.lang.String);

         while (matcher.find()) {
            val var14: java.lang.String = matcher.group(1);
            if (var14 != null && StringsKt.indexOf$default(var14, "__API_ROOT__", 0, false, 6, null) < 0) {
               val imageFile: File = BookHelp.INSTANCE.getImage(book, NetworkUtils.INSTANCE.getAbsoluteURL(chapter.getUrl(), var14));
               if (imageFile.exists()) {
                  val var22: java.lang.String = imageFile.getPath();
                  var27 = StringsKt.replace$default(
                     var27,
                     var14,
                     "${Intrinsics.stringPlus("__API_ROOT__", StringsKt.replace$default(var22, dataDir, "/book-assets", false, 4, null))}\" data-error=\"$var14",
                     false,
                     4,
                     null
                  );
               }
            }
         }

         data.append(var27).append("\n");
      }

      val var26: java.lang.String = data.toString();
      return var26;
   }

   public suspend fun exploreBook(context: RoutingContext): ReturnData {
      var `$continuation`: Continuation;
      label70: {
         if (`$completion` is <unrepresentable>) {
            `$continuation` = `$completion` as <unrepresentable>;
            if (((`$completion` as <unrepresentable>).label and Integer.MIN_VALUE) != 0) {
               `$continuation`.label -= Integer.MIN_VALUE;
               break label70;
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
               return this.this$0.exploreBook(null, this);
            }
         };
      }

      var returnData: ReturnData;
      var var10000: WebBook;
      label78: {
         var var13: Any;
         label62: {
            val `$result`: Any = `$continuation`.result;
            var13 = IntrinsicsKt.getCOROUTINE_SUSPENDED();
            switch ($continuation.label) {
               case 0:
                  ResultKt.throwOnFailure(`$result`);
                  returnData = new ReturnData();
                  `$continuation`.L$0 = this;
                  `$continuation`.L$1 = context;
                  `$continuation`.L$2 = returnData;
                  `$continuation`.label = 1;
                  if (this.checkAuth(context, `$continuation`) === var13) {
                     return var13;
                  }
                  break;
               case 1:
                  returnData = `$continuation`.L$2 as ReturnData;
                  context = `$continuation`.L$1 as RoutingContext;
                  this = `$continuation`.L$0 as BookController;
                  ResultKt.throwOnFailure(`$result`);
                  break;
               case 2:
                  returnData = `$continuation`.L$2 as ReturnData;
                  context = `$continuation`.L$1 as RoutingContext;
                  this = `$continuation`.L$0 as BookController;
                  ResultKt.throwOnFailure(`$result`);
                  var10000 = `$result`;
                  break label62;
               case 3:
                  returnData = `$continuation`.L$0 as ReturnData;
                  ResultKt.throwOnFailure(`$result`);
                  var10000 = (WebBook)`$result`;
                  break label78;
               default:
                  throw new IllegalStateException("call to 'resume' before 'invoke' with coroutine");
            }

            `$continuation`.L$0 = this;
            `$continuation`.L$1 = context;
            `$continuation`.L$2 = returnData;
            `$continuation`.label = 2;
            var10000 = getBookSourceString$default(this, context, null, false, `$continuation`, 6, null);
            if (var10000 === var13) {
               return var13;
            }
         }

         val bookSource: java.lang.String = var10000 as java.lang.String;
         if (var10000 as java.lang.String == null || (var10000 as java.lang.String).length() == 0) {
            return returnData.setErrorMsg("未配置书源");
         }

         val var15: Int;
         val var17: java.lang.String;
         if (context.request().method() === HttpMethod.POST) {
            val var18: java.lang.String = context.getBodyAsJson().getString("ruleFindUrl");
            var17 = var18;
            val var19: Int = context.getBodyAsJson().getInteger("page", Boxing.boxInt(1));
            var15 = var19.intValue();
         } else {
            var result: java.util.List = context.queryParam("ruleFindUrl");
            var var20: java.lang.String = CollectionsKt.firstOrNull(result);
            var17 = if (var20 == null) "" else var20;
            result = context.queryParam("page");
            var20 = CollectionsKt.firstOrNull(result);
            val var27: Int;
            if (var20 == null) {
               var27 = 1;
            } else {
               val var24: Int = Boxing.boxInt(Integer.parseInt(var20));
               var27 = if (var24 == null) 1 else var24;
            }

            var15 = var27;
         }

         var10000 = new WebBook(bookSource, false, null, this.getUserNameSpace(context), 4, null);
         val var10002: Int = Boxing.boxInt(var15);
         `$continuation`.L$0 = returnData;
         `$continuation`.L$1 = null;
         `$continuation`.L$2 = null;
         `$continuation`.label = 3;
         var10000 = (WebBook)var10000.exploreBook(var17, var10002, `$continuation`);
         if (var10000 === var13) {
            return var13;
         }
      }

      return ReturnData.setData$default(returnData, var10000 as java.util.List, null, 2, null);
   }

   public suspend fun searchBook(context: RoutingContext): ReturnData {
      var `$continuation`: Continuation;
      label79: {
         if (`$completion` is <unrepresentable>) {
            `$continuation` = `$completion` as <unrepresentable>;
            if (((`$completion` as <unrepresentable>).label and Integer.MIN_VALUE) != 0) {
               `$continuation`.label -= Integer.MIN_VALUE;
               break label79;
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
               return this.this$0.searchBook(null, this);
            }
         };
      }

      var returnData: ReturnData;
      var var10000: WebBook;
      label88: {
         var var13: Any;
         label71: {
            val `$result`: Any = `$continuation`.result;
            var13 = IntrinsicsKt.getCOROUTINE_SUSPENDED();
            switch ($continuation.label) {
               case 0:
                  ResultKt.throwOnFailure(`$result`);
                  returnData = new ReturnData();
                  `$continuation`.L$0 = this;
                  `$continuation`.L$1 = context;
                  `$continuation`.L$2 = returnData;
                  `$continuation`.label = 1;
                  if (this.checkAuth(context, `$continuation`) === var13) {
                     return var13;
                  }
                  break;
               case 1:
                  returnData = `$continuation`.L$2 as ReturnData;
                  context = `$continuation`.L$1 as RoutingContext;
                  this = `$continuation`.L$0 as BookController;
                  ResultKt.throwOnFailure(`$result`);
                  break;
               case 2:
                  returnData = `$continuation`.L$2 as ReturnData;
                  context = `$continuation`.L$1 as RoutingContext;
                  this = `$continuation`.L$0 as BookController;
                  ResultKt.throwOnFailure(`$result`);
                  var10000 = `$result`;
                  break label71;
               case 3:
                  returnData = `$continuation`.L$0 as ReturnData;
                  ResultKt.throwOnFailure(`$result`);
                  var10000 = (WebBook)`$result`;
                  break label88;
               default:
                  throw new IllegalStateException("call to 'resume' before 'invoke' with coroutine");
            }

            `$continuation`.L$0 = this;
            `$continuation`.L$1 = context;
            `$continuation`.L$2 = returnData;
            `$continuation`.label = 2;
            var10000 = getBookSourceString$default(this, context, null, false, `$continuation`, 6, null);
            if (var10000 === var13) {
               return var13;
            }
         }

         val bookSource: java.lang.String = var10000 as java.lang.String;
         if (var10000 as java.lang.String == null || (var10000 as java.lang.String).length() == 0) {
            return returnData.setErrorMsg("未配置书源");
         }

         val var15: java.lang.String;
         val var17: Int;
         if (context.request().method() === HttpMethod.POST) {
            val var18: java.lang.String = context.getBodyAsJson().getString("key");
            var15 = var18;
            val var19: Int = context.getBodyAsJson().getInteger("page", Boxing.boxInt(1));
            var17 = var19.intValue();
         } else {
            var result: java.util.List = context.queryParam("key");
            var var20: java.lang.String = CollectionsKt.firstOrNull(result);
            var15 = if (var20 == null) "" else var20;
            result = context.queryParam("page");
            var20 = CollectionsKt.firstOrNull(result);
            val var29: Int;
            if (var20 == null) {
               var29 = 1;
            } else {
               val var25: Int = Boxing.boxInt(Integer.parseInt(var20));
               var29 = if (var25 == null) 1 else var25;
            }

            var17 = var29;
         }

         if (var15.length() == 0) {
            return returnData.setErrorMsg("请输入搜索关键字");
         }

         var10000 = new WebBook(bookSource, this.getAppConfig().getDebugLog(), null, this.getUserNameSpace(context), 4, null);
         val var10002: Int = Boxing.boxInt(var17);
         `$continuation`.L$0 = returnData;
         `$continuation`.L$1 = null;
         `$continuation`.L$2 = null;
         `$continuation`.label = 3;
         var10000 = (WebBook)var10000.searchBook(var15, var10002, `$continuation`);
         if (var10000 === var13) {
            return var13;
         }
      }

      return ReturnData.setData$default(returnData, var10000 as java.util.List, null, 2, null);
   }

   public suspend fun searchBookMulti(context: RoutingContext): ReturnData {
      var `$continuation`: Continuation;
      label118: {
         if (`$completion` is <unrepresentable>) {
            `$continuation` = `$completion` as <unrepresentable>;
            if (((`$completion` as <unrepresentable>).label and Integer.MIN_VALUE) != 0) {
               `$continuation`.label -= Integer.MIN_VALUE;
               break label118;
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
               return this.this$0.searchBookMulti(null, this);
            }
         };
      }

      var returnData: ReturnData;
      var lastIndex: IntRef;
      var resultList: ObjectRef;
      label132: {
         val `$result`: Any = `$continuation`.result;
         val var21: Any = IntrinsicsKt.getCOROUTINE_SUSPENDED();
         var var10000: Any;
         switch ($continuation.label) {
            case 0:
               ResultKt.throwOnFailure(`$result`);
               returnData = new ReturnData();
               `$continuation`.L$0 = this;
               `$continuation`.L$1 = context;
               `$continuation`.L$2 = returnData;
               `$continuation`.label = 1;
               var10000 = this.checkAuth(context, `$continuation`);
               if (var10000 === var21) {
                  return var21;
               }
               break;
            case 1:
               returnData = `$continuation`.L$2 as ReturnData;
               context = `$continuation`.L$1 as RoutingContext;
               this = `$continuation`.L$0 as BookController;
               ResultKt.throwOnFailure(`$result`);
               var10000 = `$result`;
               break;
            case 2:
               resultList = `$continuation`.L$2 as ObjectRef;
               lastIndex = `$continuation`.L$1 as IntRef;
               returnData = `$continuation`.L$0 as ReturnData;
               ResultKt.throwOnFailure(`$result`);
               break label132;
            default:
               throw new IllegalStateException("call to 'resume' before 'invoke' with coroutine");
         }

         if (!var10000 as java.lang.Boolean) {
            return ReturnData.setData$default(returnData, "NEED_LOGIN", null, 2, null).setErrorMsg("请登录后使用");
         }

         lastIndex = new IntRef();
         val searchSize: IntRef = new IntRef();
         val bookSourceGroup: ObjectRef = new ObjectRef();
         var var22: java.lang.String;
         var var23: Int;
         if (context.request().method() === HttpMethod.POST) {
            var userNameSpace: java.lang.String = context.getBodyAsJson().getString("key", "");
            var22 = userNameSpace;
            userNameSpace = context.getBodyAsJson().getString("bookSourceGroup", "");
            bookSourceGroup.element = (T)userNameSpace;
            val var26: Int = context.getBodyAsJson().getInteger("lastIndex", Boxing.boxInt(-1));
            lastIndex.element = var26.intValue();
            val var27: Int = context.getBodyAsJson().getInteger("searchSize", Boxing.boxInt(20));
            searchSize.element = var27.intValue();
            val var28: Int = context.getBodyAsJson().getInteger("concurrentCount", Boxing.boxInt(36));
            var23 = var28.intValue();
         } else {
            var urlMap: java.util.List = context.queryParam("key");
            var var29: java.lang.String = CollectionsKt.firstOrNull(urlMap);
            var22 = if (var29 == null) "" else var29;
            urlMap = context.queryParam("bookSourceGroup");
            var29 = CollectionsKt.firstOrNull(urlMap);
            bookSourceGroup.element = (T)(if (var29 == null) "" else var29);
            urlMap = context.queryParam("lastIndex");
            var29 = CollectionsKt.firstOrNull(urlMap);
            var var10001: Int;
            if (var29 == null) {
               var10001 = -1;
            } else {
               val var37: Int = Boxing.boxInt(Integer.parseInt(var29));
               var10001 = if (var37 == null) -1 else var37;
            }

            lastIndex.element = var10001;
            urlMap = context.queryParam("searchSize");
            var29 = CollectionsKt.firstOrNull(urlMap);
            if (var29 == null) {
               var10001 = 20;
            } else {
               val var39: Int = Boxing.boxInt(Integer.parseInt(var29));
               var10001 = if (var39 == null) 20 else var39;
            }

            searchSize.element = var10001;
            urlMap = context.queryParam("concurrentCount");
            var29 = CollectionsKt.firstOrNull(urlMap);
            val var56: Int;
            if (var29 == null) {
               var56 = 36;
            } else {
               val var41: Int = Boxing.boxInt(Integer.parseInt(var29));
               var56 = if (var41 == null) 36 else var41;
            }

            var23 = var56;
         }

         val var34: ObjectRef = new ObjectRef();
         var34.element = (T)this.getUserNameSpace(context);
         val var42: java.util.Map = new BookSourceController(this.getCoroutineContext()).getBookSourceMap(var34.element as java.lang.String);
         if (var42.size() <= 0) {
            return returnData.setErrorMsg("未配置书源");
         }

         if (var22.length() == 0) {
            return returnData.setErrorMsg("请输入搜索关键字");
         }

         val var43: BooleanRef = new BooleanRef();
         if (StringsKt.startsWith(var22, "=", true)) {
            var43.element = true;
            var22 = StringsKt.replaceFirst$default(var22, "=", "", false, 4, null);
         }

         if (var22 == null || var22.length() == 0) {
            return returnData.setErrorMsg("请输入搜索关键字");
         }

         if (lastIndex.element >= var42.size() - 1) {
            return returnData.setErrorMsg("没有更多了");
         }

         searchSize.element = if (searchSize.element > 0) searchSize.element else 20;
         var23 = if (var23 > 0) var23 else 36;
         BookControllerKt.access$getLogger$p()
            .info("searchBookMulti from lastIndex: {} searchSize: {}", Boxing.boxInt(lastIndex.element), Boxing.boxInt(searchSize.element));
         val var48: BooleanRef = new BooleanRef();
         context.request().connection().closeHandler(BookController::searchBookMulti$lambda-5);
         resultList = new ObjectRef();
         resultList.element = (T)(new ArrayList());
         val var52: ObjectRef = new ObjectRef();
         var52.element = (T)((new LinkedHashMap()) as java.util.Map);
         val var53: Book = new Book(
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
         var53.setName(var22);
         val maxSize: IntRef = new IntRef();
         maxSize.element = var42.size();
         val bookSourceFile: ObjectRef = new ObjectRef();
         bookSourceFile.element = (T)ExtKt.getStorageFile$default(
            new java.lang.String[]{"data", var34.element as java.lang.String, "bookSource"}, null, 2, null
         );
         if (!(bookSourceFile.element as File).exists()) {
            bookSourceFile.element = (T)ExtKt.getStorageFile$default(new java.lang.String[]{"data", "default", "bookSource"}, null, 2, null);
         }

         val var10002: Int = lastIndex.element + 1;
         val var10003: Int = var42.size();
         val var10004: Function3 = (
            new Function3<CoroutineScope, Integer, Continuation<? super Object>, Object>(
               maxSize, lastIndex, bookSourceFile, bookSourceGroup, this, var53, var43, var34, null
            ) {
               int label;

               {
                  super(3, `$completion`);
                  this.$maxSize = `$maxSize`;
                  this.$lastIndex = `$lastIndex`;
                  this.$bookSourceFile = `$bookSourceFile`;
                  this.$bookSourceGroup = `$bookSourceGroup`;
                  this.this$0 = `$receiver`;
                  this.$book = `$book`;
                  this.$accurate = `$accurate`;
                  this.$userNameSpace = `$userNameSpace`;
               }

               @Nullable
               @Override
               public final Object invokeSuspend(@NotNull Object $result) {
                  val var6: Any = IntrinsicsKt.getCOROUTINE_SUSPENDED();
                  var var10000: Any;
                  switch (this.label) {
                     case 0:
                        ResultKt.throwOnFailure(`$result`);
                        val it: Int = this.I$0;
                        if (this.I$0 > this.$maxSize.element) {
                           return new ArrayList();
                        }

                        this.$lastIndex.element = Math.max(this.$lastIndex.element, this.I$0);
                        val var7: JsonArray = ExtKt.parseJsonStringList$default(
                           this.$bookSourceFile.element,
                           null,
                           null,
                           it,
                           it,
                           null,
                           if (this.$bookSourceGroup.element.length() == 0)
                              null
                              else
                              (
                                 new Function1<ObjectNode, java.lang.Boolean>(this.$bookSourceGroup) {
                                    {
                                       super(1);
                                       this.$bookSourceGroup = `$bookSourceGroup`;
                                    }

                                    public final boolean invoke(@NotNull ObjectNode it) {
                                       val _bookSourceGroup: java.lang.String = it.get("bookSourceGroup").asText();
                                       return _bookSourceGroup != null
                                          && _bookSourceGroup.length() != 0
                                          && StringsKt.indexOf$default(
                                                Intrinsics.stringPlus(_bookSourceGroup, ","),
                                                Intrinsics.stringPlus(this.$bookSourceGroup.element, ","),
                                                0,
                                                false,
                                                6,
                                                null
                                             )
                                             >= 0;
                                    }
                                 }
                              ) as Function1,
                           38,
                           null
                        );
                        if (var7 == null || var7.isEmpty()) {
                           this.$maxSize.element = it;
                           return new ArrayList();
                        }

                        var10000 = this.this$0;
                        val var10: java.lang.String = var7.getString(0);
                        val var10002: Book = this.$book;
                        val var10003: Boolean = this.$accurate.element;
                        val var10004: java.lang.String = this.$userNameSpace.element;
                        val var10005: Continuation = this;
                        this.label = 1;
                        var10000 = (BookController)var10000.searchBookWithSource(var10, var10002, var10003, var10004, var10005);
                        if (var10000 === var6) {
                           return var6;
                        }
                        break;
                     case 1:
                        ResultKt.throwOnFailure(`$result`);
                        var10000 = (BookController)`$result`;
                        break;
                     default:
                        throw new IllegalStateException("call to 'resume' before 'invoke' with coroutine");
                  }

                  return var10000 as ArrayList;
               }

               @Nullable
               public final Object invoke(@NotNull CoroutineScope p1, int p2, @Nullable Continuation<Object> p3) {
                  val var4: Function3 = new <anonymous constructor>(
                     this.$maxSize,
                     this.$lastIndex,
                     this.$bookSourceFile,
                     this.$bookSourceGroup,
                     this.this$0,
                     this.$book,
                     this.$accurate,
                     this.$userNameSpace,
                     p3
                  );
                  var4.I$0 = p2;
                  return var4.invokeSuspend(Unit.INSTANCE);
               }
            }
         ) as Function3;
         val var10005: Function2 = (
            new Function2<ArrayList<Object>, Integer, java.lang.Boolean>(resultList, var48, this, searchSize, var52) {
               {
                  super(2);
                  this.$resultList = `$resultList`;
                  this.$isEnd = `$isEnd`;
                  this.this$0 = `$receiver`;
                  this.$searchSize = `$searchSize`;
                  this.$resultMap = `$resultMap`;
               }

               public final boolean invoke(@NotNull ArrayList<Object> list, int loopCount) {
                  val `$this$forEach$iv`: java.lang.Iterable = list;
                  val var4: ObjectRef = this.$resultMap;
                  val var5: ObjectRef = this.$resultList;

                  for (Object element$iv : $this$forEach$iv) {
                     val bookList: java.util.Collection = `element$iv` as? java.util.Collection;
                     if ((`element$iv` as? java.util.Collection) != null) {
                        val `$this$forEach$ivx`: java.lang.Iterable;
                        for (Object element$ivx : $this$forEach$ivx) {
                           val book: SearchBook = `element$ivx` as SearchBook;
                           val bookKey: java.lang.String = "${(`element$ivx` as SearchBook).getName()}_${(`element$ivx` as SearchBook).getAuthor()}";
                           if (!(var4.element as java.util.Map).containsKey(bookKey)) {
                              (var5.element as ArrayList).add(book);
                              (var4.element as java.util.Map).put(bookKey, 1);
                           }
                        }
                     }
                  }

                  BookControllerKt.access$getLogger$p().info("Loop: {} resultList.size: {}", loopCount, this.$resultList.element.size());
                  return !this.$isEnd.element
                     && loopCount < BookController.access$getConcurrentLoopCount$p(this.this$0)
                     && this.$resultList.element.size() < this.$searchSize.element;
               }
            }
         ) as Function2;
         `$continuation`.L$0 = returnData;
         `$continuation`.L$1 = lastIndex;
         `$continuation`.L$2 = resultList;
         `$continuation`.label = 2;
         if (this.limitConcurrent(var23, var10002, var10003, var10004, var10005, `$continuation`) === var21) {
            return var21;
         }
      }

      return ReturnData.setData$default(
         returnData,
         MapsKt.mapOf(new Pair[]{TuplesKt.to("lastIndex", Boxing.boxInt(lastIndex.element)), TuplesKt.to("list", resultList.element)}),
         null,
         2,
         null
      );
   }

   public suspend fun searchBookMultiSSE(context: RoutingContext) {
      var `$continuation`: Continuation;
      label126: {
         if (`$completion` is <unrepresentable>) {
            `$continuation` = `$completion` as <unrepresentable>;
            if (((`$completion` as <unrepresentable>).label and Integer.MIN_VALUE) != 0) {
               `$continuation`.label -= Integer.MIN_VALUE;
               break label126;
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
               return this.this$0.searchBookMultiSSE(null, this);
            }
         };
      }

      var response: HttpServerResponse;
      var lastIndex: IntRef;
      var maxSize: IntRef;
      label142: {
         val `$result`: Any = `$continuation`.result;
         val var21: Any = IntrinsicsKt.getCOROUTINE_SUSPENDED();
         var returnData: ReturnData;
         var var10000: Any;
         switch ($continuation.label) {
            case 0:
               ResultKt.throwOnFailure(`$result`);
               returnData = new ReturnData();
               response = context.response().putHeader("Content-Type", "text/event-stream").putHeader("Cache-Control", "no-cache").setChunked(true);
               `$continuation`.L$0 = this;
               `$continuation`.L$1 = context;
               `$continuation`.L$2 = returnData;
               `$continuation`.L$3 = response;
               `$continuation`.label = 1;
               var10000 = this.checkAuth(context, `$continuation`);
               if (var10000 === var21) {
                  return var21;
               }
               break;
            case 1:
               response = `$continuation`.L$3 as HttpServerResponse;
               returnData = `$continuation`.L$2 as ReturnData;
               context = `$continuation`.L$1 as RoutingContext;
               this = `$continuation`.L$0 as BookController;
               ResultKt.throwOnFailure(`$result`);
               var10000 = `$result`;
               break;
            case 2:
               maxSize = `$continuation`.L$2 as IntRef;
               lastIndex = `$continuation`.L$1 as IntRef;
               response = `$continuation`.L$0 as HttpServerResponse;
               ResultKt.throwOnFailure(`$result`);
               break label142;
            default:
               throw new IllegalStateException("call to 'resume' before 'invoke' with coroutine");
         }

         if (!var10000 as java.lang.Boolean) {
            response.write("event: error\n");
            response.end("data: ${ExtKt.jsonEncode(ReturnData.setData$default(returnData, "NEED_LOGIN", null, 2, null).setErrorMsg("请登录后使用"), false)}\n\n");
            return Unit.INSTANCE;
         }

         lastIndex = new IntRef();
         val searchSize: IntRef = new IntRef();
         val bookSourceGroup: ObjectRef = new ObjectRef();
         var var22: java.lang.String;
         val var23: Int;
         if (context.request().method() === HttpMethod.POST) {
            var userNameSpace: java.lang.String = context.getBodyAsJson().getString("key", "");
            var22 = userNameSpace;
            userNameSpace = context.getBodyAsJson().getString("bookSourceGroup", "");
            bookSourceGroup.element = (T)userNameSpace;
            val var26: Int = context.getBodyAsJson().getInteger("lastIndex", Boxing.boxInt(-1));
            lastIndex.element = var26.intValue();
            val var27: Int = context.getBodyAsJson().getInteger("searchSize", Boxing.boxInt(50));
            searchSize.element = var27.intValue();
            val var28: Int = context.getBodyAsJson().getInteger("concurrentCount", Boxing.boxInt(24));
            var23 = var28.intValue();
         } else {
            var urlMap: java.util.List = context.queryParam("key");
            var var29: java.lang.String = CollectionsKt.firstOrNull(urlMap);
            var22 = if (var29 == null) "" else var29;
            urlMap = context.queryParam("bookSourceGroup");
            var29 = CollectionsKt.firstOrNull(urlMap);
            bookSourceGroup.element = (T)(if (var29 == null) "" else var29);
            urlMap = context.queryParam("lastIndex");
            var29 = CollectionsKt.firstOrNull(urlMap);
            var var10001: Int;
            if (var29 == null) {
               var10001 = -1;
            } else {
               val var37: Int = Boxing.boxInt(Integer.parseInt(var29));
               var10001 = if (var37 == null) -1 else var37;
            }

            lastIndex.element = var10001;
            urlMap = context.queryParam("searchSize");
            var29 = CollectionsKt.firstOrNull(urlMap);
            if (var29 == null) {
               var10001 = 50;
            } else {
               val var39: Int = Boxing.boxInt(Integer.parseInt(var29));
               var10001 = if (var39 == null) 50 else var39;
            }

            searchSize.element = var10001;
            urlMap = context.queryParam("concurrentCount");
            var29 = CollectionsKt.firstOrNull(urlMap);
            val var56: Int;
            if (var29 == null) {
               var56 = 24;
            } else {
               val var41: Int = Boxing.boxInt(Integer.parseInt(var29));
               var56 = if (var41 == null) 24 else var41;
            }

            var23 = var56;
         }

         val var34: ObjectRef = new ObjectRef();
         var34.element = (T)this.getUserNameSpace(context);
         val var42: java.util.Map = new BookSourceController(this.getCoroutineContext()).getBookSourceMap(var34.element as java.lang.String);
         if (var42.size() <= 0) {
            response.write("event: error\n");
            response.end("data: ${ExtKt.jsonEncode(returnData.setErrorMsg("未配置书源"), false)}\n\n");
            return Unit.INSTANCE;
         }

         if (var22.length() == 0) {
            response.write("event: error\n");
            response.end("data: ${ExtKt.jsonEncode(returnData.setErrorMsg("请输入搜索关键字"), false)}\n\n");
            return Unit.INSTANCE;
         }

         val var43: BooleanRef = new BooleanRef();
         if (StringsKt.startsWith(var22, "=", true)) {
            var43.element = true;
            var22 = StringsKt.replaceFirst$default(var22, "=", "", false, 4, null);
         }

         if (var22 == null || var22.length() == 0) {
            response.write("event: error\n");
            response.end("data: ${ExtKt.jsonEncode(returnData.setErrorMsg("请输入搜索关键字"), false)}\n\n");
            return Unit.INSTANCE;
         }

         if (lastIndex.element >= var42.size() - 1) {
            response.write("event: error\n");
            response.end("data: ${ExtKt.jsonEncode(returnData.setErrorMsg("没有更多了"), false)}\n\n");
            return Unit.INSTANCE;
         }

         searchSize.element = if (searchSize.element > 0) searchSize.element else 50;
         val var24: Int = if (var23 > 0) var23 else 24;
         BookControllerKt.access$getLogger$p()
            .info(
               "searchBookMulti from lastIndex: {} concurrentCount: {} searchSize: {}",
               new Object[]{Boxing.boxInt(lastIndex.element), Boxing.boxInt(if (var23 > 0) var23 else 24), Boxing.boxInt(searchSize.element)}
            );
         val var49: BooleanRef = new BooleanRef();
         context.request().connection().closeHandler(BookController::searchBookMultiSSE$lambda-6);
         val var51: ObjectRef = new ObjectRef();
         var51.element = (T)(new ArrayList());
         val var53: Book = new Book(
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
         var53.setName(var22);
         maxSize = new IntRef();
         maxSize.element = var42.size();
         val bookSourceFile: ObjectRef = new ObjectRef();
         bookSourceFile.element = (T)ExtKt.getStorageFile$default(
            new java.lang.String[]{"data", var34.element as java.lang.String, "bookSource"}, null, 2, null
         );
         if (!(bookSourceFile.element as File).exists()) {
            bookSourceFile.element = (T)ExtKt.getStorageFile$default(new java.lang.String[]{"data", "default", "bookSource"}, null, 2, null);
         }

         val var10002: Int = lastIndex.element + 1;
         val var10003: Int = var42.size();
         val var10004: Function3 = (
            new Function3<CoroutineScope, Integer, Continuation<? super Object>, Object>(
               maxSize, lastIndex, bookSourceFile, bookSourceGroup, this, var53, var43, var34, null
            ) {
               int label;

               {
                  super(3, `$completion`);
                  this.$maxSize = `$maxSize`;
                  this.$lastIndex = `$lastIndex`;
                  this.$bookSourceFile = `$bookSourceFile`;
                  this.$bookSourceGroup = `$bookSourceGroup`;
                  this.this$0 = `$receiver`;
                  this.$book = `$book`;
                  this.$accurate = `$accurate`;
                  this.$userNameSpace = `$userNameSpace`;
               }

               @Nullable
               @Override
               public final Object invokeSuspend(@NotNull Object $result) {
                  val var6: Any = IntrinsicsKt.getCOROUTINE_SUSPENDED();
                  var var10000: Any;
                  switch (this.label) {
                     case 0:
                        ResultKt.throwOnFailure(`$result`);
                        val it: Int = this.I$0;
                        if (this.I$0 > this.$maxSize.element) {
                           return new ArrayList();
                        }

                        this.$lastIndex.element = Math.max(this.$lastIndex.element, this.I$0);
                        val var7: JsonArray = ExtKt.parseJsonStringList$default(
                           this.$bookSourceFile.element,
                           null,
                           null,
                           it,
                           it,
                           null,
                           if (this.$bookSourceGroup.element.length() == 0)
                              null
                              else
                              (
                                 new Function1<ObjectNode, java.lang.Boolean>(this.$bookSourceGroup) {
                                    {
                                       super(1);
                                       this.$bookSourceGroup = `$bookSourceGroup`;
                                    }

                                    public final boolean invoke(@NotNull ObjectNode it) {
                                       val _bookSourceGroup: java.lang.String = it.get("bookSourceGroup").asText();
                                       return _bookSourceGroup != null
                                          && _bookSourceGroup.length() != 0
                                          && StringsKt.indexOf$default(
                                                Intrinsics.stringPlus(_bookSourceGroup, ","),
                                                Intrinsics.stringPlus(this.$bookSourceGroup.element, ","),
                                                0,
                                                false,
                                                6,
                                                null
                                             )
                                             >= 0;
                                    }
                                 }
                              ) as Function1,
                           38,
                           null
                        );
                        if (var7 == null || var7.isEmpty()) {
                           this.$maxSize.element = it;
                           return new ArrayList();
                        }

                        var10000 = this.this$0;
                        val var10: java.lang.String = var7.getString(0);
                        val var10002: Book = this.$book;
                        val var10003: Boolean = this.$accurate.element;
                        val var10004: java.lang.String = this.$userNameSpace.element;
                        val var10005: Continuation = this;
                        this.label = 1;
                        var10000 = (BookController)var10000.searchBookWithSource(var10, var10002, var10003, var10004, var10005);
                        if (var10000 === var6) {
                           return var6;
                        }
                        break;
                     case 1:
                        ResultKt.throwOnFailure(`$result`);
                        var10000 = (BookController)`$result`;
                        break;
                     default:
                        throw new IllegalStateException("call to 'resume' before 'invoke' with coroutine");
                  }

                  return var10000 as ArrayList;
               }

               @Nullable
               public final Object invoke(@NotNull CoroutineScope p1, int p2, @Nullable Continuation<Object> p3) {
                  val var4: Function3 = new <anonymous constructor>(
                     this.$maxSize,
                     this.$lastIndex,
                     this.$bookSourceFile,
                     this.$bookSourceGroup,
                     this.this$0,
                     this.$book,
                     this.$accurate,
                     this.$userNameSpace,
                     p3
                  );
                  var4.I$0 = p2;
                  return var4.invokeSuspend(Unit.INSTANCE);
               }
            }
         ) as Function3;
         val var10005: Function2 = (
            new Function2<ArrayList<Object>, Integer, java.lang.Boolean>(response, lastIndex, var51, var49, this, searchSize) {
               {
                  super(2);
                  this.$response = `$response`;
                  this.$lastIndex = `$lastIndex`;
                  this.$resultList = `$resultList`;
                  this.$isEnd = `$isEnd`;
                  this.this$0 = `$receiver`;
                  this.$searchSize = `$searchSize`;
               }

               public final boolean invoke(@NotNull ArrayList<Object> list, int loopCount) {
                  val loopResult: ArrayList = new ArrayList();
                  val var20: java.lang.Iterable = list;
                  val var5: ObjectRef = this.$resultList;

                  for (Object element$iv : var20) {
                     val bookList: java.util.Collection = `element$iv` as? java.util.Collection;
                     if ((`element$iv` as? java.util.Collection) != null) {
                        val `$this$forEach$iv`: java.lang.Iterable;
                        for (Object element$ivx : $this$forEach$iv) {
                           val book: SearchBook = `element$ivx` as SearchBook;
                           val bookKey: java.lang.String = "${(`element$ivx` as SearchBook).getName()}_${(`element$ivx` as SearchBook).getAuthor()}";
                           (var5.element as ArrayList).add(`element$ivx` as SearchBook);
                           loopResult.add(book);
                        }
                     }
                  }

                  this.$response
                     .write(
                        "data: ${ExtKt.jsonEncode(
                           MapsKt.mapOf(new Pair[]{TuplesKt.to("lastIndex", this.$lastIndex.element), TuplesKt.to("data", loopResult)}), false
                        )}\n\n"
                     );
                  BookControllerKt.access$getLogger$p().info("Loop: {} resultList.size: {}", loopCount, this.$resultList.element.size());
                  return !this.$isEnd.element
                     && loopCount < BookController.access$getConcurrentLoopCount$p(this.this$0)
                     && this.$resultList.element.size() < this.$searchSize.element;
               }
            }
         ) as Function2;
         `$continuation`.L$0 = response;
         `$continuation`.L$1 = lastIndex;
         `$continuation`.L$2 = maxSize;
         `$continuation`.L$3 = null;
         `$continuation`.label = 2;
         if (this.limitConcurrent(var24, var10002, var10003, var10004, var10005, `$continuation`) === var21) {
            return var21;
         }
      }

      response.write("event: end\n");
      response.end(
         "data: ${ExtKt.jsonEncode(
            MapsKt.mapOf(
               new Pair[]{
                  TuplesKt.to("lastIndex", Boxing.boxInt(lastIndex.element)), TuplesKt.to("isEnd", Boxing.boxBoolean(lastIndex.element >= maxSize.element))
               }
            ),
            false
         )}\n\n"
      );
      return Unit.INSTANCE;
   }

   public suspend fun searchBookSource(context: RoutingContext): ReturnData {
      var `$continuation`: Continuation;
      label109: {
         if (`$completion` is <unrepresentable>) {
            `$continuation` = `$completion` as <unrepresentable>;
            if (((`$completion` as <unrepresentable>).label and Integer.MIN_VALUE) != 0) {
               `$continuation`.label -= Integer.MIN_VALUE;
               break label109;
            }
         }

         `$continuation` = new ContinuationImpl(this, `$completion`) {
            Object L$0;
            Object L$1;
            Object L$2;
            Object L$3;
            Object L$4;
            Object L$5;
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
               return this.this$0.searchBookSource(null, this);
            }
         };
      }

      var returnData: ReturnData;
      var lastIndex: IntRef;
      var userNameSpace: ObjectRef;
      var book: ObjectRef;
      var resultList: ObjectRef;
      label112: {
         val `$result`: Any = `$continuation`.result;
         val var20: Any = IntrinsicsKt.getCOROUTINE_SUSPENDED();
         var var10000: Any;
         switch ($continuation.label) {
            case 0:
               ResultKt.throwOnFailure(`$result`);
               returnData = new ReturnData();
               `$continuation`.L$0 = this;
               `$continuation`.L$1 = context;
               `$continuation`.L$2 = returnData;
               `$continuation`.label = 1;
               var10000 = this.checkAuth(context, `$continuation`);
               if (var10000 === var20) {
                  return var20;
               }
               break;
            case 1:
               returnData = `$continuation`.L$2 as ReturnData;
               context = `$continuation`.L$1 as RoutingContext;
               this = `$continuation`.L$0 as BookController;
               ResultKt.throwOnFailure(`$result`);
               var10000 = `$result`;
               break;
            case 2:
               resultList = `$continuation`.L$5 as ObjectRef;
               book = `$continuation`.L$4 as ObjectRef;
               userNameSpace = `$continuation`.L$3 as ObjectRef;
               lastIndex = `$continuation`.L$2 as IntRef;
               returnData = `$continuation`.L$1 as ReturnData;
               this = `$continuation`.L$0 as BookController;
               ResultKt.throwOnFailure(`$result`);
               break label112;
            default:
               throw new IllegalStateException("call to 'resume' before 'invoke' with coroutine");
         }

         if (!var10000 as java.lang.Boolean) {
            return ReturnData.setData$default(returnData, "NEED_LOGIN", null, 2, null).setErrorMsg("请登录后使用");
         }

         lastIndex = new IntRef();
         val searchSize: IntRef = new IntRef();
         val bookSourceGroup: ObjectRef = new ObjectRef();
         val var21: java.lang.String;
         if (context.request().method() === HttpMethod.POST) {
            val var22: java.lang.String = context.getBodyAsJson().getString("url");
            var21 = var22;
            val var23: Int = context.getBodyAsJson().getInteger("lastIndex", Boxing.boxInt(-1));
            lastIndex.element = var23.intValue();
            val var24: Int = context.getBodyAsJson().getInteger("searchSize", Boxing.boxInt(5));
            searchSize.element = var24.intValue();
            val var25: java.lang.String = context.getBodyAsJson().getString("bookSourceGroup", "");
            bookSourceGroup.element = (T)var25;
         } else {
            var urlMap: java.util.List = context.queryParam("url");
            val var26: java.lang.String = CollectionsKt.firstOrNull(urlMap);
            var21 = if (var26 == null) "" else var26;
            urlMap = context.queryParam("lastIndex");
            val var27: java.lang.String = CollectionsKt.firstOrNull(urlMap);
            var var10001: Int;
            if (var27 == null) {
               var10001 = -1;
            } else {
               val var31: Int = Boxing.boxInt(Integer.parseInt(var27));
               var10001 = if (var31 == null) -1 else var31;
            }

            lastIndex.element = var10001;
            urlMap = context.queryParam("searchSize");
            val var28: java.lang.String = CollectionsKt.firstOrNull(urlMap);
            if (var28 == null) {
               var10001 = 5;
            } else {
               val var33: Int = Boxing.boxInt(Integer.parseInt(var28));
               var10001 = if (var33 == null) 5 else var33;
            }

            searchSize.element = var10001;
            urlMap = context.queryParam("bookSourceGroup");
            val var29: java.lang.String = CollectionsKt.firstOrNull(urlMap);
            bookSourceGroup.element = (T)(if (var29 == null) "" else var29);
         }

         userNameSpace = new ObjectRef();
         userNameSpace.element = (T)this.getUserNameSpace(context);
         val var35: java.util.Map = new BookSourceController(this.getCoroutineContext()).getBookSourceMap(userNameSpace.element as java.lang.String);
         if (var35.size() <= 0) {
            return returnData.setErrorMsg("未配置书源");
         }

         if (var21.length() == 0) {
            return returnData.setErrorMsg("请输入书籍链接");
         }

         if (lastIndex.element >= var35.size() - 1) {
            return returnData.setErrorMsg("没有更多了");
         }

         book = new ObjectRef();
         book.element = (T)this.getShelfBookByURL(var21, userNameSpace.element as java.lang.String);
         if (book.element == null) {
            val var39: java.lang.String = this.bookInfoCache.getAsString(var21);
            val var49: Book;
            if (var39 == null) {
               var49 = null;
            } else {
               val var42: java.util.Map = ExtKt.toMap(var39);
               var49 = if (var42 == null)
                  null
                  else
                  ExtKt.getGson()
                     .fromJson(
                        if (var42 is java.lang.String) var42 as java.lang.String else ExtKt.getGson().toJson(var42),
                        new BookController$searchBookSource$$inlined$toDataClass$1().getType()
                     );
            }

            book.element = (T)var49;
         }

         if (book.element == null) {
            return returnData.setErrorMsg("书籍信息错误");
         }

         BookControllerKt.access$getLogger$p().info("searchBookSource from lastIndex: {}", Boxing.boxInt(lastIndex.element));
         val var40: BooleanRef = new BooleanRef();
         context.request().connection().closeHandler(BookController::searchBookSource$lambda-7);
         searchSize.element = if (searchSize.element > 0) searchSize.element else 5;
         resultList = new ObjectRef();
         resultList.element = (T)(new ArrayList());
         val var43: Int = Math.max(searchSize.element * 2, 24);
         val var44: IntRef = new IntRef();
         var44.element = var35.size();
         val bookSourceFile: ObjectRef = new ObjectRef();
         bookSourceFile.element = (T)ExtKt.getStorageFile$default(
            new java.lang.String[]{"data", userNameSpace.element as java.lang.String, "bookSource"}, null, 2, null
         );
         if (!(bookSourceFile.element as File).exists()) {
            bookSourceFile.element = (T)ExtKt.getStorageFile$default(new java.lang.String[]{"data", "default", "bookSource"}, null, 2, null);
         }

         val var10002: Int = lastIndex.element + 1;
         val var10003: Int = var35.size();
         val var10004: Function3 = (
            new Function3<CoroutineScope, Integer, Continuation<? super Object>, Object>(
               var44, lastIndex, bookSourceFile, bookSourceGroup, this, book, userNameSpace, null
            ) {
               int label;

               {
                  super(3, `$completion`);
                  this.$maxSize = `$maxSize`;
                  this.$lastIndex = `$lastIndex`;
                  this.$bookSourceFile = `$bookSourceFile`;
                  this.$bookSourceGroup = `$bookSourceGroup`;
                  this.this$0 = `$receiver`;
                  this.$book = `$book`;
                  this.$userNameSpace = `$userNameSpace`;
               }

               @Nullable
               @Override
               public final Object invokeSuspend(@NotNull Object $result) {
                  val var6: Any = IntrinsicsKt.getCOROUTINE_SUSPENDED();
                  var var10000: Any;
                  switch (this.label) {
                     case 0:
                        ResultKt.throwOnFailure(`$result`);
                        val it: Int = this.I$0;
                        if (this.I$0 > this.$maxSize.element) {
                           return new ArrayList();
                        }

                        this.$lastIndex.element = Math.max(this.$lastIndex.element, this.I$0);
                        val var7: JsonArray = ExtKt.parseJsonStringList$default(
                           this.$bookSourceFile.element,
                           null,
                           null,
                           it,
                           it,
                           null,
                           if (this.$bookSourceGroup.element.length() == 0)
                              null
                              else
                              (
                                 new Function1<ObjectNode, java.lang.Boolean>(this.$bookSourceGroup) {
                                    {
                                       super(1);
                                       this.$bookSourceGroup = `$bookSourceGroup`;
                                    }

                                    public final boolean invoke(@NotNull ObjectNode it) {
                                       val _bookSourceGroup: java.lang.String = it.get("bookSourceGroup").asText();
                                       return _bookSourceGroup != null
                                          && _bookSourceGroup.length() != 0
                                          && StringsKt.indexOf$default(
                                                Intrinsics.stringPlus(_bookSourceGroup, ","),
                                                Intrinsics.stringPlus(this.$bookSourceGroup.element, ","),
                                                0,
                                                false,
                                                6,
                                                null
                                             )
                                             >= 0;
                                    }
                                 }
                              ) as Function1,
                           38,
                           null
                        );
                        if (var7 == null || var7.isEmpty()) {
                           this.$maxSize.element = it;
                           return new ArrayList();
                        }

                        var10000 = this.this$0;
                        val var10: java.lang.String = var7.getString(0);
                        val var10002: Book = this.$book.element;
                        val var10004: java.lang.String = this.$userNameSpace.element;
                        val var10005: Continuation = this;
                        this.label = 1;
                        var10000 = BookController.searchBookWithSource$default((BookController)var10000, var10, var10002, false, var10004, var10005, 4, null);
                        if (var10000 === var6) {
                           return var6;
                        }
                        break;
                     case 1:
                        ResultKt.throwOnFailure(`$result`);
                        var10000 = `$result`;
                        break;
                     default:
                        throw new IllegalStateException("call to 'resume' before 'invoke' with coroutine");
                  }

                  return var10000 as ArrayList;
               }

               @Nullable
               public final Object invoke(@NotNull CoroutineScope p1, int p2, @Nullable Continuation<Object> p3) {
                  val var4: Function3 = new <anonymous constructor>(
                     this.$maxSize, this.$lastIndex, this.$bookSourceFile, this.$bookSourceGroup, this.this$0, this.$book, this.$userNameSpace, p3
                  );
                  var4.I$0 = p2;
                  return var4.invokeSuspend(Unit.INSTANCE);
               }
            }
         ) as Function3;
         val var10005: Function2 = (
            new Function2<ArrayList<Object>, Integer, java.lang.Boolean>(var40, this, resultList, searchSize) {
               {
                  super(2);
                  this.$isEnd = `$isEnd`;
                  this.this$0 = `$receiver`;
                  this.$resultList = `$resultList`;
                  this.$searchSize = `$searchSize`;
               }

               public final boolean invoke(@NotNull ArrayList<Object> list, int loopCount) {
                  val `$this$forEach$iv`: java.lang.Iterable = list;
                  val var4: ObjectRef = this.$resultList;

                  for (Object element$iv : $this$forEach$iv) {
                     val bookList: java.util.Collection = `element$iv` as? java.util.Collection;
                     if ((`element$iv` as? java.util.Collection) != null) {
                        (var4.element as ArrayList).addAll(bookList);
                     }
                  }

                  return !this.$isEnd.element
                     && loopCount < BookController.access$getConcurrentLoopCount$p(this.this$0)
                     && this.$resultList.element.size() < this.$searchSize.element;
               }
            }
         ) as Function2;
         `$continuation`.L$0 = this;
         `$continuation`.L$1 = returnData;
         `$continuation`.L$2 = lastIndex;
         `$continuation`.L$3 = userNameSpace;
         `$continuation`.L$4 = book;
         `$continuation`.L$5 = resultList;
         `$continuation`.label = 2;
         if (this.limitConcurrent(var43, var10002, var10003, var10004, var10005, `$continuation`) === var20) {
            return var20;
         }
      }

      saveBookSources$default(this, book.element as Book, resultList.element as java.util.List, userNameSpace.element as java.lang.String, false, 8, null);
      return ReturnData.setData$default(
         returnData,
         MapsKt.mapOf(new Pair[]{TuplesKt.to("lastIndex", Boxing.boxInt(lastIndex.element)), TuplesKt.to("list", resultList.element)}),
         null,
         2,
         null
      );
   }

   public suspend fun searchBookSourceSSE(context: RoutingContext) {
      var `$continuation`: Continuation;
      label125: {
         if (`$completion` is <unrepresentable>) {
            `$continuation` = `$completion` as <unrepresentable>;
            if (((`$completion` as <unrepresentable>).label and Integer.MIN_VALUE) != 0) {
               `$continuation`.label -= Integer.MIN_VALUE;
               break label125;
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
               return this.this$0.searchBookSourceSSE(null, this);
            }
         };
      }

      var response: HttpServerResponse;
      var lastIndex: IntRef;
      var userNameSpace: ObjectRef;
      var book: ObjectRef;
      var resultList: ObjectRef;
      var maxSize: IntRef;
      label129: {
         val `$result`: Any = `$continuation`.result;
         val var22: Any = IntrinsicsKt.getCOROUTINE_SUSPENDED();
         var returnData: ReturnData;
         var var10000: Any;
         switch ($continuation.label) {
            case 0:
               ResultKt.throwOnFailure(`$result`);
               returnData = new ReturnData();
               response = context.response().putHeader("Content-Type", "text/event-stream").putHeader("Cache-Control", "no-cache").setChunked(true);
               `$continuation`.L$0 = this;
               `$continuation`.L$1 = context;
               `$continuation`.L$2 = returnData;
               `$continuation`.L$3 = response;
               `$continuation`.label = 1;
               var10000 = this.checkAuth(context, `$continuation`);
               if (var10000 === var22) {
                  return var22;
               }
               break;
            case 1:
               response = `$continuation`.L$3 as HttpServerResponse;
               returnData = `$continuation`.L$2 as ReturnData;
               context = `$continuation`.L$1 as RoutingContext;
               this = `$continuation`.L$0 as BookController;
               ResultKt.throwOnFailure(`$result`);
               var10000 = `$result`;
               break;
            case 2:
               maxSize = `$continuation`.L$6 as IntRef;
               resultList = `$continuation`.L$5 as ObjectRef;
               book = `$continuation`.L$4 as ObjectRef;
               userNameSpace = `$continuation`.L$3 as ObjectRef;
               lastIndex = `$continuation`.L$2 as IntRef;
               response = `$continuation`.L$1 as HttpServerResponse;
               this = `$continuation`.L$0 as BookController;
               ResultKt.throwOnFailure(`$result`);
               break label129;
            default:
               throw new IllegalStateException("call to 'resume' before 'invoke' with coroutine");
         }

         if (!var10000 as java.lang.Boolean) {
            response.write("event: error\n");
            response.end("data: ${ExtKt.jsonEncode(ReturnData.setData$default(returnData, "NEED_LOGIN", null, 2, null).setErrorMsg("请登录后使用"), false)}\n\n");
            return Unit.INSTANCE;
         }

         lastIndex = new IntRef();
         val searchSize: IntRef = new IntRef();
         val bookSourceGroup: ObjectRef = new ObjectRef();
         val var23: java.lang.String;
         if (context.request().method() === HttpMethod.POST) {
            val var25: java.lang.String = context.getBodyAsJson().getString("url");
            var23 = var25;
            val var26: Int = context.getBodyAsJson().getInteger("lastIndex", Boxing.boxInt(-1));
            lastIndex.element = var26.intValue();
            val var27: Int = context.getBodyAsJson().getInteger("searchSize", Boxing.boxInt(30));
            searchSize.element = var27.intValue();
            val var28: java.lang.String = context.getBodyAsJson().getString("bookSourceGroup", "");
            bookSourceGroup.element = (T)var28;
            val var29: Int = context.getBodyAsJson().getInteger("refresh", Boxing.boxInt(0));
            val var24: Int = var29.intValue();
         } else {
            var urlMap: java.util.List = context.queryParam("url");
            val var30: java.lang.String = CollectionsKt.firstOrNull(urlMap);
            var23 = if (var30 == null) "" else var30;
            urlMap = context.queryParam("lastIndex");
            val var31: java.lang.String = CollectionsKt.firstOrNull(urlMap);
            var var10001: Int;
            if (var31 == null) {
               var10001 = -1;
            } else {
               val var36: Int = Boxing.boxInt(Integer.parseInt(var31));
               var10001 = if (var36 == null) -1 else var36;
            }

            lastIndex.element = var10001;
            urlMap = context.queryParam("searchSize");
            val var32: java.lang.String = CollectionsKt.firstOrNull(urlMap);
            if (var32 == null) {
               var10001 = 30;
            } else {
               val var38: Int = Boxing.boxInt(Integer.parseInt(var32));
               var10001 = if (var38 == null) 30 else var38;
            }

            searchSize.element = var10001;
            urlMap = context.queryParam("bookSourceGroup");
            val var33: java.lang.String = CollectionsKt.firstOrNull(urlMap);
            bookSourceGroup.element = (T)(if (var33 == null) "" else var33);
            urlMap = context.queryParam("refresh");
            val var34: java.lang.String = CollectionsKt.firstOrNull(urlMap);
            if (var34 != null) {
               val var41: Int = Boxing.boxInt(Integer.parseInt(var34));
               if (var41 != null) {
                  var41;
               }
            }
         }

         userNameSpace = new ObjectRef();
         userNameSpace.element = (T)this.getUserNameSpace(context);
         val var42: java.util.Map = new BookSourceController(this.getCoroutineContext()).getBookSourceMap(userNameSpace.element as java.lang.String);
         if (var42.size() <= 0) {
            response.write("event: error\n");
            response.end("data: ${ExtKt.jsonEncode(returnData.setErrorMsg("未配置书源"), false)}\n\n");
            return Unit.INSTANCE;
         }

         if (var23.length() == 0) {
            response.write("event: error\n");
            response.end("data: ${ExtKt.jsonEncode(returnData.setErrorMsg("请输入书籍链接"), false)}\n\n");
            return Unit.INSTANCE;
         }

         book = new ObjectRef();
         book.element = (T)this.getShelfBookByURL(var23, userNameSpace.element as java.lang.String);
         if (book.element == null) {
            val var48: java.lang.String = this.bookInfoCache.getAsString(var23);
            val var60: Book;
            if (var48 == null) {
               var60 = null;
            } else {
               val concurrentCount: java.util.Map = ExtKt.toMap(var48);
               var60 = if (concurrentCount == null)
                  null
                  else
                  ExtKt.getGson()
                     .fromJson(
                        if (concurrentCount is java.lang.String) concurrentCount as java.lang.String else ExtKt.getGson().toJson(concurrentCount),
                        new BookController$searchBookSourceSSE$$inlined$toDataClass$1().getType()
                     );
            }

            book.element = (T)var60;
         }

         if (book.element == null) {
            response.write("event: error\n");
            response.end("data: ${ExtKt.jsonEncode(returnData.setErrorMsg("书籍信息错误"), false)}\n\n");
            return Unit.INSTANCE;
         }

         if (lastIndex.element >= var42.size() - 1) {
            response.write("event: error\n");
            response.end(
               "data: ${ExtKt.jsonEncode(
                  ReturnData.setData$default(returnData, MapsKt.mapOf(TuplesKt.to("lastIndex", Boxing.boxInt(lastIndex.element))), null, 2, null)
                     .setErrorMsg("没有更多了"),
                  false
               )}\n\n"
            );
            return Unit.INSTANCE;
         }

         searchSize.element = if (searchSize.element > 0) searchSize.element else 30;
         resultList = new ObjectRef();
         resultList.element = (T)(new ArrayList());
         val var50: Int = Math.max(searchSize.element * 2, 24);
         BookControllerKt.access$getLogger$p()
            .info(
               "searchBookMulti from lastIndex: {} concurrentCount: {} searchSize: {}",
               new Object[]{Boxing.boxInt(lastIndex.element), Boxing.boxInt(var50), Boxing.boxInt(searchSize.element)}
            );
         val var51: BooleanRef = new BooleanRef();
         context.request().connection().closeHandler(BookController::searchBookSourceSSE$lambda-8);
         val var52: ObjectRef = new ObjectRef();
         var52.element = (T)ExtKt.getStorageFile$default(new java.lang.String[]{"data", userNameSpace.element as java.lang.String, "bookSource"}, null, 2, null);
         if (!(var52.element as File).exists()) {
            var52.element = (T)ExtKt.getStorageFile$default(new java.lang.String[]{"data", "default", "bookSource"}, null, 2, null);
         }

         maxSize = new IntRef();
         maxSize.element = var42.size();
         val var10002: Int = lastIndex.element + 1;
         val var10003: Int = var42.size();
         val var10004: Function3 = (
            new Function3<CoroutineScope, Integer, Continuation<? super Object>, Object>(
               maxSize, lastIndex, var52, bookSourceGroup, this, book, userNameSpace, null
            ) {
               int label;

               {
                  super(3, `$completion`);
                  this.$maxSize = `$maxSize`;
                  this.$lastIndex = `$lastIndex`;
                  this.$bookSourceFile = `$bookSourceFile`;
                  this.$bookSourceGroup = `$bookSourceGroup`;
                  this.this$0 = `$receiver`;
                  this.$book = `$book`;
                  this.$userNameSpace = `$userNameSpace`;
               }

               @Nullable
               @Override
               public final Object invokeSuspend(@NotNull Object $result) {
                  val var6: Any = IntrinsicsKt.getCOROUTINE_SUSPENDED();
                  var var10000: Any;
                  switch (this.label) {
                     case 0:
                        ResultKt.throwOnFailure(`$result`);
                        val it: Int = this.I$0;
                        if (this.I$0 > this.$maxSize.element) {
                           return new ArrayList();
                        }

                        this.$lastIndex.element = Math.max(this.$lastIndex.element, this.I$0);
                        val var7: JsonArray = ExtKt.parseJsonStringList$default(
                           this.$bookSourceFile.element,
                           null,
                           null,
                           it,
                           it,
                           null,
                           if (this.$bookSourceGroup.element.length() == 0)
                              null
                              else
                              (
                                 new Function1<ObjectNode, java.lang.Boolean>(this.$bookSourceGroup) {
                                    {
                                       super(1);
                                       this.$bookSourceGroup = `$bookSourceGroup`;
                                    }

                                    public final boolean invoke(@NotNull ObjectNode it) {
                                       val _bookSourceGroup: java.lang.String = it.get("bookSourceGroup").asText();
                                       return _bookSourceGroup != null
                                          && _bookSourceGroup.length() != 0
                                          && StringsKt.indexOf$default(
                                                Intrinsics.stringPlus(_bookSourceGroup, ","),
                                                Intrinsics.stringPlus(this.$bookSourceGroup.element, ","),
                                                0,
                                                false,
                                                6,
                                                null
                                             )
                                             >= 0;
                                    }
                                 }
                              ) as Function1,
                           38,
                           null
                        );
                        if (var7 == null || var7.isEmpty()) {
                           this.$maxSize.element = it;
                           return new ArrayList();
                        }

                        var10000 = this.this$0;
                        val var10: java.lang.String = var7.getString(0);
                        val var10002: Book = this.$book.element;
                        val var10004: java.lang.String = this.$userNameSpace.element;
                        val var10005: Continuation = this;
                        this.label = 1;
                        var10000 = BookController.searchBookWithSource$default((BookController)var10000, var10, var10002, false, var10004, var10005, 4, null);
                        if (var10000 === var6) {
                           return var6;
                        }
                        break;
                     case 1:
                        ResultKt.throwOnFailure(`$result`);
                        var10000 = `$result`;
                        break;
                     default:
                        throw new IllegalStateException("call to 'resume' before 'invoke' with coroutine");
                  }

                  return var10000 as ArrayList;
               }

               @Nullable
               public final Object invoke(@NotNull CoroutineScope p1, int p2, @Nullable Continuation<Object> p3) {
                  val var4: Function3 = new <anonymous constructor>(
                     this.$maxSize, this.$lastIndex, this.$bookSourceFile, this.$bookSourceGroup, this.this$0, this.$book, this.$userNameSpace, p3
                  );
                  var4.I$0 = p2;
                  return var4.invokeSuspend(Unit.INSTANCE);
               }
            }
         ) as Function3;
         val var10005: Function2 = (
            new Function2<ArrayList<Object>, Integer, java.lang.Boolean>(response, lastIndex, resultList, var51, this, searchSize) {
               {
                  super(2);
                  this.$response = `$response`;
                  this.$lastIndex = `$lastIndex`;
                  this.$resultList = `$resultList`;
                  this.$isEnd = `$isEnd`;
                  this.this$0 = `$receiver`;
                  this.$searchSize = `$searchSize`;
               }

               public final boolean invoke(@NotNull ArrayList<Object> list, int loopCount) {
                  val loopResult: ArrayList = new ArrayList();
                  val var18: java.lang.Iterable = list;
                  val var5: ObjectRef = this.$resultList;

                  for (Object element$iv : var18) {
                     val bookList: java.util.Collection = `element$iv` as? java.util.Collection;
                     if ((`element$iv` as? java.util.Collection) != null) {
                        (var5.element as ArrayList).addAll(bookList);
                        loopResult.addAll(bookList);
                     }
                  }

                  this.$response
                     .write(
                        "data: ${ExtKt.jsonEncode(
                           MapsKt.mapOf(new Pair[]{TuplesKt.to("lastIndex", this.$lastIndex.element), TuplesKt.to("data", loopResult)}), false
                        )}\n\n"
                     );
                  BookControllerKt.access$getLogger$p().info("Loop: {} resultList.size: {}", loopCount, this.$resultList.element.size());
                  return !this.$isEnd.element
                     && loopCount < BookController.access$getConcurrentLoopCount$p(this.this$0)
                     && this.$resultList.element.size() < this.$searchSize.element;
               }
            }
         ) as Function2;
         `$continuation`.L$0 = this;
         `$continuation`.L$1 = response;
         `$continuation`.L$2 = lastIndex;
         `$continuation`.L$3 = userNameSpace;
         `$continuation`.L$4 = book;
         `$continuation`.L$5 = resultList;
         `$continuation`.L$6 = maxSize;
         `$continuation`.label = 2;
         if (this.limitConcurrent(var50, var10002, var10003, var10004, var10005, `$continuation`) === var22) {
            return var22;
         }
      }

      saveBookSources$default(this, book.element as Book, resultList.element as java.util.List, userNameSpace.element as java.lang.String, false, 8, null);
      response.write("event: end\n");
      response.end(
         "data: ${ExtKt.jsonEncode(
            MapsKt.mapOf(
               new Pair[]{
                  TuplesKt.to("lastIndex", Boxing.boxInt(lastIndex.element)), TuplesKt.to("isEnd", Boxing.boxBoolean(lastIndex.element >= maxSize.element))
               }
            ),
            false
         )}\n\n"
      );
      return Unit.INSTANCE;
   }

   public suspend fun searchBookWithSource(bookSourceString: String, book: Book, accurate: Boolean = ..., userNameSpace: String = ...): ArrayList<SearchBook> {
      var `$continuation`: Continuation;
      label38: {
         if (`$completion` is <unrepresentable>) {
            `$continuation` = `$completion` as <unrepresentable>;
            if (((`$completion` as <unrepresentable>).label and Integer.MIN_VALUE) != 0) {
               `$continuation`.label -= Integer.MIN_VALUE;
               break label38;
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
               return this.this$0.searchBookWithSource(null, null, false, null, this);
            }
         };
      }

      val `$result`: Any = `$continuation`.result;
      val var12: Any = IntrinsicsKt.getCOROUTINE_SUSPENDED();
      var resultList: ObjectRef;
      switch ($continuation.label) {
         case 0:
            ResultKt.throwOnFailure(`$result`);
            resultList = new ObjectRef();
            resultList.element = (T)(new ArrayList());
            val var13: ObjectRef = new ObjectRef();
            val var8: Any = BookSource.Companion.fromJson-IoAF18A(bookSourceString);
            var13.element = (T)(if (Result.isFailure-impl(var8)) null else var8);
            if (var13.element == null) {
               return resultList.element;
            }

            if (this.isInvalidBookSource(var13.element as BookSource, userNameSpace)) {
               return resultList.element;
            }

            val var10000: CoroutineContext = Dispatchers.getIO();
            val var10001: Function2 = (
               new Function2<CoroutineScope, Continuation<? super Unit>, Object>(var13, userNameSpace, book, accurate, resultList, this, null) {
                  long J$0;
                  int label;

                  {
                     super(2, `$completionx`);
                     this.$bookSource = `$bookSource`;
                     this.$userNameSpace = `$userNameSpace`;
                     this.$book = `$book`;
                     this.$accurate = `$accurate`;
                     this.$resultList = `$resultList`;
                     this.this$0 = `$receiver`;
                  }

                  // $VF: Handled exception range with multiple entry points by splitting it
                  // $VF: Duplicated exception handlers to handle obfuscated exceptions
                  @Nullable
                  @Override
                  public final Object invokeSuspend(@NotNull Object $result) {
                     val var13: Any = IntrinsicsKt.getCOROUTINE_SUSPENDED();
                     var e: Long;
                     var var10000: Any;
                     switch (this.label) {
                        case 0:
                           ResultKt.throwOnFailure(`$result`);

                           try {
                              e = System.currentTimeMillis();
                              var10000 = new WebBook(this.$bookSource.element, false, null, this.$userNameSpace, 4, null);
                              val var10001: java.lang.String = this.$book.getName();
                              val var10002: Int = Boxing.boxInt(1);
                              val var10003: Continuation = this;
                              this.J$0 = e;
                              this.label = 1;
                              var10000 = (WebBook)var10000.searchBook(var10001, var10002, var10003);
                           } catch (var15: Exception) {
                              BookController.access$addInvalidBookSource(
                                 this.this$0,
                                 this.$bookSource.element.getBookSourceUrl(),
                                 MapsKt.mutableMapOf(
                                    new Pair[]{
                                       TuplesKt.to("sourceUrl", this.$bookSource.element.getBookSourceUrl()),
                                       TuplesKt.to("time", Boxing.boxLong(System.currentTimeMillis())),
                                       TuplesKt.to("error", var15.toString())
                                    }
                                 ),
                                 this.$userNameSpace
                              );
                              var15.printStackTrace();
                              return Unit.INSTANCE;
                           }

                           if (var10000 === var13) {
                              return var13;
                           }
                           break;
                        case 1:
                           e = this.J$0;

                           try {
                              ResultKt.throwOnFailure(`$result`);
                              var10000 = (WebBook)`$result`;
                              break;
                           } catch (var16: Exception) {
                              BookController.access$addInvalidBookSource(
                                 this.this$0,
                                 this.$bookSource.element.getBookSourceUrl(),
                                 MapsKt.mutableMapOf(
                                    new Pair[]{
                                       TuplesKt.to("sourceUrl", this.$bookSource.element.getBookSourceUrl()),
                                       TuplesKt.to("time", Boxing.boxLong(System.currentTimeMillis())),
                                       TuplesKt.to("error", var16.toString())
                                    }
                                 ),
                                 this.$userNameSpace
                              );
                              var16.printStackTrace();
                              return Unit.INSTANCE;
                           }
                        default:
                           throw new IllegalStateException("call to 'resume' before 'invoke' with coroutine");
                     }

                     try {
                        val var21: java.util.List = var10000 as java.util.List;
                        val end: Long = System.currentTimeMillis();
                        if (var21.size() > 0) {
                           var var7: Int = 0;
                           val var8: Int = var21.size();
                           if (0 < var8) {
                              do {
                                 val _book: SearchBook = var21.get(var7++) as SearchBook;
                                 if (this.$accurate
                                    && _book.getName().equals(this.$book.getName())
                                    && (this.$book.getAuthor().length() == 0 || _book.getAuthor().equals(this.$book.getAuthor()))) {
                                    _book.setTime(end - e);
                                    this.$resultList.element.add(_book);
                                    continue;
                                 } else if (!this.$accurate
                                    && (
                                       StringsKt.indexOf$default(_book.getName(), this.$book.getName(), 0, true, 2, null) >= 0
                                          || StringsKt.indexOf$default(_book.getAuthor(), this.$book.getName(), 0, true, 2, null) >= 0
                                    )) {
                                    _book.setTime(end - e);
                                    this.$resultList.element.add(_book);
                                 }
                              } while (var7 < var8);
                           }
                        }
                     } catch (var14: Exception) {
                        BookController.access$addInvalidBookSource(
                           this.this$0,
                           this.$bookSource.element.getBookSourceUrl(),
                           MapsKt.mutableMapOf(
                              new Pair[]{
                                 TuplesKt.to("sourceUrl", this.$bookSource.element.getBookSourceUrl()),
                                 TuplesKt.to("time", Boxing.boxLong(System.currentTimeMillis())),
                                 TuplesKt.to("error", var14.toString())
                              }
                           ),
                           this.$userNameSpace
                        );
                        var14.printStackTrace();
                     }

                     return Unit.INSTANCE;
                  }

                  @NotNull
                  @Override
                  public final Continuation<Unit> create(@Nullable Object value, @NotNull Continuation<?> $completion) {
                     return new <anonymous constructor>(
                        this.$bookSource, this.$userNameSpace, this.$book, this.$accurate, this.$resultList, this.this$0, `$completion`
                     );
                  }

                  @Nullable
                  public final Object invoke(@NotNull CoroutineScope p1, @Nullable Continuation<? super Unit> p2) {
                     return (this.create(p1, p2) as <unrepresentable>).invokeSuspend(Unit.INSTANCE);
                  }
               }
            ) as Function2;
            `$continuation`.L$0 = resultList;
            `$continuation`.label = 1;
            if (BuildersKt.withContext(var10000, var10001, `$continuation`) === var12) {
               return var12;
            }
            break;
         case 1:
            resultList = `$continuation`.L$0 as ObjectRef;
            ResultKt.throwOnFailure(`$result`);
            break;
         default:
            throw new IllegalStateException("call to 'resume' before 'invoke' with coroutine");
      }

      return resultList.element;
   }

   public suspend fun getAvailableBookSource(context: RoutingContext): ReturnData {
      var `$continuation`: Continuation;
      label91: {
         if (`$completion` is <unrepresentable>) {
            `$continuation` = `$completion` as <unrepresentable>;
            if (((`$completion` as <unrepresentable>).label and Integer.MIN_VALUE) != 0) {
               `$continuation`.label -= Integer.MIN_VALUE;
               break label91;
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
               return this.this$0.getAvailableBookSource(null, this);
            }
         };
      }

      var returnData: ReturnData;
      var userNameSpace: ObjectRef;
      var book: ObjectRef;
      var resultList: ObjectRef;
      label99: {
         val `$result`: Any = `$continuation`.result;
         val var17: Any = IntrinsicsKt.getCOROUTINE_SUSPENDED();
         var var10000: Any;
         switch ($continuation.label) {
            case 0:
               ResultKt.throwOnFailure(`$result`);
               returnData = new ReturnData();
               `$continuation`.L$0 = this;
               `$continuation`.L$1 = context;
               `$continuation`.L$2 = returnData;
               `$continuation`.label = 1;
               var10000 = this.checkAuth(context, `$continuation`);
               if (var10000 === var17) {
                  return var17;
               }
               break;
            case 1:
               returnData = `$continuation`.L$2 as ReturnData;
               context = `$continuation`.L$1 as RoutingContext;
               this = `$continuation`.L$0 as BookController;
               ResultKt.throwOnFailure(`$result`);
               var10000 = `$result`;
               break;
            case 2:
               resultList = `$continuation`.L$4 as ObjectRef;
               book = `$continuation`.L$3 as ObjectRef;
               userNameSpace = `$continuation`.L$2 as ObjectRef;
               returnData = `$continuation`.L$1 as ReturnData;
               this = `$continuation`.L$0 as BookController;
               ResultKt.throwOnFailure(`$result`);
               break label99;
            default:
               throw new IllegalStateException("call to 'resume' before 'invoke' with coroutine");
         }

         if (!var10000 as java.lang.Boolean) {
            return ReturnData.setData$default(returnData, "NEED_LOGIN", null, 2, null).setErrorMsg("请登录后使用");
         }

         val var18: java.lang.String;
         val var19: Int;
         if (context.request().method() === HttpMethod.POST) {
            val var20: java.lang.String = context.getBodyAsJson().getString("url");
            var18 = var20;
            val var21: Int = context.getBodyAsJson().getInteger("refresh", Boxing.boxInt(0));
            var19 = var21.intValue();
         } else {
            val var25: java.util.List = context.queryParam("url");
            val var22: java.lang.String = CollectionsKt.firstOrNull(var25);
            var18 = if (var22 == null) "" else var22;
            val var26: java.util.List = context.queryParam("refresh");
            val var23: java.lang.String = CollectionsKt.firstOrNull(var26);
            val var37: Int;
            if (var23 == null) {
               var37 = 0;
            } else {
               val var27: Int = Boxing.boxInt(Integer.parseInt(var23));
               var37 = if (var27 == null) 0 else var27;
            }

            var19 = var37;
         }

         if (var18.length() == 0) {
            return returnData.setErrorMsg("请输入书籍链接");
         }

         userNameSpace = new ObjectRef();
         userNameSpace.element = (T)this.getUserNameSpace(context);
         book = new ObjectRef();
         book.element = (T)this.getShelfBookByURL(var18, userNameSpace.element as java.lang.String);
         if (book.element == null) {
            val var29: java.lang.String = this.bookInfoCache.getAsString(var18);
            val var10001: Book;
            if (var29 == null) {
               var10001 = null;
            } else {
               val var32: java.util.Map = ExtKt.toMap(var29);
               var10001 = if (var32 == null)
                  null
                  else
                  ExtKt.getGson()
                     .fromJson(
                        if (var32 is java.lang.String) var32 as java.lang.String else ExtKt.getGson().toJson(var32),
                        new BookController$getAvailableBookSource$$inlined$toDataClass$1().getType()
                     );
            }

            book.element = (T)var10001;
         }

         if (book.element == null) {
            return returnData.setErrorMsg("书籍信息错误");
         }

         val var30: ObjectRef = new ObjectRef();
         var30.element = (T)ExtKt.asJsonArray(
            this.getUserStorage(
               userNameSpace.element, new java.lang.String[]{"${(book.element as Book).getName()}_${(book.element as Book).getAuthor()}", "bookSource"}
            )
         );
         if (var30.element == null || (var30.element as JsonArray).size() <= 0) {
            return ReturnData.setData$default(returnData, new ArrayList(), null, 2, null);
         }

         if (var19 <= 0) {
            val var34: java.util.List = (var30.element as JsonArray).getList();
            return ReturnData.setData$default(returnData, var34, null, 2, null);
         }

         resultList = new ObjectRef();
         resultList.element = (T)(new ArrayList());
         val var10003: Int = (var30.element as JsonArray).size();
         val var10004: Function3 = (new Function3<CoroutineScope, Integer, Continuation<? super Object>, Object>(var30, this, userNameSpace, book, null) {
            int label;

            {
               super(3, `$completion`);
               this.$bookSourceList = `$bookSourceList`;
               this.this$0 = `$receiver`;
               this.$userNameSpace = `$userNameSpace`;
               this.$book = `$book`;
            }

            @Nullable
            @Override
            public final Object invokeSuspend(@NotNull Object $result) {
               val var6: Any = IntrinsicsKt.getCOROUTINE_SUSPENDED();
               var var10000: Any;
               switch (this.label) {
                  case 0:
                     ResultKt.throwOnFailure(`$result`);
                     val searchBook: SearchBook = this.$bookSourceList.element.getJsonObject(this.I$0).mapTo(SearchBook.class);
                     if (searchBook.getOrigin().equals("loc_book")) {
                        return CollectionsKt.arrayListOf(new SearchBook[]{searchBook});
                     }

                     val bookSource: java.lang.String = this.this$0.getBookSourceStringBySourceURLOpt(searchBook.getOrigin(), this.$userNameSpace.element);
                     if (bookSource == null) {
                        return new ArrayList();
                     }

                     var10000 = this.this$0;
                     val var10002: Book = this.$book.element;
                     val var10004: java.lang.String = this.$userNameSpace.element;
                     val var10005: Continuation = this;
                     this.label = 1;
                     var10000 = BookController.searchBookWithSource$default((BookController)var10000, bookSource, var10002, false, var10004, var10005, 4, null);
                     if (var10000 === var6) {
                        return var6;
                     }
                     break;
                  case 1:
                     ResultKt.throwOnFailure(`$result`);
                     var10000 = `$result`;
                     break;
                  default:
                     throw new IllegalStateException("call to 'resume' before 'invoke' with coroutine");
               }

               return var10000 as ArrayList;
            }

            @Nullable
            public final Object invoke(@NotNull CoroutineScope p1, int p2, @Nullable Continuation<Object> p3) {
               val var4: Function3 = new <anonymous constructor>(this.$bookSourceList, this.this$0, this.$userNameSpace, this.$book, p3);
               var4.I$0 = p2;
               return var4.invokeSuspend(Unit.INSTANCE);
            }
         }) as Function3;
         val var10005: Function2 = (new Function2<ArrayList<Object>, Integer, java.lang.Boolean>(resultList) {
            {
               super(2);
               this.$resultList = `$resultList`;
            }

            public final boolean invoke(@NotNull ArrayList<Object> list, int $noName_1) {
               val `$this$forEach$iv`: java.lang.Iterable = list;
               val var4: ObjectRef = this.$resultList;

               for (Object element$iv : $this$forEach$iv) {
                  val bookList: java.util.Collection = `element$iv` as? java.util.Collection;
                  if ((`element$iv` as? java.util.Collection) != null) {
                     (var4.element as ArrayList).addAll(bookList);
                  }
               }

               return true;
            }
         }) as Function2;
         `$continuation`.L$0 = this;
         `$continuation`.L$1 = returnData;
         `$continuation`.L$2 = userNameSpace;
         `$continuation`.L$3 = book;
         `$continuation`.L$4 = resultList;
         `$continuation`.label = 2;
         if (this.limitConcurrent(16, 0, var10003, var10004, var10005, `$continuation`) === var17) {
            return var17;
         }
      }

      this.saveBookSources(book.element as Book, resultList.element as MutableList<SearchBook>, userNameSpace.element as java.lang.String, true);
      return ReturnData.setData$default(returnData, resultList.element, null, 2, null);
   }

   public suspend fun getBookshelf(context: RoutingContext): ReturnData {
      var `$continuation`: Continuation;
      label49: {
         if (`$completion` is <unrepresentable>) {
            `$continuation` = `$completion` as <unrepresentable>;
            if (((`$completion` as <unrepresentable>).label and Integer.MIN_VALUE) != 0) {
               `$continuation`.label -= Integer.MIN_VALUE;
               break label49;
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
               return this.this$0.getBookshelf(null, this);
            }
         };
      }

      var returnData: ReturnData;
      var var10000: Any;
      label52: {
         val `$result`: Any = `$continuation`.result;
         val var11: Any = IntrinsicsKt.getCOROUTINE_SUSPENDED();
         switch ($continuation.label) {
            case 0:
               ResultKt.throwOnFailure(`$result`);
               returnData = new ReturnData();
               `$continuation`.L$0 = this;
               `$continuation`.L$1 = context;
               `$continuation`.L$2 = returnData;
               `$continuation`.label = 1;
               var10000 = this.checkAuth(context, `$continuation`);
               if (var10000 === var11) {
                  return var11;
               }
               break;
            case 1:
               returnData = `$continuation`.L$2 as ReturnData;
               context = `$continuation`.L$1 as RoutingContext;
               this = `$continuation`.L$0 as BookController;
               ResultKt.throwOnFailure(`$result`);
               var10000 = `$result`;
               break;
            case 2:
               returnData = `$continuation`.L$0 as ReturnData;
               ResultKt.throwOnFailure(`$result`);
               var10000 = `$result`;
               break label52;
            default:
               throw new IllegalStateException("call to 'resume' before 'invoke' with coroutine");
         }

         if (!var10000 as java.lang.Boolean) {
            return ReturnData.setData$default(returnData, "NEED_LOGIN", null, 2, null).setErrorMsg("请登录后使用");
         }

         val var12: Int;
         if (context.request().method() === HttpMethod.POST) {
            val bookList: Int = context.getBodyAsJson().getInteger("refresh", Boxing.boxInt(0));
            var12 = bookList.intValue();
         } else {
            val var6: java.util.List = context.queryParam("refresh");
            val var13: java.lang.String = CollectionsKt.firstOrNull(var6);
            val var17: Int;
            if (var13 == null) {
               var17 = 0;
            } else {
               val var15: Int = Boxing.boxInt(Integer.parseInt(var13));
               var17 = if (var15 == null) 0 else var15;
            }

            var12 = var17;
         }

         val var10001: Boolean = var12 > 0;
         val var10002: java.lang.String = this.getUserNameSpace(context);
         `$continuation`.L$0 = returnData;
         `$continuation`.L$1 = null;
         `$continuation`.L$2 = null;
         `$continuation`.label = 2;
         var10000 = this.getBookShelfBooks(var10001, var10002, `$continuation`);
         if (var10000 === var11) {
            return var11;
         }
      }

      return ReturnData.setData$default(returnData, var10000 as java.util.List, null, 2, null);
   }

   public suspend fun getShelfBook(context: RoutingContext): ReturnData {
      var `$continuation`: Continuation;
      label47: {
         if (`$completion` is <unrepresentable>) {
            `$continuation` = `$completion` as <unrepresentable>;
            if (((`$completion` as <unrepresentable>).label and Integer.MIN_VALUE) != 0) {
               `$continuation`.label -= Integer.MIN_VALUE;
               break label47;
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
               return this.this$0.getShelfBook(null, this);
            }
         };
      }

      val `$result`: Any = `$continuation`.result;
      val var10: Any = IntrinsicsKt.getCOROUTINE_SUSPENDED();
      var returnData: ReturnData;
      var var10000: Any;
      switch ($continuation.label) {
         case 0:
            ResultKt.throwOnFailure(`$result`);
            returnData = new ReturnData();
            `$continuation`.L$0 = this;
            `$continuation`.L$1 = context;
            `$continuation`.L$2 = returnData;
            `$continuation`.label = 1;
            var10000 = this.checkAuth(context, `$continuation`);
            if (var10000 === var10) {
               return var10;
            }
            break;
         case 1:
            returnData = `$continuation`.L$2 as ReturnData;
            context = `$continuation`.L$1 as RoutingContext;
            this = `$continuation`.L$0 as BookController;
            ResultKt.throwOnFailure(`$result`);
            var10000 = `$result`;
            break;
         default:
            throw new IllegalStateException("call to 'resume' before 'invoke' with coroutine");
      }

      if (!var10000 as java.lang.Boolean) {
         return ReturnData.setData$default(returnData, "NEED_LOGIN", null, 2, null).setErrorMsg("请登录后使用");
      } else {
         val var11: java.lang.String;
         if (context.request().method() === HttpMethod.POST) {
            val book: java.lang.String = context.getBodyAsJson().getString("url");
            var11 = book;
         } else {
            val var6: java.util.List = context.queryParam("url");
            val var12: java.lang.String = CollectionsKt.firstOrNull(var6);
            var11 = if (var12 == null) "" else var12;
         }

         if (var11.length() == 0) {
            return returnData.setErrorMsg("书源链接不能为空");
         } else {
            val var14: Book = this.getShelfBookByURL(var11, this.getUserNameSpace(context));
            return if (var14 == null) returnData.setErrorMsg("书籍不存在") else ReturnData.setData$default(returnData, var14, null, 2, null);
         }
      }
   }

   public suspend fun saveBook(context: RoutingContext): ReturnData {
      var `$continuation`: Continuation;
      label98: {
         if (`$completion` is <unrepresentable>) {
            `$continuation` = `$completion` as <unrepresentable>;
            if (((`$completion` as <unrepresentable>).label and Integer.MIN_VALUE) != 0) {
               `$continuation`.label -= Integer.MIN_VALUE;
               break label98;
            }
         }

         `$continuation` = new ContinuationImpl(this, `$completion`) {
            Object L$0;
            Object L$1;
            Object L$2;
            Object L$3;
            Object L$4;
            Object L$5;
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
               return this.this$0.saveBook(null, this);
            }
         };
      }

      var returnData: ReturnData;
      var book: Book;
      var userNameSpace: java.lang.String;
      label87: {
         var result: java.lang.String;
         var var12: Any;
         label113: {
            var var10000: Any;
            label84: {
               label106: {
                  val `$result`: Any = `$continuation`.result;
                  var12 = IntrinsicsKt.getCOROUTINE_SUSPENDED();
                  switch ($continuation.label) {
                     case 0:
                        ResultKt.throwOnFailure(`$result`);
                        returnData = new ReturnData();
                        `$continuation`.L$0 = this;
                        `$continuation`.L$1 = context;
                        `$continuation`.L$2 = returnData;
                        `$continuation`.label = 1;
                        var10000 = this.checkAuth(context, `$continuation`);
                        if (var10000 === var12) {
                           return var12;
                        }
                        break;
                     case 1:
                        returnData = `$continuation`.L$2 as ReturnData;
                        context = `$continuation`.L$1 as RoutingContext;
                        this = `$continuation`.L$0 as BookController;
                        ResultKt.throwOnFailure(`$result`);
                        var10000 = `$result`;
                        break;
                     case 2:
                        result = `$continuation`.L$5 as java.lang.String;
                        userNameSpace = `$continuation`.L$4 as java.lang.String;
                        book = `$continuation`.L$3 as Book;
                        returnData = `$continuation`.L$2 as ReturnData;
                        context = `$continuation`.L$1 as RoutingContext;
                        this = `$continuation`.L$0 as BookController;
                        ResultKt.throwOnFailure(`$result`);
                        break label106;
                     case 3:
                        result = `$continuation`.L$4 as java.lang.String;
                        userNameSpace = `$continuation`.L$3 as java.lang.String;
                        returnData = `$continuation`.L$2 as ReturnData;
                        context = `$continuation`.L$1 as RoutingContext;
                        this = `$continuation`.L$0 as BookController;
                        ResultKt.throwOnFailure(`$result`);
                        var10000 = `$result`;
                        break label84;
                     case 4:
                        userNameSpace = `$continuation`.L$4 as java.lang.String;
                        book = `$continuation`.L$3 as Book;
                        returnData = `$continuation`.L$2 as ReturnData;
                        context = `$continuation`.L$1 as RoutingContext;
                        this = `$continuation`.L$0 as BookController;
                        ResultKt.throwOnFailure(`$result`);
                        break label87;
                     case 5:
                        userNameSpace = `$continuation`.L$4 as java.lang.String;
                        book = `$continuation`.L$3 as Book;
                        returnData = `$continuation`.L$2 as ReturnData;
                        context = `$continuation`.L$1 as RoutingContext;
                        this = `$continuation`.L$0 as BookController;
                        ResultKt.throwOnFailure(`$result`);
                        break label87;
                     default:
                        throw new IllegalStateException("call to 'resume' before 'invoke' with coroutine");
                  }

                  if (!var10000 as java.lang.Boolean) {
                     return ReturnData.setData$default(returnData, "NEED_LOGIN", null, 2, null).setErrorMsg("请登录后使用");
                  }

                  book = context.getBodyAsJson().mapTo(Book.class);
                  userNameSpace = this.getUserNameSpace(context);
                  if (book.isLocalBook()) {
                     `$continuation`.L$0 = this;
                     `$continuation`.L$1 = context;
                     `$continuation`.L$2 = returnData;
                     `$continuation`.L$3 = book;
                     `$continuation`.L$4 = userNameSpace;
                     `$continuation`.label = 5;
                     if (this.saveLocalBookCover(book, userNameSpace, `$continuation`) === var12) {
                        return var12;
                     }
                     break label87;
                  }

                  result = this.getBookSourceStringBySourceURLOpt(book.getOrigin(), userNameSpace);
                  if (result == null) {
                     return returnData.setErrorMsg("书源信息错误");
                  }

                  val var7: java.lang.CharSequence = book.getTocUrl();
                  if (var7 != null && var7.length() != 0) {
                     break label113;
                  }

                  var10000 = new WebBook(result, this.getAppConfig().getDebugLog(), null, userNameSpace, 4, null);
                  `$continuation`.L$0 = this;
                  `$continuation`.L$1 = context;
                  `$continuation`.L$2 = returnData;
                  `$continuation`.L$3 = book;
                  `$continuation`.L$4 = userNameSpace;
                  `$continuation`.L$5 = result;
                  `$continuation`.label = 2;
                  if (WebBook.getBookInfo$default((WebBook)var10000, book, false, `$continuation`, 2, null) === var12) {
                     return var12;
                  }
               }

               `$continuation`.L$0 = this;
               `$continuation`.L$1 = context;
               `$continuation`.L$2 = returnData;
               `$continuation`.L$3 = userNameSpace;
               `$continuation`.L$4 = result;
               `$continuation`.L$5 = null;
               `$continuation`.label = 3;
               var10000 = this.mergeBookCacheInfo(book, `$continuation`);
               if (var10000 === var12) {
                  return var12;
               }
            }

            book = var10000 as Book;
         }

         `$continuation`.L$0 = this;
         `$continuation`.L$1 = context;
         `$continuation`.L$2 = returnData;
         `$continuation`.L$3 = book;
         `$continuation`.L$4 = userNameSpace;
         `$continuation`.label = 4;
         if (this.saveBookCover(book, userNameSpace, result, `$continuation`) === var12) {
            return var12;
         }
      }

      val var13: Pair = this.saveBookToShelf(book, userNameSpace, context);
      if (var13.getSecond() != null) {
         val var14: java.lang.String = var13.getSecond() as java.lang.String;
         return returnData.setErrorMsg(if (var14 == null) "" else var14);
      } else {
         return ReturnData.setData$default(returnData, var13.getFirst(), null, 2, null);
      }
   }

   public fun saveBookToShelf(_book: Book, userNameSpace: String, context: RoutingContext): Pair<Book, String?> {
      val book: Book = _book;
      var bookshelf: java.lang.CharSequence = _book.getOrigin();
      if (bookshelf == null || bookshelf.length() == 0) {
         return new Pair<>(_book, "未找到书源信息");
      } else {
         bookshelf = _book.getBookUrl();
         if (bookshelf == null || bookshelf.length() == 0) {
            return new Pair<>(_book, "书籍链接不能为空");
         } else {
            var var13: JsonArray = ExtKt.asJsonArray(this.getUserStorage(userNameSpace, new java.lang.String[]{"bookshelf"}));
            if (var13 == null) {
               var13 = new JsonArray();
            }

            var var16: Int = -1;
            var var18: Int = 0;
            val existBook: Int = var13.size();
            if (0 < existBook) {
               do {
                  val cachePath: Int = var18++;
                  if (var13.getJsonObject(cachePath).getString("name", "").equals(book.getName())
                     && var13.getJsonObject(cachePath).getString("author", "").equals(book.getAuthor())) {
                     var16 = cachePath;
                     break;
                  }
               } while (var18 < existBook);
            }

            if (var16 < 0) {
               val var19: User = context.get("userInfo");
               if (var19 != null && var13.size() >= var19.getBook_limit()) {
                  return new Pair<>(book, "你已达到书籍数上限，请联系管理员");
               }
            }

            if (book.isLocalBook()) {
               if (StringsKt.startsWith$default(book.getBookUrl(), "/assets/", false, 2, null)
                  || StringsKt.startsWith$default(book.getBookUrl(), "assets/", false, 2, null)) {
                  val var22: File = new File(ExtKt.getWorkDir(Intrinsics.stringPlus("storage", book.getBookUrl())));
                  if (!var22.exists()) {
                     return new Pair<>(book, "上传书籍不存在");
                  }

                  val var27: java.lang.String = Paths.get("storage", "data", userNameSpace, "${book.getName()}_${book.getAuthor()}", var22.getName())
                     .toString();
                  val var30: java.lang.String = "storage/data/$userNameSpace/${book.getName()}_${book.getAuthor()}/${var22.getName()}";
                  val var33: java.lang.String = ExtKt.getWorkDir(var27);
                  BookControllerKt.access$getLogger$p().info("localFilePath: {}", var33);
                  val var36: File = new File(var33);
                  ExtKt.deleteRecursively(var36);
                  if (!var36.getParentFile().exists()) {
                     var36.getParentFile().mkdirs();
                  }

                  if (!FilesKt.copyRecursively$default(var22, var36, false, null, 6, null)) {
                     return new Pair<>(book, "导入本地书籍失败");
                  }

                  ExtKt.deleteRecursively(var22);
                  book.setBookUrl(var30);
                  book.setOriginName(var27);
                  if (book.isEpub()) {
                     if (!extractEpub$default(this, book, false, 2, null)) {
                        return new Pair<>(book, "导入本地Epub书籍失败");
                     }
                  } else if (book.isCbz()) {
                     if (!extractCbz$default(this, book, false, 2, null)) {
                        return new Pair<>(book, "导入本地CBZ书籍失败");
                     }
                  } else if (book.isPdf() && !convertPdfToImage$default(this, book, false, 2, null)) {
                     return new Pair<>(book, "本地PDF书籍转换失败");
                  }
               } else if (StringsKt.indexOf$default(book.getBookUrl(), "localStore", 0, false, 6, null) >= 0) {
                  val tempFilex: File = new File(ExtKt.getWorkDir(book.getBookUrl()));
                  if (!tempFilex.exists()) {
                     return new Pair<>(book, "本地书仓书籍不存在");
                  }

                  book.setBookUrl("storage/data/$userNameSpace/${book.getName()}_${book.getAuthor()}/${tempFilex.getName()}");
                  if (book.isEpub()) {
                     if (!extractEpub$default(this, book, false, 2, null)) {
                        return new Pair<>(book, "导入本地Epub书籍失败");
                     }
                  } else if (book.isCbz()) {
                     if (!extractCbz$default(this, book, false, 2, null)) {
                        return new Pair<>(book, "导入本地CBZ书籍失败");
                     }
                  } else if (book.isPdf() && !convertPdfToImage$default(this, book, false, 2, null)) {
                     return new Pair<>(book, "本地PDF书籍转换失败");
                  }
               } else if (StringsKt.indexOf$default(book.getBookUrl(), "webdav", 0, false, 6, null) >= 0) {
                  val tempFilexx: File = new File(ExtKt.getWorkDir(book.getBookUrl()));
                  if (!tempFilexx.exists()) {
                     return new Pair<>(book, "webdav书仓书籍不存在");
                  }

                  book.setBookUrl("storage/data/$userNameSpace/${book.getName()}_${book.getAuthor()}/${tempFilexx.getName()}");
                  if (book.isEpub()) {
                     if (!extractEpub$default(this, book, false, 2, null)) {
                        return new Pair<>(book, "导入本地Epub书籍失败");
                     }
                  } else if (book.isCbz()) {
                     if (!extractCbz$default(this, book, false, 2, null)) {
                        return new Pair<>(book, "导入本地CBZ书籍失败");
                     }
                  } else if (book.isPdf() && !convertPdfToImage$default(this, book, false, 2, null)) {
                     return new Pair<>(book, "本地PDF书籍转换失败");
                  }
               }
            }

            book.setInShelf(true);
            if (var16 >= 0) {
               val var23: java.util.List = var13.getList();
               val var28: Book = var13.getJsonObject(var16).mapTo(Book.class);
               book.setDurChapterIndex(var28.getDurChapterIndex());
               book.setDurChapterTitle(var28.getDurChapterTitle());
               book.setDurChapterTime(var28.getDurChapterTime());
               val var31: java.lang.CharSequence = var28.getDisplayCover();
               if (var31 != null && var31.length() != 0) {
                  var var10000: java.lang.String = var28.getDisplayCover();
                  if (StringsKt.startsWith$default(var10000, "/", false, 2, null)) {
                     var10000 = var28.getDisplayCover();
                     if (!var10000.equals(book.getDisplayCover())) {
                        val var35: Array<java.lang.String> = new java.lang.String[]{"storage", null};
                        val var10002: java.lang.String = var28.getDisplayCover();
                        var35[1] = var10002;
                        FileUtils.INSTANCE.deleteFile(ExtKt.getWorkDir(var35));
                     }
                  }
               }

               var23.set(var16, JsonObject.mapFrom(book));
               var13 = new JsonArray(var23);
            } else {
               var13.add(JsonObject.mapFrom(book));
            }

            saveBookSources$default(this, book, CollectionsKt.listOf(book.toSearchBook()), userNameSpace, false, 8, null);
            this.saveUserStorage(userNameSpace, "bookshelf", var13);
            return new Pair<>(book, null);
         }
      }
   }

   private suspend fun saveLocalBookCover(book: Book, userNameSpace: String) {
      var `$continuation`: Continuation;
      label41: {
         if (`$completion` is <unrepresentable>) {
            `$continuation` = `$completion` as <unrepresentable>;
            if (((`$completion` as <unrepresentable>).label and Integer.MIN_VALUE) != 0) {
               `$continuation`.label -= Integer.MIN_VALUE;
               break label41;
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
               return BookController.access$saveLocalBookCover(this.this$0, null, null, this);
            }
         };
      }

      val `$result`: Any = `$continuation`.result;
      val var15: Any = IntrinsicsKt.getCOROUTINE_SUSPENDED();
      var cachedCoverUrl: java.lang.String;
      var cacheFile: File;
      var var10000: Any;
      switch ($continuation.label) {
         case 0:
            ResultKt.throwOnFailure(`$result`);
            val coverUrl: java.lang.String = book.getDisplayCover();
            if (coverUrl == null || StringsKt.startsWith$default(coverUrl, "/", false, 2, null)) {
               return Unit.INSTANCE;
            }

            val ext: java.lang.String = this.getFileExt(coverUrl, "jpg");
            val md5Encode: java.lang.String = MD5Utils.INSTANCE.md5Encode(coverUrl).toString();
            val cachePath: java.lang.String = ExtKt.getWorkDir("storage", "assets", userNameSpace, "covers", "$md5Encode.$ext");
            cachedCoverUrl = "/assets/$userNameSpace/covers/$md5Encode.$ext";
            cacheFile = new File(cachePath);
            if (cacheFile.exists()) {
               book.setCoverUrl(cachedCoverUrl);
               return Unit.INSTANCE;
            }

            var10000 = (new Function1<Handler<AsyncResult<HttpResponse<Buffer>>>, Unit>(this, coverUrl) {
               {
                  super(1);
                  this.this$0 = `$receiver`;
                  this.$coverUrl = `$coverUrl`;
               }

               public final void invoke(@NotNull Handler<AsyncResult<HttpResponse<Buffer>>> handler) {
                  BookController.access$getWebClient$p(this.this$0).getAbs(this.$coverUrl).timeout(3000L).send(handler);
               }
            }) as Function1;
            `$continuation`.L$0 = book;
            `$continuation`.L$1 = cachedCoverUrl;
            `$continuation`.L$2 = cacheFile;
            `$continuation`.label = 1;
            var10000 = VertxCoroutineKt.awaitResult((Function1)var10000, `$continuation`);
            if (var10000 === var15) {
               return var15;
            }
            break;
         case 1:
            cacheFile = `$continuation`.L$2 as File;
            cachedCoverUrl = `$continuation`.L$1 as java.lang.String;
            book = `$continuation`.L$0 as Book;
            ResultKt.throwOnFailure(`$result`);
            var10000 = `$result`;
            break;
         default:
            throw new IllegalStateException("call to 'resume' before 'invoke' with coroutine");
      }

      val var12: Buffer = (var10000 as HttpResponse).bodyAsBuffer();
      val bodyBytes: ByteArray = if (var12 == null) null else var12.getBytes();
      if (bodyBytes != null) {
         FilesKt.writeBytes(cacheFile, bodyBytes);
         book.setCoverUrl(cachedCoverUrl);
      }

      return Unit.INSTANCE;
   }

    /**
     * MANUALLY RECONSTRUCTED from CFR/Procyon + BookController.class (reader-pro-3.2.14.jar)
     * Original Vineflower output: Couldn't be decompiled
     */
    public suspend fun saveBookCover(book: Book, userNameSpace: String, bookSource: String? = null) {
        val coverUrl = book.displayCover
        if (coverUrl == null || coverUrl.startsWith("/")) {
            return
        }
        val sourceStr = bookSource ?: getBookSourceStringBySourceURLOpt(book.origin, userNameSpace)
        val ext = getFileExt(coverUrl, "jpg")
        val md5Encode = MD5Utils.md5Encode(coverUrl).toString()
        val cachePath = getWorkDir("storage", "assets", userNameSpace, "covers", "$md5Encode.$ext")
        val cachedCoverUrl = "/assets/$userNameSpace/covers/$md5Encode.$ext"
        val cacheFile = File(cachePath)
        if (cacheFile.exists()) {
            book.coverUrl = cachedCoverUrl
            return
        }
        try {
            requireNotNull(sourceStr)
            val source = BookSource.fromJson(sourceStr).getOrNull()
            val analyzeUrl = AnalyzeUrl(mUrl = coverUrl, source = source)
            val bytes = analyzeUrl.getByteArrayAwait()
            FileUtils.writeBytes(cachePath, bytes)
            book.coverUrl = cachedCoverUrl
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

   public suspend fun setBookSource(context: RoutingContext): ReturnData {
      var `$continuation`: Continuation;
      label185: {
         if (`$completion` is <unrepresentable>) {
            `$continuation` = `$completion` as <unrepresentable>;
            if (((`$completion` as <unrepresentable>).label and Integer.MIN_VALUE) != 0) {
               `$continuation`.label -= Integer.MIN_VALUE;
               break label185;
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
               return this.this$0.setBookSource(null, this);
            }
         };
      }

      var returnData: ReturnData;
      var newBookInfo: ObjectRef;
      label213: {
         var userNameSpace: java.lang.String;
         var bookSourceString: java.lang.String;
         var var20: Any;
         label171: {
            var book: Book;
            var var54: ObjectRef;
            var var10001: Book;
            label170: {
               var var16: ObjectRef;
               label204: {
                  val `$result`: Any = `$continuation`.result;
                  var20 = IntrinsicsKt.getCOROUTINE_SUSPENDED();
                  var var53: Any;
                  switch ($continuation.label) {
                     case 0:
                        ResultKt.throwOnFailure(`$result`);
                        returnData = new ReturnData();
                        `$continuation`.L$0 = this;
                        `$continuation`.L$1 = context;
                        `$continuation`.L$2 = returnData;
                        `$continuation`.label = 1;
                        var53 = this.checkAuth(context, `$continuation`);
                        if (var53 === var20) {
                           return var20;
                        }
                        break;
                     case 1:
                        returnData = `$continuation`.L$2 as ReturnData;
                        context = `$continuation`.L$1 as RoutingContext;
                        this = `$continuation`.L$0 as BookController;
                        ResultKt.throwOnFailure(`$result`);
                        var53 = `$result`;
                        break;
                     case 2:
                        var16 = `$continuation`.L$6 as ObjectRef;
                        newBookInfo = `$continuation`.L$5 as ObjectRef;
                        bookSourceString = `$continuation`.L$4 as java.lang.String;
                        book = `$continuation`.L$3 as Book;
                        userNameSpace = `$continuation`.L$2 as java.lang.String;
                        returnData = `$continuation`.L$1 as ReturnData;
                        this = `$continuation`.L$0 as BookController;
                        ResultKt.throwOnFailure(`$result`);
                        var10000 = `$result`;
                        break label204;
                     case 3:
                        newBookInfo = `$continuation`.L$4 as ObjectRef;
                        bookSourceString = `$continuation`.L$3 as java.lang.String;
                        userNameSpace = `$continuation`.L$2 as java.lang.String;
                        returnData = `$continuation`.L$1 as ReturnData;
                        this = `$continuation`.L$0 as BookController;
                        ResultKt.throwOnFailure(`$result`);
                        break label171;
                     case 4:
                        newBookInfo = `$continuation`.L$1 as ObjectRef;
                        returnData = `$continuation`.L$0 as ReturnData;

                        try {
                           ResultKt.throwOnFailure(`$result`);
                           break label213;
                        } catch (var23: Exception) {
                           return ReturnData.setData$default(`$continuation`.L$0 as ReturnData, newBookInfo.element, null, 2, null);
                        }
                     default:
                        throw new IllegalStateException("call to 'resume' before 'invoke' with coroutine");
                  }

                  if (!var53 as java.lang.Boolean) {
                     return ReturnData.setData$default(returnData, "NEED_LOGIN", null, 2, null).setErrorMsg("请登录后使用");
                  }

                  val var24: java.lang.String;
                  val var25: java.lang.String;
                  val var26: java.lang.String;
                  if (context.request().method() === HttpMethod.POST) {
                     userNameSpace = context.getBodyAsJson().getString("bookUrl");
                     var24 = userNameSpace;
                     userNameSpace = context.getBodyAsJson().getString("newUrl");
                     var25 = userNameSpace;
                     userNameSpace = context.getBodyAsJson().getString("bookSourceUrl");
                     var26 = userNameSpace;
                  } else {
                     val var36: java.util.List = context.queryParam("bookUrl");
                     userNameSpace = CollectionsKt.firstOrNull(var36);
                     var24 = if (userNameSpace == null) "" else userNameSpace;
                     val var37: java.util.List = context.queryParam("newUrl");
                     userNameSpace = CollectionsKt.firstOrNull(var37);
                     var25 = if (userNameSpace == null) "" else userNameSpace;
                     val var38: java.util.List = context.queryParam("bookSourceUrl");
                     userNameSpace = CollectionsKt.firstOrNull(var38);
                     var26 = if (userNameSpace == null) "" else userNameSpace;
                  }

                  if (var24.length() == 0) {
                     return returnData.setErrorMsg("书籍链接不能为空");
                  }

                  if (var25.length() == 0) {
                     return returnData.setErrorMsg("新源书籍链接不能为空");
                  }

                  if (var26.length() == 0) {
                     return returnData.setErrorMsg("书源链接不能为空");
                  }

                  userNameSpace = this.getUserNameSpace(context);
                  book = this.getShelfBookByURL(var24, userNameSpace);
                  if (book == null) {
                     return returnData.setErrorMsg("书籍信息错误");
                  }

                  bookSourceString = this.getBookSourceStringBySourceURLOpt(var26, userNameSpace);
                  var searchBook: Book = null;
                  if (bookSourceString == null || bookSourceString.length() == 0) {
                     val var46: JsonArray = ExtKt.asJsonArray(
                        this.getUserStorage(userNameSpace, new java.lang.String[]{"${book.getName()}_${book.getAuthor()}", "bookSource"})
                     );
                     if (var46 != null) {
                        var var48: Int = 0;
                        val var50: Int = var46.size();
                        if (0 < var50) {
                           do {
                              val _searchBook: SearchBook = var46.getJsonObject(var48++).mapTo(SearchBook.class);
                              if (_searchBook.getBookUrl().equals(var25)) {
                                 searchBook = _searchBook.toBook();
                                 break;
                              }
                           } while (var48 < var50);
                        }
                     }

                     if (searchBook == null) {
                        return returnData.setErrorMsg("书源信息错误");
                     }
                  }

                  newBookInfo = new ObjectRef();
                  var54 = newBookInfo;
                  if (searchBook != null) {
                     var10001 = searchBook;
                     break label170;
                  }

                  if (bookSourceString == null || bookSourceString.length() == 0) {
                     return returnData.setErrorMsg("书源信息错误");
                  }

                  var16 = newBookInfo;
                  var53 = new WebBook(bookSourceString, this.getAppConfig().getDebugLog(), null, userNameSpace, 4, null);
                  `$continuation`.L$0 = this;
                  `$continuation`.L$1 = returnData;
                  `$continuation`.L$2 = userNameSpace;
                  `$continuation`.L$3 = book;
                  `$continuation`.L$4 = bookSourceString;
                  `$continuation`.L$5 = newBookInfo;
                  `$continuation`.L$6 = newBookInfo;
                  `$continuation`.label = 2;
                  var10000 = WebBook.getBookInfo$default((WebBook)var53, var25, false, `$continuation`, 2, null);
                  if (var10000 === var20) {
                     return var20;
                  }
               }

               var54 = var16;
               var10001 = var10000 as Book;
            }

            var54.element = (T)var10001;
            val var10003: Function1 = (new Function1<Book, Book>(newBookInfo) {
               {
                  super(1);
                  this.$newBookInfo = `$newBookInfo`;
               }

               @NotNull
               public final Book invoke(@NotNull Book existBook) {
                  existBook.setOrigin(this.$newBookInfo.element.getOrigin());
                  existBook.setOriginName(this.$newBookInfo.element.getOriginName());
                  existBook.setBookUrl(this.$newBookInfo.element.getBookUrl());
                  existBook.setTocUrl(this.$newBookInfo.element.getTocUrl());
                  existBook.setInShelf(true);
                  var var2: java.lang.CharSequence = existBook.getCoverUrl();
                  if (var2 == null || var2.length() == 0) {
                     var2 = this.$newBookInfo.element.getCoverUrl();
                     if (var2 != null && var2.length() != 0) {
                        existBook.setCoverUrl(this.$newBookInfo.element.getCoverUrl());
                     }
                  }

                  BookControllerKt.access$getLogger$p().info("setBookSource: {}", existBook);
                  this.$newBookInfo.element = existBook;
                  return existBook;
               }
            }) as Function1;
            `$continuation`.L$0 = this;
            `$continuation`.L$1 = returnData;
            `$continuation`.L$2 = userNameSpace;
            `$continuation`.L$3 = bookSourceString;
            `$continuation`.L$4 = newBookInfo;
            `$continuation`.L$5 = null;
            `$continuation`.L$6 = null;
            `$continuation`.label = 3;
            if (this.editShelfBook(book, userNameSpace, var10003, `$continuation`) === var20) {
               return var20;
            }
         }

         var var56: Any;
         try {
            val var57: Book = newBookInfo.element as Book;
            val var10002: java.lang.String = if (bookSourceString == null) "" else bookSourceString;
            `$continuation`.L$0 = returnData;
            `$continuation`.L$1 = newBookInfo;
            `$continuation`.L$2 = null;
            `$continuation`.L$3 = null;
            `$continuation`.L$4 = null;
            `$continuation`.label = 4;
            var56 = getLocalChapterList$default(this, var57, var10002, true, userNameSpace, false, null, `$continuation`, 48, null);
         } catch (var22: Exception) {
            return ReturnData.setData$default(returnData, newBookInfo.element, null, 2, null);
         }

         if (var56 === var20) {
            return var20;
         }
      }

      try {
         ;
      } catch (var21: Exception) {
      }

      return ReturnData.setData$default(returnData, newBookInfo.element, null, 2, null);
   }

   public suspend fun saveBookConfig(context: RoutingContext): ReturnData {
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
               return this.this$0.saveBookConfig(null, this);
            }
         };
      }

      var returnData: ReturnData;
      var book: Book;
      var var10000: Any;
      label78: {
         val `$result`: Any = `$continuation`.result;
         val var12: Any = IntrinsicsKt.getCOROUTINE_SUSPENDED();
         switch ($continuation.label) {
            case 0:
               ResultKt.throwOnFailure(`$result`);
               returnData = new ReturnData();
               `$continuation`.L$0 = this;
               `$continuation`.L$1 = context;
               `$continuation`.L$2 = returnData;
               `$continuation`.label = 1;
               var10000 = this.checkAuth(context, `$continuation`);
               if (var10000 === var12) {
                  return var12;
               }
               break;
            case 1:
               returnData = `$continuation`.L$2 as ReturnData;
               context = `$continuation`.L$1 as RoutingContext;
               this = `$continuation`.L$0 as BookController;
               ResultKt.throwOnFailure(`$result`);
               var10000 = `$result`;
               break;
            case 2:
               book = `$continuation`.L$1 as Book;
               returnData = `$continuation`.L$0 as ReturnData;
               ResultKt.throwOnFailure(`$result`);
               var10000 = `$result`;
               break label78;
            default:
               throw new IllegalStateException("call to 'resume' before 'invoke' with coroutine");
         }

         if (!var10000 as java.lang.Boolean) {
            return ReturnData.setData$default(returnData, "NEED_LOGIN", null, 2, null).setErrorMsg("请登录后使用");
         }

         val pdfImageWidth: FloatRef = new FloatRef();
         val var13: java.lang.String;
         if (context.request().method() === HttpMethod.POST) {
            val userNameSpace: java.lang.String = context.getBodyAsJson().getString("bookUrl");
            var13 = userNameSpace;
            val var14: java.lang.Float = context.getBodyAsJson().getFloat("pdfImageWidth", Boxing.boxFloat(0.0F));
            pdfImageWidth.element = var14.floatValue();
         } else {
            val var19: java.util.List = context.queryParam("bookUrl");
            var var15: java.lang.String = CollectionsKt.firstOrNull(var19);
            var13 = if (var15 == null) "" else var15;
            val var20: java.util.List = context.queryParam("pdfImageWidth");
            var15 = CollectionsKt.firstOrNull(var20);
            val var10001: Float;
            if (var15 == null) {
               var10001 = 0.0F;
            } else {
               val var21: java.lang.Float = Boxing.boxFloat(java.lang.Float.parseFloat(var15));
               var10001 = if (var21 == null) 0.0F else var21;
            }

            pdfImageWidth.element = var10001;
         }

         if (var13.length() == 0) {
            return returnData.setErrorMsg("书籍链接不能为空");
         }

         val var18: java.lang.String = this.getUserNameSpace(context);
         book = this.getShelfBookByURL(var13, var18);
         if (book == null) {
            return returnData.setErrorMsg("书籍信息错误");
         }

         if (pdfImageWidth.element <= 0.0F) {
            return returnData.setErrorMsg("pdf图片宽度错误");
         }

         val var10003: Function1 = (new Function1<Book, Book>(pdfImageWidth) {
            {
               super(1);
               this.$pdfImageWidth = `$pdfImageWidth`;
            }

            @NotNull
            public final Book invoke(@NotNull Book existBook) {
               existBook.setPdfImageWidth(this.$pdfImageWidth.element);
               BookControllerKt.access$getLogger$p().info("saveBookConfig: {}", existBook);
               return existBook;
            }
         }) as Function1;
         `$continuation`.L$0 = returnData;
         `$continuation`.L$1 = book;
         `$continuation`.L$2 = null;
         `$continuation`.label = 2;
         var10000 = this.editShelfBook(book, var18, var10003, `$continuation`);
         if (var10000 === var12) {
            return var12;
         }
      }

      return ReturnData.setData$default(returnData, if (var10000 as Book == null) book else var10000 as Book, null, 2, null);
   }

   public suspend fun saveBookGroupId(context: RoutingContext): ReturnData {
      var `$continuation`: Continuation;
      label66: {
         if (`$completion` is <unrepresentable>) {
            `$continuation` = `$completion` as <unrepresentable>;
            if (((`$completion` as <unrepresentable>).label and Integer.MIN_VALUE) != 0) {
               `$continuation`.label -= Integer.MIN_VALUE;
               break label66;
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
               return this.this$0.saveBookGroupId(null, this);
            }
         };
      }

      var returnData: ReturnData;
      var groupId: LongRef;
      var book: Book;
      label69: {
         val `$result`: Any = `$continuation`.result;
         val var12: Any = IntrinsicsKt.getCOROUTINE_SUSPENDED();
         var var10000: Any;
         switch ($continuation.label) {
            case 0:
               ResultKt.throwOnFailure(`$result`);
               returnData = new ReturnData();
               `$continuation`.L$0 = this;
               `$continuation`.L$1 = context;
               `$continuation`.L$2 = returnData;
               `$continuation`.label = 1;
               var10000 = this.checkAuth(context, `$continuation`);
               if (var10000 === var12) {
                  return var12;
               }
               break;
            case 1:
               returnData = `$continuation`.L$2 as ReturnData;
               context = `$continuation`.L$1 as RoutingContext;
               this = `$continuation`.L$0 as BookController;
               ResultKt.throwOnFailure(`$result`);
               var10000 = `$result`;
               break;
            case 2:
               book = `$continuation`.L$2 as Book;
               groupId = `$continuation`.L$1 as LongRef;
               returnData = `$continuation`.L$0 as ReturnData;
               ResultKt.throwOnFailure(`$result`);
               break label69;
            default:
               throw new IllegalStateException("call to 'resume' before 'invoke' with coroutine");
         }

         if (!var10000 as java.lang.Boolean) {
            return ReturnData.setData$default(returnData, "NEED_LOGIN", null, 2, null).setErrorMsg("请登录后使用");
         }

         groupId = new LongRef();
         val var13: java.lang.String;
         if (context.request().method() === HttpMethod.POST) {
            val userNameSpace: java.lang.String = context.getBodyAsJson().getString("bookUrl");
            var13 = userNameSpace;
            val var14: java.lang.Long = context.getBodyAsJson().getLong("groupId", Boxing.boxLong(0L));
            groupId.element = var14.longValue();
         } else {
            val var19: java.util.List = context.queryParam("bookUrl");
            var var15: java.lang.String = CollectionsKt.firstOrNull(var19);
            var13 = if (var15 == null) "" else var15;
            val var20: java.util.List = context.queryParam("groupId");
            var15 = CollectionsKt.firstOrNull(var20);
            val var10001: Long;
            if (var15 == null) {
               var10001 = 0L;
            } else {
               val var21: java.lang.Long = Boxing.boxLong(java.lang.Long.parseLong(var15));
               var10001 = if (var21 == null) 0L else var21;
            }

            groupId.element = var10001;
         }

         if (var13.length() == 0) {
            return returnData.setErrorMsg("书籍链接不能为空");
         }

         val var18: java.lang.String = this.getUserNameSpace(context);
         book = this.getShelfBookByURL(var13, var18);
         if (book == null) {
            return returnData.setErrorMsg("书籍信息错误");
         }

         if (groupId.element <= 0L) {
            return returnData.setErrorMsg("分组信息错误");
         }

         val var10003: Function1 = (new Function1<Book, Book>(groupId) {
            {
               super(1);
               this.$groupId = `$groupId`;
            }

            @NotNull
            public final Book invoke(@NotNull Book existBook) {
               existBook.setGroup(this.$groupId.element);
               BookControllerKt.access$getLogger$p().info("saveBookGroupId: {}", existBook);
               return existBook;
            }
         }) as Function1;
         `$continuation`.L$0 = returnData;
         `$continuation`.L$1 = groupId;
         `$continuation`.L$2 = book;
         `$continuation`.label = 2;
         if (this.editShelfBook(book, var18, var10003, `$continuation`) === var12) {
            return var12;
         }
      }

      book.setGroup(groupId.element);
      return ReturnData.setData$default(returnData, book, null, 2, null);
   }

   public suspend fun addBookGroupMulti(context: RoutingContext): ReturnData {
      var `$continuation`: Continuation;
      label51: {
         if (`$completion` is <unrepresentable>) {
            `$continuation` = `$completion` as <unrepresentable>;
            if (((`$completion` as <unrepresentable>).label and Integer.MIN_VALUE) != 0) {
               `$continuation`.label -= Integer.MIN_VALUE;
               break label51;
            }
         }

         `$continuation` = new ContinuationImpl(this, `$completion`) {
            Object L$0;
            Object L$1;
            Object L$2;
            Object L$3;
            long J$0;
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
               return this.this$0.addBookGroupMulti(null, this);
            }
         };
      }

      var returnData: ReturnData;
      var groupId: Long;
      var userNameSpace: java.lang.String;
      var bookJsonArray: JsonArray;
      var var8: Int;
      var var9: Int;
      var var15: Any;
      label54: {
         val `$result`: Any = `$continuation`.result;
         var15 = IntrinsicsKt.getCOROUTINE_SUSPENDED();
         var var10000: Any;
         switch ($continuation.label) {
            case 0:
               ResultKt.throwOnFailure(`$result`);
               returnData = new ReturnData();
               `$continuation`.L$0 = this;
               `$continuation`.L$1 = context;
               `$continuation`.L$2 = returnData;
               `$continuation`.label = 1;
               var10000 = this.checkAuth(context, `$continuation`);
               if (var10000 === var15) {
                  return var15;
               }
               break;
            case 1:
               returnData = `$continuation`.L$2 as ReturnData;
               context = `$continuation`.L$1 as RoutingContext;
               this = `$continuation`.L$0 as BookController;
               ResultKt.throwOnFailure(`$result`);
               var10000 = `$result`;
               break;
            case 2:
               var9 = `$continuation`.I$1;
               var8 = `$continuation`.I$0;
               groupId = `$continuation`.J$0;
               bookJsonArray = `$continuation`.L$3 as JsonArray;
               userNameSpace = `$continuation`.L$2 as java.lang.String;
               returnData = `$continuation`.L$1 as ReturnData;
               this = `$continuation`.L$0 as BookController;
               ResultKt.throwOnFailure(`$result`);
               if (var8 >= var9) {
                  return ReturnData.setData$default(returnData, "", null, 2, null);
               }
               break label54;
            default:
               throw new IllegalStateException("call to 'resume' before 'invoke' with coroutine");
         }

         if (!var10000 as java.lang.Boolean) {
            return ReturnData.setData$default(returnData, "NEED_LOGIN", null, 2, null).setErrorMsg("请登录后使用");
         }

         val var16: java.lang.Long = context.getBodyAsJson().getLong("groupId", Boxing.boxLong(0L));
         groupId = var16.longValue();
         if (groupId <= 0L) {
            return returnData.setErrorMsg("分组信息错误");
         }

         userNameSpace = this.getUserNameSpace(context);
         bookJsonArray = context.getBodyAsJson().getJsonArray("bookList", new JsonArray());
         var8 = 0;
         var9 = bookJsonArray.size();
         if (0 >= var9) {
            return ReturnData.setData$default(returnData, "", null, 2, null);
         }
      }

      do {
         val book: Book = bookJsonArray.getJsonObject(var8++).mapTo(Book.class);
         val var10003: Function1 = (new Function1<Book, Book>(groupId) {
            {
               super(1);
               this.$groupId = `$groupId`;
            }

            @NotNull
            public final Book invoke(@NotNull Book existBook) {
               existBook.setGroup(existBook.getGroup() or this.$groupId);
               BookControllerKt.access$getLogger$p().info("saveBookGroupId: {}", existBook);
               return existBook;
            }
         }) as Function1;
         `$continuation`.L$0 = this;
         `$continuation`.L$1 = returnData;
         `$continuation`.L$2 = userNameSpace;
         `$continuation`.L$3 = bookJsonArray;
         `$continuation`.J$0 = groupId;
         `$continuation`.I$0 = var8;
         `$continuation`.I$1 = var9;
         `$continuation`.label = 2;
         if (this.editShelfBook(book, userNameSpace, var10003, `$continuation`) === var15) {
            return var15;
         }
      } while (var8 < var9);

      return ReturnData.setData$default(returnData, "", null, 2, null);
   }

   public suspend fun removeBookGroupMulti(context: RoutingContext): ReturnData {
      var `$continuation`: Continuation;
      label51: {
         if (`$completion` is <unrepresentable>) {
            `$continuation` = `$completion` as <unrepresentable>;
            if (((`$completion` as <unrepresentable>).label and Integer.MIN_VALUE) != 0) {
               `$continuation`.label -= Integer.MIN_VALUE;
               break label51;
            }
         }

         `$continuation` = new ContinuationImpl(this, `$completion`) {
            Object L$0;
            Object L$1;
            Object L$2;
            Object L$3;
            long J$0;
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
               return this.this$0.removeBookGroupMulti(null, this);
            }
         };
      }

      var returnData: ReturnData;
      var groupId: Long;
      var userNameSpace: java.lang.String;
      var bookJsonArray: JsonArray;
      var var8: Int;
      var var9: Int;
      var var15: Any;
      label54: {
         val `$result`: Any = `$continuation`.result;
         var15 = IntrinsicsKt.getCOROUTINE_SUSPENDED();
         var var10000: Any;
         switch ($continuation.label) {
            case 0:
               ResultKt.throwOnFailure(`$result`);
               returnData = new ReturnData();
               `$continuation`.L$0 = this;
               `$continuation`.L$1 = context;
               `$continuation`.L$2 = returnData;
               `$continuation`.label = 1;
               var10000 = this.checkAuth(context, `$continuation`);
               if (var10000 === var15) {
                  return var15;
               }
               break;
            case 1:
               returnData = `$continuation`.L$2 as ReturnData;
               context = `$continuation`.L$1 as RoutingContext;
               this = `$continuation`.L$0 as BookController;
               ResultKt.throwOnFailure(`$result`);
               var10000 = `$result`;
               break;
            case 2:
               var9 = `$continuation`.I$1;
               var8 = `$continuation`.I$0;
               groupId = `$continuation`.J$0;
               bookJsonArray = `$continuation`.L$3 as JsonArray;
               userNameSpace = `$continuation`.L$2 as java.lang.String;
               returnData = `$continuation`.L$1 as ReturnData;
               this = `$continuation`.L$0 as BookController;
               ResultKt.throwOnFailure(`$result`);
               if (var8 >= var9) {
                  return ReturnData.setData$default(returnData, "", null, 2, null);
               }
               break label54;
            default:
               throw new IllegalStateException("call to 'resume' before 'invoke' with coroutine");
         }

         if (!var10000 as java.lang.Boolean) {
            return ReturnData.setData$default(returnData, "NEED_LOGIN", null, 2, null).setErrorMsg("请登录后使用");
         }

         val var16: java.lang.Long = context.getBodyAsJson().getLong("groupId", Boxing.boxLong(0L));
         groupId = var16.longValue();
         if (groupId <= 0L) {
            return returnData.setErrorMsg("分组信息错误");
         }

         userNameSpace = this.getUserNameSpace(context);
         bookJsonArray = context.getBodyAsJson().getJsonArray("bookList", new JsonArray());
         var8 = 0;
         var9 = bookJsonArray.size();
         if (0 >= var9) {
            return ReturnData.setData$default(returnData, "", null, 2, null);
         }
      }

      do {
         val book: Book = bookJsonArray.getJsonObject(var8++).mapTo(Book.class);
         val var10003: Function1 = (new Function1<Book, Book>(groupId) {
            {
               super(1);
               this.$groupId = `$groupId`;
            }

            @NotNull
            public final Book invoke(@NotNull Book existBook) {
               existBook.setGroup(existBook.getGroup() xor this.$groupId);
               BookControllerKt.access$getLogger$p().info("saveBookGroupId: {}", existBook);
               return existBook;
            }
         }) as Function1;
         `$continuation`.L$0 = this;
         `$continuation`.L$1 = returnData;
         `$continuation`.L$2 = userNameSpace;
         `$continuation`.L$3 = bookJsonArray;
         `$continuation`.J$0 = groupId;
         `$continuation`.I$0 = var8;
         `$continuation`.I$1 = var9;
         `$continuation`.label = 2;
         if (this.editShelfBook(book, userNameSpace, var10003, `$continuation`) === var15) {
            return var15;
         }
      } while (var8 < var9);

      return ReturnData.setData$default(returnData, "", null, 2, null);
   }

   public suspend fun deleteBook(context: RoutingContext): ReturnData {
      var `$continuation`: Continuation;
      label65: {
         if (`$completion` is <unrepresentable>) {
            `$continuation` = `$completion` as <unrepresentable>;
            if (((`$completion` as <unrepresentable>).label and Integer.MIN_VALUE) != 0) {
               `$continuation`.label -= Integer.MIN_VALUE;
               break label65;
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
               return this.this$0.deleteBook(null, this);
            }
         };
      }

      val `$result`: Any = `$continuation`.result;
      val var18: Any = IntrinsicsKt.getCOROUTINE_SUSPENDED();
      var returnData: ReturnData;
      var var10000: Any;
      switch ($continuation.label) {
         case 0:
            ResultKt.throwOnFailure(`$result`);
            returnData = new ReturnData();
            `$continuation`.L$0 = this;
            `$continuation`.L$1 = context;
            `$continuation`.L$2 = returnData;
            `$continuation`.label = 1;
            var10000 = this.checkAuth(context, `$continuation`);
            if (var10000 === var18) {
               return var18;
            }
            break;
         case 1:
            returnData = `$continuation`.L$2 as ReturnData;
            context = `$continuation`.L$1 as RoutingContext;
            this = `$continuation`.L$0 as BookController;
            ResultKt.throwOnFailure(`$result`);
            var10000 = `$result`;
            break;
         default:
            throw new IllegalStateException("call to 'resume' before 'invoke' with coroutine");
      }

      if (!var10000 as java.lang.Boolean) {
         return ReturnData.setData$default(returnData, "NEED_LOGIN", null, 2, null).setErrorMsg("请登录后使用");
      } else {
         val book: Book = context.getBodyAsJson().mapTo(Book.class);
         val userNameSpace: java.lang.String = this.getUserNameSpace(context);
         var bookshelf: JsonArray = ExtKt.asJsonArray(this.getUserStorage(userNameSpace, new java.lang.String[]{"bookshelf"}));
         if (bookshelf == null) {
            bookshelf = new JsonArray();
         }

         var var19: Int = -1;
         var bookName: java.lang.String = "";
         var bookAuthor: java.lang.String = "";
         var existBook: Int = 0;
         val localBookPath: Int = bookshelf.size();
         if (0 < localBookPath) {
            do {
               val cachePath: Int = existBook++;
               var var14: java.lang.String = bookshelf.getJsonObject(cachePath).getString("name", "");
               bookName = var14;
               var14 = bookshelf.getJsonObject(cachePath).getString("author", "");
               bookAuthor = var14;
               var14 = bookshelf.getJsonObject(cachePath).getString("bookUrl", "");
               if (var14.equals(book.getBookUrl())) {
                  var19 = cachePath;
                  break;
               }

               if (var14.equals(book.getName()) && var14.equals(book.getAuthor())) {
                  var19 = cachePath;
                  break;
               }
            } while (existBook < localBookPath);
         }

         if (var19 < 0) {
            return returnData.setErrorMsg("书架书籍不存在");
         } else {
            val var20: JsonObject = bookshelf.getJsonObject(var19);
            bookshelf.remove(var19);
            this.saveUserStorage(userNameSpace, "bookshelf", bookshelf);
            ExtKt.deleteRecursively(new File(ExtKt.getWorkDir("storage", "data", userNameSpace, "$bookName_$bookAuthor")));
            val var23: java.lang.CharSequence = var20.getString("coverUrl");
            if (var23 != null && var23.length() != 0) {
               var10000 = var20.getString("coverUrl");
               if (StringsKt.startsWith$default((java.lang.String)var10000, "/", false, 2, null)) {
                  val var28: Array<java.lang.String> = new java.lang.String[]{"storage", null};
                  val var10002: java.lang.String = var20.getString("coverUrl");
                  var28[1] = var10002;
                  FileUtils.INSTANCE.deleteFile(ExtKt.getWorkDir(var28));
               }
            }

            return ReturnData.setData$default(returnData, "删除书籍成功", null, 2, null);
         }
      }
   }

   public suspend fun deleteBooks(context: RoutingContext): ReturnData {
      var `$continuation`: Continuation;
      label48: {
         if (`$completion` is <unrepresentable>) {
            `$continuation` = `$completion` as <unrepresentable>;
            if (((`$completion` as <unrepresentable>).label and Integer.MIN_VALUE) != 0) {
               `$continuation`.label -= Integer.MIN_VALUE;
               break label48;
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
               return this.this$0.deleteBooks(null, this);
            }
         };
      }

      val `$result`: Any = `$continuation`.result;
      val var18: Any = IntrinsicsKt.getCOROUTINE_SUSPENDED();
      var returnData: ReturnData;
      var var10000: Any;
      switch ($continuation.label) {
         case 0:
            ResultKt.throwOnFailure(`$result`);
            returnData = new ReturnData();
            `$continuation`.L$0 = this;
            `$continuation`.L$1 = context;
            `$continuation`.L$2 = returnData;
            `$continuation`.label = 1;
            var10000 = this.checkAuth(context, `$continuation`);
            if (var10000 === var18) {
               return var18;
            }
            break;
         case 1:
            returnData = `$continuation`.L$2 as ReturnData;
            context = `$continuation`.L$1 as RoutingContext;
            this = `$continuation`.L$0 as BookController;
            ResultKt.throwOnFailure(`$result`);
            var10000 = `$result`;
            break;
         default:
            throw new IllegalStateException("call to 'resume' before 'invoke' with coroutine");
      }

      if (!var10000 as java.lang.Boolean) {
         return ReturnData.setData$default(returnData, "NEED_LOGIN", null, 2, null).setErrorMsg("请登录后使用");
      } else {
         val bookJsonArray: JsonArray = context.getBodyAsJsonArray();
         val userNameSpace: java.lang.String = this.getUserNameSpace(context);
         var bookshelf: JsonArray = ExtKt.asJsonArray(this.getUserStorage(userNameSpace, new java.lang.String[]{"bookshelf"}));
         if (bookshelf == null) {
            bookshelf = new JsonArray();
         }

         val var19: java.util.Map = new LinkedHashMap();
         var var20: Int = 0;
         val book: Int = bookJsonArray.size();
         if (0 < book) {
            do {
               val bookName: Int = var20++;
               val bookAuthor: java.lang.String = bookJsonArray.getJsonObject(bookName).getString("bookUrl", "");
               var19.put(bookAuthor, Boxing.boxInt(bookName));
               var19.put(
                  "${bookJsonArray.getJsonObject(bookName).getString("name", "")}_${bookshelf.getJsonObject(bookName).getString("author", "")}",
                  Boxing.boxInt(bookName)
               );
            } while (var20 < book);
         }

         val var22: java.util.Iterator = bookshelf.iterator();
         val var21: java.util.Iterator = var22;

         while (iterator.hasNext()) {
            val var24: Any = var21.next();
            if (var24 == null) {
               throw new NullPointerException("null cannot be cast to non-null type io.vertx.core.json.JsonObject");
            }

            val var23: JsonObject = var24 as JsonObject;
            val var25: java.lang.String = (var24 as JsonObject).getString("name", "");
            val var26: java.lang.String = (var24 as JsonObject).getString("author", "");
            val bookUrl: java.lang.String = var23.getString("bookUrl", "");
            if (var19.getOrDefault(bookUrl, var19.getOrDefault("$var25_$var26", Boxing.boxInt(-1))).intValue() >= 0) {
               var21.remove();
               ExtKt.deleteRecursively(new File(ExtKt.getWorkDir("storage", "data", userNameSpace, "$var25_$var26")));
            }
         }

         this.saveUserStorage(userNameSpace, "bookshelf", bookshelf);
         return ReturnData.setData$default(returnData, "", null, 2, null);
      }
   }

   public suspend fun saveBookInfoCache(bookList: List<Book>): List<Book> {
      if (bookList.size() > 0) {
         var var3: Int = 0;
         val var4: Int = bookList.size();
         if (0 < var4) {
            do {
               val book: Book = bookList.get(var3++) as Book;
               val var10000: ACache = this.bookInfoCache;
               val var10001: java.lang.String = book.getBookUrl();
               val var7: java.util.Map = JsonObject.mapFrom(book).getMap();
               var10000.put(var10001, ExtKt.jsonEncode$default(var7, false, 2, null));
            } while (var3 < var4);
         }
      }

      return bookList;
   }

   public suspend fun mergeBookCacheInfo(book: Book): Book {
      label22: {
         val var4: java.lang.String = this.bookInfoCache.getAsString(book.getBookUrl());
         val var10000: Book;
         if (var4 == null) {
            var10000 = null;
         } else {
            val var5: java.util.Map = ExtKt.toMap(var4);
            var10000 = if (var5 == null)
               null
               else
               ExtKt.getGson()
                  .fromJson(
                     if (var5 is java.lang.String) var5 as java.lang.String else ExtKt.getGson().toJson(var5),
                     new BookController$mergeBookCacheInfo$$inlined$toDataClass$1().getType()
                  );
         }

         return if (var10000 != null)
            ExtKt.fillData(
               book, var10000, CollectionsKt.listOf(new java.lang.String[]{"name", "author", "coverUrl", "tocUrl", "intro", "latestChapterTitle", "wordCount"})
            )
            else
            book;
      }
   }

   public suspend fun getBookShelfBooks(refresh: Boolean = ..., userNameSpace: String): List<Book> {
      var `$continuation`: Continuation;
      label33: {
         if (`$completion` is <unrepresentable>) {
            `$continuation` = `$completion` as <unrepresentable>;
            if (((`$completion` as <unrepresentable>).label and Integer.MIN_VALUE) != 0) {
               `$continuation`.label -= Integer.MIN_VALUE;
               break label33;
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
               return this.this$0.getBookShelfBooks(false, null, this);
            }
         };
      }

      val `$result`: Any = `$continuation`.result;
      val var11: Any = IntrinsicsKt.getCOROUTINE_SUSPENDED();
      var bookList: ObjectRef;
      switch ($continuation.label) {
         case 0:
            ResultKt.throwOnFailure(`$result`);
            val bookshelf: ObjectRef = new ObjectRef();
            bookshelf.element = (T)ExtKt.asJsonArray(this.getUserStorage(userNameSpace, new java.lang.String[]{"bookshelf"}));
            if (bookshelf.element == null) {
               return new ArrayList();
            }

            if ((bookshelf.element as JsonArray).size() == 0) {
               return new ArrayList();
            }

            bookList = new ObjectRef();
            bookList.element = (T)(new ArrayList());
            val mutex: Mutex = MutexKt.Mutex$default(false, 1, null);
            val syncMutex: Mutex = MutexKt.Mutex$default(false, 1, null);
            val var10003: Int = (bookshelf.element as JsonArray).size();
            val var10004: Function3 = (
               new Function3<CoroutineScope, Integer, Continuation<? super Object>, Object>(
                  bookshelf, refresh, this, userNameSpace, syncMutex, bookList, mutex, null
               )// $VF: Couldn't be decompiled
   // Please report this to the Vineflower issue tracker, at https://github.com/Vineflower/vineflower/issues with a copy of the class file (if you have the rights to distribute it!)
   // java.lang.NullPointerException: Cannot invoke "org.jetbrains.java.decompiler.modules.decompiler.stats.Statement.getVarDefinitions()" because "stat" is null
   //   at org.jetbrains.java.decompiler.modules.decompiler.vars.VarDefinitionHelper.iterateClashingNames(VarDefinitionHelper.java:1468)
   //   at org.jetbrains.java.decompiler.modules.decompiler.vars.VarDefinitionHelper.iterateClashingExprent(VarDefinitionHelper.java:1679)
   //   at org.jetbrains.java.decompiler.modules.decompiler.vars.VarDefinitionHelper.iterateClashingNames(VarDefinitionHelper.java:1496)
   //   at org.jetbrains.java.decompiler.modules.decompiler.vars.VarDefinitionHelper.iterateClashingNames(VarDefinitionHelper.java:1545)
   //   at org.jetbrains.java.decompiler.modules.decompiler.vars.VarDefinitionHelper.remapClashingNames(VarDefinitionHelper.java:1458)
   //   at org.jetbrains.java.decompiler.modules.decompiler.vars.VarProcessor.rerunClashing(VarProcessor.java:99)
   //   at org.jetbrains.java.decompiler.main.ClassWriter.invokeProcessors(ClassWriter.java:118)
   //   at org.jetbrains.java.decompiler.main.ClassWriter.writeClass(ClassWriter.java:352)
   //   at org.jetbrains.java.decompiler.modules.decompiler.exps.NewExprent.toJava(NewExprent.java:407)
   //   at org.jetbrains.java.decompiler.modules.decompiler.exps.FunctionExprent.wrapOperandString(FunctionExprent.java:761)
   //   at org.jetbrains.java.decompiler.modules.decompiler.exps.FunctionExprent.wrapOperandString(FunctionExprent.java:727)
   
            ) as Function3;
            `$continuation`.L$0 = bookList;
            `$continuation`.label = 1;
            if (this.limitConcurrent(16, 0, var10003, var10004, `$continuation`) === var11) {
               return var11;
            }
            break;
         case 1:
            bookList = `$continuation`.L$0 as ObjectRef;
            ResultKt.throwOnFailure(`$result`);
            break;
         default:
            throw new IllegalStateException("call to 'resume' before 'invoke' with coroutine");
      }

      return bookList.element;
   }

    /**
     * MANUALLY RECONSTRUCTED from CFR + BookController.class (reader-pro-3.2.14.jar)
     * Original Vineflower output: Couldn't be decompiled
     */
    public suspend fun getLocalChapterList(
        book: Book,
        bookSource: String?,
        refresh: Boolean,
        userNameSpace: String,
        debugLog: Boolean,
        mutex: Mutex? = null
    ): List<BookChapter> {
        val md5Encode = MD5Utils.md5Encode(book.bookUrl).toString()
        val bookChaptersCache = getBookChaptersCache(userNameSpace)
        var chapterListJson: JsonArray? = null
        if (book.isInShelf) {
            chapterListJson = asJsonArray(
                getUserStorage(userNameSpace, book.name + '_' + book.author, md5Encode)
            )
        } else {
            chapterListJson = asJsonArray(
                bookChaptersCache.getAsString(book.name + '_' + book.author + md5Encode)
            )
        }
        if (chapterListJson != null && !refresh) {
            val localChapterList = ArrayList<BookChapter>()
            for (i in 0 until chapterListJson.size()) {
                localChapterList.add(
                    chapterListJson.getJsonObject(i).mapTo(BookChapter::class.java)
                )
            }
            return localChapterList
        }

        book.rootDir = getWorkDir()
        book.userNameSpace = userNameSpace
        val newChapterList: List<BookChapter> = try {
            if (book.isLocalBook) {
                if (book.isEpub && !extractEpub(book, refresh)) {
                    throw Exception("Epub书籍解压失败")
                }
                if (book.isCbz && !extractCbz(book, refresh)) {
                    throw Exception("CBZ书籍解压失败")
                }
                if (book.isPdf && !convertPdfToImage(book, refresh)) {
                    throw Exception("PDF书籍转换失败")
                }
                LocalBook.getChapterList(book)
            } else {
                if (bookSource.isNullOrEmpty()) {
                    throw Exception("书源信息错误")
                }
                var bookSourceObject = BookSource.fromJson(bookSource).getOrNull()
                bookSourceObject?.ruleToc?.preUpdateJs?.let { js ->
                    AnalyzeRule(book, bookSourceObject).evalJS(js)
                }
                var bookForToc = book
                if (book.tocUrl.isBlank()) {
                    bookForToc = WebBook(
                        bookSource, debugLog, null, userNameSpace
                    ).getBookInfo(book.bookUrl)
                }
                WebBook(bookSource, debugLog, null, userNameSpace)
                    .getChapterList(bookForToc)
            }
        } catch (e: Exception) {
            if (!bookSource.isNullOrEmpty()) {
                val bookSourceObject = BookSource.fromJson(bookSource).getOrNull()
                if (bookSourceObject != null) {
                    val info = mutableMapOf(
                        "sourceUrl" to bookSourceObject.bookSourceUrl,
                        "time" to System.currentTimeMillis(),
                        "error" to e.toString()
                    )
                    addInvalidBookSource(bookSourceObject.bookSourceUrl, info, userNameSpace)
                }
            }
            mutex?.lock()
            try {
                book.lastCheckError = e.toString()
                editShelfBook(book, userNameSpace) { exist ->
                    exist.lastCheckError = e.toString()
                    exist
                }
            } finally {
                mutex?.unlock()
            }
            throw e
        }

        if (book.isInShelf) {
            saveUserStorage(
                userNameSpace,
                getRelativePath(book.name + '_' + book.author, md5Encode),
                newChapterList
            )
        } else {
            bookChaptersCache.put(
                book.name + '_' + book.author + md5Encode,
                jsonEncode(newChapterList),
                3600
            )
        }
        saveShelfBookLatestChapter(book, newChapterList, userNameSpace, mutex)
        return newChapterList
    }

   public suspend fun getBookSourceString(context: RoutingContext, sourceUrl: String = ..., withExploreUrl: Boolean = ...): String? {
      var bookSourceString: java.lang.String = null;
      if (context.request().method() === HttpMethod.POST) {
         val userNameSpace: JsonObject = context.getBodyAsJson().getJsonObject("bookSource");
         if (userNameSpace != null) {
            bookSourceString = userNameSpace.toString();
         }
      }

      val var10: java.lang.String = this.getUserNameSpace(context);
      if (bookSourceString == null || bookSourceString.length() == 0) {
         val var12: java.lang.String;
         if (context.request().method() === HttpMethod.POST) {
            val var15: java.lang.String = context.getBodyAsJson().getString("bookSourceUrl", "");
            var12 = var15;
         } else {
            val var20: java.util.List = context.queryParam("bookSourceUrl");
            val var16: java.lang.String = CollectionsKt.firstOrNull(var20);
            var12 = if (var16 == null) "" else var16;
         }

         if (!StringsKt.isBlank(var12)) {
            bookSourceString = this.getBookSourceStringBySourceURLOpt(var12, var10);
         }
      }

      if ((bookSourceString == null || bookSourceString.length() == 0) && sourceUrl != null && sourceUrl.length() != 0) {
         bookSourceString = this.getBookSourceStringBySourceURLOpt(sourceUrl, var10);
      }

      return bookSourceString;
   }

   public fun getBookSourceStringBySourceURLOpt(sourceUrl: String, userNameSpace: String): String? {
      if (StringsKt.isBlank(sourceUrl)) {
         return null;
      } else {
         var file: File = ExtKt.getStorageFile$default(new java.lang.String[]{"data", userNameSpace, "bookSource"}, null, 2, null);
         if (!file.exists()) {
            file = ExtKt.getStorageFile$default(new java.lang.String[]{"data", "default", "bookSource"}, null, 2, null);
            if (!file.exists()) {
               return null;
            }
         }

         try {
            label50: {
               val factory: JsonFactory = new ObjectMapper().getFactory();
               val bookSourceString: ObjectRef = new ObjectRef();
               val var7: Closeable = factory.createParser(file);
               var var22: java.lang.Throwable = null as java.lang.Throwable;

               try {
                  try {
                     val parser: JsonParser = var7 as JsonParser;
                     if ((var7 as JsonParser).nextToken() === JsonToken.START_ARRAY) {
                        while (parser.nextToken() != JsonToken.END_ARRAY) {
                           if (parser.currentToken() === JsonToken.START_OBJECT) {
                              val var12: TreeNode = parser.readValueAsTree();
                              val jsonNode: JsonNode = var12 as JsonNode;
                              if (sourceUrl.equals((var12 as JsonNode).get("bookSourceUrl").asText())) {
                                 bookSourceString.element = (T)jsonNode.toString();
                                 break;
                              }
                           }
                        }
                     }
                  } catch (var14: java.lang.Throwable) {
                     var22 = var14;
                     throw var14;
                  }
               } catch (var15: java.lang.Throwable) {
                  CloseableKt.closeFinally(var7, var22);
               }

               CloseableKt.closeFinally(var7, null as java.lang.Throwable);
            }
         } catch (var16: Exception) {
            BookControllerKt.access$getLogger$p().error("解析文件内容出错: {}  文件: \n{}", var16, file);
            throw var16;
         }
      }
   }

   public fun getShelfBookByURL(url: String, userNameSpace: String): Book? {
      if (url.length() == 0) {
         return null;
      } else {
         val var13: JsonArray = ExtKt.asJsonArray(this.getUserStorage(userNameSpace, new java.lang.String[]{"bookshelf"}));
         if (var13 == null) {
            return null;
         } else {
            var var15: Int = 0;
            val var5: Int = var13.size();
            if (0 < var5) {
               do {
                  val `$this$toDataClass$iv`: java.util.Map = var13.getJsonObject(var15++).getMap();
                  val _book: Book = ExtKt.getGson()
                     .fromJson(
                        if (`$this$toDataClass$iv` is java.lang.String)
                           `$this$toDataClass$iv` as java.lang.String
                           else
                           ExtKt.getGson().toJson(`$this$toDataClass$iv`),
                        new BookController$getShelfBookByURL$$inlined$toDataClass$1().getType()
                     );
                  if (_book.getBookUrl().equals(url)) {
                     _book.setRootDir(ExtKt.getWorkDir$default(null, 1, null));
                     _book.setUserNameSpace(userNameSpace);
                     _book.setInShelf(true);
                     return _book;
                  }
               } while (var15 < var5);
            }

            return null;
         }
      }
   }

   public suspend fun saveShelfBookProgress(book: Book, bookChapter: BookChapter, userNameSpace: String) {
      val var10000: Any = this.editShelfBook(book, userNameSpace, (new Function1<Book, Book>(bookChapter) {
         {
            super(1);
            this.$bookChapter = `$bookChapter`;
         }

         @NotNull
         public final Book invoke(@NotNull Book existBook) {
            existBook.setDurChapterIndex(this.$bookChapter.getIndex());
            existBook.setDurChapterTitle(this.$bookChapter.getTitle());
            existBook.setDurChapterTime(System.currentTimeMillis());
            return existBook;
         }
      }) as (Book?) -> Book, `$completion`);
      return if (var10000 === IntrinsicsKt.getCOROUTINE_SUSPENDED()) var10000 else Unit.INSTANCE;
   }

    /**
     * MANUALLY RECONSTRUCTED from CFR + BookController.class (reader-pro-3.2.14.jar)
     * Original Vineflower output: Couldn't be decompiled
     */
    public suspend fun saveShelfBookLatestChapter(
        book: Book,
        bookChapterList: List<BookChapter>,
        userNameSpace: String,
        mutex: Mutex? = null
    ) {
        mutex?.lock()
        try {
            editShelfBook(book, userNameSpace) { existBook ->
                if (bookChapterList.isNotEmpty()) {
                    existBook.latestChapterTitle = bookChapterList.last().title
                }
                val delta = bookChapterList.size - existBook.totalChapterNum
                if (delta > 0) {
                    existBook.lastCheckCount = delta
                    existBook.lastCheckTime = System.currentTimeMillis()
                }
                existBook.lastCheckError = null
                existBook.totalChapterNum = bookChapterList.size
                book.latestChapterTitle = existBook.latestChapterTitle
                book.lastCheckCount = existBook.lastCheckCount
                book.lastCheckTime = existBook.lastCheckTime
                book.lastCheckError = existBook.lastCheckError
                book.totalChapterNum = existBook.totalChapterNum
                existBook
            }
        } finally {
            mutex?.unlock()
        }
    }

    /**
     * MANUALLY RECONSTRUCTED from CFR + BookController.class (reader-pro-3.2.14.jar)
     * Original Vineflower output: Couldn't be decompiled
     */
    public suspend fun editShelfBook(
        book: Book,
        userNameSpace: String,
        handler: (Book) -> Book
    ): Book? {
        val mutex = UserMutex.getLocker(userNameSpace + "@bookshelf")
        BookControllerKt.access$getLogger$p().info("wait for lock {}", userNameSpace + "@bookshelf")
        mutex.lock()
        try {
            BookControllerKt.access$getLogger$p().info("lock success")
            var bookshelf = asJsonArray(getUserStorage(userNameSpace, "bookshelf")) ?: JsonArray()
            var existIndex = -1
            var i = 0
            val size = bookshelf.size()
            while (i < size) {
                val idx = i++
                val existing = bookshelf.getJsonObject(idx).mapTo(Book::class.java)
                if (book.bookUrl.isNotEmpty() && existing.bookUrl == book.bookUrl) {
                    existIndex = idx
                    break
                }
                if (book.name.isNotEmpty() && existing.name == book.name
                    && book.author.isNotEmpty() && existing.author == book.author
                ) {
                    existIndex = idx
                    break
                }
            }
            if (existIndex >= 0) {
                val bookList = bookshelf.list
                var existBook = bookshelf.getJsonObject(existIndex).mapTo(Book::class.java)
                existBook = handler(existBook)
                bookList[existIndex] = JsonObject.mapFrom(existBook)
                bookshelf = JsonArray(bookList)
                saveUserStorage(userNameSpace, "bookshelf", bookshelf)
                return existBook
            }
            return null
        } finally {
            mutex.unlock()
        }
    }

   public fun saveBookSources(book: Book, sourceList: List<SearchBook>, userNameSpace: String, replace: Boolean = false) {
      if (book.getName().length() != 0) {
         var var12: JsonArray = new JsonArray();
         if (!replace) {
            val var13: JsonArray = ExtKt.asJsonArray(
               this.getUserStorage(userNameSpace, new java.lang.String[]{"${book.getName()}_${book.getAuthor()}", "bookSource"})
            );
            if (var13 != null) {
               var12 = var13;
            }
         }

         val var14: java.util.Map = new LinkedHashMap();
         var var16: Int = 0;
         var var8: Int = var12.size();
         if (0 < var8) {
            do {
               val k: Int = var16++;
               val searchBook: java.lang.String = var12.getJsonObject(k).getString("bookUrl");
               var14.put(searchBook, k);
            } while (var16 < var8);
         }

         var16 = 0;
         var8 = sourceList.size();
         if (0 < var8) {
            do {
               val var21: SearchBook = sourceList.get(var16++) as SearchBook;
               val existIndex: Int = var14.getOrDefault(var21.getBookUrl(), -1).intValue();
               if (existIndex >= 0) {
                  var12.set(existIndex, JsonObject.mapFrom(var21));
               } else {
                  var12.add(JsonObject.mapFrom(var21));
                  var14.put(var21.getBookUrl(), var12.size() - 1);
               }
            } while (var16 < var8);
         }

         this.saveUserStorage(userNameSpace, ExtKt.getRelativePath("${book.getName()}_${book.getAuthor()}", "bookSource"), var12);
      }
   }

   public fun extractEpub(book: Book, force: Boolean = false): Boolean {
      val epubExtractDir: File = new File(ExtKt.getWorkDir("${book.getBookUrl()}${File.separator}index"));
      if (force || !epubExtractDir.exists()) {
         ExtKt.deleteRecursively(epubExtractDir);
         var localEpubFile: File = new File(ExtKt.getWorkDir("${book.getOriginName()}${File.separator}index.epub"));
         if (StringsKt.indexOf$default(book.getOriginName(), "localStore", 0, false, 6, null) > 0) {
            localEpubFile = new File(ExtKt.getWorkDir(book.getOriginName()));
         }

         if (StringsKt.indexOf$default(book.getOriginName(), "webdav", 0, false, 6, null) > 0) {
            localEpubFile = new File(ExtKt.getWorkDir(book.getOriginName()));
         }

         BookControllerKt.access$getLogger$p().info("extractEpub from {} to {}", localEpubFile, epubExtractDir);
         val var5: java.lang.String = epubExtractDir.toString();
         if (!ExtKt.unzip(localEpubFile, var5)) {
            return false;
         }
      }

      return true;
   }

   public fun extractCbz(book: Book, force: Boolean = false): Boolean {
      val extractDir: File = new File(ExtKt.getWorkDir("${book.getBookUrl()}${File.separator}index"));
      if (force || !extractDir.exists()) {
         ExtKt.deleteRecursively(extractDir);
         var localFile: File = new File(ExtKt.getWorkDir("${book.getOriginName()}${File.separator}index.cbz"));
         if (StringsKt.indexOf$default(book.getOriginName(), "localStore", 0, false, 6, null) > 0) {
            localFile = new File(ExtKt.getWorkDir(book.getOriginName()));
         }

         if (StringsKt.indexOf$default(book.getOriginName(), "webdav", 0, false, 6, null) > 0) {
            localFile = new File(ExtKt.getWorkDir(book.getOriginName()));
         }

         val var5: java.lang.String = extractDir.toString();
         if (!ExtKt.unzip(localFile, var5)) {
            return false;
         }
      }

      return true;
   }

   public fun convertPdfToImage(book: Book, force: Boolean = false): Boolean {
      return true;
   }

   public fun convertPdfPageToImage(book: Book, index: Int, force: Boolean = false) {
      val extractDir: File = new File(ExtKt.getWorkDir("${book.getBookUrl()}${File.separator}index"));
      if (!extractDir.exists()) {
         extractDir.mkdirs();
      }

      val output: File = new File("${extractDir.toString()}${File.separator}output-$index.png");
      if (force || !output.exists()) {
         ExtKt.deleteRecursively(output);
         var localFile: File = new File(ExtKt.getWorkDir("${book.getOriginName()}${File.separator}index.pdf"));
         if (StringsKt.indexOf$default(book.getOriginName(), "localStore", 0, false, 6, null) > 0) {
            localFile = new File(ExtKt.getWorkDir(book.getOriginName()));
         }

         if (StringsKt.indexOf$default(book.getOriginName(), "webdav", 0, false, 6, null) > 0) {
            localFile = new File(ExtKt.getWorkDir(book.getOriginName()));
         }

         val document: PDDocument = PDDocument.load(localFile);
         val renderer: PDFRenderer = new PDFRenderer(document);
         val targetWidth: Float = book.getPdfImageWidth();
         this.savePdfPageToImage(document, renderer, index, targetWidth, "png", output);
      }
   }

   public fun savePdfPageToImage(document: PDDocument, renderer: PDFRenderer, index: Int, targetWidth: Float, imageFormat: String, output: File) {
      val pageSize: PDRectangle = document.getPage(index).getCropBox();
      val targetDimension: Dimension = new Dimension(
         (int)targetWidth, if (0.0F == 0.0F) (int)(pageSize.getHeight() * (targetWidth / pageSize.getWidth())) else (int)0.0F
      );
      val scaledImage: Image = renderer.renderImageWithDPI(index, 300.0F, ImageType.RGB).getScaledInstance(targetDimension.width, targetDimension.height, 4);
      val scaledBufferedImage: BufferedImage = new BufferedImage(targetDimension.width, targetDimension.height, 1);
      val graphics: Graphics2D = scaledBufferedImage.createGraphics();
      graphics.drawImage(scaledImage, 0, 0, null);
      graphics.dispose();
      ImageIO.write(scaledBufferedImage, imageFormat, output);
   }

   public suspend fun syncBookProgressFromWebdav(progressFilePath: Any, userNameSpace: String) {
      var progressFile: File = null;
      if (progressFilePath is File) {
         progressFile = progressFilePath as File;
      } else if (progressFilePath is java.lang.String) {
         progressFile = new File(progressFilePath as java.lang.String);
      }

      if (progressFile == null) {
         return Unit.INSTANCE;
      } else {
         val book: ObjectRef = new ObjectRef();
         val var6: JsonObject = ExtKt.asJsonObject(FilesKt.readText$default(progressFile, null, 1, null));
         book.element = (T)(if (var6 == null) null else var6.mapTo(Book.class) as Book);
         if (book.element != null) {
            val var10000: Any = this.editShelfBook(book.element as Book, userNameSpace, (new Function1<Book, Book>(book) {
               {
                  super(1);
                  this.$book = `$book`;
               }

               @NotNull
               public final Book invoke(@NotNull Book existBook) {
                  existBook.setDurChapterIndex(this.$book.element.getDurChapterIndex());
                  existBook.setDurChapterPos(this.$book.element.getDurChapterPos());
                  existBook.setDurChapterTime(this.$book.element.getDurChapterTime());
                  existBook.setDurChapterTitle(this.$book.element.getDurChapterTitle());
                  BookControllerKt.access$getLogger$p().info("syncShelfBookProgress: {}", existBook);
                  return existBook;
               }
            }) as (Book?) -> Book, `$completion`);
            return if (var10000 === IntrinsicsKt.getCOROUTINE_SUSPENDED()) var10000 else Unit.INSTANCE;
         } else {
            return Unit.INSTANCE;
         }
      }
   }

   public suspend fun saveBookProgressToWebdav(book: Book, bookChapter: BookChapter, userNameSpace: String) {
      val userHome: java.lang.String = this.getUserWebdavHome(userNameSpace);
      var bookProgressDir: File = new File("$userHome${File.separator}bookProgress");
      if (!bookProgressDir.exists()) {
         bookProgressDir = new File("$userHome${File.separator}legado${File.separator}bookProgress");
         if (!bookProgressDir.exists()) {
            return Unit.INSTANCE;
         }
      }

      FilesKt.writeText$default(
         new File("${bookProgressDir.toString()}${File.separator}${book.getName()}_${book.getAuthor()}.json"),
         ExtKt.jsonEncode(
            MapsKt.mapOf(
               new Pair[]{
                  TuplesKt.to("name", book.getName()),
                  TuplesKt.to("author", book.getAuthor()),
                  TuplesKt.to("durChapterIndex", Boxing.boxInt(bookChapter.getIndex())),
                  TuplesKt.to("durChapterPos", Boxing.boxInt(0)),
                  TuplesKt.to("durChapterTime", Boxing.boxLong(System.currentTimeMillis())),
                  TuplesKt.to("durChapterTitle", bookChapter.getTitle())
               }
            ),
            true
         ),
         null,
         2,
         null
      );
      return Unit.INSTANCE;
   }

    /**
     * MANUALLY RECONSTRUCTED from CFR + BookController.class (reader-pro-3.2.14.jar)
     * Original Vineflower output: Couldn't be decompiled
     */
    public suspend fun syncFromWebdav(zipFilePath: String, userNameSpace: String): Boolean {
        val descDir = getWorkDir("storage", "data", userNameSpace, "tmp")
        val descDirFile = File(descDir)
        try {
            val userHome = getUserWebdavHome(userNameSpace)
            val zipFile = File(zipFilePath)
            if (!zipFile.exists()) {
                deleteRecursively(descDirFile)
                return false
            }
            deleteRecursively(descDirFile)
            ZipUtils.unzipFile(zipFile, descDirFile)
            for (name in getBackupFileNames()) {
                val backupFile = File(descDir + File.separator + name)
                if (!backupFile.exists()) continue
                val userDataFile = File(getWorkDir("storage", "data", userNameSpace, name))
                deleteRecursively(userDataFile)
                backupFile.copyRecursively(userDataFile, overwrite = false)
            }
            val backupBooksDir = File(descDir + File.separator + "books")
            if (backupBooksDir.exists()) {
                val webdavBooksDir =
                    File(getWorkDir("storage", "data", userNameSpace, "webdav", "books"))
                deleteRecursively(webdavBooksDir)
                backupBooksDir.copyRecursively(webdavBooksDir, overwrite = false)
            }
            var bookProgressDir = File(userHome + File.separator + "bookProgress")
            if (!bookProgressDir.exists()) {
                bookProgressDir =
                    File(userHome + File.separator + "legado" + File.separator + "bookProgress")
            }
            if (bookProgressDir.exists() && bookProgressDir.isDirectory) {
                bookProgressDir.listFiles()?.forEach { f ->
                    syncBookProgressFromWebdav(f, userNameSpace)
                }
            }
            return true
        } catch (e: Exception) {
            e.printStackTrace()
            return false
        } finally {
            deleteRecursively(descDirFile)
        }
    }

   public suspend fun saveToWebdav(userNameSpace: String, latestZipFilePath: String? = ...): Boolean {
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
               return this.this$0.saveToWebdav(null, null, this);
            }
         };
      }

      var userHome: java.lang.String;
      var legadoHome: java.lang.String;
      var var9: Any;
      var var11: java.lang.String;
      label44: {
         val `$result`: Any = `$continuation`.result;
         var9 = IntrinsicsKt.getCOROUTINE_SUSPENDED();
         switch ($continuation.label) {
            case 0:
               ResultKt.throwOnFailure(`$result`);
               userHome = this.getUserWebdavHome(userNameSpace);
               legadoHome = userHome;
               if (latestZipFilePath != null) {
                  var11 = latestZipFilePath;
                  break label44;
               }

               `$continuation`.L$0 = this;
               `$continuation`.L$1 = userNameSpace;
               `$continuation`.L$2 = userHome;
               `$continuation`.L$3 = userHome;
               `$continuation`.label = 1;
               var11 = (java.lang.String)this.getLastBackFileFromWebdav(userNameSpace, `$continuation`);
               if (var11 === var9) {
                  return var9;
               }
               break;
            case 1:
               legadoHome = `$continuation`.L$3 as java.lang.String;
               userHome = `$continuation`.L$2 as java.lang.String;
               userNameSpace = `$continuation`.L$1 as java.lang.String;
               this = `$continuation`.L$0 as BookController;
               ResultKt.throwOnFailure(`$result`);
               var11 = (java.lang.String)`$result`;
               break;
            case 2:
               ResultKt.throwOnFailure(`$result`);
               return Boxing.boxBoolean(`$result` != null);
            default:
               throw new IllegalStateException("call to 'resume' before 'invoke' with coroutine");
         }

         var11 = var11;
      }

      if (var11 == null) {
         legadoHome = "$userHome${File.separator}legado";
      } else if (StringsKt.indexOf$default(var11, "legado", 0, false, 6, null) > 0) {
         legadoHome = "$userHome${File.separator}legado";
      }

      `$continuation`.L$0 = null;
      `$continuation`.L$1 = null;
      `$continuation`.L$2 = null;
      `$continuation`.L$3 = null;
      `$continuation`.label = 2;
      var11 = (java.lang.String)this.createUserBackup(userNameSpace, legadoHome, var11, `$continuation`);
      return if (var11 === var9) var9 else Boxing.boxBoolean(var11 != null);
   }

   public suspend fun createUserBackup(userNameSpace: String, backupDir: String, latestZipFilePath: String? = ...): File? {
      label59: {
         val today: java.lang.String = new SimpleDateFormat("yyyy-MM-dd").format(Boxing.boxLong(System.currentTimeMillis()));
         val var21: java.lang.String = ExtKt.getWorkDir("storage", "data", userNameSpace, Intrinsics.stringPlus("backup", today));
         val var22: File = new File(var21);
         ExtKt.deleteRecursively(var22);

         label56: {
            label55: {
               try {
                  try {
                     if (latestZipFilePath != null && !ExtKt.unzip(new File(latestZipFilePath), var21)) {
                        break label56;
                     }

                     val webdavBooksDir: Array<java.lang.String> = this.getBackupFileNames();

                     for (Object element$iv : var24) {
                        val it: java.lang.String = `element$iv` as java.lang.String;
                        val userDataFile: File = new File(ExtKt.getWorkDir("storage", "data", userNameSpace, `element$iv` as java.lang.String));
                        if (userDataFile.exists()) {
                           val var30: File = new File("$var21${File.separator}$it");
                           ExtKt.deleteRecursively(var30);
                           FilesKt.copyRecursively$default(userDataFile, var30, false, null, 6, null);
                        }
                     }

                     val var25: File = new File(ExtKt.getWorkDir("storage", "data", userNameSpace, "webdav", "books"));
                     if (var25.exists()) {
                        val var27: File = new File("$var21${File.separator}books");
                        ExtKt.deleteRecursively(var27);
                        FilesKt.copyRecursively$default(var25, var27, false, null, 6, null);
                     }

                     val var28: File = FileUtils.INSTANCE.createFileWithReplace("$backupDir${File.separator}backup$today.zip");
                     val var10000: ZipUtils = ZipUtils.INSTANCE;
                     val var29: Array<File> = var22.listFiles();
                     if (!var10000.zipFiles(CollectionsKt.arrayListOf(Arrays.copyOf(var29, var29.length)), var28, null)) {
                        ;
                     }
                     break label55;
                  } catch (var17: Exception) {
                     var17.printStackTrace();
                  }
               } catch (var18: java.lang.Throwable) {
                  ExtKt.deleteRecursively(var22);
               }

               ExtKt.deleteRecursively(var22);
            }

            ExtKt.deleteRecursively(var22);
         }

         ExtKt.deleteRecursively(var22);
      }
   }

   public suspend fun getLastBackFileFromWebdav(userNameSpace: String): String? {
      val userHome: java.lang.String = this.getUserWebdavHome(userNameSpace);
      var legadoHome: File = new File("$userHome${File.separator}legado");
      if (!legadoHome.exists()) {
         legadoHome = new File(userHome);
      }

      if (!legadoHome.exists()) {
         return null;
      } else {
         var latestZipFile: Any = null;
         val zipFileReg: Regex = new Regex("^backup[0-9-]+.zip$", RegexOption.IGNORE_CASE);
         val `$i$f$forEach`: Array<File> = legadoHome.listFiles();
         if (`$i$f$forEach`.length > 1) {
            ArraysKt.sortWith(`$i$f$forEach`, new BookController$getLastBackFileFromWebdav$lambda-16$$inlined$sortByDescending$1<>());
         }

         val var17: Array<Any> = `$i$f$forEach`;
         val var18: Int = `$i$f$forEach`.length;

         for (int it = 0; it < var18; it++) {
            val itx: File = var17[it] as File;
            val var21: java.lang.String = (var17[it] as File).getName();
            if (zipFileReg.matches(var21)) {
               latestZipFile = itx.toString();
            }
         }

         return latestZipFile;
      }
   }

   public suspend fun bookSourceDebugSSE(context: RoutingContext) {
      var `$continuation`: Continuation;
      label72: {
         if (`$completion` is <unrepresentable>) {
            `$continuation` = `$completion` as <unrepresentable>;
            if (((`$completion` as <unrepresentable>).label and Integer.MIN_VALUE) != 0) {
               `$continuation`.label -= Integer.MIN_VALUE;
               break label72;
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
               return this.this$0.bookSourceDebugSSE(null, this);
            }
         };
      }

      var response: HttpServerResponse;
      label81: {
         val `$result`: Any = `$continuation`.result;
         val var14: Any = IntrinsicsKt.getCOROUTINE_SUSPENDED();
         var returnData: ReturnData;
         var var10000: Any;
         switch ($continuation.label) {
            case 0:
               ResultKt.throwOnFailure(`$result`);
               returnData = new ReturnData();
               response = context.response().putHeader("Content-Type", "text/event-stream").putHeader("Cache-Control", "no-cache").setChunked(true);
               `$continuation`.L$0 = this;
               `$continuation`.L$1 = context;
               `$continuation`.L$2 = returnData;
               `$continuation`.L$3 = response;
               `$continuation`.label = 1;
               var10000 = this.checkAuth(context, `$continuation`);
               if (var10000 === var14) {
                  return var14;
               }
               break;
            case 1:
               response = `$continuation`.L$3 as HttpServerResponse;
               returnData = `$continuation`.L$2 as ReturnData;
               context = `$continuation`.L$1 as RoutingContext;
               this = `$continuation`.L$0 as BookController;
               ResultKt.throwOnFailure(`$result`);
               var10000 = `$result`;
               break;
            case 2:
               response = `$continuation`.L$0 as HttpServerResponse;
               ResultKt.throwOnFailure(`$result`);
               break label81;
            default:
               throw new IllegalStateException("call to 'resume' before 'invoke' with coroutine");
         }

         if (!var10000 as java.lang.Boolean) {
            response.write("event: error\n");
            response.end("data: ${ExtKt.jsonEncode(ReturnData.setData$default(returnData, "NEED_LOGIN", null, 2, null).setErrorMsg("请登录后使用"), false)}\n\n");
            return Unit.INSTANCE;
         }

         val userNameSpace: java.util.List = context.queryParam("bookSourceUrl");
         var keyword: java.lang.String = CollectionsKt.firstOrNull(userNameSpace);
         val bookSourceUrl: java.lang.String = if (keyword == null) "" else keyword;
         val bookSourceString: java.util.List = context.queryParam("keyword");
         val var16: java.lang.String = CollectionsKt.firstOrNull(bookSourceString);
         keyword = if (var16 == null) "" else var16;
         if (bookSourceUrl.length() == 0) {
            response.write("event: error\n");
            response.end("data: ${ExtKt.jsonEncode(returnData.setErrorMsg("未配置书源"), false)}\n\n");
            return Unit.INSTANCE;
         }

         if (keyword.length() == 0) {
            response.write("event: error\n");
            response.end("data: ${ExtKt.jsonEncode(returnData.setErrorMsg("请输入搜索关键词"), false)}\n\n");
            return Unit.INSTANCE;
         }

         val var19: java.lang.String = this.getUserNameSpace(context);
         val var22: java.lang.String = this.getBookSourceStringBySourceURLOpt(bookSourceUrl, var19);
         if (var22 == null || var22.length() == 0) {
            response.write("event: error\n");
            response.end("data: ${ExtKt.jsonEncode(returnData.setErrorMsg("未配置书源"), false)}\n\n");
            return Unit.INSTANCE;
         }

         context.request().connection().closeHandler(BookController::bookSourceDebugSSE$lambda-18);
         BookControllerKt.access$getLogger$p().info("bookSourceDebugSSE bookSource: {} keyword: {}", var22, keyword);
         val var25: Debugger = new Debugger((new Function1<java.lang.String, Unit>(response) {
            {
               super(1);
               this.$response = `$response`;
            }

            public final void invoke(@NotNull java.lang.String msg) {
               this.$response.write("data: ${ExtKt.jsonEncode(MapsKt.mapOf(TuplesKt.to("msg", msg)), false)}\n\n");
            }
         }) as (java.lang.String?) -> Unit);
         val var26: WebBook = new WebBook(var22, false, null, var19, 6, null);
         `$continuation`.L$0 = response;
         `$continuation`.L$1 = null;
         `$continuation`.L$2 = null;
         `$continuation`.L$3 = null;
         `$continuation`.label = 2;
         if (var25.startDebug(var26, keyword, `$continuation`) === var14) {
            return var14;
         }
      }

      response.write("event: end\n");
      response.end("data: ${ExtKt.jsonEncode(MapsKt.mapOf(TuplesKt.to("end", Boxing.boxBoolean(true))), false)}\n\n");
      return Unit.INSTANCE;
   }

   public suspend fun cacheBookSSE(context: RoutingContext) {
      var `$continuation`: Continuation;
      label127: {
         if (`$completion` is <unrepresentable>) {
            `$continuation` = `$completion` as <unrepresentable>;
            if (((`$completion` as <unrepresentable>).label and Integer.MIN_VALUE) != 0) {
               `$continuation`.label -= Integer.MIN_VALUE;
               break label127;
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
               return this.this$0.cacheBookSSE(null, this);
            }
         };
      }

      var response: HttpServerResponse;
      var cachedChapterContentSet: ObjectRef;
      var successCount: IntRef;
      var failedCount: IntRef;
      label148: {
         var refresh: Int;
         var concurrentCount: Int;
         var userNameSpace: ObjectRef;
         var bookInfo: Book;
         var bookSource: ObjectRef;
         var chapterList: ObjectRef;
         var var18: ObjectRef;
         var var22: Any;
         var var10000: Any;
         label142: {
            var returnData: ReturnData;
            label132: {
               val `$result`: Any = `$continuation`.result;
               var22 = IntrinsicsKt.getCOROUTINE_SUSPENDED();
               switch ($continuation.label) {
                  case 0:
                     ResultKt.throwOnFailure(`$result`);
                     returnData = new ReturnData();
                     response = context.response().putHeader("Content-Type", "text/event-stream").putHeader("Cache-Control", "no-cache").setChunked(true);
                     `$continuation`.L$0 = this;
                     `$continuation`.L$1 = context;
                     `$continuation`.L$2 = returnData;
                     `$continuation`.L$3 = response;
                     `$continuation`.label = 1;
                     var10000 = this.checkAuth(context, `$continuation`);
                     if (var10000 === var22) {
                        return var22;
                     }
                     break;
                  case 1:
                     response = `$continuation`.L$3 as HttpServerResponse;
                     returnData = `$continuation`.L$2 as ReturnData;
                     context = `$continuation`.L$1 as RoutingContext;
                     this = `$continuation`.L$0 as BookController;
                     ResultKt.throwOnFailure(`$result`);
                     var10000 = `$result`;
                     break;
                  case 2:
                     concurrentCount = `$continuation`.I$1;
                     refresh = `$continuation`.I$0;
                     var18 = `$continuation`.L$7 as ObjectRef;
                     bookSource = `$continuation`.L$6 as ObjectRef;
                     bookInfo = `$continuation`.L$5 as Book;
                     userNameSpace = `$continuation`.L$4 as ObjectRef;
                     response = `$continuation`.L$3 as HttpServerResponse;
                     returnData = `$continuation`.L$2 as ReturnData;
                     context = `$continuation`.L$1 as RoutingContext;
                     this = `$continuation`.L$0 as BookController;
                     ResultKt.throwOnFailure(`$result`);
                     var10000 = `$result`;
                     break label132;
                  case 3:
                     concurrentCount = `$continuation`.I$1;
                     refresh = `$continuation`.I$0;
                     var18 = `$continuation`.L$7 as ObjectRef;
                     chapterList = `$continuation`.L$6 as ObjectRef;
                     bookSource = `$continuation`.L$5 as ObjectRef;
                     bookInfo = `$continuation`.L$4 as Book;
                     userNameSpace = `$continuation`.L$3 as ObjectRef;
                     response = `$continuation`.L$2 as HttpServerResponse;
                     context = `$continuation`.L$1 as RoutingContext;
                     this = `$continuation`.L$0 as BookController;
                     ResultKt.throwOnFailure(`$result`);
                     var10000 = `$result`;
                     break label142;
                  case 4:
                     failedCount = `$continuation`.L$3 as IntRef;
                     successCount = `$continuation`.L$2 as IntRef;
                     cachedChapterContentSet = `$continuation`.L$1 as ObjectRef;
                     response = `$continuation`.L$0 as HttpServerResponse;
                     ResultKt.throwOnFailure(`$result`);
                     break label148;
                  default:
                     throw new IllegalStateException("call to 'resume' before 'invoke' with coroutine");
               }

               if (!var10000 as java.lang.Boolean) {
                  response.write("event: error\n");
                  response.end(
                     "data: ${ExtKt.jsonEncode(ReturnData.setData$default(returnData, "NEED_LOGIN", null, 2, null).setErrorMsg("请登录后使用"), false)}\n\n"
                  );
                  return Unit.INSTANCE;
               }

               val var23: java.lang.String;
               if (context.request().method() === HttpMethod.POST) {
                  val var34: java.lang.String = context.getBodyAsJson().getString("url");
                  val var27: java.lang.String = if (var34 == null) context.getBodyAsJson().getString("bookUrl") else var34;
                  var23 = if (var27 == null) "" else var27;
                  val var28: Int = context.getBodyAsJson().getInteger("refresh", Boxing.boxInt(0));
                  refresh = var28.intValue();
                  val var29: Int = context.getBodyAsJson().getInteger("concurrentCount", Boxing.boxInt(24));
                  concurrentCount = var29.intValue();
               } else {
                  val var35: java.util.List = context.queryParam("url");
                  val var30: java.lang.String = CollectionsKt.firstOrNull(var35);
                  var23 = if (var30 == null) "" else var30;
                  val var36: java.util.List = context.queryParam("refresh");
                  val var31: java.lang.String = CollectionsKt.firstOrNull(var36);
                  val var52: Int;
                  if (var31 == null) {
                     var52 = 0;
                  } else {
                     val var37: Int = Boxing.boxInt(Integer.parseInt(var31));
                     var52 = if (var37 == null) 0 else var37;
                  }

                  refresh = var52;
                  val var38: java.util.List = context.queryParam("concurrentCount");
                  val var32: java.lang.String = CollectionsKt.firstOrNull(var38);
                  val var53: Int;
                  if (var32 == null) {
                     var53 = 24;
                  } else {
                     val var39: Int = Boxing.boxInt(Integer.parseInt(var32));
                     var53 = if (var39 == null) 24 else var39;
                  }

                  concurrentCount = var53;
               }

               if (var23.length() == 0) {
                  response.write("event: error\n");
                  response.end("data: ${ExtKt.jsonEncode(returnData.setErrorMsg("请输入书籍链接"), false)}\n\n");
                  return Unit.INSTANCE;
               }

               userNameSpace = new ObjectRef();
               userNameSpace.element = (T)this.getUserNameSpace(context);
               bookInfo = this.getShelfBookByURL(var23, userNameSpace.element as java.lang.String);
               if (bookInfo == null) {
                  response.write("event: error\n");
                  response.end("data: ${ExtKt.jsonEncode(returnData.setErrorMsg("请先加入书架"), false)}\n\n");
                  return Unit.INSTANCE;
               }

               if (bookInfo.isLocalBook()) {
                  response.write("event: error\n");
                  response.end("data: ${ExtKt.jsonEncode(returnData.setErrorMsg("本地书籍无需缓存"), false)}\n\n");
                  return Unit.INSTANCE;
               }

               bookSource = new ObjectRef();
               var18 = bookSource;
               val var10002: java.lang.String = bookInfo.getOrigin();
               `$continuation`.L$0 = this;
               `$continuation`.L$1 = context;
               `$continuation`.L$2 = returnData;
               `$continuation`.L$3 = response;
               `$continuation`.L$4 = userNameSpace;
               `$continuation`.L$5 = bookInfo;
               `$continuation`.L$6 = bookSource;
               `$continuation`.L$7 = bookSource;
               `$continuation`.I$0 = refresh;
               `$continuation`.I$1 = concurrentCount;
               `$continuation`.label = 2;
               var10000 = getBookSourceString$default(this, context, var10002, false, `$continuation`, 4, null);
               if (var10000 === var22) {
                  return var22;
               }
            }

            var18.element = (T)var10000;
            if (bookSource.element as java.lang.CharSequence == null || (bookSource.element as java.lang.CharSequence).length() == 0) {
               response.write("event: error\n");
               response.end("data: ${ExtKt.jsonEncode(returnData.setErrorMsg("未配置书源"), false)}\n\n");
               return Unit.INSTANCE;
            }

            chapterList = new ObjectRef();
            var18 = chapterList;
            val var54: java.lang.String = bookSource.element as java.lang.String;
            val var10004: java.lang.String = userNameSpace.element as java.lang.String;
            `$continuation`.L$0 = this;
            `$continuation`.L$1 = context;
            `$continuation`.L$2 = response;
            `$continuation`.L$3 = userNameSpace;
            `$continuation`.L$4 = bookInfo;
            `$continuation`.L$5 = bookSource;
            `$continuation`.L$6 = chapterList;
            `$continuation`.L$7 = chapterList;
            `$continuation`.I$0 = refresh;
            `$continuation`.I$1 = concurrentCount;
            `$continuation`.label = 3;
            var10000 = getLocalChapterList$default(this, bookInfo, var54, false, var10004, false, null, `$continuation`, 48, null);
            if (var10000 === var22) {
               return var22;
            }
         }

         var18.element = (T)var10000;
         cachedChapterContentSet = new ObjectRef();
         cachedChapterContentSet.element = (T)((new LinkedHashSet()) as java.util.Set);
         if (refresh <= 0) {
            cachedChapterContentSet.element = (T)this.getCachedChapterContentSet(bookInfo, userNameSpace.element as java.lang.String);
         }

         val var47: File = this.getChapterCacheDir(bookInfo, userNameSpace.element as java.lang.String);
         val isEnd: BooleanRef = new BooleanRef();
         successCount = new IntRef();
         failedCount = new IntRef();
         context.request().connection().closeHandler(BookController::cacheBookSSE$lambda-19);
         val var26: Int = if (concurrentCount > 0) concurrentCount else 24;
         BookControllerKt.access$getLogger$p()
            .info("cacheBookSSE concurrentCount: {} refresh: {}", Boxing.boxInt(if (concurrentCount > 0) concurrentCount else 24), Boxing.boxInt(refresh));
         val var10003: Int = (chapterList.element as java.util.List).size();
         val var55: Function3 = (
            new Function3<CoroutineScope, Integer, Continuation<? super Object>, Object>(
               cachedChapterContentSet, chapterList, bookSource, this, userNameSpace, bookInfo, var47, successCount, isEnd, failedCount, null
            ) {
               int I$1;
               Object L$1;
               int label;

               {
                  super(3, `$completion`);
                  this.$cachedChapterContentSet = `$cachedChapterContentSet`;
                  this.$chapterList = `$chapterList`;
                  this.$bookSource = `$bookSource`;
                  this.this$0 = `$receiver`;
                  this.$userNameSpace = `$userNameSpace`;
                  this.$bookInfo = `$bookInfo`;
                  this.$localCacheDir = `$localCacheDir`;
                  this.$successCount = `$successCount`;
                  this.$isEnd = `$isEnd`;
                  this.$failedCount = `$failedCount`;
               }

               // $VF: Handled exception range with multiple entry points by splitting it
               // $VF: Duplicated exception handlers to handle obfuscated exceptions
               @Nullable
               @Override
               public final Object invokeSuspend(@NotNull Object $result) {
                  var it: Int;
                  var chapterIndex: Int;
                  label70: {
                     val var12: Any = IntrinsicsKt.getCOROUTINE_SUSPENDED();
                     var `$this$limitConcurrent`: CoroutineScope;
                     var chapterInfo: BookChapter;
                     var var10000: Any;
                     switch (this.label) {
                        case 0:
                           ResultKt.throwOnFailure(`$result`);
                           `$this$limitConcurrent` = this.L$0 as CoroutineScope;
                           it = this.I$0;
                           if (this.$cachedChapterContentSet.element.contains(Boxing.boxInt(this.I$0))) {
                              return Boxing.boxInt(it);
                           }

                           chapterIndex = it;
                           chapterInfo = this.$chapterList.element.get(it);

                           try {
                              var nextChapterUrl: java.lang.String = null;
                              if (chapterIndex + 1 < this.$chapterList.element.size()) {
                                 nextChapterUrl = this.$chapterList.element.get(chapterIndex + 1).getUrl();
                              }

                              var10000 = new WebBook(
                                 this.$bookSource.element, this.this$0.getAppConfig().getDebugLog(), null, this.$userNameSpace.element, 4, null
                              );
                              val var10001: Book = this.$bookInfo;
                              val var10004: Continuation = this;
                              this.L$0 = `$this$limitConcurrent`;
                              this.L$1 = chapterInfo;
                              this.I$0 = it;
                              this.I$1 = chapterIndex;
                              this.label = 1;
                              var10000 = (WebBook)var10000.getBookContent(var10001, chapterInfo, nextChapterUrl, var10004);
                           } catch (var17: Exception) {
                              this.$isEnd.element = true;
                              val var19: Int = this.$failedCount.element++;
                              return Boxing.boxInt(it);
                           }

                           if (var10000 === var12) {
                              return var12;
                           }
                           break;
                        case 1:
                           chapterIndex = this.I$1;
                           it = this.I$0;
                           chapterInfo = this.L$1 as BookChapter;
                           `$this$limitConcurrent` = this.L$0 as CoroutineScope;

                           try {
                              ResultKt.throwOnFailure(`$result`);
                              var10000 = (WebBook)`$result`;
                              break;
                           } catch (var15: Exception) {
                              this.$isEnd.element = true;
                              val var18: Int = this.$failedCount.element++;
                              return Boxing.boxInt(it);
                           }
                        case 2:
                           chapterIndex = this.I$1;
                           it = this.I$0;

                           try {
                              ResultKt.throwOnFailure(`$result`);
                              break label70;
                           } catch (var16: Exception) {
                              this.$isEnd.element = true;
                              val content: Int = this.$failedCount.element++;
                              return Boxing.boxInt(it);
                           }
                        default:
                           throw new IllegalStateException("call to 'resume' before 'invoke' with coroutine");
                     }

                     try {
                        val var22: java.lang.String = var10000 as java.lang.String;
                        FilesKt.writeText$default(
                           new File("${this.$localCacheDir.getAbsolutePath()}${File.separator}$chapterIndex.txt"), var10000 as java.lang.String, null, 2, null
                        );
                        val var26: BookHelp = BookHelp.INSTANCE;
                        val var10: Any = BookSource.Companion.fromJson-IoAF18A(this.$bookSource.element);
                        val var9: BookSource = (if (Result.isFailure-impl(var10)) null else var10) as BookSource;
                        val var10002: BookSource = if (var9 == null)
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
                           var9;
                        val var10003: Book = this.$bookInfo;
                        val var10006: Continuation = this;
                        this.L$0 = null;
                        this.L$1 = null;
                        this.I$0 = it;
                        this.I$1 = chapterIndex;
                        this.label = 2;
                        var10000 = (WebBook)var26.saveImages(`$this$limitConcurrent`, var10002, var10003, chapterInfo, var22, var10006);
                     } catch (var14: Exception) {
                        this.$isEnd.element = true;
                        val var21: Int = this.$failedCount.element++;
                        return Boxing.boxInt(it);
                     }

                     if (var10000 === var12) {
                        return var12;
                     }
                  }

                  try {
                     val var24: Int = this.$successCount.element++;
                     this.$cachedChapterContentSet.element.add(Boxing.boxInt(chapterIndex));
                  } catch (var13: Exception) {
                     this.$isEnd.element = true;
                     val var23: Int = this.$failedCount.element++;
                  }

                  return Boxing.boxInt(it);
               }

               @Nullable
               public final Object invoke(@NotNull CoroutineScope p1, int p2, @Nullable Continuation<Object> p3) {
                  val var4: Function3 = new <anonymous constructor>(
                     this.$cachedChapterContentSet,
                     this.$chapterList,
                     this.$bookSource,
                     this.this$0,
                     this.$userNameSpace,
                     this.$bookInfo,
                     this.$localCacheDir,
                     this.$successCount,
                     this.$isEnd,
                     this.$failedCount,
                     p3
                  );
                  var4.L$0 = p1;
                  var4.I$0 = p2;
                  return var4.invokeSuspend(Unit.INSTANCE);
               }
            }
         ) as Function3;
         val var10005: Function2 = (
            new Function2<ArrayList<Object>, Integer, java.lang.Boolean>(isEnd, cachedChapterContentSet, successCount, failedCount, response) {
               {
                  super(2);
                  this.$isEnd = `$isEnd`;
                  this.$cachedChapterContentSet = `$cachedChapterContentSet`;
                  this.$successCount = `$successCount`;
                  this.$failedCount = `$failedCount`;
                  this.$response = `$response`;
               }

               public final boolean invoke(@NotNull ArrayList<Object> list, int loopCount) {
                  val var10000: Boolean;
                  if (this.$isEnd.element) {
                     var10000 = false;
                  } else {
                     val result: java.util.Map = MapsKt.mapOf(
                        new Pair[]{
                           TuplesKt.to("cachedCount", this.$cachedChapterContentSet.element.size()),
                           TuplesKt.to("successCount", this.$successCount.element),
                           TuplesKt.to("failedCount", this.$failedCount.element)
                        }
                     );
                     this.$response.write("data: ${ExtKt.jsonEncode(result, false)}\n\n");
                     BookControllerKt.access$getLogger$p().info("Loop: {} list.size: {} result: {}", new Object[]{loopCount, list.size(), result});
                     var10000 = true;
                  }

                  return var10000;
               }
            }
         ) as Function2;
         `$continuation`.L$0 = response;
         `$continuation`.L$1 = cachedChapterContentSet;
         `$continuation`.L$2 = successCount;
         `$continuation`.L$3 = failedCount;
         `$continuation`.L$4 = null;
         `$continuation`.L$5 = null;
         `$continuation`.L$6 = null;
         `$continuation`.L$7 = null;
         `$continuation`.label = 4;
         if (this.limitConcurrent(var26, 0, var10003, var55, var10005, `$continuation`) === var22) {
            return var22;
         }
      }

      response.write("event: end\n");
      response.end(
         "data: ${ExtKt.jsonEncode(
            MapsKt.mapOf(
               new Pair[]{
                  TuplesKt.to("cachedCount", Boxing.boxInt((cachedChapterContentSet.element as java.util.Set).size())),
                  TuplesKt.to("successCount", Boxing.boxInt(successCount.element)),
                  TuplesKt.to("failedCount", Boxing.boxInt(failedCount.element))
               }
            ),
            false
         )}\n\n"
      );
      return Unit.INSTANCE;
   }

   public suspend fun cacheBookOnServer(context: RoutingContext): ReturnData {
      var `$continuation`: Continuation;
      label33: {
         if (`$completion` is <unrepresentable>) {
            `$continuation` = `$completion` as <unrepresentable>;
            if (((`$completion` as <unrepresentable>).label and Integer.MIN_VALUE) != 0) {
               `$continuation`.label -= Integer.MIN_VALUE;
               break label33;
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
               return this.this$0.cacheBookOnServer(null, this);
            }
         };
      }

      val `$result`: Any = `$continuation`.result;
      val var10: Any = IntrinsicsKt.getCOROUTINE_SUSPENDED();
      var returnData: ReturnData;
      var var10000: Any;
      switch ($continuation.label) {
         case 0:
            ResultKt.throwOnFailure(`$result`);
            returnData = new ReturnData();
            `$continuation`.L$0 = this;
            `$continuation`.L$1 = context;
            `$continuation`.L$2 = returnData;
            `$continuation`.label = 1;
            var10000 = this.checkAuth(context, `$continuation`);
            if (var10000 === var10) {
               return var10;
            }
            break;
         case 1:
            returnData = `$continuation`.L$2 as ReturnData;
            context = `$continuation`.L$1 as RoutingContext;
            this = `$continuation`.L$0 as BookController;
            ResultKt.throwOnFailure(`$result`);
            var10000 = `$result`;
            break;
         default:
            throw new IllegalStateException("call to 'resume' before 'invoke' with coroutine");
      }

      if (!var10000 as java.lang.Boolean) {
         return ReturnData.setData$default(returnData, "NEED_LOGIN", null, 2, null).setErrorMsg("请登录后使用");
      } else {
         val exceptionHandler: JsonArray = context.getBodyAsJson().getJsonArray("bookUrlList");
         val bookUrlList: JsonArray = if (exceptionHandler == null) new JsonArray() else exceptionHandler;
         if (bookUrlList.size() <= 0) {
            return returnData.setErrorMsg("请输入书籍链接");
         } else {
            val var11: CoroutineExceptionHandler = new BookController$cacheBookOnServer$$inlined$CoroutineExceptionHandler$1(CoroutineExceptionHandler.Key);
            val var12: java.lang.String = this.getUserNameSpace(context);
            BuildersKt.launch$default(
               this,
               new MDCContext(null, 1, null).plus(Dispatchers.getIO()).plus(var11),
               null,
               (new Function2<CoroutineScope, Continuation<? super Unit>, Object>(this, bookUrlList, var12, null) {
                  int label;

                  {
                     super(2, `$completionx`);
                     this.this$0 = `$receiver`;
                     this.$bookUrlList = `$bookUrlList`;
                     this.$userNameSpace = `$userNameSpace`;
                  }

                  @Nullable
                  @Override
                  public final Object invokeSuspend(@NotNull Object $result) {
                     val var2: Any = IntrinsicsKt.getCOROUTINE_SUSPENDED();
                     switch (this.label) {
                        case 0:
                           ResultKt.throwOnFailure(`$result`);
                           val var10000: BookController = this.this$0;
                           val var10001: JsonArray = this.$bookUrlList;
                           val var10002: java.lang.String = this.$userNameSpace;
                           val var10003: Continuation = this;
                           this.label = 1;
                           if (var10000.cacheBookOnServer(var10001, var10002, var10003) === var2) {
                              return var2;
                           }
                           break;
                        case 1:
                           ResultKt.throwOnFailure(`$result`);
                           break;
                        default:
                           throw new IllegalStateException("call to 'resume' before 'invoke' with coroutine");
                     }

                     return Unit.INSTANCE;
                  }

                  @NotNull
                  @Override
                  public final Continuation<Unit> create(@Nullable Object value, @NotNull Continuation<?> $completion) {
                     return new <anonymous constructor>(this.this$0, this.$bookUrlList, this.$userNameSpace, `$completion`);
                  }

                  @Nullable
                  public final Object invoke(@NotNull CoroutineScope p1, @Nullable Continuation<? super Unit> p2) {
                     return (this.create(p1, p2) as <unrepresentable>).invokeSuspend(Unit.INSTANCE);
                  }
               }) as Function2,
               2,
               null
            );
            return ReturnData.setData$default(returnData, "", null, 2, null);
         }
      }
   }

    /**
     * MANUALLY RECONSTRUCTED from CFR + BookController.class (reader-pro-3.2.14.jar)
     * Overload: cacheBookOnServer(bookUrlList, userNameSpace)
     * Original Vineflower output: Couldn't be decompiled
     */
    public suspend fun cacheBookOnServer(bookUrlList: JsonArray, userNameSpace: String) {
        for (bi in 0 until bookUrlList.size()) {
            val bookUrl = bookUrlList.getString(bi) ?: continue
            try {
                val book = getShelfBookByURL(bookUrl, userNameSpace) ?: continue
                val bookSource = getBookSourceString(book, userNameSpace) ?: continue
                val chapterList = getLocalChapterList(
                    book, bookSource, false, userNameSpace, getAppConfig().debugLog, null
                )
                val localCacheDir = getChapterCacheDir(book, userNameSpace)
                if (!localCacheDir.exists()) localCacheDir.mkdirs()
                val cachedChapterContentSet = linkedSetOf<Int>()
                localCacheDir.listFiles()?.forEach { f ->
                    val n = f.nameWithoutExtension.toIntOrNull()
                    if (n != null && f.extension.equals("txt", true)) {
                        cachedChapterContentSet.add(n)
                    }
                }
                for (chapterIndex in chapterList.indices) {
                    if (chapterIndex in cachedChapterContentSet) continue
                    val chapterInfo = chapterList[chapterIndex]
                    val nextChapterUrl =
                        if (chapterIndex + 1 < chapterList.size) chapterList[chapterIndex + 1].url
                        else null
                    try {
                        val content = WebBook(
                            bookSource, getAppConfig().debugLog, null, userNameSpace
                        ).getBookContent(book, chapterInfo, nextChapterUrl)
                        File(localCacheDir, "$chapterIndex.txt").writeText(content)
                        val src = BookSource.fromJson(bookSource).getOrNull() ?: BookSource()
                        BookHelp.saveImages(this, src, book, chapterInfo, content)
                        cachedChapterContentSet.add(chapterIndex)
                    } catch (e: Exception) {
                        BookControllerKt.access$getLogger$p().info("cacheBookOnServer error: {}", e.message)
                    }
                }
                BookControllerKt.access$getLogger$p().info("缓存书籍完成: {}", book)
            } catch (e: Exception) {
                BookControllerKt.access$getLogger$p().info("cacheBookOnServer error: {}", e.message)
            }
        }
    }

   public suspend fun deleteBookCache(context: RoutingContext): ReturnData {
      var `$continuation`: Continuation;
      label61: {
         if (`$completion` is <unrepresentable>) {
            `$continuation` = `$completion` as <unrepresentable>;
            if (((`$completion` as <unrepresentable>).label and Integer.MIN_VALUE) != 0) {
               `$continuation`.label -= Integer.MIN_VALUE;
               break label61;
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
               return this.this$0.deleteBookCache(null, this);
            }
         };
      }

      val `$result`: Any = `$continuation`.result;
      val var10: Any = IntrinsicsKt.getCOROUTINE_SUSPENDED();
      var returnData: ReturnData;
      var var10000: Any;
      switch ($continuation.label) {
         case 0:
            ResultKt.throwOnFailure(`$result`);
            returnData = new ReturnData();
            `$continuation`.L$0 = this;
            `$continuation`.L$1 = context;
            `$continuation`.L$2 = returnData;
            `$continuation`.label = 1;
            var10000 = this.checkAuth(context, `$continuation`);
            if (var10000 === var10) {
               return var10;
            }
            break;
         case 1:
            returnData = `$continuation`.L$2 as ReturnData;
            context = `$continuation`.L$1 as RoutingContext;
            this = `$continuation`.L$0 as BookController;
            ResultKt.throwOnFailure(`$result`);
            var10000 = `$result`;
            break;
         default:
            throw new IllegalStateException("call to 'resume' before 'invoke' with coroutine");
      }

      if (!var10000 as java.lang.Boolean) {
         return ReturnData.setData$default(returnData, "NEED_LOGIN", null, 2, null).setErrorMsg("请登录后使用");
      } else {
         val var11: java.lang.String;
         if (context.request().method() === HttpMethod.POST) {
            val bookInfo: java.lang.String = context.getBodyAsJson().getString("url");
            val userNameSpace: java.lang.String = if (bookInfo == null) context.getBodyAsJson().getString("bookUrl") else bookInfo;
            var11 = if (userNameSpace == null) "" else userNameSpace;
         } else {
            val var15: java.util.List = context.queryParam("url");
            val var12: java.lang.String = CollectionsKt.firstOrNull(var15);
            var11 = if (var12 == null) "" else var12;
         }

         if (var11.length() == 0) {
            return returnData.setErrorMsg("请输入书籍链接");
         } else {
            val var14: java.lang.String = this.getUserNameSpace(context);
            val var17: Book = this.getShelfBookByURL(var11, var14);
            if (var17 == null) {
               return returnData.setErrorMsg("请先加入书架");
            } else if (var17.isLocalBook()) {
               return returnData.setErrorMsg("本地书籍无需删除缓存");
            } else {
               ExtKt.deleteRecursively(this.getChapterCacheDir(var17, var14));
               return ReturnData.setData$default(returnData, "", null, 2, null);
            }
         }
      }
   }

   public suspend fun textToSpeech(context: RoutingContext) {
      var `$continuation`: Continuation;
      label115: {
         if (`$completion` is <unrepresentable>) {
            `$continuation` = `$completion` as <unrepresentable>;
            if (((`$completion` as <unrepresentable>).label and Integer.MIN_VALUE) != 0) {
               `$continuation`.label -= Integer.MIN_VALUE;
               break label115;
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
               return this.this$0.textToSpeech(null, this);
            }
         };
      }

      val `$result`: Any = `$continuation`.result;
      val var15: Any = IntrinsicsKt.getCOROUTINE_SUSPENDED();
      var response: HttpServerResponse;
      var var10000: Any;
      switch ($continuation.label) {
         case 0:
            ResultKt.throwOnFailure(`$result`);
            response = context.response();
            `$continuation`.L$0 = this;
            `$continuation`.L$1 = context;
            `$continuation`.L$2 = response;
            `$continuation`.label = 1;
            var10000 = this.checkAuth(context, `$continuation`);
            if (var10000 === var15) {
               return var15;
            }
            break;
         case 1:
            response = `$continuation`.L$2 as HttpServerResponse;
            context = `$continuation`.L$1 as RoutingContext;
            this = `$continuation`.L$0 as BookController;
            ResultKt.throwOnFailure(`$result`);
            var10000 = `$result`;
            break;
         default:
            throw new IllegalStateException("call to 'resume' before 'invoke' with coroutine");
      }

      if (!var10000 as java.lang.Boolean) {
         response.setStatusCode(403).end("未登录");
         return Unit.INSTANCE;
      } else {
         val text: ObjectRef = new ObjectRef();
         val type: ObjectRef = new ObjectRef();
         val var16: java.lang.String;
         val var17: java.lang.String;
         val var18: java.lang.String;
         val var19: java.lang.String;
         if (context.request().method() === HttpMethod.POST) {
            var exceptionHandler: java.lang.String = context.getBodyAsJson().getString("text");
            text.element = (T)(if (exceptionHandler == null) "" else exceptionHandler);
            exceptionHandler = context.getBodyAsJson().getString("type");
            type.element = (T)(if (exceptionHandler == null) "" else exceptionHandler);
            exceptionHandler = context.getBodyAsJson().getString("voice");
            var16 = if (exceptionHandler == null) "" else exceptionHandler;
            exceptionHandler = context.getBodyAsJson().getString("pitch");
            var17 = if (exceptionHandler == null) "" else exceptionHandler;
            exceptionHandler = context.getBodyAsJson().getString("rate");
            var18 = if (exceptionHandler == null) "" else exceptionHandler;
            exceptionHandler = context.getBodyAsJson().getString("base64");
            var19 = if (exceptionHandler == null) "" else exceptionHandler;
         } else {
            var options: java.util.List = context.queryParam("text");
            var var25: java.lang.String = CollectionsKt.firstOrNull(options);
            text.element = (T)(if (var25 == null) "" else var25);
            options = context.queryParam("type");
            var25 = CollectionsKt.firstOrNull(options);
            type.element = (T)(if (var25 == null) "" else var25);
            options = context.queryParam("voice");
            var25 = CollectionsKt.firstOrNull(options);
            var16 = if (var25 == null) "" else var25;
            options = context.queryParam("pitch");
            var25 = CollectionsKt.firstOrNull(options);
            var17 = if (var25 == null) "" else var25;
            options = context.queryParam("rate");
            var25 = CollectionsKt.firstOrNull(options);
            var18 = if (var25 == null) "" else var25;
            options = context.queryParam("base64");
            var25 = CollectionsKt.firstOrNull(options);
            var19 = if (var25 == null) "" else var25;
         }

         if (type.element as java.lang.CharSequence == null || (type.element as java.lang.CharSequence).length() == 0) {
            type.element = (T)"edge";
         }

         if (text.element as java.lang.CharSequence == null || (text.element as java.lang.CharSequence).length() == 0) {
            response.setStatusCode(404).end("参数错误");
            return Unit.INSTANCE;
         } else {
            val var33: CoroutineExceptionHandler = new BookController$textToSpeech$$inlined$CoroutineExceptionHandler$1(CoroutineExceptionHandler.Key, response);
            val var42: java.util.Map = MapsKt.mapOf(
               new Pair[]{TuplesKt.to("voice", var16), TuplesKt.to("pitch", var17), TuplesKt.to("rate", var18), TuplesKt.to("base64", var19)}
            );
            BuildersKt.launch$default(
               this,
               new MDCContext(null, 1, null).plus(Dispatchers.getIO()).plus(var33),
               null,
               (new Function2<CoroutineScope, Continuation<? super Unit>, Object>(type, this, response, text, var42, context, null) {
                  int label;

                  {
                     super(2, `$completionx`);
                     this.$type = `$type`;
                     this.this$0 = `$receiver`;
                     this.$response = `$response`;
                     this.$text = `$text`;
                     this.$options = `$options`;
                     this.$context = `$context`;
                  }

                  @Nullable
                  @Override
                  public final Object invokeSuspend(@NotNull Object $result) {
                     val var4: Any = IntrinsicsKt.getCOROUTINE_SUSPENDED();
                     switch (this.label) {
                        case 0:
                           ResultKt.throwOnFailure(`$result`);
                           val var2: java.lang.String = this.$type.element;
                           if (this.$type.element == "edge") {
                              val var8: BookController = this.this$0;
                              val var6: HttpServerResponse = this.$response;
                              val var10: java.lang.String = this.$text.element;
                              val var12: java.util.Map = this.$options;
                              val var14: Continuation = this;
                              this.label = 1;
                              if (var8.ttsByEdge(var6, var10, var12, var14) === var4) {
                                 return var4;
                              }
                           } else if (var2 == "textToSpeechCn") {
                              val var7: BookController = this.this$0;
                              val var5: HttpServerResponse = this.$response;
                              val var9: java.lang.String = this.$text.element;
                              val var11: java.util.Map = this.$options;
                              val var13: Continuation = this;
                              this.label = 2;
                              if (var7.ttsByTextToSpeechCn(var5, var9, var11, var13) === var4) {
                                 return var4;
                              }
                           } else {
                              val var10000: BookController = this.this$0;
                              val var3: HttpServerResponse = this.$response;
                              val var10002: java.lang.String = this.$text.element;
                              val var10003: java.lang.String = this.this$0.getUserNameSpace(this.$context);
                              val var10004: java.util.Map = this.$options;
                              val var10005: Continuation = this;
                              this.label = 3;
                              if (var10000.ttsByApi(var3, var10002, var10003, var10004, var10005) === var4) {
                                 return var4;
                              }
                           }
                           break;
                        case 1:
                           ResultKt.throwOnFailure(`$result`);
                           break;
                        case 2:
                           ResultKt.throwOnFailure(`$result`);
                           break;
                        case 3:
                           ResultKt.throwOnFailure(`$result`);
                           break;
                        default:
                           throw new IllegalStateException("call to 'resume' before 'invoke' with coroutine");
                     }

                     return Unit.INSTANCE;
                  }

                  @NotNull
                  @Override
                  public final Continuation<Unit> create(@Nullable Object value, @NotNull Continuation<?> $completion) {
                     return new <anonymous constructor>(this.$type, this.this$0, this.$response, this.$text, this.$options, this.$context, `$completion`);
                  }

                  @Nullable
                  public final Object invoke(@NotNull CoroutineScope p1, @Nullable Continuation<? super Unit> p2) {
                     return (this.create(p1, p2) as <unrepresentable>).invokeSuspend(Unit.INSTANCE);
                  }
               }) as Function2,
               2,
               null
            );
            return Unit.INSTANCE;
         }
      }
   }

   public suspend fun ttsByEdge(response: HttpServerResponse, text: String, options: Map<String, String>? = ...) {
      var voice: VoiceEnum = VoiceEnum.zh_CN_XiaoxiaoNeural;
      var rate: java.lang.String = "0";
      var pitch: java.lang.String = "0%";
      if (options != null) {
         if (options.containsKey("voice")) {
            val ts: VoiceEnum = VoiceEnum.fromSortName(options.get("voice") as java.lang.String);
            voice = if (ts == null) VoiceEnum.zh_CN_XiaoxiaoNeural else ts;
         }

         if (options.containsKey("rate")) {
            val var13: java.lang.String = options.get("rate") as java.lang.String;
            rate = if (var13 == null) "0" else var13;
         }

         if (options.containsKey("pitch")) {
            pitch = Intrinsics.stringPlus(options.get("pitch") as java.lang.String, "%");
         }
      }

      val var20: ByteArray = TTSService.builder()
         .build()
         .sendText(SSML.builder().synthesisText(text).voice(voice).rate(rate).pitch(pitch).style(TtsStyleEnum.chat).build());
      if (options != null && "1".equals(options.get("base64"))) {
         val returnData: ReturnData = new ReturnData();
         val var10000: HttpServerResponse = response.putHeader("content-type", "application/json; charset=utf-8");
         val var23: java.lang.String = Base64.getEncoder().encodeToString(var20);
         var10000.end(ExtKt.jsonEncode$default(ReturnData.setData$default(returnData, var23, null, 2, null), false, 2, null));
      } else {
         response.putHeader("Content-Type", "audio/mpeg").end(Buffer.buffer(var20));
      }

      return Unit.INSTANCE;
   }

   public fun getHttpTTSByName(name: String, userNameSpace: String): HttpTTS? {
      if (name.length() == 0) {
         return null;
      } else {
         val var10: JsonArray = ExtKt.asJsonArray(this.getUserStorage(userNameSpace, new java.lang.String[]{"httpTTS"}));
         if (var10 == null) {
            return null;
         } else {
            var var12: Int = 0;
            val var5: Int = var10.size();
            if (0 < var5) {
               do {
                  val i: Int = var12++;
                  val var10000: HttpTTS.Companion = HttpTTS.Companion;
                  var var8: java.lang.String = var10.getJsonObject(i).toString();
                  var8 = (java.lang.String)var10000.fromJson-IoAF18A(var8);
                  val httpTTS: HttpTTS = (if (Result.isFailure-impl(var8)) null else var8) as HttpTTS;
                  if (httpTTS != null && httpTTS.getName().equals(name)) {
                     return httpTTS;
                  }
               } while (var12 < var5);
            }

            return null;
         }
      }
   }

   public suspend fun ttsByApi(response: HttpServerResponse, text: String, userNameSpace: String, options: Map<String, String>? = ...) {
      var `$continuation`: Continuation;
      label70: {
         if (`$completion` is <unrepresentable>) {
            `$continuation` = `$completion` as <unrepresentable>;
            if (((`$completion` as <unrepresentable>).label and Integer.MIN_VALUE) != 0) {
               `$continuation`.label -= Integer.MIN_VALUE;
               break label70;
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
               return this.this$0.ttsByApi(null, null, null, null, this);
            }
         };
      }

      val `$result`: Any = `$continuation`.result;
      val var17: Any = IntrinsicsKt.getCOROUTINE_SUSPENDED();
      var httpTTS: HttpTTS;
      var var10000: Any;
      switch ($continuation.label) {
         case 0:
            ResultKt.throwOnFailure(`$result`);
            val voice: java.lang.String = if (options == null) null else options.get("voice") as java.lang.String;
            if (voice == null || voice.length() == 0) {
               response.setStatusCode(404).end();
               return Unit.INSTANCE;
            }

            httpTTS = this.getHttpTTSByName(voice, userNameSpace);
            if (httpTTS == null) {
               response.setStatusCode(404).end();
               return Unit.INSTANCE;
            }

            val var24: Double;
            if (options == null) {
               var24 = 1.0;
            } else {
               val returnData: java.lang.String = options.get("rate") as java.lang.String;
               if (returnData == null) {
                  var24 = 1.0;
               } else {
                  val var12: java.lang.Double = Boxing.boxDouble(java.lang.Double.parseDouble(returnData));
                  var24 = if (var12 == null) 1.0 else var12;
               }
            }

            val var10003: Int = (int)(5 + (var24 - 0.5) * 30);
            `$continuation`.L$0 = response;
            `$continuation`.L$1 = options;
            `$continuation`.L$2 = httpTTS;
            `$continuation`.label = 1;
            var10000 = (HttpServerResponse)this.getSpeakStream(httpTTS, text, var10003, `$continuation`);
            if (var10000 === var17) {
               return var17;
            }
            break;
         case 1:
            httpTTS = `$continuation`.L$2 as HttpTTS;
            options = `$continuation`.L$1 as java.util.Map;
            response = `$continuation`.L$0 as HttpServerResponse;
            ResultKt.throwOnFailure(`$result`);
            var10000 = (HttpServerResponse)`$result`;
            break;
         default:
            throw new IllegalStateException("call to 'resume' before 'invoke' with coroutine");
      }

      val stream: InputStream = var10000 as InputStream;
      if (var10000 as InputStream != null) {
         if (options != null && "1".equals(options.get("base64"))) {
            val var22: ReturnData = new ReturnData();
            var10000 = response.putHeader("content-type", "application/json; charset=utf-8");
            val var23: java.lang.String = Base64.getEncoder().encodeToString(ByteStreamsKt.readBytes(stream));
            var10000.end(ExtKt.jsonEncode$default(ReturnData.setData$default(var22, var23, null, 2, null), false, 2, null));
         } else {
            val var21: java.lang.String = httpTTS.getContentType();
            response.putHeader("Content-Type", if (var21 == null) "audio/mpeg" else var21).end(Buffer.buffer(ByteStreamsKt.readBytes(stream)));
         }
      } else {
         response.setStatusCode(404).end();
      }

      return Unit.INSTANCE;
   }

    /**
     * MANUALLY RECONSTRUCTED from CFR + BookController.class (reader-pro-3.2.14.jar)
     * Original Vineflower output: Couldn't be decompiled
     *
     * Downloads TTS audio stream with retries (up to 5). On recoverable errors may return null
     * (silent audio substitute path is handled by caller).
     */
    public suspend fun getSpeakStream(
        httpTts: HttpTTS,
        speakText: String,
        speechRate: Int
    ): InputStream? {
        var downloadErrorNo = 0
        while (true) {
            try {
                val analyzeUrl = AnalyzeUrl(
                    mUrl = httpTts.url,
                    key = speakText,
                    headerMapF = httpTts.getHeaderMap(true),
                    source = httpTts,
                    debugLog = Debug
                )
                // speechRate is passed as AnalyzeUrl constructor param in bytecode (boxed Int)
                var response = analyzeUrl.getResponseAwait()
                coroutineContext.ensureActive()
                val checkJs = httpTts.loginCheckJs
                if (!checkJs.isNullOrBlank()) {
                    val evaluated = analyzeUrl.evalJS(checkJs, response)
                    response = evaluated as okhttp3.Response
                }
                val contentType = response.headers["Content-Type"]
                if (contentType != null) {
                    if (contentType == "application/json") {
                        throw NoStackTraceException(response.body!!.string())
                    }
                    val ct = httpTts.contentType
                    if (!ct.isNullOrBlank()) {
                        if (!Regex(ct).matches(contentType)) {
                            throw NoStackTraceException(
                                "TTS服务器返回错误：" + response.body!!.string()
                            )
                        }
                    }
                }
                coroutineContext.ensureActive()
                downloadErrorNo = 0
                return response.body!!.byteStream()
            } catch (e: CancellationException) {
                throw e
            } catch (e: Exception) {
                if (e is ScriptException || e is WrappedException) {
                    BookControllerKt.access$getLogger$p().error("js错误\n${e.localizedMessage}", e)
                    throw e
                }
                if (e is SocketTimeoutException || e is ConnectException) {
                    downloadErrorNo++
                    if (downloadErrorNo > 5) {
                        BookControllerKt.access$getLogger$p().error("tts超时或连接错误超过5次\n${e.localizedMessage}", e)
                        throw e
                    }
                    continue
                }
                downloadErrorNo++
                BookControllerKt.access$getLogger$p().error("tts下载错误\n${e.localizedMessage}", e)
                if (downloadErrorNo > 5) {
                    BookControllerKt.access$getLogger$p().error("TTS服务器连续5次错误，已暂停阅读。", e)
                    throw e
                }
                BookControllerKt.access$getLogger$p().error("TTS下载音频出错，使用无声音频代替。\n朗读文本：$speakText")
                return null
            }
        }
    }

   public suspend fun ttsByTextToSpeechCn(response: HttpServerResponse, text: String, options: Map<String, String>? = ...) {
      var `$continuation`: Continuation;
      label36: {
         if (`$completion` is <unrepresentable>) {
            `$continuation` = `$completion` as <unrepresentable>;
            if (((`$completion` as <unrepresentable>).label and Integer.MIN_VALUE) != 0) {
               `$continuation`.label -= Integer.MIN_VALUE;
               break label36;
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
               return this.this$0.ttsByTextToSpeechCn(null, null, null, this);
            }
         };
      }

      val `$result`: Any = `$continuation`.result;
      val var12: Any = IntrinsicsKt.getCOROUTINE_SUSPENDED();
      var var10000: Any;
      switch ($continuation.label) {
         case 0:
            ResultKt.throwOnFailure(`$result`);
            val map: java.util.Map = MapsKt.mutableMapOf(
               new Pair[]{
                  TuplesKt.to("language", "中文（普通话，简体）"),
                  TuplesKt.to("voice", "zh-CN-XiaoxiaoNeural"),
                  TuplesKt.to("text", text),
                  TuplesKt.to("role", "0"),
                  TuplesKt.to("style", "0"),
                  TuplesKt.to("rate", "0"),
                  TuplesKt.to("pitch", "0"),
                  TuplesKt.to("kbitrate", "audio-16khz-32kbitrate-mono-mp3"),
                  TuplesKt.to("silence", ""),
                  TuplesKt.to("styledegree", "1"),
                  TuplesKt.to("user_id", ""),
                  TuplesKt.to("yzm", "")
               }
            );
            if (options != null) {
               map.putAll(options);
            }

            val var13: CaseInsensitiveHeaders = new CaseInsensitiveHeaders();
            map.forEach(new BiConsumer((new Function2<java.lang.String, java.lang.String, Unit>(var13) {
               {
                  super(2, receiver, CaseInsensitiveHeaders::class.java, "add", "add(Ljava/lang/String;Ljava/lang/String;)Lio/vertx/core/MultiMap;", 8);
               }

               public final void invoke(java.lang.String p0, java.lang.String p1) {
                  BookController.access$ttsByTextToSpeechCn$add(access$getReceiver$p(this) as CaseInsensitiveHeaders, p0, p1);
               }
            }) as Function2) {
               {
                  this.function = function;
               }
            });
            val var14: java.lang.String = "https://www.text-to-speech.cn/getSpeek.php";
            var10000 = (
               new Function1<Handler<AsyncResult<HttpResponse<Buffer>>>, Unit>(this, var14, var13) {
                  {
                     super(1);
                     this.this$0 = `$receiver`;
                     this.$ttsUrl = `$ttsUrl`;
                     this.$multiMap = `$multiMap`;
                  }

                  public final void invoke(@NotNull Handler<AsyncResult<HttpResponse<Buffer>>> handler) {
                     BookController.access$getWebClient$p(this.this$0)
                        .postAbs(this.$ttsUrl)
                        .timeout(5000L)
                        .putHeader("Origin", "https://www.text-to-speech.cn")
                        .putHeader("Referer", "https://www.text-to-speech.cn/")
                        .putHeader(
                           "User-Agent",
                           "Mozilla/5.0 (Macintosh; Intel Mac OS X 10_15_7) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/113.0.0.0 Safari/537.36"
                        )
                        .sendForm(this.$multiMap, handler);
                  }
               }
            ) as Function1;
            `$continuation`.L$0 = response;
            `$continuation`.label = 1;
            var10000 = VertxCoroutineKt.awaitResult((Function1)var10000, `$continuation`);
            if (var10000 === var12) {
               return var12;
            }
            break;
         case 1:
            response = `$continuation`.L$0 as HttpServerResponse;
            ResultKt.throwOnFailure(`$result`);
            var10000 = `$result`;
            break;
         default:
            throw new IllegalStateException("call to 'resume' before 'invoke' with coroutine");
      }

      val result: HttpResponse = var10000 as HttpResponse;
      BookControllerKt.access$getLogger$p().info("res: {}", var10000 as HttpResponse);
      if (result != null) {
         val jsonRes: JsonObject = result.bodyAsJsonObject();
         BookControllerKt.access$getLogger$p().info("jsonRes: {}", jsonRes);
         if (jsonRes != null && jsonRes.getString("download") != null) {
            response.setStatusCode(302).putHeader("Location", jsonRes.getString("download")).end();
         } else {
            response.setStatusCode(404).end();
         }
      } else {
         response.setStatusCode(404).end();
      }

      return Unit.INSTANCE;
   }

   public fun getChapterCacheDir(bookInfo: Book, userNameSpace: String): File {
      val var6: File = new File(
         ExtKt.getWorkDir(
            "storage", "data", userNameSpace, "${bookInfo.getName()}_${bookInfo.getAuthor()}", MD5Utils.INSTANCE.md5Encode(bookInfo.getBookUrl()).toString()
         )
      );
      if (!var6.exists()) {
         var6.mkdirs();
      }

      return var6;
   }

   public fun getCachedChapterContentSet(bookInfo: Book, userNameSpace: String): MutableSet<Int> {
      val localCacheDir: File = this.getChapterCacheDir(bookInfo, userNameSpace);
      val cachedChapterContentSet: java.util.Set = new LinkedHashSet();
      val var15: Array<File> = localCacheDir.listFiles();
      val var7: Array<Any> = var15;
      val var8: Int = var15.length;

      for (int var9 = 0; var9 < var8; var9++) {
         val it: File = var7[var9] as File;
         var var13: java.lang.String = (var7[var9] as File).getName();
         if (!StringsKt.startsWith$default(var13, ".", false, 2, null)) {
            var13 = it.getName();
            if (StringsKt.endsWith$default(var13, ".txt", false, 2, null)) {
               var13 = it.getName();
               cachedChapterContentSet.add(Integer.parseInt(StringsKt.replace$default(var13, ".txt", "", false, 4, null)));
            }
         }
      }

      return cachedChapterContentSet;
   }

   public suspend fun getShelfBookWithCacheInfo(context: RoutingContext): ReturnData {
      var `$continuation`: Continuation;
      label46: {
         if (`$completion` is <unrepresentable>) {
            `$continuation` = `$completion` as <unrepresentable>;
            if (((`$completion` as <unrepresentable>).label and Integer.MIN_VALUE) != 0) {
               `$continuation`.label -= Integer.MIN_VALUE;
               break label46;
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
               return this.this$0.getShelfBookWithCacheInfo(null, this);
            }
         };
      }

      var returnData: ReturnData;
      var userNameSpace: java.lang.String;
      var var10000: Any;
      label50: {
         val `$result`: Any = `$continuation`.result;
         val var15: Any = IntrinsicsKt.getCOROUTINE_SUSPENDED();
         switch ($continuation.label) {
            case 0:
               ResultKt.throwOnFailure(`$result`);
               returnData = new ReturnData();
               `$continuation`.L$0 = this;
               `$continuation`.L$1 = context;
               `$continuation`.L$2 = returnData;
               `$continuation`.label = 1;
               var10000 = this.checkAuth(context, `$continuation`);
               if (var10000 === var15) {
                  return var15;
               }
               break;
            case 1:
               returnData = `$continuation`.L$2 as ReturnData;
               context = `$continuation`.L$1 as RoutingContext;
               this = `$continuation`.L$0 as BookController;
               ResultKt.throwOnFailure(`$result`);
               var10000 = `$result`;
               break;
            case 2:
               userNameSpace = `$continuation`.L$2 as java.lang.String;
               returnData = `$continuation`.L$1 as ReturnData;
               this = `$continuation`.L$0 as BookController;
               ResultKt.throwOnFailure(`$result`);
               var10000 = `$result`;
               break label50;
            default:
               throw new IllegalStateException("call to 'resume' before 'invoke' with coroutine");
         }

         if (!var10000 as java.lang.Boolean) {
            return ReturnData.setData$default(returnData, "NEED_LOGIN", null, 2, null).setErrorMsg("请登录后使用");
         }

         userNameSpace = this.getUserNameSpace(context);
         `$continuation`.L$0 = this;
         `$continuation`.L$1 = returnData;
         `$continuation`.L$2 = userNameSpace;
         `$continuation`.label = 2;
         var10000 = this.getBookShelfBooks(false, userNameSpace, `$continuation`);
         if (var10000 === var15) {
            return var15;
         }
      }

      val bookList: java.util.List = var10000 as java.util.List;
      val result: java.util.List = new ArrayList();
      var var16: Int = 0;
      val var8: Int = bookList.size();
      if (0 < var8) {
         do {
            val bookInfo: Book = bookList.get(var16++) as Book;
            if (!bookInfo.isLocalBook()) {
               val cachedSet: java.util.Set = this.getCachedChapterContentSet(bookInfo, userNameSpace);
               val bookInfoMap: java.util.Map = TypeIntrinsics.asMutableMap(ExtKt.toMap(bookInfo));
               bookInfoMap.put("cachedChapterCount", Boxing.boxInt(cachedSet.size()));
               result.add(bookInfoMap);
            } else {
               result.add(bookInfo);
            }
         } while (var16 < var8);
      }

      return ReturnData.setData$default(returnData, result, null, 2, null);
   }

   public suspend fun exportBook(context: RoutingContext) {
      var `$continuation`: Continuation;
      label128: {
         if (`$completion` is <unrepresentable>) {
            `$continuation` = `$completion` as <unrepresentable>;
            if (((`$completion` as <unrepresentable>).label and Integer.MIN_VALUE) != 0) {
               `$continuation`.label -= Integer.MIN_VALUE;
               break label128;
            }
         }

         `$continuation` = new ContinuationImpl(this, `$completion`) {
            Object L$0;
            Object L$1;
            Object L$2;
            Object L$3;
            Object L$4;
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
               return this.this$0.exportBook(null, this);
            }
         };
      }

      var var38: File;
      label146: {
         label147: {
            label131: {
               var returnData: ReturnData;
               var isEpub: Int;
               var userNameSpace: java.lang.String;
               var bookInfo: Book;
               var var14: Any;
               label132: {
                  val `$result`: Any = `$continuation`.result;
                  var14 = IntrinsicsKt.getCOROUTINE_SUSPENDED();
                  switch ($continuation.label) {
                     case 0:
                        ResultKt.throwOnFailure(`$result`);
                        returnData = new ReturnData();
                        `$continuation`.L$0 = this;
                        `$continuation`.L$1 = context;
                        `$continuation`.L$2 = returnData;
                        `$continuation`.label = 1;
                        var38 = (File)this.checkAuth(context, `$continuation`);
                        if (var38 === var14) {
                           return var14;
                        }
                        break;
                     case 1:
                        returnData = `$continuation`.L$2 as ReturnData;
                        context = `$continuation`.L$1 as RoutingContext;
                        this = `$continuation`.L$0 as BookController;
                        ResultKt.throwOnFailure(`$result`);
                        var38 = (File)`$result`;
                        break;
                     case 2:
                        isEpub = `$continuation`.I$0;
                        bookInfo = `$continuation`.L$4 as Book;
                        userNameSpace = `$continuation`.L$3 as java.lang.String;
                        returnData = `$continuation`.L$2 as ReturnData;
                        context = `$continuation`.L$1 as RoutingContext;
                        this = `$continuation`.L$0 as BookController;
                        ResultKt.throwOnFailure(`$result`);
                        var38 = (File)`$result`;
                        break label132;
                     case 3:
                        context = `$continuation`.L$0 as RoutingContext;
                        ResultKt.throwOnFailure(`$result`);
                        var38 = (File)`$result`;
                        break label147;
                     case 4:
                        context = `$continuation`.L$0 as RoutingContext;
                        ResultKt.throwOnFailure(`$result`);
                        var38 = (File)`$result`;
                        break label131;
                     default:
                        throw new IllegalStateException("call to 'resume' before 'invoke' with coroutine");
                  }

                  if (!var38 as java.lang.Boolean) {
                     VertExtKt.success(context, ReturnData.setData$default(returnData, "NEED_LOGIN", null, 2, null).setErrorMsg("请登录后使用"));
                     return Unit.INSTANCE;
                  }

                  val var15: java.lang.String;
                  if (context.request().method() === HttpMethod.POST) {
                     val var22: java.lang.String = context.getBodyAsJson().getString("url");
                     userNameSpace = if (var22 == null) context.getBodyAsJson().getString("bookUrl") else var22;
                     var15 = if (userNameSpace == null) "" else userNameSpace;
                     val var18: Int = context.getBodyAsJson().getInteger("isEpub", Boxing.boxInt(0));
                     isEpub = var18.intValue();
                  } else {
                     val var23: java.util.List = context.queryParam("url");
                     userNameSpace = CollectionsKt.firstOrNull(var23);
                     var15 = if (userNameSpace == null) "" else userNameSpace;
                     val var24: java.util.List = context.queryParam("isEpub");
                     userNameSpace = CollectionsKt.firstOrNull(var24);
                     val var37: Int;
                     if (userNameSpace == null) {
                        var37 = 0;
                     } else {
                        val var25: Int = Boxing.boxInt(Integer.parseInt(userNameSpace));
                        var37 = if (var25 == null) 0 else var25;
                     }

                     isEpub = var37;
                  }

                  if (var15.length() == 0) {
                     VertExtKt.success(context, returnData.setErrorMsg("请输入书籍链接"));
                     return Unit.INSTANCE;
                  }

                  userNameSpace = this.getUserNameSpace(context);
                  bookInfo = this.getShelfBookByURL(var15, userNameSpace);
                  if (bookInfo == null) {
                     VertExtKt.success(context, returnData.setErrorMsg("请先加入书架"));
                     return Unit.INSTANCE;
                  }

                  if (bookInfo.isLocalBook() && !bookInfo.isLocalTxt()) {
                     val var29: File = bookInfo.getLocalFile();
                     context.response()
                        .putHeader("Cache-Control", "300")
                        .putHeader("Content-Disposition", Intrinsics.stringPlus("attachment; filename=", URLEncoder.encode(var29.getName(), "UTF-8")))
                        .sendFile(var29.toString());
                     return Unit.INSTANCE;
                  }

                  if (bookInfo.isLocalTxt() && isEpub <= 0) {
                     val var28: File = bookInfo.getLocalFile();
                     context.response()
                        .putHeader("Cache-Control", "300")
                        .putHeader("Content-Disposition", Intrinsics.stringPlus("attachment; filename=", URLEncoder.encode(var28.getName(), "UTF-8")))
                        .sendFile(var28.toString());
                     return Unit.INSTANCE;
                  }

                  val var10002: java.lang.String = bookInfo.getOrigin();
                  `$continuation`.L$0 = this;
                  `$continuation`.L$1 = context;
                  `$continuation`.L$2 = returnData;
                  `$continuation`.L$3 = userNameSpace;
                  `$continuation`.L$4 = bookInfo;
                  `$continuation`.I$0 = isEpub;
                  `$continuation`.label = 2;
                  var38 = (File)getBookSourceString$default(this, context, var10002, false, `$continuation`, 4, null);
                  if (var38 === var14) {
                     return var14;
                  }
               }

               val var27: java.lang.String = var38 as java.lang.String;
               if (!bookInfo.isLocalBook() && (var38 as java.lang.String == null || (var38 as java.lang.String).length() == 0)) {
                  VertExtKt.success(context, returnData.setErrorMsg("未配置书源"));
                  return Unit.INSTANCE;
               }

               val var31: File = new File(ExtKt.getWorkDir("storage", "assets", userNameSpace, "export"));
               if (isEpub > 0) {
                  `$continuation`.L$0 = context;
                  `$continuation`.L$1 = null;
                  `$continuation`.L$2 = null;
                  `$continuation`.L$3 = null;
                  `$continuation`.L$4 = null;
                  `$continuation`.label = 3;
                  var38 = (File)this.exportToEpub(var31, bookInfo, var27, userNameSpace, `$continuation`);
                  if (var38 === var14) {
                     return var14;
                  }
                  break label147;
               }

               `$continuation`.L$0 = context;
               `$continuation`.L$1 = null;
               `$continuation`.L$2 = null;
               `$continuation`.L$3 = null;
               `$continuation`.L$4 = null;
               `$continuation`.label = 4;
               var38 = (File)this.exportToTxt(var31, bookInfo, var27, userNameSpace, `$continuation`);
               if (var38 === var14) {
                  return var14;
               }
            }

            var38 = var38;
            break label146;
         }

         var38 = var38;
      }

      context.response()
         .putHeader("Cache-Control", "300")
         .putHeader("Content-Disposition", Intrinsics.stringPlus("attachment; filename=", URLEncoder.encode(var38.getName(), "UTF-8")))
         .sendFile(var38.toString());
      return Unit.INSTANCE;
   }

   public suspend fun exportToTxt(exportDir: File, bookInfo: Book, bookSource: String, userNameSpace: String): File {
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
               return this.this$0.exportToTxt(null, null, null, null, this);
            }
         };
      }

      val `$result`: Any = `$continuation`.result;
      val var11: Any = IntrinsicsKt.getCOROUTINE_SUSPENDED();
      var bookFile: File;
      switch ($continuation.label) {
         case 0:
            ResultKt.throwOnFailure(`$result`);
            bookFile = FileUtils.INSTANCE
               .createFileWithReplace(FileUtils.INSTANCE.getPath(exportDir, "《${bookInfo.getName()}》作者：${bookInfo.getRealAuthor()}.txt"));
            val var10004: Function2 = (
               new Function2<java.lang.String, ArrayList<Triple<? extends java.lang.String, ? extends Integer, ? extends java.lang.String>>, Unit>(
                  bookFile, this
               ) {
                  {
                     super(2);
                     this.$bookFile = `$bookFile`;
                     this.this$0 = `$receiver`;
                  }

                  public final void invoke(@NotNull java.lang.String text, @Nullable ArrayList<Triple<java.lang.String, Integer, java.lang.String>> srcList) {
                     val var10000: File = this.$bookFile;
                     val var3: Charset = Charset.forName(this.this$0.getAppConfig().getExportCharset());
                     FilesKt.appendText(var10000, text, var3);
                  }
               }
            ) as Function2;
            `$continuation`.L$0 = bookFile;
            `$continuation`.label = 1;
            if (this.getAllContents(bookInfo, bookSource, userNameSpace, var10004, `$continuation`) === var11) {
               return var11;
            }
            break;
         case 1:
            bookFile = `$continuation`.L$0 as File;
            ResultKt.throwOnFailure(`$result`);
            break;
         default:
            throw new IllegalStateException("call to 'resume' before 'invoke' with coroutine");
      }

      return bookFile;
   }

   private suspend fun getAllContents(
      book: Book,
      bookSourceString: String,
      userNameSpace: String,
      append: (String, ArrayList<Triple<String, Int, String>>?) -> Unit
   ) {
      var `$continuation`: Continuation;
      label40: {
         if (`$completion` is <unrepresentable>) {
            `$continuation` = `$completion` as <unrepresentable>;
            if (((`$completion` as <unrepresentable>).label and Integer.MIN_VALUE) != 0) {
               `$continuation`.label -= Integer.MIN_VALUE;
               break label40;
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
               return BookController.access$getAllContents(this.this$0, null, null, null, null, this);
            }
         };
      }

      val `$result`: Any = `$continuation`.result;
      val var23: Any = IntrinsicsKt.getCOROUTINE_SUSPENDED();
      var var10000: Any;
      switch ($continuation.label) {
         case 0:
            ResultKt.throwOnFailure(`$result`);
            append.invoke(
               "${book.getName()}\n作者：${book.getRealAuthor()}\n简介：${HtmlFormatter.format$default(HtmlFormatter.INSTANCE, book.getDisplayIntro(), null, 2, null)}",
               null
            );
            `$continuation`.L$0 = this;
            `$continuation`.L$1 = book;
            `$continuation`.L$2 = userNameSpace;
            `$continuation`.L$3 = append;
            `$continuation`.label = 1;
            var10000 = getLocalChapterList$default(this, book, bookSourceString, false, userNameSpace, false, null, `$continuation`, 48, null);
            if (var10000 === var23) {
               return var23;
            }
            break;
         case 1:
            append = `$continuation`.L$3 as Function2;
            userNameSpace = `$continuation`.L$2 as java.lang.String;
            book = `$continuation`.L$1 as Book;
            this = `$continuation`.L$0 as BookController;
            ResultKt.throwOnFailure(`$result`);
            var10000 = `$result`;
            break;
         default:
            throw new IllegalStateException("call to 'resume' before 'invoke' with coroutine");
      }

      val chapterList: java.util.List = var10000 as java.util.List;
      val localCacheDir: File = this.getChapterCacheDir(book, userNameSpace);
      val `$this$forEachIndexed$iv`: java.lang.Iterable = chapterList;
      var `index$iv`: Int = 0;

      for (Object item$iv : $this$forEachIndexed$iv) {
         val var14: Int = `index$iv`++;
         if (var14 < 0) {
            CollectionsKt.throwIndexOverflow();
         }

         var10000 = Boxing.boxInt(var14);
         val chapter: BookChapter = `item$iv` as BookChapter;
         val chapterCacheFile: File = new File("${localCacheDir.getAbsolutePath()}${File.separator}${(var10000 as java.lang.Number).intValue()}.txt");
         var content: java.lang.String = "";
         if (!this.getAppConfig().getExportNoChapterName()) {
            content = "${chapter.getTitle()}
";
         }

         if (chapterCacheFile.exists()) {
            content = "$content${FilesKt.readText$default(chapterCacheFile, null, 1, null)}
";
         } else {
            content = Intrinsics.stringPlus(content, "暂无缓存内容。\n");
         }

         append.invoke(Intrinsics.stringPlus("\n\n", content), null);
      }

      return Unit.INSTANCE;
   }

   private suspend fun exportToEpub(exportDir: File, book: Book, bookSource: String?, userNameSpace: String): File {
      var `$continuation`: Continuation;
      label27: {
         if (`$completion` is <unrepresentable>) {
            `$continuation` = `$completion` as <unrepresentable>;
            if (((`$completion` as <unrepresentable>).label and Integer.MIN_VALUE) != 0) {
               `$continuation`.label -= Integer.MIN_VALUE;
               break label27;
            }
         }

         `$continuation` = new ContinuationImpl(this, `$completion`) {
            Object L$0;
            Object L$1;
            Object L$2;
            Object L$3;
            Object L$4;
            Object L$5;
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
               return BookController.access$exportToEpub(this.this$0, null, null, null, null, this);
            }
         };
      }

      var bookFile: File;
      var epubBook: EpubBook;
      label22: {
         val `$result`: Any = `$continuation`.result;
         val var13: Any = IntrinsicsKt.getCOROUTINE_SUSPENDED();
         switch ($continuation.label) {
            case 0:
               ResultKt.throwOnFailure(`$result`);
               bookFile = FileUtils.INSTANCE.createFileWithReplace(FileUtils.INSTANCE.getPath(exportDir, "《${book.getName()}》作者：${book.getRealAuthor()}.epub"));
               epubBook = new EpubBook();
               epubBook.setVersion("2.0");
               this.setEpubMetadata(book, epubBook);
               `$continuation`.L$0 = this;
               `$continuation`.L$1 = book;
               `$continuation`.L$2 = bookSource;
               `$continuation`.L$3 = userNameSpace;
               `$continuation`.L$4 = bookFile;
               `$continuation`.L$5 = epubBook;
               `$continuation`.label = 1;
               if (this.setCover(book, epubBook, bookSource, `$continuation`) === var13) {
                  return var13;
               }
               break;
            case 1:
               epubBook = `$continuation`.L$5 as EpubBook;
               bookFile = `$continuation`.L$4 as File;
               userNameSpace = `$continuation`.L$3 as java.lang.String;
               bookSource = `$continuation`.L$2 as java.lang.String;
               book = `$continuation`.L$1 as Book;
               this = `$continuation`.L$0 as BookController;
               ResultKt.throwOnFailure(`$result`);
               break;
            case 2:
               epubBook = `$continuation`.L$1 as EpubBook;
               bookFile = `$continuation`.L$0 as File;
               ResultKt.throwOnFailure(`$result`);
               break label22;
            default:
               throw new IllegalStateException("call to 'resume' before 'invoke' with coroutine");
         }

         val contentModel: java.lang.String = this.setAssets(book, epubBook);
         `$continuation`.L$0 = bookFile;
         `$continuation`.L$1 = epubBook;
         `$continuation`.L$2 = null;
         `$continuation`.L$3 = null;
         `$continuation`.L$4 = null;
         `$continuation`.L$5 = null;
         `$continuation`.label = 2;
         if (this.setEpubContent(contentModel, book, epubBook, bookSource, userNameSpace, `$continuation`) === var13) {
            return var13;
         }
      }

      new EpubWriter().write(epubBook, new FileOutputStream(bookFile));
      return bookFile;
   }

   private fun setAssets(book: Book, epubBook: EpubBook): String {
      var var10000: Resources = epubBook.getResources();
      var var3: URL = BookController.class.getResource("/epub/fonts.css");
      var10000.add(new Resource(TextStreamsKt.readBytes(var3), "Styles/fonts.css"));
      var10000 = epubBook.getResources();
      var3 = BookController.class.getResource("/epub/main.css");
      var10000.add(new Resource(TextStreamsKt.readBytes(var3), "Styles/main.css"));
      var10000 = epubBook.getResources();
      var3 = BookController.class.getResource("/epub/logo.png");
      var10000.add(new Resource(TextStreamsKt.readBytes(var3), "Images/logo.png"));
      var var10002: java.lang.String = book.getName();
      var var10003: java.lang.String = book.getRealAuthor();
      var var10004: java.lang.String = book.getDisplayIntro();
      var var10005: java.lang.String = book.getKind();
      var var10006: java.lang.String = book.getWordCount();
      var3 = BookController.class.getResource("/epub/cover.html");
      epubBook.addSection(
         "封面",
         ResourceUtil.createPublicResource(
            var10002, var10003, var10004, var10005, var10006, new java.lang.String(TextStreamsKt.readBytes(var3), Charsets.UTF_8), "Text/cover.html"
         )
      );
      var10002 = book.getName();
      var10003 = book.getRealAuthor();
      var10004 = book.getDisplayIntro();
      var10005 = book.getKind();
      var10006 = book.getWordCount();
      var3 = BookController.class.getResource("/epub/intro.html");
      epubBook.addSection(
         "简介",
         ResourceUtil.createPublicResource(
            var10002, var10003, var10004, var10005, var10006, new java.lang.String(TextStreamsKt.readBytes(var3), Charsets.UTF_8), "Text/intro.html"
         )
      );
      var3 = BookController.class.getResource("/epub/chapter.html");
      return new java.lang.String(TextStreamsKt.readBytes(var3), Charsets.UTF_8);
   }

    /**
     * MANUALLY RECONSTRUCTED from CFR + BookController.class (reader-pro-3.2.14.jar)
     * Original Vineflower output: Couldn't be decompiled
     */
    private suspend fun setCover(book: Book, epubBook: EpubBook, bookSourceString: String?) {
        val coverUrl = book.displayCover ?: return
        if (coverUrl.startsWith("/")) {
            // Local asset under workDir/storage + path after leading '/'
            val rel = coverUrl.replace("/", File.separator).substring(1)
            val coverFile = File(getWorkDir("storage"), rel)
            if (coverFile.exists()) {
                epubBook.coverImage = Resource(coverFile.readBytes(), "Images/cover.jpg")
            }
            return
        }
        if (bookSourceString == null) return
        val ext = getFileExt(coverUrl, "jpg")
        val md5Encode = MD5Utils.md5Encode(coverUrl).toString()
        val cachePath = getWorkDir("storage", "cache", "$md5Encode.$ext")
        val cacheFile = File(cachePath)
        if (cacheFile.exists()) {
            epubBook.coverImage = Resource(cacheFile.readBytes(), "Images/cover.jpg")
            return
        }
        try {
            val source = BookSource.fromJson(bookSourceString).getOrNull()
            val analyzeUrl = AnalyzeUrl(mUrl = coverUrl, source = source)
            val bytes = analyzeUrl.getByteArrayAwait()
            epubBook.coverImage = Resource(bytes, "Images/cover.jpg")
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

   private suspend fun setEpubContent(contentModel: String, book: Book, epubBook: EpubBook, bookSourceString: String?, userNameSpace: String) {
      var `$continuation`: Continuation;
      label48: {
         if (`$completion` is <unrepresentable>) {
            `$continuation` = `$completion` as <unrepresentable>;
            if (((`$completion` as <unrepresentable>).label and Integer.MIN_VALUE) != 0) {
               `$continuation`.label -= Integer.MIN_VALUE;
               break label48;
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
               return BookController.access$setEpubContent(this.this$0, null, null, null, null, null, this);
            }
         };
      }

      val `$result`: Any = `$continuation`.result;
      val var24: Any = IntrinsicsKt.getCOROUTINE_SUSPENDED();
      var var10000: Any;
      switch ($continuation.label) {
         case 0:
            ResultKt.throwOnFailure(`$result`);
            `$continuation`.L$0 = this;
            `$continuation`.L$1 = contentModel;
            `$continuation`.L$2 = book;
            `$continuation`.L$3 = epubBook;
            `$continuation`.L$4 = userNameSpace;
            `$continuation`.label = 1;
            var10000 = getLocalChapterList$default(this, book, bookSourceString, false, userNameSpace, false, null, `$continuation`, 48, null);
            if (var10000 === var24) {
               return var24;
            }
            break;
         case 1:
            userNameSpace = `$continuation`.L$4 as java.lang.String;
            epubBook = `$continuation`.L$3 as EpubBook;
            book = `$continuation`.L$2 as Book;
            contentModel = `$continuation`.L$1 as java.lang.String;
            this = `$continuation`.L$0 as BookController;
            ResultKt.throwOnFailure(`$result`);
            var10000 = `$result`;
            break;
         default:
            throw new IllegalStateException("call to 'resume' before 'invoke' with coroutine");
      }

      val chapterList: java.util.List = var10000 as java.util.List;
      val localCacheDir: File = this.getChapterCacheDir(book, userNameSpace);
      val `$this$forEachIndexed$iv`: java.lang.Iterable = chapterList;
      var `index$iv`: Int = 0;

      for (Object item$iv : $this$forEachIndexed$iv) {
         val var14: Int = `index$iv`++;
         if (var14 < 0) {
            CollectionsKt.throwIndexOverflow();
         }

         var10000 = Boxing.boxInt(var14);
         val chapter: BookChapter = `item$iv` as BookChapter;
         val index: Int = (var10000 as java.lang.Number).intValue();
         var content: java.lang.String = "";
         if (!this.getAppConfig().getExportNoChapterName()) {
            content = "${chapter.getTitle()}
";
         }

         if (book.isLocalTxt()) {
            val content1: java.lang.String = LocalBook.INSTANCE.getContent(book, chapter);
            content = Intrinsics.stringPlus(content, if (content1 == null) "" else content1);
         } else {
            val var26: File = new File("${localCacheDir.getAbsolutePath()}${File.separator}$index.txt");
            if (var26.exists()) {
               content = "$content${FilesKt.readText$default(var26, null, 1, null)}
";
            } else {
               content = Intrinsics.stringPlus(content, "暂无缓存内容。\n");
            }
         }

         val var27: java.lang.String = this.fixPic(epubBook, book, content, chapter);
         val title: java.lang.String = chapter.getTitle();
         epubBook.addSection(
            title,
            ResourceUtil.createChapterResource(
               StringsKt.replace$default(title, "\ud83d\udd12", "", false, 4, null), var27, contentModel, "Text/chapter_$index.html"
            )
         );
      }

      return Unit.INSTANCE;
   }

   private fun fixPic(epubBook: EpubBook, book: Book, content: String, chapter: BookChapter): String {
      val data: StringBuilder = new StringBuilder("");

      val var26: java.lang.Iterable;
      for (Object element$iv : var26) {
         var var28: java.lang.String = `element$iv` as java.lang.String;
         val matcher: Matcher = AppPattern.INSTANCE.getImgPattern().matcher(`element$iv` as java.lang.String);

         while (matcher.find()) {
            val var14: java.lang.String = matcher.group(1);
            if (var14 != null) {
               val src: java.lang.String = NetworkUtils.INSTANCE.getAbsoluteURL(chapter.getUrl(), var14);
               val originalHref: java.lang.String = "${MD5Utils.INSTANCE.md5Encode16(src)}.${BookHelp.INSTANCE.getImageSuffix(src)}";
               val href: java.lang.String = Intrinsics.stringPlus("Images/", originalHref);
               val vFile: File = BookHelp.INSTANCE.getImage(book, src);
               if (vFile.exists()) {
                  epubBook.getResources().add(new LazyResource(new FileResourceProvider(vFile.getParent()), href, originalHref));
                  var28 = StringsKt.replace$default(var28, var14, Intrinsics.stringPlus("../", href), false, 4, null);
               }
            }
         }

         data.append(var28).append("\n");
      }

      val var27: java.lang.String = data.toString();
      return var27;
   }

   private fun setEpubMetadata(book: Book, epubBook: EpubBook) {
      val metadata: Metadata = new Metadata();
      metadata.getTitles().add(book.getName());
      metadata.getAuthors().add(new Author(book.getRealAuthor()));
      metadata.setLanguage("zh");
      metadata.getDates().add(new Date());
      metadata.getPublishers().add("Legado");
      metadata.getDescriptions().add(book.getDisplayIntro());
      epubBook.setMetadata(metadata);
   }

   public suspend fun searchBookContent(context: RoutingContext): ReturnData {
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
            int I$0;
            int I$1;
            int I$2;
            int I$3;
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
               return this.this$0.searchBookContent(null, this);
            }
         };
      }

      var returnData: ReturnData;
      var resultList: java.util.List;
      var currentIndex: Int;
      label174: {
         var keyword: java.lang.String;
         var size: Int;
         var bookInfo: Book;
         var chapterList: java.util.List;
         var isEnd: BooleanRef;
         var var15: Int;
         var var16: Int;
         var var22: Any;
         label213: {
            var lastIndex: Int;
            var var10000: Any;
            label188: {
               var userNameSpace: java.lang.String;
               var var48: java.lang.String;
               label189: {
                  label190: {
                     val `$result`: Any = `$continuation`.result;
                     var22 = IntrinsicsKt.getCOROUTINE_SUSPENDED();
                     switch ($continuation.label) {
                        case 0:
                           ResultKt.throwOnFailure(`$result`);
                           returnData = new ReturnData();
                           `$continuation`.L$0 = this;
                           `$continuation`.L$1 = context;
                           `$continuation`.L$2 = returnData;
                           `$continuation`.label = 1;
                           var10000 = this.checkAuth(context, `$continuation`);
                           if (var10000 === var22) {
                              return var22;
                           }
                           break;
                        case 1:
                           returnData = `$continuation`.L$2 as ReturnData;
                           context = `$continuation`.L$1 as RoutingContext;
                           this = `$continuation`.L$0 as BookController;
                           ResultKt.throwOnFailure(`$result`);
                           var10000 = `$result`;
                           break;
                        case 2:
                           size = `$continuation`.I$1;
                           lastIndex = `$continuation`.I$0;
                           bookInfo = `$continuation`.L$5 as Book;
                           userNameSpace = `$continuation`.L$4 as java.lang.String;
                           keyword = `$continuation`.L$3 as java.lang.String;
                           returnData = `$continuation`.L$2 as ReturnData;
                           context = `$continuation`.L$1 as RoutingContext;
                           this = `$continuation`.L$0 as BookController;
                           ResultKt.throwOnFailure(`$result`);
                           var10000 = `$result`;
                           break label190;
                        case 3:
                           size = `$continuation`.I$1;
                           lastIndex = `$continuation`.I$0;
                           bookInfo = `$continuation`.L$4 as Book;
                           keyword = `$continuation`.L$3 as java.lang.String;
                           returnData = `$continuation`.L$2 as ReturnData;
                           context = `$continuation`.L$1 as RoutingContext;
                           this = `$continuation`.L$0 as BookController;
                           ResultKt.throwOnFailure(`$result`);
                           var10000 = `$result`;
                           break label188;
                        case 4:
                           var16 = `$continuation`.I$3;
                           var15 = `$continuation`.I$2;
                           currentIndex = `$continuation`.I$1;
                           size = `$continuation`.I$0;
                           resultList = `$continuation`.L$6 as java.util.List;
                           isEnd = `$continuation`.L$5 as BooleanRef;
                           chapterList = `$continuation`.L$4 as java.util.List;
                           bookInfo = `$continuation`.L$3 as Book;
                           keyword = `$continuation`.L$2 as java.lang.String;
                           returnData = `$continuation`.L$1 as ReturnData;
                           this = `$continuation`.L$0 as BookController;
                           ResultKt.throwOnFailure(`$result`);
                           val chapterResult: java.util.List = `$result` as java.util.List;
                           if ((`$result` as java.util.List).size() > 0) {
                              resultList.addAll(chapterResult);
                           }

                           if (resultList.size() >= size || isEnd.element || var15 >= var16) {
                              break label174;
                           }
                           break label213;
                        default:
                           throw new IllegalStateException("call to 'resume' before 'invoke' with coroutine");
                     }

                     if (!var10000 as java.lang.Boolean) {
                        return ReturnData.setData$default(returnData, "NEED_LOGIN", null, 2, null).setErrorMsg("请登录后使用");
                     }

                     val var23: java.lang.String;
                     if (context.request().method() === HttpMethod.POST) {
                        val var38: java.lang.String = context.getBodyAsJson().getString("url");
                        userNameSpace = if (var38 == null) context.getBodyAsJson().getString("bookUrl") else var38;
                        var23 = if (userNameSpace == null) "" else userNameSpace;
                        userNameSpace = context.getBodyAsJson().getString("keyword");
                        keyword = if (userNameSpace == null) "" else userNameSpace;
                        val var30: Int = context.getBodyAsJson().getInteger("lastIndex", Boxing.boxInt(0));
                        lastIndex = var30.intValue();
                        val var31: Int = context.getBodyAsJson().getInteger("size", Boxing.boxInt(20));
                        size = var31.intValue();
                     } else {
                        val var39: java.util.List = context.queryParam("url");
                        userNameSpace = CollectionsKt.firstOrNull(var39);
                        var23 = if (userNameSpace == null) "" else userNameSpace;
                        val var40: java.util.List = context.queryParam("keyword");
                        userNameSpace = CollectionsKt.firstOrNull(var40);
                        keyword = if (userNameSpace == null) "" else userNameSpace;
                        val var41: java.util.List = context.queryParam("lastIndex");
                        userNameSpace = CollectionsKt.firstOrNull(var41);
                        val var59: Int;
                        if (userNameSpace == null) {
                           var59 = 0;
                        } else {
                           val var42: Int = Boxing.boxInt(Integer.parseInt(userNameSpace));
                           var59 = if (var42 == null) 0 else var42;
                        }

                        lastIndex = var59;
                        val var43: java.util.List = context.queryParam("size");
                        userNameSpace = CollectionsKt.firstOrNull(var43);
                        val var60: Int;
                        if (userNameSpace == null) {
                           var60 = 20;
                        } else {
                           val var44: Int = Boxing.boxInt(Integer.parseInt(userNameSpace));
                           var60 = if (var44 == null) 20 else var44;
                        }

                        size = var60;
                     }

                     if (var23.length() == 0) {
                        return returnData.setErrorMsg("请输入书籍链接");
                     }

                     if (keyword.length() == 0) {
                        return returnData.setErrorMsg("请输入搜索关键词");
                     }

                     userNameSpace = this.getUserNameSpace(context);
                     bookInfo = this.getShelfBookByURL(var23, userNameSpace);
                     if (bookInfo == null) {
                        return returnData.setErrorMsg("请先加入书架");
                     }

                     var48 = null;
                     if (bookInfo.isLocalBook()) {
                        break label189;
                     }

                     val var10002: java.lang.String = bookInfo.getOrigin();
                     `$continuation`.L$0 = this;
                     `$continuation`.L$1 = context;
                     `$continuation`.L$2 = returnData;
                     `$continuation`.L$3 = keyword;
                     `$continuation`.L$4 = userNameSpace;
                     `$continuation`.L$5 = bookInfo;
                     `$continuation`.I$0 = lastIndex;
                     `$continuation`.I$1 = size;
                     `$continuation`.label = 2;
                     var10000 = getBookSourceString$default(this, context, var10002, false, `$continuation`, 4, null);
                     if (var10000 === var22) {
                        return var22;
                     }
                  }

                  var48 = var10000 as java.lang.String;
                  if (var10000 as java.lang.String == null || (var10000 as java.lang.String).length() == 0) {
                     return returnData.setErrorMsg("未配置书源");
                  }
               }

               val var62: java.lang.String = if (var48 == null) "" else var48;
               `$continuation`.L$0 = this;
               `$continuation`.L$1 = context;
               `$continuation`.L$2 = returnData;
               `$continuation`.L$3 = keyword;
               `$continuation`.L$4 = bookInfo;
               `$continuation`.L$5 = null;
               `$continuation`.I$0 = lastIndex;
               `$continuation`.I$1 = size;
               `$continuation`.label = 3;
               var10000 = getLocalChapterList$default(this, bookInfo, var62, false, userNameSpace, false, null, `$continuation`, 48, null);
               if (var10000 === var22) {
                  return var22;
               }
            }

            chapterList = var10000 as java.util.List;
            if (lastIndex >= (var10000 as java.util.List).size()) {
               return returnData.setErrorMsg("没有更多了");
            }

            isEnd = new BooleanRef();
            context.request().connection().closeHandler(BookController::searchBookContent$lambda-30);
            BookControllerKt.access$getLogger$p().info("searchBookContent keyword: {} lastIndex: {}", keyword, Boxing.boxInt(lastIndex));
            resultList = new ArrayList();
            currentIndex = ++lastIndex;
            var15 = lastIndex;
            var16 = chapterList.size();
            if (lastIndex >= var16) {
               break label174;
            }
         }

         do {
            val chapterIndex: Int = var15++;
            currentIndex = chapterIndex;
            val chapter: BookChapter = chapterList.get(chapterIndex) as BookChapter;
            `$continuation`.L$0 = this;
            `$continuation`.L$1 = returnData;
            `$continuation`.L$2 = keyword;
            `$continuation`.L$3 = bookInfo;
            `$continuation`.L$4 = chapterList;
            `$continuation`.L$5 = isEnd;
            `$continuation`.L$6 = resultList;
            `$continuation`.I$0 = size;
            `$continuation`.I$1 = chapterIndex;
            `$continuation`.I$2 = var15;
            `$continuation`.I$3 = var16;
            `$continuation`.label = 4;
            val var61: Any = this.searchChapter(bookInfo, chapter, keyword, `$continuation`);
            if (var61 === var22) {
               return var22;
            }

            val var56: java.util.List = var61 as java.util.List;
            if ((var61 as java.util.List).size() > 0) {
               resultList.addAll(var56);
            }
         } while (resultList.size() < size && !isEnd.element && var15 < var16);
      }

      return ReturnData.setData$default(
         returnData, MapsKt.mapOf(new Pair[]{TuplesKt.to("list", resultList), TuplesKt.to("lastIndex", Boxing.boxInt(currentIndex))}), null, 2, null
      );
   }

   public suspend fun searchChapter(book: Book, chapter: BookChapter, query: String): List<SearchResult> {
      var `$continuation`: Continuation;
      label40: {
         if (`$completion` is <unrepresentable>) {
            `$continuation` = `$completion` as <unrepresentable>;
            if (((`$completion` as <unrepresentable>).label and Integer.MIN_VALUE) != 0) {
               `$continuation`.label -= Integer.MIN_VALUE;
               break label40;
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
               return this.this$0.searchChapter(null, null, null, this);
            }
         };
      }

      val `$result`: Any = `$continuation`.result;
      val var22: Any = IntrinsicsKt.getCOROUTINE_SUSPENDED();
      var searchResultsWithinChapter: java.util.List;
      var chapterContent: java.lang.String;
      var var10000: Any;
      switch ($continuation.label) {
         case 0:
            ResultKt.throwOnFailure(`$result`);
            searchResultsWithinChapter = new ArrayList();
            chapterContent = BookHelp.INSTANCE.getContent(book, chapter);
            if (chapterContent == null) {
               return searchResultsWithinChapter;
            }

            `$continuation`.L$0 = this;
            `$continuation`.L$1 = chapter;
            `$continuation`.L$2 = query;
            `$continuation`.L$3 = searchResultsWithinChapter;
            `$continuation`.L$4 = chapterContent;
            `$continuation`.label = 1;
            var10000 = this.searchPosition(chapterContent, query, `$continuation`);
            if (var10000 === var22) {
               return var22;
            }
            break;
         case 1:
            chapterContent = `$continuation`.L$4 as java.lang.String;
            searchResultsWithinChapter = `$continuation`.L$3 as java.util.List;
            query = `$continuation`.L$2 as java.lang.String;
            chapter = `$continuation`.L$1 as BookChapter;
            this = `$continuation`.L$0 as BookController;
            ResultKt.throwOnFailure(`$result`);
            var10000 = `$result`;
            break;
         default:
            throw new IllegalStateException("call to 'resume' before 'invoke' with coroutine");
      }

      val positions: java.util.List = var10000 as java.util.List;
      BookControllerKt.access$getLogger$p().info("positions: {}", var10000 as java.util.List);
      val `$this$forEachIndexed$iv`: java.lang.Iterable = positions;
      var `index$iv`: Int = 0;

      for (Object item$iv : $this$forEachIndexed$iv) {
         val var13: Int = `index$iv`++;
         if (var13 < 0) {
            CollectionsKt.throwIndexOverflow();
         }

         var10000 = Boxing.boxInt(var13);
         val position: Int = (`item$iv` as java.lang.Number).intValue();
         val index: Int = (var10000 as java.lang.Number).intValue();
         val construct: Pair = this.getResultAndQueryIndex(chapterContent, position, query);
         searchResultsWithinChapter.add(
            new SearchResult(
               0,
               index,
               construct.getSecond() as java.lang.String,
               chapter.getTitle(),
               query,
               0,
               chapter.getIndex(),
               0,
               (construct.getFirst() as java.lang.Number).intValue(),
               position,
               161,
               null
            )
         );
      }

      return searchResultsWithinChapter;
   }

   private suspend fun searchPosition(mContent: String, pattern: String): List<Int> {
      val position: java.util.List = new ArrayList();
      var var6: Int = StringsKt.indexOf$default(mContent, pattern, 0, false, 6, null);
      if (var6 >= 0) {
         while (index >= 0) {
            position.add(Boxing.boxInt(var6));
            var6 = StringsKt.indexOf$default(mContent, pattern, var6 + 1, false, 4, null);
         }
      }

      return position;
   }

   private fun getResultAndQueryIndex(content: String, queryIndexInContent: Int, query: String): Pair<Int, String> {
      var po1: Int = queryIndexInContent - 20;
      var po2: Int = queryIndexInContent + query.length() + 20;
      if (po1 < 0) {
         po1 = 0;
      }

      if (po2 > content.length()) {
         po2 = content.length();
      }

      val queryIndexInResult: Int = queryIndexInContent - po1;
      if (content == null) {
         throw new NullPointerException("null cannot be cast to non-null type java.lang.String");
      } else {
         val var10000: java.lang.String = content.substring(po1, po2);
         return TuplesKt.to(queryIndexInResult, var10000);
      }
   }

   public suspend fun backupToMongodb(context: RoutingContext): ReturnData {
      var `$continuation`: Continuation;
      label79: {
         if (`$completion` is <unrepresentable>) {
            `$continuation` = `$completion` as <unrepresentable>;
            if (((`$completion` as <unrepresentable>).label and Integer.MIN_VALUE) != 0) {
               `$continuation`.label -= Integer.MIN_VALUE;
               break label79;
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
               return this.this$0.backupToMongodb(null, this);
            }
         };
      }

      val `$result`: Any = `$continuation`.result;
      val var21: Any = IntrinsicsKt.getCOROUTINE_SUSPENDED();
      var returnData: ReturnData;
      var var10000: Any;
      switch ($continuation.label) {
         case 0:
            ResultKt.throwOnFailure(`$result`);
            returnData = new ReturnData();
            `$continuation`.L$0 = this;
            `$continuation`.L$1 = context;
            `$continuation`.L$2 = returnData;
            `$continuation`.label = 1;
            var10000 = this.checkAuth(context, `$continuation`);
            if (var10000 === var21) {
               return var21;
            }
            break;
         case 1:
            returnData = `$continuation`.L$2 as ReturnData;
            context = `$continuation`.L$1 as RoutingContext;
            this = `$continuation`.L$0 as BookController;
            ResultKt.throwOnFailure(`$result`);
            var10000 = `$result`;
            break;
         default:
            throw new IllegalStateException("call to 'resume' before 'invoke' with coroutine");
      }

      if (!var10000 as java.lang.Boolean) {
         return ReturnData.setData$default(returnData, "NEED_LOGIN", null, 2, null).setErrorMsg("请登录后使用");
      } else if (!MongoManager.INSTANCE.isInit()) {
         return returnData.setErrorMsg("请先设置 mongoUri");
      } else if (!this.checkManagerAuth(context)) {
         return ReturnData.setData$default(returnData, "NEED_SECURE_KEY", null, 2, null).setErrorMsg("请输入管理密码");
      } else {
         val handler: Array<java.lang.String> = this.getBackupFileNames();
         val syncDataFileList: ArrayList = CollectionsKt.arrayListOf(Arrays.copyOf(handler, handler.length));
         val var23: Function1 = (new Function1<java.lang.String, Unit>(syncDataFileList, this) {
            {
               super(1);
               this.$syncDataFileList = `$syncDataFileList`;
               this.this$0 = `$receiver`;
            }

            public final void invoke(@NotNull java.lang.String userNameSpace) {
               val `$this$forEach$iv`: java.lang.Iterable = this.$syncDataFileList;
               val var3: BookController = this.this$0;

               for (Object element$iv : $this$forEach$iv) {
                  val it: java.lang.String = `element$iv` as java.lang.String;
                  val var10: java.lang.String = var3.getUserStorage(userNameSpace, new java.lang.String[]{`element$iv` as java.lang.String});
                  if (var10 != null) {
                     var3.saveUserStorage(userNameSpace, it, var10);
                  }
               }
            }
         }) as Function1;
         var23.invoke("default");
         if (this.getAppConfig().getSecure()) {
            var userMap: java.util.Map = new LinkedHashMap();
            val var25: JsonObject = ExtKt.asJsonObject(ExtKt.getStorage$default(new java.lang.String[]{"data", "users"}, null, 2, null));
            if (var25 != null) {
               val var27: java.util.Map = var25.getMap();
               if (var27 == null) {
                  throw new NullPointerException(
                     "null cannot be cast to non-null type kotlin.collections.MutableMap<kotlin.String, kotlin.collections.Map<kotlin.String, kotlin.Any>>"
                  );
               }

               userMap = TypeIntrinsics.asMutableMap(var27);
            }

            for (Entry element$iv : userMap.entrySet()) {
               val it: Entry = `element$iv`;

               try {
                  val var16: java.lang.String = (it.getValue() as java.util.Map).getOrDefault("username", "");
                  val e: java.lang.String = if (var16 == null) "" else var16;
                  if ((if (var16 == null) "" else var16).length() > 0) {
                     var23.invoke(e);
                  }
               } catch (var22: Exception) {
                  var22.printStackTrace();
               }
            }
         }

         val var24: java.lang.String = ExtKt.getStorage$default(new java.lang.String[]{"users"}, null, 2, null);
         if (var24 != null) {
            ExtKt.saveStorage$default(new java.lang.String[]{"users"}, var24, false, null, 12, null);
         }

         return ReturnData.setData$default(returnData, "", null, 2, null);
      }
   }

   public suspend fun restoreFromMongodb(context: RoutingContext): ReturnData {
      var `$continuation`: Continuation;
      label78: {
         if (`$completion` is <unrepresentable>) {
            `$continuation` = `$completion` as <unrepresentable>;
            if (((`$completion` as <unrepresentable>).label and Integer.MIN_VALUE) != 0) {
               `$continuation`.label -= Integer.MIN_VALUE;
               break label78;
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
               return this.this$0.restoreFromMongodb(null, this);
            }
         };
      }

      val `$result`: Any = `$continuation`.result;
      val var21: Any = IntrinsicsKt.getCOROUTINE_SUSPENDED();
      var returnData: ReturnData;
      var var10000: Any;
      switch ($continuation.label) {
         case 0:
            ResultKt.throwOnFailure(`$result`);
            returnData = new ReturnData();
            `$continuation`.L$0 = this;
            `$continuation`.L$1 = context;
            `$continuation`.L$2 = returnData;
            `$continuation`.label = 1;
            var10000 = this.checkAuth(context, `$continuation`);
            if (var10000 === var21) {
               return var21;
            }
            break;
         case 1:
            returnData = `$continuation`.L$2 as ReturnData;
            context = `$continuation`.L$1 as RoutingContext;
            this = `$continuation`.L$0 as BookController;
            ResultKt.throwOnFailure(`$result`);
            var10000 = `$result`;
            break;
         default:
            throw new IllegalStateException("call to 'resume' before 'invoke' with coroutine");
      }

      if (!var10000 as java.lang.Boolean) {
         return ReturnData.setData$default(returnData, "NEED_LOGIN", null, 2, null).setErrorMsg("请登录后使用");
      } else if (!MongoManager.INSTANCE.isInit()) {
         return returnData.setErrorMsg("请先设置 mongoUri");
      } else if (!this.checkManagerAuth(context)) {
         return ReturnData.setData$default(returnData, "NEED_SECURE_KEY", null, 2, null).setErrorMsg("请输入管理密码");
      } else {
         val handler: Array<java.lang.String> = this.getBackupFileNames();
         val syncDataFileList: ArrayList = CollectionsKt.arrayListOf(Arrays.copyOf(handler, handler.length));
         val var23: Function1 = (
            new Function1<java.lang.String, Unit>(syncDataFileList) {
               {
                  super(1);
                  this.$syncDataFileList = `$syncDataFileList`;
               }

               public final void invoke(@NotNull java.lang.String userNameSpace) {
                  val `$this$forEach$iv`: java.lang.Iterable;
                  for (Object element$iv : $this$forEach$iv) {
                     val file: File = new File(
                        ExtKt.getWorkDir("storage", "data", userNameSpace, Intrinsics.stringPlus(`element$iv` as java.lang.String, ".json"))
                     );
                     if (file.exists()) {
                        file.delete();
                     }
                  }
               }
            }
         ) as Function1;
         var23.invoke("default");
         if (this.getAppConfig().getSecure()) {
            var usersFile: java.util.Map = new LinkedHashMap();
            val var25: JsonObject = ExtKt.asJsonObject(ExtKt.getStorage$default(new java.lang.String[]{"data", "users"}, null, 2, null));
            if (var25 != null) {
               val var28: java.util.Map = var25.getMap();
               if (var28 == null) {
                  throw new NullPointerException(
                     "null cannot be cast to non-null type kotlin.collections.MutableMap<kotlin.String, kotlin.collections.Map<kotlin.String, kotlin.Any>>"
                  );
               }

               usersFile = TypeIntrinsics.asMutableMap(var28);
            }

            for (Entry element$iv : userMap.entrySet()) {
               val it: Entry = `element$iv`;

               try {
                  val var16: java.lang.String = (it.getValue() as java.util.Map).getOrDefault("username", "");
                  val e: java.lang.String = if (var16 == null) "" else var16;
                  if ((if (var16 == null) "" else var16).length() > 0) {
                     var23.invoke(e);
                  }
               } catch (var22: Exception) {
                  var22.printStackTrace();
               }
            }
         }

         val var24: File = new File(ExtKt.getWorkDir("storage", "users.json"));
         if (var24.exists()) {
            var24.delete();
            ExtKt.getStorage$default(new java.lang.String[]{"users"}, null, 2, null);
         }

         return ReturnData.setData$default(returnData, "", null, 2, null);
      }
   }

   @JvmStatic
   fun `searchBookMulti$lambda-5`(`$isEnd`: BooleanRef, `this$0`: BookController, it: Void) {
      BookControllerKt.access$getLogger$p().info("客户端已断开链接，停止 searchBookMulti");
      `$isEnd`.element = true;
      JobKt.cancel$default(`this$0`.getCoroutineContext(), null, 1, null);
   }

   @JvmStatic
   fun `searchBookMultiSSE$lambda-6`(`$isEnd`: BooleanRef, `this$0`: BookController, it: Void) {
      BookControllerKt.access$getLogger$p().info("客户端已断开链接，停止 searchBookMultiSSE");
      `$isEnd`.element = true;
      JobKt.cancel$default(`this$0`.getCoroutineContext(), null, 1, null);
   }

   @JvmStatic
   fun `searchBookSource$lambda-7`(`$isEnd`: BooleanRef, `this$0`: BookController, it: Void) {
      BookControllerKt.access$getLogger$p().info("客户端已断开链接，停止 searchBookSource");
      `$isEnd`.element = true;
      JobKt.cancel$default(`this$0`.getCoroutineContext(), null, 1, null);
   }

   @JvmStatic
   fun `searchBookSourceSSE$lambda-8`(`$isEnd`: BooleanRef, `this$0`: BookController, it: Void) {
      BookControllerKt.access$getLogger$p().info("客户端已断开链接，停止 searchBookSourceSSE");
      `$isEnd`.element = true;
      JobKt.cancel$default(`this$0`.getCoroutineContext(), null, 1, null);
   }

   @JvmStatic
   fun `bookSourceDebugSSE$lambda-18`(`this$0`: BookController, it: Void) {
      BookControllerKt.access$getLogger$p().info("客户端已断开链接，停止 bookSourceDebugSSE");
      JobKt.cancel$default(`this$0`.getCoroutineContext(), null, 1, null);
   }

   @JvmStatic
   fun `cacheBookSSE$lambda-19`(`$isEnd`: BooleanRef, `this$0`: BookController, it: Void) {
      BookControllerKt.access$getLogger$p().info("客户端已断开链接，停止 cacheBookSSE");
      `$isEnd`.element = true;
      JobKt.cancel$default(`this$0`.getCoroutineContext(), null, 1, null);
   }

   @JvmStatic
   fun `searchBookContent$lambda-30`(`$isEnd`: BooleanRef, `this$0`: BookController, it: Void) {
      BookControllerKt.access$getLogger$p().info("客户端已断开链接，停止 searchBookContent");
      `$isEnd`.element = true;
      JobKt.cancel$default(`this$0`.getCoroutineContext(), null, 1, null);
   }
}
