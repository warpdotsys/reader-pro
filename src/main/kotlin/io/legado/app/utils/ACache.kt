package io.legado.app.utils

import java.io.File

class ACache private constructor(private val dir: File) {
    init { dir.mkdirs() }

    fun getAsString(key: String): String? {
        val f = fileOf(key)
        if (!f.isFile) return null
        val text = f.readText()
        if (text.startsWith("expireAt=")) {
            val nl = text.indexOf('\n')
            if (nl > 0) {
                val exp = text.substring(9, nl).toLongOrNull() ?: 0L
                if (exp > 0 && System.currentTimeMillis() > exp) {
                    f.delete(); return null
                }
                return text.substring(nl + 1)
            }
        }
        return text
    }

    fun getByHashCode(name: String): String? {
        val f = File(dir, name)
        return if (f.isFile) f.readText() else null
    }

    fun put(key: String, value: String, saveTimeSec: Int = 0) {
        dir.mkdirs()
        val body = if (saveTimeSec > 0) {
            "expireAt=${System.currentTimeMillis() + saveTimeSec * 1000L}\n$value"
        } else value
        fileOf(key).writeText(body)
    }

    fun remove(key: String) { fileOf(key).delete() }
    private fun fileOf(key: String) = File(dir, key.hashCode().toString())

    companion object {
        fun get(dir: File) = ACache(dir)
        fun get(dir: File, maxSize: Long, maxCount: Int) = ACache(dir)
    }
}
