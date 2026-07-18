package com.htmake.reader.config

import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.stereotype.Component

@Component
@ConfigurationProperties(prefix = "reader.app")
class AppConfig {
    var workDir = ""
    var showUI = false
    var debug = false
    var packaged = false
    var secure = false
    var inviteCode = ""
    var secureKey = ""
    var cacheChapterContent = false
    var userLimit = 15
    var userBookLimit = 200
    var debugLog = false
    var autoClearInactiveUser = 0
    var exportUseReplace = false
    var exportCharset = "UTF-8"
    var exportNoChapterName = false
    var exportPictureFile = false
    var mongoUri = ""
    var mongoDbName = "reader"
    var shelfUpdateInteval = 10
    var remoteWebviewApi = ""
    var defaultUserEnableWebdav = false
    var defaultUserEnableLocalStore = false
    var defaultUserEnableBookSource = true
    var defaultUserEnableRssSource = true
    var defaultUserBookSourceLimit = 200
    var defaultUserBookLimit = 200
    var autoBackupUserData = false
    var minUserPasswordLength = 8
    var remoteBookSourceUpdateInterval = 720
}
