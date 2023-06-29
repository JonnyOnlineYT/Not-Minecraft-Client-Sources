package org.apache.commons.codec.digest;

import java.security.SecureRandom;
import java.util.Random;

class B64 {
   static final String B64T_STRING = "./0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz";
   static final char[] B64T_ARRAY = "./0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz".toCharArray();

   static void b64from24bit(byte b2, byte b1, byte b0, int outLen, StringBuilder buffer) {
      int w = b2 << 16 & 16777215 | b1 << 8 & '\uffff' | b0 & 255;

      for(int n = outLen; n-- > 0; w >>= 6) {
         buffer.append(B64T_ARRAY[w & 63]);
      }

   }

   static String getRandomSalt(int num) {
      return getRandomSalt(num, new SecureRandom());
   }

   static String getRandomSalt(int num, Random random) {
      StringBuilder saltString = new StringBuilder(num);

      for(int i = 1; i <= num; ++i) {
         saltString.append("./0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz".charAt(random.nextInt("./0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz".length())));
      }

      return saltString.toString();
   }
}
