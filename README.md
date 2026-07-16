# reader-pro 3.2.14 逆向工程报告

> **原则**：原 JAR `H:\下载\reader-pro-3.2.14.jar` **未做任何修改**。本目录全部为只读提取与 CFR 反编译产物。

## 1. 产物概览

| 路径 | 说明 |
|------|------|
| `decompiled-src/` | CFR 0.152 反编译的 Java 源码（276 文件 / ~9.6 万行） |
| `extracted-classes/` | 从 JAR 抽出的 `BOOT-INF/classes`（936 个 .class + 资源） |
| `resources/` | 非 class 资源副本（yml、前端 web、simple-web 等） |
| `app-classes.jar` | 仅含应用 class 的中间包（供 CFR 使用） |
| `API_ROUTES.md` | 全部 `/reader3/*` API 路由（133 条） |
| `DEPENDENCIES.md` | BOOT-INF/lib 依赖清单（85 个 jar） |
| `SOURCE_INVENTORY.md` | 反编译文件清单与行数 |
| `tools/` | 便携 JDK 17 + CFR（可删） |

## 2. 应用身份

| 项 | 值 |
|----|-----|
| 类型 | Spring Boot fat JAR（可执行） |
| Spring Boot | 2.1.6.RELEASE |
| Main-Class | `org.springframework.boot.loader.JarLauncher` |
| Start-Class | `com.htmake.reader.ReaderApplicationKt` |
| 语言 | **Kotlin** 1.5.x（反编译为 Java 形态） |
| 模块名 | `reader-pro`（见 `@Metadata`） |
| Banner | `READER` ASCII art |
| 默认端口 | `8080`（`application.yml` / `RestVerticle.port`） |

本质是 **「阅读」/legado 书源体系的 Web 服务端 Pro 版**：后端 Kotlin + Vert.x HTTP，前端 Vue（`web/`），并内嵌 legado 规则引擎、本地书解析、EPUB/UMD 等。

## 3. 技术栈

### 3.1 运行时与框架
- **Spring Boot 2.1.6**（`@SpringBootApplication`，排除 Mongo 自动配置）
- **Vert.x 3.8.5**（`CoroutineVerticle` + `Router`，真正 HTTP 服务）
- **Kotlin Coroutines**（控制器大量 suspend / Continuation）
- **Spring Scheduling**（书架更新、授权检查、备份、GC 等定时任务）

### 3.2 业务核心库（打进 classes，非独立 lib）
- `io.legado.app.*` — 书源/规则解析/网络书/本地书/RSS（来自阅读/legado 移植）
- `me.ag2s.epublib.*` — EPUB
- `me.ag2s.umdlib.*` — UMD
- `org.kxml2.*` — XML

### 3.3 主要第三方依赖（见 DEPENDENCIES.md）
- Web/HTTP: vertx-web, okhttp, retrofit, jsoup, JsoupXpath, json-path
- 脚本: rhino-1.7.13
- 数据: mongodb-driver-sync（可选）、gson、jackson-module-kotlin
- 文档: pdfbox
- 工具: guava、hutool-crypto、kotlin-logging

## 4. 架构分层

```
                    ┌─────────────────────────────┐
                    │  Vue SPA (web/) + simple-web │
                    └──────────────┬──────────────┘
                                   │ HTTP :8080
                    ┌──────────────▼──────────────┐
                    │ RestVerticle (Vert.x Router) │
                    │   Session / Body / CORS /    │
                    │   StaticHandler              │
                    └──────────────┬──────────────┘
                                   │
                    ┌──────────────▼──────────────┐
                    │ YueduApi.initRouter()        │
                    │  注册 /reader3/* 133 路由    │
                    └──────────────┬──────────────┘
           ┌───────────┬───────────┼───────────┬───────────┐
           ▼           ▼           ▼           ▼           ▼
     BookController  UserCtrl  BookSource  LicenseCtrl  Webdav...
           │
           ▼
     io.legado.app.model.webBook / analyzeRule / localBook
           │
           ▼
     JSON 文件存储 (storage/data)  或  MongoDB（可选）
```

### 启动链路
1. `ReaderApplicationKt.main` → `SpringApplication.run(ReaderApplication.class)`
2. `ReaderApplication.@PostConstruct deployVerticle()`  
   - 注册 Jackson Kotlin module  
   - 部署 `YueduApi` Verticle
3. `YueduApi.initRouter`：静态资源 + `/reader3/*` API + 定时任务

### 配置（`resources/application.yml`）
- `reader.app.workDir` 工作目录
- `secure` / `inviteCode` / `secureKey` 多用户安全模式
- `userLimit` / `userBookLimit` 配额
- `mongoUri` 可选 Mongo 备份/存储
- `cacheChapterContent` 章节缓存
- `remoteWebviewApi` 远程 WebView（复杂书源）

## 5. 包结构（应用代码）

### 5.1 `com.htmake.reader`（服务端壳）
| 包 | 职责 |
|----|------|
| `api/YueduApi` | 路由表中枢（~7930 行反编译） |
| `api/controller/*` | 业务 API 实现 |
| `verticle/RestVerticle` | Vert.x 协程 Verticle 基类 |
| `config` | AppConfig / BookConfig |
| `db` | JSONTable / SQLTable 简易持久化 |
| `entity` | User / License / ActiveLicense / MongoFile |
| `utils` | Ext、MongoManager、RemoteWebview、LRUCache… |
| `lib/tts` | Azure 风格 TTS（SSML / VoiceEnum） |
| `init` | ReaderAdapter 适配 legado |

**控制器清单**
- `BookController` — 书架/搜索/章节/正文/缓存/导出（最大，~11706 行）
- `BookSourceController` — 书源 CRUD / 远程导入
- `BookGroupController` / `BookmarkController`
- `UserController` — 登录、用户管理、配置
- `LicenseController` — 授权/激活/密钥（Pro 特性）
- `RssSourceController` / `ReplaceRuleController`
- `FileController` / `WebdavController` / `HttpTTSController`
- `BaseController` / `CURD` — 公共基类

### 5.2 `io.legado.app`（阅读引擎）
| 包 | 职责 |
|----|------|
| `model/webBook` | 网络书：搜索/详情/目录/正文 |
| `model/analyzeRule` | 规则引擎 AnalyzeRule（CSS/XPath/JSON/JS） |
| `model/localBook` | TXT/EPUB/等本地书 |
| `model/rss` | RSS 源 |
| `data/entities` | Book、BookSource、规则实体 |
| `help/http` | HTTP 客户端封装 |
| `utils` | 工具集 |

## 6. API 总览

前缀统一为 **`/reader3/`**，共 **133** 条（完整列表见 `API_ROUTES.md`）。

### 分类摘要
| 模块 | 代表接口 |
|------|----------|
| 系统 | `GET /reader3/getSystemInfo` |
| 书源 | save/get/delete BookSource(s)、远程导入、调试 SSE |
| 书架 | getBookshelf、saveBook、deleteBook(s) |
| 阅读 | searchBook、getBookInfo、getChapterList、getBookContent、cover |
| 缓存/导出 | cacheBookSSE、exportBook、searchBookContent |
| 分组/书签 | BookGroup / Bookmark CRUD |
| 用户 | login/logout、用户 CRUD、密码、配置 |
| 授权 Pro | getLicense、importLicense、activateLicense、generateKeys/License |
| RSS | getRssSources 等 |
| 文件/WebDAV | uploadFile、backupToWebdav |
| TTS | `/reader3/book/tts` |
| Mongo | backupToMongodb / restoreFromMongodb |

### 静态资源
- `/*` → classpath `web/`（主前端）
- `/assets/*` → `storage/assets`
- `/book-assets/*`、`/epub/*` → 用户数据目录
- `/simple-web/*` → 轻量前端

## 7. 数据与存储

- 默认 **本地文件**：`workDir/storage/...`（用户数据、书源 JSON、章节缓存）
- 可选 **MongoDB**（`mongoUri` 非空时初始化 `MongoManager`）
- 会话：Vert.x `LocalSessionStore` + Cookie
- 持久化抽象：`JSONTable` / `SQLTable`

## 8. Pro 与授权相关（逆向可见）

实体：`License`、`ActiveLicense`  
控制器：`LicenseController`  
定时：`YueduApi.checkLicense`  
接口：生成密钥/证书、导入、激活、校验 host、邮件补发等。

> 说明：反编译结果足以阅读授权流程与字段，但 Kotlin 协程状态机 + 加密细节会使逻辑显得破碎；若目标是研究授权，应重点阅读 `LicenseController.java` 与 `entity/License*.java`。

## 9. 反编译质量说明

| 现象 | 原因 |
|------|------|
| 源码是 Java 不是 Kotlin | CFR 输出 JVM 字节码对应的 Java |
| 大量 `Continuation` / `label` switch | Kotlin 协程状态机 |
| `*Kt.java` 文件 | Kotlin 顶层函数/文件类 |
| 936 class → 276 java | lambda、协程、companion 合成类被合并或跳过 |
| `/* Unable to fully structure code */` | 控制流无法完美还原 |
| 依赖类 “Could not load” 注释 | 反编译时未挂 lib 到 classpath（不影响主体逻辑阅读） |

**建议阅读顺序**
1. `ReaderApplicationKt` / `ReaderApplication`
2. `YueduApi`（路由表）
3. `BaseController` → 各 Controller
4. `io.legado.app.model.webBook.WebBook` + `analyzeRule.AnalyzeRule`
5. `resources/application.yml` + `web/index.html`

## 10. 复现本逆向的命令（可选）

```powershell
# 已在 tools/ 准备好 jdk + cfr 的前提下
$java = ".\tools\jdk17\jdk-17.0.19+10\bin\java.exe"
& $java -jar .\tools\cfr-0.152.jar .\app-classes.jar --outputdir .\decompiled-src --caseinsensitivefs true
```

## 11. 法律与用途声明

本报告仅用于**学习、安全审计、兼容对接**等合法目的。`reader-pro` 为商业/授权软件组件时，请遵守其许可协议；请勿将逆向结果用于绕过授权或未授权分发。

---
生成日期: 2026-07-16  
工具: Eclipse Temurin 17 + CFR 0.152  
原文件 SHA/大小: 见下方
- 原 JAR 大小: 72913887 bytes
- SHA256: B26FB4769D689D98FF26408CE79A275D719F360906C84ACF52FF404E98030C8C
- 最后修改: 04/20/2026 00:10:55

