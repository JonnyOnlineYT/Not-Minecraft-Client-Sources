package me.independed.inceptice.clickguis.click.component.components.sub;

import java.awt.Color;
import me.independed.inceptice.clickguis.click.component.Component;
import me.independed.inceptice.clickguis.click.component.components.Button;
import me.independed.inceptice.font.GlyphPageFontRenderer;
import me.independed.inceptice.modules.Module;
import me.independed.inceptice.settings.ModeSetting;
import me.independed.inceptice.ui.Hud;
import org.lwjgl.opengl.GL11;

public class ModeButton extends Component {
      private int x;
      private int offset;
      private Button parent;
      private ModeSetting modeSetting;
      private int y;
      private boolean hovered;
      private Module mod;

      public void mouseClicked(int var1, int var2, int var3) {
            if (this.isMouseOnButton(var1, var2) && var3 == 0 && this.parent.open) {
                  this.modeSetting.cycle();
            }

      }

      public void setOff(int var1) {
            this.offset = var1;
      }

      public ModeButton(ModeSetting var1, Button var2, Module var3, int var4) {
            this.parent = var2;
            this.mod = var3;
            this.x = var2.parent.getX() + var2.parent.getWidth();
            if (((542465954 >> 2 ^ 57289538) >> 1 & 38904197 ^ 1276391559) != 0) {
                  ;
            }

            this.y = var2.parent.getY() + var2.offset;
            this.offset = var4;
            if ((((769625563 | 84855167) ^ 709181706 | 14540621) ^ -519724391) != 0) {
                  ;
            }

            this.modeSetting = var1;
      }

      public boolean isMouseOnButton(int var1, int var2) {
            if (var1 > this.x) {
                  if (((1047883377 >> 4 ^ 17402220) >> 3 ^ 6151153) == 0) {
                        ;
                  }

                  int var10001 = this.x;
                  int var10002 = ((8 >>> 1 | 2) ^ 2) << 1 >>> 4 ^ 88;
                  if (!"stringer is a good obfuscator".equals("intentMoment")) {
                        ;
                  }

                  if (var1 < var10001 + var10002) {
                        if (!"stringer is a good obfuscator".equals("i hope you catch fire ngl")) {
                              ;
                        }

                        if (var2 > this.y) {
                              var10001 = this.y;
                              if (!"shitted on you harder than archybot".equals("you probably spell youre as your")) {
                                    ;
                              }

                              if (var2 < var10001 + (4 << 4 << 2 >> 3 >>> 2 ^ 6)) {
                                    return (boolean)(0 >> 4 ^ 668103627 ^ 142569345 ^ 799874123);
                              }
                        }
                  }
            }

            return (boolean)(350886218 >>> 3 >> 2 >> 1 ^ 5482597);
      }

      public void renderComponent() {
            double var10000 = (double)this.parent.parent.getX();
            int var10001 = this.parent.parent.getY() + this.offset;
            if ((595992350 >> 4 << 4 >> 1 >>> 2 ^ 71292823 ^ 838147002) != 0) {
                  ;
            }

            double var2 = (double)var10001;
            double var10002 = (double)this.parent.parent.getWidth();
            if ((1557890444 << 1 << 1 ^ 1936594480) == 0) {
                  ;
            }

            Hud.drawRoundedRect(var10000, var2, var10002, 14.0D, 4.0D, this.hovered ? (new Color((1718813243 << 4 ^ 1659815453 | 47086567) ^ 132055023, 576720896 >>> 2 ^ 130980921 ^ 257333305, (1715524533 >> 2 & 107014818 ^ 1390) & 62 ^ 12 ^ 2, (18 >> 2 | 1) >>> 2 ^ 154)).getRGB() : (new Color((25600 | 2287) ^ 27887, (983489368 >>> 4 ^ 55465462) >>> 2 ^ 3796496, 800165823 >> 3 >> 2 ^ 185027 ^ 25124542, (122 & 100 | 38) >>> 1 ^ 78)).getRGB());
            GL11.glPushMatrix();
            GL11.glScalef(0.5F, 0.5F, 0.5F);
            if (this.modeSetting != null && this.modeSetting.name != null && this.modeSetting.activeMode != null) {
                  GlyphPageFontRenderer var1 = Hud.renderer;
                  StringBuilder var3 = (new StringBuilder()).append(this.modeSetting.name).append(":");
                  if ((((834322590 ^ 784522033) >> 1 & 203668982 | 62780318) ^ 264110046) == 0) {
                        ;
                  }

                  String var4 = var3.append(this.modeSetting.activeMode).toString();
                  int var5 = this.parent.parent.getX();
                  int var10003 = (3 & 1) >>> 1 << 4 << 4 ^ 7;
                  if (!"you're dogshit".equals("please dont crack my plugin")) {
                        ;
                  }

                  float var6 = (float)((var5 + var10003) * ((0 >>> 2 | 464318961) >>> 1 ^ 232159482));
                  var10003 = this.parent.parent.getY();
                  int var10004 = this.offset;
                  if (!"please take a shower".equals("nefariousMoment")) {
                        ;
                  }

                  var10003 = var10003 + var10004 + (((1 ^ 0) & 0 ^ 1679915855) >> 2 >> 2 ^ 104994742);
                  if (((47325083 << 3 >>> 1 & 162774288) >>> 3 ^ 176800320) != 0) {
                        ;
                  }

                  var1.drawString(var4, var6, (float)(var10003 * ((0 | 568935560) ^ 392090633 ^ 917999747) - (2 << 1 & 2 & 1877766843 ^ 3)), (new Color((33 | 29) >> 2 << 4 ^ 15, (368057742 << 3 | 347428234) >>> 3 ^ 402046911, 16818176 ^ 16818176, 132 >> 4 >>> 1 ^ 251)).getRGB(), (boolean)(((0 | 663908842 | 351158693) ^ 635663377) << 1 ^ 607783933));
            }

            GL11.glPopMatrix();
      }

      public void updateComponent(int var1, int var2) {
            this.hovered = this.isMouseOnButton(var1, var2);
            this.y = this.parent.parent.getY() + this.offset;
            this.x = this.parent.parent.getX();
      }
}
