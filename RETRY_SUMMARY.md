# 非正常部分重试结果（CFR → Vineflower / Procyon）

## 做了什么

对原先 **43** 个非完全正常文件（结构差 / 硬失败 / 非法标识符 / GOTO）：

1. 打成 `imperfect-classes.jar`（含 535 个相关 class/内部类）
2. 挂上 `BOOT-INF/lib`（85 个依赖）+ 全量 `app-classes.jar` 作外部类型
3. **Vineflower 1.11.1**（默认 Kotlin 输出，`.kt`）
4. **Procyon 0.6.0**（`.java`）
5. 与原 **CFR** 结果按启发式打分，生成 **best-of-3** 树：`decompiled-improved/`

原 JAR 与原 `decompiled-src/` **未改**。

## 谁赢了（43 文件）

| 工具 | 胜出数 | 说明 |
|------|--------|------|
| **Vineflower** | **31** | 整体最优，尤其协程/控制器 |
| CFR（原） | 6 | 多为仅有 “Illegal identifiers” 的实体类，或 VF 局部方法失败更多 |
| Procyon | 6 | `Coroutine` 等个别类更干净 |

## 聚合指标（43 个问题集）

| 指标 | CFR | Vineflower | Procyon |
|------|-----|------------|---------|
| `Unable to fully structure` / 结构标记 | **1412** | **0** | 1034 |
| `** GOTO` / goto 类 | **2206** | **178** | 778 |
| 硬失败桩 `Decompilation failed` stub | **4** | 0 | 0 |
| `Couldn't be decompiled` 等 | 0 | **28**（分散在 5 个文件） | 0（但部分方法变成字节码注释） |
| Illegal identifiers 注释 | 38 | 0 | 0 |

**解读**：Vineflower 基本消灭了 CFR 的 “结构无法还原 + GOTO 泥潭”；代价是少数方法标 `Couldn't be decompiled`。Procyon 对个别类好，但对 `BookChapterList` 核心方法会退化成字节码清单。

## 原硬失败 2 处

### 1. `BookChapterList.analyzeChapterList` — **明显改善（Vineflower 修好）**

| 工具 | 结果 |
|------|------|
| CFR | 方法体 = `throw new IllegalStateException("Decompilation failed")` |
| Vineflower | **完整 `suspend fun` 方法体**（约 771 行 `.kt`，可读） |
| Procyon | 无 hard-fail 字样，但主体大量 `//` 字节码，**几乎不可读** |

→ improved 采用 **Vineflower `.kt`**

### 2. `Coroutine` 内部 `invokeSuspend` — **部分改善（Procyon 略好）**

| 工具 | 结果 |
|------|------|
| CFR | 1 处 `Decompilation failed` |
| Vineflower | 仍有 1 处 `Couldn't be decompiled` |
| Procyon | 无硬失败字样，整体较短（可能简化/丢失细节），评分为优 |

→ improved 采用 **Procyon `.java`**

## 仍不完美（improved 主文件）

合并后主副本里仍带问题标记的约 **4** 个：

1. `BaseController` — 评分仍选 CFR（结构差），VF 有 2 处 couldn't  
2. `BookController.kt`（VF）— 约 9 处 `Couldn't be decompiled`  
3. `UserController.kt`（VF）— 1 处 couldn't  
4. `BookHelp` — 仍选 CFR（结构差）

这些在 `decompiled-improved/` 下另存了对照：

- `*.alt-vf.kt`
- `*.alt-pr.java`
- `*.alt-cfr.java`

## 产物路径

| 路径 | 内容 |
|------|------|
| `retry-vineflower/` | 43 类的 Kotlin 反编译（+ 碎片 `$...kt`，共 92 文件） |
| `retry-procyon/` | 43 类的 Java 反编译 |
| `decompiled-improved/` | 全量 CFR 树 + 非正常部分 best-of-3 覆盖 |
| `RETRY_COMPARISON.md` | 逐文件惩罚分对比表 |
| `RETRY_REPLACED.txt` | 实际被替换的文件列表 |
| `imperfect-classes.jar` | 仅问题类的 class 包 |

## 使用建议

- **日常阅读业务逻辑**：优先 `decompiled-improved/`，控制器/引擎看 `.kt`（Vineflower）
- **对照某方法**：同一路径下的 `.alt-*` 或 `retry-*` 目录
- **仍读不动的方法**：以 Vineflower 为主，Procyon 作第二参考；CFR 仅在 VF 标 couldn't 时对比

## 结论

- **有整体改善**：43 个问题文件中 **31 个** Vineflower 明显更好；结构/GOTO 问题从千级降到近零。
- **原 2 个硬失败**：目录解析核心 **已恢复**；协程封装 **部分恢复**。
- **未 100% 完美**：大控制器（尤其 `BookController`）仍有少量方法 Vineflower 也吐不出来。
