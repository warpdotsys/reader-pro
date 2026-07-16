"""Extract alternative decompilations for failed methods."""
import os
import re

OUT = r"C:\Users\chong\reader-pro-3.2.14-reverse"
METHODS = {
    "BookController": [
        "saveBookCover",
        "getLocalChapterList",
        "saveShelfBookLatestChapter",
        "editShelfBook",
        "syncFromWebdav",
        "cacheBookOnServer",
        "getSpeakStream",
        "setCover",
    ],
    "UserController": ["logout"],
    "BookHelp": ["saveImage"],
}

SOURCES = {
    "cfr": {
        "BookController": os.path.join(OUT, r"full-cfr\com\htmake\reader\api\controller\BookController.java"),
        "UserController": os.path.join(OUT, r"full-cfr\com\htmake\reader\api\controller\UserController.java"),
        "BookHelp": os.path.join(OUT, r"full-cfr\io\legado\app\help\BookHelp.java"),
    },
    "pr": {
        "BookController": os.path.join(OUT, r"full-procyon\com\htmake\reader\api\controller\BookController.java"),
        "UserController": os.path.join(OUT, r"full-procyon\com\htmake\reader\api\controller\UserController.java"),
        "BookHelp": os.path.join(OUT, r"full-procyon\io\legado\app\help\BookHelp.java"),
    },
    "vf": {
        "BookController": os.path.join(OUT, r"best-of-3\src\com\htmake\reader\api\controller\BookController.kt"),
        "UserController": os.path.join(OUT, r"best-of-3\src\com\htmake\reader\api\controller\UserController.kt"),
        "BookHelp": os.path.join(OUT, r"best-of-3\src\io\legado\app\help\BookHelp.kt"),
    },
}


def extract_java_method(text: str, name: str) -> str:
    # Find method containing name(
    patterns = [
        rf"(?ms)^[ \t]*(?:public|private|protected|static|final|synchronized|\s)+[^\n]*\b{re.escape(name)}\s*\([^{{]*\{{",
        rf"(?ms)^[ \t]*.*\b{re.escape(name)}\s*\([^{{]*\{{",
    ]
    for pat in patterns:
        m = re.search(pat, text)
        if not m:
            continue
        start = m.start()
        # brace match
        i = m.end() - 1
        depth = 0
        while i < len(text):
            c = text[i]
            if c == "{":
                depth += 1
            elif c == "}":
                depth -= 1
                if depth == 0:
                    return text[start : i + 1]
            i += 1
    return ""


def extract_kt_method(text: str, name: str) -> str:
    # include failed bytecode body
    pat = rf"(?ms)^[ \t]*(?:public |private |protected |internal |open |override |suspend |final )*fun {re.escape(name)}\b[^\n]*\n"
    m = re.search(pat, text)
    if not m:
        # multi-line sig
        pat2 = rf"(?ms)^[ \t]*(?:public |private |protected |internal |open |override |suspend |final )*fun {re.escape(name)}\b.*?\)[^\n]*\{{"
        m = re.search(pat2, text)
    if not m:
        return ""
    # find opening brace after match
    start = m.start()
    brace = text.find("{", m.start())
    if brace < 0:
        return text[start : start + 500]
    i = brace
    depth = 0
    while i < len(text):
        c = text[i]
        if c == "{":
            depth += 1
        elif c == "}":
            depth -= 1
            if depth == 0:
                return text[start : i + 1]
        i += 1
    return text[start : start + 2000]


dump_dir = os.path.join(OUT, "method-alts")
os.makedirs(dump_dir, exist_ok=True)

for cls, methods in METHODS.items():
    for name in methods:
        for tool, paths in SOURCES.items():
            path = paths[cls]
            text = open(path, encoding="utf-8", errors="replace").read()
            body = extract_kt_method(text, name) if tool == "vf" else extract_java_method(text, name)
            outp = os.path.join(dump_dir, f"{cls}_{name}_{tool}.txt")
            with open(outp, "w", encoding="utf-8") as f:
                f.write(body if body else f"/* NOT FOUND in {tool} */\n")
            print(f"{cls}.{name} {tool}: {len(body)} chars -> {os.path.basename(outp)}")
