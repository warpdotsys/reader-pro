# -*- coding: utf-8 -*-
import os
import re

ROOT = r"C:\Users\chong\reader-pro-3.2.14-reverse\best-of-3\src"
log = []

INIT_RE = re.compile(
    r"^([ \t]*)(\w+)\./\* \$VF: Unable to resugar constructor \*/<init>\((.*)\);\s*$"
)
DECL_RE_TMPL = r"^([ \t]*)val {var}:\s*([\w.]+)\s*=\s*new\s+([\w.]+)\s*;\s*$"
LAMBDA_RE = re.compile(r"([\w$]+)/\* \$VF was: [^*]+\*/\s*\(")

for dp, _, fs in os.walk(ROOT):
    for fn in fs:
        if not fn.endswith((".kt", ".java")):
            continue
        path = os.path.join(dp, fn)
        lines = open(path, encoding="utf-8", errors="replace").read().splitlines(True)
        out = []
        changed = False
        i = 0
        while i < len(lines):
            line = lines[i]
            m = INIT_RE.match(line.rstrip("\n") + ("\n" if line.endswith("\n") else ""))
            # match without requiring keepends
            m = INIT_RE.match(line.replace("\r\n", "\n").rstrip("\n"))
            if m:
                indent, var, args = m.group(1), m.group(2), m.group(3)
                fixed = False
                decl_re = re.compile(
                    rf"^([ \t]*)val {re.escape(var)}:\s*([\w.]+)\s*=\s*new\s+([\w.]+)\s*;\s*$"
                )
                for j in range(len(out) - 1, max(-1, len(out) - 15), -1):
                    mm = decl_re.match(out[j].replace("\r\n", "\n").rstrip("\n"))
                    if mm:
                        typ = mm.group(2)
                        out[j] = f"{mm.group(1)}val {var} = {typ}({args})\n"
                        fixed = True
                        changed = True
                        break
                if not fixed:
                    out.append(f"{indent}// resugared: {var} = Type({args})\n")
                    out.append(f"{indent}// TODO-constructor: {var}.<init>({args})\n")
                    changed = True
                i += 1
                continue

            line2 = LAMBDA_RE.sub(r"\1(", line)
            if "Illegal identifiers - consider using" in line2:
                changed = True
                i += 1
                continue
            # also remove empty illegal comment wrappers left behind
            if line2 != line:
                changed = True
            out.append(line2)
            i += 1

        if changed:
            open(path, "w", encoding="utf-8", newline="\n").write("".join(out))
            log.append(os.path.relpath(path, ROOT).replace("\\", "/"))

print("changed", len(log))
for x in log:
    print(" ", x)

# remaining
markers = 0
for dp, _, fs in os.walk(ROOT):
    for fn in fs:
        if not fn.endswith((".kt", ".java")):
            continue
        t = open(os.path.join(dp, fn), encoding="utf-8", errors="replace").read()
        markers += t.count("$VF")
        markers += t.count("Unable to resugar")
        markers += t.count("Illegal identifiers")
        markers += t.count("<unrepresentable>")
print("remaining marker count", markers)
