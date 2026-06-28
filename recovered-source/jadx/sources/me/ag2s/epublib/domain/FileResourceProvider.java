package me.ag2s.epublib.domain;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

/* JADX INFO: loaded from: reader-pro-classes-3.2.14.jar:me/ag2s/epublib/domain/FileResourceProvider.class */
public class FileResourceProvider implements LazyResourceProvider {
    String dir;

    public FileResourceProvider(String parentDir) {
        this.dir = parentDir;
    }

    public FileResourceProvider(File parentFile) {
        this.dir = parentFile.getPath();
    }

    @Override // me.ag2s.epublib.domain.LazyResourceProvider
    public InputStream getResourceStream(String href) throws IOException {
        return new FileInputStream(new File(this.dir, href));
    }
}
