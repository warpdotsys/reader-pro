// 
// Decompiled by Procyon v0.6.0
// 

package me.ag2s.epublib.domain;

import java.io.IOException;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;
import java.io.InputStream;

public class EpubResourceProvider implements LazyResourceProvider
{
    private final String epubFilename;
    
    public EpubResourceProvider(final String epubFilename) {
        this.epubFilename = epubFilename;
    }
    
    @Override
    public InputStream getResourceStream(final String href) throws IOException {
        final ZipFile zipFile = new ZipFile(this.epubFilename);
        final ZipEntry zipEntry = zipFile.getEntry(href);
        if (zipEntry == null) {
            zipFile.close();
            throw new IllegalStateException("Cannot find entry " + href + " in epub file " + this.epubFilename);
        }
        return new ResourceInputStream(zipFile.getInputStream(zipEntry), zipFile);
    }
}
