import re

p = r"C:\Users\chong\reader-pro-3.2.14-reverse\best-of-3\src\com\htmake\reader\api\controller\BookController.kt"
t = open(p, encoding="utf-8", errors="replace").read()
i = t.find("$VF: Couldn't be decompiled")
if i < 0:
    i = t.find("Couldn't be decompiled")
print("at", i, "line", t[:i].count("\n") + 1)

# show broader context
start = max(0, i - 1500)
end = min(len(t), i + 2500)
print(t[start:end])
