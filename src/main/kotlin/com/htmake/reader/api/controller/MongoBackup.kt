package com.htmake.reader.api.controller

import com.htmake.reader.utils.ExtKt
import com.mongodb.client.MongoClients
import org.bson.Document

object MongoBackup {
    private val keys = listOf(
        "bookshelf", "bookSource", "rssSource", "replaceRule",
        "bookmark", "bookGroup", "userConfig", "httpTTS"
    )

    fun backupUser(ns: String, uri: String, dbName: String): Map<String, Any?> {
        return try {
            MongoClients.create(uri).use { client ->
                val col = client.getDatabase(dbName).getCollection("user_backup")
                val doc = Document("_id", ns).append("updatedAt", System.currentTimeMillis())
                keys.forEach { k ->
                    ExtKt.getStorage("data", ns, k)?.let { doc.append(k, it) }
                }
                col.replaceOne(
                    Document("_id", ns), doc,
                    com.mongodb.client.model.ReplaceOptions().upsert(true)
                )
                mapOf("ok" to true, "keys" to keys.filter { doc.containsKey(it) })
            }
        } catch (e: Exception) {
            mapOf("ok" to false, "error" to (e.message ?: "mongo error"))
        }
    }

    fun restoreUser(ns: String, uri: String, dbName: String): Map<String, Any?> {
        return try {
            MongoClients.create(uri).use { client ->
                val col = client.getDatabase(dbName).getCollection("user_backup")
                val doc = col.find(Document("_id", ns)).first()
                    ?: return mapOf("ok" to false, "error" to "no backup")
                var n = 0
                keys.forEach { k ->
                    val v = doc.getString(k) ?: return@forEach
                    ExtKt.saveStorage(arrayOf("data", ns, k), v)
                    n++
                }
                mapOf("ok" to true, "restored" to n)
            }
        } catch (e: Exception) {
            mapOf("ok" to false, "error" to (e.message ?: "mongo error"))
        }
    }
}
