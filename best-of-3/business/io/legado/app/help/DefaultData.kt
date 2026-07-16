/** Business rewrite from reader-pro-3.2.14.jar — phase4. */

package io.legado.app.help

import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import io.legado.app.data.entities.TxtTocRule
import java.io.File

object DefaultData {
    const val txtTocRuleFileName = "txtTocRule.json"

    val txtTocRules: List<TxtTocRule> by lazy { loadTxtTocRules() }

    private fun loadTxtTocRules(): List<TxtTocRule> {
        val candidates = listOf(
            File("defaultData/txtTocRule.json"),
            File("resources/defaultData/txtTocRule.json"),
            File(System.getProperty("user.dir"), "defaultData/txtTocRule.json"),
        )
        for (f in candidates) {
            if (f.isFile) {
                return parse(f.readText())
            }
        }
        // classpath
        val stream = DefaultData::class.java.classLoader
            ?.getResourceAsStream("defaultData/txtTocRule.json")
            ?: DefaultData::class.java.getResourceAsStream("/defaultData/txtTocRule.json")
        if (stream != null) {
            return parse(stream.bufferedReader().readText())
        }
        return defaultBuiltin()
    }

    private fun parse(json: String): List<TxtTocRule> {
        val type = object : TypeToken<List<TxtTocRule>>() {}.type
        return Gson().fromJson(json, type) ?: defaultBuiltin()
    }

    private fun defaultBuiltin(): List<TxtTocRule> = listOf(
        TxtTocRule(
            id = -1, enable = true, name = "目录",
            rule = "^\\s*第[0-9零一二三四五六七八九十百千万]+[章节回卷].{0,30}$",
            serialNumber = 0
        ),
        TxtTocRule(
            id = -6, enable = true, name = "数字 分隔符 标题",
            rule = "^[ 　\\t]{0,4}\\d{1,5}[：:,.， 、_—\\-].{1,30}$",
            serialNumber = 1
        ),
    )
}
