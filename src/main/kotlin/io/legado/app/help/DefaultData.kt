package io.legado.app.help

import io.legado.app.data.entities.TxtTocRule
import io.legado.app.utils.GSON
import io.legado.app.utils.fromJsonArray

object DefaultData {
    const val txtTocRuleFileName = "txtTocRule.json"

    val txtTocRules: List<TxtTocRule> by lazy {
        val json = String(
            DefaultData::class.java.getResource("/defaultData/txtTocRule.json").readBytes(),
            Charsets.UTF_8
        )
        GSON.fromJsonArray<TxtTocRule>(json).getOrNull() ?: emptyList()
    }
}
