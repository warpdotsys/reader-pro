package org.kxml2.io;

import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.Writer;
import me.ag2s.epublib.epub.NCXDocumentV3;
import me.ag2s.epublib.epub.PackageDocumentBase;
import org.kxml2.kdom.Node;
import org.xmlpull.v1.XmlSerializer;

/* JADX INFO: loaded from: app-classes.jar:org/kxml2/io/KXmlSerializer.class */
public class KXmlSerializer implements XmlSerializer {
    private Writer writer;
    private boolean pending;
    private int auto;
    private int depth;
    private String[] elementStack = new String[12];
    private int[] nspCounts = new int[4];
    private String[] nspStack = new String[8];
    private boolean[] indent = new boolean[4];
    private boolean unicode;
    private String encoding;

    private final void check(boolean close) throws IOException {
        if (!this.pending) {
            return;
        }
        this.depth++;
        this.pending = false;
        if (this.indent.length <= this.depth) {
            boolean[] hlp = new boolean[this.depth + 4];
            System.arraycopy(this.indent, 0, hlp, 0, this.depth);
            this.indent = hlp;
        }
        this.indent[this.depth] = this.indent[this.depth - 1];
        for (int i = this.nspCounts[this.depth - 1]; i < this.nspCounts[this.depth]; i++) {
            this.writer.write(32);
            this.writer.write(NCXDocumentV3.XHTMLAttributes.xmlns);
            if (!PackageDocumentBase.PREFIX_OPF.equals(this.nspStack[i * 2])) {
                this.writer.write(58);
                this.writer.write(this.nspStack[i * 2]);
            } else if (PackageDocumentBase.PREFIX_OPF.equals(getNamespace()) && !PackageDocumentBase.PREFIX_OPF.equals(this.nspStack[(i * 2) + 1])) {
                throw new IllegalStateException("Cannot set default namespace for elements in no namespace");
            }
            this.writer.write("=\"");
            writeEscaped(this.nspStack[(i * 2) + 1], 34);
            this.writer.write(34);
        }
        if (this.nspCounts.length <= this.depth + 1) {
            int[] hlp2 = new int[this.depth + 8];
            System.arraycopy(this.nspCounts, 0, hlp2, 0, this.depth + 1);
            this.nspCounts = hlp2;
        }
        this.nspCounts[this.depth + 1] = this.nspCounts[this.depth];
        this.writer.write(close ? " />" : ">");
    }

    /* JADX WARN: Removed duplicated region for block: B:32:0x013a  */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    private final void writeEscaped(String s, int quot) throws IOException {
        int i = 0;
        while (i < s.length()) {
            char c = s.charAt(i);
            switch (c) {
                case Node.COMMENT /* 9 */:
                case Node.DOCDECL /* 10 */:
                case '\r':
                    if (quot == -1) {
                        this.writer.write(c);
                        continue;
                    } else {
                        this.writer.write("&#" + ((int) c) + ';');
                    }
                    i++;
                    break;
                case '\"':
                case '\'':
                    if (c == quot) {
                        this.writer.write(c == '\"' ? "&quot;" : "&apos;");
                    }
                    i++;
                    break;
                case '&':
                    this.writer.write("&amp;");
                    continue;
                    i++;
                    break;
                case '<':
                    this.writer.write("&lt;");
                    continue;
                    i++;
                    break;
                case '>':
                    this.writer.write("&gt;");
                    continue;
                    i++;
                    break;
            }
            if (i < s.length() - 1) {
                char cLow = s.charAt(i + 1);
                if (c >= 55296 && c <= 56319 && cLow >= 56320 && cLow <= 57343) {
                    int n = ((c - 55296) << 10) + (cLow - 56320) + 65536;
                    this.writer.write("&#" + n + ";");
                    i++;
                } else if (c >= ' ' && c != '@' && (c < 127 || this.unicode)) {
                    this.writer.write(c);
                } else {
                    this.writer.write("&#" + ((int) c) + ";");
                }
            }
            i++;
        }
    }

    @Override // org.xmlpull.v1.XmlSerializer
    public void docdecl(String dd) throws IOException {
        this.writer.write("<!DOCTYPE");
        this.writer.write(dd);
        this.writer.write(">");
    }

    @Override // org.xmlpull.v1.XmlSerializer
    public void endDocument() throws IOException {
        while (this.depth > 0) {
            endTag(this.elementStack[(this.depth * 3) - 3], this.elementStack[(this.depth * 3) - 1]);
        }
        flush();
    }

    @Override // org.xmlpull.v1.XmlSerializer
    public void entityRef(String name) throws IOException {
        check(false);
        this.writer.write(38);
        this.writer.write(name);
        this.writer.write(59);
    }

    @Override // org.xmlpull.v1.XmlSerializer
    public boolean getFeature(String name) {
        if ("http://xmlpull.org/v1/doc/features.html#indent-output".equals(name)) {
            return this.indent[this.depth];
        }
        return false;
    }

    @Override // org.xmlpull.v1.XmlSerializer
    public String getPrefix(String namespace, boolean create) {
        try {
            return getPrefix(namespace, false, create);
        } catch (IOException e) {
            throw new RuntimeException(e.toString());
        }
    }

    private final String getPrefix(String namespace, boolean includeDefault, boolean create) throws IOException {
        String prefix;
        for (int i = (this.nspCounts[this.depth + 1] * 2) - 2; i >= 0; i -= 2) {
            if (this.nspStack[i + 1].equals(namespace) && (includeDefault || !this.nspStack[i].equals(PackageDocumentBase.PREFIX_OPF))) {
                String cand = this.nspStack[i];
                int j = i + 2;
                while (true) {
                    if (j >= this.nspCounts[this.depth + 1] * 2) {
                        break;
                    }
                    if (!this.nspStack[j].equals(cand)) {
                        j++;
                    } else {
                        cand = null;
                        break;
                    }
                }
                if (cand != null) {
                    return cand;
                }
            }
        }
        if (!create) {
            return null;
        }
        if (PackageDocumentBase.PREFIX_OPF.equals(namespace)) {
            prefix = PackageDocumentBase.PREFIX_OPF;
        } else {
            do {
                StringBuilder sbAppend = new StringBuilder().append("n");
                int i2 = this.auto;
                this.auto = i2 + 1;
                prefix = sbAppend.append(i2).toString();
                int i3 = (this.nspCounts[this.depth + 1] * 2) - 2;
                while (true) {
                    if (i3 < 0) {
                        break;
                    }
                    if (prefix.equals(this.nspStack[i3])) {
                        prefix = null;
                        break;
                    }
                    i3 -= 2;
                }
            } while (prefix == null);
        }
        boolean p = this.pending;
        this.pending = false;
        setPrefix(prefix, namespace);
        this.pending = p;
        return prefix;
    }

    @Override // org.xmlpull.v1.XmlSerializer
    public Object getProperty(String name) {
        throw new RuntimeException("Unsupported property");
    }

    @Override // org.xmlpull.v1.XmlSerializer
    public void ignorableWhitespace(String s) throws IOException {
        text(s);
    }

    @Override // org.xmlpull.v1.XmlSerializer
    public void setFeature(String name, boolean value) {
        if ("http://xmlpull.org/v1/doc/features.html#indent-output".equals(name)) {
            this.indent[this.depth] = value;
            return;
        }
        throw new RuntimeException("Unsupported Feature");
    }

    @Override // org.xmlpull.v1.XmlSerializer
    public void setProperty(String name, Object value) {
        throw new RuntimeException("Unsupported Property:" + value);
    }

    @Override // org.xmlpull.v1.XmlSerializer
    public void setPrefix(String prefix, String namespace) throws IOException {
        check(false);
        if (prefix == null) {
            prefix = PackageDocumentBase.PREFIX_OPF;
        }
        if (namespace == null) {
            namespace = PackageDocumentBase.PREFIX_OPF;
        }
        String defined = getPrefix(namespace, true, false);
        if (prefix.equals(defined)) {
            return;
        }
        int[] iArr = this.nspCounts;
        int i = this.depth + 1;
        int i2 = iArr[i];
        iArr[i] = i2 + 1;
        int pos = i2 << 1;
        if (this.nspStack.length < pos + 1) {
            String[] hlp = new String[this.nspStack.length + 16];
            System.arraycopy(this.nspStack, 0, hlp, 0, pos);
            this.nspStack = hlp;
        }
        this.nspStack[pos] = prefix;
        this.nspStack[pos + 1] = namespace;
    }

    @Override // org.xmlpull.v1.XmlSerializer
    public void setOutput(Writer writer) {
        this.writer = writer;
        this.nspCounts[0] = 2;
        this.nspCounts[1] = 2;
        this.nspStack[0] = PackageDocumentBase.PREFIX_OPF;
        this.nspStack[1] = PackageDocumentBase.PREFIX_OPF;
        this.nspStack[2] = "xml";
        this.nspStack[3] = "http://www.w3.org/XML/1998/namespace";
        this.pending = false;
        this.auto = 0;
        this.depth = 0;
        this.unicode = false;
    }

    @Override // org.xmlpull.v1.XmlSerializer
    public void setOutput(OutputStream os, String encoding) throws IOException {
        OutputStreamWriter outputStreamWriter;
        if (os == null) {
            throw new IllegalArgumentException();
        }
        if (encoding == null) {
            outputStreamWriter = new OutputStreamWriter(os);
        } else {
            outputStreamWriter = new OutputStreamWriter(os, encoding);
        }
        setOutput(outputStreamWriter);
        this.encoding = encoding;
        if (encoding != null && encoding.toLowerCase().startsWith("utf")) {
            this.unicode = true;
        }
    }

    @Override // org.xmlpull.v1.XmlSerializer
    public void startDocument(String encoding, Boolean standalone) throws IOException {
        this.writer.write("<?xml version='1.0' ");
        if (encoding != null) {
            this.encoding = encoding;
            if (encoding.toLowerCase().startsWith("utf")) {
                this.unicode = true;
            }
        }
        if (this.encoding != null) {
            this.writer.write("encoding='");
            this.writer.write(this.encoding);
            this.writer.write("' ");
        }
        if (standalone != null) {
            this.writer.write("standalone='");
            this.writer.write(standalone.booleanValue() ? "yes" : PackageDocumentBase.OPFValues.no);
            this.writer.write("' ");
        }
        this.writer.write("?>");
    }

    @Override // org.xmlpull.v1.XmlSerializer
    public XmlSerializer startTag(String namespace, String name) throws IOException {
        String prefix;
        check(false);
        if (this.indent[this.depth]) {
            this.writer.write("\r\n");
            for (int i = 0; i < this.depth; i++) {
                this.writer.write("  ");
            }
        }
        int esp = this.depth * 3;
        if (this.elementStack.length < esp + 3) {
            String[] hlp = new String[this.elementStack.length + 12];
            System.arraycopy(this.elementStack, 0, hlp, 0, esp);
            this.elementStack = hlp;
        }
        if (namespace == null) {
            prefix = PackageDocumentBase.PREFIX_OPF;
        } else {
            prefix = getPrefix(namespace, true, true);
        }
        String prefix2 = prefix;
        if (PackageDocumentBase.PREFIX_OPF.equals(namespace)) {
            for (int i2 = this.nspCounts[this.depth]; i2 < this.nspCounts[this.depth + 1]; i2++) {
                if (PackageDocumentBase.PREFIX_OPF.equals(this.nspStack[i2 * 2]) && !PackageDocumentBase.PREFIX_OPF.equals(this.nspStack[(i2 * 2) + 1])) {
                    throw new IllegalStateException("Cannot set default namespace for elements in no namespace");
                }
            }
        }
        int esp2 = esp + 1;
        this.elementStack[esp] = namespace;
        this.elementStack[esp2] = prefix2;
        this.elementStack[esp2 + 1] = name;
        this.writer.write(60);
        if (!PackageDocumentBase.PREFIX_OPF.equals(prefix2)) {
            this.writer.write(prefix2);
            this.writer.write(58);
        }
        this.writer.write(name);
        this.pending = true;
        return this;
    }

    @Override // org.xmlpull.v1.XmlSerializer
    public XmlSerializer attribute(String namespace, String name, String value) throws IOException {
        String prefix;
        if (!this.pending) {
            throw new IllegalStateException("illegal position for attribute");
        }
        if (namespace == null) {
            namespace = PackageDocumentBase.PREFIX_OPF;
        }
        if (PackageDocumentBase.PREFIX_OPF.equals(namespace)) {
            prefix = PackageDocumentBase.PREFIX_OPF;
        } else {
            prefix = getPrefix(namespace, false, true);
        }
        String prefix2 = prefix;
        this.writer.write(32);
        if (!PackageDocumentBase.PREFIX_OPF.equals(prefix2)) {
            this.writer.write(prefix2);
            this.writer.write(58);
        }
        this.writer.write(name);
        this.writer.write(61);
        char q = value.indexOf(34) == -1 ? '\"' : '\'';
        this.writer.write(q);
        writeEscaped(value, q);
        this.writer.write(q);
        return this;
    }

    @Override // org.xmlpull.v1.XmlSerializer
    public void flush() throws IOException {
        check(false);
        this.writer.flush();
    }

    @Override // org.xmlpull.v1.XmlSerializer
    public XmlSerializer endTag(String namespace, String name) throws IOException {
        if (!this.pending) {
            this.depth--;
        }
        if ((namespace == null && this.elementStack[this.depth * 3] != null) || ((namespace != null && !namespace.equals(this.elementStack[this.depth * 3])) || !this.elementStack[(this.depth * 3) + 2].equals(name))) {
            throw new IllegalArgumentException("</{" + namespace + "}" + name + "> does not match start");
        }
        if (this.pending) {
            check(true);
            this.depth--;
        } else {
            if (this.indent[this.depth + 1]) {
                this.writer.write("\r\n");
                for (int i = 0; i < this.depth; i++) {
                    this.writer.write("  ");
                }
            }
            this.writer.write("</");
            String prefix = this.elementStack[(this.depth * 3) + 1];
            if (!PackageDocumentBase.PREFIX_OPF.equals(prefix)) {
                this.writer.write(prefix);
                this.writer.write(58);
            }
            this.writer.write(name);
            this.writer.write(62);
        }
        this.nspCounts[this.depth + 1] = this.nspCounts[this.depth];
        return this;
    }

    @Override // org.xmlpull.v1.XmlSerializer
    public String getNamespace() {
        if (getDepth() == 0) {
            return null;
        }
        return this.elementStack[(getDepth() * 3) - 3];
    }

    @Override // org.xmlpull.v1.XmlSerializer
    public String getName() {
        if (getDepth() == 0) {
            return null;
        }
        return this.elementStack[(getDepth() * 3) - 1];
    }

    @Override // org.xmlpull.v1.XmlSerializer
    public int getDepth() {
        return this.pending ? this.depth + 1 : this.depth;
    }

    @Override // org.xmlpull.v1.XmlSerializer
    public XmlSerializer text(String text) throws IOException {
        check(false);
        this.indent[this.depth] = false;
        writeEscaped(text, -1);
        return this;
    }

    @Override // org.xmlpull.v1.XmlSerializer
    public XmlSerializer text(char[] text, int start, int len) throws IOException {
        text(new String(text, start, len));
        return this;
    }

    @Override // org.xmlpull.v1.XmlSerializer
    public void cdsect(String data) throws IOException {
        check(false);
        this.writer.write("<![CDATA[");
        this.writer.write(data);
        this.writer.write("]]>");
    }

    @Override // org.xmlpull.v1.XmlSerializer
    public void comment(String comment) throws IOException {
        check(false);
        this.writer.write("<!--");
        this.writer.write(comment);
        this.writer.write("-->");
    }

    @Override // org.xmlpull.v1.XmlSerializer
    public void processingInstruction(String pi) throws IOException {
        check(false);
        this.writer.write("<?");
        this.writer.write(pi);
        this.writer.write("?>");
    }
}
