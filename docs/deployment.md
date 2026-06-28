# Deployment

## Run with Java

```bash
java -Dreader.app.workDir=./data -Dreader.server.port=8080 -jar app/reader-pro-3.2.14.jar
```

Then open `http://localhost:8080` unless `reader.server.contextPath` or port are changed.

## Build Docker image

```bash
docker build -t warpdotsys/reader-pro:3.2.14 .
docker run --rm -p 8080:8080 -v "$PWD/data:/data" \
  -e READER_APP_WORKDIR=/data \
  -e READER_SERVER_PORT=8080 \
  warpdotsys/reader-pro:3.2.14
```

`JAVA_OPTS` may be used for additional JVM or Spring system properties.

## Published tags

The GitHub Actions workflow builds on pull requests without pushing. On default-branch pushes, tags, or manual dispatch, it publishes DockerHub image tags including `latest`, `3.2.14`, `v3.2.14` for release tags, and short SHA tags.
