# business API 对照一页纸

> 生成自 phase11。完整 133 路由见仓库根 `API_ROUTES.md`。  
> 实现优先读 `business/`；边界对照 `best-of-3/src/`。

## 核心模块

| 领域 | business 入口 | 关键能力 |
|------|---------------|----------|
| 路由 | `YueduApi.kt` | `/reader3/*` 挂载 |
| 书架/正文 | `BookController` + `BookControllerExtras` | 搜索/目录/正文/缓存 SSE/导出 |
| 书源 | `BookSourceController` | CRUD、远程导入、失效缓存 |
| 用户 | `UserController` + Extras | 登录、userConfig 主题、上传 |
| TTS | `BookTts` + `EdgeTts` | edge / api / textToSpeechCn |
| WebDAV | `WebdavController` + `WebdavPaths` | PROPFIND… + 备份 zip |
| 规则引擎 | `AnalyzeRule` / `AnalyzeUrl` | CSS/XPath/JSON/JS/Regex + Cookie |
| 列表 | `BookList` | 搜索 + 发现 + exploreUrl 分类 |
| 替换 | `ContentProcessor` + `ReplaceRuleController` | scope/timeout/bookName |
| RSS | `Rss` + `RssSourceController` | 规则 / 默认 XML |
| 本地书 | `LocalBook` / Epub/Txt/Pdf/Cbz | 目录与正文 |

## 用户配置键（userConfig）

见 `com/htmake/reader/config/UserConfig.kt`：`theme` `fontFamily` `fontSize` `pageMode` `ttsType` `searchConcurrent` …

## 书源登录

1. `loginUrl` 为 `@js:` / `<js>` → `BaseSource.login()`  
2. `loginHeader_` + bookSourceUrl 存 `CacheManager`  
3. `getHeaderMap(withLogin=true)` 合并 loginHeader  
4. `AnalyzeUrl` 自动带 Cookie，并写回 `Set-Cookie`

## Cookie 路径

`storage/cache/cookie/{user}/` — 按 subdomain 文件（ACache）

## 失效书源

`storage/cache/invalidBookSourceCache/{user}/` — TTL 600s  
目录拉取失败、多源搜索异常时写入；搜索时跳过。

## 替换规则 scope

| scope | 作用 |
|-------|------|
| content / 空 | 正文 |
| title | 章节标题 |
| all | 正文 + 标题 |
| timeout | 单规则毫秒，默认 3000 |
| bookName | 书名包含；`regex:…` 或 `/pat/` |

## 发现 explore

- `GET/POST /reader3/exploreBook?bookSourceUrl=&url=&page=`  
- `url` 空：返回 `exploreUrl` 解析的分类 title/url  
- `url` / `sortUrl`：抓取列表，`ruleExplore` 优先  

## 路由摘录（133 条来自 API_ROUTES）

```
GET    /reader3/getSystemInfo
GET    /reader3/getBookshelf
GET    /reader3/getShelfBook
POST   /reader3/saveBook
POST   /reader3/deleteBook
POST   /reader3/deleteBooks
POST   /reader3/exploreBook
GET    /reader3/exploreBook
GET    /reader3/searchBook
POST   /reader3/searchBook
GET    /reader3/searchBookMulti
POST   /reader3/searchBookMulti
GET    /reader3/searchBookMultiSSE
GET    /reader3/getBookInfo
POST   /reader3/getBookInfo
GET    /reader3/getChapterList
POST   /reader3/getChapterList
GET    /reader3/getBookContent
POST   /reader3/getBookContent
POST   /reader3/saveBookContent
POST   /reader3/saveBookProgress
GET    /reader3/cover
POST   /reader3/importBookPreview
POST   /reader3/refreshLocalBook
GET    /reader3/getTxtTocRules
POST   /reader3/getChapterListByRule
GET    /reader3/cacheBookSSE
POST   /reader3/cacheBookOnServer
GET    /reader3/getShelfBookWithCacheInfo
POST   /reader3/deleteBookCache
POST   /reader3/exportBook
GET    /reader3/exportBook
GET    /reader3/searchBookContent
POST   /reader3/searchBookContent
POST   /reader3/book/saveBookConfig
GET    /reader3/file/list
GET    /reader3/file/get
POST   /reader3/file/save
POST   /reader3/file/mkdir
GET    /reader3/file/download
…
```

完整列表：`../../API_ROUTES.md`
