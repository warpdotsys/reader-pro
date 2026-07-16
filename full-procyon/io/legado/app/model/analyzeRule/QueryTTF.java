// 
// Decompiled by Procyon v0.6.0
// 

package io.legado.app.model.analyzeRule;

import java.nio.charset.Charset;
import java.util.Iterator;
import org.apache.commons.lang3.tuple.Triple;
import java.util.ArrayList;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;
import org.apache.commons.lang3.tuple.Pair;
import java.util.List;

public class QueryTTF
{
    private final ByteArrayReader fontReader;
    private final Header fileHeader;
    private final List<Directory> directorys;
    private final NameLayout name;
    private final HeadLayout head;
    private final MaxpLayout maxp;
    private final List<Integer> loca;
    private final CmapLayout Cmap;
    private final List<GlyfLayout> glyf;
    private final Pair<Integer, Integer>[] pps;
    public final Map<Integer, String> codeToGlyph;
    public final Map<String, Integer> glyphToCode;
    private int limitMix;
    private int limitMax;
    
    public QueryTTF(final byte[] buffer) {
        this.fileHeader = new Header();
        this.directorys = new LinkedList<Directory>();
        this.name = new NameLayout();
        this.head = new HeadLayout();
        this.maxp = new MaxpLayout();
        this.loca = new LinkedList<Integer>();
        this.Cmap = new CmapLayout();
        this.glyf = new LinkedList<GlyfLayout>();
        this.pps = (Pair<Integer, Integer>[])new Pair[] { Pair.of((Object)3, (Object)10), Pair.of((Object)0, (Object)4), Pair.of((Object)3, (Object)1), Pair.of((Object)1, (Object)0), Pair.of((Object)0, (Object)3), Pair.of((Object)0, (Object)1) };
        this.codeToGlyph = new HashMap<Integer, String>();
        this.glyphToCode = new HashMap<String, Integer>();
        this.limitMix = 0;
        this.limitMax = 0;
        this.fontReader = new ByteArrayReader(buffer, 0);
        this.fileHeader.majorVersion = this.fontReader.ReadUInt16();
        this.fileHeader.minorVersion = this.fontReader.ReadUInt16();
        this.fileHeader.numOfTables = this.fontReader.ReadUInt16();
        this.fileHeader.searchRange = this.fontReader.ReadUInt16();
        this.fileHeader.entrySelector = this.fontReader.ReadUInt16();
        this.fileHeader.rangeShift = this.fontReader.ReadUInt16();
        for (int i = 0; i < this.fileHeader.numOfTables; ++i) {
            final Directory d = new Directory();
            d.tag = this.fontReader.ReadStrings(4, StandardCharsets.US_ASCII);
            d.checkSum = this.fontReader.ReadUInt32();
            d.offset = this.fontReader.ReadUInt32();
            d.length = this.fontReader.ReadUInt32();
            this.directorys.add(d);
        }
        for (final Directory Temp : this.directorys) {
            if (Temp.tag.equals("name")) {
                this.fontReader.index = Temp.offset;
                this.name.format = this.fontReader.ReadUInt16();
                this.name.count = this.fontReader.ReadUInt16();
                this.name.stringOffset = this.fontReader.ReadUInt16();
                for (int j = 0; j < this.name.count; ++j) {
                    final NameRecord record = new NameRecord();
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
        for (final Directory Temp : this.directorys) {
            if (Temp.tag.equals("head")) {
                this.fontReader.index = Temp.offset;
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
        for (final Directory Temp : this.directorys) {
            if (Temp.tag.equals("maxp")) {
                this.fontReader.index = Temp.offset;
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
        for (final Directory Temp : this.directorys) {
            if (Temp.tag.equals("loca")) {
                this.fontReader.index = Temp.offset;
                final int offset = (this.head.indexToLocFormat == 0) ? 2 : 4;
                for (long k = 0L; k < Temp.length; k += offset) {
                    this.loca.add((offset == 2) ? (this.fontReader.ReadUInt16() << 1) : this.fontReader.ReadUInt32());
                }
            }
        }
        for (final Directory Temp : this.directorys) {
            if (Temp.tag.equals("cmap")) {
                this.fontReader.index = Temp.offset;
                this.Cmap.version = this.fontReader.ReadUInt16();
                this.Cmap.numTables = this.fontReader.ReadUInt16();
                for (int j = 0; j < this.Cmap.numTables; ++j) {
                    final CmapRecord record2 = new CmapRecord();
                    record2.platformID = this.fontReader.ReadUInt16();
                    record2.encodingID = this.fontReader.ReadUInt16();
                    record2.offset = this.fontReader.ReadUInt32();
                    this.Cmap.records.add(record2);
                }
                for (int j = 0; j < this.Cmap.numTables; ++j) {
                    final int fmtOffset = this.Cmap.records.get(j).offset;
                    this.fontReader.index = Temp.offset + fmtOffset;
                    final int EndIndex = this.fontReader.index;
                    final int format = this.fontReader.ReadUInt16();
                    if (!this.Cmap.tables.containsKey(fmtOffset)) {
                        if (format == 0) {
                            final CmapFormat f = new CmapFormat();
                            f.format = format;
                            f.length = this.fontReader.ReadUInt16();
                            f.language = this.fontReader.ReadUInt16();
                            f.glyphIdArray = this.fontReader.GetBytes(f.length - 6);
                            this.Cmap.tables.put(fmtOffset, f);
                        }
                        else if (format == 4) {
                            final CmapFormat4 f2 = new CmapFormat4();
                            f2.format = format;
                            f2.length = this.fontReader.ReadUInt16();
                            f2.language = this.fontReader.ReadUInt16();
                            f2.segCountX2 = this.fontReader.ReadUInt16();
                            final int segCount = f2.segCountX2 >> 1;
                            f2.searchRange = this.fontReader.ReadUInt16();
                            f2.entrySelector = this.fontReader.ReadUInt16();
                            f2.rangeShift = this.fontReader.ReadUInt16();
                            f2.endCode = this.fontReader.GetUInt16Array(segCount);
                            f2.reservedPad = this.fontReader.ReadUInt16();
                            f2.startCode = this.fontReader.GetUInt16Array(segCount);
                            f2.idDelta = this.fontReader.GetInt16Array(segCount);
                            f2.idRangeOffset = this.fontReader.GetUInt16Array(segCount);
                            f2.glyphIdArray = this.fontReader.GetUInt16Array(EndIndex + f2.length - this.fontReader.index >> 1);
                            this.Cmap.tables.put(fmtOffset, f2);
                        }
                        else if (format == 6) {
                            final CmapFormat6 f3 = new CmapFormat6();
                            f3.format = format;
                            f3.length = this.fontReader.ReadUInt16();
                            f3.language = this.fontReader.ReadUInt16();
                            f3.firstCode = this.fontReader.ReadUInt16();
                            f3.entryCount = this.fontReader.ReadUInt16();
                            f3.glyphIdArray = this.fontReader.GetUInt16Array(f3.entryCount);
                            this.Cmap.tables.put(fmtOffset, f3);
                        }
                        else if (format == 12) {
                            final CmapFormat12 f4 = new CmapFormat12();
                            f4.format = format;
                            f4.reserved = this.fontReader.ReadUInt16();
                            f4.length = this.fontReader.ReadUInt32();
                            f4.language = this.fontReader.ReadUInt32();
                            f4.numGroups = this.fontReader.ReadUInt32();
                            f4.groups = new ArrayList<Triple<Integer, Integer, Integer>>(f4.numGroups);
                            for (int n = 0; n < f4.numGroups; ++n) {
                                f4.groups.add((Triple<Integer, Integer, Integer>)Triple.of((Object)this.fontReader.ReadUInt32(), (Object)this.fontReader.ReadUInt32(), (Object)this.fontReader.ReadUInt32()));
                            }
                            this.Cmap.tables.put(fmtOffset, f4);
                        }
                    }
                }
            }
        }
        for (final Directory Temp : this.directorys) {
            if (Temp.tag.equals("glyf")) {
                this.fontReader.index = Temp.offset;
                for (int j = 0; j < this.maxp.numGlyphs; ++j) {
                    this.fontReader.index = Temp.offset + this.loca.get(j);
                    final short numberOfContours = this.fontReader.ReadInt16();
                    if (numberOfContours > 0) {
                        final GlyfLayout g = new GlyfLayout();
                        g.numberOfContours = numberOfContours;
                        g.xMin = this.fontReader.ReadInt16();
                        g.yMin = this.fontReader.ReadInt16();
                        g.xMax = this.fontReader.ReadInt16();
                        g.yMax = this.fontReader.ReadInt16();
                        g.endPtsOfContours = this.fontReader.GetUInt16Array(numberOfContours);
                        g.instructionLength = this.fontReader.ReadUInt16();
                        g.instructions = this.fontReader.GetBytes(g.instructionLength);
                        final int flagLength = g.endPtsOfContours[g.endPtsOfContours.length - 1] + 1;
                        g.flags = new byte[flagLength];
                        for (int n2 = 0; n2 < flagLength; ++n2) {
                            g.flags[n2] = this.fontReader.GetByte();
                            if ((g.flags[n2] & 0x8) != 0x0) {
                                for (int m = this.fontReader.ReadUInt8(); m > 0; --m) {
                                    g.flags[++n2] = g.flags[n2 - 1];
                                }
                            }
                        }
                        g.xCoordinates = new short[flagLength];
                        for (int n2 = 0; n2 < flagLength; ++n2) {
                            final short same = (short)(((g.flags[n2] & 0x10) != 0x0) ? 1 : -1);
                            if ((g.flags[n2] & 0x2) != 0x0) {
                                g.xCoordinates[n2] = (short)(same * this.fontReader.ReadUInt8());
                            }
                            else {
                                g.xCoordinates[n2] = (short)((same == 1) ? 0 : this.fontReader.ReadInt16());
                            }
                        }
                        g.yCoordinates = new short[flagLength];
                        for (int n2 = 0; n2 < flagLength; ++n2) {
                            final short same = (short)(((g.flags[n2] & 0x20) != 0x0) ? 1 : -1);
                            if ((g.flags[n2] & 0x4) != 0x0) {
                                g.yCoordinates[n2] = (short)(same * this.fontReader.ReadUInt8());
                            }
                            else {
                                g.yCoordinates[n2] = (short)((same == 1) ? 0 : this.fontReader.ReadInt16());
                            }
                        }
                        this.glyf.add(g);
                    }
                }
            }
        }
        for (int key = 0; key < 130000; ++key) {
            if (key == 255) {
                key = 13312;
            }
            final int gid = this.getGlyfIndex(key);
            if (gid != 0) {
                final StringBuilder sb = new StringBuilder();
                for (final short b : this.glyf.get(gid).xCoordinates) {
                    sb.append(b);
                }
                for (final short b : this.glyf.get(gid).yCoordinates) {
                    sb.append(b);
                }
                final String val = sb.toString();
                if (this.limitMix == 0) {
                    this.limitMix = key;
                }
                this.limitMax = key;
                this.codeToGlyph.put(key, val);
                if (!this.glyphToCode.containsKey(val)) {
                    this.glyphToCode.put(val, key);
                }
            }
        }
    }
    
    public String getNameById(final int nameId) {
        for (final Directory Temp : this.directorys) {
            if (!Temp.tag.equals("name")) {
                continue;
            }
            this.fontReader.index = Temp.offset;
            break;
        }
        for (final NameRecord record : this.name.records) {
            if (record.nameID != nameId) {
                continue;
            }
            final ByteArrayReader fontReader = this.fontReader;
            fontReader.index += this.name.stringOffset + record.offset;
            return this.fontReader.ReadStrings(record.length, (record.platformID == 1) ? StandardCharsets.UTF_8 : StandardCharsets.UTF_16BE);
        }
        return "error";
    }
    
    private int getGlyfIndex(final int code) {
        if (code == 0) {
            return 0;
        }
        int fmtKey = 0;
        for (final Pair<Integer, Integer> item : this.pps) {
            for (final CmapRecord record : this.Cmap.records) {
                if ((int)item.getLeft() == record.platformID && (int)item.getRight() == record.encodingID) {
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
        int glyfID = 0;
        final CmapFormat table = this.Cmap.tables.get(fmtKey);
        assert table != null;
        final int fmt = table.format;
        if (fmt == 0) {
            if (code < table.glyphIdArray.length) {
                glyfID = (table.glyphIdArray[code] & 0xFF);
            }
        }
        else if (fmt == 4) {
            final CmapFormat4 tab = (CmapFormat4)table;
            if (code > tab.endCode[tab.endCode.length - 1]) {
                return 0;
            }
            int start = 0;
            int end = tab.endCode.length - 1;
            while (start + 1 < end) {
                final int middle = (start + end) / 2;
                if (tab.endCode[middle] <= code) {
                    start = middle;
                }
                else {
                    end = middle;
                }
            }
            if (tab.endCode[start] < code) {
                ++start;
            }
            if (code < tab.startCode[start]) {
                return 0;
            }
            if (tab.idRangeOffset[start] != 0) {
                glyfID = tab.glyphIdArray[code - tab.startCode[start] + (tab.idRangeOffset[start] >> 1) - (tab.idRangeOffset.length - start)];
            }
            else {
                glyfID = code + tab.idDelta[start];
            }
            glyfID &= 0xFFFF;
        }
        else if (fmt == 6) {
            final CmapFormat6 tab2 = (CmapFormat6)table;
            final int index = code - tab2.firstCode;
            if (index < 0 || index >= tab2.glyphIdArray.length) {
                glyfID = 0;
            }
            else {
                glyfID = tab2.glyphIdArray[index];
            }
        }
        else if (fmt == 12) {
            final CmapFormat12 tab3 = (CmapFormat12)table;
            if (code > (int)tab3.groups.get(tab3.numGroups - 1).getMiddle()) {
                return 0;
            }
            int start = 0;
            int end = tab3.numGroups - 1;
            while (start + 1 < end) {
                final int middle = (start + end) / 2;
                if ((int)tab3.groups.get(middle).getLeft() <= code) {
                    start = middle;
                }
                else {
                    end = middle;
                }
            }
            if ((int)tab3.groups.get(start).getLeft() <= code && code <= (int)tab3.groups.get(start).getMiddle()) {
                glyfID = (int)tab3.groups.get(start).getRight() + code - (int)tab3.groups.get(start).getLeft();
            }
        }
        return glyfID;
    }
    
    public boolean inLimit(final char code) {
        return this.limitMix <= code && code < this.limitMax;
    }
    
    public String getGlyfByCode(final int key) {
        return this.codeToGlyph.getOrDefault(key, "");
    }
    
    public int getCodeByGlyf(final String val) {
        return this.glyphToCode.getOrDefault(val, 0);
    }
    
    private static class Header
    {
        public int majorVersion;
        public int minorVersion;
        public int numOfTables;
        public int searchRange;
        public int entrySelector;
        public int rangeShift;
    }
    
    private static class Directory
    {
        public String tag;
        public int checkSum;
        public int offset;
        public int length;
    }
    
    private static class NameLayout
    {
        public int format;
        public int count;
        public int stringOffset;
        public List<NameRecord> records;
        
        private NameLayout() {
            this.records = new LinkedList<NameRecord>();
        }
    }
    
    private static class NameRecord
    {
        public int platformID;
        public int encodingID;
        public int languageID;
        public int nameID;
        public int length;
        public int offset;
    }
    
    private static class HeadLayout
    {
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
    }
    
    private static class MaxpLayout
    {
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
    }
    
    private static class CmapLayout
    {
        public int version;
        public int numTables;
        public List<CmapRecord> records;
        public Map<Integer, CmapFormat> tables;
        
        private CmapLayout() {
            this.records = new LinkedList<CmapRecord>();
            this.tables = new HashMap<Integer, CmapFormat>();
        }
    }
    
    private static class CmapRecord
    {
        public int platformID;
        public int encodingID;
        public int offset;
    }
    
    private static class CmapFormat
    {
        public int format;
        public int length;
        public int language;
        public byte[] glyphIdArray;
    }
    
    private static class CmapFormat4 extends CmapFormat
    {
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
    }
    
    private static class CmapFormat6 extends CmapFormat
    {
        public int firstCode;
        public int entryCount;
        public int[] glyphIdArray;
    }
    
    private static class CmapFormat12 extends CmapFormat
    {
        public int reserved;
        public int length;
        public int language;
        public int numGroups;
        public List<Triple<Integer, Integer, Integer>> groups;
    }
    
    private static class GlyfLayout
    {
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
    }
    
    private static class ByteArrayReader
    {
        public int index;
        public byte[] buffer;
        
        public ByteArrayReader(final byte[] buffer, final int index) {
            this.buffer = buffer;
            this.index = index;
        }
        
        public long ReadUIntX(final long len) {
            long result = 0L;
            for (long i = 0L; i < len; ++i) {
                result <<= 8;
                result |= (this.buffer[this.index++] & 0xFF);
            }
            return result;
        }
        
        public long ReadUInt64() {
            return this.ReadUIntX(8L);
        }
        
        public int ReadUInt32() {
            return (int)this.ReadUIntX(4L);
        }
        
        public int ReadUInt16() {
            return (int)this.ReadUIntX(2L);
        }
        
        public short ReadInt16() {
            return (short)this.ReadUIntX(2L);
        }
        
        public short ReadUInt8() {
            return (short)this.ReadUIntX(1L);
        }
        
        public String ReadStrings(final int len, final Charset charset) {
            final byte[] result = (byte[])((len > 0) ? new byte[len] : null);
            for (int i = 0; i < len; ++i) {
                result[i] = this.buffer[this.index++];
            }
            return new String(result, charset);
        }
        
        public byte GetByte() {
            return this.buffer[this.index++];
        }
        
        public byte[] GetBytes(final int len) {
            final byte[] result = (byte[])((len > 0) ? new byte[len] : null);
            for (int i = 0; i < len; ++i) {
                result[i] = this.buffer[this.index++];
            }
            return result;
        }
        
        public int[] GetUInt16Array(final int len) {
            final int[] result = (int[])((len > 0) ? new int[len] : null);
            for (int i = 0; i < len; ++i) {
                result[i] = this.ReadUInt16();
            }
            return result;
        }
        
        public short[] GetInt16Array(final int len) {
            final short[] result = (short[])((len > 0) ? new short[len] : null);
            for (int i = 0; i < len; ++i) {
                result[i] = this.ReadInt16();
            }
            return result;
        }
    }
}
