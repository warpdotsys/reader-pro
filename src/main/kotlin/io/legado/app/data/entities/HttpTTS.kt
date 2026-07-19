package io.legado.app.data.entities

import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import com.jayway.jsonpath.DocumentContext
import io.legado.app.model.DebugLog
import io.legado.app.utils.GSON
import io.legado.app.utils.jsonPath
import io.legado.app.utils.readLong
import io.legado.app.utils.readString

@JsonIgnoreProperties("headerMap", "source", "_userNameSpace", "userNameSpace")
data class HttpTTS(
    val id: Long = System.currentTimeMillis(),
    var name: String = "",
    var url: String = "",
    var contentType: String? = null,
    override var concurrentRate: String? = "0",
    override var loginUrl: String? = null,
    override var loginUi: String? = null,
    override var header: String? = null,
    var jsLib: String? = null,
    override var enabledCookieJar: Boolean? = false,
    var loginCheckJs: String? = null,
    var lastUpdateTime: Long = System.currentTimeMillis()
) : BaseSource {

    @Transient
    private var _userNameSpace: String = ""

    fun setUserNameSpace(nameSpace: String) {
        _userNameSpace = nameSpace
    }

    override fun getUserNameSpace(): String = _userNameSpace

    @Transient
    private var debugLog: DebugLog? = null

    fun setLogger(logger: DebugLog?) {
        debugLog = logger
    }

    override fun getLogger(): DebugLog? = debugLog

    override fun getTag(): String = name

    override fun getKey(): String = "httpTts:$id"

    companion object {
        fun fromJsonDoc(doc: DocumentContext): Result<HttpTTS> = kotlin.runCatching {
            val loginUi = doc.read<Any?>("$.loginUi")
            HttpTTS(
                id = doc.readLong("$.id") ?: System.currentTimeMillis(),
                name = doc.readString("$.name")!!,
                url = doc.readString("$.url")!!,
                contentType = doc.readString("$.contentType"),
                concurrentRate = doc.readString("$.concurrentRate"),
                loginUrl = doc.readString("$.loginUrl"),
                loginUi = if (loginUi is List<*>) GSON.toJson(loginUi) else loginUi?.toString(),
                header = doc.readString("$.header"),
                loginCheckJs = doc.readString("$.loginCheckJs")
            )
        }

        fun fromJson(json: String): Result<HttpTTS> = fromJsonDoc(jsonPath.parse(json))

        fun fromJsonArray(jsonArray: String): Result<ArrayList<HttpTTS>> = kotlin.runCatching {
            val sources = arrayListOf<HttpTTS>()
            val doc = jsonPath.parse(jsonArray).read<List<Any>>("$")
            doc.forEach {
                val jsonItem = jsonPath.parse(it)
                val source = fromJsonDoc(jsonItem).getOrThrow()
                sources.add(source)
            }
            sources
        }
    }
}
