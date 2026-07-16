/** Business rewrite from reader-pro-3.2.14.jar — readability / audit. */

package io.legado.app.exception
class TocEmptyException(msg: String) : Exception(msg)
class ContentEmptyException(msg: String) : Exception(msg)
class NoStackTraceException(msg: String) : Exception(msg)
