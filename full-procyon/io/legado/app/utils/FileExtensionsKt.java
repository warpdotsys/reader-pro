// 
// Decompiled by Procyon v0.6.0
// 

package io.legado.app.utils;

import java.util.Arrays;
import kotlin.jvm.internal.Intrinsics;
import org.jetbrains.annotations.NotNull;
import java.io.File;
import kotlin.Metadata;

@Metadata(mv = { 1, 5, 1 }, k = 2, xi = 48, d1 = { "\u0000\u0018\n\u0000\n\u0002\u0010\u000b\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u0011\n\u0002\u0010\u000e\n\u0002\b\u0004\u001a#\u0010\u0000\u001a\u00020\u0001*\u00020\u00022\u0012\u0010\u0003\u001a\n\u0012\u0006\b\u0001\u0012\u00020\u00050\u0004\"\u00020\u0005?\u0006\u0002\u0010\u0006\u001a#\u0010\u0007\u001a\u00020\u0002*\u00020\u00022\u0012\u0010\u0003\u001a\n\u0012\u0006\b\u0001\u0012\u00020\u00050\u0004\"\u00020\u0005?\u0006\u0002\u0010\b¡§\u0006\t" }, d2 = { "exists", "", "Ljava/io/File;", "subDirFiles", "", "", "(Ljava/io/File;[Ljava/lang/String;)Z", "getFile", "(Ljava/io/File;[Ljava/lang/String;)Ljava/io/File;", "reader-pro" })
public final class FileExtensionsKt
{
    @NotNull
    public static final File getFile(@NotNull final File $this$getFile, @NotNull final String... subDirFiles) {
        Intrinsics.checkNotNullParameter((Object)$this$getFile, "<this>");
        Intrinsics.checkNotNullParameter((Object)subDirFiles, "subDirFiles");
        final String path = FileUtils.INSTANCE.getPath($this$getFile, (String[])Arrays.copyOf(subDirFiles, subDirFiles.length));
        return new File(path);
    }
    
    public static final boolean exists(@NotNull final File $this$exists, @NotNull final String... subDirFiles) {
        Intrinsics.checkNotNullParameter((Object)$this$exists, "<this>");
        Intrinsics.checkNotNullParameter((Object)subDirFiles, "subDirFiles");
        return getFile($this$exists, (String[])Arrays.copyOf(subDirFiles, subDirFiles.length)).exists();
    }
}
