/** Business rewrite from reader-pro-3.2.14.jar — phase5. */

package io.legado.app.utils

object HtmlFormatter {
    /** Keep img tags, strip excessive scripts already removed; light normalize. */
    fun formatKeepImg(html: String, other: Any? = null): String {
        var s = html
        // collapse 3+ newlines
        s = s.replace(Regex("\\n{3,}"), "\n\n")
        // ensure img not stripped
        return s.trim()
    }
}
