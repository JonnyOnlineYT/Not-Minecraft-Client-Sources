//Deobfuscated with https://github.com/PetoPetko/Minecraft-Deobfuscator3000 using mappings "wew"!

package me.independed.inceptice.command.impl;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.Iterator;
import me.independed.inceptice.Main;
import me.independed.inceptice.command.Command;
import me.independed.inceptice.modules.Module;
import me.independed.inceptice.modules.ModuleManager;
import me.independed.inceptice.settings.BooleanSetting;
import me.independed.inceptice.settings.ModeSetting;
import me.independed.inceptice.settings.NumberSetting;
import me.independed.inceptice.settings.Setting;
import me.independed.inceptice.settings.SettingsManager;
import me.independed.inceptice.util.ChatUtil;
import net.minecraft.client.Minecraft;

public class LoadConfig extends Command {
      private File dir;
      private File dataFile;

      public void onCommand(String[] var1, String var2) {
            if (var1.length == ((0 & 1434201268 | 692499753) >>> 1 >> 4 ^ 21640616)) {
                  File var10001 = new File;
                  if ((64651323 << 4 >> 1 >>> 3 << 1 & 101350060 ^ 699018924) != 0) {
                        ;
                  }

                  var10001.<init>(Minecraft.getMinecraft().gameDir, "sorokaR");
                  this.dir = var10001;
                  if (!this.dir.exists()) {
                        File var10000 = this.dir;
                        if (!"you probably spell youre as your".equals("stringer is a good obfuscator")) {
                              ;
                        }

                        var10000.mkdir();
                  }

                  var10001 = new File;
                  File var10003 = this.dir;
                  StringBuilder var10004 = (new StringBuilder()).append(var1[var1.length - ((0 ^ 1209875475 | 455594323) >> 2 & 213688035 ^ 76324417)]);
                  if (!"your mom your dad the one you never had".equals("buy a domain and everything else you need at namecheap.com")) {
                        ;
                  }

                  var10004 = var10004.append(".txt");
                  if (((515977587 >>> 2 | 98521279) >> 3 ^ -1767285188) != 0) {
                        ;
                  }

                  var10001.<init>(var10003, var10004.toString());
                  this.dataFile = var10001;
                  if (!this.dataFile.exists()) {
                        if (((990307283 << 3 | 872472516 | 1187832) >> 1 ^ -1235256795) != 0) {
                              ;
                        }

                        ChatUtil.sendChatMessage("Config doesn't exist.");
                        return;
                  }

                  this.load();
                  StringBuilder var3 = (new StringBuilder()).append("config - ");
                  int var10002 = var1.length;
                  if (!"i hope you catch fire ngl".equals("your mom your dad the one you never had")) {
                        ;
                  }

                  String var5 = var1[var10002 - (0 << 2 >> 4 >>> 1 >>> 2 ^ 1)];
                  if (!"i hope you catch fire ngl".equals("yo mama name maurice")) {
                        ;
                  }

                  String var4 = var3.append(var5).append(" successfully loaded").toString();
                  if ((21629488 >> 3 >> 1 ^ '\ud9a4' ^ 165703 ^ 1506880) == 0) {
                        ;
                  }

                  ChatUtil.sendChatMessage(var4);
            }

      }

      public LoadConfig() {
            if (!"please go outside".equals("stringer is a good obfuscator")) {
                  ;
            }

            if ((464742863 >>> 1 >> 3 >>> 2 >>> 2 ^ 968867871) != 0) {
                  ;
            }

            String[] var10004 = new String[0 >>> 4 << 4 >> 1 & 1230895246 ^ 4];
            var10004[(592826659 ^ 276194792) >>> 2 ^ 188569358 ^ 133544508] = "cl";
            var10004[0 >>> 2 ^ 357699526 ^ 137656117 ^ 493254898] = "loadconfig";
            if (!"please take a shower".equals("intentMoment")) {
                  ;
            }

            var10004[1 >> 1 >>> 3 & 1327033044 ^ 2] = "configload";
            int var10006 = (2 << 1 >>> 1 | 1 | 2) & 1 ^ 2;
            if ((((904919376 | 322469254) >>> 1 | 319238581) >>> 3 ^ -388936022) != 0) {
                  ;
            }

            var10004[var10006] = "lc";
            super("LoadConfig", "Loads your config", "configload <name>", var10004);
            if ((789825328 << 1 >>> 1 ^ 789825328) == 0) {
                  ;
            }

      }

      public void load() {
            ArrayList var1 = new ArrayList();

            String var3;
            try {
                  BufferedReader var10000 = new BufferedReader;
                  FileReader var10002 = new FileReader;
                  File var10004 = this.dataFile;
                  if ((33554432 >>> 4 ^ 2097152) == 0) {
                        ;
                  }

                  var10002.<init>(var10004);
                  var10000.<init>(var10002);
                  BufferedReader var2 = var10000;

                  for(var3 = var2.readLine(); var3 != null; var3 = var2.readLine()) {
                        var1.add(var3);
                  }

                  if ((2379784 ^ 446723 ^ 2124391990) != 0) {
                        ;
                  }

                  var2.close();
            } catch (Exception var7) {
                  var7.printStackTrace();
            }

            if (!"please dont crack my plugin".equals("intentMoment")) {
                  ;
            }

            Iterator var8 = var1.iterator();

            while(true) {
                  if ("please go outside".equals("stringer is a good obfuscator")) {
                  }

                  if (!var8.hasNext()) {
                        return;
                  }

                  var3 = (String)var8.next();
                  if ((134758536 ^ 100989500 ^ -534012560) != 0) {
                        ;
                  }

                  String[] var4 = (String[])var3.split(":");
                  Module var5;
                  Module var9;
                  if (var3.toLowerCase().startsWith("mod:")) {
                        var9 = ModuleManager.getModuleByName(var4[(0 | 640420152 | 561865160) ^ 662530553]);
                        if ((866844617 >> 3 >>> 3 >>> 1 ^ 6757611 ^ 1520545640) != 0) {
                              ;
                        }

                        var5 = var9;
                        System.out.println(var4[0 >>> 4 >>> 1 & 63739447 ^ 1]);
                        if (var5 != null) {
                              if ((((1186702280 | 306821549) ^ 64191926) >> 4 >>> 1 ^ -1283090808) != 0) {
                                    ;
                              }

                              if ((632952730 >> 3 << 4 ^ -1459010388) != 0) {
                                    ;
                              }

                              var5.setToggled(Boolean.parseBoolean(var4[1 >> 2 << 2 ^ 2]));
                              System.out.println(var4[(0 | 436600521 | 410200389 | 109726370) ^ 520093677]);
                              var5.setKey(Integer.parseInt(var4[(1 >>> 2 | 434501957 | 378187954) ^ 535820788]));
                              System.out.println(var4[1 >>> 2 >>> 4 >>> 2 ^ 3]);
                        }
                  } else if (var3.toLowerCase().startsWith("set:")) {
                        ModuleManager var10 = Main.moduleManager;
                        if (((327704505 ^ 79960012) << 2 ^ 1214592831) != 0) {
                              ;
                        }

                        var9 = ModuleManager.getModuleByName(var4[0 << 3 << 3 ^ 1]);
                        if (!"you're dogshit".equals("stringer is a good obfuscator")) {
                              ;
                        }

                        var5 = var9;
                        if (var5 != null) {
                              SettingsManager var11 = Main.settingsManager;
                              if ((((978247095 >> 1 & 111945169) >>> 4 & 3279128) << 4 ^ -1328428225) != 0) {
                                    ;
                              }

                              Setting var6 = SettingsManager.getSettingByName(var5, var4[0 >> 1 >> 4 >> 2 << 2 ^ 2]);
                              if (var6 != null) {
                                    if (!"stringer is a good obfuscator".equals("stringer is a good obfuscator")) {
                                          ;
                                    }

                                    if (var6 instanceof BooleanSetting) {
                                          ((BooleanSetting)var6).setEnabled(Boolean.parseBoolean(var4[1 >>> 3 ^ 1659240705 ^ 554040676 ^ 1139014758]));
                                    }

                                    String var10001;
                                    if (var6 instanceof NumberSetting) {
                                          NumberSetting var12 = (NumberSetting)var6;
                                          var10001 = var4[0 << 1 & 1269069650 ^ 3];
                                          if (((154682368 ^ 96629242) >> 2 ^ -1329058766) != 0) {
                                                ;
                                          }

                                          var12.setValue(Double.parseDouble(var10001));
                                    }

                                    if (var6 instanceof ModeSetting) {
                                          ModeSetting var13 = (ModeSetting)var6;
                                          var10001 = var4[(0 | 1194100442) & 207968736 ^ 69468355];
                                          if (!"Your family tree must be a cactus because everyone on it is a prick.".equals("i hope you catch fire ngl")) {
                                                ;
                                          }

                                          var13.setMode(var10001);
                                          System.out.println(var4[((1 ^ 0) >> 3 ^ 21702885) >> 4 & 155751 ^ 8197]);
                                    }
                              }
                        }
                  }
            }
      }
}
