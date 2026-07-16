import os

out = r"C:\Users\chong\reader-pro-3.2.14-reverse"
paths = [
    ("full-vf-bcl", os.path.join(out, r"full-vineflower\io\legado\app\model\webBook\BookChapterList.kt")),
    ("retry-vf-bcl", os.path.join(out, r"retry-vineflower\io\legado\app\model\webBook\BookChapterList.kt")),
    ("full-vf-bc", os.path.join(out, r"full-vineflower\com\htmake\reader\api\controller\BookController.kt")),
    ("retry-vf-bc", os.path.join(out, r"retry-vineflower\com\htmake\reader\api\controller\BookController.kt")),
]
for label, p in paths:
    t = open(p, encoding="utf-8", errors="replace").read()
    print(
        label,
        "lines",
        t.count("\n") + 1,
        "couldnt",
        t.count("Couldn't"),
        "aload comments",
        t.count("aload"),
        "suspend",
        t.count("suspend "),
    )
    print("  analyzeChapterList body-ish:", "suspend fun analyzeChapterList" in t)
