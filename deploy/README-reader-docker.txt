reader-pro rebuild 部署说明（对应 docker-compose.yaml）
====================================================

访问
  http://localhost:4396/              主界面（与源 jar 相同根路径 SPA）
  http://localhost:4396/simple-web/   简版
  http://localhost:4396/reader3/getSystemInfo

数据目录（相对本 compose 所在目录）
  ./storage  → 用户数据 / 书源 / 书架
  ./logs     → 日志

启动
  cd C:\Users\chong\Downloads
  docker compose up -d --build

首次构建会从 C:\Users\chong\reader-pro-3.2.14-reverse 编译，时间较长。

管理员
  secureKey = 20070828Xyt   （请求头或 query: secureKey=...）
  邀请码    = readwarpdotsyscom

切换到 GHCR 镜像（可选）
  1. 注释 build: 段
  2. 使用 image: ghcr.io/warpdotsys/reader-pro:latest
  3. watchtower command 可改回: reader remote-webview watchtower ...

兼容性
  环境变量 READER_APP_* 与原 hectorqin 配置键一致
  卷 ./storage:/storage  ./logs:/logs 已支持
