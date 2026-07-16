package io.legado.app.help

import cn.hutool.crypto.digest.DigestUtil
import cn.hutool.crypto.symmetric.AES
import cn.hutool.crypto.symmetric.DESede
import com.google.gson.Gson
import io.legado.app.adapters.ReaderAdapterHelper
import io.legado.app.constant.AppConst
import io.legado.app.data.entities.BaseSource
import io.legado.app.exception.NoStackTraceException
import io.legado.app.help.http.CookieStore
import io.legado.app.help.http.HttpHelperKt
import io.legado.app.help.http.OkHttpUtilsKt
import io.legado.app.help.http.SSLHelper
import io.legado.app.help.http.StrResponse
import io.legado.app.model.Debug
import io.legado.app.model.DebugLog
import io.legado.app.model.analyzeRule.AnalyzeUrl
import io.legado.app.model.analyzeRule.QueryTTF
import io.legado.app.utils.Base64
import io.legado.app.utils.EncoderUtils
import io.legado.app.utils.EncodingDetect
import io.legado.app.utils.FileUtils
import io.legado.app.utils.GsonExtensionsKt
import io.legado.app.utils.HtmlFormatter
import io.legado.app.utils.LogUtilsKt
import io.legado.app.utils.MD5Utils
import io.legado.app.utils.NetworkUtils
import io.legado.app.utils.StringExtensionsKt
import io.legado.app.utils.StringUtils
import io.legado.app.utils.ThrowableExtensionsKt
import io.legado.app.utils.ZipUtils
import java.io.ByteArrayInputStream
import java.io.ByteArrayOutputStream
import java.io.Closeable
import java.io.File
import java.lang.reflect.Type
import java.net.URLEncoder
import java.nio.charset.Charset
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import java.util.SimpleTimeZone
import java.util.UUID
import java.util.zip.ZipEntry
import java.util.zip.ZipInputStream
import kotlin.Result.Companion
import kotlin.coroutines.Continuation
import kotlin.coroutines.intrinsics.IntrinsicsKt
import kotlin.jvm.functions.Function1
import kotlin.jvm.functions.Function2
import kotlin.jvm.internal.Intrinsics
import kotlin.jvm.internal.Ref.ObjectRef
import kotlinx.coroutines.BuildersKt
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Deferred
import kotlinx.coroutines.Dispatchers
import okhttp3.OkHttpClient
import okhttp3.ResponseBody
import okhttp3.Request.Builder
import org.jetbrains.annotations.NotNull
import org.jetbrains.annotations.Nullable
import org.jsoup.Jsoup
import org.jsoup.Connection.Method
import org.jsoup.Connection.Response

public interface JsExtensions {
   public abstract fun getSource(): BaseSource? {
   }

   public abstract fun getUserNameSpace(): String {
   }

   public abstract fun getLogger(): DebugLog? {
   }

   public open fun ajax(urlStr: String): String? {
   }

   public open fun ajaxAll(urlList: Array<String>): Array<StrResponse?> {
   }

   public open fun connect(urlStr: String): StrResponse {
   }

   public open fun connect(urlStr: String, header: String?): StrResponse {
   }

   public open fun webView(html: String?, url: String?, js: String?): String? {
   }

   public open fun importScript(path: String): String {
   }

   public open fun cacheFile(urlStr: String): String? {
   }

   public open fun cacheFile(urlStr: String, saveTime: Int = ...): String? {
   }

   public open fun getCookie(tag: String, key: String? = ...): String {
   }

   public open fun downloadFile(content: String, url: String): String {
   }

   public open fun get(urlStr: String, headers: Map<String, String>): Response {
   }

   public open fun head(urlStr: String, headers: Map<String, String>): Response {
   }

   public open fun post(urlStr: String, body: String, headers: Map<String, String>): Response {
   }

   public open fun base64Decode(str: String): String {
   }

   public open fun base64Decode(str: String, flags: Int): String {
   }

   public open fun base64DecodeToByteArray(str: String?): ByteArray? {
   }

   public open fun base64DecodeToByteArray(str: String?, flags: Int): ByteArray? {
   }

   public open fun base64Encode(str: String): String? {
   }

   public open fun base64Encode(str: String, flags: Int): String? {
   }

   public open fun md5Encode(str: String): String {
   }

   public open fun md5Encode16(str: String): String {
   }

   public open fun timeFormatUTC(time: Long, format: String, sh: Int): String? {
   }

   public open fun timeFormat(time: Long): String {
   }

   public open fun utf8ToGbk(str: String): String {
   }

   public open fun encodeURI(str: String): String {
   }

   public open fun encodeURI(str: String, enc: String): String {
   }

   public open fun htmlFormat(str: String): String {
   }

   public open fun getFile(path: String): File {
   }

   public open fun readFile(path: String): ByteArray? {
   }

   public open fun readTxtFile(path: String): String {
   }

   public open fun readTxtFile(path: String, charsetName: String): String {
   }

   public open fun deleteFile(path: String) {
   }

   public open fun unzipFile(zipPath: String): String {
   }

   public open fun getTxtInFolder(unzipPath: String): String {
   }

   public open fun getZipStringContent(url: String, path: String): String {
   }

   public open fun getZipStringContent(url: String, path: String, charsetName: String): String {
   }

   public open fun getZipByteArrayContent(url: String, path: String): ByteArray? {
   }

   public open fun queryBase64TTF(base64: String?): QueryTTF? {
   }

   public open fun queryTTF(str: String?): QueryTTF? {
   }

   public open fun replaceFont(text: String, font1: QueryTTF?, font2: QueryTTF?): String {
   }

   public open fun toast(msg: Any?) {
   }

   public open fun longToast(msg: Any?) {
   }

   public open fun log(msg: String): String {
   }

   public open fun logType(any: Any?) {
   }

   public open fun randomUUID(): String {
   }

   public open fun aesDecodeToByteArray(str: String, key: String, transformation: String, iv: String): ByteArray? {
   }

   public open fun aesDecodeToString(str: String, key: String, transformation: String, iv: String): String? {
   }

   public open fun aesBase64DecodeToByteArray(str: String, key: String, transformation: String, iv: String): ByteArray? {
   }

   public open fun aesBase64DecodeToString(str: String, key: String, transformation: String, iv: String): String? {
   }

   public open fun aesEncodeToByteArray(data: String, key: String, transformation: String, iv: String): ByteArray? {
   }

   public open fun aesEncodeToString(data: String, key: String, transformation: String, iv: String): String? {
   }

   public open fun aesEncodeToBase64ByteArray(data: String, key: String, transformation: String, iv: String): ByteArray? {
   }

   public open fun aesEncodeToBase64String(data: String, key: String, transformation: String, iv: String): String? {
   }

   public open fun androidId(): String {
   }

   public open fun aesDecodeArgsBase64Str(data: String, key: String, mode: String, padding: String, iv: String): String? {
   }

   public open fun tripleDESDecodeStr(data: String, key: String, mode: String, padding: String, iv: String): String? {
   }

   public open fun tripleDESDecodeArgsBase64Str(data: String, key: String, mode: String, padding: String, iv: String): String? {
   }

   public open fun aesEncodeArgsBase64Str(data: String, key: String, mode: String, padding: String, iv: String): String? {
   }

   public open fun desDecodeToString(data: String, key: String, transformation: String, iv: String): String? {
   }

   public open fun desBase64DecodeToString(data: String, key: String, transformation: String, iv: String): String? {
   }

   public open fun desEncodeToString(data: String, key: String, transformation: String, iv: String): String? {
   }

   public open fun desEncodeToBase64String(data: String, key: String, transformation: String, iv: String): String? {
   }

   public open fun tripleDESEncodeBase64Str(data: String, key: String, mode: String, padding: String, iv: String): String? {
   }

   public open fun tripleDESEncodeArgsBase64Str(data: String, key: String, mode: String, padding: String, iv: String): String? {
   }

   public open fun digestHex(data: String, algorithm: String): String? {
   }

   public open fun digestBase64Str(data: String, algorithm: String): String? {
   }

   // $VF: Class flags could not be determined
   internal class DefaultImpls {
      @JvmStatic
      fun ajax(`this`: JsExtensions, urlStr: java.lang.String): java.lang.String? {
         return BuildersKt.runBlocking$default(
            null,
            (
               new Function2<CoroutineScope, Continuation<? super java.lang.String>, Object>(urlStr, this, null) {
                  int label;

                  {
                     super(2, `$completionx`);
                     this.$urlStr = `$urlStr`;
                     this.this$0 = `$receiver`;
                  }

                  @Nullable
                  @Override
                  public final Object invokeSuspend(@NotNull Object $result) {
                     val var11: Any = IntrinsicsKt.getCOROUTINE_SUSPENDED();
                     switch (this.label) {
                        case 0:
                           ResultKt.throwOnFailure(`$result`);
                           val var2: java.lang.String = this.$urlStr;
                           val var3: JsExtensions = this.this$0;

                           var it: Any;
                           try {
                              it = Result.Companion;
                              it = Result.constructor-impl(
                                 AnalyzeUrl.getStrResponse$default(
                                       new AnalyzeUrl(var2, null, null, null, null, null, var3.getSource(), null, null, null, var3.getLogger(), 958, null),
                                       var2,
                                       null,
                                       false,
                                       6,
                                       null
                                    )
                                    .getBody()
                              );
                           } catch (var12: java.lang.Throwable) {
                              val analyzeUrl: Companion = Result.Companion;
                              it = Result.constructor-impl(ResultKt.createFailure(var12));
                           }

                           val var10000: java.lang.Throwable = Result.exceptionOrNull-impl(it);
                           if (var10000 != null) {
                              LogUtilsKt.printOnDebug(var10000);
                           }

                           val var19: java.lang.Throwable = Result.exceptionOrNull-impl(it);
                           return if (var19 == null) it else ThrowableExtensionsKt.getMsg(var19);
                        default:
                           throw new IllegalStateException("call to 'resume' before 'invoke' with coroutine");
                     }
                  }

                  @NotNull
                  @Override
                  public final Continuation<Unit> create(@Nullable Object value, @NotNull Continuation<?> $completion) {
                     return new <anonymous constructor>(this.$urlStr, this.this$0, `$completion`);
                  }

                  @Nullable
                  public final Object invoke(@NotNull CoroutineScope p1, @Nullable Continuation<? super java.lang.String> p2) {
                     return (this.create(p1, p2) as <unrepresentable>).invokeSuspend(Unit.INSTANCE);
                  }
               }
            ) as Function2,
            1,
            null
         ) as java.lang.String;
      }

      @JvmStatic
      fun ajaxAll(`this`: JsExtensions, urlList: Array<java.lang.String>): Array<StrResponse> {
         return BuildersKt.runBlocking$default(
            null,
            (
               new Function2<CoroutineScope, Continuation<? super StrResponse[]>, Object>(urlList, this, null) {
                  Object L$1;
                  Object L$2;
                  int I$0;
                  int I$1;
                  int I$2;
                  int label;

                  {
                     super(2, `$completionx`);
                     this.$urlList = `$urlList`;
                     this.this$0 = `$receiver`;
                  }

                  // $VF: Irreducible bytecode was duplicated to produce valid code
                  @Nullable
                  @Override
                  public final Object invokeSuspend(@NotNull Object $result) {
                     val var12: Any = IntrinsicsKt.getCOROUTINE_SUSPENDED();
                     var asyncArray: Array<Deferred>;
                     var var5: Int;
                     var var6: Int;
                     var var7: Array<StrResponse>;
                     switch (this.label) {
                        case 0:
                           ResultKt.throwOnFailure(`$result`);
                           val `$this$runBlocking`: CoroutineScope = this.L$0 as CoroutineScope;
                           var resArray: Int = 0;
                           var5 = this.$urlList.length;

                           for (var15 = new Deferred[this.$urlList.length]; resArray < var5; resArray++) {
                              var15[resArray] = BuildersKt.async$default(
                                 `$this$runBlocking`,
                                 Dispatchers.getIO(),
                                 null,
                                 (
                                    new Function2<CoroutineScope, Continuation<? super StrResponse>, Object>(this.$urlList, resArray, this.this$0, null) {
                                       int label;

                                       {
                                          super(2, `$completionx`);
                                          this.$urlList = `$urlList`;
                                          this.$tmp = `$tmp`;
                                          this.this$0 = `$receiver`;
                                       }

                                       @Nullable
                                       @Override
                                       public final Object invokeSuspend(@NotNull Object $result) {
                                          val var4: Any = IntrinsicsKt.getCOROUTINE_SUSPENDED();
                                          switch (this.label) {
                                             case 0:
                                                ResultKt.throwOnFailure(`$result`);
                                                val url: java.lang.String = this.$urlList[this.$tmp];
                                                return AnalyzeUrl.getStrResponse$default(
                                                   new AnalyzeUrl(
                                                      this.$urlList[this.$tmp],
                                                      null,
                                                      null,
                                                      null,
                                                      null,
                                                      null,
                                                      this.this$0.getSource(),
                                                      null,
                                                      null,
                                                      null,
                                                      this.this$0.getLogger(),
                                                      958,
                                                      null
                                                   ),
                                                   url,
                                                   null,
                                                   false,
                                                   6,
                                                   null
                                                );
                                             default:
                                                throw new IllegalStateException("call to 'resume' before 'invoke' with coroutine");
                                          }
                                       }

                                       @NotNull
                                       @Override
                                       public final Continuation<Unit> create(@Nullable Object value, @NotNull Continuation<?> $completion) {
                                          return new <anonymous constructor>(this.$urlList, this.$tmp, this.this$0, `$completion`);
                                       }

                                       @Nullable
                                       public final Object invoke(@NotNull CoroutineScope p1, @Nullable Continuation<? super StrResponse> p2) {
                                          return (this.create(p1, p2) as <unrepresentable>).invokeSuspend(Unit.INSTANCE);
                                       }
                                    }
                                 ) as Function2,
                                 2,
                                 null
                              );
                           }

                           asyncArray = var15;
                           var5 = 0;
                           var6 = this.$urlList.length;
                           var7 = new StrResponse[this.$urlList.length];
                           break;
                        case 1:
                           val var10: Int = this.I$2;
                           var6 = this.I$1;
                           var5 = this.I$0;
                           val var9: Array<StrResponse> = this.L$2 as Array<StrResponse>;
                           var7 = this.L$1 as Array<StrResponse>;
                           asyncArray = this.L$0 as Array<Deferred>;
                           ResultKt.throwOnFailure(`$result`);
                           var9[var10] = `$result` as StrResponse;
                           var5++;
                           break;
                        default:
                           throw new IllegalStateException("call to 'resume' before 'invoke' with coroutine");
                     }

                     while (var5 < var6) {
                        val var10000: Deferred = asyncArray[var5];
                        val var10001: Continuation = this;
                        this.L$0 = asyncArray;
                        this.L$1 = var7;
                        this.L$2 = var7;
                        this.I$0 = var5;
                        this.I$1 = var6;
                        this.I$2 = var5;
                        this.label = 1;
                        val var16: Any = var10000.await(var10001);
                        if (var16 === var12) {
                           return var12;
                        }

                        var7[var5] = var16 as StrResponse;
                        var5++;
                     }

                     return var7;
                  }

                  @NotNull
                  @Override
                  public final Continuation<Unit> create(@Nullable Object value, @NotNull Continuation<?> $completion) {
                     val var3: Function2 = new <anonymous constructor>(this.$urlList, this.this$0, `$completion`);
                     var3.L$0 = value;
                     return var3 as Continuation<Unit>;
                  }

                  @Nullable
                  public final Object invoke(@NotNull CoroutineScope p1, @Nullable Continuation<? super StrResponse[]> p2) {
                     return (this.create(p1, p2) as <unrepresentable>).invokeSuspend(Unit.INSTANCE);
                  }
               }
            ) as Function2,
            1,
            null
         ) as Array<StrResponse>;
      }

      @JvmStatic
      fun connect(`this`: JsExtensions, urlStr: java.lang.String): StrResponse {
         return BuildersKt.runBlocking$default(
            null,
            (
               new Function2<CoroutineScope, Continuation<? super StrResponse>, Object>(urlStr, this, null) {
                  Object L$0;
                  int label;

                  {
                     super(2, `$completionx`);
                     this.$urlStr = `$urlStr`;
                     this.this$0 = `$receiver`;
                  }

                  // $VF: Handled exception range with multiple entry points by splitting it
                  // $VF: Duplicated exception handlers to handle obfuscated exceptions
                  @Nullable
                  @Override
                  public final Object invokeSuspend(@NotNull Object $result) {
                     var analyzeUrl: AnalyzeUrl;
                     var var4: Any;
                     label42: {
                        val var12: Any = IntrinsicsKt.getCOROUTINE_SUSPENDED();
                        var var10000: Any;
                        switch (this.label) {
                           case 0:
                              ResultKt.throwOnFailure(`$result`);
                              analyzeUrl = new AnalyzeUrl(
                                 this.$urlStr, null, null, null, null, null, this.this$0.getSource(), null, null, null, this.this$0.getLogger(), 958, null
                              );

                              try {
                                 var4 = Result.Companion;
                                 this.L$0 = analyzeUrl;
                                 this.label = 1;
                                 var10000 = AnalyzeUrl.getStrResponseAwait$default(analyzeUrl, null, null, false, this, 7, null);
                              } catch (var14: java.lang.Throwable) {
                                 val var26: Companion = Result.Companion;
                                 var4 = Result.constructor-impl(ResultKt.createFailure(var14));
                                 break label42;
                              }

                              if (var10000 === var12) {
                                 return var12;
                              }
                              break;
                           case 1:
                              analyzeUrl = this.L$0 as AnalyzeUrl;

                              try {
                                 ResultKt.throwOnFailure(`$result`);
                                 var10000 = `$result`;
                                 break;
                              } catch (var15: java.lang.Throwable) {
                                 val it: Companion = Result.Companion;
                                 var4 = Result.constructor-impl(ResultKt.createFailure(var15));
                                 break label42;
                              }
                           default:
                              throw new IllegalStateException("call to 'resume' before 'invoke' with coroutine");
                        }

                        try {
                           var4 = Result.constructor-impl(var10000 as StrResponse);
                        } catch (var13: java.lang.Throwable) {
                           val var27: Companion = Result.Companion;
                           var4 = Result.constructor-impl(ResultKt.createFailure(var13));
                        }
                     }

                     val var34: java.lang.Throwable = Result.exceptionOrNull-impl(var4);
                     if (var34 != null) {
                        LogUtilsKt.printOnDebug(var34);
                     }

                     val var25: java.lang.Throwable = Result.exceptionOrNull-impl(var4);
                     return if (var25 == null) var4 else new StrResponse(analyzeUrl.getUrl(), var25.getLocalizedMessage());
                  }

                  @NotNull
                  @Override
                  public final Continuation<Unit> create(@Nullable Object value, @NotNull Continuation<?> $completion) {
                     return new <anonymous constructor>(this.$urlStr, this.this$0, `$completion`);
                  }

                  @Nullable
                  public final Object invoke(@NotNull CoroutineScope p1, @Nullable Continuation<? super StrResponse> p2) {
                     return (this.create(p1, p2) as <unrepresentable>).invokeSuspend(Unit.INSTANCE);
                  }
               }
            ) as Function2,
            1,
            null
         ) as StrResponse;
      }

      @JvmStatic
      fun connect(`this`: JsExtensions, urlStr: java.lang.String, header: java.lang.String?): StrResponse {
         return BuildersKt.runBlocking$default(
            null,
            (
               new Function2<CoroutineScope, Continuation<? super StrResponse>, Object>(header, this, urlStr, null) {
                  Object L$0;
                  int label;

                  {
                     super(2, `$completionx`);
                     this.$header = `$header`;
                     this.this$0 = `$receiver`;
                     this.$urlStr = `$urlStr`;
                  }

                  // $VF: Handled exception range with multiple entry points by splitting it
                  // $VF: Duplicated exception handlers to handle obfuscated exceptions
                  @Nullable
                  @Override
                  public final Object invokeSuspend(@NotNull Object $result) {
                     var analyzeUrl: AnalyzeUrl;
                     var `$i$f$fromJsonObject`: Any;
                     label59: {
                        val var13: Any = IntrinsicsKt.getCOROUTINE_SUSPENDED();
                        var var10000: Any;
                        switch (this.label) {
                           case 0:
                              ResultKt.throwOnFailure(`$result`);
                              val var18: Gson = GsonExtensionsKt.getGSON();
                              val `json$iv`: java.lang.String = this.$header;

                              var var35: Any;
                              try {
                                 var35 = Result.Companion;
                                 val var49: Type = new JsExtensions$connect$2$invokeSuspend$$inlined$fromJsonObject$1().getType();
                                 var10000 = var18.fromJson(`json$iv`, var49);
                                 if (var10000 !is java.util.Map) {
                                    var10000 = null;
                                 }

                                 var35 = Result.constructor-impl(var10000 as java.util.Map);
                              } catch (var15: java.lang.Throwable) {
                                 val `$i$f$genericType`: Companion = Result.Companion;
                                 var35 = Result.constructor-impl(ResultKt.createFailure(var15));
                              }

                              analyzeUrl = new AnalyzeUrl(
                                 this.$urlStr,
                                 null,
                                 null,
                                 null,
                                 null,
                                 null,
                                 this.this$0.getSource(),
                                 null,
                                 null,
                                 (if (Result.isFailure-impl(var35)) null else var35) as java.util.Map,
                                 this.this$0.getLogger(),
                                 446,
                                 null
                              );

                              try {
                                 `$i$f$fromJsonObject` = Result.Companion;
                                 this.L$0 = analyzeUrl;
                                 this.label = 1;
                                 var10000 = AnalyzeUrl.getStrResponseAwait$default(analyzeUrl, null, null, false, this, 7, null);
                              } catch (var16: java.lang.Throwable) {
                                 var35 = Result.Companion;
                                 `$i$f$fromJsonObject` = Result.constructor-impl(ResultKt.createFailure(var16));
                                 break label59;
                              }

                              if (var10000 === var13) {
                                 return var13;
                              }
                              break;
                           case 1:
                              analyzeUrl = this.L$0 as AnalyzeUrl;

                              try {
                                 ResultKt.throwOnFailure(`$result`);
                                 var10000 = `$result`;
                                 break;
                              } catch (var17: java.lang.Throwable) {
                                 val it: Companion = Result.Companion;
                                 `$i$f$fromJsonObject` = Result.constructor-impl(ResultKt.createFailure(var17));
                                 break label59;
                              }
                           default:
                              throw new IllegalStateException("call to 'resume' before 'invoke' with coroutine");
                        }

                        try {
                           `$i$f$fromJsonObject` = Result.constructor-impl(var10000 as StrResponse);
                        } catch (var14: java.lang.Throwable) {
                           val var38: Companion = Result.Companion;
                           `$i$f$fromJsonObject` = Result.constructor-impl(ResultKt.createFailure(var14));
                        }
                     }

                     val var52: java.lang.Throwable = Result.exceptionOrNull-impl(`$i$f$fromJsonObject`);
                     if (var52 != null) {
                        LogUtilsKt.printOnDebug(var52);
                     }

                     val var34: java.lang.Throwable = Result.exceptionOrNull-impl(`$i$f$fromJsonObject`);
                     return if (var34 == null) `$i$f$fromJsonObject` else new StrResponse(analyzeUrl.getUrl(), var34.getLocalizedMessage());
                  }

                  @NotNull
                  @Override
                  public final Continuation<Unit> create(@Nullable Object value, @NotNull Continuation<?> $completion) {
                     return new <anonymous constructor>(this.$header, this.this$0, this.$urlStr, `$completion`);
                  }

                  @Nullable
                  public final Object invoke(@NotNull CoroutineScope p1, @Nullable Continuation<? super StrResponse> p2) {
                     return (this.create(p1, p2) as <unrepresentable>).invokeSuspend(Unit.INSTANCE);
                  }
               }
            ) as Function2,
            1,
            null
         ) as StrResponse;
      }

      @JvmStatic
      fun webView(`this`: JsExtensions, html: java.lang.String?, url: java.lang.String?, js: java.lang.String?): java.lang.String? {
         return null;
      }

      @JvmStatic
      fun importScript(`this`: JsExtensions, path: java.lang.String): java.lang.String {
         val var10000: java.lang.String;
         if (StringsKt.startsWith$default(path, "http", false, 2, null)) {
            val var3: java.lang.String = this.cacheFile(path);
            var10000 = if (var3 == null) "" else var3;
         } else {
            var10000 = if (StringsKt.startsWith$default(path, "/storage", false, 2, null))
               FileUtils.readText$default(FileUtils.INSTANCE, path, null, 2, null)
               else
               this.readTxtFile(path);
         }

         if (StringsKt.isBlank(var10000)) {
            throw new NoStackTraceException(Intrinsics.stringPlus(path, " 内容获取失败或者为空"));
         } else {
            return var10000;
         }
      }

      @JvmStatic
      fun cacheFile(`this`: JsExtensions, urlStr: java.lang.String): java.lang.String? {
         return this.cacheFile(urlStr, 0);
      }

      @JvmStatic
      fun cacheFile(`this`: JsExtensions, urlStr: java.lang.String, saveTime: Int): java.lang.String? {
         val key: java.lang.String = this.md5Encode16(urlStr);
         val cacheInstance: CacheManager = new CacheManager(this.getUserNameSpace());
         val cache: java.lang.String = cacheInstance.getFile(key);
         if (cache == null || StringsKt.isBlank(cache)) {
            this.log(Intrinsics.stringPlus("首次下载 ", urlStr));
            val var9: java.lang.String = this.ajax(urlStr);
            if (var9 == null) {
               return null;
            } else {
               cacheInstance.putFile(key, var9, saveTime);
               return var9;
            }
         } else {
            return cache;
         }
      }

      @JvmStatic
      fun getCookie(`this`: JsExtensions, tag: java.lang.String, key: java.lang.String?): java.lang.String {
         val cookieStore: CookieStore = new CookieStore(this.getUserNameSpace());
         val cookie: java.lang.String = cookieStore.getCookie(tag);
         val cookieMap: java.util.Map = cookieStore.cookieToMap(cookie);
         val var10000: java.lang.String;
         if (key != null) {
            val var6: java.lang.String = cookieMap.get(key) as java.lang.String;
            var10000 = if (var6 == null) "" else var6;
         } else {
            var10000 = cookie;
         }

         return var10000;
      }

      @JvmStatic
      fun downloadFile(`this`: JsExtensions, content: java.lang.String, url: java.lang.String): java.lang.String {
         var zipPath: java.lang.String = new AnalyzeUrl(url, null, null, null, null, null, null, null, null, null, null, 2046, null).getType();
         if (zipPath == null) {
            return "";
         } else {
            zipPath = FileUtils.INSTANCE
               .getPath(FileUtils.INSTANCE.createFolderIfNotExist(FileUtils.INSTANCE.getCachePath()), "${MD5Utils.INSTANCE.md5Encode16(url)}.$zipPath");
            FileUtils.INSTANCE.deleteFile(zipPath);
            val var16: File = FileUtils.INSTANCE.createFileIfNotExist(zipPath);
            val var6: ByteArray = StringUtils.INSTANCE.hexStringToByte(content);
            if (var6.length != 0) {
               FilesKt.writeBytes(var16, var6);
            }

            val var17: Int = FileUtils.INSTANCE.getCachePath().length();
            if (zipPath == null) {
               throw new NullPointerException("null cannot be cast to non-null type java.lang.String");
            } else {
               val var19: java.lang.String = zipPath.substring(var17);
               return var19;
            }
         }
      }

      @JvmStatic
      fun get(`this`: JsExtensions, urlStr: java.lang.String, headers: MutableMap<java.lang.String, java.lang.String>): Response {
         val response: Response = Jsoup.connect(urlStr)
            .sslSocketFactory(SSLHelper.INSTANCE.getUnsafeSSLSocketFactory())
            .ignoreContentType(true)
            .followRedirects(false)
            .headers(headers)
            .method(Method.GET)
            .execute();
         val cookies: java.util.Map = response.cookies();
         val cookieStore: CookieStore = new CookieStore(this.getUserNameSpace());
         val var6: java.lang.String = cookieStore.mapToCookie(cookies);
         if (var6 != null) {
            cookieStore.replaceCookie(Intrinsics.stringPlus(NetworkUtils.INSTANCE.getSubDomain(urlStr), "_cookieJar"), var6);
         }

         return response;
      }

      @JvmStatic
      fun head(`this`: JsExtensions, urlStr: java.lang.String, headers: MutableMap<java.lang.String, java.lang.String>): Response {
         val response: Response = Jsoup.connect(urlStr)
            .sslSocketFactory(SSLHelper.INSTANCE.getUnsafeSSLSocketFactory())
            .ignoreContentType(true)
            .followRedirects(false)
            .headers(headers)
            .method(Method.HEAD)
            .execute();
         val cookies: java.util.Map = response.cookies();
         val cookieStore: CookieStore = new CookieStore(this.getUserNameSpace());
         val var6: java.lang.String = cookieStore.mapToCookie(cookies);
         if (var6 != null) {
            cookieStore.replaceCookie(Intrinsics.stringPlus(NetworkUtils.INSTANCE.getSubDomain(urlStr), "_cookieJar"), var6);
         }

         return response;
      }

      @JvmStatic
      fun post(`this`: JsExtensions, urlStr: java.lang.String, body: java.lang.String, headers: MutableMap<java.lang.String, java.lang.String>): Response {
         val response: Response = Jsoup.connect(urlStr)
            .sslSocketFactory(SSLHelper.INSTANCE.getUnsafeSSLSocketFactory())
            .ignoreContentType(true)
            .followRedirects(false)
            .requestBody(body)
            .headers(headers)
            .method(Method.POST)
            .execute();
         val cookies: java.util.Map = response.cookies();
         val cookieStore: CookieStore = new CookieStore(this.getUserNameSpace());
         val var7: java.lang.String = cookieStore.mapToCookie(cookies);
         if (var7 != null) {
            cookieStore.replaceCookie(Intrinsics.stringPlus(NetworkUtils.INSTANCE.getSubDomain(urlStr), "_cookieJar"), var7);
         }

         return response;
      }

      @JvmStatic
      fun base64Decode(`this`: JsExtensions, str: java.lang.String): java.lang.String {
         return EncoderUtils.INSTANCE.base64Decode(str, 2);
      }

      @JvmStatic
      fun base64Decode(`this`: JsExtensions, str: java.lang.String, flags: Int): java.lang.String {
         return EncoderUtils.INSTANCE.base64Decode(str, flags);
      }

      @JvmStatic
      fun base64DecodeToByteArray(`this`: JsExtensions, str: java.lang.String?): ByteArray? {
         return if (str as java.lang.CharSequence == null || StringsKt.isBlank(str)) null else Base64.decode(str, 0);
      }

      @JvmStatic
      fun base64DecodeToByteArray(`this`: JsExtensions, str: java.lang.String?, flags: Int): ByteArray? {
         return if (str as java.lang.CharSequence == null || StringsKt.isBlank(str)) null else Base64.decode(str, flags);
      }

      @JvmStatic
      fun base64Encode(`this`: JsExtensions, str: java.lang.String): java.lang.String? {
         return EncoderUtils.INSTANCE.base64Encode(str, 2);
      }

      @JvmStatic
      fun base64Encode(`this`: JsExtensions, str: java.lang.String, flags: Int): java.lang.String? {
         return EncoderUtils.INSTANCE.base64Encode(str, flags);
      }

      @JvmStatic
      fun md5Encode(`this`: JsExtensions, str: java.lang.String): java.lang.String {
         return MD5Utils.INSTANCE.md5Encode(str);
      }

      @JvmStatic
      fun md5Encode16(`this`: JsExtensions, str: java.lang.String): java.lang.String {
         return MD5Utils.INSTANCE.md5Encode16(str);
      }

      @JvmStatic
      fun timeFormatUTC(`this`: JsExtensions, time: Long, format: java.lang.String, sh: Int): java.lang.String? {
         val utc: SimpleTimeZone = new SimpleTimeZone(sh, "UTC");
         val var6: SimpleDateFormat = new SimpleDateFormat(format, Locale.getDefault());
         var6.setTimeZone(utc);
         return var6.format(new Date(time));
      }

      @JvmStatic
      fun timeFormat(`this`: JsExtensions, time: Long): java.lang.String {
         val var3: java.lang.String = AppConst.INSTANCE.getDateFormat().format(new Date(time));
         return var3;
      }

      @JvmStatic
      fun utf8ToGbk(`this`: JsExtensions, str: java.lang.String): java.lang.String {
         var var10000: Charset = Charset.forName("UTF-8");
         val var23: ByteArray = str.getBytes(var10000);
         val var24: ByteArray = new java.lang.String(var23, Charsets.UTF_8).getBytes(Charsets.UTF_8);
         var10000 = Charset.forName("UTF-8");
         val var7: java.lang.String = new java.lang.String(var24, var10000);
         var10000 = Charset.forName("GBK");
         val var27: ByteArray = var7.getBytes(var10000);
         return new java.lang.String(var27, Charsets.UTF_8);
      }

      @JvmStatic
      fun encodeURI(`this`: JsExtensions, str: java.lang.String): java.lang.String {
         var var2: java.lang.String;
         try {
            var2 = URLEncoder.encode(str, "UTF-8");
            var2 = var2;
         } catch (var4: Exception) {
            var2 = "";
         }

         return var2;
      }

      @JvmStatic
      fun encodeURI(`this`: JsExtensions, str: java.lang.String, enc: java.lang.String): java.lang.String {
         var var3: java.lang.String;
         try {
            var3 = URLEncoder.encode(str, enc);
            var3 = var3;
         } catch (var5: Exception) {
            var3 = "";
         }

         return var3;
      }

      @JvmStatic
      fun htmlFormat(`this`: JsExtensions, str: java.lang.String): java.lang.String {
         return HtmlFormatter.formatKeepImg$default(HtmlFormatter.INSTANCE, str, null, 2, null);
      }

      @JvmStatic
      fun getFile(`this`: JsExtensions, path: java.lang.String): File {
         val cachePath: java.lang.String = ReaderAdapterHelper.INSTANCE.getAdapter().getCacheDir();
         val var4: java.lang.String = File.separator;
         return new File(
            if (StringsKt.startsWith$default(path, var4, false, 2, null)) Intrinsics.stringPlus(cachePath, path) else "$cachePath${File.separator}$path"
         );
      }

      @JvmStatic
      fun readFile(`this`: JsExtensions, path: java.lang.String): ByteArray? {
         val file: File = this.getFile(path);
         return if (file.exists()) FilesKt.readBytes(file) else null;
      }

      @JvmStatic
      fun readTxtFile(`this`: JsExtensions, path: java.lang.String): java.lang.String {
         val file: File = this.getFile(path);
         if (file.exists()) {
            val charsetName: java.lang.String = EncodingDetect.INSTANCE.getEncode(file);
            val var4: ByteArray = FilesKt.readBytes(file);
            val var10000: Charset = Charset.forName(charsetName);
            return new java.lang.String(var4, var10000);
         } else {
            return "";
         }
      }

      @JvmStatic
      fun readTxtFile(`this`: JsExtensions, path: java.lang.String, charsetName: java.lang.String): java.lang.String {
         val file: File = this.getFile(path);
         if (file.exists()) {
            val var4: ByteArray = FilesKt.readBytes(file);
            val var10000: Charset = Charset.forName(charsetName);
            return new java.lang.String(var4, var10000);
         } else {
            return "";
         }
      }

      @JvmStatic
      fun deleteFile(`this`: JsExtensions, path: java.lang.String) {
         FileUtils.INSTANCE.delete(this.getFile(path), true);
      }

      @JvmStatic
      fun unzipFile(`this`: JsExtensions, zipPath: java.lang.String): java.lang.String {
         if (zipPath.length() == 0) {
            return "";
         } else {
            val var8: java.lang.String = FileUtils.INSTANCE
               .getPath(FileUtils.INSTANCE.createFolderIfNotExist(FileUtils.INSTANCE.getCachePath()), FileUtils.INSTANCE.getNameExcludeExtension(zipPath));
            FileUtils.INSTANCE.deleteFile(var8);
            val var10: File = this.getFile(zipPath);
            ZipUtils.INSTANCE.unzipFile(var10, FileUtils.INSTANCE.createFolderIfNotExist(var8));
            val var11: FileUtils = FileUtils.INSTANCE;
            val var5: java.lang.String = var10.getAbsolutePath();
            var11.deleteFile(var5);
            val var6: Int = FileUtils.INSTANCE.getCachePath().length();
            if (var8 == null) {
               throw new NullPointerException("null cannot be cast to non-null type java.lang.String");
            } else {
               val var12: java.lang.String = var8.substring(var6);
               return var12;
            }
         }
      }

      @JvmStatic
      fun getTxtInFolder(`this`: JsExtensions, unzipPath: java.lang.String): java.lang.String {
         if (unzipPath.length() == 0) {
            return "";
         } else {
            val var17: File = this.getFile(unzipPath);
            val var18: StringBuilder = new StringBuilder();
            val var4: Array<File> = var17.listFiles();
            if (var4 != null) {
               val var9: Array<File> = var4;
               var var10: Int = 0;
               val var11: Int = var4.length;

               while (var10 < var11) {
                  val f: File = var9[var10];
                  var10++;
                  val var10000: EncodingDetect = EncodingDetect.INSTANCE;
                  val charsetName: java.lang.String = var10000.getEncode(f);
                  val var14: ByteArray = FilesKt.readBytes(f);
                  val var10001: Charset = Charset.forName(charsetName);
                  var18.append(new java.lang.String(var14, var10001)).append("\n");
               }

               var18.deleteCharAt(var18.length() - 1);
            }

            val var22: FileUtils = FileUtils.INSTANCE;
            val var19: java.lang.String = var17.getAbsolutePath();
            var22.deleteFile(var19);
            val var20: java.lang.String = var18.toString();
            return var20;
         }
      }

      @JvmStatic
      fun getZipStringContent(`this`: JsExtensions, url: java.lang.String, path: java.lang.String): java.lang.String {
         val charsetName: ByteArray = this.getZipByteArrayContent(url, path);
         if (charsetName == null) {
            return "";
         } else {
            val var5: Charset = Charset.forName(EncodingDetect.INSTANCE.getEncode(charsetName));
            return new java.lang.String(charsetName, var5);
         }
      }

      @JvmStatic
      fun getZipStringContent(`this`: JsExtensions, url: java.lang.String, path: java.lang.String, charsetName: java.lang.String): java.lang.String {
         val var5: ByteArray = this.getZipByteArrayContent(url, path);
         if (var5 == null) {
            return "";
         } else {
            val var7: Charset = Charset.forName(charsetName);
            return new java.lang.String(var5, var7);
         }
      }

      @JvmStatic
      fun getZipByteArrayContent(`this`: JsExtensions, url: java.lang.String, path: java.lang.String): ByteArray? {
         label66: {
            val bytes: ByteArray = if (!StringsKt.startsWith$default(url, "http://", false, 2, null)
                  && !StringsKt.startsWith$default(url, "https://", false, 2, null))
               StringUtils.INSTANCE.hexStringToByte(url)
               else
               BuildersKt.runBlocking$default(null, (new Function2<CoroutineScope, Continuation<byte[]>, Object>(url, null) {
                  int label;

                  {
                     super(2, `$completionx`);
                     this.$url = `$url`;
                  }

                  @Nullable
                  @Override
                  public final Object invokeSuspend(@NotNull Object $result) {
                     val var2: Any = IntrinsicsKt.getCOROUTINE_SUSPENDED();
                     var var10000: Any;
                     switch (this.label) {
                        case 0:
                           ResultKt.throwOnFailure(`$result`);
                           var10000 = HttpHelperKt.getOkHttpClient();
                           val var10002: Function1 = (new Function1<Builder, Unit>(this.$url) {
                              {
                                 super(1);
                                 this.$url = `$url`;
                              }

                              public final void invoke(@NotNull Builder $this$newCall) {
                                 `$this$newCall`.url(this.$url);
                              }
                           }) as Function1;
                           val var10003: Continuation = this;
                           this.label = 1;
                           var10000 = OkHttpUtilsKt.newCall$default((OkHttpClient)var10000, 0, var10002, var10003, 1, null);
                           if (var10000 === var2) {
                              return var2;
                           }
                           break;
                        case 1:
                           ResultKt.throwOnFailure(`$result`);
                           var10000 = `$result`;
                           break;
                        default:
                           throw new IllegalStateException("call to 'resume' before 'invoke' with coroutine");
                     }

                     return (var10000 as ResponseBody).bytes();
                  }

                  @NotNull
                  @Override
                  public final Continuation<Unit> create(@Nullable Object value, @NotNull Continuation<?> $completion) {
                     return new <anonymous constructor>(this.$url, `$completion`);
                  }

                  @Nullable
                  public final Object invoke(@NotNull CoroutineScope p1, @Nullable Continuation<byte[]> p2) {
                     return (this.create(p1, p2) as <unrepresentable>).invokeSuspend(Unit.INSTANCE);
                  }
               }) as Function2, 1, null) as ByteArray;
            val bos: ByteArrayOutputStream = new ByteArrayOutputStream();
            val zis: ZipInputStream = new ZipInputStream(new ByteArrayInputStream(bytes));

            for (ZipEntry entry = zis.getNextEntry(); entry != null; entry = zis.getNextEntry()) {
               if (entry.getName().equals(path)) {
                  val var7: Closeable = zis;
                  var var17: java.lang.Throwable = null as java.lang.Throwable;

                  try {
                     try {
                        val var18: Long = ByteStreamsKt.copyTo$default(var7 as ZipInputStream, bos, 0, 2, null);
                     } catch (var13: java.lang.Throwable) {
                        var17 = var13;
                        throw var13;
                     }
                  } catch (var14: java.lang.Throwable) {
                     CloseableKt.closeFinally(var7, var17);
                  }

                  CloseableKt.closeFinally(var7, null as java.lang.Throwable);
               }
            }

            Debug.INSTANCE.log("getZipContent 未发现内容");
            return null;
         }
      }

      @JvmStatic
      fun queryBase64TTF(`this`: JsExtensions, base64: java.lang.String?): QueryTTF? {
         val var2: ByteArray = this.base64DecodeToByteArray(base64);
         return if (var2 == null) null else new QueryTTF(var2);
      }

      @JvmStatic
      fun queryTTF(`this`: JsExtensions, str: java.lang.String?): QueryTTF? {
         if (str == null) {
            return null;
         } else {
            val key: java.lang.String = this.md5Encode16(str);
            val cacheInstance: ObjectRef = new ObjectRef();
            cacheInstance.element = (T)(new CacheManager(this.getUserNameSpace()));
            var qTTF: QueryTTF = (cacheInstance.element as CacheManager).getQueryTTF(key);
            if (qTTF != null) {
               return qTTF;
            } else {
               val font: ByteArray = if (StringExtensionsKt.isAbsUrl(str))
                  BuildersKt.runBlocking$default(null, (new Function2<CoroutineScope, Continuation<byte[]>, Object>(cacheInstance, key, str, null) {
                     int label;

                     {
                        super(2, `$completionx`);
                        this.$cacheInstance = `$cacheInstance`;
                        this.$key = `$key`;
                        this.$str = `$str`;
                     }

                     @Nullable
                     @Override
                     public final Object invokeSuspend(@NotNull Object $result) {
                        val var10: Any = IntrinsicsKt.getCOROUTINE_SUSPENDED();
                        var var10000: Any;
                        switch (this.label) {
                           case 0:
                              ResultKt.throwOnFailure(`$result`);
                              val x: ByteArray = this.$cacheInstance.element.getByteArray(this.$key);
                              if (x != null) {
                                 return x;
                              }

                              var10000 = HttpHelperKt.getOkHttpClient();
                              val var10002: Function1 = (new Function1<Builder, Unit>(this.$str) {
                                 {
                                    super(1);
                                    this.$str = `$str`;
                                 }

                                 public final void invoke(@NotNull Builder $this$newCall) {
                                    `$this$newCall`.url(this.$str);
                                 }
                              }) as Function1;
                              val var10003: Continuation = this;
                              this.label = 1;
                              var10000 = OkHttpUtilsKt.newCall$default((OkHttpClient)var10000, 0, var10002, var10003, 1, null);
                              if (var10000 === var10) {
                                 return var10;
                              }
                              break;
                           case 1:
                              ResultKt.throwOnFailure(`$result`);
                              var10000 = `$result`;
                              break;
                           default:
                              throw new IllegalStateException("call to 'resume' before 'invoke' with coroutine");
                        }

                        val xx: ByteArray = (var10000 as ResponseBody).bytes();
                        CacheManager.put$default(this.$cacheInstance.element, this.$key, xx, 0, 4, null);
                        return xx;
                     }

                     @NotNull
                     @Override
                     public final Continuation<Unit> create(@Nullable Object value, @NotNull Continuation<?> $completion) {
                        return new <anonymous constructor>(this.$cacheInstance, this.$key, this.$str, `$completion`);
                     }

                     @Nullable
                     public final Object invoke(@NotNull CoroutineScope p1, @Nullable Continuation<byte[]> p2) {
                        return (this.create(p1, p2) as <unrepresentable>).invokeSuspend(Unit.INSTANCE);
                     }
                  }) as Function2, 1, null) as ByteArray
                  else
                  (
                     if (StringsKt.indexOf$default(str, "storage/", 0, false, 6, null) > 0)
                        FilesKt.readBytes(new File(str))
                        else
                        this.base64DecodeToByteArray(str)
                  );
               if (font == null) {
                  return null;
               } else {
                  qTTF = new QueryTTF(font);
                  CacheManager.put$default(cacheInstance.element as CacheManager, key, qTTF, 0, 4, null);
                  return qTTF;
               }
            }
         }
      }

      @JvmStatic
      fun replaceFont(`this`: JsExtensions, text: java.lang.String, font1: QueryTTF?, font2: QueryTTF?): java.lang.String {
         if (font1 != null && font2 != null) {
            val var10000: CharArray = text.toCharArray();
            val contentArray: CharArray = var10000;
            var `index$iv`: Int = 0;
            val var8: CharArray = var10000;
            val var9: Int = var10000.length;

            for (int var10 = 0; var10 < var9; var10++) {
               val `item$iv`: Char = var8[var10];
               val index: Int = `index$iv`++;
               if (font1.inLimit(`item$iv`)) {
                  val code: Int = font2.getCodeByGlyf(font1.getGlyfByCode(`item$iv`));
                  if (code != 0) {
                     contentArray[index] = (char)code;
                  }
               }
            }

            return ArraysKt.joinToString$default(contentArray, "", null, null, 0, null, null, 62, null);
         } else {
            return text;
         }
      }

      @JvmStatic
      fun toast(`this`: JsExtensions, msg: Any?) {
         val var2: DebugLog = this.getLogger();
         if (var2 != null) {
            var2.log(Intrinsics.stringPlus("toast: ", msg));
         }

         Debug.INSTANCE.log(Intrinsics.stringPlus("toast: ", msg));
      }

      @JvmStatic
      fun longToast(`this`: JsExtensions, msg: Any?) {
         val var2: DebugLog = this.getLogger();
         if (var2 != null) {
            var2.log(Intrinsics.stringPlus("longToast: ", msg));
         }

         Debug.INSTANCE.log(Intrinsics.stringPlus("longToast: ", msg));
      }

      @JvmStatic
      fun log(`this`: JsExtensions, msg: java.lang.String): java.lang.String {
         val var2: DebugLog = this.getLogger();
         if (var2 != null) {
            var2.log(msg);
         }

         Debug.INSTANCE.log(msg);
         return msg;
      }

      @JvmStatic
      fun logType(`this`: JsExtensions, any: Any?) {
         if (any == null) {
            this.log("null");
         } else {
            val var2: java.lang.String = any.getClass().getName();
            this.log(var2);
         }
      }

      @JvmStatic
      fun randomUUID(`this`: JsExtensions): java.lang.String {
         val var1: java.lang.String = UUID.randomUUID().toString();
         return var1;
      }

      @JvmStatic
      fun aesDecodeToByteArray(`this`: JsExtensions, str: java.lang.String, key: java.lang.String, transformation: java.lang.String, iv: java.lang.String): ByteArray? {
         var var5: ByteArray;
         try {
            var5 = EncoderUtils.INSTANCE
               .decryptAES(StringsKt.encodeToByteArray(str), StringsKt.encodeToByteArray(key), transformation, StringsKt.encodeToByteArray(iv));
         } catch (var8: Exception) {
            LogUtilsKt.printOnDebug(var8);
            val var7: java.lang.String = var8.getLocalizedMessage();
            this.log(if (var7 == null) "aesDecodeToByteArrayERROR" else var7);
            var5 = null as ByteArray;
         }

         return var5;
      }

      @JvmStatic
      fun aesDecodeToString(`this`: JsExtensions, str: java.lang.String, key: java.lang.String, transformation: java.lang.String, iv: java.lang.String): java.lang.String? {
         val var5: ByteArray = this.aesDecodeToByteArray(str, key, transformation, iv);
         return if (var5 == null) null else new java.lang.String(var5, Charsets.UTF_8);
      }

      @JvmStatic
      fun aesBase64DecodeToByteArray(`this`: JsExtensions, str: java.lang.String, key: java.lang.String, transformation: java.lang.String, iv: java.lang.String): ByteArray? {
         var var5: ByteArray;
         try {
            var5 = EncoderUtils.INSTANCE
               .decryptBase64AES(StringsKt.encodeToByteArray(str), StringsKt.encodeToByteArray(key), transformation, StringsKt.encodeToByteArray(iv));
         } catch (var8: Exception) {
            LogUtilsKt.printOnDebug(var8);
            val var7: java.lang.String = var8.getLocalizedMessage();
            this.log(if (var7 == null) "aesDecodeToByteArrayERROR" else var7);
            var5 = null as ByteArray;
         }

         return var5;
      }

      @JvmStatic
      fun aesBase64DecodeToString(`this`: JsExtensions, str: java.lang.String, key: java.lang.String, transformation: java.lang.String, iv: java.lang.String): java.lang.String? {
         val var5: ByteArray = this.aesBase64DecodeToByteArray(str, key, transformation, iv);
         return if (var5 == null) null else new java.lang.String(var5, Charsets.UTF_8);
      }

      @JvmStatic
      fun aesEncodeToByteArray(`this`: JsExtensions, data: java.lang.String, key: java.lang.String, transformation: java.lang.String, iv: java.lang.String): ByteArray? {
         var var5: ByteArray;
         try {
            var5 = EncoderUtils.INSTANCE
               .encryptAES(StringsKt.encodeToByteArray(data), StringsKt.encodeToByteArray(key), transformation, StringsKt.encodeToByteArray(iv));
         } catch (var8: Exception) {
            LogUtilsKt.printOnDebug(var8);
            val var7: java.lang.String = var8.getLocalizedMessage();
            this.log(if (var7 == null) "aesEncodeToByteArrayERROR" else var7);
            var5 = null as ByteArray;
         }

         return var5;
      }

      @JvmStatic
      fun aesEncodeToString(`this`: JsExtensions, data: java.lang.String, key: java.lang.String, transformation: java.lang.String, iv: java.lang.String): java.lang.String? {
         val var5: ByteArray = this.aesEncodeToByteArray(data, key, transformation, iv);
         return if (var5 == null) null else new java.lang.String(var5, Charsets.UTF_8);
      }

      @JvmStatic
      fun aesEncodeToBase64ByteArray(
         `this`: JsExtensions, data: java.lang.String, key: java.lang.String, transformation: java.lang.String, iv: java.lang.String
      ): ByteArray? {
         var var5: ByteArray;
         try {
            var5 = EncoderUtils.INSTANCE
               .encryptAES2Base64(StringsKt.encodeToByteArray(data), StringsKt.encodeToByteArray(key), transformation, StringsKt.encodeToByteArray(iv));
         } catch (var8: Exception) {
            LogUtilsKt.printOnDebug(var8);
            val var7: java.lang.String = var8.getLocalizedMessage();
            this.log(if (var7 == null) "aesEncodeToBase64ByteArrayERROR" else var7);
            var5 = null as ByteArray;
         }

         return var5;
      }

      @JvmStatic
      fun aesEncodeToBase64String(`this`: JsExtensions, data: java.lang.String, key: java.lang.String, transformation: java.lang.String, iv: java.lang.String): java.lang.String? {
         val var5: ByteArray = this.aesEncodeToBase64ByteArray(data, key, transformation, iv);
         return if (var5 == null) null else new java.lang.String(var5, Charsets.UTF_8);
      }

      @JvmStatic
      fun androidId(`this`: JsExtensions): java.lang.String {
         return "";
      }

      @JvmStatic
      fun aesDecodeArgsBase64Str(
         `this`: JsExtensions, data: java.lang.String, key: java.lang.String, mode: java.lang.String, padding: java.lang.String, iv: java.lang.String
      ): java.lang.String? {
         return new AES(mode, padding, Base64.decode(key, 2), Base64.decode(iv, 2)).decryptStr(data);
      }

      @JvmStatic
      fun tripleDESDecodeStr(
         `this`: JsExtensions, data: java.lang.String, key: java.lang.String, mode: java.lang.String, padding: java.lang.String, iv: java.lang.String
      ): java.lang.String? {
         val var10004: ByteArray = key.getBytes(Charsets.UTF_8);
         val var10005: ByteArray = iv.getBytes(Charsets.UTF_8);
         return new DESede(mode, padding, var10004, var10005).decryptStr(data);
      }

      @JvmStatic
      fun tripleDESDecodeArgsBase64Str(
         `this`: JsExtensions, data: java.lang.String, key: java.lang.String, mode: java.lang.String, padding: java.lang.String, iv: java.lang.String
      ): java.lang.String? {
         return new DESede(mode, padding, Base64.decode(key, 2), Base64.decode(iv, 2)).decryptStr(data);
      }

      @JvmStatic
      fun aesEncodeArgsBase64Str(
         `this`: JsExtensions, data: java.lang.String, key: java.lang.String, mode: java.lang.String, padding: java.lang.String, iv: java.lang.String
      ): java.lang.String? {
         return new AES(mode, padding, Base64.decode(key, 2), Base64.decode(iv, 2)).encryptBase64(data);
      }

      @JvmStatic
      fun desDecodeToString(`this`: JsExtensions, data: java.lang.String, key: java.lang.String, transformation: java.lang.String, iv: java.lang.String): java.lang.String? {
         val var5: ByteArray = EncoderUtils.INSTANCE
            .decryptDES(StringsKt.encodeToByteArray(data), StringsKt.encodeToByteArray(key), transformation, StringsKt.encodeToByteArray(iv));
         return if (var5 == null) null else new java.lang.String(var5, Charsets.UTF_8);
      }

      @JvmStatic
      fun desBase64DecodeToString(`this`: JsExtensions, data: java.lang.String, key: java.lang.String, transformation: java.lang.String, iv: java.lang.String): java.lang.String? {
         val var5: ByteArray = EncoderUtils.INSTANCE
            .decryptBase64DES(StringsKt.encodeToByteArray(data), StringsKt.encodeToByteArray(key), transformation, StringsKt.encodeToByteArray(iv));
         return if (var5 == null) null else new java.lang.String(var5, Charsets.UTF_8);
      }

      @JvmStatic
      fun desEncodeToString(`this`: JsExtensions, data: java.lang.String, key: java.lang.String, transformation: java.lang.String, iv: java.lang.String): java.lang.String? {
         val var5: ByteArray = EncoderUtils.INSTANCE
            .encryptDES(StringsKt.encodeToByteArray(data), StringsKt.encodeToByteArray(key), transformation, StringsKt.encodeToByteArray(iv));
         return if (var5 == null) null else new java.lang.String(var5, Charsets.UTF_8);
      }

      @JvmStatic
      fun desEncodeToBase64String(`this`: JsExtensions, data: java.lang.String, key: java.lang.String, transformation: java.lang.String, iv: java.lang.String): java.lang.String? {
         val var5: ByteArray = EncoderUtils.INSTANCE
            .encryptDES2Base64(StringsKt.encodeToByteArray(data), StringsKt.encodeToByteArray(key), transformation, StringsKt.encodeToByteArray(iv));
         return if (var5 == null) null else new java.lang.String(var5, Charsets.UTF_8);
      }

      @JvmStatic
      fun tripleDESEncodeBase64Str(
         `this`: JsExtensions, data: java.lang.String, key: java.lang.String, mode: java.lang.String, padding: java.lang.String, iv: java.lang.String
      ): java.lang.String? {
         val var10004: ByteArray = key.getBytes(Charsets.UTF_8);
         val var10005: ByteArray = iv.getBytes(Charsets.UTF_8);
         return new DESede(mode, padding, var10004, var10005).encryptBase64(data);
      }

      @JvmStatic
      fun tripleDESEncodeArgsBase64Str(
         `this`: JsExtensions, data: java.lang.String, key: java.lang.String, mode: java.lang.String, padding: java.lang.String, iv: java.lang.String
      ): java.lang.String? {
         return new DESede(mode, padding, Base64.decode(key, 2), Base64.decode(iv, 2)).encryptBase64(data);
      }

      @JvmStatic
      fun digestHex(`this`: JsExtensions, data: java.lang.String, algorithm: java.lang.String): java.lang.String? {
         return DigestUtil.digester(algorithm).digestHex(data);
      }

      @JvmStatic
      fun digestBase64Str(`this`: JsExtensions, data: java.lang.String, algorithm: java.lang.String): java.lang.String? {
         return Base64.encodeToString(DigestUtil.digester(algorithm).digest(data), 2);
      }
   }
}
