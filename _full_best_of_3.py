# -*- coding: utf-8 -*-
"""
Full best-of-3 merge: independently pick CFR / Vineflower / Procyon for every type.
"""
from __future__ import annotations

import csv
import os
import re
import shutil
import zipfile
from collections import defaultdict
from dataclasses import dataclass, field
from typing import Dict, List, Optional, Tuple

OUT = r"C:\Users\chong\reader-pro-3.2.14-reverse"
CFR_ROOT = os.path.join(OUT, "full-cfr")
VF_ROOT = os.path.join(OUT, "full-vineflower")
PR_ROOT = os.path.join(OUT, "full-procyon")
APP_JAR = os.path.join(OUT, "app-classes.jar")
MERGED = os.path.join(OUT, "best-of-3")
RESOURCES = os.path.join(OUT, "resources")

# ---------------------------------------------------------------------------
# Discovery
# ---------------------------------------------------------------------------

def list_sources(root: str) -> Dict[str, List[str]]:
    """
    Map base type path (com/foo/Bar) -> list of primary source files.
    Ignores Vineflower fragment files like Bar$something.kt as primaries,
    but they are recorded separately.
    """
    primaries: Dict[str, List[str]] = defaultdict(list)
    if not os.path.isdir(root):
        return {}
    for dirpath, _, files in os.walk(root):
        for fn in files:
            if not (fn.endswith(".java") or fn.endswith(".kt")):
                continue
            # skip pure fragment-only names that still count as primary if no $
            full = os.path.join(dirpath, fn)
            rel = os.path.relpath(full, root).replace("\\", "/")
            # e.g. com/foo/Bar.kt or com/foo/Bar$inner.kt
            noext = rel.rsplit(".", 1)[0]
            name = os.path.basename(noext)
            if "$" in name:
                # fragment / synthetic continuation class written as separate file
                base = noext.split("$", 1)[0]
                # not a primary
                continue
            primaries[noext].append(full)
    return dict(primaries)


def list_fragments(root: str, base: str) -> List[str]:
    """Files like base$xxx.kt under same package."""
    d = os.path.join(root, os.path.dirname(base).replace("/", os.sep))
    name = os.path.basename(base)
    if not os.path.isdir(d):
        return []
    out = []
    for fn in os.listdir(d):
        if not (fn.endswith(".java") or fn.endswith(".kt")):
            continue
        stem = fn.rsplit(".", 1)[0]
        if stem.startswith(name + "$"):
            out.append(os.path.join(d, fn))
    return out


def read_text(paths: List[str]) -> str:
    parts = []
    for p in paths:
        with open(p, encoding="utf-8", errors="replace") as f:
            parts.append(f.read())
    return "\n".join(parts)


# ---------------------------------------------------------------------------
# Scoring
# ---------------------------------------------------------------------------

HARD_FAIL_PATTERNS = [
    r"Exception decompiling",
    r"Decompilation failed",
    r'throw new IllegalStateException\(\s*"Decompilation failed"\s*\)',
    r"Couldn't be decompiled",
    r"Could not decompile",
    r"/\*\s*Error decompiling",
    r"//\s*\$FF:\s*Couldn't be decompiled",
    r"// ERROR //",
    r"java\.lang\.IllegalStateException:\s*Decompilation failed",
]

STRUCT_PATTERNS = [
    r"Unable to fully structure code",
    r"\*\* GOTO",
    r"Could not resolve type clashes",
    r"Unable to fully structure",
]

BYTECODE_DUMP_PATTERNS = [
    r"//\s+\d+:\s+(aload|astore|invoke|goto|ldc|new|checkcast|if_)",  # procyon/vf dump
    r"//\s+0{0,3}:\s+(aload|astore|invoke|goto|ldc|new|checkcast|if_)",
    r"//\s+Stack map table:",
    r"//\s+Local variable table:",
    r"//\s+Bytecode:",
]

ILLEGAL_ID = [r"Illegal identifiers"]


@dataclass
class Score:
    tool: str
    present: bool = False
    paths: List[str] = field(default_factory=list)
    lines: int = 0
    chars: int = 0
    hard_fail: int = 0
    stub_methods: int = 0
    struct_bad: int = 0
    goto_cnt: int = 0
    bytecode_dump: int = 0
    illegal_id: int = 0
    if_cnt: int = 0
    for_cnt: int = 0
    when_cnt: int = 0
    suspend_cnt: int = 0
    fun_cnt: int = 0
    method_cnt: int = 0
    comment_ratio: float = 0.0
    # True when most of the file is bytecode comments with little real logic
    pure_dump: bool = False
    # True when bytecode dumps look localized (VF failed methods) but body still rich
    localized_fail: bool = False
    penalty: float = 0.0
    reward: float = 0.0
    total: float = 0.0  # lower is better
    notes: str = ""


def count_patterns(text: str, patterns: List[str]) -> int:
    n = 0
    for p in patterns:
        n += len(re.findall(p, text, flags=re.I | re.M))
    return n


def score_source(tool: str, paths: List[str]) -> Score:
    s = Score(tool=tool)
    if not paths:
        s.present = False
        s.penalty = 1_000_000
        s.total = 1_000_000
        s.notes = "missing"
        return s
    s.present = True
    s.paths = paths
    text = read_text(paths)
    s.chars = len(text)
    s.lines = text.count("\n") + 1 if text else 0
    if s.chars < 30:
        s.penalty = 500_000
        s.total = 500_000
        s.notes = "empty"
        return s

    # Unique hard-fail method markers (avoid triple-counting same failure)
    s.hard_fail = text.count("Couldn't be decompiled")
    s.hard_fail += text.count("Exception decompiling")
    s.hard_fail += text.count('throw new IllegalStateException("Decompilation failed")')
    # "Decompilation failed" prose without double-count throw line
    s.hard_fail += len(
        re.findall(r"This method has failed to decompile", text, flags=re.I)
    )

    s.stub_methods = len(
        re.findall(
            r'throw new IllegalStateException\(\s*"Decompilation failed"\s*\)',
            text,
        )
    )
    s.struct_bad = count_patterns(text, STRUCT_PATTERNS)
    s.goto_cnt = len(re.findall(r"\*\* GOTO|\bgoto\b", text, flags=re.I))
    s.bytecode_dump = count_patterns(text, BYTECODE_DUMP_PATTERNS)
    s.illegal_id = count_patterns(text, ILLEGAL_ID)
    s.if_cnt = len(re.findall(r"\bif\s*\(", text))
    s.for_cnt = len(re.findall(r"\bfor\s*\(", text))
    s.when_cnt = len(re.findall(r"\bwhen\s*\(", text))
    s.suspend_cnt = text.count("suspend ")
    s.fun_cnt = len(re.findall(r"\bfun\s+", text))
    s.method_cnt = len(
        re.findall(r"\b(public|private|protected|internal)\b[^{;\n]{0,120}\(", text)
    )

    comment_lines = sum(1 for line in text.splitlines() if line.strip().startswith("//"))
    s.comment_ratio = comment_lines / max(s.lines, 1)

    # Classify dump styles
    # Heavy instruction-comment density = unreadable (Procyon often still matches \bif\()
    bc_density = s.bytecode_dump / max(s.lines, 1)
    s.pure_dump = (
        (s.comment_ratio > 0.50 and s.bytecode_dump > 40)
        or (s.bytecode_dump > 200 and s.if_cnt < 15)
        or (s.bytecode_dump > 400 and bc_density > 0.08)
        or (s.bytecode_dump > 800)
    )
    s.localized_fail = (
        not s.pure_dump
        and s.bytecode_dump > 0
        and s.if_cnt >= 25
        and s.struct_bad == 0
        and s.comment_ratio < 0.45
        and bc_density < 0.12
    )

    # --- Penalty ---
    penalty = 0.0
    penalty += s.stub_methods * 12000
    # CFR spaghetti: structure markers + GOTO dominate readability
    penalty += s.struct_bad * 220
    penalty += min(s.goto_cnt, 500) * 30

    if s.pure_dump:
        penalty += 100000
        penalty += s.bytecode_dump * 40
    elif s.localized_fail:
        # A few VF methods failed with bytecode tail; rest is good Kotlin/Java
        penalty += max(s.hard_fail, 1) * 700
        penalty += min(s.bytecode_dump, 300) * 2
    else:
        penalty += s.hard_fail * 1600
        if s.bytecode_dump:
            penalty += s.bytecode_dump * 18
            if s.bytecode_dump > 80:
                penalty += (s.bytecode_dump - 80) * 22

    penalty += s.illegal_id * 12
    if s.comment_ratio > 0.35 and not s.localized_fail:
        penalty += (s.comment_ratio - 0.35) * 12000

    # --- Reward ---
    reward = 0.0
    reward += min(s.if_cnt, 250) * 2.5
    reward += min(s.for_cnt, 100) * 3.0
    reward += min(s.when_cnt, 50) * 4.0
    reward += min(s.suspend_cnt, 50) * 3.5
    reward += min(s.method_cnt + s.fun_cnt, 150) * 1.0

    if s.stub_methods == 0 and s.struct_bad == 0 and not s.pure_dump:
        reward += min(s.lines, 6000) / 50.0
    if tool == "vf" and s.struct_bad == 0 and not s.pure_dump and (s.suspend_cnt + s.fun_cnt) > 0:
        reward += 50
    if s.struct_bad == 0 and s.hard_fail == 0 and s.bytecode_dump < 15 and s.goto_cnt < 5:
        reward += 40
    if s.localized_fail and s.if_cnt > 80:
        reward += 300  # strongly prefer over full-file GOTO mess

    s.penalty = penalty
    s.reward = reward
    s.total = penalty - reward
    return s


def pick_winner(scores: Dict[str, Score]) -> Tuple[str, Score]:
    """Independently pick best tool for one type. Lower multi-key is better."""
    candidates = [(name, sc) for name, sc in scores.items() if sc.present]
    if not candidates:
        return "none", Score(tool="none", notes="all missing")

    priority = {"vf": 0, "cfr": 1, "pr": 2}

    def sort_key(item):
        name, sc = item
        spaghetti = sc.struct_bad > 15 or sc.goto_cnt > 40
        return (
            # disasters
            sc.stub_methods > 0,
            sc.pure_dump,
            sc.notes in ("empty", "missing"),
            # heavy spaghetti vs localized fail: prefer localized
            0 if sc.localized_fail else (1 if spaghetti else 0),
            # numeric
            sc.total,
            sc.stub_methods,
            0 if sc.localized_fail else sc.hard_fail,
            sc.struct_bad,
            sc.goto_cnt,
            sc.bytecode_dump,
            priority.get(name, 9),
        )

    candidates.sort(key=sort_key)
    return candidates[0][0], candidates[0][1]


# ---------------------------------------------------------------------------
# Universe of types from jar
# ---------------------------------------------------------------------------

def jar_base_types() -> List[str]:
    with zipfile.ZipFile(APP_JAR) as z:
        names = [n for n in z.namelist() if n.endswith(".class")]
    bases = set()
    for n in names:
        n = n[:-6]
        if "$" in n:
            n = n.split("$")[0]
        bases.add(n)
    return sorted(bases)


# ---------------------------------------------------------------------------
# Main
# ---------------------------------------------------------------------------

def main():
    print("Indexing sources...")
    cfr_map = list_sources(CFR_ROOT)
    vf_map = list_sources(VF_ROOT)
    pr_map = list_sources(PR_ROOT)
    bases = jar_base_types()
    print(f"  jar base types: {len(bases)}")
    print(f"  cfr primaries: {len(cfr_map)}  vf: {len(vf_map)}  pr: {len(pr_map)}")

    # union of all discovered bases (in case of mismatch)
    all_bases = sorted(set(bases) | set(cfr_map) | set(vf_map) | set(pr_map))

    rows = []
    wins = defaultdict(int)
    for base in all_bases:
        scores = {
            "cfr": score_source("cfr", cfr_map.get(base, [])),
            "vf": score_source("vf", vf_map.get(base, [])),
            "pr": score_source("pr", pr_map.get(base, [])),
        }
        winner, wscore = pick_winner(scores)
        wins[winner] += 1
        rows.append(
            {
                "base": base,
                "winner": winner,
                "scores": scores,
                "wscore": wscore,
            }
        )

    print("\n=== Winner distribution ===")
    for k, v in sorted(wins.items(), key=lambda x: -x[1]):
        print(f"  {k}: {v}")

    # rebuild merged tree
    if os.path.exists(MERGED):
        shutil.rmtree(MERGED)
    os.makedirs(MERGED, exist_ok=True)

    # copy resources (non-class) into merged/resources and also config at root
    if os.path.isdir(RESOURCES):
        shutil.copytree(RESOURCES, os.path.join(MERGED, "resources"), dirs_exist_ok=True)

    manifest_lines = []
    copy_log = []
    still_imperfect = []

    for row in rows:
        base = row["base"]
        winner = row["winner"]
        if winner == "none":
            continue
        root = {"cfr": CFR_ROOT, "vf": VF_ROOT, "pr": PR_ROOT}[winner]
        paths = row["scores"][winner].paths
        for p in paths:
            rel = os.path.relpath(p, root)
            dest = os.path.join(MERGED, "src", rel)
            os.makedirs(os.path.dirname(dest), exist_ok=True)
            shutil.copy2(p, dest)
            copy_log.append((winner, base, rel))
        # fragments for vineflower
        if winner == "vf":
            for fp in list_fragments(VF_ROOT, base):
                rel = os.path.relpath(fp, VF_ROOT)
                dest = os.path.join(MERGED, "src", rel)
                os.makedirs(os.path.dirname(dest), exist_ok=True)
                shutil.copy2(fp, dest)
                copy_log.append((winner, base, rel + " [fragment]"))

        sc = row["scores"][winner]
        imperfect = (
            sc.hard_fail
            or sc.stub_methods
            or sc.struct_bad
            or sc.bytecode_dump > 15
            or sc.goto_cnt > 20
        )
        if imperfect:
            still_imperfect.append((base, winner, sc))

        manifest_lines.append(
            f"{winner}\t{base}\t"
            f"cfr={row['scores']['cfr'].total:.1f}\t"
            f"vf={row['scores']['vf'].total:.1f}\t"
            f"pr={row['scores']['pr'].total:.1f}\t"
            f"hard={sc.hard_fail}\tstruct={sc.struct_bad}\tgoto={sc.goto_cnt}"
        )

    # write selection CSV + manifest
    csv_path = os.path.join(OUT, "BEST_OF_3_SELECTION.csv")
    with open(csv_path, "w", encoding="utf-8", newline="") as f:
        w = csv.writer(f)
        w.writerow(
            [
                "base",
                "winner",
                "cfr_total",
                "vf_total",
                "pr_total",
                "winner_lines",
                "winner_hard",
                "winner_struct",
                "winner_goto",
                "winner_bytecode",
                "winner_ext",
                "cfr_present",
                "vf_present",
                "pr_present",
            ]
        )
        for row in rows:
            win = row["winner"]
            sc = row["scores"].get(win) or Score(tool=win)
            ext = ""
            if sc.paths:
                ext = ",".join(sorted(set(os.path.splitext(p)[1] for p in sc.paths)))
            w.writerow(
                [
                    row["base"],
                    win,
                    f"{row['scores']['cfr'].total:.2f}",
                    f"{row['scores']['vf'].total:.2f}",
                    f"{row['scores']['pr'].total:.2f}",
                    sc.lines,
                    sc.hard_fail,
                    sc.struct_bad,
                    sc.goto_cnt,
                    sc.bytecode_dump,
                    ext,
                    int(row["scores"]["cfr"].present),
                    int(row["scores"]["vf"].present),
                    int(row["scores"]["pr"].present),
                ]
            )

    with open(os.path.join(OUT, "BEST_OF_3_MANIFEST.txt"), "w", encoding="utf-8") as f:
        f.write("\n".join(manifest_lines))

    # Markdown report
    md = []
    md.append("# Best-of-3 full decompile selection\n")
    md.append(f"- Base types considered: **{len(all_bases)}**")
    md.append(f"- CFR sources: {len(cfr_map)}")
    md.append(f"- Vineflower sources: {len(vf_map)}")
    md.append(f"- Procyon sources: {len(pr_map)}")
    md.append("\n## Winner counts\n")
    for k, v in sorted(wins.items(), key=lambda x: -x[1]):
        md.append(f"- **{k}**: {v} ({100.0 * v / max(len(all_bases), 1):.1f}%)")
    md.append("\n## Scoring (lower total is better)\n")
    md.append(
        "Penalty: hard_fail×1200 + stub×2000 + struct×80 + goto×8 + bytecode_dump×15 "
        "+ illegal_id×15 + high comment-ratio.\n"
    )
    md.append(
        "Reward: if/for/when/suspend/method counts + size (if clean) + Kotlin bonus for Vineflower.\n"
    )
    md.append("\n## Still imperfect after best-of-3\n")
    md.append(f"Count: **{len(still_imperfect)}**\n")
    for base, win, sc in still_imperfect[:50]:
        md.append(
            f"- `{base}` via **{win}**: hard={sc.hard_fail} struct={sc.struct_bad} "
            f"goto={sc.goto_cnt} bytecode={sc.bytecode_dump} lines={sc.lines}"
        )
    if len(still_imperfect) > 50:
        md.append(f"- ... and {len(still_imperfect) - 50} more")

    # package breakdown of winners
    md.append("\n## Winners by top-level package\n")
    pkg_wins = defaultdict(lambda: defaultdict(int))
    for row in rows:
        pkg = row["base"].split("/")[0] if "/" in row["base"] else row["base"]
        # use first 2 segments if com/io
        parts = row["base"].split("/")
        if len(parts) >= 2:
            pkg = "/".join(parts[:2])
        pkg_wins[pkg][row["winner"]] += 1
    md.append("| Package | CFR | Vineflower | Procyon | none |")
    md.append("|---|---:|---:|---:|---:|")
    for pkg in sorted(pkg_wins.keys()):
        d = pkg_wins[pkg]
        md.append(
            f"| `{pkg}` | {d.get('cfr',0)} | {d.get('vf',0)} | {d.get('pr',0)} | {d.get('none',0)} |"
        )

    md.append("\n## Output\n")
    md.append(f"- Merged tree: `{MERGED}/src/`")
    md.append(f"- Selection CSV: `BEST_OF_3_SELECTION.csv`")
    md.append(f"- Manifest: `BEST_OF_3_MANIFEST.txt`")
    md.append(f"- Per-tool trees: `full-cfr/`, `full-vineflower/`, `full-procyon/`")

    report_path = os.path.join(OUT, "BEST_OF_3_REPORT.md")
    with open(report_path, "w", encoding="utf-8") as f:
        f.write("\n".join(md))

    # stats of merged src
    src_root = os.path.join(MERGED, "src")
    n_java = n_kt = 0
    for r, _, fs in os.walk(src_root):
        for fn in fs:
            if fn.endswith(".java"):
                n_java += 1
            elif fn.endswith(".kt"):
                n_kt += 1

    print(f"\nMerged: {src_root}")
    print(f"  java={n_java} kt={n_kt} total={n_java + n_kt}")
    print(f"  still imperfect: {len(still_imperfect)}")
    print(f"  report: {report_path}")
    print(f"  csv: {csv_path}")

    # sample biggest files winners
    print("\n=== Sample large types ===")
    big = sorted(rows, key=lambda r: r["wscore"].lines if r["wscore"] else 0, reverse=True)[:15]
    for r in big:
        sc = r["wscore"]
        print(
            f"  {r['winner']:4s} L={sc.lines:5d} hard={sc.hard_fail} struct={sc.struct_bad}  {r['base']}"
        )


if __name__ == "__main__":
    main()
