package com.htmake.reader.config

import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.stereotype.Component

@Component("appConfig")
@ConfigurationProperties(prefix = "reader.app")
class AppConfig {
    // bound from application.yml
    var workDir: String = "."
    var showUI: Boolean = false
    var debug: Boolean = false
    var packaged: Boolean = false
    var secure: Boolean = false
    var inviteCode: String = ""
    var secureKey: String = ""
    var debugLog: Boolean = false
    var userLimit: Int = 50
    var userBookLimit: Int = 200
    var mongoUri: String = ""
    var mongoDbName: String = "reader"
    var cacheChapterContent: Boolean = true
    var remoteWebviewApi: String = ""
    var minUserPasswordLength: Int = 8
    var shelfUpdateInteval: Int = 30
    var autoClearInactiveUser: Int = 0
    var autoBackupUserData: Boolean = false
    /** Remote license activate URL (empty = skip remote). */
    var remoteActivateUrl: String = "https://r.htmake.com/reader3/activateLicense"
    /** When true, activateLicense also POSTs to remoteActivateUrl. */
    var remoteActivateEnabled: Boolean = false

    // ---- SMTP (optional) ----
    var smtpHost: String = ""
    var smtpPort: Int = 465
    var smtpUser: String = ""
    var smtpPassword: String = ""
    var smtpFrom: String = ""
    var smtpSsl: Boolean = true
    var smtpStartTls: Boolean = false

    /** Auto backup interval for Spring scheduler (ms). Default 1h. */
    var autoBackupIntervalMs: Long = 3_600_000
    /** Inactive clear interval (ms). Default 24h. */
    var autoClearIntervalMs: Long = 86_400_000
}

