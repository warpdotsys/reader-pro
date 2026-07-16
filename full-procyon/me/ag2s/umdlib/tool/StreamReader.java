// 
// Decompiled by Procyon v0.6.0
// 

package me.ag2s.umdlib.tool;

import java.io.IOException;
import java.io.InputStream;

public class StreamReader
{
    private InputStream is;
    private long offset;
    private long size;
    
    public long getOffset() {
        return this.offset;
    }
    
    public void setOffset(final long offset) {
        this.offset = offset;
    }
    
    public long getSize() {
        return this.size;
    }
    
    public void setSize(final long size) {
        this.size = size;
    }
    
    private void incCount(final int value) {
        int temp = (int)(this.offset + value);
        if (temp < 0) {
            temp = Integer.MAX_VALUE;
        }
        this.offset = temp;
    }
    
    public StreamReader(final InputStream inputStream) throws IOException {
        this.is = inputStream;
    }
    
    public short readUint8() throws IOException {
        final byte[] b = { 0 };
        this.is.read(b);
        this.incCount(1);
        return (short)(b[0] & 0xFF);
    }
    
    public byte readByte() throws IOException {
        final byte[] b = { 0 };
        this.is.read(b);
        this.incCount(1);
        return b[0];
    }
    
    public byte[] readBytes(final int len) throws IOException {
        if (len < 1) {
            System.out.println(len);
            throw new IllegalArgumentException("Length must > 0: " + len);
        }
        final byte[] b = new byte[len];
        this.is.read(b);
        this.incCount(len);
        return b;
    }
    
    public String readHex(final int len) throws IOException {
        if (len < 1) {
            System.out.println(len);
            throw new IllegalArgumentException("Length must > 0: " + len);
        }
        final byte[] b = new byte[len];
        this.is.read(b);
        this.incCount(len);
        return UmdUtils.toHex(b);
    }
    
    public short readShort() throws IOException {
        final byte[] b = new byte[2];
        this.is.read(b);
        this.incCount(2);
        final short x = (short)((b[0] & 0xFF) << 8 | (b[1] & 0xFF) << 0);
        return x;
    }
    
    public short readShortLe() throws IOException {
        final byte[] b = new byte[2];
        this.is.read(b);
        this.incCount(2);
        final short x = (short)((b[1] & 0xFF) << 8 | (b[0] & 0xFF) << 0);
        return x;
    }
    
    public int readInt() throws IOException {
        final byte[] b = new byte[4];
        this.is.read(b);
        this.incCount(4);
        final int x = (b[0] & 0xFF) << 24 | (b[1] & 0xFF) << 16 | (b[2] & 0xFF) << 8 | (b[3] & 0xFF) << 0;
        return x;
    }
    
    public int readIntLe() throws IOException {
        final byte[] b = new byte[4];
        this.is.read(b);
        this.incCount(4);
        final int x = (b[3] & 0xFF) << 24 | (b[2] & 0xFF) << 16 | (b[1] & 0xFF) << 8 | (b[0] & 0xFF) << 0;
        return x;
    }
    
    public void skip(final int len) throws IOException {
        this.readBytes(len);
    }
    
    public byte[] read(final byte[] b) throws IOException {
        this.is.read(b);
        this.incCount(b.length);
        return b;
    }
    
    public byte[] read(final byte[] b, final int off, final int len) throws IOException {
        this.is.read(b, off, len);
        this.incCount(len);
        return b;
    }
}
