//Deobfuscated with https://github.com/PetoPetko/Minecraft-Deobfuscator3000 using mappings "wew"!

package me.independed.inceptice.util;

import java.awt.Color;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.texture.TextureManager;
import net.minecraft.util.ResourceLocation;
import org.lwjgl.opengl.GL11;

public class UIRender {
      public static void drawFullCircle(int var0, int var1, double var2, int var4) {
            var2 *= 2.0D;
            var0 *= 1 >> 4 << 4 ^ 2;
            if ((((1392864642 >>> 4 >>> 1 | 30791781) ^ 17067855) >> 1 ^ 1443274015) != 0) {
                  ;
            }

            var1 *= (1 | 0) & 0 ^ 2;
            float var5 = (float)(var4 >> ((17 & 2) >>> 4 ^ 24) & (127 >> 2 << 4 ^ 271)) / 255.0F;
            float var10000 = (float)(var4 >> (((1 | 0) ^ 0 | 0) ^ 0 ^ 17) & (((194 ^ 150) & 59 | 2) >> 4 ^ 254));
            if (!"please go outside".equals("please go outside")) {
                  ;
            }

            var10000 /= 255.0F;
            if (!"your mom your dad the one you never had".equals("please go outside")) {
                  ;
            }

            float var6 = var10000;
            var10000 = (float)(var4 >> (((3 ^ 1) & 0) >> 1 ^ 1994019041 ^ 1994019049) & ((97 >> 2 ^ 19) >>> 1 << 3 ^ 215)) / 255.0F;
            if (!"please dont crack my plugin".equals("ape covered in human flesh")) {
                  ;
            }

            float var7 = var10000;
            if (((25723288 >>> 1 | 8709544) ^ 1779813384) != 0) {
                  ;
            }

            float var8 = (float)(var4 & ((231 << 3 >> 1 | 353) ^ 487 ^ 741)) / 255.0F;
            UIRender.enableGL2D();
            GL11.glScalef(0.5F, 0.5F, 0.5F);
            GL11.glColor4f(var6, var7, var8, var5);
            UIRender.drawCircle((float)var0, (float)var1, (float)var2, var4, 1.0F);
            if ((103233841 >>> 2 << 2 << 2 & 336054475 ^ 868567813) != 0) {
                  ;
            }

            int var14 = (((2 | 1) ^ 2) << 4 >> 3 | 1) ^ 10;
            if ((1419837984 ^ 831624511 ^ 1697683231) == 0) {
                  ;
            }

            GL11.glBegin(var14);
            if ((20408621 << 4 >>> 3 ^ 10045913 ^ 49781635) == 0) {
                  ;
            }

            for(int var9 = 179262343 << 2 & 68157692 ^ 1048604; var9 <= ((174 ^ 97 ^ 0) & 153 ^ 481); ++var9) {
                  double var15 = Math.sin((double)var9 * 3.141592653589793D / 180.0D);
                  if (!"nefariousMoment".equals("shitted on you harder than archybot")) {
                        ;
                  }

                  double var10 = var15 * var2;
                  double var12 = Math.cos((double)var9 * 3.141592653589793D / 180.0D) * var2;
                  if (!"you probably spell youre as your".equals("you probably spell youre as your")) {
                        ;
                  }

                  var15 = (double)var0 + var10;
                  double var10001 = (double)var1;
                  if (((1967531263 | 397566219 | 920489479) >>> 1 ^ -909669393) != 0) {
                        ;
                  }

                  GL11.glVertex2d(var15, var10001 + var12);
            }

            GL11.glEnd();
            GL11.glScalef(2.0F, 2.0F, 2.0F);
            UIRender.disableGL2D();
      }

      public static void drawRoundedRect(float var0, float var1, float var2, float var3, int var4) {
            UIRender.enableGL2D();
            GL11.glScalef(0.5F, 0.5F, 0.5F);
            if ((498193837 >> 2 & 123040293 ^ 121925665) == 0) {
                  ;
            }

            if (((812776589 | 346932297 | 356385328) >> 3 >>> 4 ^ 7077885) == 0) {
                  ;
            }

            float var10000 = var0 *= 2.0F;
            float var10001 = var1 * 2.0F;
            if ((703142786 >> 2 >> 4 ^ 7879353 ^ 14655191) == 0) {
                  ;
            }

            var1 = var10001++;
            float var10002 = var3 *= 2.0F;
            if ((17042768 >> 3 ^ 1701927285) != 0) {
                  ;
            }

            UIRender.drawVLine(var10000, var10001, var10002 - 2.0F, var4);
            var10000 = (var2 *= 2.0F) - 1.0F;
            if (!"please go outside".equals("intentMoment")) {
                  ;
            }

            UIRender.drawVLine(var10000, var1 + 1.0F, var3 - 2.0F, var4);
            UIRender.drawHLine(var0 + 2.0F, var2 - 3.0F, var1, var4, var4);
            UIRender.drawHLine(var0 + 2.0F, var2 - 3.0F, var3 - 1.0F, var4, var4);
            var10000 = var0 + 1.0F;
            var10001 = var0 + 1.0F;
            if ((285509698 << 3 ^ 1378185418) != 0) {
                  ;
            }

            var10002 = var1 + 1.0F;
            if ((882897670 >>> 3 >> 2 ^ -825784318) != 0) {
                  ;
            }

            UIRender.drawHLine(var10000, var10001, var10002, var4, var4);
            UIRender.drawHLine(var2 - 2.0F, var2 - 2.0F, var1 + 1.0F, var4, var4);
            var10000 = var2 - 2.0F;
            if (((89302711 ^ 11323806) & 39702090 ^ 824177598) != 0) {
                  ;
            }

            var10001 = var2 - 2.0F;
            if (((445435511 | 352178047) << 2 >> 2 ^ 153304736 ^ -1382152714) != 0) {
                  ;
            }

            UIRender.drawHLine(var10000, var10001, var3 - 2.0F, var4, var4);
            var10000 = var0 + 1.0F;
            var10001 = var0 + 1.0F;
            var10002 = var3 - 2.0F;
            if (((52250398 << 4 >>> 3 | 45642939) ^ 112918207) == 0) {
                  ;
            }

            UIRender.drawHLine(var10000, var10001, var10002, var4, var4);
            if ((245039851 >> 3 >>> 4 << 3 ^ 15314984) == 0) {
                  ;
            }

            UIRender.drawRect((double)(var0 + 1.0F), (double)(var1 + 1.0F), (double)(var2 - 1.0F), (double)(var3 - 1.0F), var4);
            if (((1262113499 | 650278661) ^ 687946290 ^ 1059881907 ^ 2044186206) == 0) {
                  ;
            }

            GL11.glScalef(2.0F, 2.0F, 2.0F);
            UIRender.disableGL2D();
      }

      public static void scaleRenderSecond(double var0) {
            GL11.glScaled(var0, var0, var0);
            if (((2108866415 ^ 1754245348) >> 4 & 22101790 & 11757468 ^ 1114392) == 0) {
                  ;
            }

            GL11.glPopMatrix();
      }

      public void bind(int var1, int var2, ResourceLocation var3) {
            if (!"please dont crack my plugin".equals("i hope you catch fire ngl")) {
                  ;
            }

            GlStateManager.pushMatrix();
            Minecraft.getMinecraft().renderEngine.bindTexture(var3);
            int var10004 = (86 & 7) << 3 ^ 84;
            if ((684387811 << 4 ^ 2075338392 ^ 136546100 ^ 540049071 ^ -552730317) == 0) {
                  ;
            }

            Gui.drawModalRectWithCustomSizedTexture(var1, var2, 0.0F, 0.0F, var10004, (8 | 1 | 2) >>> 4 >> 4 ^ 10, 256.0F, 373.0F);
            GlStateManager.popMatrix();
            if (((1776054322 ^ 645034578) >> 3 >>> 3 ^ 20887561) == 0) {
                  ;
            }

      }

      public static void drawGradientRect(float var0, float var1, float var2, float var3, int var4, int var5) {
            if (!"Some babies were dropped on their heads but you were clearly thrown at a wall.".equals("your mom your dad the one you never had")) {
                  ;
            }

            UIRender.enableGL2D();
            if (((2092202349 ^ 1673305160 ^ 514580523) >>> 2 ^ 6865219) == 0) {
                  ;
            }

            GL11.glShadeModel((2757 | 816) << 3 & 1820 ^ 6665);
            GL11.glBegin((5 | 3) << 4 ^ 15 ^ 120);
            UIRender.glColor(var4);
            if (((2058182469 ^ 1842418178) << 1 ^ 788044430) == 0) {
                  ;
            }

            GL11.glVertex2f(var0, var3);
            if (!"Your family tree must be a cactus because everyone on it is a prick.".equals("you're dogshit")) {
                  ;
            }

            GL11.glVertex2f(var2, var3);
            UIRender.glColor(var5);
            if (!"You're so fat whenever you go to the beach the tide comes in.".equals("please go outside")) {
                  ;
            }

            GL11.glVertex2f(var2, var1);
            if ((2818064 >> 2 << 3 ^ 1485093667) != 0) {
                  ;
            }

            GL11.glVertex2f(var0, var1);
            GL11.glEnd();
            GL11.glShadeModel(6235 ^ 3525 ^ 950 ^ 2856);
            if (!"idiot".equals("yo mama name maurice")) {
                  ;
            }

            UIRender.disableGL2D();
      }

      public static void setColor(Color var0) {
            int var10000 = var0.getRed();
            if (((788880898 >> 4 ^ 8462110) << 4 >> 2 ^ 163916792) == 0) {
                  ;
            }

            float var1 = (float)var10000 / 255.0F;
            float var10001 = (float)var0.getGreen() / 255.0F;
            float var10002 = (float)var0.getBlue();
            if (!"shitted on you harder than archybot".equals("you probably spell youre as your")) {
                  ;
            }

            GL11.glColor4f(var1, var10001, var10002 / 255.0F, (float)var0.getAlpha() / 255.0F);
      }

      public static void disableGL2D() {
            GL11.glEnable(((1485 & 661) >> 4 >>> 2 | 1) ^ 2 ^ 3552);
            GL11.glEnable((2913 & 2622) >> 1 << 1 ^ 337);
            GL11.glDisable(1451 << 1 << 3 >> 1 ^ 9848);
            GL11.glHint((1697 ^ 150 | 85) ^ 2597, (578 << 3 & 4021 | 80) ^ 4944);
            GL11.glHint((325 >> 3 & 39) >>> 1 ^ 3139, 3492 >>> 3 >> 2 << 3 << 1 ^ 6096);
      }

      public static void enableGL2D() {
            GL11.glDisable(2024 >>> 3 >> 1 ^ 2831);
            GL11.glEnable((732 >>> 3 >>> 1 ^ 15) & 14 ^ 3040);
            GL11.glDisable(((2774 ^ 687 | 1319) >> 2 | 759) >>> 1 ^ 3102);
            GL11.glBlendFunc((354 << 3 | 176) >> 1 << 4 ^ 24194, (25 | 7) << 3 << 1 >>> 4 >>> 4 ^ 770);
            if ((787845532 >>> 4 << 4 >> 2 ^ 11446881 ^ 921964977) != 0) {
                  ;
            }

            GL11.glDepthMask((boolean)(0 << 1 << 4 >>> 4 ^ 1));
            if ((((1872149469 | 304790924) ^ 333392894) >>> 1 ^ -300562688) != 0) {
                  ;
            }

            GL11.glEnable((489 ^ 48) >> 1 >>> 3 >> 3 ^ 2851);
            GL11.glHint(1213 >>> 4 >>> 2 & 12 ^ 1032335865 ^ 1032338859, (1335 << 3 & 1516) >> 3 & 23 ^ 4375);
            GL11.glHint(243 ^ 33 ^ 83 ^ 3282, 606 >>> 1 << 3 >>> 2 ^ 4956);
      }

      public void drawVerticalLine(double var1, double var3, double var5, int var7) {
            if (((14047463 >>> 3 >> 1 << 4 | 10371062 | 12390333) ^ 1284938) != 0) {
                  ;
            }

            UIRender.drawRect(var1, var3, var1 + 1.0D, var5, var7);
            if ((((574889986 | 281839052) ^ 511997024) >> 1 ^ -1278909416) != 0) {
                  ;
            }

            if (((51947928 ^ 3120862 ^ 4514771) << 2 << 2 ^ 1659081254) != 0) {
                  ;
            }

      }

      public static void drawCircle(float var0, float var1, float var2, int var3) {
            int var10000 = var3 >> ((19 >>> 3 ^ 0) & 0 ^ 24);
            if ((125516731 >> 2 >> 3 >> 3 ^ 222686837) != 0) {
                  ;
            }

            float var4 = (float)(var10000 & ((((46 >> 1 | 16) ^ 1) << 1 | 15) ^ 208)) / 255.0F;
            float var5 = (float)(var3 >> ((14 | 5 | 1) ^ 31) & (((211 & 15) >>> 4 | 1678590535) ^ 1678590648)) / 255.0F;
            float var6 = (float)(var3 >> (3 >> 1 << 4 >>> 2 << 2 ^ 24) & ((122 & 121 | 51) >>> 3 ^ 240)) / 255.0F;
            float var7 = (float)(var3 & ((31 ^ 3) >> 2 ^ 248)) / 255.0F;
            boolean var8 = GL11.glIsEnabled((2558 | 1720) >> 2 ^ 406 ^ 2443);
            boolean var9 = GL11.glIsEnabled((2807 | 2676) & 1602 ^ 2402);
            boolean var10 = GL11.glIsEnabled(((3111 | 128) >>> 3 | 151) ^ 3190);
            if (!var8) {
                  if ((((1361083279 ^ 930402131) >> 2 ^ 108652601) >>> 3 ^ 66951329) == 0) {
                        ;
                  }

                  GL11.glEnable(808 >>> 1 >> 4 & 17 ^ 3059);
            }

            if (!var9) {
                  GL11.glEnable((2839 | 218) >> 2 ^ 2519);
            }

            if (var10) {
                  GL11.glDisable(((676 ^ 270 ^ 196) & 351 | 18) ^ 3263);
            }

            var10000 = (((548 ^ 518) & 10 & 1) >>> 1 | 1065240627) ^ 1065241393;
            if (((266022267 | 16493000) >> 2 ^ 33910987 ^ 33266613) == 0) {
                  ;
            }

            GL11.glBlendFunc(var10000, 148 >>> 4 >>> 4 ^ 771);
            GL11.glColor4f(var5, var6, var7, var4);
            GL11.glBegin((6 >>> 2 << 3 & 3 | 1460714048) ^ 1460714057);
            int var11 = 26214465 ^ 21225190 ^ 13885095;
            if (((1212898431 >>> 2 | 297428819) ^ -1365893087) != 0) {
                  ;
            }

            while(true) {
                  if (((1652979265 >> 1 ^ 529360274) << 1 ^ -1760790756) != 0) {
                        ;
                  }

                  if ((62241652 >> 3 & 2421761 ^ 185508289) != 0) {
                        ;
                  }

                  if (var11 > (((175 | 10) >>> 2 | 15) << 1 << 2 ^ 16)) {
                        GL11.glEnd();
                        if (var10) {
                              GL11.glEnable((1347 << 4 & 12867) << 4 ^ 69089);
                              if (((940728353 << 3 | 1117693684) ^ 1918769175) != 0) {
                                    ;
                              }
                        }

                        if (!var9) {
                              GL11.glDisable(2352 << 2 >>> 3 ^ 4024);
                        }

                        if (!var8) {
                              GL11.glDisable(540 << 2 & 437 ^ 3026);
                        }

                        return;
                  }

                  double var12 = (double)var0 + Math.sin((double)var11 * 3.141526D / 180.0D) * (double)var2;
                  double var10001 = (double)var1;
                  if ((984104841 >> 2 >>> 4 & 8858989 ^ 8527980) == 0) {
                        ;
                  }

                  double var10002 = (double)var11 * 3.141526D / 180.0D;
                  if (((329851877 ^ 126184585) >> 3 >>> 2 ^ 10576507) == 0) {
                        ;
                  }

                  GL11.glVertex2d(var12, var10001 + Math.cos(var10002) * (double)var2);
                  ++var11;
            }
      }

      public static void drawArrow(double var0, double var2, float var4, int var5) {
            GL11.glEnable((732 << 4 | 5227) ^ 13833);
            GL11.glDisable((3025 & 1267 & 187) << 1 << 1 ^ 4005);
            GL11.glBlendFunc(504 >>> 2 << 3 << 1 ^ 1250, (505 & 213) >> 2 << 4 >>> 2 ^ 979);
            int var10001 = (13 ^ 6 | 9) << 4 ^ 75 ^ 227;
            if (((2030806770 ^ 1069045396) >>> 3 ^ -252638494) != 0) {
                  ;
            }

            float var6 = (float)(var5 >> var10001 & ((27 | 7) >> 1 ^ 240)) / 255.0F;
            float var7 = (float)(var5 >> ((6 | 0) << 1 >>> 1 >> 4 ^ 16) & (123 >> 2 & 11 ^ 9 ^ 252)) / 255.0F;
            float var8 = (float)(var5 >> (7 >> 3 << 4 ^ 1619592904 ^ 1619592896) & (243 << 3 >> 1 ^ 819)) / 255.0F;
            float var9 = (float)(var5 & ((236 ^ 72) << 2 ^ 543 ^ 112)) / 255.0F;
            if (((1161914456 >>> 2 | 58769043) ^ 332463767) == 0) {
                  ;
            }

            GL11.glColor4f(var7, var8, var9, var6);
            GL11.glEnable((58 | 51) >>> 4 << 2 ^ 6 ^ 2858);
            int var10000 = (871 & 173) << 1 ^ 3096;
            var10001 = ((2794 & 1406) >> 4 & 5 ^ 2) >>> 2 ^ 4355;
            if (((246585800 >>> 3 | 13439228 | 10232666) ^ 305582704) != 0) {
                  ;
            }

            GL11.glHint(var10000, var10001);
            if (((662293086 ^ 239704492) >> 1 ^ 15444498 ^ 343114731) == 0) {
                  ;
            }

            GL11.glLineWidth(2.0F);
            if (((153740043 << 1 & 263337729 ^ 28822911) << 2 ^ 1571716772) != 0) {
                  ;
            }

            GL11.glShadeModel((2265 << 3 ^ 14770) >>> 1 ^ 3561 ^ 12117);
            GL11.glBegin(((1 & 0) << 4 | 1329783777) ^ 1329783779);
            double var10 = var0 - (double)var4;
            double var11 = var2 - (double)var4;
            if ((2002837222 >> 4 & 87779999 ^ 87165070) == 0) {
                  ;
            }

            GL11.glVertex2d(var10, var11 - (double)var4);
            GL11.glVertex2d(var0 + (double)var4, var2 + (double)var4 - (double)var4);
            if (((72613888 ^ 34377608 | 93266766) ^ 1584508858) != 0) {
                  ;
            }

            GL11.glEnd();
            GL11.glBegin((1 | 0) >>> 3 << 3 ^ 1605962108 ^ 1605962110);
            if (!"Your family tree must be a cactus because everyone on it is a prick.".equals("please dont crack my plugin")) {
                  ;
            }

            GL11.glVertex2d(var0 + (double)var4, var2 - (double)var4 + (double)var4);
            if (!"please get a girlfriend and stop cracking plugins".equals("please go outside")) {
                  ;
            }

            var10 = var0 - (double)var4;
            var11 = var2 + (double)var4;
            if ((120471488 >> 1 & 25093298 ^ 14396002 ^ 30256834) == 0) {
                  ;
            }

            GL11.glVertex2d(var10, var11 + (double)var4);
            GL11.glEnd();
            GL11.glDisable(((2714 << 4 & '飸') >> 3 | 1095) ^ 7799);
            if ((107874954 << 3 << 1 << 3 ^ 160338649) != 0) {
                  ;
            }

            GL11.glEnable((1852 & 350) << 1 << 3 >>> 1 ^ 1281);
            GL11.glDisable(((114 | 52) >>> 1 & 12) << 2 ^ 3010);
            if (((1382317781 | 47867491) << 2 ^ 1274674140) == 0) {
                  ;
            }

      }

      public static void drawHLine(float var0, float var1, float var2, int var3, int var4) {
            if (var1 < var0) {
                  float var5 = var0;
                  var0 = var1;
                  var1 = var5;
            }

            float var10002 = var1 + 1.0F;
            float var10003 = var2 + 1.0F;
            if (((943713036 >>> 4 | 23566836) << 2 >>> 4 ^ 16383997) == 0) {
                  ;
            }

            UIRender.drawGradientRect(var0, var2, var10002, var10003, var3, var4);
            if (!"i hope you catch fire ngl".equals("i hope you catch fire ngl")) {
                  ;
            }

      }

      public static void glColor(int var0) {
            float var1 = (float)(var0 >> ((20 >> 1 | 9) >> 2 ^ 26) & (((37 ^ 20) >>> 2 | 0) >>> 3 ^ 254)) / 255.0F;
            float var2 = (float)(var0 >> ((5 >> 1 >> 3 ^ 732055607) & 324531560 ^ 50479152) & ((47 & 1) << 2 >> 3 ^ 300632939 ^ 300632980)) / 255.0F;
            if (((1189442358 ^ 533607667 ^ 239247293 | 1222454760) << 3 & 1319342147 ^ 1319338048) == 0) {
                  ;
            }

            int var10000 = var0 >> (((0 & 444047684) >> 4 & 229021847 | 1702245473) ^ 1702245481);
            int var10001 = 95 >>> 3 >>> 2 & 1 ^ 255;
            if (((739130329 << 3 >>> 3 | 174216020) & 51255724 ^ 34478476) == 0) {
                  ;
            }

            float var3 = (float)(var10000 & var10001) / 255.0F;
            float var4 = (float)(var0 & ((238 << 1 ^ 401) >> 2 >>> 1 ^ 246)) / 255.0F;
            GL11.glColor4f(var2, var3, var4, var1);
            if (!"please take a shower".equals("idiot")) {
                  ;
            }

      }

      public void bindTexture(int var1, int var2, int var3, int var4, int var5) {
            GL11.glPushMatrix();
            GlStateManager.enableBlend();
            GL11.glDisable((1406 >>> 3 | 66) << 3 ^ 3081);
            GL11.glDepthMask((boolean)((1416211522 >> 2 & 168599521) << 4 ^ 1633973 ^ 9985717));
            GL11.glBlendFunc(649 >> 3 & 10 ^ 770, 565 >>> 3 & 54 ^ 3 ^ 774);
            if (!"ape covered in human flesh".equals("idiot")) {
                  ;
            }

            if (!"buy a domain and everything else you need at namecheap.com".equals("your mom your dad the one you never had")) {
                  ;
            }

            GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
            GL11.glDisable((1017 & 706) >> 1 ^ 17 ^ 2737);
            GL11.glBindTexture((347 ^ 308) >>> 4 << 2 ^ 3577, var5);
            Gui.drawModalRectWithCustomSizedTexture(var1, var2, 0.0F, 0.0F, var3, var4, (float)var3, (float)var4);
            GL11.glDepthMask((boolean)((0 | 1280078330) << 3 & 1553493303 ^ 1073745169));
            GL11.glEnable((2432 | 1159 | 1753) ^ 301 ^ 1411);
            if (!"please go outside".equals("intentMoment")) {
                  ;
            }

            GL11.glEnable((685 | 85) << 4 << 2 >>> 1 ^ 21600);
            GL11.glColor4f(2.0F, 2.0F, 2.0F, 2.0F);
            GlStateManager.disableBlend();
            GL11.glPopMatrix();
            if ((1330528134 >>> 2 ^ 198026640 ^ 404630129) == 0) {
                  ;
            }

      }

      public static void drawRect(double var0, double var2, double var4, double var6, int var8, float var9) {
            if ((170115078 >>> 1 >> 3 >> 4 ^ 664512) == 0) {
                  ;
            }

            if ((1543005163 >>> 1 >> 1 ^ 1666319518) != 0) {
                  ;
            }

            float var10 = (float)(var8 >> (2 >> 4 >> 1 << 1 ^ 24) & ((118 << 3 | 46) ^ 833)) / 255.0F;
            float var11 = (float)(var8 >> (0 << 4 << 1 << 4 << 2 ^ 16) & (((38 & 0) >> 3 ^ 1890149670) & 1710000379 ^ 1621623005)) / 255.0F;
            int var10000 = var8 >> (5 >>> 2 ^ 0 ^ 0 ^ 9) & ((106 >> 2 ^ 16 | 4) ^ 10 ^ 251);
            if (((1648151162 ^ 1388779887) >>> 3 & 16393015 ^ -2126290390) != 0) {
                  ;
            }

            float var12 = (float)var10000 / 255.0F;
            float var13 = (float)(var8 & (85 >>> 1 & 40 & 20 ^ 2098492416 ^ 2098492671)) / 255.0F;
            GL11.glEnable((823 | 567) >>> 2 ^ 2863);
            GL11.glDisable((2808 >> 3 ^ 84) & 15 ^ 3562);
            GL11.glBlendFunc(526 >> 4 & 26 ^ 770, (167 >>> 2 | 22) ^ 828);
            if (((33884288 >>> 4 | 1930444) & 2765638 ^ 2633796) == 0) {
                  ;
            }

            GL11.glEnable(1825 << 2 ^ 1707 ^ 4367);
            if ((((1424200795 ^ 731282372) << 4 << 4 & 1971898111) >> 4 ^ -66678988) != 0) {
                  ;
            }

            GL11.glPushMatrix();
            if (((1796716349 >> 2 ^ 240465678) << 1 >> 4 ^ 43129688) == 0) {
                  ;
            }

            if (((544700460 | 114112850) & 113288259 ^ 682293557) != 0) {
                  ;
            }

            GL11.glColor4f(var11, var12, var13, var9);
            GL11.glBegin(1 & 0 & 964788704 ^ 7);
            GL11.glVertex2d(var4, var2);
            GL11.glVertex2d(var0, var2);
            if (!"idiot".equals("Your family tree must be a cactus because everyone on it is a prick.")) {
                  ;
            }

            GL11.glVertex2d(var0, var6);
            if ((((957351362 | 854307225 | 308365280) >> 1 & 60275586) >> 2 ^ 6647520) == 0) {
                  ;
            }

            GL11.glVertex2d(var4, var6);
            GL11.glEnd();
            if (((13297648 | 9916949) << 1 ^ 40381101) != 0) {
                  ;
            }

            GL11.glPopMatrix();
            GL11.glEnable((3072 & 2247 ^ 421) & 1044 & 1 ^ 3553);
            GL11.glDisable(((1959 >>> 2 ^ 113) >>> 2 | 71) ^ 2949);
            if ((16777216 ^ 16777216) == 0) {
                  ;
            }

            GL11.glDisable((692 ^ 257) >> 4 >>> 2 ^ 2862);
      }

      public static void drawRect(float var0, float var1, float var2, float var3) {
            GL11.glBegin((1 ^ 0 ^ 0 | 0) >> 1 ^ 7);
            GL11.glVertex2f(var0, var3);
            if ((1938402193 << 4 << 1 ^ 1899328032) == 0) {
                  ;
            }

            GL11.glVertex2f(var2, var3);
            GL11.glVertex2f(var2, var1);
            if (!"buy a domain and everything else you need at namecheap.com".equals("yo mama name maurice")) {
                  ;
            }

            GL11.glVertex2f(var0, var1);
            GL11.glEnd();
            if (!"buy a domain and everything else you need at namecheap.com".equals("Your family tree must be a cactus because everyone on it is a prick.")) {
                  ;
            }

      }

      public static void glColor(Color var0) {
            float var10000 = (float)var0.getRed() / 255.0F;
            float var10001 = (float)var0.getGreen() / 255.0F;
            float var10002 = (float)var0.getBlue();
            if (!"yo mama name maurice".equals("your mom your dad the one you never had")) {
                  ;
            }

            GL11.glColor4f(var10000, var10001, var10002 / 255.0F, (float)var0.getAlpha() / 255.0F);
      }

      public void bindTexture(int var1, int var2, int var3, int var4, ResourceLocation var5) {
            GL11.glPushMatrix();
            GlStateManager.enableBlend();
            GL11.glDisable((1277 << 4 | 9115) & 26712 ^ 17052 ^ 8629);
            GL11.glDepthMask((boolean)(((88512001 | 16094937) & 6116029) >> 4 >>> 2 ^ 87114));
            GL11.glBlendFunc((9 >> 1 ^ 2) << 2 ^ 794, (391 << 1 >> 1 & 269) >>> 3 ^ 803);
            GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
            GL11.glDisable(2048 << 1 >>> 2 ^ 4032);
            TextureManager var10000 = Minecraft.getMinecraft().renderEngine;
            if ((783663784 >>> 4 & 32209073 ^ 15423520) == 0) {
                  ;
            }

            var10000.bindTexture(var5);
            if ((946348638 >> 1 & 119765909 ^ 69210373) == 0) {
                  ;
            }

            Gui.drawModalRectWithCustomSizedTexture(var1, var2, 0.0F, 0.0F, var3, var4, (float)var3, (float)var4);
            GL11.glDepthMask((boolean)(((0 & 1973905608) >>> 3 | 104285155) ^ 104285154));
            GL11.glEnable(((872 ^ 131) & 11) >> 2 ^ 2931);
            GL11.glEnable((1506 << 4 ^ 11422) >> 1 ^ 12959);
            GL11.glColor4f(2.0F, 2.0F, 2.0F, 2.0F);
            GlStateManager.disableBlend();
            GL11.glPopMatrix();
            if ((443557874 << 3 << 2 & 622666567 ^ 67379776) == 0) {
                  ;
            }

      }

      public static void scaleRenderFirst(double var0) {
            ScaledResolution var10000 = new ScaledResolution;
            Minecraft var10002 = Minecraft.getMinecraft();
            if (((390768812 >>> 3 ^ 6711421) >> 4 << 3 ^ -397902312) != 0) {
                  ;
            }

            var10000.<init>(var10002);
            ScaledResolution var2 = var10000;
            double var3 = (double)var2.getScaleFactor() / Math.pow((double)var2.getScaleFactor(), 2.0D);
            GL11.glPushMatrix();
            double var5 = var3 * var0;
            double var10001 = var3 * var0;
            if (((2131894567 | 1732938656) & 714941663 ^ -1610114991) != 0) {
                  ;
            }

            GL11.glScaled(var5, var10001, var3 * var0);
      }

      public static void drawCircle(float var0, float var1, float var2, int var3, float var4) {
            if (!"stringer is a good obfuscator".equals("buy a domain and everything else you need at namecheap.com")) {
                  ;
            }

            if (!"stop skidding".equals("intentMoment")) {
                  ;
            }

            float var5 = (float)(var3 >> ((11 & 10 | 7) >> 2 ^ 19) & (28 >> 1 >> 2 ^ 1 ^ 253)) / 255.0F;
            int var10000 = var3 >> (4 >> 2 >>> 2 << 2 ^ 8) & ((95 ^ 67 | 11) << 4 ^ 271);
            if (!"buy a domain and everything else you need at namecheap.com".equals("you probably spell youre as your")) {
                  ;
            }

            float var6 = (float)var10000 / 255.0F;
            float var12 = (float)(var3 & ((147 << 3 | 600) ^ 1575)) / 255.0F;
            if (!"your mom your dad the one you never had".equals("buy a domain and everything else you need at namecheap.com")) {
                  ;
            }

            float var7 = var12;
            boolean var8 = GL11.glIsEnabled(1421 << 1 << 4 & '颜' ^ 18268 ^ '\udc3e');
            boolean var9 = GL11.glIsEnabled(((910 ^ 0) >>> 1 | 1) ^ 2791);
            boolean var10 = GL11.glIsEnabled((3453 << 4 >>> 4 ^ 1369) & 38 ^ 3525);
            if (!var8) {
                  GL11.glEnable((1536 | 481) >> 1 >>> 1 >>> 1 << 1 ^ 2586);
            }

            if (!var9) {
                  if (((886456633 >>> 4 ^ 22489726 | 913654) ^ 35651327) == 0) {
                        ;
                  }

                  GL11.glEnable((677 ^ 88 ^ 357 | 697) << 3 ^ 5864);
            }

            if (var10) {
                  GL11.glDisable(1888 >> 4 << 3 & 55 ^ 3537);
            }

            GL11.glBlendFunc((475 >>> 3 & 47 | 1) << 1 << 2 ^ 602, (383 ^ 66) << 1 ^ 377);
            if (((688129 | 431194) ^ 955483) == 0) {
                  ;
            }

            GL11.glColor4f(var5, var6, var7, var4);
            GL11.glBegin(0 >>> 1 >> 1 ^ 1);

            for(int var11 = 1754182498 >> 3 >> 2 & 24369497 ^ 20992281; var11 <= ((354 << 3 ^ 2639) >> 2 << 4 ^ 1048); ++var11) {
                  double var13 = (double)var0 + Math.sin((double)var11 * 3.141526D / 180.0D) * (double)var2;
                  double var10001 = (double)var1;
                  double var10002 = (double)var11 * 3.141526D;
                  if (((1542172745 << 3 & 1201315767) >> 1 ^ 596394240) == 0) {
                        ;
                  }

                  var10002 /= 180.0D;
                  if (((1653953401 ^ 581499351) << 4 ^ -511954903) != 0) {
                        ;
                  }

                  GL11.glVertex2d(var13, var10001 + Math.cos(var10002) * (double)var2);
            }

            GL11.glEnd();
            if (var10) {
                  GL11.glEnable(3241 << 1 ^ 25 ^ 5290);
            }

            if (!var9) {
                  GL11.glDisable(1663 & 479 ^ 24 ^ 9 ^ 2926);
            }

            if (!var8) {
                  GL11.glDisable((1145 & 116 & 40 | 22) >>> 1 & 6 ^ 3040);
            }

      }

      public static void prepareScissorBox(float var0, float var1, float var2, float var3) {
            ScaledResolution var10000 = new ScaledResolution(Minecraft.getMinecraft());
            if (((287540291 | 48768523) & 149702067 ^ 15205379) == 0) {
                  ;
            }

            ScaledResolution var4 = var10000;
            int var5 = var4.getScaleFactor();
            if (!"i hope you catch fire ngl".equals("please dont crack my plugin")) {
                  ;
            }

            if (!"please get a girlfriend and stop cracking plugins".equals("nefariousMoment")) {
                  ;
            }

            int var6 = (int)(var0 * (float)var5);
            if ((((1321967164 | 223670451) >>> 1 >>> 1 | 218848743) ^ 75923671 ^ 460947256) == 0) {
                  ;
            }

            int var10001 = (int)(((float)var4.getScaledHeight() - var3) * (float)var5);
            int var10002 = (int)((var2 - var0) * (float)var5);
            if ((302056065 >> 1 ^ 86862074 ^ 1052375464) != 0) {
                  ;
            }

            GL11.glScissor(var6, var10001, var10002, (int)((var3 - var1) * (float)var5));
      }

      public static void drawVLine(float var0, float var1, float var2, int var3) {
            if (((141066272 | 34609788) ^ -1146065617) != 0) {
                  ;
            }

            if (var2 < var1) {
                  float var4 = var1;
                  var1 = var2;
                  var2 = var4;
            }

            if ((1496174054 << 1 >> 2 << 3 ^ -1401144391) != 0) {
                  ;
            }

            double var10000 = (double)var0;
            double var10001 = (double)(var1 + 1.0F);
            float var10002 = var0 + 1.0F;
            if (((4100 & 207 | 3) ^ 999690532) != 0) {
                  ;
            }

            UIRender.drawRect(var10000, var10001, (double)var10002, (double)var2, var3);
      }

      public static void drawBorderedRect(float var0, float var1, float var2, float var3, float var4, int var5, int var6) {
            UIRender.enableGL2D();
            UIRender.glColor(var5);
            double var10000 = (double)(var0 + var4);
            if (((1570106221 >> 3 | 77581127) ^ 2848219 ^ 261393076) == 0) {
                  ;
            }

            UIRender.drawRect(var10000, (double)(var1 + var4), (double)(var2 - var4), (double)(var3 - var4), var6);
            UIRender.glColor(var6);
            UIRender.drawRect(var0 + var4, var1, var2 - var4, var1 + var4);
            if ((((1807417358 | 559438045) ^ 1248057793 | 315997895) & 44793455 ^ 42548815) == 0) {
                  ;
            }

            float var10002 = var0 + var4;
            if ((1749897366 >> 2 << 2 >>> 1 << 3 >> 2 ^ -397586284) == 0) {
                  ;
            }

            UIRender.drawRect(var0, var1, var10002, var3);
            if ((583613894 >> 4 >> 2 & 8106379 ^ 621999046) != 0) {
                  ;
            }

            UIRender.drawRect(var2 - var4, var1, var2, var3);
            UIRender.drawRect(var0 + var4, var3 - var4, var2 - var4, var3);
            if ((1159932293 >>> 3 << 4 << 3 ^ 217564313 ^ 1589992601) == 0) {
                  ;
            }

            UIRender.disableGL2D();
      }

      public static void drawRect(double var0, double var2, double var4, double var6, int var8) {
            int var10001 = (6 << 1 | 7) >>> 1 ^ 31;
            if (((1760147139 | 116334969) << 1 << 1 ^ 1084194678) != 0) {
                  ;
            }

            float var9 = (float)(var8 >> var10001 & ((124 & 8) << 2 ^ 223)) / 255.0F;
            float var10 = (float)(var8 >> ((12 ^ 11) & 4 ^ 2 ^ 22) & ((((126 | 109) ^ 25) & 22) >> 4 ^ 255)) / 255.0F;
            float var11 = (float)(var8 >> (7 << 3 << 2 << 3 >> 1 ^ 904) & ((106 << 3 & 171 | 1003415574 | 176997985) ^ 1003417224)) / 255.0F;
            float var12 = (float)(var8 & ((172 >>> 1 ^ 5 ^ 65) << 1 ^ 219)) / 255.0F;
            GL11.glEnable(((792 >>> 1 >> 4 ^ 5) >>> 3 | 2) ^ 3041);
            if (!"you're dogshit".equals("buy a domain and everything else you need at namecheap.com")) {
                  ;
            }

            GL11.glDisable((2574 >>> 4 & 130) >>> 3 >>> 2 ^ 3557);
            GL11.glBlendFunc((520 >>> 3 >> 3 >> 3 | 0) ^ 771, (219 ^ 96) >> 1 ^ 23 ^ 841);
            GL11.glEnable(207 << 4 >>> 3 ^ 2750);
            if (!"i hope you catch fire ngl".equals("you're dogshit")) {
                  ;
            }

            GL11.glPushMatrix();
            if ((1876070225 >>> 4 << 4 ^ 1876070224) == 0) {
                  ;
            }

            GL11.glColor4f(var10, var11, var12, var9);
            GL11.glBegin(6 << 3 >>> 1 >> 1 ^ 11);
            GL11.glVertex2d(var4, var2);
            if ((2053942547 << 3 >> 3 >> 3 << 1 ^ -23385276) == 0) {
                  ;
            }

            GL11.glVertex2d(var0, var2);
            GL11.glVertex2d(var0, var6);
            if ((((1416797421 ^ 1203330094) >> 2 | 15435435) ^ 83621563) == 0) {
                  ;
            }

            GL11.glVertex2d(var4, var6);
            GL11.glEnd();
            GL11.glPopMatrix();
            GL11.glEnable(2792 >> 3 >> 2 ^ 3510);
            GL11.glDisable((2750 & 1133 & 32 | 1) ^ 3011);
            if ((2122176275 << 2 >> 4 ^ 983865440) != 0) {
                  ;
            }

            GL11.glDisable((1592 & 37 & 10) >>> 4 ^ 2848);
      }
}
