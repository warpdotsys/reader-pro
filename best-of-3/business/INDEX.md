# business/ ↔ src/ 对照索引

生成自 phase6。业务化文件优先阅读；细节对照反编译 `src/`。

| 类名 | business | src（反编译） |
|------|----------|---------------|
| ACache | `io/legado/app/utils/ACache.kt` | `io/legado/app/utils/ACache.kt`<br>`io/legado/app/utils/ACacheKt.kt` |
| AnalyzeByJSonPath | `io/legado/app/model/analyzeRule/AnalyzeByJSonPath.kt` | `io/legado/app/model/analyzeRule/AnalyzeByJSonPath.kt` |
| AnalyzeByJSoup | `io/legado/app/model/analyzeRule/AnalyzeByJSoup.kt` | `io/legado/app/model/analyzeRule/AnalyzeByJSoup.java` |
| AnalyzeByXPath | `io/legado/app/model/analyzeRule/AnalyzeByXPath.kt` | `io/legado/app/model/analyzeRule/AnalyzeByXPath.kt` |
| AnalyzeRule | `io/legado/app/model/analyzeRule/AnalyzeRule.kt` | `io/legado/app/model/analyzeRule/AnalyzeRule.kt`<br>`io/legado/app/model/analyzeRule/AnalyzeRuleKt.java` |
| AnalyzeUrl | `io/legado/app/model/analyzeRule/AnalyzeUrl.kt` | `io/legado/app/model/analyzeRule/AnalyzeUrl.kt` |
| AppConfig | `com/htmake/reader/config/AppConfig.kt` | `com/htmake/reader/config/AppConfig.java` |
| BaseController | `com/htmake/reader/api/controller/BaseController.kt` | `com/htmake/reader/api/controller/BaseControllerKt.kt`<br>`com/htmake/reader/api/controller/BaseController.java` |
| Book | `io/legado/app/data/entities/Book.kt` | `io/legado/app/data/entities/Book.java`<br>`io/legado/app/data/entities/BookKt.java` |
| BookChapter | `io/legado/app/data/entities/BookChapter.kt` | `io/legado/app/data/entities/BookChapter.java` |
| BookChapterList | `io/legado/app/model/webBook/BookChapterList.kt` | `io/legado/app/model/webBook/BookChapterList.kt` |
| BookContent | `io/legado/app/model/webBook/BookContent.kt` | `io/legado/app/model/webBook/BookContent.kt` |
| BookController | `com/htmake/reader/api/controller/BookController.kt`<br>`com/htmake/reader/api/controller/BookControllerExtras.kt` | `com/htmake/reader/api/controller/BookController.kt`<br>`com/htmake/reader/api/controller/BookControllerKt.kt` |
| BookGroupController | `com/htmake/reader/api/controller/BookGroupController.kt` | `com/htmake/reader/api/controller/BookGroupController.kt`<br>`com/htmake/reader/api/controller/BookGroupControllerKt.java` |
| BookHelp | `io/legado/app/help/BookHelp.kt` | `io/legado/app/help/BookHelp.kt` |
| BookInfo | `io/legado/app/model/webBook/BookInfo.kt` | `io/legado/app/model/webBook/BookInfo.kt` |
| BookList | `io/legado/app/model/webBook/BookList.kt` | `io/legado/app/model/webBook/BookList.kt` |
| BookSource | `io/legado/app/data/entities/BookSource.kt` | `io/legado/app/data/entities/BookSource.java` |
| BookSourceController | `com/htmake/reader/api/controller/BookSourceController.kt` | `com/htmake/reader/api/controller/BookSourceController.kt`<br>`com/htmake/reader/api/controller/BookSourceControllerKt.kt` |
| BookSourceDebug | `com/htmake/reader/api/controller/BookSourceDebug.kt` | — |
| BookmarkController | `com/htmake/reader/api/controller/BookmarkController.kt` | `com/htmake/reader/api/controller/BookmarkController.kt`<br>`com/htmake/reader/api/controller/BookmarkControllerKt.java` |
| CbzFile | `io/legado/app/model/localBook/CbzFile.kt` | `io/legado/app/model/localBook/CbzFile.kt` |
| CookieStore | `io/legado/app/help/http/CookieStore.kt` | JS cookie 绑定 |
| CacheManager | `io/legado/app/help/CacheManager.kt` | JS cache 绑定 |
| ContentProcessor | `io/legado/app/help/ContentProcessor.kt` | — |
| DebugLog | `io/legado/app/model/DebugLog.kt` | `io/legado/app/model/DebugLog.kt`<br>`io/legado/app/model/DebugLogKt.kt` |
| Debugger | `io/legado/app/model/Debugger.kt` | `io/legado/app/model/Debugger.kt`<br>`io/legado/app/model/DebuggerKt.kt` |
| DefaultData | `io/legado/app/help/DefaultData.kt` | `io/legado/app/help/DefaultData.java` |
| EdgeTts | `com/htmake/reader/lib/tts/EdgeTts.kt` | `lib/tts/service/TTSService` + SSML |
| EncoderUtils | `com/htmake/reader/utils/EncoderUtils.kt` | `io/legado/app/utils/EncoderUtils.kt` |
| EpubFile | `io/legado/app/model/localBook/EpubFile.kt` | `io/legado/app/model/localBook/EpubFile.kt`<br>`io/legado/app/model/localBook/EpubFileKt.kt` |
| Ext | `com/htmake/reader/utils/ExtKt.kt` | `com/htmake/reader/utils/ExtKt.kt` |
| FileController | `com/htmake/reader/api/controller/FileController.kt` | `com/htmake/reader/api/controller/FileController.kt`<br>`com/htmake/reader/api/controller/FileControllerKt.kt` |
| FileUtils | `io/legado/app/utils/FileUtils.kt` | `io/legado/app/utils/FileUtils.kt` |
| HtmlFormatter | `io/legado/app/utils/HtmlFormatter.kt` | `io/legado/app/utils/HtmlFormatter.kt` |
| HttpTTS | `io/legado/app/data/entities/HttpTTS.kt` | `io/legado/app/data/entities/HttpTTS.kt` |
| HttpTTSController | `com/htmake/reader/api/controller/HttpTTSController.kt` | `com/htmake/reader/api/controller/HttpTTSController.kt` |
| JsExtensions | `io/legado/app/help/JsExtensions.kt` | `io/legado/app/help/JsExtensions.kt` |
| License | `com/htmake/reader/entity/License.kt` | `com/htmake/reader/entity/License.java` |
| LicenseController | `com/htmake/reader/api/controller/LicenseController.kt` | `com/htmake/reader/api/controller/LicenseController.kt`<br>`com/htmake/reader/api/controller/LicenseControllerKt.kt` |
| LocalBook | `com/htmake/reader/api/controller/LocalBookApi.kt`<br>`io/legado/app/model/localBook/LocalBook.kt` | `io/legado/app/model/localBook/LocalBook.kt` |
| LocalMedia | `io/legado/app/model/localBook/LocalMedia.kt` | — |
| MD5Utils | `io/legado/app/utils/MD5Utils.kt` | `io/legado/app/utils/MD5Utils.kt` |
| MongoBackup | `com/htmake/reader/api/controller/MongoBackup.kt` | — |
| PdfFile | `io/legado/app/model/localBook/PdfFile.kt` | `io/legado/app/model/localBook/PdfFile.kt` |
| ReplaceRuleController | `com/htmake/reader/api/controller/ReplaceRuleController.kt` | `com/htmake/reader/api/controller/ReplaceRuleController.kt`<br>`com/htmake/reader/api/controller/ReplaceRuleControllerKt.java` |
| RestVerticle | `com/htmake/reader/verticle/RestVerticle.kt` | `com/htmake/reader/verticle/RestVerticle.kt`<br>`com/htmake/reader/verticle/RestVerticleKt.kt` |
| ReturnData | `com/htmake/reader/api/ReturnData.kt` | `com/htmake/reader/api/ReturnData.kt` |
| RssSourceController | `com/htmake/reader/api/controller/RssSourceController.kt` | `com/htmake/reader/api/controller/RssSourceController.kt`<br>`com/htmake/reader/api/controller/RssSourceControllerKt.java` |
| RuleAnalyzer | `io/legado/app/model/analyzeRule/RuleAnalyzer.kt` | `io/legado/app/model/analyzeRule/RuleAnalyzer.kt` |
| Rules | `io/legado/app/data/entities/rule/Rules.kt` | — |
| SearchResult | `io/legado/app/data/entities/SearchResult.kt` | `io/legado/app/data/entities/SearchResult.java` |
| SearchBook | `io/legado/app/data/entities/SearchBook.kt` | `io/legado/app/data/entities/SearchBook.java` |
| SourceAnalyzer | `io/legado/app/help/SourceAnalyzer.kt` | `io/legado/app/help/SourceAnalyzer.kt` |
| SpringContextUtils | `com/htmake/reader/utils/SpringContextUtils.kt` | `com/htmake/reader/utils/SpringContextUtils.java` |
| StrResponse | `io/legado/app/help/http/StrResponse.kt` | `io/legado/app/help/http/StrResponse.kt` |
| TextFile | `io/legado/app/model/localBook/TextFile.kt` | `io/legado/app/model/localBook/TextFile.java`<br>`io/legado/app/model/localBook/TextFileKt.java` |
| TocEmptyException | `io/legado/app/exception/TocEmptyException.kt` | `io/legado/app/exception/TocEmptyException.java` |
| TxtTocRule | `io/legado/app/data/entities/TxtTocRule.kt` | `io/legado/app/data/entities/TxtTocRule.kt` |
| UmdFile | `io/legado/app/model/localBook/UmdFile.kt` | `io/legado/app/model/localBook/UmdFile.kt` |
| UserConfig | `com/htmake/reader/config/UserConfig.kt` | userConfig.json 键与 defaults |
| User | `com/htmake/reader/entity/User.kt` | `com/htmake/reader/entity/User.java` |
| UserController | `com/htmake/reader/api/controller/UserController.kt`<br>`com/htmake/reader/api/controller/UserControllerExtras.kt` | `com/htmake/reader/api/controller/UserController.kt`<br>`com/htmake/reader/api/controller/UserControllerKt.kt` |
| UserMutex | `com/htmake/reader/utils/UserMutex.kt` | `com/htmake/reader/utils/UserMutex.kt` |
| VertExt | `com/htmake/reader/utils/VertExtKt.kt` | `com/htmake/reader/utils/VertExtKt.kt` |
| WebBook | `io/legado/app/model/webBook/WebBook.kt` | `io/legado/app/model/webBook/WebBook.kt`<br>`io/legado/app/model/webBook/WebBookKt.kt` |
| WebdavController | `com/htmake/reader/api/controller/WebdavController.kt` | `com/htmake/reader/api/controller/WebdavController.kt`<br>`com/htmake/reader/api/controller/WebdavControllerKt.kt` |
| BookExport | `com/htmake/reader/api/controller/BookExport.kt` | `BookController.exportBook` |
| BookTts | `com/htmake/reader/api/controller/BookTts.kt` | `textToSpeech` / `ttsByEdge` / `ttsByApi` |
| Rss | `io/legado/app/model/rss/Rss.kt` | `io/legado/app/model/rss/Rss.kt`<br>`RssParserByRule` / `RssParserDefault` |
| RssSource | `io/legado/app/data/entities/RssSource.kt` | `io/legado/app/data/entities/RssSource.java` |
| RssArticle | `io/legado/app/data/entities/RssArticle.kt` | `io/legado/app/data/entities/RssArticle.java` |
| WebdavPaths | `com/htmake/reader/api/controller/WebdavPaths.kt` | （业务新增） |
| Yuedu | `com/htmake/reader/api/YueduApi.kt` | `com/htmake/reader/api/YueduApi.kt`<br>`com/htmake/reader/api/YueduApiKt.kt` |
| ZipUtils | `io/legado/app/utils/ZipUtils.kt` | `io/legado/app/utils/ZipUtils.kt`<br>`io/legado/app/utils/ZipUtilsKt.kt` |

- business `.kt` 文件数: **72**
- src 源文件数: **321**

## 推荐阅读路径
1. `business/com/htmake/reader/api/YueduApi.kt`
2. `BookController.kt` + `BookControllerExtras.kt` + `BookSourceDebug.kt` + `LocalBookApi.kt`
3. `WebdavController.kt`
4. `analyzeRule/*` + `help/SourceAnalyzer.kt` + `help/ContentProcessor.kt`
5. `localBook/*`
6. 对照 `src/` 同名类

- phase8 后 business .kt 文件数: **72** / 约 **6152** 行

- phase9 后 business `.kt` 文件数: **75** / 约 **7335** 行

- phase10 后 business `.kt` 文件数: **78** / 约 **7814** 行

详见 **API_INDEX.md**（phase11 一页纸）。
- phase11 后 business `.kt` 文件数: **81** / 约 **8205** 行
