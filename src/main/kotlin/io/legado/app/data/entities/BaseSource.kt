package io.legado.app.data.entities

import com.script.SimpleBindings
import io.legado.app.constant.AppConst
import io.legado.app.help.CacheManager
import io.legado.app.help.JsExtensions
import io.legado.app.help.http.CookieStore
import io.legado.app.utils.Base64
import io.legado.app.utils.EncoderUtils
import io.legado.app.utils.GSON
import io.legado.app.utils.fromJsonObject

interface BaseSource : JsExtensions {
    var concurrentRate: String?
    var loginUrl: String?
    var loginUi: String?
    var header: String?
    var enabledCookieJar: Boolean?

    fun getTag(): String

    fun getKey(): String

    override fun getSource(): BaseSource? {
        return this
    }

    fun getLoginJs(): String? {
        val loginJs = loginUrl
        return when {
            loginJs == null -> null
            loginJs.startsWith("@js:") -> loginJs.substring(4)
            loginJs.startsWith("<js>") -> loginJs.substring(4, loginJs.lastIndexOf("<"))
            else -> loginJs
        }
    }

    fun login() {
        getLoginJs()?.let {
            evalJS(it)
        }
    }

    fun getHeaderMap(hasLoginHeader: Boolean = false): HashMap<String, String> {
        val headerMap = HashMap<String, String>()
        headerMap["User-Agent"] = AppConst.userAgent
        header?.let {
            val json = when {
                it.startsWith("@js:", ignoreCase = true) -> evalJS(it.substring(4)).toString()
                it.startsWith("<js>", ignoreCase = true) -> evalJS(it.substring(4, it.lastIndexOf("<"))).toString()
                else -> it
            }
            val map = GSON.fromJsonObject<Map<String, String>>(json).getOrNull()
            if (map != null) {
                headerMap.putAll(map)
            }
        }
        if (hasLoginHeader) {
            getLoginHeaderMap()?.let {
                headerMap.putAll(it)
            }
        }
        return headerMap
    }

    fun getLoginHeader(): String? {
        val cacheInstance = CacheManager(getUserNameSpace())
        return cacheInstance.get("loginHeader_${getKey()}")
    }

    fun getLoginHeaderMap(): Map<String, String>? {
        val cache = getLoginHeader() ?: return null
        return GSON.fromJsonObject<Map<String, String>>(cache).getOrNull()
    }

    fun putLoginHeader(header: String) {
        val cacheInstance = CacheManager(getUserNameSpace())
        cacheInstance.put("loginHeader_${getKey()}", header)
    }

    fun removeLoginHeader() {
        val cacheInstance = CacheManager(getUserNameSpace())
        cacheInstance.delete("loginHeader_${getKey()}")
    }

    fun getLoginInfo(): String? {
        try {
            val key = AppConst.userAgent.encodeToByteArray(0, 8)
            val cacheInstance = CacheManager(getUserNameSpace())
            val cache = cacheInstance.get("userInfo_${getKey()}") ?: return null
            val encodeBytes = EncoderUtils.base64Decode(cache, 0).toByteArray(Charsets.UTF_8)
            val decodeBytes = EncoderUtils.decryptAES(encodeBytes, key) ?: return null
            return String(decodeBytes, Charsets.UTF_8)
        } catch (e: Exception) {
            log("\u83b7\u53d6\u767b\u9646\u4fe1\u606f\u51fa\u9519 ${e.localizedMessage}")
            return null
        }
    }

    fun getLoginInfoMap(): Map<String, String>? {
        return GSON.fromJsonObject<Map<String, String>>(getLoginInfo()).getOrNull()
    }

    fun putLoginInfo(info: String): Boolean {
        return try {
            val key = AppConst.userAgent.encodeToByteArray(0, 8)
            val encodeBytes = EncoderUtils.encryptAES(info.toByteArray(Charsets.UTF_8), key)
            val encodeStr = Base64.encodeToString(encodeBytes, 0)
            val cacheInstance = CacheManager(getUserNameSpace())
            cacheInstance.put("userInfo_${getKey()}", encodeStr)
            true
        } catch (e: Exception) {
            log("\u4fdd\u5b58\u767b\u9646\u4fe1\u606f\u51fa\u9519 ${e.localizedMessage}")
            false
        }
    }

    fun removeLoginInfo() {
        val cacheInstance = CacheManager(getUserNameSpace())
        cacheInstance.delete("userInfo_${getKey()}")
    }

    fun setVariable(variable: String?) {
        val cacheInstance = CacheManager(getUserNameSpace())
        if (variable != null) {
            cacheInstance.put("sourceVariable_${getKey()}", variable)
        } else {
            cacheInstance.delete("sourceVariable_${getKey()}")
        }
    }

    fun getVariable(): String? {
        val cacheInstance = CacheManager(getUserNameSpace())
        return cacheInstance.get("sourceVariable_${getKey()}")
    }

    @Throws(Exception::class)
    fun evalJS(jsStr: String, bindingsConfig: SimpleBindings.() -> Unit = {}): Any? {
        val bindings = SimpleBindings()
        bindingsConfig(bindings)
        bindings["java"] = this
        bindings["source"] = this
        bindings["baseUrl"] = getKey()
        bindings["cookie"] = CookieStore(getUserNameSpace())
        bindings["cache"] = CacheManager(getUserNameSpace())
        return AppConst.SCRIPT_ENGINE.eval(jsStr, bindings)
    }
}
