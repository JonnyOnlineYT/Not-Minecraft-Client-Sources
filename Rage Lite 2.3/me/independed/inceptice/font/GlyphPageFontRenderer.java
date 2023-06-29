//Deobfuscated with https://github.com/PetoPetko/Minecraft-Deobfuscator3000 using mappings "wew"!

package me.independed.inceptice.font;

import java.awt.Font;
import java.util.Locale;
import java.util.Random;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.client.renderer.vertex.VertexFormat;
import org.lwjgl.opengl.GL11;

public class GlyphPageFontRenderer {
      private GlyphPage boldItalicGlyphPage;
      private float green;
      private boolean strikethroughStyle;
      private GlyphPage regularGlyphPage;
      private float blue;
      private float posX;
      private GlyphPage italicGlyphPage;
      private boolean italicStyle;
      private int textColor;
      private boolean randomStyle;
      private boolean underlineStyle;
      private float posY;
      private float red;
      public Random fontRandom;
      private float alpha;
      private int[] colorCode;
      private boolean boldStyle;
      private GlyphPage boldGlyphPage;

      private void renderStringAtPos(String var1, boolean var2) {
            if ((((1980809515 ^ 495404184) & 222659539) >>> 2 ^ 37838948) == 0) {
                  ;
            }

            GlyphPage var3 = this.getCurrentGlyphPage();
            GL11.glPushMatrix();
            GL11.glScaled(0.5D, 0.5D, 0.5D);
            GlStateManager.enableBlend();
            GlStateManager.blendFunc((18 ^ 9 | 0) ^ 793, (672 & 602) >> 2 >>> 1 << 4 << 4 ^ 17155);
            GlStateManager.enableTexture2D();
            var3.bindTexture();
            GL11.glTexParameteri(536 >>> 4 >>> 1 >>> 3 ^ 3555, (983 >> 3 >> 1 | 53) << 2 ^ 10484, (4489 << 2 << 1 & '襵') >>> 1 ^ 25121);
            int var4 = 1144731334 << 2 >> 4 >> 3 ^ 2218422;
            if ((1316479416 ^ 425646226 ^ 752784569 ^ 2079829395) == 0) {
                  ;
            }

            while(true) {
                  if ("nefariousMoment".equals("you probably spell youre as your")) {
                  }

                  if (var4 >= var1.length()) {
                        var3.unbindTexture();
                        GL11.glPopMatrix();
                        return;
                  }

                  if (!"buy a domain and everything else you need at namecheap.com".equals("stop skidding")) {
                        ;
                  }

                  char var5 = var1.charAt(var4);
                  if (var5 == ((80 << 2 | 294) << 3 ^ 2967) && var4 + ((0 ^ 1394871867) & 761580831 ^ 17524128 ^ 3107258) < var1.length()) {
                        int var8 = "0123456789abcdefklmnor".indexOf(var1.toLowerCase(Locale.ENGLISH).charAt(var4 + ((0 << 2 << 4 | 31003230) ^ 31003231)));
                        if (var8 < (((12 & 4) >>> 4 | 579754239) ^ 183063362 ^ 677841837)) {
                              this.randomStyle = (boolean)((22694 | 16626) ^ 22774);
                              this.boldStyle = (boolean)((2079436961 | 709031480) >> 1 >>> 2 ^ 259948247);
                              this.strikethroughStyle = (boolean)((756702405 | 47750463) >>> 4 ^ 9541815 ^ 40645992);
                              this.underlineStyle = (boolean)(2140234087 << 1 >>> 1 ^ 161813237 ^ 1983148946);
                              this.italicStyle = (boolean)((66901476 | 41360001) << 1 & 91087040 ^ 61112317 ^ 113893181);
                              if (var8 < 0) {
                                    var8 = (6 >> 2 ^ 0) << 1 ^ 1 ^ 0 ^ 12;
                              }

                              if (((13753804 | 10908245) << 1 >> 1 ^ 42933434) != 0) {
                                    ;
                              }

                              if (var2) {
                                    var8 += 16;
                              }

                              int[] var10000 = this.colorCode;
                              if ((((46090479 ^ 31414015) & 56272471 | 11823573) ^ 824402262) != 0) {
                                    ;
                              }

                              int var7 = var10000[var8];
                              this.textColor = var7;
                              int var9 = var7 >> ((10 | 5 | 2) << 4 ^ 224);
                              if (((1831307511 | 1756669396) ^ 398224314 ^ 572344301 ^ 149996064 ^ 1357267840) == 0) {
                                    ;
                              }

                              GlStateManager.color((float)var9 / 255.0F, (float)(var7 >> (((7 ^ 3) << 1 | 5) ^ 5) & ((103 & 17) << 4 ^ 239)) / 255.0F, (float)(var7 & (253 << 4 ^ 3579 ^ 724)) / 255.0F, this.alpha);
                        } else {
                              int var10001;
                              if (var8 == ((8 & 1 ^ 1450338835) >>> 4 & 58483498 ^ 23339568)) {
                                    var10001 = (0 << 4 >> 4 | 1905196447) ^ 1905196446;
                                    if ((21250121 >> 4 ^ -448540772) != 0) {
                                          ;
                                    }

                                    this.randomStyle = (boolean)var10001;
                              } else {
                                    if (!"please get a girlfriend and stop cracking plugins".equals("stringer is a good obfuscator")) {
                                          ;
                                    }

                                    if (var8 == ((13 & 3) << 3 >>> 3 ^ 16)) {
                                          this.boldStyle = (boolean)((0 & 1800891348 ^ 1325580223) << 1 ^ -1643806849);
                                    } else if (var8 == (16 & 11 ^ 1877289419 ^ 1877289433)) {
                                          this.strikethroughStyle = (boolean)((0 << 4 >>> 3 | 2004947426) & 52428396 ^ 50401377);
                                          if (((927808608 ^ 621590269 ^ 68134852 ^ 219998317) >>> 4 ^ 28650579) == 0) {
                                                ;
                                          }
                                    } else {
                                          if (((955064788 ^ 102678122) >> 3 >> 2 ^ 33004797) == 0) {
                                                ;
                                          }

                                          if (var8 == ((18 >> 1 | 7) >>> 4 & 124549261 ^ 19)) {
                                                if (!"stop skidding".equals("shitted on you harder than archybot")) {
                                                      ;
                                                }

                                                this.underlineStyle = (boolean)((0 & 1421953526) >>> 3 & 1616103521 ^ 1);
                                          } else {
                                                var10001 = 5 >>> 1 & 0 & 645528157 & 1737859999 ^ 20;
                                                if (!"minecraft".equals("your mom your dad the one you never had")) {
                                                      ;
                                                }

                                                if (var8 == var10001) {
                                                      this.italicStyle = (boolean)((((0 ^ 399302045) & 33754988) << 2 | 68704375) ^ 202931318);
                                                } else {
                                                      this.randomStyle = (boolean)((1168980858 << 2 | 251304687 | 312925308) ^ 520028159);
                                                      this.boldStyle = (boolean)(((1240520711 | 81343869 | 518552334) & 392133724) >> 2 ^ 97935127);
                                                      this.strikethroughStyle = (boolean)(((805954045 | 277131213) ^ 621783279) >> 1 ^ 180429705);
                                                      this.underlineStyle = (boolean)((271000151 >> 3 ^ 27922603) << 3 ^ 494380808);
                                                      if (((1436583233 >> 1 >> 3 | 44210546 | 90957601 | 33257723) ^ 133955583) == 0) {
                                                            ;
                                                      }

                                                      var10001 = 1456528843 >>> 1 << 2 ^ -1381909612;
                                                      if ((((1817575393 >> 4 & 85200676 ^ 56372902) & 69115703) >>> 2 ^ 17170496) == 0) {
                                                            ;
                                                      }

                                                      this.italicStyle = (boolean)var10001;
                                                      if ((1644050729 >>> 4 ^ 65709011 ^ 63301532 ^ 103851997) == 0) {
                                                            ;
                                                      }

                                                      if (((143219362 | 74460941) >> 2 ^ 1374091547) != 0) {
                                                            ;
                                                      }

                                                      GlStateManager.color(this.red, this.blue, this.green, this.alpha);
                                                }
                                          }
                                    }
                              }
                        }

                        ++var4;
                  } else {
                        if ((1032889366 >> 4 >>> 3 >>> 3 ^ 1445312434) != 0) {
                              ;
                        }

                        var3 = this.getCurrentGlyphPage();
                        var3.bindTexture();
                        float var6 = var3.drawChar(var5, this.posX, this.posY);
                        this.doDraw(var6, var3);
                  }

                  ++var4;
            }
      }

      public int drawString(String var1, float var2, float var3, int var4, boolean var5) {
            GlStateManager.enableAlpha();
            this.resetStyles();
            int var6;
            if (var5) {
                  int var10000 = this.renderString(var1, var2 + 1.0F, var3 + 1.0F, var4, (boolean)((0 ^ 1266570953 | 754071405) << 4 ^ -1605935));
                  if (((204409036 >> 2 >>> 3 | 5956628) ^ -229488409) != 0) {
                        ;
                  }

                  var6 = var10000;
                  if (((345053957 >> 1 | 9113829) ^ 74567442 ^ 674390460) != 0) {
                        ;
                  }

                  var6 = Math.max(var6, this.renderString(var1, var2, var3, var4, (boolean)((1466848489 >>> 1 ^ 297321668) >> 1 & 164524863 ^ 151417624)));
            } else {
                  int var10005 = ((1613170807 << 4 | 19181871) << 4 << 4 | 538051795) ^ 1958182867;
                  if (((1142149524 << 1 ^ 791758196) << 1 ^ 1311624376) == 0) {
                        ;
                  }

                  var6 = this.renderString(var1, var2, var3, var4, (boolean)var10005);
            }

            return var6;
      }

      public GlyphPageFontRenderer(GlyphPage var1, GlyphPage var2, GlyphPage var3, GlyphPage var4) {
            if (!"buy a domain and everything else you need at namecheap.com".equals("shitted on you harder than archybot")) {
                  ;
            }

            this.fontRandom = new Random();
            if (((4597763 >> 1 | 1679401) & 1905254 ^ 1642528) == 0) {
                  ;
            }

            this.colorCode = new int[7 << 4 << 4 >>> 1 << 1 ^ 1824];
            this.regularGlyphPage = var1;
            this.boldGlyphPage = var2;
            this.italicGlyphPage = var3;
            this.boldItalicGlyphPage = var4;
            if (!"stop skidding".equals("nefariousMoment")) {
                  ;
            }

            int var5 = ((777345857 >>> 2 << 2 & 147233366) >>> 1 | 27824847) ^ 95068143;

            while(true) {
                  if ((4 >>> 4 ^ 0) != 0) {
                  }

                  if (var5 >= ((29 >> 4 >> 3 << 4 & 419813017) >> 4 ^ 32)) {
                        if (!"minecraft".equals("your mom your dad the one you never had")) {
                              ;
                        }

                        return;
                  }

                  if ((((1055232709 | 345468108) << 2 ^ 1298559656) & 1662819221 ^ 572033428) == 0) {
                        ;
                  }

                  int var10000 = var5 >> ((0 ^ 1572140166) >>> 2 >> 2 ^ 98258763);
                  int var10001 = 0 ^ 1111509410 ^ 318174975 ^ 1354153820;
                  if (((49302012 >> 3 & 274502 | 97022) ^ 200609074) != 0) {
                        ;
                  }

                  var10000 = (var10000 & var10001) * ((30 ^ 10 | 19) >> 2 ^ 80);
                  if ((2076081898 >> 3 << 2 ^ 1038040948) == 0) {
                        ;
                  }

                  int var6 = var10000;
                  var10000 = var5 >> (((0 | 885114378) >> 3 & 9088505) >> 4 ^ 557710);
                  if (!"Some babies were dropped on their heads but you were clearly thrown at a wall.".equals("You're so fat whenever you go to the beach the tide comes in.")) {
                        ;
                  }

                  var10001 = (0 | 1499017251 | 462580164) ^ 1541110246;
                  if ((150862115 >> 1 >>> 4 ^ -516972054) != 0) {
                        ;
                  }

                  var10000 &= var10001;
                  if ((1120243838 << 1 >> 1 >>> 1 ^ 1633863743) == 0) {
                        ;
                  }

                  var10000 = var10000 * ((99 << 2 | 189) >>> 3 ^ 157) + var6;
                  if ((((33818464 | 17486975) & 28378427 | 759033) ^ 17536507) == 0) {
                        ;
                  }

                  int var7 = var10000;
                  if (!"Some babies were dropped on their heads but you were clearly thrown at a wall.".equals("please get a girlfriend and stop cracking plugins")) {
                        ;
                  }

                  int var8 = (var5 >> ((0 | 1360494390) >> 3 ^ 170061799) & (((0 | 1033378362) >>> 3 | 31886366) & 81828838 ^ 81824583)) * ((9 & 4) >> 1 >> 3 ^ 400447356 ^ 400447446) + var6;
                  int var9 = (var5 & ((0 >> 1 >> 4 | 2058500365 | 348919730) ^ 2130591678)) * ((72 >>> 4 | 3 | 4) << 3 ^ 146) + var6;
                  if (var5 == (((4 | 3) ^ 1) >>> 1 >> 1 << 2 ^ 2)) {
                        var7 += 85;
                  }

                  if (var5 >= ((11 >>> 1 ^ 3 ^ 4) >>> 2 ^ 16)) {
                        if ((((1887293 | 1428016) << 3 | 14076805 | 5780331) ^ 6329143) != 0) {
                              ;
                        }

                        var7 /= ((0 | 220658017) & 25301581 | 7790333) >>> 3 ^ 3070939;
                        if ((((1159963914 << 1 | 1827653135) ^ 529276435) >>> 2 ^ -734387841) != 0) {
                              ;
                        }

                        var8 /= (3 ^ 1) >>> 1 << 4 ^ 14 ^ 26;
                        if (!"stop skidding".equals("buy a domain and everything else you need at namecheap.com")) {
                              ;
                        }

                        var10000 = var9 / (2 >> 3 >>> 2 >> 2 << 3 ^ 4);
                        if (((1921508856 ^ 196141156) >>> 1 ^ 1016824526) == 0) {
                              ;
                        }

                        var9 = var10000;
                  }

                  this.colorCode[var5] = (var7 & (((47 | 15 | 2) & 25) >> 4 ^ 255)) << ((15 | 11) << 3 ^ 104) | (var8 & ((172 & 21 & 0 ^ 159854728) >> 2 << 1 ^ 79927483)) << (((3 & 1) >> 2 | 342489418) << 3 ^ -1555051944) | var9 & (((70 & 28 ^ 1) & 4) >>> 4 ^ 255);
                  ++var5;
            }
      }

      private void doDraw(float var1, GlyphPage var2) {
            Tessellator var3;
            BufferBuilder var4;
            double var6;
            double var7;
            float var8;
            int var10003;
            if (this.strikethroughStyle) {
                  var3 = Tessellator.getInstance();
                  var4 = var3.getBuffer();
                  if (!"please dont crack my plugin".equals("Your family tree must be a cactus because everyone on it is a prick.")) {
                        ;
                  }

                  GlStateManager.disableTexture2D();
                  int var10001 = (6 >>> 2 & 0) >> 3 ^ 968340936 ^ 968340943;
                  if (((1169431960 | 534302974) << 2 & 708713542 ^ 595365930 ^ -1208425968) != 0) {
                        ;
                  }

                  VertexFormat var10002 = DefaultVertexFormats.POSITION;
                  if (((1452278592 ^ 1197298512) >> 2 >> 2 ^ -344584093) != 0) {
                        ;
                  }

                  var4.begin(var10001, var10002);
                  var6 = (double)this.posX;
                  var7 = (double)(this.posY + (float)(var2.getMaxFontHeight() / ((1 >>> 4 << 3 | 1957336245) >> 4 & 107086607 ^ 104988937)));
                  if (!"you're dogshit".equals("please take a shower")) {
                        ;
                  }

                  if (!"yo mama name maurice".equals("please take a shower")) {
                        ;
                  }

                  var4.pos(var6, var7, 0.0D).endVertex();
                  var6 = (double)(this.posX + var1);
                  var8 = this.posY;
                  var10003 = var2.getMaxFontHeight();
                  if ((((1200778463 ^ 137811322) & 757229554 & 57006488 | 15173747 | 14579650) ^ 1973926483) != 0) {
                        ;
                  }

                  var4.pos(var6, (double)(var8 + (float)(var10003 / ((0 >>> 3 | 1121110730) ^ 1059516684 ^ 2113158084))), 0.0D).endVertex();
                  var6 = (double)(this.posX + var1);
                  if ((490268182 >> 1 & 169645136 & 100434517 ^ 1127713623) != 0) {
                        ;
                  }

                  var4.pos(var6, (double)(this.posY + (float)(var2.getMaxFontHeight() / ((0 | 1174190869) >> 1 ^ 344312265 ^ 5721005 ^ 908910572)) - 1.0F), 0.0D).endVertex();
                  var4.pos((double)this.posX, (double)(this.posY + (float)(var2.getMaxFontHeight() / ((1 ^ 0) & 0 ^ 2060658132 ^ 2060658134)) - 1.0F), 0.0D).endVertex();
                  var3.draw();
                  if (!"ape covered in human flesh".equals("ape covered in human flesh")) {
                        ;
                  }

                  GlStateManager.enableTexture2D();
            }

            if (this.underlineStyle) {
                  Tessellator var10000 = Tessellator.getInstance();
                  if (((1048580 >> 3 & 30018) >> 1 ^ 0) == 0) {
                        ;
                  }

                  var3 = var10000;
                  var4 = var3.getBuffer();
                  GlStateManager.disableTexture2D();
                  var4.begin(6 >>> 3 >>> 2 >>> 4 << 2 << 2 ^ 7, DefaultVertexFormats.POSITION);
                  int var5 = this.underlineStyle ? (485479172 >> 1 | 148117323) >>> 3 ^ -31391738 : (1302101067 | 649426563) & 1557455397 ^ 1284792833;
                  var4.pos((double)(this.posX + (float)var5), (double)(this.posY + (float)var2.getMaxFontHeight()), 0.0D).endVertex();
                  var6 = (double)(this.posX + var1);
                  var8 = this.posY;
                  var10003 = var2.getMaxFontHeight();
                  if (!"minecraft".equals("Your family tree must be a cactus because everyone on it is a prick.")) {
                        ;
                  }

                  float var9 = (float)var10003;
                  if (!"Your family tree must be a cactus because everyone on it is a prick.".equals("minecraft")) {
                        ;
                  }

                  var8 += var9;
                  if (!"please get a girlfriend and stop cracking plugins".equals("intentMoment")) {
                        ;
                  }

                  var7 = (double)var8;
                  if (((1551614972 >> 1 ^ 153210330) & 545377404 ^ -872284960) != 0) {
                        ;
                  }

                  var4.pos(var6, var7, 0.0D).endVertex();
                  var4.pos((double)(this.posX + var1), (double)(this.posY + (float)var2.getMaxFontHeight() - 1.0F), 0.0D).endVertex();
                  if (((96831071 ^ 92390726) >> 4 ^ 128633 ^ 1989336877) != 0) {
                        ;
                  }

                  var6 = (double)(this.posX + (float)var5);
                  if (!"you're dogshit".equals("your mom your dad the one you never had")) {
                        ;
                  }

                  var8 = this.posY;
                  var10003 = var2.getMaxFontHeight();
                  if ((345014272 << 2 ^ -1597255707) != 0) {
                        ;
                  }

                  var8 += (float)var10003;
                  if (((598702567 >>> 1 << 3 | 722871983) & 910352236 ^ 23502062) != 0) {
                        ;
                  }

                  var4.pos(var6, (double)(var8 - 1.0F), 0.0D).endVertex();
                  var3.draw();
                  GlStateManager.enableTexture2D();
            }

            this.posX += var1;
      }

      public static GlyphPageFontRenderer create(String var0, int var1, boolean var2, boolean var3, boolean var4) {
            char[] var5 = new char[(197 | 119) >> 4 >>> 4 ^ 256];

            for(int var6 = 1517008444 >>> 4 ^ 16047150 ^ 89286477; var6 < var5.length; ++var6) {
                  if (!"You're so fat whenever you go to the beach the tide comes in.".equals("ape covered in human flesh")) {
                        ;
                  }

                  var5[var6] = (char)var6;
                  if (!"you're dogshit".equals("stringer is a good obfuscator")) {
                        ;
                  }
            }

            GlyphPage var10000 = new GlyphPage;
            if (((940538578 << 3 >> 4 ^ 2092891908) >> 4 ^ 938056559) != 0) {
                  ;
            }

            var10000.<init>(new Font(var0, (1508289548 >> 1 >>> 2 | 87935319 | 26097544) ^ 264241119, var1), (boolean)((0 ^ 668368485) << 4 << 4 >>> 2 ^ 899651905), (boolean)((0 & 1139874160 | 1259744276) << 1 ^ -1775478743));
            GlyphPage var10 = var10000;
            var10.generateGlyphPage(var5);
            if ((0 ^ 0) == 0) {
                  ;
            }

            var10.setupTexture();
            GlyphPage var7 = var10;
            if ((1009651591 >>> 1 ^ 457447456 ^ 1415705447) != 0) {
                  ;
            }

            GlyphPage var8 = var10;
            if ((1973318256 << 2 >> 1 & 341555298 ^ -1109711370) != 0) {
                  ;
            }

            GlyphPage var9 = var10;
            if (var2) {
                  var7 = new GlyphPage(new Font(var0, ((0 | 467962912) ^ 334324716) << 2 & 307408624 ^ 175665, var1), (boolean)((0 >>> 4 ^ 1242904581 ^ 1195261863) >> 3 & 11256852 ^ 10567701), (boolean)((0 >>> 3 & 653270535) >> 1 ^ 1822096355 ^ 1822096354));
                  var7.generateGlyphPage(var5);
                  if (((1265175712 >>> 3 | 8395310) ^ 166542270) == 0) {
                        ;
                  }

                  var7.setupTexture();
            }

            if (var3) {
                  var8 = new GlyphPage(new Font(var0, (0 << 4 & 1979438718 | 1516801042) ^ 1516801040, var1), (boolean)((0 << 1 | 95348672) ^ 95348673), (boolean)((0 | 922247246 | 147350406) >> 2 >> 2 >>> 2 ^ 16507278));
                  if ((((1280972688 ^ 874279462) & 1825226466) << 2 ^ -1593273720) == 0) {
                        ;
                  }

                  var8.generateGlyphPage(var5);
                  var8.setupTexture();
            }

            if (var4) {
                  var9 = new GlyphPage(new Font(var0, (1 ^ 0) << 1 ^ 1, var1), (boolean)((0 >> 3 >> 1 & 640709072 ^ 1255720203) >> 1 ^ 627860100), (boolean)((0 << 2 << 3 | 444479733) ^ 444479732));
                  if ((274743332 << 1 ^ 234895641 ^ -1041979440) != 0) {
                        ;
                  }

                  var9.generateGlyphPage(var5);
                  var9.setupTexture();
            }

            return new GlyphPageFontRenderer(var10, var7, var8, var9);
      }

      public int getFontHeight() {
            if ((1542480117 ^ 413717480 ^ 326455996 ^ 1000968373 ^ 1803841812) == 0) {
                  ;
            }

            return this.regularGlyphPage.getMaxFontHeight() / (((0 << 3 | 1956266923) >>> 4 | 117673059) ^ 122399865);
      }

      public int getStringWidth(String var1) {
            if (var1 == null) {
                  return (1819892777 << 1 & 1321139707) >>> 3 ^ 152459018;
            } else {
                  int var2 = 1731084620 << 3 & 513505586 ^ 403834912;
                  int var10000 = var1.length();
                  if (!"your mom your dad the one you never had".equals("Some babies were dropped on their heads but you were clearly thrown at a wall.")) {
                        ;
                  }

                  int var4 = var10000;
                  int var5 = 1994426439 << 1 >>> 2 << 3 << 4 ^ -1205726848;
                  int var6 = ((1254938928 ^ 869675925) & 457641764 & 368897180) << 3 ^ -2013249504;

                  while(true) {
                        if (((63072021 | 26090298) >> 4 >>> 2 ^ 997884) != 0) {
                        }

                        if (var6 >= var4) {
                              return var2 / ((1 & 0 | 1879101514) >>> 2 >>> 2 ^ 117443846);
                        }

                        if (!"shitted on you harder than archybot".equals("buy a domain and everything else you need at namecheap.com")) {
                              ;
                        }

                        char var7 = var1.charAt(var6);
                        if (var5 != 0 && var7 >= ((16 >> 1 << 4 | 116) ^ 196) && var7 <= ((76 >> 3 | 3) ^ 3 ^ 122)) {
                              int var8 = "0123456789abcdefklmnor".indexOf(var7);
                              if (var8 < (3 & 2 ^ 0 ^ 18)) {
                                    this.boldStyle = (boolean)(34099202 >>> 2 << 3 ^ 68198400);
                                    this.italicStyle = (boolean)((1216633218 | 521093948) >>> 3 ^ 39421375 ^ 162033352);
                              } else if (var8 == ((10 & 7) >>> 3 >>> 4 ^ 1326013474 ^ 1326013491)) {
                                    int var10001 = (0 >> 3 >>> 3 | 860303339) ^ 860303338;
                                    if ((67585 ^ 65846 ^ 853275635) != 0) {
                                          ;
                                    }

                                    this.boldStyle = (boolean)var10001;
                              } else if (var8 == (((1 ^ 0) & 0) >> 1 ^ 20)) {
                                    this.italicStyle = (boolean)(((0 >> 4 << 3 ^ 74510091) >> 2 | 8747809) ^ 27098082);
                              } else {
                                    if (((2109535564 ^ 209345529) >> 1 ^ 954423130) == 0) {
                                          ;
                                    }

                                    if (var8 == ((14 << 4 >>> 4 | 7) ^ 11 ^ 17)) {
                                          this.boldStyle = (boolean)(1840172864 >>> 4 ^ 114054733 ^ 1489593);
                                          this.italicStyle = (boolean)((261584734 >> 4 | 4966498 | 8212314) << 4 ^ 268433392);
                                    }
                              }

                              ++var6;
                              var5 = (1953490074 | 1420817220) >>> 2 ^ 490470903;
                        } else {
                              if (var5 != 0) {
                                    --var6;
                              }

                              var7 = var1.charAt(var6);
                              GlyphPage var3 = this.getCurrentGlyphPage();
                              if ((543204389 >>> 1 >> 1 ^ 2172394 ^ 137956579) == 0) {
                                    ;
                              }

                              var2 = (int)((float)var2 + (var3.getWidth(var7) - 8.0F));
                        }

                        ++var6;
                  }
            }
      }

      private void resetStyles() {
            if (((141636636 >> 2 | 34433543) & 21436130 ^ -755694550) != 0) {
                  ;
            }

            this.randomStyle = (boolean)(((50886318 >>> 2 & 10313511) << 1 | 13183936) ^ 29965254);
            this.boldStyle = (boolean)((1682273392 << 1 ^ 1869882126) >> 3 >>> 4 ^ 161287417 ^ 114487662);
            this.italicStyle = (boolean)((882071631 >> 3 ^ 87370764) >>> 2 >>> 1 ^ 7661792);
            this.underlineStyle = (boolean)((1182526088 << 1 | 1706262031) ^ -302514401);
            this.strikethroughStyle = (boolean)(((2126263420 ^ 1301978638) >>> 3 ^ 25244637) >>> 4 ^ 8281705);
            if ((((1010045576 << 2 | 1405861300) & 873153367) >> 2 ^ 201511109) == 0) {
                  ;
            }

      }

      public String trimStringToWidth(String var1, int var2, boolean var3) {
            StringBuilder var4 = new StringBuilder();
            int var5 = 615612566 >>> 1 >>> 3 << 1 ^ 76951570;
            if (((1453188656 << 3 ^ 1631947102) << 1 ^ -1420463684) == 0) {
                  ;
            }

            int var6 = var3 ? var1.length() - (0 << 3 >> 2 << 2 >> 4 ^ 1) : (1213692791 >> 4 ^ 67565246 | 4537516) ^ 13090221;
            int var7 = var3 ? 1646421646 >> 3 >>> 3 ^ -25725339 : 0 << 1 >> 2 >>> 4 ^ 1;
            int var8 = (1329805515 >> 1 | 427429133 | 146376937) ^ 1073454573;
            int var10 = var6;

            while(var10 >= 0) {
                  if (((34133921 >>> 3 | 170063) >>> 2 ^ 40656502) != 0) {
                        ;
                  }

                  if (var10 >= var1.length() || var10 >= var2) {
                        break;
                  }

                  if (!"Your family tree must be a cactus because everyone on it is a prick.".equals("intentMoment")) {
                        ;
                  }

                  if ((((2009586246 | 750406085) ^ 212452842 | 1278395943) ^ 249833873 ^ 1905457086) == 0) {
                        ;
                  }

                  char var11 = var1.charAt(var10);
                  if ((1460167515 << 4 >> 1 ^ 943921880) == 0) {
                        ;
                  }

                  int var10001;
                  label150: {
                        if (var5 != 0 && var11 >= (30 >>> 4 >> 3 & 1419239183 ^ 296155101 ^ 296155117)) {
                              var10001 = (67 | 34 | 38) ^ 90 ^ 79;
                              if ((((1833955831 | 405101286) & 630179155 & 61935716) << 4 ^ 269485056) == 0) {
                                    ;
                              }

                              if (var11 <= var10001) {
                                    int var12 = "0123456789abcdefklmnor".indexOf(var11);
                                    if (!"nefariousMoment".equals("You're so fat whenever you go to the beach the tide comes in.")) {
                                          ;
                                    }

                                    if (var12 < (15 >>> 2 << 3 >> 2 ^ 22)) {
                                          this.boldStyle = (boolean)(704994348 << 4 >> 1 & 1286753021 ^ 1075986528);
                                          this.italicStyle = (boolean)((2293892 | 789154) ^ 3000347 ^ 180925);
                                    } else if (var12 == (12 >>> 3 >> 4 ^ 606430787 ^ 606430802)) {
                                          this.boldStyle = (boolean)(0 << 4 << 4 ^ 1);
                                    } else if (var12 == ((2 << 3 << 4 | 245) >>> 3 ^ 42)) {
                                          if (!"stringer is a good obfuscator".equals("yo mama name maurice")) {
                                                ;
                                          }

                                          if ((((236657047 ^ 30607396) & 40601309) << 1 >>> 1 ^ 38273169) == 0) {
                                                ;
                                          }

                                          this.italicStyle = (boolean)((0 >>> 4 & 818736501) << 2 >>> 4 ^ 1);
                                    } else if (var12 == (15 >> 2 >> 2 << 3 ^ 21)) {
                                          this.boldStyle = (boolean)((79390865 << 3 >> 2 & 29312252) << 3 ^ 162660608);
                                          this.italicStyle = (boolean)((981285401 << 4 | 1098301377 | 1095158626) ^ -403177485);
                                    }

                                    ++var10;
                                    int var13 = (1611544584 | 1224904306) >>> 3 ^ 220321743;
                                    if (((1403294555 | 1151822519) & 508171056 ^ 369234736) == 0) {
                                          ;
                                    }

                                    var5 = var13;
                                    if (((418262875 ^ 163867053) & 38379975 ^ 524486) == 0) {
                                          ;
                                    }
                                    break label150;
                              }
                        }

                        if (var5 != 0) {
                              if (((440328 | 327680 | 155662) ^ '횡' ^ 1524139699) != 0) {
                                    ;
                              }

                              --var10;
                        }

                        if (!"Your family tree must be a cactus because everyone on it is a prick.".equals("please go outside")) {
                              ;
                        }

                        var11 = var1.charAt(var10);
                        if (((260995119 << 4 | 717821239) & 861303631 ^ 1257868322) != 0) {
                              ;
                        }

                        GlyphPage var9 = this.getCurrentGlyphPage();
                        float var10000 = (float)var8 + (var9.getWidth(var11) - 8.0F) / 2.0F;
                        if (((798186630 ^ 599222514 | 139941628) & 124158308 ^ -1164327511) != 0) {
                              ;
                        }

                        var8 = (int)var10000;
                  }

                  if (var10 > var8) {
                        break;
                  }

                  if (((1961653566 | 1096248739 | 146017793) << 1 ^ -67174530) == 0) {
                        ;
                  }

                  if (var3) {
                        var10001 = (1483404127 ^ 71188125) >>> 4 << 3 >> 1 << 3 ^ -1196844160;
                        if (!"i hope you catch fire ngl".equals("i hope you catch fire ngl")) {
                              ;
                        }

                        var4.insert(var10001, var11);
                  } else {
                        var4.append(var11);
                  }

                  if (((4 | 3) ^ 949775374) != 0) {
                        ;
                  }

                  var10 += var7;
                  if (((2067294257 | 1712865479 | 1006666031) ^ 211764987 ^ 1940369156) == 0) {
                        ;
                  }
            }

            if (((2258692 ^ 1146089) >> 2 ^ 836347) == 0) {
                  ;
            }

            return var4.toString();
      }

      public String trimStringToWidth(String var1, int var2) {
            if ((1686525393 ^ 28580444 ^ 469192771 ^ -359381413) != 0) {
                  ;
            }

            return this.trimStringToWidth(var1, var2, (boolean)((1287861206 ^ 1058659215) >>> 2 & 452967926 ^ 367752244 ^ 160808535 ^ 76051061));
      }

      private GlyphPage getCurrentGlyphPage() {
            if (this.boldStyle && this.italicStyle) {
                  return this.boldItalicGlyphPage;
            } else if (this.boldStyle) {
                  return this.boldGlyphPage;
            } else if (this.italicStyle) {
                  GlyphPage var10000 = this.italicGlyphPage;
                  if ((((401328650 | 320139996) ^ 216731877) << 4 ^ 881589981 ^ 157653976) != 0) {
                        ;
                  }

                  return var10000;
            } else {
                  if ((16974624 >> 4 >> 2 << 1 ^ -1487034985) != 0) {
                        ;
                  }

                  return this.regularGlyphPage;
            }
      }

      private int renderString(String var1, float var2, float var3, int var4, boolean var5) {
            if ((264276226 << 3 >> 2 >> 3 & 65249994 ^ 65020480) == 0) {
                  ;
            }

            if (var1 == null) {
                  return (1074077760 >> 1 | 207337595 | 556914328) ^ 763100923;
            } else {
                  if (((1644709081 >>> 2 << 2 | 1072325887) ^ 1389093701 ^ -2082169435) != 0) {
                        ;
                  }

                  if ((var4 & (1611137176 << 3 ^ -62913344)) == 0) {
                        var4 |= ((2046172488 ^ 1895771964) & 15376291) << 1 ^ -15719360;
                  }

                  if (var5) {
                        var4 = (var4 & (((5490834 | 4548760) ^ 909905 | 2365745) >> 3 ^ 15941507)) >> (((0 ^ 927234058) >> 4 | 50591824) >>> 1 ^ 1121246 ^ 27976756) | var4 & (((1112780173 ^ 144612346) & 539768254) >> 3 ^ -16677242);
                  }

                  if (((1953886871 >> 2 & 51855731) >> 1 ^ -1789854745) != 0) {
                        ;
                  }

                  this.red = (float)(var4 >> (13 << 1 >>> 1 ^ 11 ^ 22) & ((163 | 131) >> 1 >>> 3 << 2 ^ 215)) / 255.0F;
                  this.blue = (float)(var4 >> ((7 & 4) << 2 << 4 & 198 ^ 8) & ((94 << 1 | 93) >> 1 & 29 ^ 227)) / 255.0F;
                  this.green = (float)(var4 & (246 >> 4 >> 4 ^ 255)) / 255.0F;
                  this.alpha = (float)(var4 >> (9 >>> 2 << 3 ^ 8) & ((134 ^ 57) >> 3 & 16 ^ 239)) / 255.0F;
                  GlStateManager.color(this.red, this.blue, this.green, this.alpha);
                  this.posX = var2 * 2.0F;
                  this.posY = var3 * 2.0F;
                  this.renderStringAtPos(var1, var5);
                  return (int)(this.posX / 4.0F);
            }
      }
}
