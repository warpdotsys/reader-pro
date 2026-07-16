package io.legado.app.data.entities

import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import com.google.gson.Gson
import io.legado.app.model.analyzeRule.RuleDataInterface
import io.legado.app.utils.GsonExtensionsKt
import java.lang.reflect.Type
import java.util.HashMap
import kotlin.Result.Companion
import kotlin.jvm.functions.Function0
import org.jetbrains.annotations.NotNull

@JsonIgnoreProperties(["variableMap", "_userNameSpace", "userNameSpace"])
public data class RssArticle(origin: String = "",
      sort: String = "",
      title: String = "",
      order: Long = 0L,
      link: String = "",
      pubDate: String? = null,
      description: String? = null,
      content: String? = null,
      image: String? = null,
      read: Boolean = false,
      variable: String? = null
   ) :
   RuleDataInterface {
   private final var _userNameSpace: String

   public final var content: String?
      internal set

   public final var description: String?
      internal set

   public final var image: String?
      internal set

   public final var link: String
      internal set

   public final var order: Long
      internal set

   public final var origin: String
      internal set

   public final var pubDate: String?
      internal set

   public final var read: Boolean
      internal set

   public final var sort: String
      internal set

   public final var title: String
      internal set

   public final var variable: String?
      internal set

   public open val variableMap: HashMap<String, String>
      public open get() {
         return this.variableMap$delegate.getValue() as HashMap<java.lang.String, java.lang.String>;
      }


   init {
      this.origin = origin;
      this.sort = sort;
      this.title = title;
      this.order = order;
      this.link = link;
      this.pubDate = pubDate;
      this.description = description;
      this.content = content;
      this.image = image;
      this.read = read;
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
               val var17: Type = new RssArticle$variableMap$2$invoke$$inlined$fromJsonObject$1().getType();
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

   public override fun hashCode(): Int {
      return this.link.hashCode();
   }

   public override operator fun equals(other: Any?): Boolean {
      if (other == null) {
         return false;
      } else {
         return other is RssArticle && this.origin == (other as RssArticle).origin && this.link == (other as RssArticle).link;
      }
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

   override fun getVariable(key: java.lang.String): java.lang.String? {
      return RuleDataInterface.DefaultImpls.getVariable(this, key);
   }

   public operator fun component1(): String {
      return this.origin;
   }

   public operator fun component2(): String {
      return this.sort;
   }

   public operator fun component3(): String {
      return this.title;
   }

   public operator fun component4(): Long {
      return this.order;
   }

   public operator fun component5(): String {
      return this.link;
   }

   public operator fun component6(): String? {
      return this.pubDate;
   }

   public operator fun component7(): String? {
      return this.description;
   }

   public operator fun component8(): String? {
      return this.content;
   }

   public operator fun component9(): String? {
      return this.image;
   }

   public operator fun component10(): Boolean {
      return this.read;
   }

   public operator fun component11(): String? {
      return this.variable;
   }

   public fun copy(
      origin: String = this.origin,
      sort: String = this.sort,
      title: String = this.title,
      order: Long = this.order,
      link: String = this.link,
      pubDate: String? = this.pubDate,
      description: String? = this.description,
      content: String? = this.content,
      image: String? = this.image,
      read: Boolean = this.read,
      variable: String? = this.variable
   ): RssArticle {
      return new RssArticle(origin, sort, title, order, link, pubDate, description, content, image, read, variable);
   }

   public override fun toString(): String {
      val var1: StringBuilder = new StringBuilder();
      var1.append("RssArticle(origin=")
         .append(this.origin)
         .append(", sort=")
         .append(this.sort)
         .append(", title=")
         .append(this.title)
         .append(", order=")
         .append(this.order)
         .append(", link=")
         .append(this.link)
         .append(", pubDate=")
         .append(this.pubDate)
         .append(", description=")
         .append(this.description)
         .append(", content=")
         .append(this.content)
         .append(", image=")
         .append(this.image)
         .append(", read=")
         .append(this.read)
         .append(", variable=")
         .append(this.variable)
         .append(')');
      return var1.toString();
   }

   fun RssArticle() {
      this(null, null, null, 0L, null, null, null, null, null, false, null, 2047, null);
   }
}
