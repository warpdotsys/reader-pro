package io.legado.app.model.analyzeRule

interface RuleDataInterface {
    fun getUserNameSpace(): String = "default"
    fun putVariable(key: String, value: String?) {}
    fun getVariable(key: String): String? = null
}
