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
java -jar build/libs/reader-pro-*.jar
```

## API

See `API_ROUTES.md` (original 133 routes). Core routes are wired in `YueduApi`.

## Layout

```
src/main/kotlin/com/htmake/reader/   # server
src/main/kotlin/io/legado/app/       # reading engine
src/main/resources/                  # yml + static web
best-of-3/                           # reverse-engineering archive (reference)
```
