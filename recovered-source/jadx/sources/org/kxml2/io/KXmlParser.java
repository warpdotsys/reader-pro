package org.kxml2.io;

import io.legado.app.utils.FileUtils;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.util.Hashtable;
import me.ag2s.epublib.Constants;
import me.ag2s.epublib.epub.NCXDocumentV3;
import me.ag2s.epublib.epub.PackageDocumentBase;
import me.ag2s.epublib.util.IOUtil;
import me.ag2s.epublib.util.commons.io.ByteOrderMark;
import org.kxml2.wap.Wbxml;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

/* JADX INFO: loaded from: app-classes.jar:org/kxml2/io/KXmlParser.class */
public class KXmlParser implements XmlPullParser {
    private Object location;
    private static final String UNEXPECTED_EOF = "Unexpected EOF";
    private static final String ILLEGAL_TYPE = "Wrong event type";
    private static final int LEGACY = 999;
    private static final int XML_DECL = 998;
    private String version;
    private Boolean standalone;
    private boolean processNsp;
    private boolean relaxed;
    private Hashtable entityMap;
    private int depth;
    private Reader reader;
    private String encoding;
    private char[] srcBuf;
    private int srcPos;
    private int srcCount;
    private int line;
    private int column;
    private int txtPos;
    private int type;
    private boolean isWhitespace;
    private String namespace;
    private String prefix;
    private String name;
    private boolean degenerated;
    private int attributeCount;
    private String error;
    private int peekCount;
    private boolean wasCR;
    private boolean unresolved;
    private boolean token;
    private String[] elementStack = new String[16];
    private String[] nspStack = new String[8];
    private int[] nspCounts = new int[4];
    private char[] txtBuf = new char[Wbxml.EXT_T_0];
    private String[] attributes = new String[16];
    private int[] peek = new int[2];

    public KXmlParser() {
        this.srcBuf = new char[Runtime.getRuntime().freeMemory() >= FileUtils.MB ? IOUtil.DEFAULT_BUFFER_SIZE : Wbxml.EXT_T_0];
    }

    private final boolean isProp(String n1, boolean prop, String n2) {
        if (!n1.startsWith("http://xmlpull.org/v1/doc/")) {
            return false;
        }
        if (prop) {
            return n1.substring(42).equals(n2);
        }
        return n1.substring(40).equals(n2);
    }

    private final boolean adjustNsp() throws XmlPullParserException {
        String prefix;
        String attrName;
        boolean any = false;
        int i = 0;
        while (i < (this.attributeCount << 2)) {
            String attrName2 = this.attributes[i + 2];
            int cut = attrName2.indexOf(58);
            if (cut != -1) {
                prefix = attrName2.substring(0, cut);
                attrName = attrName2.substring(cut + 1);
            } else if (!attrName2.equals(NCXDocumentV3.XHTMLAttributes.xmlns)) {
                i += 4;
            } else {
                prefix = attrName2;
                attrName = null;
            }
            if (!prefix.equals(NCXDocumentV3.XHTMLAttributes.xmlns)) {
                any = true;
            } else {
                int[] iArr = this.nspCounts;
                int i2 = this.depth;
                int i3 = iArr[i2];
                iArr[i2] = i3 + 1;
                int j = i3 << 1;
                this.nspStack = ensureCapacity(this.nspStack, j + 2);
                this.nspStack[j] = attrName;
                this.nspStack[j + 1] = this.attributes[i + 3];
                if (attrName != null && this.attributes[i + 3].equals(PackageDocumentBase.PREFIX_OPF)) {
                    error("illegal empty namespace");
                }
                int i4 = this.attributeCount - 1;
                this.attributeCount = i4;
                System.arraycopy(this.attributes, i + 4, this.attributes, i, (i4 << 2) - i);
                i -= 4;
            }
            i += 4;
        }
        if (any) {
            for (int i5 = (this.attributeCount << 2) - 4; i5 >= 0; i5 -= 4) {
                String attrName3 = this.attributes[i5 + 2];
                int cut2 = attrName3.indexOf(58);
                if (cut2 == 0 && !this.relaxed) {
                    throw new RuntimeException("illegal attribute name: " + attrName3 + " at " + this);
                }
                if (cut2 != -1) {
                    String attrPrefix = attrName3.substring(0, cut2);
                    String attrName4 = attrName3.substring(cut2 + 1);
                    String attrNs = getNamespace(attrPrefix);
                    if (attrNs == null && !this.relaxed) {
                        throw new RuntimeException("Undefined Prefix: " + attrPrefix + " in " + this);
                    }
                    this.attributes[i5] = attrNs;
                    this.attributes[i5 + 1] = attrPrefix;
                    this.attributes[i5 + 2] = attrName4;
                }
            }
        }
        int cut3 = this.name.indexOf(58);
        if (cut3 == 0) {
            error("illegal tag name: " + this.name);
        }
        if (cut3 != -1) {
            this.prefix = this.name.substring(0, cut3);
            this.name = this.name.substring(cut3 + 1);
        }
        this.namespace = getNamespace(this.prefix);
        if (this.namespace == null) {
            if (this.prefix != null) {
                error("undefined prefix: " + this.prefix);
            }
            this.namespace = PackageDocumentBase.PREFIX_OPF;
        }
        return any;
    }

    private final String[] ensureCapacity(String[] arr, int required) {
        if (arr.length >= required) {
            return arr;
        }
        String[] bigger = new String[required + 16];
        System.arraycopy(arr, 0, bigger, 0, arr.length);
        return bigger;
    }

    private final void error(String desc) throws XmlPullParserException {
        if (this.relaxed) {
            if (this.error == null) {
                this.error = "ERR: " + desc;
                return;
            }
            return;
        }
        exception(desc);
    }

    private final void exception(String desc) throws XmlPullParserException {
        throw new XmlPullParserException(desc.length() < 100 ? desc : desc.substring(0, 100) + "\n", this, null);
    }

    /*  JADX ERROR: JadxRuntimeException in pass: RegionMakerVisitor
        jadx.core.utils.exceptions.JadxRuntimeException: Failed to find switch 'out' block (already processed)
            at jadx.core.dex.visitors.regions.maker.SwitchRegionMaker.calcSwitchOut(SwitchRegionMaker.java:217)
            at jadx.core.dex.visitors.regions.maker.SwitchRegionMaker.process(SwitchRegionMaker.java:68)
            at jadx.core.dex.visitors.regions.maker.RegionMaker.traverse(RegionMaker.java:112)
            at jadx.core.dex.visitors.regions.maker.RegionMaker.makeRegion(RegionMaker.java:66)
            at jadx.core.dex.visitors.regions.maker.IfRegionMaker.process(IfRegionMaker.java:102)
            at jadx.core.dex.visitors.regions.maker.RegionMaker.traverse(RegionMaker.java:106)
            at jadx.core.dex.visitors.regions.maker.RegionMaker.makeRegion(RegionMaker.java:66)
            at jadx.core.dex.visitors.regions.maker.IfRegionMaker.process(IfRegionMaker.java:102)
            at jadx.core.dex.visitors.regions.maker.RegionMaker.traverse(RegionMaker.java:106)
            at jadx.core.dex.visitors.regions.maker.RegionMaker.makeRegion(RegionMaker.java:66)
            at jadx.core.dex.visitors.regions.maker.LoopRegionMaker.process(LoopRegionMaker.java:104)
            at jadx.core.dex.visitors.regions.maker.RegionMaker.traverse(RegionMaker.java:89)
            at jadx.core.dex.visitors.regions.maker.RegionMaker.makeRegion(RegionMaker.java:66)
            at jadx.core.dex.visitors.regions.maker.RegionMaker.makeMthRegion(RegionMaker.java:48)
            at jadx.core.dex.visitors.regions.RegionMakerVisitor.visit(RegionMakerVisitor.java:25)
        */
    private final void nextImpl() throws org.xmlpull.v1.XmlPullParserException, java.io.IOException {
        /*
            Method dump skipped, instruction units count: 247
            To view this dump add '--comments-level debug' option
        */
        throw new UnsupportedOperationException("Method not decompiled: org.kxml2.io.KXmlParser.nextImpl():void");
    }

    private final int parseLegacy(boolean push) throws XmlPullParserException, IOException {
        int result;
        int term;
        String req = PackageDocumentBase.PREFIX_OPF;
        int prev = 0;
        read();
        int c = read();
        if (c == 63) {
            if ((peek(0) == 120 || peek(0) == 88) && (peek(1) == 109 || peek(1) == 77)) {
                if (push) {
                    push(peek(0));
                    push(peek(1));
                }
                read();
                read();
                if ((peek(0) == 108 || peek(0) == 76) && peek(1) <= 32) {
                    if (this.line != 1 || this.column > 4) {
                        error("PI must not start with xml");
                    }
                    parseStartTag(true);
                    if (this.attributeCount < 1 || !"version".equals(this.attributes[2])) {
                        error("version expected");
                    }
                    this.version = this.attributes[3];
                    int pos = 1;
                    if (1 < this.attributeCount && "encoding".equals(this.attributes[6])) {
                        this.encoding = this.attributes[7];
                        pos = 1 + 1;
                    }
                    if (pos < this.attributeCount && "standalone".equals(this.attributes[(4 * pos) + 2])) {
                        String st = this.attributes[3 + (4 * pos)];
                        if ("yes".equals(st)) {
                            this.standalone = new Boolean(true);
                        } else if (PackageDocumentBase.OPFValues.no.equals(st)) {
                            this.standalone = new Boolean(false);
                        } else {
                            error("illegal standalone value: " + st);
                        }
                        pos++;
                    }
                    if (pos != this.attributeCount) {
                        error("illegal xmldecl");
                    }
                    this.isWhitespace = true;
                    this.txtPos = 0;
                    return XML_DECL;
                }
            }
            term = 63;
            result = 8;
        } else if (c == 33) {
            if (peek(0) == 45) {
                result = 9;
                req = "--";
                term = 45;
            } else if (peek(0) == 91) {
                result = 5;
                req = "[CDATA[";
                term = 93;
                push = true;
            } else {
                result = 10;
                req = "DOCTYPE";
                term = -1;
            }
        } else {
            error("illegal: <" + c);
            return 9;
        }
        for (int i = 0; i < req.length(); i++) {
            read(req.charAt(i));
        }
        if (result == 10) {
            parseDoctype(push);
        } else {
            while (true) {
                int c2 = read();
                if (c2 == -1) {
                    error(UNEXPECTED_EOF);
                    return 9;
                }
                if (push) {
                    push(c2);
                }
                if ((term != 63 && c2 != term) || peek(0) != term || peek(1) != 62) {
                    prev = c2;
                } else {
                    if (term == 45 && prev == 45 && !this.relaxed) {
                        error("illegal comment delimiter: --->");
                    }
                    read();
                    read();
                    if (push && term != 63) {
                        this.txtPos--;
                    }
                }
            }
        }
        return result;
    }

    private final void parseDoctype(boolean push) throws XmlPullParserException, IOException {
        int nesting = 1;
        boolean quoted = false;
        while (true) {
            int i = read();
            switch (i) {
                case IOUtil.EOF /* -1 */:
                    error(UNEXPECTED_EOF);
                    return;
                case 39:
                    quoted = !quoted;
                    break;
                case 60:
                    if (!quoted) {
                        nesting++;
                    }
                    break;
                case 62:
                    if (!quoted) {
                        nesting--;
                        if (nesting == 0) {
                            return;
                        }
                    }
                    break;
            }
            if (push) {
                push(i);
            }
        }
    }

    private final void parseEndTag() throws XmlPullParserException, IOException {
        read();
        read();
        this.name = readName();
        skip();
        read('>');
        int sp = (this.depth - 1) << 2;
        if (this.depth == 0) {
            error("element stack empty");
            this.type = 9;
        } else if (!this.relaxed) {
            if (!this.name.equals(this.elementStack[sp + 3])) {
                error("expected: /" + this.elementStack[sp + 3] + " read: " + this.name);
            }
            this.namespace = this.elementStack[sp];
            this.prefix = this.elementStack[sp + 1];
            this.name = this.elementStack[sp + 2];
        }
    }

    private final int peekType() throws IOException {
        switch (peek(0)) {
            case IOUtil.EOF /* -1 */:
                return 1;
            case 38:
                return 6;
            case 60:
                switch (peek(1)) {
                    case 33:
                    case 63:
                        return LEGACY;
                    case 47:
                        return 3;
                    default:
                        return 2;
                }
            default:
                return 4;
        }
    }

    private final String get(int pos) {
        return new String(this.txtBuf, pos, this.txtPos - pos);
    }

    private final void push(int c) {
        this.isWhitespace &= c <= 32;
        if (this.txtPos + 1 >= this.txtBuf.length) {
            char[] bigger = new char[((this.txtPos * 4) / 3) + 4];
            System.arraycopy(this.txtBuf, 0, bigger, 0, this.txtPos);
            this.txtBuf = bigger;
        }
        if (c > 65535) {
            int offset = c - 65536;
            char[] cArr = this.txtBuf;
            int i = this.txtPos;
            this.txtPos = i + 1;
            cArr[i] = (char) ((offset >>> 10) + 55296);
            char[] cArr2 = this.txtBuf;
            int i2 = this.txtPos;
            this.txtPos = i2 + 1;
            cArr2[i2] = (char) ((offset & 1023) + 56320);
            return;
        }
        char[] cArr3 = this.txtBuf;
        int i3 = this.txtPos;
        this.txtPos = i3 + 1;
        cArr3[i3] = (char) c;
    }

    private final void parseStartTag(boolean xmldecl) throws XmlPullParserException, IOException {
        if (!xmldecl) {
            read();
        }
        this.name = readName();
        this.attributeCount = 0;
        while (true) {
            skip();
            int c = peek(0);
            if (xmldecl) {
                if (c == 63) {
                    read();
                    read('>');
                    return;
                }
            } else {
                if (c == 47) {
                    this.degenerated = true;
                    read();
                    skip();
                    read('>');
                    break;
                }
                if (c == 62 && !xmldecl) {
                    read();
                    break;
                }
            }
            if (c == -1) {
                error(UNEXPECTED_EOF);
                return;
            }
            String attrName = readName();
            if (attrName.length() == 0) {
                error("attr name expected");
                break;
            }
            int i = this.attributeCount;
            this.attributeCount = i + 1;
            int i2 = i << 2;
            this.attributes = ensureCapacity(this.attributes, i2 + 4);
            int i3 = i2 + 1;
            this.attributes[i2] = PackageDocumentBase.PREFIX_OPF;
            int i4 = i3 + 1;
            this.attributes[i3] = null;
            int i5 = i4 + 1;
            this.attributes[i4] = attrName;
            skip();
            if (peek(0) != 61) {
                if (!this.relaxed) {
                    error("Attr.value missing f. " + attrName);
                }
                this.attributes[i5] = attrName;
            } else {
                read('=');
                skip();
                int delimiter = peek(0);
                if (delimiter != 39 && delimiter != 34) {
                    if (!this.relaxed) {
                        error("attr value delimiter missing!");
                    }
                    delimiter = 32;
                } else {
                    read();
                }
                int p = this.txtPos;
                pushText(delimiter, true);
                this.attributes[i5] = get(p);
                this.txtPos = p;
                if (delimiter != 32) {
                    read();
                }
            }
        }
        int i6 = this.depth;
        this.depth = i6 + 1;
        int sp = i6 << 2;
        this.elementStack = ensureCapacity(this.elementStack, sp + 4);
        this.elementStack[sp + 3] = this.name;
        if (this.depth >= this.nspCounts.length) {
            int[] bigger = new int[this.depth + 4];
            System.arraycopy(this.nspCounts, 0, bigger, 0, this.nspCounts.length);
            this.nspCounts = bigger;
        }
        this.nspCounts[this.depth] = this.nspCounts[this.depth - 1];
        if (this.processNsp) {
            adjustNsp();
        } else {
            this.namespace = PackageDocumentBase.PREFIX_OPF;
        }
        this.elementStack[sp] = this.namespace;
        this.elementStack[sp + 1] = this.prefix;
        this.elementStack[sp + 2] = this.name;
    }

    private final void pushEntity() throws XmlPullParserException, IOException {
        int c;
        push(read());
        int pos = this.txtPos;
        while (true) {
            int c2 = peek(0);
            if (c2 == 59) {
                read();
                String code = get(pos);
                this.txtPos = pos - 1;
                if (this.token && this.type == 6) {
                    this.name = code;
                }
                if (code.charAt(0) == '#') {
                    if (code.charAt(1) == 'x') {
                        c = Integer.parseInt(code.substring(2), 16);
                    } else {
                        c = Integer.parseInt(code.substring(1));
                    }
                    push(c);
                    return;
                }
                String result = (String) this.entityMap.get(code);
                this.unresolved = result == null;
                if (this.unresolved) {
                    if (!this.token) {
                        error("unresolved: &" + code + ";");
                        return;
                    }
                    return;
                } else {
                    for (int i = 0; i < result.length(); i++) {
                        push(result.charAt(i));
                    }
                    return;
                }
            }
            if (c2 < 128 && ((c2 < 48 || c2 > 57) && ((c2 < 97 || c2 > 122) && ((c2 < 65 || c2 > 90) && c2 != 95 && c2 != 45 && c2 != 35)))) {
                if (!this.relaxed) {
                    error("unterminated entity ref");
                }
                System.out.println("broken entitiy: " + get(pos - 1));
                return;
            }
            push(read());
        }
    }

    private final void pushText(int delimiter, boolean resolveEntities) throws XmlPullParserException, IOException {
        int next = peek(0);
        int cbrCount = 0;
        while (next != -1 && next != delimiter) {
            if (delimiter != 32 || (next > 32 && next != 62)) {
                if (next == 38) {
                    if (resolveEntities) {
                        pushEntity();
                    } else {
                        return;
                    }
                } else if (next == 10 && this.type == 2) {
                    read();
                    push(32);
                } else {
                    push(read());
                }
                if (next == 62 && cbrCount >= 2 && delimiter != 93) {
                    error("Illegal: ]]>");
                }
                if (next == 93) {
                    cbrCount++;
                } else {
                    cbrCount = 0;
                }
                next = peek(0);
            } else {
                return;
            }
        }
    }

    private final void read(char c) throws XmlPullParserException, IOException {
        int a = read();
        if (a != c) {
            error("expected: '" + c + "' actual: '" + ((char) a) + "'");
        }
    }

    private final int read() throws IOException {
        int result;
        if (this.peekCount == 0) {
            result = peek(0);
        } else {
            result = this.peek[0];
            this.peek[0] = this.peek[1];
        }
        this.peekCount--;
        this.column++;
        if (result == 10) {
            this.line++;
            this.column = 1;
        }
        return result;
    }

    private final int peek(int pos) throws IOException {
        int nw;
        while (pos >= this.peekCount) {
            if (this.srcBuf.length <= 1) {
                nw = this.reader.read();
            } else if (this.srcPos < this.srcCount) {
                char[] cArr = this.srcBuf;
                int i = this.srcPos;
                this.srcPos = i + 1;
                nw = cArr[i];
            } else {
                this.srcCount = this.reader.read(this.srcBuf, 0, this.srcBuf.length);
                if (this.srcCount <= 0) {
                    nw = -1;
                } else {
                    nw = this.srcBuf[0];
                }
                this.srcPos = 1;
            }
            if (nw == 13) {
                this.wasCR = true;
                int[] iArr = this.peek;
                int i2 = this.peekCount;
                this.peekCount = i2 + 1;
                iArr[i2] = 10;
            } else {
                if (nw == 10) {
                    if (!this.wasCR) {
                        int[] iArr2 = this.peek;
                        int i3 = this.peekCount;
                        this.peekCount = i3 + 1;
                        iArr2[i3] = 10;
                    }
                } else {
                    int[] iArr3 = this.peek;
                    int i4 = this.peekCount;
                    this.peekCount = i4 + 1;
                    iArr3[i4] = nw;
                }
                this.wasCR = false;
            }
        }
        return this.peek[pos];
    }

    private final String readName() throws XmlPullParserException, IOException {
        int pos = this.txtPos;
        int c = peek(0);
        if ((c < 97 || c > 122) && ((c < 65 || c > 90) && c != 95 && c != 58 && c < 192 && !this.relaxed)) {
            error("name expected");
        }
        while (true) {
            push(read());
            int c2 = peek(0);
            if (c2 < 97 || c2 > 122) {
                if (c2 < 65 || c2 > 90) {
                    if (c2 < 48 || c2 > 57) {
                        if (c2 != 95 && c2 != 45 && c2 != 58 && c2 != 46 && c2 < 183) {
                            String result = get(pos);
                            this.txtPos = pos;
                            return result;
                        }
                    }
                }
            }
        }
    }

    private final void skip() throws IOException {
        while (true) {
            int c = peek(0);
            if (c <= 32 && c != -1) {
                read();
            } else {
                return;
            }
        }
    }

    @Override // org.xmlpull.v1.XmlPullParser
    public void setInput(Reader reader) throws XmlPullParserException {
        this.reader = reader;
        this.line = 1;
        this.column = 0;
        this.type = 0;
        this.name = null;
        this.namespace = null;
        this.degenerated = false;
        this.attributeCount = -1;
        this.encoding = null;
        this.version = null;
        this.standalone = null;
        if (reader == null) {
            return;
        }
        this.srcPos = 0;
        this.srcCount = 0;
        this.peekCount = 0;
        this.depth = 0;
        this.entityMap = new Hashtable();
        this.entityMap.put("amp", "&");
        this.entityMap.put("apos", "'");
        this.entityMap.put("gt", ">");
        this.entityMap.put("lt", "<");
        this.entityMap.put("quot", "\"");
    }

    /* JADX WARN: Removed duplicated region for block: B:41:0x01af A[Catch: Exception -> 0x0244, TryCatch #0 {Exception -> 0x0244, blocks: (B:9:0x001f, B:11:0x0027, B:14:0x0036, B:15:0x0056, B:18:0x0060, B:19:0x00a4, B:20:0x00af, B:21:0x00ba, B:22:0x00cd, B:23:0x00e0, B:24:0x00fb, B:25:0x0116, B:28:0x0125, B:30:0x013f, B:32:0x0160, B:34:0x016c, B:36:0x0178, B:37:0x017e, B:39:0x01a5, B:41:0x01af, B:42:0x01d1, B:44:0x01db, B:45:0x01fd, B:47:0x0208, B:51:0x0223), top: B:56:0x001f }] */
    /* JADX WARN: Removed duplicated region for block: B:42:0x01d1 A[Catch: Exception -> 0x0244, TryCatch #0 {Exception -> 0x0244, blocks: (B:9:0x001f, B:11:0x0027, B:14:0x0036, B:15:0x0056, B:18:0x0060, B:19:0x00a4, B:20:0x00af, B:21:0x00ba, B:22:0x00cd, B:23:0x00e0, B:24:0x00fb, B:25:0x0116, B:28:0x0125, B:30:0x013f, B:32:0x0160, B:34:0x016c, B:36:0x0178, B:37:0x017e, B:39:0x01a5, B:41:0x01af, B:42:0x01d1, B:44:0x01db, B:45:0x01fd, B:47:0x0208, B:51:0x0223), top: B:56:0x001f }] */
    @Override // org.xmlpull.v1.XmlPullParser
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    public void setInput(InputStream is, String _enc) throws XmlPullParserException {
        int i;
        this.srcPos = 0;
        this.srcCount = 0;
        String enc = _enc;
        if (is == null) {
            throw new IllegalArgumentException();
        }
        if (enc == null) {
            int chk = 0;
            while (this.srcCount < 4 && (i = is.read()) != -1) {
                try {
                    chk = (chk << 8) | i;
                    char[] cArr = this.srcBuf;
                    int i2 = this.srcCount;
                    this.srcCount = i2 + 1;
                    cArr[i2] = (char) i;
                } catch (Exception e) {
                    throw new XmlPullParserException("Invalid stream or encoding: " + e.toString(), this, e);
                }
            }
            if (this.srcCount == 4) {
                switch (chk) {
                    case -131072:
                        enc = "UTF-32LE";
                        this.srcCount = 0;
                        break;
                    case 60:
                        enc = "UTF-32BE";
                        this.srcBuf[0] = '<';
                        this.srcCount = 1;
                        break;
                    case ByteOrderMark.UTF_BOM /* 65279 */:
                        enc = "UTF-32BE";
                        this.srcCount = 0;
                        break;
                    case 3932223:
                        enc = "UTF-16BE";
                        this.srcBuf[0] = '<';
                        this.srcBuf[1] = '?';
                        this.srcCount = 2;
                        break;
                    case 1006632960:
                        enc = "UTF-32LE";
                        this.srcBuf[0] = '<';
                        this.srcCount = 1;
                        break;
                    case 1006649088:
                        enc = "UTF-16LE";
                        this.srcBuf[0] = '<';
                        this.srcBuf[1] = '?';
                        this.srcCount = 2;
                        break;
                    case 1010792557:
                        while (true) {
                            int i3 = is.read();
                            if (i3 != -1) {
                                char[] cArr2 = this.srcBuf;
                                int i4 = this.srcCount;
                                this.srcCount = i4 + 1;
                                cArr2[i4] = (char) i3;
                                if (i3 == 62) {
                                    String s = new String(this.srcBuf, 0, this.srcCount);
                                    int i0 = s.indexOf("encoding");
                                    if (i0 != -1) {
                                        while (s.charAt(i0) != '\"' && s.charAt(i0) != '\'') {
                                            i0++;
                                        }
                                        int i5 = i0;
                                        int i02 = i0 + 1;
                                        char deli = s.charAt(i5);
                                        int i1 = s.indexOf(deli, i02);
                                        enc = s.substring(i02, i1);
                                    }
                                }
                            }
                        }
                        if ((chk & (-65536)) != -16842752) {
                            enc = "UTF-16BE";
                            this.srcBuf[0] = (char) ((this.srcBuf[2] << '\b') | this.srcBuf[3]);
                            this.srcCount = 1;
                        } else if ((chk & (-65536)) == -131072) {
                            enc = "UTF-16LE";
                            this.srcBuf[0] = (char) ((this.srcBuf[3] << '\b') | this.srcBuf[2]);
                            this.srcCount = 1;
                        } else if ((chk & (-256)) == -272908544) {
                            enc = Constants.CHARACTER_ENCODING;
                            this.srcBuf[0] = this.srcBuf[3];
                            this.srcCount = 1;
                        }
                        break;
                    default:
                        if ((chk & (-65536)) != -16842752) {
                        }
                        break;
                }
            }
        }
        if (enc == null) {
            enc = Constants.CHARACTER_ENCODING;
        }
        int sc = this.srcCount;
        setInput(new InputStreamReader(is, enc));
        this.encoding = _enc;
        this.srcCount = sc;
    }

    @Override // org.xmlpull.v1.XmlPullParser
    public boolean getFeature(String feature) {
        if ("http://xmlpull.org/v1/doc/features.html#process-namespaces".equals(feature)) {
            return this.processNsp;
        }
        if (isProp(feature, false, "relaxed")) {
            return this.relaxed;
        }
        return false;
    }

    @Override // org.xmlpull.v1.XmlPullParser
    public String getInputEncoding() {
        return this.encoding;
    }

    @Override // org.xmlpull.v1.XmlPullParser
    public void defineEntityReplacementText(String entity, String value) throws XmlPullParserException {
        if (this.entityMap == null) {
            throw new RuntimeException("entity replacement text must be defined after setInput!");
        }
        this.entityMap.put(entity, value);
    }

    @Override // org.xmlpull.v1.XmlPullParser
    public Object getProperty(String property) {
        if (isProp(property, true, "xmldecl-version")) {
            return this.version;
        }
        if (isProp(property, true, "xmldecl-standalone")) {
            return this.standalone;
        }
        if (isProp(property, true, "location")) {
            return this.location != null ? this.location : this.reader.toString();
        }
        return null;
    }

    @Override // org.xmlpull.v1.XmlPullParser
    public int getNamespaceCount(int depth) {
        if (depth > this.depth) {
            throw new IndexOutOfBoundsException();
        }
        return this.nspCounts[depth];
    }

    @Override // org.xmlpull.v1.XmlPullParser
    public String getNamespacePrefix(int pos) {
        return this.nspStack[pos << 1];
    }

    @Override // org.xmlpull.v1.XmlPullParser
    public String getNamespaceUri(int pos) {
        return this.nspStack[(pos << 1) + 1];
    }

    @Override // org.xmlpull.v1.XmlPullParser
    public String getNamespace(String prefix) {
        if ("xml".equals(prefix)) {
            return "http://www.w3.org/XML/1998/namespace";
        }
        if (NCXDocumentV3.XHTMLAttributes.xmlns.equals(prefix)) {
            return "http://www.w3.org/2000/xmlns/";
        }
        for (int i = (getNamespaceCount(this.depth) << 1) - 2; i >= 0; i -= 2) {
            if (prefix == null) {
                if (this.nspStack[i] == null) {
                    return this.nspStack[i + 1];
                }
            } else if (prefix.equals(this.nspStack[i])) {
                return this.nspStack[i + 1];
            }
        }
        return null;
    }

    @Override // org.xmlpull.v1.XmlPullParser
    public int getDepth() {
        return this.depth;
    }

    @Override // org.xmlpull.v1.XmlPullParser
    public String getPositionDescription() {
        StringBuffer buf = new StringBuffer(this.type < TYPES.length ? TYPES[this.type] : "unknown");
        buf.append(' ');
        if (this.type == 2 || this.type == 3) {
            if (this.degenerated) {
                buf.append("(empty) ");
            }
            buf.append('<');
            if (this.type == 3) {
                buf.append('/');
            }
            if (this.prefix != null) {
                buf.append("{" + this.namespace + "}" + this.prefix + ":");
            }
            buf.append(this.name);
            int cnt = this.attributeCount << 2;
            for (int i = 0; i < cnt; i += 4) {
                buf.append(' ');
                if (this.attributes[i + 1] != null) {
                    buf.append("{" + this.attributes[i] + "}" + this.attributes[i + 1] + ":");
                }
                buf.append(this.attributes[i + 2] + "='" + this.attributes[i + 3] + "'");
            }
            buf.append('>');
        } else if (this.type != 7) {
            if (this.type != 4) {
                buf.append(getText());
            } else if (this.isWhitespace) {
                buf.append("(whitespace)");
            } else {
                String text = getText();
                if (text.length() > 16) {
                    text = text.substring(0, 16) + "...";
                }
                buf.append(text);
            }
        }
        buf.append("@" + this.line + ":" + this.column);
        if (this.location != null) {
            buf.append(" in ");
            buf.append(this.location);
        } else if (this.reader != null) {
            buf.append(" in ");
            buf.append(this.reader.toString());
        }
        return buf.toString();
    }

    @Override // org.xmlpull.v1.XmlPullParser
    public int getLineNumber() {
        return this.line;
    }

    @Override // org.xmlpull.v1.XmlPullParser
    public int getColumnNumber() {
        return this.column;
    }

    @Override // org.xmlpull.v1.XmlPullParser
    public boolean isWhitespace() throws XmlPullParserException {
        if (this.type != 4 && this.type != 7 && this.type != 5) {
            exception(ILLEGAL_TYPE);
        }
        return this.isWhitespace;
    }

    @Override // org.xmlpull.v1.XmlPullParser
    public String getText() {
        if (this.type < 4 || (this.type == 6 && this.unresolved)) {
            return null;
        }
        return get(0);
    }

    @Override // org.xmlpull.v1.XmlPullParser
    public char[] getTextCharacters(int[] poslen) {
        if (this.type >= 4) {
            if (this.type == 6) {
                poslen[0] = 0;
                poslen[1] = this.name.length();
                return this.name.toCharArray();
            }
            poslen[0] = 0;
            poslen[1] = this.txtPos;
            return this.txtBuf;
        }
        poslen[0] = -1;
        poslen[1] = -1;
        return null;
    }

    @Override // org.xmlpull.v1.XmlPullParser
    public String getNamespace() {
        return this.namespace;
    }

    @Override // org.xmlpull.v1.XmlPullParser
    public String getName() {
        return this.name;
    }

    @Override // org.xmlpull.v1.XmlPullParser
    public String getPrefix() {
        return this.prefix;
    }

    @Override // org.xmlpull.v1.XmlPullParser
    public boolean isEmptyElementTag() throws XmlPullParserException {
        if (this.type != 2) {
            exception(ILLEGAL_TYPE);
        }
        return this.degenerated;
    }

    @Override // org.xmlpull.v1.XmlPullParser
    public int getAttributeCount() {
        return this.attributeCount;
    }

    @Override // org.xmlpull.v1.XmlPullParser
    public String getAttributeType(int index) {
        return "CDATA";
    }

    @Override // org.xmlpull.v1.XmlPullParser
    public boolean isAttributeDefault(int index) {
        return false;
    }

    @Override // org.xmlpull.v1.XmlPullParser
    public String getAttributeNamespace(int index) {
        if (index >= this.attributeCount) {
            throw new IndexOutOfBoundsException();
        }
        return this.attributes[index << 2];
    }

    @Override // org.xmlpull.v1.XmlPullParser
    public String getAttributeName(int index) {
        if (index >= this.attributeCount) {
            throw new IndexOutOfBoundsException();
        }
        return this.attributes[(index << 2) + 2];
    }

    @Override // org.xmlpull.v1.XmlPullParser
    public String getAttributePrefix(int index) {
        if (index >= this.attributeCount) {
            throw new IndexOutOfBoundsException();
        }
        return this.attributes[(index << 2) + 1];
    }

    @Override // org.xmlpull.v1.XmlPullParser
    public String getAttributeValue(int index) {
        if (index >= this.attributeCount) {
            throw new IndexOutOfBoundsException();
        }
        return this.attributes[(index << 2) + 3];
    }

    @Override // org.xmlpull.v1.XmlPullParser
    public String getAttributeValue(String namespace, String name) {
        for (int i = (this.attributeCount << 2) - 4; i >= 0; i -= 4) {
            if (this.attributes[i + 2].equals(name) && (namespace == null || this.attributes[i].equals(namespace))) {
                return this.attributes[i + 3];
            }
        }
        return null;
    }

    @Override // org.xmlpull.v1.XmlPullParser
    public int getEventType() throws XmlPullParserException {
        return this.type;
    }

    @Override // org.xmlpull.v1.XmlPullParser
    public int next() throws XmlPullParserException, IOException {
        this.txtPos = 0;
        this.isWhitespace = true;
        int minType = 9999;
        this.token = false;
        while (true) {
            nextImpl();
            if (this.type < minType) {
                minType = this.type;
            }
            if (minType <= 6 && (minType < 4 || peekType() < 4)) {
                break;
            }
        }
        this.type = minType;
        if (this.type > 4) {
            this.type = 4;
        }
        return this.type;
    }

    @Override // org.xmlpull.v1.XmlPullParser
    public int nextToken() throws XmlPullParserException, IOException {
        this.isWhitespace = true;
        this.txtPos = 0;
        this.token = true;
        nextImpl();
        return this.type;
    }

    @Override // org.xmlpull.v1.XmlPullParser
    public int nextTag() throws XmlPullParserException, IOException {
        next();
        if (this.type == 4 && this.isWhitespace) {
            next();
        }
        if (this.type != 3 && this.type != 2) {
            exception("unexpected type");
        }
        return this.type;
    }

    @Override // org.xmlpull.v1.XmlPullParser
    public void require(int type, String namespace, String name) throws XmlPullParserException, IOException {
        if (type != this.type || ((namespace != null && !namespace.equals(getNamespace())) || (name != null && !name.equals(getName())))) {
            exception("expected: " + TYPES[type] + " {" + namespace + "}" + name);
        }
    }

    @Override // org.xmlpull.v1.XmlPullParser
    public String nextText() throws XmlPullParserException, IOException {
        String result;
        if (this.type != 2) {
            exception("precondition: START_TAG");
        }
        next();
        if (this.type == 4) {
            result = getText();
            next();
        } else {
            result = PackageDocumentBase.PREFIX_OPF;
        }
        if (this.type != 3) {
            exception("END_TAG expected");
        }
        return result;
    }

    @Override // org.xmlpull.v1.XmlPullParser
    public void setFeature(String feature, boolean value) throws XmlPullParserException {
        if ("http://xmlpull.org/v1/doc/features.html#process-namespaces".equals(feature)) {
            this.processNsp = value;
        } else if (isProp(feature, false, "relaxed")) {
            this.relaxed = value;
        } else {
            exception("unsupported feature: " + feature);
        }
    }

    @Override // org.xmlpull.v1.XmlPullParser
    public void setProperty(String property, Object value) throws XmlPullParserException {
        if (isProp(property, true, "location")) {
            this.location = value;
            return;
        }
        throw new XmlPullParserException("unsupported property: " + property);
    }

    public void skipSubTree() throws XmlPullParserException, IOException {
        require(2, null, null);
        int level = 1;
        while (level > 0) {
            int eventType = next();
            if (eventType == 3) {
                level--;
            } else if (eventType == 2) {
                level++;
            }
        }
    }
}
