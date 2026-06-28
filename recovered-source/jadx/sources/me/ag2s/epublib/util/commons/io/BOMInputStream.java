package me.ag2s.epublib.util.commons.io;

import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import me.ag2s.epublib.util.IOUtil;

/* JADX INFO: loaded from: app-classes.jar:me/ag2s/epublib/util/commons/io/BOMInputStream.class */
public class BOMInputStream extends ProxyInputStream {
    private final boolean include;
    private final List<ByteOrderMark> boms;
    private ByteOrderMark byteOrderMark;
    private int[] firstBytes;
    private int fbLength;
    private int fbIndex;
    private int markFbIndex;
    private boolean markedAtStart;
    private static final Comparator<ByteOrderMark> ByteOrderMarkLengthComparator = (bom1, bom2) -> {
        int len1 = bom1.length();
        int len2 = bom2.length();
        return Integer.compare(len2, len1);
    };

    public BOMInputStream(final InputStream delegate) {
        this(delegate, false, ByteOrderMark.UTF_8);
    }

    public BOMInputStream(final InputStream delegate, final boolean include) {
        this(delegate, include, ByteOrderMark.UTF_8);
    }

    public BOMInputStream(final InputStream delegate, final ByteOrderMark... boms) {
        this(delegate, false, boms);
    }

    public BOMInputStream(final InputStream delegate, final boolean include, final ByteOrderMark... boms) {
        super(delegate);
        if (IOUtil.length(boms) == 0) {
            throw new IllegalArgumentException("No BOMs specified");
        }
        this.include = include;
        List<ByteOrderMark> list = Arrays.asList(boms);
        this.boms = list;
    }

    public boolean hasBOM() throws IOException {
        return getBOM() != null;
    }

    public boolean hasBOM(final ByteOrderMark bom) throws IOException {
        if (!this.boms.contains(bom)) {
            throw new IllegalArgumentException("Stream not configure to detect " + bom);
        }
        getBOM();
        return this.byteOrderMark != null && this.byteOrderMark.equals(bom);
    }

    public ByteOrderMark getBOM() throws IOException {
        if (this.firstBytes == null) {
            this.fbLength = 0;
            int maxBomSize = this.boms.get(0).length();
            this.firstBytes = new int[maxBomSize];
            for (int i = 0; i < this.firstBytes.length; i++) {
                this.firstBytes[i] = this.in.read();
                this.fbLength++;
                if (this.firstBytes[i] < 0) {
                    break;
                }
            }
            this.byteOrderMark = find();
            if (this.byteOrderMark != null && !this.include) {
                if (this.byteOrderMark.length() < this.firstBytes.length) {
                    this.fbIndex = this.byteOrderMark.length();
                } else {
                    this.fbLength = 0;
                }
            }
        }
        return this.byteOrderMark;
    }

    public String getBOMCharsetName() throws IOException {
        getBOM();
        if (this.byteOrderMark == null) {
            return null;
        }
        return this.byteOrderMark.getCharsetName();
    }

    private int readFirstBytes() throws IOException {
        getBOM();
        if (this.fbIndex >= this.fbLength) {
            return -1;
        }
        int[] iArr = this.firstBytes;
        int i = this.fbIndex;
        this.fbIndex = i + 1;
        return iArr[i];
    }

    private ByteOrderMark find() {
        for (ByteOrderMark bom : this.boms) {
            if (matches(bom)) {
                return bom;
            }
        }
        return null;
    }

    private boolean matches(final ByteOrderMark bom) {
        for (int i = 0; i < bom.length(); i++) {
            if (bom.get(i) != this.firstBytes[i]) {
                return false;
            }
        }
        return true;
    }

    @Override // me.ag2s.epublib.util.commons.io.ProxyInputStream, java.io.FilterInputStream, java.io.InputStream
    public int read() throws IOException {
        int b = readFirstBytes();
        return b >= 0 ? b : this.in.read();
    }

    @Override // me.ag2s.epublib.util.commons.io.ProxyInputStream, java.io.FilterInputStream, java.io.InputStream
    public int read(final byte[] buf, int off, int len) throws IOException {
        int firstCount = 0;
        int b = 0;
        while (len > 0 && b >= 0) {
            b = readFirstBytes();
            if (b >= 0) {
                int i = off;
                off++;
                buf[i] = (byte) (b & 255);
                len--;
                firstCount++;
            }
        }
        int secondCount = this.in.read(buf, off, len);
        if (secondCount >= 0) {
            return firstCount + secondCount;
        }
        if (firstCount > 0) {
            return firstCount;
        }
        return -1;
    }

    @Override // me.ag2s.epublib.util.commons.io.ProxyInputStream, java.io.FilterInputStream, java.io.InputStream
    public int read(final byte[] buf) throws IOException {
        return read(buf, 0, buf.length);
    }

    @Override // me.ag2s.epublib.util.commons.io.ProxyInputStream, java.io.FilterInputStream, java.io.InputStream
    public synchronized void mark(final int readlimit) {
        this.markFbIndex = this.fbIndex;
        this.markedAtStart = this.firstBytes == null;
        this.in.mark(readlimit);
    }

    @Override // me.ag2s.epublib.util.commons.io.ProxyInputStream, java.io.FilterInputStream, java.io.InputStream
    public synchronized void reset() throws IOException {
        this.fbIndex = this.markFbIndex;
        if (this.markedAtStart) {
            this.firstBytes = null;
        }
        this.in.reset();
    }

    @Override // me.ag2s.epublib.util.commons.io.ProxyInputStream, java.io.FilterInputStream, java.io.InputStream
    public long skip(final long n) throws IOException {
        int skipped = 0;
        while (n > skipped && readFirstBytes() >= 0) {
            skipped++;
        }
        return this.in.skip(n - ((long) skipped)) + ((long) skipped);
    }
}
