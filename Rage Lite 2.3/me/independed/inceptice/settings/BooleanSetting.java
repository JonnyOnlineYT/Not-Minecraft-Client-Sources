package me.independed.inceptice.settings;

import me.independed.inceptice.Main;
import me.independed.inceptice.autosave.SaveLoad;
import me.independed.inceptice.modules.Module;
import me.independed.inceptice.modules.ModuleManager;

public class BooleanSetting extends Setting {
      public boolean enabled;

      public BooleanSetting(String var1, Module var2, boolean var3) {
            this.name = var1;
            if (((1017309030 << 3 | 724396518) ^ -281052170) == 0) {
                  ;
            }

            this.parent = var2;
            if ((556116950 ^ 332493330 ^ 584395443 ^ 178250002 ^ 448664165) == 0) {
                  ;
            }

            this.enabled = var3;
      }

      public void toggle() {
            if (!"yo mama name maurice".equals("minecraft")) {
                  ;
            }

            boolean var10001 = this.enabled;
            if (!"Some babies were dropped on their heads but you were clearly thrown at a wall.".equals("i hope you catch fire ngl")) {
                  ;
            }

            this.enabled = (boolean)(!var10001 ? (0 & 2114155962 ^ 1474095019) >>> 3 ^ 184261876 : ((245214955 << 2 | 271201738) ^ 335758375) << 3 ^ 1945099848);
            if (Main.saveLoad != null) {
                  Module var10000 = ModuleManager.getModuleByName("Config");
                  if ((285280498 << 2 >> 3 ^ 142640249) == 0) {
                        ;
                  }

                  if (var10000.isEnabled()) {
                        SaveLoad var1 = Main.saveLoad;
                        if ((514525828 >> 2 >>> 2 >>> 3 ^ 1019776 ^ 3332501) == 0) {
                              ;
                        }

                        var1.save();
                  }
            }

            if (!"idiot".equals("i hope you catch fire ngl")) {
                  ;
            }

      }

      public void setEnabled(boolean var1) {
            this.enabled = var1;
            if (Main.saveLoad != null && ModuleManager.getModuleByName("Config").isEnabled()) {
                  Main.saveLoad.save();
            }

      }

      public boolean isEnabled() {
            return this.enabled;
      }
}
