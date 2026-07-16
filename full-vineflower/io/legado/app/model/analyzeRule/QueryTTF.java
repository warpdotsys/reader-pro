package io.legado.app.model.analyzeRule;

import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import org.apache.commons.lang3.tuple.Pair;
import org.apache.commons.lang3.tuple.Triple;

public class QueryTTF {
   private final QueryTTF.ByteArrayReader fontReader;
   private final QueryTTF.Header fileHeader = new QueryTTF.Header();
   private final List<QueryTTF.Directory> directorys = new LinkedList<>();
   private final QueryTTF.NameLayout name = new QueryTTF.NameLayout();
   private final QueryTTF.HeadLayout head = new QueryTTF.HeadLayout();
   private final QueryTTF.MaxpLayout maxp = new QueryTTF.MaxpLayout();
   private final List<Integer> loca = new LinkedList<>();
   private final QueryTTF.CmapLayout Cmap = new QueryTTF.CmapLayout();
   private final List<QueryTTF.GlyfLayout> glyf = new LinkedList<>();
   private final Pair<Integer, Integer>[] pps = new Pair[]{Pair.of(3, 10), Pair.of(0, 4), Pair.of(3, 1), Pair.of(1, 0), Pair.of(0, 3), Pair.of(0, 1)};
   public final Map<Integer, String> codeToGlyph = new HashMap<>();
   public final Map<String, Integer> glyphToCode = new HashMap<>();
   private int limitMix = 0;
   private int limitMax = 0;

   public QueryTTF(byte[] buffer) {
      this.fontReader = new QueryTTF.ByteArrayReader(buffer, 0);
      this.fileHeader.majorVersion = this.fontReader.ReadUInt16();
      this.fileHeader.minorVersion = this.fontReader.ReadUInt16();
      this.fileHeader.numOfTables = this.fontReader.ReadUInt16();
      this.fileHeader.searchRange = this.fontReader.ReadUInt16();
      this.fileHeader.entrySelector = this.fontReader.ReadUInt16();
      this.fileHeader.rangeShift = this.fontReader.ReadUInt16();

      for (int i = 0; i < this.fileHeader.numOfTables; i++) {
         QueryTTF.Directory d = new QueryTTF.Directory();
         d.tag = this.fontReader.ReadStrings(4, StandardCharsets.US_ASCII);
         d.checkSum = this.fontReader.ReadUInt32();
         d.offset = this.fontReader.ReadUInt32();
         d.length = this.fontReader.ReadUInt32();
         this.directorys.add(d);
      }

      for (QueryTTF.Directory Temp : this.directorys) {
         if (Temp.tag.equals("name")) {
            this.fontReader.index = Temp.offset;
            this.name.format = this.fontReader.ReadUInt16();
            this.name.count = this.fontReader.ReadUInt16();
            this.name.stringOffset = this.fontReader.ReadUInt16();

            for (int i = 0; i < this.name.count; i++) {
               QueryTTF.NameRecord record = new QueryTTF.NameRecord();
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

      for (QueryTTF.Directory Tempx : this.directorys) {
         if (Tempx.tag.equals("head")) {
            this.fontReader.index = Tempx.offset;
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

      for (QueryTTF.Directory Tempxx : this.directorys) {
         if (Tempxx.tag.equals("maxp")) {
            this.fontReader.index = Tempxx.offset;
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

      for (QueryTTF.Directory Tempxxx : this.directorys) {
         if (Tempxxx.tag.equals("loca")) {
            this.fontReader.index = Tempxxx.offset;
            int offset = this.head.indexToLocFormat == 0 ? 2 : 4;

            for (long i = 0L; i < Tempxxx.length; i += offset) {
               this.loca.add(offset == 2 ? this.fontReader.ReadUInt16() << 1 : this.fontReader.ReadUInt32());
            }
         }
      }

      for (QueryTTF.Directory Tempxxxx : this.directorys) {
         if (Tempxxxx.tag.equals("cmap")) {
            this.fontReader.index = Tempxxxx.offset;
            this.Cmap.version = this.fontReader.ReadUInt16();
            this.Cmap.numTables = this.fontReader.ReadUInt16();

            for (int i = 0; i < this.Cmap.numTables; i++) {
               QueryTTF.CmapRecord record = new QueryTTF.CmapRecord();
               record.platformID = this.fontReader.ReadUInt16();
               record.encodingID = this.fontReader.ReadUInt16();
               record.offset = this.fontReader.ReadUInt32();
               this.Cmap.records.add(record);
            }

            for (int i = 0; i < this.Cmap.numTables; i++) {
               int fmtOffset = this.Cmap.records.get(i).offset;
               this.fontReader.index = Tempxxxx.offset + fmtOffset;
               int EndIndex = this.fontReader.index;
               int format = this.fontReader.ReadUInt16();
               if (!this.Cmap.tables.containsKey(fmtOffset)) {
                  if (format == 0) {
                     QueryTTF.CmapFormat f = new QueryTTF.CmapFormat();
                     f.format = format;
                     f.length = this.fontReader.ReadUInt16();
                     f.language = this.fontReader.ReadUInt16();
                     f.glyphIdArray = this.fontReader.GetBytes(f.length - 6);
                     this.Cmap.tables.put(fmtOffset, f);
                  } else if (format == 4) {
                     QueryTTF.CmapFormat4 f = new QueryTTF.CmapFormat4();
                     f.format = format;
                     f.length = this.fontReader.ReadUInt16();
                     f.language = this.fontReader.ReadUInt16();
                     f.segCountX2 = this.fontReader.ReadUInt16();
                     int segCount = f.segCountX2 >> 1;
                     f.searchRange = this.fontReader.ReadUInt16();
                     f.entrySelector = this.fontReader.ReadUInt16();
                     f.rangeShift = this.fontReader.ReadUInt16();
                     f.endCode = this.fontReader.GetUInt16Array(segCount);
                     f.reservedPad = this.fontReader.ReadUInt16();
                     f.startCode = this.fontReader.GetUInt16Array(segCount);
                     f.idDelta = this.fontReader.GetInt16Array(segCount);
                     f.idRangeOffset = this.fontReader.GetUInt16Array(segCount);
                     f.glyphIdArray = this.fontReader.GetUInt16Array(EndIndex + f.length - this.fontReader.index >> 1);
                     this.Cmap.tables.put(fmtOffset, f);
                  } else if (format == 6) {
                     QueryTTF.CmapFormat6 f = new QueryTTF.CmapFormat6();
                     f.format = format;
                     f.length = this.fontReader.ReadUInt16();
                     f.language = this.fontReader.ReadUInt16();
                     f.firstCode = this.fontReader.ReadUInt16();
                     f.entryCount = this.fontReader.ReadUInt16();
                     f.glyphIdArray = this.fontReader.GetUInt16Array(f.entryCount);
                     this.Cmap.tables.put(fmtOffset, f);
                  } else if (format == 12) {
                     QueryTTF.CmapFormat12 f = new QueryTTF.CmapFormat12();
                     f.format = format;
                     f.reserved = this.fontReader.ReadUInt16();
                     f.length = this.fontReader.ReadUInt32();
                     f.language = this.fontReader.ReadUInt32();
                     f.numGroups = this.fontReader.ReadUInt32();
                     f.groups = new ArrayList<>(f.numGroups);

                     for (int n = 0; n < f.numGroups; n++) {
                        f.groups.add(Triple.of(this.fontReader.ReadUInt32(), this.fontReader.ReadUInt32(), this.fontReader.ReadUInt32()));
                     }

                     this.Cmap.tables.put(fmtOffset, f);
                  }
               }
            }
         }
      }

      for (QueryTTF.Directory Tempxxxxx : this.directorys) {
         if (Tempxxxxx.tag.equals("glyf")) {
            this.fontReader.index = Tempxxxxx.offset;

            for (int ix = 0; ix < this.maxp.numGlyphs; ix++) {
               this.fontReader.index = Tempxxxxx.offset + this.loca.get(ix);
               short numberOfContours = this.fontReader.ReadInt16();
               if (numberOfContours > 0) {
                  QueryTTF.GlyfLayout g = new QueryTTF.GlyfLayout();
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

                  for (int n = 0; n < flagLength; n++) {
                     g.flags[n] = this.fontReader.GetByte();
                     if ((g.flags[n] & 8) != 0) {
                        for (int m = this.fontReader.ReadUInt8(); m > 0; m--) {
                           g.flags[++n] = g.flags[n - 1];
                        }
                     }
                  }

                  g.xCoordinates = new short[flagLength];

                  for (int nx = 0; nx < flagLength; nx++) {
                     short same = (short)((g.flags[nx] & 16) != 0 ? 1 : -1);
                     if ((g.flags[nx] & 2) != 0) {
                        g.xCoordinates[nx] = (short)(same * this.fontReader.ReadUInt8());
                     } else {
                        g.xCoordinates[nx] = same == 1 ? 0 : this.fontReader.ReadInt16();
                     }
                  }

                  g.yCoordinates = new short[flagLength];

                  for (int nxx = 0; nxx < flagLength; nxx++) {
                     short same = (short)((g.flags[nxx] & 32) != 0 ? 1 : -1);
                     if ((g.flags[nxx] & 4) != 0) {
                        g.yCoordinates[nxx] = (short)(same * this.fontReader.ReadUInt8());
                     } else {
                        g.yCoordinates[nxx] = same == 1 ? 0 : this.fontReader.ReadInt16();
                     }
                  }

                  this.glyf.add(g);
               }
            }
         }
      }

      for (int key = 0; key < 130000; key++) {
         if (key == 255) {
            key = 13312;
         }

         int gid = this.getGlyfIndex(key);
         if (gid != 0) {
            StringBuilder sb = new StringBuilder();

            for (short b : this.glyf.get(gid).xCoordinates) {
               sb.append((int)b);
            }

            for (short b : this.glyf.get(gid).yCoordinates) {
               sb.append((int)b);
            }

            String val = sb.toString();
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

   public String getNameById(int nameId) {
      for (QueryTTF.Directory Temp : this.directorys) {
         if (Temp.tag.equals("name")) {
            this.fontReader.index = Temp.offset;
            break;
         }
      }

      for (QueryTTF.NameRecord record : this.name.records) {
         if (record.nameID == nameId) {
            this.fontReader.index = this.fontReader.index + this.name.stringOffset + record.offset;
            return this.fontReader.ReadStrings(record.length, record.platformID == 1 ? StandardCharsets.UTF_8 : StandardCharsets.UTF_16BE);
         }
      }

      return "error";
   }

   private int getGlyfIndex(int code) {
      if (code == 0) {
         return 0;
      } else {
         int fmtKey = 0;

         for (Pair<Integer, Integer> item : this.pps) {
            for (QueryTTF.CmapRecord record : this.Cmap.records) {
               if (item.getLeft() == record.platformID && item.getRight() == record.encodingID) {
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
         } else {
            int glyfID = 0;
            QueryTTF.CmapFormat table = this.Cmap.tables.get(fmtKey);

            assert table != null;

            int fmt = table.format;
            if (fmt == 0) {
               if (code < table.glyphIdArray.length) {
                  glyfID = table.glyphIdArray[code] & 255;
               }
            } else if (fmt == 4) {
               QueryTTF.CmapFormat4 tab = (QueryTTF.CmapFormat4)table;
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
                  glyfID = tab.glyphIdArray[code - tab.startCode[start] + (tab.idRangeOffset[start] >> 1) - (tab.idRangeOffset.length - start)];
               } else {
                  glyfID = code + tab.idDelta[start];
               }

               glyfID &= 65535;
            } else if (fmt == 6) {
               QueryTTF.CmapFormat6 tabx = (QueryTTF.CmapFormat6)table;
               int index = code - tabx.firstCode;
               if (index >= 0 && index < tabx.glyphIdArray.length) {
                  glyfID = tabx.glyphIdArray[index];
               } else {
                  glyfID = 0;
               }
            } else if (fmt == 12) {
               QueryTTF.CmapFormat12 tabx = (QueryTTF.CmapFormat12)table;
               if (code > tabx.groups.get(tabx.numGroups - 1).getMiddle()) {
                  return 0;
               }

               int start = 0;
               int end = tabx.numGroups - 1;

               while (start + 1 < end) {
                  int middle = (start + end) / 2;
                  if (tabx.groups.get(middle).getLeft() <= code) {
                     start = middle;
                  } else {
                     end = middle;
                  }
               }

               if (tabx.groups.get(start).getLeft() <= code && code <= tabx.groups.get(start).getMiddle()) {
                  glyfID = tabx.groups.get(start).getRight() + code - tabx.groups.get(start).getLeft();
               }
            }

            return glyfID;
         }
      }
   }

   public boolean inLimit(char code) {
      return this.limitMix <= code && code < this.limitMax;
   }

   public String getGlyfByCode(int key) {
      return this.codeToGlyph.getOrDefault(key, "");
   }

   public int getCodeByGlyf(String val) {
      return this.glyphToCode.getOrDefault(val, 0);
   }

   private static class ByteArrayReader {
      public int index;
      public byte[] buffer;

      public ByteArrayReader(byte[] buffer, int index) {
         this.buffer = buffer;
         this.index = index;
      }

      public long ReadUIntX(long len) {
         long result = 0L;

         for (long i = 0L; i < len; i++) {
            result <<= 8;
            result |= this.buffer[this.index++] & 255;
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

      public String ReadStrings(int len, Charset charset) {
         byte[] result = len > 0 ? new byte[len] : null;

         for (int i = 0; i < len; i++) {
            result[i] = this.buffer[this.index++];
         }

         return new String(result, charset);
      }

      public byte GetByte() {
         return this.buffer[this.index++];
      }

      public byte[] GetBytes(int len) {
         byte[] result = len > 0 ? new byte[len] : null;

         for (int i = 0; i < len; i++) {
            result[i] = this.buffer[this.index++];
         }

         return result;
      }

      public int[] GetUInt16Array(int len) {
         int[] result = len > 0 ? new int[len] : null;

         for (int i = 0; i < len; i++) {
            result[i] = this.ReadUInt16();
         }

         return result;
      }

      public short[] GetInt16Array(int len) {
         short[] result = len > 0 ? new short[len] : null;

         for (int i = 0; i < len; i++) {
            result[i] = this.ReadInt16();
         }

         return result;
      }
   }

   private static class CmapFormat {
      public int format;
      public int length;
      public int language;
      public byte[] glyphIdArray;

      private CmapFormat() {
      }
   }

   private static class CmapFormat12 extends QueryTTF.CmapFormat {
      public int reserved;
      public int length;
      public int language;
      public int numGroups;
      public List<Triple<Integer, Integer, Integer>> groups;

      private CmapFormat12() {
      }
   }

   private static class CmapFormat4 extends QueryTTF.CmapFormat {
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
      }
   }

   private static class CmapFormat6 extends QueryTTF.CmapFormat {
      public int firstCode;
      public int entryCount;
      public int[] glyphIdArray;

      private CmapFormat6() {
      }
   }

   private static class CmapLayout {
      public int version;
      public int numTables;
      public List<QueryTTF.CmapRecord> records = new LinkedList<>();
      public Map<Integer, QueryTTF.CmapFormat> tables = new HashMap<>();

      private CmapLayout() {
      }
   }

   private static class CmapRecord {
      public int platformID;
      public int encodingID;
      public int offset;

      private CmapRecord() {
      }
   }

   private static class Directory {
      public String tag;
      public int checkSum;
      public int offset;
      public int length;

      private Directory() {
      }
   }

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
   }

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
   }

   private static class Header {
      public int majorVersion;
      public int minorVersion;
      public int numOfTables;
      public int searchRange;
      public int entrySelector;
      public int rangeShift;

      private Header() {
      }
   }

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
   }

   private static class NameLayout {
      public int format;
      public int count;
      public int stringOffset;
      public List<QueryTTF.NameRecord> records = new LinkedList<>();

      private NameLayout() {
      }
   }

   private static class NameRecord {
      public int platformID;
      public int encodingID;
      public int languageID;
      public int nameID;
      public int length;
      public int offset;

      private NameRecord() {
      }
   }
}
