import re, os, zipfile, json
from collections import defaultdict

out_root = r"C:\Users\chong\reader-pro-3.2.14-reverse"
src = os.path.join(out_root, "decompiled-src")
jar = r"H:\下载\reader-pro-3.2.14.jar"

# 1) All API routes
yuedu = os.path.join(src, r"com\htmake\reader\api\YueduApi.java")
with open(yuedu, encoding="utf-8", errors="replace") as f:
    text = f.read()
routes = re.findall(r'var1_1\.(get|post|put|delete)\("(/reader3/[^"]+)"\)', text)
# also route(
static_routes = re.findall(r'var1_1\.route\("([^"]+)"\)', text)
unique = []
seen = set()
for method, path in routes:
    key = (method.upper(), path)
    if key not in seen:
        seen.add(key)
        unique.append(key)

api_md = []
api_md.append("# reader-pro 3.2.14 API Routes\n")
api_md.append(f"Extracted from YueduApi.initRouter, total unique endpoints: {len(unique)}\n")
by_prefix = defaultdict(list)
for m, p in unique:
    parts = p.strip("/").split("/")
    group = parts[1] if len(parts) > 1 else "root"
    # better group by keyword
    name = parts[-1] if parts else p
    if "BookSource" in p or "bookSource" in p or "Source" in p and "Rss" not in p:
        g = "BookSource"
    elif "Rss" in p or "rss" in p:
        g = "RSS"
    elif "BookGroup" in p or "Group" in p:
        g = "BookGroup"
    elif "Bookmark" in p or "bookmark" in p:
        g = "Bookmark"
    elif "Replace" in p:
        g = "ReplaceRule"
    elif "User" in p or "login" in p or "logout" in p or "Password" in p:
        g = "User"
    elif "License" in p or "license" in p or "Keys" in p or "HostValid" in p or "Email" in p:
        g = "License"
    elif "Webdav" in p or "webdav" in p:
        g = "WebDAV"
    elif "File" in p or "upload" in p:
        g = "File"
    elif "tts" in p.lower() or "TTS" in p:
        g = "TTS"
    elif "Mongodb" in p or "Mongo" in p:
        g = "MongoBackup"
    else:
        g = "Book"
    by_prefix[g].append((m, p))

for g in sorted(by_prefix.keys()):
    api_md.append(f"\n## {g}\n")
    for m, p in by_prefix[g]:
        api_md.append(f"- `{m:6} {p}`")

api_md.append("\n## Static routes\n")
for r in static_routes:
    api_md.append(f"- `ROUTE  {r}`")

with open(os.path.join(out_root, "API_ROUTES.md"), "w", encoding="utf-8") as f:
    f.write("\n".join(api_md))
print(f"API routes: {len(unique)}")

# 2) Lib list
with zipfile.ZipFile(jar) as z:
    libs = sorted(n.split("/")[-1] for n in z.namelist() if n.startswith("BOOT-INF/lib/") and n.endswith(".jar"))
with open(os.path.join(out_root, "DEPENDENCIES.md"), "w", encoding="utf-8") as f:
    f.write("# BOOT-INF/lib dependencies\n\n")
    for lib in libs:
        f.write(f"- {lib}\n")
print(f"Libs: {len(libs)}")

# 3) Source inventory
inv = []
for root, dirs, files in os.walk(src):
    for fn in files:
        if fn.endswith(".java"):
            full = os.path.join(root, fn)
            rel = os.path.relpath(full, src).replace("\\", "/")
            lines = sum(1 for _ in open(full, encoding="utf-8", errors="replace"))
            inv.append((rel, lines))
inv.sort()
with open(os.path.join(out_root, "SOURCE_INVENTORY.md"), "w", encoding="utf-8") as f:
    f.write(f"# Decompiled source inventory ({len(inv)} files)\n\n")
    f.write("| File | Lines |\n|---|---:|\n")
    for rel, lines in inv:
        f.write(f"| `{rel}` | {lines} |\n")
print(f"Source files: {len(inv)}, total lines: {sum(l for _,l in inv)}")

# 4) Class size of key controllers
for name in ["BookController.java", "LicenseController.java", "UserController.java", "YueduApi.java", "WebBook.java", "AnalyzeRule.java"]:
    for rel, lines in inv:
        if rel.endswith(name):
            print(f"  {rel}: {lines} lines")
