// 
// Decompiled by Procyon v0.6.0
// 

package me.ag2s.epublib.epub;

import java.io.IOException;
import java.io.InputStream;
import java.util.Objects;
import java.net.URL;
import org.xml.sax.InputSource;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.DocumentBuilder;
import org.xml.sax.EntityResolver;
import org.xmlpull.v1.XmlPullParserFactory;
import java.io.UnsupportedEncodingException;
import java.io.Writer;
import java.io.OutputStreamWriter;
import org.xmlpull.v1.XmlSerializer;
import java.io.OutputStream;
import javax.xml.parsers.DocumentBuilderFactory;

public class EpubProcessorSupport
{
    private static final String TAG;
    protected static DocumentBuilderFactory documentBuilderFactory;
    
    private static void init() {
        (EpubProcessorSupport.documentBuilderFactory = DocumentBuilderFactory.newInstance()).setNamespaceAware(true);
        EpubProcessorSupport.documentBuilderFactory.setValidating(false);
    }
    
    public static XmlSerializer createXmlSerializer(final OutputStream out) throws UnsupportedEncodingException {
        return createXmlSerializer(new OutputStreamWriter(out, "UTF-8"));
    }
    
    public static XmlSerializer createXmlSerializer(final Writer out) {
        XmlSerializer result = null;
        try {
            final XmlPullParserFactory factory = XmlPullParserFactory.newInstance();
            factory.setValidating(true);
            result = factory.newSerializer();
            result.setFeature("http://xmlpull.org/v1/doc/features.html#indent-output", true);
            result.setOutput(out);
        }
        catch (final Exception e) {
            e.printStackTrace();
        }
        return result;
    }
    
    public static EntityResolver getEntityResolver() {
        return new EntityResolverImpl();
    }
    
    public DocumentBuilderFactory getDocumentBuilderFactory() {
        return EpubProcessorSupport.documentBuilderFactory;
    }
    
    public static DocumentBuilder createDocumentBuilder() {
        DocumentBuilder result = null;
        try {
            result = EpubProcessorSupport.documentBuilderFactory.newDocumentBuilder();
            result.setEntityResolver(getEntityResolver());
        }
        catch (final ParserConfigurationException e) {
            e.printStackTrace();
        }
        return result;
    }
    
    static {
        TAG = EpubProcessorSupport.class.getName();
        init();
    }
    
    static class EntityResolverImpl implements EntityResolver
    {
        private String previousLocation;
        
        @Override
        public InputSource resolveEntity(final String publicId, final String systemId) throws IOException {
            String resourcePath;
            if (systemId.startsWith("http:")) {
                final URL url = new URL(systemId);
                resourcePath = "dtd/" + url.getHost() + url.getPath();
                this.previousLocation = resourcePath.substring(0, resourcePath.lastIndexOf(47));
            }
            else {
                resourcePath = this.previousLocation + systemId.substring(systemId.lastIndexOf(47));
            }
            if (Objects.requireNonNull(this.getClass().getClassLoader()).getResource(resourcePath) == null) {
                throw new RuntimeException("remote resource is not cached : [" + systemId + "] cannot continue");
            }
            final InputStream in = Objects.requireNonNull(EpubProcessorSupport.class.getClassLoader()).getResourceAsStream(resourcePath);
            return new InputSource(in);
        }
    }
}
