// 
// Decompiled by Procyon v0.6.0
// 

package io.legado.app.model.rss;

import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.io.IOException;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParser;
import kotlin.collections.CollectionsKt;
import kotlin.text.StringsKt;
import java.io.StringReader;
import java.io.Reader;
import org.xmlpull.v1.XmlPullParserFactory;
import kotlin.jvm.internal.DefaultConstructorMarker;
import java.util.ArrayList;
import kotlin.jvm.internal.Intrinsics;
import io.legado.app.data.entities.RssArticle;
import java.util.List;
import kotlin.Pair;
import org.jetbrains.annotations.Nullable;
import io.legado.app.model.DebugLog;
import org.jetbrains.annotations.NotNull;
import kotlin.Metadata;

@Metadata(mv = { 1, 5, 1 }, k = 1, xi = 48, d1 = { "\u0000*\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\b\u0002\n\u0002\u0010\u000e\n\u0002\b\u000e\n\u0002\u0018\u0002\n\u0002\u0010!\n\u0002\u0018\u0002\n\u0002\b\u0004\n\u0002\u0018\u0002\n\u0000\b\u00c6\u0002\u0018\u00002\u00020\u0001B\u0007\b\u0002?\u0006\u0002\u0010\u0002J\u0012\u0010\u0010\u001a\u0004\u0018\u00010\u00042\u0006\u0010\u0011\u001a\u00020\u0004H\u0002J<\u0010\u0012\u001a\u0016\u0012\n\u0012\b\u0012\u0004\u0012\u00020\u00150\u0014\u0012\u0006\u0012\u0004\u0018\u00010\u00040\u00132\u0006\u0010\u0016\u001a\u00020\u00042\u0006\u0010\u0017\u001a\u00020\u00042\u0006\u0010\u0018\u001a\u00020\u00042\b\u0010\u0019\u001a\u0004\u0018\u00010\u001aR\u000e\u0010\u0003\u001a\u00020\u0004X\u0082T?\u0006\u0002\n\u0000R\u000e\u0010\u0005\u001a\u00020\u0004X\u0082T?\u0006\u0002\n\u0000R\u000e\u0010\u0006\u001a\u00020\u0004X\u0082T?\u0006\u0002\n\u0000R\u000e\u0010\u0007\u001a\u00020\u0004X\u0082T?\u0006\u0002\n\u0000R\u000e\u0010\b\u001a\u00020\u0004X\u0082T?\u0006\u0002\n\u0000R\u000e\u0010\t\u001a\u00020\u0004X\u0082T?\u0006\u0002\n\u0000R\u000e\u0010\n\u001a\u00020\u0004X\u0082T?\u0006\u0002\n\u0000R\u000e\u0010\u000b\u001a\u00020\u0004X\u0082T?\u0006\u0002\n\u0000R\u000e\u0010\f\u001a\u00020\u0004X\u0082T?\u0006\u0002\n\u0000R\u000e\u0010\r\u001a\u00020\u0004X\u0082T?\u0006\u0002\n\u0000R\u000e\u0010\u000e\u001a\u00020\u0004X\u0082T?\u0006\u0002\n\u0000R\u000e\u0010\u000f\u001a\u00020\u0004X\u0082T?\u0006\u0002\n\u0000¡§\u0006\u001b" }, d2 = { "Lio/legado/app/model/rss/RssParserDefault;", "", "()V", "RSS_ITEM", "", "RSS_ITEM_CATEGORY", "RSS_ITEM_CONTENT", "RSS_ITEM_DESCRIPTION", "RSS_ITEM_ENCLOSURE", "RSS_ITEM_LINK", "RSS_ITEM_PUB_DATE", "RSS_ITEM_THUMBNAIL", "RSS_ITEM_TIME", "RSS_ITEM_TITLE", "RSS_ITEM_TYPE", "RSS_ITEM_URL", "getImageUrl", "input", "parseXML", "Lkotlin/Pair;", "", "Lio/legado/app/data/entities/RssArticle;", "sortName", "xml", "sourceUrl", "debugLog", "Lio/legado/app/model/DebugLog;", "reader-pro" })
public final class RssParserDefault
{
    @NotNull
    public static final RssParserDefault INSTANCE;
    @NotNull
    private static final String RSS_ITEM = "item";
    @NotNull
    private static final String RSS_ITEM_TITLE = "title";
    @NotNull
    private static final String RSS_ITEM_LINK = "link";
    @NotNull
    private static final String RSS_ITEM_CATEGORY = "category";
    @NotNull
    private static final String RSS_ITEM_THUMBNAIL = "media:thumbnail";
    @NotNull
    private static final String RSS_ITEM_ENCLOSURE = "enclosure";
    @NotNull
    private static final String RSS_ITEM_DESCRIPTION = "description";
    @NotNull
    private static final String RSS_ITEM_CONTENT = "content:encoded";
    @NotNull
    private static final String RSS_ITEM_PUB_DATE = "pubDate";
    @NotNull
    private static final String RSS_ITEM_TIME = "time";
    @NotNull
    private static final String RSS_ITEM_URL = "url";
    @NotNull
    private static final String RSS_ITEM_TYPE = "type";
    
    private RssParserDefault() {
    }
    
    @NotNull
    public final Pair<List<RssArticle>, String> parseXML(@NotNull final String sortName, @NotNull final String xml, @NotNull final String sourceUrl, @Nullable final DebugLog debugLog) throws XmlPullParserException, IOException {
        Intrinsics.checkNotNullParameter((Object)sortName, "sortName");
        Intrinsics.checkNotNullParameter((Object)xml, "xml");
        Intrinsics.checkNotNullParameter((Object)sourceUrl, "sourceUrl");
        final List articleList = new ArrayList();
        RssArticle currentArticle = new RssArticle(null, null, null, 0L, null, null, null, null, null, false, null, 2047, null);
        final XmlPullParserFactory factory = XmlPullParserFactory.newInstance("\n        org.kxml2.io.KXmlParser\n        org.kxml2.io.KXmlSerializer\n               ", (Class)Thread.currentThread().getContextClassLoader().getClass());
        factory.setNamespaceAware(false);
        final XmlPullParser xmlPullParser = factory.newPullParser();
        xmlPullParser.setInput((Reader)new StringReader(xml));
        boolean insideItem = false;
        int eventType = xmlPullParser.getEventType();
        while (eventType != 1) {
            if (eventType == 2) {
                if (StringsKt.equals(xmlPullParser.getName(), "item", true)) {
                    insideItem = true;
                }
                else if (StringsKt.equals(xmlPullParser.getName(), "title", true)) {
                    if (insideItem) {
                        final RssArticle rssArticle = currentArticle;
                        final String nextText = xmlPullParser.nextText();
                        Intrinsics.checkNotNullExpressionValue((Object)nextText, "xmlPullParser.nextText()");
                        rssArticle.setTitle(StringsKt.trim((CharSequence)nextText).toString());
                    }
                }
                else if (StringsKt.equals(xmlPullParser.getName(), "link", true)) {
                    if (insideItem) {
                        final RssArticle rssArticle2 = currentArticle;
                        final String nextText2 = xmlPullParser.nextText();
                        Intrinsics.checkNotNullExpressionValue((Object)nextText2, "xmlPullParser.nextText()");
                        rssArticle2.setLink(StringsKt.trim((CharSequence)nextText2).toString());
                    }
                }
                else if (StringsKt.equals(xmlPullParser.getName(), "media:thumbnail", true)) {
                    if (insideItem) {
                        currentArticle.setImage(xmlPullParser.getAttributeValue((String)null, "url"));
                    }
                }
                else if (StringsKt.equals(xmlPullParser.getName(), "enclosure", true)) {
                    if (insideItem) {
                        final String type = xmlPullParser.getAttributeValue((String)null, "type");
                        if (type != null && StringsKt.contains$default((CharSequence)type, (CharSequence)"image/", false, 2, (Object)null)) {
                            currentArticle.setImage(xmlPullParser.getAttributeValue((String)null, "url"));
                        }
                    }
                }
                else if (StringsKt.equals(xmlPullParser.getName(), "description", true)) {
                    if (insideItem) {
                        final String description = xmlPullParser.nextText();
                        final RssArticle rssArticle3 = currentArticle;
                        Intrinsics.checkNotNullExpressionValue((Object)description, "description");
                        rssArticle3.setDescription(StringsKt.trim((CharSequence)description).toString());
                        if (currentArticle.getImage() == null) {
                            currentArticle.setImage(this.getImageUrl(description));
                        }
                    }
                }
                else if (StringsKt.equals(xmlPullParser.getName(), "content:encoded", true)) {
                    if (insideItem) {
                        final String nextText3 = xmlPullParser.nextText();
                        Intrinsics.checkNotNullExpressionValue((Object)nextText3, "xmlPullParser.nextText()");
                        final String content = StringsKt.trim((CharSequence)nextText3).toString();
                        currentArticle.setContent(content);
                        if (currentArticle.getImage() == null) {
                            currentArticle.setImage(this.getImageUrl(content));
                        }
                    }
                }
                else if (StringsKt.equals(xmlPullParser.getName(), "pubDate", true)) {
                    if (insideItem) {
                        final int nextTokenType = xmlPullParser.next();
                        if (nextTokenType != 4) {
                            continue;
                        }
                        final RssArticle rssArticle4 = currentArticle;
                        final String text = xmlPullParser.getText();
                        Intrinsics.checkNotNullExpressionValue((Object)text, "xmlPullParser.text");
                        rssArticle4.setPubDate(StringsKt.trim((CharSequence)text).toString());
                        continue;
                    }
                }
                else if (StringsKt.equals(xmlPullParser.getName(), "time", true) && insideItem) {
                    currentArticle.setPubDate(xmlPullParser.nextText());
                }
            }
            else if (eventType == 3 && StringsKt.equals(xmlPullParser.getName(), "item", true)) {
                insideItem = false;
                currentArticle.setOrigin(sourceUrl);
                currentArticle.setSort(sortName);
                articleList.add(currentArticle);
                currentArticle = new RssArticle(null, null, null, 0L, null, null, null, null, null, false, null, 2047, null);
            }
            eventType = xmlPullParser.next();
        }
        final RssArticle rssArticle5 = (RssArticle)CollectionsKt.firstOrNull(articleList);
        if (rssArticle5 != null) {
            final RssArticle it = rssArticle5;
            final int n = 0;
            if (debugLog != null) {
                DebugLog.DefaultImpls.log$default(debugLog, sourceUrl, "\u250c\u83b7\u53d6\u6807\u9898", false, 4, null);
            }
            if (debugLog != null) {
                DebugLog.DefaultImpls.log$default(debugLog, sourceUrl, Intrinsics.stringPlus("\u2514", (Object)it.getTitle()), false, 4, null);
            }
            if (debugLog != null) {
                DebugLog.DefaultImpls.log$default(debugLog, sourceUrl, "\u250c\u83b7\u53d6\u65f6\u95f4", false, 4, null);
            }
            if (debugLog != null) {
                DebugLog.DefaultImpls.log$default(debugLog, sourceUrl, Intrinsics.stringPlus("\u2514", (Object)it.getPubDate()), false, 4, null);
            }
            if (debugLog != null) {
                DebugLog.DefaultImpls.log$default(debugLog, sourceUrl, "\u250c\u83b7\u53d6\u63cf\u8ff0", false, 4, null);
            }
            if (debugLog != null) {
                DebugLog.DefaultImpls.log$default(debugLog, sourceUrl, Intrinsics.stringPlus("\u2514", (Object)it.getDescription()), false, 4, null);
            }
            if (debugLog != null) {
                DebugLog.DefaultImpls.log$default(debugLog, sourceUrl, "\u250c\u83b7\u53d6\u56fe\u7247url", false, 4, null);
            }
            if (debugLog != null) {
                DebugLog.DefaultImpls.log$default(debugLog, sourceUrl, Intrinsics.stringPlus("\u2514", (Object)it.getImage()), false, 4, null);
            }
            if (debugLog != null) {
                DebugLog.DefaultImpls.log$default(debugLog, sourceUrl, "\u250c\u83b7\u53d6\u6587\u7ae0\u94fe\u63a5", false, 4, null);
            }
            if (debugLog != null) {
                DebugLog.DefaultImpls.log$default(debugLog, sourceUrl, Intrinsics.stringPlus("\u2514", (Object)it.getLink()), false, 4, null);
            }
        }
        return (Pair<List<RssArticle>, String>)new Pair((Object)articleList, (Object)null);
    }
    
    private final String getImageUrl(final String input) {
        String url = null;
        final Pattern compile = Pattern.compile("(<img [^>]*>)", 0);
        Intrinsics.checkNotNullExpressionValue((Object)compile, "java.util.regex.Pattern.compile(this, flags)");
        final Pattern patternImg = compile;
        final Matcher matcherImg = patternImg.matcher(input);
        if (matcherImg.find()) {
            final String imgTag = matcherImg.group(1);
            final Pattern compile2 = Pattern.compile("src\\s*=\\s*\"([^\"]+)\"", 0);
            Intrinsics.checkNotNullExpressionValue((Object)compile2, "java.util.regex.Pattern.compile(this, flags)");
            final Pattern pattern;
            final Pattern patternLink = pattern = compile2;
            final String s = imgTag;
            Intrinsics.checkNotNull((Object)s);
            final Matcher matcherLink = pattern.matcher(s);
            if (matcherLink.find()) {
                final String group = matcherLink.group(1);
                Intrinsics.checkNotNull((Object)group);
                final String s2 = group;
                if (s2 == null) {
                    throw new NullPointerException("null cannot be cast to non-null type kotlin.CharSequence");
                }
                url = StringsKt.trim((CharSequence)s2).toString();
            }
        }
        return url;
    }
    
    static {
        INSTANCE = new RssParserDefault();
    }
}
