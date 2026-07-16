package io.legado.app.data.entities

import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import com.jayway.jsonpath.DocumentContext
import com.script.SimpleBindings
import io.legado.app.help.http.StrResponse
import io.legado.app.model.DebugLog
import io.legado.app.model.analyzeRule.QueryTTF
import io.legado.app.utils.JsonExtensionsKt
import java.io.File
import java.util.ArrayList
import org.jsoup.Connection.Response

@JsonIgnoreProperties(["headerMap", "source", "_userNameSpace", "userNameSpace", "loginHeader", "loginHeaderMap", "loginInfo", "loginInfoMap"])
public data class RssSource(sourceUrl: String = "",
      sourceName: String = "",
      sourceIcon: String = "",
      sourceGroup: String? = null,
      sourceComment: String? = null,
      enabled: Boolean = true,
      variableComment: String? = null,
      enabledCookieJar: Boolean? = false,
      concurrentRate: String? = null,
      header: String? = null,
      loginUrl: String? = null,
      loginUi: String? = null,
      loginCheckJs: String? = null,
      sortUrl: String? = null,
      singleUrl: Boolean = false,
      articleStyle: Int = 0,
      ruleArticles: String? = null,
      ruleNextPage: String? = null,
      ruleTitle: String? = null,
      rulePubDate: String? = null,
      ruleDescription: String? = null,
      ruleImage: String? = null,
      ruleLink: String? = null,
      ruleContent: String? = null,
      style: String? = null,
      enableJs: Boolean = true,
      loadWithBaseUrl: Boolean = true,
      customOrder: Int = 0
   ) :
   BaseSource {
   private final var _userNameSpace: String

   public final var articleStyle: Int
      internal set

   public open var concurrentRate: String?
      internal final set

   public final var customOrder: Int
      internal set

   private final var debugLog: DebugLog?

   public final var enableJs: Boolean
      internal set

   public final var enabled: Boolean
      internal set

   public open var enabledCookieJar: Boolean?
      internal final set

   public open var header: String?
      internal final set

   public final var loadWithBaseUrl: Boolean
      internal set

   public final var loginCheckJs: String?
      internal set

   public open var loginUi: String?
      internal final set

   public open var loginUrl: String?
      internal final set

   public final var ruleArticles: String?
      internal set

   public final var ruleContent: String?
      internal set

   public final var ruleDescription: String?
      internal set

   public final var ruleImage: String?
      internal set

   public final var ruleLink: String?
      internal set

   public final var ruleNextPage: String?
      internal set

   public final var rulePubDate: String?
      internal set

   public final var ruleTitle: String?
      internal set

   public final var singleUrl: Boolean
      internal set

   public final var sortUrl: String?
      internal set

   public final var sourceComment: String?
      internal set

   public final var sourceGroup: String?
      internal set

   public final var sourceIcon: String
      internal set

   public final var sourceName: String
      internal set

   public final var sourceUrl: String
      internal set

   public final var style: String?
      internal set

   public final var variableComment: String?
      internal set

   init {
      this.sourceUrl = sourceUrl;
      this.sourceName = sourceName;
      this.sourceIcon = sourceIcon;
      this.sourceGroup = sourceGroup;
      this.sourceComment = sourceComment;
      this.enabled = enabled;
      this.variableComment = variableComment;
      this.enabledCookieJar = enabledCookieJar;
      this.concurrentRate = concurrentRate;
      this.header = header;
      this.loginUrl = loginUrl;
      this.loginUi = loginUi;
      this.loginCheckJs = loginCheckJs;
      this.sortUrl = sortUrl;
      this.singleUrl = singleUrl;
      this.articleStyle = articleStyle;
      this.ruleArticles = ruleArticles;
      this.ruleNextPage = ruleNextPage;
      this.ruleTitle = ruleTitle;
      this.rulePubDate = rulePubDate;
      this.ruleDescription = ruleDescription;
      this.ruleImage = ruleImage;
      this.ruleLink = ruleLink;
      this.ruleContent = ruleContent;
      this.style = style;
      this.enableJs = enableJs;
      this.loadWithBaseUrl = loadWithBaseUrl;
      this.customOrder = customOrder;
      this._userNameSpace = "";
   }

   public override fun getTag(): String {
      return this.sourceName;
   }

   public override fun getKey(): String {
      return this.sourceUrl;
   }

   public override operator fun equals(other: Any?): Boolean {
      return other is RssSource && (other as RssSource).sourceUrl == this.sourceUrl;
   }

   public override fun hashCode(): Int {
      return this.sourceUrl.hashCode();
   }

   public fun equal(source: RssSource): Boolean {
      return this.equal(this.sourceUrl, source.sourceUrl)
         && this.equal(this.sourceIcon, source.sourceIcon)
         && this.enabled == source.enabled
         && this.getEnabledCookieJar() == source.getEnabledCookieJar()
         && this.equal(this.sourceComment, source.sourceComment)
         && this.equal(this.sourceGroup, source.sourceGroup)
         && this.equal(this.ruleArticles, source.ruleArticles)
         && this.equal(this.ruleNextPage, source.ruleNextPage)
         && this.equal(this.ruleTitle, source.ruleTitle)
         && this.equal(this.rulePubDate, source.rulePubDate)
         && this.equal(this.ruleDescription, source.ruleDescription)
         && this.equal(this.ruleLink, source.ruleLink)
         && this.equal(this.ruleContent, source.ruleContent)
         && this.enableJs == source.enableJs
         && this.loadWithBaseUrl == source.loadWithBaseUrl;
   }

   private fun equal(a: String?, b: String?): Boolean {
      label21:
      if (!(a == b)) {
         return (a == null || a.length() == 0) && (b == null || b.length() == 0);
      } else {
         return true;
      }
   }

   public fun sortUrls(): List<Pair<String, String>> {
      val var21: ArrayList = new ArrayList();
      val `$this$sortUrls_u24lambda_u2d2`: ArrayList = var21;

      try {
         var var25: java.lang.String;
         label81: {
            val var22: Result.Companion = Result.Companion;
            var25 = this.getSortUrl();
            var var27: java.lang.String = this.getSortUrl();
            if (var27 == null || !StringsKt.startsWith(var27, "<js>", false)) {
               var27 = this.getSortUrl();
               if (var27 == null || !StringsKt.startsWith(var27, "@js:", false)) {
                  break label81;
               }
            }

            var var10000: java.lang.String = this.getSortUrl();
            if (StringsKt.startsWith$default(var10000, "@", false, 2, null)) {
               var10000 = this.getSortUrl();
               if (var10000 == null) {
                  throw new NullPointerException("null cannot be cast to non-null type java.lang.String");
               }

               var10000 = var10000.substring(4);
            } else {
               var10000 = this.getSortUrl();
               var10000 = this.getSortUrl();
               val var35: Int = StringsKt.lastIndexOf$default(var10000, "<", 0, false, 6, null);
               if (var10000 == null) {
                  throw new NullPointerException("null cannot be cast to non-null type java.lang.String");
               }

               var10000 = var10000.substring(4, var35);
            }

            var25 = java.lang.String.valueOf(BaseSource.DefaultImpls.evalJS$default(this, var10000, null, 2, null));
         }

         if (var25 != null && new Regex("(&&|\n)+").split(var25, 0) != null) {
            val var34: java.lang.Iterable;
            for (Object element$iv : var34) {
               val d: java.util.List = StringsKt.split$default(var42 as java.lang.String, new java.lang.String[]{"::"}, false, 0, 6, null);
               if (d.size() > 1) {
                  `$this$sortUrls_u24lambda_u2d2`.add(new Pair<>(d.get(0), d.get(1)));
               }
            }
         }

         if (`$this$sortUrls_u24lambda_u2d2`.isEmpty()) {
            `$this$sortUrls_u24lambda_u2d2`.add(new Pair<>("", this.getSourceUrl()));
         }

         val var23: Any = Result.constructor-impl(Unit.INSTANCE);
      } catch (var20: java.lang.Throwable) {
         val a: Result.Companion = Result.Companion;
         val var7: Any = Result.constructor-impl(ResultKt.createFailure(var20));
      }

      return var21;
   }

   public fun setUserNameSpace(nameSpace: String) {
      this._userNameSpace = nameSpace;
   }

   public override fun getUserNameSpace(): String {
      return this._userNameSpace;
   }

   public fun setLogger(logger: DebugLog?) {
      this.debugLog = logger;
   }

   public override fun getLogger(): DebugLog? {
      return null;
   }

   @Throws(java/lang/Exception::class)
   override fun evalJS(jsStr: java.lang.String, bindingsConfig: (SimpleBindings?) -> Unit): Any? {
      return BaseSource.DefaultImpls.evalJS(this, jsStr, bindingsConfig);
   }

   override fun aesBase64DecodeToByteArray(str: java.lang.String, key: java.lang.String, transformation: java.lang.String, iv: java.lang.String): ByteArray? {
      return BaseSource.DefaultImpls.aesBase64DecodeToByteArray(this, str, key, transformation, iv);
   }

   override fun aesBase64DecodeToString(str: java.lang.String, key: java.lang.String, transformation: java.lang.String, iv: java.lang.String): java.lang.String? {
      return BaseSource.DefaultImpls.aesBase64DecodeToString(this, str, key, transformation, iv);
   }

   override fun aesDecodeArgsBase64Str(data: java.lang.String, key: java.lang.String, mode: java.lang.String, padding: java.lang.String, iv: java.lang.String): java.lang.String? {
      return BaseSource.DefaultImpls.aesDecodeArgsBase64Str(this, data, key, mode, padding, iv);
   }

   override fun aesDecodeToByteArray(str: java.lang.String, key: java.lang.String, transformation: java.lang.String, iv: java.lang.String): ByteArray? {
      return BaseSource.DefaultImpls.aesDecodeToByteArray(this, str, key, transformation, iv);
   }

   override fun aesDecodeToString(str: java.lang.String, key: java.lang.String, transformation: java.lang.String, iv: java.lang.String): java.lang.String? {
      return BaseSource.DefaultImpls.aesDecodeToString(this, str, key, transformation, iv);
   }

   override fun aesEncodeArgsBase64Str(data: java.lang.String, key: java.lang.String, mode: java.lang.String, padding: java.lang.String, iv: java.lang.String): java.lang.String? {
      return BaseSource.DefaultImpls.aesEncodeArgsBase64Str(this, data, key, mode, padding, iv);
   }

   override fun aesEncodeToBase64ByteArray(data: java.lang.String, key: java.lang.String, transformation: java.lang.String, iv: java.lang.String): ByteArray? {
      return BaseSource.DefaultImpls.aesEncodeToBase64ByteArray(this, data, key, transformation, iv);
   }

   override fun aesEncodeToBase64String(data: java.lang.String, key: java.lang.String, transformation: java.lang.String, iv: java.lang.String): java.lang.String? {
      return BaseSource.DefaultImpls.aesEncodeToBase64String(this, data, key, transformation, iv);
   }

   override fun aesEncodeToByteArray(data: java.lang.String, key: java.lang.String, transformation: java.lang.String, iv: java.lang.String): ByteArray? {
      return BaseSource.DefaultImpls.aesEncodeToByteArray(this, data, key, transformation, iv);
   }

   override fun aesEncodeToString(data: java.lang.String, key: java.lang.String, transformation: java.lang.String, iv: java.lang.String): java.lang.String? {
      return BaseSource.DefaultImpls.aesEncodeToString(this, data, key, transformation, iv);
   }

   override fun ajax(urlStr: java.lang.String): java.lang.String? {
      return BaseSource.DefaultImpls.ajax(this, urlStr);
   }

   override fun ajaxAll(urlList: Array<java.lang.String>): Array<StrResponse> {
      return BaseSource.DefaultImpls.ajaxAll(this, urlList);
   }

   override fun androidId(): java.lang.String {
      return BaseSource.DefaultImpls.androidId(this);
   }

   override fun base64Decode(str: java.lang.String): java.lang.String {
      return BaseSource.DefaultImpls.base64Decode(this, str);
   }

   override fun base64Decode(str: java.lang.String, flags: Int): java.lang.String {
      return BaseSource.DefaultImpls.base64Decode(this, str, flags);
   }

   override fun base64DecodeToByteArray(str: java.lang.String?): ByteArray? {
      return BaseSource.DefaultImpls.base64DecodeToByteArray(this, str);
   }

   override fun base64DecodeToByteArray(str: java.lang.String?, flags: Int): ByteArray? {
      return BaseSource.DefaultImpls.base64DecodeToByteArray(this, str, flags);
   }

   override fun base64Encode(str: java.lang.String): java.lang.String? {
      return BaseSource.DefaultImpls.base64Encode(this, str);
   }

   override fun base64Encode(str: java.lang.String, flags: Int): java.lang.String? {
      return BaseSource.DefaultImpls.base64Encode(this, str, flags);
   }

   override fun cacheFile(urlStr: java.lang.String): java.lang.String? {
      return BaseSource.DefaultImpls.cacheFile(this, urlStr);
   }

   override fun cacheFile(urlStr: java.lang.String, saveTime: Int): java.lang.String? {
      return BaseSource.DefaultImpls.cacheFile(this, urlStr, saveTime);
   }

   override fun connect(urlStr: java.lang.String): StrResponse {
      return BaseSource.DefaultImpls.connect(this, urlStr);
   }

   override fun connect(urlStr: java.lang.String, header: java.lang.String?): StrResponse {
      return BaseSource.DefaultImpls.connect(this, urlStr, header);
   }

   override fun deleteFile(path: java.lang.String) {
      BaseSource.DefaultImpls.deleteFile(this, path);
   }

   override fun desBase64DecodeToString(data: java.lang.String, key: java.lang.String, transformation: java.lang.String, iv: java.lang.String): java.lang.String? {
      return BaseSource.DefaultImpls.desBase64DecodeToString(this, data, key, transformation, iv);
   }

   override fun desDecodeToString(data: java.lang.String, key: java.lang.String, transformation: java.lang.String, iv: java.lang.String): java.lang.String? {
      return BaseSource.DefaultImpls.desDecodeToString(this, data, key, transformation, iv);
   }

   override fun desEncodeToBase64String(data: java.lang.String, key: java.lang.String, transformation: java.lang.String, iv: java.lang.String): java.lang.String? {
      return BaseSource.DefaultImpls.desEncodeToBase64String(this, data, key, transformation, iv);
   }

   override fun desEncodeToString(data: java.lang.String, key: java.lang.String, transformation: java.lang.String, iv: java.lang.String): java.lang.String? {
      return BaseSource.DefaultImpls.desEncodeToString(this, data, key, transformation, iv);
   }

   override fun digestBase64Str(data: java.lang.String, algorithm: java.lang.String): java.lang.String? {
      return BaseSource.DefaultImpls.digestBase64Str(this, data, algorithm);
   }

   override fun digestHex(data: java.lang.String, algorithm: java.lang.String): java.lang.String? {
      return BaseSource.DefaultImpls.digestHex(this, data, algorithm);
   }

   override fun downloadFile(content: java.lang.String, url: java.lang.String): java.lang.String {
      return BaseSource.DefaultImpls.downloadFile(this, content, url);
   }

   override fun encodeURI(str: java.lang.String): java.lang.String {
      return BaseSource.DefaultImpls.encodeURI(this, str);
   }

   override fun encodeURI(str: java.lang.String, enc: java.lang.String): java.lang.String {
      return BaseSource.DefaultImpls.encodeURI(this, str, enc);
   }

   override fun get(urlStr: java.lang.String, headers: MutableMap<java.lang.String, java.lang.String>): Response {
      return BaseSource.DefaultImpls.get(this, urlStr, headers);
   }

   override fun getCookie(tag: java.lang.String, key: java.lang.String?): java.lang.String {
      return BaseSource.DefaultImpls.getCookie(this, tag, key);
   }

   override fun getFile(path: java.lang.String): File {
      return BaseSource.DefaultImpls.getFile(this, path);
   }

   override fun getHeaderMap(hasLoginHeader: Boolean): HashMap<java.lang.String, java.lang.String> {
      return BaseSource.DefaultImpls.getHeaderMap(this, hasLoginHeader);
   }

   override fun getLoginHeader(): java.lang.String? {
      return BaseSource.DefaultImpls.getLoginHeader(this);
   }

   override fun getLoginHeaderMap(): MutableMap<java.lang.String, java.lang.String>? {
      return BaseSource.DefaultImpls.getLoginHeaderMap(this);
   }

   override fun getLoginInfo(): java.lang.String? {
      return BaseSource.DefaultImpls.getLoginInfo(this);
   }

   override fun getLoginInfoMap(): MutableMap<java.lang.String, java.lang.String>? {
      return BaseSource.DefaultImpls.getLoginInfoMap(this);
   }

   override fun getLoginJs(): java.lang.String? {
      return BaseSource.DefaultImpls.getLoginJs(this);
   }

   override fun getSource(): BaseSource? {
      return BaseSource.DefaultImpls.getSource(this);
   }

   override fun getTxtInFolder(unzipPath: java.lang.String): java.lang.String {
      return BaseSource.DefaultImpls.getTxtInFolder(this, unzipPath);
   }

   override fun getVariable(): java.lang.String? {
      return BaseSource.DefaultImpls.getVariable(this);
   }

   override fun getZipByteArrayContent(url: java.lang.String, path: java.lang.String): ByteArray? {
      return BaseSource.DefaultImpls.getZipByteArrayContent(this, url, path);
   }

   override fun getZipStringContent(url: java.lang.String, path: java.lang.String): java.lang.String {
      return BaseSource.DefaultImpls.getZipStringContent(this, url, path);
   }

   override fun getZipStringContent(url: java.lang.String, path: java.lang.String, charsetName: java.lang.String): java.lang.String {
      return BaseSource.DefaultImpls.getZipStringContent(this, url, path, charsetName);
   }

   override fun head(urlStr: java.lang.String, headers: MutableMap<java.lang.String, java.lang.String>): Response {
      return BaseSource.DefaultImpls.head(this, urlStr, headers);
   }

   override fun htmlFormat(str: java.lang.String): java.lang.String {
      return BaseSource.DefaultImpls.htmlFormat(this, str);
   }

   override fun importScript(path: java.lang.String): java.lang.String {
      return BaseSource.DefaultImpls.importScript(this, path);
   }

   override fun log(msg: java.lang.String): java.lang.String {
      return BaseSource.DefaultImpls.log(this, msg);
   }

   override fun logType(any: Any?) {
      BaseSource.DefaultImpls.logType(this, any);
   }

   override fun login() {
      BaseSource.DefaultImpls.login(this);
   }

   override fun longToast(msg: Any?) {
      BaseSource.DefaultImpls.longToast(this, msg);
   }

   override fun md5Encode(str: java.lang.String): java.lang.String {
      return BaseSource.DefaultImpls.md5Encode(this, str);
   }

   override fun md5Encode16(str: java.lang.String): java.lang.String {
      return BaseSource.DefaultImpls.md5Encode16(this, str);
   }

   override fun post(urlStr: java.lang.String, body: java.lang.String, headers: MutableMap<java.lang.String, java.lang.String>): Response {
      return BaseSource.DefaultImpls.post(this, urlStr, body, headers);
   }

   override fun putLoginHeader(header: java.lang.String) {
      BaseSource.DefaultImpls.putLoginHeader(this, header);
   }

   override fun putLoginInfo(info: java.lang.String): Boolean {
      return BaseSource.DefaultImpls.putLoginInfo(this, info);
   }

   override fun queryBase64TTF(base64: java.lang.String?): QueryTTF? {
      return BaseSource.DefaultImpls.queryBase64TTF(this, base64);
   }

   override fun queryTTF(str: java.lang.String?): QueryTTF? {
      return BaseSource.DefaultImpls.queryTTF(this, str);
   }

   override fun randomUUID(): java.lang.String {
      return BaseSource.DefaultImpls.randomUUID(this);
   }

   override fun readFile(path: java.lang.String): ByteArray? {
      return BaseSource.DefaultImpls.readFile(this, path);
   }

   override fun readTxtFile(path: java.lang.String): java.lang.String {
      return BaseSource.DefaultImpls.readTxtFile(this, path);
   }

   override fun readTxtFile(path: java.lang.String, charsetName: java.lang.String): java.lang.String {
      return BaseSource.DefaultImpls.readTxtFile(this, path, charsetName);
   }

   override fun removeLoginHeader() {
      BaseSource.DefaultImpls.removeLoginHeader(this);
   }

   override fun removeLoginInfo() {
      BaseSource.DefaultImpls.removeLoginInfo(this);
   }

   override fun replaceFont(text: java.lang.String, font1: QueryTTF?, font2: QueryTTF?): java.lang.String {
      return BaseSource.DefaultImpls.replaceFont(this, text, font1, font2);
   }

   override fun setVariable(variable: java.lang.String?) {
      BaseSource.DefaultImpls.setVariable(this, variable);
   }

   override fun timeFormat(time: Long): java.lang.String {
      return BaseSource.DefaultImpls.timeFormat(this, time);
   }

   override fun timeFormatUTC(time: Long, format: java.lang.String, sh: Int): java.lang.String? {
      return BaseSource.DefaultImpls.timeFormatUTC(this, time, format, sh);
   }

   override fun toast(msg: Any?) {
      BaseSource.DefaultImpls.toast(this, msg);
   }

   override fun tripleDESDecodeArgsBase64Str(
      data: java.lang.String, key: java.lang.String, mode: java.lang.String, padding: java.lang.String, iv: java.lang.String
   ): java.lang.String? {
      return BaseSource.DefaultImpls.tripleDESDecodeArgsBase64Str(this, data, key, mode, padding, iv);
   }

   override fun tripleDESDecodeStr(data: java.lang.String, key: java.lang.String, mode: java.lang.String, padding: java.lang.String, iv: java.lang.String): java.lang.String? {
      return BaseSource.DefaultImpls.tripleDESDecodeStr(this, data, key, mode, padding, iv);
   }

   override fun tripleDESEncodeArgsBase64Str(
      data: java.lang.String, key: java.lang.String, mode: java.lang.String, padding: java.lang.String, iv: java.lang.String
   ): java.lang.String? {
      return BaseSource.DefaultImpls.tripleDESEncodeArgsBase64Str(this, data, key, mode, padding, iv);
   }

   override fun tripleDESEncodeBase64Str(data: java.lang.String, key: java.lang.String, mode: java.lang.String, padding: java.lang.String, iv: java.lang.String): java.lang.String? {
      return BaseSource.DefaultImpls.tripleDESEncodeBase64Str(this, data, key, mode, padding, iv);
   }

   override fun unzipFile(zipPath: java.lang.String): java.lang.String {
      return BaseSource.DefaultImpls.unzipFile(this, zipPath);
   }

   override fun utf8ToGbk(str: java.lang.String): java.lang.String {
      return BaseSource.DefaultImpls.utf8ToGbk(this, str);
   }

   override fun webView(html: java.lang.String?, url: java.lang.String?, js: java.lang.String?): java.lang.String? {
      return BaseSource.DefaultImpls.webView(this, html, url, js);
   }

   public operator fun component1(): String {
      return this.sourceUrl;
   }

   public operator fun component2(): String {
      return this.sourceName;
   }

   public operator fun component3(): String {
      return this.sourceIcon;
   }

   public operator fun component4(): String? {
      return this.sourceGroup;
   }

   public operator fun component5(): String? {
      return this.sourceComment;
   }

   public operator fun component6(): Boolean {
      return this.enabled;
   }

   public operator fun component7(): String? {
      return this.variableComment;
   }

   public operator fun component8(): Boolean? {
      return this.getEnabledCookieJar();
   }

   public operator fun component9(): String? {
      return this.getConcurrentRate();
   }

   public operator fun component10(): String? {
      return this.getHeader();
   }

   public operator fun component11(): String? {
      return this.getLoginUrl();
   }

   public operator fun component12(): String? {
      return this.getLoginUi();
   }

   public operator fun component13(): String? {
      return this.loginCheckJs;
   }

   public operator fun component14(): String? {
      return this.sortUrl;
   }

   public operator fun component15(): Boolean {
      return this.singleUrl;
   }

   public operator fun component16(): Int {
      return this.articleStyle;
   }

   public operator fun component17(): String? {
      return this.ruleArticles;
   }

   public operator fun component18(): String? {
      return this.ruleNextPage;
   }

   public operator fun component19(): String? {
      return this.ruleTitle;
   }

   public operator fun component20(): String? {
      return this.rulePubDate;
   }

   public operator fun component21(): String? {
      return this.ruleDescription;
   }

   public operator fun component22(): String? {
      return this.ruleImage;
   }

   public operator fun component23(): String? {
      return this.ruleLink;
   }

   public operator fun component24(): String? {
      return this.ruleContent;
   }

   public operator fun component25(): String? {
      return this.style;
   }

   public operator fun component26(): Boolean {
      return this.enableJs;
   }

   public operator fun component27(): Boolean {
      return this.loadWithBaseUrl;
   }

   public operator fun component28(): Int {
      return this.customOrder;
   }

   public fun copy(
      sourceUrl: String = this.sourceUrl,
      sourceName: String = this.sourceName,
      sourceIcon: String = this.sourceIcon,
      sourceGroup: String? = this.sourceGroup,
      sourceComment: String? = this.sourceComment,
      enabled: Boolean = this.enabled,
      variableComment: String? = this.variableComment,
      enabledCookieJar: Boolean? = this.getEnabledCookieJar(),
      concurrentRate: String? = this.getConcurrentRate(),
      header: String? = this.getHeader(),
      loginUrl: String? = this.getLoginUrl(),
      loginUi: String? = this.getLoginUi(),
      loginCheckJs: String? = this.loginCheckJs,
      sortUrl: String? = this.sortUrl,
      singleUrl: Boolean = this.singleUrl,
      articleStyle: Int = this.articleStyle,
      ruleArticles: String? = this.ruleArticles,
      ruleNextPage: String? = this.ruleNextPage,
      ruleTitle: String? = this.ruleTitle,
      rulePubDate: String? = this.rulePubDate,
      ruleDescription: String? = this.ruleDescription,
      ruleImage: String? = this.ruleImage,
      ruleLink: String? = this.ruleLink,
      ruleContent: String? = this.ruleContent,
      style: String? = this.style,
      enableJs: Boolean = this.enableJs,
      loadWithBaseUrl: Boolean = this.loadWithBaseUrl,
      customOrder: Int = this.customOrder
   ): RssSource {
      return new RssSource(
         sourceUrl,
         sourceName,
         sourceIcon,
         sourceGroup,
         sourceComment,
         enabled,
         variableComment,
         enabledCookieJar,
         concurrentRate,
         header,
         loginUrl,
         loginUi,
         loginCheckJs,
         sortUrl,
         singleUrl,
         articleStyle,
         ruleArticles,
         ruleNextPage,
         ruleTitle,
         rulePubDate,
         ruleDescription,
         ruleImage,
         ruleLink,
         ruleContent,
         style,
         enableJs,
         loadWithBaseUrl,
         customOrder
      );
   }

   public override fun toString(): String {
      val var1: StringBuilder = new StringBuilder();
      var1.append("RssSource(sourceUrl=")
         .append(this.sourceUrl)
         .append(", sourceName=")
         .append(this.sourceName)
         .append(", sourceIcon=")
         .append(this.sourceIcon)
         .append(", sourceGroup=")
         .append(this.sourceGroup)
         .append(", sourceComment=")
         .append(this.sourceComment)
         .append(", enabled=")
         .append(this.enabled)
         .append(", variableComment=")
         .append(this.variableComment)
         .append(", enabledCookieJar=")
         .append(this.getEnabledCookieJar())
         .append(", concurrentRate=")
         .append(this.getConcurrentRate())
         .append(", header=")
         .append(this.getHeader())
         .append(", loginUrl=")
         .append(this.getLoginUrl())
         .append(", loginUi=");
      var1.append(this.getLoginUi())
         .append(", loginCheckJs=")
         .append(this.loginCheckJs)
         .append(", sortUrl=")
         .append(this.sortUrl)
         .append(", singleUrl=")
         .append(this.singleUrl)
         .append(", articleStyle=")
         .append(this.articleStyle)
         .append(", ruleArticles=")
         .append(this.ruleArticles)
         .append(", ruleNextPage=")
         .append(this.ruleNextPage)
         .append(", ruleTitle=")
         .append(this.ruleTitle)
         .append(", rulePubDate=")
         .append(this.rulePubDate)
         .append(", ruleDescription=")
         .append(this.ruleDescription)
         .append(", ruleImage=")
         .append(this.ruleImage)
         .append(", ruleLink=")
         .append(this.ruleLink);
      var1.append(", ruleContent=")
         .append(this.ruleContent)
         .append(", style=")
         .append(this.style)
         .append(", enableJs=")
         .append(this.enableJs)
         .append(", loadWithBaseUrl=")
         .append(this.loadWithBaseUrl)
         .append(", customOrder=")
         .append(this.customOrder)
         .append(')');
      return var1.toString();
   }

   fun RssSource() {
      this(
         null,
         null,
         null,
         null,
         null,
         false,
         null,
         null,
         null,
         null,
         null,
         null,
         null,
         null,
         false,
         0,
         null,
         null,
         null,
         null,
         null,
         null,
         null,
         null,
         null,
         false,
         false,
         0,
         268435455,
         null
      );
   }

   public companion object {
      public fun fromJsonDoc(doc: DocumentContext): Result<RssSource> {
         var var3: Any;
         try {
            var3 = Result.Companion;
            val var10000: java.lang.String = JsonExtensionsKt.readString(doc, "$.sourceUrl");
            val var45: java.lang.String = JsonExtensionsKt.readString(doc, "$.sourceName");
            var var7: java.lang.String = JsonExtensionsKt.readString(doc, "$.sourceIcon");
            val var8: java.lang.String = if (var7 == null) "" else var7;
            var7 = JsonExtensionsKt.readString(doc, "$.sourceGroup");
            val var9: java.lang.String = JsonExtensionsKt.readString(doc, "$.sourceComment");
            val var10: java.lang.Boolean = JsonExtensionsKt.readBool(doc, "$.enabled");
            val var11: Boolean = var10 == null || var10;
            val var39: java.lang.String = JsonExtensionsKt.readString(doc, "$.concurrentRate");
            val var12: java.lang.String = JsonExtensionsKt.readString(doc, "$.header");
            val var13: java.lang.String = JsonExtensionsKt.readString(doc, "$.loginUrl");
            val var14: java.lang.String = JsonExtensionsKt.readString(doc, "$.loginCheckJs");
            val var15: java.lang.String = JsonExtensionsKt.readString(doc, "$.sortUrl");
            val var16: java.lang.Boolean = JsonExtensionsKt.readBool(doc, "$.singleUrl");
            val var17: Boolean = var16 != null && var16;
            val var18: Int = JsonExtensionsKt.readInt(doc, "$.articleStyle");
            val var40: Int = if (var18 == null) 0 else var18;
            val var41: java.lang.String = JsonExtensionsKt.readString(doc, "$.ruleArticles");
            val var19: java.lang.String = JsonExtensionsKt.readString(doc, "$.ruleNextPage");
            val var20: java.lang.String = JsonExtensionsKt.readString(doc, "$.ruleTitle");
            val var21: java.lang.String = JsonExtensionsKt.readString(doc, "$.rulePubDate");
            val var22: java.lang.String = JsonExtensionsKt.readString(doc, "$.ruleDescription");
            val var23: java.lang.String = JsonExtensionsKt.readString(doc, "$.ruleImage");
            val var24: java.lang.String = JsonExtensionsKt.readString(doc, "$.ruleLink");
            val var25: java.lang.String = JsonExtensionsKt.readString(doc, "$.ruleContent");
            val var26: java.lang.String = JsonExtensionsKt.readString(doc, "$.style");
            val var27: java.lang.Boolean = JsonExtensionsKt.readBool(doc, "$.enableJs");
            val var28: Boolean = var27 == null || var27;
            val var29: java.lang.Boolean = JsonExtensionsKt.readBool(doc, "$.loadWithBaseUrl");
            val var42: Boolean = var29 == null || var29;
            val var30: java.lang.Boolean = JsonExtensionsKt.readBool(doc, "$.enabledCookieJar");
            val var43: Boolean = var30 != null && var30;
            val var31: Int = JsonExtensionsKt.readInt(doc, "$.customOrder");
            var3 = Result.constructor-impl(
               new RssSource(
                  var10000,
                  var45,
                  var8,
                  var7,
                  var9,
                  var11,
                  null,
                  var43,
                  var39,
                  var12,
                  var13,
                  null,
                  var14,
                  var15,
                  var17,
                  var40,
                  var41,
                  var19,
                  var20,
                  var21,
                  var22,
                  var23,
                  var24,
                  var25,
                  var26,
                  var28,
                  var42,
                  if (var31 == null) 0 else var31,
                  2112,
                  null
               )
            );
         } catch (var32: java.lang.Throwable) {
            val var5: Result.Companion = Result.Companion;
            var3 = Result.constructor-impl(ResultKt.createFailure(var32));
         }

         return var3;
      }

      public fun fromJson(json: String): Result<RssSource> {
         val var2: DocumentContext = JsonExtensionsKt.getJsonPath().parse(json);
         return this.fromJsonDoc-IoAF18A(var2);
      }

      public fun fromJsonArray(jsonArray: String): Result<ArrayList<RssSource>> {
         var var3: Any;
         try {
            var3 = Result.Companion;
            val var24: ArrayList = new ArrayList();

            val `$this$forEach$iv`: java.lang.Iterable;
            for (Object element$iv : $this$forEach$iv) {
               val jsonItem: DocumentContext = JsonExtensionsKt.getJsonPath().parse(`element$iv`);
               val var10000: RssSource.Companion = RssSource.Companion;
               val var14: Any = var10000.fromJsonDoc-IoAF18A(jsonItem);
               ResultKt.throwOnFailure(var14);
               var24.add(var14 as RssSource);
            }

            var3 = Result.constructor-impl(var24);
         } catch (var19: java.lang.Throwable) {
            val doc: Result.Companion = Result.Companion;
            var3 = Result.constructor-impl(ResultKt.createFailure(var19));
         }

         return var3;
      }
   }
}
