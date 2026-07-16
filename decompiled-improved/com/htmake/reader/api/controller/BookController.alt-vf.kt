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
import com.htmake.reader.api.controller.BookControllerKt.sam.java_util_function_BiConsumer.0
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
               returnData, MapsKt.mapOf(new Pair[]{TuplesKt.to("book", book), TuplesKt.to("chapters", var10000.getChapterList(book))}), null, 2, null
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

   public suspend fun saveBookCover(book: Book, userNameSpace: String, bookSource: String? = ...) {
      // $VF: Couldn't be decompiled
      // Please report this to the Vineflower issue tracker, at https://github.com/Vineflower/vineflower/issues with a copy of the class file (if you have the rights to distribute it!)
      // java.lang.NullPointerException: Cannot invoke "org.jetbrains.java.decompiler.code.cfg.ExceptionRangeCFG.isCircular()" because "range" is null
      //   at org.jetbrains.java.decompiler.modules.decompiler.decompose.DomHelper.graphToStatement(DomHelper.java:84)
      //   at org.jetbrains.java.decompiler.modules.decompiler.decompose.DomHelper.parseGraph(DomHelper.java:203)
      //   at org.jetbrains.java.decompiler.modules.decompiler.decompose.DomHelper.createStatement(DomHelper.java:27)
      //   at org.jetbrains.java.decompiler.main.rels.MethodProcessor.codeToJava(MethodProcessor.java:157)
      //
      // Bytecode:
      // 000: aload 4
      // 002: instanceof com/htmake/reader/api/controller/BookController$saveBookCover$1
      // 005: ifeq 029
      // 008: aload 4
      // 00a: checkcast com/htmake/reader/api/controller/BookController$saveBookCover$1
      // 00d: astore 19
      // 00f: aload 19
      // 011: getfield com/htmake/reader/api/controller/BookController$saveBookCover$1.label I
      // 014: ldc -2147483648
      // 016: iand
      // 017: ifeq 029
      // 01a: aload 19
      // 01c: dup
      // 01d: getfield com/htmake/reader/api/controller/BookController$saveBookCover$1.label I
      // 020: ldc -2147483648
      // 022: isub
      // 023: putfield com/htmake/reader/api/controller/BookController$saveBookCover$1.label I
      // 026: goto 035
      // 029: new com/htmake/reader/api/controller/BookController$saveBookCover$1
      // 02c: dup
      // 02d: aload 0
      // 02e: aload 4
      // 030: invokespecial com/htmake/reader/api/controller/BookController$saveBookCover$1.<init> (Lcom/htmake/reader/api/controller/BookController;Lkotlin/coroutines/Continuation;)V
      // 033: astore 19
      // 035: aload 19
      // 037: getfield com/htmake/reader/api/controller/BookController$saveBookCover$1.result Ljava/lang/Object;
      // 03a: astore 18
      // 03c: invokestatic kotlin/coroutines/intrinsics/IntrinsicsKt.getCOROUTINE_SUSPENDED ()Ljava/lang/Object;
      // 03f: astore 20
      // 041: aload 19
      // 043: getfield com/htmake/reader/api/controller/BookController$saveBookCover$1.label I
      // 046: tableswitch 436 0 1 22 340
      // 05c: aload 18
      // 05e: invokestatic kotlin/ResultKt.throwOnFailure (Ljava/lang/Object;)V
      // 061: aload 1
      // 062: invokevirtual io/legado/app/data/entities/Book.getDisplayCover ()Ljava/lang/String;
      // 065: astore 5
      // 067: aload 5
      // 069: ifnull 1f6
      // 06c: aload 5
      // 06e: ldc_w "/"
      // 071: bipush 0
      // 072: bipush 2
      // 073: aconst_null
      // 074: invokestatic kotlin/text/StringsKt.startsWith$default (Ljava/lang/String;Ljava/lang/String;ZILjava/lang/Object;)Z
      // 077: ifne 1f6
      // 07a: aload 3
      // 07b: astore 7
      // 07d: aload 7
      // 07f: ifnonnull 08e
      // 082: aload 0
      // 083: aload 1
      // 084: invokevirtual io/legado/app/data/entities/Book.getOrigin ()Ljava/lang/String;
      // 087: aload 2
      // 088: invokevirtual com/htmake/reader/api/controller/BookController.getBookSourceStringBySourceURLOpt (Ljava/lang/String;Ljava/lang/String;)Ljava/lang/String;
      // 08b: goto 090
      // 08e: aload 7
      // 090: astore 6
      // 092: aload 0
      // 093: aload 5
      // 095: ldc_w "jpg"
      // 098: invokevirtual com/htmake/reader/api/controller/BookController.getFileExt (Ljava/lang/String;Ljava/lang/String;)Ljava/lang/String;
      // 09b: astore 7
      // 09d: getstatic io/legado/app/utils/MD5Utils.INSTANCE Lio/legado/app/utils/MD5Utils;
      // 0a0: aload 5
      // 0a2: invokevirtual io/legado/app/utils/MD5Utils.md5Encode (Ljava/lang/String;)Ljava/lang/String;
      // 0a5: invokevirtual java/lang/String.toString ()Ljava/lang/String;
      // 0a8: astore 8
      // 0aa: bipush 5
      // 0ab: anewarray 96
      // 0ae: astore 10
      // 0b0: aload 10
      // 0b2: bipush 0
      // 0b3: ldc "storage"
      // 0b5: aastore
      // 0b6: aload 10
      // 0b8: bipush 1
      // 0b9: ldc_w "assets"
      // 0bc: aastore
      // 0bd: aload 10
      // 0bf: bipush 2
      // 0c0: aload 2
      // 0c1: aastore
      // 0c2: aload 10
      // 0c4: bipush 3
      // 0c5: ldc_w "covers"
      // 0c8: aastore
      // 0c9: aload 10
      // 0cb: bipush 4
      // 0cc: new java/lang/StringBuilder
      // 0cf: dup
      // 0d0: invokespecial java/lang/StringBuilder.<init> ()V
      // 0d3: aload 8
      // 0d5: invokevirtual java/lang/StringBuilder.append (Ljava/lang/String;)Ljava/lang/StringBuilder;
      // 0d8: bipush 46
      // 0da: invokevirtual java/lang/StringBuilder.append (C)Ljava/lang/StringBuilder;
      // 0dd: aload 7
      // 0df: invokevirtual java/lang/StringBuilder.append (Ljava/lang/String;)Ljava/lang/StringBuilder;
      // 0e2: invokevirtual java/lang/StringBuilder.toString ()Ljava/lang/String;
      // 0e5: aastore
      // 0e6: aload 10
      // 0e8: invokestatic com/htmake/reader/utils/ExtKt.getWorkDir ([Ljava/lang/String;)Ljava/lang/String;
      // 0eb: astore 9
      // 0ed: new java/lang/StringBuilder
      // 0f0: dup
      // 0f1: invokespecial java/lang/StringBuilder.<init> ()V
      // 0f4: ldc_w "/assets/"
      // 0f7: invokevirtual java/lang/StringBuilder.append (Ljava/lang/String;)Ljava/lang/StringBuilder;
      // 0fa: aload 2
      // 0fb: invokevirtual java/lang/StringBuilder.append (Ljava/lang/String;)Ljava/lang/StringBuilder;
      // 0fe: ldc_w "/covers/"
      // 101: invokevirtual java/lang/StringBuilder.append (Ljava/lang/String;)Ljava/lang/StringBuilder;
      // 104: aload 8
      // 106: invokevirtual java/lang/StringBuilder.append (Ljava/lang/String;)Ljava/lang/StringBuilder;
      // 109: bipush 46
      // 10b: invokevirtual java/lang/StringBuilder.append (C)Ljava/lang/StringBuilder;
      // 10e: aload 7
      // 110: invokevirtual java/lang/StringBuilder.append (Ljava/lang/String;)Ljava/lang/StringBuilder;
      // 113: invokevirtual java/lang/StringBuilder.toString ()Ljava/lang/String;
      // 116: astore 10
      // 118: new java/io/File
      // 11b: dup
      // 11c: aload 9
      // 11e: invokespecial java/io/File.<init> (Ljava/lang/String;)V
      // 121: astore 11
      // 123: aload 11
      // 125: invokevirtual java/io/File.exists ()Z
      // 128: ifeq 135
      // 12b: aload 1
      // 12c: aload 10
      // 12e: invokevirtual io/legado/app/data/entities/Book.setCoverUrl (Ljava/lang/String;)V
      // 131: getstatic kotlin/Unit.INSTANCE Lkotlin/Unit;
      // 134: areturn
      // 135: new io/legado/app/model/analyzeRule/AnalyzeUrl
      // 138: dup
      // 139: aload 5
      // 13b: aconst_null
      // 13c: aconst_null
      // 13d: aconst_null
      // 13e: aconst_null
      // 13f: aconst_null
      // 140: getstatic io/legado/app/data/entities/BookSource.Companion Lio/legado/app/data/entities/BookSource$Companion;
      // 143: aload 6
      // 145: dup
      // 146: invokestatic kotlin/jvm/internal/Intrinsics.checkNotNull (Ljava/lang/Object;)V
      // 149: invokevirtual io/legado/app/data/entities/BookSource$Companion.fromJson-IoAF18A (Ljava/lang/String;)Ljava/lang/Object;
      // 14c: astore 13
      // 14e: bipush 0
      // 14f: istore 14
      // 151: aload 13
      // 153: invokestatic kotlin/Result.isFailure-impl (Ljava/lang/Object;)Z
      // 156: ifeq 15d
      // 159: aconst_null
      // 15a: goto 15f
      // 15d: aload 13
      // 15f: checkcast io/legado/app/data/entities/BaseSource
      // 162: aconst_null
      // 163: aconst_null
      // 164: aconst_null
      // 165: aconst_null
      // 166: sipush 1982
      // 169: aconst_null
      // 16a: invokespecial io/legado/app/model/analyzeRule/AnalyzeUrl.<init> (Ljava/lang/String;Ljava/lang/String;Ljava/lang/Integer;Ljava/lang/String;Ljava/lang/Integer;Ljava/lang/String;Lio/legado/app/data/entities/BaseSource;Lio/legado/app/model/analyzeRule/RuleDataInterface;Lio/legado/app/data/entities/BookChapter;Ljava/util/Map;Lio/legado/app/model/DebugLog;ILkotlin/jvm/internal/DefaultConstructorMarker;)V
      // 16d: astore 12
      // 16f: nop
      // 170: aload 12
      // 172: aload 19
      // 174: aload 19
      // 176: aload 1
      // 177: putfield com/htmake/reader/api/controller/BookController$saveBookCover$1.L$0 Ljava/lang/Object;
      // 17a: aload 19
      // 17c: aload 9
      // 17e: putfield com/htmake/reader/api/controller/BookController$saveBookCover$1.L$1 Ljava/lang/Object;
      // 181: aload 19
      // 183: aload 10
      // 185: putfield com/htmake/reader/api/controller/BookController$saveBookCover$1.L$2 Ljava/lang/Object;
      // 188: aload 19
      // 18a: bipush 1
      // 18b: putfield com/htmake/reader/api/controller/BookController$saveBookCover$1.label I
      // 18e: invokevirtual io/legado/app/model/analyzeRule/AnalyzeUrl.getByteArrayAwait (Lkotlin/coroutines/Continuation;)Ljava/lang/Object;
      // 191: dup
      // 192: aload 20
      // 194: if_acmpne 1bf
      // 197: aload 20
      // 199: areturn
      // 19a: aload 19
      // 19c: getfield com/htmake/reader/api/controller/BookController$saveBookCover$1.L$2 Ljava/lang/Object;
      // 19f: checkcast java/lang/String
      // 1a2: astore 10
      // 1a4: aload 19
      // 1a6: getfield com/htmake/reader/api/controller/BookController$saveBookCover$1.L$1 Ljava/lang/Object;
      // 1a9: checkcast java/lang/String
      // 1ac: astore 9
      // 1ae: aload 19
      // 1b0: getfield com/htmake/reader/api/controller/BookController$saveBookCover$1.L$0 Ljava/lang/Object;
      // 1b3: checkcast io/legado/app/data/entities/Book
      // 1b6: astore 1
      // 1b7: nop
      // 1b8: aload 18
      // 1ba: invokestatic kotlin/ResultKt.throwOnFailure (Ljava/lang/Object;)V
      // 1bd: aload 18
      // 1bf: astore 13
      // 1c1: bipush 0
      // 1c2: istore 14
      // 1c4: bipush 0
      // 1c5: istore 15
      // 1c7: aload 13
      // 1c9: checkcast [B
      // 1cc: astore 16
      // 1ce: bipush 0
      // 1cf: istore 17
      // 1d1: getstatic io/legado/app/utils/FileUtils.INSTANCE Lio/legado/app/utils/FileUtils;
      // 1d4: aload 9
      // 1d6: aload 16
      // 1d8: invokevirtual io/legado/app/utils/FileUtils.writeBytes (Ljava/lang/String;[B)Z
      // 1db: pop
      // 1dc: aload 1
      // 1dd: aload 10
      // 1df: invokevirtual io/legado/app/data/entities/Book.setCoverUrl (Ljava/lang/String;)V
      // 1e2: nop
      // 1e3: nop
      // 1e4: goto 1f6
      // 1e7: astore 13
      // 1e9: aload 13
      // 1eb: invokevirtual java/lang/Exception.printStackTrace ()V
      // 1ee: goto 1f6
      // 1f1: astore 13
      // 1f3: aload 13
      // 1f5: athrow
      // 1f6: getstatic kotlin/Unit.INSTANCE Lkotlin/Unit;
      // 1f9: areturn
      // 1fa: new java/lang/IllegalStateException
      // 1fd: dup
      // 1fe: ldc_w "call to 'resume' before 'invoke' with coroutine"
      // 201: invokespecial java/lang/IllegalStateException.<init> (Ljava/lang/String;)V
      // 204: athrow
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

   public suspend fun getLocalChapterList(
      book: Book,
      bookSource: String?,
      refresh: Boolean = ...,
      userNameSpace: String,
      debugLog: Boolean = ...,
      mutex: Mutex? = ...
   ): List<BookChapter> {
      // $VF: Couldn't be decompiled
      // Please report this to the Vineflower issue tracker, at https://github.com/Vineflower/vineflower/issues with a copy of the class file (if you have the rights to distribute it!)
      // java.lang.IndexOutOfBoundsException: Index 0 out of bounds for length 0
      //   at java.base/jdk.internal.util.Preconditions.outOfBounds(Preconditions.java:64)
      //   at java.base/jdk.internal.util.Preconditions.outOfBoundsCheckIndex(Preconditions.java:70)
      //   at java.base/jdk.internal.util.Preconditions.checkIndex(Preconditions.java:266)
      //   at java.base/java.util.Objects.checkIndex(Objects.java:361)
      //   at java.base/java.util.ArrayList.remove(ArrayList.java:504)
      //   at org.jetbrains.java.decompiler.modules.decompiler.FinallyProcessor.removeExceptionInstructionsEx(FinallyProcessor.java:1057)
      //   at org.jetbrains.java.decompiler.modules.decompiler.FinallyProcessor.verifyFinallyEx(FinallyProcessor.java:572)
      //   at org.jetbrains.java.decompiler.modules.decompiler.FinallyProcessor.iterateGraph(FinallyProcessor.java:90)
      //
      // Bytecode:
      // 000: aload 7
      // 002: instanceof com/htmake/reader/api/controller/BookController$getLocalChapterList$1
      // 005: ifeq 029
      // 008: aload 7
      // 00a: checkcast com/htmake/reader/api/controller/BookController$getLocalChapterList$1
      // 00d: astore 22
      // 00f: aload 22
      // 011: getfield com/htmake/reader/api/controller/BookController$getLocalChapterList$1.label I
      // 014: ldc -2147483648
      // 016: iand
      // 017: ifeq 029
      // 01a: aload 22
      // 01c: dup
      // 01d: getfield com/htmake/reader/api/controller/BookController$getLocalChapterList$1.label I
      // 020: ldc -2147483648
      // 022: isub
      // 023: putfield com/htmake/reader/api/controller/BookController$getLocalChapterList$1.label I
      // 026: goto 035
      // 029: new com/htmake/reader/api/controller/BookController$getLocalChapterList$1
      // 02c: dup
      // 02d: aload 0
      // 02e: aload 7
      // 030: invokespecial com/htmake/reader/api/controller/BookController$getLocalChapterList$1.<init> (Lcom/htmake/reader/api/controller/BookController;Lkotlin/coroutines/Continuation;)V
      // 033: astore 22
      // 035: aload 22
      // 037: getfield com/htmake/reader/api/controller/BookController$getLocalChapterList$1.result Ljava/lang/Object;
      // 03a: astore 21
      // 03c: invokestatic kotlin/coroutines/intrinsics/IntrinsicsKt.getCOROUTINE_SUSPENDED ()Ljava/lang/Object;
      // 03f: astore 23
      // 041: aload 22
      // 043: getfield com/htmake/reader/api/controller/BookController$getLocalChapterList$1.label I
      // 046: tableswitch 1603 0 5 38 607 784 1095 1241 1510
      // 06c: aload 21
      // 06e: invokestatic kotlin/ResultKt.throwOnFailure (Ljava/lang/Object;)V
      // 071: getstatic io/legado/app/utils/MD5Utils.INSTANCE Lio/legado/app/utils/MD5Utils;
      // 074: aload 1
      // 075: invokevirtual io/legado/app/data/entities/Book.getBookUrl ()Ljava/lang/String;
      // 078: invokevirtual io/legado/app/utils/MD5Utils.md5Encode (Ljava/lang/String;)Ljava/lang/String;
      // 07b: invokevirtual java/lang/String.toString ()Ljava/lang/String;
      // 07e: astore 8
      // 080: aconst_null
      // 081: astore 9
      // 083: aload 0
      // 084: aload 4
      // 086: invokespecial com/htmake/reader/api/controller/BookController.getBookChaptersCache (Ljava/lang/String;)Lio/legado/app/utils/ACache;
      // 089: astore 10
      // 08b: aload 1
      // 08c: invokevirtual io/legado/app/data/entities/Book.isInShelf ()Z
      // 08f: ifeq 0cf
      // 092: aload 0
      // 093: aload 4
      // 095: bipush 2
      // 096: anewarray 96
      // 099: astore 11
      // 09b: aload 11
      // 09d: bipush 0
      // 09e: new java/lang/StringBuilder
      // 0a1: dup
      // 0a2: invokespecial java/lang/StringBuilder.<init> ()V
      // 0a5: aload 1
      // 0a6: invokevirtual io/legado/app/data/entities/Book.getName ()Ljava/lang/String;
      // 0a9: invokevirtual java/lang/StringBuilder.append (Ljava/lang/String;)Ljava/lang/StringBuilder;
      // 0ac: bipush 95
      // 0ae: invokevirtual java/lang/StringBuilder.append (C)Ljava/lang/StringBuilder;
      // 0b1: aload 1
      // 0b2: invokevirtual io/legado/app/data/entities/Book.getAuthor ()Ljava/lang/String;
      // 0b5: invokevirtual java/lang/StringBuilder.append (Ljava/lang/String;)Ljava/lang/StringBuilder;
      // 0b8: invokevirtual java/lang/StringBuilder.toString ()Ljava/lang/String;
      // 0bb: aastore
      // 0bc: aload 11
      // 0be: bipush 1
      // 0bf: aload 8
      // 0c1: aastore
      // 0c2: aload 11
      // 0c4: invokevirtual com/htmake/reader/api/controller/BookController.getUserStorage (Ljava/lang/Object;[Ljava/lang/String;)Ljava/lang/String;
      // 0c7: invokestatic com/htmake/reader/utils/ExtKt.asJsonArray (Ljava/lang/Object;)Lio/vertx/core/json/JsonArray;
      // 0ca: astore 9
      // 0cc: goto 0fb
      // 0cf: aload 10
      // 0d1: new java/lang/StringBuilder
      // 0d4: dup
      // 0d5: invokespecial java/lang/StringBuilder.<init> ()V
      // 0d8: aload 1
      // 0d9: invokevirtual io/legado/app/data/entities/Book.getName ()Ljava/lang/String;
      // 0dc: invokevirtual java/lang/StringBuilder.append (Ljava/lang/String;)Ljava/lang/StringBuilder;
      // 0df: bipush 95
      // 0e1: invokevirtual java/lang/StringBuilder.append (C)Ljava/lang/StringBuilder;
      // 0e4: aload 1
      // 0e5: invokevirtual io/legado/app/data/entities/Book.getAuthor ()Ljava/lang/String;
      // 0e8: invokevirtual java/lang/StringBuilder.append (Ljava/lang/String;)Ljava/lang/StringBuilder;
      // 0eb: aload 8
      // 0ed: invokevirtual java/lang/StringBuilder.append (Ljava/lang/String;)Ljava/lang/StringBuilder;
      // 0f0: invokevirtual java/lang/StringBuilder.toString ()Ljava/lang/String;
      // 0f3: invokevirtual io/legado/app/utils/ACache.getAsString (Ljava/lang/String;)Ljava/lang/String;
      // 0f6: invokestatic com/htmake/reader/utils/ExtKt.asJsonArray (Ljava/lang/Object;)Lio/vertx/core/json/JsonArray;
      // 0f9: astore 9
      // 0fb: aload 9
      // 0fd: ifnull 104
      // 100: iload 3
      // 101: ifeq 641
      // 104: aconst_null
      // 105: astore 11
      // 107: aload 1
      // 108: aconst_null
      // 109: bipush 1
      // 10a: aconst_null
      // 10b: invokestatic com/htmake/reader/utils/ExtKt.getWorkDir$default (Ljava/lang/String;ILjava/lang/Object;)Ljava/lang/String;
      // 10e: invokevirtual io/legado/app/data/entities/Book.setRootDir (Ljava/lang/String;)V
      // 111: aload 1
      // 112: aload 4
      // 114: invokevirtual io/legado/app/data/entities/Book.setUserNameSpace (Ljava/lang/String;)V
      // 117: aload 1
      // 118: invokevirtual io/legado/app/data/entities/Book.isLocalBook ()Z
      // 11b: ifeq 196
      // 11e: aload 1
      // 11f: invokevirtual io/legado/app/data/entities/Book.isEpub ()Z
      // 122: ifeq 141
      // 125: aload 0
      // 126: aload 1
      // 127: iload 3
      // 128: ifeq 12f
      // 12b: bipush 1
      // 12c: goto 130
      // 12f: bipush 0
      // 130: invokevirtual com/htmake/reader/api/controller/BookController.extractEpub (Lio/legado/app/data/entities/Book;Z)Z
      // 133: ifne 141
      // 136: new java/lang/Exception
      // 139: dup
      // 13a: ldc_w "Epub书籍解压失败"
      // 13d: invokespecial java/lang/Exception.<init> (Ljava/lang/String;)V
      // 140: athrow
      // 141: aload 1
      // 142: invokevirtual io/legado/app/data/entities/Book.isCbz ()Z
      // 145: ifeq 164
      // 148: aload 0
      // 149: aload 1
      // 14a: iload 3
      // 14b: ifeq 152
      // 14e: bipush 1
      // 14f: goto 153
      // 152: bipush 0
      // 153: invokevirtual com/htmake/reader/api/controller/BookController.extractCbz (Lio/legado/app/data/entities/Book;Z)Z
      // 156: ifne 164
      // 159: new java/lang/Exception
      // 15c: dup
      // 15d: ldc_w "CBZ书籍解压失败"
      // 160: invokespecial java/lang/Exception.<init> (Ljava/lang/String;)V
      // 163: athrow
      // 164: aload 1
      // 165: invokevirtual io/legado/app/data/entities/Book.isPdf ()Z
      // 168: ifeq 187
      // 16b: aload 0
      // 16c: aload 1
      // 16d: iload 3
      // 16e: ifeq 175
      // 171: bipush 1
      // 172: goto 176
      // 175: bipush 0
      // 176: invokevirtual com/htmake/reader/api/controller/BookController.convertPdfToImage (Lio/legado/app/data/entities/Book;Z)Z
      // 179: ifne 187
      // 17c: new java/lang/Exception
      // 17f: dup
      // 180: ldc_w "PDF书籍转换失败"
      // 183: invokespecial java/lang/Exception.<init> (Ljava/lang/String;)V
      // 186: athrow
      // 187: getstatic io/legado/app/model/localBook/LocalBook.INSTANCE Lio/legado/app/model/localBook/LocalBook;
      // 18a: aload 1
      // 18b: invokevirtual io/legado/app/model/localBook/LocalBook.getChapterList (Lio/legado/app/data/entities/Book;)Ljava/util/ArrayList;
      // 18e: checkcast java/util/List
      // 191: astore 11
      // 193: goto 56f
      // 196: nop
      // 197: aload 2
      // 198: checkcast java/lang/CharSequence
      // 19b: astore 12
      // 19d: bipush 0
      // 19e: istore 13
      // 1a0: bipush 0
      // 1a1: istore 14
      // 1a3: aload 12
      // 1a5: ifnull 1b2
      // 1a8: aload 12
      // 1aa: invokeinterface java/lang/CharSequence.length ()I 1
      // 1af: ifne 1b6
      // 1b2: bipush 1
      // 1b3: goto 1b7
      // 1b6: bipush 0
      // 1b7: ifne 2f8
      // 1ba: aconst_null
      // 1bb: astore 12
      // 1bd: getstatic io/legado/app/data/entities/BookSource.Companion Lio/legado/app/data/entities/BookSource$Companion;
      // 1c0: aload 2
      // 1c1: invokevirtual io/legado/app/data/entities/BookSource$Companion.fromJson-IoAF18A (Ljava/lang/String;)Ljava/lang/Object;
      // 1c4: astore 13
      // 1c6: bipush 0
      // 1c7: istore 14
      // 1c9: aload 13
      // 1cb: invokestatic kotlin/Result.isFailure-impl (Ljava/lang/Object;)Z
      // 1ce: ifeq 1d5
      // 1d1: aconst_null
      // 1d2: goto 1d7
      // 1d5: aload 13
      // 1d7: astore 12
      // 1d9: aload 12
      // 1db: checkcast io/legado/app/data/entities/BookSource
      // 1de: astore 13
      // 1e0: aload 13
      // 1e2: ifnonnull 1e8
      // 1e5: goto 234
      // 1e8: aload 13
      // 1ea: invokevirtual io/legado/app/data/entities/BookSource.getRuleToc ()Lio/legado/app/data/entities/rule/TocRule;
      // 1ed: astore 14
      // 1ef: aload 14
      // 1f1: ifnonnull 1f7
      // 1f4: goto 234
      // 1f7: aload 14
      // 1f9: invokevirtual io/legado/app/data/entities/rule/TocRule.getPreUpdateJs ()Ljava/lang/String;
      // 1fc: astore 15
      // 1fe: aload 15
      // 200: ifnonnull 206
      // 203: goto 234
      // 206: aload 15
      // 208: astore 16
      // 20a: bipush 0
      // 20b: istore 17
      // 20d: bipush 0
      // 20e: istore 18
      // 210: aload 16
      // 212: astore 19
      // 214: bipush 0
      // 215: istore 20
      // 217: new io/legado/app/model/analyzeRule/AnalyzeRule
      // 21a: dup
      // 21b: aload 1
      // 21c: checkcast io/legado/app/model/analyzeRule/RuleDataInterface
      // 21f: aload 12
      // 221: checkcast io/legado/app/data/entities/BaseSource
      // 224: aconst_null
      // 225: bipush 4
      // 226: aconst_null
      // 227: invokespecial io/legado/app/model/analyzeRule/AnalyzeRule.<init> (Lio/legado/app/model/analyzeRule/RuleDataInterface;Lio/legado/app/data/entities/BaseSource;Lio/legado/app/model/DebugLog;ILkotlin/jvm/internal/DefaultConstructorMarker;)V
      // 22a: aload 19
      // 22c: aconst_null
      // 22d: bipush 2
      // 22e: aconst_null
      // 22f: invokestatic io/legado/app/model/analyzeRule/AnalyzeRule.evalJS$default (Lio/legado/app/model/analyzeRule/AnalyzeRule;Ljava/lang/String;Ljava/lang/Object;ILjava/lang/Object;)Ljava/lang/Object;
      // 232: pop
      // 233: nop
      // 234: aload 1
      // 235: invokevirtual io/legado/app/data/entities/Book.getTocUrl ()Ljava/lang/String;
      // 238: checkcast java/lang/CharSequence
      // 23b: invokestatic kotlin/text/StringsKt.isBlank (Ljava/lang/CharSequence;)Z
      // 23e: ifeq 2f8
      // 241: new io/legado/app/model/webBook/WebBook
      // 244: dup
      // 245: aload 2
      // 246: iload 5
      // 248: ifeq 24f
      // 24b: bipush 1
      // 24c: goto 250
      // 24f: bipush 0
      // 250: aconst_null
      // 251: aload 4
      // 253: bipush 4
      // 254: aconst_null
      // 255: invokespecial io/legado/app/model/webBook/WebBook.<init> (Ljava/lang/String;ZLio/legado/app/model/DebugLog;Ljava/lang/String;ILkotlin/jvm/internal/DefaultConstructorMarker;)V
      // 258: aload 1
      // 259: bipush 0
      // 25a: aload 22
      // 25c: bipush 2
      // 25d: aconst_null
      // 25e: aload 22
      // 260: aload 0
      // 261: putfield com/htmake/reader/api/controller/BookController$getLocalChapterList$1.L$0 Ljava/lang/Object;
      // 264: aload 22
      // 266: aload 1
      // 267: putfield com/htmake/reader/api/controller/BookController$getLocalChapterList$1.L$1 Ljava/lang/Object;
      // 26a: aload 22
      // 26c: aload 2
      // 26d: putfield com/htmake/reader/api/controller/BookController$getLocalChapterList$1.L$2 Ljava/lang/Object;
      // 270: aload 22
      // 272: aload 4
      // 274: putfield com/htmake/reader/api/controller/BookController$getLocalChapterList$1.L$3 Ljava/lang/Object;
      // 277: aload 22
      // 279: aload 6
      // 27b: putfield com/htmake/reader/api/controller/BookController$getLocalChapterList$1.L$4 Ljava/lang/Object;
      // 27e: aload 22
      // 280: aload 8
      // 282: putfield com/htmake/reader/api/controller/BookController$getLocalChapterList$1.L$5 Ljava/lang/Object;
      // 285: aload 22
      // 287: aload 10
      // 289: putfield com/htmake/reader/api/controller/BookController$getLocalChapterList$1.L$6 Ljava/lang/Object;
      // 28c: aload 22
      // 28e: iload 5
      // 290: putfield com/htmake/reader/api/controller/BookController$getLocalChapterList$1.Z$0 Z
      // 293: aload 22
      // 295: bipush 1
      // 296: putfield com/htmake/reader/api/controller/BookController$getLocalChapterList$1.label I
      // 299: invokestatic io/legado/app/model/webBook/WebBook.getBookInfo$default (Lio/legado/app/model/webBook/WebBook;Lio/legado/app/data/entities/Book;ZLkotlin/coroutines/Continuation;ILjava/lang/Object;)Ljava/lang/Object;
      // 29c: dup
      // 29d: aload 23
      // 29f: if_acmpne 2f7
      // 2a2: aload 23
      // 2a4: areturn
      // 2a5: aload 22
      // 2a7: getfield com/htmake/reader/api/controller/BookController$getLocalChapterList$1.Z$0 Z
      // 2aa: istore 5
      // 2ac: aload 22
      // 2ae: getfield com/htmake/reader/api/controller/BookController$getLocalChapterList$1.L$6 Ljava/lang/Object;
      // 2b1: checkcast io/legado/app/utils/ACache
      // 2b4: astore 10
      // 2b6: aload 22
      // 2b8: getfield com/htmake/reader/api/controller/BookController$getLocalChapterList$1.L$5 Ljava/lang/Object;
      // 2bb: checkcast java/lang/String
      // 2be: astore 8
      // 2c0: aload 22
      // 2c2: getfield com/htmake/reader/api/controller/BookController$getLocalChapterList$1.L$4 Ljava/lang/Object;
      // 2c5: checkcast kotlinx/coroutines/sync/Mutex
      // 2c8: astore 6
      // 2ca: aload 22
      // 2cc: getfield com/htmake/reader/api/controller/BookController$getLocalChapterList$1.L$3 Ljava/lang/Object;
      // 2cf: checkcast java/lang/String
      // 2d2: astore 4
      // 2d4: aload 22
      // 2d6: getfield com/htmake/reader/api/controller/BookController$getLocalChapterList$1.L$2 Ljava/lang/Object;
      // 2d9: checkcast java/lang/String
      // 2dc: astore 2
      // 2dd: aload 22
      // 2df: getfield com/htmake/reader/api/controller/BookController$getLocalChapterList$1.L$1 Ljava/lang/Object;
      // 2e2: checkcast io/legado/app/data/entities/Book
      // 2e5: astore 1
      // 2e6: aload 22
      // 2e8: getfield com/htmake/reader/api/controller/BookController$getLocalChapterList$1.L$0 Ljava/lang/Object;
      // 2eb: checkcast com/htmake/reader/api/controller/BookController
      // 2ee: astore 0
      // 2ef: nop
      // 2f0: aload 21
      // 2f2: invokestatic kotlin/ResultKt.throwOnFailure (Ljava/lang/Object;)V
      // 2f5: aload 21
      // 2f7: pop
      // 2f8: new io/legado/app/model/webBook/WebBook
      // 2fb: dup
      // 2fc: aload 2
      // 2fd: dup
      // 2fe: invokestatic kotlin/jvm/internal/Intrinsics.checkNotNull (Ljava/lang/Object;)V
      // 301: iload 5
      // 303: ifeq 30a
      // 306: bipush 1
      // 307: goto 30b
      // 30a: bipush 0
      // 30b: aconst_null
      // 30c: aload 4
      // 30e: bipush 4
      // 30f: aconst_null
      // 310: invokespecial io/legado/app/model/webBook/WebBook.<init> (Ljava/lang/String;ZLio/legado/app/model/DebugLog;Ljava/lang/String;ILkotlin/jvm/internal/DefaultConstructorMarker;)V
      // 313: aload 1
      // 314: aload 22
      // 316: aload 22
      // 318: aload 0
      // 319: putfield com/htmake/reader/api/controller/BookController$getLocalChapterList$1.L$0 Ljava/lang/Object;
      // 31c: aload 22
      // 31e: aload 1
      // 31f: putfield com/htmake/reader/api/controller/BookController$getLocalChapterList$1.L$1 Ljava/lang/Object;
      // 322: aload 22
      // 324: aload 2
      // 325: putfield com/htmake/reader/api/controller/BookController$getLocalChapterList$1.L$2 Ljava/lang/Object;
      // 328: aload 22
      // 32a: aload 4
      // 32c: putfield com/htmake/reader/api/controller/BookController$getLocalChapterList$1.L$3 Ljava/lang/Object;
      // 32f: aload 22
      // 331: aload 6
      // 333: putfield com/htmake/reader/api/controller/BookController$getLocalChapterList$1.L$4 Ljava/lang/Object;
      // 336: aload 22
      // 338: aload 8
      // 33a: putfield com/htmake/reader/api/controller/BookController$getLocalChapterList$1.L$5 Ljava/lang/Object;
      // 33d: aload 22
      // 33f: aload 10
      // 341: putfield com/htmake/reader/api/controller/BookController$getLocalChapterList$1.L$6 Ljava/lang/Object;
      // 344: aload 22
      // 346: bipush 2
      // 347: putfield com/htmake/reader/api/controller/BookController$getLocalChapterList$1.label I
      // 34a: invokevirtual io/legado/app/model/webBook/WebBook.getChapterList (Lio/legado/app/data/entities/Book;Lkotlin/coroutines/Continuation;)Ljava/lang/Object;
      // 34d: dup
      // 34e: aload 23
      // 350: if_acmpne 3a1
      // 353: aload 23
      // 355: areturn
      // 356: aload 22
      // 358: getfield com/htmake/reader/api/controller/BookController$getLocalChapterList$1.L$6 Ljava/lang/Object;
      // 35b: checkcast io/legado/app/utils/ACache
      // 35e: astore 10
      // 360: aload 22
      // 362: getfield com/htmake/reader/api/controller/BookController$getLocalChapterList$1.L$5 Ljava/lang/Object;
      // 365: checkcast java/lang/String
      // 368: astore 8
      // 36a: aload 22
      // 36c: getfield com/htmake/reader/api/controller/BookController$getLocalChapterList$1.L$4 Ljava/lang/Object;
      // 36f: checkcast kotlinx/coroutines/sync/Mutex
      // 372: astore 6
      // 374: aload 22
      // 376: getfield com/htmake/reader/api/controller/BookController$getLocalChapterList$1.L$3 Ljava/lang/Object;
      // 379: checkcast java/lang/String
      // 37c: astore 4
      // 37e: aload 22
      // 380: getfield com/htmake/reader/api/controller/BookController$getLocalChapterList$1.L$2 Ljava/lang/Object;
      // 383: checkcast java/lang/String
      // 386: astore 2
      // 387: aload 22
      // 389: getfield com/htmake/reader/api/controller/BookController$getLocalChapterList$1.L$1 Ljava/lang/Object;
      // 38c: checkcast io/legado/app/data/entities/Book
      // 38f: astore 1
      // 390: aload 22
      // 392: getfield com/htmake/reader/api/controller/BookController$getLocalChapterList$1.L$0 Ljava/lang/Object;
      // 395: checkcast com/htmake/reader/api/controller/BookController
      // 398: astore 0
      // 399: nop
      // 39a: aload 21
      // 39c: invokestatic kotlin/ResultKt.throwOnFailure (Ljava/lang/Object;)V
      // 39f: aload 21
      // 3a1: checkcast java/util/List
      // 3a4: astore 11
      // 3a6: goto 56f
      // 3a9: astore 12
      // 3ab: aload 2
      // 3ac: checkcast java/lang/CharSequence
      // 3af: astore 13
      // 3b1: bipush 0
      // 3b2: istore 14
      // 3b4: bipush 0
      // 3b5: istore 15
      // 3b7: aload 13
      // 3b9: ifnull 3c6
      // 3bc: aload 13
      // 3be: invokeinterface java/lang/CharSequence.length ()I 1
      // 3c3: ifne 3ca
      // 3c6: bipush 1
      // 3c7: goto 3cb
      // 3ca: bipush 0
      // 3cb: ifne 43a
      // 3ce: getstatic io/legado/app/data/entities/BookSource.Companion Lio/legado/app/data/entities/BookSource$Companion;
      // 3d1: aload 2
      // 3d2: invokevirtual io/legado/app/data/entities/BookSource$Companion.fromJson-IoAF18A (Ljava/lang/String;)Ljava/lang/Object;
      // 3d5: astore 14
      // 3d7: bipush 0
      // 3d8: istore 15
      // 3da: aload 14
      // 3dc: invokestatic kotlin/Result.isFailure-impl (Ljava/lang/Object;)Z
      // 3df: ifeq 3e6
      // 3e2: aconst_null
      // 3e3: goto 3e8
      // 3e6: aload 14
      // 3e8: checkcast io/legado/app/data/entities/BookSource
      // 3eb: astore 13
      // 3ed: aload 13
      // 3ef: ifnull 43a
      // 3f2: bipush 3
      // 3f3: anewarray 764
      // 3f6: astore 15
      // 3f8: aload 15
      // 3fa: bipush 0
      // 3fb: ldc_w "sourceUrl"
      // 3fe: aload 13
      // 400: invokevirtual io/legado/app/data/entities/BookSource.getBookSourceUrl ()Ljava/lang/String;
      // 403: invokestatic kotlin/TuplesKt.to (Ljava/lang/Object;Ljava/lang/Object;)Lkotlin/Pair;
      // 406: aastore
      // 407: aload 15
      // 409: bipush 1
      // 40a: ldc_w "time"
      // 40d: invokestatic java/lang/System.currentTimeMillis ()J
      // 410: invokestatic kotlin/coroutines/jvm/internal/Boxing.boxLong (J)Ljava/lang/Long;
      // 413: invokestatic kotlin/TuplesKt.to (Ljava/lang/Object;Ljava/lang/Object;)Lkotlin/Pair;
      // 416: aastore
      // 417: aload 15
      // 419: bipush 2
      // 41a: ldc_w "error"
      // 41d: aload 12
      // 41f: invokevirtual java/lang/Exception.toString ()Ljava/lang/String;
      // 422: invokestatic kotlin/TuplesKt.to (Ljava/lang/Object;Ljava/lang/Object;)Lkotlin/Pair;
      // 425: aastore
      // 426: aload 15
      // 428: invokestatic kotlin/collections/MapsKt.mutableMapOf ([Lkotlin/Pair;)Ljava/util/Map;
      // 42b: astore 14
      // 42d: aload 0
      // 42e: aload 13
      // 430: invokevirtual io/legado/app/data/entities/BookSource.getBookSourceUrl ()Ljava/lang/String;
      // 433: aload 14
      // 435: aload 4
      // 437: invokespecial com/htmake/reader/api/controller/BookController.addInvalidBookSource (Ljava/lang/String;Ljava/util/Map;Ljava/lang/String;)V
      // 43a: nop
      // 43b: aload 6
      // 43d: astore 13
      // 43f: aload 13
      // 441: ifnonnull 447
      // 444: goto 4c6
      // 447: aload 13
      // 449: aconst_null
      // 44a: aload 22
      // 44c: bipush 1
      // 44d: aconst_null
      // 44e: aload 22
      // 450: aload 0
      // 451: putfield com/htmake/reader/api/controller/BookController$getLocalChapterList$1.L$0 Ljava/lang/Object;
      // 454: aload 22
      // 456: aload 1
      // 457: putfield com/htmake/reader/api/controller/BookController$getLocalChapterList$1.L$1 Ljava/lang/Object;
      // 45a: aload 22
      // 45c: aload 4
      // 45e: putfield com/htmake/reader/api/controller/BookController$getLocalChapterList$1.L$2 Ljava/lang/Object;
      // 461: aload 22
      // 463: aload 6
      // 465: putfield com/htmake/reader/api/controller/BookController$getLocalChapterList$1.L$3 Ljava/lang/Object;
      // 468: aload 22
      // 46a: aload 12
      // 46c: putfield com/htmake/reader/api/controller/BookController$getLocalChapterList$1.L$4 Ljava/lang/Object;
      // 46f: aload 22
      // 471: aconst_null
      // 472: putfield com/htmake/reader/api/controller/BookController$getLocalChapterList$1.L$5 Ljava/lang/Object;
      // 475: aload 22
      // 477: aconst_null
      // 478: putfield com/htmake/reader/api/controller/BookController$getLocalChapterList$1.L$6 Ljava/lang/Object;
      // 47b: aload 22
      // 47d: bipush 3
      // 47e: putfield com/htmake/reader/api/controller/BookController$getLocalChapterList$1.label I
      // 481: invokestatic kotlinx/coroutines/sync/Mutex$DefaultImpls.lock$default (Lkotlinx/coroutines/sync/Mutex;Ljava/lang/Object;Lkotlin/coroutines/Continuation;ILjava/lang/Object;)Ljava/lang/Object;
      // 484: dup
      // 485: aload 23
      // 487: if_acmpne 4c5
      // 48a: aload 23
      // 48c: areturn
      // 48d: aload 22
      // 48f: getfield com/htmake/reader/api/controller/BookController$getLocalChapterList$1.L$4 Ljava/lang/Object;
      // 492: checkcast java/lang/Exception
      // 495: astore 12
      // 497: aload 22
      // 499: getfield com/htmake/reader/api/controller/BookController$getLocalChapterList$1.L$3 Ljava/lang/Object;
      // 49c: checkcast kotlinx/coroutines/sync/Mutex
      // 49f: astore 6
      // 4a1: aload 22
      // 4a3: getfield com/htmake/reader/api/controller/BookController$getLocalChapterList$1.L$2 Ljava/lang/Object;
      // 4a6: checkcast java/lang/String
      // 4a9: astore 4
      // 4ab: aload 22
      // 4ad: getfield com/htmake/reader/api/controller/BookController$getLocalChapterList$1.L$1 Ljava/lang/Object;
      // 4b0: checkcast io/legado/app/data/entities/Book
      // 4b3: astore 1
      // 4b4: aload 22
      // 4b6: getfield com/htmake/reader/api/controller/BookController$getLocalChapterList$1.L$0 Ljava/lang/Object;
      // 4b9: checkcast com/htmake/reader/api/controller/BookController
      // 4bc: astore 0
      // 4bd: nop
      // 4be: aload 21
      // 4c0: invokestatic kotlin/ResultKt.throwOnFailure (Ljava/lang/Object;)V
      // 4c3: aload 21
      // 4c5: pop
      // 4c6: aload 1
      // 4c7: aload 12
      // 4c9: invokevirtual java/lang/Exception.toString ()Ljava/lang/String;
      // 4cc: invokevirtual io/legado/app/data/entities/Book.setLastCheckError (Ljava/lang/String;)V
      // 4cf: aload 0
      // 4d0: aload 1
      // 4d1: aload 4
      // 4d3: new com/htmake/reader/api/controller/BookController$getLocalChapterList$3
      // 4d6: dup
      // 4d7: aload 12
      // 4d9: invokespecial com/htmake/reader/api/controller/BookController$getLocalChapterList$3.<init> (Ljava/lang/Exception;)V
      // 4dc: checkcast kotlin/jvm/functions/Function1
      // 4df: aload 22
      // 4e1: aload 22
      // 4e3: aload 6
      // 4e5: putfield com/htmake/reader/api/controller/BookController$getLocalChapterList$1.L$0 Ljava/lang/Object;
      // 4e8: aload 22
      // 4ea: aload 12
      // 4ec: putfield com/htmake/reader/api/controller/BookController$getLocalChapterList$1.L$1 Ljava/lang/Object;
      // 4ef: aload 22
      // 4f1: aconst_null
      // 4f2: putfield com/htmake/reader/api/controller/BookController$getLocalChapterList$1.L$2 Ljava/lang/Object;
      // 4f5: aload 22
      // 4f7: aconst_null
      // 4f8: putfield com/htmake/reader/api/controller/BookController$getLocalChapterList$1.L$3 Ljava/lang/Object;
      // 4fb: aload 22
      // 4fd: aconst_null
      // 4fe: putfield com/htmake/reader/api/controller/BookController$getLocalChapterList$1.L$4 Ljava/lang/Object;
      // 501: aload 22
      // 503: aconst_null
      // 504: putfield com/htmake/reader/api/controller/BookController$getLocalChapterList$1.L$5 Ljava/lang/Object;
      // 507: aload 22
      // 509: aconst_null
      // 50a: putfield com/htmake/reader/api/controller/BookController$getLocalChapterList$1.L$6 Ljava/lang/Object;
      // 50d: aload 22
      // 50f: bipush 4
      // 510: putfield com/htmake/reader/api/controller/BookController$getLocalChapterList$1.label I
      // 513: invokevirtual com/htmake/reader/api/controller/BookController.editShelfBook (Lio/legado/app/data/entities/Book;Ljava/lang/String;Lkotlin/jvm/functions/Function1;Lkotlin/coroutines/Continuation;)Ljava/lang/Object;
      // 516: dup
      // 517: aload 23
      // 519: if_acmpne 53b
      // 51c: aload 23
      // 51e: areturn
      // 51f: aload 22
      // 521: getfield com/htmake/reader/api/controller/BookController$getLocalChapterList$1.L$1 Ljava/lang/Object;
      // 524: checkcast java/lang/Exception
      // 527: astore 12
      // 529: aload 22
      // 52b: getfield com/htmake/reader/api/controller/BookController$getLocalChapterList$1.L$0 Ljava/lang/Object;
      // 52e: checkcast kotlinx/coroutines/sync/Mutex
      // 531: astore 6
      // 533: nop
      // 534: aload 21
      // 536: invokestatic kotlin/ResultKt.throwOnFailure (Ljava/lang/Object;)V
      // 539: aload 21
      // 53b: pop
      // 53c: aload 6
      // 53e: astore 13
      // 540: aload 13
      // 542: ifnonnull 548
      // 545: goto 550
      // 548: aload 13
      // 54a: aconst_null
      // 54b: bipush 1
      // 54c: aconst_null
      // 54d: invokestatic kotlinx/coroutines/sync/Mutex$DefaultImpls.unlock$default (Lkotlinx/coroutines/sync/Mutex;Ljava/lang/Object;ILjava/lang/Object;)V
      // 550: goto 56c
      // 553: astore 13
      // 555: aload 6
      // 557: astore 14
      // 559: aload 14
      // 55b: ifnonnull 561
      // 55e: goto 569
      // 561: aload 14
      // 563: aconst_null
      // 564: bipush 1
      // 565: aconst_null
      // 566: invokestatic kotlinx/coroutines/sync/Mutex$DefaultImpls.unlock$default (Lkotlinx/coroutines/sync/Mutex;Ljava/lang/Object;ILjava/lang/Object;)V
      // 569: aload 13
      // 56b: athrow
      // 56c: aload 12
      // 56e: athrow
      // 56f: aload 1
      // 570: invokevirtual io/legado/app/data/entities/Book.isInShelf ()Z
      // 573: ifeq 5b3
      // 576: aload 0
      // 577: aload 4
      // 579: bipush 2
      // 57a: anewarray 96
      // 57d: astore 12
      // 57f: aload 12
      // 581: bipush 0
      // 582: new java/lang/StringBuilder
      // 585: dup
      // 586: invokespecial java/lang/StringBuilder.<init> ()V
      // 589: aload 1
      // 58a: invokevirtual io/legado/app/data/entities/Book.getName ()Ljava/lang/String;
      // 58d: invokevirtual java/lang/StringBuilder.append (Ljava/lang/String;)Ljava/lang/StringBuilder;
      // 590: bipush 95
      // 592: invokevirtual java/lang/StringBuilder.append (C)Ljava/lang/StringBuilder;
      // 595: aload 1
      // 596: invokevirtual io/legado/app/data/entities/Book.getAuthor ()Ljava/lang/String;
      // 599: invokevirtual java/lang/StringBuilder.append (Ljava/lang/String;)Ljava/lang/StringBuilder;
      // 59c: invokevirtual java/lang/StringBuilder.toString ()Ljava/lang/String;
      // 59f: aastore
      // 5a0: aload 12
      // 5a2: bipush 1
      // 5a3: aload 8
      // 5a5: aastore
      // 5a6: aload 12
      // 5a8: invokestatic com/htmake/reader/utils/ExtKt.getRelativePath ([Ljava/lang/String;)Ljava/lang/String;
      // 5ab: aload 11
      // 5ad: invokevirtual com/htmake/reader/api/controller/BookController.saveUserStorage (Ljava/lang/Object;Ljava/lang/String;Ljava/lang/Object;)V
      // 5b0: goto 5e5
      // 5b3: aload 10
      // 5b5: new java/lang/StringBuilder
      // 5b8: dup
      // 5b9: invokespecial java/lang/StringBuilder.<init> ()V
      // 5bc: aload 1
      // 5bd: invokevirtual io/legado/app/data/entities/Book.getName ()Ljava/lang/String;
      // 5c0: invokevirtual java/lang/StringBuilder.append (Ljava/lang/String;)Ljava/lang/StringBuilder;
      // 5c3: bipush 95
      // 5c5: invokevirtual java/lang/StringBuilder.append (C)Ljava/lang/StringBuilder;
      // 5c8: aload 1
      // 5c9: invokevirtual io/legado/app/data/entities/Book.getAuthor ()Ljava/lang/String;
      // 5cc: invokevirtual java/lang/StringBuilder.append (Ljava/lang/String;)Ljava/lang/StringBuilder;
      // 5cf: aload 8
      // 5d1: invokevirtual java/lang/StringBuilder.append (Ljava/lang/String;)Ljava/lang/StringBuilder;
      // 5d4: invokevirtual java/lang/StringBuilder.toString ()Ljava/lang/String;
      // 5d7: aload 11
      // 5d9: bipush 0
      // 5da: bipush 2
      // 5db: aconst_null
      // 5dc: invokestatic com/htmake/reader/utils/ExtKt.jsonEncode$default (Ljava/lang/Object;ZILjava/lang/Object;)Ljava/lang/String;
      // 5df: sipush 3600
      // 5e2: invokevirtual io/legado/app/utils/ACache.put (Ljava/lang/String;Ljava/lang/String;I)V
      // 5e5: aload 0
      // 5e6: aload 1
      // 5e7: aload 11
      // 5e9: aload 4
      // 5eb: aload 6
      // 5ed: aload 22
      // 5ef: aload 22
      // 5f1: aload 11
      // 5f3: putfield com/htmake/reader/api/controller/BookController$getLocalChapterList$1.L$0 Ljava/lang/Object;
      // 5f6: aload 22
      // 5f8: aconst_null
      // 5f9: putfield com/htmake/reader/api/controller/BookController$getLocalChapterList$1.L$1 Ljava/lang/Object;
      // 5fc: aload 22
      // 5fe: aconst_null
      // 5ff: putfield com/htmake/reader/api/controller/BookController$getLocalChapterList$1.L$2 Ljava/lang/Object;
      // 602: aload 22
      // 604: aconst_null
      // 605: putfield com/htmake/reader/api/controller/BookController$getLocalChapterList$1.L$3 Ljava/lang/Object;
      // 608: aload 22
      // 60a: aconst_null
      // 60b: putfield com/htmake/reader/api/controller/BookController$getLocalChapterList$1.L$4 Ljava/lang/Object;
      // 60e: aload 22
      // 610: aconst_null
      // 611: putfield com/htmake/reader/api/controller/BookController$getLocalChapterList$1.L$5 Ljava/lang/Object;
      // 614: aload 22
      // 616: aconst_null
      // 617: putfield com/htmake/reader/api/controller/BookController$getLocalChapterList$1.L$6 Ljava/lang/Object;
      // 61a: aload 22
      // 61c: bipush 5
      // 61d: putfield com/htmake/reader/api/controller/BookController$getLocalChapterList$1.label I
      // 620: invokevirtual com/htmake/reader/api/controller/BookController.saveShelfBookLatestChapter (Lio/legado/app/data/entities/Book;Ljava/util/List;Ljava/lang/String;Lkotlinx/coroutines/sync/Mutex;Lkotlin/coroutines/Continuation;)Ljava/lang/Object;
      // 623: dup
      // 624: aload 23
      // 626: if_acmpne 63d
      // 629: aload 23
      // 62b: areturn
      // 62c: aload 22
      // 62e: getfield com/htmake/reader/api/controller/BookController$getLocalChapterList$1.L$0 Ljava/lang/Object;
      // 631: checkcast java/util/List
      // 634: astore 11
      // 636: aload 21
      // 638: invokestatic kotlin/ResultKt.throwOnFailure (Ljava/lang/Object;)V
      // 63b: aload 21
      // 63d: pop
      // 63e: aload 11
      // 640: areturn
      // 641: bipush 0
      // 642: istore 12
      // 644: new java/util/ArrayList
      // 647: dup
      // 648: invokespecial java/util/ArrayList.<init> ()V
      // 64b: astore 11
      // 64d: bipush 0
      // 64e: istore 12
      // 650: aload 9
      // 652: invokevirtual io/vertx/core/json/JsonArray.size ()I
      // 655: istore 13
      // 657: iload 12
      // 659: iload 13
      // 65b: if_icmpge 686
      // 65e: iload 12
      // 660: istore 14
      // 662: iinc 12 1
      // 665: aload 9
      // 667: iload 14
      // 669: invokevirtual io/vertx/core/json/JsonArray.getJsonObject (I)Lio/vertx/core/json/JsonObject;
      // 66c: ldc_w io/legado/app/data/entities/BookChapter
      // 66f: invokevirtual io/vertx/core/json/JsonObject.mapTo (Ljava/lang/Class;)Ljava/lang/Object;
      // 672: checkcast io/legado/app/data/entities/BookChapter
      // 675: astore 15
      // 677: aload 11
      // 679: aload 15
      // 67b: invokevirtual java/util/ArrayList.add (Ljava/lang/Object;)Z
      // 67e: pop
      // 67f: iload 12
      // 681: iload 13
      // 683: if_icmplt 65e
      // 686: aload 11
      // 688: areturn
      // 689: new java/lang/IllegalStateException
      // 68c: dup
      // 68d: ldc_w "call to 'resume' before 'invoke' with coroutine"
      // 690: invokespecial java/lang/IllegalStateException.<init> (Ljava/lang/String;)V
      // 693: athrow
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

   public suspend fun saveShelfBookLatestChapter(book: Book, bookChapterList: List<BookChapter>, userNameSpace: String, mutex: Mutex? = ...) {
      // $VF: Couldn't be decompiled
      // Please report this to the Vineflower issue tracker, at https://github.com/Vineflower/vineflower/issues with a copy of the class file (if you have the rights to distribute it!)
      // java.lang.IndexOutOfBoundsException: Index 0 out of bounds for length 0
      //   at java.base/jdk.internal.util.Preconditions.outOfBounds(Preconditions.java:64)
      //   at java.base/jdk.internal.util.Preconditions.outOfBoundsCheckIndex(Preconditions.java:70)
      //   at java.base/jdk.internal.util.Preconditions.checkIndex(Preconditions.java:266)
      //   at java.base/java.util.Objects.checkIndex(Objects.java:361)
      //   at java.base/java.util.ArrayList.remove(ArrayList.java:504)
      //   at org.jetbrains.java.decompiler.modules.decompiler.FinallyProcessor.removeExceptionInstructionsEx(FinallyProcessor.java:1057)
      //   at org.jetbrains.java.decompiler.modules.decompiler.FinallyProcessor.verifyFinallyEx(FinallyProcessor.java:572)
      //   at org.jetbrains.java.decompiler.modules.decompiler.FinallyProcessor.iterateGraph(FinallyProcessor.java:90)
      //
      // Bytecode:
      // 000: aload 5
      // 002: instanceof com/htmake/reader/api/controller/BookController$saveShelfBookLatestChapter$1
      // 005: ifeq 029
      // 008: aload 5
      // 00a: checkcast com/htmake/reader/api/controller/BookController$saveShelfBookLatestChapter$1
      // 00d: astore 9
      // 00f: aload 9
      // 011: getfield com/htmake/reader/api/controller/BookController$saveShelfBookLatestChapter$1.label I
      // 014: ldc -2147483648
      // 016: iand
      // 017: ifeq 029
      // 01a: aload 9
      // 01c: dup
      // 01d: getfield com/htmake/reader/api/controller/BookController$saveShelfBookLatestChapter$1.label I
      // 020: ldc -2147483648
      // 022: isub
      // 023: putfield com/htmake/reader/api/controller/BookController$saveShelfBookLatestChapter$1.label I
      // 026: goto 035
      // 029: new com/htmake/reader/api/controller/BookController$saveShelfBookLatestChapter$1
      // 02c: dup
      // 02d: aload 0
      // 02e: aload 5
      // 030: invokespecial com/htmake/reader/api/controller/BookController$saveShelfBookLatestChapter$1.<init> (Lcom/htmake/reader/api/controller/BookController;Lkotlin/coroutines/Continuation;)V
      // 033: astore 9
      // 035: aload 9
      // 037: getfield com/htmake/reader/api/controller/BookController$saveShelfBookLatestChapter$1.result Ljava/lang/Object;
      // 03a: astore 8
      // 03c: invokestatic kotlin/coroutines/intrinsics/IntrinsicsKt.getCOROUTINE_SUSPENDED ()Ljava/lang/Object;
      // 03f: astore 10
      // 041: aload 9
      // 043: getfield com/htmake/reader/api/controller/BookController$saveShelfBookLatestChapter$1.label I
      // 046: tableswitch 292 0 2 26 100 221
      // 060: aload 8
      // 062: invokestatic kotlin/ResultKt.throwOnFailure (Ljava/lang/Object;)V
      // 065: nop
      // 066: aload 4
      // 068: astore 6
      // 06a: aload 6
      // 06c: ifnonnull 072
      // 06f: goto 0e1
      // 072: aload 6
      // 074: aconst_null
      // 075: aload 9
      // 077: bipush 1
      // 078: aconst_null
      // 079: aload 9
      // 07b: aload 0
      // 07c: putfield com/htmake/reader/api/controller/BookController$saveShelfBookLatestChapter$1.L$0 Ljava/lang/Object;
      // 07f: aload 9
      // 081: aload 1
      // 082: putfield com/htmake/reader/api/controller/BookController$saveShelfBookLatestChapter$1.L$1 Ljava/lang/Object;
      // 085: aload 9
      // 087: aload 2
      // 088: putfield com/htmake/reader/api/controller/BookController$saveShelfBookLatestChapter$1.L$2 Ljava/lang/Object;
      // 08b: aload 9
      // 08d: aload 3
      // 08e: putfield com/htmake/reader/api/controller/BookController$saveShelfBookLatestChapter$1.L$3 Ljava/lang/Object;
      // 091: aload 9
      // 093: aload 4
      // 095: putfield com/htmake/reader/api/controller/BookController$saveShelfBookLatestChapter$1.L$4 Ljava/lang/Object;
      // 098: aload 9
      // 09a: bipush 1
      // 09b: putfield com/htmake/reader/api/controller/BookController$saveShelfBookLatestChapter$1.label I
      // 09e: invokestatic kotlinx/coroutines/sync/Mutex$DefaultImpls.lock$default (Lkotlinx/coroutines/sync/Mutex;Ljava/lang/Object;Lkotlin/coroutines/Continuation;ILjava/lang/Object;)Ljava/lang/Object;
      // 0a1: dup
      // 0a2: aload 10
      // 0a4: if_acmpne 0e0
      // 0a7: aload 10
      // 0a9: areturn
      // 0aa: aload 9
      // 0ac: getfield com/htmake/reader/api/controller/BookController$saveShelfBookLatestChapter$1.L$4 Ljava/lang/Object;
      // 0af: checkcast kotlinx/coroutines/sync/Mutex
      // 0b2: astore 4
      // 0b4: aload 9
      // 0b6: getfield com/htmake/reader/api/controller/BookController$saveShelfBookLatestChapter$1.L$3 Ljava/lang/Object;
      // 0b9: checkcast java/lang/String
      // 0bc: astore 3
      // 0bd: aload 9
      // 0bf: getfield com/htmake/reader/api/controller/BookController$saveShelfBookLatestChapter$1.L$2 Ljava/lang/Object;
      // 0c2: checkcast java/util/List
      // 0c5: astore 2
      // 0c6: aload 9
      // 0c8: getfield com/htmake/reader/api/controller/BookController$saveShelfBookLatestChapter$1.L$1 Ljava/lang/Object;
      // 0cb: checkcast io/legado/app/data/entities/Book
      // 0ce: astore 1
      // 0cf: aload 9
      // 0d1: getfield com/htmake/reader/api/controller/BookController$saveShelfBookLatestChapter$1.L$0 Ljava/lang/Object;
      // 0d4: checkcast com/htmake/reader/api/controller/BookController
      // 0d7: astore 0
      // 0d8: nop
      // 0d9: aload 8
      // 0db: invokestatic kotlin/ResultKt.throwOnFailure (Ljava/lang/Object;)V
      // 0de: aload 8
      // 0e0: pop
      // 0e1: aload 0
      // 0e2: aload 1
      // 0e3: aload 3
      // 0e4: new com/htmake/reader/api/controller/BookController$saveShelfBookLatestChapter$2
      // 0e7: dup
      // 0e8: aload 2
      // 0e9: aload 1
      // 0ea: invokespecial com/htmake/reader/api/controller/BookController$saveShelfBookLatestChapter$2.<init> (Ljava/util/List;Lio/legado/app/data/entities/Book;)V
      // 0ed: checkcast kotlin/jvm/functions/Function1
      // 0f0: aload 9
      // 0f2: aload 9
      // 0f4: aload 4
      // 0f6: putfield com/htmake/reader/api/controller/BookController$saveShelfBookLatestChapter$1.L$0 Ljava/lang/Object;
      // 0f9: aload 9
      // 0fb: aconst_null
      // 0fc: putfield com/htmake/reader/api/controller/BookController$saveShelfBookLatestChapter$1.L$1 Ljava/lang/Object;
      // 0ff: aload 9
      // 101: aconst_null
      // 102: putfield com/htmake/reader/api/controller/BookController$saveShelfBookLatestChapter$1.L$2 Ljava/lang/Object;
      // 105: aload 9
      // 107: aconst_null
      // 108: putfield com/htmake/reader/api/controller/BookController$saveShelfBookLatestChapter$1.L$3 Ljava/lang/Object;
      // 10b: aload 9
      // 10d: aconst_null
      // 10e: putfield com/htmake/reader/api/controller/BookController$saveShelfBookLatestChapter$1.L$4 Ljava/lang/Object;
      // 111: aload 9
      // 113: bipush 2
      // 114: putfield com/htmake/reader/api/controller/BookController$saveShelfBookLatestChapter$1.label I
      // 117: invokevirtual com/htmake/reader/api/controller/BookController.editShelfBook (Lio/legado/app/data/entities/Book;Ljava/lang/String;Lkotlin/jvm/functions/Function1;Lkotlin/coroutines/Continuation;)Ljava/lang/Object;
      // 11a: dup
      // 11b: aload 10
      // 11d: if_acmpne 135
      // 120: aload 10
      // 122: areturn
      // 123: aload 9
      // 125: getfield com/htmake/reader/api/controller/BookController$saveShelfBookLatestChapter$1.L$0 Ljava/lang/Object;
      // 128: checkcast kotlinx/coroutines/sync/Mutex
      // 12b: astore 4
      // 12d: nop
      // 12e: aload 8
      // 130: invokestatic kotlin/ResultKt.throwOnFailure (Ljava/lang/Object;)V
      // 133: aload 8
      // 135: pop
      // 136: aload 4
      // 138: astore 6
      // 13a: aload 6
      // 13c: ifnonnull 142
      // 13f: goto 14a
      // 142: aload 6
      // 144: aconst_null
      // 145: bipush 1
      // 146: aconst_null
      // 147: invokestatic kotlinx/coroutines/sync/Mutex$DefaultImpls.unlock$default (Lkotlinx/coroutines/sync/Mutex;Ljava/lang/Object;ILjava/lang/Object;)V
      // 14a: goto 166
      // 14d: astore 6
      // 14f: aload 4
      // 151: astore 7
      // 153: aload 7
      // 155: ifnonnull 15b
      // 158: goto 163
      // 15b: aload 7
      // 15d: aconst_null
      // 15e: bipush 1
      // 15f: aconst_null
      // 160: invokestatic kotlinx/coroutines/sync/Mutex$DefaultImpls.unlock$default (Lkotlinx/coroutines/sync/Mutex;Ljava/lang/Object;ILjava/lang/Object;)V
      // 163: aload 6
      // 165: athrow
      // 166: getstatic kotlin/Unit.INSTANCE Lkotlin/Unit;
      // 169: areturn
      // 16a: new java/lang/IllegalStateException
      // 16d: dup
      // 16e: ldc_w "call to 'resume' before 'invoke' with coroutine"
      // 171: invokespecial java/lang/IllegalStateException.<init> (Ljava/lang/String;)V
      // 174: athrow
   }

   public suspend fun editShelfBook(book: Book, userNameSpace: String, handler: (Book) -> Book): Book? {
      // $VF: Couldn't be decompiled
      // Please report this to the Vineflower issue tracker, at https://github.com/Vineflower/vineflower/issues with a copy of the class file (if you have the rights to distribute it!)
      // java.lang.IndexOutOfBoundsException: Index -1 out of bounds for length 0
      //   at java.base/jdk.internal.util.Preconditions.outOfBounds(Preconditions.java:64)
      //   at java.base/jdk.internal.util.Preconditions.outOfBoundsCheckIndex(Preconditions.java:70)
      //   at java.base/jdk.internal.util.Preconditions.checkIndex(Preconditions.java:266)
      //   at java.base/java.util.Objects.checkIndex(Objects.java:361)
      //   at java.base/java.util.ArrayList.remove(ArrayList.java:504)
      //   at org.jetbrains.java.decompiler.modules.decompiler.FinallyProcessor.removeExceptionInstructionsEx(FinallyProcessor.java:1064)
      //   at org.jetbrains.java.decompiler.modules.decompiler.FinallyProcessor.verifyFinallyEx(FinallyProcessor.java:565)
      //   at org.jetbrains.java.decompiler.modules.decompiler.FinallyProcessor.iterateGraph(FinallyProcessor.java:90)
      //
      // Bytecode:
      // 000: aload 4
      // 002: instanceof com/htmake/reader/api/controller/BookController$editShelfBook$1
      // 005: ifeq 029
      // 008: aload 4
      // 00a: checkcast com/htmake/reader/api/controller/BookController$editShelfBook$1
      // 00d: astore 15
      // 00f: aload 15
      // 011: getfield com/htmake/reader/api/controller/BookController$editShelfBook$1.label I
      // 014: ldc -2147483648
      // 016: iand
      // 017: ifeq 029
      // 01a: aload 15
      // 01c: dup
      // 01d: getfield com/htmake/reader/api/controller/BookController$editShelfBook$1.label I
      // 020: ldc -2147483648
      // 022: isub
      // 023: putfield com/htmake/reader/api/controller/BookController$editShelfBook$1.label I
      // 026: goto 035
      // 029: new com/htmake/reader/api/controller/BookController$editShelfBook$1
      // 02c: dup
      // 02d: aload 0
      // 02e: aload 4
      // 030: invokespecial com/htmake/reader/api/controller/BookController$editShelfBook$1.<init> (Lcom/htmake/reader/api/controller/BookController;Lkotlin/coroutines/Continuation;)V
      // 033: astore 15
      // 035: aload 15
      // 037: getfield com/htmake/reader/api/controller/BookController$editShelfBook$1.result Ljava/lang/Object;
      // 03a: astore 14
      // 03c: invokestatic kotlin/coroutines/intrinsics/IntrinsicsKt.getCOROUTINE_SUSPENDED ()Ljava/lang/Object;
      // 03f: astore 16
      // 041: aload 15
      // 043: getfield com/htmake/reader/api/controller/BookController$editShelfBook$1.label I
      // 046: tableswitch 646 0 2 26 85 208
      // 060: aload 14
      // 062: invokestatic kotlin/ResultKt.throwOnFailure (Ljava/lang/Object;)V
      // 065: getstatic com/htmake/reader/utils/UserMutex.INSTANCE Lcom/htmake/reader/utils/UserMutex;
      // 068: aload 2
      // 069: ldc_w "@bookshelf"
      // 06c: invokestatic kotlin/jvm/internal/Intrinsics.stringPlus (Ljava/lang/String;Ljava/lang/Object;)Ljava/lang/String;
      // 06f: aload 15
      // 071: aload 15
      // 073: aload 0
      // 074: putfield com/htmake/reader/api/controller/BookController$editShelfBook$1.L$0 Ljava/lang/Object;
      // 077: aload 15
      // 079: aload 1
      // 07a: putfield com/htmake/reader/api/controller/BookController$editShelfBook$1.L$1 Ljava/lang/Object;
      // 07d: aload 15
      // 07f: aload 2
      // 080: putfield com/htmake/reader/api/controller/BookController$editShelfBook$1.L$2 Ljava/lang/Object;
      // 083: aload 15
      // 085: aload 3
      // 086: putfield com/htmake/reader/api/controller/BookController$editShelfBook$1.L$3 Ljava/lang/Object;
      // 089: aload 15
      // 08b: bipush 1
      // 08c: putfield com/htmake/reader/api/controller/BookController$editShelfBook$1.label I
      // 08f: invokevirtual com/htmake/reader/utils/UserMutex.getLocker (Ljava/lang/String;Lkotlin/coroutines/Continuation;)Ljava/lang/Object;
      // 092: dup
      // 093: aload 16
      // 095: if_acmpne 0c6
      // 098: aload 16
      // 09a: areturn
      // 09b: aload 15
      // 09d: getfield com/htmake/reader/api/controller/BookController$editShelfBook$1.L$3 Ljava/lang/Object;
      // 0a0: checkcast kotlin/jvm/functions/Function1
      // 0a3: astore 3
      // 0a4: aload 15
      // 0a6: getfield com/htmake/reader/api/controller/BookController$editShelfBook$1.L$2 Ljava/lang/Object;
      // 0a9: checkcast java/lang/String
      // 0ac: astore 2
      // 0ad: aload 15
      // 0af: getfield com/htmake/reader/api/controller/BookController$editShelfBook$1.L$1 Ljava/lang/Object;
      // 0b2: checkcast io/legado/app/data/entities/Book
      // 0b5: astore 1
      // 0b6: aload 15
      // 0b8: getfield com/htmake/reader/api/controller/BookController$editShelfBook$1.L$0 Ljava/lang/Object;
      // 0bb: checkcast com/htmake/reader/api/controller/BookController
      // 0be: astore 0
      // 0bf: aload 14
      // 0c1: invokestatic kotlin/ResultKt.throwOnFailure (Ljava/lang/Object;)V
      // 0c4: aload 14
      // 0c6: checkcast kotlinx/coroutines/sync/Mutex
      // 0c9: astore 5
      // 0cb: nop
      // 0cc: invokestatic com/htmake/reader/api/controller/BookControllerKt.access$getLogger$p ()Lmu/KLogger;
      // 0cf: ldc_w "wait for lock {}"
      // 0d2: aload 2
      // 0d3: ldc_w "@bookshelf"
      // 0d6: invokestatic kotlin/jvm/internal/Intrinsics.stringPlus (Ljava/lang/String;Ljava/lang/Object;)Ljava/lang/String;
      // 0d9: invokeinterface mu/KLogger.info (Ljava/lang/String;Ljava/lang/Object;)V 3
      // 0de: aload 5
      // 0e0: aconst_null
      // 0e1: aload 15
      // 0e3: bipush 1
      // 0e4: aconst_null
      // 0e5: aload 15
      // 0e7: aload 0
      // 0e8: putfield com/htmake/reader/api/controller/BookController$editShelfBook$1.L$0 Ljava/lang/Object;
      // 0eb: aload 15
      // 0ed: aload 1
      // 0ee: putfield com/htmake/reader/api/controller/BookController$editShelfBook$1.L$1 Ljava/lang/Object;
      // 0f1: aload 15
      // 0f3: aload 2
      // 0f4: putfield com/htmake/reader/api/controller/BookController$editShelfBook$1.L$2 Ljava/lang/Object;
      // 0f7: aload 15
      // 0f9: aload 3
      // 0fa: putfield com/htmake/reader/api/controller/BookController$editShelfBook$1.L$3 Ljava/lang/Object;
      // 0fd: aload 15
      // 0ff: aload 5
      // 101: putfield com/htmake/reader/api/controller/BookController$editShelfBook$1.L$4 Ljava/lang/Object;
      // 104: aload 15
      // 106: bipush 2
      // 107: putfield com/htmake/reader/api/controller/BookController$editShelfBook$1.label I
      // 10a: invokestatic kotlinx/coroutines/sync/Mutex$DefaultImpls.lock$default (Lkotlinx/coroutines/sync/Mutex;Ljava/lang/Object;Lkotlin/coroutines/Continuation;ILjava/lang/Object;)Ljava/lang/Object;
      // 10d: dup
      // 10e: aload 16
      // 110: if_acmpne 14c
      // 113: aload 16
      // 115: areturn
      // 116: aload 15
      // 118: getfield com/htmake/reader/api/controller/BookController$editShelfBook$1.L$4 Ljava/lang/Object;
      // 11b: checkcast kotlinx/coroutines/sync/Mutex
      // 11e: astore 5
      // 120: aload 15
      // 122: getfield com/htmake/reader/api/controller/BookController$editShelfBook$1.L$3 Ljava/lang/Object;
      // 125: checkcast kotlin/jvm/functions/Function1
      // 128: astore 3
      // 129: aload 15
      // 12b: getfield com/htmake/reader/api/controller/BookController$editShelfBook$1.L$2 Ljava/lang/Object;
      // 12e: checkcast java/lang/String
      // 131: astore 2
      // 132: aload 15
      // 134: getfield com/htmake/reader/api/controller/BookController$editShelfBook$1.L$1 Ljava/lang/Object;
      // 137: checkcast io/legado/app/data/entities/Book
      // 13a: astore 1
      // 13b: aload 15
      // 13d: getfield com/htmake/reader/api/controller/BookController$editShelfBook$1.L$0 Ljava/lang/Object;
      // 140: checkcast com/htmake/reader/api/controller/BookController
      // 143: astore 0
      // 144: nop
      // 145: aload 14
      // 147: invokestatic kotlin/ResultKt.throwOnFailure (Ljava/lang/Object;)V
      // 14a: aload 14
      // 14c: pop
      // 14d: invokestatic com/htmake/reader/api/controller/BookControllerKt.access$getLogger$p ()Lmu/KLogger;
      // 150: ldc_w "lock success"
      // 153: invokeinterface mu/KLogger.info (Ljava/lang/String;)V 2
      // 158: aload 0
      // 159: aload 2
      // 15a: bipush 1
      // 15b: anewarray 96
      // 15e: astore 7
      // 160: aload 7
      // 162: bipush 0
      // 163: ldc_w "bookshelf"
      // 166: aastore
      // 167: aload 7
      // 169: invokevirtual com/htmake/reader/api/controller/BookController.getUserStorage (Ljava/lang/Object;[Ljava/lang/String;)Ljava/lang/String;
      // 16c: invokestatic com/htmake/reader/utils/ExtKt.asJsonArray (Ljava/lang/Object;)Lio/vertx/core/json/JsonArray;
      // 16f: astore 6
      // 171: aload 6
      // 173: ifnonnull 17f
      // 176: new io/vertx/core/json/JsonArray
      // 179: dup
      // 17a: invokespecial io/vertx/core/json/JsonArray.<init> ()V
      // 17d: astore 6
      // 17f: bipush -1
      // 180: istore 7
      // 182: bipush 0
      // 183: istore 8
      // 185: aload 6
      // 187: invokevirtual io/vertx/core/json/JsonArray.size ()I
      // 18a: istore 9
      // 18c: iload 8
      // 18e: iload 9
      // 190: if_icmpge 248
      // 193: iload 8
      // 195: istore 10
      // 197: iinc 8 1
      // 19a: aload 6
      // 19c: iload 10
      // 19e: invokevirtual io/vertx/core/json/JsonArray.getJsonObject (I)Lio/vertx/core/json/JsonObject;
      // 1a1: ldc_w io/legado/app/data/entities/Book
      // 1a4: invokevirtual io/vertx/core/json/JsonObject.mapTo (Ljava/lang/Class;)Ljava/lang/Object;
      // 1a7: checkcast io/legado/app/data/entities/Book
      // 1aa: astore 11
      // 1ac: aload 1
      // 1ad: invokevirtual io/legado/app/data/entities/Book.getBookUrl ()Ljava/lang/String;
      // 1b0: checkcast java/lang/CharSequence
      // 1b3: astore 12
      // 1b5: bipush 0
      // 1b6: istore 13
      // 1b8: aload 12
      // 1ba: invokeinterface java/lang/CharSequence.length ()I 1
      // 1bf: ifle 1c6
      // 1c2: bipush 1
      // 1c3: goto 1c7
      // 1c6: bipush 0
      // 1c7: ifeq 1e0
      // 1ca: aload 11
      // 1cc: invokevirtual io/legado/app/data/entities/Book.getBookUrl ()Ljava/lang/String;
      // 1cf: aload 1
      // 1d0: invokevirtual io/legado/app/data/entities/Book.getBookUrl ()Ljava/lang/String;
      // 1d3: invokevirtual java/lang/String.equals (Ljava/lang/Object;)Z
      // 1d6: ifeq 1e0
      // 1d9: iload 10
      // 1db: istore 7
      // 1dd: goto 248
      // 1e0: aload 1
      // 1e1: invokevirtual io/legado/app/data/entities/Book.getName ()Ljava/lang/String;
      // 1e4: checkcast java/lang/CharSequence
      // 1e7: astore 12
      // 1e9: bipush 0
      // 1ea: istore 13
      // 1ec: aload 12
      // 1ee: invokeinterface java/lang/CharSequence.length ()I 1
      // 1f3: ifle 1fa
      // 1f6: bipush 1
      // 1f7: goto 1fb
      // 1fa: bipush 0
      // 1fb: ifeq 241
      // 1fe: aload 11
      // 200: invokevirtual io/legado/app/data/entities/Book.getName ()Ljava/lang/String;
      // 203: aload 1
      // 204: invokevirtual io/legado/app/data/entities/Book.getName ()Ljava/lang/String;
      // 207: invokevirtual java/lang/String.equals (Ljava/lang/Object;)Z
      // 20a: ifeq 241
      // 20d: aload 1
      // 20e: invokevirtual io/legado/app/data/entities/Book.getAuthor ()Ljava/lang/String;
      // 211: checkcast java/lang/CharSequence
      // 214: astore 12
      // 216: bipush 0
      // 217: istore 13
      // 219: aload 12
      // 21b: invokeinterface java/lang/CharSequence.length ()I 1
      // 220: ifle 227
      // 223: bipush 1
      // 224: goto 228
      // 227: bipush 0
      // 228: ifeq 241
      // 22b: aload 11
      // 22d: invokevirtual io/legado/app/data/entities/Book.getAuthor ()Ljava/lang/String;
      // 230: aload 1
      // 231: invokevirtual io/legado/app/data/entities/Book.getAuthor ()Ljava/lang/String;
      // 234: invokevirtual java/lang/String.equals (Ljava/lang/Object;)Z
      // 237: ifeq 241
      // 23a: iload 10
      // 23c: istore 7
      // 23e: goto 248
      // 241: iload 8
      // 243: iload 9
      // 245: if_icmplt 193
      // 248: iload 7
      // 24a: iflt 2b2
      // 24d: aload 6
      // 24f: invokevirtual io/vertx/core/json/JsonArray.getList ()Ljava/util/List;
      // 252: astore 8
      // 254: aload 6
      // 256: iload 7
      // 258: invokevirtual io/vertx/core/json/JsonArray.getJsonObject (I)Lio/vertx/core/json/JsonObject;
      // 25b: ldc_w io/legado/app/data/entities/Book
      // 25e: invokevirtual io/vertx/core/json/JsonObject.mapTo (Ljava/lang/Class;)Ljava/lang/Object;
      // 261: checkcast io/legado/app/data/entities/Book
      // 264: astore 9
      // 266: aload 3
      // 267: aload 9
      // 269: astore 10
      // 26b: aload 10
      // 26d: ldc_w "existBook"
      // 270: invokestatic kotlin/jvm/internal/Intrinsics.checkNotNullExpressionValue (Ljava/lang/Object;Ljava/lang/String;)V
      // 273: aload 10
      // 275: invokeinterface kotlin/jvm/functions/Function1.invoke (Ljava/lang/Object;)Ljava/lang/Object; 2
      // 27a: checkcast io/legado/app/data/entities/Book
      // 27d: astore 9
      // 27f: aload 8
      // 281: iload 7
      // 283: aload 9
      // 285: invokestatic io/vertx/core/json/JsonObject.mapFrom (Ljava/lang/Object;)Lio/vertx/core/json/JsonObject;
      // 288: invokeinterface java/util/List.set (ILjava/lang/Object;)Ljava/lang/Object; 3
      // 28d: pop
      // 28e: new io/vertx/core/json/JsonArray
      // 291: dup
      // 292: aload 8
      // 294: invokespecial io/vertx/core/json/JsonArray.<init> (Ljava/util/List;)V
      // 297: astore 6
      // 299: aload 0
      // 29a: aload 2
      // 29b: ldc_w "bookshelf"
      // 29e: aload 6
      // 2a0: invokevirtual com/htmake/reader/api/controller/BookController.saveUserStorage (Ljava/lang/Object;Ljava/lang/String;Ljava/lang/Object;)V
      // 2a3: aload 9
      // 2a5: astore 10
      // 2a7: aload 5
      // 2a9: aconst_null
      // 2aa: bipush 1
      // 2ab: aconst_null
      // 2ac: invokestatic kotlinx/coroutines/sync/Mutex$DefaultImpls.unlock$default (Lkotlinx/coroutines/sync/Mutex;Ljava/lang/Object;ILjava/lang/Object;)V
      // 2af: aload 10
      // 2b1: areturn
      // 2b2: aload 5
      // 2b4: aconst_null
      // 2b5: bipush 1
      // 2b6: aconst_null
      // 2b7: invokestatic kotlinx/coroutines/sync/Mutex$DefaultImpls.unlock$default (Lkotlinx/coroutines/sync/Mutex;Ljava/lang/Object;ILjava/lang/Object;)V
      // 2ba: goto 2ca
      // 2bd: astore 6
      // 2bf: aload 5
      // 2c1: aconst_null
      // 2c2: bipush 1
      // 2c3: aconst_null
      // 2c4: invokestatic kotlinx/coroutines/sync/Mutex$DefaultImpls.unlock$default (Lkotlinx/coroutines/sync/Mutex;Ljava/lang/Object;ILjava/lang/Object;)V
      // 2c7: aload 6
      // 2c9: athrow
      // 2ca: aconst_null
      // 2cb: areturn
      // 2cc: new java/lang/IllegalStateException
      // 2cf: dup
      // 2d0: ldc_w "call to 'resume' before 'invoke' with coroutine"
      // 2d3: invokespecial java/lang/IllegalStateException.<init> (Ljava/lang/String;)V
      // 2d6: athrow
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

   public suspend fun syncFromWebdav(zipFilePath: String, userNameSpace: String): Boolean {
      // $VF: Couldn't be decompiled
      // Please report this to the Vineflower issue tracker, at https://github.com/Vineflower/vineflower/issues with a copy of the class file (if you have the rights to distribute it!)
      // java.lang.NullPointerException: Cannot invoke "org.jetbrains.java.decompiler.code.cfg.ExceptionRangeCFG.isCircular()" because "range" is null
      //   at org.jetbrains.java.decompiler.modules.decompiler.decompose.DomHelper.graphToStatement(DomHelper.java:84)
      //   at org.jetbrains.java.decompiler.modules.decompiler.decompose.DomHelper.parseGraph(DomHelper.java:203)
      //   at org.jetbrains.java.decompiler.modules.decompiler.decompose.DomHelper.createStatement(DomHelper.java:27)
      //   at org.jetbrains.java.decompiler.main.rels.MethodProcessor.codeToJava(MethodProcessor.java:157)
      //
      // Bytecode:
      // 000: aload 3
      // 001: instanceof com/htmake/reader/api/controller/BookController$syncFromWebdav$1
      // 004: ifeq 027
      // 007: aload 3
      // 008: checkcast com/htmake/reader/api/controller/BookController$syncFromWebdav$1
      // 00b: astore 20
      // 00d: aload 20
      // 00f: getfield com/htmake/reader/api/controller/BookController$syncFromWebdav$1.label I
      // 012: ldc -2147483648
      // 014: iand
      // 015: ifeq 027
      // 018: aload 20
      // 01a: dup
      // 01b: getfield com/htmake/reader/api/controller/BookController$syncFromWebdav$1.label I
      // 01e: ldc -2147483648
      // 020: isub
      // 021: putfield com/htmake/reader/api/controller/BookController$syncFromWebdav$1.label I
      // 024: goto 032
      // 027: new com/htmake/reader/api/controller/BookController$syncFromWebdav$1
      // 02a: dup
      // 02b: aload 0
      // 02c: aload 3
      // 02d: invokespecial com/htmake/reader/api/controller/BookController$syncFromWebdav$1.<init> (Lcom/htmake/reader/api/controller/BookController;Lkotlin/coroutines/Continuation;)V
      // 030: astore 20
      // 032: aload 20
      // 034: getfield com/htmake/reader/api/controller/BookController$syncFromWebdav$1.result Ljava/lang/Object;
      // 037: astore 19
      // 039: invokestatic kotlin/coroutines/intrinsics/IntrinsicsKt.getCOROUTINE_SUSPENDED ()Ljava/lang/Object;
      // 03c: astore 21
      // 03e: aload 20
      // 040: getfield com/htmake/reader/api/controller/BookController$syncFromWebdav$1.label I
      // 043: tableswitch 784 0 1 21 666
      // 058: aload 19
      // 05a: invokestatic kotlin/ResultKt.throwOnFailure (Ljava/lang/Object;)V
      // 05d: aconst_null
      // 05e: astore 4
      // 060: bipush 4
      // 061: anewarray 96
      // 064: astore 5
      // 066: aload 5
      // 068: bipush 0
      // 069: ldc "storage"
      // 06b: aastore
      // 06c: aload 5
      // 06e: bipush 1
      // 06f: ldc_w "data"
      // 072: aastore
      // 073: aload 5
      // 075: bipush 2
      // 076: aload 2
      // 077: aastore
      // 078: aload 5
      // 07a: bipush 3
      // 07b: ldc_w "tmp"
      // 07e: aastore
      // 07f: aload 5
      // 081: invokestatic com/htmake/reader/utils/ExtKt.getWorkDir ([Ljava/lang/String;)Ljava/lang/String;
      // 084: astore 4
      // 086: new java/io/File
      // 089: dup
      // 08a: aload 4
      // 08c: invokespecial java/io/File.<init> (Ljava/lang/String;)V
      // 08f: astore 5
      // 091: nop
      // 092: aload 0
      // 093: aload 2
      // 094: invokevirtual com/htmake/reader/api/controller/BookController.getUserWebdavHome (Ljava/lang/Object;)Ljava/lang/String;
      // 097: astore 6
      // 099: new java/io/File
      // 09c: dup
      // 09d: aload 1
      // 09e: invokespecial java/io/File.<init> (Ljava/lang/String;)V
      // 0a1: astore 7
      // 0a3: aload 7
      // 0a5: invokevirtual java/io/File.exists ()Z
      // 0a8: ifne 0b9
      // 0ab: bipush 0
      // 0ac: invokestatic kotlin/coroutines/jvm/internal/Boxing.boxBoolean (Z)Ljava/lang/Boolean;
      // 0af: astore 8
      // 0b1: aload 5
      // 0b3: invokestatic com/htmake/reader/utils/ExtKt.deleteRecursively (Ljava/io/File;)V
      // 0b6: aload 8
      // 0b8: areturn
      // 0b9: aload 5
      // 0bb: invokestatic com/htmake/reader/utils/ExtKt.deleteRecursively (Ljava/io/File;)V
      // 0be: getstatic io/legado/app/utils/ZipUtils.INSTANCE Lio/legado/app/utils/ZipUtils;
      // 0c1: aload 7
      // 0c3: aload 5
      // 0c5: invokevirtual io/legado/app/utils/ZipUtils.unzipFile (Ljava/io/File;Ljava/io/File;)Ljava/util/List;
      // 0c8: pop
      // 0c9: aload 0
      // 0ca: invokespecial com/htmake/reader/api/controller/BookController.getBackupFileNames ()[Ljava/lang/String;
      // 0cd: astore 9
      // 0cf: aload 9
      // 0d1: aload 9
      // 0d3: arraylength
      // 0d4: invokestatic java/util/Arrays.copyOf ([Ljava/lang/Object;I)[Ljava/lang/Object;
      // 0d7: invokestatic kotlin/collections/CollectionsKt.arrayListOf ([Ljava/lang/Object;)Ljava/util/ArrayList;
      // 0da: astore 8
      // 0dc: aload 8
      // 0de: checkcast java/lang/Iterable
      // 0e1: astore 9
      // 0e3: bipush 0
      // 0e4: istore 10
      // 0e6: aload 9
      // 0e8: invokeinterface java/lang/Iterable.iterator ()Ljava/util/Iterator; 1
      // 0ed: astore 11
      // 0ef: aload 11
      // 0f1: invokeinterface java/util/Iterator.hasNext ()Z 1
      // 0f6: ifeq 179
      // 0f9: aload 11
      // 0fb: invokeinterface java/util/Iterator.next ()Ljava/lang/Object; 1
      // 100: astore 12
      // 102: aload 12
      // 104: checkcast java/lang/String
      // 107: astore 13
      // 109: bipush 0
      // 10a: istore 14
      // 10c: new java/io/File
      // 10f: dup
      // 110: new java/lang/StringBuilder
      // 113: dup
      // 114: invokespecial java/lang/StringBuilder.<init> ()V
      // 117: aload 4
      // 119: invokevirtual java/lang/StringBuilder.append (Ljava/lang/String;)Ljava/lang/StringBuilder;
      // 11c: getstatic java/io/File.separator Ljava/lang/String;
      // 11f: invokevirtual java/lang/StringBuilder.append (Ljava/lang/Object;)Ljava/lang/StringBuilder;
      // 122: aload 13
      // 124: invokevirtual java/lang/StringBuilder.append (Ljava/lang/String;)Ljava/lang/StringBuilder;
      // 127: invokevirtual java/lang/StringBuilder.toString ()Ljava/lang/String;
      // 12a: invokespecial java/io/File.<init> (Ljava/lang/String;)V
      // 12d: astore 15
      // 12f: aload 15
      // 131: invokevirtual java/io/File.exists ()Z
      // 134: ifeq 175
      // 137: new java/io/File
      // 13a: dup
      // 13b: bipush 4
      // 13c: anewarray 96
      // 13f: astore 16
      // 141: aload 16
      // 143: bipush 0
      // 144: ldc "storage"
      // 146: aastore
      // 147: aload 16
      // 149: bipush 1
      // 14a: ldc_w "data"
      // 14d: aastore
      // 14e: aload 16
      // 150: bipush 2
      // 151: aload 2
      // 152: aastore
      // 153: aload 16
      // 155: bipush 3
      // 156: aload 13
      // 158: aastore
      // 159: aload 16
      // 15b: invokestatic com/htmake/reader/utils/ExtKt.getWorkDir ([Ljava/lang/String;)Ljava/lang/String;
      // 15e: invokespecial java/io/File.<init> (Ljava/lang/String;)V
      // 161: astore 17
      // 163: aload 17
      // 165: invokestatic com/htmake/reader/utils/ExtKt.deleteRecursively (Ljava/io/File;)V
      // 168: aload 15
      // 16a: aload 17
      // 16c: bipush 0
      // 16d: aconst_null
      // 16e: bipush 6
      // 170: aconst_null
      // 171: invokestatic kotlin/io/FilesKt.copyRecursively$default (Ljava/io/File;Ljava/io/File;ZLkotlin/jvm/functions/Function2;ILjava/lang/Object;)Z
      // 174: pop
      // 175: nop
      // 176: goto 0ef
      // 179: nop
      // 17a: new java/io/File
      // 17d: dup
      // 17e: new java/lang/StringBuilder
      // 181: dup
      // 182: invokespecial java/lang/StringBuilder.<init> ()V
      // 185: aload 4
      // 187: invokevirtual java/lang/StringBuilder.append (Ljava/lang/String;)Ljava/lang/StringBuilder;
      // 18a: getstatic java/io/File.separator Ljava/lang/String;
      // 18d: invokevirtual java/lang/StringBuilder.append (Ljava/lang/Object;)Ljava/lang/StringBuilder;
      // 190: ldc_w "books"
      // 193: invokevirtual java/lang/StringBuilder.append (Ljava/lang/String;)Ljava/lang/StringBuilder;
      // 196: invokevirtual java/lang/StringBuilder.toString ()Ljava/lang/String;
      // 199: invokespecial java/io/File.<init> (Ljava/lang/String;)V
      // 19c: astore 9
      // 19e: aload 9
      // 1a0: invokevirtual java/io/File.exists ()Z
      // 1a3: ifeq 1ec
      // 1a6: new java/io/File
      // 1a9: dup
      // 1aa: bipush 5
      // 1ab: anewarray 96
      // 1ae: astore 11
      // 1b0: aload 11
      // 1b2: bipush 0
      // 1b3: ldc "storage"
      // 1b5: aastore
      // 1b6: aload 11
      // 1b8: bipush 1
      // 1b9: ldc_w "data"
      // 1bc: aastore
      // 1bd: aload 11
      // 1bf: bipush 2
      // 1c0: aload 2
      // 1c1: aastore
      // 1c2: aload 11
      // 1c4: bipush 3
      // 1c5: ldc_w "webdav"
      // 1c8: aastore
      // 1c9: aload 11
      // 1cb: bipush 4
      // 1cc: ldc_w "books"
      // 1cf: aastore
      // 1d0: aload 11
      // 1d2: invokestatic com/htmake/reader/utils/ExtKt.getWorkDir ([Ljava/lang/String;)Ljava/lang/String;
      // 1d5: invokespecial java/io/File.<init> (Ljava/lang/String;)V
      // 1d8: astore 10
      // 1da: aload 10
      // 1dc: invokestatic com/htmake/reader/utils/ExtKt.deleteRecursively (Ljava/io/File;)V
      // 1df: aload 9
      // 1e1: aload 10
      // 1e3: bipush 0
      // 1e4: aconst_null
      // 1e5: bipush 6
      // 1e7: aconst_null
      // 1e8: invokestatic kotlin/io/FilesKt.copyRecursively$default (Ljava/io/File;Ljava/io/File;ZLkotlin/jvm/functions/Function2;ILjava/lang/Object;)Z
      // 1eb: pop
      // 1ec: new java/io/File
      // 1ef: dup
      // 1f0: new java/lang/StringBuilder
      // 1f3: dup
      // 1f4: invokespecial java/lang/StringBuilder.<init> ()V
      // 1f7: aload 6
      // 1f9: invokevirtual java/lang/StringBuilder.append (Ljava/lang/String;)Ljava/lang/StringBuilder;
      // 1fc: getstatic java/io/File.separator Ljava/lang/String;
      // 1ff: invokevirtual java/lang/StringBuilder.append (Ljava/lang/Object;)Ljava/lang/StringBuilder;
      // 202: ldc_w "bookProgress"
      // 205: invokevirtual java/lang/StringBuilder.append (Ljava/lang/String;)Ljava/lang/StringBuilder;
      // 208: invokevirtual java/lang/StringBuilder.toString ()Ljava/lang/String;
      // 20b: invokespecial java/io/File.<init> (Ljava/lang/String;)V
      // 20e: astore 10
      // 210: aload 10
      // 212: invokevirtual java/io/File.exists ()Z
      // 215: ifne 248
      // 218: new java/io/File
      // 21b: dup
      // 21c: new java/lang/StringBuilder
      // 21f: dup
      // 220: invokespecial java/lang/StringBuilder.<init> ()V
      // 223: aload 6
      // 225: invokevirtual java/lang/StringBuilder.append (Ljava/lang/String;)Ljava/lang/StringBuilder;
      // 228: getstatic java/io/File.separator Ljava/lang/String;
      // 22b: invokevirtual java/lang/StringBuilder.append (Ljava/lang/Object;)Ljava/lang/StringBuilder;
      // 22e: ldc_w "legado"
      // 231: invokevirtual java/lang/StringBuilder.append (Ljava/lang/String;)Ljava/lang/StringBuilder;
      // 234: getstatic java/io/File.separator Ljava/lang/String;
      // 237: invokevirtual java/lang/StringBuilder.append (Ljava/lang/Object;)Ljava/lang/StringBuilder;
      // 23a: ldc_w "bookProgress"
      // 23d: invokevirtual java/lang/StringBuilder.append (Ljava/lang/String;)Ljava/lang/StringBuilder;
      // 240: invokevirtual java/lang/StringBuilder.toString ()Ljava/lang/String;
      // 243: invokespecial java/io/File.<init> (Ljava/lang/String;)V
      // 246: astore 10
      // 248: aload 10
      // 24a: invokevirtual java/io/File.exists ()Z
      // 24d: ifeq 327
      // 250: aload 10
      // 252: invokevirtual java/io/File.isDirectory ()Z
      // 255: ifeq 327
      // 258: aload 10
      // 25a: invokevirtual java/io/File.listFiles ()[Ljava/io/File;
      // 25d: astore 11
      // 25f: aload 11
      // 261: ldc_w "bookProgressDir.listFiles()"
      // 264: invokestatic kotlin/jvm/internal/Intrinsics.checkNotNullExpressionValue (Ljava/lang/Object;Ljava/lang/String;)V
      // 267: aload 11
      // 269: checkcast [Ljava/lang/Object;
      // 26c: astore 11
      // 26e: bipush 0
      // 26f: istore 12
      // 271: aload 11
      // 273: astore 13
      // 275: aload 13
      // 277: arraylength
      // 278: istore 14
      // 27a: bipush 0
      // 27b: istore 15
      // 27d: iload 15
      // 27f: iload 14
      // 281: if_icmpge 326
      // 284: aload 13
      // 286: iload 15
      // 288: aaload
      // 289: astore 16
      // 28b: aload 16
      // 28d: checkcast java/io/File
      // 290: astore 17
      // 292: bipush 0
      // 293: istore 18
      // 295: aload 0
      // 296: aload 17
      // 298: ldc_w "it"
      // 29b: invokestatic kotlin/jvm/internal/Intrinsics.checkNotNullExpressionValue (Ljava/lang/Object;Ljava/lang/String;)V
      // 29e: aload 17
      // 2a0: aload 2
      // 2a1: aload 20
      // 2a3: aload 20
      // 2a5: aload 0
      // 2a6: putfield com/htmake/reader/api/controller/BookController$syncFromWebdav$1.L$0 Ljava/lang/Object;
      // 2a9: aload 20
      // 2ab: aload 2
      // 2ac: putfield com/htmake/reader/api/controller/BookController$syncFromWebdav$1.L$1 Ljava/lang/Object;
      // 2af: aload 20
      // 2b1: aload 5
      // 2b3: putfield com/htmake/reader/api/controller/BookController$syncFromWebdav$1.L$2 Ljava/lang/Object;
      // 2b6: aload 20
      // 2b8: aload 13
      // 2ba: putfield com/htmake/reader/api/controller/BookController$syncFromWebdav$1.L$3 Ljava/lang/Object;
      // 2bd: aload 20
      // 2bf: iload 14
      // 2c1: putfield com/htmake/reader/api/controller/BookController$syncFromWebdav$1.I$0 I
      // 2c4: aload 20
      // 2c6: iload 15
      // 2c8: putfield com/htmake/reader/api/controller/BookController$syncFromWebdav$1.I$1 I
      // 2cb: aload 20
      // 2cd: bipush 1
      // 2ce: putfield com/htmake/reader/api/controller/BookController$syncFromWebdav$1.label I
      // 2d1: invokevirtual com/htmake/reader/api/controller/BookController.syncBookProgressFromWebdav (Ljava/lang/Object;Ljava/lang/String;Lkotlin/coroutines/Continuation;)Ljava/lang/Object;
      // 2d4: dup
      // 2d5: aload 21
      // 2d7: if_acmpne 31f
      // 2da: aload 21
      // 2dc: areturn
      // 2dd: bipush 0
      // 2de: istore 12
      // 2e0: bipush 0
      // 2e1: istore 18
      // 2e3: aload 20
      // 2e5: getfield com/htmake/reader/api/controller/BookController$syncFromWebdav$1.I$1 I
      // 2e8: istore 15
      // 2ea: aload 20
      // 2ec: getfield com/htmake/reader/api/controller/BookController$syncFromWebdav$1.I$0 I
      // 2ef: istore 14
      // 2f1: aload 20
      // 2f3: getfield com/htmake/reader/api/controller/BookController$syncFromWebdav$1.L$3 Ljava/lang/Object;
      // 2f6: checkcast [Ljava/lang/Object;
      // 2f9: astore 13
      // 2fb: aload 20
      // 2fd: getfield com/htmake/reader/api/controller/BookController$syncFromWebdav$1.L$2 Ljava/lang/Object;
      // 300: checkcast java/io/File
      // 303: astore 5
      // 305: aload 20
      // 307: getfield com/htmake/reader/api/controller/BookController$syncFromWebdav$1.L$1 Ljava/lang/Object;
      // 30a: checkcast java/lang/String
      // 30d: astore 2
      // 30e: aload 20
      // 310: getfield com/htmake/reader/api/controller/BookController$syncFromWebdav$1.L$0 Ljava/lang/Object;
      // 313: checkcast com/htmake/reader/api/controller/BookController
      // 316: astore 0
      // 317: nop
      // 318: aload 19
      // 31a: invokestatic kotlin/ResultKt.throwOnFailure (Ljava/lang/Object;)V
      // 31d: aload 19
      // 31f: pop
      // 320: iinc 15 1
      // 323: goto 27d
      // 326: nop
      // 327: bipush 1
      // 328: invokestatic kotlin/coroutines/jvm/internal/Boxing.boxBoolean (Z)Ljava/lang/Boolean;
      // 32b: astore 11
      // 32d: aload 5
      // 32f: invokestatic com/htmake/reader/utils/ExtKt.deleteRecursively (Ljava/io/File;)V
      // 332: aload 11
      // 334: areturn
      // 335: astore 6
      // 337: aload 6
      // 339: invokevirtual java/lang/Exception.printStackTrace ()V
      // 33c: aload 5
      // 33e: invokestatic com/htmake/reader/utils/ExtKt.deleteRecursively (Ljava/io/File;)V
      // 341: goto 34e
      // 344: astore 6
      // 346: aload 5
      // 348: invokestatic com/htmake/reader/utils/ExtKt.deleteRecursively (Ljava/io/File;)V
      // 34b: aload 6
      // 34d: athrow
      // 34e: bipush 0
      // 34f: invokestatic kotlin/coroutines/jvm/internal/Boxing.boxBoolean (Z)Ljava/lang/Boolean;
      // 352: areturn
      // 353: new java/lang/IllegalStateException
      // 356: dup
      // 357: ldc_w "call to 'resume' before 'invoke' with coroutine"
      // 35a: invokespecial java/lang/IllegalStateException.<init> (Ljava/lang/String;)V
      // 35d: athrow
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

   public suspend fun cacheBookOnServer(bookUrlList: JsonArray, userNameSpace: String) {
      // $VF: Couldn't be decompiled
      // Please report this to the Vineflower issue tracker, at https://github.com/Vineflower/vineflower/issues with a copy of the class file (if you have the rights to distribute it!)
      // java.lang.RuntimeException: parsing failure!
      //   at org.jetbrains.java.decompiler.modules.decompiler.decompose.DomHelper.parseGraph(DomHelper.java:211)
      //   at org.jetbrains.java.decompiler.modules.decompiler.decompose.DomHelper.createStatement(DomHelper.java:27)
      //   at org.jetbrains.java.decompiler.main.rels.MethodProcessor.codeToJava(MethodProcessor.java:157)
      //
      // Bytecode:
      // 000: aload 3
      // 001: instanceof com/htmake/reader/api/controller/BookController$cacheBookOnServer$3
      // 004: ifeq 027
      // 007: aload 3
      // 008: checkcast com/htmake/reader/api/controller/BookController$cacheBookOnServer$3
      // 00b: astore 25
      // 00d: aload 25
      // 00f: getfield com/htmake/reader/api/controller/BookController$cacheBookOnServer$3.label I
      // 012: ldc -2147483648
      // 014: iand
      // 015: ifeq 027
      // 018: aload 25
      // 01a: dup
      // 01b: getfield com/htmake/reader/api/controller/BookController$cacheBookOnServer$3.label I
      // 01e: ldc -2147483648
      // 020: isub
      // 021: putfield com/htmake/reader/api/controller/BookController$cacheBookOnServer$3.label I
      // 024: goto 032
      // 027: new com/htmake/reader/api/controller/BookController$cacheBookOnServer$3
      // 02a: dup
      // 02b: aload 0
      // 02c: aload 3
      // 02d: invokespecial com/htmake/reader/api/controller/BookController$cacheBookOnServer$3.<init> (Lcom/htmake/reader/api/controller/BookController;Lkotlin/coroutines/Continuation;)V
      // 030: astore 25
      // 032: aload 25
      // 034: getfield com/htmake/reader/api/controller/BookController$cacheBookOnServer$3.result Ljava/lang/Object;
      // 037: astore 24
      // 039: invokestatic kotlin/coroutines/intrinsics/IntrinsicsKt.getCOROUTINE_SUSPENDED ()Ljava/lang/Object;
      // 03c: astore 26
      // 03e: aload 25
      // 040: getfield com/htmake/reader/api/controller/BookController$cacheBookOnServer$3.label I
      // 043: tableswitch 1221 0 3 29 306 640 1035
      // 060: aload 24
      // 062: invokestatic kotlin/ResultKt.throwOnFailure (Ljava/lang/Object;)V
      // 065: bipush 0
      // 066: istore 4
      // 068: aload 1
      // 069: invokevirtual io/vertx/core/json/JsonArray.size ()I
      // 06c: istore 5
      // 06e: iload 4
      // 070: iload 5
      // 072: if_icmpge 504
      // 075: iload 4
      // 077: istore 6
      // 079: iinc 4 1
      // 07c: aload 1
      // 07d: iload 6
      // 07f: invokevirtual io/vertx/core/json/JsonArray.getString (I)Ljava/lang/String;
      // 082: astore 7
      // 084: aload 0
      // 085: aload 7
      // 087: ldc_w "bookUrl"
      // 08a: invokestatic kotlin/jvm/internal/Intrinsics.checkNotNullExpressionValue (Ljava/lang/Object;Ljava/lang/String;)V
      // 08d: aload 7
      // 08f: aload 2
      // 090: invokevirtual com/htmake/reader/api/controller/BookController.getShelfBookByURL (Ljava/lang/String;Ljava/lang/String;)Lio/legado/app/data/entities/Book;
      // 093: astore 8
      // 095: aload 8
      // 097: ifnonnull 0aa
      // 09a: invokestatic com/htmake/reader/api/controller/BookControllerKt.access$getLogger$p ()Lmu/KLogger;
      // 09d: ldc_w "未找到书籍信息: {}"
      // 0a0: aload 7
      // 0a2: invokeinterface mu/KLogger.info (Ljava/lang/String;Ljava/lang/Object;)V 3
      // 0a7: goto 4fd
      // 0aa: aload 8
      // 0ac: invokevirtual io/legado/app/data/entities/Book.isLocalBook ()Z
      // 0af: ifeq 0c2
      // 0b2: invokestatic com/htmake/reader/api/controller/BookControllerKt.access$getLogger$p ()Lmu/KLogger;
      // 0b5: ldc_w "本地书籍跳过缓存: {}"
      // 0b8: aload 7
      // 0ba: invokeinterface mu/KLogger.info (Ljava/lang/String;Ljava/lang/Object;)V 3
      // 0bf: goto 4fd
      // 0c2: invokestatic com/htmake/reader/api/controller/BookControllerKt.access$getLogger$p ()Lmu/KLogger;
      // 0c5: ldc_w "开始缓存书籍: {}"
      // 0c8: aload 8
      // 0ca: invokeinterface mu/KLogger.info (Ljava/lang/String;Ljava/lang/Object;)V 3
      // 0cf: aload 0
      // 0d0: aload 8
      // 0d2: invokevirtual io/legado/app/data/entities/Book.getOrigin ()Ljava/lang/String;
      // 0d5: aload 2
      // 0d6: invokevirtual com/htmake/reader/api/controller/BookController.getBookSourceStringBySourceURLOpt (Ljava/lang/String;Ljava/lang/String;)Ljava/lang/String;
      // 0d9: astore 9
      // 0db: aload 9
      // 0dd: checkcast java/lang/CharSequence
      // 0e0: astore 10
      // 0e2: bipush 0
      // 0e3: istore 11
      // 0e5: bipush 0
      // 0e6: istore 12
      // 0e8: aload 10
      // 0ea: ifnull 0f7
      // 0ed: aload 10
      // 0ef: invokeinterface java/lang/CharSequence.length ()I 1
      // 0f4: ifne 0fb
      // 0f7: bipush 1
      // 0f8: goto 0fc
      // 0fb: bipush 0
      // 0fc: ifeq 10f
      // 0ff: invokestatic com/htmake/reader/api/controller/BookControllerKt.access$getLogger$p ()Lmu/KLogger;
      // 102: ldc_w "未找到书源信息: {}"
      // 105: aload 7
      // 107: invokeinterface mu/KLogger.info (Ljava/lang/String;Ljava/lang/Object;)V 3
      // 10c: goto 4fd
      // 10f: aload 0
      // 110: aload 8
      // 112: aload 9
      // 114: bipush 0
      // 115: aload 2
      // 116: bipush 0
      // 117: aconst_null
      // 118: aload 25
      // 11a: bipush 48
      // 11c: aconst_null
      // 11d: aload 25
      // 11f: aload 0
      // 120: putfield com/htmake/reader/api/controller/BookController$cacheBookOnServer$3.L$0 Ljava/lang/Object;
      // 123: aload 25
      // 125: aload 1
      // 126: putfield com/htmake/reader/api/controller/BookController$cacheBookOnServer$3.L$1 Ljava/lang/Object;
      // 129: aload 25
      // 12b: aload 2
      // 12c: putfield com/htmake/reader/api/controller/BookController$cacheBookOnServer$3.L$2 Ljava/lang/Object;
      // 12f: aload 25
      // 131: aload 8
      // 133: putfield com/htmake/reader/api/controller/BookController$cacheBookOnServer$3.L$3 Ljava/lang/Object;
      // 136: aload 25
      // 138: aload 9
      // 13a: putfield com/htmake/reader/api/controller/BookController$cacheBookOnServer$3.L$4 Ljava/lang/Object;
      // 13d: aload 25
      // 13f: aconst_null
      // 140: putfield com/htmake/reader/api/controller/BookController$cacheBookOnServer$3.L$5 Ljava/lang/Object;
      // 143: aload 25
      // 145: aconst_null
      // 146: putfield com/htmake/reader/api/controller/BookController$cacheBookOnServer$3.L$6 Ljava/lang/Object;
      // 149: aload 25
      // 14b: aconst_null
      // 14c: putfield com/htmake/reader/api/controller/BookController$cacheBookOnServer$3.L$7 Ljava/lang/Object;
      // 14f: aload 25
      // 151: aconst_null
      // 152: putfield com/htmake/reader/api/controller/BookController$cacheBookOnServer$3.L$8 Ljava/lang/Object;
      // 155: aload 25
      // 157: iload 4
      // 159: putfield com/htmake/reader/api/controller/BookController$cacheBookOnServer$3.I$0 I
      // 15c: aload 25
      // 15e: iload 5
      // 160: putfield com/htmake/reader/api/controller/BookController$cacheBookOnServer$3.I$1 I
      // 163: aload 25
      // 165: bipush 1
      // 166: putfield com/htmake/reader/api/controller/BookController$cacheBookOnServer$3.label I
      // 169: invokestatic com/htmake/reader/api/controller/BookController.getLocalChapterList$default (Lcom/htmake/reader/api/controller/BookController;Lio/legado/app/data/entities/Book;Ljava/lang/String;ZLjava/lang/String;ZLkotlinx/coroutines/sync/Mutex;Lkotlin/coroutines/Continuation;ILjava/lang/Object;)Ljava/lang/Object;
      // 16c: dup
      // 16d: aload 26
      // 16f: if_acmpne 1b9
      // 172: aload 26
      // 174: areturn
      // 175: aload 25
      // 177: getfield com/htmake/reader/api/controller/BookController$cacheBookOnServer$3.I$1 I
      // 17a: istore 5
      // 17c: aload 25
      // 17e: getfield com/htmake/reader/api/controller/BookController$cacheBookOnServer$3.I$0 I
      // 181: istore 4
      // 183: aload 25
      // 185: getfield com/htmake/reader/api/controller/BookController$cacheBookOnServer$3.L$4 Ljava/lang/Object;
      // 188: checkcast java/lang/String
      // 18b: astore 9
      // 18d: aload 25
      // 18f: getfield com/htmake/reader/api/controller/BookController$cacheBookOnServer$3.L$3 Ljava/lang/Object;
      // 192: checkcast io/legado/app/data/entities/Book
      // 195: astore 8
      // 197: aload 25
      // 199: getfield com/htmake/reader/api/controller/BookController$cacheBookOnServer$3.L$2 Ljava/lang/Object;
      // 19c: checkcast java/lang/String
      // 19f: astore 2
      // 1a0: aload 25
      // 1a2: getfield com/htmake/reader/api/controller/BookController$cacheBookOnServer$3.L$1 Ljava/lang/Object;
      // 1a5: checkcast io/vertx/core/json/JsonArray
      // 1a8: astore 1
      // 1a9: aload 25
      // 1ab: getfield com/htmake/reader/api/controller/BookController$cacheBookOnServer$3.L$0 Ljava/lang/Object;
      // 1ae: checkcast com/htmake/reader/api/controller/BookController
      // 1b1: astore 0
      // 1b2: aload 24
      // 1b4: invokestatic kotlin/ResultKt.throwOnFailure (Ljava/lang/Object;)V
      // 1b7: aload 24
      // 1b9: checkcast java/util/List
      // 1bc: astore 10
      // 1be: aload 0
      // 1bf: aload 8
      // 1c1: aload 2
      // 1c2: invokevirtual com/htmake/reader/api/controller/BookController.getCachedChapterContentSet (Lio/legado/app/data/entities/Book;Ljava/lang/String;)Ljava/util/Set;
      // 1c5: astore 11
      // 1c7: aload 0
      // 1c8: aload 8
      // 1ca: aload 2
      // 1cb: invokevirtual com/htmake/reader/api/controller/BookController.getChapterCacheDir (Lio/legado/app/data/entities/Book;Ljava/lang/String;)Ljava/io/File;
      // 1ce: astore 12
      // 1d0: bipush 0
      // 1d1: istore 13
      // 1d3: aload 10
      // 1d5: invokeinterface java/util/List.size ()I 1
      // 1da: bipush -1
      // 1db: iadd
      // 1dc: istore 14
      // 1de: iload 13
      // 1e0: iload 14
      // 1e2: if_icmpgt 4f0
      // 1e5: iload 13
      // 1e7: istore 15
      // 1e9: iinc 13 1
      // 1ec: aload 11
      // 1ee: iload 15
      // 1f0: invokestatic kotlin/coroutines/jvm/internal/Boxing.boxInt (I)Ljava/lang/Integer;
      // 1f3: invokeinterface java/util/Set.contains (Ljava/lang/Object;)Z 2
      // 1f8: ifne 4e9
      // 1fb: iload 15
      // 1fd: istore 16
      // 1ff: aload 10
      // 201: iload 15
      // 203: invokeinterface java/util/List.get (I)Ljava/lang/Object; 2
      // 208: checkcast io/legado/app/data/entities/BookChapter
      // 20b: astore 17
      // 20d: nop
      // 20e: aconst_null
      // 20f: astore 18
      // 211: iload 16
      // 213: bipush 1
      // 214: iadd
      // 215: aload 10
      // 217: invokeinterface java/util/List.size ()I 1
      // 21c: if_icmpge 236
      // 21f: aload 10
      // 221: iload 16
      // 223: bipush 1
      // 224: iadd
      // 225: invokeinterface java/util/List.get (I)Ljava/lang/Object; 2
      // 22a: checkcast io/legado/app/data/entities/BookChapter
      // 22d: astore 19
      // 22f: aload 19
      // 231: invokevirtual io/legado/app/data/entities/BookChapter.getUrl ()Ljava/lang/String;
      // 234: astore 18
      // 236: new io/legado/app/model/webBook/WebBook
      // 239: dup
      // 23a: aload 9
      // 23c: aload 0
      // 23d: invokevirtual com/htmake/reader/api/controller/BookController.getAppConfig ()Lcom/htmake/reader/config/AppConfig;
      // 240: invokevirtual com/htmake/reader/config/AppConfig.getDebugLog ()Z
      // 243: aconst_null
      // 244: aload 2
      // 245: bipush 4
      // 246: aconst_null
      // 247: invokespecial io/legado/app/model/webBook/WebBook.<init> (Ljava/lang/String;ZLio/legado/app/model/DebugLog;Ljava/lang/String;ILkotlin/jvm/internal/DefaultConstructorMarker;)V
      // 24a: aload 8
      // 24c: aload 17
      // 24e: aload 18
      // 250: aload 25
      // 252: aload 25
      // 254: aload 0
      // 255: putfield com/htmake/reader/api/controller/BookController$cacheBookOnServer$3.L$0 Ljava/lang/Object;
      // 258: aload 25
      // 25a: aload 1
      // 25b: putfield com/htmake/reader/api/controller/BookController$cacheBookOnServer$3.L$1 Ljava/lang/Object;
      // 25e: aload 25
      // 260: aload 2
      // 261: putfield com/htmake/reader/api/controller/BookController$cacheBookOnServer$3.L$2 Ljava/lang/Object;
      // 264: aload 25
      // 266: aload 8
      // 268: putfield com/htmake/reader/api/controller/BookController$cacheBookOnServer$3.L$3 Ljava/lang/Object;
      // 26b: aload 25
      // 26d: aload 9
      // 26f: putfield com/htmake/reader/api/controller/BookController$cacheBookOnServer$3.L$4 Ljava/lang/Object;
      // 272: aload 25
      // 274: aload 10
      // 276: putfield com/htmake/reader/api/controller/BookController$cacheBookOnServer$3.L$5 Ljava/lang/Object;
      // 279: aload 25
      // 27b: aload 11
      // 27d: putfield com/htmake/reader/api/controller/BookController$cacheBookOnServer$3.L$6 Ljava/lang/Object;
      // 280: aload 25
      // 282: aload 12
      // 284: putfield com/htmake/reader/api/controller/BookController$cacheBookOnServer$3.L$7 Ljava/lang/Object;
      // 287: aload 25
      // 289: aload 17
      // 28b: putfield com/htmake/reader/api/controller/BookController$cacheBookOnServer$3.L$8 Ljava/lang/Object;
      // 28e: aload 25
      // 290: iload 4
      // 292: putfield com/htmake/reader/api/controller/BookController$cacheBookOnServer$3.I$0 I
      // 295: aload 25
      // 297: iload 5
      // 299: putfield com/htmake/reader/api/controller/BookController$cacheBookOnServer$3.I$1 I
      // 29c: aload 25
      // 29e: iload 13
      // 2a0: putfield com/htmake/reader/api/controller/BookController$cacheBookOnServer$3.I$2 I
      // 2a3: aload 25
      // 2a5: iload 14
      // 2a7: putfield com/htmake/reader/api/controller/BookController$cacheBookOnServer$3.I$3 I
      // 2aa: aload 25
      // 2ac: iload 16
      // 2ae: putfield com/htmake/reader/api/controller/BookController$cacheBookOnServer$3.I$4 I
      // 2b1: aload 25
      // 2b3: bipush 2
      // 2b4: putfield com/htmake/reader/api/controller/BookController$cacheBookOnServer$3.label I
      // 2b7: invokevirtual io/legado/app/model/webBook/WebBook.getBookContent (Lio/legado/app/data/entities/Book;Lio/legado/app/data/entities/BookChapter;Ljava/lang/String;Lkotlin/coroutines/Continuation;)Ljava/lang/Object;
      // 2ba: dup
      // 2bb: aload 26
      // 2bd: if_acmpne 345
      // 2c0: aload 26
      // 2c2: areturn
      // 2c3: aload 25
      // 2c5: getfield com/htmake/reader/api/controller/BookController$cacheBookOnServer$3.I$4 I
      // 2c8: istore 16
      // 2ca: aload 25
      // 2cc: getfield com/htmake/reader/api/controller/BookController$cacheBookOnServer$3.I$3 I
      // 2cf: istore 14
      // 2d1: aload 25
      // 2d3: getfield com/htmake/reader/api/controller/BookController$cacheBookOnServer$3.I$2 I
      // 2d6: istore 13
      // 2d8: aload 25
      // 2da: getfield com/htmake/reader/api/controller/BookController$cacheBookOnServer$3.I$1 I
      // 2dd: istore 5
      // 2df: aload 25
      // 2e1: getfield com/htmake/reader/api/controller/BookController$cacheBookOnServer$3.I$0 I
      // 2e4: istore 4
      // 2e6: aload 25
      // 2e8: getfield com/htmake/reader/api/controller/BookController$cacheBookOnServer$3.L$8 Ljava/lang/Object;
      // 2eb: checkcast io/legado/app/data/entities/BookChapter
      // 2ee: astore 17
      // 2f0: aload 25
      // 2f2: getfield com/htmake/reader/api/controller/BookController$cacheBookOnServer$3.L$7 Ljava/lang/Object;
      // 2f5: checkcast java/io/File
      // 2f8: astore 12
      // 2fa: aload 25
      // 2fc: getfield com/htmake/reader/api/controller/BookController$cacheBookOnServer$3.L$6 Ljava/lang/Object;
      // 2ff: checkcast java/util/Set
      // 302: astore 11
      // 304: aload 25
      // 306: getfield com/htmake/reader/api/controller/BookController$cacheBookOnServer$3.L$5 Ljava/lang/Object;
      // 309: checkcast java/util/List
      // 30c: astore 10
      // 30e: aload 25
      // 310: getfield com/htmake/reader/api/controller/BookController$cacheBookOnServer$3.L$4 Ljava/lang/Object;
      // 313: checkcast java/lang/String
      // 316: astore 9
      // 318: aload 25
      // 31a: getfield com/htmake/reader/api/controller/BookController$cacheBookOnServer$3.L$3 Ljava/lang/Object;
      // 31d: checkcast io/legado/app/data/entities/Book
      // 320: astore 8
      // 322: aload 25
      // 324: getfield com/htmake/reader/api/controller/BookController$cacheBookOnServer$3.L$2 Ljava/lang/Object;
      // 327: checkcast java/lang/String
      // 32a: astore 2
      // 32b: aload 25
      // 32d: getfield com/htmake/reader/api/controller/BookController$cacheBookOnServer$3.L$1 Ljava/lang/Object;
      // 330: checkcast io/vertx/core/json/JsonArray
      // 333: astore 1
      // 334: aload 25
      // 336: getfield com/htmake/reader/api/controller/BookController$cacheBookOnServer$3.L$0 Ljava/lang/Object;
      // 339: checkcast com/htmake/reader/api/controller/BookController
      // 33c: astore 0
      // 33d: nop
      // 33e: aload 24
      // 340: invokestatic kotlin/ResultKt.throwOnFailure (Ljava/lang/Object;)V
      // 343: aload 24
      // 345: checkcast java/lang/String
      // 348: astore 19
      // 34a: new java/io/File
      // 34d: dup
      // 34e: new java/lang/StringBuilder
      // 351: dup
      // 352: invokespecial java/lang/StringBuilder.<init> ()V
      // 355: aload 12
      // 357: invokevirtual java/io/File.getAbsolutePath ()Ljava/lang/String;
      // 35a: invokevirtual java/lang/StringBuilder.append (Ljava/lang/String;)Ljava/lang/StringBuilder;
      // 35d: getstatic java/io/File.separator Ljava/lang/String;
      // 360: invokevirtual java/lang/StringBuilder.append (Ljava/lang/Object;)Ljava/lang/StringBuilder;
      // 363: iload 16
      // 365: invokevirtual java/lang/StringBuilder.append (I)Ljava/lang/StringBuilder;
      // 368: ldc_w ".txt"
      // 36b: invokevirtual java/lang/StringBuilder.append (Ljava/lang/String;)Ljava/lang/StringBuilder;
      // 36e: invokevirtual java/lang/StringBuilder.toString ()Ljava/lang/String;
      // 371: invokespecial java/io/File.<init> (Ljava/lang/String;)V
      // 374: astore 20
      // 376: aload 20
      // 378: aload 19
      // 37a: aconst_null
      // 37b: bipush 2
      // 37c: aconst_null
      // 37d: invokestatic kotlin/io/FilesKt.writeText$default (Ljava/io/File;Ljava/lang/String;Ljava/nio/charset/Charset;ILjava/lang/Object;)V
      // 380: getstatic io/legado/app/help/BookHelp.INSTANCE Lio/legado/app/help/BookHelp;
      // 383: aload 0
      // 384: checkcast kotlinx/coroutines/CoroutineScope
      // 387: getstatic io/legado/app/data/entities/BookSource.Companion Lio/legado/app/data/entities/BookSource$Companion;
      // 38a: aload 9
      // 38c: invokevirtual io/legado/app/data/entities/BookSource$Companion.fromJson-IoAF18A (Ljava/lang/String;)Ljava/lang/Object;
      // 38f: astore 22
      // 391: bipush 0
      // 392: istore 23
      // 394: aload 22
      // 396: invokestatic kotlin/Result.isFailure-impl (Ljava/lang/Object;)Z
      // 399: ifeq 3a0
      // 39c: aconst_null
      // 39d: goto 3a2
      // 3a0: aload 22
      // 3a2: checkcast io/legado/app/data/entities/BookSource
      // 3a5: astore 21
      // 3a7: aload 21
      // 3a9: ifnonnull 3d4
      // 3ac: new io/legado/app/data/entities/BookSource
      // 3af: dup
      // 3b0: aconst_null
      // 3b1: aconst_null
      // 3b2: aconst_null
      // 3b3: bipush 0
      // 3b4: aconst_null
      // 3b5: bipush 0
      // 3b6: bipush 0
      // 3b7: bipush 0
      // 3b8: aconst_null
      // 3b9: aconst_null
      // 3ba: aconst_null
      // 3bb: aconst_null
      // 3bc: aconst_null
      // 3bd: aconst_null
      // 3be: aconst_null
      // 3bf: aconst_null
      // 3c0: lconst_0
      // 3c1: lconst_0
      // 3c2: bipush 0
      // 3c3: aconst_null
      // 3c4: aconst_null
      // 3c5: aconst_null
      // 3c6: aconst_null
      // 3c7: aconst_null
      // 3c8: aconst_null
      // 3c9: aconst_null
      // 3ca: ldc_w 67108863
      // 3cd: aconst_null
      // 3ce: invokespecial io/legado/app/data/entities/BookSource.<init> (Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;ILjava/lang/String;IZZLjava/lang/Boolean;Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;JJILjava/lang/String;Lio/legado/app/data/entities/rule/ExploreRule;Ljava/lang/String;Lio/legado/app/data/entities/rule/SearchRule;Lio/legado/app/data/entities/rule/BookInfoRule;Lio/legado/app/data/entities/rule/TocRule;Lio/legado/app/data/entities/rule/ContentRule;ILkotlin/jvm/internal/DefaultConstructorMarker;)V
      // 3d1: goto 3d6
      // 3d4: aload 21
      // 3d6: aload 8
      // 3d8: aload 17
      // 3da: aload 19
      // 3dc: aload 25
      // 3de: aload 25
      // 3e0: aload 0
      // 3e1: putfield com/htmake/reader/api/controller/BookController$cacheBookOnServer$3.L$0 Ljava/lang/Object;
      // 3e4: aload 25
      // 3e6: aload 1
      // 3e7: putfield com/htmake/reader/api/controller/BookController$cacheBookOnServer$3.L$1 Ljava/lang/Object;
      // 3ea: aload 25
      // 3ec: aload 2
      // 3ed: putfield com/htmake/reader/api/controller/BookController$cacheBookOnServer$3.L$2 Ljava/lang/Object;
      // 3f0: aload 25
      // 3f2: aload 8
      // 3f4: putfield com/htmake/reader/api/controller/BookController$cacheBookOnServer$3.L$3 Ljava/lang/Object;
      // 3f7: aload 25
      // 3f9: aload 9
      // 3fb: putfield com/htmake/reader/api/controller/BookController$cacheBookOnServer$3.L$4 Ljava/lang/Object;
      // 3fe: aload 25
      // 400: aload 10
      // 402: putfield com/htmake/reader/api/controller/BookController$cacheBookOnServer$3.L$5 Ljava/lang/Object;
      // 405: aload 25
      // 407: aload 11
      // 409: putfield com/htmake/reader/api/controller/BookController$cacheBookOnServer$3.L$6 Ljava/lang/Object;
      // 40c: aload 25
      // 40e: aload 12
      // 410: putfield com/htmake/reader/api/controller/BookController$cacheBookOnServer$3.L$7 Ljava/lang/Object;
      // 413: aload 25
      // 415: aconst_null
      // 416: putfield com/htmake/reader/api/controller/BookController$cacheBookOnServer$3.L$8 Ljava/lang/Object;
      // 419: aload 25
      // 41b: iload 4
      // 41d: putfield com/htmake/reader/api/controller/BookController$cacheBookOnServer$3.I$0 I
      // 420: aload 25
      // 422: iload 5
      // 424: putfield com/htmake/reader/api/controller/BookController$cacheBookOnServer$3.I$1 I
      // 427: aload 25
      // 429: iload 13
      // 42b: putfield com/htmake/reader/api/controller/BookController$cacheBookOnServer$3.I$2 I
      // 42e: aload 25
      // 430: iload 14
      // 432: putfield com/htmake/reader/api/controller/BookController$cacheBookOnServer$3.I$3 I
      // 435: aload 25
      // 437: iload 16
      // 439: putfield com/htmake/reader/api/controller/BookController$cacheBookOnServer$3.I$4 I
      // 43c: aload 25
      // 43e: bipush 3
      // 43f: putfield com/htmake/reader/api/controller/BookController$cacheBookOnServer$3.label I
      // 442: invokevirtual io/legado/app/help/BookHelp.saveImages (Lkotlinx/coroutines/CoroutineScope;Lio/legado/app/data/entities/BookSource;Lio/legado/app/data/entities/Book;Lio/legado/app/data/entities/BookChapter;Ljava/lang/String;Lkotlin/coroutines/Continuation;)Ljava/lang/Object;
      // 445: dup
      // 446: aload 26
      // 448: if_acmpne 4c6
      // 44b: aload 26
      // 44d: areturn
      // 44e: aload 25
      // 450: getfield com/htmake/reader/api/controller/BookController$cacheBookOnServer$3.I$4 I
      // 453: istore 16
      // 455: aload 25
      // 457: getfield com/htmake/reader/api/controller/BookController$cacheBookOnServer$3.I$3 I
      // 45a: istore 14
      // 45c: aload 25
      // 45e: getfield com/htmake/reader/api/controller/BookController$cacheBookOnServer$3.I$2 I
      // 461: istore 13
      // 463: aload 25
      // 465: getfield com/htmake/reader/api/controller/BookController$cacheBookOnServer$3.I$1 I
      // 468: istore 5
      // 46a: aload 25
      // 46c: getfield com/htmake/reader/api/controller/BookController$cacheBookOnServer$3.I$0 I
      // 46f: istore 4
      // 471: aload 25
      // 473: getfield com/htmake/reader/api/controller/BookController$cacheBookOnServer$3.L$7 Ljava/lang/Object;
      // 476: checkcast java/io/File
      // 479: astore 12
      // 47b: aload 25
      // 47d: getfield com/htmake/reader/api/controller/BookController$cacheBookOnServer$3.L$6 Ljava/lang/Object;
      // 480: checkcast java/util/Set
      // 483: astore 11
      // 485: aload 25
      // 487: getfield com/htmake/reader/api/controller/BookController$cacheBookOnServer$3.L$5 Ljava/lang/Object;
      // 48a: checkcast java/util/List
      // 48d: astore 10
      // 48f: aload 25
      // 491: getfield com/htmake/reader/api/controller/BookController$cacheBookOnServer$3.L$4 Ljava/lang/Object;
      // 494: checkcast java/lang/String
      // 497: astore 9
      // 499: aload 25
      // 49b: getfield com/htmake/reader/api/controller/BookController$cacheBookOnServer$3.L$3 Ljava/lang/Object;
      // 49e: checkcast io/legado/app/data/entities/Book
      // 4a1: astore 8
      // 4a3: aload 25
      // 4a5: getfield com/htmake/reader/api/controller/BookController$cacheBookOnServer$3.L$2 Ljava/lang/Object;
      // 4a8: checkcast java/lang/String
      // 4ab: astore 2
      // 4ac: aload 25
      // 4ae: getfield com/htmake/reader/api/controller/BookController$cacheBookOnServer$3.L$1 Ljava/lang/Object;
      // 4b1: checkcast io/vertx/core/json/JsonArray
      // 4b4: astore 1
      // 4b5: aload 25
      // 4b7: getfield com/htmake/reader/api/controller/BookController$cacheBookOnServer$3.L$0 Ljava/lang/Object;
      // 4ba: checkcast com/htmake/reader/api/controller/BookController
      // 4bd: astore 0
      // 4be: nop
      // 4bf: aload 24
      // 4c1: invokestatic kotlin/ResultKt.throwOnFailure (Ljava/lang/Object;)V
      // 4c4: aload 24
      // 4c6: pop
      // 4c7: aload 11
      // 4c9: iload 16
      // 4cb: invokestatic kotlin/coroutines/jvm/internal/Boxing.boxInt (I)Ljava/lang/Integer;
      // 4ce: invokeinterface java/util/Set.add (Ljava/lang/Object;)Z 2
      // 4d3: pop
      // 4d4: goto 4e9
      // 4d7: astore 18
      // 4d9: invokestatic com/htmake/reader/api/controller/BookControllerKt.access$getLogger$p ()Lmu/KLogger;
      // 4dc: ldc_w "cacheBookOnServer error: {}"
      // 4df: aload 18
      // 4e1: invokevirtual java/lang/Exception.getMessage ()Ljava/lang/String;
      // 4e4: invokeinterface mu/KLogger.info (Ljava/lang/String;Ljava/lang/Object;)V 3
      // 4e9: iload 13
      // 4eb: iload 14
      // 4ed: if_icmple 1e5
      // 4f0: invokestatic com/htmake/reader/api/controller/BookControllerKt.access$getLogger$p ()Lmu/KLogger;
      // 4f3: ldc_w "缓存书籍完成: {}"
      // 4f6: aload 8
      // 4f8: invokeinterface mu/KLogger.info (Ljava/lang/String;Ljava/lang/Object;)V 3
      // 4fd: iload 4
      // 4ff: iload 5
      // 501: if_icmplt 075
      // 504: getstatic kotlin/Unit.INSTANCE Lkotlin/Unit;
      // 507: areturn
      // 508: new java/lang/IllegalStateException
      // 50b: dup
      // 50c: ldc_w "call to 'resume' before 'invoke' with coroutine"
      // 50f: invokespecial java/lang/IllegalStateException.<init> (Ljava/lang/String;)V
      // 512: athrow
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

   public suspend fun getSpeakStream(httpTts: HttpTTS, speakText: String, speechRate: Int): InputStream? {
      // $VF: Couldn't be decompiled
      // Please report this to the Vineflower issue tracker, at https://github.com/Vineflower/vineflower/issues with a copy of the class file (if you have the rights to distribute it!)
      // java.lang.RuntimeException: parsing failure!
      //   at org.jetbrains.java.decompiler.modules.decompiler.decompose.DomHelper.parseGraph(DomHelper.java:211)
      //   at org.jetbrains.java.decompiler.modules.decompiler.decompose.DomHelper.createStatement(DomHelper.java:27)
      //   at org.jetbrains.java.decompiler.main.rels.MethodProcessor.codeToJava(MethodProcessor.java:157)
      //
      // Bytecode:
      // 000: aload 4
      // 002: instanceof com/htmake/reader/api/controller/BookController$getSpeakStream$1
      // 005: ifeq 029
      // 008: aload 4
      // 00a: checkcast com/htmake/reader/api/controller/BookController$getSpeakStream$1
      // 00d: astore 22
      // 00f: aload 22
      // 011: getfield com/htmake/reader/api/controller/BookController$getSpeakStream$1.label I
      // 014: ldc -2147483648
      // 016: iand
      // 017: ifeq 029
      // 01a: aload 22
      // 01c: dup
      // 01d: getfield com/htmake/reader/api/controller/BookController$getSpeakStream$1.label I
      // 020: ldc -2147483648
      // 022: isub
      // 023: putfield com/htmake/reader/api/controller/BookController$getSpeakStream$1.label I
      // 026: goto 035
      // 029: new com/htmake/reader/api/controller/BookController$getSpeakStream$1
      // 02c: dup
      // 02d: aload 0
      // 02e: aload 4
      // 030: invokespecial com/htmake/reader/api/controller/BookController$getSpeakStream$1.<init> (Lcom/htmake/reader/api/controller/BookController;Lkotlin/coroutines/Continuation;)V
      // 033: astore 22
      // 035: aload 22
      // 037: getfield com/htmake/reader/api/controller/BookController$getSpeakStream$1.result Ljava/lang/Object;
      // 03a: astore 21
      // 03c: invokestatic kotlin/coroutines/intrinsics/IntrinsicsKt.getCOROUTINE_SUSPENDED ()Ljava/lang/Object;
      // 03f: astore 23
      // 041: aload 22
      // 043: getfield com/htmake/reader/api/controller/BookController$getSpeakStream$1.label I
      // 046: tableswitch 849 0 1 22 170
      // 05c: aload 21
      // 05e: invokestatic kotlin/ResultKt.throwOnFailure (Ljava/lang/Object;)V
      // 061: new kotlin/jvm/internal/Ref$IntRef
      // 064: dup
      // 065: invokespecial kotlin/jvm/internal/Ref$IntRef.<init> ()V
      // 068: astore 5
      // 06a: nop
      // 06b: nop
      // 06c: new io/legado/app/model/analyzeRule/AnalyzeUrl
      // 06f: dup
      // 070: aload 1
      // 071: invokevirtual io/legado/app/data/entities/HttpTTS.getUrl ()Ljava/lang/String;
      // 074: aconst_null
      // 075: aconst_null
      // 076: aload 2
      // 077: iload 3
      // 078: invokestatic kotlin/coroutines/jvm/internal/Boxing.boxInt (I)Ljava/lang/Integer;
      // 07b: aconst_null
      // 07c: aload 1
      // 07d: checkcast io/legado/app/data/entities/BaseSource
      // 080: aconst_null
      // 081: aconst_null
      // 082: aload 1
      // 083: bipush 1
      // 084: invokevirtual io/legado/app/data/entities/HttpTTS.getHeaderMap (Z)Ljava/util/HashMap;
      // 087: checkcast java/util/Map
      // 08a: getstatic io/legado/app/model/Debug.INSTANCE Lio/legado/app/model/Debug;
      // 08d: checkcast io/legado/app/model/DebugLog
      // 090: sipush 422
      // 093: aconst_null
      // 094: invokespecial io/legado/app/model/analyzeRule/AnalyzeUrl.<init> (Ljava/lang/String;Ljava/lang/String;Ljava/lang/Integer;Ljava/lang/String;Ljava/lang/Integer;Ljava/lang/String;Lio/legado/app/data/entities/BaseSource;Lio/legado/app/model/analyzeRule/RuleDataInterface;Lio/legado/app/data/entities/BookChapter;Ljava/util/Map;Lio/legado/app/model/DebugLog;ILkotlin/jvm/internal/DefaultConstructorMarker;)V
      // 097: astore 6
      // 099: new kotlin/jvm/internal/Ref$ObjectRef
      // 09c: dup
      // 09d: invokespecial kotlin/jvm/internal/Ref$ObjectRef.<init> ()V
      // 0a0: astore 7
      // 0a2: aload 7
      // 0a4: astore 19
      // 0a6: aload 6
      // 0a8: aload 22
      // 0aa: aload 22
      // 0ac: aload 0
      // 0ad: putfield com/htmake/reader/api/controller/BookController$getSpeakStream$1.L$0 Ljava/lang/Object;
      // 0b0: aload 22
      // 0b2: aload 1
      // 0b3: putfield com/htmake/reader/api/controller/BookController$getSpeakStream$1.L$1 Ljava/lang/Object;
      // 0b6: aload 22
      // 0b8: aload 2
      // 0b9: putfield com/htmake/reader/api/controller/BookController$getSpeakStream$1.L$2 Ljava/lang/Object;
      // 0bc: aload 22
      // 0be: aload 5
      // 0c0: putfield com/htmake/reader/api/controller/BookController$getSpeakStream$1.L$3 Ljava/lang/Object;
      // 0c3: aload 22
      // 0c5: aload 6
      // 0c7: putfield com/htmake/reader/api/controller/BookController$getSpeakStream$1.L$4 Ljava/lang/Object;
      // 0ca: aload 22
      // 0cc: aload 7
      // 0ce: putfield com/htmake/reader/api/controller/BookController$getSpeakStream$1.L$5 Ljava/lang/Object;
      // 0d1: aload 22
      // 0d3: aload 19
      // 0d5: putfield com/htmake/reader/api/controller/BookController$getSpeakStream$1.L$6 Ljava/lang/Object;
      // 0d8: aload 22
      // 0da: iload 3
      // 0db: putfield com/htmake/reader/api/controller/BookController$getSpeakStream$1.I$0 I
      // 0de: aload 22
      // 0e0: bipush 1
      // 0e1: putfield com/htmake/reader/api/controller/BookController$getSpeakStream$1.label I
      // 0e4: invokevirtual io/legado/app/model/analyzeRule/AnalyzeUrl.getResponseAwait (Lkotlin/coroutines/Continuation;)Ljava/lang/Object;
      // 0e7: dup
      // 0e8: aload 23
      // 0ea: if_acmpne 141
      // 0ed: aload 23
      // 0ef: areturn
      // 0f0: aload 22
      // 0f2: getfield com/htmake/reader/api/controller/BookController$getSpeakStream$1.I$0 I
      // 0f5: istore 3
      // 0f6: aload 22
      // 0f8: getfield com/htmake/reader/api/controller/BookController$getSpeakStream$1.L$6 Ljava/lang/Object;
      // 0fb: checkcast kotlin/jvm/internal/Ref$ObjectRef
      // 0fe: astore 19
      // 100: aload 22
      // 102: getfield com/htmake/reader/api/controller/BookController$getSpeakStream$1.L$5 Ljava/lang/Object;
      // 105: checkcast kotlin/jvm/internal/Ref$ObjectRef
      // 108: astore 7
      // 10a: aload 22
      // 10c: getfield com/htmake/reader/api/controller/BookController$getSpeakStream$1.L$4 Ljava/lang/Object;
      // 10f: checkcast io/legado/app/model/analyzeRule/AnalyzeUrl
      // 112: astore 6
      // 114: aload 22
      // 116: getfield com/htmake/reader/api/controller/BookController$getSpeakStream$1.L$3 Ljava/lang/Object;
      // 119: checkcast kotlin/jvm/internal/Ref$IntRef
      // 11c: astore 5
      // 11e: aload 22
      // 120: getfield com/htmake/reader/api/controller/BookController$getSpeakStream$1.L$2 Ljava/lang/Object;
      // 123: checkcast java/lang/String
      // 126: astore 2
      // 127: aload 22
      // 129: getfield com/htmake/reader/api/controller/BookController$getSpeakStream$1.L$1 Ljava/lang/Object;
      // 12c: checkcast io/legado/app/data/entities/HttpTTS
      // 12f: astore 1
      // 130: aload 22
      // 132: getfield com/htmake/reader/api/controller/BookController$getSpeakStream$1.L$0 Ljava/lang/Object;
      // 135: checkcast com/htmake/reader/api/controller/BookController
      // 138: astore 0
      // 139: nop
      // 13a: aload 21
      // 13c: invokestatic kotlin/ResultKt.throwOnFailure (Ljava/lang/Object;)V
      // 13f: aload 21
      // 141: astore 20
      // 143: aload 19
      // 145: aload 20
      // 147: putfield kotlin/jvm/internal/Ref$ObjectRef.element Ljava/lang/Object;
      // 14a: aload 0
      // 14b: invokevirtual com/htmake/reader/api/controller/BookController.getCoroutineContext ()Lkotlin/coroutines/CoroutineContext;
      // 14e: invokestatic kotlinx/coroutines/JobKt.ensureActive (Lkotlin/coroutines/CoroutineContext;)V
      // 151: aload 1
      // 152: invokevirtual io/legado/app/data/entities/HttpTTS.getLoginCheckJs ()Ljava/lang/String;
      // 155: astore 8
      // 157: aload 8
      // 159: astore 9
      // 15b: aload 9
      // 15d: ifnonnull 164
      // 160: bipush 0
      // 161: goto 184
      // 164: aload 9
      // 166: checkcast java/lang/CharSequence
      // 169: astore 10
      // 16b: bipush 0
      // 16c: istore 11
      // 16e: aload 10
      // 170: invokestatic kotlin/text/StringsKt.isBlank (Ljava/lang/CharSequence;)Z
      // 173: ifne 17a
      // 176: bipush 1
      // 177: goto 17b
      // 17a: bipush 0
      // 17b: bipush 1
      // 17c: if_icmpne 183
      // 17f: bipush 1
      // 180: goto 184
      // 183: bipush 0
      // 184: ifeq 1af
      // 187: aload 7
      // 189: aload 6
      // 18b: aload 8
      // 18d: aload 7
      // 18f: getfield kotlin/jvm/internal/Ref$ObjectRef.element Ljava/lang/Object;
      // 192: invokevirtual io/legado/app/model/analyzeRule/AnalyzeUrl.evalJS (Ljava/lang/String;Ljava/lang/Object;)Ljava/lang/Object;
      // 195: astore 9
      // 197: aload 9
      // 199: ifnonnull 1a7
      // 19c: new java/lang/NullPointerException
      // 19f: dup
      // 1a0: ldc_w "null cannot be cast to non-null type okhttp3.Response"
      // 1a3: invokespecial java/lang/NullPointerException.<init> (Ljava/lang/String;)V
      // 1a6: athrow
      // 1a7: aload 9
      // 1a9: checkcast okhttp3/Response
      // 1ac: putfield kotlin/jvm/internal/Ref$ObjectRef.element Ljava/lang/Object;
      // 1af: aload 7
      // 1b1: getfield kotlin/jvm/internal/Ref$ObjectRef.element Ljava/lang/Object;
      // 1b4: checkcast okhttp3/Response
      // 1b7: invokevirtual okhttp3/Response.headers ()Lokhttp3/Headers;
      // 1ba: ldc_w "Content-Type"
      // 1bd: invokevirtual okhttp3/Headers.get (Ljava/lang/String;)Ljava/lang/String;
      // 1c0: astore 9
      // 1c2: aload 9
      // 1c4: ifnonnull 1ca
      // 1c7: goto 27e
      // 1ca: aload 9
      // 1cc: astore 10
      // 1ce: bipush 0
      // 1cf: istore 11
      // 1d1: bipush 0
      // 1d2: istore 12
      // 1d4: aload 10
      // 1d6: astore 13
      // 1d8: bipush 0
      // 1d9: istore 14
      // 1db: aload 1
      // 1dc: invokevirtual io/legado/app/data/entities/HttpTTS.getContentType ()Ljava/lang/String;
      // 1df: astore 15
      // 1e1: aload 13
      // 1e3: ldc_w "application/json"
      // 1e6: invokestatic kotlin/jvm/internal/Intrinsics.areEqual (Ljava/lang/Object;Ljava/lang/Object;)Z
      // 1e9: ifeq 206
      // 1ec: new io/legado/app/exception/NoStackTraceException
      // 1ef: dup
      // 1f0: aload 7
      // 1f2: getfield kotlin/jvm/internal/Ref$ObjectRef.element Ljava/lang/Object;
      // 1f5: checkcast okhttp3/Response
      // 1f8: invokevirtual okhttp3/Response.body ()Lokhttp3/ResponseBody;
      // 1fb: dup
      // 1fc: invokestatic kotlin/jvm/internal/Intrinsics.checkNotNull (Ljava/lang/Object;)V
      // 1ff: invokevirtual okhttp3/ResponseBody.string ()Ljava/lang/String;
      // 202: invokespecial io/legado/app/exception/NoStackTraceException.<init> (Ljava/lang/String;)V
      // 205: athrow
      // 206: aload 15
      // 208: astore 16
      // 20a: aload 16
      // 20c: ifnonnull 213
      // 20f: bipush 0
      // 210: goto 233
      // 213: aload 16
      // 215: checkcast java/lang/CharSequence
      // 218: astore 17
      // 21a: bipush 0
      // 21b: istore 18
      // 21d: aload 17
      // 21f: invokestatic kotlin/text/StringsKt.isBlank (Ljava/lang/CharSequence;)Z
      // 222: ifne 229
      // 225: bipush 1
      // 226: goto 22a
      // 229: bipush 0
      // 22a: bipush 1
      // 22b: if_icmpne 232
      // 22e: bipush 1
      // 22f: goto 233
      // 232: bipush 0
      // 233: ifeq 27c
      // 236: aload 13
      // 238: checkcast java/lang/CharSequence
      // 23b: astore 16
      // 23d: aload 15
      // 23f: astore 17
      // 241: bipush 0
      // 242: istore 18
      // 244: new kotlin/text/Regex
      // 247: dup
      // 248: aload 17
      // 24a: invokespecial kotlin/text/Regex.<init> (Ljava/lang/String;)V
      // 24d: astore 17
      // 24f: bipush 0
      // 250: istore 18
      // 252: aload 17
      // 254: aload 16
      // 256: invokevirtual kotlin/text/Regex.matches (Ljava/lang/CharSequence;)Z
      // 259: ifne 27c
      // 25c: new io/legado/app/exception/NoStackTraceException
      // 25f: dup
      // 260: ldc_w "TTS服务器返回错误："
      // 263: aload 7
      // 265: getfield kotlin/jvm/internal/Ref$ObjectRef.element Ljava/lang/Object;
      // 268: checkcast okhttp3/Response
      // 26b: invokevirtual okhttp3/Response.body ()Lokhttp3/ResponseBody;
      // 26e: dup
      // 26f: invokestatic kotlin/jvm/internal/Intrinsics.checkNotNull (Ljava/lang/Object;)V
      // 272: invokevirtual okhttp3/ResponseBody.string ()Ljava/lang/String;
      // 275: invokestatic kotlin/jvm/internal/Intrinsics.stringPlus (Ljava/lang/String;Ljava/lang/Object;)Ljava/lang/String;
      // 278: invokespecial io/legado/app/exception/NoStackTraceException.<init> (Ljava/lang/String;)V
      // 27b: athrow
      // 27c: nop
      // 27d: nop
      // 27e: aload 0
      // 27f: invokevirtual com/htmake/reader/api/controller/BookController.getCoroutineContext ()Lkotlin/coroutines/CoroutineContext;
      // 282: invokestatic kotlinx/coroutines/JobKt.ensureActive (Lkotlin/coroutines/CoroutineContext;)V
      // 285: aload 7
      // 287: getfield kotlin/jvm/internal/Ref$ObjectRef.element Ljava/lang/Object;
      // 28a: checkcast okhttp3/Response
      // 28d: invokevirtual okhttp3/Response.body ()Lokhttp3/ResponseBody;
      // 290: dup
      // 291: invokestatic kotlin/jvm/internal/Intrinsics.checkNotNull (Ljava/lang/Object;)V
      // 294: invokevirtual okhttp3/ResponseBody.byteStream ()Ljava/io/InputStream;
      // 297: astore 9
      // 299: bipush 0
      // 29a: istore 10
      // 29c: bipush 0
      // 29d: istore 11
      // 29f: aload 9
      // 2a1: astore 12
      // 2a3: bipush 0
      // 2a4: istore 13
      // 2a6: aload 5
      // 2a8: bipush 0
      // 2a9: putfield kotlin/jvm/internal/Ref$IntRef.element I
      // 2ac: aload 12
      // 2ae: areturn
      // 2af: astore 6
      // 2b1: aload 6
      // 2b3: astore 7
      // 2b5: aload 7
      // 2b7: instanceof java/util/concurrent/CancellationException
      // 2ba: ifeq 2c0
      // 2bd: aload 6
      // 2bf: athrow
      // 2c0: aload 7
      // 2c2: instanceof com/script/ScriptException
      // 2c5: ifeq 2cc
      // 2c8: bipush 1
      // 2c9: goto 2d1
      // 2cc: aload 7
      // 2ce: instanceof org/mozilla/javascript/WrappedException
      // 2d1: ifeq 2ef
      // 2d4: invokestatic com/htmake/reader/api/controller/BookControllerKt.access$getLogger$p ()Lmu/KLogger;
      // 2d7: ldc_w "js错误\n"
      // 2da: aload 6
      // 2dc: invokevirtual java/lang/Exception.getLocalizedMessage ()Ljava/lang/String;
      // 2df: invokestatic kotlin/jvm/internal/Intrinsics.stringPlus (Ljava/lang/String;Ljava/lang/Object;)Ljava/lang/String;
      // 2e2: aload 6
      // 2e4: checkcast java/lang/Throwable
      // 2e7: invokeinterface mu/KLogger.error (Ljava/lang/String;Ljava/lang/Throwable;)V 3
      // 2ec: aload 6
      // 2ee: athrow
      // 2ef: aload 7
      // 2f1: instanceof java/net/SocketTimeoutException
      // 2f4: ifeq 2fb
      // 2f7: bipush 1
      // 2f8: goto 300
      // 2fb: aload 7
      // 2fd: instanceof java/net/ConnectException
      // 300: ifeq 33b
      // 303: aload 5
      // 305: getfield kotlin/jvm/internal/Ref$IntRef.element I
      // 308: istore 8
      // 30a: aload 5
      // 30c: iload 8
      // 30e: bipush 1
      // 30f: iadd
      // 310: putfield kotlin/jvm/internal/Ref$IntRef.element I
      // 313: aload 5
      // 315: getfield kotlin/jvm/internal/Ref$IntRef.element I
      // 318: bipush 5
      // 319: if_icmple 06a
      // 31c: ldc_w "tts超时或连接错误超过5次\n"
      // 31f: aload 6
      // 321: invokevirtual java/lang/Exception.getLocalizedMessage ()Ljava/lang/String;
      // 324: invokestatic kotlin/jvm/internal/Intrinsics.stringPlus (Ljava/lang/String;Ljava/lang/Object;)Ljava/lang/String;
      // 327: astore 8
      // 329: invokestatic com/htmake/reader/api/controller/BookControllerKt.access$getLogger$p ()Lmu/KLogger;
      // 32c: aload 8
      // 32e: aload 6
      // 330: checkcast java/lang/Throwable
      // 333: invokeinterface mu/KLogger.error (Ljava/lang/String;Ljava/lang/Throwable;)V 3
      // 338: aload 6
      // 33a: athrow
      // 33b: aload 5
      // 33d: getfield kotlin/jvm/internal/Ref$IntRef.element I
      // 340: istore 8
      // 342: aload 5
      // 344: iload 8
      // 346: bipush 1
      // 347: iadd
      // 348: putfield kotlin/jvm/internal/Ref$IntRef.element I
      // 34b: invokestatic com/htmake/reader/api/controller/BookControllerKt.access$getLogger$p ()Lmu/KLogger;
      // 34e: ldc_w "tts下载错误\n"
      // 351: aload 6
      // 353: invokevirtual java/lang/Exception.getLocalizedMessage ()Ljava/lang/String;
      // 356: invokestatic kotlin/jvm/internal/Intrinsics.stringPlus (Ljava/lang/String;Ljava/lang/Object;)Ljava/lang/String;
      // 359: aload 6
      // 35b: checkcast java/lang/Throwable
      // 35e: invokeinterface mu/KLogger.error (Ljava/lang/String;Ljava/lang/Throwable;)V 3
      // 363: aload 5
      // 365: getfield kotlin/jvm/internal/Ref$IntRef.element I
      // 368: bipush 5
      // 369: if_icmple 383
      // 36c: ldc_w "TTS服务器连续5次错误，已暂停阅读。"
      // 36f: astore 8
      // 371: invokestatic com/htmake/reader/api/controller/BookControllerKt.access$getLogger$p ()Lmu/KLogger;
      // 374: aload 8
      // 376: aload 6
      // 378: checkcast java/lang/Throwable
      // 37b: invokeinterface mu/KLogger.error (Ljava/lang/String;Ljava/lang/Throwable;)V 3
      // 380: aload 6
      // 382: athrow
      // 383: invokestatic com/htmake/reader/api/controller/BookControllerKt.access$getLogger$p ()Lmu/KLogger;
      // 386: ldc_w "TTS下载音频出错，使用无声音频代替。\n朗读文本："
      // 389: aload 2
      // 38a: invokestatic kotlin/jvm/internal/Intrinsics.stringPlus (Ljava/lang/String;Ljava/lang/Object;)Ljava/lang/String;
      // 38d: invokeinterface mu/KLogger.error (Ljava/lang/String;)V 2
      // 392: goto 395
      // 395: aconst_null
      // 396: areturn
      // 397: new java/lang/IllegalStateException
      // 39a: dup
      // 39b: ldc_w "call to 'resume' before 'invoke' with coroutine"
      // 39e: invokespecial java/lang/IllegalStateException.<init> (Ljava/lang/String;)V
      // 3a1: athrow
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
            map.forEach(new 0((new Function2<java.lang.String, java.lang.String, Unit>(var13) {
               {
                  super(2, receiver, CaseInsensitiveHeaders::class.java, "add", "add(Ljava/lang/String;Ljava/lang/String;)Lio/vertx/core/MultiMap;", 8);
               }

               public final void invoke(java.lang.String p0, java.lang.String p1) {
                  BookController.access$ttsByTextToSpeechCn$add(access$getReceiver$p(this) as CaseInsensitiveHeaders, p0, p1);
               }
            }) as Function2));
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

   private suspend fun setCover(book: Book, epubBook: EpubBook, bookSourceString: String?) {
      // $VF: Couldn't be decompiled
      // Please report this to the Vineflower issue tracker, at https://github.com/Vineflower/vineflower/issues with a copy of the class file (if you have the rights to distribute it!)
      // java.lang.NullPointerException: Cannot invoke "org.jetbrains.java.decompiler.code.cfg.ExceptionRangeCFG.isCircular()" because "range" is null
      //   at org.jetbrains.java.decompiler.modules.decompiler.decompose.DomHelper.graphToStatement(DomHelper.java:84)
      //   at org.jetbrains.java.decompiler.modules.decompiler.decompose.DomHelper.parseGraph(DomHelper.java:203)
      //   at org.jetbrains.java.decompiler.modules.decompiler.decompose.DomHelper.createStatement(DomHelper.java:27)
      //   at org.jetbrains.java.decompiler.main.rels.MethodProcessor.codeToJava(MethodProcessor.java:157)
      //
      // Bytecode:
      // 000: aload 4
      // 002: instanceof com/htmake/reader/api/controller/BookController$setCover$1
      // 005: ifeq 029
      // 008: aload 4
      // 00a: checkcast com/htmake/reader/api/controller/BookController$setCover$1
      // 00d: astore 17
      // 00f: aload 17
      // 011: getfield com/htmake/reader/api/controller/BookController$setCover$1.label I
      // 014: ldc -2147483648
      // 016: iand
      // 017: ifeq 029
      // 01a: aload 17
      // 01c: dup
      // 01d: getfield com/htmake/reader/api/controller/BookController$setCover$1.label I
      // 020: ldc -2147483648
      // 022: isub
      // 023: putfield com/htmake/reader/api/controller/BookController$setCover$1.label I
      // 026: goto 035
      // 029: new com/htmake/reader/api/controller/BookController$setCover$1
      // 02c: dup
      // 02d: aload 0
      // 02e: aload 4
      // 030: invokespecial com/htmake/reader/api/controller/BookController$setCover$1.<init> (Lcom/htmake/reader/api/controller/BookController;Lkotlin/coroutines/Continuation;)V
      // 033: astore 17
      // 035: aload 17
      // 037: getfield com/htmake/reader/api/controller/BookController$setCover$1.result Ljava/lang/Object;
      // 03a: astore 16
      // 03c: invokestatic kotlin/coroutines/intrinsics/IntrinsicsKt.getCOROUTINE_SUSPENDED ()Ljava/lang/Object;
      // 03f: astore 18
      // 041: aload 17
      // 043: getfield com/htmake/reader/api/controller/BookController$setCover$1.label I
      // 046: tableswitch 459 0 1 22 384
      // 05c: aload 16
      // 05e: invokestatic kotlin/ResultKt.throwOnFailure (Ljava/lang/Object;)V
      // 061: aload 1
      // 062: invokevirtual io/legado/app/data/entities/Book.getDisplayCover ()Ljava/lang/String;
      // 065: astore 5
      // 067: aload 5
      // 069: ifnonnull 06f
      // 06c: goto 20d
      // 06f: aload 5
      // 071: ldc_w "/"
      // 074: bipush 0
      // 075: bipush 2
      // 076: aconst_null
      // 077: invokestatic kotlin/text/StringsKt.startsWith$default (Ljava/lang/String;Ljava/lang/String;ZILjava/lang/Object;)Z
      // 07a: ifeq 0f4
      // 07d: new java/io/File
      // 080: dup
      // 081: bipush 2
      // 082: anewarray 96
      // 085: astore 7
      // 087: aload 7
      // 089: bipush 0
      // 08a: ldc "storage"
      // 08c: aastore
      // 08d: aload 7
      // 08f: bipush 1
      // 090: aload 5
      // 092: ldc_w "/"
      // 095: getstatic java/io/File.separator Ljava/lang/String;
      // 098: astore 8
      // 09a: aload 8
      // 09c: ldc_w "separator"
      // 09f: invokestatic kotlin/jvm/internal/Intrinsics.checkNotNullExpressionValue (Ljava/lang/Object;Ljava/lang/String;)V
      // 0a2: aload 8
      // 0a4: bipush 0
      // 0a5: bipush 4
      // 0a6: aconst_null
      // 0a7: invokestatic kotlin/text/StringsKt.replace$default (Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;ZILjava/lang/Object;)Ljava/lang/String;
      // 0aa: astore 8
      // 0ac: bipush 1
      // 0ad: istore 9
      // 0af: bipush 0
      // 0b0: istore 10
      // 0b2: aload 8
      // 0b4: dup
      // 0b5: ifnonnull 0c3
      // 0b8: new java/lang/NullPointerException
      // 0bb: dup
      // 0bc: ldc_w "null cannot be cast to non-null type java.lang.String"
      // 0bf: invokespecial java/lang/NullPointerException.<init> (Ljava/lang/String;)V
      // 0c2: athrow
      // 0c3: iload 9
      // 0c5: invokevirtual java/lang/String.substring (I)Ljava/lang/String;
      // 0c8: dup
      // 0c9: ldc_w "(this as java.lang.String).substring(startIndex)"
      // 0cc: invokestatic kotlin/jvm/internal/Intrinsics.checkNotNullExpressionValue (Ljava/lang/Object;Ljava/lang/String;)V
      // 0cf: aastore
      // 0d0: aload 7
      // 0d2: invokestatic com/htmake/reader/utils/ExtKt.getWorkDir ([Ljava/lang/String;)Ljava/lang/String;
      // 0d5: invokespecial java/io/File.<init> (Ljava/lang/String;)V
      // 0d8: astore 6
      // 0da: aload 6
      // 0dc: invokestatic kotlin/io/FilesKt.readBytes (Ljava/io/File;)[B
      // 0df: astore 7
      // 0e1: aload 2
      // 0e2: new me/ag2s/epublib/domain/Resource
      // 0e5: dup
      // 0e6: aload 7
      // 0e8: ldc_w "Images/cover.jpg"
      // 0eb: invokespecial me/ag2s/epublib/domain/Resource.<init> ([BLjava/lang/String;)V
      // 0ee: invokevirtual me/ag2s/epublib/domain/EpubBook.setCoverImage (Lme/ag2s/epublib/domain/Resource;)V
      // 0f1: goto 20d
      // 0f4: aload 3
      // 0f5: ifnull 20d
      // 0f8: aload 0
      // 0f9: aload 5
      // 0fb: ldc_w "jpg"
      // 0fe: invokevirtual com/htmake/reader/api/controller/BookController.getFileExt (Ljava/lang/String;Ljava/lang/String;)Ljava/lang/String;
      // 101: astore 6
      // 103: getstatic io/legado/app/utils/MD5Utils.INSTANCE Lio/legado/app/utils/MD5Utils;
      // 106: aload 5
      // 108: invokevirtual io/legado/app/utils/MD5Utils.md5Encode (Ljava/lang/String;)Ljava/lang/String;
      // 10b: invokevirtual java/lang/String.toString ()Ljava/lang/String;
      // 10e: astore 7
      // 110: bipush 3
      // 111: anewarray 96
      // 114: astore 9
      // 116: aload 9
      // 118: bipush 0
      // 119: ldc "storage"
      // 11b: aastore
      // 11c: aload 9
      // 11e: bipush 1
      // 11f: ldc "cache"
      // 121: aastore
      // 122: aload 9
      // 124: bipush 2
      // 125: new java/lang/StringBuilder
      // 128: dup
      // 129: invokespecial java/lang/StringBuilder.<init> ()V
      // 12c: aload 7
      // 12e: invokevirtual java/lang/StringBuilder.append (Ljava/lang/String;)Ljava/lang/StringBuilder;
      // 131: bipush 46
      // 133: invokevirtual java/lang/StringBuilder.append (C)Ljava/lang/StringBuilder;
      // 136: aload 6
      // 138: invokevirtual java/lang/StringBuilder.append (Ljava/lang/String;)Ljava/lang/StringBuilder;
      // 13b: invokevirtual java/lang/StringBuilder.toString ()Ljava/lang/String;
      // 13e: aastore
      // 13f: aload 9
      // 141: invokestatic com/htmake/reader/utils/ExtKt.getWorkDir ([Ljava/lang/String;)Ljava/lang/String;
      // 144: astore 8
      // 146: new java/io/File
      // 149: dup
      // 14a: aload 8
      // 14c: invokespecial java/io/File.<init> (Ljava/lang/String;)V
      // 14f: astore 9
      // 151: aload 9
      // 153: invokevirtual java/io/File.exists ()Z
      // 156: ifeq 174
      // 159: aload 9
      // 15b: invokestatic kotlin/io/FilesKt.readBytes (Ljava/io/File;)[B
      // 15e: astore 10
      // 160: aload 2
      // 161: new me/ag2s/epublib/domain/Resource
      // 164: dup
      // 165: aload 10
      // 167: ldc_w "Images/cover.jpg"
      // 16a: invokespecial me/ag2s/epublib/domain/Resource.<init> ([BLjava/lang/String;)V
      // 16d: invokevirtual me/ag2s/epublib/domain/EpubBook.setCoverImage (Lme/ag2s/epublib/domain/Resource;)V
      // 170: getstatic kotlin/Unit.INSTANCE Lkotlin/Unit;
      // 173: areturn
      // 174: new io/legado/app/model/analyzeRule/AnalyzeUrl
      // 177: dup
      // 178: aload 5
      // 17a: aconst_null
      // 17b: aconst_null
      // 17c: aconst_null
      // 17d: aconst_null
      // 17e: aconst_null
      // 17f: getstatic io/legado/app/data/entities/BookSource.Companion Lio/legado/app/data/entities/BookSource$Companion;
      // 182: aload 3
      // 183: invokevirtual io/legado/app/data/entities/BookSource$Companion.fromJson-IoAF18A (Ljava/lang/String;)Ljava/lang/Object;
      // 186: astore 11
      // 188: bipush 0
      // 189: istore 12
      // 18b: aload 11
      // 18d: invokestatic kotlin/Result.isFailure-impl (Ljava/lang/Object;)Z
      // 190: ifeq 197
      // 193: aconst_null
      // 194: goto 199
      // 197: aload 11
      // 199: checkcast io/legado/app/data/entities/BaseSource
      // 19c: aconst_null
      // 19d: aconst_null
      // 19e: aconst_null
      // 19f: aconst_null
      // 1a0: sipush 1982
      // 1a3: aconst_null
      // 1a4: invokespecial io/legado/app/model/analyzeRule/AnalyzeUrl.<init> (Ljava/lang/String;Ljava/lang/String;Ljava/lang/Integer;Ljava/lang/String;Ljava/lang/Integer;Ljava/lang/String;Lio/legado/app/data/entities/BaseSource;Lio/legado/app/model/analyzeRule/RuleDataInterface;Lio/legado/app/data/entities/BookChapter;Ljava/util/Map;Lio/legado/app/model/DebugLog;ILkotlin/jvm/internal/DefaultConstructorMarker;)V
      // 1a7: astore 10
      // 1a9: nop
      // 1aa: aload 10
      // 1ac: aload 17
      // 1ae: aload 17
      // 1b0: aload 2
      // 1b1: putfield com/htmake/reader/api/controller/BookController$setCover$1.L$0 Ljava/lang/Object;
      // 1b4: aload 17
      // 1b6: bipush 1
      // 1b7: putfield com/htmake/reader/api/controller/BookController$setCover$1.label I
      // 1ba: invokevirtual io/legado/app/model/analyzeRule/AnalyzeUrl.getByteArrayAwait (Lkotlin/coroutines/Continuation;)Ljava/lang/Object;
      // 1bd: dup
      // 1be: aload 18
      // 1c0: if_acmpne 1d7
      // 1c3: aload 18
      // 1c5: areturn
      // 1c6: aload 17
      // 1c8: getfield com/htmake/reader/api/controller/BookController$setCover$1.L$0 Ljava/lang/Object;
      // 1cb: checkcast me/ag2s/epublib/domain/EpubBook
      // 1ce: astore 2
      // 1cf: nop
      // 1d0: aload 16
      // 1d2: invokestatic kotlin/ResultKt.throwOnFailure (Ljava/lang/Object;)V
      // 1d5: aload 16
      // 1d7: astore 11
      // 1d9: bipush 0
      // 1da: istore 12
      // 1dc: bipush 0
      // 1dd: istore 13
      // 1df: aload 11
      // 1e1: checkcast [B
      // 1e4: astore 14
      // 1e6: bipush 0
      // 1e7: istore 15
      // 1e9: aload 2
      // 1ea: new me/ag2s/epublib/domain/Resource
      // 1ed: dup
      // 1ee: aload 14
      // 1f0: ldc_w "Images/cover.jpg"
      // 1f3: invokespecial me/ag2s/epublib/domain/Resource.<init> ([BLjava/lang/String;)V
      // 1f6: invokevirtual me/ag2s/epublib/domain/EpubBook.setCoverImage (Lme/ag2s/epublib/domain/Resource;)V
      // 1f9: nop
      // 1fa: nop
      // 1fb: goto 20d
      // 1fe: astore 11
      // 200: aload 11
      // 202: invokevirtual java/lang/Exception.printStackTrace ()V
      // 205: goto 20d
      // 208: astore 11
      // 20a: aload 11
      // 20c: athrow
      // 20d: getstatic kotlin/Unit.INSTANCE Lkotlin/Unit;
      // 210: areturn
      // 211: new java/lang/IllegalStateException
      // 214: dup
      // 215: ldc_w "call to 'resume' before 'invoke' with coroutine"
      // 218: invokespecial java/lang/IllegalStateException.<init> (Ljava/lang/String;)V
      // 21b: athrow
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
