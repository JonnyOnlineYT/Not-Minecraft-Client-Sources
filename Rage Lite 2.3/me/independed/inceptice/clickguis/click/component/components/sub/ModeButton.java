package me.independed.inceptice.clickguis.click.component.components.sub;

import java.awt.Color;
import me.independed.inceptice.clickguis.click.component.Component;
import me.independed.inceptice.clickguis.click.component.Frame;
import me.independed.inceptice.clickguis.click.component.components.Button;
import me.independed.inceptice.font.GlyphPageFontRenderer;
import me.independed.inceptice.modules.Module;
import me.independed.inceptice.settings.ModeSetting;
import me.independed.inceptice.ui.Hud;
import org.lwjgl.opengl.GL11;

public class ModeButton extends Component {
      private int offset;
      private boolean hovered;
      private int x;
      private int y;
      private ModeSetting modeSetting;
      private Button parent;
      private Module mod;

      public boolean isMouseOnButton(int var1, int var2) {
            if ((478715345 >>> 2 & 86829497 ^ 85991728) == 0) {
                  ;
            }

            return (boolean)(var1 > this.x && var1 < this.x + ((30 >>> 2 | 6) >> 4 ^ 88) && var2 > this.y && var2 < this.y + (8 >> 1 & 0 ^ 1035731474 ^ 1035731484) ? 0 >> 2 ^ 1587435621 ^ 1587435620 : ((858072717 ^ 492788583) >>> 3 ^ 26294937) >>> 4 ^ 4581202);
      }

      public void setOff(int var1) {
            this.offset = var1;
            if (((1232273512 ^ 415728549 | 1213646396) << 3 ^ -810139672) == 0) {
                  ;
            }

      }

      public void updateComponent(int var1, int var2) {
            if (((1030754758 ^ 460067989) << 3 ^ 140805691 ^ -2078520276) != 0) {
                  ;
            }

            if (((16909409 << 2 ^ 15007271) << 3 ^ 661093656) == 0) {
                  ;
            }

            this.hovered = this.isMouseOnButton(var1, var2);
            this.y = this.parent.parent.getY() + this.offset;
            this.x = this.parent.parent.getX();
      }

      public void renderComponent() {
            double var10000 = (double)this.parent.parent.getX();
            int var10001 = this.parent.parent.getY();
            int var10002 = this.offset;
            if (!"please get a girlfriend and stop cracking plugins".equals("nefariousMoment")) {
                  ;
            }

            double var2 = (double)(var10001 + var10002);
            double var5 = (double)this.parent.parent.getWidth();
            int var10005;
            if (this.hovered) {
                  var10005 = (new Color((1268111468 | 500150501) << 1 >>> 4 & 138933241 ^ 138670873, (657113630 | 60748166) >>> 1 & 311263420 ^ 311230604, 26286558 >>> 3 ^ 1378367 ^ 2566916, 59 >>> 3 & 3 ^ 152)).getRGB();
            } else {
                  if (((85869799 >> 3 << 3 << 3 ^ 389669474) & 605983609 ^ 604668256) == 0) {
                        ;
                  }

                  var10005 = (new Color((462406667 | 405711416 | 128423236 | 61192698) >> 4 & 31485241 ^ 31485241, 549304654 << 3 >> 1 ^ 49734968, (521042416 | 401305534) ^ 182076524 ^ 355810194, (93 >>> 2 | 10) ^ 98)).getRGB();
            }

            Hud.drawRoundedRect(var10000, var2, var5, 14.0D, 4.0D, var10005);
            GL11.glPushMatrix();
            GL11.glScalef(0.5F, 0.5F, 0.5F);
            if (this.modeSetting != null && this.modeSetting.name != null && this.modeSetting.activeMode != null) {
                  GlyphPageFontRenderer var1 = Hud.renderer;
                  if ((91673519 << 4 << 1 & 428185700 ^ 142610528) == 0) {
                        ;
                  }

                  StringBuilder var3 = (new StringBuilder()).append(this.modeSetting.name);
                  if ((1325050412 >>> 1 >>> 2 ^ 165631301) == 0) {
                        ;
                  }

                  String var4 = var3.append(":").append(this.modeSetting.activeMode).toString();
                  if (!"stop skidding".equals("shitted on you harder than archybot")) {
                        ;
                  }

                  float var6 = (float)((this.parent.parent.getX() + (3 >> 4 & 908374161 ^ 7)) * (1 ^ 0 ^ 0 ^ 0 ^ 3));
                  Frame var10003 = this.parent.parent;
                  if ((269811717 >> 2 ^ 67452929) == 0) {
                        ;
                  }

                  int var7 = (var10003.getY() + this.offset + ((0 & 1289528802 | 757183662) ^ 757183660)) * ((0 >>> 3 | 153085298) << 3 ^ 1224682386) - (0 >> 3 << 2 ^ 3);
                  if ((1192476883 >>> 2 << 2 >> 2 >> 3 ^ -182810220) != 0) {
                        ;
                  }

                  float var8 = (float)var7;
                  Color var10004 = new Color;
                  int var10006 = ((50 & 16 ^ 11) << 3 | 83) ^ 36;
                  int var10007 = ((532306279 >>> 2 | 15904051) & 16143450) << 2 ^ 64508264;
                  int var10008 = 1584540661 << 4 >>> 2 >>> 2 >>> 1 ^ 121181690;
                  int var10009 = (240 << 2 | 712) << 2 ^ 4063;
                  if ((68160541 >>> 3 ^ 6326971 ^ 14846776) == 0) {
                        ;
                  }

                  var10004.<init>(var10006, var10007, var10008, var10009);
                  var1.drawString(var4, var6, var8, var10004.getRGB(), (boolean)(0 >>> 2 >> 4 ^ 1));
                  if (!"intentMoment".equals("stringer is a good obfuscator")) {
                        ;
                  }
            }

            GL11.glPopMatrix();
      }

      public void mouseClicked(int var1, int var2, int var3) {
            if (this.isMouseOnButton(var1, var2) && var3 == 0) {
                  Button var10000 = this.parent;
                  if (!"you're dogshit".equals("please go outside")) {
                        ;
                  }

                  if (var10000.open) {
                        this.modeSetting.cycle();
                  }
            }

      }

      public ModeButton(ModeSetting var1, Button var2, Module var3, int var4) {
            this.parent = var2;
            this.mod = var3;
            this.x = var2.parent.getX() + var2.parent.getWidth();
            if (((1963946508 | 1432639126 | 372453462) ^ -545543768) != 0) {
                  ;
            }

            Frame var10001 = var2.parent;
            if (!"minecraft".equals("yo mama name maurice")) {
                  ;
            }

            this.y = var10001.getY() + var2.offset;
            if (((((835587434 | 19269827) >>> 3 ^ 102878577) >> 2 | 339551) ^ -1587053593) != 0) {
                  ;
            }

            this.offset = var4;
            this.modeSetting = var1;
            if (((494542114 << 2 ^ 1485797811 | 216088813) << 2 ^ 1188833745 ^ 1425633223) != 0) {
                  ;
            }

      }
}
