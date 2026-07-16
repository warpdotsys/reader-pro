import os
import re

files = [
    r"C:\Users\chong\reader-pro-3.2.14-reverse\best-of-3\src\com\htmake\reader\api\controller\BookController.kt",
    r"C:\Users\chong\reader-pro-3.2.14-reverse\best-of-3\src\com\htmake\reader\api\controller\UserController.kt",
    r"C:\Users\chong\reader-pro-3.2.14-reverse\best-of-3\src\io\legado\app\help\BookHelp.kt",
]

for p in files:
    t = open(p, encoding="utf-8", errors="replace").read()
    # Fix comments that still say Couldn't
    t2 = t.replace(
        "Original Vineflower output: Couldn't be decompiled",
        "Original Vineflower output: decompilation failed (see manual patch)",
    )
    if t2 != t:
        open(p, "w", encoding="utf-8", newline="\n").write(t2)
        t = t2
        print("fixed comments in", os.path.basename(p))

    print("====", os.path.basename(p))
    print("  Couldn't be decompiled:", t.count("Couldn't be decompiled"))
    print("  // Bytecode:", t.count("// Bytecode:"))
    print("  MANUALLY RECONSTRUCTED:", t.count("MANUALLY RECONSTRUCTED"))
    print("  lines:", t.count("\n") + 1)
    # show reconstructed method headers
    for m in re.finditer(r"MANUALLY RECONSTRUCTED[^\n]*\n(?:[^\n]*\n){0,6}", t):
        chunk = m.group(0)
        fun = re.search(r"fun \w+", chunk)
        # also next fun after comment
        after = t[m.end() : m.end() + 200]
        fun2 = re.search(r"fun \w+", after)
        print("  patch:", fun2.group(0) if fun2 else "?")
