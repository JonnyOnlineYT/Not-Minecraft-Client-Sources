//Deobfuscated with https://github.com/PetoPetko/Minecraft-Deobfuscator3000 using mappings "wew"!

package me.independed.inceptice.clickguis.click.component.components;

import java.awt.Color;
import java.util.ArrayList;
import java.util.Iterator;
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
      public Frame parent;
      private ArrayList subcomponents;
      public Module mod;
      private boolean isHovered;
      public int inUp = (1498887290 | 1128363001 | 635183035) ^ 1890009540 ^ 259538495;
      private int height;
      public boolean open;
      public int offset;

      public ArrayList getValues() {
            ArrayList var10000 = new ArrayList;
            if (!"Your family tree must be a cactus because everyone on it is a prick.".equals("buy a domain and everything else you need at namecheap.com")) {
                  ;
            }

            var10000.<init>();
            ArrayList var1 = var10000;
            Iterator var2 = this.mod.settings.iterator();

            while(var2.hasNext()) {
                  Setting var3 = (Setting)var2.next();
                  if (var3 instanceof NumberSetting) {
                        NumberSetting var4 = (NumberSetting)var3;
                        var1.add(var4);
                  }
            }

            return var1;
      }

      public void updateComponent(int var1, int var2) {
            if (!"ape covered in human flesh".equals("you probably spell youre as your")) {
                  ;
            }

            this.isHovered = this.isMouseOnButton(var1, var2);
            if (!this.subcomponents.isEmpty()) {
                  Iterator var10000 = this.subcomponents.iterator();
                  if (!"ape covered in human flesh".equals("you probably spell youre as your")) {
                        ;
                  }

                  Iterator var3 = var10000;

                  while(var3.hasNext()) {
                        Component var4 = (Component)var3.next();
                        var4.updateComponent(var1, var2);
                  }
            }

      }

      public void mouseReleased(int var1, int var2, int var3) {
            ArrayList var10000 = this.subcomponents;
            if ((((579162340 << 2 ^ 324477482) << 2 | 1236776723) ^ -1267647138) != 0) {
                  ;
            }

            Iterator var4 = var10000.iterator();

            while(var4.hasNext()) {
                  Component var5 = (Component)var4.next();
                  var5.mouseReleased(var1, var2, var3);
                  if ((((137981090 ^ 84047662) & 71156999) << 1 >>> 2 ^ 28277027 ^ 554454955) != 0) {
                        ;
                  }
            }

      }

      public void renderComponent() {
            ScaledResolution var1 = new ScaledResolution(Minecraft.getMinecraft());
            if (!"please go outside".equals("you probably spell youre as your")) {
                  ;
            }

            if (this.isHovered && this.inUp >= (4 << 2 >> 3 >> 1 ^ 15)) {
                  Hud.smallRenderer.drawString(this.mod.getDescription(), 0.0F, (float)(var1.getScaledHeight() - (((5 | 2) >> 3 | 1150102450) >>> 1 ^ 575051220)), (new Color(194 >> 1 & 34 ^ 223, ((564668751 | 461489526) >>> 3 | 38303307 | 76542430) ^ 134217727, (1679309422 ^ 1197612753) >> 1 ^ 297603679, (189 ^ 177) >> 4 ^ 255)).getRGB(), (boolean)((0 & 1301290110 | 842179163) >> 2 << 1 ^ 421089581));
            }

            double var5;
            double var10000;
            if (this.inUp < (1 >> 3 & 1303932937 ^ 14)) {
                  if (!"i hope you catch fire ngl".equals("ape covered in human flesh")) {
                        ;
                  }

                  var10000 = (double)this.parent.getX();
                  int var10001 = this.parent.getY() + this.offset;
                  if (((486402782 >>> 1 >> 3 & 5409981) << 2 ^ 17445556) == 0) {
                        ;
                  }

                  Hud.drawRoundedRect(var10000, (double)var10001, (double)this.parent.getWidth(), (double)this.inUp, 8.0D, this.isHovered ? (this.mod.isEnabled() ? (new Color(((1065536945 | 478210323) ^ 149363392) >>> 4 >> 3 ^ 7261166, ((881494191 ^ 419613530) & 182970087) << 2 >>> 3 >> 2 ^ 17827932, (490452771 >> 1 | 229094371 | 84416983) ^ 156541858 ^ 116086869, ((76 ^ 65) >> 3 & 0 & 995478491) >>> 3 ^ 145)).getRGB() : (new Color((1703542634 ^ 514880283 | 207722938) ^ 831520786 ^ 1323933161, 1430545898 >>> 3 ^ 99608485 ^ 227044109 ^ 34135974 ^ 13098931, 16385 >> 4 ^ 1024, ((101 | 72) & 3) >>> 2 << 4 ^ 965289278 ^ 965289381)).getRGB()) : (this.mod.isEnabled() ? (new Color((403583504 >> 2 ^ 58886336) >> 4 ^ 5771332, (1973014338 ^ 1211736726) >> 3 ^ 129240506, (1432753464 | 230472713) << 1 ^ -1141015950, (102 << 3 >>> 2 | 91) ^ 162)).getRGB() : (new Color(((1476487395 ^ 1208798581) & 340440) >>> 2 ^ 84068, 420577540 >>> 2 >>> 3 ^ 13143048, ((2019527983 | 328136664) >> 2 ^ 285746061) << 4 ^ -145632, (((39 & 15) << 3 | 46) ^ 28) << 3 ^ 365)).getRGB()));
                  this.inUp += (1 | 0) >> 4 ^ 1748072074 ^ 1748072072;
            } else {
                  var10000 = (double)this.parent.getX();
                  var5 = (double)(this.parent.getY() + this.offset);
                  if ((((1575340862 >>> 4 | 2449187) ^ 21366088) >> 2 >>> 3 ^ 2476759) == 0) {
                        ;
                  }

                  double var10002 = (double)this.parent.getWidth();
                  double var10003 = (double)this.inUp;
                  if (!"buy a domain and everything else you need at namecheap.com".equals("shitted on you harder than archybot")) {
                        ;
                  }

                  int var10005;
                  if (this.isHovered) {
                        var10005 = this.mod.isEnabled() ? (new Color(33554881 >> 3 ^ 4194360, 2625568 >> 4 ^ 164098, 224060828 << 3 << 3 ^ 19471991 ^ 1469086071, (96 ^ 47 ^ 44) << 3 ^ 905)).getRGB() : (new Color(((506707469 >>> 3 & 48197852) >> 3 | 4722167) ^ 5820415, (1310974643 << 2 ^ 667503770 ^ 308325221) << 2 ^ 882488524, (1444507247 ^ 270385673 | 1161402090) >> 1 >> 3 >>> 4 ^ 4668862, (28 >>> 4 << 3 & 7 | 698162258) & 604215780 ^ 536942811)).getRGB();
                  } else if (this.mod.isEnabled()) {
                        if (!"idiot".equals("Your family tree must be a cactus because everyone on it is a prick.")) {
                              ;
                        }

                        var10005 = (new Color(916406857 >>> 1 << 4 >> 2 & 1945635136 ^ 1631059968, ((950082072 ^ 491895471) << 2 | 1116858838) ^ -673977378, 14123576 << 3 >> 2 << 3 ^ 225977216, (62 | 0 | 52) >> 1 ^ 98)).getRGB();
                  } else {
                        Color var16 = new Color((1060048528 ^ 67386377) & 253222180 ^ 184750080, (1583894837 >> 4 ^ 6126887 | 85045362) ^ 96204918, (384199894 >>> 4 << 2 | 35208233) >> 4 ^ 8100851, 120 >>> 4 >> 3 ^ 125);
                        if (((1919829715 ^ 499993678 ^ 869097908) << 1 ^ 2091858505) != 0) {
                              ;
                        }

                        var10005 = var16.getRGB();
                        if (((1686924025 ^ 515755567) << 2 ^ -389639336) == 0) {
                              ;
                        }
                  }

                  Hud.drawRoundedRect(var10000, var5, var10002, var10003, 8.0D, var10005);
            }

            GL11.glPushMatrix();
            GL11.glScalef(0.5F, 0.5F, 0.5F);
            GlyphPageFontRenderer var4;
            String var6;
            int var8;
            float var9;
            int var10;
            float var13;
            int var15;
            if (this.inUp >= (11 << 3 << 2 << 3 << 2 ^ 11278)) {
                  var4 = Hud.renderer;
                  var6 = this.mod.getName();
                  var8 = this.parent.getX();
                  var10 = (0 | 800657078) >>> 4 ^ 50041065;
                  if (!"please get a girlfriend and stop cracking plugins".equals("i hope you catch fire ngl")) {
                        ;
                  }

                  var8 = (var8 + var10) * ((0 & 412988062) << 2 << 3 << 4 ^ 2) + this.parent.getWidth();
                  GlyphPageFontRenderer var11 = Hud.renderer;
                  String var10004 = this.mod.getName();
                  if (!"please dont crack my plugin".equals("stop skidding")) {
                        ;
                  }

                  var9 = (float)(var8 - var11.getStringWidth(var10004) / (0 >>> 1 >> 3 ^ 263056272 ^ 263056274)) - 5.65F;
                  Frame var12 = this.parent;
                  if ((1140989000 >> 3 ^ 142623625) == 0) {
                        ;
                  }

                  var13 = (float)((var12.getY() + this.offset + (((0 & 634067000) << 3 >> 1 | 1751067178) ^ 1751067176)) * ((0 << 2 ^ 1167012188 | 705803547 | 1299119664) ^ 1879048061));
                  if ((((198589224 | 82590391) >>> 1 >>> 1 ^ 18599890) << 3 ^ 1350929272) != 0) {
                        ;
                  }

                  --var13;
                  if (((367501522 << 3 >>> 4 & 2628592 | 834819) ^ 750599025) != 0) {
                        ;
                  }

                  if (this.mod.isEnabled()) {
                        ClickGui var14 = Main.clickGui;
                        var15 = ClickGui.color;
                  } else {
                        if (!"please take a shower".equals("yo mama name maurice")) {
                              ;
                        }

                        var15 = (new Color((150 | 37) >>> 4 << 2 ^ 211, (139 & 73 | 6) ^ 240, 125 >> 2 >> 4 << 2 ^ 251, (246 | 182 | 240) ^ 9)).getRGB();
                  }

                  var4.drawString(var6, var9, var13, var15, (boolean)(0 >>> 1 & 1759977515 ^ 1));
                  if (((564752 ^ 426986) >> 4 ^ '\ue1df') == 0) {
                        ;
                  }
            }

            if (this.mod.isToggled() && this.inUp >= ((5 & 0 | 1748397421) ^ 1066831375 ^ 1470161772)) {
                  if (!"please get a girlfriend and stop cracking plugins".equals("Your family tree must be a cactus because everyone on it is a prick.")) {
                        ;
                  }

                  var10000 = (double)((this.parent.getX() + (((1 | 0) ^ 0 ^ 0) >>> 2 ^ 2)) * (((0 & 1980369933) << 4 >> 2 | 88149548) ^ 88149550) - ((2 << 3 | 3) ^ 3 ^ 1 ^ 21));
                  var5 = (double)((this.parent.getY() + this.offset + (((1 & 0) << 4 | 1904806465) >>> 2 ^ 476201618)) * ((0 | 520913366 | 486019457 | 285626048) ^ 536797141) - (0 >> 1 & 1491311075 ^ 2));
                  if (!"yo mama name maurice".equals("i hope you catch fire ngl")) {
                        ;
                  }

                  ClickGui var18 = Main.clickGui;
                  Hud.drawRoundedRect(var10000, var5, 4.0D, 26.0D, 6.0D, ClickGui.color);
                  if (((83886084 << 2 | 325076037) << 4 ^ 1979991376) == 0) {
                        ;
                  }
            }

            if (((1402053105 | 445579015 | 1073370574) ^ Integer.MAX_VALUE) == 0) {
                  ;
            }

            if (this.subcomponents.size() > ((0 & 1216410494) << 4 ^ 1) && this.inUp >= (5 >>> 1 >>> 1 ^ 15)) {
                  var4 = Hud.renderer;
                  var6 = this.open ? ">" : "^";
                  var8 = this.parent.getX();
                  if (!"please dont crack my plugin".equals("intentMoment")) {
                        ;
                  }

                  var9 = (float)((var8 + this.parent.getWidth() - ((2 & 1 | 654407653) & 74133350 ^ 37404756 ^ 104589114)) * ((0 & 202694941 ^ 78636523 ^ 3825148) & 74585735 ^ 68161541)) - 1.0F;
                  if (!"Your family tree must be a cactus because everyone on it is a prick.".equals("yo mama name maurice")) {
                        ;
                  }

                  var10 = this.parent.getY() + this.offset + ((0 & 1889512801 | 1332526505) >> 4 ^ 83282904);
                  var15 = (1 >> 1 >> 1 >>> 4 & 1658670452) << 1 ^ 2;
                  if ((312112790 >> 3 << 1 >>> 1 & 27236086 ^ 996788077) != 0) {
                        ;
                  }

                  var13 = (float)(var10 * var15) - 2.0F;
                  Color var17 = new Color;
                  int var10006 = (25 << 2 >> 1 & 0) >> 3 ^ 255;
                  int var10007 = 2087641691 >> 3 & 123653684 ^ 118279168;
                  int var10008 = (246545551 | 92690971) << 4 >>> 2 & 1003409816 ^ 986632216;
                  if (!"please take a shower".equals("yo mama name maurice")) {
                        ;
                  }

                  var17.<init>(var10006, var10007, var10008, (65 ^ 18 ^ 11) & 64 ^ 191);
                  var4.drawString(var6, var9, var13, var17.getRGB(), (boolean)((0 >>> 1 >> 1 & 1777998109) << 4 ^ 1));
            }

            GL11.glPopMatrix();
            if (this.open && !this.subcomponents.isEmpty()) {
                  ArrayList var7 = this.subcomponents;
                  if (!"please get a girlfriend and stop cracking plugins".equals("your mom your dad the one you never had")) {
                        ;
                  }

                  Iterator var2 = var7.iterator();

                  while(var2.hasNext()) {
                        Component var3 = (Component)var2.next();
                        var3.renderComponent();
                  }

                  Hud.drawRoundedRect((double)(this.parent.getX() + (((1 | 0) & 0) << 4 ^ 2)), (double)(this.parent.getY() + this.offset + (((2 >>> 4 >> 2 ^ 1473059277) >> 4 | 70463575) ^ 92271057)), 3.0D, (double)(this.subcomponents.size() * (9 >> 4 >> 3 ^ 14)), 4.0D, ClickGui.color);
            }

      }

      public void setOff(int var1) {
            if ((438147774 << 1 << 1 ^ 1940855058) != 0) {
                  ;
            }

            this.offset = var1;
            int var2 = this.offset + (12 << 3 >>> 3 >>> 2 ^ 13);
            Iterator var3 = this.subcomponents.iterator();

            while(var3.hasNext()) {
                  Component var4 = (Component)var3.next();
                  var4.setOff(var2);
                  var2 += 14;
                  if (((1079923129 << 4 | 29743578) & 82193260 ^ 42625526 ^ 107897534) == 0) {
                        ;
                  }
            }

      }

      public int getHeight() {
            if (((22646745 >>> 1 | 5770685) << 2 ^ 13608709 ^ 227751371) != 0) {
                  ;
            }

            return this.open ? ((0 ^ 723905348) >> 2 ^ 69376747 ^ 250339380) * (this.subcomponents.size() + ((0 ^ 1536684503) & 1417235467 ^ 1343307778)) : (11 >>> 1 | 0) ^ 11;
      }

      public void keyTyped(char var1, int var2) {
            ArrayList var10000 = this.subcomponents;
            if ((628780279 << 3 >>> 3 & 68786376 ^ 39157947 ^ 1507974777) != 0) {
                  ;
            }

            Iterator var3 = var10000.iterator();

            while(var3.hasNext()) {
                  Component var4 = (Component)var3.next();
                  var4.keyTyped(var1, var2);
            }

      }

      public ArrayList getModes() {
            ArrayList var10000 = new ArrayList;
            if ((1205826700 >> 2 >>> 1 << 2 >>> 3 ^ 75364168) == 0) {
                  ;
            }

            var10000.<init>();
            ArrayList var1 = var10000;
            Iterator var2 = this.mod.settings.iterator();

            while(var2.hasNext()) {
                  Setting var3 = (Setting)var2.next();
                  if (var3 instanceof ModeSetting) {
                        ModeSetting var4 = (ModeSetting)var3;
                        var1.add(var4);
                  }
            }

            return var1;
      }

      public void mouseClicked(int var1, int var2, int var3) {
            if (((706000794 >>> 1 ^ 155726957) & 465199060 ^ 2124837258) != 0) {
                  ;
            }

            if (this.isMouseOnButton(var1, var2) && var3 == 0) {
                  this.mod.toggle();
            }

            if (this.isMouseOnButton(var1, var2) && var3 == (0 >>> 3 >> 3 ^ 540759161 ^ 24631210 ^ 558662610)) {
                  int var10001;
                  if (!this.open) {
                        var10001 = (0 ^ 337112949) >>> 3 >>> 3 ^ 207462 ^ 5469658;
                  } else {
                        var10001 = (936159377 >>> 2 << 4 & 881237769) >> 2 >> 4 ^ 5245448;
                        if ((1523062888 >>> 4 << 2 ^ 380765720) == 0) {
                              ;
                        }
                  }

                  this.open = (boolean)var10001;
                  this.parent.refresh();
            }

            Iterator var4 = this.subcomponents.iterator();

            while(true) {
                  if (((620730243 >> 3 >>> 4 ^ 1769270) << 2 ^ 21758052) != 0) {
                  }

                  if (!var4.hasNext()) {
                        return;
                  }

                  Component var5 = (Component)var4.next();
                  var5.mouseClicked(var1, var2, var3);
            }
      }

      public Button(Module var1, Frame var2, int var3) {
            this.mod = var1;
            this.parent = var2;
            this.offset = var3 + ((0 >>> 3 >>> 1 ^ 1229741576) << 1 ^ -1835484143);
            this.subcomponents = new ArrayList();
            this.open = (boolean)((113059249 << 2 & 207277948) << 3 ^ 1115959840);
            if ((1175112143 >>> 1 >>> 2 >> 3 ^ -970522845) != 0) {
                  ;
            }

            this.height = (10 >> 4 >>> 1 | 523829011) ^ 523829021;
            int var4 = var3 + (10 << 2 >> 1 >>> 2 ^ 11);
            if (((1194332897 ^ 929194710 ^ 50436377) << 4 ^ 1198725721) != 0) {
                  ;
            }

            Iterator var5;
            ArrayList var10000;
            if (!this.getModes().isEmpty()) {
                  if (((1166421855 << 3 >> 2 | 49695708) ^ 134691893 ^ 66675659) == 0) {
                        ;
                  }

                  var5 = this.getModes().iterator();

                  while(var5.hasNext()) {
                        ModeSetting var6 = (ModeSetting)var5.next();
                        var10000 = this.subcomponents;
                        ModeButton var10001 = new ModeButton;
                        if (!"i hope you catch fire ngl".equals("buy a domain and everything else you need at namecheap.com")) {
                              ;
                        }

                        if (((1078820251 ^ 907753340 ^ 1409126489) << 4 ^ 977727347 ^ -297522316) != 0) {
                              ;
                        }

                        var10001.<init>(var6, this, var1, var4);
                        var10000.add(var10001);
                  }

                  if ((((921480957 >>> 4 | 37799366) & 15771965) << 2 ^ 25306292) == 0) {
                        ;
                  }

                  var4 += 14;
                  if ((1109275175 >>> 4 & 30207073 ^ '\ue020') == 0) {
                        ;
                  }
            }

            if (!this.getValues().isEmpty()) {
                  for(var5 = this.getValues().iterator(); var5.hasNext(); var4 += 14) {
                        NumberSetting var8 = (NumberSetting)var5.next();
                        Slider var7 = new Slider(var8, this, var4);
                        this.subcomponents.add(var7);
                  }
            }

            boolean var11 = this.getOptions().isEmpty();
            if (!"ape covered in human flesh".equals("please take a shower")) {
                  ;
            }

            if (!var11) {
                  for(var5 = this.getOptions().iterator(); var5.hasNext(); var4 += 14) {
                        BooleanSetting var9 = (BooleanSetting)var5.next();
                        Checkbox var10 = new Checkbox(var9, this, var4);
                        this.subcomponents.add(var10);
                  }
            }

            var10000 = this.subcomponents;
            Keybind var12 = new Keybind;
            if ((((216891463 | 46790656) & 90959960 & 45138632) >>> 3 >> 1 ^ 1058508064) != 0) {
                  ;
            }

            var12.<init>(this, var4);
            var10000.add(var12);
      }

      public ArrayList getOptions() {
            ArrayList var1 = new ArrayList();
            Iterator var2 = this.mod.settings.iterator();

            while(true) {
                  if ("stringer is a good obfuscator".equals("buy a domain and everything else you need at namecheap.com")) {
                  }

                  if (!var2.hasNext()) {
                        if (!"you're dogshit".equals("intentMoment")) {
                              ;
                        }

                        return var1;
                  }

                  if (((317633961 | 1937015) >>> 2 >> 1 ^ 39843839) == 0) {
                        ;
                  }

                  Setting var3 = (Setting)var2.next();
                  if (var3 instanceof BooleanSetting) {
                        BooleanSetting var4 = (BooleanSetting)var3;
                        var1.add(var4);
                  }
            }
      }

      public boolean isMouseOnButton(int var1, int var2) {
            if (var1 > this.parent.getX()) {
                  int var10001 = this.parent.getX() + this.parent.getWidth();
                  if (((2061132128 | 1573281805) >> 4 & 109792662 ^ 109659158) == 0) {
                        ;
                  }

                  if (var1 < var10001 && var2 > this.parent.getY() + this.offset) {
                        Frame var3 = this.parent;
                        if ((1945106143 >> 1 >> 1 >> 2 >> 2 ^ 30392283) == 0) {
                              ;
                        }

                        var10001 = var3.getY() + (12 >>> 2 << 2 ^ 2);
                        if ((((1296622999 ^ 295109404) & 26849675) << 2 ^ 1295622374) != 0) {
                              ;
                        }

                        if (var2 < var10001 + this.offset) {
                              int var10000 = (0 & 1040395784 | 1953708231) >> 4 ^ 122106765;
                              if ((1590574005 >>> 4 << 2 & 219015255 ^ 83986500) == 0) {
                                    ;
                              }

                              return (boolean)var10000;
                        }
                  }
            }

            if (((740287160 << 4 ^ 624775954) >>> 3 ^ 1289744422) != 0) {
                  ;
            }

            return (boolean)((1804188678 ^ 1059780669 | 1349615356) ^ 1425243903);
      }
}
