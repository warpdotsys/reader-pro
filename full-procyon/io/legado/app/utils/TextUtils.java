// 
// Decompiled by Procyon v0.6.0
// 

package io.legado.app.utils;

import java.util.Iterator;

public class TextUtils
{
    public static boolean isEmpty(final CharSequence str) {
        return str == null || str.length() == 0;
    }
    
    public static String join(final CharSequence delimiter, final Object[] tokens) {
        final int length = tokens.length;
        if (length == 0) {
            return "";
        }
        final StringBuilder sb = new StringBuilder();
        sb.append(tokens[0]);
        for (int i = 1; i < length; ++i) {
            sb.append(delimiter);
            sb.append(tokens[i]);
        }
        return sb.toString();
    }
    
    public static String join(final CharSequence delimiter, final Iterable tokens) {
        final Iterator<?> it = tokens.iterator();
        if (!it.hasNext()) {
            return "";
        }
        final StringBuilder sb = new StringBuilder();
        sb.append(it.next());
        while (it.hasNext()) {
            sb.append(delimiter);
            sb.append(it.next());
        }
        return sb.toString();
    }
}
