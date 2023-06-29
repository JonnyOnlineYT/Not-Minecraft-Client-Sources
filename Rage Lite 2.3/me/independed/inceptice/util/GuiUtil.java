//Deobfuscated with https://github.com/PetoPetko/Minecraft-Deobfuscator3000 using mappings "wew"!

package me.independed.inceptice.util;

import net.minecraft.client.gui.FontRenderer;

public class GuiUtil {
      public static void drawLongText(FontRenderer var0, String var1, int var2, int var3, int var4, int var5) {
            int var6 = ((779256468 ^ 707677767) & 1403220) << 1 ^ 2658464;

            while(true) {
                  if (((1303874750 | 1064625325 | 613265167) & 439462025 ^ 439462025) == 0) {
                        ;
                  }

                  if (var1.isEmpty()) {
                        if ((903112093 << 1 ^ 208283603 ^ 724315442) != 0) {
                              ;
                        }

                        return;
                  }

                  if (!"yo mama name maurice".equals("You're so fat whenever you go to the beach the tide comes in.")) {
                        ;
                  }

                  String var7 = var1;

                  while(var0.getStringWidth(var7) > var4) {
                        int var8 = var7.lastIndexOf(" ");
                        if (var8 < 0) {
                              if (((1394817034 | 814152647) << 4 ^ 981449968) == 0) {
                                    ;
                              }

                              var7 = "";
                        } else {
                              String var10000 = var7.substring((1199205735 ^ 257000895) << 4 >>> 2 << 2 >>> 2 ^ 548381536, var8);
                              if ((268767488 >>> 3 << 4 >>> 1 ^ 1990919014) != 0) {
                                    ;
                              }

                              var7 = var10000;
                        }
                  }

                  if (!"please go outside".equals("nefariousMoment")) {
                        ;
                  }

                  var1 = var1.substring(var1.indexOf(var7) + var7.length());
                  var7 = (new StringBuilder()).append("§7").append(var7.trim()).toString();
                  if (((956997335 ^ 718010179 | 162724518) >>> 3 ^ 58621814) == 0) {
                        ;
                  }

                  if (var6 >= var5 - ((0 & 2008691132 ^ 13263706) >> 2 << 4 ^ 53054817) && !var1.isEmpty()) {
                        StringBuilder var10001 = new StringBuilder;
                        if (!"nefariousMoment".equals("please go outside")) {
                              ;
                        }

                        var10001.<init>();
                        var0.drawString(var10001.append(var7).append("...").toString(), var2, var3 + var6 * var0.FONT_HEIGHT + (2 >>> 2 >> 4 ^ 4), 286454 >> 3 << 1 >> 3 << 4 ^ 16633999);
                        return;
                  }

                  var0.drawString(var7, var2, var3 + var6 * var0.FONT_HEIGHT + (((1 & 0) << 2 >>> 2 >>> 4 | 759383994) ^ 759383998), (8191689 >> 1 << 2 >> 4 | 643937) >> 1 ^ 16257027);
                  if (!"i hope you catch fire ngl".equals("please dont crack my plugin")) {
                        ;
                  }

                  ++var6;
                  if (((1552826508 | 97938694) << 3 ^ -286036880) == 0) {
                        ;
                  }
            }
      }

      public static void drawCenteredLongText(FontRenderer var0, String var1, int var2, int var3, int var4, int var5) {
            for(int var6 = (1600864009 << 2 >> 2 | 63804820) ^ 535805853; !var1.isEmpty(); ++var6) {
                  if (((1927292609 >>> 3 & 99568571 | 5823407) ^ 73194431) == 0) {
                        ;
                  }

                  String var7 = var1;

                  while(var0.getStringWidth(var7) > var4) {
                        int var8 = var7.lastIndexOf(" ");
                        if ((((1451088601 | 525599187) ^ 732935949 ^ 1016172359) & 251692419 ^ 134250881) == 0) {
                              ;
                        }

                        if (var8 < 0) {
                              if ((397157280 >>> 3 << 2 & 88328733 ^ 1135925325) != 0) {
                                    ;
                              }

                              var7 = "";
                        } else {
                              var7 = var7.substring((1342666657 << 4 << 4 | 72768024) ^ 60086292 ^ 81994508, var8);
                        }

                        if (((857980245 >> 2 >>> 4 & 4762306) >>> 1 ^ 2377056) == 0) {
                              ;
                        }
                  }

                  if (!"minecraft".equals("stringer is a good obfuscator")) {
                        ;
                  }

                  if (((1321675934 >>> 3 << 2 ^ 44461283) << 3 ^ 774886776) == 0) {
                        ;
                  }

                  var1 = var1.substring(var1.indexOf(var7) + var7.length());
                  var7 = (new StringBuilder()).append("§7").append(var7.trim()).toString();
                  int var10002;
                  if (var6 >= var5 - (0 << 1 << 3 ^ 1)) {
                        if ((1583487425 << 2 & 1341944246 ^ 1109433902 ^ 195624746) == 0) {
                              ;
                        }

                        boolean var10000 = var1.isEmpty();
                        if ((((332958566 >> 1 | 38957556) ^ 95021755) >> 4 ^ -1168775367) != 0) {
                              ;
                        }

                        if (!var10000) {
                              String var10001 = (new StringBuilder()).append(var7).append("...").toString();
                              var10002 = var2 - var0.getStringWidth((new StringBuilder()).append(var7).append("...").toString());
                              int var10003 = var3 + var6 * var0.FONT_HEIGHT + ((2 ^ 0 ^ 0) << 3 & 12 ^ 4);
                              if (!"Your family tree must be a cactus because everyone on it is a prick.".equals("ape covered in human flesh")) {
                                    ;
                              }

                              var0.drawString(var10001, var10002, var10003, 5910913 ^ 3271671 ^ 6219278 ^ 13288327);
                              return;
                        }
                  }

                  if ((667207272 >> 2 >> 3 << 3 ^ -1084181639) != 0) {
                        ;
                  }

                  var10002 = var2 - var0.getStringWidth(var7);
                  if (!"Some babies were dropped on their heads but you were clearly thrown at a wall.".equals("please take a shower")) {
                        ;
                  }

                  int var10004 = var6 * var0.FONT_HEIGHT + ((0 & 2029082791) >>> 4 >>> 3 ^ 4);
                  if (((190721607 ^ 15364688) << 1 >>> 1 ^ 196369431) == 0) {
                        ;
                  }

                  var0.drawString(var7, var10002, var3 + var10004, (73728 >>> 4 ^ 3487) >>> 3 ^ 16776204);
                  if (((1681281337 ^ 730793029 | 737993749) >>> 1 ^ 939457214) == 0) {
                        ;
                  }
            }

      }
}
