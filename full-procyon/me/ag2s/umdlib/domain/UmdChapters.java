// 
// Decompiled by Procyon v0.6.0
// 

package me.ag2s.umdlib.domain;

import java.io.File;
import java.io.OutputStream;
import java.util.zip.DeflaterOutputStream;
import java.util.Arrays;
import java.util.Iterator;
import java.io.IOException;
import me.ag2s.umdlib.tool.WrapOutputStream;
import me.ag2s.umdlib.tool.UmdUtils;
import java.util.ArrayList;
import java.io.ByteArrayOutputStream;
import java.util.List;

public class UmdChapters
{
    private static final int DEFAULT_CHUNK_INIT_SIZE = 32768;
    private int TotalContentLen;
    private List<byte[]> titles;
    public List<Integer> contentLengths;
    public ByteArrayOutputStream contents;
    
    public UmdChapters() {
        this.titles = new ArrayList<byte[]>();
        this.contentLengths = new ArrayList<Integer>();
        this.contents = new ByteArrayOutputStream();
    }
    
    public List<byte[]> getTitles() {
        return this.titles;
    }
    
    public void addTitle(final String s) {
        this.titles.add(UmdUtils.stringToUnicodeBytes(s));
    }
    
    public void addTitle(final byte[] s) {
        this.titles.add(s);
    }
    
    public void addContentLength(final Integer integer) {
        this.contentLengths.add(integer);
    }
    
    public int getContentLength(final int index) {
        return this.contentLengths.get(index);
    }
    
    public byte[] getContent(final int index) {
        final int st = this.contentLengths.get(index);
        final byte[] b = this.contents.toByteArray();
        final int end = (index + 1 < this.contentLengths.size()) ? this.contentLengths.get(index + 1) : this.getTotalContentLen();
        System.out.println("\u603b\u957f\u5ea6:" + this.contents.size());
        System.out.println("\u8d77\u59cb\u503c:" + st);
        System.out.println("\u7ed3\u675f\u503c:" + end);
        final byte[] bAr = new byte[end - st];
        System.arraycopy(b, st, bAr, 0, bAr.length);
        return bAr;
    }
    
    public String getContentString(final int index) {
        return UmdUtils.unicodeBytesToString(this.getContent(index)).replace('\u2029', '\n');
    }
    
    public String getTitle(final int index) {
        return UmdUtils.unicodeBytesToString(this.titles.get(index));
    }
    
    public void buildChapters(final WrapOutputStream wos) throws IOException {
        this.writeChaptersHead(wos);
        this.writeChaptersContentOffset(wos);
        this.writeChaptersTitles(wos);
        this.writeChaptersChunks(wos);
    }
    
    private void writeChaptersHead(final WrapOutputStream wos) throws IOException {
        wos.writeBytes(35, 11, 0, 0, 9);
        wos.writeInt(this.contents.size());
    }
    
    private void writeChaptersContentOffset(final WrapOutputStream wos) throws IOException {
        wos.writeBytes(35, 131, 0, 0, 9);
        final byte[] rb = UmdUtils.genRandomBytes(4);
        wos.writeBytes(rb);
        wos.write(36);
        wos.writeBytes(rb);
        wos.writeInt(this.contentLengths.size() * 4 + 9);
        int offset = 0;
        for (final Integer n : this.contentLengths) {
            wos.writeInt(offset);
            offset += n;
        }
    }
    
    private void writeChaptersTitles(final WrapOutputStream wos) throws IOException {
        wos.writeBytes(35, 132, 0, 1, 9);
        final byte[] rb = UmdUtils.genRandomBytes(4);
        wos.writeBytes(rb);
        wos.write(36);
        wos.writeBytes(rb);
        int totalTitlesLen = 0;
        for (final byte[] t : this.titles) {
            totalTitlesLen += t.length;
        }
        wos.writeInt(totalTitlesLen + this.titles.size() + 9);
        for (final byte[] t : this.titles) {
            wos.writeByte(t.length);
            wos.write(t);
        }
    }
    
    private void writeChaptersChunks(final WrapOutputStream wos) throws IOException {
        final byte[] allContents = this.contents.toByteArray();
        final byte[] zero16 = new byte[16];
        Arrays.fill(zero16, 0, zero16.length, (byte)0);
        int startPos = 0;
        int len = 0;
        int left = 0;
        int chunkCnt = 0;
        final ByteArrayOutputStream bos = new ByteArrayOutputStream(33024);
        final List<byte[]> chunkRbList = new ArrayList<byte[]>();
        while (startPos < allContents.length) {
            left = allContents.length - startPos;
            len = ((32768 < left) ? 32768 : left);
            bos.reset();
            final DeflaterOutputStream zos = new DeflaterOutputStream(bos);
            zos.write(allContents, startPos, len);
            zos.close();
            final byte[] chunk = bos.toByteArray();
            final byte[] rb = UmdUtils.genRandomBytes(4);
            wos.writeByte(36);
            wos.writeBytes(rb);
            chunkRbList.add(rb);
            wos.writeInt(chunk.length + 9);
            wos.write(chunk);
            wos.writeBytes(35, 241, 0, 0, 21);
            wos.write(zero16);
            startPos += len;
            ++chunkCnt;
        }
        wos.writeBytes(35, 129, 0, 1, 9);
        wos.writeBytes(0, 0, 0, 0);
        wos.write(36);
        wos.writeBytes(0, 0, 0, 0);
        wos.writeInt(chunkCnt * 4 + 9);
        for (int i = chunkCnt - 1; i >= 0; --i) {
            wos.writeBytes((byte[])chunkRbList.get(i));
        }
    }
    
    public void addChapter(final String title, final String content) {
        this.titles.add(UmdUtils.stringToUnicodeBytes(title));
        final byte[] b = UmdUtils.stringToUnicodeBytes(content);
        this.contentLengths.add(b.length);
        try {
            this.contents.write(b);
        }
        catch (final IOException e) {
            throw new RuntimeException(e);
        }
    }
    
    public void addFile(final File f, final String title) throws IOException {
        final byte[] temp = UmdUtils.readFile(f);
        final String s = new String(temp);
        this.addChapter(title, s);
    }
    
    public void addFile(final File f) throws IOException {
        String s = f.getName();
        final int idx = s.lastIndexOf(46);
        if (idx >= 0) {
            s = s.substring(0, idx);
        }
        this.addFile(f, s);
    }
    
    public void clearChapters() {
        this.titles.clear();
        this.contentLengths.clear();
        this.contents.reset();
    }
    
    public int getTotalContentLen() {
        return this.TotalContentLen;
    }
    
    public void setTotalContentLen(final int totalContentLen) {
        this.TotalContentLen = totalContentLen;
    }
}
