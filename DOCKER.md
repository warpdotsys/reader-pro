# Docker 部署（对齐源 jar 行为）

源 jar 以工作目录为数据根，内置 Vue「阅读」WebUI 挂在 **站点根路径**，API 在 `/reader3/*`。

本镜像保持同一约定：

| 项 | 源 jar | 本镜像 |
|----|--------|--------|
| HTTP 端口 | 8080 | 8080 |
| WebUI | `/` + 相对路径 `css/` `js/` | 同左（`StaticHandler("web")` on `/*`） |
| 简版 UI | `/simple-web/` | 同左 |
| 数据根 | 启动 cwd / `reader.app.workDir` | 容器内 `/data` |
| 用户数据 | `{workDir}/storage/data` | `/data/storage/data` |
| 日志 | `{workDir}/logs` | `/data/logs` |
| 自定义样式 | `storage/assets/reader.css` | 同左（首次自动生成占位） |

## 快速启动

```bash
docker compose up -d --build
# 浏览器
#   http://localhost:8080/           # 与源 jar 相同的主界面
#   http://localhost:8080/simple-web/
#   http://localhost:8080/reader3/getSystemInfo
```

数据默认落在宿主机 `./data`（可改环境变量 `READER_DATA`）。

## 等价于 jar 的运行方式

```bash
# 源 jar
java -Dreader.app.workDir=/path/data -jar reader-pro-3.2.14.jar

# 本镜像
docker run -d --name reader-pro -p 8080:8080 \
  -v /path/data:/data \
  -e TZ=Asia/Shanghai \
  reader-pro:3.2.14
```

## 常用环境变量

与 Spring 松散绑定 / 源配置键一致：

| 环境变量 | 含义 | 默认 |
|----------|------|------|
| `READER_APP_WORKDIR` | 数据根 | `/data` |
| `READER_SERVER_PORT` | 端口 | `8080` |
| `READER_APP_SECURE` | 多用户鉴权 | `false` |
| `READER_APP_SECUREKEY` | 管理密钥 | 空 |
| `READER_APP_INVITECODE` | 邀请码 | 空 |
| `READER_APP_USERLIMIT` | 用户上限 | `15`（与源 yml 一致） |
| `READER_APP_MONGOURI` | Mongo 备份 | 空（文件回落） |
| `JAVA_OPTS` | JVM 参数 | `-Xms256m -Xmx512m` |
| `TZ` | 时区 | `Asia/Shanghai` |

## docker-compose 示例（开启安全）

```yaml
environment:
  READER_APP_SECURE: "true"
  READER_APP_SECUREKEY: "your-admin-key"
  READER_APP_INVITECODE: "invite"
```

## 与错误挂载的区别

- **错误**：只挂 `/app/storage` 且 `workDir=/app` 却期望配置在别处  
- **正确**：挂载 **整个 workDir** 到 `/data`（本 compose 已如此）

## GHCR

推送 `main` 后 Actions 构建 `ghcr.io/<org>/reader-pro`。

```bash
docker pull ghcr.io/warpdotsys/reader-pro:latest
docker run -d -p 8080:8080 -v $PWD/data:/data ghcr.io/warpdotsys/reader-pro:latest
```

## ���� hectorqin/reader �� compose

��֮ǰ�Ĺ����ǣ�

```yaml
volumes:
  - ./logs:/logs
  - ./storage:/storage
```

entrypoint ���ڼ�⵽ /storage �� /logs ʱ�Զ��� workDir ��Ϊ /����ٷ�����·��һ�¡�

Ҳ���òֿ���ʾ����

```bash
docker compose -f docker-compose.hectorqin-compat.yml up -d --build
# ���� http://localhost:4396/
```

�������� `READER_APP_*` �� Spring ��ɢ��һ�£���ֱ������ԭ���ü�����
