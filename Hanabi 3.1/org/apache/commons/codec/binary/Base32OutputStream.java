package org.apache.commons.codec.binary;

import java.io.OutputStream;
import org.apache.commons.codec.CodecPolicy;

public class Base32OutputStream extends BaseNCodecOutputStream {
   public Base32OutputStream(OutputStream out) {
      this(out, true);
   }

   public Base32OutputStream(OutputStream out, boolean doEncode) {
      super(out, new Base32(false), doEncode);
   }

   public Base32OutputStream(OutputStream ouput, boolean doEncode, int lineLength, byte[] lineSeparator) {
      super(ouput, new Base32(lineLength, lineSeparator), doEncode);
   }

   public Base32OutputStream(OutputStream ouput, boolean doEncode, int lineLength, byte[] lineSeparator, CodecPolicy decodingPolicy) {
      super(ouput, new Base32(lineLength, lineSeparator, false, (byte)61, decodingPolicy), doEncode);
   }
}
