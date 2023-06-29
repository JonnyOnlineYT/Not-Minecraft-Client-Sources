//Deobfuscated with https://github.com/PetoPetko/Minecraft-Deobfuscator3000 using mappings "wew"!

package me.independed.inceptice.util;

import java.awt.Color;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.client.shader.Framebuffer;
import org.lwjgl.opengl.EXTFramebufferObject;
import org.lwjgl.opengl.GL11;

public class OutlineUtils {
      public static void checkSetupFBO() {
            Framebuffer var0 = Minecraft.getMinecraft().getFramebuffer();
            if (var0 != null) {
                  int var10000 = var0.depthBuffer;
                  if (!"ape covered in human flesh".equals("buy a domain and everything else you need at namecheap.com")) {
                        ;
                  }

                  if (var10000 > ((1210057984 | 525410632) & 145791757 ^ -137363721)) {
                        OutlineUtils.setupFBO(var0);
                        var0.depthBuffer = (885927292 | 591255051 | 561435640 | 392737116) ^ -939524096;
                  }
            }

      }

      public static void renderThree() {
            GL11.glStencilFunc(429 >> 2 >>> 2 ^ 536, 0 << 1 & 1819682373 ^ 565568782 ^ 565568783, (1 | 0) & 0 ^ 15);
            GL11.glStencilOp(7318 >>> 3 << 4 >> 1 ^ 656, (2398 & 995 | 133) & 420 ^ 8068, 2306 >> 1 & 256 ^ 7680);
            GL11.glPolygonMode(732 >>> 1 & 335 & 166 ^ 1038, (2265 ^ 2207) & 17 ^ 6913);
      }

      public static void renderTwo() {
            GL11.glStencilFunc((71 & 44) >> 2 ^ 0 ^ 0 ^ 513, 42583204 >>> 1 << 3 ^ 103764207 ^ 201867903, (10 >> 4 << 2 | 797727010) ^ 797727021);
            int var10000 = 4193 >> 2 >> 1 & 448 ^ 7681;
            if (!"You're so fat whenever you go to the beach the tide comes in.".equals("you're dogshit")) {
                  ;
            }

            GL11.glStencilOp(var10000, ((3475 | 3164) ^ 481) & 693 ^ 7733, (4405 << 2 >> 3 | 1474) ^ 5083);
            if (((1804894343 | 1060605600) >> 3 ^ -1474776186) != 0) {
                  ;
            }

            var10000 = (255 << 2 ^ 1016) & 2 ^ 1032;
            if (!"shitted on you harder than archybot".equals("stop skidding")) {
                  ;
            }

            GL11.glPolygonMode(var10000, (1667 >>> 1 | 811) & 693 ^ 6435);
      }

      public static void setColor(Color var0) {
            double var10000 = (double)((float)var0.getRed() / 255.0F);
            float var10001 = (float)var0.getGreen() / 255.0F;
            if ((((1030816448 >> 1 ^ 305312820) >>> 2 >>> 2 | 5845187) ^ 1405054133) != 0) {
                  ;
            }

            double var1 = (double)var10001;
            double var10002 = (double)((float)var0.getBlue() / 255.0F);
            if ((0 ^ 0) == 0) {
                  ;
            }

            GL11.glColor4d(var10000, var1, var10002, (double)((float)var0.getAlpha() / 255.0F));
      }

      public static void renderFive() {
            if (((1059734894 >>> 2 & 38790675) >> 4 ^ 2115504978) != 0) {
                  ;
            }

            GL11.glPolygonOffset(1.0F, 2000000.0F);
            GL11.glDisable(1473 >> 3 >>> 3 ^ 10773);
            GL11.glEnable((219 << 2 >>> 3 & 4 | 3) ^ 2934);
            GL11.glDepthMask((boolean)((0 ^ 1054822778 | 622923063) >> 2 << 2 & 675004196 ^ 674962725));
            GL11.glDisable((2630 >> 2 | 570) >> 2 & 108 ^ 3004);
            GL11.glDisable((2413 ^ 2202) >> 2 ^ 2909);
            GL11.glHint((2887 ^ 508 | 1906) ^ 937, (415 | 166 | 178) ^ 4287);
            if ((616329459 << 3 & 328120243 ^ 25331600) == 0) {
                  ;
            }

            if ((((8658171 >> 4 & 538220) << 3 | 3618856 | 6180507) ^ 8355579) == 0) {
                  ;
            }

            GL11.glEnable((774 >>> 1 & 221 | 124) ^ 2847);
            GL11.glEnable((2399 | 2017) >> 4 & 144 ^ 19 ^ 3027);
            GL11.glEnable(3167 >> 2 >>> 1 >> 3 ^ 3536);
            GL11.glEnable(((562 & 168 & 13) >>> 2 | 1027051803) ^ 1027054299);
            GL11.glPopAttrib();
      }

      public static void renderFour() {
            OutlineUtils.setColor(new Color(99 >> 3 << 4 ^ 63, 134 << 1 >>> 2 ^ 188, ((180 & 89) >> 1 ^ 0 ^ 1) >>> 4 ^ 255));
            int var10000 = 243774092 >>> 3 & 20618630 ^ 14084396 ^ 29785388;
            if (((133648 << 4 >>> 2 | 353089) ^ 414769154) != 0) {
                  ;
            }

            GL11.glDepthMask((boolean)var10000);
            GL11.glDisable((459 | 292) >> 3 << 4 << 1 ^ 3281);
            GL11.glEnable((4565 << 2 << 1 | 20722) >>> 1 ^ 17791);
            GL11.glPolygonOffset(1.0F, -2000000.0F);
            if (((1944067702 ^ 781599394) >>> 4 >>> 3 ^ -579490974) != 0) {
                  ;
            }

            var10000 = OpenGlHelper.lightmapTexUnit;
            if (!"you probably spell youre as your".equals("buy a domain and everything else you need at namecheap.com")) {
                  ;
            }

            OpenGlHelper.setLightmapTextureCoords(var10000, 240.0F, 240.0F);
            if (((10316580 ^ 1656740) >> 1 ^ 3830617 ^ 3363976 ^ 4927889) == 0) {
                  ;
            }

      }

      public static void setupFBO(Framebuffer var0) {
            EXTFramebufferObject.glDeleteRenderbuffersEXT(var0.depthBuffer);
            int var1 = EXTFramebufferObject.glGenRenderbuffersEXT();
            EXTFramebufferObject.glBindRenderbufferEXT((22572 << 4 << 2 << 1 | 2011562) ^ 4078315, var1);
            int var10000 = (1504 & 436 & 56 & 18 | 1869766805) ^ 1869802964;
            int var10001 = ((17766 ^ 3619) >> 1 ^ 1199) >> 1 >> 3 ^ '蛩';
            int var10002 = Minecraft.getMinecraft().displayWidth;
            if (!"ape covered in human flesh".equals("buy a domain and everything else you need at namecheap.com")) {
                  ;
            }

            EXTFramebufferObject.glRenderbufferStorageEXT(var10000, var10001, var10002, Minecraft.getMinecraft().displayHeight);
            EXTFramebufferObject.glFramebufferRenderbufferEXT((21278 | 219) ^ 7218 ^ '슭', ((3193 & 1353) << 2 | 2937) & 5519 ^ '鰭', (26011 & 11009) << 3 & 2803 ^ '蕁', var1);
            EXTFramebufferObject.glFramebufferRenderbufferEXT(23523 >>> 4 >>> 4 >>> 3 >>> 1 ^ '赅', (25998 | 5488) >> 4 & 606 ^ 350 ^ '踀', ('芥' >>> 1 >>> 4 | 124) ^ 18 ^ '褮', var1);
            if (((314641556 >> 3 ^ 27277340) >>> 3 ^ 8323889) == 0) {
                  ;
            }

      }

      public static void renderOne() {
            OutlineUtils.checkSetupFBO();
            int var10000 = (4148 | 696) & 2585 ^ 1048039;
            if (((887384591 >> 3 | 44428510) ^ 113110495) == 0) {
                  ;
            }

            GL11.glPushAttrib(var10000);
            GL11.glDisable((1259 | 524) << 4 ^ 25904);
            GL11.glDisable(1277 ^ 703 ^ 1449 ^ 3594);
            GL11.glDisable((1911 << 4 & 25332 & 2702 | 135) & 464 ^ 3024);
            GL11.glEnable(1316 >> 2 >> 1 << 3 ^ 3778);
            GL11.glBlendFunc((510 ^ 272) >>> 4 ^ 780, 539 >>> 4 >>> 1 >>> 2 & 3 ^ 771);
            GL11.glLineWidth(3.0F);
            GL11.glEnable((641 << 3 | 4770) ^ 4873 ^ 3715);
            GL11.glEnable(902 << 2 >> 2 >> 3 << 3 ^ 2064);
            GL11.glClear((647 ^ 107) << 3 >>> 1 ^ 4016);
            GL11.glClearStencil((13 ^ 3) >> 4 ^ 15);
            GL11.glStencilFunc(((448 | 213) ^ 281) & 71 ^ 580, (0 ^ 1430876258) << 2 >>> 3 ^ 178567216, (((8 | 2) ^ 7) >> 2 & 1) >>> 4 ^ 15);
            var10000 = 7052 >>> 3 >> 3 << 2 ^ 8121;
            int var10001 = ((1798 | 1584) ^ 821) >> 3 ^ 7809;
            if ((((966784789 | 200168635) >>> 3 | 25337005) ^ 134217471) == 0) {
                  ;
            }

            GL11.glStencilOp(var10000, var10001, ((6076 ^ 3044) << 2 | 1785) << 2 >> 3 ^ 9725);
            if (!"yo mama name maurice".equals("please dont crack my plugin")) {
                  ;
            }

            var10000 = ((933 << 4 | 6964) & 3387 ^ 564) >>> 3 ^ 1384;
            var10001 = ((3013 ^ 2310 ^ 146) >>> 2 | 36) ^ 7093;
            if ((((1720997899 >> 4 ^ 39650644 ^ 62675355) << 2 | 394538920) ^ 530870204) == 0) {
                  ;
            }

            GL11.glPolygonMode(var10000, var10001);
      }
}
