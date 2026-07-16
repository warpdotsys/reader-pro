// 
// Decompiled by Procyon v0.6.0
// 

package org.kxml2.io;

import java.io.OutputStreamWriter;
import java.io.OutputStream;
import java.io.IOException;
import java.io.Writer;
import org.xmlpull.v1.XmlSerializer;

public class KXmlSerializer implements XmlSerializer
{
    private Writer writer;
    private boolean pending;
    private int auto;
    private int depth;
    private String[] elementStack;
    private int[] nspCounts;
    private String[] nspStack;
    private boolean[] indent;
    private boolean unicode;
    private String encoding;
    
    public KXmlSerializer() {
        this.elementStack = new String[12];
        this.nspCounts = new int[4];
        this.nspStack = new String[8];
        this.indent = new boolean[4];
    }
    
    private final void check(final boolean close) throws IOException {
        if (!this.pending) {
            return;
        }
        ++this.depth;
        this.pending = false;
        if (this.indent.length <= this.depth) {
            final boolean[] hlp = new boolean[this.depth + 4];
            System.arraycopy(this.indent, 0, hlp, 0, this.depth);
            this.indent = hlp;
        }
        this.indent[this.depth] = this.indent[this.depth - 1];
        for (int i = this.nspCounts[this.depth - 1]; i < this.nspCounts[this.depth]; ++i) {
            this.writer.write(32);
            this.writer.write("xmlns");
            if (!"".equals(this.nspStack[i * 2])) {
                this.writer.write(58);
                this.writer.write(this.nspStack[i * 2]);
            }
            else if ("".equals(this.getNamespace()) && !"".equals(this.nspStack[i * 2 + 1])) {
                throw new IllegalStateException("Cannot set default namespace for elements in no namespace");
            }
            this.writer.write("=\"");
            this.writeEscaped(this.nspStack[i * 2 + 1], 34);
            this.writer.write(34);
        }
        if (this.nspCounts.length <= this.depth + 1) {
            final int[] hlp2 = new int[this.depth + 8];
            System.arraycopy(this.nspCounts, 0, hlp2, 0, this.depth + 1);
            this.nspCounts = hlp2;
        }
        this.nspCounts[this.depth + 1] = this.nspCounts[this.depth];
        this.writer.write(close ? " />" : ">");
    }
    
    private final void writeEscaped(final String s, final int quot) throws IOException {
        for (int i = 0; i < s.length(); ++i) {
            final char c = s.charAt(i);
            switch (c) {
                case '\t':
                case '\n':
                case '\r': {
                    if (quot == -1) {
                        this.writer.write(c);
                        continue;
                    }
                    this.writer.write("&#" + (int)c + ';');
                    continue;
                }
                case '&': {
                    this.writer.write("&amp;");
                    continue;
                }
                case '>': {
                    this.writer.write("&gt;");
                    continue;
                }
                case '<': {
                    this.writer.write("&lt;");
                    continue;
                }
                case '\"':
                case '\'': {
                    if (c == quot) {
                        this.writer.write((c == '\"') ? "&quot;" : "&apos;");
                        continue;
                    }
                    break;
                }
            }
            if (i < s.length() - 1) {
                final char cLow = s.charAt(i + 1);
                if (c >= '\ud800' && c <= '\udbff' && cLow >= '\udc00' && cLow <= '\udfff') {
                    final int n = (c - '\ud800' << 10) + (cLow - '\udc00') + 65536;
                    this.writer.write("&#" + n + ";");
                    ++i;
                    continue;
                }
            }
            if (c >= ' ' && c != '@' && (c < '\u007f' || this.unicode)) {
                this.writer.write(c);
            }
            else {
                this.writer.write("&#" + (int)c + ";");
            }
        }
    }
    
    public void docdecl(final String dd) throws IOException {
        this.writer.write("<!DOCTYPE");
        this.writer.write(dd);
        this.writer.write(">");
    }
    
    public void endDocument() throws IOException {
        while (this.depth > 0) {
            this.endTag(this.elementStack[this.depth * 3 - 3], this.elementStack[this.depth * 3 - 1]);
        }
        this.flush();
    }
    
    public void entityRef(final String name) throws IOException {
        this.check(false);
        this.writer.write(38);
        this.writer.write(name);
        this.writer.write(59);
    }
    
    public boolean getFeature(final String name) {
        return "http://xmlpull.org/v1/doc/features.html#indent-output".equals(name) && this.indent[this.depth];
    }
    
    public String getPrefix(final String namespace, final boolean create) {
        try {
            return this.getPrefix(namespace, false, create);
        }
        catch (final IOException e) {
            throw new RuntimeException(e.toString());
        }
    }
    
    private final String getPrefix(final String namespace, final boolean includeDefault, final boolean create) throws IOException {
        for (int i = this.nspCounts[this.depth + 1] * 2 - 2; i >= 0; i -= 2) {
            if (this.nspStack[i + 1].equals(namespace) && (includeDefault || !this.nspStack[i].equals(""))) {
                String cand = this.nspStack[i];
                for (int j = i + 2; j < this.nspCounts[this.depth + 1] * 2; ++j) {
                    if (this.nspStack[j].equals(cand)) {
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
        String prefix;
        if ("".equals(namespace)) {
            prefix = "";
        }
        else {
            do {
                prefix = "n" + this.auto++;
                for (int k = this.nspCounts[this.depth + 1] * 2 - 2; k >= 0; k -= 2) {
                    if (prefix.equals(this.nspStack[k])) {
                        prefix = null;
                        break;
                    }
                }
            } while (prefix == null);
        }
        final boolean p = this.pending;
        this.pending = false;
        this.setPrefix(prefix, namespace);
        this.pending = p;
        return prefix;
    }
    
    public Object getProperty(final String name) {
        throw new RuntimeException("Unsupported property");
    }
    
    public void ignorableWhitespace(final String s) throws IOException {
        this.text(s);
    }
    
    public void setFeature(final String name, final boolean value) {
        if ("http://xmlpull.org/v1/doc/features.html#indent-output".equals(name)) {
            this.indent[this.depth] = value;
            return;
        }
        throw new RuntimeException("Unsupported Feature");
    }
    
    public void setProperty(final String name, final Object value) {
        throw new RuntimeException("Unsupported Property:" + value);
    }
    
    public void setPrefix(String prefix, String namespace) throws IOException {
        this.check(false);
        if (prefix == null) {
            prefix = "";
        }
        if (namespace == null) {
            namespace = "";
        }
        final String defined = this.getPrefix(namespace, true, false);
        if (prefix.equals(defined)) {
            return;
        }
        int pos = this.nspCounts[this.depth + 1]++ << 1;
        if (this.nspStack.length < pos + 1) {
            final String[] hlp = new String[this.nspStack.length + 16];
            System.arraycopy(this.nspStack, 0, hlp, 0, pos);
            this.nspStack = hlp;
        }
        this.nspStack[pos++] = prefix;
        this.nspStack[pos] = namespace;
    }
    
    public void setOutput(final Writer writer) {
        this.writer = writer;
        this.nspCounts[0] = 2;
        this.nspCounts[1] = 2;
        this.nspStack[0] = "";
        this.nspStack[1] = "";
        this.nspStack[2] = "xml";
        this.nspStack[3] = "http://www.w3.org/XML/1998/namespace";
        this.pending = false;
        this.auto = 0;
        this.depth = 0;
        this.unicode = false;
    }
    
    public void setOutput(final OutputStream os, final String encoding) throws IOException {
        if (os == null) {
            throw new IllegalArgumentException();
        }
        this.setOutput((encoding == null) ? new OutputStreamWriter(os) : new OutputStreamWriter(os, encoding));
        this.encoding = encoding;
        if (encoding != null && encoding.toLowerCase().startsWith("utf")) {
            this.unicode = true;
        }
    }
    
    public void startDocument(final String encoding, final Boolean standalone) throws IOException {
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
            this.writer.write(((boolean)standalone) ? "yes" : "no");
            this.writer.write("' ");
        }
        this.writer.write("?>");
    }
    
    public XmlSerializer startTag(final String namespace, final String name) throws IOException {
        this.check(false);
        if (this.indent[this.depth]) {
            this.writer.write("\r\n");
            for (int i = 0; i < this.depth; ++i) {
                this.writer.write("  ");
            }
        }
        int esp = this.depth * 3;
        if (this.elementStack.length < esp + 3) {
            final String[] hlp = new String[this.elementStack.length + 12];
            System.arraycopy(this.elementStack, 0, hlp, 0, esp);
            this.elementStack = hlp;
        }
        final String prefix = (namespace == null) ? "" : this.getPrefix(namespace, true, true);
        if ("".equals(namespace)) {
            for (int j = this.nspCounts[this.depth]; j < this.nspCounts[this.depth + 1]; ++j) {
                if ("".equals(this.nspStack[j * 2]) && !"".equals(this.nspStack[j * 2 + 1])) {
                    throw new IllegalStateException("Cannot set default namespace for elements in no namespace");
                }
            }
        }
        this.elementStack[esp++] = namespace;
        this.elementStack[esp++] = prefix;
        this.elementStack[esp] = name;
        this.writer.write(60);
        if (!"".equals(prefix)) {
            this.writer.write(prefix);
            this.writer.write(58);
        }
        this.writer.write(name);
        this.pending = true;
        return (XmlSerializer)this;
    }
    
    public XmlSerializer attribute(String namespace, final String name, final String value) throws IOException {
        if (!this.pending) {
            throw new IllegalStateException("illegal position for attribute");
        }
        if (namespace == null) {
            namespace = "";
        }
        final String prefix = "".equals(namespace) ? "" : this.getPrefix(namespace, false, true);
        this.writer.write(32);
        if (!"".equals(prefix)) {
            this.writer.write(prefix);
            this.writer.write(58);
        }
        this.writer.write(name);
        this.writer.write(61);
        final char q = (value.indexOf(34) == -1) ? '\"' : '\'';
        this.writer.write(q);
        this.writeEscaped(value, q);
        this.writer.write(q);
        return (XmlSerializer)this;
    }
    
    public void flush() throws IOException {
        this.check(false);
        this.writer.flush();
    }
    
    public XmlSerializer endTag(final String namespace, final String name) throws IOException {
        if (!this.pending) {
            --this.depth;
        }
        if ((namespace == null && this.elementStack[this.depth * 3] != null) || (namespace != null && !namespace.equals(this.elementStack[this.depth * 3])) || !this.elementStack[this.depth * 3 + 2].equals(name)) {
            throw new IllegalArgumentException("</{" + namespace + "}" + name + "> does not match start");
        }
        if (this.pending) {
            this.check(true);
            --this.depth;
        }
        else {
            if (this.indent[this.depth + 1]) {
                this.writer.write("\r\n");
                for (int i = 0; i < this.depth; ++i) {
                    this.writer.write("  ");
                }
            }
            this.writer.write("</");
            final String prefix = this.elementStack[this.depth * 3 + 1];
            if (!"".equals(prefix)) {
                this.writer.write(prefix);
                this.writer.write(58);
            }
            this.writer.write(name);
            this.writer.write(62);
        }
        this.nspCounts[this.depth + 1] = this.nspCounts[this.depth];
        return (XmlSerializer)this;
    }
    
    public String getNamespace() {
        return (this.getDepth() == 0) ? null : this.elementStack[this.getDepth() * 3 - 3];
    }
    
    public String getName() {
        return (this.getDepth() == 0) ? null : this.elementStack[this.getDepth() * 3 - 1];
    }
    
    public int getDepth() {
        return this.pending ? (this.depth + 1) : this.depth;
    }
    
    public XmlSerializer text(final String text) throws IOException {
        this.check(false);
        this.indent[this.depth] = false;
        this.writeEscaped(text, -1);
        return (XmlSerializer)this;
    }
    
    public XmlSerializer text(final char[] text, final int start, final int len) throws IOException {
        this.text(new String(text, start, len));
        return (XmlSerializer)this;
    }
    
    public void cdsect(final String data) throws IOException {
        this.check(false);
        this.writer.write("<![CDATA[");
        this.writer.write(data);
        this.writer.write("]]>");
    }
    
    public void comment(final String comment) throws IOException {
        this.check(false);
        this.writer.write("<!--");
        this.writer.write(comment);
        this.writer.write("-->");
    }
    
    public void processingInstruction(final String pi) throws IOException {
        this.check(false);
        this.writer.write("<?");
        this.writer.write(pi);
        this.writer.write("?>");
    }
}
