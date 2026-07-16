// 
// Decompiled by Procyon v0.6.0
// 

package me.ag2s.umdlib.domain;

import java.io.IOException;
import me.ag2s.umdlib.tool.WrapOutputStream;
import java.io.OutputStream;

public class UmdBook
{
    private int num;
    private UmdHeader header;
    private UmdChapters chapters;
    private UmdCover cover;
    private UmdEnd end;
    
    public UmdBook() {
        this.header = new UmdHeader();
        this.chapters = new UmdChapters();
        this.cover = new UmdCover();
        this.end = new UmdEnd();
    }
    
    public int getNum() {
        return this.num;
    }
    
    public void setNum(final int num) {
        this.num = num;
    }
    
    public void buildUmd(final OutputStream os) throws IOException {
        final WrapOutputStream wos = new WrapOutputStream(os);
        this.header.buildHeader(wos);
        this.chapters.buildChapters(wos);
        this.cover.buildCover(wos);
        this.end.buildEnd(wos);
    }
    
    public UmdHeader getHeader() {
        return this.header;
    }
    
    public void setHeader(final UmdHeader header) {
        this.header = header;
    }
    
    public UmdChapters getChapters() {
        return this.chapters;
    }
    
    public void setChapters(final UmdChapters chapters) {
        this.chapters = chapters;
    }
    
    public UmdCover getCover() {
        return this.cover;
    }
    
    public void setCover(final UmdCover cover) {
        this.cover = cover;
    }
    
    public UmdEnd getEnd() {
        return this.end;
    }
    
    public void setEnd(final UmdEnd end) {
        this.end = end;
    }
}
