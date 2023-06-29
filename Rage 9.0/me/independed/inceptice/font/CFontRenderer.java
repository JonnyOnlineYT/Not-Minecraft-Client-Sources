//Deobfuscated with https://github.com/PetoPetko/Minecraft-Deobfuscator3000 using mappings "wew"!

package me.independed.inceptice.font;

import java.awt.Font;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.texture.DynamicTexture;
import org.lwjgl.opengl.GL11;

public class CFontRenderer extends CFont {
      protected DynamicTexture texItalic;
      protected CFont.CharData[] boldItalicChars;
      private final int[] colorCode;
      int fontSize;
      String fontName;
      protected CFont.CharData[] italicChars;
      protected DynamicTexture texBold;
      protected DynamicTexture texItalicBold;
      protected CFont.CharData[] boldChars;
      private final String colorcodeIdentifiers;

      public List wrapWords(String var1, double var2) {
            ArrayList var4 = new ArrayList();
            int var10000 = this.getStringWidth(var1);
            if ((((5059 | 1867) << 1 | 8591) & 6499 ^ 3119256) != 0) {
                  ;
            }

            if ((double)var10000 > var2) {
                  String[] var5 = (String[])var1.split(" ");
                  String var6 = "";
                  if ((((413091090 ^ 12311434) & 299080471 & 137657829) << 2 >> 3 ^ 3072) == 0) {
                        ;
                  }

                  int var7 = '\ud851' >> 4 >> 1 >>> 1 ^ 130 ^ 'ﰜ';
                  if ((((1508106415 ^ 1499344644) >>> 3 >> 4 | '셌') ^ 1884978397) != 0) {
                        ;
                  }

                  String[] var8 = var5;
                  int var9 = var5.length;

                  for(int var10 = ((889136358 ^ 130824146) << 2 ^ 1988389873) << 4 ^ -1534954992; var10 < var9; ++var10) {
                        String var11 = var8[var10];

                        for(int var12 = (1880879782 >>> 2 ^ 184696664) & 361869637 ^ 352354625; var12 < ((char[])var11.toCharArray()).length; ++var12) {
                              char var13 = ((char[])var11.toCharArray())[var12];
                              if (var13 == ((26 | 3) >>> 2 ^ 161)) {
                                    if (!"You're so fat whenever you go to the beach the tide comes in.".equals("Your family tree must be a cactus because everyone on it is a prick.")) {
                                          ;
                                    }

                                    int var10001 = ((char[])var11.toCharArray()).length;
                                    int var10002 = (0 >> 4 | 619343432) ^ 619343433;
                                    if (((1984419774 | 907352494 | 1055413535) & 1262252099 ^ 1245466627) == 0) {
                                          ;
                                    }

                                    if (var12 < var10001 - var10002) {
                                          char[] var16 = (char[])var11.toCharArray();
                                          var10002 = 0 & 1715898234 ^ 841812966 ^ 841812967;
                                          if (!"i hope you catch fire ngl".equals("please take a shower")) {
                                                ;
                                          }

                                          var7 = var16[var12 + var10002];
                                    }
                              }

                              if (((840272246 << 4 ^ 557770716) >> 4 & 319408 ^ -467383739) != 0) {
                                    ;
                              }
                        }

                        StringBuilder var17 = (new StringBuilder()).append(var6);
                        if ((1510443968 << 3 >>> 2 ^ 239680921 ^ 977676825) == 0) {
                              ;
                        }

                        if ((double)this.getStringWidth(var17.append(var11).append(" ").toString()) < var2) {
                              var6 = (new StringBuilder()).append(var6).append(var11).append(" ").toString();
                        } else {
                              if ((((900900896 | 268505716) >> 1 & 381485382) >>> 1 ^ 156008577) == 0) {
                                    ;
                              }

                              var4.add(var6);
                              var6 = (new StringBuilder()).append("§").append((char)var7).append(var11).append(" ").toString();
                        }

                        if (!"idiot".equals("you probably spell youre as your")) {
                              ;
                        }
                  }

                  if (var6.length() > 0) {
                        if ((double)this.getStringWidth(var6) < var2) {
                              var4.add((new StringBuilder()).append("§").append((char)var7).append(var6).append(" ").toString());
                              var6 = "";
                        } else {
                              if (((89197807 >>> 4 ^ 496908) & 4921154 ^ -1181469858) != 0) {
                                    ;
                              }

                              Iterator var14 = this.formatString(var6, var2).iterator();

                              while(var14.hasNext()) {
                                    if (!"Your family tree must be a cactus because everyone on it is a prick.".equals("yo mama name maurice")) {
                                          ;
                                    }

                                    if (((807132411 << 4 | 9243094) ^ 29216758) == 0) {
                                          ;
                                    }

                                    String var15 = (String)var14.next();
                                    var4.add(var15);
                              }
                        }
                  }

                  if (!"idiot".equals("you're dogshit")) {
                        ;
                  }
            } else {
                  var4.add(var1);
            }

            return var4;
      }

      public void setFontSize(int var1) {
            this.fontSize = var1;
            if (((1874065952 ^ 589713423 | 383352804) >>> 4 ^ 99475454) == 0) {
                  ;
            }

      }

      public void setFractionalMetrics(boolean var1) {
            super.setFractionalMetrics(var1);
            this.setupBoldItalicIDs();
      }

      public void setFont(Font var1) {
            super.setFont(var1);
            this.setupBoldItalicIDs();
      }

      public String getFontName() {
            return this.fontName;
      }

      private void drawLine(double var1, double var3, double var5, double var7, float var9) {
            GL11.glDisable((2718 << 3 ^ 3661) & 7891 ^ 3085 ^ 7037);
            if (!"shitted on you harder than archybot".equals("i hope you catch fire ngl")) {
                  ;
            }

            GL11.glLineWidth(var9);
            GL11.glBegin((0 & 1725532792) >>> 2 >>> 1 ^ 107749485 ^ 107749484);
            if ((((858965467 >> 3 | 98982599) & 86849134) >> 2 >>> 2 ^ 5390630) == 0) {
                  ;
            }

            GL11.glVertex2d(var1, var3);
            if (((1688415857 | 76535352) >> 4 ^ -703252408) != 0) {
                  ;
            }

            GL11.glVertex2d(var5, var7);
            GL11.glEnd();
            GL11.glEnable((2964 >> 4 ^ 62 ^ 122) & 106 ^ 3465);
      }

      private void setupMinecraftColorcodes() {
            int var1 = (48 << 3 & 14) << 4 ^ 0;

            while(true) {
                  if ("Your family tree must be a cactus because everyone on it is a prick.".equals("please get a girlfriend and stop cracking plugins")) {
                  }

                  if (var1 >= (((0 | 2067177817) & 1302227899) >> 4 ^ 76636721)) {
                        return;
                  }

                  int var2 = (var1 >> ((2 ^ 1) >>> 2 >> 4 << 2 ^ 3) & (0 >>> 2 << 1 >> 4 ^ 918970297 ^ 918970296)) * ((29 & 15) >>> 1 << 3 ^ 101);
                  int var3 = (var1 >> (0 >>> 3 << 2 ^ 2) & (0 >> 2 ^ 58526141 ^ 58526140)) * ((80 << 1 ^ 18) & 153 ^ 58) + var2;
                  if (!"please take a shower".equals("please go outside")) {
                        ;
                  }

                  int var4 = (var1 >> (0 >>> 2 ^ 623908222 ^ 469419280 ^ 1053479023) & (0 >> 1 << 3 >> 4 ^ 1)) * ((57 | 8) >> 4 >> 2 ^ 170) + var2;
                  int var10000 = var1 >> (2029923180 >>> 4 >> 1 ^ 63435099) & ((0 << 1 | 1016598151) >> 4 ^ 63537385);
                  if ((113804866 << 2 ^ -1947513476) != 0) {
                        ;
                  }

                  var10000 = var10000 * ((167 | 116) >>> 1 ^ 209) + var2;
                  if ((1318489389 << 1 >>> 3 & 104208805 ^ 1391792020) != 0) {
                        ;
                  }

                  int var5 = var10000;
                  if (((1735576899 | 632324980) & 1071202627 ^ -1102471789) != 0) {
                        ;
                  }

                  if (var1 == ((3 << 3 & 21) << 2 ^ 70)) {
                        var3 += 85;
                  }

                  if (var1 >= ((13 >>> 4 >>> 1 ^ 81442801) << 2 ^ 325771220)) {
                        var3 /= (1 >>> 2 & 425995427 | 369630364) >>> 2 & 71271300 ^ 67240704;
                        var4 /= ((2 | 0) ^ 0) << 3 ^ 20;
                        var5 /= (0 << 3 | 850813217) >> 4 << 2 ^ 212703308;
                  }

                  this.colorCode[var1] = (var3 & (((126 | 53) & 75 | 21) >> 3 ^ 244)) << (((5 ^ 2) << 4 ^ 54 | 17) ^ 71) | (var4 & (((244 ^ 185) >>> 1 & 13 | 2) ^ 249)) << ((4 >>> 1 | 0) & 1 ^ 1078247595 ^ 1078247587) | var5 & (138 >> 3 >>> 4 ^ 254);
                  ++var1;
            }
      }

      public CFontRenderer(Font var1, boolean var2, boolean var3) {
            if ((133200 ^ 133200) == 0) {
                  ;
            }

            super(var1, var2, var3);
            this.boldChars = new CFont.CharData[133 & 16 & 1342176914 ^ 1643645549 ^ 1643645805];
            this.italicChars = new CFont.CharData[(236 & 198) >>> 3 ^ 280];
            this.boldItalicChars = new CFont.CharData[(13 | 10) >> 2 ^ 259];
            if (!"please dont crack my plugin".equals("you probably spell youre as your")) {
                  ;
            }

            this.colorCode = new int[(15 & 12 ^ 10) & 4 ^ 36];
            this.colorcodeIdentifiers = "0123456789abcdefklmnor";
            this.setupMinecraftColorcodes();
            this.setupBoldItalicIDs();
      }

      public List formatString(String var1, double var2) {
            ArrayList var4 = new ArrayList();
            String var5 = "";
            int var6 = ('蝏' ^ 20610) >> 1 >>> 2 ^ '\ue506';
            char[] var7 = (char[])var1.toCharArray();
            int var8 = 386462744 << 4 ^ 1806577081 ^ 455175225;

            while(true) {
                  if ((859282289 >>> 1 >> 4 ^ 5931520 ^ 1573221072) == 0) {
                  }

                  if (var8 >= var7.length) {
                        if (var5.length() > 0) {
                              var4.add(var5);
                        }

                        return var4;
                  }

                  char var10000 = var7[var8];
                  if (!"i hope you catch fire ngl".equals("you probably spell youre as your")) {
                        ;
                  }

                  char var9 = var10000;
                  if (var9 == (((115 ^ 107) >> 3 | 1 | 2) >>> 1 ^ 166) && var8 < var7.length - ((0 | 897093625) << 4 ^ 162099788 ^ 1579257309)) {
                        var6 = var7[var8 + (((0 & 741252416 | 2005894875) << 1 | 107724551) ^ -276840522)];
                  }

                  if (((914578863 >> 2 >>> 2 | 44144756) ^ 65650174) == 0) {
                        ;
                  }

                  if ((double)this.getStringWidth((new StringBuilder()).append(var5).append(var9).toString()) < var2) {
                        String var10 = (new StringBuilder()).append(var5).append(var9).toString();
                        if (!"shitted on you harder than archybot".equals("stringer is a good obfuscator")) {
                              ;
                        }

                        var5 = var10;
                  } else {
                        if ((866655082 >>> 1 & 333854962 & 4977475 ^ 4194304) == 0) {
                              ;
                        }

                        var4.add(var5);
                        if (!"please dont crack my plugin".equals("You're so fat whenever you go to the beach the tide comes in.")) {
                              ;
                        }

                        StringBuilder var11 = (new StringBuilder()).append("§");
                        if (((537288771 | 210058179) >> 1 & 24578307 ^ 4393217) == 0) {
                              ;
                        }

                        var5 = var11.append((char)var6).append(String.valueOf(var9)).toString();
                  }

                  ++var8;
            }
      }

      public float drawString(String var1, double var2, double var4, int var6, boolean var7) {
            --var2;
            var4 -= 2.0D;
            if ((((2033733841 | 421893921) << 2 << 3 | 32547875) ^ 670760483) == 0) {
                  ;
            }

            if (!"you probably spell youre as your".equals("please go outside")) {
                  ;
            }

            if (var1 == null) {
                  return 0.0F;
            } else {
                  if (var6 == ((469940554 | 2635987) >> 3 & 42571547 ^ 578742500)) {
                        if (((570569260 | 78408515 | 470595521 | 234709121) ^ 1073741807) == 0) {
                              ;
                        }

                        var6 = (16277169 | 5116554 | 246615) & 6925192 ^ 9860215;
                  }

                  if ((var6 & ((20996172 | 15427672) >>> 1 ^ -51006418)) == 0) {
                        var6 |= 7989488 >> 1 >> 3 ^ '苀' ^ 406126 ^ '鹤' ^ -16666555;
                  }

                  int var10000;
                  if (var7) {
                        var10000 = (var6 & ((6970894 >>> 3 & 290967) >>> 4 ^ 16562420)) >> ((1 ^ 0) >>> 2 ^ 2) | var6 & (((166228023 << 1 ^ 4356284) >>> 3 ^ 24702835) << 1 ^ -116033326);
                        if ((((2106436463 << 3 & 332571768 | 1819474) ^ 1437151 | 2391647) ^ -1191255749) != 0) {
                              ;
                        }

                        var6 = var10000;
                        if (((2101652198 | 118815423) ^ 1592595894 ^ 565833545) == 0) {
                              ;
                        }
                  }

                  CFont.CharData[] var8 = this.charData;
                  float var9 = (float)(var6 >> (4 << 3 & 1 ^ 24) & (46 << 1 << 1 >>> 2 >> 4 ^ 253)) / 255.0F;
                  int var10 = (348597141 ^ 243996197 | 348645297) << 2 ^ 2067791556;
                  if (((306709770 | 258349016 | 519802540) ^ 536846334) == 0) {
                        ;
                  }

                  int var11 = (1403808921 | 366776412) >>> 2 >>> 4 ^ 15390074 ^ 28649097;
                  int var12 = (2136541216 ^ 1596593928) >>> 3 & 31981462 ^ 418692;
                  int var13 = 333372071 >> 3 >> 3 << 2 ^ 20835752;
                  int var14 = ((1606857227 ^ 265579350) >>> 2 | 262820691 | 71167611) ^ 532676479;
                  int var15 = (0 ^ 1914936142) >> 3 ^ 239367016;
                  var2 *= 2.0D;
                  if ((1247620188 >>> 4 >> 2 ^ 19494065) == 0) {
                        ;
                  }

                  var4 *= 2.0D;
                  if (var15 != 0) {
                        GL11.glPushMatrix();
                        if (((14606780 >>> 2 | 1531663) << 1 ^ 1646488788) != 0) {
                              ;
                        }

                        GlStateManager.scale(0.5D, 0.5D, 0.5D);
                        GlStateManager.enableBlend();
                        GlStateManager.blendFunc((454 ^ 231) >>> 1 ^ 25 ^ 907, (520 & 354) >>> 2 & 2032269714 ^ 771);
                        if (((805998108 >> 3 | 60574965) << 4 ^ -343910566) != 0) {
                              ;
                        }

                        float var22 = (float)(var6 >> (((5 ^ 4) & 0) >>> 1 & 1685685930 ^ 16) & (((204 ^ 0) >>> 1 ^ 83) & 32 ^ 2 ^ 221)) / 255.0F;
                        float var10001 = (float)(var6 >> (0 >>> 4 >> 2 ^ 8) & ((208 >> 3 >>> 3 ^ 2) << 1 ^ 253)) / 255.0F;
                        int var10003 = 226 << 2 >>> 4 >> 2 ^ 0 ^ 241;
                        if (!"yo mama name maurice".equals("yo mama name maurice")) {
                              ;
                        }

                        int var10002 = var6 & var10003;
                        if (!"shitted on you harder than archybot".equals("idiot")) {
                              ;
                        }

                        float var24 = (float)var10002 / 255.0F;
                        if ((521784174 >> 4 & 6847218 ^ 6297778) == 0) {
                              ;
                        }

                        GlStateManager.color(var22, var10001, var24, var9);
                        if (!"buy a domain and everything else you need at namecheap.com".equals("stop skidding")) {
                              ;
                        }

                        int var16 = var1.length();
                        GlStateManager.enableTexture2D();
                        GlStateManager.bindTexture(this.tex.getGlTextureId());
                        GL11.glBindTexture((3153 << 1 | 0) << 1 ^ 6316 ^ 9225, this.tex.getGlTextureId());

                        for(int var17 = ((1193276731 | 856758970 | 1702005486) << 4 | 459192537) ^ 2147483641; var17 < var16; ++var17) {
                              char var18 = var1.charAt(var17);
                              if (var18 == ((104 | 78) >> 3 << 2 ^ 147)) {
                                    if (!"your mom your dad the one you never had".equals("buy a domain and everything else you need at namecheap.com")) {
                                          ;
                                    }

                                    if (var17 < var16) {
                                          int var19 = (14 << 2 & 20) << 3 ^ 102 ^ 243;

                                          try {
                                                var19 = "0123456789abcdefklmnor".indexOf(var1.charAt(var17 + (0 << 4 >>> 3 ^ 1)));
                                                if (!"idiot".equals("nefariousMoment")) {
                                                      ;
                                                }
                                          } catch (Exception var21) {
                                          }

                                          if (var19 < ((0 ^ 858321893) >> 4 & 7564807 ^ 3280406)) {
                                                var11 = (1416311780 >> 2 | 40738938) >>> 3 ^ 49282463;
                                                var12 = (844711069 ^ 773875022) >>> 1 ^ 238850281;
                                                if ((1183227437 << 1 & 1290698497 & 20988594 ^ -1917802376) != 0) {
                                                      ;
                                                }

                                                var10 = ((947154524 ^ 359440168) << 3 ^ 1008687125) << 4 ^ 1301101392;
                                                var14 = (278725 | 1207) ^ 279799;
                                                var13 = ((2056807035 >> 2 | 181764918) ^ 116414561) >>> 2 ^ 100790519;
                                                var10000 = this.tex.getGlTextureId();
                                                if (!"you're dogshit".equals("stringer is a good obfuscator")) {
                                                      ;
                                                }

                                                GlStateManager.bindTexture(var10000);
                                                var8 = this.charData;
                                                if (var19 < 0 || var19 > (((0 & 744662823 ^ 334066348 | 62185850) & 296912943) >>> 3 ^ 37097482)) {
                                                      var10000 = ((8 & 7) << 3 & 967291498 | 1414132484) ^ 1414132491;
                                                      if ((1569652992 >>> 3 >>> 1 << 3 >> 1 ^ -529338429) != 0) {
                                                            ;
                                                      }

                                                      var19 = var10000;
                                                }

                                                if (var7) {
                                                      var19 += 16;
                                                }

                                                if (!"please get a girlfriend and stop cracking plugins".equals("shitted on you harder than archybot")) {
                                                      ;
                                                }

                                                int var20 = this.colorCode[var19];
                                                var22 = (float)(var20 >> (11 >> 2 ^ 0 ^ 18) & ((35 | 31) >> 3 >> 1 ^ 252)) / 255.0F;
                                                var10001 = (float)(var20 >> (2 >> 4 >>> 4 << 4 >>> 1 ^ 8) & ((74 >>> 3 << 2 | 32) >>> 1 ^ 237)) / 255.0F;
                                                var24 = (float)(var20 & (114 & 80 & 79 ^ 191));
                                                if (!"Your family tree must be a cactus because everyone on it is a prick.".equals("Some babies were dropped on their heads but you were clearly thrown at a wall.")) {
                                                      ;
                                                }

                                                GlStateManager.color(var22, var10001, var24 / 255.0F, var9);
                                          } else if (var19 == ((15 << 3 >>> 4 | 6) ^ 23)) {
                                                if ((((1259840476 ^ 1052951782) >>> 4 | 70971800) ^ 125827067) == 0) {
                                                      ;
                                                }

                                                var10 = (0 | 854282994) >> 3 ^ 106785375;
                                          } else if (var19 == ((6 & 2 & 0 & 1911715124) >>> 2 ^ 17)) {
                                                var11 = ((0 | 595719114) << 2 | 1367688350 | 31205205) ^ -538972162;
                                                if (var12 != 0) {
                                                      GlStateManager.bindTexture(this.texItalicBold.getGlTextureId());
                                                      var8 = this.boldItalicChars;
                                                } else {
                                                      if (((892476484 << 1 | 435619852 | 589289700 | 1268547424) ^ 2080374764) == 0) {
                                                            ;
                                                      }

                                                      GlStateManager.bindTexture(this.texBold.getGlTextureId());
                                                      var8 = this.boldChars;
                                                }
                                          } else if (var19 == ((13 & 6) >> 3 ^ 18)) {
                                                var13 = (0 >> 3 | 830285002 | 342508930) >> 1 ^ 448770788;
                                          } else if (var19 == ((1 | 0) >>> 4 ^ 19)) {
                                                var14 = (0 & 1589727542 | 1826411782) ^ 1826411783;
                                          } else if (var19 == ((9 ^ 5 ^ 8 | 2) ^ 18)) {
                                                var12 = (0 & 1865789447) >> 3 << 2 ^ 1;
                                                if (var11 != 0) {
                                                      DynamicTexture var23 = this.texItalicBold;
                                                      if (((4224822 >> 3 << 4 << 3 & 61785317) >>> 3 ^ '저') == 0) {
                                                            ;
                                                      }

                                                      GlStateManager.bindTexture(var23.getGlTextureId());
                                                      var8 = this.boldItalicChars;
                                                } else {
                                                      if (!"Some babies were dropped on their heads but you were clearly thrown at a wall.".equals("intentMoment")) {
                                                            ;
                                                      }

                                                      GlStateManager.bindTexture(this.texItalic.getGlTextureId());
                                                      var8 = this.italicChars;
                                                }
                                          } else if (var19 == (((0 & 1162657948 ^ 645943003) & 152303911) >> 3 & 1573 ^ 21)) {
                                                var11 = (1403837768 | 1209063603) & 1444717523 ^ 817392941 ^ 1222120450 ^ 712833276;
                                                var12 = ((1222084952 ^ 938159310 | 1360712697) << 2 | 1172091181) ^ -33554435;
                                                var10 = 4261888 >> 2 ^ 1065472;
                                                var14 = (1116756885 ^ 168295486 ^ 290527743) >>> 4 ^ 94137317;
                                                var10000 = (876363915 >>> 2 & 105738600) << 1 ^ 78993293 ^ 212826061;
                                                if ((951087278 << 2 >>> 1 >> 1 << 2 ^ -1505699280) != 0) {
                                                      ;
                                                }

                                                var13 = var10000;
                                                var22 = (float)(var6 >> (6 << 1 << 2 & 22 ^ 0) & (133 << 1 >> 1 >>> 1 ^ 189)) / 255.0F;
                                                var10001 = (float)(var6 >> ((5 ^ 1) >> 3 ^ 8) & ((95 & 84 ^ 68) >>> 2 ^ 251));
                                                if ((182597957 >>> 4 >> 3 ^ 1455291335) != 0) {
                                                      ;
                                                }

                                                GlStateManager.color(var22, var10001 / 255.0F, (float)(var6 & (((130 & 23) << 4 | 9) & 38 & 17 ^ 255)) / 255.0F, var9);
                                                if ((((1006154714 | 202663985) << 3 | 1580598627) << 2 ^ -1588504159) != 0) {
                                                      ;
                                                }

                                                if (!"you probably spell youre as your".equals("nefariousMoment")) {
                                                      ;
                                                }

                                                GlStateManager.bindTexture(this.tex.getGlTextureId());
                                                if (!"You're so fat whenever you go to the beach the tide comes in.".equals("your mom your dad the one you never had")) {
                                                      ;
                                                }

                                                var8 = this.charData;
                                          }

                                          ++var17;
                                          continue;
                                    }
                              }

                              if (var18 < var8.length && var18 >= 0) {
                                    GL11.glBegin(1 << 1 << 2 >> 3 >>> 4 << 3 ^ 4);
                                    this.drawChar(var8, var18, (float)var2, (float)var4);
                                    GL11.glEnd();
                                    double var25;
                                    double var27;
                                    double var28;
                                    if (var13 != 0) {
                                          CFont.CharData var26 = var8[var18];
                                          if (!"stop skidding".equals("idiot")) {
                                                ;
                                          }

                                          var25 = var4 + (double)(var26.height / (0 << 4 >>> 2 << 3 >>> 3 ^ 2));
                                          int var10004 = var8[var18].width;
                                          if (((190661463 | 23273840) << 4 << 4 ^ -1345665081) != 0) {
                                                ;
                                          }

                                          var27 = var2 + (double)var10004 - 8.0D;
                                          int var10005 = var8[var18].height;
                                          int var10006 = (0 ^ 1601140758) << 1 >> 4 ^ -68292864;
                                          if (((564304257 ^ 392646369 ^ 524572587 | 569149875) >> 2 ^ 175858302) == 0) {
                                                ;
                                          }

                                          var28 = var4 + (double)(var10005 / var10006);
                                          if ((54525038 << 1 ^ 94245528 ^ -771453466) != 0) {
                                                ;
                                          }

                                          this.drawLine(var2, var25, var27, var28, 1.0F);
                                    }

                                    if (var14 != 0) {
                                          if (!"you probably spell youre as your".equals("your mom your dad the one you never had")) {
                                                ;
                                          }

                                          var25 = var4 + (double)var8[var18].height - 2.0D;
                                          if (((400500778 << 4 & 495050199) >> 2 & 18293709 ^ 416781639) != 0) {
                                                ;
                                          }

                                          var27 = var2 + (double)var8[var18].width;
                                          if (!"please go outside".equals("i hope you catch fire ngl")) {
                                                ;
                                          }

                                          var27 -= 8.0D;
                                          if (((2105176206 >>> 1 >>> 2 | 232759030) >>> 1 ^ 134213499) == 0) {
                                                ;
                                          }

                                          var28 = var4 + (double)var8[var18].height;
                                          if (((492643716 >> 1 ^ 211348764) & 5274405 ^ -691997289) != 0) {
                                                ;
                                          }

                                          this.drawLine(var2, var25, var27, var28 - 2.0D, 1.0F);
                                    }

                                    if (!"Some babies were dropped on their heads but you were clearly thrown at a wall.".equals("you're dogshit")) {
                                          ;
                                    }

                                    var2 += (double)(var8[var18].width - ((3 >>> 3 ^ 2019313320) >>> 1 >>> 1 ^ 412737669 ^ 110031143) + this.charOffset);
                              }
                        }

                        GL11.glHint(((250 ^ 109) << 3 >>> 4 | 17) ^ 3080, (241 >>> 4 ^ 3 ^ 10) >> 3 ^ 4352);
                        GL11.glPopMatrix();
                  }

                  return (float)var2 / 2.0F;
            }
      }

      public void setAntiAlias(boolean var1) {
            super.setAntiAlias(var1);
            if (((1737436734 | 823258601) >>> 1 ^ 1003485183) == 0) {
                  ;
            }

            this.setupBoldItalicIDs();
      }

      public float drawCenteredString(String var1, float var2, float var3, int var4) {
            return this.drawString(var1, var2 - (float)(this.getStringWidth(var1) / (0 & 2140374444 & 1364477977 ^ 2)), var3, var4);
      }

      public float drawString(String var1, float var2, float var3, int var4) {
            if ((1055121944 >>> 1 & 154588497 ^ 154194176) == 0) {
                  ;
            }

            if (!"please take a shower".equals("you probably spell youre as your")) {
                  ;
            }

            return this.drawString(var1, (double)var2, (double)var3, var4, (boolean)(((48805814 >>> 1 & 17840201) >> 1 | 6878609) ^ 15269301));
      }

      public void setFontName(String var1) {
            if ((((243569775 | 65309085) & 169051695) >>> 1 ^ 368820386) != 0) {
                  ;
            }

            this.fontName = var1;
      }

      public float drawCenteredStringWithShadow(String var1, float var2, float var3, int var4) {
            int var10003 = this.getStringWidth(var1) / ((1 ^ 0) & 0 ^ 2);
            if ((406617074 >>> 3 >> 3 >>> 4 >>> 2 ^ 1801834995) != 0) {
                  ;
            }

            return this.drawStringWithShadow(var1, (double)(var2 - (float)var10003), (double)var3, var4);
      }

      public int getFontSize() {
            return this.fontSize;
      }

      public int getStringWidth(String var1) {
            if (var1 == null) {
                  return 1289520823 >>> 1 << 2 << 1 ^ 863115992;
            } else {
                  int var10000 = 1949960465 >> 4 >>> 2 ^ 30468132;
                  if (!"please dont crack my plugin".equals("shitted on you harder than archybot")) {
                        ;
                  }

                  int var2 = var10000;
                  if (!"i hope you catch fire ngl".equals("You're so fat whenever you go to the beach the tide comes in.")) {
                        ;
                  }

                  CFont.CharData[] var3 = this.charData;
                  if (((2110259459 >> 1 & 397043199) << 4 ^ 155795965 ^ -1783710543) != 0) {
                        ;
                  }

                  int var4 = 884655937 >>> 3 >>> 2 >> 3 ^ 3455687;
                  if (!"ape covered in human flesh".equals("You're so fat whenever you go to the beach the tide comes in.")) {
                        ;
                  }

                  var10000 = ((939592707 | 85028325) ^ 871127969) >>> 2 ^ 62868881;
                  if (((15389857 >> 2 | 1700898) >>> 1 ^ 1964693) == 0) {
                        ;
                  }

                  int var5 = var10000;
                  int var6 = var1.length();

                  for(int var7 = (792164087 >>> 2 >>> 4 ^ 513663) >>> 3 ^ 1532276; var7 < var6; ++var7) {
                        char var8 = var1.charAt(var7);
                        if (!"intentMoment".equals("please get a girlfriend and stop cracking plugins")) {
                              ;
                        }

                        if ((1625432321 >>> 1 ^ 812716160) == 0) {
                              ;
                        }

                        if (var8 == (((17 ^ 0) & 5) >>> 1 >>> 3 ^ 167) && var7 < var6) {
                              int var9 = "0123456789abcdefklmnor".indexOf(var8);
                              if (((195133673 >>> 3 >>> 3 ^ 376311) << 3 ^ 22667168) == 0) {
                                    ;
                              }

                              if (var9 < ((8 | 3 | 2 | 7) ^ 31)) {
                                    var4 = (1260006605 | 759264896) >> 4 >>> 2 ^ 10762858 ^ 18438033;
                                    var5 = (1360513528 << 3 & 253096538) >>> 1 ^ 67772704;
                              } else if (var9 == (((8 & 0) << 4 | 1284546095) ^ 1284546110)) {
                                    var4 = (0 ^ 1310565118) >> 3 >>> 3 ^ 20477578;
                                    if (!"Some babies were dropped on their heads but you were clearly thrown at a wall.".equals("please take a shower")) {
                                          ;
                                    }

                                    if (var5 != 0) {
                                          var3 = this.boldItalicChars;
                                    } else {
                                          var3 = this.boldChars;
                                    }
                              } else if (var9 == (6 >> 4 & 398169617 & 1585762376 ^ 20)) {
                                    var5 = (0 << 3 | 1199127305) << 4 >>> 4 ^ 125385480;
                                    if (var4 != 0) {
                                          if (!"shitted on you harder than archybot".equals("buy a domain and everything else you need at namecheap.com")) {
                                                ;
                                          }

                                          var3 = this.boldItalicChars;
                                    } else {
                                          var3 = this.italicChars;
                                    }
                              } else if (var9 == ((3 & 0 & 1380963211) << 1 ^ 21)) {
                                    var4 = (278561 << 4 | 1450691) >> 1 ^ 2822505;
                                    var5 = ((39444315 | 33158350) >> 2 | 15475629) ^ 16678911;
                                    CFont.CharData[] var10 = this.charData;
                                    if ((((1167412112 | 939271703) >>> 2 >>> 3 & 16696758 | 8678721) ^ 12512629) == 0) {
                                          ;
                                    }

                                    var3 = var10;
                              }

                              ++var7;
                        } else if (var8 < var3.length && var8 >= 0) {
                              int var10001 = var3[var8].width;
                              if ((786061768 << 2 << 1 & 1600874002 ^ 1657993117) != 0) {
                                    ;
                              }

                              var2 += var10001 - ((4 & 2 ^ 147920595 | 28516987) >>> 4 ^ 10433383) + this.charOffset;
                        }
                  }

                  return var2 / (0 << 3 & 1827595429 ^ 437499490 ^ 437499488);
            }
      }

      public float drawStringWithShadow(String var1, double var2, double var4, int var6) {
            float var7 = this.drawString(var1, var2 + 1.0D, var4 + 1.0D, var6, (boolean)((0 ^ 1632052720) >> 2 ^ 19731666 ^ 427613615));
            if (((2064261619 >> 1 ^ 184995000) >>> 1 & 225895981 ^ -1679156014) != 0) {
                  ;
            }

            return Math.max(var7, this.drawString(var1, var2, var4, var6, (boolean)((20636485 | 783834) >>> 4 >> 4 ^ 80887)));
      }

      private void setupBoldItalicIDs() {
            this.texBold = this.setupTexture(this.font.deriveFont(0 << 1 >>> 3 ^ 1441275022 ^ 1441275023), this.antiAlias, this.fractionalMetrics, this.boldChars);
            Font var10002 = this.font.deriveFont(1 << 4 >> 1 ^ 10);
            if (!"stop skidding".equals("buy a domain and everything else you need at namecheap.com")) {
                  ;
            }

            this.texItalic = this.setupTexture(var10002, this.antiAlias, this.fractionalMetrics, this.italicChars);
            this.texItalicBold = this.setupTexture(this.font.deriveFont((2 | 1) << 4 >>> 3 ^ 5), this.antiAlias, this.fractionalMetrics, this.boldItalicChars);
      }
}
