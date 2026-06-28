package org.kxml2.wap;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.io.Writer;
import java.util.Hashtable;
import java.util.Vector;
import me.ag2s.epublib.Constants;
import me.ag2s.epublib.epub.PackageDocumentBase;
import org.xmlpull.v1.XmlSerializer;

/* JADX INFO: loaded from: app-classes.jar:org/kxml2/wap/WbxmlSerializer.class */
public class WbxmlSerializer implements XmlSerializer {
    OutputStream out;
    String pending;
    int depth;
    String name;
    String namespace;
    private int attrPage;
    private int tagPage;
    private String encoding;
    Hashtable stringTable = new Hashtable();
    ByteArrayOutputStream buf = new ByteArrayOutputStream();
    ByteArrayOutputStream stringTableBuf = new ByteArrayOutputStream();
    Vector attributes = new Vector();
    Hashtable attrStartTable = new Hashtable();
    Hashtable attrValueTable = new Hashtable();
    Hashtable tagTable = new Hashtable();
    private boolean headerSent = false;

    @Override // org.xmlpull.v1.XmlSerializer
    public XmlSerializer attribute(String namespace, String name, String value) {
        this.attributes.addElement(name);
        this.attributes.addElement(value);
        return this;
    }

    @Override // org.xmlpull.v1.XmlSerializer
    public void cdsect(String cdsect) throws IOException {
        text(cdsect);
    }

    @Override // org.xmlpull.v1.XmlSerializer
    public void comment(String comment) {
    }

    @Override // org.xmlpull.v1.XmlSerializer
    public void docdecl(String docdecl) {
        throw new RuntimeException("Cannot write docdecl for WBXML");
    }

    @Override // org.xmlpull.v1.XmlSerializer
    public void entityRef(String er) {
        throw new RuntimeException("EntityReference not supported for WBXML");
    }

    @Override // org.xmlpull.v1.XmlSerializer
    public int getDepth() {
        return this.depth;
    }

    @Override // org.xmlpull.v1.XmlSerializer
    public boolean getFeature(String name) {
        return false;
    }

    @Override // org.xmlpull.v1.XmlSerializer
    public String getNamespace() {
        return null;
    }

    @Override // org.xmlpull.v1.XmlSerializer
    public String getName() {
        return this.pending;
    }

    @Override // org.xmlpull.v1.XmlSerializer
    public String getPrefix(String nsp, boolean create) {
        throw new RuntimeException("NYI");
    }

    @Override // org.xmlpull.v1.XmlSerializer
    public Object getProperty(String name) {
        return null;
    }

    @Override // org.xmlpull.v1.XmlSerializer
    public void ignorableWhitespace(String sp) {
    }

    @Override // org.xmlpull.v1.XmlSerializer
    public void endDocument() throws IOException {
        flush();
    }

    @Override // org.xmlpull.v1.XmlSerializer
    public void flush() throws IOException {
        checkPending(false);
        if (!this.headerSent) {
            writeInt(this.out, this.stringTableBuf.size());
            this.out.write(this.stringTableBuf.toByteArray());
            this.headerSent = true;
        }
        this.out.write(this.buf.toByteArray());
        this.buf.reset();
    }

    public void checkPending(boolean degenerated) throws IOException {
        int i;
        int i2;
        if (this.pending == null) {
            return;
        }
        int len = this.attributes.size();
        int[] idx = (int[]) this.tagTable.get(this.pending);
        if (idx == null) {
            ByteArrayOutputStream byteArrayOutputStream = this.buf;
            if (len == 0) {
                i2 = degenerated ? 4 : 68;
            } else {
                i2 = degenerated ? Wbxml.LITERAL_A : Wbxml.LITERAL_AC;
            }
            byteArrayOutputStream.write(i2);
            writeStrT(this.pending, false);
        } else {
            if (idx[0] != this.tagPage) {
                this.tagPage = idx[0];
                this.buf.write(0);
                this.buf.write(this.tagPage);
            }
            ByteArrayOutputStream byteArrayOutputStream2 = this.buf;
            if (len == 0) {
                i = degenerated ? idx[1] : idx[1] | 64;
            } else {
                i = degenerated ? idx[1] | Wbxml.EXT_T_0 : idx[1] | Wbxml.EXT_0;
            }
            byteArrayOutputStream2.write(i);
        }
        int i3 = 0;
        while (i3 < len) {
            int[] idx2 = (int[]) this.attrStartTable.get(this.attributes.elementAt(i3));
            if (idx2 == null) {
                this.buf.write(4);
                writeStrT((String) this.attributes.elementAt(i3), false);
            } else {
                if (idx2[0] != this.attrPage) {
                    this.attrPage = idx2[0];
                    this.buf.write(0);
                    this.buf.write(this.attrPage);
                }
                this.buf.write(idx2[1]);
            }
            int i4 = i3 + 1;
            int[] idx3 = (int[]) this.attrValueTable.get(this.attributes.elementAt(i4));
            if (idx3 == null) {
                writeStr((String) this.attributes.elementAt(i4));
            } else {
                if (idx3[0] != this.attrPage) {
                    this.attrPage = idx3[0];
                    this.buf.write(0);
                    this.buf.write(this.attrPage);
                }
                this.buf.write(idx3[1]);
            }
            i3 = i4 + 1;
        }
        if (len > 0) {
            this.buf.write(1);
        }
        this.pending = null;
        this.attributes.removeAllElements();
    }

    @Override // org.xmlpull.v1.XmlSerializer
    public void processingInstruction(String pi) {
        throw new RuntimeException("PI NYI");
    }

    @Override // org.xmlpull.v1.XmlSerializer
    public void setFeature(String name, boolean value) {
        throw new IllegalArgumentException("unknown feature " + name);
    }

    @Override // org.xmlpull.v1.XmlSerializer
    public void setOutput(Writer writer) {
        throw new RuntimeException("Wbxml requires an OutputStream!");
    }

    @Override // org.xmlpull.v1.XmlSerializer
    public void setOutput(OutputStream out, String encoding) throws IOException {
        this.encoding = encoding == null ? Constants.CHARACTER_ENCODING : encoding;
        this.out = out;
        this.buf = new ByteArrayOutputStream();
        this.stringTableBuf = new ByteArrayOutputStream();
        this.headerSent = false;
    }

    @Override // org.xmlpull.v1.XmlSerializer
    public void setPrefix(String prefix, String nsp) {
        throw new RuntimeException("NYI");
    }

    @Override // org.xmlpull.v1.XmlSerializer
    public void setProperty(String property, Object value) {
        throw new IllegalArgumentException("unknown property " + property);
    }

    @Override // org.xmlpull.v1.XmlSerializer
    public void startDocument(String encoding, Boolean standalone) throws IOException {
        this.out.write(3);
        this.out.write(1);
        if (encoding != null) {
            this.encoding = encoding;
        }
        if (this.encoding.toUpperCase().equals(Constants.CHARACTER_ENCODING)) {
            this.out.write(106);
        } else {
            if (this.encoding.toUpperCase().equals("ISO-8859-1")) {
                this.out.write(4);
                return;
            }
            throw new UnsupportedEncodingException(encoding);
        }
    }

    @Override // org.xmlpull.v1.XmlSerializer
    public XmlSerializer startTag(String namespace, String name) throws IOException {
        if (namespace != null && !PackageDocumentBase.PREFIX_OPF.equals(namespace)) {
            throw new RuntimeException("NSP NYI");
        }
        checkPending(false);
        this.pending = name;
        this.depth++;
        return this;
    }

    @Override // org.xmlpull.v1.XmlSerializer
    public XmlSerializer text(char[] chars, int start, int len) throws IOException {
        checkPending(false);
        writeStr(new String(chars, start, len));
        return this;
    }

    @Override // org.xmlpull.v1.XmlSerializer
    public XmlSerializer text(String text) throws IOException {
        checkPending(false);
        writeStr(text);
        return this;
    }

    private void writeStr(String text) throws IOException {
        int p0 = 0;
        int lastCut = 0;
        int len = text.length();
        if (this.headerSent) {
            writeStrI(this.buf, text);
            return;
        }
        while (p0 < len) {
            while (p0 < len && text.charAt(p0) < 'A') {
                p0++;
            }
            int p1 = p0;
            while (p1 < len && text.charAt(p1) >= 'A') {
                p1++;
            }
            if (p1 - p0 > 10) {
                if (p0 > lastCut && text.charAt(p0 - 1) == ' ' && this.stringTable.get(text.substring(p0, p1)) == null) {
                    this.buf.write(Wbxml.STR_T);
                    writeStrT(text.substring(lastCut, p1), false);
                } else {
                    if (p0 > lastCut && text.charAt(p0 - 1) == ' ') {
                        p0--;
                    }
                    if (p0 > lastCut) {
                        this.buf.write(Wbxml.STR_T);
                        writeStrT(text.substring(lastCut, p0), false);
                    }
                    this.buf.write(Wbxml.STR_T);
                    writeStrT(text.substring(p0, p1), true);
                }
                lastCut = p1;
            }
            p0 = p1;
        }
        if (lastCut < len) {
            this.buf.write(Wbxml.STR_T);
            writeStrT(text.substring(lastCut, len), false);
        }
    }

    @Override // org.xmlpull.v1.XmlSerializer
    public XmlSerializer endTag(String namespace, String name) throws IOException {
        if (this.pending != null) {
            checkPending(true);
        } else {
            this.buf.write(1);
        }
        this.depth--;
        return this;
    }

    public void writeWapExtension(int type, Object data) throws IOException {
        checkPending(false);
        this.buf.write(type);
        switch (type) {
            case 64:
            case Wbxml.EXT_I_1 /* 65 */:
            case Wbxml.EXT_I_2 /* 66 */:
                writeStrI(this.buf, (String) data);
                return;
            case Wbxml.EXT_T_0 /* 128 */:
            case Wbxml.EXT_T_1 /* 129 */:
            case Wbxml.EXT_T_2 /* 130 */:
                writeStrT((String) data, false);
                return;
            case Wbxml.EXT_0 /* 192 */:
            case Wbxml.EXT_1 /* 193 */:
            case Wbxml.EXT_2 /* 194 */:
                return;
            case Wbxml.OPAQUE /* 195 */:
                byte[] bytes = (byte[]) data;
                writeInt(this.buf, bytes.length);
                this.buf.write(bytes);
                return;
            default:
                throw new IllegalArgumentException();
        }
    }

    static void writeInt(OutputStream out, int i) throws IOException {
        byte[] buf = new byte[5];
        int idx = 0;
        do {
            int i2 = idx;
            idx++;
            buf[i2] = (byte) (i & 127);
            i >>= 7;
        } while (i != 0);
        while (idx > 1) {
            idx--;
            out.write(buf[idx] | 128);
        }
        out.write(buf[0]);
    }

    void writeStrI(OutputStream out, String s) throws IOException {
        byte[] data = s.getBytes(this.encoding);
        out.write(data);
        out.write(0);
    }

    private final void writeStrT(String s, boolean mayPrependSpace) throws IOException {
        int iIntValue;
        Integer idx = (Integer) this.stringTable.get(s);
        ByteArrayOutputStream byteArrayOutputStream = this.buf;
        if (idx == null) {
            iIntValue = addToStringTable(s, mayPrependSpace);
        } else {
            iIntValue = idx.intValue();
        }
        writeInt(byteArrayOutputStream, iIntValue);
    }

    public int addToStringTable(String s, boolean mayPrependSpace) throws IOException {
        if (this.headerSent) {
            throw new IOException("stringtable sent");
        }
        int i = this.stringTableBuf.size();
        int offset = i;
        if (s.charAt(0) >= '0' && mayPrependSpace) {
            s = ' ' + s;
            offset++;
        }
        this.stringTable.put(s, new Integer(i));
        if (s.charAt(0) == ' ') {
            this.stringTable.put(s.substring(1), new Integer(i + 1));
        }
        int j = s.lastIndexOf(32);
        if (j > 1) {
            String t = s.substring(j);
            int k = t.getBytes("utf-8").length;
            this.stringTable.put(t, new Integer(i + k));
            this.stringTable.put(s.substring(j + 1), new Integer(i + k + 1));
        }
        writeStrI(this.stringTableBuf, s);
        this.stringTableBuf.flush();
        return offset;
    }

    public void setTagTable(int page, String[] tagTable) {
        for (int i = 0; i < tagTable.length; i++) {
            if (tagTable[i] != null) {
                this.tagTable.put(tagTable[i], new int[]{page, i + 5});
            }
        }
    }

    public void setAttrStartTable(int page, String[] attrStartTable) {
        for (int i = 0; i < attrStartTable.length; i++) {
            if (attrStartTable[i] != null) {
                this.attrStartTable.put(attrStartTable[i], new int[]{page, i + 5});
            }
        }
    }

    public void setAttrValueTable(int page, String[] attrValueTable) {
        for (int i = 0; i < attrValueTable.length; i++) {
            if (attrValueTable[i] != null) {
                this.attrValueTable.put(attrValueTable[i], new int[]{page, i + 133});
            }
        }
    }
}
