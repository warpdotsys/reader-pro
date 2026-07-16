# -*- coding: utf-8 -*-
"""
Mass-fix best-of-3 sources:
1) Replace <unrepresentable> with readable Synthetic* placeholders
2) Normalize common continuation patterns
3) Strip non-actionable $VF noise comments (keep irreducible warnings lightly annotated)
4) Fix obvious illegal patterns
5) Inject shared SyntheticTypes.kt for documentation
"""
from __future__ import annotations

import os
import re
import shutil
from datetime import datetime

ROOT = r"C:\Users\chong\reader-pro-3.2.14-reverse\best-of-3\src"
OUT = r"C:\Users\chong\reader-pro-3.2.14-reverse"
BAK = os.path.join(OUT, "manual-patches", f"backup-massfix-{datetime.now().strftime('%Y%m%d-%H%M%S')}")
LOG = os.path.join(OUT, "manual-patches", "MASS_FIX_LOG.txt")

# Continuations that appear as `$completion is <unrepresentable>`
CONT_PATTERNS = [
    # if (`$completion` is <unrepresentable>) {
    (
        re.compile(r"(`\$completion`\s+is\s+)<unrepresentable>"),
        r"\1SyntheticContinuation",
    ),
    (
        re.compile(r"(\$completion\s+is\s+)<unrepresentable>"),
        r"\1SyntheticContinuation",
    ),
    # as <unrepresentable>
    (
        re.compile(r"(\bas\s+)<unrepresentable>"),
        r"\1SyntheticContinuation",
    ),
    # create(...) as <unrepresentable>
    (
        re.compile(r"(as\s+)<unrepresentable>(\s*\)\.invokeSuspend)"),
        r"\1SyntheticContinuation\2",
    ),
]

# Lambda / lazy INSTANCE placeholders
INSTANCE_PATTERNS = [
    (
        re.compile(r"KotlinLogging\.INSTANCE\.logger\(\s*<unrepresentable>\.INSTANCE\s*\)"),
        "KotlinLogging.INSTANCE.logger(SyntheticFunction0.INSTANCE)",
    ),
    (
        re.compile(r"LazyKt\.lazy\(\s*<unrepresentable>\.INSTANCE\s*\)"),
        "LazyKt.lazy(SyntheticFunction0.INSTANCE)",
    ),
    (
        re.compile(r"by\s+LazyKt\.lazy\(\s*<unrepresentable>\.INSTANCE\s*\)"),
        "by LazyKt.lazy(SyntheticFunction0.INSTANCE)",
    ),
    (
        re.compile(r"by\s+lazy\(\s*<unrepresentable>\.INSTANCE\s*\)"),
        "by lazy(SyntheticFunction0.INSTANCE)",
    ),
    (
        re.compile(r"<unrepresentable>\.INSTANCE"),
        "SyntheticFunction0.INSTANCE",
    ),
    (
        re.compile(r"<unrepresentable>::([\w$]+)"),
        r"SyntheticFunction0::\1",
    ),
    # type position: Map / lock map
    (
        re.compile(r":\s*<unrepresentable>\s*="),
        ": Any /* SyntheticType */ =",
    ),
    (
        re.compile(r"<unrepresentable>"),
        "SyntheticType",
    ),
]

# $VF comments to remove (noise) or rewrite
VF_REMOVE = [
    re.compile(r"(?m)^[ \t]*// \$VF: Handled exception range with multiple entry points by splitting it\s*\n"),
    re.compile(r"(?m)^[ \t]*// \$VF: Duplicated exception handlers to handle obfuscated exceptions\s*\n"),
    re.compile(r"(?m)^[ \t]*// \$VF: Class flags could not be determined\s*\n"),
]
VF_REWRITE = [
    (
        re.compile(r"// \$VF: Irreducible bytecode was duplicated to produce valid code"),
        "// NOTE: irreducible bytecode was split/duplicated by decompiler; logic preserved",
    ),
    (
        re.compile(r"// \$VF: monitorenter"),
        "// synchronized enter",
    ),
    (
        re.compile(r"// \$VF: monitorexit"),
        "// synchronized exit",
    ),
    (
        re.compile(r"// \$VF:[^\n]*"),
        "",  # drop remaining $VF tags
    ),
]

# Fix empty synchronized artifacts
SYNC_FIXES = [
    (
        re.compile(r"synchronized\s*\(\s*this\s*\)\s*\{\s*\}"),
        "synchronized(this) { /* monitor body (decompiler collapsed) */ }",
    ),
]


def ensure_synthetic_types():
    path = os.path.join(ROOT, "com", "htmake", "reader", "synth", "SyntheticTypes.kt")
    os.makedirs(os.path.dirname(path), exist_ok=True)
    content = r'''package com.htmake.reader.synth

import kotlin.coroutines.Continuation
import kotlin.coroutines.CoroutineContext
import kotlin.coroutines.EmptyCoroutineContext
import kotlin.jvm.functions.Function0

/**
 * Placeholders for types Vineflower could not represent from Kotlin metadata / synthetics.
 * Used only for reverse-engineering readability — not for production compilation fidelity.
 *
 * Injected by mass-fix pass against reader-pro-3.2.14.jar decompilation.
 */
open class SyntheticType

/**
 * Stand-in for synthetic ContinuationImpl subclasses generated per suspend function.
 * Real classes are BookController$foo$1 etc. in the JAR.
 */
open class SyntheticContinuation(
    completion: Continuation<Any?>? = null
) : Continuation<Any?> {
    @JvmField var result: Any? = null
    @JvmField var label: Int = 0
    override val context: CoroutineContext
        get() = EmptyCoroutineContext
    override fun resumeWith(result: Result<Any?>) {}
    open fun invokeSuspend(result: Any?): Any? = result
}

/**
 * Stand-in for compiler-generated Function0 singletons (lazy/logger SAM adapters).
 */
object SyntheticFunction0 : Function0<Any?> {
    @JvmField
    val INSTANCE: Function0<Any?> = this
    override fun invoke(): Any? = null
}
'''
    open(path, "w", encoding="utf-8", newline="\n").write(content)
    return path


def add_import_if_needed(text: str, rel: str) -> str:
    if "SyntheticContinuation" not in text and "SyntheticType" not in text and "SyntheticFunction0" not in text:
        return text
    if "com.htmake.reader.synth" in text:
        return text
    # only for kotlin
    if not rel.endswith(".kt"):
        # java: use fully qualified or simple comment types already inlined as SyntheticType
        # For java files replace already done as names - add no import if .java
        if rel.endswith(".java"):
            # use fully qualified names instead
            text = text.replace("SyntheticContinuation", "com.htmake.reader.synth.SyntheticContinuation")
            text = text.replace("SyntheticFunction0", "com.htmake.reader.synth.SyntheticFunction0")
            text = text.replace("SyntheticType", "com.htmake.reader.synth.SyntheticType")
            # avoid double-prefix
            text = text.replace(
                "com.htmake.reader.synth.com.htmake.reader.synth.",
                "com.htmake.reader.synth.",
            )
        return text
    # insert after package
    m = re.match(r"(package [^\n]+\n)", text)
    if not m:
        return text
    insert = (
        m.group(1)
        + "\n"
        + "import com.htmake.reader.synth.SyntheticContinuation\n"
        + "import com.htmake.reader.synth.SyntheticFunction0\n"
        + "import com.htmake.reader.synth.SyntheticType\n"
    )
    return insert + text[m.end() :]


def fix_file(text: str) -> tuple[str, dict]:
    stats = defaultdict_int = {}
    counts = {
        "unrep_before": text.count("<unrepresentable>"),
        "vf_before": text.count("$VF:"),
    }
    # continuation-specific first
    for cre, rep in CONT_PATTERNS:
        text, n = cre.subn(rep, text)
    # instance / remaining unrepresentable
    for cre, rep in INSTANCE_PATTERNS:
        text, n = cre.subn(rep, text)
    # vf comments
    for cre in VF_REMOVE:
        text, n = cre.subn("", text)
    for cre, rep in VF_REWRITE:
        text, n = cre.subn(rep, text)
    for cre, rep in SYNC_FIXES:
        text, n = cre.subn(rep, text)

    # clean empty comment lines doubled
    text = re.sub(r"\n{3,}", "\n\n", text)

    # illegal identifier: backticks already ok; fix `...` empty
    # rename cfr-style illegal if present as comments only

    counts["unrep_after"] = text.count("<unrepresentable>")
    counts["vf_after"] = text.count("$VF:")
    counts["synth"] = text.count("Synthetic")
    return text, counts


def defaultdict_int():
    return 0


def main():
    os.makedirs(BAK, exist_ok=True)
    log = [f"mass fix at {datetime.now().isoformat()}", f"backup: {BAK}"]

    synth_path = ensure_synthetic_types()
    log.append(f"wrote {synth_path}")

    totals = {
        "files": 0,
        "changed": 0,
        "unrep_removed": 0,
        "vf_removed": 0,
    }

    for dp, _, fs in os.walk(ROOT):
        for fn in fs:
            if not fn.endswith((".kt", ".java")):
                continue
            path = os.path.join(dp, fn)
            rel = os.path.relpath(path, ROOT).replace("\\", "/")
            if rel.endswith("SyntheticTypes.kt"):
                continue
            raw = open(path, encoding="utf-8", errors="replace").read()
            totals["files"] += 1
            fixed, counts = fix_file(raw)
            fixed = add_import_if_needed(fixed, rel)
            if fixed != raw:
                # backup
                bpath = os.path.join(BAK, rel)
                os.makedirs(os.path.dirname(bpath), exist_ok=True)
                shutil.copy2(path, bpath)
                open(path, "w", encoding="utf-8", newline="\n").write(fixed)
                totals["changed"] += 1
                totals["unrep_removed"] += counts["unrep_before"] - counts["unrep_after"]
                totals["vf_removed"] += counts["vf_before"] - counts["vf_after"]
                log.append(
                    f"FIX {rel}: unrep {counts['unrep_before']}->{counts['unrep_after']} "
                    f"vf {counts['vf_before']}->{counts['vf_after']}"
                )

    # second pass: any remaining <unrepresentable>
    remain_unrep = 0
    remain_vf = 0
    remain_files = []
    for dp, _, fs in os.walk(ROOT):
        for fn in fs:
            if not fn.endswith((".kt", ".java")):
                continue
            path = os.path.join(dp, fn)
            t = open(path, encoding="utf-8", errors="replace").read()
            u = t.count("<unrepresentable>")
            v = t.count("$VF:")
            remain_unrep += u
            remain_vf += v
            if u or v:
                remain_files.append((os.path.relpath(path, ROOT), u, v))

    log.append("=== totals ===")
    log.append(str(totals))
    log.append(f"remaining <unrepresentable>: {remain_unrep}")
    log.append(f"remaining $VF: {remain_vf}")
    for item in remain_files[:30]:
        log.append(f"  remain {item}")

    open(LOG, "w", encoding="utf-8").write("\n".join(log))
    print("\n".join(log[-40:]))
    print("log:", LOG)


if __name__ == "__main__":
    main()
