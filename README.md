# Reader Pro 3.2.14（可编译重建版）

对 `reader-pro-3.2.14.jar` 逆向重建的 Kotlin 源码工程（Spring Boot + Vert.x + 阅读书源引擎）。
目标：行为、WebUI、数据布局与源 jar 一致；可持续修复与演进。

## 特性

| 模块 | 说明 |
|------|------|
| HTTP | Vert.x Router + Kotlin 协程控制器，`/reader3/*` 133 条路由 |
| 书源 | 搜索 / 发现 / 详情 / 目录 / 正文（JSoup · XPath · JsonPath · Rhino JS） |
| 本地书 | TXT · EPUB · CBZ · PDF · UMD |
| 多用户 | `storage/data/{user}/` JSON 存储，邀请码注册 |
| WebDAV | PROPFIND/GET/PUT/DELETE/MKCOL/MOVE/COPY + 备份 |
| 前端 | 原 jar `web/` SPA + `simple-web` |
| CI | Build（jar）+ Docker（GHCR 镜像）两个 workflow |

## 快速开始

```bash
# 需要 JDK 17+
./gradlew bootJar
java -jar build/libs/reader-pro-3.2.14-rebuild.jar
# WebUI: http://localhost:8080/
# API:   http://localhost:8080/reader3/getSystemInfo
```

## Docker

```bash
docker compose up -d --build          # 本地源码构建
# 或直接使用 CI 构建产物：
#   image: ghcr.io/warpdotsys/reader-pro:latest
docker compose pull && docker compose up -d
```

数据布局与 hectorqin 部署一致：挂载 `./storage:/storage`、`./logs:/logs`，
`READER_APP_WORKDIR=/`，详见 [DOCKER.md](DOCKER.md)。

## 工程结构

```
src/main/kotlin/
  com/htmake/reader/     # 启动、路由、控制器、配置
  io/legado/app/         # 阅读引擎（规则、网书、本地书、RSS）
src/main/resources/      # application.yml + web/ + simple-web
docker/                  # 入口脚本
deploy/                  # 用户侧 compose 示例
.github/workflows/       # build.yml(jar) + docker.yml(GHCR)
```

## CI

推送到 `main` 后自动：

1. **Build**：`./gradlew clean compileKotlin bootJar`，上传 jar artifact
2. **Docker**：构建并推送 `ghcr.io/warpdotsys/reader-pro:latest`（含 semver 标签）

## 配置

`src/main/resources/application.yml`，亦可用 `READER_APP_*` 环境变量覆盖：

- `reader.app.workDir` — 数据根目录
- `reader.app.secure` / `secureKey` / `inviteCode` — 多用户与注册
- `reader.app.userLimit` 等限额参数

## License

本仓库为学习/研究用途的重建工程。原软件版权归原作者所有。
