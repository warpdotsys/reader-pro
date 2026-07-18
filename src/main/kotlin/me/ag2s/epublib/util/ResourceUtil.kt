package me.ag2s.epublib.util
import java.io.*
import java.util.zip.*
import javax.xml.parsers.DocumentBuilder
import me.ag2s.epublib.domain.*
import me.ag2s.epublib.epub.EpubProcessorSupport
import org.w3c.dom.Document
import org.xml.sax.InputSource
import java.nio.charset.Charset

object ResourceUtil {
    @JvmStatic fun createChapterResource(title:String, txt:String, model:String, href:String):Resource { val formatted=if(title.contains("\n")) "<span class=\"chapter-sequence-number\">"+title.replaceFirst(Regex("\\s*\\n\\s*"),"</span><br />") else title.replaceFirst(Regex("\\s+"),"</span><br />").let { if(it.contains("</span>")) "<span class=\"chapter-sequence-number\">$it" else it }; return Resource(model.replace("{title}",formatted).replace("{content}",StringUtil.formatHtml(txt)).toByteArray(),href) }
    @JvmStatic fun createPublicResource(name:String,author:String,intro:String?,kind:String?,wordCount:String?,model:String,href:String):Resource = Resource(model.replace("{name}",name).replace("{author}",author).replace("{kind}",kind?:"").replace("{wordCount}",wordCount?:"").replace("{intro}",StringUtil.formatHtml(intro?:"")).toByteArray(),href)
    @JvmStatic fun createResource(file:File?):Resource? = file?.let { Resource(IOUtil.toByteArray(FileInputStream(it)),MediaTypes.determineMediaType(it.name)) }
    @JvmStatic fun createResource(title:String,href:String):Resource = Resource(null,"<html><head><title>$title</title></head><body><h1>$title</h1></body></html>".toByteArray(),href,MediaTypes.XHTML,"UTF-8")
    @JvmStatic fun createResource(entry:ZipEntry,input:ZipInputStream):Resource = Resource(input,entry.name)
    @JvmStatic fun createResource(entry:ZipEntry,input:InputStream):Resource = Resource(input,entry.name)
    @JvmStatic fun recode(inputEncoding:String,outputEncoding:String,input:ByteArray):ByteArray = String(input,Charset.forName(inputEncoding)).toByteArray(Charset.forName(outputEncoding))
    @JvmStatic fun getInputSource(resource:Resource?):InputSource? = resource?.getReader()?.let(::InputSource)
    @JvmStatic fun getAsDocument(resource:Resource?):Document? = getAsDocument(resource,EpubProcessorSupport.createDocumentBuilder()!!)
    @JvmStatic fun getAsDocument(resource:Resource?,builder:DocumentBuilder):Document? = getInputSource(resource)?.let(builder::parse)
}
