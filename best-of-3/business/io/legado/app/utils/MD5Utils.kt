/** Business rewrite from reader-pro-3.2.14.jar — readability / audit. */

package io.legado.app.utils
import java.security.MessageDigest
object MD5Utils {
    fun md5Encode(str: String): String {
        val d = MessageDigest.getInstance("MD5").digest(str.toByteArray())
        return d.joinToString("") { "%02x".format(it) }
    }
    fun md5Encode16(str: String): String = md5Encode(str).substring(8, 24)
}
