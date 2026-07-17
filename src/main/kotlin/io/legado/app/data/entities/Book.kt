package io.legado.app.data.entities

data class Book(
    var bookUrl: String = "",
    var tocUrl: String = "",
    var origin: String = "",
    var originName: String = "",
    override var name: String = "",
    override var author: String = "",
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
    /** storage namespace; not the BaseBook interface method */
    var namespace: String? = null,
    var variable: String? = null,
    var charset: String? = null,
    var group: Long = 0,
    var pdfImageWidth: Float = 0f
) : BaseBook {
    private val variableMap = linkedMapOf<String, String>()

    val isLocalBook: Boolean
        get() = origin == "loc_book" || bookUrl.startsWith("file:") ||
            listOf(".txt", ".epub", ".umd", ".cbz", ".pdf").any { bookUrl.endsWith(it, true) }
    val isEpub get() = bookUrl.endsWith(".epub", true)
    val isCbz get() = bookUrl.endsWith(".cbz", true)
    val isPdf get() = bookUrl.endsWith(".pdf", true)
    val isUmd get() = bookUrl.endsWith(".umd", true)
    val isLocalTxt get() = bookUrl.endsWith(".txt", true) || (isLocalBook && !isEpub && !isCbz && !isPdf && !isUmd)

    override fun getUserNameSpace(): String = namespace ?: "default"
    override fun putVariable(key: String, value: String?) {
        if (value == null) variableMap.remove(key) else variableMap[key] = value
    }
    override fun getVariable(key: String): String? = variableMap[key]

    fun localFile(): java.io.File {
        // 原版逻辑：本地书文件 = rootDir + originName；epub/cbz/pdf 指向目录内 index.*
        var p = originName
        val root = rootDir ?: com.htmake.reader.utils.ExtKt.getWorkDir()
        if (root.isNotEmpty() && p.startsWith(root)) p = p.removePrefix(root)
        val base = java.io.File(root + p)
        if (!p.contains("localStore") && !p.contains("webdav")) {
            when {
                isEpub -> return java.io.File(base, "index.epub")
                isCbz -> return java.io.File(base, "index.cbz")
                isPdf -> return java.io.File(base, "index.pdf")
            }
        }
        return base
    }
}
