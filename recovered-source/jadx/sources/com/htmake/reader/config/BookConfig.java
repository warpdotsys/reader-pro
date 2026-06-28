package com.htmake.reader.config;

import java.io.File;
import java.nio.charset.Charset;
import kotlin.Metadata;
import kotlin.io.FilesKt;
import kotlin.jvm.internal.Intrinsics;
import kotlin.text.StringsKt;
import me.ag2s.epublib.epub.PackageDocumentBase;
import org.jetbrains.annotations.NotNull;

/* JADX INFO: compiled from: BookConfig.kt */
/* JADX INFO: loaded from: app-classes.jar:com/htmake/reader/config/BookConfig.class */
@Metadata(mv = {1, 5, 1}, k = 1, xi = 48, d1 = {"\u0000\u001c\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\b\u0002\n\u0002\u0010\u000e\n\u0002\b\u0005\n\u0002\u0010\u0002\n\u0002\b\u0002\bÆ\u0002\u0018\u00002\u00020\u0001B\u0007\b\u0002¢\u0006\u0002\u0010\u0002J\u000e\u0010\t\u001a\u00020\n2\u0006\u0010\u000b\u001a\u00020\u0004R\u0014\u0010\u0003\u001a\u00020\u0004X\u0086D¢\u0006\b\n\u0000\u001a\u0004\b\u0005\u0010\u0006R\u0014\u0010\u0007\u001a\u00020\u0004X\u0086D¢\u0006\b\n\u0000\u001a\u0004\b\b\u0010\u0006¨\u0006\f"}, d2 = {"Lcom/htmake/reader/config/BookConfig;", PackageDocumentBase.PREFIX_OPF, "()V", "epubInjectJavascript", PackageDocumentBase.PREFIX_OPF, "getEpubInjectJavascript", "()Ljava/lang/String;", "javascriptVersion", "getJavascriptVersion", "injectJavascriptToEpubChapter", PackageDocumentBase.PREFIX_OPF, "filePath", "reader-pro"})
public final class BookConfig {

    @NotNull
    public static final BookConfig INSTANCE = new BookConfig();

    @NotNull
    private static final String javascriptVersion = "reader-inject-javascript-1.9.0";

    @NotNull
    private static final String epubInjectJavascript;

    private BookConfig() {
    }

    @NotNull
    public final String getJavascriptVersion() {
        return javascriptVersion;
    }

    static {
        StringBuilder sbAppend = new StringBuilder().append("\n    //<![CDATA[\n    // ");
        BookConfig bookConfig = INSTANCE;
        epubInjectJavascript = sbAppend.append(javascriptVersion).append("\n    if (!window.reader_inited) {\n        function reader_notify(event, data, id) {\n            if (window.self !== window.top) {\n                window.top.postMessage(JSON.stringify({\n                    id: id,\n                    event: event,\n                    data: data\n                }), '*');\n            }\n        }\n\n        var reader_style_dom = document.createElement('style');\n        var head = document.head || document.getElementsByTagName('head')[0];\n        head.appendChild(reader_style_dom);\n\n        function reader_setStyle(style) {\n            reader_style_dom.textContent = style;\n            reader_notifySize();\n            setTimeout(reader_notifySize, 100);\n        }\n\n        function reader_notifySize() {\n            reader_notify(\"setHeight\", document.documentElement.scrollHeight || document.body.scrollHeight)\n            reader_notify(\"setWidth\", document.documentElement.scrollWidth || document.body.scrollWidth)\n        }\n\n        function reader_listenFromParent(event) {\n            reader_notify('received', {\n                data: event.data\n            })\n            let data;\n            try {\n                data = typeof event.data === 'string' ? JSON.parse(event.data) : event.data;\n            } catch (error) {\n                // console.error(error);\n                return;\n            }\n\n            if (!data) {\n                return;\n            }\n            reader_notify(\"data \", data);\n            if (data.event === 'setStyle') {\n                reader_setStyle(data.style);\n            } else if (data.event === 'execute') {\n                eval(data.script);\n            } else if (data.id) {\n                if (window.nativeCallback[data.id]) {\n                    window.nativeCallback[data.id](data);\n                    delete window.nativeCallback[data.id]\n                }\n            }\n        }\n\n\n        function reader_getLinkElement(element) {\n            if (!element || !element.nodeName) {\n                return false;\n            }\n            if (element.nodeName.toLowerCase() === \"a\") {\n                return element;\n            }\n            return reader_getLinkElement(element.parentNode)\n        }\n\n        function reader_getImageElement(element) {\n            if (!element || !element.nodeName) {\n                return false;\n            }\n            if (element.nodeName.toLowerCase() === \"img\") {\n                return element;\n            }\n        }\n\n        window.document.addEventListener('message', reader_listenFromParent);\n        window.addEventListener('message', reader_listenFromParent);\n        window.addEventListener('load', function() {\n            reader_notifySize();\n            reader_notify(\"load\", window.location.href);\n        });\n        window.addEventListener('resize', reader_notifySize);\n        document.addEventListener('DOMNodeInserted', reader_notifySize, false);\n        document.addEventListener('click', function(event) {\n            var linkElement = reader_getLinkElement(event.target)\n            var imageElement = reader_getImageElement(event.target)\n            if (linkElement) {\n                // 点击链接跳转\n                if (linkElement.pathname === window.location.pathname) {\n                    // 页内跳转\n                    var hashElement = document.querySelector(linkElement.hash)\n                    if (hashElement) {\n                        reader_notify(\"clickHash\", hashElement.getBoundingClientRect())\n                    }\n                } else {\n                    // 跳转其他页面\n                    reader_notify(\"clickA\", event.target.href);\n                }\n            } else if (imageElement) {\n                var imgList = document.querySelectorAll(\"img\");\n                if (imgList.length) {\n                    var imgUrlList = [];\n                    var index = 0;\n                    for (let i = 0; i < imgList.length; i++) {\n                        imgUrlList.push(imgList[i].src);\n                        if (imgList[i] === imageElement) {\n                            index = i;\n                        }\n                    }\n                    reader_notify(\"previewImageList\", {\n                        imageList: imgUrlList,\n                        imageIndex: index\n                    });\n                }\n            } else {\n                reader_notify(\"click\", {\n                    target: event.target.nodeName,\n                    clientX: event.clientX,\n                    clientY: event.clientY\n                });\n            }\n        });\n        document.addEventListener(\"touchstart\", function(event) {\n            // console.log(event)\n            reader_notify(\"touchstart\", {\n                target: event.target.nodeName,\n                touches: [{\n                    clientX: event.touches[0].clientX,\n                    clientY: event.touches[0].clientY,\n                }]\n            });\n        });\n        document.addEventListener(\"touchmove\", function(event) {\n            // console.log(event)\n            event.preventDefault && event.preventDefault();\n            event.stopPropagation && event.stopPropagation();\n            reader_notify(\"touchmove\", {\n                target: event.target.nodeName,\n                touches: [{\n                    clientX: event.touches[0].clientX,\n                    clientY: event.touches[0].clientY,\n                }]\n            });\n\n        });\n        document.addEventListener(\"touchend\", function(event) {\n            // console.log(event)\n            reader_notify(\"touchend\", {\n                target: event.target.nodeName\n            });\n        });\n        window.addEventListener(\"keydown\", function(event) {\n            event.preventDefault();\n            event.stopPropagation();\n            reader_notify(\"keydown\", {\n                key: event.key,\n                keyCode: event.keyCode\n            });\n        });\n        reader_notify(\"inited\");\n\n        window.reader_inited = true;\n    }\n    //]]>\n    ").toString();
    }

    @NotNull
    public final String getEpubInjectJavascript() {
        return epubInjectJavascript;
    }

    public final void injectJavascriptToEpubChapter(@NotNull String filePath) {
        Intrinsics.checkNotNullParameter(filePath, "filePath");
        File file = new File(filePath);
        if (file.exists()) {
            String content = FilesKt.readText$default(file, (Charset) null, 1, (Object) null);
            if (StringsKt.indexOf$default(content, javascriptVersion, 0, false, 6, (Object) null) < 0) {
                FilesKt.writeText$default(file, StringsKt.replace$default(content, "<head>", "<head><script type=\"text/javascript\">" + epubInjectJavascript + "</script>", false, 4, (Object) null), (Charset) null, 2, (Object) null);
            }
        }
    }
}
