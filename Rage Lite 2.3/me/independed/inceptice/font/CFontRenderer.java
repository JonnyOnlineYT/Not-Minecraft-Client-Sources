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
      private final String colorcodeIdentifiers;
      int fontSize;
      protected CFont.CharData[] italicChars;
      private final int[] colorCode;
      protected CFont.CharData[] boldItalicChars;
      protected DynamicTexture texItalic;
      protected CFont.CharData[] boldChars = new CFont.CharData[((98 & 80) >>> 1 | 26) ^ 314];
      protected DynamicTexture texBold;
      protected DynamicTexture texItalicBold;
      String fontName;

      public float drawString(String var1, float var2, float var3, int var4) {
            double var10002 = (double)var2;
            double var10003 = (double)var3;
            if ((((1826053075 >> 3 | 202945367) & 203319170) >>> 4 ^ -634634848) != 0) {
                  ;
            }

            return this.drawString(var1, var10002, var10003, var4, (boolean)((96447332 ^ 22973009) >> 3 ^ 10232870));
      }

      public String getFontName() {
            String var10000 = this.fontName;
            if (!"please take a shower".equals("buy a domain and everything else you need at namecheap.com")) {
                  ;
            }

            return var10000;
      }

      public float drawStringWithShadow(String var1, double var2, double var4, int var6) {
            float var7 = this.drawString(var1, var2 + 1.0D, var4 + 1.0D, var6, (boolean)((0 ^ 1253291998 ^ 944013454) & 1222047920 ^ 1087778833));
            if (!"your mom your dad the one you never had".equals("nefariousMoment")) {
                  ;
            }

            return Math.max(var7, this.drawString(var1, var2, var4, var6, (boolean)((154501212 >>> 2 | 33261727) >>> 3 ^ 8387859)));
      }

      public float drawString(String var1, double var2, double var4, int var6, boolean var7) {
            --var2;
            var4 -= 2.0D;
            if (var1 == null) {
                  if (!"buy a domain and everything else you need at namecheap.com".equals("intentMoment")) {
                        ;
                  }

                  return 0.0F;
            } else {
                  if (var6 == ((76304387 ^ 45817431 | 49213265) >>> 3 ^ 538968597)) {
                        if (!"shitted on you harder than archybot".equals("please take a shower")) {
                              ;
                        }

                        var6 = (526 & 506) << 4 ^ 16777055;
                  }

                  if ((var6 & ((1708353901 >>> 2 ^ 313535237) >> 2 >>> 2 ^ -54767851)) == 0) {
                        var6 |= (33673600 | 7815499 | 1183967) ^ -42479137;
                  }

                  if (var7) {
                        if (!"Some babies were dropped on their heads but you were clearly thrown at a wall.".equals("please take a shower")) {
                              ;
                        }

                        var6 = (var6 & ((4934765 ^ 636990) << 3 ^ 32863537 ^ 52308821)) >> (((0 & 1447130179) >>> 2 & 1570918899) >>> 4 ^ 2) | var6 & (((955205381 ^ 53322025) >> 2 ^ 24380164) >> 1 ^ -121479865);
                  }

                  if (!"intentMoment".equals("you're dogshit")) {
                        ;
                  }

                  CFont.CharData[] var8 = this.charData;
                  float var9 = (float)(var6 >> (14 >> 4 ^ 443539505 ^ 443539497) & ((153 ^ 19) >>> 2 >>> 4 ^ 253)) / 255.0F;
                  if (!"please dont crack my plugin".equals("Some babies were dropped on their heads but you were clearly thrown at a wall.")) {
                        ;
                  }

                  int var10 = ((1248018049 >> 1 ^ 483508334) << 1 & 621381388) << 3 ^ 134492256;
                  if ((1991885932 ^ 1755682536 ^ 42139703 ^ 1786520923) != 0) {
                        ;
                  }

                  int var11 = (1278579138 >> 2 >> 2 | 15942839) ^ 83058111;
                  int var12 = ((1866790506 ^ 565583449) >> 4 & 31613041 & 9809654 | 151448) ^ 8548248;
                  int var13 = 545538305 >>> 4 << 1 ^ 68192288;
                  int var10000 = (1509030216 | 944255368 | 757560057) & 346253013 ^ 346251985;
                  if (((934772014 >> 1 ^ 24492666) & 269597736 ^ -762367616) != 0) {
                        ;
                  }

                  int var14 = var10000;
                  int var15 = (0 & 697651159 | 1390057150) << 2 >> 1 ^ 632630653;
                  if (!"please go outside".equals("stop skidding")) {
                        ;
                  }

                  var2 *= 2.0D;
                  if ((2091945979 >> 4 << 1 ^ -609090745) != 0) {
                        ;
                  }

                  var4 *= 2.0D;
                  if (var15 != 0) {
                        GL11.glPushMatrix();
                        if ((1601325676 << 2 >> 2 ^ 347515727 ^ -561943385) != 0) {
                              ;
                        }

                        GlStateManager.scale(0.5D, 0.5D, 0.5D);
                        if (!"i hope you catch fire ngl".equals("You're so fat whenever you go to the beach the tide comes in.")) {
                              ;
                        }

                        GlStateManager.enableBlend();
                        var10000 = (149 & 50 | 12 | 10) >>> 3 << 2 ^ 782;
                        int var10001 = 331 >>> 2 >>> 2 ^ 791;
                        if ((6381656 >>> 4 ^ 398853) == 0) {
                              ;
                        }

                        GlStateManager.blendFunc(var10000, var10001);
                        float var22 = (float)(var6 >> ((8 << 2 | 29) >> 1 ^ 14) & ((64 & 21) << 3 ^ 255)) / 255.0F;
                        if ((1835074314 << 2 >> 4 ^ -78102334) == 0) {
                              ;
                        }

                        float var23 = (float)(var6 >> (((2 | 1) << 1 << 1 ^ 3) >>> 4 ^ 8) & (((112 | 108) << 3 | 415) ^ 768)) / 255.0F;
                        float var10002 = (float)(var6 & (236 << 3 << 1 >>> 1 ^ 1951)) / 255.0F;
                        if (((8921672 >> 3 | 693114) >>> 1 ^ -1621079157) != 0) {
                              ;
                        }

                        GlStateManager.color(var22, var23, var10002, var9);
                        if ((2041786927 >> 1 ^ 115428938 ^ 976803677) == 0) {
                              ;
                        }

                        int var16 = var1.length();
                        GlStateManager.enableTexture2D();
                        GlStateManager.bindTexture(this.tex.getGlTextureId());
                        var10000 = ((3362 >>> 4 | 98) << 4 & 232) >>> 1 ^ 3569;
                        var10001 = this.tex.getGlTextureId();
                        if ((137371723 >> 4 >> 3 ^ 1981318921) != 0) {
                              ;
                        }

                        GL11.glBindTexture(var10000, var10001);
                        if (!"shitted on you harder than archybot".equals("nefariousMoment")) {
                              ;
                        }

                        for(int var17 = ((1547385861 | 586417955 | 1789254841) & 549998109) << 3 ^ 105017576; var17 < var16; ++var17) {
                              if ((1211289038 << 1 >> 3 ^ 1661616210 ^ 1210489074) == 0) {
                              }

                              char var18 = var1.charAt(var17);
                              var10001 = (145 ^ 135 ^ 10) >>> 1 << 1 ^ 187;
                              if (!"Your family tree must be a cactus because everyone on it is a prick.".equals("stringer is a good obfuscator")) {
                                    ;
                              }

                              if (var18 == var10001 && var17 < var16) {
                                    int var19 = (15 & 2) << 2 >>> 3 >> 1 ^ 1788922654 ^ 1788922635;

                                    label245: {
                                          try {
                                                char var25 = var1.charAt(var17 + ((0 ^ 945883495 | 869954482 | 346097138) ^ 1073446902));
                                                if (((551359738 | 101819766) << 1 << 4 ^ -608780352) != 0) {
                                                }

                                                var19 = "0123456789abcdefklmnor".indexOf(var25);
                                          } catch (Exception var21) {
                                                break label245;
                                          }

                                          if (((2138113 >> 2 | 87191) >> 2 ^ 659937141) != 0) {
                                                ;
                                          }
                                    }

                                    if (var19 < (((11 ^ 1) & 1) << 2 ^ 16)) {
                                          var11 = (607021344 << 2 << 1 ^ 479136832) << 2 ^ -135195392;
                                          var12 = (((1469464715 | 1248092644) ^ 634768940) << 2 & 1215820419) >>> 1 ^ 604199168;
                                          var10 = 262912 << 1 ^ 525824;
                                          var14 = 1033621569 >> 3 << 1 ^ 258405392;
                                          var13 = (83025930 >>> 2 | 4628958) ^ 25082334;
                                          GlStateManager.bindTexture(this.tex.getGlTextureId());
                                          if (!"buy a domain and everything else you need at namecheap.com".equals("please go outside")) {
                                                ;
                                          }

                                          label234: {
                                                var8 = this.charData;
                                                if (var19 >= 0) {
                                                      var10001 = (2 ^ 0) >>> 1 >>> 3 >>> 3 ^ 15;
                                                      if (!"i hope you catch fire ngl".equals("you probably spell youre as your")) {
                                                            ;
                                                      }

                                                      if (var19 <= var10001) {
                                                            break label234;
                                                      }
                                                }

                                                var19 = (1 | 0) << 1 >> 3 ^ 146666549 ^ 146666554;
                                          }

                                          if (var7) {
                                                var19 += 16;
                                          }

                                          int var20 = this.colorCode[var19];
                                          var22 = (float)(var20 >> ((6 & 1) >> 1 ^ 2098426072 ^ 2098426056) & ((47 << 2 ^ 108 | 2) >> 1 ^ 150)) / 255.0F;
                                          var23 = (float)(var20 >> ((1 & 0) >> 2 ^ 8) & (((151 ^ 16) << 2 & 236) << 1 >> 2 ^ 249)) / 255.0F;
                                          int var24 = var20 & ((214 << 3 | 101) & 1653 & 1139 ^ 1166);
                                          if (((546000516 ^ 487847298) >>> 3 >>> 4 ^ -769081142) != 0) {
                                                ;
                                          }

                                          var10002 = (float)var24;
                                          if ((199581719 >> 4 << 4 >>> 1 ^ 99790856) == 0) {
                                                ;
                                          }

                                          GlStateManager.color(var22, var23, var10002 / 255.0F, var9);
                                    } else if (var19 == ((1 | 0 | 0) ^ 17)) {
                                          var10 = (0 >>> 1 | 540927628) ^ 540927629;
                                    } else if (var19 == (10 >>> 2 >>> 4 ^ 17)) {
                                          var11 = (0 ^ 380708125) << 4 >>> 3 >> 4 ^ 14034082;
                                          if (var12 != 0) {
                                                GlStateManager.bindTexture(this.texItalicBold.getGlTextureId());
                                                var8 = this.boldItalicChars;
                                          } else {
                                                GlStateManager.bindTexture(this.texBold.getGlTextureId());
                                                var8 = this.boldChars;
                                          }
                                    } else if (var19 == (13 >>> 3 >>> 4 ^ 18)) {
                                          var13 = (0 & 650428516 & 1909906218 | 580032779) >> 4 ^ 36252049;
                                    } else if (var19 == ((5 ^ 1) << 2 ^ 3)) {
                                          var14 = (0 ^ 1395754300) << 3 >> 4 ^ 1796803889 ^ -1836986450;
                                    } else {
                                          var10001 = (7 & 4 | 2) & 3 ^ 22;
                                          if ((17305604 << 4 & 451438 ^ 64) == 0) {
                                                ;
                                          }

                                          if (var19 == var10001) {
                                                var12 = (0 ^ 100888496) >>> 1 ^ 50444249;
                                                if (var11 != 0) {
                                                      GlStateManager.bindTexture(this.texItalicBold.getGlTextureId());
                                                      if ((1681355718 >>> 4 ^ 31577600 ^ 128098620) == 0) {
                                                            ;
                                                      }

                                                      if ((1082134657 >>> 4 & 21306985 ^ -473566956) != 0) {
                                                            ;
                                                      }

                                                      var8 = this.boldItalicChars;
                                                } else {
                                                      GlStateManager.bindTexture(this.texItalic.getGlTextureId());
                                                      var8 = this.italicChars;
                                                }
                                          } else if (var19 == (2 << 4 << 1 ^ 85)) {
                                                var11 = (775498243 | 174620078) >>> 1 >>> 3 >>> 2 ^ 12183038;
                                                var12 = ((69139128 ^ 41969683) >>> 2 & 17603928) >> 2 ^ 4268290;
                                                var10 = ((1451952840 ^ 1146330244) << 3 ^ 572790194) & 1079808132 ^ 4227200;
                                                var14 = (688078957 | 61180749 | 155639317) ^ 736616317;
                                                var13 = 76640719 >>> 3 << 4 & 132872593 ^ 19030416;
                                                var22 = (float)(var6 >> ((14 & 5 ^ 1) << 2 ^ 4) & (183 << 3 & 521 ^ 0 ^ 247)) / 255.0F;
                                                var23 = (float)(var6 >> ((2 & 0) >> 3 & 982438402 & 704043043 ^ 8) & ((209 ^ 52) >> 4 ^ 241)) / 255.0F;
                                                var10002 = (float)(var6 & (((190 | 137) ^ 120 | 110) << 4 ^ 3599)) / 255.0F;
                                                if (!"buy a domain and everything else you need at namecheap.com".equals("Some babies were dropped on their heads but you were clearly thrown at a wall.")) {
                                                      ;
                                                }

                                                GlStateManager.color(var22, var23, var10002, var9);
                                                if (((1600178683 >>> 1 << 3 | 226967662) ^ -164732942) != 0) {
                                                      ;
                                                }

                                                GlStateManager.bindTexture(this.tex.getGlTextureId());
                                                var8 = this.charData;
                                                if (!"please dont crack my plugin".equals("please dont crack my plugin")) {
                                                      ;
                                                }
                                          }
                                    }

                                    ++var17;
                              } else if (var18 < var8.length && var18 >= 0) {
                                    GL11.glBegin(3 << 3 << 2 ^ 100);
                                    float var10003 = (float)var2;
                                    if (!"buy a domain and everything else you need at namecheap.com".equals("i hope you catch fire ngl")) {
                                          ;
                                    }

                                    this.drawChar(var8, var18, var10003, (float)var4);
                                    GL11.glEnd();
                                    if (((50602136 >> 3 ^ 3263588) >>> 1 ^ 2663483) == 0) {
                                          ;
                                    }

                                    if (var13 != 0) {
                                          if ((589828 >> 3 << 1 ^ 1628204986) != 0) {
                                                ;
                                          }

                                          this.drawLine(var2, var4 + (double)(var8[var18].height / ((1 & 0 | 9212551) << 2 << 3 >>> 2 ^ 73700410)), var2 + (double)var8[var18].width - 8.0D, var4 + (double)(var8[var18].height / ((0 >>> 1 | 1781467461) >> 4 ^ 111341718)), 1.0F);
                                    }

                                    if (var14 != 0) {
                                          double var26 = (double)var8[var18].height;
                                          if ((((1382095001 >>> 3 | 21838732) & 105655525 | 34334288) ^ 38792917) == 0) {
                                                ;
                                          }

                                          this.drawLine(var2, var4 + var26 - 2.0D, var2 + (double)var8[var18].width - 8.0D, var4 + (double)var8[var18].height - 2.0D, 1.0F);
                                    }

                                    if (((608239784 ^ 247371494) << 3 ^ -1392873244) != 0) {
                                          ;
                                    }

                                    if ((1520388995 << 4 >> 3 >> 2 << 2 ^ -180447484) == 0) {
                                          ;
                                    }

                                    var10001 = var8[var18].width;
                                    if (!"please take a shower".equals("ape covered in human flesh")) {
                                          ;
                                    }

                                    var2 += (double)(var10001 - ((2 >> 2 & 1854629692 ^ 327265711) & 295749047 ^ 293635503) + this.charOffset);
                              }
                        }

                        if ((612771755 >> 3 << 2 ^ 306385876) == 0) {
                              ;
                        }

                        var10000 = 2164 & 428 ^ 11 ^ 3196;
                        var10001 = (1734 ^ 1648) >> 4 ^ 5 ^ 4366;
                        if (!"minecraft".equals("stop skidding")) {
                              ;
                        }

                        GL11.glHint(var10000, var10001);
                        if (((800787676 ^ 383402232 ^ 175803928 ^ 475730897) & 104972266 ^ 104894952) == 0) {
                              ;
                        }

                        GL11.glPopMatrix();
                  }

                  return (float)var2 / 2.0F;
            }
      }

      public int getFontSize() {
            if (!"buy a domain and everything else you need at namecheap.com".equals("i hope you catch fire ngl")) {
                  ;
            }

            return this.fontSize;
      }

      public float drawCenteredString(String var1, float var2, float var3, int var4) {
            return this.drawString(var1, var2 - (float)(this.getStringWidth(var1) / (1 >>> 1 >> 3 << 4 >>> 1 << 3 ^ 2)), var3, var4);
      }

      public void setAntiAlias(boolean var1) {
            if (((183154762 >>> 2 | 6109617) >>> 3 ^ 6289398) == 0) {
                  ;
            }

            super.setAntiAlias(var1);
            this.setupBoldItalicIDs();
      }

      private void setupMinecraftColorcodes() {
            for(int var1 = ((141679771 | 84186225) & 3289027) >>> 4 >>> 1 ^ 98406; var1 < ((27 << 1 ^ 9 ^ 52 | 9) ^ 43); ++var1) {
                  int var2 = (var1 >> ((0 ^ 1585112394) & 1045074232 ^ 377773422 ^ 147774565) & ((0 ^ 1633429440 | 1149857974) >>> 4 ^ 106813310)) * ((56 >>> 1 << 3 ^ 93 | 37) ^ 232);
                  int var10000 = var1 >> (1 >>> 4 >> 2 ^ 1781176224 ^ 1781176226);
                  int var10001 = (0 & 630217831 | 472962872 | 207372861) ^ 477943612;
                  if (((979296104 ^ 30908226) >> 4 & 9970383 ^ 9961474) == 0) {
                        ;
                  }

                  var10000 &= var10001;
                  var10001 = (144 & 109) >>> 2 >>> 2 >> 1 >> 3 ^ 170;
                  if ((109668280 ^ 41008297 ^ 43467887 ^ 107978622) == 0) {
                        ;
                  }

                  int var3 = var10000 * var10001 + var2;
                  int var4 = (var1 >> ((0 ^ 872705538) << 4 ^ 1078386721) & (((0 >>> 4 ^ 1896319659) & 1440425191) >>> 2 ^ 339787817)) * ((83 >>> 3 >> 2 ^ 0) & 1 ^ 170) + var2;
                  int var5 = (var1 >> ((1611435954 >> 3 ^ 161008096) >> 1 >> 4 >> 1 ^ 1467744) & ((0 >>> 1 ^ 588841680) & 247058565 ^ 35192961)) * (122 & 46 ^ 5 ^ 29 ^ 152) + var2;
                  if ((((1915181974 << 4 ^ 200592514) & 393179087 | 2622330) ^ -321184241) != 0) {
                        ;
                  }

                  var10001 = 0 >>> 2 & 114497180 ^ 6;
                  if (!"i hope you catch fire ngl".equals("Some babies were dropped on their heads but you were clearly thrown at a wall.")) {
                        ;
                  }

                  if (var1 == var10001) {
                        var3 += 85;
                  }

                  if (var1 >= ((1 >>> 1 | 755678120 | 516093510 | 269578155) ^ 1071382527)) {
                        var3 /= (3 ^ 1 | 1) & 0 ^ 4;
                        var4 /= ((2 | 0) ^ 0 | 1) ^ 1 ^ 6;
                        var5 /= (2 ^ 0 ^ 1) >> 3 ^ 4;
                  }

                  if (((973639781 ^ 188084831 | 356389894 | 463021330) ^ -1209547574) != 0) {
                        ;
                  }

                  int[] var6 = this.colorCode;
                  int var10002 = (var3 & (173 << 1 >> 3 >> 4 ^ 253)) << ((8 & 5) << 1 ^ 16) | (var4 & ((168 | 8 | 102) >> 3 ^ 226)) << (3 >> 3 >>> 3 ^ 201300453 ^ 201300461) | var5 & (((152 | 38) & 148) >>> 3 ^ 237);
                  if (!"your mom your dad the one you never had".equals("ape covered in human flesh")) {
                        ;
                  }

                  var6[var1] = var10002;
            }

      }

      public int getStringWidth(String var1) {
            if ((72187192 >> 4 >>> 1 >>> 2 & 265308 ^ -1015041812) != 0) {
                  ;
            }

            if (var1 == null) {
                  return (1779164294 ^ 697936025) >> 4 & 14812570 ^ 1369032 ^ 3467080;
            } else {
                  int var2 = (1902877988 << 3 | 790534323) ^ 1260459447 ^ -461378556;
                  CFont.CharData[] var3 = this.charData;
                  int var10000 = (1148850 >>> 4 | 21655) ^ '馝' ^ 116066;
                  if (((1278509016 | 171019648 | 637738845) >> 2 >>> 4 ^ -454126309) != 0) {
                        ;
                  }

                  int var4 = var10000;
                  int var5 = 1678174743 ^ 1347523005 ^ 562978669 ^ 366552263;
                  var10000 = var1.length();
                  if ((8785985 ^ 8785985) == 0) {
                        ;
                  }

                  int var6 = var10000;

                  for(int var7 = 661705577 >> 4 >> 2 << 1 >> 1 ^ 10339149; var7 < var6; ++var7) {
                        char var10 = var1.charAt(var7);
                        if (((116088316 << 1 & 7111305) << 2 ^ 17959456) == 0) {
                              ;
                        }

                        char var8 = var10;
                        if (var8 == (((30 | 8) ^ 28) & 0 ^ 167)) {
                              if (((1227249495 << 3 | 10818805) ^ 1236778749) == 0) {
                                    ;
                              }

                              if (var7 < var6) {
                                    if (((321054555 | 262303622) >>> 1 >>> 1 ^ 132692983) == 0) {
                                          ;
                                    }

                                    int var9 = "0123456789abcdefklmnor".indexOf(var8);
                                    if (var9 < ((13 | 9 | 12) << 1 << 4 ^ 432)) {
                                          if ((1856855185 >>> 3 & 125271450 ^ 89467282) == 0) {
                                                ;
                                          }

                                          var4 = (429473609 >> 1 << 4 << 2 | 21438632) ^ 862448040;
                                          var5 = (574458512 >>> 3 | 29584656) ^ 96992594;
                                    } else if (var9 == (((15 & 5) << 2 << 4 | 146) << 4 ^ 7473)) {
                                          var4 = (0 & 652527238) >> 2 >>> 3 >> 4 ^ 1;
                                          if (!"your mom your dad the one you never had".equals("stringer is a good obfuscator")) {
                                                ;
                                          }

                                          if (var5 != 0) {
                                                var3 = this.boldItalicChars;
                                          } else {
                                                var3 = this.boldChars;
                                          }
                                    } else if (var9 == (((2 ^ 1) >>> 3 | 1001908262) ^ 250532591 ^ 895040221)) {
                                          var10000 = ((0 | 1149856661) ^ 102612610 ^ 958152097) & 939350542 ^ 864569351;
                                          if (((655196871 << 4 ^ 1728186133) << 2 >> 4 ^ -1101465337) != 0) {
                                                ;
                                          }

                                          var5 = var10000;
                                          if ((2035996144 >>> 4 & 3146316 ^ 1048652) == 0) {
                                                ;
                                          }

                                          if (var4 != 0) {
                                                var3 = this.boldItalicChars;
                                          } else {
                                                var3 = this.italicChars;
                                                if ((((1049448181 | 541918320) >>> 3 | 1422241) ^ 131972095) == 0) {
                                                      ;
                                                }
                                          }
                                    } else {
                                          if (!"stop skidding".equals("please dont crack my plugin")) {
                                                ;
                                          }

                                          if (var9 == (((9 | 0) ^ 5) << 3 ^ 117)) {
                                                var4 = (33727144 | 19977665) >> 2 ^ 13417978;
                                                if (((1127139271 | 752852262 | 1843959741) >>> 1 ^ -464655887) != 0) {
                                                      ;
                                                }

                                                var5 = (1382611391 >> 1 >>> 2 & 81257823) >> 2 ^ 1130821;
                                                var3 = this.charData;
                                          }
                                    }

                                    ++var7;
                                    continue;
                              }
                        }

                        if (var8 < var3.length) {
                              if (((1201649099 << 2 ^ 191304296) >>> 2 & 1293275 ^ 143697) == 0) {
                                    ;
                              }

                              if (var8 >= 0) {
                                    if (((1496790695 ^ 1264950918 | 101312346) ^ 375123835) == 0) {
                                          ;
                                    }

                                    if ((855077017 >> 1 ^ 169843079 ^ 325329355) == 0) {
                                          ;
                                    }

                                    int var10001 = var3[var8].width;
                                    int var10002 = (6 & 0 | 857651628) >> 4 ^ 44800673 ^ 26898739;
                                    if ((473909755 << 3 >>> 4 >> 4 ^ 9779507 ^ 7652220) == 0) {
                                          ;
                                    }

                                    var2 += var10001 - var10002 + this.charOffset;
                              }
                        }
                  }

                  if ((((1354270834 << 2 >> 4 ^ 8005590) & 16119852) >> 2 ^ -341468852) != 0) {
                        ;
                  }

                  return var2 / ((1 >> 3 | 1950353986) << 1 ^ -394259322);
            }
      }

      public void setFontSize(int var1) {
            if (!"you're dogshit".equals("please dont crack my plugin")) {
                  ;
            }

            this.fontSize = var1;
      }

      public void setFontName(String var1) {
            this.fontName = var1;
      }

      public List wrapWords(String var1, double var2) {
            ArrayList var10000 = new ArrayList();
            if (((1817376840 << 1 & 488577736) >>> 2 ^ -530289032) != 0) {
                  ;
            }

            ArrayList var4 = var10000;
            if (((2114230320 | 2028454836) >>> 1 >>> 4 ^ 66535133) == 0) {
                  ;
            }

            if ((double)this.getStringWidth(var1) > var2) {
                  if (((1111968754 ^ 522853509 | 1514845176) & 1443016084 ^ -57645977) != 0) {
                        ;
                  }

                  String[] var5 = (String[])var1.split(" ");
                  String var6 = "";
                  int var7 = (('\ue779' ^ '\uda59') >>> 4 | 735 | 817) ^ 'ﰀ';
                  String[] var8 = var5;
                  int var9 = var5.length;

                  for(int var10 = 1818190091 << 1 >>> 1 >>> 1 ^ 909095045; var10 < var9; ++var10) {
                        if (!"shitted on you harder than archybot".equals("Your family tree must be a cactus because everyone on it is a prick.")) {
                              ;
                        }

                        String var11 = var8[var10];

                        for(int var12 = (1669586559 << 2 | 2099231951) ^ 1876608366 ^ -1866123119; var12 < ((char[])var11.toCharArray()).length; ++var12) {
                              char var13 = ((char[])var11.toCharArray())[var12];
                              if (var13 == ((51 & 8 | 1827978020) << 3 ^ 1738922375) && var12 < ((char[])var11.toCharArray()).length - ((0 | 1524192789) >>> 2 >>> 1 ^ 190524099)) {
                                    var7 = ((char[])var11.toCharArray())[var12 + (0 >> 3 << 1 ^ 1)];
                              }
                        }

                        String var10001 = (new StringBuilder()).append(var6).append(var11).append(" ").toString();
                        if ((434709412 >> 2 >> 1 >> 1 ^ 27169338) == 0) {
                              ;
                        }

                        if ((double)this.getStringWidth(var10001) < var2) {
                              StringBuilder var16 = new StringBuilder();
                              if ((2069009769 << 3 >>> 2 << 1 << 1 ^ 433863647) != 0) {
                                    ;
                              }

                              var6 = var16.append(var6).append(var11).append(" ").toString();
                        } else {
                              if ((360462960 >>> 4 << 1 ^ -705757325) != 0) {
                                    ;
                              }

                              if (((92472320 ^ 26261330 ^ 39474630 | 24559483) >>> 4 ^ 905686518) != 0) {
                                    ;
                              }

                              var4.add(var6);
                              var6 = (new StringBuilder()).append("§").append((char)var7).append(var11).append(" ").toString();
                        }
                  }

                  if (var6.length() > 0) {
                        double var17 = (double)this.getStringWidth(var6);
                        if (!"please take a shower".equals("you're dogshit")) {
                              ;
                        }

                        if (var17 < var2) {
                              var4.add((new StringBuilder()).append("§").append((char)var7).append(var6).append(" ").toString());
                              var6 = "";
                        } else {
                              if ((1595300885 >>> 3 >> 4 ^ 12463288) == 0) {
                                    ;
                              }

                              Iterator var14 = this.formatString(var6, var2).iterator();

                              while(var14.hasNext()) {
                                    String var15 = (String)var14.next();
                                    var4.add(var15);
                                    if (!"Some babies were dropped on their heads but you were clearly thrown at a wall.".equals("yo mama name maurice")) {
                                          ;
                                    }

                                    if (!"you probably spell youre as your".equals("please go outside")) {
                                          ;
                                    }
                              }
                        }
                  }
            } else {
                  var4.add(var1);
            }

            return var4;
      }

      private void drawLine(double var1, double var3, double var5, double var7, float var9) {
            GL11.glDisable(2726 << 1 & 197 ^ 3493);
            if ((((1826422691 ^ 259226262 ^ 1146557404) >> 1 | 92383299) ^ 402516855) == 0) {
                  ;
            }

            GL11.glLineWidth(var9);
            if (!"please get a girlfriend and stop cracking plugins".equals("Some babies were dropped on their heads but you were clearly thrown at a wall.")) {
                  ;
            }

            GL11.glBegin((0 & 79250144 ^ 1114692126) >>> 4 ^ 69668256);
            if ((263495394 << 1 << 2 ^ 402797296) != 0) {
                  ;
            }

            GL11.glVertex2d(var1, var3);
            if (!"please go outside".equals("your mom your dad the one you never had")) {
                  ;
            }

            GL11.glVertex2d(var5, var7);
            GL11.glEnd();
            GL11.glEnable(3547 << 3 >>> 3 & 2405 ^ 1184);
      }

      public CFontRenderer(Font var1, boolean var2, boolean var3) {
            super(var1, var2, var3);
            if (((1984646125 >>> 4 & 110124967 | 15401761) >>> 3 ^ 14508788) == 0) {
                  ;
            }

            this.italicChars = new CFont.CharData[(100 ^ 53 | 35) ^ 371];
            this.boldItalicChars = new CFont.CharData[(142 & 12 ^ 1) >> 3 ^ 0 ^ 0 ^ 257];
            if (((835045515 << 3 ^ 864908614) >> 3 ^ -139172317) == 0) {
                  ;
            }

            this.colorCode = new int[4 >>> 1 >>> 2 ^ 32];
            this.colorcodeIdentifiers = "0123456789abcdefklmnor";
            if (!"stop skidding".equals("please get a girlfriend and stop cracking plugins")) {
                  ;
            }

            if (((2003858558 | 1157866671) ^ 849434309 ^ 1171431994) == 0) {
                  ;
            }

            this.setupMinecraftColorcodes();
            this.setupBoldItalicIDs();
      }

      public void setFractionalMetrics(boolean var1) {
            super.setFractionalMetrics(var1);
            this.setupBoldItalicIDs();
      }

      public void setFont(Font var1) {
            if (((693929078 >>> 1 & 154021093 | 1806021) ^ 4165349) == 0) {
                  ;
            }

            super.setFont(var1);
            this.setupBoldItalicIDs();
      }

      public List formatString(String var1, double var2) {
            ArrayList var4 = new ArrayList();
            String var5 = "";
            int var6 = (4118 ^ 675) << 2 >> 4 ^ 399 ^ '\ufadd';
            char[] var7 = (char[])var1.toCharArray();
            int var8 = (1401242455 >>> 3 | 170622995) & 168985381 ^ 168984609;

            while(true) {
                  int var10001 = var7.length;
                  if (((261344668 ^ 68322626) << 2 << 3 ^ 639088077) == 0) {
                  }

                  if (var8 >= var10001) {
                        if ((1090400795 >> 1 << 3 >>> 3 ^ 1188358083) != 0) {
                              ;
                        }

                        if (var5.length() > 0) {
                              if (!"shitted on you harder than archybot".equals("ape covered in human flesh")) {
                                    ;
                              }

                              var4.add(var5);
                        }

                        if (!"please get a girlfriend and stop cracking plugins".equals("Some babies were dropped on their heads but you were clearly thrown at a wall.")) {
                              ;
                        }

                        return var4;
                  }

                  char var9 = var7[var8];
                  if (((271093484 | 83157711) >> 4 >>> 2 >>> 1 ^ 399879238) != 0) {
                        ;
                  }

                  if (var9 == (((153 | 36) ^ 160) >>> 2 & 0 ^ 167)) {
                        var10001 = var7.length;
                        int var10002 = 0 >>> 2 >>> 2 ^ 745615801 ^ 745615800;
                        if ((696666516 >>> 1 >>> 3 ^ 42718068 ^ 1292269) == 0) {
                              ;
                        }

                        if (var8 < var10001 - var10002) {
                              var10002 = 0 >>> 3 >> 1 ^ 1;
                              if (!"i hope you catch fire ngl".equals("please get a girlfriend and stop cracking plugins")) {
                                    ;
                              }

                              var6 = var7[var8 + var10002];
                        }
                  }

                  if ((1156921584 >>> 3 & 76262645 ^ 9085972) == 0) {
                        ;
                  }

                  if ((double)this.getStringWidth((new StringBuilder()).append(var5).append(var9).toString()) < var2) {
                        var5 = (new StringBuilder()).append(var5).append(var9).toString();
                  } else {
                        var4.add(var5);
                        if (!"Some babies were dropped on their heads but you were clearly thrown at a wall.".equals("You're so fat whenever you go to the beach the tide comes in.")) {
                              ;
                        }

                        StringBuilder var10000 = (new StringBuilder()).append("§").append((char)var6).append(String.valueOf(var9));
                        if ((754868134 << 4 << 4 ^ 50375164 ^ -2059264128) != 0) {
                              ;
                        }

                        var5 = var10000.toString();
                  }

                  ++var8;
            }
      }

      private void setupBoldItalicIDs() {
            if ((864913841 >>> 4 >> 3 ^ 4100092 ^ 1570750238) != 0) {
                  ;
            }

            this.texBold = this.setupTexture(this.font.deriveFont((0 << 3 >> 2 | 277869269) ^ 165643442 ^ 424703590), this.antiAlias, this.fractionalMetrics, this.boldChars);
            this.texItalic = this.setupTexture(this.font.deriveFont((1 | 0) >>> 2 & 239413103 ^ 2), this.antiAlias, this.fractionalMetrics, this.italicChars);
            Font var10002 = this.font.deriveFont(1 >> 3 & 922655159 & 1757950901 ^ 3);
            if (((1107822664 | 981960641) ^ 2056228809) == 0) {
                  ;
            }

            this.texItalicBold = this.setupTexture(var10002, this.antiAlias, this.fractionalMetrics, this.boldItalicChars);
      }

      public float drawCenteredStringWithShadow(String var1, float var2, float var3, int var4) {
            return this.drawStringWithShadow(var1, (double)(var2 - (float)(this.getStringWidth(var1) / ((1 ^ 0 | 0) >> 1 ^ 2))), (double)var3, var4);
      }
}
