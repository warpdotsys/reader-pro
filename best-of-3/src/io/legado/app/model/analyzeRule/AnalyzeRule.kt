package io.legado.app.model.analyzeRule

import com.htmake.reader.synth.SyntheticContinuation
import com.htmake.reader.synth.SyntheticFunction0
import com.htmake.reader.synth.SyntheticType

import com.google.gson.Gson
import com.script.SimpleBindings
import io.legado.app.constant.AppConst
import io.legado.app.constant.AppPattern
import io.legado.app.data.entities.BaseBook
import io.legado.app.data.entities.BaseSource
import io.legado.app.data.entities.Book
import io.legado.app.data.entities.BookChapter
import io.legado.app.data.entities.BookSource
import io.legado.app.data.entities.SearchBook
import io.legado.app.help.CacheManager
import io.legado.app.help.JsExtensions
import io.legado.app.help.http.CookieStore
import io.legado.app.help.http.StrResponse
import io.legado.app.model.DebugLog
import io.legado.app.model.webBook.WebBook
import io.legado.app.utils.GsonExtensionsKt
import io.legado.app.utils.NetworkUtils
import io.legado.app.utils.StringExtensionsKt
import io.legado.app.utils.StringUtils
import io.legado.app.utils.TextUtils
import io.legado.app.utils.ThrowableExtensionsKt
import java.io.File
import java.lang.reflect.Type
import java.net.URL
import java.util.ArrayList
import java.util.Arrays
import java.util.HashMap
import java.util.Map.Entry
import java.util.regex.Matcher
import java.util.regex.Pattern
import kotlin.coroutines.Continuation
import kotlin.coroutines.intrinsics.IntrinsicsKt
import kotlin.jvm.functions.Function2
import kotlin.jvm.internal.Intrinsics
import kotlin.jvm.internal.StringCompanionObject
import kotlinx.coroutines.BuildersKt
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.TimeoutKt
import org.jetbrains.annotations.NotNull
import org.jetbrains.annotations.Nullable
import org.jsoup.Connection.Response
import org.jsoup.nodes.Entities
import org.mozilla.javascript.NativeObject

public class AnalyzeRule(ruleData: RuleDataInterface, source: BaseSource? = null, debugLog: DebugLog? = null) : JsExtensions {
   private final var analyzeByJSonPath: AnalyzeByJSonPath?
   private final var analyzeByJSoup: AnalyzeByJSoup?
   private final var analyzeByXPath: AnalyzeByXPath?

   public final var baseUrl: String?
      private set

   public final val book: BaseBook?
      public final get() {
         return this.ruleData as? BaseBook;
      }

   public final var chapter: BookChapter?
      internal set

   public final var content: Any?
      private set

   public final var debugLog: DebugLog?
      internal set

   private final var isJSON: Boolean
   private final var isRegex: Boolean

   public final var nextChapterUrl: String?
      internal set

   private final var objectChangedJP: Boolean
   private final var objectChangedJS: Boolean
   private final var objectChangedXP: Boolean

   public final var redirectUrl: URL?
      private set

   public final var ruleData: RuleDataInterface
      internal set

   private final val source: BaseSource?

   init {
      this.ruleData = ruleData;
      this.source = source;
      this.debugLog = debugLog;
   }

   public override fun getUserNameSpace(): String {
      return this.ruleData.getUserNameSpace();
   }

   public override fun getSource(): BaseSource? {
      return this.source;
   }

   public override fun getLogger(): DebugLog? {
      return this.debugLog;
   }

   @JvmOverloads
   public fun setContent(content: Any?, baseUrl: String? = null): AnalyzeRule {
      if (content == null) {
         throw new AssertionError("内容不可空（Content cannot be null）");
      } else {
         this.content = content;
         this.isJSON = StringExtensionsKt.isJson(content.toString());
         this.setBaseUrl(baseUrl);
         this.objectChangedXP = true;
         this.objectChangedJS = true;
         this.objectChangedJP = true;
         return this;
      }
   }

   public fun setBaseUrl(baseUrl: String?): AnalyzeRule {
      if (baseUrl != null) {
         this.baseUrl = baseUrl;
      }

      return this;
   }

   public fun setRedirectUrl(url: String): URL? {
      try {
         this.redirectUrl = new URL(url);
      } catch (var3: Exception) {
         this.log("URL($url) error\n${var3.getLocalizedMessage()}");
      }

      return this.redirectUrl;
   }

   private fun getAnalyzeByXPath(o: Any): AnalyzeByXPath {
      val var10000: AnalyzeByXPath;
      if (!(o == this.content)) {
         var10000 = new AnalyzeByXPath(o);
      } else {
         if (this.analyzeByXPath == null || this.objectChangedXP) {
            val var10003: Any = this.content;
            this.analyzeByXPath = new AnalyzeByXPath(var10003);
            this.objectChangedXP = false;
         }

         var10000 = this.analyzeByXPath;
      }

      return var10000;
   }

   private fun getAnalyzeByJSoup(o: Any): AnalyzeByJSoup {
      val var10000: AnalyzeByJSoup;
      if (!(o == this.content)) {
         var10000 = new AnalyzeByJSoup(o);
      } else {
         if (this.analyzeByJSoup == null || this.objectChangedJS) {
            val var10003: Any = this.content;
            this.analyzeByJSoup = new AnalyzeByJSoup(var10003);
            this.objectChangedJS = false;
         }

         var10000 = this.analyzeByJSoup;
      }

      return var10000;
   }

   private fun getAnalyzeByJSonPath(o: Any): AnalyzeByJSonPath {
      val var10000: AnalyzeByJSonPath;
      if (!(o == this.content)) {
         var10000 = new AnalyzeByJSonPath(o);
      } else {
         if (this.analyzeByJSonPath == null || this.objectChangedJP) {
            val var10003: Any = this.content;
            this.analyzeByJSonPath = new AnalyzeByJSonPath(var10003);
            this.objectChangedJP = false;
         }

         var10000 = this.analyzeByJSonPath;
      }

      return var10000;
   }

   @JvmOverloads
   public fun getStringList(rule: String?, mContent: Any? = null, isUrl: Boolean = false): List<String>? {
      return if (rule as java.lang.CharSequence == null || rule.length() == 0) null else this.getStringList(this.splitSourceRule(rule, false), mContent, isUrl);
   }

   @JvmOverloads
   public fun getStringList(ruleList: List<io.legado.app.model.analyzeRule.AnalyzeRule.SourceRule>, mContent: Any? = null, isUrl: Boolean = false): List<
         String
      >? {
      var result: Any = null;
      val content: Any = if (mContent == null) this.content else mContent;
      if ((if (mContent == null) this.content else mContent) != null && !ruleList.isEmpty()) {
         result = content;
         if (content is NativeObject) {
            val var17: Any = (content as NativeObject).get((ruleList.get(0) as AnalyzeRule.SourceRule).getRule$reader_pro());
            result = if (var17 == null) null else var17.toString();
         } else {
            for (AnalyzeRule.SourceRule sourceRule : ruleList) {
               this.putRule(var21.getPutMap$reader_pro());
               var21.makeUpRule(result);
               if (result != null) {
                  if (var21.getRule$reader_pro().length() > 0) {
                     var var10000: Any;
                     switch (AnalyzeRule.WhenMappings.$EnumSwitchMapping$0[sourceRule.getMode$reader_pro().ordinal()]) {
                        case 1:
                           var10000 = this.evalJS(var21.getRule$reader_pro(), result);
                           break;
                        case 2:
                           var10000 = this.getAnalyzeByJSonPath(result).getStringList$reader_pro(var21.getRule$reader_pro());
                           break;
                        case 3:
                           var10000 = this.getAnalyzeByXPath(result).getStringList$reader_pro(var21.getRule$reader_pro());
                           break;
                        case 4:
                           var10000 = this.getAnalyzeByJSoup(result).getStringList$reader_pro(var21.getRule$reader_pro());
                           break;
                        default:
                           var10000 = var21.getRule$reader_pro();
                     }

                     result = var10000;
                  }

                  if (var21.getReplaceRegex$reader_pro().length() > 0 && result is java.util.List) {
                     val var28: ArrayList = new ArrayList();

                     for (Object item : (java.util.List)result) {
                        var28.add(this.replaceRegex(java.lang.String.valueOf(item), var21));
                     }

                     result = var28;
                  } else if (var21.getReplaceRegex$reader_pro().length() > 0) {
                     result = this.replaceRegex(java.lang.String.valueOf(result), var21);
                  }
               }
            }
         }
      }

      if (result == null) {
         return null;
      } else {
         if (result is java.lang.String) {
            result = StringsKt.split$default(result as java.lang.String, new java.lang.String[]{"\n"}, false, 0, 6, null);
         }

         if (!isUrl) {
            return result as? java.util.List;
         } else {
            val var20: ArrayList = new ArrayList();
            if (result is java.util.List) {
               for (Object url : (java.util.List)result) {
                  val absoluteURL: java.lang.String = NetworkUtils.INSTANCE.getAbsoluteURL(this.redirectUrl, java.lang.String.valueOf(url));
                  if (absoluteURL.length() > 0 && !var20.contains(absoluteURL)) {
                     var20.add(absoluteURL);
                  }
               }
            }

            return var20;
         }
      }
   }

   @JvmOverloads
   public fun getString(ruleStr: String?, mContent: Any? = null, isUrl: Boolean = false): String {
      return if (TextUtils.isEmpty(ruleStr)) "" else this.getString(splitSourceRule$default(this, ruleStr, false, 2, null), mContent, isUrl);
   }

   @JvmOverloads
   public fun getString(ruleList: List<io.legado.app.model.analyzeRule.AnalyzeRule.SourceRule>, mContent: Any? = null, isUrl: Boolean = false): String {
      var result: Any = null;
      val content: Any = if (mContent == null) this.content else mContent;
      if ((if (mContent == null) this.content else mContent) != null && !ruleList.isEmpty()) {
         result = content;
         if (content is NativeObject) {
            val var17: Any = (content as NativeObject).get((ruleList.get(0) as AnalyzeRule.SourceRule).getRule$reader_pro());
            result = if (var17 == null) null else var17.toString();
         } else {
            for (AnalyzeRule.SourceRule sourceRule : ruleList) {
               this.putRule(var20.getPutMap$reader_pro());
               var20.makeUpRule(result);
               if (result != null) {
                  if (!StringsKt.isBlank(var20.getRule$reader_pro()) || var20.getReplaceRegex$reader_pro().length() == 0) {
                     var var10000: Any;
                     switch (AnalyzeRule.WhenMappings.$EnumSwitchMapping$0[sourceRule.getMode$reader_pro().ordinal()]) {
                        case 1:
                           var10000 = this.evalJS(var20.getRule$reader_pro(), result);
                           break;
                        case 2:
                           var10000 = this.getAnalyzeByJSonPath(result).getString(var20.getRule$reader_pro());
                           break;
                        case 3:
                           var10000 = this.getAnalyzeByXPath(result).getString(var20.getRule$reader_pro());
                           break;
                        case 4:
                           var10000 = if (isUrl)
                              this.getAnalyzeByJSoup(result).getString0$reader_pro(var20.getRule$reader_pro())
                              else
                              this.getAnalyzeByJSoup(result).getString$reader_pro(var20.getRule$reader_pro());
                           break;
                        default:
                           var10000 = var20.getRule$reader_pro();
                     }

                     result = var10000;
                  }

                  if (result != null && var20.getReplaceRegex$reader_pro().length() > 0) {
                     result = this.replaceRegex(java.lang.String.valueOf(result), var20);
                  }
               }
            }
         }
      }

      if (result == null) {
         result = "";
      }

      var var8: Any;
      try {
         var8 = Result.Companion;
         var8 = Result.constructor-impl(Entities.unescape(java.lang.String.valueOf(result)));
      } catch (var16: java.lang.Throwable) {
         val var32: Result.Companion = Result.Companion;
         var8 = Result.constructor-impl(ResultKt.createFailure(var16));
      }

      val var46: java.lang.Throwable = Result.exceptionOrNull-impl(var8);
      if (var46 != null) {
         this.log(Intrinsics.stringPlus("Entities.unescape() error\n", var46.getLocalizedMessage()));
      }

      val var19: java.lang.String = (if (Result.exceptionOrNull-impl(var8) == null) var8 else java.lang.String.valueOf(result)) as java.lang.String;
      if (isUrl) {
         return if (StringsKt.isBlank(var19))
            (if (this.baseUrl == null) "" else this.baseUrl)
            else
            NetworkUtils.INSTANCE.getAbsoluteURL(this.redirectUrl, var19);
      } else {
         return var19;
      }
   }

   public fun getElement(ruleStr: String): Any? {
      if (TextUtils.isEmpty(ruleStr)) {
         return null;
      } else {
         var result: Any = null;
         val content: Any = this.content;
         val ruleList: java.util.List = this.splitSourceRule(ruleStr, true);
         if (content != null && !ruleList.isEmpty()) {
            result = content;

            for (AnalyzeRule.SourceRule sourceRule : ruleList) {
               this.putRule(var17.getPutMap$reader_pro());
               var17.makeUpRule(result);
               if (result != null) {
                  var var20: Any;
                  switch (AnalyzeRule.WhenMappings.$EnumSwitchMapping$0[sourceRule.getMode$reader_pro().ordinal()]) {
                     case 1:
                        var20 = this.evalJS(var17.getRule$reader_pro(), result);
                        break;
                     case 2:
                        var20 = this.getAnalyzeByJSonPath(result).getObject$reader_pro(var17.getRule$reader_pro());
                        break;
                     case 3:
                        var20 = this.getAnalyzeByXPath(result).getElements$reader_pro(var17.getRule$reader_pro());
                        break;
                     case 4:
                     default:
                        var20 = this.getAnalyzeByJSoup(result).getElements$reader_pro(var17.getRule$reader_pro());
                        break;
                     case 5:
                        var20 = AnalyzeByRegex.getElement$default(
                           AnalyzeByRegex.INSTANCE,
                           java.lang.String.valueOf(result),
                           StringExtensionsKt.splitNotBlank(var17.getRule$reader_pro(), "&&"),
                           0,
                           4,
                           null
                        );
                  }

                  result = var20;
                  if (var17.getReplaceRegex$reader_pro().length() > 0) {
                     result = this.replaceRegex(java.lang.String.valueOf(var20), var17);
                  }
               }
            }
         }

         return result;
      }
   }

   public fun getElements(ruleStr: String): List<Any> {
      label48: {
         var result: Any = null;
         val content: Any = this.content;
         val ruleList: java.util.List = this.splitSourceRule(ruleStr, true);
         if (content != null && !ruleList.isEmpty()) {
            result = content;

            for (AnalyzeRule.SourceRule sourceRule : ruleList) {
               this.putRule(var17.getPutMap$reader_pro());
               if (result != null) {
                  var var21: Any;
                  switch (AnalyzeRule.WhenMappings.$EnumSwitchMapping$0[sourceRule.getMode$reader_pro().ordinal()]) {
                     case 1:
                        var21 = this.evalJS(var17.getRule$reader_pro(), result);
                        break;
                     case 2:
                        var21 = this.getAnalyzeByJSonPath(result).getList$reader_pro(var17.getRule$reader_pro());
                        break;
                     case 3:
                        var21 = this.getAnalyzeByXPath(result).getElements$reader_pro(var17.getRule$reader_pro());
                        break;
                     case 4:
                     default:
                        var21 = this.getAnalyzeByJSoup(result).getElements$reader_pro(var17.getRule$reader_pro());
                        break;
                     case 5:
                        var21 = AnalyzeByRegex.getElements$default(
                           AnalyzeByRegex.INSTANCE,
                           java.lang.String.valueOf(result),
                           StringExtensionsKt.splitNotBlank(var17.getRule$reader_pro(), "&&"),
                           0,
                           4,
                           null
                        );
                  }

                  result = var21;
                  if (var17.getReplaceRegex$reader_pro().length() > 0) {
                     result = this.replaceRegex(java.lang.String.valueOf(var21), var17);
                  }
               }
            }
         }

         return (java.util.List<Object>)(if (result == null) new ArrayList<>() else result as java.util.List);
      }
   }

   private fun putRule(map: Map<String, String>) {
      for (Entry var3 : map.entrySet()) {
         this.put(var3.getKey() as java.lang.String, getString$default(this, var3.getValue() as java.lang.String, null, false, 6, null));
      }
   }

   private fun splitPutRule(ruleStr: String, putMap: HashMap<String, String>): String {
      var vRuleStr: java.lang.String = ruleStr;
      val putMatcher: Matcher = putPattern.matcher(ruleStr);

      while (putMatcher.find()) {
         val var5: java.lang.String = putMatcher.group();
         vRuleStr = StringsKt.replace$default(vRuleStr, var5, "", false, 4, null);
         val `$this$fromJsonObject$iv`: Gson = GsonExtensionsKt.getGSON();
         val `json$iv`: java.lang.String = putMatcher.group(1);

         var var10: Any;
         try {
            var10 = Result.Companion;
            val var24: Type = new AnalyzeRule$splitPutRule$$inlined$fromJsonObject$1().getType();
            var var10000: Any = `$this$fromJsonObject$iv`.fromJson(`json$iv`, var24);
            if (var10000 !is java.util.Map) {
               var10000 = null;
            }

            var10 = Result.constructor-impl(var10000 as java.util.Map);
         } catch (var14: java.lang.Throwable) {
            val `$i$f$genericType`: Result.Companion = Result.Companion;
            var10 = Result.constructor-impl(ResultKt.createFailure(var14));
         }

         val var15: java.util.Map = (if (Result.isFailure-impl(var10)) null else var10) as java.util.Map;
         if (var15 != null) {
            putMap.putAll(var15);
         }
      }

      return vRuleStr;
   }

   private fun replaceRegex(result: String, rule: io.legado.app.model.analyzeRule.AnalyzeRule.SourceRule): String {
      if (rule.getReplaceRegex$reader_pro().length() == 0) {
         return result;
      } else {
         val var17: java.lang.String = result;
         var var53: java.lang.String;
         if (rule.getReplaceFirst$reader_pro()) {
            var var5: Any;
            try {
               var5 = Result.Companion;
               val var39: Matcher = Pattern.compile(rule.getReplaceRegex$reader_pro()).matcher(var17);
               if (var39.find()) {
                  var53 = var39.group(0);
                  var53 = new Regex(rule.getReplaceRegex$reader_pro()).replaceFirst(var53, rule.getReplacement$reader_pro());
               } else {
                  var53 = "";
               }

               var5 = Result.constructor-impl(var53);
            } catch (var15: java.lang.Throwable) {
               val it: Result.Companion = Result.Companion;
               var5 = Result.constructor-impl(ResultKt.createFailure(var15));
            }

            var53 = (
               if (Result.exceptionOrNull-impl(var5) == null)
                  var5
                  else
                  StringsKt.replaceFirst$default(result, rule.getReplaceRegex$reader_pro(), rule.getReplacement$reader_pro(), false, 4, null)
            ) as java.lang.String;
         } else {
            var var24: Any;
            try {
               var24 = Result.Companion;
               var24 = Result.constructor-impl(new Regex(rule.getReplaceRegex$reader_pro()).replace(var17, rule.getReplacement$reader_pro()));
            } catch (var14: java.lang.Throwable) {
               val var36: Result.Companion = Result.Companion;
               var24 = Result.constructor-impl(ResultKt.createFailure(var14));
            }

            var53 = (
               if (Result.exceptionOrNull-impl(var24) == null)
                  var24
                  else
                  StringsKt.replace$default(result, rule.getReplaceRegex$reader_pro(), rule.getReplacement$reader_pro(), false, 4, null)
            ) as java.lang.String;
         }

         return var53;
      }
   }

   public fun splitSourceRule(ruleStr: String?, allInOne: Boolean = false): List<io.legado.app.model.analyzeRule.AnalyzeRule.SourceRule> {
      if (ruleStr == null || ruleStr.length() == 0) {
         return CollectionsKt.emptyList();
      } else {
         val var18: ArrayList = new ArrayList();
         var var19: AnalyzeRule.Mode = AnalyzeRule.Mode.Default;
         var var20: Int = 0;
         if (allInOne && StringsKt.startsWith$default(ruleStr, ":", false, 2, null)) {
            var19 = AnalyzeRule.Mode.Regex;
            this.isRegex = true;
            var20 = 1;
         } else if (this.isRegex) {
            var19 = AnalyzeRule.Mode.Regex;
         }

         for (Matcher jsMatcher = AppPattern.INSTANCE.getJS_PATTERN().matcher(ruleStr); jsMatcher.find(); start = jsMatcher.end()) {
            if (jsMatcher.start() > var20) {
               val `$i$f$trim`: Int = jsMatcher.start();
               if (ruleStr == null) {
                  throw new NullPointerException("null cannot be cast to non-null type java.lang.String");
               }

               val var10000: java.lang.String = ruleStr.substring(var20, `$i$f$trim`);
               val var33: java.lang.CharSequence = var10000;
               var `startIndex$iv$iv`: Int = 0;
               var `endIndex$iv$iv`: Int = var33.length() - 1;
               var `startFound$iv$iv`: Boolean = false;

               while (startIndex$iv$iv <= endIndex$iv$iv) {
                  val var40: Boolean = Intrinsics.compare(var33.charAt(if (!`startFound$iv$iv`) `startIndex$iv$iv` else `endIndex$iv$iv`), 32) <= 0;
                  if (!`startFound$iv$iv`) {
                     if (!var40) {
                        `startFound$iv$iv` = true;
                     } else {
                        `startIndex$iv$iv`++;
                     }
                  } else {
                     if (!var40) {
                        break;
                     }

                     `endIndex$iv$iv`--;
                  }
               }

               val var21: java.lang.String = var33.subSequence(`startIndex$iv$iv`, `endIndex$iv$iv` + 1).toString();
               if (var21.length() > 0) {
                  var18.add(new AnalyzeRule.SourceRule(this, var21, var19));
               }
            }

            val var29: java.lang.String = jsMatcher.group(2);
            val var24: java.lang.String = if (var29 == null) jsMatcher.group(1) else var29;
            var18.add(new AnalyzeRule.SourceRule(this, var24, AnalyzeRule.Mode.Js));
         }

         if (ruleStr.length() > var20) {
            if (ruleStr == null) {
               throw new NullPointerException("null cannot be cast to non-null type java.lang.String");
            }

            val var44: java.lang.String = ruleStr.substring(var20);
            val var34: java.lang.CharSequence = var44;
            var var36: Int = 0;
            var var37: Int = var34.length() - 1;
            var var38: Boolean = false;

            while (startIndex$iv$iv <= endIndex$iv$iv) {
               val var42: Boolean = Intrinsics.compare(var34.charAt(if (!var38) var36 else var37), 32) <= 0;
               if (!var38) {
                  if (!var42) {
                     var38 = true;
                  } else {
                     var36++;
                  }
               } else {
                  if (!var42) {
                     break;
                  }

                  var37--;
               }
            }

            val var22: java.lang.String = var34.subSequence(var36, var37 + 1).toString();
            if (var22.length() > 0) {
               var18.add(new AnalyzeRule.SourceRule(this, var22, var19));
            }
         }

         return var18;
      }
   }

   public fun put(key: String, value: String): String {
      var var10000: Unit;
      if (this.chapter == null) {
         var10000 = null;
      } else {
         this.chapter.putVariable(key, value);
         var10000 = Unit.INSTANCE;
      }

      if (var10000 == null) {
         val var6: BaseBook = this.getBook();
         if (var6 == null) {
            var10000 = null;
         } else {
            var6.putVariable(key, value);
            var10000 = Unit.INSTANCE;
         }
      } else {
         var10000 = var10000;
      }

      if (var10000 == null) {
         this.ruleData.putVariable(key, value);
      }

      return value;
   }

   public fun get(key: String): String {
      if (key == "bookName") {
         val var3: BaseBook = this.getBook();
         if (var3 != null) {
            return var3.getName();
         }
      } else if (key == "title" && this.chapter != null) {
         return this.chapter.getTitle();
      }

      val var2: java.lang.String = if (this.chapter == null) null else this.chapter.getVariable(key);
      val var10000: java.lang.String;
      if (var2 == null) {
         val var4: BaseBook = this.getBook();
         val var11: java.lang.String = if (var4 == null) null else var4.getVariable(key);
         if (var11 == null) {
            if (this.ruleData == null) {
               var10000 = "";
            } else {
               val var13: java.lang.String = this.ruleData.getVariable(key);
               var10000 = if (var13 == null) "" else var13;
            }
         } else {
            var10000 = var11;
         }
      } else {
         var10000 = var2;
      }

      return var10000;
   }

   public fun evalJS(jsStr: String, result: Any? = null): Any? {
      val bindings: SimpleBindings = new SimpleBindings();
      bindings.put("java", this);
      bindings.put("cookie", new CookieStore(this.getUserNameSpace()));
      bindings.put("cache", new CacheManager(this.getUserNameSpace()));
      bindings.put("source", this.source);
      bindings.put("book", this.getBook());
      bindings.put("result", result);
      bindings.put("baseUrl", this.baseUrl);
      bindings.put("chapter", this.chapter);
      bindings.put("title", if (this.chapter == null) null else this.chapter.getTitle());
      bindings.put("src", this.content);
      bindings.put("nextChapterUrl", this.nextChapterUrl);
      return AppConst.INSTANCE.getSCRIPT_ENGINE().eval(jsStr, bindings);
   }

   public override fun ajax(urlStr: String): String? {
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
                  var it: Any;
                  label42: {
                     val var13: Any = IntrinsicsKt.getCOROUTINE_SUSPENDED();
                     var var10000: Any;
                     switch (this.label) {
                        case 0:
                           ResultKt.throwOnFailure(`$result`);
                           val var2: java.lang.String = this.$urlStr;
                           val var3: AnalyzeRule = this.this$0;

                           try {
                              it = Result.Companion;
                              val var31: AnalyzeUrl = new AnalyzeUrl(
                                 var2,
                                 null,
                                 null,
                                 null,
                                 null,
                                 null,
                                 AnalyzeRule.access$getSource$p(var3),
                                 var3.getBook(),
                                 null,
                                 null,
                                 var3.getDebugLog(),
                                 830,
                                 null
                              );
                              this.label = 1;
                              var10000 = AnalyzeUrl.getStrResponseAwait$default(var31, null, null, false, this, 7, null);
                           } catch (var15: java.lang.Throwable) {
                              val var30: Result.Companion = Result.Companion;
                              it = Result.constructor-impl(ResultKt.createFailure(var15));
                              break label42;
                           }

                           if (var10000 === var13) {
                              return var13;
                           }
                           break;
                        case 1:
                           try {
                              ResultKt.throwOnFailure(`$result`);
                              var10000 = `$result`;
                              break;
                           } catch (var16: java.lang.Throwable) {
                              val analyzeUrl: Result.Companion = Result.Companion;
                              it = Result.constructor-impl(ResultKt.createFailure(var16));
                              break label42;
                           }
                        default:
                           throw new IllegalStateException("call to 'resume' before 'invoke' with coroutine");
                     }

                     try {
                        it = Result.constructor-impl((var10000 as StrResponse).getBody());
                     } catch (var14: java.lang.Throwable) {
                        val var32: Result.Companion = Result.Companion;
                        it = Result.constructor-impl(ResultKt.createFailure(var14));
                     }
                  }

                  val var18: AnalyzeRule = this.this$0;
                  val var20: java.lang.String = this.$urlStr;
                  val var38: java.lang.Throwable = Result.exceptionOrNull-impl(it);
                  if (var38 != null) {
                     var18.log("ajax($var20) error\n${ExceptionsKt.stackTraceToString(var38)}");
                  }

                  val var22: java.lang.Throwable = Result.exceptionOrNull-impl(it);
                  return if (var22 == null) it else ThrowableExtensionsKt.getMsg(var22);
               }

               @NotNull
               @Override
               public final Continuation<Unit> create(@Nullable Object value, @NotNull Continuation<?> $completion) {
                  return new <anonymous constructor>(this.$urlStr, this.this$0, `$completion`);
               }

               @Nullable
               public final Object invoke(@NotNull CoroutineScope p1, @Nullable Continuation<? super java.lang.String> p2) {
                  return (this.create(p1, p2) as SyntheticContinuation).invokeSuspend(Unit.INSTANCE);
               }
            }
         ) as Function2,
         1,
         null
      ) as java.lang.String;
   }

   public fun toNumChapter(s: String?): String? {
      if (s == null) {
         return null;
      } else {
         val matcher: Matcher = titleNumPattern.matcher(s);
         return if (matcher.find()) "${matcher.group(1)}${StringUtils.INSTANCE.stringToInt(matcher.group(2))}${matcher.group(3)}" else s;
      }
   }

   public fun reGetBook() {
      val bookSource: BookSource = this.source as? BookSource;
      val var3: BaseBook = this.getBook();
      val var4: Book = var3 as? Book;
      if (bookSource != null && (var3 as? Book) != null) {
         BuildersKt.runBlocking$default(
            null,
            (
               new Function2<CoroutineScope, Continuation<? super Book>, Object>(bookSource, this, var4, null) {
                  int label;

                  {
                     super(2, `$completionx`);
                     this.$bookSource = `$bookSource`;
                     this.this$0 = `$receiver`;
                     this.$book = `$book`;
                  }

                  @Nullable
                  @Override
                  public final Object invokeSuspend(@NotNull Object $result) {
                     val var2: Any = IntrinsicsKt.getCOROUTINE_SUSPENDED();
                     var var10000: Any;
                     switch (this.label) {
                        case 0:
                           ResultKt.throwOnFailure(`$result`);
                           val var10001: Function2 = (
                              new Function2<CoroutineScope, Continuation<? super Book>, Object>(this.$bookSource, this.this$0, this.$book, null) {
                                 int label;

                                 {
                                    super(2, `$completionx`);
                                    this.$bookSource = `$bookSource`;
                                    this.this$0 = `$receiver`;
                                    this.$book = `$book`;
                                 }

                                 @Nullable
                                 @Override
                                 public final Object invokeSuspend(@NotNull Object $result) {
                                    val var16: Any = IntrinsicsKt.getCOROUTINE_SUSPENDED();
                                    var var18: Any;
                                    switch (this.label) {
                                       case 0:
                                          ResultKt.throwOnFailure(`$result`);
                                          var18 = new WebBook(this.$bookSource, false, null, this.this$0.getUserNameSpace(), 6, null);
                                          val var10001: java.lang.String = this.$book.getName();
                                          val var10002: java.lang.String = this.$book.getAuthor();
                                          val var10003: Continuation = this;
                                          this.label = 1;
                                          var18 = (WebBook)var18.preciseSearch-0E7RQCE(var10001, var10002, var10003);
                                          if (var18 === var16) {
                                             return var16;
                                          }
                                          break;
                                       case 1:
                                          ResultKt.throwOnFailure(`$result`);
                                          var18 = (WebBook)(`$result` as Result).unbox-impl();
                                          break;
                                       case 2:
                                          ResultKt.throwOnFailure(`$result`);
                                          return `$result`;
                                       default:
                                          throw new IllegalStateException("call to 'resume' before 'invoke' with coroutine");
                                    }

                                    ResultKt.throwOnFailure(var18);
                                    val var17: Book = this.$book;
                                    val it: Book = var18 as Book;
                                    this.$book.setBookUrl((var18 as Book).getBookUrl());

                                    for (Entry element$iv : it.getVariableMap().entrySet()) {
                                       var17.putVariable(`element$iv`.getKey() as java.lang.String, `element$iv`.getValue() as java.lang.String);
                                    }

                                    var18 = new WebBook(this.$bookSource, false, null, this.this$0.getUserNameSpace(), 6, null);
                                    val var21: Book = this.$book;
                                    val var22: Continuation = this;
                                    this.label = 2;
                                    var18 = (WebBook)var18.getBookInfo(var21, false, var22);
                                    return if (var18 === var16) var16 else var18;
                                 }

                                 @NotNull
                                 @Override
                                 public final Continuation<Unit> create(@Nullable Object value, @NotNull Continuation<?> $completion) {
                                    return new <anonymous constructor>(this.$bookSource, this.this$0, this.$book, `$completion`);
                                 }

                                 @Nullable
                                 public final Object invoke(@NotNull CoroutineScope p1, @Nullable Continuation<? super Book> p2) {
                                    return (this.create(p1, p2) as SyntheticContinuation).invokeSuspend(Unit.INSTANCE);
                                 }
                              }
                           ) as Function2;
                           val var10002: Continuation = this;
                           this.label = 1;
                           var10000 = TimeoutKt.withTimeout(1800000L, var10001, var10002);
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

                     return var10000;
                  }

                  @NotNull
                  @Override
                  public final Continuation<Unit> create(@Nullable Object value, @NotNull Continuation<?> $completion) {
                     return new <anonymous constructor>(this.$bookSource, this.this$0, this.$book, `$completion`);
                  }

                  @Nullable
                  public final Object invoke(@NotNull CoroutineScope p1, @Nullable Continuation<? super Book> p2) {
                     return (this.create(p1, p2) as SyntheticContinuation).invokeSuspend(Unit.INSTANCE);
                  }
               }
            ) as Function2,
            1,
            null
         );
      }
   }

   public fun refreshBookUrl() {
      BuildersKt.runBlocking$default(null, (new Function2<CoroutineScope, Continuation<? super Unit>, Object>(this, null) {
         Object L$0;
         int label;

         {
            super(2, `$completionx`);
            this.this$0 = `$receiver`;
         }

         @Nullable
         @Override
         public final Object invokeSuspend(@NotNull Object $result) {
            val var13: Any = IntrinsicsKt.getCOROUTINE_SUSPENDED();
            var book: Book;
            switch (this.label) {
               case 0:
                  ResultKt.throwOnFailure(`$result`);
                  val var14: BaseSource = AnalyzeRule.access$getSource$p(this.this$0);
                  val bookSource: BookSource = var14 as? BookSource;
                  val books: BaseBook = this.this$0.getBook();
                  book = books as? Book;
                  if (bookSource == null || (books as? Book) == null) {
                     return Unit.INSTANCE;
                  }

                  val var16: WebBook = new WebBook(bookSource, false, null, this.this$0.getUserNameSpace(), 6, null);
                  val var10001: java.lang.String = book.getName();
                  val var10003: Continuation = this;
                  this.L$0 = book;
                  this.label = 1;
                  if (WebBook.searchBook$default(var16, var10001, null, var10003, 2, null) === var13) {
                     return var13;
                  }
                  break;
               case 1:
                  book = this.L$0 as Book;
                  ResultKt.throwOnFailure(`$result`);
                  break;
               default:
                  throw new IllegalStateException("call to 'resume' before 'invoke' with coroutine");
            }

            val `$this$forEach$iv`: java.lang.Iterable;
            for (Object element$iv : $this$forEach$iv) {
               val it: SearchBook = `element$iv` as SearchBook;
               if ((`element$iv` as SearchBook).getName() == book.getName() && (`element$iv` as SearchBook).getAuthor() == book.getAuthor()) {
                  book.setBookUrl(it.getBookUrl());
                  if (!StringsKt.isBlank(it.getTocUrl())) {
                     book.setTocUrl(it.getTocUrl());
                  }

                  return Unit.INSTANCE;
               }
            }

            return Unit.INSTANCE;
         }

         @NotNull
         @Override
         public final Continuation<Unit> create(@Nullable Object value, @NotNull Continuation<?> $completion) {
            return new <anonymous constructor>(this.this$0, `$completion`);
         }

         @Nullable
         public final Object invoke(@NotNull CoroutineScope p1, @Nullable Continuation<? super Unit> p2) {
            return (this.create(p1, p2) as SyntheticContinuation).invokeSuspend(Unit.INSTANCE);
         }
      }) as Function2, 1, null);
   }

   public fun refreshTocUrl() {
      BuildersKt.runBlocking$default(null, (new Function2<CoroutineScope, Continuation<? super Unit>, Object>(this, null) {
         int label;

         {
            super(2, `$completionx`);
            this.this$0 = `$receiver`;
         }

         @Nullable
         @Override
         public final Object invokeSuspend(@NotNull Object $result) {
            val var5: Any = IntrinsicsKt.getCOROUTINE_SUSPENDED();
            switch (this.label) {
               case 0:
                  ResultKt.throwOnFailure(`$result`);
                  val book: BaseSource = AnalyzeRule.access$getSource$p(this.this$0);
                  val bookSource: BookSource = book as? BookSource;
                  val var4: BaseBook = this.this$0.getBook();
                  val var6: Book = var4 as? Book;
                  if (bookSource == null || (var4 as? Book) == null) {
                     return Unit.INSTANCE;
                  }

                  val var10000: WebBook = new WebBook(bookSource, false, null, this.this$0.getUserNameSpace(), 6, null);
                  val var10003: Continuation = this;
                  this.label = 1;
                  if (WebBook.getBookInfo$default(var10000, var6, false, var10003, 2, null) === var5) {
                     return var5;
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
            return new <anonymous constructor>(this.this$0, `$completion`);
         }

         @Nullable
         public final Object invoke(@NotNull CoroutineScope p1, @Nullable Continuation<? super Unit> p2) {
            return (this.create(p1, p2) as SyntheticContinuation).invokeSuspend(Unit.INSTANCE);
         }
      }) as Function2, 1, null);
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

   override fun get(urlStr: java.lang.String, headers: MutableMap<java.lang.String, java.lang.String>): Response {
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

   override fun head(urlStr: java.lang.String, headers: MutableMap<java.lang.String, java.lang.String>): Response {
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

   override fun post(urlStr: java.lang.String, body: java.lang.String, headers: MutableMap<java.lang.String, java.lang.String>): Response {
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
   fun setContent(content: Any?): AnalyzeRule {
      return setContent$default(this, content, null, 2, null);
   }

   @JvmOverloads
   fun getStringList(rule: java.lang.String?, mContent: Any?): MutableList<java.lang.String>? {
      return getStringList$default(this, rule, mContent, false, 4, null);
   }

   @JvmOverloads
   fun getStringList(rule: java.lang.String?): MutableList<java.lang.String>? {
      return getStringList$default(this, rule, null, false, 6, null);
   }

   @JvmOverloads
   fun getStringList(ruleList: MutableList<AnalyzeRule.SourceRule>, mContent: Any?): MutableList<java.lang.String>? {
      return getStringList$default(this, ruleList, mContent, false, 4, null);
   }

   @JvmOverloads
   fun getStringList(ruleList: MutableList<AnalyzeRule.SourceRule>): MutableList<java.lang.String>? {
      return getStringList$default(this, ruleList, null, false, 6, null);
   }

   @JvmOverloads
   fun getString(ruleStr: java.lang.String?, mContent: Any?): java.lang.String {
      return getString$default(this, ruleStr, mContent, false, 4, null);
   }

   @JvmOverloads
   fun getString(ruleStr: java.lang.String?): java.lang.String {
      return getString$default(this, ruleStr, null, false, 6, null);
   }

   @JvmOverloads
   fun getString(ruleList: MutableList<AnalyzeRule.SourceRule>, mContent: Any?): java.lang.String {
      return getString$default(this, ruleList, mContent, false, 4, null);
   }

   @JvmOverloads
   fun getString(ruleList: MutableList<AnalyzeRule.SourceRule>): java.lang.String {
      return getString$default(this, ruleList, null, false, 6, null);
   }

   public companion object {
      private final val evalPattern: Pattern
      private final val putPattern: Pattern
      private final val regexPattern: Pattern
      private final val titleNumPattern: Pattern
   }

   public enum class Mode {
      XPath,
      Json,
      Default,
      Js,
      Regex   }

   public inner class SourceRule internal constructor(ruleStr: String, mode: io.legado.app.model.analyzeRule.AnalyzeRule.Mode = ...) {
      private final val defaultRuleType: Int
      private final val getRuleType: Int
      private final val jsRuleType: Int
      internal final var mode: io.legado.app.model.analyzeRule.AnalyzeRule.Mode
      internal final val putMap: HashMap<String, String>
      internal final var replaceFirst: Boolean
      internal final var replaceRegex: String
      internal final var replacement: String
      internal final var rule: String
      private final val ruleParam: ArrayList<String>
      private final val ruleType: ArrayList<Int>

      init {
         this.this$0 = `this$0`;
         this.mode = mode;
         this.replaceRegex = "";
         this.replacement = "";
         this.putMap = new HashMap<>();
         this.ruleParam = new ArrayList<>();
         this.ruleType = new ArrayList<>();
         this.getRuleType = -2;
         this.jsRuleType = -1;
         var var10001: java.lang.String;
         if (this.mode === AnalyzeRule.Mode.Js || this.mode === AnalyzeRule.Mode.Regex) {
            var10001 = ruleStr;
         } else if (StringsKt.startsWith(ruleStr, "@CSS:", true)) {
            this.mode = AnalyzeRule.Mode.Default;
            var10001 = ruleStr;
         } else if (StringsKt.startsWith$default(ruleStr, "@@", false, 2, null)) {
            this.mode = AnalyzeRule.Mode.Default;
            var10001 = ruleStr.substring(2);
         } else if (StringsKt.startsWith(ruleStr, "@XPath:", true)) {
            this.mode = AnalyzeRule.Mode.XPath;
            var10001 = ruleStr.substring(7);
         } else if (StringsKt.startsWith(ruleStr, "@Json:", true)) {
            this.mode = AnalyzeRule.Mode.Json;
            var10001 = ruleStr.substring(6);
         } else if (AnalyzeRule.access$isJSON$p(this.this$0)
            || StringsKt.startsWith$default(ruleStr, "$.", false, 2, null)
            || StringsKt.startsWith$default(ruleStr, "$[", false, 2, null)) {
            this.mode = AnalyzeRule.Mode.Json;
            var10001 = ruleStr;
         } else if (StringsKt.startsWith$default(ruleStr, "/", false, 2, null)) {
            this.mode = AnalyzeRule.Mode.XPath;
            var10001 = ruleStr;
         } else {
            var10001 = ruleStr;
         }

         this.rule = var10001;
         this.rule = AnalyzeRule.access$splitPutRule(this.this$0, this.rule, this.putMap);
         var start: Int = 0;
         val var19: Matcher = AnalyzeRule.access$getEvalPattern$cp().matcher(this.rule);
         if (var19.find()) {
            var var7: java.lang.String = this.rule;
            var var8: Int = var19.start();
            if (var7 == null) {
               throw new NullPointerException("null cannot be cast to non-null type java.lang.String");
            }

            var var10000: java.lang.String = var7.substring(0, var8);
            if (this.mode != AnalyzeRule.Mode.Js
               && this.mode != AnalyzeRule.Mode.Regex
               && (var19.start() == 0 || !StringsKt.contains$default(var10000, "##", false, 2, null))) {
               this.mode = AnalyzeRule.Mode.Regex;
            }

            do {
               if (var19.start() > start) {
                  var7 = this.rule;
                  var8 = var19.start();
                  if (var7 == null) {
                     throw new NullPointerException("null cannot be cast to non-null type java.lang.String");
                  }

                  var10000 = var7.substring(start, var8);
                  this.splitRegex(var10000);
               }

               var7 = var19.group();
               if (StringsKt.startsWith(var7, "@get:", true)) {
                  this.ruleType.add(this.getRuleType);
                  val var32: ArrayList = this.ruleParam;
                  var10001 = var7.substring(6, StringsKt.getLastIndex(var7));
                  var32.add(var10001);
               } else if (StringsKt.startsWith$default(var7, "{{", false, 2, null)) {
                  this.ruleType.add(this.jsRuleType);
                  val var33: ArrayList = this.ruleParam;
                  var10001 = var7.substring(2, var7.length() - 2);
                  var33.add(var10001);
               } else {
                  this.splitRegex(var7);
               }

               start = var19.end();
            } while (evalMatcher.find());
         }

         if (this.rule.length() > start) {
            if (this.rule == null) {
               throw new NullPointerException("null cannot be cast to non-null type java.lang.String");
            }

            val var34: java.lang.String = this.rule.substring(start);
            this.splitRegex(var34);
         }
      }

      private fun splitRegex(ruleStr: String) {
         var start: Int = 0;
         val var12: Matcher = AnalyzeRule.access$getRegexPattern$cp()
            .matcher(StringsKt.split$default(ruleStr, new java.lang.String[]{"##"}, false, 0, 6, null).get(0) as java.lang.CharSequence);
         if (var12.find()) {
            if (this.mode != AnalyzeRule.Mode.Js && this.mode != AnalyzeRule.Mode.Regex) {
               this.mode = AnalyzeRule.Mode.Regex;
            }

            do {
               if (var12.start() > start) {
                  val var7: Int = var12.start();
                  if (ruleStr == null) {
                     throw new NullPointerException("null cannot be cast to non-null type java.lang.String");
                  }

                  val var18: java.lang.String = ruleStr.substring(start, var7);
                  this.ruleType.add(this.defaultRuleType);
                  this.ruleParam.add(var18);
               }

               val var6: java.lang.String = var12.group();
               val var19: ArrayList = this.ruleType;
               val var10001: java.lang.String = var6.substring(1);
               var19.add(Integer.parseInt(var10001));
               this.ruleParam.add(var6);
               start = var12.end();
            } while (regexMatcher.find());
         }

         if (ruleStr.length() > start) {
            if (ruleStr == null) {
               throw new NullPointerException("null cannot be cast to non-null type java.lang.String");
            }

            val var20: java.lang.String = ruleStr.substring(start);
            this.ruleType.add(this.defaultRuleType);
            this.ruleParam.add(var20);
         }
      }

      public fun makeUpRule(result: Any?) {
         val infoVal: StringBuilder = new StringBuilder();
         if (!this.ruleParam.isEmpty()) {
            var var18: Int = this.ruleParam.size();

            while (true) {
               var var20: Int = var18;
               var18 += -1;
               if (var20 <= 0) {
                  val var22: java.lang.String = infoVal.toString();
                  this.rule = var22;
                  break;
               }

               var jsEval: Any = this.ruleType.get(var18);
               var20 = (jsEval as java.lang.Number).intValue();
               if (var20 > this.defaultRuleType) {
                  val var6: java.util.List = result as? java.util.List;
                  val var10000: Unit;
                  if ((result as? java.util.List) == null) {
                     var10000 = null;
                  } else {
                     if (var6.size() > var20) {
                        val var12: java.lang.String = var6.get(var20) as java.lang.String;
                        if (var12 != null) {
                           infoVal.insert(0, var12);
                        }
                     }

                     var10000 = Unit.INSTANCE;
                  }

                  if (var10000 == null) {
                     infoVal.insert(0, this.ruleParam.get(var18));
                  }
               } else if (var20 == this.jsRuleType) {
                  jsEval = this.ruleParam.get(var18);
                  if (this.isRule(jsEval as java.lang.String)) {
                     val var40: AnalyzeRule = this.this$0;
                     jsEval = new AnalyzeRule.SourceRule[1];
                     val var10005: AnalyzeRule = this.this$0;
                     val var32: Any = this.ruleParam.get(var18);
                     ((Object[])jsEval)[0] = var10005.new SourceRule(var10005, var32 as java.lang.String, null, 2, null);
                     infoVal.insert(0, AnalyzeRule.getString$default(var40, CollectionsKt.arrayListOf((Object[])jsEval), null, false, 6, null));
                  } else {
                     val var41: AnalyzeRule = this.this$0;
                     var var34: Any = this.ruleParam.get(var18);
                     jsEval = var41.evalJS(var34 as java.lang.String, result);
                     if (jsEval != null) {
                        if (jsEval is java.lang.String) {
                           infoVal.insert(0, jsEval as java.lang.String);
                        } else if (jsEval is java.lang.Double && (jsEval as java.lang.Number).doubleValue() % 1.0 == 0.0) {
                           var34 = StringCompanionObject.INSTANCE;
                           val var37: Array<Any> = new Object[]{jsEval};
                           val var10002: java.lang.String = java.lang.String.format("%.0f", Arrays.copyOf(var37, var37.length));
                           infoVal.insert(0, var10002);
                        } else {
                           infoVal.insert(0, jsEval.toString());
                        }
                     }
                  }
               } else if (var20 == this.getRuleType) {
                  val var43: AnalyzeRule = this.this$0;
                  jsEval = this.ruleParam.get(var18);
                  infoVal.insert(0, var43.get(jsEval as java.lang.String));
               } else {
                  infoVal.insert(0, this.ruleParam.get(var18));
               }
            }
         }

         val var19: java.util.List = StringsKt.split$default(this.rule, new java.lang.String[]{"##"}, false, 0, 6, null);
         val var24: java.lang.String = var19.get(0) as java.lang.String;
         if (var24 == null) {
            throw new NullPointerException("null cannot be cast to non-null type kotlin.CharSequence");
         } else {
            this.rule = StringsKt.trim(var24).toString();
            if (var19.size() > 1) {
               this.replaceRegex = var19.get(1) as java.lang.String;
            }

            if (var19.size() > 2) {
               this.replacement = var19.get(2) as java.lang.String;
            }

            if (var19.size() > 3) {
               this.replaceFirst = true;
            }
         }
      }

      private fun isRule(ruleStr: String): Boolean {
         return StringsKt.startsWith$default(ruleStr, '@', false, 2, null)
            || StringsKt.startsWith$default(ruleStr, "$.", false, 2, null)
            || StringsKt.startsWith$default(ruleStr, "$[", false, 2, null)
            || StringsKt.startsWith$default(ruleStr, "//", false, 2, null);
      }
   }
}
