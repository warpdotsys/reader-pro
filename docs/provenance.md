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

## Public references

- `changshengyu/reader-dev`: `fa22f271849d45f93349ae1636223e27b16a4691` — 2026-05-31 10:07:19 +0800 Improve reader cache warm start
- `hectorqin/reader-legado`: `d162b90398400b035da6645a2b3df6d6c81b9d72` — 2025-03-06 13:34:06 +0800 Update README.md

The public repositories are reference material only. When reference source and the 3.2.14 artifact differ, the artifact is treated as authoritative.

## Tooling limitation

The local shell did not have a Java runtime, javap, Maven, Gradle, Docker, or GitHub CLI available. The executable named `cfr` on PATH is a Python forensics tool, not the Java CFR decompiler. Therefore this pass preserves the full 3.2.14 binary artifact and recovered web/resources, and includes readable legacy source as reference. Full bytecode-to-source normalization remains a documented follow-up once a JDK/decompiler is available.

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

## Main top-level application classes

- `com.htmake.reader.ReaderApplication`
- `com.htmake.reader.ReaderApplicationKt`
- `com.htmake.reader.SpringEvent`
- `com.htmake.reader.api.ReturnData`
- `com.htmake.reader.api.YueduApi`
- `com.htmake.reader.api.YueduApiKt`
- `com.htmake.reader.api.controller.BaseController`
- `com.htmake.reader.api.controller.BaseControllerKt`
- `com.htmake.reader.api.controller.BookController`
- `com.htmake.reader.api.controller.BookControllerKt`
- `com.htmake.reader.api.controller.BookGroupController`
- `com.htmake.reader.api.controller.BookGroupControllerKt`
- `com.htmake.reader.api.controller.BookSourceController`
- `com.htmake.reader.api.controller.BookSourceControllerKt`
- `com.htmake.reader.api.controller.BookmarkController`
- `com.htmake.reader.api.controller.BookmarkControllerKt`
- `com.htmake.reader.api.controller.CURD`
- `com.htmake.reader.api.controller.CURDKt`
- `com.htmake.reader.api.controller.FileController`
- `com.htmake.reader.api.controller.FileControllerKt`
- `com.htmake.reader.api.controller.HttpTTSController`
- `com.htmake.reader.api.controller.LicenseController`
- `com.htmake.reader.api.controller.LicenseControllerKt`
- `com.htmake.reader.api.controller.ReplaceRuleController`
- `com.htmake.reader.api.controller.ReplaceRuleControllerKt`
- `com.htmake.reader.api.controller.RssSourceController`
- `com.htmake.reader.api.controller.RssSourceControllerKt`
- `com.htmake.reader.api.controller.UserController`
- `com.htmake.reader.api.controller.UserControllerKt`
- `com.htmake.reader.api.controller.WebdavController`
- `com.htmake.reader.api.controller.WebdavControllerKt`
- `com.htmake.reader.config.AppConfig`
- `com.htmake.reader.config.BookConfig`
- `com.htmake.reader.db.DB`
- `com.htmake.reader.db.JSONTable`
- `com.htmake.reader.db.JSONTableKt`
- `com.htmake.reader.db.SQLTable`
- `com.htmake.reader.db.SQLTableKt`
- `com.htmake.reader.entity.ActiveLicense`
- `com.htmake.reader.entity.BasicError`
- `com.htmake.reader.entity.License`
- `com.htmake.reader.entity.MongoFile`
- `com.htmake.reader.entity.Size`
- `com.htmake.reader.entity.User`
- `com.htmake.reader.init.ReaderAdapter`
- `com.htmake.reader.init.appCtx`
- `com.htmake.reader.lib.tts.constant.OutputFormat`
- `com.htmake.reader.lib.tts.constant.TtsConstants`
- `com.htmake.reader.lib.tts.constant.TtsStyleEnum`
- `com.htmake.reader.lib.tts.constant.VoiceEnum`
- `com.htmake.reader.lib.tts.exceptions.TtsException`
- `com.htmake.reader.lib.tts.model.SSML`
- `com.htmake.reader.lib.tts.model.SpeechConfig`
- `com.htmake.reader.lib.tts.service.TTSService`
- `com.htmake.reader.lib.tts.util.Tools`
- `com.htmake.reader.utils.ExtKt`
- `com.htmake.reader.utils.IntTypeAdapter`
- `com.htmake.reader.utils.LRUCache`
- `com.htmake.reader.utils.LongTypeAdapter`
- `com.htmake.reader.utils.MongoManager`
- `com.htmake.reader.utils.RemoteWebview`
- `com.htmake.reader.utils.SpringContextUtils`
- `com.htmake.reader.utils.UserMutex`
- `com.htmake.reader.utils.VertExtKt`
- `com.htmake.reader.verticle.RestVerticle`
- `com.htmake.reader.verticle.RestVerticleKt`
- `io.legado.app.adapters.DefaultAdpater`
- `io.legado.app.adapters.ReaderAdapterHelper`
- `io.legado.app.adapters.ReaderAdapterInterface`
- `io.legado.app.constant.Action`
- `io.legado.app.constant.AppConst`
- `io.legado.app.constant.AppPattern`
- `io.legado.app.constant.BookType`
- `io.legado.app.constant.DeepinkBookSource`
- `io.legado.app.constant.PreferKey`
- `io.legado.app.constant.RSSKeywords`
- `io.legado.app.constant.Status`
- `io.legado.app.data.entities.BaseBook`
- `io.legado.app.data.entities.BaseSource`
- `io.legado.app.data.entities.Book`
- `io.legado.app.data.entities.BookChapter`
- `io.legado.app.data.entities.BookGroup`
- `io.legado.app.data.entities.BookKt`
- `io.legado.app.data.entities.BookSource`
- `io.legado.app.data.entities.Bookmark`
- `io.legado.app.data.entities.Cache`
- `io.legado.app.data.entities.Cookie`
- `io.legado.app.data.entities.HttpTTS`
- `io.legado.app.data.entities.ReplaceRule`
- `io.legado.app.data.entities.RssArticle`
- `io.legado.app.data.entities.RssSource`
- `io.legado.app.data.entities.SearchBook`
- `io.legado.app.data.entities.SearchKeyword`
- `io.legado.app.data.entities.SearchResult`
- `io.legado.app.data.entities.TxtTocRule`
- `io.legado.app.data.entities.rule.BookInfoRule`
- `io.legado.app.data.entities.rule.BookListRule`
- `io.legado.app.data.entities.rule.ContentRule`
- `io.legado.app.data.entities.rule.ExploreRule`
- `io.legado.app.data.entities.rule.SearchRule`
- `io.legado.app.data.entities.rule.TocRule`
- `io.legado.app.exception.ConcurrentException`
- `io.legado.app.exception.ContentEmptyException`
- `io.legado.app.exception.NoStackTraceException`
- `io.legado.app.exception.RegexTimeoutException`
- `io.legado.app.exception.TocEmptyException`
- `io.legado.app.help.BookHelp`
- `io.legado.app.help.BytesEncodingDetect`
- `io.legado.app.help.CacheManager`
- `io.legado.app.help.DefaultData`
- `io.legado.app.help.Encoding`
- `io.legado.app.help.EncodingDetectHelp`
- `io.legado.app.help.JsExtensions`
- `io.legado.app.help.SourceAnalyzer`
- `io.legado.app.help.coroutine.CompositeCoroutine`
- `io.legado.app.help.coroutine.Coroutine`
- `io.legado.app.help.coroutine.CoroutineContainer`
- `io.legado.app.help.http.ByteConverter`
- `io.legado.app.help.http.CookieStore`
- `io.legado.app.help.http.CoroutinesCallAdapterFactory`
- `io.legado.app.help.http.EncodeConverter`
- `io.legado.app.help.http.HttpHelperKt`
- `io.legado.app.help.http.OkHttpUtilsKt`
- `io.legado.app.help.http.RequestMethod`
- `io.legado.app.help.http.Res`
- `io.legado.app.help.http.SSLHelper`
- `io.legado.app.help.http.StrResponse`
- `io.legado.app.help.http.api.CookieManager`
- `io.legado.app.lib.icu4j.CharsetDetector`
- `io.legado.app.lib.icu4j.CharsetMatch`
- `io.legado.app.lib.icu4j.CharsetRecog_2022`
- `io.legado.app.lib.icu4j.CharsetRecog_UTF8`
- `io.legado.app.lib.icu4j.CharsetRecog_Unicode`
- `io.legado.app.lib.icu4j.CharsetRecog_mbcs`
- `io.legado.app.lib.icu4j.CharsetRecog_sbcs`
- `io.legado.app.lib.icu4j.CharsetRecognizer`
- `io.legado.app.model.Debug`
- `io.legado.app.model.DebugKt`
- `io.legado.app.model.DebugLog`
- `io.legado.app.model.DebugLogKt`
- `io.legado.app.model.Debugger`
- `io.legado.app.model.DebuggerKt`
- `io.legado.app.model.analyzeRule.AnalyzeByJSonPath`
- `io.legado.app.model.analyzeRule.AnalyzeByJSoup`
- `io.legado.app.model.analyzeRule.AnalyzeByRegex`
- `io.legado.app.model.analyzeRule.AnalyzeByXPath`
- `io.legado.app.model.analyzeRule.AnalyzeRule`
- `io.legado.app.model.analyzeRule.AnalyzeRuleKt`
- `io.legado.app.model.analyzeRule.AnalyzeUrl`
- `io.legado.app.model.analyzeRule.QueryTTF`
- `io.legado.app.model.analyzeRule.RuleAnalyzer`
- `io.legado.app.model.analyzeRule.RuleData`
- `io.legado.app.model.analyzeRule.RuleDataInterface`
- `io.legado.app.model.localBook.CbzFile`
- `io.legado.app.model.localBook.EpubFile`
- `io.legado.app.model.localBook.EpubFileKt`
- `io.legado.app.model.localBook.LocalBook`
- `io.legado.app.model.localBook.PdfFile`
- `io.legado.app.model.localBook.TextFile`
- `io.legado.app.model.localBook.TextFileKt`
- `io.legado.app.model.localBook.UmdFile`
- `io.legado.app.model.rss.Rss`
- `io.legado.app.model.rss.RssParserByRule`
- `io.legado.app.model.rss.RssParserDefault`
- `io.legado.app.model.webBook.BookChapterList`
- `io.legado.app.model.webBook.BookContent`
- `io.legado.app.model.webBook.BookInfo`
- `io.legado.app.model.webBook.BookList`
- `io.legado.app.model.webBook.WebBook`
- `io.legado.app.model.webBook.WebBookKt`
- `io.legado.app.utils.ACache`
- `io.legado.app.utils.ACacheKt`
- `io.legado.app.utils.AnkoHelpsKt`
- `io.legado.app.utils.AttemptResult`
- `io.legado.app.utils.Base64`
- `io.legado.app.utils.EncoderUtils`
- `io.legado.app.utils.EncodingDetect`
- `io.legado.app.utils.FileExtensionsKt`
- `io.legado.app.utils.FileUtils`
- `io.legado.app.utils.GsonExtensionsKt`
- `io.legado.app.utils.HtmlFormatter`
- `io.legado.app.utils.IntJsonDeserializer`
- `io.legado.app.utils.JsonExtensionsKt`
- `io.legado.app.utils.JsoupExtensionsKt`
- `io.legado.app.utils.LogUtilsKt`
- `io.legado.app.utils.MD5Utils`
- `io.legado.app.utils.MapDeserializerDoubleAsIntFix`
- `io.legado.app.utils.NetworkUtils`
- `io.legado.app.utils.ParameterizedTypeImpl`
- `io.legado.app.utils.StringExtensionsKt`
- `io.legado.app.utils.StringUtils`
- `io.legado.app.utils.TextUtils`
- `io.legado.app.utils.ThrowableExtensionsKt`
- `io.legado.app.utils.UTF8BOMFighter`
- `io.legado.app.utils.Utf8BomUtils`
- `io.legado.app.utils.XmlUtils`
- `io.legado.app.utils.ZipUtils`
- `io.legado.app.utils.ZipUtilsKt`

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
