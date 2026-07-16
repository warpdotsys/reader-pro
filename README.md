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

## 说明

- **可编译源码** 位于 `src/main/kotlin`，以业务语义完整实现为主。  
- `best-of-3/` 等为逆向过程产物，供对照，**不进入编译 classpath**。  
- 部分能力（Edge TTS 原生库、完整 umdlib、全量 133 路由冷门分支）为可扩展桩或子集实现。

## License

本仓库为学习/研究用途的重建工程。原软件版权归原作者所有。
