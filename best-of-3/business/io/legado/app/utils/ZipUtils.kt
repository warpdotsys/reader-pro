/** Business rewrite from reader-pro-3.2.14.jar — readability / audit. */

package io.legado.app.utils
import java.io.File
import java.util.zip.ZipFile
object ZipUtils {
    fun unzipFile(zip: File, dest: File) {
        dest.mkdirs()
        ZipFile(zip).use { zf ->
            zf.stream().forEach { e ->
                val out = File(dest, e.name)
                if (e.isDirectory) out.mkdirs()
                else {
                    out.parentFile?.mkdirs()
                    zf.getInputStream(e).use { input -> out.outputStream().use { input.copyTo(it) } }
                }
            }
        }
    }
}
