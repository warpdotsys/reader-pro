/** Business rewrite from reader-pro-3.2.14.jar — readability / audit. */

package io.legado.app.utils
import java.io.File
object FileUtils {
    fun createFileIfNotExist(dir: File, vararg names: String): File {
        val f = names.fold(dir) { a, n -> File(a, n) }
        f.parentFile?.mkdirs()
        if (!f.exists()) f.createNewFile()
        return f
    }
    fun writeBytes(path: String, bytes: ByteArray) {
        File(path).apply { parentFile?.mkdirs() }.writeBytes(bytes)
    }
}
