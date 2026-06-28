package me.ag2s.umdlib.umd;

import java.io.IOException;
import java.io.InputStream;
import me.ag2s.umdlib.domain.UmdBook;
import me.ag2s.umdlib.domain.UmdCover;
import me.ag2s.umdlib.domain.UmdHeader;
import me.ag2s.umdlib.tool.StreamReader;
import me.ag2s.umdlib.tool.UmdUtils;
import org.kxml2.kdom.Node;
import org.kxml2.wap.Wbxml;

/* JADX INFO: loaded from: reader-pro-classes-3.2.14.jar:me/ag2s/umdlib/umd/UmdReader.class */
public class UmdReader {
    UmdBook book;
    InputStream inputStream;
    int _AdditionalCheckNumber;
    int _TotalContentLen;
    boolean end = false;

    public synchronized UmdBook read(InputStream inputStream) throws Exception {
        this.book = new UmdBook();
        this.inputStream = inputStream;
        StreamReader reader = new StreamReader(inputStream);
        UmdHeader umdHeader = new UmdHeader();
        this.book.setHeader(umdHeader);
        if (reader.readIntLe() != -560292983) {
            throw new IOException("Wrong header");
        }
        short num1 = -1;
        byte ch = reader.readByte();
        while (ch == 35) {
            short segType = reader.readShortLe();
            byte segFlag = reader.readByte();
            short len = (short) (reader.readUint8() - 5);
            System.out.println("块标识:" + ((int) segType));
            ReadSection(segType, segFlag, len, reader, umdHeader);
            if (segType == 241 || segType == 10) {
                segType = num1;
            }
            byte b = reader.readByte();
            while (true) {
                ch = b;
                if (ch == 36) {
                    System.out.println((int) ch);
                    int additionalCheckNumber = reader.readIntLe();
                    int length2 = reader.readIntLe() - 9;
                    ReadAdditionalSection(segType, additionalCheckNumber, length2, reader);
                    b = reader.readByte();
                }
            }
            num1 = segType;
        }
        System.out.println(this.book.getHeader().toString());
        return this.book;
    }

    private void ReadAdditionalSection(short segType, int additionalCheckNumber, int length, StreamReader reader) throws Exception {
        switch (segType) {
            case Wbxml.EXT_T_1 /* 129 */:
                reader.readBytes(length);
                break;
            case Wbxml.EXT_T_2 /* 130 */:
                this.book.setCover(new UmdCover(reader.readBytes(length)));
                break;
            case Wbxml.STR_T /* 131 */:
                System.out.println(length / 4);
                this.book.setNum(length / 4);
                for (int i = 0; i < length / 4; i++) {
                    this.book.getChapters().addContentLength(Integer.valueOf(reader.readIntLe()));
                }
                break;
            case Wbxml.LITERAL_A /* 132 */:
                System.out.println(this._AdditionalCheckNumber);
                System.out.println(additionalCheckNumber);
                if (this._AdditionalCheckNumber != additionalCheckNumber) {
                    System.out.println(length);
                    this.book.getChapters().contents.write(UmdUtils.decompress(reader.readBytes(length)));
                    this.book.getChapters().contents.flush();
                } else {
                    for (int i2 = 0; i2 < this.book.getNum(); i2++) {
                        short len = reader.readUint8();
                        byte[] title = reader.readBytes(len);
                        this.book.getChapters().addTitle(title);
                    }
                }
                break;
        }
    }

    public void ReadSection(short segType, byte segFlag, short length, StreamReader reader, UmdHeader header) throws IOException {
        switch (segType) {
            case 1:
                header.setUmdType(reader.readByte());
                reader.readBytes(2);
                System.out.println("UMD文件类型:" + ((int) header.getUmdType()));
                break;
            case 2:
                header.setTitle(UmdUtils.unicodeBytesToString(reader.readBytes(length)));
                System.out.println("文件标题:" + header.getTitle());
                break;
            case 3:
                header.setAuthor(UmdUtils.unicodeBytesToString(reader.readBytes(length)));
                System.out.println("作者:" + header.getAuthor());
                break;
            case 4:
                header.setYear(UmdUtils.unicodeBytesToString(reader.readBytes(length)));
                System.out.println("年:" + header.getYear());
                break;
            case 5:
                header.setMonth(UmdUtils.unicodeBytesToString(reader.readBytes(length)));
                System.out.println("月:" + header.getMonth());
                break;
            case 6:
                header.setDay(UmdUtils.unicodeBytesToString(reader.readBytes(length)));
                System.out.println("日:" + header.getDay());
                break;
            case 7:
                header.setBookType(UmdUtils.unicodeBytesToString(reader.readBytes(length)));
                System.out.println("小说类型:" + header.getBookType());
                break;
            case 8:
                header.setBookMan(UmdUtils.unicodeBytesToString(reader.readBytes(length)));
                System.out.println("出版商:" + header.getBookMan());
                break;
            case Node.COMMENT /* 9 */:
                header.setShopKeeper(UmdUtils.unicodeBytesToString(reader.readBytes(length)));
                System.out.println("零售商:" + header.getShopKeeper());
                break;
            case Node.DOCDECL /* 10 */:
                System.out.println("CONTENT ID:" + reader.readHex(length));
                break;
            case 11:
                this._TotalContentLen = reader.readIntLe();
                this.book.getChapters().setTotalContentLen(this._TotalContentLen);
                System.out.println("内容长度:" + this._TotalContentLen);
                break;
            case 12:
                this.end = true;
                int num2 = reader.readIntLe();
                System.out.println("整个文件长度" + num2);
                break;
            case 13:
            case 240:
                break;
            case 14:
                reader.readByte();
                break;
            case 15:
                reader.readBytes(length);
                break;
            case Wbxml.EXT_T_1 /* 129 */:
            case Wbxml.STR_T /* 131 */:
                this._AdditionalCheckNumber = reader.readIntLe();
                System.out.println("章节偏移:" + this._AdditionalCheckNumber);
                break;
            case Wbxml.EXT_T_2 /* 130 */:
                reader.readByte();
                this._AdditionalCheckNumber = reader.readIntLe();
                break;
            case Wbxml.LITERAL_A /* 132 */:
                this._AdditionalCheckNumber = reader.readIntLe();
                System.out.println("章节标题，正文:" + this._AdditionalCheckNumber);
                break;
            case 135:
                reader.readUint8();
                reader.readUint8();
                reader.readBytes(4);
                break;
            case 241:
                System.out.println("许可证(LICENCE KEY):" + reader.readHex(16));
                break;
            default:
                if (length > 0) {
                    reader.readBytes(length);
                }
                break;
        }
    }

    public String toString() {
        return "UmdReader{book=" + this.book + '}';
    }
}
