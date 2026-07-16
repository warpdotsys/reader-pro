// 
// Decompiled by Procyon v0.6.0
// 

package io.legado.app.model.localBook;

import kotlin.jvm.internal.DefaultConstructorMarker;
import java.util.ArrayList;
import io.legado.app.data.entities.BookChapter;
import java.io.File;
import kotlin.text.StringsKt;
import java.nio.file.Paths;
import io.legado.app.utils.MD5Utils;
import java.util.List;
import java.util.Enumeration;
import java.util.Locale;
import io.legado.app.utils.FileUtils;
import io.legado.app.utils.XmlUtils;
import java.util.zip.ZipEntry;
import kotlin.collections.CollectionsKt;
import java.util.zip.ZipFile;
import kotlin.Pair;
import kotlin.jvm.internal.Intrinsics;
import java.io.InputStream;
import org.jetbrains.annotations.Nullable;
import java.util.Map;
import io.legado.app.data.entities.Book;
import org.jetbrains.annotations.NotNull;
import kotlin.Metadata;

@Metadata(mv = { 1, 5, 1 }, k = 1, xi = 48, d1 = { "\u0000@\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0005\n\u0002\u0018\u0002\n\u0002\b\u0005\n\u0002\u0010%\n\u0002\u0010\u000e\n\u0002\b\u0005\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u0002\n\u0002\b\u0003\u0018\u0000 \u001f2\u00020\u0001:\u0001\u001fB\r\u0012\u0006\u0010\u0002\u001a\u00020\u0003?\u0006\u0002\u0010\u0004J\u000e\u0010\u0015\u001a\b\u0012\u0004\u0012\u00020\u00170\u0016H\u0002J\u0012\u0010\u0018\u001a\u0004\u0018\u00010\u00102\u0006\u0010\u0019\u001a\u00020\u0017H\u0002J$\u0010\u001a\u001a\u001e\u0012\u0012\u0012\u0010\u0012\u0004\u0012\u00020\u0010\u0012\u0004\u0012\u00020\u0001\u0018\u00010\u000f\u0012\u0006\u0012\u0004\u0018\u00010\t0\u001bH\u0002J\b\u0010\u001c\u001a\u00020\u001dH\u0002J\b\u0010\u001e\u001a\u00020\u001dH\u0002R\u001a\u0010\u0002\u001a\u00020\u0003X\u0086\u000e?\u0006\u000e\n\u0000\u001a\u0004\b\u0005\u0010\u0006\"\u0004\b\u0007\u0010\u0004R\u001c\u0010\b\u001a\u0004\u0018\u00010\tX\u0086\u000e?\u0006\u000e\n\u0000\u001a\u0004\b\n\u0010\u000b\"\u0004\b\f\u0010\rR(\u0010\u000e\u001a\u0010\u0012\u0004\u0012\u00020\u0010\u0012\u0004\u0012\u00020\u0001\u0018\u00010\u000fX\u0086\u000e?\u0006\u000e\n\u0000\u001a\u0004\b\u0011\u0010\u0012\"\u0004\b\u0013\u0010\u0014¡§\u0006 " }, d2 = { "Lio/legado/app/model/localBook/CbzFile;", "", "book", "Lio/legado/app/data/entities/Book;", "(Lio/legado/app/data/entities/Book;)V", "getBook", "()Lio/legado/app/data/entities/Book;", "setBook", "cover", "Ljava/io/InputStream;", "getCover", "()Ljava/io/InputStream;", "setCover", "(Ljava/io/InputStream;)V", "info", "", "", "getInfo", "()Ljava/util/Map;", "setInfo", "(Ljava/util/Map;)V", "getChapterList", "Ljava/util/ArrayList;", "Lio/legado/app/data/entities/BookChapter;", "getContent", "chapter", "parseBookInfo", "Lkotlin/Pair;", "upBookInfo", "", "updateCover", "Companion", "reader-pro" })
public final class CbzFile
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
    private static CbzFile cFile;
    
    public CbzFile(@NotNull final Book book) {
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
        if (this.cover != null || this.info != null) {
            return (Pair<Map<String, Object>, InputStream>)new Pair((Object)this.info, (Object)this.cover);
        }
        final ZipFile zf = new ZipFile(this.book.getLocalFile());
        final Enumeration entries = zf.entries();
        final List imageExt = CollectionsKt.listOf((Object[])new String[] { "jpg", "jpeg", "gif", "png", "bmp", "webp", "svg" });
        while (entries.hasMoreElements()) {
            final ZipEntry nextElement = entries.nextElement();
            if (nextElement == null) {
                throw new NullPointerException("null cannot be cast to non-null type java.util.zip.ZipEntry");
            }
            final ZipEntry zipEntry = nextElement;
            if (!zipEntry.isDirectory()) {
                final String name = zipEntry.getName();
                if (name.equals("ComicInfo.xml")) {
                    final InputStream inputStream = zf.getInputStream(zipEntry);
                    final XmlUtils instance = XmlUtils.INSTANCE;
                    final InputStream source = inputStream;
                    Intrinsics.checkNotNullExpressionValue((Object)source, "inputStream");
                    this.info = instance.xml2map(source);
                }
                else if (this.cover == null) {
                    final FileUtils instance2 = FileUtils.INSTANCE;
                    Intrinsics.checkNotNullExpressionValue((Object)name, "name");
                    final String fileExtetion$default = FileUtils.getFileExtetion$default(instance2, name, null, 2, null);
                    if (fileExtetion$default == null) {
                        throw new NullPointerException("null cannot be cast to non-null type java.lang.String");
                    }
                    final String lowerCase = fileExtetion$default.toLowerCase(Locale.ROOT);
                    Intrinsics.checkNotNullExpressionValue((Object)lowerCase, "(this as java.lang.Strin\u2026.toLowerCase(Locale.ROOT)");
                    final String ext = lowerCase;
                    if (imageExt.contains(ext)) {
                        this.cover = zf.getInputStream(zipEntry);
                    }
                }
            }
            if (this.cover != null && this.info != null) {
                break;
            }
        }
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
        final String coverFile = Intrinsics.stringPlus(MD5Utils.INSTANCE.md5Encode16(this.book.getBookUrl()), (Object)".jpg");
        final String relativeCoverUrl = Paths.get("assets", this.book.getUserNameSpace(), "covers", coverFile).toString();
        this.book.setCoverUrl(Intrinsics.stringPlus("/", (Object)StringsKt.replace$default(relativeCoverUrl, "\\", "/", false, 4, (Object)null)));
        final String coverUrl = Paths.get(this.book.workRoot(), "storage", relativeCoverUrl).toString();
        if (!new File(coverUrl).exists()) {
            final Pair result = this.parseBookInfo();
            if (result.getSecond() != null) {
                final Object second = result.getSecond();
                if (second == null) {
                    throw new NullPointerException("null cannot be cast to non-null type java.io.InputStream");
                }
                final InputStream coverStream = (InputStream)second;
                FileUtils.INSTANCE.writeInputStream(coverUrl, coverStream);
            }
        }
    }
    
    private final String getContent(final BookChapter chapter) {
        return "";
    }
    
    private final ArrayList<BookChapter> getChapterList() {
        final ArrayList chapterList = new ArrayList();
        final ZipFile zf = new ZipFile(this.book.getLocalFile());
        final Enumeration entries = zf.entries();
        final ArrayList imageFileList = new ArrayList();
        while (entries.hasMoreElements()) {
            final ZipEntry nextElement = entries.nextElement();
            if (nextElement == null) {
                throw new NullPointerException("null cannot be cast to non-null type java.util.zip.ZipEntry");
            }
            final ZipEntry zipEntry = nextElement;
            if (zipEntry.isDirectory()) {
                continue;
            }
            final String name = zipEntry.getName();
            Intrinsics.checkNotNullExpressionValue((Object)name, "name");
            if (StringsKt.endsWith$default(name, ".xml", false, 2, (Object)null)) {
                continue;
            }
            imageFileList.add(name);
        }
        CollectionsKt.sort((List)imageFileList);
        int j = 0;
        final int size = imageFileList.size();
        if (j < size) {
            do {
                final int i = j;
                ++j;
                final String value = imageFileList.get(i);
                Intrinsics.checkNotNullExpressionValue((Object)value, "imageFileList.get(i)");
                final String name2 = value;
                final BookChapter chapter = new BookChapter(null, null, false, null, null, 0, null, null, null, null, null, null, null, 8191, null);
                chapter.setTitle(name2);
                chapter.setIndex(i);
                chapter.setBookUrl(this.book.getBookUrl());
                chapter.setUrl(name2);
                chapterList.add(chapter);
            } while (j < size);
        }
        final Book book = this.book;
        final BookChapter bookChapter = (BookChapter)CollectionsKt.lastOrNull((List)chapterList);
        book.setLatestChapterTitle((bookChapter == null) ? null : bookChapter.getTitle());
        this.book.setTotalChapterNum(chapterList.size());
        return chapterList;
    }
    
    public static final /* synthetic */ CbzFile access$getCFile$cp() {
        return CbzFile.cFile;
    }
    
    public static final /* synthetic */ void access$setCFile$cp(final CbzFile <set-?>) {
        CbzFile.cFile = <set-?>;
    }
    
    static {
        Companion = new Companion(null);
    }
    
    @Metadata(mv = { 1, 5, 1 }, k = 1, xi = 48, d1 = { "\u00008\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u000e\n\u0002\b\u0002\n\u0002\u0010\u0002\n\u0000\n\u0002\u0010\u000b\n\u0000\b\u0086\u0003\u0018\u00002\u00020\u0001B\u0007\b\u0002?\u0006\u0002\u0010\u0002J\u0010\u0010\u0005\u001a\u00020\u00042\u0006\u0010\u0006\u001a\u00020\u0007H\u0002J\u0014\u0010\b\u001a\b\u0012\u0004\u0012\u00020\n0\t2\u0006\u0010\u0006\u001a\u00020\u0007J\u0018\u0010\u000b\u001a\u0004\u0018\u00010\f2\u0006\u0010\u0006\u001a\u00020\u00072\u0006\u0010\r\u001a\u00020\nJ\u0018\u0010\u000e\u001a\u00020\u000f2\u0006\u0010\u0006\u001a\u00020\u00072\b\b\u0002\u0010\u0010\u001a\u00020\u0011R\u0010\u0010\u0003\u001a\u0004\u0018\u00010\u0004X\u0082\u000e?\u0006\u0002\n\u0000¡§\u0006\u0012" }, d2 = { "Lio/legado/app/model/localBook/CbzFile$Companion;", "", "()V", "cFile", "Lio/legado/app/model/localBook/CbzFile;", "getCbzFile", "book", "Lio/legado/app/data/entities/Book;", "getChapterList", "Ljava/util/ArrayList;", "Lio/legado/app/data/entities/BookChapter;", "getContent", "", "chapter", "upBookInfo", "", "onlyCover", "", "reader-pro" })
    public static final class Companion
    {
        private Companion() {
        }
        
        private final synchronized CbzFile getCbzFile(final Book book) {
            if (CbzFile.access$getCFile$cp() != null) {
                final CbzFile access$getCFile$cp = CbzFile.access$getCFile$cp();
                if (Intrinsics.areEqual((Object)((access$getCFile$cp == null) ? null : access$getCFile$cp.getBook().getBookUrl()), (Object)book.getBookUrl())) {
                    final CbzFile access$getCFile$cp2 = CbzFile.access$getCFile$cp();
                    if (access$getCFile$cp2 != null) {
                        access$getCFile$cp2.setBook(book);
                    }
                    final CbzFile access$getCFile$cp3 = CbzFile.access$getCFile$cp();
                    Intrinsics.checkNotNull((Object)access$getCFile$cp3);
                    return access$getCFile$cp3;
                }
            }
            CbzFile.access$setCFile$cp(new CbzFile(book));
            final CbzFile access$getCFile$cp4 = CbzFile.access$getCFile$cp();
            Intrinsics.checkNotNull((Object)access$getCFile$cp4);
            return access$getCFile$cp4;
        }
        
        @NotNull
        public final synchronized ArrayList<BookChapter> getChapterList(@NotNull final Book book) {
            Intrinsics.checkNotNullParameter((Object)book, "book");
            return this.getCbzFile(book).getChapterList();
        }
        
        @Nullable
        public final synchronized String getContent(@NotNull final Book book, @NotNull final BookChapter chapter) {
            Intrinsics.checkNotNullParameter((Object)book, "book");
            Intrinsics.checkNotNullParameter((Object)chapter, "chapter");
            return this.getCbzFile(book).getContent(chapter);
        }
        
        public final synchronized void upBookInfo(@NotNull final Book book, final boolean onlyCover) {
            Intrinsics.checkNotNullParameter((Object)book, "book");
            if (onlyCover) {
                this.getCbzFile(book).updateCover();
                return;
            }
            this.getCbzFile(book).upBookInfo();
        }
    }
}
