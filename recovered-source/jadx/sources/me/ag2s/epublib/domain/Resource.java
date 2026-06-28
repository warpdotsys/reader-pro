package me.ag2s.epublib.domain;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.Reader;
import java.io.Serializable;
import me.ag2s.epublib.Constants;
import me.ag2s.epublib.util.IOUtil;
import me.ag2s.epublib.util.StringUtil;
import me.ag2s.epublib.util.commons.io.XmlStreamReader;

/* JADX INFO: loaded from: reader-pro-classes-3.2.14.jar:me/ag2s/epublib/domain/Resource.class */
public class Resource implements Serializable {
    private static final long serialVersionUID = 1043946707835004037L;
    private String id;
    private String title;
    private String href;
    private String properties;
    protected final String originalHref;
    private MediaType mediaType;
    private String inputEncoding;
    protected byte[] data;

    public Resource(String href) {
        this(null, new byte[0], href, MediaTypes.determineMediaType(href));
    }

    public Resource(byte[] data, MediaType mediaType) {
        this(null, data, null, mediaType);
    }

    public Resource(byte[] data, String href) {
        this((String) null, data, href, MediaTypes.determineMediaType(href), Constants.CHARACTER_ENCODING);
    }

    public Resource(Reader in, String href) throws IOException {
        this((String) null, IOUtil.toByteArray(in, Constants.CHARACTER_ENCODING), href, MediaTypes.determineMediaType(href), Constants.CHARACTER_ENCODING);
    }

    public Resource(InputStream in, String href) throws IOException {
        this(null, IOUtil.toByteArray(in), href, MediaTypes.determineMediaType(href));
    }

    public Resource(String id, byte[] data, String href, MediaType mediaType) {
        this(id, data, href, mediaType, Constants.CHARACTER_ENCODING);
    }

    public Resource(String id, byte[] data, String href, String originalHref, MediaType mediaType) {
        this(id, data, href, originalHref, mediaType, Constants.CHARACTER_ENCODING);
    }

    public Resource(String id, byte[] data, String href, MediaType mediaType, String inputEncoding) {
        this.id = id;
        this.href = href;
        this.originalHref = href;
        this.mediaType = mediaType;
        this.inputEncoding = inputEncoding;
        this.data = data;
    }

    public Resource(String id, byte[] data, String href, String originalHref, MediaType mediaType, String inputEncoding) {
        this.id = id;
        this.href = href;
        this.originalHref = originalHref;
        this.mediaType = mediaType;
        this.inputEncoding = inputEncoding;
        this.data = data;
    }

    public InputStream getInputStream() throws IOException {
        return new ByteArrayInputStream(getData());
    }

    public byte[] getData() throws IOException {
        return this.data;
    }

    public void close() {
    }

    public void setData(byte[] data) {
        this.data = data;
    }

    public long getSize() {
        return this.data.length;
    }

    public String getTitle() {
        return this.title;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getId() {
        return this.id;
    }

    public String getHref() {
        return this.href;
    }

    public void setHref(String href) {
        this.href = href;
    }

    public String getInputEncoding() {
        return this.inputEncoding;
    }

    public void setInputEncoding(String encoding) {
        this.inputEncoding = encoding;
    }

    public Reader getReader() throws IOException {
        return new XmlStreamReader(new ByteArrayInputStream(getData()), getInputEncoding());
    }

    public int hashCode() {
        return this.href.hashCode();
    }

    public boolean equals(Object resourceObject) {
        if (!(resourceObject instanceof Resource)) {
            return false;
        }
        return this.href.equals(((Resource) resourceObject).getHref());
    }

    public MediaType getMediaType() {
        return this.mediaType;
    }

    public void setMediaType(MediaType mediaType) {
        this.mediaType = mediaType;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getProperties() {
        return this.properties;
    }

    public void setProperties(String properties) {
        this.properties = properties;
    }

    public String toString() {
        Object[] objArr = new Object[12];
        objArr[0] = "id";
        objArr[1] = this.id;
        objArr[2] = "title";
        objArr[3] = this.title;
        objArr[4] = "encoding";
        objArr[5] = this.inputEncoding;
        objArr[6] = "mediaType";
        objArr[7] = this.mediaType;
        objArr[8] = "href";
        objArr[9] = this.href;
        objArr[10] = "size";
        objArr[11] = Integer.valueOf(this.data == null ? 0 : this.data.length);
        return StringUtil.toString(objArr);
    }
}
