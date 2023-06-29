//Deobfuscated with https://github.com/PetoPetko/Minecraft-Deobfuscator3000 using mappings "wew"!

package me.independed.inceptice.modules.hud;

import java.awt.Color;
import java.util.Iterator;
import java.util.List;
import me.independed.inceptice.font.GlyphPageFontRenderer;
import me.independed.inceptice.modules.Module;
import me.independed.inceptice.modules.ModuleManager;
import me.independed.inceptice.settings.BooleanSetting;
import me.independed.inceptice.settings.ModeSetting;
import me.independed.inceptice.settings.NumberSetting;
import me.independed.inceptice.settings.Setting;
import me.independed.inceptice.ui.Hud;
import net.minecraft.client.Minecraft;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.client.event.RenderGameOverlayEvent.ElementType;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.InputEvent.KeyInputEvent;
import org.lwjgl.input.Keyboard;

public class TabGUI extends Module {
      public boolean expanded;
      public NumberSetting greenSet = new NumberSetting("Green", this, 0.0D, 0.0D, 255.0D, 1.0D);
      public NumberSetting redSet = new NumberSetting("Red", this, 0.0D, 0.0D, 255.0D, 1.0D);
      public int currentTab = ((1168221383 << 3 | 166481789) & 240157723) >> 4 ^ 12845121;
      public NumberSetting blueSet;

      public TabGUI() {
            super("TabGUI", "tab gui", 855766758 << 2 & 107471347 ^ 67617168, Module.Category.HUD);
            if (((1560730010 >>> 1 | 34151174) ^ 116064381 ^ 1389637432) != 0) {
                  ;
            }

            this.blueSet = new NumberSetting("Blue", this, 0.0D, 0.0D, 255.0D, 1.0D);
            Setting[] var10001 = new Setting[(2 & 1) >> 1 >> 3 >>> 3 ^ 3];
            var10001[(1074039375 | 46312103) << 2 ^ 186297276] = this.redSet;
            var10001[((0 & 103204132 | 1815197881) ^ 1262333810) >>> 2 >>> 1 ^ 81887992] = this.greenSet;
            var10001[1 >>> 3 << 3 ^ 2] = this.blueSet;
            this.addSettings(var10001);
            this.toggle();
      }

      @SubscribeEvent
      public void onKey(KeyInputEvent var1) {
            if (Minecraft.getMinecraft().world != null) {
                  if (Minecraft.getMinecraft().player != null) {
                        try {
                              if (Keyboard.isCreated()) {
                                    if (((1426543576 << 4 | 833025592) >> 4 >> 1 ^ 1489239950) != 0) {
                                          ;
                                    }

                                    if (Keyboard.getEventKeyState()) {
                                          int var2 = Keyboard.getEventKey();
                                          if (var2 <= 0) {
                                                return;
                                          }

                                          Module.Category var3 = ((Module.Category[])me.independed.inceptice.modules.Module.Category.values())[this.currentTab];
                                          if (!"yo mama name maurice".equals("buy a domain and everything else you need at namecheap.com")) {
                                                ;
                                          }

                                          List var10000 = ModuleManager.getModulesByCategory(var3);
                                          if (((926683068 >>> 1 ^ 394138726) << 4 ^ -838780032) == 0) {
                                                ;
                                          }

                                          List var4 = var10000;
                                          int var10001 = (64 | 61) ^ 80 ^ 229;
                                          if (!"please go outside".equals("stringer is a good obfuscator")) {
                                                ;
                                          }

                                          Module var5;
                                          Setting var6;
                                          Setting var8;
                                          if (var2 == var10001) {
                                                if (this.expanded) {
                                                      if (!"Some babies were dropped on their heads but you were clearly thrown at a wall.".equals("stop skidding")) {
                                                            ;
                                                      }

                                                      if (this.expanded && !var4.isEmpty() && ((Module)var4.get(var3.moduleIndex)).expanded) {
                                                            if (!"Some babies were dropped on their heads but you were clearly thrown at a wall.".equals("Your family tree must be a cactus because everyone on it is a prick.")) {
                                                                  ;
                                                            }

                                                            var5 = (Module)var4.get(var3.moduleIndex);
                                                            var10000 = var5.settings;
                                                            if (!"yo mama name maurice".equals("i hope you catch fire ngl")) {
                                                                  ;
                                                            }

                                                            if (!var10000.isEmpty()) {
                                                                  if (((Setting)var5.settings.get(var5.index)).focused) {
                                                                        var8 = (Setting)((Module)var4.get(var3.moduleIndex)).settings.get(var5.index);
                                                                        if (((1605633 << 3 >> 2 | 2405039) ^ 3519151) == 0) {
                                                                              ;
                                                                        }

                                                                        var6 = var8;
                                                                        if (var6 instanceof NumberSetting) {
                                                                              ((NumberSetting)var6).increment((boolean)((0 << 3 ^ 362735740 | 288528920) ^ 364837501));
                                                                        }

                                                                        if (((73718471 >>> 2 << 1 ^ 17330428) >>> 4 ^ 463478423) != 0) {
                                                                              ;
                                                                        }
                                                                  } else {
                                                                        if (((381180328 >> 4 & 1744086) << 1 ^ 1378596) == 0) {
                                                                              ;
                                                                        }

                                                                        if (var5.index <= 0) {
                                                                              var5.index = var5.settings.size() - ((0 >> 3 ^ 1668440442) >>> 2 >>> 1 ^ 208555054);
                                                                        } else {
                                                                              var5.index -= (0 ^ 1736435743 | 1276502794 | 1303505966) ^ 521286871 ^ 1894636521;
                                                                        }
                                                                  }
                                                            }
                                                      } else {
                                                            if (!"you probably spell youre as your".equals("please go outside")) {
                                                                  ;
                                                            }

                                                            if (var3.moduleIndex <= 0) {
                                                                  var10001 = var4.size() - (0 >> 1 << 4 ^ 1159906894 ^ 1159906895);
                                                                  if ((20079207 >>> 2 >>> 4 >>> 4 ^ 1362894998) != 0) {
                                                                        ;
                                                                  }

                                                                  var3.moduleIndex = var10001;
                                                            } else {
                                                                  var10001 = var3.moduleIndex - ((0 << 2 >>> 4 ^ 564112065 ^ 326044229) << 2 ^ -876445167);
                                                                  if ((((1681202965 | 1591995775) & 936108925) >> 1 ^ 459403710) == 0) {
                                                                        ;
                                                                  }

                                                                  var3.moduleIndex = var10001;
                                                            }
                                                      }
                                                } else if (this.currentTab <= 0) {
                                                      Module.Category[] var10 = (Module.Category[])me.independed.inceptice.modules.Module.Category.values();
                                                      if (((68350028 | 58271042 | 61037328 | 48259165) ^ 1051786651) != 0) {
                                                            ;
                                                      }

                                                      this.currentTab = var10.length - (((0 | 772250748) >> 3 ^ 96008693) & 7434952 ^ 7340617);
                                                } else {
                                                      if (!"shitted on you harder than archybot".equals("please get a girlfriend and stop cracking plugins")) {
                                                            ;
                                                      }

                                                      this.currentTab -= (0 ^ 1473907427 ^ 1133476930 | 159684468) & 468732508 ^ 433078869;
                                                }
                                          }

                                          if ((1334955517 >> 1 << 3 ^ 1044854768) == 0) {
                                                ;
                                          }

                                          if (var2 == ((122 << 1 << 4 & 1096 & 136) >>> 3 ^ 208)) {
                                                if (this.expanded) {
                                                      if (this.expanded && !var4.isEmpty() && ((Module)var4.get(var3.moduleIndex)).expanded) {
                                                            if ((1268197446 >>> 3 >> 4 ^ 8798955 ^ -1890115850) != 0) {
                                                                  ;
                                                            }

                                                            var5 = (Module)var4.get(var3.moduleIndex);
                                                            if (!var5.settings.isEmpty()) {
                                                                  if (((1894524813 | 1269007128 | 584246970 | 994971482) ^ 2080370687) == 0) {
                                                                        ;
                                                                  }

                                                                  if (((Setting)var5.settings.get(var5.index)).focused) {
                                                                        var10000 = ((Module)var4.get(var3.moduleIndex)).settings;
                                                                        if (!"intentMoment".equals("please get a girlfriend and stop cracking plugins")) {
                                                                              ;
                                                                        }

                                                                        if ((1243506352 >>> 4 >> 2 >>> 3 & 796687 ^ 264195) == 0) {
                                                                              ;
                                                                        }

                                                                        var8 = (Setting)var10000.get(var5.index);
                                                                        if (((2121466686 >> 1 << 2 & 861148355 | 260826687) ^ 1070589567) == 0) {
                                                                              ;
                                                                        }

                                                                        var6 = var8;
                                                                        if (!"your mom your dad the one you never had".equals("i hope you catch fire ngl")) {
                                                                              ;
                                                                        }

                                                                        if (var6 instanceof NumberSetting) {
                                                                              NumberSetting var11 = (NumberSetting)var6;
                                                                              var10001 = (1223100939 << 3 & 580149577) >>> 2 ^ 8654866;
                                                                              if ((1415948154 << 3 >> 4 ^ -1722069125) != 0) {
                                                                                    ;
                                                                              }

                                                                              var11.increment((boolean)var10001);
                                                                        }
                                                                  } else if (var5.index >= var5.settings.size() - ((0 << 2 | 1007959570 | 314212500) << 4 ^ -336860831)) {
                                                                        var5.index = 563266174 >> 4 ^ 34384744 ^ 802712 ^ 1685719;
                                                                  } else {
                                                                        var5.index += 0 << 2 << 3 << 4 ^ 1;
                                                                  }
                                                            }

                                                            if (((660637497 >>> 2 & 116124309) >>> 2 ^ 837537518) != 0) {
                                                                  ;
                                                            }
                                                      } else if (var3.moduleIndex >= var4.size() - ((0 >>> 3 & 633189258) >> 4 ^ 1)) {
                                                            if (((2018141310 >>> 4 >> 3 ^ 2008480) << 3 ^ 124881728) == 0) {
                                                                  ;
                                                            }

                                                            var3.moduleIndex = 44047615 << 3 << 2 << 1 << 1 ^ 305316471 ^ 1111277047;
                                                      } else {
                                                            var3.moduleIndex += (0 >> 2 & 237221842) << 4 & 391896363 ^ 1;
                                                      }
                                                } else {
                                                      int var9 = this.currentTab;
                                                      var10001 = ((Module.Category[])me.independed.inceptice.modules.Module.Category.values()).length;
                                                      if ((((8709174 ^ 1290226) << 4 | 5091295 | 116705511) ^ 268304383) == 0) {
                                                            ;
                                                      }

                                                      if (var9 >= var10001 - (((0 ^ 32290163) & 5750815 ^ 2760253) >> 4 ^ 453091)) {
                                                            if ((1074004498 << 1 & 1529842227 ^ 524320) == 0) {
                                                                  ;
                                                            }

                                                            this.currentTab = (1714343355 | 425378992) << 1 >>> 3 >>> 1 ^ 267377079;
                                                      } else {
                                                            this.currentTab += ((0 | 864224027) & 462029854) << 1 ^ 654573621;
                                                      }
                                                }
                                          }

                                          if (var2 == ((10 ^ 9 | 2 | 0) >>> 1 ^ 29) && this.expanded && var4.size() != 0) {
                                                var5 = (Module)var4.get(var3.moduleIndex);
                                                if (!var5.expanded && !var5.settings.isEmpty()) {
                                                      var5.expanded = (boolean)(0 << 3 << 2 << 2 ^ 1178589780 ^ 1178589781);
                                                } else if (var5.expanded && !var5.settings.isEmpty()) {
                                                      var10000 = var5.settings;
                                                      var10001 = var5.index;
                                                      if ((927559209 << 4 & 442676500 ^ 268607504) == 0) {
                                                            ;
                                                      }

                                                      ((Setting)var10000.get(var10001)).focused = (boolean)(!((Setting)var5.settings.get(var5.index)).focused ? 0 >>> 4 >>> 2 ^ 1337968873 ^ 1337968872 : (122386106 ^ 105285027) & 5517060 & 644 ^ 344 ^ 856);
                                                }
                                          }

                                          if (var2 == ((190 | 47) >>> 3 << 1 & 40 ^ 229)) {
                                                if (this.expanded && var4.size() != 0) {
                                                      var5 = (Module)var4.get(var3.moduleIndex);
                                                      if (((2107232543 ^ 783861630) & 866084936 ^ -1304910465) != 0) {
                                                            ;
                                                      }

                                                      if (!var5.settings.isEmpty()) {
                                                            if (this.expanded && !var4.isEmpty() && var5.expanded) {
                                                                  if (((1386239061 ^ 1380208616 | 13687129) >>> 3 ^ 247587 ^ 1924188) == 0) {
                                                                        ;
                                                                  }

                                                                  var6 = (Setting)((Module)var4.get(var3.moduleIndex)).settings.get(var5.index);
                                                                  if (!"you probably spell youre as your".equals("please take a shower")) {
                                                                        ;
                                                                  }

                                                                  if (var6 instanceof BooleanSetting) {
                                                                        ((BooleanSetting)var6).toggle();
                                                                  }

                                                                  if (var6 instanceof ModeSetting) {
                                                                        ModeSetting var12 = (ModeSetting)var6;
                                                                        if (!"please go outside".equals("Your family tree must be a cactus because everyone on it is a prick.")) {
                                                                              ;
                                                                        }

                                                                        var12.cycle();
                                                                        if (!"You're so fat whenever you go to the beach the tide comes in.".equals("Your family tree must be a cactus because everyone on it is a prick.")) {
                                                                              ;
                                                                        }
                                                                  }
                                                            } else {
                                                                  if (!"intentMoment".equals("stop skidding")) {
                                                                        ;
                                                                  }

                                                                  var5.toggle();
                                                            }
                                                      } else {
                                                            var5.toggle();
                                                      }
                                                } else {
                                                      this.expanded = (boolean)((0 >> 3 & 56200968) >> 3 ^ 1013600473 ^ 1013600472);
                                                }
                                          }

                                          if (var2 == ((43 & 22 | 0) ^ 201)) {
                                                if (this.expanded && !var4.isEmpty()) {
                                                      if ((((1874450083 ^ 110968020) >> 3 | 25062053) & 12343359 ^ -715232061) != 0) {
                                                            ;
                                                      }

                                                      if (((Module)var4.get(var3.moduleIndex)).expanded) {
                                                            var5 = (Module)var4.get(var3.moduleIndex);
                                                            if (!var5.settings.isEmpty() && !((Setting)var5.settings.get(var5.index)).focused) {
                                                                  ((Module)var4.get(var3.moduleIndex)).expanded = (boolean)((43799067 ^ 32362690) >> 2 ^ 14442678);
                                                            }

                                                            return;
                                                      }
                                                }

                                                this.expanded = (boolean)(773503822 >>> 1 >>> 1 ^ 133797707 ^ 209664408);
                                                if ((((470752098 ^ 239822459) & 23138489 | 902294) & 1905807 ^ 870483515) != 0) {
                                                      ;
                                                }
                                          }
                                    }
                              }
                        } catch (Exception var7) {
                              var7.printStackTrace();
                        }

                        return;
                  }

                  if (((1536076541 << 1 >>> 3 | 267711823) ^ 202537018 ^ 1685339295) != 0) {
                        ;
                  }
            }

      }

      @SubscribeEvent
      public void onEvent(RenderGameOverlayEvent var1) {
            if (mc.player != null && mc.world != null) {
                  ElementType var10000 = var1.getType();
                  ElementType var10001 = ElementType.TEXT;
                  if ((((1526253024 ^ 607618667) >> 4 ^ 126826641) >>> 1 ^ 1608806971) != 0) {
                        ;
                  }

                  if (var10000 == var10001) {
                        if (((2048796273 ^ 510935463 | 259604156) << 3 ^ 2078015472) == 0) {
                              ;
                        }

                        int var18 = (0 | 1888220446) >>> 1 ^ 671828273 ^ 273593279;
                        if (((376839854 >>> 1 | 76763357) ^ 263938015) == 0) {
                              ;
                        }

                        int[] var19 = new int[var18];
                        int var10002 = 1796478790 >>> 2 ^ 286694609 ^ 198416640;
                        if (((206597195 | 91382846 | 50138973) >>> 3 ^ 1004870550) != 0) {
                              ;
                        }

                        int var10003 = (0 ^ 767289448) >> 2 ^ 191822363;
                        if ((599508891 >> 1 ^ 158707914 ^ 413684487) == 0) {
                              ;
                        }

                        var19[var10002] = var10003;
                        int[] var2 = var19;
                        var10003 = (0 & 1852392111) >>> 1 & 1403689713 & 1171032986 ^ 2;
                        int var10004 = ((Module.Category[])me.independed.inceptice.modules.Module.Category.values()).length * ((11 & 4) >>> 2 >> 1 & 1671808909 ^ 16);
                        if ((908704201 << 2 ^ 1368992149 ^ 2089399060) != 0) {
                              ;
                        }

                        double var28 = (double)(var10003 + var10004);
                        if (!"idiot".equals("please dont crack my plugin")) {
                              ;
                        }

                        Color var10005 = new Color;
                        int var10007 = (int)this.redSet.getValue();
                        int var10008 = (int)this.greenSet.getValue();
                        NumberSetting var10009 = this.blueSet;
                        if (((77861664 | 66673808) ^ 1514727892) != 0) {
                              ;
                        }

                        var10005.<init>(var10007, var10008, (int)var10009.getValue(), 51 >>> 3 ^ 1 ^ 1 ^ 71);
                        Hud.drawRoundedRect(5.0D, 40.0D, 80.0D, var28, 6.0D, var10005.getRGB());
                        int var21 = (28 & 13 & 10) << 1 ^ 58;
                        if (((1046502062 >> 1 | 491601975) ^ -1024095962) != 0) {
                              ;
                        }

                        Hud.drawRoundedRect(6.0D, (double)(var21 + this.currentTab * (4 & 3 ^ 1646813434 ^ 1646813418)), 2.0D, 13.0D, 3.0D, Hud.rainbow(var2[1140918312 >> 3 >> 3 ^ 17826848] * ((199 & 188 | 28) << 1 & 130 ^ 300)));
                        int var3 = 734953888 << 3 >> 4 ^ 99041488;
                        Module.Category[] var4 = (Module.Category[])me.independed.inceptice.modules.Module.Category.values();
                        int var5 = var4.length;

                        GlyphPageFontRenderer var20;
                        String var23;
                        int var31;
                        for(int var6 = 298731523 >> 4 << 1 ^ 35695818 ^ 1664458; var6 < var5; ++var6) {
                              if (!"you probably spell youre as your".equals("minecraft")) {
                                    ;
                              }

                              Module.Category var7 = var4[var6];
                              int var10006;
                              float var29;
                              Color var30;
                              if (this.currentTab == var3) {
                                    var20 = Hud.myRenderer;
                                    var23 = var7.name;
                                    var10003 = (13 >> 4 & 1435508216) >>> 3 ^ 43;
                                    var31 = (10 >> 1 << 2 | 18) ^ 6;
                                    if (!"your mom your dad the one you never had".equals("intentMoment")) {
                                          ;
                                    }

                                    var29 = (float)(var10003 + var3 * var31);
                                    var30 = new Color;
                                    if (((56918916 >> 4 >>> 1 >>> 1 | 622050) ^ 917486) == 0) {
                                          ;
                                    }

                                    var10006 = (129 >>> 1 << 2 | 221) ^ 290;
                                    var10007 = (1872466924 | 1522755189) & 976680003 ^ 974320705;
                                    var10008 = (262409560 ^ 13003644 | 203765776) ^ 258440756;
                                    if (!"You're so fat whenever you go to the beach the tide comes in.".equals("stringer is a good obfuscator")) {
                                          ;
                                    }

                                    var30.<init>(var10006, var10007, var10008, ((31 & 20) >>> 3 | 1) ^ 252);
                                    var10004 = var30.getRGB();
                                    if ((546308225 >> 3 ^ 68288528) == 0) {
                                          ;
                                    }

                                    var20.drawString(var23, 17.0F, var29, var10004, (boolean)((0 >> 3 | 1422390160 | 1100072494) << 3 ^ -1363165711));
                              } else {
                                    var20 = Hud.myRenderer;
                                    var23 = var7.name;
                                    var10003 = (6 & 5 | 0) ^ 2 ^ 45;
                                    var31 = 14 >>> 2 << 3 ^ 8;
                                    if (((989526811 >> 1 << 1 | 547950314) ^ 989593594) == 0) {
                                          ;
                                    }

                                    var10004 = var3 * var31;
                                    if (!"you're dogshit".equals("stringer is a good obfuscator")) {
                                          ;
                                    }

                                    var29 = (float)(var10003 + var10004);
                                    var30 = new Color;
                                    var10006 = ((25 | 0) >> 4 | 0) ^ 254;
                                    var10007 = (249 & 210 | 92) << 4 ^ 3391;
                                    var10008 = (169 | 117) >>> 3 ^ 9 ^ 14 ^ 231;
                                    int var32 = 131 >> 4 >>> 1 ^ 251;
                                    if ((((1863290626 ^ 1215159865) >>> 3 | 59074355) << 3 ^ 1064008120) == 0) {
                                          ;
                                    }

                                    var30.<init>(var10006, var10007, var10008, var32);
                                    var20.drawString(var23, 11.0F, var29, var30.getRGB(), (boolean)(0 >> 2 & 1974441242 ^ 1));
                              }

                              ++var3;
                        }

                        if (this.expanded) {
                              Module.Category var12 = ((Module.Category[])me.independed.inceptice.modules.Module.Category.values())[this.currentTab];
                              List var22 = ModuleManager.getModulesByCategory(var12);
                              if ((((1285814722 >> 1 | 593983090) & 579915320) >> 3 ^ 39295935 ^ 102072953) == 0) {
                                    ;
                              }

                              List var13 = var22;
                              if (var13.size() == 0) {
                                    return;
                              }

                              var28 = (double)(((1 & 0) >>> 3 ^ 2) + var13.size() * ((11 & 3 ^ 2 ^ 0) >> 1 >>> 3 ^ 16));
                              var10005 = new Color((int)this.redSet.getValue(), (int)this.greenSet.getValue(), (int)this.blueSet.getValue(), (28 & 21) << 2 ^ 17);
                              if ((1669514330 >>> 3 << 2 ^ 1577987362) != 0) {
                                    ;
                              }

                              var31 = var10005.getRGB();
                              if ((((37270496 ^ 27047) & 22332109 | 761557) >> 1 << 3 ^ -1238719348) != 0) {
                                    ;
                              }

                              Hud.drawRoundedRect(88.0D, 40.0D, 82.0D, var28, 6.0D, var31);
                              if (!"please get a girlfriend and stop cracking plugins".equals("please get a girlfriend and stop cracking plugins")) {
                                    ;
                              }

                              double var25 = (double)(((28 >> 2 ^ 3) >>> 1 ^ 42) + var12.moduleIndex * (3 & 0 ^ 1973599892 ^ 1973599876));
                              var10005 = new Color;
                              if (!"shitted on you harder than archybot".equals("Some babies were dropped on their heads but you were clearly thrown at a wall.")) {
                                    ;
                              }

                              var10005.<init>((61 & 10) << 3 << 1 << 2 >>> 4 ^ 223, (1113876698 >>> 4 << 2 ^ 175846265 | 440782555) & 386930863 ^ 301992079, ((2036260661 | 1254682416 | 1738407934) << 4 | 1761984341) ^ -33554443, (98 | 64) << 2 ^ 324 ^ 180);
                              Hud.drawRoundedRect(88.0D, var25, 82.0D, 18.0D, 1.0D, var10005.getRGB());
                              var3 = (1214132625 >>> 3 >> 2 & 9148898) >> 3 ^ 21040;
                              Iterator var14 = var13.iterator();

                              while(true) {
                                    boolean var24 = var14.hasNext();
                                    if (((1081355634 ^ 814020170) >>> 1 & 776085647 ^ 675291276) == 0) {
                                          ;
                                    }

                                    if (!var24) {
                                          break;
                                    }

                                    Module var15 = (Module)var14.next();
                                    if (var15.isToggled()) {
                                          var20 = Hud.myRenderer;
                                          var23 = var15.name;
                                          if (((1665644244 >> 1 ^ 761144624 ^ 363020073) << 1 ^ 2075563275) != 0) {
                                                ;
                                          }

                                          var20.drawString(var23, 91.0F, (float)(((28 ^ 27 | 4 | 3) ^ 44) + var3 * ((5 << 1 | 4) ^ 30)), (new Color(1 & 0 & 836216937 ^ 10, ((156 | 122) ^ 126) >>> 4 << 1 ^ 239, (3 >> 2 | 1892493840) & 1092079636 ^ 863041169 ^ 1937110670, 209 >> 3 << 3 ^ 47)).getRGB(), (boolean)(((0 ^ 892111115) >> 4 | 34703207) << 3 ^ 49773801 ^ 409535312));
                                    } else {
                                          var20 = Hud.myRenderer;
                                          if (!"buy a domain and everything else you need at namecheap.com".equals("Some babies were dropped on their heads but you were clearly thrown at a wall.")) {
                                                ;
                                          }

                                          var20.drawString(var15.name, 91.0F, (float)(((8 ^ 2) >> 1 ^ 46) + var3 * ((3 ^ 1) >> 3 ^ 16)), (new Color(109 << 2 & 168 ^ 95, 205 << 3 >> 2 ^ 138 ^ 495, (161 | 21 | 176) >>> 1 << 3 ^ 559, ((179 & 70) >>> 2 >>> 1 | 232664077) ^ 232664306)).getRGB(), (boolean)(((0 & 1178186903) << 1 & 1565538682) >> 1 ^ 1));
                                    }

                                    if (var3 == var12.moduleIndex && var15.expanded && !var15.settings.isEmpty()) {
                                          if ((((533718337 ^ 444128343) >> 1 << 2 | 132552037) ^ 267319149) == 0) {
                                                ;
                                          }

                                          var28 = (double)(((28 ^ 3) << 3 ^ 208) + var15.settings.size() * ((1 & 0 ^ 85221649) >>> 1 ^ 42610840));
                                          var10005 = new Color;
                                          var10007 = (int)this.redSet.getValue();
                                          var10008 = (int)this.greenSet.getValue();
                                          if (!"please get a girlfriend and stop cracking plugins".equals("you probably spell youre as your")) {
                                                ;
                                          }

                                          var10005.<init>(var10007, var10008, (int)this.blueSet.getValue(), (45 ^ 11) >> 2 >>> 2 ^ 67);
                                          Hud.drawRoundedRect(173.0D, 40.0D, 117.0D, var28, 6.0D, var10005.getRGB());
                                          if ((136734054 << 1 >>> 4 >> 2 ^ 4272939) == 0) {
                                                ;
                                          }

                                          var21 = (((10 | 0) & 3) << 3 >>> 4 ^ 41) + var15.index * ((8 & 3) >> 1 & 76790559 ^ 16);
                                          if ((843137073 >>> 2 & 167129862 ^ 143659012) == 0) {
                                                ;
                                          }

                                          Hud.drawRoundedRect(173.0D, (double)var21, 117.0D, 18.0D, 1.0D, (new Color((22 & 17) << 4 << 4 >>> 3 ^ 767, (1083533486 << 2 ^ 32424387) << 4 >> 3 >>> 1 ^ 62612347, 633397408 >>> 3 >> 4 ^ 4948417, (115 & 22 ^ 3 | 11) ^ 228)).getRGB());
                                          int var8 = (1593475556 ^ 1269640437 ^ 50539414) & 128082583 ^ 18951287 ^ 119615216;
                                          var22 = var15.settings;
                                          if (!"shitted on you harder than archybot".equals("idiot")) {
                                                ;
                                          }

                                          for(Iterator var9 = var22.iterator(); var9.hasNext(); ++var8) {
                                                Setting var10 = (Setting)var9.next();
                                                StringBuilder var27;
                                                if (var10 instanceof BooleanSetting) {
                                                      BooleanSetting var11 = (BooleanSetting)var10;
                                                      var20 = Hud.myRenderer;
                                                      var27 = (new StringBuilder()).append(var10.name).append(": ");
                                                      String var26 = var11.enabled ? "Enabled" : "Disabled";
                                                      if (((1556012799 >> 1 | 184520806) >> 1 ^ -2016442349) != 0) {
                                                            ;
                                                      }

                                                      var20.drawString(var27.append(var26).toString(), 177.0F, (float)((((4 & 1) >> 3 | 6407925) >> 4 ^ 400452) + var8 * ((10 >>> 1 | 3) ^ 23)), (new Color(4 >> 2 << 4 & 8 ^ 255, 252 >>> 1 >>> 3 & 1 ^ 254, 127 << 4 & 1986 ^ 1855, (21 >>> 3 | 1) ^ 252)).getRGB(), (boolean)(0 >>> 2 << 1 ^ 1));
                                                }

                                                if (var10 instanceof NumberSetting) {
                                                      if (((394667740 | 171471212) >> 1 ^ -1275239835) != 0) {
                                                            ;
                                                      }

                                                      NumberSetting var16 = (NumberSetting)var10;
                                                      var20 = Hud.myRenderer;
                                                      var27 = (new StringBuilder()).append(var10.name);
                                                      if (((1658163001 | 859867968) & 1004359020 ^ 558562694 ^ -596091401) != 0) {
                                                            ;
                                                      }

                                                      var20.drawString(var27.append(":").append(var16.getValue()).toString(), 177.0F, (float)(((34 ^ 0 ^ 18) >> 2 ^ 39) + var8 * (5 & 2 & 1159093459 ^ 16)), (new Color((211 << 2 >> 1 & 204) >> 3 ^ 239, 42 >> 2 >>> 3 ^ 254, (97 >> 2 >> 3 ^ 1) >> 4 ^ 255, 74 >>> 4 >> 1 >> 4 >> 3 >>> 1 ^ 255)).getRGB(), (boolean)(0 >>> 1 << 2 >>> 1 << 1 >>> 4 ^ 1));
                                                }

                                                if (var10 instanceof ModeSetting) {
                                                      if ((225373277 >>> 2 << 1 >> 1 & 2786314 ^ 646340363) != 0) {
                                                            ;
                                                      }

                                                      if ((350041382 >> 1 >> 3 >> 2 ^ 5469396) == 0) {
                                                            ;
                                                      }

                                                      ModeSetting var17 = (ModeSetting)var10;
                                                      var20 = Hud.myRenderer;
                                                      var23 = (new StringBuilder()).append(var10.name).append(":").append(var17.activeMode).toString();
                                                      if (((1825331951 >> 3 ^ 86192770) << 1 ^ 292910014) == 0) {
                                                            ;
                                                      }

                                                      var20.drawString(var23, 177.0F, (float)(((29 >>> 3 >> 2 ^ 2142983329) >>> 3 ^ 267872959) + var8 * ((10 ^ 5) >> 3 >>> 1 ^ 16)), (new Color((127 & 110) << 2 ^ 132 ^ 451, (97 >>> 3 >>> 1 | 0) ^ 5 ^ 252, ((147 | 40) ^ 64) >>> 2 ^ 193, ((106 ^ 1) >> 2 | 16) ^ 229)).getRGB(), (boolean)(((0 | 1939912030) ^ 881007710 | 371206233) ^ 1461954904));
                                                }
                                          }
                                    }

                                    ++var3;
                              }
                        }
                  }

            }
      }
}
