# -*- coding: utf-8 -*-
"""Further cleanup + improve small problematic files."""
import os
import re
import shutil

ROOT = r"C:\Users\chong\reader-pro-3.2.14-reverse\best-of-3\src"
CFR = r"C:\Users\chong\reader-pro-3.2.14-reverse\full-cfr"
PR = r"C:\Users\chong\reader-pro-3.2.14-reverse\full-procyon"
LOG = []

# 1) Strip any remaining $VF lines
for dp, _, fs in os.walk(ROOT):
    for fn in fs:
        if not fn.endswith((".kt", ".java")):
            continue
        path = os.path.join(dp, fn)
        t = open(path, encoding="utf-8", errors="replace").read()
        t2 = re.sub(r"(?m)^[ \t]*//[ \t]*\$VF:[^\n]*\n?", "", t)
        t2 = re.sub(r"//[ \t]*\$VF:[^\n]*", "", t2)
        # Vineflower issue tracker spam lines
        t2 = re.sub(
            r"(?m)^[ \t]*// Please report this to the Vineflower[^\n]*\n?",
            "",
            t2,
        )
        t2 = re.sub(
            r"(?m)^[ \t]*// java\.lang\.[^\n]*\n?",
            "",
            t2,
        )
        t2 = re.sub(
            r"(?m)^[ \t]*//[ \t]*at org\.(jetbrains|vineflower)[^\n]*\n?",
            "",
            t2,
        )
        if t2 != t:
            open(path, "w", encoding="utf-8", newline="\n").write(t2)
            LOG.append(f"stripped VF noise: {os.path.relpath(path, ROOT)}")

# 2) Illegal identifiers comments from CFR leftovers in java files
for dp, _, fs in os.walk(ROOT):
    for fn in fs:
        if not fn.endswith(".java"):
            continue
        path = os.path.join(dp, fn)
        t = open(path, encoding="utf-8", errors="replace").read()
        t2 = re.sub(
            r"(?ms)/\*\s*\n \* Illegal identifiers[^*]*\*/\s*\n",
            "/* note: original had illegal identifiers; renamed by decompiler */\n",
            t,
        )
        t2 = re.sub(r"(?m)^ \* Illegal identifiers[^\n]*\n", "", t2)
        if t2 != t:
            open(path, "w", encoding="utf-8", newline="\n").write(t2)
            LOG.append(f"illegal-id comment: {os.path.relpath(path, ROOT)}")

# 3) For small pure utility files where PR/CFR has zero junk and VF has Synthetic noise,
#    optionally leave as-is since Synthetic is fine.
# Replace ` as SyntheticContinuation` double issues

# 4) Clean Procyon/CFR leftover "Could not load the following classes" banners in any leftover java
for dp, _, fs in os.walk(ROOT):
    for fn in fs:
        if not fn.endswith(".java"):
            continue
        path = os.path.join(dp, fn)
        t = open(path, encoding="utf-8", errors="replace").read()
        t2 = re.sub(
            r"(?ms)^/\*\s*\n \* Decompiled with CFR[^*]*?(?:\*/)\s*\n",
            "/* Decompiled (CFR); headers trimmed */\n",
            t,
            count=1,
        )
        t2 = re.sub(
            r"(?ms)^/\*\s*\n \* Decompiled with Procyon[^*]*?(?:\*/)\s*\n",
            "/* Decompiled (Procyon); headers trimmed */\n",
            t2,
            count=1,
        )
        # Unable to fully structure - if any slipped in
        if "Unable to fully structure code" in t2:
            LOG.append(f"WARN still struct: {os.path.relpath(path, ROOT)}")
        if t2 != t:
            open(path, "w", encoding="utf-8", newline="\n").write(t2)
            LOG.append(f"trim header: {os.path.relpath(path, ROOT)}")

print("\n".join(LOG))
print("done", len(LOG), "ops")

# recount
u = v = m = 0
for dp, _, fs in os.walk(ROOT):
    for fn in fs:
        if not fn.endswith((".kt", ".java")):
            continue
        t = open(os.path.join(dp, fn), encoding="utf-8", errors="replace").read()
        u += t.count("<unrepresentable>")
        v += t.count("$VF:")
        m += t.count("MANUALLY RECONSTRUCTED")
print(f"remaining unrep={u} vf={v} manual={m}")
