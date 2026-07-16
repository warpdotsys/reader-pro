# -*- coding: utf-8 -*-
"""Compare CFR vs Vineflower vs Procyon for imperfect files."""
import os
import re
from collections import defaultdict

OUT = r"C:\Users\chong\reader-pro-3.2.14-reverse"
CFR = os.path.join(OUT, "decompiled-src")
VF = os.path.join(OUT, "retry-vineflower")
PR = os.path.join(OUT, "retry-procyon")

with open(os.path.join(OUT, "imperfect-list.txt"), encoding="utf-8") as f:
    bases = [line.strip().replace("\\", "/") for line in f if line.strip()]

# Badness markers by decompiler family
MARKERS = {
    "hard_fail": [
        r"Exception decompiling",
        r"Decompilation failed",
        r'throw new IllegalStateException\("Decompilation failed"\)',
        r"Couldn't be decompiled",
        r"Could not decompile",
        r"/\* Error decompiling",
        r"// \$FF: Couldn't be decompiled",
        r"// ERROR //",
        r"Decompiler error",
        r"java\.lang\.IllegalStateException: Decompilation failed",
    ],
    "struct_bad": [
        r"Unable to fully structure code",
        r"\*\* GOTO",
        r"// \$FF: Couldn't be decompiled",
        r"Label_\d+",  # procyon labels sometimes ok
    ],
    "goto_heavy": [
        r"\*\* GOTO",
        r"\bgoto\b",
    ],
    "illegal_id": [
        r"Illegal identifiers",
    ],
}


def find_source(root, base):
    """Return best matching source path(s) for a base type like com/foo/Bar."""
    java = os.path.join(root, base + ".java")
    kt = os.path.join(root, base + ".kt")
    found = []
    if os.path.isfile(java):
        found.append(java)
    if os.path.isfile(kt):
        found.append(kt)
    # vineflower may only produce split files - collect directory matches
    d = os.path.join(root, os.path.dirname(base))
    name = os.path.basename(base)
    if os.path.isdir(d):
        for fn in os.listdir(d):
            if fn == name + ".java" or fn == name + ".kt":
                p = os.path.join(d, fn)
                if p not in found:
                    found.append(p)
            # companion fragments BookController$foo.kt - count as related for size only
    return found


def read_all(paths):
    texts = []
    for p in paths:
        with open(p, encoding="utf-8", errors="replace") as f:
            texts.append(f.read())
    return "\n".join(texts), sum(t.count("\n") + 1 for t in texts), paths


def score(text):
    s = {
        "hard_fail": 0,
        "struct_bad": 0,
        "goto_heavy": 0,
        "illegal_id": 0,
        "lines": text.count("\n") + 1 if text else 0,
        "chars": len(text),
        "has_method_body": 0,
    }
    if not text:
        s["hard_fail"] = 999
        return s
    for cat, pats in MARKERS.items():
        for p in pats:
            s[cat] += len(re.findall(p, text, flags=re.I))
    # crude: methods that only throw decompilation failed
    s["stub_methods"] = len(
        re.findall(
            r"throw new IllegalStateException\(\s*\"Decompilation failed\"\s*\)",
            text,
        )
    )
    # vineflower empty method markers
    s["ff_fail"] = text.count("Couldn't be decompiled") + text.count("$FF:")
    # procyon
    s["pr_err"] = len(re.findall(r"/\*.*Error.*\*/", text, flags=re.I))
    # presence of real logic heuristics
    s["if_count"] = len(re.findall(r"\bif\s*\(", text))
    s["for_count"] = len(re.findall(r"\bfor\s*\(", text))
    s["when_count"] = len(re.findall(r"\bwhen\s*\(", text))  # kotlin
    s["suspend_count"] = text.count("suspend ")
    return s


def quality_rank(s):
    """Lower is better."""
    if s["hard_fail"] >= 999 or s["chars"] < 50:
        return (100000, 0, 0)
    penalty = (
        s["hard_fail"] * 1000
        + s["stub_methods"] * 800
        + s["ff_fail"] * 200
        + s["struct_bad"] * 50
        + s["goto_heavy"] * 5
        + s["illegal_id"] * 10
        + s["pr_err"] * 100
    )
    # prefer more structured logic (if/for) as signal of recovery
    reward = s["if_count"] + s["for_count"] + s["when_count"]
    # slight preference for more content if not stubby
    size_bonus = min(s["lines"], 5000) // 100
    return (penalty - reward - size_bonus, penalty, -reward)


results = []
for base in bases:
    row = {"base": base}
    for tag, root in [("cfr", CFR), ("vf", VF), ("pr", PR)]:
        paths = find_source(root, base)
        text, lines, paths = read_all(paths) if paths else ("", 0, [])
        sc = score(text)
        sc["paths"] = [os.path.relpath(p, root).replace("\\", "/") for p in paths]
        sc["ext"] = ",".join(os.path.splitext(p)[1] for p in paths) if paths else ""
        row[tag] = sc
    # pick winner
    cands = []
    for tag in ("cfr", "vf", "pr"):
        cands.append((quality_rank(row[tag]), tag))
    cands.sort()
    row["winner"] = cands[0][1]
    row["ranks"] = [(t, r[0]) for r, t in cands]
    results.append(row)

# summary
wins = defaultdict(int)
for r in results:
    wins[r["winner"]] += 1

print("=== Winner counts (lower rank score better) ===")
for k, v in sorted(wins.items(), key=lambda x: -x[1]):
    print(f"  {k}: {v}")

print("\n=== Per-file comparison ===")
print(f"{'base':<55} {'win':<5} {'cfr_pen':>8} {'vf_pen':>8} {'pr_pen':>8}  notes")
for r in results:
    def pen(tag):
        s = r[tag]
        return quality_rank(s)[1]

    notes = []
    for tag in ("cfr", "vf", "pr"):
        if r[tag]["stub_methods"] or r[tag]["hard_fail"]:
            notes.append(f"{tag}:hard={r[tag]['hard_fail']}/stub={r[tag]['stub_methods']}")
        if r[tag]["struct_bad"]:
            notes.append(f"{tag}:struct={r[tag]['struct_bad']}")
    # special hard fail methods check
    print(
        f"{r['base']:<55} {r['winner']:<5} {pen('cfr'):8d} {pen('vf'):8d} {pen('pr'):8d}  "
        + "; ".join(notes[:4])
    )

# Hard fail focus
print("\n=== Hard-fail focus: BookChapterList / Coroutine ===")
for key in ("io/legado/app/model/webBook/BookChapterList", "io/legado/app/help/coroutine/Coroutine"):
    r = next(x for x in results if x["base"] == key)
    for tag in ("cfr", "vf", "pr"):
        s = r[tag]
        print(
            f"  {tag}: lines={s['lines']} hard={s['hard_fail']} stub={s['stub_methods']} "
            f"struct={s['struct_bad']} goto={s['goto_heavy']} if={s['if_count']} "
            f"paths={s['paths']} ext={s['ext']}"
        )
        # snippet around analyzeChapterList / invokeSuspend fail
        paths = find_source({"cfr": CFR, "vf": VF, "pr": PR}[tag], key)
        if paths:
            text = read_all(paths)[0]
            if "analyzeChapterList" in text:
                idx = text.find("analyzeChapterList")
                print("    analyzeChapterList context:")
                print("   ", repr(text[idx : idx + 200].replace("\n", " "))[:180])
            if "Decompilation failed" in text:
                print("    still has Decompilation failed")
            if "Couldn't be decompiled" in text:
                print("    has Couldn't be decompiled")

# Aggregate marker reduction
print("\n=== Aggregate markers on imperfect set ===")
for tag, root in [("cfr", CFR), ("vf", VF), ("pr", PR)]:
    tot = defaultdict(int)
    files = 0
    for base in bases:
        paths = find_source(root, base)
        if not paths:
            tot["missing_files"] += 1
            continue
        files += 1
        text = read_all(paths)[0]
        sc = score(text)
        for k in ("hard_fail", "stub_methods", "struct_bad", "goto_heavy", "illegal_id", "ff_fail", "if_count"):
            tot[k] += sc[k]
        tot["lines"] += sc["lines"]
    print(f"{tag}: files_present={files} missing={tot['missing_files']} " + " ".join(f"{k}={tot[k]}" for k in tot if k != "missing_files"))

# Write markdown report + choose best merge plan
report = []
report.append("# Decompiler retry comparison (imperfect set only)\n")
report.append(f"Files compared: {len(bases)}\n")
report.append("## Winners\n")
for k, v in sorted(wins.items(), key=lambda x: -x[1]):
    report.append(f"- **{k}**: {v}")
report.append("\n## Per file\n")
report.append("| File | Winner | CFR penalty | VF penalty | Procyon penalty | CFR hard/stub | VF hard/stub | PR hard/stub |")
report.append("|---|---|---:|---:|---:|---|---|---|")
for r in results:
    def p(tag):
        return quality_rank(r[tag])[1]
    def hs(tag):
        return f"{r[tag]['hard_fail']}/{r[tag]['stub_methods']}"
    report.append(
        f"| `{r['base']}` | {r['winner']} | {p('cfr')} | {p('vf')} | {p('pr')} | {hs('cfr')} | {hs('vf')} | {hs('pr')} |"
    )

with open(os.path.join(OUT, "RETRY_COMPARISON.md"), "w", encoding="utf-8") as f:
    f.write("\n".join(report))

# Merge best into decompiled-improved
imp_root = os.path.join(OUT, "decompiled-improved")
import shutil

if os.path.exists(imp_root):
    shutil.rmtree(imp_root)
# copy full CFR tree first
shutil.copytree(CFR, imp_root)

replaced = []
for r in results:
    win = r["winner"]
    if win == "cfr":
        continue
    root = VF if win == "vf" else PR
    paths = find_source(root, r["base"])
    if not paths:
        continue
    # replace primary file; if kotlin, write as .kt alongside and also keep note
    for p in paths:
        rel = os.path.relpath(p, root)
        dest = os.path.join(imp_root, rel)
        os.makedirs(os.path.dirname(dest), exist_ok=True)
        shutil.copy2(p, dest)
        # if winner is kotlin, remove old .java if different ext
        base_noext = os.path.join(imp_root, r["base"])
        if p.endswith(".kt") and os.path.isfile(base_noext + ".java"):
            # keep both: rename java to .java.cfr-bak
            os.replace(base_noext + ".java", base_noext + ".java.cfr-bak")
        if p.endswith(".java") and os.path.isfile(base_noext + ".java"):
            pass  # already overwritten
        replaced.append((r["base"], win, rel))

# Also copy related vineflower fragment files for winners that are vf
for r in results:
    if r["winner"] != "vf":
        continue
    d_src = os.path.join(VF, os.path.dirname(r["base"]))
    name = os.path.basename(r["base"])
    if not os.path.isdir(d_src):
        continue
    d_dst = os.path.join(imp_root, os.path.dirname(r["base"]))
    for fn in os.listdir(d_src):
        if fn.startswith(name + "$") and (fn.endswith(".kt") or fn.endswith(".java")):
            os.makedirs(d_dst, exist_ok=True)
            shutil.copy2(os.path.join(d_src, fn), os.path.join(d_dst, fn))

with open(os.path.join(OUT, "RETRY_REPLACED.txt"), "w", encoding="utf-8") as f:
    for base, win, rel in replaced:
        f.write(f"{win}\t{base}\t{rel}\n")

print(f"\nMerged improved tree: {imp_root}")
print(f"Replaced/overlaid files: {len(replaced)}")
print("Report: RETRY_COMPARISON.md")
