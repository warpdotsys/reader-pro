package com.htmake.reader;

import java.util.Arrays;
import kotlin.Metadata;
import kotlin.Unit;
import kotlin.jvm.functions.Function0;
import kotlin.jvm.internal.Intrinsics;
import me.ag2s.epublib.epub.PackageDocumentBase;
import mu.KLogger;
import mu.KotlinLogging;
import org.jetbrains.annotations.NotNull;
import org.springframework.boot.SpringApplication;

/* JADX INFO: compiled from: ReaderApplication.kt */
/* JADX INFO: loaded from: app-classes.jar:com/htmake/reader/ReaderApplicationKt.class */
@Metadata(mv = {1, 5, 1}, k = 2, xi = 48, d1 = {"\u0000\u001a\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u0002\n\u0000\n\u0002\u0010\u0011\n\u0002\u0010\u000e\n\u0002\b\u0002\u001a\u0019\u0010\u0002\u001a\u00020\u00032\f\u0010\u0004\u001a\b\u0012\u0004\u0012\u00020\u00060\u0005¢\u0006\u0002\u0010\u0007\"\u000e\u0010\u0000\u001a\u00020\u0001X\u0082\u0004¢\u0006\u0002\n\u0000¨\u0006\b"}, d2 = {"logger", "Lmu/KLogger;", "main", PackageDocumentBase.PREFIX_OPF, "args", PackageDocumentBase.PREFIX_OPF, PackageDocumentBase.PREFIX_OPF, "([Ljava/lang/String;)V", "reader-pro"})
public final class ReaderApplicationKt {

    @NotNull
    private static final KLogger logger = KotlinLogging.INSTANCE.logger(new Function0<Unit>() { // from class: com.htmake.reader.ReaderApplicationKt$logger$1
        public final void invoke() {
        }

        /* JADX INFO: renamed from: invoke, reason: collision with other method in class */
        public /* bridge */ /* synthetic */ Object m6invoke() {
            invoke();
            return Unit.INSTANCE;
        }
    });

    public static final void main(@NotNull String[] args) {
        Intrinsics.checkNotNullParameter(args, "args");
        logger.info("Starting application with args: {}", args);
        SpringApplication.run(ReaderApplication.class, (String[]) Arrays.copyOf(args, args.length));
    }
}
