# reader-pro

Clean distribution repository for `reader-pro` 3.2.14 under the `warpdotsys` organization.

This repository uses the supplied `reader-pro-3.2.14.jar` as the authoritative runtime artifact, preserving the full 3.2.14 behavior while keeping recovered web/resources and legacy reference source available for audit and future source-level reconstruction.

## Contents

- `app/reader-pro-3.2.14.jar` — authoritative Spring Boot runtime artifact.
- `recovered-assets/` — web UI, simple UI, book source debug UI, defaults, and manifest extracted from the JAR.
- `reference-source/` — legacy community-maintained source reference from `changshengyu/reader-dev`; useful for names/layout/comparison, not the authoritative 3.2.14 implementation.
- `docs/provenance.md` — artifact hash, manifest, dependency inventory, and reference commits.
- `docs/recovery-notes.md` — recovery status and known gaps.
- `.github/workflows/docker-publish.yml` — DockerHub publishing workflow.

## Run with Java

```bash
java -jar app/reader-pro-3.2.14.jar
```

The app listens on port `8080` by default. Override configuration with standard Spring Boot properties, for example:

```bash
java -Dreader.app.workDir=/data -Dreader.server.port=8080 -jar app/reader-pro-3.2.14.jar
```

## Run with Docker

```bash
docker build -t warpdotsys/reader-pro:3.2.14 .
docker run --rm -p 8080:8080 -v reader-data:/data warpdotsys/reader-pro:3.2.14
```

## DockerHub publishing

GitHub Actions publishes `warpdotsys/reader-pro` on default-branch pushes, `v*` tags, and manual dispatch. Configure these repository secrets:

- `DOCKERHUB_USERNAME`
- `DOCKERHUB_TOKEN`

## License

The legacy reference source is GPLv3. See `LICENSE`. The packaged artifact and recovered assets are included under the maintainer's authorization for this repository.
