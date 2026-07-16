// 
// Decompiled by Procyon v0.6.0
// 

package io.legado.app.utils;

import java.security.NoSuchAlgorithmException;
import kotlin.text.Charsets;
import kotlin.jvm.internal.Intrinsics;
import java.security.MessageDigest;
import org.jetbrains.annotations.Nullable;
import org.jetbrains.annotations.NotNull;
import kotlin.Metadata;

@Metadata(mv = { 1, 5, 1 }, k = 1, xi = 48, d1 = { "\u0000\u0014\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\b\u0002\n\u0002\u0010\u000e\n\u0002\b\u0003\b\u00c6\u0002\u0018\u00002\u00020\u0001B\u0007\b\u0002?\u0006\u0002\u0010\u0002J\u0010\u0010\u0003\u001a\u00020\u00042\b\u0010\u0005\u001a\u0004\u0018\u00010\u0004J\u000e\u0010\u0006\u001a\u00020\u00042\u0006\u0010\u0005\u001a\u00020\u0004¡§\u0006\u0007" }, d2 = { "Lio/legado/app/utils/MD5Utils;", "", "()V", "md5Encode", "", "str", "md5Encode16", "reader-pro" })
public final class MD5Utils
{
    @NotNull
    public static final MD5Utils INSTANCE;
    
    private MD5Utils() {
    }
    
    @NotNull
    public final String md5Encode(@Nullable final String str) {
        if (str == null) {
            return "";
        }
        String reStr = "";
        try {
            final MessageDigest instance = MessageDigest.getInstance("MD5");
            Intrinsics.checkNotNullExpressionValue((Object)instance, "getInstance(\"MD5\")");
            final MessageDigest messageDigest;
            final MessageDigest md5 = messageDigest = instance;
            final byte[] bytes2 = str.getBytes(Charsets.UTF_8);
            Intrinsics.checkNotNullExpressionValue((Object)bytes2, "(this as java.lang.String).getBytes(charset)");
            final byte[] digest = messageDigest.digest(bytes2);
            Intrinsics.checkNotNullExpressionValue((Object)digest, "md5.digest(str.toByteArray())");
            final byte[] bytes = digest;
            final StringBuilder stringBuffer = new StringBuilder();
            final byte[] array = bytes;
            int i = 0;
            while (i < array.length) {
                final byte b = array[i];
                ++i;
                final int bt = b & 0xFF;
                if (bt < 16) {
                    stringBuffer.append(0);
                }
                stringBuffer.append(Integer.toHexString(bt));
            }
            final String string = stringBuffer.toString();
            Intrinsics.checkNotNullExpressionValue((Object)string, "stringBuffer.toString()");
            reStr = string;
        }
        catch (final NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return reStr;
    }
    
    @NotNull
    public final String md5Encode16(@NotNull final String str) {
        Intrinsics.checkNotNullParameter((Object)str, "str");
        final String md5Encode;
        String reStr = md5Encode = this.md5Encode(str);
        final int beginIndex = 8;
        final int endIndex = 24;
        final String s = md5Encode;
        if (s == null) {
            throw new NullPointerException("null cannot be cast to non-null type java.lang.String");
        }
        final String substring = s.substring(beginIndex, endIndex);
        Intrinsics.checkNotNullExpressionValue((Object)substring, "(this as java.lang.Strin\u2026ing(startIndex, endIndex)");
        reStr = substring;
        return reStr;
    }
    
    static {
        INSTANCE = new MD5Utils();
    }
}
