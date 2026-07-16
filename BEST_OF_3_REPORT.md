# Best-of-3 full decompile selection

- Base types considered: **276**
- CFR sources: 276
- Vineflower sources: 276
- Procyon sources: 276

## Winner counts

- **vf**: 153 (55.4%)
- **cfr**: 79 (28.6%)
- **pr**: 44 (15.9%)

## Scoring (lower total is better)

Penalty: hard_fail×1200 + stub×2000 + struct×80 + goto×8 + bytecode_dump×15 + illegal_id×15 + high comment-ratio.

Reward: if/for/when/suspend/method counts + size (if clean) + Kotlin bonus for Vineflower.


## Still imperfect after best-of-3

Count: **3**

- `com/htmake/reader/api/controller/BookController` via **vf**: hard=9 struct=0 goto=67 bytecode=787 lines=13139
- `com/htmake/reader/api/controller/UserController` via **vf**: hard=1 struct=0 goto=5 bytecode=99 lines=2065
- `io/legado/app/help/BookHelp` via **vf**: hard=1 struct=0 goto=4 bytecode=52 lines=564

## Winners by top-level package

| Package | CFR | Vineflower | Procyon | none |
|---|---:|---:|---:|---:|
| `com/htmake` | 15 | 44 | 7 | 0 |
| `io/legado` | 27 | 96 | 9 | 0 |
| `me/ag2s` | 36 | 9 | 22 | 0 |
| `org/kxml2` | 1 | 4 | 6 | 0 |

## Output

- Merged tree: `C:\Users\chong\reader-pro-3.2.14-reverse\best-of-3/src/`
- Selection CSV: `BEST_OF_3_SELECTION.csv`
- Manifest: `BEST_OF_3_MANIFEST.txt`
- Per-tool trees: `full-cfr/`, `full-vineflower/`, `full-procyon/`