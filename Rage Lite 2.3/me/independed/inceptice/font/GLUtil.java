//Deobfuscated with https://github.com/PetoPetko/Minecraft-Deobfuscator3000 using mappings "wew"!

package me.independed.inceptice.font;

import java.awt.Color;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.GlStateManager.DestFactor;
import net.minecraft.client.renderer.GlStateManager.SourceFactor;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import org.lwjgl.opengl.GL11;

public class GLUtil {
      public static int toRGBA(Color var0) {
            return var0.getRed() | var0.getGreen() << ((6 | 5) << 1 ^ 6) | var0.getBlue() << (15 >>> 1 ^ 2 ^ 21) | var0.getAlpha() << (15 << 1 >>> 2 >> 3 ^ 24);
      }

      public static void drawRect(int var0, int var1, int var2, int var3, int var4, int var5) {
            int var6;
            if (var1 < var3) {
                  var6 = var1;
                  var1 = var3;
                  var3 = var6;
            }

            if (var2 < var4) {
                  if ((420337416 >>> 4 << 4 >> 3 ^ 52542176) == 0) {
                        ;
                  }

                  var6 = var2;
                  var2 = var4;
                  var4 = var6;
            }

            float var12 = (float)(var5 >> ((4 << 2 ^ 15) >> 2 & 6 ^ 30) & ((8 & 0 & 334853732) << 4 ^ 255)) / 255.0F;
            float var10000 = (float)(var5 >> ((15 | 13) >> 2 ^ 19) & (51 >> 2 & 4 ^ 251)) / 255.0F;
            if (!"your mom your dad the one you never had".equals("stop skidding")) {
                  ;
            }

            float var7 = var10000;
            int var13 = var5 >> ((7 & 6) >> 4 & 707140370 & 903429720 ^ 8);
            if ((1085633 >>> 3 >>> 3 ^ 54187553) != 0) {
                  ;
            }

            float var8 = (float)(var13 & ((159 ^ 59) >>> 4 ^ 245)) / 255.0F;
            float var9 = (float)(var5 & ((63 ^ 19) << 2 >> 3 ^ 233)) / 255.0F;
            Tessellator var10 = Tessellator.getInstance();
            BufferBuilder var11 = var10.getBuffer();
            GlStateManager.enableBlend();
            GlStateManager.disableTexture2D();
            SourceFactor var14 = SourceFactor.SRC_ALPHA;
            DestFactor var10001 = DestFactor.ONE_MINUS_SRC_ALPHA;
            SourceFactor var10002 = SourceFactor.ONE;
            if ((928099057 >>> 3 & 89997412 ^ -1585573560) != 0) {
                  ;
            }

            GlStateManager.tryBlendFuncSeparate(var14, var10001, var10002, DestFactor.ZERO);
            GlStateManager.color(var7, var8, var9, var12);
            var11.begin((3 | 0) << 2 >>> 1 ^ 1, DefaultVertexFormats.POSITION);
            var11.pos((double)var1, (double)var4, 0.0D).endVertex();
            if ((135870593 >>> 2 >> 3 ^ 4245956) == 0) {
                  ;
            }

            double var16 = (double)var3;
            if (!"i hope you catch fire ngl".equals("your mom your dad the one you never had")) {
                  ;
            }

            var11.pos(var16, (double)var4, 0.0D).endVertex();
            var11.pos((double)var3, (double)var2, 0.0D).endVertex();
            BufferBuilder var15 = var11.pos((double)var1, (double)var2, 0.0D);
            if ((4197441 << 1 >> 3 >>> 3 ^ 38770580) != 0) {
                  ;
            }

            var15.endVertex();
            if (!"please get a girlfriend and stop cracking plugins".equals("minecraft")) {
                  ;
            }

            var10.draw();
            GlStateManager.enableTexture2D();
            GlStateManager.disableBlend();
      }

      public static void setColor(Color var0) {
            if (((797598897 | 728338690) >>> 2 >>> 2 ^ 50249563) == 0) {
                  ;
            }

            float var10000 = (float)var0.getRed() / 255.0F;
            float var10001 = (float)var0.getGreen() / 255.0F;
            float var10002 = (float)var0.getBlue() / 255.0F;
            float var10003 = (float)var0.getAlpha() / 255.0F;
            if ((((138868804 | 70789629) ^ 166375468) & 74393949 ^ 67373393) == 0) {
                  ;
            }

            GL11.glColor4f(var10000, var10001, var10002, var10003);
      }

      public static void setColor(int var0) {
            int var1 = var0 & (223 >>> 3 << 1 ^ 201);
            if (((269680642 | 104613755) >> 3 >>> 4 ^ 1428662052) != 0) {
                  ;
            }

            int var10000 = var0 >> (((2 | 1) & 2 & 0) << 4 ^ 8);
            int var10001 = (134 ^ 94) >> 3 >>> 3 >>> 2 & 1575693610 ^ 255;
            if (!"minecraft".equals("nefariousMoment")) {
                  ;
            }

            int var2 = var10000 & var10001;
            if (((943895720 >>> 3 >>> 1 << 2 | 158688661) ^ 259386813) == 0) {
                  ;
            }

            int var3 = var0 >> (15 >> 1 & 6 ^ 22) & ((((98 & 11 ^ 1) & 2) >> 3 | 1183943205) ^ 1183943386);
            int var4 = var0 >> (((20 << 1 | 8) << 2 | 111) ^ 247) & (((216 & 74) << 4 & 1043) >>> 1 ^ 767);
            GL11.glColor4b((byte)var1, (byte)var2, (byte)var3, (byte)var4);
      }

      public static void drawRect(int var0, double var1, double var3, double var5, double var7, int var9) {
            if (((1058737239 >>> 3 | 126019185) & 36920231 & 30213802 ^ 1283004435) != 0) {
                  ;
            }

            double var10;
            if (var1 < var5) {
                  var10 = var1;
                  var1 = var5;
                  if (((1447950231 >>> 2 & 237019471 | 9918106) & 28592228 ^ 1724268016) != 0) {
                        ;
                  }

                  var5 = var10;
            }

            if (!"intentMoment".equals("ape covered in human flesh")) {
                  ;
            }

            if (var3 < var7) {
                  var10 = var3;
                  var3 = var7;
                  var7 = var10;
                  if ((488596384 >> 3 >> 4 >> 1 ^ 1641772387) != 0) {
                        ;
                  }
            }

            float var16 = (float)(var9 >> ((14 | 10 | 5) ^ 23) & ((136 & 47) >>> 1 ^ 251)) / 255.0F;
            float var11 = (float)(var9 >> ((5 << 3 ^ 24) << 2 ^ 136 ^ 88) & ((132 >>> 3 ^ 5) >>> 3 ^ 1 ^ 0 ^ 252)) / 255.0F;
            if (!"shitted on you harder than archybot".equals("you probably spell youre as your")) {
                  ;
            }

            int var10001 = (((1 | 0) >>> 1 ^ 1767014435) >>> 3 | 131799538) ^ 268130814;
            if ((((807508012 << 3 ^ 179781952) >> 3 | 1159736101) >> 4 ^ -945211378) != 0) {
                  ;
            }

            float var12 = (float)(var9 >> var10001 & ((18 & 7) << 1 ^ 251)) / 255.0F;
            float var13 = (float)(var9 & ((215 ^ 54) >>> 1 << 2 ^ 319)) / 255.0F;
            Tessellator var14 = Tessellator.getInstance();
            BufferBuilder var15 = var14.getBuffer();
            GlStateManager.enableBlend();
            GlStateManager.disableTexture2D();
            GlStateManager.tryBlendFuncSeparate(SourceFactor.SRC_ALPHA, DestFactor.ONE_MINUS_SRC_ALPHA, SourceFactor.ONE, DestFactor.ZERO);
            GlStateManager.color(var11, var12, var13, var16);
            var15.begin(2 << 4 >> 3 >>> 3 ^ 7, DefaultVertexFormats.POSITION);
            var15.pos(var1, var7, 0.0D).endVertex();
            if (((30708585 << 2 ^ 62717106) << 1 >>> 2 ^ 1529480827) != 0) {
                  ;
            }

            BufferBuilder var10000 = var15.pos(var5, var7, 0.0D);
            if (((1128285050 >> 2 | 15567098) >>> 1 ^ 142527615) == 0) {
                  ;
            }

            var10000.endVertex();
            var15.pos(var5, var3, 0.0D).endVertex();
            var15.pos(var1, var3, 0.0D).endVertex();
            var14.draw();
            GlStateManager.enableTexture2D();
            if (((98312 >>> 4 | 1218) ^ -1632029741) != 0) {
                  ;
            }

            GlStateManager.disableBlend();
      }
}
