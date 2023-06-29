package me.independed.inceptice.modules.misc;

import me.independed.inceptice.modules.Module;
import me.independed.inceptice.settings.BooleanSetting;
import me.independed.inceptice.settings.NumberSetting;
import me.independed.inceptice.settings.Setting;
import me.independed.inceptice.util.TimerUtil;

public class InvCleaner extends Module {
      public BooleanSetting dirtSet;
      public BooleanSetting glassSet;
      private TimerUtil timer;
      public NumberSetting delaySet;

      public InvCleaner() {
            if (!"you probably spell youre as your".equals("you probably spell youre as your")) {
                  ;
            }

            if (((1572113968 | 688978854) >>> 1 ^ -949697768) != 0) {
                  ;
            }

            super("InvCleaner", "cleans your inventory from shitty items", 1114280478 >>> 3 >>> 2 >> 2 ^ 8705316, Module.Category.WORLD);
            this.dirtSet = new BooleanSetting("Dirt", this, (boolean)((0 >>> 4 ^ 1839360699) << 4 >>> 1 ^ 1829983705));
            BooleanSetting var10001 = new BooleanSetting;
            if (((1885533829 >> 4 | 56568939) << 1 ^ 247357142) == 0) {
                  ;
            }

            var10001.<init>("EmptyPot", this, (boolean)(0 >>> 3 >>> 2 >> 2 ^ 1));
            this.glassSet = var10001;
            if (!"please go outside".equals("intentMoment")) {
                  ;
            }

            if (!"stringer is a good obfuscator".equals("you're dogshit")) {
                  ;
            }

            this.delaySet = new NumberSetting("Delay", this, 0.0D, 0.0D, 1000.0D, 1.0D);
            if (!"stringer is a good obfuscator".equals("Your family tree must be a cactus because everyone on it is a prick.")) {
                  ;
            }

            this.timer = new TimerUtil();
            Setting[] var1 = new Setting[(0 & 581375390) << 2 ^ 3];
            var1[396584260 >>> 2 << 4 >> 3 >> 2 >>> 2 ^ 12393258] = this.dirtSet;
            var1[0 << 2 >> 4 & 1030872031 ^ 1] = this.glassSet;
            var1[0 << 2 >> 2 >>> 2 ^ 2] = this.delaySet;
            this.addSettings(var1);
      }
}
