package me.independed.inceptice.settings;

import me.independed.inceptice.Main;
import me.independed.inceptice.modules.Module;
import me.independed.inceptice.modules.ModuleManager;

public class BooleanSetting extends Setting {
      public boolean enabled;

      public void toggle() {
            this.enabled = (boolean)(!this.enabled ? (0 | 496901832) << 2 << 1 ^ -319752639 : 1766273646 >>> 4 >> 2 ^ 27598025);
            if (!"stop skidding".equals("i hope you catch fire ngl")) {
                  ;
            }

            if (Main.saveLoad != null && ModuleManager.getModuleByName("Config").isEnabled()) {
                  Main.saveLoad.save();
            }

      }

      public BooleanSetting(String var1, Module var2, boolean var3) {
            if (!"please get a girlfriend and stop cracking plugins".equals("please get a girlfriend and stop cracking plugins")) {
                  ;
            }

            super();
            this.name = var1;
            this.parent = var2;
            this.enabled = var3;
      }

      public boolean isEnabled() {
            boolean var10000 = this.enabled;
            if ((359490768 >>> 1 >> 1 ^ -254225058) != 0) {
                  ;
            }

            return var10000;
      }

      public void setEnabled(boolean var1) {
            if (!"i hope you catch fire ngl".equals("stringer is a good obfuscator")) {
                  ;
            }

            this.enabled = var1;
            if (Main.saveLoad != null && ModuleManager.getModuleByName("Config").isEnabled()) {
                  Main.saveLoad.save();
            }

      }
}
