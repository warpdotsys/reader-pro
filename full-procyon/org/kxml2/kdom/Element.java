// 
// Decompiled by Procyon v0.6.0
// 

package org.kxml2.kdom;

import org.xmlpull.v1.XmlSerializer;
import org.xmlpull.v1.XmlPullParserException;
import java.io.IOException;
import org.xmlpull.v1.XmlPullParser;
import java.util.Vector;

public class Element extends Node
{
    protected String namespace;
    protected String name;
    protected Vector attributes;
    protected Node parent;
    protected Vector prefixes;
    
    public void init() {
    }
    
    public void clear() {
        this.attributes = null;
        this.children = null;
    }
    
    @Override
    public Element createElement(final String namespace, final String name) {
        return (this.parent == null) ? super.createElement(namespace, name) : this.parent.createElement(namespace, name);
    }
    
    public int getAttributeCount() {
        return (this.attributes == null) ? 0 : this.attributes.size();
    }
    
    public String getAttributeNamespace(final int index) {
        return ((String[])this.attributes.elementAt(index))[0];
    }
    
    public String getAttributeName(final int index) {
        return ((String[])this.attributes.elementAt(index))[1];
    }
    
    public String getAttributeValue(final int index) {
        return ((String[])this.attributes.elementAt(index))[2];
    }
    
    public String getAttributeValue(final String namespace, final String name) {
        for (int i = 0; i < this.getAttributeCount(); ++i) {
            if (name.equals(this.getAttributeName(i)) && (namespace == null || namespace.equals(this.getAttributeNamespace(i)))) {
                return this.getAttributeValue(i);
            }
        }
        return null;
    }
    
    public Node getRoot() {
        Element current;
        for (current = this; current.parent != null; current = (Element)current.parent) {
            if (!(current.parent instanceof Element)) {
                return current.parent;
            }
        }
        return current;
    }
    
    public String getName() {
        return this.name;
    }
    
    public String getNamespace() {
        return this.namespace;
    }
    
    public String getNamespaceUri(final String prefix) {
        for (int cnt = this.getNamespaceCount(), i = 0; i < cnt; ++i) {
            if (prefix == this.getNamespacePrefix(i) || (prefix != null && prefix.equals(this.getNamespacePrefix(i)))) {
                return this.getNamespaceUri(i);
            }
        }
        return (this.parent instanceof Element) ? ((Element)this.parent).getNamespaceUri(prefix) : null;
    }
    
    public int getNamespaceCount() {
        return (this.prefixes == null) ? 0 : this.prefixes.size();
    }
    
    public String getNamespacePrefix(final int i) {
        return ((String[])this.prefixes.elementAt(i))[0];
    }
    
    public String getNamespaceUri(final int i) {
        return ((String[])this.prefixes.elementAt(i))[1];
    }
    
    public Node getParent() {
        return this.parent;
    }
    
    @Override
    public void parse(final XmlPullParser parser) throws IOException, XmlPullParserException {
        for (int i = parser.getNamespaceCount(parser.getDepth() - 1); i < parser.getNamespaceCount(parser.getDepth()); ++i) {
            this.setPrefix(parser.getNamespacePrefix(i), parser.getNamespaceUri(i));
        }
        for (int i = 0; i < parser.getAttributeCount(); ++i) {
            this.setAttribute(parser.getAttributeNamespace(i), parser.getAttributeName(i), parser.getAttributeValue(i));
        }
        this.init();
        if (parser.isEmptyElementTag()) {
            parser.nextToken();
        }
        else {
            parser.nextToken();
            super.parse(parser);
            if (this.getChildCount() == 0) {
                this.addChild(7, "");
            }
        }
        parser.require(3, this.getNamespace(), this.getName());
        parser.nextToken();
    }
    
    public void setAttribute(String namespace, final String name, final String value) {
        if (this.attributes == null) {
            this.attributes = new Vector();
        }
        if (namespace == null) {
            namespace = "";
        }
        for (int i = this.attributes.size() - 1; i >= 0; --i) {
            final String[] attribut = this.attributes.elementAt(i);
            if (attribut[0].equals(namespace) && attribut[1].equals(name)) {
                if (value == null) {
                    this.attributes.removeElementAt(i);
                }
                else {
                    attribut[2] = value;
                }
                return;
            }
        }
        this.attributes.addElement(new String[] { namespace, name, value });
    }
    
    public void setPrefix(final String prefix, final String namespace) {
        if (this.prefixes == null) {
            this.prefixes = new Vector();
        }
        this.prefixes.addElement(new String[] { prefix, namespace });
    }
    
    public void setName(final String name) {
        this.name = name;
    }
    
    public void setNamespace(final String namespace) {
        if (namespace == null) {
            throw new NullPointerException("Use \"\" for empty namespace");
        }
        this.namespace = namespace;
    }
    
    protected void setParent(final Node parent) {
        this.parent = parent;
    }
    
    @Override
    public void write(final XmlSerializer writer) throws IOException {
        if (this.prefixes != null) {
            for (int i = 0; i < this.prefixes.size(); ++i) {
                writer.setPrefix(this.getNamespacePrefix(i), this.getNamespaceUri(i));
            }
        }
        writer.startTag(this.getNamespace(), this.getName());
        for (int len = this.getAttributeCount(), j = 0; j < len; ++j) {
            writer.attribute(this.getAttributeNamespace(j), this.getAttributeName(j), this.getAttributeValue(j));
        }
        this.writeChildren(writer);
        writer.endTag(this.getNamespace(), this.getName());
    }
}
