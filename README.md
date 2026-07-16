# Reader Pro 3.2.14（可编译重建版）

优雅、完整、可通过 **GitHub Actions** 编译的 Kotlin 源码工程。  
语义对齐 `reader-pro-3.2.14`（Spring Boot + Vert.x + 阅读书源引擎），非原 jar 字节码级复刻。

## 特性

| 模块 | 说明 |
|------|------|
| HTTP | Vert.x 3.9 Router + 协程控制器 |
| 书源 | 搜索 / 发现 / 详情 / 目录 / 正文 |
| 规则引擎 | JSoup · XPath-lite · JsonPath · Rhino JS |
| 本地书 | TXT · EPUB · CBZ · PDF |
| 多用户 | `storage/data/{user}/` JSON 存储 |
| WebDAV | PROPFIND/GET/PUT/DELETE/MKCOL/MOVE/COPY + 备份 |
| 替换规则 | scope / timeout / bookName |
| CI | `.github/workflows/build.yml` |

## 快速开始

```bash
# 需要 JDK 17+
./gradlew bootJar
java -jar build/libs/reader-pro-3.2.14-rebuild.jar
```

默认端口 **8080**。系统信息：

```
GET http://localhost:8080/reader3/getSystemInfo
```

## 工程结构

```
src/main/kotlin/
  com/htmake/reader/     # 启动、路由、控制器、配置
  io/legado/app/         # 阅读引擎（规则、网书、本地书、RSS）
  me/ag2s/epublib/       # 轻量 EPUB 写出桩
src/main/resources/      # application.yml + 静态页
.github/workflows/       # Actions 构建
best-of-3/               # 逆向归档（参考，不参与编译）
```

## 构建（GitHub Actions）

推送到 `main` 后自动：

1. Setup Temurin JDK 17  
2. `./gradlew clean compileKotlin bootJar -x test`  
3. 上传 `build/libs/*.jar` artifact  

## 配置

`src/main/resources/application.yml`：

- `reader.app.workDir` — 数据根目录  
- `reader.app.secure` — 是否强制登录  
- `reader.app.secureKey` — 管理密码  

## 完成度（持续推进）

| 项 | 状态 |
|----|------|
| `/reader3/*` 路由 | 已按 `API_ROUTES.md` **挂满 133 条** |
| 核心阅读链路 | 搜索/多源/发现/目录/正文/缓存 SSE/导出 |
| TTS | `/reader3/book/tts`（text-to-speech.cn + HttpTTS api） |
| 前端 | 原 jar `web/` + `simple-web` 已回填 resources |
| 可编译 | `./gradlew bootJar` + GitHub Actions |

### 最近加深（phase 深度）

- **XPath**：接入 **Xsoup**（Jsoup+XPath），支持 `&&/||/%%`
- **EPUB**：OPF spine + NCX/nav 标题合并 + 封面/元数据
- **TTS**：`EdgeTts`（反射原 TTSService → fallback text-to-speech.cn）
- **测试**：`SmokeTest`（规则/书源/EPUB/路由冒烟）+ CI 跑 `test bootJar`

仍可加深：umdlib、登录 JS 全链路、生产级 Mongo、接口行为对照压测。

## 说明

- **可编译源码** 位于 `src/main/kotlin`。  
- `best-of-3/` 为逆向归档 + business 语义树（与主线同步推进）。  
- `best-of-3/` **不进入** 编译 classpath。

## License

本仓库为学习/研究用途的重建工程。原软件版权归原作者所有。
