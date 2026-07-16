/** Business rewrite from reader-pro-3.2.14.jar — readability / audit. */

package com.htmake.reader.config

import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.stereotype.Component

@Component
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
}
