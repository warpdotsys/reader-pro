// 
// Decompiled by Procyon v0.6.0
// 

package io.legado.app.utils;

import org.jsoup.nodes.CDataNode;
import org.jsoup.nodes.TextNode;
import org.jsoup.select.NodeTraversor;
import org.jsoup.nodes.Node;
import org.jsoup.select.NodeVisitor;
import org.jsoup.internal.StringUtil;
import kotlin.jvm.internal.Intrinsics;
import org.jetbrains.annotations.NotNull;
import org.jsoup.nodes.Element;
import kotlin.Metadata;

@Metadata(mv = { 1, 5, 1 }, k = 2, xi = 48, d1 = { "\u00006\n\u0000\n\u0002\u0010\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u000b\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u0011\n\u0002\u0010\u000e\n\u0002\u0018\u0002\n\u0002\b\u0002\u001a\u001c\u0010\u0000\u001a\u00020\u00012\n\u0010\u0002\u001a\u00060\u0003j\u0002`\u00042\u0006\u0010\u0005\u001a\u00020\u0006H\u0002\u001a\u0010\u0010\u0007\u001a\u00020\b2\u0006\u0010\u0002\u001a\u00020\u0003H\u0002\u001a\u0012\u0010\t\u001a\u00020\b2\b\u0010\n\u001a\u0004\u0018\u00010\u000bH\u0002\u001a\u0015\u0010\f\u001a\b\u0012\u0004\u0012\u00020\u000e0\r*\u00020\u000f?\u0006\u0002\u0010\u0010¡§\u0006\u0011" }, d2 = { "appendNormalisedText", "", "sb", "Ljava/lang/StringBuilder;", "Lkotlin/text/StringBuilder;", "textNode", "Lorg/jsoup/nodes/TextNode;", "lastCharIsWhitespace", "", "preserveWhitespace", "node", "Lorg/jsoup/nodes/Node;", "textArray", "", "", "Lorg/jsoup/nodes/Element;", "(Lorg/jsoup/nodes/Element;)[Ljava/lang/String;", "reader-pro" })
public final class JsoupExtensionsKt
{
    @NotNull
    public static final String[] textArray(@NotNull final Element $this$textArray) {
        Intrinsics.checkNotNullParameter((Object)$this$textArray, "<this>");
        final StringBuilder sb = StringUtil.borrowBuilder();
        NodeTraversor.traverse((NodeVisitor)new JsoupExtensionsKt$textArray.JsoupExtensionsKt$textArray$1(sb), (Node)$this$textArray);
        final String releaseBuilder = StringUtil.releaseBuilder(sb);
        Intrinsics.checkNotNullExpressionValue((Object)releaseBuilder, "releaseBuilder(sb)");
        final String $this$trim$iv = releaseBuilder;
        final int $i$f$trim = 0;
        final CharSequence $this$trim$iv$iv = $this$trim$iv;
        final int $i$f$trim2 = 0;
        int startIndex$iv$iv = 0;
        int endIndex$iv$iv = $this$trim$iv$iv.length() - 1;
        boolean startFound$iv$iv = false;
        while (startIndex$iv$iv <= endIndex$iv$iv) {
            final int index$iv$iv = startFound$iv$iv ? endIndex$iv$iv : startIndex$iv$iv;
            final char it = $this$trim$iv$iv.charAt(index$iv$iv);
            final int n = 0;
            final boolean match$iv$iv = Intrinsics.compare((int)it, 32) <= 0;
            if (!startFound$iv$iv) {
                if (!match$iv$iv) {
                    startFound$iv$iv = true;
                }
                else {
                    ++startIndex$iv$iv;
                }
            }
            else {
                if (!match$iv$iv) {
                    break;
                }
                --endIndex$iv$iv;
            }
        }
        final String text = $this$trim$iv$iv.subSequence(startIndex$iv$iv, endIndex$iv$iv + 1).toString();
        return StringExtensionsKt.splitNotBlank(text, "\n");
    }
    
    private static final void appendNormalisedText(final StringBuilder sb, final TextNode textNode) {
        final String text = textNode.getWholeText();
        if (preserveWhitespace(textNode.parentNode()) || textNode instanceof CDataNode) {
            sb.append(text);
        }
        else {
            StringUtil.appendNormalisedWhitespace(sb, text, lastCharIsWhitespace(sb));
        }
    }
    
    private static final boolean preserveWhitespace(final Node node) {
        if (node instanceof Element) {
            Element el = (Element)node;
            int i = 0;
            while (!el.tag().preserveWhitespace()) {
                el = el.parent();
                ++i;
                if (i >= 6 || el == null) {
                    return false;
                }
            }
            return true;
        }
        return false;
    }
    
    private static final boolean lastCharIsWhitespace(final StringBuilder sb) {
        return sb.length() > 0 && sb.charAt(sb.length() - 1) == ' ';
    }
}
