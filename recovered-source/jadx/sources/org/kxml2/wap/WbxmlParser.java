package org.kxml2.wap;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.Reader;
import java.io.UnsupportedEncodingException;
import java.util.Hashtable;
import java.util.Vector;
import me.ag2s.epublib.Constants;
import me.ag2s.epublib.epub.NCXDocumentV3;
import me.ag2s.epublib.epub.PackageDocumentBase;
import me.ag2s.epublib.util.IOUtil;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

/* JADX INFO: loaded from: reader-pro-classes-3.2.14.jar:org/kxml2/wap/WbxmlParser.class */
public class WbxmlParser implements XmlPullParser {
    static final String HEX_DIGITS = "0123456789abcdef";
    public static final int WAP_EXTENSION = 64;
    private static final String UNEXPECTED_EOF = "Unexpected EOF";
    private static final String ILLEGAL_TYPE = "Wrong event type";
    private InputStream in;
    private String[] attrStartTable;
    private String[] attrValueTable;
    private String[] tagTable;
    private byte[] stringTable;
    private boolean processNsp;
    private int depth;
    private int attributeCount;
    private int version;
    private int publicIdentifierId;
    private String prefix;
    private String namespace;
    private String name;
    private String text;
    private Object wapExtensionData;
    private int wapCode;
    private int type;
    private boolean degenerated;
    private boolean isWhitespace;
    private String encoding;
    private int TAG_TABLE = 0;
    private int ATTR_START_TABLE = 1;
    private int ATTR_VALUE_TABLE = 2;
    private Hashtable cacheStringTable = null;
    private String[] elementStack = new String[16];
    private String[] nspStack = new String[8];
    private int[] nspCounts = new int[4];
    private String[] attributes = new String[16];
    private int nextId = -2;
    private Vector tables = new Vector();

    @Override // org.xmlpull.v1.XmlPullParser
    public boolean getFeature(String feature) {
        if ("http://xmlpull.org/v1/doc/features.html#process-namespaces".equals(feature)) {
            return this.processNsp;
        }
        return false;
    }

    @Override // org.xmlpull.v1.XmlPullParser
    public String getInputEncoding() {
        return this.encoding;
    }

    @Override // org.xmlpull.v1.XmlPullParser
    public void defineEntityReplacementText(String entity, String value) throws XmlPullParserException {
    }

    @Override // org.xmlpull.v1.XmlPullParser
    public Object getProperty(String property) {
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
        return buf.toString();
    }

    @Override // org.xmlpull.v1.XmlPullParser
    public int getLineNumber() {
        return -1;
    }

    @Override // org.xmlpull.v1.XmlPullParser
    public int getColumnNumber() {
        return -1;
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
        return this.text;
    }

    @Override // org.xmlpull.v1.XmlPullParser
    public char[] getTextCharacters(int[] poslen) {
        if (this.type >= 4) {
            poslen[0] = 0;
            poslen[1] = this.text.length();
            char[] buf = new char[this.text.length()];
            this.text.getChars(0, this.text.length(), buf, 0);
            return buf;
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
        this.isWhitespace = true;
        int minType = 9999;
        while (true) {
            String save = this.text;
            nextImpl();
            if (this.type < minType) {
                minType = this.type;
            }
            if (minType <= 5) {
                if (minType >= 4) {
                    if (save != null) {
                        this.text = this.text == null ? save : save + this.text;
                    }
                    switch (peekId()) {
                        case 2:
                        case 3:
                        case 4:
                        case Wbxml.LITERAL_C /* 68 */:
                        case Wbxml.STR_T /* 131 */:
                        case Wbxml.LITERAL_A /* 132 */:
                        case Wbxml.LITERAL_AC /* 196 */:
                            break;
                    }
                }
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
    public void require(int type, String namespace, String name) throws XmlPullParserException, IOException {
        if (type != this.type || ((namespace != null && !namespace.equals(getNamespace())) || (name != null && !name.equals(getName())))) {
            exception("expected: " + (type == 64 ? "WAP Ext." : TYPES[type] + " {" + namespace + "}" + name));
        }
    }

    @Override // org.xmlpull.v1.XmlPullParser
    public void setInput(Reader reader) throws XmlPullParserException {
        exception("InputStream required");
    }

    @Override // org.xmlpull.v1.XmlPullParser
    public void setInput(InputStream in, String enc) throws XmlPullParserException {
        int cnt;
        this.in = in;
        try {
            this.version = readByte();
            this.publicIdentifierId = readInt();
            if (this.publicIdentifierId == 0) {
                readInt();
            }
            int charset = readInt();
            if (null == enc) {
                switch (charset) {
                    case 4:
                        this.encoding = "ISO-8859-1";
                        break;
                    case 106:
                        this.encoding = Constants.CHARACTER_ENCODING;
                        break;
                    default:
                        throw new UnsupportedEncodingException(PackageDocumentBase.PREFIX_OPF + charset);
                }
            } else {
                this.encoding = enc;
            }
            int strTabSize = readInt();
            this.stringTable = new byte[strTabSize];
            int ok = 0;
            while (ok < strTabSize && (cnt = in.read(this.stringTable, ok, strTabSize - ok)) > 0) {
                ok += cnt;
            }
            selectPage(0, true);
            selectPage(0, false);
        } catch (IOException e) {
            exception("Illegal input format");
        }
    }

    @Override // org.xmlpull.v1.XmlPullParser
    public void setFeature(String feature, boolean value) throws XmlPullParserException {
        if ("http://xmlpull.org/v1/doc/features.html#process-namespaces".equals(feature)) {
            this.processNsp = value;
        } else {
            exception("unsupported feature: " + feature);
        }
    }

    @Override // org.xmlpull.v1.XmlPullParser
    public void setProperty(String property, Object value) throws XmlPullParserException {
        throw new XmlPullParserException("unsupported property: " + property);
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
                    exception("illegal empty namespace");
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
                if (cut2 == 0) {
                    throw new RuntimeException("illegal attribute name: " + attrName3 + " at " + this);
                }
                if (cut2 != -1) {
                    String attrPrefix = attrName3.substring(0, cut2);
                    String attrName4 = attrName3.substring(cut2 + 1);
                    String attrNs = getNamespace(attrPrefix);
                    if (attrNs == null) {
                        throw new RuntimeException("Undefined Prefix: " + attrPrefix + " in " + this);
                    }
                    this.attributes[i5] = attrNs;
                    this.attributes[i5 + 1] = attrPrefix;
                    this.attributes[i5 + 2] = attrName4;
                    for (int j2 = (this.attributeCount << 2) - 4; j2 > i5; j2 -= 4) {
                        if (attrName4.equals(this.attributes[j2 + 2]) && attrNs.equals(this.attributes[j2])) {
                            exception("Duplicate Attribute: {" + attrNs + "}" + attrName4);
                        }
                    }
                }
            }
        }
        int cut3 = this.name.indexOf(58);
        if (cut3 == 0) {
            exception("illegal tag name: " + this.name);
        } else if (cut3 != -1) {
            this.prefix = this.name.substring(0, cut3);
            this.name = this.name.substring(cut3 + 1);
        }
        this.namespace = getNamespace(this.prefix);
        if (this.namespace == null) {
            if (this.prefix != null) {
                exception("undefined prefix: " + this.prefix);
            }
            this.namespace = PackageDocumentBase.PREFIX_OPF;
        }
        return any;
    }

    private final void setTable(int page, int type, String[] table) {
        if (this.stringTable != null) {
            throw new RuntimeException("setXxxTable must be called before setInput!");
        }
        while (this.tables.size() < (3 * page) + 3) {
            this.tables.addElement(null);
        }
        this.tables.setElementAt(table, (page * 3) + type);
    }

    private final void exception(String desc) throws XmlPullParserException {
        throw new XmlPullParserException(desc, this, null);
    }

    private void selectPage(int nr, boolean tags) throws XmlPullParserException {
        if (this.tables.size() == 0 && nr == 0) {
            return;
        }
        if (nr * 3 > this.tables.size()) {
            exception("Code Page " + nr + " undefined!");
        }
        if (tags) {
            this.tagTable = (String[]) this.tables.elementAt((nr * 3) + this.TAG_TABLE);
        } else {
            this.attrStartTable = (String[]) this.tables.elementAt((nr * 3) + this.ATTR_START_TABLE);
            this.attrValueTable = (String[]) this.tables.elementAt((nr * 3) + this.ATTR_VALUE_TABLE);
        }
    }

    private final void nextImpl() throws XmlPullParserException, IOException {
        int id;
        if (this.type == 3) {
            this.depth--;
        }
        if (this.degenerated) {
            this.type = 3;
            this.degenerated = false;
            return;
        }
        this.text = null;
        this.prefix = null;
        this.name = null;
        int iPeekId = peekId();
        while (true) {
            id = iPeekId;
            if (id != 0) {
                break;
            }
            this.nextId = -2;
            selectPage(readByte(), true);
            iPeekId = peekId();
        }
        this.nextId = -2;
        switch (id) {
            case IOUtil.EOF /* -1 */:
                this.type = 1;
                return;
            case 1:
                int sp = (this.depth - 1) << 2;
                this.type = 3;
                this.namespace = this.elementStack[sp];
                this.prefix = this.elementStack[sp + 1];
                this.name = this.elementStack[sp + 2];
                return;
            case 2:
                this.type = 6;
                char c = (char) readInt();
                this.text = PackageDocumentBase.PREFIX_OPF + c;
                this.name = "#" + ((int) c);
                return;
            case 3:
                this.type = 4;
                this.text = readStrI();
                return;
            case 64:
            case Wbxml.EXT_I_1 /* 65 */:
            case Wbxml.EXT_I_2 /* 66 */:
            case Wbxml.EXT_T_0 /* 128 */:
            case Wbxml.EXT_T_1 /* 129 */:
            case Wbxml.EXT_T_2 /* 130 */:
            case Wbxml.EXT_0 /* 192 */:
            case Wbxml.EXT_1 /* 193 */:
            case Wbxml.EXT_2 /* 194 */:
            case Wbxml.OPAQUE /* 195 */:
                this.type = 64;
                this.wapCode = id;
                this.wapExtensionData = parseWapExtension(id);
                return;
            case Wbxml.PI /* 67 */:
                throw new RuntimeException("PI curr. not supp.");
            case Wbxml.STR_T /* 131 */:
                this.type = 4;
                this.text = readStrT();
                return;
            default:
                parseElement(id);
                return;
        }
    }

    public Object parseWapExtension(int id) throws XmlPullParserException, IOException {
        switch (id) {
            case 64:
            case Wbxml.EXT_I_1 /* 65 */:
            case Wbxml.EXT_I_2 /* 66 */:
                return readStrI();
            case Wbxml.EXT_T_0 /* 128 */:
            case Wbxml.EXT_T_1 /* 129 */:
            case Wbxml.EXT_T_2 /* 130 */:
                return new Integer(readInt());
            case Wbxml.EXT_0 /* 192 */:
            case Wbxml.EXT_1 /* 193 */:
            case Wbxml.EXT_2 /* 194 */:
                return null;
            case Wbxml.OPAQUE /* 195 */:
                int count = readInt();
                byte[] buf = new byte[count];
                while (count > 0) {
                    count -= this.in.read(buf, buf.length - count, count);
                }
                return buf;
            default:
                exception("illegal id: " + id);
                return null;
        }
    }

    public void readAttr() throws XmlPullParserException, IOException {
        StringBuffer value;
        int id = readByte();
        int i = 0;
        while (id != 1) {
            while (id == 0) {
                selectPage(readByte(), false);
                id = readByte();
            }
            String name = resolveId(this.attrStartTable, id);
            int cut = name.indexOf(61);
            if (cut == -1) {
                value = new StringBuffer();
            } else {
                value = new StringBuffer(name.substring(cut + 1));
                name = name.substring(0, cut);
            }
            int i2 = readByte();
            while (true) {
                id = i2;
                if (id > 128 || id == 0 || id == 2 || id == 3 || id == 131 || ((id >= 64 && id <= 66) || (id >= 128 && id <= 130))) {
                    switch (id) {
                        case 0:
                            selectPage(readByte(), false);
                            break;
                        case 2:
                            value.append((char) readInt());
                            break;
                        case 3:
                            value.append(readStrI());
                            break;
                        case 64:
                        case Wbxml.EXT_I_1 /* 65 */:
                        case Wbxml.EXT_I_2 /* 66 */:
                        case Wbxml.EXT_T_0 /* 128 */:
                        case Wbxml.EXT_T_1 /* 129 */:
                        case Wbxml.EXT_T_2 /* 130 */:
                        case Wbxml.EXT_0 /* 192 */:
                        case Wbxml.EXT_1 /* 193 */:
                        case Wbxml.EXT_2 /* 194 */:
                        case Wbxml.OPAQUE /* 195 */:
                            value.append(resolveWapExtension(id, parseWapExtension(id)));
                            break;
                        case Wbxml.STR_T /* 131 */:
                            value.append(readStrT());
                            break;
                        default:
                            value.append(resolveId(this.attrValueTable, id));
                            break;
                    }
                    i2 = readByte();
                }
            }
            this.attributes = ensureCapacity(this.attributes, i + 4);
            int i3 = i;
            int i4 = i + 1;
            this.attributes[i3] = PackageDocumentBase.PREFIX_OPF;
            int i5 = i4 + 1;
            this.attributes[i4] = null;
            int i6 = i5 + 1;
            this.attributes[i5] = name;
            i = i6 + 1;
            this.attributes[i6] = value.toString();
            this.attributeCount++;
        }
    }

    private int peekId() throws IOException {
        if (this.nextId == -2) {
            this.nextId = this.in.read();
        }
        return this.nextId;
    }

    protected String resolveWapExtension(int id, Object data) {
        if (data instanceof byte[]) {
            StringBuffer sb = new StringBuffer();
            byte[] b = (byte[]) data;
            for (int i = 0; i < b.length; i++) {
                sb.append(HEX_DIGITS.charAt((b[i] >> 4) & 15));
                sb.append(HEX_DIGITS.charAt(b[i] & 15));
            }
            return sb.toString();
        }
        return "$(" + data + ")";
    }

    String resolveId(String[] tab, int id) throws IOException {
        int idx = (id & 127) - 5;
        if (idx == -1) {
            this.wapCode = -1;
            return readStrT();
        }
        if (idx < 0 || tab == null || idx >= tab.length || tab[idx] == null) {
            throw new IOException("id " + id + " undef.");
        }
        this.wapCode = idx + 5;
        return tab[idx];
    }

    void parseElement(int id) throws XmlPullParserException, IOException {
        this.type = 2;
        this.name = resolveId(this.tagTable, id & 63);
        this.attributeCount = 0;
        if ((id & Wbxml.EXT_T_0) != 0) {
            readAttr();
        }
        this.degenerated = (id & 64) == 0;
        int i = this.depth;
        this.depth = i + 1;
        int sp = i << 2;
        this.elementStack = ensureCapacity(this.elementStack, sp + 4);
        this.elementStack[sp + 3] = this.name;
        if (this.depth >= this.nspCounts.length) {
            int[] bigger = new int[this.depth + 4];
            System.arraycopy(this.nspCounts, 0, bigger, 0, this.nspCounts.length);
            this.nspCounts = bigger;
        }
        this.nspCounts[this.depth] = this.nspCounts[this.depth - 1];
        for (int i2 = this.attributeCount - 1; i2 > 0; i2--) {
            for (int j = 0; j < i2; j++) {
                if (getAttributeName(i2).equals(getAttributeName(j))) {
                    exception("Duplicate Attribute: " + getAttributeName(i2));
                }
            }
        }
        if (this.processNsp) {
            adjustNsp();
        } else {
            this.namespace = PackageDocumentBase.PREFIX_OPF;
        }
        this.elementStack[sp] = this.namespace;
        this.elementStack[sp + 1] = this.prefix;
        this.elementStack[sp + 2] = this.name;
    }

    private final String[] ensureCapacity(String[] arr, int required) {
        if (arr.length >= required) {
            return arr;
        }
        String[] bigger = new String[required + 16];
        System.arraycopy(arr, 0, bigger, 0, arr.length);
        return bigger;
    }

    int readByte() throws IOException {
        int i = this.in.read();
        if (i == -1) {
            throw new IOException(UNEXPECTED_EOF);
        }
        return i;
    }

    int readInt() throws IOException {
        int i;
        int result = 0;
        do {
            i = readByte();
            result = (result << 7) | (i & 127);
        } while ((i & Wbxml.EXT_T_0) != 0);
        return result;
    }

    String readStrI() throws IOException {
        ByteArrayOutputStream buf = new ByteArrayOutputStream();
        boolean wsp = true;
        while (true) {
            int i = this.in.read();
            if (i != 0) {
                if (i == -1) {
                    throw new IOException(UNEXPECTED_EOF);
                }
                if (i > 32) {
                    wsp = false;
                }
                buf.write(i);
            } else {
                this.isWhitespace = wsp;
                String result = new String(buf.toByteArray(), this.encoding);
                buf.close();
                return result;
            }
        }
    }

    String readStrT() throws IOException {
        int pos = readInt();
        if (this.cacheStringTable == null) {
            this.cacheStringTable = new Hashtable();
        }
        String forReturn = (String) this.cacheStringTable.get(new Integer(pos));
        if (forReturn == null) {
            int end = pos;
            while (end < this.stringTable.length && this.stringTable[end] != 0) {
                end++;
            }
            forReturn = new String(this.stringTable, pos, end - pos, this.encoding);
            this.cacheStringTable.put(new Integer(pos), forReturn);
        }
        return forReturn;
    }

    public void setTagTable(int page, String[] table) {
        setTable(page, this.TAG_TABLE, table);
    }

    public void setAttrStartTable(int page, String[] table) {
        setTable(page, this.ATTR_START_TABLE, table);
    }

    public void setAttrValueTable(int page, String[] table) {
        setTable(page, this.ATTR_VALUE_TABLE, table);
    }

    public int getWapCode() {
        return this.wapCode;
    }

    public Object getWapExtensionData() {
        return this.wapExtensionData;
    }
}
