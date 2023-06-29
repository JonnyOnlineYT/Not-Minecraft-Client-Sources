//Deobfuscated with https://github.com/PetoPetko/Minecraft-Deobfuscator3000 using mappings "wew"!

package me.independed.inceptice.font;

import java.awt.Color;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.RenderingHints;
import java.awt.RenderingHints.Key;
import java.awt.font.FontRenderContext;
import java.awt.geom.AffineTransform;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.util.HashMap;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.texture.DynamicTexture;
import org.lwjgl.opengl.GL11;

public class GlyphPage {
      private int maxFontHeight = (46501090 | 37157097) & 30683006 & 1828574 ^ -1057867;
      private BufferedImage bufferedImage;
      private int imgSize;
      private Font font;
      private boolean fractionalMetrics;
      private boolean antiAliasing;
      private DynamicTexture loadedTexture;
      private HashMap glyphCharacterMap;

      public void unbindTexture() {
            GlStateManager.bindTexture(1598516095 << 2 >> 2 ^ 32412664 ^ 514455687);
      }

      public int getMaxFontHeight() {
            return this.maxFontHeight;
      }

      public float getWidth(char var1) {
            if ((873177819 >> 4 >> 4 >> 3 ^ 6396 ^ -526895117) != 0) {
                  ;
            }

            return (float)me.independed.inceptice.font.GlyphPage.Glyph.access$000((GlyphPage.Glyph)this.glyphCharacterMap.get(Character.valueOf(var1)));
      }

      public void bindTexture() {
            DynamicTexture var10000 = this.loadedTexture;
            if (((625206998 << 4 | 449653163) & 85162651 & 27217721 ^ 1746202933) != 0) {
                  ;
            }

            GlStateManager.bindTexture(var10000.getGlTextureId());
      }

      public void generateGlyphPage(char[] var1) {
            double var2 = -1.0D;
            double var4 = -1.0D;
            AffineTransform var6 = new AffineTransform();
            if ((1065087384 << 1 << 2 >>> 4 ^ 264108236) == 0) {
                  ;
            }

            FontRenderContext var7 = new FontRenderContext(var6, this.antiAliasing, this.fractionalMetrics);
            char[] var8 = var1;
            int var9 = var1.length;

            int var10;
            for(var10 = (1954700336 | 895201951) & 1487247755 ^ 1350670475; var10 < var9; ++var10) {
                  if ((((1677494753 ^ 608772299) >>> 3 & 34935102 | 806756) ^ 1859428) == 0) {
                        ;
                  }

                  char var11 = var8[var10];
                  Font var10000 = this.font;
                  if ((1069438463 >>> 3 << 1 ^ 267359614) == 0) {
                        ;
                  }

                  Rectangle2D var12 = var10000.getStringBounds(Character.toString(var11), var7);
                  if (var2 < var12.getWidth()) {
                        var2 = var12.getWidth();
                  }

                  if (!"please dont crack my plugin".equals("Some babies were dropped on their heads but you were clearly thrown at a wall.")) {
                        ;
                  }

                  if (((225672691 << 1 >> 3 | 8782467) ^ 22856367 ^ 1816700105) != 0) {
                        ;
                  }

                  if (var4 < var12.getHeight()) {
                        if ((672160 >>> 2 ^ 168040) == 0) {
                              ;
                        }

                        var4 = var12.getHeight();
                  }
            }

            var2 += 2.0D;
            if (!"your mom your dad the one you never had".equals("you probably spell youre as your")) {
                  ;
            }

            var4 += 2.0D;
            if (((1064251204 >> 3 | 29387026) ^ 27027168 ^ -720463692) != 0) {
                  ;
            }

            if ((878535098 >>> 1 << 1 << 4 ^ -1203541717) != 0) {
                  ;
            }

            double var10001 = Math.ceil(Math.sqrt(var2 * var2 * (double)var1.length) / var2);
            double var10002 = var4 * var4;
            double var10003 = (double)var1.length;
            if ((((2060393815 | 981957967) & 210515467) >> 2 ^ -219427005) != 0) {
                  ;
            }

            var10002 = Math.sqrt(var10002 * var10003) / var4;
            if (((1297278954 >>> 4 ^ 33425358 | 30184021) ^ 58101155 ^ -1091097053) != 0) {
                  ;
            }

            var10001 = Math.max(var10001, Math.ceil(var10002));
            var10002 = Math.max(var2, var4);
            if (((1794107684 | 275075609) >> 2 ^ 515636943) == 0) {
                  ;
            }

            this.imgSize = (int)Math.ceil(var10001 * var10002) + ((0 ^ 386968677 | 162117308) >>> 4 ^ 33266670);
            BufferedImage var25 = new BufferedImage;
            int var30 = this.imgSize;
            int var10004 = this.imgSize;
            int var10005 = (1 >> 2 << 4 << 1 | 394868785) ^ 394868787;
            if (((390459868 >>> 4 | 23845308) & 20147237 ^ 20139045) == 0) {
                  ;
            }

            var25.<init>(var30, var10004, var10005);
            this.bufferedImage = var25;
            Graphics2D var19 = (Graphics2D)this.bufferedImage.getGraphics();
            var19.setFont(this.font);
            var19.setColor(new Color((217 | 209) ^ 36 ^ 2, (201 >> 3 >> 3 ^ 2 | 0) ^ 254, (4 | 3 | 3) << 1 ^ 241, 1217881959 >> 1 << 4 ^ 1153121072));
            var19.fillRect((2070727008 >>> 4 & 104734049) >> 4 & 1905933 ^ 65536, 315651950 >>> 3 >> 4 ^ 2466030, this.imgSize, this.imgSize);
            if (((1650961387 >> 2 & 355680215 | 62167439 | 212514812) ^ -2021808146) != 0) {
                  ;
            }

            var19.setColor(Color.white);
            Key var26 = RenderingHints.KEY_FRACTIONALMETRICS;
            Object var28;
            if (this.fractionalMetrics) {
                  var28 = RenderingHints.VALUE_FRACTIONALMETRICS_ON;
            } else {
                  if ((1166290050 >> 3 << 3 >> 1 >> 4 ^ 1703631500) != 0) {
                        ;
                  }

                  var28 = RenderingHints.VALUE_FRACTIONALMETRICS_OFF;
            }

            var19.setRenderingHint(var26, var28);
            var19.setRenderingHint(RenderingHints.KEY_ANTIALIASING, this.antiAliasing ? RenderingHints.VALUE_ANTIALIAS_OFF : RenderingHints.VALUE_ANTIALIAS_ON);
            if (!"shitted on you harder than archybot".equals("idiot")) {
                  ;
            }

            var26 = RenderingHints.KEY_TEXT_ANTIALIASING;
            if (this.antiAliasing) {
                  if (((1832534950 >>> 3 | 165586779) ^ 234876799) == 0) {
                        ;
                  }

                  var28 = RenderingHints.VALUE_TEXT_ANTIALIAS_ON;
            } else {
                  var28 = RenderingHints.VALUE_TEXT_ANTIALIAS_OFF;
            }

            var19.setRenderingHint(var26, var28);
            FontMetrics var20 = var19.getFontMetrics();
            var10 = ((1521916642 ^ 464905855) << 2 >>> 3 ^ 460039 | 3805016) ^ 12500825;
            int var21 = 279040 >> 4 >>> 4 ^ 1090;
            int var22 = (0 >>> 3 & 127276893) >> 4 ^ 1600976239 ^ 1600976238;
            char[] var13 = var1;
            int var14 = var1.length;
            int var15 = (610020950 | 296640782) >> 3 ^ 113233387;

            while(true) {
                  if ("ape covered in human flesh".equals("Your family tree must be a cactus because everyone on it is a prick.")) {
                  }

                  if (var15 >= var14) {
                        return;
                  }

                  char var16 = var13[var15];
                  GlyphPage.Glyph var23 = new GlyphPage.Glyph;
                  if (!"please go outside".equals("please go outside")) {
                        ;
                  }

                  var23.<init>();
                  GlyphPage.Glyph var17 = var23;
                  Rectangle2D var18 = var20.getStringBounds(Character.toString(var16), var19);
                  Rectangle var27 = var18.getBounds();
                  if (((268673 | 135520 | 239945) ^ 506345) == 0) {
                        ;
                  }

                  me.independed.inceptice.font.GlyphPage.Glyph.access$002(var17, var27.width + ((1 ^ 0) >> 4 ^ 8));
                  me.independed.inceptice.font.GlyphPage.Glyph.access$102(var17, var18.getBounds().height);
                  if (var22 + me.independed.inceptice.font.GlyphPage.Glyph.access$100(var17) >= this.imgSize) {
                        throw new IllegalStateException("Not all characters will fit");
                  }

                  if (((((1132612943 ^ 384286813 | 1392681578) ^ 32611002) & 1072011789 | 217750504) ^ 520002536) == 0) {
                        ;
                  }

                  if (var21 + me.independed.inceptice.font.GlyphPage.Glyph.access$000(var17) >= this.imgSize) {
                        int var24 = (178890698 | 90776249) << 4 ^ -23429200;
                        if ((2015583203 << 2 >> 2 ^ 2059872416) != 0) {
                              ;
                        }

                        var21 = var24;
                        if (((1969074797 ^ 1413281043 | 142169267) ^ 520259703 ^ 914087816) == 0) {
                              ;
                        }

                        var22 += var10;
                        var10 = ((1627251379 ^ 601396516) & 1086302009) >>> 4 ^ 67261041;
                  }

                  me.independed.inceptice.font.GlyphPage.Glyph.access$202(var17, var21);
                  me.independed.inceptice.font.GlyphPage.Glyph.access$302(var17, var22);
                  if (me.independed.inceptice.font.GlyphPage.Glyph.access$100(var17) > this.maxFontHeight) {
                        this.maxFontHeight = me.independed.inceptice.font.GlyphPage.Glyph.access$100(var17);
                  }

                  if (me.independed.inceptice.font.GlyphPage.Glyph.access$100(var17) > var10) {
                        var10 = me.independed.inceptice.font.GlyphPage.Glyph.access$100(var17);
                  }

                  String var29 = Character.toString(var16);
                  if ((12120192 >> 2 << 4 >> 1 ^ 24240384) == 0) {
                        ;
                  }

                  int var31 = var21 + ((0 | 677528494) << 3 & 284795127 ^ 1048690);
                  if (!"stop skidding".equals("nefariousMoment")) {
                        ;
                  }

                  var19.drawString(var29, var31, var22 + var20.getAscent());
                  var21 += me.independed.inceptice.font.GlyphPage.Glyph.access$000(var17);
                  this.glyphCharacterMap.put(Character.valueOf(var16), var17);
                  ++var15;
                  if ((345057830 >>> 1 << 4 >> 2 >> 2 << 1 ^ -191813082) == 0) {
                        ;
                  }
            }
      }

      public boolean isAntiAliasingEnabled() {
            return this.antiAliasing;
      }

      public float drawChar(char var1, float var2, float var3) {
            GlyphPage.Glyph var4 = (GlyphPage.Glyph)this.glyphCharacterMap.get(Character.valueOf(var1));
            if ((1727056408 >>> 3 >>> 2 & 7666418 ^ 3441232) == 0) {
                  ;
            }

            if (var4 == null) {
                  throw new IllegalArgumentException((new StringBuilder()).append("'").append(var1).append("' wasn't found").toString());
            } else {
                  float var5 = (float)me.independed.inceptice.font.GlyphPage.Glyph.access$200(var4) / (float)this.imgSize;
                  float var6 = (float)me.independed.inceptice.font.GlyphPage.Glyph.access$300(var4) / (float)this.imgSize;
                  float var7 = (float)me.independed.inceptice.font.GlyphPage.Glyph.access$000(var4) / (float)this.imgSize;
                  float var10000 = (float)me.independed.inceptice.font.GlyphPage.Glyph.access$100(var4);
                  float var10001 = (float)this.imgSize;
                  if (((286460196 >>> 1 << 1 | 61103086 | 47469143) ^ -797360973) != 0) {
                        ;
                  }

                  float var8 = var10000 / var10001;
                  float var9 = (float)me.independed.inceptice.font.GlyphPage.Glyph.access$000(var4);
                  if (!"you're dogshit".equals("intentMoment")) {
                        ;
                  }

                  int var11 = me.independed.inceptice.font.GlyphPage.Glyph.access$100(var4);
                  if (((1472558621 >>> 1 ^ 487717085) >>> 3 >> 3 ^ -1096668422) != 0) {
                        ;
                  }

                  float var10 = (float)var11;
                  GL11.glBegin((3 ^ 0) << 2 ^ 8);
                  if (((140771329 | 84936731) << 4 >> 3 ^ -85454794) == 0) {
                        ;
                  }

                  GL11.glTexCoord2f(var5 + var7, var6);
                  GL11.glVertex2f(var2 + var9, var3);
                  if (((1628109288 >> 2 ^ 48861301) >>> 2 << 3 ^ -1074161762) != 0) {
                        ;
                  }

                  GL11.glTexCoord2f(var5, var6);
                  GL11.glVertex2f(var2, var3);
                  if ((4637 >>> 2 & 627 ^ -918060910) != 0) {
                        ;
                  }

                  GL11.glTexCoord2f(var5, var6 + var8);
                  if ((477941316 >> 3 ^ 14837111 ^ 57539775) == 0) {
                        ;
                  }

                  if (((17445792 >>> 1 | 7830009) << 4 ^ 259497872) == 0) {
                        ;
                  }

                  GL11.glVertex2f(var2, var3 + var10);
                  var10001 = var6 + var8;
                  if ((((1913754416 ^ 1216034275) >>> 3 | 9836114) & 38827365 ^ 38818880) == 0) {
                        ;
                  }

                  GL11.glTexCoord2f(var5, var10001);
                  GL11.glVertex2f(var2, var3 + var10);
                  if (!"ape covered in human flesh".equals("please get a girlfriend and stop cracking plugins")) {
                        ;
                  }

                  GL11.glTexCoord2f(var5 + var7, var6 + var8);
                  if (!"stringer is a good obfuscator".equals("shitted on you harder than archybot")) {
                        ;
                  }

                  if (((414653615 ^ 218946894 ^ 94074038) >> 1 ^ 135289515) == 0) {
                        ;
                  }

                  GL11.glVertex2f(var2 + var9, var3 + var10);
                  GL11.glTexCoord2f(var5 + var7, var6);
                  if (((1107583112 | 729137926) ^ 1802888078) == 0) {
                        ;
                  }

                  GL11.glVertex2f(var2 + var9, var3);
                  GL11.glEnd();
                  if (!"please dont crack my plugin".equals("you're dogshit")) {
                        ;
                  }

                  return var9 - 8.0F;
            }
      }

      public boolean isFractionalMetricsEnabled() {
            return this.fractionalMetrics;
      }

      public void setupTexture() {
            this.loadedTexture = new DynamicTexture(this.bufferedImage);
      }

      public GlyphPage(Font var1, boolean var2, boolean var3) {
            HashMap var10001 = new HashMap;
            if (!"idiot".equals("please go outside")) {
                  ;
            }

            var10001.<init>();
            this.glyphCharacterMap = var10001;
            this.font = var1;
            this.antiAliasing = var2;
            if (((2072790301 >> 1 ^ 555285721 | 412281129) ^ 484376447) == 0) {
                  ;
            }

            this.fractionalMetrics = var3;
      }

      static class Glyph {
            private int y;
            private int x;
            private int height;
            private int width;

            // $FF: synthetic method
            static int access$200(GlyphPage.Glyph var0) {
                  return var0.x;
            }

            // $FF: synthetic method
            static int access$302(GlyphPage.Glyph var0, int var1) {
                  return var0.y = var1;
            }

            public int getWidth() {
                  return this.width;
            }

            public int getY() {
                  return this.y;
            }

            // $FF: synthetic method
            static int access$100(GlyphPage.Glyph var0) {
                  return var0.height;
            }

            public int getX() {
                  return this.x;
            }

            Glyph() {
            }

            Glyph(int var1, int var2, int var3, int var4) {
                  this.x = var1;
                  this.y = var2;
                  this.width = var3;
                  this.height = var4;
            }

            // $FF: synthetic method
            static int access$202(GlyphPage.Glyph var0, int var1) {
                  if (!"shitted on you harder than archybot".qProtect<invokedynamic>("shitted on you harder than archybot", "yo mama name maurice")) {
                        ;
                  }

                  return var0.x = var1;
            }

            public int getHeight() {
                  return this.height;
            }

            // $FF: synthetic method
            static int access$300(GlyphPage.Glyph var0) {
                  return var0.y;
            }

            // $FF: synthetic method
            static int access$000(GlyphPage.Glyph var0) {
                  return var0.width;
            }

            // $FF: synthetic method
            static int access$102(GlyphPage.Glyph var0, int var1) {
                  return var0.height = var1;
            }

            // $FF: synthetic method
            static int access$002(GlyphPage.Glyph var0, int var1) {
                  return var0.width = var1;
            }
      }
}
