package io.legado.app.utils

object TextUtils {
    @JvmStatic fun isEmpty(value: CharSequence?): Boolean = value == null || value.isEmpty()
    @JvmStatic fun join(delimiter: CharSequence, tokens: Array<out Any?>): String = tokens.joinToString(delimiter)
    @JvmStatic fun join(delimiter: CharSequence, tokens: Iterable<*>): String = tokens.joinToString(delimiter)
}
