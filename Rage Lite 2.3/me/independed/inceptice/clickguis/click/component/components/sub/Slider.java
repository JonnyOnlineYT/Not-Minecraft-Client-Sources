//Deobfuscated with https://github.com/PetoPetko/Minecraft-Deobfuscator3000 using mappings "wew"!

package me.independed.inceptice.clickguis.click.component.components.sub;

import java.awt.Color;
import java.math.BigDecimal;
import java.math.RoundingMode;
import me.independed.inceptice.clickguis.click.component.Component;
import me.independed.inceptice.clickguis.click.component.components.Button;
import me.independed.inceptice.font.GlyphPageFontRenderer;
import me.independed.inceptice.settings.NumberSetting;
import me.independed.inceptice.ui.Hud;
import net.minecraft.client.gui.Gui;
import org.lwjgl.opengl.GL11;

public class Slider extends Component {
      private int y;
      private int x;
      private boolean dragging;
      private NumberSetting val;
      private Button parent;
      private int offset;
      private double renderWidth;
      private boolean hovered;

      public Slider(NumberSetting var1, Button var2, int var3) {
            int var10001 = 319045924 >> 1 & 77988875 ^ 8519682;
            if (((1850360541 << 2 & 772844361 ^ 548118549) & 39944228 ^ 2195460) == 0) {
                  ;
            }

            this.dragging = (boolean)var10001;
            this.val = var1;
            if (!"shitted on you harder than archybot".equals("please take a shower")) {
                  ;
            }

            this.parent = var2;
            this.x = var2.parent.getX() + var2.parent.getWidth();
            this.y = var2.parent.getY() + var2.offset;
            if (!"idiot".equals("your mom your dad the one you never had")) {
                  ;
            }

            this.offset = var3;
      }

      public boolean isMouseOnButtonI(int var1, int var2) {
            int var10001 = this.x;
            Button var10002 = this.parent;
            if ((((1829967354 | 1140809502) >>> 1 | 627126297 | 938648494) ^ 939507711) == 0) {
                  ;
            }

            if (var1 > var10001 + var10002.parent.getWidth() / (0 >>> 3 << 4 & 1737677698 ^ 2)) {
                  var10001 = this.x;
                  var10002 = this.parent;
                  if (((799940548 << 2 | 1559120253) & 547334089 & 424065087 ^ 1319629150) != 0) {
                        ;
                  }

                  if (var1 < var10001 + var10002.parent.getWidth() && var2 > this.y && var2 < this.y + ((((9 ^ 1) & 0) >> 4 | 524518859) ^ 524518853)) {
                        return (boolean)((0 ^ 1589503539) >>> 2 >>> 4 ^ 24835993);
                  }
            }

            return (boolean)((1847966323 ^ 307065919 | 255095451) ^ 2138898143);
      }

      public void mouseClicked(int var1, int var2, int var3) {
            if ((((147457774 ^ 10273224) >>> 4 | 2756535) ^ 1064532 ^ 713254237) != 0) {
                  ;
            }

            if (this.isMouseOnButtonD(var1, var2) && var3 == 0 && this.parent.open) {
                  this.dragging = (boolean)(0 ^ 351350938 ^ 103664978 ^ 316472777);
            }

            if (((359957794 ^ 135259346) << 2 ^ 256387494 ^ -1199795475) != 0) {
                  ;
            }

            boolean var10000 = this.isMouseOnButtonI(var1, var2);
            if (!"You're so fat whenever you go to the beach the tide comes in.".equals("you probably spell youre as your")) {
                  ;
            }

            if (var10000 && var3 == 0 && this.parent.open) {
                  this.dragging = (boolean)(0 << 1 ^ 845826114 ^ 845826115);
            }

      }

      public void setOff(int var1) {
            if ((((960516800 | 921914323) >>> 1 | 175229259) << 2 ^ 1117774123) != 0) {
                  ;
            }

            this.offset = var1;
      }

      private static double roundToPlace(double var0, int var2) {
            if (var2 < 0) {
                  IllegalArgumentException var4 = new IllegalArgumentException;
                  if (!"i hope you catch fire ngl".equals("ape covered in human flesh")) {
                        ;
                  }

                  var4.<init>();
                  throw var4;
            } else {
                  BigDecimal var3 = new BigDecimal(var0);
                  RoundingMode var10002 = RoundingMode.HALF_UP;
                  if (((893783074 | 694877541) ^ 1030684007) == 0) {
                        ;
                  }

                  BigDecimal var10000 = var3.setScale(var2, var10002);
                  if (((435731435 >>> 3 & 43667000 | 25903858) ^ 614408431) != 0) {
                        ;
                  }

                  var3 = var10000;
                  return var3.doubleValue();
            }
      }

      public void updateComponent(int var1, int var2) {
            if (!"please go outside".equals("Your family tree must be a cactus because everyone on it is a prick.")) {
                  ;
            }

            if (((900172392 >> 4 ^ 44189937 ^ 24878017) << 2 ^ -1229234408) != 0) {
                  ;
            }

            this.hovered = (boolean)(!this.isMouseOnButtonD(var1, var2) && !this.isMouseOnButtonI(var1, var2) ? 1954777333 << 2 >>> 4 ^ 220258877 : 0 >> 2 >>> 3 ^ 1);
            int var10001 = this.parent.parent.getY();
            if (!"buy a domain and everything else you need at namecheap.com".equals("Some babies were dropped on their heads but you were clearly thrown at a wall.")) {
                  ;
            }

            this.y = var10001 + this.offset;
            if ((((234498002 ^ 221316) >>> 1 | 98838413) ^ -527440625) != 0) {
                  ;
            }

            this.x = this.parent.parent.getX();
            int var10000 = (41 >>> 1 ^ 16 ^ 1 | 4) ^ 93;
            var10001 = (680286110 | 490703003) << 4 ^ -604112400;
            if ((891140757 >>> 3 >> 2 ^ 27848148) == 0) {
                  ;
            }

            double var3 = (double)Math.min(var10000, Math.max(var10001, var1 - this.x));
            double var5 = this.val.getMinimum();
            double var7 = this.val.getMaximum();
            NumberSetting var10002 = this.val;
            if (!"idiot".equals("intentMoment")) {
                  ;
            }

            this.renderWidth = 88.0D * (var10002.getValue() - var5) / (var7 - var5);
            if ((((1197863895 >>> 1 ^ 249134082) >> 3 | 84333592) ^ 1524420998) != 0) {
                  ;
            }

            boolean var11 = this.dragging;
            if (((353162776 ^ 339546589) >>> 2 >> 2 ^ 502211 ^ 905423210) != 0) {
                  ;
            }

            if (var11) {
                  if (var3 == 0.0D) {
                        if ((97341206 << 1 >>> 1 ^ 14905997 ^ 56247857) != 0) {
                              ;
                        }

                        this.val.setValue(this.val.getMinimum());
                  } else {
                        if (((1791849307 | 1090132465) >>> 2 >> 1 ^ -1706937836) != 0) {
                              ;
                        }

                        if ((179007251 >>> 4 >> 3 ^ 334573080) != 0) {
                              ;
                        }

                        double var9 = Slider.roundToPlace(var3 / 88.0D * (var7 - var5) + var5, (1 >>> 1 ^ 495246273 ^ 224160245 | 165958640) & 235304288 ^ 134508898);
                        NumberSetting var12 = this.val;
                        if ((2060213277 >> 2 & 330868030 ^ -1776927489) != 0) {
                              ;
                        }

                        var12.setValue(var9);
                  }
            }

            if (!"you're dogshit".equals("you probably spell youre as your")) {
                  ;
            }

      }

      public void renderComponent() {
            double var10000 = (double)this.parent.parent.getX();
            Button var10001 = this.parent;
            if (!"shitted on you harder than archybot".equals("You're so fat whenever you go to the beach the tide comes in.")) {
                  ;
            }

            double var3 = (double)(var10001.parent.getY() + this.offset);
            int var10002 = this.parent.parent.getWidth();
            if (!"minecraft".equals("Some babies were dropped on their heads but you were clearly thrown at a wall.")) {
                  ;
            }

            double var7 = (double)var10002;
            int var11;
            if (this.hovered) {
                  Color var10005 = new Color;
                  int var10007 = 209037363 >>> 4 >>> 4 ^ 816552;
                  int var10008 = 1404663033 << 1 ^ 1343385518 ^ -144669092;
                  int var10009 = (721592449 >>> 2 << 4 | 2103975401) ^ -43343895;
                  if (!"shitted on you harder than archybot".equals("your mom your dad the one you never had")) {
                        ;
                  }

                  var10005.<init>(var10007, var10008, var10009, (9 & 5 & 0 | 1069658939) ^ 1069659040);
                  var11 = var10005.getRGB();
                  if (((1661914052 >> 4 >> 2 | 11129738) >>> 3 ^ 1699682085) != 0) {
                        ;
                  }
            } else {
                  var11 = (new Color((368914587 ^ 179728924 | 435720841) << 1 & 266722878 ^ 266699806, (545506112 >> 4 | 25855499) ^ 59424319, (1817724237 ^ 1693940595 | 38103600) << 3 ^ 1467740656, (118 >>> 2 | 1) ^ 20 ^ 116)).getRGB();
            }

            Hud.drawRoundedRect(var10000, var3, var7, 14.0D, 4.0D, var11);
            int var2 = (int)(this.val.getValue() / this.val.getMaximum() * (double)this.parent.parent.getWidth());
            if (((310509573 >>> 4 | 14684504) << 1 >> 3 ^ 1186174865) != 0) {
                  ;
            }

            var2 = this.parent.parent.getX();
            if (!"i hope you catch fire ngl".equals("stringer is a good obfuscator")) {
                  ;
            }

            var2 += 3 >> 4 ^ 1708504140 ^ 1708504138;
            int var5 = this.parent.parent.getY() + this.offset;
            var10002 = this.parent.parent.getX() + (int)this.renderWidth;
            int var10003 = this.parent.parent.getY() + this.offset + ((2 ^ 0) >> 3 >> 4 ^ 14);
            int var10004;
            if (this.hovered) {
                  if (((1682370284 ^ 661032741) << 1 ^ -2042575982) == 0) {
                        ;
                  }

                  var10004 = (new Color((52 ^ 1) << 1 ^ 93, (25 ^ 18) >>> 2 << 4 ^ 158, ((48 & 25) >> 2 | 3) ^ 48, (252 | 125) >> 1 >>> 2 << 3 >> 4 ^ 240)).getRGB();
            } else {
                  Color var10 = new Color;
                  if (!"Your family tree must be a cactus because everyone on it is a prick.".equals("you probably spell youre as your")) {
                        ;
                  }

                  var10.<init>((34 >> 3 ^ 3) >> 3 >> 1 ^ 55, (99 << 3 ^ 573 | 201) >> 3 & 49 ^ 170, 13 << 1 >>> 4 << 1 ^ 53, 50 >>> 1 >> 2 >> 2 ^ 254);
                  var10004 = var10.getRGB();
            }

            Gui.drawRect(var2, var5, var10002, var10003, var10004);
            GL11.glPushMatrix();
            if (!"please take a shower".equals("please go outside")) {
                  ;
            }

            GL11.glScalef(0.5F, 0.5F, 0.5F);
            if (!"i hope you catch fire ngl".equals("shitted on you harder than archybot")) {
                  ;
            }

            GlyphPageFontRenderer var4 = Hud.renderer;
            String var6 = (new StringBuilder()).append(this.val.name).append(":").append(this.val.getValue()).toString();
            float var8 = (float)(this.parent.parent.getX() * (((0 >>> 3 | 1197444061) << 1 | 669473344 | 2053265006) ^ -40964) + (((5 >>> 3 | 2144077085) >> 2 >> 1 | 149149954) ^ 268163500));
            Button var9 = this.parent;
            if (!"nefariousMoment".equals("please go outside")) {
                  ;
            }

            var10003 = var9.parent.getY() + this.offset + (1 >>> 3 >> 3 ^ 2);
            var10004 = (1 >>> 1 | 1178505311) >>> 2 ^ 294626325;
            if (((1710093029 >> 1 ^ 330891730 | 278458024) & 631717429 ^ -465359740) != 0) {
                  ;
            }

            var4.drawString(var6, var8, (float)(var10003 * var10004 - ((1 >>> 3 ^ 1620251768) & 817317181 ^ 546505787)), (new Color(((189 & 18) >> 1 & 2) >>> 4 >>> 1 ^ 255, ((7 & 5) >> 2 | 0) ^ 11, 1 << 4 >>> 3 << 1 >>> 1 ^ 8, ((209 | 57 | 45) & 95 & 21) >> 3 ^ 253)).getRGB(), (boolean)((266377399 >> 3 >>> 4 ^ 1232831 ^ 639830) & 79701 ^ 1360));
            GL11.glPopMatrix();
      }

      public void mouseReleased(int var1, int var2, int var3) {
            this.dragging = (boolean)((1850338949 >>> 1 ^ 772960838) & 320433666 ^ 193724265 ^ 434686026 ^ 57881379);
      }

      public boolean isMouseOnButtonD(int var1, int var2) {
            if ((((481834372 | 200625270) >> 4 | 30367200) >>> 2 >>> 1 ^ 4193279) == 0) {
                  ;
            }

            if (((1650714905 << 2 ^ 234264630) << 3 ^ 600433296) == 0) {
                  ;
            }

            if (var1 > this.x) {
                  int var10001 = this.x;
                  int var10002 = this.parent.parent.getWidth();
                  int var10003 = 0 >>> 4 >>> 3 ^ 2;
                  if (((1080076018 ^ 230708990) << 4 ^ -636550976) == 0) {
                        ;
                  }

                  if (var1 < var10001 + var10002 / var10003 + ((0 | 1641393515) >> 2 >> 2 >> 4 ^ 6411692)) {
                        if (!"You're so fat whenever you go to the beach the tide comes in.".equals("stringer is a good obfuscator")) {
                              ;
                        }

                        var10001 = this.y;
                        if ((540951561 << 1 >> 3 ^ -1541035572) != 0) {
                              ;
                        }

                        if (var2 > var10001) {
                              if (((270742277 | 15587232 | 262966876) ^ -218560874) != 0) {
                                    ;
                              }

                              if (var2 < this.y + (3 >> 3 >>> 4 << 4 ^ 14)) {
                                    return (boolean)(0 >>> 1 << 2 ^ 1);
                              }
                        }
                  }
            }

            return (boolean)(2042340025 >>> 4 >>> 2 ^ 31911562);
      }
}
