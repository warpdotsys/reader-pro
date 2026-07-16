// 
// Decompiled by Procyon v0.6.0
// 

package me.ag2s.epublib.util;

import java.io.IOException;
import java.io.Writer;

public class NoCloseWriter extends Writer
{
    private final Writer writer;
    
    public NoCloseWriter(final Writer writer) {
        this.writer = writer;
    }
    
    @Override
    public void close() {
    }
    
    @Override
    public void flush() throws IOException {
        this.writer.flush();
    }
    
    @Override
    public void write(final char[] cbuf, final int off, final int len) throws IOException {
        this.writer.write(cbuf, off, len);
    }
}
