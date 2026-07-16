// 
// Decompiled by Procyon v0.6.0
// 

package io.legado.app.model.localBook;

import kotlin.jvm.internal.DefaultConstructorMarker;
import kotlin.collections.CollectionsKt;
import java.util.List;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.ArrayList;
import io.legado.app.utils.FileUtils;
import java.nio.file.Paths;
import io.legado.app.utils.MD5Utils;
import me.ag2s.epublib.domain.Author;
import me.ag2s.epublib.domain.Resources;
import java.io.InputStream;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Element;
import java.util.Iterator;
import java.net.URL;
import io.legado.app.utils.HtmlFormatter;
import kotlin.text.Regex;
import me.ag2s.epublib.domain.Resource;
import org.jsoup.select.Elements;
import kotlin.text.StringsKt;
import io.legado.app.data.entities.BookChapter;
import java.io.File;
import java.util.zip.ZipFile;
import me.ag2s.epublib.epub.EpubReader;
import kotlin.jvm.internal.Intrinsics;
import org.jetbrains.annotations.Nullable;
import me.ag2s.epublib.domain.EpubBook;
import java.nio.charset.Charset;
import io.legado.app.data.entities.Book;
import org.jetbrains.annotations.NotNull;
import kotlin.Metadata;

@Metadata(mv = { 1, 5, 1 }, k = 1, xi = 48, d1 = { "\u0000X\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0005\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u000e\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\u000b\n\u0002\b\u0006\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0010\u0002\n\u0002\b\u0003\u0018\u0000 '2\u00020\u0001:\u0001'B\r\u0012\u0006\u0010\u0002\u001a\u00020\u0003?\u0006\u0002\u0010\u0004J$\u0010\u000e\u001a\u00020\u000f2\u0006\u0010\u0010\u001a\u00020\u00112\b\u0010\u0012\u001a\u0004\u0018\u00010\u00132\b\u0010\u0014\u001a\u0004\u0018\u00010\u0013H\u0002J\f\u0010\u0015\u001a\b\u0012\u0004\u0012\u00020\u00170\u0016J\u0016\u0010\u0018\u001a\b\u0012\u0004\u0012\u00020\u00170\u00162\b\b\u0002\u0010\u0019\u001a\u00020\u001aJ\f\u0010\u001b\u001a\b\u0012\u0004\u0012\u00020\u00170\u0016J\u0016\u0010\u001c\u001a\b\u0012\u0004\u0012\u00020\u00170\u00162\b\b\u0002\u0010\u001d\u001a\u00020\u001aJ\u0012\u0010\u001e\u001a\u0004\u0018\u00010\u00132\u0006\u0010\u001f\u001a\u00020\u0017H\u0002J\u0012\u0010 \u001a\u0004\u0018\u00010!2\u0006\u0010\"\u001a\u00020\u0013H\u0002J\n\u0010#\u001a\u0004\u0018\u00010\tH\u0002J\b\u0010$\u001a\u00020%H\u0002J\u0006\u0010&\u001a\u00020%R\u001a\u0010\u0002\u001a\u00020\u0003X\u0086\u000e?\u0006\u000e\n\u0000\u001a\u0004\b\u0005\u0010\u0006\"\u0004\b\u0007\u0010\u0004R\u0018\u0010\b\u001a\u0004\u0018\u00010\t8BX\u0082\u000e?\u0006\b\n\u0000\u001a\u0004\b\n\u0010\u000bR\u000e\u0010\f\u001a\u00020\rX\u0082\u000e?\u0006\u0002\n\u0000¡§\u0006(" }, d2 = { "Lio/legado/app/model/localBook/EpubFile;", "", "book", "Lio/legado/app/data/entities/Book;", "(Lio/legado/app/data/entities/Book;)V", "getBook", "()Lio/legado/app/data/entities/Book;", "setBook", "epubBook", "Lme/ag2s/epublib/domain/EpubBook;", "getEpubBook", "()Lme/ag2s/epublib/domain/EpubBook;", "mCharset", "Ljava/nio/charset/Charset;", "getBody", "Lorg/jsoup/nodes/Element;", "res", "Lme/ag2s/epublib/domain/Resource;", "startFragmentId", "", "endFragmentId", "getChapterList", "Ljava/util/ArrayList;", "Lio/legado/app/data/entities/BookChapter;", "getChapterListBySpinAndToc", "useTocTitle", "", "getChapterListBySpine", "getChapterListByTocAndSpin", "useSpinTitle", "getContent", "chapter", "getImage", "Ljava/io/InputStream;", "href", "readEpub", "upBookInfo", "", "updateCover", "Companion", "reader-pro" })
public final class EpubFile
{
    @NotNull
    public static final Companion Companion;
    @NotNull
    private Book book;
    @NotNull
    private Charset mCharset;
    @Nullable
    private EpubBook epubBook;
    @Nullable
    private static EpubFile eFile;
    
    public EpubFile(@NotNull final Book book) {
        Intrinsics.checkNotNullParameter((Object)book, "book");
        this.book = book;
        final Charset defaultCharset = Charset.defaultCharset();
        Intrinsics.checkNotNullExpressionValue((Object)defaultCharset, "defaultCharset()");
        this.mCharset = defaultCharset;
        try {
            final EpubBook epubBook = this.getEpubBook();
            if (epubBook != null) {
                final EpubBook it = epubBook;
                final int n = 0;
            }
        }
        catch (final Exception e) {
            e.printStackTrace();
        }
    }
    
    @NotNull
    public final Book getBook() {
        return this.book;
    }
    
    public final void setBook(@NotNull final Book <set-?>) {
        Intrinsics.checkNotNullParameter((Object)<set-?>, "<set-?>");
        this.book = <set-?>;
    }
    
    private final EpubBook getEpubBook() {
        if (this.epubBook != null) {
            return this.epubBook;
        }
        return this.epubBook = this.readEpub();
    }
    
    private final EpubBook readEpub() {
        try {
            final File file = this.book.getLocalFile();
            return new EpubReader().readEpubLazy(new ZipFile(file), "utf-8");
        }
        catch (final Exception e) {
            e.printStackTrace();
            return null;
        }
    }
    
    private final String getContent(final BookChapter chapter) {
        if (StringsKt.contains$default((CharSequence)chapter.getUrl(), (CharSequence)"titlepage.xhtml", false, 2, (Object)null)) {
            return "<img src=\"cover.jpeg\" />";
        }
        final EpubBook epubBook2 = this.getEpubBook();
        if (epubBook2 == null) {
            return null;
        }
        final EpubBook epubBook = epubBook2;
        final int n = 0;
        final String nextUrl = chapter.getVariable("nextUrl");
        final String startFragmentId = chapter.getStartFragmentId();
        final String endFragmentId = chapter.getEndFragmentId();
        final Elements elements = new Elements();
        boolean isChapter = false;
        for (final Resource res : epubBook.getContents()) {
            if (Intrinsics.areEqual((Object)StringsKt.substringBeforeLast$default(chapter.getUrl(), "#", (String)null, 2, (Object)null), (Object)res.getHref())) {
                final Elements elements2 = elements;
                Intrinsics.checkNotNullExpressionValue((Object)res, "res");
                elements2.add((Object)this.getBody(res, startFragmentId, endFragmentId));
                isChapter = true;
                if (nextUrl == null) {
                    break;
                }
                if (Intrinsics.areEqual((Object)res.getHref(), (Object)StringsKt.substringBeforeLast$default(nextUrl, "#", (String)null, 2, (Object)null))) {
                    break;
                }
                continue;
            }
            else {
                if (!isChapter) {
                    continue;
                }
                final String href = res.getHref();
                final String s = nextUrl;
                if (Intrinsics.areEqual((Object)href, (Object)((s == null) ? null : StringsKt.substringBeforeLast$default(s, "#", (String)null, 2, (Object)null)))) {
                    break;
                }
                final Elements elements3 = elements;
                Intrinsics.checkNotNullExpressionValue((Object)res, "res");
                elements3.add((Object)this.getBody(res, startFragmentId, endFragmentId));
            }
        }
        String html = elements.outerHtml();
        final long tag = 4L;
        if (this.getBook().getDelTag(tag)) {
            final String s2 = html;
            Intrinsics.checkNotNullExpressionValue((Object)s2, "html");
            html = new Regex("<ruby>\\s?([\\u4e00-\\u9fa5])\\s?.*?</ruby>").replace((CharSequence)s2, "$1");
        }
        return HtmlFormatter.formatKeepImg$default(HtmlFormatter.INSTANCE, html, (URL)null, 2, (Object)null);
    }
    
    private final Element getBody(final Resource res, final String startFragmentId, final String endFragmentId) {
        final byte[] data = res.getData();
        Intrinsics.checkNotNullExpressionValue((Object)data, "res.data");
        final Element body = Jsoup.parse(new String(data, this.mCharset)).body();
        final CharSequence charSequence = startFragmentId;
        if (charSequence != null && !StringsKt.isBlank(charSequence)) {
            final Element elementById = body.getElementById(startFragmentId);
            if (elementById != null) {
                final Elements previousElementSiblings = elementById.previousElementSiblings();
                if (previousElementSiblings != null) {
                    previousElementSiblings.remove();
                }
            }
        }
        final CharSequence charSequence2 = endFragmentId;
        if (charSequence2 != null && !StringsKt.isBlank(charSequence2) && !Intrinsics.areEqual((Object)endFragmentId, (Object)startFragmentId)) {
            final Element elementById2 = body.getElementById(endFragmentId);
            if (elementById2 != null) {
                final Element $this$getBody_u24lambda_u2d2 = elementById2;
                final int n = 0;
                $this$getBody_u24lambda_u2d2.nextElementSiblings().remove();
                $this$getBody_u24lambda_u2d2.remove();
            }
        }
        final long tag = 2L;
        if (this.book.getDelTag(tag)) {
            body.getElementsByTag("h1").remove();
            body.getElementsByTag("h2").remove();
            body.getElementsByTag("h3").remove();
            body.getElementsByTag("h4").remove();
            body.getElementsByTag("h5").remove();
            body.getElementsByTag("h6").remove();
        }
        final Elements children = body.children();
        children.select("script").remove();
        children.select("style").remove();
        Intrinsics.checkNotNullExpressionValue((Object)body, "body");
        return body;
    }
    
    private final InputStream getImage(final String href) {
        final String abHref = StringsKt.replace$default(href, "../", "", false, 4, (Object)null);
        final EpubBook epubBook = this.getEpubBook();
        InputStream inputStream;
        if (epubBook == null) {
            inputStream = null;
        }
        else {
            final Resources resources = epubBook.getResources();
            if (resources == null) {
                inputStream = null;
            }
            else {
                final Resource byHref = resources.getByHref(abHref);
                inputStream = ((byHref == null) ? null : byHref.getInputStream());
            }
        }
        return inputStream;
    }
    
    private final void upBookInfo() {
        if (this.getEpubBook() == null) {
            final Companion companion = EpubFile.Companion;
            EpubFile.eFile = null;
            this.book.setIntro("\u4e66\u7c4d\u5bfc\u5165\u5f02\u5e38");
        }
        else {
            final EpubBook epubBook = this.getEpubBook();
            Intrinsics.checkNotNull((Object)epubBook);
            final me.ag2s.epublib.domain.Metadata metadata = epubBook.getMetadata();
            final Book book = this.book;
            final String firstTitle = metadata.getFirstTitle();
            Intrinsics.checkNotNullExpressionValue((Object)firstTitle, "metadata.firstTitle");
            book.setName(firstTitle);
            if (this.book.getName().length() == 0) {
                this.book.setName(StringsKt.replace$default(this.book.getOriginName(), ".epub", "", false, 4, (Object)null));
            }
            if (metadata.getAuthors().size() > 0) {
                final String string = metadata.getAuthors().get(0).toString();
                Intrinsics.checkNotNullExpressionValue((Object)string, "metadata.authors[0].toString()");
                final String author = new Regex("^, |, $").replace((CharSequence)string, "");
                this.book.setAuthor(author);
            }
            if (metadata.getDescriptions().size() > 0) {
                this.book.setIntro(Jsoup.parse((String)metadata.getDescriptions().get(0)).text());
            }
            this.updateCover();
        }
    }
    
    public final void updateCover() {
        final String coverFile = Intrinsics.stringPlus(MD5Utils.INSTANCE.md5Encode16(this.book.getBookUrl()), (Object)".jpg");
        final String relativeCoverUrl = Paths.get("assets", this.book.getUserNameSpace(), "covers", coverFile).toString();
        this.book.setCoverUrl(Intrinsics.stringPlus("/", (Object)StringsKt.replace$default(relativeCoverUrl, "\\", "/", false, 4, (Object)null)));
        final String coverUrl = Paths.get(this.book.workRoot(), "storage", relativeCoverUrl).toString();
        if (!new File(coverUrl).exists()) {
            final EpubBook epubBook = this.getEpubBook();
            if (epubBook != null) {
                final Resource coverImage = epubBook.getCoverImage();
                if (coverImage != null) {
                    final byte[] data = coverImage.getData();
                    if (data != null) {
                        final byte[] it = data;
                        final int n = 0;
                        FileUtils.INSTANCE.writeBytes(coverUrl, it);
                    }
                }
            }
        }
    }
    
    @NotNull
    public final ArrayList<BookChapter> getChapterListBySpine() {
        // 
        // This method could not be decompiled.
        // 
        // Original Bytecode:
        // 
        //     3: dup            
        //     4: invokespecial   java/util/ArrayList.<init>:()V
        //     7: astore_1        /* chapterList */
        //     8: aload_0         /* this */
        //     9: invokespecial   io/legado/app/model/localBook/EpubFile.getEpubBook:()Lme/ag2s/epublib/domain/EpubBook;
        //    12: astore_2       
        //    13: aload_2        
        //    14: ifnonnull       20
        //    17: goto            413
        //    20: aload_2        
        //    21: invokevirtual   me/ag2s/epublib/domain/EpubBook.getSpine:()Lme/ag2s/epublib/domain/Spine;
        //    24: astore_3       
        //    25: aload_3        
        //    26: ifnonnull       32
        //    29: goto            413
        //    32: aload_3        
        //    33: invokevirtual   me/ag2s/epublib/domain/Spine.getSpineReferences:()Ljava/util/List;
        //    36: astore          4
        //    38: aload           4
        //    40: ifnonnull       46
        //    43: goto            413
        //    46: aload           4
        //    48: checkcast       Ljava/lang/Iterable;
        //    51: astore          $this$forEachIndexed$iv
        //    53: iconst_0       
        //    54: istore          $i$f$forEachIndexed
        //    56: iconst_0       
        //    57: istore          index$iv
        //    59: aload           $this$forEachIndexed$iv
        //    61: invokeinterface java/lang/Iterable.iterator:()Ljava/util/Iterator;
        //    66: astore          8
        //    68: aload           8
        //    70: invokeinterface java/util/Iterator.hasNext:()Z
        //    75: ifeq            412
        //    78: aload           8
        //    80: invokeinterface java/util/Iterator.next:()Ljava/lang/Object;
        //    85: astore          item$iv
        //    87: iload           index$iv
        //    89: iinc            index$iv, 1
        //    92: istore          10
        //    94: iconst_0       
        //    95: istore          11
        //    97: iload           10
        //    99: ifge            105
        //   102: invokestatic    kotlin/collections/CollectionsKt.throwIndexOverflow:()V
        //   105: iload           10
        //   107: aload           item$iv
        //   109: checkcast       Lme/ag2s/epublib/domain/SpineReference;
        //   112: astore          12
        //   114: istore          index
        //   116: iconst_0       
        //   117: istore          $i$a$-forEachIndexed-EpubFile$getChapterListBySpine$1
        //   119: aload           spinResource
        //   121: invokevirtual   me/ag2s/epublib/domain/SpineReference.getResource:()Lme/ag2s/epublib/domain/Resource;
        //   124: astore          resource
        //   126: aload           resource
        //   128: invokevirtual   me/ag2s/epublib/domain/Resource.getTitle:()Ljava/lang/String;
        //   131: astore          title
        //   133: aload           title
        //   135: checkcast       Ljava/lang/CharSequence;
        //   138: astore          17
        //   140: iconst_0       
        //   141: istore          18
        //   143: iconst_0       
        //   144: istore          19
        //   146: aload           17
        //   148: ifnull          161
        //   151: aload           17
        //   153: invokeinterface java/lang/CharSequence.length:()I
        //   158: ifne            165
        //   161: iconst_1       
        //   162: goto            166
        //   165: iconst_0       
        //   166: ifeq            256
        //   169: nop            
        //   170: aload           resource
        //   172: invokevirtual   me/ag2s/epublib/domain/Resource.getData:()[B
        //   175: astore          18
        //   177: aload           18
        //   179: ldc_w           "resource.data"
        //   182: invokestatic    kotlin/jvm/internal/Intrinsics.checkNotNullExpressionValue:(Ljava/lang/Object;Ljava/lang/String;)V
        //   185: aload           18
        //   187: astore          18
        //   189: aload_0         /* this */
        //   190: getfield        io/legado/app/model/localBook/EpubFile.mCharset:Ljava/nio/charset/Charset;
        //   193: astore          19
        //   195: iconst_0       
        //   196: istore          20
        //   198: new             Ljava/lang/String;
        //   201: dup            
        //   202: aload           18
        //   204: aload           19
        //   206: invokespecial   java/lang/String.<init>:([BLjava/nio/charset/Charset;)V
        //   209: invokestatic    org/jsoup/Jsoup.parse:(Ljava/lang/String;)Lorg/jsoup/nodes/Document;
        //   212: astore          doc
        //   214: aload           doc
        //   216: ldc_w           "title"
        //   219: invokevirtual   org/jsoup/nodes/Document.getElementsByTag:(Ljava/lang/String;)Lorg/jsoup/select/Elements;
        //   222: astore          elements
        //   224: aload           elements
        //   226: invokevirtual   org/jsoup/select/Elements.size:()I
        //   229: ifle            256
        //   232: aload           elements
        //   234: iconst_0       
        //   235: invokevirtual   org/jsoup/select/Elements.get:(I)Ljava/lang/Object;
        //   238: checkcast       Lorg/jsoup/nodes/Element;
        //   241: invokevirtual   org/jsoup/nodes/Element.text:()Ljava/lang/String;
        //   244: astore          title
        //   246: goto            256
        //   249: astore          e
        //   251: aload           e
        //   253: invokevirtual   java/io/IOException.printStackTrace:()V
        //   256: new             Lio/legado/app/data/entities/BookChapter;
        //   259: dup            
        //   260: aconst_null    
        //   261: aconst_null    
        //   262: iconst_0       
        //   263: aconst_null    
        //   264: aconst_null    
        //   265: iconst_0       
        //   266: aconst_null    
        //   267: aconst_null    
        //   268: aconst_null    
        //   269: aconst_null    
        //   270: aconst_null    
        //   271: aconst_null    
        //   272: aconst_null    
        //   273: sipush          8191
        //   276: aconst_null    
        //   277: invokespecial   io/legado/app/data/entities/BookChapter.<init>:(Ljava/lang/String;Ljava/lang/String;ZLjava/lang/String;Ljava/lang/String;ILjava/lang/String;Ljava/lang/String;Ljava/lang/Long;Ljava/lang/Long;Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;ILkotlin/jvm/internal/DefaultConstructorMarker;)V
        //   280: astore          chapter
        //   282: aload           chapter
        //   284: iload           index
        //   286: invokevirtual   io/legado/app/data/entities/BookChapter.setIndex:(I)V
        //   289: aload           chapter
        //   291: aload_0         /* this */
        //   292: invokevirtual   io/legado/app/model/localBook/EpubFile.getBook:()Lio/legado/app/data/entities/Book;
        //   295: invokevirtual   io/legado/app/data/entities/Book.getBookUrl:()Ljava/lang/String;
        //   298: invokevirtual   io/legado/app/data/entities/BookChapter.setBookUrl:(Ljava/lang/String;)V
        //   301: aload           chapter
        //   303: aload           resource
        //   305: invokevirtual   me/ag2s/epublib/domain/Resource.getHref:()Ljava/lang/String;
        //   308: astore          18
        //   310: aload           18
        //   312: ldc_w           "resource.href"
        //   315: invokestatic    kotlin/jvm/internal/Intrinsics.checkNotNullExpressionValue:(Ljava/lang/Object;Ljava/lang/String;)V
        //   318: aload           18
        //   320: invokevirtual   io/legado/app/data/entities/BookChapter.setUrl:(Ljava/lang/String;)V
        //   323: iload           index
        //   325: ifne            379
        //   328: aload           title
        //   330: astore          18
        //   332: aload           18
        //   334: ldc_w           "title"
        //   337: invokestatic    kotlin/jvm/internal/Intrinsics.checkNotNullExpressionValue:(Ljava/lang/Object;Ljava/lang/String;)V
        //   340: aload           18
        //   342: checkcast       Ljava/lang/CharSequence;
        //   345: astore          18
        //   347: iconst_0       
        //   348: istore          19
        //   350: aload           18
        //   352: invokeinterface java/lang/CharSequence.length:()I
        //   357: ifne            364
        //   360: iconst_1       
        //   361: goto            365
        //   364: iconst_0       
        //   365: ifeq            379
        //   368: aload           chapter
        //   370: ldc_w           "\u5c01\u9762"
        //   373: invokevirtual   io/legado/app/data/entities/BookChapter.setTitle:(Ljava/lang/String;)V
        //   376: goto            401
        //   379: aload           chapter
        //   381: aload           title
        //   383: astore          18
        //   385: aload           18
        //   387: ifnonnull       396
        //   390: ldc_w           ""
        //   393: goto            398
        //   396: aload           18
        //   398: invokevirtual   io/legado/app/data/entities/BookChapter.setTitle:(Ljava/lang/String;)V
        //   401: aload_1         /* chapterList */
        //   402: aload           chapter
        //   404: invokevirtual   java/util/ArrayList.add:(Ljava/lang/Object;)Z
        //   407: pop            
        //   408: nop            
        //   409: goto            68
        //   412: nop            
        //   413: aload_0         /* this */
        //   414: getfield        io/legado/app/model/localBook/EpubFile.book:Lio/legado/app/data/entities/Book;
        //   417: aload_1         /* chapterList */
        //   418: checkcast       Ljava/util/List;
        //   421: invokestatic    kotlin/collections/CollectionsKt.lastOrNull:(Ljava/util/List;)Ljava/lang/Object;
        //   424: checkcast       Lio/legado/app/data/entities/BookChapter;
        //   427: astore_2       
        //   428: aload_2        
        //   429: ifnonnull       436
        //   432: aconst_null    
        //   433: goto            440
        //   436: aload_2        
        //   437: invokevirtual   io/legado/app/data/entities/BookChapter.getTitle:()Ljava/lang/String;
        //   440: invokevirtual   io/legado/app/data/entities/Book.setLatestChapterTitle:(Ljava/lang/String;)V
        //   443: aload_0         /* this */
        //   444: getfield        io/legado/app/model/localBook/EpubFile.book:Lio/legado/app/data/entities/Book;
        //   447: aload_1         /* chapterList */
        //   448: invokevirtual   java/util/ArrayList.size:()I
        //   451: invokevirtual   io/legado/app/data/entities/Book.setTotalChapterNum:(I)V
        //   454: aload_1         /* chapterList */
        //   455: areturn        
        //    Signature:
        //  ()Ljava/util/ArrayList<Lio/legado/app/data/entities/BookChapter;>;
        //    StackMapTable: 00 14 FD 00 14 07 01 D9 07 00 39 FC 00 0B 07 01 E0 FC 00 0D 07 00 82 FF 00 15 00 09 07 00 02 07 01 D9 07 00 39 07 01 E0 07 00 82 07 01 E5 01 01 07 00 88 00 00 FE 00 24 07 00 04 01 01 FF 00 37 00 14 07 00 02 07 01 D9 07 00 39 07 01 E0 07 00 82 07 01 E5 01 01 07 00 88 07 00 04 01 01 07 01 ED 01 01 07 00 92 07 00 DB 07 00 63 01 01 00 00 03 40 01 FF 00 52 00 12 07 00 02 07 01 D9 07 00 39 07 01 E0 07 00 82 07 01 E5 01 01 07 00 88 07 00 04 01 01 07 01 ED 01 01 07 00 92 07 00 DB 07 00 04 00 01 07 01 D7 06 FF 00 6B 00 14 07 00 02 07 01 D9 07 00 39 07 01 E0 07 00 82 07 01 E5 01 01 07 00 88 07 00 04 01 01 07 01 ED 01 01 07 00 92 07 00 DB 07 00 5D 07 00 63 01 00 00 40 01 FF 00 0D 00 13 07 00 02 07 01 D9 07 00 39 07 01 E0 07 00 82 07 01 E5 01 01 07 00 88 07 00 04 01 01 07 01 ED 01 01 07 00 92 07 00 DB 07 00 5D 07 00 04 00 00 FF 00 10 00 13 07 00 02 07 01 D9 07 00 39 07 01 E0 07 00 82 07 01 E5 01 01 07 00 88 07 00 04 01 01 07 01 ED 01 01 07 00 92 07 00 DB 07 00 5D 07 00 DB 00 01 07 00 5D FF 00 01 00 13 07 00 02 07 01 D9 07 00 39 07 01 E0 07 00 82 07 01 E5 01 01 07 00 88 07 00 04 01 01 07 01 ED 01 01 07 00 92 07 00 DB 07 00 5D 07 00 DB 00 02 07 00 5D 07 00 DB FF 00 02 00 13 07 00 02 07 01 D9 07 00 39 07 01 E0 07 00 82 07 01 E5 01 01 07 00 88 07 00 04 01 01 07 01 ED 01 01 07 00 92 07 00 DB 07 00 5D 07 00 04 00 00 FF 00 0A 00 09 07 00 02 07 01 D9 07 00 39 07 01 E0 07 00 82 07 01 E5 01 01 07 00 88 00 00 FF 00 00 00 03 07 00 02 07 01 D9 07 00 39 00 00 FF 00 16 00 03 07 00 02 07 01 D9 07 00 5D 00 01 07 00 37 FF 00 03 00 03 07 00 02 07 01 D9 07 00 5D 00 02 07 00 37 07 00 DB
        //    Exceptions:
        //  Try           Handler
        //  Start  End    Start  End    Type                 
        //  -----  -----  -----  -----  ---------------------
        //  169    246    249    256    Ljava/io/IOException;
        // 
        // The error that occurred was:
        // 
        // java.lang.NullPointerException: Cannot read field "references" because "newVariable" is null
        //     at com.strobel.decompiler.ast.AstBuilder.convertLocalVariables(AstBuilder.java:2945)
        //     at com.strobel.decompiler.ast.AstBuilder.performStackAnalysis(AstBuilder.java:2501)
        //     at com.strobel.decompiler.ast.AstBuilder.build(AstBuilder.java:108)
        //     at com.strobel.decompiler.languages.java.ast.AstMethodBodyBuilder.createMethodBody(AstMethodBodyBuilder.java:203)
        //     at com.strobel.decompiler.languages.java.ast.AstMethodBodyBuilder.createMethodBody(AstMethodBodyBuilder.java:93)
        //     at com.strobel.decompiler.languages.java.ast.AstBuilder.createMethodBody(AstBuilder.java:868)
        //     at com.strobel.decompiler.languages.java.ast.AstBuilder.createMethod(AstBuilder.java:761)
        //     at com.strobel.decompiler.languages.java.ast.AstBuilder.addTypeMembers(AstBuilder.java:638)
        //     at com.strobel.decompiler.languages.java.ast.AstBuilder.createTypeCore(AstBuilder.java:605)
        //     at com.strobel.decompiler.languages.java.ast.AstBuilder.createTypeNoCache(AstBuilder.java:195)
        //     at com.strobel.decompiler.languages.java.ast.AstBuilder.createType(AstBuilder.java:162)
        //     at com.strobel.decompiler.languages.java.ast.AstBuilder.addType(AstBuilder.java:137)
        //     at com.strobel.decompiler.languages.java.JavaLanguage.buildAst(JavaLanguage.java:71)
        //     at com.strobel.decompiler.languages.java.JavaLanguage.decompileType(JavaLanguage.java:59)
        //     at com.strobel.decompiler.DecompilerDriver.decompileType(DecompilerDriver.java:333)
        //     at com.strobel.decompiler.DecompilerDriver.decompileJar(DecompilerDriver.java:254)
        //     at com.strobel.decompiler.DecompilerDriver.main(DecompilerDriver.java:129)
        // 
        throw new IllegalStateException("An error occurred while decompiling this method.");
    }
    
    @NotNull
    public final ArrayList<BookChapter> getChapterList() {
        // 
        // This method could not be decompiled.
        // 
        // Original Bytecode:
        // 
        //     3: dup            
        //     4: invokespecial   java/util/ArrayList.<init>:()V
        //     7: astore_1        /* chapterList */
        //     8: aload_0         /* this */
        //     9: invokespecial   io/legado/app/model/localBook/EpubFile.getEpubBook:()Lme/ag2s/epublib/domain/EpubBook;
        //    12: astore_2       
        //    13: aload_2        
        //    14: ifnonnull       20
        //    17: goto            406
        //    20: aload_2        
        //    21: invokevirtual   me/ag2s/epublib/domain/EpubBook.getTableOfContents:()Lme/ag2s/epublib/domain/TableOfContents;
        //    24: astore_3       
        //    25: aload_3        
        //    26: ifnonnull       32
        //    29: goto            406
        //    32: aload_3        
        //    33: invokevirtual   me/ag2s/epublib/domain/TableOfContents.getAllUniqueResources:()Ljava/util/List;
        //    36: astore          4
        //    38: aload           4
        //    40: ifnonnull       46
        //    43: goto            406
        //    46: aload           4
        //    48: checkcast       Ljava/lang/Iterable;
        //    51: astore          $this$forEachIndexed$iv
        //    53: iconst_0       
        //    54: istore          $i$f$forEachIndexed
        //    56: iconst_0       
        //    57: istore          index$iv
        //    59: aload           $this$forEachIndexed$iv
        //    61: invokeinterface java/lang/Iterable.iterator:()Ljava/util/Iterator;
        //    66: astore          8
        //    68: aload           8
        //    70: invokeinterface java/util/Iterator.hasNext:()Z
        //    75: ifeq            405
        //    78: aload           8
        //    80: invokeinterface java/util/Iterator.next:()Ljava/lang/Object;
        //    85: astore          item$iv
        //    87: iload           index$iv
        //    89: iinc            index$iv, 1
        //    92: istore          10
        //    94: iconst_0       
        //    95: istore          11
        //    97: iload           10
        //    99: ifge            105
        //   102: invokestatic    kotlin/collections/CollectionsKt.throwIndexOverflow:()V
        //   105: iload           10
        //   107: aload           item$iv
        //   109: checkcast       Lme/ag2s/epublib/domain/Resource;
        //   112: astore          12
        //   114: istore          index
        //   116: iconst_0       
        //   117: istore          $i$a$-forEachIndexed-EpubFile$getChapterList$1
        //   119: aload           resource
        //   121: invokevirtual   me/ag2s/epublib/domain/Resource.getTitle:()Ljava/lang/String;
        //   124: astore          title
        //   126: aload           title
        //   128: checkcast       Ljava/lang/CharSequence;
        //   131: astore          16
        //   133: iconst_0       
        //   134: istore          17
        //   136: iconst_0       
        //   137: istore          18
        //   139: aload           16
        //   141: ifnull          154
        //   144: aload           16
        //   146: invokeinterface java/lang/CharSequence.length:()I
        //   151: ifne            158
        //   154: iconst_1       
        //   155: goto            159
        //   158: iconst_0       
        //   159: ifeq            249
        //   162: nop            
        //   163: aload           resource
        //   165: invokevirtual   me/ag2s/epublib/domain/Resource.getData:()[B
        //   168: astore          17
        //   170: aload           17
        //   172: ldc_w           "resource.data"
        //   175: invokestatic    kotlin/jvm/internal/Intrinsics.checkNotNullExpressionValue:(Ljava/lang/Object;Ljava/lang/String;)V
        //   178: aload           17
        //   180: astore          17
        //   182: aload_0         /* this */
        //   183: getfield        io/legado/app/model/localBook/EpubFile.mCharset:Ljava/nio/charset/Charset;
        //   186: astore          18
        //   188: iconst_0       
        //   189: istore          19
        //   191: new             Ljava/lang/String;
        //   194: dup            
        //   195: aload           17
        //   197: aload           18
        //   199: invokespecial   java/lang/String.<init>:([BLjava/nio/charset/Charset;)V
        //   202: invokestatic    org/jsoup/Jsoup.parse:(Ljava/lang/String;)Lorg/jsoup/nodes/Document;
        //   205: astore          doc
        //   207: aload           doc
        //   209: ldc_w           "title"
        //   212: invokevirtual   org/jsoup/nodes/Document.getElementsByTag:(Ljava/lang/String;)Lorg/jsoup/select/Elements;
        //   215: astore          elements
        //   217: aload           elements
        //   219: invokevirtual   org/jsoup/select/Elements.size:()I
        //   222: ifle            249
        //   225: aload           elements
        //   227: iconst_0       
        //   228: invokevirtual   org/jsoup/select/Elements.get:(I)Ljava/lang/Object;
        //   231: checkcast       Lorg/jsoup/nodes/Element;
        //   234: invokevirtual   org/jsoup/nodes/Element.text:()Ljava/lang/String;
        //   237: astore          title
        //   239: goto            249
        //   242: astore          e
        //   244: aload           e
        //   246: invokevirtual   java/io/IOException.printStackTrace:()V
        //   249: new             Lio/legado/app/data/entities/BookChapter;
        //   252: dup            
        //   253: aconst_null    
        //   254: aconst_null    
        //   255: iconst_0       
        //   256: aconst_null    
        //   257: aconst_null    
        //   258: iconst_0       
        //   259: aconst_null    
        //   260: aconst_null    
        //   261: aconst_null    
        //   262: aconst_null    
        //   263: aconst_null    
        //   264: aconst_null    
        //   265: aconst_null    
        //   266: sipush          8191
        //   269: aconst_null    
        //   270: invokespecial   io/legado/app/data/entities/BookChapter.<init>:(Ljava/lang/String;Ljava/lang/String;ZLjava/lang/String;Ljava/lang/String;ILjava/lang/String;Ljava/lang/String;Ljava/lang/Long;Ljava/lang/Long;Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;ILkotlin/jvm/internal/DefaultConstructorMarker;)V
        //   273: astore          chapter
        //   275: aload           chapter
        //   277: iload           index
        //   279: invokevirtual   io/legado/app/data/entities/BookChapter.setIndex:(I)V
        //   282: aload           chapter
        //   284: aload_0         /* this */
        //   285: invokevirtual   io/legado/app/model/localBook/EpubFile.getBook:()Lio/legado/app/data/entities/Book;
        //   288: invokevirtual   io/legado/app/data/entities/Book.getBookUrl:()Ljava/lang/String;
        //   291: invokevirtual   io/legado/app/data/entities/BookChapter.setBookUrl:(Ljava/lang/String;)V
        //   294: aload           chapter
        //   296: aload           resource
        //   298: invokevirtual   me/ag2s/epublib/domain/Resource.getHref:()Ljava/lang/String;
        //   301: astore          17
        //   303: aload           17
        //   305: ldc_w           "resource.href"
        //   308: invokestatic    kotlin/jvm/internal/Intrinsics.checkNotNullExpressionValue:(Ljava/lang/Object;Ljava/lang/String;)V
        //   311: aload           17
        //   313: invokevirtual   io/legado/app/data/entities/BookChapter.setUrl:(Ljava/lang/String;)V
        //   316: iload           index
        //   318: ifne            372
        //   321: aload           title
        //   323: astore          17
        //   325: aload           17
        //   327: ldc_w           "title"
        //   330: invokestatic    kotlin/jvm/internal/Intrinsics.checkNotNullExpressionValue:(Ljava/lang/Object;Ljava/lang/String;)V
        //   333: aload           17
        //   335: checkcast       Ljava/lang/CharSequence;
        //   338: astore          17
        //   340: iconst_0       
        //   341: istore          18
        //   343: aload           17
        //   345: invokeinterface java/lang/CharSequence.length:()I
        //   350: ifne            357
        //   353: iconst_1       
        //   354: goto            358
        //   357: iconst_0       
        //   358: ifeq            372
        //   361: aload           chapter
        //   363: ldc_w           "\u5c01\u9762"
        //   366: invokevirtual   io/legado/app/data/entities/BookChapter.setTitle:(Ljava/lang/String;)V
        //   369: goto            394
        //   372: aload           chapter
        //   374: aload           title
        //   376: astore          17
        //   378: aload           17
        //   380: ifnonnull       389
        //   383: ldc_w           ""
        //   386: goto            391
        //   389: aload           17
        //   391: invokevirtual   io/legado/app/data/entities/BookChapter.setTitle:(Ljava/lang/String;)V
        //   394: aload_1         /* chapterList */
        //   395: aload           chapter
        //   397: invokevirtual   java/util/ArrayList.add:(Ljava/lang/Object;)Z
        //   400: pop            
        //   401: nop            
        //   402: goto            68
        //   405: nop            
        //   406: aload_0         /* this */
        //   407: getfield        io/legado/app/model/localBook/EpubFile.book:Lio/legado/app/data/entities/Book;
        //   410: aload_1         /* chapterList */
        //   411: checkcast       Ljava/util/List;
        //   414: invokestatic    kotlin/collections/CollectionsKt.lastOrNull:(Ljava/util/List;)Ljava/lang/Object;
        //   417: checkcast       Lio/legado/app/data/entities/BookChapter;
        //   420: astore_2       
        //   421: aload_2        
        //   422: ifnonnull       429
        //   425: aconst_null    
        //   426: goto            433
        //   429: aload_2        
        //   430: invokevirtual   io/legado/app/data/entities/BookChapter.getTitle:()Ljava/lang/String;
        //   433: invokevirtual   io/legado/app/data/entities/Book.setLatestChapterTitle:(Ljava/lang/String;)V
        //   436: aload_0         /* this */
        //   437: getfield        io/legado/app/model/localBook/EpubFile.book:Lio/legado/app/data/entities/Book;
        //   440: aload_1         /* chapterList */
        //   441: invokevirtual   java/util/ArrayList.size:()I
        //   444: invokevirtual   io/legado/app/data/entities/Book.setTotalChapterNum:(I)V
        //   447: aload_1         /* chapterList */
        //   448: areturn        
        //    Signature:
        //  ()Ljava/util/ArrayList<Lio/legado/app/data/entities/BookChapter;>;
        //    StackMapTable: 00 14 FD 00 14 07 01 D9 07 00 39 FC 00 0B 07 02 34 FC 00 0D 07 00 82 FF 00 15 00 09 07 00 02 07 01 D9 07 00 39 07 02 34 07 00 82 07 01 E5 01 01 07 00 88 00 00 FE 00 24 07 00 04 01 01 FF 00 30 00 13 07 00 02 07 01 D9 07 00 39 07 02 34 07 00 82 07 01 E5 01 01 07 00 88 07 00 04 01 01 07 00 92 01 01 07 00 DB 07 00 63 01 01 00 00 03 40 01 FF 00 52 00 11 07 00 02 07 01 D9 07 00 39 07 02 34 07 00 82 07 01 E5 01 01 07 00 88 07 00 04 01 01 07 00 92 01 01 07 00 DB 07 00 04 00 01 07 01 D7 06 FF 00 6B 00 13 07 00 02 07 01 D9 07 00 39 07 02 34 07 00 82 07 01 E5 01 01 07 00 88 07 00 04 01 01 07 00 92 01 01 07 00 DB 07 00 5D 07 00 63 01 00 00 40 01 FF 00 0D 00 12 07 00 02 07 01 D9 07 00 39 07 02 34 07 00 82 07 01 E5 01 01 07 00 88 07 00 04 01 01 07 00 92 01 01 07 00 DB 07 00 5D 07 00 04 00 00 FF 00 10 00 12 07 00 02 07 01 D9 07 00 39 07 02 34 07 00 82 07 01 E5 01 01 07 00 88 07 00 04 01 01 07 00 92 01 01 07 00 DB 07 00 5D 07 00 DB 00 01 07 00 5D FF 00 01 00 12 07 00 02 07 01 D9 07 00 39 07 02 34 07 00 82 07 01 E5 01 01 07 00 88 07 00 04 01 01 07 00 92 01 01 07 00 DB 07 00 5D 07 00 DB 00 02 07 00 5D 07 00 DB FF 00 02 00 12 07 00 02 07 01 D9 07 00 39 07 02 34 07 00 82 07 01 E5 01 01 07 00 88 07 00 04 01 01 07 00 92 01 01 07 00 DB 07 00 5D 07 00 04 00 00 FF 00 0A 00 09 07 00 02 07 01 D9 07 00 39 07 02 34 07 00 82 07 01 E5 01 01 07 00 88 00 00 FF 00 00 00 03 07 00 02 07 01 D9 07 00 39 00 00 FF 00 16 00 03 07 00 02 07 01 D9 07 00 5D 00 01 07 00 37 FF 00 03 00 03 07 00 02 07 01 D9 07 00 5D 00 02 07 00 37 07 00 DB
        //    Exceptions:
        //  Try           Handler
        //  Start  End    Start  End    Type                 
        //  -----  -----  -----  -----  ---------------------
        //  162    239    242    249    Ljava/io/IOException;
        // 
        // The error that occurred was:
        // 
        // java.lang.NullPointerException: Cannot read field "references" because "newVariable" is null
        //     at com.strobel.decompiler.ast.AstBuilder.convertLocalVariables(AstBuilder.java:2945)
        //     at com.strobel.decompiler.ast.AstBuilder.performStackAnalysis(AstBuilder.java:2501)
        //     at com.strobel.decompiler.ast.AstBuilder.build(AstBuilder.java:108)
        //     at com.strobel.decompiler.languages.java.ast.AstMethodBodyBuilder.createMethodBody(AstMethodBodyBuilder.java:203)
        //     at com.strobel.decompiler.languages.java.ast.AstMethodBodyBuilder.createMethodBody(AstMethodBodyBuilder.java:93)
        //     at com.strobel.decompiler.languages.java.ast.AstBuilder.createMethodBody(AstBuilder.java:868)
        //     at com.strobel.decompiler.languages.java.ast.AstBuilder.createMethod(AstBuilder.java:761)
        //     at com.strobel.decompiler.languages.java.ast.AstBuilder.addTypeMembers(AstBuilder.java:638)
        //     at com.strobel.decompiler.languages.java.ast.AstBuilder.createTypeCore(AstBuilder.java:605)
        //     at com.strobel.decompiler.languages.java.ast.AstBuilder.createTypeNoCache(AstBuilder.java:195)
        //     at com.strobel.decompiler.languages.java.ast.AstBuilder.createType(AstBuilder.java:162)
        //     at com.strobel.decompiler.languages.java.ast.AstBuilder.addType(AstBuilder.java:137)
        //     at com.strobel.decompiler.languages.java.JavaLanguage.buildAst(JavaLanguage.java:71)
        //     at com.strobel.decompiler.languages.java.JavaLanguage.decompileType(JavaLanguage.java:59)
        //     at com.strobel.decompiler.DecompilerDriver.decompileType(DecompilerDriver.java:333)
        //     at com.strobel.decompiler.DecompilerDriver.decompileJar(DecompilerDriver.java:254)
        //     at com.strobel.decompiler.DecompilerDriver.main(DecompilerDriver.java:129)
        // 
        throw new IllegalStateException("An error occurred while decompiling this method.");
    }
    
    @NotNull
    public final ArrayList<BookChapter> getChapterListBySpinAndToc(final boolean useTocTitle) {
        final ArrayList tocChapterList = this.getChapterList();
        final ArrayList spinChapterList = this.getChapterListBySpine();
        if (spinChapterList.size() == 0) {
            return tocChapterList;
        }
        if (tocChapterList.size() == 0) {
            return spinChapterList;
        }
        final Map titleMap = new LinkedHashMap();
        int j = 0;
        final int size = tocChapterList.size();
        if (j < size) {
            do {
                final int i = j;
                ++j;
                final Map map = titleMap;
                final String url = tocChapterList.get(i).getUrl();
                final Object value = tocChapterList.get(i);
                Intrinsics.checkNotNullExpressionValue(value, "tocChapterList.get(i)");
                map.put(url, value);
            } while (j < size);
        }
        int k = 0;
        final int size2 = spinChapterList.size();
        if (k < size2) {
            do {
                final int i = k;
                ++k;
                final BookChapter value2 = spinChapterList.get(i);
                Intrinsics.checkNotNullExpressionValue((Object)value2, "spinChapterList.get(i)");
                final BookChapter chapter = value2;
                final BookChapter tocChapter = titleMap.get(chapter.getUrl());
                if (tocChapter != null && tocChapter.getTitle().length() > 0 && (useTocTitle || chapter.getTitle().length() == 0)) {
                    chapter.setTitle(tocChapter.getTitle());
                }
            } while (k < size2);
        }
        final Book book = this.book;
        final BookChapter bookChapter = (BookChapter)CollectionsKt.lastOrNull((List)spinChapterList);
        book.setLatestChapterTitle((bookChapter == null) ? null : bookChapter.getTitle());
        this.book.setTotalChapterNum(spinChapterList.size());
        return spinChapterList;
    }
    
    public static /* synthetic */ ArrayList getChapterListBySpinAndToc$default(final EpubFile epubFile, boolean useTocTitle, final int n, final Object o) {
        if ((n & 0x1) != 0x0) {
            useTocTitle = false;
        }
        return epubFile.getChapterListBySpinAndToc(useTocTitle);
    }
    
    @NotNull
    public final ArrayList<BookChapter> getChapterListByTocAndSpin(final boolean useSpinTitle) {
        final ArrayList tocChapterList = this.getChapterList();
        final ArrayList spinChapterList = this.getChapterListBySpine();
        if (tocChapterList.size() == 0) {
            return spinChapterList;
        }
        if (spinChapterList.size() == 0) {
            return tocChapterList;
        }
        final Map titleMap = new LinkedHashMap();
        int j = 0;
        final int size = spinChapterList.size();
        if (j < size) {
            do {
                final int i = j;
                ++j;
                final Map map = titleMap;
                final String url = spinChapterList.get(i).getUrl();
                final Object value = spinChapterList.get(i);
                Intrinsics.checkNotNullExpressionValue(value, "spinChapterList.get(i)");
                map.put(url, value);
            } while (j < size);
        }
        int k = 0;
        final int size2 = tocChapterList.size();
        if (k < size2) {
            do {
                final int i = k;
                ++k;
                final BookChapter value2 = tocChapterList.get(i);
                Intrinsics.checkNotNullExpressionValue((Object)value2, "tocChapterList.get(i)");
                final BookChapter chapter = value2;
                final BookChapter tocChapter = titleMap.get(chapter.getUrl());
                if (tocChapter != null && tocChapter.getTitle().length() > 0 && (useSpinTitle || chapter.getTitle().length() == 0)) {
                    chapter.setTitle(tocChapter.getTitle());
                }
            } while (k < size2);
        }
        final Book book = this.book;
        final BookChapter bookChapter = (BookChapter)CollectionsKt.lastOrNull((List)tocChapterList);
        book.setLatestChapterTitle((bookChapter == null) ? null : bookChapter.getTitle());
        this.book.setTotalChapterNum(tocChapterList.size());
        return tocChapterList;
    }
    
    public static /* synthetic */ ArrayList getChapterListByTocAndSpin$default(final EpubFile epubFile, boolean useSpinTitle, final int n, final Object o) {
        if ((n & 0x1) != 0x0) {
            useSpinTitle = false;
        }
        return epubFile.getChapterListByTocAndSpin(useSpinTitle);
    }
    
    public static final /* synthetic */ EpubFile access$getEFile$cp() {
        return EpubFile.eFile;
    }
    
    public static final /* synthetic */ void access$setEFile$cp(final EpubFile <set-?>) {
        EpubFile.eFile = <set-?>;
    }
    
    static {
        Companion = new Companion(null);
    }
    
    @Metadata(mv = { 1, 5, 1 }, k = 1, xi = 48, d1 = { "\u0000>\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u000e\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\u0002\n\u0000\n\u0002\u0010\u000b\n\u0000\b\u0086\u0003\u0018\u00002\u00020\u0001B\u0007\b\u0002?\u0006\u0002\u0010\u0002J\u0014\u0010\u0005\u001a\b\u0012\u0004\u0012\u00020\u00070\u00062\u0006\u0010\b\u001a\u00020\tJ\u0018\u0010\n\u001a\u0004\u0018\u00010\u000b2\u0006\u0010\b\u001a\u00020\t2\u0006\u0010\f\u001a\u00020\u0007J\u0010\u0010\r\u001a\u00020\u00042\u0006\u0010\b\u001a\u00020\tH\u0002J\u0018\u0010\u000e\u001a\u0004\u0018\u00010\u000f2\u0006\u0010\b\u001a\u00020\t2\u0006\u0010\u0010\u001a\u00020\u000bJ\u0018\u0010\u0011\u001a\u00020\u00122\u0006\u0010\b\u001a\u00020\t2\b\b\u0002\u0010\u0013\u001a\u00020\u0014R\u0010\u0010\u0003\u001a\u0004\u0018\u00010\u0004X\u0082\u000e?\u0006\u0002\n\u0000¡§\u0006\u0015" }, d2 = { "Lio/legado/app/model/localBook/EpubFile$Companion;", "", "()V", "eFile", "Lio/legado/app/model/localBook/EpubFile;", "getChapterList", "Ljava/util/ArrayList;", "Lio/legado/app/data/entities/BookChapter;", "book", "Lio/legado/app/data/entities/Book;", "getContent", "", "chapter", "getEFile", "getImage", "Ljava/io/InputStream;", "href", "upBookInfo", "", "onlyCover", "", "reader-pro" })
    public static final class Companion
    {
        private Companion() {
        }
        
        private final synchronized EpubFile getEFile(final Book book) {
            if (EpubFile.access$getEFile$cp() != null) {
                final EpubFile access$getEFile$cp = EpubFile.access$getEFile$cp();
                if (Intrinsics.areEqual((Object)((access$getEFile$cp == null) ? null : access$getEFile$cp.getBook().getBookUrl()), (Object)book.getBookUrl())) {
                    final EpubFile access$getEFile$cp2 = EpubFile.access$getEFile$cp();
                    if (access$getEFile$cp2 != null) {
                        access$getEFile$cp2.setBook(book);
                    }
                    final EpubFile access$getEFile$cp3 = EpubFile.access$getEFile$cp();
                    Intrinsics.checkNotNull((Object)access$getEFile$cp3);
                    return access$getEFile$cp3;
                }
            }
            EpubFile.access$setEFile$cp(new EpubFile(book));
            final EpubFile access$getEFile$cp4 = EpubFile.access$getEFile$cp();
            Intrinsics.checkNotNull((Object)access$getEFile$cp4);
            return access$getEFile$cp4;
        }
        
        @NotNull
        public final synchronized ArrayList<BookChapter> getChapterList(@NotNull final Book book) {
            Intrinsics.checkNotNullParameter((Object)book, "book");
            if (book.getTocUrl().length() == 0) {
                book.setTocUrl("spin+toc");
            }
            final EpubFile epubFile = this.getEFile(book);
            final String tocUrl = book.getTocUrl();
            switch (tocUrl) {
                case "spin": {
                    EpubFileKt.access$getLogger$p().info("epubFile.getChapterListBySpine");
                    return epubFile.getChapterListBySpine();
                }
                case "spin<toc": {
                    EpubFileKt.access$getLogger$p().info("epubFile.getChapterListBySpinAndToc true");
                    return epubFile.getChapterListBySpinAndToc(true);
                }
                case "toc": {
                    EpubFileKt.access$getLogger$p().info("epubFile.getChapterList");
                    return epubFile.getChapterList();
                }
                case "toc<spin": {
                    EpubFileKt.access$getLogger$p().info("epubFile.getChapterListByTocAndSpin true");
                    return epubFile.getChapterListByTocAndSpin(true);
                }
                case "toc+spin": {
                    EpubFileKt.access$getLogger$p().info("epubFile.getChapterListByTocAndSpin");
                    return EpubFile.getChapterListByTocAndSpin$default(epubFile, false, 1, null);
                }
                case "spin+toc": {
                    EpubFileKt.access$getLogger$p().info("epubFile.getChapterListBySpinAndToc");
                    return EpubFile.getChapterListBySpinAndToc$default(epubFile, false, 1, null);
                }
                default:
                    break;
            }
            EpubFileKt.access$getLogger$p().info("epubFile.getChapterListBySpinAndToc");
            return EpubFile.getChapterListBySpinAndToc$default(epubFile, false, 1, null);
        }
        
        @Nullable
        public final synchronized String getContent(@NotNull final Book book, @NotNull final BookChapter chapter) {
            Intrinsics.checkNotNullParameter((Object)book, "book");
            Intrinsics.checkNotNullParameter((Object)chapter, "chapter");
            return this.getEFile(book).getContent(chapter);
        }
        
        @Nullable
        public final synchronized InputStream getImage(@NotNull final Book book, @NotNull final String href) {
            Intrinsics.checkNotNullParameter((Object)book, "book");
            Intrinsics.checkNotNullParameter((Object)href, "href");
            return this.getEFile(book).getImage(href);
        }
        
        public final synchronized void upBookInfo(@NotNull final Book book, final boolean onlyCover) {
            Intrinsics.checkNotNullParameter((Object)book, "book");
            if (onlyCover) {
                this.getEFile(book).updateCover();
                return;
            }
            this.getEFile(book).upBookInfo();
        }
    }
}
