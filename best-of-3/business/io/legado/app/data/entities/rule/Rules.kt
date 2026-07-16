/** Business rewrite from reader-pro-3.2.14.jar — phase11. */

package io.legado.app.data.entities.rule

/** 搜索规则 */
data class SearchRule(
    var checkKeyWord: String? = null,
    var url: String? = null,
    var bookList: String? = null,
    var name: String? = null,
    var author: String? = null,
    var bookUrl: String? = null,
    var coverUrl: String? = null,
    var intro: String? = null,
    var kind: String? = null,
    var lastChapter: String? = null,
    var wordCount: String? = null,
    var updateTime: String? = null
)

/** 发现规则（字段与 Search 列表项对齐） */
data class ExploreRule(
    var bookList: String? = null,
    var name: String? = null,
    var author: String? = null,
    var bookUrl: String? = null,
    var coverUrl: String? = null,
    var intro: String? = null,
    var kind: String? = null,
    var lastChapter: String? = null,
    var wordCount: String? = null,
    var updateTime: String? = null
)

data class BookInfoRule(
    var name: String? = null,
    var author: String? = null,
    var kind: String? = null,
    var coverUrl: String? = null,
    var intro: String? = null,
    var tocUrl: String? = null,
    var lastChapter: String? = null,
    var wordCount: String? = null
)

data class TocRule(
    var chapterList: String? = null,
    var chapterName: String? = null,
    var chapterUrl: String? = null,
    var nextTocUrl: String? = null,
    var preUpdateJs: String? = null,
    var isVolume: String? = null
)

data class ContentRule(
    var content: String? = null,
    var nextContentUrl: String? = null,
    var replaceRegex: String? = null,
    var imageStyle: String? = null
)
