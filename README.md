# reader-pro

Artifact-first recovery of Reader Pro `3.2.14`.

This repository preserves the provided `reader-pro-3.2.14.jar` as the authoritative runtime artifact and adds extracted assets, decompiled audit source, provenance documentation, and Docker publishing automation.

## What is authoritative?

- **Authoritative runtime:** `app/reader-pro-3.2.14.jar`
- **Extracted runtime assets:** `recovered-assets/`
- **Audit/decompiled source:** `recovered-source/jadx/`
- **Best-effort web reconstruction aids:** `reconstructed-web/`
- **Historical references only:** `reference/`

The recovered source is not claimed to be official upstream source or a complete rebuildable Kotlin/Java project. When behavior differs between old public repositories and this JAR, the JAR wins.

## Artifact facts

- SHA-256: `b26fb4769d689d98ff26408ce79a275d719f360906c84acf52ff404e98030c8c`
- Size: `72913887` bytes
- Start class: `com.htmake.reader.ReaderApplicationKt`
- Spring Boot: `2.1.6.RELEASE`

Validate locally:

```bash
python3 scripts/inventory.py
```

## Run

```bash
java -Dreader.app.workDir=./data -Dreader.server.port=8080 -jar app/reader-pro-3.2.14.jar
```

## Docker

```bash
docker build -t warpdotsys/reader-pro:3.2.14 .
docker run --rm -p 8080:8080 -v "$PWD/data:/data" warpdotsys/reader-pro:3.2.14
```

See `docs/deployment.md` for runtime options and `docs/provenance.md` for recovery boundaries.
