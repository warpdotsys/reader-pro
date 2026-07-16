# Best-of-3 全量择优结果

## 做了什么

对 **全部 276 个顶层类型**，各自独立跑通三种反编译，再**按文件打分择优**合并：

| 方案 | 目录 | 输出 |
|------|------|------|
| CFR 0.152 | `full-cfr/` | 276 × `.java` |
| Vineflower 1.11.1 | `full-vineflower/` | 230 × `.kt` + 103 × `.java` |
| Procyon 0.6.0 | `full-procyon/` | 276 × `.java` |

**最终阅读树**：`best-of-3/src/`  
**逐文件选择表**：`BEST_OF_3_SELECTION.csv`  
**报告**：`BEST_OF_3_REPORT.md`

原 JAR 与中间产物均未改写原文件。

## 择优规则（每文件独立）

对每个类型同时评估三份源码，按优先级排序：

1. **否决灾难结果**：整方法 `Decompilation failed` 桩、整文件字节码倾倒（pure dump）
2. **整文件 GOTO/结构崩溃** 劣于 **局部方法失败但其余可读**（localized fail，常见于 Vineflower）
3. 数值分：结构标记、goto、hard-fail、字节码注释密度、可读 if/for/when/suspend 等
4. 同分：Vineflower > CFR > Procyon

## 胜出分布

见最新 `BEST_OF_3_REPORT.md` / 运行日志（约）：

- **Vineflower ~55%** — Kotlin 业务/协程/规则引擎为主
- **CFR ~29%** — 实体类、部分纯 Java / 结构尚可的类
- **Procyon ~16%** — 少数 Procyon 更干净的工具类

## 关键类结果（预期）

| 类型 | 选用 | 说明 |
|------|------|------|
| `BookController` | Vineflower | 主业务；约 9 处方法 couldn't，其余可读 Kotlin |
| `UserController` | Vineflower | 局部 1 处失败 |
| `YueduApi` | Vineflower | 路由中枢，结构清晰 |
| `BookChapterList` | Vineflower | **原 CFR 硬失败方法已恢复** |
| `AnalyzeRule` / `AnalyzeUrl` | Vineflower | 规则引擎 |
| `Book` / `BookSource` 实体 | CFR | 结构正常的 Java 实体 |
| `Coroutine` | Procyon 或 VF | 以 CSV 为准 |

## 使用方式

```
best-of-3/
  src/           # 择优后的源码（.kt + .java 混排）
  resources/     # 配置与前端资源副本
```

- 业务逻辑优先看 `best-of-3/src/com/htmake/...` 与 `io/legado/...`
- 若某文件仍刺眼，用 CSV 看另两套分数，到 `full-*` 对照
- 三套完整树保留，便于人工覆写某一文件

## 仍不完美

合并后仍可能带 hard-fail / 结构标记的文件很少（见报告 “Still imperfect”）。  
这些是**三种工具都无法完美还原**的点，不是漏跑。

## 复现

```powershell
# 三工具已在 full-* 目录
python C:\Users\chong\reader-pro-3.2.14-reverse\_full_best_of_3.py
```
