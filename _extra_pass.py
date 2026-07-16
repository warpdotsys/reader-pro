# -*- coding: utf-8 -*-
"""Extra pass: more readability cleanups across best-of-3."""
import os
import re

ROOT = r"C:\Users\chong\reader-pro-3.2.14-reverse\best-of-3\src"
log = []

for dp, _, fs in os.walk(ROOT):
    for fn in fs:
        if not fn.endswith((".kt", ".java")):
            continue
        path = os.path.join(dp, fn)
        rel = os.path.relpath(path, ROOT).replace("\\", "/")
        t = open(path, encoding="utf-8", errors="replace").read()
        orig = t

        # normalize backtick-ugly $completion patterns slightly
        # `$completion` is SyntheticContinuation already fixed

        # Remove "Decompiled with CFR/Procyon could not load" long banners if any remain
        t = re.sub(
            r"(?ms)^/\*\s*\n \* Decompiled with CFR.*?\*/\s*\n",
            "/* decompiled */\n",
            t,
            count=1,
        )
        t = re.sub(
            r"(?ms)^/\*\s*\n \* Decompiled by Procyon.*?\*/\s*\n",
            "/* decompiled */\n",
            t,
            count=1,
        )

        # Compact "NOTE: irreducible..." keep one style
        t = t.replace(
            "// NOTE: irreducible bytecode was split/duplicated by decompiler; logic preserved",
            "// NOTE: decompiler split irreducible bytecode (logic preserved)",
        )

        # Fix double-space / empty javadoc shells left by illegal-id removal
        t = re.sub(r"(?m)^/\*\s*\n\s*\*/\s*\n", "", t)

        # Trailing spaces
        t = re.sub(r"[ \t]+\n", "\n", t)

        if t != orig:
            open(path, "w", encoding="utf-8", newline="\n").write(t)
            log.append(rel)

print(f"extra cleaned {len(log)} files")

# Final stats
keys = [
    ("<unrepresentable>", "unrepresentable"),
    ("$VF", "vf"),
    ("Couldn't be decompiled", "couldnt"),
    ("// Bytecode:", "bytecode"),
    ("Unable to fully structure", "struct"),
    ("** GOTO", "goto"),
    ("Illegal identifiers", "illegal"),
    ("Unable to resugar", "resugar"),
    ("MANUALLY RECONSTRUCTED", "manual"),
    ("SyntheticContinuation", "synth_cont"),
    ("SyntheticFunction0", "synth_fn"),
    ("SyntheticType", "synth_type"),
]
counts = {k: 0 for _, k in keys}
files = 0
lines = 0
for dp, _, fs in os.walk(ROOT):
    for fn in fs:
        if not fn.endswith((".kt", ".java")):
            continue
        t = open(os.path.join(dp, fn), encoding="utf-8", errors="replace").read()
        files += 1
        lines += t.count("\n") + 1
        for needle, key in keys:
            counts[key] += t.count(needle)

print(f"files={files} lines={lines}")
for k, v in counts.items():
    print(f"  {k}: {v}")
