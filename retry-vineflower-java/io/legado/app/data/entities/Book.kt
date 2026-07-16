package io.legado.app.data.entities

import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import com.google.gson.Gson
import com.jayway.jsonpath.DocumentContext
import com.jayway.jsonpath.Predicate
import io.legado.app.constant.AppPattern
import io.legado.app.model.localBook.CbzFile
import io.legado.app.model.localBook.EpubFile
import io.legado.app.model.localBook.LocalBook
import io.legado.app.model.localBook.UmdFile
import io.legado.app.utils.FileUtils
import io.legado.app.utils.GsonExtensionsKt
import io.legado.app.utils.JsonExtensionsKt
import io.legado.app.utils.MD5Utils
import java.io.File
import java.lang.reflect.Type
import java.nio.charset.Charset
import java.util.ArrayList
import java.util.HashMap
import kotlin.jvm.functions.Function0
import kotlin.jvm.internal.Intrinsics
import org.jetbrains.annotations.NotNull
import org.jsoup.Jsoup

@JsonIgnoreProperties(["variableMap", "infoHtml", "tocHtml", "config", "rootDir", "localBook", "epub", "epubRootDir", "onLineTxt", "localTxt", "umd", "realAuthor", "unreadChapterNum", "folderName", "pdfImageWidth", "localFile", "kindList", "_userNameSpace", "bookDir", "userNameSpace"])
public data class Book(bookUrl: String = "",
      tocUrl: String = "",
      origin: String = "loc_book",
      originName: String = "",
      name: String = "",
      author: String = "",
      kind: String? = null,
      customTag: String? = null,
      coverUrl: String? = null,
      customCoverUrl: String? = null,
      intro: String? = null,
      customIntro: String? = null,
      charset: String? = null,
      type: Int = 0,
      group: Long = 0L,
      latestChapterTitle: String? = null,
      latestChapterTime: Long = System.currentTimeMillis(),
      lastCheckTime: Long = System.currentTimeMillis(),
      lastCheckCount: Int = 0,
      totalChapterNum: Int = 0,
      durChapterTitle: String? = null,
      durChapterIndex: Int = 0,
      durChapterPos: Int = 0,
      durChapterTime: Long = System.currentTimeMillis(),
      wordCount: String? = null,
      canUpdate: Boolean = true,
      order: Int = 0,
      originOrder: Int = 0,
      useReplaceRule: Boolean = true,
      variable: String? = null,
      readConfig: io.legado.app.data.entities.Book.ReadConfig? = null,
      isInShelf: Boolean = false,
      lastCheckError: String? = null
   ) :
   BaseBook {
   private final var _userNameSpace: String

   public open var author: String
      internal final set

   public open var bookUrl: String
      internal final set

   public final var canUpdate: Boolean
      internal set

   public final var charset: String?
      internal set

   public final var coverUrl: String?
      internal set

   public final var customCoverUrl: String?
      internal set

   public final var customIntro: String?
      internal set

   public final var customTag: String?
      internal set

   public final var durChapterIndex: Int
      internal set

   public final var durChapterPos: Int
      internal set

   public final var durChapterTime: Long
      internal set

   public final var durChapterTitle: String?
      internal set

   public final var group: Long
      internal set

   public open var infoHtml: String?
      internal final set

   public final var intro: String?
      internal set

   public final var isInShelf: Boolean
      internal set

   public open var kind: String?
      internal final set

   public final var lastCheckCount: Int
      internal set

   public final var lastCheckError: String?
      internal set

   public final var lastCheckTime: Long
      internal set

   public final var latestChapterTime: Long
      internal set

   public final var latestChapterTitle: String?
      internal set

   public open var name: String
      internal final set

   public final var order: Int
      internal set

   public final var origin: String
      internal set

   public final var originName: String
      internal set

   public final var originOrder: Int
      internal set

   public final var readConfig: io.legado.app.data.entities.Book.ReadConfig?
      internal set

   private final var rootDir: String

   public open var tocHtml: String?
      internal final set

   public final var tocUrl: String
      internal set

   public final var totalChapterNum: Int
      internal set

   public final var type: Int
      internal set

   public final var useReplaceRule: Boolean
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
      this.tocUrl = tocUrl;
      this.origin = origin;
      this.originName = originName;
      this.name = name;
      this.author = author;
      this.kind = kind;
      this.customTag = customTag;
      this.coverUrl = coverUrl;
      this.customCoverUrl = customCoverUrl;
      this.intro = intro;
      this.customIntro = customIntro;
      this.charset = charset;
      this.type = type;
      this.group = group;
      this.latestChapterTitle = latestChapterTitle;
      this.latestChapterTime = latestChapterTime;
      this.lastCheckTime = lastCheckTime;
      this.lastCheckCount = lastCheckCount;
      this.totalChapterNum = totalChapterNum;
      this.durChapterTitle = durChapterTitle;
      this.durChapterIndex = durChapterIndex;
      this.durChapterPos = durChapterPos;
      this.durChapterTime = durChapterTime;
      this.wordCount = wordCount;
      this.canUpdate = canUpdate;
      this.order = order;
      this.originOrder = originOrder;
      this.useReplaceRule = useReplaceRule;
      this.variable = variable;
      this.readConfig = readConfig;
      this.isInShelf = isInShelf;
      this.lastCheckError = lastCheckError;
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
               val var17: Type = new Book$variableMap$2$invoke$$inlined$fromJsonObject$1().getType();
               var var10000: Any = `$this$fromJsonObject$iv`.fromJson(`json$iv`, var17);
               if (var10000 !is HashMap) {
                  var10000 = null;
               }

               var6 = Result.constructor-impl(var10000 as HashMap);
            } catch (var10: java.lang.Throwable) {
               val `$i$f$genericType`: Result.Companion = Result.Companion;
               var6 = Result.constructor-impl(ResultKt.createFailure(var10));
            }

            val var1: HashMap = (if (Result.isFailure-impl(var6)) null else var6) as HashMap;
            return if (var1 == null) new HashMap<>() else var1;
         }
      }) as Function0);
      this.rootDir = "";
      this._userNameSpace = "";
   }

   public fun isLocalBook(): Boolean {
      return this.origin == "loc_book";
   }

   public fun isLocalTxt(): Boolean {
      return this.isLocalBook() && StringsKt.endsWith(this.originName, ".txt", true);
   }

   public fun isLocalEpub(): Boolean {
      return this.isLocalBook() && StringsKt.endsWith(this.originName, ".epub", true);
   }

   public fun isLocalPdf(): Boolean {
      return this.isLocalBook() && StringsKt.endsWith(this.originName, ".pdf", true);
   }

   public fun isEpub(): Boolean {
      return StringsKt.endsWith(this.originName, ".epub", true);
   }

   public fun isCbz(): Boolean {
      return StringsKt.endsWith(this.originName, ".cbz", true);
   }

   public fun isPdf(): Boolean {
      return StringsKt.endsWith(this.originName, ".pdf", true);
   }

   public fun isUmd(): Boolean {
      return StringsKt.endsWith(this.originName, ".umd", true);
   }

   public fun isOnLineTxt(): Boolean {
      return !this.isLocalBook() && this.type == 0;
   }

   public override operator fun equals(other: Any?): Boolean {
      return other is Book && (other as Book).getBookUrl() == this.getBookUrl();
   }

   public override fun hashCode(): Int {
      return this.getBookUrl().hashCode();
   }

   public override fun putVariable(key: String, value: String?) {
      if (value != null) {
         this.getVariableMap().put(key, value);
      } else {
         this.getVariableMap().remove(key);
      }

      this.variable = GsonExtensionsKt.getGSON().toJson(this.getVariableMap());
   }

   public fun getRealAuthor(): String {
      return AppPattern.INSTANCE.getAuthorRegex().replace(this.getAuthor(), "");
   }

   public fun getUnreadChapterNum(): Int {
      return Math.max(this.totalChapterNum - this.durChapterIndex - 1, 0);
   }

   public fun getDisplayCover(): String? {
      return if (this.customCoverUrl as java.lang.CharSequence == null || this.customCoverUrl.length() == 0) this.coverUrl else this.customCoverUrl;
   }

   public fun getDisplayIntro(): String? {
      return if (this.customIntro as java.lang.CharSequence == null || this.customIntro.length() == 0) this.intro else this.customIntro;
   }

   public fun fileCharset(): Charset {
      val var10000: Charset = Charset.forName(if (this.charset == null) "UTF-8" else this.charset);
      return var10000;
   }

   private fun config(): io.legado.app.data.entities.Book.ReadConfig {
      if (this.readConfig == null) {
         this.readConfig = new Book.ReadConfig(false, 0, false, null, false, 0L, 0.0F, 127, null);
      }

      val var10000: Book.ReadConfig = this.readConfig;
      return var10000;
   }

   public fun setDelTag(tag: Long) {
      this.config().setDelTag(if ((this.config().getDelTag() and tag) == tag) this.config().getDelTag() and tag.inv() else this.config().getDelTag() or tag);
   }

   public fun getDelTag(tag: Long): Boolean {
      return (this.config().getDelTag() and tag) == tag;
   }

   public fun getPdfImageWidth(): Float {
      return this.config().getPdfImageWidth();
   }

   public fun setPdfImageWidth(pdfImageWidth: Float) {
      this.config().setPdfImageWidth(pdfImageWidth);
   }

   public fun getFolderName(): String {
      val folderName: java.lang.String = AppPattern.INSTANCE.getFileNameRegex().replace(this.getName(), "");
      val var10: Int = Math.min(9, folderName.length());
      if (folderName == null) {
         throw new NullPointerException("null cannot be cast to non-null type java.lang.String");
      } else {
         val var10000: java.lang.String = folderName.substring(0, var10);
         return Intrinsics.stringPlus(var10000, MD5Utils.INSTANCE.md5Encode16(this.getBookUrl()));
      }
   }

   public fun setRootDir(root: String) {
      if (root.length() > 0) {
         val var4: java.lang.String = File.separator;
         if (!StringsKt.endsWith$default(root, var4, false, 2, null)) {
            this.rootDir = Intrinsics.stringPlus(root, File.separator);
            return;
         }
      }

      this.rootDir = root;
   }

   public fun getLocalFile(): File {
      if (StringsKt.startsWith$default(this.originName, this.rootDir, false, 2, null)) {
         this.originName = StringsKt.replaceFirst$default(this.originName, this.rootDir, "", false, 4, null);
      }

      BookKt.getLogger().info("getLocalFile rootDir: {} originName: {}", this.rootDir, this.originName);
      if (this.isEpub()
         && StringsKt.indexOf$default(this.originName, "localStore", 0, false, 6, null) < 0
         && StringsKt.indexOf$default(this.originName, "webdav", 0, false, 6, null) < 0) {
         return FileUtils.INSTANCE.getFile(new File(Intrinsics.stringPlus(this.rootDir, this.originName)), "index.epub");
      } else {
         label25:
         if (this.isCbz()
            && StringsKt.indexOf$default(this.originName, "localStore", 0, false, 6, null) < 0
            && StringsKt.indexOf$default(this.originName, "webdav", 0, false, 6, null) < 0) {
            return FileUtils.INSTANCE.getFile(new File(Intrinsics.stringPlus(this.rootDir, this.originName)), "index.cbz");
         } else {
            return if (this.isPdf()
                  && StringsKt.indexOf$default(this.originName, "localStore", 0, false, 6, null) < 0
                  && StringsKt.indexOf$default(this.originName, "webdav", 0, false, 6, null) < 0)
               FileUtils.INSTANCE.getFile(new File(Intrinsics.stringPlus(this.rootDir, this.originName)), "index.pdf")
               else
               new File(Intrinsics.stringPlus(this.rootDir, this.originName));
         }
      }
   }

   public fun setUserNameSpace(nameSpace: String) {
      this._userNameSpace = nameSpace;
   }

   public override fun getUserNameSpace(): String {
      return this._userNameSpace;
   }

   public fun getBookDir(): String {
      return FileUtils.INSTANCE.getPath(new File(this.rootDir), "storage", "data", this._userNameSpace, "${this.getName()}_${this.getAuthor()}");
   }

   public fun getSplitLongChapter(): Boolean {
      return false;
   }

   public fun toSearchBook(): SearchBook {
      val var14: SearchBook = new SearchBook(
         this.getBookUrl(),
         this.origin,
         this.originName,
         this.type,
         this.getName(),
         this.getAuthor(),
         this.getKind(),
         this.coverUrl,
         this.intro,
         this.getWordCount(),
         this.latestChapterTitle,
         this.tocUrl,
         0L,
         this.variable,
         0,
         20480,
         null
      );
      var14.setInfoHtml(this.getInfoHtml());
      var14.setTocHtml(this.getTocHtml());
      var14.setUserNameSpace(this.getUserNameSpace());
      return var14;
   }

   public fun getEpubRootDir(): String {
      val containerRes: File = new File("${this.getBookUrl()}${File.separator}index${File.separator}META-INF${File.separator}container.xml");
      if (containerRes.exists()) {
         try {
            val result: java.lang.String = Jsoup.parse(FilesKt.readText$default(containerRes, null, 1, null))
               .getElementsByTag("rootfiles")
               .get(0)
               .getElementsByTag("rootfile")
               .get(0)
               .attr("full-path");
            System.out.println(Intrinsics.stringPlus("result: ", result));
            if (result != null && result.length() > 0) {
               val var14: File = new File(result).getParentFile();
               val var10000: java.lang.String;
               if (var14 == null) {
                  var10000 = "";
               } else {
                  val var15: java.lang.String = var14.toString();
                  var10000 = if (var15 == null) "" else var15;
               }

               return var10000;
            }
         } catch (var13: Exception) {
            var13.printStackTrace();
         }
      }

      return "OEBPS";
   }

   public fun updateFromLocal(onlyCover: Boolean = false) {
      try {
         if (this.isEpub()) {
            EpubFile.Companion.upBookInfo(this, onlyCover);
         } else if (this.isUmd()) {
            UmdFile.Companion.upBookInfo(this, onlyCover);
         } else if (this.isCbz()) {
            CbzFile.Companion.upBookInfo(this, onlyCover);
         }
      } catch (var3: Exception) {
         var3.printStackTrace();
      }
   }

   public fun workRoot(): String {
      return this.rootDir;
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
      return this.tocUrl;
   }

   public operator fun component3(): String {
      return this.origin;
   }

   public operator fun component4(): String {
      return this.originName;
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
      return this.customTag;
   }

   public operator fun component9(): String? {
      return this.coverUrl;
   }

   public operator fun component10(): String? {
      return this.customCoverUrl;
   }

   public operator fun component11(): String? {
      return this.intro;
   }

   public operator fun component12(): String? {
      return this.customIntro;
   }

   public operator fun component13(): String? {
      return this.charset;
   }

   public operator fun component14(): Int {
      return this.type;
   }

   public operator fun component15(): Long {
      return this.group;
   }

   public operator fun component16(): String? {
      return this.latestChapterTitle;
   }

   public operator fun component17(): Long {
      return this.latestChapterTime;
   }

   public operator fun component18(): Long {
      return this.lastCheckTime;
   }

   public operator fun component19(): Int {
      return this.lastCheckCount;
   }

   public operator fun component20(): Int {
      return this.totalChapterNum;
   }

   public operator fun component21(): String? {
      return this.durChapterTitle;
   }

   public operator fun component22(): Int {
      return this.durChapterIndex;
   }

   public operator fun component23(): Int {
      return this.durChapterPos;
   }

   public operator fun component24(): Long {
      return this.durChapterTime;
   }

   public operator fun component25(): String? {
      return this.getWordCount();
   }

   public operator fun component26(): Boolean {
      return this.canUpdate;
   }

   public operator fun component27(): Int {
      return this.order;
   }

   public operator fun component28(): Int {
      return this.originOrder;
   }

   public operator fun component29(): Boolean {
      return this.useReplaceRule;
   }

   public operator fun component30(): String? {
      return this.variable;
   }

   public operator fun component31(): io.legado.app.data.entities.Book.ReadConfig? {
      return this.readConfig;
   }

   public operator fun component32(): Boolean {
      return this.isInShelf;
   }

   public operator fun component33(): String? {
      return this.lastCheckError;
   }

   public fun copy(
      bookUrl: String = this.getBookUrl(),
      tocUrl: String = this.tocUrl,
      origin: String = this.origin,
      originName: String = this.originName,
      name: String = this.getName(),
      author: String = this.getAuthor(),
      kind: String? = this.getKind(),
      customTag: String? = this.customTag,
      coverUrl: String? = this.coverUrl,
      customCoverUrl: String? = this.customCoverUrl,
      intro: String? = this.intro,
      customIntro: String? = this.customIntro,
      charset: String? = this.charset,
      type: Int = this.type,
      group: Long = this.group,
      latestChapterTitle: String? = this.latestChapterTitle,
      latestChapterTime: Long = this.latestChapterTime,
      lastCheckTime: Long = this.lastCheckTime,
      lastCheckCount: Int = this.lastCheckCount,
      totalChapterNum: Int = this.totalChapterNum,
      durChapterTitle: String? = this.durChapterTitle,
      durChapterIndex: Int = this.durChapterIndex,
      durChapterPos: Int = this.durChapterPos,
      durChapterTime: Long = this.durChapterTime,
      wordCount: String? = this.getWordCount(),
      canUpdate: Boolean = this.canUpdate,
      order: Int = this.order,
      originOrder: Int = this.originOrder,
      useReplaceRule: Boolean = this.useReplaceRule,
      variable: String? = this.variable,
      readConfig: io.legado.app.data.entities.Book.ReadConfig? = this.readConfig,
      isInShelf: Boolean = this.isInShelf,
      lastCheckError: String? = this.lastCheckError
   ): Book {
      return new Book(
         bookUrl,
         tocUrl,
         origin,
         originName,
         name,
         author,
         kind,
         customTag,
         coverUrl,
         customCoverUrl,
         intro,
         customIntro,
         charset,
         type,
         group,
         latestChapterTitle,
         latestChapterTime,
         lastCheckTime,
         lastCheckCount,
         totalChapterNum,
         durChapterTitle,
         durChapterIndex,
         durChapterPos,
         durChapterTime,
         wordCount,
         canUpdate,
         order,
         originOrder,
         useReplaceRule,
         variable,
         readConfig,
         isInShelf,
         lastCheckError
      );
   }

   public override fun toString(): String {
      val var1: StringBuilder = new StringBuilder();
      var1.append("Book(bookUrl=")
         .append(this.getBookUrl())
         .append(", tocUrl=")
         .append(this.tocUrl)
         .append(", origin=")
         .append(this.origin)
         .append(", originName=")
         .append(this.originName)
         .append(", name=")
         .append(this.getName())
         .append(", author=")
         .append(this.getAuthor())
         .append(", kind=")
         .append(this.getKind())
         .append(", customTag=")
         .append(this.customTag)
         .append(", coverUrl=")
         .append(this.coverUrl)
         .append(", customCoverUrl=")
         .append(this.customCoverUrl)
         .append(", intro=")
         .append(this.intro)
         .append(", customIntro=");
      var1.append(this.customIntro)
         .append(", charset=")
         .append(this.charset)
         .append(", type=")
         .append(this.type)
         .append(", group=")
         .append(this.group)
         .append(", latestChapterTitle=")
         .append(this.latestChapterTitle)
         .append(", latestChapterTime=")
         .append(this.latestChapterTime)
         .append(", lastCheckTime=")
         .append(this.lastCheckTime)
         .append(", lastCheckCount=")
         .append(this.lastCheckCount)
         .append(", totalChapterNum=")
         .append(this.totalChapterNum)
         .append(", durChapterTitle=")
         .append(this.durChapterTitle)
         .append(", durChapterIndex=")
         .append(this.durChapterIndex)
         .append(", durChapterPos=")
         .append(this.durChapterPos);
      var1.append(", durChapterTime=")
         .append(this.durChapterTime)
         .append(", wordCount=")
         .append(this.getWordCount())
         .append(", canUpdate=")
         .append(this.canUpdate)
         .append(", order=")
         .append(this.order)
         .append(", originOrder=")
         .append(this.originOrder)
         .append(", useReplaceRule=")
         .append(this.useReplaceRule)
         .append(", variable=")
         .append(this.variable)
         .append(", readConfig=")
         .append(this.readConfig)
         .append(", isInShelf=")
         .append(this.isInShelf)
         .append(", lastCheckError=")
         .append(this.lastCheckError)
         .append(')');
      return var1.toString();
   }

   fun Book() {
      this(
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
   }

   public companion object {
      public const val hTag: Long
      public const val imgStyleDefault: String
      public const val imgStyleFull: String
      public const val imgStyleText: String
      public const val imgTag: Long
      public const val rubyTag: Long

      public fun initLocalBook(bookUrl: String, localPath: String, rootDir: String = ""): Book {
         val fileName: java.lang.String = new File(localPath).getName();
         val var10000: LocalBook = LocalBook.INSTANCE;
         val nameAuthor: Pair = var10000.analyzeNameAuthor(fileName);
         val var7: Book = new Book(
            bookUrl,
            "",
            "loc_book",
            localPath,
            nameAuthor.getFirst() as java.lang.String,
            nameAuthor.getSecond() as java.lang.String,
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
            -64,
            1,
            null
         );
         var7.setCanUpdate(false);
         var7.setRootDir(rootDir);
         Book.updateFromLocal$default(var7, false, 1, null);
         return var7;
      }

      public fun fromJsonDoc(doc: DocumentContext): Result<Book> {
         var var3: Any;
         try {
            var3 = Result.Companion;
            val var92: Any = doc.read("$.readConfig", new Predicate[0]);
            val var10000: java.lang.String = JsonExtensionsKt.readString(doc, "$.bookUrl");
            val var10001: java.lang.String = JsonExtensionsKt.readString(doc, "$.tocUrl");
            var var94: java.lang.String = JsonExtensionsKt.readString(doc, "$.origin");
            val var10002: java.lang.String = if (var94 == null) "loc_book" else var94;
            var94 = JsonExtensionsKt.readString(doc, "$.originName");
            val var10003: java.lang.String = if (var94 == null) "" else var94;
            val var10004: java.lang.String = JsonExtensionsKt.readString(doc, "$.name");
            var94 = JsonExtensionsKt.readString(doc, "$.author");
            var var10005: java.lang.String = if (var94 == null) "" else var94;
            val var10006: java.lang.String = JsonExtensionsKt.readString(doc, "$.kind");
            val var10007: java.lang.String = JsonExtensionsKt.readString(doc, "$.customTag");
            val var10008: java.lang.String = JsonExtensionsKt.readString(doc, "$.coverUrl");
            val var10009: java.lang.String = JsonExtensionsKt.readString(doc, "$.customCoverUrl");
            val var10010: java.lang.String = JsonExtensionsKt.readString(doc, "$.intro");
            val var10011: java.lang.String = JsonExtensionsKt.readString(doc, "$.customIntro");
            val var10012: java.lang.String = JsonExtensionsKt.readString(doc, "$.charset");
            val var97: Int = JsonExtensionsKt.readInt(doc, "$.type");
            val var10013: Int = if (var97 == null) 0 else var97;
            val var98: java.lang.Long = JsonExtensionsKt.readLong(doc, "$.group");
            val var10014: Long = if (var98 == null) 0L else var98;
            val var10015: java.lang.String = JsonExtensionsKt.readString(doc, "$.latestChapterTitle");
            val var99: java.lang.Long = JsonExtensionsKt.readLong(doc, "$.latestChapterTime");
            val var10016: Long = if (var99 == null) System.currentTimeMillis() else var99;
            val var100: java.lang.Long = JsonExtensionsKt.readLong(doc, "$.lastCheckTime");
            val var10017: Long = if (var100 == null) System.currentTimeMillis() else var100;
            val var101: Int = JsonExtensionsKt.readInt(doc, "$.lastCheckCount");
            val var10018: Int = if (var101 == null) 0 else var101;
            val var102: Int = JsonExtensionsKt.readInt(doc, "$.totalChapterNum");
            val var10019: Int = if (var102 == null) 0 else var102;
            val var10020: java.lang.String = JsonExtensionsKt.readString(doc, "$.durChapterTitle");
            val var103: Int = JsonExtensionsKt.readInt(doc, "$.durChapterIndex");
            val var10021: Int = if (var103 == null) 0 else var103;
            val var104: Int = JsonExtensionsKt.readInt(doc, "$.durChapterPos");
            val var10022: Int = if (var104 == null) 0 else var104;
            val var105: java.lang.Long = JsonExtensionsKt.readLong(doc, "$.durChapterTime");
            val var10023: Long = if (var105 == null) System.currentTimeMillis() else var105;
            val var10024: java.lang.String = JsonExtensionsKt.readString(doc, "$.wordCount");
            val var106: java.lang.Boolean = JsonExtensionsKt.readBool(doc, "$.canUpdate");
            val var10025: Boolean = var106 == null || var106;
            val var107: Int = JsonExtensionsKt.readInt(doc, "$.order");
            val var10026: Int = if (var107 == null) 0 else var107;
            val var108: Int = JsonExtensionsKt.readInt(doc, "$.originOrder");
            val var10027: Int = if (var108 == null) 0 else var108;
            val var109: java.lang.Boolean = JsonExtensionsKt.readBool(doc, "$.useReplaceRule");
            val var10028: Boolean = var109 == null || var109;
            val var10029: java.lang.String = JsonExtensionsKt.readString(doc, "$.variable");
            val var10030: Book.ReadConfig;
            if (var92 != null) {
               var var43: Any;
               try {
                  var43 = Result.Companion;
                  var var116: java.lang.Boolean = JsonExtensionsKt.readBool(doc, "$.readConfig.reverseToc");
                  val var122: Boolean = var116 != null && var116;
                  val var117: Int = JsonExtensionsKt.readInt(doc, "$.readConfig.pageAnim");
                  val var123: Int = if (var117 == null) -1 else var117;
                  var116 = JsonExtensionsKt.readBool(doc, "$.readConfig.reSegment");
                  val var124: Boolean = var116 != null && var116;
                  var10005 = JsonExtensionsKt.readString(doc, "$.readConfig.imageStyle");
                  var116 = JsonExtensionsKt.readBool(doc, "$.readConfig.useReplaceRule");
                  val var126: Boolean = var116 != null && var116;
                  val var120: java.lang.Long = JsonExtensionsKt.readLong(doc, "$.readConfig.delTag");
                  var43 = Result.constructor-impl(
                     new Book.ReadConfig(var122, var123, var124, var10005, var126, if (var120 == null) 0L else var120, 0.0F, 64, null)
                  );
               } catch (var88: java.lang.Throwable) {
                  val var45: Result.Companion = Result.Companion;
                  var43 = Result.constructor-impl(ResultKt.createFailure(var88));
               }

               var10030 = (if (Result.isFailure-impl(var43)) null else var43) as Book.ReadConfig;
            } else {
               var10030 = null;
            }

            val var112: java.lang.Boolean = JsonExtensionsKt.readBool(doc, "$.isInShelf");
            var3 = Result.constructor-impl(
               new Book(
                  var10000,
                  var10001,
                  var10002,
                  var10003,
                  var10004,
                  var10005,
                  var10006,
                  var10007,
                  var10008,
                  var10009,
                  var10010,
                  var10011,
                  var10012,
                  var10013,
                  var10014,
                  var10015,
                  var10016,
                  var10017,
                  var10018,
                  var10019,
                  var10020,
                  var10021,
                  var10022,
                  var10023,
                  var10024,
                  var10025,
                  var10026,
                  var10027,
                  var10028,
                  var10029,
                  var10030,
                  var112 != null && var112,
                  null,
                  0,
                  1,
                  null
               )
            );
         } catch (var89: java.lang.Throwable) {
            val readConfig: Result.Companion = Result.Companion;
            var3 = Result.constructor-impl(ResultKt.createFailure(var89));
         }

         return var3;
      }

      public fun fromJson(json: String): Result<Book> {
         val var2: DocumentContext = JsonExtensionsKt.getJsonPath().parse(json);
         return this.fromJsonDoc-IoAF18A(var2);
      }

      public fun fromJsonArray(jsonArray: String): Result<ArrayList<Book>> {
         var var3: Any;
         try {
            var3 = Result.Companion;
            val var24: ArrayList = new ArrayList();

            val `$this$forEach$iv`: java.lang.Iterable;
            for (Object element$iv : $this$forEach$iv) {
               val jsonItem: DocumentContext = JsonExtensionsKt.getJsonPath().parse(`element$iv`);
               val var10000: Book.Companion = Book.Companion;
               val var14: Any = var10000.fromJsonDoc-IoAF18A(jsonItem);
               ResultKt.throwOnFailure(var14);
               var24.add(var14 as Book);
            }

            var3 = Result.constructor-impl(var24);
         } catch (var19: java.lang.Throwable) {
            val doc: Result.Companion = Result.Companion;
            var3 = Result.constructor-impl(ResultKt.createFailure(var19));
         }

         return var3;
      }
   }

   public class Converters {
      public fun readConfigToString(config: io.legado.app.data.entities.Book.ReadConfig?): String {
         val var2: java.lang.String = GsonExtensionsKt.getGSON().toJson(config);
         return var2;
      }

      public fun stringToReadConfig(json: String?): Result<io.legado.app.data.entities.Book.ReadConfig?> {
         val `$this$fromJsonObject$iv`: Gson = GsonExtensionsKt.getGSON();

         var var5: Any;
         try {
            var5 = Result.Companion;
            val var14: Type = new Book$Converters$stringToReadConfig-IoAF18A$$inlined$fromJsonObject$1().getType();
            var var10000: Any = `$this$fromJsonObject$iv`.fromJson(json, var14);
            if (var10000 !is Book.ReadConfig) {
               var10000 = null;
            }

            var5 = Result.constructor-impl(var10000 as Book.ReadConfig);
         } catch (var9: java.lang.Throwable) {
            val `$i$f$genericType`: Result.Companion = Result.Companion;
            var5 = Result.constructor-impl(ResultKt.createFailure(var9));
         }

         return var5;
      }
   }

   public data class ReadConfig(reverseToc: Boolean = false,
      pageAnim: Int = -1,
      reSegment: Boolean = false,
      imageStyle: String? = null,
      useReplaceRule: Boolean = false,
      delTag: Long = 0L,
      pdfImageWidth: Float = 800.0F
   ) {
      public final var delTag: Long
         internal set

      public final var imageStyle: String?
         internal set

      public final var pageAnim: Int
         internal set

      public final var pdfImageWidth: Float
         internal set

      public final var reSegment: Boolean
         internal set

      public final var reverseToc: Boolean
         internal set

      public final var useReplaceRule: Boolean
         internal set

      init {
         this.reverseToc = reverseToc;
         this.pageAnim = pageAnim;
         this.reSegment = reSegment;
         this.imageStyle = imageStyle;
         this.useReplaceRule = useReplaceRule;
         this.delTag = delTag;
         this.pdfImageWidth = pdfImageWidth;
      }

      public operator fun component1(): Boolean {
         return this.reverseToc;
      }

      public operator fun component2(): Int {
         return this.pageAnim;
      }

      public operator fun component3(): Boolean {
         return this.reSegment;
      }

      public operator fun component4(): String? {
         return this.imageStyle;
      }

      public operator fun component5(): Boolean {
         return this.useReplaceRule;
      }

      public operator fun component6(): Long {
         return this.delTag;
      }

      public operator fun component7(): Float {
         return this.pdfImageWidth;
      }

      public fun copy(
         reverseToc: Boolean = this.reverseToc,
         pageAnim: Int = this.pageAnim,
         reSegment: Boolean = this.reSegment,
         imageStyle: String? = this.imageStyle,
         useReplaceRule: Boolean = this.useReplaceRule,
         delTag: Long = this.delTag,
         pdfImageWidth: Float = this.pdfImageWidth
      ): io.legado.app.data.entities.Book.ReadConfig {
         return new Book.ReadConfig(reverseToc, pageAnim, reSegment, imageStyle, useReplaceRule, delTag, pdfImageWidth);
      }

      public override fun toString(): String {
         return "ReadConfig(reverseToc=${this.reverseToc}, pageAnim=${this.pageAnim}, reSegment=${this.reSegment}, imageStyle=${this.imageStyle}, useReplaceRule=${this.useReplaceRule}, delTag=${this.delTag}, pdfImageWidth=${this.pdfImageWidth})";
      }

      public override fun hashCode(): Int {
         var var10000: Int = this.reverseToc;
         if (this.reverseToc) {
            var10000 = 1;
         }

         var10000 = (var10000 * 31 + Integer.hashCode(this.pageAnim)) * 31;
         var var10001: Byte = this.reSegment;
         if (this.reSegment) {
            var10001 = 1;
         }

         var10000 = ((var10000 + var10001) * 31 + (if (this.imageStyle == null) 0 else this.imageStyle.hashCode())) * 31;
         var10001 = this.useReplaceRule;
         if (this.useReplaceRule) {
            var10001 = 1;
         }

         return ((var10000 + var10001) * 31 + java.lang.Long.hashCode(this.delTag)) * 31 + java.lang.Float.hashCode(this.pdfImageWidth);
      }

      public override operator fun equals(other: Any?): Boolean {
         if (this === other) {
            return true;
         } else if (other !is Book.ReadConfig) {
            return false;
         } else {
            val var2: Book.ReadConfig = other as Book.ReadConfig;
            if (this.reverseToc != (other as Book.ReadConfig).reverseToc) {
               return false;
            } else if (this.pageAnim != var2.pageAnim) {
               return false;
            } else if (this.reSegment != var2.reSegment) {
               return false;
            } else if (!(this.imageStyle == var2.imageStyle)) {
               return false;
            } else if (this.useReplaceRule != var2.useReplaceRule) {
               return false;
            } else if (this.delTag != var2.delTag) {
               return false;
            } else {
               return this.pdfImageWidth == var2.pdfImageWidth;
            }
         }
      }

      fun ReadConfig() {
         this(false, 0, false, null, false, 0L, 0.0F, 127, null);
      }
   }
}
