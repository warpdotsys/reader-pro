package io.legado.app.utils

val Throwable.msg: String
    get() {
        val stackTrace = stackTraceToString()
        return if (stackTrace.isNotEmpty()) stackTrace else (localizedMessage ?: "noErrorMsg")
    }
