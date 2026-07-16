/** Business rewrite from reader-pro-3.2.14.jar — phase10. */

package io.legado.app.utils

import java.io.File

/**
 * 简易磁盘缓存（对齐 jar ACache：按 key hash 文件名）。
 * put(key, value, saveTimeSec) — saveTimeSec>0 时写过期戳。
 */
class ACache private constructor(
    private val dir: File,
    private val maxSize: Long = 5_000_000L,
    private val maxCount: Int = 1_000_000
) {
    init {
        dir.mkdirs()
    }

    fun getAsString(key: String): String? {
        val f = fileOf(key)
        if (!f.isFile) return null
        val text = f.readText()
        // optional expire header: "expireAt=<ms>\n"
        if (text.startsWith("expireAt=")) {
            val nl = text.indexOf('\n')
            if (nl > 0) {
                val exp = text.substring(9, nl).toLongOrNull() ?: 0L
                if (exp > 0 && System.currentTimeMillis() > exp) {
                    f.delete()
                    return null
                }
                return text.substring(nl + 1)
            }
        }
        return text
    }

    /** jar getByHashCode(fileName) — 直接读 hash 文件名 */
    fun getByHashCode(hashFileName: String): String? {
        val f = File(dir, hashFileName)
        if (!f.isFile) return null
        val text = f.readText()
        if (text.startsWith("expireAt=")) {
            val nl = text.indexOf('\n')
            if (nl > 0) {
                val exp = text.substring(9, nl).toLongOrNull() ?: 0L
                if (exp > 0 && System.currentTimeMillis() > exp) {
                    f.delete()
                    return null
                }
                return text.substring(nl + 1)
            }
        }
        return text
    }

    fun put(key: String, value: String, saveTimeSec: Int = 0) {
        dir.mkdirs()
        val body = if (saveTimeSec > 0) {
            val exp = System.currentTimeMillis() + saveTimeSec * 1000L
            "expireAt=$exp\n$value"
        } else value
        fileOf(key).writeText(body)
    }

    fun remove(key: String) {
        fileOf(key).delete()
    }

    private fun fileOf(key: String): File = File(dir, key.hashCode().toString())

    companion object {
        fun get(dir: File, maxSize: Long = 5_000_000L, maxCount: Int = 1_000_000): ACache =
            ACache(dir, maxSize, maxCount)
        fun get(dir: File): ACache = ACache(dir)
    }
}
