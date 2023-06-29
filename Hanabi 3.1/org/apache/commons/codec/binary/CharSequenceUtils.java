package org.apache.commons.codec.binary;

public class CharSequenceUtils {
   static boolean regionMatches(CharSequence cs, boolean ignoreCase, int thisStart, CharSequence substring, int start, int length) {
      if (cs instanceof String && substring instanceof String) {
         return ((String)cs).regionMatches(ignoreCase, thisStart, (String)substring, start, length);
      } else {
         int index1 = thisStart;
         int index2 = start;
         int tmpLen = length;

         while(tmpLen-- > 0) {
            char c1 = cs.charAt(index1++);
            char c2 = substring.charAt(index2++);
            if (c1 != c2) {
               if (!ignoreCase) {
                  return false;
               }

               if (Character.toUpperCase(c1) != Character.toUpperCase(c2) && Character.toLowerCase(c1) != Character.toLowerCase(c2)) {
                  return false;
               }
            }
         }

         return true;
      }
   }
}
