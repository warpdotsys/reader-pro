import os
import re

out = r"C:\Users\chong\reader-pro-3.2.14-reverse\best-of-3\src"
files = [
    r"com\htmake\reader\api\controller\BookController.kt",
    r"com\htmake\reader\api\controller\UserController.kt",
    r"io\legado\app\help\BookHelp.kt",
]

for rel in files:
    p = os.path.join(out, rel)
    t = open(p, encoding="utf-8", errors="replace").read()
    print("====", rel, "lines", t.count("\n") + 1)
    # Find blocks with failure
    for m in re.finditer(
        r"Couldn't be decompiled|Exception decompiling|This method has failed to decompile",
        t,
    ):
        # walk back to method signature
        before = t[: m.start()]
        # last fun/line with (
        sigs = list(
            re.finditer(
                r"(?m)^[ \t]*(?:/\*[^*]*\*/[ \t]*)?(?:public |private |protected |internal |open |override |suspend |final |abstract )*[^\n]{0,200}\{?\s*$",
                before[-2000:],
            )
        )
        # simpler: last line with fun or ) {
        lines_before = before.splitlines()
        sig = ""
        for line in reversed(lines_before[-80:]):
            if re.search(r"\b(fun|constructor)\b", line) or (
                "(" in line and ")" in line and not line.strip().startswith("//")
            ):
                if "Couldn't" in line or "Bytecode" in line:
                    continue
                sig = line.strip()
                if "fun " in line or line.strip().endswith("{") or "):" in line:
                    break
        line_no = before.count("\n") + 1
        print(f"  L{line_no}: {sig[:140]}")

    # Also list // Bytecode: section starts with preceding method-ish line
    print("  --- bytecode sections ---")
    for m in re.finditer(r"(?m)^[ \t]*// Bytecode:", t):
        before = t[: m.start()].splitlines()
        sig = ""
        for line in reversed(before[-40:]):
            s = line.strip()
            if not s or s.startswith("//") or s.startswith("/*") or s.startswith("*"):
                continue
            sig = s[:140]
            break
        line_no = t[: m.start()].count("\n") + 1
        print(f"  L{line_no}: near {sig}")
