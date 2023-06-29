package org.apache.commons.codec.digest;

import org.apache.commons.codec.binary.StringUtils;

public final class MurmurHash2 {
   private static final int M32 = 1540483477;
   private static final int R32 = 24;
   private static final long M64 = -4132994306676758123L;
   private static final int R64 = 47;

   private MurmurHash2() {
   }

   public static int hash32(byte[] data, int length, int seed) {
      int h = seed ^ length;
      int nblocks = length >> 2;

      int index;
      for(index = 0; index < nblocks; ++index) {
         int index = index << 2;
         int k = getLittleEndianInt(data, index);
         k *= 1540483477;
         k ^= k >>> 24;
         k *= 1540483477;
         h *= 1540483477;
         h ^= k;
      }

      index = nblocks << 2;
      switch (length - index) {
         case 3:
            h ^= (data[index + 2] & 255) << 16;
         case 2:
            h ^= (data[index + 1] & 255) << 8;
         case 1:
            h ^= data[index] & 255;
            h *= 1540483477;
         default:
            h ^= h >>> 13;
            h *= 1540483477;
            h ^= h >>> 15;
            return h;
      }
   }

   public static int hash32(byte[] data, int length) {
      return hash32(data, length, -1756908916);
   }

   public static int hash32(String text) {
      byte[] bytes = StringUtils.getBytesUtf8(text);
      return hash32(bytes, bytes.length);
   }

   public static int hash32(String text, int from, int length) {
      return hash32(text.substring(from, from + length));
   }

   public static long hash64(byte[] data, int length, int seed) {
      long h = (long)seed & 4294967295L ^ (long)length * -4132994306676758123L;
      int nblocks = length >> 3;

      int index;
      for(index = 0; index < nblocks; ++index) {
         int index = index << 3;
         long k = getLittleEndianLong(data, index);
         k *= -4132994306676758123L;
         k ^= k >>> 47;
         k *= -4132994306676758123L;
         h ^= k;
         h *= -4132994306676758123L;
      }

      index = nblocks << 3;
      switch (length - index) {
         case 7:
            h ^= ((long)data[index + 6] & 255L) << 48;
         case 6:
            h ^= ((long)data[index + 5] & 255L) << 40;
         case 5:
            h ^= ((long)data[index + 4] & 255L) << 32;
         case 4:
            h ^= ((long)data[index + 3] & 255L) << 24;
         case 3:
            h ^= ((long)data[index + 2] & 255L) << 16;
         case 2:
            h ^= ((long)data[index + 1] & 255L) << 8;
         case 1:
            h ^= (long)data[index] & 255L;
            h *= -4132994306676758123L;
         default:
            h ^= h >>> 47;
            h *= -4132994306676758123L;
            h ^= h >>> 47;
            return h;
      }
   }

   public static long hash64(byte[] data, int length) {
      return hash64(data, length, -512093083);
   }

   public static long hash64(String text) {
      byte[] bytes = StringUtils.getBytesUtf8(text);
      return hash64(bytes, bytes.length);
   }

   public static long hash64(String text, int from, int length) {
      return hash64(text.substring(from, from + length));
   }

   private static int getLittleEndianInt(byte[] data, int index) {
      return data[index] & 255 | (data[index + 1] & 255) << 8 | (data[index + 2] & 255) << 16 | (data[index + 3] & 255) << 24;
   }

   private static long getLittleEndianLong(byte[] data, int index) {
      return (long)data[index] & 255L | ((long)data[index + 1] & 255L) << 8 | ((long)data[index + 2] & 255L) << 16 | ((long)data[index + 3] & 255L) << 24 | ((long)data[index + 4] & 255L) << 32 | ((long)data[index + 5] & 255L) << 40 | ((long)data[index + 6] & 255L) << 48 | ((long)data[index + 7] & 255L) << 56;
   }
}
