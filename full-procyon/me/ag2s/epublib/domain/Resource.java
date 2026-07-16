// 
// Decompiled by Procyon v0.6.0
// 

package me.ag2s.epublib.domain;

import me.ag2s.epublib.util.StringUtil;
import me.ag2s.epublib.util.commons.io.XmlStreamReader;
import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.io.IOException;
import me.ag2s.epublib.util.IOUtil;
import java.io.Reader;
import java.io.Serializable;

public class Resource implements Serializable
{
    private static final long serialVersionUID = 1043946707835004037L;
    private String id;
    private String title;
    private String href;
    private String properties;
    protected final String originalHref;
    private MediaType mediaType;
    private String inputEncoding;
    protected byte[] data;
    
    public Resource(final String href) {
        this(null, new byte[0], href, MediaTypes.determineMediaType(href));
    }
    
    public Resource(final byte[] data, final MediaType mediaType) {
        this(null, data, null, mediaType);
    }
    
    public Resource(final byte[] data, final String href) {
        this(null, data, href, MediaTypes.determineMediaType(href), "UTF-8");
    }
    
    public Resource(final Reader in, final String href) throws IOException {
        this(null, IOUtil.toByteArray(in, "UTF-8"), href, MediaTypes.determineMediaType(href), "UTF-8");
    }
    
    public Resource(final InputStream in, final String href) throws IOException {
        this(null, IOUtil.toByteArray(in), href, MediaTypes.determineMediaType(href));
    }
    
    public Resource(final String id, final byte[] data, final String href, final MediaType mediaType) {
        this(id, data, href, mediaType, "UTF-8");
    }
    
    public Resource(final String id, final byte[] data, final String href, final String originalHref, final MediaType mediaType) {
        this(id, data, href, originalHref, mediaType, "UTF-8");
    }
    
    public Resource(final String id, final byte[] data, final String href, final MediaType mediaType, final String inputEncoding) {
        this.id = id;
        this.href = href;
        this.originalHref = href;
        this.mediaType = mediaType;
        this.inputEncoding = inputEncoding;
        this.data = data;
    }
    
    public Resource(final String id, final byte[] data, final String href, final String originalHref, final MediaType mediaType, final String inputEncoding) {
        this.id = id;
        this.href = href;
        this.originalHref = originalHref;
        this.mediaType = mediaType;
        this.inputEncoding = inputEncoding;
        this.data = data;
    }
    
    public InputStream getInputStream() throws IOException {
        return new ByteArrayInputStream(this.getData());
    }
    
    public byte[] getData() throws IOException {
        return this.data;
    }
    
    public void close() {
    }
    
    public void setData(final byte[] data) {
        this.data = data;
    }
    
    public long getSize() {
        return this.data.length;
    }
    
    public String getTitle() {
        return this.title;
    }
    
    public void setId(final String id) {
        this.id = id;
    }
    
    public String getId() {
        return this.id;
    }
    
    public String getHref() {
        return this.href;
    }
    
    public void setHref(final String href) {
        this.href = href;
    }
    
    public String getInputEncoding() {
        return this.inputEncoding;
    }
    
    public void setInputEncoding(final String encoding) {
        this.inputEncoding = encoding;
    }
    
    public Reader getReader() throws IOException {
        return new XmlStreamReader(new ByteArrayInputStream(this.getData()), this.getInputEncoding());
    }
    
    @Override
    public int hashCode() {
        return this.href.hashCode();
    }
    
    @Override
    public boolean equals(final Object resourceObject) {
        return resourceObject instanceof Resource && this.href.equals(((Resource)resourceObject).getHref());
    }
    
    public MediaType getMediaType() {
        return this.mediaType;
    }
    
    public void setMediaType(final MediaType mediaType) {
        this.mediaType = mediaType;
    }
    
    public void setTitle(final String title) {
        this.title = title;
    }
    
    public String getProperties() {
        return this.properties;
    }
    
    public void setProperties(final String properties) {
        this.properties = properties;
    }
    
    @Override
    public String toString() {
        return StringUtil.toString("id", this.id, "title", this.title, "encoding", this.inputEncoding, "mediaType", this.mediaType, "href", this.href, "size", (this.data == null) ? 0 : this.data.length);
    }
}
