"""Better extract: find method by name + Continuation parameter (suspend)."""
import os
import re

OUT = r"C:\Users\chong\reader-pro-3.2.14-reverse"
targets = {
    os.path.join(OUT, r"full-cfr\com\htmake\reader\api\controller\BookController.java"): [
        "saveBookCover",
        "getLocalChapterList",
        "saveShelfBookLatestChapter",
        "editShelfBook",
        "syncFromWebdav",
        "cacheBookOnServer",
        "getSpeakStream",
        "setCover",
    ],
    os.path.join(OUT, r"full-cfr\com\htmake\reader\api\controller\UserController.java"): ["logout"],
    os.path.join(OUT, r"full-cfr\io\legado\app\help\BookHelp.java"): ["saveImage"],
    os.path.join(OUT, r"full-procyon\com\htmake\reader\api\controller\BookController.java"): [
        "saveBookCover",
        "getLocalChapterList",
        "saveShelfBookLatestChapter",
        "editShelfBook",
        "syncFromWebdav",
        "cacheBookOnServer",
        "getSpeakStream",
        "setCover",
    ],
    os.path.join(OUT, r"full-procyon\com\htmake\reader\api\controller\UserController.java"): ["logout"],
    os.path.join(OUT, r"full-procyon\io\legado\app\help\BookHelp.java"): ["saveImage"],
}


def extract_all_named(text, name):
    """Return all top-level-ish methods matching name( """
    results = []
    # find all occurrences of " name(" with method-like prefix
    for m in re.finditer(rf"\b{re.escape(name)}\s*\(", text):
        # walk back to start of line / method decl
        line_start = text.rfind("\n", 0, m.start()) + 1
        # include previous lines with annotations/modifiers (up to 15 lines)
        lines_back = text[:line_start].splitlines()
        start_line_idx = max(0, len(lines_back) - 12)
        # find a line with public/private/protected before
        start = line_start
        for i in range(len(lines_back) - 1, max(-1, len(lines_back) - 15), -1):
            if re.search(r"\b(public|private|protected)\b", lines_back[i]):
                # compute char offset
                start = len("\n".join(lines_back[:i])) + (1 if i > 0 else 0)
                break
        # find opening brace after name(
        brace = text.find("{", m.start())
        if brace < 0:
            continue
        # skip if this looks like a call not a definition: previous non-space is not ) or modifiers
        prefix = text[start:m.start()].strip()
        if not re.search(r"(public|private|protected|static|final|Object|void|Boolean|Book|List|Unit)", prefix):
            continue
        if "(" in prefix.split("\n")[-1] and "Object" not in prefix and "fun" not in prefix:
            # might still be ok for java
            pass
        depth = 0
        i = brace
        while i < len(text):
            if text[i] == "{":
                depth += 1
            elif text[i] == "}":
                depth -= 1
                if depth == 0:
                    body = text[start : i + 1]
                    # filter tiny call sites
                    if len(body) > 200 or "Continuation" in body or "switch" in body or "Mutex" in body:
                        results.append(body)
                    break
            i += 1
    # unique by length keep longest few
    results.sort(key=len, reverse=True)
    uniq = []
    for r in results:
        if not any(r in u for u in uniq):
            uniq.append(r)
    return uniq[:3]


dump = os.path.join(OUT, "method-alts2")
os.makedirs(dump, exist_ok=True)
for path, names in targets.items():
    tag = "cfr" if "full-cfr" in path else "pr"
    text = open(path, encoding="utf-8", errors="replace").read()
    base = os.path.basename(path).replace(".java", "")
    for name in names:
        bodies = extract_all_named(text, name)
        for i, body in enumerate(bodies):
            outp = os.path.join(dump, f"{base}_{name}_{tag}_{i}.txt")
            open(outp, "w", encoding="utf-8").write(body)
            print(f"{base}.{name} {tag}[{i}]: {len(body)} chars, lines={body.count(chr(10))+1}")
        if not bodies:
            print(f"{base}.{name} {tag}: NOT FOUND")
