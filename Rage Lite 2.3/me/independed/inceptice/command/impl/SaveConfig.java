//Deobfuscated with https://github.com/PetoPetko/Minecraft-Deobfuscator3000 using mappings "wew"!

package me.independed.inceptice.command.impl;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Iterator;
import me.independed.inceptice.command.Command;
import me.independed.inceptice.modules.Module;
import me.independed.inceptice.modules.ModuleManager;
import me.independed.inceptice.settings.BooleanSetting;
import me.independed.inceptice.settings.ModeSetting;
import me.independed.inceptice.settings.NumberSetting;
import me.independed.inceptice.settings.Setting;
import me.independed.inceptice.util.ChatUtil;
import net.minecraft.client.Minecraft;

public class SaveConfig extends Command {
      private File dir;
      private File dataFile;

      public void save() {
            ArrayList var1 = new ArrayList();

            Iterator var2;
            Module var3;
            StringBuilder var10001;
            for(var2 = ModuleManager.modules.iterator(); var2.hasNext(); var1.add(var10001.append(var3.getKey()).toString())) {
                  var3 = (Module)var2.next();
                  var10001 = (new StringBuilder()).append("MOD:").append(var3.getName());
                  if ((734548373 << 2 >> 3 >> 1 >>> 3 ^ -1379280935) != 0) {
                        ;
                  }

                  var10001 = var10001.append(":").append(var3.isToggled()).append(":");
                  if ((((371349050 | 370579575) >>> 2 | 5281649) ^ 98416639) == 0) {
                        ;
                  }

                  if (!"stop skidding".equals("you're dogshit")) {
                        ;
                  }
            }

            var2 = ModuleManager.modules.iterator();

            while(true) {
                  if ((1116990805 << 1 >>> 1 >> 2 >>> 1 ^ 1728617335) != 0) {
                        ;
                  }

                  if (!var2.hasNext()) {
                        try {
                              PrintWriter var8 = new PrintWriter(this.dataFile);
                              Iterator var10000 = var1.iterator();
                              if (!"please take a shower".equals("your mom your dad the one you never had")) {
                                    ;
                              }

                              String var10;
                              for(Iterator var9 = var10000; var9.hasNext(); var8.println(var10)) {
                                    var10 = (String)var9.next();
                                    if (((181314961 ^ 132383999) & 79650293 ^ 42228046 ^ 112089130) == 0) {
                                          ;
                                    }
                              }

                              var8.close();
                        } catch (FileNotFoundException var7) {
                              if ((232528304 << 1 << 4 ^ 1424404997) == 0) {
                              }

                              var7.printStackTrace();
                        }

                        return;
                  }

                  if (((664876 ^ 119312) & 517386 & 75433 ^ 16773 ^ 557223653) != 0) {
                        ;
                  }

                  var3 = (Module)var2.next();
                  Iterator var4 = var3.settings.iterator();

                  while(var4.hasNext()) {
                        Setting var5 = (Setting)var4.next();
                        if (var5 instanceof BooleanSetting) {
                              if (!"your mom your dad the one you never had".equals("intentMoment")) {
                                    ;
                              }

                              BooleanSetting var6 = (BooleanSetting)var5;
                              var10001 = (new StringBuilder()).append("SET:");
                              if ((1610875716 >>> 4 ^ -275375889) != 0) {
                                    ;
                              }

                              var10001 = var10001.append(var3.getName());
                              if (((1741853396 >> 1 | 394170675) >>> 2 ^ -1084609923) != 0) {
                                    ;
                              }

                              var10001 = var10001.append(":").append(var5.name);
                              if (((19317576 >> 3 ^ 1360237) << 1 ^ 1162165758) != 0) {
                                    ;
                              }

                              var1.add(var10001.append(":").append(var6.isEnabled()).toString());
                              if (!"Your family tree must be a cactus because everyone on it is a prick.".equals("buy a domain and everything else you need at namecheap.com")) {
                                    ;
                              }
                        }

                        if (!"please take a shower".equals("shitted on you harder than archybot")) {
                              ;
                        }

                        if (var5 instanceof NumberSetting) {
                              if (!"stop skidding".equals("please get a girlfriend and stop cracking plugins")) {
                                    ;
                              }

                              NumberSetting var11 = (NumberSetting)var5;
                              var1.add((new StringBuilder()).append("SET:").append(var3.getName()).append(":").append(var5.name).append(":").append(var11.getValue()).toString());
                        }

                        if (var5 instanceof ModeSetting) {
                              ModeSetting var12 = (ModeSetting)var5;
                              var10001 = new StringBuilder;
                              if (((1208394818 >>> 3 << 2 | 88310282) ^ -63806770) != 0) {
                                    ;
                              }

                              var10001.<init>();
                              if (((1417698866 >> 1 >>> 3 & 80638991 | 70452234) ^ -289370100) != 0) {
                                    ;
                              }

                              var10001 = var10001.append("SET:");
                              String var10002 = var3.getName();
                              if (!"stringer is a good obfuscator".equals("idiot")) {
                                    ;
                              }

                              var10001 = var10001.append(var10002).append(":").append(var5.name);
                              if ((1283081009 >>> 1 >>> 4 << 2 ^ 31433692 ^ -1102600565) != 0) {
                                    ;
                              }

                              var1.add(var10001.append(":").append(var12.activeMode).toString());
                        }
                  }

                  if (!"you're dogshit".equals("shitted on you harder than archybot")) {
                        ;
                  }

                  if ((((613548662 ^ 340283792) & 339784890 | 50475415) >> 3 ^ 40388150) == 0) {
                        ;
                  }
            }
      }

      public SaveConfig() {
            String[] var10004 = new String[(((2 & 1) >>> 4 ^ 2071960083) & 1324966027) >>> 4 ^ 78090244];
            var10004[(1368328580 | 459644176) << 1 ^ -1210107096] = "cs";
            int var10006 = (0 >> 3 ^ 786887456) & 278128851 ^ 8577025;
            if (!"buy a domain and everything else you need at namecheap.com".equals("your mom your dad the one you never had")) {
                  ;
            }

            var10004[var10006] = "saveconfig";
            var10004[(1 | 0 | 0) >>> 2 >> 1 ^ 2] = "configsave";
            var10004[(1 ^ 0) >>> 1 << 2 ^ 3] = "sc";
            super("ConfigSaver", "Save your configs", "configsave <name>", var10004);
            if (((292596072 >>> 3 & 24992644) >> 3 ^ 360992) == 0) {
                  ;
            }

      }

      public void onCommand(String[] var1, String var2) {
            if (var1.length == ((0 << 2 ^ 87273140) << 4 ^ 1396370241)) {
                  this.dir = new File(Minecraft.getMinecraft().gameDir, "sorokaR");
                  if (!this.dir.exists()) {
                        this.dir.mkdir();
                  }

                  if ((1272757757 >>> 1 & 574456922 ^ 539755610) == 0) {
                        ;
                  }

                  File var10001 = new File;
                  if ((890955377 >> 2 >> 2 >> 3 ^ -104730107) != 0) {
                        ;
                  }

                  var10001.<init>(this.dir, (new StringBuilder()).append(var1[var1.length - ((0 >>> 4 | 350200921) >>> 2 << 3 ^ 700401841)]).append(".txt").toString());
                  this.dataFile = var10001;
                  if (!this.dataFile.exists()) {
                        label46: {
                              try {
                                    this.dataFile.createNewFile();
                              } catch (IOException var4) {
                                    var4.printStackTrace();
                                    break label46;
                              }

                              if (((811347111 ^ 387785233 ^ 499229911) >>> 2 & 30323841 ^ 8396928) == 0) {
                                    ;
                              }
                        }
                  }

                  this.save();
                  StringBuilder var10000 = (new StringBuilder()).append("config - ");
                  if (((2656260 ^ 912361) >>> 4 ^ 153150) == 0) {
                        ;
                  }

                  var10000 = var10000.append(var1[var1.length - ((0 ^ 589294694 ^ 265500509) >> 4 ^ 24472760 ^ 62497034)]);
                  if (((460055089 >> 4 >> 1 | 6308872) << 3 ^ -744284250) != 0) {
                        ;
                  }

                  ChatUtil.sendChatMessage(var10000.append(" successfully saved").toString());
            }

      }
}
