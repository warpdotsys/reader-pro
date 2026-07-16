package io.legado.app.utils

object HtmlFormatter {
    private val brRe = Regex("<br\\s*/?>", RegexOption.IGNORE_CASE)
    private val pCloseRe = Regex("</p\\s*>", RegexOption.IGNORE_CASE)
    private val pOpenRe = Regex("<p\\b[^>]*>", RegexOption.IGNORE_CASE)
    private val imgRe = Regex("<img\\b[^>]*/?>", setOf(RegexOption.IGNORE_CASE))
    private val tagRe = Regex("<[^>]+>")

    /** Strip tags; keep line breaks from br/p. */
    fun format(html: String): String =
        html.replace(brRe, "\n")
            .replace(pCloseRe, "\n")
            .replace(pOpenRe, "")
            .replace(tagRe, "")
            .replace(Regex("\n{3,}"), "\n\n")
            .trim()

    /** Strip tags but preserve &lt;img&gt; elements (legado keepImg). */
    fun formatKeepImg(html: String): String {
        val imgs = ArrayList<String>()
        var s = brRe.replace(html) { "\n" }
        s = pCloseRe.replace(s) { "\n" }
        s = imgRe.replace(s) { m ->
            imgs += m.value
            "\u0000IMG${imgs.size - 1}\u0000"
        }
        s = tagRe.replace(s) { "" }
        imgs.forEachIndexed { i, tag ->
            s = s.replace("\u0000IMG$i\u0000", tag)
        }
        return s.replace(Regex("\n{3,}"), "\n\n").trim()
    }
}
