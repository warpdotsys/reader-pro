package io.legado.app.data.entities

import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import com.google.gson.Gson
import io.legado.app.utils.GsonExtensionsKt
import java.lang.reflect.Type
import java.util.HashMap
import java.util.LinkedHashSet
import kotlin.Result.Companion
import kotlin.jvm.functions.Function0
import org.jetbrains.annotations.NotNull

@JsonIgnoreProperties(["variableMap", "infoHtml", "tocHtml", "origins", "kindList"])
public data class SearchBook(bookUrl: String = "",
      origin: String = "",
      originName: String = "",
      type: Int = 0,
      name: String = "",
      author: String = "",
      kind: String? = null,
      coverUrl: String? = null,
      intro: String? = null,
      wordCount: String? = null,
      latestChapterTitle: String? = null,
      tocUrl: String = "",
      time: Long = 0L,
      variable: String? = null,
      originOrder: Int = 0
   ) :
   BaseBook,
   java.lang.Comparable<SearchBook> {
   private final var _userNameSpace: String

   public open var author: String
      internal final set

   public open var bookUrl: String
      internal final set

   public final var coverUrl: String?
      internal set

   public open var infoHtml: String?
      internal final set

   public final var intro: String?
      internal set

   public open var kind: String?
      internal final set

   public final var latestChapterTitle: String?
      internal set

   public open var name: String
      internal final set

   public final var origin: String
      internal set

   public final var originName: String
      internal set

   public final var originOrder: Int
      internal set

   public final var origins: LinkedHashSet<String>?
      private set

   public final var time: Long
      internal set

   public open var tocHtml: String?
      internal final set

   public final var tocUrl: String
      internal set

   public final var type: Int
      internal set

   public final var variable: String?
      internal set

   public open val variableMap: HashMap<String, String>
      public open get() {
         return this.variableMap$delegate.getValue() as HashMap<java.lang.String, java.lang.String>;
      }


   public open var wordCount: String?
      internal final set

   init {
      this.bookUrl = bookUrl;
      this.origin = origin;
      this.originName = originName;
      this.type = type;
      this.name = name;
      this.author = author;
      this.kind = kind;
      this.coverUrl = coverUrl;
      this.intro = intro;
      this.wordCount = wordCount;
      this.latestChapterTitle = latestChapterTitle;
      this.tocUrl = tocUrl;
      this.time = time;
      this.variable = variable;
      this.originOrder = originOrder;
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
               val var17: Type = new SearchBook$variableMap$2$invoke$$inlined$fromJsonObject$1().getType();
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

   public override operator fun equals(other: Any?): Boolean {
      return other is SearchBook && (other as SearchBook).getBookUrl() == this.getBookUrl();
   }

   public override fun hashCode(): Int {
      return this.getBookUrl().hashCode();
   }

   public open operator fun compareTo(other: SearchBook): Int {
      return other.originOrder - this.originOrder;
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

   public fun addOrigin(origin: String) {
      if (this.origins == null) {
         this.origins = SetsKt.linkedSetOf(new java.lang.String[]{this.origin});
      }

      if (this.origins != null) {
         this.origins.add(origin);
      }
   }

   public fun toBook(): Book {
      val var14: Book = new Book(
         this.getBookUrl(),
         this.tocUrl,
         this.origin,
         this.originName,
         this.getName(),
         this.getAuthor(),
         this.getKind(),
         null,
         this.coverUrl,
         null,
         this.intro,
         null,
         null,
         this.type,
         0L,
         this.latestChapterTitle,
         0L,
         0L,
         0,
         0,
         null,
         0,
         0,
         0L,
         this.getWordCount(),
         false,
         0,
         0,
         false,
         this.variable,
         null,
         false,
         null,
         -553690496,
         1,
         null
      );
      var14.setInfoHtml(this.getInfoHtml());
      var14.setTocUrl(this.getTocUrl());
      var14.setUserNameSpace(this.getUserNameSpace());
      return var14;
   }

   override fun getKindList(): MutableList<java.lang.String> {
      return BaseBook.DefaultImpls.getKindList(this);
   }

   override fun getVariable(key: java.lang.String): java.lang.String? {
      return BaseBook.DefaultImpls.getVariable(this, key);
   }

   public operator fun component1(): String {
      return this.getBookUrl();
   }

   public operator fun component2(): String {
      return this.origin;
   }

   public operator fun component3(): String {
      return this.originName;
   }

   public operator fun component4(): Int {
      return this.type;
   }

   public operator fun component5(): String {
      return this.getName();
   }

   public operator fun component6(): String {
      return this.getAuthor();
   }

   public operator fun component7(): String? {
      return this.getKind();
   }

   public operator fun component8(): String? {
      return this.coverUrl;
   }

   public operator fun component9(): String? {
      return this.intro;
   }

   public operator fun component10(): String? {
      return this.getWordCount();
   }

   public operator fun component11(): String? {
      return this.latestChapterTitle;
   }

   public operator fun component12(): String {
      return this.tocUrl;
   }

   public operator fun component13(): Long {
      return this.time;
   }

   public operator fun component14(): String? {
      return this.variable;
   }

   public operator fun component15(): Int {
      return this.originOrder;
   }

   public fun copy(
      bookUrl: String = this.getBookUrl(),
      origin: String = this.origin,
      originName: String = this.originName,
      type: Int = this.type,
      name: String = this.getName(),
      author: String = this.getAuthor(),
      kind: String? = this.getKind(),
      coverUrl: String? = this.coverUrl,
      intro: String? = this.intro,
      wordCount: String? = this.getWordCount(),
      latestChapterTitle: String? = this.latestChapterTitle,
      tocUrl: String = this.tocUrl,
      time: Long = this.time,
      variable: String? = this.variable,
      originOrder: Int = this.originOrder
   ): SearchBook {
      return new SearchBook(
         bookUrl, origin, originName, type, name, author, kind, coverUrl, intro, wordCount, latestChapterTitle, tocUrl, time, variable, originOrder
      );
   }

   public override fun toString(): String {
      val var1: StringBuilder = new StringBuilder();
      var1.append("SearchBook(bookUrl=")
         .append(this.getBookUrl())
         .append(", origin=")
         .append(this.origin)
         .append(", originName=")
         .append(this.originName)
         .append(", type=")
         .append(this.type)
         .append(", name=")
         .append(this.getName())
         .append(", author=")
         .append(this.getAuthor())
         .append(", kind=")
         .append(this.getKind())
         .append(", coverUrl=")
         .append(this.coverUrl)
         .append(", intro=")
         .append(this.intro)
         .append(", wordCount=")
         .append(this.getWordCount())
         .append(", latestChapterTitle=")
         .append(this.latestChapterTitle)
         .append(", tocUrl=");
      var1.append(this.tocUrl)
         .append(", time=")
         .append(this.time)
         .append(", variable=")
         .append(this.variable)
         .append(", originOrder=")
         .append(this.originOrder)
         .append(')');
      return var1.toString();
   }

   fun SearchBook() {
      this(null, null, null, 0, null, null, null, null, null, null, null, null, 0L, null, 0, 32767, null);
   }
}
