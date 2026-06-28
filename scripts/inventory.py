#!/usr/bin/env python3
import hashlib
import os
import zipfile

jar = os.path.abspath(os.path.join(os.path.dirname(__file__), '..', 'app', 'reader-pro-3.2.14.jar'))
print('jar', jar)
print('size_bytes', os.path.getsize(jar))
h = hashlib.sha256()
with open(jar, 'rb') as fh:
    for chunk in iter(lambda: fh.read(1024 * 1024), b''):
        h.update(chunk)
print('sha256', h.hexdigest())
with zipfile.ZipFile(jar) as zf:
    names = zf.namelist()
    print('entries', len(names))
    print('classes', sum(1 for n in names if n.endswith('.class')))
    print('libs', sum(1 for n in names if n.startswith('BOOT-INF/lib/') and n.endswith('.jar')))
