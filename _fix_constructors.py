# -*- coding: utf-8 -*-
"""Fix Vineflower 'Unable to resugar constructor' and leftover VF name comments."""
import os
import re

ROOT = r"C:\Users\chong\reader-pro-3.2.14-reverse\best-of-3\src"
log = []

# Pattern A: split constructor
# val x: T = new T;
# ... optional null checks ...
# x./* $VF: Unable to resugar constructor */<init>(args);
PAT_SPLIT = re.compile(
    r"(?P<indent>[ \t]*)val (?P<var>\w+):\s*(?P<type>[\w.]+)\s*=\s*new\s+(?P=type)\s*;\s*\n"
    r"(?P<body>(?:.*\n){0,8}?)"
    r"(?P=indent)(?P=var)\./\*\s*\$VF:\s*Unable to resugar constructor\s*\*/<init>\((?P<args>.*?)\);\s*\n",
    re.M | re.S,
)

# Pattern B: lambda name comment
PAT_LAMBDA = re.compile(
    r"(?P<name>[\w$]+)/\*\s*\$VF was:\s*[^ *]+\s*\*/\s*\("
)

# Illegal identifiers block
PAT_ILLEGAL = re.compile(
    r"(?ms)^[ \t]*/\*\s*\n[ \t]*\*[ \t]*Illegal identifiers[^\n]*\n[ \t]*\*/\s*\n"
)
PAT_ILLEGAL2 = re.compile(
    r"(?m)^[ \t]*\*[ \t]*Illegal identifiers[^\n]*\n"
)


def fix_split_ctor(m: re.Match) -> str:
    indent = m.group("indent")
    var = m.group("var")
    typ = m.group("type")
    body = m.group("body")
    args = m.group("args")
    # keep body (null checks etc.) but constructor becomes one-liner after body
    # If body only whitespace/null checks referencing nothing needed before init, put ctor first
    # Safer: ctor after body checks that don't use var as constructed
    if re.search(rf"\b{re.escape(var)}\b", body):
        # body references var - keep two-step with comment
        return (
            f"{indent}val {var}: {typ} = {typ}({args}) // resugared constructor\n"
            f"{body}"
        )
    return f"{body}{indent}val {var} = {typ}({args})\n"


def fix_text(t: str, rel: str) -> str:
    orig = t
    t, n1 = PAT_SPLIT.subn(fix_split_ctor, t)
    # simpler line-level fallback
    t2 = []
    lines = t.splitlines(keepends=True)
    i = 0
    n2 = 0
    while i < len(lines):
        line = lines[i]
        m = re.search(
            r"^([ \t]*)(\w+)\./\*\s*\$VF:\s*Unable to resugar constructor\s*\*/<init>\((.*)\);\s*$",
            line,
        )
        if m:
            indent, var, args = m.group(1), m.group(2), m.group(3)
            # look back for `val var: Type = new Type;`
            typ = None
            for j in range(i - 1, max(-1, i - 12), -1):
                mm = re.match(
                    rf"^[ \t]*val {re.escape(var)}:\s*([\w.]+)\s*=\s*new\s+([\w.]+)\s*;\s*$",
                    lines[j],
                )
                if mm:
                    typ = mm.group(1)
                    # rewrite that line to final form and drop init line
                    lines[j] = re.sub(
                        rf"val {re.escape(var)}:\s*{re.escape(typ)}\s*=\s*new\s+{re.escape(typ)}\s*;",
                        f"val {var} = {typ}({args})",
                        lines[j],
                    )
                    n2 += 1
                    i += 1
                    continue  # skip writing init line
            if typ is None:
                # fallback: comment
                line = f"{indent}// resugar ctor: {var}({args})\n{indent}{var}/*ctor*/\n"
                t2.append(line)
                n2 += 1
                i += 1
                continue
            # skipped init line
            continue
        t2.append(line)
        i += 1
    t = "".join(t2) if n2 else t

    t, n3 = PAT_LAMBDA.subn(r"\g<name>(", t)
    t, n4 = PAT_ILLEGAL.subn("", t)
    t, n5 = PAT_ILLEGAL2.subn("", t)

    # leftover $VF fragments
    t, n6 = re.subn(r"/\*\s*\$VF:[^*]*\*/", "", t)
    t, n7 = re.subn(r"/\*\s*\$VF was:[^*]*\*/", "", t)

    if t != orig:
        log.append(f"{rel}: split={n1} linefix={n2} lambda={n3} illegal={n4+n5} frag={n6+n7}")
    return t


for dp, _, fs in os.walk(ROOT):
    for fn in fs:
        if not fn.endswith((".kt", ".java")):
            continue
        path = os.path.join(dp, fn)
        rel = os.path.relpath(path, ROOT).replace("\\", "/")
        raw = open(path, encoding="utf-8", errors="replace").read()
        fixed = fix_text(raw, rel)
        if fixed != raw:
            open(path, "w", encoding="utf-8", newline="\n").write(fixed)

print("\n".join(log) if log else "no changes?")
# verify
u = v = c = 0
for dp, _, fs in os.walk(ROOT):
    for fn in fs:
        if not fn.endswith((".kt", ".java")):
            continue
        t = open(os.path.join(dp, fn), encoding="utf-8", errors="replace").read()
        u += t.count("<unrepresentable>")
        v += t.count("$VF")
        c += t.count("Unable to resugar")
print(f"remaining unrep={u} $VF={v} resugar={c}")
