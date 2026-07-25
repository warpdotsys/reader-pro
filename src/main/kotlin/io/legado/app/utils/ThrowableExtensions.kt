package io.legado.app.utils

val Throwable.msg: String
    get() {
        val stackTrace = stackTraceToString()
        val msg = localizedMessage ?: "noErrorMsg"
        return if (stackTrace.isNotEmpty()) stackTrace else msg
    }
