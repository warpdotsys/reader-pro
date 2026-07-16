// 
// Decompiled by Procyon v0.6.0
// 

package me.ag2s.epublib.epub;

import java.io.OutputStream;
import me.ag2s.epublib.domain.Resource;

public interface HtmlProcessor
{
    void processHtmlResource(final Resource resource, final OutputStream out);
}
