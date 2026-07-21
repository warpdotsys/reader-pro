# Strict reader-pro 3.2.14 JAR Audit

Audit date: 2026-07-21

## Baseline

- Target artifact: `C:\Users\chong\Downloads\reader-pro-3.2.14.jar`
- SHA-256: `B26FB4769D689D98FF26408CE79A275D719F360906C84ACF52FF404E98030C8C`
- Source branch: `codex/file-controller-39`
- Source build: Gradle 8.7, JDK `17.0.19`, Kotlin `1.9.24`

The target SHA-256 is identical to `ORIGINAL_JAR.sha256`. This report therefore uses the user-provided JAR as the only artifact baseline.

## Scope And Method

This is a strict artifact audit, not an ABI-only audit.

1. Run `clean test` using the workspace JDK 17.
2. Enumerate every `BOOT-INF/classes/**/*.class` entry in the target JAR.
3. Compare the entry path and SHA-256 of every target class against the current `build/classes/java/main` and `build/classes/kotlin/main` outputs.
4. Compare the class-file major version of every shared class.
5. Compare every non-class `BOOT-INF/classes` resource by entry path and SHA-256.
6. Run `bootJar` to check whether the current repository can produce an executable artifact.

No compiled class, resource, metadata entry, or compiler-generated coroutine class is treated as an ignorable difference in the strict result.

## Result: Not Bytecode Aligned

| Check | Target JAR | Current build | Result |
| --- | ---: | ---: | --- |
| Class files | 936 | 693 | Not closed |
| Shared class paths | 636 | 636 | 0 byte-identical |
| Different bytes at shared paths | 0 | 636 | All differ |
| Target-only class paths | 0 | 300 | Missing |
| Current-only class paths | 0 | 57 | Extra |
| Major version for shared classes | 52 (Java 8), 636 | 61 (Java 17), 636 | All differ |
| Target Kotlin Metadata sample | `mv=[1,5,1]` | `mv=[1,9,0]` | Different compiler metadata |
| `clean test` | n/a | 18 test classes, 64 tests passed | ABI tests only |
| `bootJar` | executable Spring Boot JAR exists | `mainClass` cannot be resolved | Fails |

The current repository is not bytecode-equivalent to the target. Passing tests and selected descriptor checks are insufficient evidence for bytecode or behavior equivalence.

## P0: Compiler And Classfile Mismatch

The build explicitly targets Java 17 in `build.gradle.kts`:

```kotlin
sourceCompatibility = JavaVersion.VERSION_17
targetCompatibility = JavaVersion.VERSION_17
jvmTarget = "17"
```

Every shared target class has class-file major version 52, while every shared current class has major version 61. The target `BaseController` has Kotlin Metadata `mv=[1,5,1]`; the current counterpart has `mv=[1,9,0]`.

This alone prevents byte-for-byte equality. Changing the JVM target is necessary but not sufficient: Kotlin compiler version, compiler flags, dependencies, generated state machines, metadata, debug attributes, constant-pool ordering, and source semantics must all match as well.

## P0: Class Inventory Is Incomplete

The following target top-level classes are absent from the current build:

```text
com/htmake/reader/ReaderApplication.class
com/htmake/reader/ReaderApplicationKt.class
com/htmake/reader/api/YueduApi.class
com/htmake/reader/api/YueduApiKt.class
com/htmake/reader/api/controller/FileController.class
com/htmake/reader/api/controller/FileControllerKt.class
com/htmake/reader/config/BookConfig.class
com/htmake/reader/init/ReaderAdapter.class
com/htmake/reader/utils/RemoteWebview.class
com/htmake/reader/verticle/RestVerticle.class
com/htmake/reader/verticle/RestVerticleKt.class
```

The 300 target-only class entries include missing compiler-generated and coroutine classes. Largest missing groups are:

| Outer class | Missing class entries |
| --- | ---: |
| `YueduApi` | 146 |
| `BookController` | 67 |
| `FileController` | 13 |
| `UserController` | 12 |
| `SourceAnalyzer` | 10 |
| `LicenseController` | 6 |
| `BaseController` | 5 |
| `BookSourceController` | 4 |
| `CURD` | 4 |
| `WebdavController` | 4 |

The 57 current-only entries include 19 top-level `com.script` Rhino classes. These are not top-level target classes and must be explicitly reconciled or excluded by a documented packaging rule before strict acceptance.

## P0: Shared Classes Are All Different

There are 636 class paths present in both artifacts. SHA-256 comparison reports zero matching class-file payloads and 636 different payloads.

Some differences are known compiler-version effects, but the set also contains source-level divergence. For example, a private member comparison of `BaseController` shows that the current build has an additional `userNameSpace(Object): String` method not present in the target. The 300 missing generated classes are also evidence of non-equivalent coroutine lowering and/or source behavior.

No shared class can be described as bytecode aligned until its target entry bytes match exactly.

## P0: The Current Repository Cannot Produce A Product JAR

`bootJar` fails with:

```text
Main class name has not been configured and it could not be resolved
```

The missing `ReaderApplication`, `YueduApi`, and `RestVerticle` target classes explain this failure. There is no current boot artifact available to compare against the supplied JAR's launcher metadata, manifest, nested libraries, or final ZIP entry layout.

## P1: Resource Differences

| Check | Count |
| --- | ---: |
| Target non-class `BOOT-INF/classes` resources | 159 |
| Current build resources | 159 |
| Shared paths | 158 |
| Byte-identical shared resources | 69 |
| Different shared resources | 89 |
| Target-only resource | 1 |
| Current-only resource | 1 |

The target-only resource is `META-INF/reader-pro.kotlin_module`. The current-only resource is `reader3-routes.txt`.

Of the 89 unequal shared resources, 86 become equal after normalizing CRLF to LF. Strict artifact matching still counts those as differences. Three resource paths remain materially different after newline normalization:

```text
application.yml
web/bookSourceDebug/index.html
web/bookSourceDebug/index.js
```

## Required Acceptance Gate

The project must not be labeled JAR-aligned or a replacement for `reader-pro-3.2.14.jar` until all of the following are true:

1. Rebuild with the target Java and Kotlin compiler toolchain, including matching compiler flags and dependency versions.
2. Restore every missing target class entry, including entrypoint, routing, controller, and coroutine-generated classes.
3. Remove or document every current-only class entry through an explicit target packaging rule.
4. Make every shared `BOOT-INF/classes/**/*.class` entry byte-identical to the target JAR.
5. Make every target resource path and resource byte identical, including newline bytes and the Kotlin module metadata.
6. Produce a bootable JAR and compare its manifest, launcher classes, `BOOT-INF/lib` inventory, entry names, and entry bytes against the target artifact.
7. Retain ABI and behavioral tests as supporting evidence only; neither may substitute for the artifact comparison above.

Until those gates pass, all previous statements such as "public descriptors match" must be read as limited ABI observations, not bytecode-equivalence claims.
