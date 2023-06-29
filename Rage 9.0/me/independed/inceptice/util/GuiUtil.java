//Deobfuscated with https://github.com/PetoPetko/Minecraft-Deobfuscator3000 using mappings "wew"!

package me.independed.inceptice.util;

import net.minecraft.client.gui.FontRenderer;

public class GuiUtil {
      public static void drawCenteredLongText(FontRenderer var0, String var1, int var2, int var3, int var4, int var5) {
            for(int var6 = (278503444 | 244689413) & 122389165 & 80108228 ^ 67109380; !var1.isEmpty(); ++var6) {
                  String var7 = var1;

                  while(true) {
                        if (((335814900 >>> 4 & 5610891) << 4 << 2 ^ 268436160) == 0) {
                              ;
                        }

                        if (var0.getStringWidth(var7) <= var4) {
                              if ((1071465276 >>> 3 << 3 >>> 3 >>> 3 ^ 16741644) == 0) {
                                    ;
                              }

                              var1 = var1.substring(var1.indexOf(var7) + var7.length());
                              var7 = (new StringBuilder()).append("§7").append(var7.trim()).toString();
                              if (var6 >= var5 - (((0 >> 3 & 878709349) >>> 3 | 425606182) ^ 425606183) && !var1.isEmpty()) {
                                    String var10001 = (new StringBuilder()).append(var7).append("...").toString();
                                    StringBuilder var10004 = new StringBuilder;
                                    if (!"shitted on you harder than archybot".equals("you probably spell youre as your")) {
                                          ;
                                    }

                                    var10004.<init>();
                                    var0.drawString(var10001, var2 - var0.getStringWidth(var10004.append(var7).append("...").toString()), var3 + var6 * var0.FONT_HEIGHT + ((3 >> 4 & 1355723698) >>> 4 ^ 1165178761 ^ 1165178765), (9634355 >> 3 | 288078) << 3 & 6592137 ^ 14679551);
                                    return;
                              }

                              if (((180245126 >>> 2 | 19464726) ^ 61839031) == 0) {
                                    ;
                              }

                              var0.drawString(var7, var2 - var0.getStringWidth(var7), var3 + var6 * var0.FONT_HEIGHT + ((1 | 0) >> 2 >>> 3 ^ 4), (362514 >>> 4 >> 4 & 1169 ^ 808) >> 1 ^ 16776235);
                              break;
                        }

                        int var8 = var7.lastIndexOf(" ");
                        if (var8 < 0) {
                              var7 = "";
                              if (((146803728 ^ 130472371) >> 4 ^ 1946960826) != 0) {
                                    ;
                              }
                        } else {
                              var7 = var7.substring((308377789 << 2 >> 3 | 89221498) ^ 225574270, var8);
                        }

                        if ((((603734899 ^ 509601332 | 961438822) & 360486743 | 79375732) ^ 369077623) == 0) {
                              ;
                        }
                  }
            }

      }

      public static void drawLongText(FontRenderer var0, String var1, int var2, int var3, int var4, int var5) {
            int var6 = 402677826 ^ 395570902 ^ 261328532;

            while(true) {
                  if (!"yo mama name maurice".equals("intentMoment")) {
                        ;
                  }

                  if (var1.isEmpty()) {
                        return;
                  }

                  String var7 = var1;

                  while(true) {
                        if ((800674810 << 3 ^ 4075549 ^ 8298330) != 0) {
                              ;
                        }

                        if (var0.getStringWidth(var7) <= var4) {
                              int var10001 = var1.indexOf(var7);
                              int var10002 = var7.length();
                              if (((498678270 >>> 3 | 21048819) >> 4 << 2 ^ -2044652748) != 0) {
                                    ;
                              }

                              var1 = var1.substring(var10001 + var10002);
                              StringBuilder var10000 = (new StringBuilder()).append("§7").append(var7.trim());
                              if ((((209325194 | 133836649) >>> 1 | 73966092) << 3 ^ 1072545768) == 0) {
                                    ;
                              }

                              var7 = var10000.toString();
                              if (var6 >= var5 - ((0 & 636374036 & 580723191) << 1 >> 1 ^ 1) && !var1.isEmpty()) {
                                    if ((414985849 >>> 3 << 3 << 3 ^ -484547265) != 0) {
                                          ;
                                    }

                                    String var9 = (new StringBuilder()).append(var7).append("...").toString();
                                    if (((136314996 >>> 2 | 29794162) >>> 2 ^ -1312906866) != 0) {
                                          ;
                                    }

                                    var0.drawString(var9, var2, var3 + var6 * var0.FONT_HEIGHT + ((1 << 3 ^ 0) >>> 3 ^ 5), ((11955362 >>> 1 | 3000153) ^ 4130007) >> 4 ^ 16511175);
                                    return;
                              }

                              if ((1411823963 >>> 3 & 157315086 ^ 83791788 ^ 218029990) == 0) {
                                    ;
                              }

                              int var10004 = var6 * var0.FONT_HEIGHT + ((3 >>> 1 >> 1 ^ 563086982 | 560396032) ^ 569833346);
                              if ((540060696 ^ 12708356 ^ 552683036) == 0) {
                                    ;
                              }

                              var0.drawString(var7, var2, var3 + var10004, (7358779 ^ 1599424) >>> 4 ^ 16350480);
                              ++var6;
                              break;
                        }

                        int var8 = var7.lastIndexOf(" ");
                        if (var8 < 0) {
                              if (((1156601726 >>> 3 | 36919563 | 69169772) >>> 4 ^ -408074322) != 0) {
                                    ;
                              }

                              var7 = "";
                        } else {
                              var7 = var7.substring((2126232319 | 70083228) >>> 4 ^ 124100859 ^ 9330516, var8);
                        }
                  }
            }
      }
}
