package org.apache.commons.codec.digest;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import org.apache.commons.codec.binary.Hex;
import org.apache.commons.codec.binary.StringUtils;

public final class HmacUtils {
   private static final int STREAM_BUFFER_LENGTH = 1024;
   private final Mac mac;

   public static boolean isAvailable(String name) {
      try {
         Mac.getInstance(name);
         return true;
      } catch (NoSuchAlgorithmException var2) {
         return false;
      }
   }

   public static boolean isAvailable(HmacAlgorithms name) {
      try {
         Mac.getInstance(name.getName());
         return true;
      } catch (NoSuchAlgorithmException var2) {
         return false;
      }
   }

   /** @deprecated */
   @Deprecated
   public static Mac getHmacMd5(byte[] key) {
      return getInitializedMac(HmacAlgorithms.HMAC_MD5, key);
   }

   /** @deprecated */
   @Deprecated
   public static Mac getHmacSha1(byte[] key) {
      return getInitializedMac(HmacAlgorithms.HMAC_SHA_1, key);
   }

   /** @deprecated */
   @Deprecated
   public static Mac getHmacSha256(byte[] key) {
      return getInitializedMac(HmacAlgorithms.HMAC_SHA_256, key);
   }

   /** @deprecated */
   @Deprecated
   public static Mac getHmacSha384(byte[] key) {
      return getInitializedMac(HmacAlgorithms.HMAC_SHA_384, key);
   }

   /** @deprecated */
   @Deprecated
   public static Mac getHmacSha512(byte[] key) {
      return getInitializedMac(HmacAlgorithms.HMAC_SHA_512, key);
   }

   public static Mac getInitializedMac(HmacAlgorithms algorithm, byte[] key) {
      return getInitializedMac(algorithm.getName(), key);
   }

   public static Mac getInitializedMac(String algorithm, byte[] key) {
      if (key == null) {
         throw new IllegalArgumentException("Null key");
      } else {
         try {
            SecretKeySpec keySpec = new SecretKeySpec(key, algorithm);
            Mac mac = Mac.getInstance(algorithm);
            mac.init(keySpec);
            return mac;
         } catch (NoSuchAlgorithmException var4) {
            throw new IllegalArgumentException(var4);
         } catch (InvalidKeyException var5) {
            throw new IllegalArgumentException(var5);
         }
      }
   }

   /** @deprecated */
   @Deprecated
   public static byte[] hmacMd5(byte[] key, byte[] valueToDigest) {
      return (new HmacUtils(HmacAlgorithms.HMAC_MD5, key)).hmac(valueToDigest);
   }

   /** @deprecated */
   @Deprecated
   public static byte[] hmacMd5(byte[] key, InputStream valueToDigest) throws IOException {
      return (new HmacUtils(HmacAlgorithms.HMAC_MD5, key)).hmac(valueToDigest);
   }

   /** @deprecated */
   @Deprecated
   public static byte[] hmacMd5(String key, String valueToDigest) {
      return (new HmacUtils(HmacAlgorithms.HMAC_MD5, key)).hmac(valueToDigest);
   }

   /** @deprecated */
   @Deprecated
   public static String hmacMd5Hex(byte[] key, byte[] valueToDigest) {
      return (new HmacUtils(HmacAlgorithms.HMAC_MD5, key)).hmacHex(valueToDigest);
   }

   /** @deprecated */
   @Deprecated
   public static String hmacMd5Hex(byte[] key, InputStream valueToDigest) throws IOException {
      return (new HmacUtils(HmacAlgorithms.HMAC_MD5, key)).hmacHex(valueToDigest);
   }

   /** @deprecated */
   @Deprecated
   public static String hmacMd5Hex(String key, String valueToDigest) {
      return (new HmacUtils(HmacAlgorithms.HMAC_MD5, key)).hmacHex(valueToDigest);
   }

   /** @deprecated */
   @Deprecated
   public static byte[] hmacSha1(byte[] key, byte[] valueToDigest) {
      return (new HmacUtils(HmacAlgorithms.HMAC_SHA_1, key)).hmac(valueToDigest);
   }

   /** @deprecated */
   @Deprecated
   public static byte[] hmacSha1(byte[] key, InputStream valueToDigest) throws IOException {
      return (new HmacUtils(HmacAlgorithms.HMAC_SHA_1, key)).hmac(valueToDigest);
   }

   /** @deprecated */
   @Deprecated
   public static byte[] hmacSha1(String key, String valueToDigest) {
      return (new HmacUtils(HmacAlgorithms.HMAC_SHA_1, key)).hmac(valueToDigest);
   }

   /** @deprecated */
   @Deprecated
   public static String hmacSha1Hex(byte[] key, byte[] valueToDigest) {
      return (new HmacUtils(HmacAlgorithms.HMAC_SHA_1, key)).hmacHex(valueToDigest);
   }

   /** @deprecated */
   @Deprecated
   public static String hmacSha1Hex(byte[] key, InputStream valueToDigest) throws IOException {
      return (new HmacUtils(HmacAlgorithms.HMAC_SHA_1, key)).hmacHex(valueToDigest);
   }

   /** @deprecated */
   @Deprecated
   public static String hmacSha1Hex(String key, String valueToDigest) {
      return (new HmacUtils(HmacAlgorithms.HMAC_SHA_1, key)).hmacHex(valueToDigest);
   }

   /** @deprecated */
   @Deprecated
   public static byte[] hmacSha256(byte[] key, byte[] valueToDigest) {
      return (new HmacUtils(HmacAlgorithms.HMAC_SHA_256, key)).hmac(valueToDigest);
   }

   /** @deprecated */
   @Deprecated
   public static byte[] hmacSha256(byte[] key, InputStream valueToDigest) throws IOException {
      return (new HmacUtils(HmacAlgorithms.HMAC_SHA_256, key)).hmac(valueToDigest);
   }

   /** @deprecated */
   @Deprecated
   public static byte[] hmacSha256(String key, String valueToDigest) {
      return (new HmacUtils(HmacAlgorithms.HMAC_SHA_256, key)).hmac(valueToDigest);
   }

   /** @deprecated */
   @Deprecated
   public static String hmacSha256Hex(byte[] key, byte[] valueToDigest) {
      return (new HmacUtils(HmacAlgorithms.HMAC_SHA_256, key)).hmacHex(valueToDigest);
   }

   /** @deprecated */
   @Deprecated
   public static String hmacSha256Hex(byte[] key, InputStream valueToDigest) throws IOException {
      return (new HmacUtils(HmacAlgorithms.HMAC_SHA_256, key)).hmacHex(valueToDigest);
   }

   /** @deprecated */
   @Deprecated
   public static String hmacSha256Hex(String key, String valueToDigest) {
      return (new HmacUtils(HmacAlgorithms.HMAC_SHA_256, key)).hmacHex(valueToDigest);
   }

   /** @deprecated */
   @Deprecated
   public static byte[] hmacSha384(byte[] key, byte[] valueToDigest) {
      return (new HmacUtils(HmacAlgorithms.HMAC_SHA_384, key)).hmac(valueToDigest);
   }

   /** @deprecated */
   @Deprecated
   public static byte[] hmacSha384(byte[] key, InputStream valueToDigest) throws IOException {
      return (new HmacUtils(HmacAlgorithms.HMAC_SHA_384, key)).hmac(valueToDigest);
   }

   /** @deprecated */
   @Deprecated
   public static byte[] hmacSha384(String key, String valueToDigest) {
      return (new HmacUtils(HmacAlgorithms.HMAC_SHA_384, key)).hmac(valueToDigest);
   }

   /** @deprecated */
   @Deprecated
   public static String hmacSha384Hex(byte[] key, byte[] valueToDigest) {
      return (new HmacUtils(HmacAlgorithms.HMAC_SHA_384, key)).hmacHex(valueToDigest);
   }

   /** @deprecated */
   @Deprecated
   public static String hmacSha384Hex(byte[] key, InputStream valueToDigest) throws IOException {
      return (new HmacUtils(HmacAlgorithms.HMAC_SHA_384, key)).hmacHex(valueToDigest);
   }

   /** @deprecated */
   @Deprecated
   public static String hmacSha384Hex(String key, String valueToDigest) {
      return (new HmacUtils(HmacAlgorithms.HMAC_SHA_384, key)).hmacHex(valueToDigest);
   }

   /** @deprecated */
   @Deprecated
   public static byte[] hmacSha512(byte[] key, byte[] valueToDigest) {
      return (new HmacUtils(HmacAlgorithms.HMAC_SHA_512, key)).hmac(valueToDigest);
   }

   /** @deprecated */
   @Deprecated
   public static byte[] hmacSha512(byte[] key, InputStream valueToDigest) throws IOException {
      return (new HmacUtils(HmacAlgorithms.HMAC_SHA_512, key)).hmac(valueToDigest);
   }

   /** @deprecated */
   @Deprecated
   public static byte[] hmacSha512(String key, String valueToDigest) {
      return (new HmacUtils(HmacAlgorithms.HMAC_SHA_512, key)).hmac(valueToDigest);
   }

   /** @deprecated */
   @Deprecated
   public static String hmacSha512Hex(byte[] key, byte[] valueToDigest) {
      return (new HmacUtils(HmacAlgorithms.HMAC_SHA_512, key)).hmacHex(valueToDigest);
   }

   /** @deprecated */
   @Deprecated
   public static String hmacSha512Hex(byte[] key, InputStream valueToDigest) throws IOException {
      return (new HmacUtils(HmacAlgorithms.HMAC_SHA_512, key)).hmacHex(valueToDigest);
   }

   /** @deprecated */
   @Deprecated
   public static String hmacSha512Hex(String key, String valueToDigest) {
      return (new HmacUtils(HmacAlgorithms.HMAC_SHA_512, key)).hmacHex(valueToDigest);
   }

   public static Mac updateHmac(Mac mac, byte[] valueToDigest) {
      mac.reset();
      mac.update(valueToDigest);
      return mac;
   }

   public static Mac updateHmac(Mac mac, InputStream valueToDigest) throws IOException {
      mac.reset();
      byte[] buffer = new byte[1024];

      for(int read = valueToDigest.read(buffer, 0, 1024); read > -1; read = valueToDigest.read(buffer, 0, 1024)) {
         mac.update(buffer, 0, read);
      }

      return mac;
   }

   public static Mac updateHmac(Mac mac, String valueToDigest) {
      mac.reset();
      mac.update(StringUtils.getBytesUtf8(valueToDigest));
      return mac;
   }

   /** @deprecated */
   @Deprecated
   public HmacUtils() {
      this((Mac)null);
   }

   private HmacUtils(Mac mac) {
      this.mac = mac;
   }

   public HmacUtils(String algorithm, byte[] key) {
      this(getInitializedMac(algorithm, key));
   }

   public HmacUtils(String algorithm, String key) {
      this(algorithm, StringUtils.getBytesUtf8(key));
   }

   public HmacUtils(HmacAlgorithms algorithm, String key) {
      this(algorithm.getName(), StringUtils.getBytesUtf8(key));
   }

   public HmacUtils(HmacAlgorithms algorithm, byte[] key) {
      this(algorithm.getName(), key);
   }

   public byte[] hmac(byte[] valueToDigest) {
      return this.mac.doFinal(valueToDigest);
   }

   public String hmacHex(byte[] valueToDigest) {
      return Hex.encodeHexString(this.hmac(valueToDigest));
   }

   public byte[] hmac(String valueToDigest) {
      return this.mac.doFinal(StringUtils.getBytesUtf8(valueToDigest));
   }

   public String hmacHex(String valueToDigest) {
      return Hex.encodeHexString(this.hmac(valueToDigest));
   }

   public byte[] hmac(ByteBuffer valueToDigest) {
      this.mac.update(valueToDigest);
      return this.mac.doFinal();
   }

   public String hmacHex(ByteBuffer valueToDigest) {
      return Hex.encodeHexString(this.hmac(valueToDigest));
   }

   public byte[] hmac(InputStream valueToDigest) throws IOException {
      byte[] buffer = new byte[1024];

      int read;
      while((read = valueToDigest.read(buffer, 0, 1024)) > -1) {
         this.mac.update(buffer, 0, read);
      }

      return this.mac.doFinal();
   }

   public String hmacHex(InputStream valueToDigest) throws IOException {
      return Hex.encodeHexString(this.hmac(valueToDigest));
   }

   public byte[] hmac(File valueToDigest) throws IOException {
      BufferedInputStream stream = new BufferedInputStream(new FileInputStream(valueToDigest));
      Throwable var3 = null;

      byte[] var4;
      try {
         var4 = this.hmac((InputStream)stream);
      } catch (Throwable var13) {
         var3 = var13;
         throw var13;
      } finally {
         if (stream != null) {
            if (var3 != null) {
               try {
                  stream.close();
               } catch (Throwable var12) {
                  var3.addSuppressed(var12);
               }
            } else {
               stream.close();
            }
         }

      }

      return var4;
   }

   public String hmacHex(File valueToDigest) throws IOException {
      return Hex.encodeHexString(this.hmac(valueToDigest));
   }
}
