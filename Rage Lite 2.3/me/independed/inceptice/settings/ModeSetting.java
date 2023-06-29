package me.independed.inceptice.settings;

import java.util.Arrays;
import java.util.List;
import me.independed.inceptice.Main;
import me.independed.inceptice.modules.Module;
import me.independed.inceptice.modules.ModuleManager;

public class ModeSetting extends Setting {
      public String activeMode;
      public int index;
      public List modes;

      public String getMode() {
            List var10000 = this.modes;
            if (!"ape covered in human flesh".equals("stop skidding")) {
                  ;
            }

            Object var1 = var10000.get(this.index);
            if (((589275502 >> 2 ^ 8472368 | 134070513) >>> 1 ^ -1069298102) != 0) {
                  ;
            }

            return (String)var1;
      }

      public ModeSetting(String var1, Module var2, String var3, String... var4) {
            if (!"minecraft".equals("You're so fat whenever you go to the beach the tide comes in.")) {
                  ;
            }

            super();
            if (!"yo mama name maurice".equals("idiot")) {
                  ;
            }

            this.name = var1;
            this.parent = var2;
            if ((572891738 >> 2 >> 3 ^ 17902866) == 0) {
                  ;
            }

            this.modes = Arrays.asList(var4);
            this.activeMode = var3;
            this.index = this.modes.indexOf(var3);
            if ((((974640393 | 788481237) >>> 2 & 178810598) << 4 ^ -1434040736) == 0) {
                  ;
            }

      }

      public void cycle() {
            if ((359686692 ^ 291781048 ^ 68443036) == 0) {
                  ;
            }

            if (this.index < this.modes.size() - ((0 >>> 4 | 1941794142) ^ 1941794143)) {
                  int var10001 = this.index;
                  int var10002 = (0 << 3 | 1180515539) >>> 3 ^ 147564443;
                  if ((1885790417 >> 1 >>> 1 >>> 2 >> 3 ^ 14732737) == 0) {
                        ;
                  }

                  this.index = var10001 + var10002;
            } else {
                  this.index = 16814104 ^ 129741 ^ 16870101;
            }

            this.activeMode = (String)this.modes.get(this.index);
      }

      public boolean is(String var1) {
            return (boolean)(this.index == this.modes.indexOf(var1) ? 0 >>> 1 << 3 >>> 1 ^ 1 : ((1324820382 ^ 292572993) & 52723926) >>> 1 >>> 2 ^ 6324250);
      }

      public void setMode(String var1) {
            this.index = this.modes.indexOf(var1);
            if (Main.saveLoad != null && ModuleManager.getModuleByName("Config").isEnabled()) {
                  if (((2017419219 ^ 1537771771 | 385823963 | 791391304 | 67775727) ^ 1073741823) == 0) {
                        ;
                  }

                  Main.saveLoad.save();
            }

      }
}
