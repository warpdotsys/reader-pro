//
// Decompiled by Procyon v0.6.0
//

package me.ag2s.epublib.util;

import javax.xml.parsers.DocumentBuilder;
import org.xml.sax.SAXException;
import me.ag2s.epublib.epub.EpubProcessorSupport;
import org.w3c.dom.Document;
import java.io.Reader;
import org.xml.sax.InputSource;
import java.io.UnsupportedEncodingException;
import java.util.zip.ZipInputStream;
import java.util.zip.ZipEntry;
import java.io.IOException;
import me.ag2s.epublib.domain.MediaType;
import java.io.InputStream;
import java.io.FileInputStream;
import me.ag2s.epublib.domain.MediaTypes;
import java.io.File;
import me.ag2s.epublib.domain.Resource;

public class ResourceUtil
{
    public static Resource createChapterResource(String title, final String txt, final String model, final String href) {
        if (title.contains("\n")) {
            title = "<span class=\"chapter-sequence-number\">" + title.replaceFirst("\\s*\\n\\s*", "</span><br />");
        }
        else {
            title = title.replaceFirst("\\s+", "</span><br />");
            if (title.contains("</span>")) {
                title = "<span class=\"chapter-sequence-number\">" + title;
            }
        }
        final String html = model.replace("{title}", title).replace("{content}", StringUtil.formatHtml(txt));
        return new Resource(html.getBytes(), href);
    }

    public static Resource createPublicResource(final String name, final String author, final String intro, final String kind, final String wordCount, final String model, final String href) {
        final String html = model.replace("{name}", name).replace("{author}", author).replace("{kind}", (kind == null) ? "" : kind).replace("{wordCount}", (wordCount == null) ? "" : wordCount).replace("{intro}", StringUtil.formatHtml((intro == null) ? "" : intro));
        return new Resource(html.getBytes(), href);
    }

    public static Resource createResource(final File file) throws IOException {
        if (file == null) {
            return null;
        }
        final MediaType mediaType = MediaTypes.determineMediaType(file.getName());
        final byte[] data = IOUtil.toByteArray(new FileInputStream(file));
        return new Resource(data, mediaType);
    }

    public static Resource createResource(final String title, final String href) {
        final String content = "<html><head><title>" + title + "</title></head><body><h1>" + title + "</h1></body></html>";
        return new Resource(null, content.getBytes(), href, MediaTypes.XHTML, "UTF-8");
    }

    public static Resource createResource(final ZipEntry zipEntry, final ZipInputStream zipInputStream) throws IOException {
        return new Resource(zipInputStream, zipEntry.getName());
    }

    public static Resource createResource(final ZipEntry zipEntry, final InputStream zipInputStream) throws IOException {
        return new Resource(zipInputStream, zipEntry.getName());
    }

    public static byte[] recode(final String inputEncoding, final String outputEncoding, final byte[] input) throws UnsupportedEncodingException {
        return new String(input, inputEncoding).getBytes(outputEncoding);
    }

    public static InputSource getInputSource(final Resource resource) throws IOException {
        if (resource == null) {
            return null;
        }
        final Reader reader = resource.getReader();
        if (reader == null) {
            return null;
        }
        return new InputSource(reader);
    }

    public static Document getAsDocument(final Resource resource) throws SAXException, IOException {
        return getAsDocument(resource, EpubProcessorSupport.createDocumentBuilder());
    }

    public static Document getAsDocument(final Resource resource, final DocumentBuilder documentBuilder) throws UnsupportedEncodingException, SAXException, IOException {
        final InputSource inputSource = getInputSource(resource);
        if (inputSource == null) {
            return null;
        }
        return documentBuilder.parse(inputSource);
    }
}
