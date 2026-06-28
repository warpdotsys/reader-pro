FROM eclipse-temurin:8-jre-jammy

ENV TZ=Asia/Shanghai     READER_APP_WORKDIR=/data     READER_SERVER_PORT=8080

WORKDIR /app
COPY app/reader-pro-3.2.14.jar /app/reader-pro.jar

RUN mkdir -p /data
VOLUME ["/data"]
EXPOSE 8080

ENTRYPOINT ["java", "-Dreader.app.workDir=/data", "-Dreader.server.port=8080", "-jar", "/app/reader-pro.jar"]
