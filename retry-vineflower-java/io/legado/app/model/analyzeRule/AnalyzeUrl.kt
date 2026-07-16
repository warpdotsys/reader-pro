package io.legado.app.model.analyzeRule

import com.google.gson.Gson
import com.script.SimpleBindings
import io.legado.app.adapters.ReaderAdapterHelper
import io.legado.app.adapters.ReaderAdapterInterface
import io.legado.app.constant.AppConst
import io.legado.app.constant.AppPattern
import io.legado.app.data.entities.BaseSource
import io.legado.app.data.entities.Book
import io.legado.app.data.entities.BookChapter
import io.legado.app.exception.ConcurrentException
import io.legado.app.help.CacheManager
import io.legado.app.help.JsExtensions
import io.legado.app.help.http.CookieStore
import io.legado.app.help.http.HttpHelperKt
import io.legado.app.help.http.OkHttpUtilsKt
import io.legado.app.help.http.RequestMethod
import io.legado.app.help.http.StrResponse
import io.legado.app.model.DebugLog
import io.legado.app.utils.Base64
import io.legado.app.utils.EncoderUtils
import io.legado.app.utils.GsonExtensionsKt
import io.legado.app.utils.NetworkUtils
import io.legado.app.utils.ParameterizedTypeImpl
import io.legado.app.utils.StringExtensionsKt
import io.legado.app.utils.StringUtils
import java.io.File
import java.lang.reflect.Type
import java.net.URLEncoder
import java.util.Arrays
import java.util.HashMap
import java.util.LinkedHashMap
import java.util.Map.Entry
import java.util.regex.Matcher
import java.util.regex.Pattern
import kotlin.coroutines.Continuation
import kotlin.coroutines.intrinsics.IntrinsicsKt
import kotlin.coroutines.jvm.internal.ContinuationImpl
import kotlin.jvm.functions.Function1
import kotlin.jvm.functions.Function2
import kotlin.jvm.internal.Intrinsics
import kotlin.jvm.internal.StringCompanionObject
import kotlinx.coroutines.BuildersKt
import kotlinx.coroutines.CoroutineScope
import okhttp3.MediaType
import okhttp3.OkHttpClient
import okhttp3.RequestBody
import okhttp3.Response
import okhttp3.ResponseBody
import okhttp3.Request.Builder
import org.jetbrains.annotations.NotNull
import org.jetbrains.annotations.Nullable

public class AnalyzeUrl(mUrl: String,
      key: String? = null,
      page: Int? = null,
      speakText: String? = null,
      speakSpeed: Int? = null,
      baseUrl: String = "",
      source: BaseSource? = null,
      ruleData: RuleDataInterface? = null,
      chapter: BookChapter? = null,
      headerMapF: Map<String, String>? = null,
      debugLog: DebugLog? = null
   ) :
   JsExtensions {
   public final var baseUrl: String
      internal set

   public final var body: String?
      private set

   private final val chapter: BookChapter?
   private final var charset: String?

   public final var debugLog: DebugLog?
      internal set

   private final val enabledCookieJar: Boolean
   private final val fieldMap: LinkedHashMap<String, String>
   public final val headerMap: HashMap<String, String>
   public final val key: String?
   public final val mUrl: String
   private final var method: RequestMethod
   public final val page: Int?
   private final var proxy: String?
   private final var queryStr: String?
   private final var retry: Int
   private final val ruleData: RuleDataInterface?

   public final var ruleUrl: String
      private set

   private final val source: BaseSource?
   public final val speakSpeed: Int?
   public final val speakText: String?

   public final var type: String?
      private set

   public final var url: String
      private set

   private final var urlNoQuery: String
   private final var useWebView: Boolean
   private final var webJs: String?

   init {
      this.mUrl = mUrl;
      this.key = key;
      this.page = page;
      this.speakText = speakText;
      this.speakSpeed = speakSpeed;
      this.baseUrl = baseUrl;
      this.source = source;
      this.ruleData = ruleData;
      this.chapter = chapter;
      this.debugLog = debugLog;
      this.ruleUrl = "";
      this.url = "";
      this.headerMap = new HashMap<>();
      this.urlNoQuery = "";
      this.fieldMap = new LinkedHashMap<>();
      this.method = RequestMethod.GET;
      val var10001: Boolean;
      if (this.source == null) {
         var10001 = false;
      } else {
         val var13: java.lang.Boolean = this.source.getEnabledCookieJar();
         var10001 = var13 != null && var13;
      }

      this.enabledCookieJar = var10001;
      if (!StringExtensionsKt.isDataUrl(this.mUrl)) {
         val var19: Matcher = paramPattern.matcher(this.baseUrl);
         if (var19.find()) {
            val var20: java.lang.String = this.baseUrl;
            val var15: Int = var19.start();
            if (var20 == null) {
               throw new NullPointerException("null cannot be cast to non-null type java.lang.String");
            }

            val var25: java.lang.String = var20.substring(0, var15);
            this.baseUrl = var25;
         }

         val var10000: java.util.Map = (java.util.Map)(if (headerMapF == null)
            (if (this.source == null) null else this.source.getHeaderMap(true))
            else
            headerMapF);
         if (var10000 != null) {
            this.getHeaderMap().putAll(var10000);
            if (var10000.containsKey("proxy")) {
               this.proxy = var10000.get("proxy") as java.lang.String;
               this.getHeaderMap().remove("proxy");
            }
         }

         this.initUrl();
      }
   }

   public override fun getUserNameSpace(): String {
      val var10000: java.lang.String;
      if (this.ruleData == null) {
         var10000 = "unknow";
      } else {
         val var2: java.lang.String = this.ruleData.getUserNameSpace();
         var10000 = if (var2 == null) "unknow" else var2;
      }

      return var10000;
   }

   public override fun getSource(): BaseSource? {
      return this.source;
   }

   public override fun getLogger(): DebugLog? {
      return this.debugLog;
   }

   public fun initUrl() {
      this.ruleUrl = this.mUrl;
      this.analyzeJs();
      this.replaceKeyPageJs();
      this.analyzeUrl();
   }

   private fun analyzeJs() {
      var start: Int = 0;

      for (Matcher jsMatcher = AppPattern.INSTANCE.getJS_PATTERN().matcher(this.ruleUrl); jsMatcher.find(); start = jsMatcher.end()) {
         if (jsMatcher.start() > start) {
            val `$this$trim$iv`: java.lang.String = this.ruleUrl;
            val `$i$f$trim`: Int = jsMatcher.start();
            if (`$this$trim$iv` == null) {
               throw new NullPointerException("null cannot be cast to non-null type java.lang.String");
            }

            val var10000: java.lang.String = `$this$trim$iv`.substring(start, `$i$f$trim`);
            val var28: java.lang.CharSequence = var10000;
            var `startIndex$iv$iv`: Int = 0;
            var `endIndex$iv$iv`: Int = var28.length() - 1;
            var `startFound$iv$iv`: Boolean = false;

            while (startIndex$iv$iv <= endIndex$iv$iv) {
               val var36: Boolean = Intrinsics.compare(var28.charAt(if (!`startFound$iv$iv`) `startIndex$iv$iv` else `endIndex$iv$iv`), 32) <= 0;
               if (!`startFound$iv$iv`) {
                  if (!var36) {
                     `startFound$iv$iv` = true;
                  } else {
                     `startIndex$iv$iv`++;
                  }
               } else {
                  if (!var36) {
                     break;
                  }

                  `endIndex$iv$iv`--;
               }
            }

            val var14: java.lang.String = var28.subSequence(`startIndex$iv$iv`, `endIndex$iv$iv` + 1).toString();
            if (var14.length() > 0) {
               this.ruleUrl = StringsKt.replace$default(var14, "@result", this.ruleUrl, false, 4, null);
            }
         }

         val var29: java.lang.String = jsMatcher.group(2);
         val var24: java.lang.String = if (var29 == null) jsMatcher.group(1) else var29;
         val var18: Any = this.evalJS(var24, this.ruleUrl);
         if (var18 == null) {
            throw new NullPointerException("null cannot be cast to non-null type kotlin.String");
         }

         this.ruleUrl = var18 as java.lang.String;
      }

      if (this.ruleUrl.length() > start) {
         if (this.ruleUrl == null) {
            throw new NullPointerException("null cannot be cast to non-null type java.lang.String");
         }

         val var40: java.lang.String = this.ruleUrl.substring(start);
         val var30: java.lang.CharSequence = var40;
         var var32: Int = 0;
         var var33: Int = var30.length() - 1;
         var var34: Boolean = false;

         while (startIndex$iv$iv <= endIndex$iv$iv) {
            val var38: Boolean = Intrinsics.compare(var30.charAt(if (!var34) var32 else var33), 32) <= 0;
            if (!var34) {
               if (!var38) {
                  var34 = true;
               } else {
                  var32++;
               }
            } else {
               if (!var38) {
                  break;
               }

               var33--;
            }
         }

         val var15: java.lang.String = var30.subSequence(var32, var33 + 1).toString();
         if (var15.length() > 0) {
            this.ruleUrl = StringsKt.replace$default(var15, "@result", this.ruleUrl, false, 4, null);
         }
      }
   }

   private fun replaceKeyPageJs() {
      if (StringsKt.contains$default(this.ruleUrl, "{{", false, 2, null) && StringsKt.contains$default(this.ruleUrl, "}}", false, 2, null)) {
         val url: java.lang.String = new RuleAnalyzer(this.ruleUrl, false, 2, null)
            .innerRule("{{", "}}", (new Function1<java.lang.String, java.lang.String>(this) {
               {
                  super(1);
                  this.this$0 = `$receiver`;
               }

               @Nullable
               public final java.lang.String invoke(@NotNull java.lang.String it) {
                  var var3: Any = AnalyzeUrl.evalJS$default(this.this$0, it, null, 2, null);
                  val jsEval: Any = if (var3 == null) "" else var3;
                  val var10000: java.lang.String;
                  if ((if (var3 == null) "" else var3) is java.lang.String) {
                     var10000 = jsEval as java.lang.String;
                  } else if (jsEval is java.lang.Double && (jsEval as java.lang.Number).doubleValue() % 1.0 == 0.0) {
                     var3 = StringCompanionObject.INSTANCE;
                     val var5: Array<Any> = new Object[]{jsEval};
                     var10000 = java.lang.String.format("%.0f", Arrays.copyOf(var5, var5.length));
                  } else {
                     var10000 = jsEval.toString();
                  }

                  return var10000;
               }
            }) as (java.lang.String?) -> java.lang.String);
         if (url.length() > 0) {
            this.ruleUrl = url;
         }
      }

      if (this.page != null) {
         val it: Int = this.page.intValue();
         val matcher: Matcher = pagePattern.matcher(this.getRuleUrl());

         while (matcher.find()) {
            val var10000: java.lang.String = matcher.group(1);
            val pages: java.util.List = StringsKt.split$default(var10000, new java.lang.String[]{","}, false, 0, 6, null);
            val var45: AnalyzeUrl;
            var var46: java.lang.String;
            if (this.getPage() < pages.size()) {
               var46 = this.getRuleUrl();
               val var28: java.lang.String = matcher.group();
               val var33: java.lang.CharSequence = pages.get(this.getPage() - 1) as java.lang.String;
               var var35: Int = 0;
               var var36: Int = var33.length() - 1;
               var var37: Boolean = false;

               while (startIndex$iv$iv <= endIndex$iv$iv) {
                  val var41: Boolean = Intrinsics.compare(var33.charAt(if (!var37) var35 else var36), 32) <= 0;
                  if (!var37) {
                     if (!var41) {
                        var37 = true;
                     } else {
                        var35++;
                     }
                  } else {
                     if (!var41) {
                        break;
                     }

                     var36--;
                  }
               }

               val var43: java.lang.String = var33.subSequence(var35, var36 + 1).toString();
               var45 = this;
               var46 = StringsKt.replace$default(var46, var28, var43, false, 4, null);
            } else {
               var46 = this.getRuleUrl();
               val var26: java.lang.String = matcher.group();
               val `$this$trim$iv$iv`: java.lang.CharSequence = CollectionsKt.last(pages) as java.lang.String;
               var `startIndex$iv$iv`: Int = 0;
               var `endIndex$iv$iv`: Int = `$this$trim$iv$iv`.length() - 1;
               var `startFound$iv$iv`: Boolean = false;

               while (startIndex$iv$iv <= endIndex$iv$iv) {
                  val var39: Boolean = Intrinsics.compare(`$this$trim$iv$iv`.charAt(if (!`startFound$iv$iv`) `startIndex$iv$iv` else `endIndex$iv$iv`), 32)
                     <= 0;
                  if (!`startFound$iv$iv`) {
                     if (!var39) {
                        `startFound$iv$iv` = true;
                     } else {
                        `startIndex$iv$iv`++;
                     }
                  } else {
                     if (!var39) {
                        break;
                     }

                     `endIndex$iv$iv`--;
                  }
               }

               val var22: java.lang.String = `$this$trim$iv$iv`.subSequence(`startIndex$iv$iv`, `endIndex$iv$iv` + 1).toString();
               var45 = this;
               var46 = StringsKt.replace$default(var46, var26, var22, false, 4, null);
            }

            var45.ruleUrl = var46;
         }
      }
   }

   private fun analyzeUrl() {
      val urlMatcher: Matcher = paramPattern.matcher(this.ruleUrl);
      var var10000: java.lang.String;
      if (urlMatcher.find()) {
         val var3: java.lang.String = this.ruleUrl;
         val pos: Int = urlMatcher.start();
         if (var3 == null) {
            throw new NullPointerException("null cannot be cast to non-null type java.lang.String");
         }

         var10000 = var3.substring(0, pos);
      } else {
         var10000 = this.ruleUrl;
      }

      this.url = NetworkUtils.INSTANCE.getAbsoluteURL(this.baseUrl, var10000);
      val var23: java.lang.String = NetworkUtils.INSTANCE.getBaseUrl(this.url);
      if (var23 != null) {
         this.setBaseUrl(var23);
      }

      if (var10000.length() != this.ruleUrl.length()) {
         val var27: Gson = GsonExtensionsKt.getGSON();
         var var30: java.lang.String = this.ruleUrl;
         val var38: Int = urlMatcher.end();
         if (var30 == null) {
            throw new NullPointerException("null cannot be cast to non-null type java.lang.String");
         }

         var10000 = var30.substring(var38);
         var30 = var10000;

         var var49: Any;
         try {
            var49 = Result.Companion;
            val var66: Type = new AnalyzeUrl$analyzeUrl$$inlined$fromJsonObject$1().getType();
            var10000 = var27.fromJson(var30, var66);
            if (var10000 !is AnalyzeUrl.UrlOption) {
               var10000 = null;
            }

            var49 = Result.constructor-impl(var10000 as AnalyzeUrl.UrlOption);
         } catch (var22: java.lang.Throwable) {
            val var10: Result.Companion = Result.Companion;
            var49 = Result.constructor-impl(ResultKt.createFailure(var22));
         }

         val var24: AnalyzeUrl.UrlOption = (if (Result.isFailure-impl(var49)) null else var49) as AnalyzeUrl.UrlOption;
         if (var24 != null) {
            var var57: java.lang.String = var24.getMethod();
            if (var57 != null && StringsKt.equals(var57, "POST", true)) {
               this.method = RequestMethod.POST;
            }

            val var58: java.util.Map = var24.getHeaderMap();
            if (var58 != null) {
               for (Entry element$iv : var58.entrySet()) {
                  this.getHeaderMap().put(java.lang.String.valueOf(`element$iv`.getKey()), java.lang.String.valueOf(`element$iv`.getValue()));
               }
            }

            var57 = var24.getBody();
            if (var57 != null) {
               this.body = var57;
            }

            this.type = var24.getType();
            this.charset = var24.getCharset();
            this.retry = var24.getRetry();
            this.useWebView = var24.useWebView();
            this.webJs = var24.getWebJs();
            var57 = var24.getJs();
            if (var57 != null) {
               val var81: Any = this.evalJS(var57, this.getUrl());
               if (var81 != null) {
                  val entry: java.lang.String = var81.toString();
                  if (entry != null) {
                     this.url = entry;
                  }
               }
            }
         }
      }

      if (this.headerMap.get("User-Agent") == null) {
         val var45: AnalyzeUrl = this;
         this.getHeaderMap().put("User-Agent", AppConst.INSTANCE.getUserAgent());
      }

      this.urlNoQuery = this.url;
      switch (AnalyzeUrl.WhenMappings.$EnumSwitchMapping$0[this.method.ordinal()]) {
         case 1:
            val var36: Int = StringsKt.indexOf$default(this.url, '?', 0, false, 6, null);
            if (var36 != -1) {
               val var47: Int = var36 + 1;
               if (this.url == null) {
                  throw new NullPointerException("null cannot be cast to non-null type java.lang.String");
               }

               var var10001: java.lang.String = this.url.substring(var47);
               this.analyzeFields(var10001);
               if (this.url == null) {
                  throw new NullPointerException("null cannot be cast to non-null type java.lang.String");
               }

               var10001 = this.url.substring(0, var36);
               this.urlNoQuery = var10001;
            }
            break;
         case 2:
            val var35: java.lang.String = this.body;
            if (this.body != null && !StringExtensionsKt.isJson(this.body) && !StringExtensionsKt.isXml(var35)) {
               val var72: java.lang.CharSequence = this.getHeaderMap().get("Content-Type");
               if (var72 == null || var72.length() == 0) {
                  this.analyzeFields(var35);
               }
            }
         default:
      }
   }

   private fun analyzeFields(fieldsTxt: String) {
      this.queryStr = fieldsTxt;
      val queryS: Array<java.lang.String> = StringExtensionsKt.splitNotBlank(fieldsTxt, "&");
      val var13: Array<java.lang.String> = queryS;
      var var4: Int = 0;
      val var5: Int = queryS.length;

      while (var4 < var5) {
         val query: java.lang.String = var13[var4];
         var4++;
         val queryM: Array<java.lang.String> = StringExtensionsKt.splitNotBlank(query, "=");
         val var14: java.lang.String = if (queryM.length > 1) queryM[1] else "";
         if (this.charset == null || this.charset.length() == 0) {
            if (NetworkUtils.INSTANCE.hasUrlEncoded(var14)) {
               this.fieldMap.put(queryM[0], var14);
            } else {
               val var16: java.util.Map = this.fieldMap;
               val var20: java.lang.String = queryM[0];
               val var24: java.lang.String = URLEncoder.encode(var14, "UTF-8");
               var16.put(var20, var24);
            }
         } else if (this.charset == "escape") {
            this.fieldMap.put(queryM[0], EncoderUtils.INSTANCE.escape(var14));
         } else {
            val var18: java.util.Map = this.fieldMap;
            val var22: java.lang.String = queryM[0];
            val var26: java.lang.String = URLEncoder.encode(var14, this.charset);
            var18.put(var22, var26);
         }
      }
   }

   public fun evalJS(jsStr: String, result: Any? = null): Any? {
      val bindings: SimpleBindings = new SimpleBindings();
      bindings.put("java", this);
      bindings.put("baseUrl", this.baseUrl);
      bindings.put("cookie", new CookieStore(this.getUserNameSpace()));
      bindings.put("cache", new CacheManager(this.getUserNameSpace()));
      bindings.put("page", this.page);
      bindings.put("key", this.key);
      bindings.put("speakText", this.speakText);
      bindings.put("speakSpeed", this.speakSpeed);
      bindings.put("book", this.ruleData as? Book);
      bindings.put("source", this.source);
      bindings.put("result", result);
      return AppConst.INSTANCE.getSCRIPT_ENGINE().eval(jsStr, bindings);
   }

   public fun put(key: String, value: String): String {
      val var10000: Unit;
      if (this.chapter == null) {
         var10000 = null;
      } else {
         this.chapter.putVariable(key, value);
         var10000 = Unit.INSTANCE;
      }

      if (var10000 == null) {
         if (this.ruleData != null) {
            this.ruleData.putVariable(key, value);
         }
      }

      return value;
   }

   public fun get(key: String): String {
      if (key == "bookName") {
         val var3: Book = this.ruleData as? Book;
         if ((this.ruleData as? Book) != null) {
            return var3.getName();
         }
      } else if (key == "title" && this.chapter != null) {
         return this.chapter.getTitle();
      }

      val var2: java.lang.String = if (this.chapter == null) null else this.chapter.getVariable(key);
      val var10000: java.lang.String;
      if (var2 == null) {
         if (this.ruleData == null) {
            var10000 = "";
         } else {
            val var12: java.lang.String = this.ruleData.getVariable(key);
            var10000 = if (var12 == null) "" else var12;
         }
      } else {
         var10000 = var2;
      }

      return var10000;
   }

   private fun fetchStart(): io.legado.app.model.analyzeRule.AnalyzeUrl.ConcurrentRecord? {
      if (this.source == null) {
         return null;
      } else {
         val var22: java.lang.String = this.source.getConcurrentRate();
         if (var22 == null || var22.length() == 0) {
            return null;
         } else {
            val var23: Int = StringsKt.indexOf$default(var22, "/", 0, false, 6, null);
            var var25: AnalyzeUrl.ConcurrentRecord = concurrentRecordMap.get(this.source.getKey());
            if (var25 == null) {
               var25 = new AnalyzeUrl.ConcurrentRecord(var23 > 0, System.currentTimeMillis(), 1);
               concurrentRecordMap.put(this.source.getKey(), var25);
               return var25;
            } else {
               val var28: Int;
               synchronized (var25) {
                  var var10000: Int;
                  try {
                     if (var23 == -1) {
                        if (var25.getFrequency() > 0) {
                           var10000 = Integer.parseInt(var22);
                        } else {
                           val nextTime: Long = var25.getTime() + Integer.parseInt(var22);
                           if (System.currentTimeMillis() >= nextTime) {
                              var25.setTime(System.currentTimeMillis());
                              var25.setFrequency(1);
                              var10000 = 0;
                           } else {
                              var10000 = (int)(nextTime - System.currentTimeMillis());
                           }
                        }
                     } else {
                        val var11: Int = var23 + 1;
                        if (var22 == null) {
                           throw new NullPointerException("null cannot be cast to non-null type java.lang.String");
                        }

                        val var34: java.lang.String = var22.substring(var11);
                        val nextTime: Long = var25.getTime() + Integer.parseInt(var34);
                        if (System.currentTimeMillis() >= nextTime) {
                           var25.setTime(System.currentTimeMillis());
                           var25.setFrequency(1);
                           var10000 = 0;
                        } else {
                           if (var22 == null) {
                              throw new NullPointerException("null cannot be cast to non-null type java.lang.String");
                           }

                           val var36: java.lang.String = var22.substring(0, var23);
                           if (var25.getFrequency() > Integer.parseInt(var36)) {
                              var10000 = (int)(nextTime - System.currentTimeMillis());
                           } else {
                              var25.setFrequency(var25.getFrequency() + 1);
                              var10000 = 0;
                           }
                        }
                     }
                  } catch (var20: Exception) {
                     var10000 = 0;
                  }

                  var28 = var10000;
               }

               if (var28 > 0) {
                  throw new ConcurrentException("根据并发率还需等待$var28毫秒才可以访问", var28);
               } else {
                  return var25;
               }
            }
         }
      }
   }

   private fun fetchEnd(concurrentRecord: io.legado.app.model.analyzeRule.AnalyzeUrl.ConcurrentRecord?) {
      if (concurrentRecord != null && !concurrentRecord.getConcurrent()) {
         synchronized (concurrentRecord) {
            concurrentRecord.setFrequency(concurrentRecord.getFrequency() - 1);
         }
      }
   }

   public suspend fun getStrResponseAwait(jsStr: String? = ..., sourceRegex: String? = ..., useWebView: Boolean = ...): StrResponse {
      var `$continuation`: Continuation;
      label93: {
         if (`$completion` is <unrepresentable>) {
            `$continuation` = `$completion` as <unrepresentable>;
            if (((`$completion` as <unrepresentable>).label and Integer.MIN_VALUE) != 0) {
               `$continuation`.label -= Integer.MIN_VALUE;
               break label93;
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
               return this.this$0.getStrResponseAwait(null, null, false, this);
            }
         };
      }

      var var18: java.lang.String;
      var var19: StringUtils;
      var var40: Any;
      label107: {
         var concurrentRecord: AnalyzeUrl.ConcurrentRecord;
         var var27: StrResponse;
         label85: {
            label108: {
               label82: {
                  label81: {
                     val `$result`: Any = `$continuation`.result;
                     val var25: Any = IntrinsicsKt.getCOROUTINE_SUSPENDED();
                     switch ($continuation.label) {
                        case 0:
                           ResultKt.throwOnFailure(`$result`);
                           if (this.getType() != null) {
                              var40 = this.getUrl();
                              var19 = StringUtils.INSTANCE;
                              var18 = (java.lang.String)var40;
                              `$continuation`.L$0 = var40;
                              `$continuation`.L$1 = var19;
                              `$continuation`.label = 1;
                              var40 = this.getByteArrayAwait(`$continuation`);
                              if (var40 === var25) {
                                 return var25;
                              }
                              break label107;
                           }

                           concurrentRecord = this.fetchStart();
                           this.setCookie(if (this.source == null) null else this.source.getKey());
                           if (this.useWebView && useWebView) {
                              if (AnalyzeUrl.WhenMappings.$EnumSwitchMapping$0[this.method.ordinal()] == 2) {
                                 val var28: java.lang.String = this.urlNoQuery;
                                 val var29: java.lang.String = if (this.source == null) null else this.source.getKey();
                                 val var32: java.lang.String = if (this.webJs == null) jsStr else this.webJs;
                                 val var35: HashMap = this.getHeaderMap();
                                 val var36: java.lang.String = this.getBody();
                                 val var37: java.lang.String = this.getUserNameSpace();
                                 val var15: DebugLog = this.getDebugLog();
                                 var40 = ReaderAdapterHelper.INSTANCE.getAdapter();
                                 val var46: java.util.Map = var35;
                                 `$continuation`.L$0 = this;
                                 `$continuation`.L$1 = concurrentRecord;
                                 `$continuation`.label = 2;
                                 var40 = ReaderAdapterInterface.DefaultImpls.getStrResponseByRemoteWebview$default(
                                    (ReaderAdapterInterface)var40,
                                    var28,
                                    null,
                                    null,
                                    var29,
                                    var46,
                                    sourceRegex,
                                    var32,
                                    null,
                                    true,
                                    var36,
                                    var37,
                                    var15,
                                    `$continuation`,
                                    134,
                                    null
                                 );
                                 if (var40 === var25) {
                                    return var25;
                                 }
                                 break label81;
                              }

                              val var9: java.lang.String = this.getUrl();
                              val var10: java.lang.String = if (this.source == null) null else this.source.getKey();
                              val var30: java.lang.String = if (this.webJs == null) jsStr else this.webJs;
                              val var33: HashMap = this.getHeaderMap();
                              val var13: java.lang.String = this.getUserNameSpace();
                              val var14: DebugLog = this.getDebugLog();
                              var40 = ReaderAdapterHelper.INSTANCE.getAdapter();
                              val var10005: java.util.Map = var33;
                              `$continuation`.L$0 = this;
                              `$continuation`.L$1 = concurrentRecord;
                              `$continuation`.label = 3;
                              var40 = ReaderAdapterInterface.DefaultImpls.getStrResponseByRemoteWebview$default(
                                 (ReaderAdapterInterface)var40,
                                 var9,
                                 null,
                                 null,
                                 var10,
                                 var10005,
                                 sourceRegex,
                                 var30,
                                 null,
                                 false,
                                 null,
                                 var13,
                                 var14,
                                 `$continuation`,
                                 902,
                                 null
                              );
                              if (var40 === var25) {
                                 return var25;
                              }
                              break;
                           }

                           var40 = HttpHelperKt.getProxyClient(this.proxy, this.getDebugLog());
                           val var10001: Int = this.retry;
                           val var10002: Function1 = (
                              new Function1<Builder, Unit>(this) {
                                 {
                                    super(1);
                                    this.this$0 = `$receiver`;
                                 }

                                 public final void invoke(@NotNull Builder $this$newCallStrResponse) {
                                    OkHttpUtilsKt.addHeaders(`$this$newCallStrResponse`, this.this$0.getHeaderMap());
                                    if (WhenMappings.$EnumSwitchMapping$0[AnalyzeUrl.access$getMethod$p(this.this$0).ordinal()] == 1) {
                                       `$this$newCallStrResponse`.url(AnalyzeUrl.access$getUrlNoQuery$p(this.this$0));
                                       val contentType: java.lang.String = this.this$0.getHeaderMap().get("Content-Type");
                                       val body: java.lang.String = this.this$0.getBody();
                                       if (AnalyzeUrl.access$getFieldMap$p(this.this$0).isEmpty() && body != null && !StringsKt.isBlank(body)) {
                                          if (contentType != null && !StringsKt.isBlank(contentType)) {
                                             `$this$newCallStrResponse`.post(RequestBody.Companion.create(body, MediaType.Companion.get(contentType)));
                                          } else {
                                             OkHttpUtilsKt.postJson(`$this$newCallStrResponse`, body);
                                          }

                                          return;
                                       }

                                       OkHttpUtilsKt.postForm(`$this$newCallStrResponse`, AnalyzeUrl.access$getFieldMap$p(this.this$0), true);
                                    } else {
                                       OkHttpUtilsKt.get(
                                          `$this$newCallStrResponse`,
                                          AnalyzeUrl.access$getUrlNoQuery$p(this.this$0),
                                          AnalyzeUrl.access$getFieldMap$p(this.this$0),
                                          true
                                       );
                                    }
                                 }
                              }
                           ) as Function1;
                           `$continuation`.L$0 = this;
                           `$continuation`.L$1 = concurrentRecord;
                           `$continuation`.label = 4;
                           var40 = OkHttpUtilsKt.newCallStrResponse((OkHttpClient)var40, var10001, var10002, `$continuation`);
                           if (var40 === var25) {
                              return var25;
                           }
                           break label82;
                        case 1:
                           var19 = `$continuation`.L$1 as StringUtils;
                           var18 = `$continuation`.L$0 as java.lang.String;
                           ResultKt.throwOnFailure(`$result`);
                           var40 = `$result`;
                           break label107;
                        case 2:
                           concurrentRecord = `$continuation`.L$1 as AnalyzeUrl.ConcurrentRecord;
                           this = `$continuation`.L$0 as AnalyzeUrl;
                           ResultKt.throwOnFailure(`$result`);
                           var40 = `$result`;
                           break label81;
                        case 3:
                           concurrentRecord = `$continuation`.L$1 as AnalyzeUrl.ConcurrentRecord;
                           this = `$continuation`.L$0 as AnalyzeUrl;
                           ResultKt.throwOnFailure(`$result`);
                           var40 = `$result`;
                           break;
                        case 4:
                           concurrentRecord = `$continuation`.L$1 as AnalyzeUrl.ConcurrentRecord;
                           this = `$continuation`.L$0 as AnalyzeUrl;
                           ResultKt.throwOnFailure(`$result`);
                           var40 = `$result`;
                           break label82;
                        default:
                           throw new IllegalStateException("call to 'resume' before 'invoke' with coroutine");
                     }

                     var40 = var40 as StrResponse;
                     break label108;
                  }

                  var40 = var40 as StrResponse;
                  break label108;
               }

               var27 = var40 as StrResponse;
               this.saveCookieJar((var40 as StrResponse).getRaw());
               break label85;
            }

            var27 = (StrResponse)var40;
         }

         this.fetchEnd(concurrentRecord);
         return var27;
      }

      return new StrResponse(var18, var19.byteToHexString(var40 as ByteArray));
   }

   public fun saveCookieJar(response: Response) {
      if (response.headers("Set-Cookie").size() > 0) {
         val cookieStore: CookieStore = new CookieStore(this.getUserNameSpace());
         val domain: java.lang.String = NetworkUtils.INSTANCE.getSubDomain(this.url);

         val `$this$forEach$iv`: java.lang.Iterable;
         for (Object element$iv : $this$forEach$iv) {
            cookieStore.replaceCookie(Intrinsics.stringPlus(domain, "_cookieJar"), `element$iv` as java.lang.String);
         }
      }
   }

   @JvmOverloads
   public fun getStrResponse(jsStr: String? = null, sourceRegex: String? = null, useWebView: Boolean = true): StrResponse {
      return BuildersKt.runBlocking$default(
         null, (new Function2<CoroutineScope, Continuation<? super StrResponse>, Object>(this, jsStr, sourceRegex, useWebView, null) {
            int label;

            {
               super(2, `$completionx`);
               this.this$0 = `$receiver`;
               this.$jsStr = `$jsStr`;
               this.$sourceRegex = `$sourceRegex`;
               this.$useWebView = `$useWebView`;
            }

            @Nullable
            @Override
            public final Object invokeSuspend(@NotNull Object $result) {
               val var2: Any = IntrinsicsKt.getCOROUTINE_SUSPENDED();
               var var10000: Any;
               switch (this.label) {
                  case 0:
                     ResultKt.throwOnFailure(`$result`);
                     var10000 = this.this$0;
                     val var10001: java.lang.String = this.$jsStr;
                     val var10002: java.lang.String = this.$sourceRegex;
                     val var10003: Boolean = this.$useWebView;
                     val var10004: Continuation = this;
                     this.label = 1;
                     var10000 = (AnalyzeUrl)var10000.getStrResponseAwait(var10001, var10002, var10003, var10004);
                     if (var10000 === var2) {
                        return var2;
                     }
                     break;
                  case 1:
                     ResultKt.throwOnFailure(`$result`);
                     var10000 = (AnalyzeUrl)`$result`;
                     break;
                  default:
                     throw new IllegalStateException("call to 'resume' before 'invoke' with coroutine");
               }

               return var10000;
            }

            @NotNull
            @Override
            public final Continuation<Unit> create(@Nullable Object value, @NotNull Continuation<?> $completion) {
               return new <anonymous constructor>(this.this$0, this.$jsStr, this.$sourceRegex, this.$useWebView, `$completion`);
            }

            @Nullable
            public final Object invoke(@NotNull CoroutineScope p1, @Nullable Continuation<? super StrResponse> p2) {
               return (this.create(p1, p2) as <unrepresentable>).invokeSuspend(Unit.INSTANCE);
            }
         }) as Function2, 1, null
      ) as StrResponse;
   }

   public suspend fun getResponseAwait(): Response {
      var `$continuation`: Continuation;
      label25: {
         if (`$completion` is <unrepresentable>) {
            `$continuation` = `$completion` as <unrepresentable>;
            if (((`$completion` as <unrepresentable>).label and Integer.MIN_VALUE) != 0) {
               `$continuation`.label -= Integer.MIN_VALUE;
               break label25;
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
               return this.this$0.getResponseAwait(this);
            }
         };
      }

      val `$result`: Any = `$continuation`.result;
      val var6: Any = IntrinsicsKt.getCOROUTINE_SUSPENDED();
      var concurrentRecord: AnalyzeUrl.ConcurrentRecord;
      var var10000: Any;
      switch ($continuation.label) {
         case 0:
            ResultKt.throwOnFailure(`$result`);
            concurrentRecord = this.fetchStart();
            this.setCookie(if (this.source == null) null else this.source.getKey());
            var10000 = HttpHelperKt.getProxyClient$default(this.proxy, null, 2, null);
            val var10001: Int = this.retry;
            val var10002: Function1 = (
               new Function1<Builder, Unit>(this) {
                  {
                     super(1);
                     this.this$0 = `$receiver`;
                  }

                  public final void invoke(@NotNull Builder $this$newCallResponse) {
                     OkHttpUtilsKt.addHeaders(`$this$newCallResponse`, this.this$0.getHeaderMap());
                     if (WhenMappings.$EnumSwitchMapping$0[AnalyzeUrl.access$getMethod$p(this.this$0).ordinal()] == 1) {
                        `$this$newCallResponse`.url(AnalyzeUrl.access$getUrlNoQuery$p(this.this$0));
                        val contentType: java.lang.String = this.this$0.getHeaderMap().get("Content-Type");
                        val body: java.lang.String = this.this$0.getBody();
                        if (AnalyzeUrl.access$getFieldMap$p(this.this$0).isEmpty() && body != null && !StringsKt.isBlank(body)) {
                           if (contentType != null && !StringsKt.isBlank(contentType)) {
                              `$this$newCallResponse`.post(RequestBody.Companion.create(body, MediaType.Companion.get(contentType)));
                           } else {
                              OkHttpUtilsKt.postJson(`$this$newCallResponse`, body);
                           }

                           return;
                        }

                        OkHttpUtilsKt.postForm(`$this$newCallResponse`, AnalyzeUrl.access$getFieldMap$p(this.this$0), true);
                     } else {
                        OkHttpUtilsKt.get(
                           `$this$newCallResponse`, AnalyzeUrl.access$getUrlNoQuery$p(this.this$0), AnalyzeUrl.access$getFieldMap$p(this.this$0), true
                        );
                     }
                  }
               }
            ) as Function1;
            `$continuation`.L$0 = this;
            `$continuation`.L$1 = concurrentRecord;
            `$continuation`.label = 1;
            var10000 = OkHttpUtilsKt.newCallResponse((OkHttpClient)var10000, var10001, var10002, `$continuation`);
            if (var10000 === var6) {
               return var6;
            }
            break;
         case 1:
            concurrentRecord = `$continuation`.L$1 as AnalyzeUrl.ConcurrentRecord;
            this = `$continuation`.L$0 as AnalyzeUrl;
            ResultKt.throwOnFailure(`$result`);
            var10000 = `$result`;
            break;
         default:
            throw new IllegalStateException("call to 'resume' before 'invoke' with coroutine");
      }

      val var7: Response = var10000 as Response;
      this.fetchEnd(concurrentRecord);
      return var7;
   }

   public fun getResponse(): Response {
      return BuildersKt.runBlocking$default(null, (new Function2<CoroutineScope, Continuation<? super Response>, Object>(this, null) {
         int label;

         {
            super(2, `$completionx`);
            this.this$0 = `$receiver`;
         }

         @Nullable
         @Override
         public final Object invokeSuspend(@NotNull Object $result) {
            val var2: Any = IntrinsicsKt.getCOROUTINE_SUSPENDED();
            var var10000: Any;
            switch (this.label) {
               case 0:
                  ResultKt.throwOnFailure(`$result`);
                  var10000 = this.this$0;
                  val var10001: Continuation = this;
                  this.label = 1;
                  var10000 = (AnalyzeUrl)var10000.getResponseAwait(var10001);
                  if (var10000 === var2) {
                     return var2;
                  }
                  break;
               case 1:
                  ResultKt.throwOnFailure(`$result`);
                  var10000 = (AnalyzeUrl)`$result`;
                  break;
               default:
                  throw new IllegalStateException("call to 'resume' before 'invoke' with coroutine");
            }

            return var10000;
         }

         @NotNull
         @Override
         public final Continuation<Unit> create(@Nullable Object value, @NotNull Continuation<?> $completion) {
            return new <anonymous constructor>(this.this$0, `$completion`);
         }

         @Nullable
         public final Object invoke(@NotNull CoroutineScope p1, @Nullable Continuation<? super Response> p2) {
            return (this.create(p1, p2) as <unrepresentable>).invokeSuspend(Unit.INSTANCE);
         }
      }) as Function2, 1, null) as Response;
   }

   public suspend fun getByteArrayAwait(): ByteArray {
      var `$continuation`: Continuation;
      label29: {
         if (`$completion` is <unrepresentable>) {
            `$continuation` = `$completion` as <unrepresentable>;
            if (((`$completion` as <unrepresentable>).label and Integer.MIN_VALUE) != 0) {
               `$continuation`.label -= Integer.MIN_VALUE;
               break label29;
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
               return this.this$0.getByteArrayAwait(this);
            }
         };
      }

      val `$result`: Any = `$continuation`.result;
      val var8: Any = IntrinsicsKt.getCOROUTINE_SUSPENDED();
      var concurrentRecord: AnalyzeUrl.ConcurrentRecord;
      var var10000: Any;
      switch ($continuation.label) {
         case 0:
            ResultKt.throwOnFailure(`$result`);
            concurrentRecord = this.fetchStart();
            val dataUriFindResult: MatchResult = Regex.find$default(AppPattern.INSTANCE.getDataUriRegex(), this.urlNoQuery, 0, 2, null);
            if (dataUriFindResult != null) {
               val byteArray: ByteArray = Base64.decode(dataUriFindResult.getGroupValues().get(1), 0);
               this.fetchEnd(concurrentRecord);
               return byteArray;
            }

            this.setCookie(if (this.source == null) null else this.source.getKey());
            var10000 = HttpHelperKt.getProxyClient$default(this.proxy, null, 2, null);
            val var10001: Int = this.retry;
            val var10002: Function1 = (
               new Function1<Builder, Unit>(this) {
                  {
                     super(1);
                     this.this$0 = `$receiver`;
                  }

                  public final void invoke(@NotNull Builder $this$newCallResponseBody) {
                     OkHttpUtilsKt.addHeaders(`$this$newCallResponseBody`, this.this$0.getHeaderMap());
                     if (WhenMappings.$EnumSwitchMapping$0[AnalyzeUrl.access$getMethod$p(this.this$0).ordinal()] == 1) {
                        `$this$newCallResponseBody`.url(AnalyzeUrl.access$getUrlNoQuery$p(this.this$0));
                        val contentType: java.lang.String = this.this$0.getHeaderMap().get("Content-Type");
                        val body: java.lang.String = this.this$0.getBody();
                        if (AnalyzeUrl.access$getFieldMap$p(this.this$0).isEmpty() && body != null && !StringsKt.isBlank(body)) {
                           if (contentType != null && !StringsKt.isBlank(contentType)) {
                              `$this$newCallResponseBody`.post(RequestBody.Companion.create(body, MediaType.Companion.get(contentType)));
                           } else {
                              OkHttpUtilsKt.postJson(`$this$newCallResponseBody`, body);
                           }

                           return;
                        }

                        OkHttpUtilsKt.postForm(`$this$newCallResponseBody`, AnalyzeUrl.access$getFieldMap$p(this.this$0), true);
                     } else {
                        OkHttpUtilsKt.get(
                           `$this$newCallResponseBody`, AnalyzeUrl.access$getUrlNoQuery$p(this.this$0), AnalyzeUrl.access$getFieldMap$p(this.this$0), true
                        );
                     }
                  }
               }
            ) as Function1;
            `$continuation`.L$0 = this;
            `$continuation`.L$1 = concurrentRecord;
            `$continuation`.label = 1;
            var10000 = OkHttpUtilsKt.newCallResponseBody((OkHttpClient)var10000, var10001, var10002, `$continuation`);
            if (var10000 === var8) {
               return var8;
            }
            break;
         case 1:
            concurrentRecord = `$continuation`.L$1 as AnalyzeUrl.ConcurrentRecord;
            this = `$continuation`.L$0 as AnalyzeUrl;
            ResultKt.throwOnFailure(`$result`);
            var10000 = `$result`;
            break;
         default:
            throw new IllegalStateException("call to 'resume' before 'invoke' with coroutine");
      }

      val var9: ByteArray = (var10000 as ResponseBody).bytes();
      this.fetchEnd(concurrentRecord);
      return var9;
   }

   public fun getByteArray(): ByteArray {
      return BuildersKt.runBlocking$default(null, (new Function2<CoroutineScope, Continuation<byte[]>, Object>(this, null) {
         int label;

         {
            super(2, `$completionx`);
            this.this$0 = `$receiver`;
         }

         @Nullable
         @Override
         public final Object invokeSuspend(@NotNull Object $result) {
            val var2: Any = IntrinsicsKt.getCOROUTINE_SUSPENDED();
            var var10000: Any;
            switch (this.label) {
               case 0:
                  ResultKt.throwOnFailure(`$result`);
                  var10000 = this.this$0;
                  val var10001: Continuation = this;
                  this.label = 1;
                  var10000 = (AnalyzeUrl)var10000.getByteArrayAwait(var10001);
                  if (var10000 === var2) {
                     return var2;
                  }
                  break;
               case 1:
                  ResultKt.throwOnFailure(`$result`);
                  var10000 = (AnalyzeUrl)`$result`;
                  break;
               default:
                  throw new IllegalStateException("call to 'resume' before 'invoke' with coroutine");
            }

            return var10000;
         }

         @NotNull
         @Override
         public final Continuation<Unit> create(@Nullable Object value, @NotNull Continuation<?> $completion) {
            return new <anonymous constructor>(this.this$0, `$completion`);
         }

         @Nullable
         public final Object invoke(@NotNull CoroutineScope p1, @Nullable Continuation<byte[]> p2) {
            return (this.create(p1, p2) as <unrepresentable>).invokeSuspend(Unit.INSTANCE);
         }
      }) as Function2, 1, null) as ByteArray;
   }

   public suspend fun upload(fileName: String, file: Any, contentType: String): StrResponse {
      return OkHttpUtilsKt.newCallStrResponse(
         HttpHelperKt.getProxyClient$default(this.proxy, null, 2, null),
         this.retry,
         (
            new Function1<Builder, Unit>(this, fileName, file, contentType) {
               {
                  super(1);
                  this.this$0 = `$receiver`;
                  this.$fileName = `$fileName`;
                  this.$file = `$file`;
                  this.$contentType = `$contentType`;
               }

               public final void invoke(@NotNull Builder $this$newCallStrResponse) {
                  `$this$newCallStrResponse`.url(AnalyzeUrl.access$getUrlNoQuery$p(this.this$0));
                  val `$this$forEach$iv`: Gson = GsonExtensionsKt.getGSON();
                  var `json$iv`: java.lang.String = this.this$0.getBody();

                  var `$i$f$forEach`: Any;
                  try {
                     `$i$f$forEach` = Result.Companion;
                     val var30: Type = new AnalyzeUrl$upload$2$invoke$$inlined$fromJsonObject$1().getType();
                     var var10000: Any = `$this$forEach$iv`.fromJson(`json$iv`, var30);
                     if (var10000 !is HashMap) {
                        var10000 = null;
                     }

                     `$i$f$forEach` = Result.constructor-impl(var10000 as HashMap);
                  } catch (var18: java.lang.Throwable) {
                     val `$i$f$genericType`: Result.Companion = Result.Companion;
                     `$i$f$forEach` = Result.constructor-impl(ResultKt.createFailure(var18));
                  }

                  val var33: Any = if (Result.isFailure-impl(`$i$f$forEach`)) null else `$i$f$forEach`;
                  val bodyMap: HashMap = var33 as HashMap;
                  val var19: java.util.Map = var33 as HashMap;
                  `json$iv` = this.$fileName;
                  val var22: Any = this.$file;
                  val var23: java.lang.String = this.$contentType;

                  for (Entry element$iv : var19.entrySet()) {
                     if (`element$iv`.getValue().toString() == "fileRequest") {
                        bodyMap.put(
                           `element$iv`.getKey(),
                           MapsKt.mapOf(new Pair[]{new Pair<>("fileName", `json$iv`), new Pair<>("file", var22), new Pair<>("contentType", var23)})
                        );
                     }
                  }

                  OkHttpUtilsKt.postMultipart(`$this$newCallStrResponse`, this.this$0.getType(), bodyMap);
               }
            }
         ) as (Builder?) -> Unit,
         `$completion`
      );
   }

   private fun setCookie(tag: String?) {
      val domain: java.lang.String = NetworkUtils.INSTANCE.getSubDomain(if (tag == null) this.url else tag);
      if (domain.length() != 0) {
         val var14: CookieStore = new CookieStore(this.getUserNameSpace());
         if (this.enabledCookieJar) {
            val cookieMap: java.lang.String = var14.getCookie(Intrinsics.stringPlus(domain, "_cookieJar"));
            if (cookieMap != null) {
               var14.replaceCookie(domain, cookieMap);
            }
         }

         val var16: java.lang.String = var14.getCookie(domain);
         if (var16.length() > 0) {
            val var18: java.util.Map = var14.cookieToMap(var16);
            val var22: java.lang.String = this.headerMap.get("Cookie");
            var18.putAll(var14.cookieToMap(if (var22 == null) "" else var22));
            val var21: java.lang.String = var14.mapToCookie(var18);
            if (var21 != null) {
               val var10000: java.lang.String = this.getHeaderMap().put("Cookie", var21);
            }
         }
      }
   }

   public fun getUserAgent(): String {
      val var1: java.lang.String = this.headerMap.get("User-Agent");
      return if (var1 == null) AppConst.INSTANCE.getUserAgent() else var1;
   }

   public fun isPost(): Boolean {
      return this.method === RequestMethod.POST;
   }

   override fun aesBase64DecodeToByteArray(str: java.lang.String, key: java.lang.String, transformation: java.lang.String, iv: java.lang.String): ByteArray? {
      return JsExtensions.DefaultImpls.aesBase64DecodeToByteArray(this, str, key, transformation, iv);
   }

   override fun aesBase64DecodeToString(str: java.lang.String, key: java.lang.String, transformation: java.lang.String, iv: java.lang.String): java.lang.String? {
      return JsExtensions.DefaultImpls.aesBase64DecodeToString(this, str, key, transformation, iv);
   }

   override fun aesDecodeArgsBase64Str(data: java.lang.String, key: java.lang.String, mode: java.lang.String, padding: java.lang.String, iv: java.lang.String): java.lang.String? {
      return JsExtensions.DefaultImpls.aesDecodeArgsBase64Str(this, data, key, mode, padding, iv);
   }

   override fun aesDecodeToByteArray(str: java.lang.String, key: java.lang.String, transformation: java.lang.String, iv: java.lang.String): ByteArray? {
      return JsExtensions.DefaultImpls.aesDecodeToByteArray(this, str, key, transformation, iv);
   }

   override fun aesDecodeToString(str: java.lang.String, key: java.lang.String, transformation: java.lang.String, iv: java.lang.String): java.lang.String? {
      return JsExtensions.DefaultImpls.aesDecodeToString(this, str, key, transformation, iv);
   }

   override fun aesEncodeArgsBase64Str(data: java.lang.String, key: java.lang.String, mode: java.lang.String, padding: java.lang.String, iv: java.lang.String): java.lang.String? {
      return JsExtensions.DefaultImpls.aesEncodeArgsBase64Str(this, data, key, mode, padding, iv);
   }

   override fun aesEncodeToBase64ByteArray(data: java.lang.String, key: java.lang.String, transformation: java.lang.String, iv: java.lang.String): ByteArray? {
      return JsExtensions.DefaultImpls.aesEncodeToBase64ByteArray(this, data, key, transformation, iv);
   }

   override fun aesEncodeToBase64String(data: java.lang.String, key: java.lang.String, transformation: java.lang.String, iv: java.lang.String): java.lang.String? {
      return JsExtensions.DefaultImpls.aesEncodeToBase64String(this, data, key, transformation, iv);
   }

   override fun aesEncodeToByteArray(data: java.lang.String, key: java.lang.String, transformation: java.lang.String, iv: java.lang.String): ByteArray? {
      return JsExtensions.DefaultImpls.aesEncodeToByteArray(this, data, key, transformation, iv);
   }

   override fun aesEncodeToString(data: java.lang.String, key: java.lang.String, transformation: java.lang.String, iv: java.lang.String): java.lang.String? {
      return JsExtensions.DefaultImpls.aesEncodeToString(this, data, key, transformation, iv);
   }

   override fun ajax(urlStr: java.lang.String): java.lang.String? {
      return JsExtensions.DefaultImpls.ajax(this, urlStr);
   }

   override fun ajaxAll(urlList: Array<java.lang.String>): Array<StrResponse> {
      return JsExtensions.DefaultImpls.ajaxAll(this, urlList);
   }

   override fun androidId(): java.lang.String {
      return JsExtensions.DefaultImpls.androidId(this);
   }

   override fun base64Decode(str: java.lang.String): java.lang.String {
      return JsExtensions.DefaultImpls.base64Decode(this, str);
   }

   override fun base64Decode(str: java.lang.String, flags: Int): java.lang.String {
      return JsExtensions.DefaultImpls.base64Decode(this, str, flags);
   }

   override fun base64DecodeToByteArray(str: java.lang.String?): ByteArray? {
      return JsExtensions.DefaultImpls.base64DecodeToByteArray(this, str);
   }

   override fun base64DecodeToByteArray(str: java.lang.String?, flags: Int): ByteArray? {
      return JsExtensions.DefaultImpls.base64DecodeToByteArray(this, str, flags);
   }

   override fun base64Encode(str: java.lang.String): java.lang.String? {
      return JsExtensions.DefaultImpls.base64Encode(this, str);
   }

   override fun base64Encode(str: java.lang.String, flags: Int): java.lang.String? {
      return JsExtensions.DefaultImpls.base64Encode(this, str, flags);
   }

   override fun cacheFile(urlStr: java.lang.String): java.lang.String? {
      return JsExtensions.DefaultImpls.cacheFile(this, urlStr);
   }

   override fun cacheFile(urlStr: java.lang.String, saveTime: Int): java.lang.String? {
      return JsExtensions.DefaultImpls.cacheFile(this, urlStr, saveTime);
   }

   override fun connect(urlStr: java.lang.String): StrResponse {
      return JsExtensions.DefaultImpls.connect(this, urlStr);
   }

   override fun connect(urlStr: java.lang.String, header: java.lang.String?): StrResponse {
      return JsExtensions.DefaultImpls.connect(this, urlStr, header);
   }

   override fun deleteFile(path: java.lang.String) {
      JsExtensions.DefaultImpls.deleteFile(this, path);
   }

   override fun desBase64DecodeToString(data: java.lang.String, key: java.lang.String, transformation: java.lang.String, iv: java.lang.String): java.lang.String? {
      return JsExtensions.DefaultImpls.desBase64DecodeToString(this, data, key, transformation, iv);
   }

   override fun desDecodeToString(data: java.lang.String, key: java.lang.String, transformation: java.lang.String, iv: java.lang.String): java.lang.String? {
      return JsExtensions.DefaultImpls.desDecodeToString(this, data, key, transformation, iv);
   }

   override fun desEncodeToBase64String(data: java.lang.String, key: java.lang.String, transformation: java.lang.String, iv: java.lang.String): java.lang.String? {
      return JsExtensions.DefaultImpls.desEncodeToBase64String(this, data, key, transformation, iv);
   }

   override fun desEncodeToString(data: java.lang.String, key: java.lang.String, transformation: java.lang.String, iv: java.lang.String): java.lang.String? {
      return JsExtensions.DefaultImpls.desEncodeToString(this, data, key, transformation, iv);
   }

   override fun digestBase64Str(data: java.lang.String, algorithm: java.lang.String): java.lang.String? {
      return JsExtensions.DefaultImpls.digestBase64Str(this, data, algorithm);
   }

   override fun digestHex(data: java.lang.String, algorithm: java.lang.String): java.lang.String? {
      return JsExtensions.DefaultImpls.digestHex(this, data, algorithm);
   }

   override fun downloadFile(content: java.lang.String, url: java.lang.String): java.lang.String {
      return JsExtensions.DefaultImpls.downloadFile(this, content, url);
   }

   override fun encodeURI(str: java.lang.String): java.lang.String {
      return JsExtensions.DefaultImpls.encodeURI(this, str);
   }

   override fun encodeURI(str: java.lang.String, enc: java.lang.String): java.lang.String {
      return JsExtensions.DefaultImpls.encodeURI(this, str, enc);
   }

   override fun get(urlStr: java.lang.String, headers: MutableMap<java.lang.String, java.lang.String>): org.jsoup.Connection.Response {
      return JsExtensions.DefaultImpls.get(this, urlStr, headers);
   }

   override fun getCookie(tag: java.lang.String, key: java.lang.String?): java.lang.String {
      return JsExtensions.DefaultImpls.getCookie(this, tag, key);
   }

   override fun getFile(path: java.lang.String): File {
      return JsExtensions.DefaultImpls.getFile(this, path);
   }

   override fun getTxtInFolder(unzipPath: java.lang.String): java.lang.String {
      return JsExtensions.DefaultImpls.getTxtInFolder(this, unzipPath);
   }

   override fun getZipByteArrayContent(url: java.lang.String, path: java.lang.String): ByteArray? {
      return JsExtensions.DefaultImpls.getZipByteArrayContent(this, url, path);
   }

   override fun getZipStringContent(url: java.lang.String, path: java.lang.String): java.lang.String {
      return JsExtensions.DefaultImpls.getZipStringContent(this, url, path);
   }

   override fun getZipStringContent(url: java.lang.String, path: java.lang.String, charsetName: java.lang.String): java.lang.String {
      return JsExtensions.DefaultImpls.getZipStringContent(this, url, path, charsetName);
   }

   override fun head(urlStr: java.lang.String, headers: MutableMap<java.lang.String, java.lang.String>): org.jsoup.Connection.Response {
      return JsExtensions.DefaultImpls.head(this, urlStr, headers);
   }

   override fun htmlFormat(str: java.lang.String): java.lang.String {
      return JsExtensions.DefaultImpls.htmlFormat(this, str);
   }

   override fun importScript(path: java.lang.String): java.lang.String {
      return JsExtensions.DefaultImpls.importScript(this, path);
   }

   override fun log(msg: java.lang.String): java.lang.String {
      return JsExtensions.DefaultImpls.log(this, msg);
   }

   override fun logType(any: Any?) {
      JsExtensions.DefaultImpls.logType(this, any);
   }

   override fun longToast(msg: Any?) {
      JsExtensions.DefaultImpls.longToast(this, msg);
   }

   override fun md5Encode(str: java.lang.String): java.lang.String {
      return JsExtensions.DefaultImpls.md5Encode(this, str);
   }

   override fun md5Encode16(str: java.lang.String): java.lang.String {
      return JsExtensions.DefaultImpls.md5Encode16(this, str);
   }

   override fun post(urlStr: java.lang.String, body: java.lang.String, headers: MutableMap<java.lang.String, java.lang.String>): org.jsoup.Connection.Response {
      return JsExtensions.DefaultImpls.post(this, urlStr, body, headers);
   }

   override fun queryBase64TTF(base64: java.lang.String?): QueryTTF? {
      return JsExtensions.DefaultImpls.queryBase64TTF(this, base64);
   }

   override fun queryTTF(str: java.lang.String?): QueryTTF? {
      return JsExtensions.DefaultImpls.queryTTF(this, str);
   }

   override fun randomUUID(): java.lang.String {
      return JsExtensions.DefaultImpls.randomUUID(this);
   }

   override fun readFile(path: java.lang.String): ByteArray? {
      return JsExtensions.DefaultImpls.readFile(this, path);
   }

   override fun readTxtFile(path: java.lang.String): java.lang.String {
      return JsExtensions.DefaultImpls.readTxtFile(this, path);
   }

   override fun readTxtFile(path: java.lang.String, charsetName: java.lang.String): java.lang.String {
      return JsExtensions.DefaultImpls.readTxtFile(this, path, charsetName);
   }

   override fun replaceFont(text: java.lang.String, font1: QueryTTF?, font2: QueryTTF?): java.lang.String {
      return JsExtensions.DefaultImpls.replaceFont(this, text, font1, font2);
   }

   override fun timeFormat(time: Long): java.lang.String {
      return JsExtensions.DefaultImpls.timeFormat(this, time);
   }

   override fun timeFormatUTC(time: Long, format: java.lang.String, sh: Int): java.lang.String? {
      return JsExtensions.DefaultImpls.timeFormatUTC(this, time, format, sh);
   }

   override fun toast(msg: Any?) {
      JsExtensions.DefaultImpls.toast(this, msg);
   }

   override fun tripleDESDecodeArgsBase64Str(
      data: java.lang.String, key: java.lang.String, mode: java.lang.String, padding: java.lang.String, iv: java.lang.String
   ): java.lang.String? {
      return JsExtensions.DefaultImpls.tripleDESDecodeArgsBase64Str(this, data, key, mode, padding, iv);
   }

   override fun tripleDESDecodeStr(data: java.lang.String, key: java.lang.String, mode: java.lang.String, padding: java.lang.String, iv: java.lang.String): java.lang.String? {
      return JsExtensions.DefaultImpls.tripleDESDecodeStr(this, data, key, mode, padding, iv);
   }

   override fun tripleDESEncodeArgsBase64Str(
      data: java.lang.String, key: java.lang.String, mode: java.lang.String, padding: java.lang.String, iv: java.lang.String
   ): java.lang.String? {
      return JsExtensions.DefaultImpls.tripleDESEncodeArgsBase64Str(this, data, key, mode, padding, iv);
   }

   override fun tripleDESEncodeBase64Str(data: java.lang.String, key: java.lang.String, mode: java.lang.String, padding: java.lang.String, iv: java.lang.String): java.lang.String? {
      return JsExtensions.DefaultImpls.tripleDESEncodeBase64Str(this, data, key, mode, padding, iv);
   }

   override fun unzipFile(zipPath: java.lang.String): java.lang.String {
      return JsExtensions.DefaultImpls.unzipFile(this, zipPath);
   }

   override fun utf8ToGbk(str: java.lang.String): java.lang.String {
      return JsExtensions.DefaultImpls.utf8ToGbk(this, str);
   }

   override fun webView(html: java.lang.String?, url: java.lang.String?, js: java.lang.String?): java.lang.String? {
      return JsExtensions.DefaultImpls.webView(this, html, url, js);
   }

   @JvmOverloads
   fun getStrResponse(jsStr: java.lang.String?, sourceRegex: java.lang.String?): StrResponse {
      return getStrResponse$default(this, jsStr, sourceRegex, false, 4, null);
   }

   @JvmOverloads
   fun getStrResponse(jsStr: java.lang.String?): StrResponse {
      return getStrResponse$default(this, jsStr, null, false, 6, null);
   }

   @JvmOverloads
   fun getStrResponse(): StrResponse {
      return getStrResponse$default(this, null, null, false, 7, null);
   }

   @JvmStatic
   fun {
      val var0: Pattern = Pattern.compile("\\s*,\\s*(?=\\{)");
      paramPattern = var0;
   }

   public companion object {
      private final val concurrentRecordMap: HashMap<String, io.legado.app.model.analyzeRule.AnalyzeUrl.ConcurrentRecord>
      private final val pagePattern: Pattern
      public final val paramPattern: Pattern
   }

   public data class ConcurrentRecord(concurrent: Boolean, time: Long, frequency: Int) {
      public final val concurrent: Boolean

      public final var frequency: Int
         internal set

      public final var time: Long
         internal set

      init {
         this.concurrent = concurrent;
         this.time = time;
         this.frequency = frequency;
      }

      public operator fun component1(): Boolean {
         return this.concurrent;
      }

      public operator fun component2(): Long {
         return this.time;
      }

      public operator fun component3(): Int {
         return this.frequency;
      }

      public fun copy(concurrent: Boolean = this.concurrent, time: Long = this.time, frequency: Int = this.frequency): io.legado.app.model.analyzeRule.AnalyzeUrl.ConcurrentRecord {
         return new AnalyzeUrl.ConcurrentRecord(concurrent, time, frequency);
      }

      public override fun toString(): String {
         return "ConcurrentRecord(concurrent=${this.concurrent}, time=${this.time}, frequency=${this.frequency})";
      }

      public override fun hashCode(): Int {
         var var10000: Byte = this.concurrent;
         if (this.concurrent) {
            var10000 = 1;
         }

         return (var10000 * 31 + java.lang.Long.hashCode(this.time)) * 31 + Integer.hashCode(this.frequency);
      }

      public override operator fun equals(other: Any?): Boolean {
         if (this === other) {
            return true;
         } else if (other !is AnalyzeUrl.ConcurrentRecord) {
            return false;
         } else {
            val var2: AnalyzeUrl.ConcurrentRecord = other as AnalyzeUrl.ConcurrentRecord;
            if (this.concurrent != (other as AnalyzeUrl.ConcurrentRecord).concurrent) {
               return false;
            } else if (this.time != var2.time) {
               return false;
            } else {
               return this.frequency == var2.frequency;
            }
         }
      }
   }

   public data class UrlOption(method: String? = null,
      charset: String? = null,
      headers: Any? = null,
      body: Any? = null,
      retry: Int? = null,
      type: String? = null,
      webView: Any? = null,
      webJs: String? = null,
      js: String? = null
   ) {
      private final var body: Any?
      private final var charset: String?
      private final var headers: Any?
      private final var js: String?
      private final var method: String?
      private final var retry: Int?
      private final var type: String?
      private final var webJs: String?
      private final var webView: Any?

      init {
         this.method = method;
         this.charset = charset;
         this.headers = headers;
         this.body = body;
         this.retry = retry;
         this.type = type;
         this.webView = webView;
         this.webJs = webJs;
         this.js = js;
      }

      public fun setMethod(value: String?) {
         this.method = if (value as java.lang.CharSequence == null || StringsKt.isBlank(value)) null else value;
      }

      public fun getMethod(): String? {
         return this.method;
      }

      public fun setCharset(value: String?) {
         this.charset = if (value as java.lang.CharSequence == null || StringsKt.isBlank(value)) null else value;
      }

      public fun getCharset(): String? {
         return this.charset;
      }

      public fun setRetry(value: String?) {
         this.retry = if (value as java.lang.CharSequence == null || value.length() == 0) null else StringsKt.toIntOrNull(value);
      }

      public fun getRetry(): Int {
         return if (this.retry == null) 0 else this.retry;
      }

      public fun setType(value: String?) {
         this.type = if (value as java.lang.CharSequence == null || StringsKt.isBlank(value)) null else value;
      }

      public fun getType(): String? {
         return this.type;
      }

      public fun useWebView(): Boolean {
         val var1: Any = this.webView;
         return this.webView != null && !(this.webView == "") && !(this.webView == false) && !(this.webView == "false");
      }

      public fun useWebView(boolean: Boolean) {
         this.webView = if (boolean) true else null;
      }

      public fun setHeaders(value: String?) {
         var var10000: AnalyzeUrl.UrlOption = this;
         val var10001: java.util.Map;
         if (value == null || StringsKt.isBlank(value)) {
            var10001 = null as java.util.Map;
         } else {
            val var12: Gson = GsonExtensionsKt.getGSON();

            var var5: Any;
            try {
               var5 = Result.Companion;
               val var20: Type = new AnalyzeUrl$UrlOption$setHeaders$$inlined$fromJsonObject$1().getType();
               var var21: Any = var12.fromJson(value, var20);
               if (var21 !is java.util.Map) {
                  var21 = null;
               }

               var5 = Result.constructor-impl(var21 as java.util.Map);
            } catch (var11: java.lang.Throwable) {
               val `$i$f$genericType`: Result.Companion = Result.Companion;
               var5 = Result.constructor-impl(ResultKt.createFailure(var11));
            }

            var10000 = this;
            var10001 = (if (Result.isFailure-impl(var5)) null else var5) as java.util.Map;
         }

         var10000.headers = var10001;
      }

      public fun getHeaderMap(): Map<*, *>? {
         val value: Any = this.headers;
         val var10000: java.util.Map;
         if (this.headers is java.util.Map) {
            var10000 = this.headers as java.util.Map;
         } else if (this.headers is java.lang.String) {
            val `$this$fromJsonObject$iv`: Gson = GsonExtensionsKt.getGSON();
            val `json$iv`: java.lang.String = value as java.lang.String;

            var var6: Any;
            try {
               var6 = Result.Companion;
               val var16: Type = new AnalyzeUrl$UrlOption$getHeaderMap$$inlined$fromJsonObject$1().getType();
               var var17: Any = `$this$fromJsonObject$iv`.fromJson(`json$iv`, var16);
               if (var17 !is java.util.Map) {
                  var17 = null;
               }

               var6 = Result.constructor-impl(var17 as java.util.Map);
            } catch (var10: java.lang.Throwable) {
               val `$i$f$genericType`: Result.Companion = Result.Companion;
               var6 = Result.constructor-impl(ResultKt.createFailure(var10));
            }

            var10000 = (if (Result.isFailure-impl(var6)) null else var6) as java.util.Map;
         } else {
            var10000 = null;
         }

         return var10000;
      }

      public fun setBody(value: String?) {
         var var10000: AnalyzeUrl.UrlOption = this;
         val var10001: Any;
         if (value == null || StringsKt.isBlank(value)) {
            var10001 = null;
         } else if (StringExtensionsKt.isJsonObject(value)) {
            val var13: Gson = GsonExtensionsKt.getGSON();

            var var5: Any;
            try {
               var5 = Result.Companion;
               val var30: Type = new AnalyzeUrl$UrlOption$setBody$$inlined$fromJsonObject$1().getType();
               var var32: Any = var13.fromJson(value, var30);
               if (var32 !is java.util.Map) {
                  var32 = null;
               }

               var5 = Result.constructor-impl(var32 as java.util.Map);
            } catch (var12: java.lang.Throwable) {
               val `$i$f$genericType`: Result.Companion = Result.Companion;
               var5 = Result.constructor-impl(ResultKt.createFailure(var12));
            }

            var10000 = this;
            var10001 = Result.box-impl(var5);
         } else if (StringExtensionsKt.isJsonArray(value)) {
            val var14: Gson = GsonExtensionsKt.getGSON();

            var var20: Any;
            try {
               var20 = Result.Companion;
               val var28: Any = var14.fromJson(value, new ParameterizedTypeImpl(java.util.Map.class));
               var20 = Result.constructor-impl(var28 as? java.util.List);
            } catch (var11: java.lang.Throwable) {
               val var27: Result.Companion = Result.Companion;
               var20 = Result.constructor-impl(ResultKt.createFailure(var11));
            }

            var10000 = this;
            var10001 = Result.box-impl(var20);
         } else {
            var10001 = value;
         }

         var10000.body = var10001;
      }

      public fun getBody(): String? {
         val var1: Any = this.body;
         return if (this.body == null)
            null
            else
            (if (this.body is java.lang.String) this.body as java.lang.String else GsonExtensionsKt.getGSON().toJson(var1));
      }

      public fun setWebJs(value: String?) {
         this.webJs = if (value as java.lang.CharSequence == null || StringsKt.isBlank(value)) null else value;
      }

      public fun getWebJs(): String? {
         return this.webJs;
      }

      public fun setJs(value: String?) {
         this.js = if (value as java.lang.CharSequence == null || StringsKt.isBlank(value)) null else value;
      }

      public fun getJs(): String? {
         return this.js;
      }

      private operator fun component1(): String? {
         return this.method;
      }

      private operator fun component2(): String? {
         return this.charset;
      }

      private operator fun component3(): Any? {
         return this.headers;
      }

      private operator fun component4(): Any? {
         return this.body;
      }

      private operator fun component5(): Int? {
         return this.retry;
      }

      private operator fun component6(): String? {
         return this.type;
      }

      private operator fun component7(): Any? {
         return this.webView;
      }

      private operator fun component8(): String? {
         return this.webJs;
      }

      private operator fun component9(): String? {
         return this.js;
      }

      public fun copy(
         method: String? = this.method,
         charset: String? = this.charset,
         headers: Any? = this.headers,
         body: Any? = this.body,
         retry: Int? = this.retry,
         type: String? = this.type,
         webView: Any? = this.webView,
         webJs: String? = this.webJs,
         js: String? = this.js
      ): io.legado.app.model.analyzeRule.AnalyzeUrl.UrlOption {
         return new AnalyzeUrl.UrlOption(method, charset, headers, body, retry, type, webView, webJs, js);
      }

      public override fun toString(): String {
         return "UrlOption(method=${this.method}, charset=${this.charset}, headers=${this.headers}, body=${this.body}, retry=${this.retry}, type=${this.type}, webView=${this.webView}, webJs=${this.webJs}, js=${this.js})";
      }

      public override fun hashCode(): Int {
         return (
                  (
                           (
                                    (
                                             (
                                                      (
                                                               (
                                                                        (if (this.method == null) 0 else this.method.hashCode()) * 31
                                                                           + (if (this.charset == null) 0 else this.charset.hashCode())
                                                                     )
                                                                     * 31
                                                                  + (if (this.headers == null) 0 else this.headers.hashCode())
                                                            )
                                                            * 31
                                                         + (if (this.body == null) 0 else this.body.hashCode())
                                                   )
                                                   * 31
                                                + (if (this.retry == null) 0 else this.retry.hashCode())
                                          )
                                          * 31
                                       + (if (this.type == null) 0 else this.type.hashCode())
                                 )
                                 * 31
                              + (if (this.webView == null) 0 else this.webView.hashCode())
                        )
                        * 31
                     + (if (this.webJs == null) 0 else this.webJs.hashCode())
               )
               * 31
            + (if (this.js == null) 0 else this.js.hashCode());
      }

      public override operator fun equals(other: Any?): Boolean {
         if (this === other) {
            return true;
         } else if (other !is AnalyzeUrl.UrlOption) {
            return false;
         } else {
            val var2: AnalyzeUrl.UrlOption = other as AnalyzeUrl.UrlOption;
            if (!(this.method == (other as AnalyzeUrl.UrlOption).method)) {
               return false;
            } else if (!(this.charset == var2.charset)) {
               return false;
            } else if (!(this.headers == var2.headers)) {
               return false;
            } else if (!(this.body == var2.body)) {
               return false;
            } else if (!(this.retry == var2.retry)) {
               return false;
            } else if (!(this.type == var2.type)) {
               return false;
            } else if (!(this.webView == var2.webView)) {
               return false;
            } else if (!(this.webJs == var2.webJs)) {
               return false;
            } else {
               return this.js == var2.js;
            }
         }
      }

      fun UrlOption() {
         this(null, null, null, null, null, null, null, null, null, 511, null);
      }
   }
}
