// 
// Decompiled by Procyon v0.6.0
// 

package me.ag2s.epublib.domain;

import java.io.IOException;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.File;

public class FileResourceProvider implements LazyResourceProvider
{
    String dir;
    
    public FileResourceProvider(final String parentDir) {
        this.dir = parentDir;
    }
    
    public FileResourceProvider(final File parentFile) {
        this.dir = parentFile.getPath();
    }
    
    @Override
    public InputStream getResourceStream(final String href) throws IOException {
        return new FileInputStream(new File(this.dir, href));
    }
}
