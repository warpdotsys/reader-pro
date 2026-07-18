package me.ag2s.epublib.domain

import me.ag2s.epublib.util.StringUtil

object MediaTypes {
    @JvmField val XHTML = MediaType("application/xhtml+xml", ".xhtml", listOf(".htm", ".html", ".xhtml"))
    @JvmField val EPUB = MediaType("application/epub+zip", ".epub")
    @JvmField val NCX = MediaType("application/x-dtbncx+xml", ".ncx")
    @JvmField val JAVASCRIPT = MediaType("text/javascript", ".js")
    @JvmField val CSS = MediaType("text/css", ".css")
    @JvmField val JPG = MediaType("image/jpeg", ".jpg", listOf(".jpg", ".jpeg"))
    @JvmField val PNG = MediaType("image/png", ".png")
    @JvmField val GIF = MediaType("image/gif", ".gif")
    @JvmField val SVG = MediaType("image/svg+xml", ".svg")
    @JvmField val TTF = MediaType("application/x-truetype-font", ".ttf")
    @JvmField val OPENTYPE = MediaType("application/vnd.ms-opentype", ".otf")
    @JvmField val WOFF = MediaType("application/font-woff", ".woff")
    @JvmField val MP3 = MediaType("audio/mpeg", ".mp3")
    @JvmField val OGG = MediaType("audio/ogg", ".ogg")
    @JvmField val MP4 = MediaType("video/mp4", ".mp4")
    @JvmField val SMIL = MediaType("application/smil+xml", ".smil")
    @JvmField val XPGT = MediaType("application/adobe-page-template+xml", ".xpgt")
    @JvmField val PLS = MediaType("application/pls+xml", ".pls")
    @JvmField val mediaTypes = arrayOf(XHTML, EPUB, JPG, PNG, GIF, CSS, SVG, TTF, NCX, XPGT, OPENTYPE, WOFF, SMIL, PLS, JAVASCRIPT, MP3, MP4, OGG)
    @JvmField val mediaTypesByName: MutableMap<String?, MediaType> = HashMap<String?, MediaType>().apply { mediaTypes.forEach { put(it.getName(), it) } }
    @JvmStatic fun isBitmapImage(mediaType: MediaType?): Boolean = mediaType === JPG || mediaType === PNG || mediaType === GIF
    @JvmStatic fun determineMediaType(filename: String?): MediaType? = mediaTypesByName.values.firstOrNull { mediaType -> mediaType.getExtensions().any { StringUtil.endsWithIgnoreCase(filename, it) } }
    @JvmStatic fun getMediaTypeByName(mediaTypeName: String?): MediaType? = mediaTypesByName[mediaTypeName]
}
