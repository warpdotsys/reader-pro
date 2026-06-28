# Provenance

This repository was assembled from an authorized local binary artifact supplied by the maintainer plus limited public reference repositories. The 3.2.14 JAR is the authoritative runtime source for behavior.

## Authoritative artifact

- Original path: `C:\Users\chong\OneDrive\下载\reader-pro-3.2.14.jar`
- Repository copy: `app/reader-pro-3.2.14.jar`
- Size: `72913887` bytes
- SHA-256: `b26fb4769d689d98ff26408ce79a275d719f360906c84acf52ff404e98030c8c`
- Manifest start class: `com.htmake.reader.ReaderApplicationKt`
- Spring Boot version: `2.1.6.RELEASE`
- Embedded class count: `936`
- Embedded library count: `85`
- Embedded non-class resource count: `257`

## Decompiled source recovery

- Tool: JADX `1.5.5` downloaded from the official `skylot/jadx` GitHub release archive.
- Input for decompilation: application classes extracted from `BOOT-INF/classes` and repackaged as `recovery/decompile-work/app-classes.jar`.
- Output committed at: `recovered-source/jadx/`.
- Decompiled Java files: `302`.
- Decompiled resource files committed: `0`.
- Duplicate `.class` files copied by JADX into `resources/` are omitted because `app/reader-pro-3.2.14.jar` is the authoritative bytecode archive.
- JADX reported error count: `19`. Output was still emitted; decompiled files should be treated as recovered audit/reconstruction material, not as a verified clean build source tree.

## Public references

- `changshengyu/reader-dev`: `fa22f271849d45f93349ae1636223e27b16a4691` — limited older source reference copied into `reference-source/`.
- `hectorqin/reader-legado`: `d162b90398400b035da6645a2b3df6d6c81b9d72` — limited older source reference used for comparison.

The public repositories are reference material only. When reference source and the 3.2.14 artifact differ, the artifact is treated as authoritative.

## Top package inventory

- `com.htmake.reader.api`: 392
- `io.legado.app.model`: 127
- `io.legado.app.help`: 79
- `io.legado.app.data`: 53
- `io.legado.app.utils`: 50
- `io.legado.app.lib`: 49
- `me.ag2s.epublib.domain`: 28
- `me.ag2s.epublib.epub`: 27
- `com.htmake.reader.utils`: 24
- `io.legado.app.constant`: 15
- `com.htmake.reader.lib`: 13
- `me.ag2s.epublib.util`: 13
- `com.htmake.reader.db`: 8
- `com.htmake.reader.entity`: 6
- `com.htmake.reader.verticle`: 6
- `me.ag2s.umdlib.domain`: 5
- `me.ag2s.epublib.browsersupport`: 5
- `io.legado.app.exception`: 5
- `io.legado.app.adapters`: 4
- `me.ag2s.umdlib.tool`: 3
- `com.htmake.reader.init`: 3
- `com.htmake.reader.config`: 2
- `com.htmake.reader.SpringEvent`: 1
- `me.ag2s.umdlib.umd`: 1
- `me.ag2s.epublib.Constants`: 1
- `org.kxml2.io.KXmlParser`: 1
- `org.kxml2.io.KXmlSerializer`: 1
- `org.kxml2.wap.wv`: 1
- `org.kxml2.wap.syncml`: 1
- `org.kxml2.wap.wml`: 1
- `org.kxml2.wap.WbxmlSerializer`: 1
- `org.kxml2.wap.Wbxml`: 1
- `org.kxml2.wap.WbxmlParser`: 1
- `org.kxml2.kdom.Node`: 1
- `org.kxml2.kdom.Element`: 1
- `org.kxml2.kdom.Document`: 1
- `com.htmake.reader.ReaderApplicationKt$logger$1`: 1
- `com.htmake.reader.ReaderApplicationKt`: 1
- `com.htmake.reader.ReaderApplication$Companion`: 1
- `com.htmake.reader.ReaderApplication`: 1
- `com.htmake.reader.ReaderApplication$Companion$vertx$2`: 1

## Main recovered Java files

- `recovered-source/jadx/sources/com/htmake/reader/ReaderApplication.java`
- `recovered-source/jadx/sources/com/htmake/reader/ReaderApplicationKt.java`
- `recovered-source/jadx/sources/com/htmake/reader/SpringEvent.java`
- `recovered-source/jadx/sources/com/htmake/reader/api/ReturnData.java`
- `recovered-source/jadx/sources/com/htmake/reader/api/YueduApi.java`
- `recovered-source/jadx/sources/com/htmake/reader/api/YueduApiKt.java`
- `recovered-source/jadx/sources/com/htmake/reader/api/controller/BaseController.java`
- `recovered-source/jadx/sources/com/htmake/reader/api/controller/BaseControllerKt.java`
- `recovered-source/jadx/sources/com/htmake/reader/api/controller/BookController$cacheBookOnServer$$inlined$CoroutineExceptionHandler$1.java`
- `recovered-source/jadx/sources/com/htmake/reader/api/controller/BookController$getBookCover$$inlined$CoroutineExceptionHandler$1.java`
- `recovered-source/jadx/sources/com/htmake/reader/api/controller/BookController$textToSpeech$$inlined$CoroutineExceptionHandler$1.java`
- `recovered-source/jadx/sources/com/htmake/reader/api/controller/BookController.java`
- `recovered-source/jadx/sources/com/htmake/reader/api/controller/BookControllerKt.java`
- `recovered-source/jadx/sources/com/htmake/reader/api/controller/BookGroupController.java`
- `recovered-source/jadx/sources/com/htmake/reader/api/controller/BookGroupControllerKt.java`
- `recovered-source/jadx/sources/com/htmake/reader/api/controller/BookSourceController.java`
- `recovered-source/jadx/sources/com/htmake/reader/api/controller/BookSourceControllerKt.java`
- `recovered-source/jadx/sources/com/htmake/reader/api/controller/BookmarkController.java`
- `recovered-source/jadx/sources/com/htmake/reader/api/controller/BookmarkControllerKt.java`
- `recovered-source/jadx/sources/com/htmake/reader/api/controller/CURD.java`
- `recovered-source/jadx/sources/com/htmake/reader/api/controller/CURDKt.java`
- `recovered-source/jadx/sources/com/htmake/reader/api/controller/FileController.java`
- `recovered-source/jadx/sources/com/htmake/reader/api/controller/FileControllerKt.java`
- `recovered-source/jadx/sources/com/htmake/reader/api/controller/HttpTTSController.java`
- `recovered-source/jadx/sources/com/htmake/reader/api/controller/LicenseController$checkLicense$$inlined$CoroutineExceptionHandler$1.java`
- `recovered-source/jadx/sources/com/htmake/reader/api/controller/LicenseController$importLicense$$inlined$CoroutineExceptionHandler$1.java`
- `recovered-source/jadx/sources/com/htmake/reader/api/controller/LicenseController.java`
- `recovered-source/jadx/sources/com/htmake/reader/api/controller/LicenseControllerKt.java`
- `recovered-source/jadx/sources/com/htmake/reader/api/controller/ReplaceRuleController.java`
- `recovered-source/jadx/sources/com/htmake/reader/api/controller/ReplaceRuleControllerKt.java`
- `recovered-source/jadx/sources/com/htmake/reader/api/controller/RssSourceController.java`
- `recovered-source/jadx/sources/com/htmake/reader/api/controller/RssSourceControllerKt.java`
- `recovered-source/jadx/sources/com/htmake/reader/api/controller/UserController.java`
- `recovered-source/jadx/sources/com/htmake/reader/api/controller/UserControllerKt.java`
- `recovered-source/jadx/sources/com/htmake/reader/api/controller/WebdavController$1$10.java`
- `recovered-source/jadx/sources/com/htmake/reader/api/controller/WebdavController$1$2.java`
- `recovered-source/jadx/sources/com/htmake/reader/api/controller/WebdavController$1$3.java`
- `recovered-source/jadx/sources/com/htmake/reader/api/controller/WebdavController$1$4.java`
- `recovered-source/jadx/sources/com/htmake/reader/api/controller/WebdavController$1$5.java`
- `recovered-source/jadx/sources/com/htmake/reader/api/controller/WebdavController$1$6.java`
- `recovered-source/jadx/sources/com/htmake/reader/api/controller/WebdavController$1$7.java`
- `recovered-source/jadx/sources/com/htmake/reader/api/controller/WebdavController$1$8.java`
- `recovered-source/jadx/sources/com/htmake/reader/api/controller/WebdavController$1$9.java`
- `recovered-source/jadx/sources/com/htmake/reader/api/controller/WebdavController.java`
- `recovered-source/jadx/sources/com/htmake/reader/api/controller/WebdavControllerKt.java`
- `recovered-source/jadx/sources/com/htmake/reader/config/AppConfig.java`
- `recovered-source/jadx/sources/com/htmake/reader/config/BookConfig.java`
- `recovered-source/jadx/sources/com/htmake/reader/db/DB.java`
- `recovered-source/jadx/sources/com/htmake/reader/db/JSONTable.java`
- `recovered-source/jadx/sources/com/htmake/reader/db/JSONTableKt.java`
- `recovered-source/jadx/sources/com/htmake/reader/db/SQLTable.java`
- `recovered-source/jadx/sources/com/htmake/reader/db/SQLTableKt.java`
- `recovered-source/jadx/sources/com/htmake/reader/entity/ActiveLicense.java`
- `recovered-source/jadx/sources/com/htmake/reader/entity/BasicError.java`
- `recovered-source/jadx/sources/com/htmake/reader/entity/License.java`
- `recovered-source/jadx/sources/com/htmake/reader/entity/MongoFile.java`
- `recovered-source/jadx/sources/com/htmake/reader/entity/Size.java`
- `recovered-source/jadx/sources/com/htmake/reader/entity/User.java`
- `recovered-source/jadx/sources/com/htmake/reader/init/ReaderAdapter.java`
- `recovered-source/jadx/sources/com/htmake/reader/init/appCtx.java`
- `recovered-source/jadx/sources/com/htmake/reader/lib/tts/constant/OutputFormat.java`
- `recovered-source/jadx/sources/com/htmake/reader/lib/tts/constant/TtsConstants.java`
- `recovered-source/jadx/sources/com/htmake/reader/lib/tts/constant/TtsStyleEnum.java`
- `recovered-source/jadx/sources/com/htmake/reader/lib/tts/constant/VoiceEnum.java`
- `recovered-source/jadx/sources/com/htmake/reader/lib/tts/exceptions/TtsException.java`
- `recovered-source/jadx/sources/com/htmake/reader/lib/tts/model/SSML.java`
- `recovered-source/jadx/sources/com/htmake/reader/lib/tts/model/SpeechConfig.java`
- `recovered-source/jadx/sources/com/htmake/reader/lib/tts/service/TTSService.java`
- `recovered-source/jadx/sources/com/htmake/reader/lib/tts/util/Tools.java`
- `recovered-source/jadx/sources/com/htmake/reader/utils/ExtKt.java`
- `recovered-source/jadx/sources/com/htmake/reader/utils/IntTypeAdapter.java`
- `recovered-source/jadx/sources/com/htmake/reader/utils/LRUCache.java`
- `recovered-source/jadx/sources/com/htmake/reader/utils/LongTypeAdapter.java`
- `recovered-source/jadx/sources/com/htmake/reader/utils/MongoManager.java`
- `recovered-source/jadx/sources/com/htmake/reader/utils/RemoteWebview.java`
- `recovered-source/jadx/sources/com/htmake/reader/utils/SpringContextUtils.java`
- `recovered-source/jadx/sources/com/htmake/reader/utils/UserMutex.java`
- `recovered-source/jadx/sources/com/htmake/reader/utils/VertExtKt.java`
- `recovered-source/jadx/sources/com/htmake/reader/verticle/RestVerticle$coroutineHandler$1$2.java`
- `recovered-source/jadx/sources/com/htmake/reader/verticle/RestVerticle$coroutineHandlerWithoutRes$1$2.java`
- `recovered-source/jadx/sources/com/htmake/reader/verticle/RestVerticle.java`
- `recovered-source/jadx/sources/com/htmake/reader/verticle/RestVerticleKt.java`
- `recovered-source/jadx/sources/io/legado/app/adapters/DefaultAdpater.java`
- `recovered-source/jadx/sources/io/legado/app/adapters/ReaderAdapterHelper.java`
- `recovered-source/jadx/sources/io/legado/app/adapters/ReaderAdapterInterface.java`
- `recovered-source/jadx/sources/io/legado/app/constant/Action.java`
- `recovered-source/jadx/sources/io/legado/app/constant/AppConst.java`
- `recovered-source/jadx/sources/io/legado/app/constant/AppPattern.java`
- `recovered-source/jadx/sources/io/legado/app/constant/BookType.java`
- `recovered-source/jadx/sources/io/legado/app/constant/DeepinkBookSource.java`
- `recovered-source/jadx/sources/io/legado/app/constant/PreferKey.java`
- `recovered-source/jadx/sources/io/legado/app/constant/RSSKeywords.java`
- `recovered-source/jadx/sources/io/legado/app/constant/Status.java`
- `recovered-source/jadx/sources/io/legado/app/data/entities/BaseBook.java`
- `recovered-source/jadx/sources/io/legado/app/data/entities/BaseSource.java`
- `recovered-source/jadx/sources/io/legado/app/data/entities/Book.java`
- `recovered-source/jadx/sources/io/legado/app/data/entities/BookChapter.java`
- `recovered-source/jadx/sources/io/legado/app/data/entities/BookGroup.java`
- `recovered-source/jadx/sources/io/legado/app/data/entities/BookKt.java`
- `recovered-source/jadx/sources/io/legado/app/data/entities/BookSource.java`
- `recovered-source/jadx/sources/io/legado/app/data/entities/Bookmark.java`
- `recovered-source/jadx/sources/io/legado/app/data/entities/Cache.java`
- `recovered-source/jadx/sources/io/legado/app/data/entities/Cookie.java`
- `recovered-source/jadx/sources/io/legado/app/data/entities/HttpTTS.java`
- `recovered-source/jadx/sources/io/legado/app/data/entities/ReplaceRule.java`
- `recovered-source/jadx/sources/io/legado/app/data/entities/RssArticle.java`
- `recovered-source/jadx/sources/io/legado/app/data/entities/RssSource.java`
- `recovered-source/jadx/sources/io/legado/app/data/entities/SearchBook.java`
- `recovered-source/jadx/sources/io/legado/app/data/entities/SearchKeyword.java`
- `recovered-source/jadx/sources/io/legado/app/data/entities/SearchResult.java`
- `recovered-source/jadx/sources/io/legado/app/data/entities/TxtTocRule.java`
- `recovered-source/jadx/sources/io/legado/app/data/entities/rule/BookInfoRule.java`
- `recovered-source/jadx/sources/io/legado/app/data/entities/rule/BookListRule.java`
- `recovered-source/jadx/sources/io/legado/app/data/entities/rule/ContentRule.java`
- `recovered-source/jadx/sources/io/legado/app/data/entities/rule/ExploreRule.java`
- `recovered-source/jadx/sources/io/legado/app/data/entities/rule/SearchRule.java`
- `recovered-source/jadx/sources/io/legado/app/data/entities/rule/TocRule.java`
- `recovered-source/jadx/sources/io/legado/app/exception/ConcurrentException.java`
- `recovered-source/jadx/sources/io/legado/app/exception/ContentEmptyException.java`
- `recovered-source/jadx/sources/io/legado/app/exception/NoStackTraceException.java`
- `recovered-source/jadx/sources/io/legado/app/exception/RegexTimeoutException.java`
- `recovered-source/jadx/sources/io/legado/app/exception/TocEmptyException.java`
- `recovered-source/jadx/sources/io/legado/app/help/BookHelp$saveImages$2$1$req$1.java`
- `recovered-source/jadx/sources/io/legado/app/help/BookHelp.java`
- `recovered-source/jadx/sources/io/legado/app/help/BytesEncodingDetect.java`
- `recovered-source/jadx/sources/io/legado/app/help/CacheManager.java`
- `recovered-source/jadx/sources/io/legado/app/help/DefaultData.java`
- `recovered-source/jadx/sources/io/legado/app/help/Encoding.java`
- `recovered-source/jadx/sources/io/legado/app/help/EncodingDetectHelp.java`
- `recovered-source/jadx/sources/io/legado/app/help/JsExtensions$ajaxAll$1$asyncArray$1$1.java`
- `recovered-source/jadx/sources/io/legado/app/help/JsExtensions$getZipByteArrayContent$bytes$1.java`
- `recovered-source/jadx/sources/io/legado/app/help/JsExtensions$queryTTF$font$1.java`
- `recovered-source/jadx/sources/io/legado/app/help/JsExtensions.java`
- `recovered-source/jadx/sources/io/legado/app/help/SourceAnalyzer.java`
- `recovered-source/jadx/sources/io/legado/app/help/coroutine/CompositeCoroutine.java`
- `recovered-source/jadx/sources/io/legado/app/help/coroutine/Coroutine$cancel$1$1.java`
- `recovered-source/jadx/sources/io/legado/app/help/coroutine/Coroutine.java`
- `recovered-source/jadx/sources/io/legado/app/help/coroutine/CoroutineContainer.java`
- `recovered-source/jadx/sources/io/legado/app/help/http/ByteConverter.java`
- `recovered-source/jadx/sources/io/legado/app/help/http/CookieStore.java`
- `recovered-source/jadx/sources/io/legado/app/help/http/CoroutinesCallAdapterFactory.java`
- `recovered-source/jadx/sources/io/legado/app/help/http/EncodeConverter.java`
- `recovered-source/jadx/sources/io/legado/app/help/http/HttpHelperKt.java`
- `recovered-source/jadx/sources/io/legado/app/help/http/OkHttpUtilsKt.java`
- `recovered-source/jadx/sources/io/legado/app/help/http/RequestMethod.java`
- `recovered-source/jadx/sources/io/legado/app/help/http/Res.java`
- `recovered-source/jadx/sources/io/legado/app/help/http/SSLHelper.java`
- `recovered-source/jadx/sources/io/legado/app/help/http/StrResponse.java`
- `recovered-source/jadx/sources/io/legado/app/help/http/api/CookieManager.java`
- `recovered-source/jadx/sources/io/legado/app/lib/icu4j/CharsetDetector.java`
- `recovered-source/jadx/sources/io/legado/app/lib/icu4j/CharsetMatch.java`
- `recovered-source/jadx/sources/io/legado/app/lib/icu4j/CharsetRecog_2022.java`
- `recovered-source/jadx/sources/io/legado/app/lib/icu4j/CharsetRecog_UTF8.java`
- `recovered-source/jadx/sources/io/legado/app/lib/icu4j/CharsetRecog_Unicode.java`
- `recovered-source/jadx/sources/io/legado/app/lib/icu4j/CharsetRecog_mbcs.java`
- `recovered-source/jadx/sources/io/legado/app/lib/icu4j/CharsetRecog_sbcs.java`
- `recovered-source/jadx/sources/io/legado/app/lib/icu4j/CharsetRecognizer.java`
- `recovered-source/jadx/sources/io/legado/app/model/Debug.java`
- `recovered-source/jadx/sources/io/legado/app/model/DebugKt.java`
- `recovered-source/jadx/sources/io/legado/app/model/DebugLog.java`

## Manifest

```text
Manifest-Version: 1.0
Main-Class: org.springframework.boot.loader.JarLauncher
Start-Class: com.htmake.reader.ReaderApplicationKt
Spring-Boot-Version: 2.1.6.RELEASE
Spring-Boot-Classes: BOOT-INF/classes/
Spring-Boot-Lib: BOOT-INF/lib/
```

## Bundled libraries

- `rhino-1.7.13-1.jar`
- `xmlpull-1.1.3.1.jar`
- `spring-boot-starter-2.1.6.RELEASE.jar`
- `vertx-lang-kotlin-3.8.5.jar`
- `vertx-lang-kotlin-coroutines-3.8.5.jar`
- `kotlinx-coroutines-slf4j-1.5.2.jar`
- `kotlinx-coroutines-core-jvm-1.5.2.jar`
- `kotlin-stdlib-jdk8-1.5.21.jar`
- `vertx-web-3.8.5.jar`
- `vertx-web-client-3.8.5.jar`
- `retrofit-vertx-1.1.3.jar`
- `vertx-web-common-3.8.5.jar`
- `vertx-auth-common-3.8.5.jar`
- `vertx-core-3.8.5.jar`
- `gson-2.8.5.jar`
- `jackson-module-kotlin-2.13.5.jar`
- `kotlin-logging-1.6.24.jar`
- `sysout-over-slf4j-1.0.2.jar`
- `guava-28.0-jre.jar`
- `logging-interceptor-4.1.0.jar`
- `retrofit-2.6.1.jar`
- `okhttp-4.9.1.jar`
- `JsoupXpath-2.5.0.jar`
- `jsoup-1.14.1.jar`
- `json-path-2.6.0.jar`
- `hutool-crypto-5.8.0.M1.jar`
- `mongodb-driver-sync-3.8.2.jar`
- `pdfbox-2.0.27.jar`
- `spring-boot-autoconfigure-2.1.6.RELEASE.jar`
- `spring-boot-2.1.6.RELEASE.jar`
- `spring-boot-starter-logging-2.1.6.RELEASE.jar`
- `javax.annotation-api-1.3.2.jar`
- `spring-context-5.1.8.RELEASE.jar`
- `spring-aop-5.1.8.RELEASE.jar`
- `spring-beans-5.1.8.RELEASE.jar`
- `spring-expression-5.1.8.RELEASE.jar`
- `spring-core-5.1.8.RELEASE.jar`
- `snakeyaml-1.23.jar`
- `kotlin-stdlib-jdk7-1.5.21.jar`
- `kotlin-reflect-1.5.21.jar`
- `okio-jvm-2.8.0.jar`
- `kotlin-stdlib-1.5.21.jar`
- `netty-handler-proxy-4.1.42.Final.jar`
- `netty-codec-http2-4.1.42.Final.jar`
- `netty-codec-http-4.1.42.Final.jar`
- `netty-handler-4.1.42.Final.jar`
- `netty-resolver-dns-4.1.42.Final.jar`
- `netty-codec-socks-4.1.42.Final.jar`
- `netty-codec-dns-4.1.42.Final.jar`
- `netty-codec-4.1.42.Final.jar`
- `netty-transport-4.1.42.Final.jar`
- `netty-buffer-4.1.42.Final.jar`
- `netty-resolver-4.1.42.Final.jar`
- `netty-common-4.1.42.Final.jar`
- `jackson-databind-2.9.9.jar`
- `jackson-core-2.9.9.jar`
- `vertx-bridge-common-3.8.5.jar`
- `logback-classic-1.2.3.jar`
- `log4j-to-slf4j-2.11.2.jar`
- `jul-to-slf4j-1.7.26.jar`
- `slf4j-api-1.7.26.jar`
- `jackson-annotations-2.9.0.jar`
- `kotlin-logging-common-1.6.24.jar`
- `failureaccess-1.0.1.jar`
- `listenablefuture-9999.0-empty-to-avoid-conflict-with-guava.jar`
- `jsr305-3.0.2.jar`
- `checker-qual-2.8.1.jar`
- `error_prone_annotations-2.3.2.jar`
- `j2objc-annotations-1.3.jar`
- `animal-sniffer-annotations-1.17.jar`
- `commons-lang3-3.8.1.jar`
- `antlr4-runtime-4.7.2.jar`
- `json-smart-2.4.7.jar`
- `hutool-core-5.8.0.M1.jar`
- `mongodb-driver-core-3.8.2.jar`
- `bson-3.8.2.jar`
- `fontbox-2.0.27.jar`
- `commons-logging-1.2.jar`
- `spring-jcl-5.1.8.RELEASE.jar`
- `annotations-13.0.jar`
- `kotlin-stdlib-common-1.5.30.jar`
- `accessors-smart-2.4.7.jar`
- `logback-core-1.2.3.jar`
- `log4j-api-2.11.2.jar`
- `asm-9.1.jar`
