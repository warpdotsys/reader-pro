package me.ag2s.epublib.util

object StringUtil {
    @JvmStatic
    fun collapsePathDots(path: String): String {
        val parts = path.split('/').toMutableList()
        var index = 0
        while (index < parts.size - 1) {
            val currentDir = parts[index]
            if (currentDir.isNotEmpty() && currentDir != ".") {
                if (currentDir == "..") {
                    parts.removeAt(index - 1)
                    parts.removeAt(index - 1)
                    index -= 2
                }
            } else {
                parts.removeAt(index)
                index--
            }
            index++
        }
        return buildString {
            if (path.startsWith('/')) append('/')
            parts.forEachIndexed { i, part ->
                append(part)
                if (i < parts.lastIndex) append('/')
            }
        }
    }

    @JvmStatic fun isNotBlank(text: String?): Boolean = !isBlank(text)
    @JvmStatic fun isBlank(text: String?): Boolean = isEmpty(text) || text!!.all { it.isWhitespace() }
    @JvmStatic fun isEmpty(text: String?): Boolean = text == null || text.isEmpty()

    @JvmStatic
    fun endsWithIgnoreCase(source: String?, suffix: String?): Boolean = when {
        isEmpty(suffix) -> true
        isEmpty(source) -> false
        suffix!!.length > source!!.length -> false
        else -> source.substring(source.length - suffix.length).lowercase().endsWith(suffix.lowercase())
    }

    @JvmStatic fun defaultIfNull(text: String?): String = text ?: ""
    @JvmStatic fun defaultIfNull(text: String?, defaultValue: String?): String? = text ?: defaultValue
    @JvmStatic fun equals(text1: String?, text2: String?): Boolean = text1 == text2

    @JvmStatic
    fun toString(vararg keyValues: Any?): String = buildString {
        append('[')
        keyValues.indices.step(2).forEach { i ->
            if (i > 0) append(", ")
            append(keyValues[i])
            append(": ")
            val value = keyValues.getOrNull(i + 1)
            if (value == null) append("<null>") else append('\'').append(value).append('\'')
        }
        append(']')
    }

    @JvmStatic
    fun hashCode(vararg values: String?): Int {
        var result = 31
        values.forEach { value -> result = result xor value.toString().hashCode() }
        return result
    }

    @JvmStatic fun substringBefore(text: String?, separator: Char): String? = substring(text, separator, false, false)
    @JvmStatic fun substringBeforeLast(text: String?, separator: Char): String? = substring(text, separator, true, false)
    @JvmStatic fun substringAfterLast(text: String?, separator: Char): String? = substring(text, separator, true, true)
    @JvmStatic fun substringAfter(text: String?, separator: Char): String? = substring(text, separator, false, true)

    private fun substring(text: String?, separator: Char, last: Boolean, after: Boolean): String? {
        if (isEmpty(text)) return text
        val position = if (last) text!!.lastIndexOf(separator) else text!!.indexOf(separator)
        if (position < 0) return if (after) "" else text
        return if (after) text.substring(position + 1) else text.substring(0, position)
    }

    @JvmStatic
    fun formatHtml(text: String): String = buildString {
        text.split(Regex("\\r?\\n")).forEach { raw ->
            val line = raw.replace(Regex("^\\s+|\\s+$"), "")
            if (line.isNotEmpty()) {
                if (line.matches(Regex("^<img\\s([^>]+)/?>$", RegexOption.IGNORE_CASE))) {
                    append(line.replace(Regex("^<img\\s([^>]+)/?>$", RegexOption.IGNORE_CASE), "<div class=\"duokan-image-single\"><img class=\"picture-80\" $1/></div>"))
                } else append("<p>").append(line).append("</p>")
            }
        }
    }
}
