package io.legado.app.help.http

import io.legado.app.adapters.ReaderAdapterHelper
import io.legado.app.help.http.api.CookieManager
import io.legado.app.utils.ACache
import io.legado.app.utils.NetworkUtils
import io.legado.app.utils.TextUtils
import java.io.File
import java.util.LinkedHashMap
import kotlin.jvm.internal.Intrinsics

public class CookieStore(userNameSpace: String) : CookieManager {
   public final val cacheInstance: ACache
   public final val userNameSpace: String

   init {
      this.userNameSpace = userNameSpace;
      this.cacheInstance = ACache.Companion
         .get(new File(ReaderAdapterHelper.INSTANCE.getAdapter().getWorkDir("storage", "cache", "cookie", this.userNameSpace)), 50000000L, 1000000);
   }

   public override fun setCookie(url: String, cookie: String?) {
      val domain: java.lang.String = NetworkUtils.INSTANCE.getSubDomain(url);
      if (domain.length() > 0) {
         this.cacheInstance.put(domain, if (cookie == null) "" else cookie);
      }
   }

   public override fun replaceCookie(url: String, cookie: String) {
      if (!TextUtils.isEmpty(url) && !TextUtils.isEmpty(cookie)) {
         val oldCookie: java.lang.String = this.getCookie(url);
         if (TextUtils.isEmpty(oldCookie)) {
            this.setCookie(url, cookie);
         } else {
            val cookieMap: java.util.Map = this.cookieToMap(oldCookie);
            cookieMap.putAll(this.cookieToMap(cookie));
            this.setCookie(url, this.mapToCookie(cookieMap));
         }
      }
   }

   public override fun getCookie(url: String): String {
      val domain: java.lang.String = NetworkUtils.INSTANCE.getSubDomain(url);
      if (domain.length() == 0) {
         return "";
      } else {
         val var5: java.lang.String = this.cacheInstance.getAsString(domain);
         return if (var5 == null) "" else var5;
      }
   }

   public fun getKey(url: String, key: String): String {
      val var5: java.lang.String = this.cookieToMap(this.getCookie(url)).get(key);
      return if (var5 == null) "" else var5;
   }

   public override fun removeCookie(url: String) {
      val domain: java.lang.String = NetworkUtils.INSTANCE.getSubDomain(url);
      if (domain.length() != 0) {
         this.cacheInstance.remove(domain);
      }
   }

   public override fun cookieToMap(cookie: String): MutableMap<String, String> {
      val cookieMap: java.util.Map = new LinkedHashMap();
      if (StringsKt.isBlank(cookie)) {
         return cookieMap;
      } else {
         var var10000: java.util.List;
         label173: {
            val var23: java.util.List = new Regex(";").split(cookie, 0);
            if (!var23.isEmpty()) {
               val var31: java.util.ListIterator = var23.listIterator(var23.size());

               while (iterator$iv.hasPrevious()) {
                  if ((var31.previous() as java.lang.String).length() != 0) {
                     var10000 = CollectionsKt.take(var23, var31.nextIndex() + 1);
                     break label173;
                  }
               }
            }

            var10000 = CollectionsKt.emptyList();
         }

         val var77: Array<Any> = var10000.toArray(new java.lang.String[0]);
         if (var77 == null) {
            throw new NullPointerException("null cannot be cast to non-null type kotlin.Array<T>");
         } else {
            val var22: Array<java.lang.String> = var77 as Array<java.lang.String>;
            val var25: Array<java.lang.String> = var77 as Array<java.lang.String>;
            var var29: Int = 0;
            val var32: Int = var22.length;

            label162:
            while (var29 < var32) {
               label118: {
                  val var34: java.lang.String = var25[var29];
                  var29++;
                  val var37: java.util.List = new Regex("=").split(var34, 0);
                  if (!var37.isEmpty()) {
                     val var47: java.util.ListIterator = var37.listIterator(var37.size());

                     while (iterator$iv.hasPrevious()) {
                        if ((var47.previous() as java.lang.String).length() != 0) {
                           var10000 = CollectionsKt.take(var37, var47.nextIndex() + 1);
                           break label118;
                        }
                     }
                  }

                  var10000 = CollectionsKt.emptyList();
               }

               val var79: Array<Any> = var10000.toArray(new java.lang.String[0]);
               if (var79 == null) {
                  throw new NullPointerException("null cannot be cast to non-null type kotlin.Array<T>");
               }

               val var35: Array<java.lang.String> = var79 as Array<java.lang.String>;
               if ((var79 as Array<java.lang.String>).length != 1) {
                  val var51: java.lang.CharSequence = var35[0];
                  var var59: Int = 0;
                  var var62: Int = var51.length() - 1;
                  var `startIndex$iv$iv`: Boolean = (boolean)0;

                  while (startIndex$iv$iv <= endIndex$iv$iv) {
                     val var69: Boolean = Intrinsics.compare(var51.charAt(if (!`startIndex$iv$iv`) var59 else var62), 32) <= 0;
                     if (!`startIndex$iv$iv`) {
                        if (!var69) {
                           `startIndex$iv$iv` = (boolean)1;
                        } else {
                           var59++;
                        }
                     } else {
                        if (!var69) {
                           break;
                        }

                        var62--;
                     }
                  }

                  val var39: java.lang.String = var51.subSequence(var59, var62 + 1).toString();
                  val var45: java.lang.String = var35[1];
                  if (StringsKt.isBlank(var35[1])) {
                     val `$this$trim$iv$ivx`: java.lang.CharSequence = var45;
                     var62 = 0;
                     `startIndex$iv$iv` = (boolean)(`$this$trim$iv$ivx`.length() - 1);
                     var `startFound$iv$ivx`: Boolean = false;

                     while (true) {
                        if (var62 <= `startIndex$iv$iv`) {
                           val var73: Boolean = Intrinsics.compare(`$this$trim$iv$ivx`.charAt(if (!`startFound$iv$ivx`) var62 else `startIndex$iv$iv`), 32)
                              <= 0;
                           if (!`startFound$iv$ivx`) {
                              if (!var73) {
                                 `startFound$iv$ivx` = true;
                              } else {
                                 var62++;
                              }
                              continue;
                           }

                           if (var73) {
                              `startIndex$iv$iv`--;
                              continue;
                           }
                        }

                        if (`$this$trim$iv$ivx`.subSequence(var62, `startIndex$iv$iv` + 1).toString() == "null") {
                           break;
                        }
                        continue label162;
                     }
                  }

                  val `$this$trim$iv$ivx`: java.lang.CharSequence = var45;
                  `startIndex$iv$iv` = (boolean)0;
                  var `endIndex$iv$ivx`: Int = `$this$trim$iv$ivx`.length() - 1;
                  var `startFound$iv$ivx`: Boolean = false;

                  while (startFound$iv$iv <= endIndex$iv$ivx) {
                     val `match$iv$ivx`: Boolean = Intrinsics.compare(
                           `$this$trim$iv$ivx`.charAt(if (!`startFound$iv$ivx`) `startIndex$iv$iv` else `endIndex$iv$ivx`), 32
                        )
                        <= 0;
                     if (!`startFound$iv$ivx`) {
                        if (!`match$iv$ivx`) {
                           `startFound$iv$ivx` = true;
                        } else {
                           `startIndex$iv$iv`++;
                        }
                     } else {
                        if (!`match$iv$ivx`) {
                           break;
                        }

                        `endIndex$iv$ivx`--;
                     }
                  }

                  cookieMap.put(var39, `$this$trim$iv$ivx`.subSequence(`startIndex$iv$iv`, `endIndex$iv$ivx` + 1).toString());
               }
            }

            return cookieMap;
         }
      }
   }

   public override fun mapToCookie(cookieMap: Map<String, String>?): String? {
      if (cookieMap != null && !cookieMap.isEmpty()) {
         val builder: StringBuilder = new StringBuilder();

         for (java.lang.String key : cookieMap.keySet()) {
            val value: java.lang.String = cookieMap.get(key) as java.lang.String;
            if (value != null && !StringsKt.isBlank(value)) {
               builder.append(key).append("=").append(value).append(";");
            }
         }

         return builder.deleteCharAt(builder.lastIndexOf(";")).toString();
      } else {
         return null;
      }
   }

   public fun clear() {
      this.cacheInstance.clear();
   }
}
