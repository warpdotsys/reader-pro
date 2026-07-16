package io.legado.app.help

import io.legado.app.data.entities.TxtTocRule

object DefaultData {
    val txtTocRules: List<TxtTocRule> = listOf(
        TxtTocRule("中文章节", """^(第[0-9零一二三四五六七八九十百千]+[章节回部集卷].*)$"""),
        TxtTocRule("Chapter", """^(Chapter\s+\d+.*)$""", enable = true),
        TxtTocRule("数字点", """^(\d+\.\s*.{2,40})$""")
    )
}
