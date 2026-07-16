package com.htmake.reader.schedule

import com.htmake.reader.api.controller.MongoBackup
import com.htmake.reader.config.AppConfig
import com.htmake.reader.utils.ExtKt
import io.vertx.core.json.JsonObject
import mu.KotlinLogging
import org.springframework.scheduling.annotation.Scheduled
import org.springframework.stereotype.Component
import java.io.File
import java.util.concurrent.atomic.AtomicLong

private val log = try {
    KotlinLogging.logger {}
} catch (_: Throwable) {
    null
}

/**
 * Background jobs (Spring @Scheduled). Enabled flags come from AppConfig.
 */
@Component
class ReaderJobs(private val appConfig: AppConfig) {

    private val lastBackupAt = AtomicLong(0)
    private val lastClearAt = AtomicLong(0)
    private val lastShelfTick = AtomicLong(0)

    /** Every hour: auto backup all users when enabled. */
    @Scheduled(fixedDelayString = "\${reader.app.autoBackupIntervalMs:3600000}", initialDelay = 120_000)
    fun autoBackupJob() {
        if (!appConfig.autoBackupUserData) return
        runCatching {
            val result = runAutoBackup(appConfig)
            lastBackupAt.set(System.currentTimeMillis())
            log?.info { "autoBackup done: $result" }
            println("[ReaderJobs] autoBackup: $result")
        }.onFailure {
            log?.error(it) { "autoBackup failed" }
            println("[ReaderJobs] autoBackup error: ${it.message}")
        }
    }

    /** Every day: clear inactive users when autoClearInactiveUser > 0 (days). */
    @Scheduled(fixedDelayString = "\${reader.app.autoClearIntervalMs:86400000}", initialDelay = 180_000)
    fun clearInactiveJob() {
        val days = appConfig.autoClearInactiveUser
        if (days <= 0) return
        runCatching {
            val n = runClearInactive(days, purgeData = false)
            lastClearAt.set(System.currentTimeMillis())
            log?.info { "clearInactive removed=$n day=$days" }
            println("[ReaderJobs] clearInactive removed=$n day=$days")
        }.onFailure {
            println("[ReaderJobs] clearInactive error: ${it.message}")
        }
    }

    /** Shelf update tick log (interval minutes from config). */
    @Scheduled(fixedDelay = 60_000, initialDelay = 60_000)
    fun shelfUpdateTick() {
        val interval = appConfig.shelfUpdateInteval
        if (interval <= 0) return
        val now = System.currentTimeMillis()
        val last = lastShelfTick.get()
        if (last > 0 && now - last < interval * 60_000L) return
        lastShelfTick.set(now)
        // Full multi-user shelf refresh is heavy; record heartbeat for ops.
        runCatching {
            val f = File(ExtKt.getWorkDir("storage", "data", "_jobs", "shelf-tick.json"))
            f.parentFile?.mkdirs()
            f.writeText(
                JsonObject()
                    .put("at", now)
                    .put("intervalMin", interval)
                    .encode()
            )
        }
    }

    fun status(): Map<String, Any?> = mapOf(
        "autoBackupUserData" to appConfig.autoBackupUserData,
        "autoClearInactiveUser" to appConfig.autoClearInactiveUser,
        "shelfUpdateInteval" to appConfig.shelfUpdateInteval,
        "lastBackupAt" to lastBackupAt.get(),
        "lastClearAt" to lastClearAt.get(),
        "lastShelfTick" to lastShelfTick.get()
    )

    companion object {
        fun runAutoBackup(cfg: AppConfig): Map<String, Any?> {
            val uri = cfg.mongoUri.takeIf { it.isNotBlank() }
            return MongoBackup.backupAllUsers(uri, cfg.mongoDbName)
        }

        /**
         * Pure cleanup used by job + tests (does not need RoutingContext).
         */
        fun runClearInactive(day: Int, purgeData: Boolean): Int {
            val cutoff = System.currentTimeMillis() - day.toLong() * 24 * 3600 * 1000
            val raw = ExtKt.getStorage("data", "users") ?: return 0
            val obj = ExtKt.asJsonObject(raw) ?: return 0
            val keep = JsonObject()
            var removed = 0
            obj.fieldNames().forEach { name ->
                val u = obj.getJsonObject(name) ?: return@forEach
                val last = u.getLong("last_login_at")
                    ?: u.getLong("created_at")
                    ?: 0L
                val drop = last > 0 && last < cutoff
                if (drop) {
                    removed++
                    if (purgeData) {
                        ExtKt.deleteRecursively(File(ExtKt.getWorkDir("storage", "data", name)))
                    }
                } else {
                    keep.put(name, u)
                }
            }
            if (removed > 0) {
                ExtKt.saveStorage(arrayOf("data", "users"), keep.encode())
            }
            return removed
        }
    }
}
