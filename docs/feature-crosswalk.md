# Feature crosswalk

The 3.2.14 JAR remains authoritative. The repositories below were cloned temporarily and used only as historical references; none of their files were copied over extracted 3.2.14 content.

## Reference commits

- `changshengyu/reader-dev` `fa22f271849d45f93349ae1636223e27b16a4691` (master), source/config/web files scanned: 276, class-name matches with JAR decompile: 209
- `hectorqin/reader-legado` `d162b90398400b035da6645a2b3df6d6c81b9d72` (main), source/config/web files scanned: 205, class-name matches with JAR decompile: 193

## Class-name overlap summary

### changshengyu/reader-dev

- Matched simple class names: 209
- JAR simple class names not found in this reference: 40
- Reference simple class names not present in JAR decompile: 5

Sample matched names:

- `ACache`
- `Action`
- `AnalyzeByJSonPath`
- `AnalyzeByJSoup`
- `AnalyzeByRegex`
- `AnalyzeByXPath`
- `AnalyzeRule`
- `AnalyzeUrl`
- `AnkoHelps`
- `AppConfig`
- `AppConst`
- `AppPattern`
- `Author`
- `BOMInputStream`
- `Base64`
- `BaseBook`
- `BaseController`
- `BaseSource`
- `BasicError`
- `Book`
- `BookChapter`
- `BookChapterList`
- `BookConfig`
- `BookContent`
- `BookController`
- `BookGroup`
- `BookHelp`
- `BookInfo`
- `BookInfoRule`
- `BookList`
- `BookListRule`
- `BookProcessor`
- `BookProcessorPipeline`
- `BookSource`
- `BookSourceController`
- `BookType`
- `Bookmark`
- `BookmarkController`
- `ByteConverter`
- `ByteOrderMark`

Sample JAR-only names:

- `ActiveLicense`
- `AttemptResult`
- `BookGroupController`
- `BytesEncodingDetect`
- `CURD`
- `DB`
- `DefaultAdpater`
- `Encoding`
- `FileController`
- `FileUtils`
- `HttpTTS`
- `HttpTTSController`
- `IntJsonDeserializer`
- `IntTypeAdapter`
- `JSONTable`
- `LRUCache`
- `License`
- `LicenseController`
- `LongTypeAdapter`
- `MapDeserializerDoubleAsIntFix`
- `MongoFile`
- `MongoManager`
- `OutputFormat`
- `ParameterizedTypeImpl`
- `PdfFile`
- `ReaderAdapter`
- `ReaderAdapterHelper`
- `ReaderAdapterInterface`
- `RemoteWebview`
- `SQLTable`
- `SSML`
- `SpeechConfig`
- `TTSService`
- `Tools`
- `TtsConstants`
- `TtsException`
- `TtsStyleEnum`
- `UserMutex`
- `VoiceEnum`
- `XmlUtils`

### hectorqin/reader-legado

- Matched simple class names: 193
- JAR simple class names not found in this reference: 56
- Reference simple class names not present in JAR decompile: 5

Sample matched names:

- `ACache`
- `Action`
- `AnalyzeByJSonPath`
- `AnalyzeByJSoup`
- `AnalyzeByRegex`
- `AnalyzeByXPath`
- `AnalyzeRule`
- `AnalyzeUrl`
- `AnkoHelps`
- `AppConst`
- `AppPattern`
- `Author`
- `BOMInputStream`
- `Base64`
- `BaseBook`
- `BaseSource`
- `Book`
- `BookChapter`
- `BookChapterList`
- `BookContent`
- `BookGroup`
- `BookHelp`
- `BookInfo`
- `BookInfoRule`
- `BookList`
- `BookListRule`
- `BookProcessor`
- `BookProcessorPipeline`
- `BookSource`
- `BookType`
- `Bookmark`
- `ByteConverter`
- `ByteOrderMark`
- `Cache`
- `CacheManager`
- `CbzFile`
- `CharsetDetector`
- `CharsetMatch`
- `CharsetRecog_2022`
- `CharsetRecog_UTF8`

Sample JAR-only names:

- `ActiveLicense`
- `AppConfig`
- `AttemptResult`
- `BaseController`
- `BasicError`
- `BookConfig`
- `BookController`
- `BookGroupController`
- `BookSourceController`
- `BookmarkController`
- `BytesEncodingDetect`
- `CURD`
- `DB`
- `DefaultAdpater`
- `Encoding`
- `FileController`
- `FileUtils`
- `HttpTTSController`
- `IntJsonDeserializer`
- `IntTypeAdapter`
- `JSONTable`
- `LRUCache`
- `License`
- `LicenseController`
- `LongTypeAdapter`
- `MapDeserializerDoubleAsIntFix`
- `MongoFile`
- `MongoManager`
- `OutputFormat`
- `ParameterizedTypeImpl`
- `ReaderAdapter`
- `ReaderApplication`
- `RemoteWebview`
- `ReplaceRuleController`
- `RestVerticle`
- `ReturnData`
- `RssSourceController`
- `SQLTable`
- `SSML`
- `Size`

## Decompiled controller/API mapping evidence

The following mapping annotations were found in JADX output. This is audit evidence from the JAR, not a rebuilt API specification.


## Feature presence evidence from 3.2.14 JAR

Representative class/file paths matching planned feature areas:

### Book
- `com/htmake/reader/api/YueduApi.java`
- `com/htmake/reader/api/controller/BaseController.java`
- `com/htmake/reader/api/controller/BookController$backupFileNames$2.java`
- `com/htmake/reader/api/controller/BookController$backupToMongodb$handler$1.java`
- `com/htmake/reader/api/controller/BookController$bookSourceDebugSSE$debugger$1.java`
- `com/htmake/reader/api/controller/BookController$cacheBookOnServer$$inlined$CoroutineExceptionHandler$1.java`
- `com/htmake/reader/api/controller/BookController$getAvailableBookSource$$inlined$toDataClass$1.java`
- `com/htmake/reader/api/controller/BookController$getBookContent$$inlined$toDataClass$1.java`
- `com/htmake/reader/api/controller/BookController$getBookCover$$inlined$CoroutineExceptionHandler$1.java`
- `com/htmake/reader/api/controller/BookController$getBookCover$2$result$1.java`
- `com/htmake/reader/api/controller/BookController$getBookInfo$$inlined$toDataClass$1.java`
- `com/htmake/reader/api/controller/BookController$getChapterList$$inlined$toDataClass$1.java`

### Rss
- `com/htmake/reader/api/controller/RssSourceControllerKt$logger$1.java`
- `com/htmake/reader/api/controller/RssSourceControllerKt.java`
- `com/htmake/reader/api/controller/WebdavController$webdavList$formatter$1.java`
- `com/htmake/reader/init/ReaderAdapter.java`
- `com/htmake/reader/utils/RemoteWebview.java`
- `io/legado/app/adapters/DefaultAdpater.java`
- `io/legado/app/adapters/ReaderAdapterInterface.java`
- `io/legado/app/constant/RSSKeywords.java`
- `io/legado/app/data/entities/BaseSource.java`
- `io/legado/app/data/entities/Cookie.java`
- `io/legado/app/data/entities/HttpTTS.java`
- `io/legado/app/data/entities/RssArticle$variableMap$2$invoke$$inlined$fromJsonObject$1.java`

### User
- `com/htmake/reader/api/controller/BaseController$checkAuth$$inlined$toDataClass$1.java`
- `com/htmake/reader/api/controller/BaseController$formatUser$$inlined$toDataClass$1.java`
- `com/htmake/reader/api/controller/BaseController$getUserInfoClass$$inlined$toDataClass$1.java`
- `com/htmake/reader/api/controller/CURD.java`
- `com/htmake/reader/api/controller/HttpTTSController.java`
- `com/htmake/reader/api/controller/LicenseController.java`
- `com/htmake/reader/api/controller/ReplaceRuleController.java`
- `com/htmake/reader/api/controller/UserController$forEachUser$lambda7$lambda6$$inlined$toDataClass$1.java`
- `com/htmake/reader/api/controller/UserController$login$$inlined$toDataClass$1.java`
- `com/htmake/reader/api/controller/UserControllerKt$logger$1.java`
- `com/htmake/reader/api/controller/UserControllerKt.java`
- `com/htmake/reader/api/controller/WebdavController$checkAuthorization$$inlined$toDataClass$1.java`

### WebDav
- `com/htmake/reader/api/YueduApi$initRouter$webdavController$1.java`
- `com/htmake/reader/api/controller/WebdavController$1$10.java`
- `com/htmake/reader/api/controller/WebdavController$1$2.java`
- `com/htmake/reader/api/controller/WebdavController$1$3.java`
- `com/htmake/reader/api/controller/WebdavController$1$4.java`
- `com/htmake/reader/api/controller/WebdavController$1$5.java`
- `com/htmake/reader/api/controller/WebdavController$1$6.java`
- `com/htmake/reader/api/controller/WebdavController$1$7.java`
- `com/htmake/reader/api/controller/WebdavController$1$8.java`
- `com/htmake/reader/api/controller/WebdavController$1$9.java`
- `com/htmake/reader/api/controller/WebdavControllerKt$logger$1.java`
- `com/htmake/reader/api/controller/WebdavControllerKt.java`

### File
- `com/htmake/reader/api/controller/FileControllerKt$logger$1.java`
- `com/htmake/reader/api/controller/FileControllerKt.java`
- `com/htmake/reader/entity/MongoFile.java`
- `com/htmake/reader/utils/MongoManager.java`
- `com/htmake/reader/verticle/RestVerticle.java`
- `io/legado/app/constant/AppConst$fileNameFormat$2.java`
- `io/legado/app/help/BytesEncodingDetect.java`
- `io/legado/app/help/DefaultData$txtTocRules$2.java`
- `io/legado/app/help/DefaultData.java`
- `io/legado/app/help/EncodingDetectHelp.java`
- `io/legado/app/help/http/SSLHelper.java`
- `io/legado/app/model/analyzeRule/QueryTTF.java`

### TTS
- `com/htmake/reader/lib/tts/constant/OutputFormat.java`
- `com/htmake/reader/lib/tts/constant/TtsConstants.java`
- `com/htmake/reader/lib/tts/constant/TtsStyleEnum.java`
- `com/htmake/reader/lib/tts/constant/VoiceEnum.java`
- `com/htmake/reader/lib/tts/exceptions/TtsException.java`
- `com/htmake/reader/lib/tts/model/SSML.java`
- `com/htmake/reader/lib/tts/model/SpeechConfig.java`
- `com/htmake/reader/lib/tts/service/TTSService.java`
- `com/htmake/reader/lib/tts/util/Tools.java`
- `io/legado/app/constant/Action.java`

### License
- `com/htmake/reader/api/controller/LicenseController$checkLicense$$inlined$CoroutineExceptionHandler$1.java`
- `com/htmake/reader/api/controller/LicenseController$checkLicense$2$result$1.java`
- `com/htmake/reader/api/controller/LicenseController$importLicense$$inlined$CoroutineExceptionHandler$1.java`
- `com/htmake/reader/api/controller/LicenseController$importLicense$2$result$1.java`
- `com/htmake/reader/api/controller/LicenseController$webClient$2.java`
- `com/htmake/reader/api/controller/LicenseControllerKt$logger$1.java`
- `com/htmake/reader/api/controller/LicenseControllerKt.java`
- `com/htmake/reader/utils/ExtKt$decryptToLicense$lambda19$$inlined$toDataClass$1.java`

### Security
- `io/legado/app/help/http/SSLHelper$unsafeSSLSocketFactory$2.java`
- `io/legado/app/help/http/SSLHelper$unsafeTrustManager$1.java`
- `io/legado/app/utils/EncoderUtils.java`
- `io/legado/app/utils/MD5Utils.java`

### Mongo
- `com/htmake/reader/ReaderApplication.java`

### Db
- `com/htmake/reader/db/JSONTableKt$logger$1.java`
- `com/htmake/reader/db/JSONTableKt.java`
- `com/htmake/reader/db/SQLTableKt$logger$1.java`
- `com/htmake/reader/db/SQLTableKt.java`
- `io/legado/app/lib/icu4j/CharsetRecog_mbcs.java`
- `io/legado/app/lib/icu4j/CharsetRecog_sbcs.java`
- `io/legado/app/utils/JsonExtensionsKt.java`
- `me/ag2s/umdlib/tool/StreamReader.java`
- `org/kxml2/wap/WbxmlParser.java`

### Remote
- `com/htmake/reader/utils/RemoteWebview$getStrResponse$strResponse$1.java`
- `me/ag2s/epublib/domain/ManifestItemProperties.java`
- `me/ag2s/epublib/epub/EpubProcessorSupport.java`

### Epub
- `com/htmake/reader/ReaderApplicationKt$logger$1.java`
- `com/htmake/reader/ReaderApplicationKt.java`
- `com/htmake/reader/api/ReturnData.java`
- `com/htmake/reader/api/YueduApiKt$logger$1.java`
- `com/htmake/reader/api/controller/BaseControllerKt$logger$1.java`
- `com/htmake/reader/api/controller/CURDKt$logger$1.java`
- `com/htmake/reader/api/controller/ReplaceRuleControllerKt$logger$1.java`
- `com/htmake/reader/entity/BasicError.java`
- `com/htmake/reader/entity/Size.java`
- `com/htmake/reader/init/appCtx$cacheDir$2.java`
- `com/htmake/reader/init/appCtx.java`
- `com/htmake/reader/utils/ExtKt$gson$1.java`

### Umd
- `me/ag2s/umdlib/domain/UmdEnd.java`
- `me/ag2s/umdlib/tool/WrapOutputStream.java`

### Group
- `io/legado/app/utils/Base64.java`

## Web comparison boundary

- JAR web assets are preserved under `recovered-assets/web/` and are authoritative.
- No source maps were present, so `reconstructed-web/` contains only audit aids.
- Historical web source in `changshengyu/reader-dev` can guide future manual reconstruction, but it must not replace hashed JAR bundles unless a reproducible build proves equivalence.
