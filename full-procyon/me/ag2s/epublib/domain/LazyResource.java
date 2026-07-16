// 
// Decompiled by Procyon v0.6.0
// 

package me.ag2s.epublib.domain;

import me.ag2s.epublib.util.IOUtil;
import java.io.IOException;
import java.io.ByteArrayInputStream;
import java.io.InputStream;

public class LazyResource extends Resource
{
    private static final long serialVersionUID = 5089400472352002866L;
    private final String TAG;
    private final LazyResourceProvider resourceProvider;
    private final long cachedSize;
    
    public LazyResource(final LazyResourceProvider resourceProvider, final String href) {
        this(resourceProvider, -1L, href);
    }
    
    public LazyResource(final LazyResourceProvider resourceProvider, final String href, final String originalHref) {
        this(resourceProvider, -1L, href, originalHref);
    }
    
    public LazyResource(final LazyResourceProvider resourceProvider, final long size, final String href) {
        super(null, null, href, MediaTypes.determineMediaType(href));
        this.TAG = this.getClass().getName();
        this.resourceProvider = resourceProvider;
        this.cachedSize = size;
    }
    
    public LazyResource(final LazyResourceProvider resourceProvider, final long size, final String href, final String originalHref) {
        super(null, null, href, originalHref, MediaTypes.determineMediaType(href));
        this.TAG = this.getClass().getName();
        this.resourceProvider = resourceProvider;
        this.cachedSize = size;
    }
    
    @Override
    public InputStream getInputStream() throws IOException {
        if (this.isInitialized()) {
            return new ByteArrayInputStream(this.getData());
        }
        return this.resourceProvider.getResourceStream(this.originalHref);
    }
    
    public void initialize() throws IOException {
        this.getData();
    }
    
    @Override
    public byte[] getData() throws IOException {
        if (this.data == null) {
            final InputStream in = this.resourceProvider.getResourceStream(this.originalHref);
            final byte[] readData = IOUtil.toByteArray(in, (int)this.cachedSize);
            if (readData == null) {
                throw new IOException("Could not load the contents of resource: " + this.getHref());
            }
            this.data = readData;
            in.close();
        }
        return this.data;
    }
    
    @Override
    public void close() {
        if (this.resourceProvider != null) {
            this.data = null;
        }
    }
    
    public boolean isInitialized() {
        return this.data != null;
    }
    
    @Override
    public long getSize() {
        if (this.data != null) {
            return this.data.length;
        }
        return this.cachedSize;
    }
}
