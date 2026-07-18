package me.ag2s.epublib.epub
import java.io.OutputStream
import me.ag2s.epublib.domain.Resource
fun interface HtmlProcessor { fun processHtmlResource(resource: Resource, output: OutputStream) }
