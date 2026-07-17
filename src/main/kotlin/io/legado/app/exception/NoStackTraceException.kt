package io.legado.app.exception

open class NoStackTraceException(msg: String) : Exception(msg) {
    override fun fillInStackTrace(): Throwable = this
}
