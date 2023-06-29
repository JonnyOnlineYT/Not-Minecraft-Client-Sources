package me.independed.inceptice.clickguis.click.component.components.sub;

import java.awt.Color;
import me.independed.inceptice.clickguis.click.component.Component;
import me.independed.inceptice.clickguis.click.component.Frame;
import me.independed.inceptice.clickguis.click.component.components.Button;
import me.independed.inceptice.font.GlyphPageFontRenderer;
import me.independed.inceptice.modules.Module;
import me.independed.inceptice.ui.Hud;
import org.lwjgl.input.Keyboard;
import org.lwjgl.opengl.GL11;

public class Keybind extends Component {
      private Button parent;
      private int offset;
      private boolean binding;
      private int x;
      private boolean hovered;
      private int y;

      public Keybind(Button var1, int var2) {
            if ((32653158 >>> 4 >> 1 ^ 51125977) != 0) {
                  ;
            }

            this.parent = var1;
            this.x = var1.parent.getX() + var1.parent.getWidth();
            int var10001 = var1.parent.getY();
            if (!"your mom your dad the one you never had".equals("nefariousMoment")) {
                  ;
            }

            this.y = var10001 + var1.offset;
            if (((1543881530 >>> 2 & 215525829 | 14922360) ^ 82031356) == 0) {
                  ;
            }

            this.offset = var2;
      }

      public void setOff(int var1) {
            this.offset = var1;
      }

      public void updateComponent(int var1, int var2) {
            boolean var10001 = this.isMouseOnButton(var1, var2);
            if (!"buy a domain and everything else you need at namecheap.com".equals("minecraft")) {
                  ;
            }

            this.hovered = var10001;
            int var3 = this.parent.parent.getY();
            if (((313510581 | 87595659) >> 2 >> 3 ^ 318947218) != 0) {
                  ;
            }

            this.y = var3 + this.offset;
            this.x = this.parent.parent.getX();
      }

      public void renderComponent() {
            if ((968036538 << 4 << 1 ^ -1742821067) != 0) {
                  ;
            }

            double var10000 = (double)this.parent.parent.getX();
            double var10001 = (double)(this.parent.parent.getY() + this.offset);
            double var10002 = (double)this.parent.parent.getWidth();
            if (((9441382 << 3 << 1 ^ 81480464 | 155435369) ^ 261621565) != 0) {
                  ;
            }

            int var8;
            Color var10005;
            if (this.hovered) {
                  var10005 = new Color;
                  int var10007 = (1082327113 | 853325211) >> 4 ^ 120453981;
                  int var10008 = ('鉀' | 25051) << 1 ^ 124854;
                  int var10009 = (784572053 >>> 2 >>> 2 | 17606675) ^ 65845755;
                  int var10010 = (110 << 1 & 199) >>> 1 ^ 249;
                  if (!"idiot".equals("you're dogshit")) {
                        ;
                  }

                  var10005.<init>(var10007, var10008, var10009, var10010);
                  var8 = var10005.getRGB();
            } else {
                  if (((374918095 | 62950966 | 239437904) >>> 3 ^ 66828799) == 0) {
                        ;
                  }

                  var10005 = new Color((452078495 << 4 ^ 1166556334) >>> 2 << 1 ^ 1968521902, (195545727 >> 3 | 2781100) >>> 2 >>> 1 ^ 3137533, (1301813495 << 4 ^ 173372266) & 93027323 & 1372176 ^ 28688, 105 << 2 >> 4 ^ 6 ^ 97);
                  if (((134240450 >> 4 | 5157340) ^ 13546972) == 0) {
                        ;
                  }

                  var8 = var10005.getRGB();
            }

            Hud.drawRoundedRect(var10000, var10001, var10002, 14.0D, 4.0D, var8);
            GL11.glPushMatrix();
            GL11.glScalef(0.5F, 0.5F, 0.5F);
            GlyphPageFontRenderer var1 = Hud.renderer;
            String var2;
            if (this.binding) {
                  var2 = "Press a key...";
            } else {
                  StringBuilder var3 = (new StringBuilder()).append("Key: ");
                  Button var4 = this.parent;
                  if (((460693097 >>> 2 | 13942334) ^ -761377866) != 0) {
                        ;
                  }

                  var2 = var3.append(Keyboard.getKeyName(var4.mod.getKey())).toString();
            }

            if ((((187353032 | 138128200) << 2 | 599290546) ^ 805306290) == 0) {
                  ;
            }

            int var5 = this.parent.parent.getX() + (1 << 4 >> 2 & 1 ^ 7);
            if ((((42024987 >> 1 | 18224861) ^ 20888296) >> 3 ^ 852358) == 0) {
                  ;
            }

            float var6 = (float)(var5 * ((0 & 1290636724 & 1409357335 | 318190912) >> 3 ^ 39773866));
            Frame var10003 = this.parent.parent;
            if ((((1038973480 | 414927009) & 544548435) >> 4 >> 3 ^ -1409307338) != 0) {
                  ;
            }

            float var7 = (float)((var10003.getY() + this.offset + ((1 | 0) >> 3 ^ 1560440704 ^ 1560440706)) * ((0 & 388620161 ^ 1445572639) << 1 ^ -1403822020) - (((0 ^ 145255347) >>> 3 >>> 3 | 382852) ^ 2619309));
            if ((950570803 >>> 4 ^ 31924585 ^ 40741274) == 0) {
                  ;
            }

            var1.drawString(var2, var6, var7, (new Color((85 ^ 70) >> 3 ^ 253, (6 >> 1 | 0) ^ 1 ^ 8, 4 << 4 ^ 13 ^ 73 ^ 14, ((115 & 4) >>> 3 >>> 2 & 1568026044 | 499975436) ^ 499975667)).getRGB(), (boolean)(0 << 3 << 1 << 2 ^ 1));
            GL11.glPopMatrix();
      }

      public void mouseClicked(int var1, int var2, int var3) {
            if (this.isMouseOnButton(var1, var2) && var3 == 0) {
                  boolean var10000 = this.parent.open;
                  if (!"please take a shower".equals("ape covered in human flesh")) {
                        ;
                  }

                  if (var10000) {
                        int var10001;
                        if (!this.binding) {
                              var10001 = (0 ^ 1624554173) >> 4 & 53021313 ^ 34146944;
                              if ((88453801 >>> 2 & 12545891 ^ 1141794) == 0) {
                                    ;
                              }
                        } else {
                              var10001 = (2074615277 >>> 3 << 1 ^ 19022447) << 2 ^ 2132874324;
                        }

                        this.binding = (boolean)var10001;
                        if (!"your mom your dad the one you never had".equals("Some babies were dropped on their heads but you were clearly thrown at a wall.")) {
                              ;
                        }
                  }
            }

      }

      public void keyTyped(char var1, int var2) {
            if (this.binding) {
                  Module var10000 = this.parent.mod;
                  if (!"buy a domain and everything else you need at namecheap.com".equals("minecraft")) {
                        ;
                  }

                  var10000.setKey(var2);
                  if (((1636079455 >> 2 >>> 3 | 31928122) >>> 4 ^ 1786451095) != 0) {
                        ;
                  }

                  this.binding = (boolean)((77441281 | 61704412) ^ 5283255 ^ 132985962);
            }

            if (((1802325708 ^ 504012320 | 91734558) >> 1 >> 4 ^ -1816250510) != 0) {
                  ;
            }

      }

      public boolean isMouseOnButton(int var1, int var2) {
            if (var1 > this.x && var1 < this.x + ((79 >>> 3 ^ 0) >>> 1 ^ 92) && var2 > this.y && var2 < this.y + ((0 ^ 1824952895 ^ 1001176542) >>> 1 >>> 4 ^ 45830513)) {
                  return (boolean)(((0 ^ 900347333) >>> 3 | 3710549) ^ 113106684);
            } else {
                  int var10000 = 14811522 >> 2 ^ 3702880;
                  if (!"minecraft".equals("idiot")) {
                        ;
                  }

                  return (boolean)var10000;
            }
      }
}
