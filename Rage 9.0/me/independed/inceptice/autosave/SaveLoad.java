//Deobfuscated with https://github.com/PetoPetko/Minecraft-Deobfuscator3000 using mappings "wew"!

package me.independed.inceptice.autosave;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Iterator;
import me.independed.inceptice.Main;
import me.independed.inceptice.modules.Module;
import me.independed.inceptice.modules.ModuleManager;
import me.independed.inceptice.settings.BooleanSetting;
import me.independed.inceptice.settings.ModeSetting;
import me.independed.inceptice.settings.NumberSetting;
import me.independed.inceptice.settings.Setting;
import me.independed.inceptice.settings.SettingsManager;
import net.minecraft.client.Minecraft;

public class SaveLoad {
      private File dataFile;
      private File dir;

      public void load() {
            ArrayList var1 = new ArrayList();

            String var3;
            try {
                  BufferedReader var10000 = new BufferedReader;
                  FileReader var10002 = new FileReader;
                  if ((48334573 << 3 << 2 ^ 1495260127) != 0) {
                        ;
                  }

                  var10002.<init>(this.dataFile);
                  var10000.<init>(var10002);
                  BufferedReader var2 = var10000;

                  for(var3 = var2.readLine(); var3 != null; var3 = var2.readLine()) {
                        if (((1823620880 | 42055093) >>> 1 & 602811054 ^ -797225486) != 0) {
                              ;
                        }

                        var1.add(var3);
                  }

                  var2.close();
            } catch (Exception var7) {
                  if ("please take a shower".equals("You're so fat whenever you go to the beach the tide comes in.")) {
                  }

                  var7.printStackTrace();
            }

            Iterator var8 = var1.iterator();

            while(var8.hasNext()) {
                  if ((872520720 >> 1 ^ -272944886) != 0) {
                        ;
                  }

                  var3 = (String)var8.next();
                  String[] var4 = (String[])var3.split(":");
                  Module var5;
                  if (var3.toLowerCase().startsWith("mod:")) {
                        if ((((414970206 ^ 112748619) << 2 | 1460771109) ^ 2132802933) == 0) {
                              ;
                        }

                        var5 = ModuleManager.getModuleByName(var4[(0 & 838920887) >>> 4 ^ 1]);
                        System.out.println(var4[(0 & 544887128 | 1980529922) >> 3 ^ 160308259 ^ 122655618]);
                        if (var5 != null) {
                              var5.setToggled(Boolean.parseBoolean(var4[(1 | 0) >> 3 >>> 3 ^ 2]));
                              System.out.println(var4[(0 ^ 1284150885) >>> 2 >>> 3 << 4 ^ 642075442]);
                              String var10001 = var4[0 & 1760262568 & 1314646112 ^ 3];
                              if (!"nefariousMoment".equals("please dont crack my plugin")) {
                                    ;
                              }

                              var5.setKey(Integer.parseInt(var10001));
                              System.out.println(var4[(1 | 0) << 4 >> 2 & 3 ^ 3]);
                        }
                  } else if (var3.toLowerCase().startsWith("set:")) {
                        ModuleManager var9 = Main.moduleManager;
                        int var12 = (0 & 1347193029) << 1 << 4 >>> 4 ^ 1;
                        if ((((13170982 << 2 | 8399173) ^ 42559057) & 8059620 ^ 576076742) != 0) {
                              ;
                        }

                        var5 = ModuleManager.getModuleByName(var4[var12]);
                        if (!"yo mama name maurice".equals("shitted on you harder than archybot")) {
                              ;
                        }

                        if (var5 != null) {
                              SettingsManager var10 = Main.settingsManager;
                              Setting var6 = SettingsManager.getSettingByName(var5, var4[0 >> 3 << 1 & 1035682474 & 459881739 ^ 2]);
                              if (var6 != null) {
                                    if (var6 instanceof BooleanSetting) {
                                          if (!"you're dogshit".equals("Your family tree must be a cactus because everyone on it is a prick.")) {
                                                ;
                                          }

                                          ((BooleanSetting)var6).setEnabled(Boolean.parseBoolean(var4[1 >> 4 >> 4 >> 1 << 1 ^ 3]));
                                    }

                                    boolean var11 = var6 instanceof NumberSetting;
                                    if (!"you probably spell youre as your".equals("you're dogshit")) {
                                          ;
                                    }

                                    if (var11) {
                                          ((NumberSetting)var6).setValue(Double.parseDouble(var4[0 >>> 3 >>> 3 << 4 ^ 7640293 ^ 7640294]));
                                    }

                                    if (var6 instanceof ModeSetting) {
                                          ((ModeSetting)var6).setMode(var4[0 << 4 >>> 2 >>> 1 ^ 3]);
                                          if (((1665613301 | 726910174) & 913653696 ^ 576011712) == 0) {
                                                ;
                                          }
                                    }
                              }
                        }
                  }
            }

      }

      public void save() {
            ArrayList var1 = new ArrayList();
            Iterator var2 = ModuleManager.modules.iterator();

            Module var3;
            while(var2.hasNext()) {
                  var3 = (Module)var2.next();
                  var1.add((new StringBuilder()).append("MOD:").append(var3.getName()).append(":").append(var3.isToggled()).append(":").append(var3.getKey()).toString());
                  if (((42949416 >>> 2 << 2 & 21454305) >> 2 ^ 120392) == 0) {
                        ;
                  }
            }

            var2 = ModuleManager.modules.iterator();

            while(var2.hasNext()) {
                  var3 = (Module)var2.next();
                  if (!"Some babies were dropped on their heads but you were clearly thrown at a wall.".equals("your mom your dad the one you never had")) {
                        ;
                  }

                  Iterator var4 = var3.settings.iterator();

                  while(true) {
                        if (((1731349769 | 852019014 | 1933433938) >> 4 << 1 ^ 251657194) != 0) {
                        }

                        boolean var10000 = var4.hasNext();
                        if (((1669707397 << 4 ^ 431584192) >> 2 ^ 142116580) == 0) {
                              ;
                        }

                        if (!var10000) {
                              if (((1351054280 << 4 >>> 1 >> 2 | 17278609) << 1 ^ 1867165609) != 0) {
                                    ;
                              }
                              break;
                        }

                        Setting var5 = (Setting)var4.next();
                        StringBuilder var10001;
                        if (var5 instanceof BooleanSetting) {
                              if ((139266 >> 3 >>> 1 ^ -2089335577) != 0) {
                                    ;
                              }

                              BooleanSetting var6 = (BooleanSetting)var5;
                              var10001 = (new StringBuilder()).append("SET:");
                              String var10002 = var3.getName();
                              if (!"shitted on you harder than archybot".equals("idiot")) {
                                    ;
                              }

                              var1.add(var10001.append(var10002).append(":").append(var5.name).append(":").append(var6.isEnabled()).toString());
                        }

                        if (var5 instanceof NumberSetting) {
                              NumberSetting var11 = (NumberSetting)var5;
                              if (((640027962 ^ 587259990) << 1 ^ 172874456) == 0) {
                                    ;
                              }

                              var10001 = new StringBuilder();
                              if (((1758830012 | 324919417) << 2 >>> 1 ^ -1904081116) != 0) {
                                    ;
                              }

                              var10001 = var10001.append("SET:").append(var3.getName()).append(":").append(var5.name).append(":");
                              if ((((1910308850 ^ 295319234 | 1066624645) ^ 2141672617) << 3 ^ 59103456) == 0) {
                                    ;
                              }

                              var1.add(var10001.append(var11.getValue()).toString());
                        }

                        if (var5 instanceof ModeSetting) {
                              ModeSetting var12 = (ModeSetting)var5;
                              if ((2058283619 << 3 >>> 1 ^ 1790683532) == 0) {
                                    ;
                              }

                              var10001 = (new StringBuilder()).append("SET:").append(var3.getName()).append(":").append(var5.name).append(":");
                              if ((766547583 >> 2 << 4 ^ 824492304 ^ 1155588622) != 0) {
                                    ;
                              }

                              var1.add(var10001.append(var12.activeMode).toString());
                        }
                  }
            }

            try {
                  PrintWriter var8 = new PrintWriter(this.dataFile);
                  Iterator var9 = var1.iterator();

                  while(var9.hasNext()) {
                        String var10 = (String)var9.next();
                        var8.println(var10);
                  }

                  var8.close();
                  if (!"you're dogshit".equals("nefariousMoment")) {
                        ;
                  }
            } catch (FileNotFoundException var7) {
                  if ("stringer is a good obfuscator".equals("intentMoment")) {
                  }

                  var7.printStackTrace();
            }

      }

      public SaveLoad() {
            this.dir = new File(Minecraft.getMinecraft().gameDir, "sorokaR");
            if (!this.dir.exists()) {
                  this.dir.mkdir();
            }

            this.dataFile = new File(this.dir, "config.txt");
            if (!this.dataFile.exists()) {
                  try {
                        this.dataFile.createNewFile();
                  } catch (IOException var2) {
                        var2.printStackTrace();
                  }
            }

            this.load();
      }
}
