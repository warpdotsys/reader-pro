package me.ag2s.epublib.epub;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.nio.charset.StandardCharsets;
import javax.xml.parsers.DocumentBuilder;
import org.junit.jupiter.api.Test;
import org.w3c.dom.Document;
import org.xmlpull.v1.XmlSerializer;

class EpubRuntimeResourcesTest {

    @Test
    void createsTargetKxmlSerializerThroughXmlPullProvider() throws Exception {
        ByteArrayOutputStream output = new ByteArrayOutputStream();

        XmlSerializer serializer = EpubProcessorSupport.createXmlSerializer(output);

        assertNotNull(serializer);
        assertEquals("org.kxml2.io.KXmlSerializer", serializer.getClass().getName());
        serializer.startDocument(StandardCharsets.UTF_8.name(), null);
        serializer.startTag(null, "chapter").text("reader").endTag(null, "chapter");
        serializer.endDocument();
        assertEquals(
                "<?xml version='1.0' encoding='UTF-8' ?>\r\n<chapter>reader</chapter>",
                output.toString(StandardCharsets.UTF_8)
        );
    }

    @Test
    void resolvesNcxDtdFromPackagedResources() throws Exception {
        String ncx = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>"
                + "<!DOCTYPE ncx PUBLIC \"-//NISO//DTD ncx 2005-1//EN\" "
                + "\"http://www.daisy.org/z3986/2005/ncx-2005-1.dtd\">"
                + "<ncx xmlns=\"http://www.daisy.org/z3986/2005/ncx/\" version=\"2005-1\">"
                + "<head/><docTitle><text>Reader</text></docTitle><navMap/></ncx>";
        DocumentBuilder builder = EpubProcessorSupport.createDocumentBuilder();

        assertNotNull(builder);
        Document document = builder.parse(
                new ByteArrayInputStream(ncx.getBytes(StandardCharsets.UTF_8))
        );

        assertEquals("ncx", document.getDocumentElement().getLocalName());
    }
}
