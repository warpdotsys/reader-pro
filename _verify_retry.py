# -*- coding: utf-8 -*-
import os
import re

out = r"C:\Users\chong\reader-pro-3.2.14-reverse"


def check(path, needles):
    if not os.path.isfile(path):
        print("MISSING", path)
        return
    t = open(path, encoding="utf-8", errors="replace").read()
    print("====", os.path.relpath(path, out), "lines", t.count("\n") + 1, "bytes", len(t))
    for n in needles:
        print(f"  {n!r}: {t.count(n)}")
    if "analyzeChapterList" in t:
        m = re.search(r"analyzeChapterList[^{]{0,200}\{(.{0,400})", t, re.S)
        if m:
            body = m.group(1).replace("\n", " ")[:300]
            print("  body start:", body)


print("CFR / VF / PR hard-fail classes:")
check(
    os.path.join(out, r"decompiled-src\io\legado\app\model\webBook\BookChapterList.java"),
    ["Decompilation failed", "Unable to fully", "** GOTO", "if ("],
)
check(
    os.path.join(out, r"retry-vineflower\io\legado\app\model\webBook\BookChapterList.kt"),
    ["Decompilation failed", "Couldn't be decompiled", "Unable to fully", "** GOTO", "if (", "suspend "],
)
check(
    os.path.join(out, r"retry-procyon\io\legado\app\model\webBook\BookChapterList.java"),
    ["Decompilation failed", "Error", "if (", "goto"],
)

print()
check(
    os.path.join(out, r"decompiled-src\io\legado\app\help\coroutine\Coroutine.java"),
    ["Decompilation failed", "Couldn't be decompiled"],
)
check(
    os.path.join(out, r"retry-vineflower\io\legado\app\help\coroutine\Coroutine.kt"),
    ["Decompilation failed", "Couldn't be decompiled", "$FF:"],
)
check(
    os.path.join(out, r"retry-procyon\io\legado\app\help\coroutine\Coroutine.java"),
    ["Decompilation failed", "Couldn't be decompiled", "Error"],
)

print()
print("VF files with residual decompile issues:")
vf = os.path.join(out, "retry-vineflower")
for root, ds, fs in os.walk(vf):
    for f in fs:
        if not f.endswith((".kt", ".java")):
            continue
        p = os.path.join(root, f)
        t = open(p, encoding="utf-8", errors="replace").read()
        c1 = t.count("Couldn't be decompiled")
        c2 = t.count("$FF:")
        if c1 or c2:
            print(f"  couldn't={c1} ff={c2}  {os.path.relpath(p, vf)}")

print()
print("Improved tree stats:")
imp = os.path.join(out, "decompiled-improved")
java_n = kt_n = bak_n = 0
for root, ds, fs in os.walk(imp):
    for f in fs:
        if f.endswith(".java"):
            java_n += 1
        elif f.endswith(".kt"):
            kt_n += 1
        elif f.endswith(".cfr-bak"):
            bak_n += 1
print(f"  java={java_n} kt={kt_n} cfr-bak={bak_n}")

# remaining issues in improved for the 43 bases
markers = [
    "Unable to fully structure code",
    "Exception decompiling",
    "Decompilation failed",
    "** GOTO",
    "Couldn't be decompiled",
]
still = []
with open(os.path.join(out, "imperfect-list.txt"), encoding="utf-8") as f:
    bases = [x.strip().replace("\\", "/") for x in f if x.strip()]
for base in bases:
    for ext in (".kt", ".java"):
        p = os.path.join(imp, base + ext)
        if os.path.isfile(p):
            t = open(p, encoding="utf-8", errors="replace").read()
            hits = [m for m in markers if m in t]
            if hits:
                still.append((base + ext, hits, t.count("\n") + 1))
print(f"Still marked imperfect among improved primary files: {len(still)}")
for b, hits, lines in still:
    print(f"  {lines:5d}  {b}  {hits}")
