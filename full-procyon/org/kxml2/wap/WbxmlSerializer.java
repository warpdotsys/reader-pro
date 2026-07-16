// 
// Decompiled by Procyon v0.6.0
// 

package org.kxml2.wap;

import java.io.UnsupportedEncodingException;
import java.io.Writer;
import java.io.IOException;
import java.util.Vector;
import java.io.ByteArrayOutputStream;
import java.io.OutputStream;
import java.util.Hashtable;
import org.xmlpull.v1.XmlSerializer;

public class WbxmlSerializer implements XmlSerializer
{
    Hashtable stringTable;
    OutputStream out;
    ByteArrayOutputStream buf;
    ByteArrayOutputStream stringTableBuf;
    String pending;
    int depth;
    String name;
    String namespace;
    Vector attributes;
    Hashtable attrStartTable;
    Hashtable attrValueTable;
    Hashtable tagTable;
    private int attrPage;
    private int tagPage;
    private String encoding;
    private boolean headerSent;
    
    public WbxmlSerializer() {
        this.stringTable = new Hashtable();
        this.buf = new ByteArrayOutputStream();
        this.stringTableBuf = new ByteArrayOutputStream();
        this.attributes = new Vector();
        this.attrStartTable = new Hashtable();
        this.attrValueTable = new Hashtable();
        this.tagTable = new Hashtable();
        this.headerSent = false;
    }
    
    public XmlSerializer attribute(final String namespace, final String name, final String value) {
        this.attributes.addElement(name);
        this.attributes.addElement(value);
        return (XmlSerializer)this;
    }
    
    public void cdsect(final String cdsect) throws IOException {
        this.text(cdsect);
    }
    
    public void comment(final String comment) {
    }
    
    public void docdecl(final String docdecl) {
        throw new RuntimeException("Cannot write docdecl for WBXML");
    }
    
    public void entityRef(final String er) {
        throw new RuntimeException("EntityReference not supported for WBXML");
    }
    
    public int getDepth() {
        return this.depth;
    }
    
    public boolean getFeature(final String name) {
        return false;
    }
    
    public String getNamespace() {
        return null;
    }
    
    public String getName() {
        return this.pending;
    }
    
    public String getPrefix(final String nsp, final boolean create) {
        throw new RuntimeException("NYI");
    }
    
    public Object getProperty(final String name) {
        return null;
    }
    
    public void ignorableWhitespace(final String sp) {
    }
    
    public void endDocument() throws IOException {
        this.flush();
    }
    
    public void flush() throws IOException {
        this.checkPending(false);
        if (!this.headerSent) {
            writeInt(this.out, this.stringTableBuf.size());
            this.out.write(this.stringTableBuf.toByteArray());
            this.headerSent = true;
        }
        this.out.write(this.buf.toByteArray());
        this.buf.reset();
    }
    
    public void checkPending(final boolean degenerated) throws IOException {
        if (this.pending == null) {
            return;
        }
        final int len = this.attributes.size();
        int[] idx = this.tagTable.get(this.pending);
        if (idx == null) {
            this.buf.write((len == 0) ? (degenerated ? 4 : 68) : (degenerated ? 132 : 196));
            this.writeStrT(this.pending, false);
        }
        else {
            if (idx[0] != this.tagPage) {
                this.tagPage = idx[0];
                this.buf.write(0);
                this.buf.write(this.tagPage);
            }
            this.buf.write((len == 0) ? (degenerated ? idx[1] : (idx[1] | 0x40)) : (degenerated ? (idx[1] | 0x80) : (idx[1] | 0xC0)));
        }
        for (int i = 0; i < len; ++i) {
            idx = this.attrStartTable.get(this.attributes.elementAt(i));
            if (idx == null) {
                this.buf.write(4);
                this.writeStrT(this.attributes.elementAt(i), false);
            }
            else {
                if (idx[0] != this.attrPage) {
                    this.attrPage = idx[0];
                    this.buf.write(0);
                    this.buf.write(this.attrPage);
                }
                this.buf.write(idx[1]);
            }
            idx = this.attrValueTable.get(this.attributes.elementAt(++i));
            if (idx == null) {
                this.writeStr(this.attributes.elementAt(i));
            }
            else {
                if (idx[0] != this.attrPage) {
                    this.attrPage = idx[0];
                    this.buf.write(0);
                    this.buf.write(this.attrPage);
                }
                this.buf.write(idx[1]);
            }
        }
        if (len > 0) {
            this.buf.write(1);
        }
        this.pending = null;
        this.attributes.removeAllElements();
    }
    
    public void processingInstruction(final String pi) {
        throw new RuntimeException("PI NYI");
    }
    
    public void setFeature(final String name, final boolean value) {
        throw new IllegalArgumentException("unknown feature " + name);
    }
    
    public void setOutput(final Writer writer) {
        throw new RuntimeException("Wbxml requires an OutputStream!");
    }
    
    public void setOutput(final OutputStream out, final String encoding) throws IOException {
        this.encoding = ((encoding == null) ? "UTF-8" : encoding);
        this.out = out;
        this.buf = new ByteArrayOutputStream();
        this.stringTableBuf = new ByteArrayOutputStream();
        this.headerSent = false;
    }
    
    public void setPrefix(final String prefix, final String nsp) {
        throw new RuntimeException("NYI");
    }
    
    public void setProperty(final String property, final Object value) {
        throw new IllegalArgumentException("unknown property " + property);
    }
    
    public void startDocument(final String encoding, final Boolean standalone) throws IOException {
        this.out.write(3);
        this.out.write(1);
        if (encoding != null) {
            this.encoding = encoding;
        }
        if (this.encoding.toUpperCase().equals("UTF-8")) {
            this.out.write(106);
        }
        else {
            if (!this.encoding.toUpperCase().equals("ISO-8859-1")) {
                throw new UnsupportedEncodingException(encoding);
            }
            this.out.write(4);
        }
    }
    
    public XmlSerializer startTag(final String namespace, final String name) throws IOException {
        if (namespace != null && !"".equals(namespace)) {
            throw new RuntimeException("NSP NYI");
        }
        this.checkPending(false);
        this.pending = name;
        ++this.depth;
        return (XmlSerializer)this;
    }
    
    public XmlSerializer text(final char[] chars, final int start, final int len) throws IOException {
        this.checkPending(false);
        this.writeStr(new String(chars, start, len));
        return (XmlSerializer)this;
    }
    
    public XmlSerializer text(final String text) throws IOException {
        this.checkPending(false);
        this.writeStr(text);
        return (XmlSerializer)this;
    }
    
    private void writeStr(final String text) throws IOException {
        int p0 = 0;
        int lastCut = 0;
        final int len = text.length();
        if (this.headerSent) {
            this.writeStrI(this.buf, text);
            return;
        }
        while (p0 < len) {
            while (p0 < len && text.charAt(p0) < 'A') {
                ++p0;
            }
            int p2;
            for (p2 = p0; p2 < len && text.charAt(p2) >= 'A'; ++p2) {}
            if (p2 - p0 > 10) {
                if (p0 > lastCut && text.charAt(p0 - 1) == ' ' && this.stringTable.get(text.substring(p0, p2)) == null) {
                    this.buf.write(131);
                    this.writeStrT(text.substring(lastCut, p2), false);
                }
                else {
                    if (p0 > lastCut && text.charAt(p0 - 1) == ' ') {
                        --p0;
                    }
                    if (p0 > lastCut) {
                        this.buf.write(131);
                        this.writeStrT(text.substring(lastCut, p0), false);
                    }
                    this.buf.write(131);
                    this.writeStrT(text.substring(p0, p2), true);
                }
                lastCut = p2;
            }
            p0 = p2;
        }
        if (lastCut < len) {
            this.buf.write(131);
            this.writeStrT(text.substring(lastCut, len), false);
        }
    }
    
    public XmlSerializer endTag(final String namespace, final String name) throws IOException {
        if (this.pending != null) {
            this.checkPending(true);
        }
        else {
            this.buf.write(1);
        }
        --this.depth;
        return (XmlSerializer)this;
    }
    
    public void writeWapExtension(final int type, final Object data) throws IOException {
        this.checkPending(false);
        this.buf.write(type);
        switch (type) {
            case 192:
            case 193:
            case 194: {
                break;
            }
            case 195: {
                final byte[] bytes = (byte[])data;
                writeInt(this.buf, bytes.length);
                this.buf.write(bytes);
                break;
            }
            case 64:
            case 65:
            case 66: {
                this.writeStrI(this.buf, (String)data);
                break;
            }
            case 128:
            case 129:
            case 130: {
                this.writeStrT((String)data, false);
                break;
            }
            default: {
                throw new IllegalArgumentException();
            }
        }
    }
    
    static void writeInt(final OutputStream out, int i) throws IOException {
        final byte[] buf = new byte[5];
        int idx = 0;
        do {
            buf[idx++] = (byte)(i & 0x7F);
            i >>= 7;
        } while (i != 0);
        while (idx > 1) {
            out.write(buf[--idx] | 0x80);
        }
        out.write(buf[0]);
    }
    
    void writeStrI(final OutputStream out, final String s) throws IOException {
        final byte[] data = s.getBytes(this.encoding);
        out.write(data);
        out.write(0);
    }
    
    private final void writeStrT(final String s, final boolean mayPrependSpace) throws IOException {
        final Integer idx = this.stringTable.get(s);
        writeInt(this.buf, (idx == null) ? this.addToStringTable(s, mayPrependSpace) : ((int)idx));
    }
    
    public int addToStringTable(String s, final boolean mayPrependSpace) throws IOException {
        if (this.headerSent) {
            throw new IOException("stringtable sent");
        }
        int offset;
        final int i = offset = this.stringTableBuf.size();
        if (s.charAt(0) >= '0' && mayPrependSpace) {
            s = ' ' + s;
            ++offset;
        }
        this.stringTable.put(s, new Integer(i));
        if (s.charAt(0) == ' ') {
            this.stringTable.put(s.substring(1), new Integer(i + 1));
        }
        final int j = s.lastIndexOf(32);
        if (j > 1) {
            final String t = s.substring(j);
            final int k = t.getBytes("utf-8").length;
            this.stringTable.put(t, new Integer(i + k));
            this.stringTable.put(s.substring(j + 1), new Integer(i + k + 1));
        }
        this.writeStrI(this.stringTableBuf, s);
        this.stringTableBuf.flush();
        return offset;
    }
    
    public void setTagTable(final int page, final String[] tagTable) {
        for (int i = 0; i < tagTable.length; ++i) {
            if (tagTable[i] != null) {
                final Object idx = { page, i + 5 };
                this.tagTable.put(tagTable[i], idx);
            }
        }
    }
    
    public void setAttrStartTable(final int page, final String[] attrStartTable) {
        for (int i = 0; i < attrStartTable.length; ++i) {
            if (attrStartTable[i] != null) {
                final Object idx = { page, i + 5 };
                this.attrStartTable.put(attrStartTable[i], idx);
            }
        }
    }
    
    public void setAttrValueTable(final int page, final String[] attrValueTable) {
        for (int i = 0; i < attrValueTable.length; ++i) {
            if (attrValueTable[i] != null) {
                final Object idx = { page, i + 133 };
                this.attrValueTable.put(attrValueTable[i], idx);
            }
        }
    }
}
