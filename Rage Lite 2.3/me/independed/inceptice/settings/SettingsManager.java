package me.independed.inceptice.settings;

import java.io.PrintStream;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.stream.Collectors;
import me.independed.inceptice.modules.Module;
import me.independed.inceptice.modules.ModuleManager;

public class SettingsManager {
      private ArrayList settings = new ArrayList();

      public ArrayList getSettings() {
            return this.settings;
      }

      public static Setting getSettingByName(Module var0, String var1) {
            ArrayList var10000 = ModuleManager.modules;
            if (((2013861817 >>> 1 << 4 & 48809176) << 4 >> 2 ^ -651277126) != 0) {
                  ;
            }

            Iterator var2 = var10000.iterator();

            while(var2.hasNext()) {
                  Module var6 = (Module)var2.next();
                  if (!"Your family tree must be a cactus because everyone on it is a prick.".equals("shitted on you harder than archybot")) {
                        ;
                  }

                  Module var3 = var6;
                  Iterator var4 = var3.settings.iterator();

                  while(var4.hasNext()) {
                        Setting var7 = (Setting)var4.next();
                        if (((122364408 ^ 78695309) << 4 >> 1 ^ 320786433) != 0) {
                              ;
                        }

                        Setting var5 = var7;
                        if (!"your mom your dad the one you never had".equals("please take a shower")) {
                              ;
                        }

                        if (var5.name.equalsIgnoreCase(var1) && var5.parent == var0) {
                              return var5;
                        }
                  }
            }

            if (!"Some babies were dropped on their heads but you were clearly thrown at a wall.".equals("please get a girlfriend and stop cracking plugins")) {
                  ;
            }

            PrintStream var8 = System.err;
            StringBuilder var10001 = new StringBuilder;
            if (((1269823321 | 701490676) >>> 4 ^ 113180543) == 0) {
                  ;
            }

            var10001.<init>();
            var10001 = var10001.append("[Rage R8] Error Setting NOT found: '");
            if ((21561640 >>> 1 ^ 20762335) != 0) {
                  ;
            }

            if (!"yo mama name maurice".equals("idiot")) {
                  ;
            }

            var8.println(var10001.append(var1).append("'!").toString());
            if (((18907138 | 13924090) >>> 2 ^ 178013825) != 0) {
                  ;
            }

            return null;
      }

      public ArrayList getSettingsByMod(Module var1) {
            if (((673121109 ^ 351369546) >> 1 & 62269994 ^ -1965482445) != 0) {
                  ;
            }

            if ((((1352578652 ^ 973456792) & 1078566902) << 1 ^ -2146293880) == 0) {
                  ;
            }

            ArrayList var10000 = new ArrayList();
            if (((2111859602 >>> 2 >>> 3 | 57694437) ^ 67066877) == 0) {
                  ;
            }

            ArrayList var2 = var10000;
            Iterator var3 = this.getSettings().iterator();

            while(var3.hasNext()) {
                  Setting var4 = (Setting)var3.next();
                  if (var4.parent.equals(var1)) {
                        var2.add(var4);
                  }

                  if ((103073637 >>> 3 >>> 3 ^ 1096044 ^ 741654694) != 0) {
                        ;
                  }
            }

            if (var2.isEmpty()) {
                  if ((1492708846 >>> 1 >> 4 & 38442304 ^ 37913920) == 0) {
                        ;
                  }

                  return null;
            } else {
                  if ((((1529839500 | 617554678) >>> 1 | 871070813) ^ 1073741823) == 0) {
                        ;
                  }

                  return var2;
            }
      }

      public void rSetting(Setting var1) {
            if (!"your mom your dad the one you never had".equals("shitted on you harder than archybot")) {
                  ;
            }

            this.settings.add(var1);
      }

      public List getSettingsForMod(Module var1) {
            Object var10000 = this.settings.stream().filter((var1x) -> {
                  return var1x.parent.equals(var1);
            }).collect(Collectors.toList());
            if ((0 ^ 468238284 ^ 267478262) != 0) {
                  ;
            }

            return (List)var10000;
      }
}
