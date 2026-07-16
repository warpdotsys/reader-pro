# -*- coding: utf-8 -*-
"""Scan best-of-3 (and optionally full trees) for remaining decompile quality issues."""
import os
import re
from collections import defaultdict

ROOT = r"C:\Users\chong\reader-pro-3.2.14-reverse\best-of-3\src"

MARKERS = {
    "hard_fail": [
        r"Couldn't be decompiled",
        r"Could not decompile",
        r"Exception decompiling",
        r"This method has failed to decompile",
        r'throw new IllegalStateException\(\s*"Decompilation failed"\s*\)',
        r"Decompilation failed",
        r"// \$VF:",
        r"// \$FF:",
    ],
    "struct_bad": [
        r"Unable to fully structure code",
        r"\*\* GOTO",
        r"Could not resolve type clashes",
    ],
    "bytecode_dump": [
        r"//\s+Bytecode:",
        r"//\s+\d+:\s+(aload|astore|invoke|goto|ldc|new|checkcast|if_)",
        r"//\s+0{2,3}:\s+(aload|astore|invoke)",
    ],
    "illegal_id": [
        r"Illegal identifiers",
    ],
    "placeholder": [
        r"invalid duplicate definition",
        r"<unrepresentable>",
        r"Please report this to the Vineflower",
        r"Please report this to the CFR",
        r"MANUALLY RECONSTRUCTED",  # note only, not a bug
    ],
}

results = defaultdict(list)  # category -> [(rel, count, sample_line)]
file_stats = []
total_files = 0
total_lines = 0

for dirpath, _, files in os.walk(ROOT):
    for fn in files:
        if not (fn.endswith(".kt") or fn.endswith(".java") or fn.endswith(".cfr-bak")):
            continue
        path = os.path.join(dirpath, fn)
        rel = os.path.relpath(path, ROOT).replace("\\", "/")
        try:
            text = open(path, encoding="utf-8", errors="replace").read()
        except Exception as e:
            results["io_error"].append((rel, 1, str(e)))
            continue
        total_files += 1
        lines = text.count("\n") + 1
        total_lines += lines
        file_hit = {}
        for cat, pats in MARKERS.items():
            c = 0
            sample = ""
            for pat in pats:
                for m in re.finditer(pat, text, flags=re.I):
                    c += 1
                    if not sample:
                        line_no = text[: m.start()].count("\n") + 1
                        line = text.splitlines()[line_no - 1].strip()[:120]
                        sample = f"L{line_no}: {line}"
            if c:
                file_hit[cat] = c
                results[cat].append((rel, c, sample))
        # high goto density in non-reconstructed files
        goto_n = len(re.findall(r"\*\* GOTO|\bgoto\b", text, flags=re.I))
        if goto_n > 30 and "MANUALLY RECONSTRUCTED" not in text:
            results["high_goto"].append((rel, goto_n, f"goto-ish count={goto_n}"))
        file_stats.append((rel, lines, file_hit, goto_n))

print(f"Scanned: {total_files} source files, {total_lines} lines")
print(f"Root: {ROOT}")
print()

# Summary excluding manual note as problem
problem_cats = ["hard_fail", "struct_bad", "bytecode_dump", "illegal_id", "high_goto", "io_error"]
# placeholder: only report non-manual
ph = [(r, c, s) for r, c, s in results.get("placeholder", []) if "MANUALLY RECONSTRUCTED" not in open(os.path.join(ROOT, r), encoding="utf-8", errors="replace").read()[:5000] or "invalid" in s.lower() or "unrepresentable" in s.lower() or "Please report" in s]

print("=== Issue summary ===")
for cat in problem_cats:
    items = results.get(cat, [])
    # dedupe by file for high_goto
    files_n = len({x[0] for x in items})
    total_c = sum(x[1] for x in items)
    print(f"  {cat:16s}  files={files_n:3d}  hits={total_c}")

# invalid/unrepresentable separately
inv = []
unrep = []
for dirpath, _, files in os.walk(ROOT):
    for fn in files:
        if not (fn.endswith(".kt") or fn.endswith(".java")):
            continue
        path = os.path.join(dirpath, fn)
        rel = os.path.relpath(path, ROOT).replace("\\", "/")
        text = open(path, encoding="utf-8", errors="replace").read()
        if "invalid duplicate definition" in text:
            inv.append(rel)
        if "<unrepresentable>" in text:
            unrep.append((rel, text.count("<unrepresentable>")))

print(f"  invalid_duplicate  files={len(inv)}")
print(f"  unrepresentable    files={len(unrep)} hits={sum(c for _,c in unrep)}")
print(f"  manual_patches     files={len([x for x in file_stats if 'MANUALLY' in open(os.path.join(ROOT,x[0]),encoding='utf-8',errors='replace').read()])}")

print()
print("=== Hard fail details ===")
for rel, c, sample in sorted(results.get("hard_fail", [])):
    print(f"  [{c}] {rel}")
    print(f"       {sample}")

print()
print("=== Struct / GOTO heavy (top 20 by hits) ===")
struct_files = defaultdict(int)
for rel, c, sample in results.get("struct_bad", []):
    struct_files[rel] += c
for rel, c, sample in results.get("high_goto", []):
    struct_files[rel] += c
for rel, c in sorted(struct_files.items(), key=lambda x: -x[1])[:20]:
    print(f"  [{c:4d}] {rel}")

print()
print("=== Bytecode dump files ===")
for rel, c, sample in sorted(results.get("bytecode_dump", []), key=lambda x: -x[1])[:20]:
    print(f"  [{c}] {rel} | {sample}")

print()
print("=== <unrepresentable> top ===")
for rel, c in sorted(unrep, key=lambda x: -x[1])[:15]:
    print(f"  [{c}] {rel}")

print()
print("=== invalid duplicate definition ===")
for rel in inv[:20]:
    print(f"  {rel}")

# True remaining "real" problems: hard_fail without MANUAL context, bytecode, pure dump
real_hard = []
for rel, c, sample in results.get("hard_fail", []):
    text = open(os.path.join(ROOT, rel), encoding="utf-8", errors="replace").read()
    # if only in manual comments - check
    if "MANUALLY RECONSTRUCTED" in text and text.count("Couldn't be decompiled") == 0 and text.count("Decompilation failed") == 0:
        # might be other hard markers
        pass
    real_hard.append((rel, c, sample))

print()
print("=== Verdict counts ===")
has_hard = len({r for r, _, _ in results.get("hard_fail", [])})
has_bc = len({r for r, _, _ in results.get("bytecode_dump", [])})
has_struct = len({r for r, _, _ in results.get("struct_bad", [])})
print(f"files with hard_fail markers: {has_hard}")
print(f"files with bytecode dumps: {has_bc}")
print(f"files with struct_bad: {has_struct}")
print(f"files with high_goto(>30): {len(results.get('high_goto', []))}")
print(f"files with unrepresentable: {len(unrep)}")
print(f"files with invalid duplicate: {len(inv)}")
