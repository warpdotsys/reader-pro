package com.htmake.reader.config

import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.stereotype.Component

@Component
@ConfigurationProperties(prefix = "reader.app")
public open class AppConfig {
   public open var autoBackupUserData: Boolean
      internal final set

   public open var autoClearInactiveUser: Int
      internal final set

   public open var cacheChapterContent: Boolean
      internal final set

   public open var debug: Boolean
      internal final set

   public open var debugLog: Boolean
      internal final set

   public open var defaultUserBookLimit: Int = 200
      internal final set

   public open var defaultUserBookSourceLimit: Int = 200
      internal final set

   public open var defaultUserEnableBookSource: Boolean = true
      internal final set

   public open var defaultUserEnableLocalStore: Boolean
      internal final set

   public open var defaultUserEnableRssSource: Boolean = true
      internal final set

   public open var defaultUserEnableWebdav: Boolean
      internal final set

   public open var exportCharset: String = "UTF-8"
      internal final set

   public open var exportNoChapterName: Boolean
      internal final set

   public open var exportPictureFile: Boolean
      internal final set

   public open var exportUseReplace: Boolean
      internal final set

   public open var inviteCode: String = ""
      internal final set

   public open var minUserPasswordLength: Int = 8
      internal final set

   public open var mongoDbName: String = "reader"
      internal final set

   public open var mongoUri: String = ""
      internal final set

   public open var packaged: Boolean
      internal final set

   public open var remoteBookSourceUpdateInterval: Int = 720
      internal final set

   public open var remoteWebviewApi: String = ""
      internal final set

   public open var secure: Boolean
      internal final set

   public open var secureKey: String = ""
      internal final set

   public open var shelfUpdateInteval: Int = 10
      internal final set

   public open var showUI: Boolean
      internal final set

   public open var userBookLimit: Int = 200
      internal final set

   public open var userLimit: Int = 15
      internal final set

   public open var workDir: String = ""
      internal final set
}
