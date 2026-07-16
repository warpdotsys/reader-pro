package io.legado.app.data.entities

data class SearchBook(
    var name: String = "",
    var author: String = "",
    var bookUrl: String = "",
    var origin: String = "",
    var originName: String = "",
    var coverUrl: String? = null,
    var intro: String? = null,
    var kind: String? = null,
    var latestChapterTitle: String? = null
) {
    fun toBook() = Book(
        bookUrl = bookUrl, name = name, author = author,
        origin = origin, originName = originName,
        coverUrl = coverUrl, intro = intro, kind = kind,
        latestChapterTitle = latestChapterTitle
    )
}
