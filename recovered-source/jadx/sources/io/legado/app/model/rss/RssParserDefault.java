package io.legado.app.model.rss;

import io.legado.app.data.entities.RssArticle;
import io.legado.app.model.DebugLog;
import java.io.IOException;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import kotlin.Metadata;
import kotlin.Pair;
import kotlin.collections.CollectionsKt;
import kotlin.jvm.internal.Intrinsics;
import kotlin.text.StringsKt;
import me.ag2s.epublib.epub.PackageDocumentBase;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;

/* JADX INFO: compiled from: RssParserDefault.kt */
/* JADX INFO: loaded from: reader-pro-classes-3.2.14.jar:io/legado/app/model/rss/RssParserDefault.class */
@Metadata(mv = {1, 5, 1}, k = 1, xi = 48, d1 = {"\u0000*\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\b\u0002\n\u0002\u0010\u000e\n\u0002\b\u000e\n\u0002\u0018\u0002\n\u0002\u0010!\n\u0002\u0018\u0002\n\u0002\b\u0004\n\u0002\u0018\u0002\n\u0000\bÆ\u0002\u0018\u00002\u00020\u0001B\u0007\b\u0002¢\u0006\u0002\u0010\u0002J\u0012\u0010\u0010\u001a\u0004\u0018\u00010\u00042\u0006\u0010\u0011\u001a\u00020\u0004H\u0002J<\u0010\u0012\u001a\u0016\u0012\n\u0012\b\u0012\u0004\u0012\u00020\u00150\u0014\u0012\u0006\u0012\u0004\u0018\u00010\u00040\u00132\u0006\u0010\u0016\u001a\u00020\u00042\u0006\u0010\u0017\u001a\u00020\u00042\u0006\u0010\u0018\u001a\u00020\u00042\b\u0010\u0019\u001a\u0004\u0018\u00010\u001aR\u000e\u0010\u0003\u001a\u00020\u0004X\u0082T¢\u0006\u0002\n\u0000R\u000e\u0010\u0005\u001a\u00020\u0004X\u0082T¢\u0006\u0002\n\u0000R\u000e\u0010\u0006\u001a\u00020\u0004X\u0082T¢\u0006\u0002\n\u0000R\u000e\u0010\u0007\u001a\u00020\u0004X\u0082T¢\u0006\u0002\n\u0000R\u000e\u0010\b\u001a\u00020\u0004X\u0082T¢\u0006\u0002\n\u0000R\u000e\u0010\t\u001a\u00020\u0004X\u0082T¢\u0006\u0002\n\u0000R\u000e\u0010\n\u001a\u00020\u0004X\u0082T¢\u0006\u0002\n\u0000R\u000e\u0010\u000b\u001a\u00020\u0004X\u0082T¢\u0006\u0002\n\u0000R\u000e\u0010\f\u001a\u00020\u0004X\u0082T¢\u0006\u0002\n\u0000R\u000e\u0010\r\u001a\u00020\u0004X\u0082T¢\u0006\u0002\n\u0000R\u000e\u0010\u000e\u001a\u00020\u0004X\u0082T¢\u0006\u0002\n\u0000R\u000e\u0010\u000f\u001a\u00020\u0004X\u0082T¢\u0006\u0002\n\u0000¨\u0006\u001b"}, d2 = {"Lio/legado/app/model/rss/RssParserDefault;", PackageDocumentBase.PREFIX_OPF, "()V", "RSS_ITEM", PackageDocumentBase.PREFIX_OPF, "RSS_ITEM_CATEGORY", "RSS_ITEM_CONTENT", "RSS_ITEM_DESCRIPTION", "RSS_ITEM_ENCLOSURE", "RSS_ITEM_LINK", "RSS_ITEM_PUB_DATE", "RSS_ITEM_THUMBNAIL", "RSS_ITEM_TIME", "RSS_ITEM_TITLE", "RSS_ITEM_TYPE", "RSS_ITEM_URL", "getImageUrl", "input", "parseXML", "Lkotlin/Pair;", PackageDocumentBase.PREFIX_OPF, "Lio/legado/app/data/entities/RssArticle;", "sortName", "xml", "sourceUrl", "debugLog", "Lio/legado/app/model/DebugLog;", "reader-pro"})
public final class RssParserDefault {

    @NotNull
    public static final RssParserDefault INSTANCE = new RssParserDefault();

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
    public final Pair<List<RssArticle>, String> parseXML(@NotNull String sortName, @NotNull String xml, @NotNull String sourceUrl, @Nullable DebugLog debugLog) throws XmlPullParserException, IOException {
        String type;
        Intrinsics.checkNotNullParameter(sortName, "sortName");
        Intrinsics.checkNotNullParameter(xml, "xml");
        Intrinsics.checkNotNullParameter(sourceUrl, "sourceUrl");
        List articleList = new ArrayList();
        RssArticle currentArticle = new RssArticle(null, null, null, 0L, null, null, null, null, null, false, null, 2047, null);
        XmlPullParserFactory factory = XmlPullParserFactory.newInstance("\n        org.kxml2.io.KXmlParser\n        org.kxml2.io.KXmlSerializer\n               ", Thread.currentThread().getContextClassLoader().getClass());
        factory.setNamespaceAware(false);
        XmlPullParser xmlPullParser = factory.newPullParser();
        xmlPullParser.setInput(new StringReader(xml));
        boolean insideItem = false;
        int eventType = xmlPullParser.getEventType();
        while (eventType != 1) {
            if (eventType == 2) {
                if (StringsKt.equals(xmlPullParser.getName(), "item", true)) {
                    insideItem = true;
                } else if (StringsKt.equals(xmlPullParser.getName(), "title", true)) {
                    if (insideItem) {
                        String strNextText = xmlPullParser.nextText();
                        Intrinsics.checkNotNullExpressionValue(strNextText, "xmlPullParser.nextText()");
                        currentArticle.setTitle(StringsKt.trim(strNextText).toString());
                    }
                } else if (StringsKt.equals(xmlPullParser.getName(), "link", true)) {
                    if (insideItem) {
                        String strNextText2 = xmlPullParser.nextText();
                        Intrinsics.checkNotNullExpressionValue(strNextText2, "xmlPullParser.nextText()");
                        currentArticle.setLink(StringsKt.trim(strNextText2).toString());
                    }
                } else if (StringsKt.equals(xmlPullParser.getName(), "media:thumbnail", true)) {
                    if (insideItem) {
                        currentArticle.setImage(xmlPullParser.getAttributeValue(null, "url"));
                    }
                } else if (StringsKt.equals(xmlPullParser.getName(), "enclosure", true)) {
                    if (insideItem && (type = xmlPullParser.getAttributeValue(null, "type")) != null && StringsKt.contains$default(type, "image/", false, 2, (Object) null)) {
                        currentArticle.setImage(xmlPullParser.getAttributeValue(null, "url"));
                    }
                } else if (StringsKt.equals(xmlPullParser.getName(), "description", true)) {
                    if (insideItem) {
                        String description = xmlPullParser.nextText();
                        Intrinsics.checkNotNullExpressionValue(description, "description");
                        currentArticle.setDescription(StringsKt.trim(description).toString());
                        if (currentArticle.getImage() == null) {
                            currentArticle.setImage(getImageUrl(description));
                        }
                    }
                } else if (StringsKt.equals(xmlPullParser.getName(), "content:encoded", true)) {
                    if (insideItem) {
                        String strNextText3 = xmlPullParser.nextText();
                        Intrinsics.checkNotNullExpressionValue(strNextText3, "xmlPullParser.nextText()");
                        String content = StringsKt.trim(strNextText3).toString();
                        currentArticle.setContent(content);
                        if (currentArticle.getImage() == null) {
                            currentArticle.setImage(getImageUrl(content));
                        }
                    }
                } else if (StringsKt.equals(xmlPullParser.getName(), "pubDate", true)) {
                    if (insideItem) {
                        int nextTokenType = xmlPullParser.next();
                        if (nextTokenType == 4) {
                            String text = xmlPullParser.getText();
                            Intrinsics.checkNotNullExpressionValue(text, "xmlPullParser.text");
                            currentArticle.setPubDate(StringsKt.trim(text).toString());
                        }
                    }
                } else if (StringsKt.equals(xmlPullParser.getName(), "time", true) && insideItem) {
                    currentArticle.setPubDate(xmlPullParser.nextText());
                }
            } else if (eventType == 3 && StringsKt.equals(xmlPullParser.getName(), "item", true)) {
                insideItem = false;
                currentArticle.setOrigin(sourceUrl);
                currentArticle.setSort(sortName);
                articleList.add(currentArticle);
                currentArticle = new RssArticle(null, null, null, 0L, null, null, null, null, null, false, null, 2047, null);
            }
            eventType = xmlPullParser.next();
        }
        RssArticle it = (RssArticle) CollectionsKt.firstOrNull(articleList);
        if (it != null) {
            if (debugLog != null) {
                DebugLog.DefaultImpls.log$default(debugLog, sourceUrl, "┌获取标题", false, 4, null);
            }
            if (debugLog != null) {
                DebugLog.DefaultImpls.log$default(debugLog, sourceUrl, Intrinsics.stringPlus("└", it.getTitle()), false, 4, null);
            }
            if (debugLog != null) {
                DebugLog.DefaultImpls.log$default(debugLog, sourceUrl, "┌获取时间", false, 4, null);
            }
            if (debugLog != null) {
                DebugLog.DefaultImpls.log$default(debugLog, sourceUrl, Intrinsics.stringPlus("└", it.getPubDate()), false, 4, null);
            }
            if (debugLog != null) {
                DebugLog.DefaultImpls.log$default(debugLog, sourceUrl, "┌获取描述", false, 4, null);
            }
            if (debugLog != null) {
                DebugLog.DefaultImpls.log$default(debugLog, sourceUrl, Intrinsics.stringPlus("└", it.getDescription()), false, 4, null);
            }
            if (debugLog != null) {
                DebugLog.DefaultImpls.log$default(debugLog, sourceUrl, "┌获取图片url", false, 4, null);
            }
            if (debugLog != null) {
                DebugLog.DefaultImpls.log$default(debugLog, sourceUrl, Intrinsics.stringPlus("└", it.getImage()), false, 4, null);
            }
            if (debugLog != null) {
                DebugLog.DefaultImpls.log$default(debugLog, sourceUrl, "┌获取文章链接", false, 4, null);
            }
            if (debugLog != null) {
                DebugLog.DefaultImpls.log$default(debugLog, sourceUrl, Intrinsics.stringPlus("└", it.getLink()), false, 4, null);
            }
        }
        return new Pair<>(articleList, (Object) null);
    }

    private final String getImageUrl(String input) {
        String url = null;
        Pattern patternImg = Pattern.compile("(<img [^>]*>)", 0);
        Intrinsics.checkNotNullExpressionValue(patternImg, "java.util.regex.Pattern.compile(this, flags)");
        Matcher matcherImg = patternImg.matcher(input);
        if (matcherImg.find()) {
            String imgTag = matcherImg.group(1);
            Pattern patternLink = Pattern.compile("src\\s*=\\s*\"([^\"]+)\"", 0);
            Intrinsics.checkNotNullExpressionValue(patternLink, "java.util.regex.Pattern.compile(this, flags)");
            Intrinsics.checkNotNull(imgTag);
            Matcher matcherLink = patternLink.matcher(imgTag);
            if (matcherLink.find()) {
                String strGroup = matcherLink.group(1);
                Intrinsics.checkNotNull(strGroup);
                if (strGroup == null) {
                    throw new NullPointerException("null cannot be cast to non-null type kotlin.CharSequence");
                }
                url = StringsKt.trim(strGroup).toString();
            }
        }
        return url;
    }
}
