/** Business rewrite from reader-pro-3.2.14.jar — readability / audit. */

package com.htmake.reader.utils

import io.vertx.core.json.Json
import io.vertx.core.json.JsonArray
import io.vertx.core.json.JsonObject
import java.io.File
import java.security.MessageDigest
import javax.crypto.SecretKeyFactory
import javax.crypto.spec.PBEKeySpec

/**
 * Storage / crypto / path helpers used across controllers.
 */
object ExtKt {
    @JvmStatic
    var workDir: String = System.getProperty("reader.workDir", ".")

    @JvmStatic
    fun getWorkDir(vararg parts: String): String {
        return parts.fold(File(workDir)) { acc, p -> File(acc, p) }.absolutePath
    }

    @JvmStatic
    fun getWorkDir(): String = File(workDir).absolutePath

    @JvmStatic
    fun getStorage(vararg path: String): String? {
        val f = File(getWorkDir("storage", *path).let {
            // allow "data/x" style via multiple args
            File(getWorkDir(), listOf("storage", *path).joinToString(File.separator))
        })
        // simpler:
        val file = path.fold(File(getWorkDir(), "storage")) { a, p -> File(a, p) }
        val json = File(file.path + ".json")
        val plain = file
        return when {
            json.isFile -> json.readText()
            plain.isFile -> plain.readText()
            else -> null
        }
    }

    @JvmStatic
    fun saveStorage(path: Array<String>, content: String, pretty: Boolean = false, unused: Any? = null) {
        val base = path.fold(File(getWorkDir(), "storage")) { a, p -> File(a, p) }
        val f = if (base.extension.isEmpty()) File(base.path + ".json") else base
        f.parentFile?.mkdirs()
        f.writeText(content)
    }

    @JvmStatic fun asJsonObject(raw: String?): JsonObject? =
        raw?.let { try { JsonObject(it) } catch (_: Exception) { null } }

    @JvmStatic fun asJsonArray(raw: String?): JsonArray? =
        raw?.let { try { JsonArray(it) } catch (_: Exception) { null } }

    @JvmStatic fun jsonEncode(obj: Any?, pretty: Boolean = false): String = Json.encode(obj)

    @JvmStatic fun getRelativePath(vararg parts: String): String = parts.joinToString("/")

    @JvmStatic
    fun genEncryptedPassword(password: String, salt: String): String {
        // approximate: jar uses salted hash (see hutool / custom in ExtKt CFR)
        val md = MessageDigest.getInstance("SHA-256")
        return md.digest((password + salt).toByteArray()).joinToString("") { "%02x".format(it) }
    }

    @JvmStatic
    fun deleteRecursively(f: File?) {
        if (f == null || !f.exists()) return
        if (f.isDirectory) f.listFiles()?.forEach { deleteRecursively(it) }
        f.delete()
    }
}
