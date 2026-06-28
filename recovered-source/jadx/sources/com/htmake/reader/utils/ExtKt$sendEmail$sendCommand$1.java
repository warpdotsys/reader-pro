package com.htmake.reader.utils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.OutputStreamWriter;
import kotlin.Metadata;
import kotlin.Pair;
import kotlin.jvm.functions.Function3;
import kotlin.jvm.internal.Intrinsics;
import kotlin.jvm.internal.Lambda;
import kotlin.text.StringsKt;
import me.ag2s.epublib.epub.PackageDocumentBase;
import mu.KLogger;
import org.jetbrains.annotations.NotNull;

/* JADX INFO: compiled from: Ext.kt */
/* JADX INFO: loaded from: reader-pro-classes-3.2.14.jar:com/htmake/reader/utils/ExtKt$sendEmail$sendCommand$1.class */
@Metadata(mv = {1, 5, 1}, k = 3, xi = 48, d1 = {"\u0000 \n\u0000\n\u0002\u0010\u000b\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\u0010\u000e\n\u0002\u0010\b\u0010\u0000\u001a\u00020\u00012\u0006\u0010\u0002\u001a\u00020\u00032\u0006\u0010\u0004\u001a\u00020\u00052\u0012\u0010\u0006\u001a\u000e\u0012\u0004\u0012\u00020\b\u0012\u0004\u0012\u00020\t0\u0007H\n"}, d2 = {"<no name provided>", PackageDocumentBase.PREFIX_OPF, "writer", "Ljava/io/OutputStreamWriter;", "reader", "Ljava/io/BufferedReader;", "command", "Lkotlin/Pair;", PackageDocumentBase.PREFIX_OPF, PackageDocumentBase.PREFIX_OPF})
final class ExtKt$sendEmail$sendCommand$1 extends Lambda implements Function3<OutputStreamWriter, BufferedReader, Pair<? extends String, ? extends Integer>, Boolean> {
    public static final ExtKt$sendEmail$sendCommand$1 INSTANCE = new ExtKt$sendEmail$sendCommand$1();

    ExtKt$sendEmail$sendCommand$1() {
        super(3);
    }

    public /* bridge */ /* synthetic */ Object invoke(Object p1, Object p2, Object p3) {
        return Boolean.valueOf(invoke((OutputStreamWriter) p1, (BufferedReader) p2, (Pair<String, Integer>) p3));
    }

    public final boolean invoke(@NotNull OutputStreamWriter writer, @NotNull BufferedReader reader, @NotNull Pair<String, Integer> command) throws IOException {
        Intrinsics.checkNotNullParameter(writer, "writer");
        Intrinsics.checkNotNullParameter(reader, "reader");
        Intrinsics.checkNotNullParameter(command, "command");
        String cmd = (String) command.getFirst();
        int code = ((Number) command.getSecond()).intValue();
        KLogger logger = ExtKt.getLogger();
        if (cmd == null) {
            throw new NullPointerException("null cannot be cast to non-null type kotlin.CharSequence");
        }
        logger.debug("Send command {}, expect code {}", StringsKt.trim(cmd).toString(), Integer.valueOf(code));
        writer.write(String.valueOf(cmd));
        writer.flush();
        String response = reader.readLine();
        ExtKt.getLogger().debug("Response {}", response);
        String str = response;
        if (!(str == null || str.length() == 0)) {
            Intrinsics.checkNotNullExpressionValue(response, "response");
            if (!StringsKt.startsWith$default(response, Intrinsics.stringPlus(PackageDocumentBase.PREFIX_OPF, Integer.valueOf(code)), false, 2, (Object) null)) {
                ExtKt.getLogger().error("Error response from SMTP server.");
                return false;
            }
            return true;
        }
        ExtKt.getLogger().error("SMTP server no response.");
        return false;
    }
}
