package io.legado.app.help

import com.google.gson.Gson
import com.jayway.jsonpath.DocumentContext
import com.jayway.jsonpath.JsonPath
import com.jayway.jsonpath.Predicate
import com.jayway.jsonpath.ReadContext
import io.legado.app.data.entities.BookSource
import io.legado.app.data.entities.rule.BookInfoRule
import io.legado.app.data.entities.rule.ContentRule
import io.legado.app.data.entities.rule.ExploreRule
import io.legado.app.data.entities.rule.SearchRule
import io.legado.app.data.entities.rule.TocRule
import io.legado.app.exception.NoStackTraceException
import io.legado.app.model.Debug
import io.legado.app.model.DebugLog.DefaultImpls
import io.legado.app.utils.GsonExtensionsKt
import io.legado.app.utils.JsonExtensionsKt
import io.legado.app.utils.StringExtensionsKt
import java.io.InputStream
import java.lang.reflect.Type
import java.util.ArrayList
import java.util.HashMap
import java.util.regex.Matcher
import java.util.regex.Pattern
import kotlin.Result.Companion
import kotlin.jvm.internal.Intrinsics

public object SourceAnalyzer {
   private final val headerPattern: Pattern = Pattern.compile("@Header:\\{.+?\\}", 2)
   private final val jsPattern: Pattern = Pattern.compile("\\{\\{.+?\\}\\}", 2)

   public fun jsonToBookSources(json: String): Result<MutableList<BookSource>> {
      var var3: Any;
      try {
         var3 = Result.Companion;
         val var21: java.util.List = new ArrayList();
         if (StringExtensionsKt.isJsonArray(json)) {
            val var23: Any = JsonExtensionsKt.getJsonPath().parse(json).read("$", new Predicate[0]);

            for (java.util.Map item : (java.util.List)var23) {
               val var26: DocumentContext = JsonExtensionsKt.getJsonPath().parse(var25);
               val var10000: SourceAnalyzer = INSTANCE;
               var var27: java.lang.String = var26.jsonString();
               var27 = (java.lang.String)var10000.jsonToBookSource-IoAF18A(var27);
               ResultKt.throwOnFailure(var27);
               var21.add(var27 as BookSource);
            }
         } else {
            if (!StringExtensionsKt.isJsonObject(json)) {
               throw new NoStackTraceException("格式不对");
            }

            val var18: Any = INSTANCE.jsonToBookSource-IoAF18A(json);
            ResultKt.throwOnFailure(var18);
            var21.add(var18 as BookSource);
         }

         var3 = Result.constructor-impl(var21);
      } catch (var15: java.lang.Throwable) {
         val items: Companion = Result.Companion;
         var3 = Result.constructor-impl(ResultKt.createFailure(var15));
      }

      return var3;
   }

   public fun jsonToBookSources(inputStream: InputStream): Result<MutableList<BookSource>> {
      var var3: Any;
      try {
         var3 = Result.Companion;
         val var29: java.util.List = new ArrayList();

         var var7: Any;
         try {
            var7 = Result.Companion;
            val var34: Any = JsonExtensionsKt.getJsonPath().parse(inputStream).read("$", new Predicate[0]);

            for (java.util.Map item : (java.util.List)var34) {
               val jsonItem: DocumentContext = JsonExtensionsKt.getJsonPath().parse(item);
               val var10000: SourceAnalyzer = INSTANCE;
               var it: java.lang.String = jsonItem.jsonString();
               it = (java.lang.String)var10000.jsonToBookSource-IoAF18A(it);
               ResultKt.throwOnFailure(it);
               var29.add(it as BookSource);
            }

            var7 = Result.constructor-impl(Unit.INSTANCE);
         } catch (var22: java.lang.Throwable) {
            val var9: Companion = Result.Companion;
            var7 = Result.constructor-impl(ResultKt.createFailure(var22));
         }

         if (Result.exceptionOrNull-impl(var7) != null) {
            var var44: DocumentContext = JsonExtensionsKt.getJsonPath().parse(inputStream).read("$", new Predicate[0]);
            var44 = JsonExtensionsKt.getJsonPath().parse(var44 as java.util.Map);
            val var50: SourceAnalyzer = INSTANCE;
            var var47: java.lang.String = var44.jsonString();
            var47 = (java.lang.String)var50.jsonToBookSource-IoAF18A(var47);
            ResultKt.throwOnFailure(var47);
            var29.add(var47 as BookSource);
         }

         var3 = Result.constructor-impl(var29);
      } catch (var23: java.lang.Throwable) {
         val var5: Companion = Result.Companion;
         var3 = Result.constructor-impl(ResultKt.createFailure(var23));
      }

      return var3;
   }

   public fun jsonToBookSource(json: String): Result<BookSource> {
      val source: BookSource = new BookSource(
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
      );
      val `$this$fromJsonObject$iv`: Gson = GsonExtensionsKt.getGSON();
      var `json$iv`: java.lang.String = StringsKt.trim(json).toString();

      var `json$ivx`: Any;
      try {
         `json$ivx` = Result.Companion;
         val var110: Type = new SourceAnalyzer$jsonToBookSource-IoAF18A$$inlined$fromJsonObject$1().getType();
         var var10000: Any = `$this$fromJsonObject$iv`.fromJson(`json$iv`, var110);
         if (var10000 !is SourceAnalyzer.BookSourceAny) {
            var10000 = null;
         }

         `json$ivx` = Result.constructor-impl(var10000 as SourceAnalyzer.BookSourceAny);
      } catch (var34: java.lang.Throwable) {
         val `$this$jsonToBookSource_IoAF18A_u24lambda_u2d10_u24lambda_u2d9`: Companion = Result.Companion;
         `json$ivx` = Result.constructor-impl(ResultKt.createFailure(var34));
      }

      var var234: java.lang.Throwable = Result.exceptionOrNull-impl(`json$ivx`);
      if (var234 != null) {
         DefaultImpls.log$default(Debug.INSTANCE, "转化书源出错", var234.getLocalizedMessage(), false, 4, null);
      }

      val sourceAny: SourceAnalyzer.BookSourceAny = (if (Result.isFailure-impl(`json$ivx`)) null else `json$ivx`) as SourceAnalyzer.BookSourceAny;

      try {
         val var41: Companion = Result.Companion;
         if ((if (sourceAny == null) null else sourceAny.getRuleToc()) != null) {
            source.setBookSourceUrl(sourceAny.getBookSourceUrl());
            source.setBookSourceName(sourceAny.getBookSourceName());
            source.setBookSourceGroup(sourceAny.getBookSourceGroup());
            source.setBookSourceType(sourceAny.getBookSourceType());
            source.setBookUrlPattern(sourceAny.getBookUrlPattern());
            source.setCustomOrder(sourceAny.getCustomOrder());
            source.setEnabled(sourceAny.getEnabled());
            source.setEnabledExplore(sourceAny.getEnabledExplore());
            source.setEnabledCookieJar(sourceAny.getEnabledCookieJar());
            source.setConcurrentRate(sourceAny.getConcurrentRate());
            source.setHeader(sourceAny.getHeader());
            var var48: Gson = (Gson)sourceAny.getLoginUrl();
            val var252: java.lang.String;
            if (var48 == null) {
               var252 = null;
            } else if (var48 is java.lang.String) {
               var252 = java.lang.String.valueOf(sourceAny.getLoginUrl());
            } else {
               `json$ivx` = JsonPath.parse(sourceAny.getLoginUrl());
               var252 = JsonExtensionsKt.readString(`json$ivx` as ReadContext, "url");
            }

            source.setLoginUrl(var252);
            source.setLoginCheckJs(sourceAny.getLoginCheckJs());
            source.setBookSourceComment(sourceAny.getBookSourceComment());
            source.setLastUpdateTime(sourceAny.getLastUpdateTime());
            source.setRespondTime(sourceAny.getRespondTime());
            source.setWeight(sourceAny.getWeight());
            source.setExploreUrl(sourceAny.getExploreUrl());
            val var238: BookSource;
            val var253: ExploreRule;
            if (sourceAny.getRuleExplore() is java.lang.String) {
               var48 = GsonExtensionsKt.getGSON();
               `json$ivx` = java.lang.String.valueOf(sourceAny.getRuleExplore());

               var var112: Any;
               try {
                  var112 = Result.Companion;
                  val var194: Type = new SourceAnalyzer$jsonToBookSource_IoAF18A$lambda-10$$inlined$fromJsonObject$1().getType();
                  var234 = var48.fromJson((java.lang.String)`json$ivx`, var194);
                  if (var234 !is ExploreRule) {
                     var234 = null;
                  }

                  var112 = Result.constructor-impl(var234 as ExploreRule);
               } catch (var33: java.lang.Throwable) {
                  val var163: Companion = Result.Companion;
                  var112 = Result.constructor-impl(ResultKt.createFailure(var33));
               }

               var238 = source;
               var253 = (if (Result.isFailure-impl(var112)) null else var112) as ExploreRule;
            } else {
               var48 = GsonExtensionsKt.getGSON();
               `json$ivx` = GsonExtensionsKt.getGSON().toJson(sourceAny.getRuleExplore());

               var var114: Any;
               try {
                  var114 = Result.Companion;
                  val var196: Type = new SourceAnalyzer$jsonToBookSource_IoAF18A$lambda-10$$inlined$fromJsonObject$2().getType();
                  var234 = var48.fromJson((java.lang.String)`json$ivx`, var196);
                  if (var234 !is ExploreRule) {
                     var234 = null;
                  }

                  var114 = Result.constructor-impl(var234 as ExploreRule);
               } catch (var32: java.lang.Throwable) {
                  val var166: Companion = Result.Companion;
                  var114 = Result.constructor-impl(ResultKt.createFailure(var32));
               }

               var238 = source;
               var253 = (if (Result.isFailure-impl(var114)) null else var114) as ExploreRule;
            }

            var238.setRuleExplore(var253);
            source.setSearchUrl(sourceAny.getSearchUrl());
            val var241: BookSource;
            val var254: SearchRule;
            if (sourceAny.getRuleSearch() is java.lang.String) {
               var48 = GsonExtensionsKt.getGSON();
               `json$ivx` = java.lang.String.valueOf(sourceAny.getRuleSearch());

               var var116: Any;
               try {
                  var116 = Result.Companion;
                  val var198: Type = new SourceAnalyzer$jsonToBookSource_IoAF18A$lambda-10$$inlined$fromJsonObject$3().getType();
                  var234 = var48.fromJson((java.lang.String)`json$ivx`, var198);
                  if (var234 !is SearchRule) {
                     var234 = null;
                  }

                  var116 = Result.constructor-impl(var234 as SearchRule);
               } catch (var31: java.lang.Throwable) {
                  val var169: Companion = Result.Companion;
                  var116 = Result.constructor-impl(ResultKt.createFailure(var31));
               }

               var241 = source;
               var254 = (if (Result.isFailure-impl(var116)) null else var116) as SearchRule;
            } else {
               var48 = GsonExtensionsKt.getGSON();
               `json$ivx` = GsonExtensionsKt.getGSON().toJson(sourceAny.getRuleSearch());

               var var118: Any;
               try {
                  var118 = Result.Companion;
                  val var200: Type = new SourceAnalyzer$jsonToBookSource_IoAF18A$lambda-10$$inlined$fromJsonObject$4().getType();
                  var234 = var48.fromJson((java.lang.String)`json$ivx`, var200);
                  if (var234 !is SearchRule) {
                     var234 = null;
                  }

                  var118 = Result.constructor-impl(var234 as SearchRule);
               } catch (var30: java.lang.Throwable) {
                  val var172: Companion = Result.Companion;
                  var118 = Result.constructor-impl(ResultKt.createFailure(var30));
               }

               var241 = source;
               var254 = (if (Result.isFailure-impl(var118)) null else var118) as SearchRule;
            }

            var241.setRuleSearch(var254);
            val var244: BookSource;
            val var255: BookInfoRule;
            if (sourceAny.getRuleBookInfo() is java.lang.String) {
               var48 = GsonExtensionsKt.getGSON();
               `json$ivx` = java.lang.String.valueOf(sourceAny.getRuleBookInfo());

               var var120: Any;
               try {
                  var120 = Result.Companion;
                  val var202: Type = new SourceAnalyzer$jsonToBookSource_IoAF18A$lambda-10$$inlined$fromJsonObject$5().getType();
                  var234 = var48.fromJson((java.lang.String)`json$ivx`, var202);
                  if (var234 !is BookInfoRule) {
                     var234 = null;
                  }

                  var120 = Result.constructor-impl(var234 as BookInfoRule);
               } catch (var29: java.lang.Throwable) {
                  val var175: Companion = Result.Companion;
                  var120 = Result.constructor-impl(ResultKt.createFailure(var29));
               }

               var244 = source;
               var255 = (if (Result.isFailure-impl(var120)) null else var120) as BookInfoRule;
            } else {
               var48 = GsonExtensionsKt.getGSON();
               `json$ivx` = GsonExtensionsKt.getGSON().toJson(sourceAny.getRuleBookInfo());

               var var122: Any;
               try {
                  var122 = Result.Companion;
                  val var204: Type = new SourceAnalyzer$jsonToBookSource_IoAF18A$lambda-10$$inlined$fromJsonObject$6().getType();
                  var234 = var48.fromJson((java.lang.String)`json$ivx`, var204);
                  if (var234 !is BookInfoRule) {
                     var234 = null;
                  }

                  var122 = Result.constructor-impl(var234 as BookInfoRule);
               } catch (var28: java.lang.Throwable) {
                  val var178: Companion = Result.Companion;
                  var122 = Result.constructor-impl(ResultKt.createFailure(var28));
               }

               var244 = source;
               var255 = (if (Result.isFailure-impl(var122)) null else var122) as BookInfoRule;
            }

            var244.setRuleBookInfo(var255);
            val var247: BookSource;
            val var256: TocRule;
            if (sourceAny.getRuleToc() is java.lang.String) {
               var48 = GsonExtensionsKt.getGSON();
               `json$ivx` = java.lang.String.valueOf(sourceAny.getRuleToc());

               var var124: Any;
               try {
                  var124 = Result.Companion;
                  val var206: Type = new SourceAnalyzer$jsonToBookSource_IoAF18A$lambda-10$$inlined$fromJsonObject$7().getType();
                  var234 = var48.fromJson((java.lang.String)`json$ivx`, var206);
                  if (var234 !is TocRule) {
                     var234 = null;
                  }

                  var124 = Result.constructor-impl(var234 as TocRule);
               } catch (var27: java.lang.Throwable) {
                  val var181: Companion = Result.Companion;
                  var124 = Result.constructor-impl(ResultKt.createFailure(var27));
               }

               var247 = source;
               var256 = (if (Result.isFailure-impl(var124)) null else var124) as TocRule;
            } else {
               var48 = GsonExtensionsKt.getGSON();
               `json$ivx` = GsonExtensionsKt.getGSON().toJson(sourceAny.getRuleToc());

               var var126: Any;
               try {
                  var126 = Result.Companion;
                  val var208: Type = new SourceAnalyzer$jsonToBookSource_IoAF18A$lambda-10$$inlined$fromJsonObject$8().getType();
                  var234 = var48.fromJson((java.lang.String)`json$ivx`, var208);
                  if (var234 !is TocRule) {
                     var234 = null;
                  }

                  var126 = Result.constructor-impl(var234 as TocRule);
               } catch (var26: java.lang.Throwable) {
                  val var184: Companion = Result.Companion;
                  var126 = Result.constructor-impl(ResultKt.createFailure(var26));
               }

               var247 = source;
               var256 = (if (Result.isFailure-impl(var126)) null else var126) as TocRule;
            }

            var247.setRuleToc(var256);
            val var250: BookSource;
            val var257: ContentRule;
            if (sourceAny.getRuleContent() is java.lang.String) {
               var48 = GsonExtensionsKt.getGSON();
               `json$ivx` = java.lang.String.valueOf(sourceAny.getRuleContent());

               var var128: Any;
               try {
                  var128 = Result.Companion;
                  val var210: Type = new SourceAnalyzer$jsonToBookSource_IoAF18A$lambda-10$$inlined$fromJsonObject$9().getType();
                  var234 = var48.fromJson((java.lang.String)`json$ivx`, var210);
                  if (var234 !is ContentRule) {
                     var234 = null;
                  }

                  var128 = Result.constructor-impl(var234 as ContentRule);
               } catch (var25: java.lang.Throwable) {
                  val var187: Companion = Result.Companion;
                  var128 = Result.constructor-impl(ResultKt.createFailure(var25));
               }

               var250 = source;
               var257 = (if (Result.isFailure-impl(var128)) null else var128) as ContentRule;
            } else {
               var48 = GsonExtensionsKt.getGSON();
               `json$ivx` = GsonExtensionsKt.getGSON().toJson(sourceAny.getRuleContent());

               var var130: Any;
               try {
                  var130 = Result.Companion;
                  val var212: Type = new SourceAnalyzer$jsonToBookSource_IoAF18A$lambda-10$$inlined$fromJsonObject$10().getType();
                  var234 = var48.fromJson((java.lang.String)`json$ivx`, var212);
                  if (var234 !is ContentRule) {
                     var234 = null;
                  }

                  var130 = Result.constructor-impl(var234 as ContentRule);
               } catch (var24: java.lang.Throwable) {
                  val var190: Companion = Result.Companion;
                  var130 = Result.constructor-impl(ResultKt.createFailure(var24));
               }

               var250 = source;
               var257 = (if (Result.isFailure-impl(var130)) null else var130) as ContentRule;
            }

            var250.setRuleContent(var257);
         } else {
            val jsonItem: DocumentContext = JsonExtensionsKt.getJsonPath().parse(StringsKt.trim(json).toString());
            var var132: java.lang.String = JsonExtensionsKt.readString(jsonItem, "bookSourceUrl");
            if (var132 == null) {
               throw new NoStackTraceException("格式不对");
            }

            var var10001: Byte;
            label336: {
               source.setBookSourceUrl(var132);
               var132 = JsonExtensionsKt.readString(jsonItem, "bookSourceName");
               source.setBookSourceName(if (var132 == null) "" else var132);
               source.setBookSourceGroup(JsonExtensionsKt.readString(jsonItem, "bookSourceGroup"));
               var132 = JsonExtensionsKt.readString(jsonItem, "bookSourceComment");
               source.setBookSourceComment(if (var132 == null) "" else var132);
               source.setBookUrlPattern(JsonExtensionsKt.readString(jsonItem, "ruleBookUrlPattern"));
               val var135: Int = JsonExtensionsKt.readInt(jsonItem, "serialNumber");
               source.setCustomOrder(if (var135 == null) 0 else var135);
               source.setHeader(INSTANCE.uaToHeader(JsonExtensionsKt.readString(jsonItem, "httpUserAgent")));
               source.setSearchUrl(INSTANCE.toNewUrl(JsonExtensionsKt.readString(jsonItem, "ruleSearchUrl")));
               source.setExploreUrl(INSTANCE.toNewUrls(JsonExtensionsKt.readString(jsonItem, "ruleFindUrl")));
               var132 = JsonExtensionsKt.readString(jsonItem, "bookSourceType");
               if (var132 != null) {
                  switch (sourceType.hashCode()) {
                     case 49:
                        if (var132.equals("1")) {
                           var10001 = 1;
                           break label336;
                        }
                        break;
                     case 50:
                        if (var132.equals("2")) {
                           var10001 = 2;
                           break label336;
                        }
                        break;
                     case 51:
                        if (var132.equals("3")) {
                           var10001 = 3;
                           break label336;
                        }
                        break;
                     case 2157948:
                        if (var132.equals("FILE")) {
                           var10001 = 3;
                           break label336;
                        }
                        break;
                     case 3143036:
                        if (var132.equals("file")) {
                           var10001 = 3;
                           break label336;
                        }
                        break;
                     case 62628790:
                        if (var132.equals("AUDIO")) {
                           var10001 = 1;
                           break label336;
                        }
                        break;
                     case 69775675:
                        if (var132.equals("IMAGE")) {
                           var10001 = 2;
                           break label336;
                        }
                        break;
                     case 93166550:
                        if (var132.equals("audio")) {
                           var10001 = 1;
                           break label336;
                        }
                        break;
                     case 100313435:
                        if (var132.equals("image")) {
                           var10001 = 2;
                           break label336;
                        }
                     default:
                  }
               }

               var10001 = 0;
            }

            source.setBookSourceType(var10001);
            val var157: java.lang.Boolean = JsonExtensionsKt.readBool(jsonItem, "enable");
            source.setEnabled(var157 == null || var157);
            val var158: java.lang.CharSequence = source.getExploreUrl();
            if (var158 == null || StringsKt.isBlank(var158)) {
               source.setEnabledExplore(false);
            }

            source.setRuleSearch(
               new SearchRule(
                  INSTANCE.toNewRule(JsonExtensionsKt.readString(jsonItem, "ruleSearchList")),
                  INSTANCE.toNewRule(JsonExtensionsKt.readString(jsonItem, "ruleSearchName")),
                  INSTANCE.toNewRule(JsonExtensionsKt.readString(jsonItem, "ruleSearchAuthor")),
                  INSTANCE.toNewRule(JsonExtensionsKt.readString(jsonItem, "ruleSearchIntroduce")),
                  INSTANCE.toNewRule(JsonExtensionsKt.readString(jsonItem, "ruleSearchKind")),
                  INSTANCE.toNewRule(JsonExtensionsKt.readString(jsonItem, "ruleSearchLastChapter")),
                  null,
                  INSTANCE.toNewRule(JsonExtensionsKt.readString(jsonItem, "ruleSearchNoteUrl")),
                  INSTANCE.toNewRule(JsonExtensionsKt.readString(jsonItem, "ruleSearchCoverUrl")),
                  null,
                  576,
                  null
               )
            );
            source.setRuleExplore(
               new ExploreRule(
                  INSTANCE.toNewRule(JsonExtensionsKt.readString(jsonItem, "ruleFindList")),
                  INSTANCE.toNewRule(JsonExtensionsKt.readString(jsonItem, "ruleFindName")),
                  INSTANCE.toNewRule(JsonExtensionsKt.readString(jsonItem, "ruleFindAuthor")),
                  INSTANCE.toNewRule(JsonExtensionsKt.readString(jsonItem, "ruleFindIntroduce")),
                  INSTANCE.toNewRule(JsonExtensionsKt.readString(jsonItem, "ruleFindKind")),
                  INSTANCE.toNewRule(JsonExtensionsKt.readString(jsonItem, "ruleFindLastChapter")),
                  null,
                  INSTANCE.toNewRule(JsonExtensionsKt.readString(jsonItem, "ruleFindNoteUrl")),
                  INSTANCE.toNewRule(JsonExtensionsKt.readString(jsonItem, "ruleFindCoverUrl")),
                  null,
                  576,
                  null
               )
            );
            source.setRuleBookInfo(
               new BookInfoRule(
                  INSTANCE.toNewRule(JsonExtensionsKt.readString(jsonItem, "ruleBookInfoInit")),
                  INSTANCE.toNewRule(JsonExtensionsKt.readString(jsonItem, "ruleBookName")),
                  INSTANCE.toNewRule(JsonExtensionsKt.readString(jsonItem, "ruleBookAuthor")),
                  INSTANCE.toNewRule(JsonExtensionsKt.readString(jsonItem, "ruleIntroduce")),
                  INSTANCE.toNewRule(JsonExtensionsKt.readString(jsonItem, "ruleBookKind")),
                  INSTANCE.toNewRule(JsonExtensionsKt.readString(jsonItem, "ruleBookLastChapter")),
                  null,
                  INSTANCE.toNewRule(JsonExtensionsKt.readString(jsonItem, "ruleCoverUrl")),
                  INSTANCE.toNewRule(JsonExtensionsKt.readString(jsonItem, "ruleChapterUrl")),
                  null,
                  null,
                  1600,
                  null
               )
            );
            source.setRuleToc(
               new TocRule(
                  null,
                  INSTANCE.toNewRule(JsonExtensionsKt.readString(jsonItem, "ruleChapterList")),
                  INSTANCE.toNewRule(JsonExtensionsKt.readString(jsonItem, "ruleChapterName")),
                  INSTANCE.toNewRule(JsonExtensionsKt.readString(jsonItem, "ruleContentUrl")),
                  null,
                  null,
                  null,
                  INSTANCE.toNewRule(JsonExtensionsKt.readString(jsonItem, "ruleChapterUrlNext")),
                  113,
                  null
               )
            );
            val var216: java.lang.String = INSTANCE.toNewRule(JsonExtensionsKt.readString(jsonItem, "ruleBookContent"));
            var var162: java.lang.String = if (var216 == null) "" else var216;
            if (StringsKt.startsWith$default(if (var216 == null) "" else var216, "$", false, 2, null)
               && !StringsKt.startsWith$default(if (var216 == null) "" else var216, "$.", false, 2, null)) {
               val var236: java.lang.String = var162.substring(1);
               var162 = var236;
            }

            source.setRuleContent(
               new ContentRule(
                  var162,
                  INSTANCE.toNewRule(JsonExtensionsKt.readString(jsonItem, "ruleContentUrlNext")),
                  null,
                  null,
                  INSTANCE.toNewRule(JsonExtensionsKt.readString(jsonItem, "ruleBookContentReplace")),
                  null,
                  44,
                  null
               )
            );
         }

         `json$iv` = (java.lang.String)Result.constructor-impl(source);
      } catch (var35: java.lang.Throwable) {
         val var47: Companion = Result.Companion;
         `json$iv` = (java.lang.String)Result.constructor-impl(ResultKt.createFailure(var35));
      }

      return `json$iv`;
   }

   private fun toNewRule(oldRule: String?): String? {
      if (oldRule == null || StringsKt.isBlank(oldRule)) {
         return null;
      } else {
         var var9: java.lang.String = oldRule;
         var var10: Boolean = false;
         var var11: Boolean = false;
         if (StringsKt.startsWith$default(oldRule, "-", false, 2, null)) {
            var10 = true;
            if (oldRule == null) {
               throw new NullPointerException("null cannot be cast to non-null type java.lang.String");
            }

            val var10000: java.lang.String = oldRule.substring(1);
            var9 = var10000;
         }

         if (StringsKt.startsWith$default(var9, "+", false, 2, null)) {
            var11 = true;
            if (var9 == null) {
               throw new NullPointerException("null cannot be cast to non-null type java.lang.String");
            }

            val var17: java.lang.String = var9.substring(1);
            var9 = var17;
         }

         if (!StringsKt.startsWith(var9, "@CSS:", true)
            && !StringsKt.startsWith(var9, "@XPath:", true)
            && !StringsKt.startsWith$default(var9, "//", false, 2, null)
            && !StringsKt.startsWith$default(var9, "##", false, 2, null)
            && !StringsKt.startsWith$default(var9, ":", false, 2, null)
            && !StringsKt.contains(var9, "@js:", true)
            && !StringsKt.contains(var9, "<js>", true)) {
            if (StringsKt.contains$default(var9, "#", false, 2, null) && !StringsKt.contains$default(var9, "##", false, 2, null)) {
               var9 = StringsKt.replace$default(oldRule, "#", "##", false, 4, null);
            }

            if (StringsKt.contains$default(var9, "|", false, 2, null) && !StringsKt.contains$default(var9, "||", false, 2, null)) {
               if (StringsKt.contains$default(var9, "##", false, 2, null)) {
                  val list: java.util.List = StringsKt.split$default(var9, new java.lang.String[]{"##"}, false, 0, 6, null);
                  if (StringsKt.contains$default(list.get(0) as java.lang.CharSequence, "|", false, 2, null)) {
                     var9 = StringsKt.replace$default(list.get(0) as java.lang.String, "|", "||", false, 4, null);
                     var var14: Int = 1;
                     val var16: Int = list.size();
                     if (1 < var16) {
                        do {
                           var9 = "$var9##${list.get(var14++) as java.lang.String}";
                        } while (var14 < var16);
                     }
                  }
               } else {
                  var9 = StringsKt.replace$default(var9, "|", "||", false, 4, null);
               }
            }

            if (StringsKt.contains$default(var9, "&", false, 2, null)
               && !StringsKt.contains$default(var9, "&&", false, 2, null)
               && !StringsKt.contains$default(var9, "http", false, 2, null)
               && !StringsKt.startsWith$default(var9, "/", false, 2, null)) {
               var9 = StringsKt.replace$default(var9, "&", "&&", false, 4, null);
            }
         }

         if (var11) {
            var9 = Intrinsics.stringPlus("+", var9);
         }

         if (var10) {
            var9 = Intrinsics.stringPlus("-", var9);
         }

         return var9;
      }
   }

   private fun toNewUrls(oldUrls: String?): String? {
      if (oldUrls == null || StringsKt.isBlank(oldUrls)) {
         return null;
      } else if (!StringsKt.startsWith$default(oldUrls, "@js:", false, 2, null) && !StringsKt.startsWith$default(oldUrls, "<js>", false, 2, null)) {
         if (!StringsKt.contains$default(oldUrls, "\n", false, 2, null) && !StringsKt.contains$default(oldUrls, "&&", false, 2, null)) {
            return this.toNewUrl(oldUrls);
         } else {
            val var21: java.lang.Iterable = new Regex("(&&|\r?\n)+").split(oldUrls, 0);
            val var26: java.util.Collection = new ArrayList(CollectionsKt.collectionSizeOrDefault(var21, 10));

            for (Object item$iv$iv : $this$map$iv) {
               val var12: java.lang.String = INSTANCE.toNewUrl(`item$iv$iv` as java.lang.String);
               var26.add(if (var12 == null) null else new Regex("\n\\s*").replace(var12, ""));
            }

            return CollectionsKt.joinToString$default(var26 as java.util.List, "\n", null, null, 0, null, null, 62, null);
         }
      } else {
         return oldUrls;
      }
   }

   private fun toNewUrl(oldUrl: String?): String? {
      if (oldUrl == null || StringsKt.isBlank(oldUrl)) {
         return null;
      } else {
         var var11: java.lang.String = oldUrl;
         if (StringsKt.startsWith(oldUrl, "<js>", true)) {
            return StringsKt.replace$default(
               StringsKt.replace$default(oldUrl, "=searchKey", "={{key}}", false, 4, null), "=searchPage", "={{page}}", false, 4, null
            );
         } else {
            val var17: HashMap = new HashMap();
            var var18: Matcher = headerPattern.matcher(oldUrl);
            if (var18.find()) {
               val urlList: java.lang.String = var18.group();
               var11 = StringsKt.replace$default(oldUrl, urlList, "", false, 4, null);
               val jsList: java.util.Map = var17;
               val var10000: java.lang.String = urlList.substring(8);
               jsList.put("headers", var10000);
            }

            var var20: java.util.List = StringsKt.split$default(var11, new java.lang.String[]{"|"}, false, 0, 6, null);
            var11 = var20.get(0) as java.lang.String;
            if (var20.size() > 1) {
               var17.put("charset", StringsKt.split$default(var20.get(1) as java.lang.CharSequence, new java.lang.String[]{"="}, false, 0, 6, null).get(1));
            }

            var18 = jsPattern.matcher(var11);

            val var24: ArrayList;
            for (jsList = new ArrayList();
               mather.find();
               url = StringsKt.replace$default(url, CollectionsKt.last(jsList), Intrinsics.stringPlus("$", jsList.size() - 1), false, 4, null)
            ) {
               var24.add(var18.group());
            }

            var11 = StringsKt.replace$default(
               new Regex("searchPage([-+]1)")
                  .replace(
                     new Regex("<searchPage([-+]1)>")
                        .replace(
                           StringsKt.replace$default(
                              StringsKt.replace$default(StringsKt.replace$default(var11, "{", "<", false, 4, null), "}", ">", false, 4, null),
                              "searchKey",
                              "{{key}}",
                              false,
                              4,
                              null
                           ),
                           "{{page$1}}"
                        ),
                     "{{page$1}}"
                  ),
               "searchPage",
               "{{page}}",
               false,
               4,
               null
            );
            val var29: java.util.Iterator = var24.iterator();
            var var39: Int = 0;

            while (var29.hasNext()) {
               var11 = StringsKt.replace$default(
                  var11,
                  Intrinsics.stringPlus("$", var39++),
                  StringsKt.replace$default(
                     StringsKt.replace$default(var29.next() as java.lang.String, "searchKey", "key", false, 4, null), "searchPage", "page", false, 4, null
                  ),
                  false,
                  4,
                  null
               );
            }

            var20 = StringsKt.split$default(var11, new java.lang.String[]{"@"}, false, 0, 6, null);
            var11 = var20.get(0) as java.lang.String;
            if (var20.size() > 1) {
               var17.put("method", "POST");
               var17.put("body", var20.get(1));
            }

            if (var17.size() > 0) {
               var11 = "$var11,${GsonExtensionsKt.getGSON().toJson(var17)}";
            }

            return var11;
         }
      }
   }

   private fun uaToHeader(ua: String?): String? {
      return if (ua as java.lang.CharSequence == null || ua.length() == 0)
         null
         else
         GsonExtensionsKt.getGSON().toJson(MapsKt.mapOf(new Pair<>("User-Agent", ua)));
   }

   public data class BookSourceAny(bookSourceName: String = "",
      bookSourceGroup: String? = null,
      bookSourceUrl: String = "",
      bookSourceType: Int = 0,
      bookUrlPattern: String? = null,
      customOrder: Int = 0,
      enabled: Boolean = true,
      enabledExplore: Boolean = true,
      enabledCookieJar: Boolean = false,
      concurrentRate: String? = null,
      header: String? = null,
      loginUrl: Any? = null,
      loginUi: Any? = null,
      loginCheckJs: String? = null,
      bookSourceComment: String? = "",
      lastUpdateTime: Long = 0L,
      respondTime: Long = 180000L,
      weight: Int = 0,
      exploreUrl: String? = null,
      ruleExplore: Any? = null,
      searchUrl: String? = null,
      ruleSearch: Any? = null,
      ruleBookInfo: Any? = null,
      ruleToc: Any? = null,
      ruleContent: Any? = null
   ) {
      public final var bookSourceComment: String?
         internal set

      public final var bookSourceGroup: String?
         internal set

      public final var bookSourceName: String
         internal set

      public final var bookSourceType: Int
         internal set

      public final var bookSourceUrl: String
         internal set

      public final var bookUrlPattern: String?
         internal set

      public final var concurrentRate: String?
         internal set

      public final var customOrder: Int
         internal set

      public final var enabled: Boolean
         internal set

      public final var enabledCookieJar: Boolean
         internal set

      public final var enabledExplore: Boolean
         internal set

      public final var exploreUrl: String?
         internal set

      public final var header: String?
         internal set

      public final var lastUpdateTime: Long
         internal set

      public final var loginCheckJs: String?
         internal set

      public final var loginUi: Any?
         internal set

      public final var loginUrl: Any?
         internal set

      public final var respondTime: Long
         internal set

      public final var ruleBookInfo: Any?
         internal set

      public final var ruleContent: Any?
         internal set

      public final var ruleExplore: Any?
         internal set

      public final var ruleSearch: Any?
         internal set

      public final var ruleToc: Any?
         internal set

      public final var searchUrl: String?
         internal set

      public final var weight: Int
         internal set

      init {
         this.bookSourceName = bookSourceName;
         this.bookSourceGroup = bookSourceGroup;
         this.bookSourceUrl = bookSourceUrl;
         this.bookSourceType = bookSourceType;
         this.bookUrlPattern = bookUrlPattern;
         this.customOrder = customOrder;
         this.enabled = enabled;
         this.enabledExplore = enabledExplore;
         this.enabledCookieJar = enabledCookieJar;
         this.concurrentRate = concurrentRate;
         this.header = header;
         this.loginUrl = loginUrl;
         this.loginUi = loginUi;
         this.loginCheckJs = loginCheckJs;
         this.bookSourceComment = bookSourceComment;
         this.lastUpdateTime = lastUpdateTime;
         this.respondTime = respondTime;
         this.weight = weight;
         this.exploreUrl = exploreUrl;
         this.ruleExplore = ruleExplore;
         this.searchUrl = searchUrl;
         this.ruleSearch = ruleSearch;
         this.ruleBookInfo = ruleBookInfo;
         this.ruleToc = ruleToc;
         this.ruleContent = ruleContent;
      }

      public operator fun component1(): String {
         return this.bookSourceName;
      }

      public operator fun component2(): String? {
         return this.bookSourceGroup;
      }

      public operator fun component3(): String {
         return this.bookSourceUrl;
      }

      public operator fun component4(): Int {
         return this.bookSourceType;
      }

      public operator fun component5(): String? {
         return this.bookUrlPattern;
      }

      public operator fun component6(): Int {
         return this.customOrder;
      }

      public operator fun component7(): Boolean {
         return this.enabled;
      }

      public operator fun component8(): Boolean {
         return this.enabledExplore;
      }

      public operator fun component9(): Boolean {
         return this.enabledCookieJar;
      }

      public operator fun component10(): String? {
         return this.concurrentRate;
      }

      public operator fun component11(): String? {
         return this.header;
      }

      public operator fun component12(): Any? {
         return this.loginUrl;
      }

      public operator fun component13(): Any? {
         return this.loginUi;
      }

      public operator fun component14(): String? {
         return this.loginCheckJs;
      }

      public operator fun component15(): String? {
         return this.bookSourceComment;
      }

      public operator fun component16(): Long {
         return this.lastUpdateTime;
      }

      public operator fun component17(): Long {
         return this.respondTime;
      }

      public operator fun component18(): Int {
         return this.weight;
      }

      public operator fun component19(): String? {
         return this.exploreUrl;
      }

      public operator fun component20(): Any? {
         return this.ruleExplore;
      }

      public operator fun component21(): String? {
         return this.searchUrl;
      }

      public operator fun component22(): Any? {
         return this.ruleSearch;
      }

      public operator fun component23(): Any? {
         return this.ruleBookInfo;
      }

      public operator fun component24(): Any? {
         return this.ruleToc;
      }

      public operator fun component25(): Any? {
         return this.ruleContent;
      }

      public fun copy(
         bookSourceName: String = this.bookSourceName,
         bookSourceGroup: String? = this.bookSourceGroup,
         bookSourceUrl: String = this.bookSourceUrl,
         bookSourceType: Int = this.bookSourceType,
         bookUrlPattern: String? = this.bookUrlPattern,
         customOrder: Int = this.customOrder,
         enabled: Boolean = this.enabled,
         enabledExplore: Boolean = this.enabledExplore,
         enabledCookieJar: Boolean = this.enabledCookieJar,
         concurrentRate: String? = this.concurrentRate,
         header: String? = this.header,
         loginUrl: Any? = this.loginUrl,
         loginUi: Any? = this.loginUi,
         loginCheckJs: String? = this.loginCheckJs,
         bookSourceComment: String? = this.bookSourceComment,
         lastUpdateTime: Long = this.lastUpdateTime,
         respondTime: Long = this.respondTime,
         weight: Int = this.weight,
         exploreUrl: String? = this.exploreUrl,
         ruleExplore: Any? = this.ruleExplore,
         searchUrl: String? = this.searchUrl,
         ruleSearch: Any? = this.ruleSearch,
         ruleBookInfo: Any? = this.ruleBookInfo,
         ruleToc: Any? = this.ruleToc,
         ruleContent: Any? = this.ruleContent
      ): io.legado.app.help.SourceAnalyzer.BookSourceAny {
         return new SourceAnalyzer.BookSourceAny(
            bookSourceName,
            bookSourceGroup,
            bookSourceUrl,
            bookSourceType,
            bookUrlPattern,
            customOrder,
            enabled,
            enabledExplore,
            enabledCookieJar,
            concurrentRate,
            header,
            loginUrl,
            loginUi,
            loginCheckJs,
            bookSourceComment,
            lastUpdateTime,
            respondTime,
            weight,
            exploreUrl,
            ruleExplore,
            searchUrl,
            ruleSearch,
            ruleBookInfo,
            ruleToc,
            ruleContent
         );
      }

      public override fun toString(): String {
         val var1: StringBuilder = new StringBuilder();
         var1.append("BookSourceAny(bookSourceName=")
            .append(this.bookSourceName)
            .append(", bookSourceGroup=")
            .append(this.bookSourceGroup)
            .append(", bookSourceUrl=")
            .append(this.bookSourceUrl)
            .append(", bookSourceType=")
            .append(this.bookSourceType)
            .append(", bookUrlPattern=")
            .append(this.bookUrlPattern)
            .append(", customOrder=")
            .append(this.customOrder)
            .append(", enabled=")
            .append(this.enabled)
            .append(", enabledExplore=")
            .append(this.enabledExplore)
            .append(", enabledCookieJar=")
            .append(this.enabledCookieJar)
            .append(", concurrentRate=")
            .append(this.concurrentRate)
            .append(", header=")
            .append(this.header)
            .append(", loginUrl=");
         var1.append(this.loginUrl)
            .append(", loginUi=")
            .append(this.loginUi)
            .append(", loginCheckJs=")
            .append(this.loginCheckJs)
            .append(", bookSourceComment=")
            .append(this.bookSourceComment)
            .append(", lastUpdateTime=")
            .append(this.lastUpdateTime)
            .append(", respondTime=")
            .append(this.respondTime)
            .append(", weight=")
            .append(this.weight)
            .append(", exploreUrl=")
            .append(this.exploreUrl)
            .append(", ruleExplore=")
            .append(this.ruleExplore)
            .append(", searchUrl=")
            .append(this.searchUrl)
            .append(", ruleSearch=")
            .append(this.ruleSearch)
            .append(", ruleBookInfo=")
            .append(this.ruleBookInfo);
         var1.append(", ruleToc=").append(this.ruleToc).append(", ruleContent=").append(this.ruleContent).append(')');
         return var1.toString();
      }

      public override fun hashCode(): Int {
         var var10000: Int = (
               (
                        (
                                 (
                                          (this.bookSourceName.hashCode() * 31 + (if (this.bookSourceGroup == null) 0 else this.bookSourceGroup.hashCode()))
                                                * 31
                                             + this.bookSourceUrl.hashCode()
                                       )
                                       * 31
                                    + Integer.hashCode(this.bookSourceType)
                              )
                              * 31
                           + (if (this.bookUrlPattern == null) 0 else this.bookUrlPattern.hashCode())
                     )
                     * 31
                  + Integer.hashCode(this.customOrder)
            )
            * 31;
         var var10001: Byte = this.enabled;
         if (this.enabled) {
            var10001 = 1;
         }

         var10000 = (var10000 + var10001) * 31;
         var10001 = this.enabledExplore;
         if (this.enabledExplore) {
            var10001 = 1;
         }

         var10000 = (var10000 + var10001) * 31;
         var10001 = this.enabledCookieJar;
         if (this.enabledCookieJar) {
            var10001 = 1;
         }

         return (
                  (
                           (
                                    (
                                             (
                                                      (
                                                               (
                                                                        (
                                                                                 (
                                                                                          (
                                                                                                   (
                                                                                                            (
                                                                                                                     (
                                                                                                                              (
                                                                                                                                       (
                                                                                                                                                (
                                                                                                                                                         var10000
                                                                                                                                                            + var10001
                                                                                                                                                      )
                                                                                                                                                      * 31
                                                                                                                                                   + (
                                                                                                                                                      if (this.concurrentRate
                                                                                                                                                            == null)
                                                                                                                                                         0
                                                                                                                                                         else
                                                                                                                                                         this.concurrentRate
                                                                                                                                                            .hashCode()
                                                                                                                                                   )
                                                                                                                                             )
                                                                                                                                             * 31
                                                                                                                                          + (
                                                                                                                                             if (this.header
                                                                                                                                                   == null)
                                                                                                                                                0
                                                                                                                                                else
                                                                                                                                                this.header
                                                                                                                                                   .hashCode()
                                                                                                                                          )
                                                                                                                                    )
                                                                                                                                    * 31
                                                                                                                                 + (
                                                                                                                                    if (this.loginUrl == null)
                                                                                                                                       0
                                                                                                                                       else
                                                                                                                                       this.loginUrl.hashCode()
                                                                                                                                 )
                                                                                                                           )
                                                                                                                           * 31
                                                                                                                        + (
                                                                                                                           if (this.loginUi == null)
                                                                                                                              0
                                                                                                                              else
                                                                                                                              this.loginUi.hashCode()
                                                                                                                        )
                                                                                                                  )
                                                                                                                  * 31
                                                                                                               + (
                                                                                                                  if (this.loginCheckJs == null)
                                                                                                                     0
                                                                                                                     else
                                                                                                                     this.loginCheckJs.hashCode()
                                                                                                               )
                                                                                                         )
                                                                                                         * 31
                                                                                                      + (
                                                                                                         if (this.bookSourceComment == null)
                                                                                                            0
                                                                                                            else
                                                                                                            this.bookSourceComment.hashCode()
                                                                                                      )
                                                                                                )
                                                                                                * 31
                                                                                             + java.lang.Long.hashCode(this.lastUpdateTime)
                                                                                       )
                                                                                       * 31
                                                                                    + java.lang.Long.hashCode(this.respondTime)
                                                                              )
                                                                              * 31
                                                                           + Integer.hashCode(this.weight)
                                                                     )
                                                                     * 31
                                                                  + (if (this.exploreUrl == null) 0 else this.exploreUrl.hashCode())
                                                            )
                                                            * 31
                                                         + (if (this.ruleExplore == null) 0 else this.ruleExplore.hashCode())
                                                   )
                                                   * 31
                                                + (if (this.searchUrl == null) 0 else this.searchUrl.hashCode())
                                          )
                                          * 31
                                       + (if (this.ruleSearch == null) 0 else this.ruleSearch.hashCode())
                                 )
                                 * 31
                              + (if (this.ruleBookInfo == null) 0 else this.ruleBookInfo.hashCode())
                        )
                        * 31
                     + (if (this.ruleToc == null) 0 else this.ruleToc.hashCode())
               )
               * 31
            + (if (this.ruleContent == null) 0 else this.ruleContent.hashCode());
      }

      public override operator fun equals(other: Any?): Boolean {
         if (this === other) {
            return true;
         } else if (other !is SourceAnalyzer.BookSourceAny) {
            return false;
         } else {
            val var2: SourceAnalyzer.BookSourceAny = other as SourceAnalyzer.BookSourceAny;
            if (!(this.bookSourceName == (other as SourceAnalyzer.BookSourceAny).bookSourceName)) {
               return false;
            } else if (!(this.bookSourceGroup == var2.bookSourceGroup)) {
               return false;
            } else if (!(this.bookSourceUrl == var2.bookSourceUrl)) {
               return false;
            } else if (this.bookSourceType != var2.bookSourceType) {
               return false;
            } else if (!(this.bookUrlPattern == var2.bookUrlPattern)) {
               return false;
            } else if (this.customOrder != var2.customOrder) {
               return false;
            } else if (this.enabled != var2.enabled) {
               return false;
            } else if (this.enabledExplore != var2.enabledExplore) {
               return false;
            } else if (this.enabledCookieJar != var2.enabledCookieJar) {
               return false;
            } else if (!(this.concurrentRate == var2.concurrentRate)) {
               return false;
            } else if (!(this.header == var2.header)) {
               return false;
            } else if (!(this.loginUrl == var2.loginUrl)) {
               return false;
            } else if (!(this.loginUi == var2.loginUi)) {
               return false;
            } else if (!(this.loginCheckJs == var2.loginCheckJs)) {
               return false;
            } else if (!(this.bookSourceComment == var2.bookSourceComment)) {
               return false;
            } else if (this.lastUpdateTime != var2.lastUpdateTime) {
               return false;
            } else if (this.respondTime != var2.respondTime) {
               return false;
            } else if (this.weight != var2.weight) {
               return false;
            } else if (!(this.exploreUrl == var2.exploreUrl)) {
               return false;
            } else if (!(this.ruleExplore == var2.ruleExplore)) {
               return false;
            } else if (!(this.searchUrl == var2.searchUrl)) {
               return false;
            } else if (!(this.ruleSearch == var2.ruleSearch)) {
               return false;
            } else if (!(this.ruleBookInfo == var2.ruleBookInfo)) {
               return false;
            } else if (!(this.ruleToc == var2.ruleToc)) {
               return false;
            } else {
               return this.ruleContent == var2.ruleContent;
            }
         }
      }

      fun BookSourceAny() {
         this(
            null,
            null,
            null,
            0,
            null,
            0,
            false,
            false,
            false,
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
            33554431,
            null
         );
      }
   }
}
