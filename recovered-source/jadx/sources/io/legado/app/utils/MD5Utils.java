package io.legado.app.utils;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import kotlin.Metadata;
import kotlin.jvm.internal.Intrinsics;
import kotlin.text.Charsets;
import me.ag2s.epublib.epub.PackageDocumentBase;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/* JADX INFO: compiled from: MD5Utils.kt */
/* JADX INFO: loaded from: app-classes.jar:io/legado/app/utils/MD5Utils.class */
@Metadata(mv = {1, 5, 1}, k = 1, xi = 48, d1 = {"\u0000\u0014\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\b\u0002\n\u0002\u0010\u000e\n\u0002\b\u0003\bÆ\u0002\u0018\u00002\u00020\u0001B\u0007\b\u0002¢\u0006\u0002\u0010\u0002J\u0010\u0010\u0003\u001a\u00020\u00042\b\u0010\u0005\u001a\u0004\u0018\u00010\u0004J\u000e\u0010\u0006\u001a\u00020\u00042\u0006\u0010\u0005\u001a\u00020\u0004¨\u0006\u0007"}, d2 = {"Lio/legado/app/utils/MD5Utils;", PackageDocumentBase.PREFIX_OPF, "()V", "md5Encode", PackageDocumentBase.PREFIX_OPF, "str", "md5Encode16", "reader-pro"})
public final class MD5Utils {

    @NotNull
    public static final MD5Utils INSTANCE = new MD5Utils();

    private MD5Utils() {
    }

    @NotNull
    public final String md5Encode(@Nullable String str) {
        if (str == null) {
            return PackageDocumentBase.PREFIX_OPF;
        }
        String reStr = PackageDocumentBase.PREFIX_OPF;
        try {
            MessageDigest md5 = MessageDigest.getInstance("MD5");
            Intrinsics.checkNotNullExpressionValue(md5, "getInstance(\"MD5\")");
            byte[] bytes = str.getBytes(Charsets.UTF_8);
            Intrinsics.checkNotNullExpressionValue(bytes, "(this as java.lang.String).getBytes(charset)");
            byte[] bytes2 = md5.digest(bytes);
            Intrinsics.checkNotNullExpressionValue(bytes2, "md5.digest(str.toByteArray())");
            StringBuilder stringBuffer = new StringBuilder();
            int i = 0;
            int length = bytes2.length;
            while (i < length) {
                byte b = bytes2[i];
                i++;
                int bt = b & 255;
                if (bt < 16) {
                    stringBuffer.append(0);
                }
                stringBuffer.append(Integer.toHexString(bt));
            }
            String string = stringBuffer.toString();
            Intrinsics.checkNotNullExpressionValue(string, "stringBuffer.toString()");
            reStr = string;
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return reStr;
    }

    @NotNull
    public final String md5Encode16(@NotNull String str) {
        Intrinsics.checkNotNullParameter(str, "str");
        String reStr = md5Encode(str);
        if (reStr == null) {
            throw new NullPointerException("null cannot be cast to non-null type java.lang.String");
        }
        String reStr2 = reStr.substring(8, 24);
        Intrinsics.checkNotNullExpressionValue(reStr2, "(this as java.lang.Strin…ing(startIndex, endIndex)");
        return reStr2;
    }
}
