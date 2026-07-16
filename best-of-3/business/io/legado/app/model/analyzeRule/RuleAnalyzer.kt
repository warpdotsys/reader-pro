/** Business rewrite from reader-pro-3.2.14.jar — phase3. */

package io.legado.app.model.analyzeRule

/**
 * Split rule expressions by && / || / %% with balanced brackets.
 * Port of RuleAnalyzer from jar (simplified balanced scan).
 */
class RuleAnalyzer(private val queue: String, private val trim: Boolean = true) {
    var pos: Int = 0
    var start: Int = 0
    var elementsType: String = "&&"
        private set

    fun splitRule(vararg splitCodes: String): ArrayList<String> {
        val list = ArrayList<String>()
        if (queue.isEmpty()) return list
        // detect which splitter is used first at top level
        elementsType = splitCodes.firstOrNull { queue.contains(it) } ?: "&&"
        val parts = ArrayList<String>()
        var depthSquare = 0
        var depthParen = 0
        var depthCurly = 0
        var last = 0
        var i = 0
        while (i < queue.length) {
            when (queue[i]) {
                '[' -> depthSquare++
                ']' -> depthSquare--
                '(' -> depthParen++
                ')' -> depthParen--
                '{' -> depthCurly++
                '}' -> depthCurly--
            }
            if (depthSquare == 0 && depthParen == 0 && depthCurly == 0) {
                for (code in splitCodes) {
                    if (queue.startsWith(code, i)) {
                        elementsType = code
                        val part = queue.substring(last, i)
                        if (part.isNotEmpty()) parts += if (trim) part.trim() else part
                        i += code.length
                        last = i
                        continue
                    }
                }
            }
            i++
        }
        val tail = queue.substring(last)
        if (tail.isNotEmpty()) parts += if (trim) tail.trim() else tail
        if (parts.isEmpty()) parts += queue
        list.addAll(parts)
        return list
    }

    fun consumeTo(end: String): Boolean {
        val idx = queue.indexOf(end, pos)
        if (idx < 0) return false
        pos = idx + end.length
        return true
    }
}
