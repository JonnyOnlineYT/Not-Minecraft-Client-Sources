package org.apache.commons.codec;

import java.util.Comparator;

public class StringEncoderComparator implements Comparator {
   private final StringEncoder stringEncoder;

   /** @deprecated */
   @Deprecated
   public StringEncoderComparator() {
      this.stringEncoder = null;
   }

   public StringEncoderComparator(StringEncoder stringEncoder) {
      this.stringEncoder = stringEncoder;
   }

   public int compare(Object o1, Object o2) {
      int compareCode = false;

      int compareCode;
      try {
         Comparable s1 = (Comparable)this.stringEncoder.encode(o1);
         Comparable s2 = (Comparable)this.stringEncoder.encode(o2);
         compareCode = s1.compareTo(s2);
      } catch (EncoderException var6) {
         compareCode = 0;
      }

      return compareCode;
   }
}
