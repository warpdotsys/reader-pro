plugins {
    id("org.springframework.boot") version "2.1.6.RELEASE"
    id("io.spring.dependency-management") version "1.0.11.RELEASE"
    kotlin("jvm") version "1.5.21"
    kotlin("plugin.spring") version "1.5.21"
}

group = "com.htmake"
version = "3.2.14-rebuild"

java {
    sourceCompatibility = JavaVersion.VERSION_1_8
    targetCompatibility = JavaVersion.VERSION_1_8
}

repositories {
    mavenCentral()
}

dependencies {
    implementation("org.springframework.boot:spring-boot-starter:2.1.6.RELEASE")

    // Kotlin
    implementation("org.jetbrains.kotlin:kotlin-reflect:1.5.21")
    implementation("org.jetbrains.kotlin:kotlin-stdlib-jdk8:1.5.21")
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.5.2")
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-slf4j:1.5.2")
    implementation("com.fasterxml.jackson.module:jackson-module-kotlin:2.13.5")

    val vertx = "3.8.5"
    implementation("io.vertx:vertx-core:$vertx")
    implementation("io.vertx:vertx-web:$vertx")
    implementation("io.vertx:vertx-web-client:$vertx")
    implementation("io.vertx:vertx-lang-kotlin:$vertx")
    implementation("io.vertx:vertx-lang-kotlin-coroutines:$vertx")
    implementation("io.vertx:vertx-auth-common:$vertx")

    // HTTP / parse / crypto
    implementation("com.squareup.okhttp3:okhttp:4.9.1")
    implementation("com.squareup.okhttp3:logging-interceptor:4.1.0")
    implementation("com.squareup.retrofit2:retrofit:2.6.1")
    implementation("com.julienviet:retrofit-vertx:1.1.3")
    implementation("org.jsoup:jsoup:1.14.1")
    implementation("com.jayway.jsonpath:json-path:2.6.0")
    implementation("com.google.code.gson:gson:2.8.5")
    implementation("cn.hutool:hutool-crypto:5.8.0.M1")
    implementation("cn.hutool:hutool-core:5.8.0.M1")
    implementation("org.mozilla:rhino:1.7.13")
    implementation("xmlpull:xmlpull:1.1.3.1")

    // PDF
    implementation("org.apache.pdfbox:pdfbox:2.0.27")

    // XPath (Jsoup + XPath, legado-compatible)
    implementation("us.codecraft:xsoup:0.3.2")
    implementation("cn.wanghaomiao:JsoupXpath:2.5.0")

    // Mongo optional
    implementation("org.mongodb:mongodb-driver-sync:3.8.2")

    // Logging
    implementation("io.github.microutils:kotlin-logging:1.6.24")
    implementation("uk.org.lidalia:sysout-over-slf4j:1.0.2")

    testImplementation("org.springframework.boot:spring-boot-starter-test")
    testImplementation("org.jetbrains.kotlin:kotlin-test-junit5:1.5.21")
    testImplementation("org.jetbrains.kotlinx:kotlinx-coroutines-test:1.5.2")
}

tasks.withType<org.jetbrains.kotlin.gradle.tasks.KotlinCompile>().configureEach {
    kotlinOptions {
        jvmTarget = "1.8"
        // Target-era source can contain legacy warnings.
        allWarningsAsErrors = false
        suppressWarnings = false
    }
}

tasks.withType<JavaCompile>().configureEach {
    options.encoding = "UTF-8"
    options.compilerArgs.add("-Xlint:all")
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

// Reconstructed sources deliberately retain the JAR's original Java/Kotlin split.
sourceSets {
    main {
        java {
            srcDir("src/main/java")
        }
    }
}
