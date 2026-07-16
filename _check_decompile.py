import os
import re
import zipfile
from collections import defaultdict

out = r"C:\Users\chong\reader-pro-3.2.14-reverse"
src = os.path.join(out, "decompiled-src")
app_jar = os.path.join(out, "app-classes.jar")

with zipfile.ZipFile(app_jar) as z:
    class_names = [n for n in z.namelist() if n.endswith(".class")]

print("=== Coverage ===")
print(f"class files in app-classes.jar: {len(class_names)}")

java_files = []
for root, dirs, files in os.walk(src):
    for f in files:
        if f.endswith(".java"):
            java_files.append(os.path.join(root, f))
print(f"java files produced: {len(java_files)}")


def base_type(cn: str) -> str:
    cn = cn[:-6]  # strip .class
    if "$" in cn:
        cn = cn.split("$")[0]
    return cn


bases = set(base_type(c) for c in class_names)
java_bases = set()
for jf in java_files:
    rel = os.path.relpath(jf, src).replace("\\", "/")
    if rel.endswith(".java"):
        java_bases.add(rel[:-5])

missing = sorted(bases - java_bases)
extra = sorted(java_bases - bases)
print(f"unique base types from class: {len(bases)}")
print(f"unique base types from java:  {len(java_bases)}")
print(f"missing java for base type: {len(missing)}")
print(f"java without matching base class: {len(extra)}")
if missing:
    print("ALL missing base types:")
    for m in missing:
        # count related class files
        related = [c for c in class_names if base_type(c) == m]
        print(f"  {m}  ({len(related)} class files)")

# Quality markers
marker_keys = [
    "Unable to fully structure code",
    "Could not load the following classes",
    "Exception decompiling",
    "Decompilation failed",
    "Illegal identifiers",
    "** GOTO",
    "/* Error */",
    "throw new IllegalStateException(\"Decompilation failed\"",
]

files_with = defaultdict(list)
struct_bad = []
empty = []
hard_fail = []

for jf in java_files:
    with open(jf, encoding="utf-8", errors="replace") as f:
        text = f.read()
    lines = text.count("\n") + 1
    rel = os.path.relpath(jf, src).replace("\\", "/")
    emptyish = lines < 5 or len(text.strip()) < 50
    if emptyish:
        empty.append((rel, lines, len(text)))

    for k in marker_keys:
        c = text.count(k)
        if c:
            files_with[k].append((rel, c, lines))

    if "Unable to fully structure code" in text:
        struct_bad.append((rel, lines))
    if "Exception decompiling" in text or "Decompilation failed" in text:
        hard_fail.append((rel, lines))
    # CFR sometimes leaves almost empty stub
    if "/*" in text and "public class" not in text and "public final class" not in text and "public abstract class" not in text and "public enum" not in text and "public interface" not in text and "class " not in text and "interface " not in text and "enum " not in text:
        hard_fail.append((rel + " [no type decl?]", lines))

print()
print("=== Quality markers ===")
for k in marker_keys:
    items = files_with.get(k, [])
    total = sum(c for _, c, _ in items)
    if total:
        print(f"  {total:5d} occurrences in {len(items):3d} files  |  {k}")

print()
print(f'Files with "Unable to fully structure code": {len(struct_bad)}')
struct_bad.sort(key=lambda x: -x[1])
print("Largest incomplete-structure files:")
for rel, lines in struct_bad[:30]:
    print(f"  {lines:6d} lines  {rel}")

print()
print(f"Empty/tiny files: {len(empty)}")
for rel, lines, sz in empty[:30]:
    print(f"  {lines} lines ({sz} bytes)  {rel}")

print()
print(f"Hard fail candidates: {len(hard_fail)}")
for item in hard_fail[:30]:
    print(" ", item)

print()
print('Top files by "Could not load the following classes" header size:')
load_files = sorted(files_with.get("Could not load the following classes", []), key=lambda x: -x[1])[:20]
for rel, c, lines in load_files:
    print(f"  header_hits={c} lines={lines}  {rel}")

# Count synthetic classes not expected as separate java
synthetic = [c for c in class_names if "$" in c]
print()
print(f"Synthetic/inner class files ($): {len(synthetic)}")
print(f"Top-level class files (no $): {len(class_names) - len(synthetic)}")

# List top-level classes missing java more carefully
top_level = [c for c in class_names if "$" not in c]
top_bases = set(c[:-6] for c in top_level)
missing_top = sorted(top_bases - java_bases)
print(f"Top-level classes: {len(top_bases)}")
print(f"Missing top-level java: {len(missing_top)}")
for m in missing_top:
    print(f"  MISSING: {m}")
