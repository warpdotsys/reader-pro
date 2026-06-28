package io.legado.app.utils;

import kotlin.Metadata;
import kotlin.jvm.internal.Intrinsics;
import me.ag2s.epublib.epub.PackageDocumentBase;
import org.jetbrains.annotations.NotNull;
import org.jsoup.internal.StringUtil;
import org.jsoup.nodes.CDataNode;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.Node;
import org.jsoup.nodes.TextNode;
import org.jsoup.select.NodeTraversor;
import org.jsoup.select.NodeVisitor;

/* JADX INFO: compiled from: JsoupExtensions.kt */
/* JADX INFO: loaded from: app-classes.jar:io/legado/app/utils/JsoupExtensionsKt.class */
@Metadata(mv = {1, 5, 1}, k = 2, xi = 48, d1 = {"\u00006\n\u0000\n\u0002\u0010\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u000b\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u0011\n\u0002\u0010\u000e\n\u0002\u0018\u0002\n\u0002\b\u0002\u001a\u001c\u0010\u0000\u001a\u00020\u00012\n\u0010\u0002\u001a\u00060\u0003j\u0002`\u00042\u0006\u0010\u0005\u001a\u00020\u0006H\u0002\u001a\u0010\u0010\u0007\u001a\u00020\b2\u0006\u0010\u0002\u001a\u00020\u0003H\u0002\u001a\u0012\u0010\t\u001a\u00020\b2\b\u0010\n\u001a\u0004\u0018\u00010\u000bH\u0002\u001a\u0015\u0010\f\u001a\b\u0012\u0004\u0012\u00020\u000e0\r*\u00020\u000f¢\u0006\u0002\u0010\u0010¨\u0006\u0011"}, d2 = {"appendNormalisedText", PackageDocumentBase.PREFIX_OPF, "sb", "Ljava/lang/StringBuilder;", "Lkotlin/text/StringBuilder;", "textNode", "Lorg/jsoup/nodes/TextNode;", "lastCharIsWhitespace", PackageDocumentBase.PREFIX_OPF, "preserveWhitespace", "node", "Lorg/jsoup/nodes/Node;", "textArray", PackageDocumentBase.PREFIX_OPF, PackageDocumentBase.PREFIX_OPF, "Lorg/jsoup/nodes/Element;", "(Lorg/jsoup/nodes/Element;)[Ljava/lang/String;", "reader-pro"})
public final class JsoupExtensionsKt {
    @NotNull
    public static final String[] textArray(@NotNull Element $this$textArray) {
        Intrinsics.checkNotNullParameter($this$textArray, "<this>");
        final StringBuilder sb = StringUtil.borrowBuilder();
        NodeTraversor.traverse(new NodeVisitor() { // from class: io.legado.app.utils.JsoupExtensionsKt.textArray.1
            public void head(@NotNull Node node, int depth) {
                Intrinsics.checkNotNullParameter(node, "node");
                if (node instanceof TextNode) {
                    StringBuilder sb2 = sb;
                    Intrinsics.checkNotNullExpressionValue(sb2, "sb");
                    JsoupExtensionsKt.appendNormalisedText(sb2, (TextNode) node);
                } else if (node instanceof Element) {
                    StringBuilder sb3 = sb;
                    Intrinsics.checkNotNullExpressionValue(sb3, "sb");
                    if (sb3.length() > 0) {
                        if (((Element) node).isBlock() || Intrinsics.areEqual(((Element) node).tag().getName(), "br")) {
                            StringBuilder sb4 = sb;
                            Intrinsics.checkNotNullExpressionValue(sb4, "sb");
                            if (!JsoupExtensionsKt.lastCharIsWhitespace(sb4)) {
                                sb.append("\n");
                            }
                        }
                    }
                }
            }

            public void tail(@NotNull Node node, int depth) {
                Intrinsics.checkNotNullParameter(node, "node");
                if ((node instanceof Element) && ((Element) node).isBlock() && (node.nextSibling() instanceof TextNode)) {
                    StringBuilder sb2 = sb;
                    Intrinsics.checkNotNullExpressionValue(sb2, "sb");
                    if (!JsoupExtensionsKt.lastCharIsWhitespace(sb2)) {
                        sb.append("\n");
                    }
                }
            }
        }, (Node) $this$textArray);
        CharSequence $this$trim$iv = StringUtil.releaseBuilder(sb);
        Intrinsics.checkNotNullExpressionValue($this$trim$iv, "releaseBuilder(sb)");
        CharSequence $this$trim$iv$iv = $this$trim$iv;
        int startIndex$iv$iv = 0;
        int endIndex$iv$iv = $this$trim$iv$iv.length() - 1;
        boolean startFound$iv$iv = false;
        while (startIndex$iv$iv <= endIndex$iv$iv) {
            int index$iv$iv = !startFound$iv$iv ? startIndex$iv$iv : endIndex$iv$iv;
            char it = $this$trim$iv$iv.charAt(index$iv$iv);
            boolean match$iv$iv = Intrinsics.compare(it, 32) <= 0;
            if (!startFound$iv$iv) {
                if (!match$iv$iv) {
                    startFound$iv$iv = true;
                } else {
                    startIndex$iv$iv++;
                }
            } else {
                if (!match$iv$iv) {
                    break;
                }
                endIndex$iv$iv--;
            }
        }
        String text = $this$trim$iv$iv.subSequence(startIndex$iv$iv, endIndex$iv$iv + 1).toString();
        return StringExtensionsKt.splitNotBlank(text, "\n");
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static final void appendNormalisedText(StringBuilder sb, TextNode textNode) {
        String text = textNode.getWholeText();
        if (preserveWhitespace(textNode.parentNode()) || (textNode instanceof CDataNode)) {
            sb.append(text);
        } else {
            StringUtil.appendNormalisedWhitespace(sb, text, lastCharIsWhitespace(sb));
        }
    }

    private static final boolean preserveWhitespace(Node node) {
        if (node instanceof Element) {
            Element el = (Element) node;
            int i = 0;
            while (!el.tag().preserveWhitespace()) {
                el = el.parent();
                i++;
                if (i >= 6 || el == null) {
                    return false;
                }
            }
            return true;
        }
        return false;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static final boolean lastCharIsWhitespace(StringBuilder sb) {
        return (sb.length() > 0) && sb.charAt(sb.length() - 1) == ' ';
    }
}
