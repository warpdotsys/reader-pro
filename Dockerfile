# reader-pro rebuild — Docker layout aligned with original fat-jar usage:
#   workDir = /data  →  storage/ + logs/ under the volume
#   HTTP    = 8080   →  Vue SPA at /  (classpath web/), APIs at /reader3/*
#
# Multi-stage: build bootJar, run JRE only.

FROM eclipse-temurin:17-jdk-jammy AS build
WORKDIR /workspace
COPY gradlew settings.gradle.kts build.gradle.kts gradle.properties ./
COPY gradle ./gradle
COPY src ./src
RUN chmod +x gradlew \
    && ./gradlew --no-daemon bootJar -x test \
    && JAR=$(ls build/libs/*.jar | head -n1) \
    && cp "$JAR" /workspace/app.jar

FROM eclipse-temurin:17-jre-jammy
LABEL org.opencontainers.image.title="reader-pro" \
      org.opencontainers.image.description="reader-pro 3.2.14 rebuild (legado web reader)" \
      org.opencontainers.image.source="https://github.com/warpdotsys/reader-pro"

# Match common Chinese community deploys: data volume at /data
ENV TZ=Asia/Shanghai \
    LANG=C.UTF-8 \
    JAVA_OPTS="-Xms256m -Xmx512m -Dfile.encoding=UTF-8" \
    READER_APP_WORKDIR=/data \
    READER_SERVER_PORT=8080 \
    SPRING_PROFILES_ACTIVE=prod

WORKDIR /app
RUN ln -snf /usr/share/zoneinfo/$TZ /etc/localtime && echo $TZ > /etc/timezone \
    && mkdir -p /data/storage /data/logs \
    && groupadd -r reader && useradd -r -g reader -d /data reader \
    && chown -R reader:reader /data /app

COPY --from=build /workspace/app.jar /app/reader-pro.jar
COPY docker/docker-entrypoint.sh /app/docker-entrypoint.sh
RUN chmod +x /app/docker-entrypoint.sh && chown reader:reader /app/reader-pro.jar /app/docker-entrypoint.sh

USER reader
VOLUME ["/data"]
EXPOSE 8080
HEALTHCHECK --interval=30s --timeout=5s --start-period=40s --retries=3 \
  CMD bash -c 'exec 3<>/dev/tcp/127.0.0.1/8080' || exit 1

ENTRYPOINT ["/app/docker-entrypoint.sh"]
