package com.htmake.reader.api.controller

import com.htmake.reader.utils.ExtKt
import com.mongodb.client.MongoClients
import io.vertx.core.json.JsonObject
import org.bson.Document
import java.io.File

/**
 * User data backup/restore.
 * Prefer Mongo when uri is set; otherwise file store under
 * `storage/data/_mongo_fallback/user_backup/{ns}.json` so APIs work offline.
 */
object MongoBackup {
    val keys = listOf(
        "bookshelf", "bookSource", "rssSource", "replaceRule",
        "bookmark", "bookGroup", "userConfig", "httpTTS"
    )

    fun backupUser(ns: String, uri: String?, dbName: String): Map<String, Any?> {
        val payload = buildPayload(ns)
        return if (!uri.isNullOrBlank()) {
            try {
                mongoUpsert(uri, dbName, payload)
                mapOf(
                    "ok" to true,
                    "backend" to "mongo",
                    "ns" to ns,
                    "keys" to keys.filter { payload.containsKey(it) },
                    "updatedAt" to payload["updatedAt"]
                )
            } catch (e: Exception) {
                // fall through to file
                fileUpsert(ns, payload)
                mapOf(
                    "ok" to true,
                    "backend" to "file",
                    "mongoError" to (e.message ?: "mongo error"),
                    "ns" to ns,
                    "keys" to keys.filter { payload.containsKey(it) }
                )
            }
        } else {
            fileUpsert(ns, payload)
            mapOf(
                "ok" to true,
                "backend" to "file",
                "ns" to ns,
                "keys" to keys.filter { payload.containsKey(it) },
                "updatedAt" to payload["updatedAt"]
            )
        }
    }

    fun restoreUser(ns: String, uri: String?, dbName: String): Map<String, Any?> {
        val doc = if (!uri.isNullOrBlank()) {
            try {
                mongoFind(uri, dbName, ns)
            } catch (_: Exception) {
                fileFind(ns)
            }
        } else fileFind(ns)
        if (doc == null) return mapOf("ok" to false, "error" to "no backup")
        var n = 0
        keys.forEach { k ->
            val v = doc[k]?.toString() ?: return@forEach
            if (v.isBlank()) return@forEach
            ExtKt.saveStorage(arrayOf("data", ns, k), v)
            n++
        }
        return mapOf("ok" to true, "restored" to n, "updatedAt" to doc["updatedAt"])
    }

    fun listBackups(uri: String?, dbName: String): List<Map<String, Any?>> {
        return if (!uri.isNullOrBlank()) {
            try {
                MongoClients.create(uri).use { client ->
                    client.getDatabase(dbName).getCollection("user_backup")
                        .find()
                        .map { d ->
                            mapOf(
                                "ns" to d.getString("_id"),
                                "updatedAt" to (d.getLong("updatedAt") ?: 0L),
                                "keys" to keys.filter { d.containsKey(it) }
                            )
                        }.toList()
                }
            } catch (_: Exception) {
                listFileBackups()
            }
        } else listFileBackups()
    }

    fun deleteBackup(ns: String, uri: String?, dbName: String): Map<String, Any?> {
        if (!uri.isNullOrBlank()) {
            try {
                MongoClients.create(uri).use { client ->
                    val r = client.getDatabase(dbName).getCollection("user_backup")
                        .deleteOne(Document("_id", ns))
                    return mapOf("ok" to true, "deleted" to r.deletedCount, "backend" to "mongo")
                }
            } catch (e: Exception) {
                fileDelete(ns)
                return mapOf("ok" to true, "backend" to "file", "mongoError" to e.message)
            }
        }
        fileDelete(ns)
        return mapOf("ok" to true, "backend" to "file")
    }

    fun backupAllUsers(uri: String?, dbName: String): Map<String, Any?> {
        val users = listUserNamespaces()
        val results = users.map { ns -> ns to backupUser(ns, uri, dbName) }
        val ok = results.count { (it.second["ok"] as? Boolean) == true }
        return mapOf("ok" to true, "total" to users.size, "success" to ok, "results" to results.map {
            mapOf("ns" to it.first, "result" to it.second)
        })
    }

    fun listUserNamespaces(): List<String> {
        val data = File(ExtKt.getWorkDir("storage", "data"))
        if (!data.isDirectory) return listOf("default")
        val fromDirs = data.listFiles()?.filter { it.isDirectory && !it.name.startsWith("_") && it.name != "cache" }
            ?.map { it.name } ?: emptyList()
        val fromUsers = try {
            val raw = ExtKt.getStorage("data", "users")
            ExtKt.asJsonObject(raw)?.fieldNames()?.toList() ?: emptyList()
        } catch (_: Exception) {
            emptyList()
        }
        return (fromDirs + fromUsers + "default").distinct().sorted()
    }

    private fun buildPayload(ns: String): MutableMap<String, Any?> {
        val doc = linkedMapOf<String, Any?>(
            "_id" to ns,
            "updatedAt" to System.currentTimeMillis()
        )
        keys.forEach { k ->
            ExtKt.getStorage("data", ns, k)?.let { doc[k] = it }
        }
        // lightweight stats
        val shelf = ExtKt.getStorage("data", ns, "bookshelf")
        doc["meta"] = mapOf(
            "bookshelfBytes" to (shelf?.length ?: 0),
            "keyCount" to keys.count { doc.containsKey(it) }
        )
        return doc
    }

    private fun mongoUpsert(uri: String, dbName: String, payload: Map<String, Any?>) {
        MongoClients.create(uri).use { client ->
            val col = client.getDatabase(dbName).getCollection("user_backup")
            val doc = Document(payload)
            col.replaceOne(
                Document("_id", payload["_id"]),
                doc,
                com.mongodb.client.model.ReplaceOptions().upsert(true)
            )
        }
    }

    private fun mongoFind(uri: String, dbName: String, ns: String): Map<String, Any?>? {
        MongoClients.create(uri).use { client ->
            val col = client.getDatabase(dbName).getCollection("user_backup")
            val d = col.find(Document("_id", ns)).first() ?: return null
            return d.entries.associate { it.key to it.value }
        }
    }

    private fun fallbackDir(): File =
        File(ExtKt.getWorkDir("storage", "data", "_mongo_fallback", "user_backup")).apply { mkdirs() }

    private fun fileUpsert(ns: String, payload: Map<String, Any?>) {
        val f = File(fallbackDir(), "$ns.json")
        f.writeText(JsonObject(payload).encode())
    }

    private fun fileFind(ns: String): Map<String, Any?>? {
        val f = File(fallbackDir(), "$ns.json")
        if (!f.isFile) return null
        return runCatching { JsonObject(f.readText()).map }.getOrNull()
    }

    private fun fileDelete(ns: String) {
        File(fallbackDir(), "$ns.json").delete()
    }

    private fun listFileBackups(): List<Map<String, Any?>> {
        return fallbackDir().listFiles()?.filter { it.extension.equals("json", true) }?.map { f ->
            val o = runCatching { JsonObject(f.readText()) }.getOrNull()
            mapOf(
                "ns" to (o?.getString("_id") ?: f.nameWithoutExtension),
                "updatedAt" to (o?.getLong("updatedAt") ?: f.lastModified()),
                "keys" to keys.filter { o?.containsKey(it) == true },
                "backend" to "file"
            )
        }?.sortedByDescending { it["updatedAt"] as? Long ?: 0L } ?: emptyList()
    }
}
