package me.ag2s.epublib.domain;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import me.ag2s.epublib.util.IOUtil;

public class LazyResource extends Resource {
   private static final long serialVersionUID = 5089400472352002866L;
   private final String TAG;
   private final LazyResourceProvider resourceProvider;
   private final long cachedSize;

   public LazyResource(LazyResourceProvider resourceProvider, String href) {
      this(resourceProvider, -1L, href);
   }

   public LazyResource(LazyResourceProvider resourceProvider, String href, String originalHref) {
      this(resourceProvider, -1L, href, originalHref);
   }

   public LazyResource(LazyResourceProvider resourceProvider, long size, String href) {
      super((String)null, (byte[])null, href, MediaTypes.determineMediaType(href));
      this.TAG = this.getClass().getName();
      this.resourceProvider = resourceProvider;
      this.cachedSize = size;
   }

   public LazyResource(LazyResourceProvider resourceProvider, long size, String href, String originalHref) {
      super((String)null, (byte[])null, href, (String)originalHref, (MediaType)MediaTypes.determineMediaType(href));
      this.TAG = this.getClass().getName();
      this.resourceProvider = resourceProvider;
      this.cachedSize = size;
   }

   public InputStream getInputStream() throws IOException {
      return (InputStream)(this.isInitialized() ? new ByteArrayInputStream(this.getData()) : this.resourceProvider.getResourceStream(this.originalHref));
   }

   public void initialize() throws IOException {
      this.getData();
   }

   public byte[] getData() throws IOException {
      if (this.data == null) {
         InputStream in = this.resourceProvider.getResourceStream(this.originalHref);
         byte[] readData = IOUtil.toByteArray(in, (int)this.cachedSize);
         if (readData == null) {
            throw new IOException("Could not load the contents of resource: " + this.getHref());
         }

         this.data = readData;
         in.close();
      }

      return this.data;
   }

   public void close() {
      if (this.resourceProvider != null) {
         this.data = null;
      }

   }

   public boolean isInitialized() {
      return this.data != null;
   }

   public long getSize() {
      return this.data != null ? (long)this.data.length : this.cachedSize;
   }
}
