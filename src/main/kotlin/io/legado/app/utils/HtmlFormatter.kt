package io.legado.app.utils

object HtmlFormatter {
    fun format(html: String): String =
        html.replace(Regex("<br\\s*/?>", RegexOption.IGNORE_CASE), "\n")
            .replace(Regex("</p>", RegexOption.IGNORE_CASE), "\n")
            .replace(Regex("<[^>]+>"), "")
            .trim()

    fun formatKeepImg(html: String): String =
        html.replace(Regex("<(?!img\\b)[^>]+>", RegexOption.IGNORE_CASE), "")
}
