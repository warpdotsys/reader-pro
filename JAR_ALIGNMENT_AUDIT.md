# reader-pro 3.2.14 JAR 对齐与代码库审计

审计日期：2026-07-20
审计基线：`301c294`（PR #32 合并后的 `main`）
目标制品：`reader-pro-3.2.14.jar`
状态：本批实现恢复完成；整体 JAR 对齐进行中；安全问题仅记录、暂不修复

## 1. 审计原则

- 原始 JAR 的 class、Kotlin Metadata、字节码和资源是行为事实源。
- 社区源码只用于恢复源码结构，不能覆盖 JAR 已证明的差异。
- 当前优先级是恢复原 JAR 的接口和行为，不顺手修复继承自原 JAR 的安全问题。
- 安全问题统一记录在本文第 8 节，等待完成 JAR 对齐后单独处理。
- 本报告覆盖 class/resource 清单对账、共同类型成员对账、构建与测试、重点运行路径复现，以及高风险安全模式检查；它不等同于完整渗透测试。

## 2. 结论

当前源码只是一个可编译的阶段性子闭包，尚不能替代原 JAR：

1. `clean test` 有 56/56 通过，但 `bootJar` 仍无法确定主类并直接失败。
2. 目标应用与 Legado 范围仍缺 21 个顶层符号，集中在入口、配置、剩余业务 controller 和远程 WebView 运行栈。
3. 本批恢复 `HttpTTSController`；其公开 descriptor 已与原 JAR 零差异对账。
4. EPUB 所需 DTD、模板、KXml 源码和 provider 已恢复，标准 NCX 读取与 XML serializer 均有回归测试覆盖。
5. CI 仍只执行 `clean test`，无法阻止不可执行制品进入 `main`。

因此，绿色 CI 目前只证明“现有源码子集可编译并通过少量单元测试”，不证明 JAR 对齐或应用可运行。

## 3. 覆盖统计

### 3.1 顶层类型

| 口径 | 原 JAR | 当前编译产物 | 差异 |
| --- | ---: | ---: | ---: |
| 全部顶层 class | 276 | 274 | 缺 21，多 19 |
| 项目与 Legado 顶层符号 | 198 | 177 | 缺 21 |
| `me.ag2s` vendored 类型 | 67 | 67 | 已覆盖 |
| `org.kxml2` vendored 类型 | 11 | 11 | 已恢复 |

当前多出的 19 个类型均为有意恢复到源码树的 `com.script` Rhino 类型；它们在原制品中位于嵌套依赖 JAR，不属于行为偏差。

按目标 class 的 `SourceFile` 聚合后，项目与 Legado 范围共有 163 个源码单元。当前功能上覆盖 151 个，仍有 12 个 Kotlin 源码单元未恢复。已映射的 255 个共同顶层类型语言归属全部一致：152 Kotlin、103 Java，未发现 Java/Kotlin 边界错配。

### 3.2 资源

| 口径 | 原 JAR | 当前 `src/main/resources` | 差异 |
| --- | ---: | ---: | ---: |
| 非 class 资源 | 159 | 159 | 目标缺 1、额外 1 |
| 共同路径 | 158 | 158 | 153 字节相同，2 个仅换行不同，3 个实质不同 |

前序批次恢复了 68 个目标资源：50 个 `dtd/**`、6 个 `epub/**`、6 个图标、`images/loading.gif`、根目录 `bookSourceDebug/**`、KXml service provider 与 `simplelogger.properties`；全部新增资源 SHA-256 与目标一致。

唯一未放入源码资源树的目标路径是 `META-INF/reader-pro.kotlin_module`，它由 Kotlin 编译器生成；当前额外路径 `reader3-routes.txt` 是测试用路由清单。上表采用稳定的 Git blob 口径；Windows checkout 因 `core.autocrlf=true` 表现为 69 个字节相同、86 个仅换行不同、3 个实质不同。3 个实质资源差异见第 9 节。

## 4. P0：运行与打包阻塞

### 4.1 无应用入口

执行：

```text
./gradlew bootJar --console=plain
```

结果：

```text
Main class name has not been configured and it could not be resolved
```

当前缺少原 JAR 的 `ReaderApplication`、`ReaderApplicationKt.main`、`YueduApi` 和 `RestVerticle`。`Dockerfile` 会调用同一个失败的 `bootJar` 任务，因此当前也无法按文档构建镜像。

不能通过添加空壳 `main` 消除错误；入口必须在依赖栈恢复后按目标字节码恢复。

### 4.2 缺失运行栈

主要缺失类型按依赖域归类如下：

- 启动与配置：`ReaderApplication`、`BookConfig`、`ReaderAdapter`、`YueduApi`、`RestVerticle`。
- API：8 个业务 controller（`BaseController`、`CURD`、4 个低耦合数据 controller 与 `HttpTTSController` 已恢复）。
- 支撑工具：`RemoteWebview`。

当前编译产物仍未闭合这 21 个入口与 API 符号，说明工程尚未恢复完整 HTTP 运行闭包。

## 5. P1：已完成批次的回炉项

| 文件/单元 | 已确认偏差 | 状态 |
| --- | --- | --- |
| `Relator.java` | 第 470 行包含真实的 `…1693 tokens truncated…` 反编译伪影，约 34 个枚举常量被吞进注释 | 已修复；220 个常量、顺序与 code/description 对账通过 |
| `License.kt` | 缺 `isValid()`、`validHost(String)`、`toActiveLicense()` | 已修复 |
| `EncoderUtils.kt` | 缺整套 RSA 密钥生成、四种公私钥 API及分段加解密实现 | 已修复 |
| `SearchBook.kt` | `origins` 缺 `private set`，额外暴露 `setOrigins` | 已修复 |
| `appCtx.kt` | 目标调用 `ExtKt.getWorkDir("storage", "cache")`；当前改走 adapter | 已随 `ExtKt` 回正 |
| `VertExt.kt` | 目标使用 compact、注册 Int/Long adapter 的 `ExtKt.gson`；当前使用 pretty `GSON`，数值 JSON 语义不同 | 已随 `ExtKt` 回正 |
| `EncodingDetect` | HTML charset fallback、`String?` 返回与 `File?` 私有参数同目标 Metadata 不一致 | 已修复 |
| `EncodingDetectHelp`、`EncodeConverter`、协程、独立实体/工具 | 14 个目标 `SourceFile` 单元未恢复或不完整 | 已恢复；公开 descriptor 与目标对账通过 |
| `org.kxml2` | KXml parser/serializer/provider 未进入运行闭包 | 已恢复 11 个目标同源 Java 单元 |
| `ExtKt`、`SpringContextUtils`、`MongoManager` | 目标工具、Spring 上下文和 Mongo 存储运行栈缺失 | 已恢复；公开 descriptor 与目标对账通过 |
| `DB`、`JSONTable`、`SQLTable`、`User` | 目标持久化基础与用户实体缺失 | 已恢复；保留目标批量替换与顺序删除行为 |
| `BaseController`、`CURD` | 会话、命名空间、用户存储和通用 JSON 表 CRUD 契约缺失 | 已恢复；目标 4 个顶层 class 与公开/受保护 descriptor 零差异，新增签名和转换回归测试 |
| `BookGroupController`、`BookmarkController`、`ReplaceRuleController`、`RssSourceController` | 低耦合数据 controller 及其 Kotlin facade 缺失 | 已恢复；目标 8 个顶层 class 与公开/受保护 descriptor 零差异，覆盖默认分组、去重键、校验、排序保存和 RSS CRUD/抓取契约 |
| `HttpTTSController` | 通用 CURD 的专用 HTTP TTS 数据控制器缺失 | 已恢复；目标公开 descriptor 零差异对账，覆盖 JSON 解析、名称去重、名称/链接校验与通用 CURD 契约 |
| `JsExtensions.kt` | 当前额外声明 `getCookie(String)`；目标仅有 `getCookie(String, String?)` | 已修复；反射 descriptor 测试覆盖 |
| `AnalyzeRule`、`WebBook`、`Debugger`、RSS 栈 | 在线书籍与 RSS 运行闭包缺失；旧参考源码不含目标命名空间和 logger 传播 | 已恢复；13 个目标顶层类公开/受保护 descriptor 零差异，目标 RSS provider 类名声明失败行为有回归测试 |

其余 descriptor 差异中包含 Kotlin 1.9 生成的 `EnumEntries`、lambda 名称、synthetic accessor 等编译器差异，不能直接当作源码缺陷。每项必须结合反汇编行为再分类。

## 6. EPUB 运行闭包（已恢复）

### 6.1 DTD 资源

`EpubProcessorSupport.EntityResolverImpl` 会把远程 DTD URL 映射到 classpath，例如：

```text
http://www.daisy.org/z3986/2005/ncx-2005-1.dtd
-> dtd/www.daisy.org/z3986/2005/ncx-2005-1.dtd
```

此前资源不存在时会抛出 `remote resource is not cached`。本批已恢复 50 个目标 DTD 和 6 个 EPUB 资源；标准 EPUB/NCX DTD 路径现在可由 classpath 解析。

### 6.2 XML serializer provider

此前只有 `xmlpull` API，`XmlPullParserFactory.newInstance()` 会因 provider 资源缺失失败。现已恢复目标的 11 个 `org.kxml2` Java 源码和 `META-INF/services/org.xmlpull.v1.XmlPullParserFactory`，`EpubProcessorSupport.createXmlSerializer()` 返回 `org.kxml2.io.KXmlSerializer`。

### 6.3 验证

- `EpubRuntimeResourcesTest` 验证 serializer 非空、最小 XML 输出和 NCX DTD classpath 解析。
- 57 个 EPUB/DTD 资源的 SHA-256 与目标一致；另有 11 个图标、调试页和日志资源同样逐路径对账。
- KXml 源码与目标同源的 upstream `v2.5.0` 对齐（仅规范化了源码尾随空白）；重建后的 class major 仍随项目 JDK 17 产生，不作为源码行为偏差。

## 7. 构建、测试与代码质量

### 7.1 测试缺口

- 当前有 13 个测试类、56 个测试，`clean test` 为 56/56 通过。
- 本批新增数据 controller 的 descriptor、分组 ID 分配、书签/替换规则去重键和输入校验回归测试。
- 本批新增 `AnalyzeRule` 命名空间/脚本作用域测试、WebBook ABI/mask/异常边界测试，以及 Debugger/RSS descriptor 与原版失败行为测试。
- 目标 `RssParserDefault` 把两个 KXml provider 类名写入没有逗号的多行字符串，而目标同版 `xmlpull-1.1.3.1` 只按逗号分隔；因此默认 RSS XML 解析会抛 `XmlPullParserException`。当前测试有意锁定该原版失败，不把它误报为已修复。
- `LocalBook`、TXT、EPUB、CBZ、PDF、UMD、`AnalyzeUrl` 和 WebBook 的真实 HTTP/分页主要分支仍缺回归覆盖。

建议按风险优先补充：

1. EPUB 的完整读取/写入 fixture，而非当前 DTD 和 serializer smoke test。
2. CBZ `ComicInfo.xml`、PDF 有/无 outline、TXT 编码与章节偏移。
3. UMD golden fixture。
4. `AnalyzeUrl` 的请求体、分页、Cookie、代理和 WebView 分支。
5. 应用恢复后增加启动 smoke test 和路由清单对账。

### 7.2 CI 缺口

`.github/workflows/build.yml` 目前只运行 `clean test`。完整运行栈恢复后，合并门禁应至少执行：

```text
./gradlew --no-daemon clean test bootJar
```

并对生成 JAR 进行入口启动 smoke test。入口恢复前保留 `bootJar` 失败作为已知 P0，不能把它描述为可发布状态。

### 7.3 编译告警与文档偏差

- Java 有 100 条 `-Xlint` 告警；其中新增的 50 条来自目标同源 KXml 旧版源码的 raw/deprecated API。
- Kotlin 在 `allWarningsAsErrors=true` 下无告警。
- `README.md`、`DOCKER.md` 和 Docker 构建说明声称应用可打包运行，与当前状态不符。
- `deploy/docker-compose.user.yaml` 注释和 `UmdCover.java` 注释存在 mojibake；目前未发现其改变运行行为。

## 8. 安全问题（记录，暂不修复）

以下问题均保留原 JAR 或当前恢复代码的行为。本轮不修改，以免在兼容性闭合前引入额外漂移。

### S-01 Rhino 书源脚本可访问任意 Java 宿主能力（严重）

- 位置：`RhinoClassShutter.java:18-38`、`BaseSource.kt:138-146`、`AppConst.kt:15-16`。
- 证据：class shutter 仅拒绝 `java.lang.Runtime`、`java.io.File`、`java.security.AccessController` 三个精确类名，其他类默认可见。已用当前引擎实证 `java.lang.System.getProperty('user.dir')` 可读出工作目录，`java.lang.ProcessBuilder` 可解析为 Java class。
- 影响：恶意或被劫持的书源 JavaScript 可访问 `System`、`ProcessBuilder`、`java.nio.file.*` 等宿主 API，形成服务端命令执行、文件读写与环境信息泄露风险。当前入口栈尚未恢复；恢复后该行为会重新可达。
- 处置：目标 JAR 继承行为，暂缓；后续安全 PR 应采用 allowlist ClassShutter 和最小化脚本 bindings。

### S-02 书源脚本文件 API 可逃逸缓存目录（高）

- 位置：`JsExtensions.kt:296-346`，经 `BaseSource` 暴露给书源脚本。
- 证据：`getFile(path)` 直接拼接 `cachePath + File.separator + path`，未做 canonical containment；`readFile`、`readTxtFile`、`deleteFile` 和 `unzipFile` 复用该路径。
- 影响：传入 `..\\` 可越过缓存目录读取、删除或写入进程可访问的路径；与 S-01 组合时攻击面更大。
- 处置：目标 JAR 继承行为，暂缓。

### S-03 全局关闭 TLS 证书与主机名校验（高）

- 位置：`SSLHelper.kt:23-41`、`HttpHelper.kt:21-29`，以及复用不安全 socket factory 的 `JsExtensions` 请求路径。
- 证据：`checkServerTrusted` 为空实现，accepted issuers 为空，hostname verifier 恒为 `true`。
- 影响：HTTPS 请求可被中间人拦截或篡改，Cookie、Authorization、正文和书源脚本均可能暴露。
- 处置：继承自目标行为，暂缓。

### S-04 CBZ `ComicInfo.xml` 可触发 XXE（中）

- 位置：`XmlUtils.kt:12-19`，调用点 `CbzFile.kt:66-69`。
- 证据：默认 `DocumentBuilderFactory` 未关闭 DOCTYPE、外部通用实体和外部参数实体，直接解析导入压缩包中的 XML。
- 影响：恶意 CBZ 在被导入和读取元数据时可能访问本地文件或发起外部请求；具体数据回传能力取决于后续使用路径。
- 处置：继承自目标行为，暂缓。

### S-05 ZIP 解压路径穿越（高，目标 HTTP 栈恢复后可远程触发）

- 位置：`Ext.kt:140-180`；另一路位于 `ZipUtils.kt:132-186`、`JsExtensions.kt:333-346`。
- 证据：`File.unzip(descDir)` 直接以 `descDir + separator + entry.name` 创建文件，对 entry 名称完全不校验。目标 `BookController.extractEpub/extractCbz` 调用该方法，保存图书路由接收客户端提供的 EPUB/CBZ。`ZipUtils` 一路只拒绝 `../`，未拒绝 Windows `..\`、绝对路径、UNC/盘符路径，也未做 canonical containment；但当前 `JsExtensions` 调用会先被尚未恢复的 `FileUtils.getCachePath()` 阻断。
- 影响：入口和 controller 恢复后，恶意压缩包可把文件写到目标目录之外并覆盖进程可写文件；默认 `secure=false` 时无需认证。当前源码尚无 HTTP 入口，因此暂不能远程触发。
- 处置：继承自目标行为，暂缓。

### S-06 调试 HTTP BODY 日志可能泄露敏感信息（中）

- 位置：`HttpHelper.kt:76-79`。
- 证据：传入 `DebugLog` 时启用 `HttpLoggingInterceptor.Level.BODY`。
- 影响：请求/响应头和正文可能包含 Cookie、Authorization、代理凭据、账号信息和书源返回内容。
- 处置：暂缓；后续应按 header/body 做定向脱敏，而不是简单关闭所有调试能力。

### S-07 500 响应暴露内部异常与完整 URI（中）

- 位置：`VertExt.kt:16-31`。
- 证据：错误响应包含解码后的 `absoluteURI`、`throwable.toString()` 和 `throwable.message`。
- 影响：查询参数和内部类名、路径或下游错误信息可能返回给客户端。
- 处置：目标 JAR 原有行为，暂缓。

### S-08 MDC traceId 未清理（低）

- 位置：`VertExt.kt:34-41`。
- 证据：请求处理前 `MDC.put`，处理完成或异常后没有 `remove/clear`。
- 影响：在线程复用时可能发生跨请求日志上下文污染，降低审计可靠性。
- 处置：目标 JAR 原有行为，暂缓。

### S-09 文件与压缩内容存在无上限整块读取（中）

- 位置：`JsExtensions.kt`、`ZipUtils.kt`、`ACache.kt` 等多处 `readBytes()`。
- 证据：若干用户文件、压缩 entry 和脚本可达文件路径在读入内存前没有大小上限。
- 影响：超大文件或高膨胀压缩内容可能导致内存耗尽和服务不可用。
- 处置：暂缓；后续需结合各格式的兼容上限逐项修复。

### S-10 目标用户密码使用快速双 MD5（高，目标栈恢复后可达）

- 位置：目标反编译 `ExtKt.java:1045-1048`、`UserController.java:199-201`、`UserController.java:230-231`；`ExtKt/User` 已恢复，`UserController` 与入口栈尚未恢复，因此暂未通过 HTTP API 可达。
- 证据：目标实现为 `MD5(MD5(password + salt) + salt)`，用户创建与校验使用 8 字符 salt。
- 影响：数据库泄露后可被高速离线枚举，salt 长度和双轮 MD5 都不能提供现代密码哈希的工作因子。
- 处置：这是目标行为，`ExtKt` 已按原样保留；恢复 `UserController` 时也不得静默改成新哈希，以免与既有数据和 JAR 行为不兼容。后续安全 PR 需要设计迁移兼容层。

### S-11 源码内硬编码 SMTP 账号与密码（高，凭据已暴露）

- 位置：`Ext.kt:715-739`、`Ext.kt:766-778`；目标调用链 `LicenseController.sendCodeToEmail -> ExtKt.sendEmail`，目标路由为 `POST /reader3/sendCodeToEmail`。
- 证据：SMTP 主机固定为 `smtp.qiye.aliyun.com:465`，`AUTH LOGIN` 使用源码内明文账号和密码；该路由目标实现未调用 `checkAuth`。未尝试使用凭据登录或验证其当前有效性。
- 影响：凭据必须按已泄露处理；若仍有效，可被用于未授权发信、垃圾邮件或账号滥用。当前 `LicenseController/YueduApi` 尚未恢复，当前构建无远程调用入口。
- 处置：为保持原 JAR 行为本轮不改代码；凭据应立即在服务端轮换/吊销。兼容性闭合后的安全 PR 应删除硬编码值并只从受保护配置或密钥管理服务读取。

### S-12 默认配置关闭 HTTP 认证（高，入口恢复后可达）

- 位置：`application.yml:7`；目标反编译 `BaseController.checkAuth`。
- 证据：默认 `reader.app.secure=false`，目标 `checkAuth` 在该值下直接返回 `true`。业务、书源、文件和调试端点因而默认不要求 token。
- 影响：入口栈恢复后，未显式覆盖默认配置的部署会把高权限 API 暴露给网络调用者，并放大 S-01、S-05 等问题的远程攻击面。
- 处置：目标默认行为，暂缓修改；运行部署必须显式启用认证。后续安全 PR 需要改为安全默认值，并为升级兼容与首次初始化设计迁移路径。

### S-13 MongoDB 连接 URI 记录到日志（中）

- 位置：`MongoManager.kt:18-23`；目标启动调用链 `YueduApi -> MongoManager.connect(AppConfig.mongoUri)`。
- 证据：连接异常时把完整 URI 作为日志参数输出；标准 MongoDB URI 可内含用户名和密码。
- 影响：连接失败会把数据库凭据写入应用日志，扩大日志读取者、采集系统和备份中的秘密暴露范围。当前入口尚未恢复，因此启动调用链暂不可达。
- 处置：目标行为，暂缓；后续安全 PR 应只记录去除 user-info 与敏感查询参数后的地址。

### S-14 通用文件 API 可逃逸根目录并任意读写删除（严重，目标栈恢复后可达）

- 位置：目标反编译 `FileController.java:100-205`、`:299-300`、`:432-458`、`:561-573`、`:656-658`、`:734`、`:812`、`:902-909`、`:989-990`；路由注册见 `YueduApi.java:5539-6057`。
- 证据：controller 选择 `__HOME__`、`__WEBDAV__`、`__LOCAL_STORE__` 或 `__STORAGE__` 根后，直接以 `File(root + path/filename)` 处理 list、upload、download、get、save、mkdir、delete 和 deleteMulti；没有 canonical containment。目标 `checkAuth/checkManagerAuth` 在默认 `secure=false` 时放行。旧 WebDAV、LocalStore 和 User 上传路径存在同类拼接。
- 影响：入口恢复后，默认部署中的远程调用者可用 `..`、绝对路径或平台特定路径逃逸预期根目录，读取、覆盖或删除进程权限范围内的文件，并可与配置、密钥或启动文件组合扩大影响。签名部署若保存 `storage/data/privateKey.key`，该问题还可与 S-16 组合导出许可私钥。
- 处置：目标行为，暂缓；恢复时不得误写为已修复。后续安全 PR 应统一使用解析后的 canonical/normalized path 做根目录 containment，并在路径解析前强制认证和授权。

### S-15 入站完整 URI、请求正文与 WebDAV Basic 凭据写入日志（高，目标栈恢复后可达）

- 位置：目标反编译 `RestVerticle.java:283-288`、`WebdavController.java:66-85`。
- 证据：`RestVerticle` 以 INFO 记录完整请求 URI 和小于 1000 字节的请求正文，查询 token 与登录请求中的明文密码会进入日志；`WebdavController` 以 INFO 记录完整 `Authorization: Basic ...` 头。该问题不同于 S-06 的出站调试 HTTP 日志。
- 影响：应用日志、集中采集、告警和备份会持久化访问 token、账号密码或可解码的 Basic 凭据，扩大低权限运维读者和日志系统被攻破后的横向风险。
- 处置：目标行为，暂缓；后续安全 PR 应对认证路由和敏感 header/body 做结构化拒绝记录或不可逆脱敏。

### S-16 无认证许可证签发与密钥生成端点（严重，目标栈恢复后可达）

- 位置：目标反编译 `YueduApi.java:4113-4269`、`LicenseController.java:338-490`。
- 证据：`GET/POST /reader3/generateLicense` 未调用 `checkAuth/checkManagerAuth`，只比较一个可从 JAR 恢复的硬编码共享 key（本报告不复述该值），随后读取服务器私钥并按客户端提供的 host、有效期、用户上限、openApi、实例数和类型签发许可。`/reader3/generateKeys` 同样无认证并返回完整新公私钥对。
- 影响：部署存在签名私钥时，公开共享 key 等同于远程签名 oracle，攻击者可生成任意授权许可；密钥生成端点还会向匿名调用者返回私钥材料。若签名私钥落在默认存储路径，S-14 还可能导出该私钥并允许离线伪造。当前 controller/入口缺失，当前构建暂不可达。
- 处置：为对齐原 JAR 本轮不改；后续安全 PR 应删除公网管理端点或置于独立管理面，使用强身份认证、细粒度授权和审计，并轮换可能已受影响的签名材料。

### S-17 远程书源导入接口可发起任意 URL 请求（高，目标栈恢复后可达）

- 位置：目标反编译 `YueduApi.java:745-762`、`BookSourceController.java:1115`、`:1135-1144`、`:1162`。
- 证据：`POST /reader3/saveFromRemoteSource` 从请求中取得客户端控制的 URL 后直接调用 `webClient.getAbs(url)`；没有限制协议、loopback、内网、链路本地、云元数据地址或 DNS 重绑定。该路由只调用 `checkAuth`，默认 `secure=false` 时放行。
- 影响：入口恢复后，默认部署中的远程调用者可借服务端网络身份访问本机、内网服务和云元数据端点；响应后续会被书源导入逻辑处理，具体数据回传取决于格式解析与错误路径。
- 处置：目标行为，暂缓；后续安全 PR 应在解析与每次重定向后执行协议和解析 IP allowlist/denylist，并阻止 DNS 重绑定与代理绕过。

### S-18 登录会话 token 可由用户名和毫秒时间推导（高，目标栈恢复后条件可利用）

- 位置：当前 `Ext.kt:655-656`；目标 `BaseController.kt:77`、`:87-103`、`UserController.java:211`、`:238`，登录路由见 `YueduApi.java:3475-3493`。
- 证据：目标会话 token 为 `MD5(MD5(username + timestamp_ms) + timestamp_ms)`，全部输入只有公开用户名与登录时的毫秒时间，没有 CSPRNG 或服务端秘密；token 最长保存 7 天，并通过 `username:token`/`accessToken` 使用。
- 影响：能把登录时间缩小到较窄窗口的攻击者可离线枚举毫秒时间并伪造仍在有效期内的认证 token。利用难度取决于攻击者对登录时间窗口的观测精度；当前 controller/入口缺失，当前构建不可达。
- 处置：目标行为，暂缓；后续安全 PR 应改用 CSPRNG 生成的高熵不透明 token 或成熟会话机制，并设计已有 token 的失效与迁移策略。

### S-19 恶意书源可驱动服务端访问任意 URL（高，目标栈恢复后可达）

- 位置：`WebBook.kt:61-78`、`:101-117`、`:144-159`、`:196-211`、`:245-255`，以及 `BookChapterList.kt:63-114`、`BookContent.kt:68-124`。
- 证据：搜索、发现、详情、目录、正文和分页 URL 均由书源规则或上游响应生成并直接传入 `AnalyzeUrl`；当前链路不限制协议、loopback、内网、链路本地、云元数据地址或重定向后的解析地址。`loginCheckJs` 还会对响应执行书源脚本。该问题不同于 S-17 的“导入书源定义 URL”请求；这里覆盖导入后正常解析流程中的全部书源 URL。
- 影响：入口/controller 恢复后，能够保存、导入或触发恶意书源的调用者可利用服务器网络身份探测或访问本机、内网服务和云元数据端点，并通过解析规则、脚本、错误或日志路径带出数据。
- 处置：原 JAR 行为，暂缓；后续安全 PR 应在初始请求和每次重定向处统一校验协议、主机与解析 IP，并阻止 DNS 重绑定和代理绕过。

### S-20 调试路径记录书籍元数据与正文内容（中）

- 位置：`BookList.kt:184-249`、`BookContent.kt:141-151`、`Debugger.kt:19-32`。
- 证据：调试 logger 会记录书名、作者、简介、封面和详情 URL；正文路径记录章节名、正文长度，以及正文不足 300 字时的全文或较长正文首尾各 150 字。目标 controller 的 SSE 调试链会把这些消息发送给调试客户端，其他 logger 实现也可能持久化。
- 影响：受版权保护或含账号态/个性化信息的正文、内部 URL 与书源返回元数据可能进入应用日志、集中采集或调试响应；与 S-06 的原始 HTTP BODY 日志构成不同泄露面。
- 处置：原 JAR 行为，暂缓；后续应按字段分类脱敏，正文只记录长度和不可逆摘要，并对调试端点实施强认证与短期会话授权。

### S-21 缺省 WebBook 命名空间会跨调用共享状态（中）

- 位置：`WebBook.kt:16-20`、`:49-50`、`:57-58`；`AnalyzeRule.kt:659-664`。
- 证据：两个 `WebBook` 构造器允许省略 `userNameSpace`，随后统一回退到固定字符串 `"unknow"`，并把它写入 `BookSource`/书籍变量；规则脚本的 `CookieStore` 与 `CacheManager` 使用同一命名空间。
- 影响：多租户调用方若遗漏显式 namespace，不同用户会共享书源 Cookie、登录态、缓存和规则变量，可能发生跨用户数据读取或身份混用。风险取决于后续 controller 是否在每条调用链都正确传入用户名。
- 处置：原 JAR 行为，暂缓；恢复 controller 时必须逐调用点核对 namespace 传播。后续安全 PR 应取消固定回退值，并让缺失租户上下文显式失败或分配不可共享的作用域。

### S-22 `preciseSearch` 把协程取消包装为普通失败（低）

- 位置：`WebBook.kt:270-282`。
- 证据：挂起函数整体使用 Kotlin `runCatching`，会捕获 `CancellationException` 在内的所有 `Throwable` 并返回失败的 `Result<Book>`，没有重新抛出取消信号。
- 影响：请求断开、超时或服务关闭时，上游可能把取消当成普通搜索失败，破坏结构化并发的快速终止与资源回收语义；在高并发或慢书源场景下会放大资源占用。
- 处置：原 JAR 行为，暂缓；后续应在保留业务异常包装的同时显式重抛 `CancellationException`。

审计残余：本轮未覆盖依赖版本的 CVE 情报、打包后前端静态资产的独立审计和运行态动态利用验证；由于应用入口仍缺失，无法执行端到端渗透测试。定向检查未发现除 S-01 Rhino/书源脚本外的新命令执行链，`SQLTable` 没有真实 SQL 执行面，`ACache.getAsObject` 的原生反序列化目前没有正常调用链。

## 9. 有意资源偏差

共同资源中只有 3 个存在实质内容差异：

- `application.yml` 增加了重建版、Spring 和 SMTP 配置。
- `web/bookSourceDebug/index.html` 增加登录 UI。
- `web/bookSourceDebug/index.js` 增加对应登录逻辑。

这些差异来自既有扩展提交，不是本轮反编译错误。若最终验收采用严格字节级资源对齐，必须先建立显式 allowlist，再决定保留或回退，不能静默混入恢复批次。

## 10. 建议恢复顺序

1. 恢复 `BookConfig + ReaderAdapter + RemoteWebview`。
2. 恢复 `BaseController/CURD` 与业务 controller。
3. 恢复 `YueduApi + RestVerticle`。
4. 最后恢复 `ReaderApplication`，启用 CI `bootJar` 和启动 smoke test。
5. JAR 对齐闭合后，另起安全修复 PR，逐项处理第 8 节并增加负向测试。

## 11. 验收门槛

只有同时满足以下条件，才能将状态改为“可替代原 JAR”：

- 目标项目/Legado 顶层符号全部闭合，或每个差异都有书面 allowlist。
- 共同类型的公开/受保护成员 descriptor 对账完成，真实行为差异归零或列入 allowlist。
- 原 JAR 必需资源全部恢复，资源差异有明确处置。
- `clean test bootJar` 通过。
- 应用可启动，关键路由与 `reader3-routes.txt` 对账通过。
- EPUB/TXT/UMD/CBZ/PDF 和核心在线书源路径至少有 smoke/golden 回归测试。
- 安全暂缓项仍清晰可追踪，且未被误写成“已修复”。
