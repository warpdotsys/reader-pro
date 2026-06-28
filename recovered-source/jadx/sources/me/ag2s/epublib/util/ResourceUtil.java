package me.ag2s.epublib.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.Reader;
import java.io.UnsupportedEncodingException;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;
import javax.xml.parsers.DocumentBuilder;
import me.ag2s.epublib.Constants;
import me.ag2s.epublib.domain.MediaType;
import me.ag2s.epublib.domain.MediaTypes;
import me.ag2s.epublib.domain.Resource;
import me.ag2s.epublib.epub.EpubProcessorSupport;
import me.ag2s.epublib.epub.PackageDocumentBase;
import org.w3c.dom.Document;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

/* JADX INFO: loaded from: reader-pro-classes-3.2.14.jar:me/ag2s/epublib/util/ResourceUtil.class */
public class ResourceUtil {
    public static Resource createChapterResource(String title, String txt, String model, String href) {
        String title2;
        if (title.contains("\n")) {
            title2 = "<span class=\"chapter-sequence-number\">" + title.replaceFirst("\\s*\\n\\s*", "</span><br />");
        } else {
            title2 = title.replaceFirst("\\s+", "</span><br />");
            if (title2.contains("</span>")) {
                title2 = "<span class=\"chapter-sequence-number\">" + title2;
            }
        }
        String html = model.replace("{title}", title2).replace("{content}", StringUtil.formatHtml(txt));
        return new Resource(html.getBytes(), href);
    }

    public static Resource createPublicResource(String name, String author, String intro, String kind, String wordCount, String model, String href) {
        String html = model.replace("{name}", name).replace("{author}", author).replace("{kind}", kind == null ? PackageDocumentBase.PREFIX_OPF : kind).replace("{wordCount}", wordCount == null ? PackageDocumentBase.PREFIX_OPF : wordCount).replace("{intro}", StringUtil.formatHtml(intro == null ? PackageDocumentBase.PREFIX_OPF : intro));
        return new Resource(html.getBytes(), href);
    }

    public static Resource createResource(File file) throws IOException {
        if (file == null) {
            return null;
        }
        MediaType mediaType = MediaTypes.determineMediaType(file.getName());
        byte[] data = IOUtil.toByteArray(new FileInputStream(file));
        return new Resource(data, mediaType);
    }

    public static Resource createResource(String title, String href) {
        String content = "<html><head><title>" + title + "</title></head><body><h1>" + title + "</h1></body></html>";
        return new Resource((String) null, content.getBytes(), href, MediaTypes.XHTML, Constants.CHARACTER_ENCODING);
    }

    public static Resource createResource(ZipEntry zipEntry, ZipInputStream zipInputStream) throws IOException {
        return new Resource(zipInputStream, zipEntry.getName());
    }

    public static Resource createResource(ZipEntry zipEntry, InputStream zipInputStream) throws IOException {
        return new Resource(zipInputStream, zipEntry.getName());
    }

    public static byte[] recode(String inputEncoding, String outputEncoding, byte[] input) throws UnsupportedEncodingException {
        return new String(input, inputEncoding).getBytes(outputEncoding);
    }

    public static InputSource getInputSource(Resource resource) throws IOException {
        Reader reader;
        if (resource == null || (reader = resource.getReader()) == null) {
            return null;
        }
        return new InputSource(reader);
    }

    public static Document getAsDocument(Resource resource) throws SAXException, IOException {
        return getAsDocument(resource, EpubProcessorSupport.createDocumentBuilder());
    }

    public static Document getAsDocument(Resource resource, DocumentBuilder documentBuilder) throws SAXException, IOException {
        InputSource inputSource = getInputSource(resource);
        if (inputSource == null) {
            return null;
        }
        return documentBuilder.parse(inputSource);
    }
}
