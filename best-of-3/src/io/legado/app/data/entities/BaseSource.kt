package io.legado.app.data.entities

import com.google.gson.Gson
import com.script.SimpleBindings
import io.legado.app.constant.AppConst
import io.legado.app.help.CacheManager
import io.legado.app.help.JsExtensions
import io.legado.app.help.http.CookieStore
import io.legado.app.help.http.StrResponse
import io.legado.app.model.analyzeRule.QueryTTF
import io.legado.app.utils.Base64
import io.legado.app.utils.EncoderUtils
import io.legado.app.utils.GsonExtensionsKt
import java.io.File
import java.lang.reflect.Type
import java.util.HashMap
import kotlin.Result.Companion
import kotlin.jvm.internal.Intrinsics
import org.jsoup.Connection.Response

public interface BaseSource : JsExtensions {
   public var concurrentRate: String?
      internal final set

   public var enabledCookieJar: Boolean?
      internal final set

   public var header: String?
      internal final set

   public var loginUi: String?
      internal final set

   public var loginUrl: String?
      internal final set

   public abstract fun getTag(): String {
   }

   public abstract fun getKey(): String {
   }

   public override fun getSource(): BaseSource? {
   }

   public open fun getLoginJs(): String? {
   }

   public open fun login() {
   }

   public open fun getHeaderMap(hasLoginHeader: Boolean = ...): HashMap<String, String> {
   }

   public open fun getLoginHeader(): String? {
   }

   public open fun getLoginHeaderMap(): Map<String, String>? {
   }

   public open fun putLoginHeader(header: String) {
   }

   public open fun removeLoginHeader() {
   }

   public open fun getLoginInfo(): String? {
   }

   public open fun getLoginInfoMap(): Map<String, String>? {
   }

   public open fun putLoginInfo(info: String): Boolean {
   }

   public open fun removeLoginInfo() {
   }

   public open fun setVariable(variable: String?) {
   }

   public open fun getVariable(): String? {
   }

   @Throws(java/lang/Exception::class)
   public open fun evalJS(jsStr: String, bindingsConfig: (SimpleBindings) -> Unit = ...): Any? {
   }

   internal class DefaultImpls {
      @JvmStatic
      fun getSource(`this`: BaseSource): BaseSource? {
         return this;
      }

      @JvmStatic
      fun getLoginJs(`this`: BaseSource): java.lang.String? {
         val loginJs: java.lang.String = this.getLoginUrl();
         val var10000: java.lang.String;
         if (loginJs == null) {
            var10000 = null;
         } else if (StringsKt.startsWith$default(loginJs, "@js:", false, 2, null)) {
            var10000 = loginJs.substring(4);
         } else if (StringsKt.startsWith$default(loginJs, "<js>", false, 2, null)) {
            var10000 = loginJs.substring(4, StringsKt.lastIndexOf$default(loginJs, "<", 0, false, 6, null));
         } else {
            var10000 = loginJs;
         }

         return var10000;
      }

      @JvmStatic
      fun login(`this`: BaseSource) {
         val var1: java.lang.String = this.getLoginJs();
         if (var1 != null) {
            evalJS$default(this, var1, null, 2, null);
         }
      }

      @JvmStatic
      fun getHeaderMap(`this`: BaseSource, hasLoginHeader: Boolean): HashMap<java.lang.String, java.lang.String> {
         val var2: HashMap = new HashMap();
         var2.put("User-Agent", AppConst.INSTANCE.getUserAgent());
         val var23: java.lang.String = this.getHeader();
         if (var23 != null) {
            val `$this$fromJsonObject$iv`: Gson = GsonExtensionsKt.getGSON();
            var var10000: java.lang.String;
            if (StringsKt.startsWith(var23, "@js:", true)) {
               val var10001: java.lang.String = var23.substring(4);
               var10000 = java.lang.String.valueOf(evalJS$default(this, var10001, null, 2, null));
            } else if (StringsKt.startsWith(var23, "<js>", true)) {
               val var45: java.lang.String = var23.substring(4, StringsKt.lastIndexOf$default(var23, "<", 0, false, 6, null));
               var10000 = java.lang.String.valueOf(evalJS$default(this, var45, null, 2, null));
            } else {
               var10000 = var23;
            }

            val `json$iv`: java.lang.String = var10000;

            var var37: Any;
            try {
               var37 = Result.Companion;
               val var43: Type = new BaseSource$DefaultImpls$getHeaderMap$lambda-4$lambda-2$$inlined$fromJsonObject$1().getType();
               var10000 = `$this$fromJsonObject$iv`.fromJson(`json$iv`, var43);
               if (var10000 !is java.util.Map) {
                  var10000 = null;
               }

               var37 = Result.constructor-impl(var10000 as java.util.Map);
            } catch (var22: java.lang.Throwable) {
               val `$i$f$genericType`: Companion = Result.Companion;
               var37 = Result.constructor-impl(ResultKt.createFailure(var22));
            }

            val var21: java.util.Map = (if (Result.isFailure-impl(var37)) null else var37) as java.util.Map;
            if (var21 != null) {
               var2.putAll(var21);
            }
         }

         if (hasLoginHeader) {
            val var24: java.util.Map = this.getLoginHeaderMap();
            if (var24 != null) {
               var2.putAll(var24);
            }
         }

         return var2;
      }

      @JvmStatic
      fun getLoginHeader(`this`: BaseSource): java.lang.String? {
         return new CacheManager(this.getUserNameSpace()).get(Intrinsics.stringPlus("loginHeader_", this.getKey()));
      }

      @JvmStatic
      fun getLoginHeaderMap(`this`: BaseSource): MutableMap<java.lang.String, java.lang.String>? {
         val `$this$fromJsonObject$iv`: java.lang.String = this.getLoginHeader();
         if (`$this$fromJsonObject$iv` == null) {
            return null;
         } else {
            val cache: java.lang.String = `$this$fromJsonObject$iv`;
            val var10: Gson = GsonExtensionsKt.getGSON();

            var var5: Any;
            try {
               var5 = Result.Companion;
               val var16: Type = new BaseSource$DefaultImpls$getLoginHeaderMap$$inlined$fromJsonObject$1().getType();
               var var10000: Any = var10.fromJson(cache, var16);
               if (var10000 !is java.util.Map) {
                  var10000 = null;
               }

               var5 = Result.constructor-impl(var10000 as java.util.Map);
            } catch (var9: java.lang.Throwable) {
               val `$i$f$genericType`: Companion = Result.Companion;
               var5 = Result.constructor-impl(ResultKt.createFailure(var9));
            }

            return (if (Result.isFailure-impl(var5)) null else var5) as MutableMap<java.lang.String, java.lang.String>;
         }
      }

      @JvmStatic
      fun putLoginHeader(`this`: BaseSource, header: java.lang.String) {
         CacheManager.put$default(new CacheManager(this.getUserNameSpace()), Intrinsics.stringPlus("loginHeader_", this.getKey()), header, 0, 4, null);
      }

      @JvmStatic
      fun removeLoginHeader(`this`: BaseSource) {
         new CacheManager(this.getUserNameSpace()).delete(Intrinsics.stringPlus("loginHeader_", this.getKey()));
      }

      @JvmStatic
      fun getLoginInfo(`this`: BaseSource): java.lang.String? {
         try {
            val e: ByteArray = StringsKt.encodeToByteArray$default(AppConst.INSTANCE.getUserAgent(), 0, 8, false, 4, null);
            val encodeBytes: java.lang.String = new CacheManager(this.getUserNameSpace()).get(Intrinsics.stringPlus("userInfo_", this.getKey()));
            if (encodeBytes == null) {
               return null;
            } else {
               val decodeBytes: java.lang.String = EncoderUtils.INSTANCE.base64Decode(encodeBytes, 0);
               label17:
               if (decodeBytes == null) {
                  throw new NullPointerException("null cannot be cast to non-null type java.lang.String");
               } else {
                  val var10000: ByteArray = decodeBytes.getBytes(Charsets.UTF_8);
                  val var11: ByteArray = EncoderUtils.decryptAES$default(EncoderUtils.INSTANCE, var10000, e, null, null, 12, null);
                  return if (var11 == null) null else new java.lang.String(var11, Charsets.UTF_8);
               }
            }
         } catch (var8: Exception) {
            this.log(Intrinsics.stringPlus("获取登陆信息出错 ", var8.getLocalizedMessage()));
            return null;
         }
      }

      @JvmStatic
      fun getLoginInfoMap(`this`: BaseSource): MutableMap<java.lang.String, java.lang.String>? {
         val `$this$fromJsonObject$iv`: Gson = GsonExtensionsKt.getGSON();
         val `json$iv`: java.lang.String = this.getLoginInfo();

         var var5: Any;
         try {
            var5 = Result.Companion;
            val var15: Type = new BaseSource$DefaultImpls$getLoginInfoMap$$inlined$fromJsonObject$1().getType();
            var var10000: Any = `$this$fromJsonObject$iv`.fromJson(`json$iv`, var15);
            if (var10000 !is java.util.Map) {
               var10000 = null;
            }

            var5 = Result.constructor-impl(var10000 as java.util.Map);
         } catch (var9: java.lang.Throwable) {
            val `$i$f$genericType`: Companion = Result.Companion;
            var5 = Result.constructor-impl(ResultKt.createFailure(var9));
         }

         return (if (Result.isFailure-impl(var5)) null else var5) as MutableMap<java.lang.String, java.lang.String>;
      }

      @JvmStatic
      fun putLoginInfo(`this`: BaseSource, info: java.lang.String): Boolean {
         var key: Boolean;
         try {
            val var8: ByteArray = StringsKt.encodeToByteArray$default(AppConst.INSTANCE.getUserAgent(), 0, 8, false, 4, null);
            val var10000: EncoderUtils = EncoderUtils.INSTANCE;
            val var10001: ByteArray = info.getBytes(Charsets.UTF_8);
            val encodeStr: java.lang.String = Base64.encodeToString(EncoderUtils.encryptAES$default(var10000, var10001, var8, null, null, 12, null), 0);
            val var9: CacheManager = new CacheManager(this.getUserNameSpace());
            val var10: java.lang.String = Intrinsics.stringPlus("userInfo_", this.getKey());
            CacheManager.put$default(var9, var10, encodeStr, 0, 4, null);
            key = true;
         } catch (var7: Exception) {
            this.log(Intrinsics.stringPlus("保存登陆信息出错 ", var7.getLocalizedMessage()));
            key = false;
         }

         return key;
      }

      @JvmStatic
      fun removeLoginInfo(`this`: BaseSource) {
         new CacheManager(this.getUserNameSpace()).delete(Intrinsics.stringPlus("userInfo_", this.getKey()));
      }

      @JvmStatic
      fun setVariable(`this`: BaseSource, variable: java.lang.String?) {
         val cacheInstance: CacheManager = new CacheManager(this.getUserNameSpace());
         if (variable != null) {
            CacheManager.put$default(cacheInstance, Intrinsics.stringPlus("sourceVariable_", this.getKey()), variable, 0, 4, null);
         } else {
            cacheInstance.delete(Intrinsics.stringPlus("sourceVariable_", this.getKey()));
         }
      }

      @JvmStatic
      fun getVariable(`this`: BaseSource): java.lang.String? {
         return new CacheManager(this.getUserNameSpace()).get(Intrinsics.stringPlus("sourceVariable_", this.getKey()));
      }

      @Throws(java/lang/Exception::class)
      @JvmStatic
      fun evalJS(`this`: BaseSource, jsStr: java.lang.String, bindingsConfig: (SimpleBindings?) -> Unit): Any? {
         val bindings: SimpleBindings = new SimpleBindings();
         bindingsConfig.invoke(bindings);
         bindings.put("java", this);
         bindings.put("source", this);
         bindings.put("baseUrl", this.getKey());
         bindings.put("cookie", new CookieStore(this.getUserNameSpace()));
         bindings.put("cache", new CacheManager(this.getUserNameSpace()));
         return AppConst.INSTANCE.getSCRIPT_ENGINE().eval(jsStr, bindings);
      }

      @JvmStatic
      fun aesBase64DecodeToByteArray(`this`: BaseSource, str: java.lang.String, key: java.lang.String, transformation: java.lang.String, iv: java.lang.String): ByteArray? {
         return JsExtensions.DefaultImpls.aesBase64DecodeToByteArray(this, str, key, transformation, iv);
      }

      @JvmStatic
      fun aesBase64DecodeToString(`this`: BaseSource, str: java.lang.String, key: java.lang.String, transformation: java.lang.String, iv: java.lang.String): java.lang.String? {
         return JsExtensions.DefaultImpls.aesBase64DecodeToString(this, str, key, transformation, iv);
      }

      @JvmStatic
      fun aesDecodeArgsBase64Str(
         `this`: BaseSource, data: java.lang.String, key: java.lang.String, mode: java.lang.String, padding: java.lang.String, iv: java.lang.String
      ): java.lang.String? {
         return JsExtensions.DefaultImpls.aesDecodeArgsBase64Str(this, data, key, mode, padding, iv);
      }

      @JvmStatic
      fun aesDecodeToByteArray(`this`: BaseSource, str: java.lang.String, key: java.lang.String, transformation: java.lang.String, iv: java.lang.String): ByteArray? {
         return JsExtensions.DefaultImpls.aesDecodeToByteArray(this, str, key, transformation, iv);
      }

      @JvmStatic
      fun aesDecodeToString(`this`: BaseSource, str: java.lang.String, key: java.lang.String, transformation: java.lang.String, iv: java.lang.String): java.lang.String? {
         return JsExtensions.DefaultImpls.aesDecodeToString(this, str, key, transformation, iv);
      }

      @JvmStatic
      fun aesEncodeArgsBase64Str(
         `this`: BaseSource, data: java.lang.String, key: java.lang.String, mode: java.lang.String, padding: java.lang.String, iv: java.lang.String
      ): java.lang.String? {
         return JsExtensions.DefaultImpls.aesEncodeArgsBase64Str(this, data, key, mode, padding, iv);
      }

      @JvmStatic
      fun aesEncodeToBase64ByteArray(`this`: BaseSource, data: java.lang.String, key: java.lang.String, transformation: java.lang.String, iv: java.lang.String): ByteArray? {
         return JsExtensions.DefaultImpls.aesEncodeToBase64ByteArray(this, data, key, transformation, iv);
      }

      @JvmStatic
      fun aesEncodeToBase64String(`this`: BaseSource, data: java.lang.String, key: java.lang.String, transformation: java.lang.String, iv: java.lang.String): java.lang.String? {
         return JsExtensions.DefaultImpls.aesEncodeToBase64String(this, data, key, transformation, iv);
      }

      @JvmStatic
      fun aesEncodeToByteArray(`this`: BaseSource, data: java.lang.String, key: java.lang.String, transformation: java.lang.String, iv: java.lang.String): ByteArray? {
         return JsExtensions.DefaultImpls.aesEncodeToByteArray(this, data, key, transformation, iv);
      }

      @JvmStatic
      fun aesEncodeToString(`this`: BaseSource, data: java.lang.String, key: java.lang.String, transformation: java.lang.String, iv: java.lang.String): java.lang.String? {
         return JsExtensions.DefaultImpls.aesEncodeToString(this, data, key, transformation, iv);
      }

      @JvmStatic
      fun ajax(`this`: BaseSource, urlStr: java.lang.String): java.lang.String? {
         return JsExtensions.DefaultImpls.ajax(this, urlStr);
      }

      @JvmStatic
      fun ajaxAll(`this`: BaseSource, urlList: Array<java.lang.String>): Array<StrResponse> {
         return JsExtensions.DefaultImpls.ajaxAll(this, urlList);
      }

      @JvmStatic
      fun androidId(`this`: BaseSource): java.lang.String {
         return JsExtensions.DefaultImpls.androidId(this);
      }

      @JvmStatic
      fun base64Decode(`this`: BaseSource, str: java.lang.String): java.lang.String {
         return JsExtensions.DefaultImpls.base64Decode(this, str);
      }

      @JvmStatic
      fun base64Decode(`this`: BaseSource, str: java.lang.String, flags: Int): java.lang.String {
         return JsExtensions.DefaultImpls.base64Decode(this, str, flags);
      }

      @JvmStatic
      fun base64DecodeToByteArray(`this`: BaseSource, str: java.lang.String?): ByteArray? {
         return JsExtensions.DefaultImpls.base64DecodeToByteArray(this, str);
      }

      @JvmStatic
      fun base64DecodeToByteArray(`this`: BaseSource, str: java.lang.String?, flags: Int): ByteArray? {
         return JsExtensions.DefaultImpls.base64DecodeToByteArray(this, str, flags);
      }

      @JvmStatic
      fun base64Encode(`this`: BaseSource, str: java.lang.String): java.lang.String? {
         return JsExtensions.DefaultImpls.base64Encode(this, str);
      }

      @JvmStatic
      fun base64Encode(`this`: BaseSource, str: java.lang.String, flags: Int): java.lang.String? {
         return JsExtensions.DefaultImpls.base64Encode(this, str, flags);
      }

      @JvmStatic
      fun cacheFile(`this`: BaseSource, urlStr: java.lang.String): java.lang.String? {
         return JsExtensions.DefaultImpls.cacheFile(this, urlStr);
      }

      @JvmStatic
      fun cacheFile(`this`: BaseSource, urlStr: java.lang.String, saveTime: Int): java.lang.String? {
         return JsExtensions.DefaultImpls.cacheFile(this, urlStr, saveTime);
      }

      @JvmStatic
      fun connect(`this`: BaseSource, urlStr: java.lang.String): StrResponse {
         return JsExtensions.DefaultImpls.connect(this, urlStr);
      }

      @JvmStatic
      fun connect(`this`: BaseSource, urlStr: java.lang.String, header: java.lang.String?): StrResponse {
         return JsExtensions.DefaultImpls.connect(this, urlStr, header);
      }

      @JvmStatic
      fun deleteFile(`this`: BaseSource, path: java.lang.String) {
         JsExtensions.DefaultImpls.deleteFile(this, path);
      }

      @JvmStatic
      fun desBase64DecodeToString(`this`: BaseSource, data: java.lang.String, key: java.lang.String, transformation: java.lang.String, iv: java.lang.String): java.lang.String? {
         return JsExtensions.DefaultImpls.desBase64DecodeToString(this, data, key, transformation, iv);
      }

      @JvmStatic
      fun desDecodeToString(`this`: BaseSource, data: java.lang.String, key: java.lang.String, transformation: java.lang.String, iv: java.lang.String): java.lang.String? {
         return JsExtensions.DefaultImpls.desDecodeToString(this, data, key, transformation, iv);
      }

      @JvmStatic
      fun desEncodeToBase64String(`this`: BaseSource, data: java.lang.String, key: java.lang.String, transformation: java.lang.String, iv: java.lang.String): java.lang.String? {
         return JsExtensions.DefaultImpls.desEncodeToBase64String(this, data, key, transformation, iv);
      }

      @JvmStatic
      fun desEncodeToString(`this`: BaseSource, data: java.lang.String, key: java.lang.String, transformation: java.lang.String, iv: java.lang.String): java.lang.String? {
         return JsExtensions.DefaultImpls.desEncodeToString(this, data, key, transformation, iv);
      }

      @JvmStatic
      fun digestBase64Str(`this`: BaseSource, data: java.lang.String, algorithm: java.lang.String): java.lang.String? {
         return JsExtensions.DefaultImpls.digestBase64Str(this, data, algorithm);
      }

      @JvmStatic
      fun digestHex(`this`: BaseSource, data: java.lang.String, algorithm: java.lang.String): java.lang.String? {
         return JsExtensions.DefaultImpls.digestHex(this, data, algorithm);
      }

      @JvmStatic
      fun downloadFile(`this`: BaseSource, content: java.lang.String, url: java.lang.String): java.lang.String {
         return JsExtensions.DefaultImpls.downloadFile(this, content, url);
      }

      @JvmStatic
      fun encodeURI(`this`: BaseSource, str: java.lang.String): java.lang.String {
         return JsExtensions.DefaultImpls.encodeURI(this, str);
      }

      @JvmStatic
      fun encodeURI(`this`: BaseSource, str: java.lang.String, enc: java.lang.String): java.lang.String {
         return JsExtensions.DefaultImpls.encodeURI(this, str, enc);
      }

      @JvmStatic
      fun get(`this`: BaseSource, urlStr: java.lang.String, headers: MutableMap<java.lang.String, java.lang.String>): Response {
         return JsExtensions.DefaultImpls.get(this, urlStr, headers);
      }

      @JvmStatic
      fun getCookie(`this`: BaseSource, tag: java.lang.String, key: java.lang.String?): java.lang.String {
         return JsExtensions.DefaultImpls.getCookie(this, tag, key);
      }

      @JvmStatic
      fun getFile(`this`: BaseSource, path: java.lang.String): File {
         return JsExtensions.DefaultImpls.getFile(this, path);
      }

      @JvmStatic
      fun getTxtInFolder(`this`: BaseSource, unzipPath: java.lang.String): java.lang.String {
         return JsExtensions.DefaultImpls.getTxtInFolder(this, unzipPath);
      }

      @JvmStatic
      fun getZipByteArrayContent(`this`: BaseSource, url: java.lang.String, path: java.lang.String): ByteArray? {
         return JsExtensions.DefaultImpls.getZipByteArrayContent(this, url, path);
      }

      @JvmStatic
      fun getZipStringContent(`this`: BaseSource, url: java.lang.String, path: java.lang.String): java.lang.String {
         return JsExtensions.DefaultImpls.getZipStringContent(this, url, path);
      }

      @JvmStatic
      fun getZipStringContent(`this`: BaseSource, url: java.lang.String, path: java.lang.String, charsetName: java.lang.String): java.lang.String {
         return JsExtensions.DefaultImpls.getZipStringContent(this, url, path, charsetName);
      }

      @JvmStatic
      fun head(`this`: BaseSource, urlStr: java.lang.String, headers: MutableMap<java.lang.String, java.lang.String>): Response {
         return JsExtensions.DefaultImpls.head(this, urlStr, headers);
      }

      @JvmStatic
      fun htmlFormat(`this`: BaseSource, str: java.lang.String): java.lang.String {
         return JsExtensions.DefaultImpls.htmlFormat(this, str);
      }

      @JvmStatic
      fun importScript(`this`: BaseSource, path: java.lang.String): java.lang.String {
         return JsExtensions.DefaultImpls.importScript(this, path);
      }

      @JvmStatic
      fun log(`this`: BaseSource, msg: java.lang.String): java.lang.String {
         return JsExtensions.DefaultImpls.log(this, msg);
      }

      @JvmStatic
      fun logType(`this`: BaseSource, any: Any?) {
         JsExtensions.DefaultImpls.logType(this, any);
      }

      @JvmStatic
      fun longToast(`this`: BaseSource, msg: Any?) {
         JsExtensions.DefaultImpls.longToast(this, msg);
      }

      @JvmStatic
      fun md5Encode(`this`: BaseSource, str: java.lang.String): java.lang.String {
         return JsExtensions.DefaultImpls.md5Encode(this, str);
      }

      @JvmStatic
      fun md5Encode16(`this`: BaseSource, str: java.lang.String): java.lang.String {
         return JsExtensions.DefaultImpls.md5Encode16(this, str);
      }

      @JvmStatic
      fun post(`this`: BaseSource, urlStr: java.lang.String, body: java.lang.String, headers: MutableMap<java.lang.String, java.lang.String>): Response {
         return JsExtensions.DefaultImpls.post(this, urlStr, body, headers);
      }

      @JvmStatic
      fun queryBase64TTF(`this`: BaseSource, base64: java.lang.String?): QueryTTF? {
         return JsExtensions.DefaultImpls.queryBase64TTF(this, base64);
      }

      @JvmStatic
      fun queryTTF(`this`: BaseSource, str: java.lang.String?): QueryTTF? {
         return JsExtensions.DefaultImpls.queryTTF(this, str);
      }

      @JvmStatic
      fun randomUUID(`this`: BaseSource): java.lang.String {
         return JsExtensions.DefaultImpls.randomUUID(this);
      }

      @JvmStatic
      fun readFile(`this`: BaseSource, path: java.lang.String): ByteArray? {
         return JsExtensions.DefaultImpls.readFile(this, path);
      }

      @JvmStatic
      fun readTxtFile(`this`: BaseSource, path: java.lang.String): java.lang.String {
         return JsExtensions.DefaultImpls.readTxtFile(this, path);
      }

      @JvmStatic
      fun readTxtFile(`this`: BaseSource, path: java.lang.String, charsetName: java.lang.String): java.lang.String {
         return JsExtensions.DefaultImpls.readTxtFile(this, path, charsetName);
      }

      @JvmStatic
      fun replaceFont(`this`: BaseSource, text: java.lang.String, font1: QueryTTF?, font2: QueryTTF?): java.lang.String {
         return JsExtensions.DefaultImpls.replaceFont(this, text, font1, font2);
      }

      @JvmStatic
      fun timeFormat(`this`: BaseSource, time: Long): java.lang.String {
         return JsExtensions.DefaultImpls.timeFormat(this, time);
      }

      @JvmStatic
      fun timeFormatUTC(`this`: BaseSource, time: Long, format: java.lang.String, sh: Int): java.lang.String? {
         return JsExtensions.DefaultImpls.timeFormatUTC(this, time, format, sh);
      }

      @JvmStatic
      fun toast(`this`: BaseSource, msg: Any?) {
         JsExtensions.DefaultImpls.toast(this, msg);
      }

      @JvmStatic
      fun tripleDESDecodeArgsBase64Str(
         `this`: BaseSource, data: java.lang.String, key: java.lang.String, mode: java.lang.String, padding: java.lang.String, iv: java.lang.String
      ): java.lang.String? {
         return JsExtensions.DefaultImpls.tripleDESDecodeArgsBase64Str(this, data, key, mode, padding, iv);
      }

      @JvmStatic
      fun tripleDESDecodeStr(
         `this`: BaseSource, data: java.lang.String, key: java.lang.String, mode: java.lang.String, padding: java.lang.String, iv: java.lang.String
      ): java.lang.String? {
         return JsExtensions.DefaultImpls.tripleDESDecodeStr(this, data, key, mode, padding, iv);
      }

      @JvmStatic
      fun tripleDESEncodeArgsBase64Str(
         `this`: BaseSource, data: java.lang.String, key: java.lang.String, mode: java.lang.String, padding: java.lang.String, iv: java.lang.String
      ): java.lang.String? {
         return JsExtensions.DefaultImpls.tripleDESEncodeArgsBase64Str(this, data, key, mode, padding, iv);
      }

      @JvmStatic
      fun tripleDESEncodeBase64Str(
         `this`: BaseSource, data: java.lang.String, key: java.lang.String, mode: java.lang.String, padding: java.lang.String, iv: java.lang.String
      ): java.lang.String? {
         return JsExtensions.DefaultImpls.tripleDESEncodeBase64Str(this, data, key, mode, padding, iv);
      }

      @JvmStatic
      fun unzipFile(`this`: BaseSource, zipPath: java.lang.String): java.lang.String {
         return JsExtensions.DefaultImpls.unzipFile(this, zipPath);
      }

      @JvmStatic
      fun utf8ToGbk(`this`: BaseSource, str: java.lang.String): java.lang.String {
         return JsExtensions.DefaultImpls.utf8ToGbk(this, str);
      }

      @JvmStatic
      fun webView(`this`: BaseSource, html: java.lang.String?, url: java.lang.String?, js: java.lang.String?): java.lang.String? {
         return JsExtensions.DefaultImpls.webView(this, html, url, js);
      }
   }
}
