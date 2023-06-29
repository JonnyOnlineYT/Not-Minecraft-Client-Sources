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
import net.minecraft.client.multiplayer.WorldClient;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.client.event.RenderGameOverlayEvent.ElementType;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.InputEvent.KeyInputEvent;
import org.lwjgl.input.Keyboard;

public class TabGUI extends Module {
      public NumberSetting greenSet;
      public NumberSetting blueSet;
      public boolean expanded;
      public NumberSetting redSet;
      public int currentTab;

      @SubscribeEvent
      public void onKey(KeyInputEvent var1) {
            WorldClient var10000 = Minecraft.getMinecraft().world;
            if ((465382909 >>> 2 ^ 53367989 ^ 96542666) == 0) {
                  ;
            }

            if (var10000 != null && Minecraft.getMinecraft().player != null) {
                  try {
                        if (Keyboard.isCreated()) {
                              if ((((111438383 ^ 104617321) << 2 << 2 | 75675458) ^ 226752354) == 0) {
                                    ;
                              }

                              if (Keyboard.getEventKeyState()) {
                                    if ((536952337 >>> 3 & 36376787 ^ -1683896498) != 0) {
                                          ;
                                    }

                                    int var2 = Keyboard.getEventKey();
                                    if (((281821186 ^ 223629986) << 3 ^ -1402890184) != 0) {
                                          ;
                                    }

                                    if (var2 <= 0) {
                                          return;
                                    }

                                    Module.Category var3 = ((Module.Category[])me.independed.inceptice.modules.Module.Category.values())[this.currentTab];
                                    List var4 = ModuleManager.getModulesByCategory(var3);
                                    if ((335608356 << 1 << 1 & 149194225 ^ -1157900819) != 0) {
                                          ;
                                    }

                                    int var10001 = (72 | 70) >> 2 & 18 ^ 218;
                                    if (!"you're dogshit".equals("shitted on you harder than archybot")) {
                                          ;
                                    }

                                    Module var5;
                                    Setting var6;
                                    if (var2 == var10001) {
                                          if (this.expanded) {
                                                if ((2163208 << 2 ^ 2103011089) != 0) {
                                                      ;
                                                }

                                                if (this.expanded && !var4.isEmpty() && ((Module)var4.get(var3.moduleIndex)).expanded) {
                                                      var5 = (Module)var4.get(var3.moduleIndex);
                                                      if ((((1361068166 | 685463664) ^ 192063458 ^ 141471664 | 127081013) ^ 331245059 ^ 1816738998) == 0) {
                                                            ;
                                                      }

                                                      if (!var5.settings.isEmpty()) {
                                                            if (((Setting)var5.settings.get(var5.index)).focused) {
                                                                  var6 = (Setting)((Module)var4.get(var3.moduleIndex)).settings.get(var5.index);
                                                                  if (var6 instanceof NumberSetting) {
                                                                        if (((1151078851 ^ 316543808) & 1119137156 ^ -715824661) != 0) {
                                                                              ;
                                                                        }

                                                                        ((NumberSetting)var6).increment((boolean)(0 << 3 >>> 1 ^ 1));
                                                                  }
                                                            } else if (var5.index <= 0) {
                                                                  var10001 = var5.settings.size();
                                                                  int var10002 = (0 << 3 & 896826352) >> 1 & 1777463602 ^ 1;
                                                                  if (!"please dont crack my plugin".equals("please get a girlfriend and stop cracking plugins")) {
                                                                        ;
                                                                  }

                                                                  var10001 -= var10002;
                                                                  if (!"stop skidding".equals("you probably spell youre as your")) {
                                                                        ;
                                                                  }

                                                                  var5.index = var10001;
                                                            } else {
                                                                  var5.index -= (0 | 974943974) >>> 3 ^ 48704352 ^ 94676413;
                                                            }
                                                      }

                                                      if ((53891428 >> 4 << 2 << 2 << 1 ^ 88817834 ^ 587804930) != 0) {
                                                            ;
                                                      }
                                                } else if (var3.moduleIndex <= 0) {
                                                      if (((1327500021 >>> 1 >>> 2 ^ 39107241) << 1 ^ -529203337) != 0) {
                                                            ;
                                                      }

                                                      var3.moduleIndex = var4.size() - (0 >>> 1 >>> 1 ^ 1);
                                                } else {
                                                      var3.moduleIndex -= 0 << 4 >>> 3 >> 3 ^ 1;
                                                }
                                          } else if (this.currentTab <= 0) {
                                                this.currentTab = ((Module.Category[])me.independed.inceptice.modules.Module.Category.values()).length - (0 & 50009304 & 717794361 ^ 1);
                                                if (!"idiot".equals("please go outside")) {
                                                      ;
                                                }
                                          } else {
                                                var10001 = this.currentTab - ((0 >> 2 >> 2 | 1955430790) ^ 1955430791);
                                                if ((675318065 << 1 >>> 2 ^ 337659032) == 0) {
                                                      ;
                                                }

                                                this.currentTab = var10001;
                                          }
                                    }

                                    Object var8;
                                    if (var2 == (48 >>> 2 << 1 & 0 ^ 208)) {
                                          if (this.expanded) {
                                                label291: {
                                                      if (this.expanded && !var4.isEmpty()) {
                                                            if (((22519728 ^ 11501619 ^ 19094957) << 3 >> 3 ^ 693566274) != 0) {
                                                                  ;
                                                            }

                                                            if (((Module)var4.get(var3.moduleIndex)).expanded) {
                                                                  var5 = (Module)var4.get(var3.moduleIndex);
                                                                  if (!"stringer is a good obfuscator".equals("intentMoment")) {
                                                                        ;
                                                                  }

                                                                  if (!var5.settings.isEmpty()) {
                                                                        var8 = var5.settings.get(var5.index);
                                                                        if (((1426400269 << 3 << 1 >> 4 | 52880444) ^ -1116835625) != 0) {
                                                                              ;
                                                                        }

                                                                        if (((Setting)var8).focused) {
                                                                              List var9 = ((Module)var4.get(var3.moduleIndex)).settings;
                                                                              if ((((835913013 ^ 320351584 | 423753928) ^ 16511749) & 667110816 ^ 587204992) == 0) {
                                                                                    ;
                                                                              }

                                                                              var10001 = var5.index;
                                                                              if (((143770541 >> 4 | 4947969) >>> 4 >> 2 ^ -147788981) != 0) {
                                                                                    ;
                                                                              }

                                                                              var6 = (Setting)var9.get(var10001);
                                                                              if (var6 instanceof NumberSetting) {
                                                                                    ((NumberSetting)var6).increment((boolean)((498081799 | 233078836) >>> 1 & 109169226 ^ 109068298));
                                                                              }
                                                                        } else {
                                                                              int var10 = var5.index;
                                                                              List var14 = var5.settings;
                                                                              if (((1402122099 >> 1 ^ 670333990 ^ 51751984 | 121468493) ^ 255719407) == 0) {
                                                                                    ;
                                                                              }

                                                                              if (var10 >= var14.size() - ((0 | 1104291796) << 4 ^ 488799553)) {
                                                                                    if (!"Your family tree must be a cactus because everyone on it is a prick.".equals("ape covered in human flesh")) {
                                                                                          ;
                                                                                    }

                                                                                    var5.index = (616386484 ^ 570273058 | 9209156) ^ 97314774;
                                                                              } else {
                                                                                    var5.index += (0 | 516646219 | 192370939) ^ 122661131 ^ 414242033;
                                                                              }
                                                                        }
                                                                  }
                                                                  break label291;
                                                            }
                                                      }

                                                      if (var3.moduleIndex >= var4.size() - ((0 & 1197486919) << 1 >> 1 ^ 1)) {
                                                            var3.moduleIndex = (33749193 << 4 | 105798021) ^ 644865429;
                                                      } else {
                                                            var3.moduleIndex += (0 << 4 | 681361664) >> 4 >> 1 ^ 21292553;
                                                      }
                                                }
                                          } else {
                                                if (((36967938 >> 3 | 940319) ^ 56635897) != 0) {
                                                      ;
                                                }

                                                if (this.currentTab >= ((Module.Category[])me.independed.inceptice.modules.Module.Category.values()).length - ((0 >> 2 | 1930592412) ^ 1930592413)) {
                                                      this.currentTab = (1461958213 ^ 1263130766 ^ 55634736 | 408301505) & 430392166 ^ 421921634;
                                                } else {
                                                      if (!"stop skidding".equals("nefariousMoment")) {
                                                            ;
                                                      }

                                                      this.currentTab += 0 << 1 >> 3 ^ 1;
                                                }
                                          }
                                    }

                                    if (var2 == ((9 ^ 2) & 9 ^ 21) && this.expanded && var4.size() != 0) {
                                          var5 = (Module)var4.get(var3.moduleIndex);
                                          if (!var5.expanded && !var5.settings.isEmpty()) {
                                                var5.expanded = (boolean)(0 >> 3 << 4 >> 1 >> 2 ^ 1);
                                          } else if (var5.expanded && !var5.settings.isEmpty()) {
                                                Setting var11 = (Setting)var5.settings.get(var5.index);
                                                var10001 = !((Setting)var5.settings.get(var5.index)).focused ? (0 | 240842932) << 2 ^ 151335119 ^ 812569118 : (1193227756 ^ 236100456) >>> 2 >> 3 ^ 28530115 ^ 66795703;
                                                if (!"buy a domain and everything else you need at namecheap.com".equals("Your family tree must be a cactus because everyone on it is a prick.")) {
                                                      ;
                                                }

                                                var11.focused = (boolean)var10001;
                                          }
                                    }

                                    if (var2 == (11 >>> 4 >> 2 ^ 205)) {
                                          if (((60187198 << 4 & 468616142) << 4 << 2 ^ 405338958) != 0) {
                                                ;
                                          }

                                          if (this.expanded && var4.size() != 0) {
                                                if (!"intentMoment".equals("you probably spell youre as your")) {
                                                      ;
                                                }

                                                var5 = (Module)var4.get(var3.moduleIndex);
                                                if (!var5.settings.isEmpty()) {
                                                      label264: {
                                                            if (this.expanded) {
                                                                  if ((84549634 >>> 1 << 1 & 37766828 ^ 0) == 0) {
                                                                        ;
                                                                  }

                                                                  boolean var12 = var4.isEmpty();
                                                                  if ((781800070 >> 1 >> 4 ^ 24431252) == 0) {
                                                                        ;
                                                                  }

                                                                  if (!var12 && var5.expanded) {
                                                                        if (!"you probably spell youre as your".equals("your mom your dad the one you never had")) {
                                                                              ;
                                                                        }

                                                                        if (!"You're so fat whenever you go to the beach the tide comes in.".equals("you're dogshit")) {
                                                                              ;
                                                                        }

                                                                        var10001 = var3.moduleIndex;
                                                                        if ((945913760 >>> 4 << 4 >> 4 ^ 40786668 ^ -1856067797) != 0) {
                                                                              ;
                                                                        }

                                                                        Module var13 = (Module)var4.get(var10001);
                                                                        if (!"stop skidding".equals("you probably spell youre as your")) {
                                                                              ;
                                                                        }

                                                                        var8 = var13.settings.get(var5.index);
                                                                        if (((987849970 ^ 314145615) >>> 3 << 1 ^ 169215982) == 0) {
                                                                              ;
                                                                        }

                                                                        var6 = (Setting)var8;
                                                                        if (var6 instanceof BooleanSetting) {
                                                                              ((BooleanSetting)var6).toggle();
                                                                        }

                                                                        if (var6 instanceof ModeSetting) {
                                                                              ((ModeSetting)var6).cycle();
                                                                        }

                                                                        if (((2119910798 ^ 456292557) << 2 << 3 ^ -1389909920) == 0) {
                                                                              ;
                                                                        }
                                                                        break label264;
                                                                  }
                                                            }

                                                            var5.toggle();
                                                      }
                                                } else {
                                                      var5.toggle();
                                                }
                                          } else {
                                                this.expanded = (boolean)((0 << 3 & 1498087098) << 4 ^ 1);
                                          }
                                    }

                                    if (var2 == ((191 << 4 | 687) >>> 4 ^ 116)) {
                                          if (this.expanded) {
                                                if (((287142764 | 77025983) >>> 1 >>> 4 ^ 11336607) == 0) {
                                                      ;
                                                }

                                                if (!var4.isEmpty() && ((Module)var4.get(var3.moduleIndex)).expanded) {
                                                      var5 = (Module)var4.get(var3.moduleIndex);
                                                      if (!var5.settings.isEmpty()) {
                                                            if (((Setting)var5.settings.get(var5.index)).focused) {
                                                                  if ((171204829 >> 2 ^ 9150196 ^ -1093393096) != 0) {
                                                                        return;
                                                                  }
                                                            } else {
                                                                  ((Module)var4.get(var3.moduleIndex)).expanded = (boolean)(764533560 >>> 1 >>> 1 >> 1 ^ 95566695);
                                                            }

                                                            return;
                                                      }

                                                      return;
                                                }
                                          }

                                          this.expanded = (boolean)(((1488616689 ^ 240464370) >> 1 | 467492414) ^ 820090954 ^ 186541813);
                                    }
                              }
                        }
                  } catch (Exception var7) {
                        var7.printStackTrace();
                  }

            }
      }

      @SubscribeEvent
      public void renderOverlay(RenderGameOverlayEvent var1) {
            if ((672338176 >> 2 ^ -1911673368) != 0) {
                  ;
            }

            if (var1.getType() == ElementType.TEXT) {
                  int[] var10000 = new int[(0 & 647988993) << 2 >> 4 ^ 1];
                  var10000[((1777918417 ^ 422450043) >> 2 ^ 296119460 | 191735716) ^ 268433326] = (0 >> 2 | 963904212 | 818769978) ^ 972911359;
                  int[] var2 = var10000;
                  int var10003 = 1 >> 1 << 3 ^ 282073996 ^ 282073998;
                  int var10004 = ((Module.Category[])me.independed.inceptice.modules.Module.Category.values()).length;
                  int var10005 = (1 | 0) ^ 0 ^ 17;
                  if (((339820712 >> 4 >>> 3 | 147489) >>> 3 ^ -2101488589) != 0) {
                        ;
                  }

                  double var25 = (double)(var10003 + var10004 * var10005);
                  Color var30 = new Color;
                  if (!"nefariousMoment".equals("please go outside")) {
                        ;
                  }

                  var30.<init>((int)this.redSet.getValue(), (int)this.greenSet.getValue(), (int)this.blueSet.getValue(), (17 >>> 3 >>> 1 >> 3 ^ 50628440) << 3 ^ 405027457);
                  Hud.drawRoundedRect(5.0D, 40.0D, 80.0D, var25, 6.0D, var30.getRGB());
                  Hud.drawRoundedRect(6.0D, (double)((((28 | 20) & 7 | 0) ^ 46) + this.currentTab * ((6 | 0 | 4) ^ 22)), 2.0D, 13.0D, 3.0D, Hud.rainbow(var2[(1693712526 ^ 737803659) >>> 1 ^ 663158402] * ((21 ^ 8 ^ 12 | 7) << 4 ^ 92)));
                  int var3 = (1983816488 >> 2 & 16285415) >>> 4 ^ 557708;
                  Module.Category[] var4 = (Module.Category[])me.independed.inceptice.modules.Module.Category.values();
                  int var5 = var4.length;

                  String var10001;
                  GlyphPageFontRenderer var18;
                  float var27;
                  Color var28;
                  for(int var6 = (124457147 | 46342032) >> 3 << 1 & 28959116 ^ 28885388; var6 < var5; ++var6) {
                        Module.Category var7 = var4[var6];
                        if (this.currentTab == var3) {
                              var18 = Hud.myRenderer;
                              var10001 = var7.name;
                              var10003 = (10 & 5) >>> 2 ^ 853973526 ^ 853973565;
                              var10005 = 14 << 1 << 4 << 1 ^ 912;
                              if ((537002064 >> 3 ^ -433627957) != 0) {
                                    ;
                              }

                              var18.drawString(var10001, 17.0F, (float)(var10003 + var3 * var10005), (new Color((3 | 1) >> 1 << 3 << 3 ^ 191, (1634968358 | 1263918689) >>> 2 ^ 450756569, (1313216795 ^ 85781551 | 1200669428 | 916125051) ^ 2145056767, (111 >>> 3 | 10) ^ 240)).getRGB(), (boolean)((0 >>> 2 & 1843830184 ^ 42645425) >>> 2 ^ 10661357));
                        } else {
                              var18 = Hud.myRenderer;
                              var10001 = var7.name;
                              var10003 = (25 | 5) & 10 ^ 2 ^ 33;
                              if (!"buy a domain and everything else you need at namecheap.com".equals("nefariousMoment")) {
                                    ;
                              }

                              var27 = (float)(var10003 + var3 * (4 << 1 << 2 << 3 ^ 272));
                              var28 = new Color;
                              if (!"intentMoment".equals("you probably spell youre as your")) {
                                    ;
                              }

                              var28.<init>(2 >>> 1 & 0 & 179279619 ^ 255, 113 & 34 & 16 & 975496651 ^ 255, (142 ^ 55 | 168) ^ 70, 76 >>> 2 << 4 ^ 463);
                              var18.drawString(var10001, 11.0F, var27, var28.getRGB(), (boolean)((0 << 3 >>> 4 ^ 485716250) & 4803050 ^ 4276491));
                              if (((581311296 ^ 245794925) << 2 ^ 495074963 ^ -1384091097) == 0) {
                                    ;
                              }
                        }

                        ++var3;
                  }

                  if (this.expanded) {
                        Module.Category[] var19 = (Module.Category[])me.independed.inceptice.modules.Module.Category.values();
                        int var21 = this.currentTab;
                        if (((403687602 >>> 2 << 4 >>> 1 | 558319905) ^ -1857778004) != 0) {
                              ;
                        }

                        Module.Category var12 = var19[var21];
                        List var13 = ModuleManager.getModulesByCategory(var12);
                        if (var13.size() == 0) {
                              return;
                        }

                        Hud.drawRoundedRect(88.0D, 40.0D, 82.0D, (double)((1 >> 3 >> 2 & 1980054046 ^ 2) + var13.size() * (15 >> 3 >>> 3 ^ 16)), 6.0D, (new Color((int)this.redSet.getValue(), (int)this.greenSet.getValue(), (int)this.blueSet.getValue(), 41 >>> 1 >> 3 ^ 67)).getRGB());
                        if (!"you probably spell youre as your".equals("Some babies were dropped on their heads but you were clearly thrown at a wall.")) {
                              ;
                        }

                        var21 = 19 ^ 13 ^ 29 ^ 43;
                        int var10002 = var12.moduleIndex;
                        if (!"i hope you catch fire ngl".equals("please go outside")) {
                              ;
                        }

                        double var22 = (double)(var21 + var10002 * ((15 >>> 4 | 1257692655) >>> 4 ^ 78605774));
                        if ((7439380 >>> 2 << 3 << 4 >>> 3 ^ 29757520) == 0) {
                              ;
                        }

                        Hud.drawRoundedRect(88.0D, var22, 82.0D, 18.0D, 1.0D, (new Color((144 >> 2 & 18 | 306133488) ^ 306133263, ((1408557088 | 772749906) & 1249332584) << 1 ^ -1796304704, (514356149 << 4 >>> 3 | 134609729) ^ 492175211, ((71 ^ 36) << 4 | 620) ^ 1540)).getRGB());
                        if (!"i hope you catch fire ngl".equals("Your family tree must be a cactus because everyone on it is a prick.")) {
                              ;
                        }

                        var3 = ((1073141921 ^ 882749728 ^ 148697934 | 7776202) ^ 5651913) << 1 ^ 121832460;
                        Iterator var20 = var13.iterator();
                        if (((428160653 | 296280489) << 4 ^ 948198294 ^ -1571086010) == 0) {
                              ;
                        }

                        for(Iterator var14 = var20; var14.hasNext(); ++var3) {
                              Module var15 = (Module)var14.next();
                              if (var15.isToggled()) {
                                    var18 = Hud.myRenderer;
                                    var10001 = var15.name;
                                    var27 = (float)((16 >> 4 >> 4 ^ 194303129 ^ 194303154) + var3 * (13 >>> 1 >>> 1 >>> 1 ^ 17));
                                    if (((615033368 >> 1 >> 1 | 84540033) >> 2 & 33393462 ^ 1702733659) != 0) {
                                          ;
                                    }

                                    var10004 = (new Color((7 | 2) ^ 2 ^ 15, (1 ^ 0) >>> 1 ^ 255, (5 ^ 2 | 6) << 1 >> 3 ^ 0 ^ 14, ((39 >>> 3 | 0) << 3 ^ 1) >> 3 ^ 251)).getRGB();
                                    var10005 = (0 >>> 1 ^ 910536060) >>> 2 & 85011147 ^ 85011018;
                                    if ((109538175 >>> 3 ^ 4966174 ^ 1543349149) != 0) {
                                          ;
                                    }

                                    var18.drawString(var10001, 91.0F, var27, var10004, (boolean)var10005);
                              } else {
                                    if (!"idiot".equals("you probably spell youre as your")) {
                                          ;
                                    }

                                    var18 = Hud.myRenderer;
                                    var10001 = var15.name;
                                    if (!"idiot".equals("buy a domain and everything else you need at namecheap.com")) {
                                          ;
                                    }

                                    var18.drawString(var10001, 91.0F, (float)(((27 & 19) << 1 ^ 37 ^ 40) + var3 * (((12 | 11) << 1 | 3) << 3 ^ 232)), (new Color((172 & 53 & 33 | 22) ^ 201, (101 << 3 >>> 2 ^ 80) >>> 3 ^ 236, 138 << 4 & 1723 ^ 95, ((159 & 44) << 2 >> 3 ^ 0 | 5) ^ 248)).getRGB(), (boolean)((0 & 104736208) >> 1 << 2 ^ 1));
                              }

                              if (var3 == var12.moduleIndex) {
                                    if ((1581685302 >>> 4 >>> 1 ^ 49427665) == 0) {
                                          ;
                                    }

                                    if (var15.expanded && !var15.settings.isEmpty()) {
                                          if (((1283743232 | 228652553) << 2 << 1 ^ 1831333960) == 0) {
                                                ;
                                          }

                                          var10003 = (((5 ^ 3) & 3) >>> 4 | 1764924202) ^ 1598773169 ^ 913951923;
                                          List var29 = var15.settings;
                                          if (((555331654 << 1 | 595608862 | 51240484) >> 2 ^ 418381807) == 0) {
                                                ;
                                          }

                                          var25 = (double)(var10003 + var29.size() * ((7 & 3 ^ 1) & 1 & 962183645 ^ 16));
                                          var30 = new Color;
                                          if (((31560796 ^ 14949842) >> 2 ^ 562993888) != 0) {
                                                ;
                                          }

                                          var30.<init>((int)this.redSet.getValue(), (int)this.greenSet.getValue(), (int)this.blueSet.getValue(), 56 << 4 >>> 1 ^ 385);
                                          Hud.drawRoundedRect(173.0D, 40.0D, 117.0D, var25, 6.0D, var30.getRGB());
                                          Hud.drawRoundedRect(173.0D, (double)((6 >>> 4 >>> 4 >> 2 ^ 40) + var15.index * ((8 << 3 << 4 & 497 | 754356073) & 115160950 ^ 81004400)), 117.0D, 18.0D, 1.0D, (new Color((143 >>> 4 | 6) << 3 ^ 143, (292693856 ^ 264066686) >>> 4 & 11654162 ^ 3557753 ^ 9870697, 21381254 << 2 >> 4 ^ 5345313, (171 >> 4 >>> 3 | 0) ^ 254)).getRGB());
                                          int var8 = (1510813328 >> 3 & 95937054) << 3 ^ 135069840;

                                          for(Iterator var9 = var15.settings.iterator(); var9.hasNext(); ++var8) {
                                                if ((((519016653 >>> 1 ^ 145583110) << 2 | 304167915) ^ -646586509) != 0) {
                                                      ;
                                                }

                                                Setting var23 = (Setting)var9.next();
                                                if ((281102176 >>> 3 >>> 2 ^ -1147564905) != 0) {
                                                      ;
                                                }

                                                Setting var10 = var23;
                                                if (!"idiot".equals("you probably spell youre as your")) {
                                                      ;
                                                }

                                                if (var10 instanceof BooleanSetting) {
                                                      if ((1301488108 >>> 3 & 65790862 ^ 27418636) == 0) {
                                                            ;
                                                      }

                                                      BooleanSetting var24 = (BooleanSetting)var10;
                                                      if (((1589273487 << 1 ^ 132360324 | 680247397) ^ 512453891 ^ 1817658051) != 0) {
                                                            ;
                                                      }

                                                      BooleanSetting var11 = var24;
                                                      if (!"You're so fat whenever you go to the beach the tide comes in.".equals("shitted on you harder than archybot")) {
                                                            ;
                                                      }

                                                      Hud.myRenderer.drawString((new StringBuilder()).append(var10.name).append(": ").append(var11.enabled ? "Enabled" : "Disabled").toString(), 177.0F, (float)((11 >> 1 << 4 >>> 1 ^ 3) + var8 * ((1 | 0) << 2 >> 4 ^ 1236089126 ^ 1236089142)), (new Color((122 ^ 24) << 3 << 3 >> 3 ^ 1007, ((115 | 4) ^ 108) >>> 1 ^ 242, 249 >> 2 >> 3 >>> 4 ^ 255, (248 >> 1 << 4 | 1391) ^ 1808)).getRGB(), (boolean)((0 >> 2 | 328124819) ^ 328124818));
                                                }

                                                StringBuilder var26;
                                                if (var10 instanceof NumberSetting) {
                                                      NumberSetting var16 = (NumberSetting)var10;
                                                      var18 = Hud.myRenderer;
                                                      var26 = new StringBuilder();
                                                      if ((1272160682 << 4 << 3 ^ 1178850275) != 0) {
                                                            ;
                                                      }

                                                      var26 = var26.append(var10.name).append(":").append(var16.getValue());
                                                      if (((1437731693 << 2 << 2 >>> 4 >> 4 | 5870657) ^ 6010103) == 0) {
                                                            ;
                                                      }

                                                      var10001 = var26.toString();
                                                      var10003 = (35 & 21) >> 1 & 122953969 ^ 43;
                                                      var10004 = var8 * ((4 >> 2 & 0) >> 4 ^ 743815115 ^ 743815131);
                                                      if ((435830156 << 4 & 409293341 ^ -1016103497) != 0) {
                                                            ;
                                                      }

                                                      var27 = (float)(var10003 + var10004);
                                                      var28 = new Color;
                                                      if ((((1665622785 ^ 932474293) >> 1 >>> 4 | 34623441) ^ 45539317) == 0) {
                                                            ;
                                                      }

                                                      var28.<init>(105 >>> 1 ^ 45 ^ 2 ^ 228, ((165 & 160) >>> 2 ^ 18) >>> 2 >>> 3 ^ 254, ((108 | 30) & 37 | 24) ^ 195, (163 ^ 26) >>> 2 ^ 16 ^ 193);
                                                      var18.drawString(var10001, 177.0F, var27, var28.getRGB(), (boolean)((0 ^ 1978122566) >> 3 >> 2 ^ 61816331));
                                                }

                                                if (var10 instanceof ModeSetting) {
                                                      ModeSetting var17 = (ModeSetting)var10;
                                                      var18 = Hud.myRenderer;
                                                      var26 = (new StringBuilder()).append(var10.name).append(":");
                                                      if (((21168663 | 12337458) ^ 245752 ^ 27140585 ^ -2083568881) != 0) {
                                                            ;
                                                      }

                                                      if (!"stringer is a good obfuscator".equals("You're so fat whenever you go to the beach the tide comes in.")) {
                                                            ;
                                                      }

                                                      var10001 = var26.append(var17.activeMode).toString();
                                                      var10003 = (((7 ^ 1) & 1) << 4 ^ 43) + var8 * (14 >> 4 << 2 ^ 16);
                                                      if (!"please get a girlfriend and stop cracking plugins".equals("shitted on you harder than archybot")) {
                                                            ;
                                                      }

                                                      var27 = (float)var10003;
                                                      var28 = new Color;
                                                      if ((((919142192 | 681576151) & 749075260) >>> 2 ^ 187186893) == 0) {
                                                            ;
                                                      }

                                                      var28.<init>(72 & 5 ^ 324156667 ^ 324156420, 175 >> 2 << 2 ^ 83, ((40 | 23) ^ 23 ^ 19) >> 1 ^ 226, (134 | 11) >>> 3 << 2 ^ 187);
                                                      var18.drawString(var10001, 177.0F, var27, var28.getRGB(), (boolean)(0 >> 2 >> 1 ^ 2019074737 ^ 2019074736));
                                                }
                                          }
                                    }
                              }
                        }
                  }
            }

            if (((((581874904 ^ 315887660) & 193503373) >> 3 & '\uab9c') << 4 ^ 256) == 0) {
                  ;
            }

      }

      public TabGUI() {
            if ((((726654185 ^ 618195790) >>> 2 & 24143437) << 3 ^ 184689224) == 0) {
                  ;
            }

            super("TabGUI", "tab gui", 229989084 >>> 2 >>> 2 >>> 4 ^ 898394, Module.Category.HUD);
            if ((((2137537918 ^ 873472600) >> 1 | 420501850) ^ 1035755483) == 0) {
                  ;
            }

            this.currentTab = (1412542062 >> 1 | 583041934) << 4 ^ -1383236624;
            if ((((793980275 ^ 120688821) >>> 3 & 57226875 | 1682978) ^ -153967141) != 0) {
                  ;
            }

            this.redSet = new NumberSetting("Red", this, 0.0D, 0.0D, 255.0D, 1.0D);
            this.greenSet = new NumberSetting("Green", this, 0.0D, 0.0D, 255.0D, 1.0D);
            NumberSetting var10001 = new NumberSetting;
            if (!"stop skidding".equals("Your family tree must be a cactus because everyone on it is a prick.")) {
                  ;
            }

            var10001.<init>("Blue", this, 0.0D, 0.0D, 255.0D, 1.0D);
            this.blueSet = var10001;
            if (!"you're dogshit".equals("Some babies were dropped on their heads but you were clearly thrown at a wall.")) {
                  ;
            }

            Setting[] var1 = new Setting[(2 ^ 1 ^ 0) << 2 ^ 15];
            var1[(863122413 >>> 4 >>> 4 ^ 2326322) << 4 >> 3 ^ 2103810] = this.redSet;
            var1[(0 ^ 1834496732 ^ 224537499 | 274824850) ^ 1887141846] = this.greenSet;
            int var10003 = ((1 | 0 | 0) ^ 0) << 3 ^ 10;
            if ((526300494 >> 3 << 4 ^ -1315076038) != 0) {
                  ;
            }

            var1[var10003] = this.blueSet;
            this.addSettings(var1);
            if ((515146926 << 4 >> 2 << 4 >> 2 >> 2 ^ 1521212344) != 0) {
                  ;
            }

            this.toggle();
      }
}
