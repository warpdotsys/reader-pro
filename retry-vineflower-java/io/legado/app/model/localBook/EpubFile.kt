package io.legado.app.model.localBook

import io.legado.app.data.entities.Book
import io.legado.app.data.entities.BookChapter
import io.legado.app.utils.FileUtils
import io.legado.app.utils.HtmlFormatter
import io.legado.app.utils.MD5Utils
import java.io.File
import java.io.IOException
import java.io.InputStream
import java.nio.charset.Charset
import java.nio.file.Paths
import java.util.ArrayList
import java.util.LinkedHashMap
import java.util.zip.ZipFile
import kotlin.jvm.internal.Intrinsics
import me.ag2s.epublib.domain.EpubBook
import me.ag2s.epublib.domain.Metadata
import me.ag2s.epublib.domain.Resource
import me.ag2s.epublib.domain.Resources
import me.ag2s.epublib.domain.Spine
import me.ag2s.epublib.domain.SpineReference
import me.ag2s.epublib.domain.TableOfContents
import me.ag2s.epublib.epub.EpubReader
import org.jsoup.Jsoup
import org.jsoup.nodes.Element
import org.jsoup.select.Elements

public class EpubFile(book: Book) {
   public final var book: Book
      internal set

   private final var epubBook: EpubBook?
      private final get() {
         if (this.epubBook != null) {
            return this.epubBook;
         } else {
            this.epubBook = this.readEpub();
            return this.epubBook;
         }
      }


   private final var mCharset: Charset

   init {
      this.book = book;
      val e: Charset = Charset.defaultCharset();
      this.mCharset = e;

      try {
         if (this.getEpubBook() != null) {
            ;
         }
      } catch (var8: Exception) {
         var8.printStackTrace();
      }
   }

   private fun readEpub(): EpubBook? {
      try {
         return new EpubReader().readEpubLazy(new ZipFile(this.book.getLocalFile()), "utf-8");
      } catch (var2: Exception) {
         var2.printStackTrace();
         return null;
      }
   }

   private fun getContent(chapter: BookChapter): String? {
      if (StringsKt.contains$default(chapter.getUrl(), "titlepage.xhtml", false, 2, null)) {
         return "<img src=\"cover.jpeg\" />";
      } else {
         val var2: EpubBook = this.getEpubBook();
         if (var2 == null) {
            return null;
         } else {
            val nextUrl: java.lang.String = chapter.getVariable("nextUrl");
            val startFragmentId: java.lang.String = chapter.getStartFragmentId();
            val endFragmentId: java.lang.String = chapter.getEndFragmentId();
            val elements: Elements = new Elements();
            var isChapter: Boolean = false;

            for (Resource res : var2.getContents()) {
               if (StringsKt.substringBeforeLast$default(chapter.getUrl(), "#", null, 2, null) == res.getHref()) {
                  elements.add(this.getBody(res, startFragmentId, endFragmentId));
                  isChapter = true;
                  if (nextUrl == null || res.getHref() == StringsKt.substringBeforeLast$default(nextUrl, "#", null, 2, null)) {
                     break;
                  }
               } else if (isChapter) {
                  if (res.getHref() == (if (nextUrl == null) null else StringsKt.substringBeforeLast$default(nextUrl, "#", null, 2, null))) {
                     break;
                  }

                  elements.add(this.getBody(res, startFragmentId, endFragmentId));
               }
            }

            var var22: java.lang.String = elements.outerHtml();
            if (this.getBook().getDelTag(4L)) {
               var22 = new Regex("<ruby>\\s?([\\u4e00-\\u9fa5])\\s?.*?</ruby>").replace(var22, "$1");
            }

            return HtmlFormatter.formatKeepImg$default(HtmlFormatter.INSTANCE, var22, null, 2, null);
         }
      }
   }

   private fun getBody(res: Resource, startFragmentId: String?, endFragmentId: String?): Element {
      val tag: ByteArray = res.getData();
      val body: Element = Jsoup.parse(new java.lang.String(tag, this.mCharset)).body();
      if (startFragmentId != null && !StringsKt.isBlank(startFragmentId)) {
         val var12: Element = body.getElementById(startFragmentId);
         if (var12 != null) {
            val var17: Elements = var12.previousElementSiblings();
            if (var17 != null) {
               var17.remove();
            }
         }
      }

      if (endFragmentId != null && !StringsKt.isBlank(endFragmentId) && !(endFragmentId == startFragmentId)) {
         val var14: Element = body.getElementById(endFragmentId);
         if (var14 != null) {
            var14.nextElementSiblings().remove();
            var14.remove();
         }
      }

      if (this.book.getDelTag(2L)) {
         body.getElementsByTag("h1").remove();
         body.getElementsByTag("h2").remove();
         body.getElementsByTag("h3").remove();
         body.getElementsByTag("h4").remove();
         body.getElementsByTag("h5").remove();
         body.getElementsByTag("h6").remove();
      }

      val var22: Elements = body.children();
      var22.select("script").remove();
      var22.select("style").remove();
      return body;
   }

   private fun getImage(href: String): InputStream? {
      val abHref: java.lang.String = StringsKt.replace$default(href, "../", "", false, 4, null);
      val var3: EpubBook = this.getEpubBook();
      val var10000: InputStream;
      if (var3 == null) {
         var10000 = null;
      } else {
         val var4: Resources = var3.getResources();
         if (var4 == null) {
            var10000 = null;
         } else {
            val var5: Resource = var4.getByHref(abHref);
            var10000 = if (var5 == null) null else var5.getInputStream();
         }
      }

      return var10000;
   }

   private fun upBookInfo() {
      if (this.getEpubBook() == null) {
         eFile = null;
         this.book.setIntro("书籍导入异常");
      } else {
         val var10000: EpubBook = this.getEpubBook();
         val metadata: Metadata = var10000.getMetadata();
         val var13: Book = this.book;
         val author: java.lang.String = metadata.getFirstTitle();
         var13.setName(author);
         if (this.book.getName().length() == 0) {
            this.book.setName(StringsKt.replace$default(this.book.getOriginName(), ".epub", "", false, 4, null));
         }

         if (metadata.getAuthors().size() > 0) {
            val var9: java.lang.String = metadata.getAuthors().get(0).toString();
            this.book.setAuthor(new Regex("^, |, $").replace(var9, ""));
         }

         if (metadata.getDescriptions().size() > 0) {
            this.book.setIntro(Jsoup.parse(metadata.getDescriptions().get(0)).text());
         }

         this.updateCover();
      }
   }

   public fun updateCover() {
      val relativeCoverUrl: java.lang.String = Paths.get(
            "assets", this.book.getUserNameSpace(), "covers", Intrinsics.stringPlus(MD5Utils.INSTANCE.md5Encode16(this.book.getBookUrl()), ".jpg")
         )
         .toString();
      this.book.setCoverUrl(Intrinsics.stringPlus("/", StringsKt.replace$default(relativeCoverUrl, "\\", "/", false, 4, null)));
      val var12: java.lang.String = Paths.get(this.book.workRoot(), "storage", relativeCoverUrl).toString();
      if (!new File(var12).exists()) {
         val var13: EpubBook = this.getEpubBook();
         if (var13 != null) {
            val var5: Resource = var13.getCoverImage();
            if (var5 != null) {
               val var6: ByteArray = var5.getData();
               if (var6 != null) {
                  FileUtils.INSTANCE.writeBytes(var12, var6);
               }
            }
         }
      }
   }

   public fun getChapterListBySpine(): ArrayList<BookChapter> {
      val chapterList: ArrayList = new ArrayList();
      val var2: EpubBook = this.getEpubBook();
      if (var2 != null) {
         val var3: Spine = var2.getSpine();
         if (var3 != null) {
            val var4: java.util.List = var3.getSpineReferences();
            if (var4 != null) {
               val `$this$forEachIndexed$iv`: java.lang.Iterable = var4;
               var `index$iv`: Int = 0;

               for (Object item$iv : $this$forEachIndexed$iv) {
                  val var10: Int = `index$iv`++;
                  if (var10 < 0) {
                     CollectionsKt.throwIndexOverflow();
                  }

                  val resource: Resource = (`item$iv` as SpineReference).getResource();
                  var title: java.lang.String = resource.getTitle();
                  if (title == null || title.length() == 0) {
                     try {
                        val var25: ByteArray = resource.getData();
                        val var26: Elements = Jsoup.parse(new java.lang.String(var25, this.mCharset)).getElementsByTag("title");
                        if (var26.size() > 0) {
                           title = var26.get(0).text();
                        }
                     } catch (var21: IOException) {
                        var21.printStackTrace();
                     }
                  }

                  var var24: BookChapter;
                  label78: {
                     var24 = new BookChapter(null, null, false, null, null, 0, null, null, null, null, null, null, null, 8191, null);
                     var24.setIndex(var10);
                     var24.setBookUrl(this.getBook().getBookUrl());
                     val var27: java.lang.String = resource.getHref();
                     var24.setUrl(var27);
                     if (var10 == 0) {
                        if (title.length() == 0) {
                           var24.setTitle("封面");
                           break label78;
                        }
                     }

                     var24.setTitle(if (title == null) "" else title);
                  }

                  chapterList.add(var24);
               }
            }
         }
      }

      val var31: Book = this.book;
      val var22: BookChapter = CollectionsKt.lastOrNull(chapterList);
      var31.setLatestChapterTitle(if (var22 == null) null else var22.getTitle());
      this.book.setTotalChapterNum(chapterList.size());
      return chapterList;
   }

   public fun getChapterList(): ArrayList<BookChapter> {
      val chapterList: ArrayList = new ArrayList();
      val var2: EpubBook = this.getEpubBook();
      if (var2 != null) {
         val var3: TableOfContents = var2.getTableOfContents();
         if (var3 != null) {
            val var4: java.util.List = var3.getAllUniqueResources();
            if (var4 != null) {
               val `$this$forEachIndexed$iv`: java.lang.Iterable = var4;
               var `index$iv`: Int = 0;

               for (Object item$iv : $this$forEachIndexed$iv) {
                  val var10: Int = `index$iv`++;
                  if (var10 < 0) {
                     CollectionsKt.throwIndexOverflow();
                  }

                  val resource: Resource = `item$iv` as Resource;
                  var title: java.lang.String = (`item$iv` as Resource).getTitle();
                  if (title == null || title.length() == 0) {
                     try {
                        val var24: ByteArray = resource.getData();
                        val var25: Elements = Jsoup.parse(new java.lang.String(var24, this.mCharset)).getElementsByTag("title");
                        if (var25.size() > 0) {
                           title = var25.get(0).text();
                        }
                     } catch (var20: IOException) {
                        var20.printStackTrace();
                     }
                  }

                  var var23: BookChapter;
                  label78: {
                     var23 = new BookChapter(null, null, false, null, null, 0, null, null, null, null, null, null, null, 8191, null);
                     var23.setIndex(var10);
                     var23.setBookUrl(this.getBook().getBookUrl());
                     val var26: java.lang.String = resource.getHref();
                     var23.setUrl(var26);
                     if (var10 == 0) {
                        if (title.length() == 0) {
                           var23.setTitle("封面");
                           break label78;
                        }
                     }

                     var23.setTitle(if (title == null) "" else title);
                  }

                  chapterList.add(var23);
               }
            }
         }
      }

      val var30: Book = this.book;
      val var21: BookChapter = CollectionsKt.lastOrNull(chapterList);
      var30.setLatestChapterTitle(if (var21 == null) null else var21.getTitle());
      this.book.setTotalChapterNum(chapterList.size());
      return chapterList;
   }

   public fun getChapterListBySpinAndToc(useTocTitle: Boolean = false): ArrayList<BookChapter> {
      val tocChapterList: ArrayList = this.getChapterList();
      val spinChapterList: ArrayList = this.getChapterListBySpine();
      if (spinChapterList.size() == 0) {
         return tocChapterList;
      } else if (tocChapterList.size() == 0) {
         return spinChapterList;
      } else {
         val titleMap: java.util.Map = new LinkedHashMap();
         var var12: Int = 0;
         var var6: Int = tocChapterList.size();
         if (0 < var6) {
            do {
               val i: Int = var12++;
               val var10001: java.lang.String = (tocChapterList.get(i) as BookChapter).getUrl();
               val chapter: Any = tocChapterList.get(i);
               titleMap.put(var10001, chapter);
            } while (var12 < var6);
         }

         var12 = 0;
         var6 = spinChapterList.size();
         if (0 < var6) {
            do {
               var tocChapter: BookChapter = (BookChapter)spinChapterList.get(var12++);
               val var17: BookChapter = tocChapter;
               tocChapter = titleMap.get(tocChapter.getUrl()) as BookChapter;
               if (tocChapter != null && tocChapter.getTitle().length() > 0 && (useTocTitle || var17.getTitle().length() == 0)) {
                  var17.setTitle(tocChapter.getTitle());
               }
            } while (var12 < var6);
         }

         val var10000: Book = this.book;
         val var14: BookChapter = CollectionsKt.lastOrNull(spinChapterList);
         var10000.setLatestChapterTitle(if (var14 == null) null else var14.getTitle());
         this.book.setTotalChapterNum(spinChapterList.size());
         return spinChapterList;
      }
   }

   public fun getChapterListByTocAndSpin(useSpinTitle: Boolean = false): ArrayList<BookChapter> {
      val tocChapterList: ArrayList = this.getChapterList();
      val spinChapterList: ArrayList = this.getChapterListBySpine();
      if (tocChapterList.size() == 0) {
         return spinChapterList;
      } else if (spinChapterList.size() == 0) {
         return tocChapterList;
      } else {
         val titleMap: java.util.Map = new LinkedHashMap();
         var var12: Int = 0;
         var var6: Int = spinChapterList.size();
         if (0 < var6) {
            do {
               val i: Int = var12++;
               val var10001: java.lang.String = (spinChapterList.get(i) as BookChapter).getUrl();
               val chapter: Any = spinChapterList.get(i);
               titleMap.put(var10001, chapter);
            } while (var12 < var6);
         }

         var12 = 0;
         var6 = tocChapterList.size();
         if (0 < var6) {
            do {
               var tocChapter: BookChapter = (BookChapter)tocChapterList.get(var12++);
               val var17: BookChapter = tocChapter;
               tocChapter = titleMap.get(tocChapter.getUrl()) as BookChapter;
               if (tocChapter != null && tocChapter.getTitle().length() > 0 && (useSpinTitle || var17.getTitle().length() == 0)) {
                  var17.setTitle(tocChapter.getTitle());
               }
            } while (var12 < var6);
         }

         val var10000: Book = this.book;
         val var14: BookChapter = CollectionsKt.lastOrNull(tocChapterList);
         var10000.setLatestChapterTitle(if (var14 == null) null else var14.getTitle());
         this.book.setTotalChapterNum(tocChapterList.size());
         return tocChapterList;
      }
   }

   public companion object {
      private final var eFile: EpubFile?

      @Synchronized
      private fun getEFile(book: Book): EpubFile {
         if (EpubFile.access$getEFile$cp() != null) {
            var var2: EpubFile = EpubFile.access$getEFile$cp();
            if ((if (var2 == null) null else var2.getBook().getBookUrl()) == book.getBookUrl()) {
               var2 = EpubFile.access$getEFile$cp();
               if (var2 != null) {
                  var2.setBook(book);
               }

               val var6: EpubFile = EpubFile.access$getEFile$cp();
               return var6;
            }
         }

         EpubFile.access$setEFile$cp(new EpubFile(book));
         val var5: EpubFile = EpubFile.access$getEFile$cp();
         return var5;
      }

      @Synchronized
      public fun getChapterList(book: Book): ArrayList<BookChapter> {
         if (book.getTocUrl().length() == 0) {
            book.setTocUrl("spin+toc");
         }

         val var4: EpubFile = this.getEFile(book);
         val var5: java.lang.String = book.getTocUrl();
         switch (var5.hashCode()) {
            case -2010033025:
               if (var5.equals("spin+toc")) {
                  EpubFileKt.access$getLogger$p().info("epubFile.getChapterListBySpinAndToc");
                  return EpubFile.getChapterListBySpinAndToc$default(var4, false, 1, null);
               }
               break;
            case -2009526578:
               if (var5.equals("spin<toc")) {
                  EpubFileKt.access$getLogger$p().info("epubFile.getChapterListBySpinAndToc true");
                  return var4.getChapterListBySpinAndToc(true);
               }
               break;
            case -1386236251:
               if (var5.equals("toc+spin")) {
                  EpubFileKt.access$getLogger$p().info("epubFile.getChapterListByTocAndSpin");
                  return EpubFile.getChapterListByTocAndSpin$default(var4, false, 1, null);
               }
               break;
            case -1370536394:
               if (var5.equals("toc<spin")) {
                  EpubFileKt.access$getLogger$p().info("epubFile.getChapterListByTocAndSpin true");
                  return var4.getChapterListByTocAndSpin(true);
               }
               break;
            case 115016:
               if (var5.equals("toc")) {
                  EpubFileKt.access$getLogger$p().info("epubFile.getChapterList");
                  return var4.getChapterList();
               }
               break;
            case 3536962:
               if (var5.equals("spin")) {
                  EpubFileKt.access$getLogger$p().info("epubFile.getChapterListBySpine");
                  return var4.getChapterListBySpine();
               }
            default:
         }

         EpubFileKt.access$getLogger$p().info("epubFile.getChapterListBySpinAndToc");
         return EpubFile.getChapterListBySpinAndToc$default(var4, false, 1, null);
      }

      @Synchronized
      public fun getContent(book: Book, chapter: BookChapter): String? {
         return EpubFile.access$getContent(this.getEFile(book), chapter);
      }

      @Synchronized
      public fun getImage(book: Book, href: String): InputStream? {
         return EpubFile.access$getImage(this.getEFile(book), href);
      }

      @Synchronized
      public fun upBookInfo(book: Book, onlyCover: Boolean = false) {
         if (onlyCover) {
            this.getEFile(book).updateCover();
         } else {
            EpubFile.access$upBookInfo(this.getEFile(book));
         }
      }
   }
}
