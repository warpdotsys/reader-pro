# Feature crosswalk

The 3.2.14 JAR is authoritative. Legacy source is only a reference. Inventory showed the JAR contains newer/pro-only classes and assets that are not fully represented in the public reference source.

| Area | 3.2.14 artifact evidence | Legacy reference evidence | Status |
| --- | --- | --- | --- |
| Spring Boot entrypoint | `com.htmake.reader.ReaderApplicationKt` in manifest | older `ReaderApplication.kt` exists | Preserved by running artifact |
| Web UI | `BOOT-INF/classes/web` copied to `recovered-assets/web` | web build workflow exists in old repo | 3.2.14 asset bundle preserved |
| Simple web UI | `BOOT-INF/classes/simple-web` | older repo has related routing/source | 3.2.14 asset bundle preserved |
| Book source debugger | `bookSourceDebug` in root and web bundle | older functionality present | 3.2.14 asset bundle preserved |
| Book/source/rss/user/webdav controllers | classes under `com.htmake.reader.api.controller` | older source has controller files | Runtime preserved by artifact |
| Newer/pro controllers | `BookGroupController`, `FileController`, `HttpTTSController`, `LicenseController` | not all present in older source | Runtime preserved by artifact; source reconstruction still needed for edits |
| Storage/database | `DB`, `JSONTable`, `SQLTable`, `MongoManager`, `MongoFile` | older source lacks some additions | Runtime preserved by artifact |
| TTS | `com.htmake.reader.lib.tts` and `HttpTTS` entity | older core has less/no HTTP TTS | Runtime preserved by artifact |
| Local books | EPUB/PDF/UMD/TXT classes included | older core has local book classes | Runtime preserved by artifact |
| Remote webview/book source updates | `RemoteWebview`, config `remoteWebviewApi`, `remoteBookSourceUpdateInterval` | older source differs | Runtime preserved by artifact |
| License/invite/security | `License`, `ActiveLicense`, `LicenseController`; config `inviteCode`, `secureKey` | older source differs | Runtime preserved by artifact |
