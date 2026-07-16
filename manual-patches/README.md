# 手动 / 批量修复记录（对照 reader-pro-3.2.14.jar）

目标：`best-of-3/src/`

## 1. 方法级手修（对照 CFR + jar）

| 类 | 方法 |
|----|------|
| `BookHelp` | `saveImage` |
| `UserController` | `logout` |
| `BookController` | `saveBookCover`, `getLocalChapterList`, `saveShelfBookLatestChapter`, `editShelfBook`, `syncFromWebdav`, `cacheBookOnServer`, `getSpeakStream`, `setCover`, `getBookShelfBooks` |

注释标记：`MANUALLY RECONSTRUCTED from CFR + *.class`

## 2. 批量修复（mass-fix）

| 项 | 处理 |
|----|------|
| `<unrepresentable>`（458 处 / 48 文件） | → `SyntheticContinuation` / `SyntheticFunction0` / `SyntheticType` |
| `$VF:` 告警注释 | 删除或改写为简短 NOTE |
| 构造器无法 resugar | → `Type(args)` |
| Illegal identifiers 注释 | 删除 |
| CFR/Procyon 冗长文件头 | 精简 |

共享占位类型：

`com/htmake/reader/synth/SyntheticTypes.kt`

## 3. 备份与日志

- `manual-patches/backup-before-fix/`
- `manual-patches/backup-massfix-*`
- `manual-patches/MASS_FIX_LOG.txt`
- `manual-patches/APPLY_LOG.txt`

## 4. 当前质量目标

- `Couldn't be decompiled` / `// Bytecode:` / `** GOTO` / `$VF` / `<unrepresentable>` → **0**
- 仍会看到：`SyntheticContinuation`（协程合成类占位）、手修方法、反编译变量名
