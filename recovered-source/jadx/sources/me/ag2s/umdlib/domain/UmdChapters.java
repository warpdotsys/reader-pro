package me.ag2s.umdlib.domain;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.zip.DeflaterOutputStream;
import me.ag2s.umdlib.tool.UmdUtils;
import me.ag2s.umdlib.tool.WrapOutputStream;
import org.kxml2.wap.Wbxml;

/* JADX INFO: loaded from: reader-pro-classes-3.2.14.jar:me/ag2s/umdlib/domain/UmdChapters.class */
public class UmdChapters {
    private static final int DEFAULT_CHUNK_INIT_SIZE = 32768;
    private int TotalContentLen;
    private List<byte[]> titles = new ArrayList();
    public List<Integer> contentLengths = new ArrayList();
    public ByteArrayOutputStream contents = new ByteArrayOutputStream();

    public List<byte[]> getTitles() {
        return this.titles;
    }

    public void addTitle(String s) {
        this.titles.add(UmdUtils.stringToUnicodeBytes(s));
    }

    public void addTitle(byte[] s) {
        this.titles.add(s);
    }

    public void addContentLength(Integer integer) {
        this.contentLengths.add(integer);
    }

    public int getContentLength(int index) {
        return this.contentLengths.get(index).intValue();
    }

    public byte[] getContent(int index) {
        int st = this.contentLengths.get(index).intValue();
        byte[] b = this.contents.toByteArray();
        int end = index + 1 < this.contentLengths.size() ? this.contentLengths.get(index + 1).intValue() : getTotalContentLen();
        System.out.println("总长度:" + this.contents.size());
        System.out.println("起始值:" + st);
        System.out.println("结束值:" + end);
        byte[] bAr = new byte[end - st];
        System.arraycopy(b, st, bAr, 0, bAr.length);
        return bAr;
    }

    public String getContentString(int index) {
        return UmdUtils.unicodeBytesToString(getContent(index)).replace((char) 8233, '\n');
    }

    public String getTitle(int index) {
        return UmdUtils.unicodeBytesToString(this.titles.get(index));
    }

    public void buildChapters(WrapOutputStream wos) throws IOException {
        writeChaptersHead(wos);
        writeChaptersContentOffset(wos);
        writeChaptersTitles(wos);
        writeChaptersChunks(wos);
    }

    private void writeChaptersHead(WrapOutputStream wos) throws IOException {
        wos.writeBytes(35, 11, 0, 0, 9);
        wos.writeInt(this.contents.size());
    }

    private void writeChaptersContentOffset(WrapOutputStream wos) throws IOException {
        wos.writeBytes(35, Wbxml.STR_T, 0, 0, 9);
        byte[] rb = UmdUtils.genRandomBytes(4);
        wos.writeBytes(rb);
        wos.write(36);
        wos.writeBytes(rb);
        wos.writeInt((this.contentLengths.size() * 4) + 9);
        int offset = 0;
        for (Integer n : this.contentLengths) {
            wos.writeInt(offset);
            offset += n.intValue();
        }
    }

    private void writeChaptersTitles(WrapOutputStream wos) throws IOException {
        wos.writeBytes(35, Wbxml.LITERAL_A, 0, 1, 9);
        byte[] rb = UmdUtils.genRandomBytes(4);
        wos.writeBytes(rb);
        wos.write(36);
        wos.writeBytes(rb);
        int totalTitlesLen = 0;
        Iterator<byte[]> it = this.titles.iterator();
        while (it.hasNext()) {
            totalTitlesLen += it.next().length;
        }
        wos.writeInt(totalTitlesLen + this.titles.size() + 9);
        for (byte[] t : this.titles) {
            wos.writeByte(t.length);
            wos.write(t);
        }
    }

    private void writeChaptersChunks(WrapOutputStream wos) throws IOException {
        byte[] allContents = this.contents.toByteArray();
        byte[] zero16 = new byte[16];
        Arrays.fill(zero16, 0, zero16.length, (byte) 0);
        int startPos = 0;
        int chunkCnt = 0;
        ByteArrayOutputStream bos = new ByteArrayOutputStream(33024);
        List<byte[]> chunkRbList = new ArrayList<>();
        while (startPos < allContents.length) {
            int left = allContents.length - startPos;
            int len = DEFAULT_CHUNK_INIT_SIZE < left ? DEFAULT_CHUNK_INIT_SIZE : left;
            bos.reset();
            DeflaterOutputStream zos = new DeflaterOutputStream(bos);
            zos.write(allContents, startPos, len);
            zos.close();
            byte[] chunk = bos.toByteArray();
            byte[] rb = UmdUtils.genRandomBytes(4);
            wos.writeByte(36);
            wos.writeBytes(rb);
            chunkRbList.add(rb);
            wos.writeInt(chunk.length + 9);
            wos.write(chunk);
            wos.writeBytes(35, 241, 0, 0, 21);
            wos.write(zero16);
            startPos += len;
            chunkCnt++;
        }
        wos.writeBytes(35, Wbxml.EXT_T_1, 0, 1, 9);
        wos.writeBytes(0, 0, 0, 0);
        wos.write(36);
        wos.writeBytes(0, 0, 0, 0);
        wos.writeInt((chunkCnt * 4) + 9);
        for (int i = chunkCnt - 1; i >= 0; i--) {
            wos.writeBytes(chunkRbList.get(i));
        }
    }

    public void addChapter(String title, String content) {
        this.titles.add(UmdUtils.stringToUnicodeBytes(title));
        byte[] b = UmdUtils.stringToUnicodeBytes(content);
        this.contentLengths.add(Integer.valueOf(b.length));
        try {
            this.contents.write(b);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void addFile(File f, String title) throws IOException {
        byte[] temp = UmdUtils.readFile(f);
        String s = new String(temp);
        addChapter(title, s);
    }

    public void addFile(File f) throws IOException {
        String s = f.getName();
        int idx = s.lastIndexOf(46);
        if (idx >= 0) {
            s = s.substring(0, idx);
        }
        addFile(f, s);
    }

    public void clearChapters() {
        this.titles.clear();
        this.contentLengths.clear();
        this.contents.reset();
    }

    public int getTotalContentLen() {
        return this.TotalContentLen;
    }

    public void setTotalContentLen(int totalContentLen) {
        this.TotalContentLen = totalContentLen;
    }
}
