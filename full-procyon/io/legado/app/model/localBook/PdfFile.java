// 
// Decompiled by Procyon v0.6.0
// 

package io.legado.app.model.localBook;

import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.interactive.documentnavigation.outline.PDOutlineItem;
import org.apache.pdfbox.pdmodel.interactive.documentnavigation.outline.PDDocumentOutline;
import org.apache.pdfbox.pdmodel.interactive.documentnavigation.outline.PDOutlineNode;
import okhttp3.internal.Util;
import java.io.Closeable;
import kotlin.collections.CollectionsKt;
import java.util.List;
import kotlin.jvm.internal.DefaultConstructorMarker;
import org.apache.pdfbox.pdmodel.PDDocument;
import java.util.ArrayList;
import io.legado.app.data.entities.BookChapter;
import kotlin.Pair;
import kotlin.jvm.internal.Intrinsics;
import java.io.InputStream;
import org.jetbrains.annotations.Nullable;
import java.util.Map;
import io.legado.app.data.entities.Book;
import org.jetbrains.annotations.NotNull;
import kotlin.Metadata;

@Metadata(mv = { 1, 5, 1 }, k = 1, xi = 48, d1 = { "\u0000N\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0005\n\u0002\u0018\u0002\n\u0002\b\u0005\n\u0002\u0010%\n\u0002\u0010\u000e\n\u0002\b\u0005\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0005\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0004\u0018\u0000 '2\u00020\u0001:\u0001'B\r\u0012\u0006\u0010\u0002\u001a\u00020\u0003?\u0006\u0002\u0010\u0004J\u000e\u0010\u0015\u001a\b\u0012\u0004\u0012\u00020\u00170\u0016H\u0002J\u000e\u0010\u0018\u001a\b\u0012\u0004\u0012\u00020\u00170\u0016H\u0002J\u000e\u0010\u0019\u001a\b\u0012\u0004\u0012\u00020\u00170\u0016H\u0002J\u0012\u0010\u001a\u001a\u0004\u0018\u00010\u00102\u0006\u0010\u001b\u001a\u00020\u0017H\u0002J$\u0010\u001c\u001a\u001e\u0012\u0012\u0012\u0010\u0012\u0004\u0012\u00020\u0010\u0012\u0004\u0012\u00020\u0001\u0018\u00010\u000f\u0012\u0006\u0012\u0004\u0018\u00010\t0\u001dH\u0002J&\u0010\u001e\u001a\u00020\u001f2\u0006\u0010 \u001a\u00020!2\f\u0010\"\u001a\b\u0012\u0004\u0012\u00020\u00170\u00162\u0006\u0010#\u001a\u00020$H\u0002J\b\u0010%\u001a\u00020\u001fH\u0002J\b\u0010&\u001a\u00020\u001fH\u0002R\u001a\u0010\u0002\u001a\u00020\u0003X\u0086\u000e?\u0006\u000e\n\u0000\u001a\u0004\b\u0005\u0010\u0006\"\u0004\b\u0007\u0010\u0004R\u001c\u0010\b\u001a\u0004\u0018\u00010\tX\u0086\u000e?\u0006\u000e\n\u0000\u001a\u0004\b\n\u0010\u000b\"\u0004\b\f\u0010\rR(\u0010\u000e\u001a\u0010\u0012\u0004\u0012\u00020\u0010\u0012\u0004\u0012\u00020\u0001\u0018\u00010\u000fX\u0086\u000e?\u0006\u000e\n\u0000\u001a\u0004\b\u0011\u0010\u0012\"\u0004\b\u0013\u0010\u0014¡§\u0006(" }, d2 = { "Lio/legado/app/model/localBook/PdfFile;", "", "book", "Lio/legado/app/data/entities/Book;", "(Lio/legado/app/data/entities/Book;)V", "getBook", "()Lio/legado/app/data/entities/Book;", "setBook", "cover", "Ljava/io/InputStream;", "getCover", "()Ljava/io/InputStream;", "setCover", "(Ljava/io/InputStream;)V", "info", "", "", "getInfo", "()Ljava/util/Map;", "setInfo", "(Ljava/util/Map;)V", "getChapterList", "Ljava/util/ArrayList;", "Lio/legado/app/data/entities/BookChapter;", "getChapterListByOutline", "getChapterListByPage", "getContent", "chapter", "parseBookInfo", "Lkotlin/Pair;", "processOutline", "", "document", "Lorg/apache/pdfbox/pdmodel/PDDocument;", "chapterList", "outline", "Lorg/apache/pdfbox/pdmodel/interactive/documentnavigation/outline/PDOutlineNode;", "upBookInfo", "updateCover", "Companion", "reader-pro" })
public final class PdfFile
{
    @NotNull
    public static final Companion Companion;
    @NotNull
    private Book book;
    @Nullable
    private Map<String, Object> info;
    @Nullable
    private InputStream cover;
    @Nullable
    private static PdfFile cFile;
    
    public PdfFile(@NotNull final Book book) {
        Intrinsics.checkNotNullParameter((Object)book, "book");
        this.book = book;
    }
    
    @NotNull
    public final Book getBook() {
        return this.book;
    }
    
    public final void setBook(@NotNull final Book <set-?>) {
        Intrinsics.checkNotNullParameter((Object)<set-?>, "<set-?>");
        this.book = <set-?>;
    }
    
    @Nullable
    public final Map<String, Object> getInfo() {
        return this.info;
    }
    
    public final void setInfo(@Nullable final Map<String, Object> <set-?>) {
        this.info = <set-?>;
    }
    
    @Nullable
    public final InputStream getCover() {
        return this.cover;
    }
    
    public final void setCover(@Nullable final InputStream <set-?>) {
        this.cover = <set-?>;
    }
    
    private final Pair<Map<String, Object>, InputStream> parseBookInfo() {
        return (Pair<Map<String, Object>, InputStream>)new Pair((Object)this.info, (Object)this.cover);
    }
    
    private final void upBookInfo() {
        final Pair result = this.parseBookInfo();
        if (result.getFirst() != null) {
            final Object first = result.getFirst();
            if (first == null) {
                throw new NullPointerException("null cannot be cast to non-null type kotlin.collections.Map<kotlin.String, kotlin.Any>");
            }
            final Map bookInfo = (Map)first;
            final Map map = bookInfo.get("ComicInfo");
            final Map info = (map == null) ? null : map;
            final Book book = this.book;
            final Map map2 = info;
            final Object o = (map2 == null) ? null : map2.get("Title");
            book.setName((String)((o == null) ? this.book.getName() : o));
            final Book book2 = this.book;
            final Map map3 = info;
            final Object o2 = (map3 == null) ? null : map3.get("Writer");
            book2.setAuthor((String)((o2 == null) ? this.book.getAuthor() : o2));
        }
        this.updateCover();
    }
    
    private final void updateCover() {
    }
    
    private final String getContent(final BookChapter chapter) {
        return "";
    }
    
    private final ArrayList<BookChapter> getChapterList() {
        if (this.book.getTocUrl().length() == 0) {
            this.book.setTocUrl("page");
        }
        if (Intrinsics.areEqual((Object)this.book.getTocUrl(), (Object)"page")) {
            return this.getChapterListByPage();
        }
        return this.getChapterListByOutline();
    }
    
    private final ArrayList<BookChapter> getChapterListByPage() {
        final ArrayList chapterList = new ArrayList();
        final PDDocument document = PDDocument.load(this.book.getLocalFile());
        int i = 0;
        final int numberOfPages = document.getNumberOfPages();
        if (i < numberOfPages) {
            do {
                final int pageIndex = i;
                ++i;
                final String name = "output-" + pageIndex + ".png";
                final BookChapter chapter = new BookChapter(null, null, false, null, null, 0, null, null, null, null, null, null, null, 8191, null);
                chapter.setTitle(name);
                chapter.setIndex(pageIndex);
                chapter.setBookUrl(this.book.getBookUrl());
                chapter.setUrl(name);
                chapter.setStart((long)pageIndex);
                chapter.setEnd((long)pageIndex);
                chapterList.add(chapter);
            } while (i < numberOfPages);
        }
        final Book book = this.book;
        final BookChapter bookChapter = (BookChapter)CollectionsKt.lastOrNull((List)chapterList);
        book.setLatestChapterTitle((bookChapter == null) ? null : bookChapter.getTitle());
        this.book.setTotalChapterNum(chapterList.size());
        Intrinsics.checkNotNullExpressionValue((Object)document, "document");
        Util.closeQuietly((Closeable)document);
        return chapterList;
    }
    
    private final ArrayList<BookChapter> getChapterListByOutline() {
        final ArrayList chapterList = new ArrayList();
        final PDDocument document = PDDocument.load(this.book.getLocalFile());
        final PDDocumentOutline outline = document.getDocumentCatalog().getDocumentOutline();
        if (outline == null) {
            return chapterList;
        }
        Intrinsics.checkNotNullExpressionValue((Object)document, "document");
        this.processOutline(document, chapterList, (PDOutlineNode)outline);
        if (chapterList.size() > 0) {
            chapterList.get(chapterList.size() - 1).setEnd((long)document.getNumberOfPages());
        }
        Util.closeQuietly((Closeable)document);
        return chapterList;
    }
    
    private final void processOutline(final PDDocument document, final ArrayList<BookChapter> chapterList, final PDOutlineNode outline) {
        PDOutlineItem current = outline.getFirstChild();
        while (current != null) {
            final PDPage page = current.findDestinationPage(document);
            final int pageIndex = document.getDocumentCatalog().getPages().indexOf(page);
            if (chapterList.size() == 0 && pageIndex >= 1) {
                final BookChapter chapter = new BookChapter(null, null, false, null, null, 0, null, null, null, null, null, null, null, 8191, null);
                chapter.setTitle("\u9996\u7ae0");
                chapter.setIndex(0);
                chapter.setBookUrl(this.book.getBookUrl());
                chapter.setUrl("chapter-0");
                chapter.setStart(0L);
                chapter.setEnd((long)pageIndex);
                chapterList.add(chapter);
            }
            if (chapterList.size() > 0) {
                final Long start = chapterList.get(chapterList.size() - 1).getStart();
                final long n = pageIndex;
                if (start != null) {
                    if (start == n) {
                        current = current.getNextSibling();
                        continue;
                    }
                }
                final BookChapter bookChapter;
                final BookChapter chapter = bookChapter = new BookChapter(null, null, false, null, null, 0, null, null, null, null, null, null, null, 8191, null);
                final String title = current.getTitle();
                Intrinsics.checkNotNullExpressionValue((Object)title, "current.getTitle()");
                bookChapter.setTitle(title);
                chapter.setIndex(chapterList.size());
                chapter.setBookUrl(this.book.getBookUrl());
                chapter.setUrl(Intrinsics.stringPlus("chapter-", (Object)chapterList.size()));
                chapter.setStart((long)pageIndex);
                ((BookChapter)chapterList.get(chapterList.size() - 1)).setEnd(pageIndex - 1L);
                chapterList.add(chapter);
            }
            if (current.hasChildren()) {
                this.processOutline(document, chapterList, (PDOutlineNode)current);
            }
            current = current.getNextSibling();
        }
    }
    
    public static final /* synthetic */ PdfFile access$getCFile$cp() {
        return PdfFile.cFile;
    }
    
    public static final /* synthetic */ void access$setCFile$cp(final PdfFile <set-?>) {
        PdfFile.cFile = <set-?>;
    }
    
    static {
        Companion = new Companion(null);
    }
    
    @Metadata(mv = { 1, 5, 1 }, k = 1, xi = 48, d1 = { "\u00006\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u000e\n\u0002\b\u0003\n\u0002\u0010\u0002\n\u0000\n\u0002\u0010\u000b\n\u0000\b\u0086\u0003\u0018\u00002\u00020\u0001B\u0007\b\u0002?\u0006\u0002\u0010\u0002J\u0014\u0010\u0005\u001a\b\u0012\u0004\u0012\u00020\u00070\u00062\u0006\u0010\b\u001a\u00020\tJ\u0018\u0010\n\u001a\u0004\u0018\u00010\u000b2\u0006\u0010\b\u001a\u00020\t2\u0006\u0010\f\u001a\u00020\u0007J\u0010\u0010\r\u001a\u00020\u00042\u0006\u0010\b\u001a\u00020\tH\u0002J\u0018\u0010\u000e\u001a\u00020\u000f2\u0006\u0010\b\u001a\u00020\t2\b\b\u0002\u0010\u0010\u001a\u00020\u0011R\u0010\u0010\u0003\u001a\u0004\u0018\u00010\u0004X\u0082\u000e?\u0006\u0002\n\u0000¡§\u0006\u0012" }, d2 = { "Lio/legado/app/model/localBook/PdfFile$Companion;", "", "()V", "cFile", "Lio/legado/app/model/localBook/PdfFile;", "getChapterList", "Ljava/util/ArrayList;", "Lio/legado/app/data/entities/BookChapter;", "book", "Lio/legado/app/data/entities/Book;", "getContent", "", "chapter", "getPdfFile", "upBookInfo", "", "onlyCover", "", "reader-pro" })
    public static final class Companion
    {
        private Companion() {
        }
        
        private final synchronized PdfFile getPdfFile(final Book book) {
            if (PdfFile.access$getCFile$cp() != null) {
                final PdfFile access$getCFile$cp = PdfFile.access$getCFile$cp();
                if (Intrinsics.areEqual((Object)((access$getCFile$cp == null) ? null : access$getCFile$cp.getBook().getBookUrl()), (Object)book.getBookUrl())) {
                    final PdfFile access$getCFile$cp2 = PdfFile.access$getCFile$cp();
                    if (access$getCFile$cp2 != null) {
                        access$getCFile$cp2.setBook(book);
                    }
                    final PdfFile access$getCFile$cp3 = PdfFile.access$getCFile$cp();
                    Intrinsics.checkNotNull((Object)access$getCFile$cp3);
                    return access$getCFile$cp3;
                }
            }
            PdfFile.access$setCFile$cp(new PdfFile(book));
            final PdfFile access$getCFile$cp4 = PdfFile.access$getCFile$cp();
            Intrinsics.checkNotNull((Object)access$getCFile$cp4);
            return access$getCFile$cp4;
        }
        
        @NotNull
        public final synchronized ArrayList<BookChapter> getChapterList(@NotNull final Book book) {
            Intrinsics.checkNotNullParameter((Object)book, "book");
            return this.getPdfFile(book).getChapterList();
        }
        
        @Nullable
        public final synchronized String getContent(@NotNull final Book book, @NotNull final BookChapter chapter) {
            Intrinsics.checkNotNullParameter((Object)book, "book");
            Intrinsics.checkNotNullParameter((Object)chapter, "chapter");
            return this.getPdfFile(book).getContent(chapter);
        }
        
        public final synchronized void upBookInfo(@NotNull final Book book, final boolean onlyCover) {
            Intrinsics.checkNotNullParameter((Object)book, "book");
            if (onlyCover) {
                this.getPdfFile(book).updateCover();
                return;
            }
            this.getPdfFile(book).upBookInfo();
        }
    }
}
