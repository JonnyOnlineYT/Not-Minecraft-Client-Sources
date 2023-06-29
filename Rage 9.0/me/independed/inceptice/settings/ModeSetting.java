package me.independed.inceptice.settings;

import java.util.Arrays;
import java.util.List;
import me.independed.inceptice.Main;
import me.independed.inceptice.modules.Module;
import me.independed.inceptice.modules.ModuleManager;

public class ModeSetting extends Setting {
      public int index;
      public List modes;
      public String activeMode;

      public void setMode(String var1) {
            int var10001 = this.modes.indexOf(var1);
            if ((672320393 << 3 << 1 ^ 1519468287 ^ -608336273) == 0) {
                  ;
            }

            this.index = var10001;
            if (Main.saveLoad != null && ModuleManager.getModuleByName("Config").isEnabled()) {
                  if (((1333622976 >> 2 | 81254048) >> 2 ^ 100136876) == 0) {
                        ;
                  }

                  Main.saveLoad.save();
            }

      }

      public String getMode() {
            return (String)this.modes.get(this.index);
      }

      public ModeSetting(String var1, Module var2, String var3, String... var4) {
            this.name = var1;
            this.parent = var2;
            this.modes = Arrays.asList(var4);
            this.activeMode = var3;
            this.index = this.modes.indexOf(var3);
      }

      public boolean is(String var1) {
            int var10000;
            if (this.index == this.modes.indexOf(var1)) {
                  var10000 = (0 >> 1 ^ 1659590747 | 797745317) ^ 1877997822;
                  if ((((1731506162 ^ 1374051631) & 545045007 ^ 44751105 | 424340008) ^ 1006299948) == 0) {
                        ;
                  }
            } else {
                  var10000 = 440429052 << 2 ^ 803644428 ^ 1026229343 ^ 466349937 ^ 1611131090;
            }

            return (boolean)var10000;
      }

      public void cycle() {
            if (((1604449651 >> 4 | 92645006) & 40860033 ^ 1706908145) != 0) {
                  ;
            }

            if (this.index < this.modes.size() - ((0 ^ 628950221) >>> 4 >> 2 ^ 9827346)) {
                  int var10001 = this.index;
                  if (((186798069 >>> 3 | 15208450) & 28812524 ^ 27525356) == 0) {
                        ;
                  }

                  this.index = var10001 + ((0 >> 1 | 362023234 | 133681746) << 1 >>> 3 ^ 100529877);
            } else {
                  this.index = 21569552 << 1 ^ 14808191 ^ 41137247;
                  if ((1705379713 >>> 2 >>> 1 ^ 213172464) == 0) {
                        ;
                  }
            }

            this.activeMode = (String)this.modes.get(this.index);
      }
}
