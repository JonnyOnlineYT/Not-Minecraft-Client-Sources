package net.minecraft.util;

import io.netty.util.ResourceLeakDetector;
import io.netty.util.ResourceLeakDetector.Level;

public class ChatAllowedCharacters {
   public static final Level NETTY_LEAK_DETECTION;
   public static final char[] ILLEGAL_STRUCTURE_CHARACTERS;
   public static final char[] ILLEGAL_FILE_CHARACTERS;

   public static boolean isAllowedCharacter(char character) {
      return character != 167 && character >= ' ' && character != 127;
   }

   public static String filterAllowedCharacters(String input) {
      StringBuilder stringbuilder = new StringBuilder();
      char[] var2 = input.toCharArray();
      int var3 = var2.length;

      for(int var4 = 0; var4 < var3; ++var4) {
         char c0 = var2[var4];
         if (isAllowedCharacter(c0)) {
            stringbuilder.append(c0);
         }
      }

      return stringbuilder.toString();
   }

   static {
      NETTY_LEAK_DETECTION = Level.DISABLED;
      ILLEGAL_STRUCTURE_CHARACTERS = new char[]{'.', '\n', '\r', '\t', '\u0000', '\f', '`', '?', '*', '\\', '<', '>', '|', '"'};
      ILLEGAL_FILE_CHARACTERS = new char[]{'/', '\n', '\r', '\t', '\u0000', '\f', '`', '?', '*', '\\', '<', '>', '|', '"', ':'};
      ResourceLeakDetector.setLevel(NETTY_LEAK_DETECTION);
   }
}
