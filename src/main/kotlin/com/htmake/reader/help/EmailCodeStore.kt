package com.htmake.reader.help

import com.htmake.reader.utils.ExtKt
import io.vertx.core.json.JsonObject
import java.io.File
import java.util.concurrent.ConcurrentHashMap
import kotlin.random.Random

/**
 * Email verification codes for license / register flows.
 * In-memory + disk fallback; real SMTP optional via AppConfig later.
 */
object EmailCodeStore {
    private data class Entry(val code: String, val expireAt: Long)

    private val mem = ConcurrentHashMap<String, Entry>()

    fun generateCode(length: Int = 6): String =
        (1..length).map { Random.nextInt(0, 10) }.joinToString("")

    fun put(email: String, code: String, ttlMs: Long = 10 * 60 * 1000L) {
        val key = email.trim().lowercase()
        val exp = System.currentTimeMillis() + ttlMs
        mem[key] = Entry(code, exp)
        persist()
    }

    fun verify(email: String, code: String): Boolean {
        load()
        val key = email.trim().lowercase()
        val e = mem[key] ?: return false
        if (System.currentTimeMillis() > e.expireAt) {
            mem.remove(key); persist(); return false
        }
        val ok = e.code == code.trim()
        if (ok) {
            mem.remove(key); persist()
        }
        return ok
    }

    fun peek(email: String): String? {
        load()
        val e = mem[email.trim().lowercase()] ?: return null
        if (System.currentTimeMillis() > e.expireAt) return null
        return e.code
    }

    private fun file(): File =
        File(ExtKt.getWorkDir("storage", "data", "emailCodes.json"))

    private fun persist() {
        val o = JsonObject()
        mem.forEach { (k, v) ->
            o.put(k, JsonObject().put("code", v.code).put("expireAt", v.expireAt))
        }
        file().apply { parentFile?.mkdirs() }.writeText(o.encode())
    }

    private fun load() {
        val f = file()
        if (!f.isFile) return
        runCatching {
            val o = JsonObject(f.readText())
            o.fieldNames().forEach { k ->
                val e = o.getJsonObject(k) ?: return@forEach
                mem[k] = Entry(e.getString("code") ?: return@forEach, e.getLong("expireAt") ?: 0L)
            }
        }
    }
}
