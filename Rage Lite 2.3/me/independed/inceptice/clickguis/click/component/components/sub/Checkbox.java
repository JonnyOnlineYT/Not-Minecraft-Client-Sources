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
      private boolean hovered;
      private BooleanSetting op;
      private int offset;
      private Button parent;
      private int y;
      private int x;

      public boolean isMouseOnButton(int var1, int var2) {
            return (boolean)(var1 > this.x && var1 < this.x + ((10 << 4 | 90 | 87) >>> 3 ^ 71) && var2 > this.y && var2 < this.y + ((2 ^ 0) >>> 2 ^ 14) ? 0 >> 4 >>> 2 ^ 1 : (1380057111 >>> 2 | 86067589) ^ 363972997);
      }

      public void setOff(int var1) {
            if (!"yo mama name maurice".equals("i hope you catch fire ngl")) {
                  ;
            }

            this.offset = var1;
      }

      public void renderComponent() {
            if (!"your mom your dad the one you never had".equals("please go outside")) {
                  ;
            }

            double var10000 = (double)this.parent.parent.getX();
            Frame var10001 = this.parent.parent;
            if (!"minecraft".equals("yo mama name maurice")) {
                  ;
            }

            double var3 = (double)(var10001.getY() + this.offset);
            double var10002 = (double)this.parent.parent.getWidth();
            Color var7;
            int var10005;
            if (this.hovered) {
                  var10005 = (new Color(1991239026 << 1 << 4 >>> 4 ^ 224381668, (230225569 << 1 | 202175682) ^ 519532991 ^ 25857149, 450035902 << 3 >> 3 << 3 ^ -694680080, (83 | 65) >> 1 ^ 178)).getRGB();
            } else {
                  var7 = new Color;
                  int var10007 = 1382515690 ^ 1008549036 ^ 1421580436 ^ 985737170;
                  int var10008 = 896971839 >> 4 & 6312916 ^ 4211520;
                  int var10009 = 1107214176 << 2 >> 2 << 3 ^ 267778816;
                  int var10010 = 69 & 56 & 732741406 ^ 125;
                  if ((272666752 ^ -1812223014) != 0) {
                        ;
                  }

                  var7.<init>(var10007, var10008, var10009, var10010);
                  var10005 = var7.getRGB();
            }

            if (!"minecraft".equals("nefariousMoment")) {
                  ;
            }

            Hud.drawRoundedRect(var10000, var3, var10002, 14.0D, 4.0D, var10005);
            GL11.glPushMatrix();
            if (((131947585 << 1 | 159262937) ^ 268351707) == 0) {
                  ;
            }

            GL11.glScalef(0.5F, 0.5F, 0.5F);
            GlyphPageFontRenderer var1 = Hud.renderer;
            String var4 = this.op.name;
            Button var5 = this.parent;
            if (((809047558 >> 1 << 3 ^ 1389473608) >>> 2 ^ -697927370) != 0) {
                  ;
            }

            var1.drawString(var4, (float)((var5.parent.getX() + (4 >>> 2 >>> 3 ^ 555430951 ^ 555430957) + ((1 << 1 & 0) >>> 4 ^ 4)) * ((1 << 3 & 4 | 1476921873) ^ 1476921875) + (0 & 1048438682 & 1064465402 ^ 5)), (float)((this.parent.parent.getY() + this.offset + ((0 & 750038825) >> 1 ^ 2)) * ((0 & 1984646417 & 24781108) >>> 3 >> 1 ^ 2) - ((0 >> 4 >> 4 >>> 4 | 1029643765) ^ 1029643766)), (new Color(109 >>> 4 & 4 ^ 251, ((1405727280 | 930825174) ^ 1348176856) << 2 ^ -1635655496, ((274793220 ^ 266639259) >>> 1 | 193653796) << 1 ^ 529915102, (212 >>> 1 >> 3 << 2 | 7) ^ 200)).getRGB(), (boolean)((0 >> 2 | 385238944) ^ 385238945));
            GL11.glPopMatrix();
            var10000 = (double)(this.parent.parent.getX() + ((1 & 0) << 1 ^ 3) + (1 >> 2 >>> 3 << 4 ^ 4));
            var3 = (double)(this.parent.parent.getY() + this.offset + ((0 | 174291839) << 2 & 229341611 ^ 160000427));
            if ((502143784 >> 1 >>> 3 << 2 << 2 ^ 28780364 ^ -314429778) != 0) {
                  ;
            }

            Hud.drawRoundedRect(var10000, var3, 6.0D, 6.0D, 6.0D, (new Color(228 << 2 ^ 146 ^ 1021, (19 ^ 4) >> 4 >>> 2 ^ 255, (159 ^ 109) << 2 ^ 383 ^ 584, (84 & 58 | 9) & 17 ^ 238)).getRGB());
            if (this.op.isEnabled()) {
                  if (('\ue008' >>> 4 >> 1 ^ -1953802907) != 0) {
                        ;
                  }

                  int var2 = this.parent.parent.getX();
                  if (!"You're so fat whenever you go to the beach the tide comes in.".equals("idiot")) {
                        ;
                  }

                  var2 += (2 & 0) >>> 3 >>> 4 ^ 4;
                  int var6 = (0 & 2021298049 | 1380712778 | 1303714790) ^ 1610423274;
                  if ((728566802 >> 4 >> 4 & 1807696 ^ 362997 ^ 908927021) != 0) {
                        ;
                  }

                  var10000 = (double)(var2 + var6);
                  if (!"shitted on you harder than archybot".equals("please dont crack my plugin")) {
                        ;
                  }

                  var3 = (double)(this.parent.parent.getY() + this.offset + (1 >> 1 >>> 2 & 276717549 ^ 4));
                  var7 = new Color((169 & 55 | 0) ^ 222, (998461361 << 2 & 2107101906 ^ 1127700762) << 3 ^ 2039635664, (1316761001 | 822885613) << 3 ^ -69079192, (85 << 2 & 304 | 78) & 345 ^ 423);
                  if (((2025686852 | 1687885782) & 958927852 ^ -1187980324) != 0) {
                        ;
                  }

                  Hud.drawRoundedRect(var10000, var3, 4.0D, 4.0D, 6.0D, var7.getRGB());
            }

      }

      public void updateComponent(int var1, int var2) {
            this.hovered = this.isMouseOnButton(var1, var2);
            int var10001 = this.parent.parent.getY();
            if ((((2089220726 ^ 355770306) << 2 << 1 | 49193556) ^ 1342156788) == 0) {
                  ;
            }

            this.y = var10001 + this.offset;
            this.x = this.parent.parent.getX();
      }

      public Checkbox(BooleanSetting var1, Button var2, int var3) {
            if ((792043398 >> 2 << 2 ^ 792043396) == 0) {
                  ;
            }

            super();
            this.op = var1;
            if ((70203225 ^ 62980297 ^ 15443795 ^ 117805251) == 0) {
                  ;
            }

            this.parent = var2;
            this.x = var2.parent.getX() + var2.parent.getWidth();
            this.y = var2.parent.getY() + var2.offset;
            this.offset = var3;
      }

      public void mouseClicked(int var1, int var2, int var3) {
            if (this.isMouseOnButton(var1, var2)) {
                  if (!"Some babies were dropped on their heads but you were clearly thrown at a wall.".equals("shitted on you harder than archybot")) {
                        ;
                  }

                  if (var3 == 0) {
                        if (!"shitted on you harder than archybot".equals("please dont crack my plugin")) {
                              ;
                        }

                        if (this.parent.open) {
                              this.op.toggle();
                        }
                  }
            }

      }
}
