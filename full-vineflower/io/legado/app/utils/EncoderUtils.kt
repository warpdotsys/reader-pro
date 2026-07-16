package io.legado.app.utils

import java.io.ByteArrayOutputStream
import java.security.KeyPair
import java.security.KeyPairGenerator
import java.security.PrivateKey
import java.security.PublicKey
import javax.crypto.Cipher
import javax.crypto.spec.IvParameterSpec
import javax.crypto.spec.SecretKeySpec

public object EncoderUtils {
   public fun escape(src: String): String {
      val tmp: StringBuilder = new StringBuilder();
      var var3: java.lang.String = src;
      var var4: Int = 0;
      val var5: Int = src.length();

      while (var4 < var5) {
         val var6: Char = var3.charAt(var4);
         var4++;
         if (('0' > var6 || var6 > '9') && ('A' > var6 || var6 > 'Z') && ('a' > var6 || var6 > 'z')) {
            val var10000: StringBuilder = tmp.append(if (var6 < 16) "%0" else (if (var6 < 256) "%" else "%u"));
            val var10001: java.lang.String = Integer.toString(var6, CharsKt.checkRadix(16));
            var10000.append(var10001);
         } else {
            tmp.append(var6);
         }
      }

      var3 = tmp.toString();
      return var3;
   }

   @JvmOverloads
   public fun base64Decode(str: String, flags: Int = 0): String {
      val bytes: ByteArray = Base64.decode(str, flags);
      return new java.lang.String(bytes, Charsets.UTF_8);
   }

   @JvmOverloads
   public fun base64Encode(str: String, flags: Int = 2): String? {
      val var10000: ByteArray = str.getBytes(Charsets.UTF_8);
      return Base64.encodeToString(var10000, flags);
   }

   @Throws(java/lang/Exception::class)
   public fun encryptAES2Base64(data: ByteArray?, key: ByteArray?, transformation: String? = "DES/ECB/PKCS5Padding", iv: ByteArray? = null): ByteArray? {
      return Base64.encode(this.encryptAES(data, key, transformation, iv), 2);
   }

   @Throws(java/lang/Exception::class)
   public fun encryptAES(data: ByteArray?, key: ByteArray?, transformation: String? = "DES/ECB/PKCS5Padding", iv: ByteArray? = null): ByteArray? {
      return this.symmetricTemplate(data, key, "AES", transformation, iv, true);
   }

   @Throws(java/lang/Exception::class)
   public fun decryptBase64AES(data: ByteArray?, key: ByteArray?, transformation: String = "DES/ECB/PKCS5Padding", iv: ByteArray? = null): ByteArray? {
      return this.decryptAES(Base64.decode(data, 2), key, transformation, iv);
   }

   @Throws(java/lang/Exception::class)
   public fun decryptAES(data: ByteArray?, key: ByteArray?, transformation: String = "DES/ECB/PKCS5Padding", iv: ByteArray? = null): ByteArray? {
      return this.symmetricTemplate(data, key, "AES", transformation, iv, false);
   }

   @Throws(java/lang/Exception::class)
   private fun symmetricTemplate(data: ByteArray?, key: ByteArray?, algorithm: String, transformation: String, iv: ByteArray?, isEncrypt: Boolean): ByteArray? {
      if (data != null && data.length != 0 && key != null && key.length != 0) {
         val keySpec: SecretKeySpec = new SecretKeySpec(key, algorithm);
         val var13: Cipher = Cipher.getInstance(transformation);
         val mode: Int = if (isEncrypt) 1 else 2;
         if (iv != null && iv.length != 0) {
            var13.init(mode, keySpec, new IvParameterSpec(iv));
         } else {
            var13.init(mode, keySpec);
         }

         return var13.doFinal(data);
      } else {
         return null;
      }
   }

   @Throws(java/lang/Exception::class)
   public fun encryptDES2Base64(data: ByteArray?, key: ByteArray?, transformation: String? = "DES/ECB/PKCS5Padding", iv: ByteArray? = null): ByteArray? {
      return Base64.encode(this.encryptDES(data, key, transformation, iv), 2);
   }

   @Throws(java/lang/Exception::class)
   public fun encryptDES(data: ByteArray?, key: ByteArray?, transformation: String? = "DES/ECB/PKCS5Padding", iv: ByteArray? = null): ByteArray? {
      return this.symmetricTemplate(data, key, "DES", transformation, iv, true);
   }

   @Throws(java/lang/Exception::class)
   public fun decryptBase64DES(data: ByteArray?, key: ByteArray?, transformation: String = "DES/ECB/PKCS5Padding", iv: ByteArray? = null): ByteArray? {
      return this.decryptDES(Base64.decode(data, 2), key, transformation, iv);
   }

   @Throws(java/lang/Exception::class)
   public fun decryptDES(data: ByteArray?, key: ByteArray?, transformation: String = "DES/ECB/PKCS5Padding", iv: ByteArray? = null): ByteArray? {
      return this.symmetricTemplate(data, key, "DES", transformation, iv, false);
   }

   @Throws(java/lang/Exception::class)
   public fun encryptDESede2Base64(data: ByteArray?, key: ByteArray?, transformation: String? = "DESede/ECB/PKCS5Padding", iv: ByteArray? = null): ByteArray? {
      return Base64.encode(this.encryptDESede(data, key, transformation, iv), 2);
   }

   @Throws(java/lang/Exception::class)
   public fun encryptDESede(data: ByteArray?, key: ByteArray?, transformation: String? = "DESede/ECB/PKCS5Padding", iv: ByteArray? = null): ByteArray? {
      return this.symmetricTemplate(data, key, "DESede", transformation, iv, true);
   }

   @Throws(java/lang/Exception::class)
   public fun decryptBase64DESede(data: ByteArray?, key: ByteArray?, transformation: String = "DESede/ECB/PKCS5Padding", iv: ByteArray? = null): ByteArray? {
      return this.decryptDESede(Base64.decode(data, 2), key, transformation, iv);
   }

   @Throws(java/lang/Exception::class)
   public fun decryptDESede(data: ByteArray?, key: ByteArray?, transformation: String = "DESede/ECB/PKCS5Padding", iv: ByteArray? = null): ByteArray? {
      return this.symmetricTemplate(data, key, "DESede", transformation, iv, false);
   }

   public fun encryptByPrivateKey(input: String, privateKey: PrivateKey): String {
      val cipher: Cipher = Cipher.getInstance("RSA");
      cipher.init(1, privateKey);
      val var10001: ByteArray = input.getBytes(Charsets.UTF_8);
      val var5: java.lang.String = Base64.encodeToString(cipher.doFinal(var10001), 2);
      return var5;
   }

   public fun decryptByPublicKey(input: String, publicKey: PublicKey): String {
      val decode: ByteArray = Base64.decode(input, 2);
      val cipher: Cipher = Cipher.getInstance("RSA");
      cipher.init(2, publicKey);
      val encrypt: ByteArray = cipher.doFinal(decode);
      return new java.lang.String(encrypt, Charsets.UTF_8);
   }

   public fun encryptByPublicKey(input: String, publicKey: PublicKey): String {
      val cipher: Cipher = Cipher.getInstance("RSA");
      cipher.init(1, publicKey);
      val var10001: ByteArray = input.getBytes(Charsets.UTF_8);
      val var5: java.lang.String = Base64.encodeToString(cipher.doFinal(var10001), 2);
      return var5;
   }

   public fun decryptByPrivateKey(input: String, privateKey: PrivateKey): String {
      val decode: ByteArray = Base64.decode(input, 2);
      val cipher: Cipher = Cipher.getInstance("RSA");
      cipher.init(2, privateKey);
      val encrypt: ByteArray = cipher.doFinal(decode);
      return new java.lang.String(encrypt, Charsets.UTF_8);
   }

   public fun encryptSegmentByPrivateKey(input: String, privateKey: PrivateKey, keySize: Int = 2048): String {
      val var10000: ByteArray = input.getBytes(Charsets.UTF_8);
      val byteArray: ByteArray = var10000;
      var var12: Int = 0;
      val var13: Int = keySize / 8 - 11;
      val bos: ByteArrayOutputStream = new ByteArrayOutputStream();
      val cipher: Cipher = Cipher.getInstance("RSA");
      cipher.init(1, privateKey);

      while (byteArray.length - offset > 0) {
         val var11: ByteArray;
         if (byteArray.length - var12 >= var13) {
            val var10: ByteArray = cipher.doFinal(byteArray, var12, var13);
            var11 = var10;
            var12 += var13;
         } else {
            val var14: ByteArray = cipher.doFinal(byteArray, var12, byteArray.length - var12);
            var11 = var14;
            var12 = byteArray.length;
         }

         bos.write(var11);
      }

      bos.close();
      val var15: java.lang.String = Base64.encodeToString(bos.toByteArray(), 2);
      return var15;
   }

   public fun decryptSegmentByPublicKey(input: String, publicKey: PublicKey, keySize: Int = 2048): String? {
      val byteArray: ByteArray = Base64.decode(input, 2);
      var offset: Int = 0;
      val MAX_DECRYPT_BLOCK: Int = keySize / 8;
      val bos: ByteArrayOutputStream = new ByteArrayOutputStream();
      val cipher: Cipher = Cipher.getInstance("RSA");
      cipher.init(2, publicKey);

      while (byteArray.length - offset > 0) {
         val var12: ByteArray;
         if (byteArray.length - offset >= MAX_DECRYPT_BLOCK) {
            val var10: ByteArray = cipher.doFinal(byteArray, offset, MAX_DECRYPT_BLOCK);
            var12 = var10;
            offset += MAX_DECRYPT_BLOCK;
         } else {
            val var13: ByteArray = cipher.doFinal(byteArray, offset, byteArray.length - offset);
            var12 = var13;
            offset = byteArray.length;
         }

         bos.write(var12);
      }

      bos.close();
      val var14: ByteArray = bos.toByteArray();
      return new java.lang.String(var14, Charsets.UTF_8);
   }

   public fun encryptSegmentByPublicKey(input: String, publicKey: PublicKey, keySize: Int = 2048): String {
      val var10000: ByteArray = input.getBytes(Charsets.UTF_8);
      val byteArray: ByteArray = var10000;
      var var12: Int = 0;
      val var13: Int = keySize / 8 - 11;
      val bos: ByteArrayOutputStream = new ByteArrayOutputStream();
      val cipher: Cipher = Cipher.getInstance("RSA");
      cipher.init(1, publicKey);

      while (byteArray.length - offset > 0) {
         val var11: ByteArray;
         if (byteArray.length - var12 >= var13) {
            val var10: ByteArray = cipher.doFinal(byteArray, var12, var13);
            var11 = var10;
            var12 += var13;
         } else {
            val var14: ByteArray = cipher.doFinal(byteArray, var12, byteArray.length - var12);
            var11 = var14;
            var12 = byteArray.length;
         }

         bos.write(var11);
      }

      bos.close();
      val var15: java.lang.String = Base64.encodeToString(bos.toByteArray(), 2);
      return var15;
   }

   public fun decryptSegmentByPrivateKey(input: String, privateKey: PrivateKey, keySize: Int = 2048): String? {
      val byteArray: ByteArray = Base64.decode(input, 2);
      var offset: Int = 0;
      val MAX_DECRYPT_BLOCK: Int = keySize / 8;
      val bos: ByteArrayOutputStream = new ByteArrayOutputStream();
      val cipher: Cipher = Cipher.getInstance("RSA");
      cipher.init(2, privateKey);

      while (byteArray.length - offset > 0) {
         val var12: ByteArray;
         if (byteArray.length - offset >= MAX_DECRYPT_BLOCK) {
            val var10: ByteArray = cipher.doFinal(byteArray, offset, MAX_DECRYPT_BLOCK);
            var12 = var10;
            offset += MAX_DECRYPT_BLOCK;
         } else {
            val var13: ByteArray = cipher.doFinal(byteArray, offset, byteArray.length - offset);
            var12 = var13;
            offset = byteArray.length;
         }

         bos.write(var12);
      }

      bos.close();
      val var14: ByteArray = bos.toByteArray();
      return new java.lang.String(var14, Charsets.UTF_8);
   }

   public fun generateKeys(): KeyPair {
      val var2: KeyPair = KeyPairGenerator.getInstance("RSA").genKeyPair();
      return var2;
   }

   @JvmOverloads
   fun base64Decode(str: java.lang.String): java.lang.String {
      return base64Decode$default(this, str, 0, 2, null);
   }

   @JvmOverloads
   fun base64Encode(str: java.lang.String): java.lang.String? {
      return base64Encode$default(this, str, 0, 2, null);
   }
}
