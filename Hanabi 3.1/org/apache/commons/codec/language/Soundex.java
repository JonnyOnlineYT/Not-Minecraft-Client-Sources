package org.apache.commons.codec.language;

import org.apache.commons.codec.EncoderException;
import org.apache.commons.codec.StringEncoder;

public class Soundex implements StringEncoder {
   public static final char SILENT_MARKER = '-';
   public static final String US_ENGLISH_MAPPING_STRING = "01230120022455012623010202";
   private static final char[] US_ENGLISH_MAPPING = "01230120022455012623010202".toCharArray();
   public static final Soundex US_ENGLISH = new Soundex();
   public static final Soundex US_ENGLISH_SIMPLIFIED = new Soundex("01230120022455012623010202", false);
   public static final Soundex US_ENGLISH_GENEALOGY = new Soundex("-123-12--22455-12623-1-2-2");
   /** @deprecated */
   @Deprecated
   private int maxLength = 4;
   private final char[] soundexMapping;
   private final boolean specialCaseHW;

   public Soundex() {
      this.soundexMapping = US_ENGLISH_MAPPING;
      this.specialCaseHW = true;
   }

   public Soundex(char[] mapping) {
      this.soundexMapping = new char[mapping.length];
      System.arraycopy(mapping, 0, this.soundexMapping, 0, mapping.length);
      this.specialCaseHW = !this.hasMarker(this.soundexMapping);
   }

   private boolean hasMarker(char[] mapping) {
      char[] var2 = mapping;
      int var3 = mapping.length;

      for(int var4 = 0; var4 < var3; ++var4) {
         char ch = var2[var4];
         if (ch == '-') {
            return true;
         }
      }

      return false;
   }

   public Soundex(String mapping) {
      this.soundexMapping = mapping.toCharArray();
      this.specialCaseHW = !this.hasMarker(this.soundexMapping);
   }

   public Soundex(String mapping, boolean specialCaseHW) {
      this.soundexMapping = mapping.toCharArray();
      this.specialCaseHW = specialCaseHW;
   }

   public int difference(String s1, String s2) throws EncoderException {
      return SoundexUtils.difference(this, s1, s2);
   }

   public Object encode(Object obj) throws EncoderException {
      if (!(obj instanceof String)) {
         throw new EncoderException("Parameter supplied to Soundex encode is not of type java.lang.String");
      } else {
         return this.soundex((String)obj);
      }
   }

   public String encode(String str) {
      return this.soundex(str);
   }

   /** @deprecated */
   @Deprecated
   public int getMaxLength() {
      return this.maxLength;
   }

   private char map(char ch) {
      int index = ch - 65;
      if (index >= 0 && index < this.soundexMapping.length) {
         return this.soundexMapping[index];
      } else {
         throw new IllegalArgumentException("The character is not mapped: " + ch + " (index=" + index + ")");
      }
   }

   /** @deprecated */
   @Deprecated
   public void setMaxLength(int maxLength) {
      this.maxLength = maxLength;
   }

   public String soundex(String str) {
      if (str == null) {
         return null;
      } else {
         str = SoundexUtils.clean(str);
         if (str.length() == 0) {
            return str;
         } else {
            char[] out = new char[]{'0', '0', '0', '0'};
            int count = 0;
            char first = str.charAt(0);
            out[count++] = first;
            char lastDigit = this.map(first);

            for(int i = 1; i < str.length() && count < out.length; ++i) {
               char ch = str.charAt(i);
               if (!this.specialCaseHW || ch != 'H' && ch != 'W') {
                  char digit = this.map(ch);
                  if (digit != '-') {
                     if (digit != '0' && digit != lastDigit) {
                        out[count++] = digit;
                     }

                     lastDigit = digit;
                  }
               }
            }

            return new String(out);
         }
      }
   }
}
