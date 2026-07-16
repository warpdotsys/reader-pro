# Multi-stage build for reader-pro rebuild
FROM eclipse-temurin:17-jdk AS build
WORKDIR /app
COPY gradlew settings.gradle.kts build.gradle.kts gradle.properties ./
COPY gradle ./gradle
COPY src ./src
RUN chmod +x gradlew && ./gradlew --no-daemon bootJar -x test

FROM eclipse-temurin:17-jre
WORKDIR /app
ENV JAVA_OPTS="-Xms256m -Xmx512m"
COPY --from=build /app/build/libs/*.jar /app/reader-pro.jar
RUN mkdir -p /app/storage
VOLUME ["/app/storage"]
EXPOSE 8080
ENTRYPOINT ["sh", "-c", "java $JAVA_OPTS -Dreader.app.workDir=/app -jar /app/reader-pro.jar"]
