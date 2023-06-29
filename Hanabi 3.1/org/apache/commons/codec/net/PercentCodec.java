package org.apache.commons.codec.net;

import java.nio.ByteBuffer;
import java.util.BitSet;
import org.apache.commons.codec.BinaryDecoder;
import org.apache.commons.codec.BinaryEncoder;
import org.apache.commons.codec.DecoderException;
import org.apache.commons.codec.EncoderException;

public class PercentCodec implements BinaryEncoder, BinaryDecoder {
   private static final byte ESCAPE_CHAR = 37;
   private final BitSet alwaysEncodeChars = new BitSet();
   private final boolean plusForSpace;
   private int alwaysEncodeCharsMin = Integer.MAX_VALUE;
   private int alwaysEncodeCharsMax = Integer.MIN_VALUE;

   public PercentCodec() {
      this.plusForSpace = false;
      this.insertAlwaysEncodeChar((byte)37);
   }

   public PercentCodec(byte[] alwaysEncodeChars, boolean plusForSpace) {
      this.plusForSpace = plusForSpace;
      this.insertAlwaysEncodeChars(alwaysEncodeChars);
   }

   private void insertAlwaysEncodeChars(byte[] alwaysEncodeCharsArray) {
      if (alwaysEncodeCharsArray != null) {
         byte[] var2 = alwaysEncodeCharsArray;
         int var3 = alwaysEncodeCharsArray.length;

         for(int var4 = 0; var4 < var3; ++var4) {
            byte b = var2[var4];
            this.insertAlwaysEncodeChar(b);
         }
      }

      this.insertAlwaysEncodeChar((byte)37);
   }

   private void insertAlwaysEncodeChar(byte b) {
      this.alwaysEncodeChars.set(b);
      if (b < this.alwaysEncodeCharsMin) {
         this.alwaysEncodeCharsMin = b;
      }

      if (b > this.alwaysEncodeCharsMax) {
         this.alwaysEncodeCharsMax = b;
      }

   }

   public byte[] encode(byte[] bytes) throws EncoderException {
      if (bytes == null) {
         return null;
      } else {
         int expectedEncodingBytes = this.expectedEncodingBytes(bytes);
         boolean willEncode = expectedEncodingBytes != bytes.length;
         return !willEncode && (!this.plusForSpace || !this.containsSpace(bytes)) ? bytes : this.doEncode(bytes, expectedEncodingBytes, willEncode);
      }
   }

   private byte[] doEncode(byte[] bytes, int expectedLength, boolean willEncode) {
      ByteBuffer buffer = ByteBuffer.allocate(expectedLength);
      byte[] var5 = bytes;
      int var6 = bytes.length;

      for(int var7 = 0; var7 < var6; ++var7) {
         byte b = var5[var7];
         if (willEncode && this.canEncode(b)) {
            byte bb = b;
            if (b < 0) {
               bb = (byte)(256 + b);
            }

            char hex1 = Utils.hexDigit(bb >> 4);
            char hex2 = Utils.hexDigit(bb);
            buffer.put((byte)37);
            buffer.put((byte)hex1);
            buffer.put((byte)hex2);
         } else if (this.plusForSpace && b == 32) {
            buffer.put((byte)43);
         } else {
            buffer.put(b);
         }
      }

      return buffer.array();
   }

   private int expectedEncodingBytes(byte[] bytes) {
      int byteCount = 0;
      byte[] var3 = bytes;
      int var4 = bytes.length;

      for(int var5 = 0; var5 < var4; ++var5) {
         byte b = var3[var5];
         byteCount += this.canEncode(b) ? 3 : 1;
      }

      return byteCount;
   }

   private boolean containsSpace(byte[] bytes) {
      byte[] var2 = bytes;
      int var3 = bytes.length;

      for(int var4 = 0; var4 < var3; ++var4) {
         byte b = var2[var4];
         if (b == 32) {
            return true;
         }
      }

      return false;
   }

   private boolean canEncode(byte c) {
      return !this.isAsciiChar(c) || this.inAlwaysEncodeCharsRange(c) && this.alwaysEncodeChars.get(c);
   }

   private boolean inAlwaysEncodeCharsRange(byte c) {
      return c >= this.alwaysEncodeCharsMin && c <= this.alwaysEncodeCharsMax;
   }

   private boolean isAsciiChar(byte c) {
      return c >= 0;
   }

   public byte[] decode(byte[] bytes) throws DecoderException {
      if (bytes == null) {
         return null;
      } else {
         ByteBuffer buffer = ByteBuffer.allocate(this.expectedDecodingBytes(bytes));

         for(int i = 0; i < bytes.length; ++i) {
            byte b = bytes[i];
            if (b == 37) {
               try {
                  ++i;
                  int u = Utils.digit16(bytes[i]);
                  ++i;
                  int l = Utils.digit16(bytes[i]);
                  buffer.put((byte)((u << 4) + l));
               } catch (ArrayIndexOutOfBoundsException var7) {
                  throw new DecoderException("Invalid percent decoding: ", var7);
               }
            } else if (this.plusForSpace && b == 43) {
               buffer.put((byte)32);
            } else {
               buffer.put(b);
            }
         }

         return buffer.array();
      }
   }

   private int expectedDecodingBytes(byte[] bytes) {
      int byteCount = 0;

      for(int i = 0; i < bytes.length; ++byteCount) {
         byte b = bytes[i];
         i += b == 37 ? 3 : 1;
      }

      return byteCount;
   }

   public Object encode(Object obj) throws EncoderException {
      if (obj == null) {
         return null;
      } else if (obj instanceof byte[]) {
         return this.encode((byte[])((byte[])obj));
      } else {
         throw new EncoderException("Objects of type " + obj.getClass().getName() + " cannot be Percent encoded");
      }
   }

   public Object decode(Object obj) throws DecoderException {
      if (obj == null) {
         return null;
      } else if (obj instanceof byte[]) {
         return this.decode((byte[])((byte[])obj));
      } else {
         throw new DecoderException("Objects of type " + obj.getClass().getName() + " cannot be Percent decoded");
      }
   }
}
