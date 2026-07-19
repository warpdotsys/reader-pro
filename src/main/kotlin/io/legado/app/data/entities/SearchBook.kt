package io.legado.app.data.entities

import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import io.legado.app.utils.GSON
import io.legado.app.utils.fromJsonObject

@JsonIgnoreProperties("variableMap", "infoHtml", "tocHtml", "origins", "kindList")
data class SearchBook(
    override var bookUrl: String = "",
    var origin: String = "",
    var originName: String = "",
    var type: Int = 0,
    override var name: String = "",
    override var author: String = "",
    override var kind: String? = null,
    var coverUrl: String? = null,
    var intro: String? = null,
    override var wordCount: String? = null,
    var latestChapterTitle: String? = null,
    var tocUrl: String = "",
    var time: Long = 0L,
    var variable: String? = null,
    var originOrder: Int = 0
) : BaseBook, Comparable<SearchBook> {

    override fun equals(other: Any?): Boolean {
        return other is SearchBook && other.bookUrl == bookUrl
    }

    override fun hashCode(): Int {
        return bookUrl.hashCode()
    }

    @delegate:Transient
    override val variableMap: HashMap<String, String> by lazy {
        GSON.fromJsonObject<HashMap<String, String>>(variable).getOrNull() ?: hashMapOf()
    }

    override fun putVariable(key: String, value: String?) {
        if (value != null) {
            variableMap[key] = value
        } else {
            variableMap.remove(key)
        }
        variable = GSON.toJson(variableMap)
    }

    override var infoHtml: String? = null

    override var tocHtml: String? = null

    @Transient
    private var _userNameSpace: String = ""

    fun setUserNameSpace(nameSpace: String) {
        _userNameSpace = nameSpace
    }

    override fun getUserNameSpace(): String {
        return _userNameSpace
    }

    var origins: LinkedHashSet<String>? = null
        private set

    fun addOrigin(origin: String) {
        if (origins == null) {
            origins = linkedSetOf(this.origin)
        }
        origins?.add(origin)
    }

    override fun compareTo(other: SearchBook): Int = other.originOrder - this.originOrder

    fun toBook(): Book {
        return Book(
            bookUrl = bookUrl,
            tocUrl = tocUrl,
            origin = origin,
            originName = originName,
            name = name,
            author = author,
            kind = kind,
            coverUrl = coverUrl,
            intro = intro,
            type = type,
            latestChapterTitle = latestChapterTitle,
            wordCount = wordCount,
            variable = variable
        ).apply {
            this.infoHtml = this@SearchBook.infoHtml
            this.tocUrl = this@SearchBook.tocUrl
            this.setUserNameSpace(this@SearchBook.getUserNameSpace())
        }
    }
}
