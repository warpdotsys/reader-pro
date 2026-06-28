package me.ag2s.epublib.util;

import java.io.IOException;
import java.io.Writer;

/* JADX INFO: loaded from: reader-pro-classes-3.2.14.jar:me/ag2s/epublib/util/NoCloseWriter.class */
public class NoCloseWriter extends Writer {
    private final Writer writer;

    public NoCloseWriter(Writer writer) {
        this.writer = writer;
    }

    @Override // java.io.Writer, java.io.Closeable, java.lang.AutoCloseable
    public void close() {
    }

    @Override // java.io.Writer, java.io.Flushable
    public void flush() throws IOException {
        this.writer.flush();
    }

    @Override // java.io.Writer
    public void write(char[] cbuf, int off, int len) throws IOException {
        this.writer.write(cbuf, off, len);
    }
}
