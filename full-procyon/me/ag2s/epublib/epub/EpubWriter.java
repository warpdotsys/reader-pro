// 
// Decompiled by Procyon v0.6.0
// 

package me.ag2s.epublib.epub;

import java.util.zip.CRC32;
import me.ag2s.epublib.domain.MediaTypes;
import java.io.Writer;
import java.io.OutputStreamWriter;
import org.xmlpull.v1.XmlSerializer;
import java.io.InputStream;
import me.ag2s.epublib.util.IOUtil;
import java.util.zip.ZipEntry;
import java.util.Iterator;
import me.ag2s.epublib.domain.Resource;
import java.io.IOException;
import java.util.zip.ZipOutputStream;
import java.io.OutputStream;
import me.ag2s.epublib.domain.EpubBook;

public class EpubWriter
{
    private static final String TAG;
    static final String EMPTY_NAMESPACE_PREFIX = "";
    private BookProcessor bookProcessor;
    
    public EpubWriter() {
        this(BookProcessor.IDENTITY_BOOKPROCESSOR);
    }
    
    public EpubWriter(final BookProcessor bookProcessor) {
        this.bookProcessor = bookProcessor;
    }
    
    public void write(EpubBook book, final OutputStream out) throws IOException {
        book = this.processBook(book);
        final ZipOutputStream resultStream = new ZipOutputStream(out);
        this.writeMimeType(resultStream);
        this.writeContainer(resultStream);
        this.initTOCResource(book);
        this.writeResources(book, resultStream);
        this.writePackageDocument(book, resultStream);
        resultStream.close();
    }
    
    private EpubBook processBook(EpubBook book) {
        if (this.bookProcessor != null) {
            book = this.bookProcessor.processBook(book);
        }
        return book;
    }
    
    private void initTOCResource(final EpubBook book) {
        try {
            Resource tocResource;
            if (book.isEpub3()) {
                tocResource = NCXDocumentV3.createNCXResource(book);
            }
            else {
                tocResource = NCXDocumentV2.createNCXResource(book);
            }
            final Resource currentTocResource = book.getSpine().getTocResource();
            if (currentTocResource != null) {
                book.getResources().remove(currentTocResource.getHref());
            }
            book.getSpine().setTocResource(tocResource);
            book.getResources().add(tocResource);
        }
        catch (final Exception e) {
            e.printStackTrace();
        }
    }
    
    private void writeResources(final EpubBook book, final ZipOutputStream resultStream) {
        for (final Resource resource : book.getResources().getAll()) {
            this.writeResource(resource, resultStream);
        }
    }
    
    private void writeResource(final Resource resource, final ZipOutputStream resultStream) {
        if (resource == null) {
            return;
        }
        try {
            resultStream.putNextEntry(new ZipEntry("OEBPS/" + resource.getHref()));
            final InputStream inputStream = resource.getInputStream();
            IOUtil.copy(inputStream, resultStream);
            inputStream.close();
        }
        catch (final Exception e) {
            e.printStackTrace();
        }
    }
    
    private void writePackageDocument(final EpubBook book, final ZipOutputStream resultStream) throws IOException {
        resultStream.putNextEntry(new ZipEntry("OEBPS/content.opf"));
        final XmlSerializer xmlSerializer = EpubProcessorSupport.createXmlSerializer(resultStream);
        PackageDocumentWriter.write(this, xmlSerializer, book);
        xmlSerializer.flush();
    }
    
    private void writeContainer(final ZipOutputStream resultStream) throws IOException {
        resultStream.putNextEntry(new ZipEntry("META-INF/container.xml"));
        final Writer out = new OutputStreamWriter(resultStream);
        out.write("<?xml version=\"1.0\"?>\n");
        out.write("<container version=\"1.0\" xmlns=\"urn:oasis:names:tc:opendocument:xmlns:container\">\n");
        out.write("\t<rootfiles>\n");
        out.write("\t\t<rootfile full-path=\"OEBPS/content.opf\" media-type=\"application/oebps-package+xml\"/>\n");
        out.write("\t</rootfiles>\n");
        out.write("</container>");
        out.flush();
    }
    
    private void writeMimeType(final ZipOutputStream resultStream) throws IOException {
        final ZipEntry mimetypeZipEntry = new ZipEntry("mimetype");
        mimetypeZipEntry.setMethod(0);
        final byte[] mimetypeBytes = MediaTypes.EPUB.getName().getBytes();
        mimetypeZipEntry.setSize(mimetypeBytes.length);
        mimetypeZipEntry.setCrc(this.calculateCrc(mimetypeBytes));
        resultStream.putNextEntry(mimetypeZipEntry);
        resultStream.write(mimetypeBytes);
    }
    
    private long calculateCrc(final byte[] data) {
        final CRC32 crc = new CRC32();
        crc.update(data);
        return crc.getValue();
    }
    
    String getNcxId() {
        return "ncx";
    }
    
    String getNcxHref() {
        return "toc.ncx";
    }
    
    String getNcxMediaType() {
        return MediaTypes.NCX.getName();
    }
    
    public BookProcessor getBookProcessor() {
        return this.bookProcessor;
    }
    
    public void setBookProcessor(final BookProcessor bookProcessor) {
        this.bookProcessor = bookProcessor;
    }
    
    static {
        TAG = EpubWriter.class.getName();
    }
}
