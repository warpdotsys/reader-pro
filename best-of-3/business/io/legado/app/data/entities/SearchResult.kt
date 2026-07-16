/** Business rewrite from reader-pro-3.2.14.jar — phase9. */

package io.legado.app.data.entities

/**
 * 正文检索命中（对齐 jar SearchResult 字段）
 */
data class SearchResult(
    var resultCount: Int = 0,
    var resultCountWithinChapter: Int = 0,
    var resultText: String = "",
    var chapterTitle: String = "",
    var query: String = "",
    var pageSize: Int = 0,
    var chapterIndex: Int = 0,
    var pageIndex: Int = 0,
    var queryIndexInResult: Int = 0,
    var queryIndexInChapter: Int = 0
)
