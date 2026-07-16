// 
// Decompiled by Procyon v0.6.0
// 

package io.legado.app.model.localBook;

import java.util.Iterator;
import java.util.List;
import kotlin.jvm.internal.DefaultConstructorMarker;
import kotlin.collections.CollectionsKt;
import java.util.ArrayList;
import me.ag2s.umdlib.domain.UmdChapters;
import io.legado.app.data.entities.BookChapter;
import me.ag2s.umdlib.domain.UmdCover;
import io.legado.app.utils.FileUtils;
import java.io.File;
import kotlin.text.StringsKt;
import java.nio.file.Paths;
import io.legado.app.utils.MD5Utils;
import me.ag2s.umdlib.domain.UmdHeader;
import java.io.InputStream;
import me.ag2s.umdlib.umd.UmdReader;
import kotlin.jvm.internal.Intrinsics;
import org.jetbrains.annotations.Nullable;
import me.ag2s.umdlib.domain.UmdBook;
import io.legado.app.data.entities.Book;
import org.jetbrains.annotations.NotNull;
import kotlin.Metadata;

@Metadata(mv = { 1, 5, 1 }, k = 1, xi = 48, d1 = { "\u0000<\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0005\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u000e\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0010\u0002\n\u0002\b\u0003\u0018\u0000 \u00192\u00020\u0001:\u0001\u0019B\r\u0012\u0006\u0010\u0002\u001a\u00020\u0003?\u0006\u0002\u0010\u0004J\u000e\u0010\f\u001a\b\u0012\u0004\u0012\u00020\u000e0\rH\u0002J\u0012\u0010\u000f\u001a\u0004\u0018\u00010\u00102\u0006\u0010\u0011\u001a\u00020\u000eH\u0002J\u0012\u0010\u0012\u001a\u0004\u0018\u00010\u00132\u0006\u0010\u0014\u001a\u00020\u0010H\u0002J\n\u0010\u0015\u001a\u0004\u0018\u00010\tH\u0002J\b\u0010\u0016\u001a\u00020\u0017H\u0002J\b\u0010\u0018\u001a\u00020\u0017H\u0002R\u001a\u0010\u0002\u001a\u00020\u0003X\u0086\u000e?\u0006\u000e\n\u0000\u001a\u0004\b\u0005\u0010\u0006\"\u0004\b\u0007\u0010\u0004R\u0018\u0010\b\u001a\u0004\u0018\u00010\t8BX\u0082\u000e?\u0006\b\n\u0000\u001a\u0004\b\n\u0010\u000b¡§\u0006\u001a" }, d2 = { "Lio/legado/app/model/localBook/UmdFile;", "", "book", "Lio/legado/app/data/entities/Book;", "(Lio/legado/app/data/entities/Book;)V", "getBook", "()Lio/legado/app/data/entities/Book;", "setBook", "umdBook", "Lme/ag2s/umdlib/domain/UmdBook;", "getUmdBook", "()Lme/ag2s/umdlib/domain/UmdBook;", "getChapterList", "Ljava/util/ArrayList;", "Lio/legado/app/data/entities/BookChapter;", "getContent", "", "chapter", "getImage", "Ljava/io/InputStream;", "href", "readUmd", "upBookInfo", "", "updateCover", "Companion", "reader-pro" })
public final class UmdFile
{
    @NotNull
    public static final Companion Companion;
    @NotNull
    private Book book;
    @Nullable
    private UmdBook umdBook;
    @Nullable
    private static UmdFile uFile;
    
    public UmdFile(@NotNull final Book book) {
        Intrinsics.checkNotNullParameter((Object)book, "book");
        this.book = book;
        try {
            final UmdBook umdBook = this.getUmdBook();
            if (umdBook != null) {
                final UmdBook it = umdBook;
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
    
    private final UmdBook getUmdBook() {
        if (this.umdBook != null) {
            return this.umdBook;
        }
        return this.umdBook = this.readUmd();
    }
    
    private final UmdBook readUmd() {
        final InputStream input = LocalBook.INSTANCE.getBookInputStream(this.book);
        return new UmdReader().read(input);
    }
    
    private final void upBookInfo() {
        if (this.getUmdBook() == null) {
            final Companion companion = UmdFile.Companion;
            UmdFile.uFile = null;
            this.book.setIntro("\u4e66\u7c4d\u5bfc\u5165\u5f02\u5e38");
        }
        else {
            final UmdBook umdBook = this.getUmdBook();
            Intrinsics.checkNotNull((Object)umdBook);
            final UmdHeader hd = umdBook.getHeader();
            final Book book = this.book;
            final String title = hd.getTitle();
            Intrinsics.checkNotNullExpressionValue((Object)title, "hd.title");
            book.setName(title);
            final Book book2 = this.book;
            final String author = hd.getAuthor();
            Intrinsics.checkNotNullExpressionValue((Object)author, "hd.author");
            book2.setAuthor(author);
            this.book.setKind(hd.getBookType());
            this.updateCover();
        }
    }
    
    private final void updateCover() {
        if (this.getUmdBook() == null) {
            final Companion companion = UmdFile.Companion;
            UmdFile.uFile = null;
            return;
        }
        final String coverFile = Intrinsics.stringPlus(MD5Utils.INSTANCE.md5Encode16(this.book.getBookUrl()), (Object)".jpg");
        final String relativeCoverUrl = Paths.get("assets", this.book.getUserNameSpace(), "covers", coverFile).toString();
        this.book.setCoverUrl(Intrinsics.stringPlus("/", (Object)StringsKt.replace$default(relativeCoverUrl, "\\", "/", false, 4, (Object)null)));
        final String coverUrl = Paths.get(this.book.workRoot(), "storage", relativeCoverUrl).toString();
        if (!new File(coverUrl).exists()) {
            final UmdBook umdBook = this.getUmdBook();
            if (umdBook != null) {
                final UmdCover cover = umdBook.getCover();
                if (cover != null) {
                    final byte[] coverData = cover.getCoverData();
                    if (coverData != null) {
                        final byte[] it = coverData;
                        final int n = 0;
                        FileUtils.INSTANCE.writeBytes(coverUrl, it);
                    }
                }
            }
        }
    }
    
    private final String getContent(final BookChapter chapter) {
        final UmdBook umdBook = this.getUmdBook();
        String s;
        if (umdBook == null) {
            s = null;
        }
        else {
            final UmdChapters chapters = umdBook.getChapters();
            s = ((chapters == null) ? null : chapters.getContentString(chapter.getIndex()));
        }
        return s;
    }
    
    private final ArrayList<BookChapter> getChapterList() {
        final ArrayList chapterList = new ArrayList();
        final UmdBook umdBook = this.getUmdBook();
        if (umdBook != null) {
            final UmdChapters chapters = umdBook.getChapters();
            if (chapters != null) {
                final List<byte[]> titles = chapters.getTitles();
                if (titles != null) {
                    final Iterable $this$forEachIndexed$iv = titles;
                    final int $i$f$forEachIndexed = 0;
                    int index$iv = 0;
                    for (final Object item$iv : $this$forEachIndexed$iv) {
                        final int n = index$iv++;
                        if (n < 0) {
                            CollectionsKt.throwIndexOverflow();
                        }
                        final int n2 = n;
                        final byte[] array = (byte[])item$iv;
                        final int index = n2;
                        final int n3 = 0;
                        final UmdBook umdBook2 = this.getUmdBook();
                        Intrinsics.checkNotNull((Object)umdBook2);
                        final String title = umdBook2.getChapters().getTitle(index);
                        final BookChapter bookChapter;
                        final BookChapter chapter = bookChapter = new BookChapter(null, null, false, null, null, 0, null, null, null, null, null, null, null, 8191, null);
                        Intrinsics.checkNotNullExpressionValue((Object)title, "title");
                        bookChapter.setTitle(title);
                        chapter.setIndex(index);
                        chapter.setBookUrl(this.getBook().getBookUrl());
                        chapter.setUrl(String.valueOf(index));
                        System.out.println(Intrinsics.stringPlus("UMD", (Object)chapter.getUrl()));
                        chapterList.add(chapter);
                    }
                }
            }
        }
        final Book book = this.book;
        final BookChapter bookChapter2 = (BookChapter)CollectionsKt.lastOrNull((List)chapterList);
        book.setLatestChapterTitle((bookChapter2 == null) ? null : bookChapter2.getTitle());
        this.book.setTotalChapterNum(chapterList.size());
        return chapterList;
    }
    
    private final InputStream getImage(final String href) {
        return null;
    }
    
    public static final /* synthetic */ UmdFile access$getUFile$cp() {
        return UmdFile.uFile;
    }
    
    public static final /* synthetic */ void access$setUFile$cp(final UmdFile <set-?>) {
        UmdFile.uFile = <set-?>;
    }
    
    static {
        Companion = new Companion(null);
    }
    
    @Metadata(mv = { 1, 5, 1 }, k = 1, xi = 48, d1 = { "\u0000>\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u000e\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0010\u0002\n\u0000\n\u0002\u0010\u000b\n\u0000\b\u0086\u0003\u0018\u00002\u00020\u0001B\u0007\b\u0002?\u0006\u0002\u0010\u0002J\u0014\u0010\u0005\u001a\b\u0012\u0004\u0012\u00020\u00070\u00062\u0006\u0010\b\u001a\u00020\tJ\u0018\u0010\n\u001a\u0004\u0018\u00010\u000b2\u0006\u0010\b\u001a\u00020\t2\u0006\u0010\f\u001a\u00020\u0007J\u0018\u0010\r\u001a\u0004\u0018\u00010\u000e2\u0006\u0010\b\u001a\u00020\t2\u0006\u0010\u000f\u001a\u00020\u000bJ\u0010\u0010\u0010\u001a\u00020\u00042\u0006\u0010\b\u001a\u00020\tH\u0002J\u0018\u0010\u0011\u001a\u00020\u00122\u0006\u0010\b\u001a\u00020\t2\b\b\u0002\u0010\u0013\u001a\u00020\u0014R\u0010\u0010\u0003\u001a\u0004\u0018\u00010\u0004X\u0082\u000e?\u0006\u0002\n\u0000¡§\u0006\u0015" }, d2 = { "Lio/legado/app/model/localBook/UmdFile$Companion;", "", "()V", "uFile", "Lio/legado/app/model/localBook/UmdFile;", "getChapterList", "Ljava/util/ArrayList;", "Lio/legado/app/data/entities/BookChapter;", "book", "Lio/legado/app/data/entities/Book;", "getContent", "", "chapter", "getImage", "Ljava/io/InputStream;", "href", "getUFile", "upBookInfo", "", "onlyCover", "", "reader-pro" })
    public static final class Companion
    {
        private Companion() {
        }
        
        private final synchronized UmdFile getUFile(final Book book) {
            if (UmdFile.access$getUFile$cp() != null) {
                final UmdFile access$getUFile$cp = UmdFile.access$getUFile$cp();
                if (Intrinsics.areEqual((Object)((access$getUFile$cp == null) ? null : access$getUFile$cp.getBook().getBookUrl()), (Object)book.getBookUrl())) {
                    final UmdFile access$getUFile$cp2 = UmdFile.access$getUFile$cp();
                    if (access$getUFile$cp2 != null) {
                        access$getUFile$cp2.setBook(book);
                    }
                    final UmdFile access$getUFile$cp3 = UmdFile.access$getUFile$cp();
                    Intrinsics.checkNotNull((Object)access$getUFile$cp3);
                    return access$getUFile$cp3;
                }
            }
            UmdFile.access$setUFile$cp(new UmdFile(book));
            final UmdFile access$getUFile$cp4 = UmdFile.access$getUFile$cp();
            Intrinsics.checkNotNull((Object)access$getUFile$cp4);
            return access$getUFile$cp4;
        }
        
        @NotNull
        public final synchronized ArrayList<BookChapter> getChapterList(@NotNull final Book book) {
            Intrinsics.checkNotNullParameter((Object)book, "book");
            return this.getUFile(book).getChapterList();
        }
        
        @Nullable
        public final synchronized String getContent(@NotNull final Book book, @NotNull final BookChapter chapter) {
            Intrinsics.checkNotNullParameter((Object)book, "book");
            Intrinsics.checkNotNullParameter((Object)chapter, "chapter");
            return this.getUFile(book).getContent(chapter);
        }
        
        @Nullable
        public final synchronized InputStream getImage(@NotNull final Book book, @NotNull final String href) {
            Intrinsics.checkNotNullParameter((Object)book, "book");
            Intrinsics.checkNotNullParameter((Object)href, "href");
            return this.getUFile(book).getImage(href);
        }
        
        public final synchronized void upBookInfo(@NotNull final Book book, final boolean onlyCover) {
            Intrinsics.checkNotNullParameter((Object)book, "book");
            if (onlyCover) {
                this.getUFile(book).updateCover();
                return;
            }
            this.getUFile(book).upBookInfo();
        }
    }
}
