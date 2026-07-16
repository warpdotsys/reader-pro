package io.legado.app.utils

import java.io.File
import java.util.zip.ZipFile

object ZipUtils {
    fun unzipFile(zip: File, dest: File): Boolean {
        return try {
            dest.mkdirs()
            ZipFile(zip).use { zf ->
                zf.entries().asSequence().forEach { e ->
                    val out = File(dest, e.name)
                    if (e.isDirectory) out.mkdirs()
                    else {
                        out.parentFile?.mkdirs()
                        zf.getInputStream(e).use { ins -> out.outputStream().use { ins.copyTo(it) } }
                    }
                }
            }
            true
        } catch (_: Exception) {
            false
        }
    }
}
