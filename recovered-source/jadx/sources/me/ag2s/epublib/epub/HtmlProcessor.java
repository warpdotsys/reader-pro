package me.ag2s.epublib.epub;

import java.io.OutputStream;
import me.ag2s.epublib.domain.Resource;

/* JADX INFO: loaded from: app-classes.jar:me/ag2s/epublib/epub/HtmlProcessor.class */
public interface HtmlProcessor {
    void processHtmlResource(Resource resource, OutputStream out);
}
