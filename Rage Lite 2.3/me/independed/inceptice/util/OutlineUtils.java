//Deobfuscated with https://github.com/PetoPetko/Minecraft-Deobfuscator3000 using mappings "wew"!

package me.independed.inceptice.util;

import java.awt.Color;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.client.shader.Framebuffer;
import org.lwjgl.opengl.EXTFramebufferObject;
import org.lwjgl.opengl.GL11;

public class OutlineUtils {
      public static void renderFour() {
            OutlineUtils.setColor(new Color(((93 ^ 69) & 15) << 2 ^ 223, (91 ^ 54) >>> 1 >>> 4 ^ 252, (139 << 2 | 496) ^ 623 ^ 376 ^ 20));
            GL11.glDepthMask((boolean)('ꤓ' ^ 'ꤓ'));
            GL11.glDisable(((1626 & 1596) << 2 ^ 3819 | 70) ^ 7614);
            GL11.glEnable((6586 | 5203) << 2 ^ 24046);
            if (((878756752 | 708074372) & 752662158 ^ 536044864 ^ -1299067844) != 0) {
                  ;
            }

            GL11.glPolygonOffset(1.0F, -2000000.0F);
            OpenGlHelper.setLightmapTextureCoords(OpenGlHelper.lightmapTexUnit, 240.0F, 240.0F);
      }

      public static void renderTwo() {
            GL11.glStencilFunc(((141 << 1 ^ 153) & 113) >> 1 ^ 512, 678761615 >>> 2 << 1 ^ 339380806, (14 >> 2 << 2 >> 1 | 5) >> 3 ^ 15);
            GL11.glStencilOp(6783 << 2 >> 4 >> 3 ^ 7890, (4670 | 1713) << 1 >>> 1 ^ 2238, (1589 ^ 941 | 692) >> 2 << 4 ^ 241);
            GL11.glPolygonMode(813 >> 4 >> 3 >> 4 ^ 1032, 3416 << 4 >> 4 >>> 3 ^ 6825);
      }

      public static void renderFive() {
            GL11.glPolygonOffset(1.0F, 2000000.0F);
            if (!"i hope you catch fire ngl".equals("ape covered in human flesh")) {
                  ;
            }

            GL11.glDisable(1623 >>> 1 << 4 << 4 & 161940 ^ 90098 ^ 220656);
            GL11.glEnable((1060 & 420) >>> 2 >>> 4 << 3 ^ 2929);
            GL11.glDepthMask((boolean)((0 >> 4 | 1256164666 | 916810212) >>> 4 ^ 133168862));
            GL11.glDisable(((483 << 1 | 572) & 987 & 594) << 2 ^ 728);
            GL11.glDisable((1003 >>> 1 | 240) >>> 1 ^ 100 ^ 3006);
            if ((((1431595153 ^ 196719081) & 31542214) << 3 ^ 118114816) == 0) {
                  ;
            }

            GL11.glHint((1292 >>> 4 | 41 | 91) >>> 4 ^ 3157, (3789 & 1159) >> 3 ^ 4496);
            GL11.glEnable(89 >>> 4 >>> 3 ^ 3042);
            if ((105615397 << 2 & 106440506 ^ -954488737) != 0) {
                  ;
            }

            GL11.glEnable(2163 << 4 >>> 2 ^ 820 ^ 10664);
            GL11.glEnable((1375 ^ 1159) >>> 3 ^ 3546);
            GL11.glEnable(((3000 & 107 | 21) >> 2 | 8) ^ 3023);
            if (!"buy a domain and everything else you need at namecheap.com".equals("your mom your dad the one you never had")) {
                  ;
            }

            GL11.glPopAttrib();
      }

      public static void checkSetupFBO() {
            Framebuffer var0 = Minecraft.getMinecraft().getFramebuffer();
            if ((1619250785 >>> 1 >>> 1 >>> 4 & 8525233 ^ -1267508792) != 0) {
                  ;
            }

            if (var0 != null && var0.depthBuffer > ((((1986894240 ^ 243888679) & 473607316) >>> 3 | 44636298) ^ -61676955)) {
                  OutlineUtils.setupFBO(var0);
                  var0.depthBuffer = (2121568629 ^ 1621597034) & 356075712 ^ 79468604 ^ -279810109;
            }

      }

      public static void setupFBO(Framebuffer var0) {
            if (((1422642600 | 332551138) << 3 ^ 1015373270) != 0) {
                  ;
            }

            EXTFramebufferObject.glDeleteRenderbuffersEXT(var0.depthBuffer);
            if (!"you probably spell youre as your".equals("your mom your dad the one you never had")) {
                  ;
            }

            int var1 = EXTFramebufferObject.glGenRenderbuffersEXT();
            EXTFramebufferObject.glBindRenderbufferEXT(('膋' | 10221) >>> 1 ^ 21047 ^ '貁', var1);
            EXTFramebufferObject.glRenderbufferStorageEXT(1272 >>> 2 & 252 ^ '赽', ((5980 & 4507) >> 4 << 1 & 357 | 15) ^ '蓖', Minecraft.getMinecraft().displayWidth, Minecraft.getMinecraft().displayHeight);
            EXTFramebufferObject.glFramebufferRenderbufferEXT((5748 << 3 >>> 4 << 4 | 14089) >> 3 ^ '鮵', (27376 | 9801) >>> 2 >>> 2 ^ '诏', 11440 >>> 1 >>> 4 & 46 & 15 ^ '赅', var1);
            int var10000 = 5180 >> 1 ^ 2380 ^ '踒';
            int var10001 = 18051 >> 4 << 1 ^ '藐';
            int var10002 = (12164 & 3224 ^ 1691 | 2572) ^ '蝞';
            if (!"your mom your dad the one you never had".equals("Your family tree must be a cactus because everyone on it is a prick.")) {
                  ;
            }

            EXTFramebufferObject.glFramebufferRenderbufferEXT(var10000, var10001, var10002, var1);
      }

      public static void renderThree() {
            GL11.glStencilFunc(402 >>> 3 >>> 4 ^ 513, (0 | 1466478923 | 316180691) ^ 658910476 ^ 1891538134, ((2 | 0) ^ 1) & 1 ^ 14);
            GL11.glStencilOp((1686 & 226) >> 1 << 4 ^ 6672, 5671 >> 1 >> 3 ^ 8034, (4310 & 652 & 68) >>> 1 ^ 7682);
            if ((((1145018500 ^ 957688082) << 3 ^ 50182358) >>> 4 ^ 1909795643) != 0) {
                  ;
            }

            GL11.glPolygonMode(((81 ^ 10 | 89) ^ 8) >>> 4 >>> 2 ^ 1033, (((5386 | 4349) ^ 4075) & 3099) >> 2 ^ 6405);
      }

      public static void setColor(Color var0) {
            if (((895145801 ^ 230742724 ^ 899870571) & 67603989 ^ 59737494 ^ -1579826807) != 0) {
                  ;
            }

            double var10000 = (double)((float)var0.getRed() / 255.0F);
            float var10001 = (float)var0.getGreen() / 255.0F;
            if (((103590818 | 13580187) & 34947525 ^ 33882497) == 0) {
                  ;
            }

            double var1 = (double)var10001;
            float var10002 = (float)var0.getBlue();
            if (!"idiot".equals("ape covered in human flesh")) {
                  ;
            }

            double var2 = (double)(var10002 / 255.0F);
            if (!"minecraft".equals("your mom your dad the one you never had")) {
                  ;
            }

            GL11.glColor4d(var10000, var1, var2, (double)((float)var0.getAlpha() / 255.0F));
      }

      public static void renderOne() {
            OutlineUtils.checkSetupFBO();
            GL11.glPushAttrib((855584 << 2 & 1878455) << 1 ^ 2600703);
            GL11.glDisable((696 ^ 387) >>> 2 ^ 2830);
            GL11.glDisable(((2980 | 1692) ^ 1167) & 2485 ^ 1232);
            GL11.glDisable(761 >> 1 >>> 4 ^ 2887);
            GL11.glEnable((956 >> 1 | 39) ^ 2589);
            GL11.glBlendFunc((565 & 540) << 4 ^ 8770, (550 | 468) >> 4 ^ 828);
            GL11.glLineWidth(3.0F);
            GL11.glEnable(2286 << 4 >>> 1 >> 2 << 4 ^ '龰' ^ 100688);
            GL11.glEnable(((1554 | 1339) & 1777) >> 4 ^ 3059);
            GL11.glClear((202 >>> 2 | 12) << 2 >>> 2 ^ 43 ^ 1045);
            GL11.glClearStencil(((7 | 0) << 3 >>> 4 | 2) ^ 12);
            GL11.glStencilFunc(((249 | 234) >> 4 >> 2 | 2) << 4 ^ 560, 0 << 2 >> 3 >>> 1 ^ 1198641986 ^ 1198641987, (6 << 2 >>> 1 | 8) >> 4 ^ 15);
            GL11.glStencilOp((5386 | 3258) << 1 ^ 9589, (4697 << 3 >>> 3 ^ 61) & 2984 ^ 7201, 7496 >>> 2 & 1565 ^ 627 ^ 6754);
            GL11.glPolygonMode((895 & 846) >>> 4 ^ 1084, (((5183 | 133) ^ 2806) & 6484) << 4 ^ 106241);
      }
}
