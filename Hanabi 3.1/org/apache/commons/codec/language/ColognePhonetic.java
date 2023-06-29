package org.apache.commons.codec.language;

import java.util.Locale;
import org.apache.commons.codec.EncoderException;
import org.apache.commons.codec.StringEncoder;

public class ColognePhonetic implements StringEncoder {
   private static final char[] AEIJOUY = new char[]{'A', 'E', 'I', 'J', 'O', 'U', 'Y'};
   private static final char[] CSZ = new char[]{'C', 'S', 'Z'};
   private static final char[] FPVW = new char[]{'F', 'P', 'V', 'W'};
   private static final char[] GKQ = new char[]{'G', 'K', 'Q'};
   private static final char[] CKQ = new char[]{'C', 'K', 'Q'};
   private static final char[] AHKLOQRUX = new char[]{'A', 'H', 'K', 'L', 'O', 'Q', 'R', 'U', 'X'};
   private static final char[] SZ = new char[]{'S', 'Z'};
   private static final char[] AHKOQUX = new char[]{'A', 'H', 'K', 'O', 'Q', 'U', 'X'};
   private static final char[] DTX = new char[]{'D', 'T', 'X'};
   private static final char CHAR_IGNORE = '-';

   private static boolean arrayContains(char[] arr, char key) {
      char[] var2 = arr;
      int var3 = arr.length;

      for(int var4 = 0; var4 < var3; ++var4) {
         char element = var2[var4];
         if (element == key) {
            return true;
         }
      }

      return false;
   }

   public String colognePhonetic(String text) {
      if (text == null) {
         return null;
      } else {
         CologneInputBuffer input = new CologneInputBuffer(this.preprocess(text));
         CologneOutputBuffer output = new CologneOutputBuffer(input.length() * 2);
         char lastChar = '-';

         while(true) {
            char nextChar;
            char chr;
            do {
               do {
                  if (input.length() <= 0) {
                     return output.toString();
                  }

                  chr = input.removeNext();
                  if (input.length() > 0) {
                     nextChar = input.getNextChar();
                  } else {
                     nextChar = '-';
                  }
               } while(chr < 'A');
            } while(chr > 'Z');

            if (arrayContains(AEIJOUY, chr)) {
               output.put('0');
            } else if (chr != 'B' && (chr != 'P' || nextChar == 'H')) {
               if ((chr == 'D' || chr == 'T') && !arrayContains(CSZ, nextChar)) {
                  output.put('2');
               } else if (arrayContains(FPVW, chr)) {
                  output.put('3');
               } else if (arrayContains(GKQ, chr)) {
                  output.put('4');
               } else if (chr == 'X' && !arrayContains(CKQ, lastChar)) {
                  output.put('4');
                  output.put('8');
               } else if (chr != 'S' && chr != 'Z') {
                  if (chr == 'C') {
                     if (output.length() == 0) {
                        if (arrayContains(AHKLOQRUX, nextChar)) {
                           output.put('4');
                        } else {
                           output.put('8');
                        }
                     } else if (!arrayContains(SZ, lastChar) && arrayContains(AHKOQUX, nextChar)) {
                        output.put('4');
                     } else {
                        output.put('8');
                     }
                  } else if (arrayContains(DTX, chr)) {
                     output.put('8');
                  } else if (chr == 'R') {
                     output.put('7');
                  } else if (chr == 'L') {
                     output.put('5');
                  } else if (chr != 'M' && chr != 'N') {
                     if (chr == 'H') {
                        output.put('-');
                     }
                  } else {
                     output.put('6');
                  }
               } else {
                  output.put('8');
               }
            } else {
               output.put('1');
            }

            lastChar = chr;
         }
      }
   }

   public Object encode(Object object) throws EncoderException {
      if (!(object instanceof String)) {
         throw new EncoderException("This method's parameter was expected to be of the type " + String.class.getName() + ". But actually it was of the type " + object.getClass().getName() + ".");
      } else {
         return this.encode((String)object);
      }
   }

   public String encode(String text) {
      return this.colognePhonetic(text);
   }

   public boolean isEncodeEqual(String text1, String text2) {
      return this.colognePhonetic(text1).equals(this.colognePhonetic(text2));
   }

   private char[] preprocess(String text) {
      char[] chrs = text.toUpperCase(Locale.GERMAN).toCharArray();

      for(int index = 0; index < chrs.length; ++index) {
         switch (chrs[index]) {
            case 'Ä':
               chrs[index] = 'A';
               break;
            case 'Ö':
               chrs[index] = 'O';
               break;
            case 'Ü':
               chrs[index] = 'U';
         }
      }

      return chrs;
   }

   private class CologneInputBuffer extends CologneBuffer {
      public CologneInputBuffer(char[] data) {
         super(data);
      }

      protected char[] copyData(int start, int length) {
         char[] newData = new char[length];
         System.arraycopy(this.data, this.data.length - this.length + start, newData, 0, length);
         return newData;
      }

      public char getNextChar() {
         return this.data[this.getNextPos()];
      }

      protected int getNextPos() {
         return this.data.length - this.length;
      }

      public char removeNext() {
         char ch = this.getNextChar();
         --this.length;
         return ch;
      }
   }

   private class CologneOutputBuffer extends CologneBuffer {
      private char lastCode = '/';

      public CologneOutputBuffer(int buffSize) {
         super(buffSize);
      }

      public void put(char code) {
         if (code != '-' && this.lastCode != code && (code != '0' || this.length == 0)) {
            this.data[this.length] = code;
            ++this.length;
         }

         this.lastCode = code;
      }

      protected char[] copyData(int start, int length) {
         char[] newData = new char[length];
         System.arraycopy(this.data, start, newData, 0, length);
         return newData;
      }
   }

   private abstract class CologneBuffer {
      protected final char[] data;
      protected int length = 0;

      public CologneBuffer(char[] data) {
         this.data = data;
         this.length = data.length;
      }

      public CologneBuffer(int buffSize) {
         this.data = new char[buffSize];
         this.length = 0;
      }

      protected abstract char[] copyData(int var1, int var2);

      public int length() {
         return this.length;
      }

      public String toString() {
         return new String(this.copyData(0, this.length));
      }
   }
}
