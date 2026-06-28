package me.ag2s.epublib.domain;

import java.io.FilterInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.zip.ZipFile;

/* JADX INFO: loaded from: app-classes.jar:me/ag2s/epublib/domain/ResourceInputStream.class */
public class ResourceInputStream extends FilterInputStream {
    private final ZipFile zipFile;

    public ResourceInputStream(InputStream in, ZipFile zipFile) {
        super(in);
        this.zipFile = zipFile;
    }

    @Override // java.io.FilterInputStream, java.io.InputStream, java.io.Closeable, java.lang.AutoCloseable
    public void close() throws IOException {
        super.close();
        this.zipFile.close();
    }
}
