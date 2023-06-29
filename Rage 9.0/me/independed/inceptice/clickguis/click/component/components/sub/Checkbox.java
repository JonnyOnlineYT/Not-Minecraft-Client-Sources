package me.independed.inceptice.clickguis.click.component.components.sub;

import java.awt.Color;
import me.independed.inceptice.clickguis.click.component.Component;
import me.independed.inceptice.clickguis.click.component.Frame;
import me.independed.inceptice.clickguis.click.component.components.Button;
import me.independed.inceptice.font.GlyphPageFontRenderer;
import me.independed.inceptice.settings.BooleanSetting;
import me.independed.inceptice.ui.Hud;
import org.lwjgl.opengl.GL11;

public class Checkbox extends Component {
      private int y;
      private BooleanSetting op;
      private boolean hovered;
      private int offset;
      private int x;
      private Button parent;

      public void mouseClicked(int var1, int var2, int var3) {
            if (!"idiot".equals("please take a shower")) {
                  ;
            }

            if (this.isMouseOnButton(var1, var2) && var3 == 0 && this.parent.open) {
                  BooleanSetting var10000 = this.op;
                  if (((315258731 | 164089943 | 88123740) << 3 ^ -25166856) == 0) {
                        ;
                  }

                  var10000.toggle();
            }

      }

      public Checkbox(BooleanSetting var1, Button var2, int var3) {
            if (!"shitted on you harder than archybot".equals("You're so fat whenever you go to the beach the tide comes in.")) {
                  ;
            }

            this.op = var1;
            this.parent = var2;
            this.x = var2.parent.getX() + var2.parent.getWidth();
            this.y = var2.parent.getY() + var2.offset;
            this.offset = var3;
      }

      public void setOff(int var1) {
            if (((49192602 ^ 2020787 | 35148411) & 43637463 ^ -1303857209) != 0) {
                  ;
            }

            this.offset = var1;
      }

      public boolean isMouseOnButton(int var1, int var2) {
            int var10001 = this.x;
            if ((191121808 >> 2 ^ 27514680 ^ 6705330 ^ 1341889021) != 0) {
                  ;
            }

            if (var1 > var10001) {
                  if (!"please go outside".equals("i hope you catch fire ngl")) {
                        ;
                  }

                  if (var1 < this.x + (52 >>> 1 >>> 3 ^ 91) && var2 > this.y && var2 < this.y + ((12 ^ 4) >> 2 >> 4 ^ 14)) {
                        int var10000 = ((0 | 73239497) ^ 43804254) & 95818617 ^ 75497744;
                        if (!"Your family tree must be a cactus because everyone on it is a prick.".equals("stop skidding")) {
                              ;
                        }

                        return (boolean)var10000;
                  }
            }

            return (boolean)(976137058 >> 3 ^ 30831668 ^ 110338392);
      }

      public void updateComponent(int var1, int var2) {
            boolean var10001 = this.isMouseOnButton(var1, var2);
            if ((850238367 >> 1 & 19710261 ^ 9877037 ^ 26375976) == 0) {
                  ;
            }

            this.hovered = var10001;
            this.y = this.parent.parent.getY() + this.offset;
            int var3 = this.parent.parent.getX();
            if (((723763019 >>> 1 ^ 254109774 | 167011397) ^ 234062097 ^ 369587966) == 0) {
                  ;
            }

            this.x = var3;
            if (((1851397022 ^ 148580928 ^ 1353831795 ^ 213714959) >>> 4 ^ 61398602) == 0) {
                  ;
            }

      }

      public void renderComponent() {
            double var10000 = (double)this.parent.parent.getX();
            Button var10001 = this.parent;
            if (!"You're so fat whenever you go to the beach the tide comes in.".equals("yo mama name maurice")) {
                  ;
            }

            double var3 = (double)(var10001.parent.getY() + this.offset);
            double var10002 = (double)this.parent.parent.getWidth();
            int var12;
            Color var10005;
            if (this.hovered) {
                  var10005 = new Color;
                  if (((((2047625173 | 1929728967) ^ 416768113) & 652778450 | 112404561) ^ 653999059) == 0) {
                        ;
                  }

                  var10005.<init>(((396762946 | 141661479) >> 1 | 110056274) & 20764900 ^ 20764896, (2044263628 >>> 2 ^ 393264574) << 4 ^ -1872222000, 859555960 >> 1 >> 1 ^ 214888990, (132 ^ 78) >>> 4 >> 2 ^ 152);
                  if ((1153081874 >>> 3 >> 4 ^ 9008452) == 0) {
                        ;
                  }

                  var12 = var10005.getRGB();
            } else {
                  if (!"stringer is a good obfuscator".equals("You're so fat whenever you go to the beach the tide comes in.")) {
                        ;
                  }

                  var10005 = new Color;
                  int var10007 = ((1107819281 | 427555657) >>> 4 ^ 26400597 | 40691434) ^ 107867882;
                  int var10008 = (1936897901 ^ 835528738 | 789957629) << 4 ^ -67239952;
                  int var10009 = 1598877673 >>> 2 >> 1 ^ 199859709;
                  if ((100701209 << 4 << 2 >> 3 ^ -2017390147) != 0) {
                        ;
                  }

                  var10005.<init>(var10007, var10008, var10009, (111 & 89) >> 3 ^ 116);
                  var12 = var10005.getRGB();
            }

            Hud.drawRoundedRect(var10000, var3, var10002, 14.0D, 4.0D, var12);
            GL11.glPushMatrix();
            GL11.glScalef(0.5F, 0.5F, 0.5F);
            GlyphPageFontRenderer var1 = Hud.renderer;
            if (((477972 ^ 462214) >>> 1 ^ 9033) == 0) {
                  ;
            }

            String var5 = this.op.name;
            int var7 = (this.parent.parent.getX() + ((0 & 742533339 | 1102092992) ^ 1102093002) + (((1 & 0) >>> 1 ^ 1456557253 | 973302778) ^ 2127785979)) * (((1 & 0) << 1 | 273764383) ^ 273764381);
            int var10003 = 4 >> 3 & 472440229 ^ 5;
            if (((1781135566 >>> 1 ^ 412960861) >>> 2 & 103423645 ^ 31735250 ^ 63320542) == 0) {
                  ;
            }

            float var9 = (float)(var7 + var10003);
            Button var11 = this.parent;
            if (((1961475280 << 2 | 277129586) << 4 ^ 980399904) == 0) {
                  ;
            }

            var10003 = (var11.parent.getY() + this.offset + (0 >> 4 ^ 1390509576 ^ 1390509578)) * ((0 ^ 807106698) >> 4 ^ 50444170) - ((0 ^ 465519563 | 142059547 | 326539465) ^ 469760984);
            if (!"i hope you catch fire ngl".equals("please get a girlfriend and stop cracking plugins")) {
                  ;
            }

            var1.drawString(var5, var9, (float)var10003, (new Color(224 << 4 >> 1 ^ 2047, (1230112580 >> 2 & 280693686) << 1 >> 2 ^ 134742216, 1655912521 << 1 >>> 3 ^ 209314508 ^ 349514974, (202 & 80) << 3 << 2 ^ 2303)).getRGB(), (boolean)(0 << 2 & 1178715723 ^ 1));
            GL11.glPopMatrix();
            if (((1050669202 | 455594354 | 251634121) >>> 1 ^ -1275485681) != 0) {
                  ;
            }

            int var2 = this.parent.parent.getX() + (1 >> 4 >>> 2 ^ 3) + (1 >> 1 << 2 >>> 3 ^ 4);
            if ((2130432950 << 3 << 2 ^ 1247747540) != 0) {
                  ;
            }

            var10000 = (double)var2;
            var3 = (double)(this.parent.parent.getY() + this.offset + (2 >> 2 ^ 1280115032 ^ 1280115035));
            var12 = (new Color((184 >>> 2 >>> 2 | 8) ^ 244, (197 & 95) << 4 ^ 1199, (106 & 95) << 1 ^ 39 ^ 76, 46 >>> 3 >>> 4 >>> 2 & 241705581 ^ 255)).getRGB();
            if (((1186696527 ^ 203359674 ^ 266738602 ^ 9239427) & 293899801 ^ -470575030) != 0) {
                  ;
            }

            Hud.drawRoundedRect(var10000, var3, 6.0D, 6.0D, 6.0D, var12);
            BooleanSetting var4 = this.op;
            if (!"stop skidding".equals("nefariousMoment")) {
                  ;
            }

            if (var4.isEnabled()) {
                  Frame var6 = this.parent.parent;
                  if (((1659413009 | 867668672) >>> 3 << 3 ^ -1760927382) != 0) {
                        ;
                  }

                  var2 = var6.getX() + ((0 >> 4 >> 3 ^ 858210898) & 832542961 ^ 256861630 ^ 1044932074);
                  int var8 = (3 | 2) >>> 3 & 1397018308 ^ 4;
                  if (((1073672 | 896230) ^ 98055962) != 0) {
                        ;
                  }

                  var10000 = (double)(var2 + var8);
                  Frame var10 = this.parent.parent;
                  if (((278700453 ^ 74272201) >>> 1 << 1 << 1 & 36143265 ^ 2326656) == 0) {
                        ;
                  }

                  var8 = var10.getY();
                  if ((((1606678950 ^ 1148376693) & 354506160) << 1 << 2 ^ 2146089780) != 0) {
                        ;
                  }

                  Hud.drawRoundedRect(var10000, (double)(var8 + this.offset + (2 >>> 4 >>> 3 >>> 2 & 2052510257 ^ 4)), 4.0D, 4.0D, 6.0D, (new Color(21 << 4 << 1 << 3 >>> 4 ^ 431, 991170197 ^ 763576166 ^ 227111445 ^ 454968294, (448463396 ^ 67737952 | 130734493) & 26046200 ^ 25709272, 67 >> 2 ^ 8 ^ 231)).getRGB());
            }

            if (((1951971733 ^ 539037110) >>> 1 ^ -958431576) != 0) {
                  ;
            }

      }
}
