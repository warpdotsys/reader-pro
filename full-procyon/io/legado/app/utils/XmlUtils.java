// 
// Decompiled by Procyon v0.6.0
// 

package io.legado.app.utils;

import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.w3c.dom.Document;
import javax.xml.parsers.DocumentBuilder;
import org.xml.sax.InputSource;
import java.io.InputStream;
import java.util.LinkedHashMap;
import javax.xml.parsers.DocumentBuilderFactory;
import kotlin.jvm.internal.Intrinsics;
import java.util.Map;
import org.jetbrains.annotations.NotNull;
import kotlin.Metadata;

@Metadata(mv = { 1, 5, 1 }, k = 1, xi = 48, d1 = { "\u0000\u001e\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\b\u0002\n\u0002\u0010%\n\u0002\u0010\u000e\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0003\b\u00c6\u0002\u0018\u00002\u00020\u0001B\u0007\b\u0002?\u0006\u0002\u0010\u0002J\u001a\u0010\u0003\u001a\u000e\u0012\u0004\u0012\u00020\u0005\u0012\u0004\u0012\u00020\u00010\u00042\u0006\u0010\u0006\u001a\u00020\u0007J\u001a\u0010\b\u001a\u000e\u0012\u0004\u0012\u00020\u0005\u0012\u0004\u0012\u00020\u00010\u00042\u0006\u0010\t\u001a\u00020\u0001¡§\u0006\n" }, d2 = { "Lio/legado/app/utils/XmlUtils;", "", "()V", "parseNode", "", "", "list", "Lorg/w3c/dom/NodeList;", "xml2map", "source", "reader-pro" })
public final class XmlUtils
{
    @NotNull
    public static final XmlUtils INSTANCE;
    
    private XmlUtils() {
    }
    
    @NotNull
    public final Map<String, Object> xml2map(@NotNull final Object source) {
        Intrinsics.checkNotNullParameter(source, "source");
        final DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        final Map doc = new LinkedHashMap();
        try {
            final DocumentBuilder builder = factory.newDocumentBuilder();
            if (source instanceof String) {
                final Document document = builder.parse((String)source);
                final NodeList childNodes = document.getChildNodes();
                Intrinsics.checkNotNullExpressionValue((Object)childNodes, "document.getChildNodes()");
                return this.parseNode(childNodes);
            }
            if (source instanceof InputStream) {
                final Document document = builder.parse((InputStream)source);
                final NodeList childNodes2 = document.getChildNodes();
                Intrinsics.checkNotNullExpressionValue((Object)childNodes2, "document.getChildNodes()");
                return this.parseNode(childNodes2);
            }
            if (source instanceof InputSource) {
                final Document document = builder.parse((InputSource)source);
                final NodeList childNodes3 = document.getChildNodes();
                Intrinsics.checkNotNullExpressionValue((Object)childNodes3, "document.getChildNodes()");
                return this.parseNode(childNodes3);
            }
            return doc;
        }
        catch (final Exception e) {
            e.printStackTrace();
            return doc;
        }
    }
    
    @NotNull
    public final Map<String, Object> parseNode(@NotNull final NodeList list) {
        Intrinsics.checkNotNullParameter((Object)list, "list");
        final Map doc = new LinkedHashMap();
        int j = 0;
        final int length = list.getLength();
        if (j < length) {
            do {
                final int i = j;
                ++j;
                final Node node = list.item(i);
                if (node.getNodeType() == 1) {
                    final NodeList childNodes = node.getChildNodes();
                    if (childNodes.getLength() == 1 && node.getFirstChild().getNodeType() == 3) {
                        final Map map = doc;
                        final String nodeName = node.getNodeName();
                        Intrinsics.checkNotNullExpressionValue((Object)nodeName, "node.getNodeName()");
                        final String s = nodeName;
                        final String nodeValue = node.getFirstChild().getNodeValue();
                        Intrinsics.checkNotNullExpressionValue((Object)nodeValue, "node.getFirstChild().getNodeValue()");
                        map.put(s, nodeValue);
                    }
                    else {
                        if (childNodes.getLength() <= 1) {
                            continue;
                        }
                        final Map map2 = doc;
                        final String nodeName2 = node.getNodeName();
                        Intrinsics.checkNotNullExpressionValue((Object)nodeName2, "node.getNodeName()");
                        final String s2 = nodeName2;
                        Intrinsics.checkNotNullExpressionValue((Object)childNodes, "childNodes");
                        map2.put(s2, this.parseNode(childNodes));
                    }
                }
            } while (j < length);
        }
        return doc;
    }
    
    static {
        INSTANCE = new XmlUtils();
    }
}
