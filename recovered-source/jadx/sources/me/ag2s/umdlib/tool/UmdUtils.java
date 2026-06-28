package me.ag2s.umdlib.tool;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Random;
import java.util.zip.InflaterInputStream;

/* JADX INFO: loaded from: reader-pro-classes-3.2.14.jar:me/ag2s/umdlib/tool/UmdUtils.class */
public class UmdUtils {
    private static final int EOF = -1;
    private static final int BUFFER_SIZE = 8192;
    private static Random random = new Random();

    public static byte[] stringToUnicodeBytes(String s) {
        if (s == null) {
            throw new NullPointerException();
        }
        int len = s.length();
        byte[] ret = new byte[len * 2];
        for (int i = 0; i < len; i++) {
            int c = s.charAt(i);
            int a = c >> 8;
            int b = c & 255;
            if (a < 0) {
                a += 255;
            }
            if (b < 0) {
                b += 255;
            }
            ret[i * 2] = (byte) b;
            ret[(i * 2) + 1] = (byte) a;
        }
        return ret;
    }

    public static String unicodeBytesToString(byte[] bytes) {
        char[] s = new char[bytes.length / 2];
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < s.length; i++) {
            int c = ((bytes[(i * 2) + 1] & 255) << 8) | (bytes[i * 2] & 255);
            if (c < 0) {
                c += 65535;
            }
            char[] c1 = Character.toChars(c);
            sb.append(c1);
        }
        return sb.toString();
    }

    public static String toHex(byte[] bArr) {
        StringBuilder sb = new StringBuilder(bArr.length);
        for (byte b : bArr) {
            String sTmp = Integer.toHexString(255 & b);
            if (sTmp.length() < 2) {
                sb.append(0);
            }
            sb.append(sTmp.toUpperCase());
        }
        return sb.toString();
    }

    public static byte[] decompress(byte[] compress) throws Exception {
        ByteArrayInputStream bais = new ByteArrayInputStream(compress);
        InflaterInputStream iis = new InflaterInputStream(bais);
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        byte[] buf = new byte[8192];
        while (true) {
            int c = iis.read(buf);
            if (c != -1) {
                baos.write(buf, 0, c);
            } else {
                baos.flush();
                return baos.toByteArray();
            }
        }
    }

    public static void saveFile(File f, byte[] content) throws IOException {
        FileOutputStream fos = new FileOutputStream(f);
        try {
            BufferedOutputStream bos = new BufferedOutputStream(fos);
            bos.write(content);
            bos.flush();
            fos.close();
        } catch (Throwable th) {
            fos.close();
            throw th;
        }
    }

    public static byte[] readFile(File f) throws IOException {
        FileInputStream fis = new FileInputStream(f);
        try {
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            BufferedInputStream bis = new BufferedInputStream(fis);
            while (true) {
                int ch = bis.read();
                if (ch >= 0) {
                    baos.write(ch);
                } else {
                    baos.flush();
                    byte[] byteArray = baos.toByteArray();
                    fis.close();
                    return byteArray;
                }
            }
        } catch (Throwable th) {
            fis.close();
            throw th;
        }
    }

    public static byte[] genRandomBytes(int len) {
        if (len <= 0) {
            throw new IllegalArgumentException("Length must > 0: " + len);
        }
        byte[] ret = new byte[len];
        for (int i = 0; i < ret.length; i++) {
            ret[i] = (byte) random.nextInt(256);
        }
        return ret;
    }
}
