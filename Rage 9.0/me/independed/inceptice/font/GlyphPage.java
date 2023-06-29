//Deobfuscated with https://github.com/PetoPetko/Minecraft-Deobfuscator3000 using mappings "wew"!

package me.independed.inceptice.font;

import java.awt.Color;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.font.FontRenderContext;
import java.awt.geom.AffineTransform;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.util.HashMap;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.texture.DynamicTexture;
import org.lwjgl.opengl.GL11;

public class GlyphPage {
      private BufferedImage bufferedImage;
      private DynamicTexture loadedTexture;
      private int maxFontHeight;
      private boolean fractionalMetrics;
      private HashMap glyphCharacterMap;
      private Font font;
      private int imgSize;
      private boolean antiAliasing;

      public void setupTexture() {
            DynamicTexture var10001 = new DynamicTexture(this.bufferedImage);
            if ((5776387 << 1 << 1 ^ 2131038506) != 0) {
                  ;
            }

            this.loadedTexture = var10001;
      }

      public void generateGlyphPage(char[] var1) {
            if (((1318856067 | 384596310) ^ 679088232 ^ 1988521407) == 0) {
                  ;
            }

            double var2 = -1.0D;
            double var4 = -1.0D;
            AffineTransform var6 = new AffineTransform();
            FontRenderContext var10000 = new FontRenderContext;
            boolean var10003 = this.antiAliasing;
            boolean var10004 = this.fractionalMetrics;
            if (!"please get a girlfriend and stop cracking plugins".equals("You're so fat whenever you go to the beach the tide comes in.")) {
                  ;
            }

            var10000.<init>(var6, var10003, var10004);
            FontRenderContext var7 = var10000;
            char[] var8 = var1;
            int var9 = var1.length;

            int var10;
            double var10001;
            for(var10 = (570086049 >> 4 >> 4 & 750798) << 3 ^ 755216; var10 < var9; ++var10) {
                  char var11 = var8[var10];
                  Rectangle2D var12 = this.font.getStringBounds(Character.toString(var11), var7);
                  if ((((668928250 | 223232043 | 135108276) & 793828248) >> 4 ^ 49614249) == 0) {
                        ;
                  }

                  var10001 = var12.getWidth();
                  if ((((585231183 ^ 200826585) << 1 >>> 4 | 55876621) ^ 125288127) == 0) {
                        ;
                  }

                  if (var2 < var10001) {
                        var2 = var12.getWidth();
                  }

                  if (var4 < var12.getHeight()) {
                        var4 = var12.getHeight();
                  }

                  if (!"idiot".equals("idiot")) {
                        ;
                  }
            }

            var2 += 2.0D;
            if (((854359413 << 2 | 1911509727) ^ -67110945) == 0) {
                  ;
            }

            var4 += 2.0D;
            if (!"i hope you catch fire ngl".equals("yo mama name maurice")) {
                  ;
            }

            var10001 = Math.max(Math.ceil(Math.sqrt(var2 * var2 * (double)var1.length) / var2), Math.ceil(Math.sqrt(var4 * var4 * (double)var1.length) / var4));
            if ((338975468 << 2 >> 1 ^ 677950936) == 0) {
                  ;
            }

            var10001 = Math.ceil(var10001 * Math.max(var2, var4));
            if (((1928910558 | 1380334658) & 374890209 & 168262847 ^ 24885786 ^ 58451610) == 0) {
                  ;
            }

            this.imgSize = (int)var10001 + (0 << 2 & 614687314 ^ 268528721 ^ 268528720);
            if (!"yo mama name maurice".equals("intentMoment")) {
                  ;
            }

            this.bufferedImage = new BufferedImage(this.imgSize, this.imgSize, 1 << 4 << 2 ^ 66);
            if ((8388616 ^ -1704127707) != 0) {
                  ;
            }

            Graphics2D var19 = (Graphics2D)this.bufferedImage.getGraphics();
            var19.setFont(this.font);
            var19.setColor(new Color((103 ^ 72) << 3 << 1 ^ 527, 10 >> 4 << 3 << 4 << 3 ^ 255, (81 | 47) >>> 3 << 3 ^ 135, (1334647060 >>> 1 ^ 429566927) & 497888523 ^ 270591823 ^ 204259918));
            var19.fillRect((1522773392 | 889961601) ^ 1924687451 ^ 225696714, 1881169776 << 3 >>> 2 & 118731654 ^ '늀', this.imgSize, this.imgSize);
            var19.setColor(Color.white);
            var19.setRenderingHint(RenderingHints.KEY_FRACTIONALMETRICS, this.fractionalMetrics ? RenderingHints.VALUE_FRACTIONALMETRICS_ON : RenderingHints.VALUE_FRACTIONALMETRICS_OFF);
            if (!"ape covered in human flesh".equals("You're so fat whenever you go to the beach the tide comes in.")) {
                  ;
            }

            var19.setRenderingHint(RenderingHints.KEY_ANTIALIASING, this.antiAliasing ? RenderingHints.VALUE_ANTIALIAS_OFF : RenderingHints.VALUE_ANTIALIAS_ON);
            var19.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, this.antiAliasing ? RenderingHints.VALUE_TEXT_ANTIALIAS_ON : RenderingHints.VALUE_TEXT_ANTIALIAS_OFF);
            FontMetrics var20 = var19.getFontMetrics();
            if (!"stop skidding".equals("please dont crack my plugin")) {
                  ;
            }

            var10 = (109126195 >>> 3 >> 2 | 2321912) ^ 267930 ^ 3373923;
            int var21 = (1771064419 >> 1 >> 4 & 6641475) >> 1 ^ 2228513;
            int var22 = (0 | 1229590765 | 904818298) << 2 << 4 ^ 2074066881;
            if (!"idiot".equals("i hope you catch fire ngl")) {
                  ;
            }

            if ((634584797 >> 1 << 4 >> 3 ^ -1090030425) != 0) {
                  ;
            }

            char[] var13 = var1;
            int var14 = var1.length;
            int var15 = 2023665460 >> 2 ^ 408225437 ^ 108178768;
            if (!"stringer is a good obfuscator".equals("yo mama name maurice")) {
                  ;
            }

            for(; var15 < var14; ++var15) {
                  char var16 = var13[var15];
                  if (((1932591661 << 4 ^ 619562204 | 351863525) & 273774941 ^ -1297198999) != 0) {
                        ;
                  }

                  GlyphPage.Glyph var23 = new GlyphPage.Glyph;
                  if (((1969377755 >> 3 & 158208583) << 4 << 3 ^ 369164672) == 0) {
                        ;
                  }

                  var23.<init>();
                  GlyphPage.Glyph var17 = var23;
                  Rectangle2D var18 = var20.getStringBounds(Character.toString(var16), var19);
                  if ((12753 << 2 >> 1 & 13004 ^ 1899 ^ 1298507592) != 0) {
                        ;
                  }

                  int var25 = var18.getBounds().width + ((0 ^ 1004717466 | 354177786) ^ 1073666034);
                  if ((1174699466 >>> 1 << 3 & 125758390 ^ 1106720) == 0) {
                        ;
                  }

                  me.independed.inceptice.font.GlyphPage.Glyph.access$002(var17, var25);
                  var25 = var18.getBounds().height;
                  if ((675623402 >>> 4 ^ 24528664 ^ 8133219 ^ 1504100789) != 0) {
                        ;
                  }

                  me.independed.inceptice.font.GlyphPage.Glyph.access$102(var17, var25);
                  if (((1161917654 << 1 | 2129610787) >>> 3 ^ -1666681576) != 0) {
                        ;
                  }

                  if (var22 + me.independed.inceptice.font.GlyphPage.Glyph.access$100(var17) >= this.imgSize) {
                        IllegalStateException var24 = new IllegalStateException;
                        if ((((1679707606 >>> 2 & 398805985) >>> 4 | 3739581) << 3 ^ -1923869947) != 0) {
                              ;
                        }

                        var24.<init>("Not all characters will fit");
                        throw var24;
                  }

                  if (!"you probably spell youre as your".equals("your mom your dad the one you never had")) {
                        ;
                  }

                  if (((1529160045 << 4 | 987834883) ^ -1158463789) == 0) {
                        ;
                  }

                  if (var21 + me.independed.inceptice.font.GlyphPage.Glyph.access$000(var17) >= this.imgSize) {
                        var21 = (544778065 ^ 503668555) >>> 4 >>> 4 >>> 1 ^ 2047740;
                        if (!"You're so fat whenever you go to the beach the tide comes in.".equals("ape covered in human flesh")) {
                              ;
                        }

                        if (!"please take a shower".equals("Your family tree must be a cactus because everyone on it is a prick.")) {
                              ;
                        }

                        if (!"nefariousMoment".equals("buy a domain and everything else you need at namecheap.com")) {
                              ;
                        }

                        var22 += var10;
                        var10 = (1342309398 >>> 4 | 40687170) & 71958468 ^ 71890496;
                  }

                  me.independed.inceptice.font.GlyphPage.Glyph.access$202(var17, var21);
                  me.independed.inceptice.font.GlyphPage.Glyph.access$302(var17, var22);
                  if (me.independed.inceptice.font.GlyphPage.Glyph.access$100(var17) > this.maxFontHeight) {
                        if (((1066563784 >>> 1 | 502489417 | 457523802) ^ 536838015) == 0) {
                              ;
                        }

                        this.maxFontHeight = me.independed.inceptice.font.GlyphPage.Glyph.access$100(var17);
                  }

                  if (((1783809484 >>> 2 << 2 | 1671122770) & 1037415805 ^ 701597020) == 0) {
                        ;
                  }

                  if (me.independed.inceptice.font.GlyphPage.Glyph.access$100(var17) > var10) {
                        var10 = me.independed.inceptice.font.GlyphPage.Glyph.access$100(var17);
                  }

                  if ((195620002 >>> 1 << 1 >> 4 ^ 9437254 ^ 431666471) != 0) {
                        ;
                  }

                  var19.drawString(Character.toString(var16), var21 + ((1 & 0) << 2 ^ 402349773 ^ 402349775), var22 + var20.getAscent());
                  var21 += me.independed.inceptice.font.GlyphPage.Glyph.access$000(var17);
                  this.glyphCharacterMap.put(Character.valueOf(var16), var17);
                  if ((1679243376 >> 2 ^ 290167988 ^ 53563498 ^ 95772237 ^ 248142991) == 0) {
                        ;
                  }
            }

      }

      public int getMaxFontHeight() {
            return this.maxFontHeight;
      }

      public boolean isAntiAliasingEnabled() {
            return this.antiAliasing;
      }

      public GlyphPage(Font var1, boolean var2, boolean var3) {
            if ((420219800 >> 2 >> 2 ^ 9006191 ^ 525275 ^ 477360496) != 0) {
                  ;
            }

            super();
            this.maxFontHeight = ((1320424900 << 2 | 505835522) ^ 777409376) >> 2 ^ 35412929 ^ -104092382;
            if (((1686711185 >> 1 ^ 585369686 | 12006058) ^ -792331631) != 0) {
                  ;
            }

            this.glyphCharacterMap = new HashMap();
            this.font = var1;
            this.antiAliasing = var2;
            this.fractionalMetrics = var3;
      }

      public void bindTexture() {
            if (((1360565315 | 704518280) ^ 41970429 ^ 1611534960 ^ -373861318) != 0) {
                  ;
            }

            GlStateManager.bindTexture(this.loadedTexture.getGlTextureId());
            if (((1817644442 ^ 617362060) >> 3 ^ 2067257747) != 0) {
                  ;
            }

      }

      public float getWidth(char var1) {
            float var10000 = (float)me.independed.inceptice.font.GlyphPage.Glyph.access$000((GlyphPage.Glyph)this.glyphCharacterMap.get(Character.valueOf(var1)));
            if (((4305443 << 4 & 26772960 | 1376658) ^ 1031094993) != 0) {
                  ;
            }

            return var10000;
      }

      public boolean isFractionalMetricsEnabled() {
            return this.fractionalMetrics;
      }

      public void unbindTexture() {
            GlStateManager.bindTexture(((648474615 >> 3 | 63744466) & 70427136) >>> 1 ^ 34099200);
      }

      public float drawChar(char var1, float var2, float var3) {
            GlyphPage.Glyph var4 = (GlyphPage.Glyph)this.glyphCharacterMap.get(Character.valueOf(var1));
            if (var4 == null) {
                  IllegalArgumentException var12 = new IllegalArgumentException;
                  StringBuilder var10002 = (new StringBuilder()).append("'").append(var1).append("' wasn't found");
                  if (!"please take a shower".equals("shitted on you harder than archybot")) {
                        ;
                  }

                  var12.<init>(var10002.toString());
                  if ((1931090879 >> 4 >> 2 ^ 1987498166) != 0) {
                        ;
                  }

                  throw var12;
            } else {
                  if (!"your mom your dad the one you never had".equals("i hope you catch fire ngl")) {
                        ;
                  }

                  float var10000 = (float)me.independed.inceptice.font.GlyphPage.Glyph.access$200(var4) / (float)this.imgSize;
                  if ((526960763 >> 1 >> 1 << 3 >> 2 ^ 1226500738) != 0) {
                        ;
                  }

                  float var5 = var10000;
                  if ((24910581 >>> 2 << 4 >> 4 >> 3 ^ 358089218) != 0) {
                        ;
                  }

                  var10000 = (float)me.independed.inceptice.font.GlyphPage.Glyph.access$300(var4);
                  int var10001 = this.imgSize;
                  if (!"i hope you catch fire ngl".equals("you're dogshit")) {
                        ;
                  }

                  float var6 = var10000 / (float)var10001;
                  float var7 = (float)me.independed.inceptice.font.GlyphPage.Glyph.access$000(var4) / (float)this.imgSize;
                  float var8 = (float)me.independed.inceptice.font.GlyphPage.Glyph.access$100(var4) / (float)this.imgSize;
                  int var11 = me.independed.inceptice.font.GlyphPage.Glyph.access$000(var4);
                  if (((780639623 << 1 & 999960656 ^ 304565129) & 148446347 ^ -1600697730) != 0) {
                        ;
                  }

                  float var9 = (float)var11;
                  float var10 = (float)me.independed.inceptice.font.GlyphPage.Glyph.access$100(var4);
                  GL11.glBegin((0 ^ 1190990908) & 1052380538 ^ 112723004);
                  GL11.glTexCoord2f(var5 + var7, var6);
                  if (!"stringer is a good obfuscator".equals("Some babies were dropped on their heads but you were clearly thrown at a wall.")) {
                        ;
                  }

                  GL11.glVertex2f(var2 + var9, var3);
                  GL11.glTexCoord2f(var5, var6);
                  if (((747145311 ^ 225281361 ^ 160244276) >>> 4 ^ 2090442447) != 0) {
                        ;
                  }

                  GL11.glVertex2f(var2, var3);
                  GL11.glTexCoord2f(var5, var6 + var8);
                  GL11.glVertex2f(var2, var3 + var10);
                  if ((814466724 >>> 3 << 3 & 226421388 & 479511 ^ -1413266557) != 0) {
                        ;
                  }

                  if ((26294343 >> 4 >>> 3 >>> 3 ^ 1613870817) != 0) {
                        ;
                  }

                  GL11.glTexCoord2f(var5, var6 + var8);
                  if ((848957502 >> 3 << 2 >> 3 ^ 53059843) == 0) {
                        ;
                  }

                  GL11.glVertex2f(var2, var3 + var10);
                  GL11.glTexCoord2f(var5 + var7, var6 + var8);
                  GL11.glVertex2f(var2 + var9, var3 + var10);
                  GL11.glTexCoord2f(var5 + var7, var6);
                  GL11.glVertex2f(var2 + var9, var3);
                  GL11.glEnd();
                  return var9 - 8.0F;
            }
      }

      static class Glyph {
            private int x;
            private int height;
            private int width;
            private int y;

            // $FF: synthetic method
            static int access$102(GlyphPage.Glyph var0, int var1) {
                  if (!"you're dogshit".qProtect<invokedynamic>("you're dogshit", "please go outside")) {
                        ;
                  }

                  if (((2465304 >>> 4 << 2 & 500540 | 17306) ^ 7034960) != 0) {
                        ;
                  }

                  var0.height = var1;
                  return var1;
            }

            public int getX() {
                  return this.x;
            }

            Glyph() {
            }

            // $FF: synthetic method
            static int access$100(GlyphPage.Glyph var0) {
                  return var0.height;
            }

            // $FF: synthetic method
            static int access$000(GlyphPage.Glyph var0) {
                  if (((237040428 | 80403280 | 194350975) >>> 2 << 4 & 88582977 ^ 88582464) == 0) {
                        ;
                  }

                  return var0.width;
            }

            // $FF: synthetic method
            static int access$302(GlyphPage.Glyph var0, int var1) {
                  return var0.y = var1;
            }

            public int getY() {
                  int var10000 = this.y;
                  if (((1460630442 >>> 4 | 34655967) << 3 ^ -99348963) != 0) {
                        ;
                  }

                  return var10000;
            }

            // $FF: synthetic method
            static int access$202(GlyphPage.Glyph var0, int var1) {
                  return var0.x = var1;
            }

            public int getHeight() {
                  if (((250386182 | 63689812) >> 3 & 9595605 ^ 410406654) != 0) {
                        ;
                  }

                  return this.height;
            }

            // $FF: synthetic method
            static int access$300(GlyphPage.Glyph var0) {
                  return var0.y;
            }

            Glyph(int var1, int var2, int var3, int var4) {
                  this.x = var1;
                  this.y = var2;
                  this.width = var3;
                  this.height = var4;
            }

            // $FF: synthetic method
            static int access$002(GlyphPage.Glyph var0, int var1) {
                  return var0.width = var1;
            }

            // $FF: synthetic method
            static int access$200(GlyphPage.Glyph var0) {
                  return var0.x;
            }

            public int getWidth() {
                  return this.width;
            }
      }
}
