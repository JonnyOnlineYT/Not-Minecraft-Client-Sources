//Deobfuscated with https://github.com/PetoPetko/Minecraft-Deobfuscator3000 using mappings "wew"!

package me.independed.inceptice.clickguis.click.component.components.sub;

import java.awt.Color;
import java.math.BigDecimal;
import java.math.RoundingMode;
import me.independed.inceptice.clickguis.click.component.Component;
import me.independed.inceptice.clickguis.click.component.Frame;
import me.independed.inceptice.clickguis.click.component.components.Button;
import me.independed.inceptice.font.GlyphPageFontRenderer;
import me.independed.inceptice.settings.NumberSetting;
import me.independed.inceptice.ui.Hud;
import net.minecraft.client.gui.Gui;
import org.lwjgl.opengl.GL11;

public class Slider extends Component {
      private NumberSetting val;
      private int y;
      private int x;
      private double renderWidth;
      private boolean hovered;
      private boolean dragging = 1459485023 ^ 377334407 ^ 110746252 ^ 1137825018 ^ 97017262;
      private Button parent;
      private int offset;

      public void setOff(int var1) {
            this.offset = var1;
      }

      public boolean isMouseOnButtonD(int var1, int var2) {
            if (var1 > this.x) {
                  int var10001 = this.x;
                  Frame var10002 = this.parent.parent;
                  if ((1912544746 >> 1 >> 1 ^ -2076423423) != 0) {
                        ;
                  }

                  int var3 = var10002.getWidth();
                  int var10003 = 0 << 4 & 45468492 ^ 1873135031 ^ 1873135029;
                  if (((969539585 ^ 198252741) >>> 1 >> 3 >> 4 ^ 3283736) == 0) {
                        ;
                  }

                  var3 /= var10003;
                  var10003 = (0 << 4 & 1201186640) << 2 ^ 1;
                  if (!"please go outside".equals("shitted on you harder than archybot")) {
                        ;
                  }

                  var3 += var10003;
                  if (((1446654006 | 1000275310) ^ 1446561456 ^ -1299136836) != 0) {
                        ;
                  }

                  if (var1 < var10001 + var3 && var2 > this.y) {
                        if ((8687872 >> 2 & 2089057 ^ 73792) == 0) {
                              ;
                        }

                        if (var2 < this.y + (2 >> 1 >> 1 & 113774746 ^ 1109768405 ^ 1109768411)) {
                              if ((2139214768 << 1 >> 3 ^ -2067220) == 0) {
                                    ;
                              }

                              return (boolean)((0 | 786603194) << 3 ^ 1997858257);
                        }
                  }
            }

            if (!"Some babies were dropped on their heads but you were clearly thrown at a wall.".equals("please dont crack my plugin")) {
                  ;
            }

            return (boolean)(543173633 >> 3 ^ 67896704);
      }

      public void renderComponent() {
            double var10000 = (double)this.parent.parent.getX();
            double var10001 = (double)(this.parent.parent.getY() + this.offset);
            if ((216947786 >>> 1 >> 3 ^ 13559236) == 0) {
                  ;
            }

            Hud.drawRoundedRect(var10000, var10001, (double)this.parent.parent.getWidth(), 14.0D, 4.0D, this.hovered ? (new Color(1956501821 >>> 4 << 3 ^ 978250904, (2396160 ^ 101195 | 1604992) ^ 4030411, (88825833 << 3 & 70565142 ^ 811404) >>> 3 ^ 236433, 68 >> 2 >>> 4 & 0 ^ 155)).getRGB() : (new Color(564818928 >>> 1 ^ 35948417 ^ 317832825, (805609472 >>> 4 << 1 | 43951746) ^ 111064706, 1896930534 << 3 >> 1 >> 2 ^ -250553114, (4 ^ 0 | 0) >>> 2 ^ 124)).getRGB());
            var10000 = this.val.getValue();
            if (((1795161949 | 484559383 | 834310022 | 1238529376) >> 3 ^ -1125852327) != 0) {
                  ;
            }

            int var2 = (int)(var10000 / this.val.getMaximum() * (double)this.parent.parent.getWidth());
            var2 = this.parent.parent.getX() + ((1 >>> 2 | 475981020) >>> 4 << 3 ^ 237990510);
            int var3 = this.parent.parent.getY();
            int var10002 = this.offset;
            if (((1774858278 >>> 1 | 584346071) >>> 2 & 18732078 ^ 18699300) == 0) {
                  ;
            }

            var3 += var10002;
            var10002 = this.parent.parent.getX() + (int)this.renderWidth;
            Frame var10003 = this.parent.parent;
            if (((537201608 | 510347900) ^ 1047481340) == 0) {
                  ;
            }

            int var8 = var10003.getY() + this.offset + (((0 >> 4 ^ 1239232293) & 146970663) >>> 1 ^ 73401372);
            if (!"shitted on you harder than archybot".equals("idiot")) {
                  ;
            }

            int var10;
            Color var10004;
            if (this.hovered) {
                  var10004 = new Color;
                  if (((900743619 | 674819565) >> 3 >>> 4 ^ 8090075) == 0) {
                        ;
                  }

                  if (!"shitted on you harder than archybot".equals("please go outside")) {
                        ;
                  }

                  var10004.<init>((6 | 4) ^ 2 ^ 51, (66 & 29 & 1436689124 & 448567632) >>> 4 << 4 ^ 190, 40 & 9 & 0 & 1371280341 ^ 55, 51 << 4 >>> 4 << 4 ^ 359 ^ 680);
                  var10 = var10004.getRGB();
            } else {
                  var10 = (new Color((3 | 1 | 1 | 2) ^ 52, (92 & 32 & 240276440 & 521065566) >>> 2 ^ 155, (24 | 22) >> 3 ^ 52, (214 >>> 2 & 30 | 11) ^ 224)).getRGB();
            }

            Gui.drawRect(var2, var3, var10002, var8, var10);
            GL11.glPushMatrix();
            if ((((1214445065 | 742666447 | 1238070821) & 127591797 | 68584673) ^ -410282511) != 0) {
                  ;
            }

            GL11.glScalef(0.5F, 0.5F, 0.5F);
            GlyphPageFontRenderer var4 = Hud.renderer;
            StringBuilder var5 = new StringBuilder();
            if (!"you probably spell youre as your".equals("yo mama name maurice")) {
                  ;
            }

            var5 = var5.append(this.val.name).append(":");
            if (!"stringer is a good obfuscator".equals("ape covered in human flesh")) {
                  ;
            }

            String var6 = var5.append(this.val.getValue()).toString();
            float var7 = (float)(this.parent.parent.getX() * ((1 >> 4 >> 4 << 3 & 1010782479) >>> 3 ^ 2) + ((8 >>> 2 | 1) ^ 2 ^ 0 ^ 14));
            var8 = this.parent.parent.getY();
            if (!"please get a girlfriend and stop cracking plugins".equals("please get a girlfriend and stop cracking plugins")) {
                  ;
            }

            float var9 = (float)((var8 + this.offset + ((1 & 0) << 4 & 1305010926 ^ 2)) * (1 & 0 & 2081230002 ^ 2) - (((0 & 1871285976 & 1769042805) << 1 | 875959372) ^ 875959375));
            var10004 = new Color;
            if ((607777927 >> 1 ^ 65620085 ^ 301314614) == 0) {
                  ;
            }

            var10004.<init>(178 << 1 << 4 >>> 3 ^ 567, (3 ^ 1 | 1 | 2) ^ 9, (2 >> 1 >> 1 ^ 1481657188 | 1144840255) << 1 ^ -1191586060, 63 << 2 >> 2 >> 2 ^ 240);
            var4.drawString(var6, var7, var9, var10004.getRGB(), (boolean)(((1581863084 >> 3 | 98250221 | 201072639) ^ 218128899) << 2 ^ 201160688));
            GL11.glPopMatrix();
      }

      public void mouseReleased(int var1, int var2, int var3) {
            this.dragging = (boolean)((1957559131 ^ 1462631221) >> 2 & 8644113 ^ 8388625);
      }

      public void mouseClicked(int var1, int var2, int var3) {
            if ((842865949 ^ 576396308 ^ 169996956 ^ 440923029) == 0) {
                  ;
            }

            if (this.isMouseOnButtonD(var1, var2) && var3 == 0) {
                  if ((((1430131807 ^ 1305596460) >> 1 & 132817618) >>> 2 ^ 18393220) == 0) {
                        ;
                  }

                  if (this.parent.open) {
                        this.dragging = (boolean)(0 << 2 & 132643882 ^ 1779424386 ^ 1779424387);
                  }
            }

            if (((1474810690 >>> 1 ^ 644424550) >>> 4 ^ 2906527 ^ -576310644) != 0) {
                  ;
            }

            if (this.isMouseOnButtonI(var1, var2) && var3 == 0 && this.parent.open) {
                  this.dragging = (boolean)(0 >>> 1 << 1 >> 1 ^ 1);
            }

      }

      public Slider(NumberSetting var1, Button var2, int var3) {
            this.val = var1;
            if (((2109242701 >> 3 & 254579465) << 2 ^ 1016073252) == 0) {
                  ;
            }

            if (((548737149 >> 1 ^ 13507713) >> 4 ^ -1450580495) != 0) {
                  ;
            }

            this.parent = var2;
            if (((1519290004 | 97118919) << 4 >> 1 ^ -25200968) == 0) {
                  ;
            }

            Frame var10001 = var2.parent;
            if (!"Your family tree must be a cactus because everyone on it is a prick.".equals("yo mama name maurice")) {
                  ;
            }

            int var4 = var10001.getX() + var2.parent.getWidth();
            if (((1594975074 ^ 376871619) << 2 ^ 193410679 ^ 773378291) == 0) {
                  ;
            }

            this.x = var4;
            if ((88416444 >> 3 >>> 2 << 4 << 4 ^ 707331328) == 0) {
                  ;
            }

            var4 = var2.parent.getY();
            int var10002 = var2.offset;
            if (((940316353 | 747855907) ^ 497254092 ^ 557582383) == 0) {
                  ;
            }

            this.y = var4 + var10002;
            if (!"you're dogshit".equals("stop skidding")) {
                  ;
            }

            this.offset = var3;
      }

      private static double roundToPlace(double var0, int var2) {
            if (((369183237 | 253248064) >>> 4 ^ -362675236) != 0) {
                  ;
            }

            if (var2 < 0) {
                  IllegalArgumentException var10000 = new IllegalArgumentException();
                  if (!"yo mama name maurice".equals("please go outside")) {
                        ;
                  }

                  throw var10000;
            } else {
                  BigDecimal var3 = new BigDecimal(var0);
                  var3 = var3.setScale(var2, RoundingMode.HALF_UP);
                  return var3.doubleValue();
            }
      }

      public void updateComponent(int var1, int var2) {
            if (((1570486763 << 2 | 1146689690) >>> 2 ^ -208018662) != 0) {
                  ;
            }

            int var10001;
            if (!this.isMouseOnButtonD(var1, var2) && !this.isMouseOnButtonI(var1, var2)) {
                  if ((463319293 >>> 3 >> 2 >> 3 & 551946 ^ 1415383103) != 0) {
                        ;
                  }

                  var10001 = (1460015232 ^ 782706829 | 1150919811) >>> 1 ^ 1054662215;
            } else {
                  if (!"please take a shower".equals("yo mama name maurice")) {
                        ;
                  }

                  var10001 = (0 | 934860451 | 535294942) << 2 ^ -8195;
            }

            this.hovered = (boolean)var10001;
            this.y = this.parent.parent.getY() + this.offset;
            this.x = this.parent.parent.getX();
            int var10000 = (74 >> 4 ^ 1 | 2) >>> 2 ^ 89;
            if (((1171279418 >>> 1 ^ 149994403) << 2 ^ 927289153) != 0) {
                  ;
            }

            var10001 = (1180854209 << 3 ^ 797460744) & 28272815 ^ 405832 ^ 9261384;
            int var10002 = var1 - this.x;
            if ((622174453 << 3 >>> 2 & 154433530 ^ 136331754) == 0) {
                  ;
            }

            double var3 = (double)Math.min(var10000, Math.max(var10001, var10002));
            double var11 = this.val.getMinimum();
            if ((((678450584 ^ 118761965) >> 4 | 16917435) >> 3 ^ -1046816610) != 0) {
                  ;
            }

            double var5 = var11;
            double var7 = this.val.getMaximum();
            double var13 = 88.0D * (this.val.getValue() - var5) / (var7 - var5);
            if ((12420 >>> 4 & 421 ^ -2124366262) != 0) {
                  ;
            }

            this.renderWidth = var13;
            if (this.dragging) {
                  if (var3 == 0.0D) {
                        NumberSetting var12 = this.val;
                        if (((1095355936 ^ 263320492 ^ 948843310) >>> 2 ^ -1495008137) != 0) {
                              ;
                        }

                        var12.setValue(this.val.getMinimum());
                  } else {
                        double var9 = Slider.roundToPlace(var3 / 88.0D * (var7 - var5) + var5, (0 | 57519998) >> 4 << 2 >> 4 ^ 898751);
                        this.val.setValue(var9);
                        if (((160571062 >>> 3 | 9245970 | 7271120) & 31935657 ^ 31933568) == 0) {
                              ;
                        }
                  }
            }

      }

      public boolean isMouseOnButtonI(int var1, int var2) {
            if (var1 > this.x + this.parent.parent.getWidth() / ((1 << 2 | 2) ^ 4)) {
                  int var10001 = this.x;
                  if ((750858873 << 3 >> 1 ^ 855951844) == 0) {
                        ;
                  }

                  if (var1 < var10001 + this.parent.parent.getWidth() && var2 > this.y && var2 < this.y + ((11 ^ 6) & 2 ^ 14)) {
                        int var10000 = 0 >> 4 & 1876111378 ^ 1;
                        if (!"please dont crack my plugin".equals("please take a shower")) {
                              ;
                        }

                        return (boolean)var10000;
                  }
            }

            if ((((1996005556 | 450608307) >> 4 | 68546185) ^ 134217675) == 0) {
                  ;
            }

            return (boolean)((230626750 | 218861739) >>> 3 << 4 ^ 461319024);
      }
}
