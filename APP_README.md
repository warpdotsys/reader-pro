# Reader Pro (rebuild)

Elegant, self-contained rebuild of **reader-pro 3.2.14** semantics:

- Spring Boot 2.7 + Vert.x 3.9 HTTP
- Kotlin coroutines controllers
- Legado-style rule engine (JSoup / XPath-lite / JsonPath / Rhino JS)
- Web book search / explore / info / TOC / content
- Local TXT / EPUB / CBZ / PDF
- Multi-user file storage under `storage/data`
- WebDAV subset + backup zip
- GitHub Actions: `.github/workflows/build.yml`

## Build

```bash
./gradlew bootJar
java -Dreader.app.workDir=. -jar build/libs/reader-pro-*.jar
# WebUI: http://localhost:8080/   (same as original jar — SPA at site root)
# API:   http://localhost:8080/reader3/getSystemInfo
```

## Docker（对齐源 jar）

```bash
docker compose up -d --build          # 本地构建
# 或用 CI 产物: ghcr.io/warpdotsys/reader-pro:latest
# 数据: ./storage /storage, ./logs /logs（hectorqin 布局）
```

See [DOCKER.md](DOCKER.md).

## API

See `API_ROUTES.md` and live `/reader3/apiDocs`.

## Layout

```
src/main/kotlin/com/htmake/reader/   # server
src/main/kotlin/io/legado/app/       # reading engine
src/main/resources/                  # yml + static web
```
