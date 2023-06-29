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
      public static void setColor(int var0) {
            if (((1222174918 ^ 19058856 | 333853237) >> 3 ^ 140686709 ^ 19111751 ^ 1877124723) != 0) {
                  ;
            }

            int var1 = var0 & ((177 & 95) >> 1 & 5 ^ 255);
            int var10000 = var0 >> (3 << 4 << 2 ^ 200);
            if ((163603977 << 2 ^ 1449280153) != 0) {
                  ;
            }

            int var2 = var10000 & ((182 >>> 1 | 15) << 4 ^ 1295);
            var10000 = var0 >> ((3 >>> 4 ^ 941549588) >> 2 >>> 4 ^ 14711728);
            int var10001 = ((100 & 45 & 1) >> 1 ^ 338223447) >> 2 ^ 84555946;
            if ((107125490 << 1 >> 4 ^ 364766875) != 0) {
                  ;
            }

            int var3 = var10000 & var10001;
            int var4 = var0 >> ((22 >> 3 ^ 1 | 1) ^ 27) & ((41 | 37) & 3 ^ 254);
            GL11.glColor4b((byte)var1, (byte)var2, (byte)var3, (byte)var4);
      }

      public static void drawRect(int var0, int var1, int var2, int var3, int var4, int var5) {
            int var6;
            if (var1 < var3) {
                  var6 = var1;
                  var1 = var3;
                  if ((((713951253 | 450596183 | 905561657) ^ 643021457) >> 2 ^ 846509797) != 0) {
                        ;
                  }

                  var3 = var6;
            }

            if (var2 < var4) {
                  var6 = var2;
                  var2 = var4;
                  if (!"Your family tree must be a cactus because everyone on it is a prick.".equals("i hope you catch fire ngl")) {
                        ;
                  }

                  var4 = var6;
            }

            float var12 = (float)(var5 >> ((15 & 8) >> 3 ^ 25) & ((21 | 3) >>> 4 ^ 0 ^ 254)) / 255.0F;
            float var7 = (float)(var5 >> ((0 >>> 1 << 3 | 2116944108) >>> 1 ^ 1058472038) & (((60 | 36) & 52 ^ 12 | 34) ^ 197)) / 255.0F;
            float var10000 = (float)(var5 >> ((5 ^ 3 | 5) ^ 15) & ((11 & 0 | 902923852 | 378109278) & 174113803 ^ 37799157));
            if ((1789490944 >> 2 >>> 4 << 2 ^ 1880252622) != 0) {
                  ;
            }

            float var8 = var10000 / 255.0F;
            if (!"you probably spell youre as your".equals("you probably spell youre as your")) {
                  ;
            }

            var10000 = (float)(var5 & ((180 << 1 >> 1 | 176) ^ 75));
            if (!"Your family tree must be a cactus because everyone on it is a prick.".equals("stop skidding")) {
                  ;
            }

            float var9 = var10000 / 255.0F;
            Tessellator var13 = Tessellator.getInstance();
            if (!"yo mama name maurice".equals("You're so fat whenever you go to the beach the tide comes in.")) {
                  ;
            }

            Tessellator var10 = var13;
            BufferBuilder var11 = var10.getBuffer();
            GlStateManager.enableBlend();
            GlStateManager.disableTexture2D();
            SourceFactor var14 = SourceFactor.SRC_ALPHA;
            DestFactor var10001 = DestFactor.ONE_MINUS_SRC_ALPHA;
            SourceFactor var10002 = SourceFactor.ONE;
            DestFactor var10003 = DestFactor.ZERO;
            if ((1891132055 << 2 >> 4 >>> 1 ^ 2115439698) == 0) {
                  ;
            }

            GlStateManager.tryBlendFuncSeparate(var14, var10001, var10002, var10003);
            if (((165742250 | 44499882) << 2 << 4 ^ -2007064646) != 0) {
                  ;
            }

            GlStateManager.color(var7, var8, var9, var12);
            if (((144703492 << 4 | 1624641813) ^ -355069611) == 0) {
                  ;
            }

            if ((965064731 >> 3 >> 4 ^ -1050851898) != 0) {
                  ;
            }

            var11.begin(6 >> 3 << 3 ^ 7, DefaultVertexFormats.POSITION);
            var11.pos((double)var1, (double)var4, 0.0D).endVertex();
            var11.pos((double)var3, (double)var4, 0.0D).endVertex();
            double var15 = (double)var3;
            if (((624763966 | 520976040) ^ 283073467 ^ 104587189) != 0) {
                  ;
            }

            var11.pos(var15, (double)var2, 0.0D).endVertex();
            var15 = (double)var1;
            if (!"i hope you catch fire ngl".equals("shitted on you harder than archybot")) {
                  ;
            }

            var11.pos(var15, (double)var2, 0.0D).endVertex();
            var10.draw();
            GlStateManager.enableTexture2D();
            GlStateManager.disableBlend();
      }

      public static int toRGBA(Color var0) {
            int var10000 = var0.getRed();
            if ((1203544805 >> 4 ^ 45366593 ^ 113472934 ^ -1654557946) != 0) {
                  ;
            }

            int var10001 = var0.getGreen() << ((6 >> 3 | 2133214591 | 975557913) << 3 & 779516012 ^ 674637920);
            if (!"You're so fat whenever you go to the beach the tide comes in.".equals("stringer is a good obfuscator")) {
                  ;
            }

            var10000 = var10000 | var10001 | var0.getBlue() << ((2 ^ 0 ^ 0) >>> 4 ^ 16);
            var10001 = var0.getAlpha();
            if (((871768463 ^ 155083700) >> 1 ^ 493101341) == 0) {
                  ;
            }

            return var10000 | var10001 << ((4 & 1 ^ 138712816 | 82843130) >> 3 << 4 ^ 434712552);
      }

      public static void drawRect(int var0, double var1, double var3, double var5, double var7, int var9) {
            double var18;
            int var10000 = (var18 = var1 - var5) == 0.0D ? 0 : (var18 < 0.0D ? -1 : 1);
            if (!"yo mama name maurice".equals("shitted on you harder than archybot")) {
                  ;
            }

            double var10;
            if (var10000 < 0) {
                  if (((161754225 >>> 4 | 2564863) ^ 5629797 ^ 403493372) != 0) {
                        ;
                  }

                  var10 = var1;
                  if (((40288133 >> 4 >>> 4 | '磻') >>> 2 ^ 139983903) != 0) {
                        ;
                  }

                  var1 = var5;
                  var5 = var10;
            }

            if (((1471534996 >>> 1 << 4 & 151093968) >> 3 ^ 1988878751) != 0) {
                  ;
            }

            if (var3 < var7) {
                  var10 = var3;
                  var3 = var7;
                  var7 = var10;
            }

            float var16 = (float)(var9 >> ((18 << 1 >> 1 | 10) ^ 2) & ((227 >>> 1 >> 1 | 8 | 16) ^ 199)) / 255.0F;
            float var11 = (float)(var9 >> ((11 >> 1 & 3 | 0) & 0 ^ 16) & (92 >> 4 >>> 4 ^ 2011543082 ^ 2011543253)) / 255.0F;
            var10000 = var9 >> ((3 & 0 | 2098870587) & 1982633421 ^ 1946685697);
            int var10001 = 239 >> 2 >> 2 >> 3 ^ 254;
            if (((906339945 ^ 99328334 ^ 646363024 | 6068384) ^ 360495287) == 0) {
                  ;
            }

            float var17 = (float)(var10000 & var10001) / 255.0F;
            if ((((784368626 | 12155669) ^ 215832779) << 3 ^ 287644128) == 0) {
                  ;
            }

            float var12 = var17;
            float var13 = (float)(var9 & ((36 >> 1 ^ 2 ^ 14) << 2 >> 1 ^ 195)) / 255.0F;
            Tessellator var14 = Tessellator.getInstance();
            if ((1507442110 >>> 4 >> 2 >>> 3 ^ -987591987) != 0) {
                  ;
            }

            BufferBuilder var15 = var14.getBuffer();
            GlStateManager.enableBlend();
            GlStateManager.disableTexture2D();
            GlStateManager.tryBlendFuncSeparate(SourceFactor.SRC_ALPHA, DestFactor.ONE_MINUS_SRC_ALPHA, SourceFactor.ONE, DestFactor.ZERO);
            GlStateManager.color(var11, var12, var13, var16);
            var15.begin((1 >>> 1 >> 1 ^ 2100756198 | 439492642) << 2 ^ -52692065, DefaultVertexFormats.POSITION);
            if ((((1003740202 ^ 107435053) & 715984077) >> 3 >> 3 >>> 3 ^ 1331716) == 0) {
                  ;
            }

            var15.pos(var1, var7, 0.0D).endVertex();
            if (!"yo mama name maurice".equals("buy a domain and everything else you need at namecheap.com")) {
                  ;
            }

            var15.pos(var5, var7, 0.0D).endVertex();
            if (((1192982247 << 3 >>> 4 ^ 12211662) >>> 2 ^ -1841060803) != 0) {
                  ;
            }

            var15.pos(var5, var3, 0.0D).endVertex();
            if (((855960859 | 635595698 | 255567889) ^ -167293353) != 0) {
                  ;
            }

            if (!"nefariousMoment".equals("shitted on you harder than archybot")) {
                  ;
            }

            var15.pos(var1, var3, 0.0D).endVertex();
            var14.draw();
            GlStateManager.enableTexture2D();
            GlStateManager.disableBlend();
      }

      public static void setColor(Color var0) {
            GL11.glColor4f((float)var0.getRed() / 255.0F, (float)var0.getGreen() / 255.0F, (float)var0.getBlue() / 255.0F, (float)var0.getAlpha() / 255.0F);
      }
}
