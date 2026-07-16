package io.legado.app.data.entities

import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import com.jayway.jsonpath.DocumentContext
import com.jayway.jsonpath.Predicate
import com.script.SimpleBindings
import io.legado.app.help.http.StrResponse
import io.legado.app.model.DebugLog
import io.legado.app.model.analyzeRule.QueryTTF
import io.legado.app.utils.GsonExtensionsKt
import io.legado.app.utils.JsonExtensionsKt
import java.io.File
import java.util.ArrayList
import kotlin.jvm.internal.Intrinsics
import org.jsoup.Connection.Response

@JsonIgnoreProperties(["headerMap", "source", "_userNameSpace", "userNameSpace"])
public data class HttpTTS(id: Long = System.currentTimeMillis(),
      name: String = "",
      url: String = "",
      contentType: String? = null,
      concurrentRate: String? = "0",
      loginUrl: String? = null,
      loginUi: String? = null,
      header: String? = null,
      jsLib: String? = null,
      enabledCookieJar: Boolean? = false,
      loginCheckJs: String? = null,
      lastUpdateTime: Long = System.currentTimeMillis()
   ) :
   BaseSource {
   private final var _userNameSpace: String

   public open var concurrentRate: String?
      internal final set

   public final var contentType: String?
      internal set

   private final var debugLog: DebugLog?

   public open var enabledCookieJar: Boolean?
      internal final set

   public open var header: String?
      internal final set

   public final val id: Long

   public final var jsLib: String?
      internal set

   public final var lastUpdateTime: Long
      internal set

   public final var loginCheckJs: String?
      internal set

   public open var loginUi: String?
      internal final set

   public open var loginUrl: String?
      internal final set

   public final var name: String
      internal set

   public final var url: String
      internal set

   init {
      this.id = id;
      this.name = name;
      this.url = url;
      this.contentType = contentType;
      this.concurrentRate = concurrentRate;
      this.loginUrl = loginUrl;
      this.loginUi = loginUi;
      this.header = header;
      this.jsLib = jsLib;
      this.enabledCookieJar = enabledCookieJar;
      this.loginCheckJs = loginCheckJs;
      this.lastUpdateTime = lastUpdateTime;
      this._userNameSpace = "";
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

   public override fun getTag(): String {
      return this.name;
   }

   public override fun getKey(): String {
      return Intrinsics.stringPlus("httpTts:", this.id);
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

   public operator fun component1(): Long {
      return this.id;
   }

   public operator fun component2(): String {
      return this.name;
   }

   public operator fun component3(): String {
      return this.url;
   }

   public operator fun component4(): String? {
      return this.contentType;
   }

   public operator fun component5(): String? {
      return this.getConcurrentRate();
   }

   public operator fun component6(): String? {
      return this.getLoginUrl();
   }

   public operator fun component7(): String? {
      return this.getLoginUi();
   }

   public operator fun component8(): String? {
      return this.getHeader();
   }

   public operator fun component9(): String? {
      return this.jsLib;
   }

   public operator fun component10(): Boolean? {
      return this.getEnabledCookieJar();
   }

   public operator fun component11(): String? {
      return this.loginCheckJs;
   }

   public operator fun component12(): Long {
      return this.lastUpdateTime;
   }

   public fun copy(
      id: Long = this.id,
      name: String = this.name,
      url: String = this.url,
      contentType: String? = this.contentType,
      concurrentRate: String? = this.getConcurrentRate(),
      loginUrl: String? = this.getLoginUrl(),
      loginUi: String? = this.getLoginUi(),
      header: String? = this.getHeader(),
      jsLib: String? = this.jsLib,
      enabledCookieJar: Boolean? = this.getEnabledCookieJar(),
      loginCheckJs: String? = this.loginCheckJs,
      lastUpdateTime: Long = this.lastUpdateTime
   ): HttpTTS {
      return new HttpTTS(id, name, url, contentType, concurrentRate, loginUrl, loginUi, header, jsLib, enabledCookieJar, loginCheckJs, lastUpdateTime);
   }

   public override fun toString(): String {
      val var1: StringBuilder = new StringBuilder();
      var1.append("HttpTTS(id=")
         .append(this.id)
         .append(", name=")
         .append(this.name)
         .append(", url=")
         .append(this.url)
         .append(", contentType=")
         .append(this.contentType)
         .append(", concurrentRate=")
         .append(this.getConcurrentRate())
         .append(", loginUrl=")
         .append(this.getLoginUrl())
         .append(", loginUi=")
         .append(this.getLoginUi())
         .append(", header=")
         .append(this.getHeader())
         .append(", jsLib=")
         .append(this.jsLib)
         .append(", enabledCookieJar=")
         .append(this.getEnabledCookieJar())
         .append(", loginCheckJs=")
         .append(this.loginCheckJs)
         .append(", lastUpdateTime=");
      var1.append(this.lastUpdateTime).append(')');
      return var1.toString();
   }

   public override fun hashCode(): Int {
      return (
               (
                        (
                                 (
                                          (
                                                   (
                                                            (
                                                                     (
                                                                              (
                                                                                       (java.lang.Long.hashCode(this.id) * 31 + this.name.hashCode()) * 31
                                                                                          + this.url.hashCode()
                                                                                    )
                                                                                    * 31
                                                                                 + (if (this.contentType == null) 0 else this.contentType.hashCode())
                                                                           )
                                                                           * 31
                                                                        + (if (this.getConcurrentRate() == null) 0 else this.getConcurrentRate().hashCode())
                                                                  )
                                                                  * 31
                                                               + (if (this.getLoginUrl() == null) 0 else this.getLoginUrl().hashCode())
                                                         )
                                                         * 31
                                                      + (if (this.getLoginUi() == null) 0 else this.getLoginUi().hashCode())
                                                )
                                                * 31
                                             + (if (this.getHeader() == null) 0 else this.getHeader().hashCode())
                                       )
                                       * 31
                                    + (if (this.jsLib == null) 0 else this.jsLib.hashCode())
                              )
                              * 31
                           + (if (this.getEnabledCookieJar() == null) 0 else this.getEnabledCookieJar().hashCode())
                     )
                     * 31
                  + (if (this.loginCheckJs == null) 0 else this.loginCheckJs.hashCode())
            )
            * 31
         + java.lang.Long.hashCode(this.lastUpdateTime);
   }

   public override operator fun equals(other: Any?): Boolean {
      if (this === other) {
         return true;
      } else if (other !is HttpTTS) {
         return false;
      } else {
         val var2: HttpTTS = other as HttpTTS;
         if (this.id != (other as HttpTTS).id) {
            return false;
         } else if (!(this.name == var2.name)) {
            return false;
         } else if (!(this.url == var2.url)) {
            return false;
         } else if (!(this.contentType == var2.contentType)) {
            return false;
         } else if (!(this.getConcurrentRate() == var2.getConcurrentRate())) {
            return false;
         } else if (!(this.getLoginUrl() == var2.getLoginUrl())) {
            return false;
         } else if (!(this.getLoginUi() == var2.getLoginUi())) {
            return false;
         } else if (!(this.getHeader() == var2.getHeader())) {
            return false;
         } else if (!(this.jsLib == var2.jsLib)) {
            return false;
         } else if (!(this.getEnabledCookieJar() == var2.getEnabledCookieJar())) {
            return false;
         } else if (!(this.loginCheckJs == var2.loginCheckJs)) {
            return false;
         } else {
            return this.lastUpdateTime == var2.lastUpdateTime;
         }
      }
   }

   fun HttpTTS() {
      this(0L, null, null, null, null, null, null, null, null, null, null, 0L, 4095, null);
   }

   public companion object {
      public fun fromJsonDoc(doc: DocumentContext): Result<HttpTTS> {
         var var3: Any;
         try {
            var3 = Result.Companion;
            val var10: Any = doc.read("$.loginUi", new Predicate[0]);
            val var12: java.lang.Long = JsonExtensionsKt.readLong(doc, "$.id");
            val var10002: Long = if (var12 == null) System.currentTimeMillis() else var12;
            val var10003: java.lang.String = JsonExtensionsKt.readString(doc, "$.name");
            val var10004: java.lang.String = JsonExtensionsKt.readString(doc, "$.url");
            var3 = Result.constructor-impl(
               new HttpTTS(
                  var10002,
                  var10003,
                  var10004,
                  JsonExtensionsKt.readString(doc, "$.contentType"),
                  JsonExtensionsKt.readString(doc, "$.concurrentRate"),
                  JsonExtensionsKt.readString(doc, "$.loginUrl"),
                  if (var10 is java.util.List) GsonExtensionsKt.getGSON().toJson(var10) else (if (var10 == null) null else var10.toString()),
                  JsonExtensionsKt.readString(doc, "$.header"),
                  null,
                  null,
                  JsonExtensionsKt.readString(doc, "$.loginCheckJs"),
                  0L,
                  2816,
                  null
               )
            );
         } catch (var7: java.lang.Throwable) {
            val loginUi: Result.Companion = Result.Companion;
            var3 = Result.constructor-impl(ResultKt.createFailure(var7));
         }

         return var3;
      }

      public fun fromJson(json: String): Result<HttpTTS> {
         val var2: DocumentContext = JsonExtensionsKt.getJsonPath().parse(json);
         return this.fromJsonDoc-IoAF18A(var2);
      }

      public fun fromJsonArray(jsonArray: String): Result<ArrayList<HttpTTS>> {
         var var3: Any;
         try {
            var3 = Result.Companion;
            val var24: ArrayList = new ArrayList();

            val `$this$forEach$iv`: java.lang.Iterable;
            for (Object element$iv : $this$forEach$iv) {
               val jsonItem: DocumentContext = JsonExtensionsKt.getJsonPath().parse(`element$iv`);
               val var10000: HttpTTS.Companion = HttpTTS.Companion;
               val var14: Any = var10000.fromJsonDoc-IoAF18A(jsonItem);
               ResultKt.throwOnFailure(var14);
               var24.add(var14 as HttpTTS);
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
