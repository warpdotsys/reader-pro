# -*- coding: utf-8 -*-
import os, re
from collections import Counter

ROOT = r"C:\Users\chong\reader-pro-3.2.14-reverse\best-of-3\src"

patterns = {
    "unrepresentable": r"<unrepresentable>",
    "VF_tag": r"\$VF",
    "bytecode_dump": r"//\s+Bytecode:",
    "couldnt_decomp": r"Couldn't be decompiled",
    "struct_goto": r"Unable to fully structure|\*\* GOTO",
    "decomp_failed_throw": r'throw new IllegalStateException\("Decompilation failed"\)',
    "illegal_id": r"Illegal identifiers",
    "unable_resugar": r"Unable to resugar",
    "new_type_semicolon": r"=\s*new\s+[\w.]+\s*;",  # incomplete ctor
    "manual": r"MANUALLY RECONSTRUCTED",
    "synthetic": r"Synthetic(Continuation|Type|Function0)",
    "please_report": r"Please report this to",
    "irreducible_note": r"irreducible bytecode",
}

counts = Counter()
files_hit = {k: set() for k in patterns}
total_files = 0
total_lines = 0

for dp, _, fs in os.walk(ROOT):
    for fn in fs:
        if not fn.endswith((".kt", ".java")):
            continue
        path = os.path.join(dp, fn)
        rel = os.path.relpath(path, ROOT).replace("\\", "/")
        t = open(path, encoding="utf-8", errors="replace").read()
        total_files += 1
        total_lines += t.count("\n") + 1
        for name, pat in patterns.items():
            n = len(re.findall(pat, t, flags=re.I))
            if n:
                counts[name] += n
                files_hit[name].add(rel)

print(f"files={total_files} lines={total_lines}")
print("---")
for name in patterns:
    print(f"{name:22s} hits={counts[name]:5d}  files={len(files_hit[name])}")

print("\nincomplete new Type; examples:")
for rel in list(files_hit["new_type_semicolon"])[:15]:
    t = open(os.path.join(ROOT, rel), encoding="utf-8", errors="replace").read()
    for m in re.finditer(r".{0,40}=\s*new\s+[\w.]+\s*;.{0,20}", t):
        print(" ", rel, ":", re.sub(r"\s+", " ", m.group(0))[:100])
        break
