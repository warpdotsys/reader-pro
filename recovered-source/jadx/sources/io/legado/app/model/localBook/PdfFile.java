package io.legado.app.model.localBook;

import io.legado.app.data.entities.Book;
import io.legado.app.data.entities.BookChapter;
import java.io.Closeable;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Map;
import kotlin.Metadata;
import kotlin.Pair;
import kotlin.collections.CollectionsKt;
import kotlin.jvm.internal.DefaultConstructorMarker;
import kotlin.jvm.internal.Intrinsics;
import me.ag2s.epublib.epub.NCXDocumentV2;
import me.ag2s.epublib.epub.PackageDocumentBase;
import okhttp3.internal.Util;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.interactive.documentnavigation.outline.PDDocumentOutline;
import org.apache.pdfbox.pdmodel.interactive.documentnavigation.outline.PDOutlineItem;
import org.apache.pdfbox.pdmodel.interactive.documentnavigation.outline.PDOutlineNode;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/* JADX INFO: compiled from: PdfFile.kt */
/* JADX INFO: loaded from: reader-pro-classes-3.2.14.jar:io/legado/app/model/localBook/PdfFile.class */
@Metadata(mv = {1, 5, 1}, k = 1, xi = 48, d1 = {"\u0000N\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0005\n\u0002\u0018\u0002\n\u0002\b\u0005\n\u0002\u0010%\n\u0002\u0010\u000e\n\u0002\b\u0005\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0005\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0004\u0018\u0000 '2\u00020\u0001:\u0001'B\r\u0012\u0006\u0010\u0002\u001a\u00020\u0003¢\u0006\u0002\u0010\u0004J\u000e\u0010\u0015\u001a\b\u0012\u0004\u0012\u00020\u00170\u0016H\u0002J\u000e\u0010\u0018\u001a\b\u0012\u0004\u0012\u00020\u00170\u0016H\u0002J\u000e\u0010\u0019\u001a\b\u0012\u0004\u0012\u00020\u00170\u0016H\u0002J\u0012\u0010\u001a\u001a\u0004\u0018\u00010\u00102\u0006\u0010\u001b\u001a\u00020\u0017H\u0002J$\u0010\u001c\u001a\u001e\u0012\u0012\u0012\u0010\u0012\u0004\u0012\u00020\u0010\u0012\u0004\u0012\u00020\u0001\u0018\u00010\u000f\u0012\u0006\u0012\u0004\u0018\u00010\t0\u001dH\u0002J&\u0010\u001e\u001a\u00020\u001f2\u0006\u0010 \u001a\u00020!2\f\u0010\"\u001a\b\u0012\u0004\u0012\u00020\u00170\u00162\u0006\u0010#\u001a\u00020$H\u0002J\b\u0010%\u001a\u00020\u001fH\u0002J\b\u0010&\u001a\u00020\u001fH\u0002R\u001a\u0010\u0002\u001a\u00020\u0003X\u0086\u000e¢\u0006\u000e\n\u0000\u001a\u0004\b\u0005\u0010\u0006\"\u0004\b\u0007\u0010\u0004R\u001c\u0010\b\u001a\u0004\u0018\u00010\tX\u0086\u000e¢\u0006\u000e\n\u0000\u001a\u0004\b\n\u0010\u000b\"\u0004\b\f\u0010\rR(\u0010\u000e\u001a\u0010\u0012\u0004\u0012\u00020\u0010\u0012\u0004\u0012\u00020\u0001\u0018\u00010\u000fX\u0086\u000e¢\u0006\u000e\n\u0000\u001a\u0004\b\u0011\u0010\u0012\"\u0004\b\u0013\u0010\u0014¨\u0006("}, d2 = {"Lio/legado/app/model/localBook/PdfFile;", PackageDocumentBase.PREFIX_OPF, "book", "Lio/legado/app/data/entities/Book;", "(Lio/legado/app/data/entities/Book;)V", "getBook", "()Lio/legado/app/data/entities/Book;", "setBook", "cover", "Ljava/io/InputStream;", "getCover", "()Ljava/io/InputStream;", "setCover", "(Ljava/io/InputStream;)V", "info", PackageDocumentBase.PREFIX_OPF, PackageDocumentBase.PREFIX_OPF, "getInfo", "()Ljava/util/Map;", "setInfo", "(Ljava/util/Map;)V", "getChapterList", "Ljava/util/ArrayList;", "Lio/legado/app/data/entities/BookChapter;", "getChapterListByOutline", "getChapterListByPage", "getContent", NCXDocumentV2.NCXAttributeValues.chapter, "parseBookInfo", "Lkotlin/Pair;", "processOutline", PackageDocumentBase.PREFIX_OPF, "document", "Lorg/apache/pdfbox/pdmodel/PDDocument;", "chapterList", "outline", "Lorg/apache/pdfbox/pdmodel/interactive/documentnavigation/outline/PDOutlineNode;", "upBookInfo", "updateCover", "Companion", "reader-pro"})
public final class PdfFile {

    /* JADX INFO: renamed from: Companion, reason: from kotlin metadata */
    @NotNull
    public static final Companion INSTANCE = new Companion(null);

    @NotNull
    private Book book;

    @Nullable
    private Map<String, Object> info;

    @Nullable
    private InputStream cover;

    @Nullable
    private static PdfFile cFile;

    public PdfFile(@NotNull Book book) {
        Intrinsics.checkNotNullParameter(book, "book");
        this.book = book;
    }

    @NotNull
    public final Book getBook() {
        return this.book;
    }

    public final void setBook(@NotNull Book book) {
        Intrinsics.checkNotNullParameter(book, "<set-?>");
        this.book = book;
    }

    @Nullable
    public final Map<String, Object> getInfo() {
        return this.info;
    }

    public final void setInfo(@Nullable Map<String, Object> map) {
        this.info = map;
    }

    @Nullable
    public final InputStream getCover() {
        return this.cover;
    }

    public final void setCover(@Nullable InputStream inputStream) {
        this.cover = inputStream;
    }

    /* JADX INFO: compiled from: PdfFile.kt */
    /* JADX INFO: loaded from: reader-pro-classes-3.2.14.jar:io/legado/app/model/localBook/PdfFile$Companion.class */
    @Metadata(mv = {1, 5, 1}, k = 1, xi = 48, d1 = {"\u00006\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u000e\n\u0002\b\u0003\n\u0002\u0010\u0002\n\u0000\n\u0002\u0010\u000b\n\u0000\b\u0086\u0003\u0018\u00002\u00020\u0001B\u0007\b\u0002¢\u0006\u0002\u0010\u0002J\u0014\u0010\u0005\u001a\b\u0012\u0004\u0012\u00020\u00070\u00062\u0006\u0010\b\u001a\u00020\tJ\u0018\u0010\n\u001a\u0004\u0018\u00010\u000b2\u0006\u0010\b\u001a\u00020\t2\u0006\u0010\f\u001a\u00020\u0007J\u0010\u0010\r\u001a\u00020\u00042\u0006\u0010\b\u001a\u00020\tH\u0002J\u0018\u0010\u000e\u001a\u00020\u000f2\u0006\u0010\b\u001a\u00020\t2\b\b\u0002\u0010\u0010\u001a\u00020\u0011R\u0010\u0010\u0003\u001a\u0004\u0018\u00010\u0004X\u0082\u000e¢\u0006\u0002\n\u0000¨\u0006\u0012"}, d2 = {"Lio/legado/app/model/localBook/PdfFile$Companion;", PackageDocumentBase.PREFIX_OPF, "()V", "cFile", "Lio/legado/app/model/localBook/PdfFile;", "getChapterList", "Ljava/util/ArrayList;", "Lio/legado/app/data/entities/BookChapter;", "book", "Lio/legado/app/data/entities/Book;", "getContent", PackageDocumentBase.PREFIX_OPF, NCXDocumentV2.NCXAttributeValues.chapter, "getPdfFile", "upBookInfo", PackageDocumentBase.PREFIX_OPF, "onlyCover", PackageDocumentBase.PREFIX_OPF, "reader-pro"})
    public static final class Companion {
        public /* synthetic */ Companion(DefaultConstructorMarker $constructor_marker) {
            this();
        }

        private Companion() {
        }

        private final synchronized PdfFile getPdfFile(Book book) {
            if (PdfFile.cFile != null) {
                PdfFile pdfFile = PdfFile.cFile;
                if (Intrinsics.areEqual(pdfFile == null ? null : pdfFile.getBook().getBookUrl(), book.getBookUrl())) {
                    PdfFile pdfFile2 = PdfFile.cFile;
                    if (pdfFile2 != null) {
                        pdfFile2.setBook(book);
                    }
                    PdfFile pdfFile3 = PdfFile.cFile;
                    Intrinsics.checkNotNull(pdfFile3);
                    return pdfFile3;
                }
            }
            PdfFile.cFile = new PdfFile(book);
            PdfFile pdfFile4 = PdfFile.cFile;
            Intrinsics.checkNotNull(pdfFile4);
            return pdfFile4;
        }

        @NotNull
        public final synchronized ArrayList<BookChapter> getChapterList(@NotNull Book book) {
            Intrinsics.checkNotNullParameter(book, "book");
            return getPdfFile(book).getChapterList();
        }

        @Nullable
        public final synchronized String getContent(@NotNull Book book, @NotNull BookChapter chapter) {
            Intrinsics.checkNotNullParameter(book, "book");
            Intrinsics.checkNotNullParameter(chapter, NCXDocumentV2.NCXAttributeValues.chapter);
            return getPdfFile(book).getContent(chapter);
        }

        public static /* synthetic */ void upBookInfo$default(Companion companion, Book book, boolean z, int i, Object obj) {
            if ((i & 2) != 0) {
                z = false;
            }
            companion.upBookInfo(book, z);
        }

        public final synchronized void upBookInfo(@NotNull Book book, boolean onlyCover) {
            Intrinsics.checkNotNullParameter(book, "book");
            if (onlyCover) {
                getPdfFile(book).updateCover();
            } else {
                getPdfFile(book).upBookInfo();
            }
        }
    }

    private final Pair<Map<String, Object>, InputStream> parseBookInfo() {
        return new Pair<>(this.info, this.cover);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public final void upBookInfo() {
        Pair<Map<String, Object>, InputStream> bookInfo = parseBookInfo();
        if (bookInfo.getFirst() != null) {
            Object first = bookInfo.getFirst();
            if (first == null) {
                throw new NullPointerException("null cannot be cast to non-null type kotlin.collections.Map<kotlin.String, kotlin.Any>");
            }
            Map bookInfo2 = (Map) first;
            Map map = (Map) bookInfo2.get("ComicInfo");
            Map info = map == null ? null : map;
            Book book = this.book;
            Object obj = info == null ? null : info.get("Title");
            book.setName((String) (obj == null ? this.book.getName() : obj));
            Book book2 = this.book;
            Object obj2 = info == null ? null : info.get("Writer");
            book2.setAuthor((String) (obj2 == null ? this.book.getAuthor() : obj2));
        }
        updateCover();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public final void updateCover() {
    }

    /* JADX INFO: Access modifiers changed from: private */
    public final String getContent(BookChapter chapter) {
        return PackageDocumentBase.PREFIX_OPF;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public final ArrayList<BookChapter> getChapterList() {
        if (this.book.getTocUrl().length() == 0) {
            this.book.setTocUrl("page");
        }
        if (Intrinsics.areEqual(this.book.getTocUrl(), "page")) {
            return getChapterListByPage();
        }
        return getChapterListByOutline();
    }

    private final ArrayList<BookChapter> getChapterListByPage() {
        ArrayList<BookChapter> arrayList = new ArrayList<>();
        Closeable closeableLoad = PDDocument.load(this.book.getLocalFile());
        int i = 0;
        int numberOfPages = closeableLoad.getNumberOfPages();
        if (0 < numberOfPages) {
            do {
                int pageIndex = i;
                i++;
                String name = "output-" + pageIndex + ".png";
                BookChapter chapter = new BookChapter(null, null, false, null, null, 0, null, null, null, null, null, null, null, 8191, null);
                chapter.setTitle(name);
                chapter.setIndex(pageIndex);
                chapter.setBookUrl(this.book.getBookUrl());
                chapter.setUrl(name);
                chapter.setStart(Long.valueOf(pageIndex));
                chapter.setEnd(Long.valueOf(pageIndex));
                arrayList.add(chapter);
            } while (i < numberOfPages);
        }
        Book book = this.book;
        BookChapter bookChapter = (BookChapter) CollectionsKt.lastOrNull(arrayList);
        book.setLatestChapterTitle(bookChapter == null ? null : bookChapter.getTitle());
        this.book.setTotalChapterNum(arrayList.size());
        Intrinsics.checkNotNullExpressionValue(closeableLoad, "document");
        Util.closeQuietly(closeableLoad);
        return arrayList;
    }

    private final ArrayList<BookChapter> getChapterListByOutline() {
        ArrayList<BookChapter> arrayList = new ArrayList<>();
        Closeable closeableLoad = PDDocument.load(this.book.getLocalFile());
        PDDocumentOutline outline = closeableLoad.getDocumentCatalog().getDocumentOutline();
        if (outline == null) {
            return arrayList;
        }
        Intrinsics.checkNotNullExpressionValue(closeableLoad, "document");
        processOutline(closeableLoad, arrayList, (PDOutlineNode) outline);
        if (arrayList.size() > 0) {
            arrayList.get(arrayList.size() - 1).setEnd(Long.valueOf(closeableLoad.getNumberOfPages()));
        }
        Util.closeQuietly(closeableLoad);
        return arrayList;
    }

    private final void processOutline(PDDocument document, ArrayList<BookChapter> chapterList, PDOutlineNode outline) {
        PDOutlineItem firstChild = outline.getFirstChild();
        while (true) {
            PDOutlineItem current = firstChild;
            if (current != null) {
                PDPage page = current.findDestinationPage(document);
                int pageIndex = document.getDocumentCatalog().getPages().indexOf(page);
                if (chapterList.size() == 0 && pageIndex >= 1) {
                    BookChapter chapter = new BookChapter(null, null, false, null, null, 0, null, null, null, null, null, null, null, 8191, null);
                    chapter.setTitle("首章");
                    chapter.setIndex(0);
                    chapter.setBookUrl(this.book.getBookUrl());
                    chapter.setUrl("chapter-0");
                    chapter.setStart(0L);
                    chapter.setEnd(Long.valueOf(pageIndex));
                    chapterList.add(chapter);
                }
                if (chapterList.size() > 0) {
                    Long start = chapterList.get(chapterList.size() - 1).getStart();
                    long j = pageIndex;
                    if (start != null && start.longValue() == j) {
                        firstChild = current.getNextSibling();
                    } else {
                        BookChapter chapter2 = new BookChapter(null, null, false, null, null, 0, null, null, null, null, null, null, null, 8191, null);
                        String title = current.getTitle();
                        Intrinsics.checkNotNullExpressionValue(title, "current.getTitle()");
                        chapter2.setTitle(title);
                        chapter2.setIndex(chapterList.size());
                        chapter2.setBookUrl(this.book.getBookUrl());
                        chapter2.setUrl(Intrinsics.stringPlus("chapter-", Integer.valueOf(chapterList.size())));
                        chapter2.setStart(Long.valueOf(pageIndex));
                        chapterList.get(chapterList.size() - 1).setEnd(Long.valueOf(((long) pageIndex) - 1));
                        chapterList.add(chapter2);
                    }
                }
                if (current.hasChildren()) {
                    processOutline(document, chapterList, (PDOutlineNode) current);
                }
                firstChild = current.getNextSibling();
            } else {
                return;
            }
        }
    }
}
