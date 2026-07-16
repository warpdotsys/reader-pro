package io.legado.app.data.entities

import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import com.google.gson.Gson
import com.script.SimpleBindings
import io.legado.app.data.entities.rule.BookInfoRule
import io.legado.app.data.entities.rule.ContentRule
import io.legado.app.data.entities.rule.ExploreRule
import io.legado.app.data.entities.rule.SearchRule
import io.legado.app.data.entities.rule.TocRule
import io.legado.app.help.SourceAnalyzer
import io.legado.app.help.http.StrResponse
import io.legado.app.model.DebugLog
import io.legado.app.model.analyzeRule.QueryTTF
import io.legado.app.utils.GsonExtensionsKt
import java.io.File
import java.io.InputStream
import java.lang.reflect.Type
import org.jsoup.Connection.Response

@JsonIgnoreProperties(["headerMap", "source", "_userNameSpace", "userNameSpace", "loginHeader", "loginHeaderMap", "loginInfo", "loginInfoMap"])
public data class BookSource(bookSourceUrl: String = "",
      bookSourceName: String = "",
      bookSourceGroup: String? = null,
      bookSourceType: Int = 0,
      bookUrlPattern: String? = null,
      customOrder: Int = 0,
      enabled: Boolean = true,
      enabledExplore: Boolean = true,
      enabledCookieJar: Boolean? = false,
      concurrentRate: String? = null,
      header: String? = null,
      loginUrl: String? = null,
      loginUi: String? = null,
      loginCheckJs: String? = null,
      bookSourceComment: String? = null,
      variableComment: String? = null,
      lastUpdateTime: Long = 0L,
      respondTime: Long = 180000L,
      weight: Int = 0,
      exploreUrl: String? = null,
      ruleExplore: ExploreRule? = null,
      searchUrl: String? = null,
      ruleSearch: SearchRule? = null,
      ruleBookInfo: BookInfoRule? = null,
      ruleToc: TocRule? = null,
      ruleContent: ContentRule? = null
   ) :
   BaseSource {
   private final var _userNameSpace: String
   private final var bookInfoRuleV: BookInfoRule?

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

   public open var concurrentRate: String?
      internal final set

   private final var contentRuleV: ContentRule?

   public final var customOrder: Int
      internal set

   private final var debugLog: DebugLog?

   public final var enabled: Boolean
      internal set

   public open var enabledCookieJar: Boolean?
      internal final set

   public final var enabledExplore: Boolean
      internal set

   private final var exploreRuleV: ExploreRule?

   public final var exploreUrl: String?
      internal set

   public open var header: String?
      internal final set

   public final var lastUpdateTime: Long
      internal set

   public final var loginCheckJs: String?
      internal set

   public open var loginUi: String?
      internal final set

   public open var loginUrl: String?
      internal final set

   public final var respondTime: Long
      internal set

   public final var ruleBookInfo: BookInfoRule?
      internal set

   public final var ruleContent: ContentRule?
      internal set

   public final var ruleExplore: ExploreRule?
      internal set

   public final var ruleSearch: SearchRule?
      internal set

   public final var ruleToc: TocRule?
      internal set

   private final var searchRuleV: SearchRule?

   public final var searchUrl: String?
      internal set

   private final var tocRuleV: TocRule?

   public final var variableComment: String?
      internal set

   public final var weight: Int
      internal set

   init {
      this.bookSourceUrl = bookSourceUrl;
      this.bookSourceName = bookSourceName;
      this.bookSourceGroup = bookSourceGroup;
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
      this.variableComment = variableComment;
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
      this._userNameSpace = "";
   }

   public override fun getTag(): String {
      return this.bookSourceName;
   }

   public override fun getKey(): String {
      return this.bookSourceUrl;
   }

   public override fun hashCode(): Int {
      return this.bookSourceUrl.hashCode();
   }

   public override operator fun equals(other: Any?): Boolean {
      return other is BookSource && (other as BookSource).bookSourceUrl == this.bookSourceUrl;
   }

   public fun getSearchRule(): SearchRule {
      return if (this.ruleSearch == null) new SearchRule(null, null, null, null, null, null, null, null, null, null, 1023, null) else this.ruleSearch;
   }

   public fun getExploreRule(): ExploreRule {
      return if (this.ruleExplore == null) new ExploreRule(null, null, null, null, null, null, null, null, null, null, 1023, null) else this.ruleExplore;
   }

   public fun getBookInfoRule(): BookInfoRule {
      return if (this.ruleBookInfo == null)
         new BookInfoRule(null, null, null, null, null, null, null, null, null, null, null, 2047, null)
         else
         this.ruleBookInfo;
   }

   public fun getTocRule(): TocRule {
      return if (this.ruleToc == null) new TocRule(null, null, null, null, null, null, null, null, 255, null) else this.ruleToc;
   }

   public fun getContentRule(): ContentRule {
      return if (this.ruleContent == null) new ContentRule(null, null, null, null, null, null, 63, null) else this.ruleContent;
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
      return this.debugLog;
   }

   public fun equal(source: BookSource): Boolean {
      return this.equal(this.bookSourceName, source.bookSourceName)
         && this.equal(this.bookSourceUrl, source.bookSourceUrl)
         && this.equal(this.bookSourceGroup, source.bookSourceGroup)
         && this.bookSourceType == source.bookSourceType
         && this.equal(this.bookUrlPattern, source.bookUrlPattern)
         && this.enabled == source.enabled
         && this.enabledExplore == source.enabledExplore
         && this.getEnabledCookieJar() == source.getEnabledCookieJar()
         && this.equal(this.getHeader(), source.getHeader())
         && this.equal(this.getLoginUrl(), source.getLoginUrl())
         && this.equal(this.exploreUrl, source.exploreUrl)
         && this.equal(this.searchUrl, source.searchUrl)
         && this.getSearchRule() == source.getSearchRule()
         && this.getExploreRule() == source.getExploreRule()
         && this.getBookInfoRule() == source.getBookInfoRule()
         && this.getTocRule() == source.getTocRule()
         && this.getContentRule() == source.getContentRule();
   }

   private fun equal(a: String?, b: String?): Boolean {
      label21:
      if (!(a == b)) {
         return (a == null || a.length() == 0) && (b == null || b.length() == 0);
      } else {
         return true;
      }
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
      return this.bookSourceUrl;
   }

   public operator fun component2(): String {
      return this.bookSourceName;
   }

   public operator fun component3(): String? {
      return this.bookSourceGroup;
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

   public operator fun component9(): Boolean? {
      return this.getEnabledCookieJar();
   }

   public operator fun component10(): String? {
      return this.getConcurrentRate();
   }

   public operator fun component11(): String? {
      return this.getHeader();
   }

   public operator fun component12(): String? {
      return this.getLoginUrl();
   }

   public operator fun component13(): String? {
      return this.getLoginUi();
   }

   public operator fun component14(): String? {
      return this.loginCheckJs;
   }

   public operator fun component15(): String? {
      return this.bookSourceComment;
   }

   public operator fun component16(): String? {
      return this.variableComment;
   }

   public operator fun component17(): Long {
      return this.lastUpdateTime;
   }

   public operator fun component18(): Long {
      return this.respondTime;
   }

   public operator fun component19(): Int {
      return this.weight;
   }

   public operator fun component20(): String? {
      return this.exploreUrl;
   }

   public operator fun component21(): ExploreRule? {
      return this.ruleExplore;
   }

   public operator fun component22(): String? {
      return this.searchUrl;
   }

   public operator fun component23(): SearchRule? {
      return this.ruleSearch;
   }

   public operator fun component24(): BookInfoRule? {
      return this.ruleBookInfo;
   }

   public operator fun component25(): TocRule? {
      return this.ruleToc;
   }

   public operator fun component26(): ContentRule? {
      return this.ruleContent;
   }

   public fun copy(
      bookSourceUrl: String = this.bookSourceUrl,
      bookSourceName: String = this.bookSourceName,
      bookSourceGroup: String? = this.bookSourceGroup,
      bookSourceType: Int = this.bookSourceType,
      bookUrlPattern: String? = this.bookUrlPattern,
      customOrder: Int = this.customOrder,
      enabled: Boolean = this.enabled,
      enabledExplore: Boolean = this.enabledExplore,
      enabledCookieJar: Boolean? = this.getEnabledCookieJar(),
      concurrentRate: String? = this.getConcurrentRate(),
      header: String? = this.getHeader(),
      loginUrl: String? = this.getLoginUrl(),
      loginUi: String? = this.getLoginUi(),
      loginCheckJs: String? = this.loginCheckJs,
      bookSourceComment: String? = this.bookSourceComment,
      variableComment: String? = this.variableComment,
      lastUpdateTime: Long = this.lastUpdateTime,
      respondTime: Long = this.respondTime,
      weight: Int = this.weight,
      exploreUrl: String? = this.exploreUrl,
      ruleExplore: ExploreRule? = this.ruleExplore,
      searchUrl: String? = this.searchUrl,
      ruleSearch: SearchRule? = this.ruleSearch,
      ruleBookInfo: BookInfoRule? = this.ruleBookInfo,
      ruleToc: TocRule? = this.ruleToc,
      ruleContent: ContentRule? = this.ruleContent
   ): BookSource {
      return new BookSource(
         bookSourceUrl,
         bookSourceName,
         bookSourceGroup,
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
         variableComment,
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
      var1.append("BookSource(bookSourceUrl=")
         .append(this.bookSourceUrl)
         .append(", bookSourceName=")
         .append(this.bookSourceName)
         .append(", bookSourceGroup=")
         .append(this.bookSourceGroup)
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
         .append(this.getEnabledCookieJar())
         .append(", concurrentRate=")
         .append(this.getConcurrentRate())
         .append(", header=")
         .append(this.getHeader())
         .append(", loginUrl=");
      var1.append(this.getLoginUrl())
         .append(", loginUi=")
         .append(this.getLoginUi())
         .append(", loginCheckJs=")
         .append(this.loginCheckJs)
         .append(", bookSourceComment=")
         .append(this.bookSourceComment)
         .append(", variableComment=")
         .append(this.variableComment)
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
         .append(this.ruleSearch);
      var1.append(", ruleBookInfo=")
         .append(this.ruleBookInfo)
         .append(", ruleToc=")
         .append(this.ruleToc)
         .append(", ruleContent=")
         .append(this.ruleContent)
         .append(')');
      return var1.toString();
   }

   fun BookSource() {
      this(
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
   }

   public companion object {
      public fun fromJson(json: String): Result<BookSource> {
         return SourceAnalyzer.INSTANCE.jsonToBookSource-IoAF18A(json);
      }

      public fun fromJsonArray(json: String): Result<MutableList<BookSource>> {
         return SourceAnalyzer.INSTANCE.jsonToBookSources-IoAF18A(json);
      }

      public fun fromJsonArray(inputStream: InputStream): Result<MutableList<BookSource>> {
         return SourceAnalyzer.INSTANCE.jsonToBookSources-IoAF18A(inputStream);
      }
   }

   public class Converters {
      public fun exploreRuleToString(exploreRule: ExploreRule?): String {
         val var2: java.lang.String = GsonExtensionsKt.getGSON().toJson(exploreRule);
         return var2;
      }

      public fun stringToExploreRule(json: String?): ExploreRule? {
         val `$this$fromJsonObject$iv`: Gson = GsonExtensionsKt.getGSON();

         var var5: Any;
         try {
            var5 = Result.Companion;
            val var15: Type = new BookSource$Converters$stringToExploreRule$$inlined$fromJsonObject$1().getType();
            var var10000: Any = `$this$fromJsonObject$iv`.fromJson(json, var15);
            if (var10000 !is ExploreRule) {
               var10000 = null;
            }

            var5 = Result.constructor-impl(var10000 as ExploreRule);
         } catch (var9: java.lang.Throwable) {
            val `$i$f$genericType`: Result.Companion = Result.Companion;
            var5 = Result.constructor-impl(ResultKt.createFailure(var9));
         }

         return (if (Result.isFailure-impl(var5)) null else var5) as ExploreRule;
      }

      public fun searchRuleToString(searchRule: SearchRule?): String {
         val var2: java.lang.String = GsonExtensionsKt.getGSON().toJson(searchRule);
         return var2;
      }

      public fun stringToSearchRule(json: String?): SearchRule? {
         val `$this$fromJsonObject$iv`: Gson = GsonExtensionsKt.getGSON();

         var var5: Any;
         try {
            var5 = Result.Companion;
            val var15: Type = new BookSource$Converters$stringToSearchRule$$inlined$fromJsonObject$1().getType();
            var var10000: Any = `$this$fromJsonObject$iv`.fromJson(json, var15);
            if (var10000 !is SearchRule) {
               var10000 = null;
            }

            var5 = Result.constructor-impl(var10000 as SearchRule);
         } catch (var9: java.lang.Throwable) {
            val `$i$f$genericType`: Result.Companion = Result.Companion;
            var5 = Result.constructor-impl(ResultKt.createFailure(var9));
         }

         return (if (Result.isFailure-impl(var5)) null else var5) as SearchRule;
      }

      public fun bookInfoRuleToString(bookInfoRule: BookInfoRule?): String {
         val var2: java.lang.String = GsonExtensionsKt.getGSON().toJson(bookInfoRule);
         return var2;
      }

      public fun stringToBookInfoRule(json: String?): BookInfoRule? {
         val `$this$fromJsonObject$iv`: Gson = GsonExtensionsKt.getGSON();

         var var5: Any;
         try {
            var5 = Result.Companion;
            val var15: Type = new BookSource$Converters$stringToBookInfoRule$$inlined$fromJsonObject$1().getType();
            var var10000: Any = `$this$fromJsonObject$iv`.fromJson(json, var15);
            if (var10000 !is BookInfoRule) {
               var10000 = null;
            }

            var5 = Result.constructor-impl(var10000 as BookInfoRule);
         } catch (var9: java.lang.Throwable) {
            val `$i$f$genericType`: Result.Companion = Result.Companion;
            var5 = Result.constructor-impl(ResultKt.createFailure(var9));
         }

         return (if (Result.isFailure-impl(var5)) null else var5) as BookInfoRule;
      }

      public fun tocRuleToString(tocRule: TocRule?): String {
         val var2: java.lang.String = GsonExtensionsKt.getGSON().toJson(tocRule);
         return var2;
      }

      public fun stringToTocRule(json: String?): TocRule? {
         val `$this$fromJsonObject$iv`: Gson = GsonExtensionsKt.getGSON();

         var var5: Any;
         try {
            var5 = Result.Companion;
            val var15: Type = new BookSource$Converters$stringToTocRule$$inlined$fromJsonObject$1().getType();
            var var10000: Any = `$this$fromJsonObject$iv`.fromJson(json, var15);
            if (var10000 !is TocRule) {
               var10000 = null;
            }

            var5 = Result.constructor-impl(var10000 as TocRule);
         } catch (var9: java.lang.Throwable) {
            val `$i$f$genericType`: Result.Companion = Result.Companion;
            var5 = Result.constructor-impl(ResultKt.createFailure(var9));
         }

         return (if (Result.isFailure-impl(var5)) null else var5) as TocRule;
      }

      public fun contentRuleToString(contentRule: ContentRule?): String {
         val var2: java.lang.String = GsonExtensionsKt.getGSON().toJson(contentRule);
         return var2;
      }

      public fun stringToContentRule(json: String?): ContentRule? {
         val `$this$fromJsonObject$iv`: Gson = GsonExtensionsKt.getGSON();

         var var5: Any;
         try {
            var5 = Result.Companion;
            val var15: Type = new BookSource$Converters$stringToContentRule$$inlined$fromJsonObject$1().getType();
            var var10000: Any = `$this$fromJsonObject$iv`.fromJson(json, var15);
            if (var10000 !is ContentRule) {
               var10000 = null;
            }

            var5 = Result.constructor-impl(var10000 as ContentRule);
         } catch (var9: java.lang.Throwable) {
            val `$i$f$genericType`: Result.Companion = Result.Companion;
            var5 = Result.constructor-impl(ResultKt.createFailure(var9));
         }

         return (if (Result.isFailure-impl(var5)) null else var5) as ContentRule;
      }
   }

   public data class ExploreKind(title: String, url: String? = null) {
      public final var title: String
         internal set

      public final var url: String?
         internal set

      init {
         this.title = title;
         this.url = url;
      }

      public operator fun component1(): String {
         return this.title;
      }

      public operator fun component2(): String? {
         return this.url;
      }

      public fun copy(title: String = this.title, url: String? = this.url): io.legado.app.data.entities.BookSource.ExploreKind {
         return new BookSource.ExploreKind(title, url);
      }

      public override fun toString(): String {
         return "ExploreKind(title=${this.title}, url=${this.url})";
      }

      public override fun hashCode(): Int {
         return this.title.hashCode() * 31 + (if (this.url == null) 0 else this.url.hashCode());
      }

      public override operator fun equals(other: Any?): Boolean {
         if (this === other) {
            return true;
         } else if (other !is BookSource.ExploreKind) {
            return false;
         } else {
            val var2: BookSource.ExploreKind = other as BookSource.ExploreKind;
            if (!(this.title == (other as BookSource.ExploreKind).title)) {
               return false;
            } else {
               return this.url == var2.url;
            }
         }
      }
   }
}
