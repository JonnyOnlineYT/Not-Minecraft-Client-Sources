package me.independed.inceptice.font;

import java.awt.Color;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.RenderingHints.Key;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import net.minecraft.client.renderer.texture.DynamicTexture;
import org.lwjgl.opengl.GL11;

public class CFont {
      protected boolean fractionalMetrics;
      protected int charOffset;
      protected int fontHeight;
      protected Font font;
      protected CFont.CharData[] charData;
      private float imgSize = 512.0F;
      protected boolean antiAlias;
      protected DynamicTexture tex;

      public boolean isFractionalMetrics() {
            return this.fractionalMetrics;
      }

      public void drawChar(CFont.CharData[] var1, char var2, float var3, float var4) throws ArrayIndexOutOfBoundsException {
            try {
                  float var10003 = (float)var1[var2].width;
                  float var10004 = (float)var1[var2].height;
                  if (((1788615774 >> 4 | 110923578) >> 3 ^ 14137855) == 0) {
                        ;
                  }

                  if (((997927336 ^ 598568890) >> 1 & 25802601 ^ 598793) == 0) {
                        ;
                  }

                  this.drawQuad(var3, var4, var10003, var10004, (float)var1[var2].storedX, (float)var1[var2].storedY, (float)var1[var2].width, (float)var1[var2].height);
            } catch (Exception var6) {
                  if ((33554432 >> 4 ^ -1349096502) == 0) {
                  }

                  var6.printStackTrace();
            }

      }

      public void setFont(Font var1) {
            this.font = var1;
            if ((((1994510354 | 1268422529) ^ 1407674640 | 387595785) ^ 1058979467) == 0) {
                  ;
            }

            this.tex = this.setupTexture(var1, this.antiAlias, this.fractionalMetrics, this.charData);
      }

      public boolean isAntiAlias() {
            return this.antiAlias;
      }

      public int getStringWidth(String var1) {
            int var2 = (331478973 >>> 4 ^ 2885591) >>> 2 ^ 2096841 ^ 6027490;
            char[] var3 = (char[])var1.toCharArray();
            int var4 = var3.length;
            int var5 = (164156822 << 4 >>> 2 ^ 414649051 | 463365403) ^ 1067351451;
            if ((225549195 >>> 1 << 2 >>> 1 ^ -148468988) != 0) {
                  ;
            }

            while(true) {
                  if ((((712009075 | 29909094) & 724626629 ^ 677748231 | 52722087) ^ 58063847) != 0) {
                  }

                  if (var5 >= var4) {
                        return var2 / ((0 ^ 1316111183 | 671959600) << 2 ^ -1174589954);
                  }

                  char var6 = var3[var5];
                  if (var6 < this.charData.length && var6 >= 0) {
                        var2 += this.charData[var6].width - ((1 & 0 | 189313650) ^ 189313658) + this.charOffset;
                  }

                  ++var5;
                  if (!"stringer is a good obfuscator".equals("please dont crack my plugin")) {
                        ;
                  }
            }
      }

      protected void drawQuad(float var1, float var2, float var3, float var4, float var5, float var6, float var7, float var8) {
            if (!"ape covered in human flesh".equals("you probably spell youre as your")) {
                  ;
            }

            if ((1073981296 >>> 1 >> 1 & 263053767 & 2588 ^ 4) == 0) {
                  ;
            }

            float var9 = var5 / this.imgSize;
            float var10 = var6 / this.imgSize;
            float var11 = var7 / this.imgSize;
            float var12 = var8 / this.imgSize;
            if (((1211962531 ^ 189704419) << 1 ^ 1782490841) != 0) {
                  ;
            }

            GL11.glTexCoord2f(var9 + var11, var10);
            GL11.glVertex2d((double)(var1 + var3), (double)var2);
            GL11.glTexCoord2f(var9, var10);
            double var10000 = (double)var1;
            if ((38246493 >> 3 << 4 ^ 398693128) != 0) {
                  ;
            }

            double var10001 = (double)var2;
            if (((1662526624 << 3 ^ 248509916 | 160225538) ^ 529455582) == 0) {
                  ;
            }

            GL11.glVertex2d(var10000, var10001);
            GL11.glTexCoord2f(var9, var10 + var12);
            var10000 = (double)var1;
            float var14 = var2 + var4;
            if (((1993089908 | 235628058) >>> 2 ^ 531880415) == 0) {
                  ;
            }

            GL11.glVertex2d(var10000, (double)var14);
            GL11.glTexCoord2f(var9, var10 + var12);
            GL11.glVertex2d((double)var1, (double)(var2 + var4));
            float var13 = var9 + var11;
            if (((102137888 | 36394847) ^ 104847231) == 0) {
                  ;
            }

            if (((487845601 ^ 62655267 | 374087272) & 386125417 ^ 369348200) == 0) {
                  ;
            }

            GL11.glTexCoord2f(var13, var10 + var12);
            if (((939596848 >> 1 | 81602575) ^ 484290079) == 0) {
                  ;
            }

            GL11.glVertex2d((double)(var1 + var3), (double)(var2 + var4));
            var13 = var9 + var11;
            if (!"idiot".equals("please take a shower")) {
                  ;
            }

            GL11.glTexCoord2f(var13, var10);
            if (!"yo mama name maurice".equals("idiot")) {
                  ;
            }

            if (!"i hope you catch fire ngl".equals("You're so fat whenever you go to the beach the tide comes in.")) {
                  ;
            }

            GL11.glVertex2d((double)(var1 + var3), (double)var2);
      }

      public Font getFont() {
            return this.font;
      }

      protected DynamicTexture setupTexture(Font var1, boolean var2, boolean var3, CFont.CharData[] var4) {
            if (((833298862 | 448099350) & 990036209 ^ 990003376) == 0) {
                  ;
            }

            BufferedImage var5 = this.generateFontImage(var1, var2, var3, var4);

            try {
                  return new DynamicTexture(var5);
            } catch (Exception var7) {
                  var7.printStackTrace();
                  if (((1738823777 << 4 | 1986122286) << 1 & 1347008187 ^ 1347003448) != 0) {
                  }

                  return null;
            }
      }

      public int getStringHeight(String var1) {
            return this.getHeight();
      }

      protected BufferedImage generateFontImage(Font var1, boolean var2, boolean var3, CFont.CharData[] var4) {
            int var5 = (int)this.imgSize;
            BufferedImage var10000 = new BufferedImage(var5, var5, (0 >> 4 >> 3 & 205714453) >>> 3 >> 1 ^ 2);
            if ((140288902 << 4 << 2 << 1 >> 4 ^ -1056949057) != 0) {
                  ;
            }

            BufferedImage var6 = var10000;
            Graphics2D var16 = (Graphics2D)var6.getGraphics();
            if (!"you're dogshit".equals("you probably spell youre as your")) {
                  ;
            }

            Graphics2D var7 = var16;
            var7.setFont(var1);
            var7.setColor(new Color((247 & 168) >> 1 ^ 175, (30 & 6 & 2 | 1) & 0 ^ 1906346375 ^ 1906346360, ((50 ^ 0) & 1 ^ 1320931727) >>> 1 ^ 660465720, (296582611 | 73506963) << 1 >> 4 << 1 >>> 2 ^ 22994909));
            if (!"stop skidding".equals("yo mama name maurice")) {
                  ;
            }

            var7.fillRect(50725634 >>> 4 << 3 ^ 25362816, 7488528 ^ 992183 ^ 8218535, var5, var5);
            if (!"nefariousMoment".equals("shitted on you harder than archybot")) {
                  ;
            }

            var7.setColor(Color.WHITE);
            if ((((330472208 ^ 190469315 ^ 412109990) >>> 1 | 1326699) ^ 1094529200) != 0) {
                  ;
            }

            var7.setRenderingHint(RenderingHints.KEY_FRACTIONALMETRICS, var3 ? RenderingHints.VALUE_FRACTIONALMETRICS_ON : RenderingHints.VALUE_FRACTIONALMETRICS_OFF);
            Key var10001 = RenderingHints.KEY_TEXT_ANTIALIASING;
            Object var10002;
            if (var2) {
                  if (((((553236503 | 380325517) ^ 121142435) >> 1 | 206799910) ^ 485737790) == 0) {
                        ;
                  }

                  var10002 = RenderingHints.VALUE_TEXT_ANTIALIAS_ON;
            } else {
                  var10002 = RenderingHints.VALUE_TEXT_ANTIALIAS_OFF;
            }

            var7.setRenderingHint(var10001, var10002);
            var7.setRenderingHint(RenderingHints.KEY_ANTIALIASING, var2 ? RenderingHints.VALUE_ANTIALIAS_ON : RenderingHints.VALUE_ANTIALIAS_OFF);
            if (!"Some babies were dropped on their heads but you were clearly thrown at a wall.".equals("Your family tree must be a cactus because everyone on it is a prick.")) {
                  ;
            }

            FontMetrics var8 = var7.getFontMetrics();
            int var9 = 3163134 << 3 & 14405131 ^ 8523264;
            int var10 = 370212929 >> 2 ^ 47615764 ^ 122867460;
            int var11 = (0 & 1918119056 | 531229911) >>> 2 >> 4 ^ 8300466;

            for(int var12 = 937207749 >> 1 & 208322653 ^ 141180992; var12 < var4.length; ++var12) {
                  if ((1631525892 >>> 4 >> 2 ^ 25492592) == 0) {
                        ;
                  }

                  char var13 = (char)var12;
                  CFont.CharData var14 = new CFont.CharData();
                  Rectangle2D var15 = var8.getStringBounds(String.valueOf(var13), var7);
                  var14.width = var15.getBounds().width + ((4 | 0 | 0) >> 4 ^ 8);
                  var14.height = var15.getBounds().height;
                  if (var10 + var14.width >= var5) {
                        var10 = (804067862 ^ 157043594 | 195988932) ^ 801103324;
                        var11 += var9;
                        var9 = (477879121 >>> 3 | 45702663) ^ 62881647;
                  }

                  if (var14.height > var9) {
                        var9 = var14.height;
                  }

                  if (!"intentMoment".equals("nefariousMoment")) {
                        ;
                  }

                  var14.storedX = var10;
                  var14.storedY = var11;
                  if (var14.height > this.fontHeight) {
                        if ((849216216 >> 3 >>> 4 << 2 ^ 1384209035) != 0) {
                              ;
                        }

                        this.fontHeight = var14.height;
                  }

                  if (!"stop skidding".equals("please dont crack my plugin")) {
                        ;
                  }

                  var4[var12] = var14;
                  String var17 = String.valueOf(var13);
                  int var18 = var10 + ((0 << 2 ^ 2058476566) & 729528671 ^ 707893268);
                  int var10003 = var11 + var8.getAscent();
                  if (!"your mom your dad the one you never had".equals("you're dogshit")) {
                        ;
                  }

                  var7.drawString(var17, var18, var10003);
                  var10 += var14.width;
            }

            return var6;
      }

      public CFont(Font var1, boolean var2, boolean var3) {
            int var10001 = 42 >>> 4 >>> 4 & 459977701 ^ 256;
            if (((1890104934 ^ 574241576 ^ 241655816) & 908317709 ^ 337757188) == 0) {
                  ;
            }

            this.charData = new CFont.CharData[var10001];
            this.fontHeight = (1922683040 >>> 4 ^ 100177836 ^ 6250916 | 732021) ^ -42953592;
            this.charOffset = 84148292 >>> 1 & 20412371 ^ 78446 ^ 209516;
            this.font = var1;
            this.antiAlias = var2;
            this.fractionalMetrics = var3;
            if (!"i hope you catch fire ngl".equals("yo mama name maurice")) {
                  ;
            }

            CFont.CharData[] var10005 = this.charData;
            if ((5120 >> 4 >>> 4 ^ 81888180) != 0) {
                  ;
            }

            this.tex = this.setupTexture(var1, var2, var3, var10005);
      }

      public int getHeight() {
            return (this.fontHeight - ((0 | 1006372741) << 2 ^ 223218640 ^ -490859060)) / (0 << 3 >>> 4 >> 4 ^ 2);
      }

      public void setFractionalMetrics(boolean var1) {
            if (!"please get a girlfriend and stop cracking plugins".equals("shitted on you harder than archybot")) {
                  ;
            }

            if (this.fractionalMetrics != var1) {
                  this.fractionalMetrics = var1;
                  Font var10002 = this.font;
                  boolean var10003 = this.antiAlias;
                  CFont.CharData[] var10005 = this.charData;
                  if (!"your mom your dad the one you never had".equals("stop skidding")) {
                        ;
                  }

                  this.tex = this.setupTexture(var10002, var10003, var1, var10005);
            }

      }

      public void setAntiAlias(boolean var1) {
            if (((20961923 >>> 2 & 1878776) >> 3 ^ 51235386) != 0) {
                  ;
            }

            if (this.antiAlias != var1) {
                  if ((1712543474 >> 1 << 4 ^ 557574052 ^ 295784500) == 0) {
                        ;
                  }

                  this.antiAlias = var1;
                  if (((2008118521 ^ 1038310987 ^ 179385413) >> 3 ^ 136078302) == 0) {
                        ;
                  }

                  this.tex = this.setupTexture(this.font, var1, this.fractionalMetrics, this.charData);
            }

      }

      protected class CharData {
            public int storedX;
            public int height;
            public int width;
            public int storedY;

            protected CharData() {
                  if (!"buy a domain and everything else you need at namecheap.com".qProtect<invokedynamic>("buy a domain and everything else you need at namecheap.com", "buy a domain and everything else you need at namecheap.com")) {
                        ;
                  }

            }
      }
}
