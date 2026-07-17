package com.htmake.reader.utils

import com.htmake.reader.config.AppConfig
import io.vertx.core.json.Json
import io.vertx.core.json.JsonArray
import io.vertx.core.json.JsonObject
import java.io.File

object ExtKt {
    private fun appConfig(): AppConfig = try {
        SpringContextUtils.getBean("appConfig", AppConfig::class.java)
    } catch (_: Exception) {
        AppConfig()
    }

    fun getWorkDir(vararg parts: String): String {
        // Prefer live system property / env so Docker -Dreader.app.workDir=/data always wins
        val configured = System.getProperty("reader.app.workDir")
            ?: System.getenv("READER_APP_WORKDIR")
            ?: appConfig().workDir
        val base = File(configured.ifBlank { "." }).absoluteFile
        return if (parts.isEmpty()) base.absolutePath
        else File(base, parts.joinToString(File.separator)).absolutePath
    }

    fun getStorage(vararg path: String): String? {
        if (path.isEmpty()) return null
        val file = File(getWorkDir(*path))
        val withJson = if (file.extension.isEmpty()) File(file.parent, file.name + ".json") else file
        val target = when {
            file.isFile -> file
            withJson.isFile -> withJson
            else -> File(getWorkDir(*path.dropLast(1).toTypedArray() + (path.last() + ".json")))
        }
        return if (target.isFile) target.readText(Charsets.UTF_8) else null
    }

    fun saveStorage(path: Array<String>, value: String) {
        if (path.isEmpty()) return
        val name = path.last().let { if (it.endsWith(".json")) it else "$it.json" }
        val dir = File(getWorkDir(*path.dropLast(1).toTypedArray()))
        dir.mkdirs()
        File(dir, name).writeText(value, Charsets.UTF_8)
    }

    fun asJsonArray(raw: String?): JsonArray? = try {
        if (raw.isNullOrBlank()) null else JsonArray(raw)
    } catch (_: Exception) {
        null
    }

    fun asJsonObject(raw: String?): JsonObject? = try {
        if (raw.isNullOrBlank()) null else JsonObject(raw)
    } catch (_: Exception) {
        null
    }

    fun jsonEncode(obj: Any?, pretty: Boolean = false): String = when (obj) {
        null -> "null"
        is String -> obj
        is JsonObject -> if (pretty) obj.encodePrettily() else obj.encode()
        is JsonArray -> if (pretty) obj.encodePrettily() else obj.encode()
        else -> try {
            if (pretty) Json.encodePrettily(obj) else Json.encode(obj)
        } catch (_: Exception) {
            obj.toString()
        }
    }

    fun deleteRecursively(f: File?) {
        if (f == null) return
        if (f.isDirectory) f.listFiles()?.forEach { deleteRecursively(it) }
        f.delete()
    }

    fun getRelativePath(vararg parts: String): String = parts.joinToString(File.separator)

    /** Original jar: random A-Za-z0-9 of given length. */
    fun getRandomString(length: Int): String {
        val allowed = "ABCDEFGHIJKLMNOPQRSTUVWXTZabcdefghiklmnopqrstuvwxyz0123456789"
        return (1..length).map { allowed.random() }.joinToString("")
    }

    /**
     * Original jar password scheme:
     * `md5( md5(password + salt) + salt )`
     */
    fun genEncryptedPassword(password: String, salt: String): String {
        val inner = io.legado.app.utils.MD5Utils.md5Encode(password + salt)
        return io.legado.app.utils.MD5Utils.md5Encode(inner + salt)
    }

    fun verifyPassword(plain: String, stored: String, salt: String?): Boolean {
        if (salt.isNullOrBlank()) {
            // legacy plain / direct match
            return stored == plain || stored == genEncryptedPassword(plain, "")
        }
        return stored == genEncryptedPassword(plain, salt) || stored == plain
    }
}

