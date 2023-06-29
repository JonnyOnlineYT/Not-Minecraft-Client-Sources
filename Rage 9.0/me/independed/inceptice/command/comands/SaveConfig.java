//Deobfuscated with https://github.com/PetoPetko/Minecraft-Deobfuscator3000 using mappings "wew"!

package me.independed.inceptice.command.comands;

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

      public SaveConfig() {
            if (!"you're dogshit".equals("intentMoment")) {
                  ;
            }

            if (!"please dont crack my plugin".equals("stop skidding")) {
                  ;
            }

            String[] var10004 = new String[(0 | 1260965831) << 1 ^ 19841149 ^ -1753260046];
            int var10006 = (517711393 >> 3 | 26825872) >> 3 ^ 8089242;
            if ((((1055029183 ^ 149738232) & 30850572) >>> 2 ^ 108801) == 0) {
                  ;
            }

            var10004[var10006] = "cs";
            super("ConfigSaver", "Save your configs", "configsave <name>", var10004);
      }

      public void save() {
            if (!"shitted on you harder than archybot".equals("your mom your dad the one you never had")) {
                  ;
            }

            ArrayList var1 = new ArrayList();
            Iterator var2 = ModuleManager.modules.iterator();

            Module var3;
            while(var2.hasNext()) {
                  var3 = (Module)var2.next();
                  var1.add((new StringBuilder()).append("MOD:").append(var3.getName()).append(":").append(var3.isToggled()).append(":").append(var3.getKey()).toString());
            }

            var2 = ModuleManager.modules.iterator();

            while(var2.hasNext()) {
                  var3 = (Module)var2.next();
                  Iterator var4 = var3.settings.iterator();

                  while(true) {
                        if ((145112862 >> 2 << 3 & 181800823 ^ 1213053725) != 0) {
                              ;
                        }

                        if (!var4.hasNext()) {
                              if (!"shitted on you harder than archybot".equals("stringer is a good obfuscator")) {
                                    ;
                              }
                              break;
                        }

                        if ((81984 << 1 ^ 90311 ^ 139126 ^ -866081345) != 0) {
                              ;
                        }

                        Object var10000 = var4.next();
                        if (!"please get a girlfriend and stop cracking plugins".equals("buy a domain and everything else you need at namecheap.com")) {
                              ;
                        }

                        Setting var5 = (Setting)var10000;
                        if (var5 instanceof BooleanSetting) {
                              BooleanSetting var6 = (BooleanSetting)var5;
                              var1.add((new StringBuilder()).append("SET:").append(var3.getName()).append(":").append(var5.name).append(":").append(var6.isEnabled()).toString());
                        }

                        if (var5 instanceof NumberSetting) {
                              NumberSetting var11 = (NumberSetting)var5;
                              StringBuilder var10001 = (new StringBuilder()).append("SET:").append(var3.getName()).append(":").append(var5.name).append(":");
                              if (!"please go outside".equals("You're so fat whenever you go to the beach the tide comes in.")) {
                                    ;
                              }

                              var1.add(var10001.append(var11.getValue()).toString());
                        }

                        if ((4779026 >> 2 >> 1 >>> 2 ^ 93478 ^ -1673233086) != 0) {
                              ;
                        }

                        if (var5 instanceof ModeSetting) {
                              ModeSetting var12 = (ModeSetting)var5;
                              var1.add((new StringBuilder()).append("SET:").append(var3.getName()).append(":").append(var5.name).append(":").append(var12.activeMode).toString());
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

                  if (((907741526 ^ 76522420 | 321924544 | 429200592) >>> 2 ^ 250441724) == 0) {
                        ;
                  }

                  var8.close();
                  if (!"nefariousMoment".equals("i hope you catch fire ngl")) {
                        ;
                  }
            } catch (FileNotFoundException var7) {
                  var7.printStackTrace();
            }

      }

      public void onCommand(String[] var1, String var2) {
            if (var1.length > 0) {
                  File var10001 = new File;
                  Minecraft var10003 = Minecraft.getMinecraft();
                  if (((232341099 ^ 103692614) << 3 ^ 1230976264) != 0) {
                        ;
                  }

                  var10001.<init>(var10003.gameDir, "sorokaR");
                  this.dir = var10001;
                  if (!this.dir.exists()) {
                        this.dir.mkdir();
                  }

                  var10001 = new File;
                  File var5 = this.dir;
                  if (!"your mom your dad the one you never had".equals("you're dogshit")) {
                        ;
                  }

                  StringBuilder var10004 = new StringBuilder();
                  int var10006 = var1.length;
                  if (((1286145468 | 1015665459) << 2 >> 3 << 2 >> 4 ^ -1323594034) != 0) {
                        ;
                  }

                  var10001.<init>(var5, var10004.append(var1[var10006 - ((0 << 3 ^ 515599544) >> 3 ^ 54233718 ^ 15525856)]).append(".txt").toString());
                  this.dataFile = var10001;
                  if (((579225749 | 262303057) >>> 4 ^ 49964765) == 0) {
                        ;
                  }

                  if (!this.dataFile.exists()) {
                        try {
                              this.dataFile.createNewFile();
                        } catch (IOException var4) {
                              var4.printStackTrace();
                        }
                  }

                  StringBuilder var10000 = (new StringBuilder()).append("config - ");
                  if (!"please dont crack my plugin".equals("please get a girlfriend and stop cracking plugins")) {
                        ;
                  }

                  var10000 = var10000.append(var1[var1.length - ((0 & 1002562071) << 3 >>> 3 ^ 1)]).append(" successfully saved");
                  if (!"yo mama name maurice".equals("please dont crack my plugin")) {
                        ;
                  }

                  ChatUtil.sendChatMessage(var10000.toString());
            }

      }
}
