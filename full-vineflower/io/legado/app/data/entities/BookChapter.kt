package io.legado.app.data.entities

import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import com.google.gson.Gson
import io.legado.app.model.analyzeRule.AnalyzeUrl
import io.legado.app.model.analyzeRule.RuleDataInterface
import io.legado.app.utils.GsonExtensionsKt
import io.legado.app.utils.MD5Utils
import io.legado.app.utils.NetworkUtils
import java.lang.reflect.Type
import java.util.Arrays
import java.util.HashMap
import java.util.regex.Matcher
import kotlin.Result.Companion
import kotlin.jvm.functions.Function0
import kotlin.jvm.internal.StringCompanionObject
import org.jetbrains.annotations.NotNull

@JsonIgnoreProperties(["variableMap", "_userNameSpace", "userNameSpace"])
public data class BookChapter(url: String = "",
      title: String = "",
      isVolume: Boolean = false,
      baseUrl: String = "",
      bookUrl: String = "",
      index: Int = 0,
      resourceUrl: String? = null,
      tag: String? = null,
      start: Long? = null,
      end: Long? = null,
      startFragmentId: String? = null,
      endFragmentId: String? = null,
      variable: String? = null
   ) :
   RuleDataInterface {
   private final var _userNameSpace: String

   public final var baseUrl: String
      internal set

   public final var bookUrl: String
      internal set

   public final var end: Long?
      internal set

   public final var endFragmentId: String?
      internal set

   public final var index: Int
      internal set

   public final var isVolume: Boolean
      internal set

   public final var resourceUrl: String?
      internal set

   public final var start: Long?
      internal set

   public final var startFragmentId: String?
      internal set

   public final var tag: String?
      internal set

   public final var title: String
      internal set

   public final var url: String
      internal set

   public final var variable: String?
      internal set

   public open val variableMap: HashMap<String, String>
      public open get() {
         return this.variableMap$delegate.getValue() as HashMap<java.lang.String, java.lang.String>;
      }


   init {
      this.url = url;
      this.title = title;
      this.isVolume = isVolume;
      this.baseUrl = baseUrl;
      this.bookUrl = bookUrl;
      this.index = index;
      this.resourceUrl = resourceUrl;
      this.tag = tag;
      this.start = start;
      this.end = end;
      this.startFragmentId = startFragmentId;
      this.endFragmentId = endFragmentId;
      this.variable = variable;
      this.variableMap$delegate = LazyKt.lazy((new Function0<HashMap<java.lang.String, java.lang.String>>(this) {
         {
            super(0);
            this.this$0 = `$receiver`;
         }

         @NotNull
         public final HashMap<java.lang.String, java.lang.String> invoke() {
            val `$this$fromJsonObject$iv`: Gson = GsonExtensionsKt.getGSON();
            val `json$iv`: java.lang.String = this.this$0.getVariable();

            var var6: Any;
            try {
               var6 = Result.Companion;
               val var17: Type = new BookChapter$variableMap$2$invoke$$inlined$fromJsonObject$1().getType();
               var var10000: Any = `$this$fromJsonObject$iv`.fromJson(`json$iv`, var17);
               if (var10000 !is HashMap) {
                  var10000 = null;
               }

               var6 = Result.constructor-impl(var10000 as HashMap);
            } catch (var10: java.lang.Throwable) {
               val `$i$f$genericType`: Companion = Result.Companion;
               var6 = Result.constructor-impl(ResultKt.createFailure(var10));
            }

            val var1: HashMap = (if (Result.isFailure-impl(var6)) null else var6) as HashMap;
            return if (var1 == null) new HashMap<>() else var1;
         }
      }) as Function0);
      this._userNameSpace = "";
   }

   public override fun putVariable(key: String, value: String?) {
      if (value != null) {
         this.getVariableMap().put(key, value);
      } else {
         this.getVariableMap().remove(key);
      }

      this.variable = GsonExtensionsKt.getGSON().toJson(this.getVariableMap());
   }

   public fun setUserNameSpace(nameSpace: String) {
      this._userNameSpace = nameSpace;
   }

   public override fun getUserNameSpace(): String {
      return this._userNameSpace;
   }

   public override fun hashCode(): Int {
      return this.url.hashCode();
   }

   public override operator fun equals(other: Any?): Boolean {
      return other is BookChapter && (other as BookChapter).url == this.url;
   }

   public fun getAbsoluteURL(): String {
      val urlMatcher: Matcher = AnalyzeUrl.Companion.getParamPattern().matcher(this.url);
      var var10000: java.lang.String;
      if (urlMatcher.find()) {
         val urlAbsoluteBefore: java.lang.String = this.url;
         val var5: Int = urlMatcher.start();
         if (urlAbsoluteBefore == null) {
            throw new NullPointerException("null cannot be cast to non-null type java.lang.String");
         }

         var10000 = urlAbsoluteBefore.substring(0, var5);
      } else {
         var10000 = this.url;
      }

      val var7: java.lang.String = NetworkUtils.INSTANCE.getAbsoluteURL(this.baseUrl, var10000);
      if (var10000.length() == this.url.length()) {
         var10000 = var7;
      } else {
         val var12: StringBuilder = new StringBuilder().append(var7).append(',');
         val var8: java.lang.String = this.url;
         val var9: Int = urlMatcher.end();
         if (var8 == null) {
            throw new NullPointerException("null cannot be cast to non-null type java.lang.String");
         }

         val var10001: java.lang.String = var8.substring(var9);
         var10000 = var12.append(var10001).toString();
      }

      return var10000;
   }

   public fun getFileName(): String {
      val var1: StringCompanionObject = StringCompanionObject.INSTANCE;
      val var3: Array<Any> = new Object[]{this.index, MD5Utils.INSTANCE.md5Encode16(this.title)};
      val var10000: java.lang.String = java.lang.String.format("%05d-%s.nb", Arrays.copyOf(var3, var3.length));
      return var10000;
   }

   override fun getVariable(key: java.lang.String): java.lang.String? {
      return RuleDataInterface.DefaultImpls.getVariable(this, key);
   }

   public operator fun component1(): String {
      return this.url;
   }

   public operator fun component2(): String {
      return this.title;
   }

   public operator fun component3(): Boolean {
      return this.isVolume;
   }

   public operator fun component4(): String {
      return this.baseUrl;
   }

   public operator fun component5(): String {
      return this.bookUrl;
   }

   public operator fun component6(): Int {
      return this.index;
   }

   public operator fun component7(): String? {
      return this.resourceUrl;
   }

   public operator fun component8(): String? {
      return this.tag;
   }

   public operator fun component9(): Long? {
      return this.start;
   }

   public operator fun component10(): Long? {
      return this.end;
   }

   public operator fun component11(): String? {
      return this.startFragmentId;
   }

   public operator fun component12(): String? {
      return this.endFragmentId;
   }

   public operator fun component13(): String? {
      return this.variable;
   }

   public fun copy(
      url: String = this.url,
      title: String = this.title,
      isVolume: Boolean = this.isVolume,
      baseUrl: String = this.baseUrl,
      bookUrl: String = this.bookUrl,
      index: Int = this.index,
      resourceUrl: String? = this.resourceUrl,
      tag: String? = this.tag,
      start: Long? = this.start,
      end: Long? = this.end,
      startFragmentId: String? = this.startFragmentId,
      endFragmentId: String? = this.endFragmentId,
      variable: String? = this.variable
   ): BookChapter {
      return new BookChapter(url, title, isVolume, baseUrl, bookUrl, index, resourceUrl, tag, start, end, startFragmentId, endFragmentId, variable);
   }

   public override fun toString(): String {
      val var1: StringBuilder = new StringBuilder();
      var1.append("BookChapter(url=")
         .append(this.url)
         .append(", title=")
         .append(this.title)
         .append(", isVolume=")
         .append(this.isVolume)
         .append(", baseUrl=")
         .append(this.baseUrl)
         .append(", bookUrl=")
         .append(this.bookUrl)
         .append(", index=")
         .append(this.index)
         .append(", resourceUrl=")
         .append(this.resourceUrl)
         .append(", tag=")
         .append(this.tag)
         .append(", start=")
         .append(this.start)
         .append(", end=")
         .append(this.end)
         .append(", startFragmentId=")
         .append(this.startFragmentId)
         .append(", endFragmentId=");
      var1.append(this.endFragmentId).append(", variable=").append(this.variable).append(')');
      return var1.toString();
   }

   fun BookChapter() {
      this(null, null, false, null, null, 0, null, null, null, null, null, null, null, 8191, null);
   }
}
