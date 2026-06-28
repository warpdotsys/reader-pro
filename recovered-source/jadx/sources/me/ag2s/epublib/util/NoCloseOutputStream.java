package me.ag2s.epublib.util;

import java.io.IOException;
import java.io.OutputStream;

/* JADX INFO: loaded from: app-classes.jar:me/ag2s/epublib/util/NoCloseOutputStream.class */
public class NoCloseOutputStream extends OutputStream {
    private final OutputStream outputStream;

    public NoCloseOutputStream(OutputStream outputStream) {
        this.outputStream = outputStream;
    }

    @Override // java.io.OutputStream
    public void write(int b) throws IOException {
        this.outputStream.write(b);
    }

    @Override // java.io.OutputStream, java.io.Closeable, java.lang.AutoCloseable
    public void close() {
    }
}
