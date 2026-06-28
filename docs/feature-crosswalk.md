# Feature crosswalk

The 3.2.14 JAR is authoritative. Legacy source is only a reference. Authorized JADX output now provides a source-level audit view of the newer/pro-only classes, but the artifact remains the verified runnable implementation.

| Area | 3.2.14 artifact evidence | Decompiled/reference evidence | Status |
| --- | --- | --- | --- |
| Spring Boot entrypoint | `com.htmake.reader.ReaderApplicationKt` in manifest | `recovered-source/jadx/sources/com/htmake/reader/ReaderApplicationKt.java` | Runtime preserved by artifact; source recovered for audit |
| Web UI | `BOOT-INF/classes/web` copied to `recovered-assets/web` | resources preserved under `recovered-assets/` | 3.2.14 asset bundle preserved |
| Simple web UI | `BOOT-INF/classes/simple-web` | older repo has related routing/source | 3.2.14 asset bundle preserved |
| Book source debugger | `bookSourceDebug` in root and web bundle | older functionality present | 3.2.14 asset bundle preserved |
| Book/source/rss/user/webdav controllers | classes under `com.htmake.reader.api.controller` | controller Java emitted under `recovered-source/jadx/sources/com/htmake/reader/api/controller` | Runtime preserved; source recovered |
| Newer/pro controllers | `BookGroupController`, `FileController`, `HttpTTSController`, `LicenseController` | Java emitted for each controller by JADX | Runtime preserved; source recovered for audit |
| Storage/database | `DB`, `JSONTable`, `SQLTable`, `MongoManager`, `MongoFile` | Java emitted under `recovered-source/jadx/sources/com/htmake/reader/db` and entity packages | Runtime preserved; source recovered |
| TTS | `com.htmake.reader.lib.tts` and `HttpTTS` entity | Java emitted under `recovered-source/jadx/sources/com/htmake/reader/lib/tts` | Runtime preserved; source recovered |
| Local books | EPUB/PDF/UMD/TXT classes included | Java emitted under recovered `io.legado.app` packages | Runtime preserved; source recovered |
| Remote webview/book source updates | `RemoteWebview`, config `remoteWebviewApi`, `remoteBookSourceUpdateInterval` | Java emitted for newer config/entity paths | Runtime preserved; source recovered |
| License/invite/security | `License`, `ActiveLicense`, `LicenseController`; config `inviteCode`, `secureKey` | Java emitted for license entity/controller paths | Runtime preserved; source recovered |

JADX emitted `302` Java files and reported `19` errors. Treat recovered source as a reconstruction aid until a clean source build is normalized and verified.
