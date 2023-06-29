package org.apache.commons.codec.digest;

import java.nio.charset.StandardCharsets;

public class Crypt {
   public static String crypt(byte[] keyBytes) {
      return crypt((byte[])keyBytes, (String)null);
   }

   public static String crypt(byte[] keyBytes, String salt) {
      if (salt == null) {
         return Sha2Crypt.sha512Crypt(keyBytes);
      } else if (salt.startsWith("$6$")) {
         return Sha2Crypt.sha512Crypt(keyBytes, salt);
      } else if (salt.startsWith("$5$")) {
         return Sha2Crypt.sha256Crypt(keyBytes, salt);
      } else {
         return salt.startsWith("$1$") ? Md5Crypt.md5Crypt(keyBytes, salt) : UnixCrypt.crypt(keyBytes, salt);
      }
   }

   public static String crypt(String key) {
      return crypt((String)key, (String)null);
   }

   public static String crypt(String key, String salt) {
      return crypt(key.getBytes(StandardCharsets.UTF_8), salt);
   }
}
