// 
// Decompiled by Procyon v0.6.0
// 

package me.ag2s.epublib.domain;

import java.io.IOException;
import java.io.InputStream;
import java.util.zip.ZipFile;
import java.io.FilterInputStream;

public class ResourceInputStream extends FilterInputStream
{
    private final ZipFile zipFile;
    
    public ResourceInputStream(final InputStream in, final ZipFile zipFile) {
        super(in);
        this.zipFile = zipFile;
    }
    
    @Override
    public void close() throws IOException {
        super.close();
        this.zipFile.close();
    }
}
