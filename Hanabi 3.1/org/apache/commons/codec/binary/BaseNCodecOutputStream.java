package org.apache.commons.codec.binary;

import java.io.FilterOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Objects;

public class BaseNCodecOutputStream extends FilterOutputStream {
   private final boolean doEncode;
   private final BaseNCodec baseNCodec;
   private final byte[] singleByte = new byte[1];
   private final BaseNCodec.Context context = new BaseNCodec.Context();

   public BaseNCodecOutputStream(OutputStream output, BaseNCodec basedCodec, boolean doEncode) {
      super(output);
      this.baseNCodec = basedCodec;
      this.doEncode = doEncode;
   }

   public void close() throws IOException {
      this.eof();
      this.flush();
      this.out.close();
   }

   public void eof() throws IOException {
      if (this.doEncode) {
         this.baseNCodec.encode(this.singleByte, 0, -1, this.context);
      } else {
         this.baseNCodec.decode(this.singleByte, 0, -1, this.context);
      }

   }

   public void flush() throws IOException {
      this.flush(true);
   }

   private void flush(boolean propagate) throws IOException {
      int avail = this.baseNCodec.available(this.context);
      if (avail > 0) {
         byte[] buf = new byte[avail];
         int c = this.baseNCodec.readResults(buf, 0, avail, this.context);
         if (c > 0) {
            this.out.write(buf, 0, c);
         }
      }

      if (propagate) {
         this.out.flush();
      }

   }

   public boolean isStrictDecoding() {
      return this.baseNCodec.isStrictDecoding();
   }

   public void write(byte[] array, int offset, int len) throws IOException {
      Objects.requireNonNull(array, "array");
      if (offset >= 0 && len >= 0) {
         if (offset <= array.length && offset + len <= array.length) {
            if (len > 0) {
               if (this.doEncode) {
                  this.baseNCodec.encode(array, offset, len, this.context);
               } else {
                  this.baseNCodec.decode(array, offset, len, this.context);
               }

               this.flush(false);
            }

         } else {
            throw new IndexOutOfBoundsException();
         }
      } else {
         throw new IndexOutOfBoundsException();
      }
   }

   public void write(int i) throws IOException {
      this.singleByte[0] = (byte)i;
      this.write(this.singleByte, 0, 1);
   }
}
