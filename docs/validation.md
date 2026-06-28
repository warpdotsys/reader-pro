# Validation report

Validation performed during repository reconstruction.

## Artifact

```text
python3 scripts/inventory.py
jar=app/reader-pro-3.2.14.jar
sha256=b26fb4769d689d98ff26408ce79a275d719f360906c84acf52ff404e98030c8c
size=72913887
start_class=com.htmake.reader.ReaderApplicationKt
spring_boot=2.1.6.RELEASE
classes=936
libraries=85
classpath_resources=159
```

## Local JVM smoke test

Command shape:

```bash
java -Dreader.app.workDir=/tmp/reader-pro-smoke/data -Dreader.server.port=18080 -jar app/reader-pro-3.2.14.jar
```

Result: application started, used `/tmp/reader-pro-smoke/data`, listened on port `18080`, and `HEAD /` returned `HTTP/1.1 200 OK`.

## Docker smoke test

Command shape:

```bash
docker build -t reader-pro-smoke:3.2.14 .
docker run -p 18081:18081 -v /tmp/reader-pro-docker-smoke/data:/data \
  -e READER_APP_WORKDIR=/data \
  -e READER_SERVER_PORT=18081 reader-pro-smoke:3.2.14
```

Result: image built successfully, container used `/data`, listened on port `18081`, and `HEAD /` returned `HTTP/1.1 200 OK`.

## Web source maps

No `.map` files were found under `BOOT-INF/classes/web`; see `reconstructed-web/README.md` for the recovery boundary.
