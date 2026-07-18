package me.ag2s.umdlib.domain

import java.io.ByteArrayOutputStream
import java.io.File
import java.util.zip.DeflaterOutputStream
import me.ag2s.umdlib.tool.UmdUtils
import me.ag2s.umdlib.tool.WrapOutputStream

class UmdChapters {
    private var totalContentLen = 0
    private val titles = mutableListOf<ByteArray>()
    val contentLengths = mutableListOf<Int>()
    val contents = ByteArrayOutputStream()
    fun addTitle(value: String) { titles += UmdUtils.stringToUnicodeBytes(value) }
    fun addTitle(value: ByteArray) { titles += value }
    fun addContentLength(value: Int) { contentLengths += value }
    fun getContentLength(index: Int): Int = contentLengths[index]
    fun getContent(index: Int): ByteArray { val start=contentLengths[index]; val end=if(index+1<contentLengths.size) contentLengths[index+1] else totalContentLen; return contents.toByteArray().copyOfRange(start,end) }
    fun getContentString(index: Int): String = UmdUtils.unicodeBytesToString(getContent(index)).replace('\u2029','\n')
    fun getTitle(index: Int): String = UmdUtils.unicodeBytesToString(titles[index])
    fun addChapter(title: String, content: String) { titles += UmdUtils.stringToUnicodeBytes(title); val bytes=UmdUtils.stringToUnicodeBytes(content); contentLengths += bytes.size; contents.write(bytes) }
    fun addFile(file: File, title: String) = addChapter(title,String(UmdUtils.readFile(file)))
    fun addFile(file: File) = addFile(file,file.name.substringBeforeLast('.',file.name))
    fun clearChapters() { titles.clear(); contentLengths.clear(); contents.reset() }
    fun getTotalContentLen(): Int = totalContentLen
    fun setTotalContentLen(value: Int) { totalContentLen=value }
    fun buildChapters(wos: WrapOutputStream) {
        wos.writeBytes(35,11,0,0,9); wos.writeInt(contents.size())
        wos.writeBytes(35,131,0,0,9); val random=UmdUtils.genRandomBytes(4); wos.writeBytes(random); wos.write(36); wos.writeBytes(random); wos.writeInt(contentLengths.size*4+9); var offset=0; contentLengths.forEach { wos.writeInt(offset); offset+=it }
        wos.writeBytes(35,132,0,1,9); val titleRandom=UmdUtils.genRandomBytes(4); wos.writeBytes(titleRandom); wos.write(36); wos.writeBytes(titleRandom); wos.writeInt(titles.sumOf{it.size}+titles.size+9); titles.forEach { wos.writeByte(it.size); wos.write(it) }
        val chunks=mutableListOf<ByteArray>(); val bytes=contents.toByteArray(); var start=0
        while(start<bytes.size){ val len=minOf(32768,bytes.size-start); val compressed=ByteArrayOutputStream().also { DeflaterOutputStream(it).use { zip->zip.write(bytes,start,len) } }.toByteArray(); val chunkRandom=UmdUtils.genRandomBytes(4); wos.write(36); wos.writeBytes(chunkRandom); chunks+=chunkRandom; wos.writeInt(compressed.size+9); wos.write(compressed); wos.writeBytes(35,241,0,0,21); wos.write(ByteArray(16)); start+=len }
        wos.writeBytes(35,129,0,1,9,0,0,0,0,36,0,0,0,0); wos.writeInt(chunks.size*4+9); chunks.asReversed().forEach(wos::writeBytes)
    }
}
