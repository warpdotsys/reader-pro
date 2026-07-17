package io.legado.app.exception

class ConcurrentException(msg: String, val waitTime: Int) : NoStackTraceException(msg)
