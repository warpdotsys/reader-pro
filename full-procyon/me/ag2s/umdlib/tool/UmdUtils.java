// 
// Decompiled by Procyon v0.6.0
// 

package me.ag2s.umdlib.tool;

import java.io.BufferedInputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.BufferedOutputStream;
import java.io.FileOutputStream;
import java.io.File;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.util.zip.InflaterInputStream;
import java.io.ByteArrayInputStream;
import java.util.Random;

public class UmdUtils
{
    private static final int EOF = -1;
    private static final int BUFFER_SIZE = 8192;
    private static Random random;
    
    public static byte[] stringToUnicodeBytes(final String s) {
        if (s == null) {
            throw new NullPointerException();
        }
        final int len = s.length();
        final byte[] ret = new byte[len * 2];
        for (int i = 0; i < len; ++i) {
            final int c = s.charAt(i);
            int a = c >> 8;
            int b = c & 0xFF;
            if (a < 0) {
                a += 255;
            }
            if (b < 0) {
                b += 255;
            }
            ret[i * 2] = (byte)b;
            ret[i * 2 + 1] = (byte)a;
        }
        return ret;
    }
    
    public static String unicodeBytesToString(final byte[] bytes) {
        final char[] s = new char[bytes.length / 2];
        final StringBuilder sb = new StringBuilder();
        for (int i = 0; i < s.length; ++i) {
            final int a = bytes[i * 2 + 1];
            final int b = bytes[i * 2];
            int c = (a & 0xFF) << 8 | (b & 0xFF);
            if (c < 0) {
                c += 65535;
            }
            final char[] c2 = Character.toChars(c);
            sb.append(c2);
        }
        return sb.toString();
    }
    
    public static String toHex(final byte[] bArr) {
        final StringBuilder sb = new StringBuilder(bArr.length);
        for (int i = 0; i < bArr.length; ++i) {
            final String sTmp = Integer.toHexString(0xFF & bArr[i]);
            if (sTmp.length() < 2) {
                sb.append(0);
            }
            sb.append(sTmp.toUpperCase());
        }
        return sb.toString();
    }
    
    public static byte[] decompress(final byte[] compress) throws Exception {
        final ByteArrayInputStream bais = new ByteArrayInputStream(compress);
        final InflaterInputStream iis = new InflaterInputStream(bais);
        final ByteArrayOutputStream baos = new ByteArrayOutputStream();
        int c = 0;
        final byte[] buf = new byte[8192];
        while (true) {
            c = iis.read(buf);
            if (c == -1) {
                break;
            }
            baos.write(buf, 0, c);
        }
        baos.flush();
        return baos.toByteArray();
    }
    
    public static void saveFile(final File f, final byte[] content) throws IOException {
        final FileOutputStream fos = new FileOutputStream(f);
        try {
            final BufferedOutputStream bos = new BufferedOutputStream(fos);
            bos.write(content);
            bos.flush();
        }
        finally {
            fos.close();
        }
    }
    
    public static byte[] readFile(final File f) throws IOException {
        final FileInputStream fis = new FileInputStream(f);
        try {
            final ByteArrayOutputStream baos = new ByteArrayOutputStream();
            final BufferedInputStream bis = new BufferedInputStream(fis);
            int ch;
            while ((ch = bis.read()) >= 0) {
                baos.write(ch);
            }
            baos.flush();
            return baos.toByteArray();
        }
        finally {
            fis.close();
        }
    }
    
    public static byte[] genRandomBytes(final int len) {
        if (len <= 0) {
            throw new IllegalArgumentException("Length must > 0: " + len);
        }
        final byte[] ret = new byte[len];
        for (int i = 0; i < ret.length; ++i) {
            ret[i] = (byte)UmdUtils.random.nextInt(256);
        }
        return ret;
    }
    
    static {
        UmdUtils.random = new Random();
    }
}
