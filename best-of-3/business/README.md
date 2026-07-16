# business/ — 业务化重写源码

从 `reader-pro-3.2.14.jar` 逆向后的 **可读业务层 Kotlin**（约 43 文件 / 3000+ 行）。

## 两棵树怎么用

| 路径 | 内容 |
|------|------|
| `best-of-3/src/` | 反编译择优 + 硬伤修补（贴近字节码，含协程状态机） |
| **`best-of-3/business/`** | **业务化重写**：suspend 顺序逻辑、中文错误文案、路由与领域清晰 |

**读业务 / 做对接：优先 business/**  
**对某一条指令或边界：回看 src/ 或 full-cfr/**

## 模块清单

### 服务端
- `YueduApi` — 路由表（核心 `/reader3/*` 已挂；完整 133 条见仓库根 `API_ROUTES.md`）
- `RestVerticle` — HTTP / Session / Body / CORS
- `ReturnData`、`BaseController`（鉴权、用户空间、limitConcurrent）
- `AppConfig`、`User` / `License` 实体
- `ExtKt` / `UserMutex` / `VertExtKt` / `SpringContextUtils`

### 控制器（业务方法已顺序化）
| 控制器 | 职责 |
|--------|------|
| **BookController** | 书架、搜索、详情、目录、正文、进度、缓存、封面、WebDAV 同步、TTS |
| **UserController** | 登录注册、登出、用户 CRUD、配置 |
| **BookSourceController** | 书源 CRUD、默认书源、远程导入占位 |
| **LicenseController** | 授权导入/校验/激活（加密细节见 jar） |
| **FileController** | 本地文件浏览/上传/下载 |
| **WebdavController** | 备份到 WebDAV |
| **RssSourceController** | RSS 源 |
| **BookGroup / Bookmark / ReplaceRule** | 分组、书签、替换规则 |

### 阅读引擎
- `WebBook` 门面
- `BookList` / `BookInfo` / `BookChapterList` / `BookContent`
- `AnalyzeRule` / `AnalyzeUrl`（API 面；底层 CSS/XPath/JS 完整实现仍在 `src/` 反编译大文件）
- `BookHelp.saveImage`、`LocalBook` 占位

## 生成脚本

```
_business_rewrite_core.py
_business_rewrite_more.py
_business_book_controller.py
_business_utils_entities.py
```

## 说明与边界

1. **不是**可直接编译的完整工程（依赖版本、部分方法体为语义还原）。
2. **BookController** 从 ~1 万行状态机压成约 500 行业务逻辑；冷门分支见 `src/`。
3. **AnalyzeRule 内核**（JSoup/XPath/JSON/Rhino）建议继续读 `src/io/legado/app/model/analyzeRule/*`。
4. **License 加签验签** 仅结构级；具体 RSA/hutool 调用在 jar 与 `src` 的 LicenseController。

## 建议阅读顺序

1. `YueduApi.kt`（路由）  
2. `BaseController.kt` + `UserController.kt`  
3. `BookController.kt`  
4. `BookSourceController.kt`  
5. `webBook/*` + `BookChapterList` / `BookContent`  
6. 对照 `../src/` 同名类补细节  
'''


## Phase 2 增量

- **YueduApi**：按 `API_ROUTES.md` 挂载 **133 路由**（SSE/文件流单独处理）
- **BookControllerExtras**：explore/multi/SSE/cache/export/mongo/tts 等
- **HttpTTSController**、Rss 文章接口
- **AnalyzeRule**：Mode 分发（Js/Regex/Json/XPath/Default）+ JSoup 实现
- **JsExtensions**：ajax/connect/base64/md5/aes/file 等书源 JS API
- **LicenseController + EncoderUtils**：RSA 密钥、分段加解密、activate 落盘


## Phase 3 增量

- **AnalyzeByXPath**：seimicrawler JXDocument + `&&/||/%%` 规则拆分（RuleAnalyzer）
- **SourceAnalyzer**：legado/旧版书源 JSON → BookSource 规范化；`BookSource.fromJson` 接入
- **searchBookMulti / SSE**：多书源协程并发搜索与事件流
- **LocalBook + TextFile**：本地书分发；TXT 目录正则切分与按 offset 取正文
- **MongoBackup**：按用户备份/恢复 JSON 文档到 MongoDB


## Phase 4 增量

- **EpubFile**：epublib 读 spine/TOC + 章节 HTML（Jsoup 去 script/style）
- **CbzFile / PdfFile / UmdFile**：分页/条目目录与正文
- **AnalyzeByJSoup**：`&&` / `||` / `%%` 与属性 `@text/@html/@href`
- **searchBookMulti**：默认并发 **36**，SSE **24**，单源超时 **15s**
- **DefaultData + txtTocRule.json**：内置目录正则库；TextFile 自动选最优规则


## Phase 5 增量

- **EpubFile**：`getChapterListBySpinAndToc` / `getChapterListByTocAndSpin` 标题合并；默认目录用 spine 顺序 + TOC 标题
- **LocalMedia**：CBZ 条目图 / PDF 页渲染 JPG / EPUB 封面与内嵌图字节流
- **SourceAnalyzer.toNewRule**：完整 `-`/`+`、`#`→`##`、`|`→`||`、`&`→`&&` 旧规则迁移
- **HtmlFormatter.formatKeepImg**
- **LocalBookApi**：本地书导入预览 + 章节图/封面 HTTP 流式输出


## Phase 6 增量

- **WebdavController**：PROPFIND/MKCOL/PUT/GET/DELETE/MOVE/COPY/LOCK/UNLOCK + zip 备份
- **Debugger + bookSourceDebugSSE**：搜索→详情→目录→正文 逐步 SSE 日志
- **ContentProcessor**：`replaceRule.json` 应用到 `getBookContent`
- **INDEX.md**：business ↔ src 类名对照索引


## Phase 7 增量

- **WebdavPaths + MOVE/COPY**：Destination 按 URL.path 解析并去掉 `/reader3/webdav`，Overwrite 语义对齐 jar（缺省 412）
- **Debugger**：`http(s)` 详情 / `::` 发现 / `++` 目录 / `--` 正文 / 默认搜索
- **ContentProcessor**：timeout（默认 3s）、bookName 过滤、title/content 作用域
- **BookExport**：`exportToTxt` / `exportToEpub`（全文拉取 + 替换规则）


## Phase 8 增量

- **exportBook** 接线 `BookExport`（txt/epub）
- **cacheBookSSE**：并发默认 24，进度 SSE（success/failed/cached/total）
- **saveBookContent / deleteBookCache / getShelfBookWithCacheInfo**
- **getChapterListByRule**：本地 txt/epub/pdf 按规则重切目录
- **searchBookContent**：章节内关键字检索（分页 lastIndex/size）
- **saveBookGroupId / addBookGroupMulti / removeBookGroupMulti / saveBookConfig**
- **setBookSource / searchBookSourceSSE / getInvalidBookSources**
- **Rss** 引擎 + **RssSourceController**：规则解析 / 默认 RSS·Atom XML
- **Book**：`group`、`pdfImageWidth` 字段


## Phase 9 增量

- **TTS**：`textToSpeech` / `ttsByEdge`（TTSService 反射）/ `ttsByApi`（HttpTTS）/ `ttsByTextToSpeechCn`
- **EdgeTts**：SSML 构造 + jar `TTSService.sendText` 对接
- **saveFromRemoteSource**：OkHttp 拉取远程书源 JSON → `saveBookSources`
- **SearchResult** + `searchChapter` / `searchPosition` / `getResultAndQueryIndex`
- **syncFromWebdav**：恢复 books 目录 + bookProgress 合并
- **saveToWebdav / createUserBackup / getLastBackFileFromWebdav**
- **backupToWebdav** 打包 books 镜像
- **HttpTTS.name** + 按 name 删除


## Phase 10 增量

- **UserConfig**：主题/字体/翻页/TTS/并发等键；`saveUserConfig` 写 `@updateTime`，支持 `merge=true`
- **getUserConfig**：无文件时「没有备份文件」+ defaults
- **uploadFile / deleteFile**：用户 assets 上传与安全删除
- **invalidBookSource 缓存**：`addInvalidBookSource` TTL 600s；目录失败写入；多源搜索跳过/标记
- **getInvalidBookSources**：按 ACache hash 读取
- **AnalyzeRule**：allInOne `:`、`<js>`/`@js` 拆分、`##`/`###` 替换、`@put`、`put/get`、evalJS 全绑定
- **CookieStore / CacheManager**：JS `cookie` / `cache` 绑定
- **ACache**：过期头 + `getByHashCode`


## Phase 11 增量

- **CookieStore**：磁盘 ACache 按 subdomain 持久化；`replaceCookie` / Set-Cookie 合并
- **BaseSource**：loginUrl / loginCheckJs / loginHeader / getHeaderMap(withLogin)
- **BookSource**：exploreUrl、enabledExplore、login* 字段；`setUserNameSpace`
- **AnalyzeUrl**：请求带 Cookie；响应写 Cookie；loginCheckJs 钩子
- **BookList.explore**：ruleExplore 字段回落 ruleSearch；allInOne 列表；`parseExploreUrl`
- **exploreBook API**：无 url 时返回发现分类；支持 sortUrl
- **ReplaceRuleController**：按 name 增改删；normalize scope/timeout
- **ContentProcessor**：scope=`all`；bookName 支持 `regex:` 与 `/pat/`
- **API_INDEX.md**：业务一页纸对照

## Phase 12 增量（与 src/main 对齐）

- **YueduApi**：按 API_ROUTES.md 挂满 **133** 路由
- **BookMore**：cache SSE/导出/TTS/正文检索/分组/封面/本地预览
- **MongoBackup** / ControllerMore：书源批量、文件上传、用户资产
- **前端**：src/main/resources/web + simple-web 从 jar 资源回填

## Phase 13 深度

- Xsoup XPath + &&/||/%%
- EpubFile OPF spine/NCX/nav
- EdgeTts + SmokeTest

