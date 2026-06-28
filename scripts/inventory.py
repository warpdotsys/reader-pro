#!/usr/bin/env python3
"""Validate the recovered reader-pro 3.2.14 artifact inventory."""
from pathlib import Path
import argparse, hashlib, zipfile

EXPECTED_SHA256 = "b26fb4769d689d98ff26408ce79a275d719f360906c84acf52ff404e98030c8c"
EXPECTED_START_CLASS = "com.htmake.reader.ReaderApplicationKt"
EXPECTED_BOOT_VERSION = "2.1.6.RELEASE"

def sha256_file(path: Path) -> str:
    h = hashlib.sha256()
    with path.open('rb') as f:
        for chunk in iter(lambda: f.read(1024 * 1024), b''):
            h.update(chunk)
    return h.hexdigest()

def parse_manifest(text: str) -> dict[str, str]:
    result = {}
    for line in text.splitlines():
        if ': ' in line:
            key, value = line.split(': ', 1)
            result[key] = value
    return result

def main() -> int:
    parser = argparse.ArgumentParser()
    parser.add_argument('--jar', default='app/reader-pro-3.2.14.jar')
    args = parser.parse_args()
    jar = Path(args.jar)
    digest = sha256_file(jar)
    if digest != EXPECTED_SHA256:
        raise SystemExit(f'SHA-256 mismatch: {digest} != {EXPECTED_SHA256}')
    with zipfile.ZipFile(jar) as zf:
        names = zf.namelist()
        manifest = parse_manifest(zf.read('META-INF/MANIFEST.MF').decode('utf-8', 'replace'))
        class_count = sum(1 for n in names if n.startswith('BOOT-INF/classes/') and n.endswith('.class'))
        lib_count = sum(1 for n in names if n.startswith('BOOT-INF/lib/') and n.endswith('.jar'))
        resource_count = sum(1 for n in names if n.startswith('BOOT-INF/classes/') and not n.endswith('/') and not n.endswith('.class'))
    if manifest.get('Start-Class') != EXPECTED_START_CLASS:
        raise SystemExit(f"Start-Class mismatch: {manifest.get('Start-Class')}")
    if manifest.get('Spring-Boot-Version') != EXPECTED_BOOT_VERSION:
        raise SystemExit(f"Spring Boot version mismatch: {manifest.get('Spring-Boot-Version')}")
    print(f'jar={jar}')
    print(f'sha256={digest}')
    print(f'size={jar.stat().st_size}')
    print(f'start_class={manifest.get("Start-Class")}')
    print(f'spring_boot={manifest.get("Spring-Boot-Version")}')
    print(f'classes={class_count}')
    print(f'libraries={lib_count}')
    print(f'classpath_resources={resource_count}')
    return 0

if __name__ == '__main__':
    raise SystemExit(main())
