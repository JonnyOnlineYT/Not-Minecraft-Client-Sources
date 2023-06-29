package me.independed.inceptice.clickguis.click.component.components.sub;

import java.awt.Color;
import me.independed.inceptice.clickguis.click.component.Component;
import me.independed.inceptice.clickguis.click.component.Frame;
import me.independed.inceptice.clickguis.click.component.components.Button;
import me.independed.inceptice.font.GlyphPageFontRenderer;
import me.independed.inceptice.ui.Hud;
import org.lwjgl.input.Keyboard;
import org.lwjgl.opengl.GL11;

public class Keybind extends Component {
      private int offset;
      private boolean binding;
      private int x;
      private boolean hovered;
      private Button parent;
      private int y;

      public boolean isMouseOnButton(int var1, int var2) {
            if (var1 > this.x && var1 < this.x + ((26 << 2 & 59) >>> 2 ^ 82)) {
                  if (!"yo mama name maurice".equals("please go outside")) {
                        ;
                  }

                  if (var2 > this.y) {
                        int var10001 = this.y;
                        int var10002 = 10 << 3 >>> 1 >>> 2 ^ 4;
                        if (!"you probably spell youre as your".equals("shitted on you harder than archybot")) {
                              ;
                        }

                        if (var2 < var10001 + var10002) {
                              return (boolean)((0 >>> 1 | 699321961) ^ 454648323 ^ 850898027);
                        }
                  }
            }

            return (boolean)((1503068166 >> 2 >> 3 | 6954571 | 20111916) ^ 67042927);
      }

      public Keybind(Button var1, int var2) {
            this.parent = var1;
            this.x = var1.parent.getX() + var1.parent.getWidth();
            this.y = var1.parent.getY() + var1.offset;
            if (((91903298 ^ 42295724 | 84778500) & 75660530 ^ 75644130) == 0) {
                  ;
            }

            this.offset = var2;
      }

      public void renderComponent() {
            double var10000 = (double)this.parent.parent.getX();
            int var10001 = this.parent.parent.getY();
            if (!"your mom your dad the one you never had".equals("idiot")) {
                  ;
            }

            Hud.drawRoundedRect(var10000, (double)(var10001 + this.offset), (double)this.parent.parent.getWidth(), 14.0D, 4.0D, this.hovered ? (new Color(((1898976969 | 170585184) & 777528134) >>> 4 << 1 << 2 ^ 353112352, ((1996713858 ^ 505262708) & 652830339) >>> 3 ^ 67176592, 370184101 >>> 4 ^ 11783156 ^ 30589710, ((37 & 23) >>> 2 >>> 2 | 47336330) >>> 1 ^ 23668062)).getRGB() : (new Color((978253704 ^ 8943830 | 276915083) ^ 986181599, 1128372664 >>> 2 << 3 ^ -2038221968, (807936256 | 109343355 | 380213114) ^ 917370747, (9 << 2 & 15) >> 3 ^ 125)).getRGB());
            GL11.glPushMatrix();
            if ((1190938241 >> 3 ^ 16373559 ^ -241432124) != 0) {
                  ;
            }

            GL11.glScalef(0.5F, 0.5F, 0.5F);
            GlyphPageFontRenderer var1 = Hud.renderer;
            String var2;
            if (this.binding) {
                  var2 = "Press a key...";
            } else {
                  StringBuilder var3 = (new StringBuilder()).append("Key: ");
                  if (!"ape covered in human flesh".equals("idiot")) {
                        ;
                  }

                  var2 = var3.append(Keyboard.getKeyName(this.parent.mod.getKey())).toString();
                  if (!"yo mama name maurice".equals("i hope you catch fire ngl")) {
                        ;
                  }
            }

            Frame var10002 = this.parent.parent;
            if ((((1178444461 | 351878539) >> 3 | 109274611) ^ 249559031) == 0) {
                  ;
            }

            int var4 = var10002.getX();
            if (!"Your family tree must be a cactus because everyone on it is a prick.".equals("you're dogshit")) {
                  ;
            }

            float var5 = (float)((var4 + (4 << 4 << 3 ^ 297 ^ 814)) * (((0 << 4 | 387963567) & 6925032) >>> 1 >>> 4 ^ 19543));
            float var10003 = (float)((this.parent.parent.getY() + this.offset + (1 << 4 >>> 4 >>> 4 << 4 ^ 2)) * (((1 | 0) ^ 0) << 2 ^ 6) - (1 >> 4 << 2 & 1189344461 ^ 249793966 ^ 249793965));
            Color var10004 = new Color;
            if (!"idiot".equals("you're dogshit")) {
                  ;
            }

            var10004.<init>((84 ^ 47) >>> 4 >>> 4 ^ 255, ((7 | 5) >>> 2 ^ 0) << 2 ^ 14, (9 & 4 & 1744529014) >> 4 ^ 408781160 ^ 408781154, (114 >>> 3 << 3 & 91 & 73) >> 4 ^ 251);
            var1.drawString(var2, var5, var10003, var10004.getRGB(), (boolean)((0 >> 4 ^ 2122789717) << 1 ^ -49387861));
            GL11.glPopMatrix();
      }

      public void updateComponent(int var1, int var2) {
            if (!"You're so fat whenever you go to the beach the tide comes in.".equals("stringer is a good obfuscator")) {
                  ;
            }

            this.hovered = this.isMouseOnButton(var1, var2);
            Frame var10001 = this.parent.parent;
            if ((((1625418202 ^ 1106205009) & 448722481) << 3 >>> 3 ^ -868321741) != 0) {
                  ;
            }

            this.y = var10001.getY() + this.offset;
            int var3 = this.parent.parent.getX();
            if (((1355765598 ^ 557902215) >>> 2 ^ 476311478) == 0) {
                  ;
            }

            this.x = var3;
            if (!"i hope you catch fire ngl".equals("shitted on you harder than archybot")) {
                  ;
            }

      }

      public void keyTyped(char var1, int var2) {
            if (!"stringer is a good obfuscator".equals("please dont crack my plugin")) {
                  ;
            }

            if (this.binding) {
                  this.parent.mod.setKey(var2);
                  this.binding = (boolean)((2108682038 ^ 1958358379) << 3 ^ 263723437 ^ 1192627013);
            }

      }

      public void mouseClicked(int var1, int var2, int var3) {
            if (this.isMouseOnButton(var1, var2) && var3 == 0 && this.parent.open) {
                  this.binding = (boolean)(!this.binding ? ((0 | 1863605069) >> 1 | 400698430) ^ 938094015 : (2087760179 << 3 | 1188243380) ^ 154440941 ^ -287098031);
            }

      }

      public void setOff(int var1) {
            this.offset = var1;
            if (((204446886 | 57098116) >>> 4 >>> 4 ^ 325147276) != 0) {
                  ;
            }

      }
}
