package io.legado.app.model.analyzeRule

class RuleAnalyzer(private val rule: String) {
    var elementsType: String = "&&"

    fun splitRule(vararg separators: String): List<String> {
        for (sep in separators) {
            if (rule.contains(sep)) {
                elementsType = sep
                return rule.split(sep).map { it.trim() }.filter { it.isNotEmpty() }
            }
        }
        return listOf(rule)
    }
}
