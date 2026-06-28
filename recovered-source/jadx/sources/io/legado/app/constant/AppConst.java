package io.legado.app.constant;

import com.script.javascript.RhinoScriptEngine;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import kotlin.Lazy;
import kotlin.LazyKt;
import kotlin.Metadata;
import kotlin.collections.CollectionsKt;
import kotlin.jvm.functions.Function0;
import me.ag2s.epublib.domain.TableOfContents;
import me.ag2s.epublib.epub.NCXDocumentV2;
import me.ag2s.epublib.epub.PackageDocumentBase;
import org.jetbrains.annotations.NotNull;

/* JADX INFO: compiled from: AppConst.kt */
/* JADX INFO: loaded from: app-classes.jar:io/legado/app/constant/AppConst.class */
@Metadata(mv = {1, 5, 1}, k = 1, xi = 48, d1 = {"\u0000,\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0005\n\u0002\u0018\u0002\n\u0002\b\u0004\n\u0002\u0010\u000e\n\u0002\b\u0007\n\u0002\u0010 \n\u0002\b\u000b\bÆ\u0002\u0018\u00002\u00020\u0001B\u0007\b\u0002¢\u0006\u0002\u0010\u0002R\u001b\u0010\u0003\u001a\u00020\u00048FX\u0086\u0084\u0002¢\u0006\f\n\u0004\b\u0007\u0010\b\u001a\u0004\b\u0005\u0010\u0006R\u001b\u0010\t\u001a\u00020\n8FX\u0086\u0084\u0002¢\u0006\f\n\u0004\b\r\u0010\b\u001a\u0004\b\u000b\u0010\fR\u000e\u0010\u000e\u001a\u00020\u000fX\u0086T¢\u0006\u0002\n\u0000R\u001b\u0010\u0010\u001a\u00020\n8FX\u0086\u0084\u0002¢\u0006\f\n\u0004\b\u0012\u0010\b\u001a\u0004\b\u0011\u0010\fR\u001b\u0010\u0013\u001a\u00020\n8FX\u0086\u0084\u0002¢\u0006\f\n\u0004\b\u0015\u0010\b\u001a\u0004\b\u0014\u0010\fR!\u0010\u0016\u001a\b\u0012\u0004\u0012\u00020\u000f0\u00178FX\u0086\u0084\u0002¢\u0006\f\n\u0004\b\u001a\u0010\b\u001a\u0004\b\u0018\u0010\u0019R\u001b\u0010\u001b\u001a\u00020\n8FX\u0086\u0084\u0002¢\u0006\f\n\u0004\b\u001d\u0010\b\u001a\u0004\b\u001c\u0010\fR\u001b\u0010\u001e\u001a\u00020\u000f8FX\u0086\u0084\u0002¢\u0006\f\n\u0004\b!\u0010\b\u001a\u0004\b\u001f\u0010 ¨\u0006\""}, d2 = {"Lio/legado/app/constant/AppConst;", PackageDocumentBase.PREFIX_OPF, "()V", "SCRIPT_ENGINE", "Lcom/script/javascript/RhinoScriptEngine;", "getSCRIPT_ENGINE", "()Lcom/script/javascript/RhinoScriptEngine;", "SCRIPT_ENGINE$delegate", "Lkotlin/Lazy;", "TIME_FORMAT", "Ljava/text/SimpleDateFormat;", "getTIME_FORMAT", "()Ljava/text/SimpleDateFormat;", "TIME_FORMAT$delegate", "UA_NAME", PackageDocumentBase.PREFIX_OPF, "dateFormat", "getDateFormat", "dateFormat$delegate", "fileNameFormat", "getFileNameFormat", "fileNameFormat$delegate", "keyboardToolChars", PackageDocumentBase.PREFIX_OPF, "getKeyboardToolChars", "()Ljava/util/List;", "keyboardToolChars$delegate", "timeFormat", "getTimeFormat", "timeFormat$delegate", "userAgent", "getUserAgent", "()Ljava/lang/String;", "userAgent$delegate", "reader-pro"})
public final class AppConst {

    @NotNull
    public static final String UA_NAME = "User-Agent";

    @NotNull
    public static final AppConst INSTANCE = new AppConst();

    /* JADX INFO: renamed from: userAgent$delegate, reason: from kotlin metadata */
    @NotNull
    private static final Lazy userAgent = LazyKt.lazy(new Function0<String>() { // from class: io.legado.app.constant.AppConst$userAgent$2
        @NotNull
        /* JADX INFO: renamed from: invoke, reason: merged with bridge method [inline-methods] */
        public final String m131invoke() {
            return "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/75.0.3770.142 Safari/537.36";
        }
    });

    /* JADX INFO: renamed from: SCRIPT_ENGINE$delegate, reason: from kotlin metadata */
    @NotNull
    private static final Lazy SCRIPT_ENGINE = LazyKt.lazy(new Function0<RhinoScriptEngine>() { // from class: io.legado.app.constant.AppConst$SCRIPT_ENGINE$2
        @NotNull
        /* JADX INFO: renamed from: invoke, reason: merged with bridge method [inline-methods] */
        public final RhinoScriptEngine m119invoke() {
            return new RhinoScriptEngine();
        }
    });

    /* JADX INFO: renamed from: TIME_FORMAT$delegate, reason: from kotlin metadata */
    @NotNull
    private static final Lazy TIME_FORMAT = LazyKt.lazy(new Function0<SimpleDateFormat>() { // from class: io.legado.app.constant.AppConst$TIME_FORMAT$2
        @NotNull
        /* JADX INFO: renamed from: invoke, reason: merged with bridge method [inline-methods] */
        public final SimpleDateFormat m121invoke() {
            return new SimpleDateFormat("HH:mm");
        }
    });

    /* JADX INFO: renamed from: timeFormat$delegate, reason: from kotlin metadata */
    @NotNull
    private static final Lazy timeFormat = LazyKt.lazy(new Function0<SimpleDateFormat>() { // from class: io.legado.app.constant.AppConst$timeFormat$2
        @NotNull
        /* JADX INFO: renamed from: invoke, reason: merged with bridge method [inline-methods] */
        public final SimpleDateFormat m129invoke() {
            return new SimpleDateFormat("HH:mm");
        }
    });

    /* JADX INFO: renamed from: dateFormat$delegate, reason: from kotlin metadata */
    @NotNull
    private static final Lazy dateFormat = LazyKt.lazy(new Function0<SimpleDateFormat>() { // from class: io.legado.app.constant.AppConst$dateFormat$2
        @NotNull
        /* JADX INFO: renamed from: invoke, reason: merged with bridge method [inline-methods] */
        public final SimpleDateFormat m123invoke() {
            return new SimpleDateFormat("yyyy/MM/dd HH:mm");
        }
    });

    /* JADX INFO: renamed from: fileNameFormat$delegate, reason: from kotlin metadata */
    @NotNull
    private static final Lazy fileNameFormat = LazyKt.lazy(new Function0<SimpleDateFormat>() { // from class: io.legado.app.constant.AppConst$fileNameFormat$2
        @NotNull
        /* JADX INFO: renamed from: invoke, reason: merged with bridge method [inline-methods] */
        public final SimpleDateFormat m125invoke() {
            return new SimpleDateFormat("yy-MM-dd-HH-mm-ss");
        }
    });

    /* JADX INFO: renamed from: keyboardToolChars$delegate, reason: from kotlin metadata */
    @NotNull
    private static final Lazy keyboardToolChars = LazyKt.lazy(new Function0<ArrayList<String>>() { // from class: io.legado.app.constant.AppConst$keyboardToolChars$2
        @NotNull
        /* JADX INFO: renamed from: invoke, reason: merged with bridge method [inline-methods] */
        public final ArrayList<String> m127invoke() {
            return CollectionsKt.arrayListOf(new String[]{"@", "&", "|", "%", TableOfContents.DEFAULT_PATH_SEPARATOR, ":", "[", "]", "{", "}", "<", ">", "\\", "$", "#", "!", ".", "href", NCXDocumentV2.NCXAttributes.src, "textNodes", "xpath", "json", "css", "id", NCXDocumentV2.NCXAttributes.clazz, "tag"});
        }
    });

    private AppConst() {
    }

    @NotNull
    public final String getUserAgent() {
        return (String) userAgent.getValue();
    }

    @NotNull
    public final RhinoScriptEngine getSCRIPT_ENGINE() {
        return (RhinoScriptEngine) SCRIPT_ENGINE.getValue();
    }

    @NotNull
    public final SimpleDateFormat getTIME_FORMAT() {
        return (SimpleDateFormat) TIME_FORMAT.getValue();
    }

    @NotNull
    public final SimpleDateFormat getTimeFormat() {
        return (SimpleDateFormat) timeFormat.getValue();
    }

    @NotNull
    public final SimpleDateFormat getDateFormat() {
        return (SimpleDateFormat) dateFormat.getValue();
    }

    @NotNull
    public final SimpleDateFormat getFileNameFormat() {
        return (SimpleDateFormat) fileNameFormat.getValue();
    }

    @NotNull
    public final List<String> getKeyboardToolChars() {
        return (List) keyboardToolChars.getValue();
    }
}
