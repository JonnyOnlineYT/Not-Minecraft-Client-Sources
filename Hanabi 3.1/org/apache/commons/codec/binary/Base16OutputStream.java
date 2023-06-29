package org.apache.commons.codec.binary;

import java.io.OutputStream;
import org.apache.commons.codec.CodecPolicy;

public class Base16OutputStream extends BaseNCodecOutputStream {
   public Base16OutputStream(OutputStream out) {
      this(out, true);
   }

   public Base16OutputStream(OutputStream out, boolean doEncode) {
      this(out, doEncode, false);
   }

   public Base16OutputStream(OutputStream out, boolean doEncode, boolean lowerCase) {
      this(out, doEncode, lowerCase, CodecPolicy.LENIENT);
   }

   public Base16OutputStream(OutputStream out, boolean doEncode, boolean lowerCase, CodecPolicy decodingPolicy) {
      super(out, new Base16(lowerCase, decodingPolicy), doEncode);
   }
}
