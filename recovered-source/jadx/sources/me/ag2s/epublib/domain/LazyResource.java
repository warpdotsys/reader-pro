package me.ag2s.epublib.domain;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import me.ag2s.epublib.util.IOUtil;

/* JADX INFO: loaded from: reader-pro-classes-3.2.14.jar:me/ag2s/epublib/domain/LazyResource.class */
public class LazyResource extends Resource {
    private static final long serialVersionUID = 5089400472352002866L;
    private final String TAG;
    private final LazyResourceProvider resourceProvider;
    private final long cachedSize;

    public LazyResource(LazyResourceProvider resourceProvider, String href) {
        this(resourceProvider, -1L, href);
    }

    public LazyResource(LazyResourceProvider resourceProvider, String href, String originalHref) {
        this(resourceProvider, -1L, href, originalHref);
    }

    public LazyResource(LazyResourceProvider resourceProvider, long size, String href) {
        super(null, null, href, MediaTypes.determineMediaType(href));
        this.TAG = getClass().getName();
        this.resourceProvider = resourceProvider;
        this.cachedSize = size;
    }

    public LazyResource(LazyResourceProvider resourceProvider, long size, String href, String originalHref) {
        super((String) null, (byte[]) null, href, originalHref, MediaTypes.determineMediaType(href));
        this.TAG = getClass().getName();
        this.resourceProvider = resourceProvider;
        this.cachedSize = size;
    }

    @Override // me.ag2s.epublib.domain.Resource
    public InputStream getInputStream() throws IOException {
        if (isInitialized()) {
            return new ByteArrayInputStream(getData());
        }
        return this.resourceProvider.getResourceStream(this.originalHref);
    }

    public void initialize() throws IOException {
        getData();
    }

    @Override // me.ag2s.epublib.domain.Resource
    public byte[] getData() throws IOException {
        if (this.data == null) {
            InputStream in = this.resourceProvider.getResourceStream(this.originalHref);
            byte[] readData = IOUtil.toByteArray(in, (int) this.cachedSize);
            if (readData == null) {
                throw new IOException("Could not load the contents of resource: " + getHref());
            }
            this.data = readData;
            in.close();
        }
        return this.data;
    }

    @Override // me.ag2s.epublib.domain.Resource
    public void close() {
        if (this.resourceProvider != null) {
            this.data = null;
        }
    }

    public boolean isInitialized() {
        return this.data != null;
    }

    @Override // me.ag2s.epublib.domain.Resource
    public long getSize() {
        if (this.data != null) {
            return this.data.length;
        }
        return this.cachedSize;
    }
}
