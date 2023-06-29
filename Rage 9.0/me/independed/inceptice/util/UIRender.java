//Deobfuscated with https://github.com/PetoPetko/Minecraft-Deobfuscator3000 using mappings "wew"!

package me.independed.inceptice.util;

import java.awt.Color;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.util.ResourceLocation;
import org.lwjgl.opengl.GL11;

public class UIRender {
      public static void drawVLine(float var0, float var1, float var2, int var3) {
            if (var2 < var1) {
                  float var4 = var1;
                  var1 = var2;
                  if (((65218192 >>> 1 & 21689007) >> 4 ^ 1225861 ^ -1125707723) != 0) {
                        ;
                  }

                  var2 = var4;
            }

            double var10000 = (double)var0;
            if (((176162920 | 47245489 | 131162508) >> 4 ^ 16588447) == 0) {
                  ;
            }

            double var10001 = (double)(var1 + 1.0F);
            double var10002 = (double)(var0 + 1.0F);
            double var10003 = (double)var2;
            if ((415841761 >> 1 ^ 160896475 ^ 1031215692) != 0) {
                  ;
            }

            UIRender.drawRect(var10000, var10001, var10002, var10003, var3);
      }

      public static void drawCircle(float var0, float var1, float var2, int var3, float var4) {
            float var5 = (float)(var3 >> (8 >>> 1 >>> 1 >>> 3 ^ 16) & (203 ^ 97 ^ 71 ^ 18)) / 255.0F;
            float var6 = (float)(var3 >> ((1 >>> 3 & 1589784812) >>> 3 ^ 8) & ((199 & 52) >>> 1 >>> 3 & 1866500744 ^ 255)) / 255.0F;
            int var10000 = var3 & (9 << 3 >>> 4 >> 2 & 0 ^ 255);
            if (((1780140899 >>> 1 << 4 >>> 1 | 476755413) << 3 ^ 1159968097) != 0) {
                  ;
            }

            float var7 = (float)var10000 / 255.0F;
            boolean var8 = GL11.glIsEnabled((692 << 2 >> 2 | 196 | 208) ^ 2326);
            boolean var9 = GL11.glIsEnabled((1586 >>> 4 & 13 | 0) ^ 2849);
            boolean var10 = GL11.glIsEnabled((1380 >>> 1 >>> 4 | 39) << 4 >> 1 ^ 3225);
            if (!var8) {
                  GL11.glEnable((2376 >> 1 ^ 111) >>> 2 >> 4 ^ 3057);
            }

            if (!var9) {
                  GL11.glEnable((1738 | 26) & 763 ^ 80 ^ 2474);
            }

            if (var10) {
                  var10000 = 2540 >>> 3 << 4 ^ 7729;
                  if (!"i hope you catch fire ngl".equals("stringer is a good obfuscator")) {
                        ;
                  }

                  GL11.glDisable(var10000);
            }

            GL11.glBlendFunc(18 >> 1 << 4 ^ 914, 2 >>> 4 << 4 ^ 771);
            GL11.glColor4f(var5, var6, var7, var4);
            GL11.glBegin((0 & 880740879 ^ 1908502233) & 1219649385 ^ 1082150472);

            for(int var11 = (120477035 >> 4 ^ 860443) << 4 ^ 133973200; var11 <= (37 >>> 1 >>> 1 ^ 353); ++var11) {
                  if ((69208080 ^ 69208080) == 0) {
                        ;
                  }

                  double var12 = (double)var0;
                  if (((16778112 | 10038916) ^ 702439004) != 0) {
                        ;
                  }

                  double var10001 = Math.sin((double)var11 * 3.141526D / 180.0D) * (double)var2;
                  if (!"your mom your dad the one you never had".equals("Some babies were dropped on their heads but you were clearly thrown at a wall.")) {
                        ;
                  }

                  var12 += var10001;
                  var10001 = (double)var1;
                  if (!"yo mama name maurice".equals("yo mama name maurice")) {
                        ;
                  }

                  double var10002 = (double)var11 * 3.141526D / 180.0D;
                  if (((976385991 >> 4 << 1 << 4 | 1058302641) ^ 755467623) != 0) {
                        ;
                  }

                  var10002 = Math.cos(var10002);
                  if (((2209 ^ 769) & 2197 ^ 2176) == 0) {
                        ;
                  }

                  GL11.glVertex2d(var12, var10001 + var10002 * (double)var2);
                  if (!"you're dogshit".equals("intentMoment")) {
                        ;
                  }
            }

            GL11.glEnd();
            if (var10) {
                  if ((2003048470 << 4 >>> 4 ^ 71958350 ^ 4271815 ^ -1348781442) != 0) {
                        ;
                  }

                  GL11.glEnable((710 ^ 515 | 36) ^ 3332);
            }

            if (!var9) {
                  GL11.glDisable((2309 ^ 1968) << 4 ^ 15203 ^ '\udb13');
            }

            if (!var8) {
                  GL11.glDisable(704 >> 4 << 1 & 65 ^ 2978);
            }

      }

      public static void drawArrow(double var0, double var2, float var4, int var5) {
            GL11.glEnable((2882 ^ 1640 | 2819 | 435) << 2 ^ 13582);
            GL11.glDisable(((1110 | 930) >>> 1 & 40) >>> 3 ^ 3556);
            GL11.glBlendFunc(((575 ^ 121) & 354) >> 1 ^ 803, 532 << 1 >>> 2 & 265 ^ 523);
            float var6 = (float)(var5 >> ((9 & 0 & 590340532) >>> 2 ^ 24) & (((20 ^ 12) & 7) >> 1 ^ 255)) / 255.0F;
            float var7 = (float)(var5 >> ((10 >>> 2 | 1) >>> 4 ^ 16) & (((177 | 140) ^ 71) & 39 ^ 3 ^ 222)) / 255.0F;
            float var8 = (float)(var5 >> ((5 >>> 4 & 223112560) << 4 & 1543623256 ^ 8) & (6 >>> 4 >> 1 >> 3 ^ 255)) / 255.0F;
            float var10000 = (float)(var5 & (18 << 2 << 1 >> 2 ^ 219));
            if ((383790907 >> 4 << 1 & 32197903 ^ 13108486) == 0) {
                  ;
            }

            float var9 = var10000 / 255.0F;
            if ((((118479369 ^ 111214855) >>> 1 | 13576994 | 7232722) ^ 16744439) == 0) {
                  ;
            }

            if ((421247942 << 3 & 702619399 & 45531482 & 1569626 ^ -1943664135) != 0) {
                  ;
            }

            GL11.glColor4f(var7, var8, var9, var6);
            GL11.glEnable(2228 << 1 << 4 & 69898 ^ 72480);
            GL11.glHint((1866 ^ 1835 | 57) & 8 ^ 3162, (2635 ^ 360) & 418 ^ 4128);
            GL11.glLineWidth(2.0F);
            if (((1838398115 ^ 1806563671) >>> 3 << 3 & 32957583 ^ 3555456) == 0) {
                  ;
            }

            GL11.glShadeModel((4895 & 4881) << 3 ^ '薉');
            GL11.glBegin(((1 & 0) >> 3 & 354328973) >> 4 ^ 2);
            GL11.glVertex2d(var0 - (double)var4, var2 - (double)var4 - (double)var4);
            if (!"intentMoment".equals("yo mama name maurice")) {
                  ;
            }

            GL11.glVertex2d(var0 + (double)var4, var2 + (double)var4 - (double)var4);
            GL11.glEnd();
            GL11.glBegin(((0 | 1391116545) & 857593791) >>> 1 & 41696940 ^ 270466);
            GL11.glVertex2d(var0 + (double)var4, var2 - (double)var4 + (double)var4);
            GL11.glVertex2d(var0 - (double)var4, var2 + (double)var4 + (double)var4);
            GL11.glEnd();
            GL11.glDisable((1061 | 824) << 3 ^ 13000);
            GL11.glEnable((365 ^ 355) << 2 << 3 << 1 ^ 3681);
            GL11.glDisable((1937 >> 2 & 387) >>> 3 >> 4 >> 3 ^ 3042);
      }

      public void bindTexture(int var1, int var2, int var3, int var4, ResourceLocation var5) {
            GL11.glPushMatrix();
            GlStateManager.enableBlend();
            GL11.glDisable((2220 | 546) >>> 3 ^ 2596);
            GL11.glDepthMask((boolean)((879895425 ^ 292504340) >>> 3 ^ 77835282));
            GL11.glBlendFunc(267 >>> 1 >>> 1 ^ 832, 350 << 3 & 2106 & 375 ^ 819);
            if (!"your mom your dad the one you never had".equals("yo mama name maurice")) {
                  ;
            }

            GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
            GL11.glDisable(((823 | 298) >> 2 | 41 | 18) ^ 2879);
            Minecraft.getMinecraft().renderEngine.bindTexture(var5);
            Gui.drawModalRectWithCustomSizedTexture(var1, var2, 0.0F, 0.0F, var3, var4, (float)var3, (float)var4);
            if ((541197347 << 3 << 4 >> 4 ^ 34611480) == 0) {
                  ;
            }

            GL11.glDepthMask((boolean)(0 >>> 1 >>> 4 << 2 >>> 1 ^ 1));
            GL11.glEnable((1669 | 583 | 1335) >> 4 >> 2 ^ 2926);
            GL11.glEnable(2460 >> 1 >>> 1 >> 4 ^ 18 ^ 3060);
            GL11.glColor4f(2.0F, 2.0F, 2.0F, 2.0F);
            GlStateManager.disableBlend();
            if (((1205766118 | 558069493) >> 2 ^ 1420862838) != 0) {
                  ;
            }

            GL11.glPopMatrix();
      }

      public static void glColor(Color var0) {
            GL11.glColor4f((float)var0.getRed() / 255.0F, (float)var0.getGreen() / 255.0F, (float)var0.getBlue() / 255.0F, (float)var0.getAlpha() / 255.0F);
      }

      public void bind(int var1, int var2, ResourceLocation var3) {
            GlStateManager.pushMatrix();
            Minecraft.getMinecraft().renderEngine.bindTexture(var3);
            if (((1473662118 >>> 3 >> 4 & 4811365) << 4 ^ -165903532) != 0) {
                  ;
            }

            Gui.drawModalRectWithCustomSizedTexture(var1, var2, 0.0F, 0.0F, (23 ^ 14) >> 4 >> 4 ^ 100, (8 & 6 | 1369017515) << 3 ^ -1932761774, 256.0F, 373.0F);
            GlStateManager.popMatrix();
      }

      public static void setColor(Color var0) {
            float var10000 = (float)var0.getRed() / 255.0F;
            float var10001 = (float)var0.getGreen() / 255.0F;
            if (!"idiot".equals("nefariousMoment")) {
                  ;
            }

            GL11.glColor4f(var10000, var10001, (float)var0.getBlue() / 255.0F, (float)var0.getAlpha() / 255.0F);
      }

      public static void drawHLine(float var0, float var1, float var2, int var3, int var4) {
            if (var1 < var0) {
                  float var5 = var0;
                  if (!"your mom your dad the one you never had".equals("shitted on you harder than archybot")) {
                        ;
                  }

                  var0 = var1;
                  var1 = var5;
            }

            if (!"yo mama name maurice".equals("i hope you catch fire ngl")) {
                  ;
            }

            float var10002 = var1 + 1.0F;
            float var10003 = var2 + 1.0F;
            if ((((596197444 ^ 398726195) >>> 1 & 44369013) << 3 ^ 287310216) == 0) {
                  ;
            }

            UIRender.drawGradientRect(var0, var2, var10002, var10003, var3, var4);
            if (((195003307 << 1 << 1 | 162237663) ^ 805277439) == 0) {
                  ;
            }

      }

      public static void scaleRenderSecond(double var0) {
            if (((307747747 >> 1 | 130291139 | 87771827) ^ 890736560) != 0) {
                  ;
            }

            GL11.glScaled(var0, var0, var0);
            GL11.glPopMatrix();
      }

      public void bindTexture(int var1, int var2, int var3, int var4, int var5) {
            if (!"your mom your dad the one you never had".equals("please take a shower")) {
                  ;
            }

            GL11.glPushMatrix();
            if (((1988830138 | 1270383588) ^ 1710556926 ^ 22605603 ^ -843344269) != 0) {
                  ;
            }

            GlStateManager.enableBlend();
            GL11.glDisable((2603 << 3 | 20448) & 18126 ^ 19897);
            GL11.glDepthMask((boolean)((1396747163 >> 2 | 347162995) >> 4 ^ 2131163 ^ 24089188));
            GL11.glBlendFunc((120 | 17 | 20) >> 2 >> 2 ^ 773, (677 << 2 | 932) >>> 1 ^ 1753);
            if (!"You're so fat whenever you go to the beach the tide comes in.".equals("stop skidding")) {
                  ;
            }

            GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
            GL11.glDisable((97 >>> 2 | 3) ^ 25 ^ 3010);
            GL11.glBindTexture((1521 ^ 71 ^ 531) >> 3 ^ 3349, var5);
            if (((1339722694 >> 1 ^ 340788463) >>> 2 ^ 1448102215) != 0) {
                  ;
            }

            Gui.drawModalRectWithCustomSizedTexture(var1, var2, 0.0F, 0.0F, var3, var4, (float)var3, (float)var4);
            if ((1627124622 >>> 4 >>> 1 ^ -1104267252) != 0) {
                  ;
            }

            GL11.glDepthMask((boolean)((0 >>> 4 & 1113701626) >>> 2 ^ 1));
            GL11.glEnable((2479 & 725) << 3 ^ 3929);
            GL11.glEnable(1207 << 1 & 434 & 118 ^ 3042);
            GL11.glColor4f(2.0F, 2.0F, 2.0F, 2.0F);
            GlStateManager.disableBlend();
            GL11.glPopMatrix();
      }

      public static void disableGL2D() {
            if (((295004664 << 3 | 668312097) >> 3 >>> 1 >>> 4 ^ 128974831) == 0) {
                  ;
            }

            if ((((359787950 | 166716410) ^ 75080166) >> 1 ^ 214116108) == 0) {
                  ;
            }

            GL11.glEnable((584 >>> 3 & 46 | 2) & 3 ^ 3555);
            GL11.glEnable(((485 | 299) >>> 4 >> 3 | 2) ^ 2930);
            GL11.glDisable(1821 >> 1 >> 2 ^ 3011);
            GL11.glHint((1666 & 686 | 633) ^ 3753, 23 << 2 >> 1 ^ 4398);
            if (((1256970251 << 4 >> 3 | 1535567272) ^ 1932352287) != 0) {
                  ;
            }

            GL11.glHint(1535 >> 2 >>> 2 << 1 << 3 ^ 2467, 2280 >> 2 >> 1 ^ 4125);
      }

      public static void glColor(int var0) {
            float var1 = (float)(var0 >> ((6 ^ 3) << 2 >>> 2 ^ 1 ^ 28) & ((234 ^ 151) >> 1 << 1 ^ 131)) / 255.0F;
            int var10001 = (14 | 5) >> 1 ^ 23;
            if (((327614981 >>> 1 >> 2 | 10630974) ^ -1115198185) != 0) {
                  ;
            }

            float var2 = (float)(var0 >> var10001 & (144 << 3 << 1 >> 3 ^ 479)) / 255.0F;
            if (!"nefariousMoment".equals("idiot")) {
                  ;
            }

            float var3 = (float)(var0 >> ((0 >> 2 >>> 4 & 1272474508 | 1109307670 | 605099316) ^ 1713354046) & ((92 | 38) >>> 2 ^ 29 ^ 253)) / 255.0F;
            int var10000 = var0 & (((44 & 41) >>> 1 << 3 | 152) ^ 71);
            if (!"You're so fat whenever you go to the beach the tide comes in.".equals("yo mama name maurice")) {
                  ;
            }

            float var5 = (float)var10000 / 255.0F;
            if ((((897380271 >> 4 & 23465375) << 3 | 135084015) ^ 171800575) == 0) {
                  ;
            }

            float var4 = var5;
            GL11.glColor4f(var2, var3, var4, var1);
      }

      public static void drawRect(float var0, float var1, float var2, float var3) {
            GL11.glBegin(((3 ^ 0) & 1) >>> 1 ^ 7);
            GL11.glVertex2f(var0, var3);
            GL11.glVertex2f(var2, var3);
            GL11.glVertex2f(var2, var1);
            GL11.glVertex2f(var0, var1);
            GL11.glEnd();
            if ((1417243970 << 1 >> 3 >>> 2 ^ -300383475) != 0) {
                  ;
            }

      }

      public static void drawRect(double var0, double var2, double var4, double var6, int var8, float var9) {
            float var10 = (float)(var8 >> ((11 >> 3 | 0) ^ 25) & (49 << 1 >>> 3 >>> 2 >>> 4 ^ 255)) / 255.0F;
            float var11 = (float)(var8 >> ((10 >>> 3 | 0) >>> 4 ^ 16) & ((193 ^ 111 | 96) << 4 >> 3 ^ 291)) / 255.0F;
            float var12 = (float)(var8 >> (3 << 1 << 2 >> 2 >>> 2 ^ 9) & ((153 | 145) >> 4 >> 2 >>> 3 ^ 255)) / 255.0F;
            float var10000 = (float)(var8 & (50 & 19 ^ 14 ^ 227)) / 255.0F;
            if (((831946370 >>> 3 | 13960458) & 116229694 & 102348997 ^ 1744459457) != 0) {
                  ;
            }

            float var13 = var10000;
            GL11.glEnable((495 ^ 181 | 320 | 178) ^ 2584);
            GL11.glDisable(((2630 >>> 4 ^ 113) >> 1 ^ 36) << 3 ^ 3985);
            GL11.glBlendFunc((94 >>> 1 | 23) << 4 << 3 << 1 ^ 15362, (101 >>> 2 | 19) ^ 792);
            GL11.glEnable((721 | 238) << 3 & 4161 ^ 7008);
            GL11.glPushMatrix();
            if (!"please take a shower".equals("yo mama name maurice")) {
                  ;
            }

            GL11.glColor4f(var11, var12, var13, var9);
            GL11.glBegin((3 >> 1 ^ 0) >>> 4 ^ 1238813787 ^ 1238813788);
            if (!"intentMoment".equals("please get a girlfriend and stop cracking plugins")) {
                  ;
            }

            GL11.glVertex2d(var4, var2);
            GL11.glVertex2d(var0, var2);
            if ((552738944 >>> 2 ^ 1367547990) != 0) {
                  ;
            }

            GL11.glVertex2d(var0, var6);
            GL11.glVertex2d(var4, var6);
            if (!"you're dogshit".equals("intentMoment")) {
                  ;
            }

            GL11.glEnd();
            GL11.glPopMatrix();
            if (!"shitted on you harder than archybot".equals("please go outside")) {
                  ;
            }

            GL11.glEnable(((3104 << 4 & 28261 ^ 3120) & 19105) >>> 2 ^ 8041);
            GL11.glDisable((2666 >> 3 >>> 3 & 17) << 1 << 1 ^ 3046);
            GL11.glDisable(((403 << 3 | 2692) & 971) >> 2 << 2 ^ 2472);
      }

      public static void prepareScissorBox(float var0, float var1, float var2, float var3) {
            ScaledResolution var4 = new ScaledResolution(Minecraft.getMinecraft());
            int var5 = var4.getScaleFactor();
            float var10001 = (float)var5;
            if ((((1172106109 | 1150411732) ^ 210070595 | 577838574) ^ -154741069) != 0) {
                  ;
            }

            int var10000 = (int)(var0 * var10001);
            if (!"ape covered in human flesh".equals("you're dogshit")) {
                  ;
            }

            int var6 = (int)(((float)var4.getScaledHeight() - var3) * (float)var5);
            int var10002 = (int)((var2 - var0) * (float)var5);
            if (((1368397441 | 1307545027 | 175904370) >>> 1 ^ 805294073) == 0) {
                  ;
            }

            GL11.glScissor(var10000, var6, var10002, (int)((var3 - var1) * (float)var5));
      }

      public static void drawRect(double var0, double var2, double var4, double var6, int var8) {
            float var9 = (float)(var8 >> ((18 ^ 4 ^ 18) >>> 1 ^ 26) & ((197 | 114) >>> 3 ^ 29 ^ 252)) / 255.0F;
            if (((100835538 >>> 1 | 14778549) >>> 2 ^ -204673918) != 0) {
                  ;
            }

            int var10000 = var8 >> (12 >>> 4 & 958699899 ^ 16) & (164 >>> 3 >> 4 ^ 254);
            if ((403714484 >> 4 & 12706822 ^ 8454146) == 0) {
                  ;
            }

            float var10 = (float)var10000 / 255.0F;
            var10000 = var8 >> (((4 | 1) >>> 2 & 0) >>> 2 ^ 8);
            int var10001 = (110 << 1 << 4 | 1413) ^ 3386;
            if ((((43254274 ^ 20851742) << 4 | 440128329) ^ 985396169) == 0) {
                  ;
            }

            float var11 = (float)(var10000 & var10001) / 255.0F;
            float var12 = (float)(var8 & (((72 ^ 68 ^ 6) & 8) << 2 ^ 223)) / 255.0F;
            GL11.glEnable((467 | 389) >> 2 ^ 33 ^ 2998);
            GL11.glDisable((1335 & 162 | 3) << 2 ^ 3437);
            GL11.glBlendFunc((517 & 114 & 1159866317) >>> 4 ^ 770, 94 >>> 4 >>> 1 ^ 769);
            GL11.glEnable((1251 & 1051) >> 2 ^ 2592);
            if (!"please get a girlfriend and stop cracking plugins".equals("stringer is a good obfuscator")) {
                  ;
            }

            if ((((346867821 ^ 218522825) << 1 ^ 828636255) >>> 2 >>> 2 ^ 2049883793) != 0) {
                  ;
            }

            GL11.glPushMatrix();
            GL11.glColor4f(var10, var11, var12, var9);
            GL11.glBegin(((5 ^ 0 | 4) ^ 1) << 1 & 0 ^ 7);
            GL11.glVertex2d(var4, var2);
            GL11.glVertex2d(var0, var2);
            GL11.glVertex2d(var0, var6);
            if ((((1595785930 >> 4 ^ 33967795) >>> 3 | 9146751) ^ -1033756595) != 0) {
                  ;
            }

            GL11.glVertex2d(var4, var6);
            if ((1458620317 << 3 >>> 2 >> 2 ^ 192439246) == 0) {
                  ;
            }

            GL11.glEnd();
            GL11.glPopMatrix();
            GL11.glEnable((206 >> 2 << 4 | 333) & 361 ^ 3208);
            if ((((1813644302 | 1257563063) & 912150917 ^ 527563390) >>> 1 ^ -10650293) != 0) {
                  ;
            }

            GL11.glDisable((1143 >>> 1 | 276) >>> 3 ^ 2949);
            GL11.glDisable((166 | 44) << 4 >> 4 & 72 ^ 2856);
      }

      public static void drawGradientRect(float var0, float var1, float var2, float var3, int var4, int var5) {
            UIRender.enableGL2D();
            GL11.glShadeModel((3362 >>> 3 | 145) ^ 7348);
            GL11.glBegin((5 | 1 | 0) ^ 2);
            if (!"buy a domain and everything else you need at namecheap.com".equals("i hope you catch fire ngl")) {
                  ;
            }

            UIRender.glColor(var4);
            GL11.glVertex2f(var0, var3);
            GL11.glVertex2f(var2, var3);
            UIRender.glColor(var5);
            GL11.glVertex2f(var2, var1);
            GL11.glVertex2f(var0, var1);
            GL11.glEnd();
            GL11.glShadeModel((3109 | 84) >>> 3 >>> 3 >> 4 ^ 7427);
            UIRender.disableGL2D();
      }

      public static void drawFullCircle(int var0, int var1, double var2, int var4) {
            var2 *= 2.0D;
            var0 *= (0 ^ 183111432) << 3 & 1422958735 & 332594987 ^ 273686530;
            var1 *= 0 >> 1 ^ 374752278 ^ 374752276;
            float var5 = (float)(var4 >> ((11 ^ 4) >>> 3 >>> 3 ^ 24) & (42 >> 3 >> 3 >> 2 ^ 255)) / 255.0F;
            if (((1156170449 << 1 << 4 | 897784406) & 591938373 ^ 2102402163) != 0) {
                  ;
            }

            float var10000 = (float)(var4 >> (((14 ^ 7) << 4 | 23) ^ 135) & (((128 ^ 81) >> 4 & 12) >> 1 & 1 ^ 255));
            if ((2046249867 << 3 >> 3 ^ 645571196 ^ 1523689336) != 0) {
                  ;
            }

            float var6 = var10000 / 255.0F;
            if (((50645557 >>> 4 ^ 633611) & 2078593 ^ 1681280) == 0) {
                  ;
            }

            var10000 = (float)(var4 >> (((1 >>> 1 | 1192055626) ^ 902352592) << 2 ^ -887720352) & (((249 >> 4 | 8) >> 1 | 5) >> 2 ^ 254));
            if ((((2118663904 | 1382500602) >> 2 ^ 207738581) >>> 1 ^ 167576757) == 0) {
                  ;
            }

            float var7 = var10000 / 255.0F;
            float var8 = (float)(var4 & (246 >> 4 & 12 ^ 243)) / 255.0F;
            UIRender.enableGL2D();
            GL11.glScalef(0.5F, 0.5F, 0.5F);
            if (((1532916887 >>> 1 | 40647739) >> 4 ^ 50263015) == 0) {
                  ;
            }

            GL11.glColor4f(var6, var7, var8, var5);
            if ((1091791441 >> 2 ^ 95794140 ^ 368142664) == 0) {
                  ;
            }

            var10000 = (float)var0;
            float var10001 = (float)var1;
            if ((((473281095 >>> 2 | 109537488) & 25471367 | 8369433) >> 4 ^ 2096089) == 0) {
                  ;
            }

            UIRender.drawCircle(var10000, var10001, (float)var2, var4, 1.0F);
            GL11.glBegin((4 >> 2 ^ 0) & 0 ^ 9);
            int var14 = (613971403 >> 3 | 18970973) << 1 ^ 191299578;
            if ((((1666576568 | 324774526) >>> 1 & 434677602) << 1 ^ 860965572) == 0) {
                  ;
            }

            int var9 = var14;

            while(true) {
                  int var15 = ((125 ^ 84) & 40) << 2 ^ 456;
                  if ("intentMoment".equals("nefariousMoment")) {
                  }

                  if (var9 > var15) {
                        GL11.glEnd();
                        if (!"you're dogshit".equals("shitted on you harder than archybot")) {
                              ;
                        }

                        GL11.glScalef(2.0F, 2.0F, 2.0F);
                        UIRender.disableGL2D();
                        return;
                  }

                  double var16 = (double)var9 * 3.141592653589793D;
                  if (((85813673 | 34349884) << 3 ^ -1079594733) != 0) {
                        ;
                  }

                  double var10 = Math.sin(var16 / 180.0D) * var2;
                  double var12 = Math.cos((double)var9 * 3.141592653589793D / 180.0D) * var2;
                  if ((2037183047 << 3 >>> 3 ^ -542516799) != 0) {
                        ;
                  }

                  GL11.glVertex2d((double)var0 + var10, (double)var1 + var12);
                  ++var9;
            }
      }

      public static void drawCircle(float var0, float var1, float var2, int var3) {
            float var4 = (float)(var3 >> (12 >>> 1 >> 1 << 4 << 1 ^ 120) & ((56 ^ 38) >>> 2 ^ 248)) / 255.0F;
            int var10001 = 7 & 0 ^ 1198591200 ^ 1198591216;
            if ((535835356 << 1 >>> 1 >>> 3 ^ -890907834) != 0) {
                  ;
            }

            float var5 = (float)(var3 >> var10001 & ((59 >>> 2 | 3) ^ 240)) / 255.0F;
            if (!"buy a domain and everything else you need at namecheap.com".equals("ape covered in human flesh")) {
                  ;
            }

            float var6 = (float)(var3 >> ((7 >> 3 | 1387345629) >> 3 ^ 13215969 ^ 178228786) & ((163 >> 1 | 2) >> 3 ^ 245)) / 255.0F;
            float var7 = (float)(var3 & ((24 >> 2 | 5) & 5 ^ 250)) / 255.0F;
            boolean var8 = GL11.glIsEnabled((2496 | 997) >> 2 << 2 ^ 6);
            if (!"stop skidding".equals("yo mama name maurice")) {
                  ;
            }

            if (((493479371 | 209162001 | 105227674) ^ 528478171) == 0) {
                  ;
            }

            boolean var9 = GL11.glIsEnabled((2483 & 819 | 113) & 143 ^ 2 ^ 2849);
            boolean var10 = GL11.glIsEnabled((120 | 63) >> 4 & 6 ^ 4 ^ 1 ^ 3554);
            if (!var8) {
                  GL11.glEnable(2481 & 117 ^ 24 ^ 3019);
            }

            if (!var9) {
                  GL11.glEnable(((1305 ^ 189) & 232) >>> 1 ^ 2928);
            }

            if (var10) {
                  GL11.glDisable((1067 & 252) >>> 4 ^ 3555);
            }

            GL11.glBlendFunc(260 >> 1 << 3 >>> 4 & 28 ^ 770, (415 & 351 ^ 141) >> 4 ^ 794);
            GL11.glColor4f(var5, var6, var7, var4);
            GL11.glBegin(((8 & 4) >> 1 & 75996608) >> 2 ^ 9);
            int var11 = ((619012089 | 177896937) << 2 | 2143628838) ^ 325273348 ^ -325142302;

            while(true) {
                  var10001 = 93 >>> 2 << 3 ^ 74 ^ 410;
                  if ((866390532 << 3 >> 2 ^ -414702584) != 0) {
                  }

                  if (var11 > var10001) {
                        GL11.glEnd();
                        if (!"idiot".equals("nefariousMoment")) {
                              ;
                        }

                        if (var10) {
                              GL11.glEnable((354 >> 2 | 67) ^ 3514);
                        }

                        if (!var9) {
                              GL11.glDisable((1965 ^ 434) << 2 ^ 4956);
                        }

                        if (!"Your family tree must be a cactus because everyone on it is a prick.".equals("your mom your dad the one you never had")) {
                              ;
                        }

                        if (!var8) {
                              GL11.glDisable((2213 << 2 >>> 2 | 1931) >>> 1 ^ 3125);
                        }

                        return;
                  }

                  double var10000 = (double)var0 + Math.sin((double)var11 * 3.141526D / 180.0D) * (double)var2;
                  double var12 = (double)var1;
                  double var10002 = (double)var11 * 3.141526D;
                  if (((1336725320 | 346349956 | 219972560) ^ 1606217692) == 0) {
                        ;
                  }

                  GL11.glVertex2d(var10000, var12 + Math.cos(var10002 / 180.0D) * (double)var2);
                  ++var11;
            }
      }

      public void drawVerticalLine(double var1, double var3, double var5, int var7) {
            if (!"your mom your dad the one you never had".equals("please get a girlfriend and stop cracking plugins")) {
                  ;
            }

            UIRender.drawRect(var1, var3, var1 + 1.0D, var5, var7);
      }

      public static void scaleRenderFirst(double var0) {
            ScaledResolution var2 = new ScaledResolution(Minecraft.getMinecraft());
            if (((1498556534 ^ 416735627) >> 1 >>> 1 ^ 274805375) == 0) {
                  ;
            }

            double var10000 = (double)var2.getScaleFactor();
            double var10001 = Math.pow((double)var2.getScaleFactor(), 2.0D);
            if (!"Your family tree must be a cactus because everyone on it is a prick.".equals("you're dogshit")) {
                  ;
            }

            double var3 = var10000 / var10001;
            GL11.glPushMatrix();
            GL11.glScaled(var3 * var0, var3 * var0, var3 * var0);
      }

      public static void enableGL2D() {
            GL11.glDisable((165 >>> 2 | 10) >> 3 ^ 2932);
            GL11.glEnable((2194 & 580 ^ 782821846 | 329791906) ^ 1068036628);
            GL11.glDisable((3497 & 1755 | 153) >>> 2 ^ 143 ^ 3144);
            GL11.glBlendFunc((68 >>> 2 | 3) ^ 785, 659 & 276 & 5 ^ 771);
            GL11.glDepthMask((boolean)((0 ^ 908317193 | 850701568) ^ 918028040));
            int var10000 = (2788 ^ 2402) >> 4 >> 1 ^ 2876;
            if (((876686842 >> 1 & 353177915 | 247524805) ^ 515964413) == 0) {
                  ;
            }

            GL11.glEnable(var10000);
            GL11.glHint((229 & 166 | 114) << 3 ^ 3042, ((1751 & 1629 ^ 1259) & 323) >>> 2 ^ 4354);
            GL11.glHint((30 << 3 | 180) ^ 211 ^ 3188, (3968 & 2579 & 1494) >>> 2 ^ 4354);
      }

      public static void drawBorderedRect(float var0, float var1, float var2, float var3, float var4, int var5, int var6) {
            UIRender.enableGL2D();
            UIRender.glColor(var5);
            if (((70523380 ^ 49539314) << 3 << 1 ^ 1907326185) != 0) {
                  ;
            }

            UIRender.drawRect((double)(var0 + var4), (double)(var1 + var4), (double)(var2 - var4), (double)(var3 - var4), var6);
            if (((12992101 << 1 | 19679700) << 2 ^ 166542144) != 0) {
                  ;
            }

            UIRender.glColor(var6);
            UIRender.drawRect(var0 + var4, var1, var2 - var4, var1 + var4);
            UIRender.drawRect(var0, var1, var0 + var4, var3);
            if ((465996270 >>> 2 << 3 << 4 >>> 3 ^ -994487998) != 0) {
                  ;
            }

            UIRender.drawRect(var2 - var4, var1, var2, var3);
            if (((2066846136 | 707502559) >>> 4 ^ -1853325995) != 0) {
                  ;
            }

            UIRender.drawRect(var0 + var4, var3 - var4, var2 - var4, var3);
            UIRender.disableGL2D();
      }

      public UIRender() {
            if (!"i hope you catch fire ngl".equals("ape covered in human flesh")) {
                  ;
            }

            super();
      }

      public static void drawRoundedRect(float var0, float var1, float var2, float var3, int var4) {
            UIRender.enableGL2D();
            if ((((1314811088 ^ 419440317) << 1 | 1738838850) ^ -272853030) == 0) {
                  ;
            }

            GL11.glScalef(0.5F, 0.5F, 0.5F);
            if (!"please get a girlfriend and stop cracking plugins".equals("please go outside")) {
                  ;
            }

            float var10000 = var0 *= 2.0F;
            float var10001 = (var1 *= 2.0F) + 1.0F;
            if (!"please go outside".equals("yo mama name maurice")) {
                  ;
            }

            UIRender.drawVLine(var10000, var10001, (var3 *= 2.0F) - 2.0F, var4);
            UIRender.drawVLine((var2 *= 2.0F) - 1.0F, var1 + 1.0F, var3 - 2.0F, var4);
            var10000 = var0 + 2.0F;
            var10001 = var2 - 3.0F;
            if (((2132852086 | 43278427) & 1538786483 ^ 1353441181 ^ 186375086) == 0) {
                  ;
            }

            UIRender.drawHLine(var10000, var10001, var1, var4, var4);
            UIRender.drawHLine(var0 + 2.0F, var2 - 3.0F, var3 - 1.0F, var4, var4);
            if (((109346834 ^ 28294501) >> 4 << 1 ^ 15034286) == 0) {
                  ;
            }

            UIRender.drawHLine(var0 + 1.0F, var0 + 1.0F, var1 + 1.0F, var4, var4);
            if (((1028762297 >> 1 << 2 >>> 1 | 275745518) & 32262428 ^ 23857180) == 0) {
                  ;
            }

            UIRender.drawHLine(var2 - 2.0F, var2 - 2.0F, var1 + 1.0F, var4, var4);
            var10000 = var2 - 2.0F;
            if ((((1508843987 ^ 1491415451) >>> 2 | 2610774) ^ 6805078) == 0) {
                  ;
            }

            UIRender.drawHLine(var10000, var2 - 2.0F, var3 - 2.0F, var4, var4);
            var10000 = var0 + 1.0F;
            var10001 = var0 + 1.0F;
            float var10002 = var3 - 2.0F;
            if (((265132813 >>> 3 | 22386779) & 9234757 ^ 983294916) != 0) {
                  ;
            }

            UIRender.drawHLine(var10000, var10001, var10002, var4, var4);
            var10000 = var0 + 1.0F;
            if (!"idiot".equals("nefariousMoment")) {
                  ;
            }

            double var5 = (double)var10000;
            if ((146800650 << 2 ^ 587202600) == 0) {
                  ;
            }

            UIRender.drawRect(var5, (double)(var1 + 1.0F), (double)(var2 - 1.0F), (double)(var3 - 1.0F), var4);
            GL11.glScalef(2.0F, 2.0F, 2.0F);
            UIRender.disableGL2D();
      }
}
