package io.legado.app.help

import cn.hutool.crypto.symmetric.AES
import io.legado.app.data.entities.BaseSource
import io.legado.app.data.entities.BookSource
import io.legado.app.model.DebugLog
import io.legado.app.model.analyzeRule.AnalyzeRule
import io.legado.app.model.analyzeRule.AnalyzeUrl
import io.vertx.core.json.JsonArray
import io.vertx.core.json.JsonObject
import java.nio.charset.StandardCharsets
import java.util.Base64

/**
 * Book source login: loginUrl / loginUi / loginCheckJs / loginHeader / loginInfo.
 *
 * loginUi: JSON array of form fields, e.g.
 * ```
 * [
 *   {"name":"username","type":"text","hint":"用户名"},
 *   {"name":"password","type":"password","hint":"密码"}
 * ]
 * ```
 */
object SourceLogin {

    data class LoginField(
        val name: String,
        val type: String = "text",
        val hint: String = "",
        val value: String = ""
    )

    private const val UA_KEY = "Mozilla/5.0 ReaderProRebuild"

    // ---------- loginInfo (form values) ----------

    fun getLoginInfo(source: BaseSource): String? {
        return try {
            val encoded = CacheManager(source.getUserNameSpace()).get("userInfo_${source.getKey()}")
                ?: return null
            val raw = Base64.getDecoder().decode(encoded)
            val key = UA_KEY.toByteArray(StandardCharsets.UTF_8).copyOf(16)
            val aes = AES(key)
            String(aes.decrypt(raw), StandardCharsets.UTF_8)
        } catch (_: Exception) {
            // plain fallback
            CacheManager(source.getUserNameSpace()).get("userInfo_plain_${source.getKey()}")
        }
    }

    fun getLoginInfoMap(source: BaseSource): Map<String, String> {
        val raw = getLoginInfo(source) ?: return emptyMap()
        return try {
            val o = JsonObject(raw)
            o.map.mapValues { it.value?.toString() ?: "" }
        } catch (_: Exception) {
            emptyMap()
        }
    }

    fun putLoginInfo(source: BaseSource, infoJson: String): Boolean {
        return try {
            val key = UA_KEY.toByteArray(StandardCharsets.UTF_8).copyOf(16)
            val aes = AES(key)
            val enc = Base64.getEncoder().encodeToString(aes.encrypt(infoJson.toByteArray(StandardCharsets.UTF_8)))
            CacheManager(source.getUserNameSpace()).put("userInfo_${source.getKey()}", enc)
            CacheManager(source.getUserNameSpace()).put("userInfo_plain_${source.getKey()}", infoJson)
            true
        } catch (_: Exception) {
            CacheManager(source.getUserNameSpace()).put("userInfo_plain_${source.getKey()}", infoJson)
            true
        }
    }

    fun removeLoginInfo(source: BaseSource) {
        val cm = CacheManager(source.getUserNameSpace())
        cm.delete("userInfo_${source.getKey()}")
        cm.delete("userInfo_plain_${source.getKey()}")
    }

    // ---------- loginUi parse ----------

    fun parseLoginUi(loginUi: String?): List<LoginField> {
        if (loginUi.isNullOrBlank()) {
            return listOf(
                LoginField("username", "text", "用户名"),
                LoginField("password", "password", "密码")
            )
        }
        val trimmed = loginUi.trim()
        // "user,pass" shortcut
        if (!trimmed.startsWith("[") && !trimmed.startsWith("{")) {
            return trimmed.split(',', '，', ';').mapNotNull { part ->
                val name = part.trim()
                if (name.isEmpty()) null
                else LoginField(
                    name = name,
                    type = if (name.contains("pass", true) || name.contains("pwd", true)) "password" else "text",
                    hint = name
                )
            }
        }
        return try {
            when {
                trimmed.startsWith("[") -> {
                    val arr = JsonArray(trimmed)
                    (0 until arr.size()).mapNotNull { i ->
                        val v = arr.getValue(i)
                        when (v) {
                            is JsonObject -> fieldFromObj(v)
                            is String -> LoginField(v, hint = v)
                            else -> null
                        }
                    }
                }
                else -> {
                    val o = JsonObject(trimmed)
                    when {
                        o.containsKey("rows") -> {
                            val rows = o.getJsonArray("rows") ?: JsonArray()
                            (0 until rows.size()).flatMap { ri ->
                                val row = rows.getJsonObject(ri) ?: return@flatMap emptyList()
                                // row may be array of fields or single field
                                if (row.containsKey("name")) listOfNotNull(fieldFromObj(row))
                                else {
                                    val fields = row.getJsonArray("fields") ?: JsonArray()
                                    (0 until fields.size()).mapNotNull { fi ->
                                        fields.getJsonObject(fi)?.let { fieldFromObj(it) }
                                    }
                                }
                            }
                        }
                        o.containsKey("name") -> listOfNotNull(fieldFromObj(o))
                        else -> o.map.keys.map { LoginField(it, hint = it) }
                    }
                }
            }
        } catch (_: Exception) {
            listOf(
                LoginField("username", "text", "用户名"),
                LoginField("password", "password", "密码")
            )
        }
    }

    private fun fieldFromObj(o: JsonObject): LoginField? {
        val name = o.getString("name") ?: o.getString("id") ?: return null
        val type = o.getString("type") ?: if (name.contains("pass", true)) "password" else "text"
        val hint = o.getString("hint") ?: o.getString("title") ?: o.getString("label") ?: name
        val value = o.getString("value") ?: ""
        return LoginField(name, type, hint, value)
    }

    fun getLoginUiPayload(source: BookSource): Map<String, Any?> {
        val fields = parseLoginUi(source.getLoginUi())
        val saved = getLoginInfoMap(source)
        val filled = fields.map { f ->
            mapOf(
                "name" to f.name,
                "type" to f.type,
                "hint" to f.hint,
                "value" to (saved[f.name] ?: f.value)
            )
        }
        return mapOf(
            "bookSourceUrl" to source.bookSourceUrl,
            "bookSourceName" to source.bookSourceName,
            "loginUrl" to source.getLoginUrl(),
            "hasLoginHeader" to (source.getLoginHeader() != null),
            "fields" to filled
        )
    }

    /**
     * Save form values then run loginUrl JS.
     * JS may read via source.getLoginInfo() / getLoginInfoMap if bound, or we inject `loginInfo` binding.
     */
    fun loginWithForm(
        source: BookSource,
        form: Map<String, String>,
        debugLog: DebugLog? = null
    ): Map<String, Any?> {
        val info = JsonObject()
        form.forEach { (k, v) -> info.put(k, v) }
        putLoginInfo(source, info.encode())
        val ok = login(source, debugLog, form)
        return mapOf(
            "ok" to (ok || source.getLoginHeader() != null),
            "loginHeader" to source.getLoginHeader(),
            "loginInfoKeys" to form.keys.toList()
        )
    }

    fun login(source: BaseSource, debugLog: DebugLog? = null, form: Map<String, String>? = null): Boolean {
        val loginJs = source.getLoginJs() ?: return false
        val rule = AnalyzeRule(null, source, debugLog)
        // Bind form as result for scripts that use `result.username`
        val bind = form ?: getLoginInfoMap(source)
        val result = rule.evalJS(loginJs, bind)
        when (result) {
            is String -> if (result.isNotBlank() &&
                (result.trimStart().startsWith("{") || result.contains(":"))
            ) {
                source.putLoginHeader(result)
            }
            is Map<*, *> -> {
                val o = JsonObject()
                result.forEach { (k, v) -> if (k != null) o.put(k.toString(), v?.toString() ?: "") }
                source.putLoginHeader(o.encode())
            }
        }
        return source.getLoginHeader() != null
    }

    fun BaseSource.getLoginJs(): String? {
        val loginJs = getLoginUrl() ?: return null
        return when {
            loginJs.startsWith("@js:", ignoreCase = true) -> loginJs.substring(4)
            loginJs.startsWith("<js>", ignoreCase = true) -> {
                val end = loginJs.lastIndexOf('<')
                if (end > 4) loginJs.substring(4, end) else loginJs.removePrefix("<js>").removeSuffix("</js>")
            }
            loginJs.startsWith("http://") || loginJs.startsWith("https://") -> null
            else -> loginJs
        }
    }

    fun checkLogin(
        source: BaseSource,
        responseBody: String?,
        baseUrl: String?,
        debugLog: DebugLog? = null
    ): Any? {
        val js = source.getLoginCheckJs() ?: return responseBody
        if (js.isBlank()) return responseBody
        val rule = AnalyzeRule(null, source, debugLog)
        rule.setContent(responseBody ?: "", baseUrl)
        return rule.evalJS(js, responseBody)
    }

    suspend fun ensureLoginIfNeeded(source: BookSource, debugLog: DebugLog? = null) {
        if (source.getLoginHeader() != null) return
        val loginUrl = source.getLoginUrl() ?: return
        if (loginUrl.startsWith("http://") || loginUrl.startsWith("https://")) {
            runCatching {
                val body = AnalyzeUrl(mUrl = loginUrl, source = source, debugLog = debugLog)
                    .getStrResponseAwait().body
                checkLogin(source, body, loginUrl, debugLog)
            }
        } else {
            login(source, debugLog)
        }
    }

    fun clearLogin(source: BaseSource) {
        CacheManager(source.getUserNameSpace()).delete("loginHeader_${source.getKey()}")
        removeLoginInfo(source)
    }
}
