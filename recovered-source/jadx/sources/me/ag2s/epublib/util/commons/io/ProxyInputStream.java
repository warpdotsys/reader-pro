package me.ag2s.epublib.util.commons.io;

import java.io.FilterInputStream;
import java.io.IOException;
import java.io.InputStream;
import me.ag2s.epublib.util.IOUtil;

/* JADX INFO: loaded from: app-classes.jar:me/ag2s/epublib/util/commons/io/ProxyInputStream.class */
public abstract class ProxyInputStream extends FilterInputStream {
    public ProxyInputStream(final InputStream proxy) {
        super(proxy);
    }

    @Override // java.io.FilterInputStream, java.io.InputStream
    public int read() throws IOException {
        try {
            beforeRead(1);
            int b = this.in.read();
            afterRead(b != -1 ? 1 : -1);
            return b;
        } catch (IOException e) {
            handleIOException(e);
            return -1;
        }
    }

    @Override // java.io.FilterInputStream, java.io.InputStream
    public int read(final byte[] bts) throws IOException {
        try {
            beforeRead(IOUtil.length(bts));
            int n = this.in.read(bts);
            afterRead(n);
            return n;
        } catch (IOException e) {
            handleIOException(e);
            return -1;
        }
    }

    @Override // java.io.FilterInputStream, java.io.InputStream
    public int read(final byte[] bts, final int off, final int len) throws IOException {
        try {
            beforeRead(len);
            int n = this.in.read(bts, off, len);
            afterRead(n);
            return n;
        } catch (IOException e) {
            handleIOException(e);
            return -1;
        }
    }

    @Override // java.io.FilterInputStream, java.io.InputStream
    public long skip(final long ln) throws IOException {
        try {
            return this.in.skip(ln);
        } catch (IOException e) {
            handleIOException(e);
            return 0L;
        }
    }

    @Override // java.io.FilterInputStream, java.io.InputStream
    public int available() throws IOException {
        try {
            return super.available();
        } catch (IOException e) {
            handleIOException(e);
            return 0;
        }
    }

    @Override // java.io.FilterInputStream, java.io.InputStream, java.io.Closeable, java.lang.AutoCloseable
    public void close() throws IOException {
        IOUtil.close(this.in, this::handleIOException);
    }

    @Override // java.io.FilterInputStream, java.io.InputStream
    public synchronized void mark(final int readlimit) {
        this.in.mark(readlimit);
    }

    @Override // java.io.FilterInputStream, java.io.InputStream
    public synchronized void reset() throws IOException {
        try {
            this.in.reset();
        } catch (IOException e) {
            handleIOException(e);
        }
    }

    @Override // java.io.FilterInputStream, java.io.InputStream
    public boolean markSupported() {
        return this.in.markSupported();
    }

    protected void beforeRead(final int n) {
    }

    protected void afterRead(final int n) {
    }

    protected void handleIOException(final IOException e) throws IOException {
        throw e;
    }
}
