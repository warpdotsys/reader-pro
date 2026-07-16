import re

p = r"C:\Users\chong\reader-pro-3.2.14-reverse\best-of-3\src\com\htmake\reader\api\controller\BookController.kt"
t = open(p, encoding="utf-8", errors="replace").read()
i = t.find("Couldn't be decompiled")
print("idx", i, "line", t[:i].count("\n") + 1 if i >= 0 else None)
if i >= 0:
    print(repr(t[max(0, i - 250) : i + 120]))
print("--- reconstructed fun names ---")
for m in re.finditer(r"MANUALLY RECONSTRUCTED[\s\S]{0,400}?fun (\w+)", t):
    print(" ", m.group(1))
# any Bytecode left
print("Bytecode sections", t.count("// Bytecode:"))
