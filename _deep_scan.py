# -*- coding: utf-8 -*-
import os, re
from collections import Counter, defaultdict

ROOT = r"C:\Users\chong\reader-pro-3.2.14-reverse\best-of-3\src"

unrep_ctx = Counter()
unrep_by_file = Counter()
vf_comments = Counter()
vf_by_file = defaultdict(list)

for dp, _, fs in os.walk(ROOT):
    for fn in fs:
        if not fn.endswith((".kt", ".java")):
            continue
        path = os.path.join(dp, fn)
        rel = os.path.relpath(path, ROOT).replace("\\", "/")
        t = open(path, encoding="utf-8", errors="replace").read()
        for m in re.finditer(r".{0,60}<unrepresentable>.{0,60}", t):
            s = re.sub(r"\s+", " ", m.group(0)).strip()
            unrep_ctx[s[:100]] += 1
            unrep_by_file[rel] += 1
        for m in re.finditer(r"// \$VF:[^\n]+", t):
            vf_comments[m.group(0).strip()[:90]] += 1
            vf_by_file[rel].append(m.group(0).strip()[:80])

print("=== Top <unrepresentable> contexts ===")
for s, c in unrep_ctx.most_common(25):
    print(f"  [{c:3d}] {s}")

print("\n=== Top files by unrepresentable ===")
for rel, c in unrep_by_file.most_common(25):
    print(f"  [{c:3d}] {rel}")

print("\n=== $VF comment types ===")
for s, c in vf_comments.most_common(20):
    print(f"  [{c:3d}] {s}")

print("\n=== Files with $VF comments ===")
for rel, lst in sorted(vf_by_file.items(), key=lambda x: -len(x[1]))[:20]:
    print(f"  [{len(lst):2d}] {rel}")
