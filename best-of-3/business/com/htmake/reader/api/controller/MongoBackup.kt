/** Business rewrite from reader-pro-3.2.14.jar — phase3. */

package com.htmake.reader.api.controller

import com.htmake.reader.utils.ExtKt
import com.mongodb.client.MongoClients
import org.bson.Document
import java.io.File

/**
 * Optional MongoDB backup/restore of user JSON storage.
 * Requires reader.app.mongoUri.
 */
object MongoBackup {
    private val files = arrayOf(
        "bookshelf", "bookSource", "rssSource", "replaceRule",
        "bookmark", "bookGroup", "userConfig", "httpTTS"
    )

    fun backupUser(userNameSpace: String, mongoUri: String, dbName: String): Map<String, Any?> {
        return try {
            MongoClients.create(mongoUri).use { client ->
                val db = client.getDatabase(dbName)
                val col = db.getCollection("reader_user_$userNameSpace")
                col.deleteMany(Document())
                var n = 0
                for (name in files) {
                    val raw = ExtKt.getStorage("data", userNameSpace, name) ?: continue
                    col.insertOne(Document(mapOf("name" to name, "payload" to raw, "ts" to System.currentTimeMillis())))
                    n++
                }
                mapOf("ok" to true, "docs" to n)
            }
        } catch (e: Exception) {
            mapOf("ok" to false, "error" to (e.message ?: "mongo error"))
        }
    }

    fun restoreUser(userNameSpace: String, mongoUri: String, dbName: String): Map<String, Any?> {
        return try {
            MongoClients.create(mongoUri).use { client ->
                val db = client.getDatabase(dbName)
                val col = db.getCollection("reader_user_$userNameSpace")
                var n = 0
                col.find().forEach { doc ->
                    val name = doc.getString("name") ?: return@forEach
                    val payload = doc.getString("payload") ?: return@forEach
                    ExtKt.saveStorage(arrayOf("data", userNameSpace, name), payload)
                    n++
                }
                mapOf("ok" to true, "docs" to n)
            }
        } catch (e: Exception) {
            mapOf("ok" to false, "error" to (e.message ?: "mongo error"))
        }
    }
}
