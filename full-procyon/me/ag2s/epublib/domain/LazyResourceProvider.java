// 
// Decompiled by Procyon v0.6.0
// 

package me.ag2s.epublib.domain;

import java.io.IOException;
import java.io.InputStream;

public interface LazyResourceProvider
{
    InputStream getResourceStream(final String href) throws IOException;
}
