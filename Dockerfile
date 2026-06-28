FROM eclipse-temurin:8-jre-jammy

WORKDIR /opt/reader-pro

ENV READER_APP_WORKDIR=/data \
    READER_SERVER_PORT=8080 \
    JAVA_OPTS=""

COPY app/reader-pro-3.2.14.jar /opt/reader-pro/reader-pro.jar

RUN mkdir -p /data && chown -R 10001:0 /data /opt/reader-pro

USER 10001
EXPOSE 8080

ENTRYPOINT ["sh", "-c", "exec java $JAVA_OPTS -Dreader.app.workDir=${READER_APP_WORKDIR} -Dreader.server.port=${READER_SERVER_PORT} -jar /opt/reader-pro/reader-pro.jar"]
