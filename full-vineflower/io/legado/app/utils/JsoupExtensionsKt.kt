package io.legado.app.utils

import kotlin.jvm.internal.Intrinsics
import org.jetbrains.annotations.NotNull
import org.jsoup.internal.StringUtil
import org.jsoup.nodes.CDataNode
import org.jsoup.nodes.Element
import org.jsoup.nodes.Node
import org.jsoup.nodes.TextNode
import org.jsoup.select.NodeTraversor
import org.jsoup.select.NodeVisitor

public fun Element.textArray(): Array<String> {
   val sb: StringBuilder = StringUtil.borrowBuilder();
   NodeTraversor.traverse(new NodeVisitor(sb) {
      {
         this.$sb = `$sb`;
      }

      @Override
      public void head(@NotNull Node node, int depth) {
         if (node is TextNode) {
            val var3: StringBuilder = this.$sb;
            JsoupExtensionsKt.access$appendNormalisedText(var3, node as TextNode);
         } else if (node is Element) {
            var var5: StringBuilder = this.$sb;
            if (var5.length() > 0 && ((node as Element).isBlock() || (node as Element).tag().getName() == "br")) {
               var5 = this.$sb;
               if (!JsoupExtensionsKt.access$lastCharIsWhitespace(var5)) {
                  this.$sb.append("\n");
               }
            }
         }
      }

      @Override
      public void tail(@NotNull Node node, int depth) {
         if (node is Element && (node as Element).isBlock() && node.nextSibling() is TextNode) {
            val var3: StringBuilder = this.$sb;
            if (!JsoupExtensionsKt.access$lastCharIsWhitespace(var3)) {
               this.$sb.append("\n");
            }
         }
      }
   }, `$this$textArray`);
   val `$this$trim$iv`: java.lang.String = StringUtil.releaseBuilder(sb);
   val `$this$trim$iv$iv`: java.lang.CharSequence = `$this$trim$iv`;
   var `startIndex$iv$iv`: Int = 0;
   var `endIndex$iv$iv`: Int = `$this$trim$iv$iv`.length() - 1;
   var `startFound$iv$iv`: Boolean = false;

   while (startIndex$iv$iv <= endIndex$iv$iv) {
      val var14: Boolean = Intrinsics.compare(`$this$trim$iv$iv`.charAt(if (!`startFound$iv$iv`) `startIndex$iv$iv` else `endIndex$iv$iv`), 32) <= 0;
      if (!`startFound$iv$iv`) {
         if (!var14) {
            `startFound$iv$iv` = true;
         } else {
            `startIndex$iv$iv`++;
         }
      } else {
         if (!var14) {
            break;
         }

         `endIndex$iv$iv`--;
      }
   }

   return StringExtensionsKt.splitNotBlank(`$this$trim$iv$iv`.subSequence(`startIndex$iv$iv`, `endIndex$iv$iv` + 1).toString(), "\n");
}

private fun appendNormalisedText(sb: StringBuilder, textNode: TextNode) {
   val text: java.lang.String = textNode.getWholeText();
   if (!preserveWhitespace(textNode.parentNode()) && textNode !is CDataNode) {
      StringUtil.appendNormalisedWhitespace(sb, text, lastCharIsWhitespace(sb));
   } else {
      sb.append(text);
   }
}

private fun preserveWhitespace(node: Node?): Boolean {
   if (node is Element) {
      var el: Element = node as Element;
      val i: Int = 0;

      do {
         if (el.tag().preserveWhitespace()) {
            return true;
         }

         el = el.parent();
      } while (++i < 6 && el != null);
   }

   return false;
}

private fun lastCharIsWhitespace(sb: StringBuilder): Boolean {
   return sb.length() > 0 && sb.charAt(sb.length() - 1) == ' ';
}

@JvmSynthetic
fun `access$appendNormalisedText`(sb: StringBuilder, textNode: TextNode) {
   appendNormalisedText(sb, textNode);
}

@JvmSynthetic
fun `access$lastCharIsWhitespace`(sb: StringBuilder): Boolean {
   return lastCharIsWhitespace(sb);
}
