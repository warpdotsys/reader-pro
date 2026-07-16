package io.legado.app.utils

import java.io.File

object FileUtils {
    fun createFileIfNotExist(parent: File, vararg names: String): File {
        var f = parent
        names.forEach { f = File(f, it) }
        f.parentFile?.mkdirs()
        if (!f.exists()) f.createNewFile()
        return f
    }
}
