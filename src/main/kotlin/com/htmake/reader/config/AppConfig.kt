package com.htmake.reader.config

import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.stereotype.Component

/**
 * Mirrors original jar `reader.app.*` (+ rebuild-only optional fields).
 */
@Component("appConfig")
@ConfigurationProperties(prefix = "reader.app")
class AppConfig {
    var workDir: String = "."
    var showUI: Boolean = false
    var debug: Boolean = false
    var packaged: Boolean = false
    var secure: Boolean = false
    var inviteCode: String = ""
    var secureKey: String = ""
    var proxy: Boolean = false
    var proxyType: String = "HTTP"
    var proxyHost: String = ""
    var proxyPort: String = ""
    var proxyUsername: String = ""
    var proxyPassword: String = ""
    var cacheChapterContent: Boolean = true
    var userLimit: Int = 15
    var userBookLimit: Int = 200
    var debugLog: Boolean = false
    var autoClearInactiveUser: Int = 0
    var mongoUri: String = ""
    var mongoDbName: String = "reader"
    var shelfUpdateInteval: Int = 30
    var remoteWebviewApi: String = ""
    var defaultUserEnableWebdav: Boolean = true
    var defaultUserEnableLocalStore: Boolean = true
    var defaultUserEnableBookSource: Boolean = true
    var defaultUserEnableRssSource: Boolean = true
    var defaultUserBookSourceLimit: Int = 100
    var defaultUserBookLimit: Int = 200
    var autoBackupUserData: Boolean = false
    var minUserPasswordLength: Int = 8
    var remoteBookSourceUpdateInterval: Int = 720

    // rebuild extensions
    var remoteActivateUrl: String = "https://r.htmake.com/reader3/activateLicense"
    var remoteActivateEnabled: Boolean = false
    var smtpHost: String = ""
    var smtpPort: Int = 465
    var smtpUser: String = ""
    var smtpPassword: String = ""
    var smtpFrom: String = ""
    var smtpSsl: Boolean = true
    var smtpStartTls: Boolean = false
    var autoBackupIntervalMs: Long = 3_600_000
    var autoClearIntervalMs: Long = 86_400_000
}

@Component("readerServerConfig")
@ConfigurationProperties(prefix = "reader.server")
class ReaderServerConfig {
    var port: Int = 8080
    var contextPath: String = ""
    var webUrl: String = "http://localhost:8080"
}
