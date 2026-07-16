// 
// Decompiled by Procyon v0.6.0
// 

package io.legado.app.utils;

import java.security.KeyPairGenerator;
import java.security.KeyPair;
import java.io.ByteArrayOutputStream;
import java.security.PublicKey;
import java.security.PrivateKey;
import javax.crypto.spec.IvParameterSpec;
import java.security.spec.AlgorithmParameterSpec;
import java.security.Key;
import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;
import org.jetbrains.annotations.Nullable;
import kotlin.jvm.JvmOverloads;
import kotlin.text.Charsets;
import kotlin.text.CharsKt;
import kotlin.jvm.internal.Intrinsics;
import org.jetbrains.annotations.NotNull;
import kotlin.Metadata;

@Metadata(mv = { 1, 5, 1 }, k = 1, xi = 48, d1 = { "\u0000B\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\b\u0002\n\u0002\u0010\u000e\n\u0002\b\u0002\n\u0002\u0010\b\n\u0002\b\u0002\n\u0002\u0010\u0012\n\u0002\b\n\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0012\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0010\u000b\n\u0000\b\u00c6\u0002\u0018\u00002\u00020\u0001B\u0007\b\u0002?\u0006\u0002\u0010\u0002J\u001a\u0010\u0003\u001a\u00020\u00042\u0006\u0010\u0005\u001a\u00020\u00042\b\b\u0002\u0010\u0006\u001a\u00020\u0007H\u0007J\u001c\u0010\b\u001a\u0004\u0018\u00010\u00042\u0006\u0010\u0005\u001a\u00020\u00042\b\b\u0002\u0010\u0006\u001a\u00020\u0007H\u0007J2\u0010\t\u001a\u0004\u0018\u00010\n2\b\u0010\u000b\u001a\u0004\u0018\u00010\n2\b\u0010\f\u001a\u0004\u0018\u00010\n2\b\b\u0002\u0010\r\u001a\u00020\u00042\n\b\u0002\u0010\u000e\u001a\u0004\u0018\u00010\nJ2\u0010\u000f\u001a\u0004\u0018\u00010\n2\b\u0010\u000b\u001a\u0004\u0018\u00010\n2\b\u0010\f\u001a\u0004\u0018\u00010\n2\b\b\u0002\u0010\r\u001a\u00020\u00042\n\b\u0002\u0010\u000e\u001a\u0004\u0018\u00010\nJ2\u0010\u0010\u001a\u0004\u0018\u00010\n2\b\u0010\u000b\u001a\u0004\u0018\u00010\n2\b\u0010\f\u001a\u0004\u0018\u00010\n2\b\b\u0002\u0010\r\u001a\u00020\u00042\n\b\u0002\u0010\u000e\u001a\u0004\u0018\u00010\nJ2\u0010\u0011\u001a\u0004\u0018\u00010\n2\b\u0010\u000b\u001a\u0004\u0018\u00010\n2\b\u0010\f\u001a\u0004\u0018\u00010\n2\b\b\u0002\u0010\r\u001a\u00020\u00042\n\b\u0002\u0010\u000e\u001a\u0004\u0018\u00010\nJ\u0016\u0010\u0012\u001a\u00020\u00042\u0006\u0010\u0013\u001a\u00020\u00042\u0006\u0010\u0014\u001a\u00020\u0015J\u0016\u0010\u0016\u001a\u00020\u00042\u0006\u0010\u0013\u001a\u00020\u00042\u0006\u0010\u0017\u001a\u00020\u0018J2\u0010\u0019\u001a\u0004\u0018\u00010\n2\b\u0010\u000b\u001a\u0004\u0018\u00010\n2\b\u0010\f\u001a\u0004\u0018\u00010\n2\b\b\u0002\u0010\r\u001a\u00020\u00042\n\b\u0002\u0010\u000e\u001a\u0004\u0018\u00010\nJ2\u0010\u001a\u001a\u0004\u0018\u00010\n2\b\u0010\u000b\u001a\u0004\u0018\u00010\n2\b\u0010\f\u001a\u0004\u0018\u00010\n2\b\b\u0002\u0010\r\u001a\u00020\u00042\n\b\u0002\u0010\u000e\u001a\u0004\u0018\u00010\nJ\"\u0010\u001b\u001a\u0004\u0018\u00010\u00042\u0006\u0010\u0013\u001a\u00020\u00042\u0006\u0010\u0014\u001a\u00020\u00152\b\b\u0002\u0010\u001c\u001a\u00020\u0007J\"\u0010\u001d\u001a\u0004\u0018\u00010\u00042\u0006\u0010\u0013\u001a\u00020\u00042\u0006\u0010\u0017\u001a\u00020\u00182\b\b\u0002\u0010\u001c\u001a\u00020\u0007J4\u0010\u001e\u001a\u0004\u0018\u00010\n2\b\u0010\u000b\u001a\u0004\u0018\u00010\n2\b\u0010\f\u001a\u0004\u0018\u00010\n2\n\b\u0002\u0010\r\u001a\u0004\u0018\u00010\u00042\n\b\u0002\u0010\u000e\u001a\u0004\u0018\u00010\nJ4\u0010\u001f\u001a\u0004\u0018\u00010\n2\b\u0010\u000b\u001a\u0004\u0018\u00010\n2\b\u0010\f\u001a\u0004\u0018\u00010\n2\n\b\u0002\u0010\r\u001a\u0004\u0018\u00010\u00042\n\b\u0002\u0010\u000e\u001a\u0004\u0018\u00010\nJ\u0016\u0010 \u001a\u00020\u00042\u0006\u0010\u0013\u001a\u00020\u00042\u0006\u0010\u0014\u001a\u00020\u0015J\u0016\u0010!\u001a\u00020\u00042\u0006\u0010\u0013\u001a\u00020\u00042\u0006\u0010\u0017\u001a\u00020\u0018J4\u0010\"\u001a\u0004\u0018\u00010\n2\b\u0010\u000b\u001a\u0004\u0018\u00010\n2\b\u0010\f\u001a\u0004\u0018\u00010\n2\n\b\u0002\u0010\r\u001a\u0004\u0018\u00010\u00042\n\b\u0002\u0010\u000e\u001a\u0004\u0018\u00010\nJ4\u0010#\u001a\u0004\u0018\u00010\n2\b\u0010\u000b\u001a\u0004\u0018\u00010\n2\b\u0010\f\u001a\u0004\u0018\u00010\n2\n\b\u0002\u0010\r\u001a\u0004\u0018\u00010\u00042\n\b\u0002\u0010\u000e\u001a\u0004\u0018\u00010\nJ4\u0010$\u001a\u0004\u0018\u00010\n2\b\u0010\u000b\u001a\u0004\u0018\u00010\n2\b\u0010\f\u001a\u0004\u0018\u00010\n2\n\b\u0002\u0010\r\u001a\u0004\u0018\u00010\u00042\n\b\u0002\u0010\u000e\u001a\u0004\u0018\u00010\nJ4\u0010%\u001a\u0004\u0018\u00010\n2\b\u0010\u000b\u001a\u0004\u0018\u00010\n2\b\u0010\f\u001a\u0004\u0018\u00010\n2\n\b\u0002\u0010\r\u001a\u0004\u0018\u00010\u00042\n\b\u0002\u0010\u000e\u001a\u0004\u0018\u00010\nJ \u0010&\u001a\u00020\u00042\u0006\u0010\u0013\u001a\u00020\u00042\u0006\u0010\u0014\u001a\u00020\u00152\b\b\u0002\u0010\u001c\u001a\u00020\u0007J \u0010'\u001a\u00020\u00042\u0006\u0010\u0013\u001a\u00020\u00042\u0006\u0010\u0017\u001a\u00020\u00182\b\b\u0002\u0010\u001c\u001a\u00020\u0007J\u000e\u0010(\u001a\u00020\u00042\u0006\u0010)\u001a\u00020\u0004J\u0006\u0010*\u001a\u00020+J@\u0010,\u001a\u0004\u0018\u00010\n2\b\u0010\u000b\u001a\u0004\u0018\u00010\n2\b\u0010\f\u001a\u0004\u0018\u00010\n2\u0006\u0010-\u001a\u00020\u00042\u0006\u0010\r\u001a\u00020\u00042\b\u0010\u000e\u001a\u0004\u0018\u00010\n2\u0006\u0010.\u001a\u00020/H\u0002¡§\u00060" }, d2 = { "Lio/legado/app/utils/EncoderUtils;", "", "()V", "base64Decode", "", "str", "flags", "", "base64Encode", "decryptAES", "", "data", "key", "transformation", "iv", "decryptBase64AES", "decryptBase64DES", "decryptBase64DESede", "decryptByPrivateKey", "input", "privateKey", "Ljava/security/PrivateKey;", "decryptByPublicKey", "publicKey", "Ljava/security/PublicKey;", "decryptDES", "decryptDESede", "decryptSegmentByPrivateKey", "keySize", "decryptSegmentByPublicKey", "encryptAES", "encryptAES2Base64", "encryptByPrivateKey", "encryptByPublicKey", "encryptDES", "encryptDES2Base64", "encryptDESede", "encryptDESede2Base64", "encryptSegmentByPrivateKey", "encryptSegmentByPublicKey", "escape", "src", "generateKeys", "Ljava/security/KeyPair;", "symmetricTemplate", "algorithm", "isEncrypt", "", "reader-pro" })
public final class EncoderUtils
{
    @NotNull
    public static final EncoderUtils INSTANCE;
    
    private EncoderUtils() {
    }
    
    @NotNull
    public final String escape(@NotNull final String src) {
        Intrinsics.checkNotNullParameter((Object)src, "src");
        final StringBuilder tmp = new StringBuilder();
        int i = 0;
        while (i < src.length()) {
            final char char1 = src.charAt(i);
            ++i;
            final int charCode = char1;
            if ((48 <= charCode && charCode <= 57) || (65 <= charCode && charCode <= 90) || (97 <= charCode && charCode <= 122)) {
                tmp.append(char1);
            }
            else {
                final String prefix = (charCode < 16) ? "%0" : ((charCode < 256) ? "%" : "%u");
                final StringBuilder append = tmp.append(prefix);
                final String string = Integer.toString(charCode, CharsKt.checkRadix(16));
                Intrinsics.checkNotNullExpressionValue((Object)string, "java.lang.Integer.toStri\u2026(this, checkRadix(radix))");
                append.append(string);
            }
        }
        final String string2 = tmp.toString();
        Intrinsics.checkNotNullExpressionValue((Object)string2, "tmp.toString()");
        return string2;
    }
    
    @JvmOverloads
    @NotNull
    public final String base64Decode(@NotNull final String str, final int flags) {
        Intrinsics.checkNotNullParameter((Object)str, "str");
        final byte[] bytes = Base64.decode(str, flags);
        Intrinsics.checkNotNullExpressionValue((Object)bytes, "bytes");
        return new String(bytes, Charsets.UTF_8);
    }
    
    public static /* synthetic */ String base64Decode$default(final EncoderUtils encoderUtils, final String str, int flags, final int n, final Object o) {
        if ((n & 0x2) != 0x0) {
            flags = 0;
        }
        return encoderUtils.base64Decode(str, flags);
    }
    
    @JvmOverloads
    @Nullable
    public final String base64Encode(@NotNull final String str, final int flags) {
        Intrinsics.checkNotNullParameter((Object)str, "str");
        final byte[] bytes = str.getBytes(Charsets.UTF_8);
        Intrinsics.checkNotNullExpressionValue((Object)bytes, "(this as java.lang.String).getBytes(charset)");
        return Base64.encodeToString(bytes, flags);
    }
    
    public static /* synthetic */ String base64Encode$default(final EncoderUtils encoderUtils, final String str, int flags, final int n, final Object o) {
        if ((n & 0x2) != 0x0) {
            flags = 2;
        }
        return encoderUtils.base64Encode(str, flags);
    }
    
    @Nullable
    public final byte[] encryptAES2Base64(@Nullable final byte[] data, @Nullable final byte[] key, @Nullable final String transformation, @Nullable final byte[] iv) throws Exception {
        return Base64.encode(this.encryptAES(data, key, transformation, iv), 2);
    }
    
    @Nullable
    public final byte[] encryptAES(@Nullable final byte[] data, @Nullable final byte[] key, @Nullable final String transformation, @Nullable final byte[] iv) throws Exception {
        final String algorithm = "AES";
        Intrinsics.checkNotNull((Object)transformation);
        return this.symmetricTemplate(data, key, algorithm, transformation, iv, true);
    }
    
    @Nullable
    public final byte[] decryptBase64AES(@Nullable final byte[] data, @Nullable final byte[] key, @NotNull final String transformation, @Nullable final byte[] iv) throws Exception {
        Intrinsics.checkNotNullParameter((Object)transformation, "transformation");
        return this.decryptAES(Base64.decode(data, 2), key, transformation, iv);
    }
    
    @Nullable
    public final byte[] decryptAES(@Nullable final byte[] data, @Nullable final byte[] key, @NotNull final String transformation, @Nullable final byte[] iv) throws Exception {
        Intrinsics.checkNotNullParameter((Object)transformation, "transformation");
        return this.symmetricTemplate(data, key, "AES", transformation, iv, false);
    }
    
    private final byte[] symmetricTemplate(final byte[] data, final byte[] key, final String algorithm, final String transformation, final byte[] iv, final boolean isEncrypt) throws Exception {
        byte[] doFinal;
        if (data == null || (data.length == 0 || key == null) || key.length == 0) {
            doFinal = null;
        }
        else {
            final SecretKeySpec keySpec = new SecretKeySpec(key, algorithm);
            final Cipher cipher = Cipher.getInstance(transformation);
            final int mode = isEncrypt ? 1 : 2;
            if (iv == null || iv.length == 0) {
                cipher.init(mode, keySpec);
            }
            else {
                final AlgorithmParameterSpec params = new IvParameterSpec(iv);
                cipher.init(mode, keySpec, params);
            }
            doFinal = cipher.doFinal(data);
        }
        return doFinal;
    }
    
    @Nullable
    public final byte[] encryptDES2Base64(@Nullable final byte[] data, @Nullable final byte[] key, @Nullable final String transformation, @Nullable final byte[] iv) throws Exception {
        return Base64.encode(this.encryptDES(data, key, transformation, iv), 2);
    }
    
    @Nullable
    public final byte[] encryptDES(@Nullable final byte[] data, @Nullable final byte[] key, @Nullable final String transformation, @Nullable final byte[] iv) throws Exception {
        final String algorithm = "DES";
        Intrinsics.checkNotNull((Object)transformation);
        return this.symmetricTemplate(data, key, algorithm, transformation, iv, true);
    }
    
    @Nullable
    public final byte[] decryptBase64DES(@Nullable final byte[] data, @Nullable final byte[] key, @NotNull final String transformation, @Nullable final byte[] iv) throws Exception {
        Intrinsics.checkNotNullParameter((Object)transformation, "transformation");
        return this.decryptDES(Base64.decode(data, 2), key, transformation, iv);
    }
    
    @Nullable
    public final byte[] decryptDES(@Nullable final byte[] data, @Nullable final byte[] key, @NotNull final String transformation, @Nullable final byte[] iv) throws Exception {
        Intrinsics.checkNotNullParameter((Object)transformation, "transformation");
        return this.symmetricTemplate(data, key, "DES", transformation, iv, false);
    }
    
    @Nullable
    public final byte[] encryptDESede2Base64(@Nullable final byte[] data, @Nullable final byte[] key, @Nullable final String transformation, @Nullable final byte[] iv) throws Exception {
        return Base64.encode(this.encryptDESede(data, key, transformation, iv), 2);
    }
    
    @Nullable
    public final byte[] encryptDESede(@Nullable final byte[] data, @Nullable final byte[] key, @Nullable final String transformation, @Nullable final byte[] iv) throws Exception {
        final String algorithm = "DESede";
        Intrinsics.checkNotNull((Object)transformation);
        return this.symmetricTemplate(data, key, algorithm, transformation, iv, true);
    }
    
    @Nullable
    public final byte[] decryptBase64DESede(@Nullable final byte[] data, @Nullable final byte[] key, @NotNull final String transformation, @Nullable final byte[] iv) throws Exception {
        Intrinsics.checkNotNullParameter((Object)transformation, "transformation");
        return this.decryptDESede(Base64.decode(data, 2), key, transformation, iv);
    }
    
    @Nullable
    public final byte[] decryptDESede(@Nullable final byte[] data, @Nullable final byte[] key, @NotNull final String transformation, @Nullable final byte[] iv) throws Exception {
        Intrinsics.checkNotNullParameter((Object)transformation, "transformation");
        return this.symmetricTemplate(data, key, "DESede", transformation, iv, false);
    }
    
    @NotNull
    public final String encryptByPrivateKey(@NotNull final String input, @NotNull final PrivateKey privateKey) {
        Intrinsics.checkNotNullParameter((Object)input, "input");
        Intrinsics.checkNotNullParameter((Object)privateKey, "privateKey");
        final Cipher cipher = Cipher.getInstance("RSA");
        cipher.init(1, privateKey);
        final Cipher cipher2 = cipher;
        final byte[] bytes = input.getBytes(Charsets.UTF_8);
        Intrinsics.checkNotNullExpressionValue((Object)bytes, "(this as java.lang.String).getBytes(charset)");
        final byte[] encrypt = cipher2.doFinal(bytes);
        final String encodeToString = Base64.encodeToString(encrypt, 2);
        Intrinsics.checkNotNullExpressionValue((Object)encodeToString, "encodeToString(encrypt, Base64.NO_WRAP)");
        return encodeToString;
    }
    
    @NotNull
    public final String decryptByPublicKey(@NotNull final String input, @NotNull final PublicKey publicKey) {
        Intrinsics.checkNotNullParameter((Object)input, "input");
        Intrinsics.checkNotNullParameter((Object)publicKey, "publicKey");
        final byte[] decode = Base64.decode(input, 2);
        final Cipher cipher = Cipher.getInstance("RSA");
        cipher.init(2, publicKey);
        final byte[] encrypt = cipher.doFinal(decode);
        Intrinsics.checkNotNullExpressionValue((Object)encrypt, "encrypt");
        return new String(encrypt, Charsets.UTF_8);
    }
    
    @NotNull
    public final String encryptByPublicKey(@NotNull final String input, @NotNull final PublicKey publicKey) {
        Intrinsics.checkNotNullParameter((Object)input, "input");
        Intrinsics.checkNotNullParameter((Object)publicKey, "publicKey");
        final Cipher cipher = Cipher.getInstance("RSA");
        cipher.init(1, publicKey);
        final Cipher cipher2 = cipher;
        final byte[] bytes = input.getBytes(Charsets.UTF_8);
        Intrinsics.checkNotNullExpressionValue((Object)bytes, "(this as java.lang.String).getBytes(charset)");
        final byte[] encrypt = cipher2.doFinal(bytes);
        final String encodeToString = Base64.encodeToString(encrypt, 2);
        Intrinsics.checkNotNullExpressionValue((Object)encodeToString, "encodeToString(encrypt, Base64.NO_WRAP)");
        return encodeToString;
    }
    
    @NotNull
    public final String decryptByPrivateKey(@NotNull final String input, @NotNull final PrivateKey privateKey) {
        Intrinsics.checkNotNullParameter((Object)input, "input");
        Intrinsics.checkNotNullParameter((Object)privateKey, "privateKey");
        final byte[] decode = Base64.decode(input, 2);
        final Cipher cipher = Cipher.getInstance("RSA");
        cipher.init(2, privateKey);
        final byte[] encrypt = cipher.doFinal(decode);
        Intrinsics.checkNotNullExpressionValue((Object)encrypt, "encrypt");
        return new String(encrypt, Charsets.UTF_8);
    }
    
    @NotNull
    public final String encryptSegmentByPrivateKey(@NotNull final String input, @NotNull final PrivateKey privateKey, final int keySize) {
        Intrinsics.checkNotNullParameter((Object)input, "input");
        Intrinsics.checkNotNullParameter((Object)privateKey, "privateKey");
        final byte[] bytes = input.getBytes(Charsets.UTF_8);
        Intrinsics.checkNotNullExpressionValue((Object)bytes, "(this as java.lang.String).getBytes(charset)");
        final byte[] byteArray = bytes;
        byte[] temp = null;
        int offset = 0;
        final int MAX_ENCRYPT_BLOCK = keySize / 8 - 11;
        final ByteArrayOutputStream bos = new ByteArrayOutputStream();
        final Cipher cipher = Cipher.getInstance("RSA");
        cipher.init(1, privateKey);
        while (byteArray.length - offset > 0) {
            if (byteArray.length - offset >= MAX_ENCRYPT_BLOCK) {
                final byte[] doFinal = cipher.doFinal(byteArray, offset, MAX_ENCRYPT_BLOCK);
                Intrinsics.checkNotNullExpressionValue((Object)doFinal, "cipher.doFinal(byteArray, offset, MAX_ENCRYPT_BLOCK)");
                temp = doFinal;
                offset += MAX_ENCRYPT_BLOCK;
            }
            else {
                final byte[] doFinal2 = cipher.doFinal(byteArray, offset, byteArray.length - offset);
                Intrinsics.checkNotNullExpressionValue((Object)doFinal2, "cipher.doFinal(byteArray, offset, byteArray.size - offset)");
                temp = doFinal2;
                offset = byteArray.length;
            }
            bos.write(temp);
        }
        bos.close();
        final String encodeToString = Base64.encodeToString(bos.toByteArray(), 2);
        Intrinsics.checkNotNullExpressionValue((Object)encodeToString, "encodeToString(bos.toByteArray(), Base64.NO_WRAP)");
        return encodeToString;
    }
    
    @Nullable
    public final String decryptSegmentByPublicKey(@NotNull final String input, @NotNull final PublicKey publicKey, final int keySize) {
        Intrinsics.checkNotNullParameter((Object)input, "input");
        Intrinsics.checkNotNullParameter((Object)publicKey, "publicKey");
        final byte[] byteArray = Base64.decode(input, 2);
        byte[] temp = null;
        int offset = 0;
        final int MAX_DECRYPT_BLOCK = keySize / 8;
        final ByteArrayOutputStream bos = new ByteArrayOutputStream();
        final Cipher cipher = Cipher.getInstance("RSA");
        cipher.init(2, publicKey);
        while (byteArray.length - offset > 0) {
            if (byteArray.length - offset >= MAX_DECRYPT_BLOCK) {
                final byte[] doFinal = cipher.doFinal(byteArray, offset, MAX_DECRYPT_BLOCK);
                Intrinsics.checkNotNullExpressionValue((Object)doFinal, "cipher.doFinal(byteArray, offset, MAX_DECRYPT_BLOCK)");
                temp = doFinal;
                offset += MAX_DECRYPT_BLOCK;
            }
            else {
                final byte[] doFinal2 = cipher.doFinal(byteArray, offset, byteArray.length - offset);
                Intrinsics.checkNotNullExpressionValue((Object)doFinal2, "cipher.doFinal(byteArray, offset, byteArray.size - offset)");
                temp = doFinal2;
                offset = byteArray.length;
            }
            bos.write(temp);
        }
        bos.close();
        final byte[] byteArray2 = bos.toByteArray();
        Intrinsics.checkNotNullExpressionValue((Object)byteArray2, "bos.toByteArray()");
        return new String(byteArray2, Charsets.UTF_8);
    }
    
    @NotNull
    public final String encryptSegmentByPublicKey(@NotNull final String input, @NotNull final PublicKey publicKey, final int keySize) {
        Intrinsics.checkNotNullParameter((Object)input, "input");
        Intrinsics.checkNotNullParameter((Object)publicKey, "publicKey");
        final byte[] bytes = input.getBytes(Charsets.UTF_8);
        Intrinsics.checkNotNullExpressionValue((Object)bytes, "(this as java.lang.String).getBytes(charset)");
        final byte[] byteArray = bytes;
        byte[] temp = null;
        int offset = 0;
        final int MAX_ENCRYPT_BLOCK = keySize / 8 - 11;
        final ByteArrayOutputStream bos = new ByteArrayOutputStream();
        final Cipher cipher = Cipher.getInstance("RSA");
        cipher.init(1, publicKey);
        while (byteArray.length - offset > 0) {
            if (byteArray.length - offset >= MAX_ENCRYPT_BLOCK) {
                final byte[] doFinal = cipher.doFinal(byteArray, offset, MAX_ENCRYPT_BLOCK);
                Intrinsics.checkNotNullExpressionValue((Object)doFinal, "cipher.doFinal(byteArray, offset, MAX_ENCRYPT_BLOCK)");
                temp = doFinal;
                offset += MAX_ENCRYPT_BLOCK;
            }
            else {
                final byte[] doFinal2 = cipher.doFinal(byteArray, offset, byteArray.length - offset);
                Intrinsics.checkNotNullExpressionValue((Object)doFinal2, "cipher.doFinal(byteArray, offset, byteArray.size - offset)");
                temp = doFinal2;
                offset = byteArray.length;
            }
            bos.write(temp);
        }
        bos.close();
        final String encodeToString = Base64.encodeToString(bos.toByteArray(), 2);
        Intrinsics.checkNotNullExpressionValue((Object)encodeToString, "encodeToString(bos.toByteArray(), Base64.NO_WRAP)");
        return encodeToString;
    }
    
    @Nullable
    public final String decryptSegmentByPrivateKey(@NotNull final String input, @NotNull final PrivateKey privateKey, final int keySize) {
        Intrinsics.checkNotNullParameter((Object)input, "input");
        Intrinsics.checkNotNullParameter((Object)privateKey, "privateKey");
        final byte[] byteArray = Base64.decode(input, 2);
        byte[] temp = null;
        int offset = 0;
        final int MAX_DECRYPT_BLOCK = keySize / 8;
        final ByteArrayOutputStream bos = new ByteArrayOutputStream();
        final Cipher cipher = Cipher.getInstance("RSA");
        cipher.init(2, privateKey);
        while (byteArray.length - offset > 0) {
            if (byteArray.length - offset >= MAX_DECRYPT_BLOCK) {
                final byte[] doFinal = cipher.doFinal(byteArray, offset, MAX_DECRYPT_BLOCK);
                Intrinsics.checkNotNullExpressionValue((Object)doFinal, "cipher.doFinal(byteArray, offset, MAX_DECRYPT_BLOCK)");
                temp = doFinal;
                offset += MAX_DECRYPT_BLOCK;
            }
            else {
                final byte[] doFinal2 = cipher.doFinal(byteArray, offset, byteArray.length - offset);
                Intrinsics.checkNotNullExpressionValue((Object)doFinal2, "cipher.doFinal(byteArray, offset, byteArray.size - offset)");
                temp = doFinal2;
                offset = byteArray.length;
            }
            bos.write(temp);
        }
        bos.close();
        final byte[] byteArray2 = bos.toByteArray();
        Intrinsics.checkNotNullExpressionValue((Object)byteArray2, "bos.toByteArray()");
        return new String(byteArray2, Charsets.UTF_8);
    }
    
    @NotNull
    public final KeyPair generateKeys() {
        final KeyPairGenerator generator = KeyPairGenerator.getInstance("RSA");
        final KeyPair genKeyPair = generator.genKeyPair();
        Intrinsics.checkNotNullExpressionValue((Object)genKeyPair, "generator.genKeyPair()");
        return genKeyPair;
    }
    
    @JvmOverloads
    @NotNull
    public final String base64Decode(@NotNull final String str) {
        Intrinsics.checkNotNullParameter((Object)str, "str");
        return base64Decode$default(this, str, 0, 2, null);
    }
    
    @JvmOverloads
    @Nullable
    public final String base64Encode(@NotNull final String str) {
        Intrinsics.checkNotNullParameter((Object)str, "str");
        return base64Encode$default(this, str, 0, 2, null);
    }
    
    static {
        INSTANCE = new EncoderUtils();
    }
}
