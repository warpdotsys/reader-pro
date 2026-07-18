package me.ag2s.epublib.epub;

import java.io.OutputStream;
import me.ag2s.epublib.domain.Resource;

public interface HtmlProcessor {
   void processHtmlResource(Resource resource, OutputStream out);
}
