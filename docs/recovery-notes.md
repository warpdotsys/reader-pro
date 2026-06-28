# Recovery notes

## Backend source

`recovered-source/jadx/` is generated with JADX from a class-only JAR created from `BOOT-INF/classes`. Decompiled Java is intended for audit, navigation, and future reconstruction work. It is not represented as official source and may not compile without manual restoration of Kotlin/Java build structure.

## Runtime assets

`recovered-assets/` contains selected runtime assets plus `classes-resources/`, a full mirror of non-class resources under `BOOT-INF/classes`. `recovered-assets/resource-manifest.txt` records SHA-256 and size for every recovered asset file.

## Web source

No `.map` files were found in the JAR web assets. `reconstructed-web/` therefore contains only audit aids extracted from shipped bundles. The authoritative frontend is `recovered-assets/web/`.

## Reference repositories

`changshengyu/reader-dev` and `hectorqin/reader-legado` are used only for comparison, naming, and historical context. See `docs/feature-crosswalk.md` and `reference/README.md`.
