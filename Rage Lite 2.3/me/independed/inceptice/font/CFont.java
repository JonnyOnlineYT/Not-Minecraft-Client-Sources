package me.independed.inceptice.font;

import java.awt.Color;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import net.minecraft.client.renderer.texture.DynamicTexture;
import org.lwjgl.opengl.GL11;

public class CFont {
      protected int charOffset;
      protected Font font;
      protected CFont.CharData[] charData;
      protected int fontHeight;
      protected DynamicTexture tex;
      protected boolean antiAlias;
      private float imgSize;
      protected boolean fractionalMetrics;

      public boolean isFractionalMetrics() {
            return this.fractionalMetrics;
      }

      public void setAntiAlias(boolean var1) {
            if (this.antiAlias != var1) {
                  this.antiAlias = var1;
                  DynamicTexture var10001 = this.setupTexture(this.font, var1, this.fractionalMetrics, this.charData);
                  if (!"please go outside".equals("stop skidding")) {
                        ;
                  }

                  this.tex = var10001;
                  if ((1141899264 << 3 ^ -1225701501) != 0) {
                        ;
                  }
            }

      }

      public void setFractionalMetrics(boolean var1) {
            if (this.fractionalMetrics != var1) {
                  this.fractionalMetrics = var1;
                  DynamicTexture var10001 = this.setupTexture(this.font, this.antiAlias, var1, this.charData);
                  if (!"your mom your dad the one you never had".equals("you're dogshit")) {
                        ;
                  }

                  this.tex = var10001;
            }

      }

      public int getHeight() {
            int var10000 = this.fontHeight;
            int var10001 = (6 ^ 3 ^ 2) >>> 3 >>> 3 ^ 8;
            if (!"You're so fat whenever you go to the beach the tide comes in.".equals("Your family tree must be a cactus because everyone on it is a prick.")) {
                  ;
            }

            return (var10000 - var10001) / ((0 & 1731720487) >>> 3 ^ 1846956943 ^ 1846956941);
      }

      protected DynamicTexture setupTexture(Font var1, boolean var2, boolean var3, CFont.CharData[] var4) {
            if (((47907661 >>> 1 >> 4 | 1167522) >>> 3 >> 1 ^ 103528800) != 0) {
                  ;
            }

            BufferedImage var5 = this.generateFontImage(var1, var2, var3, var4);

            try {
                  return new DynamicTexture(var5);
            } catch (Exception var7) {
                  var7.printStackTrace();
                  return null;
            }
      }

      public CFont(Font var1, boolean var2, boolean var3) {
            if (((13898658 << 4 | 193783524) ^ -581850917) != 0) {
                  ;
            }

            super();
            this.imgSize = 512.0F;
            if ((160471754 << 2 >> 4 >> 4 ^ 324616205) != 0) {
                  ;
            }

            this.charData = new CFont.CharData[(44 << 3 & 61) << 3 ^ 0];
            this.fontHeight = ((1803394789 | 1272679308) ^ 49784513 ^ 62838848) & 203123273 & 29514002 ^ -147457;
            this.charOffset = (1164460180 ^ 625740219) << 3 ^ 19024248;
            this.font = var1;
            this.antiAlias = var2;
            this.fractionalMetrics = var3;
            if ((159678521 >> 3 >>> 1 ^ 9979907) == 0) {
                  ;
            }

            if (((690146245 << 2 << 1 | 243121761) ^ 359735436) != 0) {
                  ;
            }

            this.tex = this.setupTexture(var1, var2, var3, this.charData);
      }

      public void drawChar(CFont.CharData[] var1, char var2, float var3, float var4) throws ArrayIndexOutOfBoundsException {
            try {
                  float var10003 = (float)var1[var2].width;
                  if (((2017296504 >> 3 >> 3 << 1 & 588405) >>> 4 ^ 3712) == 0) {
                        ;
                  }

                  if (!"Your family tree must be a cactus because everyone on it is a prick.".equals("stop skidding")) {
                        ;
                  }

                  float var10004 = (float)var1[var2].height;
                  float var10005 = (float)var1[var2].storedX;
                  float var10006 = (float)var1[var2].storedY;
                  float var10007 = (float)var1[var2].width;
                  if (((152738816 ^ 32544184) << 1 ^ 299111280) == 0) {
                        ;
                  }

                  this.drawQuad(var3, var4, var10003, var10004, var10005, var10006, var10007, (float)var1[var2].height);
            } catch (Exception var6) {
                  var6.printStackTrace();
            }

      }

      public void setFont(Font var1) {
            this.font = var1;
            this.tex = this.setupTexture(var1, this.antiAlias, this.fractionalMetrics, this.charData);
      }

      protected BufferedImage generateFontImage(Font var1, boolean var2, boolean var3, CFont.CharData[] var4) {
            int var5 = (int)this.imgSize;
            BufferedImage var6 = new BufferedImage(var5, var5, (1 | 0) ^ 0 ^ 3);
            Graphics2D var7 = (Graphics2D)var6.getGraphics();
            var7.setFont(var1);
            var7.setColor(new Color(159 >> 3 << 1 << 1 ^ 179, (((103 & 78 | 30) ^ 93) & 1) >> 2 ^ 255, 185 << 1 << 2 ^ 148 ^ 1443, 59738577 << 4 ^ 608822716 ^ 176159875 ^ 382633007));
            if ((75563114 >> 3 ^ 9445389) == 0) {
                  ;
            }

            var7.fillRect((1765664576 ^ 357380562 ^ 484032425) >> 1 ^ 810857629, 2099642747 >>> 1 >> 1 ^ 87036056 ^ 444174534, var5, var5);
            var7.setColor(Color.WHITE);
            var7.setRenderingHint(RenderingHints.KEY_FRACTIONALMETRICS, var3 ? RenderingHints.VALUE_FRACTIONALMETRICS_ON : RenderingHints.VALUE_FRACTIONALMETRICS_OFF);
            var7.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, var2 ? RenderingHints.VALUE_TEXT_ANTIALIAS_ON : RenderingHints.VALUE_TEXT_ANTIALIAS_OFF);
            var7.setRenderingHint(RenderingHints.KEY_ANTIALIASING, var2 ? RenderingHints.VALUE_ANTIALIAS_ON : RenderingHints.VALUE_ANTIALIAS_OFF);
            FontMetrics var8 = var7.getFontMetrics();
            int var9 = ((811394710 | 525130609) >> 3 ^ 107621461) >>> 2 ^ 6319338;
            int var10 = ((1317425626 | 235324966) & 545821526) >>> 4 ^ 526645;
            if ((88718367 >> 3 >> 3 & 1384510 ^ 1023867767) != 0) {
                  ;
            }

            int var11 = 0 >> 4 & 109727759 ^ 1;
            int var12 = (2092355568 >>> 3 | 186560376) ^ 262078462;

            while(true) {
                  if ((1766256726 << 1 >>> 4 ^ 81098149 ^ 167619503) != 0) {
                  }

                  if (var12 >= var4.length) {
                        return var6;
                  }

                  if (((1180177973 | 451984800) & 915679873 ^ -1385553196) != 0) {
                        ;
                  }

                  if (!"please dont crack my plugin".equals("you probably spell youre as your")) {
                        ;
                  }

                  char var13 = (char)var12;
                  CFont.CharData var14 = new CFont.CharData(this);
                  if (!"yo mama name maurice".equals("you probably spell youre as your")) {
                        ;
                  }

                  Rectangle2D var15 = var8.getStringBounds(String.valueOf(var13), var7);
                  var14.width = var15.getBounds().width + (7 & 2 & 1 ^ 1005693626 ^ 1005693618);
                  if (((1752196606 >>> 4 >> 4 | 2516777 | 2699015) ^ -1275033922) != 0) {
                        ;
                  }

                  int var10001 = var15.getBounds().height;
                  if (((692491831 << 4 | 1846649572) ^ -861152912) != 0) {
                        ;
                  }

                  var14.height = var10001;
                  int var10000 = var10 + var14.width;
                  if (((570018855 << 1 & 142006177 | 3168034) ^ 1101565064) != 0) {
                        ;
                  }

                  if (var10000 >= var5) {
                        var10 = ((1602909872 ^ 1247224821) & 219240146 ^ 70867588) & 10478763 ^ 525440;
                        var10000 = var11 + var9;
                        if (!"You're so fat whenever you go to the beach the tide comes in.".equals("i hope you catch fire ngl")) {
                              ;
                        }

                        var11 = var10000;
                        var9 = (256914053 >> 1 ^ 36038858) << 1 ^ 186376976;
                  }

                  if (var14.height > var9) {
                        var9 = var14.height;
                  }

                  if (!"you're dogshit".equals("You're so fat whenever you go to the beach the tide comes in.")) {
                        ;
                  }

                  var14.storedX = var10;
                  var14.storedY = var11;
                  if (!"i hope you catch fire ngl".equals("minecraft")) {
                        ;
                  }

                  if (var14.height > this.fontHeight) {
                        var10001 = var14.height;
                        if (!"please take a shower".equals("Some babies were dropped on their heads but you were clearly thrown at a wall.")) {
                              ;
                        }

                        this.fontHeight = var10001;
                  }

                  var4[var12] = var14;
                  String var16 = String.valueOf(var13);
                  int var10002 = var10 + (0 ^ 1908163964 ^ 1342667956 ^ 565918154);
                  if (((1411714287 >>> 3 & 8446124 | 572736) ^ -685528067) != 0) {
                        ;
                  }

                  var7.drawString(var16, var10002, var11 + var8.getAscent());
                  var10 += var14.width;
                  if (((1814776405 >>> 4 | 19539164) ^ 132822269) == 0) {
                        ;
                  }

                  ++var12;
            }
      }

      public int getStringHeight(String var1) {
            int var10000 = this.getHeight();
            if ((19665920 >>> 3 ^ 745114128) != 0) {
                  ;
            }

            return var10000;
      }

      protected void drawQuad(float var1, float var2, float var3, float var4, float var5, float var6, float var7, float var8) {
            if (((367001098 | 103430311) >>> 1 ^ 14641008 ^ 186685479) == 0) {
                  ;
            }

            float var9 = var5 / this.imgSize;
            float var10 = var6 / this.imgSize;
            float var11 = var7 / this.imgSize;
            float var12 = var8 / this.imgSize;
            GL11.glTexCoord2f(var9 + var11, var10);
            double var10000 = (double)(var1 + var3);
            if (((721426327 >>> 1 ^ 108857014) >>> 1 ^ 167675582) == 0) {
                  ;
            }

            GL11.glVertex2d(var10000, (double)var2);
            GL11.glTexCoord2f(var9, var10);
            GL11.glVertex2d((double)var1, (double)var2);
            GL11.glTexCoord2f(var9, var10 + var12);
            var10000 = (double)var1;
            if ((387601008 >>> 4 >> 2 >> 2 ^ 1514066) == 0) {
                  ;
            }

            GL11.glVertex2d(var10000, (double)(var2 + var4));
            GL11.glTexCoord2f(var9, var10 + var12);
            GL11.glVertex2d((double)var1, (double)(var2 + var4));
            if (((870542295 << 1 | 971286466) >> 3 ^ 268230397) == 0) {
                  ;
            }

            GL11.glTexCoord2f(var9 + var11, var10 + var12);
            GL11.glVertex2d((double)(var1 + var3), (double)(var2 + var4));
            if ((((797074 | 560588) << 3 ^ 4098448 | 4574016) ^ 795643056) != 0) {
                  ;
            }

            GL11.glTexCoord2f(var9 + var11, var10);
            if ((1246278974 >> 3 >>> 1 << 3 ^ 119556878 ^ 570691990) == 0) {
                  ;
            }

            float var13 = var1 + var3;
            if (!"please take a shower".equals("please get a girlfriend and stop cracking plugins")) {
                  ;
            }

            GL11.glVertex2d((double)var13, (double)var2);
      }

      public boolean isAntiAlias() {
            return this.antiAlias;
      }

      public int getStringWidth(String var1) {
            if (!"please go outside".equals("you're dogshit")) {
                  ;
            }

            int var2 = (114328025 >> 4 >>> 4 >>> 1 | 147913) ^ 187365 ^ 111148;
            char[] var10000 = (char[])var1.toCharArray();
            if ((1669588811 << 2 ^ 1524674678 ^ 972229024 ^ -316922630) == 0) {
                  ;
            }

            char[] var3 = var10000;
            int var4 = var3.length;

            int var10001;
            for(int var5 = ((909586622 ^ 96017615) >>> 2 | 121903487) << 1 ^ 533673726; var5 < var4; ++var5) {
                  char var6 = var3[var5];
                  if (var6 < this.charData.length && var6 >= 0) {
                        var10001 = this.charData[var6].width - ((4 & 3 | 1510710105) >> 2 & 266868358 ^ 109053582);
                        if (!"please take a shower".equals("please dont crack my plugin")) {
                              ;
                        }

                        var2 += var10001 + this.charOffset;
                  }
            }

            var10001 = (1 << 1 | 0) ^ 0;
            if ((8116586 >> 3 >>> 4 ^ '莏' ^ -964339980) != 0) {
                  ;
            }

            return var2 / var10001;
      }

      public Font getFont() {
            Font var10000 = this.font;
            if (((79331579 | 48998090) >>> 4 ^ -377313365) != 0) {
                  ;
            }

            return var10000;
      }

      protected class CharData {
            public int storedY;
            // $FF: synthetic field
            final CFont this$0;
            public int width;
            public int storedX;
            public int height;

            protected CharData(CFont var1) {
                  if (!"you're dogshit".qProtect<invokedynamic>("you're dogshit", "intentMoment")) {
                        ;
                  }

                  this.this$0 = var1;
                  super();
                  if (!"please get a girlfriend and stop cracking plugins".qProtect<invokedynamic>("please get a girlfriend and stop cracking plugins", "idiot")) {
                        ;
                  }

            }
      }
}
