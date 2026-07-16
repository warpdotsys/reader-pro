/** Business rewrite from reader-pro-3.2.14.jar — phase8. */

package io.legado.app.data.entities

data class Book(
    var bookUrl: String = "",
    var tocUrl: String = "",
    var origin: String = "",
    var originName: String = "",
    var name: String = "",
    var author: String = "",
    var kind: String? = null,
    var coverUrl: String? = null,
    var intro: String? = null,
    var latestChapterTitle: String? = null,
    var totalChapterNum: Int = 0,
    var durChapterIndex: Int = 0,
    var durChapterPos: Int = 0,
    var durChapterTitle: String? = null,
    var durChapterTime: Long = 0,
    var canUpdate: Boolean = true,
    var isInShelf: Boolean = false,
    var lastCheckCount: Int = 0,
    var lastCheckTime: Long = 0,
    var lastCheckError: String? = null,
    var rootDir: String? = null,
    var userNameSpace: String? = null,
    var variable: String? = null,
    var charset: String? = null,
    var group: Long = 0,
    var pdfImageWidth: Float = 0f
) : BaseBook {
    val displayCover: String? get() = coverUrl
    val isLocalBook: Boolean
        get() = origin == "loc_book" || bookUrl.startsWith("file:") ||
            bookUrl.endsWith(".txt", true) || bookUrl.endsWith(".epub", true) ||
            bookUrl.endsWith(".umd", true) || bookUrl.endsWith(".cbz", true) ||
            bookUrl.endsWith(".pdf", true)
    val isEpub: Boolean get() = bookUrl.endsWith(".epub", true)
    val isCbz: Boolean get() = bookUrl.endsWith(".cbz", true)
    val isPdf: Boolean get() = bookUrl.endsWith(".pdf", true)
    val isUmd: Boolean get() = bookUrl.endsWith(".umd", true)
    val isLocalTxt: Boolean get() = bookUrl.endsWith(".txt", true) || (isLocalBook && !isEpub && !isCbz && !isPdf && !isUmd)

    private val variableMap = linkedMapOf<String, String>()
    override fun getUserNameSpace(): String = userNameSpace ?: "default"
    override fun putVariable(key: String, value: String?) {
        if (value == null) variableMap.remove(key) else variableMap[key] = value
    }
    override fun getVariable(key: String): String? = variableMap[key]
}
