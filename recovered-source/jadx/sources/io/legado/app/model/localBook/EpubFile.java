package io.legado.app.model.localBook;

import io.legado.app.data.entities.Book;
import io.legado.app.data.entities.BookChapter;
import io.legado.app.utils.FileUtils;
import io.legado.app.utils.HtmlFormatter;
import io.legado.app.utils.MD5Utils;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.Charset;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.zip.ZipFile;
import kotlin.Metadata;
import kotlin.collections.CollectionsKt;
import kotlin.jvm.internal.DefaultConstructorMarker;
import kotlin.jvm.internal.Intrinsics;
import kotlin.text.Regex;
import kotlin.text.StringsKt;
import me.ag2s.epublib.domain.EpubBook;
import me.ag2s.epublib.domain.Resource;
import me.ag2s.epublib.domain.Resources;
import me.ag2s.epublib.domain.TableOfContents;
import me.ag2s.epublib.epub.EpubReader;
import me.ag2s.epublib.epub.NCXDocumentV2;
import me.ag2s.epublib.epub.NCXDocumentV3;
import me.ag2s.epublib.epub.PackageDocumentBase;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

/* JADX INFO: compiled from: EpubFile.kt */
/* JADX INFO: loaded from: reader-pro-classes-3.2.14.jar:io/legado/app/model/localBook/EpubFile.class */
@Metadata(mv = {1, 5, 1}, k = 1, xi = 48, d1 = {"\u0000X\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0005\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u000e\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\u000b\n\u0002\b\u0006\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0010\u0002\n\u0002\b\u0003\u0018\u0000 '2\u00020\u0001:\u0001'B\r\u0012\u0006\u0010\u0002\u001a\u00020\u0003¢\u0006\u0002\u0010\u0004J$\u0010\u000e\u001a\u00020\u000f2\u0006\u0010\u0010\u001a\u00020\u00112\b\u0010\u0012\u001a\u0004\u0018\u00010\u00132\b\u0010\u0014\u001a\u0004\u0018\u00010\u0013H\u0002J\f\u0010\u0015\u001a\b\u0012\u0004\u0012\u00020\u00170\u0016J\u0016\u0010\u0018\u001a\b\u0012\u0004\u0012\u00020\u00170\u00162\b\b\u0002\u0010\u0019\u001a\u00020\u001aJ\f\u0010\u001b\u001a\b\u0012\u0004\u0012\u00020\u00170\u0016J\u0016\u0010\u001c\u001a\b\u0012\u0004\u0012\u00020\u00170\u00162\b\b\u0002\u0010\u001d\u001a\u00020\u001aJ\u0012\u0010\u001e\u001a\u0004\u0018\u00010\u00132\u0006\u0010\u001f\u001a\u00020\u0017H\u0002J\u0012\u0010 \u001a\u0004\u0018\u00010!2\u0006\u0010\"\u001a\u00020\u0013H\u0002J\n\u0010#\u001a\u0004\u0018\u00010\tH\u0002J\b\u0010$\u001a\u00020%H\u0002J\u0006\u0010&\u001a\u00020%R\u001a\u0010\u0002\u001a\u00020\u0003X\u0086\u000e¢\u0006\u000e\n\u0000\u001a\u0004\b\u0005\u0010\u0006\"\u0004\b\u0007\u0010\u0004R\u0018\u0010\b\u001a\u0004\u0018\u00010\t8BX\u0082\u000e¢\u0006\b\n\u0000\u001a\u0004\b\n\u0010\u000bR\u000e\u0010\f\u001a\u00020\rX\u0082\u000e¢\u0006\u0002\n\u0000¨\u0006("}, d2 = {"Lio/legado/app/model/localBook/EpubFile;", PackageDocumentBase.PREFIX_OPF, "book", "Lio/legado/app/data/entities/Book;", "(Lio/legado/app/data/entities/Book;)V", "getBook", "()Lio/legado/app/data/entities/Book;", "setBook", "epubBook", "Lme/ag2s/epublib/domain/EpubBook;", "getEpubBook", "()Lme/ag2s/epublib/domain/EpubBook;", "mCharset", "Ljava/nio/charset/Charset;", "getBody", "Lorg/jsoup/nodes/Element;", "res", "Lme/ag2s/epublib/domain/Resource;", "startFragmentId", PackageDocumentBase.PREFIX_OPF, "endFragmentId", "getChapterList", "Ljava/util/ArrayList;", "Lio/legado/app/data/entities/BookChapter;", "getChapterListBySpinAndToc", "useTocTitle", PackageDocumentBase.PREFIX_OPF, "getChapterListBySpine", "getChapterListByTocAndSpin", "useSpinTitle", "getContent", NCXDocumentV2.NCXAttributeValues.chapter, "getImage", "Ljava/io/InputStream;", "href", "readEpub", "upBookInfo", PackageDocumentBase.PREFIX_OPF, "updateCover", "Companion", "reader-pro"})
public final class EpubFile {

    /* JADX INFO: renamed from: Companion, reason: from kotlin metadata */
    @NotNull
    public static final Companion INSTANCE = new Companion(null);

    @NotNull
    private Book book;

    @NotNull
    private Charset mCharset;

    @Nullable
    private EpubBook epubBook;

    @Nullable
    private static EpubFile eFile;

    public EpubFile(@NotNull Book book) {
        Intrinsics.checkNotNullParameter(book, "book");
        this.book = book;
        Charset charsetDefaultCharset = Charset.defaultCharset();
        Intrinsics.checkNotNullExpressionValue(charsetDefaultCharset, "defaultCharset()");
        this.mCharset = charsetDefaultCharset;
        try {
            if (getEpubBook() == null) {
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @NotNull
    public final Book getBook() {
        return this.book;
    }

    public final void setBook(@NotNull Book book) {
        Intrinsics.checkNotNullParameter(book, "<set-?>");
        this.book = book;
    }

    /* JADX INFO: compiled from: EpubFile.kt */
    /* JADX INFO: loaded from: reader-pro-classes-3.2.14.jar:io/legado/app/model/localBook/EpubFile$Companion.class */
    @Metadata(mv = {1, 5, 1}, k = 1, xi = 48, d1 = {"\u0000>\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u000e\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\u0002\n\u0000\n\u0002\u0010\u000b\n\u0000\b\u0086\u0003\u0018\u00002\u00020\u0001B\u0007\b\u0002¢\u0006\u0002\u0010\u0002J\u0014\u0010\u0005\u001a\b\u0012\u0004\u0012\u00020\u00070\u00062\u0006\u0010\b\u001a\u00020\tJ\u0018\u0010\n\u001a\u0004\u0018\u00010\u000b2\u0006\u0010\b\u001a\u00020\t2\u0006\u0010\f\u001a\u00020\u0007J\u0010\u0010\r\u001a\u00020\u00042\u0006\u0010\b\u001a\u00020\tH\u0002J\u0018\u0010\u000e\u001a\u0004\u0018\u00010\u000f2\u0006\u0010\b\u001a\u00020\t2\u0006\u0010\u0010\u001a\u00020\u000bJ\u0018\u0010\u0011\u001a\u00020\u00122\u0006\u0010\b\u001a\u00020\t2\b\b\u0002\u0010\u0013\u001a\u00020\u0014R\u0010\u0010\u0003\u001a\u0004\u0018\u00010\u0004X\u0082\u000e¢\u0006\u0002\n\u0000¨\u0006\u0015"}, d2 = {"Lio/legado/app/model/localBook/EpubFile$Companion;", PackageDocumentBase.PREFIX_OPF, "()V", "eFile", "Lio/legado/app/model/localBook/EpubFile;", "getChapterList", "Ljava/util/ArrayList;", "Lio/legado/app/data/entities/BookChapter;", "book", "Lio/legado/app/data/entities/Book;", "getContent", PackageDocumentBase.PREFIX_OPF, NCXDocumentV2.NCXAttributeValues.chapter, "getEFile", "getImage", "Ljava/io/InputStream;", "href", "upBookInfo", PackageDocumentBase.PREFIX_OPF, "onlyCover", PackageDocumentBase.PREFIX_OPF, "reader-pro"})
    public static final class Companion {
        public /* synthetic */ Companion(DefaultConstructorMarker $constructor_marker) {
            this();
        }

        private Companion() {
        }

        private final synchronized EpubFile getEFile(Book book) {
            if (EpubFile.eFile != null) {
                EpubFile epubFile = EpubFile.eFile;
                if (Intrinsics.areEqual(epubFile == null ? null : epubFile.getBook().getBookUrl(), book.getBookUrl())) {
                    EpubFile epubFile2 = EpubFile.eFile;
                    if (epubFile2 != null) {
                        epubFile2.setBook(book);
                    }
                    EpubFile epubFile3 = EpubFile.eFile;
                    Intrinsics.checkNotNull(epubFile3);
                    return epubFile3;
                }
            }
            EpubFile.eFile = new EpubFile(book);
            EpubFile epubFile4 = EpubFile.eFile;
            Intrinsics.checkNotNull(epubFile4);
            return epubFile4;
        }

        /* JADX WARN: Failed to restore switch over string. Please report as a decompilation issue */
        @NotNull
        public final synchronized ArrayList<BookChapter> getChapterList(@NotNull Book book) {
            Intrinsics.checkNotNullParameter(book, "book");
            if (book.getTocUrl().length() == 0) {
                book.setTocUrl("spin+toc");
            }
            EpubFile epubFile = getEFile(book);
            String tocUrl = book.getTocUrl();
            switch (tocUrl.hashCode()) {
                case -2010033025:
                    if (tocUrl.equals("spin+toc")) {
                        EpubFileKt.logger.info("epubFile.getChapterListBySpinAndToc");
                        return EpubFile.getChapterListBySpinAndToc$default(epubFile, false, 1, null);
                    }
                    break;
                case -2009526578:
                    if (tocUrl.equals("spin<toc")) {
                        EpubFileKt.logger.info("epubFile.getChapterListBySpinAndToc true");
                        return epubFile.getChapterListBySpinAndToc(true);
                    }
                    break;
                case -1386236251:
                    if (tocUrl.equals("toc+spin")) {
                        EpubFileKt.logger.info("epubFile.getChapterListByTocAndSpin");
                        return EpubFile.getChapterListByTocAndSpin$default(epubFile, false, 1, null);
                    }
                    break;
                case -1370536394:
                    if (tocUrl.equals("toc<spin")) {
                        EpubFileKt.logger.info("epubFile.getChapterListByTocAndSpin true");
                        return epubFile.getChapterListByTocAndSpin(true);
                    }
                    break;
                case 115016:
                    if (tocUrl.equals("toc")) {
                        EpubFileKt.logger.info("epubFile.getChapterList");
                        return epubFile.getChapterList();
                    }
                    break;
                case 3536962:
                    if (tocUrl.equals("spin")) {
                        EpubFileKt.logger.info("epubFile.getChapterListBySpine");
                        return epubFile.getChapterListBySpine();
                    }
                    break;
            }
            EpubFileKt.logger.info("epubFile.getChapterListBySpinAndToc");
            return EpubFile.getChapterListBySpinAndToc$default(epubFile, false, 1, null);
        }

        @Nullable
        public final synchronized String getContent(@NotNull Book book, @NotNull BookChapter chapter) {
            Intrinsics.checkNotNullParameter(book, "book");
            Intrinsics.checkNotNullParameter(chapter, NCXDocumentV2.NCXAttributeValues.chapter);
            return getEFile(book).getContent(chapter);
        }

        @Nullable
        public final synchronized InputStream getImage(@NotNull Book book, @NotNull String href) {
            Intrinsics.checkNotNullParameter(book, "book");
            Intrinsics.checkNotNullParameter(href, "href");
            return getEFile(book).getImage(href);
        }

        public static /* synthetic */ void upBookInfo$default(Companion companion, Book book, boolean z, int i, Object obj) {
            if ((i & 2) != 0) {
                z = false;
            }
            companion.upBookInfo(book, z);
        }

        public final synchronized void upBookInfo(@NotNull Book book, boolean onlyCover) {
            Intrinsics.checkNotNullParameter(book, "book");
            if (!onlyCover) {
                getEFile(book).upBookInfo();
            } else {
                getEFile(book).updateCover();
            }
        }
    }

    private final EpubBook getEpubBook() {
        if (this.epubBook != null) {
            return this.epubBook;
        }
        this.epubBook = readEpub();
        return this.epubBook;
    }

    private final EpubBook readEpub() {
        try {
            File file = this.book.getLocalFile();
            return new EpubReader().readEpubLazy(new ZipFile(file), "utf-8");
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public final String getContent(BookChapter chapter) {
        if (StringsKt.contains$default(chapter.getUrl(), "titlepage.xhtml", false, 2, (Object) null)) {
            return "<img src=\"cover.jpeg\" />";
        }
        EpubBook epubBook = getEpubBook();
        if (epubBook != null) {
            String nextUrl = chapter.getVariable("nextUrl");
            String startFragmentId = chapter.getStartFragmentId();
            String endFragmentId = chapter.getEndFragmentId();
            Elements elements = new Elements();
            boolean isChapter = false;
            for (Resource res : epubBook.getContents()) {
                if (Intrinsics.areEqual(StringsKt.substringBeforeLast$default(chapter.getUrl(), "#", (String) null, 2, (Object) null), res.getHref())) {
                    Intrinsics.checkNotNullExpressionValue(res, "res");
                    elements.add(getBody(res, startFragmentId, endFragmentId));
                    isChapter = true;
                    if (nextUrl == null || Intrinsics.areEqual(res.getHref(), StringsKt.substringBeforeLast$default(nextUrl, "#", (String) null, 2, (Object) null))) {
                        break;
                    }
                } else if (isChapter) {
                    if (Intrinsics.areEqual(res.getHref(), nextUrl == null ? null : StringsKt.substringBeforeLast$default(nextUrl, "#", (String) null, 2, (Object) null))) {
                        break;
                    }
                    Intrinsics.checkNotNullExpressionValue(res, "res");
                    elements.add(getBody(res, startFragmentId, endFragmentId));
                } else {
                    continue;
                }
            }
            String html = elements.outerHtml();
            if (getBook().getDelTag(4L)) {
                Intrinsics.checkNotNullExpressionValue(html, "html");
                html = new Regex("<ruby>\\s?([\\u4e00-\\u9fa5])\\s?.*?</ruby>").replace(html, "$1");
            }
            return HtmlFormatter.formatKeepImg$default(HtmlFormatter.INSTANCE, html, null, 2, null);
        }
        return null;
    }

    private final Element getBody(Resource res, String startFragmentId, String endFragmentId) throws IOException {
        Element $this$getBody_u24lambda_u2d2;
        Element elementById;
        Elements elementsPreviousElementSiblings;
        byte[] data = res.getData();
        Intrinsics.checkNotNullExpressionValue(data, "res.data");
        Element body = Jsoup.parse(new String(data, this.mCharset)).body();
        String str = startFragmentId;
        if (!(str == null || StringsKt.isBlank(str)) && (elementById = body.getElementById(startFragmentId)) != null && (elementsPreviousElementSiblings = elementById.previousElementSiblings()) != null) {
            elementsPreviousElementSiblings.remove();
        }
        String str2 = endFragmentId;
        if (!(str2 == null || StringsKt.isBlank(str2)) && !Intrinsics.areEqual(endFragmentId, startFragmentId) && ($this$getBody_u24lambda_u2d2 = body.getElementById(endFragmentId)) != null) {
            $this$getBody_u24lambda_u2d2.nextElementSiblings().remove();
            $this$getBody_u24lambda_u2d2.remove();
        }
        if (this.book.getDelTag(2L)) {
            body.getElementsByTag(NCXDocumentV3.XHTMLTgs.h1).remove();
            body.getElementsByTag(NCXDocumentV3.XHTMLTgs.h2).remove();
            body.getElementsByTag("h3").remove();
            body.getElementsByTag("h4").remove();
            body.getElementsByTag("h5").remove();
            body.getElementsByTag("h6").remove();
        }
        Elements children = body.children();
        children.select("script").remove();
        children.select("style").remove();
        Intrinsics.checkNotNullExpressionValue(body, NCXDocumentV3.XHTMLTgs.body);
        return body;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public final InputStream getImage(String href) {
        Resources resources;
        Resource byHref;
        String abHref = StringsKt.replace$default(href, "../", PackageDocumentBase.PREFIX_OPF, false, 4, (Object) null);
        EpubBook epubBook = getEpubBook();
        if (epubBook == null || (resources = epubBook.getResources()) == null || (byHref = resources.getByHref(abHref)) == null) {
            return null;
        }
        return byHref.getInputStream();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public final void upBookInfo() {
        if (getEpubBook() == null) {
            Companion companion = INSTANCE;
            eFile = null;
            this.book.setIntro("书籍导入异常");
            return;
        }
        EpubBook epubBook = getEpubBook();
        Intrinsics.checkNotNull(epubBook);
        me.ag2s.epublib.domain.Metadata metadata = epubBook.getMetadata();
        Book book = this.book;
        String firstTitle = metadata.getFirstTitle();
        Intrinsics.checkNotNullExpressionValue(firstTitle, "metadata.firstTitle");
        book.setName(firstTitle);
        if (this.book.getName().length() == 0) {
            this.book.setName(StringsKt.replace$default(this.book.getOriginName(), ".epub", PackageDocumentBase.PREFIX_OPF, false, 4, (Object) null));
        }
        if (metadata.getAuthors().size() > 0) {
            String string = metadata.getAuthors().get(0).toString();
            Intrinsics.checkNotNullExpressionValue(string, "metadata.authors[0].toString()");
            String author = new Regex("^, |, $").replace(string, PackageDocumentBase.PREFIX_OPF);
            this.book.setAuthor(author);
        }
        if (metadata.getDescriptions().size() > 0) {
            this.book.setIntro(Jsoup.parse(metadata.getDescriptions().get(0)).text());
        }
        updateCover();
    }

    public final void updateCover() {
        EpubBook epubBook;
        Resource coverImage;
        byte[] it;
        String coverFile = Intrinsics.stringPlus(MD5Utils.INSTANCE.md5Encode16(this.book.getBookUrl()), ".jpg");
        String relativeCoverUrl = Paths.get("assets", this.book.getUserNameSpace(), "covers", coverFile).toString();
        this.book.setCoverUrl(Intrinsics.stringPlus(TableOfContents.DEFAULT_PATH_SEPARATOR, StringsKt.replace$default(relativeCoverUrl, "\\", TableOfContents.DEFAULT_PATH_SEPARATOR, false, 4, (Object) null)));
        String coverUrl = Paths.get(this.book.workRoot(), "storage", relativeCoverUrl).toString();
        if (!new File(coverUrl).exists() && (epubBook = getEpubBook()) != null && (coverImage = epubBook.getCoverImage()) != null && (it = coverImage.getData()) != null) {
            FileUtils.INSTANCE.writeBytes(coverUrl, it);
        }
    }

    /*  JADX ERROR: JadxRuntimeException in pass: BlockSplitter
        jadx.core.utils.exceptions.JadxRuntimeException: Unexpected missing predecessor for block: B:24:0x00a9
        	at jadx.core.dex.visitors.blocks.BlockSplitter.addTempConnectionsForExcHandlers(BlockSplitter.java:280)
        	at jadx.core.dex.visitors.blocks.BlockSplitter.visit(BlockSplitter.java:79)
        */
    @org.jetbrains.annotations.NotNull
    public final java.util.ArrayList<io.legado.app.data.entities.BookChapter> getChapterListBySpine() throws java.io.IOException {
        /*
            Method dump skipped, instruction units count: 456
            To view this dump add '--comments-level debug' option
        */
        throw new UnsupportedOperationException("Method not decompiled: io.legado.app.model.localBook.EpubFile.getChapterListBySpine():java.util.ArrayList");
    }

    /*  JADX ERROR: JadxRuntimeException in pass: BlockSplitter
        jadx.core.utils.exceptions.JadxRuntimeException: Unexpected missing predecessor for block: B:24:0x00a2
        	at jadx.core.dex.visitors.blocks.BlockSplitter.addTempConnectionsForExcHandlers(BlockSplitter.java:280)
        	at jadx.core.dex.visitors.blocks.BlockSplitter.visit(BlockSplitter.java:79)
        */
    @org.jetbrains.annotations.NotNull
    public final java.util.ArrayList<io.legado.app.data.entities.BookChapter> getChapterList() throws java.io.IOException {
        /*
            Method dump skipped, instruction units count: 449
            To view this dump add '--comments-level debug' option
        */
        throw new UnsupportedOperationException("Method not decompiled: io.legado.app.model.localBook.EpubFile.getChapterList():java.util.ArrayList");
    }

    public static /* synthetic */ ArrayList getChapterListBySpinAndToc$default(EpubFile epubFile, boolean z, int i, Object obj) {
        if ((i & 1) != 0) {
            z = false;
        }
        return epubFile.getChapterListBySpinAndToc(z);
    }

    /* JADX WARN: Removed duplicated region for block: B:30:0x00f5  */
    @NotNull
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    public final ArrayList<BookChapter> getChapterListBySpinAndToc(boolean useTocTitle) throws IOException {
        ArrayList<BookChapter> chapterList = getChapterList();
        ArrayList<BookChapter> chapterListBySpine = getChapterListBySpine();
        if (chapterListBySpine.size() == 0) {
            return chapterList;
        }
        if (chapterList.size() == 0) {
            return chapterListBySpine;
        }
        Map titleMap = new LinkedHashMap();
        int i = 0;
        int size = chapterList.size();
        if (0 < size) {
            do {
                int i2 = i;
                i++;
                String url = chapterList.get(i2).getUrl();
                BookChapter bookChapter = chapterList.get(i2);
                Intrinsics.checkNotNullExpressionValue(bookChapter, "tocChapterList.get(i)");
                titleMap.put(url, bookChapter);
            } while (i < size);
        }
        int i3 = 0;
        int size2 = chapterListBySpine.size();
        if (0 < size2) {
            do {
                int i4 = i3;
                i3++;
                BookChapter bookChapter2 = chapterListBySpine.get(i4);
                Intrinsics.checkNotNullExpressionValue(bookChapter2, "spinChapterList.get(i)");
                BookChapter chapter = bookChapter2;
                BookChapter tocChapter = (BookChapter) titleMap.get(chapter.getUrl());
                if (tocChapter != null) {
                    if (tocChapter.getTitle().length() > 0) {
                        if (!useTocTitle) {
                            if (chapter.getTitle().length() == 0) {
                            }
                        } else {
                            chapter.setTitle(tocChapter.getTitle());
                        }
                    }
                }
            } while (i3 < size2);
        }
        Book book = this.book;
        BookChapter bookChapter3 = (BookChapter) CollectionsKt.lastOrNull(chapterListBySpine);
        book.setLatestChapterTitle(bookChapter3 == null ? null : bookChapter3.getTitle());
        this.book.setTotalChapterNum(chapterListBySpine.size());
        return chapterListBySpine;
    }

    public static /* synthetic */ ArrayList getChapterListByTocAndSpin$default(EpubFile epubFile, boolean z, int i, Object obj) {
        if ((i & 1) != 0) {
            z = false;
        }
        return epubFile.getChapterListByTocAndSpin(z);
    }

    /* JADX WARN: Removed duplicated region for block: B:30:0x00f5  */
    @NotNull
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    public final ArrayList<BookChapter> getChapterListByTocAndSpin(boolean useSpinTitle) throws IOException {
        ArrayList<BookChapter> chapterList = getChapterList();
        ArrayList<BookChapter> chapterListBySpine = getChapterListBySpine();
        if (chapterList.size() == 0) {
            return chapterListBySpine;
        }
        if (chapterListBySpine.size() == 0) {
            return chapterList;
        }
        Map titleMap = new LinkedHashMap();
        int i = 0;
        int size = chapterListBySpine.size();
        if (0 < size) {
            do {
                int i2 = i;
                i++;
                String url = chapterListBySpine.get(i2).getUrl();
                BookChapter bookChapter = chapterListBySpine.get(i2);
                Intrinsics.checkNotNullExpressionValue(bookChapter, "spinChapterList.get(i)");
                titleMap.put(url, bookChapter);
            } while (i < size);
        }
        int i3 = 0;
        int size2 = chapterList.size();
        if (0 < size2) {
            do {
                int i4 = i3;
                i3++;
                BookChapter bookChapter2 = chapterList.get(i4);
                Intrinsics.checkNotNullExpressionValue(bookChapter2, "tocChapterList.get(i)");
                BookChapter chapter = bookChapter2;
                BookChapter tocChapter = (BookChapter) titleMap.get(chapter.getUrl());
                if (tocChapter != null) {
                    if (tocChapter.getTitle().length() > 0) {
                        if (!useSpinTitle) {
                            if (chapter.getTitle().length() == 0) {
                            }
                        } else {
                            chapter.setTitle(tocChapter.getTitle());
                        }
                    }
                }
            } while (i3 < size2);
        }
        Book book = this.book;
        BookChapter bookChapter3 = (BookChapter) CollectionsKt.lastOrNull(chapterList);
        book.setLatestChapterTitle(bookChapter3 == null ? null : bookChapter3.getTitle());
        this.book.setTotalChapterNum(chapterList.size());
        return chapterList;
    }
}
