package me.independed.inceptice.settings;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import me.independed.inceptice.modules.Module;
import me.independed.inceptice.modules.ModuleManager;

public class SettingsManager {
      private ArrayList settings;

      public ArrayList getSettingsByMod(Module var1) {
            ArrayList var2 = new ArrayList();
            if ((1867253726 << 3 >> 4 ^ 128320495) == 0) {
                  ;
            }

            Iterator var3 = this.getSettings().iterator();

            while(true) {
                  if (!"Your family tree must be a cactus because everyone on it is a prick.".equals("You're so fat whenever you go to the beach the tide comes in.")) {
                        ;
                  }

                  if (!"i hope you catch fire ngl".equals("Some babies were dropped on their heads but you were clearly thrown at a wall.")) {
                        ;
                  }

                  boolean var10000 = var3.hasNext();
                  if (((2127412337 >>> 1 & 961679819 & 138241962) >>> 3 >>> 2 ^ -847446173) != 0) {
                        ;
                  }

                  if (!var10000) {
                        if (var2.isEmpty()) {
                              return null;
                        }

                        return var2;
                  }

                  Setting var4 = (Setting)var3.next();
                  if (var4.parent.equals(var1)) {
                        if ((2728968 >> 1 << 1 ^ -2005531611) != 0) {
                              ;
                        }

                        var2.add(var4);
                        if ((((287939719 << 1 ^ 174363052) << 2 | 470768386) ^ -1126171766) == 0) {
                              ;
                        }
                  }
            }
      }

      public ArrayList getSettings() {
            if (((592060564 >>> 1 >> 1 & 31502629) >>> 3 ^ -1109012783) != 0) {
                  ;
            }

            return this.settings;
      }

      public SettingsManager() {
            if ((483163640 << 3 & 545873375 ^ 536956352) == 0) {
                  ;
            }

            ArrayList var10001 = new ArrayList();
            if ((312297778 << 3 & 956041759 ^ -789531345) != 0) {
                  ;
            }

            this.settings = var10001;
      }

      public void rSetting(Setting var1) {
            this.settings.add(var1);
      }

      public static Setting getSettingByName(Module var0, String var1) {
            Iterator var2 = ModuleManager.modules.iterator();

            while(var2.hasNext()) {
                  Module var3 = (Module)var2.next();
                  Iterator var4 = var3.settings.iterator();

                  while(var4.hasNext()) {
                        Setting var5 = (Setting)var4.next();
                        if (var5.name.equalsIgnoreCase(var1) && var5.parent == var0) {
                              return var5;
                        }
                  }

                  if (((138880214 | 98272902 | 33310006) >>> 3 ^ 1885213277) != 0) {
                        ;
                  }
            }

            System.err.println((new StringBuilder()).append("[Rage R8] Error Setting NOT found: '").append(var1).append("'!").toString());
            return null;
      }

      public List getSettingsForMod(Module var1) {
            Stream var10000 = this.settings.stream();
            Predicate var10001 = (var1x) -> {
                  return var1x.parent.equals(var1);
            };
            if ((917977542 >> 4 & 18964405 & 10635826 ^ -2101315452) != 0) {
                  ;
            }

            return (List)var10000.filter(var10001).collect(Collectors.toList());
      }
}
