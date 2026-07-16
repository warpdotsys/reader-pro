package io.legado.app.help

import com.google.gson.JsonParser
import io.legado.app.data.entities.TxtTocRule

object DefaultData {
    private val builtinFallback: List<TxtTocRule> = listOf(
        TxtTocRule("中文章节", """^(第[0-9零一二三四五六七八九十百千]+[章节回部集卷].*)$"""),
        TxtTocRule("Chapter", """^(Chapter\s+\d+.*)$""", enable = true),
        TxtTocRule("数字点", """^(\d+\.\s*.{2,40})$""")
    )

    /** Prefer classpath `defaultData/txtTocRule.json` (from original jar). */
    val txtTocRules: List<TxtTocRule> by lazy { loadTxtTocRules() }

    fun loadTxtTocRules(): List<TxtTocRule> {
        val stream = javaClass.classLoader.getResourceAsStream("defaultData/txtTocRule.json")
            ?: return builtinFallback
        return try {
            val raw = stream.bufferedReader(Charsets.UTF_8).use { it.readText() }
            val arr = JsonParser.parseString(raw).asJsonArray
            val list = arr.mapNotNull { el ->
                val o = el.asJsonObject
                val rule = o.get("rule")?.asString ?: return@mapNotNull null
                TxtTocRule(
                    name = o.get("name")?.asString ?: "",
                    rule = rule,
                    example = o.get("example")?.asString,
                    enable = o.get("enable")?.asBoolean ?: true
                )
            }
            if (list.isEmpty()) builtinFallback else list
        } catch (_: Exception) {
            builtinFallback
        }
    }

    fun enabledTxtTocRules(): List<TxtTocRule> = txtTocRules.filter { it.enable }
}
