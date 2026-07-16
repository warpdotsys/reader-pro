package me.ag2s.epublib.epub;

import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;
import java.util.List;
import java.util.zip.ZipFile;
import java.util.zip.ZipInputStream;
import me.ag2s.epublib.domain.EpubBook;
import me.ag2s.epublib.domain.MediaType;
import me.ag2s.epublib.domain.MediaTypes;
import me.ag2s.epublib.domain.Resource;
import me.ag2s.epublib.domain.Resources;
import me.ag2s.epublib.util.ResourceUtil;
import me.ag2s.epublib.util.StringUtil;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

public class EpubReader {
   private static final String TAG = EpubReader.class.getName();
   private final BookProcessor bookProcessor = BookProcessor.IDENTITY_BOOKPROCESSOR;

   public EpubBook readEpub(InputStream in) throws IOException {
      return this.readEpub(in, "UTF-8");
   }

   public EpubBook readEpub(ZipInputStream in) throws IOException {
      return this.readEpub(in, "UTF-8");
   }

   public EpubBook readEpub(ZipFile zipfile) throws IOException {
      return this.readEpub(zipfile, "UTF-8");
   }

   public EpubBook readEpub(InputStream in, String encoding) throws IOException {
      return this.readEpub(new ZipInputStream(in), encoding);
   }

   public EpubBook readEpubLazy(ZipFile zipFile, String encoding) throws IOException {
      return this.readEpubLazy(zipFile, encoding, Arrays.asList(MediaTypes.mediaTypes));
   }

   public EpubBook readEpub(ZipInputStream in, String encoding) throws IOException {
      return this.readEpub(ResourcesLoader.loadResources(in, encoding));
   }

   public EpubBook readEpub(ZipFile in, String encoding) throws IOException {
      return this.readEpub(ResourcesLoader.loadResources(in, encoding));
   }

   public EpubBook readEpubLazy(ZipFile zipFile, String encoding, List<MediaType> lazyLoadedTypes) throws IOException {
      Resources resources = ResourcesLoader.loadResources(zipFile, encoding, lazyLoadedTypes);
      return this.readEpub(resources);
   }

   public EpubBook readEpub(Resources resources) {
      return this.readEpub(resources, new EpubBook());
   }

   public EpubBook readEpub(Resources resources, EpubBook result) {
      if (result == null) {
         result = new EpubBook();
      }

      this.handleMimeType(result, resources);
      String packageResourceHref = this.getPackageResourceHref(resources);
      Resource packageResource = this.processPackageResource(packageResourceHref, result, resources);
      result.setOpfResource(packageResource);
      Resource ncxResource = this.processNcxResource(packageResource, result);
      result.setNcxResource(ncxResource);
      return this.postProcessBook(result);
   }

   private EpubBook postProcessBook(EpubBook book) {
      if (this.bookProcessor != null) {
         book = this.bookProcessor.processBook(book);
      }

      return book;
   }

   private Resource processNcxResource(Resource packageResource, EpubBook book) {
      System.out.println(TAG + " OPF:getHref()" + packageResource.getHref());
      return book.isEpub3() ? NCXDocumentV3.read(book, this) : NCXDocumentV2.read(book, this);
   }

   private Resource processPackageResource(String packageResourceHref, EpubBook book, Resources resources) {
      Resource packageResource = resources.remove(packageResourceHref);

      try {
         PackageDocumentReader.read(packageResource, this, book, resources);
      } catch (Exception var6) {
         var6.printStackTrace();
      }

      return packageResource;
   }

   private String getPackageResourceHref(Resources resources) {
      String defaultResult = "OEBPS/content.opf";
      String result = defaultResult;
      Resource containerResource = resources.remove("META-INF/container.xml");
      if (containerResource == null) {
         return defaultResult;
      } else {
         try {
            Document document = ResourceUtil.getAsDocument(containerResource);
            Element rootFileElement = (Element)((Element)document.getDocumentElement().getElementsByTagName("rootfiles").item(0))
               .getElementsByTagName("rootfile")
               .item(0);
            result = rootFileElement.getAttribute("full-path");
         } catch (Exception var7) {
            var7.printStackTrace();
         }

         if (StringUtil.isBlank(result)) {
            result = defaultResult;
         }

         return result;
      }
   }

   private void handleMimeType(EpubBook result, Resources resources) {
      resources.remove("mimetype");
   }
}
