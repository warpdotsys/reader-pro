package io.legado.app.model.analyzeRule;

import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import me.ag2s.epublib.epub.PackageDocumentBase;
import org.apache.commons.lang3.tuple.Pair;
import org.apache.commons.lang3.tuple.Triple;

/* JADX INFO: loaded from: reader-pro-classes-3.2.14.jar:io/legado/app/model/analyzeRule/QueryTTF.class */
public class QueryTTF {
    private final ByteArrayReader fontReader;
    private final Header fileHeader = new Header(null);
    private final List<Directory> directorys = new LinkedList();
    private final NameLayout name = new NameLayout(null);
    private final HeadLayout head = new HeadLayout(null);
    private final MaxpLayout maxp = new MaxpLayout(null);
    private final List<Integer> loca = new LinkedList();
    private final CmapLayout Cmap = new CmapLayout(null);
    private final List<GlyfLayout> glyf = new LinkedList();
    private final Pair<Integer, Integer>[] pps = {Pair.of(3, 10), Pair.of(0, 4), Pair.of(3, 1), Pair.of(1, 0), Pair.of(0, 3), Pair.of(0, 1)};
    public final Map<Integer, String> codeToGlyph = new HashMap();
    public final Map<String, Integer> glyphToCode = new HashMap();
    private int limitMix;
    private int limitMax;
    static final /* synthetic */ boolean $assertionsDisabled;

    static {
        $assertionsDisabled = !QueryTTF.class.desiredAssertionStatus();
    }

    /* JADX INFO: loaded from: reader-pro-classes-3.2.14.jar:io/legado/app/model/analyzeRule/QueryTTF$Header.class */
    private static class Header {
        public int majorVersion;
        public int minorVersion;
        public int numOfTables;
        public int searchRange;
        public int entrySelector;
        public int rangeShift;

        private Header() {
        }

        /* synthetic */ Header(AnonymousClass1 x0) {
            this();
        }
    }

    /* JADX INFO: loaded from: reader-pro-classes-3.2.14.jar:io/legado/app/model/analyzeRule/QueryTTF$Directory.class */
    private static class Directory {
        public String tag;
        public int checkSum;
        public int offset;
        public int length;

        private Directory() {
        }

        /* synthetic */ Directory(AnonymousClass1 x0) {
            this();
        }
    }

    /* JADX INFO: loaded from: reader-pro-classes-3.2.14.jar:io/legado/app/model/analyzeRule/QueryTTF$NameLayout.class */
    private static class NameLayout {
        public int format;
        public int count;
        public int stringOffset;
        public List<NameRecord> records;

        private NameLayout() {
            this.records = new LinkedList();
        }

        /* synthetic */ NameLayout(AnonymousClass1 x0) {
            this();
        }
    }

    /* JADX INFO: loaded from: reader-pro-classes-3.2.14.jar:io/legado/app/model/analyzeRule/QueryTTF$NameRecord.class */
    private static class NameRecord {
        public int platformID;
        public int encodingID;
        public int languageID;
        public int nameID;
        public int length;
        public int offset;

        private NameRecord() {
        }

        /* synthetic */ NameRecord(AnonymousClass1 x0) {
            this();
        }
    }

    /* JADX INFO: loaded from: reader-pro-classes-3.2.14.jar:io/legado/app/model/analyzeRule/QueryTTF$HeadLayout.class */
    private static class HeadLayout {
        public int majorVersion;
        public int minorVersion;
        public int fontRevision;
        public int checkSumAdjustment;
        public int magicNumber;
        public int flags;
        public int unitsPerEm;
        public long created;
        public long modified;
        public short xMin;
        public short yMin;
        public short xMax;
        public short yMax;
        public int macStyle;
        public int lowestRecPPEM;
        public short fontDirectionHint;
        public short indexToLocFormat;
        public short glyphDataFormat;

        private HeadLayout() {
        }

        /* synthetic */ HeadLayout(AnonymousClass1 x0) {
            this();
        }
    }

    /* JADX INFO: loaded from: reader-pro-classes-3.2.14.jar:io/legado/app/model/analyzeRule/QueryTTF$MaxpLayout.class */
    private static class MaxpLayout {
        public int majorVersion;
        public int minorVersion;
        public int numGlyphs;
        public int maxPoints;
        public int maxContours;
        public int maxCompositePoints;
        public int maxCompositeContours;
        public int maxZones;
        public int maxTwilightPoints;
        public int maxStorage;
        public int maxFunctionDefs;
        public int maxInstructionDefs;
        public int maxStackElements;
        public int maxSizeOfInstructions;
        public int maxComponentElements;
        public int maxComponentDepth;

        private MaxpLayout() {
        }

        /* synthetic */ MaxpLayout(AnonymousClass1 x0) {
            this();
        }
    }

    /* JADX INFO: loaded from: reader-pro-classes-3.2.14.jar:io/legado/app/model/analyzeRule/QueryTTF$CmapLayout.class */
    private static class CmapLayout {
        public int version;
        public int numTables;
        public List<CmapRecord> records;
        public Map<Integer, CmapFormat> tables;

        private CmapLayout() {
            this.records = new LinkedList();
            this.tables = new HashMap();
        }

        /* synthetic */ CmapLayout(AnonymousClass1 x0) {
            this();
        }
    }

    /* JADX INFO: loaded from: reader-pro-classes-3.2.14.jar:io/legado/app/model/analyzeRule/QueryTTF$CmapRecord.class */
    private static class CmapRecord {
        public int platformID;
        public int encodingID;
        public int offset;

        private CmapRecord() {
        }

        /* synthetic */ CmapRecord(AnonymousClass1 x0) {
            this();
        }
    }

    /* JADX INFO: loaded from: reader-pro-classes-3.2.14.jar:io/legado/app/model/analyzeRule/QueryTTF$CmapFormat.class */
    private static class CmapFormat {
        public int format;
        public int length;
        public int language;
        public byte[] glyphIdArray;

        private CmapFormat() {
        }

        /* synthetic */ CmapFormat(AnonymousClass1 x0) {
            this();
        }
    }

    /* JADX INFO: loaded from: reader-pro-classes-3.2.14.jar:io/legado/app/model/analyzeRule/QueryTTF$CmapFormat4.class */
    private static class CmapFormat4 extends CmapFormat {
        public int segCountX2;
        public int searchRange;
        public int entrySelector;
        public int rangeShift;
        public int[] endCode;
        public int reservedPad;
        public int[] startCode;
        public short[] idDelta;
        public int[] idRangeOffset;
        public int[] glyphIdArray;

        private CmapFormat4() {
            super(null);
        }

        /* synthetic */ CmapFormat4(AnonymousClass1 x0) {
            this();
        }
    }

    /* JADX INFO: loaded from: reader-pro-classes-3.2.14.jar:io/legado/app/model/analyzeRule/QueryTTF$CmapFormat6.class */
    private static class CmapFormat6 extends CmapFormat {
        public int firstCode;
        public int entryCount;
        public int[] glyphIdArray;

        private CmapFormat6() {
            super(null);
        }

        /* synthetic */ CmapFormat6(AnonymousClass1 x0) {
            this();
        }
    }

    /* JADX INFO: loaded from: reader-pro-classes-3.2.14.jar:io/legado/app/model/analyzeRule/QueryTTF$CmapFormat12.class */
    private static class CmapFormat12 extends CmapFormat {
        public int reserved;
        public int length;
        public int language;
        public int numGroups;
        public List<Triple<Integer, Integer, Integer>> groups;

        private CmapFormat12() {
            super(null);
        }

        /* synthetic */ CmapFormat12(AnonymousClass1 x0) {
            this();
        }
    }

    /* JADX INFO: loaded from: reader-pro-classes-3.2.14.jar:io/legado/app/model/analyzeRule/QueryTTF$GlyfLayout.class */
    private static class GlyfLayout {
        public short numberOfContours;
        public short xMin;
        public short yMin;
        public short xMax;
        public short yMax;
        public int[] endPtsOfContours;
        public int instructionLength;
        public byte[] instructions;
        public byte[] flags;
        public short[] xCoordinates;
        public short[] yCoordinates;

        private GlyfLayout() {
        }

        /* synthetic */ GlyfLayout(AnonymousClass1 x0) {
            this();
        }
    }

    /* JADX INFO: loaded from: reader-pro-classes-3.2.14.jar:io/legado/app/model/analyzeRule/QueryTTF$ByteArrayReader.class */
    private static class ByteArrayReader {
        public int index;
        public byte[] buffer;

        public ByteArrayReader(byte[] buffer, int index) {
            this.buffer = buffer;
            this.index = index;
        }

        public long ReadUIntX(long len) {
            long result = 0;
            long j = 0;
            while (true) {
                long i = j;
                if (i < len) {
                    byte[] bArr = this.buffer;
                    int i2 = this.index;
                    this.index = i2 + 1;
                    result = (result << 8) | ((long) (bArr[i2] & 255));
                    j = i + 1;
                } else {
                    return result;
                }
            }
        }

        public long ReadUInt64() {
            return ReadUIntX(8L);
        }

        public int ReadUInt32() {
            return (int) ReadUIntX(4L);
        }

        public int ReadUInt16() {
            return (int) ReadUIntX(2L);
        }

        public short ReadInt16() {
            return (short) ReadUIntX(2L);
        }

        public short ReadUInt8() {
            return (short) ReadUIntX(1L);
        }

        public String ReadStrings(int len, Charset charset) {
            byte[] result = len > 0 ? new byte[len] : null;
            for (int i = 0; i < len; i++) {
                byte[] bArr = this.buffer;
                int i2 = this.index;
                this.index = i2 + 1;
                result[i] = bArr[i2];
            }
            return new String(result, charset);
        }

        public byte GetByte() {
            byte[] bArr = this.buffer;
            int i = this.index;
            this.index = i + 1;
            return bArr[i];
        }

        public byte[] GetBytes(int len) {
            byte[] result = len > 0 ? new byte[len] : null;
            for (int i = 0; i < len; i++) {
                byte[] bArr = this.buffer;
                int i2 = this.index;
                this.index = i2 + 1;
                result[i] = bArr[i2];
            }
            return result;
        }

        public int[] GetUInt16Array(int len) {
            int[] result = len > 0 ? new int[len] : null;
            for (int i = 0; i < len; i++) {
                result[i] = ReadUInt16();
            }
            return result;
        }

        public short[] GetInt16Array(int len) {
            short[] result = len > 0 ? new short[len] : null;
            for (int i = 0; i < len; i++) {
                result[i] = ReadInt16();
            }
            return result;
        }
    }

    public QueryTTF(byte[] buffer) {
        this.limitMix = 0;
        this.limitMax = 0;
        this.fontReader = new ByteArrayReader(buffer, 0);
        this.fileHeader.majorVersion = this.fontReader.ReadUInt16();
        this.fileHeader.minorVersion = this.fontReader.ReadUInt16();
        this.fileHeader.numOfTables = this.fontReader.ReadUInt16();
        this.fileHeader.searchRange = this.fontReader.ReadUInt16();
        this.fileHeader.entrySelector = this.fontReader.ReadUInt16();
        this.fileHeader.rangeShift = this.fontReader.ReadUInt16();
        for (int i = 0; i < this.fileHeader.numOfTables; i++) {
            Directory d = new Directory(null);
            d.tag = this.fontReader.ReadStrings(4, StandardCharsets.US_ASCII);
            d.checkSum = this.fontReader.ReadUInt32();
            d.offset = this.fontReader.ReadUInt32();
            d.length = this.fontReader.ReadUInt32();
            this.directorys.add(d);
        }
        for (Directory Temp : this.directorys) {
            if (Temp.tag.equals("name")) {
                this.fontReader.index = Temp.offset;
                this.name.format = this.fontReader.ReadUInt16();
                this.name.count = this.fontReader.ReadUInt16();
                this.name.stringOffset = this.fontReader.ReadUInt16();
                for (int i2 = 0; i2 < this.name.count; i2++) {
                    NameRecord record = new NameRecord(null);
                    record.platformID = this.fontReader.ReadUInt16();
                    record.encodingID = this.fontReader.ReadUInt16();
                    record.languageID = this.fontReader.ReadUInt16();
                    record.nameID = this.fontReader.ReadUInt16();
                    record.length = this.fontReader.ReadUInt16();
                    record.offset = this.fontReader.ReadUInt16();
                    this.name.records.add(record);
                }
            }
        }
        for (Directory Temp2 : this.directorys) {
            if (Temp2.tag.equals("head")) {
                this.fontReader.index = Temp2.offset;
                this.head.majorVersion = this.fontReader.ReadUInt16();
                this.head.minorVersion = this.fontReader.ReadUInt16();
                this.head.fontRevision = this.fontReader.ReadUInt32();
                this.head.checkSumAdjustment = this.fontReader.ReadUInt32();
                this.head.magicNumber = this.fontReader.ReadUInt32();
                this.head.flags = this.fontReader.ReadUInt16();
                this.head.unitsPerEm = this.fontReader.ReadUInt16();
                this.head.created = this.fontReader.ReadUInt64();
                this.head.modified = this.fontReader.ReadUInt64();
                this.head.xMin = this.fontReader.ReadInt16();
                this.head.yMin = this.fontReader.ReadInt16();
                this.head.xMax = this.fontReader.ReadInt16();
                this.head.yMax = this.fontReader.ReadInt16();
                this.head.macStyle = this.fontReader.ReadUInt16();
                this.head.lowestRecPPEM = this.fontReader.ReadUInt16();
                this.head.fontDirectionHint = this.fontReader.ReadInt16();
                this.head.indexToLocFormat = this.fontReader.ReadInt16();
                this.head.glyphDataFormat = this.fontReader.ReadInt16();
            }
        }
        for (Directory Temp3 : this.directorys) {
            if (Temp3.tag.equals("maxp")) {
                this.fontReader.index = Temp3.offset;
                this.maxp.majorVersion = this.fontReader.ReadUInt16();
                this.maxp.minorVersion = this.fontReader.ReadUInt16();
                this.maxp.numGlyphs = this.fontReader.ReadUInt16();
                this.maxp.maxPoints = this.fontReader.ReadUInt16();
                this.maxp.maxContours = this.fontReader.ReadUInt16();
                this.maxp.maxCompositePoints = this.fontReader.ReadUInt16();
                this.maxp.maxCompositeContours = this.fontReader.ReadUInt16();
                this.maxp.maxZones = this.fontReader.ReadUInt16();
                this.maxp.maxTwilightPoints = this.fontReader.ReadUInt16();
                this.maxp.maxStorage = this.fontReader.ReadUInt16();
                this.maxp.maxFunctionDefs = this.fontReader.ReadUInt16();
                this.maxp.maxInstructionDefs = this.fontReader.ReadUInt16();
                this.maxp.maxStackElements = this.fontReader.ReadUInt16();
                this.maxp.maxSizeOfInstructions = this.fontReader.ReadUInt16();
                this.maxp.maxComponentElements = this.fontReader.ReadUInt16();
                this.maxp.maxComponentDepth = this.fontReader.ReadUInt16();
            }
        }
        for (Directory Temp4 : this.directorys) {
            if (Temp4.tag.equals("loca")) {
                this.fontReader.index = Temp4.offset;
                int offset = this.head.indexToLocFormat == 0 ? 2 : 4;
                long j = 0;
                while (true) {
                    long i3 = j;
                    if (i3 < Temp4.length) {
                        this.loca.add(Integer.valueOf(offset == 2 ? this.fontReader.ReadUInt16() << 1 : this.fontReader.ReadUInt32()));
                        j = i3 + ((long) offset);
                    }
                }
            }
        }
        for (Directory Temp5 : this.directorys) {
            if (Temp5.tag.equals("cmap")) {
                this.fontReader.index = Temp5.offset;
                this.Cmap.version = this.fontReader.ReadUInt16();
                this.Cmap.numTables = this.fontReader.ReadUInt16();
                for (int i4 = 0; i4 < this.Cmap.numTables; i4++) {
                    CmapRecord record2 = new CmapRecord(null);
                    record2.platformID = this.fontReader.ReadUInt16();
                    record2.encodingID = this.fontReader.ReadUInt16();
                    record2.offset = this.fontReader.ReadUInt32();
                    this.Cmap.records.add(record2);
                }
                for (int i5 = 0; i5 < this.Cmap.numTables; i5++) {
                    int fmtOffset = this.Cmap.records.get(i5).offset;
                    this.fontReader.index = Temp5.offset + fmtOffset;
                    int EndIndex = this.fontReader.index;
                    int format = this.fontReader.ReadUInt16();
                    if (!this.Cmap.tables.containsKey(Integer.valueOf(fmtOffset))) {
                        if (format == 0) {
                            CmapFormat f = new CmapFormat(null);
                            f.format = format;
                            f.length = this.fontReader.ReadUInt16();
                            f.language = this.fontReader.ReadUInt16();
                            f.glyphIdArray = this.fontReader.GetBytes(f.length - 6);
                            this.Cmap.tables.put(Integer.valueOf(fmtOffset), f);
                        } else if (format == 4) {
                            CmapFormat4 f2 = new CmapFormat4(null);
                            f2.format = format;
                            f2.length = this.fontReader.ReadUInt16();
                            f2.language = this.fontReader.ReadUInt16();
                            f2.segCountX2 = this.fontReader.ReadUInt16();
                            int segCount = f2.segCountX2 >> 1;
                            f2.searchRange = this.fontReader.ReadUInt16();
                            f2.entrySelector = this.fontReader.ReadUInt16();
                            f2.rangeShift = this.fontReader.ReadUInt16();
                            f2.endCode = this.fontReader.GetUInt16Array(segCount);
                            f2.reservedPad = this.fontReader.ReadUInt16();
                            f2.startCode = this.fontReader.GetUInt16Array(segCount);
                            f2.idDelta = this.fontReader.GetInt16Array(segCount);
                            f2.idRangeOffset = this.fontReader.GetUInt16Array(segCount);
                            f2.glyphIdArray = this.fontReader.GetUInt16Array(((EndIndex + f2.length) - this.fontReader.index) >> 1);
                            this.Cmap.tables.put(Integer.valueOf(fmtOffset), f2);
                        } else if (format == 6) {
                            CmapFormat6 f3 = new CmapFormat6(null);
                            f3.format = format;
                            f3.length = this.fontReader.ReadUInt16();
                            f3.language = this.fontReader.ReadUInt16();
                            f3.firstCode = this.fontReader.ReadUInt16();
                            f3.entryCount = this.fontReader.ReadUInt16();
                            f3.glyphIdArray = this.fontReader.GetUInt16Array(f3.entryCount);
                            this.Cmap.tables.put(Integer.valueOf(fmtOffset), f3);
                        } else if (format == 12) {
                            CmapFormat12 f4 = new CmapFormat12(null);
                            f4.format = format;
                            f4.reserved = this.fontReader.ReadUInt16();
                            f4.length = this.fontReader.ReadUInt32();
                            f4.language = this.fontReader.ReadUInt32();
                            f4.numGroups = this.fontReader.ReadUInt32();
                            f4.groups = new ArrayList(f4.numGroups);
                            for (int n = 0; n < f4.numGroups; n++) {
                                f4.groups.add(Triple.of(Integer.valueOf(this.fontReader.ReadUInt32()), Integer.valueOf(this.fontReader.ReadUInt32()), Integer.valueOf(this.fontReader.ReadUInt32())));
                            }
                            this.Cmap.tables.put(Integer.valueOf(fmtOffset), f4);
                        }
                    }
                }
            }
        }
        for (Directory Temp6 : this.directorys) {
            if (Temp6.tag.equals("glyf")) {
                this.fontReader.index = Temp6.offset;
                for (int i6 = 0; i6 < this.maxp.numGlyphs; i6++) {
                    this.fontReader.index = Temp6.offset + this.loca.get(i6).intValue();
                    short numberOfContours = this.fontReader.ReadInt16();
                    if (numberOfContours > 0) {
                        GlyfLayout g = new GlyfLayout(null);
                        g.numberOfContours = numberOfContours;
                        g.xMin = this.fontReader.ReadInt16();
                        g.yMin = this.fontReader.ReadInt16();
                        g.xMax = this.fontReader.ReadInt16();
                        g.yMax = this.fontReader.ReadInt16();
                        g.endPtsOfContours = this.fontReader.GetUInt16Array(numberOfContours);
                        g.instructionLength = this.fontReader.ReadUInt16();
                        g.instructions = this.fontReader.GetBytes(g.instructionLength);
                        int flagLength = g.endPtsOfContours[g.endPtsOfContours.length - 1] + 1;
                        g.flags = new byte[flagLength];
                        int n2 = 0;
                        while (n2 < flagLength) {
                            g.flags[n2] = this.fontReader.GetByte();
                            if ((g.flags[n2] & 8) != 0) {
                                for (int m = this.fontReader.ReadUInt8(); m > 0; m--) {
                                    n2++;
                                    g.flags[n2] = g.flags[n2 - 1];
                                }
                            }
                            n2++;
                        }
                        g.xCoordinates = new short[flagLength];
                        for (int n3 = 0; n3 < flagLength; n3++) {
                            short same = (short) ((g.flags[n3] & 16) != 0 ? 1 : -1);
                            if ((g.flags[n3] & 2) != 0) {
                                g.xCoordinates[n3] = (short) (same * this.fontReader.ReadUInt8());
                            } else {
                                g.xCoordinates[n3] = same == 1 ? (short) 0 : this.fontReader.ReadInt16();
                            }
                        }
                        g.yCoordinates = new short[flagLength];
                        for (int n4 = 0; n4 < flagLength; n4++) {
                            short same2 = (short) ((g.flags[n4] & 32) != 0 ? 1 : -1);
                            if ((g.flags[n4] & 4) != 0) {
                                g.yCoordinates[n4] = (short) (same2 * this.fontReader.ReadUInt8());
                            } else {
                                g.yCoordinates[n4] = same2 == 1 ? (short) 0 : this.fontReader.ReadInt16();
                            }
                        }
                        this.glyf.add(g);
                    }
                }
            }
        }
        int key = 0;
        while (key < 130000) {
            key = key == 255 ? 13312 : key;
            int gid = getGlyfIndex(key);
            if (gid != 0) {
                StringBuilder sb = new StringBuilder();
                for (short b : this.glyf.get(gid).xCoordinates) {
                    sb.append((int) b);
                }
                for (short b2 : this.glyf.get(gid).yCoordinates) {
                    sb.append((int) b2);
                }
                String val = sb.toString();
                if (this.limitMix == 0) {
                    this.limitMix = key;
                }
                this.limitMax = key;
                this.codeToGlyph.put(Integer.valueOf(key), val);
                if (!this.glyphToCode.containsKey(val)) {
                    this.glyphToCode.put(val, Integer.valueOf(key));
                }
            }
            key++;
        }
    }

    public String getNameById(int nameId) {
        Iterator<Directory> it = this.directorys.iterator();
        while (true) {
            if (!it.hasNext()) {
                break;
            }
            Directory Temp = it.next();
            if (Temp.tag.equals("name")) {
                this.fontReader.index = Temp.offset;
                break;
            }
        }
        for (NameRecord record : this.name.records) {
            if (record.nameID == nameId) {
                this.fontReader.index += this.name.stringOffset + record.offset;
                return this.fontReader.ReadStrings(record.length, record.platformID == 1 ? StandardCharsets.UTF_8 : StandardCharsets.UTF_16BE);
            }
        }
        return "error";
    }

    private int getGlyfIndex(int code) {
        int glyfID;
        if (code == 0) {
            return 0;
        }
        int fmtKey = 0;
        for (Pair<Integer, Integer> item : this.pps) {
            Iterator<CmapRecord> it = this.Cmap.records.iterator();
            while (true) {
                if (!it.hasNext()) {
                    break;
                }
                CmapRecord record = it.next();
                if (((Integer) item.getLeft()).intValue() == record.platformID && ((Integer) item.getRight()).intValue() == record.encodingID) {
                    fmtKey = record.offset;
                    break;
                }
            }
            if (fmtKey > 0) {
                break;
            }
        }
        if (fmtKey == 0) {
            return 0;
        }
        int glyfID2 = 0;
        CmapFormat table = this.Cmap.tables.get(Integer.valueOf(fmtKey));
        if (!$assertionsDisabled && table == null) {
            throw new AssertionError();
        }
        int fmt = table.format;
        if (fmt == 0) {
            if (code < table.glyphIdArray.length) {
                glyfID2 = table.glyphIdArray[code] & 255;
            }
        } else if (fmt == 4) {
            CmapFormat4 tab = (CmapFormat4) table;
            if (code > tab.endCode[tab.endCode.length - 1]) {
                return 0;
            }
            int start = 0;
            int end = tab.endCode.length - 1;
            while (start + 1 < end) {
                int middle = (start + end) / 2;
                if (tab.endCode[middle] <= code) {
                    start = middle;
                } else {
                    end = middle;
                }
            }
            if (tab.endCode[start] < code) {
                start++;
            }
            if (code < tab.startCode[start]) {
                return 0;
            }
            if (tab.idRangeOffset[start] != 0) {
                glyfID = tab.glyphIdArray[((code - tab.startCode[start]) + (tab.idRangeOffset[start] >> 1)) - (tab.idRangeOffset.length - start)];
            } else {
                glyfID = code + tab.idDelta[start];
            }
            glyfID2 = glyfID & 65535;
        } else if (fmt == 6) {
            CmapFormat6 tab2 = (CmapFormat6) table;
            int index = code - tab2.firstCode;
            glyfID2 = (index < 0 || index >= tab2.glyphIdArray.length) ? 0 : tab2.glyphIdArray[index];
        } else if (fmt == 12) {
            CmapFormat12 tab3 = (CmapFormat12) table;
            if (code > ((Integer) tab3.groups.get(tab3.numGroups - 1).getMiddle()).intValue()) {
                return 0;
            }
            int start2 = 0;
            int end2 = tab3.numGroups - 1;
            while (start2 + 1 < end2) {
                int middle2 = (start2 + end2) / 2;
                if (((Integer) tab3.groups.get(middle2).getLeft()).intValue() <= code) {
                    start2 = middle2;
                } else {
                    end2 = middle2;
                }
            }
            if (((Integer) tab3.groups.get(start2).getLeft()).intValue() <= code && code <= ((Integer) tab3.groups.get(start2).getMiddle()).intValue()) {
                glyfID2 = (((Integer) tab3.groups.get(start2).getRight()).intValue() + code) - ((Integer) tab3.groups.get(start2).getLeft()).intValue();
            }
        }
        return glyfID2;
    }

    public boolean inLimit(char code) {
        return this.limitMix <= code && code < this.limitMax;
    }

    public String getGlyfByCode(int key) {
        return this.codeToGlyph.getOrDefault(Integer.valueOf(key), PackageDocumentBase.PREFIX_OPF);
    }

    public int getCodeByGlyf(String val) {
        return this.glyphToCode.getOrDefault(val, 0).intValue();
    }
}
