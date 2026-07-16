p = r"C:\Users\chong\reader-pro-3.2.14-reverse\_apply_manual_patches.py"
t = open(p, encoding="utf-8").read()
# Raw-string over-escaping: \$ -> $
t = t.replace("access\\$getLogger\\$p", "access$getLogger$p")
t = t.replace('\\"$chapterIndex.txt\\"', '"$chapterIndex.txt"')
t = t.replace('"\\$chapterIndex.txt"', '"$chapterIndex.txt"')
t = t.replace("\\${e.localizedMessage}", "${e.localizedMessage}")
t = t.replace("\\$speakText", "$speakText")
open(p, "w", encoding="utf-8").write(t)
print("ok")
# verify
t2 = open(p, encoding="utf-8").read()
print("logger sample", "access$getLogger$p" in t2, "access\\$getLogger" in t2)
print("chapter", '"$chapterIndex.txt"' in t2)
