//Deobfuscated with https://github.com/PetoPetko/Minecraft-Deobfuscator3000 using mappings "wew"!

package me.independed.inceptice.clickguis.click.component.components;

import java.awt.Color;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import me.independed.inceptice.Main;
import me.independed.inceptice.clickguis.click.ClickGui;
import me.independed.inceptice.clickguis.click.component.Component;
import me.independed.inceptice.clickguis.click.component.Frame;
import me.independed.inceptice.clickguis.click.component.components.sub.Checkbox;
import me.independed.inceptice.clickguis.click.component.components.sub.Keybind;
import me.independed.inceptice.clickguis.click.component.components.sub.ModeButton;
import me.independed.inceptice.clickguis.click.component.components.sub.Slider;
import me.independed.inceptice.font.GlyphPageFontRenderer;
import me.independed.inceptice.modules.Module;
import me.independed.inceptice.settings.BooleanSetting;
import me.independed.inceptice.settings.ModeSetting;
import me.independed.inceptice.settings.NumberSetting;
import me.independed.inceptice.settings.Setting;
import me.independed.inceptice.ui.Hud;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.ScaledResolution;
import org.lwjgl.opengl.GL11;

public class Button extends Component {
      private boolean isHovered;
      private ArrayList subcomponents;
      public int inUp = ((1289816694 | 939927011) >>> 1 & 742654981) >> 4 ^ 46399552;
      public Module mod;
      public int offset;
      public Frame parent;
      public boolean open;
      private int height;

      public void keyTyped(char var1, int var2) {
            Iterator var3 = this.subcomponents.iterator();

            while(true) {
                  if ("you're dogshit".equals("please take a shower")) {
                  }

                  if (!var3.hasNext()) {
                        return;
                  }

                  Component var10000 = (Component)var3.next();
                  if ((38113276 >> 3 >>> 4 ^ 297759) == 0) {
                        ;
                  }

                  Component var4 = var10000;
                  var4.keyTyped(var1, var2);
            }
      }

      public void mouseReleased(int var1, int var2, int var3) {
            Iterator var4 = this.subcomponents.iterator();

            while(true) {
                  if (((167378665 | 97229410) >> 3 << 4 ^ 469237200) == 0) {
                        ;
                  }

                  if (((1793300072 >> 4 | 54357926) << 3 ^ -1782523602) != 0) {
                        ;
                  }

                  if (!var4.hasNext()) {
                        return;
                  }

                  Component var5 = (Component)var4.next();
                  if (((558717055 >>> 3 >> 1 >>> 2 | 3274810) & 8092812 ^ 3243016) == 0) {
                        ;
                  }

                  var5.mouseReleased(var1, var2, var3);
            }
      }

      public ArrayList getOptions() {
            ArrayList var1 = new ArrayList();
            Iterator var2 = this.mod.settings.iterator();

            while(var2.hasNext()) {
                  Setting var3 = (Setting)var2.next();
                  boolean var10000 = var3 instanceof BooleanSetting;
                  if (((9380116 | 5134724) >> 4 << 4 ^ 231574138) != 0) {
                        ;
                  }

                  if (var10000) {
                        if (!"intentMoment".equals("yo mama name maurice")) {
                              ;
                        }

                        if (((647358472 | 316383706) >>> 3 >> 3 ^ -455689195) != 0) {
                              ;
                        }

                        BooleanSetting var4 = (BooleanSetting)var3;
                        if ((((497806649 ^ 385489366) << 4 | 1305752903 | 1573855162) >>> 1 ^ -525161452) != 0) {
                              ;
                        }

                        var1.add(var4);
                  }

                  if (!"buy a domain and everything else you need at namecheap.com".equals("you're dogshit")) {
                        ;
                  }
            }

            return var1;
      }

      public void renderComponent() {
            ScaledResolution var1 = new ScaledResolution(Minecraft.getMinecraft());
            GlyphPageFontRenderer var10000;
            String var10001;
            float var10003;
            if (this.isHovered && this.inUp >= (3 >> 1 << 2 >>> 3 ^ 14)) {
                  var10000 = Hud.smallRenderer;
                  var10001 = this.mod.getDescription();
                  var10003 = (float)(var1.getScaledHeight() - (((11 << 2 ^ 4) >> 2 | 7) ^ 2));
                  if (((16777744 | 16007511) ^ 32785239) == 0) {
                        ;
                  }

                  var10000.drawString(var10001, 0.0F, var10003, (new Color((142 >>> 4 << 1 | 5) ^ 234, 1026068839 >>> 4 >>> 4 << 2 ^ 16032324, (1372927436 ^ 951824493) >> 3 & 205579699 & 110563509 ^ 67109040, (57 >>> 4 ^ 1) >>> 4 & 1970931324 ^ 255)).getRGB(), (boolean)(0 >> 3 >> 3 ^ 1));
            }

            if (!"stop skidding".equals("You're so fat whenever you go to the beach the tide comes in.")) {
                  ;
            }

            double var4;
            int var7;
            double var9;
            double var12;
            double var10002;
            int var10005;
            int var10007;
            int var10008;
            int var10009;
            if (this.inUp < ((5 | 4) >>> 3 & 1019251600 ^ 14)) {
                  var4 = (double)this.parent.getX();
                  Frame var5 = this.parent;
                  if (!"please go outside".equals("buy a domain and everything else you need at namecheap.com")) {
                        ;
                  }

                  var7 = var5.getY();
                  if (!"You're so fat whenever you go to the beach the tide comes in.".equals("please dont crack my plugin")) {
                        ;
                  }

                  var7 += this.offset;
                  if (!"Your family tree must be a cactus because everyone on it is a prick.".equals("nefariousMoment")) {
                        ;
                  }

                  var9 = (double)var7;
                  var10002 = (double)this.parent.getWidth();
                  var12 = (double)this.inUp;
                  if (this.isHovered) {
                        if (this.mod.isEnabled()) {
                              var10005 = (new Color((800999890 | 205371387 | 671057454) >>> 1 & 257342233 ^ 123124505, 1503146043 >>> 2 >> 1 ^ 62514165 ^ 143320562, ((260790756 ^ 89216153) & 166690311) >>> 4 >> 3 ^ 1152024, (88 ^ 50) << 3 >> 1 ^ 313)).getRGB();
                        } else {
                              var10005 = (new Color((387573937 | 314707000) & 71162570 ^ 68798600, ((1741648746 | 1216810979) >>> 3 ^ 231049713) >> 1 ^ 1979142, (133539268 >> 1 >>> 4 ^ 1409979) >>> 1 & 699985 ^ 1600, 146 >> 1 >> 4 >> 3 >> 1 ^ 155)).getRGB();
                              if ((258003080 >>> 2 >> 4 >> 1 & 2009090 ^ 836685394) != 0) {
                                    ;
                              }
                        }
                  } else if (this.mod.isEnabled()) {
                        if (((122661407 >> 3 >>> 4 | 909539) ^ 863698067) != 0) {
                              ;
                        }

                        var10005 = (new Color((478314496 ^ 367846081) & 1745776 ^ 664128, 2053290135 >>> 4 >> 2 >> 1 << 1 ^ 32082658, 44065824 ^ 44065824, (121 >> 2 & 2) << 3 ^ 109)).getRGB();
                  } else {
                        var10005 = (new Color((37879936 >> 3 | 299599) << 4 ^ 80553456, 14956544 << 1 >> 2 >>> 4 >>> 2 ^ 116848, (68009998 | 8547827) ^ 76541439, (122 ^ 90 | 21) ^ 52 ^ 124)).getRGB();
                  }

                  Hud.drawRoundedRect(var4, var9, var10002, var12, 8.0D, var10005);
                  this.inUp += (1 ^ 0 ^ 0) & 0 ^ 2;
            } else {
                  var4 = (double)this.parent.getX();
                  var7 = this.parent.getY();
                  if ((631400527 >>> 1 >> 1 ^ 157850131) == 0) {
                        ;
                  }

                  var9 = (double)(var7 + this.offset);
                  if ((935635967 >> 2 & 77326248 ^ 76620456) == 0) {
                        ;
                  }

                  var10002 = (double)this.parent.getWidth();
                  var12 = (double)this.inUp;
                  Color var18;
                  if (this.isHovered) {
                        if (this.mod.isEnabled()) {
                              if (!"You're so fat whenever you go to the beach the tide comes in.".equals("intentMoment")) {
                                    ;
                              }

                              var18 = new Color(790971453 ^ 308924063 ^ 759732282 ^ 268702360, ((381149446 | 136846306) >> 1 >> 4 | 15610105) ^ 16777215, (1936162948 << 2 >>> 1 | 371139735) >> 1 ^ 40719814 ^ 956483337, (26 >>> 4 >> 2 >>> 4 | 746461606) ^ 746461495);
                              if (((416281582 >> 3 >> 3 & 3570116) >> 2 ^ 560753) == 0) {
                                    ;
                              }

                              var10005 = var18.getRGB();
                        } else {
                              var18 = new Color;
                              var10007 = 1625925829 >>> 3 << 1 ^ 406481456;
                              var10008 = (1175460948 >>> 4 << 4 >>> 3 | 132023599) ^ 266241967;
                              var10009 = (1998971055 ^ 1300397517) >>> 4 << 3 ^ 179692925 ^ 400960461;
                              int var10010 = 147 >> 1 & 65 ^ 57 ^ 227;
                              if (((2113647714 >>> 4 & 76183209 ^ 73653090) >> 1 ^ -278344632) != 0) {
                                    ;
                              }

                              var18.<init>(var10007, var10008, var10009, var10010);
                              var10005 = var18.getRGB();
                        }
                  } else if (this.mod.isEnabled()) {
                        var18 = new Color;
                        if ((962153529 >> 4 << 1 ^ 5544118 ^ 125808944) == 0) {
                              ;
                        }

                        var18.<init>((1679788152 ^ 793152486) & 63097820 ^ 54526876, (555907075 | 460497627) ^ 997391067, (267345026 | 40012645) >>> 2 >>> 3 ^ 8355583, ((50 ^ 11 ^ 9) >> 4 | 1) ^ 126);
                        var10005 = var18.getRGB();
                  } else {
                        var10005 = (new Color((1793587961 ^ 893943630) & 331174199 ^ 330105143, 176195600 >> 1 & 38704005 ^ 4194304, (164240922 ^ 18056829) << 3 ^ 1187824440, (54 << 1 ^ 38 ^ 11) << 4 ^ 1133)).getRGB();
                  }

                  Hud.drawRoundedRect(var4, var9, var10002, var12, 8.0D, var10005);
            }

            GL11.glPushMatrix();
            GL11.glScalef(0.5F, 0.5F, 0.5F);
            int var10;
            float var11;
            int var14;
            int var10004;
            if (this.inUp >= ((12 << 2 & 38 ^ 28 ^ 47) << 2 ^ 66)) {
                  if (!"Some babies were dropped on their heads but you were clearly thrown at a wall.".equals("stringer is a good obfuscator")) {
                        ;
                  }

                  var10000 = Hud.renderer;
                  if ((((803488000 ^ 406067358) << 1 | 312378257) ^ 959238641) != 0) {
                        ;
                  }

                  Module var13 = this.mod;
                  if ((((2029699121 ^ 1558321679) & 468663850 ^ 139558) << 4 >>> 3 ^ 1340952) == 0) {
                        ;
                  }

                  var10001 = var13.getName();
                  var10 = (this.parent.getX() + ((0 << 3 ^ 1089598369) << 4 ^ 234930951 ^ 18872597)) * (((0 & 1679561271) >> 3 ^ 411299833) >> 2 ^ 102824956) + this.parent.getWidth();
                  var14 = Hud.renderer.getStringWidth(this.mod.getName());
                  var10004 = (0 | 1250646702) >>> 2 << 1 ^ 625323348;
                  if (((2057037878 >> 2 << 3 ^ 1092286392 | 938715052 | 268058752) ^ 493317785) != 0) {
                        ;
                  }

                  var11 = (float)(var10 - var14 / var10004) - 5.65F;
                  var10003 = (float)((this.parent.getY() + this.offset + ((1 << 4 | 11) << 2 ^ 110)) * (((0 | 982826748) ^ 17680982 | 890499016 | 695019155) << 2 ^ -18));
                  if (((1112796090 ^ 431016003 ^ 357588788) >>> 1 ^ 660192870) == 0) {
                        ;
                  }

                  --var10003;
                  Module var15 = this.mod;
                  if ((1175640249 << 4 >> 4 << 2 ^ 407593700) == 0) {
                        ;
                  }

                  if (var15.isEnabled()) {
                        if (!"please get a girlfriend and stop cracking plugins".equals("idiot")) {
                              ;
                        }

                        ClickGui var16 = Main.clickGui;
                        var10004 = ClickGui.color;
                  } else {
                        if (((256865939 >> 1 | 124112885) >>> 4 << 2 ^ 524511838) != 0) {
                              ;
                        }

                        var10004 = (new Color((238 | 131) >> 3 >>> 4 ^ 254, (34 | 19) >> 1 << 3 ^ 55, ((126 ^ 40) & 12) >>> 1 & 1 ^ 255, (97 | 88) & 48 ^ 207)).getRGB();
                  }

                  var10000.drawString(var10001, var11, var10003, var10004, (boolean)((0 ^ 2075754038 | 564151617) ^ 2075754358));
            }

            boolean var6 = this.mod.isToggled();
            if (((450725230 | 323998448 | 277858653) >> 2 ^ 2064671106) != 0) {
                  ;
            }

            if (var6 && this.inUp >= (3 << 2 >> 1 >> 2 ^ 15)) {
                  if (!"nefariousMoment".equals("you're dogshit")) {
                        ;
                  }

                  var4 = (double)((this.parent.getX() + (0 << 3 >>> 4 ^ 2)) * ((0 >> 2 ^ 1969985894) >>> 3 ^ 138682902 ^ 115953976) - (3 << 1 << 2 ^ 28));
                  var9 = (double)((this.parent.getY() + this.offset + (1 >>> 3 >> 2 ^ 1331800088 ^ 1331800090)) * ((1 << 1 | 0) << 4 ^ 18 ^ 48) - ((0 & 1958738675) << 4 ^ 1635833895 ^ 1635833893));
                  ClickGui var19 = Main.clickGui;
                  if (!"please get a girlfriend and stop cracking plugins".equals("stringer is a good obfuscator")) {
                        ;
                  }

                  Hud.drawRoundedRect(var4, var9, 4.0D, 26.0D, 6.0D, ClickGui.color);
            }

            if (!"please dont crack my plugin".equals("nefariousMoment")) {
                  ;
            }

            int var8;
            if (this.subcomponents.size() > ((0 & 442039186) >>> 2 & 1616711505 ^ 1)) {
                  var8 = this.inUp;
                  var7 = (6 & 1 ^ 252715870) >>> 2 ^ 63178969;
                  if (!"stringer is a good obfuscator".equals("minecraft")) {
                        ;
                  }

                  if (var8 >= var7) {
                        var10000 = Hud.renderer;
                        if (this.open) {
                              var10001 = ">";
                        } else {
                              var10001 = "^";
                              if (!"stringer is a good obfuscator".equals("please take a shower")) {
                                    ;
                              }
                        }

                        var10 = this.parent.getX() + this.parent.getWidth();
                        var14 = (5 | 2) << 1 >> 3 ^ 0 ^ 11;
                        if (!"yo mama name maurice".equals("buy a domain and everything else you need at namecheap.com")) {
                              ;
                        }

                        var11 = (float)((var10 - var14) * ((1 << 3 | 4) >>> 1 >> 2 >>> 2 ^ 2)) - 1.0F;
                        if (!"Your family tree must be a cactus because everyone on it is a prick.".equals("please go outside")) {
                              ;
                        }

                        var14 = this.parent.getY();
                        var10004 = this.offset;
                        if (!"Your family tree must be a cactus because everyone on it is a prick.".equals("intentMoment")) {
                              ;
                        }

                        var14 += var10004;
                        if (!"please dont crack my plugin".equals("please take a shower")) {
                              ;
                        }

                        var10003 = (float)((var14 + ((0 & 1124381809 ^ 577895261 ^ 181095211) >> 2 ^ 170830879)) * ((1 | 0) >>> 3 & 684326094 ^ 2)) - 2.0F;
                        if (((((14839155 | 10457374) ^ 3598781) & 10177128) >> 4 ^ 34696730) != 0) {
                              ;
                        }

                        Color var17 = new Color;
                        int var10006 = (216 & 72) >>> 3 >> 4 ^ 255;
                        var10007 = (1645961654 << 4 ^ 547020523 | 6784106) << 2 >> 3 ^ 12051189;
                        var10008 = ((239158273 >> 3 | 11172551) ^ 15127536) >> 4 ^ 1100435;
                        var10009 = ((201 >>> 4 | 4) >> 4 >>> 4 | 42563497) ^ 42563414;
                        if (!"Your family tree must be a cactus because everyone on it is a prick.".equals("stop skidding")) {
                              ;
                        }

                        var17.<init>(var10006, var10007, var10008, var10009);
                        var10000.drawString(var10001, var11, var10003, var17.getRGB(), (boolean)(((0 | 1564452379) >>> 2 & 187754814) << 3 ^ 403130417));
                  }
            }

            GL11.glPopMatrix();
            var6 = this.open;
            if (!"you're dogshit".equals("minecraft")) {
                  ;
            }

            if (var6 && !this.subcomponents.isEmpty()) {
                  Iterator var2 = this.subcomponents.iterator();

                  while(var2.hasNext()) {
                        Component var3 = (Component)var2.next();
                        var3.renderComponent();
                  }

                  if ((1558342742 >>> 2 & 168518998 ^ -1760758947) != 0) {
                        ;
                  }

                  if (((327681 | 272101) << 4 ^ -941855808) != 0) {
                        ;
                  }

                  var8 = this.parent.getX() + ((1 & 0 | 1899457491) >> 1 ^ 949728747);
                  if (((173133067 | 124248529) >>> 2 ^ 64879478) == 0) {
                        ;
                  }

                  var4 = (double)var8;
                  var9 = (double)(this.parent.getY() + this.offset + (10 << 3 & 4 & 1342380418 ^ 14));
                  var12 = (double)(this.subcomponents.size() * ((10 >> 1 | 4) ^ 2 ^ 9));
                  if ((992721237 >> 2 >> 2 & 35147884 & 18726509 ^ 1086730221) != 0) {
                        ;
                  }

                  Hud.drawRoundedRect(var4, var9, 3.0D, var12, 4.0D, ClickGui.color);
            }

            if ((((246721221 ^ 28788717) >>> 4 | 1780350) ^ -1535053322) != 0) {
                  ;
            }

      }

      public ArrayList getModes() {
            ArrayList var1 = new ArrayList();
            if (((485776730 ^ 148391829) >>> 1 >> 1 ^ -1303998104) != 0) {
                  ;
            }

            Iterator var2 = this.mod.settings.iterator();

            while(var2.hasNext()) {
                  Setting var3 = (Setting)var2.next();
                  if (var3 instanceof ModeSetting) {
                        ModeSetting var4 = (ModeSetting)var3;
                        var1.add(var4);
                  }
            }

            if (((1823520483 ^ 1501638889) & 297795019 & 130486835 & 8889401 ^ -1297760555) != 0) {
                  ;
            }

            if ((((345566519 << 2 & 1045611002) << 1 | 28806879) ^ 540735825 ^ -295781064) != 0) {
                  ;
            }

            return var1;
      }

      public void updateComponent(int var1, int var2) {
            if (((165159633 | 155806452) >>> 1 ^ 991979360) != 0) {
                  ;
            }

            this.isHovered = this.isMouseOnButton(var1, var2);
            if (!this.subcomponents.isEmpty()) {
                  Iterator var3 = this.subcomponents.iterator();

                  while(true) {
                        if (!"please take a shower".equals("please get a girlfriend and stop cracking plugins")) {
                              ;
                        }

                        if (!var3.hasNext()) {
                              break;
                        }

                        Component var4 = (Component)var3.next();
                        var4.updateComponent(var1, var2);
                  }
            }

      }

      public void mouseClicked(int var1, int var2, int var3) {
            boolean var10000 = this.isMouseOnButton(var1, var2);
            if ((665250963 >>> 3 >>> 2 ^ -2118264882) != 0) {
                  ;
            }

            if (var10000) {
                  if (((536877986 >>> 3 | 35617391) ^ 2035471970) != 0) {
                        ;
                  }

                  if (var3 == 0) {
                        this.mod.toggle();
                  }
            }

            if ((548476203 >> 3 >> 1 >> 3 >> 4 ^ -735809370) != 0) {
                  ;
            }

            if (this.isMouseOnButton(var1, var2) && var3 == ((0 >>> 2 | 1234048202) << 1 >>> 2 ^ 617024100)) {
                  boolean var10001 = this.open;
                  if (!"Your family tree must be a cactus because everyone on it is a prick.".equals("minecraft")) {
                        ;
                  }

                  this.open = (boolean)(!var10001 ? (0 ^ 649659906 | 565227294 | 438897383) >> 1 ^ 534566910 : 1643732314 << 3 >>> 1 ^ 132478312);
                  if ((1376559372 << 3 >>> 1 ^ 1211270192) == 0) {
                        ;
                  }

                  this.parent.refresh();
            }

            Component var5;
            for(Iterator var4 = this.subcomponents.iterator(); var4.hasNext(); var5.mouseClicked(var1, var2, var3)) {
                  var5 = (Component)var4.next();
                  if (!"you probably spell youre as your".equals("please go outside")) {
                        ;
                  }
            }

      }

      public ArrayList getValues() {
            ArrayList var1 = new ArrayList();
            List var10000 = this.mod.settings;
            if (((842357040 ^ 476183629) << 3 & 430528256 ^ 278987520) == 0) {
                  ;
            }

            Iterator var2 = var10000.iterator();

            while(var2.hasNext()) {
                  Setting var3 = (Setting)var2.next();
                  if (var3 instanceof NumberSetting) {
                        NumberSetting var5 = (NumberSetting)var3;
                        if (((1224232938 ^ 509048671 | 1042636116 | 1771111282) ^ 2143256567) == 0) {
                              ;
                        }

                        NumberSetting var4 = var5;
                        if (((1021952056 | 97839439 | 7894895) >>> 2 >>> 2 ^ 65003511) == 0) {
                              ;
                        }

                        var1.add(var4);
                        if (((303399936 << 3 | 1936180341 | 692630296) ^ 1665012473) != 0) {
                              ;
                        }
                  }
            }

            return var1;
      }

      public boolean isMouseOnButton(int var1, int var2) {
            if (var1 > this.parent.getX() && var1 < this.parent.getX() + this.parent.getWidth() && var2 > this.parent.getY() + this.offset) {
                  int var10001 = this.parent.getY() + (2 >>> 3 << 2 << 3 >> 1 ^ 424312645 ^ 424312651);
                  if (((1725794091 ^ 287510840 | 1129898394) ^ -408709075) != 0) {
                        ;
                  }

                  if (var2 < var10001 + this.offset) {
                        return (boolean)((0 << 3 >> 3 | 1597553518) ^ 1597553519);
                  }
            }

            return (boolean)(843190272 << 4 >> 3 << 4 >>> 4 ^ 75767808);
      }

      public Button(Module var1, Frame var2, int var3) {
            if (!"stringer is a good obfuscator".equals("stringer is a good obfuscator")) {
                  ;
            }

            this.mod = var1;
            this.parent = var2;
            if ((((784199495 | 471937129) << 2 | 1203152185) ^ -524867) == 0) {
                  ;
            }

            int var10001 = var3 + (((0 | 663350282) & 116569375 ^ 76889304 | 23235396) << 3 ^ 465362609);
            if ((2428033 >> 1 ^ -632856463) != 0) {
                  ;
            }

            this.offset = var10001;
            this.subcomponents = new ArrayList();
            if (!"idiot".equals("Some babies were dropped on their heads but you were clearly thrown at a wall.")) {
                  ;
            }

            this.open = (boolean)((1551660234 >>> 1 ^ 12797973 | 458403936) << 3 ^ -27776);
            this.height = (1 >> 4 >> 3 | 1363535374) >> 1 ^ 681767689;
            int var4 = var3 + (7 >>> 3 & 1586634139 ^ 1199864254 ^ 1199864240);
            Iterator var5;
            if (!this.getModes().isEmpty()) {
                  var5 = this.getModes().iterator();

                  while(var5.hasNext()) {
                        ModeSetting var6 = (ModeSetting)var5.next();
                        this.subcomponents.add(new ModeButton(var6, this, var1, var4));
                  }

                  if ((203467682 >> 3 ^ 6587348 ^ 31495840) == 0) {
                        ;
                  }

                  var4 += 14;
            }

            if (!this.getValues().isEmpty()) {
                  var5 = this.getValues().iterator();

                  while(true) {
                        if ((1938365772 >>> 4 >> 4 << 2 ^ 25515614 ^ 4944426) != 0) {
                        }

                        if (!"Your family tree must be a cactus because everyone on it is a prick.".equals("stringer is a good obfuscator")) {
                              ;
                        }

                        if (!var5.hasNext()) {
                              break;
                        }

                        NumberSetting var8 = (NumberSetting)var5.next();
                        Slider var7 = new Slider(var8, this, var4);
                        this.subcomponents.add(var7);
                        var4 += 14;
                  }
            }

            if (!this.getOptions().isEmpty()) {
                  var5 = this.getOptions().iterator();

                  while(var5.hasNext()) {
                        BooleanSetting var9 = (BooleanSetting)var5.next();
                        Checkbox var10000 = new Checkbox;
                        if (!"i hope you catch fire ngl".equals("stringer is a good obfuscator")) {
                              ;
                        }

                        var10000.<init>(var9, this, var4);
                        Checkbox var10 = var10000;
                        if (((1363797366 << 1 & 1416238170) >>> 2 ^ '耒') == 0) {
                              ;
                        }

                        this.subcomponents.add(var10);
                        var4 += 14;
                        if ((899865060 >>> 3 >> 4 >> 4 ^ -1608352869) != 0) {
                              ;
                        }
                  }
            }

            this.subcomponents.add(new Keybind(this, var4));
      }

      public void setOff(int var1) {
            this.offset = var1;
            int var2 = this.offset + ((8 >> 4 | 1167714178 | 202378826) >> 1 << 1 ^ 1301934020);
            Iterator var10000 = this.subcomponents.iterator();
            if (((1535094415 << 2 >> 2 ^ 267937589) << 4 ^ 1216125856) == 0) {
                  ;
            }

            Iterator var3 = var10000;

            while(var3.hasNext()) {
                  Component var4 = (Component)var3.next();
                  if (!"yo mama name maurice".equals("ape covered in human flesh")) {
                        ;
                  }

                  var4.setOff(var2);
                  var2 += 14;
                  if (((((311756377 | 97744373) & 312143591 ^ 125599690) & 125947757) << 3 ^ 433075760) != 0) {
                        ;
                  }
            }

      }

      public int getHeight() {
            return this.open ? ((13 ^ 9 | 3) >>> 1 ^ 13) * (this.subcomponents.size() + ((0 >> 4 | 1146985053) >> 1 ^ 573492527)) : (7 >>> 4 | 1022155733) ^ 1022155739;
      }
}
