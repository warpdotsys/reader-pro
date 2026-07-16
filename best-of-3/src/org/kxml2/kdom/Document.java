//
// Decompiled by Procyon v0.6.0
//

package org.kxml2.kdom;

import org.xmlpull.v1.XmlSerializer;
import org.xmlpull.v1.XmlPullParserException;
import java.io.IOException;
import org.xmlpull.v1.XmlPullParser;

public class Document extends Node
{
    protected int rootIndex;
    String encoding;
    Boolean standalone;

    public Document() {
        this.rootIndex = -1;
    }

    public String getEncoding() {
        return this.encoding;
    }

    public void setEncoding(final String enc) {
        this.encoding = enc;
    }

    public void setStandalone(final Boolean standalone) {
        this.standalone = standalone;
    }

    public Boolean getStandalone() {
        return this.standalone;
    }

    public String getName() {
        return "#document";
    }

    @Override
    public void addChild(final int index, final int type, final Object child) {
        if (type == 2) {
            this.rootIndex = index;
        }
        else if (this.rootIndex >= index) {
            ++this.rootIndex;
        }
        super.addChild(index, type, child);
    }

    @Override
    public void parse(final XmlPullParser parser) throws IOException, XmlPullParserException {
        parser.require(0, (String)null, (String)null);
        parser.nextToken();
        this.encoding = parser.getInputEncoding();
        this.standalone = (Boolean)parser.getProperty("http://xmlpull.org/v1/doc/properties.html#xmldecl-standalone");
        super.parse(parser);
        if (parser.getEventType() != 1) {
            throw new RuntimeException("Document end expected!");
        }
    }

    @Override
    public void removeChild(final int index) {
        if (index == this.rootIndex) {
            this.rootIndex = -1;
        }
        else if (index < this.rootIndex) {
            --this.rootIndex;
        }
        super.removeChild(index);
    }

    public Element getRootElement() {
        if (this.rootIndex == -1) {
            throw new RuntimeException("Document has no root element!");
        }
        return (Element)this.getChild(this.rootIndex);
    }

    @Override
    public void write(final XmlSerializer writer) throws IOException {
        writer.startDocument(this.encoding, this.standalone);
        this.writeChildren(writer);
        writer.endDocument();
    }
}
