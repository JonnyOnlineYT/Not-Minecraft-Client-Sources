//Deobfuscated with https://github.com/PetoPetko/Minecraft-Deobfuscator3000 using mappings "wew"!

package me.independed.inceptice.font;

import java.awt.Font;
import java.util.Locale;
import java.util.Random;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import org.lwjgl.opengl.GL11;

public class GlyphPageFontRenderer {
      private float green;
      private GlyphPage regularGlyphPage;
      private boolean randomStyle;
      private float red;
      private float blue;
      private int[] colorCode;
      private float posY;
      public Random fontRandom;
      private GlyphPage boldGlyphPage;
      private boolean italicStyle;
      private boolean boldStyle;
      private float alpha;
      private GlyphPage boldItalicGlyphPage;
      private boolean underlineStyle;
      private GlyphPage italicGlyphPage;
      private boolean strikethroughStyle;
      private int textColor;
      private float posX;

      private int renderString(String var1, float var2, float var3, int var4, boolean var5) {
            if (var1 == null) {
                  return 537043200 << 4 >>> 4 >> 2 ^ 'ꡀ';
            } else {
                  if ((var4 & (866233997 << 4 >> 4 ^ -6181235)) == 0) {
                        var4 |= (405665092 >> 4 & 12446846) << 1 ^ 9959928 ^ -23699120;
                  }

                  if (var5) {
                        int var10001 = 2261888 >>> 2 << 3 >>> 3 ^ 16014364;
                        if (((1810508118 | 1807273418) >>> 3 << 2 ^ 905803500) == 0) {
                              ;
                        }

                        var4 = (var4 & var10001) >> ((1 ^ 0) << 2 >> 4 >>> 1 >> 2 ^ 2) | var4 & (1053034404 << 1 >>> 1 << 3 >>> 2 ^ -1031270584);
                  }

                  this.red = (float)(var4 >> (((15 & 11) >> 1 ^ 4) & 0 ^ 16) & ((137 & 79 | 8) ^ 246)) / 255.0F;
                  this.blue = (float)(var4 >> ((2 ^ 1 ^ 1 | 1) ^ 11) & (((48 ^ 6) >>> 1 ^ 14) >>> 2 ^ 250)) / 255.0F;
                  this.green = (float)(var4 & (((53 | 6) ^ 26 | 39) ^ 208)) / 255.0F;
                  this.alpha = (float)(var4 >> ((6 >>> 1 >> 3 << 1 | 606299740) ^ 606299716) & (((196 ^ 113) << 3 | 56) >> 2 << 4 ^ 5663)) / 255.0F;
                  if ((1839122053 >>> 4 << 2 >>> 4 >> 2 ^ 7184070) == 0) {
                        ;
                  }

                  GlStateManager.color(this.red, this.blue, this.green, this.alpha);
                  this.posX = var2 * 2.0F;
                  this.posY = var3 * 2.0F;
                  this.renderStringAtPos(var1, var5);
                  float var10000 = this.posX;
                  if (((218104071 ^ 115309437) << 2 ^ 796781032) == 0) {
                        ;
                  }

                  return (int)(var10000 / 4.0F);
            }
      }

      public GlyphPageFontRenderer(GlyphPage var1, GlyphPage var2, GlyphPage var3, GlyphPage var4) {
            if (((558367056 | 72545205) >> 2 >> 1 ^ 826240962) != 0) {
                  ;
            }

            super();
            this.fontRandom = new Random();
            this.colorCode = new int[2 << 1 >>> 3 & 594912331 ^ 860210408 ^ 689996013 ^ 442844197];
            if (!"stringer is a good obfuscator".equals("please get a girlfriend and stop cracking plugins")) {
                  ;
            }

            this.regularGlyphPage = var1;
            this.boldGlyphPage = var2;
            this.italicGlyphPage = var3;
            this.boldItalicGlyphPage = var4;

            for(int var5 = (1552058747 ^ 1005519185) >>> 1 ^ 867615765; var5 < (14 >> 1 ^ 3 ^ 36); ++var5) {
                  int var10000 = var5 >> ((0 & 745326223) >> 2 << 1 << 3 ^ 3) & (0 >>> 2 >>> 2 >>> 1 & 241940321 ^ 1);
                  if (!"You're so fat whenever you go to the beach the tide comes in.".equals("stringer is a good obfuscator")) {
                        ;
                  }

                  int var6 = var10000 * (((35 | 18) ^ 5) >> 4 ^ 86);
                  int var7 = (var5 >> ((1 ^ 0) & 0 ^ 2) & (0 << 3 & 148872229 ^ 1)) * ((18 << 3 << 1 << 4 | 543) ^ 2469 ^ 6928) + var6;
                  if (((336597216 >> 1 ^ 76518876) >> 2 ^ -1017683921) != 0) {
                        ;
                  }

                  int var10001 = (0 ^ 1170078832) << 3 >> 4 ^ 48168505;
                  if (!"please take a shower".equals("i hope you catch fire ngl")) {
                        ;
                  }

                  int var8 = (var5 >> var10001 & (0 >> 2 >>> 2 ^ 1)) * (((145 | 102) >> 3 | 10) >>> 2 ^ 173) + var6;
                  var10000 = (var5 & ((0 >> 1 | 1912523630) >> 4 ^ 119532727)) * (((28 ^ 19) & 5) << 3 ^ 130);
                  if (((((225749062 ^ 29724461) & 130793604) << 3 | 413206224) >>> 3 ^ 273850398) != 0) {
                        ;
                  }

                  int var9 = var10000 + var6;
                  if (!"stringer is a good obfuscator".equals("idiot")) {
                        ;
                  }

                  if (var5 == ((3 >>> 4 & 62887921) >> 3 ^ 6)) {
                        var7 += 85;
                  }

                  if (!"stop skidding".equals("Some babies were dropped on their heads but you were clearly thrown at a wall.")) {
                        ;
                  }

                  if (var5 >= (((4 | 0) ^ 2) << 3 & 38 ^ 48)) {
                        var7 /= (0 << 4 | 715114868) ^ 715114864;
                        var8 /= 1 << 2 >> 1 >> 3 ^ 4;
                        if (((1830038880 >>> 4 | 31220095) ^ 131949567) == 0) {
                              ;
                        }

                        var9 /= (0 >> 2 >> 2 >> 2 | 181946378) ^ 181946382;
                        if (!"shitted on you harder than archybot".equals("i hope you catch fire ngl")) {
                              ;
                        }
                  }

                  int[] var10 = this.colorCode;
                  int var10002 = (var7 & ((167 ^ 152) >>> 1 ^ 224)) << (9 >>> 3 & 0 & 1760102245 & 1066274322 ^ 16);
                  if (!"Some babies were dropped on their heads but you were clearly thrown at a wall.".equals("intentMoment")) {
                        ;
                  }

                  if (((1038307220 >>> 3 ^ 19427193) << 4 & 911166162 ^ 1496973421) != 0) {
                        ;
                  }

                  var10[var5] = var10002 | (var8 & (166 << 4 << 4 & 27058 ^ 8447)) << ((3 >> 2 | 904266627) ^ 904266635) | var9 & ((115 >>> 1 ^ 38) << 2 ^ 131);
            }

      }

      private void renderStringAtPos(String var1, boolean var2) {
            GlyphPage var3 = this.getCurrentGlyphPage();
            GL11.glPushMatrix();
            if (!"Your family tree must be a cactus because everyone on it is a prick.".equals("buy a domain and everything else you need at namecheap.com")) {
                  ;
            }

            GL11.glScaled(0.5D, 0.5D, 0.5D);
            GlStateManager.enableBlend();
            GlStateManager.blendFunc(572 >>> 4 >> 4 ^ 768, (158 ^ 33) >>> 2 << 3 ^ 635);
            GlStateManager.enableTexture2D();
            var3.bindTexture();
            int var10000 = (277 | 182) >>> 4 >>> 2 ^ 3559;
            if (((770039080 ^ 189898683 | 42931877) << 2 ^ -1694672164) == 0) {
                  ;
            }

            GL11.glTexParameteri(var10000, ((1758 | 1377) << 1 << 2 | 13789) ^ 11894 ^ 14731, (8469 | 1926) ^ 6507 ^ 6397);

            for(int var4 = (1964689738 ^ 1232396634 | 829200438) ^ 1030666294; var4 < var1.length(); ++var4) {
                  char var5 = var1.charAt(var4);
                  if (var5 == (((51 | 37) ^ 0 | 39) ^ 144)) {
                        if (((479068043 >>> 1 >>> 1 << 1 | 94818708) ^ 266797012) == 0) {
                              ;
                        }

                        if (var4 + (((0 | 2000613852) >> 1 ^ 146399621) << 2 ^ 1626356293 ^ -1402314776) < var1.length()) {
                              if (!"your mom your dad the one you never had".equals("buy a domain and everything else you need at namecheap.com")) {
                                    ;
                              }

                              int var8 = "0123456789abcdefklmnor".indexOf(var1.toLowerCase(Locale.ENGLISH).charAt(var4 + (0 << 1 << 4 ^ 1547579308 ^ 1547579309)));
                              float var9;
                              float var10;
                              float var11;
                              int var10001;
                              if (var8 < (11 << 4 & 24 ^ 0)) {
                                    this.randomStyle = (boolean)(39290610 >>> 1 << 3 ^ 157162440);
                                    this.boldStyle = (boolean)((34611216 >> 3 | 2624176) & 5357681 ^ 4194352);
                                    this.strikethroughStyle = (boolean)((1521271367 | 405797025) >>> 4 ^ 5868053 ^ 39813024 ^ 128715291);
                                    if (((2024808140 >> 4 >>> 1 ^ 51795788) << 2 & 38061104 ^ -680599709) != 0) {
                                          ;
                                    }

                                    this.underlineStyle = (boolean)(256964677 << 1 & 83692758 ^ 77660290);
                                    if ((1636943268 << 4 >> 3 ^ -613533134) != 0) {
                                          ;
                                    }

                                    this.italicStyle = (boolean)(1624567175 >>> 1 >>> 4 ^ 42524068 ^ 26114760);
                                    if (var8 < 0) {
                                          var8 = 5 >>> 1 << 4 & 24 ^ 15;
                                    }

                                    if (var2) {
                                          var8 += 16;
                                    }

                                    int var7 = this.colorCode[var8];
                                    if ((1979114894 >>> 4 >>> 3 ^ 13580667 ^ -730592310) != 0) {
                                          ;
                                    }

                                    this.textColor = var7;
                                    var9 = (float)(var7 >> ((12 | 11) << 4 & 165 & 133 ^ 144)) / 255.0F;
                                    if ((250441065 >>> 4 << 1 ^ -375054957) != 0) {
                                          ;
                                    }

                                    var10001 = var7 >> ((4 & 0) >> 3 >>> 3 ^ 8);
                                    int var10002 = 139 << 3 << 4 << 3 << 2 << 1 ^ 1138943;
                                    if (!"please go outside".equals("You're so fat whenever you go to the beach the tide comes in.")) {
                                          ;
                                    }

                                    var10 = (float)(var10001 & var10002);
                                    if (((1041 | 183) ^ 1203 ^ 604716822) != 0) {
                                          ;
                                    }

                                    var10 /= 255.0F;
                                    var11 = (float)(var7 & ((214 >> 4 << 3 | 65) ^ 150));
                                    if (!"ape covered in human flesh".equals("please get a girlfriend and stop cracking plugins")) {
                                          ;
                                    }

                                    GlStateManager.color(var9, var10, var11 / 255.0F, this.alpha);
                              } else if (var8 == ((9 & 8) << 1 ^ 0)) {
                                    this.randomStyle = (boolean)((0 & 1976720933 ^ 1256035191) << 3 ^ 1458346937);
                              } else if (var8 == ((0 >>> 4 & 1086658967) << 3 >>> 2 ^ 17)) {
                                    if ((822509890 ^ 708212204 ^ 1972240826) != 0) {
                                          ;
                                    }

                                    var10001 = 0 >> 3 >> 1 ^ 1;
                                    if (!"buy a domain and everything else you need at namecheap.com".equals("Some babies were dropped on their heads but you were clearly thrown at a wall.")) {
                                          ;
                                    }

                                    this.boldStyle = (boolean)var10001;
                                    if (((1515800683 >>> 1 >>> 1 ^ 12663516 ^ 118798913) & 70520209 ^ -622570090) != 0) {
                                          ;
                                    }
                              } else if (var8 == (15 << 2 << 2 ^ 226)) {
                                    var10001 = 0 << 3 >> 2 ^ 1;
                                    if ((523561025 >> 3 >>> 3 & 640006 ^ -2067640719) != 0) {
                                          ;
                                    }

                                    this.strikethroughStyle = (boolean)var10001;
                              } else if (var8 == ((18 >>> 4 ^ 0) >> 1 ^ 19)) {
                                    this.underlineStyle = (boolean)((0 << 1 | 1118366625) >>> 3 >>> 1 ^ 69897915);
                              } else if (var8 == ((15 >>> 2 | 1) >> 2 ^ 20)) {
                                    if ((2098895818 >>> 1 & 934796259 ^ 914704865) == 0) {
                                          ;
                                    }

                                    this.italicStyle = (boolean)(0 >> 1 ^ 1614855550 ^ 47885911 ^ 1654264616);
                              } else {
                                    this.randomStyle = (boolean)(((390981063 | 240911542) << 3 | 1208556422) & 1637963427 ^ 527225984 ^ 2144191010);
                                    this.boldStyle = (boolean)((1240422687 >> 4 >>> 4 | 1342234 | 274289) ^ 6160251);
                                    this.strikethroughStyle = (boolean)((787604340 >>> 4 >>> 4 ^ 2866879) >>> 2 << 3 ^ 695992);
                                    var10001 = (408061209 | 407320458 | 135098433) ^ 408942555;
                                    if ((1778568475 << 3 >>> 2 ^ 335911478) == 0) {
                                          ;
                                    }

                                    this.underlineStyle = (boolean)var10001;
                                    this.italicStyle = (boolean)((1755311274 ^ 37581599) >>> 1 & 556201124 ^ 553665664);
                                    var9 = this.red;
                                    var10 = this.blue;
                                    var11 = this.green;
                                    if (((1105973134 >>> 4 << 2 >>> 3 ^ 1203201) & 29704087 ^ 328213) == 0) {
                                          ;
                                    }

                                    GlStateManager.color(var9, var10, var11, this.alpha);
                              }

                              ++var4;
                              continue;
                        }
                  }

                  var3 = this.getCurrentGlyphPage();
                  var3.bindTexture();
                  float var6 = var3.drawChar(var5, this.posX, this.posY);
                  if ((532592718 >>> 3 >> 2 << 1 ^ -52546752) != 0) {
                        ;
                  }

                  if ((2065576185 >> 3 >>> 3 >> 3 ^ 1139276 ^ -1566355538) != 0) {
                        ;
                  }

                  this.doDraw(var6, var3);
            }

            if ((((335030417 ^ 264360392) & 191034025) << 3 ^ 1431159924) != 0) {
                  ;
            }

            var3.unbindTexture();
            GL11.glPopMatrix();
      }

      public int getStringWidth(String var1) {
            if (var1 == null) {
                  return 15321124 >> 3 >> 2 ^ 478785;
            } else {
                  int var2 = (431908235 | 267982192) ^ 20378580 ^ 516523567;
                  int var4 = var1.length();
                  int var5 = ((1951609570 << 4 ^ 617337223) >> 1 >>> 3 | 74023415) ^ 109023231;

                  for(int var6 = 1181221056 >>> 3 << 4 ^ -1932525184; var6 < var4; ++var6) {
                        char var10000 = var1.charAt(var6);
                        if ((230788 >> 1 << 4 ^ 1846304) == 0) {
                              ;
                        }

                        char var7 = var10000;
                        if (var5 != 0 && var7 >= (((26 | 11) & 11) >> 1 >> 2 ^ 49) && var7 <= ((23 & 3) >>> 1 >> 3 ^ 114)) {
                              int var9 = "0123456789abcdefklmnor".indexOf(var7);
                              if ((((1342857903 >> 1 | 7875424) >>> 2 ^ 76857254) & 66060568 ^ -27660205) != 0) {
                                    ;
                              }

                              int var8 = var9;
                              if ((((1444272912 << 4 ^ 235708969) & 273256492) << 1 >>> 2 ^ -1089741406) != 0) {
                                    ;
                              }

                              if (var8 < ((1 >> 1 & 214399936 | 1417146343 | 648595469) ^ 1996484607)) {
                                    this.boldStyle = (boolean)((((1554435327 >> 1 | 663729835) & 706982910) >> 2 | 64757611) ^ 199032831);
                                    this.italicStyle = (boolean)((775846573 ^ 551495353 | 118545687) << 4 << 3 ^ -118584448);
                              } else if (var8 == (16 >> 3 << 3 >>> 1 ^ 25)) {
                                    this.boldStyle = (boolean)(0 & 1380019718 & 1500818914 ^ 1);
                              } else if (var8 == ((18 >>> 2 >> 3 | 254727503) ^ 254727515)) {
                                    this.italicStyle = (boolean)(0 << 3 >> 4 >>> 3 ^ 1);
                              } else {
                                    if ((((1266622559 >> 1 & 320533577 | 1911815) >> 1 | 1205208) ^ 10485727) == 0) {
                                          ;
                                    }

                                    if (var8 == ((5 & 1) >>> 1 << 3 >> 4 ^ 21)) {
                                          this.boldStyle = (boolean)((651674364 >>> 3 >> 2 & 3375302) >> 4 ^ 206848);
                                          this.italicStyle = (boolean)(1652794130 >> 1 & 493578700 ^ 289489288);
                                    }
                              }

                              if (!"Your family tree must be a cactus because everyone on it is a prick.".equals("you probably spell youre as your")) {
                                    ;
                              }

                              ++var6;
                              var5 = (872289473 | 117656766) >> 2 >> 2 ^ 28868445 ^ 46631570;
                        } else {
                              if (!"please go outside".equals("please take a shower")) {
                                    ;
                              }

                              if (var5 != 0) {
                                    --var6;
                              }

                              if (!"please get a girlfriend and stop cracking plugins".equals("stringer is a good obfuscator")) {
                                    ;
                              }

                              var7 = var1.charAt(var6);
                              GlyphPage var3 = this.getCurrentGlyphPage();
                              var2 = (int)((float)var2 + (var3.getWidth(var7) - 8.0F));
                        }
                  }

                  return var2 / ((0 >>> 1 | 1776964713) << 4 ^ -1633335662);
            }
      }

      public String trimStringToWidth(String var1, int var2) {
            return this.trimStringToWidth(var1, var2, (boolean)(((1163018760 | 308622853) << 4 | 494211855) ^ 2138534879));
      }

      public int drawString(String var1, float var2, float var3, int var4, boolean var5) {
            if (((143057100 ^ 81533886 ^ 164984325) >> 1 ^ 46646971) == 0) {
                  ;
            }

            GlStateManager.enableAlpha();
            if ((((614255963 >> 2 >>> 4 | 4176531) ^ 8919221) & 849563 ^ 1489813734) != 0) {
                  ;
            }

            this.resetStyles();
            int var6;
            if (var5) {
                  var6 = this.renderString(var1, var2 + 1.0F, var3 + 1.0F, var4, (boolean)(((0 | 1266735465) ^ 1264594172) & 3848560 ^ 2142481));
                  var6 = Math.max(var6, this.renderString(var1, var2, var3, var4, (boolean)(((1304268574 ^ 201217959) & 1111589997) << 3 ^ 302776648)));
            } else {
                  if (((311298998 >>> 1 & 91358218 | 9738115) ^ 23444279 ^ 1223492548) != 0) {
                        ;
                  }

                  var6 = this.renderString(var1, var2, var3, var4, (boolean)(1589539477 >> 3 >> 3 >>> 4 << 1 ^ 3104568));
            }

            if ((1007621363 ^ 881972309 ^ 94673540 ^ 221927970) == 0) {
                  ;
            }

            return var6;
      }

      private void resetStyles() {
            this.randomStyle = (boolean)(248347615 >>> 1 >>> 3 >>> 4 ^ 970107);
            this.boldStyle = (boolean)(25265152 >>> 3 ^ 3158144);
            this.italicStyle = (boolean)((2028400817 >>> 4 >> 3 | 4263746) ^ 15847395);
            this.underlineStyle = (boolean)((25952548 >> 1 << 1 | 11293052) ^ 28070268);
            this.strikethroughStyle = (boolean)((1965328299 | 730133854 | 1890075909) >> 3 ^ 267755263);
      }

      public static GlyphPageFontRenderer create(String var0, int var1, boolean var2, boolean var3, boolean var4) {
            char[] var5 = new char[228 >>> 3 >>> 2 ^ 1 ^ 262];

            for(int var6 = (564292927 >> 4 | 7006495) & 35447928 ^ 35185752; var6 < var5.length; ++var6) {
                  if (((1172621501 >> 4 ^ 19022128) >>> 1 & 28125335 ^ 11272341) == 0) {
                        ;
                  }

                  var5[var6] = (char)var6;
            }

            if ((((1128304660 ^ 1110419644) & 23482760) >> 2 & 452836 ^ '耠') == 0) {
                  ;
            }

            GlyphPage var10000 = new GlyphPage;
            Font var10002 = new Font;
            if (!"ape covered in human flesh".equals("Some babies were dropped on their heads but you were clearly thrown at a wall.")) {
                  ;
            }

            var10002.<init>(var0, (1094625014 << 4 ^ 322860027) >>> 1 ^ 6950477, var1);
            if (!"yo mama name maurice".equals("please take a shower")) {
                  ;
            }

            var10000.<init>(var10002, (boolean)((0 >>> 3 << 2 ^ 1611891305 | 996360762) >> 3 ^ 258897998), (boolean)((0 << 4 >> 2 | 1827777658) ^ 1827777659));
            GlyphPage var10 = var10000;
            var10.generateGlyphPage(var5);
            var10.setupTexture();
            GlyphPage var7 = var10;
            GlyphPage var8 = var10;
            GlyphPage var9 = var10;
            if ((758824129 << 2 & 635826156 ^ -7852479) != 0) {
                  ;
            }

            int var10005;
            if (var2) {
                  var10000 = new GlyphPage;
                  var10002 = new Font;
                  var10005 = (0 | 1054754820) >> 1 >>> 4 ^ 32961089;
                  if ((((461677840 ^ 206497563 | 58898647) ^ 266070860) & 30821300 ^ 2091958371) != 0) {
                        ;
                  }

                  var10002.<init>(var0, var10005, var1);
                  var10000.<init>(var10002, (boolean)((0 << 4 | 634638805 | 131683038) >>> 2 ^ 167179510), (boolean)((0 >>> 3 ^ 462062691) >> 1 ^ 125109205 ^ 179323365));
                  var7 = var10000;
                  var7.generateGlyphPage(var5);
                  if (!"nefariousMoment".equals("You're so fat whenever you go to the beach the tide comes in.")) {
                        ;
                  }

                  var7.setupTexture();
            }

            if (var3) {
                  var10000 = new GlyphPage;
                  if (((1999530327 | 1060600810) >>> 2 ^ -437352223) != 0) {
                        ;
                  }

                  var10000.<init>(new Font(var0, 0 << 4 >>> 4 >>> 4 ^ 2, var1), (boolean)((0 << 1 | 1992195546) << 2 << 1 ^ -1242304815), (boolean)((0 ^ 2013505374) >>> 2 ^ 503376342));
                  if (((1280 | 670 | 948) ^ 1492127240) != 0) {
                        ;
                  }

                  var8 = var10000;
                  var8.generateGlyphPage(var5);
                  var8.setupTexture();
            }

            if (var4) {
                  if (!"please take a shower".equals("please take a shower")) {
                        ;
                  }

                  var10000 = new GlyphPage;
                  var10002 = new Font;
                  var10005 = (2 ^ 1) & 0 ^ 3;
                  if ((((1909595047 ^ 1508356766) >> 3 ^ 11423567 | 59676779) ^ 128907883) == 0) {
                        ;
                  }

                  var10002.<init>(var0, var10005, var1);
                  var10000.<init>(var10002, (boolean)((0 & 1715331066 | 1917868678) ^ 1917868679), (boolean)((0 | 2053561729) << 3 >> 4 ^ -46960959));
                  var9 = var10000;
                  if (!"shitted on you harder than archybot".equals("stop skidding")) {
                        ;
                  }

                  var9.generateGlyphPage(var5);
                  var9.setupTexture();
            }

            GlyphPageFontRenderer var11 = new GlyphPageFontRenderer;
            if (!"Some babies were dropped on their heads but you were clearly thrown at a wall.".equals("yo mama name maurice")) {
                  ;
            }

            if (!"buy a domain and everything else you need at namecheap.com".equals("you probably spell youre as your")) {
                  ;
            }

            var11.<init>(var10, var7, var8, var9);
            return var11;
      }

      public String trimStringToWidth(String var1, int var2, boolean var3) {
            StringBuilder var10000 = new StringBuilder;
            if (!"i hope you catch fire ngl".equals("Some babies were dropped on their heads but you were clearly thrown at a wall.")) {
                  ;
            }

            var10000.<init>();
            if (((728850882 | 250722625) >>> 4 ^ -2112264216) != 0) {
                  ;
            }

            StringBuilder var4 = var10000;
            int var5 = (569767559 ^ 230699257 | 18187492) >> 4 ^ 47406191;
            int var6 = var3 ? var1.length() - ((0 >>> 3 ^ 2074314746 | 1126327057) ^ 2074339322) : (1361534747 ^ 1199295070) << 1 ^ 198477962 ^ 661496320;
            int var13;
            if (var3) {
                  var13 = (326574725 ^ 259126945) >>> 2 ^ -117552394;
            } else {
                  if (((1406536 << 2 & 1458811) << 3 ^ 232741708) != 0) {
                        ;
                  }

                  var13 = (0 ^ 1868458974) >>> 4 ^ 116778684;
                  if (!"please get a girlfriend and stop cracking plugins".equals("buy a domain and everything else you need at namecheap.com")) {
                        ;
                  }
            }

            int var7 = var13;
            int var8 = (129929599 << 4 >>> 3 & 186698477 | 174217358) ^ 190995182;

            for(int var10 = var6; var10 >= 0; var10 += var7) {
                  if (!"please go outside".equals("ape covered in human flesh")) {
                        ;
                  }

                  if (var10 >= var1.length() || var10 >= var2) {
                        break;
                  }

                  char var11 = var1.charAt(var10);
                  int var10001;
                  if (var5 != 0 && var11 >= (((15 | 0) ^ 4) & 7 ^ 51) && var11 <= (((99 >> 3 ^ 1) << 2 | 29) & 25 ^ 107)) {
                        if (!"i hope you catch fire ngl".equals("stop skidding")) {
                              ;
                        }

                        int var12 = "0123456789abcdefklmnor".indexOf(var11);
                        if (!"please go outside".equals("idiot")) {
                              ;
                        }

                        if (var12 < (1 & 0 & 1193127024 ^ 16)) {
                              this.boldStyle = (boolean)((1384514215 ^ 605023218) >> 2 >> 1 ^ 248593706);
                              this.italicStyle = (boolean)(((375281921 ^ 228789032) >>> 2 | 107869212) ^ 117437598);
                        } else if (var12 == ((16 ^ 0) >> 3 & 0 ^ 17)) {
                              if (((1462189445 ^ 269351043) << 3 << 4 << 4 ^ 1445474304) == 0) {
                                    ;
                              }

                              this.boldStyle = (boolean)(0 >> 3 >> 3 >> 3 << 1 ^ 1);
                        } else {
                              if (!"ape covered in human flesh".equals("stop skidding")) {
                                    ;
                              }

                              var10001 = (19 & 12 | 1724492570) >>> 3 ^ 215561591;
                              if (((2066463517 >> 2 ^ 178086023) & 150881702 ^ 5637120) == 0) {
                                    ;
                              }

                              if (var12 == var10001) {
                                    this.italicStyle = (boolean)((0 | 1470886461) >> 2 ^ 367721614);
                              } else if (var12 == (((11 >>> 3 | 0) << 3 | 6) ^ 27)) {
                                    this.boldStyle = (boolean)(597178988 >> 2 << 2 << 4 << 2 ^ -435250432);
                                    this.italicStyle = (boolean)(157498244 >> 1 ^ 74078414 ^ 14402828);
                              }
                        }

                        ++var10;
                        var5 = 274060529 >> 1 & 136329856 ^ 136325632;
                        if ((687640243 << 2 >>> 3 & 322695425 ^ 272253185) == 0) {
                              ;
                        }
                  } else {
                        if (var5 != 0) {
                              --var10;
                        }

                        var11 = var1.charAt(var10);
                        GlyphPage var9 = this.getCurrentGlyphPage();
                        float var14 = (float)var8 + (var9.getWidth(var11) - 8.0F) / 2.0F;
                        if (((1698599589 << 1 << 2 | 609414336) ^ 771161576) == 0) {
                              ;
                        }

                        var8 = (int)var14;
                  }

                  if (var10 > var8) {
                        break;
                  }

                  if (var3) {
                        var10001 = (70796168 | 52908006) << 3 ^ 972717936;
                        if (!"buy a domain and everything else you need at namecheap.com".equals("i hope you catch fire ngl")) {
                              ;
                        }

                        var4.insert(var10001, var11);
                  } else {
                        var4.append(var11);
                  }

                  if (!"please take a shower".equals("please get a girlfriend and stop cracking plugins")) {
                        ;
                  }

                  if (!"You're so fat whenever you go to the beach the tide comes in.".equals("you probably spell youre as your")) {
                        ;
                  }
            }

            return var4.toString();
      }

      private GlyphPage getCurrentGlyphPage() {
            if (this.boldStyle && this.italicStyle) {
                  return this.boldItalicGlyphPage;
            } else {
                  if (!"Your family tree must be a cactus because everyone on it is a prick.".equals("ape covered in human flesh")) {
                        ;
                  }

                  if (!"nefariousMoment".equals("stop skidding")) {
                        ;
                  }

                  if (this.boldStyle) {
                        if (((488178741 ^ 181001633 ^ 334934068) << 2 >>> 3 ^ -824060366) != 0) {
                              ;
                        }

                        return this.boldGlyphPage;
                  } else {
                        if ((((228704542 ^ 163598232 ^ 37770977) & 74244388 | 68102197) ^ 194373640) != 0) {
                              ;
                        }

                        if (this.italicStyle) {
                              if (((2017205149 ^ 676174397 ^ 1242986916 | 72900331) & 430872454 ^ -792130682) != 0) {
                                    ;
                              }

                              return this.italicGlyphPage;
                        } else {
                              if (!"You're so fat whenever you go to the beach the tide comes in.".equals("stop skidding")) {
                                    ;
                              }

                              return this.regularGlyphPage;
                        }
                  }
            }
      }

      public int getFontHeight() {
            int var10000 = this.regularGlyphPage.getMaxFontHeight();
            if (((67829792 ^ 58352066) << 2 ^ 499484552) == 0) {
                  ;
            }

            return var10000 / ((0 | 140217085) ^ 127596395 ^ 87160726 ^ 183536130);
      }

      private void doDraw(float var1, GlyphPage var2) {
            Tessellator var3;
            BufferBuilder var4;
            double var8;
            double var10001;
            if (this.strikethroughStyle) {
                  var3 = Tessellator.getInstance();
                  if (!"intentMoment".equals("Some babies were dropped on their heads but you were clearly thrown at a wall.")) {
                        ;
                  }

                  var4 = var3.getBuffer();
                  GlStateManager.disableTexture2D();
                  var4.begin(4 & 1 & 264443018 ^ 7, DefaultVertexFormats.POSITION);
                  var10001 = (double)this.posX;
                  float var10002 = this.posY;
                  int var10003 = var2.getMaxFontHeight() / (1 >> 3 >> 1 ^ 2);
                  if (((1912017712 | 1344913306) ^ 785386621 ^ 1596961735) == 0) {
                        ;
                  }

                  var4.pos(var10001, (double)(var10002 + (float)var10003), 0.0D).endVertex();
                  var10001 = (double)(this.posX + var1);
                  if (!"yo mama name maurice".equals("please dont crack my plugin")) {
                        ;
                  }

                  var10002 = this.posY;
                  if ((897756720 >>> 1 >> 2 & 34131349 ^ 33572228) == 0) {
                        ;
                  }

                  BufferBuilder var10000 = var4.pos(var10001, (double)(var10002 + (float)(var2.getMaxFontHeight() / ((((0 ^ 599875563) >> 3 | 41357025) >>> 2 | 8873357) ^ 27258877))), 0.0D);
                  if ((621457070 >> 1 >> 3 ^ 1249870109) != 0) {
                        ;
                  }

                  var10000.endVertex();
                  float var7 = this.posX + var1;
                  if (!"please dont crack my plugin".equals("please get a girlfriend and stop cracking plugins")) {
                        ;
                  }

                  var4.pos((double)var7, (double)(this.posY + (float)(var2.getMaxFontHeight() / ((0 ^ 1225578859) >> 4 & 38071881 ^ '찂')) - 1.0F), 0.0D).endVertex();
                  var10001 = (double)this.posX;
                  if ((33570818 ^ 31745368 ^ -579998217) != 0) {
                        ;
                  }

                  var10002 = this.posY;
                  var10003 = var2.getMaxFontHeight() / (1 << 4 << 2 & 56 ^ 2);
                  if (((67111168 | 33619265) << 3 << 4 ^ 8298624) == 0) {
                        ;
                  }

                  var10002 += (float)var10003;
                  if (!"Your family tree must be a cactus because everyone on it is a prick.".equals("intentMoment")) {
                        ;
                  }

                  var8 = (double)(var10002 - 1.0F);
                  if (!"Some babies were dropped on their heads but you were clearly thrown at a wall.".equals("you probably spell youre as your")) {
                        ;
                  }

                  var4.pos(var10001, var8, 0.0D).endVertex();
                  var3.draw();
                  GlStateManager.enableTexture2D();
            }

            if (this.underlineStyle) {
                  var3 = Tessellator.getInstance();
                  if ((1716591658 << 4 >> 4 ^ -1166196625) != 0) {
                        ;
                  }

                  if (!"please take a shower".equals("stringer is a good obfuscator")) {
                        ;
                  }

                  var4 = var3.getBuffer();
                  GlStateManager.disableTexture2D();
                  var4.begin((3 & 2 | 0) << 3 << 4 ^ 263, DefaultVertexFormats.POSITION);
                  int var6;
                  if (this.underlineStyle) {
                        var6 = 464200349 >>> 1 >>> 3 ^ -29012522;
                  } else {
                        var6 = ((2037065822 | 396373916) >> 1 | 959355210) << 2 ^ -98372;
                        if ((1929860999 >> 4 << 4 ^ 1310840346 ^ -359880768) != 0) {
                              ;
                        }
                  }

                  int var5 = var6;
                  var4.pos((double)(this.posX + (float)var5), (double)(this.posY + (float)var2.getMaxFontHeight()), 0.0D).endVertex();
                  var4.pos((double)(this.posX + var1), (double)(this.posY + (float)var2.getMaxFontHeight()), 0.0D).endVertex();
                  if ((451557184 >>> 2 ^ 20234607 ^ 126766271) == 0) {
                        ;
                  }

                  var10001 = (double)(this.posX + var1);
                  if (((74883614 | 9458631) ^ 80528890 ^ 1471465247) != 0) {
                        ;
                  }

                  var4.pos(var10001, (double)(this.posY + (float)var2.getMaxFontHeight() - 1.0F), 0.0D).endVertex();
                  var10001 = (double)(this.posX + (float)var5);
                  if (((1181533235 >> 4 & 48811229 ^ 3591140) >> 3 ^ 704756) == 0) {
                        ;
                  }

                  var8 = (double)(this.posY + (float)var2.getMaxFontHeight() - 1.0F);
                  if (((724821388 << 2 >>> 3 | 20304003) & 157539307 ^ -1081138070) != 0) {
                        ;
                  }

                  var4.pos(var10001, var8, 0.0D).endVertex();
                  var3.draw();
                  GlStateManager.enableTexture2D();
            }

            if (((1069408495 >> 3 | 112483142 | 104088817) ^ 133693439) == 0) {
                  ;
            }

            this.posX += var1;
      }
}
