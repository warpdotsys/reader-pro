// 
// Decompiled by Procyon v0.6.0
// 

package org.kxml2.kdom;

import org.xmlpull.v1.XmlSerializer;
import org.xmlpull.v1.XmlPullParserException;
import java.io.IOException;
import org.xmlpull.v1.XmlPullParser;
import java.util.Vector;

public class Node
{
    public static final int DOCUMENT = 0;
    public static final int ELEMENT = 2;
    public static final int TEXT = 4;
    public static final int CDSECT = 5;
    public static final int ENTITY_REF = 6;
    public static final int IGNORABLE_WHITESPACE = 7;
    public static final int PROCESSING_INSTRUCTION = 8;
    public static final int COMMENT = 9;
    public static final int DOCDECL = 10;
    protected Vector children;
    protected StringBuffer types;
    
    public void addChild(final int index, final int type, final Object child) {
        if (child == null) {
            throw new NullPointerException();
        }
        if (this.children == null) {
            this.children = new Vector();
            this.types = new StringBuffer();
        }
        if (type == 2) {
            if (!(child instanceof Element)) {
                throw new RuntimeException("Element obj expected)");
            }
            ((Element)child).setParent(this);
        }
        else if (!(child instanceof String)) {
            throw new RuntimeException("String expected");
        }
        this.children.insertElementAt(child, index);
        this.types.insert(index, (char)type);
    }
    
    public void addChild(final int type, final Object child) {
        this.addChild(this.getChildCount(), type, child);
    }
    
    public Element createElement(final String namespace, final String name) {
        final Element e = new Element();
        e.namespace = ((namespace == null) ? "" : namespace);
        e.name = name;
        return e;
    }
    
    public Object getChild(final int index) {
        return this.children.elementAt(index);
    }
    
    public int getChildCount() {
        return (this.children == null) ? 0 : this.children.size();
    }
    
    public Element getElement(final int index) {
        final Object child = this.getChild(index);
        return (child instanceof Element) ? ((Element)child) : null;
    }
    
    public Element getElement(final String namespace, final String name) {
        final int i = this.indexOf(namespace, name, 0);
        final int j = this.indexOf(namespace, name, i + 1);
        if (i == -1 || j != -1) {
            throw new RuntimeException("Element {" + namespace + "}" + name + ((i == -1) ? " not found in " : " more than once in ") + this);
        }
        return this.getElement(i);
    }
    
    public String getText(final int index) {
        return this.isText(index) ? ((String)this.getChild(index)) : null;
    }
    
    public int getType(final int index) {
        return this.types.charAt(index);
    }
    
    public int indexOf(final String namespace, final String name, final int startIndex) {
        for (int len = this.getChildCount(), i = startIndex; i < len; ++i) {
            final Element child = this.getElement(i);
            if (child != null && name.equals(child.getName()) && (namespace == null || namespace.equals(child.getNamespace()))) {
                return i;
            }
        }
        return -1;
    }
    
    public boolean isText(final int i) {
        final int t = this.getType(i);
        return t == 4 || t == 7 || t == 5;
    }
    
    public void parse(final XmlPullParser parser) throws IOException, XmlPullParserException {
        boolean leave = false;
        do {
            final int type = parser.getEventType();
            switch (type) {
                case 2: {
                    final Element child = this.createElement(parser.getNamespace(), parser.getName());
                    this.addChild(2, child);
                    child.parse(parser);
                    continue;
                }
                case 1:
                case 3: {
                    leave = true;
                    continue;
                }
                default: {
                    if (parser.getText() != null) {
                        this.addChild((type == 6) ? 4 : type, parser.getText());
                    }
                    else if (type == 6 && parser.getName() != null) {
                        this.addChild(6, parser.getName());
                    }
                    parser.nextToken();
                    continue;
                }
            }
        } while (!leave);
    }
    
    public void removeChild(final int idx) {
        this.children.removeElementAt(idx);
        final int n = this.types.length() - 1;
        for (int i = idx; i < n; ++i) {
            this.types.setCharAt(i, this.types.charAt(i + 1));
        }
        this.types.setLength(n);
    }
    
    public void write(final XmlSerializer writer) throws IOException {
        this.writeChildren(writer);
        writer.flush();
    }
    
    public void writeChildren(final XmlSerializer writer) throws IOException {
        if (this.children == null) {
            return;
        }
        for (int len = this.children.size(), i = 0; i < len; ++i) {
            final int type = this.getType(i);
            final Object child = this.children.elementAt(i);
            switch (type) {
                case 2: {
                    ((Element)child).write(writer);
                    break;
                }
                case 4: {
                    writer.text((String)child);
                    break;
                }
                case 7: {
                    writer.ignorableWhitespace((String)child);
                    break;
                }
                case 5: {
                    writer.cdsect((String)child);
                    break;
                }
                case 9: {
                    writer.comment((String)child);
                    break;
                }
                case 6: {
                    writer.entityRef((String)child);
                    break;
                }
                case 8: {
                    writer.processingInstruction((String)child);
                    break;
                }
                case 10: {
                    writer.docdecl((String)child);
                    break;
                }
                default: {
                    throw new RuntimeException("Illegal type: " + type);
                }
            }
        }
    }
}
