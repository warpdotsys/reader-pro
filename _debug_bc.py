import re

p = r"C:\Users\chong\reader-pro-3.2.14-reverse\full-vineflower\com\htmake\reader\api\controller\BookController.kt"
t = open(p, encoding="utf-8", errors="replace").read()
pat = re.compile(r"//\s+\d+:\s+(aload|astore|invoke|goto|ldc|new|checkcast|if_)", re.I)
ms = list(pat.finditer(t))
print("matches", len(ms))
print("Couldn", t.count("Couldn"))
print("aload total", t.count("aload"))
for m in ms[:8]:
    i = m.start()
    print(repr(t[max(0, i - 40) : i + 90]))
for s in ["//   ", "Bytecode", "INVOKESPECIAL", "aload_0", "$FF", "Couldn't be decompiled"]:
    print(repr(s), t.count(s))

# UserController
p2 = r"C:\Users\chong\reader-pro-3.2.14-reverse\full-vineflower\com\htmake\reader\api\controller\UserController.kt"
t2 = open(p2, encoding="utf-8", errors="replace").read()
ms2 = list(pat.finditer(t2))
print("UserController matches", len(ms2), "Couldn", t2.count("Couldn"), "lines", t2.count("\n") + 1)
