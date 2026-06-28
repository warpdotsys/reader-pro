# Recovery notes

The latest 3.2.14 source could not be obtained from public channels. This repository therefore keeps the supplied JAR as the authoritative runtime artifact and includes recovered web/resource assets from that JAR.

## What was recovered directly

- `recovered-assets/web/`: bundled Vue/PWA web build from `BOOT-INF/classes/web`.
- `recovered-assets/simple-web/`: bundled simple web UI from `BOOT-INF/classes/simple-web`.
- `recovered-assets/bookSourceDebug-root/`: root-level book source debugger assets.
- `recovered-assets/defaultData/`: default data resources such as `txtTocRule.json`.
- `recovered-assets/application.yml`: runtime defaults extracted from the artifact.
- `recovered-assets/MANIFEST.MF`: Spring Boot manifest.

## Source status

`reference-source/` contains the community-maintained legacy source from `changshengyu/reader-dev` for review and future source-level reconstruction. It does not replace the 3.2.14 artifact. The JAR contains newer/pro-only classes that are not fully represented in that source, including database/Mongo helpers, license entities/controllers, file and HTTP TTS controllers, book group controller updates, remote webview support, user mutex/cache utilities, and updated web assets.

The local shell did not have a Java runtime, javap, Maven, Gradle, Docker, or GitHub CLI available. The executable named `cfr` on PATH is a Python forensics tool, not the Java CFR decompiler. Therefore this pass preserves the full 3.2.14 binary artifact and recovered web/resources, and includes readable legacy source as reference. Full bytecode-to-source normalization remains a documented follow-up once a JDK/decompiler is available.

## Application defaults

```yaml
reader:
  app:
    workDir: "."
    showUI: false
    debug: false
    packaged: false
    secure: false
    inviteCode: ""
    secureKey: ""
    proxy: false
    proxyType: "HTTP"
    proxyHost: ""
    proxyPort: ""
    proxyUsername: ""
    proxyPassword: ""
    cacheChapterContent: true
    userLimit: 15
    userBookLimit: 200
    debugLog: false
    autoClearInactiveUser: 0
    mongoUri: ""
    mongoDbName: "reader"
    shelfUpdateInteval: 30
    remoteWebviewApi: ""
    defaultUserEnableWebdav: true
    defaultUserEnableLocalStore: true
    defaultUserEnableBookSource: true
    defaultUserEnableRssSource: true
    defaultUserBookSourceLimit: 100
    defaultUserBookLimit: 200
    autoBackupUserData: false
    minUserPasswordLength: 8
    remoteBookSourceUpdateInterval: 720

  server:
    port: 8080
    contextPath: ""
    webUrl: http://localhost:${reader.server.port}${reader.server.contextPath}

logging:
  path: "${reader.app.workDir}/logs"
```
