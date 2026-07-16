//
// Decompiled by Procyon v0.6.0
//

package me.ag2s.epublib.epub;

import java.util.ArrayList;
import java.util.zip.ZipException;
import java.util.zip.ZipInputStream;
import me.ag2s.epublib.util.CollectionUtil;
import java.io.IOException;
import me.ag2s.epublib.domain.Resource;
import java.util.Enumeration;
import me.ag2s.epublib.domain.LazyResourceProvider;
import me.ag2s.epublib.domain.MediaTypes;
import me.ag2s.epublib.util.ResourceUtil;
import me.ag2s.epublib.domain.LazyResource;
import java.util.Collection;
import java.util.zip.ZipEntry;
import me.ag2s.epublib.domain.EpubResourceProvider;
import me.ag2s.epublib.domain.Resources;
import me.ag2s.epublib.domain.MediaType;
import java.util.List;
import java.util.zip.ZipFile;

public class ResourcesLoader
{
    private static final String TAG;

    public static Resources loadResources(final ZipFile zipFile, final String defaultHtmlEncoding, final List<MediaType> lazyLoadedTypes) throws IOException {
        final LazyResourceProvider resourceProvider = new EpubResourceProvider(zipFile.getName());
        final Resources result = new Resources();
        final Enumeration<? extends ZipEntry> entries = zipFile.entries();
        while (entries.hasMoreElements()) {
            final ZipEntry zipEntry = (ZipEntry)entries.nextElement();
            if (zipEntry != null) {
                if (zipEntry.isDirectory()) {
                    continue;
                }
                final String href = zipEntry.getName();
                Resource resource;
                if (shouldLoadLazy(href, lazyLoadedTypes)) {
                    resource = new LazyResource(resourceProvider, zipEntry.getSize(), href);
                }
                else {
                    resource = ResourceUtil.createResource(zipEntry, zipFile.getInputStream(zipEntry));
                    if (href.endsWith("opf")) {
                        final String string = new String(resource.getData()).replace("smlns=\"", "xmlns=\"");
                        resource.setData(string.getBytes());
                    }
                }
                if (resource.getMediaType() == MediaTypes.XHTML) {
                    resource.setInputEncoding(defaultHtmlEncoding);
                }
                result.add(resource);
            }
        }
        return result;
    }

    private static boolean shouldLoadLazy(final String href, final Collection<MediaType> lazilyLoadedMediaTypes) {
        if (CollectionUtil.isEmpty(lazilyLoadedMediaTypes)) {
            return false;
        }
        final MediaType mediaType = MediaTypes.determineMediaType(href);
        return lazilyLoadedMediaTypes.contains(mediaType);
    }

    public static Resources loadResources(final ZipInputStream zipInputStream, final String defaultHtmlEncoding) throws IOException {
        final Resources result = new Resources();
        ZipEntry zipEntry;
        do {
            zipEntry = getNextZipEntry(zipInputStream);
            if (zipEntry != null) {
                if (zipEntry.isDirectory()) {
                    continue;
                }
                final String href = zipEntry.getName();
                final Resource resource = ResourceUtil.createResource(zipEntry, zipInputStream);
                if (href.endsWith("opf")) {
                    final String string = new String(resource.getData()).replace("smlns=\"", "xmlns=\"");
                    resource.setData(string.getBytes());
                }
                if (resource.getMediaType() == MediaTypes.XHTML) {
                    resource.setInputEncoding(defaultHtmlEncoding);
                }
                result.add(resource);
            }
        } while (zipEntry != null);
        return result;
    }

    private static ZipEntry getNextZipEntry(final ZipInputStream zipInputStream) throws IOException {
        try {
            return zipInputStream.getNextEntry();
        }
        catch (final ZipException e) {
            e.printStackTrace();
            try {
                zipInputStream.closeEntry();
            }
            catch (final Exception ex) {}
            throw e;
        }
    }

    public static Resources loadResources(final ZipFile zipFile, final String defaultHtmlEncoding) throws IOException {
        final List<MediaType> ls = new ArrayList<MediaType>();
        return loadResources(zipFile, defaultHtmlEncoding, ls);
    }

    static {
        TAG = ResourcesLoader.class.getName();
    }
}
