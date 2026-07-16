plugins {
    id("org.springframework.boot") version "2.7.18"
    id("io.spring.dependency-management") version "1.1.4"
    kotlin("jvm") version "1.9.24"
    kotlin("plugin.spring") version "1.9.24"
}

group = "com.htmake"
version = "3.2.14-rebuild"

java {
    sourceCompatibility = JavaVersion.VERSION_17
    targetCompatibility = JavaVersion.VERSION_17
}

repositories {
    mavenCentral()
}

dependencies {
    // Spring Boot (Mongo auto-config excluded in application)
    implementation("org.springframework.boot:spring-boot-starter")
    implementation("org.springframework.boot:spring-boot-starter-json")
    implementation("org.springframework:spring-context")
    implementation("org.springframework:spring-web")

    // Kotlin
    implementation("org.jetbrains.kotlin:kotlin-reflect")
    implementation("org.jetbrains.kotlin:kotlin-stdlib-jdk8")
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.7.3")
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-jdk8:1.7.3")
    implementation("com.fasterxml.jackson.module:jackson-module-kotlin")

    // Vert.x 3.9 (API close to original 3.8.5)
    val vertx = "3.9.16"
    implementation("io.vertx:vertx-core:$vertx")
    implementation("io.vertx:vertx-web:$vertx")
    implementation("io.vertx:vertx-web-client:$vertx")
    implementation("io.vertx:vertx-lang-kotlin:$vertx")
    implementation("io.vertx:vertx-lang-kotlin-coroutines:$vertx")
    implementation("io.vertx:vertx-auth-common:$vertx")

    // HTTP / parse / crypto
    implementation("com.squareup.okhttp3:okhttp:4.12.0")
    implementation("org.jsoup:jsoup:1.17.2")
    implementation("com.jayway.jsonpath:json-path:2.9.0")
    implementation("com.google.code.gson:gson:2.10.1")
    implementation("cn.hutool:hutool-crypto:5.8.25")
    implementation("cn.hutool:hutool-core:5.8.25")
    implementation("org.mozilla:rhino:1.7.14")
    implementation("commons-lang:commons-lang:2.6")
    implementation("org.apache.commons:commons-lang3:3.14.0")

    // PDF
    implementation("org.apache.pdfbox:pdfbox:2.0.31")

    // XPath (Jsoup + XPath, legado-compatible)
    implementation("us.codecraft:xsoup:0.3.2")

    // Mongo optional
    implementation("org.mongodb:mongodb-driver-sync:4.11.1")

    // SMTP optional (JavaMail)
    implementation("com.sun.mail:javax.mail:1.6.2")

    // Logging
    implementation("io.github.microutils:kotlin-logging-jvm:3.0.5")
    implementation("org.slf4j:slf4j-api:1.7.36")

    testImplementation("org.springframework.boot:spring-boot-starter-test")
    testImplementation("org.jetbrains.kotlin:kotlin-test-junit5")
    testImplementation("org.jetbrains.kotlinx:kotlinx-coroutines-test:1.7.3")
}

tasks.withType<org.jetbrains.kotlin.gradle.tasks.KotlinCompile>().configureEach {
    kotlinOptions {
        freeCompilerArgs = listOf("-Xjsr305=strict", "-opt-in=kotlin.RequiresOptIn")
        jvmTarget = "17"
        // allow business tree gradual strictness
        allWarningsAsErrors = false
        suppressWarnings = true
    }
}

tasks.withType<Test> {
    useJUnitPlatform()
    // PDFBox page render + ImageIO on CI
    jvmArgs("-Djava.awt.headless=true")
}

tasks.named<org.springframework.boot.gradle.tasks.bundling.BootJar>("bootJar") {
    archiveBaseName.set("reader-pro")
    archiveVersion.set(project.version.toString())
}

// Soft-fail: compile only main sources we control
sourceSets {
    main {
        java {
            // no java sources expected
        }
    }
}
