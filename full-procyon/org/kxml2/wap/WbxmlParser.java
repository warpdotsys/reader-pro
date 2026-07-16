// 
// Decompiled by Procyon v0.6.0
// 

package org.kxml2.wap;

import java.io.ByteArrayOutputStream;
import java.io.UnsupportedEncodingException;
import java.io.Reader;
import java.io.IOException;
import org.xmlpull.v1.XmlPullParserException;
import java.util.Vector;
import java.util.Hashtable;
import java.io.InputStream;
import org.xmlpull.v1.XmlPullParser;

public class WbxmlParser implements XmlPullParser
{
    static final String HEX_DIGITS = "0123456789abcdef";
    public static final int WAP_EXTENSION = 64;
    private static final String UNEXPECTED_EOF = "Unexpected EOF";
    private static final String ILLEGAL_TYPE = "Wrong event type";
    private InputStream in;
    private int TAG_TABLE;
    private int ATTR_START_TABLE;
    private int ATTR_VALUE_TABLE;
    private String[] attrStartTable;
    private String[] attrValueTable;
    private String[] tagTable;
    private byte[] stringTable;
    private Hashtable cacheStringTable;
    private boolean processNsp;
    private int depth;
    private String[] elementStack;
    private String[] nspStack;
    private int[] nspCounts;
    private int attributeCount;
    private String[] attributes;
    private int nextId;
    private Vector tables;
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
    
    public WbxmlParser() {
        this.TAG_TABLE = 0;
        this.ATTR_START_TABLE = 1;
        this.ATTR_VALUE_TABLE = 2;
        this.cacheStringTable = null;
        this.elementStack = new String[16];
        this.nspStack = new String[8];
        this.nspCounts = new int[4];
        this.attributes = new String[16];
        this.nextId = -2;
        this.tables = new Vector();
    }
    
    public boolean getFeature(final String feature) {
        return "http://xmlpull.org/v1/doc/features.html#process-namespaces".equals(feature) && this.processNsp;
    }
    
    public String getInputEncoding() {
        return this.encoding;
    }
    
    public void defineEntityReplacementText(final String entity, final String value) throws XmlPullParserException {
    }
    
    public Object getProperty(final String property) {
        return null;
    }
    
    public int getNamespaceCount(final int depth) {
        if (depth > this.depth) {
            throw new IndexOutOfBoundsException();
        }
        return this.nspCounts[depth];
    }
    
    public String getNamespacePrefix(final int pos) {
        return this.nspStack[pos << 1];
    }
    
    public String getNamespaceUri(final int pos) {
        return this.nspStack[(pos << 1) + 1];
    }
    
    public String getNamespace(final String prefix) {
        if ("xml".equals(prefix)) {
            return "http://www.w3.org/XML/1998/namespace";
        }
        if ("xmlns".equals(prefix)) {
            return "http://www.w3.org/2000/xmlns/";
        }
        for (int i = (this.getNamespaceCount(this.depth) << 1) - 2; i >= 0; i -= 2) {
            if (prefix == null) {
                if (this.nspStack[i] == null) {
                    return this.nspStack[i + 1];
                }
            }
            else if (prefix.equals(this.nspStack[i])) {
                return this.nspStack[i + 1];
            }
        }
        return null;
    }
    
    public int getDepth() {
        return this.depth;
    }
    
    public String getPositionDescription() {
        final StringBuffer buf = new StringBuffer((this.type < WbxmlParser.TYPES.length) ? WbxmlParser.TYPES[this.type] : "unknown");
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
            for (int cnt = this.attributeCount << 2, i = 0; i < cnt; i += 4) {
                buf.append(' ');
                if (this.attributes[i + 1] != null) {
                    buf.append("{" + this.attributes[i] + "}" + this.attributes[i + 1] + ":");
                }
                buf.append(this.attributes[i + 2] + "='" + this.attributes[i + 3] + "'");
            }
            buf.append('>');
        }
        else if (this.type != 7) {
            if (this.type != 4) {
                buf.append(this.getText());
            }
            else if (this.isWhitespace) {
                buf.append("(whitespace)");
            }
            else {
                String text = this.getText();
                if (text.length() > 16) {
                    text = text.substring(0, 16) + "...";
                }
                buf.append(text);
            }
        }
        return buf.toString();
    }
    
    public int getLineNumber() {
        return -1;
    }
    
    public int getColumnNumber() {
        return -1;
    }
    
    public boolean isWhitespace() throws XmlPullParserException {
        if (this.type != 4 && this.type != 7 && this.type != 5) {
            this.exception("Wrong event type");
        }
        return this.isWhitespace;
    }
    
    public String getText() {
        return this.text;
    }
    
    public char[] getTextCharacters(final int[] poslen) {
        if (this.type >= 4) {
            poslen[0] = 0;
            poslen[1] = this.text.length();
            final char[] buf = new char[this.text.length()];
            this.text.getChars(0, this.text.length(), buf, 0);
            return buf;
        }
        poslen[1] = (poslen[0] = -1);
        return null;
    }
    
    public String getNamespace() {
        return this.namespace;
    }
    
    public String getName() {
        return this.name;
    }
    
    public String getPrefix() {
        return this.prefix;
    }
    
    public boolean isEmptyElementTag() throws XmlPullParserException {
        if (this.type != 2) {
            this.exception("Wrong event type");
        }
        return this.degenerated;
    }
    
    public int getAttributeCount() {
        return this.attributeCount;
    }
    
    public String getAttributeType(final int index) {
        return "CDATA";
    }
    
    public boolean isAttributeDefault(final int index) {
        return false;
    }
    
    public String getAttributeNamespace(final int index) {
        if (index >= this.attributeCount) {
            throw new IndexOutOfBoundsException();
        }
        return this.attributes[index << 2];
    }
    
    public String getAttributeName(final int index) {
        if (index >= this.attributeCount) {
            throw new IndexOutOfBoundsException();
        }
        return this.attributes[(index << 2) + 2];
    }
    
    public String getAttributePrefix(final int index) {
        if (index >= this.attributeCount) {
            throw new IndexOutOfBoundsException();
        }
        return this.attributes[(index << 2) + 1];
    }
    
    public String getAttributeValue(final int index) {
        if (index >= this.attributeCount) {
            throw new IndexOutOfBoundsException();
        }
        return this.attributes[(index << 2) + 3];
    }
    
    public String getAttributeValue(final String namespace, final String name) {
        for (int i = (this.attributeCount << 2) - 4; i >= 0; i -= 4) {
            if (this.attributes[i + 2].equals(name) && (namespace == null || this.attributes[i].equals(namespace))) {
                return this.attributes[i + 3];
            }
        }
        return null;
    }
    
    public int getEventType() throws XmlPullParserException {
        return this.type;
    }
    
    public int next() throws XmlPullParserException, IOException {
        this.isWhitespace = true;
        int minType = 9999;
    Label_0162:
        while (true) {
            final String save = this.text;
            this.nextImpl();
            if (this.type < minType) {
                minType = this.type;
            }
            if (minType > 5) {
                continue;
            }
            if (minType < 4) {
                break;
            }
            if (save != null) {
                this.text = ((this.text == null) ? save : (save + this.text));
            }
            switch (this.peekId()) {
                case 2:
                case 3:
                case 4:
                case 68:
                case 131:
                case 132:
                case 196: {
                    continue;
                }
                default: {
                    break Label_0162;
                }
            }
        }
        this.type = minType;
        if (this.type > 4) {
            this.type = 4;
        }
        return this.type;
    }
    
    public int nextToken() throws XmlPullParserException, IOException {
        this.isWhitespace = true;
        this.nextImpl();
        return this.type;
    }
    
    public int nextTag() throws XmlPullParserException, IOException {
        this.next();
        if (this.type == 4 && this.isWhitespace) {
            this.next();
        }
        if (this.type != 3 && this.type != 2) {
            this.exception("unexpected type");
        }
        return this.type;
    }
    
    public String nextText() throws XmlPullParserException, IOException {
        if (this.type != 2) {
            this.exception("precondition: START_TAG");
        }
        this.next();
        String result;
        if (this.type == 4) {
            result = this.getText();
            this.next();
        }
        else {
            result = "";
        }
        if (this.type != 3) {
            this.exception("END_TAG expected");
        }
        return result;
    }
    
    public void require(final int type, final String namespace, final String name) throws XmlPullParserException, IOException {
        if (type != this.type || (namespace != null && !namespace.equals(this.getNamespace())) || (name != null && !name.equals(this.getName()))) {
            this.exception("expected: " + ((type == 64) ? "WAP Ext." : (WbxmlParser.TYPES[type] + " {" + namespace + "}" + name)));
        }
    }
    
    public void setInput(final Reader reader) throws XmlPullParserException {
        this.exception("InputStream required");
    }
    
    public void setInput(final InputStream in, final String enc) throws XmlPullParserException {
        this.in = in;
        try {
            this.version = this.readByte();
            this.publicIdentifierId = this.readInt();
            if (this.publicIdentifierId == 0) {
                this.readInt();
            }
            final int charset = this.readInt();
            if (null == enc) {
                switch (charset) {
                    case 4: {
                        this.encoding = "ISO-8859-1";
                        break;
                    }
                    case 106: {
                        this.encoding = "UTF-8";
                        break;
                    }
                    default: {
                        throw new UnsupportedEncodingException("" + charset);
                    }
                }
            }
            else {
                this.encoding = enc;
            }
            final int strTabSize = this.readInt();
            this.stringTable = new byte[strTabSize];
            int cnt;
            for (int ok = 0; ok < strTabSize; ok += cnt) {
                cnt = in.read(this.stringTable, ok, strTabSize - ok);
                if (cnt <= 0) {
                    break;
                }
            }
            this.selectPage(0, true);
            this.selectPage(0, false);
        }
        catch (final IOException e) {
            this.exception("Illegal input format");
        }
    }
    
    public void setFeature(final String feature, final boolean value) throws XmlPullParserException {
        if ("http://xmlpull.org/v1/doc/features.html#process-namespaces".equals(feature)) {
            this.processNsp = value;
        }
        else {
            this.exception("unsupported feature: " + feature);
        }
    }
    
    public void setProperty(final String property, final Object value) throws XmlPullParserException {
        throw new XmlPullParserException("unsupported property: " + property);
    }
    
    private final boolean adjustNsp() throws XmlPullParserException {
        boolean any = false;
        for (int i = 0; i < this.attributeCount << 2; i += 4) {
            String attrName = this.attributes[i + 2];
            final int cut = attrName.indexOf(58);
            String prefix;
            if (cut != -1) {
                prefix = attrName.substring(0, cut);
                attrName = attrName.substring(cut + 1);
            }
            else {
                if (!attrName.equals("xmlns")) {
                    continue;
                }
                prefix = attrName;
                attrName = null;
            }
            if (!prefix.equals("xmlns")) {
                any = true;
            }
            else {
                final int j = this.nspCounts[this.depth]++ << 1;
                (this.nspStack = this.ensureCapacity(this.nspStack, j + 2))[j] = attrName;
                this.nspStack[j + 1] = this.attributes[i + 3];
                if (attrName != null && this.attributes[i + 3].equals("")) {
                    this.exception("illegal empty namespace");
                }
                System.arraycopy(this.attributes, i + 4, this.attributes, i, (--this.attributeCount << 2) - i);
                i -= 4;
            }
        }
        if (any) {
            for (int i = (this.attributeCount << 2) - 4; i >= 0; i -= 4) {
                String attrName = this.attributes[i + 2];
                final int cut = attrName.indexOf(58);
                if (cut == 0) {
                    throw new RuntimeException("illegal attribute name: " + attrName + " at " + this);
                }
                if (cut != -1) {
                    final String attrPrefix = attrName.substring(0, cut);
                    attrName = attrName.substring(cut + 1);
                    final String attrNs = this.getNamespace(attrPrefix);
                    if (attrNs == null) {
                        throw new RuntimeException("Undefined Prefix: " + attrPrefix + " in " + this);
                    }
                    this.attributes[i] = attrNs;
                    this.attributes[i + 1] = attrPrefix;
                    this.attributes[i + 2] = attrName;
                    for (int k = (this.attributeCount << 2) - 4; k > i; k -= 4) {
                        if (attrName.equals(this.attributes[k + 2]) && attrNs.equals(this.attributes[k])) {
                            this.exception("Duplicate Attribute: {" + attrNs + "}" + attrName);
                        }
                    }
                }
            }
        }
        final int cut2 = this.name.indexOf(58);
        if (cut2 == 0) {
            this.exception("illegal tag name: " + this.name);
        }
        else if (cut2 != -1) {
            this.prefix = this.name.substring(0, cut2);
            this.name = this.name.substring(cut2 + 1);
        }
        this.namespace = this.getNamespace(this.prefix);
        if (this.namespace == null) {
            if (this.prefix != null) {
                this.exception("undefined prefix: " + this.prefix);
            }
            this.namespace = "";
        }
        return any;
    }
    
    private final void setTable(final int page, final int type, final String[] table) {
        if (this.stringTable != null) {
            throw new RuntimeException("setXxxTable must be called before setInput!");
        }
        while (this.tables.size() < 3 * page + 3) {
            this.tables.addElement(null);
        }
        this.tables.setElementAt(table, page * 3 + type);
    }
    
    private final void exception(final String desc) throws XmlPullParserException {
        throw new XmlPullParserException(desc, (XmlPullParser)this, (Throwable)null);
    }
    
    private void selectPage(final int nr, final boolean tags) throws XmlPullParserException {
        if (this.tables.size() == 0 && nr == 0) {
            return;
        }
        if (nr * 3 > this.tables.size()) {
            this.exception("Code Page " + nr + " undefined!");
        }
        if (tags) {
            this.tagTable = this.tables.elementAt(nr * 3 + this.TAG_TABLE);
        }
        else {
            this.attrStartTable = this.tables.elementAt(nr * 3 + this.ATTR_START_TABLE);
            this.attrValueTable = this.tables.elementAt(nr * 3 + this.ATTR_VALUE_TABLE);
        }
    }
    
    private final void nextImpl() throws IOException, XmlPullParserException {
        if (this.type == 3) {
            --this.depth;
        }
        if (this.degenerated) {
            this.type = 3;
            this.degenerated = false;
            return;
        }
        this.text = null;
        this.prefix = null;
        this.name = null;
        int id;
        for (id = this.peekId(); id == 0; id = this.peekId()) {
            this.nextId = -2;
            this.selectPage(this.readByte(), true);
        }
        this.nextId = -2;
        switch (id) {
            case -1: {
                this.type = 1;
                break;
            }
            case 1: {
                final int sp = this.depth - 1 << 2;
                this.type = 3;
                this.namespace = this.elementStack[sp];
                this.prefix = this.elementStack[sp + 1];
                this.name = this.elementStack[sp + 2];
                break;
            }
            case 2: {
                this.type = 6;
                final char c = (char)this.readInt();
                this.text = "" + c;
                this.name = "#" + (int)c;
                break;
            }
            case 3: {
                this.type = 4;
                this.text = this.readStrI();
                break;
            }
            case 64:
            case 65:
            case 66:
            case 128:
            case 129:
            case 130:
            case 192:
            case 193:
            case 194:
            case 195: {
                this.type = 64;
                this.wapCode = id;
                this.wapExtensionData = this.parseWapExtension(id);
                break;
            }
            case 67: {
                throw new RuntimeException("PI curr. not supp.");
            }
            case 131: {
                this.type = 4;
                this.text = this.readStrT();
                break;
            }
            default: {
                this.parseElement(id);
                break;
            }
        }
    }
    
    public Object parseWapExtension(final int id) throws IOException, XmlPullParserException {
        switch (id) {
            case 64:
            case 65:
            case 66: {
                return this.readStrI();
            }
            case 128:
            case 129:
            case 130: {
                return new Integer(this.readInt());
            }
            case 192:
            case 193:
            case 194: {
                return null;
            }
            case 195: {
                int count;
                byte[] buf;
                for (count = this.readInt(), buf = new byte[count]; count > 0; count -= this.in.read(buf, buf.length - count, count)) {}
                return buf;
            }
            default: {
                this.exception("illegal id: " + id);
                return null;
            }
        }
    }
    
    public void readAttr() throws IOException, XmlPullParserException {
        int id = this.readByte();
        int i = 0;
        while (id != 1) {
            while (id == 0) {
                this.selectPage(this.readByte(), false);
                id = this.readByte();
            }
            String name = this.resolveId(this.attrStartTable, id);
            final int cut = name.indexOf(61);
            StringBuffer value;
            if (cut == -1) {
                value = new StringBuffer();
            }
            else {
                value = new StringBuffer(name.substring(cut + 1));
                name = name.substring(0, cut);
            }
            for (id = this.readByte(); id > 128 || id == 0 || id == 2 || id == 3 || id == 131 || (id >= 64 && id <= 66) || (id >= 128 && id <= 130); id = this.readByte()) {
                switch (id) {
                    case 0: {
                        this.selectPage(this.readByte(), false);
                        break;
                    }
                    case 2: {
                        value.append((char)this.readInt());
                        break;
                    }
                    case 3: {
                        value.append(this.readStrI());
                        break;
                    }
                    case 64:
                    case 65:
                    case 66:
                    case 128:
                    case 129:
                    case 130:
                    case 192:
                    case 193:
                    case 194:
                    case 195: {
                        value.append(this.resolveWapExtension(id, this.parseWapExtension(id)));
                        break;
                    }
                    case 131: {
                        value.append(this.readStrT());
                        break;
                    }
                    default: {
                        value.append(this.resolveId(this.attrValueTable, id));
                        break;
                    }
                }
            }
            (this.attributes = this.ensureCapacity(this.attributes, i + 4))[i++] = "";
            this.attributes[i++] = null;
            this.attributes[i++] = name;
            this.attributes[i++] = value.toString();
            ++this.attributeCount;
        }
    }
    
    private int peekId() throws IOException {
        if (this.nextId == -2) {
            this.nextId = this.in.read();
        }
        return this.nextId;
    }
    
    protected String resolveWapExtension(final int id, final Object data) {
        if (data instanceof byte[]) {
            final StringBuffer sb = new StringBuffer();
            final byte[] b = (byte[])data;
            for (int i = 0; i < b.length; ++i) {
                sb.append("0123456789abcdef".charAt(b[i] >> 4 & 0xF));
                sb.append("0123456789abcdef".charAt(b[i] & 0xF));
            }
            return sb.toString();
        }
        return "$(" + data + ")";
    }
    
    String resolveId(final String[] tab, final int id) throws IOException {
        final int idx = (id & 0x7F) - 5;
        if (idx == -1) {
            this.wapCode = -1;
            return this.readStrT();
        }
        if (idx < 0 || tab == null || idx >= tab.length || tab[idx] == null) {
            throw new IOException("id " + id + " undef.");
        }
        this.wapCode = idx + 5;
        return tab[idx];
    }
    
    void parseElement(final int id) throws IOException, XmlPullParserException {
        this.type = 2;
        this.name = this.resolveId(this.tagTable, id & 0x3F);
        this.attributeCount = 0;
        if ((id & 0x80) != 0x0) {
            this.readAttr();
        }
        this.degenerated = ((id & 0x40) == 0x0);
        final int sp = this.depth++ << 2;
        (this.elementStack = this.ensureCapacity(this.elementStack, sp + 4))[sp + 3] = this.name;
        if (this.depth >= this.nspCounts.length) {
            final int[] bigger = new int[this.depth + 4];
            System.arraycopy(this.nspCounts, 0, bigger, 0, this.nspCounts.length);
            this.nspCounts = bigger;
        }
        this.nspCounts[this.depth] = this.nspCounts[this.depth - 1];
        for (int i = this.attributeCount - 1; i > 0; --i) {
            for (int j = 0; j < i; ++j) {
                if (this.getAttributeName(i).equals(this.getAttributeName(j))) {
                    this.exception("Duplicate Attribute: " + this.getAttributeName(i));
                }
            }
        }
        if (this.processNsp) {
            this.adjustNsp();
        }
        else {
            this.namespace = "";
        }
        this.elementStack[sp] = this.namespace;
        this.elementStack[sp + 1] = this.prefix;
        this.elementStack[sp + 2] = this.name;
    }
    
    private final String[] ensureCapacity(final String[] arr, final int required) {
        if (arr.length >= required) {
            return arr;
        }
        final String[] bigger = new String[required + 16];
        System.arraycopy(arr, 0, bigger, 0, arr.length);
        return bigger;
    }
    
    int readByte() throws IOException {
        final int i = this.in.read();
        if (i == -1) {
            throw new IOException("Unexpected EOF");
        }
        return i;
    }
    
    int readInt() throws IOException {
        int result = 0;
        int i;
        do {
            i = this.readByte();
            result = (result << 7 | (i & 0x7F));
        } while ((i & 0x80) != 0x0);
        return result;
    }
    
    String readStrI() throws IOException {
        final ByteArrayOutputStream buf = new ByteArrayOutputStream();
        boolean wsp = true;
        while (true) {
            final int i = this.in.read();
            if (i == 0) {
                this.isWhitespace = wsp;
                final String result = new String(buf.toByteArray(), this.encoding);
                buf.close();
                return result;
            }
            if (i == -1) {
                throw new IOException("Unexpected EOF");
            }
            if (i > 32) {
                wsp = false;
            }
            buf.write(i);
        }
    }
    
    String readStrT() throws IOException {
        final int pos = this.readInt();
        if (this.cacheStringTable == null) {
            this.cacheStringTable = new Hashtable();
        }
        String forReturn = this.cacheStringTable.get(new Integer(pos));
        if (forReturn == null) {
            int end;
            for (end = pos; end < this.stringTable.length && this.stringTable[end] != 0; ++end) {}
            forReturn = new String(this.stringTable, pos, end - pos, this.encoding);
            this.cacheStringTable.put(new Integer(pos), forReturn);
        }
        return forReturn;
    }
    
    public void setTagTable(final int page, final String[] table) {
        this.setTable(page, this.TAG_TABLE, table);
    }
    
    public void setAttrStartTable(final int page, final String[] table) {
        this.setTable(page, this.ATTR_START_TABLE, table);
    }
    
    public void setAttrValueTable(final int page, final String[] table) {
        this.setTable(page, this.ATTR_VALUE_TABLE, table);
    }
    
    public int getWapCode() {
        return this.wapCode;
    }
    
    public Object getWapExtensionData() {
        return this.wapExtensionData;
    }
}
