// 
// Decompiled by Procyon v0.6.0
// 

package me.ag2s.umdlib.domain;

import java.io.IOException;
import me.ag2s.umdlib.tool.UmdUtils;
import me.ag2s.umdlib.tool.WrapOutputStream;

public class UmdHeader
{
    private byte umdType;
    private String title;
    private String author;
    private String year;
    private String month;
    private String day;
    private String bookType;
    private String bookMan;
    private String shopKeeper;
    private static final byte B_type_umd = 1;
    private static final byte B_type_title = 2;
    private static final byte B_type_author = 3;
    private static final byte B_type_year = 4;
    private static final byte B_type_month = 5;
    private static final byte B_type_day = 6;
    private static final byte B_type_bookType = 7;
    private static final byte B_type_bookMan = 8;
    private static final byte B_type_shopKeeper = 9;
    
    public byte getUmdType() {
        return this.umdType;
    }
    
    public void setUmdType(final byte umdType) {
        this.umdType = umdType;
    }
    
    public void buildHeader(final WrapOutputStream wos) throws IOException {
        wos.writeBytes(137, 155, 154, 222);
        wos.writeByte(35);
        wos.writeBytes(1, 0, 0, 8);
        wos.writeByte(1);
        wos.writeBytes(UmdUtils.genRandomBytes(2));
        this.buildType(wos, (byte)2, this.getTitle());
        this.buildType(wos, (byte)3, this.getAuthor());
        this.buildType(wos, (byte)4, this.getYear());
        this.buildType(wos, (byte)5, this.getMonth());
        this.buildType(wos, (byte)6, this.getDay());
        this.buildType(wos, (byte)7, this.getBookType());
        this.buildType(wos, (byte)8, this.getBookMan());
        this.buildType(wos, (byte)9, this.getShopKeeper());
    }
    
    public void buildType(final WrapOutputStream wos, final byte type, final String content) throws IOException {
        if (content == null || content.length() == 0) {
            return;
        }
        wos.writeBytes(new int[] { 35, type, 0, 0 });
        final byte[] temp = UmdUtils.stringToUnicodeBytes(content);
        wos.writeByte(temp.length + 5);
        wos.write(temp);
    }
    
    public String getTitle() {
        return this.title;
    }
    
    public void setTitle(final String title) {
        this.title = title;
    }
    
    public String getAuthor() {
        return this.author;
    }
    
    public void setAuthor(final String author) {
        this.author = author;
    }
    
    public String getBookMan() {
        return this.bookMan;
    }
    
    public void setBookMan(final String bookMan) {
        this.bookMan = bookMan;
    }
    
    public String getShopKeeper() {
        return this.shopKeeper;
    }
    
    public void setShopKeeper(final String shopKeeper) {
        this.shopKeeper = shopKeeper;
    }
    
    public String getYear() {
        return this.year;
    }
    
    public void setYear(final String year) {
        this.year = year;
    }
    
    public String getMonth() {
        return this.month;
    }
    
    public void setMonth(final String month) {
        this.month = month;
    }
    
    public String getDay() {
        return this.day;
    }
    
    public void setDay(final String day) {
        this.day = day;
    }
    
    public String getBookType() {
        return this.bookType;
    }
    
    public void setBookType(final String bookType) {
        this.bookType = bookType;
    }
    
    @Override
    public String toString() {
        return "UmdHeader{umdType=" + this.umdType + ", title='" + this.title + '\'' + ", author='" + this.author + '\'' + ", year='" + this.year + '\'' + ", month='" + this.month + '\'' + ", day='" + this.day + '\'' + ", bookType='" + this.bookType + '\'' + ", bookMan='" + this.bookMan + '\'' + ", shopKeeper='" + this.shopKeeper + '\'' + '}';
    }
}
