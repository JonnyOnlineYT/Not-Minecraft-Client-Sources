package org.apache.commons.codec.digest;

import java.util.zip.Checksum;

public class XXHash32 implements Checksum {
   private static final int BUF_SIZE = 16;
   private static final int ROTATE_BITS = 13;
   private static final int PRIME1 = -1640531535;
   private static final int PRIME2 = -2048144777;
   private static final int PRIME3 = -1028477379;
   private static final int PRIME4 = 668265263;
   private static final int PRIME5 = 374761393;
   private final byte[] oneByte;
   private final int[] state;
   private final byte[] buffer;
   private final int seed;
   private int totalLen;
   private int pos;
   private boolean stateUpdated;

   public XXHash32() {
      this(0);
   }

   public XXHash32(int seed) {
      this.oneByte = new byte[1];
      this.state = new int[4];
      this.buffer = new byte[16];
      this.seed = seed;
      this.initializeState();
   }

   public void reset() {
      this.initializeState();
      this.totalLen = 0;
      this.pos = 0;
      this.stateUpdated = false;
   }

   public void update(int b) {
      this.oneByte[0] = (byte)(b & 255);
      this.update(this.oneByte, 0, 1);
   }

   public void update(byte[] b, int off, int len) {
      if (len > 0) {
         this.totalLen += len;
         int end = off + len;
         if (this.pos + len - 16 < 0) {
            System.arraycopy(b, off, this.buffer, this.pos, len);
            this.pos += len;
         } else {
            int limit;
            if (this.pos > 0) {
               limit = 16 - this.pos;
               System.arraycopy(b, off, this.buffer, this.pos, limit);
               this.process(this.buffer, 0);
               off += limit;
            }

            for(limit = end - 16; off <= limit; off += 16) {
               this.process(b, off);
            }

            if (off < end) {
               this.pos = end - off;
               System.arraycopy(b, off, this.buffer, 0, this.pos);
            } else {
               this.pos = 0;
            }

         }
      }
   }

   public long getValue() {
      int hash;
      if (this.stateUpdated) {
         hash = Integer.rotateLeft(this.state[0], 1) + Integer.rotateLeft(this.state[1], 7) + Integer.rotateLeft(this.state[2], 12) + Integer.rotateLeft(this.state[3], 18);
      } else {
         hash = this.state[2] + 374761393;
      }

      hash += this.totalLen;
      int idx = 0;

      for(int limit = this.pos - 4; idx <= limit; idx += 4) {
         hash = Integer.rotateLeft(hash + getInt(this.buffer, idx) * -1028477379, 17) * 668265263;
      }

      while(idx < this.pos) {
         hash = Integer.rotateLeft(hash + (this.buffer[idx++] & 255) * 374761393, 11) * -1640531535;
      }

      hash ^= hash >>> 15;
      hash *= -2048144777;
      hash ^= hash >>> 13;
      hash *= -1028477379;
      hash ^= hash >>> 16;
      return (long)hash & 4294967295L;
   }

   private static int getInt(byte[] buffer, int idx) {
      return buffer[idx] & 255 | (buffer[idx + 1] & 255) << 8 | (buffer[idx + 2] & 255) << 16 | (buffer[idx + 3] & 255) << 24;
   }

   private void initializeState() {
      this.state[0] = this.seed + -1640531535 + -2048144777;
      this.state[1] = this.seed + -2048144777;
      this.state[2] = this.seed;
      this.state[3] = this.seed - -1640531535;
   }

   private void process(byte[] b, int offset) {
      int s0 = this.state[0];
      int s1 = this.state[1];
      int s2 = this.state[2];
      int s3 = this.state[3];
      s0 = Integer.rotateLeft(s0 + getInt(b, offset) * -2048144777, 13) * -1640531535;
      s1 = Integer.rotateLeft(s1 + getInt(b, offset + 4) * -2048144777, 13) * -1640531535;
      s2 = Integer.rotateLeft(s2 + getInt(b, offset + 8) * -2048144777, 13) * -1640531535;
      s3 = Integer.rotateLeft(s3 + getInt(b, offset + 12) * -2048144777, 13) * -1640531535;
      this.state[0] = s0;
      this.state[1] = s1;
      this.state[2] = s2;
      this.state[3] = s3;
      this.stateUpdated = true;
   }
}
