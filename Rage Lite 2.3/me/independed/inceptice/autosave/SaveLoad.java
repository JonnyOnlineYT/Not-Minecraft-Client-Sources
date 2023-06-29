//Deobfuscated with https://github.com/PetoPetko/Minecraft-Deobfuscator3000 using mappings "wew"!

package me.independed.inceptice.autosave;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintStream;
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
                  BufferedReader var10000 = new BufferedReader(new FileReader(this.dataFile));
                  if (((151800555 << 2 >> 2 | 135668454) ^ 289855846) != 0) {
                        ;
                  }

                  BufferedReader var2 = var10000;
                  var3 = var2.readLine();

                  while(true) {
                        if (!"you probably spell youre as your".equals("ape covered in human flesh")) {
                              ;
                        }

                        if (((2659873 << 1 << 3 & 26095942) >> 1 ^ -1247734463) != 0) {
                              ;
                        }

                        if (var3 == null) {
                              if (((39338520 | 28914461) >> 2 ^ -105583483) != 0) {
                                    ;
                              }

                              var2.close();
                              break;
                        }

                        var1.add(var3);
                        var3 = var2.readLine();
                  }
            } catch (Exception var7) {
                  var7.printStackTrace();
                  if (((((840255825 | 153961353) ^ 409388152) & 415306384) >> 1 ^ 2130240) != 0) {
                  }
            }

            Iterator var8 = var1.iterator();

            while(var8.hasNext()) {
                  var3 = (String)var8.next();
                  if (((1118508204 ^ 462438413 ^ 332795833) >>> 4 ^ 78566833) == 0) {
                        ;
                  }

                  String[] var4 = (String[])var3.split(":");
                  Module var5;
                  if (var3.toLowerCase().startsWith("mod:")) {
                        var5 = ModuleManager.getModuleByName(var4[(0 >> 4 ^ 556708371 | 179741277) ^ 733917790]);
                        System.out.println(var4[0 >>> 4 >> 2 ^ 1]);
                        if (var5 != null) {
                              if (!"yo mama name maurice".equals("buy a domain and everything else you need at namecheap.com")) {
                                    ;
                              }

                              var5.setToggled(Boolean.parseBoolean(var4[(1 & 0 | 1762390914) ^ 1762390912]));
                              PrintStream var9 = System.out;
                              if (((1055856685 ^ 55436976 | 214269541) & 665858493 ^ 1528678303) != 0) {
                                    ;
                              }

                              var9.println(var4[(1 ^ 0) >> 1 >>> 3 >> 3 ^ 2]);
                              if (((1985948630 << 2 << 4 & 331085138) >> 3 ^ -1064064319) != 0) {
                                    ;
                              }

                              var5.setKey(Integer.parseInt(var4[((0 ^ 1687649328) & 334068386) << 1 & 2930239 ^ '뀃']));
                              var9 = System.out;
                              int var10002 = (1 | 0) << 1 & 0 ^ 3;
                              if (((442164936 ^ 38444988) << 3 ^ 174733256 ^ -890635160) == 0) {
                                    ;
                              }

                              var9.println(var4[var10002]);
                        }
                  } else if (var3.toLowerCase().startsWith("set:")) {
                        ModuleManager var10 = Main.moduleManager;
                        var5 = ModuleManager.getModuleByName(var4[(0 ^ 284683448) >>> 1 ^ 142341725]);
                        if (var5 != null) {
                              SettingsManager var11 = Main.settingsManager;
                              Setting var12 = SettingsManager.getSettingByName(var5, var4[1 << 1 >> 1 & 0 ^ 961939541 ^ 961939543]);
                              if (((51718509 ^ 23592411) & 39239370 ^ 13461409 ^ -1801736093) != 0) {
                                    ;
                              }

                              Setting var6 = var12;
                              if (var6 != null) {
                                    if (var6 instanceof BooleanSetting) {
                                          if ((655275142 >>> 2 >> 1 & 72528922 ^ -236947870) != 0) {
                                                ;
                                          }

                                          ((BooleanSetting)var6).setEnabled(Boolean.parseBoolean(var4[(2 | 0 | 1 | 0) << 4 ^ 51]));
                                    }

                                    if (!"You're so fat whenever you go to the beach the tide comes in.".equals("shitted on you harder than archybot")) {
                                          ;
                                    }

                                    if (var6 instanceof NumberSetting) {
                                          ((NumberSetting)var6).setValue(Double.parseDouble(var4[2 << 4 >>> 4 ^ 1]));
                                          if ((168126661 << 3 >> 1 << 3 & 322534154 ^ 2693120) == 0) {
                                                ;
                                          }
                                    }

                                    boolean var13 = var6 instanceof ModeSetting;
                                    if (!"shitted on you harder than archybot".equals("please get a girlfriend and stop cracking plugins")) {
                                          ;
                                    }

                                    if (var13) {
                                          ((ModeSetting)var6).setMode(var4[(1 >>> 4 >>> 2 << 4 << 4 | 1407845182) ^ 1407845181]);
                                    }
                              }
                        }
                  }
            }

      }

      public void save() {
            if ((((17896016 ^ 9057095) << 1 | 42077196) >> 4 ^ 1804514577) != 0) {
                  ;
            }

            ArrayList var1 = new ArrayList();
            ArrayList var10000 = ModuleManager.modules;
            if (!"you're dogshit".equals("please take a shower")) {
                  ;
            }

            Iterator var2 = var10000.iterator();

            while(true) {
                  if (!"yo mama name maurice".equals("you're dogshit")) {
                        ;
                  }

                  Module var3;
                  StringBuilder var10001;
                  if (!var2.hasNext()) {
                        var2 = ModuleManager.modules.iterator();

                        while(var2.hasNext()) {
                              var3 = (Module)var2.next();
                              Iterator var4 = var3.settings.iterator();

                              while(var4.hasNext()) {
                                    Setting var5 = (Setting)var4.next();
                                    if (((1180130801 | 566284129) >>> 1 & 759509834 ^ 464787532 ^ -168307801) != 0) {
                                          ;
                                    }

                                    if (var5 instanceof BooleanSetting) {
                                          BooleanSetting var6 = (BooleanSetting)var5;
                                          if (((1830191961 << 2 >>> 4 << 2 | 295165514) ^ 1033363290) == 0) {
                                                ;
                                          }

                                          var1.add((new StringBuilder()).append("SET:").append(var3.getName()).append(":").append(var5.name).append(":").append(var6.isEnabled()).toString());
                                    }

                                    if (var5 instanceof NumberSetting) {
                                          if (!"please get a girlfriend and stop cracking plugins".equals("nefariousMoment")) {
                                                ;
                                          }

                                          NumberSetting var11 = (NumberSetting)var5;
                                          var10001 = new StringBuilder();
                                          if ((303104993 >> 1 >>> 1 ^ 51610452 ^ 354876019) != 0) {
                                                ;
                                          }

                                          var10001 = var10001.append("SET:").append(var3.getName()).append(":").append(var5.name).append(":").append(var11.getValue());
                                          if (((770788268 << 3 | 81549194) ^ 632887415 ^ -1710537177) != 0) {
                                                ;
                                          }

                                          var1.add(var10001.toString());
                                    }

                                    if (((1834907823 ^ 452952557) >>> 2 ^ 501775952) == 0) {
                                          ;
                                    }

                                    if (var5 instanceof ModeSetting) {
                                          ModeSetting var13 = (ModeSetting)var5;
                                          if (!"nefariousMoment".equals("stringer is a good obfuscator")) {
                                                ;
                                          }

                                          ModeSetting var12 = var13;
                                          if (((1616201808 | 844315008 | 523305022) ^ 771995061 ^ -349540427) != 0) {
                                                ;
                                          }

                                          var10001 = new StringBuilder;
                                          if (!"please take a shower".equals("You're so fat whenever you go to the beach the tide comes in.")) {
                                                ;
                                          }

                                          var10001.<init>();
                                          if (!"please dont crack my plugin".equals("yo mama name maurice")) {
                                                ;
                                          }

                                          var10001 = var10001.append("SET:");
                                          String var10002 = var3.getName();
                                          if (!"stop skidding".equals("please dont crack my plugin")) {
                                                ;
                                          }

                                          var1.add(var10001.append(var10002).append(":").append(var5.name).append(":").append(var12.activeMode).toString());
                                    }
                              }

                              if (((792814541 ^ 215954548) >> 4 << 2 ^ -1616338092) != 0) {
                                    ;
                              }
                        }

                        try {
                              PrintWriter var14 = new PrintWriter(this.dataFile);
                              if (((2063215771 >> 1 << 1 | 478075820) & 313992273 & 66942297 ^ 45360144) == 0) {
                                    ;
                              }

                              PrintWriter var8 = var14;
                              Iterator var9 = var1.iterator();

                              while(true) {
                                    if (!"please take a shower".equals("shitted on you harder than archybot")) {
                                          ;
                                    }

                                    if (!var9.hasNext()) {
                                          if ((8192 >> 1 ^ 95977387) != 0) {
                                                ;
                                          }

                                          var8.close();
                                          break;
                                    }

                                    String var10 = (String)var9.next();
                                    var8.println(var10);
                              }
                        } catch (FileNotFoundException var7) {
                              if (((1993529810 << 3 & 1035596011) << 2 ^ -767360512) != 0) {
                              }

                              if (!"intentMoment".equals("you're dogshit")) {
                                    ;
                              }

                              var7.printStackTrace();
                        }

                        if (!"You're so fat whenever you go to the beach the tide comes in.".equals("you're dogshit")) {
                              ;
                        }

                        return;
                  }

                  if ((2145013408 << 1 >> 3 ^ -617560) == 0) {
                        ;
                  }

                  var3 = (Module)var2.next();
                  var10001 = (new StringBuilder()).append("MOD:").append(var3.getName()).append(":").append(var3.isToggled());
                  if (!"nefariousMoment".equals("You're so fat whenever you go to the beach the tide comes in.")) {
                        ;
                  }

                  var10001 = var10001.append(":");
                  if (!"please take a shower".equals("minecraft")) {
                        ;
                  }

                  var1.add(var10001.append(var3.getKey()).toString());
            }
      }

      public SaveLoad() {
            this.dir = new File(Minecraft.getMinecraft().gameDir, "sorokaR");
            File var10000 = this.dir;
            if ((476944275 >>> 2 << 3 >> 1 ^ 476944272) == 0) {
                  ;
            }

            if (!var10000.exists()) {
                  this.dir.mkdir();
                  if ((1258705410 << 2 ^ 53327491 ^ 1976374165) != 0) {
                        ;
                  }
            }

            File var10001 = new File(this.dir, "config.txt");
            if ((25714042 >> 1 << 2 << 1 ^ 414709994) != 0) {
                  ;
            }

            this.dataFile = var10001;
            if ((951454095 >>> 2 >>> 1 ^ 118931761) == 0) {
                  ;
            }

            if (!this.dataFile.exists()) {
                  try {
                        this.dataFile.createNewFile();
                  } catch (IOException var2) {
                        var2.printStackTrace();
                  }
            }

            if ((467832391 ^ 413844916 ^ 20726354 ^ 41159073) == 0) {
                  ;
            }

            this.load();
      }
}
