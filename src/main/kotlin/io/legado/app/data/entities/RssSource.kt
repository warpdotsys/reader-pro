package io.legado.app.data.entities

import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import com.jayway.jsonpath.DocumentContext
import io.legado.app.model.DebugLog
import io.legado.app.utils.jsonPath
import io.legado.app.utils.readBool
import io.legado.app.utils.readInt
import io.legado.app.utils.readString

@JsonIgnoreProperties(
    "headerMap",
    "source",
    "_userNameSpace",
    "userNameSpace",
    "loginHeader",
    "loginHeaderMap",
    "loginInfo",
    "loginInfoMap"
)
data class RssSource(
    var sourceUrl: String = "",
    var sourceName: String = "",
    var sourceIcon: String = "",
    var sourceGroup: String? = null,
    var sourceComment: String? = null,
    var enabled: Boolean = true,
    var variableComment: String? = null,
    override var enabledCookieJar: Boolean? = false,
    override var concurrentRate: String? = null,
    override var header: String? = null,
    override var loginUrl: String? = null,
    override var loginUi: String? = null,
    var loginCheckJs: String? = null,
    var sortUrl: String? = null,
    var singleUrl: Boolean = false,
    var articleStyle: Int = 0,
    var ruleArticles: String? = null,
    var ruleNextPage: String? = null,
    var ruleTitle: String? = null,
    var rulePubDate: String? = null,
    var ruleDescription: String? = null,
    var ruleImage: String? = null,
    var ruleLink: String? = null,
    var ruleContent: String? = null,
    var style: String? = null,
    var enableJs: Boolean = true,
    var loadWithBaseUrl: Boolean = true,
    var customOrder: Int = 0
) : BaseSource {

    override fun getTag(): String = sourceName

    override fun getKey(): String = sourceUrl

    override fun equals(other: Any?) =
        other is RssSource && other.sourceUrl == sourceUrl

    override fun hashCode(): Int = sourceUrl.hashCode()

    fun equal(source: RssSource): Boolean {
        return equal(sourceUrl, source.sourceUrl) &&
            equal(sourceIcon, source.sourceIcon) &&
            enabled == source.enabled &&
            enabledCookieJar == source.enabledCookieJar &&
            equal(sourceComment, source.sourceComment) &&
            equal(sourceGroup, source.sourceGroup) &&
            equal(ruleArticles, source.ruleArticles) &&
            equal(ruleNextPage, source.ruleNextPage) &&
            equal(ruleTitle, source.ruleTitle) &&
            equal(rulePubDate, source.rulePubDate) &&
            equal(ruleDescription, source.ruleDescription) &&
            equal(ruleLink, source.ruleLink) &&
            equal(ruleContent, source.ruleContent) &&
            enableJs == source.enableJs &&
            loadWithBaseUrl == source.loadWithBaseUrl
    }

    private fun equal(a: String?, b: String?): Boolean {
        return a == b || (a.isNullOrEmpty() && b.isNullOrEmpty())
    }

    fun sortUrls(): List<Pair<String, String>> = arrayListOf<Pair<String, String>>().apply {
        kotlin.runCatching {
            var value = sortUrl
            if (sortUrl?.startsWith("<js>", false) == true ||
                sortUrl?.startsWith("@js:", false) == true
            ) {
                val jsStr = if (sortUrl!!.startsWith("@")) {
                    sortUrl!!.substring(4)
                } else {
                    sortUrl!!.substring(4, sortUrl!!.lastIndexOf("<"))
                }
                value = evalJS(jsStr).toString()
            }
            value?.split("(&&|\n)+".toRegex())?.forEach { entry ->
                val parts = entry.split("::")
                if (parts.size > 1) {
                    add(Pair(parts[0], parts[1]))
                }
            }
            if (isEmpty()) {
                add(Pair("", sourceUrl))
            }
        }
    }

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

    override fun getLogger(): DebugLog? = null

    companion object {
        fun fromJsonDoc(doc: DocumentContext): Result<RssSource> = kotlin.runCatching {
            RssSource(
                sourceUrl = doc.readString("$.sourceUrl")!!,
                sourceName = doc.readString("$.sourceName")!!,
                sourceIcon = doc.readString("$.sourceIcon") ?: "",
                sourceGroup = doc.readString("$.sourceGroup"),
                sourceComment = doc.readString("$.sourceComment"),
                enabled = doc.readBool("$.enabled") ?: true,
                enabledCookieJar = doc.readBool("$.enabledCookieJar") ?: false,
                concurrentRate = doc.readString("$.concurrentRate"),
                header = doc.readString("$.header"),
                loginUrl = doc.readString("$.loginUrl"),
                loginCheckJs = doc.readString("$.loginCheckJs"),
                sortUrl = doc.readString("$.sortUrl"),
                singleUrl = doc.readBool("$.singleUrl") ?: false,
                articleStyle = doc.readInt("$.articleStyle") ?: 0,
                ruleArticles = doc.readString("$.ruleArticles"),
                ruleNextPage = doc.readString("$.ruleNextPage"),
                ruleTitle = doc.readString("$.ruleTitle"),
                rulePubDate = doc.readString("$.rulePubDate"),
                ruleDescription = doc.readString("$.ruleDescription"),
                ruleImage = doc.readString("$.ruleImage"),
                ruleLink = doc.readString("$.ruleLink"),
                ruleContent = doc.readString("$.ruleContent"),
                style = doc.readString("$.style"),
                enableJs = doc.readBool("$.enableJs") ?: true,
                loadWithBaseUrl = doc.readBool("$.loadWithBaseUrl") ?: true,
                customOrder = doc.readInt("$.customOrder") ?: 0
            )
        }

        fun fromJson(json: String): Result<RssSource> = fromJsonDoc(jsonPath.parse(json))

        fun fromJsonArray(jsonArray: String): Result<ArrayList<RssSource>> = kotlin.runCatching {
            val sources = arrayListOf<RssSource>()
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
