# Docker 部署（与源 jar 行为一致）

镜像保持 hectorqin/reader 的部署约定：

| 项 | 约定 |
|----|------|
| HTTP | 容器内 `8080`，默认映射宿主机 `4396` |
| WebUI | 站点根路径 `/`（Vue SPA），简版 `/simple-web/` |
| 数据根 | `READER_APP_WORKDIR=/`，挂载 `./storage:/storage`、`./logs:/logs` |
| 用户数据 | `storage/data/{user}/`（书架、书源、章节缓存） |
| 远程 webview | 可选服务 `hectorqin/remote-webview`（`READER_APP_REMOTEWEBVIEWAPI`） |

## 方式 A：GHCR 镜像（推荐）

CI 已自动构建并推送 `ghcr.io/warpdotsys/reader-pro:latest`：

```bash
docker compose pull && docker compose up -d
```

watchtower 会每天自动跟进 `latest`。

## 方式 B：本地源码构建

```bash
docker compose up -d --build
```

构建使用 BuildKit 缓存挂载复用 `/root/.gradle`，增量构建约 1-2 分钟。

## 等价于 jar 的运行方式

```bash
# 源 jar
java -Dreader.app.workDir=/ -jar reader-pro-3.2.14.jar

# 本镜像（与 compose 相同的挂载约定）
docker run -d --name reader -p 4396:8080 \
  -v ./storage:/storage -v ./logs:/logs \
  -e READER_APP_WORKDIR=/ \
  ghcr.io/warpdotsys/reader-pro:latest
```

## 常用环境变量

| 变量 | 含义 | 默认 |
|------|------|------|
| `READER_APP_WORKDIR` | 数据根 | `/`（镜像内默认 `/data`） |
| `READER_SERVER_PORT` | 容器内端口 | `8080` |
| `READER_APP_SECURE` | 多用户鉴权 | `true` |
| `READER_APP_SECUREKEY` | 管理密码 | — |
| `READER_APP_INVITECODE` | 注册邀请码 | — |
| `READER_APP_USERLIMIT` | 用户上限 | `50` |
| `READER_APP_USERBOOKLIMIT` | 用户书籍上限 | `20000` |
| `READER_APP_REMOTEWEBVIEWAPI` | 远程 webview 地址 | — |
| `JAVA_OPTS` | JVM 参数 | `-Xms256m -Xmx512m` |

完整示例见仓库根目录 `docker-compose.yml` 与 `deploy/docker-compose.user.yaml`。
