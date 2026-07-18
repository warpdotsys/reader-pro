package me.ag2s.epublib.domain;

import java.io.IOException;
import java.io.InputStream;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

public class EpubResourceProvider implements LazyResourceProvider {
   private final String epubFilename;

   public EpubResourceProvider(String epubFilename) {
      this.epubFilename = epubFilename;
   }

   public InputStream getResourceStream(String href) throws IOException {
      ZipFile zipFile = new ZipFile(this.epubFilename);
      ZipEntry zipEntry = zipFile.getEntry(href);
      if (zipEntry == null) {
         zipFile.close();
         throw new IllegalStateException("Cannot find entry " + href + " in epub file " + this.epubFilename);
      } else {
         return new ResourceInputStream(zipFile.getInputStream(zipEntry), zipFile);
      }
   }
}
