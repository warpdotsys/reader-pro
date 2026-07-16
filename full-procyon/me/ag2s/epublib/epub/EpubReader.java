// 
// Decompiled by Procyon v0.6.0
// 

package me.ag2s.epublib.epub;

import org.w3c.dom.Document;
import me.ag2s.epublib.util.StringUtil;
import org.w3c.dom.Element;
import me.ag2s.epublib.util.ResourceUtil;
import me.ag2s.epublib.domain.Resource;
import me.ag2s.epublib.domain.Resources;
import me.ag2s.epublib.domain.MediaType;
import java.util.List;
import java.util.Arrays;
import me.ag2s.epublib.domain.MediaTypes;
import java.util.zip.ZipFile;
import java.util.zip.ZipInputStream;
import java.io.IOException;
import me.ag2s.epublib.domain.EpubBook;
import java.io.InputStream;

public class EpubReader
{
    private static final String TAG;
    private final BookProcessor bookProcessor;
    
    public EpubReader() {
        this.bookProcessor = BookProcessor.IDENTITY_BOOKPROCESSOR;
    }
    
    public EpubBook readEpub(final InputStream in) throws IOException {
        return this.readEpub(in, "UTF-8");
    }
    
    public EpubBook readEpub(final ZipInputStream in) throws IOException {
        return this.readEpub(in, "UTF-8");
    }
    
    public EpubBook readEpub(final ZipFile zipfile) throws IOException {
        return this.readEpub(zipfile, "UTF-8");
    }
    
    public EpubBook readEpub(final InputStream in, final String encoding) throws IOException {
        return this.readEpub(new ZipInputStream(in), encoding);
    }
    
    public EpubBook readEpubLazy(final ZipFile zipFile, final String encoding) throws IOException {
        return this.readEpubLazy(zipFile, encoding, Arrays.asList(MediaTypes.mediaTypes));
    }
    
    public EpubBook readEpub(final ZipInputStream in, final String encoding) throws IOException {
        return this.readEpub(ResourcesLoader.loadResources(in, encoding));
    }
    
    public EpubBook readEpub(final ZipFile in, final String encoding) throws IOException {
        return this.readEpub(ResourcesLoader.loadResources(in, encoding));
    }
    
    public EpubBook readEpubLazy(final ZipFile zipFile, final String encoding, final List<MediaType> lazyLoadedTypes) throws IOException {
        final Resources resources = ResourcesLoader.loadResources(zipFile, encoding, lazyLoadedTypes);
        return this.readEpub(resources);
    }
    
    public EpubBook readEpub(final Resources resources) {
        return this.readEpub(resources, new EpubBook());
    }
    
    public EpubBook readEpub(final Resources resources, EpubBook result) {
        if (result == null) {
            result = new EpubBook();
        }
        this.handleMimeType(result, resources);
        final String packageResourceHref = this.getPackageResourceHref(resources);
        final Resource packageResource = this.processPackageResource(packageResourceHref, result, resources);
        result.setOpfResource(packageResource);
        final Resource ncxResource = this.processNcxResource(packageResource, result);
        result.setNcxResource(ncxResource);
        result = this.postProcessBook(result);
        return result;
    }
    
    private EpubBook postProcessBook(EpubBook book) {
        if (this.bookProcessor != null) {
            book = this.bookProcessor.processBook(book);
        }
        return book;
    }
    
    private Resource processNcxResource(final Resource packageResource, final EpubBook book) {
        System.out.println(EpubReader.TAG + " OPF:getHref()" + packageResource.getHref());
        if (book.isEpub3()) {
            return NCXDocumentV3.read(book, this);
        }
        return NCXDocumentV2.read(book, this);
    }
    
    private Resource processPackageResource(final String packageResourceHref, final EpubBook book, final Resources resources) {
        final Resource packageResource = resources.remove(packageResourceHref);
        try {
            PackageDocumentReader.read(packageResource, this, book, resources);
        }
        catch (final Exception e) {
            e.printStackTrace();
        }
        return packageResource;
    }
    
    private String getPackageResourceHref(final Resources resources) {
        String result;
        final String defaultResult = result = "OEBPS/content.opf";
        final Resource containerResource = resources.remove("META-INF/container.xml");
        if (containerResource == null) {
            return result;
        }
        try {
            final Document document = ResourceUtil.getAsDocument(containerResource);
            final Element rootFileElement = (Element)((Element)document.getDocumentElement().getElementsByTagName("rootfiles").item(0)).getElementsByTagName("rootfile").item(0);
            result = rootFileElement.getAttribute("full-path");
        }
        catch (final Exception e) {
            e.printStackTrace();
        }
        if (StringUtil.isBlank(result)) {
            result = defaultResult;
        }
        return result;
    }
    
    private void handleMimeType(final EpubBook result, final Resources resources) {
        resources.remove("mimetype");
    }
    
    static {
        TAG = EpubReader.class.getName();
    }
}
