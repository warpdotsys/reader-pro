package io.legado.app.model

fun interface DebugLog {
    fun log(source: String?, msg: String?)
}

object ConsoleDebugLog : DebugLog {
    override fun log(source: String?, msg: String?) {
        println("[${source ?: "-"}] $msg")
    }
}
