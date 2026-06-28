package io.legado.app.utils;

import java.io.InputStream;
import java.util.LinkedHashMap;
import java.util.Map;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import kotlin.Metadata;
import kotlin.jvm.internal.Intrinsics;
import me.ag2s.epublib.epub.PackageDocumentBase;
import org.jetbrains.annotations.NotNull;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;

/* JADX INFO: compiled from: XmlUtils.kt */
/* JADX INFO: loaded from: app-classes.jar:io/legado/app/utils/XmlUtils.class */
@Metadata(mv = {1, 5, 1}, k = 1, xi = 48, d1 = {"\u0000\u001e\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\b\u0002\n\u0002\u0010%\n\u0002\u0010\u000e\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0003\bÆ\u0002\u0018\u00002\u00020\u0001B\u0007\b\u0002¢\u0006\u0002\u0010\u0002J\u001a\u0010\u0003\u001a\u000e\u0012\u0004\u0012\u00020\u0005\u0012\u0004\u0012\u00020\u00010\u00042\u0006\u0010\u0006\u001a\u00020\u0007J\u001a\u0010\b\u001a\u000e\u0012\u0004\u0012\u00020\u0005\u0012\u0004\u0012\u00020\u00010\u00042\u0006\u0010\t\u001a\u00020\u0001¨\u0006\n"}, d2 = {"Lio/legado/app/utils/XmlUtils;", PackageDocumentBase.PREFIX_OPF, "()V", "parseNode", PackageDocumentBase.PREFIX_OPF, PackageDocumentBase.PREFIX_OPF, "list", "Lorg/w3c/dom/NodeList;", "xml2map", PackageDocumentBase.DCTags.source, "reader-pro"})
public final class XmlUtils {

    @NotNull
    public static final XmlUtils INSTANCE = new XmlUtils();

    private XmlUtils() {
    }

    @NotNull
    public final Map<String, Object> xml2map(@NotNull Object source) {
        Intrinsics.checkNotNullParameter(source, PackageDocumentBase.DCTags.source);
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        Map doc = new LinkedHashMap();
        try {
            DocumentBuilder builder = factory.newDocumentBuilder();
            if (source instanceof String) {
                Document document = builder.parse((String) source);
                NodeList childNodes = document.getChildNodes();
                Intrinsics.checkNotNullExpressionValue(childNodes, "document.getChildNodes()");
                return parseNode(childNodes);
            }
            if (source instanceof InputStream) {
                Document document2 = builder.parse((InputStream) source);
                NodeList childNodes2 = document2.getChildNodes();
                Intrinsics.checkNotNullExpressionValue(childNodes2, "document.getChildNodes()");
                return parseNode(childNodes2);
            }
            if (source instanceof InputSource) {
                Document document3 = builder.parse((InputSource) source);
                NodeList childNodes3 = document3.getChildNodes();
                Intrinsics.checkNotNullExpressionValue(childNodes3, "document.getChildNodes()");
                return parseNode(childNodes3);
            }
            return doc;
        } catch (Exception e) {
            e.printStackTrace();
            return doc;
        }
    }

    @NotNull
    public final Map<String, Object> parseNode(@NotNull NodeList list) {
        Intrinsics.checkNotNullParameter(list, "list");
        Map doc = new LinkedHashMap();
        int i = 0;
        int length = list.getLength();
        if (0 < length) {
            do {
                int i2 = i;
                i++;
                Node node = list.item(i2);
                if (node.getNodeType() == 1) {
                    NodeList childNodes = node.getChildNodes();
                    if (childNodes.getLength() == 1 && node.getFirstChild().getNodeType() == 3) {
                        String nodeName = node.getNodeName();
                        Intrinsics.checkNotNullExpressionValue(nodeName, "node.getNodeName()");
                        String nodeValue = node.getFirstChild().getNodeValue();
                        Intrinsics.checkNotNullExpressionValue(nodeValue, "node.getFirstChild().getNodeValue()");
                        doc.put(nodeName, nodeValue);
                    } else if (childNodes.getLength() > 1) {
                        String nodeName2 = node.getNodeName();
                        Intrinsics.checkNotNullExpressionValue(nodeName2, "node.getNodeName()");
                        Intrinsics.checkNotNullExpressionValue(childNodes, "childNodes");
                        doc.put(nodeName2, parseNode(childNodes));
                    }
                }
            } while (i < length);
        }
        return doc;
    }
}
