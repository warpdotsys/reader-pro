// 
// Decompiled by Procyon v0.6.0
// 

package me.ag2s.epublib.domain;

import java.util.HashMap;
import java.util.Iterator;
import me.ag2s.epublib.util.StringUtil;
import java.util.Map;

public class MediaTypes
{
    public static final MediaType XHTML;
    public static final MediaType EPUB;
    public static final MediaType NCX;
    public static final MediaType JAVASCRIPT;
    public static final MediaType CSS;
    public static final MediaType JPG;
    public static final MediaType PNG;
    public static final MediaType GIF;
    public static final MediaType SVG;
    public static final MediaType TTF;
    public static final MediaType OPENTYPE;
    public static final MediaType WOFF;
    public static final MediaType MP3;
    public static final MediaType OGG;
    public static final MediaType MP4;
    public static final MediaType SMIL;
    public static final MediaType XPGT;
    public static final MediaType PLS;
    public static final MediaType[] mediaTypes;
    public static final Map<String, MediaType> mediaTypesByName;
    
    public static boolean isBitmapImage(final MediaType mediaType) {
        return mediaType == MediaTypes.JPG || mediaType == MediaTypes.PNG || mediaType == MediaTypes.GIF;
    }
    
    public static MediaType determineMediaType(final String filename) {
        for (final MediaType mediaType : MediaTypes.mediaTypesByName.values()) {
            for (final String extension : mediaType.getExtensions()) {
                if (StringUtil.endsWithIgnoreCase(filename, extension)) {
                    return mediaType;
                }
            }
        }
        return null;
    }
    
    public static MediaType getMediaTypeByName(final String mediaTypeName) {
        return MediaTypes.mediaTypesByName.get(mediaTypeName);
    }
    
    static {
        XHTML = new MediaType("application/xhtml+xml", ".xhtml", new String[] { ".htm", ".html", ".xhtml" });
        EPUB = new MediaType("application/epub+zip", ".epub");
        NCX = new MediaType("application/x-dtbncx+xml", ".ncx");
        JAVASCRIPT = new MediaType("text/javascript", ".js");
        CSS = new MediaType("text/css", ".css");
        JPG = new MediaType("image/jpeg", ".jpg", new String[] { ".jpg", ".jpeg" });
        PNG = new MediaType("image/png", ".png");
        GIF = new MediaType("image/gif", ".gif");
        SVG = new MediaType("image/svg+xml", ".svg");
        TTF = new MediaType("application/x-truetype-font", ".ttf");
        OPENTYPE = new MediaType("application/vnd.ms-opentype", ".otf");
        WOFF = new MediaType("application/font-woff", ".woff");
        MP3 = new MediaType("audio/mpeg", ".mp3");
        OGG = new MediaType("audio/ogg", ".ogg");
        MP4 = new MediaType("video/mp4", ".mp4");
        SMIL = new MediaType("application/smil+xml", ".smil");
        XPGT = new MediaType("application/adobe-page-template+xml", ".xpgt");
        PLS = new MediaType("application/pls+xml", ".pls");
        mediaTypes = new MediaType[] { MediaTypes.XHTML, MediaTypes.EPUB, MediaTypes.JPG, MediaTypes.PNG, MediaTypes.GIF, MediaTypes.CSS, MediaTypes.SVG, MediaTypes.TTF, MediaTypes.NCX, MediaTypes.XPGT, MediaTypes.OPENTYPE, MediaTypes.WOFF, MediaTypes.SMIL, MediaTypes.PLS, MediaTypes.JAVASCRIPT, MediaTypes.MP3, MediaTypes.MP4, MediaTypes.OGG };
        mediaTypesByName = new HashMap<String, MediaType>();
        for (final MediaType mediaType : MediaTypes.mediaTypes) {
            MediaTypes.mediaTypesByName.put(mediaType.getName(), mediaType);
        }
    }
}
