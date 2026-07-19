# reader-pro 3.2.14 JAR 对齐与代码库审计

审计日期：2026-07-19
审计基线：`d95c411`（PR #27 合并后的 `main`）
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

1. `clean test` 有 29/29 通过，但 `bootJar` 仍无法确定主类并直接失败。
2. 目标应用与 Legado 范围仍缺 56 个顶层符号，包括入口、控制器、数据库、WebBook、RSS 和规则分析运行栈。
3. `Relator`、`License`、`EncoderUtils`、`SearchBook`、`EncodingDetect` 的已证实偏差均已按目标 JAR 回正；另恢复 14 个低耦合源码单元。
4. EPUB 所需 DTD、模板、KXml 源码和 provider 已恢复，标准 NCX 读取与 XML serializer 均有回归测试覆盖。
5. CI 仍只执行 `clean test`，无法阻止不可执行制品进入 `main`。

因此，绿色 CI 目前只证明“现有源码子集可编译并通过少量单元测试”，不证明 JAR 对齐或应用可运行。

## 3. 覆盖统计

### 3.1 顶层类型

| 口径 | 原 JAR | 当前编译产物 | 差异 |
| --- | ---: | ---: | ---: |
| 全部顶层 class | 276 | 239 | 缺 56，多 19 |
| 项目与 Legado 顶层符号 | 198 | 142 | 缺 56 |
| `me.ag2s` vendored 类型 | 67 | 67 | 已覆盖 |
| `org.kxml2` vendored 类型 | 11 | 11 | 已恢复 |

当前多出的 19 个类型均为有意恢复到源码树的 `com.script` Rhino 类型；它们在原制品中位于嵌套依赖 JAR，不属于行为偏差。

按目标 class 的 `SourceFile` 聚合后，项目与 Legado 范围共有 163 个源码单元。当前功能上覆盖 127 个，仍有 36 个不完整源码单元（35 Kotlin、1 Java）。已映射的 220 个共同顶层类型语言归属全部一致：118 Kotlin、102 Java，未发现 Java/Kotlin 边界错配。

### 3.2 资源

| 口径 | 原 JAR | 当前 `src/main/resources` | 差异 |
| --- | ---: | ---: | ---: |
| 非 class 资源 | 159 | 159 | 目标缺 1、额外 1 |
| 共同路径 | 158 | 158 | 128 字节相同，27 仅换行不同，3 个实质不同 |

本批恢复了 68 个目标资源：50 个 `dtd/**`、6 个 `epub/**`、6 个图标、`images/loading.gif`、根目录 `bookSourceDebug/**`、KXml service provider 与 `simplelogger.properties`；全部新增资源 SHA-256 与目标一致。

唯一未放入源码资源树的目标路径是 `META-INF/reader-pro.kotlin_module`，它由 Kotlin 编译器生成；当前额外路径 `reader3-routes.txt` 是测试用路由清单。3 个实质资源差异见第 9 节。

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
- API：`BaseController`、`CURD` 以及 11 个业务 controller。
- 持久化：`DB`、`JSONTable`、`SQLTable`、`MongoManager`、`User`。
- 规则与网络书籍：`AnalyzeRule`、`WebBook`、`BookList`、`BookInfo`、`BookChapterList`、`BookContent`、`Debugger`。
- RSS：`Rss`、`RssParserByRule`、`RssParserDefault` 及 RSS 实体。
- 支撑工具：`ExtKt`、`SpringContextUtils`、`RemoteWebview`。

当前编译产物对这 56 个缺失符号的静态引用仍基本为零，说明工程通过删减调用边维持编译，而不是已恢复完整运行闭包。

## 5. P1：已完成批次的回炉项

| 文件/单元 | 已确认偏差 | 状态 |
| --- | --- | --- |
| `Relator.java` | 第 470 行包含真实的 `…1693 tokens truncated…` 反编译伪影，约 34 个枚举常量被吞进注释 | 已修复；220 个常量、顺序与 code/description 对账通过 |
| `License.kt` | 缺 `isValid()`、`validHost(String)`、`toActiveLicense()` | 已修复 |
| `EncoderUtils.kt` | 缺整套 RSA 密钥生成、四种公私钥 API及分段加解密实现 | 已修复 |
| `SearchBook.kt` | `origins` 缺 `private set`，额外暴露 `setOrigins` | 已修复 |
| `appCtx.kt` | 目标调用 `ExtKt.getWorkDir("storage", "cache")`；当前改走 adapter | 随 `ExtKt` 恢复 |
| `VertExt.kt` | 目标使用 compact、注册 Int/Long adapter 的 `ExtKt.gson`；当前使用 pretty `GSON`，数值 JSON 语义不同 | 随 `ExtKt` 恢复 |
| `EncodingDetect` | HTML charset fallback、`String?` 返回与 `File?` 私有参数同目标 Metadata 不一致 | 已修复 |
| `EncodingDetectHelp`、`EncodeConverter`、协程、独立实体/工具 | 14 个目标 `SourceFile` 单元未恢复或不完整 | 已恢复；公开 descriptor 与目标对账通过 |
| `org.kxml2` | KXml parser/serializer/provider 未进入运行闭包 | 已恢复 11 个目标同源 Java 单元 |
| `JsExtensions.kt` | 当前额外声明 `getCookie(String)`；目标仅有 `getCookie(String, String?)` | P2 残余，留待 `JsExtensions` 专项回炉 |

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

- 当前有 6 个测试类、29 个测试，`clean test` 为 29/29 通过。
- PR #27 新增约 4,397 行生产代码，没有新增测试。
- `LocalBook`、TXT、EPUB、CBZ、PDF、UMD 和 `AnalyzeUrl` 主要分支均缺回归覆盖。

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

- 位置：`JsExtensions.kt:298-348`，经 `BaseSource` 暴露给书源脚本。
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

### S-05 ZIP 解压路径穿越检查不完整（高）

- 位置：`ZipUtils.kt:132-186`，可达调用 `JsExtensions.kt:335-348`。
- 证据：只拒绝包含 `../` 的 entry；未拒绝 Windows `..\`、绝对路径、UNC/盘符路径，也未做 canonical containment 校验。
- 影响：恶意压缩包可能把文件写到目标目录之外并覆盖进程可写文件。
- 处置：继承自目标行为，暂缓。

### S-06 调试 HTTP BODY 日志可能泄露敏感信息（中）

- 位置：`HttpHelper.kt:76-79`。
- 证据：传入 `DebugLog` 时启用 `HttpLoggingInterceptor.Level.BODY`。
- 影响：请求/响应头和正文可能包含 Cookie、Authorization、代理凭据、账号信息和书源返回内容。
- 处置：暂缓；后续应按 header/body 做定向脱敏，而不是简单关闭所有调试能力。

### S-07 500 响应暴露内部异常与完整 URI（中）

- 位置：`VertExt.kt:21-36`。
- 证据：错误响应包含解码后的 `absoluteURI`、`throwable.toString()` 和 `throwable.message`。
- 影响：查询参数和内部类名、路径或下游错误信息可能返回给客户端。
- 处置：目标 JAR 原有行为，暂缓。

### S-08 MDC traceId 未清理（低）

- 位置：`VertExt.kt:38-47`。
- 证据：请求处理前 `MDC.put`，处理完成或异常后没有 `remove/clear`。
- 影响：在线程复用时可能发生跨请求日志上下文污染，降低审计可靠性。
- 处置：目标 JAR 原有行为，暂缓。

### S-09 文件与压缩内容存在无上限整块读取（中）

- 位置：`JsExtensions.kt`、`ZipUtils.kt`、`ACache.kt` 等多处 `readBytes()`。
- 证据：若干用户文件、压缩 entry 和脚本可达文件路径在读入内存前没有大小上限。
- 影响：超大文件或高膨胀压缩内容可能导致内存耗尽和服务不可用。
- 处置：暂缓；后续需结合各格式的兼容上限逐项修复。

### S-10 目标用户密码使用快速双 MD5（高，目标栈恢复后可达）

- 位置：目标反编译 `ExtKt.java:1045-1048`、`UserController.java:199-201`、`UserController.java:230-231`；当前这些运行栈尚未恢复。
- 证据：目标实现为 `MD5(MD5(password + salt) + salt)`，用户创建与校验使用 8 字符 salt。
- 影响：数据库泄露后可被高速离线枚举，salt 长度和双轮 MD5 都不能提供现代密码哈希的工作因子。
- 处置：这是目标行为，先记录；恢复 `ExtKt/UserController` 时不得静默改成新哈希，以免与既有数据和 JAR 行为不兼容。后续安全 PR 需要设计迁移兼容层。

## 9. 有意资源偏差

共同资源中只有 3 个存在实质内容差异：

- `application.yml` 增加了重建版、Spring 和 SMTP 配置。
- `web/bookSourceDebug/index.html` 增加登录 UI。
- `web/bookSourceDebug/index.js` 增加对应登录逻辑。

这些差异来自既有扩展提交，不是本轮反编译错误。若最终验收采用严格字节级资源对齐，必须先建立显式 allowlist，再决定保留或回退，不能静默混入恢复批次。

## 10. 建议恢复顺序

1. 恢复 `SpringContextUtils + ExtKt + MongoManager`，同时回正 `appCtx/VertExt`。
2. 恢复 `DB + JSONTable + SQLTable + User`。
3. 恢复 `AnalyzeRule + WebBook + Debugger`，随后恢复 RSS 栈。
4. 回炉 `JsExtensions.getCookie` 的额外 overload，并完成其余共同类型成员对账。
5. 恢复 `BaseController/CURD`、业务 controller、`YueduApi/RestVerticle`。
6. 最后恢复 `ReaderApplication`，启用 CI `bootJar` 和启动 smoke test。
7. JAR 对齐闭合后，另起安全修复 PR，逐项处理第 8 节并增加负向测试。

## 11. 验收门槛

只有同时满足以下条件，才能将状态改为“可替代原 JAR”：

- 目标项目/Legado 顶层符号全部闭合，或每个差异都有书面 allowlist。
- 共同类型的公开/受保护成员 descriptor 对账完成，真实行为差异归零或列入 allowlist。
- 原 JAR 必需资源全部恢复，资源差异有明确处置。
- `clean test bootJar` 通过。
- 应用可启动，关键路由与 `reader3-routes.txt` 对账通过。
- EPUB/TXT/UMD/CBZ/PDF 和核心在线书源路径至少有 smoke/golden 回归测试。
- 安全暂缓项仍清晰可追踪，且未被误写成“已修复”。
