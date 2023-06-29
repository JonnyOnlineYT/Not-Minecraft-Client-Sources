package net.minecraft.util.text;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import java.util.Collection;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.regex.Pattern;
import javax.annotation.Nullable;

public enum TextFormatting {
   BLACK("BLACK", '0', 0),
   DARK_BLUE("DARK_BLUE", '1', 1),
   DARK_GREEN("DARK_GREEN", '2', 2),
   DARK_AQUA("DARK_AQUA", '3', 3),
   DARK_RED("DARK_RED", '4', 4),
   DARK_PURPLE("DARK_PURPLE", '5', 5),
   GOLD("GOLD", '6', 6),
   GRAY("GRAY", '7', 7),
   DARK_GRAY("DARK_GRAY", '8', 8),
   BLUE("BLUE", '9', 9),
   GREEN("GREEN", 'a', 10),
   AQUA("AQUA", 'b', 11),
   RED("RED", 'c', 12),
   LIGHT_PURPLE("LIGHT_PURPLE", 'd', 13),
   YELLOW("YELLOW", 'e', 14),
   WHITE("WHITE", 'f', 15),
   OBFUSCATED("OBFUSCATED", 'k', true),
   BOLD("BOLD", 'l', true),
   STRIKETHROUGH("STRIKETHROUGH", 'm', true),
   UNDERLINE("UNDERLINE", 'n', true),
   ITALIC("ITALIC", 'o', true),
   RESET("RESET", 'r', -1);

   private static final Map<String, TextFormatting> NAME_MAPPING = Maps.newHashMap();
   private static final Pattern FORMATTING_CODE_PATTERN = Pattern.compile("(?i)§[0-9A-FK-OR]");
   private final String name;
   private final char formattingCode;
   private final boolean fancyStyling;
   private final String controlString;
   private final int colorIndex;

   private static String lowercaseAlpha(String p_175745_0_) {
      return p_175745_0_.toLowerCase(Locale.ROOT).replaceAll("[^a-z]", "");
   }

   private TextFormatting(String formattingName, char formattingCodeIn, int colorIndex) {
      this(formattingName, formattingCodeIn, false, colorIndex);
   }

   private TextFormatting(String formattingName, char formattingCodeIn, boolean fancyStylingIn) {
      this(formattingName, formattingCodeIn, fancyStylingIn, -1);
   }

   private TextFormatting(String formattingName, char formattingCodeIn, boolean fancyStylingIn, int colorIndex) {
      this.name = formattingName;
      this.formattingCode = formattingCodeIn;
      this.fancyStyling = fancyStylingIn;
      this.colorIndex = colorIndex;
      this.controlString = "§" + formattingCodeIn;
   }

   public int getColorIndex() {
      return this.colorIndex;
   }

   public boolean isFancyStyling() {
      return this.fancyStyling;
   }

   public boolean isColor() {
      return !this.fancyStyling && this != RESET;
   }

   public String getFriendlyName() {
      return this.name().toLowerCase(Locale.ROOT);
   }

   public String toString() {
      return this.controlString;
   }

   @Nullable
   public static String getTextWithoutFormattingCodes(@Nullable String text) {
      return text == null ? null : FORMATTING_CODE_PATTERN.matcher(text).replaceAll("");
   }

   @Nullable
   public static TextFormatting getValueByName(@Nullable String friendlyName) {
      return friendlyName == null ? null : (TextFormatting)NAME_MAPPING.get(lowercaseAlpha(friendlyName));
   }

   @Nullable
   public static TextFormatting fromColorIndex(int index) {
      if (index < 0) {
         return RESET;
      } else {
         TextFormatting[] var1 = values();
         int var2 = var1.length;

         for(int var3 = 0; var3 < var2; ++var3) {
            TextFormatting textformatting = var1[var3];
            if (textformatting.getColorIndex() == index) {
               return textformatting;
            }
         }

         return null;
      }
   }

   public static Collection<String> getValidValues(boolean p_96296_0_, boolean p_96296_1_) {
      List<String> list = Lists.newArrayList();
      TextFormatting[] var3 = values();
      int var4 = var3.length;

      for(int var5 = 0; var5 < var4; ++var5) {
         TextFormatting textformatting = var3[var5];
         if ((!textformatting.isColor() || p_96296_0_) && (!textformatting.isFancyStyling() || p_96296_1_)) {
            list.add(textformatting.getFriendlyName());
         }
      }

      return list;
   }

   static {
      TextFormatting[] var0 = values();
      int var1 = var0.length;

      for(int var2 = 0; var2 < var1; ++var2) {
         TextFormatting textformatting = var0[var2];
         NAME_MAPPING.put(lowercaseAlpha(textformatting.name), textformatting);
      }

   }
}
